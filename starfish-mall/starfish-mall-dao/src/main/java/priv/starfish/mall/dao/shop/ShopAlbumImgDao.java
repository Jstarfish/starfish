package priv.starfish.mall.dao.shop;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.shop.entity.ShopAlbumImg;

@IBatisSqlTarget
public interface ShopAlbumImgDao extends BaseDao<ShopAlbumImg, Integer> {
	ShopAlbumImg selectById(Integer id);

	List<ShopAlbumImg> selectByShopId(Integer shopId);

	ShopAlbumImg selectByImageUuid(String imageUuid);

	int insert(ShopAlbumImg shopAlbumImg);

	int update(ShopAlbumImg shopAlbumImg);

	int deleteById(Integer id);

	PaginatedList<ShopAlbumImg> selectList(PaginatedFilter paginatedFilter);

	int deleteByIds(List<Integer> ids);
}