package priv.starfish.mall.service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.social.entity.UserBlog;
import priv.starfish.mall.social.entity.UserBlogComment;
import priv.starfish.mall.social.entity.UserFeedback;

public interface SocialService extends BaseService {
	// ---------------------------------用户博客-----------------------------------
	boolean saveUserBlog(UserBlog userBlog);

	boolean updateUserBlog(UserBlog userBlog);
	
	/**
	 * 更新博文审核状态
	 * 
	 * @author 邓华锋
	 * @date 2016年1月11日 下午2:17:40
	 * 
	 * @param userBlog
	 * @return
	 */
	boolean updateUserBlogAuditStatus(UserBlog userBlog);
	
	boolean deleteUserBlogById(Integer id);

	boolean deleteUserBlogByIdAndUserId(Integer blogId, Integer userId);

	UserBlog getUserBlogById(Integer id);

	/**
	 * 根据博客ID和用户ID获取博客信息
	 * 
	 * @author 邓华锋
	 * @date 2015年10月22日 下午2:41:43
	 * 
	 * @param blogId
	 * @param userId
	 * @return
	 */
	UserBlog getUserBlogByIdAndUserId(Integer blogId, Integer userId);

	/**
	 * 用户中心的博客列表分页 published=true 表示发布 published=false表示草稿
	 * 
	 * @author 邓华锋
	 * @date 2015年10月13日 22:39:57
	 * 
	 * @param paginatedFilter
	 *            title like '%keyword%' or content like '%keyword%',= published
	 * @param userId
	 *            用户ID
	 * @return
	 */
	PaginatedList<UserBlog> getUserBlogsByFilterAndUserId(PaginatedFilter paginatedFilter, Integer userId);

	/**
	 * 获取车友分享博客,即已经发布的博客分页列表
	 * 
	 * @author 邓华锋
	 * @date 2015年10月13日 下午5:06:26
	 * 
	 * @param paginatedFilter
	 *            title like '%keywords%' or content like '%keywords%,published=true
	 * @return
	 */
	PaginatedList<UserBlog> getShareBlogsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 获取指定用户的博客草稿数
	 * 
	 * @author 邓华锋
	 * @date 2015年10月27日 下午7:23:12
	 * 
	 * @param userId
	 * @return
	 */
	int getBlogDraftCountByUserId(Integer userId);
	
	/**
	 * 获取指定用户的博客发表数
	 * 
	 * @author 李江
	 * @date 2016年2月22日 下午5:51:52
	 * 
	 * @param userId
	 * @return
	 */
	int getBlogDeliverCountByUserId(Integer userId);
	
	/**
	 * 草稿发表博文
	 * 
	 * @author 李江
	 * @date 2016年2月22日 下午5:51:52
	 * 
	 * @param id
	 * @return
	 */
	boolean userBlogToDeliver(Integer id, Integer userId);
	
	/**
	 * 假删除用户博文
	 * 
	 * @author 李江
	 * @date 2016年2月22日 下午5:51:52
	 * 
	 * @param id
	 * @return
	 */
	boolean deleteFalseUserBlog(Integer blogId, Integer userId);

	// -----------------------------------用户评论---------------------------------
	boolean saveUserBlogComment(UserBlogComment userBlogComment);

	boolean updateUserBlogComment(UserBlogComment userBlogComment);

	boolean deleteUserBlogCommentById(Integer id);

	UserBlogComment getUserBlogCommentById(Integer id);

	/**
	 * 博客评论分页
	 * 
	 * @author 邓华锋
	 * @date 2015年10月19日 01:58:45
	 * 
	 * @param paginatedFilter
	 *            =blogId
	 * @return
	 */
	PaginatedList<UserBlogComment> getUserBlogCommentsByFilter(PaginatedFilter paginatedFilter);
	
	//-------------------------------------用户反馈----------------------------------
	boolean saveUserFeedback(UserFeedback userFeedback);

	boolean updateUserFeedback(UserFeedback userFeedback);
	
	boolean deleteUserFeedbackById(Long id);
	
	UserFeedback getUserFeedbackById(Long id);
	
	/**
	 * 分页
	 * @author 邓华锋
	 * @date 2016年01月12日 11:14:22
	 * 
	 * @param paginatedFilter 
	 * 						like  userName,like  appType,like  subject,like  content,like  handleMemo
	 * @return
	 */
	PaginatedList<UserFeedback> getUserFeedbacksByFilter(PaginatedFilter paginatedFilter);
}
