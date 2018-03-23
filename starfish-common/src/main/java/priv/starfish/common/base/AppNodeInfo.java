package priv.starfish.common.base;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.jms.SimpleMessage;
import priv.starfish.common.jms.SimpleMessageSender;
import priv.starfish.common.json.JsonDateTimeSerializer;
import priv.starfish.common.util.NetUtil;
import priv.starfish.common.util.OSUtil;
import priv.starfish.common.util.StrUtil;

public class AppNodeInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	// 默认的静态资源目录（以/开头，相对于WebContent）
	public static final String APP_RES_DIR = "/static";
	//
	private static final String CONFIG_PREFIX = "app.node.";
	//
	public static final String KEY_ROLE = CONFIG_PREFIX + "role";
	public static final String KEY_SERVER_BASE = CONFIG_PREFIX + "server.base";
	public static final String KEY_NAME = CONFIG_PREFIX + "name";
	public static final String KEY_MASTER = CONFIG_PREFIX + "master";
	public static final String KEY_RES_BASE_URL = CONFIG_PREFIX + "res.base.url";
	//
	@JsonIgnore
	private transient final Log logger = LogFactory.getLog(this.getClass());

	public AppNodeInfo() {
		osInfo.clear();
		osInfo.put("name", OSUtil.getOsName());
		osInfo.put("arch", OSUtil.getOsArch());
		osInfo.put("version", OSUtil.getOsVersion());
	}

	// 单例对象
	private static final AppNodeInfo current = new AppNodeInfo();

	public static AppNodeInfo getCurrent() {
		return current;
	}

	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date startedTime = new Date();
	// 节点角色名称 front,back,task...
	private String role;
	// 当前节点实例name
	private String name;
	// 当前节点实例是否为主节点
	private Boolean master = Boolean.FALSE;
	// 当前节点应用所在服务器base路径(ContextPath前面的部分)
	private String serverBase;
	// 应用的相对上下文路径
	private String appBaseUrl;
	// 应用的绝对上下文路径
	private String absBaseUrl;
	// 静态资源的相对url
	private String resBaseUrl;
	//
	private boolean _runningOnProductionServer;
	//
	private Map<String, String> osInfo = new HashMap<String, String>();

	public Map<String, String> getOsInfo() {
		return osInfo;
	}

	//
	@JsonIgnore
	private transient SimpleMessageSender simpleMessageSender;

	public void setSimpleMessageSender(SimpleMessageSender simpleMessageSender) {
		this.simpleMessageSender = simpleMessageSender;
	}

	public Date getStartedTime() {
		return startedTime;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
		//
		this.logger.debug("应用节点角色 : " + role);
	}

	public String getServerBase() {
		return serverBase;
	}

	public void setServerBase(String serverBase) {
		this.serverBase = serverBase;
		//
		this.logger.debug("应用节点所在服务器base路径 : " + serverBase);
	}

	public void setAppBaseUrl(String appBaseUrl) {
		if (StrUtil.isNullOrBlank(this.serverBase)) {
			throw new RuntimeException("请先设置服务器base路径(serverBase) ip:port");
		}
		//
		this.appBaseUrl = appBaseUrl;
		this.logger.debug("应用节点所在路径(ContextPath) : " + appBaseUrl);
		//
		this.absBaseUrl = serverBase + this.appBaseUrl;
		this.logger.debug("应用节点所在绝对路径 : " + this.absBaseUrl);
		// 应用资源url安全值
		this.resBaseUrl = this.appBaseUrl + APP_RES_DIR;
	}

	public String getAppBaseUrl() {
		return appBaseUrl;
	}

	public void setResBaseUrl(String resBaseUrl) {
		if (this.appBaseUrl == null) {
			this.logger.error("尚未设置应用的相对上下文路径(appBaseUrl)");
			throw new RuntimeException("请先设置应用的相对上下文路径(appBaseUrl)");
		}
		if (StrUtil.hasText(resBaseUrl)) {
			resBaseUrl = resBaseUrl.trim();
			if (resBaseUrl.indexOf("://") == -1) {
				this.resBaseUrl = this.appBaseUrl + resBaseUrl;
			} else {
				this.resBaseUrl = resBaseUrl;
			}
			this.logger.debug("资源服务base路径：" + this.resBaseUrl);
		} else {
			this.logger.debug("资源服务base路径（默认值）：" + this.resBaseUrl);
		}
	}

	public String getResBaseUrl() {
		return resBaseUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		//
		System.out.println("应用节点Name : " + name);
	}

	public Boolean getMaster() {
		return master;
	}

	public void setMaster(Boolean master) {
		this.master = master == null ? Boolean.FALSE : master;
	}

	public String getAbsBaseUrl() {
		return absBaseUrl;
	}

	public void setProductionServerIps(String productionServerIps) {
		_runningOnProductionServer = StrUtil.hasText(productionServerIps) ? NetUtil.hasHostIp(productionServerIps.split(",")) : false;
		//
		System.out.println(_runningOnProductionServer ? "正在 生产服务器 上运行 ！" : "不在 生产服务器上运行");
	}

	public boolean isRunningOnProductionServer() {
		return _runningOnProductionServer;
	}

	public int getClusterHeartbeatInterval() {
		return ClusterInfo.getCurrent().getClusterHeartbeatInterval();
	}

	public String getMessageTopicName() {
		return ClusterInfo.getCurrent().getMessageTopicName();
	}

	@Override
	public String toString() {
		return "AppNodeInfo [role=" + role + ", name=" + name + ", master=" + master + ", serverBase=" + serverBase + ", runningOnProductionServer=" + _runningOnProductionServer + ", clusterHeartbeatInterval="
				+ getClusterHeartbeatInterval() + ", messageTopicName=" + getMessageTopicName() + "]";
	}

	//
	@JsonIgnore
	private transient Timer taskTimer = null;

	private class HeartbeatTask extends TimerTask {
		String messageTopicName = getMessageTopicName();

		@Override
		public void run() {
			SimpleMessage messageToSend = SimpleMessage.newOne();
			messageToSend.data = AppNodeInfo.getCurrent();
			simpleMessageSender.sendTopicMessage(messageTopicName, messageToSend);
		}

	}

	public void startClusterHeartbeating() {
		if (taskTimer != null) {
			stopClusterHeartbeating();
		}
		//
		taskTimer = new Timer(false);
		//
		long clusterHeartbeatIntervalMills = ClusterInfo.getCurrent().getClusterHeartbeatIntervalMills();
		taskTimer.schedule(new HeartbeatTask(), 1, clusterHeartbeatIntervalMills);
	}

	public void stopClusterHeartbeating() {
		if (taskTimer != null) {
			taskTimer.cancel();
			taskTimer = null;
		}
	}
}
