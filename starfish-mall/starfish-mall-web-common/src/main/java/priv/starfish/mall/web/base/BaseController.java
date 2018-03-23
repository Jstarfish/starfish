package priv.starfish.mall.web.base;

import freemarker.template.Template;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import priv.starfish.common.cms.FreeMarkerService;
import priv.starfish.common.jms.SimpleMessageSender;
import priv.starfish.common.mail.MailService;
import priv.starfish.common.model.ScopeEntity;
import priv.starfish.common.sms.SmsErrorCode;
import priv.starfish.common.sms.SmsMessage;
import priv.starfish.common.sms.SmsResult;
import priv.starfish.common.sms.SmsService;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.user.UserContextHolder;
import priv.starfish.common.util.CodeUtil;
import priv.starfish.common.util.DateUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.WebUtil;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.Resource;
import priv.starfish.mall.comn.entity.SiteFunction;
import priv.starfish.mall.comn.entity.SiteModule;
import priv.starfish.mall.mall.dto.MallDto;
import priv.starfish.mall.notify.dict.SmsUsage;
import priv.starfish.mall.notify.entity.SmsVerfCode;
import priv.starfish.mall.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

public abstract class BaseController {
	protected final Log logger = LogFactory.getLog(this.getClass());
	//
	@Autowired(required = false)
	protected SimpleMessageSender simpleMessageSender;

	@javax.annotation.Resource(name = "freeMarkerService")
	protected FreeMarkerService freeMarkerService;
	@javax.annotation.Resource(name = "fileSrcFreeMarkerService")
	protected FreeMarkerService fileSrcFreeMarkerService;

	@javax.annotation.Resource(name = "settingService")
	protected SettingService settingService;

	@javax.annotation.Resource
	protected MailService mailService;

	@javax.annotation.Resource
	protected SmsService smsService;

	@javax.annotation.Resource
	protected UserService userService;

	@javax.annotation.Resource
	protected User3rdService user3rdService;

	@javax.annotation.Resource
	protected ResourceService rescService;
	
	@javax.annotation.Resource
	protected AuthxService authxService;

	protected static <T> ResponseEntity<T> newResponseEntity(T result, HttpStatus statusCode) {
		return new ResponseEntity<T>(result, statusCode);
	}

	protected static <T> ResponseEntity<T> newResponseEntity(T result) {
		HttpStatus statusCode = HttpStatus.OK;
		return newResponseEntity(result, statusCode);
	}

	// ------------------- 前、后台通用
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

	protected static UserContext getUserContext(HttpServletRequest request) {
		return getUserContext(request.getSession(true));
	}

	protected static void setUserContext(HttpServletRequest request, UserContext userContext) {
		setUserContext(request.getSession(true), userContext);
	}

	// ------------------- 后台专用
	// 返回当前用户会话所在实体范围scope内的实体的id
	protected static Integer getUserContextScopeEntityId(HttpServletRequest request, AuthScope scope) {
		UserContext userContext = getUserContext(request);
		//
		return userContext.getScopeEntityId(scope == null ? null : scope.name());
	}

	// 用户当前上下文的资源模块菜单树
	protected static void setUserScopeEntitySiteResTree(HttpServletRequest request, List<SiteModule> siteResTree) {
		WebUtil.setSessionAttribute(request, AppBase.SESSION_KEY_USER_SCOPE_SITE_RES_TREE, siteResTree);
	}

	protected static List<SiteModule> getUserScopeEntitySiteResTree(HttpServletRequest request) {
		return WebUtil.getSessionAttribute(request, AppBase.SESSION_KEY_USER_SCOPE_SITE_RES_TREE);
	}

	protected List<SiteModule> fetchUserSiteModules(UserContext userContext) {
		List<SiteModule> siteModules = null;
		//
		ScopeEntity scopeEntity = userContext.getScopeEntity();
		AuthScope scope = AuthScope.valueOf(scopeEntity.getScope());
		// 当前用户权限列表
		List<Integer> permIds = CacheHelper.getContextUserPermIds(userContext);
		//
		if (userContext.isSysUser()) {
			siteModules = rescService.getSiteModulesByScope(scope, true);
			for (int si = siteModules.size() - 1; si >= 0; si--) {
				SiteModule siteModule = siteModules.get(si);
				List<SiteFunction> functions = siteModule.getFunctions();
				for (int fi = functions.size() - 1; fi >= 0; fi--) {
					SiteFunction function = functions.get(fi);
					List<Resource> resources = function.getResources();
					//
					for (int ri = resources.size() - 1; ri >= 0; ri--) {
						Resource resource = resources.get(ri);
						Integer permId = resource.getPermId();
						//
						if (permId != null) {
							if (!permIds.contains(permId)) {
								resources.remove(ri);
							}
						}
						//
					}
					// 检查sitefunction中的resource数目
					if (resources.size() < 1) {
						functions.remove(fi);
					}
					//
				}
				// 检查siteModule中的function数目
				if (functions.size() < 1) {
					siteModules.remove(si);
				}
			}
		}
		//
		return siteModules;
	}

	/**
	 * 一般发送短信
	 * 
	 * @author koqiui
	 * @date 2015年11月18日 下午8:47:34
	 * 
	 * @param phoneNo
	 * @param smsTplCode
	 * @param dataModel
	 * @return
	 */
	protected SmsResult sendSmsText(String phoneNo, String smsTplCode, MapContext dataModel) {
		Template template = freeMarkerService.getTemplate("sms", smsTplCode);
		//
		if (dataModel == null) {
			dataModel = MapContext.newOne();
		}
		//
		String smsText = freeMarkerService.renderContent(template, dataModel);
		//
		SmsMessage message = new SmsMessage();
		message.setReceiverNumber(phoneNo);
		message.setText(smsText);
		//
		SmsResult result = SmsResult.newOne();
		result.smsText = smsText;
		result.errCode = smsService.sendSms(message, dataModel);
		//
		return result;
	}
	/**
	 * 判断发送短信验证码数量是否超出
	 * 
	 * @author 郝江奎
	 * @date 2015年11月26日 下午16:18:12
	 * 
	 * @return 
	 */
	protected boolean sendSmsByLimit(HttpServletRequest request, String reqIp, SmsUsage tplName) {
		// 发送验证码
		boolean success = true;
		
		SmsUsage[] limitUsages = BaseConst.SMS_LIMIT_USAGE_LIST;
		
		for (int i = 0; i <limitUsages.length ; i++) {
			if (limitUsages[i].equals(tplName)) {
				Date now = new Date();
				String sendDateStr = DateUtil.toStdDateStr(now);
				int alreaySent = settingService.getSmsVerfCodes(reqIp, sendDateStr, limitUsages).size();
				this.logger.debug(sendDateStr + " 已发送的短信数量：" + alreaySent);
				if (alreaySent >= BaseConst.SMS_MAX_COUNT_PER_DAY_PER_IP) {
					success = false;
				}else{
					success = true;
				}
				//
				break;
			}
		}
		//
		return success;
	}

	/**
	 * 发送短信验证码（内用）
	 * 
	 * @author koqiui
	 * @date 2015年11月12日 下午12:18:12
	 * 
	 * @param usage
	 * @param phoneNo
	 * @param smsTplCode
	 * @param reqIp
	 * @return
	 */
	protected boolean sendSmsVerfCode(SmsUsage usage, String phoneNo, String smsTplCode, String reqIp) {
		boolean success = false;
		//
		String vfCode = CodeUtil.randomNumCode(6);
		//
		SmsVerfCode smsVerfCode = new SmsVerfCode();
		smsVerfCode.setUsage(usage);
		smsVerfCode.setPhoneNo(phoneNo);
		smsVerfCode.setVfCode(vfCode);
		smsVerfCode.setReqIp(reqIp);
		smsVerfCode.setSendOk(false);
		smsVerfCode.setInvalid(false);
		try {
			Date sendTime = new Date();
			Date expireTime = DateUtil.addMinutes(sendTime, BaseConst.SMS_CODE_EXPIRE_TIME_IN_MINUTES);
			smsVerfCode.setSendTime(sendTime);
			smsVerfCode.setExpireTime(expireTime);
			//
			MallDto mallDto = CacheHelper.getMallInfo();
			
			MapContext dataModel = MapContext.newOne();
			dataModel.put(BaseConst.TplModelVars.CODE, vfCode);
			dataModel.put(BaseConst.TplModelVars.COMPANY, mallDto.getName());
			dataModel.put(BaseConst.TplModelVars.EXPIRE_MINUTES, BaseConst.SMS_CODE_EXPIRE_TIME_IN_MINUTES);
			//
			dataModel.put("bizCode", usage.name());
			dataModel.put("sendTime", sendTime);
			//
			SmsResult smsResult = this.sendSmsText(phoneNo, smsTplCode, dataModel);
			SmsErrorCode smsErrorCode = smsResult.errCode;
			String smsText = smsResult.smsText;
			//
			smsVerfCode.setContent(smsText);
			//
			if (smsErrorCode == SmsErrorCode.OK) {
				smsVerfCode.setSendOk(true);
				success = true;
			}
		} catch (Exception e) {
			this.logger.error(e);
		} finally {
			settingService.saveSmsVerfCode(smsVerfCode);
		}
		//
		return success;
	}
}
