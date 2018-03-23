package priv.starfish.mall.portal.interceptor;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.user.UserContextHolder;
import priv.starfish.common.util.BoolUtil;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.WebUtil;
import priv.starfish.mall.comn.dict.Gender;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.member.entity.Member;
import priv.starfish.mall.service.MemberService;
import priv.starfish.mall.service.UserService;
import priv.starfish.mall.web.base.AlToken;
import priv.starfish.mall.web.base.AlTokenHelper;
import priv.starfish.mall.web.base.AppBase;
import priv.starfish.mall.web.base.CartHelper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;


/**
 * Cookie拦截器
 * 
 * @author koqiui
 * @date 2015年5月29日 下午2:53:19
 */
public class CookieInterceptor extends HandlerInterceptorAdapter {
	@javax.annotation.Resource
	UserService userService;
	@javax.annotation.Resource
	MemberService memberService;

	protected static UserContext getUserContext(HttpSession session) {
		UserContext userContext = WebUtil.getSessionAttribute(session, UserContextHolder.SESSION_KEY_USER_CONTEXT);
		if (userContext == null) {
			userContext = UserContext.newOne();
			WebUtil.setSessionAttribute(session, UserContextHolder.SESSION_KEY_USER_CONTEXT, userContext);
		}
		return userContext;
	}

	protected static void setUserContext(HttpSession session, UserContext userContext) {
		WebUtil.setSessionAttribute(session, UserContextHolder.SESSION_KEY_USER_CONTEXT, userContext);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		boolean result = true;
		//
		HttpSession session = request.getSession(true);
		//
		UserContext userContext = getUserContext(session);
		if (!userContext.isSysUser()) {
			// 未登录，则检查自动登录cookie
			Cookie alCookie = WebUtil.getCookie(request, AppBase.COOKIE_ALTOKEN_NAME);
			if (alCookie != null) {
				String tokenEncoded = alCookie.getValue();
				if (StrUtil.hasText(tokenEncoded)) {
					AlToken givenToken = AlToken.parse(tokenEncoded);
					if (givenToken != null) {
						Integer userId = givenToken.userId;
						String appType = givenToken.appType;
						AlToken savedToken = AlTokenHelper.getUserAlToken(userId, appType);
						if (savedToken != null && savedToken.validate(givenToken)) {
							User user = userService.getUserById(userId);
							if (user != null) {
								Member member = memberService.getMemberById(userId);
								if (member == null || !BoolUtil.isTrue(member.getDisabled())) {
									userContext.setUserId(userId);
									userContext.setPhoneNo(user.getPhoneNo());
									userContext.setUserName(user.getNickName());
									Gender gender = user.getGender();
									if (gender == null) {
										gender = Gender.X;
									}
									userContext.setGender(gender.name());
									//
									setUserContext(session, userContext);
									// 更新alToken
									long createTimeTs = savedToken.createTs;
									long expireTimeTs = createTimeTs + AlToken.MAX_AGE_IN_SECONDS * 1000;
									int leftSeconds = (int) Math.ceil((expireTimeTs - new Date().getTime()) / 1000);
									//
									tokenEncoded = AlTokenHelper.setUserAlToken(userId, appType, session.getId(), new Date(createTimeTs));
									//
									Cookie alTokenCookie = new Cookie(AppBase.COOKIE_ALTOKEN_NAME, tokenEncoded);
									alTokenCookie.setMaxAge(leftSeconds);
									alTokenCookie.setPath("/");
									response.addCookie(alTokenCookie);
									//
									response.setHeader(AppBase.HEADER_ALTOKEN_NAME, tokenEncoded);
									// response.setHeader(AppBase.HEADER_SESSION_NAME, session.getId());
									// 告诉后续安全拦截器
									request.setAttribute(AppBase.REQUEST_ALTOKEN_SET_FLAG_NAME, true);

									// 同步session购物车到Ceche中
									CartHelper.mergeCenterCacheCart(session);
								}
							}
						}
					}
				}
			}
		}
		//
		return result;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		super.afterConcurrentHandlingStarted(request, response, handler);
	}

}
