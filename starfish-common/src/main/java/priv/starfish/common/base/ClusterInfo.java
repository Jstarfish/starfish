package priv.starfish.common.base;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import priv.starfish.common.util.StrUtil;

public class ClusterInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	//
	private static final String CONFIG_PREFIX = "cluster.";
	//
	public static final String KEY_PRODUCTION_SERVER_IPS = CONFIG_PREFIX + "production.server.ips";
	public static final String KEY_HEARTBEAT_INTERVAL = CONFIG_PREFIX + "heartbeat.interval";
	public static final String KEY_MESSAGE_TOPIC_NAME = CONFIG_PREFIX + "message.topic.name";
	//
	@JsonIgnore
	private transient final Log logger = LogFactory.getLog(this.getClass());

	public ClusterInfo() {
	}

	// 单例对象
	private static final ClusterInfo current = new ClusterInfo();

	public static ClusterInfo getCurrent() {
		return current;
	}

	// 生产服务器ip列表（用以判断是否在生产服务器上运行）
	@JsonIgnore
	private String productionServerIps;
	// 集群心跳时间间隔（秒）
	private int clusterHeartbeatInterval = 10;
	private transient long clusterHeartbeatIntervalMills = TimeUnit.SECONDS.toMillis(clusterHeartbeatInterval);
	// jms消息Topic名称
	private String messageTopicName;
	//

	public void setProductionServerIps(String productionServerIps) {
		if (StrUtil.hasText(productionServerIps)) {
			this.productionServerIps = productionServerIps;
		}
	}

	public String getProductionServerIps() {
		return productionServerIps;
	}

	public void setClusterHeartbeatInterval(int clusterHeartbeatInterval) {
		if (clusterHeartbeatInterval > 0) {
			this.clusterHeartbeatInterval = clusterHeartbeatInterval;
			this.clusterHeartbeatIntervalMills = TimeUnit.SECONDS.toMillis(clusterHeartbeatInterval);
		}
	}

	public int getClusterHeartbeatInterval() {
		return clusterHeartbeatInterval;
	}

	public long getClusterHeartbeatIntervalMills() {
		return clusterHeartbeatIntervalMills;
	}

	public void setMessageTopicName(String messageTopicName) {
		this.messageTopicName = messageTopicName;
		//
		this.logger.debug("消息主题设置为：" + messageTopicName);
	}

	public String getMessageTopicName() {
		return messageTopicName;
	}

}
