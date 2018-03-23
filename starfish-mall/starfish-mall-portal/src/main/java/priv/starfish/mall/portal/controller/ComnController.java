package priv.starfish.mall.portal.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.*;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.comn.entity.Bank;
import priv.starfish.mall.comn.entity.BizLicense;
import priv.starfish.mall.comn.entity.Faq;
import priv.starfish.mall.comn.entity.FaqCat;
import priv.starfish.mall.service.ComnService;
import priv.starfish.mall.service.MerchantService;
import priv.starfish.mall.service.ShopService;
import priv.starfish.mall.service.UserService;
import priv.starfish.mall.web.base.AppBase;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 基础功能
 * 
 * @author 邓华锋
 * @date 2015年10月27日 下午1:17:33
 *
 */
@Controller
@RequestMapping("/comn")
public class ComnController extends BaseController {
	@Resource
	ComnService comnService;

	@Resource
	private MerchantService merchService;

	@Resource
	private UserService userService;

	@Resource
	private ShopService shopService;

	@Remark("设置登陆后重定向url")
	@RequestMapping(value = "/redirectUrl/set/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<Boolean> setRedirectUrl(HttpSession session, @RequestBody MapContext requestData) throws IOException {
		Result<Boolean> result = Result.newOne();
		//
		String redirectUrl = requestData.getTypedValue("url", String.class);
		if (StrUtil.hasText(redirectUrl)) {
			session.setAttribute(AppBase.SESSION_KEY_REDIRECT_URL, redirectUrl);
			result.data = true;
		} else {
			result.data = false;
		}
		//
		return result;
	}

	// ---------------------------------链接---------------------------

	@Remark("联系我们")
	@RequestMapping(value = "/contactUs/jsp", method = RequestMethod.GET)
	public String toContactUs(HttpServletRequest request) {
		return "about/contactUs";
	}

	// ---------------------------------常见问题---------------------------

	/**
	 * 常见问题页面
	 * 
	 * @author 邓华锋
	 * @date 2015年9月21日上午10:06:44
	 * 
	 * @return
	 */
	@Remark("常见问题页面")
	@RequestMapping(value = "/faq/jsp", method = RequestMethod.GET)
	public String toFaqList() {
		return "comn/faq";
	}

	/**
	 * 获取常见问题分类列表 二级菜单
	 * 
	 * @author 邓华锋
	 * @date 2015年10月21日 下午4:02:08
	 * 
	 * @return
	 */
	@Remark("获取常见问题分类列表")
	@RequestMapping(value = "/faq/cat/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<FaqCat>> getFaqCats() {
		Result<List<FaqCat>> result = Result.newOne();
		result.data = comnService.getFaqCats();
		return result;
	}

	/**
	 * 获取常见问题列表
	 * 
	 * @author 邓华锋
	 * @date 2015年10月21日 下午5:52:02
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	@Remark("常见问题列表")
	@RequestMapping(value = "/faq/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<PaginatedList<Faq>> getFaqs(@RequestBody PaginatedFilter paginatedFilter) {
		Result<PaginatedList<Faq>> result = Result.newOne();
		PaginatedList<Faq> paginatedList = comnService.getFaqsByFilter(paginatedFilter);
		result.data = paginatedList;
		return result;
	}

	/**
	 * 根据
	 * 
	 * @author 邓华锋
	 * @date 2015年10月21日 下午5:52:11
	 * 
	 * @param faq
	 * @return
	 */
	@Remark("常见问题详情")
	@RequestMapping(value = "/faq/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Faq> getFaqDetail(@RequestBody Faq faq) {
		Result<Faq> result = Result.newOne();
		result.data = comnService.getFaqById(faq.getId());
		return result;
	}

	/**
	 * 封装银行select组件
	 * 
	 * @author 毛智东
	 * @date 2015年5月29日 下午5:56:40
	 * 
	 * @return
	 */
	@Remark("封装银行select组件")
	@RequestMapping(value = "/bank/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SelectList> getBanks() {
		Result<SelectList> result = Result.newOne();
		SelectList selectList = SelectList.newOne();
		List<Bank> list = merchService.getBanks();
		if (list.size() > 0) {
			selectList.setUnSelectedItem("", "- 请选择 -");
		}
		for (Bank bank : list) {
			selectList.addItem(bank.getCode(), bank.getName());
		}
		selectList.setDefaultValue("");
		result.data = selectList;
		return result;
	}

	@Remark("增加企业营业执照")
	@RequestMapping(value = "/bizLicense/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<BizLicense> addBizLicense(HttpServletRequest request, @RequestBody BizLicense bizLicense) {
		Result<BizLicense> result = Result.newOne();
		// 获取页面缓存用户信息
		bizLicense.setTs(new Date());
		//
		boolean ok = shopService.createBizLicense(bizLicense);
		if (ok) {
			result.data = bizLicense;
			result.message = "添加成功";
		} else {
			result.type = Type.warn;
			result.message = "添加失败";
		}
		return result;
	}

	@Remark("获取营业执照")
	@RequestMapping(value = "/bizLicense/list/get/by/userId", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<BizLicense> getBizLicense(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		MapContext filterItems = paginatedFilter.getFilterItems();
		Integer userId = filterItems.getTypedValue("userId", Integer.class);
		filterItems.remove("userId");
		if (userId == null) {
			// 获取页面缓存用户信息
			UserContext userContext = getUserContext(request);
			userId = userContext.getUserId();
			filterItems.put("userId", userId);
		}
		//
		PaginatedList<BizLicense> paginatedList = shopService.getBizLicensesByFilter(paginatedFilter);
		//
		JqGridPage<BizLicense> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

//	@Remark("获取营业执照")
//	@RequestMapping(value = "/bizLicense/list/get/by/userId", method = RequestMethod.POST)
//	@ResponseBody
//	public JqGridPage<BizLicense> getInfo(HttpServletRequest request) {
//		// 封装前台参数为JqGridRequest格式
//		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
//		// 封装为PaginatedFilter格式
//		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
//		MapContext filterItems = paginatedFilter.getFilterItems();
//		Integer userId = filterItems.getTypedValue("userId", Integer.class);
//		filterItems.remove("userId");
//		if (userId == null) {
//			// 获取页面缓存用户信息
//			UserContext userContext = getUserContext(request);
//			userId = userContext.getUserId();
//			filterItems.put("userId", userId);
//		}
//		//
//		PaginatedList<BizLicense> paginatedList = shopService.getBizLicensesByFilter(paginatedFilter);
//		//
//		JqGridPage<BizLicense> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
//		return jqGridPage;
//	}

	/**
	 * 安装费详细说明
	 * 
	 * @author 邓华锋
	 * @date 2015年9月21日上午10:06:44
	 * 
	 * @return
	 */
	@Remark("安装费详细说明")
	@RequestMapping(value = "/installation/fee/details/jsp", method = RequestMethod.GET)
	public String toInstallationFeeDetails() {
		return "comn/installationFeeDetails";
	}
}
