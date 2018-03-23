package priv.starfish.mall.dao.comn;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.entity.UserAccount;

@IBatisSqlTarget
public interface UserAccountDao extends BaseDao<UserAccount, Integer> {
	UserAccount selectById(Integer id);

	int insert(UserAccount userAccount);

	int update(UserAccount userAccount);

	int deleteById(Integer id);

	/**
	 * 通过用户获取账户列表
	 * 
	 * @author 廖晓远
	 * @date 2015年8月24日 下午1:13:03
	 * 
	 * @param userId
	 * @return
	 */
	List<UserAccount> selectByUserId(Integer userId);

	/**
	 * 通过用户删除资金帐户
	 * 
	 * @author 廖晓远
	 * @date 2015年8月24日 下午1:13:36
	 * 
	 * @param id
	 * @return
	 */
	int deleteByUserId(Integer id);

	/**
	 * 通过paginatedFilter获取资金帐户
	 * 
	 * @author guoyn
	 * @date 2015年10月9日 下午6:39:57
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<UserAccount> selectByFilter(PaginatedFilter paginatedFilter);

	List<UserAccount> selectDisByUserId(Integer userId);

	UserAccount selectByUserIdAndBankCodeAndAcctNo(Integer userId, String bankCode, String acctNo);

}