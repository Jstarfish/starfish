package priv.starfish.mall.dao.market;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.market.entity.EcardActRule;

@IBatisSqlTarget
public interface EcardActRuleDao extends BaseDao<EcardActRule, Integer> {
	EcardActRule selectById(Integer id);

	int insert(EcardActRule ecardActRule);

	int update(EcardActRule ecardActRule);

	int deleteById(Integer id);
	
	List<EcardActRule> selectByFilter(MapContext requestData);
}
