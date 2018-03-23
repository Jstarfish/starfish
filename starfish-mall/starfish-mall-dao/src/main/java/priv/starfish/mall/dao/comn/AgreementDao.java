package priv.starfish.mall.dao.comn;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.entity.Agreement;

@IBatisSqlTarget
public interface AgreementDao extends BaseDao<Agreement, Integer> {

	Agreement selectById(Integer id);

	Agreement selectByTarget(String target);

	int insert(Agreement agreement);

	int update(Agreement agreement);

	int deleteById(Integer id);
}