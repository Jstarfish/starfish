package priv.starfish.mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.PayStateType;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.*;
import priv.starfish.mall.car.entity.UserCar;
import priv.starfish.mall.car.entity.UserCarSvcRec;
import priv.starfish.mall.comn.dict.UserType;
import priv.starfish.mall.order.dict.OrderAction;
import priv.starfish.mall.order.dict.OrderType;
import priv.starfish.mall.order.dto.OrderStateTypeCountDto;
import priv.starfish.mall.order.dto.SaleOrderInfo;
import priv.starfish.mall.order.entity.SaleOrder;
import priv.starfish.mall.order.entity.SaleOrderSvc;
import priv.starfish.mall.order.entity.UserSvcPackTicket;
import priv.starfish.mall.order.po.SaleOrderPo;
import priv.starfish.mall.service.*;
import priv.starfish.mall.service.misc.XOrderMessageProxy;
import priv.starfish.mall.shop.entity.DistShop;
import priv.starfish.mall.shop.entity.DistShopSvc;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.web.base.CartHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Remark("订单相关处理")
@Controller
@RequestMapping(value = "/saleOrder")
public class SaleOrderController extends BaseController {

	@Resource
	SaleOrderService saleOrderService;

	@Resource
	UserService userService;

	@Resource
	ECardService eCardService;

	@Resource
	CarService carService;

	@Resource
	DistShopService distShopService;

	@Remark("查询订单不同状态记录数")
	@RequestMapping(value = "/count/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<OrderStateTypeCountDto> getSaleOrderCount(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<OrderStateTypeCountDto> result = Result.newOne();
		try {
			UserContext usetContext = getUserContext(request);
			requestData.put("userId", usetContext.getUserId());
			result.data = saleOrderService.getSaleOrderCount(requestData);
		} catch (Exception e) {
			result.type = Result.Type.warn;
		}

		return result;
	}

	/**
	 * 
	 * 分页获取销售订单列表
	 * 
	 * @author 李超杰
	 * @date 2015年10月15日 上午10:03:56
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页获取销售订单列表")
	@RequestMapping(value = "/list/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<String, Object>> getSaleOrders(HttpServletRequest request, @RequestBody PaginatedFilter paginatedFilter) {
		Result<Map<String, Object>> result = Result.newOne();
		try {

			// PaginatedFilter paginatedFilter =
			// jqGridRequest.toPaginatedFilter();
			MapContext mapContext = paginatedFilter.getFilterItems();

			UserContext usetContext = getUserContext(request);
			mapContext.put("userId", usetContext.getUserId());

			PaginatedList<SaleOrder> paginatedList = saleOrderService.getSaleOrdersByUserId(paginatedFilter);

			Map<String, Object> resultData = new HashMap<String, Object>();

			resultData.put("orderStateTypeCount", saleOrderService.getSaleOrderCountByUserIdAndStateType(mapContext));
			resultData.put("paginatedList", paginatedList);
			result.data = resultData;
		} catch (Exception e) {
			result.type = Result.Type.warn;
		}

		return result;
	}

	// /**
	// * 销售服务列表
	// *
	// * @author 李超杰
	// * @date 2015年10月15日 下午1:42:15
	// *
	// * @param request
	// * @param requestData
	// * @param session
	// * @return
	// */
	// @Remark("销售订单服务列表")
	// @RequestMapping(value = "/svc/list/do", method = RequestMethod.POST)
	// @ResponseBody
	// public Result<List<SaleOrderSvc>> getSaleOrderSvcs(HttpServletRequest
	// request, @RequestBody MapContext requestData, HttpSession session) {
	// Result<List<SaleOrderSvc>> result = Result.newOne();
	// try {
	// Long orderId = requestData.getTypedValue("orderId", Long.class);
	// result.data = saleOrderService.getSaleOrderSvcsByOrderId(orderId);
	// } catch (Exception e) {
	// result.type = Result.Type.warn;
	// }
	// return result;
	// }

	@Remark("跳转到选择店铺")
	@RequestMapping(value = "/ucenter/shop/select/jsp", method = RequestMethod.GET)
	public String toSelectShop() {
		return "ucenter/order/shopSelect";
	}

	@Remark("跳转订单详情")
	@RequestMapping(value = "/detail/jsp", method = RequestMethod.GET)
	public String toOrderDetail() {
		return "ucenter/order/saleOrderDetail";
	}

	@Remark("跳转取消订单详情")
	@RequestMapping(value = "/cancelled/detail/jsp", method = RequestMethod.GET)
	public String toOrderCancelledDetail() {
		return "ucenter/order/saleOrderDetailCancelled";
	}

	@Remark("跳转取消订单详情")
	@RequestMapping(value = "/refunded/detail/jsp", method = RequestMethod.GET)
	public String toOrderRefundedDetail() {
		return "ucenter/order/saleOrderDetailRefund";
	}

	/**
	 * 根据订单id销售订单服务详情
	 * 
	 * @author 李超杰
	 * @date 2015年10月15日 下午1:42:15
	 * 
	 * @param request
	 * @param requestData
	 * @return
	 */
	@Remark("根据订单id销售订单服务详情")
	@RequestMapping(value = "/detail/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SaleOrder> getSaleOrderDetailByOrderId(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<SaleOrder> result = Result.newOne();
		try {
			String orderNo = requestData.getTypedValue("orderNo", String.class);
			if (StrUtil.hasText(orderNo)) {
				result.data = saleOrderService.getSaleOrderByNo(orderNo);
			} else {
				Long orderId = requestData.getTypedValue("orderId", Long.class);
				// requestData.get("orderId");
				result.data = saleOrderService.getSaleOrderDetailByOrderId(orderId);
			}
		} catch (Exception e) {
			result.type = Result.Type.warn;
		}
		return result;
	}

	@Remark("删除订单（假删）")
	@RequestMapping(value = "/mark/deleted/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Long> deleteSaleOrderByorderId(HttpServletRequest request, @RequestBody SaleOrder saleOrder) {
		Result<Long> result = Result.newOne();
		UserContext usetContext = getUserContext(request);
		try {
			boolean status = false;
			if (saleOrder != null) {
				saleOrder.setRoleName(UserType.member.name());
				saleOrder.setUserId(usetContext.getUserId());
				status = saleOrderService.updateSaleOrderForDelete(saleOrder, usetContext);
			}
			if (status) {
				result.data = saleOrder.getId();
			} else {
				result.type = Type.warn;
				result.message = "取消失败!";
			}
		} catch (Exception e) {
			result.type = Result.Type.warn;
		}
		return result;
	}

	@Remark("取消订单")
	@RequestMapping(value = "/cancel/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Long> updateSaleOrderByIdAndCancelled(HttpServletRequest request, @RequestBody SaleOrder saleOrder) {
		Result<Long> result = Result.newOne();
		UserContext usetContext = getUserContext(request);
		try {
			boolean status = false;
			if (saleOrder != null) {
				saleOrder.setRoleName(UserType.member.name());
				saleOrder.setUserId(usetContext.getUserId());
				status = saleOrderService.updateSaleOrderForCancel(saleOrder, usetContext);
			}
			if (status) {
				result.data = saleOrder.getId();
			} else {
				result.type = Type.warn;
				result.message = "取消失败!";
			}
		} catch (Exception e) {
			result.type = Result.Type.warn;
		}
		return result;
	}

	@Remark("享用完成订单")
	@RequestMapping(value = "/finished/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Long> updateSaleOrderFinished(HttpServletRequest request, @RequestBody SaleOrder saleOrder) {
		Result<Long> result = Result.newOne();
		UserContext usetContext = getUserContext(request);
		try {
			boolean status = false;
			if (saleOrder != null) {
				saleOrder.setRoleName(UserType.member.name());
				saleOrder.setUserId(usetContext.getUserId());
				status = saleOrderService.updateSaleOrderForFinish(saleOrder, usetContext);
			}
			if (status) {
				result.data = saleOrder.getId();
			} else {
				result.type = Type.warn;
				result.message = "操作失败!";
			}
		} catch (Exception e) {
			result.type = Result.Type.warn;
		}
		return result;
	}

	@Remark("更改服务店铺")
	@RequestMapping(value = "/change/shop/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Long> updateSaleOrderForShopChange(@RequestBody MapContext mapContext) {
		Result<Long> result = Result.newOne();
		Integer distShopId = mapContext.getTypedValue("distShopId", Integer.class);
		Long saleOrderId = mapContext.getTypedValue("saleOrderId", Long.class);
		boolean status = false;
		SaleOrder saleOrder = null;
		if (distShopId != null && saleOrderId != null) {
			saleOrder = new SaleOrder();
			saleOrder.setId(saleOrderId);
			// 更新店铺信息
			DistShop distShop = distShopService.getById(distShopId);
			saleOrder.setDistShopName(distShop.getName());
			saleOrder.setDistributorId(distShop.getId());
			saleOrder.setDistributorName(distShop.getRealName());
			saleOrder.setRegionId(distShop.getRegionId());
			saleOrder.setRegionName(distShop.getRegionName());
			saleOrder.setStreet(distShop.getStreet());
			saleOrder.setLat(distShop.getLat());
			saleOrder.setLng(distShop.getLng());
			saleOrder.setDistFlag(true);

			// 更新分销利润信息
			List<SaleOrderSvc> saleOrderSvcs = saleOrderService.getSaleOrderSvcsByOrderId(saleOrder.getId());
			BigDecimal distProfit = new BigDecimal(0);
			for (SaleOrderSvc saleOrderSvc : saleOrderSvcs) {
				DistShopSvc distShopSvc = distShopService.getDistShopSvcByDistShopIdAndSvcId(distShopId, saleOrderSvc.getSvcId());
				if (distShopSvc != null) {
					saleOrderSvc.setDistProfit(distShopSvc.getDistProfit());
					if (distShopSvc.getDistProfit() != null) {
						distProfit = distProfit.add(distShopSvc.getDistProfit());
					}
				}
			}
			distProfit = distProfit.setScale(2, BigDecimal.ROUND_UNNECESSARY);
			saleOrder.setDistProfit(distProfit);
			saleOrder.setSaleOrderSvcs(saleOrderSvcs);
			status = saleOrderService.updateSaleOrderForShopChange(saleOrder);
		}
		if (status) {
			result.data = saleOrder.getId();
		} else {
			result.type = Type.warn;
			result.message = "操作失败!";
		}
		return result;
	}

	// -----------------------------提交订单----------------------------
	/**
	 * 提交订单页面
	 * 
	 * @author 邓华锋
	 * @date 2015年10月30日 上午10:34:21
	 * 
	 * @return
	 */
	@Remark("提交订单页面")
	@RequestMapping(value = "/submit/jsp", method = RequestMethod.GET)
	public String toOrderSubmit() {
		return "saleOrder/submit";
	}

	/**
	 * 选择门店
	 * 
	 * @author 邓华锋
	 * @date 2015年10月30日 下午3:08:09
	 * 
	 * @return
	 */
	@Remark("选择门店")
	@RequestMapping(value = "/shop/select/jsp", method = RequestMethod.GET)
	public String toShopSelect() {
		return "saleOrder/shopSelect";
	}

	/**
	 * 提交订单
	 * 
	 * @author 邓华锋
	 * @date 2015年11月2日 下午3:01:23
	 * 
	 * @param request
	 *            订单
	 * @return
	 */
	@Remark("提交订单")
	@RequestMapping(value = "/submit/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<String, Object>> submitSaleOrder(HttpServletRequest request, @RequestBody SaleOrderInfo saleOrderInfo) {
		Result<Map<String, Object>> result = Result.newOne();
		return submitOrder(result, request, saleOrderInfo);
	}

	/**
	 * 通用的订单提交
	 * 
	 * @author 邓华锋
	 * @date 2015年11日 上午9:20:41
	 * 
	 * @param result
	 * @param request
	 * @param saleOrderInfo
	 * @return
	 */
	private Result<Map<String, Object>> submitOrder(Result<Map<String, Object>> result, HttpServletRequest request, SaleOrderInfo saleOrderInfo) {
		UserContext userContext = getUserContext(request);
		if (userContext.getUserId() != null) {
			SaleOrderPo saleOrderPo = saleOrderInfo.getSaleOrderPo();
			if (saleOrderPo != null && saleOrderPo.getUserEcardId() != null && Integer.valueOf(saleOrderPo.getUserEcardId()) > 0) {
				boolean isExist = eCardService.existECardByUserIdAndCardId(userContext.getUserId(), saleOrderPo.getUserEcardId(), false);
				if (!isExist) {
					result.type = Type.warn;
					result.message = "E卡(" + saleOrderPo.getUserEcardId() + ")过期或不存在";
					return result;
				}
				isExist = userService.checkUserPayPasswordSet(userContext.getUserId());
				if (!isExist) {
					result.type = Type.warn;
					result.message = "您还没有设置支付密码，请设置后，再提交订单";
					return result;
				}
				if (saleOrderPo.getPayPassword() == null || !StrUtil.hasText(saleOrderPo.getPayPassword())) {
					result.type = Type.warn;
					result.message = "请输入支付密码";
					return result;
				} else {
					saleOrderPo.setPayPassword(RSACrypter.decryptStringFromJs(saleOrderPo.getPayPassword()));// 解密
					if (!userService.verifyUserPayPassword(userContext.getUserId(), saleOrderPo.getPayPassword())) {
						result.type = Type.warn;
						result.message = "支付密码不正确，请重新输入";
						return result;
					}
				}
			}

			boolean ok = saleOrderService.saveSaleOrder(userContext.getUserId(), saleOrderInfo);
			if (ok) {
				boolean isSvcPack = saleOrderInfo.getSaleOrderPo().getPackId() != null;
				// 如果是普通订单支付完成（注：服务套餐不需要），则发送确认码短信给联系人
				if (PayStateType.paid.name().equals(saleOrderInfo.getPayState())) {
					if (isSvcPack) {
						MapContext dataModel = MapContext.newOne();
						dataModel.put(BaseConst.TplModelVars.ORDER_NO, saleOrderInfo.getSaleOrderNo());
						dataModel.put(BaseConst.TplModelVars.COMPANY, "亿投车吧");
						this.sendSmsText(saleOrderInfo.getSaleOrderPo().getLinkNo(), BaseConst.SmsCodes.ORDER_PAY_RESULT, dataModel);
					} else {
						MapContext dataModel = MapContext.newOne();
						dataModel.put(BaseConst.TplModelVars.CODE, saleOrderInfo.getDoneCode());
						dataModel.put(BaseConst.TplModelVars.SALE_ORDER_NO, saleOrderInfo.getSaleOrderNo());
						this.sendSmsText(saleOrderInfo.getSaleOrderPo().getLinkNo(), BaseConst.SmsCodes.SALE_ORDER, dataModel);
					}
				}

				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("id", saleOrderInfo.getSaleOrderId());
				resultMap.put("no", saleOrderInfo.getSaleOrderNo());
				resultMap.put("isSvcPack", isSvcPack);
				resultMap.put("isPayFinished", PayStateType.paid.name().equals(saleOrderInfo.getPayState()));
				result.data = resultMap;
				result.message = "提交成功";
				if (!isSvcPack) {
					CartHelper.syncSvcCartClearInfo(request.getSession());
					XOrderMessageProxy.getInstance().sendActionMessage(OrderType.saleOrder, saleOrderInfo.getSaleOrderId().longValue(), saleOrderInfo.getSaleOrderNo(), userContext.getUserId(), saleOrderInfo.getSaleOrderPo().getShopId(),
							OrderAction.submit, true);
				}
			} else {
				result.type = Type.warn;
				result.message = "提交失败";
			}
		} else {
			result.type = Type.warn;
			result.message = "登录超时，请重新登录";
		}
		return result;
	}

	/**
	 * 订单提交结果页面
	 * 
	 * @author 邓华锋
	 * @date 2015年11月14日 上午11:20:58
	 * 
	 * @return
	 */
	@Remark("提交订单结果页面")
	@RequestMapping(value = "/submit/result/jsp", method = RequestMethod.GET)
	public String toOrderSubmitResult() {
		return "saleOrder/submitResult";
	}

	@Remark("订单支付完成页面")
	@RequestMapping(value = "/pay/result/jsp", method = RequestMethod.GET)
	public String toOrderPayResult() {
		return "saleOrder/payResult";
	}

	/**
	 * 根据销售订单no,查询订单
	 * 
	 * @author "WJJ"
	 * @date 2015年11月18日 下午2:33:09
	 * 
	 * @param request
	 * @param saleOrder
	 * @return
	 */
	@Remark("根据订单no,查询订单")
	@RequestMapping(value = "/get/by/no", method = RequestMethod.POST)
	@ResponseBody
	public Result<SaleOrder> getSaleOrderOrderNO(HttpServletRequest request, @RequestBody SaleOrder saleOrder) {
		Result<SaleOrder> result = Result.newOne();
		try {
			SaleOrder sOrder = saleOrderService.getSaleOrderByNo(saleOrder.getNo());
			String phone = PhoneNumberUtil.asMaskedPhone(sOrder.getPhoneNo());
			sOrder.setPhoneNo(phone);
			//
			String code = sOrder.getDoneCode();
			String doneCode = code.substring(0, 2) + "***" + code.substring(4);
			sOrder.setDoneCode(doneCode);

			result.data = sOrder;

		} catch (Exception e) {
			result.type = Result.Type.warn;
		}
		return result;
	}

	// -----------------------------服务套餐----------------------------
	/**
	 * 提交服务套餐订单页面
	 * 
	 * @author 邓华锋
	 * @date 2016年1月22日 上午11:57:04
	 * 
	 * @return
	 */
	@Remark("提交服务套餐订单页面")
	@RequestMapping(value = "/svc/pack/submit/jsp", method = RequestMethod.GET)
	public String toPackOrderSubmit() {
		return "saleOrder/packSubmit";
	}

	/**
	 * 提交服务套餐订单
	 * 
	 * @author 邓华锋
	 * @date 2016年1月22日 上午11:57:04
	 * 
	 * @param request
	 * @param saleOrderInfo
	 * @return
	 */
	@Remark("提交服务套餐订单")
	@RequestMapping(value = "/svc/pack/submit/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<String, Object>> submitPackOrder(HttpServletRequest request, @RequestBody SaleOrderInfo saleOrderInfo) {
		Result<Map<String, Object>> result = Result.newOne();
		Integer packId = saleOrderInfo.getSaleOrderPo().getPackId();
		if (packId == null) {
			result.type = Type.warn;
			result.message = "非法操作，提交订单失败";
		}
		return submitOrder(result, request, saleOrderInfo);
	}

	/**
	 * 获取服务套餐票
	 * 
	 * @author 邓华锋
	 * @date 2016年1月27日 上午12:47:02
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	@Remark("分页获取服务套餐列表")
	@RequestMapping(value = "/svc/pack/ticket/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<PaginatedList<UserSvcPackTicket>> getUserSvcPackTicketList(HttpServletRequest request, @RequestBody PaginatedFilter paginatedFilter) {
		Result<PaginatedList<UserSvcPackTicket>> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		if (userContext.getUserId() != null) {
			MapContext mapContext = paginatedFilter.getFilterItems();
			mapContext.put("userId", userContext.getUserId());
		}
		PaginatedList<UserSvcPackTicket> paginatedList = saleOrderService.getUserSvcPackTicketsByFilter(paginatedFilter);
		result.data = paginatedList;
		return result;
	}

	/**
	 * 服务套餐票确认
	 * 
	 * @author 邓华锋
	 * @date 2016年1月27日 上午10:21:40
	 * 
	 * @param request
	 * @param filter
	 * @return
	 */
	@Remark("服务套餐票确认")
	@RequestMapping(value = "/userSvcPackTicket/confirm/finish/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> updateUserSvcPackTicket(HttpServletRequest request, @RequestBody MapContext filter) {
		Result<Boolean> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		Integer tickId = filter.getTypedValue("ticketId", Integer.class);
		result.type = Result.Type.warn;
		result.message = "服务套餐票确认失败！";
		Integer userId = userContext.getUserId();
		if (userId != null && tickId != null) {
			filter.put("userId", userId);
			UserSvcPackTicket userSvcPackTicket = saleOrderService.getUserSvcPackTicketByFilter(filter);
			if (userSvcPackTicket != null) {
				userSvcPackTicket.setFinished(true);
				userSvcPackTicket.setFinishTime(new Date());
				userSvcPackTicket.setInvalid(true);
				userSvcPackTicket.setActorId(userId);
				userSvcPackTicket.setActorName(userContext.getUserName());
				userSvcPackTicket.setActRole(UserType.member.name());
				result.type = Result.Type.info;
				result.data = saleOrderService.updateUserSvcPackTicket(userSvcPackTicket);
				if (result.data) {
					SaleOrder saleOrder = saleOrderService.getSaleOrderByNo(userSvcPackTicket.getOrderNo());
					// 插入车辆服务记录
					if (saleOrder.getCarId() != null) {
						UserCar userCar = carService.getUserCarById(saleOrder.getCarId());
						if (userCar != null && saleOrder.getSvcPackId() != null) {
							UserCarSvcRec userCarSvcRec = carService.getUserCarSvcRecByUserIdAndCarId(userId, userCar.getId());
							if (userCarSvcRec != null) {
								String svcIds = userCarSvcRec.getSvcIds();
								svcIds += "," + userSvcPackTicket.getSvcId();
								userCarSvcRec.setSvcIds(svcIds);
								String svcNames = userCarSvcRec.getSvcNames();
								svcNames += "," + userSvcPackTicket.getSvcName();
								userCarSvcRec.setSvcNames(svcNames);
								carService.updateUserCarSvcRec(userCarSvcRec);
							} else {
								Date now = new Date();
								userCarSvcRec = new UserCarSvcRec();
								userCarSvcRec.setUserId(userId);
								userCarSvcRec.setCarId(userCar.getId());
								userCarSvcRec.setCarName(userCar.getName());
								userCarSvcRec.setBrandId(userCar.getBrandId());
								userCarSvcRec.setSerialId(userCar.getSerialId());
								userCarSvcRec.setModelId(userCar.getModelId());
								userCarSvcRec.setDateVal(now);
								userCarSvcRec.setDateStr(DateUtil.toDateDirStr(new Date()));
								userCarSvcRec.setOrderId(saleOrder.getId());
								userCarSvcRec.setOrderNo(saleOrder.getNo());
								userCarSvcRec.setShopId(saleOrder.getShopId());
								userCarSvcRec.setShopName(saleOrder.getShopName());
								userCarSvcRec.setDistFlag(false);
								userCarSvcRec.setDistShopName(null);
								userCarSvcRec.setSvcIds(userSvcPackTicket.getSvcId().toString());
								userCarSvcRec.setSvcNames(userSvcPackTicket.getSvcName());
								userCarSvcRec.setTs(new Date());
								if (saleOrder.getDistFlag()) {
									userCarSvcRec.setDistFlag(saleOrder.getDistFlag());
									userCarSvcRec.setDistShopName(saleOrder.getDistShopName());
								} else {
									userCarSvcRec.setDistFlag(false);
									userCarSvcRec.setDistShopName(null);
								}
								carService.saveUserCarSvcRec(userCarSvcRec);
							}

							Integer svcTimes = saleOrder.getSvcTimes();
							// 更新服务次数
							saleOrder = new SaleOrder();
							saleOrder.setId(saleOrder.getId());
							svcTimes += 1;
							saleOrder.setSvcTimes(svcTimes);
							saleOrderService.updateSaleOrder(saleOrder);
						}

					}
					result.message = "服务套餐票确认完成";
				} else {
					result.message = "服务套餐票确认失败";
				}
			} else {
				result.type = Result.Type.error;
				result.message = "服务套餐票不存在！";
			}
		}
		return result;
	}

	/**
	 * 根据套餐票id获取服务套餐票
	 * 
	 * @author 邓华锋
	 * @date 2016年1月27日 上午10:21:40
	 * 
	 * @param request
	 * @return
	 */
	@Remark("根据套餐票id获取服务套餐票")
	@RequestMapping(value = "/svc/pack/ticket/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<UserSvcPackTicket> getUserSvcPackTicketList(HttpServletRequest request, @RequestBody MapContext mapContext) {
		Result<UserSvcPackTicket> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		if (userContext.getUserId() != null) {
			mapContext.put("userId", userContext.getUserId());
			mapContext.put("invalid", "true");
			UserSvcPackTicket ticket = saleOrderService.getUserSvcPackTicketByFilter(mapContext);
			result.data = ticket;
		} else {
			result.type = Type.warn;
			result.message = "参数异常";
		}
		return result;
	}

	/**
	 * 获取用户未使用的套餐票数
	 * 
	 * @author 邓华锋
	 * @date 2016年2月17日 上午11:22:35
	 * 
	 * @param request
	 * @return
	 */
	@Remark("获取用户未使用的套餐票数")
	@RequestMapping(value = "/userSvcPackTicket/unUsed/count/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> getUnUsedSvcPackTicketCount(HttpServletRequest request) {
		Result<Integer> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		Integer userId = userContext.getUserId();
		result.data = saleOrderService.getUnUsedSvcPackTicketCount(userId);
		return result;
	}
	// -------------------------------------------------------------
}
