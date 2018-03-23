package priv.starfish.mall.dao.categ;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.categ.entity.BrandDef;

@IBatisSqlTarget
public interface BrandDefDao extends BaseDao<BrandDef, Integer> {
	BrandDef selectById(Integer id);

	BrandDef selectByCode(String code);

	int insert(BrandDef brandDef);

	int update(BrandDef brandDef);

	int deleteById(Integer id);

	/**
	 * 分页查询品牌
	 * 
	 * @author 廖晓远
	 * @date 2015年6月23日 下午4:37:47
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<BrandDef> selectBrandDefs(PaginatedFilter paginatedFilter);

	/**
	 * 通过分类Id获取品牌
	 * 
	 * @author 廖晓远
	 * @date 2015年7月20日 下午5:09:26
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<BrandDef> selectByCodes(PaginatedFilter paginatedFilter);
}
