package priv.starfish.mall.dao.car.impl;

import java.util.HashMap;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.car.UserCarSvcRecDao;
import priv.starfish.mall.car.entity.UserCarSvcRec;

@Component("userCarSvcRecDao")
public class UserCarSvcRecDaoImpl extends BaseDaoImpl<UserCarSvcRec, Integer> implements UserCarSvcRecDao {
	@Override
	public UserCarSvcRec selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}
	
	
	@Override
	public UserCarSvcRec selectUserCarSvcRecByUserIdAndCarId(Integer userId, Integer carId) {
		String sqlId = this.getNamedSqlId("selectUserCarSvcRecs");
		Map<String,Integer> map=new HashMap<String,Integer>();
		map.put("userId", userId);
		map.put("carId", carId);
		return this.getSqlSession().selectOne(sqlId, map);
	}


	@Override
	public int insert(UserCarSvcRec userCarSvcRec) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, userCarSvcRec);
	}

	@Override
	public int update(UserCarSvcRec userCarSvcRec) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, userCarSvcRec);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<UserCarSvcRec> selectUserCarSvcRecs(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectUserCarSvcRecs");
		//
		MapContext filter = paginatedFilter.getFilterItems();

		// 根据车辆名称查询使用
		String carName = filter.getTypedValue("carName", String.class);
		filter.remove(carName);
		if (StrUtil.hasText(carName)) {
			carName = SqlBuilder.likeStrVal(carName.toString());
			filter.put("carName", carName);
		}
		
		// 根据店铺名称查询使用
		String shopName = filter.getTypedValue("shopName", String.class);
		filter.remove(shopName);
		if (StrUtil.hasText(shopName)) {
			shopName = SqlBuilder.likeStrVal(shopName.toString());
			filter.put("shopName", shopName);
		}

		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<UserCarSvcRec> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}
}