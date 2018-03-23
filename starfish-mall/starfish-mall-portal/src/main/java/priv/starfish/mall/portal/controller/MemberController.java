package priv.starfish.mall.portal.controller;

import freemarker.template.Template;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.Result;
import priv.starfish.common.sms.SmsErrorCode;
import priv.starfish.common.sms.SmsMessage;
import priv.starfish.common.util.*;
import priv.starfish.mall.comn.dict.VerfAspect;
import priv.starfish.mall.comn.dto.UserDto;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.mall.dto.MallDto;
import priv.starfish.mall.member.entity.Member;
import priv.starfish.mall.notify.dict.SmsUsage;
import priv.starfish.mall.notify.entity.SmsVerfCode;
import priv.starfish.mall.service.BaseConst.SmsCodes;
import priv.starfish.mall.service.MemberService;
import priv.starfish.mall.service.SettingService;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.web.base.CacheHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Remark("会员Controller")
@Controller
@RequestMapping("/member")
public class MemberController extends BaseController {

	@Resource
	SettingService settingService;

	@Resource
	MemberService memberService;

	// -------------------------------------会员注册-------------------------------------------------

	/**
	 * 会员注册
	 * 
	 * @author 毛智东
	 * @date 2015年7月2日 上午10:10:21
	 * 
	 * @return
	 */
	@Remark("会员注册")
	@RequestMapping(value = "/regist/jsp", method = RequestMethod.GET)
	public String goMemberRegistJsp() {
		return "member/memberRegist";
	}

	/**
	 * 发送注册短信验证码
	 * 
	 * @author 毛智东
	 * @date 2015年7月2日 下午8:04:26
	 * 
	 * @param phoneNo
	 * @return
	 */
	@Remark("发送注册短信验证码")
	@RequestMapping(value = "/regist/sms/code/send", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> sendSmsCodeOfRegist(HttpServletRequest request, @RequestBody String phoneNo) {
		Result<Boolean> result = Result.newOne();
		String reqIp = request.getRemoteAddr();
		String vfCode = CodeUtil.randomNumCode(6);
		SmsVerfCode smsVerfCode = new SmsVerfCode();
		smsVerfCode.setPhoneNo(phoneNo);
		smsVerfCode.setReqIp(reqIp);
		smsVerfCode.setUsage(SmsUsage.regist);
		smsVerfCode.setVfCode(vfCode);
		smsVerfCode.setSendTime(new Date());
		smsVerfCode.setExpireTime(DateUtil.addMinutes(5));
		smsVerfCode.setSendOk(false);
		smsVerfCode.setInvalid(true);
		try {
			// 获取freeMarker测试邮件
			Template template = freeMarkerService.getTemplate("sms", SmsCodes.REGIST);
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("code", vfCode);
			MallDto mallInfo = CacheHelper.getMallInfo();
			model.put("company", mallInfo.getName());
			String smsText = freeMarkerService.renderContent(template, model);
			SmsMessage message = new SmsMessage();
			message.setReceiverNumber(phoneNo);
			message.setText(smsText);
			smsVerfCode.setContent(smsText);
			SmsErrorCode smsErrorCode = smsService.sendSms(message, null);
			if (smsErrorCode == SmsErrorCode.OK) {
				result.data = true;
				smsVerfCode.setSendOk(true);
				smsVerfCode.setInvalid(false);
			} else {
				result.data = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.data = false;
		} finally {
			settingService.saveSmsVerfCode(smsVerfCode);
		}
		return result;
	}

	/**
	 * 用户注册
	 * 
	 * @author 毛智东
	 * @date 2015年7月6日 下午6:28:15
	 * 
	 * @return
	 */
	@Remark("用户注册")
	@RequestMapping(value = "/regist/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> memberRegist(@RequestBody UserDto userDto) {
		Result<Boolean> result = Result.newOne();
		Member member = new Member();
		User user = new User();
		String password = userDto.getPassword();
		if (StrUtil.hasText(password)) {
			password = RSACrypter.decryptStringFromJs(password);
			userDto.setPassword(password);
		}
		String newPassword = userDto.getNewPassword();
		if (StrUtil.hasText(newPassword)) {
			newPassword = RSACrypter.decryptStringFromJs(newPassword);
			userDto.setNewPassword(newPassword);
		}
		String payPassword = userDto.getPayPassword();
		if (StrUtil.hasText(payPassword)) {
			payPassword = RSACrypter.decryptStringFromJs(payPassword);
			userDto.setPayPassword(payPassword);
		}
		TypeUtil.copyProperties(userDto, user);
		member.setUser(user);
		member.setDisabled(false);
		MapContext extra = MapContext.newOne();
		extra.put(VerfAspect.phoneNo.name(), true);
		result.data = memberService.memberRegist(member, extra);
		return result;
	}

}
