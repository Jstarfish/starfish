package priv.starfish.mall.portal.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.*;
import priv.starfish.common.web.CheckCodeHelper;
import priv.starfish.mall.cart.dto.SaleCartInfo;
import priv.starfish.mall.comn.dict.Gender;
import priv.starfish.mall.comn.dict.VerfAspect;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.comn.entity.UserLinkWay;
import priv.starfish.mall.comn.entity.UserVerfStatus;
import priv.starfish.mall.comn.misc.BizParamInfo;
import priv.starfish.mall.dao.notify.MailServerDao;
import priv.starfish.mall.market.entity.UserCoupon;
import priv.starfish.mall.market.entity.UserSvcCoupon;
import priv.starfish.mall.member.entity.Member;
import priv.starfish.mall.notify.dict.SmsUsage;
import priv.starfish.mall.notify.entity.SmsVerfCode;
import priv.starfish.mall.order.dto.SaleOrderInfo;
import priv.starfish.mall.service.*;
import priv.starfish.mall.web.base.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
	@Resource
	UserService userService;
	@Resource
	private MemberService memberService;
	@Resource
	SettingService settingService;
	@Resource
	MailServerDao mailServerDao;

	@Resource
	private SalePrmtService salePrmtService;

	/**
	 * 返回登录页面
	 * 
	 * @author 廖晓远
	 * @date 2015年7月8日 上午10:03:54
	 * 
	 * @return
	 * @throws IOException
	 */
	@Remark("登录页面")
	@RequestMapping(value = "/login/jsp", method = RequestMethod.GET)
	public String toLoginJsp() {
		return "user/login";
	}

	/**
	 * 返回注册页面
	 * 
	 * @author 郝江奎
	 * @date 2015年9月17日 上午10:07:20
	 * 
	 * @return
	 * @throws IOException
	 */
	@Remark("注册页面")
	@RequestMapping(value = "/regist/jsp", method = RequestMethod.GET)
	public String toRegJsp() {
		return "user/regist";
	}

	/**
	 * 返回当前用户是否已登录
	 * 
	 * @author koqiui
	 * @date 2015年12月11日 下午11:49:43
	 * 
	 * @param session
	 * @return
	 */
	@Remark("返回当前用户是否已登录")
	@RequestMapping(value = "/has/loggedin/get", method = RequestMethod.GET)
	@ResponseBody
	public Result<Boolean> hasUserLoggedIn(HttpSession session) {
		UserContext userContext = getUserContext(session);
		//
		Result<Boolean> result = Result.newOne();
		result.data = userContext.isSysUser();
		//
		return result;
	}

	/**
	 * 获取当前登录用户信息
	 * 
	 * @author 郝江奎
	 * @date 2015年9月14日 上午17:19:23
	 * 
	 * @param request
	 * @return
	 */
	@Remark("返回当前用户信息")
	@RequestMapping(value = "/current/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<String, Object>> getCurrentUser(HttpServletRequest request) {
		UserContext userContext = getUserContext(request);
		//
		Result<Map<String, Object>> result = Result.newOne();
		if (userContext.isSysUser()) {
			result.type = Type.info;
			//
			Map<String, Object> userInfo = new HashMap<String, Object>();
			userInfo.put("id", userContext.getUserId());
			userInfo.put("nickName", userContext.getUserName());
			userInfo.put("phoneNo", userContext.getPhoneNo());
			userInfo.put("gender", userContext.getGender());
			//
			result.data = userInfo;
		} else if (userContext.isAuthenticated()) {
			result.type = Type.warn;
			result.message = "用户信息未绑定";
		} else {
			result.type = Type.warn;
			result.message = "用户未登录或未注册";
		}
		return result;
	}

	/**
	 * 修改密码验证
	 * 
	 * @author 郝江奎
	 * @date 2015年11月6日 下午14:04:37
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@Remark("修改密码验证")
	@RequestMapping(value = "/logPass/check/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<Boolean> doCheck(HttpServletRequest request, HttpSession session, @RequestBody MapContext requestData) throws IOException {
		Result<Boolean> result = Result.newOne();
		//
		String phoneNo = requestData.getTypedValue("phoneNo", String.class);
		String chkCode = requestData.getTypedValue("chkCode", String.class);
		String smsCode = requestData.getTypedValue("smsCode", String.class);
		if (CheckCodeHelper.isValidCode(session, chkCode)) {
			User user = userService.getUserByPhoneNo(phoneNo);
			if (user == null) {
				user = userService.getUserByEmail(phoneNo);
			}
			if (user != null) {
				SmsVerfCode smsVerfCode = new SmsVerfCode();
				smsVerfCode.setPhoneNo(phoneNo);
				smsVerfCode.setVfCode(smsCode);
				smsVerfCode.setUsage(SmsUsage.logPass);
				if (settingService.validSmsVerfCode(smsVerfCode)) {
					UserContext userContext = getUserContext(request);
					userContext.setPhoneNo(user.getPhoneNo());
					//
					setUserContext(request, userContext);
					// 修改验证码记录（已使用）
					settingService.updateSmsVerfCode(smsVerfCode);
				} else {
					result.type = Type.warn;
					result.message = "手机验证码错误";
					result.code = 11;
				}
			} else {
				result.type = Type.warn;
				result.message = "手机号码不存在";
				result.code = 12;
			}

		} else {
			result.type = Type.warn;
			result.message = "验证码错误";
			result.code = 21;
		}
		return result;
	}

	/**
	 * 登录
	 * 
	 * @author 廖晓远
	 * @date 2015年7月8日 上午10:04:37
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@Remark("登录")
	@RequestMapping(value = "/login/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<String> doLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam(value = "appType") String appType, @RequestBody MapContext requestData) throws IOException {
		Result<String> result = Result.newOne();
		//
		String phoneNo = requestData.getTypedValue("phoneNo", String.class);
		String password = requestData.getTypedValue("password", String.class);
		String chkCode = requestData.getTypedValue("chkCode", String.class);
		Boolean autoLoginFlag = requestData.getTypedValue("alFlag", Boolean.class);
		if (autoLoginFlag == null) {
			autoLoginFlag = Boolean.FALSE;
		}
		//
		if (!CheckCodeHelper.isValidCode(session, chkCode)) {
			result.type = Type.warn;
			result.message = "验证码错误，请输入正确的验证码";
			return result;
		}
		// 默认错误次数
		Integer maxFaileTimes = BizParamInfo.loginFailLockCount;
		User user = userService.getUserByPhoneNo(phoneNo);
		//
		boolean shouldTryNext = false;
		//
		Date now = new Date();
		if (user != null) {
			// 获取会员信息
			Member member = memberService.getMemberById(user.getId());
			// 检查锁定状态
			if (user.getLocked()) {
				Date lockTime = user.getLockTime();
				Date unlockTime = DateUtil.addDays(lockTime, 1);
				if (unlockTime.getTime() < now.getTime()) {
					// 自动解锁
					user.setLocked(false);
					user.setLockTime(null);
					user.setFailTime(null);
					user.setFailCount(0);
					userService.updateUser(user);
					//
					shouldTryNext = true;
				} else {
					result.type = Type.warn;
					result.message = "您的账户已锁定，请在（24小时后）自动解锁后重试";
				}
			} else {
				if (!userService.checkUserPasswordSet(user.getId())) {
					result.type = Type.warn;
					result.code = 10;
					result.message = "您的登录密码未初始化！";
				} else if (member != null && member.getDisabled()) {
					result.type = Type.warn;
					result.message = "您的账户已被禁用，请联系商城客服！";
				} else {
					shouldTryNext = true;
				}
			}
		} else {
			result.type = Type.warn;
			result.message = "此手机号码关联用户不存在";
		}
		if (shouldTryNext) {
			String encrypted = user.getPassword();
			String saltStr = user.getSalt();
			password = RSACrypter.decryptStringFromJs(password);
			String loginEncrypted = PasswordUtil.encrypt(password, saltStr);
			if (encrypted.equals(loginEncrypted)) {
				// 清除失败/锁定信息
				user.setLocked(false);
				user.setLockTime(null);
				user.setFailTime(null);
				user.setFailCount(0);
				userService.updateUser(user);
				//
				UserContext userContext = getUserContext(request);
				userContext.setUserId(user.getId());
				userContext.setPhoneNo(user.getPhoneNo());
				userContext.setUserName(user.getNickName());
				Gender gender = user.getGender();
				if (gender == null) {
					gender = Gender.X;
				}
				userContext.setGender(gender.name());
				//
				setUserContext(request, userContext);
				//
				if (autoLoginFlag) {
					// 处理自动登录
					String tokenEncoded = AlTokenHelper.setUserAlToken(user.getId(), appType, session.getId());
					//
					Cookie alTokenCookie = new Cookie(AppBase.COOKIE_ALTOKEN_NAME, tokenEncoded);
					alTokenCookie.setMaxAge(AlToken.MAX_AGE_IN_SECONDS);
					alTokenCookie.setPath("/");
					response.addCookie(alTokenCookie);
					//
					response.setHeader(AppBase.HEADER_ALTOKEN_NAME, tokenEncoded);
					// response.setHeader(AppBase.HEADER_SESSION_NAME, session.getId());
				} else {
					WebUtil.removeCookie(response, AppBase.COOKIE_ALTOKEN_NAME, "/");
					AlTokenHelper.removeUserAlToken(user.getId(), appType);
				}
				//
				// 同步session购物车到Ceche中
				CartHelper.mergeCenterCacheCart(session);
				//
				result.message = "登录信息已通过验证";
				// 检查页面重定向
				String redirectUrl = (String) session.getAttribute(AppBase.SESSION_KEY_REDIRECT_URL);
				if (StrUtil.hasText(redirectUrl)) {
					result.data = redirectUrl;
					//
					session.removeAttribute(AppBase.SESSION_KEY_REDIRECT_URL);
				} else {
					result.data = "/";
				}
			} else {
				Integer faileTimes = 1;
				if (user.getFailCount() != null) {
					faileTimes = user.getFailCount() + 1;
				}
				// 判断错误次数
				if (faileTimes >= maxFaileTimes) {
					user.setLockTime(now);
					user.setLocked(true);
					user.setFailTime(now);
					user.setFailCount(faileTimes);

					result.type = Type.warn;
					result.message = "失败次数已经达到" + maxFaileTimes + "次，账户已被锁定！";
				} else {
					user.setFailTime(now);
					user.setFailCount(faileTimes);

					result.type = Type.warn;
					if (faileTimes == maxFaileTimes - 1) {
						result.message = "你还有一次机会，再次失败，您的账户将被锁定！";
					} else {
						result.message = "密码有误";
					}

				}
				//
				userService.updateUser(user);
			}
		}
		return result;
	}

	/**
	 * 注册会员
	 * 
	 * @author 郝江奎
	 * @date 2015年9月18日 上午10:11:46
	 * 
	 * @param response
	 * @return Result<Integer> 返回新注册会员的id
	 */
	@Remark("注册会员")
	@RequestMapping(value = "/regist/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> saveMember(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam(value = "appType") String appType, @RequestBody MapContext requestData) {
		Result<String> result = Result.newOne();
		// 获取数据
		String nickName = requestData.getTypedValue("nickName", String.class);
		String password = requestData.getTypedValue("password", String.class);
		String phoneNo = requestData.getTypedValue("phoneNo", String.class);
		String smsCode = requestData.getTypedValue("smsCode", String.class);
		String chkCode = requestData.getTypedValue("chkCode", String.class);
		Integer secureLevel = requestData.getTypedValue("secureLevel", Integer.class);
		Boolean autoLoginFlag = requestData.getTypedValue("alFlag", Boolean.class);
		if (autoLoginFlag == null) {
			autoLoginFlag = Boolean.FALSE;
		}
		//
		if (!BaseConst.SITE_OPEN_TO_PUBLIC) {
			result.type = Type.warn;
			result.message = "非常抱歉，平台正在测试，暂不支持注册！";
			return result;
		}
		//
		if (!CheckCodeHelper.isValidCode(session, chkCode)) {
			result.type = Type.warn;
			result.message = "请输入正确的验证码";
			result.code = 21;
			return result;
		}
		User dbUser = userService.getUserByPhoneNo(phoneNo);
		if (dbUser == null) {
			SmsVerfCode smsVerfCode = new SmsVerfCode();
			smsVerfCode.setPhoneNo(phoneNo);
			smsVerfCode.setVfCode(smsCode);
			smsVerfCode.setUsage(SmsUsage.regist);
			if (settingService.validSmsVerfCode(smsVerfCode)) {
				Member member = new Member();
				User user = new User();
				password = RSACrypter.decryptStringFromJs(password);
				user.setPassword(password);
				if (StrUtil.isNullOrBlank(nickName)) {
					nickName = phoneNo;
				}
				user.setNickName(nickName);
				user.setPhoneNo(phoneNo);
				user.setSecureLevel(secureLevel);
				member.setPoint(0);
				member.setGrade(1);
				member.setUser(user);
				member.setDisabled(false);
				member.setMemo("您是注册会员");
				boolean ok = memberService.saveMember(member);
				if (ok) {
					// 修改验证码记录（已使用）
					settingService.updateSmsVerfCode(smsVerfCode);
					//
					UserContext userContext = getUserContext(request);
					userContext.setUserId(user.getId());
					userContext.setPhoneNo(user.getPhoneNo());
					userContext.setUserName(user.getNickName());
					//
					setUserContext(request, userContext);
					//
					if (autoLoginFlag) {
						// 处理自动登录
						String tokenEncoded = AlTokenHelper.setUserAlToken(user.getId(), appType, session.getId());
						//
						Cookie alTokenCookie = new Cookie(AppBase.COOKIE_ALTOKEN_NAME, tokenEncoded);
						alTokenCookie.setMaxAge(AlToken.MAX_AGE_IN_SECONDS);
						alTokenCookie.setPath("/");
						response.addCookie(alTokenCookie);
						//
						response.setHeader(AppBase.HEADER_ALTOKEN_NAME, tokenEncoded);
						// response.setHeader(AppBase.HEADER_SESSION_NAME, session.getId());
					} else {
						WebUtil.removeCookie(response, AppBase.COOKIE_ALTOKEN_NAME, "/");
						AlTokenHelper.removeUserAlToken(user.getId(), appType);
					}
					//
					// 同步session购物车到Ceche中
					CartHelper.mergeCenterCacheCart(session);
					//
					result.message = "恭喜您，注册已成功!";
					// 检查页面重定向
					String redirectUrl = (String) session.getAttribute(AppBase.SESSION_KEY_REDIRECT_URL);
					if (StrUtil.hasText(redirectUrl)) {
						result.data = redirectUrl;
						//
						session.removeAttribute(AppBase.SESSION_KEY_REDIRECT_URL);
					} else {
						result.data = "/";
					}
				} else {
					result.type = Type.warn;
					result.message = "很抱歉，注册失败!";
				}
			} else {
				result.type = Type.warn;
				result.message = "手机验证码错误";
				result.code = 11;
			}
		} else {
			result.type = Type.warn;
			result.message = "此手机号已被注册使用";
			result.code = 12;
		}
		return result;
	}

	/**
	 * 注销操作
	 * 
	 * @author 廖晓远
	 * @date 2015-5-13 下午6:06:03
	 * @param request
	 * @return String 登录页面
	 * @throws IOException
	 */
	@Remark("注销操作")
	@RequestMapping(value = "/logout/do", method = RequestMethod.GET)
	public String toLoginOut(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		session.invalidate();
		// 清空原有的自动登录cookie
		WebUtil.removeCookie(response, AppBase.COOKIE_ALTOKEN_NAME, "/");
		//
		String resultUrl = WebUtil.getRefererHeader(request);
		if (StrUtil.isNullOrBlank(resultUrl)) {
			resultUrl = WebUtil.getAppRelUrl(request);
		} else {
			resultUrl = WebUtil.getAppRelUrl(request, resultUrl);
		}
		//
		return "redirect:" + resultUrl;
	}

	/**
	 * 手机号码是否存在
	 * 
	 * @author 廖晓远
	 * @date 2015-5-26 上午9:51:30
	 * 
	 * @return
	 */
	@Remark("手机号码是否存在")
	@RequestMapping(value = "/exist/by/phoneNo", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getUserExistByPhoneNo(@RequestBody MapContext requestData) {
		Result<Boolean> result = Result.newOne();
		String phoneNo = requestData.getTypedValue("phoneNo", String.class);
		if (phoneNo != null) {
			result.data = (userService.getUserByPhoneNo(phoneNo) != null);
		}
		return result;
	}

	/**
	 * 邮箱是否存在
	 * 
	 * @author 毛智东
	 * @date 2015年7月6日 下午1:17:54
	 * 
	 * @param email
	 * @return
	 */
	@Remark("邮箱是否存在")
	@RequestMapping(value = "/exist/by/email", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getUserExistByEmail(@RequestBody String email) {
		Result<Boolean> result = Result.newOne();
		if (email != null) {
			result.data = (userService.getUserByEmail(email) != null);
		}
		return result;
	}

	// ----------------------------------------------找回登录密码--------------------------------------------------------------------

	/**
	 * 检查用户身份页面
	 * 
	 * @author 郝江奎
	 * @date 2015年12月21日 上午11:48:44
	 * 
	 * @return
	 */
	@Remark("检查用户身份页面")
	@RequestMapping(value = "/logPass/trv/jsp", method = RequestMethod.GET)
	public String toSetLogPassJsp() {
		return "user/logPass-trv";
	}

	/**
	 * 设置登录密码页面
	 * 
	 * @author 郝江奎
	 * @date 2015年12月21日 上午11:48:44
	 * 
	 * @return
	 */
	@Remark("设置登录密码页面")
	@RequestMapping(value = "/logPass/set/jsp", method = RequestMethod.GET)
	public String toSetPasswordJsp() {
		return "user/logPass-set";
	}

	/**
	 * 设置登录密码结果页面
	 * 
	 * @author 郝江奎
	 * @date 2015年12月21日 上午11:48:44
	 * 
	 * @return
	 */
	@Remark("设置登录密码结果页面")
	@RequestMapping(value = "/logPass/set/result/jsp", method = RequestMethod.GET)
	public String toSetSuccessJsp() {
		return "user/logPass-set-result";
	}

	/**
	 * 找回登录密码
	 * 
	 * @author 毛智东
	 * @date 2015年7月9日 上午11:48:44
	 * 
	 * @return
	 */
	@Remark("找回登录密码")
	@RequestMapping(value = "/logPass/retrv/jsp", method = RequestMethod.GET)
	public String toLogPassJsp() {
		return "user/logPass-retrv";
	}

	/**
	 * 重置登录密码页面
	 * 
	 * @author 郝江奎
	 * @date 2015年11月7日 下午14:12:44
	 * 
	 * @return
	 */
	@Remark("重置登录密码页面")
	@RequestMapping(value = "/logPass/reset/jsp", method = RequestMethod.GET)
	public String toResetPasswordJsp() {
		return "user/logPass-reset";
	}

	/**
	 * 重置密码成功页面
	 * 
	 * @author 郝江奎
	 * @date 2015年11月7日 下午14:12:44
	 * 
	 * @return
	 */
	@Remark("重置密码成功页面")
	@RequestMapping(value = "/logPass/reset/result/jsp", method = RequestMethod.GET)
	public String toResetSuccessJsp() {
		return "user/logPass-reset-result";
	}

	/**
	 * 得到验证的手机或邮箱
	 * 
	 * @author 毛智东
	 * @date 2015年7月9日 下午7:15:37
	 * 
	 * @param mapContext
	 * @return
	 */
	@Remark("得到验证的手机或邮箱")
	@RequestMapping(value = "/phone/or/email/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> getUserVerfPhoneOrEmail(@RequestBody MapContext mapContext) {
		Result<String> result = Result.newOne();
		String userName = mapContext.getTypedValue("userName", String.class);
		String validType = mapContext.getTypedValue("validType", String.class);
		VerfAspect aspect = VerfAspect.valueOf(validType);
		User user = new User();
		if (aspect == VerfAspect.phoneNo) {
			user = userService.getUserByPhoneNo(userName);
		}
		if (aspect == VerfAspect.email) {
			user = userService.getUserByEmail(userName);
		}
		Integer userId = user.getId();
		boolean flag = userService.getUserVerfStatusByUserIdAndAspect(userId, aspect) != null;
		if (flag) {
			if (aspect == VerfAspect.phoneNo) {
				result.data = user.getPhoneNo();
			}
			if (aspect == VerfAspect.email) {
				result.data = user.getEmail();
			}
		} else {
			result.type = Type.warn;
		}
		return result;
	}

	/**
	 * 修改登录密码（未登录）
	 * 
	 * @author 郝江奎
	 * @date 2015年11月7日 下午14:07:09
	 * 
	 * @param mapContext
	 * @return
	 */
	@Remark("修改登录密码（未登录）")
	@RequestMapping(value = "/logPass/reset/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> updatePassword(HttpServletRequest request, @RequestBody MapContext mapContext) {
		Result<?> result = Result.newOne();
		String password = mapContext.getTypedValue("password", String.class);
		Integer secureLevel = mapContext.getTypedValue("secureLevel", Integer.class);

		UserContext userContext = getUserContext(request);
		String phoneNo = userContext.getPhoneNo();
		User user = userService.getUserByPhoneNo(phoneNo);
		String salt = user.getSalt();
		if (StrUtil.isNullOrBlank(salt)) {
			salt = PasswordUtil.generateSaltStr();
			user.setSalt(salt);
		}
		password = RSACrypter.decryptStringFromJs(password);
		password = PasswordUtil.encrypt(password, salt);
		user.setPassword(password);
		user.setId(user.getId());
		user.setPhoneNo(phoneNo);

		boolean ok = false;
		ok = userService.updateUser(user);
		if (ok) {
			// 用户验证信息(登录密码)
			UserVerfStatus userVerfStatus = userService.getUserVerfStatusByUserIdAndAspect(user.getId(), VerfAspect.logPass);
			if (userVerfStatus != null) {
				userVerfStatus.setSecureLevel(secureLevel);
				userService.updateUserVerfStatus(userVerfStatus);
			} else {
				UserVerfStatus userVerfStatusN = new UserVerfStatus();
				userVerfStatusN.setUserId(user.getId());
				userVerfStatusN.setFlag(true);
				userVerfStatusN.setAspect(VerfAspect.logPass);
				userVerfStatusN.setTs(new Date());
				userVerfStatusN.setSecureLevel(secureLevel);
				userService.saveUserVerfStatus(userVerfStatusN);
			}
			result.message = "修改成功";
		} else {
			result.message = "修改失败";
			result.type = Type.warn;
		}
		return result;
	}

	/**
	 * 修改登录密码（已登录）
	 * 
	 * @author 郝江奎
	 * @date 2015年11月12日 下午14:07:09
	 * 
	 * @param mapContext
	 * @return
	 */
	@Remark("修改登录密码（已登录）")
	@RequestMapping(value = "/logined/logPass/reset/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> updateLogPassword(HttpServletRequest request, @RequestBody MapContext mapContext) {
		Result<?> result = Result.newOne();
		String oldPassword = mapContext.getTypedValue("oldPassword", String.class);
		String password = mapContext.getTypedValue("password", String.class);
		Integer secureLevel = mapContext.getTypedValue("secureLevel", Integer.class);

		UserContext userContext = getUserContext(request);
		Integer userId = userContext.getUserId();
		String phoneNo = userContext.getPhoneNo();
		oldPassword = RSACrypter.decryptStringFromJs(oldPassword);
		boolean userFlag = userService.verifyUserPassword(userId, oldPassword);
		if (userFlag) {
			User user = userService.getUserByPhoneNo(phoneNo);
			String salt = user.getSalt();
			password = RSACrypter.decryptStringFromJs(password);
			password = PasswordUtil.encrypt(password, salt);
			user.setPassword(password);
			user.setId(userId);
			user.setPhoneNo(phoneNo);
			boolean ok = false;
			ok = userService.updateUser(user);
			if (ok) {
				// 用户验证信息(登录密码)
				UserVerfStatus userVerfStatus = userService.getUserVerfStatusByUserIdAndAspect(user.getId(), VerfAspect.logPass);
				if (userVerfStatus != null) {
					userVerfStatus.setSecureLevel(secureLevel);
					userService.updateUserVerfStatus(userVerfStatus);
				} else {
					UserVerfStatus userVerfStatusN = new UserVerfStatus();
					userVerfStatusN.setUserId(user.getId());
					userVerfStatusN.setFlag(true);
					userVerfStatusN.setAspect(VerfAspect.logPass);
					userVerfStatusN.setTs(new Date());
					userVerfStatusN.setSecureLevel(secureLevel);
					userService.saveUserVerfStatus(userVerfStatusN);
				}

				userContext.setUserId(user.getId());
				userContext.setPhoneNo(user.getPhoneNo());
				userContext.setUserName(user.getNickName());
				Gender gender = user.getGender();
				if (gender == null) {
					gender = Gender.X;
				}
				userContext.setGender(gender.name());
				//
				setUserContext(request, userContext);
				result.message = "修改成功";
			} else {
				result.message = "修改失败";
				result.type = Type.warn;
			}
		} else {
			result.message = "旧密码不正确";
			result.type = Type.warn;
		}

		return result;
	}

	/**
	 * 修改支付密码
	 * 
	 * @author 郝江奎
	 * @date 2015年11月9日 下午14:07:09
	 * 
	 * @param mapContext
	 * @return
	 */
	@Remark("修改支付密码")
	@RequestMapping(value = "/payPass/reset/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> updatePayPassword(HttpServletRequest request, @RequestBody MapContext mapContext) {
		Result<?> result = Result.newOne();
		String payPassword = mapContext.getTypedValue("payPassword", String.class);
		String phoneCode = mapContext.getTypedValue("phoneCode", String.class);
		Integer secureLevel = mapContext.getTypedValue("secureLevel", Integer.class);

		UserContext userContext = getUserContext(request);
		String phoneNo = userContext.getPhoneNo();
		SmsVerfCode smsVerfCode = new SmsVerfCode();
		smsVerfCode.setPhoneNo(phoneNo);
		smsVerfCode.setVfCode(phoneCode);
		smsVerfCode.setUsage(SmsUsage.payPass);
		if (settingService.validSmsVerfCode(smsVerfCode)) {

			User user = userService.getUserByPhoneNo(phoneNo);
			String salt = user.getSalt();
			payPassword = RSACrypter.decryptStringFromJs(payPassword);
			payPassword = PasswordUtil.encrypt(payPassword, salt);
			user.setPayPassword(payPassword);
			user.setId(user.getId());
			user.setPhoneNo(phoneNo);
			boolean ok = false;
			ok = userService.updateUser(user);
			if (ok) {
				// 修改验证码记录（已使用）
				settingService.updateSmsVerfCode(smsVerfCode);
				// 用户验证信息(支付密码)
				UserVerfStatus userVerfStatus = userService.getUserVerfStatusByUserIdAndAspect(user.getId(), VerfAspect.payPass);
				if (userVerfStatus != null) {
					userVerfStatus.setSecureLevel(secureLevel);
					userService.updateUserVerfStatus(userVerfStatus);
				} else {
					UserVerfStatus userVerfStatusN = new UserVerfStatus();
					userVerfStatusN.setUserId(user.getId());
					userVerfStatusN.setFlag(true);
					userVerfStatusN.setAspect(VerfAspect.payPass);
					userVerfStatusN.setTs(new Date());
					userVerfStatusN.setSecureLevel(secureLevel);
					userService.saveUserVerfStatus(userVerfStatusN);
				}
				result.message = "修改成功";
			} else {
				result.message = "修改失败";
				result.type = Type.warn;
			}
		} else {
			result.type = Type.warn;
			result.message = "手机验证码错误";
		}

		return result;
	}

	/**
	 * 获取用户详细信息
	 * 
	 * @author 郝江奎
	 * @date 2015年11月3日 下午20:15:09
	 * 
	 * @param request
	 * @return
	 */
	@Remark("获取用户详细信息")
	@RequestMapping(value = "/info/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Member> getUserInfo(HttpServletRequest request) {
		Result<Member> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		Integer id = userContext.getUserId();
		Member member = memberService.getMemberById(id);
		if (member != null) {
			result.type = Type.info;
			result.data = member;
		} else {
			result.type = Type.warn;
			result.message = "请先登录";
		}
		return result;
	}

	/**
	 * 修改用户详细信息
	 * 
	 * @author 郝江奎
	 * @date 2015年11月4日 下午13:56:09
	 * 
	 * @param request
	 * @return
	 */
	@Remark("修改用户详细信息")
	@RequestMapping(value = "/info/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Member> updateUserInfo(HttpServletRequest request, @RequestBody Member member) {
		Result<Member> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		Integer id = userContext.getUserId();
		boolean ok = false;
		member.setId(id);
		ok = memberService.updateMember(member);
		if (ok) {
			userContext.setUserName(member.getUser().getNickName());
			Gender gender = member.getUser().getGender();
			if (gender == null) {
				gender = Gender.X;
			}
			userContext.setGender(gender.name());
			//
			setUserContext(request, userContext);
			result.message = "更新成功";
			result.type = Type.info;
			result.data = member;
		} else {
			result.type = Type.warn;
			result.message = "请先登录";
		}
		return result;
	}

	/**
	 * 获取用户联系方式
	 * 
	 * @author 郝江奎
	 * @date 2015年11月4日 下午13:56:09
	 * 
	 * @param request
	 * @return
	 */
	@Remark("获取用户联系方式")
	@RequestMapping(value = "/linkWay/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<UserLinkWay>> getUserLinkWay(HttpServletRequest request) {
		Result<List<UserLinkWay>> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		Integer id = userContext.getUserId();
		List<UserLinkWay> userLinkWay = userService.getUserLinkWayByUserId(id);
		result.data = userLinkWay;
		return result;
	}

	/**
	 * 添加用户联系方式
	 * 
	 * @author 郝江奎
	 * @date 2015年11月4日 下午20:56:09
	 * 
	 * @param request
	 * @return
	 */
	@Remark("添加用户联系方式")
	@RequestMapping(value = "/linkWay/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> saveUserLinkWay(HttpServletRequest request, @RequestBody UserLinkWay userLinkWay) {
		Result<?> result = Result.newOne();
		boolean ok = false;
		UserContext userContext = getUserContext(request);
		Integer id = userContext.getUserId();
		userLinkWay.setUserId(id);
		ok = userService.addUserLinkWay(userLinkWay);
		if (ok) {
			result.message = "添加成功";
			result.type = Type.info;
		} else {
			result.message = "添加失败";
			result.type = Type.warn;
		}
		return result;
	}

	/**
	 * 修改用户联系方式
	 * 
	 * @author 郝江奎
	 * @date 2015年11月4日 下午17:56:09
	 * 
	 * @param request
	 * @return
	 */
	@Remark("修改用户联系方式")
	@RequestMapping(value = "/linkWay/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> updateUserLinkWay(HttpServletRequest request, @RequestBody UserLinkWay userLinkWay) {
		Result<?> result = Result.newOne();
		boolean ok = false;
		ok = userService.updateUserLinkWay(userLinkWay);
		if (ok) {
			result.message = "修改成功";
			result.type = Type.info;
		} else {
			result.message = "修改失败";
			result.type = Type.warn;
		}
		return result;
	}

	/**
	 * 删除联系方式
	 * 
	 * @author 郝江奎
	 * @date 2015年11月4日 下午7:39:26
	 * 
	 * @param id
	 *            删除的联系方式id
	 * @return 返回删除结果
	 */
	@Remark("删除联系方式")
	@RequestMapping(value = "/linkWay/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteUserLinkWay(@RequestParam("id") Integer id) {
		Result<?> result = Result.newOne();
		result.message = userService.deleteUserLinkWayById(id) ? "删除成功！" : "删除失败！";
		return result;
	}

	/**
	 * 设置默认联系方式
	 * 
	 * @author 郝江奎
	 * @date 2015年11月4日 下午7:39:26
	 * 
	 *            设置的联系方式id
	 * @return 返回设置结果
	 */
	@Remark("设置默认联系方式")
	@RequestMapping(value = "/linkWay/set/defaulted/by/id", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> setDefaultedLinkWay(HttpServletRequest request, @RequestParam("id") Integer linkWayId) {
		Result<?> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		Integer userId = userContext.getUserId();
		result.message = userService.setUserDefaultLinkWay(userId, linkWayId) ? "设置成功！" : "设置失败！";
		return result;
	}

	/**
	 * 获取默认的联系方式
	 * 
	 * @author 郝江奎
	 * @date 2015年11月4日 下午7:39:26
	 * 
	 *            默认的联系方式id
	 * @return 返回默认结果
	 */
	@Remark("获取默认的联系方式")
	@RequestMapping(value = "/linkWay/default/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<UserLinkWay> getDefaultUserLinkWay(HttpServletRequest request) {
		Result<UserLinkWay> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		Integer userId = userContext.getUserId();
		boolean defaulted = true;
		result.data = userService.getUserDefaultLinkWay(userId, defaulted);
		return result;
	}

	/**
	 * 检查联系方式别名
	 * 
	 * @author 郝江奎
	 * @date 2015年11月5日 上午10:39:26
	 * 
	 * @param alias
	 *            联系方式别名alias
	 * @return 返回查找结果
	 */
	@Remark("检查联系方式别名")
	@RequestMapping(value = "/alias/check/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<UserLinkWay> checkUserLinkWayByAlias(HttpServletRequest request, @RequestParam("alias") String alias) {
		Result<UserLinkWay> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		Integer userId = userContext.getUserId();
		UserLinkWay userLinkWay = userService.getUserLinkWayByUserIdAndAlias(userId, alias);
		if (userLinkWay != null) {
			result.type = Type.warn;
			result.message = "该联系方式别名已存在！";
			result.data = userLinkWay;
		}
		return result;
	}

	/**
	 * 根据手机号查询用户名称
	 * 
	 * @author wangdi
	 * @date 2015年11月7日 下午2:55:34
	 * 
	 * @param request
	 * @return
	 */
	@Remark("根据手机号查询用户名称")
	@RequestMapping(value = "/name/get/by/phoneNo", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> getUserNameByPhoneNo(HttpServletRequest request) {
		Result<String> result = Result.newOne();
		String phoneNo = request.getParameter("phoneNo");
		if (phoneNo == null) {
			result.type = Result.Type.error;
			result.message = "未指定手机号";
			return result;
		}

		User user = userService.getUserByPhoneNo(phoneNo);
		if (user == null) {
			result.type = Result.Type.error;
			result.message = "用户不存在";
			return result;
		}
		// 查询结果赋值
		result.data = user.getNickName();

		return result;
	}

	/**
	 * 判断用户是否设置了支付密码
	 * 
	 * @author 邓华锋
	 * @date 2015年12月1日 下午3:32:08
	 * 
	 * @param request
	 * @return
	 */
	@Remark("判断用户是否设置了支付密码")
	@RequestMapping(value = "/payPass/exist", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> payPasswordExist(HttpServletRequest request) {
		Result<Boolean> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		Integer userId = userContext.getUserId();
		result.data = userService.checkUserPayPasswordSet(userId);
		return result;
	}

	// -------------------------------------用户优惠券-----------------------------------------

	@Remark("我的优惠券")
	@RequestMapping(value = "/coupon/jsp", method = RequestMethod.GET)
	public String toCouponJsp() {
		return "ucenter/coupon/userCoupon";
	}

	@Remark("分页获取用户优惠券列表")
	@RequestMapping(value = "/coupon/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<String, Object>> getUserCoupons(HttpServletRequest request, @RequestBody PaginatedFilter paginatedFilter) {
		Result<Map<String, Object>> result = Result.newOne();
		try {
			MapContext mapContext = paginatedFilter.getFilterItems();

			UserContext usetContext = getUserContext(request);
			mapContext.put("userId", usetContext.getUserId());

			PaginatedList<UserCoupon> paginatedList = salePrmtService.getUserCouponsByFilter(paginatedFilter);

			Map<String, Object> resultData = new HashMap<String, Object>();

			resultData.put("couponUnusedCount", salePrmtService.getUserCouponCountByUserId(usetContext.getUserId()));
			resultData.put("paginatedList", paginatedList);
			result.data = resultData;
		} catch (Exception e) {
			e.printStackTrace();
			result.type = Result.Type.warn;
		}
		return result;
	}

	@Remark("根据用户id获取当前优惠券未使用数量")
	@RequestMapping(value = "/coupon/count/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> getUserCouponCount(HttpServletRequest request) {
		Result<Integer> result = Result.newOne();
		try {
			UserContext usetContext = getUserContext(request);

			result.data = salePrmtService.getUserCouponCountByUserId(usetContext.getUserId());
		} catch (Exception e) {
			result.type = Result.Type.warn;
		}
		return result;
	}

	@Remark("删除用户商品优惠券")
	@RequestMapping(value = "/coupon/deleted/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> updateUserCouponForDelete(HttpServletRequest request, @RequestBody UserCoupon userCoupon) {
		Result<Boolean> result = Result.newOne();
		try {
			UserContext usetContext = getUserContext(request);
			result.data = salePrmtService.updateUserCouponForDelete(userCoupon, usetContext);
		} catch (Exception e) {
			result.type = Result.Type.warn;
		}
		return result;
	}
	// -------------------------------------用户优惠券-----------------------------------------

	@Remark("我的服务优惠券")
	@RequestMapping(value = "/svc/coupon/jsp", method = RequestMethod.GET)
	public String toSvcCouponJsp() {
		return "ucenter/coupon/userSvcCoupon";
	}

	@Remark("分页获取用户服务优惠券列表")
	@RequestMapping(value = "/svc/coupon/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<String, Object>> getUserSvcCoupons(HttpServletRequest request, @RequestBody PaginatedFilter paginatedFilter) {
		Result<Map<String, Object>> result = Result.newOne();
		try {
			MapContext mapContext = paginatedFilter.getFilterItems();

			UserContext usetContext = getUserContext(request);
			mapContext.put("userId", usetContext.getUserId());

			PaginatedList<UserSvcCoupon> paginatedList = salePrmtService.getUserSvcCouponsByFilter(paginatedFilter);

			Map<String, Object> resultData = new HashMap<String, Object>();

			resultData.put("couponUnusedCount", salePrmtService.getUserSvcCouponCountByUserId(usetContext.getUserId()));
			resultData.put("paginatedList", paginatedList);
			result.data = resultData;
		} catch (Exception e) {
			e.printStackTrace();
			result.type = Result.Type.warn;
		}
		return result;
	}

	@Remark("根据用户id获取当前优惠券未使用数量")
	@RequestMapping(value = "/svc/coupon/count/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> getUserSvcCouponCount(HttpServletRequest request) {
		Result<Integer> result = Result.newOne();
		try {
			UserContext usetContext = getUserContext(request);

			result.data = salePrmtService.getUserSvcCouponCountByUserId(usetContext.getUserId());
		} catch (Exception e) {
			result.type = Result.Type.warn;
		}
		return result;
	}

	@Remark("删除用户服务优惠券")
	@RequestMapping(value = "/svc/coupon/deleted/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> updateUserSvcCouponForDeleted(HttpServletRequest request, @RequestBody UserSvcCoupon userSvcCoupon) {
		Result<Boolean> result = Result.newOne();
		try {
			UserContext usetContext = getUserContext(request);
			result.data = salePrmtService.updateUserSvcCouponForDeleted(userSvcCoupon, usetContext);
		} catch (Exception e) {
			result.type = Result.Type.warn;
		}
		return result;
	}

	// ---------------------------------------------------根据用户id匹配优惠券--------------------------------------------------------
	@Remark("根据用户id获取当前匹配的优惠券列表")
	@RequestMapping(value = "/coupon/map/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<String, Object>> getUserSvcCouponsByUserId(HttpServletRequest request, @RequestBody SaleOrderInfo saleOrderInfo) {
		Result<Map<String, Object>> result = Result.newOne();
		try {
			SaleCartInfo saleCartInfo = saleOrderInfo.getSaleCartInfo();
			if (saleCartInfo != null) {
				UserContext usetContext = getUserContext(request);
				result.data = salePrmtService.getMatchingUserCoupons(usetContext.getUserId(), saleCartInfo);
			} else {
				result.type = Result.Type.warn;
				result.message = "参数不全";
			}
		} catch (Exception e) {
			result.type = Result.Type.warn;
		}
		return result;
	}
}
