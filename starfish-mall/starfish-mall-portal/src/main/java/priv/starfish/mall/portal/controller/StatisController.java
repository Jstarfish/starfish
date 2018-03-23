package priv.starfish.mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.Result;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.service.StatisService;
import priv.starfish.mall.statis.entity.GoodsBrowseSum;
import priv.starfish.mall.statis.entity.ShopBrowseSum;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Remark("统计功能")
@Controller
@RequestMapping(value = "/statis")
public class StatisController extends BaseController {
	@Resource
	private StatisService statisService;

	// ---------------------------------- 商品浏览数量 ------------------------------------
	@Remark("增加商品浏览数量")
	@RequestMapping(value = "/goods/browse/count/add", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> addGoodsBrowseCount(HttpServletRequest request, @RequestBody GoodsBrowseSum requestData) {
		Result<Boolean> result = Result.newOne();
		//
		UserContext userContext = getUserContext(request);

		Long productId = requestData.getProductId();
		Integer userId = requestData.getUserId();
		if (userId == null) {
			if (userContext.isSysUser()) {
				userId = userContext.getUserId();
			} else {
				userId = User.UNKNOWN_USER_ID;
			}
		}

		result.data = statisService.addGoodsBrowseCount(productId, userId);

		return result;
	}

	@Remark("获取指定商品的浏览数量")
	@RequestMapping(value = "/goods/browse/count/get/by/id", method = RequestMethod.GET)
	@ResponseBody
	public Result<Long> getGoodsBrowseCountById(@RequestParam("productId") Long productId) {
		Result<Long> result = Result.newOne();

		result.data = statisService.getGoodsBrowseCount(productId);

		return result;
	}

	@SuppressWarnings("unchecked")
	@Remark("获取指定的多个商品(ids)的浏览数量")
	@RequestMapping(value = "/goods/browse/count/get/by/ids", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<Long, Long>> getGoodsBrowseCountByIds(@RequestBody MapContext requestData) {
		Result<Map<Long, Long>> result = Result.newOne();

		// 可能是int型（默认），也可能是long型列表
		List<? extends Number> _productIds = (List<? extends Number>) requestData.get("productIds");

		List<Long> productIds = TypeUtil.toLongList(_productIds);

		result.data = statisService.getGoodsBrowseCounts(productIds);

		return result;
	}

	// ---------------------------------- 店铺浏览数量 ------------------------------------

	@Remark("增加店铺浏览数量")
	@RequestMapping(value = "/shop/browse/count/add", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> addShopBrowseCount(HttpServletRequest request, @RequestBody ShopBrowseSum requestData) {
		Result<Boolean> result = Result.newOne();
		//
		UserContext userContext = getUserContext(request);

		Integer shopId = requestData.getShopId();
		Integer userId = requestData.getUserId();
		if (userId == null) {
			if (userContext.isSysUser()) {
				userId = userContext.getUserId();
			} else {
				userId = User.UNKNOWN_USER_ID;
			}
		}

		result.data = statisService.addShopBrowseCount(shopId, userId);

		return result;
	}

	@Remark("获取指定店铺的浏览数量")
	@RequestMapping(value = "/shop/browse/count/get/by/id", method = RequestMethod.GET)
	@ResponseBody
	public Result<Long> getShopBrowseCountById(@RequestParam("shopId") Integer shopId) {
		Result<Long> result = Result.newOne();

		result.data = statisService.getShopBrowseCount(shopId);

		return result;
	}

	@SuppressWarnings("unchecked")
	@Remark("获取指定的多个店铺(ids)的浏览数量")
	@RequestMapping(value = "/shop/browse/count/get/by/ids", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<Integer, Long>> getShopBrowseCountByIds(@RequestBody MapContext requestData) {
		Result<Map<Integer, Long>> result = Result.newOne();

		List<Integer> shopIds = (List<Integer>) requestData.get("shopIds");
		result.data = statisService.getShopBrowseCounts(shopIds);

		return result;
	}

}
