package priv.starfish.common.webols;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 包装 Pushlet 请求/响应 数据
 * 
 * @author koqiui
 * @date 2015年6月21日 下午5:34:59
 *
 */
public class OlsCommand implements OlsProtocol {
	private OlsSession session;
	private OlsEvent reqEvent;
	private HttpServletRequest httpReq;
	private OlsHttpAdapter httpAdapter;

	public OlsSession getSession() {
		return session;
	}

	public OlsEvent getReqEvent() {
		return reqEvent;
	}

	public void setReqEvent(OlsEvent reqEvent) {
		this.reqEvent = reqEvent;
	}

	public HttpServletRequest getHttpReq() {
		return httpReq;
	}

	public void setHttpReq(HttpServletRequest httpReq) {
		this.httpReq = httpReq;
	}

	public OlsHttpAdapter getHttpAdapter() {
		return httpAdapter;
	}

	private OlsCommand(OlsSession session, OlsEvent reqEvent, HttpServletRequest httpReq, HttpServletResponse httpRsp) {
		this.session = session;
		this.reqEvent = reqEvent;
		this.httpReq = httpReq;
		//
		this.httpAdapter = OlsHttpAdapter.newOne(httpRsp);
	}

	public static OlsCommand newOne(OlsSession session, OlsEvent reqEvent, HttpServletRequest httpReq, HttpServletResponse httpRsp) {
		return new OlsCommand(session, reqEvent, httpReq, httpRsp);
	}
}
