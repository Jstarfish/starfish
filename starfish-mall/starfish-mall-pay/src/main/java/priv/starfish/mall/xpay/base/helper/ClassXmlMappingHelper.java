package priv.starfish.mall.xpay.base.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import priv.starfish.common.dag.DirectedEdge;
import priv.starfish.common.dag.DirectedGraph;
import priv.starfish.common.helper.FileHelper;
import priv.starfish.common.util.*;
import priv.starfish.mall.xpay.base.annotation.XmlMapped;
import priv.starfish.mall.xpay.base.bean.ClassXmlInfo;
import priv.starfish.mall.xpay.base.bean.FieldXmlInfo;

public class ClassXmlMappingHelper {
	private static final Log logger = LogFactory.getLog(ClassXmlMappingHelper.class);
	//
	private static Map<String, Map<Class<?>, ClassXmlInfo>> namedMappings = new HashMap<String, Map<Class<?>, ClassXmlInfo>>();

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private static Map<String, Map<String, Object>> assemblyMappingJson(Map<String, Object> allMappings) {
		// 所有类名称
		List<String> allClassNames = new ArrayList<String>();
		allClassNames.addAll(allMappings.keySet());
		// 类依赖关系
		List<DirectedEdge<String>> classRefs = new ArrayList<DirectedEdge<String>>();
		//
		for (Map.Entry<String, Object> clazzMapping : allMappings.entrySet()) {
			String className = clazzMapping.getKey();
			try {
				Class<?> clazz = Class.forName(className);
				Map<String, Object> mapping = (Map<String, Object>) clazzMapping.getValue();
				if (mapping != null) {
					if (mapping.containsKey(ClassXmlInfo.EXTENDS_KEY)) {
						String superClassName = (String) mapping.get(ClassXmlInfo.EXTENDS_KEY);
						classRefs.add(DirectedEdge.newOne(className, superClassName));
						//
						mapping.remove(ClassXmlInfo.EXTENDS_KEY);
					}
				}
			} catch (ClassNotFoundException ex) {
				// 去除不存在的类信息
				allClassNames.remove(className);
				//
				logger.error(ex);
			}
		}
		DirectedGraph classRefGraph = DirectedGraph.newOne();
		classRefGraph.addEdges(classRefs);
		List<String> orderedClassNames = classRefGraph.getArrangedEdgeNodes();
		// 去除带依赖关系的类名
		allClassNames.removeAll(orderedClassNames);
		// 把无依赖关系的类名插入到前面
		orderedClassNames.addAll(0, allClassNames);
		//
		System.out.println(orderedClassNames);
		//
		Map<String, Map<String, Object>> retMappings = new LinkedHashMap<String, Map<String, Object>>();
		for (String className : orderedClassNames) {
			Map<String, Object> mapping = (Map<String, Object>) allMappings.get(className);
			if (mapping == null) {
				mapping = new HashMap<String, Object>();
			}
			List<String> superRefs = classRefGraph.getFanoutNodes(className);
			if (superRefs.isEmpty()) {
				retMappings.put(className, mapping);
			} else {
				String superClass = superRefs.get(0);
				Map<String, Object> superMapping = retMappings.get(superClass);
				Map<String, Object> newMapping = new LinkedHashMap<String, Object>();
				newMapping.putAll(superMapping);
				newMapping.putAll(mapping);
				//
				retMappings.put(className, newMapping);
			}
		}
		return retMappings;
	}

	public static void loadClassXmlMappings(String... mappingJsonFileNames) {
		for (String jsonFileName : mappingJsonFileNames) {
			if (StrUtil.isNullOrBlank(jsonFileName)) {
				continue;
			}
			int charIndex = jsonFileName.lastIndexOf('/');
			String mappingName = charIndex == -1 ? jsonFileName : jsonFileName.substring(charIndex + 1);
			charIndex = mappingName.lastIndexOf(".mapping.json");
			mappingName = charIndex == -1 ? mappingName : mappingName.substring(0, charIndex);
			//
			Map<Class<?>, ClassXmlInfo> namedMapping = new HashMap<Class<?>, ClassXmlInfo>();
			namedMappings.put(mappingName, namedMapping);
			//
			Map<String, String> classXmlNames = new HashMap<String, String>();
			//
			try {
				BufferedReader dataReader = FileHelper.getResourceAsBufferedReader(jsonFileName);
				String mappingStr = FileHelper.readBufferedReaderAsString(dataReader);
				Map<String, Object> _allMappings = JsonUtil.fromJson(mappingStr, TypeUtil.TypeRefs.StringObjectMapType);
				//
				Map<String, Map<String, Object>> allMappings = assemblyMappingJson(_allMappings);
				// 获取类的默认xmlName
				for (Map.Entry<String, Map<String, Object>> clazzMapping : allMappings.entrySet()) {
					String className = clazzMapping.getKey();
					Map<String, Object> mapping = clazzMapping.getValue();
					if (mapping.containsKey(ClassXmlInfo.ROOT_KEY)) {
						String xmlName = (String) mapping.get(ClassXmlInfo.ROOT_KEY);
						classXmlNames.put(className, xmlName);
						// System.out.println(className + "=>" + xmlName);
					}
				}
				//
				logger.debug(JsonUtil.toFormattedJson(allMappings));
				for (Map.Entry<String, Map<String, Object>> clazzMapping : allMappings.entrySet()) {
					String className = clazzMapping.getKey();
					try {
						Class<?> clazz = Class.forName(className);
						Map<String, Object> mapping = clazzMapping.getValue();
						//
						ClassXmlInfo classFieldXmlInfo = ClassXmlInfo.newOne();

						classFieldXmlInfo.clazz = clazz;

						classFieldXmlInfo.parseFromMapping(mapping, classXmlNames);
						//
						// System.out.println(clazz + "=>" + classFieldXmlInfo);
						namedMapping.put(clazz, classFieldXmlInfo);

					} catch (ClassNotFoundException ex) {
						// ...
						logger.error(ex);
					}

				}

			} catch (Exception ex) {
				logger.error(ex);
			}
			// logger.debug(mappingName + " => " + JsonUtil.toFormattedJson(namedMapping));
		}

	}

	public static Map<Class<?>, ClassXmlInfo> getClassXmlMappingsByName(String mappingName) {
		return namedMappings.get(mappingName);
	}

	public static String objectToXml(Object object, String mappingName) {
		return objectToXml(object, mappingName, false);
	}

	public static String objectToXml(Object object, String mappingName, boolean formatted) {
		return objectToXml(object, null, mappingName, formatted);
	}

	public static String objectToXml(Object object, String elemName, String mappingName, boolean formatted) {
		if (object == null) {
			return null;
		}
		Map<Class<?>, ClassXmlInfo> classXmlMappings = getClassXmlMappingsByName(mappingName);
		if (classXmlMappings == null) {
			logger.error("未找到与[" + mappingName + "] 对应的xml映射");
			return null;
		}
		Element element = objectToElement(object, elemName, classXmlMappings);
		if (element == null) {
			return null;
		}
		XMLOutputter out = new XMLOutputter();
		// 设置生成xml文档的格式
		if (formatted) {
			Format format = Format.getPrettyFormat();
			// 自定义xml文档的缩进(敲了四个空格，代表四个缩进)
			format.setIndent("    ");
			//
			out.setFormat(format);
		}
		return out.outputString(element);
	}

	private static Element objectToElement(Object object, String elemName, Map<Class<?>, ClassXmlInfo> classXmlMappings) {
		if (object == null || classXmlMappings == null) {
			return null;
		}

		Element retElem = null;
		Class<?> clazz = object.getClass();
		if (clazz.isAnnotationPresent(XmlMapped.class)) {
			ClassXmlInfo classFieldInfo = classXmlMappings.get(clazz);
			if (classFieldInfo != null) {
				if (StrUtil.isNullOrBlank(elemName)) {
					elemName = classFieldInfo.xmlName;
				}
				retElem = new Element(elemName);
				for (Map.Entry<String, FieldXmlInfo> infoItem : classFieldInfo.field2XmlNames.entrySet()) {
					String fieldName = infoItem.getKey();
					FieldXmlInfo fieldXmlInfo = infoItem.getValue();
					String xmlName = fieldXmlInfo.xmlName;
					Class<?> fieldType = fieldXmlInfo.fieldType;
					//
					Object fieldValue = TypeUtil.getObjectFieldValue(object, fieldName);
					if (fieldValue == null) {
						// 处理null值
						String nullAs = fieldXmlInfo.nullAs;
						if (nullAs != null) {
							Element elem = new Element(xmlName);
							elem.addContent(new Text(nullAs));
							retElem.addContent(elem);
						}
						//
						continue;
					}
					if (fieldType.isAnnotationPresent(XmlMapped.class)) {
						Element elem = objectToElement(fieldValue, xmlName, classXmlMappings);
						if (elem != null) {
							retElem.addContent(elem);
						}
					} else if (TypeUtil.isSimpleType(fieldType)) {
						Element elem = new Element(xmlName);
						String valueStr = fieldValue.toString();
						// 处理格式化
						Class<?> typex = TypeUtil.getWrapperType(fieldType);
						if (typex == Date.class) {
							String fieldFormat = fieldXmlInfo.fieldFormat;
							if (fieldFormat != null) {
								Date value = (Date) fieldValue;
								valueStr = DateUtil.getDateFormat(fieldFormat).format(value);
							}
						} else if (Number.class.isAssignableFrom(typex)) {
							String fieldFormat = fieldXmlInfo.fieldFormat;
							if (fieldFormat != null) {
								Number value = (Number) fieldValue;
								valueStr = NumUtil.getNumFormat(fieldFormat).format(value);
							}
						}
						//
						elem.addContent(new Text(valueStr));
						retElem.addContent(elem);
					}
				}
			}
		}
		return retElem;
	}

	@SuppressWarnings("unchecked")
	public static <T> T objectFromXml(String xmlStr, Class<T> clazz, String mappingName) {
		if (StrUtil.isNullOrBlank(xmlStr)) {
			return null;
		}
		Map<Class<?>, ClassXmlInfo> classXmlMappings = getClassXmlMappingsByName(mappingName);
		if (classXmlMappings == null) {
			logger.error("未找到与[" + mappingName + "] 对应的xml映射");
			return null;
		}

		StringReader xmlReader = new StringReader(xmlStr);
		InputSource xmlSource = new InputSource(xmlReader);
		SAXBuilder builder = new SAXBuilder(false);
		try {
			Document doc = builder.build(xmlSource);
			Element root = doc.getRootElement();
			return (T) objectFromElement(root, clazz, classXmlMappings);
		} catch (JDOMException | IOException e) {
			logger.error(e);
			return null;
		}
	}

	// <ap><Amt>53</Amt><Corp><PsFlag>X</PsFlag><BookingDate>2016-02-28</BookingDate><BookingTime>03:01:19</BookingTime><WhyUse>不知道什么用</WhyUse></Corp></ap>
	@SuppressWarnings("unchecked")
	private static Object objectFromElement(Element elem, Class<?> clazz, Map<Class<?>, ClassXmlInfo> classXmlMappings) {
		if (elem != null && clazz.isAnnotationPresent(XmlMapped.class)) {
			ClassXmlInfo classFieldInfo = classXmlMappings.get(clazz);
			if (classFieldInfo != null) {
				try {
					Object retObj = clazz.newInstance();
					//
					List<Element> childElems = elem.getChildren();
					int childCount = childElems.size();
					// System.out.println("childCount: " + childCount);
					if (childCount > 0) {
						for (int i = 0; i < childCount; i++) {
							Element childElem = childElems.get(i);
							if (childElem != null) {
								String xmlName = childElem.getName();
								FieldXmlInfo fieldXmlInfo = classFieldInfo.xmlName2Fields.get(xmlName);
								if (fieldXmlInfo != null) {
									Class<?> fieldType = fieldXmlInfo.fieldType;
									String fieldName = fieldXmlInfo.fieldName;
									if (fieldType.isAnnotationPresent(XmlMapped.class)) {
										Object fieldValue = objectFromElement(childElem, fieldType, classXmlMappings);
										TypeUtil.setObjectFieldValue(retObj, fieldName, fieldValue);
									} else if (TypeUtil.isSimpleType(fieldType)) {
										String fieldFormat = fieldXmlInfo.fieldFormat;
										String valueStr = childElem.getValue();
										Object fieldValue = TypeUtil.convertToType(valueStr, fieldType, fieldFormat);
										TypeUtil.setObjectFieldValue(retObj, fieldName, fieldValue);
									}
								}
							}
						}
					}
					//
					return retObj;
				} catch (InstantiationException | IllegalAccessException e) {
					logger.error(e);
				}
			}
		}
		return null;
	}

}
