package priv.starfish.mall.service.impl;

import org.springframework.stereotype.Service;
import priv.starfish.common.jms.SimpleMessage;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.dto.RegionParts;
import priv.starfish.mall.comn.entity.*;
import priv.starfish.mall.dao.comn.*;
import priv.starfish.mall.dao.mall.MallDao;
import priv.starfish.mall.dao.mall.MallNoticeDao;
import priv.starfish.mall.dao.mall.OperatorDao;
import priv.starfish.mall.dao.member.MemberDao;
import priv.starfish.mall.mall.dto.MallDto;
import priv.starfish.mall.mall.entity.Mall;
import priv.starfish.mall.mall.entity.MallNotice;
import priv.starfish.mall.mall.entity.Operator;
import priv.starfish.mall.member.entity.Member;
import priv.starfish.mall.service.BaseConst;
import priv.starfish.mall.service.MallService;
import priv.starfish.mall.service.UserService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("mallService")
public class MallServiceImpl extends BaseServiceImpl implements MallService {

	@Resource
	RegionDao regionDao;
	@Resource
	MallDao mallDao;
	@Resource
	MallNoticeDao mallNoticeDao;
	@Resource
	RoleDao roleDao;
	@Resource
	UserRoleDao userRoleDao;
	@Resource
	UserDao userDao;
	@Resource
	MemberDao memberDao;
	@Resource
	OperatorDao operatorDao;
	@Resource
	AgreementDao agreementDao;
	@Resource
	FileRepository fileRepository;
	@Resource
	UserAccountDao userAccountDao;

	@Resource
	UserService userService;

	private void sendMallInfoChangeTopicMessage() {
		SimpleMessage messageToSend = SimpleMessage.newOne();
		messageToSend.subject = BaseConst.SubjectNames.MALL_INFO;
		simpleMessageSender.sendTopicMessage(BaseConst.TopicNames.CONFIG, messageToSend);
	}

	@Override
	public Mall getMallById(Integer id) {
		return this.filterFileBrowseUrl(mallDao.selectById(id));
	}

	@Override
	public Mall getMallByCode(String code) {
		return this.filterFileBrowseUrl(mallDao.selectByCode(code));
	}

	@Override
	public Mall getMall() {
		return this.filterFileBrowseUrl(mallDao.selectTheOne());
	}

	@Override
	public Operator getOperator() {
		return operatorDao.selectTheOne();
	}

	@Override
	public MallDto getMallInfo() {
		//
		Mall mall = this.getMall();
		if (mall == null) {
			return null;
		}
		//
		MallDto mallInfo = new MallDto();
		//
		TypeUtil.copyProperties(mall, mallInfo);
		//
		return mallInfo;
	}

	@Override
	public Agreement getMerchAgreement() {
		return agreementDao.selectByTarget(Agreement.TARGET_MERCHANT);
	}

	@Override
	public Agreement getAgentAgreement() {
		return agreementDao.selectByTarget(Agreement.TARGET_AGENT);
	}

	@Override
	public Agreement getVendorAgreement() {
		return agreementDao.selectByTarget(Agreement.TARGET_VENDOR);
	}

	@Override
	public Agreement getMemberAgreement() {
		return agreementDao.selectByTarget(Agreement.TARGET_MEMBER);
	}

	@Override
	public MallNotice getMallNoticeById(Integer id) {
		return mallNoticeDao.selectById(id);
	}

	@Override
	public PaginatedList<MallNotice> getMallNoticesByFilter(PaginatedFilter paginatedFilter) {
		return mallNoticeDao.selectByFitler(paginatedFilter);
	}

	@Override
	public boolean createOrUpdateMall(MallDto mallDto) {
		boolean ok = false;
		//
		Date ts = new Date();
		//
		Operator operator = this.getOperator();
		//
		Mall mall = this.getMall();
		//
		if (mall == null) {// 新商城信息
			mall = new Mall();
			//
			mall.setRegTime(ts);
			mall.setDisabled(false);
		} else {// 修改商城信息
			mall.setDisabled(mallDto.getDisabled());
		}
		mall.setCode(mallDto.getCode());
		mall.setName(mallDto.getName());
		// 设置运营商id
		mall.setOperatorId(operator != null ? operator.getId() : mallDto.getOperatorId());
		mall.setRegionId(mallDto.getRegionId());
		mall.setStreet(mallDto.getStreet());
		mall.setPhoneNo(mallDto.getPhoneNo());
		mall.setLinkMan(mallDto.getLinkMan());
		mall.setTelNo(mallDto.getTelNo());
		mall.setBizScope(mallDto.getBizScope());
		mall.setLogoUuid(mallDto.getLogoUuid());
		mall.setLogoUsage(mallDto.getLogoUsage());
		mall.setLogoPath(mallDto.getLogoPath());
		mall.setIcpNo(mallDto.getIcpNo());
		mall.setIcpUuid(mallDto.getIcpNo());
		mall.setIcpUsage(mallDto.getIcpNo());
		mall.setIcpPath(mallDto.getIcpPath());
		mall.setDesc(mallDto.getDesc());

		// 数据重整
		Integer regionId = mall.getRegionId();
		if (regionId != null) {
			RegionParts regionParts = regionDao.selectPartsById(regionId);
			mall.setRegionName(regionParts.fullName);
		}

		// 检查街道信息
		String street = mall.getStreet();
		if (street == null) {
			street = "";
			mall.setStreet(street);
		}
		// 检查地址信息
		String address = mall.getAddress();
		if (StrUtil.isNullOrBlank(address)) {
			address = mall.getRegionName() + street;
			mall.setAddress(address);
		}
		// 检查用户-------------------------------------
		User user = null;
		Integer userId = mall.getOperatorId();
		if (userId != null) {
			// 检查OperatorId是否有效的用户id
			user = userDao.selectById(userId);
			if (user == null) {
				mall.setOperatorId(null);
			}
		}
		if (user == null) {
			String phoneNo = mall.getPhoneNo();
			user = userDao.selectByPhoneNo(phoneNo);
			if (user == null) {// 新用户
				user = new User();
				user.setPhoneNo(phoneNo);
				user.setNickName(StrUtil.hasText(mallDto.getNickName()) ? mallDto.getNickName() : phoneNo);
				user.setRegTime(ts);
				userDao.insert(user);
			}
			userId = user.getId();
		}
		// 检查会员--------------------------------------
		Member member = memberDao.selectById(userId);
		if (member == null) {// 新增会员信息
			member = new Member();
			member.setId(userId);
			member.setDisabled(false);
			memberDao.insert(member);
		}
		// 检查运营商
		if (operator == null) {
			operator = new Operator();
			operator.setId(userId);
			operator.setDisabled(false);
			operatorDao.insert(operator);
		}
		mall.setOperatorId(userId);

		// 注册资金账户-----------------------------------
		UserAccount userAccount = mallDto.getUserAccount();
		if (userAccount != null) {
			userAccount.setUserId(userId);
			userAccount.setVerified(false);
			userAccount.setDisabled(false);
			userAccountDao.insert(userAccount);
		}
		//
		if (mall.getId() == null) {
			ok = this.saveMall(mall);
		} else {
			ok = this.updateMall(mall);
		}
		// 注册权限
		if (ok) {
			// 检查用户商城的管理员权限---------------------
			Role adminRole = roleDao.selectBuiltInAdminByScope(AuthScope.mall);
			int adminRoleId = adminRole.getId();
			UserRole targetRole = userRoleDao.selectByUserIdAndRoleIdAndEntityId(userId, adminRoleId, mall.getId());
			if (targetRole == null) {
				targetRole = new UserRole();
				targetRole.setUserId(userId);
				targetRole.setRoleId(adminRoleId);
				targetRole.setScope(AuthScope.mall);
				targetRole.setEntityId(mall.getId());
				//
				ok = userRoleDao.insert(targetRole) > 0;
			}
			//
			this.sendMallInfoChangeTopicMessage();
		}

		return ok;
	}

	@Override
	public boolean saveMerchAgreement(Agreement mallAgreement) {
		if (StrUtil.isNullOrBlank(mallAgreement.getTarget())) {
			mallAgreement.setTarget(Agreement.TARGET_MERCHANT);
		}
		return agreementDao.insert(mallAgreement) > 0;
	}

	@Override
	public boolean saveAgentAgreement(Agreement agentAgreement) {
		if (StrUtil.isNullOrBlank(agentAgreement.getTarget())) {
			agentAgreement.setTarget(Agreement.TARGET_AGENT);
		}
		return agreementDao.insert(agentAgreement) > 0;
	}

	@Override
	public boolean saveVendorAgreement(Agreement agentAgreement) {
		if (StrUtil.isNullOrBlank(agentAgreement.getTarget())) {
			agentAgreement.setTarget(Agreement.TARGET_VENDOR);
		}
		return agreementDao.insert(agentAgreement) > 0;
	}

	@Override
	public boolean saveMemberAgreement(Agreement memberAgreement) {
		if (StrUtil.isNullOrBlank(memberAgreement.getTarget())) {
			memberAgreement.setTarget(Agreement.TARGET_MEMBER);
		}
		return agreementDao.insert(memberAgreement) > 0;
	}

	@Override
	public boolean updateAgreement(Agreement agreement) {
		return agreementDao.update(agreement) > 0;
	}

	@Override
	public boolean updateMallAndUser(MallDto mallDto) {
		Boolean ok = false;
		Mall mall = new Mall();
		TypeUtil.copyProperties(mallDto, mall);
		ok = updateMall(mall);
		// WJJ更新资金账户
		UserAccount userAccount = mallDto.getUserAccount();
		userAccountDao.update(userAccount);

		User user = userDao.selectById(mall.getOperatorId());
		if (user != null) {
			user.setNickName(mallDto.getNickName());
			user.setPhoneNo(mallDto.getUserPhoneNo());
			ok = userDao.update(user) > 0;
			//
		}
		if (ok) {
			this.sendMallInfoChangeTopicMessage();
		}

		return ok;
	}

	@Override
	public boolean saveMall(Mall mall) {
		return mallDao.insert(mall) > 0;
	}

	@Override
	public boolean saveMallNotice(MallNotice mallNotice) {
		return mallNoticeDao.insert(mallNotice) > 0;
	}

	@Override
	public boolean updateMall(Mall mall) {
		boolean ok = false;
		ok = mallDao.update(mall) > 0;
		if (ok) {
			//
			this.sendMallInfoChangeTopicMessage();
		}
		return ok;
	}

	@Override
	public boolean updateMallNotice(MallNotice mallNotice) {
		return mallNoticeDao.update(mallNotice) > 0;
	}

	@Override
	public boolean deleteMallById(Integer id) {
		return mallDao.deleteById(id) > 0;
	}

	@Override
	public boolean delMallNoticeById(Integer id) {
		return mallNoticeDao.deleteById(id) > 0;
	}

	@Override
	public boolean delMallNoticeByIds(List<Integer> ids) {
		return mallNoticeDao.deleteByIds(ids) > 0;
	}

	@Override
	public List<User> getMallWorkersById(Integer mallId, boolean includeRoles) {
		return userService.getAllUsersByScopeAndEntityId(AuthScope.mall, mallId, includeRoles);
	}

}
