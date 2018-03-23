package priv.starfish.common.jdbc;

import priv.starfish.common.util.StrUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * TBC ...
 * 
 * @author Hu Changwei
 * @version 1.0
 */
public class InsertBuilder extends SqlBuilder {
	public static InsertBuilder getInstance() {
		return new InsertBuilder();
	}

	public InsertBuilder insertInto(String tableName) {
		this.isFieldState = false;
		this.tableName = tableName;
		return this;
	}

	protected Map<String, Object> valuePairs = new LinkedHashMap<String, Object>();
	protected String lastColName = null;

	public InsertBuilder value(String colName, Object value) {
		this.isFieldState = true;
		this.lastColName = colName;
		valuePairs.put(colName, value);
		return this;
	}

	public InsertBuilder value(String colName) {
		return this.value(colName, JdbcParamPlaceHolder);
	}

	public InsertBuilder asPlaceHolder() {
		if (this.isFieldState) {
			Object lastValue = this.valuePairs.get(this.lastColName);
			if (lastValue != null && lastValue instanceof String) {
				String valueStr = (String) lastValue;
				if (JdbcParamPlaceHolder.equals(valueStr)) {
					valueStr = this.lastColName;
				}
				if (!this.placeHolderMaker.isPlaceHolder(valueStr)) {
					this.valuePairs.put(this.lastColName, this.placeHolderMaker.makePlaceHolder(valueStr));
				}
			}
			//
			this.isFieldState = false;
			this.lastColName = null;
		}
		return this;
	}

	private final static int VALUES_LEN = "VALUES".length();

	@Override
	public String toSQL() throws BuildingException {
		if (!StrUtil.hasText(this.tableName)) {
			throw new BuildingException("表名不能为空。");
		}
		List<String> colNames = new ArrayList<String>(this.valuePairs.keySet());
		int colCount = colNames.size();
		if (colCount < 1) {
			throw new BuildingException("必须要有插入列。");
		}
		List<String> valueStrList = new ArrayList<String>(colCount);
		for (int i = 0; i < colCount; i++) {
			String colName = colNames.get(i);
			Object value = this.valuePairs.get(colName);
			if (!this.isPlaceHolder(value)) {
				valueStrList.add(toValueStr(value));
			} else {
				valueStrList.add(value.toString());
			}
		}
		StringBuilder sb = new StringBuilder();
		//
		int alignLen = Math.max(this.tableName.length(), VALUES_LEN);
		sb.append("INSERT INTO ").append("\n").append(StrUtil.leftPadding(this.tableName, alignLen)).append("(");
		sb.append(StrUtil.join(colNames, ", "));
		sb.append(")");
		sb.append("\n").append(StrUtil.leftPadding("VALUES", alignLen)).append("(");
		sb.append(StrUtil.join(valueStrList, ", "));
		sb.append(")");
		//
		String prepended = this.prependList.size() > 0 ? StrUtil.join(this.prependList, "\n") + "\n" : "";
		String appended = this.appendList.size() > 0 ? "\n" + StrUtil.join(this.appendList, "\n") : "";
		return prepended + sb.toString() + appended;
	}
}
