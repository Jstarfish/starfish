package priv.starfish.common.pay.alipay;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

/* *
 *类名：AlipayFunction
 *功能：支付宝接口公用函数类
 *详细：该类是请求、通知返回两个文件所调用的公用函数核心处理文件，不需要修改
 *版本：3.3
 *日期：2012-08-14
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayCore {

	/**
	 * 除去数组中的空值和签名参数
	 * 
	 * @param paramMap
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> filterParams(Map<String, String> paramMap) {
		Map<String, String> result = new HashMap<String, String>();

		if (paramMap == null || paramMap.size() <= 0) {
			return result;
		}

		for (String key : paramMap.keySet()) {
			String value = paramMap.get(key);
			if (value == null || value.trim().equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")) {
				continue;
			}
			result.put(key, value);
		}

		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param paramMap
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createParamString(Map<String, String> paramMap) {
		List<String> keys = new ArrayList<String>(paramMap.keySet());
		Collections.sort(keys);

		StringBuilder sb = new StringBuilder();
		int paramCount = keys.size();
		for (int i = 0; i < paramCount; i++) {
			String key = keys.get(i);
			String value = paramMap.get(key);
			sb.append(key + "=" + value);
			if (i < paramCount - 1) {// 拼接时，不包括最后一个&字符
				sb.append("&");
			}
		}
		return sb.toString();
	}

	/**
	 * 把数组所有元素按照固定参数排序，以“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param paramMap
	 *            需要参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createParamStringNoSort(Map<String, String> paramMap) {

		// 手机网站支付MD5签名固定参数排序，顺序参照文档说明
		StringBuilder toSign = new StringBuilder();
		toSign.append("service=" + paramMap.get("service"));
		toSign.append("&v=" + paramMap.get("v"));
		toSign.append("&sec_id=" + paramMap.get("sec_id"));
		toSign.append("&notify_data=" + paramMap.get("notify_data"));

		return toSign.toString();
	}

	/**
	 * 生成文件摘要
	 * 
	 * @param strFilePath
	 *            文件路径
	 * @param fileDigestType
	 *            摘要算法
	 * @return 文件摘要结果
	 */
	public static String getAbstract(String strFilePath, String fileDigestType) throws IOException {
		FileInputStream fileInput = new FileInputStream(strFilePath);
		try {
			if (fileDigestType.equals("MD5")) {
				return DigestUtils.md5Hex(fileInput);
			} else if (fileDigestType.equals("SHA")) {
				return DigestUtils.sha256Hex(fileInput);
			} else {
				return "";
			}
		} catch (Exception ex) {
			return null;
		} finally {
			if (fileInput != null) {
				fileInput.close();
			}
		}
	}

}
