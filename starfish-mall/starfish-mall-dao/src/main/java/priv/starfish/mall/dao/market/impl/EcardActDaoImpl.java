package priv.starfish.mall.dao.market.impl;

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
import priv.starfish.mall.dao.market.EcardActDao;
import priv.starfish.mall.market.entity.EcardAct;

@Component("ecardActDao")
public class EcardActDaoImpl extends BaseDaoImpl<EcardAct, Integer> implements EcardActDao {
	@Override
	public EcardAct selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public EcardAct selectByYearAndName(Integer year, String name) {
		String sqlId = this.getNamedSqlId("selectByYearAndName");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("year", year);
		params.put("name", name);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(EcardAct ecardAct) {
		String sqlId = this.getNamedSqlId("insert");
		//
		if (ecardAct.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(EcardAct.class, "year", ecardAct.getYear()) + 1;
			ecardAct.setSeqNo(seqNo);
		}
		return this.getSqlSession().insert(sqlId, ecardAct);
	}

	@Override
	public int update(EcardAct ecardAct) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, ecardAct);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<EcardAct> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		MapContext filter = paginatedFilter.getFilterItems();
		String name = filter.getTypedValue("name", String.class);
		filter.remove(name);
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name.toString());
			filter.put("name", name);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<EcardAct> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}
}
