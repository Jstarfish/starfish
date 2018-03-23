package priv.starfish.mall.dao.goods.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.goods.GoodsIntroImgDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.goods.entity.GoodsIntroImg;

@Component("goodsIntroImgDao")
public class GoodsIntroImgDaoImpl extends BaseDaoImpl<GoodsIntroImg, Long> implements GoodsIntroImgDao {

	@Override
	public GoodsIntroImg selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(GoodsIntroImg goodsIntroImg) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, goodsIntroImg);
	}

	@Override
	public int update(GoodsIntroImg goodsIntroImg) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, goodsIntroImg);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<GoodsIntroImg> selectByGoodsId(Integer goodsId) {
		String sqlId = this.getNamedSqlId("selectByGoodsId");
		
		return this.getSqlSession().selectList(sqlId, goodsId);
	}

	@Override
	public int deleteByUuid(String uuid) {
		String sqlId = this.getNamedSqlId("deleteByUuid");
		//
		return this.getSqlSession().delete(sqlId, uuid);
	}

	@Override
	public int deleteByGoodsId(Integer goodsId) {
		String sqlId = this.getNamedSqlId("deleteByGoodsId");
		//
		return this.getSqlSession().delete(sqlId, goodsId);
	}
}
