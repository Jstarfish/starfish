package priv.starfish.mall.service;

import java.math.BigDecimal;
import java.util.List;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.member.entity.Member;
import priv.starfish.mall.member.entity.MemberGrade;
import priv.starfish.mall.member.entity.MemberPointRule;

public interface MemberService extends BaseService {

	/**
	 * 获取会员等级列表
	 * 
	 * @author guoyn
	 * @date 2015年5月28日 下午5:24:09
	 * 
	 * @param paginatedFilter like name,
	 * @return PaginatedList<MemberGrade>
	 */
	PaginatedList<MemberGrade> getMemberGradesByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 保存会员等级信息
	 * 
	 * @author guoyn
	 * @date 2015年5月29日 上午9:34:49
	 * 
	 * @param memberGrade
	 * @return boolean
	 */
	boolean saveMemberGrade(MemberGrade memberGrade);

	/**
	 * 更新会员等级信息
	 * 
	 * @author guoyn
	 * @date 2015年5月29日 上午9:38:52
	 * 
	 * @param memberGrade
	 * @return boolean
	 */
	boolean updateMemberGrade(MemberGrade memberGrade);

	/**
	 * 删除会员等级信息
	 * 
	 * @author guoyn
	 * @date 2015年5月29日 上午9:40:33
	 * 
	 * @param memberGrade
	 * @return boolean
	 */
	boolean deleteMemberGradeById(Integer id);

	/**
	 * 保存会员等级信息列表
	 * 
	 * @author guoyn
	 * @date 2015年5月29日 下午5:25:59
	 * 
	 * @param memberGradeList
	 * @return
	 */
	boolean updateMemberGrades(List<MemberGrade> memberGradeList);

	/**
	 * 保存会员积分规则信息
	 * 
	 * @author guoyn
	 * @date 2015年6月1日 下午2:48:12
	 * 
	 * @param rule
	 * @return boolean
	 */
	boolean saveMemberPointRule(MemberPointRule rule);

	/**
	 * 更新会员积分规则
	 * 
	 * @author guoyn
	 * @date 2015年6月1日 下午3:38:51
	 * 
	 * @param ruleList
	 * @return boolean
	 */
	boolean saveMemberPointRules(List<MemberPointRule> ruleList);

	/**
	 * 获取所有会员积分规则信息
	 * 
	 * @author guoyn
	 * @date 2015年6月1日 下午4:02:17
	 * 
	 * @return List<MemberPointRule>
	 */
	List<MemberPointRule> getMemberPointRules();

	/**
	 * 分页获取会员列表
	 * 
	 * @author guoyn
	 * @date 2015年6月2日 下午2:09:02
	 * 
	 * @param paginatedFilter =会员id，like 用户名，=会员等级，=手机号
	 * @return PaginatedList<Member>
	 */
	PaginatedList<Member> getMembersByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据id获取会员信息
	 * 
	 * @author guoyn
	 * @date 2015年6月2日 下午5:02:55
	 * 
	 * @param id
	 * @return
	 */
	Member getMemberById(Integer id);

	/**
	 * 保存用户信息
	 * 
	 * @author guoyn
	 * @date 2015年6月3日 下午2:56:29
	 * 
	 * @param member
	 * @return boolean
	 */
	boolean saveMember(Member member);

	/**
	 * 更新会员信息
	 * 
	 * @author guoyn
	 * @date 2015年6月3日 下午6:28:05
	 * 
	 * @param member
	 * @return boolean
	 */
	boolean updateMember(Member member);

	/**
	 * 根据会员Id启用、禁用该会员
	 * 
	 * @author guoyn
	 * @date 2015年6月4日 上午10:33:31
	 * 
	 * @param memberId
	 * @param disabled
	 *            false表示启用，true表示禁用
	 * @return boolean
	 */
	boolean updateMemberDisabledById(Integer memberId, Boolean disabled);

	/**
	 * 批量启用、禁用会员列表
	 * 
	 * @author guoyn
	 * @date 2015年6月4日 下午2:01:25
	 * 
	 * @param ids
	 * @param disabled
	 * @return boolean
	 */
	boolean batchUpdateMemberStatus(List<Integer> ids, boolean disabled);

	/**
	 * 查询所有会员信息
	 * 
	 * @author guoyn
	 * @date 2015年6月9日 下午3:28:08
	 * 
	 * @return List<Member>
	 */
	List<Member> getMembersByFilterItems(MapContext filterItems);

	/**
	 * 分页查询店铺会员列表
	 * 
	 * @author guoyn
	 * @date 2015年6月30日 上午10:42:08
	 * 
	 * @param shopId
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<Member> getMembersByShopIdAndFilter(Integer shopId, PaginatedFilter paginatedFilter);

	/**
	 * 前台会员注册
	 * 
	 * @author 毛智东
	 * @date 2015年7月9日 上午10:08:32
	 * 
	 * @param member
	 * @return
	 */
	boolean memberRegist(Member member, MapContext extra);
	
	/**
	 * 更新会员身份
	 * 
	 * @author guoyn
	 * @date 2015年12月16日 下午2:39:19
	 * 
	 * @param rank 身份
	 * @param memberId 用户id
	 * @return boolean
	 */
	boolean updateRankByMemberId(Integer rank, Integer memberId);
	
	/**
	 * 更新会员积分
	 * 
	 * @author guoyn
	 * @date 2015年12月16日 下午3:53:14
	 * 
	 * @param point
	 * @param memberId
	 * @return boolean
	 */
	boolean updatePointByMemberId(Integer point, Integer memberId);

	/**
	 * 根据消费金额更新会员当前的积分
	 * 
	 * @author guoyn
	 * @date 2015年12月16日 下午2:56:46
	 * 
	 * @param saleAmount 正值：消费金额增加积分，负值：退款金额减少积分
	 * @param memberId 用户id
	 * @return int
	 */
	boolean updateTradePointByMemberId(BigDecimal saleAmount, Integer memberId);
}
