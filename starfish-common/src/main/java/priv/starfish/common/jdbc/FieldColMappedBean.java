package priv.starfish.common.jdbc;

import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.TypeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class FieldColMappedBean {
	private Map<String, Class<?>> fieldNameTypeMap = new LinkedHashMap<String, Class<?>>();
	private Map<String, String> fieldNameColNameMap = new HashMap<String, String>();
	private Map<String, Object> fieldNameValueMap = new LinkedHashMap<String, Object>();

	public static FieldColMappedBean getInstance() {
		return new FieldColMappedBean();
	}

	public static FieldColMappedBean cloneByFields(FieldColMappedBean refInstance) {
		FieldColMappedBean newInstance = FieldColMappedBean.getInstance();
		Map<String, Class<?>> fieldNameTypeMap = refInstance.fieldNameTypeMap;
		Map<String, String> fieldNameColNameMap = refInstance.fieldNameColNameMap;
		for (String fieldName : fieldNameTypeMap.keySet()) {
			newInstance.addField(fieldNameTypeMap.get(fieldName), fieldName, fieldNameColNameMap.get(fieldName));
		}
		return newInstance;
	}

	public void addField(Class<?> fieldType, String fieldName, String colName) {
		if (!fieldNameTypeMap.containsKey(fieldName)) {
			fieldNameTypeMap.put(fieldName, TypeUtil.getWrapperType(fieldType));
			fieldNameColNameMap.put(fieldName, colName.toUpperCase());
		}
	}

	public void addField(Class<?> fieldType, String fieldName) {
		if (!fieldNameTypeMap.containsKey(fieldName)) {
			fieldNameTypeMap.put(fieldName, TypeUtil.getWrapperType(fieldType));
		}
	}

	public void addField(String fieldName, String colName) {
		this.addField(String.class, fieldName, colName);
	}

	public Class<?> getFieldType(String fieldName) {
		return this.fieldNameTypeMap.get(fieldName);
	}

	public void setColName(String fieldName, String colName) {
		if (this.fieldNameTypeMap.containsKey(fieldName)) {
			fieldNameColNameMap.put(fieldName, colName.toUpperCase());
		}
	}

	public String getColName(String fieldName) {
		return this.fieldNameColNameMap.get(fieldName);
	}

	public void setFieldValue(String fieldName, Object fieldValue) throws Exception {
		Class<?> fieldType = this.fieldNameTypeMap.get(fieldName);
		if (fieldValue == null) {
			this.fieldNameValueMap.put(fieldName, fieldType.cast(fieldValue));
		} else if (fieldType.isAssignableFrom(fieldValue.getClass())) {
			fieldType.isPrimitive();
			this.fieldNameValueMap.put(fieldName, fieldValue);
		} else {
			throw new Exception("对字段 \"" + fieldType.getSimpleName() + " " + fieldName + "\"赋值错误！");
		}
	}

	public Object getFieldValue(String fieldName) {
		return this.fieldNameValueMap.get(fieldName);
	}

	public List<String> getFieldNames() {
		return new ArrayList<String>(this.fieldNameTypeMap.keySet());
	}

	public void clearFieldValues() {
		this.fieldNameValueMap.clear();
	}

	@Override
	public String toString() {
		List<String> items = new ArrayList<String>();
		for (String fieldName : this.fieldNameTypeMap.keySet()) {
			Class<?> fieldType = this.fieldNameTypeMap.get(fieldName);
			String colName = this.fieldNameColNameMap.get(fieldName);
			Object fieldValue = this.fieldNameValueMap.get(fieldName);
			items.add(fieldType.getSimpleName() + " " + fieldName + " (" + colName + ") : " + fieldValue);
		}
		return JsonUtil.toJson(items);
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (String fieldName : this.fieldNameTypeMap.keySet()) {
			Object fieldValue = this.fieldNameValueMap.get(fieldName);
			map.put(fieldName, fieldValue);
		}
		return map;
	}
}
