package priv.starfish.mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.*;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.car.entity.*;
import priv.starfish.mall.service.CarService;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 与车辆相关处理的Controller
 * 
 * @author 郝江奎
 * @date 2016年1月21日 下午17:29:11
 *
 */
@Controller
@RequestMapping("/car")
public class CarController extends BaseController {

	@Resource
	private CarService carService;

	// --------------------------------------- 车辆品牌----------------------------------------------

	/**
	 * 查询车辆品牌by Id
	 * 
	 * @author 郝江奎
	 * @date 2016年1月21日 下午19:09:00
	 * 
	 * @param request
	 * @return CarBrand
	 */
	@Remark("查询车辆品牌")
	@RequestMapping(value = "/carBrand/get/by/id", method = RequestMethod.POST)
	@ResponseBody
	public Result<CarBrand> listCarBrand(HttpServletRequest request, @RequestParam("id") Integer carBrandId) {
		Result<CarBrand> result = Result.newOne();
		//
		CarBrand carBrand = carService.getCarBrandById(carBrandId);
		result.data = carBrand;
		//
		return result;
	}

	/**
	 * 分页查询车辆品牌
	 * 
	 * @author 郝江奎
	 * @date 2016年1月21日 下午19:09:00
	 * 
	 * @param request
	 * @return JqGridPage<CarBrand>
	 */
	@Remark("分页查询车辆品牌")
	@RequestMapping(value = "/carBrand/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<CarBrand> listCarBrand(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<CarBrand> paginatedList = carService.getCarBrandsByFilter(paginatedFilter);
		//
		JqGridPage<CarBrand> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 添加车辆品牌
	 * 
	 * @author 郝江奎
	 * @date 2016-1-22 上午12:01:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("增加车辆品牌")
	@RequestMapping(value = "/carBrand/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> addCarBrand(HttpServletRequest request, @RequestBody CarBrand carBrand) {
		Result<?> result = Result.newOne();
		boolean ok = false;
		ok = carService.saveCarBrand(carBrand);
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
	 * 车辆品牌禁用启用
	 * 
	 * @author 郝江奎
	 * @date 2016年1月22日 下午12:26:14
	 * 
	 * @param request
	 * @return Result<?>
	 */
	@Remark("车辆品牌禁用启用")
	@RequestMapping(value = "/carBrand/isDisabled/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> changeCarBrandDisabled(HttpServletRequest request, @RequestBody CarBrand carBrand) {
		Result<?> result = Result.newOne();
		boolean disabled = carBrand.getDisabled() == false ? true : false;
		boolean flag = carService.updateCarBrandDisableState(carBrand.getId(), disabled);
		if (flag) {
			result.message = "操作成功";
		} else {
			result.message = "操作失败";
			result.type = Result.Type.error;
		}
		return result;
	}

	/**
	 * 获取车辆品牌列表
	 * 
	 * @author 郝江奎
	 * @date 2016年1月22日上午10:06:06
	 * 
	 * @return
	 */
	@Remark("获取车辆品牌列表")
	@RequestMapping(value = "/carBrand/selectList/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SelectList> getCarBrandList() {
		Result<SelectList> result = Result.newOne();
		SelectList selectList = SelectList.newOne();
		List<CarBrand> list = carService.getCarBrands(false);
		selectList.setUnSelectedItem("", "- 请选择 -");
		if (list.size() > 0) {
			for (CarBrand carBrand : list) {
				selectList.addItem(Integer.toString(carBrand.getId()), carBrand.getName());
			}
		}
		selectList.setDefaultValue("");
		result.data = selectList;
		return result;
	}

	// --------------------------------------- 车辆系列----------------------------------------------

	/**
	 * 分页查询车辆系列
	 * 
	 * @author 郝江奎
	 * @date 2016年1月21日 下午19:09:00
	 * 
	 * @param request
	 * @return JqGridPage<CarSerial>
	 */
	@Remark("分页查询车辆系列")
	@RequestMapping(value = "/carSerial/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<CarSerial> listCarSerial(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<CarSerial> paginatedList = carService.getCarSerialsByFilter(paginatedFilter);
		//
		JqGridPage<CarSerial> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 添加车辆系列
	 * 
	 * @author 郝江奎
	 * @date 2016-1-22 上午12:01:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("增加车辆系列")
	@RequestMapping(value = "/carSerial/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> addCarSerial(HttpServletRequest request, @RequestBody CarSerial carSerial) {
		Result<?> result = Result.newOne();
		boolean ok = false;
		ok = carService.saveCarSerial(carSerial);
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
	 * 车辆系列禁用启用
	 * 
	 * @author 郝江奎
	 * @date 2016年1月22日 下午14:26:14
	 * 
	 * @param request
	 * @param carSerial
	 * @return Result<?>
	 */
	@Remark("车辆系列禁用启用")
	@RequestMapping(value = "/carSerial/isDisabled/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> changeCarSerialDisabled(HttpServletRequest request, @RequestBody CarSerial carSerial) {
		Result<?> result = Result.newOne();
		boolean disabled = carSerial.getDisabled() == false ? true : false;
		boolean flag = carService.updateCarSerialDisableState(carSerial.getId(), disabled);
		if (flag) {
			result.message = "操作成功";
		} else {
			result.message = "操作失败";
			result.type = Result.Type.error;
		}
		return result;
	}

	/**
	 * 获取车辆系列列表
	 * 
	 * @author 郝江奎
	 * @date 2016年1月22日上午10:06:06
	 * 
	 * @return
	 */
	@Remark("获取车辆系列列表")
	@RequestMapping(value = "/carSerial/selectList/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SelectList> getCarSerialList(@RequestParam(value = "brandId", required = false) Integer brandId) {
		Result<SelectList> result = Result.newOne();
		SelectList selectList = SelectList.newOne();
		List<CarSerial> list = carService.getCarSerials(brandId, false);
		selectList.setUnSelectedItem("", "- 请选择 -");
		if (list.size() > 0) {
			for (CarSerial carSerial : list) {
				selectList.addItem(Integer.toString(carSerial.getId()), carSerial.getName());
			}
		}
		selectList.setDefaultValue("");
		result.data = selectList;
		return result;
	}

	/**
	 * 获取车辆系列列表
	 * 
	 * @author 郝江奎
	 * @date 2016年1月22日上午10:06:06
	 * 
	 * @return
	 */
	@Remark("获取车辆系列列表")
	@RequestMapping(value = "/carSerial/get/by/id", method = RequestMethod.POST)
	@ResponseBody
	public Result<CarSerial> getCarSerialById(@RequestBody CarSerial carSerial) {
		Result<CarSerial> result = Result.newOne();
		result.data = carService.getCarSerialById(carSerial.getId());
		return result;
	}

	// --------------------------------------- 车辆型号----------------------------------------------
	/**
	 * 分页查询车辆型号
	 * 
	 * @author 郝江奎
	 * @date 2016年1月21日 下午19:09:00
	 * 
	 * @param request
	 * @return JqGridPage<CarBrand>
	 */
	@Remark("分页查询车辆型号")
	@RequestMapping(value = "/carModel/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<CarModel> listCarModel(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<CarModel> paginatedList = carService.getCarModelsByFilter(paginatedFilter);
		//
		JqGridPage<CarModel> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 添加车辆型号
	 * 
	 * @author 郝江奎
	 * @date 2016-1-22 上午12:01:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("增加车辆型号")
	@RequestMapping(value = "/carModel/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> addCarModel(HttpServletRequest request, @RequestBody CarModel carModel) {
		Result<?> result = Result.newOne();
		boolean ok = false;
		ok = carService.saveCarModel(carModel);
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
	 * 车辆型号禁用启用
	 * 
	 * @author 郝江奎
	 * @date 2016年1月22日 下午14:26:14
	 * 
	 * @param request
	 * @return Result<?>
	 */
	@Remark("车辆型号禁用启用")
	@RequestMapping(value = "/carModel/isDisabled/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> changeCarModelDisabled(HttpServletRequest request, @RequestBody CarModel carModel) {
		Result<?> result = Result.newOne();
		boolean disabled = carModel.getDisabled() == false ? true : false;
		boolean flag = carService.updateCarModelDisableState(carModel.getId(), disabled);
		if (flag) {
			result.message = "操作成功";
		} else {
			result.message = "操作失败";
			result.type = Result.Type.error;
		}
		return result;
	}

	/**
	 * 获取车辆型号列表
	 * 
	 * @author 郝江奎
	 * @date 2016年1月22日下午17:06:06
	 * 
	 * @return
	 */
	@Remark("获取车辆型号列表")
	@RequestMapping(value = "/carModel/selectList/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SelectList> getCarModelList(@RequestParam(value = "serialId", required = false) Integer serialId) {
		Result<SelectList> result = Result.newOne();
		SelectList selectList = SelectList.newOne();
		List<CarModel> list = carService.getCarModels(serialId, false);
		selectList.setUnSelectedItem("", "- 请选择 -");
		if (list.size() > 0) {
			for (CarModel carModel : list) {
				selectList.addItem(Integer.toString(carModel.getId()), carModel.getName());
			}
		}
		selectList.setDefaultValue("");
		result.data = selectList;
		return result;
	}

	/**
	 * 获取车辆型号列表
	 * 
	 * @author 郝江奎
	 * @date 2016年1月22日下午18:06:06
	 * 
	 * @return
	 */
	@Remark("获取车辆型号列表")
	@RequestMapping(value = "/carModel/get/by/id", method = RequestMethod.POST)
	@ResponseBody
	public Result<CarModel> getCarModelById(@RequestBody CarModel carModel) {
		Result<CarModel> result = Result.newOne();
		result.data = carService.getCarModelById(carModel.getId());
		return result;
	}

	// -------------------------------会员车辆管理-----------------------------------

	/**
	 * 查询会员车辆
	 * 
	 * @author 郝江奎
	 * @date 2016年1月21日 下午19:09:00
	 * 
	 * @param request
	 * @return List<UserCar>
	 */
	@Remark("查询会员车辆")
	@RequestMapping(value = "/userCar/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<UserCar>> listUserCar(HttpServletRequest request, @RequestBody MapContext requestData) {

		Result<List<UserCar>> result = Result.newOne();
		PaginatedFilter paginatedFilter = new PaginatedFilter();
		paginatedFilter.setFilterItems(requestData);
		// 获取会员id
		MapContext filter = paginatedFilter.getFilterItems();
		Integer userId = filter.getTypedValue("userId", Integer.class);
		if (userId == null) {
			UserContext userContext = getUserContext(request);
			userId = userContext.getUserId();
			filter.put("userId", userId);
		}
		//
		PaginatedList<UserCar> items = carService.getUserCarsByFilter(paginatedFilter);
		if (items != null) {
			result.data = items.getRows();
		}
		return result;
	}

	/**
	 * 获取用户默认的车辆
	 * 
	 * @author 郝江奎
	 * @date 2016年1月26日 下午14:39:26
	 * 
	 *            默认的车辆id
	 * @return 返回默认结果
	 */
	@Remark("获取用户默认的车辆")
	@RequestMapping(value = "/userCar/default/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<UserCar> getDefaultUserCar(HttpServletRequest request) {
		Result<UserCar> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		Integer userId = userContext.getUserId();
		boolean defaulted = true;
		result.data = carService.getUserCarDefault(userId, defaulted);
		return result;
	}

	/**
	 * 添加会员车辆
	 * 
	 * @author 郝江奎
	 * @date 2016-1-23 上午16:01:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("增加会员车辆")
	@RequestMapping(value = "/userCar/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> addUserCar(HttpServletRequest request, @RequestBody UserCar userCar) {
		Result<?> result = Result.newOne();
		Integer userId = userCar.getUserId();
		if (userId == null) {
			UserContext userContext = getUserContext(request);
			userId = userContext.getUserId();
			userCar.setUserId(userId);
		}
		List<UserCar> userCars = carService.selectByUserId(userCar.getUserId());
		if (userCars.size() < 5) {
			boolean ok = false;
			ok = carService.saveUserCar(userCar);
			//
			if (ok) {
				result.message = "添加成功!";
			} else {
				result.type = Type.warn;
				result.message = "添加失败";
			}
		}else{
			result.type = Type.warn;
			result.message = "添加失败，您添加的车辆数量已达到限制！";
		}

		return result;
	}

	/**
	 * 修改会员车辆
	 * 
	 * @author 郝江奎
	 * @date 2016-1-23 上午15:01:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("修改会员车辆")
	@RequestMapping(value = "/userCar/edit/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> editUserCar(HttpServletRequest request, @RequestBody UserCar userCar) {
		Result<?> result = Result.newOne();
		boolean ok = false;
		ok = carService.saveUserCar(userCar);
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
	 * 删除用户车辆
	 * 
	 * @author 郝江奎
	 * @date 2016年1月26日 上午10:01:38
	 * 
	 * @param id
	 * @return
	 */
	@Remark("删除用户车辆")
	@RequestMapping(value = "/userCar/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteShopImage(@RequestParam("id") Integer id) {
		Result<?> result = Result.newOne();
		result.message = carService.delUserCarById(id) ? "删除成功！" : "删除失败！";
		return result;
	}

	/**
	 * 设置用户默认车辆
	 * 
	 * @author 郝江奎
	 * @date 2016年1月26日 上午10:39:26
	 * 
	 *            设置的用户车辆id
	 * @return 返回设置结果
	 */
	@Remark("设置用户默认车辆")
	@RequestMapping(value = "/userCar/defaulted/set/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> setDefaultedLinkWay(HttpServletRequest request, @RequestParam("id") Integer userCarId) {
		Result<?> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		Integer userId = userContext.getUserId();
		result.message = carService.setUserCarDefault(userId, userCarId) ? "设置成功！" : "设置失败！";
		return result;
	}

	// -------------------------------会员车辆服务记录管理-----------------------------------
	/**
	 * 分页查询会员车辆服务记录
	 * 
	 * @author 郝江奎
	 * @date 2016年1月21日 下午19:09:00
	 * 
	 * @param request
	 * @return JqGridPage<UserCarSvcRec>
	 */
	@Remark("分页查询车辆服务记录")
	@RequestMapping(value = "/userCarSvcRec/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<UserCarSvcRec> listUserCarSvcRec(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<UserCarSvcRec> paginatedList = carService.getUserCarSvcRecsByFilter(paginatedFilter);
		//
		JqGridPage<UserCarSvcRec> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

}
