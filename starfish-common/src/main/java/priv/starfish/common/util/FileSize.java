package priv.starfish.common.util;

/**
 * 文件大小转化
 * 
 * @author koqiui
 * @date 2015年5月23日 下午4:24:33
 *
 */
public class FileSize {
	public static final int B = 1;
	public static final int K = 1024 * B;
	public static final int M = 1024 * K;
	public static final int G = 1024 * M;

	/**
	 * @param sizeStr
	 *            2M12K
	 * @return K
	 */
	public static int fromSizeStr(String sizeStr) {
		sizeStr = sizeStr.trim().toUpperCase();
		int size = -1;
		String unit = "B";
		int Unit = B;
		int unitIndex = sizeStr.indexOf(unit);
		if (unitIndex != -1) {
			size = Integer.parseInt(sizeStr.substring(0, unitIndex).trim());
		} else {
			unit = "K";
			Unit = K;
			unitIndex = sizeStr.indexOf(unit);
			if (unitIndex != -1) {
				size = Integer.parseInt(sizeStr.substring(0, unitIndex).trim());
			} else {
				unit = "M";
				Unit = M;
				unitIndex = sizeStr.indexOf(unit);
				if (unitIndex != -1) {
					size = Integer.parseInt(sizeStr.substring(0, unitIndex).trim());
				} else {
					unit = "K";
					Unit = K;
					size = Integer.parseInt(sizeStr.trim());
				}
			}
		}
		return size * Unit / K;
	}

	/**
	 * @param byteSize
	 * @return 2M12K
	 */
	public static String toSizeStr(long byteSize) {
		long remain = byteSize;
		int sizeG = (int) remain / G;
		remain = remain - sizeG * G;
		int sizeM = (int) remain / M;
		remain = remain - sizeM * M;
		int sizeK = (int) remain / K;
		remain = remain - sizeK * K;
		StringBuffer sb = new StringBuffer();
		if (sizeG > 0) {
			sb.append(sizeG + "G,");
		}
		if (sizeM > 0) {
			sb.append(sizeM + "M,");
		}
		if (sizeK > 0) {
			sb.append(sizeK + "K,");
		} else {
			sb.append(remain + "B,");
		}
		return sb.substring(0, sb.length() - 1);
	}
}
