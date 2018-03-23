package priv.starfish.common.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import priv.starfish.common.model.Result;
import priv.starfish.common.util.BoolUtil;
import priv.starfish.common.util.DateUtil;
import priv.starfish.common.util.WebUtil;

@WebServlet(name = "serverTimeServlet", value = "/xutil/serverTime.do")
public class ServerTimeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//
			response.setHeader("Access-Control-Allow-Origin", "*");
			// 是否作为数值
			Boolean asNum = BoolUtil.isTrue(request.getParameter("asNum"));
			// 是否作为时间戳
			Boolean asTs = BoolUtil.isTrue(request.getParameter("asTs"));
			//
			Result<Object> result = Result.newOne();
			result.data = asNum ? System.currentTimeMillis() : (asTs ? DateUtil.toStdTimestampStr(new Date()) : DateUtil.toStdDateTimeStr(new Date()));
			PrintWriter writer = WebUtil.responseAsJson(response, result);
			//
			writer.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
