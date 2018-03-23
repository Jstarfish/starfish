package priv.starfish.mall.dao.mall;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.mall.entity.Operator;

@IBatisSqlTarget
public interface OperatorDao extends BaseDao<Operator, Integer> {
	Operator selectById(Integer id);

	/**
	 * 获取唯一的运营商
	 * 
	 * @author koqiui
	 * @date 2015年11月11日 下午6:39:48
	 * 
	 * @return
	 */
	Operator selectTheOne();

	int insert(Operator operator);

	int update(Operator operator);

	int deleteById(Integer id);
}
