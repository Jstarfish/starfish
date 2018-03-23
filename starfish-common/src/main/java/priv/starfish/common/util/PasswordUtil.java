package priv.starfish.common.util;

/**
 * 
 * @author koqiui
 *
 */
public class PasswordUtil {
	private static final int HASH_INTERATIONS = 1024;
	private static final int SALT_BYTES = 8;

	/**
	 * 生成加密盐字符串
	 * 
	 * @return
	 */
	public static String generateSaltStr() {
		byte[] saltBytes = DigestUtil.generateSalt(SALT_BYTES);
		return EncodeUtil.encodeHex(saltBytes);
	}

	/**
	 * 根据密码原文和加密盐生成加密后的密码串
	 * 
	 * @param rawPassword
	 * @param saltStr
	 * @return
	 */
	public static String encrypt(String rawPassword, String saltStr) {
		byte[] passwordBytes = rawPassword.getBytes();
		byte[] saltBytes = EncodeUtil.decodeHex(saltStr);
		byte[] resultBytes = DigestUtil.sha1(passwordBytes, saltBytes, HASH_INTERATIONS);

		return EncodeUtil.encodeHex(resultBytes);
	}

	/**
	 * 判断给定的原文密码和加密盐 是否与加过密的密码串匹配<br/>
	 * 注意：通常不要这样做（容易泄露密码），而是获取saltStr，调用 encrypt得到结果后去与数据库中的密码比较是否相等
	 * 
	 * @param rawPassword
	 * @param saltStr
	 * @param encrypted
	 * @return
	 */
	public static boolean matches(String rawPassword, String saltStr, String encrypted) {
		String testEncrypted = encrypt(rawPassword, saltStr);
		return encrypted.equals(testEncrypted);
	}
}
