package priv.starfish.mall.dao.member.impl;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.member.MemberDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.member.entity.Member;

@Component("memberDao")
public class MemberDaoImpl extends BaseDaoImpl<Member, Integer> implements MemberDao {
	@Override
	public Member selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(Member member) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, member);
	}

	@Override
	public int update(Member member) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, member);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<Member> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		
		MapContext filterItems = paginatedFilter.getFilterItems();
		//
		String nickName = filterItems.getTypedValue("nickName", String.class);
		filterItems.remove("nickName");
		if (StrUtil.hasText(nickName)) {
			nickName = SqlBuilder.likeStrVal(nickName);
			filterItems.put("nickName", nickName);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
 		PageList<Member> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<Member> selectByFilter(MapContext filterItems) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		if(filterItems != null){
			String nickName = filterItems.getTypedValue("nickName", String.class);
			filterItems.remove("nickName");
			if (StrUtil.hasText(nickName)) {
				nickName = SqlBuilder.likeStrVal(nickName);
				filterItems.put("nickName", nickName);
			}
		}
		return this.getSqlSession().selectList(sqlId, filterItems);
	}

	@Override
	public List<Integer> selectIdsByFilter(MapContext filter) {
		String sqlId = this.getNamedSqlId("selectIdsByFilter");
		if(filter != null){
			Object nickName = filter.get("nickName");
			if(nickName != null){
				nickName = SqlBuilder.likeStrVal(nickName.toString());
				filter.put("nickName", nickName);
			}
		}
		return this.getSqlSession().selectList(sqlId, filter);
	}

	@Override
	public List<Member> selectByIds(List<Integer> memberIds) {
		String sqlId = this.getNamedSqlId("selectByIds");
		//
		return this.getSqlSession().selectList(sqlId, memberIds);
	}

	@Override
	public int updateRankById(Integer rank, Integer memberId) {
		String sqlId = this.getNamedSqlId("updateRankById");
		Map<String, Object> params = this.newParamMap();
		params.put("rank", rank);
		params.put("memberId", memberId);
		//
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public int updatePointById(Integer point, Integer memberId) {
		String sqlId = this.getNamedSqlId("updatePointById");
		Map<String, Object> params = this.newParamMap();
		params.put("point", point);
		params.put("memberId", memberId);
		//
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public int updateGradeById(Integer grade, Integer memberId) {
		String sqlId = this.getNamedSqlId("updateGradeById");
		Map<String, Object> params = this.newParamMap();
		params.put("grade", grade);
		params.put("memberId", memberId);
		//
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public int updateDisabledById(Boolean disabled, Integer memberId) {
		String sqlId = this.getNamedSqlId("updateDisabledById");
		Map<String, Object> params = this.newParamMap();
		params.put("disabled", disabled);
		params.put("memberId", memberId);
		//
		return this.getSqlSession().update(sqlId, params);
	}

}