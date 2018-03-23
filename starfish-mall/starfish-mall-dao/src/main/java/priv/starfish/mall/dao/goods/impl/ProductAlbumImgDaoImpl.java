package priv.starfish.mall.dao.goods.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.goods.ProductAlbumImgDao;
import priv.starfish.mall.goods.entity.ProductAlbumImg;

@Component("productAlbumImgDao")
public class ProductAlbumImgDaoImpl extends BaseDaoImpl<ProductAlbumImg, Long> implements ProductAlbumImgDao {
	@Override
	public ProductAlbumImg selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public ProductAlbumImg selectByProductIdAndImageId(Long productId, Integer imageId) {
		String sqlId = this.getNamedSqlId("selectByProductIdAndImageId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("productId", productId);
		params.put("imageId", imageId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(ProductAlbumImg productAlbumImg) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, productAlbumImg);
	}

	@Override
	public int update(ProductAlbumImg productAlbumImg) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, productAlbumImg);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<ProductAlbumImg> selectByProductId(Long productId) {
		String sqlId = this.getNamedSqlId("selectByProductId");
		//
		return this.getSqlSession().selectList(sqlId, productId);
	}

	@Override
	public List<ProductAlbumImg> selectByImageId(Long imageId) {
		String sqlId = this.getNamedSqlId("selectByImageId");
		//
		return this.getSqlSession().selectList(sqlId, imageId);
	}

	@Override
	public List<Long> selectIdsByImageId(Long id) {
		String sqlId = this.getNamedSqlId("selectIdsByImageId");
		//
		return this.getSqlSession().selectList(sqlId, id);
	}

	@Override
	public int deleteByProductId(Long productId) {
		String sqlId = this.getNamedSqlId("deleteByProductId");
		//
		return this.getSqlSession().delete(sqlId, productId);
	}

	@Override
	public List<Long> selectImageIdsByProductId(Long productId) {
		String sqlId = this.getNamedSqlId("selectImageIdsByProductId");
		//
		return this.getSqlSession().selectList(sqlId, productId);
	}


}
