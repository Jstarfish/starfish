package priv.starfish.mall.jms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import priv.starfish.common.jms.SimpleMessage;
import priv.starfish.common.jms.SimpleMessageSender;

import javax.annotation.Resource;

public class SimpleMessageSenderTest extends JmsTestBase {

	@Resource
	SimpleMessageSender simpleMessageSender;

	@Before
	public void setup() {
		Assert.assertNotNull(simpleMessageSender);
	}

	@Test
	public void testSendQueueMessageSimpleMessage() {
		SimpleMessage message = SimpleMessage.newOne();
		message.change = SimpleMessage.Created;
		message.data = "hellow";

		simpleMessageSender.sendQueueMessage(message);
	}

	@Test
	public void testSendQueueMessageStringSimpleMessage() {
		SimpleMessage message = SimpleMessage.newOne();
		message.change = SimpleMessage.Created;
		message.data = "hellow";

		simpleMessageSender.sendQueueMessage("ezmall.config", message);
	}

	@Test
	public void testSendTopicMessageSimpleMessage() {
		SimpleMessage message = SimpleMessage.newOne();
		message.change = SimpleMessage.Created;
		message.data = "hellow";

		simpleMessageSender.sendTopicMessage(message);
	}

	@Test
	public void testSendTopicMessageStringSimpleMessage() {
		SimpleMessage message = SimpleMessage.newOne();
		message.change = SimpleMessage.Created;
		message.data = "hellow";

		simpleMessageSender.sendTopicMessage("ezmall.config", message);
	}

}
