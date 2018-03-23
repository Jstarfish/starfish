package priv.starfish.mall.dao.ecard.impl;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.ecard.ECardTransactRecDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.ecard.entity.ECardTransactRec;

@Component("eCardTransactRecDao")
public class ECardTransactRecDaoImpl extends BaseDaoImpl<ECardTransactRec, Long> implements ECardTransactRecDao {
	@Override
	public ECardTransactRec selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(ECardTransactRec eCardTransactRec) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, eCardTransactRec);
	}

	@Override
	public int update(ECardTransactRec eCardTransactRec) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, eCardTransactRec);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public PaginatedList<ECardTransactRec> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");

		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<ECardTransactRec> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<ECardTransactRec> selectByCardId(Integer cardId) {
		String sqlId = this.getNamedSqlId("selectByCardId");
		//
		return this.getSqlSession().selectList(sqlId, cardId);
	}

	@Override
	public List<ECardTransactRec> selectByFilterNormal(MapContext filter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		return this.getSqlSession().selectList(sqlId, filter);
	}
}
