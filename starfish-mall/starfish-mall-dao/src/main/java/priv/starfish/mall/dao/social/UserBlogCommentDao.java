package priv.starfish.mall.dao.social;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.social.entity.UserBlogComment;

/**
 * 博客评论
 * @author 邓华锋
 * @date 2015年10月15日 14:56:59
 *
 */@IBatisSqlTarget
public interface UserBlogCommentDao extends BaseDao<UserBlogComment, Integer> {
	
	int insert(UserBlogComment userBlogComment);
	
	int deleteById(Integer id);
	
	int update(UserBlogComment userBlogComment);
	
	UserBlogComment selectById(Integer id);
	
	/**
	 * 博客评论分页
	 * @author 邓华锋
	 * @date  2015年10月15日 02:56:59
	 * 
	 * @param paginatedFilter 
	 * 						=blogId
	 * @return
	 */
	PaginatedList<UserBlogComment> selectByFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 获取博客评论数
	 * 
	 * @author 邓华锋
	 * @date 2015年10月22日 上午10:30:08
	 * 
	 * @param blogId
	 * @return
	 */
	int selectCountByBlogId(Integer blogId);
}