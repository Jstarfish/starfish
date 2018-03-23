package priv.starfish.mall.service;

import java.util.List;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.comn.entity.Bank;
import priv.starfish.mall.merchant.dto.MerchantDto;
import priv.starfish.mall.merchant.entity.Merchant;
import priv.starfish.mall.merchant.entity.MerchantSettleAcct;
import priv.starfish.mall.settle.entity.SettleWay;
import priv.starfish.mall.shop.dto.ShopDto;

public interface MerchantService extends BaseService {

	/**
	 * 添加商户
	 * 
	 * @author 廖晓远
	 * @date 2015-5-20 下午5:44:10
	 * 
	 * @param merchant
	 *            商户对象包含店铺、用户和账户数据
	 * @return
	 */
	boolean saveMerchant(MerchantDto merchantDto);

	/**
	 * 修改商户
	 * 
	 * @author 廖晓远
	 * @date 2015年8月27日 下午8:51:51
	 * 
	 * @param merchant
	 * @return
	 */
	boolean updateMerchant(Merchant merchant);

	/**
	 * 分页查询商户信息
	 * 
	 * @author 廖晓远
	 * @date 2015-5-20 下午8:44:07
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<Merchant> getMerchants(PaginatedFilter paginatedFilter);

	/**
	 * 通过商户Id删除商户
	 * 
	 * @author 廖晓远
	 * @date 2015-5-21 下午6:21:58
	 * 
	 * @param merchId
	 * @return boolean
	 */
	boolean delteMerchantById(Integer merchId);

	/**
	 * 通过商户Id删除商户
	 * 
	 * @author 廖晓远
	 * @date 2015-5-21 下午6:35:55
	 * 
	 * @param ids
	 * @return
	 */
	boolean deleteMerchantByIds(List<String> ids);

	/**
	 * 根据查询条件获取所有商户信息
	 * 
	 * @author 廖晓远
	 * @date 2015-5-22 下午2:45:10
	 * 
	 * @return
	 */
	List<Merchant> getAllMerchant(MapContext param);

	/**
	 * 根据手机号码获取商户信息
	 * 
	 * @author 廖晓远
	 * @date 2015年7月3日 下午3:21:06
	 * 
	 * @param phoneNo
	 * @return
	 */
	Merchant getMerchantByPhoneNo(String phoneNo);

	/**
	 * 根据Id获取商户信息
	 * 
	 * @author 廖晓远
	 * @date 2015年8月27日 下午3:50:35
	 * 
	 * @param id
	 * @return
	 */
	Merchant getMerchantById(Integer id);

	/**
	 * 查询所以银行列表
	 * 
	 * @author 廖晓远
	 * @date 2015年8月24日 上午10:37:29
	 * 
	 * @return
	 */
	List<Bank> getBanks();

	/**
	 * 商户入驻
	 * 
	 * @author guoyn
	 * @date 2015年10月8日 下午2:46:06
	 * 
	 * @param shopDto
	 * @return boolean
	 */
	boolean settledMerchant(ShopDto shopDto);

	boolean creatMerchantSettleAcct(MerchantSettleAcct merchantSettleAcct);

	boolean deleteMerchantSettleAcct(MerchantSettleAcct merchantSettleAcct);

	MerchantSettleAcct selectMerchantSettleAcctByMerchantIdAndAccountId(Integer merchantId, Integer accountId);

	MerchantSettleAcct getByMerchantIdAndSettleWayCode(Integer merchantId, String settleWayCode);

	SettleWay getSettleWayBySettleWayCode(String settleWayCode);

	boolean updateMerchantSettleAcct(MerchantSettleAcct merchantSettleAcct);
	
}
