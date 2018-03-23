package priv.starfish.mall.dao.member.impl;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.member.MemberGradeDao;
import priv.starfish.mall.member.entity.MemberGrade;

@Component("memberGradeDao")
public class MemberGradeDaoImpl extends BaseDaoImpl<MemberGrade, Integer> implements MemberGradeDao {
	@Override
	public MemberGrade selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public MemberGrade selectByGrade(Integer grade) {
		String sqlId = this.getNamedSqlId("selectByGrade");
		//
		return this.getSqlSession().selectOne(sqlId, grade);
	}

	@Override
	public int insert(MemberGrade memberGrade) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, memberGrade);
	}

	@Override
	public int update(MemberGrade memberGrade) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, memberGrade);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<MemberGrade> selectMemberGradesByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectMemberGradesByFilter");
		MapContext filter = paginatedFilter.getFilterItems();
		String name = filter.getTypedValue("name", String.class);
		if(StrUtil.hasText(name)){
			name = SqlBuilder.likeStrVal(name);
			filter.put("name", name);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<MemberGrade> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<MemberGrade> selectMemberGrades() {
		String sqlId = this.getNamedSqlId("selectMemberGrades");
		//
		return this.getSqlSession().selectList(sqlId);
	}

	@Override
	public int selectGradeByPoint(Integer point) {
		String sqlId = this.getNamedSqlId("selectByPoint");
		//
		Integer grade = this.getSqlSession().selectOne(sqlId, point);
		if(grade == null){
			sqlId = this.getNamedSqlId("selectMaxGrade");
			grade = this.getSqlSession().selectOne(sqlId);
		}
		return grade;
	}
}
