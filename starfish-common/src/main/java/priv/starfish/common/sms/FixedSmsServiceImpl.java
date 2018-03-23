package priv.starfish.common.sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import priv.starfish.common.http.HttpClientX;
import priv.starfish.common.http.HttpNameValuePair;
import priv.starfish.common.util.MapContext;


/**
 * 短信服务（通过向短信网关发送http请求实现）
 * 
 * @author zhangjunjun
 * 
 */
public class FixedSmsServiceImpl implements SmsService {

	private static final Log logger = LogFactory.getLog(FixedSmsServiceImpl.class);

	private String uid;

	private String key;

	/**
	 * 控制是否启用发送短信功能，enabled = true为发送，enabled = false为不发送
	 */
	private boolean enabled = true;

	private HttpClientX<String> sendHttpClient;

	private HttpClientX<String> queryHttpClient;

	@Override
	public SmsErrorCode sendSms(SmsMessage message, MapContext extra) {
		if (!enabled) {
			return SmsErrorCode.OK;
		}

		List<HttpNameValuePair> params = new ArrayList<HttpNameValuePair>();
		params.add(new HttpNameValuePair("Uid", uid));
		params.add(new HttpNameValuePair("Key", key));
		params.add(new HttpNameValuePair("smsMob", message.getReceiverNumber()));
		params.add(new HttpNameValuePair("smsText", message.getText()));
		try {
			String result = sendHttpClient.doGetRequest(null, params);
			if (StringUtils.isNotBlank(result)) {
				for (SmsErrorCode scode : SmsErrorCode.values()) {
					if (scode.getValue().equals(Integer.parseInt(result))) {
						return scode;
					}
				}
			}
		} catch (HttpException e) {
			logger.error("send sms fail!", e);
		}
		return SmsErrorCode.HTTP_ERROR;
	}

	@Override
	public int getSmsRemain() {
		int remain = Integer.MIN_VALUE;
		List<HttpNameValuePair> params = new ArrayList<HttpNameValuePair>();
		params.add(new HttpNameValuePair("Action", "SMS_Num"));
		params.add(new HttpNameValuePair("Uid", uid));
		params.add(new HttpNameValuePair("Key", key));
		try {
			String result = queryHttpClient.doGetRequest(null, params);
			if (StringUtils.isNotBlank(result)) {
				return Integer.valueOf(result);
			}
		} catch (Exception e) {
			logger.error("query sms remains fail!", e);
		}
		return remain;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setSendHttpClient(HttpClientX<String> sendHttpClient) {
		this.sendHttpClient = sendHttpClient;
	}

	public void setQueryHttpClient(HttpClientX<String> queryHttpClient) {
		this.queryHttpClient = queryHttpClient;
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		// 发送内容
		String content = "您好，欢迎关注！";

		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb = new StringBuffer("http://api.duanxin.cm/?");

		// 向StringBuffer追加用户名
		sb.append("action=send&username=70203463");

		// 向StringBuffer追加密码（密码采用MD5 32位 小写）
		String pwd = "15001290309";
		sb.append("&password=" + MD5(pwd));

		// 向StringBuffer追加手机号码
		sb.append("&phone=+65 93576965");

		// 向StringBuffer追加消息内容转URL标准码
		sb.append("&content=" + URLEncoder.encode(content));
		sb.append("&encode=utf8");

		/*
		 * content = URLEncoder.encode(content, "UTF-8"); System.out.println("encode :"+content); //backstage java/jsp dispose content = URLDecoder.decode(content, "UTF-8");
		 * System.out.println("decode :"+content);
		 */

		// 创建url对象
		URL url = new URL(sb.toString());

		// 打开url连接
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// 设置url请求方式 ‘get’ 或者 ‘post’
		connection.setRequestMethod("POST");

		// 发送
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

		// 返回发送结果
		String inputline = in.readLine();

		// 返回结果为‘100’ 发送成功
		System.out.println(inputline);
	}

	private static String MD5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
			System.out.println("MD5(" + sourceStr + ",32) = " + result);
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return result;
	}
}
