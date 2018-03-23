package priv.starfish.mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.car.CarBrandDao;
import priv.starfish.mall.dao.car.CarModelDao;
import priv.starfish.mall.dao.car.CarSerialDao;
import priv.starfish.mall.dao.car.CarSerialGroupDao;
import priv.starfish.mall.dao.car.UserCarDao;
import priv.starfish.mall.dao.car.UserCarSvcRecDao;
import priv.starfish.mall.car.entity.CarBrand;
import priv.starfish.mall.car.entity.CarModel;
import priv.starfish.mall.car.entity.CarSerial;
import priv.starfish.mall.car.entity.CarSerialGroup;
import priv.starfish.mall.car.entity.UserCar;
import priv.starfish.mall.car.entity.UserCarSvcRec;
import priv.starfish.mall.dao.comn.UserDao;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.service.CarService;

@Service("carService")
public class CarServiceImpl extends BaseServiceImpl implements CarService {
	@Resource
	CarBrandDao carBrandDao;

	@Resource
	CarSerialGroupDao carSerialGroupDao;

	@Resource
	CarSerialDao carSerialDao;

	@Resource
	CarModelDao carModelDao;

	@Resource
	UserCarDao userCarDao;

	@Resource
	UserCarSvcRecDao userCarSvcRecDao;

	@Resource
	UserDao userDao;

	@Override
	public CarBrand getCarBrandById(Integer id) {
		CarBrand carBrand = carBrandDao.selectById(id);
		this.filterFileBrowseUrl(carBrand);
		return carBrand;
	}

	@Override
	public CarBrand getCarBrandByName(String name) {
		return this.carBrandDao.selectByName(name);
	}

	@Override
	public CarBrand getCarBrandByRefId(Integer refId) {
		return this.carBrandDao.selectByRefId(refId);
	}

	@Override
	public boolean delCarBrandById(Integer id) {
		return this.carBrandDao.deleteById(id) > 0;
	}

	@Override
	public boolean saveCarBrand(CarBrand carBrand) {
		if (StrUtil.hasText(carBrand.getName()) && !StrUtil.hasText(carBrand.getPy())) {
			carBrand.setPy(StrUtil.chsToPy(carBrand.getName()));
		}
		if (carBrand.getPy() != null) {
			carBrand.setPy(StrUtil.castToLength(carBrand.getPy(), 30));
			carBrand.setZm(carBrand.getPy().charAt(0));
		}
		if (carBrand.getTs() == null) {
			carBrand.setTs(new Date());
		}
		//
		if (carBrand.getId() == null) {
			if (carBrand.getSeqNo() == null) {
				carBrand.setSeqNo(this.carBrandDao.getEntityMaxSeqNo(CarBrand.class) + 1);
			}
			if (carBrand.getDisabled() == null) {
				carBrand.setDisabled(false);
			}
			return this.carBrandDao.insert(carBrand) > 0;
		} else {
			return this.carBrandDao.update(carBrand) > 0;
		}
	}

	@Override
	public List<CarBrand> getCarBrands(boolean includeDisabled) {
		return this.carBrandDao.selectAll(includeDisabled);
	}

	//

	@Override
	public CarSerialGroup getCarSerialGroupById(Integer id) {
		return this.carSerialGroupDao.selectById(id);
	}

	@Override
	public CarSerialGroup getCarSerialGroupByBrandIdAndName(Integer brandId, String name) {
		return this.carSerialGroupDao.selectByBrandIdAndName(brandId, name);
	}

	@Override
	public CarSerialGroup getCarSerialGroupByRefId(Integer refId) {
		return this.carSerialGroupDao.selectByRefId(refId);
	}

	@Override
	public int delCarSerialGroupById(Integer id) {
		return this.carSerialGroupDao.deleteById(id);
	}

	@Override
	public int saveCarSerialGroup(CarSerialGroup carSerialGroup) {
		if (StrUtil.hasText(carSerialGroup.getName()) && !StrUtil.hasText(carSerialGroup.getPy())) {
			carSerialGroup.setPy(StrUtil.chsToPy(carSerialGroup.getName()));
		}
		if (carSerialGroup.getPy() != null) {
			carSerialGroup.setPy(StrUtil.castToLength(carSerialGroup.getPy(), 30));
		}
		if (carSerialGroup.getTs() == null) {
			carSerialGroup.setTs(new Date());
		}
		//
		if (carSerialGroup.getId() == null) {
			if (carSerialGroup.getSeqNo() == null) {
				carSerialGroup.setSeqNo(this.carSerialGroupDao.getEntityMaxSeqNo(CarSerialGroup.class) + 1);
			}
			return this.carSerialGroupDao.insert(carSerialGroup);
		} else {
			return this.carSerialGroupDao.update(carSerialGroup);
		}
	}

	//
	@Override
	public CarSerial getCarSerialById(Integer id) {
		return this.carSerialDao.selectById(id);
	}

	@Override
	public CarSerial getCarSerialByBrandIdAndName(Integer brandId, String name) {
		return this.carSerialDao.selectByBrandIdAndName(brandId, name);
	}

	@Override
	public CarSerial getCarSerialByRefId(Integer refId) {
		return this.carSerialDao.selectByRefId(refId);
	}

	@Override
	public boolean delCarSerialById(Integer id) {
		return this.carSerialDao.deleteById(id) > 0;
	}

	@Override
	public boolean saveCarSerial(CarSerial carSerial) {
		if (StrUtil.hasText(carSerial.getName()) && !StrUtil.hasText(carSerial.getPy())) {
			carSerial.setPy(StrUtil.chsToPy(carSerial.getName()));
		}
		if (carSerial.getPy() != null) {
			carSerial.setPy(StrUtil.castToLength(carSerial.getPy(), 30));
		}
		if (carSerial.getTs() == null) {
			carSerial.setTs(new Date());
		}
		//
		if (carSerial.getId() == null) {
			if (carSerial.getSeqNo() == null) {
				carSerial.setSeqNo(this.carSerialDao.getEntityMaxSeqNo(CarSerial.class) + 1);
			}
			if (carSerial.getDisabled() == null) {
				carSerial.setDisabled(false);
			}
			return this.carSerialDao.insert(carSerial) > 0;
		} else {
			return this.carSerialDao.update(carSerial) > 0;
		}
	}

	@Override
	public List<CarSerial> getCarSerials(Integer brandId, boolean includeDisabled) {
		return this.carSerialDao.selectAll(brandId, includeDisabled);
	}

	//
	@Override
	public CarModel getCarModelById(Integer id) {
		return this.carModelDao.selectById(id);
	}

	@Override
	public CarModel getCarModelBySerialIdAndName(Integer serialId, String name) {
		return this.carModelDao.selectBySerialIdAndName(serialId, name);
	}

	@Override
	public CarModel getCarModelByRefId(Integer refId) {
		return this.carModelDao.selectByRefId(refId);
	}

	@Override
	public boolean delCarModelById(Integer id) {
		return this.carModelDao.deleteById(id) > 0;
	}

	@Override
	public boolean saveCarModel(CarModel carModel) {
		if (StrUtil.hasText(carModel.getName()) && !StrUtil.hasText(carModel.getPy())) {
			carModel.setPy(StrUtil.chsToPy(carModel.getName()));
		}
		if (carModel.getName() != null) {
			carModel.setName(StrUtil.castToLength(carModel.getName(), 60));
		}
		if (carModel.getPy() != null) {
			carModel.setPy(StrUtil.castToLength(carModel.getPy(), 60));
		}
		if (carModel.getRefName() != null) {
			carModel.setRefName(StrUtil.castToLength(carModel.getRefName(), 60));
		}
		if (carModel.getTs() == null) {
			carModel.setTs(new Date());
		}
		//
		if (carModel.getId() == null) {
			if (carModel.getSeqNo() == null) {
				carModel.setSeqNo(this.carModelDao.getEntityMaxSeqNo(CarModel.class) + 1);
			}
			if (carModel.getDisabled() == null) {
				carModel.setDisabled(false);
			}
			return this.carModelDao.insert(carModel) > 0;
		} else {
			return this.carModelDao.update(carModel) > 0;
		}
	}

	@Override
	public List<CarModel> getCarModels(Integer serialId, boolean includeDisabled) {
		return this.carModelDao.selectAll(serialId, includeDisabled);
	}

	@Override
	public PaginatedList<CarBrand> getCarBrandsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<CarBrand> carBrandList = carBrandDao.selectCarBrands(paginatedFilter);
		List<CarBrand> listCarBrand = carBrandList.getRows();
		for (CarBrand carBrand : listCarBrand) {
			this.filterFileBrowseUrl(carBrand);
		}
		carBrandList.setRows(listCarBrand);
		return carBrandList;
	}

	@Override
	public PaginatedList<CarSerial> getCarSerialsByFilter(PaginatedFilter paginatedFilter) {
		return carSerialDao.selectCarSerials(paginatedFilter);
	}

	@Override
	public PaginatedList<CarModel> getCarModelsByFilter(PaginatedFilter paginatedFilter) {
		return carModelDao.selectCarModels(paginatedFilter);
	}

	@Override
	public boolean updateCarBrandDisableState(Integer id, boolean disabled) {
		return carBrandDao.updateDisabled(id, disabled) > 0;
	}

	@Override
	public boolean updateCarSerialDisableState(Integer id, boolean disabled) {
		return carSerialDao.updateDisabled(id, disabled) > 0;
	}

	@Override
	public boolean updateCarModelDisableState(Integer id, boolean disabled) {
		return carModelDao.updateDisabled(id, disabled) > 0;
	}

	// ------------------- 用户车辆 -------------------

	@Override
	public UserCar getUserCarById(Integer id) {
		return userCarDao.selectById(id);
	}

	@Override
	public UserCar selectByUserIdAndModelId(Integer userId, Integer modelId) {
		return userCarDao.selectByUserIdAndModelId(userId, modelId);
	}

	@Override
	public boolean saveUserCar(UserCar userCar) {

		if (userCar.getId() == null) {
			if (userCar.getDeleted() == null) {
				userCar.setDeleted(false);
			}
			if (userCar.getDefaulted() == null) {
				userCar.setDefaulted(false);
			}
			return this.userCarDao.insert(userCar) > 0;
		} else {
			return this.userCarDao.update(userCar) > 0;
		}
	}

	@Override
	public boolean delUserCarById(Integer id) {
		return userCarDao.deleteById(id) > 0;
	}

	@Override
	public PaginatedList<UserCar> getUserCarsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<UserCar> userCarList = userCarDao.selectUserCars(paginatedFilter);
		List<UserCar> listUserCar = userCarList.getRows();
		for (UserCar userCar : listUserCar) {
			User user = userDao.selectById(userCar.getUserId());
			userCar.setUser(user);
			//
			if (userCar.getBrandId() != null) {
				CarBrand carBrand = carBrandDao.selectById(userCar.getBrandId());
				this.filterFileBrowseUrl(carBrand);
				userCar.setCarBrand(carBrand);
			}
			//
			if (userCar.getSerialId() != null) {
				CarSerial carSerial = carSerialDao.selectById(userCar.getSerialId());
				userCar.setCarSerial(carSerial);
			}
			//
			if (userCar.getModelId() != null) {
				CarModel carModel = carModelDao.selectById(userCar.getModelId());
				userCar.setCarModel(carModel);
			}
			
		}
		userCarList.setRows(listUserCar);
		return userCarList;
	}

	@Override
	public boolean setUserCarDefault(Integer userId, Integer userCarId) {
		Integer updateLinkWayId = userCarDao.updateUserCarByUserId(userId);
		boolean ok = false;
		if (updateLinkWayId > 0) {
			UserCar userCar = new UserCar();
			userCar.setId(userCarId);
			userCar.setDefaulted(true);
			ok = userCarDao.update(userCar) > 0;
		}
		return ok;
	}
	
	@Override
	public UserCar getUserCarDefault(Integer userId, boolean defaulted) {
		return userCarDao.selectDefaultByUserId(defaulted, userId);
	}
	
	@Override
	public List<UserCar> selectByUserId(Integer userId) {
		return userCarDao.selectByUserId(userId);
	}

	// ------------------- 用户车辆服务记录 -------------------

	@Override
	public UserCarSvcRec getUserCarSvcRecById(Integer id) {
		return userCarSvcRecDao.selectById(id);
	}

	@Override
	public UserCarSvcRec getUserCarSvcRecByUserIdAndCarId(Integer userId, Integer carId) {
		return userCarSvcRecDao.selectUserCarSvcRecByUserIdAndCarId(userId,carId);
	}

	@Override
	public boolean saveUserCarSvcRec(UserCarSvcRec userCarSvcRec) {
		if (userCarSvcRec.getId() == null) {
			return this.userCarSvcRecDao.insert(userCarSvcRec) > 0;
		} else {
			return this.userCarSvcRecDao.update(userCarSvcRec) > 0;
		}
	}

	@Override
	public boolean delUserCarSvcRecById(Integer id) {
		return userCarSvcRecDao.deleteById(id) > 0;
	}

	@Override
	public PaginatedList<UserCarSvcRec> getUserCarSvcRecsByFilter(PaginatedFilter paginatedFilter) {
		return userCarSvcRecDao.selectUserCarSvcRecs(paginatedFilter);
	}

	@Override
	public boolean updateUserCarSvcRec(UserCarSvcRec userCarSvcRec) {
		return userCarSvcRecDao.update(userCarSvcRec)>0;
	}
	
	

}
