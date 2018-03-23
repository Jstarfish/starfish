package priv.starfish.mall.dao.social.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.social.UserBlogDao;
import priv.starfish.mall.social.entity.UserBlog;

@Component("userBlogDao")
public class UserBlogDaoImpl extends BaseDaoImpl<UserBlog, Integer> implements UserBlogDao {
	
	@Override
	public int insert(UserBlog userBlog) {
		String sqlId = this.getNamedSqlId("insert");
		return this.getSqlSession().insert(sqlId, userBlog);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int update(UserBlog userBlog) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, userBlog);
	}
	
	@Override
	public int updateAuditStatus(UserBlog userBlog) {
		String sqlId = this.getNamedSqlId("updateAuditStatus");
		return this.getSqlSession().update(sqlId, userBlog);
	}

	@Override
	public int updateIndexTime(UserBlog userBlog) {
		String sqlId = this.getNamedSqlId("updateIndexTime");
		return this.getSqlSession().update(sqlId, userBlog);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<UserBlog> selectByFilter(PaginatedFilter paginatedFilter){
		String sqlId = this.getNamedSqlId("selectByFilter");
		if(paginatedFilter.getSortItems()==null || paginatedFilter.getSortItems().isEmpty()){
			LinkedHashMap<String, String> sortItems=new LinkedHashMap<String, String>();
			sortItems.put("createTime", "desc");
			paginatedFilter.setSortItems(sortItems);//默认按创建时间排序
		}
		MapContext filter = paginatedFilter.getFilterItems();
		if(filter!=null){
			String  keywords = filter.getTypedValue("keywords", String.class);
			filter.remove( keywords);
			if (StrUtil.hasText( keywords)) {
				keywords = SqlBuilder.likeStrVal(keywords);
				filter.put("keywords",  keywords);
			}
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination(),paginatedFilter.getSortItems());
		PageList<UserBlog> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public UserBlog selectById(Integer id){
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, id);
	}
	
	@Override
	public UserBlog selectByIdAndUserId(Integer id, Integer userId) {
		Map<String,Integer> map=new HashMap<String,Integer>();
		map.put("id", id);
		map.put("userId", userId);
		String sqlId = this.getNamedSqlId("selectByIdAndUserId");
		return this.getSqlSession().selectOne(sqlId, map);
	}

	@Override
	public int selectBlogDraftCountByUserId(Integer userId) {
		String sqlId = this.getNamedSqlId("selectBlogDraftCountByUserId");
		return this.getSqlSession().selectOne(sqlId, userId);
	}

	@Override
	public int getBlogDeliverCountByUserId(Integer userId) {
		String sqlId = this.getNamedSqlId("getBlogDeliverCountByUserId");
		return this.getSqlSession().selectOne(sqlId, userId);
	}
	
}