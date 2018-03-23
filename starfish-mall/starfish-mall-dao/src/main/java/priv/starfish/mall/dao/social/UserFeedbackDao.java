package priv.starfish.mall.dao.social;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.social.entity.UserFeedback;

/**
 * 用户反馈
 * 
 * @author 邓华锋
 * @date 2016年01月12日 11:14:22
 *
 */@IBatisSqlTarget
public interface UserFeedbackDao extends BaseDao<UserFeedback, Long> {
	
	int insert(UserFeedback userFeedback);
	
	int deleteById(Long id);
	
	int update(UserFeedback userFeedback);
	
	UserFeedback selectById(Long id);
	
	/**
	 * 分页
	 * @author 邓华锋
	 * @date  2016年01月12日 11:14:22
	 * 
	 * @param paginatedFilter 
	 * 						like  userName,like  appType,like  subject,like  content,like  handleMemo
	 * @return
	 */
	PaginatedList<UserFeedback> selectByFilter(PaginatedFilter paginatedFilter);
	
}