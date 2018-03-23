package priv.starfish.mall.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.exception.XRuntimeException;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.comn.UserDao;
import priv.starfish.mall.dao.comn.UserRoleDao;
import priv.starfish.mall.dao.comn.UserVerfStatusDao;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.dict.VerfAspect;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.comn.entity.UserVerfStatus;
import priv.starfish.mall.dao.member.MemberDao;
import priv.starfish.mall.dao.member.MemberGradeDao;
import priv.starfish.mall.dao.member.MemberPointRuleDao;
import priv.starfish.mall.member.entity.Member;
import priv.starfish.mall.member.entity.MemberGrade;
import priv.starfish.mall.member.entity.MemberPointRule;
import priv.starfish.mall.service.MemberService;
import priv.starfish.mall.dao.statis.GoodsBuySumDao;

@Service("memberService")
public class MemberServiceImpl extends BaseServiceImpl implements MemberService {

	@Resource
	MemberGradeDao memberGradeDao;

	@Resource
	MemberPointRuleDao memberPointRuleDao;

	@Resource
	MemberDao memberDao;

	@Resource
	UserDao userDao;

	@Resource
	GoodsBuySumDao goodsBuySumDao;

	@Resource
	UserVerfStatusDao userVerfStatusDao;

	@Resource
	UserRoleDao userRoleDao;

	@Resource
	FileRepository fileRepository;

	@Override
	public PaginatedList<MemberGrade> getMemberGradesByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<MemberGrade> gradePageList = memberGradeDao.selectMemberGradesByFilter(paginatedFilter);
		List<MemberGrade> gradeList = gradePageList.getRows();
		// 设置icon浏览路径
		if (gradeList != null && !gradeList.isEmpty()) {
			for (MemberGrade grade : gradeList) {
				String browseUrl = fileRepository.getFileBrowseUrl(grade.getIconUsage(), grade.getIconPath());
				grade.setFileBrowseUrl(browseUrl);
			}
		}
		//
		return gradePageList;
	}

	@Override
	public boolean saveMemberGrade(MemberGrade memberGrade) {
		int count = memberGradeDao.insert(memberGrade);
		return count > 0;
	}

	@Override
	public boolean updateMemberGrade(MemberGrade memberGrade) {
		int count = memberGradeDao.update(memberGrade);
		return count > 0;
	}

	public boolean deleteMemberGradeById(Integer id) {
		int count = memberGradeDao.deleteById(id);
		return count > 0;
	}

	@Override
	public boolean updateMemberGrades(List<MemberGrade> memberGradeList) {
		int count = 0;
		for (MemberGrade memberGrade : memberGradeList) {
			count += memberGradeDao.update(memberGrade);
		}
		return count == memberGradeList.size();
	}

	@Override
	public boolean saveMemberPointRule(MemberPointRule memberPointRule) {
		int count = memberPointRuleDao.insert(memberPointRule);
		return count > 0;
	}

	@Override
	public boolean saveMemberPointRules(List<MemberPointRule> ruleList) {
		int count = 0;
		for (MemberPointRule rule : ruleList) {
			MemberPointRule memberPointRule = memberPointRuleDao.selectByCode(rule.getCode());
			if (memberPointRule != null) {
				count += memberPointRuleDao.updateByCode(rule);
			} else {
				count += memberPointRuleDao.insert(rule);
			}
		}
		return count == ruleList.size();
	}

	@Override
	public List<MemberPointRule> getMemberPointRules() {
		return memberPointRuleDao.selectAll();
	}

	@Override
	public PaginatedList<Member> getMembersByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<Member> members = memberDao.selectByFilter(paginatedFilter);
		return members;
	}

	@Override
	public Member getMemberById(Integer id) {
		Member member = memberDao.selectById(id);
		User user = userDao.selectById(id);
		if (member != null) {
			user.setPhoneNo(user.getPhoneNo());
			//
			List<UserVerfStatus> userVerfStatusList = userVerfStatusDao.selectByUserId(id);
			// 计算用户安全等级
			int safeLevels = 0;
			if (userVerfStatusList.size() > 0) {
				for (UserVerfStatus userVerfStatus : userVerfStatusList) {
					int secureLevel = userVerfStatus.getSecureLevel();
					safeLevels = secureLevel + safeLevels;
				}
				safeLevels = Math.round(safeLevels / (userVerfStatusList.size()));
			}
			user.setSecureLevel(safeLevels);
			//
			user.setUserVerfStatus(userVerfStatusList);
			member.setUser(user);
			String browseUrl = fileRepository.getFileBrowseUrl(member.getHeadUsage(), member.getHeadPath());
			if (StrUtil.isNullOrBlank(browseUrl)) {
				browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
			}
			member.setFileBrowseUrl(browseUrl);
			return member;
		} else {
			// 新建会员信息
			Member userMember = new Member();
			userMember.setId(user.getId());
			userMember.setPoint(0);
			userMember.setGrade(1);
			userMember.setDisabled(false);
			userMember.setMemo("您是注册会员");
			memberDao.insert(userMember);
			return userMember;
		}
		//
	}

	@Override
	public boolean saveMember(Member member) {
		User user = member.getUser();
		int uCount = userDao.insert(user);
		int mCount = 0;
		if (uCount > 0) {
			// 会员信息
			member.setId(user.getId());
			if (member.getPoint() == null) {
				member.setPoint(0);
				member.setGrade(1);
			} else {
				int grade = memberGradeDao.selectGradeByPoint(member.getPoint());
				member.setGrade(grade);
			}
			mCount = memberDao.insert(member);
			// 用户验证信息(手机号)
			UserVerfStatus userVerfStatus = new UserVerfStatus();
			userVerfStatus.setUserId(user.getId());
			userVerfStatus.setFlag(false);
			userVerfStatus.setAspect(VerfAspect.phoneNo);
			userVerfStatus.setTs(new Date());
			userVerfStatus.setSecureLevel(0);
			userVerfStatusDao.insert(userVerfStatus);

			// 用户验证信息(登录密码)
			userVerfStatus.setFlag(true);
			userVerfStatus.setAspect(VerfAspect.logPass);
			if (StrUtil.hasText(user.getSecureLevel() + "")) {
				userVerfStatus.setSecureLevel(user.getSecureLevel());
			} else {
				userVerfStatus.setSecureLevel(0);
			}

			userVerfStatusDao.insert(userVerfStatus);

			// 用户验证信息(支付密码)
			userVerfStatus.setFlag(false);
			userVerfStatus.setAspect(VerfAspect.payPass);
			userVerfStatus.setSecureLevel(0);
			userVerfStatusDao.insert(userVerfStatus);

		}
		return mCount > 0;
	}

	@Override
	public boolean updateMember(Member member) {
		int mCount = memberDao.update(member);
		boolean flag = mCount > 0;
		if (flag) {
			User user = member.getUser();
			if (user != null) {
				user.setId(member.getId());
				userDao.update(user);
			}
		}

		return flag;
	}

	@Override
	public boolean updateMemberDisabledById(Integer memberId, Boolean disabled) {
		// TODO 启用禁用会员状态需要记录在操作日志中
		int count = memberDao.updateDisabledById(disabled, memberId);
		return count > 0;
	}

	@Override
	public boolean batchUpdateMemberStatus(List<Integer> ids, boolean disabled) {
		for (Integer id : ids) {
			boolean status = updateMemberDisabledById(id, disabled);
			if (!status)
				return false;
		}
		return true;
	}

	@Override
	public List<Member> getMembersByFilterItems(MapContext filterItems) {
		return memberDao.selectByFilter(filterItems);
	}

	@Override
	public PaginatedList<Member> getMembersByShopIdAndFilter(Integer shopId, PaginatedFilter paginatedFilter) {
		MapContext filter = paginatedFilter.getFilterItems();
		// 设置店铺id过滤信息
		filter.put("shopId", shopId);
		filter.put("scope", AuthScope.shop);
		PaginatedList<Member> members = memberDao.selectByFilter(paginatedFilter);
		//
		return members;
	}

	@Override
	public boolean memberRegist(Member member, MapContext extra) {
		boolean result = this.saveMember(member);
		if (extra != null) {
			Object phoneNoFlag = extra.get("phoneNo");
			if (phoneNoFlag != null) {
				boolean flag = Boolean.parseBoolean(phoneNoFlag.toString());
				if (flag) {
					UserVerfStatus userVerfStatus = new UserVerfStatus();
					userVerfStatus.setUserId(member.getId());
					userVerfStatus.setAspect(VerfAspect.phoneNo);
					userVerfStatus.setFlag(true);
					result = userVerfStatusDao.insert(userVerfStatus) > 0 && result;
				}
			}
		}
		return result;
	}

	@Override
	public boolean updateRankByMemberId(Integer rank, Integer memberId) {
		int count = memberDao.updateRankById(rank, memberId);
		return count == 1;
	}

	@Override
	public boolean updatePointByMemberId(Integer point, Integer memberId) {
		// 获取当前用户的积分
		Member member = memberDao.selectById(memberId);
		if (member == null) {
			throw new XRuntimeException("会员不存在");
		}
		Integer _grade = member.getGrade();
		if (_grade == null) {
			_grade = 0;
		}
		// 更新会员积分
		int count = memberDao.updatePointById(point, memberId);
		// 更新会员等级
		int grade = memberGradeDao.selectGradeByPoint(point);
		if (!_grade.equals(grade) && count > 0) {
			memberDao.updateGradeById(grade, memberId);
		}
		return count > 0;
	}

	@Override
	public boolean updateTradePointByMemberId(BigDecimal saleAmount, Integer memberId) {
		// 获取当前用户的积分
		Member member = memberDao.selectById(memberId);
		if (member == null) {
			throw new XRuntimeException("会员不存在");
		}
		Integer point = member.getPoint();
		if (point == null) {
			point = 0;
		}
		// 获取积分兑换比例
		Float ratio = 1.0f;
		MemberPointRule memberPointRule = memberPointRuleDao.selectByCode("trade.exchange.ratio");
		if (memberPointRule != null) {
			ratio = memberPointRule.getValue();
		}
		// 获取兑换积分
		MathContext mc = new MathContext(2, RoundingMode.CEILING);
		BigDecimal bPoint = saleAmount.divide(new BigDecimal(ratio), mc);
		Double dPoint = Math.ceil(bPoint.doubleValue());
		int exchangePoint = dPoint.intValue();
		point += exchangePoint;
		//
		boolean ok = updatePointByMemberId(point, memberId);
		return ok;
	}

}
