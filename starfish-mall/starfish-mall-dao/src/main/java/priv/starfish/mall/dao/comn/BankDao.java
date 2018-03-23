package priv.starfish.mall.dao.comn;


import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.comn.entity.Bank;
import priv.starfish.mall.dao.base.BaseDao;

import java.util.List;

@IBatisSqlTarget
public interface BankDao extends BaseDao<Bank, String> {
	Bank selectById(String code);

	int insert(Bank bank);

	int update(Bank bank);

	int deleteById(String code);

	/**
	 * 获取银行列表
	 * 
	 * @author 廖晓远
	 * @date 2015年8月24日 上午10:32:11
	 * 
	 * @return List<Bank>
	 */
	List<Bank> selectList();
}