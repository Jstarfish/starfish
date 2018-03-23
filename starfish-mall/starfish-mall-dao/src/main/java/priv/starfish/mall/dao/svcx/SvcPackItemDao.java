package priv.starfish.mall.dao.svcx;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.svcx.entity.SvcPackItem;

@IBatisSqlTarget
public interface SvcPackItemDao extends BaseDao<SvcPackItem, Integer> {
	SvcPackItem selectById(Integer id);
	
	SvcPackItem selectBySvcIdAndSvcPackId(Integer svcId, Integer svcPackId);

	int insert(SvcPackItem svcPackItem);

	int update(SvcPackItem svcPackItem);

	int deleteById(Integer id);

	/**
	 * 根据套餐id获取套餐服务明细
	 * 
	 * @author 李超杰
	 * @date 2015年10月13日 下午2:17:33
	 * 
	 * @param groupId
	 * @return
	 */
	public List<SvcPackItem> selectByPackId(Integer packId);
}
