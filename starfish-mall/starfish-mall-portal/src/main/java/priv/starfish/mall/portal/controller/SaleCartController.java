package priv.starfish.mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.cart.dict.CartAction;
import priv.starfish.mall.cart.dto.SaleCartInfo;
import priv.starfish.mall.cart.entity.SaleCart;
import priv.starfish.mall.cart.entity.SaleCartSvc;
import priv.starfish.mall.cart.po.SaleCartCheckPo;
import priv.starfish.mall.cart.po.SaleCartGoodsPo;
import priv.starfish.mall.cart.po.SaleCartSvcPo;
import priv.starfish.mall.cart.po.SaleCartSvcPoList;
import priv.starfish.mall.order.dto.SaleOrderInfo;
import priv.starfish.mall.order.entity.SaleOrderGoods;
import priv.starfish.mall.order.entity.SaleOrderSvc;
import priv.starfish.mall.service.SaleCartService;
import priv.starfish.mall.service.SaleOrderService;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.web.base.CartHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Remark("销售购物车信息")
@Controller
@RequestMapping(value = "/saleCart")
public class SaleCartController extends BaseController {
	@Resource
	SaleCartService saleCartService;

	@Resource
	SaleOrderService saleOrderService;

	/**
	 * 跳转车辆服务页面
	 * 
	 * @author 李超杰
	 * @date 2015年10月16日 下午4:00:18
	 * 
	 * @param
	 * @return
	 */
	@Remark("跳转服务购物车页面")
	@RequestMapping(value = "/list/jsp", method = RequestMethod.GET)
	public String tosvcCartList() {
		return "saleCart/saleCart";
	}

	/**
	 * 获取车辆服务列表
	 * 
	 * @author 李超杰
	 * @date 2015年10月16日 下午1:42:15
	 * 
	 * @param request
	 * @param requestData
	 * @return
	 */
	@Remark("查询购物车信息")
	@RequestMapping(value = "/list/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<SaleCart> getSvcCart(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<SaleCart> result = Result.newOne();
		try {
			String falgType = requestData.getTypedValue("falgType", String.class);
			if (falgType != null) {
				result.data = CartHelper.fetchSvcCartInfo(request.getSession(), falgType);
			} else {
				result.data = CartHelper.fetchSvcCartInfo(request.getSession(), "check");
			}
		} catch (Exception e) {
			result.type = Type.warn;
		}
		return result;
	}

	@Remark("商品加入(减少、更新、单个删除)购物车")
	@RequestMapping(value = "/goods/sync/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<SaleCart> syncSvcCartGoods(HttpSession session, @RequestBody SaleCartGoodsPo goodsPo) {
		Result<SaleCart> result = Result.newOne();
		try {
			result.data = CartHelper.syncSvcCartGoods(session, goodsPo);
		} catch (Exception e) {
			result.type = Type.warn;
		}
		return result;
	}

	@Remark("服务加入(减少、更新、单个删除)购物车")
	@RequestMapping(value = "/svc/sync/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<SaleCart> syncSvcCartSvc(HttpSession session, @RequestBody SaleCartSvcPo svcPo) {
		Result<SaleCart> result = Result.newOne();
		try {
			result.data = CartHelper.syncSaleCartSvc(session, svcPo);
		} catch (Exception e) {
			result.type = Type.warn;
		}
		return result;
	}

	@Remark("选中服务（商品）删除、更新购物车")
	@RequestMapping(value = "/check/sync/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<SaleCart> syncSaleCartCheck(HttpSession session, @RequestBody SaleCartCheckPo cartPo) {
		Result<SaleCart> result = Result.newOne();
		try {
			CartHelper.syncCheckSaleCart(session, cartPo);
			result.data = CartHelper.fetchSvcCartInfo(session, "check");
		} catch (Exception e) {
			result.type = Type.warn;
		}
		return result;
	}

	@Remark("全部服务加入购物车")
	@RequestMapping(value = "/svc/all/sync/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<SaleCart> syncSvcCartSvcAll(HttpSession session, @RequestBody SaleCartSvcPoList svcPos) {
		Result<SaleCart> result = Result.newOne();
		try {
			if (svcPos != null && svcPos.size() > 0) {
				result.data = CartHelper.syncSvcCartAllInfo(session, svcPos);
			} else {
				result.data = null;
				result.message = "请至少选中一件服务！";
			}

		} catch (Exception e) {
			result.type = Type.warn;
		}
		return result;
	}

	@Remark("清除购物车")
	@RequestMapping(value = "/clear/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<SaleCart> syncSvcCartSvcAll(HttpSession session) {
		Result<SaleCart> result = Result.newOne();
		try {
			CartHelper.syncSvcCartClearInfo(session);
		} catch (Exception e) {
			result.type = Type.warn;
		}
		return result;
	}

	@Remark("获取购物车信息")
	@RequestMapping(value = "/info/fetch/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SaleCart> fetchSvcCartInfo(HttpServletRequest request, HttpSession session) {
		Result<SaleCart> result = Result.newOne();
		result.data = CartHelper.fetchSaleCartInfo(session);
		return result;
	}

	/**
	 * 获取购物车数量
	 * 
	 * @author 李超杰
	 * @date 2015年12月07日 下午1:42:15
	 * 
	 * @param session
	 * @return
	 */
	@Remark("查询购物车数量")
	@RequestMapping(value = "/count/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> getSvcCartCount(HttpSession session) {
		Result<Integer> result = Result.newOne();
		try {
			result.data = CartHelper.fetchSaleCartCount(session);
		} catch (Exception e) {
			result.type = Type.warn;
		}
		return result;
	}

	@Remark("查询服务返回购物车服务信息")
	@RequestMapping(value = "/saleCartSvc/by/svcId/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SaleCartSvc> getSaleCartSvcBySvcId(HttpSession session, @RequestBody MapContext requestData) {
		Result<SaleCartSvc> result = Result.newOne();
		try {
			Integer svcId = requestData.getTypedValue("svcId", Integer.class);
			UserContext userContext = getUserContext(session);
			SaleCartSvc saleCartSvc = CartHelper.getcarSvc(svcId, userContext.getUserId());
			if (saleCartSvc != null) {
				result.data = saleCartSvc;
			}
		} catch (Exception e) {
			result.type = Type.warn;
		}
		return result;
	}

	/**
	 * 计算价格
	 * 
	 * @author 李超杰
	 * @date 2015年10月16日 下午1:42:15
	 * 
	 * @param request
	 * @return
	 */
	@Remark("计算价格")
	@RequestMapping(value = "/amount/calc/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<SaleCart> getSvcCartBygoodsQuantity(HttpServletRequest request, @RequestBody SaleOrderInfo saleOrderInfo) {
		Result<SaleCart> result = Result.newOne();
		try {
			result.data = getSaleCartSvc(saleOrderInfo, request, "checked");
		} catch (Exception e) {
			result.type = Type.warn;
		}
		return result;
	}

	/**
	 * 计算价格
	 * 
	 * @author 李超杰
	 * @date 2015年10月16日 下午1:42:15
	 * 
	 * @param request
	 * @return
	 */
	@Remark("计算价格不带checkBox")
	@RequestMapping(value = "/amount/calc/nocheck/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<SaleCart> getAmountCalcNoCheck(HttpServletRequest request, @RequestBody SaleOrderInfo saleOrderInfo) {
		Result<SaleCart> result = Result.newOne();
		try {
			result.data = getSaleCartSvc(saleOrderInfo, request, "not");
		} catch (Exception e) {
			result.type = Type.warn;
		}
		return result;
	}

	@Remark("继续购买")
	@RequestMapping(value = "/sync/saleOrder/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> addSaleCartById(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<String> result = Result.newOne();
		try {
			String orderId = requestData.getTypedValue("orderId", String.class);
			if (!StrUtil.isNullOrBlank(orderId)) {
				Long id = Long.valueOf(orderId);
				List<SaleOrderSvc> saleOrderSvcList = saleOrderService.getSaleOrderSvcsByOrderId(id);
				if (saleOrderSvcList != null && saleOrderSvcList.size() > 0) {
					for (SaleOrderSvc saleOrderSvc : saleOrderSvcList) {
						SaleCartSvcPo cartSvcPo = SaleCartSvcPo.newOne();
						cartSvcPo.svcId = saleOrderSvc.getSvcId();
						cartSvcPo.checkFlag = true;
						cartSvcPo.action = CartAction.add;
						CartHelper.syncSaleCartSvc(request.getSession(), cartSvcPo);
					}
				}
				List<SaleOrderGoods> saleOrderGoods = saleOrderService.getSaleOrderGoodsByOrderId(id);
				if (saleOrderGoods != null && saleOrderGoods.size() > 0) {
					for (SaleOrderGoods saleOrderGoods2 : saleOrderGoods) {
						SaleCartGoodsPo goodsPo = SaleCartGoodsPo.newOne();
						goodsPo.action = CartAction.add;
						goodsPo.checkFlag = true;
						goodsPo.productId = saleOrderGoods2.getProductId();
						goodsPo.quantity = saleOrderGoods2.getQuantity();
						CartHelper.syncSvcCartGoods(request.getSession(), goodsPo);
					}
				}
			}
			result.data = orderId;
		} catch (Exception e) {
			result.type = Result.Type.warn;
		}
		return result;
	}

	// 计算价格封装购物车返回
	public SaleCart getSaleCartSvc(SaleOrderInfo saleOrderInfo, HttpServletRequest request, String check) {
		SaleCartInfo saleCartInfo = saleOrderInfo.getSaleCartInfo();
		// 前台传參结算价格
		List<SaleCartSvc> saleCartSvcList = saleCartInfo.getSaleCartSvcList();
		SaleCart saleCart = null;
		if (saleCartSvcList != null && saleCartSvcList.size() > 0) {
			saleCart = SaleCart.newOne();
			// 存放购物车价格组，key：服务购物车信息id
			// Map<Integer, VarietyAmountInfo> map = saleOrderInfo.toSaleCartSvcAmount(saleCartSvcList);
			if (check.equals("checked")) {
				// saleCart = saleOrderInfo.toCartAmount(saleCartSvcList, map);
			} else {
				// saleCart = saleOrderInfo.toSaleCartAmount(saleCartSvcList, map);
			}

		}
		return saleCart;
	}
}
