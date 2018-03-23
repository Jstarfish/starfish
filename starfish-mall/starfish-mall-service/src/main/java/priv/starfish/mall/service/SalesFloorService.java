package priv.starfish.mall.service;

import java.util.List;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.goods.entity.Product;
import priv.starfish.mall.market.entity.SalesFloor;
import priv.starfish.mall.market.entity.SalesRegion;
import priv.starfish.mall.market.entity.SalesRegionGoods;
import priv.starfish.mall.market.entity.SalesRegionSvc;

public interface SalesFloorService extends BaseService {

	SalesFloor getSalesFloorByNo(Integer No);
	
	SalesFloor getSalesFloorByName(String name);

	boolean saveSalesFloor(SalesFloor salesFloor);

	boolean delSalesFloor(Integer No);

	/**
	 * 分页查询楼层
	 * 
	 * @author 郝江奎
	 * @date 2016年1月30日 下午2:41:08
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SalesFloor> getSalesFloorsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 通过type获取楼层
	 * 
	 * @author 郝江奎
	 * @date 2016年1月30日 下午5:12:52
	 * 
	 * @param type
	 * @return
	 */
	List<SalesFloor> getSalesFloorByType(Integer type);
	
	//-----------------------------------------销售专区--------------------------------------
	
	SalesRegion getSalesRegionById(Integer id);

	boolean saveSalesRegion(SalesRegion salesRegion);

	boolean delSalesRegionById(Integer id);

	/**
	 * 分页查询楼层
	 * 
	 * @author 郝江奎
	 * @date 2016年1月30日 下午2:41:08
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SalesRegion> getSalesRegionsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 通过type获取楼层
	 * 
	 * @author 郝江奎
	 * @date 2016年1月30日 下午5:12:52
	 * 
	 * @param type
	 * @return
	 */
	List<SalesRegion> getSalesRegionByType(Integer type);

	/**
	 * 获取专区商品
	 * 
	 * @author 郝江奎
	 * @date 2016年2月1日 下午4:39:21
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SalesRegionGoods> getSalesRegionGoodsByFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 获取非专区商品
	 * 
	 * @author 郝江奎
	 * @date 2016年2月17日 上午7:56:47
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<Product> getProductsAndFilter(PaginatedFilter paginatedFilter);

	/**
	 * 批量添加专区商品
	 * 
	 * @author 郝江奎
	 * @date 2016年2月1日 下午6:25:04
	 * 
	 * @param list
	 * @param regionId
	 * @return
	 */
	boolean saveRegionGoods(List<String> list, Integer regionId);
	
	/**
	 * 删除专区商品
	 * 
	 * @author 郝江奎
	 * @date 2016年2月2日 下午1:52:14
	 * 
	 * @param id
	 * @return
	 */
	boolean delSalesRegionGoodsById(Integer id);

	/**
	 * 查询专区服务
	 * 
	 * @author 郝江奎
	 * @date 2016年2月2日 下午2:33:45
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SalesRegionSvc> getSalesRegionSvcsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 批量添加专区服务
	 * 
	 * @author 郝江奎
	 * @date 2016年2月2日 下午2:25:04
	 * 
	 * @param list
	 * @param regionId
	 * @return
	 */
	boolean saveRegionSvc(List<String> list, Integer regionId);
	
	/**
	 * 删除专区服务
	 * 
	 * @author 郝江奎
	 * @date 2016年2月2日 下午1:52:14
	 * 
	 * @param id
	 * @return
	 */
	boolean delSalesRegionSvcById(Integer id);

	/**
	 * 根据Name和floorNo查询楼层专区
	 * 
	 * @author 郝江奎
	 * @date 2016年2月2日 下午4:36:04
	 * 
	 * @param name
	 * @param floorNo
	 * @return
	 */
	SalesRegion getSalesRegionByNameAndFloorNo(String name, Integer floorNo);

	/**
	 * 查询专区
	 * 
	 * @author 郝江奎
	 * @date 2016年2月3日 上午10:44:35
	 * 
	 * @param type
	 * @return
	 */
	List<SalesRegion> getSalesRegionListByType(Integer type);

	/**
	 * 删除专区
	 * 
	 * @author 郝江奎
	 * @date 2016年2月17日 下午2:46:18
	 * 
	 * @param id
	 * @param type
	 * @return
	 */
	boolean delSalesRegionByIdAndType(SalesRegion salesRegion);
}
