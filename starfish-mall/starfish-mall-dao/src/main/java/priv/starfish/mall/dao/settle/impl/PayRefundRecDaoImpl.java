package priv.starfish.mall.dao.settle.impl;

import java.util.Date;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.settle.PayRefundRecDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.settle.entity.PayRefundRec;

@Component("payRefundRecDao")
public class PayRefundRecDaoImpl extends BaseDaoImpl<PayRefundRec, Long> implements PayRefundRecDao {
	@Override
	public PayRefundRec selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(PayRefundRec payRefundRec) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, payRefundRec);
	}

	@Override
	public int update(PayRefundRec payRefundRec) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, payRefundRec);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<PayRefundRec> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");

		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<PayRefundRec> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public PayRefundRec selectByOrderId(String orderId) {
		String sqlId = this.getNamedSqlId("selectByOrderId");
		//
		return this.getSqlSession().selectOne(sqlId, orderId);
	}

	@Override
	public Integer updatePayRefundRecForRefund(PayRefundRec payRefundRec) {
		String sqlId = this.getNamedSqlId("updatePayRefundRecForRefund");
		//
		return this.getSqlSession().update(sqlId, payRefundRec);
	}

	@Override
	public PayRefundRec selectByTradeNo(String tradeNo) {
		String sqlId = this.getNamedSqlId("selectByTradeNo");
		//
		return this.getSqlSession().selectOne(sqlId, tradeNo);
	}

	@Override
	public Integer updateRefundStatus(PayRefundRec payRefundRec) {
		String sqlId = this.getNamedSqlId("updateRefundStatus");
		//
		return this.getSqlSession().update(sqlId, payRefundRec);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<PayRefundRec> selectByToRefundFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByToRefundFilter");
		//
		MapContext filter = paginatedFilter.getFilterItems();
		Date date = new Date();
		filter.put("date", date);

		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<PayRefundRec> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<PayRefundRec> selectByCanRefundFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByCanRefundFilter");
		//
		MapContext filter = paginatedFilter.getFilterItems();
		Date date = new Date();
		filter.put("date", date);

		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<PayRefundRec> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<PayRefundRec> selectByRefundAuditRecsFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByRefundAuditRecsFilter");
		//
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<PayRefundRec> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public int updateRefundStatu(PayRefundRec payRefundRec) {
		String sqlId = this.getNamedSqlId("updateRefundStatu");
		//
		return this.getSqlSession().update(sqlId, payRefundRec);
	}

	@Override
	public PayRefundRec selectByOrderNo(String no) {
		String sqlId = this.getNamedSqlId("selectByOrderNo");
		//
		return this.getSqlSession().selectOne(sqlId, no);
	}

	@Override
	public int updateRefundInfoForWechatPay(PayRefundRec payRefundRec) {
		String sqlId = this.getNamedSqlId("updateRefundInfoForWechatPay");
		//
		return this.getSqlSession().update(sqlId, payRefundRec);
	}
}