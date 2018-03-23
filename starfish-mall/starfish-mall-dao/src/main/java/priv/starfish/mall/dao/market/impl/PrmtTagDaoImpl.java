package priv.starfish.mall.dao.market.impl;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.market.PrmtTagDao;
import priv.starfish.mall.market.entity.PrmtTag;

@Component("prmtTagDao")
public class PrmtTagDaoImpl extends BaseDaoImpl<PrmtTag, Integer> implements PrmtTagDao {
	@Override
	public PrmtTag selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public PrmtTag selectByCode(String code) {
		String sqlId = this.getNamedSqlId("selectByCode");
		//
		return this.getSqlSession().selectOne(sqlId, code);
	}

	@Override
	public int insert(PrmtTag prmtTag) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, prmtTag);
	}

	@Override
	public int update(PrmtTag prmtTag) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, prmtTag);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<PrmtTag> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		MapContext filter = paginatedFilter.getFilterItems();
		String name = filter.getTypedValue("name", String.class);
		if(StrUtil.hasText(name)){
			name = SqlBuilder.likeStrVal(name);
			filter.put("name", name);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<PrmtTag> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<PrmtTag> selectAll() {
		String sqlId = this.getNamedSqlId("selectAll");
		//
		return this.getSqlSession().selectList(sqlId);
	}
}