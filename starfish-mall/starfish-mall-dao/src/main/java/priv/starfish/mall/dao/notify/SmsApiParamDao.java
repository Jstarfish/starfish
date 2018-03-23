package priv.starfish.mall.dao.notify;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.notify.entity.SmsApiParam;

@IBatisSqlTarget
public interface SmsApiParamDao extends BaseDao<SmsApiParam, Integer> {

	SmsApiParam selectById(Integer id);

	SmsApiParam selectByApiIdAndNameAndIoFlag(Integer apiId, String name, Integer ioFlag);

	int insert(SmsApiParam smsApiParam);

	int update(SmsApiParam smsApiParam);

	int deleteById(Integer id);

	/**
	 * 根据短信接口id获取短信接口参数列表
	 * 
	 * @author guoyn
	 * @date 2015年5月23日 上午11:46:31
	 * 
	 * @param id
	 * @return List<SmsApi>
	 */
	List<SmsApiParam> selectByApiId(Integer id);

	/**
	 * 根据短信接口id删除该接口的所有参数
	 * 
	 * @author guoyn
	 * @date 2015年5月27日 下午3:35:48
	 * 
	 * @param id
	 * @return int
	 */
	int deleteByApiId(Integer id);

	/**
	 * 根据apiId查找短信接口参数Id列表
	 * 
	 * @author guoyn
	 * @date 2015年5月28日 上午9:48:07
	 * 
	 * @param apiId
	 * @return List<Integer>
	 */
	List<Integer> selectParamIdsByApiId(Integer apiId);

	/**
	 * 批量插入物流接口参数
	 * 
	 * @author guoyn
	 * @date 2015年5月22日 下午5:51:03
	 * 
	 * @param params
	 * @return int
	 */
	int batchInsert(List<SmsApiParam> params);

	/**
	 * 批量删除参数
	 * 
	 * @author guoyn
	 * @date 2015年5月28日 上午10:32:13
	 * 
	 * @param paramIds
	 * @return int
	 */
	int batchDelete(List<Integer> paramIds);

}
