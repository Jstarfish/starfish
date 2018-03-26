package priv.starfish.mall.manager.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.base.Converter;
import priv.starfish.common.model.*;
import priv.starfish.common.util.*;
import priv.starfish.mall.categ.dto.GoodsCatMenuItemDto;
import priv.starfish.mall.categ.entity.*;
import priv.starfish.mall.comn.dict.WeightUnit;
import priv.starfish.mall.comn.misc.SysParamInfo;
import priv.starfish.mall.interact.entity.GoodsInquiry;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.service.CategService;
import priv.starfish.mall.service.GoodsService;
import priv.starfish.mall.service.InteractService;
import priv.starfish.mall.service.ShopService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Remark("商品分类")
@Controller
@RequestMapping(value = "/categ")
public class CategController extends BaseController {

	@Resource
	private CategService categService;

	@Resource
	private InteractService interactService;

	@Resource
	private ShopService shopService;

	@Resource
	private GoodsService goodsService;

	// -------------------------------规格参照-----------------------------------
	/**
	 * 跳转规格参照页面
	 * 
	 * @author 王少辉
	 * @date 2015年5月27日 下午6:20:14
	 * 
	 * @return 返回规格参照页面
	 */
	@Remark("规格参照页面")
	@RequestMapping(value = "/specRef/list/jsp", method = RequestMethod.GET)
	public String toSpecRefListPage() {
		return "categ/specRefList";
	}

	/**
	 * 查询规格参照
	 * 
	 * @author 王少辉
	 * @date 2015年5月27日 下午6:41:42
	 * 
	 *            请求
	 * @return 返回规格参照
	 */
	@Remark("查询规格参照")
	@RequestMapping(value = "/spec/ref/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SpecRef> getSpecRef(@RequestBody SpecRef specRef) {
		Result<SpecRef> result = Result.newOne();
		try {
			result.data = categService.getSpecRefById(specRef.getId());
		} catch (Exception e) {
			result.type = Result.Type.warn;
			result.message = "数据查询错误";
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 分页查询规格参照
	 * 
	 * @author 王少辉
	 * @date 2015年5月27日 下午6:41:42
	 * 
	 * @param request
	 *            请求
	 * @return 返回规格参照列表
	 */
	@Remark("分页查询规格参照")
	@RequestMapping(value = "/spec/ref/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SpecRef> listSpecRef(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<SpecRef> paginatedList = categService.getSpecRefs(paginatedFilter);
		//
		JqGridPage<SpecRef> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 保存规格参照
	 * 
	 * @author 王少辉
	 * @date 2015年5月28日 下午1:33:11
	 * 
	 * @param specRef
	 *            规格参照
	 * @return 返回规格参照id
	 */
	@Remark("保存规格参照")
	@RequestMapping(value = "/spec/ref/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> saveSpecRef(@RequestBody SpecRef specRef) {
		Result<Integer> result = Result.newOne();
		boolean ok = false;

		if (specRef == null) {
			result.message = "保存失败";
			return result;
		}

		// 保存规格参照
		ok = categService.saveSpecRef(specRef);
		result.data = specRef.getId();
		result.message = ok ? "保存成功" : "保存失败";

		return result;
	}

	/**
	 * 修改规格参照
	 * 
	 * @author 王少辉
	 * @date 2015年5月28日 下午8:33:45
	 * 
	 * @param specRef
	 *            规格参照
	 * @return 返回规格参照id
	 */
	@Remark("修改规格参照")
	@RequestMapping(value = "/spec/ref/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateSpecRef(@RequestBody SpecRef specRef) {
		Result<Integer> result = Result.newOne();
		boolean ok = false;

		if (specRef == null) {
			result.message = "保存失败";
			return result;
		}

		// 修改规格参照
		ok = categService.updateSpecRef(specRef);
		result.message = ok ? "保存成功!" : "保存失败!";

		return result;
	}

	/**
	 * 删除规格参照
	 * 
	 * @author 郝江奎
	 * @date 2015年8月31日 下午11:34:32
	 * 
	 *            规格参照
	 * @return
	 */
	@Remark("删除规格参照")
	@RequestMapping(value = "/spec/ref/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteSpecRef(@RequestParam("id") Integer id) {
		Result<?> result = Result.newOne();
		// 删除规格参照
		result.message = categService.deleteSpecRefById(id) ? "删除成功!" : "删除失败!";

		return result;
	}

	/**
	 * 批量删除规格参照
	 * 
	 * @author 郝江奎
	 * @date 2015年8月31日 下午11:34:32
	 * 
	 *            规格参照
	 * @return
	 */
	@Remark("批量删除规格参照")
	@RequestMapping(value = "/specRef/delete/by/ids", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteSpecRefs(@RequestBody List<Integer> ids) {
		Result<?> result = Result.newOne();
		// 删除规格参照
		result.message = categService.deleteSpecRefsByIds(ids) ? "删除成功!" : "删除失败!";

		return result;
	}

	// --------------------------------------- 商品分类 ----------------------------------------------

	@Remark("商品分类管理页面")
	@RequestMapping(value = "/goodsCat/tree/jsp", method = RequestMethod.GET)
	public String toGoodsCatJsp() {
		return "categ/goodsCat";
	}

	/**
	 * 
	 * 获取系统参数中商品分类级数
	 * 
	 * @author guoyn
	 * @date 2015年10月14日 上午11:16:25
	 * 
	 * @param request
	 * @return Result<Integer>
	 */
	@Remark("获取系统参数中商品分类级数")
	@RequestMapping(value = "/sysParam/categLevels/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> getCategLevels(HttpServletRequest request) {
		Result<Integer> result = Result.newOne();
		Integer levels = SysParamInfo.goodsCategLevels;
		result.data = levels;
		return result;
	}

	@Remark("查询所有商品分类")
	@RequestMapping(value = "/goodsCat/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<GoodsCat>> getGoodsCatList(@RequestBody GoodsCat goodsCat) {
		Result<List<GoodsCat>> result = Result.newOne();
		if (goodsCat != null) {
			List<GoodsCat> goodsCats = categService.getGoodsCatsByName(goodsCat.getName());
			// 查询父节点
			if (goodsCat.getName() != null && goodsCat.getName() != "") {
				if (goodsCats != null && goodsCats.size() > 0) {
					Map<Integer, GoodsCat> map = new HashMap<Integer, GoodsCat>();
					for (GoodsCat goodsCat2 : goodsCats) {
						map.put(goodsCat2.getId(), goodsCat2);
						getGoodsCat(map, goodsCat2.getParentId());
					}
					List<GoodsCat> cats = new ArrayList<GoodsCat>();
					for (Map.Entry<Integer, GoodsCat> entry : map.entrySet()) {
						GoodsCat cat = entry.getValue();
						cats.add(cat);
					}
					if (cats != null && cats.size() > 0) {
						goodsCats = cats;
					}
					// 排序
					Collections.sort(goodsCats, new Comparator<GoodsCat>() {
						@Override
						public int compare(GoodsCat o1, GoodsCat o2) {
							return o1.getLevel().compareTo(o2.getLevel());
						}
					});
				}
			}
			result.data = goodsCats;
		} else {
			result.message = "数据异常";
			result.type = Result.Type.warn;
		}
		return result;
	}

	private void getGoodsCat(Map<Integer, GoodsCat> map, Integer parentId) {
		GoodsCat cat = categService.getGoodsCatById(parentId);
		if (cat != null) {
			map.put(cat.getId(), cat);
			getGoodsCat(map, cat.getParentId());
			// GoodsCat cat1 = categService.getGoodsCatById(cat.getParentId());
		}
	}

	/**
	 * 通过级别查询商品分类
	 * 
	 * @author 廖晓远
	 * @date 2015年7月15日 下午3:44:37
	 * 
	 * @param goodsCat
	 * @return
	 */
	@Remark("通过级别查询商品分类")
	@RequestMapping(value = "/goodsCat/list/get/by/level", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<GoodsCat>> getGoodsCatListByLevel(@RequestBody GoodsCat goodsCat) {
		Result<List<GoodsCat>> result = Result.newOne();
		if (goodsCat != null) {
			List<GoodsCat> goodsCats = categService.getGoodsCatsByLevel(goodsCat.getLevel());
			result.data = goodsCats;
		} else {
			result.message = "数据异常";
			result.type = Result.Type.warn;
		}
		return result;
	}

	@Remark("通过父节点查询所以子级商品分类")
	@RequestMapping(value = "/goodsCat/list/get/by/parentId", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<GoodsCat>> getGoodsCatListByParentId(@RequestBody GoodsCat goodsCat) {
		Result<List<GoodsCat>> result = Result.newOne();
		if (goodsCat != null) {
			List<GoodsCat> goodsCats = categService.getAllGoodsCatsByParentId(goodsCat.getParentId());
			result.data = goodsCats;
		} else {
			result.message = "数据异常";
			result.type = Result.Type.warn;
		}
		return result;
	}

	Converter<Map<String, Object>, GoodsCat> goodsCatConverter = new Converter<Map<String, Object>, GoodsCat>() {
		@Override
		public GoodsCat convert(Map<String, Object> src, int index) {
			GoodsCat goodsCat = new GoodsCat();
			MapContext mapContext = MapContext.from(src);
			goodsCat.setName(mapContext.getTypedValue("name", String.class));
			goodsCat.setLevel(mapContext.getTypedValue("level", Integer.class));
			goodsCat.setParentId(mapContext.getTypedValue("parentId", Integer.class));
			goodsCat.setIdPath(mapContext.getTypedValue("idPath", String.class));
			String unit = mapContext.getTypedValue("unit", String.class);
			if (unit != null)
				goodsCat.setUnit(unit);
			String weightUnit = mapContext.getTypedValue("weightUnit", String.class);
			if (weightUnit != null)
				goodsCat.setWeightUnit(WeightUnit.valueOf(weightUnit));
			goodsCat.setHasSpec(mapContext.getTypedValue("hasSpec", Boolean.class));
			goodsCat.setTs(new Date());
			return goodsCat;
		}
	};

	/**
	 * 新增商品分类
	 * 
	 * @author 廖晓远
	 * @date 2015-5-27 下午7:34:02
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Remark("增加商品分类")
	@RequestMapping(value = "/goodsCat/add/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<Integer> addGoodsCat(@RequestBody MapContext requestData) {
		Result<Integer> result = Result.newOne();
		List<Map<String, Object>> _goodsCat = requestData.getTypedValue("goodsCat", TypeUtil.Types.StringObjectMapList.getClass());
		List<GoodsCat> goodsCats = CollectionUtil.convertToList(_goodsCat, goodsCatConverter);
		GoodsCat goodsCat = goodsCats.get(0);
		goodsCat.setLeafFlag(false);
		// 分组
		String groupStr = requestData.getTypedValue("groups", String.class);
		Map<String, Object> groupMaps = JsonUtil.fromJson(groupStr, TypeUtil.TypeRefs.StringObjectMapType);
		String groupDataStr = requestData.getTypedValue("groupData", String.class);
		Map<String, Object> groupDatas = JsonUtil.fromJson(JsonUtil.formatAsMap(groupDataStr), TypeUtil.TypeRefs.StringObjectMapType);
		// 属性
		String goodsCatAttrStr = requestData.getTypedValue("goodsCatAttrs", String.class);
		List<Map<String, Object>> goodsCatAttrMaps = JsonUtil.fromJson(JsonUtil.formatAsList(goodsCatAttrStr), TypeUtil.TypeRefs.StringObjectMapListType);
		String attrsEnumValuesStr = requestData.getTypedValue("attrsEnumValues", String.class);
		Map<String, Object> attrsEnumValues = JsonUtil.fromJson(JsonUtil.formatAsMap(attrsEnumValuesStr), TypeUtil.TypeRefs.StringObjectMapType);
		// 规格
		String goodsCatSpecStr = requestData.getTypedValue("goodsCatSpecs", String.class);
		List<Map<String, Object>> goodsCatSpecMaps = JsonUtil.fromJson(JsonUtil.formatAsList(goodsCatSpecStr), TypeUtil.TypeRefs.StringObjectMapListType);
		String specsEnumValuesStr = requestData.getTypedValue("specsEnumValues", String.class);
		Map<String, Object> specsEnumValues = JsonUtil.fromJson(JsonUtil.formatAsMap(specsEnumValuesStr), TypeUtil.TypeRefs.StringObjectMapType);
		if (goodsCat != null) {
			// 获取商品分类分组
			List<GoodsCatAttrGroup> goodsCatAttrGroups = new ArrayList<GoodsCatAttrGroup>(0);
			if (groupMaps != null) {
				Iterator<String> it = groupMaps.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					GoodsCatAttrGroup goodsCatAttrGroup = JsonUtil.fromJson(JsonUtil.toJson(groupMaps.get(key)), GoodsCatAttrGroup.class);
					goodsCatAttrGroups.add(goodsCatAttrGroup);
				}
			}
			goodsCat.setCatAttrGroups(goodsCatAttrGroups);
			goodsCat.setGroupDatas(groupDatas);

			// 获取商品分类属性
			List<GoodsCatAttr> goodsCatAttrs = new ArrayList<GoodsCatAttr>(0);
			if (goodsCatAttrMaps != null) {
				for (Map<String, Object> map : goodsCatAttrMaps) {
					GoodsCatAttr goodsCatAttr = JsonUtil.fromJson(JsonUtil.toJson(map), GoodsCatAttr.class);
					Map<String, Object> attrItemMap = (Map<String, Object>) attrsEnumValues.get(goodsCatAttr.getRefId() + "");
					if (attrItemMap != null) {
						// 获取属性枚举值
						List<GoodsCatAttrItem> goodsCatAttrItems = new ArrayList<GoodsCatAttrItem>(0);
						for (String value : attrItemMap.keySet()) {
							GoodsCatAttrItem goodsCatAttrItem = JsonUtil.fromJson(JsonUtil.toJson(attrItemMap.get(value)), GoodsCatAttrItem.class);
							goodsCatAttrItem.setSeqNo(1);
							goodsCatAttrItems.add(goodsCatAttrItem);
						}
						goodsCatAttr.setGoodsCatAttrItems(goodsCatAttrItems);
					}
					goodsCatAttrs.add(goodsCatAttr);
				}
			}

			goodsCat.setCatAttrs(goodsCatAttrs);

			// 获取商品分类规格
			List<GoodsCatSpec> goodsCatSpecs = new ArrayList<GoodsCatSpec>(0);

			if (goodsCatSpecMaps != null) {
				for (Map<String, Object> map : goodsCatSpecMaps) {
					GoodsCatSpec goodsCatSpec = JsonUtil.fromJson(JsonUtil.toJson(map), GoodsCatSpec.class);
					Map<String, Object> specItemMap = (Map<String, Object>) specsEnumValues.get(goodsCatSpec.getRefId() + "");
					if (specItemMap != null) {
						// 获取规格枚举值
						List<GoodsCatSpecItem> goodsCatSpecItems = new ArrayList<GoodsCatSpecItem>(0);
						for (String value : specItemMap.keySet()) {
							GoodsCatSpecItem goodsCatSpecItem = JsonUtil.fromJson(JsonUtil.toJson(specItemMap.get(value)), GoodsCatSpecItem.class);
							goodsCatSpecItem.setSeqNo(1);
							goodsCatSpecItem.setValue(value);
							goodsCatSpecItems.add(goodsCatSpecItem);
						}
						goodsCatSpec.setGoodsCatSpecItems(goodsCatSpecItems);
					}
					goodsCatSpecs.add(goodsCatSpec);
				}
			}

			goodsCat.setCatSpecs(goodsCatSpecs);

			if (categService.saveGoodsCat(goodsCat)) {
				result.data = goodsCat.getId();
				result.message = "添加成功!";
			} else {
				result.message = "添加失败";
				result.type = Result.Type.error;
			}
		}
		return result;
	}

	/**
	 * 修改商品分类
	 * 
	 * @author 廖晓远
	 * @date 2015-5-29 上午11:43:46
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Remark("修改商品分类")
	@RequestMapping(value = "/goodsCat/update/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<?> updateGoodsCat(@RequestBody MapContext requestData) {
		Result<Integer> result = Result.newOne();
		GoodsCat goodsCat = JsonUtil.fromJson(JsonUtil.toJson(requestData.get("goodsCat")), GoodsCat.class);
		// 分组
		String groupStr = requestData.getTypedValue("groups", String.class);
		Map<String, Object> groupMaps = JsonUtil.fromJson(groupStr, TypeUtil.TypeRefs.StringObjectMapType);
		String groupDataStr = requestData.getTypedValue("groupData", String.class);
		Map<String, Object> groupDatas = JsonUtil.fromJson(JsonUtil.formatAsMap(groupDataStr), TypeUtil.TypeRefs.StringObjectMapType);
		// 属性
		String goodsCatAttrStr = requestData.getTypedValue("goodsCatAttrs", String.class);
		List<Map<String, Object>> goodsCatAttrMaps = JsonUtil.fromJson(JsonUtil.formatAsList(goodsCatAttrStr), TypeUtil.TypeRefs.StringObjectMapListType);
		String attrsEnumValuesStr = requestData.getTypedValue("attrsEnumValues", String.class);
		Map<String, Object> attrsEnumValues = JsonUtil.fromJson(JsonUtil.formatAsMap(attrsEnumValuesStr), TypeUtil.TypeRefs.StringObjectMapType);
		// 规格
		String goodsCatSpecStr = requestData.getTypedValue("goodsCatSpecs", String.class);
		List<Map<String, Object>> goodsCatSpecMaps = JsonUtil.fromJson(JsonUtil.formatAsList(goodsCatSpecStr), TypeUtil.TypeRefs.StringObjectMapListType);
		String specsEnumValuesStr = requestData.getTypedValue("specsEnumValues", String.class);
		Map<String, Object> specsEnumValues = JsonUtil.fromJson(JsonUtil.formatAsMap(specsEnumValuesStr), TypeUtil.TypeRefs.StringObjectMapType);

		if (goodsCat != null) {
			Integer catId = goodsCat.getId();
			GoodsCat oldGoodsCat = categService.getGoodsCatById(catId);
			if (oldGoodsCat != null) {
				boolean hasSpec = goodsCat.getHasSpec();
				oldGoodsCat.setUnit(goodsCat.getUnit());
				oldGoodsCat.setMaxPrice(goodsCat.getMaxPrice());
				oldGoodsCat.setMinPrice(goodsCat.getMinPrice());
				oldGoodsCat.setWeightUnit(goodsCat.getWeightUnit());
				oldGoodsCat.setName(goodsCat.getName());
				oldGoodsCat.setHasSpec(hasSpec);
				// 设置价格区间
				oldGoodsCat.setGoodsCatPriceRanges(goodsCat.getGoodsCatPriceRanges());
			} else {
				result.message = "数据异常";
				result.type = Result.Type.error;
			}

			// 获取商品分类属性分组
			List<GoodsCatAttrGroup> goodsCatAttrGroups = new ArrayList<GoodsCatAttrGroup>(0);
			if (groupMaps != null) {
				Iterator<String> it = groupMaps.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					GoodsCatAttrGroup goodsCatAttrGroup = JsonUtil.fromJson(JsonUtil.toJson(groupMaps.get(key)), GoodsCatAttrGroup.class);
					goodsCatAttrGroup.setCatId(catId);
					goodsCatAttrGroups.add(goodsCatAttrGroup);
				}
			}
			oldGoodsCat.setCatAttrGroups(goodsCatAttrGroups);
			oldGoodsCat.setGroupDatas(groupDatas);

			// 获取商品分类属性
			List<GoodsCatAttr> goodsCatAttrs = new ArrayList<GoodsCatAttr>(0);
			if (goodsCatAttrMaps != null) {
				for (Map<String, Object> map : goodsCatAttrMaps) {
					GoodsCatAttr goodsCatAttr = JsonUtil.fromJson(JsonUtil.toJson(map), GoodsCatAttr.class);
					Integer attrId = goodsCatAttr.getId();
					goodsCatAttr.setCatId(catId);
					Map<String, Object> attrItemMap = (Map<String, Object>) attrsEnumValues.get(goodsCatAttr.getRefId() + "");
					if (attrItemMap != null) {
						// 属性枚举值
						List<GoodsCatAttrItem> goodsCatAttrItems = new ArrayList<GoodsCatAttrItem>(0);
						for (String value : attrItemMap.keySet()) {
							GoodsCatAttrItem goodsCatAttrItem = JsonUtil.fromJson(JsonUtil.toJson(attrItemMap.get(value)), GoodsCatAttrItem.class);
							goodsCatAttrItem.setSeqNo(1);
							goodsCatAttrItem.setValue(value);
							if (attrId != null && attrId > 0) {
								goodsCatAttrItem.setAttrId(attrId);
							}
							goodsCatAttrItems.add(goodsCatAttrItem);
						}
						goodsCatAttr.setGoodsCatAttrItems(goodsCatAttrItems);
					}
					goodsCatAttrs.add(goodsCatAttr);
				}
			}
			oldGoodsCat.setCatAttrs(goodsCatAttrs);

			// 获取商品分类规格
			List<GoodsCatSpec> goodsCatSpecs = new ArrayList<GoodsCatSpec>(0);
			if (goodsCatSpecMaps != null) {
				for (Map<String, Object> map : goodsCatSpecMaps) {
					GoodsCatSpec goodsCatSpec = JsonUtil.fromJson(JsonUtil.toJson(map), GoodsCatSpec.class);
					Integer specId = goodsCatSpec.getId();
					goodsCatSpec.setCatId(catId);
					Map<String, Object> specItemMap = (Map<String, Object>) specsEnumValues.get(goodsCatSpec.getRefId() + "");
					if (specItemMap != null) {
						// 规格枚举值
						List<GoodsCatSpecItem> goodsCatSpecItems = new ArrayList<GoodsCatSpecItem>(0);
						for (String value : specItemMap.keySet()) {
							GoodsCatSpecItem goodsCatSpecItem = JsonUtil.fromJson(JsonUtil.toJson(specItemMap.get(value)), GoodsCatSpecItem.class);
							goodsCatSpecItem.setSeqNo(1);
							goodsCatSpecItem.setValue(value);
							if (specId != null && specId > 0) {
								goodsCatSpecItem.setSpecId(specId);
							}
							goodsCatSpecItems.add(goodsCatSpecItem);
						}
						goodsCatSpec.setGoodsCatSpecItems(goodsCatSpecItems);
					}
					goodsCatSpecs.add(goodsCatSpec);
				}
			}
			oldGoodsCat.setCatSpecs(goodsCatSpecs);

			Boolean flag = categService.updateGoodsCat(oldGoodsCat);
			if (flag == true) {
				result.message = "修改成功!";
			} else {
				result.message = "修改失败";
				result.type = Result.Type.error;
			}
		} else {
			result.message = "数据异常";
			result.type = Result.Type.warn;
		}
		return result;
	}

	/**
	 * 删除商品分类
	 * 
	 * @author 廖晓远
	 * @date 2015-5-29 上午11:43:56
	 * 
	 * @return
	 */
	@Remark("删除商品分类")
	@RequestMapping(value = "/goodsCat/delete/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<?> deleteGoodsCat(@RequestBody List<Integer> catIds) {
		Result<?> result = Result.newOne();
		if (catIds != null && catIds.size() > 0) {
			for (Integer catId : catIds) {
				result = checkDelGoodsCat(catId);
				if (result.type == Result.Type.warn) {
					return result;
				}
			}
			boolean flag = categService.deleteGoodsCatByIds(catIds);
			if (flag == true) {
				result.message = "删除成功!";
			} else {
				result.message = "删除失败";
				result.type = Result.Type.error;
			}
		} else {
			result.message = "数据异常";
			result.type = Result.Type.warn;
		}
		return result;
	}

	/**
	 * 删除分类前检查关联
	 * 
	 * @author 廖晓远
	 * @date 2015-5-29 上午11:52:34
	 * 
	 * @return
	 */
	public Result<?> checkDelGoodsCat(Integer catId) {
		Result<?> result = Result.newOne();
		if (catId != null && catId > 0) {
			GoodsCat goodsCat = categService.getGoodsCatById(catId);
			if (goodsCat != null) {
				// 判断商品数量
				int goodsNum = goodsService.countGoodsByCatId(catId);
				if (goodsNum > 0) {
					result.message = "商品分类： " + goodsCat.getName() + " 含有" + goodsNum + "个商品正在使用，请处理后再删除";
					result.type = Result.Type.warn;
					return result;
				}
				// 判断店铺数量
				int shopNum = shopService.getShopCountByCatId(catId);
				if (shopNum > 0) {
					result.message = "商品分类： " + goodsCat.getName() + " 含有" + shopNum + "个店铺正在使用，请处理后再删除";
					result.type = Result.Type.warn;
					return result;
				}
				return result;
			}
		}
		result.message = "数据异常";
		result.type = Result.Type.warn;
		return result;
	}

	// --------------------------------------- 属性参照 ----------------------------------------------

	/**
	 * 属性参照页面
	 * 
	 * @author 毛智东
	 * @date 2015年5月28日 上午10:36:13
	 * 
	 * @return
	 */
	@Remark("属性参照页面")
	@RequestMapping(value = "/attrRef/list/jsp", method = RequestMethod.GET)
	public String toAttrRefListPage() {
		return "categ/attrRefList";
	}

	/**
	 * 分页查询属性参照
	 * 
	 * @author 毛智东
	 * @date 2015年5月28日 上午10:39:50
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询属性参照")
	@RequestMapping(value = "/attrRef/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<AttrRef> getAttrRefs(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<AttrRef> paginatedList = categService.getAttrRefsByFilter(paginatedFilter);
		//
		JqGridPage<AttrRef> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 属性参照名称是否占用
	 * 
	 * @author 毛智东
	 * @date 2015年5月28日 下午4:51:18
	 * 
	 * @return
	 */
	@Remark("属性参照名称是否占用")
	@RequestMapping(value = "/attrRef/name/isabel/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getAttrRefByName(@RequestBody AttrRef attrRef) {
		Result<Boolean> result = Result.newOne();
		result.data = categService.getAttrRefByName(attrRef.getName()) == null;
		return result;
	}

	/**
	 * 添加属性参照
	 * 
	 * @author 毛智东
	 * @date 2015年5月28日 下午5:58:48
	 * 
	 * @param attrRef
	 * @return
	 */
	@Remark("添加属性参照")
	@RequestMapping(value = "/attrRef/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<AttrRef> addAttrRef(@RequestBody AttrRef attrRef) {
		Result<AttrRef> result = Result.newOne();
		String code = StrUtil.chsToPinyin(attrRef.getName());
		attrRef.setCode(code);
		result.message = categService.saveAttrRef(attrRef) ? "保存成功！" : "保存失败！";
		result.data = attrRef;
		return result;
	}

	/**
	 * 修改属性参照
	 * 
	 * @author 毛智东
	 * @date 2015年5月28日 下午7:22:42
	 * 
	 * @param attrRef
	 * @return
	 */
	@Remark("修改属性参照")
	@RequestMapping(value = "/attrRef/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<AttrRef> updateAttrRef(@RequestBody AttrRef attrRef) {
		Result<AttrRef> result = Result.newOne();
		String code = StrUtil.chsToPinyin(attrRef.getName());
		attrRef.setCode(code);
		result.message = categService.updateAttrRef(attrRef) ? "保存成功！" : "保存失败！";
		result.data = attrRef;
		return result;
	}

	/**
	 * 删除属性参照
	 * 
	 * @author 郝江奎
	 * @date 2015年5月28日 下午8:48:18
	 * 
	 * @param id
	 * @return
	 */
	@Remark("删除属性参照")
	@RequestMapping(value = "/attrRef/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteAttrRef(@RequestParam("id") Integer id) {
		Result<?> result = Result.newOne();
		result.message = categService.deleteAttrRefById(id) ? "删除成功！" : "删除失败！";
		return result;
	}

	/**
	 * 批量删除属性参照
	 * 
	 * @author 毛智东
	 * @date 2015年5月28日 下午8:50:48
	 * 
	 * @param ids
	 * @return
	 */
	@Remark("批量删除属性参照")
	@RequestMapping(value = "/attrRef/delete/batch/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteAttrRefBatch(@RequestBody List<Integer> ids) {
		Result<?> result = Result.newOne();
		result.message = categService.deleteAttrRefByIds(ids) ? "删除成功！" : "删除失败！";
		return result;
	}

	/**
	 * 是否存在品牌属性
	 * 
	 * @author 毛智东
	 * @date 2015年6月24日 下午4:26:08
	 * 
	 * @return
	 */
	@Remark("是否存在品牌属性")
	@RequestMapping(value = "/attrRef/brand/is/true", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getAttrRefByBrandFlagIsTrue() {
		Result<Boolean> result = Result.newOne();
		result.data = categService.getAttrRefByBrandFlagIsTrue();
		return result;
	}

	// --------------------------------------- 商品分类-属性参照 ----------------------------------------------
	/**
	 * 通过分类Id查询商品分类属性关系
	 * 
	 * @author 廖晓远
	 * @date 2015-5-30 下午5:02:44
	 * 
	 * @param request
	 * @return
	 */
	@Remark("通过分类Id查询商品分类属性关系")
	@RequestMapping(value = "/goodsCatAttr/list/get/by/catId", method = RequestMethod.POST)
	@ResponseBody
	public List<GoodsCatAttr> getGoodsCatAttrs(HttpServletRequest request) {
		List<GoodsCatAttr> goodsCatAttrs = new ArrayList<GoodsCatAttr>(0);
		String filterStr = request.getParameter("filterStr");
		if (filterStr != null) {
			GoodsCatAttr goodsCatAttr = JsonUtil.fromJson(filterStr, GoodsCatAttr.class);
			if (goodsCatAttr != null) {
				goodsCatAttrs = categService.getCatAttrsByCatId(goodsCatAttr.getCatId());
			}
		}
		return goodsCatAttrs;
	}

	/**
	 * 批量删除商品分类属性关联
	 * 
	 * @author 廖晓远
	 * @date 2015-5-30 下午5:13:27
	 * 
	 * @param ids
	 * @return
	 */
	@Remark("批量删除商品分类属性关联")
	@RequestMapping(value = "/goodsCatAttr/delete/batch/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> batchDelGoodsCatAttr(@RequestBody List<Integer> ids) {
		Result<?> result = Result.newOne();
		boolean flag = categService.deleteGoodsCatAttrByIds(ids);
		if (flag == true) {
			result.message = "删除成功!";
		} else {
			result.message = "删除失败";
			result.type = Result.Type.error;
		}
		return result;
	}

	// --------------------------------------- 商品分类-规格参照 ----------------------------------------------

	/**
	 * 通过分类Id查询商品分类规格关系
	 * 
	 * @author 廖晓远
	 * @date 2015-6-3 下午3:52:58
	 * 
	 * @param request
	 * @return
	 */
	@Remark("通过分类Id查询商品分类规格关系")
	@RequestMapping(value = "/goodsCatSpec/list/get/by/catId", method = RequestMethod.POST)
	@ResponseBody
	public List<GoodsCatSpec> getGoodsCatSpecs(HttpServletRequest request) {
		List<GoodsCatSpec> goodsCatSpecs = new ArrayList<GoodsCatSpec>(0);
		String filterStr = request.getParameter("filterStr");
		if (filterStr != null) {
			GoodsCatSpec goodsCatSpec = JsonUtil.fromJson(filterStr, GoodsCatSpec.class);
			if (goodsCatSpec != null) {
				goodsCatSpecs = categService.getCatSpecsByCatId(goodsCatSpec.getCatId());
			}
		}
		return goodsCatSpecs;
	}

	/**
	 * 批量删除商品分类规格关联
	 * 
	 * @author 廖晓远
	 * @date 2015-6-3 下午3:57:10
	 * 
	 * @param ids
	 * @return
	 */
	@Remark("批量删除商品分类规格关联")
	@RequestMapping(value = "/goodsCatSpec/delete/batch/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> batchDelGoodsCatSpec(@RequestBody List<Integer> ids) {
		Result<?> result = Result.newOne();
		boolean flag = categService.deleteCatSpecByIds(ids);
		if (flag == true) {
			result.message = "删除成功!";
		} else {
			result.message = "删除失败";
			result.type = Result.Type.error;
		}
		return result;
	}

	// --------------------------------------- 商品分类-分组 ----------------------------------------------

	@Remark("通过分类Id查询商品分类分组")
	@RequestMapping(value = "/goodsCatGroup/list/get/by/catId", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<GoodsCatAttrGroup>> getGoodsCatAttrGroups(@RequestBody GoodsCatAttrGroup goodsCatAttrGroup) {
		Result<List<GoodsCatAttrGroup>> result = Result.newOne();
		List<GoodsCatAttrGroup> goodsCatAttrGroups = categService.getCatAttrGroupByCatId(goodsCatAttrGroup.getCatId());
		result.data = goodsCatAttrGroups;
		return result;
	}

	// --------------------------------商品分类菜单----------------------------------
	/**
	 * 转向商品分类菜单界面
	 * 
	 * @author zjl
	 * @date 2015年6月18日 下午7:09:40
	 * 
	 * @return
	 */
	@Remark("商品分类菜单界面")
	@RequestMapping(value = "/menu/list/jsp", method = RequestMethod.GET)
	public String toGoodsCatMenus() {
		return "categ/goodsCatMenuList";
	}

	/**
	 * 跳转到1级导航深度的商品分类菜单界面
	 * 
	 * @author 王少辉
	 * @date 2015年6月16日 上午9:53:09
	 * 
	 * @return 返回商品分类菜单页面
	 */
	@Remark("跳转到1级导航深度的商品分类菜单界面")
	@RequestMapping(value = "/menu/tree/jsp", method = RequestMethod.GET)
	public String toNavDepthOneMenuTreePage() {
		return "categ/goodsCatMenuTree";
	}

	/**
	 * 跳转到2级导航深度的商品分类菜单界面
	 * 
	 * @author 王少辉
	 * @date 2015年8月17日 下午1:09:08
	 * 
	 * @return 返回商品分类菜单页面
	 */
	@Remark("跳转到2级导航深度的商品分类菜单界面")
	@RequestMapping(value = "/second/depth/menu/tree/jsp", method = RequestMethod.GET)
	public String toNavDepthTwoMenuTreePage() {
		return "categ/goodsCatMenuTreeTwo";
	}

	/**
	 * 分页查询商品分类菜单
	 * 
	 * @author zjl
	 * @date 2015年5月20日 下午4:54:55
	 * 
	 * @param request
	 * @return JqGridPage<GoodsCatMenu>
	 */
	@Remark("分页查询商品分类菜单")
	@RequestMapping(value = "/menu/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<GoodsCatMenu> listGoodsCatMenus(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<GoodsCatMenu> paginatedList = categService.getGoodsCatMenus(paginatedFilter);
		JqGridPage<GoodsCatMenu> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 添加商品分类菜单
	 * 
	 * @author zjl
	 * @date 2015年6月18日 下午6:23:56
	 * 
	 * @param request
	 * @param goodsCatMenu
	 * @return
	 */
	@Remark("添加商品分类菜单")
	@RequestMapping(value = "/menu/create/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> saveGoodsCatMenu(HttpServletRequest request, @RequestBody GoodsCatMenu goodsCatMenu) {
		Result<?> result = Result.newOne();
		boolean ok = true;
		try {
			ok = categService.saveGoodsCatMenu(goodsCatMenu);
		} catch (Exception e) {
			ok = false;
			result.type = Result.Type.error;
			e.printStackTrace();
		}

		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	/**
	 * 更新商品分类菜单
	 * 
	 * @author zjl
	 * @date 2015年6月18日 下午6:23:56
	 * 
	 * @param request
	 * @param goodsCatMenu
	 * @return
	 */
	@Remark("更新商品分类菜单")
	@RequestMapping(value = "/menu/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<GoodsCatMenu> updateGoodsCatMenu(HttpServletRequest request, @RequestBody GoodsCatMenu goodsCatMenu) {
		Result<GoodsCatMenu> result = Result.newOne();
		boolean ok = true;
		try {
			ok = categService.updateGoodsCatMenu(goodsCatMenu);
		} catch (Exception e) {
			ok = false;
			result.type = Result.Type.error;
			e.printStackTrace();
		}
		result.data = goodsCatMenu;
		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	/**
	 * 删除商品分类菜单
	 * 
	 * @author zjl
	 * @date 2015年6月18日 下午6:23:56
	 * 
	 * @param request
	 * @param goodsCatMenu
	 * @return
	 */
	@Remark("删除商品分类菜单")
	@RequestMapping(value = "/menu/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteGoodsCatMenu(HttpServletRequest request, @RequestBody GoodsCatMenu goodsCatMenu) {
		Result<?> result = Result.newOne();
		boolean ok = true;
		try {
			ok = categService.deleteGoodsCatMenuById(goodsCatMenu.getId());
		} catch (Exception e) {
			ok = false;
			result.type = Result.Type.error;
		}

		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	/**
	 * 根据id获取菜单信息
	 * 
	 * @author 王少辉
	 * @date 2015年6月6日 下午4:51:52
	 * 
	 * @return 返回商品分类菜单
	 */
	@Remark("根据id获取菜单信息")
	@RequestMapping(value = "/menu/get/by/id", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<String, String>> getSecondLevelMenuById(@RequestBody Integer id) {
		Result<Map<String, String>> result = Result.newOne();

		try {
			GoodsCatMenu menu = categService.getGoodsCatMenuById(id);
			GoodsCatMenuItem menuItem = categService.getGoodsCatMenuItemByMenuIdAndLevelAndName(menu.getId(), menu.getNavDepth() + 1, menu.getName());
			GoodsCatMenuItemLink menuItemLink = categService.getGoodsCatMenuItemLinkByMenuItemIdAndName(menuItem.getId(), menu.getName());

			Map<String, String> menuMap = new HashMap<String, String>();
			menuMap.put("name", menu.getName());
			menuMap.put("linkFlag", menuItem.getLinkFlag() ? "true" : "false");
			menuMap.put("linkUrl", menuItemLink.getLinkUrl());

			result.data = menuMap;
		} catch (Exception e) {
			result.type = Result.Type.error;
			result.message = "获取商品分类菜单列表失败";
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 获取商品分类菜单的一级菜单项列表
	 * 
	 * @author 王少辉
	 * @date 2015年6月18日 下午12:09:42
	 * 
	 * @param menuItem
	 *            查询条件
	 * @return 返回一级菜单项列表
	 */
	@Remark("获取商品分类一级菜单列表")
	@RequestMapping(value = "/menu/items/first/level/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<GoodsCatMenuItemDto>> getMenuItemsByMenuIdAndLevel(@RequestBody GoodsCatMenuItem menuItem) {
		Result<List<GoodsCatMenuItemDto>> result = Result.newOne();

		try {
			// 一级菜单项列表使用menuId和level获取
			if (menuItem != null) {
				Integer menuId = menuItem.getMenuId();
				Integer level = menuItem.getLevel();

				if (menuId != null && level != null) {
					List<GoodsCatMenuItem> menuItemsList = categService.getMenuItemsByMenuIdAndLevel(menuId, level);

					if (CollectionUtils.isEmpty(menuItemsList)) {
						return result;
					}

					List<GoodsCatMenuItemDto> linksList = new ArrayList<GoodsCatMenuItemDto>();
					GoodsCatMenuItemDto dto = null;
					for (GoodsCatMenuItem bean : menuItemsList) {
						dto = new GoodsCatMenuItemDto();
						TypeUtil.copyProperties(bean, dto);
						List<GoodsCatMenuItemLink> links = categService.getMenuItemLinksByMenuItemId(bean.getId());
						dto.setLinks(links);
						linksList.add(dto);
					}

					result.data = linksList;
				}
			}
		} catch (Exception e) {
			result.type = Result.Type.error;
			result.message = "获取菜单项列表失败";
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 获取商品分类菜单一级菜单项下关联的二级菜单项列表
	 * 
	 * @author 王少辉
	 * @date 2015年6月18日 下午3:17:18
	 * 
	 * @return
	 */
	@Remark("根据商品分类一级菜单获取二级菜单列表")
	@RequestMapping(value = "/menu/items/second/level/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<GoodsCatMenuItem>> getSecondLevelMenuItemByPId(@RequestBody GoodsCatMenuItem menuItem) {
		Result<List<GoodsCatMenuItem>> result = Result.newOne();

		try {
			result.data = categService.getMenuItemsByPId(menuItem.getParentId());
		} catch (Exception e) {
			result.type = Result.Type.error;
			result.message = "获取二级菜单项列表失败";
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 添加商品分类菜单一级菜单项信息
	 * 
	 * @author 王少辉
	 * @date 2015年6月10日 下午2:00:43
	 * 
	 * @param requestData
	 * @return 返回添加结果
	 */
	@Remark("添加商品分类菜单一级菜单项信息")
	@RequestMapping(value = "/menu/item/first/level/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<GoodsCatMenuItem> addFirstLevelMenuItem(@RequestBody MapContext requestData) {
		Result<GoodsCatMenuItem> result = Result.newOne();

		// 构造参数
		GoodsCatMenuItem menuItem = new GoodsCatMenuItem();
		List<GoodsCatMenuItemLink> menuItemLinkList = new ArrayList<GoodsCatMenuItemLink>(0);

		// 设置一级菜单项
		Integer menuId = requestData.getTypedValue("menuId", Integer.class);
		String menuName = requestData.getTypedValue("firstLevelMenuItemName", String.class);
		menuItem.setMenuId(menuId);
		menuItem.setLevel(1);
		menuItem.setParentId(-1);
		menuItem.setName(menuName);
		menuItem.setLinkFlag(false); // 默认无链接，下面代码继续维护

		// 设置一级菜单项第一个链接属性
		String nameOne = requestData.getTypedValue("name0", String.class);
		String urlOne = requestData.getTypedValue("url0", String.class);
		if (StringUtils.isNotEmpty(nameOne)) {
			GoodsCatMenuItemLink menuItemLinkOne = new GoodsCatMenuItemLink();
			menuItemLinkOne.setName(nameOne);
			if (StringUtils.isNotEmpty(urlOne)) {
				menuItem.setLinkFlag(true);

				menuItemLinkOne.setLinkFlag(true);
				menuItemLinkOne.setLinkUrl(urlOne);
			} else {
				menuItemLinkOne.setLinkFlag(false);
			}
			menuItemLinkList.add(menuItemLinkOne);
		}

		// 设置一级菜单项第二个链接属性
		String nameTwo = requestData.getTypedValue("name1", String.class);
		String urlTwo = requestData.getTypedValue("url1", String.class);
		if (StringUtils.isNotEmpty(nameTwo)) {
			GoodsCatMenuItemLink menuItemLinkTwo = new GoodsCatMenuItemLink();
			menuItemLinkTwo.setName(nameTwo);
			if (StringUtils.isNotEmpty(urlTwo)) {
				menuItem.setLinkFlag(true);

				menuItemLinkTwo.setLinkFlag(true);
				menuItemLinkTwo.setLinkUrl(urlTwo);
			} else {
				menuItemLinkTwo.setLinkFlag(false);
			}
			menuItemLinkList.add(menuItemLinkTwo);
		}

		// 设置一级菜单项第三个链接属性
		String nameThree = requestData.getTypedValue("name2", String.class);
		String urlThree = requestData.getTypedValue("url2", String.class);
		if (StringUtils.isNotEmpty(nameThree)) {
			GoodsCatMenuItemLink menuItemLinkThree = new GoodsCatMenuItemLink();
			menuItemLinkThree.setName(nameThree);
			if (StringUtils.isNotEmpty(urlThree)) {
				menuItem.setLinkFlag(true);

				menuItemLinkThree.setLinkFlag(true);
				menuItemLinkThree.setLinkUrl(urlThree);
			} else {
				menuItemLinkThree.setLinkFlag(false);
			}
			menuItemLinkList.add(menuItemLinkThree);
		}

		// 设置一级菜单项第四个链接属性
		String nameFour = requestData.getTypedValue("name3", String.class);
		String urlFour = requestData.getTypedValue("url3", String.class);
		if (StringUtils.isNotEmpty(nameFour)) {
			GoodsCatMenuItemLink menuItemLinkFour = new GoodsCatMenuItemLink();
			menuItemLinkFour.setName(nameFour);
			if (StringUtils.isNotEmpty(urlFour)) {
				menuItem.setLinkFlag(true);

				menuItemLinkFour.setLinkFlag(true);
				menuItemLinkFour.setLinkUrl(urlFour);
			} else {
				menuItemLinkFour.setLinkFlag(false);
			}
			menuItemLinkList.add(menuItemLinkFour);
		}

		boolean ok = false;
		try {
			ok = categService.saveGoodsMenuItemAndLinks(menuItem, menuItemLinkList);
			result.data = menuItem;
		} catch (Exception e) {
			result.type = Result.Type.error;
			e.printStackTrace();
		}

		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	/**
	 * 添加商品分类菜单二级菜单项信息
	 * 
	 * @author 王少辉
	 * @date 2015年6月12日 上午11:09:12
	 * 
	 * @param requestData
	 * @return
	 */
	@Remark("添加商品分类菜单二级菜单项信息")
	@RequestMapping(value = "/menu/item/second/level/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<GoodsCatMenuItem> addGoodsCatMenuSecondLevel(@RequestBody MapContext requestData) {
		Result<GoodsCatMenuItem> result = Result.newOne();

		// 构造参数
		GoodsCatMenuItem menuItem = new GoodsCatMenuItem();
		List<GoodsCatMenuItemLink> menuItemLinkList = new ArrayList<GoodsCatMenuItemLink>(0);

		Integer menuId = requestData.getTypedValue("menuId", Integer.class);
		Integer pId = requestData.getTypedValue("curFirstLevelMenuItemId", Integer.class);
		String menuName = requestData.getTypedValue("secondLevelMenuItemName", String.class);
		String url = requestData.getTypedValue("secondLevelMenuItemUrl", String.class);
		menuItem.setMenuId(menuId);
		menuItem.setLevel(2);
		menuItem.setParentId(pId);
		menuItem.setName(menuName);
		if (StringUtils.isEmpty(url)) {
			menuItem.setLinkFlag(false);
		} else {
			menuItem.setLinkFlag(true);

			GoodsCatMenuItemLink menuItemLink = new GoodsCatMenuItemLink();
			menuItemLink.setName(menuName);
			menuItemLink.setLinkFlag(true);
			menuItemLink.setLinkUrl(url);
			menuItemLinkList.add(menuItemLink);
		}

		boolean ok = false;
		try {
			ok = categService.saveGoodsMenuItemAndLinks(menuItem, menuItemLinkList);
			result.data = menuItem;
		} catch (Exception e) {
			result.type = Result.Type.error;
			e.printStackTrace();
		}

		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	/**
	 * 根据一级菜单项id关联删除一级菜单项
	 * 
	 * @author 王少辉
	 * @date 2015年6月11日 下午8:32:13
	 * 
	 *            一级菜单项id
	 * @return 返回删除结果
	 */
	@Remark("根据一级菜单项id关联删除一级菜单项")
	@RequestMapping(value = "/first/level/menu/item/del/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteFirstLevelMenuItemById(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		boolean ok = false;

		try {
			Integer id = requestData.getTypedValue("id", Integer.class);
			ok = categService.deleteFirstLevelMenuItemById(id);
		} catch (Exception e) {
			ok = false;
			result.type = Result.Type.error;
			e.printStackTrace();
		}

		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	/**
	 * 根据二级菜单项id关联删除二级菜单项
	 * 
	 * @author 王少辉
	 * @date 2015年6月11日 下午8:32:13
	 * 
	 *            二级菜单项id
	 * @return 返回删除结果
	 */
	@Remark("根据二级菜单项id关联删除二级菜单项")
	@RequestMapping(value = "/second/level/menu/item/del/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteSecondLevelMenuItemById(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		boolean ok = false;

		try {
			Integer id = requestData.getTypedValue("id", Integer.class);
			ok = categService.deleteSecondLevelMenuItemById(id);
		} catch (Exception e) {
			ok = false;
			result.type = Result.Type.error;
			e.printStackTrace();
		}

		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	/**
	 * 根据三级菜单项id关联删除三级菜单项
	 * 
	 * @author 王少辉
	 * @date 2015年6月11日 下午8:32:13
	 * 
	 *            三级菜单项id
	 * @return 返回删除结果
	 */
	@Remark("根据三级菜单项id关联删除三级菜单项")
	@RequestMapping(value = "/third/level/menu/item/del/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteThirdLevelMenuItemById(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		boolean ok = false;

		try {
			Integer id = requestData.getTypedValue("id", Integer.class);
			ok = categService.deleteThirdLevelMenuItemById(id);
		} catch (Exception e) {
			ok = false;
			result.type = Result.Type.error;
			e.printStackTrace();
		}

		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	/**
	 * 通过菜单项Id分页查询菜单项分类关联
	 * 
	 * @author 廖晓远
	 * @date 2015年6月18日 上午11:01:14
	 * 
	 * @param request
	 * @return
	 */
	@Remark("通过菜单项Id分页查询菜单项分类关联")
	@RequestMapping(value = "/menu/item/cat/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<GoodsCatMenuItemCat> getGoodsCatMenuCats(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<GoodsCatMenuItemCat> paginatedList = categService.getGoodsCatMenuItemCats(paginatedFilter);
		//
		JqGridPage<GoodsCatMenuItemCat> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	// 把DB数据模型转化为通用模型
	Converter<Map<String, Object>, GoodsCatMenuItemCat> goodsCatMenuItemLinkConverter = new Converter<Map<String, Object>, GoodsCatMenuItemCat>() {
		@Override
		public GoodsCatMenuItemCat convert(Map<String, Object> src, int index) {
			GoodsCatMenuItemCat cat = new GoodsCatMenuItemCat();
			MapContext mapContext = MapContext.from(src);
			// {"name":"平板电视","id":"","menuItemId":"4","catFlag":"false","catId":"","linkUrl":"http://list.jd.com/list.html?cat=737,794,798","emphFlag":true,"seqNo":1}
			cat.setId(mapContext.getTypedValue("id", Integer.class));
			cat.setMenuItemId(mapContext.getTypedValue("menuItemId", Integer.class));
			Boolean catFlag = mapContext.getTypedValue("catFlag", Boolean.class);
			cat.setCatFlag(catFlag);
			if (catFlag) {
				cat.setCatId(mapContext.getTypedValue("catId", Integer.class));
			}
			cat.setName(mapContext.getTypedValue("name", String.class));
			cat.setLinkUrl(mapContext.getTypedValue("linkUrl", String.class));
			cat.setEmphFlag(mapContext.getTypedValue("emphFlag", Boolean.class));
			cat.setSeqNo(mapContext.getTypedValue("seqNo", Integer.class));

			return cat;
		}
	};

	/**
	 * 批量修改商品分类菜单项链接
	 * 
	 * @author 王少辉
	 * @date 2015年6月20日 下午11:01:14
	 * 
	 * @param requestData
	 * @return
	 */
	@Remark("批量修改商品分类菜单项链接")
	@RequestMapping(value = "/menu/item/cat/batch/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> batchSaveGoodsCatMenuItemLink(@RequestBody List<Map<String, Object>> requestData) {
		Result<?> result = Result.newOne();
		Boolean ok;
		try {
			List<GoodsCatMenuItemCat> catList = CollectionUtil.convertToList(requestData, goodsCatMenuItemLinkConverter);
			ok = categService.batchSaveGoodsCatMenuItemCat(catList);
		} catch (Exception e) {
			ok = false;
			result.type = Result.Type.error;
			e.printStackTrace();
		}

		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	// /**
	// * 通过Id删除菜单项链接
	// *
	// * @author 廖晓远
	// * @date 2015年6月18日 下午3:43:23
	// *
	// * @param id
	// * @return
	// */
	// @Remark("通过Id删除菜单项链接")
	// @RequestMapping(value = "/menu/item/cat/delete/do", method = RequestMethod.POST)
	// @ResponseBody
	// public Result<?> deleteGoodsCatMenuLinkById(@RequestBody MapContext requestData) {
	// Integer id = requestData.getTypedValue("id", Integer.class);
	// Result<?> result = Result.newOne();
	// boolean flag = true;
	// if (id == null) {
	// result.type = Result.Type.warn;
	// result.message = "参数错误";
	// } else {
	// flag = categService.deleteGoodsCatMenuItemCat(id);
	// try {
	// } catch (Exception e) {
	// flag = false;
	// result.type = Result.Type.error;
	// e.printStackTrace();
	// }
	// result.message = flag ? "操作成功" : "操作失败";
	// }
	//
	// return result;
	// }

	// -------------------------------颜色定义-----------------------------------
	/**
	 * 转到颜色定义界面
	 * 
	 * @author zjl
	 * @date 2015年6月23日 下午5:26:14
	 * 
	 * @return
	 */
	@Remark("颜色定义界面")
	@RequestMapping(value = "/colorDef/list/jsp", method = RequestMethod.GET)
	public String toColorDef() {
		return "categ/colorDef";
	}

	/**
	 * 分页查询定义颜色
	 * 
	 * @author zjl
	 * @date 2015年5月20日 下午4:54:55
	 * 
	 * @param request
	 * @return JqGridPage<ShopGrade>
	 */
	@Remark("分页查询定义颜色")
	@RequestMapping(value = "/colorDef/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<ColorDef> listColorDefs(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<ColorDef> paginatedList = categService.getColorDefs(paginatedFilter);
		JqGridPage<ColorDef> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 查询所有颜色
	 * 
	 * @author 廖晓远
	 * @date 2015年6月25日 下午6:41:38
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Remark("查询所有颜色")
	@RequestMapping(value = "/colorDef/list/get/all", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<ColorDef>> getAllColorDefs(@RequestBody MapContext requestData) {
		Result<List<ColorDef>> result = Result.newOne();
		List<String> names = null;
		String name = null;
		if (requestData != null) {
			names = requestData.getTypedValue("uncontainNames", List.class);
			name = requestData.getTypedValue("name", String.class);
		}
		List<ColorDef> colorDefs = categService.getColorDefsByFilter(names, name);
		result.data = colorDefs;
		return result;
	}

	/**
	 * 添加定义颜色
	 * 
	 * @author zjl
	 * @date 2015年6月18日 下午6:23:56
	 * 
	 * @param request
	 * @return
	 */
	@Remark("添加定义颜色")
	@RequestMapping(value = "/colorDef/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<ColorDef> addColorDef(HttpServletRequest request, @RequestBody ColorDef colorDef) {
		Result<ColorDef> result = Result.newOne();
		boolean ok = true;
		try {
			ok = categService.saveColorDef(colorDef);
			result.data = colorDef;

		} catch (Exception e) {
			ok = false;
			result.type = Result.Type.error;
		}

		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	/**
	 * 修改定义颜色
	 * 
	 * @author zjl
	 * @date 2015年6月18日 下午6:23:56
	 * 
	 * @param request
	 * @return
	 */
	@Remark("修改定义颜色")
	@RequestMapping(value = "/colorDef/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<ColorDef> updateColorDef(HttpServletRequest request, @RequestBody ColorDef colorDef) {
		Result<ColorDef> result = Result.newOne();
		boolean ok = true;
		try {
			ok = categService.updateColorDef(colorDef);
			result.data = colorDef;

		} catch (Exception e) {
			ok = false;
			result.type = Result.Type.error;
		}

		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	@Remark("删除定义颜色")
	@RequestMapping(value = "/colorDef/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> delColorDefById(HttpServletRequest request, @RequestBody ColorDef colorDef) {
		Result<?> result = Result.newOne();
		boolean ok = true;
		try {
			ok = categService.deleteColorDefById(colorDef.getId());
		} catch (Exception e) {
			ok = false;
			result.type = Result.Type.error;
		}

		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	/**
	 * 颜色定义名称是否存在
	 * 
	 * @author zjl
	 * @date 2015年7月20日 下午12:02:27
	 * 
	 * @return
	 */
	@Remark("颜色定义名称是否存在")
	@RequestMapping(value = "/colorDef/name/isabel/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getColorDefByName(HttpServletRequest request, @RequestBody ColorDef colorDef) {
		Result<Boolean> result = Result.newOne();
		if (colorDef.getName() != null) {
			result.data = (categService.getColorDefByName(colorDef.getName()) == null);
		}
		return result;
	}

	// --------------------------------------- 品牌定义----------------------------------------------

	/**
	 * 返回品牌定义页面
	 * 
	 * @author 廖晓远
	 * @date 2015年6月23日 下午4:31:16
	 * 
	 * @return
	 */
	@Remark("品牌定义页面")
	@RequestMapping(value = "/brandDef/list/jsp", method = RequestMethod.GET)
	public String toBrandDefPage() {
		return "categ/brandDef";
	}

	/**
	 * 分页查询品牌定义
	 * 
	 * @author 廖晓远
	 * @date 2015年6月23日 下午4:32:47
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询品牌定义")
	@RequestMapping(value = "/brandDef/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<BrandDef> getBrandDefs(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<BrandDef> paginatedList = categService.getBrandDefs(paginatedFilter);
		JqGridPage<BrandDef> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 通过商品分类Id分页查询品牌定义
	 * 
	 * @author 廖晓远
	 * @date 2015年7月20日 下午5:24:41
	 * 
	 * @param request
	 * @return
	 */
	@Remark("通过商品分类Id分页查询品牌定义")
	@RequestMapping(value = "/brandDef/list/get/by/catId", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<BrandDef> getBrandDefsByCatId(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<BrandDef> paginatedList = categService.getBrandDefsByCatId(paginatedFilter);
		JqGridPage<BrandDef> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 创建品牌定义
	 * 
	 * @author 廖晓远
	 * @date 2015年6月23日 下午4:42:16
	 * 
	 * @param request
	 * @param brandDef
	 * @return
	 */
	@Remark("创建品牌定义")
	@RequestMapping(value = "/brandDef/create/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<BrandDef> saveBrandDef(HttpServletRequest request, @RequestBody BrandDef brandDef) {
		Result<BrandDef> result = Result.newOne();
		boolean flag = true;
		try {
			flag = categService.saveBrandDef(brandDef);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			result.type = Result.Type.error;
		}
		result.data = brandDef;
		result.message = flag ? "添加成功" : "添加失败";
		return result;
	}

	/**
	 * 修改品牌定义
	 * 
	 * @author 廖晓远
	 * @date 2015年6月23日 下午4:46:20
	 * 
	 * @param request
	 * @param brandDef
	 * @return
	 */
	@Remark("修改品牌定义")
	@RequestMapping(value = "/brandDef/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<BrandDef> updateBrandDef(HttpServletRequest request, @RequestBody BrandDef brandDef) {
		Result<BrandDef> result = Result.newOne();
		boolean flag = true;
		try {
			flag = categService.updateBrandDef(brandDef);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			result.type = Result.Type.error;
		}
		result.data = brandDef;
		result.message = flag ? "修改成功" : "修改失败";
		return result;
	}

	@Remark("删除品牌定义")
	@RequestMapping(value = "/brandDef/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteBrandDef(HttpServletRequest request, @RequestBody BrandDef brandDef) {
		Result<?> result = Result.newOne();
		boolean flag = true;
		try {
			Integer id = brandDef.getId();
			flag = categService.deleteBrandDefById(id);
		} catch (Exception e) {
			flag = false;
			result.type = Result.Type.error;
		}

		result.message = flag ? "删除成功" : "删除失败";
		return result;
	}

	/**
	 * 获取Code是否存在
	 * 
	 * @author 廖晓远
	 * @date 2015年6月25日 上午10:13:01
	 * 
	 * @param code
	 * @return
	 */
	@Remark("code是否存在")
	@RequestMapping(value = "/brandDef/exist/by/code/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getBrandDefExistByCode(@RequestBody String code) {
		Result<Boolean> result = Result.newOne();
		if (code != null) {
			result.data = (categService.getBrandDefByCode(code) != null);
		}
		return result;
	}

	@Remark("检查品牌是否被使用")
	@RequestMapping(value = "/brandDef/check/inUse/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> checkBrandDefInUse(@RequestBody MapContext requestData) {
		Result<Boolean> result = Result.newOne();
		Integer defId = requestData.getTypedValue("id", Integer.class);
		boolean inUse = categService.getInUseBrandDefById(defId);
		result.data = inUse;
		return result;
	}

	@Remark("检查规格是否被使用")
	@RequestMapping(value = "/specRef/check/inUse/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> checkSpecRefInUse(@RequestBody MapContext requestData) {
		Result<Boolean> result = Result.newOne();
		Integer specRefId = requestData.getTypedValue("id", Integer.class);
		boolean inUse = categService.getInUseSpecRefById(specRefId);
		result.data = inUse;
		return result;
	}

	@Remark("检查属性是否被使用")
	@RequestMapping(value = "/attrRef/check/inUse/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> checkAttrRefInUse(@RequestBody MapContext requestData) {
		Result<Boolean> result = Result.newOne();
		Integer attrRefId = requestData.getTypedValue("id", Integer.class);
		boolean inUse = categService.getInUseAttrRefById(attrRefId);
		result.data = inUse;
		return result;
	}

	@Remark("检查颜色是否被使用")
	@RequestMapping(value = "/colorDef/check/inUse/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> checkColorDefInUse(@RequestBody MapContext requestData) {
		Result<Boolean> result = Result.newOne();
		Integer colorDefId = requestData.getTypedValue("id", Integer.class);
		boolean inUse = categService.getInUseColorDefById(colorDefId);
		result.data = inUse;
		return result;
	}

	// --------------------------------------- 商品分类-价格区间 ----------------------------------------------

	/**
	 * 通过分类Id查询商品分类价格区间
	 * 
	 * @author 廖晓远
	 * @date 2015年7月27日 下午4:16:05
	 * 
	 * @param request
	 * @return
	 */
	@Remark("通过分类Id查询商品分类价格区间")
	@RequestMapping(value = "/goodsCatPriceRange/list/get/by/catId", method = RequestMethod.POST)
	@ResponseBody
	public List<GoodsCatPriceRange> getGoodsCatPriceRanges(HttpServletRequest request) {
		List<GoodsCatPriceRange> goodsCatPriceRanges = new ArrayList<GoodsCatPriceRange>(0);
		String filterStr = request.getParameter("filterStr");
		if (filterStr != null) {
			GoodsCatPriceRange goodsCatPriceRange = JsonUtil.fromJson(filterStr, GoodsCatPriceRange.class);
			if (goodsCatPriceRange != null) {
				goodsCatPriceRanges = categService.getCatPriceRangesByCatId(goodsCatPriceRange.getCatId());
			}
		}
		return goodsCatPriceRanges;
	}

	// TODO--------wjj----------begin--------------------- 商品咨询 ----------------------------------------------
	/**
	 * 商品咨询页面
	 * 
	 * @author WJJ
	 * @date 2015年9月25日 上午11:54:40
	 * 
	 * @return
	 */
	@Remark("商品咨询页面")
	@RequestMapping(value = "/consult/jsp", method = RequestMethod.GET)
	public String toConsultPage() {
		return "categ/consult";
	}

	@Remark("获取商品咨询列表信息")
	@RequestMapping(value = "/consult/list", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<GoodsInquiry> getGoodsInquiryByPage(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<GoodsInquiry> paginatedList = interactService.getGoodsInquiriesByFilter(paginatedFilter);
		//
		JqGridPage<GoodsInquiry> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	// --------wjj----------end--------------------- 商品咨询 ----------------------------------------------

	// TODO--------wjj----------begin--------------------- 商品评论 ----------------------------------------------
	/**
	 * 商品评论页面
	 * 
	 * @author WJJ
	 * @date 2015年9月25日 上午11:54:40
	 * 
	 * @return
	 */
	@Remark("商品评论页面")
	@RequestMapping(value = "/comment/jsp", method = RequestMethod.GET)
	public String toCommentPage() {
		return "categ/comment";
	}
	// --------wjj----------end--------------------- 商品评论 ----------------------------------------------
}
