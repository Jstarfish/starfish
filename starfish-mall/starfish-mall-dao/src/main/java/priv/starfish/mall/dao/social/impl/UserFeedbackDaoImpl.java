package priv.starfish.mall.dao.social.impl;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.social.UserFeedbackDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.social.entity.UserFeedback;

@Component("userFeedbackDao")
public class UserFeedbackDaoImpl extends BaseDaoImpl<UserFeedback, Long> implements
        UserFeedbackDao {
	
	@Override
	public int insert(UserFeedback userFeedback) {
		String sqlId = this.getNamedSqlId("insert");
		return this.getSqlSession().insert(sqlId, userFeedback);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		return this.getSqlSession().delete(sqlId, id);
	}
	
	@Override
	public int update(UserFeedback userFeedback) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, userFeedback);
	}
	
	@Override
	public PaginatedList<UserFeedback> selectByFilter(PaginatedFilter paginatedFilter){
		String sqlId = this.getNamedSqlId("selectByFilter");
		MapContext filter = paginatedFilter.getFilterItems();
		String  userName = filter.getTypedValue("userName", String.class);
		filter.remove( userName);
		if (StrUtil.hasText( userName)) {
			userName = SqlBuilder.likeStrVal(userName);
			filter.put("userName",  userName);
		}
		String  appType = filter.getTypedValue("appType", String.class);
		filter.remove( appType);
		if (StrUtil.hasText( appType)) {
			appType = SqlBuilder.likeStrVal(appType);
			filter.put("appType",  appType);
		}
		String  subject = filter.getTypedValue("subject", String.class);
		filter.remove( subject);
		if (StrUtil.hasText( subject)) {
			subject = SqlBuilder.likeStrVal(subject);
			filter.put("subject",  subject);
		}
		String  content = filter.getTypedValue("content", String.class);
		filter.remove( content);
		if (StrUtil.hasText( content)) {
			content = SqlBuilder.likeStrVal(content);
			filter.put("content",  content);
		}
		String  keywords = paginatedFilter.getKeywords();
		filter.remove( keywords);
		if (StrUtil.hasText( keywords)) {
			keywords = SqlBuilder.likeStrVal(keywords);
			filter.put("content",  keywords);
		}
		String  handleMemo = filter.getTypedValue("handleMemo", String.class);
		filter.remove( handleMemo);
		if (StrUtil.hasText( handleMemo)) {
			handleMemo = SqlBuilder.likeStrVal(handleMemo);
			filter.put("handleMemo",  handleMemo);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<UserFeedback> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public UserFeedback selectById(Long id){
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, id);
	}
	
}