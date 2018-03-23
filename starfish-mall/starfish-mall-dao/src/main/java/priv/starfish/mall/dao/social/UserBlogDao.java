package priv.starfish.mall.dao.social;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.social.entity.UserBlog;

/**
 * 用户博客
 * @author 邓华锋
 * @date 2015年10月13日 21:49:35
 *
 */@IBatisSqlTarget
public interface UserBlogDao extends BaseDao<UserBlog, Integer> {
	
	int insert(UserBlog userBlog);
	
	int deleteById(Integer id);
	
	int update(UserBlog userBlog);
	
	/**
	 * 更新博客审核
	 * 
	 * @author 邓华锋
	 * @date 2016年1月11日 下午2:13:10
	 * 
	 * @param userBlog
	 * @return
	 */
	int updateAuditStatus(UserBlog userBlog);
	
	/**
	 * 博客索引更新时间
	 * 
	 * @author 邓华锋
	 * @date 2015年10月22日 上午9:23:04
	 * 
	 * @param userBlog
	 * @return
	 */
	int updateIndexTime(UserBlog userBlog);
	
	
	UserBlog selectById(Integer id);
	
	/**
	 * 根据博客ID和用户ID更新用户的博客
	 * 
	 * @author 邓华锋
	 * @date 2015年10月13日 12:49:35
	 * 
	 * @param id
	 * @param userId
	 * @return
	 */
	UserBlog selectByIdAndUserId(Integer id, Integer userId);
	
	/**
	 * 用户博客分页
	 * @author 邓华锋
	 * @date  2015年10月13日 10:49:35
	 * 
	 * @param paginatedFilter 
	 * 						=userId,title  like '%keyword%' OR content  like '%keyword%',=userId
	 * @return
	 */
	PaginatedList<UserBlog> selectByFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 获取用户草稿博客数
	 * 
	 * @author 邓华锋
	 * @date 2015年10月27日 下午7:25:04
	 * 
	 * @return
	 */
	int selectBlogDraftCountByUserId(Integer userId);
	
	
	/**
	 * 获取用户已发表博客数
	 * 
	 * @author 李江
	 * @date 2016年2月22日 下午5:49:02
	 * 
	 * @param userId
	 * @return
	 */
	int getBlogDeliverCountByUserId(Integer userId);
	
}