package priv.starfish.mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.exception.XRuntimeException;
import priv.starfish.common.model.*;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.service.CarSvcService;
import priv.starfish.mall.service.SalePrmtService;
import priv.starfish.mall.shop.entity.ShopSvc;
import priv.starfish.mall.svcx.entity.SvcGroup;
import priv.starfish.mall.svcx.entity.SvcKind;
import priv.starfish.mall.svcx.entity.SvcPack;
import priv.starfish.mall.svcx.entity.Svcx;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Remark("汽车服务相关处理")
@Controller
@RequestMapping(value = "/carSvc")
public class CarSvcController extends BaseController {

	@Resource
	private CarSvcService carSvcService;

	@Resource
	private SalePrmtService salePrmtService;

	// -----------------------------------车辆服务分组------------------
	/**
	 * 
	 * 车辆服务分组页面
	 * 
	 * @author 李超杰
	 * @date 2015年10月12日 下午1:28:54
	 * 
	 * @return
	 */
	@Remark("车辆服务分组页面")
	@RequestMapping(value = "/svcGroup/list/jsp", method = RequestMethod.GET)
	public String toFaqGroupList() {
		return "carSvc/carSvcGroupList";
	}

	/**
	 * 
	 * 分页获取车辆服务分组列表
	 * 
	 * @author 李超杰
	 * @date 2015年10月12日 下午6:46:26
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页获取车辆服务分组列表")
	@RequestMapping(value = "/svcGroup/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SvcGroup> getCarSvcGroupList(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		PaginatedList<SvcGroup> paginatedList = carSvcService.getCarSvcGroupsByFilter(paginatedFilter);
		JqGridPage<SvcGroup> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 根据分组名称判断分类是否存在
	 * 
	 * @author 李超杰
	 * @date 2015年10月14日 下午7:13:22
	 * 
	 * @param carSvcGroup
	 * @return
	 */
	@Remark("根据分组名称判断分类是否存在")
	@RequestMapping(value = "/svcGroup/exist/by/name", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> existFaqCatByName(@RequestBody SvcGroup carSvcGroup) {
		Result<Boolean> result = Result.newOne();
		result.data = carSvcService.existCarSvcGroupByNameAndKindId(carSvcGroup.getName(), carSvcGroup.getKindId());
		return result;
	}

	/**
	 * 保存车辆服务分组
	 * 
	 * @author 李超杰
	 * @date 2015年10月12日 下午5:00:08
	 * 
	 * @return
	 */
	@Remark("保存车辆服务分组")
	@RequestMapping(value = "/svcGroup/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> saveCarSvcGroup(@RequestBody SvcGroup carSvcGroup) {
		Result<Integer> result = Result.newOne();
		// 保存常见问题分组
		boolean ok = carSvcService.saveCarSvcGroup(carSvcGroup);
		if (ok) {
			result.data = carSvcGroup.getId();
			result.message = "保存成功";
		} else {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}

	/**
	 * 
	 * 修改车辆服务分组
	 * 
	 * @author 李超杰
	 * @date 2015年10月12日 下午5:03:45
	 * 
	 * @param carSvcGroup
	 * @return
	 */
	@Remark("修改车辆服务分组")
	@RequestMapping(value = "/svcGroup/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateCarSvcGroup(@RequestBody SvcGroup carSvcGroup) {
		Result<Integer> result = Result.newOne();
		// 修改常见问题分组
		boolean ok = carSvcService.updateCarSvcGroup(carSvcGroup);
		if (ok) {
			result.message = "保存成功";
		} else {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}

	/**
	 * 根据ID删除车辆服务分组
	 * 
	 * @author MCIUJavaDept
	 * @date 2015年10月12日 下午5:04:02
	 * 
	 * @param carSvcGroup
	 * @return
	 */
	@Remark("删除车辆服务分组(假删)")
	@RequestMapping(value = "/svcGroup/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> updateSvcGroupFordeleted(@RequestBody SvcGroup carSvcGroup) {
		Result<?> result = Result.newOne();
		List<Svcx> svcList = carSvcService.getCarSvcsByGroupId(carSvcGroup.getId());
		if (svcList != null && svcList.size() > 0) {
			result.type = Type.warn;
			result.message = "请先删除服务！";
		} else {
			boolean ok = carSvcService.updateSvcGroupFordeleted(carSvcGroup.getId());
			if (ok) {
				result.message = "删除成功";
			} else {
				result.type = Type.warn;
				result.message = "删除失败";
			}
		}
		return result;
	}

	// -----------------------------------车辆服务------------------
	/**
	 * 
	 * 车辆服务页面
	 * 
	 * @author 李超杰
	 * @date 2015年10月13日 下午3:03:32
	 * 
	 * @return
	 */
	@Remark("车辆服务页面")
	@RequestMapping(value = "/list/jsp/-mall", method = RequestMethod.GET)
	public String toCarSvcList() {
		return "carSvc/carSvcList";
	}

	/**
	 * 分页获取车辆服务列表
	 * 
	 * @author 李超杰
	 * @date 2015年10月13日 下午3:04:58
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页获取车辆服务列表")
	@RequestMapping(value = "/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<Svcx> getCarSvcList(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntityId(AuthScope.shop.name());
		if (shopId != null) {
			MapContext filters = paginatedFilter.getFilterItems();
			filters.put("shopId", shopId);
		}
		//
		PaginatedList<Svcx> paginatedList = carSvcService.getCarSvcsByFilter(paginatedFilter);
		JqGridPage<Svcx> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 保存车辆服务
	 * 
	 * @author 李超杰
	 * @date 2015年10月13日 下午3:05:36
	 * 
	 * @return
	 */
	@Remark("保存车辆服务")
	@RequestMapping(value = "/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> saveCarSvc(@RequestBody Svcx carSvc) {
		Result<Integer> result = Result.newOne();
		// 保存常见问题
		boolean ok = carSvcService.saveCarSvc(carSvc);
		if (ok) {
			result.data = carSvc.getId();
			result.message = "保存成功";
		} else {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}

	/**
	 * 修改车辆服务
	 * 
	 * @author 李超杰
	 * @date 2015年10月13日 下午3:06:35
	 * 
	 * @return
	 */
	@Remark("修改车辆服务")
	@RequestMapping(value = "/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateCarSvc(@RequestBody Svcx carSvc) {
		Result<Integer> result = Result.newOne();
		// 修改常见问题分组
		boolean ok = carSvcService.updateCarSvc(carSvc);
		if (ok) {
			result.message = "保存成功";
		} else {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}

	/**
	 * 删除车辆服务
	 * 
	 * @author 李超杰
	 * @date 2015年10月13日 下午3:07:16
	 * 
	 * @return
	 */
	@Remark("删除车辆服务(假删)")
	@RequestMapping(value = "/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> updateCarSvcForDeleted(@RequestBody Svcx carSvc) {
		Result<?> result = Result.newOne();
		boolean ok = carSvcService.updateCarSvcForDeleted(carSvc.getId());
		if (ok) {
			result.message = "删除成功";
		} else {
			result.type = Type.warn;
			result.message = "删除失败";
		}
		return result;
	}

	/**
	 * 获取车辆服务分组列表
	 * 
	 * @author 李超杰
	 * @date 2015年10月13日 下午18:11:06
	 * 
	 * @param carSvc
	 * @return
	 */
	@Remark("获取车辆服务分组列表")
	@RequestMapping(value = "/svcGroup/selectList/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SelectList> getCarSvcGroupList(@RequestBody Svcx carSvc) {
		Result<SelectList> result = Result.newOne();
		SelectList selectList = SelectList.newOne();
		List<SvcGroup> listGroup = carSvcService.getCarSvcGroups();
		if (null != listGroup && listGroup.size() > 0) {
			for (SvcGroup carSvcGroup : listGroup) {
				selectList.addItem(Integer.toString(carSvcGroup.getId()), carSvcGroup.getName());
			}
		}
		selectList.setDefaultValue("");
		result.data = selectList;
		return result;
	}

	/**
	 * 获取车辆服务列表
	 * 
	 * @author 李超杰
	 * @date 2015年10月16日 下午1:42:15
	 * 
	 * @param request
	 * @return
	 */
	@Remark("获取车辆服务列表")
	@RequestMapping(value = "/list/get/-shop", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<Svcx>> getSaleOrderSvcs(HttpServletRequest request) {
		Result<List<Svcx>> result = Result.newOne();
		try {
			result.data = carSvcService.getCarSvcs();
		} catch (Exception e) {
			result.type = Type.warn;
			result.message = "网络异常，请稍后重试!";
		}
		return result;
	}

	@Remark("根据分组名称判断分类是否存在")
	@RequestMapping(value = "/svc/exist/by/name", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> existSvcxByName(@RequestBody Svcx svcx) {
		Result<Boolean> result = Result.newOne();
		result.data = carSvcService.existCarSvcByGroupIdAndName(svcx.getGroupId(), svcx.getName());
		return result;
	}

	// ---------------------------商户下服务-----------------------------------------

	/**
	 * 
	 * 店铺下的服务列表
	 * 
	 * @author guoyn
	 * @date 2015年10月15日 下午6:06:06
	 * 
	 * @return String
	 */
	@Remark("车辆服务页面")
	@RequestMapping(value = "/list/jsp/-shop", method = RequestMethod.GET)
	public String toShopCarSvcList() {
		return "goods/carSvcList-shop";
	}

	// -----------------------------------------------获取车辆种类---------------------------------------
	/**
	 * 获取车辆服务分组列表
	 * 
	 * @author 李超杰
	 * @date 2015年10月13日 下午18:11:06
	 * 
	 * @return
	 */
	@Remark("获取车辆服务种类列表")
	@RequestMapping(value = "/svcKind/selectList/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SelectList> getSvcKindList() {
		Result<SelectList> result = Result.newOne();
		SelectList selectList = SelectList.newOne();
		List<SvcKind> listKind = carSvcService.getSvcKindsByFilter();
		if (null != listKind && listKind.size() > 0) {
			for (SvcKind svcKind : listKind) {
				selectList.addItem(Integer.toString(svcKind.getId()), svcKind.getName());
			}
		}
		selectList.setDefaultValue("");
		result.data = selectList;
		return result;
	}

	// -----------------------------------车辆服务套餐------------------
	/**
	 * 
	 * 车辆服务套餐页面
	 * 
	 * @author 李超杰
	 * @date 2015年10月12日 下午1:28:54
	 * 
	 * @return
	 */
	@Remark("车辆服务套餐页面")
	@RequestMapping(value = "/svcPack/list/jsp", method = RequestMethod.GET)
	public String toSvcPackJsp() {
		return "carSvc/svcPackList";
	}

	/**
	 * 
	 * 车辆服务套餐页面
	 * 
	 * @author 李超杰
	 * @date 2015年10月12日 下午1:28:54
	 * 
	 * @return
	 */
	@Remark("选择服务")
	@RequestMapping(value = "/bind/svc/jsp", method = RequestMethod.GET)
	public String toBindSvcJsp() {
		return "carSvc/checkSvcx";
	}

	/**
	 * 分页获取车辆服务套餐列表
	 * 
	 * @author 李超杰
	 * @date 2015年10月13日 下午3:04:58
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页获取车辆服务套餐列表")
	@RequestMapping(value = "/pack/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SvcPack> getSvcPackList(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		PaginatedList<SvcPack> paginatedList = carSvcService.getSvcPacksByFilter(paginatedFilter);
		JqGridPage<SvcPack> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	@Remark("保存服务套餐")
	@RequestMapping(value = "/pack/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> saveSvcPackItem(@RequestBody SvcPack svcPack) {
		Result<Integer> result = Result.newOne();
		// 保存常见问题
		boolean ok = carSvcService.saveSvcPack(svcPack);
		if (ok) {
			result.data = svcPack.getId();
			result.message = "保存成功";
		} else {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}

	@Remark("修改服务套餐")
	@RequestMapping(value = "/pack/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateSvcPack(@RequestBody SvcPack svcPack) {
		Result<Integer> result = Result.newOne();
		// 修改常见问题分组
		boolean ok = carSvcService.updateSvcPack(svcPack);
		if (ok) {
			result.message = "修改成功";
		} else {
			result.type = Type.warn;
			result.message = "修改失败";
		}
		return result;
	}

	@Remark("上下架更新")
	@RequestMapping(value = "/pack/disabled/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> updatesvcPackByDisabled(@RequestBody SvcPack svcPack) {
		Result<Boolean> result = Result.newOne();
		// 修改常见问题分组
		boolean ok = carSvcService.updateSvcPackForDisabled(svcPack);
		if (ok) {
			result.message = "修改成功";
		} else {
			result.type = Type.warn;
			result.message = "修改失败";
		}
		return result;
	}

	@Remark("删除服务套餐(假删)")
	@RequestMapping(value = "/pack/deleted/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> updateSvcPackForDeleted(@RequestBody SvcPack svcPack) {
		Result<Boolean> result = Result.newOne();
		boolean ok = carSvcService.updateSvcPackForDeleted(svcPack);
		if (ok) {
			result.message = "删除成功";
		} else {
			result.type = Type.warn;
			result.message = "删除失败";
		}
		return result;
	}

	@Remark("根据分类id和名称判断套餐是否存在")
	@RequestMapping(value = "/pack/exist/by/name", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> existSvcPackByName(@RequestBody SvcPack svcPack) {
		Result<Boolean> result = Result.newOne();
		result.data = carSvcService.existSvcPackKindIdByName(svcPack.getKindId(), svcPack.getName());
		return result;
	}
	// ------------------------------------促销标签----------------------------------------------------------------

	@Remark("保存货品促销标签")
	@RequestMapping(value = "/prmtTag/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> savePrmtTag(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		//
		Integer svcId = requestData.getTypedValue("svcId", Integer.class);
		Integer prmtTagId = requestData.getTypedValue("prmtTagId", Integer.class);
		//
		result.message = "保存成功";
		boolean ok = salePrmtService.savePrmtTagSvc(prmtTagId, svcId);
		//
		if (!ok) {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}

	@Remark("删除货品促销标签")
	@RequestMapping(value = "/prmtTag/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deletePrmtTag(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		//
		Integer svcId = requestData.getTypedValue("svcId", Integer.class);
		Integer tagId = requestData.getTypedValue("tagId", Integer.class);
		//
		result.message = "删除成功";
		boolean ok = salePrmtService.deletePrmtTagSvc(tagId, svcId);
		//
		if (!ok) {
			result.type = Type.warn;
			result.message = "删除失败";
		}
		//
		return result;
	}

	// ----------------------------------------店铺服务----------------------------------------------------------

	@Remark("分页获取店铺车辆服务列表")
	@RequestMapping(value = "/shop/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<ShopSvc> getShopCarSvcList(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		// 获取当前店铺id
		MapContext filter = paginatedFilter.getFilterItems();
		UserContext userContext = getUserContext(request);
		ScopeEntity scopeEntity = userContext.getScopeEntity();
		filter.put("shopId", scopeEntity.getId());
		//
		PaginatedList<ShopSvc> paginatedList = carSvcService.getShopCarSvcsByFilter(paginatedFilter);
		JqGridPage<ShopSvc> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	@Remark("选择汽车服务列表页面")
	@RequestMapping(value = "/list/for/shop/jsp", method = RequestMethod.GET)
	public String toPrmtTagPageForProduct() {
		return "goods/selectSvcxList";
	}

	@Remark("保存店铺服务")
	@RequestMapping(value = "/shop/svc/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> saveShopSvc(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		//
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntityId(AuthScope.shop.name());
		if (shopId == null) {
			throw new XRuntimeException("找不到当前店铺实体信息");
		}
		Integer svcId = requestData.getTypedValue("svcId", Integer.class);
		//
		result.message = "保存成功";
		boolean ok = carSvcService.saveShopSvcBySvcIdAndShopId(svcId, shopId);
		//
		if (!ok) {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}

	@Remark("根据服务id删除店铺服务")
	@RequestMapping(value = "/shop/svc/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteShopSvc(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		//
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntityId(AuthScope.shop.name());
		if (shopId == null) {
			throw new XRuntimeException("找不到当前店铺实体信息");
		}
		Integer svcId = requestData.getTypedValue("svcId", Integer.class);

		//
		result.message = "下架成功";
		boolean ok = carSvcService.deleteShopSvcBySvcIdAndShopId(svcId, shopId);
		//
		if (!ok) {
			result.type = Type.warn;
			result.message = "下架失败";
		}
		//
		return result;
	}

	@Remark("根据店铺服务id删除店铺服务")
	@RequestMapping(value = "shop/svc/delete/by/id/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteShopSvcById(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		//
		Long shopSvcId = requestData.getTypedValue("id", Long.class);

		//
		result.message = "下架成功";
		boolean ok = carSvcService.deleteShopSvcById(shopSvcId);
		//
		if (!ok) {
			result.type = Type.warn;
			result.message = "下架失败";
		}
		//
		return result;
	}

	@Remark("店铺车辆服务套餐页面")
	@RequestMapping(value = "/svcPack/list/jsp/-shop", method = RequestMethod.GET)
	public String toShopSvcPackJsp() {
		return "shop/svcPackList";
	}

}