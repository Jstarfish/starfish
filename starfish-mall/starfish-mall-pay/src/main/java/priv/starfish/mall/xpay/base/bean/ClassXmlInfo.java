package priv.starfish.mall.xpay.base.bean;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.TypeUtil;

public class ClassXmlInfo {
	public static final String EXTENDS_KEY = "-extends-";
	public static final String ROOT_KEY = "-xml-";
	public static final String DESC_KEY = "-desc-";
	//
	@JsonIgnore
	public Class<?> clazz;
	public String className;
	public String xmlName;
	public String desc;
	//
	public Map<String, FieldXmlInfo> field2XmlNames = TypeUtil.newOrderedMap(String.class, FieldXmlInfo.class);
	public Map<String, FieldXmlInfo> xmlName2Fields = TypeUtil.newOrderedMap(String.class, FieldXmlInfo.class);

	public static ClassXmlInfo newOne() {
		return new ClassXmlInfo();
	}

	@Override
	public String toString() {
		return "ClassXmlInfo [className=" + className + ", xmlName=" + xmlName + ", desc=" + desc + ", field2XmlNames=" + field2XmlNames + ", xmlName2Fields=" + xmlName2Fields + "]";
	}

	public void parseFromMapping(Map<String, Object> mapping, Map<String, String> classXmlNames) {
		if (this.className == null) {
			this.className = this.clazz.getName();
		}
		if (mapping == null) {
			mapping = new HashMap<String, Object>(0);
		}
		//
		String classXmlName = classXmlNames == null ? null : classXmlNames.get(this.className);
		if (StrUtil.isNullOrBlank(classXmlName)) {
			classXmlName = this.clazz.getSimpleName();
			if (classXmlNames != null) {
				classXmlNames.put(this.className, classXmlName);
			}
		}
		this.xmlName = classXmlName;
		//
		if (mapping.containsKey(ClassXmlInfo.DESC_KEY)) {
			this.desc = (String) mapping.get(ClassXmlInfo.DESC_KEY);
			mapping.remove(ClassXmlInfo.DESC_KEY);
			// System.out.println("desc : " + desc);
		}
		//
		List<Field> fields = TypeUtil.getClassFields(this.clazz);
		for (Field field : fields) {
			String fieldName = field.getName();
			FieldXmlInfo fieldXmlInfo = FieldXmlInfo.newOne();
			fieldXmlInfo.clazz = this.clazz;
			fieldXmlInfo.field = field;
			fieldXmlInfo.fieldName = fieldName;
			fieldXmlInfo.fieldType = field.getType();
			//
			Object fieldMapping = mapping.get(fieldName);
			fieldXmlInfo.parseFromMapping(fieldMapping, classXmlNames);
			//
			field2XmlNames.put(fieldName, fieldXmlInfo);
			//
			String fieldXmlName = fieldXmlInfo.xmlName;
			xmlName2Fields.put(fieldXmlName, fieldXmlInfo);
		}
	}

}
