package priv.starfish.mall.dao.settle;

import java.util.List;
import java.util.Map;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.settle.entity.SaleSettleRec;

@IBatisSqlTarget
public interface SaleSettleRecDao extends BaseDao<SaleSettleRec, Integer> {
	SaleSettleRec selectById(Integer id);

	int insert(SaleSettleRec saleSettleRec);

	int update(SaleSettleRec saleSettleRec);

	int deleteById(Integer id);

	/**
	 * 分页查询销售结算记录（对最终客户）
	 * 
	 * @author "WJJ"
	 * @date 2015年10月19日 下午7:43:49
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SaleSettleRec> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据请求转账流水号，查询
	 * 
	 * @author "WJJ"
	 * @date 2015年11月24日 上午11:44:45
	 * 
	 * @param reqNo
	 * @return
	 */
	SaleSettleRec selectByReqNo(String reqNo);

	/**
	 * 更新状态state
	 * 
	 * @author "WJJ"
	 * @date 2015年11月24日 上午11:50:47
	 * 
	 * @param saleSettleRec
	 * @return
	 */
	int updateForState(SaleSettleRec saleSettleRec);

	/**
	 * 用于商户资金统计
	 * 
	 * @author "WJJ"
	 * @date 2015年12月29日 下午4:44:40
	 * 
	 * @param merchantId
	 * @param state
	 * @return
	 */
	List<SaleSettleRec> selectByMerchantIdAsSuccess(Map<String, Object> params);

	/**
	 * 根据结算单ID，查找结算记录
	 * 
	 * @author "WJJ"
	 * @date 2016年1月12日 上午10:13:25
	 * 
	 * @param settleProcessId
	 * @return
	 */
	SaleSettleRec selecBySettleProcessId(Long settleProcessId);

	/**
	 * 更新结算记录的请求数据
	 * 
	 * @author "WJJ"
	 * @date 2016年1月12日 上午10:22:19
	 * 
	 * @param saleRec
	 * @return
	 */
	int updateForReqData(SaleSettleRec saleRec);
}