package priv.starfish.mall.dao.notify;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.notify.entity.SmsApi;

@IBatisSqlTarget
public interface SmsApiDao extends BaseDao<SmsApi, Integer> {

	SmsApi selectById(Integer id);

	SmsApi selectByName(String name);

	int insert(SmsApi smsApi);

	int update(SmsApi smsApi);

	int deleteById(Integer id);

	/**
	 * 分页获取短信接口信息
	 * 
	 * @author guoyn
	 * @date 2015年5月23日 上午10:58:13
	 * 
	 * @param paginatedFilter
	 * 	like name, == status
	 * @return PaginatedList<SmsApi>
	 */
	PaginatedList<SmsApi> selectSmsApisByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 获取短信接口信息集合
	 * 
	 * @author guoyn
	 * @date 2015年5月25日 下午7:25:50
	 * 
	 * @return List<SmsApi>
	 */
	List<SmsApi> selectSmsApis();
}
