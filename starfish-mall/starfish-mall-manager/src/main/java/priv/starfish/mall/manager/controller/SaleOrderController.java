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
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.misc.BizParamInfo;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.order.entity.SaleOrder;
import priv.starfish.mall.order.po.SaleOrderActionPo;
import priv.starfish.mall.order.po.SaleOrderPo;
import priv.starfish.mall.pay.entity.PayWay;
import priv.starfish.mall.service.SaleOrderService;
import priv.starfish.mall.service.SettingService;
import priv.starfish.mall.service.ShopService;
import priv.starfish.mall.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Remark("订单相关处理")
@Controller
@RequestMapping(value = "/saleOrder")
public class SaleOrderController extends BaseController {
	@Resource
	private SaleOrderService saleOrderService;

	@Resource
	private ShopService shopService;

	@Resource
	private UserService userService;

	@Resource
	private SettingService settingService;

	/**
	 * 返回销售订单管理页面
	 * 
	 * @author wangdi
	 * @date 2015年10月13日 下午6:00:18
	 * 
	 * @return
	 */
	@Remark("销售订单管理页面")
	@RequestMapping(value = "/list/jsp/-mall", method = RequestMethod.GET)
	public String toSaleOrderList() {
		return "order/saleOrderList";
	}

	/**
	 * 返回店铺销售订单管理页面
	 * 
	 * @author wangdi
	 * @date 2015年10月21日 上午11:37:35
	 * 
	 * @return
	 */
	@Remark("店铺销售订单管理页面")
	@RequestMapping(value = "/list/jsp/-shop", method = RequestMethod.GET)
	public String toShopSaleOrderList() {
		return "order/shopSaleOrderList";
	}

	@Remark("店铺代理下订单页面")
	@RequestMapping(value = "/create/jsp/-shop", method = RequestMethod.GET)
	public String toShopSaleOrder() {
		return "order/submitSaleOrder";
	}

	/**
	 * 分页查询所有销售订单
	 * 
	 * @author wangdi
	 * @date 2015年10月13日 下午5:25:51
	 * 
	 * @param request
	 * @return
	 */
	@Remark("根据过滤条件分页查询所有销售订单")
	@RequestMapping(value = "/list/get/-mall", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SaleOrder> getSaleOrders(HttpServletRequest request) {
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		PaginatedList<SaleOrder> paginatedList = saleOrderService.getSaleOrdersByFilter(paginatedFilter);
		JqGridPage<SaleOrder> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 根据过滤条件分页查询当前店铺的销售订单
	 * 
	 * @author wangdi
	 * @date 2015年10月21日 上午11:38:40
	 * 
	 * @param request
	 * @return
	 */
	@Remark("根据过滤条件分页查询当前店铺的销售订单")
	@RequestMapping(value = "/list/get/-shop", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SaleOrder> getSaleOrdersByContext(HttpServletRequest request) {
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		// 读取上下文环境中的店铺ID，并放入filterItems中
		UserContext userContext = getUserContext(request);
		Map<String, Object> prarms = new HashMap<String, Object>();
		MapContext map = paginatedFilter.getFilterItems();
		if (map != null) {
			prarms.putAll(map);
		}
		Integer shopId = userContext.getScopeEntityId(AuthScope.shop.name());
		if (shopId != null) {
			prarms.put("shopId", shopId);
		} else {
			prarms.put("shopId", -1);
		}
		paginatedFilter.setFilterItems(prarms);
		PaginatedList<SaleOrder> paginatedList = saleOrderService.getSaleOrdersByFilter(paginatedFilter);
		JqGridPage<SaleOrder> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 代理下单生成订单
	 * 
	 * @author wangdi
	 * @date 2015年10月23日 下午4:38:37
	 * 
	 * @param request
	 * @return
	 */
	@Remark("代理下单生成订单")
	@RequestMapping(value = "/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<String, Object>> saveSaleOrderAsProxy(HttpServletRequest request, @RequestBody SaleOrderPo saleOrderPo) {
		Result<Map<String, Object>> result = Result.newOne();
		try {
			UserContext userContext = getUserContext(request);
			Integer shopId = userContext.getScopeEntityId(AuthScope.shop.name());
			Integer actorId = userContext.getUserId();
			saleOrderPo.setShopId(shopId);
			saleOrderPo.setActorId(actorId);
			// 下单
			if (saleOrderService.saveSaleOrderAsProxy(saleOrderPo)) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("id", saleOrderPo.getId());
				resultMap.put("no", saleOrderPo.getNo());
				result.type = Result.Type.info;
				result.data = resultMap;
				result.message = "代理下单成功";
			} else {
				result.type = Result.Type.error;
				result.message = "代理下单失败";
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
			result.message = "代理下单失败！";
		}

		return result;
	}

	/**
	 * 执行订单状态转换动作 TODO:临时将动作处理逻辑写死在代码里，后续需要调整为使用订单状态处理引擎进行处理
	 * 
	 * @author wangdi
	 * @date 2015年10月24日 下午2:28:46
	 * 
	 * @param request
	 * @return
	 */
	@Remark("执行订单状态转换动作")
	@RequestMapping(value = "/action/execute/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> executeSaleOrderAction(HttpServletRequest request, @RequestBody SaleOrderActionPo actionPo) {
		Result<?> result = Result.newOne();
		try {
			String action = actionPo.getActionName();
			Long orderId = actionPo.getOrderId();

			if (action == null) {
				logger.warn(String.format("Parameter action is null. Class:%s Method:executeSaleOrderAction", getClass().getName()));
				result.type = Result.Type.error;
				result.message = "操作未指定";
				return result;
			}

			if (orderId == null) {
				logger.warn(String.format("Parameter orderId is null. Class:%s Method:executeSaleOrderAction", getClass().getName()));
				result.type = Result.Type.error;
				result.message = "订单未指定";
				return result;
			}

			// 获取上下文信息
			UserContext userContext = getUserContext(request);
			Integer shopId = userContext.getScopeEntityId(AuthScope.shop.name());
			Integer actorId = userContext.getUserId();
			actionPo.setActorId(actorId);

			SaleOrder order = saleOrderService.getSaleOrderById(orderId);
			if (order == null) {
				logger.info(String.format("The order does not exist，orderId：%d", orderId));
				result.type = Result.Type.error;
				result.message = "订单不存在";
				return result;
			}
			if (!shopId.equals(order.getShopId())) {
				logger.info(String.format("The actor has no permission to execute action. action: %s orderId： %d actorId: %d", action, orderId, actorId));
				result.type = Result.Type.error;
				result.message = "非当前门店订单，无操作权限";
				return result;
			}
			actionPo.setUserId(order.getUserId());

			// 根据动作类型选择执行不同业务逻辑和状态转换
			if (action.equals("complete")) {
				String doneCode = actionPo.getDoneCode();
				if (doneCode == null) {
					logger.info(String.format("Parameter doneCode is null. Class:%s Method:executeSaleOrderAction", getClass().getName()));
					result.type = Result.Type.error;
					result.message = "未提供服务完成确认码！";
					return result;
				}

				if (!order.getPayState().equals("paid")) {
					logger.info(String.format("Not correct pay state to execute the action. action: %s orderId： %d actorId: %d payState: %s", action, orderId, actorId, order.getPayState()));
					result.type = Result.Type.error;
					result.message = "用户未支付，无法完成该服务！";
					return result;
				}
				// 验证服务完成确认码
				String code = order.getDoneCode();
				if (code == null) {
					logger.info(String.format("doneCode not exist! orderId : %d", orderId));
					result.type = Result.Type.error;
					result.message = "订单信息异常，服务完成确认码不存在！";
					return result;
				}
				if (!code.equals(doneCode)) {
					logger.debug(String.format("Invalid done code! doneCode : %s", doneCode));
					result.type = Result.Type.warn;
					result.message = "错误的服务完成确认码！";
					return result;
				}

				// 更新订单完成状态
				if (!saleOrderService.updateSaleOrderForFinishAsProxy(actionPo)) {
					logger.warn(String.format("Update sale order failed! orderId： %s", orderId));
					result.type = Result.Type.error;
					result.message = "更新订单完成状态失败！";
					return result;
				}
			} else if (action.equals("addInfo")) {
				// 补充订单信息
				if (!saleOrderService.updateSaleOrderForAddInfo(actionPo)) {
					logger.warn(String.format("Update sale order for add info failed! orderId： %s", orderId));
					result.type = Result.Type.warn;
					result.message = "补充订单信息失败";
					return result;
				}
			} else if (action.equals("cancel")) {
				// 取消订单
				if (!saleOrderService.updateSaleOrderForCancelAsProxy(actionPo)) {
					logger.warn(String.format("Update sale order for cancel failed! orderId： %s", orderId));
					result.type = Result.Type.warn;
					result.message = "取消订单失败";
					return result;
				}
			} else {
				logger.warn(String.format("Undefined action： %s.", action));
				result.type = Result.Type.warn;
				result.message = "操作类型未定义";
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
			result.message = "执行操作过程出现异常！";
		}
		return result;
	}

	/**
	 * 获取订单星标状态
	 * 
	 * @author wangdi
	 * @date 2015年10月27日 下午6:28:41
	 * 
	 * @return
	 */
	@Remark("获取订单星标状态")
	@RequestMapping(value = "/starFlag/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getStarFlag(Long orderId) {
		Result<Boolean> result = Result.newOne();
		try {
			if (orderId == null) {
				logger.error(String.format("Parameter orderId is null. Class:%s Method:getStarFlag", getClass().getName()));
				result.type = Result.Type.error;
				result.message = "订单未指定";
				return result;
			}

			SaleOrder order = saleOrderService.getSaleOrderById(orderId);
			if (order == null) {
				logger.error(String.format("The order does not exist，orderId：%s", orderId));
				result.type = Result.Type.error;
				result.message = "订单不存在！";
				return result;
			}
			// 返回值
			result.data = order.getStarFlag();
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
			result.message = "执行操作过程出现异常！";
		}

		return result;
	}

	/**
	 * 设置订单星标状态
	 * 
	 * @author wangdi
	 * @date 2015年10月27日 下午6:28:41
	 * 
	 * @return
	 */
	@Remark("设置订单星标状态")
	@RequestMapping(value = "/starFlag/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> setStarFlag(Long orderId, Boolean starFlag) {
		Result<?> result = Result.newOne();
		try {
			if (orderId == null) {
				logger.error(String.format("Parameter orderId is null. Class:%s Method:setStarFlag", getClass().getName()));
				result.type = Result.Type.error;
				result.message = "订单未指定！";
				return result;
			}
			if (starFlag == null) {
				logger.error(String.format("Parameter starFlag is null. Class:%s Method:setStarFlag", getClass().getName()));
				result.type = Result.Type.error;
				result.message = "星标状态未指定！";
				return result;
			}

			SaleOrder order = saleOrderService.getSaleOrderById(orderId);
			if (order == null) {
				logger.error(String.format("The order does not exist，orderId：%d", orderId));
				result.type = Result.Type.error;
				result.message = "订单不存在！";
				return result;
			}

			// TODO:可以考虑验证一下上下文中门店的ID是否与订单关联的门店ID一致

			order.setStarFlag(starFlag);
			if (!saleOrderService.updateSaleOrder(order)) {
				logger.error(String.format("Failed to update star flag，orderId：%d", orderId));
				result.type = Result.Type.error;
				result.message = "更新星标状态失败！";
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
			result.message = "执行操作过程出现异常！";
		}

		return result;
	}

	@Remark("修改销售订单预约时间")
	@RequestMapping(value = "/planTime/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<SaleOrder> updateSaleOrder(@RequestBody MapContext requestData) {
		Result<SaleOrder> result = Result.newOne();
		Long id = requestData.getTypedValue("id", Long.class);
		Date planTime = requestData.getTypedValue("planTime", Date.class);
		SaleOrder saleOrder = saleOrderService.getSaleOrderById(id);
		Integer currentPlanModTimes = saleOrder.getPlanModTimes();
		Integer maxPlanTimeModifyTimes = BizParamInfo.maxPlanTimeModifyTimes;
		if (currentPlanModTimes >= maxPlanTimeModifyTimes) {
			result.type = Type.warn;
			result.message = "已经达到预约时间修改次数上限";
			return result;
		}
		saleOrder.setPlanTime(planTime);
		saleOrder.setPlanModTimes(currentPlanModTimes + 1);
		boolean ok = saleOrderService.updateSaleOrder(saleOrder);
		if (ok) {
			result.data = saleOrder;
			result.message = "修改成功";
		} else {
			result.type = Type.warn;
			result.message = "修改失败";
		}
		return result;
	}

	/**
	 * 获取销售订单详情
	 * 
	 * @author wangdi
	 * @date 2016年1月27日 下午4:54:49
	 * 
	 * @param request
	 * @return
	 */
	@Remark("获取销售订单详情")
	@RequestMapping(value = "/detail/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SaleOrder> getSaleOrderDetail(HttpServletRequest request) {
		Result<SaleOrder> result = Result.newOne();

		try {
			String orderId = request.getParameter("orderId");
			if (orderId == null) {
				result.type = Result.Type.warn;
				result.message = "无隶属订单";
				return result;
			}
			// TODO:后续需要增加订单是否归属门店的校验
			SaleOrder saleOrder = saleOrderService.getSaleOrderDetailById(Long.parseLong(orderId));
			if (saleOrder == null) {
				result.type = Result.Type.warn;
				result.message = "隶属订单不存在";
				return result;
			}

			result.data = saleOrder;
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
			result.message = "获取销售订单详情失败";
		}
		return result;
	}

	@Remark("获取处于启用状态的支付方式,用于select选项")
	@RequestMapping(value = "/payWay/usbale/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SelectList> getUsablePayWay() {
		Result<SelectList> result = Result.newOne();
		SelectList selectList = SelectList.newOne();
		List<PayWay> payWays = settingService.getUsablePayWay();
		selectList.setUnSelectedItem("", "- 请选择 -");
		if (payWays.size() > 0) {
			for (PayWay payWay : payWays) {
				selectList.addItem(payWay.getCode(), payWay.getName());
			}
		}
		selectList.setDefaultValue("");
		result.data = selectList;
		return result;
	}

}
