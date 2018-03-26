package priv.starfish.mall.manager.controller;

import freemarker.template.Template;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.base.TypedField;
import priv.starfish.common.model.*;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.sms.SmsErrorCode;
import priv.starfish.common.sms.SmsMessage;
import priv.starfish.common.util.*;
import priv.starfish.mall.agent.entity.Agent;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.mall.dto.MallDto;
import priv.starfish.mall.notify.dict.SmsUsage;
import priv.starfish.mall.notify.entity.SmsVerfCode;
import priv.starfish.mall.service.AgencyService;
import priv.starfish.mall.service.AgentService;
import priv.starfish.mall.service.BaseConst.SmsCodes;
import priv.starfish.mall.service.SettingService;
import priv.starfish.mall.service.UserService;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.web.base.CacheHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 代理商管理 控制层
 * 
 * @author WJJ
 * @date 2015年9月10日 下午4:21:58
 *
 */

@Controller
@RequestMapping(value = "/agent")
public class AgentController extends BaseController {

	@Resource
	private AgentService agentService;

	@Resource
	private UserService userService;

	@Resource
	private AgencyService agencyService;

	@Resource
	SettingService settingService;

	/**
	 * 代理商列表页面
	 * 
	 * @author WJJ
	 * @date 2015年9月10日 下午4:24:08
	 * 
	 * @return
	 */
	@Remark("代理商列表页面")
	@RequestMapping(value = "/list/jsp", method = RequestMethod.GET)
	public String toAgentJsp() {
		return "agent/agentList";
	}

	/**
	 * 分页查询代理商
	 * 
	 * @author WJJ
	 * @date 2015年9月10日 下午4:25:02
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询代理商")
	@RequestMapping(value = "/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<Agent> getAgentList(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<Agent> paginatedList = agentService.getAgentsByFilter(paginatedFilter);
		JqGridPage<Agent> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 增加代理商
	 * 
	 * @author WJJ
	 * @date 2015年9月10日 下午4:25:50
	 * 
	 * @param request
	 * @param agent
	 * @return
	 */
	@Remark("增加代理商")
	@RequestMapping(value = "/add/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<Agent> addAgent(HttpServletRequest request, @RequestBody Agent agent) {
		//
		Result<Agent> result = Result.newOne();
		boolean ok = agentService.saveAgent(agent);
		result.message = "保存成功!";
		result.data = agent;
		//
		if (!ok) {
			result.type = Type.warn;
			result.message = "保存失败!";
		}
		//
		return result;
	}

	/**
	 * 更新代理商,包括代理商启用禁用
	 * 
	 * @author WJJ
	 * @date 2015年9月10日 下午4:26:56
	 * 
	 * @param request
	 * @param agent
	 * @return
	 */
	@Remark("更新代理商")
	@RequestMapping(value = "/update/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<Agent> updateAgent(HttpServletRequest request, @RequestBody Agent agent) {
		//
		Result<Agent> result = Result.newOne();
		boolean ok = agentService.updateAgent(agent);
		result.message = "更新成功!";
		result.data = agent;
		//
		if (!ok) {
			result.type = Type.error;
			result.message = "更新失败!";
		}
		//
		return result;
	}

	/*@Remark("代理商禁用启用")
	@RequestMapping(value = "/isDisabled/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Agent> disableAgent(HttpServletRequest request, @RequestBody Agent agent) {
		//
		Result<Agent> result = Result.newOne();
		boolean ok = agentService.updateAgent(agent);
		if (ok) {
			result.message = "操作成功";
		} else {
			result.message = "操作失败";
			result.type = Type.error;
		}
		return result;
	}*/

	/**
	 * 通过代理商Id删除代理商及关联数据
	 * 
	 * @author WJJ
	 * @date 2015年9月10日 下午4:27:24
	 * 
	 * @param requestData
	 * @return
	 */
	@Remark("通过代理商Id删除代理商及关联数据")
	@RequestMapping(value = "/delete/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<?> delAgentById(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		Integer agentId = requestData.getTypedValue("id", Integer.class);
		if (agentId != null) {
			Boolean flag = agentService.deleteAgentById(agentId);
			if (flag == true) {
				result.message = "删除成功!";
			} else {
				result.message = "删除失败";
				result.type = Result.Type.error;
			}
		} else {
			result.message = "数据异常";
			result.type = Result.Type.warn;
		}

		return result;
	}

	/**
	 * 通过代理商Id列表删除多个代理商及关联数据
	 * 
	 * @author WJJ
	 * @date 2015年9月10日 下午4:27:58
	 * 
	 * @param requestData
	 * @return
	 */
	@Remark("通过代理商Id列表删除多个代理商及关联数据")
	@RequestMapping(value = "/delete/by/ids", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<?> delAgentsByIds(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		@SuppressWarnings("unchecked")
		List<Integer> agentIds = requestData.getTypedValue("ids", TypeUtil.Types.IntegerList.getClass());
		if (agentIds != null) {
			Boolean flag = agentService.deleteAgentsByIds(agentIds);
			if (flag == true) {
				result.message = "删除成功!";
			} else {
				result.message = "删除失败";
				result.type = Result.Type.error;
			}
		} else {
			result.message = "数据异常";
			result.type = Result.Type.warn;
		}

		return result;
	}

	/**
	 * 添加代理商时 校验手机号
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 下午1:40:59
	 * 
	 * @param requestData
	 * @return Result<Map<String,Object>> {"user":user}
	 */
	@Remark("添加代理商时 验证手机号")
	@RequestMapping(value = "/exist/by/phoneNo/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<String, Object>> getAgentExistByPhoneNo(@RequestBody MapContext requestData) {
		Result<Map<String, Object>> result = Result.newOne();
		String phoneNo = requestData.getTypedValue("phoneNo", String.class);
		// {"user":user}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		// 验证手机号是否已备注册
		if (StrUtil.hasText(phoneNo)) {
			User user = userService.getUserByPhoneNo(phoneNo);
			if (user != null) {
				dataMap.put("userFlag", true);
				dataMap.put("user", user);
				// 验证系统用户是否为代理商
				Agent agent = agentService.getAgentById(user.getId());
				if (agent != null) {
					dataMap.put("agentFlag", true);
				} else {
					// 是系统用户，非代理商
					dataMap.put("agentFlag", false);
				}

			} else {
				// 系统用户不存在
				dataMap.put("userFlag", false);
			}

		}
		//
		result.data = dataMap;
		return result;
	}

	/**
	 * 设置导入导出列信息
	 * 
	 * @author WJJ
	 * @date 2015年9月10日 下午4:28:58
	 * 
	 * @return
	 */
	List<TypedField> setXlsColums() {
		// 列配置信息
		List<TypedField> columns = new ArrayList<TypedField>();
		TypedField col = new TypedField("代理商编号", "num");
		columns.add(col);

		col = new TypedField("代理商昵称", "str");
		columns.add(col);

		col = new TypedField("代理商姓名", "str");
		columns.add(col);

		col = new TypedField("手机号码", "str");
		columns.add(col);

		col = new TypedField("身份证号", "str");
		columns.add(col);

		col = new TypedField("银行名称", "str");
		columns.add(col);

		col = new TypedField("银行账户", "str");
		columns.add(col);

		col = new TypedField("开户名", "str");
		columns.add(col);

		col = new TypedField("是否可用", "bool", "是/否");
		columns.add(col);

		col = new TypedField("注册时间", "date", "yyyy'年'M'月'd'日'H'点'm'分'");
		columns.add(col);

		col = new TypedField("备注", "str");
		columns.add(col);

		return columns;
	}

	/**
	 * 设置导入匹配字段
	 * 
	 * @author WJJ
	 * @date 2015年9月10日 下午4:29:08
	 * 
	 * @return
	 */
	Map<String, String> setAgentColums() {
		// 列配置信息
		Map<String, String> columns = new HashMap<String, String>();
		columns.put("代理商编号", "id");
		columns.put("代理商昵称", "nickName");
		columns.put("代理商姓名", "realName");
		columns.put("手机号码", "phoneNo");
		columns.put("身份证号", "idCertNo");
		columns.put("银行名称", "bankName");
		columns.put("银行账户", "acctNo");
		columns.put("开户名", "acctName");
		columns.put("是否可用", "disabled");
		columns.put("注册时间", "regTime");
		columns.put("备注", "memo");

		return columns;
	}

	/**
	 * 代理商入驻页面 TODO 此页面还没做
	 * 
	 * @author WJJ
	 * @date 2015年9月10日 下午4:29:22
	 * 
	 * @return
	 */
	@Remark("代理商入驻页面")
	@RequestMapping(value = "/entering/jsp", method = RequestMethod.GET)
	public String toAgentSettledJsp() {
		return "agent/agentSettled";
	}

	/**
	 * 通过手机号码获取代理商信息
	 * 
	 * @author WJJ
	 * @date 2015年9月10日 下午4:29:31
	 * 
	 * @param requestData
	 * @return
	 */
	@Remark("通过手机号码获取代理商信息")
	@RequestMapping(value = "/settled/get/by/phoneNo", method = RequestMethod.POST)
	@ResponseBody
	public Result<Agent> getAgentByPhoneNo(@RequestBody MapContext requestData) {
		Result<Agent> result = Result.newOne();
		String phoneNo = requestData.getTypedValue("phoneNo", String.class);
		if (phoneNo != null) {
			User user = userService.getUserByPhoneNo(phoneNo);
			if (user != null) {
				result.data = agentService.getAgentById(user.getId());
			}
		}
		return result;
	}

	/**
	 * 发送代理商入驻短信验证码
	 * 
	 * @author WJJ
	 * @date 2015年9月10日 下午4:29:41
	 * 
	 * @param request
	 * @param phoneNo
	 * @return
	 */
	@Remark("发送代理商入驻短信验证码")
	@RequestMapping(value = "/settled/sms/code/send", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> sendSmsCode(HttpServletRequest request, @RequestBody String phoneNo) {
		Result<Boolean> result = Result.newOne();
		String reqIp = WebUtil.getIpAddrFromRequest(request);
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

			smsVerfCode.setSendOk(true);
			smsVerfCode.setInvalid(false);
			result.data = true;
		} catch (Exception e) {
			e.printStackTrace();
			result.data = false;
		} finally {
			settingService.saveSmsVerfCode(smsVerfCode);
		}
		return result;
	}

}
