package priv.starfish.mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.*;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.goods.entity.Product;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.market.entity.SalesFloor;
import priv.starfish.mall.market.entity.SalesRegion;
import priv.starfish.mall.market.entity.SalesRegionGoods;
import priv.starfish.mall.market.entity.SalesRegionSvc;
import priv.starfish.mall.service.SalesFloorService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Remark("楼层相关处理")
@Controller
@RequestMapping(value = "/salesFloor")
public class SalesFloorController extends BaseController {

	@Resource
	private SalesFloorService salesFloorService;
	
	/**
	 * 返回销售楼层列表页面
	 * 
	 * @author 郝江奎
	 * @date 2016年1月30日 上午12:00:18
	 * 
	 * @return
	 */
	@Remark("返回销售楼层列表页面")
	@RequestMapping(value = "/list/jsp", method = RequestMethod.GET)
	public String toSalesFloorList() {
		return "salesFloor/salesFloorList";
	}

	/**
	 * 分页查询销售楼层
	 * 
	 * @author 郝江奎
	 * @date 2016年1月28日 下午19:09:00
	 * 
	 * @param request
	 * @return JqGridPage<salesFloor>
	 */
	@Remark("分页查询销售楼层")
	@RequestMapping(value = "/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SalesFloor> listSalesFloor(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<SalesFloor> paginatedList = salesFloorService.getSalesFloorsByFilter(paginatedFilter);
		//
		JqGridPage<SalesFloor> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}
	
	/**
	 * 获取楼层列表
	 * 
	 * @author 郝江奎
	 * @date 2016年1月22日上午10:06:06
	 * 
	 * @return
	 */
	@Remark("获取楼层列表")
	@RequestMapping(value = "/selectList/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SelectList> getCarBrandList(@RequestParam(value = "type", required = false) Integer type) {
		Result<SelectList> result = Result.newOne();
		SelectList selectList = SelectList.newOne();
		List<SalesFloor> list = salesFloorService.getSalesFloorByType(type);
		selectList.setUnSelectedItem("", "- 请选择 -");
		if (list.size() > 0) {
			for (SalesFloor salesFloor : list) {
				selectList.addItem(Integer.toString(salesFloor.getNo()), salesFloor.getName());
			}
		}
		selectList.setDefaultValue("");
		result.data = selectList;
		return result;
	}
	
	/**
	 * 根据no查询楼层
	 * 
	 * @author 郝江奎
	 * @date 2016-1-30 下午16:01:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("根据no查询楼层")
	@RequestMapping(value = "/get/by/no", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> getSalesFloorByNo(HttpServletRequest request, @RequestBody SalesFloor salesFloor) {
		Result<?> result = Result.newOne();

		SalesFloor salesF = salesFloorService.getSalesFloorByNo(salesFloor.getNo());
		//
		if (salesF != null) {
			if (salesF.getNo().equals(salesFloor.getId())) {
				result.type = Type.warn;
			}
			result.message = "获取成功!";
		} else {
			result.type = Type.warn;
			result.message = "暂无此楼层";
		}

		return result;
	}
	
	/**
	 * 根据Name查询楼层
	 * 
	 * @author 郝江奎
	 * @date 2016-1-30 下午16:01:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("根据Name查询楼层")
	@RequestMapping(value = "/get/by/name", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> getSalesFloorByName(HttpServletRequest request, @RequestBody SalesFloor salesFloor) {
		Result<?> result = Result.newOne();

		SalesFloor salesF = salesFloorService.getSalesFloorByName(salesFloor.getName());
		//
		if (salesF != null) {
			if (salesF.getNo().equals(salesFloor.getId())) {
				result.type = Type.warn;
			}
			result.message = "获取成功!";
		} else {
			result.type = Type.warn;
			result.message = "暂无此楼层";
		}

		return result;
	}

	/**
	 * 添加楼层
	 * 
	 * @author 郝江奎
	 * @date 2016-1-30 下午16:01:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("增加楼层")
	@RequestMapping(value = "/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> addSalesFloor(HttpServletRequest request, @RequestBody SalesFloor salesFloor) {
		Result<?> result = Result.newOne();
		boolean ok = false;

		ok = salesFloorService.saveSalesFloor(salesFloor);
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
	 * 修改楼层
	 * 
	 * @author 郝江奎
	 * @date 2016-1-30 上午15:01:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("修改楼层")
	@RequestMapping(value = "/edit/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> editSalesFloor(HttpServletRequest request, @RequestBody SalesFloor salesFloor) {
		Result<?> result = Result.newOne();
		boolean ok = false;
		ok = salesFloorService.saveSalesFloor(salesFloor);
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
	 * 楼层禁用启用
	 * 
	 * @author 郝江奎
	 * @date 2016年1月30日 下午14:26:14
	 * 
	 * @param request
	 * @return Result<?>
	 */
	@Remark("楼层禁用启用")
	@RequestMapping(value = "/isDisabled/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> changeIsDisabled(HttpServletRequest request, @RequestBody SalesFloor salesFloor) {
		Result<?> result = Result.newOne();
		boolean disabled = salesFloor.getDisabled() == false ? true : false;
		salesFloor.setDisabled(disabled);
		boolean flag = salesFloorService.saveSalesFloor(salesFloor);
		if (flag) {
			result.message = "操作成功";
		} else {
			result.message = "操作失败";
			result.type = Result.Type.error;
		}
		return result;
	}

	/**
	 * 删除楼层
	 * 
	 * @author 郝江奎
	 * @date 2016年1月30日 下午2:49:52
	 * 
	 * @param request
	 * @return
	 */
	@Remark("删除楼层")
	@RequestMapping(value = "/delete/by/no", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> delSalesFloorById(HttpServletRequest request, @RequestBody SalesFloor salesFloor) {
		Result<?> result = Result.newOne();
		boolean flag = salesFloorService.delSalesFloor(salesFloor.getNo());
		if (flag) {
			result.message = "删除成功";
		} else {
			result.message = "删除失败";
			result.type = Result.Type.error;
		}
		return result;
	}
	
	//-----------------------------分区管理-------------------------------
	/**
	 * 商品销售专区页面
	 * 
	 * @author 郝江奎
	 * @date 2016年1月30日 下午4:45:51
	 * 
	 * @return
	 */
	@Remark("商品销售专区页面")
	@RequestMapping(value = "/salesRegionGoods/list/jsp", method = RequestMethod.GET)
	public String toSalesRegionGoodsList() {
		return "salesFloor/salesRegionGoods";
	}
	
	/**
	 * 服务销售专区页面
	 * 
	 * @author 郝江奎
	 * @date 2016年1月30日 下午4:55:51
	 * 
	 * @return
	 */
	@Remark("服务销售专区页面")
	@RequestMapping(value = "/salesRegionSvc/list/jsp", method = RequestMethod.GET)
	public String toSalesRegionSvcList() {
		return "salesFloor/salesRegionSvc";
	}
	
	/**
	 * 分页查询销售专区
	 * 
	 * @author 郝江奎
	 * @date 2016年1月28日 下午19:09:00
	 * 
	 * @param request
	 * @return JqGridPage<salesRegion>
	 */
	@Remark("分页查询销售专区")
	@RequestMapping(value = "/salesRegion/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SalesRegion> listSalesRegion(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<SalesRegion> paginatedList = salesFloorService.getSalesRegionsByFilter(paginatedFilter);
		//
		JqGridPage<SalesRegion> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}
	
	/**
	 * 根据Name和floorNo查询楼层专区
	 * 
	 * @author 郝江奎
	 * @date 2016-1-30 下午16:01:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("根据Name和floorNo查询楼层专区")
	@RequestMapping(value = "/salesRegion/get/by/name/and/floorNo", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> getSalesRegionByNameAndFloorNo(HttpServletRequest request, @RequestBody SalesRegion salesRegion) {
		Result<?> result = Result.newOne();

		SalesRegion salesR = salesFloorService.getSalesRegionByNameAndFloorNo(salesRegion.getName(), salesRegion.getFloorNo());
		//
		if (salesR != null) {
			if (salesR.getId().equals(salesRegion.getId())) {
				result.type = Type.warn;
			}
			result.message = "获取成功!";
		} else {
			result.type = Type.warn;
			result.message = "暂无此楼层";
		}

		return result;
	}
	
	/**
	 * 添加专区
	 * 
	 * @author 郝江奎
	 * @date 2016-1-30 下午16:01:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("增加专区")
	@RequestMapping(value = "/salesRegion/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> addSalesRegion(HttpServletRequest request, @RequestBody SalesRegion salesRegion) {
		Result<?> result = Result.newOne();
		boolean ok = false;

		ok = salesFloorService.saveSalesRegion(salesRegion);
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
	 * 修改专区
	 * 
	 * @author 郝江奎
	 * @date 2016-1-30 下午18:01:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("修改专区")
	@RequestMapping(value = "/salesRegion/edit/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> editSalesRegion(HttpServletRequest request, @RequestBody SalesRegion salesRegion) {
		Result<?> result = Result.newOne();
		boolean ok = false;
		ok = salesFloorService.saveSalesRegion(salesRegion);
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
	 * 删除专区
	 * 
	 * @author 郝江奎
	 * @date 2016年2月2日 下午1:46:53
	 * 
	 * @param request
	 * @param salesRegion
	 * @return
	 */
	@Remark("删除专区")
	@RequestMapping(value = "/salesRegion/delete/by/id", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> delSalesRegionGoodsById(HttpServletRequest request, @RequestBody SalesRegion salesRegion) {
		Result<?> result = Result.newOne();
		boolean flag = salesFloorService.delSalesRegionByIdAndType(salesRegion);
		if (flag) {
			result.message = "删除成功";
		} else {
			result.message = "删除失败";
			result.type = Result.Type.error;
		}
		return result;
	}
	
	//-----------------------------分区商品管理-------------------------------
	
	/**
	 * 分页查询销售专区商品
	 * 
	 * @author 郝江奎
	 * @date 2016年2月1日 下午16:09:00
	 * 
	 * @param request
	 * @return JqGridPage<salesRegionGoods>
	 */
	@Remark("分页查询销售专区商品")
	@RequestMapping(value = "/salesRegionGoods/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SalesRegionGoods> listSalesRegionGoods(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<SalesRegionGoods> paginatedList = salesFloorService.getSalesRegionGoodsByFilter(paginatedFilter);
		//
		JqGridPage<SalesRegionGoods> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}
	
	/**
	 * 查询商城非专区货品列表
	 * 
	 * @author 郝江奎
	 * @date 2016年2月17日 下午4:31:04
	 * 
	 * @param request
	 * @return JqGridPage<Product>
	 */
	@Remark("查询商城非专区货品列表")
	@RequestMapping(value = "/product/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<Product> getMallProducts(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<Product> paginatedList = salesFloorService.getProductsAndFilter(paginatedFilter);
		//
		JqGridPage<Product> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}
	
	/**
	 * 批量添加专区商品
	 * 
	 * @author 郝江奎
	 * @date 2016-2-1 下午18:01:10
	 * 
	 * @param request
	 * @param requestData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Remark("批量添加专区商品")
	@RequestMapping(value = "/regionGoods/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> addRegionGoods(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		List<String> list = requestData.getTypedValue("ids", TypeUtil.Types.StringList.getClass());
		Integer regionId = requestData.getTypedValue("regionId", Integer.class);
		boolean ok = false;
		ok = salesFloorService.saveRegionGoods(list, regionId);
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
	 * 删除专区商品
	 * 
	 * @author 郝江奎
	 * @date 2016年2月2日 下午1:46:53
	 * 
	 * @param request
	 * @param salesRegionGoods
	 * @return
	 */
	@Remark("删除专区商品")
	@RequestMapping(value = "/regionGoods/delete/by/id", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> delSalesRegionGoodsById(HttpServletRequest request, @RequestBody SalesRegionGoods salesRegionGoods) {
		Result<?> result = Result.newOne();
		boolean flag = salesFloorService.delSalesRegionGoodsById(salesRegionGoods.getId());
		if (flag) {
			result.message = "删除成功";
		} else {
			result.message = "删除失败";
			result.type = Result.Type.error;
		}
		return result;
	}
	
	/**
	 * 查询专区服务
	 * 
	 * @author 郝江奎
	 * @date 2016年2月2日 下午2:31:31
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询销售专区服务")
	@RequestMapping(value = "/salesRegionSvcs/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SalesRegionSvc> listSalesRegionSvcs(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<SalesRegionSvc> paginatedList = salesFloorService.getSalesRegionSvcsByFilter(paginatedFilter);
		//
		JqGridPage<SalesRegionSvc> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}
	
	/**
	 * 批量添加专区服务
	 * 
	 * @author 郝江奎
	 * @date 2016-2-2 下午18:01:10
	 * 
	 * @param request
	 * @param requestData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Remark("批量添加专区服务")
	@RequestMapping(value = "/regionSvc/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> addRegionSvc(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		List<String> list = requestData.getTypedValue("ids", TypeUtil.Types.StringList.getClass());
		Integer regionId = requestData.getTypedValue("regionId", Integer.class);
		boolean ok = false;
		ok = salesFloorService.saveRegionSvc(list, regionId);
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
	 * 删除专区服务
	 * 
	 * @author 郝江奎
	 * @date 2016年2月2日 下午1:46:53
	 * 
	 * @param request
	 * @param salesRegionSvc
	 * @return
	 */
	@Remark("删除专区服务")
	@RequestMapping(value = "/regionSvc/delete/by/id", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> delSalesRegionSvcById(HttpServletRequest request, @RequestBody SalesRegionSvc salesRegionSvc) {
		Result<?> result = Result.newOne();
		boolean flag = salesFloorService.delSalesRegionSvcById(salesRegionSvc.getId());
		if (flag) {
			result.message = "删除成功";
		} else {
			result.message = "删除失败";
			result.type = Result.Type.error;
		}
		return result;
	}

}
