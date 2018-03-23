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
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.service.SearchService;
import priv.starfish.mall.shop.entity.DistShop;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;

/**
 * 分销店铺Controller
 * 
 */
@Remark("分销店铺Controller")
@Controller
@RequestMapping("/distshop")
public class DistShopController extends BaseController {

	@Resource
	private SearchService searchService;

	// --------------------------------------- 卫星店铺----------------------------------------------

	/**
	 * 分页查询卫星店铺
	 * 
	 */
	@Remark("分页查询卫星店铺")
	@RequestMapping(value = "/wxshop/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<PaginatedList<DistShop>> listDistShop(@RequestBody PaginatedFilter paginatedFilter) {
		Result<PaginatedList<DistShop>> result = Result.newOne();
		// 增加过滤条件
		MapContext filterItems = paginatedFilter.getFilterItems();
		filterItems.put("disabled", false);
		filterItems.put("auditStatus", 1);
		if (filterItems.get("distance") != null) {
			if (filterItems.get("lat") == null || filterItems.get("lon") == null) {
				result.type = Type.warn;
				result.message = "无法获取您的位置信息！";
				return result;
			}
		}
		PaginatedList<DistShop> paginatedList = searchService.searchDistShopsByFilter(paginatedFilter);
		result.data = paginatedList;
		return result;
	}

}
