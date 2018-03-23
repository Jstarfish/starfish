package priv.starfish.mall.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.cart.dto.SaleCartInfo;
import priv.starfish.mall.market.dict.CouponType;
import priv.starfish.mall.market.entity.Coupon;
import priv.starfish.mall.market.entity.EcardAct;
import priv.starfish.mall.market.entity.EcardActGift;
import priv.starfish.mall.market.entity.EcardActRule;
import priv.starfish.mall.market.entity.PrmtTag;
import priv.starfish.mall.market.entity.PrmtTagGoods;
import priv.starfish.mall.market.entity.PrmtTagSvc;
import priv.starfish.mall.market.entity.SvcCoupon;
import priv.starfish.mall.market.entity.UserCoupon;
import priv.starfish.mall.market.entity.UserSvcCoupon;

/**
 * 促销相关服务（活动、优惠券）
 * 
 * @author koqiui
 * @date 2015年10月30日 下午10:39:11
 *
 */
public interface SalePrmtService extends BaseService {
	String newCouponNo(CouponType couponType);

	// --------------------------------优惠券------------------------------------------------

	boolean saveCoupon(Coupon coupon);

	PaginatedList<Coupon> getCouponsByFilter(PaginatedFilter paginatedFilter);

	boolean updateCouponByIdAndDeleted(Integer id, Boolean deleted);

	boolean updateCouponByIdAndDisabled(Integer id, Boolean disabled);

	boolean updateCoupon(Coupon coupon);

	/**
	 * 获取e卡首单要赠送的优惠券
	 * 
	 * @author lichaojie
	 * @date 2015年11月20日 下午6:42:15
	 * 
	 * @return
	 */
	public Coupon getCouponForFirstECardOrder();

	boolean existCouponByGroupName(String name);

	// --------------------------------------用户优惠券--------------------------------------------

	PaginatedList<UserCoupon> getUserCouponsByFilter(PaginatedFilter paginatedFilter);

	List<UserCoupon> getUserCouponsByFilter(MapContext filter);

	/**
	 * 发放
	 * 
	 * @param userCoupon
	 * @return
	 */
	boolean saveUserCoupon(UserCoupon userCoupon);

	Integer getUserCouponCountByUserId(Integer userId);

	/**
	 * 删除优惠券假删
	 * 
	 * @param coupon
	 * @param userContext
	 * @return
	 */
	boolean updateUserCouponForDelete(UserCoupon userCoupon, UserContext userContext);

	/**
	 * 根新优惠券状态为已使用和还原优惠券
	 * 
	 * @author lichaojie
	 * @date 2015年11月21日 下午4:18:37
	 * 
	 * @param userCoupon
	 * @param userId
	 * @return
	 */
	boolean updateUserCouponByOrderId(UserCoupon userCoupon, Integer userId);

	/**
	 * 一次下单赠送一张优惠券方法
	 * 
	 * @param coupon
	 * @return
	 */
	boolean createUserCoupon(Coupon coupon, Integer userId);

	// --------------------------------------服务优惠券--------------------------------------------

	boolean saveSvcCoupon(SvcCoupon svcCoupon);

	PaginatedList<SvcCoupon> getSvcCouponsByFilter(PaginatedFilter paginatedFilter);

	boolean updateSvcCouponForDeleted(Integer id, Boolean deleted);

	boolean updateSvcCouponForDisabled(Integer id, Boolean disabled);

	boolean updateSvcCoupon(SvcCoupon svcCoupon);

	boolean existSvcCouponByGroupName(String name);

	// --------------------------------------用户优惠券--------------------------------------------

	PaginatedList<UserSvcCoupon> getUserSvcCouponsByFilter(PaginatedFilter paginatedFilter);

	List<UserSvcCoupon> getUserSvcCouponByFilter(MapContext filter);

	/**
	 * 发放
	 * 
	 * @param userCoupon
	 * @return
	 */
	boolean saveUserSvcCoupon(UserSvcCoupon userSvcCoupon);

	Integer getUserSvcCouponCountByUserId(Integer userId);

	/**
	 * 删除优惠券假删
	 * 
	 * @param coupon
	 * @param userContext
	 * @return
	 */
	boolean updateUserSvcCouponForDeleted(UserSvcCoupon userSvcCoupon, UserContext userContext);

	/**
	 * 根新优惠券状态为已使用和还原优惠券
	 * 
	 * @author lichaojie
	 * @date 2015年11月21日 下午4:18:37
	 * 
	 * @param userCoupon
	 * @param userId
	 * @return
	 */
	boolean updateUserSvcCouponByOrderId(UserSvcCoupon userSvcCoupon, Integer userId);

	/**
	 * 一次下单赠送一张优惠券方法
	 * 
	 * @param coupon
	 * @return
	 */
	boolean createUserSvcCoupon(SvcCoupon svcCoupon, Integer userId);

	// --------------------------------匹配优惠券------------------------------------------------------
	Map<String, Object> getMatchingUserCoupons(Integer userId, SaleCartInfo saleCartInfo);

	// --------------------------------促销标签------------------------------------------------------

	/**
	 * 分页查询促销标签列表
	 * 
	 * @param paginatedFilter
	 *            :like name
	 * @return PaginatedList<PrmtTag>
	 */
	PaginatedList<PrmtTag> getPrmtTagsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 保存促销标签货品
	 * 
	 * @param prmtTagId
	 * @param productId
	 * @return boolean
	 */
	boolean savePrmtTagProduct(Integer prmtTagId, Long productId);

	/**
	 * 删除促销标签货品记录信息
	 * 
	 * @param tagId
	 * @param productId
	 * @return boolean
	 */
	boolean deletePrmtTagProduct(Integer tagId, Long productId);

	/**
	 * 保存促销标签服务
	 * 
	 * @param prmtTagId
	 * @param svcId
	 * @return boolean
	 */
	boolean savePrmtTagSvc(Integer prmtTagId, Integer svcId);

	/**
	 * 删除促销标签服务
	 * 
	 * @param tagId
	 * @param svcId
	 * @return boolean
	 */
	boolean deletePrmtTagSvc(Integer tagId, Integer svcId);

	/**
	 * 分页获取促销标签货品列表
	 * 
	 * @param paginatedFilter:
	 *            like product.title, = prmtTag.id
	 * @return PaginatedList<PrmtTagGoods>
	 */
	PaginatedList<PrmtTagGoods> getPrmtTagProductsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 获取所有促销标签列表
	 * 
	 * @return List<PrmtTag>
	 */
	List<PrmtTag> getPrmtTags();

	/**
	 * 分页获取促销标签服务列表
	 * 
	 * @param paginatedFilter:
	 *            like svc.name, = prmtTag.id
	 * @return PaginatedList<PrmtTagSvc>
	 */
	PaginatedList<PrmtTagSvc> getPrmtTagSvcsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 批量更新促销标签货品的序号
	 * 
	 * @param tagProductSeqNos:
	 *            key:促销标签货品id，value:序号
	 * @return boolean
	 */
	boolean updatePrmtTagProductSeqNos(List<Map<String, Object>> tagProductSeqNos);

	/**
	 * 批量更新促销标签服务的序号
	 * 
	 * @param tagSvcSeqNos:
	 *            key:促销标签货品id，value:序号
	 * @return boolean
	 */
	boolean updatePrmtTagSvcSeqNos(List<Map<String, Object>> tagSvcSeqNos);

	// --------------------------------------------e卡活动-----------------------------------------------
	/**
	 * 分页获取e卡活动列表
	 * 
	 * @param paginatedFilter:
	 * @return PaginatedList<PrmtTagSvc>
	 */
	PaginatedList<EcardAct> getEcardActByFilter(PaginatedFilter paginatedFilter);

	boolean saveEcardAct(EcardAct ecardAct);

	boolean updateEcardAct(EcardAct ecardAct);

	boolean updateEcardActForDeleted(EcardAct ecardAct);

	boolean updateEcardActForDisabled(EcardAct ecardAct);

	boolean existEcardActByName(String name);

	// ------------------------------------------------e卡活动规则---------------------------------------------
	List<EcardActRule> getEcardActRulesByActId(Integer actId);

	boolean saveEcardActRule(EcardActRule ecardActRule);

	boolean updateEcardActRule(EcardActRule ecardActRule);

	boolean deleteEcardActRule(Integer ruleId);

	List<EcardActRule> getEcardActRulesByFilter(MapContext requestFilter);

	EcardActRule getEcardActRuleByFilter(MapContext requestFilter);

	void saveEcardActGift(Integer userId, String code, Integer buyQuantity, BigDecimal buyAmount);
	// ------------------------------------------------e卡活动商品---------------------------------------------

	List<EcardActGift> getEcardActGiftsByActRuleId(Integer actRuleId);

	boolean saveEcardActGift(EcardActGift ecardActGift);

	boolean updateEcardActGift(EcardActGift ecardActGift);

	boolean deleteEcardActGift(Long giftId);

	// ------------------------------------------------ 促销商品 ---------------------------------------------
	PrmtTag getPrmtTagByCode(String code);
	
}
