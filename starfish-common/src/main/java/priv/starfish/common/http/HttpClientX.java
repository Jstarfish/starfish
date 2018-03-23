package priv.starfish.common.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.MediaType;

import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.StrUtil;

public class HttpClientX<TResult> {
	private final Log logger = LogFactory.getLog(this.getClass());
	//
	private static final int CONNECTION_TIMEOUT_MS = 60000;
	private static final int SOCKET_TIMEOUT_MS = 60000;
	//
	private static boolean printRequestHeaders = false;
	//
	private RequestConfig requestConfig;
	private String httpBaseUrl;
	private ResponseHandler<TResult> responseHandler;
	//
	private CloseableHttpClient httpClient;
	private HttpRequestInterceptor requestInterceptor;
	private boolean keepAlive = true;
	private ContentType contentType = null;
	private String accept = MediaType.ALL_VALUE;
	private Charset charset = ContentTypes.CHARSET_UTF8;
	private Map<String, String> requestHeaders = new LinkedHashMap<>();

	//
	private void doInit(String httpBaseUrl, ResponseHandler<TResult> responseHandler) {
		RequestConfig.Builder reqConfigBuilder = RequestConfig.custom();
		reqConfigBuilder.setConnectTimeout(CONNECTION_TIMEOUT_MS);
		reqConfigBuilder.setSocketTimeout(SOCKET_TIMEOUT_MS);
		requestConfig = reqConfigBuilder.build();
		//
		httpClient = HttpClients.createDefault();
		//
		this.httpBaseUrl = httpBaseUrl;
		this.responseHandler = responseHandler;
		//
		logger.debug(this.httpBaseUrl);
	}

	//
	public HttpClientX(String httpBaseUrl, ResponseHandler<TResult> responseHandler) {
		this.doInit(httpBaseUrl, responseHandler);
	}

	public HttpClientX(String scheme, String host, int port, String path, ResponseHandler<TResult> responseHandler) {
		try {
			if (StrUtil.isNullOrBlank(scheme)) {
				scheme = "http";
			}
			if (StrUtil.isNullOrBlank(host)) {
				host = "localhost";
			}
			if (port <= 0) {
				port = 80;
			}
			if (StrUtil.isNullOrBlank(path)) {
				path = "";
			}
			//
			URIBuilder uriBuiler = new URIBuilder();
			uriBuiler.setScheme(scheme);
			uriBuiler.setHost(host);
			uriBuiler.setPort(port);
			uriBuiler.setPath(path);
			URI uri = uriBuiler.build();
			//
			String httpBaseUrl = uri.toString();
			//
			this.doInit(httpBaseUrl, responseHandler);
		} catch (URISyntaxException e) {
			logger.error(e);
		}
	}

	public HttpClientX(String host, int port, String path, ResponseHandler<TResult> responseHandler) {
		this(null, host, port, path, responseHandler);
	}

	public HttpClientX(String host, String path, ResponseHandler<TResult> responseHandler) {
		this(null, host, -1, path, responseHandler);
	}

	public HttpClientX(ResponseHandler<TResult> responseHandler) {
		this("", responseHandler);
	}

	public HttpClientX() {
		this("", null);
	}

	public static String toJsonString(List<? extends NameValuePair> params, boolean noNullValues) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				NameValuePair param = params.get(i);
				if (param instanceof HttpNameValuePair) {
					HttpNameValuePair paramX = (HttpNameValuePair) param;
					dataMap.put(paramX.getName(), paramX.getValueObject());
				} else {
					dataMap.put(param.getName(), param.getValue());
				}
			}
		}
		return JsonUtil.toJson(dataMap, noNullValues);
	}

	// String httpPath
	public static String makeUrl(String httpPath, String urlParamString) {
		if (httpPath.indexOf("?") == -1) {
			httpPath += "?";
		} else {
			if (!httpPath.endsWith("?")) {
				httpPath += "&";
			}
		}
		return httpPath + urlParamString;
	}

	public String makeUrl(String httpPath, List<? extends NameValuePair> params) {
		String urlParamString = URLEncodedUtils.format(params, this.charset);
		return makeUrl(httpPath, urlParamString);
	}

	public void setContentType(ContentType contentType) {
		if (contentType != null) {
			if (contentType.getCharset() == null) {
				contentType = ContentTypes.create(contentType.getMimeType());
			}
		}
		this.contentType = contentType;
		//
		this.charset = contentType == null ? ContentTypes.CHARSET_UTF8 : contentType.getCharset();
	}

	public void setAccept(String accept) {
		if (StrUtil.hasText(accept)) {
			this.accept = accept;
		}
	}

	//
	public void setRequestHeader(String name, String value) {
		requestHeaders.put(name, value);
	}

	public void setRequestInterceptor(HttpRequestInterceptor requestInterceptor) {
		this.requestInterceptor = requestInterceptor;
	}

	//
	private void fillRequestHeaders(HttpUriRequest request) {
		if (request == null) {
			return;
		}
		if (this.keepAlive) {
			request.setHeader(HttpHeaders.CONNECTION, "Keep-Alive");
		}
		if (this.contentType != null) {
			request.setHeader(HttpHeaders.CONTENT_TYPE, this.contentType.toString());
		}
		request.setHeader(HttpHeaders.ACCEPT, this.accept);
		//
		for (Map.Entry<String, String> header : this.requestHeaders.entrySet()) {
			request.setHeader(header.getKey(), header.getValue());
		}
	}

	private HttpUriRequest createRequest(HttpMethod httpMethod, String httpPath, List<? extends NameValuePair> params) throws HttpException {
		if (httpPath == null) {
			httpPath = "";
		}
		if (!httpPath.startsWith("http")) {
			httpPath = this.httpBaseUrl + httpPath;
		}
		if (params == null) {
			params = new ArrayList<HttpNameValuePair>();
		}
		URI uri = null;
		HttpUriRequest request = null;
		try {
			if (HttpMethod.GET.equals(httpMethod)) {
				String httpUrl = makeUrl(httpPath, params);
				uri = new URI(httpUrl);
				HttpGet xGet = new HttpGet(uri);
				xGet.setConfig(requestConfig);
				this.fillRequestHeaders(xGet);
				//
				request = xGet;
			} else if (HttpMethod.POST.equals(httpMethod)) {
				uri = new URI(httpPath);
				HttpPost xPost = new HttpPost(uri);
				xPost.setConfig(requestConfig);
				this.fillRequestHeaders(xPost);
				//
				boolean isJson = false;
				if (this.contentType != null) {
					String mimeType = this.contentType.getMimeType();
					isJson = ContentTypes.isJson(mimeType);
				}
				if (isJson) {
					String json = toJsonString(params, true);
					StringEntity entity = new StringEntity(json, this.contentType);
					xPost.setEntity(entity);
				} else {
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, this.charset);
					xPost.setEntity(entity);
				}
				//
				request = xPost;
			} else if (HttpMethod.PUT.equals(httpMethod)) {
				uri = new URI(httpPath);
				HttpPut xPut = new HttpPut(uri);
				xPut.setConfig(requestConfig);
				this.fillRequestHeaders(xPut);
				//
				boolean isJson = false;
				if (this.contentType != null) {
					String mimeType = this.contentType.getMimeType();
					isJson = ContentTypes.isJson(mimeType);
				}
				if (isJson) {
					String json = toJsonString(params, true);
					StringEntity entity = new StringEntity(json, this.contentType);
					xPut.setEntity(entity);
				} else {
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, this.charset);
					xPut.setEntity(entity);
				}
				//
				request = xPut;
			} else if (HttpMethod.DELETE.equals(httpMethod)) {
				String httpUrl = makeUrl(httpPath, params);
				uri = new URI(httpUrl);
				HttpDelete xDelete = new HttpDelete(uri);
				xDelete.setConfig(requestConfig);
				this.fillRequestHeaders(xDelete);
				//
				request = xDelete;
			} else {
				// default to GET
				String httpUrl = makeUrl(httpPath, params);
				uri = new URI(httpUrl);
				HttpGet xGet = new HttpGet(uri);
				xGet.setConfig(requestConfig);
				this.fillRequestHeaders(xGet);
				//
				request = xGet;
			}
		} catch (URISyntaxException e) {
			throw new HttpException("Invalid URI:" + uri);
		}
		if (request != null && printRequestHeaders) {
			Header[] headers = request.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				Header header = headers[i];
				logger.debug(header.getName() + ": " + header.getValue());
			}
			// System.out.println(">>MimeType:" + ContentTypes.getMimeType(request));
		}
		return request;
	}

	private TResult sendRequest(HttpUriRequest request) throws HttpException {
		TResult result = null;
		try {
			if (this.requestInterceptor != null) {
				this.requestInterceptor.process(request, null);
			}
			//
			result = httpClient.execute(request, responseHandler);
		} catch (ClientProtocolException e) {
			logger.error(e);
			request.abort();
			throw new HttpException("Execute http " + request.getMethod() + " request fail [" + e.getMessage() + "] !");
		} catch (IOException e) {
			logger.error(e);
			request.abort();
			throw new HttpException("Execute http " + request.getMethod() + " request fail [" + e.getMessage() + "] !");
		}
		return result;
	}

	public TResult doRequest(HttpMethod httpMethod, String httpPath, List<? extends NameValuePair> params) throws HttpException {
		HttpUriRequest request = createRequest(httpMethod, httpPath, params);
		return sendRequest(request);
	}

	public TResult doGetRequest(String httpPath, List<? extends NameValuePair> params) throws HttpException {
		return doRequest(HttpMethod.GET, httpPath, params);
	}

	public TResult doPostRequest(String httpPath, List<? extends NameValuePair> params) throws HttpException {
		return doRequest(HttpMethod.POST, httpPath, params);
	}

	public TResult doPutRequest(String httpPath, List<? extends NameValuePair> params) throws HttpException {
		return doRequest(HttpMethod.PUT, httpPath, params);
	}

	public TResult doDeleteRequest(String httpPath, List<? extends NameValuePair> params) throws HttpException {
		return doRequest(HttpMethod.DELETE, httpPath, params);
	}

	@Override
	protected void finalize() throws Throwable {
		httpClient.close();
		//
		super.finalize();
	}
}
