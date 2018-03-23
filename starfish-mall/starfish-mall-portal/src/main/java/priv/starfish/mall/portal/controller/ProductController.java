package priv.starfish.mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.common.util.TypeUtil.Types;
import priv.starfish.mall.cart.dict.CartAction;
import priv.starfish.mall.cart.entity.SaleCart;
import priv.starfish.mall.cart.po.SaleCartGoodsPo;
import priv.starfish.mall.categ.entity.BrandDef;
import priv.starfish.mall.categ.entity.GoodsCat;
import priv.starfish.mall.categ.entity.GoodsCatAttrGroup;
import priv.starfish.mall.categ.entity.GoodsCatSpec;
import priv.starfish.mall.goods.dto.ProductDto;
import priv.starfish.mall.goods.entity.Goods;
import priv.starfish.mall.goods.entity.GoodsIntro;
import priv.starfish.mall.goods.entity.Product;
import priv.starfish.mall.service.CategService;
import priv.starfish.mall.service.GoodsService;
import priv.starfish.mall.service.ShopService;
import priv.starfish.mall.service.StatisService;
import priv.starfish.mall.service.util.SxUtil;
import priv.starfish.mall.shop.entity.Shop;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.web.base.CartHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Remark("商品")
@Controller
@RequestMapping(value = "/product")
public class ProductController extends BaseController {

	@Resource
	private CategService categService;

	@Resource
	private GoodsService goodsService;

	@Resource
	private ShopService shopService;

	@Resource
	private StatisService statisService;

	// -------------------------------店铺后台-商品添加-----------------------------------

	/**
	 * 
	 * @author zjl
	 * @date 2015年6月11日 上午10:01:43
	 * 
	 * @return
	 */
	@Remark("商品详情界面")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public String toAddGoods() {
		return "product/productDetails";
	}

	/**
	 * 获取货品信息
	 * 
	 * @author zjl
	 * @date 2015年7月18日 上午10:31:32
	 * 
	 * @return
	 */
	@Remark("获取货品信息")
	@RequestMapping(value = "/get/by/id", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<String, Object>> getGoodMsg(@RequestBody Product productf) {
		Result<Map<String, Object>> result = Result.newOne();
		try {
			Product product = goodsService.getProductById(productf.getId());
			List<GoodsCatSpec> specs = categService.getGoodCatSpecsByCatId(product.getCatId(), product.getGoodsId());
			Map<String, Object> resultData = new HashMap<String, Object>();
			resultData.put("basic", product);
			resultData.put("goodSpecs", specs);
			result.data = resultData;
		} catch (Exception e) {
			result.type = Result.Type.warn;
		}
		return result;
	}

	/**
	 * 获取商品信息店铺信息
	 * 
	 * @author zjl
	 * @date 2015年7月18日 上午10:31:32
	 * 
	 * @return
	 */
	@Remark("获取商品信息")
	@RequestMapping(value = "/goods/by/id", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<String, Object>> getGoodAndShopMsg(@RequestBody Goods goods) {
		Result<Map<String, Object>> result = Result.newOne();
		try {
			Goods good = goodsService.getGoodsBasisInfoById(goods.getId());
			String[] catPath = good.getCatPath().split(",");
			String catString = "";
			for (String catId : catPath) {
				GoodsCat goodcat = categService.getGoodsCatById(Integer.valueOf(catId));
				catString = catString + "<a>" + goodcat.getName() + "</a> >";
			}
			catString = catString + good.getName();
			List<GoodsCat> siblingGoodsCat = categService.getSiblingGoodsCatById(good.getCatId());
			GoodsIntro goodIntro = goodsService.getGoodsIntroByGoodsId(goods.getId());
			Shop shop = shopService.getShopById(good.getShopId());
			Map<String, Object> resultData = new HashMap<String, Object>();
			resultData.put("good", good);// 商品信息
			resultData.put("goodIntro", goodIntro);// 商品介绍
			resultData.put("shop", shop);// 店铺信息
			resultData.put("catString", catString);// 分类导航
			resultData.put("sibling", siblingGoodsCat);// 同级分类
			result.data = resultData;
		} catch (Exception e) {
			result.type = Result.Type.warn;
		}
		return result;
	}

	/**
	 * 获取商品信息店铺信息
	 * 
	 * @author zjl
	 * @date 2015年7月18日 上午10:31:32
	 * 
	 * @return
	 */
	@Remark("获取商品规格")
	@RequestMapping(value = "/goodAttrVal/by/catId", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<GoodsCatAttrGroup>> getGoodAttrVal(@RequestBody Goods goods) {
		Result<List<GoodsCatAttrGroup>> result = Result.newOne();
		try {
			List<GoodsCatAttrGroup> listCatAttrGroup = categService.getCatAttrGroupByCatId(goods.getCatId());
			for (GoodsCatAttrGroup goodsCatAttrGroup : listCatAttrGroup) {
				goodsCatAttrGroup.setGoodsCatAttrs(categService.getCatAttrsByGroupIdAndGoodsId(goodsCatAttrGroup.getId(), goods.getId()));
			}
			result.data = listCatAttrGroup;
		} catch (Exception e) {
			result.type = Result.Type.warn;
		}
		return result;
	}

	// -------------------------------------产品（车品超市）-----------------------------------------------------------

	/**
	 * 车品超市频道页
	 * 
	 * @author guoyn
	 * @date 2015年10月26日 上午9:58:39
	 * 
	 * @return String
	 */
	@Remark("车品超市频道页")
	@RequestMapping(value = "/supermarket/list/jsp", method = RequestMethod.GET)
	public String toProductSupermarket() {
		return "product/productSupermarket";
	}

	/**
	 * 查询所有商品分类
	 * 
	 * @author guoyn
	 * @date 2015年10月26日 上午9:58:55
	 * 
	 * @param request
	 * @return Result<List<GoodsCat>>
	 */
	@Remark("查询所有商品分类")
	@RequestMapping(value = "/goods/categ/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<GoodsCat>> getGoodsCatListFromDB(HttpServletRequest request) {
		Result<List<GoodsCat>> result = Result.newOne();
		//
		List<GoodsCat> goodsCats = categService.getGoodsCatsByName(null);
		result.data = goodsCats;
		//
		return result;
	}

	/**
	 * 查询某商品分类下相关品牌列表
	 * 
	 * @author guoyn
	 * @date 2015年10月26日 上午9:59:24
	 * 
	 * @param requestData
	 * @return Result<List<BrandDef>>
	 */
	@Remark("查询某商品分类下相关品牌列表")
	@RequestMapping(value = "/brand/list/by/catId/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<BrandDef>> getBrandListFromDB(@RequestBody MapContext requestData) {
		Result<List<BrandDef>> result = Result.newOne();
		PaginatedFilter paginatedFilter = new PaginatedFilter();
		paginatedFilter.setFilterItems(requestData);
		//
		PaginatedList<BrandDef> items = categService.getBrandDefsByCatId(paginatedFilter);
		if(items != null){
			result.data = items.getRows();
		}
		//
		return result;
	}

	/**
	 * 根据过滤条件分页获取车品列表
	 * 
	 * @author guoyn
	 * @date 2015年10月26日 上午10:04:14
	 * 
	 * @param paginatedFilter
	 * @return Result<PaginatedList<Product>>
	 */
	@Remark("根据过滤条件分页获取车品列表")
	@RequestMapping(value = "/list/by/filter/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<PaginatedList<Product>> getProductsFromDB(HttpServletRequest request, @RequestBody PaginatedFilter paginatedFilter) {
		Result<PaginatedList<Product>> result = Result.newOne();
		MapContext filterItems = paginatedFilter.getFilterItems();
		filterItems.put("shelfStatus", 1);
		PaginatedList<Product> paginatedList = goodsService.getProductsByBrandIdAndFilter(paginatedFilter);
		result.data = paginatedList;
		return result;
	}

	/**
	 * 添加到购物车
	 * 
	 * @author guoyn
	 * @date 2015年10月26日 下午7:19:58
	 * 
	 * @param requestData
	 * @return
	 */
	@Remark("添加到购物车")
	@RequestMapping(value = "/add/cart/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> getAddToCart(HttpSession session, @RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		// 获取参数值
		Long productId = requestData.getTypedValue("productId", Long.class);
		Integer quantity = requestData.getTypedValue("quantity", Integer.class);
		//
		SaleCartGoodsPo goodsPo = new SaleCartGoodsPo();
		goodsPo.action = CartAction.add;
		//goodsPo.svcId = -1;
		goodsPo.productId = productId;
		goodsPo.quantity = quantity;
		//
		SaleCart saleCartDto = CartHelper.syncSvcCartGoods(session, goodsPo);
		if (saleCartDto == null) {
			result.type = Type.warn;
		}
		return result;
	}

	/**
	 * 获取产品购买统计数据
	 * 
	 * @author guoyn
	 * @date 2015年11月4日 上午10:51:02
	 * 
	 * @param requestData
	 * @return Result<List<ProductDto>>
	 */
	@Remark("获取产品购买统计数据")
	@RequestMapping(value = "/products/buyNum/summary/get", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Result<List<ProductDto>> getBuyProductNum(@RequestBody MapContext requestData) {
		Result<List<ProductDto>> result = Result.newOne();
		// 获取参数值
		List<? extends Number> dataList = (List<? extends Number>) requestData.get("productIds");
		if (dataList != null) {
			List<Long> productIds = TypeUtil.toLongList(dataList);
			List<ProductDto> productDtos = statisService.getProductsBuySummary(productIds);
			result.data = productDtos;
		}
		return result;
	}

	/**
	 * 获取产品的销售价格和市场价格
	 * 
	 * @author guoyn
	 * @date 2015年11月4日 上午10:51:23
	 * 
	 * @param requestData
	 * @return Result<List<ProductDto>>
	 */
	@Remark("获取产品的销售价格和市场价格")
	@RequestMapping(value = "/products/prices/get", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Result<List<ProductDto>> getProductPrices(@RequestBody MapContext requestData) {
		Result<List<ProductDto>> result = Result.newOne();
		// 获取参数值
		List<? extends Number> dataList = (List<? extends Number>) requestData.get("productIds");
		if (dataList != null) {
			List<Long> productIds = TypeUtil.toLongList(dataList);
			List<ProductDto> productDtos = goodsService.getProductSaleAndMarketPrices(productIds);
			result.data = productDtos;
		}
		return result;
	}

	@Remark("产品详细页面")
	@RequestMapping(value = "/detail/jsp", method = RequestMethod.GET)
	public String toProductDetailPage() {
		return "product/productDetailPage";
	}

	/**
	 * 获取产品详细信息
	 * 
	 * @author guoyn
	 * @date 2015年11月4日 上午10:51:38
	 * 
	 * @param requestData
	 * @return Result<ProductDto>
	 */
	@Remark("获取产品详细信息")
	@RequestMapping(value = "/info/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<ProductDto> getProductDetailById(@RequestBody MapContext requestData) {
		Result<ProductDto> result = Result.newOne();
		// 获取参数值
		Long productId = requestData.getTypedValue("productId", Long.class);
		ProductDto productDto = goodsService.getProductDtoByProductId(productId);
		result.data = productDto;
		return result;
	}
	
	@Remark("获取指定规格下的产品详细信息")
	@RequestMapping(value = "/productId/by/specCodes/and/specItemIds/get", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Result<Long> getProductDetailBySpecCodeAndItemIds(@RequestBody MapContext requestData) {
		Result<Long> result = Result.newOne();
		// 获取参数值
		Integer goodsId = requestData.getTypedValue("goodsId", Integer.class);
		Map<String, Integer> specMap = requestData.getTypedValue("specMap", Types.StringIntegerMap.getClass());
		String specStr = SxUtil.toSpecificStr(specMap);
		//
		//ProductDto productDto = goodsService.getProductDtoBySpecStrAndGoodsId(specStr, goodsId);
		Long productId = goodsService.getProductIdBySpecStrAndGoodsId(specStr, goodsId);
		result.data = productId;
		return result;
	}
	
	@Remark("产品下架页面")
	@RequestMapping(value = "/unshelve/jsp", method = RequestMethod.GET)
	public String toProductUnshelvePage() {
		return "product/productUnShelve";
	}
}