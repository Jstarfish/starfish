package priv.starfish.mall.dao.shop.impl;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.shop.ShopAlbumImgDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.shop.entity.ShopAlbumImg;

@Component("shopAlbumImgDao")
public class ShopAlbumImgDaoImpl extends BaseDaoImpl<ShopAlbumImg, Integer> implements ShopAlbumImgDao {

	@Override
	public ShopAlbumImg selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public List<ShopAlbumImg> selectByShopId(Integer shopId) {
		String sqlId = this.getNamedSqlId("selectByShopId");
		//
		return this.getSqlSession().selectList(sqlId, shopId);
	}

	@Override
	public ShopAlbumImg selectByImageUuid(String imageUuid) {
		String sqlId = this.getNamedSqlId("selectByImageUuid");
		//
		return this.getSqlSession().selectOne(sqlId, imageUuid);
	}

	@Override
	public int insert(ShopAlbumImg shopAlbumImg) {
		String sqlId = this.getNamedSqlId("insert");
		int maxSeqNo = this.getEntityMaxSeqNo(ShopAlbumImg.class);
		shopAlbumImg.setSeqNo(maxSeqNo + 1);
		//
		return this.getSqlSession().insert(sqlId, shopAlbumImg);
	}

	@Override
	public int update(ShopAlbumImg shopAlbumImg) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, shopAlbumImg);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<ShopAlbumImg> selectList(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectList");
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<ShopAlbumImg> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}
	
	@Override
	public int deleteByIds(List<Integer> ids) {
		if (ids != null && ids.size() > 0) {
			String sqlId = this.getNamedSqlId("deleteByIds");
			return this.getSqlSession().delete(sqlId, ids);
		} else {
			return 0;
		}
	}

}