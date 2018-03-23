package priv.starfish.common.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import priv.starfish.common.base.AppNodeInfo;
import priv.starfish.common.exception.UnAuthenticatedException;
import priv.starfish.common.exception.UnAuthorizedException;
import priv.starfish.common.exception.UnBoundOuterUserException;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.model.ResultCode;
import priv.starfish.common.util.ExceptionUtil;
import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.WebUtil;

public class WebExceptionResolver extends AbstractHandlerExceptionResolver {
	public static final String ERROR_INFO_KEY = "error.info";
	public static final String DEFAULT_ERROR_URL = "/error/error";
	public static final String REDIRECT_URL_KEY = "redirect.url";
	public static final String LOGIN_URL = "";
	public static final int DEFAULT_STATUS_CODE = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
	//
	private String errorInfoKey = ERROR_INFO_KEY;
	private String defaultErrorUrl = DEFAULT_ERROR_URL;
	private String redirectUrlKey = REDIRECT_URL_KEY;
	private String loginUrl = LOGIN_URL;
	private Map<Class<?>, String> errorViewMapping;
	private int defaultStatusCode;
	private Map<Integer, String> statusCodeViewMapping;

	public String getErrorInfoKey() {
		return errorInfoKey;
	}

	public void setErrorInfoKey(String errorInfoKey) {
		if (StrUtil.hasText(errorInfoKey)) {
			this.errorInfoKey = errorInfoKey;
		}
	}

	public String getDefaultErrorUrl() {
		return defaultErrorUrl;
	}

	public void setDefaultErrorUrl(String defaultErrorUrl) {
		if (StrUtil.hasText(defaultErrorUrl)) {
			this.defaultErrorUrl = defaultErrorUrl;
		}
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getRedirectUrlKey() {
		return redirectUrlKey;
	}

	public void setRedirectUrlKey(String redirectUrlKey) {
		this.redirectUrlKey = redirectUrlKey;
	}

	public Map<Class<?>, String> getErrorViewMapping() {
		return errorViewMapping;
	}

	public void setErrorViewMapping(Map<Class<?>, String> errorViewMapping) {
		this.errorViewMapping = errorViewMapping;
	}

	public int getDefaultStatusCode() {
		return defaultStatusCode;
	}

	public void setDefaultStatusCode(int defaultStatusCode) {
		this.defaultStatusCode = defaultStatusCode;
	}

	public Map<Integer, String> getStatusCodeViewMapping() {
		return statusCodeViewMapping;
	}

	public void setStatusCodeViewMapping(Map<Integer, String> statusCodeViewMapping) {
		this.statusCodeViewMapping = statusCodeViewMapping;
	}

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		String contentType = request.getHeader("Accept");
		if (contentType.contains("/json")) {
			contentType = "json";
		} else if (contentType.contains("/html")) {
			contentType = "html";
		} else {
			contentType = "*/*";
		}
		//todo Spring V4.1以后的版本在不支持Servlet3.0的应用服务器上
		//int statusCode = response.getStatus();
		int statusCode = 200;
		logger.info(">>  异常发生时 statusCode ：" + statusCode);
		//
		//
		if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
			response.setStatus(this.defaultStatusCode);
		}
		//
		Result<String> result = Result.newOne();
		result.type = Type.error;
		result.message = ExceptionUtil.extractMsg(ex);
		this.logger.error(ex);
		if (!AppNodeInfo.getCurrent().isRunningOnProductionServer()) {
			String contextPath = request.getContextPath();
			String requestUri = request.getRequestURI();
			String relativeUri = requestUri.substring(contextPath.length());
			result.data = relativeUri;
		}

		this.logger.error(result);
		//
		if (ex instanceof UnAuthenticatedException) {
			result.code = ResultCode.Auth.Authenticate_Fail;
			response.setStatus(HttpStatus.SC_UNAUTHORIZED);
		} else if (ex instanceof UnBoundOuterUserException) {
			result.code = ResultCode.Auth.AnthenBinding_Fail;
			response.setStatus(HttpStatus.SC_UNAUTHORIZED);
		} else if (ex instanceof UnAuthorizedException) {
			result.code = ResultCode.Auth.Authorize_Fail;
			response.setStatus(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
		}
		//
		if ("json".equals(contentType)) {
			try {
				PrintWriter writer = WebUtil.responseAsJson(response, result);
				writer.close();
			} catch (IOException e) {
				logger.error("输出json错误信息失败", e);
			}
			return null;
		} else {
			String errorJson = JsonUtil.toJson(result);
			ModelAndView mv = new ModelAndView();
			if (result.code == ResultCode.Auth.Authenticate_Fail) {
				String redirectUrl = WebUtil.getRequestUriWithParams(request, false);
				request.getSession().setAttribute(this.redirectUrlKey, redirectUrl);
				//
				if (StrUtil.hasText(this.loginUrl)) {
					mv.setViewName("redirect:" + this.loginUrl);
				} else {
					mv.setViewName(this.defaultErrorUrl);
					mv.addObject(this.errorInfoKey, errorJson);
				}
			} else {
				mv.setViewName(this.defaultErrorUrl);
				mv.addObject(this.errorInfoKey, errorJson);
			}
			return mv;
		}
	}

}
