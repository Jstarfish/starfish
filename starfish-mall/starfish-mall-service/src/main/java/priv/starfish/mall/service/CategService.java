package priv.starfish.mall.service;

import java.util.List;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.categ.entity.AttrRef;
import priv.starfish.mall.categ.entity.BrandDef;
import priv.starfish.mall.categ.entity.ColorDef;
import priv.starfish.mall.categ.entity.GoodsCat;
import priv.starfish.mall.categ.entity.GoodsCatAttr;
import priv.starfish.mall.categ.entity.GoodsCatAttrGroup;
import priv.starfish.mall.categ.entity.GoodsCatMenu;
import priv.starfish.mall.categ.entity.GoodsCatMenuItem;
import priv.starfish.mall.categ.entity.GoodsCatMenuItemCat;
import priv.starfish.mall.categ.entity.GoodsCatMenuItemLink;
import priv.starfish.mall.categ.entity.GoodsCatPriceRange;
import priv.starfish.mall.categ.entity.GoodsCatSpec;
import priv.starfish.mall.categ.entity.SpecRef;

public interface CategService extends BaseService {

	// ---------------------------------规格参照-------------------------------------
	SpecRef getSpecRefById(Integer id);

	SpecRef getSpecRefByCode(String code);

	/**
	 * 分页查询规格参照
	 * 
	 * @author 王少辉
	 * @date 2015年5月27日 下午6:45:13
	 * 
	 * @param paginatedFilter
	 *            分页参数
	 * @return 返回规格信息
	 */
	PaginatedList<SpecRef> getSpecRefs(PaginatedFilter paginatedFilter);

	boolean saveSpecRef(SpecRef specRef);

	boolean updateSpecRef(SpecRef specRef);

	/**
	 * 
	 * @author 王少辉
	 * @date 2015年6月12日 下午6:26:21
	 * 
	 * @param id
	 * @return
	 */
	boolean deleteSpecRefById(Integer id);

	/**
	 * 根据id批量删除规格参照
	 * 
	 * @author 王少辉
	 * @date 2015年5月29日 上午8:57:35
	 * 
	 * @param ids
	 *            规格id
	 * @return 返回删除结果
	 */
	boolean deleteSpecRefsByIds(List<Integer> ids);

	// -------------------------------商品分类-规格-----------------------------------
	/**
	 * 通过Id获取商品分类规格关联
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午5:36:44
	 * 
	 * @param id
	 *            商品分类-规格Id
	 * @return
	 */
	GoodsCatSpec getCatSpecById(Integer id);

	/**
	 * 通过分类Id和规格Id获取商品分类规格关联
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午5:39:28
	 * 
	 * @param catId
	 *            分类Id
	 * @param specRefId
	 *            规格Id
	 * @return
	 */
	GoodsCatSpec getCatSpecByCatIdAndRefId(Integer catId, Integer specRefId);

	/**
	 * 保存商品分类规格关联
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午5:41:56
	 * 
	 * @param goodsCatSpec
	 *            商品分类规格关联
	 * @return
	 */
	boolean saveCatSpec(GoodsCatSpec goodsCatSpec);

	/**
	 * 修改商品分类规格关联
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午5:43:18
	 * 
	 * @param goodsCatSpec
	 *            商品分类规格关联
	 * @return
	 */
	boolean updateCatSpec(GoodsCatSpec goodsCatSpec);

	/**
	 * 批量修改商品分类规格关联
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午5:46:18
	 * 
	 * @param goodsCatSpecs
	 *            商品分类规格关联List
	 * @return
	 */
	boolean updateGoodsCatSpecs(List<GoodsCatSpec> goodsCatSpecs);

	/**
	 * 通过Id删除商品分类规格关联
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午5:47:19
	 * 
	 * @param id
	 *            商品分类规格关联Id
	 * @return
	 */
	boolean deleteCatSpecById(Integer id);

	/**
	 * 通过分类Id获取商品分类规格关联数量
	 * 
	 * @author 廖晓远
	 * @date 2015-5-29 上午11:36:56
	 * 
	 * @param catId
	 *            分类Id
	 * @return
	 */
	int countCatSpecByCatId(Integer catId);

	/**
	 * 通过分类Id获取商品分类规格关联
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午5:00:02
	 * 
	 * @param catId
	 * @return
	 */
	List<GoodsCatSpec> getCatSpecsByCatId(Integer catId);

	/**
	 * 通过分类Id和商品Id获取商品分类规格关联
	 * 
	 * @author zjl
	 * @date 2015年7月24日 上午11:30:42
	 * 
	 * @param catId
	 *            分类Id
	 * @param goodsId
	 *            商品Id
	 * @return List<GoodsCatSpec>
	 */
	List<GoodsCatSpec> getGoodCatSpecsByCatId(Integer catId, Integer goodsId);

	/**
	 * 批量删除商品分类规格关联
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午5:49:14
	 * 
	 * @param ids
	 *            商品分类规格关联Id列表
	 * @return
	 */
	boolean deleteCatSpecByIds(List<Integer> ids);

	/**
	 * 通过商品分类Id获取商品分类规格关联的Id列表
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午5:51:18
	 * 
	 * @param catId
	 *            商品分类Id
	 * @return
	 */
	List<Integer> getCatSpecIdsByCatId(Integer catId);

	/**
	 * 通过商品分类Id列表获取商品分类规格关联的Id列表
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午5:52:09
	 * 
	 * @param catIds
	 *            商品分类Id列表
	 * @return
	 */
	List<Integer> getCatSpecIdsByCatIds(List<Integer> catIds);

	/**
	 * 批量添加分类规格关联
	 * 
	 * @author 廖晓远
	 * @date 2015-6-3 下午8:03:53
	 * 
	 * @param goodsCatSpecs
	 * @return
	 */
	boolean saveCatSpecs(List<GoodsCatSpec> goodsCatSpecs);

	// ------------------------------商品分类_属性分组---------------------------------
	/**
	 * 通过Id获取商品分类属性分组
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午5:55:35
	 * 
	 * @param id
	 *            分组Id
	 * @return
	 */
	GoodsCatAttrGroup getCatAttrGroupById(Integer id);

	/**
	 * 通过分类Id和分组名称获取商品分类属性分组
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午5:58:23
	 * 
	 * @param catId
	 *            分类Id
	 * @param name
	 *            分组名称
	 * @return
	 */
	GoodsCatAttrGroup getCatAttrGroupByCatIdAndName(Integer catId, String name);

	/**
	 * 保存商品分类属性分组
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午5:59:06
	 * 
	 * @param goodsCatAttrGroup
	 *            商品分类属性分组
	 * @return
	 */
	boolean saveCatAttrGroup(GoodsCatAttrGroup goodsCatAttrGroup);

	/**
	 * 批量保存商品分类属性分组
	 * 
	 * @author 廖晓远
	 * @date 2015-6-6 下午5:40:45
	 * 
	 * @param goodsCatAttrGroups
	 *            商品分类属性分组列表
	 * @return
	 */
	boolean saveCatAttrGroups(List<GoodsCatAttrGroup> goodsCatAttrGroups);

	/**
	 * 修改商品分类属性分组
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午6:00:20
	 * 
	 * @param goodsCatAttrGroup
	 *            商品分类属性分组
	 * @return
	 */
	boolean updateCatAttrGroup(GoodsCatAttrGroup goodsCatAttrGroup);

	/**
	 * 批量修改商品分类属性分组
	 * 
	 * @author 廖晓远
	 * @date 2015-6-8 下午7:25:26
	 * 
	 * @param goodsCatAttrGroups
	 *            商品分类属性分组列表
	 * @return
	 */
	boolean updateCatAttrGroups(List<GoodsCatAttrGroup> goodsCatAttrGroups);

	/**
	 * 通过Id删除商品分类属性分组
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午6:02:14
	 * 
	 * @param id
	 *            分组Id
	 * @return
	 */
	boolean deleteCatAttrGroupById(Integer id);

	/**
	 * 批量删除 条件为分类id和不在ids中的 多余分组
	 * 
	 * @author 廖晓远
	 * @date 2015-6-8 下午7:31:04
	 * 
	 * @param ids
	 *            商品分类-属性分组id集
	 * @param catId
	 *            商品分类id
	 * @return
	 */
	boolean deleteCatAttrGroupByCatIdAndNotInIds(Integer catId, List<Integer> ids);

	/**
	 * 通过分类Id获取商品分类_属性分组数量
	 * 
	 * @author 廖晓远
	 * @date 2015-5-29 上午11:36:56
	 * 
	 * @param catId
	 *            分类ID
	 * @return
	 */
	int countCatAttrGroupByCatId(Integer catId);

	/**
	 * 通过商品分类Id获取分组
	 * 
	 * @author 廖晓远
	 * @date 2015-6-6 下午6:54:55
	 * 
	 * @param catId
	 *            分类Id
	 * @return
	 */
	List<GoodsCatAttrGroup> getCatAttrGroupByCatId(Integer catId);

	// ------------------------------商品分类---------------------------------

	/**
	 * 保存商品分类
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午6:05:42
	 * 
	 * @param goodsCat
	 *            商品分类
	 * @return
	 */
	boolean saveGoodsCat(GoodsCat goodsCat);

	/**
	 * 通过Id获取商品分类
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午6:06:33
	 * 
	 * @param id
	 *            商品分类Id
	 * @return
	 */
	GoodsCat getGoodsCatById(Integer id);

	/**
	 * 修改商品分类
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午6:07:42
	 * 
	 * @param goodsCat
	 *            商品分类
	 * @return
	 */
	boolean updateGoodsCat(GoodsCat goodsCat);

	/**
	 * 通过Id删除商品分类
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午6:08:05
	 * 
	 * @param id
	 *            商品分类Id
	 * @return
	 */
	boolean deleteGoodsCatById(Integer id);

	/**
	 * 通过Id列表批量删除分类
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午4:41:54
	 * 
	 * @param ids
	 *            商品分类Id列表
	 * @return
	 */
	boolean deleteGoodsCatByIds(List<Integer> ids);

	/**
	 * 通过父分类Id获取一级子分类
	 * 
	 * @author 廖晓远
	 * @date 2015-5-27 下午3:39:05
	 * 
	 * @param parentId
	 *            父分类Id
	 * @return
	 */
	List<GoodsCat> getGoodsCatsByParentId(Integer parentId);

	/**
	 * 通过父分类Id获取所有子分类
	 * 
	 * @author 廖晓远
	 * @date 2015年7月15日 下午6:47:24
	 * 
	 * @param parentId
	 * @return
	 */
	List<GoodsCat> getAllGoodsCatsByParentId(Integer parentId);

	/**
	 * 根据过滤条件获取所有商品分类
	 * 
	 * @author 廖晓远
	 * @date 2015年6月12日 下午3:54:01
	 * 
	 * @param name
	 *            模糊查询分类名称 为空时相当于无过滤条件查询
	 * @return List<GoodsCat>
	 */
	List<GoodsCat> getGoodsCatsByName(String name);

	/**
	 * 通过级别获取分类
	 * 
	 * @author 廖晓远
	 * @date 2015年7月15日 上午10:48:25
	 * 
	 * @param level
	 * @return
	 */
	List<GoodsCat> getGoodsCatsByLevel(Integer level);

	/**
	 * 根据商品分类Id 获取和此分类同级的商品分类
	 * 
	 * @author zjl
	 * @date 2015年7月24日 下午4:19:13
	 * 
	 * @param id
	 *            商品分类Id
	 * @return List<GoodsCat>
	 */
	List<GoodsCat> getSiblingGoodsCatById(Integer id);

	// ------------------------------属性参照--------------------------------------

	/**
	 * 添加属性参照
	 * 
	 * @author 毛智东
	 * @date 2015年5月28日 上午10:18:07
	 * 
	 * @param attrRef
	 * @return
	 */
	boolean saveAttrRef(AttrRef attrRef);

	/**
	 * 根据id删除属性参照
	 * 
	 * @author 毛智东
	 * @date 2015年5月28日 上午10:18:44
	 * 
	 * @param id
	 * @return
	 */
	boolean deleteAttrRefById(Integer id);

	/**
	 * 修改属性参照
	 * 
	 * @author 毛智东
	 * @date 2015年5月28日 上午10:19:06
	 * 
	 * @param attrRef
	 * @return
	 */
	boolean updateAttrRef(AttrRef attrRef);

	/**
	 * 批量删除属性参照
	 * 
	 * @author 毛智东
	 * @date 2015年5月28日 上午10:19:20
	 * 
	 * @param ids
	 * @return
	 */
	boolean deleteAttrRefByIds(List<Integer> ids);

	/**
	 * 分页获取属性参照列表
	 * 
	 * @author 毛智东
	 * @date 2015年5月28日 上午10:19:54
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<AttrRef> getAttrRefsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据名字得到属性参照
	 * 
	 * @author 毛智东
	 * @date 2015年6月12日 下午6:38:43
	 * 
	 * @param name
	 * @return
	 */
	AttrRef getAttrRefByName(String name);

	/**
	 * 查询是品牌属性的属性参照
	 * 
	 * @author 毛智东
	 * @date 2015年6月24日 下午4:16:41
	 * 
	 * @return
	 */
	boolean getAttrRefByBrandFlagIsTrue();

	// ------------------------------商品分类属性关联--------------------------------------
	/**
	 * 添加商品分类属性关联
	 * 
	 * @author 廖晓远
	 * @date 2015-6-1 上午11:27:34
	 * 
	 * @param goodsCatAttr
	 *            商品分类属性关联
	 * @return
	 */
	boolean saveGoodsCatAttr(GoodsCatAttr goodsCatAttr);

	/**
	 * 批量添加商品分类属性关联
	 * 
	 * @author 廖晓远
	 * @date 2015-6-1 上午11:29:40
	 * 
	 * @param goodsCatAttrs
	 *            商品分类属性关联列表
	 * @return
	 */
	boolean saveGoodsCatAttrs(List<GoodsCatAttr> goodsCatAttrs);

	/**
	 * 修改商品分类属性关联
	 * 
	 * @author 廖晓远
	 * @date 2015-6-4 下午7:41:27
	 * 
	 * @param goodsCatAttr
	 * @return
	 */
	boolean updateGoodsCatAttr(GoodsCatAttr goodsCatAttr);

	/**
	 * 批量修改商品分类属性关联
	 * 
	 * @author 廖晓远
	 * @date 2015-6-4 下午7:37:41
	 * 
	 * @param goodsCatAttrs
	 *            商品分类属性关联列表
	 * @return
	 */
	boolean updateGoodsCatAttrs(List<GoodsCatAttr> goodsCatAttrs);

	/**
	 * 通过分类Id获取商品分类属性关联数量
	 * 
	 * @author 廖晓远
	 * @date 2015-5-29 上午11:36:56
	 * 
	 * @param catId
	 *            分类Id
	 * @return
	 */
	int countCatAttrByCatId(Integer catId);
	
	/**
	 * 通过分类Id获取商品分类属性关联
	 * 
	 * @author 廖晓远
	 * @date 2015-5-29 下午5:33:43
	 * 
	 * @param catId
	 *            分类Id
	 * @return
	 */
	List<GoodsCatAttr> getCatAttrsByCatId(Integer catId);

	/**
	 * 通过分组groupId和商品Id查询分类下属性
	 * 
	 * @author zjl
	 * @date 2015年7月29日 下午4:55:14
	 * 
	 * @param groupId
	 *            分组Id
	 * @param goodsId
	 *            goodsId 商品Id
	 * @return List<GoodsCatAttr>
	 */
	List<GoodsCatAttr> getCatAttrsByGroupIdAndGoodsId(Integer groupId, Integer goodsId);

	/**
	 * 批量删除商品分类属性关联
	 * 
	 * @author 廖晓远
	 * @date 2015-5-30 下午5:06:36
	 * 
	 * @param ids
	 *            商品分类属性关联Id列表
	 * @return
	 */
	boolean deleteGoodsCatAttrByIds(List<Integer> ids);

	/**
	 * 通过分类ID获取商品分类属性关联ID
	 * 
	 * @author 廖晓远
	 * @date 2015-6-3 上午10:11:28
	 * 
	 * @param catId
	 *            分类Id
	 * @return
	 */
	List<Integer> getCatAttrIdsByCatId(Integer catId);

	/**
	 * 通过分类ID列表获取商品分类属性关联ID
	 * 
	 * @author 廖晓远
	 * @date 2015-5-30 下午5:06:36
	 * 
	 * @param catIds
	 *            分类Id列表
	 * @return
	 */
	List<Integer> getCatAttrIdsByCatIds(List<Integer> catIds);

	// ------------------------------商品分类菜单------------------------------------
	/**
	 * 根据id查询商品分类菜单
	 * 
	 * @author 王少辉
	 * @date 2015年6月12日 上午11:57:14
	 * 
	 * @param id
	 *            菜单id
	 * @return 返回商品分类菜单
	 */
	GoodsCatMenu getGoodsCatMenuById(Integer id);

	/**
	 * 根据名字查询商品分类菜单
	 * 
	 * @author 毛智东
	 * @date 2015年7月16日 上午10:28:43
	 * 
	 * @param name
	 * @return
	 */
	GoodsCatMenu getGoodsCatMenuByName(String name);
	
	

	/**
	 * 根据菜单id、级别、名称获取菜单项
	 * 
	 * @author 王少辉
	 * @date 2015年6月12日 下午3:50:37
	 * 
	 * @param menuId
	 *            菜单id
	 * @param level
	 *            级别
	 * @param name
	 *            名称
	 * @return 返回菜单项
	 */
	GoodsCatMenuItem getGoodsCatMenuItemByMenuIdAndLevelAndName(Integer menuId, int level, String name);

	/**
	 * 根据菜单项id和名称获取菜单链接
	 * 
	 * @author 王少辉
	 * @date 2015年6月12日 下午3:41:11
	 * 
	 * @param menuItemId
	 *            菜单项id
	 * @param name
	 *            名称
	 * @return 返回菜单项链接
	 */
	GoodsCatMenuItemLink getGoodsCatMenuItemLinkByMenuItemIdAndName(Integer menuItemId, String name);

	/**
	 * 
	 * @author 王少辉
	 * @date 2015年6月18日 上午11:54:05
	 * 
	 * @param menuItem
	 * @return
	 */
	List<GoodsCatMenuItem> getMenuItemsByMenuIdAndLevel(Integer menuId, Integer level);
	
	/**
	 * 
	 * @author 郝江奎
	 * @date 2015年9月21日 上午10:58:03
	 * 
	 * @param menuId name level
	 * @param level
	 * @return
	 */
	
	GoodsCatMenuItem getMenuItemsByMenuIdAndLevelAndName(Integer menuId, Integer level, String name);

	List<GoodsCatMenuItemLink> getMenuItemLinksByMenuItemId(Integer menuItemId);

	/**
	 * 根据商品分类一级菜单获取二级菜单列表
	 * 
	 * @author 王少辉
	 * @date 2015年6月11日 下午9:30:37
	 * 
	 * @param parentId
	 *            父id
	 * @return 返回二级菜单列表
	 */
	List<GoodsCatMenuItem> getMenuItemsByPId(Integer parentId);

	/**
	 * 添加商品分类菜单一级菜单项属性
	 * 
	 * @author 王少辉
	 * @date 2015年6月9日 下午8:43:19
	 * 
	 * @param menuItem
	 *            菜单项
	 * @param menuItemLinkList
	 *            菜单项链接列表
	 * @return 返回添加结果
	 */
	boolean saveGoodsMenuItemAndLinks(GoodsCatMenuItem menuItem, List<GoodsCatMenuItemLink> menuItemLinkList);

	boolean batchSaveGoodsCatMenuItemCat(List<GoodsCatMenuItemCat> catList);

	/**
	 * 根据一级菜单项id关联删除一级菜单项
	 * 
	 * @author 王少辉
	 * @date 2015年6月11日 下午8:32:04
	 * 
	 * @param id
	 *            一级菜单项id
	 * @return 返回删除结果
	 */
	boolean deleteFirstLevelMenuItemById(Integer id);

	/**
	 * 根据二级菜单项id关联删除二级菜单项
	 * 
	 * @author 王少辉
	 * @date 2015年8月17日 上午11:58:06
	 * 
	 * @param id
	 *            二级菜单项id
	 * @return 返回删除结果
	 */
	boolean deleteSecondLevelMenuItemById(Integer id);

	/**
	 * 根据三级菜单项id删除三级菜单项
	 * 
	 * @author 王少辉
	 * @date 2015年8月17日 上午11:58:10
	 * 
	 * @param id
	 *            三级菜单项id
	 * @return 返回删除结果
	 */
	public boolean deleteThirdLevelMenuItemById(Integer id);

	/**
	 * 根据二级菜单项id关联删除三级菜单项
	 * 
	 * @author 王少辉
	 * @date 2015年8月17日 上午11:58:13
	 * 
	 * @param menuItemId
	 *            二级菜单项id
	 * @return 返回删除结果
	 */
	public boolean deleteRelThirdLevelMenuItemByMenuItemId(Integer menuItemId);

	/**
	 * 分页查询商品分类菜单
	 * 
	 * @author zjl
	 * @date 2015年6月16日 下午4:05:16
	 * 
	 * @param paginatedFilter分页条件及过滤信息
	 * @return PaginatedList<GoodsCatMenu>
	 */
	PaginatedList<GoodsCatMenu> getGoodsCatMenus(PaginatedFilter paginatedFilter);

	/**
	 * 添加商品分类菜单
	 * 
	 * @author zjl
	 * @date 2015年6月18日 下午6:10:17
	 * 
	 * @param menu
	 * @return
	 */
	boolean saveGoodsCatMenu(GoodsCatMenu menu);
	
	/**
	 * 修改商品分类菜单
	 * 
	 * @author 郝江奎
	 * @date 2015年9月19日 下午5:10:17
	 * 
	 * @param menu
	 * @return
	 */
	boolean updateGoodsCatMenu(GoodsCatMenu goodsCatMenu);

	/**
	 * 通过菜单项Id分页查询菜单项链接
	 * 
	 * @author 廖晓远
	 * @date 2015年6月18日 上午11:06:47
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<GoodsCatMenuItemCat> getGoodsCatMenuItemCats(PaginatedFilter paginatedFilter);

	/**
	 * 根据Id 删除商品分类
	 * 
	 * @author zjl
	 * @date 2015年6月18日 下午7:28:20
	 * 
	 * @param goodsCatMenuId
	 *            商品分类ID
	 * @return
	 */
	boolean deleteGoodsCatMenuById(Integer goodsCatMenuId);

	// -------------------------------颜色定义-----------------------------------

	/**
	 * 分页获取颜色信息
	 * 
	 * @author zjl
	 * @date 2015年6月23日 下午3:49:44
	 * 
	 * @param paginatedFilter
	 * @return PaginatedList<ColorDef>
	 */
	PaginatedList<ColorDef> getColorDefs(PaginatedFilter paginatedFilter);

	/**
	 * 获取所有颜色信息
	 * 
	 * @author 廖晓远
	 * @date 2015年6月25日 下午6:44:35
	 * 
	 * @param Filters
	 *            : excludeNames:需要排除的名字，queryName:查询的名字
	 * @return
	 */
	List<ColorDef> getColorDefsByFilter(List<String> excludeNames, String queryName);

	/**
	 * 保存颜色信息
	 * 
	 * @author zjl
	 * @date 2015年6月23日 下午7:04:18
	 * 
	 * @param colorDef
	 * @return
	 */
	boolean saveColorDef(ColorDef colorDef);

	/**
	 * 更新颜色信息
	 * 
	 * @author zjl
	 * @date 2015年6月23日 下午7:04:18
	 * 
	 * @param colorDef
	 * @return
	 */
	boolean updateColorDef(ColorDef colorDef);

	/**
	 * 删除颜色信息
	 * 
	 * @author zjl
	 * @date 2015年6月24日 上午11:56:17
	 * 
	 * @param id主键
	 * @return
	 */
	boolean deleteColorDefById(Integer id);

	/**
	 * 根据名称查询颜色定义
	 * 
	 * @author zjl
	 * @date 2015年7月20日 下午12:03:33
	 * 
	 * @param 颜色定义名称
	 * @return
	 */
	ColorDef getColorDefByName(String name);

	// ------------------------------品牌定义------------------------------------
	/**
	 * 分页获取品牌
	 * 
	 * @author 廖晓远
	 * @date 2015年6月23日 下午4:34:57
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<BrandDef> getBrandDefs(PaginatedFilter paginatedFilter);

	/**
	 * 通过商品分类Id获取品牌
	 * 
	 * @author 廖晓远
	 * @date 2015年7月20日 下午5:25:25
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<BrandDef> getBrandDefsByCatId(PaginatedFilter paginatedFilter);

	/**
	 * 创建品牌
	 * 
	 * @author 廖晓远
	 * @date 2015年6月23日 下午4:43:36
	 * 
	 * @param brandDef
	 * @return
	 */
	boolean saveBrandDef(BrandDef brandDef);

	/**
	 * 修改品牌
	 * 
	 * @author 廖晓远
	 * @date 2015年6月23日 下午4:47:07
	 *
	 * @param brandDef
	 * @return
	 */
	boolean updateBrandDef(BrandDef brandDef);

	/**
	 * 删除品牌
	 * 
	 * @author 廖晓远
	 * @date 2015年6月23日 下午4:51:03
	 * 
	 * @param id
	 *            主键
	 * @return
	 */
	boolean deleteBrandDefById(Integer id);

	/**
	 * 通过Code获取品牌
	 * 
	 * @author 廖晓远
	 * @date 2015年6月25日 上午10:14:03
	 * 
	 * @param code
	 * @return
	 */
	BrandDef getBrandDefByCode(String code);

	// --------------------------------商品分类价格区间----------------------------------

	/**
	 * 通过分类Id查询商品分类价格区间
	 * 
	 * @author 廖晓远
	 * @date 2015年7月27日 下午4:18:12
	 * 
	 * @param catId
	 * @return
	 */
	List<GoodsCatPriceRange> getCatPriceRangesByCatId(Integer catId);

	/**
	 * 保存商品分类价格区间
	 * 
	 * @author 廖晓远
	 * @date 2015年7月27日 上午11:51:36
	 * 
	 * @param goodsCatPriceRange
	 * @return
	 */
	boolean saveCatPriceRange(GoodsCatPriceRange goodsCatPriceRange);

	/**
	 * 批量保存商品分类价格区间
	 * 
	 * @author 廖晓远
	 * @date 2015年7月27日 上午11:51:55
	 * 
	 * @param goodsCatPriceRanges
	 * @return
	 */
	boolean saveCatPriceRanges(List<GoodsCatPriceRange> goodsCatPriceRanges);

	/**
	 * 修改商品分类价格区间
	 * 
	 * @author 廖晓远
	 * @date 2015年7月27日 下午12:04:43
	 * 
	 * @param goodsCatPriceRange
	 * @return
	 */
	boolean updateCatPriceRange(GoodsCatPriceRange goodsCatPriceRange);

	/**
	 * 批量修改商品分类价格区间
	 * 
	 * @author 廖晓远
	 * @date 2015年7月27日 下午12:05:25
	 * 
	 * @param goodsCatPriceRanges
	 * @return
	 */
	boolean updateCatPriceRanges(List<GoodsCatPriceRange> goodsCatPriceRanges);

	
	
	/**
	 * 批量修改商品分类价格区间
	 * 
	 * @author 郝江奎
	 * @date 2015年7月27日 下午12:05:25
	 * 
	 * @param goodsCatPriceRanges
	 * @return
	 */
	boolean deleteCatAttrById(Integer attrId);

	List<GoodsCatMenu> getGoodsCatMenusByDefaulted(Boolean defaulted);

	/**
	 * 检查品牌是否被使用中
	 * 
	 * @author guoyn
	 * @date 2015年11月23日 下午7:29:50
	 * 
	 * @param defId
	 * @return boolean
	 */
	boolean getInUseBrandDefById(Integer defId);

	/**
	 * 检查规格是否被分类使用中
	 * 
	 * @author guoyn
	 * @date 2015年11月27日 下午4:59:07
	 * 
	 * @param specRefId
	 * @return boolean
	 */
	boolean getInUseSpecRefById(Integer specRefId);

	/**
	 * 检查属性是否被分类使用中
	 * 
	 * @author guoyn
	 * @date 2015年11月27日 下午5:06:12
	 * 
	 * @param attrRefId
	 * @return boolean
	 */
	boolean getInUseAttrRefById(Integer attrRefId);

	/**
	 * 检查颜色参照是否正在被分类使用中
	 * 
	 * @author guoyn
	 * @date 2015年11月27日 下午5:18:12
	 * 
	 * @param colorDefId
	 * @return boolean
	 */
	boolean getInUseColorDefById(Integer colorDefId);

}
