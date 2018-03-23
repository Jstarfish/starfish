package priv.starfish.mall.dao.market.impl;

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
import priv.starfish.mall.dao.market.AdvertDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.market.entity.Advert;

@Component("advertDao")
public class AdvertDaoImpl extends BaseDaoImpl<Advert, Integer> implements
        AdvertDao {
	@Override
	public Advert selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public Advert selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}

	@Override
	public int insert(Advert advert) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, advert);
	}

	@Override
	public int update(Advert advert) {
		String sqlId = this.getNamedSqlId("update");
		//
		Map<String, Object> params = this.newParamMap();

		return this.getSqlSession().update(sqlId, advert);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<Advert> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		MapContext filterItems = paginatedFilter.getFilterItems();
		//
		String name = filterItems.getTypedValue("name", String.class);
		filterItems.remove("name");
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name);
			filterItems.put("name", name);
		}
		String posCode = filterItems.getTypedValue("posCode", String.class);
		filterItems.remove("posCode");
		if (StrUtil.hasText(posCode)) {
			posCode = SqlBuilder.likeStrVal(posCode);
			filterItems.put("posCode", posCode);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<Advert> PageList = (PageList) getSqlSession().selectList(
				sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public Advert selectByPosCode(String posCode) {
		String sqlId = this.getNamedSqlId("selectByPosCode");
		//
		return this.getSqlSession().selectOne(sqlId, posCode);
	}

	@Override
	public List<Advert> selectAll() {
		String sqlId = this.getNamedSqlId("selectAll");
		//
		return this.getSqlSession().selectList(sqlId);
	}
}