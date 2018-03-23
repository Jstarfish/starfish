package priv.starfish.mall.dao.logistic.impl;

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
import priv.starfish.mall.dao.logistic.DeliveryWayDao;
import priv.starfish.mall.logistic.entity.DeliveryWay;

@Component("deliveryWayDao")
public class DeliveryWayDaoImpl extends BaseDaoImpl<DeliveryWay, Integer> implements DeliveryWayDao {
	@Override
	public DeliveryWay selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public DeliveryWay selectByNameAndComId(String name, Integer comId) {
		String sqlId = this.getNamedSqlId("selectByNameAndComId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("name", name);
		params.put("comId", comId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(DeliveryWay deliveryWay) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, deliveryWay);
	}

	@Override
	public int update(DeliveryWay deliveryWay) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, deliveryWay);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public PaginatedList<DeliveryWay> selectList(PaginatedFilter paginatedFilter) {
		// 分页参数
		String sqlId = this.getNamedSqlId("selectDeliveryWays");
		
		MapContext filterItems = paginatedFilter.getFilterItems();
		//
		String comName = filterItems.getTypedValue("comName", String.class);
		filterItems.remove("comName");
		if (StrUtil.hasText(comName)) {
			comName = SqlBuilder.likeStrVal(comName);
			filterItems.put("comName", comName);
		}
		
		String name = filterItems.getTypedValue("name", String.class);
		filterItems.remove("name");
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name);
			filterItems.put("name", name);
		}
		
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<DeliveryWay> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

}
