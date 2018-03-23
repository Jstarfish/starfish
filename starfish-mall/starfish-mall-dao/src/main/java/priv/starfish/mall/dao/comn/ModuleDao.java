package priv.starfish.mall.dao.comn;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.Module;

@IBatisSqlTarget
public interface ModuleDao extends BaseDao<Module, Integer> {
	Module selectById(Integer id);

	Module selectByName(String name);

	int insert(Module module);

	int update(Module module);

	int deleteById(Integer id);

	/**
	 * 查询认证范围内的模块及权限
	 * 
	 * @author guoyn
	 * @date 2015年5月12日 下午6:18:58
	 * 
	 * @param scope
	 * @return
	 */
	List<Module> selectByScope(AuthScope scope);

}
