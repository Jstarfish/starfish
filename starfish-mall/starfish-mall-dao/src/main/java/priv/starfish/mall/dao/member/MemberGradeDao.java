package priv.starfish.mall.dao.member;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.member.entity.MemberGrade;

@IBatisSqlTarget
public interface MemberGradeDao extends BaseDao<MemberGrade, Integer> {
	MemberGrade selectById(Integer id);

	MemberGrade selectByGrade(Integer grade);

	int insert(MemberGrade memberGrade);

	int update(MemberGrade memberGrade);

	int deleteById(Integer id);

	/**
	 * 分页查询会员等级列表
	 * 
	 * @author guoyn
	 * @date 2015年5月28日 下午5:27:46
	 * 
	 * @param paginatedFilter like name, = grade, >= lowerPoint, <upperPoint
	 * @return PaginatedList<MemberGrade>
	 */
	PaginatedList<MemberGrade> selectMemberGradesByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 查询会员等级列表
	 * 
	 * @author guoyn
	 * @date 2015年6月2日 下午6:15:39
	 * 
	 * @return List<MemberGrade>
	 */
	List<MemberGrade> selectMemberGrades();

	/**
	 * 根据积分值得到积分等级
	 * 
	 * @author guoyn
	 * @date 2015年6月5日 下午6:10:46
	 * 
	 * @param point
	 * @return int
	 */
	int selectGradeByPoint(Integer point);

}