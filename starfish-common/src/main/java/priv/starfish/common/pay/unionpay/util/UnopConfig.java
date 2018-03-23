package priv.starfish.common.pay.unionpay.util;

import com.unionpay.acp.sdk.HttpClient;
import com.unionpay.acp.sdk.SDKUtil;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class UnopConfig {
	public static String encoding = "UTF-8";
	public static String merId = "777290058110097";//商户号码
	public static String version = "5.0.0";
	//后台服务对应的写法参照 FrontRcvResponse.java
	//public static String frontUrl = "http://localhost:8080/ACPTest/acp_front_url.do";

	//后台服务对应的写法参照 FrontRcvResponse.java
	public static String frontUrl = "http://172.18.137.63:8080/ACPSample-PCGate-UTF8/frontRcvResponse";

	//后台服务对应的写法参照 BackRcvResponse.java
	public static String backUrl = "http://222.222.222.222:8080/ACPSample-PCGate-UTF8/BackRcvResponse";//受理方和发卡方自选填写的域[O]--后台通知地址

	public static String getOrderId() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	/**
	 * java main方法 数据提交　 数据组装进行提交 包含签名
	 *
	 * @param contentData
	 * @return 返回报文 map
	 */
	public static Map<String, String> submitDate(Map<String, ?> contentData,String requestUrl) {
		Map<String, String> submitFromData = (Map<String, String>) signData(contentData);
		return submitUrl(submitFromData,requestUrl);
	}

	/**
	 * java main方法 数据提交 提交到后台
	 *
	 * @return 返回报文 map
	 */
	public static Map<String, String> submitUrl(Map<String, String> submitFromData,String requestUrl) {
		String resultString = "";
		System.out.println("requestUrl====" + requestUrl);
		System.out.println("submitFromData====" + submitFromData.toString());
		/**
		 * 发送
		 */
		HttpClient hc = new HttpClient(requestUrl, 30000, 30000);
		try {
			int status = hc.send(submitFromData, encoding);
			if (200 == status) {
				resultString = hc.getResult();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, String> resData = new HashMap<String, String>();
		/**
		 * 验证签名
		 */
		if (null != resultString && !"".equals(resultString)) {
			// 将返回结果转换为map
			resData = SDKUtil.convertResultStringToMap(resultString);
			if (SDKUtil.validate(resData, encoding)) {
				System.out.println("验证签名成功");
			} else {
				System.out.println("验证签名失败");
			}
			// 打印返回报文
			System.out.println("打印返回报文：" + resultString);
		}
		return resData;
	}
	/**
	 * java main方法 数据提交 　　 对数据进行签名
	 *
	 * @param contentData
	 * @return　签名后的map对象
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> signData(Map<String, ?> contentData) {
		Entry<String, String> obj = null;
		Map<String, String> submitFromData = new HashMap<String, String>();
		for (Iterator<?> it = contentData.entrySet().iterator(); it.hasNext();) {
			obj = (Entry<String, String>) it.next();
			String value = obj.getValue();
			if (StringUtils.isNotBlank(value)) {
				// 对value值进行去除前后空处理
				submitFromData.put(obj.getKey(), value.trim());
				System.out.println(obj.getKey() + "-->" + String.valueOf(value));
			}
		}
		/** 签名 */
		SDKUtil.sign(submitFromData, encoding);
		return submitFromData;
	}


	/**
	 * 构造HTTP POST交易表单的方法示例
	 *
	 * @param action
	 *            表单提交地址
	 * @param hiddens
	 *            以MAP形式存储的表单键值
	 * @return 构造好的HTTP POST交易表单
	 */
	public static String createHtml(String action, Map<String, String> hiddens) {
		StringBuffer sf = new StringBuffer();
		sf.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\"/></head><body>");
		sf.append("<form id=\"pay_form\" name=\"pay_form\" action=\"" + action +"\" method=\"post\">");
		if (null != hiddens && 0 != hiddens.size()) {
			Set<Entry<String, String>> set = hiddens.entrySet();
			Iterator<Entry<String, String>> it = set.iterator();
			while (it.hasNext()) {
				Entry<String, String> ey = it.next();
				String key = ey.getKey();
				String value = ey.getValue();
				sf.append("<input type=\"hidden\" name=\"" + key + "\" id=\"" + key + "\" value=\"" + value + "\"/>");
			}
		}
		sf.append("<input type=\"submit\" value=\"确认\" style=\"display:none;\">");
		sf.append("</form>");
		sf.append("</body>");
		sf.append("<script type=\"text/javascript\">");
		sf.append("document.forms['pay_form'].submit();");
		sf.append("</script>");
		sf.append("</html>");
		return sf.toString();
	}
}
