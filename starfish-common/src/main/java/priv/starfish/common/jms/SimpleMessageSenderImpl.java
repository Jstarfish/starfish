package priv.starfish.common.jms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;
import priv.starfish.common.base.AppNodeInfo;

public class SimpleMessageSenderImpl implements SimpleMessageSender {
	@JsonIgnore
	protected final Log logger = LogFactory.getLog(this.getClass());
	//
	protected JmsTemplate jmsQueueTemplate;
	protected JmsTemplate jmsTopicTemplate;

	public void setJmsQueueTemplate(JmsTemplate jmsQueueTemplate) {
		this.jmsQueueTemplate = jmsQueueTemplate;
		//
		logger.debug("jmsQueueTemplate set");
	}

	public void setJmsTopicTemplate(JmsTemplate jmsTopicTemplate) {
		this.jmsTopicTemplate = jmsTopicTemplate;
		//
		logger.debug("jmsTopicTemplate set");
	}

	@Override
	public void sendQueueMessage(String queueName, SimpleMessage message) {
		assert message != null;
		//
		message.type = SimpleMessage.Queue;
		if (message.source == null) {
			message.source = AppNodeInfo.getCurrent().getName();
		}
		if (message.category == null) {
			message.category = queueName;
		}
		if (queueName == null) {
			jmsQueueTemplate.convertAndSend(message);
		} else {
			jmsQueueTemplate.convertAndSend(queueName, message);
		}
		//
		//logger.debug(message.category + " sent to queue : " + (queueName == null ? " <default> " : queueName));
	}

	@Override
	public void sendTopicMessage(String topicName, SimpleMessage message) {
		assert message != null;
		//
		message.type = SimpleMessage.Topic;
		if (message.source == null) {
			message.source = AppNodeInfo.getCurrent().getName();
		}
		if (message.category == null) {
			message.category = topicName;
		}
		if (topicName == null) {
			jmsTopicTemplate.convertAndSend(message);
		} else {
			jmsTopicTemplate.convertAndSend(topicName, message);
		}
		//
		//logger.debug(message.category + " sent to topic : " + (topicName == null ? " <default> " : topicName));
	}

	@Override
	public void sendQueueMessage(SimpleMessage message) {
		this.sendQueueMessage(null, message);
	}

	@Override
	public void sendTopicMessage(SimpleMessage message) {
		this.sendTopicMessage(null, message);
	}
}
