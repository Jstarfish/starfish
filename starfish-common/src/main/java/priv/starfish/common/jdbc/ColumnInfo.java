package priv.starfish.common.jdbc;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用于记录数据模型对应的数据库表列名称 与 字段的映射信息
 * 
 * @author Hu Changwei
 * 
 */
public class ColumnInfo<TExtra> {
	private String colName;
	private String fieldName;
	private Class<?> fieldType;
	private TExtra extra;

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Class<?> getFieldType() {
		return fieldType;
	}

	public void setFieldType(Class<?> fieldType) {
		this.fieldType = fieldType;
	}

	public TExtra getExtra() {
		return extra;
	}

	public void setExtra(TExtra extra) {
		this.extra = extra;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("colName", colName)
				.append("fieldName", fieldName).append("fieldType", fieldType.getSimpleName()).toString();
	}
}