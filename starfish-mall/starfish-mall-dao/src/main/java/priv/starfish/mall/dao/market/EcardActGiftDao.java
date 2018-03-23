package priv.starfish.mall.dao.market;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.market.entity.EcardActGift;

@IBatisSqlTarget
public interface EcardActGiftDao extends BaseDao<EcardActGift, Long> {
	EcardActGift selectById(Long id);

	int insert(EcardActGift ecardActGift);

	int update(EcardActGift ecardActGift);

	int deleteById(Long id);

	List<EcardActGift> selectByFilter(MapContext requestDate);
	
	int deleteByActRuleId(Integer actRuleId);
}
