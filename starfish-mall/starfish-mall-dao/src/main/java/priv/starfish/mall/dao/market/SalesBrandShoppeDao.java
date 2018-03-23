package priv.starfish.mall.dao.market;


import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.market.entity.SalesBrandShoppe;

@IBatisSqlTarget
public interface SalesBrandShoppeDao extends BaseDao<SalesBrandShoppe, Integer> {
	SalesBrandShoppe selectById(Integer id);

	SalesBrandShoppe selectByFloorNoAndBrandCode(Integer floorNo, String brandCode);

	int insert(SalesBrandShoppe salesBrandShoppe);

	int update(SalesBrandShoppe salesBrandShoppe);

	int deleteById(Integer id);

	/**
	 * 通过楼层No获取销售品牌专柜
	 * 
	 * @author 廖晓远
	 * @date 2015年7月21日 下午4:42:40
	 * 
	 * @param floorNo
	 * @return
	 */
	List<SalesBrandShoppe> selectByFloorNo(Integer floorNo);

	/**
	 * 根据楼层号和ids删除多余的销售品牌专柜
	 * 
	 * @author 廖晓远
	 * @date 2015年7月18日 下午6:46:39
	 * 
	 * @param floorNo
	 * @param uncontainIds
	 * @return
	 */
	int deleteByFloorNoAndUncontainIds(Integer floorNo, List<Integer> uncontainIds);

	/**
	 * 通过楼层号删除销售品牌专柜
	 * 
	 * @author 廖晓远
	 * @date 2015年7月18日 上午10:36:24
	 * 
	 * @param floorNo
	 * @return
	 */
	int deleteByFloorNo(Integer floorNo);
}
