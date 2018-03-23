package priv.starfish.mall.dao.order;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.order.entity.SaleOrderRecord;

@IBatisSqlTarget
public interface SaleOrderRecordDao extends BaseDao<SaleOrderRecord, Long> {
	SaleOrderRecord selectById(Long id);

	int insert(SaleOrderRecord saleOrderRecord);

	int update(SaleOrderRecord saleOrderRecord);

	int deleteById(Long id);
	
	/**
	 * 根据订单ID查询操作记录
	 * 
	 * @author wangdi
	 * @date 2015年11月18日 下午7:13:28
	 * 
	 * @param orderId
	 * @return
	 */
	List<SaleOrderRecord> selectByOrderId(Long orderId);
}