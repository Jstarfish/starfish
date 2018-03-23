package priv.starfish.mall.dao.shop;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.shop.entity.ShopGrade;

@IBatisSqlTarget
public interface ShopGradeDao extends BaseDao<ShopGrade, Integer> {

	/**
	 * 根据ID查询商铺评分等级
	 */
	ShopGrade selectById(Integer id);

	/**
	 * 根据级别和档位查询商铺评分等级
	 * 
	 * @author zjl
	 * @date 2015年5月20日 下午5:03:14
	 * 
	 * @param grade
	 *            级别
	 * @param level
	 *            档位
	 * @return ShopGrade
	 */
	ShopGrade selectByGradeAndLevel(Integer grade, Integer level);

	/**
	 * 添加商铺评分等级
	 */
	int insert(ShopGrade shopGrade);

	/**
	 * 更新商铺评分等级
	 */
	int update(ShopGrade shopGrade);

	/**
	 * 根据ID 删除商铺评分等级
	 */
	int deleteById(Integer id);

	/**
	 * 分页查询商铺评分等级
	 * 
	 * @author zjl
	 * @date 2015年5月20日 下午5:01:43
	 * 
	 * @param paginatedFilter
	 * @return PaginatedList<ShopGrade>
	 */
	PaginatedList<ShopGrade> selectShopGrades(PaginatedFilter paginatedFilter);

	
	/**
	 * 查询最小分数与最大分数之间存不存在其他等级
	 * @author zjl
	 * @date 2015年7月20日 下午4:21:04
	 * 
	 * @param lowerPoint
	 * @param upperPoint
	 * @return
	 */
	List<ShopGrade> selectShopGradesByLowerPointAndUpperPoint(Integer lowerPoint, Integer upperPoint);
	/**
	 * 查询分数在那个店铺等级中
	 * @author zjl
	 * @date 2015年7月20日 下午5:21:24
	 * 
	 * @param point
	 * @return
	 */
	List<ShopGrade> selectShopGradesByPoint(Integer point);
	/**
	 * 查询店铺等级
	 * @author zjl
	 * @date 2015年8月5日 下午5:10:02
	 * 
	 * @return List<ShopGrade>
	 */
	List<ShopGrade> selectShopGrades();
}
