package priv.starfish.mall.dao.social.impl;


import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.social.UserBlogImgDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.social.entity.UserBlogImg;

@Component("userBlogImgDao")
public class UserBlogImgDaoImpl extends BaseDaoImpl<UserBlogImg, Long> implements UserBlogImgDao {

	
	@Override
	public int insert(UserBlogImg userBlogImg) {
		String sqlId = this.getNamedSqlId("insert");
		return this.getSqlSession().insert(sqlId, userBlogImg);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		return this.getSqlSession().delete(sqlId, id);
	}
	
	@Override
	public int update(UserBlogImg userBlogImg) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, userBlogImg);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<UserBlogImg> selectByFilter(PaginatedFilter paginatedFilter){
		String sqlId = this.getNamedSqlId("selectByFilter");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<UserBlogImg> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public UserBlogImg selectById(Long id){
		String sqlId = this.getNamedSqlId("selectById");
		UserBlogImg userBlogImg=this.getSqlSession().selectOne(sqlId, id);
		return userBlogImg;
	}
	
	@Override
	public UserBlogImg selectByBlogId(Integer blogId) {
		String sqlId = this.getNamedSqlId("selectByBlogId");
		UserBlogImg userBlogImg=this.getSqlSession().selectOne(sqlId, blogId);
		return userBlogImg;
	}
	
}