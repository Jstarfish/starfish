package priv.starfish.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import priv.starfish.common.model.SelectItem;


public class EnumUtil {
	private static final Log logger = LogFactory.getLog(EnumUtil.class);

	//
	public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {
		T retValue = null;
		try {
			retValue = Enum.valueOf(enumType, name);
		} catch (Exception ex) {
			//
		}
		return retValue;
	}

	//
	public static Map<String, String> toValueTextMap(Class<? extends Enum<?>> enumClass) {
		Field textField = null;
		Field valueField = null;
		try {
			textField = enumClass.getDeclaredField("text");
			try {
				valueField = enumClass.getDeclaredField("value");
			} catch (Exception exx) {
				//
			}
		} catch (Exception ex) {
			try {
				textField = enumClass.getDeclaredField("value");
			} catch (Exception exx) {
				logger.error(exx);
			}
		}
		Map<String, String> retMap = new LinkedHashMap<String, String>();
		Enum<?>[] enumElems = enumClass.getEnumConstants();
		for (int i = 0; i < enumElems.length; i++) {
			Enum<?> enumElem = enumElems[i];
			String name = enumElem.name();
			String value = name;
			if (valueField != null) {
				try {
					valueField.setAccessible(true);
					value = String.valueOf(valueField.get(enumElem));
				} catch (Exception e) {
					logger.warn(e);
				}
			}
			String text = name;
			if (textField != null) {
				try {
					textField.setAccessible(true);
					text = String.valueOf(textField.get(enumElem));
				} catch (Exception e) {
					logger.warn(e);
				}
			}
			retMap.put(value, text);
		}
		return retMap;
	}

	public static List<SelectItem> toSelectItems(Class<? extends Enum<?>> enumClass) {
		Field textField = null;
		Field valueField = null;
		try {
			textField = enumClass.getDeclaredField("text");
			try {
				valueField = enumClass.getDeclaredField("value");
			} catch (Exception exx) {
				//
			}
		} catch (Exception ex) {
			try {
				textField = enumClass.getDeclaredField("value");
			} catch (Exception exx) {
				logger.error(exx);
			}
		}
		//
		List<SelectItem> selectItems = new ArrayList<SelectItem>();
		Enum<?>[] enumElems = enumClass.getEnumConstants();
		for (int i = 0; i < enumElems.length; i++) {
			Enum<?> enumElem = enumElems[i];
			String name = enumElem.name();
			String value = name;
			if (valueField != null) {
				try {
					valueField.setAccessible(true);
					value = String.valueOf(valueField.get(enumElem));
				} catch (Exception e) {
					logger.warn(e);
				}
			}
			String text = name;
			if (textField != null) {
				try {
					textField.setAccessible(true);
					text = String.valueOf(textField.get(enumElem));
				} catch (Exception e) {
					logger.warn(e);
				}
			}
			SelectItem item = SelectItem.newOne(value, text);
			selectItems.add(item);
		}
		return selectItems;
	}

	// ===========================================================================================================
	public static Map<String, Object> toNameValueMap(Class<? extends Enum<?>> enumClass, String valueFieldName) {
		if (StrUtil.isNullOrBlank(valueFieldName)) {
			valueFieldName = "name";
		}
		Field valueField = null;
		try {
			if (!valueFieldName.equals("name")) {
				valueField = enumClass.getDeclaredField(valueFieldName);
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		Map<String, Object> retMap = new LinkedHashMap<String, Object>();
		Enum<?>[] enumElems = enumClass.getEnumConstants();
		for (int i = 0; i < enumElems.length; i++) {
			Enum<?> enumElem = enumElems[i];
			String name = enumElem.name();
			Object value = name;
			if (valueField != null) {
				try {
					valueField.setAccessible(true);
					value = valueField.get(enumElem);
				} catch (Exception e) {
					logger.warn(e);
				}
			}
			retMap.put(name, value);
		}
		return retMap;
	}
}
