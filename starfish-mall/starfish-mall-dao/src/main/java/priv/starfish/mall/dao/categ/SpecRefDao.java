package priv.starfish.mall.dao.categ;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.categ.entity.SpecRef;

@IBatisSqlTarget
public interface SpecRefDao extends BaseDao<SpecRef, Integer> {

	SpecRef selectById(Integer id);

	SpecRef selectByCode(String code);

	/**
	 * 分页查询规格参照
	 * 
	 * @author 王少辉
	 * @date 2015年5月27日 下午6:48:39
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SpecRef> selectSpecRef(PaginatedFilter paginatedFilter);

	int insert(SpecRef specRef);

	int update(SpecRef specRef);

	int deleteById(Integer id);

	/**
	 * 根据id批量删除商品规格参照属性
	 * 
	 * @author 王少辉
	 * @date 2015年5月29日 上午8:59:42
	 * 
	 * @param ids
	 * @return
	 */
	int batchDelByIds(List<Integer> ids);

	/**
	 * 选择颜色规格参照
	 * 
	 * @author guoyn
	 * @date 2015年11月2日 上午11:38:51
	 * 
	 * @return SpecRef
	 */
	SpecRef selectColorSpecRef();
}
