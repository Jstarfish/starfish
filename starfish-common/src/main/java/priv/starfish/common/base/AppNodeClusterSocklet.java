/*
package priv.starfish.common.base;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.ResultCode;
import priv.starfish.common.util.DateUtil;
import priv.starfish.common.util.JsonUtil;


*/
/**
 * 应用节点集群信息
 *//*

@SuppressWarnings("deprecation")
@WebServlet("/appNodeCluster")
public class AppNodeClusterSocklet extends WebSocketServlet implements AppNodeCluster.NodeInfoListener {
	private static final long serialVersionUID = 1L;
	private final Log logger = LogFactory.getLog(this.getClass());

	@Override
	public void init() throws ServletException {
		super.init();
		// 注册监听
		AppNodeCluster.getCurrent().addNodeInfoListener(this);
	}

	@Override
	public void destroy() {
		AppNodeCluster.getCurrent().removeNodeInfoListener(this);
		// 注销监听
		super.destroy();
	}

	interface AppNodeInfoPusher {
		void pushAppNodeInfo(AppNodeInfo appNodeInfo, Date ts);
	}

	private List<AppNodeInfoPusher> appNodeInfoPushers = new CopyOnWriteArrayList<AppNodeInfoPusher>();

	@Override
	protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {
		// HttpSession session = request.getSession();
		// TODO 检查用户权限等
		RequestMessageInbound appNodeInfoPusher = new RequestMessageInbound();
		//
		return appNodeInfoPusher;
	}

	class RequestMessageInbound extends MessageInbound implements AppNodeInfoPusher {

		@Override
		protected void onClose(int status) {
			logger.debug("客户端已断开");
			//
			appNodeInfoPushers.remove(this);
		}

		@Override
		protected void onOpen(WsOutbound outbound) {
			logger.debug("客户端已连接");
			//
			appNodeInfoPushers.add(this);
			//
			Result<String> result = Result.newOne();
			result.data = "您可以接收集群应用节点信息了";
			//
			String jsonStr = JsonUtil.toJson(result);
			try {
				this.getWsOutbound().writeTextMessage(CharBuffer.wrap(jsonStr));
			} catch (IOException e) {
				logger.error(e);
			}
		}

		@Override
		protected void onBinaryMessage(ByteBuffer byteBuffer) throws IOException {
			throw new UnsupportedOperationException("不支持二进制消息.");
		}

		@Override
		protected void onTextMessage(CharBuffer message) throws IOException {
			logger.debug("接收到的消息是：" + message);
			//
			// Request request = JsonUtil.fromJson(message.toString(), Request.class);
			// 处理请求...
			//
			Result<?> result = Result.newOne();
			result.type = Type.warn;
			result.message = "不接受请求内容";
			//
			String jsonStr = JsonUtil.toJson(result);
			try {
				this.getWsOutbound().writeTextMessage(CharBuffer.wrap(jsonStr));
			} catch (IOException e) {
				logger.error(e);
			}
		}

		@Override
		public void pushAppNodeInfo(AppNodeInfo appNodeInfo, Date ts) {
			Result<Map<String, Object>> result = Result.newOne();
			//
			result.code = ResultCode.App.AppNode_Info;
			//
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("appNode", appNodeInfo);
			data.put("ts", DateUtil.toStdDateTimeStr(ts));
			result.data = data;
			//
			String jsonStr = JsonUtil.toJson(result);
			try {
				this.getWsOutbound().writeTextMessage(CharBuffer.wrap(jsonStr));
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

	@Override
	public void onNodeInfo(AppNodeInfo appNodeInfo, Date ts) {
		for (AppNodeInfoPusher appNodeInfoPusher : appNodeInfoPushers) {
			appNodeInfoPusher.pushAppNodeInfo(appNodeInfo, ts);
		}
	}

}
*/
