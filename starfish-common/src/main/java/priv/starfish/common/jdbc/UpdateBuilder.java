package priv.starfish.common.jdbc;

import priv.starfish.common.util.StrUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author Hu Changwei
 * @version 1.0
 */
public class UpdateBuilder extends SqlBuilder {
	// set 语句生成器
	public static interface SetClauseBuilder {
		void filterItem(String colName, Object value, Map<String, Object> valuePairs, UpdateBuilder updateBuilder, IPlaceHolderMaker placeHolderMaker);

		String finish(Map<String, Object> itemPairs, UpdateBuilder updateBuilder);
	}

	//
	public static UpdateBuilder getInstance() {
		return new UpdateBuilder();
	}

	public UpdateBuilder update(String tableName) {
		this.isFieldState = false;
		this.tableName = tableName;
		return this;
	}

	protected SetClauseBuilder setClauseBuilder;
	protected Map<String, Object> valuePairs = new LinkedHashMap<String, Object>();
	protected String lastColName = null;

	public UpdateBuilder withSetClauseBuilder(SetClauseBuilder setClauseBuilder) {
		this.setClauseBuilder = setClauseBuilder;
		return this;
	}

	public UpdateBuilder set(String colName, Object value) {
		this.isFieldState = true;
		this.lastColName = colName;
		valuePairs.put(colName, value);
		return this;
	}

	public UpdateBuilder set(String colName) {
		return this.set(colName, JdbcParamPlaceHolder);
	}

	public UpdateBuilder asPlaceHolder() {
		if (this.isFieldState) {
			Object lastValue = this.valuePairs.get(this.lastColName);
			if (this.setClauseBuilder == null) {
				if (lastValue != null && lastValue instanceof String) {
					String valueStr = (String) lastValue;
					if (JdbcParamPlaceHolder.equals(valueStr)) {
						valueStr = this.lastColName;
					}
					if (!this.placeHolderMaker.isPlaceHolder(valueStr)) {
						this.valuePairs.put(this.lastColName, this.placeHolderMaker.makePlaceHolder(valueStr));
					}
				}
			} else {
				this.setClauseBuilder.filterItem(this.lastColName, lastValue, valuePairs, this, this.placeHolderMaker);
			}
			//
			this.isFieldState = false;
			this.lastColName = null;
		}
		return this;
	}

	@Override
	public String toSQL() throws BuildingException {
		if (!StrUtil.hasText(this.tableName)) {
			throw new BuildingException("表名不能为空。");
		}
		List<String> colNames = new ArrayList<String>(this.valuePairs.keySet());
		int colCount = colNames.size();
		if (colCount < 1) {
			throw new BuildingException("必须要有更新列。");
		}
		String setClause = null;
		if (this.setClauseBuilder == null) {
			List<String> setStrList = new ArrayList<String>(colCount);
			for (int i = 0; i < colCount; i++) {
				String colName = colNames.get(i);
				Object value = this.valuePairs.get(colName);
				String setStr = null;
				if (!this.isPlaceHolder(value)) {
					setStr = colName + " = " + toValueStr(value);
				} else {
					setStr = colName + " = " + value.toString();
				}
				if (i > 0) {
					setStr = "    " + setStr;
				}
				setStrList.add(setStr);
			}
			setClause = "SET " + StrUtil.join(setStrList, " ,\n");
		} else {
			setClause = this.setClauseBuilder.finish(this.valuePairs, this);
		}
		StringBuilder sb = new StringBuilder();
		//
		sb.append("UPDATE ").append(this.tableName);
		sb.append("\n");
		sb.append(setClause);
		//
		if (whereList.size() > 0) {
			sb.append("\n");
			sb.append("WHERE  ").append(getWhereStr());
		}
		//
		String prepended = this.prependList.size() > 0 ? StrUtil.join(this.prependList, "\n") + "\n" : "";
		String appended = this.appendList.size() > 0 ? "\n" + StrUtil.join(this.appendList, "\n") : "";
		return prepended + sb.toString() + appended;
	}
}
