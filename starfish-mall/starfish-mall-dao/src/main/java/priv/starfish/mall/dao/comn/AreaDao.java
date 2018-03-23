package priv.starfish.mall.dao.comn;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.entity.Area;

@IBatisSqlTarget
public interface AreaDao extends BaseDao<Area, Integer> {

	Area selectById(Integer id);

	Area selectByNameAndRegionId(String name, Integer regionId);

	int insert(Area area);

	int update(Area area);

	int deleteById(Integer id);
}