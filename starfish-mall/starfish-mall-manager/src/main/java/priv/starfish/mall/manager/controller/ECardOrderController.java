package priv.starfish.mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.*;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.order.entity.ECardOrder;
import priv.starfish.mall.service.ECardOrderService;
import priv.starfish.mall.service.ECardService;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/ecardOrder")
public class ECardOrderController extends BaseController {
	@Resource
	ECardService ecardService;
	
	@Resource
	ECardOrderService eCardOrderService;


	/**
	 * 商城后台——E卡订单列表
	 * 
	 * @author "WJJ"
	 * @date 2016年1月12日 下午2:28:26
	 * 
	 * @return
	 */
	@Remark("E卡订单列表")
	@RequestMapping(value = "/list/jsp/-mall", method = RequestMethod.GET)
	public String toEcardOrderMall() {
		return "ecardOrder/ecardOrderList";
	}
	
	/**
	 * 商城后台——分页查询E卡订单列表
	 * 
	 * @author "WJJ"
	 * @date 2016年1月12日 下午2:28:43
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询e卡订单列表")
	@RequestMapping(value = "/list/get/-mall", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<ECardOrder> getEcardOrderListByMall(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<ECardOrder> paginatedList = eCardOrderService.getByFilter(paginatedFilter);
		//
		JqGridPage<ECardOrder> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}
	
	/**
	 * 商户后台——E卡订单列表
	 * 
	 * @author "WJJ"
	 * @date 2016年1月12日 下午2:28:26
	 * 
	 * @return
	 */
	@Remark("E卡订单列表")
	@RequestMapping(value = "/list/jsp/-shop", method = RequestMethod.GET)
	public String toEcardOrderShop() {
		return "ecardOrder/shopEcardOrderList";
	}
	
	/**
	 * 商城后台——分页查询E卡订单列表
	 * 
	 * @author "WJJ"
	 * @date 2016年1月12日 下午2:28:43
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询e卡订单列表")
	@RequestMapping(value = "/list/get/-shop", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<ECardOrder> getEcardOrderListByShop(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntity().getId();

		MapContext filterItems = paginatedFilter.getFilterItems();
		filterItems.put("shopId", shopId);
		//
		PaginatedList<ECardOrder> paginatedList = eCardOrderService.getByFilter(paginatedFilter);
		//
		JqGridPage<ECardOrder> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}
	
	/**
	 * 获取E卡订单详情
	 * 
	 * @author "WJJ"
	 * @date 2016年2月17日 上午7:20:48
	 * 
	 * @param request
	 * @return
	 */
	@Remark("获取E卡订单详情")
	@RequestMapping(value = "/detail/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<ECardOrder> getSaleOrderDetail(HttpServletRequest request) {
		Result<ECardOrder> result = Result.newOne();

		try {
			String orderNo = request.getParameter("orderNo");
			ECardOrder eCardOrder = eCardOrderService.getECardOrderByNo(orderNo);
			result.data = eCardOrder;
		} catch (Exception e) {
			result.type = Result.Type.error;
			result.data = null;
		}
		return result;
	}

}
