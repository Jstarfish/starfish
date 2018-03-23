package priv.starfish.common.http;

import java.net.HttpURLConnection;
import java.nio.charset.Charset;

import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;

public final class ContentTypes {
	private ContentTypes() {
		// ...
	}

	public final static Charset CHARSET_UTF8 = Charset.forName("UTF-8");
	//
	public final static String ALL_VALUE = "*/*";
	public final static String APPLICATION_ATOM_XML_VALUE = "application/atom+xml";
	public final static String APPLICATION_XHTML_XML_VALUE = "application/xhtml+xml";
	public final static String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";
	public final static String MULTIPART_FORM_DATA_VALUE = "multipart/form-data";
	public final static String APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream";
	public final static String APPLICATION_XML_VALUE = "application/xml";
	public final static String APPLICATION_JSON_VALUE = "application/json";
	public final static String TEXT_XML_VALUE = "text/xml";
	public final static String TEXT_HTML_VALUE = "text/html";
	public final static String TEXT_PLAIN_VALUE = "text/plain";
	public final static String IMAGE_GIF_VALUE = "image/gif";
	public final static String IMAGE_JPEG_VALUE = "image/jpeg";
	public final static String IMAGE_PNG_VALUE = "image/png";
	public final static String IMAGE_BMP_VALUE = "image/bmp";
	//
	public final static ContentType ALL = ContentType.create(ALL_VALUE, CHARSET_UTF8);
	public final static ContentType APPLICATION_ATOM_XML = ContentType.create(APPLICATION_ATOM_XML_VALUE, CHARSET_UTF8);
	public final static ContentType APPLICATION_XHTML_XML = ContentType.create(APPLICATION_XHTML_XML_VALUE, CHARSET_UTF8);
	public final static ContentType APPLICATION_FORM_URLENCODED = ContentType.create(APPLICATION_FORM_URLENCODED_VALUE, CHARSET_UTF8);
	public final static ContentType MULTIPART_FORM_DATA = ContentType.create(MULTIPART_FORM_DATA_VALUE, CHARSET_UTF8);
	public final static ContentType APPLICATION_OCTET_STREAM = ContentType.create(APPLICATION_OCTET_STREAM_VALUE, (Charset) null);
	public final static ContentType APPLICATION_XML = ContentType.create(APPLICATION_XML_VALUE, CHARSET_UTF8);
	public final static ContentType APPLICATION_JSON = ContentType.create(APPLICATION_JSON_VALUE, CHARSET_UTF8);
	public final static ContentType TEXT_XML = ContentType.create(TEXT_XML_VALUE, CHARSET_UTF8);
	public final static ContentType TEXT_HTML = ContentType.create(TEXT_HTML_VALUE, CHARSET_UTF8);
	public final static ContentType TEXT_PLAIN = ContentType.create(TEXT_PLAIN_VALUE, CHARSET_UTF8);
	public final static ContentType IMAGE_GIF = ContentType.create(IMAGE_GIF_VALUE, CHARSET_UTF8);
	public final static ContentType IMAGE_JPEG = ContentType.create(IMAGE_JPEG_VALUE, CHARSET_UTF8);
	public final static ContentType IMAGE_PNG = ContentType.create(IMAGE_PNG_VALUE, CHARSET_UTF8);
	public final static ContentType IMAGE_BMP = ContentType.create(IMAGE_BMP_VALUE, CHARSET_UTF8);

	public static ContentType create(String mimeType, String charset) {
		return ContentType.create(mimeType, charset);
	}

	public static ContentType create(String mimeType) {
		return ContentType.create(mimeType, CHARSET_UTF8);
	}

	//
	public static String extractMimeType(String contentType) {
		if (contentType == null) {
			return null;
		}
		ContentType tmp = ContentType.parse(contentType);
		return tmp == null ? null : tmp.getMimeType();
	}

	public static Charset extractCharset(String contentType) {
		if (contentType == null) {
			return null;
		}
		ContentType tmp = ContentType.parse(contentType);
		return tmp == null ? null : tmp.getCharset();
	}

	public static String getMimeType(HttpURLConnection urlConnection) {
		return extractMimeType(urlConnection.getContentType());
	}

	public static String getMimeType(HttpUriRequest request) {
		Header header = request.getFirstHeader(Headers.CONTENT_TYPE);
		if (header == null) {
			return null;
		} else {
			return extractMimeType(header.getValue());
		}
	}

	public static String withMimeTypeAll(String mimeType) {
		return mimeType + ", " + ALL_VALUE;
	}

	public static boolean isHtml(String mimeType) {
		return mimeType != null && mimeType.endsWith("/html");
	}

	public static boolean isXml(String mimeType) {
		return mimeType != null && mimeType.endsWith("xml");
	}

	public static boolean isPlain(String mimeType) {
		return mimeType != null && mimeType.endsWith("/plain");
	}

	public static boolean isJson(String mimeType) {
		return mimeType != null && mimeType.endsWith("/json");
	}

	public static boolean isFormUrlEncoded(String mimeType) {
		return mimeType != null && mimeType.endsWith("form-urlencoded");
	}

	public static boolean isImage(String mimeType) {
		return mimeType != null && mimeType.startsWith("image/");
	}

	public static boolean isStream(String mimeType) {
		return mimeType != null && mimeType.endsWith("stream");
	}
}
