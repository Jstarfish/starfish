package priv.starfish.mall.dao.comn.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.comn.UserLinkWayDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.comn.entity.UserLinkWay;

@Component("userLinkWayDao")
public class UserLinkWayDaoImpl extends BaseDaoImpl<UserLinkWay, Integer> implements UserLinkWayDao {
	@Override
	public UserLinkWay selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public UserLinkWay selectByUserIdAndAlias(Integer userId, String alias) {
		String sqlId = this.getNamedSqlId("selectByUserIdAndAlias");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("userId", userId);
		params.put("alias", alias);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(UserLinkWay userLinkWay) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, userLinkWay);
	}

	@Override
	public int update(UserLinkWay userLinkWay) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, userLinkWay);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<UserLinkWay> selectByUserId(Integer userId) {
		String sqlId = this.getNamedSqlId("selectByUserId");
		//
		return this.getSqlSession().selectList(sqlId, userId);
	}

	@Override
	public UserLinkWay selectDefaultByUserId(Integer userId, boolean defaulted) {
		String sqlId = this.getNamedSqlId("selectDefaultByUserId");
		Map<String, Object> params = this.newParamMap();
		params.put("userId", userId);
		params.put("defaulted", defaulted);
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int updateLinkWayByUserId(Integer userId) {
		String sqlId = this.getNamedSqlId("updateLinkWayByUserId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("userId", userId);
		params.put("defaulted", false);
		//
		return this.getSqlSession().update(sqlId, params);
	}
}