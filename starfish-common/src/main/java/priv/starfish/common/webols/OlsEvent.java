package priv.starfish.common.webols;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 代表事件数据信息
 * 
 * @author koqiui
 * @date 2015年6月21日 下午5:37:43
 *
 */
public class OlsEvent implements Serializable {
	private static final long serialVersionUID = -1L;
	//
	private String source;
	private String type;
	private Map<String, Object> attrs;
	private long time;

	public OlsEvent(String source, String type, Map<String, Object> attrs) {
		this.source = source;
		this.type = type;
		this.attrs = new HashMap<String, Object>();
		if (attrs != null) {
			this.attrs.putAll(attrs);
		}
		this.time = System.currentTimeMillis() / 1000;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Object> attrs) {
		this.attrs = attrs;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setAttr(String name, Object value) {
		this.attrs.put(name, value);
	}

	public Object getAttr(String name) {
		return this.attrs.get(name);
	}

	public Object getAttr(String name, Object defValue) {
		Object value = this.getAttr(name);
		return value == null ? defValue : value;
	}

	@Override
	public String toString() {
		return "RtEvent [source = " + source + ", type=" + type + ", attrs=" + attrs + ", time=" + time + "]";
	}

	//
	public static OlsEvent newEvent(String source, String type, Map<String, Object> attrs) {
		return new OlsEvent(source, type, attrs);
	}
}
