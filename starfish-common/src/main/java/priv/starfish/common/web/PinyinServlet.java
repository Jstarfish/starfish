package priv.starfish.common.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import priv.starfish.common.model.Result;
import priv.starfish.common.util.BoolUtil;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.WebUtil;

/**
 * Servlet implementation class PinyinServlet 汉字转拼音
 */
@WebServlet(name = "pinyinServlet", value = "/xutil/pinyin.do")
public class PinyinServlet extends HttpServlet {
	protected final Log logger = LogFactory.getLog(this.getClass());
	//
	private static final long serialVersionUID = 1L;
	private static final String PARAM_ChsStr = "chsStr";
	private static final String PARAM_AsJianpin = "asJianpin";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding(WebUtil.DEFAULT_ENCODING);
		response.setCharacterEncoding(WebUtil.DEFAULT_ENCODING);
		response.setHeader("Access-Control-Allow-Origin", "*");
		//
		String chsStr = request.getParameter(PARAM_ChsStr);
		Boolean jianpin = BoolUtil.isTrue(request.getParameter(PARAM_AsJianpin));
		Result<String> result = Result.newOne();
		result.data = StrUtil.chsToPinyin(chsStr, jianpin);

		WebUtil.responseAsJson(response, result);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		throw new ServletException("Post not supported !");
	}

}
