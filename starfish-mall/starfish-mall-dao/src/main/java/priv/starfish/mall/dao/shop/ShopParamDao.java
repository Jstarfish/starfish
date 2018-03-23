package priv.starfish.mall.dao.shop;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.shop.entity.ShopParam;

@IBatisSqlTarget
public interface ShopParamDao extends BaseDao<ShopParam, Integer> {
	ShopParam selectById(Integer id);

	ShopParam selectByShopIdAndCode(Integer shopId, String code);

	int insert(ShopParam shopParam);

	int update(ShopParam shopParam);

	int deleteById(Integer id);

	/**
	 * 根据code查找店铺参数列表
	 * 
	 * @author 毛智东
	 * @date 2015年8月22日 下午2:39:46
	 * 
	 * @param code
	 * @return
	 */
	List<ShopParam> selectByCode(String code);
}