package priv.starfish.mall.xpay.base.bean;

import java.lang.reflect.Field;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.xpay.base.annotation.XmlMapped;

public class FieldXmlInfo {
	@JsonIgnore
	public Class<?> clazz;
	@JsonIgnore
	public Field field;
	public String fieldName;
	public Class<?> fieldType;
	public String fieldFormat;
	public String xmlName;
	public String desc;
	public String nullAs;

	public static FieldXmlInfo newOne() {
		return new FieldXmlInfo();
	}

	@Override
	public String toString() {
		return "FieldXmlInfo [fieldName=" + fieldName + ", fieldType=" + fieldType.getName() + ", fieldFormat=" + fieldFormat + ", xmlName=" + xmlName + ", desc=" + desc + ", nullAs=" + nullAs + "]";
	}

	@SuppressWarnings("unchecked")
	public void parseFromMapping(Object mapping, Map<String, String> classXmlNames) {
		String tmpStr = null;
		if (mapping != null && Map.class.isAssignableFrom(mapping.getClass())) {
			Map<String, Object> mappingX = (Map<String, Object>) mapping;
			//
			tmpStr = (String) mappingX.get("format");
			if (StrUtil.hasText(tmpStr)) {
				this.fieldFormat = tmpStr.trim();
			}
			//
			tmpStr = (String) mappingX.get("xml");
			if (StrUtil.hasText(tmpStr)) {
				this.xmlName = tmpStr.trim();
			} else {
				this.xmlName = this.fieldName;
			}
			//
			if (mappingX.containsKey("desc")) {
				this.desc = (String) mappingX.get("desc");
				// System.out.println("desc : " + desc);
			}
			//
			if (mappingX.containsKey("nullAs")) {
				this.nullAs = (String) mappingX.get("nullAs");
				// System.out.println("nullAs : " + nullAs);
			}
		} else {
			tmpStr = (String) mapping;
			if (StrUtil.hasText(tmpStr)) {
				tmpStr = tmpStr.trim();
			} else {
				if (this.fieldType != null && this.fieldType.isAnnotationPresent(XmlMapped.class) && classXmlNames != null) {
					tmpStr = classXmlNames.get(this.fieldType.getName());
				}
				if (tmpStr == null) {
					tmpStr = this.fieldName;
				}
			}
			this.xmlName = tmpStr;
		}
	}
}
