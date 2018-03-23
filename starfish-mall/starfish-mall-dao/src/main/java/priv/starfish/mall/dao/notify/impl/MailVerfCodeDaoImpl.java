package priv.starfish.mall.dao.notify.impl;

import org.springframework.stereotype.Component;

import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.notify.MailVerfCodeDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.notify.entity.MailVerfCode;

@Component("mailVerfCodeDao")
public class MailVerfCodeDaoImpl extends BaseDaoImpl<MailVerfCode, Integer> implements MailVerfCodeDao {
	@Override
	public MailVerfCode selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(MailVerfCode mailVerfCode) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, mailVerfCode);
	}

	@Override
	public int update(MailVerfCode mailVerfCode) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, mailVerfCode);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public MailVerfCode selectByEmailAndVfCode(String email, String vfCode) {
		String sqlId = this.getNamedSqlId("selectByEmailAndVfCode");
		//
		MapContext params = MapContext.newOne();
		params.put("email", email);
		params.put("vfCode", vfCode);
		return this.getSqlSession().selectOne(sqlId, params);
	}
}