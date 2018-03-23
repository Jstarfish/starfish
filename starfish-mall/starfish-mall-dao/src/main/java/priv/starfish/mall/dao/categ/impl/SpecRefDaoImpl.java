package priv.starfish.mall.dao.categ.impl;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.categ.SpecRefDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.categ.entity.SpecRef;

@Component("specRefDao")
public class SpecRefDaoImpl extends BaseDaoImpl<SpecRef, Integer> implements SpecRefDao {

	@Override
	public SpecRef selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public SpecRef selectByCode(String code) {
		String sqlId = this.getNamedSqlId("selectByCode");
		return this.getSqlSession().selectOne(sqlId, code);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<SpecRef> selectSpecRef(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectSpecRef");
		//
		MapContext filterItems = paginatedFilter.getFilterItems();
		//
		String name = filterItems.getTypedValue("name", String.class);
		filterItems.remove("name");
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name);
			filterItems.put("name", name);
		}

		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<SpecRef> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public int insert(SpecRef specRef) {
		String sqlId = this.getNamedSqlId("insert");
		//
		int maxSeqNo = this.getEntityMaxSeqNo(SpecRef.class);
		specRef.setSeqNo(maxSeqNo + 1);
		//
		return this.getSqlSession().insert(sqlId, specRef);
	}

	@Override
	public int update(SpecRef specRef) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, specRef);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int batchDelByIds(List<Integer> ids) {
		String sqlId = this.getNamedSqlId("batchDelByIds");
		return this.getSqlSession().delete(sqlId, ids);
	}

	@Override
	public SpecRef selectColorSpecRef() {
		String sqlId = this.getNamedSqlId("selectColorSpecRef");
		return this.getSqlSession().selectOne(sqlId);
	}
}
