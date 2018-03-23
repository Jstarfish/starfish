package priv.starfish.mall.dao.merchant.impl;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.merchant.MerchantSettleAcctDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.merchant.entity.MerchantSettleAcct;

@Component("merchantSettleAcctDao")
public class MerchantSettleAcctDaoImpl extends BaseDaoImpl<MerchantSettleAcct, Integer> implements MerchantSettleAcctDao {
	@Override
	public MerchantSettleAcct selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public MerchantSettleAcct selectByMerchantIdAndAccountId(Integer merchantId, Integer accountId) {
		String sqlId = this.getNamedSqlId("selectByMerchantIdAndAccountId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("merchantId", merchantId);
		params.put("accountId", accountId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public MerchantSettleAcct selectByMerchantIdAndBankCodeAndAcctNo(Integer merchantId, String bankCode, String acctNo) {
		String sqlId = this.getNamedSqlId("selectByMerchantIdAndBankCodeAndAcctNo");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("merchantId", merchantId);
		params.put("bankCode", bankCode);
		params.put("acctNo", acctNo);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}
	
	@Override
	public MerchantSettleAcct selectByMerchantIdAndSettleWayCode(Integer merchantId, String settleWayCode) {
		String sqlId = this.getNamedSqlId("selectByMerchantIdAndSettleWayCode");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("merchantId", merchantId);
		params.put("settleWayCode", settleWayCode);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(MerchantSettleAcct merchantSettleAcct) {
		String sqlId = this.getNamedSqlId("insert");
		//获取最大排序号
		int maxSeqNo = getEntityMaxSeqNo(MerchantSettleAcct.class);
		merchantSettleAcct.setSeqNo(maxSeqNo + 1);
		//
		return this.getSqlSession().insert(sqlId, merchantSettleAcct);
	}

	@Override
	public int update(MerchantSettleAcct merchantSettleAcct) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, merchantSettleAcct);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<MerchantSettleAcct> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<MerchantSettleAcct> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public int deleteByMerchantIdAndSettleWayCode(Integer merchantId, String settleWayCode) {
		String sqlId = this.getNamedSqlId("deleteByMerchantIdAndSettleWayCode");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("merchantId", merchantId);
		params.put("settleWayCode", settleWayCode);
		//
		return this.getSqlSession().delete(sqlId, params);
	}

	@Override
	public List<MerchantSettleAcct> selectByMerchantId(Integer merchantId) {
		String sqlId = this.getNamedSqlId("selectByMerchantId");
		//
		return this.getSqlSession().selectList(sqlId, merchantId);
	}
}