package priv.starfish.mall.service;

import java.util.List;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.market.entity.Advert;
import priv.starfish.mall.market.entity.AdvertPos;


public interface AdvertService extends BaseService {

	/**
	 * 根据广告名称获取广告
	 * @author guoyn
	 * @date 2015年9月15日 下午2:57:08
	 * 
	 * @param advertName
	 * @return Advert
	 */
	Advert getAdvertByName(String advertName);

	/**
	 * 删除广告链接
	 * @author guoyn
	 * @date 2015年9月16日 下午4:47:31
	 */
	boolean deleteAdvertLinkById(Integer advertLinkId);
	
	/**
	 * 根据Id删除广告
	 * @author guoyn
	 * @date 2015年9月16日 下午6:25:38
	 */
	boolean deleteAdvertById(Integer advertId);
	
	/**
	 * 批量删除删除广告
	 * @author guoyn
	 * @date 2015年9月16日 下午6:28:49
	 * 
	 * @param advertIds
	 * @return boolean
	 */
	boolean deleteAdvertByIds(List<Integer> advertIds);
	
	/**
	 * 添加广告
	 * @author guoyn
	 * @date 2015年9月16日 下午6:47:44
	 * 
	 * @param advert
	 * @return boolean
	 */
	boolean saveAdvert(Advert advert);
	
	/**
	 * 分页查询广告列表
	 * @author guoyn
	 * @date 2015年9月17日 上午9:34:44
	 * 
	 * @param paginatedFilter =id, like name
	 * @return PaginatedList<Advert>
	 */
	PaginatedList<Advert> getAdvertsByFilter(PaginatedFilter paginatedFilter);

	
	/**
	 * 查询所有广告预定义位置
	 * @author guoyn
	 * @date 2015年9月18日 下午5:10:44
	 * 
	 * @return List<AdvertPos>
	 */
	List<AdvertPos> getAdvertPoss();
	
	/**
	 * 更新广告
	 * @author guoyn
	 * @date 2015年9月18日 下午5:12:03
	 * 
	 * @param advert
	 * @return boolean
	 */
	boolean updateAdvert(Advert advert);

	
	/**
	 * 根据广告posCode获取广告
	 * 
	 * @author 郝江奎
	 * @date 2015年9月18日 下午3:58:31
	 * 
	 * @param posCode
	 * @return Advert
	 */
	 
	Advert getAdvertByPosCode(String posCode);

	List<Advert> getAdverts();

}
