package priv.starfish.common.xsearch;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.IdField;
import priv.starfish.common.json.JsonDateTimeDeserializer;
import priv.starfish.common.json.JsonDateTimeSerializer;
import priv.starfish.common.util.StrUtil;

@IdField(name = "id")
public abstract class EsDoc {
	// 默认的文档类型类名称后缀
	private static final String DEFUALT_DOC_TYPE_CLASS_NAME_SUFFIX = "Doc";
	// 默认的文档类型映射文件后缀名
	private static final String DEFUALT_DOC_TYPE_MAPPING_FILE_SUFFIX = ".mapping.json";

	/**
	 * 返回类型对应的搜索文档类型（可硬使用 EsDocType进行标注）， </br>
	 * 如果找不到EsDocType，则把类型的名称的开头字母小写并且去掉结尾的"Doc"后缀作为名称 </br>
	 * 比如对于没有用EsDocType标注的 ShopxDoc 得到的文档类型名称为 ShopxDoc => shopxDoc => shopx
	 * 
	 * @author koqiui
	 * @date 2016年2月3日 下午7:46:07
	 * 
	 * @param docClass
	 * @return
	 */
	public static String getDocTypeName(Class<?> docClass) {
		EsDocType esDocType = docClass.getAnnotation(EsDocType.class);
		String typeName = esDocType == null ? null : esDocType.name();
		if (StrUtil.isNullOrBlank(typeName)) {
			typeName = docClass.getSimpleName();
			typeName = StrUtil.toJavaVariableName(typeName);
			if (typeName.endsWith(DEFUALT_DOC_TYPE_CLASS_NAME_SUFFIX)) {
				typeName = typeName.substring(0, typeName.length() - DEFUALT_DOC_TYPE_CLASS_NAME_SUFFIX.length());
			}
		}
		return typeName;
	}

	/**
	 * 返回类型对应的搜索文档类型映射文件路径（可硬使用 EsDocType进行标注）， </br>
	 * 如果找不到EsDocType，则把类型的名称+".mapping.json"作为路径文件路径 </br>
	 * 比如对于没有用EsDocType标注的 ShopxDoc 得到的文档类型映射文件路径为 ShopxDoc => ShopxDoc.mapping.json
	 * 
	 * @author koqiui
	 * @date 2016年2月3日 下午7:50:53
	 * 
	 * @param docClass
	 * @return
	 */
	public static String getDocTypeMappingFilePath(Class<?> docClass) {
		EsDocType esDocType = docClass.getAnnotation(EsDocType.class);
		String mappingFilePath = esDocType == null ? null : esDocType.mappingFilePath();
		if (StrUtil.isNullOrBlank(mappingFilePath)) {
			mappingFilePath = "/" + docClass.getPackage().getName().replace('.', '/') + "/" + docClass.getSimpleName() + DEFUALT_DOC_TYPE_MAPPING_FILE_SUFFIX;
		} else if (mappingFilePath.startsWith("./")) {
			mappingFilePath = "/" + docClass.getPackage().getName().replace('.', '/') + "/" + mappingFilePath.substring(2);
		}
		return mappingFilePath;
	}

	// -----------------------------------------------------------------------------------
	// 创建/更新索引文档的时间（在索引的时候设置）
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonDateTimeDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	public Date indexTime;

	// 创建/更新索引文档的时间(Long在索引的时候设置 = indexTime.getTime())
	@JsonInclude(Include.NON_NULL)
	public Long indexTimeLong;

}
