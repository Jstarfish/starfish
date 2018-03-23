package priv.starfish.mall.dao.cart.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.cart.SaleCartDao;
import priv.starfish.mall.cart.entity.SaleCart;

@Component("saleCartDao")
public class SaleCartDaoImpl extends BaseDaoImpl<SaleCart, Integer> implements SaleCartDao {

	@Override
	public SaleCart selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(SaleCart saleCart) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, saleCart);
	}

	@Override
	public int update(SaleCart saleCart) {
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
	public List<SaleCart> selectByUserId(Integer userId) {
		String sqlId=this.getNamedSqlId("selectByUserId");
		//
		return this.getSqlSession().selectList(sqlId,userId);
	}

}
