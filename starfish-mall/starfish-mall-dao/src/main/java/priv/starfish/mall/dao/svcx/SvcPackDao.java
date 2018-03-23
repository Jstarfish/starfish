package priv.starfish.mall.dao.svcx;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.svcx.entity.SvcPack;

@IBatisSqlTarget
public interface SvcPackDao extends BaseDao<SvcPack, Integer> {
	SvcPack selectById(Integer id);

	SvcPack selectByKindIdAndName(Integer kindId, String name);

	int insert(SvcPack svcPack);

	int update(SvcPack svcPack);

	int deleteById(Integer id);
	
	/**
	 * 
	 * 分页获取服务套擦列表
	 * @author 李超杰
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SvcPack> selectByFilter(PaginatedFilter paginatedFilter);
	
	List<SvcPack> selectByFilter(MapContext requestFilter);
}