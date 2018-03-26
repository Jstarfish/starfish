package priv.starfish.mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.*;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.order.entity.SaleOrder;
import priv.starfish.mall.service.DistShopService;
import priv.starfish.mall.service.SaleOrderService;
import priv.starfish.mall.service.SearchService;
import priv.starfish.mall.service.StatisService;
import priv.starfish.mall.settle.entity.DistSettleRec;
import priv.starfish.mall.shop.entity.DistShop;
import priv.starfish.mall.shop.entity.DistShopSvc;
import priv.starfish.mall.statis.dto.DistShopSumDto;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 分销店铺Controller
 * 
 * @author "郝江奎"
 * @date 2016年1月19日 上午10:07:05
 *
 */
@Remark("分销店铺Controller")
@Controller
@RequestMapping("/distshop")
public class DistShopController extends BaseController {

	@Resource
	private DistShopService distShopService;

	@Resource
	private SaleOrderService saleOrderService;

	@Resource
	private StatisService statisService;
	
	@Resource
	private SearchService searchService;

	// --------------------------------------- 卫星店铺----------------------------------------------

	/**
	 * 转向商城卫星店列表界面
	 * 
	 * @author 郝江奎
	 * @date 2016年1月18日 下午2:50:51
	 * 
	 * @return String
	 */
	@Remark("转向商城卫星店列表界面")
	@RequestMapping(value = "/wxshop/list/jsp/-mall", method = RequestMethod.GET)
	public String toWxshopListMall() {
		return "shop/wxshopList-mall";
	}

	/**
	 * 转向店铺卫星店列表界面
	 * 
	 * @author 郝江奎
	 * @date 2016年1月18日 下午3:00:51
	 * 
	 * @return String
	 */
	@Remark("转向店铺卫星店列表界面")
	@RequestMapping(value = "/wxshop/list/jsp/-shop", method = RequestMethod.GET)
	public String toWxshopListShop() {
		return "shop/wxshopList-shop";
	}

	/**
	 * 分页查询卫星店铺
	 * 
	 * @author 郝江奎
	 * @date 2016年1月19日 上午9:59:00
	 * 
	 * @param request
	 * @return JqGridPage<DistShop>
	 */
	@Remark("分页查询卫星店铺")
	@RequestMapping(value = "/wxshop/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<DistShop> listDistShop(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<DistShop> paginatedList = distShopService.getDistShopsByFilter(paginatedFilter);
		//
		JqGridPage<DistShop> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 添加卫星店铺
	 * 
	 * @author 郝江奎
	 * @date 2016-1-19 上午11:01:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("增加卫星店铺")
	@RequestMapping(value = "/wxshop/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> addWxshop(HttpServletRequest request, @RequestBody DistShop distShop) {
		Result<?> result = Result.newOne();
		boolean ok = false;
		ok = distShopService.saveDistShop(distShop);
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
	 * 修改卫星店铺
	 * 
	 * @author 郝江奎
	 * @date 2016-1-19 上午11:21:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("修改卫星店铺")
	@RequestMapping(value = "/wxshop/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> updateWxshop(HttpServletRequest request, @RequestBody DistShop distShop) {
		Result<?> result = Result.newOne();
		boolean ok = false;
		ok = distShopService.updateDistShop(distShop);
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
	 * 删除卫星店铺
	 * 
	 * @author 郝江奎
	 * @date 2016-1-19 上午11:30:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("删除卫星店铺")
	@RequestMapping(value = "/wxshop/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteWxshop(HttpServletRequest request, @RequestBody DistShop distShop) {
		Result<?> result = Result.newOne();
		boolean ok = false;
		ok = distShopService.deleteDistShopById(distShop.getId());
		//
		if (ok) {
			result.message = "删除成功!";
		} else {
			result.type = Type.warn;
			result.message = "删除失败";
		}

		return result;
	}

	/**
	 * 卫星店铺禁用启用
	 * 
	 * @author 郝江奎
	 * @date 2016年1月19日 下午17:26:14
	 * 
	 * @param request
	 * @param distShop
	 * @return Result<?>
	 */
	@Remark("卫星店铺禁用启用")
	@RequestMapping(value = "/wxShop/isDisabled/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> changeIsDisabled(HttpServletRequest request, @RequestBody DistShop distShop) {
		Result<?> result = Result.newOne();
		boolean disabled = distShop.getDisabled() == false ? true : false;
		boolean flag = distShopService.updateDistShopDisableState(distShop.getId(), disabled);
		if (flag) {
			result.message = "操作成功";
		} else {
			result.message = "操作失败";
			result.type = Result.Type.error;
		}
		return result;
	}

	/**
	 * 卫星店铺审核
	 * 
	 * @author 郝江奎
	 * @date 2019年1月19日 下午7:26:14
	 * 
	 * @param request
	 * @param distShop
	 * @return Result<?>
	 */
	@Remark("卫星店铺审核")
	@RequestMapping(value = "/wxShop/audit/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> auditShop(HttpServletRequest request, @RequestBody DistShop distShop) {
		UserContext userContext = getUserContext(request);
		Result<?> result = Result.newOne();
		boolean flag = false;
		if (userContext.isSysUser()) {
			flag = distShopService.updateDistShopAudit(distShop, userContext);
		}
		if (flag) {
			result.message = "操作成功";
		} else {
			result.message = "操作失败";
			result.type = Result.Type.warn;
		}
		return result;
	}

	/**
	 * 批量审核卫星店铺
	 * 
	 * @author 郝江奎
	 * @date 2016年1月19日 下午19:55:28
	 * 
	 * @param request
	 * @param requestData
	 * @return Result<String>
	 */
	@Remark("批量审核卫星店铺")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/wxShops/audit/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> auditShops(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<String> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		List<String> list = requestData.getTypedValue("ids", TypeUtil.Types.StringList.getClass());
		Integer auditStatus = requestData.getTypedValue("auditStatus", Integer.class);
		String auditorDesc = requestData.getTypedValue("auditorDesc", String.class);
		boolean flag = distShopService.batchUpdateDistShopsAudit(list, auditStatus, auditorDesc, userContext);
		if (flag) {
			result.message = "操作成功";
		} else {
			result.message = "操作失败";
			result.type = Result.Type.error;
		}
		return result;
	}

	// --------------------------------------- 合作店铺----------------------------------------------

	/**
	 * 转向店铺后台合作店铺列表界面
	 * 
	 * @author 郝江奎
	 * @date 2016年1月20日 上午10:50:51
	 * 
	 * @return String
	 */
	@Remark("转向店铺后台合作店铺列表界面")
	@RequestMapping(value = "/hzshop/list/jsp/-shop", method = RequestMethod.GET)
	public String toHzshopListShop() {
		return "shop/hzshopList-shop";
	}

	/**
	 * 转向商城后台合作店铺列表界面
	 * 
	 * @author 郝江奎
	 * @date 2016年1月20日 上午10:51:51
	 * 
	 * @return String
	 */
	@Remark("转向商城后台合作店铺列表界面")
	@RequestMapping(value = "/hzshop/list/jsp/-mall", method = RequestMethod.GET)
	public String toHzshopListMall() {
		return "shop/hzshopList-mall";
	}

	/**
	 * 店铺后台分页查询卫星店铺
	 * 
	 * @author 郝江奎
	 * @date 2016年1月19日 上午9:59:00
	 * 
	 * @param request
	 * @return JqGridPage<DistShop>
	 */
	@Remark("店铺后台分页查询卫星店铺")
	@RequestMapping(value = "/wxshop/list/get/-shop", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<DistShop> getDistShopList(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		// 获取当前店铺id
		MapContext filter = paginatedFilter.getFilterItems();
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getUserId();
		ScopeEntity scopeEntity = userContext.getScopeEntity();
		filter.put("ownerShopId", scopeEntity.getId());
		//
		PaginatedList<DistShop> paginatedList = distShopService.getDistShopsByFilterAndShopId(paginatedFilter, shopId);
		//
		JqGridPage<DistShop> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	@Remark("卫星店铺审核")
	@RequestMapping(value = "/wxShopSvc/audit/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> auditShopSvc(HttpServletRequest request, @RequestBody DistShopSvc distShopSvc) {
		UserContext userContext = getUserContext(request);
		Result<?> result = Result.newOne();
		boolean flag = false;
		if (userContext.isSysUser()) {
			distShopSvc.setAuditorId(userContext.getUserId());
			distShopSvc.setAuditorName(userContext.getUserName());
			distShopSvc.setAuditTime(new Date());
			distShopSvc.setApplyFlag(false);
			flag = distShopService.updateDistShopSvc(distShopSvc);
		}
		if (flag) {
			result.message = "操作成功";
		} else {
			result.message = "操作失败";
			result.type = Result.Type.warn;
		}
		return result;
	}
	
	@Remark("启动店铺增量索引")
	@RequestMapping(value = "/doc/index/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> indexDoc() {
		Result<Object> result = Result.newOne();
		searchService.indexDistShopDocs(false);
		result.message = "操作成功!";
		return result;
	}

	// ------------------------------------------------卫星店订单相关----------------------------------------------------------

	/**
	 * 店铺-卫星店订单管理
	 * 
	 * @author 李江
	 * @date 2016年2月20日 上午11:34:09
	 * 
	 * @return
	 */
	@Remark("店铺下卫星店订单管理")
	@RequestMapping(value = "/order/list/jsp", method = RequestMethod.GET)
	public String toDistShopOrder() {
		return "order/distShopOrderList";
	}

	/**
	 * 店铺-分页查询卫星店订单列表
	 * 
	 * @author 李江
	 * @date 2016年2月20日 上午11:34:09
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询店铺下卫星店订单列表")
	@RequestMapping(value = "/order/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SaleOrder> getDistShopOrderList(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		// 获取登录店铺编号
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntity().getId();
		MapContext filterItems = paginatedFilter.getFilterItems();
		filterItems.put("shopId", shopId); // 标示数据来源店铺或合作店
		// 获取数据
		PaginatedList<SaleOrder> paginatedList = saleOrderService.selectDistShopOrderByFilter(paginatedFilter);
		// 封装数据
		JqGridPage<SaleOrder> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}
	
	/**
	 * 店铺-卫星店订单结算
	 * 
	 * @author 李江
	 * @date 2016年2月20日 上午11:34:09
	 * 
	 * @return
	 */
	@Remark("店铺下卫星店订单结算管理")
	@RequestMapping(value = "/order/settle/list/jsp", method = RequestMethod.GET)
	public String toDistShopOrderSettle() {
		return "order/distShopOrderSettleList";
	}

	/**
	 * 店铺-分页查询卫星店订单结算列表
	 * 
	 * @author 李江
	 * @date 2016年2月20日 上午11:34:09
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询店铺下卫星店订单结算列表")
	@RequestMapping(value = "/order/settle/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SaleOrder> getDistShopOrderSettleList(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		// 获取登录店铺编号
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntity().getId();
		MapContext filterItems = paginatedFilter.getFilterItems();
		filterItems.put("shopId", shopId); // 标示数据来源店铺或合作店
		// 获取数据
		PaginatedList<SaleOrder> paginatedList = saleOrderService.selectDistShopOrderSettleByFilter(paginatedFilter);
		// 封装数据
		JqGridPage<SaleOrder> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 店铺——批量结算合作店订单
	 * 
	 * @author 李江
	 * @date 2016年2月20日 上午11:34:09
	 * 
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@Remark("批量结算店铺下合作店订单")
	@RequestMapping(value = "/order/batch/settle/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<Long>> batchSettleDistShopOrder(HttpServletRequest request) throws Exception {
		Result<List<Long>> result = Result.newOne();
		boolean status = true;
		String orderIdStr = request.getParameter("orderIds");
		String amount = request.getParameter("amount");
		String theAmount = request.getParameter("theAmount");
		String desc = request.getParameter("desc");
		String theTime = request.getParameter("theTime");
		if (orderIdStr != null && theAmount != null) {
			DistSettleRec distSettleRec = new DistSettleRec();
			if (theTime == null || "".equals(theTime)) {
				distSettleRec.setTheTime(new Date());
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				distSettleRec.setTheTime(sdf.parse(theTime));
			}
			distSettleRec.setAmount(new BigDecimal(amount));
			distSettleRec.setTheAmount(new BigDecimal(theAmount));
			distSettleRec.setDesc(desc);

			List<Long> orderIds = new ArrayList<Long>();
			for (String orderId : orderIdStr.split(",")) {
				orderIds.add(Long.parseLong(orderId));
			}

			// 批量结算
			status = distShopService.updateDistShopSettleByOrderIds(orderIds, distSettleRec);
			result.message = "操作成功!";
		}
		//
		if (!status) {
			result.type = Type.warn;
			result.message = "操作失败!";
			return result;
		}
		//
		return result;
	}

	// ------------------------------------------------卫星店统计相关----------------------------------------------------------

	/**
	 * 商城-卫星店铺业绩统计页面
	 * 
	 * @author 李江
	 * @date 2016年2月20日 上午11:34:09
	 * 
	 * @return
	 */
	@Remark("商城下卫星店铺业绩统计")
	@RequestMapping(value = "/achievement/list/jsp", method = RequestMethod.GET)
	public String toDistShopAchievement() {
		return "statis/distShopStatisList";
	}

	/**
	 * 商城——分页查询卫星店统计信息
	 * 
	 * @author 李江
	 * @date 2016年2月20日 上午11:34:09
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询商城下卫星店统计信息")
	@RequestMapping(value = "/achievement/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<DistShopSumDto> getStatisAchievementList(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		// 获取数据
		PaginatedList<DistShopSumDto> paginatedList = statisService.selectDistShopStatisByFilter(paginatedFilter);
		// 封装数据
		JqGridPage<DistShopSumDto> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 获取卫星店统计信息
	 * 
	 * @author 李江
	 * @date 2016年2月20日 上午11:34:09
	 * 
	 * @return
	 */
	@Remark("获取卫星店统计信息")
	@RequestMapping(value = "/statis/count/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<DistShopSumDto> getDistShopStatisCount(HttpServletRequest request, @RequestBody MapContext filter) {
		Result<DistShopSumDto> result = Result.newOne();
		try {
			// 获取数据
			result.data = statisService.getDistShopStatis(filter);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
		}
		return result;
	}

}
