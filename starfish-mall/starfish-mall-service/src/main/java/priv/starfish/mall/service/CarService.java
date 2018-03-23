package priv.starfish.mall.service;

import java.util.List;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.car.entity.CarBrand;
import priv.starfish.mall.car.entity.CarModel;
import priv.starfish.mall.car.entity.CarSerial;
import priv.starfish.mall.car.entity.CarSerialGroup;
import priv.starfish.mall.car.entity.UserCar;
import priv.starfish.mall.car.entity.UserCarSvcRec;

public interface CarService extends BaseService {
	// ------------------- 品牌 -------------------
	CarBrand getCarBrandById(Integer id);

	CarBrand getCarBrandByName(String name);

	CarBrand getCarBrandByRefId(Integer refId);

	boolean delCarBrandById(Integer id);

	boolean saveCarBrand(CarBrand carBrand);

	List<CarBrand> getCarBrands(boolean includeDisabled);
	
	/**
	 * 查询车辆品牌
	 * 
	 * @author 郝江奎
	 * @date 2016年1月21日 下午19:17:40
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<CarBrand> getCarBrandsByFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 启用禁用车辆品牌
	 * 
	 * @author 郝江奎
	 * @date 2016年1月22日 下午12:17:40
	 * 
	 * @param id  disabled
	 * @return
	 */
	boolean updateCarBrandDisableState(Integer id, boolean disabled);

	// ------------------- 车系分组 -------------------
	CarSerialGroup getCarSerialGroupById(Integer id);

	CarSerialGroup getCarSerialGroupByBrandIdAndName(Integer brandId, String name);

	CarSerialGroup getCarSerialGroupByRefId(Integer refId);

	int delCarSerialGroupById(Integer id);

	int saveCarSerialGroup(CarSerialGroup carSerialGroup);

	// ------------------- 车系 -------------------
	CarSerial getCarSerialById(Integer id);

	CarSerial getCarSerialByBrandIdAndName(Integer brandId, String name);

	CarSerial getCarSerialByRefId(Integer refId);

	boolean delCarSerialById(Integer id);

	boolean saveCarSerial(CarSerial carSerial);

	List<CarSerial> getCarSerials(Integer brandId, boolean includeDisabled);
	
	/**
	 * 查询车系
	 * 
	 * @author 郝江奎
	 * @date 2016年1月21日 下午19:18:40
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<CarSerial> getCarSerialsByFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 启用禁用车辆系列
	 * 
	 * @author 郝江奎
	 * @date 2016年1月22日 下午12:17:40
	 * 
	 * @param id  disabled
	 * @return
	 */
	boolean updateCarSerialDisableState(Integer id, boolean disabled);

	// ------------------- 车型 -------------------
	CarModel getCarModelById(Integer id);

	CarModel getCarModelBySerialIdAndName(Integer serialId, String name);

	CarModel getCarModelByRefId(Integer refId);

	boolean delCarModelById(Integer id);

	boolean saveCarModel(CarModel carModel);

	List<CarModel> getCarModels(Integer serialId, boolean includeDisabled);
	
	/**
	 * 查询车型
	 * 
	 * @author 郝江奎
	 * @date 2016年1月21日 下午19:20:40
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<CarModel> getCarModelsByFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 启用禁用车辆车型
	 * 
	 * @author 郝江奎
	 * @date 2016年1月22日 下午12:17:40
	 * 
	 * @param id  disabled
	 * @return
	 */
	boolean updateCarModelDisableState(Integer id, boolean disabled);
	
	// ------------------- 车型 -------------------
	
	UserCar getUserCarById(Integer id);

	UserCar selectByUserIdAndModelId(Integer userId, Integer modelId);

	boolean saveUserCar(UserCar userCar);

	boolean delUserCarById(Integer id);
	
	/**
	 * 查询会员车辆
	 * 
	 * @author 郝江奎
	 * @date 2016年1月23日 下午14:20:40
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<UserCar> getUserCarsByFilter(PaginatedFilter paginatedFilter);
	
	// ------------------- 车辆服务记录 -------------------
	
	UserCarSvcRec getUserCarSvcRecById(Integer id);
	
	UserCarSvcRec getUserCarSvcRecByUserIdAndCarId(Integer userId, Integer carId);
	
	boolean updateUserCarSvcRec(UserCarSvcRec userCarSvcRec);

	boolean saveUserCarSvcRec(UserCarSvcRec userCarSvcRec);

	boolean delUserCarSvcRecById(Integer id);
	
	/**
	 * 查询会员车辆服务记录
	 * 
	 * @author 郝江奎
	 * @date 2016年1月25日 上午9:50:40
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<UserCarSvcRec> getUserCarSvcRecsByFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 设置用户默认车辆
	 * 
	 * @author 郝江奎
	 * @date 2016年1月26日 上午10:10:21
	 * 
	 * @param userId
	 * @param userCarId
	 * @return
	 */
	boolean setUserCarDefault(Integer userId, Integer userCarId);

	/**
	 * 获取用户默认车辆
	 * 
	 * @author 郝江奎
	 * @date 2016年1月26日 下午2:52:22
	 * 
	 * @param userId
	 * @param defaulted
	 * @return
	 */
	UserCar getUserCarDefault(Integer userId, boolean defaulted);
	
	/**
	 * 获取用户车辆
	 * 
	 * @author 郝江奎
	 * @date 2016年1月26日 下午2:52:22
	 * 
	 * @param userId
	 * @return
	 */
	List<UserCar> selectByUserId(Integer userId);
	
}
