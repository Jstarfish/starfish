package priv.starfish.common.util;

import java.util.List;
import java.util.Map;

public class JsonRSACrypter {

	public static String encode(Map<String, Object> data, String cryptKey) {
		if (data == null) {
			return null;
		}
		String jsonStr = JsonUtil.toJson(data, true);
		// 切分，加密，连接
		List<String> slicedJsons = StrUtil.slice(jsonStr, RSACrypter.MAX_SAFE_TEXT_LENGTH);
		for (int i = 0; i < slicedJsons.size(); i++) {
			String encryted = RSACrypter.encryptString(slicedJsons.get(i));
			slicedJsons.set(i, encryted);
		}
		return StrUtil.join(slicedJsons, ",");
	}

	public static Map<String, Object> decode(String dataStr, String cryptKey) {
		if (dataStr == null) {
			return null;
		}
		// 分割，解密，拼接
		String[] slicedJsons = StrUtil.split(dataStr, ",");
		for (int i = 0; i < slicedJsons.length; i++) {
			String decrypted = RSACrypter.decryptString(slicedJsons[i]);
			slicedJsons[i] = decrypted;
		}
		String jsonStr = StrUtil.splice(slicedJsons);
		//
		return JsonUtil.fromJson(jsonStr, TypeUtil.TypeRefs.StringObjectMapType);
	}

}
