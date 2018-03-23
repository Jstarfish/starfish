package priv.starfish.mall.dao.car.impl;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.car.UserCarDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.car.entity.UserCar;

@Component("userCarDao")
public class UserCarDaoImpl extends BaseDaoImpl<UserCar, Integer> implements UserCarDao {
	@Override
	public UserCar selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public UserCar selectByUserIdAndModelId(Integer userId, Integer modelId) {
		String sqlId = this.getNamedSqlId("selectByUserIdAndModelId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("userId", userId);
		params.put("modelId", modelId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(UserCar userCar) {
		String sqlId = this.getNamedSqlId("insert");
		//
		if (userCar.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(UserCar.class, "userId", userCar.getUserId()) + 1;
			userCar.setSeqNo(seqNo);
		}
		//
		return this.getSqlSession().insert(sqlId, userCar);
	}

	@Override
	public int update(UserCar userCar) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, userCar);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<UserCar> selectUserCars(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectUserCars");
		//
		MapContext filter = paginatedFilter.getFilterItems();

		// 根据车辆名称查询使用
		String name = filter.getTypedValue("name", String.class);
		filter.remove(name);
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name.toString());
			filter.put("name", name);
		}

		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<UserCar> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public Integer updateUserCarByUserId(Integer userId) {
		String sqlId = this.getNamedSqlId("updateUserCarByUserId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("userId", userId);
		params.put("defaulted", false);
		//
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public UserCar selectDefaultByUserId(boolean defaulted, Integer userId) {
		String sqlId = this.getNamedSqlId("selectDefaultByUserId");
		Map<String, Object> params = this.newParamMap();
		params.put("userId", userId);
		params.put("defaulted", defaulted);
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public List<UserCar> selectByUserId(Integer userId) {
		String sqlId = this.getNamedSqlId("selectByUserId");
		return this.getSqlSession().selectList(sqlId, userId);
	}
}