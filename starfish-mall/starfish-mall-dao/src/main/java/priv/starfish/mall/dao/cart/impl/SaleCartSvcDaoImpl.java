package priv.starfish.mall.dao.cart.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.cart.SaleCartSvcDao;
import priv.starfish.mall.cart.entity.SaleCartSvc;

@Component("saleCartSvcDao")
public class SaleCartSvcDaoImpl extends BaseDaoImpl<SaleCartSvc, Integer> implements SaleCartSvcDao {

	@Override
	public SaleCartSvc selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(SaleCartSvc saleCartSvc) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, saleCartSvc);
	}

	@Override
	public int update(SaleCartSvc saleCartSvc) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, saleCartSvc);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<SaleCartSvc> selectByCartId(Integer cartId) {
		String sqlId=this.getNamedSqlId("selectByCartId");
		//
		return this.getSqlSession().selectList(sqlId,cartId);
	}

	@Override
	public SaleCartSvc selectByCartIdAndSvcId(Integer cartId, Integer svcId) {
		String sqlId=this.getNamedSqlId("selectByUserIdAndSvcId");
		MapContext map =MapContext.newOne();
		map.put("cartId", cartId);
		map.put("svcId", svcId);
		//
		return this.getSqlSession().selectOne(sqlId,map);
	}

}
