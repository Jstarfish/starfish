package priv.starfish.common.xload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import priv.starfish.common.helper.FileHelper;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.util.BoolUtil;
import priv.starfish.common.util.ExceptionUtil;
import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.StrUtil;

public class FileDownloader {
	// 单例对象
	private static final FileDownloader instance = new FileDownloader();

	public static FileDownloader getInstance() {
		return instance;
	}

	FileProviderMgr fileProviderMgr = null;

	public FileProviderMgr getFileProviderMgr() {
		return fileProviderMgr;
	}

	public void setFileProviderMgr(FileProviderMgr fileProviderMgr) {
		this.fileProviderMgr = fileProviderMgr;
	}

	// 强制下载文本标记参数名
	static final String KEY_DOWNLOAD_TEXT = "downloadText";
	// 强制下载的文本MIME
	static final String DEF_DOWNLOAD_TEXT_CONTENT_TYPE = "text/x-download";
	// 默认宿主（下载）页面接收下载失败消息的函数名称
	static final String KEY_FAIL_MSG_CALLBACK = "failMsgCallback";
	static final String DEF_FAIL_MSG_CALLBACK = "showFailDownloadMsg";

	public void download(HttpServletRequest request, HttpServletResponse response) {
		// 获取接收下载失败消息的函数名称
		String failMsgCallback = request.getParameter(KEY_FAIL_MSG_CALLBACK);
		if (StrUtil.isNullOrBlank(failMsgCallback)) {
			// 默认宿主页面的接收上传消息的函数
			failMsgCallback = DEF_FAIL_MSG_CALLBACK;
		}
		// 提取参数配置
		HashMap<String, Object> paramsMap = new LinkedHashMap<String, Object>();
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			paramsMap.put(paramName, request.getParameter(paramName));
		}
		//
		Result<Map<String, Object>> result = Result.newOne();
		result.data = paramsMap;
		//
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			//
			FileProvider fileProvider = this.fileProviderMgr.pick(paramsMap);
			//
			if (fileProvider == null) {
				result.type = Type.error;
				result.message = "没有找到匹配的文件提供者！";
			} else {
				// ======================================================
				FileResult fileResult = fileProvider.provideFile(paramsMap);
				// ======================================================
				if (fileResult == null) {
					result.type = Type.error;
					result.message = "下载失败，文件提供者[" + fileProvider.getClass().getName() + "]没有给出任何信息！";
				} else {
					inputStream = fileResult.getInputStream();
					if (inputStream == null) {
						result.type = Type.error;
						result.message = fileResult.getMessage();
						if (StrUtil.isNullOrBlank(result.message)) {
							result.message = "没有得到文件内容，文件提供者[" + fileProvider.getClass().getName() + "]没有给出任何信息！";
						}
					} else {
						String fileName = fileResult.getFileName();
						if (StrUtil.isNullOrEmpty(fileName)) {
							fileName = "未命名";
						}
						String contentType = FileHelper.getContentType(fileName);
						if (contentType.startsWith("text/")) {
							if (BoolUtil.isTrue(String.valueOf(paramsMap.get(KEY_DOWNLOAD_TEXT)))) {
								// 强制下载文本文件
								contentType = DEF_DOWNLOAD_TEXT_CONTENT_TYPE;
							}
						}
						fileName = FileHelper.replaceFileNameBlanks(fileName);
						fileName = URLEncoder.encode(fileName, "UTF-8");
						response.setContentType(contentType);
						response.setHeader("Content-disposition", "attachment;filename=" + fileName);
						outputStream = response.getOutputStream();
						IOUtils.copy(inputStream, outputStream);
					}
				}
			}
		} catch (Exception e) {
			result.type = Type.error;
			result.message = ExceptionUtil.extractMsg(e);
		} finally {
			if (!result.type.equals(Type.info)) {
				writerToClient(response, result, failMsgCallback);
			}
			if (inputStream != null) {
				IOUtils.closeQuietly(inputStream);
			}
			if (outputStream != null) {
				IOUtils.closeQuietly(outputStream);
			}
		}
	}

	public static void writerToClient(HttpServletResponse response, Result<Map<String, Object>> result, String failMsgCallback) {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		StringBuffer sb = new StringBuffer();
		sb.append("<script type='text/javascript' >");
		sb.append("var downloadFailInfo = " + JsonUtil.toJson(result) + ";");
		sb.append("var downloadWin = window.parent;");
		sb.append("if(downloadWin != window) {");
		sb.append("    downloadWin = downloadWin.window;");
		sb.append("    var failMsgCallback = downloadWin['" + failMsgCallback + "'];");
		sb.append("    if( typeof failMsgCallback == 'function') {");
		sb.append("        failMsgCallback(downloadFailInfo);");
		sb.append("    }");
		sb.append("}");
		sb.append("</script>");
		try {
			response.getWriter().write(sb.toString());
			response.flushBuffer();
		} catch (IOException e) {
		}
	}
}
