package priv.starfish.mall.dao.market;


import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.market.entity.Advert;


@IBatisSqlTarget
public interface AdvertDao extends BaseDao<Advert, Integer> {
	Advert selectById(Integer id);
	Advert selectByName(String name);
	int insert(Advert advert);
	int update(Advert advert);
	int deleteById(Integer id);
	
	/**
	 * 分页查询广告列表
	 * @author guoyn
	 * @date 2015年9月17日 上午9:54:39
	 * 
	 * @param paginatedFilter =id, like name
	 * @return PaginatedList<Advert>
	 */
	PaginatedList<Advert> selectByFilter(PaginatedFilter paginatedFilter);
	
	Advert selectByPosCode(String posCode);
	
	List<Advert> selectAll();
}