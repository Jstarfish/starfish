package priv.starfish.mall.dao.pay;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.pay.entity.PayWayParam;

@IBatisSqlTarget
public interface PayWayParamDao extends BaseDao<PayWayParam, Integer> {

	PayWayParam selectById(Integer id);

	PayWayParam selectByPWayIdAndNameAndIoFlag(Integer pWayId, String name);

	int insert(PayWayParam payWayParam);

	int update(PayWayParam payWayParam);

	int deleteById(Integer id);

	/**
	 * 批量插入支付方式参数
	 * 
	 * @author WJJ
	 * @date 2015年9月9日 下午6:29:49
	 * 
	 * @param params
	 * @return
	 */
	int batchInsertPayWayParams(List<PayWayParam> params);

	/**
	 * 根据支付方式id查找支付参数
	 * 
	 * @author WJJ
	 * @date 2015年9月9日 下午6:29:57
	 * 
	 * @param payWayId
	 * @return
	 */
	List<PayWayParam> selectByPWayId(Integer payWayId);

	/**
	 * 根据支付方式id删除支付参数
	 * 
	 * @author WJJ
	 * @date 2015年9月9日 下午6:30:04
	 * 
	 * @param payWayId
	 * @return
	 */
	int deleteByPWayId(Integer payWayId);

}
