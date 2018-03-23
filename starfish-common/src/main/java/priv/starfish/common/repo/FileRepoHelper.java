package priv.starfish.common.repo;

import priv.starfish.common.util.DateUtil;
import priv.starfish.common.util.StrUtil;

import java.util.Date;
import java.util.UUID;


public class FileRepoHelper {
	public static String generateRelativeFileDir(Date date, String subUsage) {
		String dateDir = DateUtil.toDateDirStr(date);
		return StrUtil.hasText(subUsage) ? subUsage + "/" + dateDir : dateDir;
	}

	public static String generateRelativeFileDir(String subUsage) {
		return generateRelativeFileDir(new Date(), subUsage);
	}

	public static String generateRelativeFileDir() {
		return generateRelativeFileDir(new Date(), null);
	}

	public static String generateUniqueFileName(String fileExt) {
		return UUID.randomUUID().toString() + fileExt;
	}
}
