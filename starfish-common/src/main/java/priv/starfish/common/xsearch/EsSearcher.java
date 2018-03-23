package priv.starfish.common.xsearch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.flush.FlushResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.ImmutableSettings.Builder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.get.GetField;
import priv.starfish.common.annotation.IdField;
import priv.starfish.common.helper.FileHelper;
import priv.starfish.common.util.*;
import priv.starfish.common.web.WebEnvHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * base index er + search er => xsearch
 * 
 * @author koqiui
 * @date 2015年9月19日 上午11:37:53
 *
 */

public class EsSearcher {
	protected final static Log logger = LogFactory.getLog(EsSearcher.class);
	//
	public static final String indexTimeLongFieldName = "indexTimeLong";
	protected static final String configPrefix = "elastic.";
	//
	protected static final String defaultClusterName = "elasticsearch";
	protected static final String defaultIndexDbName = "ezmall";
	protected static final String defaultTransHost = "localhost";
	protected static final int defaultTransPort = 9300;
	// 是否启用把对象id作为索引文档id
	protected static boolean objectIdAsDocIdEnabled = false;

	// 操作客户端
	protected static TransportClient client;
	// 应用对应的索引名称
	private static String indexDbName = defaultIndexDbName;

	private EsSearcher() {
		//
	}

	protected static String getConfigValue(String key) {
		return WebEnvHelper.getConfig(configPrefix + key);
	}

	// 自动获取其对象的id字段的类型=>id字段名称映射(对Map<String,Object> 和 MapContext进行特殊处理，id字段固定为id)
	protected static final Class<?> StringObjectMap = TypeUtil.Types.StringObjectMap.getClass();
	protected static Map<Class<?>, Field> autoExtractedClassIdFieldMap = new ConcurrentHashMap<Class<?>, Field>();

	public static boolean setAutoExtractedClassIdField(Class<?> clazz, String idFieldName) {
		if (clazz == null) {
			throw new IllegalArgumentException("指定的类不可为空");
		}
		if (idFieldName == null) {
			idFieldName = "id";
		}
		//
		Field idField = TypeUtil.getClassField(clazz, idFieldName);
		if (idField != null) {
			//
			autoExtractedClassIdFieldMap.put(clazz, idField);
			//
			logger.debug(">> " + clazz.getName() + " # " + idFieldName);
			//
			return true;
		} else {
			logger.warn("访问字段 " + clazz.getName() + "#" + idFieldName + "时发生异常：指定的字段不存在或存在安全问题");
			//
			return false;
		}
	}

	public static boolean setAutoExtractedClassIdField(String className, String idFieldName) {
		try {
			Class<?> clazz = Class.forName(className);
			return setAutoExtractedClassIdField(clazz, idFieldName);
		} catch (ClassNotFoundException e) {
			logger.warn("未找到指定的类：" + className);
			//
			return false;
		}
	}

	protected static boolean isIdAutoExtractedClass(Class<?> clazz) {
		boolean result = autoExtractedClassIdFieldMap.containsKey(clazz) || MapContext.class.isAssignableFrom(clazz) || StringObjectMap.isAssignableFrom(clazz);
		if (result) {
			return true;
		}
		if (clazz.isAnnotationPresent(IdField.class)) {
			IdField idField = clazz.getAnnotation(IdField.class);
			String idFieldName = idField.name();
			result = setAutoExtractedClassIdField(clazz, idFieldName);
		}
		return result;
	}

	protected static String objectIdAsString(Object object) {
		if (object == null) {
			return null;
		}
		Class<?> clazz = object.getClass();
		if (!objectIdAsDocIdEnabled || !isIdAutoExtractedClass(clazz)) {
			return null;
		}
		if (MapContext.class.isAssignableFrom(clazz) || StringObjectMap.isAssignableFrom(clazz)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> tmpObject = (Map<String, Object>) object;
			Object id = tmpObject.get("id");
			return id == null ? null : id.toString();
		} else {
			Field idField = autoExtractedClassIdFieldMap.get(clazz);
			idField.setAccessible(true);
			try {
				Object id = idField.get(object);
				return id == null ? null : id.toString();
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.warn(e);
			}
		}
		return null;
	}

	/**
	 * 解析形如 127.0.0.1#9300,127.0.0.1#9310 的ip:port字符串
	 * 
	 * @author koqiui
	 * @date 2015年9月19日 下午2:32:17
	 * 
	 * @param transportAddressesStr
	 * @return
	 */
	private static List<InetSocketTransportAddress> parseTransportAddresses(String transportAddressesStr) {
		List<InetSocketTransportAddress> retAddresses = new ArrayList<InetSocketTransportAddress>();
		if (transportAddressesStr != null) {
			// 格式：127.0.0.1#9300,127.0.0.1#9310
			String[] transportAddressses = transportAddressesStr.split(",");
			for (String transportAddress : transportAddressses) {
				String[] hostAndPort = transportAddress.trim().split("#", -1);
				String host = hostAndPort[0].trim();
				if (StrUtil.isNullOrEmpty(host)) {
					host = defaultTransHost;
				}
				String portStr = hostAndPort.length > 1 ? hostAndPort[1].trim() : null;
				int port = defaultTransPort;
				if (StrUtil.hasText(portStr)) {
					port = Integer.valueOf(portStr);
				}
				retAddresses.add(new InetSocketTransportAddress(host, port));
			}
		}
		return retAddresses;
	}

	/**
	 * 解析形如 priv.starfish.common.xsearch.IX#code,priv.starfish.common.xsearch.CX#id 的类型=>id字段名称映射字符串
	 * 
	 * @author koqiui
	 * @date 2015年9月23日 上午12:39:19
	 * 
	 * @param classNameIdFieldMapStr
	 * @return
	 */
	private static Map<String, String> parseClassNameIdFieldMap(String classNameIdFieldMapStr) {
		Map<String, String> retMap = new HashMap<String, String>();
		if (classNameIdFieldMapStr != null) {
			// 格式：priv.starfish.common.xsearch.IX#code,priv.starfish.common.xsearch.CX#id
			String[] classNameIdFieldStrs = classNameIdFieldMapStr.split(",");
			for (String classNameIdFieldStr : classNameIdFieldStrs) {
				String[] pair = classNameIdFieldStr.trim().split("#", -1);
				String className = pair[0].trim();
				if (StrUtil.isNullOrEmpty(className)) {
					continue;
				}
				String idField = pair.length > 1 ? pair[1].trim() : null;
				if (StrUtil.isNullOrEmpty(idField)) {
					idField = "id";
				}
				retMap.put(className, idField);
			}
		}
		return retMap;
	}

	// 预建的单例
	protected static Lock xLock = new ReentrantLock();

	public static void init() {
		if (client == null) {
			try {
				xLock.lock();
				if (client == null) {
					// 静态初始化
					Builder builder = ImmutableSettings.settingsBuilder();
					//
					String clusterName = getConfigValue("cluster.name");
					if (StrUtil.isNullOrBlank(clusterName)) {
						clusterName = defaultClusterName;
					}
					builder.put("cluster.name", clusterName);
					//
					String toSniff = getConfigValue("transport.sniff");
					builder.put("client.transport.sniff", BoolUtil.isTrue(toSniff));
					//
					Settings settings = builder.build();
					//
					client = new TransportClient(settings);
					String transportAddressesStr = getConfigValue("transport.addresses");
					List<InetSocketTransportAddress> addresses = parseTransportAddresses(transportAddressesStr);
					for (InetSocketTransportAddress address : addresses) {
						client.addTransportAddress(address);
					}
					client.connectedNodes();
					//
					indexDbName = getConfigValue("indexdb.name");
					if (StrUtil.isNullOrBlank(indexDbName)) {
						indexDbName = defaultIndexDbName;
					} else {
						indexDbName = indexDbName.trim();
					}
					//
					String objectIdAsDocId = getConfigValue("object.id.as.doc.id");
					objectIdAsDocIdEnabled = BoolUtil.isTrue(objectIdAsDocId);
					if (objectIdAsDocIdEnabled) {
						String classNameIdFieldMapStr = getConfigValue("classname.id.field.map");
						Map<String, String> classNameIdFieldMap = parseClassNameIdFieldMap(classNameIdFieldMapStr);
						for (Map.Entry<String, String> classNameIdField : classNameIdFieldMap.entrySet()) {
							setAutoExtractedClassIdField(classNameIdField.getKey(), classNameIdField.getValue());
						}
					}
				}
				// 自动创建索引库
				createIndexDbIfNotExist();
			} finally {
				xLock.unlock();
			}
		}

	}

	public static void destroy() {
		if (client != null) {
			try {
				xLock.lock();
				if (client != null) {
					client.close();
				}
			} finally {
				xLock.unlock();
			}
		}
	}

	public static Client getClient() {
		return client;
	}

	public static String getIndexDbName() {
		return indexDbName;
	}

	/**
	 * 返回对象最后的主动同步/索引时间（用于与数据源的数据进行比较）
	 * 
	 * @author koqiui
	 * @date 2015年11月5日 下午2:14:10
	 * 
	 * @param docType
	 * @param docId
	 * @return null : 指定的文档不存在, -1L：文档存在，但值不存在， >0 ：正常
	 */
	public static Long getIndexTimeLong(String docType, Object docId) {
		GetRequest request = newGetRequest(docType);
		request.fields(indexTimeLongFieldName);
		GetResponse response = EsSearcher.doGet(request, docId);
		if (response.isExists()) {
			GetField field = response.getField(indexTimeLongFieldName);
			if (field != null) {
				return (Long) field.getValue();
			} else {
				return -1L;
			}
		}
		return null;
	}

	/**
	 * 生成文档索引请求（保存）builder [C reate or replace]
	 * 
	 * @author koqiui
	 * @date 2015年9月19日 下午2:28:03
	 * 
	 * @param docType
	 * @param docId
	 * @return
	 */
	public static IndexRequestBuilder newIndexRequestBuilder(String docType, Object docId) {
		IndexRequestBuilder requestBuilder = client.prepareIndex();
		requestBuilder.setIndex(indexDbName);
		if (docType != null) {
			requestBuilder.setType(docType);
		}
		if (docId != null) {
			requestBuilder.setId(String.valueOf(docId));
		}
		//
		return requestBuilder;
	}

	public static IndexRequestBuilder newIndexRequestBuilder(String docType) {
		return newIndexRequestBuilder(docType, null);
	}

	public static IndexRequestBuilder newIndexRequestBuilder() {
		return newIndexRequestBuilder(null, null);
	}

	// ---------------
	public static IndexRequest newIndexRequest(String docType, Object docId) {
		IndexRequest request = new IndexRequest(indexDbName);
		// request.index(indexDbName);
		if (docType != null) {
			request.type(docType);
		}
		if (docId != null) {
			request.id(String.valueOf(docId));
		}
		//
		return request;
	}

	public static IndexRequest newIndexRequest(String docType) {
		return newIndexRequest(docType, null);
	}

	public static IndexRequest newIndexRequest() {
		return newIndexRequest(null, null);
	}

	/**
	 * 合理地过滤提取数据的id作为文档的id
	 * 
	 * @author koqiui
	 * @date 2015年9月24日 上午12:03:33
	 * 
	 * @param request
	 * @param source
	 * @return
	 */
	public static IndexRequest fillRequestSource(IndexRequest request, Object source) {
		if (source != null) {
			String json = null;
			if (source.getClass() == String.class) {
				json = (String) source;
			} else {
				if (request.id() == null) {
					// 检查id是否设置
					String id = objectIdAsString(source);
					if (id != null) {
						request.id(id);
					}
				}
				json = JsonUtil.toJson(source, true);
			}
			System.out.println("IndexRequest:\n" + json);
			request.source(json);
		}
		return request;
	}

	public static IndexResponse doIndex(IndexRequest request, Object source) {
		fillRequestSource(request, source);
		try {
			return client.index(request).get();
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e);
			return null;
		}
	}

	public static IndexResponse doIndex(IndexRequest request) {
		return doIndex(request, null);
	}

	/**
	 * 生成文档获取请求builder [R ead]
	 * 
	 * @author koqiui
	 * @date 2015年9月19日 下午3:28:33
	 * 
	 * @param docType
	 * @param docId
	 * @return
	 */
	public static GetRequestBuilder newGetRequestBuilder(String docType, Object docId) {
		GetRequestBuilder requestBuilder = client.prepareGet();
		requestBuilder.setIndex(indexDbName);
		if (docType != null) {
			requestBuilder.setType(docType);
		}
		if (docId != null) {
			requestBuilder.setId(String.valueOf(docId));
		}
		//
		return requestBuilder;
	}

	public static GetRequestBuilder newGetRequestBuilder(String docType) {
		return newGetRequestBuilder(docType, null);
	}

	public static GetRequestBuilder newGetRequestBuilder() {
		return newGetRequestBuilder(null, null);
	}

	// ---------------
	public static GetRequest newGetRequest(String docType, Object docId) {
		GetRequest request = new GetRequest(indexDbName);
		if (docType != null) {
			request.type(docType);
		}
		if (docId != null) {
			request.id(String.valueOf(docId));
		}
		//
		return request;
	}

	public static GetRequest newGetRequest(String docType) {
		return newGetRequest(docType, null);
	}

	public GetRequest newGetRequest() {
		return newGetRequest(null, null);
	}

	public static GetResponse doGet(GetRequest request, Object docId) {
		if (docId != null) {
			request.id(String.valueOf(docId));
		}
		try {
			return client.get(request).get();
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e);
			return null;
		}
	}

	public static GetResponse doGet(GetRequest request) {
		return doGet(request, null);
	}

	// 一次获取多个-------------------------------------------------
	public static MultiGetRequestBuilder newMultiGetRequestBuilder(String docType, List<?> docIds) {
		MultiGetRequestBuilder requestBuilder = client.prepareMultiGet();
		if (docIds != null) {
			for (Object docId : docIds) {
				String id = String.valueOf(docId);
				requestBuilder.add(indexDbName, docType, id);
			}
		}
		//
		return requestBuilder;
	}

	// ---------------
	public static MultiGetRequest newMultiGetRequest(String docType, List<?> docIds) {
		MultiGetRequest request = new MultiGetRequest();
		if (docIds != null) {
			for (Object docId : docIds) {
				String id = String.valueOf(docId);
				request.add(indexDbName, docType, id);
			}
		}
		//
		return request;
	}

	public static MultiGetResponse doMultiGet(MultiGetRequest request) {
		try {
			return client.multiGet(request).get();
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * 生成文档更新请求builder [U pdate]
	 * 
	 * @author koqiui
	 * @date 2015年9月19日 下午4:55:02
	 * 
	 * @param docType
	 * @param docId
	 * @return
	 */
	public static UpdateRequestBuilder newUpdateRequestBuilder(String docType, Object docId) {
		UpdateRequestBuilder requestBuilder = client.prepareUpdate();
		requestBuilder.setIndex(indexDbName);
		if (docType != null) {
			requestBuilder.setType(docType);
		}
		if (docId != null) {
			requestBuilder.setId(String.valueOf(docId));
		}
		//
		return requestBuilder;
	}

	public static UpdateRequestBuilder newUpdateRequestBuilder(String docType) {
		return newUpdateRequestBuilder(docType, null);
	}

	public UpdateRequestBuilder newUpdateRequestBuilder() {
		return newUpdateRequestBuilder(null, null);
	}

	// ---------------
	public static UpdateRequest newUpdateRequest(String docType, Object docId) {
		UpdateRequest request = new UpdateRequest();
		request.index(indexDbName);
		if (docType != null) {
			request.type(docType);
		}
		if (docId != null) {
			request.id(String.valueOf(docId));
		}
		//
		return request;
	}

	public static UpdateRequest newUpdateRequest(String docType) {
		return newUpdateRequest(docType, null);
	}

	public static UpdateRequest newUpdateRequest() {
		return newUpdateRequest(null, null);
	}

	/**
	 * 合理地过滤提取数据的id作为文档的id
	 * 
	 * @author koqiui
	 * @date 2015年9月24日 上午12:02:17
	 * 
	 * @param request
	 * @param source
	 * @return
	 */
	public static UpdateRequest fillRequestSource(UpdateRequest request, Object source) {
		if (source != null) {
			String json = null;
			if (source.getClass() == String.class) {
				json = (String) source;
			} else {
				if (request.id() == null) {
					// 检查id是否设置
					String id = objectIdAsString(source);
					if (id != null) {
						request.id(id);
					}
				}
				json = JsonUtil.toJson(source, true);
			}
			System.out.println("IndexRequest:\n" + json);
			request.doc(json);
		}
		return request;
	}

	public static UpdateResponse doUpdate(UpdateRequest request, Object source) {
		fillRequestSource(request, source);
		try {
			return client.update(request).get();
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e);
			return null;
		}
	}

	public static UpdateResponse doUpdate(UpdateRequest request) {
		return doUpdate(request, null);
	}

	/**
	 * 生成文档删除请求builder [D elete]
	 * 
	 * @author koqiui
	 * @date 2015年9月19日 下午2:32:06
	 * 
	 * @param docType
	 * @param docId
	 * @return
	 */
	public static DeleteRequestBuilder newDeleteRequestBuilder(String docType, Object docId) {
		DeleteRequestBuilder requestBuilder = client.prepareDelete();
		requestBuilder.setIndex(indexDbName);
		if (docType != null) {
			requestBuilder.setType(docType);
		}
		if (docId != null) {
			requestBuilder.setId(String.valueOf(docId));
		}
		//
		return requestBuilder;
	}

	public static DeleteRequestBuilder newDeleteRequestBuilder(String docType) {
		return newDeleteRequestBuilder(docType, null);
	}

	public static DeleteRequestBuilder newDeleteRequestBuilder() {
		return newDeleteRequestBuilder(null, null);
	}

	// ---------------
	public static DeleteRequest newDeleteRequest(String docType, Object docId) {
		DeleteRequest request = new DeleteRequest(indexDbName);
		// request.index(indexDbName);
		if (docType != null) {
			request.type(docType);
		}
		if (docId != null) {
			request.id(String.valueOf(docId));
		}
		//
		return request;
	}

	public static DeleteRequest newDeleteRequest(String docType) {
		return newDeleteRequest(docType, null);
	}

	public static DeleteRequest newDeleteRequest() {
		return newDeleteRequest(null, null);
	}

	public static DeleteResponse doDelete(DeleteRequest request, Object docId) {
		if (docId != null) {
			request.id(String.valueOf(docId));
		}
		try {
			return client.delete(request).get();
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e);
			return null;
		}
	}

	public static DeleteResponse doDelete(DeleteRequest request) {
		return doDelete(request, null);
	}

	/**
	 * 生成批量请求builder [B ulk]
	 * 
	 * @author koqiui
	 * @date 2015年9月23日 下午11:41:03
	 * 
	 * @return
	 */
	public static BulkRequestBuilder newBulkRequestBuilder() {
		return client.prepareBulk();
	}

	// ---------------
	public static BulkRequest newBulkRequest() {
		return new BulkRequest();
	}

	@SuppressWarnings("rawtypes")
	public static BulkResponse doBulk(List<ActionRequest> requests, BulkRequest bulkRequest) {
		if (bulkRequest == null) {
			bulkRequest = newBulkRequest();
		}
		for (ActionRequest request : requests) {
			bulkRequest.add(request);
		}
		try {
			return client.bulk(bulkRequest).get();
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e);
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public static BulkResponse doBulk(List<ActionRequest> requests) {
		return doBulk(requests, null);
	}

	// ---------------------------------------- admin ------------------------------------------
	/**
	 * 检查索引库是否存在
	 * 
	 * @author koqiui
	 * @date 2016年2月3日 下午6:41:24
	 * 
	 * @return
	 */
	public static boolean existsIndexDb() {
		AdminClient admin = client.admin();
		//
		IndicesExistsResponse response = admin.indices().prepareExists(indexDbName).get();
		return response.isExists();
	}

	/**
	 * 创建索引库
	 * 
	 * @author koqiui
	 * @date 2016年2月3日 下午6:42:17
	 * 
	 * @return
	 */
	public static boolean createIndexDb() {
		AdminClient admin = client.admin();
		CreateIndexResponse response = admin.indices().prepareCreate(indexDbName).get();
		return response.isAcknowledged();
	}

	/**
	 * 检查索引库，如果不存在则创建
	 * 
	 * @author koqiui
	 * @date 2016年2月3日 下午6:42:33
	 *
	 */
	public static void createIndexDbIfNotExist() {
		AdminClient admin = client.admin();
		//
		IndicesExistsResponse responseX = admin.indices().prepareExists(indexDbName).get();
		if (!responseX.isExists()) {
			CreateIndexResponse response = admin.indices().prepareCreate(indexDbName).get();
			boolean createResult = response.isAcknowledged();
			if (createResult) {
				logger.debug("索引[" + indexDbName + "]创建已成功");
			} else {
				logger.warn("索引[" + indexDbName + "]已创建失败");
			}
		}
	}

	/** 把数据从内存中转移到索引存储（以释放一些内存） */
	public static boolean flushIndexDb() {
		AdminClient admin = client.admin();
		//
		FlushResponse response = admin.indices().prepareFlush(indexDbName).get();
		//
		int totalShards = response.getTotalShards();
		int successShards = response.getSuccessfulShards();
		int failureShards = response.getFailedShards();
		logger.info("[" + indexDbName + "]中共" + totalShards + "个分片，Flush 成功： " + successShards + "个，失败： " + failureShards + "个");
		return failureShards <= 0;
	}

	/** 检查是否存在某个类型 */
	public static boolean existsDocType(String docType) {
		AdminClient admin = client.admin();
		//
		TypesExistsResponse response = admin.indices().prepareTypesExists(indexDbName).setTypes(docType).get();
		return response.isExists();
	}

	/** 设置文档类型映射（根据提供的映射内容字符串） */
	public static boolean putDocTypeMapping(String docType, String source) {
		AdminClient admin = client.admin();
		//
		PutMappingRequest request = Requests.putMappingRequest(indexDbName).type(docType).source(source);
		try {
			PutMappingResponse response = admin.indices().putMapping(request).get();
			if (response.isAcknowledged()) {
				logger.info(indexDbName + "." + docType + " 的类型映射已创建/更新");
				return true;
			}
		} catch (InterruptedException | ExecutionException e) {
			logger.error(indexDbName + "." + docType + " 的类型映射创建/更新失败", e);
		}
		return false;
	}

	/** 设置文档类型映射（根据提供的映射内容文件所在的类路径） */
	public static boolean putDocTypeMappingFromClassPath(String docType, String sourcePath) {
		String mappingSource = FileHelper.readResourceAsAsString(sourcePath);
		if (mappingSource == null) {
			logger.error(indexDbName + "." + docType + " 的类型映射内容读取失败，类路径[" + sourcePath + "]");
			return false;
		}
		return putDocTypeMapping(docType, mappingSource);
	}

	/** 设置文档类型映射（根据ESDocType） */
	public static boolean putDocTypeMappingForClass(Class<?> docClass) {
		String docType = EsDoc.getDocTypeName(docClass);
		String docTypeMappingFilePath = EsDoc.getDocTypeMappingFilePath(docClass);
		return putDocTypeMappingFromClassPath(docType, docTypeMappingFilePath);
	}

	/** 设置文档类型映射（根据ESDocType），如果不存在的话 */
	public static boolean putDocTypeMappingForClassIfNotExist(Class<?> docClass) {
		String docType = EsDoc.getDocTypeName(docClass);
		if (!existsDocType(docType)) {
			String docTypeMappingFilePath = EsDoc.getDocTypeMappingFilePath(docClass);
			try {
				return putDocTypeMappingFromClassPath(docType, docTypeMappingFilePath);
			} catch (Exception ex) {
				logger.error(indexDbName + "." + docType + " 的类型映射设置失败，类路径[" + docTypeMappingFilePath + "]");
				return false;
			}
		}
		return false;
	}

	// ----------------------------------------------------------------------------------
	public static SearchRequestBuilder newSearchRequestBuilder() {
		return client.prepareSearch(indexDbName);
	}

}
