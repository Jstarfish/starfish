package priv.starfish.common.http;

import org.apache.http.message.BasicNameValuePair;

public class HttpNameValuePair extends BasicNameValuePair {
	private static final long serialVersionUID = 3084857821712288283L;

	private Object value = null;

	//
	public HttpNameValuePair(String name, Object value) {
		super(name, value == null ? null : value.toString());
		this.value = value;
	}

	private HttpNameValuePair(String name, String value) {
		super(name, value);
		this.value = value;
	}

	public static HttpNameValuePair newOne(String name, Object value) {
		return new HttpNameValuePair(name, value);
	}

	public Object getValueObject() {
		return this.value;
	}
}
