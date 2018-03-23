package priv.starfish.mall.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.comn.entity.BizLicense;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.shop.dto.ShopDto;
import priv.starfish.mall.shop.dto.ShopParamDto;
import priv.starfish.mall.shop.entity.Shop;
import priv.starfish.mall.shop.entity.ShopAlbumImg;
import priv.starfish.mall.shop.entity.ShopAuditRec;
import priv.starfish.mall.shop.entity.ShopBizStatus;
import priv.starfish.mall.shop.entity.ShopGrade;
import priv.starfish.mall.shop.entity.ShopMemo;
import priv.starfish.mall.shop.entity.ShopNotice;
import priv.starfish.mall.shop.entity.ShopParam;
import priv.starfish.mall.shop.entity.ShopProduct;
import priv.starfish.mall.shop.entity.ShopSvc;

public interface ShopService extends BaseService {

	// --------------------------------------- ShopGrade ----------------------------------------------
	/**
	 * 根据Id 查询店铺评分等级
	 * 
	 * @author zjl
	 * @date 2015年5月20日 下午4:15:17
	 * 
	 * @param shopGradeId
	 * @return ShopGrade
	 */
	ShopGrade getShopGradeById(Integer shopGradeId);

	/**
	 * 根据级别和档位查找商铺评分等级
	 * 
	 * @author zjl
	 * @date 2015年5月20日 下午4:21:39
	 * 
	 * @param grade
	 *            评分级别
	 * @param level
	 *            评分档位
	 * @return ShopGrade
	 */
	ShopGrade getShopGradeByGradeAndLevel(Integer grade, Integer level);

	/**
	 * 添加商品评分等级
	 * 
	 * @author zjl
	 * @date 2015年5月20日 下午4:27:48
	 * 
	 * @param shopGrade
	 * @return
	 */
	boolean saveShopGrade(ShopGrade shopGrade);

	/**
	 * 更新商铺评分等级
	 * 
	 * @author zjl
	 * @date 2015年5月20日 下午4:28:13
	 * 
	 * @param shopGrades
	 * @return
	 */
	boolean updateShopGrades(List<ShopGrade> shopGrades);

	/**
	 * 根据ID 删除评分等级
	 * 
	 * @author zjl
	 * @date 2015年5月20日 下午4:28:33
	 * 
	 * @param shopGradeId
	 * @return
	 */
	boolean deleteShopGradeById(Integer shopGradeId);

	/**
	 * 分页查询商铺评分等级
	 * 
	 * @author zjl
	 * @date 2015年5月20日 下午4:56:56
	 * 
	 * @param paginatedFilter
	 * @return PaginatedList<ShopGrade>
	 */
	PaginatedList<ShopGrade> getShopGrades();

	/**
	 * 查询最小分数和最大分数之间存不存在其他等级
	 * 
	 * @author zjl
	 * @date 2015年7月20日 下午4:22:43
	 * 
	 * @param lowerPoint
	 * @param upperPoint
	 * @return
	 */
	List<ShopGrade> getShopGradesByLowerAndUpperPoint(Integer lowerPoint, Integer upperPoint);

	/**
	 * 查询分数在那个等级内
	 * 
	 * @author zjl
	 * @date 2015年7月20日 下午5:24:03
	 * 
	 * @param point
	 * @return
	 */
	List<ShopGrade> getGradesByPoint(Integer point);

	// --------------------------------------- Shop ----------------------------------------------
	/**
	 * 添加商品
	 * 
	 * @author 郝江奎
	 * @date 2015年10月17日 下午3:33:41
	 * 
	 * @param name
	 * @return
	 */
	boolean saveShop(Shop shop);

	/**
	 * 根据名称查询店铺
	 * 
	 * @author zjl
	 * @date 2015年5月27日 下午4:33:41
	 * 
	 * @param name
	 * @return
	 */
	Shop getShopByName(String name);

	/**
	 * 分页查询商铺
	 * 
	 * @author zjl
	 * @date 2015年5月20日 下午4:56:56
	 * 
	 * @param paginatedFilter
	 * @return PaginatedList<Shop>
	 */
	PaginatedList<ShopDto> getShops(PaginatedFilter paginatedFilter);

	/**
	 * 更改店铺开启关闭
	 * 
	 * @author zjl
	 * @date 2015年5月27日 下午7:15:15
	 * 
	 * @param id
	 *            店铺ID
	 * @param closed
	 *            店铺状态
	 * @return boolean
	 */
	boolean updateShopCloseState(Integer id, boolean closed);

	/**
	 * 更改店铺启用禁用
	 * 
	 * @author 郝江奎
	 * @date 2015年10月12日 下午16:15:15
	 * 
	 * @param id
	 *            店铺ID
	 * @param disabled
	 *            店铺状态
	 * @return boolean
	 */
	boolean updateShopDisableState(Integer id, boolean disabled);

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
	boolean updateShopAudit(ShopAuditRec shopAuditRec, Integer auditorId, String auditorName, Date auditTime, Integer auditStatus);

	/**
	 * 批量审核店铺
	 * 
	 * @author zjl
	 * @date 2015年5月28日 下午7:58:47
	 * 
	 * @param ids
	 *            店铺IDs
	 * @param auditStatus审核状态
	 * @return
	 */
	boolean batchUpdateShopsAudit(List<String> ids, Integer auditStatus, ShopAuditRec shopAuditRec);

	/**
	 * 根据ID删除店铺
	 * 
	 * @author zjl
	 * @date 2015年5月28日 下午6:47:28
	 * 
	 * @param id
	 *            店铺ID
	 * @return
	 */
	boolean deleteShopById(Integer id);

	/**
	 * 批量删除店铺
	 * 
	 * @author zjl
	 * @date 2015年5月28日 下午7:03:27
	 * 
	 * @param ids
	 *            店铺IDs
	 * @return
	 */
	boolean batchDeleteShops(List<String> ids);

	/**
	 * 根据ID获取店铺信息
	 * 
	 * @author zjl
	 * @date 2015年6月8日 下午7:12:51
	 * 
	 * @param id
	 * @return
	 */
	ShopDto getShopInfoById(Integer id);

	/**
	 * 根据店铺Id查询店铺信息（前台用）
	 * 
	 * @author zjl
	 * @date 2015年7月23日 下午5:19:52
	 * 
	 * @param shopId
	 *            店铺信息
	 * @return
	 */
	Shop getShopById(Integer shopId);

	/**
	 * 
	 * 更新店铺更改时间
	 */
	boolean updateChangeTime(Integer id, boolean changeOrNot);

	// --------------------------------------- shopGoodsCat ----------------------------------------------
	/**
	 * 通过分类Id获取店铺数量
	 * 
	 * @author 廖晓远
	 * @date 2015-5-29 上午11:36:56
	 * 
	 * @param catId
	 * @return
	 */
	int getShopCountByCatId(Integer catId);

	/**
	 * 修改店铺（联系人，店铺名称，联系电话，所在地，街道，店铺领域，店铺Logo 是否自营，固定电话等字段）
	 * 
	 * @author zjl
	 * @date 2015年6月1日 下午6:16:10
	 * 
	 * @param shop
	 * @return
	 */
	boolean updateShop(Shop shop);

	// -------------------------------------------店铺参数设置------------------------------------------------

	/**
	 * 根据code和shopId查询店铺参数
	 * 
	 * @author 毛智东
	 * @date 2015年6月9日 下午6:13:52
	 * 
	 * @param shopId
	 * @param code
	 * @return
	 */
	ShopParam getShopParamByShopIdAndCode(Integer shopId, String code);

	/**
	 * 保存店铺参数
	 * 
	 * @author 毛智东
	 * @date 2015年6月9日 下午6:13:56
	 * 
	 * @param shopParam
	 * @return
	 */
	boolean saveShopParamDto(ShopParamDto shopParamDto);

	// -------------------------------------------店铺相册--------------------------------------------------------

	/**
	 * 分页查询店铺公告
	 * 
	 * @author 毛智东
	 * @date 2015年8月14日 下午6:08:53
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<ShopAlbumImg> getShopImages(PaginatedFilter paginatedFilter);

	/**
	 * 保存商铺图片
	 * 
	 * @author 郝江奎
	 * @date 2015年10月29日 下午3:23:02
	 * 
	 * @param shopImage
	 * @return
	 */
	boolean saveShopImage(ShopAlbumImg shopAlbumImg);

	/**
	 * 修改商铺图片
	 * 
	 * @author 郝江奎
	 * @date 2015年10月29日 下午3:23:02
	 * 
	 * @param shopImage
	 * @return
	 */
	boolean updateShopImage(ShopAlbumImg shopAlbumImg);

	/**
	 * 删除店铺图片
	 * 
	 * @author 郝江奎
	 * @date 2015年10月29日 下午4:21:11
	 * 
	 * @param imageId
	 * @return
	 */
	boolean deleteShopImageById(Integer imageId);

	/**
	 * 批量删除店铺图片
	 * 
	 * @author 郝江奎
	 * @date 2015年10月29日 下午4:21:15
	 * 
	 * @param imageIds
	 * @return
	 */
	boolean deleteShopImagesByIds(List<Integer> imageIds);

	// -------------------------------------------店铺公告--------------------------------------------------------

	/**
	 * 分页查询店铺公告
	 * 
	 * @author 毛智东
	 * @date 2015年8月14日 下午6:08:53
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<ShopNotice> getShopNotices(PaginatedFilter paginatedFilter);

	/**
	 * 保存商铺公告
	 * 
	 * @author 毛智东
	 * @date 2015年6月10日 上午10:23:02
	 * 
	 * @param shopNotice
	 * @return
	 */
	boolean saveShopNotice(ShopNotice shopNotice);

	/**
	 * 修改店铺公告
	 * 
	 * @author 毛智东
	 * @date 2015年8月14日 下午6:21:08
	 * 
	 * @param shopNotice
	 * @return
	 */
	boolean updateShopNotice(ShopNotice shopNotice);

	/**
	 * 删除店铺公告
	 * 
	 * @author 毛智东
	 * @date 2015年8月14日 下午6:21:11
	 * 
	 * @param noticeId
	 * @return
	 */
	boolean deleteShopNoticeById(Integer noticeId);

	/**
	 * 批量删除店铺公告
	 * 
	 * @author 毛智东
	 * @date 2015年8月14日 下午6:21:15
	 * 
	 * @param noticeIds
	 * @return
	 */
	boolean deleteShopNoticesByIds(List<Integer> noticeIds);

	/**
	 * 获取指定条件下的某商户的所有营业执照
	 * 
	 * @author guoyn
	 * @date 2015年10月10日 下午2:12:33
	 * 
	 * @param paginatedFilter
	 *            =userId
	 * @return PaginatedList<BizLicense>
	 */
	PaginatedList<BizLicense> getBizLicensesByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 增加企业营业执照
	 * 
	 * @author guoyn
	 * @date 2015年10月10日 下午3:27:36
	 * 
	 * @param bizLicense
	 * @return boolean
	 */
	boolean createBizLicense(BizLicense bizLicense);

	/**
	 * 查询某商户下的店铺
	 * 
	 * @author guoyn
	 * @date 2015年10月12日 下午5:45:18
	 * 
	 * @param merchantId
	 * @param shopName
	 * @return Shop
	 */
	Shop getShopByMerchantIdAndName(Integer merchantId, String shopName);

	// ------------------------------------店铺的商品处理----------------------------------------

	/**
	 * 根据条件查询店铺中的产品列表
	 * 
	 * @author guoyn
	 * @date 2015年10月17日 下午4:02:44
	 * 
	 * @param paginatedFilter
	 *            like goodsName, =catId, =lackFlag, =productId
	 * @return PaginatedList<ShopProduct>
	 */
	PaginatedList<ShopProduct> getShopProductsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 更新店铺的商品中的缺货状态
	 * 
	 * @author guoyn
	 * @date 2015年10月17日 下午6:19:37
	 * 
	 * @param shopProductId
	 * @param lackFlag
	 * @return Boolean
	 */
	Boolean updateShopProductLackFlagById(Long shopProductId, Boolean lackFlag);

	/**
	 * 店铺店家产品
	 * 
	 * @author guoyn
	 * @date 2015年10月20日 下午4:39:24
	 * 
	 * @param shopId
	 * @param productId
	 * @return boolean
	 */
	boolean saveProduct(Integer shopId, Long productId);

	/**
	 * 根据过滤条件查询店铺中是否存在相应的产品
	 * 
	 * @author guoyn
	 * @date 2015年10月20日 下午5:05:33
	 * 
	 * @param filter
	 *            =shopId, =productId
	 * @return boolean
	 */
	boolean existShopProductByFilter(MapContext filter);

	/**
	 * 删除店铺的产品
	 * 
	 * @author guoyn
	 * @date 2015年10月20日 下午5:34:00
	 * 
	 * @param shopProductId
	 * @return boolean
	 */
	boolean deleteProductByShopProductId(Long shopProductId);

	/**
	 * 获取指定店铺指定产品的缺货状态
	 * 
	 * @author 邓华锋
	 * @date 2015年11月3日 上午10:09:43
	 * 
	 * @param shopId
	 * @param productIds
	 * @return
	 */
	Map<Long, ShopProduct> getProductLackByShopIdAndProductIds(Integer shopId, List<Long> productIds);

	/**
	 * 获取指定店铺的服务状态
	 * 
	 * @author 邓华锋
	 * @date 2016年1月18日 下午2:25:19
	 * 
	 * @param shopId
	 * @param svcIds
	 * @return
	 */
	Map<Long, ShopSvc> getSvcStatusByShopIdAndSvcIds(Integer shopId, List<Long> svcIds);

	// ---------------------------------------------------------------------------------------------
	/**
	 * 获取某个货品被多少个店铺使用
	 * 
	 * @author guoyn
	 * @date 2015年11月3日 下午3:21:25
	 * 
	 * @param productId
	 * @return int
	 */
	int getShopCountByProductId(Long productId);

	/**
	 * 批量删除店铺货品
	 * 
	 * @author guoyn
	 * @date 2015年11月19日 上午11:35:57
	 * 
	 * @param shopProductIds
	 * @return boolean
	 */
	boolean deleteShopProductsByIds(List<Long> shopProductIds);

	// ----------------------------------- 店铺人员 ---------------------------------------
	/**
	 * 返回店铺所有（有角色）的人员
	 * 
	 * @author koqiui
	 * @date 2015年12月17日 上午12:33:26
	 * 
	 * @param shopId
	 * @param includeRoles
	 *            是否包含角色信息
	 * @return
	 */
	List<User> getShopWorkersById(Integer shopId, boolean includeRoles);

	// ------------------------------------ 店铺营业状态 ------------------------------------
	/**
	 * 获取店铺当日营业状态
	 * 
	 * @author koqiui
	 * @date 2015年12月17日 上午2:57:37
	 * 
	 * @param shopId
	 * @return
	 */
	ShopBizStatus getShopBizStatusById(Integer shopId);

	/**
	 * 获取店铺某日营业状态
	 * 
	 * @author koqiui
	 * @date 2015年12月17日 下午4:07:17
	 * 
	 * @param shopId
	 * @param bizDate
	 * @return
	 */
	ShopBizStatus getShopBizStatusByIdAndDate(Integer shopId, Date bizDate);

	/**
	 * 设置/更新店铺目前营业状态（会自动更具 dateStr 生成 bizDate）
	 * 
	 * @author koqiui
	 * @date 2015年12月17日 上午2:59:14
	 * 
	 * @param shopBizStatus
	 * @return
	 */
	boolean saveShopBizStatus(ShopBizStatus shopBizStatus);

	/**
	 * 根据缺货状态获取某一店铺下的所有商品id
	 */
	List<Integer> getProductIdsByShopIdAndLackFlag(Integer shopId);

	/**
	 * 
	 * * 更新为已索引
	 * 
	 * @return
	 */
	boolean updateShopAsIndexed(Integer id, Date newIndexTime);

	/**
	 * 获取分页的最新变化/需要重新索引的(indexTime == null || changeTime != null && changeTime > indexTime) <br/>
	 */
	PaginatedList<ShopDto> getShopsByLatestChanges(PaginatedFilter pager);

	// ---------------------------店铺备忘录-------------------------------------------------
	boolean saveShopMemo(ShopMemo shopMemo);

	boolean updateShopMemo(ShopMemo shopMemo);

	boolean deleteShopMemoById(Integer id);

	ShopMemo getShopMemoById(Integer id);

	ShopMemo getShopMemoByIdAndShopId(Integer id, Integer shopId);

	/**
	 * 店铺备忘录分页
	 * 
	 * @author 邓华锋
	 * @date 2016年02月19日 18:09:44
	 * 
	 * @param paginatedFilter
	 *            like shopName,like title,like content
	 * @return
	 */
	PaginatedList<ShopMemo> getShopMemosByFilter(PaginatedFilter paginatedFilter);
	// -----------------------------------------------------------------------------------
}
