package priv.starfish.mall.dao.categ;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.categ.entity.AttrRef;

/**
 * 属性参照dao接口
 * 
 * @author 毛智东
 * @date 2015年5月27日 下午7:16:51
 *
 */
@IBatisSqlTarget
public interface AttrRefDao extends BaseDao<AttrRef, Integer> {
	AttrRef selectById(Integer id);

	AttrRef selectByCode(String code);

	AttrRef selectByName(String name);

	int insert(AttrRef attrRef);

	int update(AttrRef attrRef);

	int deleteById(Integer id);

	/**
	 * 批量删除
	 * 
	 * @author 毛智东
	 * @date 2015年5月27日 下午7:57:59
	 * 
	 * @param ids
	 * @return
	 */
	int deleteBatch(List<Integer> ids);

	/**
	 * 分页查询
	 * 
	 * @author 毛智东
	 * @date 2015年5月27日 下午8:02:18
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<AttrRef> selectByfilter(PaginatedFilter paginatedFilter);

	/**
	 * 查询是品牌属性的属性参照
	 * 
	 * @author 毛智东
	 * @date 2015年6月24日 下午4:09:25
	 * 
	 * @return
	 */
	AttrRef selectByBrandFlagIsTrue();
}