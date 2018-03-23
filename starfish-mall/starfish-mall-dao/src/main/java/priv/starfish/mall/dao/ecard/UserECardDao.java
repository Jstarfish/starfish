package priv.starfish.mall.dao.ecard;

import java.util.List;
import java.util.Map;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.ecard.entity.UserECard;

@IBatisSqlTarget
public interface UserECardDao extends BaseDao<UserECard, Integer> {
	UserECard selectById(Integer id);

	int insert(UserECard userECard);

	int update(UserECard userECard);

	int deleteById(Integer id);

	/**
	 * 分页查询 用户e卡 列表
	 * 
	 * @author "WJJ"
	 * @date 2015年10月20日 上午11:50:18
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<UserECard> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 查询用户e卡列表
	 * 
	 * @author wangdi
	 * @date 2015年11月4日 下午7:10:44
	 * 
	 * @param filter
	 * @return
	 */
	List<UserECard> selectByFilterNormal(MapContext filter);

	/**
	 * 查询当前登录用户名下的所有e卡
	 * 
	 * @author 邓华锋
	 * @date 2016年1月5日 下午8:30:38
	 * 
	 * @param userId
	 * @return
	 */
	Map<Integer, Integer> selectECardShopsByUserId(Integer userId);

	/**
	 * 根据过滤条件查询符合条件的卡数量（可用于检查用户和e卡的绑定关系）
	 * 
	 * @author wangdi
	 * @date 2015年11月5日 下午4:27:02
	 * 
	 * @param userId
	 * @param cardId
	 * @return
	 */
	Integer selectCountByFilter(MapContext filter);

	/**
	 * e卡合并相关更新
	 * 
	 * @author wangdi
	 * @date 2015年11月6日 上午11:49:38
	 * 
	 * @param params
	 * @return
	 */
	int updateForMerge(MapContext params);

	/**
	 * e卡转赠相关更新
	 * 
	 * @author wangdi
	 * @date 2015年11月9日 上午10:32:19
	 * 
	 * @param params
	 * @return
	 */
	int updateForPresent(MapContext params);

	/**
	 * e卡删除相关更新
	 * 
	 * @author wangdi
	 * @date 2015年11月9日 下午3:50:14
	 * 
	 * @param params
	 * @return
	 */
	int updateForDelete(MapContext params);

	/**
	 * 分页查询用户e卡列表，后台
	 * 
	 * @author "WJJ"
	 * @date 2015年11月21日 上午10:52:33
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<UserECard> selectByFilterBack(PaginatedFilter paginatedFilter);

	/**
	 * 解除用户E卡与店铺的绑定关系
	 * 
	 * @author "WJJ"
	 * @date 2015年11月21日 下午4:51:32
	 * 
	 * @param userECard
	 * @return
	 */
	int updateUnbind(UserECard userECard);

	/**
	 * 增减余额
	 * 
	 * @author lichaojie
	 * @date 2015年11月21日 下午4:24:50
	 * 
	 * @param params
	 * @return
	 */
	int updateForChangeRemainVal(UserECard userECard);

	/**
	 * 根据卡编号查询UserECard
	 * 
	 * @author "WJJ"
	 * @date 2016年2月16日 下午10:26:49
	 * 
	 * @param cardNo
	 * @return
	 */
	UserECard selectByCardNo(String cardNo);
}
