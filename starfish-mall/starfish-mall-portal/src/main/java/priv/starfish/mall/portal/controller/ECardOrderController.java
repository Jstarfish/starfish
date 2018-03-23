package priv.starfish.mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.Result;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.ecard.entity.ECard;
import priv.starfish.mall.order.dto.ECardDto;
import priv.starfish.mall.order.entity.ECardOrder;
import priv.starfish.mall.service.ECardOrderService;
import priv.starfish.mall.service.ECardService;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Remark("e卡订单相关处理")
@Controller
@RequestMapping(value = "/eCardOrder")
public class ECardOrderController extends BaseController {
	@Resource
	ECardOrderService eCardOrderService;

	@Resource
	ECardService eCardService;

	/**
	 * e卡订单详情页面-已取消
	 * 
	 * @author wangdi
	 * @date 2015年11月16日 下午4:41:11
	 * 
	 * @param request
	 * @return
	 */
	@Remark("e卡订单详情页面-已取消")
	@RequestMapping(value = "/detail/cancelled/jsp", method = RequestMethod.GET)
	public String toECardOrderDetailCancelled(HttpServletRequest request) {
		return "ucenter/order/ecardOrderDetailCancelled";
	}

	/**
	 * e卡订单详情页面-已完成
	 * 
	 * @author wangdi
	 * @date 2015年11月16日 下午4:41:22
	 * 
	 * @param request
	 * @return
	 */
	@Remark("e卡订单详情页面-已完成")
	@RequestMapping(value = "/detail/finished/jsp", method = RequestMethod.GET)
	public String toECardOrderDetailFinished(HttpServletRequest request) {
		return "ucenter/order/ecardOrderDetailFinished";
	}

	/**
	 * e卡订单详情页面-待支付
	 * 
	 * @author wangdi
	 * @date 2015年11月16日 下午4:41:38
	 * 
	 * @param request
	 * @return
	 */
	@Remark("e卡订单详情页面-待支付")
	@RequestMapping(value = "/detail/unpaid/jsp", method = RequestMethod.GET)
	public String toECardOrderDetailUnpaid(HttpServletRequest request) {
		return "ucenter/order/ecardOrderDetailUnpaid";
	}

	/**
	 * 分页获取用户e卡订单列表
	 * 
	 * @author wangdi
	 * @date 2015年11月16日 下午4:41:57
	 * 
	 * @param request
	 * @param paginatedFilter
	 * @return
	 */
	@Remark("分页获取用户e卡订单列表")
	@RequestMapping(value = "/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<PaginatedList<ECardOrder>> getECardOrders(HttpServletRequest request, @RequestBody PaginatedFilter paginatedFilter) {
		Result<PaginatedList<ECardOrder>> result = Result.newOne();
		// 获取当前登录用户ID
		UserContext userContext = getUserContext(request);
		Integer userId = userContext.getUserId();
		// 添加过滤条件
		MapContext filterItems = paginatedFilter.getFilterItems();
		filterItems.put("userId", userId);
		// 获取e卡订单
		PaginatedList<ECardOrder> eCardOrders = eCardOrderService.getECardOrdersByFilter(paginatedFilter);
		result.data = eCardOrders;

		return result;
	}

	/**
	 * 获取用户e卡订单详情
	 * 
	 * @author wangdi
	 * @date 2015年11月16日 下午4:42:12
	 * 
	 * @param request
	 * @return
	 */
	@Remark("获取用户e卡订单详情")
	@RequestMapping(value = "/detail/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<ECardOrder> getECardOrder(HttpServletRequest request) {
		Result<ECardOrder> result = Result.newOne();
		String orderId = request.getParameter("orderId");
		if (orderId == null) {
			logger.warn("Not enough parameters.");
			result.type = Result.Type.warn;
			result.message = "缺少参数";
			return result;
		}
		// 获取当前登录用户ID
		UserContext userContext = getUserContext(request);
		Integer userId = userContext.getUserId();

		ECardOrder eCardOrder = eCardOrderService.getECardOrderByOrderIdAndUserId(Integer.parseInt(orderId), userId);
		if (eCardOrder == null) {
			logger.warn(String.format("Order not exist or not belong to current user. orderId:%s userId:%d", orderId, userId));
			result.type = Result.Type.warn;
			result.message = "订单不存在或不属于当前用户";
			return result;
		}
		// 查询结果
		result.data = eCardOrder;
		return result;
	}
	
	/**
	 * 获取E卡订单详情——状态
	 * 
	 * @author "WJJ"
	 * @date 2016年2月17日 上午7:20:48
	 * 
	 * @param request
	 * @return
	 */
	@Remark("获取E卡订单详情——状态")
	@RequestMapping(value = "/status/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<ECardOrder> getSaleOrderDetail(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<ECardOrder> result = Result.newOne();

		try {
			String orderNo = requestData.getTypedValue("orderNo", String.class);
			ECardOrder eCardOrder = eCardOrderService.getECardOrderByNo(orderNo);
			result.data = eCardOrder;
		} catch (Exception e) {
			result.type = Result.Type.error;
			result.data = null;
		}
		return result;
	}

	/**
	 * 取消e卡订单
	 * 
	 * @author wangdi
	 * @date 2015年11月16日 下午3:33:18
	 * 
	 * @param request
	 * @return
	 */
	@Remark("取消e卡订单")
	@RequestMapping(value = "/cancel/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> cancelECardOrder(HttpServletRequest request) {
		Result<Boolean> result = Result.newOne();
		try {
			String orderId = request.getParameter("orderId");
			if (orderId == null) {
				logger.warn("Not enough parameters.");
				result.type = Result.Type.warn;
				result.message = "缺少参数";
				return result;
			}
			// 获取当前登录用户ID
			UserContext userContext = getUserContext(request);
			Integer userId = userContext.getUserId();

			result.data = eCardOrderService.updateECardOrderForCancel(Integer.parseInt(orderId), userId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
			result.message = "取消订单失败";
		}
		return result;
	}

	// TODO---------------------------------------------e卡提交---------------------------------------------

	@Remark("e卡提交页面")
	@RequestMapping(value = "/submit/jsp", method = RequestMethod.GET)
	public String toSubmitJsp() {
		return "ecardOrder/submit";
	}

	@Remark("e卡提交信息")
	@RequestMapping(value = "/submit/info/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<ECard> getECardInfo(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<ECard> result = Result.newOne();
		try {
			String code = requestData.getTypedValue("code", String.class);
			if (!StrUtil.isNullOrBlank(code)) {
				ECard eCard = eCardService.getECardById(code);
				result.data = eCard;
			} else {
				result.type = Result.Type.warn;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
		}

		return result;
	}

	@Remark("e卡提交")
	@RequestMapping(value = "/submit/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> submitECard(HttpServletRequest request, @RequestBody ECardDto cardDto) {
		Result<Integer> result = Result.newOne();
		try {
			if (cardDto != null) {
				UserContext userContext = getUserContext(request);
				Integer orderId = eCardOrderService.createECardOrder(userContext, cardDto);
				if (orderId != null) {
					result.data = orderId;
				} else {
					result.type = Result.Type.warn;
					result.message = "提交失败";
				}
			} else {
				result.type = Result.Type.warn;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
		}

		return result;
	}

	@Remark("e卡提交订单成功页面")
	@RequestMapping(value = "/submit/result/jsp", method = RequestMethod.GET)
	public String toSubmitResultJsp() {
		return "ecardOrder/submitResult";
	}

	@Remark("e卡提交订单成功加载信息")
	@RequestMapping(value = "/submit/result/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<ECardOrder> getECardOrderInfo(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<ECardOrder> result = Result.newOne();
		try {
			Integer orderId = requestData.getTypedValue("orderId", Integer.class);
			if (orderId != null) {
				UserContext userContext = getUserContext(request);
				ECardOrder eCardOrder = eCardOrderService.getECardOrderByOrderId(orderId, userContext.getUserId());
				result.data = eCardOrder;
			} else {
				result.type = Result.Type.warn;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
		}
		return result;
	}
}
