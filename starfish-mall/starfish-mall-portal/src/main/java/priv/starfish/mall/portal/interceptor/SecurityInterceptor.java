package priv.starfish.mall.portal.interceptor;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import priv.starfish.common.exception.UnAuthenticatedException;
import priv.starfish.common.exception.UnBoundOuterUserException;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.user.UserContextHolder;
import priv.starfish.common.util.WebUtil;
import priv.starfish.mall.web.base.AppBase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * 登录、鉴权拦截器
 * 
 * @author koqiui
 * @date 2015年5月29日 下午2:53:19 TODO
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {
	protected static UserContext getUserContext(HttpSession session) {
		UserContext userContext = WebUtil.getSessionAttribute(session, UserContextHolder.SESSION_KEY_USER_CONTEXT);
		if (userContext == null) {
			userContext = UserContext.newOne();
			WebUtil.setSessionAttribute(session, UserContextHolder.SESSION_KEY_USER_CONTEXT, userContext);
		}
		return userContext;
	}

	protected static UserContext getUserContext(HttpServletRequest request) {
		return getUserContext(request.getSession(true));
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// HttpSessionManager sessionManager = (HttpSessionManager) request.getAttribute(HttpSessionManager.class.getName());
		// SessionRepository<? extends Session> sessionRepository = (SessionRepository<? extends Session>) request.getAttribute(SessionRepositoryFilter.SESSION_REPOSITORY_ATTR);
		boolean result = true;
		UserContext userContext = getUserContext(request);
		if (!userContext.isSysUser()) {
			if (userContext.isOutUser()) {
				throw new UnBoundOuterUserException("未绑定系统用户");
			} else {
				if (request.getAttribute(AppBase.REQUEST_ALTOKEN_SET_FLAG_NAME) == null) {
					throw new UnAuthenticatedException("未登录或未注册");
				}
			}
		}
		return result;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
		// System.out.println("postHandle >> " + request.getRequestURI());
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		super.afterCompletion(request, response, handler, ex);
		// System.out.println("afterCompletion >> " + request.getRequestURI());
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		super.afterConcurrentHandlingStarted(request, response, handler);
		// System.out.println("并发处理开始了 >> " + request.getRequestURI());
	}

}
