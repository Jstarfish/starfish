package priv.starfish.mall.dao.goods.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.goods.GoodsAlbumImgDao;
import priv.starfish.mall.goods.entity.GoodsAlbumImg;

@Component("goodsAlbumImgDao")
public class GoodsAlbumImgDaoImpl extends BaseDaoImpl<GoodsAlbumImg, Long> implements GoodsAlbumImgDao {

	@Override
	public GoodsAlbumImg selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(GoodsAlbumImg goodsAlbumImg) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, goodsAlbumImg);
	}

	@Override
	public int update(GoodsAlbumImg goodsAlbumImg) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, goodsAlbumImg);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<GoodsAlbumImg> selectByGoodsId(Integer goodsId) {
		String sqlId = this.getNamedSqlId("selectByGoodsId");
		
		return this.getSqlSession().selectList(sqlId, goodsId);
		
	}

	@Override
	public int deleteByImageUuid(String uuid) {
		String sqlId = this.getNamedSqlId("deleteByImageUuid");
		//
		return this.getSqlSession().delete(sqlId, uuid);
	}

	@Override
	public GoodsAlbumImg selectByImageUuid(String uuid) {
		String sqlId = this.getNamedSqlId("selectByImageUuid");
		//
		return this.getSqlSession().selectOne(sqlId, uuid);
	}

	@Override
	public int updateImageGroupById(String specVal, Long imgId) {
		String sqlId = this.getNamedSqlId("updateImageGroupById");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("imageGroup", specVal);
		params.put("id", imgId);
		//
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public int updateImageGroupAsNullByGoodsId(Integer goodsId) {
		String sqlId = this.getNamedSqlId("updateImageGroupAsNullByGoodsId");
		//
		return this.getSqlSession().update(sqlId, goodsId);
	}

	@Override
	public int deleteByGoodsId(Integer goodsId) {
		String sqlId = this.getNamedSqlId("deleteByGoodsId");
		//
		return this.getSqlSession().delete(sqlId, goodsId);
	}

	@Override
	public int updateImageGroupAsNullById(Long imgId) {
		String sqlId = this.getNamedSqlId("updateImageGroupAsNullById");
		//
		return this.getSqlSession().update(sqlId, imgId);
	}
}
