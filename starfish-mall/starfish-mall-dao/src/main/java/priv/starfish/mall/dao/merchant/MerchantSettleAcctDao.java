package priv.starfish.mall.dao.merchant;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.merchant.entity.MerchantSettleAcct;

@IBatisSqlTarget
public interface MerchantSettleAcctDao extends BaseDao<MerchantSettleAcct, Integer> {
	MerchantSettleAcct selectById(Integer id);

	MerchantSettleAcct selectByMerchantIdAndAccountId(Integer merchantId, Integer accountId);

	MerchantSettleAcct selectByMerchantIdAndBankCodeAndAcctNo(Integer merchantId, String bankCode, String acctNo);

	MerchantSettleAcct selectByMerchantIdAndSettleWayCode(Integer merchantId, String settleWayCode);

	int insert(MerchantSettleAcct merchantSettleAcct);

	int update(MerchantSettleAcct merchantSettleAcct);

	int deleteById(Integer id);

	/**
	 * 通过paginatedFilter获取资金帐户
	 * 
	 * @author 郝江奎
	 * @date 2015年11月2日 下午2:39:57
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<MerchantSettleAcct> selectByFilter(PaginatedFilter paginatedFilter);

	int deleteByMerchantIdAndSettleWayCode(Integer merchantId, String settleWayCode);

	/**
	 * 通过商户ID，查找关联的结算资金账户
	 * 
	 * @author "WJJ"
	 * @date 2015年12月11日 下午2:40:01
	 * 
	 * @param id
	 * @return
	 */
	List<MerchantSettleAcct> selectByMerchantId(Integer merchantId);

}