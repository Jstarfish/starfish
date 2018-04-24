package priv.starfish.common.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import priv.starfish.common.helper.FileHelper;

import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;


/**
 * RSA加密解密工具
 * 
 * @author koqiui
 * @date 2015年12月15日 下午11:49:52
 *
 */
public class RSACrypter {
	private static final Log logger = LogFactory.getLog(RSACrypter.class);
	//
	public static final int MAX_SAFE_TEXT_LENGTH = 42;

	/** 算法名称 */
	private static final String ALGORITHOM = "RSA";
	/** 保存生成的密钥对的文件名称。 */
	private static final String KEY_PAIR_FILENAME = "conf/crypt.keys";
	/** 密钥大小 */
	private static final int KEY_SIZE = 1024;
	/** 默认的安全服务提供者 */
	private static final Provider DEFAULT_PROVIDER = new BouncyCastleProvider();
	//
	// private static final String RANDOM_SEED_STR = "etcb2015".substring(0, 8);
	//
	/** 是否自动读取keypair */
	private static final boolean AUTO_LOAD_KEY_PAIR = true;
	//
	private static KeyPairGenerator keyPairGenerator = null;
	private static KeyFactory keyFactory = null;
	/** 缓存的密钥对。 */
	private static KeyPair cachedKeyPair = null;

	//
	public static long lastLoadTime = -1;

	static {
		try {
			keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
			keyFactory = KeyFactory.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
			//
			if (AUTO_LOAD_KEY_PAIR) {
				readKeyPair();
			}
		} catch (NoSuchAlgorithmException ex) {
			logger.error(ex.getMessage());
		}
	}

	private RSACrypter() {
	}

	/**
	 * 生成并返回RSA密钥对。
	 */
	private static KeyPair generateKeyPair() {
		try {
			SecureRandom secureRandom = new SecureRandom();
			keyPairGenerator.initialize(KEY_SIZE, secureRandom);
			return keyPairGenerator.generateKeyPair();
		} catch (InvalidParameterException ex) {
			logger.error("KeyPairGenerator does not support a key length of " + KEY_SIZE + ".", ex);
		} catch (NullPointerException ex) {
			logger.error("CryptUtil#KEY_PAIR_GEN is null, can not generate KeyPairGenerator instance.", ex);
		}
		return null;
	}

	/**
	 * 生成并保存/输出RSA密钥对
	 * 
	 * @param keyPairFilePath
	 *            输出保存的文件路径名称
	 * @return 是否成功
	 */
	public static boolean createKeyPairFile(String keyPairFilePath) {
		KeyPair keyPair = generateKeyPair();
		if (keyPair == null) {
			return false;
		}
		//
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = FileUtils.openOutputStream(new File(keyPairFilePath));
			oos = new ObjectOutputStream(fos);
			oos.writeObject(keyPair);
			//
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			//
			return false;
		} finally {
			IOUtils.closeQuietly(oos);
			IOUtils.closeQuietly(fos);
		}
	}

	public static KeyPair readKeyPair() {
		lastLoadTime = System.currentTimeMillis();
		//
		return readKeyPair(null);
	}

	// 同步读出保存的密钥对
	public static KeyPair readKeyPair(String keyPairFilePath) {
		InputStream fis = null;
		ObjectInputStream ois = null;
		try {
			if (StrUtil.isNullOrBlank(keyPairFilePath)) {
				keyPairFilePath = KEY_PAIR_FILENAME;
			}
			fis = FileHelper.getResourceAsInputStream(keyPairFilePath);
			ois = new ObjectInputStream(fis);
			cachedKeyPair = (KeyPair) ois.readObject();
			return cachedKeyPair;
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			IOUtils.closeQuietly(ois);
			IOUtils.closeQuietly(fis);
		}
		return null;
	}

	/**
	 * 返回RSA密钥对。
	 */
	public static KeyPair getKeyPair() {
		return cachedKeyPair == null ? readKeyPair() : cachedKeyPair;
	}

	/**
	 * 根据给定的系数和专用指数构造一个RSA专用的公钥对象。
	 * 
	 * @param modulus
	 *            系数。
	 * @param publicExponent
	 *            专用指数。
	 * @return RSA专用公钥对象。
	 */
	public static RSAPublicKey generatePublicKey(byte[] modulus, byte[] publicExponent) {
		RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
		try {
			return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
		} catch (InvalidKeySpecException ex) {
			logger.error("RSAPublicKeySpec is unavailable.", ex);
		} catch (NullPointerException ex) {
			logger.error("CryptUtil#KEY_FACTORY is null, can not generate KeyFactory instance.", ex);
		}
		return null;
	}

	/**
	 * 根据给定的系数和专用指数构造一个RSA专用的私钥对象。
	 * 
	 * @param modulus
	 *            系数。
	 * @param privateExponent
	 *            专用指数。
	 * @return RSA专用私钥对象。
	 */
	public static RSAPrivateKey generatePrivateKey(byte[] modulus, byte[] privateExponent) {
		RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
		try {
			return (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
		} catch (InvalidKeySpecException ex) {
			logger.error("RSAPrivateKeySpec is unavailable.", ex);
		} catch (NullPointerException ex) {
			logger.error("CryptUtil#KEY_FACTORY is null, can not generate KeyFactory instance.", ex);
		}
		return null;
	}

	/**
	 * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的私钥对象。
	 * 
	 * @return RSA专用私钥对象。
	 */
	public static RSAPrivateKey getPrivateKey(String hexModulus, String hexPrivateExponent) {
		if (StringUtils.isBlank(hexModulus) || StringUtils.isBlank(hexPrivateExponent)) {
			if (logger.isDebugEnabled()) {
				logger.debug("hexModulus and hexPrivateExponent cannot be empty. RSAPrivateKey value is null to return.");
			}
			return null;
		}
		byte[] modulus = null;
		byte[] privateExponent = null;
		try {
			modulus = Hex.decodeHex(hexModulus.toCharArray());
			privateExponent = Hex.decodeHex(hexPrivateExponent.toCharArray());
		} catch (DecoderException ex) {
			logger.error("hexModulus or hexPrivateExponent value is invalid. return null(RSAPrivateKey).");
		}
		if (modulus != null && privateExponent != null) {
			return generatePrivateKey(modulus, privateExponent);
		}
		return null;
	}

	/**
	 * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的公钥对象。
	 * 
	 * @return RSA专用公钥对象。
	 */
	public static RSAPublicKey getPublidKey(String hexModulus, String hexPublicExponent) {
		if (StringUtils.isBlank(hexModulus) || StringUtils.isBlank(hexPublicExponent)) {
			if (logger.isDebugEnabled()) {
				logger.debug("hexModulus and hexPublicExponent cannot be empty. return null(RSAPublicKey).");
			}
			return null;
		}
		byte[] modulus = null;
		byte[] publicExponent = null;
		try {
			modulus = Hex.decodeHex(hexModulus.toCharArray());
			publicExponent = Hex.decodeHex(hexPublicExponent.toCharArray());
		} catch (DecoderException ex) {
			logger.error("hexModulus or hexPublicExponent value is invalid. return null(RSAPublicKey).");
		}
		if (modulus != null && publicExponent != null) {
			return generatePublicKey(modulus, publicExponent);
		}
		return null;
	}

	/**
	 * 使用指定的公钥加密数据。
	 * 
	 * @param publicKey
	 *            给定的公钥。
	 * @param data
	 *            要加密的数据。
	 * @return 加密后的数据。
	 */
	public static byte[] encrypt(PublicKey publicKey, byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	/**
	 * 使用指定的私钥解密数据。
	 * 
	 * @param privateKey
	 *            给定的私钥。
	 * @param data
	 *            要解密的数据。
	 * @return 原数据。
	 */
	public static byte[] decrypt(PrivateKey privateKey, byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	/**
	 * 使用给定的公钥加密给定的字符串。
	 * <p />
	 * 若 {@code publicKey} 为 {@code null}，或者 {@code plaintext} 为 {@code null} 则返回 {@code
	 * null}。
	 * 
	 * @param publicKey
	 *            给定的公钥。
	 * @param plainText
	 *            字符串。
	 * @return 给定字符串的密文。
	 */
	public static String encryptString(PublicKey publicKey, String plainText) {
		if (publicKey == null || plainText == null) {
			return null;
		}
		byte[] data = plainText.getBytes();
		try {
			byte[] en_data = encrypt(publicKey, data);
			return new String(Hex.encodeHex(en_data));
		} catch (Exception ex) {
			logger.error(ex.getCause().getMessage());
		}
		return null;
	}

	/**
	 * 使用默认的公钥加密给定的字符串。
	 * <p />
	 * 若{@code plaintext} 为 {@code null} 则返回 {@code null}。
	 * 
	 * @param plainText
	 *            字符串。
	 * @return 给定字符串的密文。
	 */
	public static String encryptString(String plainText) {
		if (plainText == null) {
			return null;
		}
		byte[] data = plainText.getBytes();
		KeyPair keyPair = getKeyPair();
		try {
			byte[] en_data = encrypt((RSAPublicKey) keyPair.getPublic(), data);
			return new String(Hex.encodeHex(en_data));
		} catch (NullPointerException ex) {
			logger.error("keyPair cannot be null.");
		} catch (Exception ex) {
			logger.error(ExceptionUtil.extractMsg(ex));
		}
		return null;
	}

	/**
	 * 使用给定的私钥解密给定的字符串。
	 * <p />
	 * 若私钥为 {@code null}，或者 {@code encrypttext} 为 {@code null}或空字符串则返回 {@code null}。 私钥不匹配时，返回 {@code null}。
	 * 
	 * @param privateKey
	 *            给定的私钥。
	 * @param encryptedText
	 *            密文。
	 * @return 原文字符串。
	 */
	public static String decryptString(PrivateKey privateKey, String encryptedText) {
		if (privateKey == null || StringUtils.isBlank(encryptedText)) {
			return null;
		}
		try {
			byte[] en_data = Hex.decodeHex(encryptedText.toCharArray());
			byte[] data = decrypt(privateKey, en_data);
			return new String(data);
		} catch (Exception ex) {
			logger.error(String.format("\"%s\" Decryption failed. Cause: %s", encryptedText, ex.getCause().getMessage()));
		}
		return null;
	}

	/**
	 * 使用默认的私钥解密给定的字符串。
	 * <p />
	 * 若{@code encrypttext} 为 {@code null}或空字符串则返回 {@code null}。 私钥不匹配时，返回 {@code null}。
	 * 
	 * @param encryptedText
	 *            密文。
	 * @return 原文字符串。
	 */
	public static String decryptString(String encryptedText) {
		if (StringUtils.isBlank(encryptedText)) {
			return null;
		}
		KeyPair keyPair = getKeyPair();
		try {
			byte[] en_data = Hex.decodeHex(encryptedText.toCharArray());
			byte[] data = decrypt((RSAPrivateKey) keyPair.getPrivate(), en_data);
			return new String(data);
		} catch (NullPointerException ex) {
			logger.error("keyPair cannot be null.");
		} catch (Exception ex) {
			logger.error(String.format("\"%s\" Decryption failed. Cause: %s", encryptedText, ex.getMessage()));
		}
		return null;
	}

	/**
	 * 使用默认的私钥解密由JS加密（使用此类提供的公钥加密）的字符串。
	 * 
	 * @param encryptedText
	 *            密文。
	 * @return 原文文字符串。
	 */
	public static String decryptStringFromJs(String encryptedText) {
		String text = decryptString(encryptedText);
		if (text == null) {
			return null;
		}
		try {
			String tmpStr = URLDecoder.decode(text, WebUtil.DEFAULT_ENCODING);
			return StringUtils.reverse(tmpStr);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 返回已初始化的默认的公钥。 */
	public static RSAPublicKey getDefaultPublicKey() {
		KeyPair keyPair = getKeyPair();
		if (keyPair != null) {
			return (RSAPublicKey) keyPair.getPublic();
		}
		return null;
	}

	/** 返回已初始化的默认的私钥。 */
	public static RSAPrivateKey getDefaultPrivateKey() {
		KeyPair keyPair = getKeyPair();
		if (keyPair != null) {
			return (RSAPrivateKey) keyPair.getPrivate();
		}
		return null;
	}

	public static PublicKeyInfo getPublicKeyInfo() {
		PublicKeyInfo publicKeyInfo = new PublicKeyInfo();
		RSAPublicKey rsaPublicKey = getDefaultPublicKey();
		publicKeyInfo.setModulus(new String(Hex.encodeHex(rsaPublicKey.getModulus().toByteArray())));
		publicKeyInfo.setExponent(new String(Hex.encodeHex(rsaPublicKey.getPublicExponent().toByteArray())));
		// publicKeyInfo.setKeySize(KEY_SIZE);
		return publicKeyInfo;
	}

	public static class PublicKeyInfo {
		private String modulus;
		private String exponent;
		private int keySize = KEY_SIZE;

		public int getKeySize() {
			return keySize;
		}

		public void setKeySize(int keySize) {
			this.keySize = keySize;
		}

		public String getModulus() {
			return modulus;
		}

		public void setModulus(String modulus) {
			this.modulus = modulus;
		}

		public String getExponent() {
			return exponent;
		}

		public void setExponent(String exponent) {
			this.exponent = exponent;
		}

		@Override
		public String toString() {
			return "PublicKeyInfo [ modulus : " + modulus + ", exponent : " + exponent + " ]";
		}
	}
}
