package priv.starfish.common.webols;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import priv.starfish.common.jms.SimpleMessageSender;
import priv.starfish.common.model.Result;
import priv.starfish.common.util.BoolUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.WebUtil;
import priv.starfish.common.web.WebEnvHelper;

/**
 * 处理 客户端请求与响应
 * 
 * @author koqiui
 * @date 2015年6月21日 下午5:41:51
 *
 */
public class OlsServlet extends HttpServlet implements OlsProtocol {
	private static final long serialVersionUID = 1L;
	//
	private static final String INIT_PARAM_NAME_USE_JMS = "useJms";
	private static final String INIT_PARAM_NAME_JMS_TOPIC_NAME = "jmsTopicName";

	private final Log logger = LogFactory.getLog(this.getClass());

	private OlsUserInfoProvider userInfoProvider;
	private OlsFocusInfoProvider focusInfoProvider;

	public void init(ServletConfig config) throws ServletException {
		// jms支持相关参数
		String tmpStr = config.getInitParameter(INIT_PARAM_NAME_USE_JMS);
		if (BoolUtil.isTrue(tmpStr)) {
			tmpStr = config.getInitParameter(INIT_PARAM_NAME_JMS_TOPIC_NAME);
			if (StrUtil.isNullOrBlank(tmpStr)) {
				throw new IllegalArgumentException("启用jms 支持时必须指定 " + INIT_PARAM_NAME_JMS_TOPIC_NAME);
			}
			OlsMessageProxy.getInstance().setMessageTopicName(tmpStr);
			SimpleMessageSender simpleMessageSender = WebEnvHelper.getSpringBean(SimpleMessageSender.class);
			if (simpleMessageSender == null) {
				throw new IllegalArgumentException("启用jms 必须存在有效的  SimpleMessageSender 对象");
			}
			OlsMessageProxy.getInstance().setSimpleMessageSender(simpleMessageSender);
		}
		userInfoProvider = WebEnvHelper.getSpringBean(OlsUserInfoProvider.class);
		if (userInfoProvider == null) {
			userInfoProvider = new DemoOlsUserInfoProvider();
		}
		focusInfoProvider = WebEnvHelper.getSpringBean(OlsFocusInfoProvider.class);
		if (focusInfoProvider == null) {
			focusInfoProvider = new DemoOlsFocusInfoProvider();
		}
		//
		OlsSessionManager.getInstance().start();
		//
	}

	@Override
	public void destroy() {
		//
		OlsSessionManager.getInstance().stop();
		//
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		//
		String reqAction = request.getParameter(PARAM_ACTION);
		if (ACTION_VALUE_INFO_FLAG.equalsIgnoreCase(reqAction)) {
			// 返回对话人员信息
			String peerType = request.getParameter(PARAM_PEER_TYPE);
			String peerId = request.getParameter(PARAM_PEER_ID);
			Result<Object> result = Result.newOne();
			if (OlsProtocol.SOURCE_SERVANT.equalsIgnoreCase(peerType)) {
				OlsServant servant = userInfoProvider.getServant(peerId, request);
				result.data = servant;
			} else {
				OlsCustomer servant = userInfoProvider.getCustomer(peerId, request);
				result.data = servant;
			}
			WebUtil.responseAsJson(response, result);
			//
			return;
		} else if (ACTION_VALUE_FOCUS_FLAG.equalsIgnoreCase(reqAction)) {
			// 返回对话人员信息
			String focusCode = request.getParameter(PARAM_FOCUS_CODE);
			String focusId = request.getParameter(PARAM_FOCUS_ID);
			Result<Object> result = Result.newOne();
			result.data = focusInfoProvider.getFocusInfo(focusCode, focusId, request);
			WebUtil.responseAsJson(response, result);
			return;
		}
		response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OlsHttpAdapter pusher = OlsHttpAdapter.newOne(response);
		// 会话处理
		OlsEvent event = null;
		String eventSource = null, eventType = null, snId = null;
		//
		try {
			MapContext mapContext = (MapContext) WebUtil.convertToType(request, MapContext.class);
			//
			eventSource = mapContext.getTypedValue(PARAM_EVENT_SOURCE, String.class);
			eventType = mapContext.getTypedValue(PARAM_EVENT_TYPE, String.class);
			snId = mapContext.getTypedValue(PARAM_SESSION_ID, String.class);
			// 获取合适的pusher
			int sessionType = OlsSession.typeBySource(eventSource);
			//
			if (sessionType == OlsSession.TYPE_UNKNOWN) {
				logger.warn("Pushlet.doPost(): bad request, no valid " + PARAM_EVENT_SOURCE + " specified");
				//
				OlsEvent errRspEvent = OlsEvent.newEvent(eventSource, OlsProtocol.EVENT_FACK, null);
				errRspEvent.setAttr(OlsProtocol.PARAM_SESSION_ID, snId);
				errRspEvent.setAttr(OlsProtocol.PARAM_REASON, "未指定有效的会话来源(" + PARAM_EVENT_SOURCE + ")");
				pusher.push(errRspEvent);
				return;
			}
			if (eventType == null) {
				logger.warn("Pushlet.doPost(): bad request, no " + PARAM_EVENT_TYPE + " specified");
				//
				OlsEvent errRspEvent = OlsEvent.newEvent(eventSource, OlsProtocol.EVENT_FACK, null);
				errRspEvent.setAttr(OlsProtocol.PARAM_SESSION_ID, snId);
				errRspEvent.setAttr(OlsProtocol.PARAM_REASON, "未指定事件类型(" + PARAM_EVENT_TYPE + ")");
				pusher.push(errRspEvent);
				return;
			}
			event = OlsEvent.newEvent(eventSource, eventType, mapContext);
		} catch (Throwable t) {
			logger.error("Pushlet: Error creating event in doPost()", t);
			//
			OlsEvent errRspEvent = OlsEvent.newEvent(eventSource, OlsProtocol.EVENT_NACK, null);
			errRspEvent.setAttr(OlsProtocol.PARAM_SESSION_ID, snId);
			errRspEvent.setAttr(OlsProtocol.PARAM_REASON, "请求处理失败");
			pusher.push(errRspEvent);
			return;
		}
		//
		doRequest(event, request, response, pusher);
	}

	protected void doRequest(OlsEvent event, HttpServletRequest request, HttpServletResponse response, OlsHttpAdapter pusher) throws IOException {
		String eventSource = event.getSource();
		String eventType = event.getType();
		String snId = String.valueOf(event.getAttr(PARAM_SESSION_ID));
		try {
			OlsSession session = null;
			if (eventType.equalsIgnoreCase(EVENT_START)) {
				if (OlsProtocol.SOURCE_CUSTOMER.equalsIgnoreCase(eventSource)) {
					String customerId = String.valueOf(event.getAttr(OlsProtocol.PARAM_CUSTOMER_ID));
					if (StrUtil.isNullOrBlank(customerId)) {
						logger.warn("Pushlet: bad request, no " + OlsProtocol.PARAM_CUSTOMER_ID + " specified event = " + eventType);
						//
						OlsEvent errRspEvent = OlsEvent.newEvent(eventSource, OlsProtocol.EVENT_FACK, null);
						errRspEvent.setAttr(OlsProtocol.PARAM_SESSION_ID, snId);
						errRspEvent.setAttr(OlsProtocol.PARAM_REASON, "未指定" + OlsProtocol.PARAM_CUSTOMER_ID);
						pusher.push(errRspEvent);
						return;
					}
				} else {
					String servantId = String.valueOf(event.getAttr(OlsProtocol.PARAM_SERVANT_ID));
					if (StrUtil.isNullOrBlank(servantId)) {
						logger.warn("Pushlet: bad request, no " + PARAM_SERVANT_ID + " specified event = " + eventType);
						//
						OlsEvent errRspEvent = OlsEvent.newEvent(eventSource, OlsProtocol.EVENT_FACK, null);
						errRspEvent.setAttr(OlsProtocol.PARAM_SESSION_ID, snId);
						errRspEvent.setAttr(OlsProtocol.PARAM_REASON, "未指定" + OlsProtocol.PARAM_SERVANT_ID);
						pusher.push(errRspEvent);
						return;
					}
				}
				session = OlsSessionManager.createSession(event);
			} else {
				if (StrUtil.isNullOrBlank(snId)) {
					logger.warn("Pushlet: bad request, no " + PARAM_SESSION_ID + " specified event = " + eventType);
					//
					OlsEvent errRspEvent = OlsEvent.newEvent(eventSource, OlsProtocol.EVENT_FACK, null);
					errRspEvent.setAttr(OlsProtocol.PARAM_SESSION_ID, snId);
					errRspEvent.setAttr(OlsProtocol.PARAM_REASON, "未指定" + OlsProtocol.PARAM_SESSION_ID);
					pusher.push(errRspEvent);
					return;
				}
				int sessionType = OlsSession.typeBySource(eventSource);
				session = OlsSessionManager.getInstance().getSession(sessionType, snId);
				if (session == null) {
					logger.warn("Pushlet:  bad request, no session found snId = " + snId + " event = " + eventType);
					//
					OlsEvent errRspEvent = OlsEvent.newEvent(eventSource, OlsProtocol.EVENT_TOACK, null);
					errRspEvent.setAttr(OlsProtocol.PARAM_SESSION_ID, snId);
					errRspEvent.setAttr(OlsProtocol.PARAM_REASON, "指定的会话无效或已过期");
					pusher.push(errRspEvent);
					return;
				}
			}
			OlsCommand command = OlsCommand.newOne(session, event, request, response);
			OlsController.getInstance().doCommand(command);
		} catch (Throwable t) {
			logger.error("Pushlet: Error in doRequest()", t);
			//
			OlsEvent errRspEvent = OlsEvent.newEvent(eventSource, OlsProtocol.EVENT_NACK, null);
			errRspEvent.setAttr(OlsProtocol.PARAM_SESSION_ID, snId);
			errRspEvent.setAttr(OlsProtocol.PARAM_REASON, "服务器内部错误");
			pusher.push(errRspEvent);
		}
	}
}
