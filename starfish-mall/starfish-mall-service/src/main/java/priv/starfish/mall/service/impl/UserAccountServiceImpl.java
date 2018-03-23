package priv.starfish.mall.service.impl;

import org.springframework.stereotype.Service;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.comn.entity.BankProvinceCode;
import priv.starfish.mall.comn.entity.UserAccount;
import priv.starfish.mall.dao.comn.BankProvinceCodeDao;
import priv.starfish.mall.dao.comn.UserAccountDao;
import priv.starfish.mall.dao.merchant.MerchantSettleAcctDao;
import priv.starfish.mall.dao.settle.SettleWayDao;
import priv.starfish.mall.merchant.entity.MerchantSettleAcct;
import priv.starfish.mall.service.UserAccountService;
import priv.starfish.mall.settle.entity.SettleWay;

import javax.annotation.Resource;
import java.util.List;

@Service("userAccountService")
public class UserAccountServiceImpl extends BaseServiceImpl implements UserAccountService {

	@Resource
	UserAccountDao userAccountDao;
	
	@Resource
	SettleWayDao settleWayDao;
	
	@Resource
	MerchantSettleAcctDao merchantSettleAcctDao;
	
	@Resource
	BankProvinceCodeDao bankProvinceCodeDao;

	@Override
	public UserAccount getUserAccountById(Integer id) {
		return userAccountDao.selectById(id);
	}

	@Override
	public boolean createUserAccount(UserAccount userAccount) {
		return userAccountDao.insert(userAccount) > 0;
	}

	@Override
	public boolean updateUserAccount(UserAccount userAccount) {
		return userAccountDao.update(userAccount) > 0;
	}

	@Override
	public boolean deleteUserAccountById(Integer userAccountId) {
		return userAccountDao.deleteById(userAccountId) > 0;
	}
	
	@Override
	public List<UserAccount> getUserAccountsByUserId(Integer userId) {
		return userAccountDao.selectByUserId(userId);
	}

	@Override
	public PaginatedList<UserAccount> getUserAccountsByFilter(PaginatedFilter paginatedFilter) {
		return userAccountDao.selectByFilter(paginatedFilter);
	}

	@Override
	public PaginatedList<MerchantSettleAcct> getMerchantSettleAcctsByFilter(PaginatedFilter paginatedFilter) {
		return merchantSettleAcctDao.selectByFilter(paginatedFilter);
	}

	@Override
	public List<UserAccount> getUserAccountsDisByBankCode(Integer userId) {
		List<UserAccount> userAccountList = userAccountDao.selectDisByUserId(userId);
		for (UserAccount userAccount : userAccountList) {
			MerchantSettleAcct merchantSettleAcct = merchantSettleAcctDao.selectByMerchantIdAndAccountId(userId, userAccount.getId());
			userAccount.setMerchantSettleAcct(merchantSettleAcct);
		}
		return userAccountList;
	}

	@Override
	public List<SettleWay> getUserWay(int userId) {
		List<SettleWay> settleWayList = settleWayDao.selectAll(false);
		for (SettleWay settleWay : settleWayList) {
			MerchantSettleAcct merchantSettleAcct = merchantSettleAcctDao.selectByMerchantIdAndSettleWayCode(userId, settleWay.getCode());
			settleWay.setUserId(userId);
			settleWay.setMerchantSettleAcct(merchantSettleAcct);
		}
		return settleWayList;
	}

	@Override
	public List<BankProvinceCode> getBankProvinceCodes() {
		return bankProvinceCodeDao.selectAll();
	}

}
