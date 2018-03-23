package priv.starfish.mall.dao.categ;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.categ.entity.GoodsCat;

@IBatisSqlTarget
public interface GoodsCatDao extends BaseDao<GoodsCat, Integer> {
	GoodsCat selectById(Integer id);

	GoodsCat selectByNameAndLevel(String name, Integer level);

	int insert(GoodsCat goodsCat);

	int update(GoodsCat goodsCat);

	int deleteById(Integer id);

	/**
	 * 通过父分类Id获取直接子级分类
	 * 
	 * @author 廖晓远
	 * @date 2015-5-27 下午3:31:40
	 * 
	 * @param parentId
	 * @return
	 */
	List<GoodsCat> selectByParentId(Integer parentId);

	/**
	 * 获取所有商品分类
	 * 
	 * @author 廖晓远
	 * @date 2015-5-27 下午7:42:43
	 * @return
	 */
	List<GoodsCat> selectByName(String name);

	/**
	 * 批量删除
	 * 
	 * @author 廖晓远
	 * @date 2015-5-28 下午7:33:26
	 * 
	 * @param ids
	 * @return
	 */
	int deleteByIds(List<Integer> ids);

	/**
	 * 通过级别获取分类
	 * 
	 * @author 廖晓远
	 * @date 2015年7月15日 上午10:46:00
	 * 
	 * @param level
	 * @return
	 */
	List<GoodsCat> selectByLevel(Integer level);

	/**
	 * 
	 * 更新商品分类是否为叶子节点
	 * @author guoyn
	 * @date 2015年10月14日 下午2:23:46
	 * 
	 * @param leafFlag true:叶子节点
	 * @param id
	 * @return int
	 */
	int updateLeafFlagById(boolean leafFlag, Integer id);

}
