package priv.starfish.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import priv.starfish.common.helper.FileHelper;
import priv.starfish.common.http.HttpBodyMessage;

public final class WebUtil {
	public static final String HEADER_REFERER_NAME = "Referer";
	//
	private static MappingJackson2HttpMessageConverter jsonConverter = null;

	static {
		jsonConverter = new MappingJackson2HttpMessageConverter();
		ObjectMapper objectMapper = new priv.starfish.common.json.JacksonObjectMapper();
		jsonConverter.setObjectMapper(objectMapper);
		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		supportedMediaTypes.add(MediaType.APPLICATION_JSON);
		jsonConverter.setSupportedMediaTypes(supportedMediaTypes);
	}

	private WebUtil() {
		//
	}

	public static String getIpAddrFromRequest(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			//
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
				//
				if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
					ip = request.getHeader("HTTP_CLIENT_IP");
					//
					if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
						ip = request.getHeader("HTTP_X_FORWARDED_FOR");
						//
						if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
							ip = request.getRemoteAddr();
							//
							if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
								ip = "unknown";
							}
						}
					}
				}
			}
		}
		//
		return ip;
	}

	//

	public static String DEFAULT_ENCODING = "UTF-8";

	public static String CONTENT_TYPE_JSON = "application/json";
	public static String CONTENT_TYPE_HTML = "text/html";
	public static String CONTENT_TYPE_JAVASCRIPT = "text/javascript";

	public static void markAsJson(HttpServletResponse response) throws IOException {
		response.setContentType(CONTENT_TYPE_JSON);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
		response.addDateHeader("Expires", 1L);
	}

	public static PrintWriter responseAsJson(HttpServletResponse response, Object jsonObject) throws IOException {
		return responseAsJson(response, jsonObject, null, false);
	}

	public static PrintWriter responseAsJson(HttpServletResponse response, Object jsonObject, boolean cache) throws IOException {
		return responseAsJson(response, jsonObject, null, cache);
	}

	public static PrintWriter responseAsJson(HttpServletResponse response, Object jsonObject, String encoding) throws IOException {
		return responseAsJson(response, jsonObject, encoding, false);
	}

	public static PrintWriter responseAsJson(HttpServletResponse response, Object jsonObject, String encoding, boolean cache) throws IOException {
		if (!StrUtil.hasText(encoding)) {
			encoding = DEFAULT_ENCODING;
		}
		response.setContentType(CONTENT_TYPE_JSON);
		response.setCharacterEncoding(encoding);
		if (!cache) {
			response.addHeader("Pragma", "no-cache");
			response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
			response.addDateHeader("Expires", 1L);
		}
		PrintWriter writer = response.getWriter();
		writer.write(JsonUtil.toJson(jsonObject));
		writer.flush();
		//
		return writer;
	}

	public static void markAsHtml(HttpServletResponse response) throws IOException {
		response.setContentType(CONTENT_TYPE_HTML);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
		response.addDateHeader("Expires", 1L);
	}

	public static PrintWriter responseAsHtml(HttpServletResponse response, String htmlContent) throws IOException {
		return responseAsHtml(response, htmlContent, null);
	}

	public static PrintWriter responseAsHtml(HttpServletResponse response, String htmlContent, String encoding) throws IOException {
		if (!StrUtil.hasText(encoding)) {
			encoding = DEFAULT_ENCODING;
		}
		response.setContentType(CONTENT_TYPE_HTML);
		response.setCharacterEncoding(encoding);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
		response.addDateHeader("Expires", 1L);
		PrintWriter writer = response.getWriter();
		writer.write(htmlContent);
		writer.flush();
		//
		return writer;
	}

	public static void markAsJavascript(HttpServletResponse response) throws IOException {
		response.setContentType(CONTENT_TYPE_JAVASCRIPT);
	}

	public static PrintWriter responseAsJavascript(HttpServletResponse response, String jsContent) throws IOException {
		return responseAsJavascript(response, jsContent, null);
	}

	public static PrintWriter responseAsJavascript(HttpServletResponse response, String jsContent, String encoding) throws IOException {
		if (!StrUtil.hasText(encoding)) {
			encoding = DEFAULT_ENCODING;
		}
		response.setContentType(CONTENT_TYPE_JAVASCRIPT);
		response.setCharacterEncoding(encoding);
		PrintWriter writer = response.getWriter();
		writer.write(jsContent);
		writer.flush();
		//
		return writer;
	}

	public static String getContextPathPrefix(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		sb.append(request.getScheme()).append("://");
		sb.append(request.getServerName());
		int port = request.getServerPort();
		if (port != 80) {
			sb.append(":").append(port);
		}
		return sb.toString();
	}

	public static String getFullContextPath(HttpServletRequest request) {
		return getContextPathPrefix(request) + request.getContextPath();
	}

	public static String requestBodyAsString(HttpServletRequest request) {
		try {
			BufferedReader reader = request.getReader();
			StringBuffer sb = new StringBuffer();
			String lineStr = null;
			while ((lineStr = reader.readLine()) != null) {
				sb.append(lineStr);
				sb.append(FileHelper.FILE_SEPERATOR);
			}
			int length = sb.length();
			if (length > 0) {
				sb.deleteCharAt(length - 1);
			}
			reader.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> Object convertToType(HttpServletRequest request, Class<T> javaType) throws IOException {
		HttpBodyMessage httpBodyMessage = new HttpBodyMessage(request);
		return jsonConverter.read(javaType, httpBodyMessage);
	}

	/**
	 * 返回带请求参数的的url
	 * 
	 * @author koqiui
	 * @date 2015年12月9日 下午12:24:33
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param includeContextPath
	 *            是否包含应用上下文路径（contextPath）
	 * @return
	 */
	public static String getRequestUriWithParams(HttpServletRequest request, boolean includeContextPath) {
		String requestUri = request.getRequestURI();
		if (!includeContextPath) {
			String contextPath = request.getContextPath();
			requestUri = requestUri.substring(contextPath.length());
		}
		String queryString = request.getQueryString();
		if (StrUtil.hasText(queryString)) {
			return requestUri + "?" + queryString;
		} else {
			return requestUri;
		}
	}

	public static String getRequestUriWithParams(HttpServletRequest request) {
		return getRequestUriWithParams(request, true);
	}

	public static String getRequestUrlWithParams(HttpServletRequest request) {
		String requestUrl = request.getRequestURL().toString();
		String queryString = request.getQueryString();
		if (StrUtil.hasText(queryString)) {
			return requestUrl + "?" + queryString;
		} else {
			return requestUrl;
		}
	}

	//
	private static Pattern PatternStaticResource = Pattern.compile(".+(\\.js|\\.css|\\.png|\\.jpg|\\.gif|\\.bmp|\\.ico|\\.swf)$");

	public static boolean isStaticResourceURI(String uri) {
		return PatternStaticResource.matcher(uri).matches();
	}

	//
	public static void setSessionAttribute(HttpSession session, String attrKey, Object attrVal) {
		session.setAttribute(attrKey, attrVal);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getSessionAttribute(HttpSession session, String attrKey) {
		return (T) session.getAttribute(attrKey);
	}

	public static void setSessionAttribute(HttpServletRequest request, String attrKey, Object attrVal) {
		setSessionAttribute(request.getSession(true), attrKey, attrVal);
	}

	public static <T> T getSessionAttribute(HttpServletRequest request, String attrKey) {
		return getSessionAttribute(request.getSession(false), attrKey);
	}

	//
	public static String getRefererHeader(HttpServletRequest request) {
		return request.getHeader(HEADER_REFERER_NAME);
	}

	// cookie.getPath() 和 cookie.getDomain() 总是= null
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				// System.out.println("cookie>>"+JsonUtil.toJson(cookie));
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		return null;
	}

	public static void removeCookie(HttpServletResponse response, String name, String path, String domain) {
		Cookie cookie = new Cookie(name, null);
		cookie.setMaxAge(0);
		if (path != null) {
			cookie.setPath(path);
		}
		if (domain != null) {
			cookie.setDomain(domain);
		}
		response.addCookie(cookie);
	}

	public static void removeCookie(HttpServletResponse response, String name, String path) {
		removeCookie(response, name, path, null);
	}

	public static void removeCookie(HttpServletResponse response, String name) {
		removeCookie(response, name, null, null);
	}

	// :: http://localhost:8080/web-front/ucenter/saleOrder/list/jsp
	// => http://localhost:8080
	public static String getServerBase(String url) {
		if (StrUtil.isNullOrBlank(url)) {
			return null;
		}
		int slashIndex = url.indexOf("://") + 3;
		if (slashIndex < 3) {
			return null;
		}
		slashIndex = url.indexOf("/", slashIndex);
		return url.substring(0, slashIndex);
	}

	/**
	 * 返回应用相对url
	 * 
	 * @author koqiui
	 * @date 2015年12月10日 下午6:04:55
	 * 
	 * @return
	 */
	public static String getAppRelUrl(HttpServletRequest request, String url) {
		if (url == null) {
			url = request.getRequestURL().toString();
			// System.out.println("::>> " + url);
		}
		//
		String resultUrl = url;
		//
		String serverBase = getServerBase(url);
		if (serverBase != null) {
			int index = url.indexOf(serverBase);
			resultUrl = url.substring(index + serverBase.length());
		}
		String contextPath = request.getContextPath();
		if (StrUtil.hasText(contextPath)) {
			int index = resultUrl.indexOf(contextPath);
			if (index != -1) {
				resultUrl = resultUrl.substring(index + contextPath.length());
			}
		}
		return resultUrl;
	}

	public static String getAppRelUrl(HttpServletRequest request) {
		return getAppRelUrl(request, null);
	}

	// 检查并设置缓存时间

	public void checkAndSetCacheTime(HttpServletRequest request, HttpServletResponse response, long cacheTimeInMs) {
		long headerLastModified = request.getDateHeader("If-Modified-Since");
		long curTimeInMs = System.currentTimeMillis();
		if (headerLastModified >= curTimeInMs) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
		} else {
			response.setDateHeader("Last-Modified", DateUtil.ceilToSecond(curTimeInMs + cacheTimeInMs));
		}
	}

}
