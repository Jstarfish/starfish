package priv.starfish.mall.dao.member.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.member.MemberPointRuleDao;
import priv.starfish.mall.member.entity.MemberPointRule;

@Component("memberPointRuleDao")
public class MemberPointRuleDaoImpl extends BaseDaoImpl<MemberPointRule, Integer> implements MemberPointRuleDao {
	@Override
	public MemberPointRule selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public MemberPointRule selectByCode(String code) {
		String sqlId = this.getNamedSqlId("selectByCode");
		//
		return this.getSqlSession().selectOne(sqlId, code);
	}

	@Override
	public int insert(MemberPointRule memberPointRule) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, memberPointRule);
	}

	@Override
	public int update(MemberPointRule memberPointRule) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, memberPointRule);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int updateByCode(MemberPointRule rule) {
		String sqlId = this.getNamedSqlId("updateByCode");
		//
		return this.getSqlSession().update(sqlId, rule);
	}

	@Override
	public List<MemberPointRule> selectAll() {
		String sqlId = this.getNamedSqlId("selectAll");
		//
		return this.getSqlSession().selectList(sqlId);
	}
}