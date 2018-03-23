package priv.starfish.mall.service;

import java.util.List;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.shop.entity.ShopSvc;
import priv.starfish.mall.svcx.entity.SvcGroup;
import priv.starfish.mall.svcx.entity.SvcKind;
import priv.starfish.mall.svcx.entity.SvcPack;
import priv.starfish.mall.svcx.entity.SvcPackItem;
import priv.starfish.mall.svcx.entity.Svcx;

public interface CarSvcService extends BaseService {

	// ---------------------------------------车辆服务分组--------------------------------------------
	/**
	 * 获取车辆服务分组列表
	 * 
	 * @author 李超杰
	 * @date 2015年10月12日 下午4:28:21
	 * 
	 * @return
	 */
	public List<SvcGroup> getCarSvcGroups();

	/**
	 * 判断是否已经存在车辆服务分组
	 * 
	 * @author 李超杰
	 * @date 2015年10月14日 下午7:09:35
	 * 
	 * @param name
	 * @return
	 */
	boolean existCarSvcGroupByNameAndKindId(String groupName, Integer kindId);

	/**
	 * 保存车辆服务分组
	 * 
	 * @author 李超杰
	 * @date 2015年10月12日 下午4:48:04
	 * 
	 * @param faqCat
	 * @return
	 */
	boolean saveCarSvcGroup(SvcGroup carSvcGroup);

	/**
	 * 更新车辆服务分组
	 * 
	 * @author 李超杰
	 * @date 2015年10月12日 下午4:49:37
	 * 
	 * @param SvcGroup
	 * @return
	 */
	boolean updateCarSvcGroup(SvcGroup carSvcGroup);

	/**
	 * 删除车辆服务分组
	 * 
	 * @author 李超杰
	 * @date 2015年10月12日 下午4:50:10
	 * 
	 * @param id
	 * @return
	 */
	boolean updateSvcGroupFordeleted(Integer id);

	/**
	 * 
	 * 获取分页车辆服务分组列表
	 * 
	 * @author 李超杰
	 * @date 2015年10月12日 下午8:58:48
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SvcGroup> getCarSvcGroupsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 获取车辆服务分组列表前台用
	 * 
	 * @author 李超杰
	 * @date 2015年10月12日 下午4:28:21
	 * 
	 * @return
	 */
	public List<SvcGroup> getCarSvcGroupsFrout();

	// ---------------------------------------车辆服务--------------------------------------------
	/**
	 * 
	 * 获取车辆服务列表
	 * 
	 * @author 李超杰
	 * @date 2015年10月13日 下午2:49:40
	 * 
	 * @return
	 */
	public List<Svcx> getCarSvcsByGroupId(Integer groupId);

	/**
	 * 保存车辆服务分组
	 * 
	 * @author 李超杰
	 * @date 2015年10月12日 下午4:48:04
	 * 
	 * @param faqCat
	 * @return
	 */
	boolean saveCarSvc(Svcx carSvc);

	/**
	 * 更新车辆服务分组
	 * 
	 * @author 李超杰
	 * @date 2015年10月12日 下午4:49:37
	 * 
	 * @param SvcGroup
	 * @return
	 */
	boolean updateCarSvc(Svcx carSvc);

	/**
	 * 删除车辆服务分组
	 * 
	 * @author 李超杰
	 * @date 2015年10月12日 下午4:50:10
	 * 
	 * @param id
	 * @return
	 */
	boolean updateCarSvcForDeleted(Integer id);

	/**
	 * 根据车辆服务分组ID删除车辆服务
	 * 
	 * @author MCIUJavaDept
	 * @date 2015年10月13日 下午3:01:53
	 * 
	 * @param groupId
	 * @return
	 */
	boolean updateSvcForDeleteByGroupId(Integer groupId);

	/**
	 * 
	 * 获取分页车辆服务分组列表
	 * 
	 * @author 李超杰
	 * @date 2015年10月12日 下午8:58:48 question like '%name%'
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<Svcx> getCarSvcsByFilter(PaginatedFilter paginatedFilter);

	List<Svcx> getCarSvcsByGroupIdAndState(Integer groupId);

	Svcx getCarSvc(Integer id);

	List<Svcx> getCarSvcs();

	boolean existCarSvcByGroupIdAndName(Integer groupId, String name);

	// ----------------------------------------分类-------------------------------------------------
	List<SvcKind> getSvcKindsByFilter();

	// -----------------------------------------服务套餐------------------------------------------------------

	boolean saveSvcPack(SvcPack svcPack);

	boolean updateSvcPack(SvcPack svcPack);

	boolean updateSvcPackForDeleted(SvcPack svcPack);

	boolean updateSvcPackForDisabled(SvcPack svcPack);

	PaginatedList<SvcPack> getSvcPacksByFilter(PaginatedFilter paginatedFilter);

	SvcPack getSvcPackById(Integer packId);

	boolean existSvcPackKindIdByName(Integer kindId, String name);

	List<SvcPack> getSvcPacksByFilter(MapContext requestData);

	// -----------------------------------------服务套餐明细------------------------------------------------------
	List<SvcPackItem> getSvcPackItemsByPackId(Integer packId);

	boolean saveSvcPackItem(SvcPackItem svcPackItem);

	boolean updateSvcPackItem(SvcPackItem svcPackItem);

	// -----------------------------------------店铺服务--------------------------------------------------------
	/**
	 * 分页查询店铺服务列表
	 * 
	 * @param paginatedFilter:
	 *            like svcName
	 * @return PaginatedList<ShopSvc>
	 */
	public PaginatedList<ShopSvc> getShopCarSvcsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 保存店铺服务信息
	 * 
	 * @param svcId
	 * @param shopId
	 * @return boolean
	 */
	public boolean saveShopSvcBySvcIdAndShopId(Integer svcId, Integer shopId);

	/**
	 * 删除店铺服务信息
	 * 
	 * @param svcId
	 * @param shopId
	 * @return boolean
	 */
	public boolean deleteShopSvcBySvcIdAndShopId(Integer svcId, Integer shopId);

	/**
	 * 根据店铺服务id删除数据
	 * 
	 * @param shopSvcId
	 * @return boolean
	 */
	public boolean deleteShopSvcById(Long shopSvcId);

	/**
	 * 获取店铺下的所有车辆服务信息
	 */
	public List<Svcx> getCarSvcsByShopId(Integer shopId);

	public List<ShopSvc> getShopSvcsByFilter(MapContext requestData);

	public boolean deleteShopSvcBySvcId(Integer svcId);
}
