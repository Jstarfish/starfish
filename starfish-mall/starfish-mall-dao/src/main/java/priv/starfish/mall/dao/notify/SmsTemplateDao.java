package priv.starfish.mall.dao.notify;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.notify.entity.SmsTemplate;

/**
 * 短信模板dao接口
 * 
 * @author 毛智东
 * @date 2015年5月21日 下午6:54:23
 *
 */
@IBatisSqlTarget
public interface SmsTemplateDao extends BaseDao<SmsTemplate, Integer> {
	SmsTemplate selectById(Integer id);

	SmsTemplate selectByCode(String code);

	SmsTemplate selectByName(String name);

	int insert(SmsTemplate smsTemplate);

	int update(SmsTemplate smsTemplate);

	int deleteById(Integer id);

	/**
	 * 分页查询
	 * 
	 * @author 毛智东
	 * @date 2015年5月22日 上午10:56:54
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SmsTemplate> selectList(PaginatedFilter paginatedFilter);

	/**
	 * 查询所有短信模板
	 * 
	 * @author 毛智东
	 * @date 2015年6月16日 下午12:03:08
	 * 
	 * @return
	 */
	List<SmsTemplate> selectAll();
}