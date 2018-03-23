package priv.starfish.mall.dao.member;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.member.entity.MemberPointRule;

@IBatisSqlTarget
public interface MemberPointRuleDao extends BaseDao<MemberPointRule, Integer> {
	MemberPointRule selectById(Integer id);

	MemberPointRule selectByCode(String code);

	int insert(MemberPointRule memberPointRule);

	int update(MemberPointRule memberPointRule);

	int deleteById(Integer id);

	/**
	 * 根据code更新会员积分规则
	 * 
	 * @author guoyn
	 * @date 2015年6月1日 下午3:41:53
	 * 
	 * @param rule
	 * @return int
	 */
	int updateByCode(MemberPointRule rule);

	/**
	 * 获取所有会员积分规则信息
	 * 
	 * @author guoyn
	 * @date 2015年6月1日 下午4:03:29
	 * 
	 * @return List<MemberPointRule>
	 */
	List<MemberPointRule> selectAll();
}