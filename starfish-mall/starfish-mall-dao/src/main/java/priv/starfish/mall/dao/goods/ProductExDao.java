package priv.starfish.mall.dao.goods;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.goods.entity.ProductEx;

@IBatisSqlTarget
public interface ProductExDao extends BaseDao<ProductEx, Long> {
	ProductEx selectById(Long id);

	int insert(ProductEx productEx);

	int update(ProductEx productEx);

	int deleteById(Long id);

	/**
	 * 根据规格值key获取某商品下的货品Id
	 * 
	 * @author guoyn
	 * @date 2015年11月4日 下午8:39:05
	 * 
	 * @param specStr
	 * @param goodsId
	 * @return Long
	 */
	Long selectIdBySpecStrAndGoodsId(String specStr, Integer goodsId);
}