package priv.starfish.common.jms;

import java.io.Serializable;

public class SimpleMessageHandler extends MessageHandlerAdapter {

	@Override
	public void handleMessage(Serializable object) {
		assert object instanceof SimpleMessage;
		//
		SimpleMessage simpleMessage = (SimpleMessage) object;

		System.out.println(simpleMessage.toString());

	}

}
