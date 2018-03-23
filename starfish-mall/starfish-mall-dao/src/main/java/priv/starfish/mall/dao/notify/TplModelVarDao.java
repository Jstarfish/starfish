package priv.starfish.mall.dao.notify;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.notify.entity.TplModelVar;

@IBatisSqlTarget
public interface TplModelVarDao extends BaseDao<TplModelVar, Integer> {
	TplModelVar selectById(Integer id);

	TplModelVar selectByModelCodeAndExpr(String modelCode, String expr);

	int insert(TplModelVar tplModelVar);

	int update(TplModelVar tplModelVar);

	int deleteById(Integer id);

	/**
	 * 根据modelCode查询属性值列表
	 * 
	 * @author 毛智东
	 * @date 2015年6月23日 下午5:04:19
	 * 
	 * @param modelCode
	 * @return
	 */
	List<TplModelVar> selectListByModelCode(String modelCode);
}