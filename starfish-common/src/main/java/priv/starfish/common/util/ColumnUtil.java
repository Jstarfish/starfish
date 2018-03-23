package priv.starfish.common.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.jdbc.ColumnInfo;
import priv.starfish.common.jdbc.FieldColMappedBean;

public final class ColumnUtil {
	private ColumnUtil() {
		//
	}

	public static Map<String, ColumnInfo<Field>> getTypeColumnInfoMap(final Class<?> modelType) {
		Map<String, ColumnInfo<Field>> retMap = new HashMap<String, ColumnInfo<Field>>();
		Field[] typeFields = modelType.getDeclaredFields();
		for (int i = 0, j = typeFields.length; i < j; i++) {
			Field field = typeFields[i];
			field.setAccessible(true);
			if (field.isAnnotationPresent(Column.class)) {
				Column col = field.getAnnotation(Column.class);
				if (col != null) {
					String fieldName = field.getName();
					String colName = col.name();
					if (!StrUtil.hasText(colName)) {
						colName = fieldName;
					}
					colName = colName.toUpperCase();
					Class<?> fieldType = field.getType();
					ColumnInfo<Field> info = new ColumnInfo<Field>();
					info.setColName(colName);
					info.setFieldName(fieldName);
					info.setFieldType(fieldType);
					info.setExtra(field);
					retMap.put(colName, info);
				}
			}
		}
		return retMap;
	}

	public static <T> List<T> rowMapListToColumnMarkedObjectList(final List<Map<String, Object>> rowMapList,
			final Class<T> modelType) {
		if (rowMapList == null) {
			return null;
		}
		List<T> retList = TypeUtil.newList(modelType);
		if (rowMapList.size() > 0) {
			Map<String, ColumnInfo<Field>> colInfoMap = getTypeColumnInfoMap(modelType);
			if (colInfoMap == null) {
				return null;
			}
			try {
				for (int i = 0, j = rowMapList.size(); i < j; i++) {
					Map<String, Object> rowMap = rowMapList.get(i);
					T object = modelType.newInstance();
					for (String colName : rowMap.keySet()) {
						ColumnInfo<Field> colInfo = colInfoMap.get(colName.toUpperCase());
						if (colInfo == null) {
							continue;
						}
						Field field = colInfo.getExtra();
						Object colValue = rowMap.get(colName);
						field.setAccessible(true);
						field.set(object, colValue);
					}
					retList.add(object);
				}
			} catch (Exception e) {
				return null;
			}
		}
		return retList;
	}

	// =========================================================================================

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, ColumnInfo> getBeanColumnInfoMap(final FieldColMappedBean mappedBean) {
		Map<String, ColumnInfo> retMap = new HashMap<String, ColumnInfo>();
		List<String> fieldNames = mappedBean.getFieldNames();
		for (int i = 0, j = fieldNames.size(); i < j; i++) {
			String fieldName = fieldNames.get(i);
			String colName = mappedBean.getColName(fieldName);
			if (colName == null) {
				continue;
			}
			Class<?> fieldType = mappedBean.getFieldType(fieldName);
			ColumnInfo info = new ColumnInfo();
			info.setColName(colName);
			info.setFieldName(fieldName);
			info.setFieldType(fieldType);
			retMap.put(colName, info);
		}
		return retMap;
	}

	@SuppressWarnings("rawtypes")
	public static List<FieldColMappedBean> rowMapListToFieldColMappedBeanList(
			final List<Map<String, Object>> rowMapList, final FieldColMappedBean refMappedBean) {
		if (rowMapList == null) {
			return null;
		}
		List<FieldColMappedBean> retList = TypeUtil.newList(FieldColMappedBean.class);
		if (rowMapList.size() > 0) {
			Map<String, ColumnInfo> colInfoMap = getBeanColumnInfoMap(refMappedBean);
			if (colInfoMap == null) {
				return null;
			}
			try {
				for (int i = 0, j = rowMapList.size(); i < j; i++) {
					Map<String, Object> rowMap = rowMapList.get(i);
					FieldColMappedBean object = FieldColMappedBean.cloneByFields(refMappedBean);
					for (String colName : rowMap.keySet()) {
						ColumnInfo colInfo = colInfoMap.get(colName.toUpperCase());
						if (colInfo == null) {
							continue;
						}
						String fieldName = colInfo.getFieldName();
						Object colValue = rowMap.get(colName);
						object.setFieldValue(fieldName, colValue);
					}
					retList.add(object);
				}
			} catch (Exception e) {
				return null;
			}
		}
		return retList;
	}
}
