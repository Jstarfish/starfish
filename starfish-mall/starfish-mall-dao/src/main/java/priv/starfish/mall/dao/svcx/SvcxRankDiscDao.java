package priv.starfish.mall.dao.svcx;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.svcx.entity.SvcxRankDisc;

@IBatisSqlTarget
public interface SvcxRankDiscDao extends BaseDao<SvcxRankDisc, Integer> {
	SvcxRankDisc selectById(Integer id);

	SvcxRankDisc selectByShopIdAndSvcIdAndRank(Integer shopId, Integer svcId, Integer rank);

	int insert(SvcxRankDisc svcxRankDisc);

	int update(SvcxRankDisc svcxRankDisc);

	int deleteById(Integer id);

	List<SvcxRankDisc> selectByShopIdAndSvcIdAndRank(Integer shopId, Integer svcId);
}