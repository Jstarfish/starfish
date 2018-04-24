package priv.starfish.common.xload;

import priv.starfish.common.exception.XRuntimeException;
import priv.starfish.common.util.FileSize;
import priv.starfish.common.util.StrUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 上传配置解析、存取器
 * 
 * @author koqiui
 * @date 2015年5月23日 下午4:12:23
 *
 */
public class UploadConfig {
	private static final String CONFIG_PREFIX = "upload.";
	//
	public static final String KEY_TMP_FILE_DIR = CONFIG_PREFIX + "tmp.file.dir";
	public static final String KEY_TOTAL_SIZE_LIMIT = CONFIG_PREFIX + "total.size.limit";
	public static final String KEY_FILE_SIZE_LIMIT = CONFIG_PREFIX + "file.size.limit";
	public static final String KEY_TO_DISK_THRESHOLD = CONFIG_PREFIX + "to.disk.threshold";
	public static final String KEY_ALLOWED_FILE_EXTS = CONFIG_PREFIX + "allowed.file.exts";

	// 上传文件临时保存目录（最好是绝对路径）
	private File _tmpFileDir = null;

	public File getTmpFileDir() {
		return _tmpFileDir;
	}

	public void setTmpFileDir(String tmpFileDir) {
		if (StrUtil.isNullOrBlank(tmpFileDir)) {
			return;
		}
		tmpFileDir = tmpFileDir.trim();
		File dir = new File(tmpFileDir);
		if (dir.exists()) {
			if (!dir.isDirectory()) {
				throw new XRuntimeException("指定的文件上传临时目录【" + tmpFileDir + "】有问题：已存在但不是目录！");
			}
		} else {
			dir.mkdir();
		}
		if (!dir.canWrite()) {
			System.out.println("指定的文件上传临时目录【" + tmpFileDir + "】不能写入！");
		}
		this._tmpFileDir = dir;
		System.out.println(">> tmp-file-dir : " + dir.getAbsolutePath());
	}

	// 单次上传文件总大小限制（单位：KB千字节）
	private int _totalSizeLimit = 100 * FileSize.K;

	public int getTotalSizeLimit() {
		return _totalSizeLimit;
	}

	public void setTotalSizeLimit(String totalSizeLimit) {
		if (StrUtil.isNullOrBlank(totalSizeLimit)) {
			return;
		}
		this._totalSizeLimit = FileSize.fromSizeStr(totalSizeLimit);
		System.out.println("UploadConfig total-size-limit : " + this._totalSizeLimit / FileSize.K + "M");

	}

	// 单个文件大小限制（默认单位：KB千字节）
	private int _fileSizeLimit = 10 * FileSize.K;

	public int getFileSizeLimit() {
		return _fileSizeLimit;
	}

	public void setFileSizeLimit(String fileSizeLimit) {
		if (StrUtil.isNullOrBlank(fileSizeLimit)) {
			return;
		}
		this._fileSizeLimit = FileSize.fromSizeStr(fileSizeLimit);
		System.out.println(">> file-size-limit : " + this._fileSizeLimit / FileSize.K + "M");
	}

	// 决定临时文件缓存在内存（而不暂存在临时目录）中的最大值（单位：KB千字节）
	private int _toDiskThreshold = 10;

	public int getToDiskThreshold() {
		return _toDiskThreshold;
	}

	public void setToDiskThreshold(String toDiskThreshold) {
		if (StrUtil.isNullOrBlank(toDiskThreshold)) {
			return;
		}
		this._toDiskThreshold = FileSize.fromSizeStr(toDiskThreshold);
		System.out.println(">> to-disk-threshold : " + this._toDiskThreshold + "K");
	}

	// 允许上传的文件的扩展名
	private List<String> _allowedFileExts = null;

	public List<String> getAllowedFileExts() {
		return _allowedFileExts;
	}

	public void setAllowedFileExts(String allowedFileExts) {
		if (StrUtil.isNullOrBlank(allowedFileExts)) {
			return;
		}
		_allowedFileExts = new ArrayList<String>();
		String[] tmpExts = allowedFileExts.split(",", -1);
		for (String tmpExt : tmpExts) {
			tmpExt = tmpExt.trim().toLowerCase();
			if (StrUtil.hasText(tmpExt)) {
				_allowedFileExts.add(tmpExt);
			}
		}
		System.out.println(">> allowed-file-exts : " + this._allowedFileExts);
	}
}
