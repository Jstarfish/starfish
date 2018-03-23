package priv.starfish.common.http;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

public class HttpBodyMessage implements HttpInputMessage {
	HttpServletRequest httpRequest;

	public HttpBodyMessage(HttpServletRequest request) {
		this.httpRequest = request;
	}

	@Override
	public HttpHeaders getHeaders() {
		return null;
	}

	@Override
	public InputStream getBody() throws IOException {
		return httpRequest.getInputStream();
	}

}
