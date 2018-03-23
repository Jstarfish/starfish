package priv.starfish.mall.service;

import java.util.Date;
import java.util.List;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.order.entity.SaleOrder;
import priv.starfish.mall.settle.entity.DistSettleRec;
import priv.starfish.mall.shop.entity.DistShop;
import priv.starfish.mall.shop.entity.DistShopMemo;
import priv.starfish.mall.shop.entity.DistShopSvc;

public interface DistShopService extends BaseService {

	// --------------------------------------- 卫星店铺 ----------------------------------------------

	/**
	 * 获取卫星店铺
	 * 
	 * @author 郝江奎
	 * @date 2016年1月19日 下午13:45:17
	 * 
	 * @param DistShopId
	 * @return DistShop
	 */
	DistShop getById(Integer DistShopId);

	/**
	 * 分页查询卫星店铺
	 * 
	 * @author 郝江奎
	 * @date 2016年1月19日 上午10:15:17
	 * 
	 * @param paginatedFilter
	 * @return DistShop
	 */
	PaginatedList<DistShop> getDistShopsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 添加卫星店铺
	 * 
	 * @author 郝江奎
	 * @date 2016年1月19日 上午10:17:17
	 * 
	 * @param distShop
	 * @return
	 */
	boolean saveDistShop(DistShop distShop);

	/**
	 * 修改卫星店铺
	 * 
	 * @author 郝江奎
	 * @date 2016年1月19日 上午10:19:17
	 * 
	 * @param distShop
	 * @return
	 */
	boolean updateDistShop(DistShop distShop);
	
	/**
	 * 
	 * 更新卫星店铺更改时间
	 */
	boolean updateChangeTime(Integer id, boolean updateOrNot);

	/**
	 * 删除卫星店铺
	 * 
	 * @author 郝江奎
	 * @date 2016年1月19日 上午10:25:17
	 * 
	 * @param distShopId
	 * @return
	 */
	boolean deleteDistShopById(Integer distShopId);

	/**
	 * 启用禁用卫星店铺
	 * 
	 * @author 郝江奎
	 * @date 2016年1月19日 上午10:25:17
	 * 
	 * @param distShop
	 * @return
	 */
	boolean updateDistShopDisableState(Integer id, boolean disabled);

	/**
	 * 审核卫星店铺
	 * 
	 * @author 郝江奎
	 * @date 2016年1月19日 下午18:58:17
	 * 
	 * @param distShop
	 * @return
	 */
	boolean updateDistShopAudit(DistShop distShop, UserContext userContext);

	/**
	 * 批量审核卫星店铺
	 * 
	 * @author 郝江奎
	 * @date 2016年1月19日 下午19:58:17
	 * 
	 * @param distShops
	 * @return
	 */
	boolean batchUpdateDistShopsAudit(List<String> list, Integer auditStatus, String auditorDesc, UserContext userContext);

	/**
	 * 店铺后台分页查询卫星店铺
	 * 
	 * @author 郝江奎
	 * @date 2016年1月19日 上午10:15:17
	 * 
	 * @param paginatedFilter
	 * @return DistShop
	 */
	PaginatedList<DistShop> getDistShopsByFilterAndShopId(PaginatedFilter paginatedFilter, Integer shopId);

	// ---------------------------卫星店备忘录-------------------------------------------------
	boolean saveDistShopMemo(DistShopMemo distShopMemo);

	boolean updateDistShopMemo(DistShopMemo distShopMemo);

	boolean deleteDistShopMemoById(Integer id);

	DistShopMemo getDistShopMemoById(Integer id);

	DistShopMemo getDistShopMemoByIdAndDistShopId(Integer id, Integer distShopId);

	/**
	 * 卫星店备忘录分页
	 * 
	 * @author 邓华锋
	 * @date 2016年02月19日 19:11:12
	 * 
	 * @param paginatedFilter
	 *            like distShopName,like title,like content
	 * @return
	 */
	PaginatedList<DistShopMemo> getDistShopMemosByFilter(PaginatedFilter paginatedFilter);

	// -------------------------------------------卫星店服务--------------------------------
	boolean saveDistShopSvc(DistShopSvc distShopSvc);

	boolean updateDistShopSvc(DistShopSvc distShopSvc);

	boolean deleteDistShopSvcById(Long id);

	boolean deleteDistShopSvc(Integer distShopId, Integer svcId);

	DistShopSvc getDistShopSvcById(Long id);
	
	DistShopSvc getDistShopSvcByDistShopIdAndSvcId(Integer distShopId, Integer svcId);
	/**
	 * 卫星店服务分页
	 * 
	 * @author 邓华锋
	 * @date 2016年02月20日 15:31:54
	 * 
	 * @param paginatedFilter
	 *            like auditorName,like auditDesc, =applyFlag, =auditStatus
	 * @return
	 */
	PaginatedList<DistShopSvc> getDistShopSvcsByFilter(PaginatedFilter paginatedFilter);

	List<DistShopSvc> getDistShopSvcsByFilter(MapContext requestData);

	/**
	 * 获取分页的最新变化/需要重新索引的(indexTime == null || changeTime != null && changeTime > indexTime) <br/>
	 */
	PaginatedList<DistShop> getDistShopsByLatestChanges(PaginatedFilter pager);

	/**
	 * 
	 * 更新为已索引
	 */
	boolean updateDistShopAsIndexed(Integer id, Date newIndexTime);
	// ---------------------------------------------------------------------------------
	
	// -------------------------------------卫星店结算相关---------------------------------------
	
	/**
	 * 根据结算编号获取结算订单信息
	 * 
	 * @author 李江
	 * @date 2015年11月10日 下午4:36:23
	 * 
	 * @param id
	 * @return
	 */
	List<SaleOrder> selectSaleOrderByDistSettleRecId(Integer id);
	
	/**
	 * 查询结算记录信息
	 * 
	 * @author 李江
	 * @date 2015年11月10日 下午4:36:23
	 * 
	 * @param filter
	 * @return
	 */
	PaginatedList<DistSettleRec> selectDistSettleRecByFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 合作店铺订单结算
	 * 
	 * @author 李江
	 * @date 2015年11月10日 下午4:36:23
	 * 
	 * @param orderId
	 * @param distSettleRec
	 * @return
	 */
	boolean updateDistShopSettleByOrderId(Long orderId, DistSettleRec distSettleRec);
	
	/**
	 * 合作店铺订单批量结算
	 * 
	 * @author 李江
	 * @date 2015年11月10日 下午4:36:23
	 * 
	 * @param orderIds
	 * @param distSettleRec
	 * @return
	 */
	boolean updateDistShopSettleByOrderIds(List<Long> orderIds, DistSettleRec distSettleRec);
	
}
