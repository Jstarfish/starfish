package priv.starfish.mall.dao.market;


import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.market.entity.AdvertLink;


@IBatisSqlTarget
public interface AdvertLinkDao extends BaseDao<AdvertLink, Integer> {
	AdvertLink selectById(Integer id);
	int insert(AdvertLink advertLink);
	int update(AdvertLink advertLink);
	int deleteById(Integer id);
	
	/**
	 * 根据广告Id查询广告链接
	 * @author zjl
	 * @date 2015年7月7日 下午12:42:04
	 * 
	 * @param advertId 广告Id
	 * @return List<AdvertLink>
	 */
	List<AdvertLink> selectByAdvertId(Integer advertId);
	/**
	 * 获取每个广告的最大日期最小日期
	 * @author zjl
	 * @date 2015年7月8日 上午9:23:44
	 * 
	 * @param id 广告Id
	 * @return AdvertLink
	 */
	AdvertLink selectMinMaxDate(Integer advertId);
}