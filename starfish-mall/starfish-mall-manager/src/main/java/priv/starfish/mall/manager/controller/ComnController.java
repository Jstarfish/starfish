package priv.starfish.mall.manager.controller;

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
import priv.starfish.mall.comn.entity.*;
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
 * 商城基础功能
 * 
 * @author 邓华锋
 * @date 2015年9月21日 上午9:05:51
 *
 */
@Remark("通用基础功能")
@Controller
@RequestMapping(value = "/comn")
public class ComnController extends BaseController {
	@Resource
	private ComnService comnService;

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

	// -----------------------------------常见问题分类------------------
	/**
	 * 常见问题分类页面
	 * 
	 * @author 邓华锋
	 * @date 2015年9月21日上午10:06:35
	 * 
	 * @return
	 */
	@Remark("常见问题分类页面")
	@RequestMapping(value = "/faqCat/list/jsp", method = RequestMethod.GET)
	public String toFaqCatList() {
		return "comn/faqCatList";
	}

	/**
	 * 根据分类名称判断分类是否存在
	 * 
	 * @author 邓华锋
	 * @date 2015年9月30日 上午10:50:31
	 * 
	 * @return
	 */
	@Remark("根据分类名称判断分类是否存在")
	@RequestMapping(value = "/faqCat/exist/by/name", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> existFaqCatByName(@RequestBody FaqCat faqCat) {
		Result<Boolean> result = Result.newOne();
		result.data = comnService.existFaqCatByName(faqCat.getName());
		return result;
	}

	/**
	 * 保存常见问题分类
	 * 
	 * @author 邓华锋
	 * @date 2015年9月21日上午10:06:30
	 * 
	 * @param faqCat
	 * @return
	 */
	@Remark("保存常见问题分类")
	@RequestMapping(value = "/faqCat/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> saveFaqCat(@RequestBody FaqCat faqCat) {
		Result<Integer> result = Result.newOne();
		boolean ok = comnService.saveFaqCat(faqCat);
		if (ok) {
			result.data = faqCat.getId();
			result.message = "保存成功";
		} else {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}

	/**
	 * 修改常见问题分类
	 * 
	 * @author 邓华锋
	 * @date 2015年9月21日上午10:06:25
	 * 
	 * @param faqCat
	 * @return
	 */
	@Remark("修改常见问题分类")
	@RequestMapping(value = "/faqCat/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateFaqCat(@RequestBody FaqCat faqCat) {
		Result<Integer> result = Result.newOne();
		// 修改常见问题分类
		boolean ok = comnService.updateFaqCat(faqCat);
		if (ok) {
			result.message = "修改成功";
		} else {
			result.type = Type.warn;
			result.message = "修改失败";
		}
		return result;
	}

	/**
	 * 删除常见问题分类
	 * 
	 * @author 邓华锋
	 * @date 2015年9月21日上午10:06:20
	 * 
	 * @return
	 */
	@Remark("删除常见问题分类")
	@RequestMapping(value = "/faqCat/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteFaqCat(@RequestBody FaqCat faqCat) {
		Result<?> result = Result.newOne();
		// 保存常见问题分类
		boolean ok = comnService.deleteFaqCatById(faqCat.getId());
		if (ok) {
			result.message = "删除成功";
		} else {
			result.type = Type.warn;
			result.message = "删除失败";
		}
		return result;
	}

	/**
	 * 分页查询常见问题分类
	 * 
	 * @author 邓华锋
	 * @date 2015年9月21日上午10:06:11
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询常见问题分类")
	@RequestMapping(value = "/faqCat/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<FaqCat> getFaqCats(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<FaqCat> paginatedList = comnService.getFaqCatsByFilter(paginatedFilter);
		//
		JqGridPage<FaqCat> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 获取常见问题分类列表
	 * 
	 * @author 邓华锋
	 * @date 2015年9月21日上午10:06:06
	 * 
	 * @return
	 */
	@Remark("获取常见问题分类列表")
	@RequestMapping(value = "/faqCat/selectList/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SelectList> getFaqCatList() {
		Result<SelectList> result = Result.newOne();
		SelectList selectList = SelectList.newOne();
		List<FaqCat> list = comnService.getFaqCats();
		selectList.setUnSelectedItem("", "- 请选择 -");
		if (list.size() > 0) {
			for (FaqCat faqCat : list) {
				selectList.addItem(Integer.toString(faqCat.getId()), faqCat.getName());
			}
		}
		selectList.setDefaultValue("");
		result.data = selectList;
		return result;
	}

	@Remark("根据分类ID获取常见问题分类")
	@RequestMapping(value = "/faqCat/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<FaqCat> getFaqCatById(@RequestBody FaqCat faqCat) {
		Result<FaqCat> result = Result.newOne();
		// 保存常见问题分类
		result.data = comnService.getFaqCatById(faqCat.getId());
		return result;
	}

	// --------------------------------常见问题分组----------------------------------------
	/**
	 * 常见问题分组页面
	 * 
	 * @author 邓华锋
	 * @date 2015年9月21日上午10:06:39
	 * 
	 * @return
	 */
	@Remark("常见问题分组页面")
	@RequestMapping(value = "/faqGroup/list/jsp", method = RequestMethod.GET)
	public String toFaqGroupList() {
		return "comn/faqGroupList";
	}

	/**
	 * 获取常见问题分组列表
	 * 
	 * @author 邓华锋
	 * @date 2015年9月21日上午10:05:59
	 * 
	 * @param catId
	 * @return
	 */
	@Remark("获取常见问题分组列表")
	@RequestMapping(value = "/faqGroup/selectList/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SelectList> getFaqGroupList(Integer catId) {
		Result<SelectList> result = Result.newOne();
		SelectList selectList = SelectList.newOne();
		List<FaqGroup> list = comnService.getFaqGroupsByCatId(catId);
		selectList.setUnSelectedItem("", "- 请选择 -");
		if (list != null && list.size() > 0) {
			for (FaqGroup fg : list) {
				selectList.addItem(Integer.toString(fg.getId()), fg.getName());
			}
		}
		selectList.setDefaultValue("");
		result.data = selectList;
		return result;
	}

	/**
	 * 保存常见问题分类
	 * 
	 * @author 邓华锋
	 * @date 2015年9月21日上午10:05:56
	 * 
	 * @param faqGroup
	 * @return
	 */
	@Remark("保存常见问题分类")
	@RequestMapping(value = "/faqGroup/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> saveFaqGroup(@RequestBody FaqGroup faqGroup) {
		Result<Integer> result = Result.newOne();
		// 保存常见问题分组
		boolean ok = comnService.saveFaqGroup(faqGroup);
		if (ok) {
			result.data = faqGroup.getId();
			result.message = "保存成功";
		} else {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}

	/**
	 * 修改常见问题分类
	 * 
	 * @author 邓华锋
	 * @date 2015年9月21日上午10:05:51
	 * 
	 * @param faqGroup
	 * @return
	 */
	@Remark("修改常见问题分类")
	@RequestMapping(value = "/faqGroup/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateFaqGroup(@RequestBody FaqGroup faqGroup) {
		Result<Integer> result = Result.newOne();
		// 修改常见问题分组
		boolean ok = comnService.updateFaqGroup(faqGroup);
		if (ok) {
			result.message = "保存成功";
		} else {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}

	/**
	 * 删除常见问题分组
	 * 
	 * @author 邓华锋
	 * @date 2015年9月21日上午10:05:47
	 * 
	 * @return
	 */
	@Remark("删除常见问题分组")
	@RequestMapping(value = "/faqGroup/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteFaqGroup(@RequestBody FaqGroup faqGroup) {
		Result<?> result = Result.newOne();
		boolean ok = comnService.deleteFaqGroupById(faqGroup.getId());
		if (ok) {
			result.message = "删除成功";
		} else {
			result.type = Type.warn;
			result.message = "删除失败";
		}
		return result;
	}

	/**
	 * 分页查询常见问题分组
	 * 
	 * @author 邓华锋
	 * @date 2015年9月21日上午10:05:42
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询常见问题分组")
	@RequestMapping(value = "/faqGroup/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<FaqGroup> getFaqGroups(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<FaqGroup> paginatedList = comnService.getFaqGroupsByFilter(paginatedFilter);
		//
		JqGridPage<FaqGroup> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	// --------------------------------常见问题----------------------------------------
	/**
	 * 常见问题页面
	 * 
	 * @author 邓华锋
	 * @date 2015年9月21日上午10:06:44
	 * 
	 * @return
	 */
	@Remark("常见问题页面")
	@RequestMapping(value = "/faq/list/jsp", method = RequestMethod.GET)
	public String toFaqList() {
		return "comn/faqList";
	}

	/**
	 * 保存常见问题
	 * 
	 * @author 邓华锋
	 * @date 2015年9月21日上午10:05:37
	 * 
	 * @param faq
	 * @return
	 */
	@Remark("保存常见问题")
	@RequestMapping(value = "/faq/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> saveFaq(@RequestBody Faq faq) {
		Result<Integer> result = Result.newOne();
		// 保存常见问题分组
		boolean ok = comnService.saveFaq(faq);
		if (ok) {
			result.data = faq.getId();
			result.message = "保存成功";
		} else {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}

	/**
	 * 修改常见问题
	 * 
	 * @author 邓华锋
	 * @date 2015年9月21日上午10:03:13
	 * 
	 * @param faq
	 * @return
	 */
	@Remark("修改常见问题")
	@RequestMapping(value = "/faq/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateFaq(@RequestBody Faq faq) {
		Result<Integer> result = Result.newOne();
		boolean ok = comnService.updateFaq(faq);
		if (ok) {
			result.message = "保存成功";
		} else {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}

	/**
	 * 删除常见问题
	 * 
	 * @author 邓华锋
	 * @date 2015年9月21日上午10:02:58
	 * 
	 * @return
	 */
	@Remark("删除常见问题")
	@RequestMapping(value = "/faq/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteFaq(@RequestBody Faq faq) {
		Result<?> result = Result.newOne();
		boolean ok = comnService.deleteFaqById(faq.getId());
		if (ok) {
			result.message = "删除成功";
		} else {
			result.type = Type.warn;
			result.message = "删除失败";
		}
		return result;
	}

	/**
	 * 分页查询常见问题
	 * 
	 * @author 邓华锋
	 * @date 2015年9月21日 上午10:02:27
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询常见问题")
	@RequestMapping(value = "/faq/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<Faq> getFaqs(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<Faq> paginatedList = comnService.getFaqsByFilter(paginatedFilter);
		//
		JqGridPage<Faq> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	// ---------------------------------------------------------------------------------
	/**
	 * 封装银行select组件
	 * 
	 * @author 毛智东
	 * @date 2015年5月29日 下午5:56:40
	 * 
	 * @return
	 */
	@Remark("封装银行select组件")
	@RequestMapping(value = "/bank/selectList/get", method = RequestMethod.POST)
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

	/**
	 * 增加企业营业执照
	 * 
	 * @author 郝江奎
	 * @date 2015年11月19日 下午1:56:40
	 * 
	 * @param request
	 * @return
	 */
	@Remark("增加企业营业执照")
	@RequestMapping(value = "/bizLicense/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<BizLicense> addBizLicense(HttpServletRequest request, @RequestBody BizLicense bizLicense) {
		Result<BizLicense> result = Result.newOne();
		// 获取页面缓存用户信息
		bizLicense.setTs(new Date());
		UserContext userContext = getUserContext(request);

		Integer userId = bizLicense.getUserId();
		if (userId == null) {
			userId = userContext.getUserId();
			bizLicense.setUserId(userId);
		}
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

	/**
	 * 获取营业执照
	 * 
	 * @author 郝江奎
	 * @date 2015年11月19日 下午1:56:40
	 * 
	 * @param request
	 * @return
	 */
	@Remark("获取营业执照")
	@RequestMapping(value = "/bizLicense/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<BizLicense> getBizLicense(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<BizLicense> paginatedList = shopService.getBizLicensesByFilter(paginatedFilter);
		//
		JqGridPage<BizLicense> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 通过userId获取营业执照
	 * 
	 * @author 郝江奎
	 * @date 2015年11月19日 下午1:56:40
	 * 
	 * @param request
	 * @return
	 */
	@Remark("通过userId获取营业执照")
	@RequestMapping(value = "/bizLicense/list/get/by/userId", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<BizLicense> getBizLicenseByUserId(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		MapContext filterItems = paginatedFilter.getFilterItems();
		Integer userId = filterItems.getTypedValue("userId", Integer.class);
		//
		if (userId == null) {
			// 获取页面缓存用户信息
			filterItems.remove("userId");
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

}
