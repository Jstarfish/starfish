package priv.starfish.mall.dao.member;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.member.entity.Member;

@IBatisSqlTarget
public interface MemberDao extends BaseDao<Member, Integer> {
	Member selectById(Integer id);

	int insert(Member member);

	int update(Member member);

	int deleteById(Integer id);

	/**
	 * 分页获取会员列表
	 * 
	 * @author guoyn
	 * @date 2015年6月2日 下午2:42:56
	 * 
	 * @param paginatedFilter
	 * @return PaginatedList<Member>
	 */
	PaginatedList<Member> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据查询条件获取会员列表
	 * @author guoyn
	 * @date 2015年6月29日 下午10:51:39
	 * 
	 * @param filter
	 * @return List<Member>
	 */
	List<Member> selectByFilter(MapContext filterItems);

	/**
	 * 根据查询条件获取会员id列表 
	 * @author guoyn
	 * @date 2015年6月30日 上午9:29:34
	 * 
	 * @param filter =会员id，like用户名， =手机号， =会员等级
	 * @return List<Integer>
	 */
	List<Integer> selectIdsByFilter(MapContext filter);

	/**
	 * 根据会员Id列表查询会员集合
	 * @author guoyn
	 * @date 2015年8月17日 下午3:03:31
	 * 
	 * @param memberIds
	 * @return List<Member>
	 */
	List<Member> selectByIds(List<Integer> memberIds);

	/**
	 * 更新会员身份
	 * 
	 * @author guoyn
	 * @date 2015年12月16日 下午2:40:54
	 * 
	 * @param rank
	 * @param memberId
	 * @return int
	 */
	int updateRankById(Integer rank, Integer memberId);

	/**
	 * 更新会员积分
	 * 
	 * @author guoyn
	 * @date 2015年12月16日 下午3:42:54
	 * 
	 * @param point
	 * @param memberId
	 * @return int
	 */
	int updatePointById(Integer point, Integer memberId);

	/**
	 * 更新会员等级
	 * 
	 * @author guoyn
	 * @date 2015年12月16日 下午4:09:44
	 * 
	 * @param grade
	 * @param memberId
	 * @return int
	 */
	int updateGradeById(Integer grade, Integer memberId);

	/**
	 * 更新会员启用、禁用状态
	 * 
	 * @author guoyn
	 * @date 2015年12月25日 上午11:51:16
	 * 
	 * @param disabled
	 * @param userId
	 * @return int
	 */
	int updateDisabledById(Boolean disabled, Integer userId);

}
