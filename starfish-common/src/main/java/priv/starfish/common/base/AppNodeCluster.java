package priv.starfish.common.base;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 应用节点集群
 * 
 * @author koqiui
 * @date 2015年6月27日 下午10:37:26
 *
 */
public class AppNodeCluster {
	private final Log logger = LogFactory.getLog(this.getClass());

	private AppNodeCluster() {
		//
	}

	public static interface NodeInfoListener {
		void onNodeInfo(AppNodeInfo appNodeInfo, Date ts);
	}

	// 单例对象
	private static final AppNodeCluster current = new AppNodeCluster();

	public static AppNodeCluster getCurrent() {
		return current;
	}

	private Map<String, Long> appNodesTsMap = new ConcurrentHashMap<String, Long>();
	private Map<String, AppNodeInfo> appNodesInfoMap = new ConcurrentHashMap<String, AppNodeInfo>();
	//
	private List<NodeInfoListener> nodeInfoListeners = new CopyOnWriteArrayList<NodeInfoListener>();

	public void addNodeInfoListener(NodeInfoListener nodeInfoListener) {
		if (!nodeInfoListeners.contains(nodeInfoListener)) {
			this.nodeInfoListeners.add(nodeInfoListener);
		}
	}

	public void removeNodeInfoListener(NodeInfoListener nodeInfoListener) {
		if (nodeInfoListeners.contains(nodeInfoListener)) {
			this.nodeInfoListeners.remove(nodeInfoListener);
		}
	}

	//
	private AppNodeInfo curAppNode = AppNodeInfo.getCurrent();

	public void addAppNode(AppNodeInfo appNodeInfo, long ts) {
		Date tsDate = new Date(ts);
		String appNodeName = appNodeInfo.getName();
		//
		if (!appNodeName.equals(curAppNode.getName())) {
			appNodesTsMap.put(appNodeName, ts);
			appNodesInfoMap.put(appNodeName, appNodeInfo);
			//
			// logger.debug("应用节点报告：" + appNodeInfo);
		} else {
			// logger.debug("当前节点报告：" + appNodeInfo);
		}
		//
		for (NodeInfoListener listener : nodeInfoListeners) {
			listener.onNodeInfo(appNodeInfo, tsDate);
		}
	}

	// 按节点角色获取对应的AbsBaseUrl
	public String getAbsBaseUrlByRole(String role) {
		for (Map.Entry<String, AppNodeInfo> appNodeInfoEntry : appNodesInfoMap.entrySet()) {
			AppNodeInfo appNodeInfo = appNodeInfoEntry.getValue();
			if (appNodeInfo.getRole().equals(role)) {
				return appNodeInfo.getAbsBaseUrl();
			}
		}
		if (curAppNode.getRole().equals(role)) {
			return curAppNode.getAbsBaseUrl();
		}
		return curAppNode.getAbsBaseUrl();
	}
}
