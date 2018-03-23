package priv.starfish.mall.dao.comn.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.comn.UserVerfStatusDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.comn.dict.VerfAspect;
import priv.starfish.mall.comn.entity.UserVerfStatus;

@Component("userVerfStatusDao")
public class UserVerfStatusDaoImpl extends BaseDaoImpl<UserVerfStatus, Integer> implements UserVerfStatusDao {
	@Override
	public UserVerfStatus selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}
	
	@Override
	public List<UserVerfStatus> selectByUserId(Integer userId) {
		String sqlId = this.getNamedSqlId("selectByUserId");
		//
		return this.getSqlSession().selectList(sqlId, userId);
	}

	@Override
	public UserVerfStatus selectByUserIdAndAspect(Integer userId, VerfAspect aspect) {
		String sqlId = this.getNamedSqlId("selectByUserIdAndAspect");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("userId", userId);
		params.put("aspect", aspect);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(UserVerfStatus userVerfStatus) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, userVerfStatus);
	}

	@Override
	public int update(UserVerfStatus userVerfStatus) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, userVerfStatus);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int deleteByUserIdAndAspect(Integer userId, VerfAspect aspect) {
		String sqlId = this.getNamedSqlId("deleteByUserIdAndAspect");
		//
		MapContext params = MapContext.newOne();
		params.put("userId", userId);
		params.put("aspect", aspect);
		//
		return this.getSqlSession().delete(sqlId, params);
	}

}