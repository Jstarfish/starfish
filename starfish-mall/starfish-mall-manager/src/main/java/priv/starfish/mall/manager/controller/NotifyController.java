package priv.starfish.mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.exception.ValidationException;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.PhoneNumberUtil;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.WebUtil;
import priv.starfish.common.web.CheckCodeHelper;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.notify.dict.SmsUsage;
import priv.starfish.mall.notify.entity.SmsVerfCode;
import priv.starfish.mall.service.BaseConst.SmsCodes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Remark("短信、邮件等通知")
@Controller
@RequestMapping(value = "/notify")
public class NotifyController extends BaseController {

	/*
	 * 发送短信验证码，一般用途 /sms/send/for/normal/do 参数：phoneNo
	 * 
	 * 发送短信验证码，注册用（检查确保手机号未被注册使用） /sms/send/for/regist/do 参数：phoneNo [, chkCode]
	 * 
	 * 发送短信验证码，找回登录密码用（未登录，检查确保手机号已被注册使用） /sms/send/for/logPass/do 参数：phoneNo [, chkCode]
	 * 
	 * 发送短信验证码，支付用（检查确保手机号已被注册使用，要(找出当前用户手机号)验证手机号和短信码） /sms/send/for/pay/do 参数：phoneNo, orderNo
	 * 
	 * 发送短信验证码，支付密码重置用（已登录，验证图形码、要(找出当前用户手机号)验证手机号） /sms/send/for/payPass/do 参数：phoneNo [, chkCode]
	 */
	@Remark("发送短信验证码，注册用")
	@RequestMapping(value = "/sms/send/for/regist/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> sendSmsForRegist(HttpServletRequest request, @RequestBody MapContext requstData, HttpSession session) {
		Result<Boolean> result = Result.newOne();
		// 如果有图形验证码，检查图形验证码的有效性
		String chkCode = requstData.getTypedValue("chkCode", String.class);
		if (StrUtil.hasText(chkCode)) {
			if (!CheckCodeHelper.isValidCode(request.getSession(true), chkCode)) {
				throw new ValidationException("请输入正确的图形验证码");
			}
		}
		// 验证手机号码的有效性
		String phoneNo = requstData.getTypedValue("phoneNo", String.class);
		if (!PhoneNumberUtil.isMobile(phoneNo)) {
			throw new ValidationException("请输入有效的手机号码");
		}
		// 确保手机号码未被注册使用
		boolean exists = userService.existsUserByPhoneNo(phoneNo);
		if (exists) {
			throw new ValidationException("手机号已存在");
		}
		// 发送验证码
		String reqIp = WebUtil.getIpAddrFromRequest(request);
		SmsUsage tplName = SmsUsage.regist;
		// 判断发送短信的数量是否超出限制
		if (!sendSmsByLimit(request, reqIp, tplName)) {
			throw new ValidationException("您今天发送短信的数量已经达到上限！");
		}
		boolean success = sendSmsVerfCode(tplName, phoneNo, SmsCodes.REGIST, reqIp);
		//
		result.data = success;
		if (success) {
			result.message = "短信验证码已发送";
		} else {
			result.type = Type.warn;
			result.message = "短信验证码发送失败";
		}
		//
		return result;
	}

	@Remark("发送短信验证码，找回登录密码用")
	@RequestMapping(value = "/sms/send/for/logPass/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> sendSmsForLogPass(HttpServletRequest request, @RequestBody MapContext requstData, HttpSession session) {
		Result<Boolean> result = Result.newOne();
		// 如果有图形验证码，检查图形验证码的有效性
		String chkCode = requstData.getTypedValue("chkCode", String.class);
		if (StrUtil.hasText(chkCode)) {
			if (!CheckCodeHelper.isValidCode(request.getSession(true), chkCode)) {
				throw new ValidationException("请输入正确的图形验证码");
			}
		}
		// 验证手机号码的有效性
		String phoneNo = requstData.getTypedValue("phoneNo", String.class);
		if (!PhoneNumberUtil.isMobile(phoneNo)) {
			throw new ValidationException("请输入有效的手机号码");
		}
		// 确保手机号码未被注册使用
		boolean exists = userService.existsUserByPhoneNo(phoneNo);
		if (!exists) {
			throw new ValidationException("手机号不存在");
		}
		// 发送验证码
		String reqIp = WebUtil.getIpAddrFromRequest(request);
		SmsUsage tplName = SmsUsage.logPass;
		// 判断发送短信的数量是否超出限制
		if (!sendSmsByLimit(request, reqIp, tplName)) {
			throw new ValidationException("您今天发送短信的数量已经达到上限！");
		}
		boolean success = sendSmsVerfCode(tplName, phoneNo, SmsCodes.LOG_PASS, reqIp);
		//
		result.data = success;
		if (success) {
			result.message = "短信验证码已发送";
		} else {
			result.type = Type.warn;
			result.message = "短信验证码发送失败";
		}
		//
		return result;
	}

	/**
	 * 发送重置支付密码短信验证码
	 * 
	 * @author 郝江奎
	 * @date 2015年11月12日 上午10:04:26
	 * 
	 * @return
	 */
	@Remark("发送重置支付密码短信验证码")
	@RequestMapping(value = "/sms/send/for/payPass/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> sendSmsForPayPass(HttpServletRequest request, @RequestBody MapContext requstData) {
		Result<Boolean> result = Result.newOne();
		// 如果有图形验证码，检查图形验证码的有效性
		String chkCode = requstData.getTypedValue("chkCode", String.class);
		if (StrUtil.hasText(chkCode)) {
			if (!CheckCodeHelper.isValidCode(request.getSession(true), chkCode)) {
				throw new ValidationException("请输入正确的图形验证码");
			}
		}
		UserContext userContext = getUserContext(request);
		// 验证手机号码的有效性
		String phoneNo = userContext.getPhoneNo();
		if (!PhoneNumberUtil.isMobile(phoneNo)) {
			throw new ValidationException("请输入有效的手机号码");
		}
		// 确保手机号码未被注册使用
		boolean exists = userService.existsUserByPhoneNo(phoneNo);
		if (!exists) {
			throw new ValidationException("手机号不存在");
		}
		// 发送验证码
		String reqIp = WebUtil.getIpAddrFromRequest(request);
		SmsUsage tplName = SmsUsage.payPass;
		// 判断发送短信的数量是否超出限制
		if (!sendSmsByLimit(request, reqIp, tplName)) {
			throw new ValidationException("您今天发送短信的数量已经达到上限！");
		}
		boolean success = sendSmsVerfCode(tplName, phoneNo, SmsCodes.PAY_PASS, reqIp);
		//
		result.data = success;
		if (success) {
			result.message = "短信验证码已发送";
		} else {
			result.type = Type.warn;
			result.message = "短信验证码发送失败";
		}
		//
		return result;
	}

	/**
	 * 发送一般用途短信验证码
	 * 
	 * @author 郝江奎
	 * @date 2015年11月12日 上午10:04:26
	 * 
	 * @return
	 */
	@Remark("发送一般用途短信验证码")
	@RequestMapping(value = "/sms/send/for/normal/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> sendSmsForNormal(HttpServletRequest request, @RequestBody MapContext requstData) {
		Result<Boolean> result = Result.newOne();
		// 如果有图形验证码，检查图形验证码的有效性
		String chkCode = requstData.getTypedValue("chkCode", String.class);
		if (StrUtil.hasText(chkCode)) {
			if (!CheckCodeHelper.isValidCode(request.getSession(true), chkCode)) {
				throw new ValidationException("请输入正确的图形验证码");
			}
		}
		UserContext userContext = getUserContext(request);
		// 验证手机号码的有效性
		String phoneNo = userContext.getPhoneNo();
		if (!PhoneNumberUtil.isMobile(phoneNo)) {
			throw new ValidationException("请输入有效的手机号码");
		}
		// 确保手机号码未被注册使用
		boolean exists = userService.existsUserByPhoneNo(phoneNo);
		if (!exists) {
			throw new ValidationException("手机号不存在");
		}
		// 发送验证码
		String reqIp = WebUtil.getIpAddrFromRequest(request);
		SmsUsage tplName = SmsUsage.normal;
		boolean success = sendSmsVerfCode(tplName, phoneNo, SmsCodes.NORMAL, reqIp);
		//
		result.data = success;
		if (success) {
			result.message = "短信验证码已发送";
		} else {
			result.type = Type.warn;
			result.message = "短信验证码发送失败";
		}
		//
		return result;
	}

	/**
	 * 短信验证码验证
	 * 
	 * @author 毛智东
	 * @date 2015年7月3日 下午7:33:05
	 * 
	 * @param smsVerfCode
	 *            页面参数：ajax.data({phoneNo : "13311111111", vfCode : "123456", usage : "regist"});
	 * @return
	 */
	@Remark("短信验证码验证")
	@RequestMapping(value = "/sms/verf/code/valid", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> validSmsVerfCode(@RequestBody SmsVerfCode smsVerfCode) {
		Result<Boolean> result = Result.newOne();
		result.data = settingService.validSmsVerfCode(smsVerfCode);
		return result;
	}

	/**
	 * 图形验证码验证
	 * 
	 * @author 毛智东
	 * @date 2015年7月9日 下午5:51:35
	 * 
	 * @param request
	 * @param session
	 * @param verfCode
	 * @return
	 */
	@Remark("图形验证码验证")
	@RequestMapping(value = "/img/verf/code/valid", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> validImgVerfCode(HttpServletRequest request, HttpSession session, @RequestBody String verfCode) {
		Result<Boolean> result = Result.newOne();
		result.data = CheckCodeHelper.isValidCode(session, verfCode);
		return result;
	}
}
