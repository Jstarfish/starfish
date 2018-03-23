package priv.starfish.mall.dao.order;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.order.entity.ECardOrderRecord;

@IBatisSqlTarget
public interface ECardOrderRecordDao extends BaseDao<ECardOrderRecord, Long> {
	ECardOrderRecord selectById(Long id);

	int insert(ECardOrderRecord eCardOrderRecord);

	int update(ECardOrderRecord eCardOrderRecord);

	int deleteById(Long id);
	
	List<ECardOrderRecord> selectByOrderId(Integer orderId);
}