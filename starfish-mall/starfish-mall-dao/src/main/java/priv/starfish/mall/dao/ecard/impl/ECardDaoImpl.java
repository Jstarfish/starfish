package priv.starfish.mall.dao.ecard.impl;

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
import priv.starfish.mall.dao.ecard.ECardDao;
import priv.starfish.mall.ecard.entity.ECard;

@Component("eCardDao")
public class ECardDaoImpl extends BaseDaoImpl<ECard, String> implements ECardDao {
	@Override
	public ECard selectById(String code) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, code);
	}

	@Override
	public ECard selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}

	@Override
	public ECard selectByRank(Integer rank) {
		String sqlId = this.getNamedSqlId("selectByRank");
		//
		return this.getSqlSession().selectOne(sqlId, rank);
	}

	@Override
	public int insert(ECard eCard) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, eCard);
	}

	@Override
	public int update(ECard eCard) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, eCard);
	}

	@Override
	public int deleteById(String code) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, code);
	}

	@Override
	public PaginatedList<ECard> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");

		MapContext filter = paginatedFilter.getFilterItems();
		String name = filter.getTypedValue("name", String.class);
		filter.remove(name);
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name);
			filter.put("name", name);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<ECard> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);

		PageList = this.checkAndRefetchList(paginatedFilter, sqlId, PageList);

		return this.toPaginatedList(PageList);
	}

	@Override
	public Integer existsByName(String name) {
		String sqlId = this.getNamedSqlId("existsByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}

	@Override
	public List<ECard> selectNormalECards() {
		String sqlId = this.getNamedSqlId("selectNormalECards");
		//
		return this.getSqlSession().selectList(sqlId);
	}

	@Override
	public List<ECard> selectBySeqNo(int seqNo, int nowSeqNo) {
		String sqlId = this.getNamedSqlId("selectBySeqNo");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("seqNo", seqNo);
		params.put("nowSeqNo", nowSeqNo);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public int updateForSeqNo(String code, int seqNo) {
		String sqlId = this.getNamedSqlId("updateForSeqNo");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("code", code);
		params.put("seqNo", seqNo);
		//
		return this.getSqlSession().update(sqlId, params);
	}
}