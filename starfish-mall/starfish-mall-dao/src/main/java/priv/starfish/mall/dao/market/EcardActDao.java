package priv.starfish.mall.dao.market;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.market.entity.EcardAct;

@IBatisSqlTarget
public interface EcardActDao extends BaseDao<EcardAct, Integer> {
	EcardAct selectById(Integer id);

	EcardAct selectByYearAndName(Integer year, String name);

	int insert(EcardAct ecardAct);

	int update(EcardAct ecardAct);

	int deleteById(Integer id);

	PaginatedList<EcardAct> selectByFilter(PaginatedFilter paginatedFilter);
	
}
