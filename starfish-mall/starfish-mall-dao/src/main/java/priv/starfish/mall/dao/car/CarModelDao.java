package priv.starfish.mall.dao.car;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.car.entity.CarModel;

@IBatisSqlTarget
public interface CarModelDao extends BaseDao<CarModel, Integer> {
	CarModel selectById(Integer id);

	CarModel selectBySerialIdAndName(Integer serialId, String name);

	int insert(CarModel carModel);

	int update(CarModel carModel);

	int deleteById(Integer id);

	CarModel selectByRefId(Integer refId);

	List<CarModel> selectAll(Integer serialId, boolean includeDisabled);

	/**
	 * 
	 * 
	 * @author 郝江奎
	 * @date 2016年1月21日 下午7:21:42
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<CarModel> selectCarModels(PaginatedFilter paginatedFilter);

	/**
	 * 
	 * 
	 * @author 郝江奎
	 * @date 2016年1月22日 下午12:43:14
	 * 
	 * @param id
	 * @param disabled
	 * @return
	 */
	int updateDisabled(Integer id, boolean disabled);

}