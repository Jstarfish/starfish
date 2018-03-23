package priv.starfish.common.json;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import priv.starfish.common.base.DateFormats;

/**
 * "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd"
 * 
 * @author koqiui
 * @date 2015年9月21日 下午11:45:54
 *
 */
public class JsonDateTimeDeserializer extends JsonDeserializer<Date> {
	private static SimpleDateFormat STD_DATE_TIME_FORMAT = new SimpleDateFormat(DateFormats.STD_DATE_TIME);
	private static SimpleDateFormat STD_SHORT_DATE_TIME_FORMAT = new SimpleDateFormat(DateFormats.STD_SHORT_DATE_TIME);
	private static SimpleDateFormat STD_DATE_FORMAT = new SimpleDateFormat(DateFormats.STD_DATE);

	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

		Date date = null;
		try {
			date = STD_DATE_TIME_FORMAT.parse(jp.getText());
		} catch (ParseException e) {
			try {
				date = STD_SHORT_DATE_TIME_FORMAT.parse(jp.getText());
			} catch (ParseException e2) {
				try {
					date = STD_DATE_FORMAT.parse(jp.getText());
				} catch (ParseException e3) {
					e3.printStackTrace();
				}
			}
		}
		return date;
	}
}
