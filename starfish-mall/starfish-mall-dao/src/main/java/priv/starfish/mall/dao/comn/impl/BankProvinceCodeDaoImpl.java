package priv.starfish.mall.dao.comn.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.comn.BankProvinceCodeDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.comn.entity.BankProvinceCode;

@Component("bankProvinceCodeDao")
public class BankProvinceCodeDaoImpl extends BaseDaoImpl<BankProvinceCode, Integer> implements BankProvinceCodeDao {
	@Override
	public BankProvinceCode selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(BankProvinceCode bankProvinceCode) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, bankProvinceCode);
	}

	@Override
	public int update(BankProvinceCode bankProvinceCode) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, bankProvinceCode);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<BankProvinceCode> selectAll() {
		String sqlId = this.getNamedSqlId("selectAll");
		//
		return this.getSqlSession().selectList(sqlId);
	}
}