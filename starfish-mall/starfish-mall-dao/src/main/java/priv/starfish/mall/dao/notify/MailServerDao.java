package priv.starfish.mall.dao.notify;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.notify.entity.MailServer;

/**
 * 邮件服务器dao接口
 * 
 * @author 毛智东
 * @date 2015年5月18日 下午5:09:40
 *
 */
@IBatisSqlTarget
public interface MailServerDao extends BaseDao<MailServer, Integer> {
	MailServer selectById(Integer id);

	MailServer selectByName(String name);

	int insert(MailServer mailServer);

	int update(MailServer mailServer);

	int deleteById(Integer id);

	/**
	 * 分页查询
	 * 
	 * @author 毛智东
	 * @date 2015年5月18日 下午8:23:52
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<MailServer> selectList(PaginatedFilter paginatedFilter);

	/**
	 * 把所有的服务器状态设为禁用
	 * 
	 * @author 毛智东
	 * @date 2015年5月20日 下午3:59:38
	 * 
	 * @return
	 */
	void updateAllUnabled();

	/**
	 * 查询可用的邮件服务器
	 * 
	 * @author 廖晓远
	 * @date 2015年6月16日 下午1:53:13
	 * 
	 * @return
	 */
	MailServer selectEnabled();
}