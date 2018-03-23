package priv.starfish.mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.Result;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.categ.entity.GoodsCatMenu;
import priv.starfish.mall.categ.entity.GoodsCatMenuItem;
import priv.starfish.mall.categ.entity.GoodsCatMenuItemCat;
import priv.starfish.mall.service.CategService;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;
import java.util.List;

@Remark("分类Controller")
@Controller
@RequestMapping("/categ")
public class CategController extends BaseController {

	@Resource
	CategService categService;

	// --------------------------------------------------商品分类菜单---------------------------------------------------------------

	/**
	 * 初始化商品分类菜单
	 * 
	 * @author 毛智东
	 * @date 2015年7月15日 下午5:36:07
	 * 
	 * @return
	 */
	@Remark("初始化商品分类菜单")
	@RequestMapping(value = "/goods/cat/menu/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<GoodsCatMenuItem>> initGoodsCatMenu() {
		Result<List<GoodsCatMenuItem>> result = Result.newOne();
		String name = "全部商品分类";
		GoodsCatMenu goodsCatMenu = categService.getGoodsCatMenuByName(name);
		if (goodsCatMenu != null) {
			List<GoodsCatMenuItem> firstGradeMenuItems = categService.getMenuItemsByMenuIdAndLevel(goodsCatMenu.getId(), 2);
			for (GoodsCatMenuItem firstGradeMenuItem : firstGradeMenuItems) {
				List<GoodsCatMenuItem> secondGradeMenuItems = categService.getMenuItemsByPId(firstGradeMenuItem.getId());
				for (GoodsCatMenuItem secondGradeMenuItem : secondGradeMenuItems) {
					PaginatedFilter paginatedFilter = PaginatedFilter.newOne();
					MapContext filterItems = MapContext.newOne();
					filterItems.put("menuItemId", secondGradeMenuItem.getId());
					paginatedFilter.setFilterItems(filterItems);
					PaginatedList<GoodsCatMenuItemCat> paginatedList = categService.getGoodsCatMenuItemCats(paginatedFilter);
					if (paginatedList != null) {
						List<GoodsCatMenuItemCat> goodsCatList = paginatedList.getRows();
						secondGradeMenuItem.setGoodsCatList(goodsCatList);
					}
				}
				firstGradeMenuItem.setSonsList(secondGradeMenuItems);
			}
			result.data = firstGradeMenuItems;
		}
		return result;
	}
}
