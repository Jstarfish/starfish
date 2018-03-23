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
import priv.starfish.mall.dao.categ.AttrRefDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.categ.entity.AttrRef;

@Component("attrRefDao")
public class AttrRefDaoImpl extends BaseDaoImpl<AttrRef, Integer> implements AttrRefDao {
	@Override
	public AttrRef selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public AttrRef selectByCode(String code) {
		String sqlId = this.getNamedSqlId("selectByCode");
		//
		return this.getSqlSession().selectOne(sqlId, code);
	}

	@Override
	public AttrRef selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}

	@Override
	public int insert(AttrRef attrRef) {
		String sqlId = this.getNamedSqlId("insert");
		int maxSeq = this.getEntityMaxSeqNo(AttrRef.class);
		attrRef.setSeqNo(maxSeq + 1);
		//
		return this.getSqlSession().insert(sqlId, attrRef);
	}

	@Override
	public int update(AttrRef attrRef) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, attrRef);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int deleteBatch(List<Integer> ids) {
		String sqlId = this.getNamedSqlId("deleteBatch");
		//
		return this.getSqlSession().delete(sqlId, ids);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<AttrRef> selectByfilter(PaginatedFilter paginatedFilter) {
		// 分页参数
		String sqlId = this.getNamedSqlId("selectByfilter");
		MapContext filterItems = paginatedFilter.getFilterItems();
		//
		String name = filterItems.getTypedValue("name", String.class);
		filterItems.remove("name");
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name);
			filterItems.put("name", name);
		}
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<AttrRef> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public AttrRef selectByBrandFlagIsTrue() {
		String sqlId = this.getNamedSqlId("selectListByBrandFlagIsTrue");
		return this.getSqlSession().selectOne(sqlId);
	}

}