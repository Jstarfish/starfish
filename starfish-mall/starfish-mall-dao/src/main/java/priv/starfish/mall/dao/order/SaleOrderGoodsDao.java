package priv.starfish.mall.dao.order;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.order.entity.SaleOrderGoods;

@IBatisSqlTarget
public interface SaleOrderGoodsDao extends BaseDao<SaleOrderGoods, Long> {
	SaleOrderGoods selectById(Long id);

	int insert(SaleOrderGoods saleOrderGoods);

	int update(SaleOrderGoods saleOrderGoods);

	int deleteById(Long id);

	/**
	 * 获取某货品的记录总数
	 * 
	 * @author guoyn
	 * @date 2015年11月3日 下午4:51:42
	 * 
	 * @param productId
	 * @return int
	 */
	int selectCountByProductId(Long productId);
	
	List<SaleOrderGoods> selectByOrderSvcId(Long orderSvcId);
	
	/**
	 * 根据订单No和用户ID获取商品详情
	 * 
	 * @author 邓华锋
	 * @date 2015年12月7日 上午11:20:26
	 * 
	 * @param orderNo
	 * @param userId
	 * @return
	 */
	List<SaleOrderGoods> selectByOrderNoAndUserId(String orderNo, Integer userId);
	
	/**
	 * 根据订单ID获取订单商品信息
	 * 
	 * @author wangdi
	 * @date 2016年1月11日 下午5:02:32
	 * 
	 * @param orderId
	 * @return
	 */
	List<SaleOrderGoods> selectByOrderId(Long orderId);
}