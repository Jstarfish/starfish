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
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.car.CarBrandDao;
import priv.starfish.mall.car.entity.CarBrand;

@Component("carBrandDao")
public class CarBrandDaoImpl extends BaseDaoImpl<CarBrand, Integer> implements CarBrandDao {
	@Override
	public CarBrand selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public CarBrand selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}

	@Override
	public int insert(CarBrand carBrand) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, carBrand);
	}

	@Override
	public int update(CarBrand carBrand) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, carBrand);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public CarBrand selectByRefId(Integer refId) {
		String sqlId = this.getNamedSqlId("selectByRefId");
		//
		return this.getSqlSession().selectOne(sqlId, refId);
	}

	@Override
	public List<CarBrand> selectAll(boolean includeDisabled) {
		String sqlId = this.getNamedSqlId("selectAll");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("includeDisabled", includeDisabled);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<CarBrand> selectCarBrands(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectCarBrands");
		//
		MapContext filter = paginatedFilter.getFilterItems();

		// 根据店铺名查询使用
		String name = filter.getTypedValue("name", String.class);
		filter.remove(name);
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name.toString());
			filter.put("name", name);
		}

		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<CarBrand> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public int updateDisabled(Integer id, boolean disabled) {
		String sqlId = this.getNamedSqlId("updateDisabled");
		
		Map<String, Object> params = this.newParamMap();
		params.put("id", id);
		params.put("disabled", disabled);
		//
		return this.getSqlSession().update(sqlId, params);
	}
}