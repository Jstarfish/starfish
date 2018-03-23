package priv.starfish.mall.dao.svcx.impl;

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
import priv.starfish.mall.dao.svcx.SvcPackDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.svcx.entity.SvcPack;

@Component("svcPackDao")
public class SvcPackDaoImpl extends BaseDaoImpl<SvcPack, Integer> implements SvcPackDao {
	@Override
	public SvcPack selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public SvcPack selectByKindIdAndName(Integer kindId, String name) {
		String sqlId = this.getNamedSqlId("selectByKindIdAndName");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("kindId", kindId);
		params.put("name", name);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(SvcPack svcPack) {
		String sqlId = this.getNamedSqlId("insert");
		if (svcPack.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(SvcPack.class) + 1;
			svcPack.setSeqNo(seqNo);
		}
		return this.getSqlSession().insert(sqlId, svcPack);
	}

	@Override
	public int update(SvcPack svcPack) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, svcPack);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<SvcPack> selectByFilter(PaginatedFilter paginatedFilter) {
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
		PageList<SvcPack> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<SvcPack> selectByFilter(MapContext requestFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		return this.getSqlSession().selectList(sqlId, requestFilter);
	}
}
