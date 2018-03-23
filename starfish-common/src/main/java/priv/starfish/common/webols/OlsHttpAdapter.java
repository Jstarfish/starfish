package priv.starfish.common.webols;

import priv.starfish.common.util.WebUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * http 客户端适配器
 * 
 * @author koqiui
 * @date 2015年6月21日 下午5:38:33
 *
 */
public class OlsHttpAdapter {
	private HttpServletResponse httpRsp;

	private OlsHttpAdapter(HttpServletResponse httpRsp) {
		this.httpRsp = httpRsp;
	}

	public static OlsHttpAdapter newOne(HttpServletResponse httpRsp) {
		return new OlsHttpAdapter(httpRsp);
	}

	public void push(OlsEvent event) throws IOException {
		WebUtil.responseAsJson(httpRsp, event);
	}

}
