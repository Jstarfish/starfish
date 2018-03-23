package priv.starfish.mall.dao.merchant;

import java.util.Date;
import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.merchant.entity.Merchant;

@IBatisSqlTarget
public interface MerchantDao extends BaseDao<Merchant, Integer> {
	Merchant selectById(Integer id);

	int insert(Merchant merchant);

	int update(Merchant merchant);

	int deleteById(Integer id);

	/**
	 * 分页查询商户信息列表
	 * 
	 * @author 廖晓远
	 * @date 2015-5-19 下午3:10:22
	 * 
	 * @param paginatedFilter
	 * @return PaginatedList<Merchant> 商户分页列表
	 */
	PaginatedList<Merchant> selectMerchants(PaginatedFilter paginatedFilter);

	/**
	 * 通过查询条件查询所有商户
	 * 
	 * @author 廖晓远
	 * @date 2015-5-22 下午2:50:18
	 * 
	 * @param param
	 * @return
	 */
	List<Merchant> selectAllMerchants(MapContext param);
	
	/**
	 * 通过手机号码查询商户
	 * 
	 * @author 廖晓远
	 * @date 2015年7月3日 下午3:23:55
	 * 
	 * @param phoneNo
	 * @return
	 */
	Merchant selectByPhoneNo(String phoneNo);

	/**
	 * 通过结算日查询所有的商户
	 * 
	 * @author "WJJ"
	 * @date 2015年12月5日 下午4:03:37
	 * 
	 * @param date
	 * @return
	 */
	List<Merchant> selectBySettleDay(Date settleDay);

	/**
	 * 查询所有处于disabled状态的商户
	 * 
	 * @author "WJJ"
	 * @date 2015年12月11日 下午3:25:11
	 * 
	 * @param disabled
	 * @return
	 */
	List<Merchant> selectMerchantsAsEnabled(Boolean disabled);

}