package priv.starfish.common.http;

/**
 * HTTP 请求方法
 * 
 * @author koqiui
 * 
 */
public enum HttpMethod {
	GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE"), //
	HEAD("HEAD"), OPTIONS("OPTIONS"), TRACE("TRACE"), //
	CONNECT("CONNECT");

	private String value;

	HttpMethod(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
