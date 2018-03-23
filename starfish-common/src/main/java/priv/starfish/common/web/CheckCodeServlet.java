package priv.starfish.common.web;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import priv.starfish.common.util.CaptchaUtil;
import priv.starfish.common.util.StrUtil;

/**
 * Servlet implementation class CheckCodeServlet 图形验证码
 */
@WebServlet(name = "checkCodeServlet", value = "/xutil/checkCode.do")
public class CheckCodeServlet extends HttpServlet {
	//private final Log logger = LogFactory.getLog(this.getClass());
	//
	private static final long serialVersionUID = 1L;

	private static final String IMAGE_TYPE = "JPEG";
	private static final String MIME_TYPE = "image/jpeg";
	private static final String CODE_NAME_KEY = "name";

	/*
	 * 配置基本的头属性
	 */
	private void confBaseHeader(HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType(MIME_TYPE);
	}

	public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String codeName = request.getParameter(CODE_NAME_KEY);
		if (StrUtil.hasText(codeName)) {
			codeName = codeName.trim();
		} else {
			codeName = null;
		}
		//
		// 配置头文件属性
		confBaseHeader(response);
		// 生产验证码信息
		String captchaText = CaptchaUtil.createText();
		// save in session
		if (codeName == null) {
			CheckCodeHelper.saveCode(request.getSession(true), captchaText);
		} else {
			CheckCodeHelper.saveCode(request.getSession(true), codeName, captchaText);
		}
		//
		BufferedImage captchaImage = CaptchaUtil.createImage(captchaText);
		OutputStream os = response.getOutputStream();
		ImageIO.write(captchaImage, IMAGE_TYPE, os);
		closeStream(os);
	}

	/*
	 * 关闭流信息
	 */
	private void closeStream(OutputStream os) throws IOException {
		os.flush();
		os.close();
		os = null;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		createCode(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		throw new ServletException("Post not supported !");
	}

}
