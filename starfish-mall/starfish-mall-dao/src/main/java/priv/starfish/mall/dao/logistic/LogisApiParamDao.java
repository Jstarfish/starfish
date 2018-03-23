package priv.starfish.mall.dao.logistic;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.logistic.entity.LogisApiParam;

@IBatisSqlTarget
public interface LogisApiParamDao extends BaseDao<LogisApiParam, Integer> {

	LogisApiParam selectById(Integer id);

	LogisApiParam selectByApiIdAndNameAndIoFlag(Integer apiId, String name, Integer ioFlag);

	int insert(LogisApiParam logisApiParam);

	int update(LogisApiParam logisApiParam);

	int deleteById(Integer id);

	/**
	 * 批量插入物流接口参数
	 * 
	 * @author guoyn
	 * @date 2015年5月21日 下午1:30:59
	 * 
	 * @param params
	 *            物流接口参数列表
	 * @return 执行记录数
	 */
	int batchInsertLogisApiParams(List<LogisApiParam> params);

	/**
	 * 根据ApiId获取LogisApiParam集合
	 * 
	 * @author guoyn
	 * @date 2015年5月21日 下午3:14:22
	 * 
	 * @param logisApiId
	 * @return List<LogisApiParam>
	 */
	List<LogisApiParam> selectByApiId(Integer logisApiId);

	/**
	 * 根据api的id删除物流服务商参数集
	 * 
	 * @author guoyn
	 * @date 2015年7月31日 上午9:56:05
	 * 
	 * @param apiId
	 * @return int
	 */
	int deleteByApiId(Integer apiId);

}
