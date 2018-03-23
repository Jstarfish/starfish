package priv.starfish.common.util;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import priv.starfish.common.json.JacksonObjectMapper;

/**
 * 
 * @author Hu Changwei
 * @date 2013-12-25
 * 
 */
public class JsonUtil {
	private static final Log logger = LogFactory.getLog(JsonUtil.class);

	private static ObjectMapper defaultJsonMapper = new JacksonObjectMapper();

	private static ObjectMapper noNullJsonMapper = new JacksonObjectMapper();

	private static ObjectMapper formatJsonMapper = new JacksonObjectMapper();

	static {
		noNullJsonMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		//
		formatJsonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	}

	public static <T> T fromJson(String json, Class<T> type) {
		try {
			return defaultJsonMapper.readValue(json, type);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	public static <T> T fromJson(String json, TypeReference<T> typeRef) {
		try {
			return defaultJsonMapper.readValue(json, typeRef);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	public static String toJson(Object src) {
		if (src == null) {
			return null;
		}
		try {
			return defaultJsonMapper.writeValueAsString(src);
		} catch (JsonProcessingException e) {
			logger.error(e);
			return null;
		}
	}

	public static String toFormattedJson(Object src) {
		if (src == null) {
			return null;
		}
		try {
			return formatJsonMapper.writeValueAsString(src);
		} catch (JsonProcessingException e) {
			logger.error(e);
			return null;
		}
	}

	public static String toJson(Object src, boolean noNullValues) {
		if (src == null) {
			return null;
		}
		ObjectMapper jsonMapper = noNullValues ? noNullJsonMapper : defaultJsonMapper;
		try {
			return jsonMapper.writeValueAsString(src);
		} catch (JsonProcessingException e) {
			logger.error(e);
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public static String formatAsMap(String jsonStr) {
		Map data = fromJson(jsonStr, Map.class);
		return toFormattedJson(data);
	}

	@SuppressWarnings("rawtypes")
	public static String formatAsList(String jsonStr) {
		List data = fromJson(jsonStr, List.class);
		return toFormattedJson(data);
	}
}
