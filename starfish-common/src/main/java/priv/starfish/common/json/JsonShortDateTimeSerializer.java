package priv.starfish.common.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import priv.starfish.common.base.DateFormats;

/**
 * "yyyy-MM-dd HH:mm"
 * 
 * @author koqiui
 * @date 2015年9月21日 下午11:45:12
 *
 */
public class JsonShortDateTimeSerializer extends JsonSerializer<Date> {
	private static SimpleDateFormat STD_SHORT_DATE_TIME_FORMAT = new SimpleDateFormat(DateFormats.STD_SHORT_DATE_TIME);

	@Override
	public void serialize(Date value, JsonGenerator jgen, SerializerProvider sp) throws IOException, JsonProcessingException {

		jgen.writeString(STD_SHORT_DATE_TIME_FORMAT.format(value));
	}
}