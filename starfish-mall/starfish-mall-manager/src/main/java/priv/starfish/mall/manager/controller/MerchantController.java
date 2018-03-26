package priv.starfish.mall.manager.controller;

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
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.common.xport.XlsExportor;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.comn.entity.UserAccount;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.merchant.dto.MerchantDto;
import priv.starfish.mall.merchant.entity.Merchant;
import priv.starfish.mall.merchant.entity.MerchantSettleAcct;
import priv.starfish.mall.service.*;
import priv.starfish.mall.settle.entity.SettleWay;
import priv.starfish.mall.shop.dto.ShopDto;
import priv.starfish.mall.shop.entity.Shop;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/merch")
public class MerchantController extends BaseController {
	@Resource
	private MerchantService merchService;

	@Resource
	private UserService userService;

	@Resource
	private ShopService shopService;

	@Resource
	SettingService settingService;

	@Resource
	private UserAccountService userAccountService;

	/**
	 * 返回商户列表页面
	 * 
	 * @author 廖晓远
	 * @date 2015-5-19 上午10:34:39
	 * 
	 * @return
	 */
	@Remark("商户列表页面")
	@RequestMapping(value = "/list/jsp", method = RequestMethod.GET)
	public String toMerchantJsp() {
		return "merchant/merchMgmt";
	}

	/**
	 * 添加商户
	 * 
	 * @author 郝江奎
	 * @date 2015-10-16 下午8:41:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("增加商户")
	@RequestMapping(value = "/add/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<?> addMerchant(HttpServletRequest request, @RequestBody MerchantDto merchantDto) {
		Result<?> result = Result.newOne();
		boolean ok = false;
		ok = merchService.saveMerchant(merchantDto);
		//
		if (ok) {
			result.message = "添加成功!";
		} else {
			result.type = Type.warn;
			result.message = "添加失败";
		}

		return result;
	}

	/**
	 * 修改商户
	 * 
	 * @author 郝江奎
	 * @date 2015-10-18 下午4:41:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("修改商户")
	@RequestMapping(value = "/update/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<?> updateMerchant(HttpServletRequest request, @RequestBody Merchant merchant) {
		Result<?> result = Result.newOne();
		boolean ok = false;
		ok = merchService.updateMerchant(merchant);
		//
		if (ok) {
			result.message = "修改成功!";
		} else {
			result.type = Type.warn;
			result.message = "修改失败";
		}

		return result;
	}

	@Remark("商户禁用启用")
	@RequestMapping(value = "/disalbed/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> disableMerchant(HttpServletRequest request, @RequestBody Merchant merchant) {
		Result<?> result = Result.newOne();
		boolean disabled = merchant.getDisabled() == false ? true : false;
		merchant.setDisabled(disabled);
		boolean flag = merchService.updateMerchant(merchant);
		if (flag) {
			result.message = "操作成功";
		} else {
			result.message = "操作失败";
			result.type = Result.Type.error;
		}
		return result;
	}

	/**
	 * 分页查询商户
	 * 
	 * @author 廖晓远
	 * @date 2015-5-21 上午9:53:18
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询商户")
	@RequestMapping(value = "/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<Merchant> getMerchantList(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<Merchant> paginatedList = merchService.getMerchants(paginatedFilter);
		JqGridPage<Merchant> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 通过商户Id删除商户及关联数据
	 * 
	 * @author 廖晓远
	 * @date 2015-5-21 下午6:17:05
	 * 
	 * @return
	 */
	@Remark("通过商户Id删除商户及关联数据")
	@RequestMapping(value = "/delete/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<?> delMerchantById(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		Integer merchId = requestData.getTypedValue("id", Integer.class);
		if (merchId != null) {
			Boolean flag = merchService.delteMerchantById(merchId);
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
	 * 通过商户Id列表删除商户及关联数据
	 * 
	 * @author 廖晓远
	 * @date 2015-5-21 下午6:31:06
	 * 
	 * @param requestData
	 * @return
	 */
	@Remark("通过商户Id列表删除商户及关联数据")
	@RequestMapping(value = "/delete/by/ids", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<?> delMerchantByIds(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		@SuppressWarnings("unchecked")
		List<String> merchIds = requestData.getTypedValue("ids", TypeUtil.Types.StringList.getClass());
		if (merchIds != null) {
			Boolean flag = merchService.deleteMerchantByIds(merchIds);
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
	public Result<?> getMerchExistByPhoneNo(@RequestBody MapContext requestData) {
		Result<Map<String, Object>> result = Result.newOne();
		String phoneNo = requestData.getTypedValue("phoneNo", String.class);
		if (phoneNo != null) {
			Map<String, Object> map = new HashMap<String, Object>(0);
			User user = userService.getUserByPhoneNo(phoneNo);
			if (user != null) {
				map.put("userFlag", true);
				map.put("user", user);
				Merchant merchant = merchService.getMerchantById(user.getId());
				if (merchant != null) {
					map.put("merchFlag", true);
				} else {
					map.put("merchFlag", false);
				}
			} else {
				map.put("userFlag", false);
			}
			result.data = map;
		}
		return result;
	}

	/**
	 * 通过查询条件导出商户及关联数据
	 * 
	 * @author 廖晓远
	 * @date 2015-5-22 下午2:56:33
	 * 
	 * @param requestData
	 * @return
	 */
	@Remark("通过查询条件导出商户及关联数据")
	@RequestMapping(value = "/xls/export/do", method = RequestMethod.GET)
	public void exportMerchantXls(HttpServletResponse response, @RequestBody MapContext requestData) throws IOException {
		String fileName = "商户信息";
		String dataName = "商户信息";

		List<Map<String, Object>> dataRows = new ArrayList<Map<String, Object>>();
		List<Merchant> merchants = merchService.getAllMerchant(requestData);
		// 列配置信息
		List<TypedField> columns = setXlsColums();

		for (Merchant merchant : merchants) {
			Map<String, Object> dataRow = new HashMap<String, Object>();
			User user = merchant.getUser();
			dataRow.put("商户编号", merchant.getId());
			dataRow.put("商户昵称", user.getNickName());
			dataRow.put("商户姓名", user.getRealName());
			dataRow.put("手机号码", user.getPhoneNo());
			dataRow.put("身份证号", user.getIdCertNo());
			dataRow.put("是否可用", merchant.getDisabled());
			dataRow.put("注册时间", user.getRegTime());
			dataRow.put("备注", merchant.getMemo());
			dataRows.add(dataRow);
		}

		try {
			XlsExportor.writeDataTo(response, fileName, dataName, TypeUtil.listToArray(columns, TypedField.class), dataRows);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置导入导出列信息
	 * 
	 * @author 廖晓远
	 * @date 2015-5-23 下午4:25:23
	 * 
	 * @return
	 */
	List<TypedField> setXlsColums() {
		// 列配置信息
		List<TypedField> columns = new ArrayList<TypedField>();
		TypedField col = new TypedField("商户编号", "num");
		columns.add(col);

		col = new TypedField("商户昵称", "str");
		columns.add(col);

		col = new TypedField("商户姓名", "str");
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
	 * @author 廖晓远
	 * @date 2015-5-23 下午6:47:35
	 * 
	 * @return
	 */
	Map<String, String> setMerchColums() {
		// 列配置信息
		Map<String, String> columns = new HashMap<String, String>();
		columns.put("商户编号", "id");
		columns.put("商户昵称", "nickName");
		columns.put("商户姓名", "realName");
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
	 * 返回商户入驻页面
	 * 
	 * @author 廖晓远
	 * @date 2015年7月1日 下午6:49:51
	 * 
	 * @return
	 */
	@Remark("商户入驻页面")
	@RequestMapping(value = "/entering/jsp", method = RequestMethod.GET)
	public String toMerchantSettledJsp() {
		return "merchant/merchSettling";
	}

	/**
	 * 通过手机号码获取商户信息
	 * 
	 * @author 廖晓远
	 * @date 2015年7月3日 下午12:09:01
	 * 
	 * @param requestData
	 * @return
	 */
	@Remark("通过手机号码获取商户信息")
	@RequestMapping(value = "/get/by/phoneNo/", method = RequestMethod.POST)
	@ResponseBody
	public Result<Merchant> getMerchByPhoneNo(@RequestBody MapContext requestData) {
		Result<Merchant> result = Result.newOne();
		String phoneNo = requestData.getTypedValue("phoneNo", String.class);
		if (phoneNo != null) {
			result.data = merchService.getMerchantByPhoneNo(phoneNo);
		}
		return result;
	}

	// /**
	// * 发送商户入驻短信验证码
	// *
	// * @author 廖晓远
	// * @date 2015年7月6日 上午10:04:11
	// *
	// * @param request
	// * @param phoneNo
	// * @return
	// */
	// @Remark("发送商户入驻短信验证码")
	// @RequestMapping(value = "/settled/sms/code/send", method = RequestMethod.POST)
	// @ResponseBody
	// public Result<Boolean> sendSmsCode(HttpServletRequest request, @RequestBody String phoneNo) {
	// Result<Boolean> result = Result.newOne();
	// String reqIp = WebUtil.getIpAddrFromRequest(request);
	// String vfCode = CodeUtil.randomNumCode(6);
	// SmsVerfCode smsVerfCode = new SmsVerfCode();
	// smsVerfCode.setPhoneNo(phoneNo);
	// smsVerfCode.setReqIp(reqIp);
	// smsVerfCode.setUsage(SmsUsage.regist);
	// smsVerfCode.setVfCode(vfCode);
	// smsVerfCode.setSendTime(new Date());
	// smsVerfCode.setExpireTime(DateUtil.addMinutes(5));
	// smsVerfCode.setSendOk(false);
	// smsVerfCode.setInvalid(true);
	// try {
	// Template template = freeMarkerService.getTemplate("sms", "sms.regist");
	// Map<String, Object> model = new HashMap<String, Object>();
	// model.put("code", vfCode);
	// model.put("company", "融芯思联");
	// String smsText = freeMarkerService.renderContent(template, model);
	// SmsMessage message = new SmsMessage();
	// message.setReceiverNumber(phoneNo);
	// message.setText(smsText);
	// smsVerfCode.setContent(smsText);
	//
	// SmsErrorCode smsErrorCode = smsService.sendSms(message, null);
	// if (smsErrorCode == SmsErrorCode.OK) {
	// result.data = true;
	// smsVerfCode.setSendOk(true);
	// smsVerfCode.setInvalid(false);
	// } else {
	// result.data = false;
	// }
	//
	// smsVerfCode.setSendOk(true);
	// smsVerfCode.setInvalid(false);
	// result.data = true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// result.data = false;
	// } finally {
	// settingService.saveSmsVerfCode(smsVerfCode);
	// }
	// return result;
	// }

	/**
	 * 
	 * 获取用户资金账户
	 * 
	 * @author guoyn
	 * @date 2015年10月9日 下午6:35:07
	 * 
	 * @param request
	 * @return JqGridPage<UserAccount>
	 */
	@Remark("获取用户资金账户")
	@RequestMapping(value = "/userAccount/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<UserAccount> getUserAccountList(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<UserAccount> paginatedList = userAccountService.getUserAccountsByFilter(paginatedFilter);
		//
		JqGridPage<UserAccount> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}
	
	/**
	 * 获取用户结算资金账户
	 * 
	 * @author "WJJ"
	 * @date 2016年1月11日 上午10:12:11
	 * 
	 * @param request
	 * @return
	 */
	@Remark("获取用户结算资金账户")
	@RequestMapping(value = "/merchantSettleAcct/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<MerchantSettleAcct> getMerchantSettleAcctList(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<MerchantSettleAcct> paginatedList = userAccountService.getMerchantSettleAcctsByFilter(paginatedFilter);
		//
		JqGridPage<MerchantSettleAcct> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	@Remark("验证商户的店铺名称是否存在")
	@RequestMapping(value = "/shop/exist/by/name", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> exsitsShopByName(@RequestBody MapContext requestData, HttpServletRequest request) {
		//
		Result<Boolean> result = Result.newOne();
		//
		UserContext userContext = getUserContext(request);
		Integer merchantId = userContext.getUserId();
		//
		String shopName = requestData.getTypedValue("name", String.class);
		boolean isExsit = false;
		Shop shop = shopService.getShopByMerchantIdAndName(merchantId, shopName);
		if (shop != null)
			isExsit = true;
		result.data = isExsit;
		return result;
	}

	/**
	 * 申请商户入驻
	 * 
	 * @author guoyn
	 * @date 2015年10月8日 下午2:27:22
	 * 
	 * @param request
	 * @return Result<?>
	 */
	@Remark("申请商户入驻")
	@RequestMapping(value = "/settled/apply/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<?> applyMerchant(HttpServletRequest request, @RequestBody ShopDto shopDto) {
		Result<?> result = Result.newOne();
		boolean ok = false;
		//
		UserContext userContext = getUserContext(request);
		Integer userId = userContext.getUserId();
		// Integer userId = 1;
		//
		if (userId == null) {
			result.type = Type.warn;
			result.message = "请先登录!";
		} else {
			Integer merchantId = shopDto.getMerchantId();
			if (merchantId == null) {
				Merchant merchant = shopDto.getMerchant();
				merchant.setId(userId);
				merchant.setDisabled(false);
			}
			//
			ok = merchService.settledMerchant(shopDto);
			//
			if (ok) {
				result.message = "申请成功!";
			} else {
				result.type = Type.warn;
				result.message = "申请失败";
			}
		}
		return result;
	}

	@Remark("验证商户是否存在")
	@RequestMapping(value = "/exist/by/userId", method = RequestMethod.POST)
	@ResponseBody
	public Result<Merchant> isExsitMerchant(HttpServletRequest request) {
		//
		Result<Merchant> result = Result.newOne();
		//
		UserContext userContext = getUserContext(request);
		Integer userId = userContext.getUserId();
		// Integer userId = 1;
		//
		Merchant merchant = merchService.getMerchantById(userId);
		if (merchant != null)
			result.data = merchant;
		//
		return result;
	}

	// TODO--------wjj----------begin--------------------- 商户资金结算 ----------------------------------------------
	/**
	 * 商户资金结算页面
	 * 
	 * @author WJJ
	 * @date 2015年9月25日 上午11:54:40
	 * 
	 * @return
	 */
	@Remark("商户资金结算页面")
	@RequestMapping(value = "/settle/jsp", method = RequestMethod.GET)
	public String toCapitalSettlePage() {
		return "merchant/capitalSettle";
	}

	// --------wjj----------end--------------------- 商户资金结算 ----------------------------------------------

	// TODO--------wjj----------begin--------------------- 商户操作日志 ----------------------------------------------
	/**
	 * 商户操作日志
	 * 
	 * @author WJJ
	 * @date 2015年9月25日 上午11:54:40
	 * 
	 * @return
	 */
	@Remark("商户操作日志")
	@RequestMapping(value = "/operation/log/jsp", method = RequestMethod.GET)
	public String toOperationLogPage() {
		return "merchant/operationLog";
	}

	// --------wjj----------end--------------------- 商户操作日志 ----------------------------------------------
	// TODO--------郝江奎----------begin--------------------- 结算资金账户 ----------------------------------------------

	/**
	 * 绑定商户结算账户
	 * 
	 * @author 郝江奎
	 * @date 2015年11月3日 上午11:27:22
	 * 
	 * @param request
	 * @return Result<?>
	 */
	@Remark("绑定商户结算账户")
	@RequestMapping(value = "/settle/acct/bind/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<MerchantSettleAcct> addMerchantSettleAcct(HttpServletRequest request, @RequestBody MerchantSettleAcct merchantSettleAcct) {
		Result<MerchantSettleAcct> result = Result.newOne();
		boolean ok = false;
		Integer merchantId = merchantSettleAcct.getMerchantId();
		String settleWayCode = merchantSettleAcct.getSettleWayCode();
		SettleWay settleWay = merchService.getSettleWayBySettleWayCode(settleWayCode);

		MerchantSettleAcct MerchantSettleAcctList = merchService.getByMerchantIdAndSettleWayCode(merchantId, settleWayCode);
		if (MerchantSettleAcctList == null) {
			merchantSettleAcct.setSettleX(settleWay.getSettleX());
			ok = merchService.creatMerchantSettleAcct(merchantSettleAcct);
			//
			if (ok) {
				result.message = "绑定成功!";
			} else {
				result.type = Type.warn;
				result.message = "绑定失败";
			}
		} else {
			result.type = Type.warn;
			result.message = "此账户已绑定";
		}
		return result;
	}

	/**
	 * 清除商户结算账户
	 * 
	 * @author 郝江奎
	 * @date 2015年11月3日 上午11:27:22
	 * 
	 * @param request
	 * @return Result<?>
	 */
	@Remark("清除商户结算账户")
	@RequestMapping(value = "/settle/acct/unbind/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<MerchantSettleAcct> delMerchantSettleAcctByData(HttpServletRequest request, @RequestBody MerchantSettleAcct merchantSettleAcct) {
		Result<MerchantSettleAcct> result = Result.newOne();
		boolean ok = false;
		ok = merchService.deleteMerchantSettleAcct(merchantSettleAcct);
		//
		if (ok) {
			result.data = merchantSettleAcct;
			result.message = "清除成功!";
		} else {
			result.type = Type.warn;
			result.message = "清除失败";
		}
		return result;
	}
	
	/**
	 * 修改商户结算账户
	 * 
	 * @author 郝江奎
	 * @date 2015年11月3日 上午11:27:22
	 * 
	 * @param request
	 * @return Result<?>
	 */
	@Remark("修改商户结算账户")
	@RequestMapping(value = "/settle/acct/update/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<MerchantSettleAcct> updateMerchantSettleAcct(HttpServletRequest request, @RequestBody MerchantSettleAcct merchantSettleAcct) {
		Result<MerchantSettleAcct> result = Result.newOne();
		boolean ok = false;
		
		ok = merchService.updateMerchantSettleAcct(merchantSettleAcct);
		//
		if (ok) {
			result.message = "修改成功!";
		} else {
			result.type = Type.warn;
			result.message = "修改失败";
		}
		return result;
	}

}
