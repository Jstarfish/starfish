package priv.starfish.mall.dao.notify;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.notify.entity.MailTemplate;

/**
 * 短信模板dao接口
 * 
 * @author 毛智东
 * @date 2015年5月21日 下午7:00:07
 *
 */
@IBatisSqlTarget
public interface MailTemplateDao extends BaseDao<MailTemplate, Integer> {
	MailTemplate selectById(Integer id);

	MailTemplate selectByCode(String code);

	MailTemplate selectByName(String name);

	int insert(MailTemplate mailTemplate);

	int update(MailTemplate mailTemplate);

	int deleteById(Integer id);

	/**
	 * 分页查询
	 * 
	 * @author 毛智东
	 * @date 2015年5月22日 上午10:56:00
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<MailTemplate> selectList(PaginatedFilter paginatedFilter);

	/**
	 * 查询所有邮件模板
	 * 
	 * @author 毛智东
	 * @date 2015年6月16日 上午11:59:40
	 * 
	 * @return
	 */
	List<MailTemplate> selectAll();
}