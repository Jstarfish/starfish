package priv.starfish.mall.dao.categ;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.categ.entity.ColorDef;

@IBatisSqlTarget
public interface ColorDefDao extends BaseDao<ColorDef, Integer> {
	ColorDef selectById(Integer id);

	ColorDef selectByName(String name);

	int insert(ColorDef colorDef);

	int update(ColorDef colorDef);

	int deleteById(Integer id);

	/**
	 * 分页获取颜色信息
	 * 
	 * @author zjl
	 * @date 2015年6月23日 下午3:49:44
	 * 
	 * @param paginatedFilter
	 * @return PaginatedList<ColorDef>
	 */
	PaginatedList<ColorDef> selectColorDefs(PaginatedFilter paginatedFilter);

	/**
	 * 获取所有颜色信息
	 * 
	 * @author 廖晓远
	 * @date 2015年6月25日 下午6:46:11
	 * 
	 * @param Filters
	 *            : excludeNames:需要排除的名字，queryName:查询的名字
	 * @return
	 */
	List<ColorDef> selectByFilter(List<String> excludeNames, String queryName);

}
