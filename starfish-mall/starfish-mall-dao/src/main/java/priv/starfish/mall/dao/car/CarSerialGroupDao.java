package priv.starfish.mall.dao.car;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.car.entity.CarSerialGroup;

@IBatisSqlTarget
public interface CarSerialGroupDao extends BaseDao<CarSerialGroup, Integer> {
	CarSerialGroup selectById(Integer id);

	CarSerialGroup selectByBrandIdAndName(Integer brandId, String name);

	int insert(CarSerialGroup carSerialGroup);

	int update(CarSerialGroup carSerialGroup);

	int deleteById(Integer id);

	CarSerialGroup selectByRefId(Integer refId);
}