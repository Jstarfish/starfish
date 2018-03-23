package priv.starfish.mall.dao.ecard.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.ecard.ECardTransferRecDao;
import priv.starfish.mall.ecard.entity.ECardTransferRec;

@Component("eCardTransferRecDao")
public class ECardTransferRecDaoImpl extends BaseDaoImpl<ECardTransferRec, Integer> implements ECardTransferRecDao {
	@Override
	public ECardTransferRec selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(ECardTransferRec eCardTransferRec) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, eCardTransferRec);
	}

	@Override
	public int update(ECardTransferRec eCardTransferRec) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, eCardTransferRec);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public PaginatedList<ECardTransferRec> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");

		// TODO:此处应该改为根据e卡编号进行模糊查询
		MapContext filter = paginatedFilter.getFilterItems();
		/*String cardId = filter.getTypedValue("cardId", String.class);
		filter.remove(cardId);
		if (StrUtil.hasText(cardId)) {
			cardId = SqlBuilder.likeStrVal(cardId);
			filter.put("cardId", cardId);
		}*/
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<ECardTransferRec> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public ECardTransferRec selectByCardIdAndUserIdTo(Integer cardId, Integer userIdTo) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cardId", cardId);
		params.put("userIdTo", userIdTo);
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public List<ECardTransferRec> selectByFilterNormal(MapContext filter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		return this.getSqlSession().selectList(sqlId, filter);
	}

	@Override
	public int updateForDelete(ECardTransferRec eCardTransferRec) {
		String sqlId = this.getNamedSqlId("updateForDelete");
		//
		return this.getSqlSession().update(sqlId, eCardTransferRec);
	}
}