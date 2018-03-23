package priv.starfish.common.webols;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.json.JsonDateTimeSerializer;
import priv.starfish.common.util.DateUtil;

import java.io.Serializable;
import java.util.Date;

public class OlsMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	//
	public String source;
	public String customerId;
	public String servantId;
	public String content;
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	public Date ts = new Date();

	//
	@Override
	public String toString() {
		return "OlsMessage [source=" + source + ", customerId=" + customerId + ", servantId=" + servantId + ", content=" + content + ", ts=" + DateUtil.toStdDateTimeStr(ts) + "]";
	}
}
