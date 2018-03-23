package priv.starfish.mall.dao.categ;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.categ.entity.GoodsCatMenu;

@IBatisSqlTarget
public interface GoodsCatMenuDao extends BaseDao<GoodsCatMenu, Integer> {

	GoodsCatMenu selectById(Integer id);

	GoodsCatMenu selectByName(String name);

	/**
	 * 获取商品分类一级菜单列表
	 * 
	 * @author 王少辉
	 * @date 2015年6月6日 下午4:46:27
	 * 
	 * @return 返回商品分类一级菜单列表
	 */
	List<GoodsCatMenu> selectFirstLevel();

	/**
	 * 根据商品分类一级菜单获取二级菜单列表
	 * 
	 * @author 王少辉
	 * @date 2015年6月11日 下午9:31:42
	 * 
	 * @param parentId
	 *            父id
	 * @return 返回二级菜单列表
	 */
	List<GoodsCatMenu> selectSecondLevelByPId(Integer parentId);

	int insert(GoodsCatMenu goodsCatMenu);

	int update(GoodsCatMenu goodsCatMenu);

	int deleteById(Integer id);

	/**
	 * 分页查询商品分类菜单
	 * 
	 * @author zjl
	 * @date 2015年6月16日 下午4:05:16
	 * 
	 * @param paginatedFilter分页条件及过滤信息
	 * @return PaginatedList<GoodsCatMenu>
	 */
	PaginatedList<GoodsCatMenu> selectGoodsCatMenus(PaginatedFilter paginatedFilter);

	List<GoodsCatMenu> selectByDefaulted(Boolean defaulted);
}
