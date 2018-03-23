package priv.starfish.common.jdbc;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import priv.starfish.common.util.ColumnUtil;


/**
 * 为标记为@ColumnMarked的数据模型类自动生成（并缓存）记录行到数据模型的转换器
 * 
 * @author Hu Changwei
 * 
 */
public class ColumnMarkedTypeRowMapperProvider {
	private static Map<Class<?>, ColumnMarkedTypeRowMapper<?>> cachedTypeRowMapperMap = new ConcurrentHashMap<Class<?>, ColumnMarkedTypeRowMapper<?>>();

	public static class ColumnMarkedTypeRowMapper<T> implements RowMapper<T> {
		private Class<T> modelType;
		private Map<String, ColumnInfo<Field>> colInfoMap;

		public ColumnMarkedTypeRowMapper(Class<T> modelType, Map<String, ColumnInfo<Field>> colInfoMap) {
			this.modelType = modelType;
			this.colInfoMap = colInfoMap;
		}

		@Override
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {
			try {
				T object = modelType.newInstance();
				ResultSetMetaData metadata = rs.getMetaData();
				int colCount = metadata.getColumnCount();
				for (int i = 1; i <= colCount; i++) {
					String colName = metadata.getColumnName(i).toUpperCase();
					ColumnInfo<Field> colInfo = colInfoMap.get(colName);
					if (colInfo == null) {
						continue;
					}
					Field field = colInfo.getExtra();
					Class<?> fieldType = colInfo.getFieldType();
					Object colValue = JdbcUtils.getResultSetValue(rs, i, fieldType);
					field.setAccessible(true);
					field.set(object, colValue);
				}
				return object;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> RowMapper<T> getRowMapper(final Class<T> modelType) {
		ColumnMarkedTypeRowMapper<T> typeRowMapper = null;
		if (!cachedTypeRowMapperMap.containsKey(modelType)) {
			Map<String, ColumnInfo<Field>> colInfoMap = ColumnUtil.getTypeColumnInfoMap(modelType);
			if (colInfoMap != null) {
				typeRowMapper = new ColumnMarkedTypeRowMapper<T>(modelType, colInfoMap);
			}
			cachedTypeRowMapperMap.put(modelType, typeRowMapper);
		} else {
			typeRowMapper = (ColumnMarkedTypeRowMapper<T>) cachedTypeRowMapperMap.get(modelType);
		}
		return typeRowMapper;
	}	
}
