package priv.starfish.common.jms;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.listener.SubscriptionNameProvider;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MessageHandlerAdapter implements MessageHandler, SubscriptionNameProvider {
	@JsonIgnore
	protected final Log logger = LogFactory.getLog(this.getClass());
	//
	protected String defaultSubscriptionName = this.getClass().getName();
	//
	protected String subscriptionName;

	public void setSubscriptionName(String subscriptionName) {
		this.subscriptionName = subscriptionName;
	}

	public String getSubscriptionName() {
		return subscriptionName == null ? defaultSubscriptionName : subscriptionName;
	}

	@Override
	public void handleMessage(String text) {
		logger.warn("Ignored Message >> text : " + text);
	}

	@Override
	public void handleMessage(Map<?, ?> map) {
		logger.warn("Ignored Message >> map : " + map);
	}

	@Override
	public void handleMessage(byte[] bytes) {
		logger.warn("Ignored Message >> bytes : " + bytes);
	}

	@Override
	public void handleMessage(Serializable object) {
		logger.warn("Ignored Message >> object : " + object);
	}

}
