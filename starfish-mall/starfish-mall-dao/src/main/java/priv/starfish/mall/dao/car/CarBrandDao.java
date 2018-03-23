package priv.starfish.mall.dao.car;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.car.entity.CarBrand;

@IBatisSqlTarget
public interface CarBrandDao extends BaseDao<CarBrand, Integer> {
	CarBrand selectById(Integer id);

	CarBrand selectByName(String name);

	int insert(CarBrand carBrand);

	int update(CarBrand carBrand);

	int deleteById(Integer id);

	CarBrand selectByRefId(Integer refId);

	List<CarBrand> selectAll(boolean includeDisabled);

	/**
	 * 
	 * 
	 * @author 郝江奎
	 * @date 2016年1月21日 下午7:20:14
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<CarBrand> selectCarBrands(PaginatedFilter paginatedFilter);
	
	/**
	 * 
	 * 
	 * @author 郝江奎
	 * @date 2016年1月22日 下午12:42:34
	 * 
	 * @param id
	 * @param disabled
	 * @return
	 */
	int updateDisabled(Integer id, boolean disabled);

}