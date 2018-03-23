package priv.starfish.mall.dao.svcx;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.svcx.entity.SvcKind;

@IBatisSqlTarget
public interface SvcKindDao extends BaseDao<SvcKind, Integer> {
	SvcKind selectById(Integer id);

	SvcKind selectByName(String name);

	int insert(SvcKind svcKind);

	int update(SvcKind svcKind);

	int deleteById(Integer id);

	List<SvcKind> selectByFilter();
}