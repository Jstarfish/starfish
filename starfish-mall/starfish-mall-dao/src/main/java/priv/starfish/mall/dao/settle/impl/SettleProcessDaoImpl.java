package priv.starfish.mall.dao.settle.impl;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.settle.SettleProcessDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.settle.entity.SettleProcess;

import java.util.Date;
import java.util.List;

@Component("settleProcessDao")
public class SettleProcessDaoImpl extends BaseDaoImpl<SettleProcess, Long> implements SettleProcessDao {
	@Override
	public SettleProcess selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(SettleProcess settleProcess) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, settleProcess);
	}

	@Override
	public int update(SettleProcess settleProcess) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, settleProcess);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int updateForReq(SettleProcess settleProcess) {
		String sqlId = this.getNamedSqlId("updateForReq");
		//
		return this.getSqlSession().update(sqlId, settleProcess);
	}

	@Override
	public SettleProcess selectByReqNo(String reqNo) {
		String sqlId = this.getNamedSqlId("selectByReqNo");
		//
		return this.getSqlSession().selectOne(sqlId, reqNo);
	}

	@Override
	public int updateForResp(SettleProcess settleProcess) {
		String sqlId = this.getNamedSqlId("updateForResp");
		//
		return this.getSqlSession().update(sqlId, settleProcess);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<SettleProcess> selectByFilterAsMall(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilterAsMall");
		//
		MapContext filter = paginatedFilter.getFilterItems();
		Date date = new Date();
		filter.put("date", date);

		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<SettleProcess> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<SettleProcess> selectByFilterAsMerch(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilterAsMerch");
		//
		MapContext filter = paginatedFilter.getFilterItems();
		Date date = new Date();
		filter.put("date", date);

		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<SettleProcess> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public int updateSettleFlag(SettleProcess settleProcess) {
		String sqlId = this.getNamedSqlId("updateSettleFlag");
		//
		return this.getSqlSession().update(sqlId, settleProcess);
	}

	@Override
	public int selectBySettleDay(Date settleDay) {
		String sqlId = this.getNamedSqlId("selectBySettleDay");
		//
		return this.getSqlSession().selectOne(sqlId, settleDay);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<SettleProcess> selectByFilterAsSuccess(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilterAsSuccess");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<SettleProcess> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	/*
	 * @Override public List<SettleProcess> selectByMerchantIdAsSuccess(Integer merchantId) { String sqlId = this.getNamedSqlId("selectByMerchantIdAsSuccess"); // return
	 * this.getSqlSession().selectList(sqlId, merchantId); }
	 */

	@Override
	public Date selectMaxCreateDay(Integer merchantId) {
		String sqlId = this.getNamedSqlId("selectMaxCreateDay");
		//
		return this.getSqlSession().selectOne(sqlId, merchantId);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<SettleProcess> selectByWaitingFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByWaitingFilter");

		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<SettleProcess> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);

		return this.toPaginatedList(PageList);
	}

	@Override
	public int submitSettleInfo() {
		String sqlId = this.getNamedSqlId("submitSettleInfo");
		//
		return this.getSqlSession().update(sqlId);
	}

	@Override
	public int updateSettleInfo(SettleProcess settleProcess) {
		String sqlId = this.getNamedSqlId("updateSettleInfo");
		//
		return this.getSqlSession().update(sqlId, settleProcess);
	}

	@Override
	public List<SettleProcess> selectBysettleFlag() {
		String sqlId = this.getNamedSqlId("selectBysettleFlag");
		//
		return this.getSqlSession().selectList(sqlId);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<SettleProcess> selectByFilterAsSettleDay(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilterAsSettleDay");

		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<SettleProcess> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);

		return this.toPaginatedList(PageList);
	}
}
