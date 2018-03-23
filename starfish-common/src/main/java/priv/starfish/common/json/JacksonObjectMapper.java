package priv.starfish.common.json;

import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JacksonObjectMapper extends ObjectMapper {
	private static final long serialVersionUID = 1L;

	public JacksonObjectMapper() {

		SimpleModule module = new SimpleModule("miscDateModule");
		// yyyy-MM-dd HH:mm:ss
		module.addSerializer(Date.class, new JsonDateTimeSerializer());
		module.addDeserializer(Date.class, new JsonDateTimeDeserializer());
		// yyyy-MM-dd HH:mm
		module.addSerializer(Date.class, new JsonShortDateTimeSerializer());
		module.addDeserializer(Date.class, new JsonShortDateTimeDeserializer());
		// yyyy-MM-dd
		module.addSerializer(Date.class, new JsonDateSerializer());
		module.addDeserializer(Date.class, new JsonDateDeserializer());

		this.registerModule(module);
		//
		this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		this.configure(JsonParser.Feature.ALLOW_COMMENTS, true);

		this.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
}