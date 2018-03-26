package priv.starfish.mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.*;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.Agreement;
import priv.starfish.mall.comn.entity.Permission;
import priv.starfish.mall.comn.entity.Role;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.mall.dto.MallDto;
import priv.starfish.mall.mall.entity.Mall;
import priv.starfish.mall.mall.entity.MallNotice;
import priv.starfish.mall.mall.entity.Operator;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.service.MallService;
import priv.starfish.mall.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Remark("商城")
@Controller
@RequestMapping(value = "/mall")
public class MallController extends BaseController {

	@Resource
    MallService mallService;

	@Resource
	private UserService userService;

	// ---------------------------------商城注册页面---------------------------------
	/**
	 * 跳转商城注册页面
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:45:07
	 * 
	 * @return 返回页面路径
	 */
	@Remark("商城注册页面")
	@RequestMapping(value = "/regist/jsp", method = RequestMethod.GET)
	public String index() {
		return "mall/mallRegist";
	}

	/**
	 * 跳转商城资金账户
	 * 
	 * @author 郝江奎
	 * @date 2015年10月30日 下午13:45:07
	 * 
	 * @return 返回页面路径
	 */
	@Remark("商城资金账户页面")
	@RequestMapping(value = "/userAccount/jsp", method = RequestMethod.GET)
	public String toGetAccount() {
		return "mall/mallAccount";
	}

	/**
	 * 跳转商城公告
	 * 
	 * @author 郝江奎
	 * @date 2015年10月30日 下午13:50:07
	 * 
	 * @return 返回页面路径
	 */
	@Remark("商城公告页面")
	@RequestMapping(value = "/notice/jsp", method = RequestMethod.GET)
	public String toGetNotice() {
		return "mall/mallNotice";
	}

	/**
	 * 跳转各方协议
	 * 
	 * @author 郝江奎
	 * @date 2015年10月30日 下午13:55:07
	 * 
	 * @return 返回页面路径
	 */
	@Remark("各方协议页面")
	@RequestMapping(value = "/agreement/jsp", method = RequestMethod.GET)
	public String toGetAgreement() {
		return "mall/mallAgreement";
	}

	/**
	 * 获取商城基本信息
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:45:13
	 * 
	 * @return 返回商城基本信息
	 */
	@Remark("获取商城基本信息")
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<MallDto> getMall(HttpServletRequest request) {
		Result<MallDto> result = Result.newOne();
		//
		MallDto mallDto = mallService.getMallInfo();
		result.data = mallDto;
		//
		return result;
	}

	/**
	 * 获取商城协议信息
	 * 
	 * @author 王少辉
	 * @date 2015年8月18日 下午1:30:15
	 * 
	 * @param request
	 * @return 返回商城协议信息
	 */
	@Remark("获取商城协议信息")
	@RequestMapping(value = "/mallAgreement/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Agreement> getMallAgreement(HttpServletRequest request) {
		Result<Agreement> result = Result.newOne();

		try {
			result.data = mallService.getMerchAgreement();
		} catch (Exception e) {
			result.type = Result.Type.error;
			result.message = "获取商城协议失败";
		}

		return result;
	}

	/**
	 * 获取代理商协议信息
	 * 
	 * @author 郝江奎
	 * @date 2015年9月6日 下午11:00:15
	 * 
	 * @param request
	 * @return 返回代理协议信息
	 */
	@Remark("获取代理商协议信息")
	@RequestMapping(value = "/agentAgreement/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Agreement> getAgentAgreement(HttpServletRequest request) {
		Result<Agreement> result = Result.newOne();

		try {
			result.data = mallService.getAgentAgreement();
		} catch (Exception e) {
			result.type = Result.Type.error;
			result.message = "获取代理商协议失败";
		}

		return result;
	}

	/**
	 * 获取供应商商协议信息
	 * 
	 * @author 郝江奎
	 * @date 2015年10月12日 下午2:10:15
	 * 
	 * @param request
	 * @return 返回供应商协议信息
	 */
	@Remark("获取供应商协议信息")
	@RequestMapping(value = "/vendorAgreement/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Agreement> getVendorAgreement(HttpServletRequest request) {
		Result<Agreement> result = Result.newOne();

		try {
			result.data = mallService.getVendorAgreement();
		} catch (Exception e) {
			result.type = Result.Type.error;
			result.message = "获取供应商协议失败";
		}

		return result;
	}

	/**
	 * 获取会员协议信息
	 * 
	 * @author 郝江奎
	 * @date 2015年9月6日 下午11:30:15
	 * 
	 * @param request
	 * @return 返回会员协议信息
	 */
	@Remark("获取会员协议信息")
	@RequestMapping(value = "/memberAgreement/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Agreement> getMemberAgreement(HttpServletRequest request) {
		Result<Agreement> result = Result.newOne();

		try {
			result.data = mallService.getMemberAgreement();
		} catch (Exception e) {
			result.type = Result.Type.error;
			result.message = "获取会员协议失败";
		}

		return result;
	}

	/**
	 * 获取商城管理员信息
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:45:17
	 * 
	 * @return 返回商城管理员信息
	 */
	@Remark("获取商城管理员信息")
	@RequestMapping(value = "/admin/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<User> getMallAdmin(HttpServletRequest request) {
		Result<User> result = Result.newOne();

		try {
			Operator operator = mallService.getOperator();
			if (operator != null && operator.getId() != null) {
				result.data = userService.getUserById(operator.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.type = Type.error;
			result.message = "获取商城管理员信息失败";
		}

		return result;
	}

	/**
	 * 检验商城编号是否唯一
	 * 
	 * @author 王少辉
	 * @date 2015年7月23日 上午11:46:26
	 * 
	 * @param request
	 * @param mall
	 * @return 返回验证结果
	 */
	@Remark("检验商城编号是否唯一")
	@RequestMapping(value = "/code/validate/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> validateCode(HttpServletRequest request, @RequestBody Mall mall) {
		Result<?> result = Result.newOne();
		if (mall != null && mall.getCode() != null) {
			Mall existMall = mallService.getMallByCode(mall.getCode());
			// 如果商城编号已存在，返回error
			if (existMall != null) {
				result.type = Type.error;
			}
		}

		return result;
	}

	/**
	 * 检验用户手机号是否唯一
	 * 
	 * @author 王少辉
	 * @date 2015年7月23日 上午11:46:26
	 * 
	 * @param request
	 * @param user
	 * @return 返回验证结果
	 */
	@Remark("检验用户手机号是否唯一")
	@RequestMapping(value = "/phoneNo/validate/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> validatePhoneNo(HttpServletRequest request, @RequestBody User user) {
		Result<?> result = Result.newOne();
		if (user != null && user.getPhoneNo() != null) {
			User existUser = userService.getUserByPhoneNo(user.getPhoneNo());

			// 如果商城手机号已存在，返回error
			if (existUser != null && existUser.getId() != user.getId()) {
				result.type = Type.error;
			}
		}

		return result;
	}

	/**
	 * 注册商城基本信息（如果已存在则修改）
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:45:27
	 * 
	 * @return 返回注册结果
	 */
	@Remark("注册或修改商城信息")
	@RequestMapping(value = "/regist/or/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<MallDto> registerOrUpdate(HttpServletRequest request, @RequestBody MallDto mallDto) {
		Result<MallDto> result = Result.newOne();

		try {
			mallDto.setOpenTime(new Date());

			boolean ok = false;
			if (mallDto.getId() == null) {
				ok = mallService.createOrUpdateMall(mallDto);
			} else {
				ok = mallService.updateMallAndUser(mallDto);
			}

			if (ok) {
				result.message = "保存成功";
				result.data = mallDto;
			} else {
				result.type = Type.error;
				result.message = "保存失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.type = Type.error;
			result.message = "保存失败";
		}

		return result;
	}

	/**
	 * 保存商城基本信息
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:45:27
	 * 
	 * @return 返回保存结果
	 */
	@Remark("保存商城基本信息")
	@RequestMapping(value = "/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> saveMall(@RequestBody Mall mall) {
		Result<?> result = Result.newOne();

		try {
			boolean ok = mallService.updateMall(mall);

			if (ok) {
				result.message = "保存成功";
			} else {
				result.type = Type.error;
				result.message = "保存失败";
			}
		} catch (Exception e) {
			result.type = Type.error;
			result.message = "保存失败";
		}

		return result;
	}

	/**
	 * 保存商城协议信息
	 * 
	 * @author 王少辉
	 * @date 2015年8月18日 下午1:49:07
	 * 
	 *            协议
	 * @return 返回保存结果
	 */
	@Remark("保存商城协议信息")
	@RequestMapping(value = "/mallAgreement/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> saveMallAgreement(@RequestBody Agreement agreement) {
		Result<?> result = Result.newOne();
		boolean ok = false;

		try {
			Agreement agr = mallService.getMerchAgreement();
			if (agr == null) {
				ok = mallService.saveMerchAgreement(agreement);
			} else {
				agr.setContent(agreement.getContent());
				ok = mallService.updateAgreement(agr);
			}

			if (ok) {
				result.message = "保存成功";
			} else {
				result.type = Type.error;
				result.message = "保存失败";
			}
		} catch (Exception e) {
			result.type = Type.error;
			result.message = "保存失败";
		}

		return result;
	}

	/**
	 * 保存代理协议信息
	 * 
	 * @author 郝江奎
	 * @date 2015年9月6日 下午11:38:07
	 * 
	 *            协议
	 * @return 返回保存结果
	 */
	@Remark("保存代理协议信息")
	@RequestMapping(value = "/agentAgreement/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> saveAgentAgreement(@RequestBody Agreement agreement) {
		Result<?> result = Result.newOne();
		boolean ok = false;

		try {
			Agreement agr = mallService.getAgentAgreement();
			if (agr == null) {
				ok = mallService.saveAgentAgreement(agreement);
			} else {
				agr.setContent(agreement.getContent());
				ok = mallService.updateAgreement(agr);
			}

			if (ok) {
				result.message = "保存成功";
			} else {
				result.type = Type.error;
				result.message = "保存失败";
			}
		} catch (Exception e) {
			result.type = Type.error;
			result.message = "保存失败";
		}

		return result;
	}

	/**
	 * 保存供应商协议信息
	 * 
	 * @author 郝江奎
	 * @date 2015年10月12日 下午14:38:07
	 * 
	 *            协议
	 * @return 返回保存结果
	 */
	@Remark("保存供应商协议信息")
	@RequestMapping(value = "/vendorAgreement/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> saveVendorAgreement(@RequestBody Agreement agreement) {
		Result<?> result = Result.newOne();
		boolean ok = false;

		try {
			Agreement agr = mallService.getVendorAgreement();
			if (agr == null) {
				ok = mallService.saveVendorAgreement(agreement);
			} else {
				agr.setContent(agreement.getContent());
				ok = mallService.updateAgreement(agr);
			}

			if (ok) {
				result.message = "保存成功";
			} else {
				result.type = Type.error;
				result.message = "保存失败";
			}
		} catch (Exception e) {
			result.type = Type.error;
			result.message = "保存失败";
		}

		return result;
	}

	/**
	 * 保存会员协议信息
	 * 
	 * @author 郝江奎
	 * @date 2015年9月6日 下午11:40:07
	 * 
	 *            协议
	 * @return 返回保存结果
	 */
	@Remark("保存会员协议信息")
	@RequestMapping(value = "/memberAgreement/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> saveMemberAgreement(@RequestBody Agreement agreement) {
		Result<?> result = Result.newOne();
		boolean ok = false;

		try {
			Agreement agr = mallService.getMemberAgreement();
			if (agr == null) {
				ok = mallService.saveMemberAgreement(agreement);
			} else {
				agr.setContent(agreement.getContent());
				ok = mallService.updateAgreement(agr);
			}

			if (ok) {
				result.message = "保存成功";
			} else {
				result.type = Type.error;
				result.message = "保存失败";
			}
		} catch (Exception e) {
			result.type = Type.error;
			result.message = "保存失败";
		}

		return result;
	}

	// ----------------------------------商城设置----------------------------------
	/**
	 * 商城设置页面
	 * 
	 * @author 王少辉
	 * @date 2015年5月21日 上午9:56:46
	 * 
	 * @return 返回商城设置页面
	 */
	@Remark("商城设置页面")
	@RequestMapping(value = "/setting/jsp", method = RequestMethod.GET)
	public String toPersonalInfoJsp() {
		return "mall/mallSetting";
	}

	/**
	 * 获取商城公告列表
	 * 
	 * @author 王少辉
	 * @date 2015年5月21日 上午10:04:29
	 * 
	 * @return 返回商城公告列表
	 */
	@RequestMapping(value = "/notice/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<MallNotice> getMallNoticeList(HttpServletRequest request) {
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		PaginatedList<MallNotice> paginatedList = mallService.getMallNoticesByFilter(paginatedFilter);
		JqGridPage<MallNotice> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 获取商城公告
	 * 
	 * @author 王少辉
	 * @date 2015年8月21日 上午10:29:56
	 * 
	 * @return 返回商城公告
	 */
	@RequestMapping(value = "/notice/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<MallNotice> getMallNotice() {
		Result<MallNotice> result = Result.newOne();

		try {
			Integer id = 1;
			MallNotice mallNotice = mallService.getMallNoticeById(id);

			if (mallNotice == null) {
				return result;
			}

			result.data = mallNotice;
		} catch (Exception e) {
			result.type = Type.error;
			result.message = "获取商城公告失败";
		}

		return result;
	}

	/**
	 * 保存商城公告
	 * 
	 * @author 王少辉
	 * @date 2015年5月21日 上午11:13:50
	 * 
	 * @param mallNotice
	 *            商城公告
	 * @return 返回保存结果
	 */
	@RequestMapping(value = "/notice/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<MallNotice> saveMallNotice(@RequestBody MallNotice mallNotice) {
		Result<MallNotice> result = Result.newOne();

		try {
			if (mallNotice.getAutoFlag()) {
				// 自动发布
				mallNotice.setStartDate(mallNotice.getPubTime());
				mallNotice.setEndDate(mallNotice.getEndTime());
			} else {
				// 手动发布
				mallNotice.setEndDate(mallNotice.getPubTime());
			}

			result.message = mallService.saveMallNotice(mallNotice) ? "保存成功" : "保存失败";
			result.data = mallNotice;
		} catch (Exception e) {
			result.type = Type.error;
			result.message = "保存失败";
		}

		return result;
	}

	/**
	 * 修改商城公告
	 * 
	 * @author 王少辉
	 * @date 2015年8月27日 下午7:36:10
	 * 
	 * @param mallNotice
	 *            商城公告
	 * @return 返回修改商城公告结果
	 */
	@Remark("修改商城公告")
	@RequestMapping(value = "/notice/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<MallNotice> updateMallNotice(@RequestBody MallNotice mallNotice) {
		Result<MallNotice> result = Result.newOne();

		if (mallNotice.getAutoFlag()) {
			// 自动发布
			mallNotice.setStartDate(mallNotice.getPubTime());
			mallNotice.setEndDate(mallNotice.getEndTime());
		} else {
			// 手动发布
			mallNotice.setPubTime(null);
			mallNotice.setEndDate(mallNotice.getPubTime());
		}

		result.message = mallService.updateMallNotice(mallNotice) ? "修改成功！" : "修改失败！";
		result.data = mallNotice;

		return result;
	}

	/**
	 * 发布商城公告
	 * 
	 * @author 王少辉
	 * @date 2015年8月27日 下午7:36:47
	 * 
	 * @param requestData
	 * @return 返回发布商城公告结果
	 */
	@Remark("发布商城公告")
	@RequestMapping(value = "/notice/publish/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> publishMallNotice(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		Integer id = requestData.getTypedValue("id", Integer.class);
		MallNotice mallNotice = mallService.getMallNoticeById(id);
		Date pubTime = new Date();
		mallNotice.setStatus(1);
		mallNotice.setPubTime(pubTime);
		mallNotice.setStartDate(pubTime);
		result.message = mallService.updateMallNotice(mallNotice) ? "发布成功！" : "发布失败！";
		return result;
	}

	/**
	 * 停止店铺公告
	 * 
	 * @author 王少辉
	 * @date 2015年8月27日 下午7:38:22
	 * 
	 * @param requestData
	 * @return 返回停止商城公告结果
	 */
	@Remark("停止店铺公告")
	@RequestMapping(value = "/notice/stop/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> stopMallNotice(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		Integer id = requestData.getTypedValue("id", Integer.class);
		MallNotice mallNotice = mallService.getMallNoticeById(id);
		Date endTime = new Date();
		mallNotice.setStatus(2);
		mallNotice.setEndTime(endTime);
		mallNotice.setEndDate(endTime);
		result.message = mallService.updateMallNotice(mallNotice) ? "停止成功！" : "停止失败！";
		return result;
	}

	/**
	 * 删除店铺公告
	 * 
	 * @author 王少辉
	 * @date 2015年8月27日 下午7:39:26
	 * 
	 * @param id
	 *            删除的店铺公告id
	 * @return 返回删除店铺公告结果
	 */
	@Remark("删除店铺公告")
	@RequestMapping(value = "/notice/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteMallNotice(@RequestParam("id") Integer id) {
		Result<?> result = Result.newOne();
		result.message = mallService.delMallNoticeById(id) ? "删除成功！" : "删除失败！";
		return result;
	}

	/**
	 * 批量删除店铺公告
	 * 
	 * @author 王少辉
	 * @date 2015年8月27日 下午7:43:07
	 * 
	 * @param ids
	 *            批量删除的店铺公告id
	 * @return 返回批量删除店铺公告结果
	 */
	@Remark("批量删除店铺公告")
	@RequestMapping(value = "/notice/delete/batch/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteMallNoticeByIds(@RequestBody List<Integer> ids) {
		Result<?> result = Result.newOne();
		result.message = mallService.delMallNoticeByIds(ids) ? "删除成功！" : "删除失败！";
		return result;
	}

	// ----------------------------------商城人员列表 角色分配----------------------------------
	/**
	 * 人员列表界面
	 * 
	 * @author zjl
	 * @date 2015年6月5日 上午11:31:10
	 * 
	 * @return
	 */
	@Remark("商城人员列表界面")
	@RequestMapping(value = "/staff/list/jsp", method = RequestMethod.GET)
	public String toMallStaffListPage() {
		return "mall/mallStaffList";
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
		MapContext filter = paginatedFilter.getFilterItems();
		String phoneNo = filter.getTypedValue("phoneNo", String.class);
		String nickName = filter.getTypedValue("nickName", String.class);
		//
		PaginatedList<User> paginatedList = PaginatedList.newOne();

		if (!StrUtil.hasText(phoneNo) && !StrUtil.hasText(nickName)) {
			paginatedList.setRows(new ArrayList<User>(0));
			paginatedList.setPagination(paginatedFilter.getPagination());
		} else {
			UserContext userContext = getUserContext(request);
			String scope = userContext.getScope();
			AuthScope authScope = AuthScope.valueOf(scope);
			Integer entityId = userContext.getScopeEntityId(scope);
			//
			paginatedList = userService.getUsersByFilter(paginatedFilter, true);
			List<User> userList = paginatedList.getRows();
			for (User user : userList) {
				List<Role> roles = authxService.getRolesByUserIdAndScopeAndEntityId(user.getId(), authScope, entityId);
				user.setRoles(roles);
			}
			//
			paginatedList.setRows(userList);
		}
		//
		JqGridPage<User> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
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
		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

}
