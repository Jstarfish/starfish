package priv.starfish.mall.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.categ.AttrRefDao;
import priv.starfish.mall.dao.categ.BrandDefDao;
import priv.starfish.mall.dao.categ.ColorDefDao;
import priv.starfish.mall.dao.categ.GoodsCatAttrDao;
import priv.starfish.mall.dao.categ.GoodsCatAttrGroupDao;
import priv.starfish.mall.dao.categ.GoodsCatAttrItemDao;
import priv.starfish.mall.dao.categ.GoodsCatDao;
import priv.starfish.mall.dao.categ.GoodsCatMenuDao;
import priv.starfish.mall.dao.categ.GoodsCatMenuItemAdDao;
import priv.starfish.mall.dao.categ.GoodsCatMenuItemCatDao;
import priv.starfish.mall.dao.categ.GoodsCatMenuItemDao;
import priv.starfish.mall.dao.categ.GoodsCatMenuItemLinkDao;
import priv.starfish.mall.dao.categ.GoodsCatPriceRangeDao;
import priv.starfish.mall.dao.categ.GoodsCatSpecDao;
import priv.starfish.mall.dao.categ.GoodsCatSpecItemDao;
import priv.starfish.mall.dao.categ.SpecRefDao;
import priv.starfish.mall.categ.entity.AttrRef;
import priv.starfish.mall.categ.entity.BrandDef;
import priv.starfish.mall.categ.entity.ColorDef;
import priv.starfish.mall.categ.entity.GoodsCat;
import priv.starfish.mall.categ.entity.GoodsCatAttr;
import priv.starfish.mall.categ.entity.GoodsCatAttrGroup;
import priv.starfish.mall.categ.entity.GoodsCatAttrItem;
import priv.starfish.mall.categ.entity.GoodsCatMenu;
import priv.starfish.mall.categ.entity.GoodsCatMenuItem;
import priv.starfish.mall.categ.entity.GoodsCatMenuItemCat;
import priv.starfish.mall.categ.entity.GoodsCatMenuItemLink;
import priv.starfish.mall.categ.entity.GoodsCatPriceRange;
import priv.starfish.mall.categ.entity.GoodsCatSpec;
import priv.starfish.mall.categ.entity.GoodsCatSpecItem;
import priv.starfish.mall.categ.entity.SpecRef;
import priv.starfish.mall.comn.misc.SysParamInfo;
import priv.starfish.mall.dao.goods.GoodsAttrValDao;
import priv.starfish.mall.dao.goods.ProductSpecValDao;
import priv.starfish.mall.service.CategService;

@Service("categService")
public class CategServiceImpl extends BaseServiceImpl implements CategService {

	@Resource
	SpecRefDao specRefDao;

	@Resource
	GoodsCatSpecDao goodsCatSpecDao;

	@Resource
	GoodsCatAttrGroupDao goodsCatAttrGroupDao;

	@Resource
	GoodsCatDao goodsCatDao;

	@Resource
	AttrRefDao attrRefDao;

	@Resource
	GoodsCatAttrDao goodsCatAttrDao;

	@Resource
	GoodsCatAttrItemDao goodsCatAttrItemDao;

	@Resource
	GoodsCatSpecItemDao goodsCatSpecItemDao;

	@Resource
	GoodsCatMenuDao goodsCatMenuDao;

	@Resource
	GoodsCatMenuItemDao goodsCatMenuItemDao;

	@Resource
	GoodsCatMenuItemAdDao goodsCatMenuItemAdDao;

	@Resource
	GoodsCatMenuItemCatDao goodsCatMenuItemCatDao;

	@Resource
	GoodsCatMenuItemLinkDao goodsCatMenuItemLinkDao;

	@Resource
	ColorDefDao colorDefDao;

	@Resource
	BrandDefDao brandDefDao;
	@Resource
	FileRepository fileRepository;

	@Resource
	ProductSpecValDao productSpecValDao;

	@Resource
	GoodsCatPriceRangeDao goodsCatPriceRangeDao;
	@Resource
	GoodsAttrValDao goodsAttrValDao;

	// ---------------------------------规格参照-------------------------------------
	@Override
	public SpecRef getSpecRefById(Integer id) {
		return specRefDao.selectById(id);
	}

	@Override
	public SpecRef getSpecRefByCode(String code) {
		return specRefDao.selectByCode(code);
	}

	@Override
	public PaginatedList<SpecRef> getSpecRefs(PaginatedFilter paginatedFilter) {
		return specRefDao.selectSpecRef(paginatedFilter);
	}

	@Override
	public boolean saveSpecRef(SpecRef specRef) {
		// 后续业务有改动时可改这里
		if (!StrUtil.isNullOrBlank(specRef.getName())) {
			specRef.setCode(StrUtil.chsToPinyin(specRef.getName()));
		}
		//
		return specRefDao.insert(specRef) > 0;
	}

	@Override
	public boolean updateSpecRef(SpecRef specRef) {
		// 后续业务有改动时可改这里
		if (!StrUtil.isNullOrBlank(specRef.getName())) {
			specRef.setCode(StrUtil.chsToPinyin(specRef.getName()));
		}
		if (specRef.getSeqNo() == null) {
			specRef.setSeqNo(1);
		}
		return specRefDao.update(specRef) > 0;
	}

	@Override
	public boolean deleteSpecRefById(Integer id) {
		List<Integer> goodsCatSpecId = goodsCatSpecDao.selectIdsByRefId(id);
		productSpecValDao.deleteBySpecIds(goodsCatSpecId);
		goodsCatSpecItemDao.deleteBySpecIds(goodsCatSpecId);
		goodsCatSpecDao.deleteByRefId(id);
		return specRefDao.deleteById(id) > 0;
	}

	@Override
	public boolean deleteSpecRefsByIds(List<Integer> ids) {
		List<Integer> goodsCatSpecIds = goodsCatSpecDao.selectIdByRefIds(ids);
		productSpecValDao.deleteBySpecIds(goodsCatSpecIds);
		goodsCatSpecItemDao.deleteBySpecIds(goodsCatSpecIds);
		goodsCatSpecDao.deleteByRefIds(ids);
		return specRefDao.batchDelByIds(ids) > 0;
	}

	// -------------------------------商品分类_规格-----------------------------------
	@Override
	public GoodsCatSpec getCatSpecById(Integer id) {
		return goodsCatSpecDao.selectById(id);
	}

	@Override
	public GoodsCatSpec getCatSpecByCatIdAndRefId(Integer catId, Integer refId) {
		return goodsCatSpecDao.selectByCatIdAndRefId(catId, refId);
	}

	@Override
	public boolean saveCatSpec(GoodsCatSpec goodsCatSpec) {
		if (goodsCatSpec.getGroupId() == null) {
			goodsCatSpec.setGroupId(0);
		}
		boolean result = goodsCatSpecDao.insert(goodsCatSpec) > 0;
		List<GoodsCatSpecItem> goodsCatSpecItems = goodsCatSpec.getGoodsCatSpecItems();
		if (goodsCatSpecItems != null && goodsCatSpecItems.size() > 0) {
			for (GoodsCatSpecItem goodsCatSpecItem : goodsCatSpecItems) {
				goodsCatSpecItem.setSpecId(goodsCatSpec.getId());
				goodsCatSpecItemDao.insert(goodsCatSpecItem);
			}
		}
		return result;
	}

	@Override
	public boolean updateCatSpec(GoodsCatSpec goodsCatSpec) {
		List<GoodsCatSpecItem> goodsCatSpecItems = goodsCatSpec.getGoodsCatSpecItems();
		if (goodsCatSpecItems != null && goodsCatSpecItems.size() > 0) {
			Integer specId = goodsCatSpec.getId();
			List<Integer> uncontainIds = new ArrayList<Integer>(0);
			for (GoodsCatSpecItem goodsCatSpecItem : goodsCatSpecItems) {
				Integer itemId = goodsCatSpecItem.getId();
				goodsCatSpecItem.setSpecId(specId);
				if (itemId != null && itemId > 0) {
					goodsCatSpecItemDao.update(goodsCatSpecItem);
					uncontainIds.add(itemId);
				} else {
					goodsCatSpecItemDao.insert(goodsCatSpecItem);
					uncontainIds.add(goodsCatSpecItem.getId());
				}
			}
			goodsCatSpecItemDao.deleteBySpecIdAndUncontainIds(specId, uncontainIds);
		}

		return goodsCatSpecDao.update(goodsCatSpec) > 0;
	}

	@Override
	public boolean updateGoodsCatSpecs(List<GoodsCatSpec> goodsCatSpecs) {
		boolean result = true;
		for (GoodsCatSpec goodsCatSpec : goodsCatSpecs) {
			boolean flag = true;
			Integer specId = goodsCatSpec.getId();
			if (specId != null && specId > 0) {
				flag = updateCatSpec(goodsCatSpec);
			} else {
				flag = saveCatSpec(goodsCatSpec);
			}
			result = result && flag;
		}
		return result;
	}

	@Override
	public boolean deleteCatSpecById(Integer id) {
		goodsCatSpecItemDao.deleteBySpecId(id);
		productSpecValDao.deleteBySpecId(id);
		return goodsCatSpecDao.deleteById(id) > 0;
	}

	@Override
	public int countCatSpecByCatId(Integer catId) {
		return goodsCatSpecDao.selectCountByCatId(catId);
	}

	@Override
	public List<GoodsCatSpec> getCatSpecsByCatId(Integer catId) {
		List<GoodsCatSpec> goodsCatSpecs = goodsCatSpecDao.selectByCatId(catId);
		for (GoodsCatSpec goodsCatSpec : goodsCatSpecs) {
			goodsCatSpec.setGoodsCatSpecItems(goodsCatSpecItemDao.selectBySpecId(goodsCatSpec.getId()));
		}
		return goodsCatSpecs;
	}

	@Override
	public List<GoodsCatSpec> getGoodCatSpecsByCatId(Integer catId, Integer goodsId) {
		List<GoodsCatSpec> goodsCatSpecs = goodsCatSpecDao.selectByCatId(catId);
		for (GoodsCatSpec goodsCatSpec : goodsCatSpecs) {
			List<Integer> specItemIds = productSpecValDao.selectSpecItemIdsBySpecIdAndGoodsId(goodsCatSpec.getId(), goodsId);
			if (specItemIds.size() > 0) {
				goodsCatSpec.setGoodsCatSpecItems(goodsCatSpecItemDao.selectGoodsCatSpecItemBySpecItemIds(specItemIds));
			}
		}
		return goodsCatSpecs;
	}

	@Override
	public boolean deleteCatSpecByIds(List<Integer> ids) {
		goodsCatSpecItemDao.deleteBySpecIds(ids);
		return goodsCatSpecDao.deleteByIds(ids) > 0;
	}

	@Override
	public List<Integer> getCatSpecIdsByCatId(Integer catId) {
		return goodsCatSpecDao.selectIdByCatId(catId);
	}

	@Override
	public List<Integer> getCatSpecIdsByCatIds(List<Integer> catIds) {
		return goodsCatSpecDao.selectIdByCatIds(catIds);
	}

	@Override
	public boolean saveCatSpecs(List<GoodsCatSpec> goodsCatSpecs) {
		boolean result = true;
		for (GoodsCatSpec goodsCatSpec : goodsCatSpecs) {
			result = result && saveCatSpec(goodsCatSpec);
		}
		return result;
	}

	// ------------------------------商品分类_属性分组---------------------------------
	@Override
	public GoodsCatAttrGroup getCatAttrGroupById(Integer id) {
		return goodsCatAttrGroupDao.selectById(id);
	}

	@Override
	public GoodsCatAttrGroup getCatAttrGroupByCatIdAndName(Integer catId, String name) {
		return goodsCatAttrGroupDao.selectByCatIdAndName(catId, name);
	}

	@Override
	public boolean saveCatAttrGroup(GoodsCatAttrGroup goodsCatAttrGroup) {
		if (goodsCatAttrGroup.getSeqNo() == null) {
			goodsCatAttrGroup.setSeqNo(1);
		}
		return goodsCatAttrGroupDao.insert(goodsCatAttrGroup) > 0;
	}

	@Override
	public boolean saveCatAttrGroups(List<GoodsCatAttrGroup> goodsCatAttrGroups) {
		boolean result = true;
		for (GoodsCatAttrGroup goodsCatAttrGroup : goodsCatAttrGroups) {
			result = result && saveCatAttrGroup(goodsCatAttrGroup);
		}
		return result;
	}

	@Override
	public boolean updateCatAttrGroup(GoodsCatAttrGroup goodsCatAttrGroup) {
		return goodsCatAttrGroupDao.update(goodsCatAttrGroup) > 0;
	}

	@Override
	public boolean updateCatAttrGroups(List<GoodsCatAttrGroup> goodsCatAttrGroups) {
		boolean result = true;
		if (goodsCatAttrGroups.size() > 0) {
			List<Integer> ids = new ArrayList<Integer>(0);
			for (GoodsCatAttrGroup goodsCatAttrGroup : goodsCatAttrGroups) {
				Integer id = goodsCatAttrGroup.getId();
				if (id != null && id > 0) {
					ids.add(id);
					result = result && updateCatAttrGroup(goodsCatAttrGroup);
				} else {
					result = result && saveCatAttrGroup(goodsCatAttrGroup);
					ids.add(goodsCatAttrGroup.getId());
				}
			}
			deleteCatAttrGroupByCatIdAndNotInIds(goodsCatAttrGroups.get(0).getCatId(), ids);
		}
		return result;
	}

	@Override
	public boolean deleteCatAttrGroupById(Integer id) {
		return goodsCatAttrGroupDao.deleteById(id) > 0;
	}

	@Override
	public boolean deleteCatAttrGroupByCatIdAndNotInIds(Integer catId, List<Integer> ids) {
		return goodsCatAttrGroupDao.deleteByUncontainIdsAndCatId(ids, catId) > 0;
	}

	@Override
	public int countCatAttrGroupByCatId(Integer catId) {
		return goodsCatAttrGroupDao.selectCountByCatId(catId);
	}

	@Override
	public List<GoodsCatAttrGroup> getCatAttrGroupByCatId(Integer catId) {
		return goodsCatAttrGroupDao.selectByCatId(catId);
	}

	// ------------------------------商品分类---------------------------------

	@SuppressWarnings("unchecked")
	@Override
	public boolean saveGoodsCat(GoodsCat goodsCat) {
		boolean flag = goodsCatDao.insert(goodsCat) > 0;
		if (goodsCat.getLevel() == 1) {
			goodsCat.setIdPath(goodsCat.getId() + "");
		} else {
			goodsCat.setIdPath(goodsCat.getIdPath() + "," + goodsCat.getId());
		}
		goodsCatDao.update(goodsCat);
		Integer catId = goodsCat.getId();
		if (flag) {
			List<GoodsCatPriceRange> priceRanges = goodsCat.getGoodsCatPriceRanges();
			if (priceRanges != null) {
				for (GoodsCatPriceRange priceRange : priceRanges) {
					priceRange.setCatId(catId);
				}
				saveCatPriceRanges(priceRanges);
			}

			List<GoodsCatAttrGroup> groups = goodsCat.getCatAttrGroups();
			for (GoodsCatAttrGroup group : groups) {
				group.setCatId(catId);
			}
			saveCatAttrGroups(groups);

			Map<String, Object> groupDatas = goodsCat.getGroupDatas();
			Map<Integer, Integer> groupAttrMap = new HashMap<Integer, Integer>(0);
			Map<Integer, Integer> groupSpecMap = new HashMap<Integer, Integer>(0);
			if (groupDatas != null) {
				Map<String, Integer> groupIdMap = new HashMap<String, Integer>(0);
				for (GoodsCatAttrGroup goodsCatAttrGroup : groups) {
					groupIdMap.put(goodsCatAttrGroup.getName(), goodsCatAttrGroup.getId());
				}
				Iterator<String> it = groupDatas.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					Integer groupId = groupIdMap.get(key);
					Map<String, Object> map = (Map<String, Object>) groupDatas.get(key);
					if (map.get("attr") != null) {
						Map<String, Object> attrMap = (Map<String, Object>) map.get("attr");
						if (attrMap != null) {
							Set<String> attrIds = attrMap.keySet();
							for (String attrId : attrIds) {
								groupAttrMap.put(Integer.valueOf(attrId), groupId);
							}
						}
					}
					if (map.get("spec") != null) {
						Map<String, Object> specMap = (Map<String, Object>) map.get("spec");
						if (specMap != null) {
							Set<String> specIds = specMap.keySet();
							for (String specId : specIds) {
								groupSpecMap.put(Integer.valueOf(specId), groupId);
							}
						}
					}
				}
			}
			List<GoodsCatAttr> catAttrs = goodsCat.getCatAttrs();
			for (GoodsCatAttr catAttr : catAttrs) {
				catAttr.setCatId(catId);
				catAttr.setGroupId(groupAttrMap.get(catAttr.getRefId()));
			}
			saveGoodsCatAttrs(catAttrs);

			List<GoodsCatSpec> catSpecs = goodsCat.getCatSpecs();
			for (GoodsCatSpec catSpec : catSpecs) {
				catSpec.setCatId(catId);
				catSpec.setGroupId(groupAttrMap.get(catSpec.getRefId()));
			}
			saveCatSpecs(catSpecs);
		}
		//设置叶子节点
		Integer systemLevel = SysParamInfo.goodsCategLevels;
		Integer level = goodsCat.getLevel();
		if(systemLevel == level){
			goodsCatDao.updateLeafFlagById(true, goodsCat.getId());
		}
		//
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean updateGoodsCat(GoodsCat goodsCat) {
		boolean flag = true;
		flag = goodsCatDao.update(goodsCat) > 0;
		if (flag) {
			List<GoodsCatPriceRange> priceRanges = goodsCat.getGoodsCatPriceRanges();
			if (priceRanges != null) {
				for (GoodsCatPriceRange priceRange : priceRanges) {
					priceRange.setCatId(goodsCat.getId());
				}
				updateCatPriceRanges(priceRanges);
			}
			List<GoodsCatAttrGroup> groups = goodsCat.getCatAttrGroups();
			if (groups.size() > 0) {
				updateCatAttrGroups(groups);
			} else {
				goodsCatAttrGroupDao.deleteByCatId(goodsCat.getId());
			}

			Map<String, Object> groupDatas = goodsCat.getGroupDatas();
			Map<Integer, Integer> groupAttrMap = new HashMap<Integer, Integer>(0);
			Map<Integer, Integer> groupSpecMap = new HashMap<Integer, Integer>(0);
			if (groupDatas != null) {
				Map<String, Integer> groupIdMap = new HashMap<String, Integer>(0);
				for (GoodsCatAttrGroup goodsCatAttrGroup : groups) {
					groupIdMap.put(goodsCatAttrGroup.getName(), goodsCatAttrGroup.getId());
				}
				Iterator<String> it = groupDatas.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					Integer groupId = groupIdMap.get(key);
					Map<String, Object> map = (Map<String, Object>) groupDatas.get(key);
					if (map.get("attr") != null) {
						Map<String, Object> attrMap = (Map<String, Object>) map.get("attr");
						if (attrMap != null) {
							Set<String> attrIds = attrMap.keySet();
							for (String attrId : attrIds) {
								groupAttrMap.put(Integer.valueOf(attrId), groupId);
							}
						}
					}
					if (map.get("spec") != null) {
						Map<String, Object> specMap = (Map<String, Object>) map.get("spec");
						if (specMap != null) {
							Set<String> specIds = specMap.keySet();
							for (String specId : specIds) {
								groupSpecMap.put(Integer.valueOf(specId), groupId);
							}
						}
					}
				}
			}

			List<GoodsCatAttr> catAttrs = goodsCat.getCatAttrs();
			for (GoodsCatAttr catAttr : catAttrs) {
				Integer groupId = groupAttrMap.get(catAttr.getRefId());
				if (groupId != null && groupId > 0) {
					catAttr.setGroupId(groupId);
				} else {
					catAttr.setGroupId(0);
				}
			}
			updateGoodsCatAttrs(catAttrs);

			List<GoodsCatSpec> catSpecs = goodsCat.getCatSpecs();
			for (GoodsCatSpec catSpec : catSpecs) {
				Integer groupId = groupSpecMap.get(catSpec.getRefId());
				if (groupId != null && groupId > 0) {
					catSpec.setGroupId(groupId);
				} else {
					catSpec.setGroupId(0);
				}
			}
			updateGoodsCatSpecs(catSpecs);
		}

		return flag;
	}

	@Override
	public boolean deleteGoodsCatById(Integer id) {
		goodsCatPriceRangeDao.deleteByCatId(id);
		goodsCatAttrGroupDao.deleteByCatId(id);
		List<Integer> goodsCatAttrIds = getCatAttrIdsByCatId(id);
		deleteGoodsCatAttrByIds(goodsCatAttrIds);
		List<Integer> goodsCatSpecIds = getCatSpecIdsByCatId(id);
		deleteCatSpecByIds(goodsCatSpecIds);
		return goodsCatDao.deleteById(id) > 0;
	}

	@Override
	public boolean deleteGoodsCatByIds(List<Integer> ids) {
		goodsCatPriceRangeDao.deleteByCatIds(ids);
		goodsCatAttrGroupDao.deleteByCatIds(ids);
		List<Integer> goodsCatAttrIds = getCatAttrIdsByCatIds(ids);
		deleteGoodsCatAttrByIds(goodsCatAttrIds);
		List<Integer> goodsCatSpecIds = getCatSpecIdsByCatIds(ids);
		deleteCatSpecByIds(goodsCatSpecIds);
		return goodsCatDao.deleteByIds(ids) > 0;
	}

	@Override
	public List<GoodsCat> getGoodsCatsByParentId(Integer parentId) {
		return goodsCatDao.selectByParentId(parentId);
	}

	@Override
	public List<GoodsCat> getAllGoodsCatsByParentId(Integer parentId) {
		List<GoodsCat> all = new ArrayList<GoodsCat>();
		List<GoodsCat> cats = getGoodsCatsByParentId(parentId);
		all.addAll(cats);
		for (GoodsCat cat : cats) {
			all.addAll(getGoodsCatsByParentId(cat.getId()));
		}
		return all;
	}

	@Override
	public List<GoodsCat> getGoodsCatsByName(String name) {
		return goodsCatDao.selectByName(name);
	}

	@Override
	public GoodsCat getGoodsCatById(Integer id) {
		return goodsCatDao.selectById(id);
	}

	@Override
	public List<GoodsCat> getGoodsCatsByLevel(Integer level) {
		return goodsCatDao.selectByLevel(level);
	}

	@Override
	public List<GoodsCat> getSiblingGoodsCatById(Integer id) {
		return goodsCatDao.selectByParentId(goodsCatDao.selectById(id).getParentId());
	}

	// ------------------------------属性参照--------------------------------------

	@Override
	public boolean saveAttrRef(AttrRef attrRef) {
		return attrRefDao.insert(attrRef) > 0;
	}

	@Override
	public boolean deleteAttrRefById(Integer id) {
		List<Integer> goodsCatAttrId = goodsCatAttrDao.selectIdsByRefId(id);
		goodsAttrValDao.deleteByAttrIds(goodsCatAttrId);
		goodsCatAttrItemDao.deleteByAttrIds(goodsCatAttrId);
		goodsCatAttrDao.deleteByRefId(id);
		return attrRefDao.deleteById(id) > 0;
	}

	@Override
	public boolean updateAttrRef(AttrRef attrRef) {
		return attrRefDao.update(attrRef) > 0;
	}

	@Override
	public boolean deleteAttrRefByIds(List<Integer> ids) {
		List<Integer> goodsCatAttrids = goodsCatAttrDao.selectIdsByRefIds(ids);
		goodsAttrValDao.deleteByAttrIds(goodsCatAttrids);
		goodsCatAttrItemDao.deleteByAttrIds(goodsCatAttrids);
		goodsCatAttrDao.deleteByRefIds(ids);
		return attrRefDao.deleteBatch(ids) > 0;
	}

	@Override
	public AttrRef getAttrRefByName(String name) {
		return attrRefDao.selectByName(name);
	}

	@Override
	public PaginatedList<AttrRef> getAttrRefsByFilter(PaginatedFilter paginatedFilter) {
		return attrRefDao.selectByfilter(paginatedFilter);
	}

	@Override
	public boolean getAttrRefByBrandFlagIsTrue() {
		return attrRefDao.selectByBrandFlagIsTrue() == null;
	}

	// ------------------------------商品分类_属性参照--------------------------------------

	@Override
	public boolean saveGoodsCatAttr(GoodsCatAttr goodsCatAttr) {
		boolean result = goodsCatAttrDao.insert(goodsCatAttr) > 0;
		List<GoodsCatAttrItem> goodsCatAttrItems = goodsCatAttr.getGoodsCatAttrItems();
		if (goodsCatAttrItems != null && goodsCatAttrItems.size() > 0) {
			for (GoodsCatAttrItem goodsCatAttrItem : goodsCatAttrItems) {
				goodsCatAttrItem.setAttrId(goodsCatAttr.getId());
				goodsCatAttrItemDao.insert(goodsCatAttrItem);
			}
		}
		return result;
	}

	@Override
	public boolean saveGoodsCatAttrs(List<GoodsCatAttr> goodsCatAttrs) {
		boolean result = true;
		for (GoodsCatAttr goodsCatAttr : goodsCatAttrs) {
			result = result && saveGoodsCatAttr(goodsCatAttr);
		}
		return result;
	}

	@Override
	public boolean updateGoodsCatAttr(GoodsCatAttr goodsCatAttr) {
		boolean result = goodsCatAttrDao.update(goodsCatAttr) > 0;
		List<GoodsCatAttrItem> goodsCatAttrItems = goodsCatAttr.getGoodsCatAttrItems();
		List<Integer> uncontainIds = new ArrayList<Integer>(0);
		if (goodsCatAttrItems != null && goodsCatAttrItems.size() > 0) {
			Integer attrId = goodsCatAttr.getId();
			for (GoodsCatAttrItem goodsCatAttrItem : goodsCatAttrItems) {
				goodsCatAttrItem.setAttrId(attrId);
				Integer itemId = goodsCatAttrItem.getId();
				if (itemId != null && itemId > 0) {
					goodsCatAttrItemDao.update(goodsCatAttrItem);
					uncontainIds.add(itemId);
				} else {
					goodsCatAttrItemDao.insert(goodsCatAttrItem);
					uncontainIds.add(goodsCatAttrItem.getId());
				}
			}
			goodsCatAttrItemDao.deleteByAttrIdAndUncontainIds(attrId, uncontainIds);
		}
		return result;
	}

	@Override
	public boolean updateGoodsCatAttrs(List<GoodsCatAttr> goodsCatAttrs) {
		boolean result = true;
		for (GoodsCatAttr goodsCatAttr : goodsCatAttrs) {
			boolean flag = true;
			Integer catId = goodsCatAttr.getId();
			if (catId != null && catId > 0) {
				flag = updateGoodsCatAttr(goodsCatAttr);
			} else {
				flag = saveGoodsCatAttr(goodsCatAttr);
			}
			result = result && flag;
		}
		return result;
	}

	@Override
	public int countCatAttrByCatId(Integer catId) {
		return goodsCatAttrDao.selectCountByCatId(catId);
	}

	@Override
	public List<GoodsCatAttr> getCatAttrsByCatId(Integer catId) {
		List<GoodsCatAttr> goodsCatAttrs = goodsCatAttrDao.selectByCatId(catId);
		for (GoodsCatAttr goodsCatAttr : goodsCatAttrs) {
			if (goodsCatAttr.getAttrRef().getEnumFlag()) {
				goodsCatAttr.setGoodsCatAttrItems(goodsCatAttrItemDao.selectByAttrId(goodsCatAttr.getId()));
			}
		}
		return goodsCatAttrs;
	}

	@Override
	public List<GoodsCatAttr> getCatAttrsByGroupIdAndGoodsId(Integer groupId, Integer goodsId) {
		List<GoodsCatAttr> goodsCatAttrs = goodsCatAttrDao.selectByGroupId(groupId);
		for (GoodsCatAttr goodsCatAttr : goodsCatAttrs) {
			goodsCatAttr.getAttrRef().setGoodsAttrVals(goodsAttrValDao.selectByGoodsIdAndAttrId(goodsId, goodsCatAttr.getId()));
		}
		return goodsCatAttrs;
	}

	@Override
	public boolean deleteCatAttrById(Integer attrId) {
		goodsAttrValDao.deleteByAttrId(attrId);
		goodsCatAttrItemDao.deleteByAttrId(attrId);
		return goodsCatAttrDao.deleteById(attrId) > 0;
	}
	
	@Override
	public boolean deleteGoodsCatAttrByIds(List<Integer> ids) {
		goodsCatAttrItemDao.deleteByAttrIds(ids);
		return goodsCatAttrDao.deleteByIds(ids) > 0;
	}
	
	@Override
	public List<Integer> getCatAttrIdsByCatId(Integer catId) {
		return goodsCatAttrDao.selectIdByCatId(catId);
	}

	@Override
	public List<Integer> getCatAttrIdsByCatIds(List<Integer> catIds) {
		return goodsCatAttrDao.selectIdByCatIds(catIds);
	}

	// --------------------------------商品分类菜单----------------------------------
	@Override
	public GoodsCatMenu getGoodsCatMenuById(Integer id) {
		return goodsCatMenuDao.selectById(id);
	}

	@Override
	public GoodsCatMenuItem getGoodsCatMenuItemByMenuIdAndLevelAndName(Integer menuId, int level, String name) {
		return goodsCatMenuItemDao.selectByMenuIdAndLevelAndName(menuId, level, name);
	}

	@Override
	public GoodsCatMenuItemLink getGoodsCatMenuItemLinkByMenuItemIdAndName(Integer menuItemId, String name) {
		return goodsCatMenuItemLinkDao.selectByMenuItemIdAndName(menuItemId, name);
	}

	@Override
	public List<GoodsCatMenuItem> getMenuItemsByMenuIdAndLevel(Integer menuId, Integer level) {
		return goodsCatMenuItemDao.selectByMenuIdAndLevel(menuId, level);
	}
	
	@Override
	public GoodsCatMenuItem getMenuItemsByMenuIdAndLevelAndName(Integer menuId, Integer level, String name) {
		return goodsCatMenuItemDao.selectByMenuIdAndLevelAndName(menuId, level, name);
	}

	@Override
	public List<GoodsCatMenuItemLink> getMenuItemLinksByMenuItemId(Integer menuItemId) {
		return goodsCatMenuItemLinkDao.selectByMenuItemId(menuItemId);
	}

	@Override
	public List<GoodsCatMenuItem> getMenuItemsByPId(Integer parentId) {
		return goodsCatMenuItemDao.selectByPId(parentId);
	}

	@Override
	public boolean saveGoodsMenuItemAndLinks(GoodsCatMenuItem menuItem, List<GoodsCatMenuItemLink> menuItemLinkList) {
		boolean ok = false;

		// 保存商品分类菜单项
		Integer seqNo = goodsCatMenuItemDao.getEntityMaxSeqNo(GoodsCatMenuItem.class, "parentId", menuItem.getParentId()) + 1;
		menuItem.setSeqNo(seqNo);
		ok = goodsCatMenuItemDao.insert(menuItem) > 0;

		// 如无商品分类菜单项链接，直接返回
		if (CollectionUtils.isEmpty(menuItemLinkList)) {
			return ok;
		}

		// 保存商品分类菜单项链接
		int menuItemId = menuItem.getId();
		for (GoodsCatMenuItemLink menuItemLink : menuItemLinkList) {
			menuItemLink.setMenuItemId(menuItemId);
			Integer menuItemLinkSeqNo = goodsCatMenuItemLinkDao.getEntityMaxSeqNo(GoodsCatMenuItemLink.class, "menuItemId", menuItem.getId()) + 1;
			menuItemLink.setSeqNo(menuItemLinkSeqNo);
			ok = goodsCatMenuItemLinkDao.insert(menuItemLink) > 0;
		}

		return ok;
	}

	@Override
	public boolean batchSaveGoodsCatMenuItemCat(List<GoodsCatMenuItemCat> catList) {
		boolean ok = false;

		if (CollectionUtils.isEmpty(catList)) {
			return true;
		}

		for (GoodsCatMenuItemCat goodsCatMenuItemCat : catList) {
			if (goodsCatMenuItemCat.getId() != null) {
				ok = goodsCatMenuItemCatDao.update(goodsCatMenuItemCat) > 0;
			} else {
				ok = goodsCatMenuItemCatDao.insert(goodsCatMenuItemCat) > 0;
			}
		}

		return ok;
	}

	@Override
	public boolean deleteFirstLevelMenuItemById(Integer id) {
		boolean ok = false;

		// 删除关联的三级菜单项（对于1级导航深度删除三级菜单项）
		ok = deleteRelThirdLevelMenuItemByMenuItemId(id);

		// 删除关联的三级菜单项和二级菜单项（对于1级导航深度该步无效）
		List<GoodsCatMenuItem> secondList = getMenuItemsByPId(id);
		if (CollectionUtils.isNotEmpty(secondList)) {
			for (GoodsCatMenuItem goodsCatMenuItem : secondList) {
				ok = deleteSecondLevelMenuItemById(goodsCatMenuItem.getId());
			}
		}

		// 删除一级菜单项（goods_cat_menu_item.id = goods_cat_menu_item_link.menuItemId）
		List<GoodsCatMenuItemLink> firstLinkList = goodsCatMenuItemLinkDao.selectByMenuItemId(id);
		if (CollectionUtils.isNotEmpty(firstLinkList)) {
			ok = goodsCatMenuItemLinkDao.deleteByMenuItemId(firstLinkList.get(0).getMenuItemId()) > 0;
		}
		if (ok) {
			ok = goodsCatMenuItemDao.deleteById(id) > 0;
		}

		return ok;
	}

	@Override
	public boolean deleteSecondLevelMenuItemById(Integer menuItemId) {
		boolean ok = false;

		// 删除关联的三级菜单项
		ok = deleteRelThirdLevelMenuItemByMenuItemId(menuItemId);

		// 删除关联的二级菜单项（包括二级菜单项链接和二级菜单项名称）
		List<GoodsCatMenuItemLink> secondLinkList = goodsCatMenuItemLinkDao.selectByMenuItemId(menuItemId);
		if (CollectionUtils.isNotEmpty(secondLinkList)) {
			ok = goodsCatMenuItemLinkDao.deleteByMenuItemId(secondLinkList.get(0).getMenuItemId()) > 0;
		}
		if (ok) {
			// goods_cat_menu_item.id = goods_cat_menu_item_link.menuItemId
			ok = goodsCatMenuItemDao.deleteById(menuItemId) > 0;
		}

		return ok;
	}

	@Override
	public boolean deleteThirdLevelMenuItemById(Integer id) {
		return goodsCatMenuItemCatDao.deleteById(id) > 0;
	}

	@Override
	public boolean deleteRelThirdLevelMenuItemByMenuItemId(Integer menuItemId) {
		List<GoodsCatMenuItemCat> thirdList = goodsCatMenuItemCatDao.selectByMenuItemId(menuItemId);
		if (CollectionUtils.isNotEmpty(thirdList)) {
			return goodsCatMenuItemCatDao.deleteByMenuItemId(thirdList.get(0).getMenuItemId()) > 0;
		}

		return true;
	}

	@Override
	public PaginatedList<GoodsCatMenuItemCat> getGoodsCatMenuItemCats(PaginatedFilter paginatedFilter) {
		return goodsCatMenuItemCatDao.selectAllByMenuItemId(paginatedFilter);
	}

	@Override
	public PaginatedList<GoodsCatMenu> getGoodsCatMenus(PaginatedFilter paginatedFilter) {
		return goodsCatMenuDao.selectGoodsCatMenus(paginatedFilter);
	}

	@Override
	public boolean saveGoodsCatMenu(GoodsCatMenu goodsCatMenu) {

		return goodsCatMenuDao.insert(goodsCatMenu) > 0;
	}
	
	@Override
	public boolean updateGoodsCatMenu(GoodsCatMenu goodsCatMenu) {
		return goodsCatMenuDao.update(goodsCatMenu) > 0;
	}


	@Override
	public boolean deleteGoodsCatMenuById(Integer goodsCatMenuId) {
		// TODO 删除一级和二级 以及其他
		return goodsCatMenuDao.deleteById(goodsCatMenuId) > 0;
	}

	@Override
	public GoodsCatMenu getGoodsCatMenuByName(String name) {
		return goodsCatMenuDao.selectByName(name);
	}
	
	@Override
	public List<GoodsCatMenu> getGoodsCatMenusByDefaulted(Boolean defaulted) {
		return goodsCatMenuDao.selectByDefaulted(defaulted);
	}

	// --------------------------------颜色定义----------------------------------
	@Override
	public PaginatedList<ColorDef> getColorDefs(PaginatedFilter paginatedFilter) {
		return colorDefDao.selectColorDefs(paginatedFilter);
	}

	@Override
	public List<ColorDef> getColorDefsByFilter(List<String> names, String name) {
		return colorDefDao.selectByFilter(names, name);
	}

	@Override
	public boolean saveColorDef(ColorDef colorDef) {
		return colorDefDao.insert(colorDef) > 0;
	}

	@Override
	public boolean updateColorDef(ColorDef colorDef) {
		return colorDefDao.update(colorDef) > 0;
	}

	@Override
	public boolean deleteColorDefById(Integer id) {

		return colorDefDao.deleteById(id) > 0;
	}

	@Override
	public ColorDef getColorDefByName(String name) {
		return colorDefDao.selectByName(name);
	}

	// --------------------------------品牌定义----------------------------------

	@Override
	public PaginatedList<BrandDef> getBrandDefs(PaginatedFilter paginatedFilter) {
		PaginatedList<BrandDef> brandDefs = brandDefDao.selectBrandDefs(paginatedFilter);
		List<BrandDef> brands = brandDefs.getRows();
		for (BrandDef brandDef : brands) {
			String browseUrl = fileRepository.getFileBrowseUrl(brandDef.getLogoUsage(), brandDef.getLogoPath());
			if (StrUtil.isNullOrBlank(browseUrl)) {
				browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
			}
			brandDef.setFileBrowseUrl(browseUrl);
		}
		return brandDefs;
	}

	@Override
	public PaginatedList<BrandDef> getBrandDefsByCatId(PaginatedFilter paginatedFilter) {
		PaginatedList<BrandDef> brandDefs = PaginatedList.newOne();
		MapContext filter = paginatedFilter.getFilterItems();
		//获取全部分类相关的品牌列表
		Object catId = filter.get("catId");
		if (catId == null) {
			List<Integer> attrIds = goodsCatAttrDao.selectIdsByBrandFlag(true);
			if(attrIds != null && !attrIds.isEmpty()){
				filter.put("attrIds", attrIds);
				PaginatedList<String> pageCodes = goodsCatAttrItemDao.selectValue2ByFilter(paginatedFilter);
				List<String> codes = pageCodes.getRows();
				List<BrandDef> brandDefList = new ArrayList<BrandDef>(0);
				for(String code:codes){
					BrandDef brandDef = brandDefDao.selectByCode(code);
					brandDefList.add(brandDef);
				}
				//
				brandDefs.setRows(brandDefList);
				return brandDefs;
			}
			//所有商品分类没有属性时
			return null;
		}
		//获取某个商品分类相关的品牌列表
		Integer attrId = goodsCatAttrDao.selectIdByCatIdAndBrandFlag(Integer.valueOf(catId.toString()), true);
		List<String> codes = goodsCatAttrItemDao.selectCodesByAttrId(attrId);
		filter.put("codes", codes);
		if(codes.size() == 0) return brandDefs;
		brandDefs = brandDefDao.selectByCodes(paginatedFilter);
		List<BrandDef> brands = brandDefs.getRows();
		for (BrandDef brandDef : brands) {
			String browseUrl = fileRepository.getFileBrowseUrl(brandDef.getLogoUsage(), brandDef.getLogoPath());
			if (StrUtil.isNullOrBlank(browseUrl)) {
				browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
			}
			brandDef.setFileBrowseUrl(browseUrl);
		}
		return brandDefs;
	}

	@Override
	public boolean saveBrandDef(BrandDef brandDef) {
		if (brandDef.getSeqNo() == null) {
			brandDef.setSeqNo(1);
		}
		String py = StrUtil.chsToPy(brandDef.getName());
		brandDef.setPy(py);
		return brandDefDao.insert(brandDef) > 0;
	}

	@Override
	public boolean updateBrandDef(BrandDef brandDef) {
		if (brandDef.getSeqNo() == null) {
			brandDef.setSeqNo(1);
		}
		String py = StrUtil.chsToPy(brandDef.getName());
		brandDef.setPy(py);
		return brandDefDao.update(brandDef) > 0;
	}

	@Override
	public boolean deleteBrandDefById(Integer id) {
		return brandDefDao.deleteById(id) > 0;
	}

	@Override
	public BrandDef getBrandDefByCode(String code) {
		return brandDefDao.selectByCode(code);
	}

	// --------------------------------商品分类价格区间----------------------------------

	@Override
	public boolean saveCatPriceRange(GoodsCatPriceRange goodsCatPriceRange) {
		return goodsCatPriceRangeDao.insert(goodsCatPriceRange) > 0;
	}

	@Override
	public boolean saveCatPriceRanges(List<GoodsCatPriceRange> goodsCatPriceRanges) {
		boolean result = true;
		for (GoodsCatPriceRange goodsCatPriceRange : goodsCatPriceRanges) {
			result = result && saveCatPriceRange(goodsCatPriceRange);
		}
		return result;
	}

	@Override
	public boolean updateCatPriceRange(GoodsCatPriceRange goodsCatPriceRange) {
		return goodsCatPriceRangeDao.update(goodsCatPriceRange) > 0;
	}

	@Override
	public boolean updateCatPriceRanges(List<GoodsCatPriceRange> goodsCatPriceRanges) {
		boolean result = true;
		if (goodsCatPriceRanges.size() > 0) {
			List<Integer> ids = new ArrayList<Integer>(0);
			for (GoodsCatPriceRange goodsCatPriceRange : goodsCatPriceRanges) {
				Integer id = goodsCatPriceRange.getId();
				if (id != null && id > 0) {
					result = result && updateCatPriceRange(goodsCatPriceRange);
				} else {
					result = result && saveCatPriceRange(goodsCatPriceRange);
				}
				ids.add(goodsCatPriceRange.getId());
			}
			deleteCatPriceRangeByCatIdAndNotInIds(goodsCatPriceRanges.get(0).getCatId(), ids);
		}
		return result;
	}

	public boolean deleteCatPriceRangeByCatIdAndNotInIds(Integer catId, List<Integer> ids) {
		return goodsCatPriceRangeDao.deleteCatPriceRangeByCatIdAndNotInIds(catId, ids) > 0;
	}

	@Override
	public List<GoodsCatPriceRange> getCatPriceRangesByCatId(Integer catId) {
		return goodsCatPriceRangeDao.selectByCatId(catId);
	}

	@Override
	public boolean getInUseBrandDefById(Integer defId) {
		boolean inUse = false;
		List<Integer> goodsCatAttrIds = goodsCatAttrDao.selectIdsByBrandFlag(true);
		//
		List<String> itemValues = new ArrayList<String>(0);
		for(Integer attrId:goodsCatAttrIds){
			List<GoodsCatAttrItem> goodsCatAttrItems = goodsCatAttrItemDao.selectByAttrId(attrId);
			for(GoodsCatAttrItem item:goodsCatAttrItems){
				itemValues.add(item.getValue());
			}
		}
		//
		BrandDef brandDef = brandDefDao.selectById(defId);
		String name = brandDef.getName();
		//检查是否存在
		for(String itemValue : itemValues){
			inUse = itemValue.equals(name);
			if(inUse) break;
		}
		return inUse;
	}

	@Override
	public boolean getInUseSpecRefById(Integer specRefId) {
		boolean inUse = false;
		List<Integer> goodsCatSpecIds = goodsCatSpecDao.selectIdsByRefId(specRefId);
		if(goodsCatSpecIds != null && !goodsCatSpecIds.isEmpty()){
			inUse = true;
		}
		return inUse;
	}

	@Override
	public boolean getInUseAttrRefById(Integer attrRefId) {
		boolean inUse = false;
		List<Integer> goodsCatAttrs = goodsCatAttrDao.selectIdsByRefId(attrRefId);
		if(goodsCatAttrs != null && !goodsCatAttrs.isEmpty()){
			inUse = true;
		}
		return inUse;
	}

	@Override
	public boolean getInUseColorDefById(Integer colorDefId) {
		boolean inUse = false;
		List<Integer> goodsCatSpecIds = goodsCatSpecDao.selectIdsByColorFlag(true);
		//
		List<String> itemValues = new ArrayList<String>(0);
		for(Integer specId:goodsCatSpecIds){
			List<GoodsCatSpecItem> goodsCatSpecItems = goodsCatSpecItemDao.selectBySpecId(specId);
			for(GoodsCatSpecItem item:goodsCatSpecItems){
				//缓存颜色表达式的值
				itemValues.add(item.getValue2());
			}
		}
		//
		ColorDef colorDef = colorDefDao.selectById(colorDefId);
		String expr = colorDef.getExpr();
		//检查是否存在
		for(String itemValue : itemValues){
			inUse = itemValue.equals(expr);
			if(inUse) break;
		}
		return inUse;
	}

}
