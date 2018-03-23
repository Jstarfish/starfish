package priv.starfish.mall.service.xyz;

import java.util.List;

import org.apache.http.HttpException;

import priv.starfish.common.http.ContentTypes;
import priv.starfish.common.http.HttpClientX;
import priv.starfish.common.http.HttpNameValuePair;
import priv.starfish.common.http.StringResponseHandler;
import priv.starfish.common.util.StrUtil;

public abstract class CrawlerBase<TResult> implements Crawler<TResult> {
	protected static void println(String tag, Object content) {
		System.out.println(">>" + tag + "--------------------------------");
		System.out.println(content);
	}

	@SuppressWarnings("rawtypes")
	public static <T> Crawler newInstance(Class<? extends Crawler> T) {
		try {
			return T.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String extractJsonExpr(String jsLines) {
		if (StrUtil.hasText(jsLines)) {
			int eqIndex = jsLines.indexOf("=");
			if (eqIndex != -1) {
				int scIndex = jsLines.indexOf(";", eqIndex + 1);
				if (scIndex != -1) {
					return jsLines.substring(eqIndex + 1, scIndex).replace("\\", "\\\\");
				}
			}
		}
		return null;
	}

	public static HttpClientX<String> getHttpClient(String httpBaseUrl, String path) {
		return new HttpClientX<String>(httpBaseUrl + path, new StringResponseHandler());
	}

	public static String getForString(HttpClientX<String> httpClient, List<HttpNameValuePair> params, String asWhat) {
		return getForString(httpClient, null, params, asWhat);
	}

	public static String getForString(HttpClientX<String> httpClient, String partPath, List<HttpNameValuePair> params, String asWhat) {
		if (StrUtil.hasText("html")) {
			httpClient.setAccept(ContentTypes.withMimeTypeAll(ContentTypes.TEXT_HTML_VALUE));
		} else {
			httpClient.setAccept(ContentTypes.withMimeTypeAll(ContentTypes.APPLICATION_JSON_VALUE));
		}
		try {
			return httpClient.doGetRequest(partPath, params);
		} catch (HttpException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String postForString(HttpClientX<String> httpClient, List<HttpNameValuePair> params, String asWhat) {
		return postForString(httpClient, null, params, asWhat);
	}

	public static String postForString(HttpClientX<String> httpClient, String partPath, List<HttpNameValuePair> params, String asWhat) {
		if (StrUtil.hasText("html")) {
			httpClient.setAccept(ContentTypes.withMimeTypeAll(ContentTypes.TEXT_HTML_VALUE));
		} else {
			httpClient.setAccept(ContentTypes.withMimeTypeAll(ContentTypes.APPLICATION_JSON_VALUE));
		}
		try {
			return httpClient.doPostRequest(partPath, params);
		} catch (HttpException e) {
			e.printStackTrace();
		}
		return null;
	}

}
