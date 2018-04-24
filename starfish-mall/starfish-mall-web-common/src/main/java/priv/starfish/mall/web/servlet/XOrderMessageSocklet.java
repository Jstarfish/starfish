package priv.starfish.mall.web.servlet;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.model.ScopeEntity;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.user.UserContextHolder;
import priv.starfish.common.util.*;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.order.dict.OrderAction;
import priv.starfish.mall.order.dict.OrderType;
import priv.starfish.mall.order.dto.XOrderActionResult;
import priv.starfish.mall.service.misc.XOrderMessageProxy;
import priv.starfish.mall.service.misc.XOrderMessageProxy.MessageListener;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// 应用节点集群信息


@SuppressWarnings("deprecation")
@WebServlet("/misc/xOrder/message")
public class XOrderMessageSocklet extends WebSocketServlet implements MessageListener {
	private static final long serialVersionUID = 1L;
	private final Log logger = LogFactory.getLog(this.getClass());

	XOrderMessageProxy xOrderMessageProxy = XOrderMessageProxy.getInstance();

	@Override
	public void init() throws ServletException {
		super.init();
		// 注册监听
		xOrderMessageProxy.addMessageListener(this);
	}

	@Override
	public void destroy() {
		xOrderMessageProxy.removeMessageListener(this);
		// 注销监听
		super.destroy();
	}

	interface XOrderMessagePusher {
		void pushActionMessage(XOrderActionResult message);
	}

	private List<XOrderMessagePusher> xOrderMessagePushers = new CopyOnWriteArrayList<XOrderMessagePusher>();

	protected static UserContext getUserContext(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (UserContext) (session == null ? null : WebUtil.getSessionAttribute(session, UserContextHolder.SESSION_KEY_USER_CONTEXT));
	}

	@Override
	protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {
		//
		RequestMessageInbound xOrderMessagePusher = new RequestMessageInbound();
		//
		String orderType = request.getParameter("orderType");
		if (StrUtil.hasText(orderType)) {
			xOrderMessagePusher.setOrderType(orderType);
		}
		//
		String orderNo = request.getParameter("orderNo");
		xOrderMessagePusher.setOrderNo(orderNo);
		//
		String byUserIdStr = request.getParameter("byUserId");
		Boolean byUserId = false;
		try {
			byUserId = BoolUtil.isTrue(byUserIdStr);
		} catch (NumberFormatException nfe) {
			//
		}
		//
		String orderAction = request.getParameter("orderAction");
		xOrderMessagePusher.setOrderAction(orderAction);
		// 检查消息权限和范围
		UserContext userContext = getUserContext(request);
		if (userContext == null || !userContext.isSysUser()) {
			xOrderMessagePusher.setHasLoggedIn(false);
		} else {
			xOrderMessagePusher.setHasLoggedIn(true);
			//
			if (userContext.isInScopeEntity()) {
				Integer shopId = null;
				ScopeEntity scopeEntity = userContext.getScopeEntity();
				AuthScope userScope = EnumUtil.valueOf(AuthScope.class, scopeEntity.getScope());
				if (AuthScope.mall.equals(userScope)) {
					shopId = -1;
				} else if (AuthScope.shop.equals(userScope)) {
					shopId = scopeEntity.getId();
				}
				xOrderMessagePusher.setShopId(shopId);
			} else {
				if (byUserId) {
					xOrderMessagePusher.setUserId(userContext.getUserId());
				}
			}
		}
		//
		return xOrderMessagePusher;
	}

	class RequestMessageInbound extends MessageInbound implements XOrderMessagePusher {
		Boolean hasLoggedIn = false;
		OrderType orderType = null;// 必须参数
		//
		Integer shopId = null;
		// 或者
		String orderNo = null;
		Integer userId = null;

		OrderAction orderAction = null;
		//
		boolean paramsOk = false;

		public void setHasLoggedIn(Boolean hasLoggedIn) {
			this.hasLoggedIn = hasLoggedIn;
		}

		public void setOrderType(String orderType) {
			this.orderType = EnumUtil.valueOf(OrderType.class, orderType);
		}

		public void setShopId(Integer shopId) {
			this.shopId = shopId;
		}

		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}

		public void setUserId(Integer userId) {
			this.userId = userId;
		}

		public void setOrderAction(String orderAction) {
			this.orderAction = EnumUtil.valueOf(OrderAction.class, orderAction);
		}

		@Override
		protected void onClose(int status) {
			logger.debug("客户端已断开");
			//
			xOrderMessagePushers.remove(this);
		}

		@Override
		protected void onOpen(WsOutbound outbound) {
			logger.debug("客户端已连接");
			//
			xOrderMessagePushers.add(this);
			//
			Result<String> result = Result.newOne();
			if (this.orderType == null) {
				// 设置默认类型
				this.orderType = OrderType.saleOrder;
			}
			//
			if (this.hasLoggedIn) {
				if (userId != null) {
					// 登录用户接收订单信息
					paramsOk = true;
					result.data = "您可以接收" + orderType.getText() + "操作信息了";
				} else if (shopId != null) {
					// 后台商城/店铺按类型接收
					paramsOk = true;
					result.data = "您可以接收" + orderType.getText() + "操作信息了";
				}
			}
			//
			if (!paramsOk) {
				if (StrUtil.hasText(orderNo) && orderAction != null) {
					// 前台按指定订单的action接收
					paramsOk = true;
				} else {
					result.type = Type.warn;
					result.message = "无法确定要接收的订单信息";
				}
			}
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
			result.message = "不接受请求内容（参数请在url中传递）";
			//
			String jsonStr = JsonUtil.toJson(result);
			try {
				this.getWsOutbound().writeTextMessage(CharBuffer.wrap(jsonStr));
			} catch (IOException e) {
				logger.error(e);
			}
		}

		@Override
		public void pushActionMessage(XOrderActionResult message) {
			if (!this.paramsOk) {
				return;
			}
			//
			OrderType msgOrderType = message.orderType;
			if (!this.orderType.equals(msgOrderType)) {
				return;
			}
			boolean shouldOutput = false;

			if (this.shopId != null) {
				if (this.shopId == -1 || this.shopId.equals(message.shopId)) {
					shouldOutput = true;
				}
			} else if (userId != null) {
				if (userId.equals(message.userId)) {
					shouldOutput = true;
				}
			} else {
				if (this.orderNo.equals(message.orderNo) && this.orderAction.equals(message.orderAction)) {
					shouldOutput = true;
				}
			}
			//
			if (shouldOutput) {
				Result<XOrderActionResult> result = Result.newOne();
				//
				result.data = message;
				//
				String jsonStr = JsonUtil.toJson(result);
				try {
					this.getWsOutbound().writeTextMessage(CharBuffer.wrap(jsonStr));
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
	}

	@Override
	public void onActionMessage(XOrderActionResult message, String source) {
		for (XOrderMessagePusher xOrderMessagePusher : xOrderMessagePushers) {
			xOrderMessagePusher.pushActionMessage(message);
		}
	}

}
