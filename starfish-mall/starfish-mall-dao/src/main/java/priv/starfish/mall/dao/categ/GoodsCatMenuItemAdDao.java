package priv.starfish.mall.dao.categ;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.categ.entity.GoodsCatMenuItemAd;

@IBatisSqlTarget
public interface GoodsCatMenuItemAdDao extends BaseDao<GoodsCatMenuItemAd, Integer> {

	GoodsCatMenuItemAd selectById(Integer id);

	List<GoodsCatMenuItemAd> selectByMenuItemId(Integer menuItemId);

	GoodsCatMenuItemAd selectByMenuItemIdAndImageUuid(Integer menuItemId, String imageUuid);

	int insert(GoodsCatMenuItemAd goodsCatMenuItemAd);

	int update(GoodsCatMenuItemAd goodsCatMenuItemAd);

	int deleteById(Integer id);

	int deleteByMenuItemId(Integer menuItemId);

}
