package priv.starfish.mall.dao.market;


import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.market.entity.AdvertPos;


@IBatisSqlTarget
public interface AdvertPosDao extends BaseDao<AdvertPos, String> {
	AdvertPos selectById(String code);
	AdvertPos selectByName(String name);
	int insert(AdvertPos advertPos);
	int update(AdvertPos advertPos);
	int deleteById(String code);
	
	/**
	 * 查询所有预定义广告位置
	 * @author zjl
	 * @date 2015年7月7日 下午12:51:27
	 * 
	 * @return List<AdvertPos>
	 */
	List<AdvertPos> selectAdvertPoss();
}