package priv.starfish.mall.dao.comn;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.entity.BizParam;

@IBatisSqlTarget
public interface BizParamDao extends BaseDao<BizParam, String> {

	/**
	 * 根据主键获取业务参数
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:22:10
	 * 
	 * @param code
	 *            主键
	 * @return 返回的业务参数
	 */
	BizParam selectById(String code);

	/**
	 * 根据名称获取业务参数
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:22:10
	 * 
	 * @param name
	 *            业务参数名称
	 * @return 返回的业务参数
	 */
	BizParam selectByName(String name);

	/**
	 * 获取参数列表
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:22:16
	 * 
	 * @return 返回参数列表
	 */
	List<BizParam> selectAll();

	/**
	 * 新增一条业务参数
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:22:16
	 * 
	 * @param bizParam
	 *            要新增的业务参数
	 */
	int insert(BizParam bizParam);

	/**
	 * 更新一条业务参数
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:22:16
	 * 
	 * @param bizParam
	 *            要更新的业务参数
	 */
	int update(BizParam bizParam);

	/**
	 * 根据主键删除一条业务参数
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:22:16
	 * 
	 * @param code
	 *            主键
	 */
	int deleteById(String code);
}