package priv.starfish.mall.dao.comn;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.entity.SysParam;

@IBatisSqlTarget
public interface SysParamDao extends BaseDao<SysParam, String> {

	SysParam selectByCode(String code);

	SysParam selectByName(String name);

	List<SysParam> selectAll();

	int insert(SysParam sysParam);

	int update(SysParam sysParam);

	int deleteByCode(String code);
}
