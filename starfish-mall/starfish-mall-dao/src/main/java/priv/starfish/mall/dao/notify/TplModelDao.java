package priv.starfish.mall.dao.notify;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.notify.entity.TplModel;

@IBatisSqlTarget
public interface TplModelDao extends BaseDao<TplModel, Integer> {
	TplModel selectById(Integer id);

	TplModel selectByCode(String code);

	int insert(TplModel tplModel);

	int update(TplModel tplModel);

	int deleteById(Integer id);

	/**
	 * 查询所有的模板模型
	 * 
	 * @author 毛智东
	 * @param code 
	 * @date 2015年6月18日 上午11:27:10
	 * 
	 * @return
	 */
	List<TplModel> selectAll(String code);
}