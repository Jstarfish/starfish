package priv.starfish.common.web;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.google.zxing.WriterException;
import priv.starfish.common.qrcode.UrlQrCodecoder;
import priv.starfish.common.util.StrUtil;

/**
 * Servlet implementation class QrCodeServlet 二维码图形
 */
@WebServlet(name = "qrCodeServlet", value = "/xutil/qrCode.do")
public class QrCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String IMAGE_TYPE = "PNG";
	private static final String MIME_TYPE = "image/png";
	private static final int EXPIRES_MINUTES = 3600 * 24 * 7;
	//
	private static int DEFAULT_WIDTH = 280;
	private static int DEFAULT_HEIGHT = 280;

	/*
	 * 配置基本的头属性
	 */
	private void confBaseHeader(HttpServletResponse response) {
		response.setHeader("Cache-Control", "Max-Age=" + EXPIRES_MINUTES);
		response.setContentType(MIME_TYPE);
	}

	public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String url = request.getParameter("url");
		String widthStr = request.getParameter("width");
		String heightStr = request.getParameter("height");
		// 头像
		// String icon = request.getParameter("icon");
		if (StrUtil.hasText(url)) {
			// 配置头文件属性
			confBaseHeader(response);
			//
			int width = DEFAULT_WIDTH;
			if (StrUtil.hasText(widthStr)) {
				width = Integer.parseInt(widthStr);
			}
			int height = DEFAULT_HEIGHT;
			if (StrUtil.hasText(heightStr)) {
				height = Integer.parseInt(heightStr);
			}
			//
			OutputStream os = response.getOutputStream();
			try {
				UrlQrCodecoder.encodeToStream(url, width, height, IMAGE_TYPE, os);
			} catch (WriterException e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(os);
			}
		} else {
			IOUtils.closeQuietly(response.getOutputStream());
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		createCode(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		throw new ServletException("Post not supported !");
	}

}
