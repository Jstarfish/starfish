package priv.starfish.common.http;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

public class ByteArrayResponseHandler implements ResponseHandler<byte[]>, HttpErrorStatusHandler {
	protected final Log logger = LogFactory.getLog(this.getClass());
	//
	protected boolean handleHeaders = false;

	public ByteArrayResponseHandler(boolean handleHeaders) {
		this.handleHeaders = handleHeaders;
	}

	public ByteArrayResponseHandler() {
		this(false);
	}

	@Override
	public byte[] handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		if (this.handleHeaders) {
			Header[] headers = response.getAllHeaders();
			if (headers != null) {
				this.onHeaders(headers);
			}
		}
		//
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode >= 300) {
			this.onErrorStatus(statusCode, statusLine.getReasonPhrase());
			return null;
		} else {
			return EntityUtils.toByteArray(response.getEntity());
		}
	}

	@Override
	public void onErrorStatus(int statusCode, String reasonPhrase) {
		logger.error("statusCode : " + statusCode + " , reasonPhrase : " + reasonPhrase);
	}

	public void onHeaders(Header[] headers) {
		for (int i = 0; i < headers.length; i++) {
			Header header = headers[i];
			logger.debug(header.getName() + ": " + header.getValue());
		}
	}
}
