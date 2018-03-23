package priv.starfish.mall.service;

import java.util.Date;
import java.util.List;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.categ.entity.GoodsCatSpecItem;
import priv.starfish.mall.goods.dto.ProductDto;
import priv.starfish.mall.goods.entity.Goods;
import priv.starfish.mall.goods.entity.GoodsAlbumImg;
import priv.starfish.mall.goods.entity.GoodsColorImg;
import priv.starfish.mall.goods.entity.GoodsIntro;
import priv.starfish.mall.goods.entity.GoodsIntroImg;
import priv.starfish.mall.goods.entity.Product;
import priv.starfish.mall.goods.entity.ProductAlbumImg;

public interface GoodsService extends BaseService {

	// ------------------------------商品------------------------------------

	/**
	 * 保存商品信息及关联商品属性值信息
	 * 
	 * @author guoyn
	 * @date 2015年6月19日 下午3:42:18
	 * 
	 * @param goods
	 * @return boolean
	 */
	boolean saveGoodsBasisInfo(Goods goods);

	/**
	 * 保存货品及货品规格值
	 * 
	 * @author guoyn
	 * @date 2015年6月19日 下午6:47:54
	 * 
	 * @param product
	 * @return
	 */
	boolean saveProduct(Product product);

	/**
	 * 分页查询商品
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<Goods> getGoodsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 批量查出货品列表
	 * 
	 * @author guoyn
	 * @date 2015年6月23日 下午5:30:30
	 * 
	 * @param products
	 * @return boolean
	 */
	boolean batchSaveProduct(List<Product> products);

	/**
	 * 根据id查询商品
	 * 
	 * @author guoyn
	 * @date 2015年6月24日 下午3:58:51
	 * 
	 * @param goodsId
	 * @return Goods
	 */
	Goods getGoodsBasisInfoById(Integer goodsId);

	/**
	 * 获取某个商品的所有未被删除货品
	 * 
	 * @author guoyn
	 * @date 2015年6月24日 下午5:50:09
	 * 
	 * @param goodsId
	 * @return List<Product>
	 */
	List<Product> getProductsByGoodsId(Integer goodsId);

	/**
	 * 批量更新货品及货品规格值
	 * 
	 * @author guoyn
	 * @date 2015年6月25日 下午2:50:21
	 * 
	 * @param products
	 * @return boolean
	 */
	boolean batchUpdateProduct(List<Product> products);

	/**
	 * 保存商品相册
	 * 
	 * @author guoyn
	 * @date 2015年6月26日 下午5:50:14
	 * 
	 * @param albumImgs
	 * @return boolean
	 */
	boolean batchSaveAlbumImgs(List<GoodsAlbumImg> albumImgs);

	/**
	 * 通过上下文分页查询店铺所有货品
	 * 
	 * @author 廖晓远
	 * @date 2015年6月26日 上午11:14:30
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<Product> getProductsByContext(PaginatedFilter paginatedFilter);

	/**
	 * 根据品牌的id和商品分类id条件查询货品及货品相册列表
	 * 
	 * @author guoyn
	 * @date 2015年10月19日 下午7:15:07
	 * 
	 * @param paginatedFilter
	 *            =brandId, =catId
	 * @return PaginatedList<Product>
	 */
	PaginatedList<Product> getProductsByBrandIdAndFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 根据品牌的名称和商品分类条件查询货品及货品相册列表
	 * 
	 * @author guoyn
	 * @date 2015年11月9日 下午8:23:38
	 * 
	 * @param brandName
	 * @param paginatedFilter
	 * @return PaginatedList<Product>
	 */
	PaginatedList<Product> getProductsByBrandNameAndFilter(String brandName, PaginatedFilter paginatedFilter);

	/**
	 * 根据商品id和店铺Id查询商品相册
	 * 
	 * @author guoyn
	 * @date 2015年6月29日 下午12:01:11
	 * 
	 * @param goodsId
	 * @return List<GoodsAlbumImg>
	 */
	List<GoodsAlbumImg> getAlbumImgsByGoodsId(Integer goodsId);

	/**
	 * 批量删除商品相册图片
	 * 
	 * @author guoyn
	 * @date 2015年6月29日 下午3:15:46
	 * 
	 * @param uuids
	 * @return boolean
	 */
	boolean deleteAlbumImgsByUuids(List<String> uuids);

	/**
	 * 查询分类下商品数量
	 * 
	 * @author 廖晓远
	 * @date 2015年6月29日 下午6:44:04
	 * 
	 * @return
	 */
	int countGoodsByCatId(Integer catId);

	/**
	 * 保存商品介绍信息
	 * 
	 * @author guoyn
	 * @date 2015年7月1日 下午5:50:28
	 * 
	 * @param goodsIntro
	 * @return boolean
	 */
	boolean saveGoodsIntro(GoodsIntro goodsIntro);

	/**
	 * 根据商品Id获取商品介绍信息
	 * 
	 * @author guoyn
	 * @date 2015年7月1日 下午6:25:31
	 * 
	 * @param goodsId
	 * @return GoodsIntro
	 */
	GoodsIntro getGoodsIntroByGoodsId(Integer goodsId);

	/**
	 * 批量下架商品
	 * 
	 * @author zjl
	 * @date 2015年7月1日 下午6:06:45
	 * 
	 * @param ids
	 *            货品Ids
	 * @param shelfStatus
	 *            上下架状态
	 * @param date
	 *            最近时间
	 * @return
	 */
	boolean batchDownShelfProducts(List<Long> ids, Integer shelfStatus, Date date);

	/**
	 * 批量上架商品
	 * 
	 * @author zjl
	 * @date 2015年7月1日 下午6:06:45
	 * 
	 * @param ids
	 *            货品Ids
	 * @param shelfStatus
	 *            上下架状态
	 * @param date
	 *            最近时间
	 * @return
	 */
	boolean batchUpShelfProducts(List<Long> ids, Integer shelfStatus, Date date);

	/**
	 * 查询某商品的商品介绍图片
	 * 
	 * @author guoyn
	 * @date 2015年7月2日 下午2:38:31
	 * 
	 * @param goodsId
	 * @return List<GoodsIntroImg>
	 */
	List<GoodsIntroImg> getIntroImgsByGoodsId(Integer goodsId);

	/**
	 * 批量保存商品介绍图片
	 * 
	 * @author guoyn
	 * @date 2015年7月3日 下午2:46:16
	 * 
	 * @param introImgs
	 * @return boolean
	 */
	boolean batchSaveIntroImgs(List<GoodsIntroImg> introImgs);

	/**
	 * 根据uuid删除商品介绍图片
	 * 
	 * @author guoyn
	 * @date 2015年7月3日 下午3:31:11
	 * 
	 * @param uuid
	 * @return boolean
	 */
	boolean deleteIntroImgByUuid(String uuid);

	/**
	 * 更新商品介绍
	 * 
	 * @author guoyn
	 * @date 2015年7月6日 下午6:50:56
	 * 
	 * @param goodsIntro
	 * @return
	 */
	boolean updateGoodsIntro(GoodsIntro goodsIntro);

	/**
	 * 保存商品
	 * 
	 * @author guoyn
	 * @date 2015年7月6日 下午6:51:10
	 * 
	 * @param goods
	 * @return boolean
	 */
	boolean saveOrUpdateGoods(Goods goods);

	/**
	 * 根据id删除产品（某商品下没有货品后，此商品也将被删除）
	 * 
	 * @author guoyn
	 * @date 2015年7月7日 上午9:49:10
	 * 
	 * @param id
	 * @return boolean
	 */
	boolean deleteProductRelatedById(Long id);

	/**
	 * 根据id列表批量删除产品
	 * 
	 * @author 廖晓远
	 * @date 2015年8月28日 上午11:32:04
	 * 
	 * @param ids
	 * @return
	 */
	boolean deleteProductByIds(List<Long> ids);

	/**
	 * 更新产品
	 * 
	 * @author guoyn
	 * @date 2015年7月7日 上午10:15:03
	 * 
	 * @param product
	 * @return boolean
	 */
	boolean updateProduct(Product product);

	/**
	 * 删除商品相册图片
	 * 
	 * @author guoyn
	 * @date 2015年7月10日 下午6:06:32
	 * 
	 * @param uuid
	 * @return boolean
	 */
	boolean deleteGoodsAlbumImgByUuid(String uuid);

	/**
	 * 删除货品相册图片
	 * 
	 * @author guoyn
	 * @date 2015年7月10日 下午6:02:40
	 * 
	 * @param id
	 * @return boolean
	 */
	boolean deleteProductAlbumImgById(Long id);

	/**
	 * 根据商品相册图片uuid获取货品相册图片列表
	 * 
	 * @author guoyn
	 * @date 2015年7月14日 下午6:22:19
	 * 
	 * @param uuid
	 * @return List<ProductAlbumImg>
	 */
	List<ProductAlbumImg> getProductAlbumImgsByUuid(String uuid);

	/**
	 * 根据商品相册图片uuid获取货品相册图片列表Ids
	 * 
	 * @author guoyn
	 * @date 2015年7月14日 下午6:36:35
	 * 
	 * @param uuid
	 * @return List<Long>
	 */
	List<Long> getProductAlbumImgIdsByUuid(String uuid);

	/**
	 * 批量删除货品相册图片
	 * 
	 * @author guoyn
	 * @date 2015年7月14日 下午7:12:32
	 * 
	 * @param ids
	 * @return boolean
	 */
	boolean deleteProductAlbumImgByIds(List<Long> ids);

	/**
	 * 根据Id 获取产品信息
	 * 
	 * @author zjl
	 * @date 2015年7月22日 下午2:57:42
	 * 
	 * @param productId
	 *            产品ID
	 * @return
	 */
	Product getProductById(Long productId);

	/**
	 * 更新产品deleted状态
	 * 
	 * @author guoyn
	 * @date 2015年10月22日 下午12:01:09
	 * 
	 * @param productId
	 *            产品id
	 * @param deleted
	 *            true:假删, false:恢复
	 * @return boolean
	 */
	boolean updateProductDeletedById(Long productId, boolean deleted);

	/**
	 * 获取产品的销售价格和市场价格
	 * 
	 * @author guoyn
	 * @date 2015年10月28日 下午3:49:29
	 * 
	 * @param productIds
	 * @return
	 */
	List<ProductDto> getProductSaleAndMarketPrices(List<Long> productIds);

	/**
	 * 根据id获取产品相关信息
	 * 
	 * @author guoyn
	 * @date 2015年10月28日 下午6:56:47
	 * 
	 * @param productId
	 * @return ProductDto
	 */
	ProductDto getProductDtoByProductId(Long productId);

	/**
	 * 保存或更新商品扩展信息
	 * 
	 * @author guoyn
	 * @date 2015年10月29日 下午3:56:30
	 * 
	 * @param goodsId
	 * @return boolean
	 */
	public boolean saveGoodsExInfo(Integer goodsId);

	/**
	 * 保存或更新货品扩展信息
	 * 
	 * @author guoyn
	 * @date 2015年10月29日 下午4:20:50
	 * 
	 * @param productId
	 * @return boolean
	 */
	public boolean saveProductExInfo(Long productId);

	/**
	 * 上下架货品TODO:消息推送
	 * 
	 * @author guoyn
	 * @date 2015年10月30日 下午7:01:23
	 * 
	 * @param productId
	 * @param shelfStatus
	 * @return boolean
	 */
	boolean updateProductShelfStatus(Long productId, Integer shelfStatus);

	/**
	 * 获取商品所有颜色规格枚举项值列表
	 * 
	 * @author guoyn
	 * @date 2015年11月2日 上午10:36:38
	 * 
	 * @param goodsId
	 * @return List<GoodsCatSpecItem>
	 */
	List<GoodsCatSpecItem> getColorSpecItemsByGoodsId(Integer goodsId);

	/**
	 * 保存商品颜色图片
	 * 
	 * @author guoyn
	 * @date 2015年11月2日 下午6:47:24
	 * 
	 * @param goodsColorImgs
	 * @return boolean
	 */
	boolean saveGoodsColorImgs(List<GoodsColorImg> goodsColorImgs);

	/**
	 * 获取商品下的所有颜色图片集合
	 * 
	 * @author guoyn
	 * @date 2015年11月3日 上午10:27:15
	 * 
	 * @param goodsId
	 * @return List<GoodsColorImg>
	 */
	List<GoodsColorImg> getGoodsColorImgsByGoodsId(Integer goodsId);

	/**
	 * 根据规格值字符key获取某商品下的货品
	 * 
	 * @author guoyn
	 * @date 2015年11月4日 下午8:35:36
	 * 
	 * @param specStr
	 * @param goodsId
	 * @return ProductDto
	 */
	ProductDto getProductDtoBySpecStrAndGoodsId(String specStr, Integer goodsId);

	/**
	 * 删除商品相关数据
	 * 
	 * @author guoyn
	 * @date 2015年11月5日 下午2:42:53
	 * 
	 * @param goodsId
	 * @return boolean
	 */
	boolean deleteGoodsRelatedByGoodsId(Integer goodsId);

	/**
	 * 查询某个货品所属商品的关联信息
	 * 
	 * @author guoyn
	 * @date 2015年11月7日 上午10:25:45
	 * 
	 * @param specStr
	 * @param goodsId
	 * @return Long
	 */
	Long getProductIdBySpecStrAndGoodsId(String specStr, Integer goodsId);

	/**
	 * 根据过滤条件查询所有产品及货品相册
	 * 
	 * @author guoyn
	 * @date 2015年10月19日 下午7:15:07
	 * 
	 * @param paginatedFilter
	 *            =catId, =shelfStatus, like goodsName...
	 * @return PaginatedList<Product>
	 */
	PaginatedList<Product> getProductsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 验证商品名称是否被占用
	 * 
	 * @author guoyn
	 * @date 2015年11月27日 下午2:51:08
	 * 
	 * @param goodsName
	 * @param categId
	 * @param vendorId
	 * @param shopId
	 * @return boolean
	 */
	boolean existGoodName(String goodsName, Integer categId, Integer vendorId, Integer shopId);
	
	/** 
	 * 更新用户订单的商品购买数量
	 * 
	 * @author 邓华锋
	 * @date 2015年12月7日 上午11:27:32
	 * 
	 * @param orderNo
	 * @param userId
	 */
	 void updateGoodsBuySumByOrderNoAndUserId(String orderNo, Integer userId);

	 /**
	  * 获取某商品下的所有未上架及下架的货品
	  * 
	  * @author guoyn
	  * @date 2015年12月24日 下午3:23:12
	  * 
	  * @param goodsId
	  * @return
	  */
	List<Product> getUnShelveProductsByGoodsId(Integer goodsId);

	/**
	 * 删除某商品的颜色分组图片
	 * 
	 * @author guoyn
	 * @date 2015年12月25日 下午5:56:03
	 * 
	 * @param goodsId
	 * @return boolean
	 */
	boolean deleteGoodsColorImgs(Integer goodsId);
}
