package priv.starfish.mall.service;

import java.util.List;
import java.util.Map;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.ecard.entity.ECard;
import priv.starfish.mall.ecard.entity.ECardTransactRec;
import priv.starfish.mall.ecard.entity.ECardTransferRec;
import priv.starfish.mall.ecard.entity.UserECard;
import priv.starfish.mall.order.entity.ECardOrderItem;
import priv.starfish.mall.svcx.entity.Svcx;
import priv.starfish.mall.svcx.entity.SvcxRankDisc;

public interface ECardService extends BaseService {

	/**
	 * 生成指定类型ecard的新编号
	 * 
	 * @author koqiui
	 * @date 2015年10月20日 下午8:00:32
	 * 
	 * @param eCardCode
	 * @return
	 */
	String newCardNo();

	// TODO------------------------e卡设置------------------------------

	/**
	 * 分页查询e卡类型
	 * 
	 * @author "WJJ"
	 * @date 2015年10月19日 下午4:06:23
	 * 
	 * @param paginatedFilter
	 *            like name , = disabled , = deleted
	 * @return
	 */
	PaginatedList<ECard> getECardsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 添加e卡类型
	 * 
	 * @author "WJJ"
	 * @date 2015年10月19日 下午4:21:04
	 * 
	 * @param eCard
	 * @return
	 */
	boolean saveECard(ECard eCard);

	/**
	 * 修改e卡类型
	 * 
	 * @author "WJJ"
	 * @date 2015年10月19日 下午4:23:47
	 * 
	 * @param eCard
	 * @return
	 */
	boolean updateECard(ECard eCard);

	/**
	 * 删除e卡类型
	 * 
	 * @author "WJJ"
	 * @date 2015年10月19日 下午4:31:54
	 * 
	 * @param code
	 * @return
	 */
	boolean deleteECardById(String code);

	/**
	 * 根据code获取e卡
	 * 
	 * @author "WJJ"
	 * @date 2015年10月19日 下午4:38:59
	 * 
	 * @param code
	 * @return
	 */
	ECard getECardById(String code);

	/**
	 * e卡首页
	 * 
	 * @author lichaojie
	 * @date 2015年11月10日 下午11:10:59
	 * 
	 * @param filter
	 * @return
	 */
	List<ECard> getNormalECards();

	/**
	 * 根据name判断是否已存在
	 * 
	 * @author "WJJ"
	 * @date 2015年10月19日 下午5:13:32
	 * 
	 * @param name
	 * @return
	 */
	Boolean existECardByName(String name);

	/**
	 * 根据用户ID和用户e卡ID判断是否存在
	 * 
	 * @author wangdi
	 * @date 2015年11月5日 下午4:19:33
	 * 
	 * @param userId
	 * @param cardId
	 * @param invalid
	 *            是否无效 false有效 true无效
	 * @return
	 */
	Boolean existECardByUserIdAndCardId(Integer userId, Integer cardId, Boolean invalid);

	/**
	 * e卡合并
	 * 
	 * @author wangdi
	 * @date 2015年11月5日 下午6:17:04
	 * 
	 * @param cardIdToMerge
	 * @param cardIdToBeMerged
	 * @return
	 */
	Boolean updateUserECardsForMerge(Integer cardIdToMerge, Integer cardIdToBeMerged);

	/**
	 * e卡赠送
	 * 
	 * @author wangdi
	 * @date 2015年11月9日 上午9:58:57
	 * 
	 * @param cardId
	 * @param userId
	 * @return
	 */
	Boolean updateUserECardForPresent(Integer cardId, Integer userIdFrom, String userPhoneTo);

	/**
	 * e卡删除
	 * 
	 * @author wangdi
	 * @date 2015年11月9日 下午4:25:22
	 * 
	 * @param cardId
	 * @return
	 */
	Boolean updateUserECardForDelete(Integer cardId);

	/**
	 * 更改余额（退款、取消订单、下单）
	 * 
	 * @author Administrator
	 * @date 2015年11月21日 下午4:45:24
	 * 
	 * @param userECard
	 * @return
	 */
	Boolean updateUserECardForChangeRemainVal(UserECard userECard);

	// TODO------------------------e卡列表------------------------------

	/**
	 * 分页查询 用户e卡列表
	 * 
	 * @author "WJJ"
	 * @date 2015年10月19日 下午4:06:23
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<UserECard> getUserECardsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 新增用户e卡
	 * 
	 * @author "WJJ"
	 * @date 2015年11月19日 下午1:36:30
	 * 
	 * @param userECard
	 * @return
	 */
	Boolean saveUserECard(UserECard userECard);

	/**
	 * 解除用户E卡与店铺的绑定关系
	 * 
	 * @author "WJJ"
	 * @date 2015年11月21日 下午4:49:55
	 * 
	 * @param userECard
	 * @return
	 */
	Boolean unbindUserECard(UserECard userECard);

	/**
	 * 获取用户全部e卡
	 * 
	 * @author wangdi
	 * @date 2015年11月10日 上午10:15:00
	 * 
	 * @param filter
	 * @return
	 */
	List<UserECard> getUserECardsByFilterNormal(MapContext filter);

	/**
	 * 分页查询用户e卡列表，后台
	 * 
	 * @author "WJJ"
	 * @date 2015年11月21日 上午10:53:47
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<UserECard> getUserECardsByFilterBack(PaginatedFilter paginatedFilter);

	// TODO------------------------e卡交易列表------------------------------
	/**
	 * 分页查询 e卡交易列表
	 * 
	 * @author "WJJ"
	 * @date 2015年10月19日 下午4:06:23
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<ECardTransactRec> getECardTradeRecByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据卡ID获取e卡交易记录列表
	 * 
	 * @author wangdi
	 * @date 2015年11月2日 下午3:48:31
	 * 
	 * @param userId
	 * @return
	 */
	List<ECardTransactRec> getECardTransactRecsByCardIdAndUserId(Integer cardId, Integer userId);

	// TODO------------------------e卡转赠列表------------------------------
	/**
	 * 分页查询 e卡转赠记录列表
	 * 
	 * @author "WJJ"
	 * @date 2015年10月19日 下午4:06:23
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<ECardTransferRec> getECardTransferRecByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据e卡ID和被赠予用户的ID查询转赠记录
	 * 
	 * @author wangdi
	 * @date 2015年11月4日 下午2:49:59
	 * 
	 * @param cardId
	 * @param userIdTo
	 * @return
	 */
	ECardTransferRec getECardTransferRecByCardIdAndUserIdTo(Integer cardId, Integer userIdTo);

	/**
	 * 获取e卡转赠记录
	 * 
	 * @author wangdi
	 * @date 2015年12月3日 下午2:29:51
	 * 
	 * @param filter
	 * @return
	 */
	List<ECardTransferRec> getECardTransferRecs(MapContext filter);

	/**
	 * 删除e卡转赠记录（假删）
	 * 
	 * @author wangdi
	 * @date 2015年12月30日 下午3:29:13
	 * 
	 * @param eCardTransferRecId
	 * @return
	 */
	Boolean updateECardTransferRecForDelete(Integer eCardTransferRecId, Integer userIdFrom);

	// TODO------------------------e卡订单项------------------------------

	/**
	 * 新增e卡订单项
	 * 
	 * @author "WJJ"
	 * @date 2015年11月19日 下午2:27:44
	 * 
	 * @param eCardOrderItem
	 * @return
	 */
	Boolean saveECardOrderItem(ECardOrderItem eCardOrderItem);

	// TODO------------------------e卡详情------------------------------

	/**
	 * 获取用户e卡信息
	 * 
	 * @author wangdi
	 * @date 2015年12月5日 下午5:38:56
	 * 
	 * @return
	 */
	UserECard getUserECardInfo(Integer cardId);

	/**
	 * 查询当前登录用户名下的所有e卡
	 * 
	 * @author 邓华锋
	 * @date 2016年1月5日 下午8:30:38
	 * 
	 * @param userId
	 * @return
	 */
	Map<Integer, Integer> getECardShopsByUserId(Integer userId);

	/**
	 * 
	 * 
	 * @author "WJJ"
	 * @date 2016年1月6日 下午2:47:28
	 * 
	 * @param seqNo
	 * @param seqNo2
	 * @return
	 */
	boolean updateECardForSeqNo(int seqNo, int seqNo2);

	List<SvcxRankDisc> getSvcRankDiscs(Integer shopId, Integer svcId);

	boolean saveSvcRankDiscs(Svcx svcx);
	
	SvcxRankDisc getSvcRankDiscByFilter(Integer shopId, Integer svcId, Integer rank);

	/**
	 * 根据卡编号查找UserECard
	 * 
	 * @author "WJJ"
	 * @date 2016年2月16日 下午10:22:43
	 * 
	 * @param cardNo
	 * @return
	 */
	UserECard getUserECardByCardNo(String cardNo);
}
