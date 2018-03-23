package priv.starfish.mall.dao.comn.impl;


import org.springframework.stereotype.Component;
import priv.starfish.mall.comn.entity.Bank;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.comn.BankDao;

import java.util.List;

@Component("bankDao")
public class BankDaoImpl extends BaseDaoImpl<Bank, String> implements BankDao {
	@Override
	public Bank selectById(String code) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, code);
	}

	@Override
	public int insert(Bank bank) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, bank);
	}

	@Override
	public int update(Bank bank) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, bank);
	}

	@Override
	public int deleteById(String code) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, code);
	}

	@Override
	public List<Bank> selectList() {
		String sqlId = this.getNamedSqlId("selectList");
		return this.getSqlSession().selectList(sqlId);
	}
}