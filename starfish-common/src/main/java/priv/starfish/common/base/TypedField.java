package priv.starfish.common.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * @author Hu Changwei
 * @date 2013-06-11
 * 
 *       <pre>
 * 功能：带类型的字段/列信息类
 * </pre>
 */
public class TypedField implements Serializable {
	private static final long serialVersionUID = -4677748722297786418L;
	private static final String EmptyStr = "";

	private static boolean hasText(String chkStr) {
		return chkStr != null && !chkStr.trim().equals(EmptyStr);
	}

	/**
	 * 支持的列基本类型
	 * 
	 */
	public static enum Type {
		String, Number, Date, Boolean;
		/**
		 * 从字符串解析类型
		 * 
		 * @param typeName
		 * @return
		 */
		public static Type valueof(String typeName) {
			if (!hasText(typeName)) {
				return String;
			}
			typeName = typeName.trim().toLowerCase();
			if (typeName.startsWith("str")) {
				return String;
			} else if (typeName.startsWith("num")) {
				return Number;
			} else if (typeName.startsWith("date")) {
				return Date;
			} else if (typeName.startsWith("bool")) {
				return Boolean;
			} else {
				return String;
			}
		}

	}

	/**
	 * 默认日期格式
	 */
	public static final String DefaultDateFormatStr = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 默认boolean格式
	 */
	public static final String DefaultBoolFormatStr = Boolean.TRUE.toString() + "/" + Boolean.FALSE.toString();

	/**
	 * 解析boolean值(true/false)代码对
	 * 
	 * @param booleanFormatStr
	 * @return (true/false)代码对
	 */
	public static final String[] parseBooleanPair(String booleanFormatStr) {
		if (!hasText(booleanFormatStr)) {
			booleanFormatStr = DefaultBoolFormatStr;
		}
		String[] pair = booleanFormatStr.split("/", -1);
		pair[0] = pair[0].trim();
		pair[1] = pair[1].trim();
		return pair;
	}

	/**
	 * 编码boolean值
	 * 
	 * @param booleanValues
	 * @param booleanValue
	 * @return boolean 代码值
	 */
	public static final String encodeBoolean(String[] booleanValues, Boolean booleanValue) {
		if (booleanValue == null) {
			return null;
		} else if (Boolean.TRUE.equals(booleanValue)) {
			return booleanValues[0];
		} else if (Boolean.FALSE.equals(booleanValue)) {
			return booleanValues[1];
		} else {
			return null;
		}
	}

	/**
	 * 解码boolean值
	 * 
	 * @param booleanValues
	 *            True/False 字符串代码（如：Y/N）
	 * @param booleanValue
	 *            如 Y
	 * @return
	 */
	public static final Boolean decodeBoolean(String[] booleanValues, String booleanValue) {
		if (booleanValues[0].equalsIgnoreCase(booleanValue)) {
			return Boolean.TRUE;
		} else if (booleanValues[1].equalsIgnoreCase(booleanValue)) {
			return Boolean.FALSE;
		} else {
			return null;
		}
	}

	private static final String[] NumTypes = new String[] { "double", "float", "int", "long", "short", "byte" };

	/**
	 * 判断给定的数值类型是否在支持范围之内
	 * 
	 * @param numType
	 * @return
	 */
	public static final boolean isSupportedNumberType(String numType) {
		if (!hasText(numType)) {
			return false;
		}
		for (int i = 0; i < NumTypes.length; i++) {
			if (NumTypes[i].equalsIgnoreCase(numType)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 各种数据类型的默认对齐方式
	 */
	public static final Map<Type, String> DefaultTypeAligns;
	static {
		DefaultTypeAligns = new HashMap<Type, String>();
		DefaultTypeAligns.put(Type.String, "left");
		DefaultTypeAligns.put(Type.Number, "right");
		DefaultTypeAligns.put(Type.Date, "center");
		DefaultTypeAligns.put(Type.Boolean, "center");
	}

	//
	public TypedField() {
		super();
	}

	public TypedField(String name, String typeName, String format, String subType) {
		super();
		this.name = name.trim();
		this.type = Type.valueof(typeName);
		if (hasText(format)) {
			this.format = format.trim();
		}
		if (hasText(subType)) {
			this.subType = subType.trim();
		}
	}

	public TypedField(String name, String typeName, String format) {
		super();
		this.name = name.trim();
		this.type = Type.valueof(typeName);
		if (hasText(format)) {
			this.format = format.trim();
		}
	}

	public TypedField(String name, String typeName) {
		super();
		this.name = name.trim();
		this.type = Type.valueof(typeName);
	}

	public TypedField(String name) {
		super();
		this.name = name.trim();
		this.type = Type.String;
	}

	private String name = null;// （导入导出的）列名称
	private Type type = Type.String;// 列数据类型 str[ing], num[ber], date, bool[ean]
	private String subType = null;// 对Number : byte, double, float, int, long,
									// and short
	private String format = null;// 数据类型格式化字符串
	private String align;// 对齐方式
	private boolean wrap = false;// 是否折行
	private int widthInChars = -1; // 宽度（按字符数）
	private Object nullAs = null;// 空值替代
	private Object extra = null;// 额外信息
	private int srcPos = -1;// 数据源列位置（从1开始）

	public String getName() {
		return name;
	}

	public TypedField setName(String name) {
		this.name = name.trim();
		return this;
	}

	public Type getType() {
		return type;
	}

	public TypedField setType(Type type) {
		this.type = type;
		return this;
	}

	public TypedField setType(String typeName) {
		this.type = Type.valueof(typeName);
		return this;
	}

	public String getSubType() {
		return subType;
	}

	public TypedField setSubType(String subType) {
		if (hasText(subType)) {
			this.subType = subType.trim();
		}
		return this;
	}

	public String getFormat() {
		return format;
	}

	public TypedField setFormat(String format) {
		if (hasText(format)) {
			this.format = format.trim();
		}
		return this;
	}

	public String getAlign() {
		return !hasText(align) ? DefaultTypeAligns.get(this.type) : align;
	}

	public TypedField setAlign(String align) {
		this.align = align;
		return this;
	}

	public boolean isWrap() {
		return wrap;
	}

	public TypedField setWrap(boolean wrap) {
		this.wrap = wrap;
		return this;
	}

	public int getWidthInChars() {
		return widthInChars;
	}

	public TypedField setWidthInChars(int widthInChars) {
		this.widthInChars = widthInChars;
		return this;
	}

	public Object getNullAs() {
		return nullAs;
	}

	public TypedField setNullAs(Object nullAs) {
		this.nullAs = nullAs;
		return this;
	}

	public Object getExtra() {
		return extra;
	}

	public TypedField setExtra(Object extra) {
		this.extra = extra;
		return this;
	}

	public int getSrcPos() {
		return srcPos;
	}

	public TypedField setSrcPos(int srcPos) {
		this.srcPos = srcPos;
		return this;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(name).append(type).append(subType).append(format).append(align)
				.append(wrap).append(widthInChars).append(nullAs).append(extra).append(srcPos).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		TypedField another = (TypedField) obj;
		return new EqualsBuilder().append(name, another.name).append(type, another.type)
				.append(subType, another.subType).append(format, another.format).append(align, another.align)
				.append(wrap, another.wrap).append(widthInChars, another.widthInChars).append(nullAs, another.nullAs)
				.append(extra, another.extra).append(srcPos, another.srcPos).isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("name", name).append("type", type)
				.append("subType", subType).append("format", format).append("align", align).append("wrap", wrap)
				.append("widthInChars", widthInChars).append("nullAs", nullAs).append("extra", extra)
				.append("srcPos", srcPos).toString();
	}
}
