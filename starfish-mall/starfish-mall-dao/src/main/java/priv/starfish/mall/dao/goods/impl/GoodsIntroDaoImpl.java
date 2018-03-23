package priv.starfish.mall.dao.goods.impl;


import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.goods.GoodsIntroDao;
import priv.starfish.mall.goods.entity.GoodsIntro;


@Component("goodsIntroDao")
public class GoodsIntroDaoImpl extends BaseDaoImpl<GoodsIntro, Integer> implements GoodsIntroDao {
	@Override
	public GoodsIntro selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}
	@Override
	public GoodsIntro selectByGoodsId(Integer goodsId) {
		String sqlId = this.getNamedSqlId("selectByGoodsId");
		//
		return this.getSqlSession().selectOne(sqlId, goodsId);
	}
	@Override
	public int insert(GoodsIntro goodsIntro) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, goodsIntro);
	}
	@Override
	public int update(GoodsIntro goodsIntro) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, goodsIntro);
	}
	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}
	@Override
	public int deleteByGoodsId(Integer goodsId) {
		String sqlId = this.getNamedSqlId("deleteByGoodsId");
		//
		return this.getSqlSession().delete(sqlId, goodsId);
	}
}