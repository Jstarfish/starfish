package priv.starfish.mall.dao.settle.impl;

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
import priv.starfish.mall.dao.settle.SaleSettleRecDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.settle.entity.SaleSettleRec;

@Component("saleSettleRecDao")
public class SaleSettleRecDaoImpl extends BaseDaoImpl<SaleSettleRec, Integer> implements SaleSettleRecDao {
	@Override
	public SaleSettleRec selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(SaleSettleRec saleSettleRec) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, saleSettleRec);
	}

	@Override
	public int update(SaleSettleRec saleSettleRec) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, saleSettleRec);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<SaleSettleRec> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		
		MapContext filter = paginatedFilter.getFilterItems();
		
		String peerName = filter.getTypedValue("peerName", String.class);
		filter.remove(peerName);
		if (StrUtil.hasText(peerName)) {
			peerName = SqlBuilder.likeStrVal(peerName.toString());
			filter.put("peerName", peerName);
		}

		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<SaleSettleRec> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public SaleSettleRec selectByReqNo(String reqNo) {
		String sqlId = this.getNamedSqlId("selectByReqNo");
		//
		return this.getSqlSession().selectOne(sqlId, reqNo);
	}

	@Override
	public int updateForState(SaleSettleRec saleSettleRec) {
		String sqlId = this.getNamedSqlId("updateForState");
		//
		return this.getSqlSession().update(sqlId, saleSettleRec);
	}

	@Override
	public List<SaleSettleRec> selectByMerchantIdAsSuccess(Map<String, Object> params) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public SaleSettleRec selecBySettleProcessId(Long settleProcessId) {
		String sqlId = this.getNamedSqlId("selecBySettleProcessId");
		//
		return this.getSqlSession().selectOne(sqlId, settleProcessId);
	}

	@Override
	public int updateForReqData(SaleSettleRec saleSettleRec) {
		String sqlId = this.getNamedSqlId("updateForReqData");
		//
		return this.getSqlSession().update(sqlId, saleSettleRec);
	}
}