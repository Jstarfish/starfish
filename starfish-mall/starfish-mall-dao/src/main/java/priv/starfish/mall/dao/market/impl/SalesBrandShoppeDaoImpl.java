package priv.starfish.mall.dao.market.impl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.market.SalesBrandShoppeDao;
import priv.starfish.mall.market.entity.SalesBrandShoppe;

@Component("salesBrandShoppeDao")
public class SalesBrandShoppeDaoImpl extends BaseDaoImpl<SalesBrandShoppe, Integer> implements SalesBrandShoppeDao {
	@Override
	public SalesBrandShoppe selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public SalesBrandShoppe selectByFloorNoAndBrandCode(Integer floorNo, String brandCode) {
		String sqlId = this.getNamedSqlId("selectByFloorNoAndBrandCode");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("floorNo", floorNo);
		params.put("brandCode", brandCode);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(SalesBrandShoppe salesBrandShoppe) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, salesBrandShoppe);
	}

	@Override
	public int update(SalesBrandShoppe salesBrandShoppe) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, salesBrandShoppe);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<SalesBrandShoppe> selectByFloorNo(Integer floorNo) {
		String sqlId = this.getNamedSqlId("selectByFloorNo");
		return this.getSqlSession().selectList(sqlId, floorNo);
	}

	@Override
	public int deleteByFloorNoAndUncontainIds(Integer floorNo, List<Integer> uncontainIds) {
		if (uncontainIds.size() == 0) {
			uncontainIds = null;
		}
		String sqlId = this.getNamedSqlId("deleteByFloorNoAndUncontainIds");
		Map<String, Object> params = this.newParamMap();
		params.put("floorNo", floorNo);
		params.put("uncontainIds", uncontainIds);
		return this.getSqlSession().delete(sqlId, params);
	}

	@Override
	public int deleteByFloorNo(Integer floorNo) {
		String sqlId = this.getNamedSqlId("deleteByFloorNo");
		return this.getSqlSession().delete(sqlId, floorNo);
	}
}