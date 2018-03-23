package priv.starfish.mall.dao.settle;

import java.util.Date;
import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.settle.entity.SettleProcess;

@IBatisSqlTarget
public interface SettleProcessDao extends BaseDao<SettleProcess, Long> {
	SettleProcess selectById(Long id);

	int insert(SettleProcess settleProcess);

	int update(SettleProcess settleProcess);

	int deleteById(Long id);

	/**
	 * 请求转账时，更新 结算流程表 ：请求批次号、请求流水号
	 * 
	 * @author "WJJ"
	 * @date 2015年11月9日 下午3:16:19
	 * 
	 * @param settleProcess
	 * @return
	 */
	int updateForReq(SettleProcess settleProcess);

	/**
	 * 根据请求流水号获取结算流程表中数据
	 * 
	 * @author "WJJ"
	 * @date 2015年11月9日 下午3:43:22
	 * 
	 * @param reqSerialNumber
	 * @return
	 */
	SettleProcess selectByReqNo(String reqNo);

	/**
	 * 转账完成后，更新 结算流程表 ：（支付宝）流水号、完成时间、转账标识。
	 * 
	 * @author "WJJ"
	 * @date 2015年11月9日 下午3:56:09
	 * 
	 * @param settleProcess
	 * @return
	 */
	int updateForResp(SettleProcess settleProcess);

	/**
	 * 商城后台--分页查询，结算流程表 （即待结算的信息）
	 * 
	 * @author "WJJ"
	 * @date 2015年11月12日 上午12:09:07
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SettleProcess> selectByFilterAsMall(PaginatedFilter paginatedFilter);
	
	/**
	 * 商户后台--分页查询，结算流程表 （即待结算的信息）
	 * 
	 * @author "WJJ"
	 * @date 2015年11月12日 上午12:09:07
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SettleProcess> selectByFilterAsMerch(PaginatedFilter paginatedFilter);

	/**
	 * 后台人员提交结算申请后，更新settleFlag为 settling
	 * 
	 * @author "WJJ"
	 * @date 2015年11月13日 下午3:49:50
	 * 
	 * @param settleProcess
	 * @return
	 */
	int updateSettleFlag(SettleProcess settleProcess);

	/**
	 * 查询是否已经有此账期的结算单
	 * 
	 * @author "WJJ"
	 * @date 2015年11月14日 上午1:37:32
	 * 
	 * @param settleDay
	 * @return
	 */
	int selectBySettleDay(Date settleDay);

	/**
	 * 分页查询结算流程记录表中 settleFlag为1 成功的
	 * 
	 * @author "WJJ"
	 * @date 2015年11月14日 下午12:10:06
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SettleProcess> selectByFilterAsSuccess(PaginatedFilter paginatedFilter);

	/**
	 * 商户后台，
	 * 
	 * @author "WJJ"
	 * @date 2015年11月26日 下午5:59:54
	 * 
	 * @param merchantId
	 * @param state 
	 * @return
	 */
	//List<SaleSettleRec> selectByMerchantIdAsSuccess(Integer merchantId, Integer state);

	/**
	 * 查询结算单信息中，商户最后一次生成结算单的日期
	 * 
	 * @author "WJJ"
	 * @date 2015年12月11日 下午4:24:58
	 * 
	 * @param id
	 * @return
	 */
	Date selectMaxCreateDay(Integer merchantId);

	/**
	 * 商城后台，待结算页面：分页查询状态为6、7的。
	 * 
	 * @author "WJJ"
	 * @date 2015年12月28日 上午1:40:20
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SettleProcess> selectByWaitingFilter(PaginatedFilter paginatedFilter);

	/**
	 * 支付宝——后台人员，点击按钮：提交结算单数据
	 * 
	 * @author "WJJ"
	 * @date 2015年12月28日 上午2:16:07
	 * 
	 * @return
	 */
	int submitSettleInfo();

	/**
	 * 支付宝——后台人员，点击按钮：更新结算单数据
	 * 
	 * @author "WJJ"
	 * @date 2015年12月28日 上午2:38:40
	 * 
	 * @return
	 */
	int updateSettleInfo(SettleProcess settleProcess);

	/**
	 * 根据结算状态，查询结算单集合
	 * 默认为7和8的。
	 * 
	 * @author "WJJ"
	 * @date 2015年12月28日 上午3:02:17
	 * 
	 * @return
	 */
	List<SettleProcess> selectBysettleFlag();

	/**
	 * 分页查询某结算日期内，所有的结算单
	 * 
	 * @author "WJJ"
	 * @date 2016年1月11日 上午12:44:01
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SettleProcess> selectByFilterAsSettleDay(PaginatedFilter paginatedFilter);
}
