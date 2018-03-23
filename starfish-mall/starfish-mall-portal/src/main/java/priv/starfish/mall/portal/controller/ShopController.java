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
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.service.SearchService;
import priv.starfish.mall.service.ShopService;
import priv.starfish.mall.shop.dto.ShopDto;
import priv.starfish.mall.shop.entity.ShopProduct;
import priv.starfish.mall.shop.entity.ShopSvc;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Remark("店铺")
@Controller
@RequestMapping(value = "/shop")
public class ShopController extends BaseController {
	@Resource
	private ShopService shopService;

	@Resource
	private SearchService searchService;

	// -----------------------------------门店------------------------------------------------------
	/**
	 * 返回门店列表页面
	 * 
	 * @author wangdi
	 * @date 2015年10月19日 下午8:36:41
	 * 
	 * @return
	 */
	@Remark("门店列表页面")
	@RequestMapping(value = "/list/jsp", method = RequestMethod.GET)
	public String toShopListJsp() {
		return "shop/shopList";
	}

	/**
	 * 返回门店详情页面
	 * 
	 * @author wangdi
	 * @date 2015年10月28日 下午4:16:20
	 * 
	 * @return
	 */
	@Remark("门店详情页面")
	@RequestMapping(value = "/detail/jsp", method = RequestMethod.GET)
	public String toShopJsp() {
		return "shop/shopDetail";
	}

	@Remark("根据过滤条件分页获取门店列表")
	@RequestMapping(value = "/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<PaginatedList<ShopDto>> getShops(@RequestBody PaginatedFilter paginatedFilter) {
		Result<PaginatedList<ShopDto>> result = Result.newOne();
		// 增加过滤条件
		MapContext filterItems = paginatedFilter.getFilterItems();
		filterItems.put("disabled", false);
		filterItems.put("closed", false);
		filterItems.put("auditStatus", 2);
		// 查询店铺
		PaginatedList<ShopDto> paginatedList = searchService.searchShopsByFilter(paginatedFilter);
		result.data = paginatedList;
		return result;
	}

	@Remark("获取门店详情")
	@RequestMapping(value = "/detail/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<ShopDto> getShopDetail(Integer shopId) {
		Result<ShopDto> result = Result.newOne();
		try {
			if (shopId == null) {
				logger.error(String.format("Parameter shopId is null. Class:%s Method:getShopDetail", getClass().getName()));
				result.type = Result.Type.error;
				result.message = "未指定门店！";
			}
			ShopDto shopDto = shopService.getShopInfoById(shopId);
			if (shopDto == null) {
				logger.error(String.format("Shop is not exist! shopId:%d", shopId));
				result.type = Result.Type.error;
				result.message = "指定的门店不存在！";
			}
			// 赋值
			result.data = shopDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
			result.message = "获取门店详细信息失败！";
		}

		return result;
	}

	// ----------------------------------店铺产品服务---------------------------------------------------------
	/**
	 * 获取指定店铺指定货品的缺货状态
	 * 
	 * @author 邓华锋
	 * @date 2015年11月15日 下午3:08:26
	 * 
	 * @param requestData
	 * @return
	 */
	@Remark("获取指定店铺指定货品的缺货状态")
	@RequestMapping(value = "/product/lack/get", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Result<Map<Long, ShopProduct>> getShopProductLack(@RequestBody MapContext requestData) {
		// by/shopId/and/productIds
		Result<Map<Long, ShopProduct>> result = Result.newOne();
		Integer shopId = requestData.getTypedValue("shopId", Integer.class);
		List<Long> productIds = requestData.getTypedValue("productIds", ArrayList.class);
		result.data = shopService.getProductLackByShopIdAndProductIds(shopId, productIds);
		return result;
	}

	/**
	 * 获取指定店铺服务状态
	 * 
	 * @author 邓华锋
	 * @date 2016年1月18日 下午3:07:56
	 * 
	 * @param requestData
	 * @return
	 */
	@Remark("获取指定店铺服务状态")
	@RequestMapping(value = "/svc/status/get", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Result<Map<Long, ShopSvc>> getShopSvcStatus(@RequestBody MapContext requestData) {
		Result<Map<Long, ShopSvc>> result = Result.newOne();
		Integer shopId = requestData.getTypedValue("shopId", Integer.class);
		List<Long> svcIds = requestData.getTypedValue("svcIds", ArrayList.class);
		result.data = shopService.getSvcStatusByShopIdAndSvcIds(shopId, svcIds);
		return result;
	}
}
