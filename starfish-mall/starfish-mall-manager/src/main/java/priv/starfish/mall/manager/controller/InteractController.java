package priv.starfish.mall.manager.controller;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.*;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.EnumUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.dto.UserDto;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.goods.entity.Product;
import priv.starfish.mall.interact.dto.InteractUserDto;
import priv.starfish.mall.interact.entity.GoodsInquiry;
import priv.starfish.mall.interact.entity.OnlineServeNo;
import priv.starfish.mall.interact.entity.OnlineServeRecord;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.service.GoodsService;
import priv.starfish.mall.service.InteractService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@Remark("在线客服、在线咨询等")
@RequestMapping(value = "/interact")
public class InteractController extends BaseController {

	@Resource
	InteractService interactService;
	@Resource
	GoodsService GoodsService;

	@Remark("在线客服页面")
	@RequestMapping(value = { "/olsChat/jsp" }, method = RequestMethod.GET)
	public String toOlsChatPage() {
		return "interact/olsChat";
	}

	// --------------------------------在线客服------------------------------------

	/**
	 * 查询店铺下的人员
	 * 
	 * @author 郝江奎
	 * @date 2015年9月9日 上午10:02:00
	 * 
	 * @param request
	 * @return JqGridPage<User>
	 */
	@Remark("店铺下非客服人员")
	@RequestMapping(value = "/staff/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<User> listUserRole(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		// 获取当前的authScope和entityId
		UserContext userContext = getUserContext(request);
		String scope = userContext.getScope();
		AuthScope authScope = AuthScope.valueOf(scope);
		Integer entityId = userContext.getScopeEntityId(scope);
		//
		PaginatedList<User> userPaginatedList = userService.getUsersByScopeAndEntityIdAndFilter(authScope, entityId, paginatedFilter);
		PaginatedList<InteractUserDto> interactPaginatedList = interactService.getOnlineServeNos(authScope, entityId, paginatedFilter);

		List<User> userList = userPaginatedList.getRows();
		List<InteractUserDto> interactUserDtoList = interactPaginatedList.getRows();

		for (int i = userList.size(); i > 0; i--) {
			int j = i - 1;
			for (InteractUserDto interactUserDto : interactUserDtoList) {
				if (interactUserDto.getServantId().equals(userList.get(j).getId())) {
					userList.remove(userList.get(j));
					break;
				}
			}
		}
		//
		JqGridPage<User> jqGridPage = JqGridPage.fromPaginatedList(userPaginatedList);
		return jqGridPage;
	}

	/**
	 * 分布查询在线客服列表
	 * 
	 * @author 郝江奎
	 * @date 2015年9月9日 上午10:21:40
	 * 
	 * @param request
	 *            请求参数
	 * @return 返回在线客服列表
	 */
	@Remark("查询在线客服列表")
	@RequestMapping(value = "/servant/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<InteractUserDto> listOls(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		// 获取当前的authScope和entityId
		UserContext userContext = getUserContext(request);
		String scope = userContext.getScope();
		AuthScope authScope = AuthScope.valueOf(scope);
		Integer entityId = userContext.getScopeEntityId(scope);
		// 根据条件查找在线客服
		PaginatedList<InteractUserDto> paginatedList = interactService.getOnlineServeNos(authScope, entityId, paginatedFilter);
		//
		JqGridPage<InteractUserDto> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);

		return jqGridPage;
	}

	/**
	 * 添加客服
	 * 
	 * @author 郝江奎
	 * @date 2015年9月7日 下午4:43:07
	 * 
	 *            根据会员ids添加客服
	 * @return 返回添加客服结果
	 */
	@Remark("批量添加客服公告")
	@RequestMapping(value = "/servant/add", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> addOlsesByServantIds(@RequestBody List<Integer> servantIds, HttpServletRequest request) {
		Result<?> result = Result.newOne();
		try {
			for (Integer servantId : servantIds) {
				OnlineServeNo olsNo = new OnlineServeNo();
				UserContext userContext = getUserContext(request);
				olsNo.setEntityId(userContext.getScopeEntity().getId());
				olsNo.setEntityName(userContext.getScopeEntity().getName());
				String scope = userContext.getScope();
				olsNo.setScope(EnumUtil.valueOf(AuthScope.class, scope));
				olsNo.setServantId(servantId);
				interactService.insertOnlineServeNo(olsNo);
			}
			result.message = "添加成功！";
		} catch (Exception e) {
			result.type = Result.Type.error;
			result.message = "添加失败！";
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据客服id查询在线客服信息
	 * 
	 * @author 王少辉
	 * @date 2015年7月9日 上午9:28:46
	 * 
	 * @param onlineServeNo
	 *            在线客服
	 * @return 返回在线客服信息
	 */
	@Remark("根据客服id查询在线客服信息")
	@RequestMapping(value = "/online/servant/get/by/id", method = RequestMethod.POST)
	@ResponseBody
	public Result<UserDto> getOlsById(@RequestBody OnlineServeNo onlineServeNo) {
		Result<UserDto> result = Result.newOne();

		OnlineServeNo olsNo;
		try {
			olsNo = null;
			if (onlineServeNo != null) {
				Integer id = onlineServeNo.getId();
				if (id != null) {
					OnlineServeNo no = userService.getOnlineServeNoById(id);
					if (no == null) {
						result.type = Result.Type.error;
						result.message = "查询的记录不存在";
					}

					olsNo = userService.getOnlineServeNoById(id);
					UserDto userDto = new UserDto();
					User user = userService.getUserById(olsNo.getServantId());
					TypeUtil.copyProperties(user, userDto);
					userDto.setOlsId(olsNo.getId());
					userDto.setServantNo(olsNo.getServantNo());
					userDto.setServantName(olsNo.getServantName());

					result.data = userDto;
				}
			}
		} catch (Exception e) {
			result.type = Result.Type.error;
			result.message = "查询失败";
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 更新在线客服信息
	 * 
	 * @author 郝江奎
	 * @date 2015年9月8日 下午4:24:50
	 * 
	 * @param onlineServeNo
	 *            客服信息
	 * @return 返回操作结果
	 */
	@Remark("保存或更新在线客服信息")
	@RequestMapping(value = "/servant/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<OnlineServeNo> updateOls(@RequestBody OnlineServeNo onlineServeNo) {
		Result<OnlineServeNo> result = Result.newOne();
		Boolean ok = userService.updateOnlineServeNo(onlineServeNo);
		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	/**
	 * 客服编号是否存在
	 * 
	 * @author 郝江奎
	 * @date 2015年9月16日 下午2:19:00
	 * 
	 *            客服编号
	 * @return返回操作结果
	 */

	@Remark("客服编号是否存在")
	@RequestMapping(value = "/exist/by/servantNo/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<OnlineServeNo> getInteractExistByServantNo(@RequestBody String servantNo) {
		Result<OnlineServeNo> result = Result.newOne();
		OnlineServeNo onlineServeNo = interactService.getOnlineServeNoByServantNo(servantNo);
		if (onlineServeNo != null) {
			result.data = onlineServeNo;
			result.type = Result.Type.info;
		} else {
			result.type = Result.Type.error;
		}
		return result;
	}

	/**
	 * 客服名称是否存在
	 * 
	 * @author 郝江奎
	 * @date 2015年9月16日 下午2:31:19
	 * 
	 *            客服名称
	 * @return返回操作结果
	 */

	@Remark("客服名称是否存在")
	@RequestMapping(value = "/exist/by/servantName/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<OnlineServeNo> getInteractExistByServantName(@RequestBody String servantName) {
		Result<OnlineServeNo> result = Result.newOne();
		OnlineServeNo onlineServeNo = interactService.getOnlineServeNoByServantName(servantName);
		if (onlineServeNo != null) {
			result.data = onlineServeNo;
			result.type = Result.Type.info;
		} else {
			result.type = Result.Type.error;
		}
		return result;
	}

	/**
	 * 删除客服
	 * 
	 * @author 郝江奎
	 * @date 2015年9月8日 下午5:24:50
	 * 
	 *            客服信息
	 * @return 返回操作结果
	 */
	@Remark("删除客服")
	@RequestMapping(value = "/servant/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteOls(@RequestParam("id") Integer id) {
		Result<?> result = Result.newOne();
		result.message = userService.deleteOnlineServeNoById(id) ? "删除成功！" : "删除失败！";
		return result;
	}

	/**
	 * 批量删除客服
	 * 
	 * @author 郝江奎
	 * @date 2015年9月8日 下午5:36:50
	 * 
	 *            客服信息
	 * @return 返回操作结果
	 */
	@Remark("批量删除客服")
	@RequestMapping(value = "/servant/delete/batch/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteOlsesByIds(@RequestBody List<Integer> ids) {
		Result<?> result = Result.newOne();
		result.message = userService.deleteOnlineServeNosByIds(ids) ? "删除成功！" : "删除失败！";
		return result;
	}

	/**
	 * 在线客服历史页面
	 * 
	 * @author 王少辉
	 * @date 2015年6月3日 下午12:51:17
	 * 
	 * @return 返回在线客服页面
	 */
	@Remark("在线客服页面")
	@RequestMapping(value = "/servant/list/jsp", method = RequestMethod.GET)
	public String toOnlineSvntHisteryPage() {
		return "user/onlineServantList";
	}

	// /**
	// * 查询在线客服
	// *
	// * @author 王少辉
	// * @date 2015年6月3日 下午12:52:49
	// *
	// * @param request
	// * @return
	// */
	// @Remark("查询在线客服")
	// @RequestMapping(value = "/servant/list/get", method = RequestMethod.POST)
	// @ResponseBody
	// public JqGridPage<OnlineServeRecord> listOnlineSvnt(HttpServletRequest request) {
	// // 封装前台参数为JqGridRequest格式
	// JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
	// // 封装为PaginatedFilter格式
	// PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
	// //
	// PaginatedList<OnlineServeRecord> paginatedList = userService.getOnlineServeRecords(paginatedFilter);
	// //
	// JqGridPage<OnlineServeRecord> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
	// return jqGridPage;
	// }

	@Remark("获取客服会员对话信息")
	@RequestMapping(value = "/servant/member/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<OnlineServeRecord>> getSvntMemberContent(@RequestBody OnlineServeRecord svntRecord) {
		Result<List<OnlineServeRecord>> result = Result.newOne();

		try {
			List<OnlineServeRecord> svntRecList = userService.getServeRecordByMemeberId(svntRecord.getMemberId());
			if (CollectionUtils.isEmpty(svntRecList)) {
				result.message = "会员无对话记录";
			} else {
				result.data = svntRecList;
			}
		} catch (Exception e) {
			result.type = Type.error;
			result.message = "初始化数据出错！";
		}

		return result;
	}

	/**
	 * 商品咨询列表页面
	 * 
	 * @author 毛智东
	 * @date 2015年7月29日 下午4:14:22
	 * 
	 * @return
	 */
	@Remark("商品咨询列表页面")
	@RequestMapping(value = { "/inquiry/list/jsp" }, method = RequestMethod.GET)
	public String toInquiryListJsp() {
		return "interact/inquiryList";
	}

	/**
	 * 
	 * @author 毛智东
	 * @date 2015年7月29日 下午4:08:25
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询商品咨询")
	@RequestMapping(value = { "/inquiry/list/get/-mall", "/inquiry/list/get/-shop" }, method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<GoodsInquiry> getGoodsInquirys(HttpServletRequest request) {
		UserContext userContext = getUserContext(request);
		//
		AuthScope scope = EnumUtil.valueOf(AuthScope.class, userContext.getScope());
		assert scope != null;
		// 设置一个容错返回值
		PaginatedList<GoodsInquiry> paginatedList = PaginatedList.newOne();
		//
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		MapContext params = paginatedFilter.getFilterItems();

		PaginatedFilter filter = PaginatedFilter.newOne();
		MapContext filterItems = MapContext.newOne();
		if (scope == AuthScope.mall) {
			// 商城登录
			filterItems.put("shopId", null);
			filter.setFilterItems(filterItems);
			PaginatedList<Product> productList = GoodsService.getProductsByContext(filter);
			params.put("products", productList.getRows());
			//
			paginatedList = interactService.getGoodsInquiriesByFilter(paginatedFilter);
		} else if (scope == AuthScope.shop) {
			// 店铺登录
			filterItems.put("shopId", userContext.getScopeEntityId(scope.name()));
			filter.setFilterItems(filterItems);
			PaginatedList<Product> productList = GoodsService.getProductsByContext(filter);
			params.put("products", productList.getRows());
			//
			paginatedList = interactService.getGoodsInquiriesByFilter(paginatedFilter);
		}
		//
		JqGridPage<GoodsInquiry> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	// TODO-------WJJ-----begin--------------------客服历史------------------------------------

	/**
	 * 客服历史页面
	 * 
	 * @author WJJ
	 * @date 2015年9月24日 下午2:27:19
	 * 
	 * @return
	 */
	@Remark("客服历史页面")
	@RequestMapping(value = { "/service/record/jsp" }, method = RequestMethod.GET)
	public String toServiceRecordPage() {
		return "interact/serviceRecord";
	}

	/**
	 * 获取商城客服历史信息
	 * 
	 * @author WJJ
	 * @date 2015年9月24日 下午2:27:38
	 * 
	 * @param request
	 * @return jqGridPage
	 */
	@Remark("获取商城客服历史信息")
	@RequestMapping(value = "/service/record/list", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<OnlineServeRecord> getRecordsByPage(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<OnlineServeRecord> paginatedList = interactService.getRecordsByFilter(paginatedFilter);
		//
		JqGridPage<OnlineServeRecord> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	// -------WJJ-----end--------------------客服历史------------------------------------
}
