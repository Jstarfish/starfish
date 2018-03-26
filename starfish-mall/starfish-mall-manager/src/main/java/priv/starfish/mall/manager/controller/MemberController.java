package priv.starfish.mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.base.Converter;
import priv.starfish.common.model.*;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.CollectionUtil;
import priv.starfish.common.util.EnumUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.member.entity.Member;
import priv.starfish.mall.member.entity.MemberGrade;
import priv.starfish.mall.member.entity.MemberPointRule;
import priv.starfish.mall.service.MemberService;
import priv.starfish.mall.service.StatisService;
import priv.starfish.mall.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Remark("会员、等级、账号、地址等相关处理")
@Controller
@RequestMapping(value = "/member")
public class MemberController extends BaseController {

	@Resource
	MemberService memberService;

	@Resource
	UserService userService;

	@Resource
	StatisService memberTradesService;

	// -------------------------------会员等级配置-----------------------------------
	/**
	 * 
	 * @author guoyn
	 * @date 2015年5月28日 下午5:33:47
	 * 
	 * @return
	 */
	@Remark("会员等级配置页面")
	@RequestMapping(value = "/grade/list/jsp", method = RequestMethod.GET)
	public String toMemberGradeConfig() {
		return "member/memberGradeConfig";
	}
	
	/**
	 * 分页查询会员等级信息
	 * 
	 * @author guoyn
	 * @date 2015年5月22日 下午5:35:45
	 * 
	 * @param request
	 * @return JqGridPage<MemberGrade>
	 */
	@Remark("分页查询会员等级信息")
	@RequestMapping(value = "/grade/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<MemberGrade> getMemberGrades(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<MemberGrade> paginatedList = memberService.getMemberGradesByFilter(paginatedFilter);
		//
		JqGridPage<MemberGrade> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	// 把DB数据模型转化为通用模型
	Converter<Map<String, Object>, MemberGrade> memberGradeConverter = new Converter<Map<String, Object>, MemberGrade>() {

		@Override
		public MemberGrade convert(Map<String, Object> src, int index) {
			MemberGrade mg = new MemberGrade();
			MapContext mapContext = MapContext.from(src);
			// {id=3, grade=1, name=csdc, lowerPoint=1000, desc=}
			mg.setId(mapContext.getTypedValue("id", Integer.class));
			mg.setGrade(mapContext.getTypedValue("grade", Integer.class));
			mg.setName(mapContext.getTypedValue("name", String.class));
			mg.setLowerPoint(mapContext.getTypedValue("lowerPoint", Integer.class));
			mg.setUpperPoint(mapContext.getTypedValue("upperPoint", Integer.class));
			mg.setIconPath(mapContext.getTypedValue("iconPath", String.class));
			mg.setIconUuid(mapContext.getTypedValue("iconUuid", String.class));
			mg.setIconUsage(mapContext.getTypedValue("iconUsage", String.class));
			return mg;
		}
	};

	/**
	 * 保存会员等级信息
	 * 
	 * @author guoyn
	 * @date 2015年6月1日 上午10:15:11
	 * 
	 * @param requestData
	 * @param response
	 * @return Result<?>
	 */
	@Remark("保存会员等级信息")
	@RequestMapping(value = "/grade/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> updateMemberGrade(@RequestBody List<Map<String, Object>> requestData, HttpServletResponse response) {
		//
		List<MemberGrade> mgList = CollectionUtil.convertToList(requestData, memberGradeConverter);
		//
		Result<?> result = Result.newOne();
		//
		result.message = "保存成功!";
		boolean ok = memberService.updateMemberGrades(mgList);
		if (!ok) {
			result.type = Type.warn;
			result.message = "保存失败!";
		}
		return result;
	}

	// -------------------------------会员积分规则-----------------------------------

	/**
	 * 积分规则配置页面
	 * 
	 * @author guoyn
	 * @date 2015年6月1日 上午10:17:09
	 * 
	 * @return String
	 */
	@Remark("会员积分规则配置页面")
	@RequestMapping(value = "/point/rule/list/jsp", method = RequestMethod.GET)
	public String toMemberPointRuleConfig() {
		return "member/memberPointRuleConfig";
	}

	/**
	 * 获取所有会员积分规则信息
	 * 
	 * @author guoyn
	 * @date 2015年6月1日 下午3:59:10
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@Remark("获取所有会员积分规则信息")
	@RequestMapping(value = "/point/rule/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<MemberPointRule>> getMemberPointRules(HttpServletRequest request, HttpServletResponse response) {
		Result<List<MemberPointRule>> result = Result.newOne();
		List<MemberPointRule> ruleList = new ArrayList<MemberPointRule>(0);
		ruleList = memberService.getMemberPointRules();
		result.data = ruleList;
		return result;
	}

	// 把DB数据模型转化为通用模型
	Converter<Map<String, Object>, MemberPointRule> memberPointRuleConverter = new Converter<Map<String, Object>, MemberPointRule>() {

		@Override
		public MemberPointRule convert(Map<String, Object> src, int index) {
			MemberPointRule rule = new MemberPointRule();
			MapContext mapContext = MapContext.from(src);
			rule.setCode(mapContext.getTypedValue("code", String.class));
			rule.setValue(Float.valueOf(mapContext.getTypedValue("value", String.class)));
			return rule;
		}
	};

	/**
	 * 更新会员积分规则信息
	 * 
	 * @author guoyn
	 * @date 2015年6月1日 下午3:33:50
	 * 
	 * @param requestData
	 * @param response
	 * @return Result<Object>
	 */
	@Remark("更新会员积分规则信息")
	@RequestMapping(value = "/point/rule/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> updateMemberPointRule(@RequestBody List<Map<String, Object>> requestData, HttpServletResponse response) {
		//
		List<MemberPointRule> ruleList = CollectionUtil.convertToList(requestData, memberPointRuleConverter);
		//
		Result<Object> result = Result.newOne();
		//
		result.message = "保存成功!";
		boolean ok = memberService.saveMemberPointRules(ruleList);
		if (!ok) {
			result.type = Type.warn;
			result.message = "保存失败!";
		}
		return result;
	}

	// TODO-------------------------------商城会员列表-----------------------------------
	/**
	 * 会员管理页面
	 * 
	 * @author guoyn
	 * @date 2015年6月2日 下午2:03:07
	 * 
	 * @return String
	 */
	@Remark("会员管理（商城级）")
	@RequestMapping(value = "/mall/list/jsp", method = RequestMethod.GET)
	public String toMallMemberConfig() {
		return "member/mallMemberMgmt";
	}

	/**
	 * 分页获取商城会员列表
	 * 
	 * @author guoyn
	 * @date 2015年6月2日 下午2:06:53
	 * 
	 * @param request
	 * @return JqGridPage<Member>
	 */
	@Remark("获取商城会员列表信息")
	@RequestMapping(value = "/mall/list/by/page/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<Member> getMembersByPage(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<Member> paginatedList = memberService.getMembersByFilter(paginatedFilter);
		//
		JqGridPage<Member> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 验证会员是否存在
	 * 
	 * @author guoyn
	 * @date 2015年8月7日 上午11:46:42
	 * 
	 * @param requestData
	 * @param response
	 * @return Result<Boolean>
	 */
	@Remark("验证会员是否存在")
	@RequestMapping(value = "/isExsit/by/phoneNo/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> isExsitMember(@RequestBody MapContext requestData, HttpServletResponse response) {
		//
		Result<Boolean> result = Result.newOne();
		//
		String phoneNo = requestData.getTypedValue("phoneNo", String.class);
		boolean isExsit = false;
		User user = userService.getUserByPhoneNo(phoneNo);
		if (user != null)
			isExsit = true;
		result.data = isExsit;
		return result;
	}

	/**
	 * 保存会员信息
	 * 
	 * @author guoyn
	 * @date 2015年6月3日 下午6:26:37
	 * 
	 * @param member
	 * @return Result<Integer> 返回新添加会员的id
	 */
	@Remark("保存会员信息")
	@RequestMapping(value = "/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> saveMember(HttpServletRequest request, @RequestBody Member member) {
		//
		Result<Integer> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		//设置店铺相关信息
		ScopeEntity scopeEntity = userContext.getScopeEntity();
		Integer entityId = scopeEntity.getId();
		AuthScope scope = EnumUtil.valueOf(AuthScope.class, scopeEntity.getScope());
		member.setScope(scope);
		member.setEntityId(entityId);
		boolean ok = memberService.saveMember(member);
		result.data = member.getId();
		result.message = ok ? "保存成功!" : "保存失败!";
		return result;
	}

	/**
	 * 更新会员信息，返回更新后的会员Id
	 * 
	 * @author guoyn
	 * @date 2015年6月4日 上午10:13:14
	 * 
	 * @param member
	 * @param response
	 * @return Result<Integer>
	 */
	@Remark("更新会员信息")
	@RequestMapping(value = "/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateMember(@RequestBody Member member, HttpServletResponse response) {
		//
		Result<Integer> result = Result.newOne();
		//
		boolean ok = memberService.updateMember(member);
		result.data = member.getId();
		result.message = ok ? "保存成功!" : "保存失败!";
		return result;
	}

	/**
	 * 根据id启用、禁用会员
	 * 
	 * @author guoyn
	 * @date 2015年6月4日 上午10:17:13
	 * 
	 * @param member
	 * @param response
	 * @return Result<Object>
	 */
	@Remark("根据id启用、禁用会员")
	@RequestMapping(value = "/update/status/by/id/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> updateMemberStatusById(@RequestBody Member member, HttpServletResponse response) {
		//
		Result<Object> result = Result.newOne();
		result.message = "修改成功!";
		//
		boolean ok = memberService.updateMemberDisabledById(member.getId(), member.getDisabled());
		if (!ok) {
			result.type = Type.warn;
			result.message = "修改失败!";
		}
		return result;
	}

	/**
	 * 批量启用、禁用会员
	 * 
	 * @author guoyn
	 * @date 2015年6月4日 下午1:58:24
	 * 
	 * @param requestData
	 * @param response
	 * @return Result<Object>
	 */
	@Remark("批量启用、禁用会员")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/update/status/by/ids/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> updateMemberStatusByIds(@RequestBody MapContext requestData, HttpServletResponse response) {
		//
		List<Integer> ids = requestData.getTypedValue("ids", TypeUtil.Types.IntegerList.getClass());
		boolean disabled = requestData.getTypedValue("disabled", Boolean.class);
		Result<Object> result = Result.newOne();
		result.message = "修改成功!";
		//
		boolean ok = memberService.batchUpdateMemberStatus(ids, disabled);
		if (!ok) {
			result.type = Type.warn;
			result.message = "修改失败!";
		}
		return result;
	}

	// -------------------------------店铺会员列表-----------------------------------

	/**
	 * 跳转店铺会员管理页面
	 * 
	 * @author guoyn
	 * @date 2015年6月9日 上午9:30:20
	 * 
	 * @return String
	 */
	@Remark("会员管理（店铺级）")
	@RequestMapping(value = "/shop/list/jsp", method = RequestMethod.GET)
	public String toShopMemberConfig() {
		return "member/shopMemberMgmt";
	}

	/**
	 * 分页获取某店铺的会员相关信息
	 * 
	 * @author guoyn
	 * @date 2015年6月9日 上午9:31:02
	 * 
	 * @param request
	 * @return JqGridPage<GoodsBuySum>
	 */
	@Remark("获取某店铺会员列表信息")
	@RequestMapping(value = "/shop/list/by/page/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<Member> getShopMembers(HttpServletRequest request) {
		UserContext userContext = getUserContext(request);
		//
		AuthScope scope = EnumUtil.valueOf(AuthScope.class, userContext.getScope());
		//
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		// 从session中获取实体信息--店铺id
		// Integer shopId = null;
		Integer shopId = 1;
		if (scope == AuthScope.shop) {
			shopId = userContext.getScopeEntityId(scope.name());
		} else {
			// shopId = Integer.valueOf(request.getParameter("shopId"));
		}

		PaginatedList<Member> paginatedList = memberService.getMembersByShopIdAndFilter(shopId, paginatedFilter);
		//
		JqGridPage<Member> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 获取所有会员列表
	 * 
	 * @author guoyn
	 * @date 2015年8月7日 上午11:48:37
	 * 
	 * @param request
	 * @param response
	 * @return Result<List<Member>>
	 */
	@Remark("获取所有会员列表")
	@RequestMapping(value = "/mall/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<Member>> getAllMembers(HttpServletRequest request, HttpServletResponse response) {
		Result<List<Member>> result = Result.newOne();
		//
		List<Member> members = memberService.getMembersByFilterItems(null);
		result.data = members;
		return result;
	}

	// TODO---------WJJ-----begin-----------------会员操作日志-----------------------------------

	/**
	 * 
	 * @author WJJ
	 * @date 2015年9月17日 下午3:48:36
	 * 
	 * @return
	 */
	@Remark("去往会员操作日志页面")
	@RequestMapping(value = "/operation/log/jsp", method = RequestMethod.GET)
	public String toMemberOperationLog() {
		return "member/memberOperationLog";
	}

	/*
	 * @Remark("获取商城会员操作日志信息") 还没做，做时可根据 （214 会员管理页面）memberOperationLog.jsp 也还没改
	 * 
	 * @RequestMapping(value = "/mall/list/by/page/get", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public JqGridPage<Member> getLogsByPage(HttpServletRequest request) { // 封装前台参数为JqGridRequest格式 JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request); //
	 * 封装为PaginatedFilter格式 PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter(); // PaginatedList<Member> paginatedList = memberService.getMembersByFilter(paginatedFilter); //
	 * JqGridPage<Member> jqGridPage = JqGridPage.fromPaginatedList(paginatedList); return jqGridPage; }
	 */

	// ---------WJJ-----end-----------------会员操作日志-----------------------------------

	
}


