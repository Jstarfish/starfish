package priv.starfish.mall.dao.settle;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.settle.entity.SettleWay;

/**
 * 结算方式
 * 
 * @author koqiui
 * @date 2015年12月31日 上午1:35:16
 *
 */
@IBatisSqlTarget
public interface SettleWayDao extends BaseDao<SettleWay, String> {
	SettleWay selectById(String code);

	SettleWay selectByName(String name);

	int insert(SettleWay settleWay);

	int update(SettleWay settleWay);

	int deleteById(String code);

	/**
	 * 返回所有结算方式
	 * 
	 * @author koqiui
	 * @date 2015年12月31日 上午1:34:22
	 * 
	 * @param includeDisabled
	 *            是否包含禁用章台的
	 * @return
	 */
	List<SettleWay> selectAll(boolean includeDisabled);
}