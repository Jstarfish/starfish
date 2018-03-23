package priv.starfish.common.pay.wechatpay.util;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;
import priv.starfish.common.pay.PayNotifyConfig;


public class WechatpayCore {

	/**
	 * 获取随机字符串
	 * 
	 * @return
	 */
	public static String getNonceStr() {
		// 随机数
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		return strTime + strRandom;
	}

	/**
	 * 元转换成分
	 * 
	 * @return
	 */
	public static String getMoneyY2F(String amount) {
		if (amount == null) {
			return "";
		}
		// 金额转化为分为单位
		String currency = amount.replaceAll("\\$|\\￥|\\,", ""); // 处理包含, ￥ 或者$的金额
		int index = currency.indexOf(".");
		int length = currency.length();
		Long amLong = 0l;
		if (index == -1) {
			amLong = Long.valueOf(currency + "00");
		} else if (length - index >= 3) {
			amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
		} else if (length - index == 2) {
			amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
		} else {
			amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
		}
		return amLong.toString();
	}

	/**
	 * 分转换为元
	 * 
	 * @author "WJJ"
	 * @date 2016年1月19日 上午11:29:12
	 * 
	 * @param amount
	 * @return
	 */
	public static String getMoneyF2Y(String amount) {
		if (amount == null) {
			return "";
		}
		return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();
	}

	/**
	 * description: 解析微信通知xml
	 * 
	 * @param xml
	 * @return
	 * @see
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXmlToList2(String xml) {
		Map<String, String> retMap = new HashMap<String, String>();
		try {
			StringReader read = new StringReader(xml);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			// 通过输入源构造一个Document
			Document doc = (Document) sb.build(source);
			Element root = doc.getRootElement();// 指向根节点
			List<Element> es = root.getChildren();
			if (es != null && es.size() != 0) {
				for (Element element : es) {
					retMap.put(element.getName(), element.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retMap;
	}

	/**
	 * 
	 * 
	 * @author "WJJ"
	 * @date 2016年1月15日 下午2:44:39
	 * 
	 * @param object
	 * @return
	 */
	public static String mapToString(Object object) {
		return object == null ? null : object.toString();
	}

	public static String getXml(String body, String nonce_str, String out_trade_no, String spbill_create_ip, String total_fee, String attach) {
		// 请求参数列表
		StringBuffer sb = new StringBuffer();
		// appid
		sb.append("appid=" + WechatpayConfig.appid_app);
		// 商品描述
		sb.append("&body=" + body);
		// 商户号
		sb.append("&mch_id=" + WechatpayConfig.partner_app);
		// 随机字符串
		sb.append("&nonce_str=" + nonce_str);
		// 通知地址
		sb.append("&notify_url=" + PayNotifyConfig.wx_notify_url_pc);
		// 商户订单号
		sb.append("&out_trade_no=" + out_trade_no);
		// 终端IP
		sb.append("&spbill_create_ip=" + spbill_create_ip);
		// 支付金额，分为单位
		sb.append("&total_fee=" + total_fee);
		// 回传参数
		sb.append("&attach=" + attach);
		// 交易类型
		sb.append("&trade_type=" + "APP");
		// key
		sb.append("&key=" + WechatpayConfig.partnerkey_app);

		// 生成sign
		String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
		// xml
		StringBuffer xml = new StringBuffer();
		xml.append("<xml>");
		xml.append("<appid>");
		xml.append(WechatpayConfig.appid_app);
		xml.append("</appid>");
		xml.append("<body><![CDATA[");
		xml.append(body);
		xml.append("]]></body>");
		xml.append("<mch_id>");
		xml.append(WechatpayConfig.partner_app);
		xml.append("</mch_id>");
		xml.append("<nonce_str>");
		xml.append(nonce_str);
		xml.append("</nonce_str>");
		xml.append("<notify_url>");
		xml.append(PayNotifyConfig.wx_notify_url_pc);
		xml.append("</notify_url>");
		xml.append("<out_trade_no>");
		xml.append(out_trade_no);
		xml.append("</out_trade_no>");
		xml.append("<spbill_create_ip>");
		xml.append(spbill_create_ip);
		xml.append("</spbill_create_ip>");
		xml.append("<total_fee>");
		xml.append(total_fee);
		xml.append("</total_fee>");
		xml.append("<attach>");
		xml.append(attach);
		xml.append("</attach>");
		xml.append("<trade_type>");
		xml.append("APP");
		xml.append("</trade_type>");
		xml.append("<sign>");
		xml.append(sign);
		xml.append("</sign>");
		xml.append("</xml>");

		return xml.toString();
	}
	
	public static String getJson(String nonce_str, String prepay_id) {
		String timestamp = Sha1Util.getTimeStamp();
		String s = "appid=" + WechatpayConfig.appid_app + "&noncestr=" + nonce_str + "&package=Sign=WXPay" + "&partnerid=" + WechatpayConfig.partner_app + "&prepayid=" + prepay_id + "&timestamp=" + timestamp;
		String newSign = MD5Util.MD5Encode(s + "&key=" + WechatpayConfig.partnerkey_app, "UTF-8").toUpperCase();

		StringBuffer sb = new StringBuffer();
		sb.append("{\"appid\":\"");
		sb.append(WechatpayConfig.appid_app);
		sb.append("\",\"noncestr\":\"");
		sb.append(nonce_str);
		sb.append("\",\"package\":\"");
		sb.append("Sign=WXPay");
		sb.append("\",\"partnerid\":\"");
		sb.append(WechatpayConfig.partner_app);
		sb.append("\",\"prepayid\":\"");
		sb.append(prepay_id);
		sb.append("\",\"timestamp\":\"");
		sb.append(timestamp);
		sb.append("\",\"sign\":\"");
		sb.append(newSign);
		sb.append("\"}");

		return sb.toString();
	}
	
	public static String getJsonAsDist(String nonce_str, String prepay_id) {
		String timestamp = Sha1Util.getTimeStamp();
		String s = "appid=" + WechatpayConfig.appid_huiyou_app + "&noncestr=" + nonce_str + "&package=Sign=WXPay" + "&partnerid=" + WechatpayConfig.partner_huiyou_app + "&prepayid=" + prepay_id + "&timestamp=" + timestamp;
		String newSign = MD5Util.MD5Encode(s + "&key=" + WechatpayConfig.partnerkey_huiyou_app, "UTF-8").toUpperCase();

		StringBuffer sb = new StringBuffer();
		sb.append("{\"appid\":\"");
		sb.append(WechatpayConfig.appid_huiyou_app);
		sb.append("\",\"noncestr\":\"");
		sb.append(nonce_str);
		sb.append("\",\"package\":\"");
		sb.append("Sign=WXPay");
		sb.append("\",\"partnerid\":\"");
		sb.append(WechatpayConfig.partner_huiyou_app);
		sb.append("\",\"prepayid\":\"");
		sb.append(prepay_id);
		sb.append("\",\"timestamp\":\"");
		sb.append(timestamp);
		sb.append("\",\"sign\":\"");
		sb.append(newSign);
		sb.append("\"}");

		return sb.toString();
	}

}
