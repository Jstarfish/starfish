package priv.starfish.mall.dao.categ;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.categ.entity.GoodsCatMenuItemCat;

@IBatisSqlTarget
public interface GoodsCatMenuItemCatDao extends BaseDao<GoodsCatMenuItemCat, Integer> {

	GoodsCatMenuItemCat selectById(Integer id);

	List<GoodsCatMenuItemCat> selectByMenuItemId(Integer menuItemId);

	GoodsCatMenuItemCat selectByMenuItemIdAndName(Integer menuItemId, String name);

	int insert(GoodsCatMenuItemCat goodsCatMenuItemCat);

	int update(GoodsCatMenuItemCat goodsCatMenuItemCat);

	int deleteById(Integer id);

	int deleteByMenuItemId(Integer menuItemId);

	/**
	 * 通过菜单项Id分页查询菜单项分类关联
	 * 
	 * @author 廖晓远
	 * @date 2015年6月18日 上午11:18:26
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<GoodsCatMenuItemCat> selectAllByMenuItemId(PaginatedFilter paginatedFilter);
}
