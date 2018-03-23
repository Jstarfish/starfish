package priv.starfish.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class CodeUtil {
	private static final String SHORT_DATE_FORMAT_STRING = "yyyyMMdd";
	private static final int SHORT_DATE_FORMAT_LENGTH = SHORT_DATE_FORMAT_STRING.length();
	private static final SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat(SHORT_DATE_FORMAT_STRING);
	//
	private static final String SHORT_DATE_TIME_FORMAT_STRING = "yyyyMMddHHmm";
	private static final int SHORT_DATE_TIME_FORMAT_LENGTH = SHORT_DATE_TIME_FORMAT_STRING.length();
	private static final SimpleDateFormat SHORT_DATE_TIME_FORMAT = new SimpleDateFormat(SHORT_DATE_TIME_FORMAT_STRING);

	//
	public static String generateRandomCode(int length) {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return StrUtil.right(uuid, length).toUpperCase();
	}

	public static String randomNumCode(int length) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			int nextInt = random.nextInt(10);
			sb.append(nextInt);
		}

		return sb.toString();
	}

	public static String generateDatedNo(int totalLength) {
		String resultNo = SHORT_DATE_FORMAT.format(new Date());
		int latterLength = totalLength - SHORT_DATE_FORMAT_LENGTH;
		if (latterLength > 0) {
			resultNo += generateRandomCode(latterLength);
		}
		return resultNo;
	}

	public static String generateTimedNo(int totalLength) {
		String resultNo = SHORT_DATE_TIME_FORMAT.format(new Date());
		int latterLength = totalLength - SHORT_DATE_TIME_FORMAT_LENGTH;
		if (latterLength > 0) {
			resultNo += generateRandomCode(latterLength);
		}
		return resultNo;
	}

	public static String generateCode(String code, Integer uId) {
		StringBuilder sb = new StringBuilder();
		sb.append(code);
		sb.append('-');
		sb.append(uId);
		sb.append('-');
		String random = generateRandomCode(8);
		sb.append(StrUtil.seperate(random, 4, '-'));
		return sb.toString();
	}

	public static String generateCode(String code) {
		// String random = new IdWorker(1,2).getUinqStr();
		// return StrUtil.seperate(random, 4, '-');
		return generateCode(code, 0);
	}

	public static String generateCode(Integer codeId, Integer uId) {
		String code = StrUtil.leftPadding(codeId.toString(), 3, '0');
		return generateCode(code, uId);
	}

	public static String generateCode(Integer codeId) {
		return generateCode(codeId, 0);
	}

	public static void main(String[] args) {
		System.out.println(generateCode(""));
	}
}
