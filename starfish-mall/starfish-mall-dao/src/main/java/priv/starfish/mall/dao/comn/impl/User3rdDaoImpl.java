package priv.starfish.mall.dao.comn.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.comn.User3rdDao;
import priv.starfish.mall.comn.entity.User3rd;

@Component("user3rdDao")
public class User3rdDaoImpl extends BaseDaoImpl<User3rd, Integer> implements User3rdDao {
	@Override
	public User3rd selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public User3rd selectByAppIdAndOpenId(Integer appId, String openId) {
		String sqlId = this.getNamedSqlId("selectByAppIdAndOpenId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("appId", appId);
		params.put("openId", openId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public Integer selectSysUserIdById(Integer id) {
		String sqlId = this.getNamedSqlId("selectSysUserIdById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public Integer selectSysUserIdByAppIdAndOpenId(Integer appId, String openId) {
		String sqlId = this.getNamedSqlId("selectSysUserIdByAppIdAndOpenId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("appId", appId);
		params.put("openId", openId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(User3rd user3rd) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, user3rd);
	}

	@Override
	public int update(User3rd user3rd) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, user3rd);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

}