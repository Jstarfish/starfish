package priv.starfish.mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.service.CarSvcService;
import priv.starfish.mall.svcx.entity.SvcGroup;
import priv.starfish.mall.svcx.entity.SvcPack;
import priv.starfish.mall.svcx.entity.Svcx;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Remark("车辆服务相关信息")
@Controller
@RequestMapping(value = "/carSvc")
public class CarSvcController extends BaseController {
	@Resource
	CarSvcService carSvcService;
	/**
	 * 跳转车辆服务页面
	 * @author 李超杰
	 * @date 2015年10月16日 下午4:00:18
	 * 
	 * @param
	 * @return
	 */
	@Remark("跳转车辆服务页面")
	@RequestMapping(value = "/list/jsp", method = RequestMethod.GET)
	public String tocarSvcList() {
		return "carSvc/carSvc";
	}
	
	/**
	 * 
	 * 获取车辆服务分类列表
	 * @author 李超杰
	 * @date 2015年10月16日 上午10:03:56
	 * 
	 * @param request
	 * @return
	 */
	@Remark("获取车辆服务分组列表")
	@RequestMapping(value = "/svcGroup/list/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<SvcGroup>> getSvcGroups(HttpServletRequest request){
		Result<List<SvcGroup>> result = Result.newOne();
		try {
			result.data=carSvcService.getCarSvcGroupsFrout();
		} catch (Exception e) {
			result.type = Type.warn;
			result.message="网络异常，请稍后重试!";
		}
		
		return result;
	}
	
	/**
	 * 获取车辆服务列表
	 * @author 李超杰
	 * @date 2015年10月16日 下午1:42:15
	 * 
	 * @param request
	 *  requestData
	 *  session
	 * @return
	 */
	@Remark("获取车辆服务列表")
	@RequestMapping(value = "/list/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<Svcx>> getCarSvcs(HttpServletRequest request){
		Result<List<Svcx>> result = Result.newOne();
		try {
			result.data=carSvcService.getCarSvcs();
		} catch (Exception e) {
			result.type = Type.warn;
		}
		return result;
	}
	
	@Remark("跳转车辆服务套餐列表页面")
	@RequestMapping(value = "/svc/pack/list/jsp", method = RequestMethod.GET)
	public String toSvcPackList() {
		return "carSvc/packList";
	}
	
	/**
	 * 分页获取服务套餐列表
	 * 
	 * @author 邓华锋
	 * @date 2016年1月22日 上午9:47:16
	 * 
	 * @return
	 */
	@Remark("分页获取服务套餐列表")
	@RequestMapping(value = "/svc/pack/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<PaginatedList<SvcPack>> getSvcPackList(@RequestBody PaginatedFilter paginatedFilter) {
		Result<PaginatedList<SvcPack>> result = Result.newOne();
		MapContext mapContext = paginatedFilter.getFilterItems();
		mapContext.put("disabled", false);
		mapContext.put("isValidTime", true);
		PaginatedList<SvcPack> paginatedList = carSvcService.getSvcPacksByFilter(paginatedFilter);
		result.data = paginatedList;
		return result;
	}
	/**
	 * 获取服务套餐详情
	 * 
	 * @author 邓华锋
	 * @date 2016年1月22日 上午11:55:55
	 * 
	 * @param request
	 * @param svcPack
	 * @return
	 */
	@Remark("获取服务套餐详情")
	@RequestMapping(value = "/svc/pack/detail/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SvcPack> getSvcPackDetail(HttpServletRequest request,@RequestBody SvcPack svcPack) {
		Result<SvcPack> result = Result.newOne();
		if(svcPack.getId()!=null){
			result.data=carSvcService.getSvcPackById(svcPack.getId());
		}else{
			result.type = Type.warn;
			result.message="获取服务套餐失败";
		}
		return result;
	}
}
