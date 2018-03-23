package priv.starfish.common.tool;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.*;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import priv.starfish.common.annotation.*;
import priv.starfish.common.base.AnnotatedClassScanner;
import priv.starfish.common.base.Converter;
import priv.starfish.common.config.PropertyConfigurer;
import priv.starfish.common.dag.DirectedGraph;
import priv.starfish.common.jdbc.*;
import priv.starfish.common.model.Couple;
import priv.starfish.common.util.BoolUtil;
import priv.starfish.common.util.CollectionUtil;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.TypeUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * 根据实体类的配置更新数据库表定义（只增加表和表的列）
 * 
 * @author koqiui
 * @date 2015-03-21
 */
public class MySqlDbTool {
	private final static Log logger = LogFactory.getLog(MySqlDbTool.class);

	//
	private static class IdMeta {
		public String name;
		public int type;
		public String typeName;
		@JsonIgnore
		public Class<?> javaType;
		public String fieldName;
		public boolean auto;
		public int length;
		public String desc;
	}

	private static class ColumnMeta {
		public String name;
		public int type;
		public String typeName;
		@JsonIgnore
		public Class<?> javaType;
		public String fieldName;
		public boolean nullable;
		public boolean updatable;
		public String defaultValue;
		public int length;
		public int precision;
		public int scale;
		public String desc;
	}

	private static class UniqueKeyMeta {
		public String name;
		public String[] columnNames;
	}

	private static class ForeignKeyMeta {
		public String name;
		//
		@JsonIgnore
		public Class<?> entityClass;
		@SuppressWarnings("unused")
		public String fieldName;
		public String tableName;
		public String columnName;
		//
		@JsonIgnore
		public Class<?> refEntityClass;
		public String refFieldName;
		public String refTableName;
		public String refColumnName;
	}

	private static class TableMeta {
		public String name;
		public String desc;
		@JsonIgnore
		public Class<?> javaType;
		public List<UniqueKeyMeta> uniqueKeyMetaList;
		//
		public IdMeta idMeta;
		public List<ColumnMeta> columnMetaList;
		public List<ForeignKeyMeta> foreignKeyMetaList;
	}

	private static UpdateBuilder.SetClauseBuilder ifSetClauseBuilder = new UpdateBuilder.SetClauseBuilder() {
		private Map<String, String> col2FieldNames = new HashMap<String, String>();

		@Override
		public void filterItem(String colName, Object value, Map<String, Object> valuePairs, UpdateBuilder updateBuilder, IPlaceHolderMaker placeHolderMaker) {
			if (value != null && value instanceof String) {
				String valueStr = (String) value;
				if (SqlBuilder.JdbcParamPlaceHolder.equals(valueStr)) {
					valueStr = colName;
				}
				if (!placeHolderMaker.isPlaceHolder(valueStr)) {
					valuePairs.put(colName, placeHolderMaker.makePlaceHolder(valueStr));
					//
					this.col2FieldNames.put(colName, valueStr);
				}
			}
		}

		@Override
		public String finish(Map<String, Object> itemPairs, UpdateBuilder updateBuilder) {
			List<String> setStrList = new ArrayList<String>();
			//
			List<String> colNames = new ArrayList<String>(itemPairs.keySet());
			int colCount = colNames.size();
			for (int i = 0; i < colCount; i++) {
				String colName = colNames.get(i);
				String fldName = this.col2FieldNames.get(colName);
				Object value = itemPairs.get(colName);
				String setStr = null;
				if (!updateBuilder.isPlaceHolder(value)) {
					setStr = colName + " = " + UpdateBuilder.toValueStr(value);
				} else {
					setStr = colName + " = " + value.toString();
				}
				if (i < colCount - 1) {
					setStr = setStr + ",";
				}
				if (fldName != null) {
					setStr = "<if test=\"" + fldName + " != null\">\n" + "    " + setStr + "\n</if>";
				}
				if (i > 0) {
					setStr = "    " + setStr;
				}
				setStrList.add(setStr);
			}
			//
			this.col2FieldNames.clear();
			//
			return "<set><trim suffixOverrides=\",\">\n" + StrUtil.join(setStrList, "\n") + "\n</trim></set>";
		}
	};

	private static ColumnMeta findColumnMetaByColName(TableMeta tableMeta, String colName) {
		ColumnMeta colMeta = null;
		for (ColumnMeta columnMeta : tableMeta.columnMetaList) {
			String columnName = columnMeta.name;
			if (columnName.equalsIgnoreCase(colName)) {
				colMeta = columnMeta;
				break;
			}
		}
		return colMeta;
	}

	private static Object findColumnMetaByFieldName(TableMeta tableMeta, String fieldName) {
		IdMeta idMeta = tableMeta.idMeta;
		if (idMeta != null && idMeta.fieldName.equals(fieldName)) {
			return idMeta;
		}
		List<ColumnMeta> columnMetaList = tableMeta.columnMetaList;
		for (ColumnMeta columnMeta : columnMetaList) {
			if (columnMeta.fieldName.equals(fieldName)) {
				return columnMeta;
			}
		}
		throw new RuntimeException("在实体" + tableMeta.javaType.getName() + "中没有找到指定的唯一键约束字段[" + fieldName + "]");
	}

	private static TableMeta extractTableMeta(Class<?> entityClass) {
		final Table table = entityClass.getAnnotation(Table.class);
		if (table == null) {
			return null;
		}
		TableMeta tableMeta = new TableMeta();
		//
		tableMeta.name = table.name();
		if (!StrUtil.hasText(tableMeta.name)) {
			tableMeta.name = StrUtil.toJavaVariableName(entityClass.getSimpleName());
		}
		//
		tableMeta.desc = table.desc();
		//
		tableMeta.javaType = entityClass;
		//
		List<ForeignKeyMeta> foreignKeyMetaList = new ArrayList<ForeignKeyMeta>();
		tableMeta.foreignKeyMetaList = foreignKeyMetaList;
		List<ColumnMeta> columnMetaList = new ArrayList<ColumnMeta>();
		tableMeta.columnMetaList = columnMetaList;
		Field[] fields = entityClass.getDeclaredFields();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			ForeignKey foreignKey = field.getAnnotation(ForeignKey.class);
			if (column != null) {
				ColumnMeta columnMeta = new ColumnMeta();
				columnMetaList.add(columnMeta);
				columnMeta.name = column.name();
				if (!StrUtil.hasText(columnMeta.name)) {
					columnMeta.name = field.getName();
				}
				columnMeta.type = column.type();
				columnMeta.javaType = field.getType();
				if (columnMeta.javaType.isPrimitive()) {
					columnMeta.javaType = TypeUtil.getWrapperType(columnMeta.javaType);
				}
				columnMeta.fieldName = field.getName();
				columnMeta.nullable = column.nullable();
				columnMeta.updatable = column.updatable();
				columnMeta.defaultValue = column.defaultValue();
				columnMeta.length = column.length();
				columnMeta.precision = column.precision();
				columnMeta.scale = column.scale();
				columnMeta.desc = column.desc();
				//
				if (foreignKey != null) {
					ForeignKeyMeta foreignKeyMeta = new ForeignKeyMeta();
					foreignKeyMeta.name = foreignKey.name();
					foreignKeyMeta.entityClass = tableMeta.javaType;
					foreignKeyMeta.fieldName = columnMeta.fieldName;
					foreignKeyMeta.tableName = tableMeta.name;
					foreignKeyMeta.columnName = columnMeta.name;
					//
					foreignKeyMeta.refEntityClass = foreignKey.refEntityClass();
					foreignKeyMeta.refFieldName = foreignKey.refFieldName();
					foreignKeyMetaList.add(foreignKeyMeta);
				}
			} else {
				Id id = field.getAnnotation(Id.class);
				if (id != null) {
					IdMeta idMeta = new IdMeta();
					tableMeta.idMeta = idMeta;
					idMeta.name = id.name();
					if (!StrUtil.hasText(idMeta.name)) {
						idMeta.name = field.getName();
					}
					idMeta.type = id.type();
					idMeta.javaType = field.getType();
					if (idMeta.javaType.isPrimitive()) {
						idMeta.javaType = TypeUtil.getWrapperType(idMeta.javaType);
					}
					idMeta.fieldName = field.getName();
					idMeta.auto = id.auto();
					idMeta.length = id.length();
					idMeta.desc = id.desc();
					//
					if (foreignKey != null) {
						ForeignKeyMeta foreignKeyMeta = new ForeignKeyMeta();
						foreignKeyMeta.name = foreignKey.name();
						foreignKeyMeta.entityClass = tableMeta.javaType;
						foreignKeyMeta.fieldName = idMeta.fieldName;
						foreignKeyMeta.tableName = tableMeta.name;
						foreignKeyMeta.columnName = idMeta.name;
						//
						foreignKeyMeta.refEntityClass = foreignKey.refEntityClass();
						foreignKeyMeta.refFieldName = foreignKey.refFieldName();
						foreignKeyMetaList.add(foreignKeyMeta);
					}
				}
			}
		}
		//
		UniqueConstraint[] uniqueConstraints = table.uniqueConstraints();
		List<UniqueKeyMeta> uniqueKeyMetaList = new ArrayList<UniqueKeyMeta>();
		tableMeta.uniqueKeyMetaList = uniqueKeyMetaList;
		if (uniqueConstraints != null && uniqueConstraints.length > 0) {
			for (UniqueConstraint uniqueConstraint : uniqueConstraints) {
				UniqueKeyMeta uniqueKeyMeta = new UniqueKeyMeta();
				uniqueKeyMetaList.add(uniqueKeyMeta);
				String[] fieldNames = uniqueConstraint.fieldNames();
				uniqueKeyMeta.columnNames = new String[fieldNames.length];
				for (int i = 0; i < fieldNames.length; i++) {
					String fieldName = fieldNames[i];
					Object colMeta = findColumnMetaByFieldName(tableMeta, fieldName);
					uniqueKeyMeta.columnNames[i] = colMeta instanceof IdMeta ? ((IdMeta) colMeta).name : ((ColumnMeta) colMeta).name;
				}
				String ukName = uniqueConstraint.name();
				if (!StrUtil.hasText(ukName)) {
					List<String> tmpColNames = new ArrayList<String>();//
					for (String colName : uniqueKeyMeta.columnNames) {
						tmpColNames.add(unwrapName(colName));
					}
					ukName = "UK_" + StrUtil.join(tmpColNames, "_");
				}
				uniqueKeyMeta.name = ukName;
			}
		}
		//
		return tableMeta;
	}

	private static Map<Class<?>, Integer> javaType2SqlTypeNameMap;
	private static String OTHER_COL_TYPE_NAME = "<OTHER>";
	private static Map<Integer, String> sqlType2ColTypeNameMap;
	private static Map<String, String> colType2sqlTypeNameMap;

	static {
		javaType2SqlTypeNameMap = new HashMap<Class<?>, Integer>();
		javaType2SqlTypeNameMap.put(String.class, Types.VARCHAR);
		javaType2SqlTypeNameMap.put(Character.class, Types.CHAR);

		javaType2SqlTypeNameMap.put(Integer.class, Types.INTEGER);
		javaType2SqlTypeNameMap.put(Long.class, Types.BIGINT);
		javaType2SqlTypeNameMap.put(BigInteger.class, Types.BIGINT);

		javaType2SqlTypeNameMap.put(Float.class, Types.FLOAT);
		javaType2SqlTypeNameMap.put(Double.class, Types.DOUBLE);
		javaType2SqlTypeNameMap.put(BigDecimal.class, Types.DECIMAL);
		javaType2SqlTypeNameMap.put(Number.class, Types.NUMERIC);

		javaType2SqlTypeNameMap.put(Boolean.class, Types.BIT);

		javaType2SqlTypeNameMap.put(Date.class, Types.TIMESTAMP);

		javaType2SqlTypeNameMap.put(byte[].class, Types.VARBINARY);
		// ----------------------------------------------------------
		sqlType2ColTypeNameMap = new HashMap<Integer, String>();

		sqlType2ColTypeNameMap.put(Types.VARCHAR, "VARCHAR");// VARCHAR(length)
		sqlType2ColTypeNameMap.put(Types.CHAR, "CHAR");// CHAR(length)

		sqlType2ColTypeNameMap.put(Types.INTEGER, "INTEGER");// INTEGER [(length)]
		sqlType2ColTypeNameMap.put(Types.BIGINT, "BIGINT");// BIGINT [(length)]

		sqlType2ColTypeNameMap.put(Types.FLOAT, "FLOAT");// FLOAT [(length,decimals)]
		sqlType2ColTypeNameMap.put(Types.DOUBLE, "DOUBLE");// DOUBLE [(length,decimals)]
		sqlType2ColTypeNameMap.put(Types.DECIMAL, "DECIMAL"); // DECIMAL [(length,decimals)]
		sqlType2ColTypeNameMap.put(Types.NUMERIC, "NUMERIC"); // NUMERIC [(length,decimals)]

		sqlType2ColTypeNameMap.put(Types.BIT, "BIT");
		sqlType2ColTypeNameMap.put(Types.BOOLEAN, "BOOLEAN");

		sqlType2ColTypeNameMap.put(Types.DATE, "DATE");// DATE
		sqlType2ColTypeNameMap.put(Types.TIME, "TIME");// TIME
		sqlType2ColTypeNameMap.put(Types.TIMESTAMP, "TIMESTAMP");// TIMESTAMP
		//
		sqlType2ColTypeNameMap.put(Types.VARBINARY, "VARBINARY");

		sqlType2ColTypeNameMap.put(Types.CLOB, "TEXT");// TEXT
		sqlType2ColTypeNameMap.put(Types.BLOB, "BLOB");// BLOB

		sqlType2ColTypeNameMap.put(Types.LONGVARCHAR, "LONGTEXT");

		sqlType2ColTypeNameMap.put(Types.LONGVARBINARY, "LONGBLOB");

		sqlType2ColTypeNameMap.put(Types.OTHER, OTHER_COL_TYPE_NAME);

		//
		colType2sqlTypeNameMap = new HashMap<String, String>();
		colType2sqlTypeNameMap.put("BIT", "BOOLEAN");
		colType2sqlTypeNameMap.put("TEXT", "CLOB");
		colType2sqlTypeNameMap.put("LONGTEXT", "LONGVARCHAR");
		colType2sqlTypeNameMap.put("LONGBLOB", "LONGVARBINARY");
		colType2sqlTypeNameMap.put("ENUM", "VARCHAR");
		// ENUM(value1,value2,value3,...)
		// SET(value1,value2,value3,...)
	}

	private static Integer getSqlType4JavaType(Class<?> javaType) {
		return javaType2SqlTypeNameMap.get(javaType);
	}

	private static String getColTypeName4SqlType(int sqlType) {
		return sqlType2ColTypeNameMap.get(sqlType);
	}

	private static String getColType4sqlType(String colType) {
		return colType2sqlTypeNameMap.get(colType);
	}

	// column_definition::
	// col_name type [NOT NULL | NULL] [DEFAULT default_value]
	// [AUTO_INCREMENT] [UNIQUE [KEY] | [PRIMARY] KEY]
	// [COMMENT 'string'] [reference_definition]

	// reference_definition::
	// REFERENCES tbl_name [(index_col_name,...)]
	// precision 数据长度,scale 小数长度

	//
	private static List<String> createUKDefination(TableMeta tableMeta, UniqueKeyMeta ukMeta, boolean alterMode) {
		List<String> defParts = new ArrayList<String>();
		if (alterMode) {
			defParts.add("ALTER TABLE " + tableMeta.name);
			defParts.add("ADD CONSTRAINT " + ukMeta.name);
			defParts.add("UNIQUE");
			defParts.add("(" + StrUtil.join(ukMeta.columnNames, ",") + ");");
		} else {
			defParts.add("UNIQUE");
			defParts.add(ukMeta.name);
			defParts.add("(" + StrUtil.join(ukMeta.columnNames, ",") + ")");
		}
		return defParts;
	}

	private static List<String> createFKDefination(TableMeta tableMeta, ForeignKeyMeta fkMeta) {
		List<String> defParts = new ArrayList<String>();
		defParts.add("ALTER TABLE " + fkMeta.tableName);
		defParts.add("ADD CONSTRAINT " + fkMeta.name);
		defParts.add("FOREIGN KEY");
		defParts.add("(" + fkMeta.columnName + ")");
		defParts.add("REFERENCES " + fkMeta.refTableName);
		defParts.add("(" + fkMeta.refColumnName + ");");
		return defParts;
	}

	private static List<String> createIdDefination(TableMeta tableMeta, IdMeta idMeta) {
		List<String> defParts = new ArrayList<String>();
		defParts.add(idMeta.name);
		if (idMeta.type == Types.OTHER) {
			Integer sqlType = getSqlType4JavaType(idMeta.javaType);
			if (sqlType != null) {
				idMeta.type = sqlType;
			}
		}
		String colTypeName = getColTypeName4SqlType(idMeta.type);
		if (colTypeName == null) {
			throw new NullPointerException(tableMeta.javaType.getName() + "列定义：[" + tableMeta.name + "." + idMeta.name + "中指定的type参数没有对应的mysql列类型名称!");
		}
		idMeta.typeName = colTypeName;
		defParts.add(colTypeName);
		if (idMeta.length > 0) {
			defParts.add("(" + idMeta.length + ")");
		}
		defParts.add("NOT NULL");
		if (idMeta.auto) {
			defParts.add("AUTO_INCREMENT");
		}
		defParts.add("PRIMARY KEY");
		if (StrUtil.hasText(idMeta.desc)) {
			defParts.add("COMMENT " + SqlBuilder.strVal(idMeta.desc));
		}

		return defParts;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List<String> createColDefination(TableMeta tableMeta, ColumnMeta colMeta) {
		List<String> defParts = new ArrayList<String>();
		defParts.add(colMeta.name);
		if (colMeta.type == Types.OTHER) {
			Integer sqlType = getSqlType4JavaType(colMeta.javaType);
			if (sqlType != null) {
				colMeta.type = sqlType;
			}
		}
		String colTypeName = getColTypeName4SqlType(colMeta.type);
		if (colTypeName == null) {
			throw new NullPointerException(tableMeta.javaType.getName() + "列定义：[" + tableMeta.name + "." + colMeta.name + "中指定的type参数没有对应的mysql列类型名称!");
		}
		if (OTHER_COL_TYPE_NAME.equals(colTypeName)) {
			if (colMeta.javaType.isEnum()) {
				colTypeName = "ENUM";
				colMeta.typeName = colTypeName;
				Class<? extends Enum> enumClass = (Class<? extends Enum>) colMeta.javaType;
				Enum[] enumElems = enumClass.getEnumConstants();
				List<String> enumNames = new ArrayList<String>();
				for (Enum enumElem : enumElems) {
					enumNames.add(SqlBuilder.strVal(enumElem.name()));
				}
				//
				defParts.add(colTypeName);
				defParts.add("(" + StrUtil.join(enumNames, ",") + ")");
			} else {
				System.err.println(tableMeta.javaType.getName() + "列定义：[" + tableMeta.name + "." + colMeta.name + "中指定的type参数没有对应的mysql列类型!");
				return null;
			}
		} else {
			colMeta.typeName = colTypeName;
			defParts.add(colTypeName);
			if (colTypeName.equals("VARCHAR")) {
				if (colMeta.length <= 0) {
					colMeta.length = 255;
				}
			} else if (colTypeName.equals("CHAR")) {
				if (colMeta.length <= 0) {
					colMeta.length = 1;
				}
			} else if (colTypeName.equals("BIT")) {
				if (colMeta.length <= 0) {
					colMeta.length = 1;
				}
			}
			if (colMeta.length > 0) {
				defParts.add("(" + colMeta.length + ")");
			} else if (colMeta.precision > 0) {
				String tmpStr = "(" + colMeta.precision;
				if (colMeta.scale > 0) {
					tmpStr += "," + colMeta.scale;
				}
				tmpStr += ")";
				defParts.add(tmpStr);
			}
		}
		defParts.add(colMeta.nullable ? "NULL" : "NOT NULL");
		if (StrUtil.hasText(colMeta.defaultValue)) {
			defParts.add("DEFAULT " + colMeta.defaultValue);
		}
		if (StrUtil.hasText(colMeta.desc)) {
			defParts.add("COMMENT " + SqlBuilder.strVal(colMeta.desc));
		}

		return defParts;
	}

	private static String createTableDefination(TableMeta tableMeta) {
		List<String> defParts = new ArrayList<String>();
		defParts.add("CREATE TABLE " + tableMeta.name + " (\n");
		//
		List<String> defItems = new ArrayList<String>();
		// id
		List<String> idDefination = createIdDefination(tableMeta, tableMeta.idMeta);
		defItems.add("\t" + StrUtil.join(idDefination, " "));
		// col
		for (ColumnMeta columnMeta : tableMeta.columnMetaList) {
			List<String> colDefination = createColDefination(tableMeta, columnMeta);
			if (colDefination != null) {
				defItems.add("\t" + StrUtil.join(colDefination, " "));
			}
		}
		// uk
		for (UniqueKeyMeta ukMeta : tableMeta.uniqueKeyMetaList) {
			List<String> ukDefination = createUKDefination(tableMeta, ukMeta, false);

			defItems.add("\t" + StrUtil.join(ukDefination, " "));
		}

		defParts.add(StrUtil.join(defItems, ",\n"));
		//
		defParts.add("\n)");
		if (StrUtil.hasText(tableMeta.desc)) {
			defParts.add("\nCOMMENT " + SqlBuilder.strVal(tableMeta.desc));
		}

		return StrUtil.join(defParts, "");
	}

	private static String unwrapName(String name) {
		return name.replaceAll("`", "");
	}

	private static String toAbbrName(String className) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < className.length(); i++) {
			char chr = className.charAt(i);
			if (Character.isUpperCase(chr)) {
				sb.append(chr);
			}
		}
		return sb.toString();
	}

	private static String createSql4CheckDb(String dbName) {
		SelectBuilder selectBuilder = SelectBuilder.getInstance();
		selectBuilder.select("COUNT(*)");
		selectBuilder.from("information_schema.`SCHEMATA`");
		selectBuilder.where("SCHEMA_NAME = " + SqlBuilder.strVal(unwrapName(dbName)));
		return selectBuilder.toSQL();
	}

	private static String createSql4CheckTables(String dbName) {
		SelectBuilder selectBuilder = SelectBuilder.getInstance();
		selectBuilder.select("COUNT(*)");
		selectBuilder.from("information_schema.`TABLES`");
		selectBuilder.where("TABLE_SCHEMA = " + SqlBuilder.strVal(unwrapName(dbName)));
		return selectBuilder.toSQL();
	}

	private static String createSql4CheckTable(String dbName, String tableName) {
		SelectBuilder selectBuilder = SelectBuilder.getInstance();
		selectBuilder.select("COUNT(*)");
		selectBuilder.from("information_schema.`TABLES`");
		selectBuilder.where("TABLE_SCHEMA = " + SqlBuilder.strVal(unwrapName(dbName)));
		selectBuilder.where("TABLE_NAME = " + SqlBuilder.strVal(unwrapName(tableName)));
		return selectBuilder.toSQL();
	}

	private static String createSql4CheckColumn(String dbName, String tableName, String columnName) {
		SelectBuilder selectBuilder = SelectBuilder.getInstance();
		selectBuilder.select("COUNT(*)");
		selectBuilder.from("information_schema.`COLUMNS`");
		selectBuilder.where("TABLE_SCHEMA = " + SqlBuilder.strVal(unwrapName(dbName)));
		selectBuilder.where("TABLE_NAME = " + SqlBuilder.strVal(unwrapName(tableName)));
		selectBuilder.where("COLUMN_NAME = " + SqlBuilder.strVal(unwrapName(columnName)));
		return selectBuilder.toSQL();
	}

	private static Map<String, Boolean> createFlagMap(String[] keys) {
		Map<String, Boolean> flagMap = new HashMap<String, Boolean>();
		for (String key : keys) {
			flagMap.put(unwrapName(key), false);
		}
		return flagMap;
	}

	private static boolean isMatchedFlagMap(Map<String, Boolean> flagMap) {
		for (Map.Entry<String, Boolean> entry : flagMap.entrySet()) {
			if (!Boolean.TRUE.equals(entry.getValue())) {
				return false;
			}
		}
		return true;
	}

	private static boolean existsUK(Connection connection, String dbName, String tableName, UniqueKeyMeta uniqueKeyMeta) throws SQLException {
		SelectBuilder selectBuilder = SelectBuilder.getInstance();
		selectBuilder.select("CONSTRAINT_NAME");
		selectBuilder.from("information_schema.`TABLE_CONSTRAINTS`");
		selectBuilder.where("TABLE_SCHEMA = " + SqlBuilder.strVal(unwrapName(dbName)));
		selectBuilder.where("TABLE_NAME = " + SqlBuilder.strVal(unwrapName(tableName)));
		selectBuilder.where("CONSTRAINT_TYPE = 'UNIQUE'");
		//
		Statement statement = connection.createStatement();
		List<String> dbUkNames = new ArrayList<String>();
		ResultSet rs = statement.executeQuery(selectBuilder.toSQL());
		while (rs.next()) {
			dbUkNames.add(rs.getString(1));
		}
		rs.close();
		boolean found = false;
		for (String dbUkName : dbUkNames) {
			selectBuilder = SelectBuilder.getInstance();
			selectBuilder.select("COLUMN_NAME");
			selectBuilder.from("information_schema.`KEY_COLUMN_USAGE`");
			selectBuilder.where("TABLE_SCHEMA = " + SqlBuilder.strVal(unwrapName(dbName)));
			selectBuilder.where("TABLE_NAME = " + SqlBuilder.strVal(unwrapName(tableName)));
			selectBuilder.where("CONSTRAINT_NAME = " + SqlBuilder.strVal(dbUkName));
			//
			Map<String, Boolean> flagMap = createFlagMap(uniqueKeyMeta.columnNames);
			rs = statement.executeQuery(selectBuilder.toSQL());
			while (rs.next()) {
				String colName = rs.getString(1);
				if (flagMap.containsKey(colName)) {
					flagMap.put(colName, true);
				}
			}
			rs.close();
			if (isMatchedFlagMap(flagMap)) {
				found = true;
				break;
			}
		}
		return found;
	}

	private static boolean existsFK(Connection connection, String dbName, ForeignKeyMeta foreignKeyMeta) throws SQLException {
		SelectBuilder selectBuilder = SelectBuilder.getInstance();
		selectBuilder.select("CONSTRAINT_NAME");
		selectBuilder.from("information_schema.`TABLE_CONSTRAINTS`");
		selectBuilder.where("TABLE_SCHEMA = " + SqlBuilder.strVal(unwrapName(dbName)));
		selectBuilder.where("TABLE_NAME = " + SqlBuilder.strVal(unwrapName(foreignKeyMeta.tableName)));
		selectBuilder.where("CONSTRAINT_TYPE = 'FOREIGN KEY'");
		//
		Statement statement = connection.createStatement();
		List<String> dbFkNames = new ArrayList<String>();
		ResultSet rs = statement.executeQuery(selectBuilder.toSQL());
		while (rs.next()) {
			dbFkNames.add(rs.getString(1));
		}
		rs.close();
		boolean found = false;
		for (String dbFkName : dbFkNames) {
			selectBuilder = SelectBuilder.getInstance();
			selectBuilder.select("TABLE_NAME, COLUMN_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME");
			selectBuilder.from("information_schema.`KEY_COLUMN_USAGE`");
			selectBuilder.where("TABLE_SCHEMA = " + SqlBuilder.strVal(unwrapName(dbName)));
			selectBuilder.where("TABLE_NAME = " + SqlBuilder.strVal(unwrapName(foreignKeyMeta.tableName)));
			selectBuilder.where("CONSTRAINT_NAME = " + SqlBuilder.strVal(dbFkName));
			//
			rs = statement.executeQuery(selectBuilder.toSQL());
			while (rs.next()) {
				String columnName = rs.getString(2);
				if (!columnName.equals(foreignKeyMeta.columnName)) {
					continue;
				}
				String refTableName = rs.getString(3);
				if (!refTableName.equals(foreignKeyMeta.refTableName)) {
					continue;
				}
				String refColumnName = rs.getString(4);
				if (!refColumnName.equals(foreignKeyMeta.refColumnName)) {
					continue;
				}
				found = true;
				break;
			}
			rs.close();
			if (found) {
				break;
			}
		}
		return found;
	}

	private static void createOrAlterTable(Connection connection, String dbName, TableMeta tableMeta) throws SQLException {
		Statement statement = connection.createStatement();
		//
		String tableName = tableMeta.name;
		System.out.println("Checking :  [" + dbName + "].[" + tableName + "]\t\t\t" + tableMeta.javaType.getName());
		String sql4CheckTable = createSql4CheckTable(dbName, tableName);
		ResultSet rs = statement.executeQuery(sql4CheckTable);
		rs.next();
		boolean tableExists = rs.getInt(1) > 0;
		rs.close();
		//
		if (!tableExists) {
			// 创建表
			String sql_createTable = createTableDefination(tableMeta);
			statement.executeUpdate(sql_createTable);
			logger.debug(">>\n" + sql_createTable);
		} else {
			// 修改表
			// 增加列
			for (ColumnMeta columnMeta : tableMeta.columnMetaList) {
				String columnName = columnMeta.name;
				String sql4CheckColumn = createSql4CheckColumn(dbName, tableName, columnName);
				rs = statement.executeQuery(sql4CheckColumn);
				rs.next();
				boolean columnExists = rs.getInt(1) > 0;
				rs.close();
				//
				if (!columnExists) {
					List<String> colDefination = createColDefination(tableMeta, columnMeta);
					if (colDefination != null) {
						String sql_addTableColumn = "ALTER TABLE " + tableName + " ADD COLUMN " + StrUtil.join(colDefination, " ");
						statement.executeUpdate(sql_addTableColumn);
						System.out.println(">>\n" + sql_addTableColumn);
					}
				}
			}
			// 增加唯一约束
			for (UniqueKeyMeta ukMeta : tableMeta.uniqueKeyMetaList) {
				if (!existsUK(connection, dbName, tableName, ukMeta)) {
					List<String> ukDefination = createUKDefination(tableMeta, ukMeta, true);
					String sql_addUK = StrUtil.join(ukDefination, " ");
					System.out.println(">> UK for [" + tableName + "]");
					statement.executeUpdate(sql_addUK);
					System.out.println(">>\n" + sql_addUK);
				}
			}
		}
	}

	/**
	 * 按【创建】依赖关系获取所有表名
	 */
	@SuppressWarnings("unchecked")
	public static List<Couple<String, String>> getTableListByCreateOrder(String... packagesToScan) {
		if (packagesToScan.length == 0) {
			packagesToScan = new String[] { "priv.starfish.mall.**.entity" };
		}
		//
		AnnotatedClassScanner classScanner = new AnnotatedClassScanner(packagesToScan, Table.class);
		//
		try {
			Set<Class<?>> entityClasses = classScanner.getClassSet();
			List<TableMeta> tableMetaList = new ArrayList<TableMeta>();
			Map<Class<?>, TableMeta> entityClassTableMetaMap = new HashMap<Class<?>, TableMeta>();
			Map<String, TableMeta> tableNameMetaMap = new HashMap<String, TableMeta>();
			for (Class<?> entityClass : entityClasses) {
				TableMeta tableMeta = extractTableMeta(entityClass);
				entityClassTableMetaMap.put(entityClass, tableMeta);
				tableNameMetaMap.put(tableMeta.name, tableMeta);
				tableMetaList.add(tableMeta);
			}
			// 生成外键
			DirectedGraph<String> tableGraph = DirectedGraph.newOne();
			for (TableMeta tableMeta : tableMetaList) {
				for (ForeignKeyMeta fkMeta : tableMeta.foreignKeyMetaList) {
					TableMeta tmpMeta = entityClassTableMetaMap.get(fkMeta.refEntityClass);
					fkMeta.refTableName = tmpMeta.name;
					Object colMeta = findColumnMetaByFieldName(tmpMeta, fkMeta.refFieldName);
					fkMeta.refColumnName = colMeta instanceof IdMeta ? ((IdMeta) colMeta).name : ((ColumnMeta) colMeta).name;
					//
					tableGraph.addEdge(fkMeta.tableName, fkMeta.refTableName);
					//
				}
			}
			List<String> tableNames = tableGraph.getArrangedEdgeNodes();
			// TODO
			// for (int i = 0; i < tableNames.size(); i++) {
			// String tableName = tableNames.get(i);
			// TableMeta tableMeta = tableNameMetaMap.get(tableName);
			// if (tableMeta.idMeta.auto) {
			// System.out.println("ALTER TABLE `" + tableName + "` AUTO_INCREMENT=2000;");
			// }
			// }
			//
			List<Couple<String, String>> tableList = new ArrayList<Couple<String, String>>();
			for (int i = 0; i < tableNames.size(); i++) {
				String tableName = tableNames.get(i);
				TableMeta tableMeta = tableNameMetaMap.get(tableName);
				//
				Couple<String, String> tableInfo = Couple.newOne();
				tableInfo.setFirst(tableName);
				tableInfo.setSecond(tableMeta.desc);
				//
				tableList.add(tableInfo);
			}
			return tableList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 按【删除】依赖关系获取所有表名
	 */
	@SuppressWarnings("unchecked")
	public static List<Couple<String, String>> getTableListByDeleteOrder(Class<?> targetTableClass, String... packagesToScan) {
		if (packagesToScan.length == 0) {
			packagesToScan = new String[] { "priv.starfish.mall.**.entity" };
		}
		//
		AnnotatedClassScanner classScanner = new AnnotatedClassScanner(packagesToScan, Table.class);
		//
		try {
			String targetTableName = null;
			//
			Set<Class<?>> entityClasses = classScanner.getClassSet();
			List<TableMeta> tableMetaList = new ArrayList<TableMeta>();
			Map<Class<?>, TableMeta> entityClassTableMetaMap = new HashMap<Class<?>, TableMeta>();
			Map<String, TableMeta> tableNameMetaMap = new HashMap<String, TableMeta>();
			for (Class<?> entityClass : entityClasses) {
				TableMeta tableMeta = extractTableMeta(entityClass);
				entityClassTableMetaMap.put(entityClass, tableMeta);
				tableNameMetaMap.put(tableMeta.name, tableMeta);
				tableMetaList.add(tableMeta);
				//
				if (entityClass.equals(targetTableClass)) {
					targetTableName = tableMeta.name;
				}
			}
			// 生成外键
			DirectedGraph<String> tableGraph = DirectedGraph.newOne();
			for (TableMeta tableMeta : tableMetaList) {
				for (ForeignKeyMeta fkMeta : tableMeta.foreignKeyMetaList) {
					TableMeta tmpMeta = entityClassTableMetaMap.get(fkMeta.refEntityClass);
					fkMeta.refTableName = tmpMeta.name;
					Object colMeta = findColumnMetaByFieldName(tmpMeta, fkMeta.refFieldName);
					fkMeta.refColumnName = colMeta instanceof IdMeta ? ((IdMeta) colMeta).name : ((ColumnMeta) colMeta).name;
					//
					tableGraph.addEdge(fkMeta.tableName, fkMeta.refTableName);
					//
				}
			}
			if (targetTableName == null) {
				System.err.println("未能匹配到给定的表名！");
				//
				return null;
			}
			List<String> tableNames = tableGraph.getArrangedEdgeNodesDependingOn(targetTableName);
			//
			List<Couple<String, String>> tableList = new ArrayList<Couple<String, String>>();
			for (int i = 0; i < tableNames.size(); i++) {
				String tableName = tableNames.get(i);
				TableMeta tableMeta = tableNameMetaMap.get(tableName);
				//
				Couple<String, String> tableInfo = Couple.newOne();
				tableInfo.setFirst(tableName);
				tableInfo.setSecond(tableMeta.desc);
				//
				tableList.add(tableInfo);
			}
			return tableList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private static void createOrAlterTables(Connection connection, String dbName, String... packagesToScan) throws SQLException {
		AnnotatedClassScanner classScanner = new AnnotatedClassScanner(packagesToScan, Table.class);
		//
		try {
			Set<Class<?>> entityClasses = classScanner.getClassSet();
			List<TableMeta> tableMetaList = new ArrayList<TableMeta>();
			Map<Class<?>, TableMeta> entityClassTableMetaMap = new HashMap<Class<?>, TableMeta>();
			Map<String, TableMeta> tableNameMetaMap = new HashMap<String, TableMeta>();
			for (Class<?> entityClass : entityClasses) {
				TableMeta tableMeta = extractTableMeta(entityClass);
				entityClassTableMetaMap.put(entityClass, tableMeta);
				tableNameMetaMap.put(tableMeta.name, tableMeta);
				tableMetaList.add(tableMeta);
				createOrAlterTable(connection, dbName, tableMeta);
			}
			// 生成外键
			DirectedGraph<String> tableGraph = DirectedGraph.newOne();
			List<ForeignKeyMeta> foreignKeyMetaList = new ArrayList<ForeignKeyMeta>();
			for (TableMeta tableMeta : tableMetaList) {
				for (ForeignKeyMeta fkMeta : tableMeta.foreignKeyMetaList) {
					TableMeta tmpMeta = entityClassTableMetaMap.get(fkMeta.refEntityClass);
					fkMeta.refTableName = tmpMeta.name;
					Object colMeta = findColumnMetaByFieldName(tmpMeta, fkMeta.refFieldName);
					fkMeta.refColumnName = colMeta instanceof IdMeta ? ((IdMeta) colMeta).name : ((ColumnMeta) colMeta).name;
					//
					if (StrUtil.isNullOrBlank(fkMeta.name)) {
						fkMeta.name = "FK_" + unwrapName(fkMeta.tableName) + "_" + unwrapName(fkMeta.columnName) + "_2_" + unwrapName(fkMeta.refTableName) + "_" + unwrapName(fkMeta.refColumnName);
					}
					//
					tableGraph.addEdge(fkMeta.tableName, fkMeta.refTableName);
					//
					foreignKeyMetaList.add(fkMeta);
				}
			}
			List<String> tableNames = tableGraph.getArrangedEdgeNodes();
			Statement statement = connection.createStatement();
			for (String tableName : tableNames) {
				TableMeta tableMeta = tableNameMetaMap.get(tableName);
				for (ForeignKeyMeta fkMeta : tableMeta.foreignKeyMetaList) {
					if (!existsFK(connection, dbName, fkMeta)) {
						List<String> fkDefination = createFKDefination(tableMeta, fkMeta);
						String sql_addFK = StrUtil.join(fkDefination, " ");
						System.out.println(">> FK for [" + tableName + "]");
						statement.executeUpdate(sql_addFK);
						System.out.println(">>\n" + sql_addFK);
					}
				}
			}
			//
			System.out.println(tableNames);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final IPlaceHolderMaker ibatisPlaceHolderMaker = new IPlaceHolderMaker() {
		@Override
		public String makePlaceHolder(String orginalStr) {
			return "#{" + orginalStr.trim() + "}";
		}

		@Override
		public boolean isPlaceHolder(String valueStr) {
			if (!StrUtil.hasText(valueStr)) {
				return false;
			}
			return valueStr.startsWith("#{") && valueStr.endsWith("}");
		}

	};

	public static Boolean dbExists(String dbConfPropertiesFile) throws ClassNotFoundException {
		if (!StrUtil.hasText(dbConfPropertiesFile)) {
			dbConfPropertiesFile = "conf/jdbc.properties";
		}
		PropertyConfigurer config = PropertyConfigurer.newInstance(dbConfPropertiesFile);
		//
		String jdbcUrl = config.get("jdbc.url");
		String username = config.get("jdbc.username");
		String password = config.get("jdbc.password");
		String dbName = config.get("jdbc.schema");
		if (!StrUtil.hasText(dbName)) {
			System.err.println("数据库连接配置文件中缺少必要的 jdbc.schema = [数据库名] 配置项 ！");
			return null;
		}
		Class.forName(config.get("jdbc.driverClass"));
		try {
			String actualUrl = jdbcUrl.replace("${jdbc.schema}", "information_schema");
			Connection connection = DriverManager.getConnection(actualUrl, username, password);
			connection.setAutoCommit(true);

			System.out.println("Starting to check if db exists " + dbName);

			Statement statement = connection.createStatement();
			//
			String sql4CheckDb = createSql4CheckDb(dbName);
			ResultSet rs = statement.executeQuery(sql4CheckDb);
			rs.next();
			boolean exists = rs.getInt(1) > 0;
			rs.close();

			System.out.println("Finished checking db existence " + dbName);
			//
			connection.close();

			return exists;
		} catch (SQLException e) {
			e.printStackTrace();
			//
			return null;
		}
	}

	public static Boolean dbExists() throws ClassNotFoundException {
		return dbExists(null);
	}

	//
	public static Boolean createDb(String dbConfPropertiesFile) throws ClassNotFoundException {
		if (!StrUtil.hasText(dbConfPropertiesFile)) {
			dbConfPropertiesFile = "conf/jdbc.properties";
		}
		PropertyConfigurer config = PropertyConfigurer.newInstance(dbConfPropertiesFile);
		//
		String jdbcUrl = config.get("jdbc.url");
		String username = config.get("jdbc.username");
		String password = config.get("jdbc.password");
		String dbName = config.get("jdbc.schema");
		if (!StrUtil.hasText(dbName)) {
			System.err.println("数据库连接配置文件中缺少必要的 jdbc.schema = [数据库名] 配置项 ！");
			return null;
		}
		Class.forName(config.get("jdbc.driverClass"));
		try {
			String actualUrl = jdbcUrl.replace("${jdbc.schema}", "information_schema");
			Connection connection = DriverManager.getConnection(actualUrl, username, password);
			connection.setAutoCommit(true);

			System.out.println("Starting to create db " + dbName);

			Statement statement = connection.createStatement();
			//
			String sql4CreateDb = "CREATE DATABASE IF NOT EXISTS " + dbName + " DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci";
			int count = statement.executeUpdate(sql4CreateDb);

			System.out.println("Finished creating db " + dbName);
			//
			connection.close();

			return count > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			//
			return null;
		}
	}

	public static Boolean createDb() throws ClassNotFoundException {
		return createDb(null);
	}

	public static Boolean tablesExist(String dbConfPropertiesFile) throws ClassNotFoundException {
		if (!StrUtil.hasText(dbConfPropertiesFile)) {
			dbConfPropertiesFile = "conf/jdbc.properties";
		}
		PropertyConfigurer config = PropertyConfigurer.newInstance(dbConfPropertiesFile);
		//
		String jdbcUrl = config.get("jdbc.url");
		String username = config.get("jdbc.username");
		String password = config.get("jdbc.password");
		String dbName = config.get("jdbc.schema");
		if (!StrUtil.hasText(dbName)) {
			System.err.println("数据库连接配置文件中缺少必要的 jdbc.schema = [数据库名] 配置项 ！");
			return null;
		}
		Class.forName(config.get("jdbc.driverClass"));
		try {
			String actualUrl = jdbcUrl.replace("${jdbc.schema}", dbName);
			Connection connection = DriverManager.getConnection(actualUrl, username, password);
			connection.setAutoCommit(true);

			System.out.println("Starting to check if db tables exist " + dbName);

			Statement statement = connection.createStatement();
			//
			String sql4CheckTables = createSql4CheckTables(dbName);
			ResultSet rs = statement.executeQuery(sql4CheckTables);
			rs.next();
			boolean exist = rs.getInt(1) > 0;
			rs.close();

			System.out.println("Finished checking db tables' existence " + dbName);
			//
			connection.close();

			return exist;
		} catch (SQLException e) {
			e.printStackTrace();
			//
			return null;
		}
	}

	public static Boolean tablesExist() throws ClassNotFoundException {
		return tablesExist(null);
	}

	public static void createOrAlterTables(String dbConfPropertiesFile, String... packagesToScan) throws ClassNotFoundException {
		if (!StrUtil.hasText(dbConfPropertiesFile)) {
			dbConfPropertiesFile = "conf/jdbc.properties";
		}
		if (packagesToScan.length == 0) {
			packagesToScan = new String[] { "priv.starfish.mall.**.entity" };
		}
		PropertyConfigurer config = PropertyConfigurer.newInstance(dbConfPropertiesFile);
		//
		String jdbcUrl = config.get("jdbc.url");
		String username = config.get("jdbc.username");
		String password = config.get("jdbc.password");
		String dbName = config.get("jdbc.schema");
		if (!StrUtil.hasText(dbName)) {
			System.err.println("数据库连接配置文件中缺少必要的 jdbc.schema = [数据库名] 配置项 ！");
			return;
		}
		Class.forName(config.get("jdbc.driverClass"));
		try {
			String actualUrl = jdbcUrl.replace("${jdbc.schema}", dbName);
			Connection connection = DriverManager.getConnection(actualUrl, username, password);
			connection.setAutoCommit(true);

			System.out.println("Starting to check and update table defination \n" + actualUrl);

			createOrAlterTables(connection, dbName, packagesToScan);

			System.out.println("Finished checking and updating table defination .");

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * // 根据实体类的配置更新数据库表定义（只增加表和表的列）
	 * 
	 * @throws ClassNotFoundException
	 */
	public static void createOrAlterTables() throws ClassNotFoundException {
		createOrAlterTables(null);
	}

	/**
	 * 输出根据实体类的配置生成的Mapper类和xml配置
	 * 
	 * @param entityClass
	 * @throws IOException
	 */
	public static void printMapperClassAndXml(Class<?> entityClass) throws IOException {
		TableMeta tableMeta = extractTableMeta(entityClass);
		if (tableMeta == null) {
			return;
		}
		List<String> mapperClassRows = new ArrayList<String>();
		String mapperPackageName = entityClass.getName().substring(0, entityClass.getName().indexOf(".entity.")) + ".mapper";
		String mapperClassSimpleName = entityClass.getSimpleName() + "Mapper";
		String mapperClassName = mapperPackageName + "." + mapperClassSimpleName;
		mapperClassRows.add("package " + mapperPackageName + ";");
		mapperClassRows.add(StrUtil.NewLine);
		mapperClassRows.add("import org.apache.ibatis.annotations.Param;");
		mapperClassRows.add(StrUtil.NewLine);
		mapperClassRows.add("import priv.starfish.common.annotation.IBatisMapper;");
		mapperClassRows.add("import priv.starfish.mall.base.mapper.BaseMapper;");
		mapperClassRows.add("import " + entityClass.getName() + ";");
		mapperClassRows.add(StrUtil.NewLine);
		mapperClassRows.add("@IBatisMapper");
		//
		StringBuilder mapperMethod = null;
		//
		Document doc = new Document();
		DocType docType = new DocType("mapper", "-//mybatis.org//DTD Mapper 3.0//EN", "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
		doc.addContent(docType);
		Element root = new Element("mapper");
		doc.addContent(root);
		root.setAttribute("namespace", mapperClassName);
		final Map<String, String> colFieldNameMap = new LinkedHashMap<String, String>();
		final String tableAlias = toAbbrName(entityClass.getSimpleName());
		String tableName = tableMeta.name;
		// resultMap node
		Element resultMap = new Element("resultMap");
		root.addContent(resultMap);
		String resultMapId = entityClass.getSimpleName() + "Map";
		resultMap.setAttribute("id", resultMapId);
		String resultMapType = entityClass.getName();
		resultMap.setAttribute("type", resultMapType);
		// resultMap id node
		IdMeta idMeta = tableMeta.idMeta;
		mapperClassRows.add("public interface " + mapperClassSimpleName + " extends BaseMapper<" + entityClass.getSimpleName() + ", " + idMeta.javaType.getSimpleName() + "> {");
		@SuppressWarnings("unused")
		List<String> idDefination = createIdDefination(tableMeta, idMeta);
		Element id = new Element("id");
		resultMap.addContent(id);
		id.setAttribute("column", unwrapName(idMeta.name));
		id.setAttribute("property", idMeta.fieldName);
		colFieldNameMap.put(idMeta.name, idMeta.fieldName);//
		String jdbcTypeName = idMeta.typeName;
		if (getColType4sqlType(jdbcTypeName) != null) {
			jdbcTypeName = getColType4sqlType(jdbcTypeName);
		}
		id.setAttribute("jdbcType", jdbcTypeName);
		// resultMap result node
		List<String> unUpdatableFieldNames = new ArrayList<String>();
		for (ColumnMeta colMeta : tableMeta.columnMetaList) {
			List<String> colDefination = createColDefination(tableMeta, colMeta);
			if (colDefination != null) {
				Element result = new Element("result");
				resultMap.addContent(result);
				result.setAttribute("column", unwrapName(colMeta.name));
				result.setAttribute("property", colMeta.fieldName);
				colFieldNameMap.put(colMeta.name, colMeta.fieldName);
				//
				jdbcTypeName = colMeta.typeName;
				if (getColType4sqlType(jdbcTypeName) != null) {
					jdbcTypeName = getColType4sqlType(jdbcTypeName);
				}
				result.setAttribute("jdbcType", jdbcTypeName);
				// 记录不可更新字段
				if (!colMeta.updatable) {
					unUpdatableFieldNames.add(colMeta.fieldName);
				}
			}
		}
		//
		List<String> colNames = new ArrayList<String>();
		List<String> fieldNames = new ArrayList<String>();
		colNames.addAll(colFieldNameMap.keySet());
		for (String colName : colNames) {
			fieldNames.add(colFieldNameMap.get(colName));
		}
		// select by id
		mapperMethod = new StringBuilder();//
		mapperMethod.append("\t").append(entityClass.getSimpleName());//
		Element selectNode = new Element("select");
		root.addContent(selectNode);
		String selectId = "selectById";
		selectNode.setAttribute("id", selectId);
		mapperMethod.append(" ").append(selectId).append("(");//
		selectNode.setAttribute("resultMap", resultMapId);
		selectNode.setAttribute("parameterType", idMeta.javaType.getSimpleName());
		SelectBuilder selectBuilder = SelectBuilder.getInstance();
		Converter<String, String> colNameConverter = new Converter<String, String>() {
			@Override
			public String convert(String src, int index) {
				return tableAlias + "." + unwrapName(src);
			}

		};
		selectBuilder.select(StrUtil.join(CollectionUtil.convertToList(colNames, colNameConverter), ", "));
		selectBuilder.from(tableName + " " + tableAlias);
		selectBuilder.where(tableAlias + "." + idMeta.name + " = " + ibatisPlaceHolderMaker.makePlaceHolder(idMeta.fieldName));
		selectNode.addContent(new Text(StrUtil.NewLine + selectBuilder.toSQL()));

		mapperMethod.append(idMeta.javaType.getSimpleName()).append(" ").append(idMeta.fieldName);//
		mapperMethod.append(");");//
		mapperClassRows.add(mapperMethod.toString());//
		// select by uk columns
		for (UniqueKeyMeta ukMeta : tableMeta.uniqueKeyMetaList) {
			mapperMethod = new StringBuilder();//
			mapperMethod.append("\t").append(entityClass.getSimpleName());//
			//
			String[] ukColumns = ukMeta.columnNames;
			selectNode = new Element("select");
			root.addContent(selectNode);
			Converter<String, String> col2FieldConverter = new Converter<String, String>() {
				@Override
				public String convert(String src, int index) {
					return colFieldNameMap.get(src);
				}
			};
			List<String> ukFieldNames = CollectionUtil.convertToList(TypeUtil.arrayToList(ukColumns), col2FieldConverter);
			List<String> ukFiledNames2 = new ArrayList<String>();
			for (String ukFieldName : ukFieldNames) {
				ukFiledNames2.add(StrUtil.capitalize(ukFieldName));
			}
			selectId = "selectBy" + StrUtil.join(ukFiledNames2, "And");
			selectNode.setAttribute("id", selectId);
			mapperMethod.append(" ").append(selectId).append("(");//
			selectNode.setAttribute("resultMap", resultMapId);
			//
			selectBuilder = SelectBuilder.getInstance();
			selectBuilder.select(StrUtil.join(CollectionUtil.convertToList(colNames, colNameConverter), ", "));
			selectBuilder.from(tableMeta.name + " " + tableAlias);
			List<String> mapperMethodParams = new ArrayList<String>();
			for (int i = 0; i < ukColumns.length; i++) {
				String ukColName = ukColumns[i];
				selectBuilder.where(tableAlias + "." + ukColName + " = " + ibatisPlaceHolderMaker.makePlaceHolder(ukFieldNames.get(i)));
				ColumnMeta ukColMeta = findColumnMetaByColName(tableMeta, ukColName);

				mapperMethodParams.add("@Param(\"" + ukColMeta.fieldName + "\") " + ukColMeta.javaType.getSimpleName() + " " + ukColMeta.fieldName);
			}
			selectNode.addContent(new Text(StrUtil.NewLine + selectBuilder.toSQL()));
			mapperMethod.append(StrUtil.join(mapperMethodParams, ", "));//
			mapperMethod.append(");");//
			mapperClassRows.add(mapperMethod.toString());
		}

		// insert
		mapperMethod = new StringBuilder();//
		mapperMethod.append("\t").append("int");//
		Element insertNode = new Element("insert");
		root.addContent(insertNode);
		insertNode.setAttribute("id", "insert");
		mapperMethod.append(" ").append("insert").append("(");//
		mapperMethod.append(entityClass.getSimpleName()).append(" ").append(StrUtil.toJavaVariableName(entityClass.getSimpleName()));//
		mapperMethod.append(");");//
		mapperClassRows.add(mapperMethod.toString());//
		insertNode.setAttribute("useGeneratedKeys", idMeta.auto ? "true" : "false");
		if (idMeta.auto) {
			insertNode.setAttribute("keyProperty", idMeta.fieldName);
		}
		InsertBuilder insertBuilder = InsertBuilder.getInstance();
		insertBuilder.setPlaceHolderMaker(ibatisPlaceHolderMaker);
		insertBuilder.insertInto(tableName);
		for (int i = 0; i < colNames.size(); i++) {
			String colName = colNames.get(i);
			if (idMeta.auto && colName.equals(idMeta.name)) {
				continue;
			}
			String fieldName = fieldNames.get(i);
			insertBuilder.value(colName, fieldName).asPlaceHolder();
		}
		insertNode.addContent(new Text(StrUtil.NewLine + insertBuilder.toSQL()));
		// update
		mapperMethod = new StringBuilder();//
		mapperMethod.append("\t").append("int");//
		Element updateNode = new Element("update");
		root.addContent(updateNode);
		updateNode.setAttribute("id", "update");
		mapperMethod.append(" ").append("update").append("(");//
		mapperMethod.append(entityClass.getSimpleName()).append(" ").append(StrUtil.toJavaVariableName(entityClass.getSimpleName()));//
		mapperMethod.append(");");//
		mapperClassRows.add(mapperMethod.toString());//
		UpdateBuilder updateBuilder = UpdateBuilder.getInstance();
		updateBuilder.setPlaceHolderMaker(ibatisPlaceHolderMaker);
		updateBuilder.withSetClauseBuilder(ifSetClauseBuilder);
		updateBuilder.update(tableName);
		for (int i = 0; i < colNames.size(); i++) {
			String colName = colNames.get(i);
			if (!colName.equals(idMeta.name)) {
				String fieldName = fieldNames.get(i);
				if (!unUpdatableFieldNames.contains(fieldName)) {
					updateBuilder.set(colName, fieldName).asPlaceHolder();
				}
			}
		}
		updateBuilder.where(idMeta.name + " = " + ibatisPlaceHolderMaker.makePlaceHolder(idMeta.fieldName));
		updateNode.addContent(new CDATA(StrUtil.NewLine + updateBuilder.toSQL()));
		// delete by id
		mapperMethod = new StringBuilder();//
		mapperMethod.append("\t").append("int");//
		Element deleteNode = new Element("delete");
		root.addContent(deleteNode);
		String deleteId = "deleteById";
		deleteNode.setAttribute("id", deleteId);
		mapperMethod.append(" ").append(deleteId).append("(");//
		deleteNode.setAttribute("parameterType", idMeta.javaType.getSimpleName());
		DeleteBuilder deleteBuilder = DeleteBuilder.getInstance();
		deleteBuilder.deleteFrom(tableName);
		deleteBuilder.where(idMeta.name + " = " + ibatisPlaceHolderMaker.makePlaceHolder(idMeta.fieldName));
		deleteNode.addContent(new Text(StrUtil.NewLine + deleteBuilder.toSQL()));
		mapperMethod.append(idMeta.javaType.getSimpleName()).append(" ").append(idMeta.fieldName);//
		mapperMethod.append(");");//
		mapperClassRows.add(mapperMethod.toString());//
		/*
		 * delete by uk columns for (UniqueKeyMeta ukMeta : tableMeta.uniqueKeyMetaList) { mapperMethod = new StringBuilder();// mapperMethod.append("\t").append("void");// String[] ukColumns =
		 * ukMeta.columnNames; deleteNode = new Element("delete"); root.addContent(deleteNode); Converter<String, String> col2FieldConverter = new Converter<String, String>() {
		 * 
		 * @Override public String convert(String src, int index) { return colFieldNameMap.get(src); } }; List<String> ukFieldNames = CollectionUtil.convertToList(TypeUtil.arrayToList(ukColumns),
		 * col2FieldConverter); List<String> ukFiledNames2 = new ArrayList<String>(); for (String ukFieldName : ukFieldNames) { ukFiledNames2.add(StrUtil.capitalize(ukFieldName)); } deleteId =
		 * "deleteBy" + StrUtil.join(ukFiledNames2, "And"); deleteNode.setAttribute("id", deleteId); mapperMethod.append(" ").append(deleteId).append("(");// // deleteBuilder =
		 * DeleteBuilder.getInstance(); deleteBuilder.deleteFrom(tableName); List<String> mapperMethodParams = new ArrayList<String>(); for (int i = 0; i < ukColumns.length; i++) { String ukColName =
		 * ukColumns[i]; deleteBuilder.where(ukColName + " = " + ibatisPlaceHolderMaker.makePlaceHolder(ukFieldNames.get(i))); ColumnMeta ukColMeta = getColumnMetaByColName(tableMeta, ukColName);
		 * 
		 * mapperMethodParams.add("@Param(\"" + ukColMeta.fieldName + "\") " + ukColMeta.javaType.getSimpleName() + " " + ukColMeta.fieldName); } deleteNode.addContent(new Text(StrUtil.NewLine +
		 * deleteBuilder.toSQL())); mapperMethod.append(StrUtil.join(mapperMethodParams, ", "));// mapperMethod.append(");");// mapperClassRows.add(mapperMethod.toString()); }
		 */
		mapperClassRows.add("}");

		System.out.println("========================================== java mapper interface =============================================");
		System.out.println(StrUtil.join(mapperClassRows, StrUtil.NewLine));
		System.out.println();
		System.out.println("=========================================== mybatis mapper xml ===============================================");
		// 把创建的xml文档写到流中
		XMLOutputter out = new XMLOutputter();
		// 设置生成xml文档的格式
		Format format = Format.getPrettyFormat();

		// 自定义xml文档的缩进(敲了四个空格，代表四个缩进)
		format.setIndent("    ");
		out.setFormat(format);
		out.output(doc, System.out);
	}

	// ================================================================================================================================================================

	public static void printDaoClassAndXml(Class<?> entityClass) throws IOException {
		TableMeta tableMeta = extractTableMeta(entityClass);
		if (tableMeta == null) {
			return;
		}
		List<String> daoClassRows = new ArrayList<String>();
		List<String> daoImplClassRows = new ArrayList<String>();
		String daoPackageName = entityClass.getName().substring(0, entityClass.getName().indexOf(".entity.")) + ".dao";
		String daoImplPackageName = daoPackageName + ".impl";
		String daoClassSimpleName = entityClass.getSimpleName() + "Dao";
		String daoImplClassSimpleName = daoClassSimpleName + "Impl";
		String daoClassName = daoPackageName + "." + daoClassSimpleName;
		// String daoImplClassName = daoImplPackageName + "." + daoImplClassSimpleName;
		daoClassRows.add("package " + daoPackageName + ";");
		daoClassRows.add(StrUtil.NewLine);
		daoClassRows.add("import java.util.List;");
		daoClassRows.add(StrUtil.NewLine);
		daoClassRows.add("import priv.starfish.common.annotation.IBatisSqlTarget;");
		daoClassRows.add("import priv.starfish.mall.base.dao.BaseDao;");
		daoClassRows.add("import " + entityClass.getName() + ";");
		daoClassRows.add(StrUtil.NewLine);
		daoClassRows.add("@IBatisSqlTarget");
		//
		daoImplClassRows.add("package " + daoImplPackageName + ";");
		daoImplClassRows.add(StrUtil.NewLine);
		daoImplClassRows.add("import java.util.List;");
		daoImplClassRows.add("import java.util.Map;");
		daoImplClassRows.add(StrUtil.NewLine);
		daoImplClassRows.add("import org.springframework.stereotype.Component;");
		daoImplClassRows.add(StrUtil.NewLine);
		daoImplClassRows.add("import priv.starfish.mall.base.dao.impl.BaseDaoImpl;");
		daoImplClassRows.add("import " + daoClassName + ";");
		daoImplClassRows.add("import " + entityClass.getName() + ";");
		daoImplClassRows.add(StrUtil.NewLine);
		daoImplClassRows.add("@Component(\"" + StrUtil.toJavaVariableName(daoClassSimpleName) + "\")");
		StringBuilder daoMethod = null;
		StringBuilder daoImplMethod = null;
		//
		Document doc = new Document();
		DocType docType = new DocType("mapper", "-//mybatis.org//DTD Mapper 3.0//EN", "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
		doc.addContent(docType);
		Element root = new Element("mapper");
		doc.addContent(root);
		root.setAttribute("namespace", daoClassName);
		final Map<String, String> colFieldNameMap = new LinkedHashMap<String, String>();
		final String tableAlias = toAbbrName(entityClass.getSimpleName());
		String tableName = tableMeta.name;
		// resultMap node
		Element resultMap = new Element("resultMap");
		root.addContent(resultMap);
		String resultMapId = entityClass.getSimpleName() + "Map";
		resultMap.setAttribute("id", resultMapId);
		String resultMapType = entityClass.getName();
		resultMap.setAttribute("type", resultMapType);
		// resultMap id node
		IdMeta idMeta = tableMeta.idMeta;
		daoClassRows.add("public interface " + daoClassSimpleName + " extends BaseDao<" + entityClass.getSimpleName() + ", " + idMeta.javaType.getSimpleName() + "> {");
		daoImplClassRows.add("public class " + daoImplClassSimpleName + " extends BaseDaoImpl<" + entityClass.getSimpleName() + ", " + idMeta.javaType.getSimpleName() + "> implements " + daoClassSimpleName + " {");
		@SuppressWarnings("unused")
		List<String> idDefination = createIdDefination(tableMeta, idMeta);
		Element id = new Element("id");
		resultMap.addContent(id);
		id.setAttribute("column", unwrapName(idMeta.name));
		id.setAttribute("property", idMeta.fieldName);
		colFieldNameMap.put(idMeta.name, idMeta.fieldName);//
		String jdbcTypeName = idMeta.typeName;
		if (getColType4sqlType(jdbcTypeName) != null) {
			jdbcTypeName = getColType4sqlType(jdbcTypeName);
		}
		id.setAttribute("jdbcType", jdbcTypeName);
		// resultMap result node
		List<String> unUpdatableFieldNames = new ArrayList<String>();
		for (ColumnMeta colMeta : tableMeta.columnMetaList) {
			List<String> colDefination = createColDefination(tableMeta, colMeta);
			if (colDefination != null) {
				Element result = new Element("result");
				resultMap.addContent(result);
				result.setAttribute("column", unwrapName(colMeta.name));
				result.setAttribute("property", colMeta.fieldName);
				colFieldNameMap.put(colMeta.name, colMeta.fieldName);
				//
				jdbcTypeName = colMeta.typeName;
				if (getColType4sqlType(jdbcTypeName) != null) {
					jdbcTypeName = getColType4sqlType(jdbcTypeName);
				}
				result.setAttribute("jdbcType", jdbcTypeName);
				// 记录不可更新字段
				if (!colMeta.updatable) {
					unUpdatableFieldNames.add(colMeta.fieldName);
				}
			}
		}
		//
		List<String> colNames = new ArrayList<String>();
		List<String> fieldNames = new ArrayList<String>();

		colNames.addAll(colFieldNameMap.keySet());
		for (String colName : colNames) {
			fieldNames.add(colFieldNameMap.get(colName));
		}
		// select by id
		daoMethod = new StringBuilder();//
		daoImplMethod = new StringBuilder();//
		daoMethod.append("\t").append(entityClass.getSimpleName());//
		daoImplMethod.append("\t").append("@Override").append(StrUtil.NewLine);
		daoImplMethod.append("\t").append("public").append(" ").append(entityClass.getSimpleName());//
		Element selectNode = new Element("select");
		root.addContent(selectNode);
		String selectId = "selectById";
		selectNode.setAttribute("id", selectId);
		daoMethod.append(" ").append(selectId).append("(");//
		daoImplMethod.append(" ").append(selectId).append("(");//
		selectNode.setAttribute("resultMap", resultMapId);
		selectNode.setAttribute("parameterType", idMeta.javaType.getSimpleName());
		SelectBuilder selectBuilder = SelectBuilder.getInstance();
		Converter<String, String> colNameConverter = new Converter<String, String>() {
			@Override
			public String convert(String src, int index) {
				return tableAlias + "." + unwrapName(src);
			}

		};
		selectBuilder.select(StrUtil.join(CollectionUtil.convertToList(colNames, colNameConverter), ", "));
		selectBuilder.from(tableName + " " + tableAlias);
		selectBuilder.where(tableAlias + "." + idMeta.name + " = " + ibatisPlaceHolderMaker.makePlaceHolder(idMeta.fieldName));
		selectNode.addContent(new Text(StrUtil.NewLine + selectBuilder.toSQL()));

		daoMethod.append(idMeta.javaType.getSimpleName()).append(" ").append(idMeta.fieldName);//
		daoImplMethod.append(idMeta.javaType.getSimpleName()).append(" ").append(idMeta.fieldName);//
		daoMethod.append(");");//
		daoImplMethod.append(") {").append(StrUtil.NewLine);//
		daoClassRows.add(daoMethod.toString());//
		// {
		daoImplMethod.append("\t\t").append("String sqlId = this.getNamedSqlId(\"" + selectId + "\");").append(StrUtil.NewLine);
		daoImplMethod.append("\t\t").append("//").append(StrUtil.NewLine);
		daoImplMethod.append("\t\t").append("return this.getSqlSession().selectOne(sqlId, " + idMeta.fieldName + ");").append(StrUtil.NewLine);
		// }
		daoImplMethod.append("\t").append("}");//
		daoImplClassRows.add(daoImplMethod.toString());//
		// select by uk columns
		for (UniqueKeyMeta ukMeta : tableMeta.uniqueKeyMetaList) {
			daoMethod = new StringBuilder();//
			daoImplMethod = new StringBuilder();
			daoMethod.append("\t").append(entityClass.getSimpleName());//
			daoImplMethod.append("\t").append("@Override").append(StrUtil.NewLine);
			daoImplMethod.append("\t").append("public").append(" ").append(entityClass.getSimpleName());//
			//
			String[] ukColumns = ukMeta.columnNames;
			selectNode = new Element("select");
			root.addContent(selectNode);
			Converter<String, String> col2FieldConverter = new Converter<String, String>() {
				@Override
				public String convert(String src, int index) {
					return colFieldNameMap.get(src);
				}
			};
			List<String> ukFieldNames = CollectionUtil.convertToList(TypeUtil.arrayToList(ukColumns), col2FieldConverter);
			List<String> ukFiledNames2 = new ArrayList<String>();
			for (String ukFieldName : ukFieldNames) {
				ukFiledNames2.add(StrUtil.capitalize(ukFieldName));
			}
			selectId = "selectBy" + StrUtil.join(ukFiledNames2, "And");
			selectNode.setAttribute("id", selectId);
			daoMethod.append(" ").append(selectId).append("(");//
			daoImplMethod.append(" ").append(selectId).append("(");//
			selectNode.setAttribute("resultMap", resultMapId);
			//
			selectBuilder = SelectBuilder.getInstance();
			selectBuilder.select(StrUtil.join(CollectionUtil.convertToList(colNames, colNameConverter), ", "));
			selectBuilder.from(tableMeta.name + " " + tableAlias);
			List<String> daoMethodParams = new ArrayList<String>();
			for (int i = 0; i < ukColumns.length; i++) {
				String ukColName = ukColumns[i];
				selectBuilder.where(tableAlias + "." + ukColName + " = " + ibatisPlaceHolderMaker.makePlaceHolder(ukFieldNames.get(i)));
				ColumnMeta ukColMeta = findColumnMetaByColName(tableMeta, ukColName);

				daoMethodParams.add(ukColMeta.javaType.getSimpleName() + " " + ukColMeta.fieldName);
			}
			selectNode.addContent(new Text(StrUtil.NewLine + selectBuilder.toSQL()));
			daoMethod.append(StrUtil.join(daoMethodParams, ", "));//
			daoImplMethod.append(StrUtil.join(daoMethodParams, ", "));//
			daoMethod.append(");");//
			daoImplMethod.append(") {").append(StrUtil.NewLine);//
			daoClassRows.add(daoMethod.toString());
			// {
			daoImplMethod.append("\t\t").append("String sqlId = this.getNamedSqlId(\"" + selectId + "\");").append(StrUtil.NewLine);
			daoImplMethod.append("\t\t").append("//").append(StrUtil.NewLine);
			if (ukColumns.length < 2) {
				ColumnMeta ukColMeta = findColumnMetaByColName(tableMeta, ukColumns[0]);
				daoImplMethod.append("\t\t").append("return this.getSqlSession().selectOne(sqlId, " + ukColMeta.fieldName + ");").append(StrUtil.NewLine);
			} else {
				daoImplMethod.append("\t\t").append("Map<String, Object> params = this.newParamMap();").append(StrUtil.NewLine);
				for (int i = 0; i < ukColumns.length; i++) {
					String ukColName = ukColumns[i];
					ColumnMeta ukColMeta = findColumnMetaByColName(tableMeta, ukColName);
					daoImplMethod.append("\t\t").append("params.put(\"" + ukColMeta.fieldName + "\", " + ukColMeta.fieldName + ");").append(StrUtil.NewLine);
				}
				daoImplMethod.append("\t\t").append("//").append(StrUtil.NewLine);
				daoImplMethod.append("\t\t").append("return this.getSqlSession().selectOne(sqlId, params);").append(StrUtil.NewLine);
			}
			// }
			daoImplMethod.append("\t").append("}");//
			daoImplClassRows.add(daoImplMethod.toString());//
		}

		// insert
		daoMethod = new StringBuilder();//
		daoImplMethod = new StringBuilder();//
		daoMethod.append("\t").append("int");//
		daoImplMethod.append("\t").append("@Override").append(StrUtil.NewLine);
		daoImplMethod.append("\t").append("public").append(" ").append("int");//
		Element insertNode = new Element("insert");
		root.addContent(insertNode);
		insertNode.setAttribute("id", "insert");
		daoMethod.append(" ").append("insert").append("(");//
		daoImplMethod.append(" ").append("insert").append("(");//
		daoMethod.append(entityClass.getSimpleName()).append(" ").append(StrUtil.toJavaVariableName(entityClass.getSimpleName()));//
		daoImplMethod.append(entityClass.getSimpleName()).append(" ").append(StrUtil.toJavaVariableName(entityClass.getSimpleName()));//
		daoMethod.append(");");//
		daoImplMethod.append(") {").append(StrUtil.NewLine);//
		daoClassRows.add(daoMethod.toString());//
		// {
		daoImplMethod.append("\t\t").append("String sqlId = this.getNamedSqlId(\"" + "insert" + "\");").append(StrUtil.NewLine);
		daoImplMethod.append("\t\t").append("//").append(StrUtil.NewLine);
		daoImplMethod.append("\t\t").append("return this.getSqlSession().insert(sqlId, " + StrUtil.toJavaVariableName(entityClass.getSimpleName()) + ");").append(StrUtil.NewLine);
		// }
		daoImplMethod.append("\t").append("}");//
		daoImplClassRows.add(daoImplMethod.toString());//
		insertNode.setAttribute("useGeneratedKeys", idMeta.auto ? "true" : "false");
		if (idMeta.auto) {
			insertNode.setAttribute("keyProperty", idMeta.fieldName);
		}
		InsertBuilder insertBuilder = InsertBuilder.getInstance();
		insertBuilder.setPlaceHolderMaker(ibatisPlaceHolderMaker);
		insertBuilder.insertInto(tableName);
		for (int i = 0; i < colNames.size(); i++) {
			String colName = colNames.get(i);
			if (idMeta.auto && colName.equals(idMeta.name)) {
				continue;
			}
			String fieldName = fieldNames.get(i);
			insertBuilder.value(colName, fieldName).asPlaceHolder();
		}
		insertNode.addContent(new Text(StrUtil.NewLine + insertBuilder.toSQL()));
		// update
		daoMethod = new StringBuilder();//
		daoImplMethod = new StringBuilder();//
		daoMethod.append("\t").append("int");//
		daoImplMethod.append("\t").append("@Override").append(StrUtil.NewLine);
		daoImplMethod.append("\t").append("public").append(" ").append("int");//
		Element updateNode = new Element("update");
		root.addContent(updateNode);
		updateNode.setAttribute("id", "update");
		daoMethod.append(" ").append("update").append("(");//
		daoImplMethod.append(" ").append("update").append("(");//
		daoMethod.append(entityClass.getSimpleName()).append(" ").append(StrUtil.toJavaVariableName(entityClass.getSimpleName()));//
		daoImplMethod.append(entityClass.getSimpleName()).append(" ").append(StrUtil.toJavaVariableName(entityClass.getSimpleName()));//
		daoMethod.append(");");//
		daoImplMethod.append(") {").append(StrUtil.NewLine);//
		daoClassRows.add(daoMethod.toString());//
		// {
		daoImplMethod.append("\t\t").append("String sqlId = this.getNamedSqlId(\"" + "update" + "\");").append(StrUtil.NewLine);
		daoImplMethod.append("\t\t").append("//").append(StrUtil.NewLine);
		daoImplMethod.append("\t\t").append("return this.getSqlSession().update(sqlId, " + StrUtil.toJavaVariableName(entityClass.getSimpleName()) + ");").append(StrUtil.NewLine);
		// }
		daoImplMethod.append("\t").append("}");//
		daoImplClassRows.add(daoImplMethod.toString());//
		UpdateBuilder updateBuilder = UpdateBuilder.getInstance();
		updateBuilder.setPlaceHolderMaker(ibatisPlaceHolderMaker);
		updateBuilder.withSetClauseBuilder(ifSetClauseBuilder);
		updateBuilder.update(tableName);
		for (int i = 0; i < colNames.size(); i++) {
			String colName = colNames.get(i);
			if (!colName.equals(idMeta.name)) {
				String fieldName = fieldNames.get(i);
				if (!unUpdatableFieldNames.contains(fieldName)) {
					updateBuilder.set(colName, fieldName).asPlaceHolder();
				}
			}
		}
		updateBuilder.where(idMeta.name + " = " + ibatisPlaceHolderMaker.makePlaceHolder(idMeta.fieldName));
		updateNode.addContent(new CDATA(StrUtil.NewLine + updateBuilder.toSQL()));
		// delete by id
		daoMethod = new StringBuilder();//
		daoImplMethod = new StringBuilder();//
		daoMethod.append("\t").append("int");//
		daoImplMethod.append("\t").append("@Override").append(StrUtil.NewLine);
		daoImplMethod.append("\t").append("public").append(" ").append("int");//
		Element deleteNode = new Element("delete");
		root.addContent(deleteNode);
		String deleteId = "deleteById";
		deleteNode.setAttribute("id", deleteId);
		daoMethod.append(" ").append(deleteId).append("(");//
		daoImplMethod.append(" ").append(deleteId).append("(");//
		deleteNode.setAttribute("parameterType", idMeta.javaType.getSimpleName());
		DeleteBuilder deleteBuilder = DeleteBuilder.getInstance();
		deleteBuilder.deleteFrom(tableName);
		deleteBuilder.where(idMeta.name + " = " + ibatisPlaceHolderMaker.makePlaceHolder(idMeta.fieldName));
		deleteNode.addContent(new Text(StrUtil.NewLine + deleteBuilder.toSQL()));
		daoMethod.append(idMeta.javaType.getSimpleName()).append(" ").append(idMeta.fieldName);//
		daoImplMethod.append(idMeta.javaType.getSimpleName()).append(" ").append(idMeta.fieldName);//
		daoMethod.append(");");//
		daoImplMethod.append(") {").append(StrUtil.NewLine);//
		daoClassRows.add(daoMethod.toString());//
		// {
		daoImplMethod.append("\t\t").append("String sqlId = this.getNamedSqlId(\"" + deleteId + "\");").append(StrUtil.NewLine);
		daoImplMethod.append("\t\t").append("//").append(StrUtil.NewLine);
		daoImplMethod.append("\t\t").append("return this.getSqlSession().delete(sqlId, " + idMeta.fieldName + ");").append(StrUtil.NewLine);
		// }
		daoImplMethod.append("\t").append("}");//
		daoImplClassRows.add(daoImplMethod.toString());//
		/*
		 * delete by uk columns for (UniqueKeyMeta ukMeta : tableMeta.uniqueKeyMetaList) { daoMethod = new StringBuilder();// daoImplMethod = new StringBuilder();
		 * daoMethod.append("\t").append("void");// daoImplMethod.append("\t").append("@Override").append(StrUtil.NewLine); daoImplMethod.append("\t").append("public").append(" ").append("void");//
		 * String[] ukColumns = ukMeta.columnNames; deleteNode = new Element("delete"); root.addContent(deleteNode); Converter<String, String> col2FieldConverter = new Converter<String, String>() {
		 * 
		 * @Override public String convert(String src, int index) { return colFieldNameMap.get(src); } }; List<String> ukFieldNames = CollectionUtil.convertToList(TypeUtil.arrayToList(ukColumns),
		 * col2FieldConverter); List<String> ukFiledNames2 = new ArrayList<String>(); for (String ukFieldName : ukFieldNames) { ukFiledNames2.add(StrUtil.capitalize(ukFieldName)); } deleteId =
		 * "deleteBy" + StrUtil.join(ukFiledNames2, "And"); deleteNode.setAttribute("id", deleteId); daoMethod.append(" ").append(deleteId).append("(");// daoImplMethod.append(" "
		 * ).append(deleteId).append("(");// // deleteBuilder = DeleteBuilder.getInstance(); deleteBuilder.deleteFrom(tableName); List<String> daoMethodParams = new ArrayList<String>(); for (int i =
		 * 0; i < ukColumns.length; i++) { String ukColName = ukColumns[i]; deleteBuilder.where(ukColName + " = " + ibatisPlaceHolderMaker.makePlaceHolder(ukFieldNames.get(i))); ColumnMeta ukColMeta =
		 * getColumnMetaByColName(tableMeta, ukColName);
		 * 
		 * daoMethodParams.add(ukColMeta.javaType.getSimpleName() + " " + ukColMeta.fieldName); } deleteNode.addContent(new Text(StrUtil.NewLine + deleteBuilder.toSQL()));
		 * daoMethod.append(StrUtil.join(daoMethodParams, ", "));// daoImplMethod.append(StrUtil.join(daoMethodParams, ", "));// daoMethod.append(");");// daoImplMethod.append(") {"
		 * ).append(StrUtil.NewLine);// daoClassRows.add(daoMethod.toString());
		 * 
		 * // { daoImplMethod.append("\t\t").append("String sqlId = this.getNamedSqlId(\"" + deleteId + "\");").append(StrUtil.NewLine);
		 * daoImplMethod.append("\t\t").append("//").append(StrUtil.NewLine); if (ukColumns.length < 2) { ColumnMeta ukColMeta = getColumnMetaByColName(tableMeta, ukColumns[0]);
		 * daoImplMethod.append("\t\t").append("this.getSqlSession().delete(sqlId, " + ukColMeta.fieldName + ");").append(StrUtil.NewLine); } else { daoImplMethod.append("\t\t").append(
		 * "Map<String, Object> params = this.newParamMap();").append(StrUtil.NewLine); for (int i = 0; i < ukColumns.length; i++) { String ukColName = ukColumns[i]; ColumnMeta ukColMeta =
		 * getColumnMetaByColName(tableMeta, ukColName); daoImplMethod.append("\t\t").append("params.put(\"" + ukColMeta.fieldName + "\", " + ukColMeta.fieldName + ");").append(StrUtil.NewLine); }
		 * daoImplMethod.append("\t\t").append("//").append(StrUtil.NewLine); daoImplMethod.append("\t\t").append("this.getSqlSession().delete(sqlId, params);").append(StrUtil.NewLine); } // }
		 * daoImplMethod.append("\t").append("}");// daoImplClassRows.add(daoImplMethod.toString());// }
		 */
		daoClassRows.add("}");
		daoImplClassRows.add("}");

		System.out.println("========================================== java dao interface =============================================");
		System.out.println(StrUtil.join(daoClassRows, StrUtil.NewLine));
		System.out.println();

		System.out.println("======================================= java dao implementation ===========================================");
		System.out.println(StrUtil.join(daoImplClassRows, StrUtil.NewLine));
		System.out.println();

		System.out.println("=========================================== mybatis mapper xml ===============================================");
		// 把创建的xml文档写到流中
		XMLOutputter out = new XMLOutputter();
		// 设置生成xml文档的格式
		Format format = Format.getPrettyFormat();

		// 自定义xml文档的缩进(敲了四个空格，代表四个缩进)
		format.setIndent("    ");
		out.setFormat(format);
		out.output(doc, System.out);
	}

	public static boolean createDbIfNotExist() {
		// 检查并创建数据库相关对象
		try {
			Boolean dbExists = MySqlDbTool.dbExists();
			//
			if (BoolUtil.isFalse(dbExists)) {
				MySqlDbTool.createDb();
			}
			//
			return true;
		} catch (Exception e) {
			logger.error(e);
			//
			return false;
		}
	}

	public static void initDbObjects() {
		// 检查并创建数据库相关对象
		try {
			boolean dbOk = createDbIfNotExist();
			//
			if (dbOk) {
				MySqlDbTool.createOrAlterTables();
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
