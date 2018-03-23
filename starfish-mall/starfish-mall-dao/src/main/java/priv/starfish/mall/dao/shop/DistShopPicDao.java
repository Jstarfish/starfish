package priv.starfish.mall.dao.shop;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.shop.entity.DistShopPic;

@IBatisSqlTarget
public interface DistShopPicDao extends BaseDao<DistShopPic, Long> {
	DistShopPic selectById(Long id);

	int insert(DistShopPic distShopPic);

	int update(DistShopPic distShopPic);

	int deleteById(Long id);

	List<DistShopPic> selectByDistShopId(Integer id);
	
}