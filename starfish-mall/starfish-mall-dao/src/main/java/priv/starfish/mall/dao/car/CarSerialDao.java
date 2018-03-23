package priv.starfish.mall.dao.car;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.car.entity.CarSerial;

@IBatisSqlTarget
public interface CarSerialDao extends BaseDao<CarSerial, Integer> {
	CarSerial selectById(Integer id);

	CarSerial selectByBrandIdAndName(Integer brandId, String name);

	int insert(CarSerial carSerial);

	int update(CarSerial carSerial);

	int deleteById(Integer id);

	CarSerial selectByRefId(Integer refId);

	List<CarSerial> selectAll(Integer brandId, boolean includeDisabled);

	/**
	 * 
	 * 
	 * @author 郝江奎
	 * @date 2016年1月21日 下午7:21:12
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<CarSerial> selectCarSerials(PaginatedFilter paginatedFilter);

	/**
	 * 
	 * 
	 * @author 郝江奎
	 * @date 2016年1月22日 下午12:42:58
	 * 
	 * @param id
	 * @param disabled
	 * @return
	 */
	int updateDisabled(Integer id, boolean disabled);

}
