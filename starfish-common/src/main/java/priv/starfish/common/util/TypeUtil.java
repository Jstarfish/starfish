package priv.starfish.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import priv.starfish.common.map.ParamMapping;
import priv.starfish.common.model.RelChangeInfo;

/**
 * 
 * @author Hu Changwei
 * @date 2013-12-15
 * 
 */
public class TypeUtil {
	public static class TypeRefs {
		public static TypeReference<List<String>> StringListType = new TypeReference<List<String>>() {
		};
		public static TypeReference<List<Integer>> IntegerListType = new TypeReference<List<Integer>>() {
		};
		public static TypeReference<List<Boolean>> BooleanListType = new TypeReference<List<Boolean>>() {
		};
		public static TypeReference<List<Map<String, Object>>> StringObjectMapListType = new TypeReference<List<Map<String, Object>>>() {
		};
		public static TypeReference<Map<String, Object>> StringObjectMapType = new TypeReference<Map<String, Object>>() {
		};
		public static TypeReference<Map<String, String>> StringStringMapType = new TypeReference<Map<String, String>>() {
		};
		public static TypeReference<Map<String, Integer>> StringIntegerMapType = new TypeReference<Map<String, Integer>>() {
		};
		public static TypeReference<Map<String, Boolean>> StringBooleanMapType = new TypeReference<Map<String, Boolean>>() {
		};
		//
		public static TypeReference<List<RelChangeInfo>> RelChangeInfoListType = new TypeReference<List<RelChangeInfo>>() {
		};
		public static TypeReference<List<ParamMapping>> ParamMappingListType = new TypeReference<List<ParamMapping>>() {
		};

	}

	public static class Types {
		public final static List<String> StringList = new ArrayList<String>(0);
		public final static List<Integer> IntegerList = new ArrayList<Integer>(0);
		public final static List<Boolean> BooleanList = new ArrayList<Boolean>(0);
		public final static List<Long> LongList = new ArrayList<Long>(0);
		//
		public final static List<Map<String, Object>> StringObjectMapList = new ArrayList<Map<String, Object>>();
		public final static Map<String, Object> StringObjectMap = new HashMap<String, Object>();
		public final static Map<String, String> StringStringMap = new HashMap<String, String>();
		public final static Map<String, Integer> StringIntegerMap = new HashMap<String, Integer>();
		public final static Map<String, Boolean> StringBooleanMap = new HashMap<String, Boolean>();
		//
		public final static List<RelChangeInfo> RelChangeInfoList = new ArrayList<RelChangeInfo>();
		public final static List<ParamMapping> ParamMappingList = new ArrayList<ParamMapping>();
	}

	// -----------------------------------------------------------------------
	public static List<Integer> toIntegerList(List<? extends Number> numList) {
		List<Integer> retList = null;
		if (numList != null) {
			retList = new ArrayList<Integer>();
			for (int i = 0; i < numList.size(); i++) {
				Number numVal = numList.get(i);
				Integer val = null;
				if (numVal != null) {
					val = numVal.intValue();
				}
				retList.add(val);
			}
		}
		return retList;
	}

	public static List<Integer> toIntegerListX(List<String> strList) {
		List<Integer> retList = null;
		if (strList != null) {
			retList = new ArrayList<Integer>();
			for (int i = 0; i < strList.size(); i++) {
				String str = strList.get(i);
				Integer val = null;
				if (str != null) {
					val = Integer.parseInt(str);
				}
				retList.add(val);
			}
		}
		return retList;
	}

	public static List<Long> toLongList(List<? extends Number> numList) {
		List<Long> retList = null;
		if (numList != null) {
			retList = new ArrayList<Long>();
			for (int i = 0; i < numList.size(); i++) {
				Number numVal = numList.get(i);
				Long val = null;
				if (numVal != null) {
					val = numVal.longValue();
				}
				retList.add(val);
			}
		}
		return retList;
	}

	public static List<Long> toLongListX(List<String> strList) {
		List<Long> retList = null;
		if (strList != null) {
			retList = new ArrayList<Long>();
			for (int i = 0; i < strList.size(); i++) {
				String str = strList.get(i);
				Long val = null;
				if (str != null) {
					val = Long.parseLong(str);
				}
				retList.add(val);
			}
		}
		return retList;
	}

	public static List<Boolean> toBooleanListX(List<String> strList) {
		List<Boolean> retList = null;
		if (strList != null) {
			retList = new ArrayList<Boolean>();
			for (int i = 0; i < strList.size(); i++) {
				String str = strList.get(i);
				Boolean val = null;
				if (str != null) {
					val = Boolean.parseBoolean(str);
				}
				retList.add(val);
			}
		}
		return retList;
	}

	// -----------------------------------------------------------------------
	public static Map<Integer, Object> toIntegerObjectMap(Map<String, Object> orgMap) {
		Map<Integer, Object> retMap = null;
		if (orgMap != null) {
			retMap = new HashMap<Integer, Object>();
			for (Map.Entry<String, Object> orgEntry : orgMap.entrySet()) {
				String orgKey = orgEntry.getKey();
				retMap.put(Integer.parseInt(orgKey), orgEntry.getValue());
			}
		}
		return retMap;
	}

	public static Map<Integer, Integer> toIntegerIntegerMap(Map<String, Integer> orgMap) {
		Map<Integer, Integer> retMap = null;
		if (orgMap != null) {
			retMap = new HashMap<Integer, Integer>();
			for (Map.Entry<String, Integer> orgEntry : orgMap.entrySet()) {
				String orgKey = orgEntry.getKey();
				retMap.put(Integer.parseInt(orgKey), orgEntry.getValue());
			}
		}
		return retMap;
	}

	public static Map<Integer, Boolean> toIntegerBooleanMap(Map<String, Boolean> orgMap) {
		Map<Integer, Boolean> retMap = null;
		if (orgMap != null) {
			retMap = new HashMap<Integer, Boolean>();
			for (Map.Entry<String, Boolean> orgEntry : orgMap.entrySet()) {
				String orgKey = orgEntry.getKey();
				retMap.put(Integer.parseInt(orgKey), orgEntry.getValue());
			}
		}
		return retMap;
	}

	// -----------------------------------------------------------------------
	public static <T> boolean isNullOrEmpty(T[] array) {
		return array == null || array.length == 0;
	}

	public static <T> boolean isNullOrEmpty(Collection<T> list) {
		return list == null || list.size() == 0;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] newArray(Class<T> elemType, int length) {
		return (T[]) Array.newInstance(elemType, length);
	}

	public static <T> T[] newEmptyArray(Class<T> elemType) {
		return newArray(elemType, 0);
	}

	public static <T> List<T> newList(Class<T> elemType) {
		return new ArrayList<T>();
	}

	public static <T> List<T> newEmptyList(Class<T> elemType) {
		return new ArrayList<T>(0);
	}

	public static <TK, TV> Map<TK, TV> newMap(Class<TK> keyType, Class<TV> valueType) {
		return new HashMap<TK, TV>();
	}

	public static <TK, TV> Map<TK, TV> newOrderedMap(Class<TK> keyType, Class<TV> valueType) {
		return new LinkedHashMap<TK, TV>();
	}

	public static Map<String, Object> newOrderedStringObjectMap() {
		return new LinkedHashMap<String, Object>();
	}

	public static <T> List<T> arrayToList(T[] array) {
		if (array == null) {
			return null;
		}
		return Arrays.asList(array);
	}

	public static <T> T[] listToArray(List<T> list, Class<T> elemType) {
		if (list == null) {
			return null;
		}
		T[] array = newArray(elemType, list.size());
		return list.toArray(array);
	}

	public static List<Object> toObjectList(List<?> anyList) {
		List<Object> retList = new ArrayList<Object>();
		for (int i = 0, j = anyList.size(); i < j; i++) {
			retList.add(anyList.get(i));
		}
		return retList;
	}

	// ===============================================================================

	@SuppressWarnings("rawtypes")
	public static boolean areEqual(Object arrA, Object arrB) {
		if (arrA == arrB) {
			return true;
		}
		if (arrA == null && arrB != null || arrA != null && arrB == null) {
			return false;
		}
		Class clsA = arrA.getClass();
		Class clsB = arrB.getClass();
		if (clsA.isArray()) {
			if (!clsB.isArray()) {
				return false;
			}
		} else {
			if (clsB.isArray()) {
				return false;
			} else {
				return arrA.equals(arrB);
			}
		}
		int len = Array.getLength(arrA);
		if (Array.getLength(arrB) != len) {
			return false;
		}
		for (int i = 0; i < len; i++) {
			if (!areEqual(Array.get(arrA, i), Array.get(arrB, i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 根据Map的值生成排过序的Map
	 * 
	 * @param srcMap
	 * @param desc
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <K, V extends Comparable> Map<K, V> sortMapByValues(Map<K, V> srcMap, boolean desc) {
		List<Map.Entry<K, V>> entries = new ArrayList<Map.Entry<K, V>>();
		entries.addAll(srcMap.entrySet());
		int size = entries.size();
		for (int i = 0; i < size - 1; i++) {
			Map.Entry<K, V> entry = entries.get(i);
			int k = i;
			for (int j = i + 1; j < size; j++) {
				Map.Entry<K, V> entryJ = entries.get(j);
				if (desc ? entryJ.getValue().compareTo(entry.getValue()) > 0 : entryJ.getValue().compareTo(entry.getValue()) < 0) {
					k = j;
					entry = entryJ;
				}
			}
			if (k != i) {
				Map.Entry<K, V> entryI = entries.get(i);
				entries.set(i, entry);
				entries.set(k, entryI);
			}
		}
		// System.out.println(entries);
		Map<K, V> sortedMap = new LinkedHashMap<K, V>();
		for (int i = 0; i < size; i++) {
			Map.Entry<K, V> entry = entries.get(i);
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	/**
	 * 默认的字典顺序比较器
	 */
	public static final Comparator<String> DictionaryComparator = new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
	};

	@SuppressWarnings("rawtypes")
	public static <K, V extends Comparable> Map<K, V> sortMapByValues(Map<K, V> srcMap) {
		return sortMapByValues(srcMap, false);
	}

	public static <K, V> Map<K, V> sortMapByKey(Map<K, V> srcMap, Comparator<K> keyComparator) {
		List<K> keys = new ArrayList<K>();
		keys.addAll(srcMap.keySet());
		Collections.sort(keys, keyComparator);
		int size = keys.size();
		// System.out.println(keys);
		Map<K, V> sortedMap = new LinkedHashMap<K, V>();
		for (int i = 0; i < size; i++) {
			K key = keys.get(i);
			sortedMap.put(key, srcMap.get(key));
		}
		return sortedMap;
	}

	// ===============================================================================
	public static Field getClassField(Class<?> clazz, String fieldName) {
		Field retField = null;
		while (clazz != null && !clazz.isPrimitive() && !clazz.isInterface() && !clazz.isArray() && clazz != Void.class) {
			try {
				retField = clazz.getDeclaredField(fieldName);
				if (retField != null) {
					break;
				}
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			} catch (SecurityException e) {
				break;
			}
		}
		return retField;
	}

	public static List<Field> getClassFields(Class<?> clazz) {
		List<Field> fieldList = TypeUtil.newList(Field.class);
		//
		while (clazz != null && !clazz.isPrimitive() && !clazz.isInterface() && !clazz.isArray() && clazz != Void.class) {
			try {
				Field[] fields = clazz.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					// TODO 查重
					fieldList.add(field);
				}
				clazz = clazz.getSuperclass();
			} catch (SecurityException e) {
				break;
			}
		}
		return fieldList;
	}

	public static Object getObjectFieldValue(Object object, String fieldName) {
		try {
			Class<?> objClass = object.getClass();
			// 优先使用属性
			try {
				String getterName = StrUtil.toGetterMethodName(fieldName);
				Method getter = objClass.getMethod(getterName);
				getter.setAccessible(true);
				return getter.invoke(object);
			} catch (Exception ex) {
				//
			}
			// 其次使用字段
			Field field = objClass.getField(fieldName);
			field.setAccessible(true);
			return field.get(object);
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}
	}

	public static void setObjectFieldValue(Object object, String fieldName, Object fieldValue) {
		try {
			Class<?> objClass = object.getClass();
			// 优先使用属性
			try {
				String setterName = StrUtil.toSetterMethodName(fieldName);
				Method setter = objClass.getMethod(setterName, fieldValue.getClass());
				setter.setAccessible(true);
				setter.invoke(object, fieldValue);
				return;
			} catch (Exception ex) {
				//
			}
			// 其次使用字段
			Field field = objClass.getField(fieldName);
			field.setAccessible(true);
			field.set(object, fieldValue);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public static void copyProperties(Object source, Object target) {
		BeanUtils.copyProperties(source, target);
	}

	public static void copyPropertiesExcept(Object source, Object target, String... excludeProperties) {
		BeanUtils.copyProperties(source, target, excludeProperties);
	}

	// primitive Java types (boolean, byte, char, short, int, long, float, and double),
	public static Class<?> getWrapperType(Class<?> type) {
		if (type.isPrimitive()) {
			// boolean, byte, char, short, int, long, float, and double
			if (type == boolean.class) {
				return Boolean.class;
			} else if (type == byte.class) {
				return Byte.class;
			} else if (type == char.class) {
				return Character.class;
			} else if (type == short.class) {
				return Short.class;
			} else if (type == int.class) {
				return Integer.class;
			} else if (type == long.class) {
				return Long.class;
			} else if (type == float.class) {
				return Float.class;
			} else if (type == double.class) {
				return Double.class;
			} else if (type == void.class) {
				return Void.class;
			}
		}
		return type;
	}

	private static final List<Class<?>> simpleTypes;

	private static final Map<String, Class<?>> aliasToTypeMap;

	static {
		simpleTypes = new ArrayList<Class<?>>();
		aliasToTypeMap = new HashMap<String, Class<?>>();
		//
		simpleTypes.add(String.class);
		aliasToTypeMap.put("str", String.class);
		aliasToTypeMap.put("string", String.class);
		aliasToTypeMap.put("String", String.class);

		simpleTypes.add(Boolean.class);
		aliasToTypeMap.put("bool", Boolean.class);
		aliasToTypeMap.put("boolean", Boolean.class);
		aliasToTypeMap.put("Boolean", Boolean.class);

		simpleTypes.add(Date.class);
		aliasToTypeMap.put("date", Date.class);
		aliasToTypeMap.put("Date", Date.class);

		simpleTypes.add(Byte.class);
		aliasToTypeMap.put("byte", Byte.class);
		aliasToTypeMap.put("Byte", Byte.class);

		simpleTypes.add(Integer.class);
		aliasToTypeMap.put("int", Integer.class);
		aliasToTypeMap.put("integer", Integer.class);
		aliasToTypeMap.put("Integer", Integer.class);

		simpleTypes.add(Long.class);
		aliasToTypeMap.put("long", Long.class);
		aliasToTypeMap.put("Long", Long.class);

		simpleTypes.add(Short.class);
		aliasToTypeMap.put("short", Short.class);
		aliasToTypeMap.put("Short", Short.class);

		simpleTypes.add(Float.class);
		aliasToTypeMap.put("float", Float.class);
		aliasToTypeMap.put("Float", Float.class);

		simpleTypes.add(Double.class);
		aliasToTypeMap.put("double", Double.class);
		aliasToTypeMap.put("Double", Double.class);

		simpleTypes.add(Character.class);
		aliasToTypeMap.put("char", Character.class);
		aliasToTypeMap.put("Character", Character.class);

		simpleTypes.add(BigInteger.class);
		aliasToTypeMap.put("bigInt", BigInteger.class);
		aliasToTypeMap.put("bigInteger", BigInteger.class);
		aliasToTypeMap.put("BigInteger", BigInteger.class);

		simpleTypes.add(BigDecimal.class);
		aliasToTypeMap.put("bigDec", BigDecimal.class);
		aliasToTypeMap.put("bigDecimal", BigDecimal.class);
		aliasToTypeMap.put("BigDecimal", BigDecimal.class);
		/*
		 * for (String typeName : aliasToTypeMap.keySet()) { Class<?> clazz = aliasToTypeMap.get(typeName); System.out.println(typeName + " => " + clazz.getCanonicalName()); }
		 */
	}

	public static boolean isSimpleType(Class<?> type) {
		return type.isPrimitive() || simpleTypes.contains(type);
	}

	public static Class<?> getTypeForAlias(String typeAlias) {
		return aliasToTypeMap.get(typeAlias);
	}

	public static Object convertToType(String strVal, Class<?> type) {
		return convertToType(strVal, type, null);
	}

	public static Object convertToType(String strVal, Class<?> type, String typeFormat) {
		if (strVal == null) {
			return null;
		}
		if (type == null || type == String.class) {
			return strVal;
		}
		Class<?> typex = TypeUtil.getWrapperType(type);
		if (typex == Boolean.class) {
			return BoolUtil.parse(strVal);
		} else if (typex == Date.class) {
			if (StrUtil.hasText(typeFormat)) {
				try {
					return DateUtil.getDateFormat(typeFormat).parse(strVal);
				} catch (ParseException e) {
					e.printStackTrace();
					return null;
				}
			} else {
				int strLen = strVal.length();
				if (strLen <= 10) {
					try {
						return DateUtil.fromStdDateStr(strVal);
					} catch (ParseException e) {
						e.printStackTrace();
						return null;
					}
				} else {
					try {
						return DateUtil.fromStdDateTimeStr(strVal);
					} catch (ParseException e) {
						try {
							return DateUtil.fromStdShortDateTimeStr(strVal);
						} catch (ParseException e1) {
							e1.printStackTrace();
							return null;
						}
					}
				}
			}

		} else if (typex == Integer.class) {
			return Integer.valueOf(strVal);
		} else if (typex == Long.class) {
			return Long.valueOf(strVal);
		} else if (typex == Short.class) {
			return Short.valueOf(strVal);
		} else if (typex == Float.class) {
			return Float.valueOf(strVal);
		} else if (typex == Double.class) {
			return Double.valueOf(strVal);
		} else if (typex == Character.class) {
			return Character.valueOf(strVal.charAt(0));
		} else if (typex == BigInteger.class) {
			return new BigInteger(strVal);
		} else if (typex == BigDecimal.class) {
			return new BigDecimal(strVal);
		} else if (typex == Byte.class) {
			return Byte.valueOf(strVal);
		} else {
			return type.cast(strVal);
		}
	}

	// --------------------------------------------------------------------------------------------------
	/**
	 * 通过反射,获得指定类的父类的泛型参数的实际类型. 如BuyerServiceBean extends DaoSupport<Buyer>
	 * 
	 * @param clazz
	 *            clazz �?��反射的类,该类必须继承范型父类
	 * @param index
	 *            泛型参数�?��索引,�?�?��.
	 * @return 范型参数的实际类�? 如果没有实现ParameterizedType接口，即不支持泛型，�?��直接返回 <code>Object.class</code>
	 */
	public static Class<?> getSuperClassGenericType(Class<?> clazz, int index) {
		Type genType = clazz.getGenericSuperclass();// 得到泛型父类 DaoSupport<Buyer>
		// 如果没有实现ParameterizedType接口，即不支持泛型，直接返回Object.class
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		// 返回表示此类型实际类型参数的Type对象的数�?数组里放的都是对应类型的Class, 如BuyerServiceBean extends
		// DaoSupport<Buyer,Contact>就返回Buyer和Contact类型
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();// 取得真实的类型参数Buyer
		if (index >= params.length || index < 0) {
			throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class<?>) params[index];
	}

	/**
	 * 通过反射,获得指定类的父类的第�?��泛型参数的实际类�? 如BuyerServiceBean extends DaoSupport<Buyer>
	 * 
	 * @param clazz
	 *            clazz �?��反射的类,该类必须继承泛型父类
	 * @return 泛型参数的实际类�? 如果没有实现ParameterizedType接口，即不支持泛型，�?��直接返回 <code>Object.class</code>
	 */
	public static Class<?> getSuperClassGenericType(Class<?> clazz) {
		// logger.debug("当前类："+clazz);
		return getSuperClassGenericType(clazz, 0);
	}

	/**
	 * 通过反射,获得方法返回值泛型参数的实际类型. �? public Map<String, Buyer> getNames(){}
	 * 
	 * @param Method
	 *            method 方法
	 * @param int
	 *            index 泛型参数�?��索引,�?�?��.
	 * @return 泛型参数的实际类�? 如果没有实现ParameterizedType接口，即不支持泛型，�?��直接返回 <code>Object.class</code>
	 */
	public static Class<?> getMethodGenericReturnType(Method method, int index) {
		Type returnType = method.getGenericReturnType();
		if (returnType instanceof ParameterizedType) {
			ParameterizedType type = (ParameterizedType) returnType;
			Type[] typeArguments = type.getActualTypeArguments();
			if (index >= typeArguments.length || index < 0) {
				throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
			}
			return (Class<?>) typeArguments[index];
		}
		return Object.class;
	}

	/**
	 * 通过反射,获得方法返回值第�?��泛型参数的实际类�? �? public Map<String, Buyer> getNames(){}
	 * 
	 * @param Method
	 *            method 方法
	 * @return 泛型参数的实际类�? 如果没有实现ParameterizedType接口，即不支持泛型，�?��直接返回 <code>Object.class</code>
	 */
	public static Class<?> getMethodGenericReturnType(Method method) {
		return getMethodGenericReturnType(method, 0);
	}

	/**
	 * 通过反射,获得方法输入参数第index个输入参数的�?��泛型参数的实际类�? �? public void add(Map<String, Buyer> maps, List<String> names){}
	 * 
	 * @param Method
	 *            method 方法
	 * @param int
	 *            index 第几个输入参�?
	 * @return 输入参数的泛型参数的实际类型集合, 如果没有实现ParameterizedType接口，即不支持泛型，�?��直接返回空集�?
	 */
	@SuppressWarnings("rawtypes")
	public static List<Class> getMethodGenericParameterTypes(Method method, int index) {
		List<Class> results = new ArrayList<Class>();
		Type[] genericParameterTypes = method.getGenericParameterTypes();
		if (index >= genericParameterTypes.length || index < 0) {
			throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
		}
		Type genericParameterType = genericParameterTypes[index];
		if (genericParameterType instanceof ParameterizedType) {
			ParameterizedType aType = (ParameterizedType) genericParameterType;
			Type[] parameterArgTypes = aType.getActualTypeArguments();
			for (Type parameterArgType : parameterArgTypes) {
				Class parameterArgClass = (Class) parameterArgType;
				results.add(parameterArgClass);
			}
			return results;
		}
		return results;
	}

	/**
	 * 通过反射,获得方法输入参数第一个输入参数的�?��泛型参数的实际类�? �? public void add(Map<String, Buyer> maps, List<String> names){}
	 * 
	 * @param Method
	 *            method 方法
	 * @return 输入参数的泛型参数的实际类型集合, 如果没有实现ParameterizedType接口，即不支持泛型，�?��直接返回空集�?
	 */
	@SuppressWarnings("rawtypes")
	public static List<Class> getMethodGenericParameterTypes(Method method) {
		return getMethodGenericParameterTypes(method, 0);
	}

	/**
	 * 通过反射,获得Field泛型参数的实际类�? �? public Map<String, Buyer> names;
	 * 
	 * @param Field
	 *            field 字段
	 * @param int
	 *            index 泛型参数�?��索引,�?�?��.
	 * @return 泛型参数的实际类�? 如果没有实现ParameterizedType接口，即不支持泛型，�?��直接返回 <code>Object.class</code>
	 */
	public static Class<?> getFieldGenericType(Field field, int index) {
		Type genericFieldType = field.getGenericType();

		if (genericFieldType instanceof ParameterizedType) {
			ParameterizedType aType = (ParameterizedType) genericFieldType;
			Type[] fieldArgTypes = aType.getActualTypeArguments();
			if (index >= fieldArgTypes.length || index < 0) {
				throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
			}
			return (Class<?>) fieldArgTypes[index];
		}
		return Object.class;
	}

	/**
	 * 通过反射,获得Field泛型参数的实际类�? �? public Map<String, Buyer> names;
	 * 
	 * @param Field
	 *            field 字段
	 * @param int
	 *            index 泛型参数�?��索引,�?�?��.
	 * @return 泛型参数的实际类�? 如果没有实现ParameterizedType接口，即不支持泛型，�?��直接返回 <code>Object.class</code>
	 */
	public static Class<?> getFieldGenericType(Field field) {
		return getFieldGenericType(field, 0);
	}

	public static Class<?> getInterfaceTypeWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
		Class<?>[] interfaces = clazz.getInterfaces();
		for (Class<?> intfc : interfaces) {
			if (intfc.isAnnotationPresent(annotationClass)) {
				return intfc;
			}
		}
		return null;
	}

	public static void invokeStaticMethod(Class<?> clazz, String methodName) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method staticMethod = clazz.getDeclaredMethod(methodName);
		staticMethod.setAccessible(true);
		staticMethod.invoke(null);
	}
}
