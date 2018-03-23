package priv.starfish.common.jms;

import java.io.Serializable;
import java.util.Map;

public interface MessageHandler {
	void handleMessage(String text);

	void handleMessage(Map<?, ?> map);

	void handleMessage(byte[] bytes);

	void handleMessage(Serializable object);
}
