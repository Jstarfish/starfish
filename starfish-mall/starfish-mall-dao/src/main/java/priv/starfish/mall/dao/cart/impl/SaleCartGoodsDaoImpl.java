package priv.starfish.mall.dao.cart.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.cart.SaleCartGoodsDao;
import priv.starfish.mall.cart.entity.SaleCartGoods;

@Component("saleCartGoodsDao")
public class SaleCartGoodsDaoImpl extends BaseDaoImpl<SaleCartGoods, Integer> implements SaleCartGoodsDao {

	@Override
	public SaleCartGoods selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(SaleCartGoods saleCart) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, saleCart);
	}

	@Override
	public int update(SaleCartGoods saleCart) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, saleCart);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<SaleCartGoods> selectByCartSvcId(Long cartSvcId) {
		String sqlId = this.getNamedSqlId("selectByCartSvcId");
		//
		return this.getSqlSession().selectList(sqlId, cartSvcId);
	}

	@Override
	public int deleteByCartSvcId(Integer cartSvcid) {
		String sqlId = this.getNamedSqlId("deleteByCartSvcId");
		//
		return this.getSqlSession().delete(sqlId, cartSvcid);
	}

	@Override
	public int selectCountByProductId(Long productId) {
		String sqlId = this.getNamedSqlId("selectCountByProductId");
		//
		return this.getSqlSession().selectOne(sqlId, productId);
	}

}
