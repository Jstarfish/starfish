package priv.starfish.mall.dao.categ;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.categ.entity.GoodsCatSpecItem;

@IBatisSqlTarget
public interface GoodsCatSpecItemDao extends BaseDao<GoodsCatSpecItem, Integer> {
	GoodsCatSpecItem selectById(Integer id);

	GoodsCatSpecItem selectBySpecIdAndValue(Integer specId, String value);

	int insert(GoodsCatSpecItem goodsCatSpecItem);

	int update(GoodsCatSpecItem goodsCatSpecItem);

	int deleteById(Integer id);

	/**
	 * 通过规格关联Id删除
	 * 
	 * @author 廖晓远
	 * @date 2015-6-3 上午9:55:19
	 * 
	 * @param specId
	 * @return
	 */
	int deleteBySpecId(Integer specId);

	/**
	 * 通过规格关联Id列表删除
	 * 
	 * @author 廖晓远
	 * @date 2015-6-3 上午9:55:22
	 * 
	 * @param specIds
	 * @return
	 */
	int deleteBySpecIds(List<Integer> specIds);

	/**
	 * 通过分类规格关联Id查找枚举值
	 * 
	 * @author 廖晓远
	 * @date 2015-6-3 下午8:30:30
	 * 
	 * @param specId
	 * @return
	 */
	List<GoodsCatSpecItem> selectBySpecId(Integer specId);

	/**
	 * 通过分类规格关联Id查找枚举值
	 * 
	 * @author 廖晓远
	 * @date 2015-6-5 下午3:59:51
	 * 
	 * @param specId
	 * @param uncontainIds
	 *            不包含的Id，实现增量
	 * @return
	 */
	List<GoodsCatSpecItem> selectBySpecId(Integer specId, List<Integer> uncontainIds);

	/**
	 * 通过属性关联Id和需要排除的Id删除
	 * 
	 * @author 廖晓远
	 * @date 2015年6月24日 下午7:56:18
	 * 
	 * @param specId
	 *            规格关联ID
	 * @param uncontainIds
	 *            需要排除的Id
	 * @return
	 */
	int deleteBySpecIdAndUncontainIds(Integer specId, List<Integer> uncontainIds);

	/**
	 * 通过枚举值ids 查找
	 * @author zjl
	 * @date 2015年7月24日 下午12:03:46
	 * 
	 * @param specItemIds  举值ids 
	 * @return List<GoodsCatSpecItem>
	 */
	List<GoodsCatSpecItem> selectGoodsCatSpecItemBySpecItemIds(List<Integer> specItemIds);
}