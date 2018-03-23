package priv.starfish.common.xload;

import java.io.InputStream;
import java.util.Map;

public interface FileReceiver extends FileHandler {
	public static final String CONFIG_PREFIX = "file.receiver.";

	// 方法实现要负责最终关闭输入流
	public FileResult receiveFile(Map<String, Object> infoMap, InputStream inputStream);
}
