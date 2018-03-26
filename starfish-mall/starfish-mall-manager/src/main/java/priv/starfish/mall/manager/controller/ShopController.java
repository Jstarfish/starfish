package priv.starfish.mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.base.AppNodeCluster;
import priv.starfish.common.model.*;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.sms.SmsErrorCode;
import priv.starfish.common.sms.SmsResult;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.*;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.Permission;
import priv.starfish.mall.comn.entity.Role;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.mall.dto.MallDto;
import priv.starfish.mall.notify.dict.SmsUsage;
import priv.starfish.mall.notify.entity.SmsVerfCode;
import priv.starfish.mall.service.*;
import priv.starfish.mall.service.BaseConst.SmsCodes;
import priv.starfish.mall.shop.dto.ShopDto;
import priv.starfish.mall.shop.dto.ShopParamDto;
import priv.starfish.mall.shop.entity.*;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.web.base.CacheHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Remark("店铺Controller")
@Controller
@RequestMapping("/shop")
public class ShopController extends BaseController {
	@Resource
	private CarSvcService carSvcService;
	@Resource
	private ShopService shopService;
	@Resource
	private UserService userService;
	@Resource
	private SearchService searchService;

	// --------------------------------------- ShopGrade----------------------------------------------

	/**
	 * 转向店铺评分等级界面
	 * 
	 * @author zjl
	 * @date 2015年5月20日 下午4:50:51
	 * 
	 * @return String
	 */
	@Remark("店铺评分等级界面")
	@RequestMapping(value = "/shopGrade/list/jsp", method = RequestMethod.GET)
	public String toShopGradePage() {
		return "shop/shopGradeList";
	}

	/**
	 * 查询店铺评分等级
	 * 
	 * @author zjl
	 * @date 2015年5月20日 下午4:54:55
	 * 
	 * @param request
	 * @return JqGridPage<ShopGrade>
	 */
	@Remark("查询店铺评分等级")
	@RequestMapping(value = "/shopGrade/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<ShopGrade> listShopGrades(HttpServletRequest request) {
		PaginatedList<ShopGrade> paginatedList = shopService.getShopGrades();
		JqGridPage<ShopGrade> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 添加店铺
	 * 
	 * @author 郝江奎
	 * @date 2015-10-17 下午15:01:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("增加店铺")
	@RequestMapping(value = "/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> addMerchant(HttpServletRequest request, @RequestBody Shop shop) {
		Result<?> result = Result.newOne();
		boolean ok = false;
		ok = shopService.saveShop(shop);
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
	 * 修改店铺
	 * 
	 * @author 郝江奎
	 * @date 2015-10-18 上午11:01:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("修改店铺")
	@RequestMapping(value = "/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> updateMerchant(HttpServletRequest request, @RequestBody Shop shop) {
		Result<?> result = Result.newOne();
		boolean ok = false;
		ok = shopService.updateShop(shop);
		//
		if (ok) {
			result.message = "修改成功!";
		} else {
			result.type = Type.warn;
			result.message = "修改失败";
		}

		return result;
	}

	/**
	 * 更新店铺评分等级
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:26:14
	 * 
	 * @param shopGrade
	 * @return Result<?>
	 */
	@Remark("更新店铺评分等级")
	@RequestMapping(value = "/shopGrade/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> updateShopGrade(@RequestBody ShopGrade shopGrade) {
		Result<?> result = Result.newOne();
		boolean flag = shopService.updateShopGrades(shopGrade.getShopGrades());
		if (flag) {
			result.message = "更新成功";
		} else {
			result.message = "更新失败";
			result.type = Result.Type.error;
		}
		return result;
	}

	/**
	 * 验证最小分数和最大分数
	 * 
	 * @author zjl
	 * @date 2015年7月20日 下午5:29:25
	 * 
	 * @param shopGrade
	 * @return
	 */
	@Remark("验证最小分数和最大分数")
	@RequestMapping(value = "/shopGrade/points/isabel/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getShopGradeByPoints(@RequestBody ShopGrade shopGrade) {
		Result<Boolean> result = Result.newOne();
		boolean flag = true;
		List<ShopGrade> listShopGrade = shopService.getShopGradesByLowerAndUpperPoint(shopGrade.getLowerPoint(), shopGrade.getUpperPoint());
		if (listShopGrade.size() > 0) {
			if (listShopGrade.size() == 1) {
				if (listShopGrade.get(0).getId() == shopGrade.getId()) {
					flag = true;
				} else {
					flag = false;
				}
			} else {
				flag = false;
			}
		} else {
			flag = true;
		}
		result.data = flag;
		return result;
	}

	/**
	 * 验证分数是否在其他集合内
	 * 
	 * @author zjl
	 * @date 2015年7月20日 下午5:30:25
	 * 
	 * @param shopGrade
	 * @return
	 */
	@Remark("验证分数是否在其他集合内")
	@RequestMapping(value = "/shopGrade/point/isabel/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getShopGradeByPoint(@RequestBody ShopGrade shopGrade) {
		Result<Boolean> result = Result.newOne();
		boolean flag = true;
		List<ShopGrade> listShopGrade = shopService.getGradesByPoint(shopGrade.getLowerPoint());
		if (listShopGrade.size() > 0) {
			if (listShopGrade.size() == 1) {
				if (listShopGrade.get(0).getId() == shopGrade.getId()) {
					flag = true;
				} else {
					flag = false;
				}
			} else {
				flag = false;
			}
		} else {
			flag = true;
		}
		result.data = flag;
		return result;
	}

	// --------------------------------------- Shop ----------------------------------------------
	/**
	 * 转向店铺列表页面
	 * 
	 * @author zjl
	 * @date 2015年5月27日 下午3:04:34
	 * 
	 * @return String
	 */
	@Remark("店铺列表页面")
	@RequestMapping(value = "/list/jsp", method = RequestMethod.GET)
	public String toShopJsp() {
		return "shop/shopList";
	}

	/**
	 * 分页查询店铺
	 * 
	 * @author zjl
	 * @date 2015年5月20日 下午4:54:55
	 * 
	 * @param request
	 * @return JqGridPage<ShopGrade>
	 */
	@Remark("分页查询店铺")
	@RequestMapping(value = "/shop/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<ShopDto> listShops(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<ShopDto> paginatedList = shopService.getShops(paginatedFilter);
		JqGridPage<ShopDto> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 店铺禁用启用
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:26:14
	 * 
	 * @param request
	 * @return Result<?>
	 */
	@Remark("店铺禁用启用")
	@RequestMapping(value = "/shop/isDisabled/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> changeIsDisabled(HttpServletRequest request, @RequestBody Shop shop) {
		Result<?> result = Result.newOne();
		boolean disabled = shop.getDisabled() == false ? true : false;
		boolean flag = shopService.updateShopDisableState(shop.getId(), disabled);
		if (flag) {
			result.message = "操作成功";
		} else {
			result.message = "操作失败";
			result.type = Result.Type.error;
		}
		return result;
	}

	/**
	 * 店铺审核
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:26:14
	 * 
	 * @param request
	 * @return Result<?>
	 */
	@Remark("店铺审核")
	@RequestMapping(value = "/shop/audit/shop/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> auditShop(HttpServletRequest request, @RequestBody ShopAuditRec shopAuditRec) {
		UserContext userContext = getUserContext(request);
		Result<?> result = Result.newOne();
		boolean flag = false;
		if (userContext.isSysUser()) {
			flag = shopService.updateShopAudit(shopAuditRec, userContext.getUserId(), userContext.getUserName(), new Date(), shopAuditRec.getAuditStatus());
		}
		String reqIp = WebUtil.getIpAddrFromRequest(request);
		if (flag) {
			result.message = "操作成功";
		} else {
			result.message = "操作失败";
			result.type = Result.Type.error;
		}
		// 发送通知短信
		sendShopSmsVerfCode(SmsUsage.apply, shopAuditRec.getPhoneNo(), SmsCodes.SHOP_APPLY, reqIp, shopAuditRec);
		return result;
	}

	/**
	 * 批量审核店铺
	 * 
	 * @author zjl
	 * @date 2015年5月13日 下午2:55:28
	 * 
	 * @param request
	 * @param requestData
	 * @return Result<String>
	 */
	@Remark("批量审核店铺")
	@RequestMapping(value = "/shop/audit/shops/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> auditShops(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<String> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		@SuppressWarnings("unchecked")
		List<String> list = requestData.getTypedValue("ids", TypeUtil.Types.StringList.getClass());
		Integer auditStatus = requestData.getTypedValue("auditStatus", Integer.class);
		String desc = requestData.getTypedValue("desc", String.class);
		ShopAuditRec shopAuditRec = new ShopAuditRec();
		shopAuditRec.setAuditorId(userContext.getUserId());
		shopAuditRec.setAuditorName(userContext.getUserName());
		shopAuditRec.setAuditStatus(auditStatus);
		shopAuditRec.setAuditTime(new Date());
		shopAuditRec.setDesc(desc);
		boolean flag = shopService.batchUpdateShopsAudit(list, auditStatus, shopAuditRec);
		//
		String reqIp = WebUtil.getIpAddrFromRequest(request);
		for (String id : list) {
			ShopDto shopDto = shopService.getShopInfoById(Integer.parseInt(id));
			shopAuditRec.setShopName(shopDto.getShop().getName());
			shopAuditRec.setPhoneNo(shopDto.getMerchant().getUser().getPhoneNo());
			// 发送通知短信
			sendShopSmsVerfCode(SmsUsage.apply, shopAuditRec.getPhoneNo(), SmsCodes.SHOP_APPLY, reqIp, shopAuditRec);
		}

		if (flag) {
			result.message = "操作成功";
		} else {
			result.message = "操作失败";
			result.type = Result.Type.error;
		}
		return result;
	}

	/**
	 * 发送店铺审核结果通知短信
	 * 
	 * @author 郝江奎
	 * @date 2015年12月27日 下午15:18:12
	 * 
	 * @param usage
	 * @param phoneNo
	 * @param smsTplCode
	 * @param reqIp
	 * @param shopAuditRec
	 * @return
	 */
	protected boolean sendShopSmsVerfCode(SmsUsage usage, String phoneNo, String smsTplCode, String reqIp, ShopAuditRec shopAuditRec) {
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
			MapContext dataModel = MapContext.newOne();
			dataModel.put(BaseConst.TplModelVars.CODE, vfCode);
			MallDto mallInfo = CacheHelper.getMallInfo();
			dataModel.put(BaseConst.TplModelVars.COMPANY, mallInfo.getName());
			//
			dataModel.put("bizCode", usage.name());
			dataModel.put("sendTime", sendTime);
			dataModel.put("result", shopAuditRec.getAuditStatus() == 2 ? true : false);
			dataModel.put("shopName", shopAuditRec.getShopName());
			dataModel.put("backSiteUrl", AppNodeCluster.getCurrent().getAbsBaseUrlByRole("web-back"));
			dataModel.put("hotLineNo", "4000982198");
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

	/**
	 * 删除店铺
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:26:14
	 * 
	 * @param request
	 * @return Result<?>
	 */
	@Remark("删除店铺")
	@RequestMapping(value = "/shop/delete/by/id", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> delShopById(HttpServletRequest request, @RequestBody Shop shop) {
		Result<?> result = Result.newOne();
		boolean flag = shopService.deleteShopById(shop.getId());
		if (flag) {
			result.message = "删除成功";
		} else {
			result.message = "删除失败";
			result.type = Result.Type.error;
		}
		return result;
	}

	/**
	 * 根据IDs 删除站点模块
	 * 
	 * @author zjl
	 * @date 2015年5月13日 下午2:55:28
	 * 
	 * @param request
	 * @param requestData
	 * @return Result<String>
	 */
	@Remark("根据IDs 删除站点模块")
	@RequestMapping(value = "/shop/delete/by/ids/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> delShopsByIds(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<String> result = Result.newOne();
		@SuppressWarnings("unchecked")
		List<String> list = requestData.getTypedValue("ids", TypeUtil.Types.StringList.getClass());
		boolean flag = shopService.batchDeleteShops(list);
		if (flag) {
			result.message = "删除成功";
		} else {
			result.message = "删除失败";
			result.type = Result.Type.error;
		}
		return result;
	}

	/**
	 * 修改店铺
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:26:14
	 * 
	 * @param request
	 * @return Result<?>
	 */
	@Remark("修改店铺")
	@RequestMapping(value = "/shop/update/shop/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> updateShop(HttpServletRequest request, @RequestBody Shop shop) {
		Result<?> result = Result.newOne();
		boolean flag = shopService.updateShop(shop);
		if (flag) {
			result.message = "修改成功";
		} else {
			result.message = "修改失败";
			result.type = Result.Type.error;
		}
		return result;
	}

	/**
	 * 店铺名称是否存在
	 * 
	 * @author 廖晓远
	 * @date 2015年7月6日 下午6:27:32
	 * 
	 * @param name
	 * @return
	 */
	@Remark("店铺名称是否存在")
	@RequestMapping(value = "/exist/by/name/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getShopExistByName(@RequestBody String name) {
		Result<Boolean> result = Result.newOne();
		if (name != null) {
			result.data = (shopService.getShopByName(name) != null);
		}
		return result;
	}

	// -------------------------------------------店铺参数设置--------------------------------------------------------

	/**
	 * 店铺设置：配送方式设置页面
	 * 
	 * @author 毛智东
	 * @date 2015年6月6日 下午3:00:27
	 * 
	 * @return
	 */
	@Remark("店铺设置：配送方式设置页面")
	@RequestMapping(value = "/delivery/way/list/jsp", method = RequestMethod.GET)
	public String toDeliveryWayJsp() {
		return "shop/deliveryWayList";
	}

	/**
	 * 店铺设置：获取店铺参数
	 *
	 * @author 毛智东
	 * @date 2015年6月6日 下午2:57:03
	 *
	 * @param request
	 * @return
	 */
	@Remark("店铺设置：获取店铺参数")
	@RequestMapping(value = "/param/val/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<ShopParam> getShopParamVals(HttpServletRequest request, @RequestBody ShopParam shopParam) {
		UserContext userContext = getUserContext(request);
		//
		Result<ShopParam> result = Result.newOne();
		//
		String code = shopParam.getCode();
		Integer shopId = userContext.getScopeEntityId(AuthScope.shop.name());
		ShopParam param = shopService.getShopParamByShopIdAndCode(shopId, code);
		result.data = param;
		//
		return result;
	}

	/**
	 * 保存店铺参数
	 *
	 * @author 毛智东
	 * @date 2015年6月6日 下午5:37:03
	 *
	 * @param shopParamDto
	 * @return
	 */
	@Remark("保存店铺参数")
	@RequestMapping(value = "/param/val/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> saveShopParamDto(HttpServletRequest request, @RequestBody ShopParamDto shopParamDto) {
		UserContext userContext = getUserContext(request);
		//
		Result<?> result = Result.newOne();
		//
		shopParamDto.shopId = userContext.getScopeEntityId(AuthScope.shop.name());
		result.message = shopService.saveShopParamDto(shopParamDto) ? "保存成功！" : "保存失败！";
		//
		return result;
	}

	/**
	 * 删除参数值
	 *
	 * @author 毛智东
	 * @date 2015年6月6日 下午7:27:29
	 *
	 * @param shopParamDto
	 * @return
	 */
	@Remark("删除参数值")
	@RequestMapping(value = "/param/val/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteShopParam(HttpServletRequest request, @RequestBody ShopParamDto shopParamDto) {
		UserContext userContext = getUserContext(request);
		//
		Result<?> result = Result.newOne();
		//
		shopParamDto.shopId = userContext.getScopeEntityId(AuthScope.shop.name());
		result.message = shopService.saveShopParamDto(shopParamDto) ? "删除成功！" : "删除失败！";
		//
		return result;
	}

	// -------------------------------------------店铺基本信息--------------------------------------------------------
	/**
	 * 店铺基本信息界面
	 * 
	 * @author zjl
	 * @date 2015年6月6日 下午3:00:27
	 * 
	 * @return
	 */
	@Remark("店铺基本信息界面(店铺信息，店铺公告，发票设置，短信设置)")
	@RequestMapping(value = "/basic/info/jsp", method = RequestMethod.GET)
	public String toShopBasicInfoPage() {
		return "shop/shopMsg";
	}

	/**
	 * 获取店信息
	 * 
	 * @author zjl
	 * @date 2015年6月8日 下午7:19:48
	 * 
	 * @return
	 */
	@Remark("根据ID获取店铺信息")
	@RequestMapping(value = "/shop/get/by/id", method = RequestMethod.POST)
	@ResponseBody
	public Result<ShopDto> getShopById(HttpServletRequest request) {
		Result<ShopDto> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		String scope = userContext.getScope();
		Integer entityId = userContext.getScopeEntityId(scope);
		result.data = shopService.getShopInfoById(entityId);
		return result;
	}

	@Remark("启动店铺增量索引")
	@RequestMapping(value = "/doc/index/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> indexDoc() {
		Result<Object> result = Result.newOne();
		searchService.indexShopDocs(false);
		result.message = "操作成功!";
		return result;
	}

	@Remark("转到店铺索引管理页面")
	@RequestMapping(value = "/doc/index/jsp", method = RequestMethod.GET)
	public String toIndexJsp() {
		return "mall/docIndex";
	}

	// ----------------------------------店铺人员列表 角色分配----------------------------------
	/**
	 * 店铺人员列表界面
	 * 
	 * @author zjl
	 * @date 2015年6月5日 上午11:31:10
	 * 
	 * @return
	 */
	@Remark("店铺人员列表界面")
	@RequestMapping(value = "/staff/list/jsp", method = RequestMethod.GET)
	public String toUserRoleListPage() {
		return "shop/shopStaffList";
	}

	/**
	 * 查询店铺下的人员
	 * 
	 * @author guoyn
	 * @date 2015年8月19日 上午9:32:00
	 * 
	 * @param request
	 * @return JqGridPage<User>
	 */
	@Remark("店铺下的人员")
	@RequestMapping(value = "/staff/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<User> listUserRole(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		UserContext userContext = getUserContext(request);
		String scope = userContext.getScope();
		AuthScope authScope = AuthScope.valueOf(scope);
		Integer entityId = userContext.getScopeEntityId(scope);
		//
		PaginatedList<User> paginatedList = userService.getUsersByScopeAndEntityIdAndFilter(authScope, entityId, paginatedFilter);
		// PaginatedList<User> paginatedList = userService.getUsersByScopeAndEntityIdAndFilter(AuthScope.shop, 1, paginatedFilter);
		//
		JqGridPage<User> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 根据条件查询用户
	 * 
	 * @author guoyn
	 * @date 2015年8月19日 上午9:32:43
	 * 
	 * @param request
	 * @return JqGridPage<User>
	 */
	@Remark("根据条件查询用户")
	@RequestMapping(value = "/user/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<User> listUsersByParms(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		UserContext userContext = getUserContext(request);
		String scope = userContext.getScope();
		AuthScope authScope = AuthScope.valueOf(scope);
		Integer entityId = userContext.getScopeEntityId(scope);
		//
		MapContext filters = paginatedFilter.getFilterItems();
		String phoneNo = filters.getTypedValue("phoneNo", String.class);
		//
		PaginatedList<User> paginatedList = PaginatedList.newOne();
		//
		if (!StrUtil.hasText(phoneNo)) {
			paginatedList.setRows(new ArrayList<User>(0));
		} else {
			User user = userService.getUserByPhoneNo(phoneNo);
			List<User> userList = new ArrayList<User>();
			if (user != null) {
				List<Role> roles = authxService.getRolesByUserIdAndScopeAndEntityId(user.getId(), authScope, entityId);
				user.setRoles(roles);
				userList.add(user);
			}
			//
			paginatedList.setRows(userList);
		}
		//
		paginatedList.setPagination(paginatedFilter.getPagination());
		JqGridPage<User> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 查询门店人员
	 * 
	 * @author wangdi
	 * @date 2015年12月20日 上午9:32:00
	 * 
	 * @param request
	 * @return Result<List<User>>
	 */
	@Remark("查询门店人员")
	@RequestMapping(value = "/user/list/get/normal/-shop", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<User>> getShopUsers(HttpServletRequest request) {
		Result<List<User>> result = Result.newOne();
		try {
			UserContext userContext = getUserContext(request);
			String scope = userContext.getScope();
			Integer entityId = userContext.getScopeEntityId(scope);
			// 添加过滤条件
			MapContext filter = MapContext.newOne();
			filter.put("scope", scope);
			filter.put("entityId", entityId);
			// 查询
			List<User> users = userService.getUsersByFilterNormal(filter);
			result.data = users;
		} catch (Exception e) {
			result.type = Result.Type.warn;
			result.message = "查询门店人员失败";
			logger.warn(e.getMessage());
		}
		return result;
	}

	/**
	 * 获取店铺下所有的角色及相应权限列表
	 * 
	 * @author guoyn
	 * @date 2015年8月19日 上午9:33:00
	 * 
	 * @param requestData
	 * @return Result<List<Role>>
	 */
	@Remark("获取店铺下所有的角色及相应权限列表")
	@RequestMapping(value = "/roles/and/perms/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<Role>> getAllRoles(HttpServletRequest request, @RequestBody MapContext requestData) {
		//
		Result<List<Role>> result = Result.newOne();
		//
		UserContext userContext = getUserContext(request);
		String scope = userContext.getScope();
		AuthScope authScope = AuthScope.valueOf(scope);
		Integer entityId = userContext.getScopeEntityId(scope);
		//
		List<Role> roles = authxService.getRolesByScopeAndEntityId(authScope, entityId, true);
		// List<Role> roles = authxService.getRolesByScopeAndEntityId(AuthScope.shop, 1, true);
		for (Role role : roles) {
			List<Permission> perms = authxService.getPermissonsByRoleId(role.getId());
			role.setPerms(perms);
		}
		//
		result.data = roles;
		//
		return result;
	}

	/**
	 * 更新店铺人员的角色列表
	 * 
	 * @author guoyn
	 * @date 2015年8月19日 下午5:26:02
	 * 
	 * @param requestData
	 * @return Result<Object>
	 */
	@Remark("更新店铺人员的角色")
	@RequestMapping(value = "/user/roles/update/do", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Result<Object> updateUserRoles(HttpServletRequest request, @RequestBody MapContext requestData) {
		//
		Result<Object> result = Result.newOne();
		result.message = "保存成功";
		//
		UserContext userContext = getUserContext(request);
		String scope = userContext.getScope();
		AuthScope authScope = AuthScope.valueOf(scope);
		Integer entityId = userContext.getScopeEntityId(scope);
		//
		RelChangeInfo relChangeInfo = new RelChangeInfo();
		//
		Integer userId = requestData.getTypedValue("userId", Integer.class);
		List<Integer> deleteRoleIds = requestData.getTypedValue("deleteRoleIds", TypeUtil.Types.IntegerList.getClass());
		List<Integer> addRoleIds = requestData.getTypedValue("addRoleIds", TypeUtil.Types.IntegerList.getClass());
		//
		relChangeInfo.setMainId(userId);
		relChangeInfo.setSubIdsDeleted(deleteRoleIds);
		relChangeInfo.setSubIdsAdded(addRoleIds);
		//
		boolean ok = authxService.updateUserRoles(relChangeInfo, authScope, entityId);
		// boolean ok = userService.updateUserRoles(relChangeInfo, 1, AuthScope.shop);
		//
		result.message = ok ? "保存成功!" : "保存失败!";
		return result;
	}

	/**
	 * 解除用户所有角色
	 * 
	 * @author guoyn
	 * @date 2015年8月19日 下午5:32:25
	 * 
	 * @param requestData
	 * @return Result<Object>
	 */
	@Remark("解除用户所有角色")
	@RequestMapping(value = "/user/roles/unbind/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> unbindUserRoles(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<Object> result = Result.newOne();
		//
		UserContext userContext = getUserContext(request);
		String scope = userContext.getScope();
		AuthScope authScope = AuthScope.valueOf(scope);
		Integer entityId = userContext.getScopeEntityId(scope);
		//
		Integer userId = requestData.getTypedValue("userId", Integer.class);
		boolean ok = authxService.unbindUserRolesByUserIdAndScopeAndEntityId(userId, authScope, entityId);
		// boolean ok = userService.unbindUserRolesByUserIdAndScopeAndEntityId(userId, AuthScope.shop, 1);
		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	// --------------------------------------------------店铺相册--------------------------------------------------

	/**
	 * 得到店铺相册
	 * 
	 * @author 郝江奎
	 * @date 2015年10月28日 下午6:22:45
	 * 
	 * @param request
	 * @return
	 */
	@Remark("得到店铺相册")
	@RequestMapping(value = "/album/image/get/-shop", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<ShopAlbumImg> getShopImage(HttpServletRequest request) {
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntity().getId();
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		MapContext filterItems = paginatedFilter.getFilterItems();
		filterItems.put("shopId", shopId);
		//
		PaginatedList<ShopAlbumImg> paginatedList = shopService.getShopImages(paginatedFilter);
		JqGridPage<ShopAlbumImg> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 保存店铺图片
	 * 
	 * @author 郝江奎
	 * @date 2015年10月29日 下午3:38:20
	 * 
	 * @param request
	 * @return
	 */
	@Remark("保存店铺图片")
	@RequestMapping(value = "/album/image/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<ShopNotice> saveShopImage(HttpServletRequest request, @RequestBody ShopAlbumImg shopAlbumImg) {
		UserContext userContext = getUserContext(request);
		//
		Result<ShopNotice> result = Result.newOne();
		//
		Integer shopId = userContext.getScopeEntity().getId();
		shopAlbumImg.setShopId(shopId);
		shopAlbumImg.setTs(new Date());
		result.message = shopService.saveShopImage(shopAlbumImg) ? "保存成功！" : "保存失败！";
		//
		return result;
	}

	/**
	 * 修改店铺图片
	 * 
	 * @author 郝江奎
	 * @date 2015年10月30日 上午9:26:09
	 * 
	 * @param request
	 * @return
	 */
	@Remark("修改店铺图片")
	@RequestMapping(value = "/album/image/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<ShopAlbumImg> updateShopImage(HttpServletRequest request, @RequestBody ShopAlbumImg shopAlbumImg) {
		UserContext userContext = getUserContext(request);
		//
		Result<ShopAlbumImg> result = Result.newOne();

		Integer shopId = userContext.getScopeEntity().getId();
		shopAlbumImg.setShopId(shopId);
		result.message = shopService.updateShopImage(shopAlbumImg) ? "修改成功！" : "修改失败！";
		result.data = shopAlbumImg;
		//
		return result;
	}

	/**
	 * 删除店铺图片
	 * 
	 * @author 郝江奎
	 * @date 2015年10月29日 下午4:31:38
	 * 
	 * @param id
	 * @return
	 */
	@Remark("删除店铺图片")
	@RequestMapping(value = "/album/image/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteShopImage(@RequestParam("id") Integer id) {
		Result<?> result = Result.newOne();
		result.message = shopService.deleteShopImageById(id) ? "删除成功！" : "删除失败！";
		return result;
	}

	/**
	 * 批量删除店铺图片
	 * 
	 * @author 郝江奎
	 * @date 2015年10月29日 下午4:34:14
	 * 
	 * @param ids
	 * @return
	 */
	@Remark("批量删除店铺图片")
	@RequestMapping(value = "/image/delete/batch/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteShopImageByIds(@RequestBody List<Integer> ids) {
		Result<?> result = Result.newOne();
		result.message = shopService.deleteShopImagesByIds(ids) ? "删除成功！" : "删除失败！";
		return result;
	}

	// --------------------------------------------------店铺公告--------------------------------------------------

	/**
	 * 得到店铺公告
	 * 
	 * @author 毛智东
	 * @date 2015年6月10日 下午10:22:45
	 * 
	 * @param request
	 * @return
	 */
	@Remark("得到店铺公告")
	@RequestMapping(value = "/notice/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<ShopNotice> getShopNotice(HttpServletRequest request) {
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntity().getId();
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		MapContext filterItems = paginatedFilter.getFilterItems();
		filterItems.put("shopId", shopId);
		//
		PaginatedList<ShopNotice> paginatedList = shopService.getShopNotices(paginatedFilter);
		JqGridPage<ShopNotice> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 保存店铺公告
	 * 
	 * @author 毛智东
	 * @date 2015年6月10日 下午10:38:20
	 * 
	 * @param request
	 * @param shopNotice
	 * @return
	 */
	@Remark("保存店铺公告")
	@RequestMapping(value = "/notice/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<ShopNotice> saveShopNotice(HttpServletRequest request, @RequestBody ShopNotice shopNotice) {
		UserContext userContext = getUserContext(request);
		//
		Result<ShopNotice> result = Result.newOne();
		if (shopNotice.getAutoFlag()) {
			// 自动发布
			shopNotice.setStartDate(shopNotice.getPubTime());
			shopNotice.setEndDate(shopNotice.getEndTime());
		} else {
			// 手动发布
			shopNotice.setEndDate(shopNotice.getPubTime());
		}
		//
		Integer shopId = userContext.getScopeEntity().getId();
		shopNotice.setShopId(shopId);
		result.message = shopService.saveShopNotice(shopNotice) ? "保存成功！" : "保存失败！";
		result.data = shopNotice;
		//
		return result;
	}

	/**
	 * 修改店铺公告
	 * 
	 * @author 毛智东
	 * @date 2015年8月14日 下午6:26:09
	 * 
	 * @param request
	 * @param shopNotice
	 * @return
	 */
	@Remark("修改店铺公告")
	@RequestMapping(value = "/notice/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<ShopNotice> updateShopNotice(HttpServletRequest request, @RequestBody ShopNotice shopNotice) {
		UserContext userContext = getUserContext(request);
		//
		Result<ShopNotice> result = Result.newOne();
		if (shopNotice.getAutoFlag()) {
			// 自动发布
			shopNotice.setStartDate(shopNotice.getPubTime());
			shopNotice.setEndDate(shopNotice.getEndTime());
		} else {
			// 手动发布
			shopNotice.setPubTime(null);
			shopNotice.setEndDate(shopNotice.getPubTime());
		}

		Integer shopId = userContext.getScopeEntity().getId();
		shopNotice.setShopId(shopId);
		result.message = shopService.updateShopNotice(shopNotice) ? "修改成功！" : "修改失败！";
		result.data = shopNotice;
		//
		return result;
	}

	/**
	 * 发布店铺公告
	 * 
	 * @author 郝江奎
	 * @date 2015年8月14日 下午2:20:38
	 * 
	 * @return
	 */
	@Remark("发布店铺公告")
	@RequestMapping(value = "/notice/publish/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> publishShopNotice(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		ShopNotice shopNotice = new ShopNotice();
		Integer id = requestData.getTypedValue("id", Integer.class);
		Date pubTime = new Date();
		shopNotice.setId(id);
		shopNotice.setStatus(1);
		shopNotice.setPubTime(pubTime);
		shopNotice.setStartDate(pubTime);
		result.message = shopService.updateShopNotice(shopNotice) ? "发布成功！" : "发布失败！";
		return result;
	}

	/**
	 * 停止店铺公告
	 * 
	 * @author 郝江奎
	 * @date 2015年8月14日 下午2:31:38
	 * 
	 * @return
	 */
	@Remark("停止店铺公告")
	@RequestMapping(value = "/notice/stop/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> stopShopNotice(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		ShopNotice shopNotice = new ShopNotice();
		Integer id = requestData.getTypedValue("id", Integer.class);
		Date endTime = new Date();
		shopNotice.setId(id);
		shopNotice.setEndTime(endTime);
		shopNotice.setStatus(2);
		shopNotice.setEndDate(endTime);
		result.message = shopService.updateShopNotice(shopNotice) ? "停止成功！" : "停止失败！";
		return result;
	}

	/**
	 * 删除店铺公告
	 * 
	 * @author 毛智东
	 * @date 2015年8月14日 下午6:31:38
	 * 
	 * @param id
	 * @return
	 */
	@Remark("删除店铺公告")
	@RequestMapping(value = "/notice/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteShopNotice(@RequestParam("id") Integer id) {
		Result<?> result = Result.newOne();
		result.message = shopService.deleteShopNoticeById(id) ? "删除成功！" : "删除失败！";
		return result;
	}

	/**
	 * 批量删除店铺公告
	 * 
	 * @author 毛智东
	 * @date 2015年8月14日 下午6:34:14
	 * 
	 * @param ids
	 * @return
	 */
	@Remark("批量删除店铺公告")
	@RequestMapping(value = "/notice/delete/batch/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteShopNoticeByIds(@RequestBody List<Integer> ids) {
		Result<?> result = Result.newOne();
		result.message = shopService.deleteShopNoticesByIds(ids) ? "删除成功！" : "删除失败！";
		return result;
	}

	// -------------------------------- 个人信息 ------------------------------------
	/**
	 * 店铺个人信息页面
	 * 
	 * @author 王少辉
	 * @date 2015年6月16日 下午6:28:26
	 * 
	 * @return 返回店铺个人信息页面
	 */
	@Remark("店铺个人信息页面")
	@RequestMapping(value = "/personal/info/jsp", method = RequestMethod.GET)
	public String toUserSettingPage() {
		return "shop/shopPersonalInfo";
	}

	// ------------------------------------店铺备忘录----------------------------------------
	/**
	 * 店铺备忘录页面
	 * 
	 * @author 邓华锋
	 * @date 2016年2月19日 下午6:23:35
	 * 
	 * @return
	 */
	@Remark("店铺备忘录页面")
	@RequestMapping(value = "/memo/list/jsp", method = RequestMethod.GET)
	public String toShopMemoList() {
		return "shop/shopMemoList";
	}

	/**
	 * 保存店铺备忘录
	 * 
	 * @author 邓华锋
	 * @date 2016年2月19日 下午6:24:06
	 * 
	 * @param shopMemo
	 * @return
	 */
	@Remark("保存店铺备忘录")
	@RequestMapping(value = "/memo/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> saveShopMemo(@RequestBody ShopMemo shopMemo, HttpServletRequest request) {
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntity().getId();
		String shopName = userContext.getScopeEntity().getName();
		Result<Integer> result = Result.newOne();
		shopMemo.setShopId(shopId);
		shopMemo.setShopName(shopName);
		Date now = new Date();
		shopMemo.setCreateTime(now);
		shopMemo.setDeleted(false);
		shopMemo.setTs(now);
		boolean ok = shopService.saveShopMemo(shopMemo);
		if (ok) {
			result.data = shopMemo.getId();
			result.message = "保存成功";
		} else {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}

	/**
	 * 修改店铺备忘录
	 * 
	 * @author 邓华锋
	 * @date 2016年2月19日 下午6:24:15
	 * 
	 * @param shopMemo
	 * @return
	 */
	@Remark("修改店铺备忘录")
	@RequestMapping(value = "/memo/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateShopMemo(@RequestBody ShopMemo shopMemo, HttpServletRequest request) {
		Result<Integer> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntity().getId();
		String shopName = userContext.getScopeEntity().getName();
		shopMemo.setShopId(shopId);
		shopMemo.setShopName(shopName);
		shopMemo.setTs(new Date());
		boolean ok = shopService.updateShopMemo(shopMemo);
		if (ok) {
			result.message = "修改成功";
		} else {
			result.type = Type.warn;
			result.message = "修改失败";
		}
		return result;
	}

	/**
	 * 删除店铺备忘录
	 * 
	 * @author 邓华锋
	 * @date 2016年2月19日 下午6:24:23
	 * 
	 * @param shopMemo
	 * @return
	 */
	@Remark("删除店铺备忘录")
	@RequestMapping(value = "/memo/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteShopMemo(@RequestBody ShopMemo shopMemo, HttpServletRequest request) {
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntity().getId();
		String shopName = userContext.getScopeEntity().getName();
		Result<?> result = Result.newOne();
		shopMemo.setShopId(shopId);
		shopMemo.setShopName(shopName);
		shopMemo.setDeleted(true);
		shopMemo.setDeleteTime(new Date());
		boolean ok = shopService.updateShopMemo(shopMemo);
		if (ok) {
			result.message = "删除成功";
		} else {
			result.type = Type.warn;
			result.message = "删除失败";
		}
		return result;
	}

	/**
	 * 根据店铺备忘录ID获取店铺备忘录
	 * 
	 * @author 邓华锋
	 * @date 2016年2月19日 下午6:24:30
	 * 
	 * @param shopMemo
	 * @return
	 */
	@Remark("根据店铺备忘录ID获取店铺备忘录")
	@RequestMapping(value = "/memo/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<ShopMemo> getShopMemoById(@RequestBody ShopMemo shopMemo, HttpServletRequest request) {
		Result<ShopMemo> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntity().getId();
		shopMemo.setShopId(shopId);
		result.data = shopService.getShopMemoByIdAndShopId(shopMemo.getId(), shopId);
		return result;
	}

	/**
	 * 分页查询店铺备忘录
	 * 
	 * @author 邓华锋
	 * @date 2016年2月19日 下午6:24:37
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询店铺备忘录")
	@RequestMapping(value = "/memo/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<ShopMemo> getShopMemos(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		MapContext filterItems = paginatedFilter.getFilterItems();
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntity().getId();
		filterItems.put("shopId", shopId);
		filterItems.put("deleted", false);
		PaginatedList<ShopMemo> paginatedList = shopService.getShopMemosByFilter(paginatedFilter);
		JqGridPage<ShopMemo> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}
	// ---------------------------------------------------------------------------------------------------------------------
	/**
	 * 获取店铺下的所有车辆服务列表
	 * 
	 * @author 邓华锋
	 * @date 2016年2月19日 下午6:24:37
	 * 
	 * @param request
	 * @return
	 */
	@Remark("获取店铺下的所有车辆服务列表")
	@RequestMapping(value = "/svc/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<ShopSvc> getShopSvcs(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		MapContext filterItems = paginatedFilter.getFilterItems();
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntityId(AuthScope.shop.name());
		filterItems.put("shopId", shopId);
		PaginatedList<ShopSvc> paginatedList = carSvcService.getShopCarSvcsByFilter(paginatedFilter);
		JqGridPage<ShopSvc> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}
}
