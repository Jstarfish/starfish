package priv.starfish.mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.base.Converter;
import priv.starfish.common.model.*;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.*;
import priv.starfish.mall.categ.entity.*;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.misc.SysParamInfo;
import priv.starfish.mall.goods.entity.*;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.service.CategService;
import priv.starfish.mall.service.GoodsService;
import priv.starfish.mall.service.SalePrmtService;
import priv.starfish.mall.service.ShopService;
import priv.starfish.mall.shop.entity.ShopProduct;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

@Remark("商品、属性规格、价格、库存相关处理")
@Controller
@RequestMapping(value = "/goods")
public class GoodsController extends BaseController {

	@Resource
	private CategService categService;

	@Resource
	private GoodsService goodsService;
	
	@Resource
	private ShopService shopService;
	
	@Resource
	private SalePrmtService salePrmtService;

	// -店铺后台-商品添加--

	/**
	 * 店铺后台-商品添加页面
	 * 
	 * @author guoyn
	 * @date 2015年6月11日 上午10:01:43
	 * 
	 * @return String
	 */
	@Remark("添加商品页面")
	@RequestMapping(value = "/add/jsp", method = RequestMethod.GET)
	public String toAddGoods() {
		return "goods/addGoods-mall";
	}

	/**
	 * 查询所有商品分类
	 * 
	 * @author guoyn
	 * @date 2015年6月12日 下午4:01:38
	 * 
	 * @param request
	 * @return Result<List<GoodsCat>>
	 */
	@Remark("查询所有商品分类")
	@RequestMapping(value = "/categ/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<GoodsCat>> getGoodsCatList(HttpServletRequest request) {
		Result<List<GoodsCat>> result = Result.newOne();
		//
		List<GoodsCat> goodsCats = categService.getGoodsCatsByName(null);
		result.data = goodsCats;
		//
		return result;
	}

	/**
	 * 查询某个商品分类下的所有属性
	 * 
	 * @author guoyn
	 * @date 2015年6月16日 下午4:25:12
	 * 
	 * @param requestData
	 * @return Result<List<GoodsCatAttr>>
	 */
	@Remark("查询某个商品分类下的所有属性")
	@RequestMapping(value = "/categ/attrs/by/categId/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<GoodsCatAttr>> getAttrsByCategId(@RequestBody MapContext requestData) {
		Result<List<GoodsCatAttr>> result = Result.newOne();
		Integer categId = requestData.getTypedValue("categId", Integer.class);
		//
		List<GoodsCatAttr> attrs = categService.getCatAttrsByCatId(categId);
		//
		result.data = attrs;
		//
		return result;
	}

	@Remark("查询某个商品分类")
	@RequestMapping(value = "/categ/by/categId/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<GoodsCat> getCategByCategId(@RequestBody MapContext requestData) {
		Result<GoodsCat> result = Result.newOne();
		Integer categId = requestData.getTypedValue("categId", Integer.class);
		//
		GoodsCat categ = categService.getGoodsCatById(categId);
		result.data = categ;
		//
		return result;
	}

	@Remark("查询某个商品分类的所有商品规格")
	@RequestMapping(value = "/specs/by/categId/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<GoodsCatSpec>> getSpecsyCategId(@RequestBody MapContext requestData) {
		Result<List<GoodsCatSpec>> result = Result.newOne();
		Integer categId = requestData.getTypedValue("categId", Integer.class);
		//
		List<GoodsCatSpec> specs = categService.getCatSpecsByCatId(categId);
		result.data = specs;
		//
		return result;
	}

	@SuppressWarnings("unchecked")
	Converter<Map<String, Object>, Product> productConverter = new Converter<Map<String, Object>, Product>() {
		@Override
		public Product convert(Map<String, Object> src, int index) {
			Product product = new Product();
			MapContext mapContext = MapContext.from(src);
			product.setId("".equals(mapContext.get("id")) ? null : mapContext.getTypedValue("id", Long.class));
			product.setGoodsId(mapContext.getTypedValue("goodsId", Integer.class));
			product.setGoodsName(mapContext.getTypedValue("goodsName", String.class));
			product.setCatId(mapContext.getTypedValue("catId", Integer.class));
			product.setQuantity(mapContext.getTypedValue("quantity", Integer.class));
			product.setOrigPrice(new BigDecimal(mapContext.getTypedValue("origPrice", String.class)));
			product.setSalePrice(new BigDecimal(mapContext.getTypedValue("salePrice", String.class)));
			product.setPurchPrice(mapContext.get("purchPrice") == null ? null : mapContext.getTypedValue("purchPrice", BigDecimal.class));
			product.setMarketPrice(mapContext.get("marketPrice") == null ? null : mapContext.getTypedValue("marketPrice", BigDecimal.class));
			product.setWholePrice(mapContext.get("wholePrice") == null ? null : mapContext.getTypedValue("wholePrice", BigDecimal.class));
			product.setWeight(mapContext.get("weight") == null ? null : mapContext.getTypedValue("weight", Double.class));
			product.setShelfStatus(mapContext.getTypedValue("shelfStatus", Integer.class));
			product.setPackList(mapContext.get("packList") == null ? null : mapContext.getTypedValue("packList", String.class));
			product.setGiftFlag(mapContext.get("giftFlag") == null ? null : mapContext.getTypedValue("giftFlag", Boolean.class));
			//
			List<Map<String, Object>> dataList = mapContext.getTypedValue("specVals", TypeUtil.Types.StringObjectMapList.getClass());
			List<ProductSpecVal> specValList = new ArrayList<ProductSpecVal>(0);
			for (Map<String, Object> data : dataList) {
				ProductSpecVal specVal = new ProductSpecVal();
				specVal.setSpecId(Integer.valueOf(data.get("specId").toString()));
				specVal.setGoodsId(Integer.valueOf(data.get("goodsId").toString()));
				specVal.setSpecVal(data.get("specVal").toString());
				specVal.setSpecItemId(Integer.valueOf(data.get("specItemId").toString()));
				specVal.setColorFlag(Boolean.valueOf(data.get("colorFlag").toString()));
				specVal.setRefCode(data.get("refCode").toString());
				specVal.setId("".equals(data.get("id")) ? null : Long.valueOf(data.get("id").toString()));
				specVal.setProductId("".equals(data.get("productId")) ? null : Long.valueOf(data.get("productId").toString()));
				specValList.add(specVal);
			}
			product.setSpecVals(specValList);
			//
			List<Map<String, Object>> _dataList = mapContext.getTypedValue("productAlbumImgs", TypeUtil.Types.StringObjectMapList.getClass());
			List<ProductAlbumImg> pAlbumImgsList = new ArrayList<ProductAlbumImg>(0);
			for (Map<String, Object> data : _dataList) {
				ProductAlbumImg img = new ProductAlbumImg();
				img.setId("".equals(data.get("id")) ? null : Long.valueOf(data.get("id").toString()));
				img.setImageId("".equals(data.get("imageId")) ? null : Long.valueOf(data.get("imageId").toString()));
				img.setFileBrowseUrl(data.get("fileBrowseUrl").toString());
				img.setSeqNo(data.get("seqNo") == null ? 1 : Integer.valueOf(data.get("seqNo").toString()));
				pAlbumImgsList.add(img);
			}
			product.setProductAlbumImgs(pAlbumImgsList);
			//
			return product;
		}
	};

	Converter<Map<String, Object>, GoodsAttrVal> attrValConverter = new Converter<Map<String, Object>, GoodsAttrVal>() {
		@Override
		public GoodsAttrVal convert(Map<String, Object> src, int index) {
			GoodsAttrVal attrVal = new GoodsAttrVal();
			MapContext mapContext = MapContext.from(src);
			attrVal.setId("".equals(mapContext.get("id")) ? null : Long.valueOf(mapContext.getTypedValue("id", String.class)));
			attrVal.setGoodsId(mapContext.getTypedValue("goodsId", Integer.class));
			attrVal.setAttrId(mapContext.getTypedValue("attrId", Integer.class));
			attrVal.setAttrVal(mapContext.getTypedValue("attrVal", String.class));
			attrVal.setKeyFlag(mapContext.getTypedValue("keyFlag", Boolean.class));
			Boolean brandFlag = mapContext.get("brandFlag") == null ? false : mapContext.getTypedValue("brandFlag", Boolean.class);
			attrVal.setBrandFlag(brandFlag);
			attrVal.setRefCode(mapContext.getTypedValue("refCode", String.class));
			attrVal.setAttrItemId("".equals(mapContext.get("attrItemId")) ? null : mapContext.getTypedValue("attrItemId", Integer.class));

			//
			return attrVal;
		}
	};

	/**
	 * 保存或更新商品
	 * 
	 * @author guoyn
	 * @date 2015年7月6日 下午6:44:51
	 * 
	 * @param request
	 * @param requestData
	 * @return Result<Goods>
	 */
	@Remark("保存或更新商品")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/goods/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Goods> saveOrUpdateGoods(HttpServletRequest request, @RequestBody MapContext requestData) {
		UserContext userContext = getUserContext(request);
		//
		Result<Goods> result = Result.newOne();
		//
		Goods goods = Goods.newOne();
		Integer id = requestData.getTypedValue("id", Integer.class);
		goods.setId(id);
		Integer catId = requestData.getTypedValue("catId", Integer.class);
		goods.setCatId(catId);
		Integer shopId = userContext.getScopeEntityId(AuthScope.shop.name());
		goods.setShopId(shopId);
		String catPath = requestData.getTypedValue("catPath", String.class);
		goods.setCatPath(catPath);
		String name = requestData.getTypedValue("name", String.class);
		goods.setName(name);
		String title = requestData.getTypedValue("title", String.class);
		goods.setTitle(title);
		String packList = requestData.get("packList") == null ? null : requestData.getTypedValue("packList", String.class);
		goods.setPackList(packList);
		//
		List<Map<String, Object>> _attrVals = requestData.getTypedValue("attrVals", TypeUtil.Types.StringObjectMapList.getClass());
		List<GoodsAttrVal> attrVals = new ArrayList<GoodsAttrVal>(0);
		attrVals = CollectionUtil.convertToList(_attrVals, attrValConverter);
		goods.setAttrVals(attrVals);
		//
		GoodsIntro goodsIntro = new GoodsIntro();
		goodsIntro = JsonUtil.fromJson(JsonUtil.toFormattedJson(requestData.get("goodsIntro")), GoodsIntro.class);
		goods.setGoodsIntro(goodsIntro);

		//
		List<Map<String, Object>> _products = requestData.getTypedValue("products", TypeUtil.Types.StringObjectMapList.getClass());
		List<Product> products = new ArrayList<Product>(0);
		products = CollectionUtil.convertToList(_products, productConverter);
		//
		goods.setProducts(products);
		//
		boolean status = goodsService.saveOrUpdateGoods(goods);

		result.message = "商品保存成功!";
		if (!status) {
			result.type = Type.warn;
			result.message = "商品保存失败!";
		}
		//
		result.data = goods;
		//
		return result;
	}

	/**
	 * 删除货品及货品规格值
	 * 
	 * @author guoyn
	 * @date 2015年8月7日 上午11:31:24
	 * 
	 * @param requestData
	 * @return Result<String>
	 */
	@Remark("删除货品及货品规格值")
	@RequestMapping(value = "/product/delete/by/id/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> deleteProduct(@RequestBody MapContext requestData) {
		Result<String> result = Result.newOne();
		//
		String idStr = requestData.getTypedValue("productId", String.class);
		boolean status = true;
		result.message = "保存成功!";
		//
		if (!StrUtil.isNullOrBlank(idStr)) {
			Long id = Long.valueOf(idStr);
			status = goodsService.deleteProductRelatedById(id);
		}
		//
		if (!status) {
			result.type = Type.warn;
			result.message = "保存失败!";
		}
		//
		result.data = idStr;
		//
		return result;
	}

	/**
	 * 批量删除货品及货品规格值
	 * 
	 * @author 廖晓远
	 * @date 2015年8月28日 上午11:28:00
	 * 
	 * @param requestData
	 * @return
	 */
	@Remark("删除货品及货品规格值")
	@RequestMapping(value = "/product/delete/by/ids", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Result<?> deleteProducts(@RequestBody MapContext requestData) {
		Result<String> result = Result.newOne();
		//
		List<String> ids = requestData.getTypedValue("productIds", List.class);
		boolean status = true;
		result.message = "删除成功!";
		List<Long> productIds = new ArrayList<Long>(0);
		//
		if (ids.size() > 0) {
			for (String s : ids) {
				Long id = Long.valueOf(s);
				productIds.add(id);
			}
			status = goodsService.deleteProductByIds(productIds);
		}

		if (!status) {
			result.type = Type.warn;
			result.message = "删除失败!";
		}
		//
		return result;
	}

	Converter<Map<String, Object>, GoodsAlbumImg> albumImgsConverter = new Converter<Map<String, Object>, GoodsAlbumImg>() {
		@Override
		public GoodsAlbumImg convert(Map<String, Object> src, int index) {
			GoodsAlbumImg albumImg = new GoodsAlbumImg();
			MapContext mapContext = MapContext.from(src);
			// albumImg.setShopId(shopId);
			albumImg.setGoodsId(mapContext.getTypedValue("goodsId", Integer.class));
			albumImg.setImageUuid(mapContext.getTypedValue("imageUuid", String.class));
			albumImg.setImageUsage(mapContext.getTypedValue("imageUsage", String.class));
			albumImg.setImagePath(mapContext.getTypedValue("imagePath", String.class));
			return albumImg;
		}
	};

	/**
	 * 批量保存商品相册图片
	 * 
	 * @author guoyn
	 * @date 2015年8月7日 上午11:31:42
	 * 
	 * @param request
	 * @param requestData
	 * @return Result<List<GoodsAlbumImg>>
	 */
	@Remark("批量保存商品相册图片")
	@RequestMapping(value = "/albumImgs/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<GoodsAlbumImg>> saveAlbumImgs(HttpServletRequest request, @RequestBody List<Map<String, Object>> requestData) {
		Result<List<GoodsAlbumImg>> result = Result.newOne();
		//
		List<GoodsAlbumImg> albumImgs = CollectionUtil.convertToList(requestData, albumImgsConverter);
		boolean status = true;
		result.message = "商品相册保存成功!";
		status = goodsService.batchSaveAlbumImgs(albumImgs);
		result.data = albumImgs;
		//
		if (!status) {
			result.type = Type.warn;
			result.message = "商品相册保存失败!";
		}
		//
		return result;
	}

	/**
	 * 删除商品相册图片
	 * 
	 * @author guoyn
	 * @date 2015年8月7日 上午11:32:42
	 * 
	 * @param requestData
	 * @return Result<String>
	 */
	@Remark("删除商品相册图片")
	@RequestMapping(value = "/albumImg/delete/by/uuid/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> deleteAlbumImg(@RequestBody MapContext requestData) {
		Result<String> result = Result.newOne();
		//
		String uuid = requestData.getTypedValue("uuid", String.class);
		boolean status = goodsService.deleteGoodsAlbumImgByUuid(uuid);
		result.message = "保存成功!";
		if (!status) {
			result.type = Type.warn;
			result.message = "保存失败!";
		}
		//
		result.data = uuid;
		//
		return result;
	}

	/**
	 * 删除产品相册图片
	 * 
	 * @author guoyn
	 * @date 2015年8月7日 上午11:34:09
	 * 
	 * @param requestData
	 * @return Result<String>
	 */
	@Remark("删除产品相册图片")
	@RequestMapping(value = "/product/albumImg/delete/by/id/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> deleteProductAlbumImg(@RequestBody MapContext requestData) {
		Result<String> result = Result.newOne();
		//
		Long id = requestData.getTypedValue("id", Long.class);
		boolean status = goodsService.deleteProductAlbumImgById(id);
		result.message = "保存成功!";
		if (!status) {
			result.type = Type.warn;
			result.message = "保存失败!";
		}
		//
		result.data = id.toString();
		//
		return result;
	}

	/**
	 * 保存商品基本信息及商品属性值
	 * 
	 * @author guoyn
	 * @date 2015年8月7日 上午11:34:27
	 * 
	 * @param request
	 * @param goods
	 * @return Result<Goods>
	 */
	@Remark("保存商品基本信息及商品属性值")
	@RequestMapping(value = "/basisInfo/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Goods> saveGoodsBasisInfo(HttpServletRequest request, @RequestBody Goods goods) {
		Result<Goods> result = Result.newOne();
		Boolean goodsDefindedOnlyByMall = SysParamInfo.goodsDefindedOnlyByMall;
		Integer shopId = -1;
		//商品不是由平台定义
		if(!goodsDefindedOnlyByMall){
			UserContext userContext = getUserContext(request);
			shopId = userContext.getScopeEntityId(AuthScope.shop.name());
		}
		//
		boolean status = true;
		result.message = "商品基本信息保存成功!";
		logger.info("---->goodsDefindedOnlyByMall="+goodsDefindedOnlyByMall);
		logger.info("---->shopId="+shopId);
		goods.setShopId(shopId);
		status = goodsService.saveGoodsBasisInfo(goods);
		result.data = goods;
		//
		if (!status) {
			result.type = Type.warn;
			result.message = "商品基本信息保存失败!";
		}
		//
		return result;
	}

	/**
	 * 查询某商品的商品介绍图片
	 * 
	 * @author guoyn
	 * @date 2015年8月7日 上午11:34:51
	 * 
	 * @param requestData
	 * @return Result<List<GoodsIntroImg>>
	 */
	@Remark("查询某商品的商品介绍图片")
	@RequestMapping(value = "/intro/imgs/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<GoodsIntroImg>> getIntroImgByGoodsId(@RequestBody MapContext requestData) {
		Result<List<GoodsIntroImg>> result = Result.newOne();
		Integer goodsId = requestData.getTypedValue("goodsId", Integer.class);
		//
		List<GoodsIntroImg> introImgs = goodsService.getIntroImgsByGoodsId(goodsId);
		result.data = introImgs;
		//
		return result;
	}

	Converter<Map<String, Object>, GoodsIntroImg> introImgConverter = new Converter<Map<String, Object>, GoodsIntroImg>() {
		@Override
		public GoodsIntroImg convert(Map<String, Object> src, int index) {
			GoodsIntroImg introImg = new GoodsIntroImg();
			MapContext mapContext = MapContext.from(src);
			introImg.setGoodsId(mapContext.getTypedValue("goodsId", Integer.class));
			introImg.setImageUuid(mapContext.getTypedValue("imageUuid", String.class));
			introImg.setImageUsage(mapContext.getTypedValue("imageUsage", String.class));
			introImg.setImagePath(mapContext.getTypedValue("imagePath", String.class));
			return introImg;
		}
	};

	/**
	 * 批量保存商品介绍图片
	 * 
	 * @author guoyn
	 * @date 2015年8月7日 上午11:35:11
	 * 
	 * @param request
	 * @param requestData
	 * @return Result<List<GoodsIntroImg>>
	 */
	@Remark("批量保存商品介绍图片")
	@RequestMapping(value = "/intro/imgs/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<GoodsIntroImg>> saveIntroImgs(HttpServletRequest request, @RequestBody List<Map<String, Object>> requestData) {
		//
		List<GoodsIntroImg> introImgs = CollectionUtil.convertToList(requestData, introImgConverter);
		Result<List<GoodsIntroImg>> result = Result.newOne();
		//
		boolean status = true;
		result.message = "商品介绍图片保存成功!";
		status = goodsService.batchSaveIntroImgs(introImgs);
		result.data = introImgs;
		//
		if (!status) {
			result.type = Type.warn;
			result.message = "商品介绍图片保存失败!";
		}
		//
		return result;
	}

	/**
	 * 删除商品介绍图片
	 * 
	 * @author guoyn
	 * @date 2015年8月7日 上午11:35:25
	 * 
	 * @param request
	 * @param requestData
	 * @return Result<Object>
	 */
	@Remark("删除商品介绍图片")
	@RequestMapping(value = "/intro/img/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> deleteIntroImg(HttpServletRequest request, @RequestBody MapContext requestData) {
		String uuid = requestData.getTypedValue("fileUuid", String.class);
		//
		Result<Object> result = Result.newOne();
		//
		boolean status = goodsService.deleteIntroImgByUuid(uuid);
		result.message = "商品介绍图片删除成功!";
		result.data = status;
		//
		if (!status) {
			result.type = Type.warn;
			result.message = "商品介绍图片删除失败!";
		}
		//
		return result;
	}

	/**
	 * 查询所有商品相册图片
	 * 
	 * @author guoyn
	 * @date 2015年8月7日 上午11:35:41
	 * 
	 * @param requestData
	 * @param request
	 * @return Result<List<GoodsAlbumImg>>
	 */
	@Remark("查询所有商品相册图片")
	@RequestMapping(value = "/albumImg/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<GoodsAlbumImg>> getAlbumImgList(@RequestBody MapContext requestData, HttpServletRequest request) {
		Result<List<GoodsAlbumImg>> result = Result.newOne();

		Integer goodsId = requestData.getTypedValue("goodsId", Integer.class);
		List<GoodsAlbumImg> albumImgs = new ArrayList<GoodsAlbumImg>(0);
		//
		albumImgs = goodsService.getAlbumImgsByGoodsId(goodsId);
		//
		result.data = albumImgs;
		return result;
	}

	/**
	 * 查询某个商品介绍
	 * 
	 * @author guoyn
	 * @date 2015年8月7日 上午11:35:55
	 * 
	 * @param requestData
	 * @return Result<GoodsIntro>
	 */
	@Remark("查询某个商品介绍")
	@RequestMapping(value = "/intro/by/goodsId/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<GoodsIntro> getGoodsIntroByGoodsId(@RequestBody MapContext requestData) {
		Result<GoodsIntro> result = Result.newOne();
		Integer goodsId = requestData.getTypedValue("goodsId", Integer.class);
		//
		GoodsIntro intro = goodsService.getGoodsIntroByGoodsId(goodsId);
		result.data = intro;
		//
		return result;
	}

	// -店铺后台-商品修改--

	/**
	 * 跳转至商品修改页面
	 * 
	 * @author guoyn
	 * @date 2015年6月24日 上午10:33:32
	 * 
	 * @return String
	 */
	@Remark("商品修改页面")
	@RequestMapping(value = "/edit/jsp", method = RequestMethod.GET)
	public String toEditGoods(@RequestParam("goodsId") Integer goodsId) {
		return "goods/editGoods";
	}

	/**
	 * 查询某个商品的基本信息
	 * 
	 * @author guoyn
	 * @date 2015年8月7日 上午11:36:22
	 * 
	 * @param requestData
	 * @param response
	 * @return Result<Goods>
	 */
	@Remark("查询某个商品的基本信息")
	@RequestMapping(value = "/basisInfo/by/id/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Goods> getGoodsById(@RequestBody MapContext requestData, HttpServletResponse response) {
		Result<Goods> result = Result.newOne();
		Integer goodsId = requestData.getTypedValue("goodsId", Integer.class);
		//
		Goods goods = goodsService.getGoodsBasisInfoById(goodsId);
		result.data = goods;
		//
		return result;
	}

	/**
	 * 查询某个商品下的所有货品
	 * 
	 * @author guoyn
	 * @date 2015年8月7日 上午11:36:44
	 * 
	 * @param requestData
	 * @return Result<List<Product>>
	 */
	@Remark("查询某个商品下的所有货品")
	@RequestMapping(value = "/products/shelve/by/goodsId/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<Product>> getProductsByGoodsId(@RequestBody MapContext requestData) {
		Result<List<Product>> result = Result.newOne();
		Integer goodsId = requestData.getTypedValue("goodsId", Integer.class);
		//
		List<Product> products = goodsService.getUnShelveProductsByGoodsId(goodsId);
		result.data = products;
		//
		return result;
	}

	/**
	 * 查询商品相册图片下的货品相册图片
	 * 
	 * @author guoyn
	 * @date 2015年8月7日 上午11:37:02
	 * 
	 * @param requestData
	 * @return Result<List<Long>>
	 */
	@Remark("查询商品相册图片下的货品相册图片")
	@RequestMapping(value = "/product/albumImgIds/by/uuid/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<Long>> getProductAlbumImgByUuid(@RequestBody MapContext requestData) {
		Result<List<Long>> result = Result.newOne();
		String uuid = requestData.getTypedValue("uuid", String.class);
		//
		List<Long> imgIds = goodsService.getProductAlbumImgIdsByUuid(uuid);
		result.data = imgIds;
		//
		return result;
	}

	Converter<Map<String, Object>, Long> productImgIdsConverter = new Converter<Map<String, Object>, Long>() {
		@Override
		public Long convert(Map<String, Object> src, int index) {
			MapContext mapContext = MapContext.from(src);
			Long id = "".equals(mapContext.get("id")) ? null : Long.valueOf(mapContext.get("id").toString());
			return id;
		}
	};

	/**
	 * 批量删除货品相册图片
	 * 
	 * @author guoyn
	 * @date 2015年8月7日 上午11:37:17
	 * 
	 * @param requestData
	 * @return Result<List<Long>>
	 */
	@Remark("批量删除货品相册图片 ")
	@RequestMapping(value = "/product/albumImgs/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<Long>> deleteProductAlbumImgs(@RequestBody List<Map<String, Object>> requestData) {
		Result<List<Long>> result = Result.newOne();
		List<Long> imgIds = CollectionUtil.convertToList(requestData, productImgIdsConverter);
		//
		boolean status = goodsService.deleteProductAlbumImgByIds(imgIds);

		result.data = imgIds;
		//
		return result;
	}

	/**
	 * 批量更新货品及货品规格值
	 * 
	 * @author guoyn
	 * @date 2015年8月7日 上午11:37:56
	 * 
	 * @param request
	 * @param requestData
	 * @return Result<List<Product>>
	 */
	@Remark("批量更新货品及货品规格值")
	@RequestMapping(value = "/products/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<Product>> updateProducts(HttpServletRequest request, @RequestBody List<Map<String, Object>> requestData) {
		//
		List<Product> products = CollectionUtil.convertToList(requestData, productConverter);
		Result<List<Product>> result = Result.newOne();
		//
		boolean status = goodsService.batchUpdateProduct(products);
		result.message = "具体商品保存成功!";
		result.data = products;
		//
		if (!status) {
			result.type = Type.warn;
			result.message = "具体商品保存失败!";
			return result;
		}
		//
		return result;
	}

	// -店铺后台-产品列表--

	/**
	 * 返回产品列表页面
	 * 
	 * @author 廖晓远
	 * @date 2015年6月26日 上午10:53:27
	 * 
	 * @return
	 */
	@Remark("商城产品列表页面")
	@RequestMapping(value = "/list/jsp/-mall", method = RequestMethod.GET)
	public String toGoodsMgmt() {
		return "goods/productList-mall";
	}

	/**
	 * 通过上下文分页查询店铺所有货品
	 * 
	 * @author 廖晓远
	 * @date 2015年6月26日 上午11:52:13
	 * 
	 * @param request
	 * @return
	 */
	@Remark("通过上下文分页查询店铺所有货品")
	@RequestMapping(value = "/product/list/by/context", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<Product> getProductsByContext(HttpServletRequest request) {
		UserContext userContext = getUserContext(request);
		//
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		Map<String, Object> prarms = new HashMap<String, Object>();
		MapContext map = paginatedFilter.getFilterItems();
		if (map != null) {
			prarms.putAll(map);
		}
		Integer shopId = userContext.getScopeEntityId(AuthScope.shop.name());
		if (shopId != null) {
			prarms.put("shopId", shopId);
		} else {
			prarms.put("shopId", -1);
		}
		paginatedFilter.setFilterItems(prarms);
		PaginatedList<Product> paginatedList = goodsService.getProductsByContext(paginatedFilter);
		JqGridPage<Product> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 通过分类Id分页查询所有货品
	 * 
	 * @author 廖晓远
	 * @date 2015年7月16日 下午2:49:48
	 * 
	 * @param request
	 * @return
	 */
	@Remark("通过分类Id分页查询所有货品")
	@RequestMapping(value = "/product/list/by/catId", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<Product> getProductsByCatId(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		PaginatedList<Product> paginatedList = goodsService.getProductsByBrandIdAndFilter(paginatedFilter);
		JqGridPage<Product> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 批量下架产品
	 * 
	 * @author zjl
	 * @date 2015年7月1日 下午5:59:29
	 * 
	 * @param request
	 * @param ids
	 * @return
	 */
	@Remark("批量下架产品")
	@RequestMapping(value = "/products/batch/downShelf/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> batchDownShelfProducts(HttpServletRequest request, @RequestBody List<String> ids) {
		Result<?> result = Result.newOne();
		List<Long> _ids = TypeUtil.toLongListX(ids);
		Integer shelfStatus = 2;// 下架状态
		Date date = new Date();// 最新时间
		result.message = goodsService.batchDownShelfProducts(_ids, shelfStatus, date) ? "操作成功！" : "操作失败！";
		return result;
	}

	/**
	 * 批量上架产品
	 * 
	 * @author zjl
	 * @date 2015年7月1日 下午5:59:29
	 * 
	 * @param request
	 * @param ids
	 * @return
	 */
	@Remark("批量上架产品")
	@RequestMapping(value = "/products/batch/upShelf/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> batchUpShelfProducts(HttpServletRequest request, @RequestBody List<String> ids) {
		Result<?> result = Result.newOne();
		List<Long> _ids = TypeUtil.toLongListX(ids);
		Integer shelfStatus = 1;// 上架状态
		Date date = new Date();// 最新时间
		result.message = goodsService.batchUpShelfProducts(_ids, shelfStatus, date) ? "操作成功！" : "操作失败！";
		return result;
	}

	/**
	 * 货品详情界面
	 * 
	 * @author 廖晓远
	 * @date 2015年8月14日 下午6:23:25
	 * 
	 * @return
	 */
	@Remark("货品详情界面")
	@RequestMapping(value = "/product/detail/jsp", method = RequestMethod.GET)
	public String toProductDetails() {
		return "goods/productDetail";
	}

	@Remark("获取货品信息")
	@RequestMapping(value = "/product/get/by/id", method = RequestMethod.POST)
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
	 * 获取货品规格
	 * 
	 * @author 廖晓远
	 * @date 2015年8月14日 下午6:25:48
	 * 
	 * @param goods
	 * @return
	 */
	@Remark("获取货品规格")
	@RequestMapping(value = "/product/goodsAttrVal/by/catId", method = RequestMethod.POST)
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
	
	/**
	 * 返回店铺的产品列表页面
	 * 
	 * @author guoyn
	 * @date 2015年10月17日 上午11:20:01
	 * 
	 * @return String
	 */
	@Remark("店铺产品列表页面")
	@RequestMapping(value = "/list/jsp/-shop", method = RequestMethod.GET)
	public String toShopGoodsMgmt() {
		return "goods/productList-shop";
	}
	
	/**
	 * 查询店铺货品
	 * 
	 * @author guoyn
	 * @date 2015年10月17日 下午3:57:28
	 * 
	 * @param request
	 * @return JqGridPage<Product>
	 */
	@Remark("查询店铺货品列表")
	@RequestMapping(value = "/product/list/get/-shop", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<ShopProduct> getShopProducts(HttpServletRequest request) {
		UserContext userContext = getUserContext(request);
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		Map<String, Object> prarms = paginatedFilter.getFilterItems();
		prarms.remove("shopId");
		Integer shopId = userContext.getScopeEntityId(AuthScope.shop.name());
		if (shopId != null) {
			prarms.put("shopId", shopId);
		} else {
			prarms.put("shopId", -1);
		}
		PaginatedList<ShopProduct> paginatedList = shopService.getShopProductsByFilter(paginatedFilter);
		JqGridPage<ShopProduct> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}
	
	/**
	 * 
	 * 根据id获取店铺中的产品
	 * @author guoyn
	 * @date 2015年10月17日 下午6:15:29
	 * 
	 * @param request
	 * @param requestData
	 * @return
	 */
	@Remark("根据id获取店铺中的产品")
	@RequestMapping(value = "/product/get/by/id/-shop", method = RequestMethod.POST)
	@ResponseBody
	public Result<Product> getShopProduct(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<Product> result = Result.newOne();
		//
		Long productId = requestData.getTypedValue("productId", Long.class);
		
		Product product = goodsService.getProductById(productId);
		//
		result.data = product;
		//
		return result;
	} 
	
	/**
	 * 更新产品的缺货状态
	 * 
	 * @author guoyn
	 * @date 2015年10月19日 下午6:57:41
	 * 
	 * @param requestData
	 * @return Result<?>
	 */
	@Remark("更新产品的缺货状态")
	@RequestMapping(value = "/product/update/lackFlag/do/-shop", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> updateShopProductLackFlag(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		String msg = "更新成功！";
		//
		Long id = requestData.getTypedValue("id", Long.class);
		Boolean lackFlag = requestData.getTypedValue("lackFlag", Boolean.class);
		Boolean ok = shopService.updateShopProductLackFlagById(id, lackFlag);
		if(!ok){
			msg = "更新失败！";
			result.type = Type.warn;
		}
		result.message = msg;
		//
		return result;
	} 
	
	/**
	 * 店铺添加产品页面
	 * 
	 * @author guoyn
	 * @date 2015年10月19日 下午6:57:50
	 * 
	 * @return String
	 */
	@Remark("店铺添加产品页面")
	@RequestMapping(value = "/product/add/jsp/-shop", method = RequestMethod.GET)
	public String toAddShopProducts() {
		return "goods/addGoods-shop";
	}
	
	/**
	 * 查询商城货品列表
	 * 
	 * @author guoyn
	 * @date 2015年10月20日 下午4:31:04
	 * 
	 * @param request
	 * @return JqGridPage<Product>
	 */
	@Remark("查询商城货品列表")
	@RequestMapping(value = "/product/list/get/-mall", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<Product> getMallProducts(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<Product> paginatedList = goodsService.getProductsByBrandIdAndFilter(paginatedFilter);
		//标识已存在店铺的商品
		List<Product> products = paginatedList.getRows();
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntityId(AuthScope.shop.name());
		for(Product product:products){
			MapContext filters = new MapContext();
			filters.put("shopId", shopId);
			filters.put("productId", product.getId());
			boolean existFlag = shopService.existShopProductByFilter(filters);
			product.setExistFlag(existFlag);
		}
		JqGridPage<Product> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}
	
	/**
	 * 添加店铺商品
	 * 
	 * @author guoyn
	 * @date 2015年10月20日 下午4:57:26
	 * 
	 * @param request
	 * @param requestData
	 * @return
	 */
	@Remark("添加店铺商品")
	@RequestMapping(value = "/product/add/do/-shop", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> addProductToShop(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntityId(AuthScope.shop.name());
		Long productId = requestData.getTypedValue("productId", Long.class);
		//
		boolean status = shopService.saveProduct(shopId, productId);
		result.message = "操作成功!";
		//
		if (!status) {
			result.type = Type.warn;
			result.message = "操作失败!";
			return result;
		}
		//
		return result;
	}
	
	@Remark("删除店铺商品")
	@RequestMapping(value = "/shopProduct/delete/do/-shop", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteShopProduct(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		Long id = requestData.getTypedValue("id", Long.class);
		//
		boolean status = shopService.deleteProductByShopProductId(id);
		result.message = "操作成功!";
		//
		if (!status) {
			result.type = Type.warn;
			result.message = "操作失败!";
			return result;
		}
		//
		return result;
	}
	
	@Remark("删除店铺商品")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/shopProduct/delete/by/Ids/-shop", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteShopProducts(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		boolean status = true;
		List<? extends Number> dataList = (List<? extends Number>) requestData.get("ids");
		if(dataList != null){
			List<Long> ids = TypeUtil.toLongList(dataList);
			//
			status = shopService.deleteShopProductsByIds(ids);
			result.message = "操作成功!";
		}
		//
		if (!status) {
			result.type = Type.warn;
			result.message = "操作失败!";
			return result;
		}
		//
		return result;
	}
	
	@Remark("上下架产品")
	@RequestMapping(value = "/product/shelve/by/id/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> shelveProduct(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		Long productId = requestData.getTypedValue("productId", Long.class);
		Integer shelfStatus = requestData.getTypedValue("shelfStatus", Integer.class);// 上架状态
		boolean ok = goodsService.updateProductShelfStatus(productId, shelfStatus); 
		result.message = ok ? "操作成功！" : "操作失败！";
		return result;
	}
	
	@Remark("根据id获取店铺中的产品")
	@RequestMapping(value = "/product/color/specVals/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<GoodsCatSpecItem>> getColorSpecValsByGoodsId(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<List<GoodsCatSpecItem>> result = Result.newOne();
		//
		Integer goodsId = requestData.getTypedValue("goodsId", Integer.class);
		
		List<GoodsCatSpecItem> specItems = goodsService.getColorSpecItemsByGoodsId(goodsId);
		//
		result.data = specItems;
		//
		return result;
	}
	
	@Remark("保存商品相册颜色分组图片")
	@RequestMapping(value = "/album/color/imgs/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> saveGoodsAlbumColorImgs(@RequestBody GoodsColorImgList goodsColorImgs) {
		Result<?> result = Result.newOne();
		boolean ok = goodsService.saveGoodsColorImgs(goodsColorImgs);
		//
		if(!ok){
			result.type = Type.warn;
		}
		return result;
	}
	
	@Remark("获取商品相册颜色分组图片")
	@RequestMapping(value = "/color/imgs/by/goodsId/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<GoodsColorImg>> getGoodsColorImgs(@RequestBody MapContext requestData) {
		Result<List<GoodsColorImg>> result = Result.newOne();
		Integer goodsId = requestData.getTypedValue("goodsId", Integer.class);
		//
		List<GoodsColorImg> goodsColorImgs = goodsService.getGoodsColorImgsByGoodsId(goodsId);
		result.data = goodsColorImgs;
		//
		return result;
	}
	
	@Remark("获取商品相册颜色分组图片")
	@RequestMapping(value = "/album/color/imgs/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> deleteGoodsColorImgs(@RequestBody MapContext requestData) {
		Result<Boolean> result = Result.newOne();
		Integer goodsId = requestData.getTypedValue("goodsId", Integer.class);
		//
		boolean ok = goodsService.deleteGoodsColorImgs(goodsId);
		result.data = ok;
		//
		return result;
	}
	
	@Remark("分页获取商品列表")
	@RequestMapping(value = "/goods/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<Goods> getGoodsList(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		PaginatedList<Goods> paginatedList = goodsService.getGoodsByFilter(paginatedFilter);
		JqGridPage<Goods> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}
	
	@Remark("判断货品是否被店铺使用中")
	@RequestMapping(value = "/product/validate/inUse/by/productId/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> validateInUse(@RequestBody MapContext requestData) {
		Result<Integer> result = Result.newOne();
		Long productId = requestData.getTypedValue("productId", Long.class);
		int count = shopService.getShopCountByProductId(productId);
		result.data = count;
		//
		return result;
	}
	
	@Remark("查询某个商品名称是否被占用")
	@RequestMapping(value = "/name/exist/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> checkGoodsName(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<Boolean> result = Result.newOne();
		String goodsName = requestData.getTypedValue("goodsName", String.class);
		Integer categId = requestData.getTypedValue("catId", Integer.class);
		Integer vendorId = requestData.getTypedValue("vendorId", Integer.class);
		//
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntityId("shop");
		if(shopId == null) shopId = -1;
		//
		boolean existName = goodsService.existGoodName(goodsName, categId, vendorId, shopId);
		result.data = existName;
		//
		return result;
	}
	
	//------------------------------------促销标签----------------------------------------------------------------
	
	@Remark("保存货品促销标签")
	@RequestMapping(value = "/product/prmtTag/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> savePrmtTag(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		//
		Long productId = requestData.getTypedValue("productId", Long.class);
		Integer prmtTagId = requestData.getTypedValue("prmtTagId", Integer.class);
		//
		result.message = "保存成功";
		boolean ok = salePrmtService.savePrmtTagProduct(prmtTagId, productId);
		//
		if(!ok){
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}
	
	@Remark("删除货品促销标签")
	@RequestMapping(value = "/product/prmtTag/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deletePrmtTag(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		//
		Long productId = requestData.getTypedValue("productId", Long.class);
		Integer tagId = requestData.getTypedValue("tagId", Integer.class);
		//
		result.message = "删除成功";
		boolean ok = salePrmtService.deletePrmtTagProduct(tagId, productId);
		//
		if(!ok){
			result.type = Type.warn;
			result.message = "删除失败";
		}
		return result;
	}
}