package priv.starfish.mall.dao.mall;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.mall.entity.Mall;

@IBatisSqlTarget
public interface MallDao extends BaseDao<Mall, Integer> {

	/**
	 * 根据商城主键获取商城基本信息
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:57:28
	 * 
	 * @param id
	 *            商城主键
	 * @return 返回商城基本信息
	 */
	Mall selectById(Integer id);

	/**
	 * 获取唯一的商城
	 * 
	 * @author koqiui
	 * @date 2015年11月11日 下午5:04:30
	 * 
	 * @return
	 */
	Mall selectTheOne();

	/**
	 * 根据商城code获取商城基本信息
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:57:28
	 * 
	 * @param code
	 *            商城code
	 * @return 返回商城基本信息
	 */
	Mall selectByCode(String code);

	/**
	 * 新增一条商城信息
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:57:28
	 * 
	 * @param mall
	 *            要新增的商城信息
	 */
	int insert(Mall mall);

	/**
	 * 更新一条商城信息
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:57:28
	 * 
	 * @param mall
	 *            要更新的商城信息
	 */
	int update(Mall mall);

	/**
	 * 根据商城主键删除商城信息
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:57:28
	 * 
	 * @param id
	 *            商城主键
	 */
	int deleteById(Integer id);

}
