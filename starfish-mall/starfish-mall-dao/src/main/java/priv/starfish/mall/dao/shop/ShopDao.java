package priv.starfish.mall.dao.shop;

import java.util.Date;
import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.shop.entity.Shop;

@IBatisSqlTarget
public interface ShopDao extends BaseDao<Shop, Integer> {
	/*
	 * 根据ID查询店铺
	 */
	Shop selectById(Integer id);

	/**
	 * 根据Code 查询店铺
	 * 
	 * @author zjl
	 * @date 2015年5月27日 下午4:07:57
	 * 
	 * @param code
	 * @return Shop
	 */
	Shop selectByCode(String code);

	/**
	 * 根据名称查询店铺
	 * 
	 * @author zjl
	 * @date 2015年5月27日 下午4:08:14
	 * 
	 * @param name
	 * @return Shop
	 */
	Shop selectByName(String name);

	/*
	 * 添加店铺
	 */
	int insert(Shop shop);

	/*
	 * 更新店铺
	 */
	int update(Shop shop);

	/*
	 * 根据ID删除店铺
	 */
	int deleteById(Integer id);

	/**
	 * 通过商户Id查询店铺信息
	 * 
	 * @author 廖晓远
	 * @date 2015-5-21 上午10:45:37
	 * 
	 * @param merchId
	 * @return
	 */
	List<Shop> selectByMerchId(Integer merchId);

	/**
	 * 通过商户Id删除店铺
	 * 
	 * @author 廖晓远
	 * @date 2015-5-21 下午6:27:14
	 * 
	 * @param merchId
	 * @return
	 */
	int deleteByMerchId(Integer merchId);

	/**
	 * 分页查询店铺
	 * 
	 * @author zjl
	 * @date 2015年5月20日 下午5:01:43
	 * 
	 * @param paginatedFilter
	 * @return PaginatedList<Shop>
	 */
	PaginatedList<Shop> selectShops(PaginatedFilter paginatedFilter);

	/**
	 * 更改店铺开店关闭
	 * 
	 * @author zjl
	 * @date 2015年5月27日 下午7:15:15
	 * 
	 * @param id
	 *            店铺ID
	 * @param closed
	 *            店铺状态
	 * @return int
	 */
	int updateClosed(Integer id, boolean closed);

	/**
	 * 更改店铺启用禁用
	 * 
	 * @author 郝江奎
	 * @date 2015年10月12日 下午4:26:04
	 * 
	 * @param id
	 *            店铺ID
	 * @param disabled
	 *            店铺状态
	 * @return int
	 */
	int updateDisabled(Integer id, boolean disabled);

	/**
	 * 店铺审核
	 * 
	 * @author zjl
	 * @date 2015年5月28日 下午6:02:55
	 * @param id
	 *            店铺ID
	 * @param auditorId
	 *            审核人ID
	 * @param auditorName
	 *            审核人姓名
	 * @param auditTime
	 *            审核时间
	 * @param auditStatus
	 *            审核状态
	 * @return
	 */
	int updateAudit(Integer id, Integer auditorId, String auditorName, Date auditTime, Integer auditStatus);

	/**
	 * 修改店铺（联系人，店铺名称，联系电话，所在地，街道，店铺领域，店铺Logo ,是否自营，固定电话 等字段）
	 * 
	 * @author zjl
	 * @date 2015年6月1日 下午6:16:10
	 * 
	 * @param shop
	 * @return
	 */
	int updateShop(Shop shop);

	/**
	 * 查询某商户的店铺
	 * 
	 * @author guoyn
	 * @date 2015年10月12日 下午5:47:14
	 * 
	 * @param merchantId
	 * @param name
	 * @return Shop
	 */
	Shop selectByMerchantIdAndName(Integer merchantId, String name);

	/**
	 * 
	 * 更新索引
	 */
	int updateAsIndexed(Integer id, Date newIndexTime);

	/**
	 * 获取分页的最新变化/需要重新索引的(indexTime == null || changeTime != null && changeTime > indexTime) <br/>
	 */
	PaginatedList<Shop> selectByLatestChanges(PaginatedFilter pager);

	/**
	 * 根据缺货状态获取某一店铺下的所有商品id
	 */
	List<Integer> selectProductIdsByShopIdAndLackFlag(Integer shopId);

	/**
	 * 
	 * 更新店铺更改时间
	 * @return 
	 */
	int updateChangeTime(Integer shopId);

}