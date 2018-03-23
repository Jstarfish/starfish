package priv.starfish.mall.dao.order.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;


import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.order.SaleOrderDao;
import priv.starfish.mall.order.entity.SaleOrder;

@Component("saleOrderDao")
public class SaleOrderDaoImpl extends BaseDaoImpl<SaleOrder, Long> implements SaleOrderDao {
	@Override
	public int updateForClosed(SaleOrder saleOrder) {
		String sqlId = this.getNamedSqlId("updateForClosed");
		//
		return this.getSqlSession().update(sqlId, saleOrder);
	}
	
	@Override
	public SaleOrder selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(SaleOrder saleOrder) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, saleOrder);
	}

	@Override
	public int update(SaleOrder saleOrder) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, saleOrder);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public PaginatedList<SaleOrder> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<SaleOrder> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<SaleOrder> selectByFilterNormal(MapContext filter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		return this.getSqlSession().selectList(sqlId, filter);
	}

	@Override
	public PaginatedList<SaleOrder> selectByUserId(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByUserId");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<SaleOrder> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public Integer selectCount(MapContext requestData) {
		String sqlId = this.getNamedSqlId("selectCount");
		//
		return this.getSqlSession().selectOne(sqlId, requestData);
	}

	@Override
	public SaleOrder selectByNo(String no) {
		String sqlId = this.getNamedSqlId("selectByNo");
		//
		return this.getSqlSession().selectOne(sqlId, no);
	}

	@Override
	public Integer updateForDelete(SaleOrder saleOrder) {
		String sqlId = this.getNamedSqlId("updateForDelete");
		//
		return this.getSqlSession().update(sqlId, saleOrder);
	}

	@Override
	public Integer updateForCancel(SaleOrder saleOrder) {
		String sqlId = this.getNamedSqlId("updateForCancel");
		//
		return this.getSqlSession().update(sqlId, saleOrder);
	}

	@Override
	public Integer updatePayStateByNo(Map<String, Object> map) {
		String sqlId = this.getNamedSqlId("updatePayStateByNo");
		//
		return this.getSqlSession().update(sqlId, map);
	}

	@Override
	public List<SaleOrder> selectByCompareSettleDayUse(Date settleDay, Date beforeSettleDay, Integer merchantId) {
		String sqlId = this.getNamedSqlId("selectByCompareSettleDayUse");
		Map<String, Object> params = this.newParamMap();
		params.put("settleDay", settleDay);
		params.put("beforeSettleDay", beforeSettleDay);
		params.put("merchantId", merchantId);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public List<Integer> selectMerchantIds(Date settleDay, Date beforeSettleDay) {
		String sqlId = this.getNamedSqlId("selectMerchantIds");
		Map<String, Object> params = this.newParamMap();
		params.put("settleDay", settleDay);
		params.put("beforeSettleDay", beforeSettleDay);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<SaleOrder> selectByFilterAsSettleDay(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilterAsSettleDay");

		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<SaleOrder> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<String> selectOrderIdsByProcessId(Long processId) {
		String sqlId = this.getNamedSqlId("selectOrderIdsByProcessId");
		//
		return this.getSqlSession().selectList(sqlId, processId);
	}

	@Override
	public Integer updateForFinish(SaleOrder saleOrder) {
		String sqlId = this.getNamedSqlId("updateForFinish");
		//
		return this.getSqlSession().update(sqlId, saleOrder);
	}

	@Override
	public Integer updateForFinishAsProxy(SaleOrder saleOrder) {
		String sqlId = this.getNamedSqlId("updateForFinishAsProxy");
		//
		return this.getSqlSession().update(sqlId, saleOrder);
	}

	@Override
	public Integer updateForAddInfo(SaleOrder saleOrder) {
		String sqlId = this.getNamedSqlId("updateForAddInfo");
		//
		return this.getSqlSession().update(sqlId, saleOrder);
	}

	@Override
	public List<SaleOrder> selectByCompareSettleDay(Date settleDay, Date beforeSettleDay) {
		String sqlId = this.getNamedSqlId("selectByCompareSettleDay");
		Map<String, Object> params = this.newParamMap();
		params.put("settleDay", settleDay);
		params.put("beforeSettleDay", beforeSettleDay);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public List<SaleOrder> selectForCreateSettleInfo(Integer merchantId, Date finishTime) {
		String sqlId = this.getNamedSqlId("selectForCreateSettleInfo");
		Map<String, Object> params = this.newParamMap();
		params.put("merchantId", merchantId);
		params.put("finishTime", finishTime);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public Date selectMinFinishedDay(Integer merchantId) {
		String sqlId = this.getNamedSqlId("selectMinFinishedDay");
		//
		return this.getSqlSession().selectOne(sqlId, merchantId);
	}

	@Override
	public Date selectMaxFinishedTimeByDate(Integer merchantId, Date yesterday) {
		String sqlId = this.getNamedSqlId("selectMaxFinishedTimeByDate");
		Map<String, Object> params = this.newParamMap();
		params.put("merchantId", merchantId);
		params.put("yesterday", yesterday);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public Date selectFinishedTimeBySettleProcessId(Long settleProcessId) {
		String sqlId = this.getNamedSqlId("selectFinishedTimeBySettleProcessId");
		//
		return this.getSqlSession().selectOne(sqlId, settleProcessId);
	}

	@Override
	public List<SaleOrder> selectByFinishedTimeAsToday(Integer merchantId) {
		String sqlId = this.getNamedSqlId("selectByFinishedTimeAsToday");
		Date finishTime = new Date();
		Map<String, Object> params = this.newParamMap();
		params.put("merchantId", merchantId);
		params.put("finishTime", finishTime);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public List<Integer> selectMerchantIdsAsFinishedDate(Date date) {
		String sqlId = this.getNamedSqlId("selectMerchantIdsAsFinishedDate");
		//
		return this.getSqlSession().selectList(sqlId, date);
	}

	@Override
	public List<SaleOrder> selectByMerchantName(String merchantName, Date finishTime) {
		String sqlId = this.getNamedSqlId("selectByMerchantName");
		Map<String, Object> params = this.newParamMap();
		params.put("merchantName", merchantName);
		params.put("finishTime", finishTime);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public int updateForRecId(String no, Integer settleRecId) {
		String sqlId = this.getNamedSqlId("updateForRecId");
		Map<String, Object> params = this.newParamMap();
		params.put("no", no);
		params.put("settleRecId", settleRecId);
		//
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public Integer updateForPayFinished(SaleOrder saleOrder) {
		String sqlId = this.getNamedSqlId("updateForPayFinished");
		//
		return this.getSqlSession().update(sqlId, saleOrder);
	}

	@Override
	public PaginatedList<SaleOrder> selectByDistributorId(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByDistributorId");

		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<SaleOrder> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<SaleOrder> selectBySettleRecId2Dist(Integer id) {
		String sqlId = this.getNamedSqlId("selectBySettleRecId2Dist");
		//
		return this.getSqlSession().selectList(sqlId, id);
	}

	@Override
	public PaginatedList<SaleOrder> selectDistShopOrderByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectDistShopOrderByFilter");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<SaleOrder> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}
	
	@Override
	public PaginatedList<SaleOrder> selectDistShopOrderSettleByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectDistShopOrderSettleByFilter");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<SaleOrder> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(),
				pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<SaleOrder> selectbyFilterByShop(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectbyFilterByShop");

		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<SaleOrder> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<Integer> selectShopIdsByUserId(Integer userId) {
		String sqlId = this.getNamedSqlId("selectShopIdsByUserId");
		return this.sqlSession.selectList(sqlId, userId);
	}
}