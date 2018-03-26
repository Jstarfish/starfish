package priv.starfish.mall.manager.controller;

import org.apache.commons.collections.CollectionUtils;
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
import priv.starfish.common.util.TypeUtil;
import priv.starfish.common.util.TypeUtil.Types;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.market.entity.*;
import priv.starfish.mall.service.ActivityService;
import priv.starfish.mall.service.AdvertService;
import priv.starfish.mall.service.SalePrmtService;
import priv.starfish.mall.service.SettingService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Remark("商品展示分类、促销、活动、广告、优惠券")
@Controller
@RequestMapping(value = "/market")
public class MarketController extends BaseController {

	@Resource
	private SettingService settingService;

	@Resource
	private AdvertService advertService;

	@Resource
	private SalePrmtService salePrmtService;
	
	@Resource
	private ActivityService activityService;

	// -------------------------------页面设置-----------------------------------

	/**
	 * 页面设置
	 * 
	 * @author zjl
	 * @date 2015年7月8日 下午4:03:33
	 * 
	 * @return
	 */
	@Remark("页面设置")
	@RequestMapping(value = "/page/set/jsp", method = RequestMethod.GET)
	public String toPagSetJsp() {
		return "market/pageSet";
	}

	/**
	 * 验证广告名称是否被占用
	 * 
	 * @author guoyn
	 * @date 2015年9月15日 下午2:35:44
	 * 
	 * @param advert
	 * @return Result<Boolean>
	 */
	@Remark("广告名称是否占用")
	@RequestMapping(value = "/advert/exist/by/name", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> validateAdvertByName(@RequestBody Advert advert) {
		Result<Boolean> result = Result.newOne();
		boolean isExsit = false;
		String advertName = advert.getName();
		if (StrUtil.hasText(advertName)) {
			Advert _advert = advertService.getAdvertByName(advertName);
			if (_advert != null)
				isExsit = true;
		}
		//
		result.data = isExsit;
		return result;
	}

	/**
	 * 删除广告链接
	 * 
	 * @author guoyn
	 * @date 2015年9月16日 下午4:46:46
	 * 
	 * @param advertLink
	 * @return Result<Object>
	 */
	@Remark("删除广告链接")
	@RequestMapping(value = "/advertLink/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> delAdvertLinkById(@RequestBody AdvertLink advertLink) {
		Result<Object> result = Result.newOne();
		boolean flag = true;
		Integer advertLinkId = advertLink.getId();
		if (advertLinkId != null) {
			flag = advertService.deleteAdvertLinkById(advertLinkId);
		}
		//
		if (!flag) {
			flag = false;
			result.type = Result.Type.error;
		}
		//
		result.message = flag ? "操作成功" : "操作失败";
		return result;

	}

	/**
	 * 根据Id删除广告
	 * 
	 * @author guoyn
	 * @date 2015年9月16日 下午6:23:39
	 * 
	 * @return Result<Object>
	 */
	@Remark("根据Id删除广告")
	@RequestMapping(value = "/advert/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> delAdvertById(@RequestBody Advert advert) {
		Result<Object> result = Result.newOne();
		boolean flag = true;
		Integer advertId = advert.getId();
		if (advertId != null) {
			flag = advertService.deleteAdvertById(advertId);
		}
		//
		if (!flag) {
			flag = false;
			result.type = Result.Type.error;
		}
		//
		result.message = flag ? "操作成功" : "操作失败";
		return result;

	}

	/**
	 * 批量删除广告
	 * 
	 * @author guoyn
	 * @date 2015年9月16日 下午6:30:26
	 * 
	 * @return Result<Object>
	 */
	@Remark("根据Ids 删除广告")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/advert/delete/by/ids", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> deleteAdverts(@RequestBody MapContext requestData) {
		Result<Object> result = Result.newOne();
		List<Integer> ids = requestData.getTypedValue("ids", Types.IntegerList.getClass());
		boolean ok = advertService.deleteAdvertByIds(ids);
		result.message = "操作成功";
		if (!ok) {
			result.type = Type.error;
			result.message = "操作失败";
		}
		//
		return result;
	}

	/**
	 * 分页查询广告
	 * 
	 * @author guoyn
	 * @date 2015年9月16日 下午7:36:56
	 * 
	 * @param request
	 * @return JqGridPage<Advert>
	 */
	@Remark("分页查询广告")
	@RequestMapping(value = "/advert/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<Advert> getAdverts(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<Advert> paginatedList = advertService.getAdvertsByFilter(paginatedFilter);
		JqGridPage<Advert> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 新增广告
	 * 
	 * @author guoyn
	 * @date 2015年9月16日 下午6:46:43
	 * 
	 * @param advert
	 * @return Result<Integer>
	 */
	@Remark("新增广告")
	@RequestMapping(value = "/advert/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createAdvert(@RequestBody Advert advert) {
		Result<Integer> result = Result.newOne();
		boolean caroFlag = false;
		String caroAnim = advert.getCaroAnim();
		Integer caroIntv = advert.getCaroIntv();
		if (!StrUtil.isNullOrBlank(caroAnim) && caroIntv != null) {
			caroFlag = true;
		}
		advert.setCaroFlag(caroFlag);
		//
		boolean ok = advertService.saveAdvert(advert);
		//
		result.message = "操作成功";
		result.data = advert.getId();
		//
		if (!ok) {
			result.type = Type.error;
			result.message = "操作失败";
		}
		return result;
	}

	/**
	 * 广告预定义位置
	 * 
	 * @author guoyn
	 * @date 2015年9月18日 下午5:09:39
	 * 
	 * @param request
	 * @return Result<SelectList>
	 */
	@Remark("广告预定义位置")
	@RequestMapping(value = "/advertPos/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SelectList> getAdvertPoss(HttpServletRequest request) {
		Result<SelectList> result = Result.newOne();
		try {
			List<AdvertPos> advertPosList = advertService.getAdvertPoss();

			SelectList selectList = SelectList.newOne();
			selectList.setUnSelectedItem("", "- 自定义 -");
			if (CollectionUtils.isNotEmpty(advertPosList)) {
				for (AdvertPos advertPos : advertPosList) {
					selectList.addItem(advertPos.getCode() + "", advertPos.getName());
				}
				selectList.setDefaultValue("");
			}

			result.data = selectList;
		} catch (Exception e) {
			result.type = Type.error;
			result.message = "初始化预定义广告位置错误！";
		}

		return result;
	}

	/**
	 * 更新广告
	 * 
	 * @author guoyn
	 * @date 2015年9月18日 下午5:11:35
	 * 
	 * @param advert
	 * @return Result<Advert>
	 */
	@Remark("更新广告")
	@RequestMapping(value = "/advert/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Advert> updateAdvert(@RequestBody Advert advert) {
		Result<Advert> result = Result.newOne();
		boolean flag = true;
		try {
			flag = advertService.updateAdvert(advert);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			result.type = Result.Type.error;
		}
		result.data = advert;
		result.message = flag ? "操作成功" : "操作失败";
		return result;

	}

	// -------------------------------商品优惠券页面设置-----------------------------------

	@Remark("页面设置")
	@RequestMapping(value = "/coupon/list/jsp", method = RequestMethod.GET)
	public String toCouponJsp() {
		return "market/couponList";
	}

	@Remark("分页查询")
	@RequestMapping(value = "/coupon/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<Coupon> getCoupons(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		MapContext mapContext = paginatedFilter.getFilterItems();
		mapContext.put("deleted", false);
		PaginatedList<Coupon> paginatedList = salePrmtService.getCouponsByFilter(paginatedFilter);
		JqGridPage<Coupon> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	@Remark("优惠券回收站分页查询")
	@RequestMapping(value = "/coupon/recycle/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<Coupon> getRecycleCoupons(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		MapContext mapContext = paginatedFilter.getFilterItems();
		mapContext.put("deleted", true);
		PaginatedList<Coupon> paginatedList = salePrmtService.getCouponsByFilter(paginatedFilter);
		JqGridPage<Coupon> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	@Remark("优惠券选择商品列表页")
	@RequestMapping(value = "/coupon/bind/goods/jsp", method = RequestMethod.GET)
	public String toCouponGoodsJsp() {
		return "market/checkGoods";
	}

	@Remark("优惠券选择服务列表页")
	@RequestMapping(value = "/coupon/bind/svc/jsp", method = RequestMethod.GET)
	public String toCouponCarSvcJsp() {
		return "market/checkCarSvc";
	}

	/**
	 * 保存优惠券
	 * 
	 * @author 李超杰
	 * @date 2015年10月13日 下午3:05:36
	 * 
	 * @return
	 */
	@Remark("保存优惠券")
	@RequestMapping(value = "/coupon/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> saveCoupon(@RequestBody Coupon coupon) {
		Result<Integer> result = Result.newOne();
		// 保存常见问题
		boolean ok = salePrmtService.saveCoupon(coupon);
		if (ok) {
			result.data = coupon.getId();
			result.message = "保存成功";
		} else {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}

	@Remark("删除优惠券 假删")
	@RequestMapping(value = "/coupon/deleted/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateCouponByidAndDeleted(@RequestBody MapContext requestDate) {
		Result<Integer> result = Result.newOne();
		Integer id = requestDate.getTypedValue("id", Integer.class);
		Boolean deleted = requestDate.getTypedValue("deleted", Boolean.class);
		//
		boolean ok = salePrmtService.updateCouponByIdAndDeleted(id, deleted);
		if (ok) {
			result.data = id;
			result.message = "更新成功";
		} else {
			result.type = Type.warn;
			result.message = "更新失败";
		}
		return result;
	}

	@Remark("启禁用优惠券")
	@RequestMapping(value = "/coupon/disabled/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateCouponByidAndDisabled(@RequestBody MapContext requestDate) {
		Result<Integer> result = Result.newOne();
		Integer id = requestDate.getTypedValue("id", Integer.class);
		Boolean disabled = requestDate.getTypedValue("disabled", Boolean.class);
		//
		boolean ok = salePrmtService.updateCouponByIdAndDisabled(id, disabled);
		if (ok) {
			result.data = id;
			result.message = "更新成功";
		} else {
			result.type = Type.warn;
			result.message = "更新失败";
		}
		return result;
	}

	@Remark("更新优惠券")
	@RequestMapping(value = "/coupon/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateCouponByid(@RequestBody Coupon coupon) {
		Result<Integer> result = Result.newOne();
		//
		boolean ok = salePrmtService.updateCoupon(coupon);
		if (ok) {
			result.data = coupon.getId();
			result.message = "更新成功";
		} else {
			result.type = Type.warn;
			result.message = "更新失败";
		}
		return result;
	}

	/**
	 * 根据分组名称判断分类是否存在
	 * 
	 * @author 李超杰
	 * @date 2015年10月14日 下午7:13:22
	 * 
	 * @return
	 */
	@Remark("根据分组名称判断优惠券是否存在")
	@RequestMapping(value = "/coupon/exist/by/name", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> existCouponByName(@RequestBody Coupon coupon) {
		Result<Boolean> result = Result.newOne();
		result.data = salePrmtService.existCouponByGroupName(coupon.getName());
		return result;
	}

	// -------------------------------服务优惠券页面设置-----------------------------------

	@Remark("页面设置")
	@RequestMapping(value = "/svcCoupon/list/jsp", method = RequestMethod.GET)
	public String toSvcCouponJsp() {
		return "market/svcCouponList";
	}

	@Remark("分页查询")
	@RequestMapping(value = "/svcCoupon/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SvcCoupon> getSvcCoupons(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		MapContext mapContext = paginatedFilter.getFilterItems();
		mapContext.put("deleted", false);
		PaginatedList<SvcCoupon> paginatedList = salePrmtService.getSvcCouponsByFilter(paginatedFilter);
		JqGridPage<SvcCoupon> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	@Remark("优惠券回收站分页查询")
	@RequestMapping(value = "/svcCoupon/recycle/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SvcCoupon> getRecycleSvcCoupons(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		MapContext mapContext = paginatedFilter.getFilterItems();
		mapContext.put("deleted", true);
		PaginatedList<SvcCoupon> paginatedList = salePrmtService.getSvcCouponsByFilter(paginatedFilter);
		JqGridPage<SvcCoupon> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 保存优惠券
	 * 
	 * @author 李超杰
	 * @date 2015年10月13日 下午3:05:36
	 * 
	 * @return
	 */
	@Remark("保存优惠券")
	@RequestMapping(value = "/svcCoupon/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> saveSvcCoupon(@RequestBody SvcCoupon svcCoupon) {
		Result<Integer> result = Result.newOne();
		// 保存常见问题
		boolean ok = salePrmtService.saveSvcCoupon(svcCoupon);
		if (ok) {
			result.data = svcCoupon.getId();
			result.message = "保存成功";
		} else {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}

	@Remark("更新优惠券")
	@RequestMapping(value = "/svcCoupon/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateSvcCouponByid(@RequestBody SvcCoupon svcCoupon) {
		Result<Integer> result = Result.newOne();
		//
		boolean ok = salePrmtService.updateSvcCoupon(svcCoupon);
		if (ok) {
			result.data = svcCoupon.getId();
			result.message = "更新成功";
		} else {
			result.type = Type.warn;
			result.message = "更新失败";
		}
		return result;
	}

	@Remark("根据分组名称判断优惠券是否存在")
	@RequestMapping(value = "/svcCoupon/exist/by/name", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> existCouponByName(@RequestBody SvcCoupon svcCoupon) {
		Result<Boolean> result = Result.newOne();
		result.data = salePrmtService.existSvcCouponByGroupName(svcCoupon.getName());
		return result;
	}

	@Remark("删除优惠券 假删")
	@RequestMapping(value = "/svcCoupon/mark/deleted/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateSvcCouponByidAndDeleted(@RequestBody MapContext requestDate) {
		Result<Integer> result = Result.newOne();
		Integer id = requestDate.getTypedValue("id", Integer.class);
		Boolean deleted = requestDate.getTypedValue("deleted", Boolean.class);
		//
		boolean ok = salePrmtService.updateSvcCouponForDeleted(id, deleted);
		if (ok) {
			result.data = id;
			result.message = "更新成功";
		} else {
			result.type = Type.warn;
			result.message = "更新失败";
		}
		return result;
	}

	@Remark("启禁用优惠券")
	@RequestMapping(value = "/svcCoupon/disabled/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updatesvcCouponByidAndDisabled(@RequestBody MapContext requestDate) {
		Result<Integer> result = Result.newOne();
		Integer id = requestDate.getTypedValue("id", Integer.class);
		Boolean disabled = requestDate.getTypedValue("disabled", Boolean.class);
		//
		boolean ok = salePrmtService.updateSvcCouponForDisabled(id, disabled);
		if (ok) {
			result.data = id;
			result.message = "更新成功";
		} else {
			result.type = Type.warn;
			result.message = "更新失败";
		}
		return result;
	}

	// -------------------------------促销标签设置-----------------------------------

	@Remark("产品列表中打开的促销标签页面")
	@RequestMapping(value = "/prmtTag/list/for/product/jsp", method = RequestMethod.GET)
	public String toPrmtTagPageForProduct() {
		return "market/prmtTagList-product";
	}

	@Remark("服务列表中打开的促销标签页面")
	@RequestMapping(value = "/prmtTag/list/for/svc/jsp", method = RequestMethod.GET)
	public String toPrmtTagPageForSvc() {
		return "market/prmtTagList-svc";
	}

	@Remark("促销标签分页查询")
	@RequestMapping(value = "/prmtTag/list/get/by/filter", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<PrmtTag> getPrmtTagsByPage(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<PrmtTag> paginatedList = salePrmtService.getPrmtTagsByFilter(paginatedFilter);
		JqGridPage<PrmtTag> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	@Remark("促销标签产品列表页面")
	@RequestMapping(value = "/prmtTag/product/list/jsp", method = RequestMethod.GET)
	public String toPrmtTagProductsPage() {
		return "market/prmtTagProductList";
	}

	@Remark("促销标签服务列表页面")
	@RequestMapping(value = "/prmtTag/svc/list/jsp", method = RequestMethod.GET)
	public String toPrmtTagSvcsPage() {
		return "market/prmtTagSvcList";
	}

	@Remark("获取所有促销标签")
	@RequestMapping(value = "/prmtTag/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<PrmtTag>> getPrmtTags(HttpServletRequest request) {
		Result<List<PrmtTag>> result = Result.newOne();
		//
		List<PrmtTag> dataList = salePrmtService.getPrmtTags();
		result.data = dataList;
		//
		return result;
	}

	@Remark("促销标签货品分页查询")
	@RequestMapping(value = "/prmtTag/product/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<PrmtTagGoods> getPrmtTagsProductsByPage(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<PrmtTagGoods> paginatedList = salePrmtService.getPrmtTagProductsByFilter(paginatedFilter);
		JqGridPage<PrmtTagGoods> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	@Remark("促销标签服务分页查询")
	@RequestMapping(value = "/prmtTag/svc/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<PrmtTagSvc> getPrmtTagsSvcxsByPage(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<PrmtTagSvc> paginatedList = salePrmtService.getPrmtTagSvcsByFilter(paginatedFilter);
		JqGridPage<PrmtTagSvc> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	@Remark("更新促销标签货品序号")
	@RequestMapping(value = "/prmtTag/product/seqNo/update/do", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Result<?> updatePrmtTagProductSeqNo(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		List<Map<String, Object>> tagProductSeqNos = requestData.getTypedValue("tagProductSeqNos", TypeUtil.Types.StringObjectMapList.getClass());
		//
		result.message = "更新成功";
		boolean ok = salePrmtService.updatePrmtTagProductSeqNos(tagProductSeqNos);
		if (!ok) {
			result.type = Type.warn;
			result.message = "更新失败";
		}
		return result;
	}

	@Remark("更新促销标签货品序号")
	@RequestMapping(value = "/prmtTag/svc/seqNo/update/do", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Result<?> updatePrmtTagSvcSeqNo(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		List<Map<String, Object>> tagSvcSeqNos = requestData.getTypedValue("tagSvcSeqNos", TypeUtil.Types.StringObjectMapList.getClass());
		//
		result.message = "更新成功";
		boolean ok = salePrmtService.updatePrmtTagSvcSeqNos(tagSvcSeqNos);
		if (!ok) {
			result.type = Type.warn;
			result.message = "更新失败";
		}
		return result;
	}

	// ----------------------------------------------e卡活动------------------------------------------------------
	@Remark("e卡活动页面")
	@RequestMapping(value = "/ecardAct/list/jsp", method = RequestMethod.GET)
	public String toEcardActJsp() {
		return "market/cardActList";
	}
	
	@Remark("e卡活动页面-店铺")
	@RequestMapping(value = "/ecardAct/list/jsp/-shop", method = RequestMethod.GET)
	public String toShopEcardActJsp() {
		return "shop/cardActList";
	}

	@Remark("选择优惠券页面")
	@RequestMapping(value = "/ecardAct/bind/coupon/jsp", method = RequestMethod.GET)
	public String toEcardActForCouponJsp() {
		return "market/selectCoupon";
	}

	@Remark("分页查询e卡活动")
	@RequestMapping(value = "/ecardAct/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<EcardAct> getEcardActs(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<EcardAct> paginatedList = salePrmtService.getEcardActByFilter(paginatedFilter);
		JqGridPage<EcardAct> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	@Remark("删除e卡活动 假删")
	@RequestMapping(value = "/ecardAct/deleted/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateEcardActForDeleted(@RequestBody EcardAct ecardAct) {
		Result<Integer> result = Result.newOne();
		//
		boolean ok = salePrmtService.updateEcardActForDeleted(ecardAct);
		if (ok) {
			result.data = ecardAct.getId();
			result.message = "更新成功";
		} else {
			result.type = Type.warn;
			result.message = "更新失败";
		}
		return result;
	}

	@Remark("启禁e卡活动")
	@RequestMapping(value = "/ecardAct/disabled/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateEcardActForDisabled(@RequestBody EcardAct ecardAct) {
		Result<Integer> result = Result.newOne();
		//
		boolean ok = salePrmtService.updateEcardActForDisabled(ecardAct);
		if (ok) {
			result.data = ecardAct.getId();
			result.message = "更新成功";
		} else {
			result.type = Type.warn;
			result.message = "更新失败";
		}
		return result;
	}

	@Remark("保存e卡活动")
	@RequestMapping(value = "/ecardAct/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> saveEcardAct(@RequestBody EcardAct ecardAct) {
		Result<Integer> result = Result.newOne();
		// 保存常见问题
		boolean ok = salePrmtService.saveEcardAct(ecardAct);
		if (ok) {
			result.data = ecardAct.getId();
			result.message = "保存成功";
		} else {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}

	@Remark("更新e卡活动")
	@RequestMapping(value = "/ecardAct/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateEcardAct(@RequestBody EcardAct ecardAct) {
		Result<Integer> result = Result.newOne();
		// 保存常见问题
		boolean ok = salePrmtService.updateEcardAct(ecardAct);
		if (ok) {
			result.data = ecardAct.getId();
			result.message = "保存成功";
		} else {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}

	@Remark("验证e卡活动名称")
	@RequestMapping(value = "/ecardAct/exist/by/name", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> existEcardActByName(@RequestBody EcardAct ecardAct) {
		Result<Boolean> result = Result.newOne();
		// 保存常见问题
		boolean ok = salePrmtService.existEcardActByName(ecardAct.getName());
		if (ok) {
			result.data = ok;
			result.message = "验证成功";
		} else {
			result.type = Type.warn;
			result.message = "验证失败";
		}
		return result;
	}
	
	// --------------------------------------- 活动----------------------------------------------
	/**
	 * 活动管理页面
	 * 
	 * @author 郝江奎
	 * @date 2016年1月28日 下午2:27:05
	 * 
	 * @return
	 */
	@Remark("活动管理页面")
	@RequestMapping(value = "/activity/list/jsp/-mall", method = RequestMethod.GET)
	public String toMallActivityJsp() {
		return "market/activity-mall";
	}
	
	/**
	 * 活动消息页面
	 * 
	 * @author 郝江奎
	 * @date 2016年1月28日 下午2:27:22
	 * 
	 * @return
	 */
	@Remark("活动消息页面")
	@RequestMapping(value = "/activity/list/jsp/-shop", method = RequestMethod.GET)
	public String toShopActivityJsp() {
		return "market/activity-shop";
	}
	
	/**
	 * 分页查询活动
	 * 
	 * @author 郝江奎
	 * @date 2016年1月28日 下午19:09:00
	 * 
	 * @param request
	 * @return JqGridPage<activity>
	 */
	@Remark("分页查询活动")
	@RequestMapping(value = "/activity/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<Activity> listUserCar(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<Activity> paginatedList = activityService.getActivitysByFilter(paginatedFilter);
		//
		JqGridPage<Activity> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 添加活动
	 * 
	 * @author 郝江奎
	 * @date 2016-1-28 下午16:01:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("增加活动")
	@RequestMapping(value = "/activity/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> addActivity(HttpServletRequest request, @RequestBody Activity activity) {
		Result<?> result = Result.newOne();
		boolean ok = false;
		//
		UserContext userContext = getUserContext(request);
		activity.setCreatorId(userContext.getUserId());
		activity.setCreatorName(userContext.getUserName());
		activity.setCreateTime(new Date());
		
		ok = activityService.saveActivity(activity);
		//
		if (ok) {
			result.message = "添加成功!";
		} else {
			result.type = Type.warn;
			result.message = "添加失败";
		}

		return result;
	}

	/**
	 * 修改活动
	 * 
	 * @author 郝江奎
	 * @date 2016-1-23 上午15:01:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("修改活动")
	@RequestMapping(value = "/activity/edit/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> editActivity(HttpServletRequest request, @RequestBody Activity activity) {
		Result<?> result = Result.newOne();
		boolean ok = false;
		ok = activityService.saveActivity(activity);
		//
		if (ok) {
			result.message = "修改成功!";
		} else {
			result.type = Type.warn;
			result.message = "修改失败";
		}

		return result;
	}
	
	/**
	 * 活动发布停用
	 * 
	 * @author 郝江奎
	 * @date 2016年1月29日 上午10:26:14
	 * 
	 * @param request
	 * @param activity
	 * @return Result<?>
	 */
	@Remark("活动发布停用")
	@RequestMapping(value = "/activity/isStatus/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> changeIsStatus(HttpServletRequest request, @RequestBody Activity activity) {
		Result<?> result = Result.newOne();
		Integer status = activity.getStatus();
		if (status == 0 || status == 2) {
			activity.setStatus(1);
			activity.setPubTime(new Date());
		} else {
			activity.setStatus(2);
			activity.setEndTime(new Date());
		}
		boolean flag = activityService.saveActivity(activity);
		if (flag) {
			result.message = "操作成功";
		} else {
			result.message = "操作失败";
			result.type = Result.Type.error;
		}
		return result;
	}
	
	/**
	 * 删除活动
	 * 
	 * @author 郝江奎
	 * @date 2016-1-23 上午15:01:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("删除活动")
	@RequestMapping(value = "/activity/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> delActivity(HttpServletRequest request, @RequestBody Activity activity) {
		Result<?> result = Result.newOne();
		boolean ok = false;
		ok = activityService.deleteActivityById(activity.getId());
		//
		if (ok) {
			result.message = "删除成功!";
		} else {
			result.type = Type.warn;
			result.message = "删除失败";
		}

		return result;
	}
	
}
