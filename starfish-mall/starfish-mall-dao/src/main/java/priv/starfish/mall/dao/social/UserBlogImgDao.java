package priv.starfish.mall.dao.social;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.social.entity.UserBlogImg;

/**
 * 博客图片信息
 * @author 邓华锋
 * @date 2015年10月15日 13:58:49
 *
 */@IBatisSqlTarget
public interface UserBlogImgDao extends BaseDao<UserBlogImg, Long> {
	
	int insert(UserBlogImg userBlogImg);
	
	int deleteById(Long id);
	
	int update(UserBlogImg userBlogImg);
	
	UserBlogImg selectById(Long id);
	
	UserBlogImg selectByBlogId(Integer blogId);
	
	/**
	 * 博客图片信息分页
	 * @author 邓华锋
	 * @date  2015年10月15日 01:58:49
	 * 
	 * @param paginatedFilter 
	 * 						=blogId
	 * @return
	 */
	PaginatedList<UserBlogImg> selectByFilter(PaginatedFilter paginatedFilter);
	
}