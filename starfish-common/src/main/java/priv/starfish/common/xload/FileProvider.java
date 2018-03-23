package priv.starfish.common.xload;

import java.util.Map;

public interface FileProvider extends FileHandler {
	public static final String CONFIG_PREFIX = "file.provider.";

	// 方法实现要负责在结果中提供输入流
	public FileResult provideFile(Map<String, Object> infoMap);
}
