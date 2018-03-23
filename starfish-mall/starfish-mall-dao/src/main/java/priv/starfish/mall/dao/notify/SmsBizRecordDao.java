package priv.starfish.mall.dao.notify;

import java.math.BigInteger;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.notify.entity.SmsBizRecord;

@IBatisSqlTarget
public interface SmsBizRecordDao extends BaseDao<SmsBizRecord, BigInteger> {
	SmsBizRecord selectById(BigInteger id);

	int insert(SmsBizRecord smsBizRecord);

	int update(SmsBizRecord smsBizRecord);

	int deleteById(BigInteger id);
}