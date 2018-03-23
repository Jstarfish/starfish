package priv.starfish.mall.service;

import java.util.List;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.comn.entity.BankProvinceCode;
import priv.starfish.mall.comn.entity.UserAccount;
import priv.starfish.mall.merchant.entity.MerchantSettleAcct;
import priv.starfish.mall.settle.entity.SettleWay;

public interface UserAccountService extends BaseService {
	
	UserAccount getUserAccountById(Integer id);
	
	boolean createUserAccount(UserAccount userAccount);
	
	boolean updateUserAccount(UserAccount userAccount);
	
	boolean deleteUserAccountById(Integer userAccountId);

	// ----------------------------------用户------------------------------------
	/**
	 * 通过用户Id获取用户资金账户
	 * 
	 * @author 郝江奎
	 * @date 2015-9-24 下午10:34:26
	 * 
	 * @param userId
	 * @return
	 */
	List<UserAccount> getUserAccountsByUserId(Integer userId);

	/**
	 * 根据过滤条件获取用户资金列表
	 * 
	 * @author guoyn
	 * @date 2015年10月9日 下午6:35:50
	 * 
	 * @param paginatedFilter =userId, =typeFlag
	 * @return PaginatedList<UserAccount>
	 */
	PaginatedList<UserAccount> getUserAccountsByFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 根据过滤条件获取用户结算资金账户列表
	 * 
	 * @author 郝江奎
	 * @date 2015年11月2日 上午12:05:50
	 * 
	 * @param paginatedFilter 
	 * @return PaginatedList<UserAccount>
	 */
	PaginatedList<MerchantSettleAcct> getMerchantSettleAcctsByFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 根据过滤条件获取用户结算资金账户列表
	 * 
	 * @author 郝江奎
	 * @date 2015年11月2日 下午8:35:50
	 * 
	 * @param paginatedFilter 
	 * @return List<UserAccount>
	 */
	List<UserAccount> getUserAccountsDisByBankCode(Integer userId);

	List<SettleWay> getUserWay(int userId);

	List<BankProvinceCode> getBankProvinceCodes();
	
}
