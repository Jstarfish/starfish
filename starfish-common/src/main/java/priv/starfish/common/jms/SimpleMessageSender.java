package priv.starfish.common.jms;

public interface SimpleMessageSender {
	void sendQueueMessage(SimpleMessage message);

	void sendQueueMessage(String queueName, SimpleMessage message);

	void sendTopicMessage(SimpleMessage message);

	void sendTopicMessage(String topicName, SimpleMessage message);
}
