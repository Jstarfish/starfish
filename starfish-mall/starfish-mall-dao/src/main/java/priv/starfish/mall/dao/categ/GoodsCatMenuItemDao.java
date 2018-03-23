package priv.starfish.mall.dao.categ;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.categ.entity.GoodsCatMenuItem;

@IBatisSqlTarget
public interface GoodsCatMenuItemDao extends BaseDao<GoodsCatMenuItem, Integer> {

	GoodsCatMenuItem selectById(Integer id);

	GoodsCatMenuItem selectByMenuIdAndLevelAndName(Integer menuId, Integer level, String name);

	List<GoodsCatMenuItem> selectByMenuIdAndLevel(Integer menuId, Integer level);

	List<GoodsCatMenuItem> selectByPId(Integer parentId);

	int insert(GoodsCatMenuItem goodsCatMenuItem);

	int update(GoodsCatMenuItem goodsCatMenuItem);

	int deleteById(Integer id);


}
