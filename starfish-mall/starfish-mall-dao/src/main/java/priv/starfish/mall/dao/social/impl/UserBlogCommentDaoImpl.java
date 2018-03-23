package priv.starfish.mall.dao.social.impl;


import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.social.UserBlogCommentDao;
import priv.starfish.mall.social.entity.UserBlogComment;

@Component("userBlogCommentDao")
public class UserBlogCommentDaoImpl extends BaseDaoImpl<UserBlogComment, Integer> implements
        UserBlogCommentDao {
	
	@Override
	public int insert(UserBlogComment userBlogComment) {
		String sqlId = this.getNamedSqlId("insert");
		if (userBlogComment.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(UserBlogComment.class) + 1;
			userBlogComment.setSeqNo(seqNo);
		}
		return this.getSqlSession().insert(sqlId, userBlogComment);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		return this.getSqlSession().delete(sqlId, id);
	}
	
	@Override
	public int update(UserBlogComment userBlogComment) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, userBlogComment);
	}
	
	@Override
	public PaginatedList<UserBlogComment> selectByFilter(PaginatedFilter paginatedFilter){
		String sqlId = this.getNamedSqlId("selectByFilter");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<UserBlogComment> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public UserBlogComment selectById(Integer id){
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int selectCountByBlogId(Integer blogId) {
		String sqlId = this.getNamedSqlId("selectCountByBlogId");
		return this.getSqlSession().selectOne(sqlId, blogId);
	}
	
}