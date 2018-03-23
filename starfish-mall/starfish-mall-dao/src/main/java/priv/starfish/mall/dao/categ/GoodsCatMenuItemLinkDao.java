package priv.starfish.mall.dao.categ;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.categ.entity.GoodsCatMenuItemLink;

@IBatisSqlTarget
public interface GoodsCatMenuItemLinkDao extends BaseDao<GoodsCatMenuItemLink, Integer> {

	GoodsCatMenuItemLink selectById(Integer id);

	List<GoodsCatMenuItemLink> selectByMenuItemId(Integer menuItemId);

	GoodsCatMenuItemLink selectByMenuItemIdAndName(Integer menuItemId, String name);

	int insert(GoodsCatMenuItemLink goodsCatMenuItemLink);

	int update(GoodsCatMenuItemLink goodsCatMenuItemLink);

	int deleteById(Integer id);

	int deleteByMenuItemId(Integer menuItemId);

}
