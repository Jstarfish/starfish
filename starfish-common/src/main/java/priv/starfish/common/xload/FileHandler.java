package priv.starfish.common.xload;

import java.util.Map;

public interface FileHandler {
	public String getName();

	public boolean willHandle(Map<String, Object> infoMap);
}
