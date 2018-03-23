package priv.starfish.mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.PhoneNumberUtil;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.ecard.entity.ECard;
import priv.starfish.mall.ecard.entity.ECardTransactRec;
import priv.starfish.mall.ecard.entity.ECardTransferRec;
import priv.starfish.mall.ecard.entity.UserECard;
import priv.starfish.mall.notify.dict.SmsUsage;
import priv.starfish.mall.notify.entity.SmsVerfCode;
import priv.starfish.mall.service.BaseConst.SmsCodes;
import priv.starfish.mall.service.ECardService;
import priv.starfish.mall.service.SettingService;
import priv.starfish.mall.service.UserService;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ecard")
public class ECardController extends BaseController {
	@Resource
	ECardService eCardService;

	@Resource
	SettingService settingService;

	@Resource
	UserService userService;

	/**
	 * 分页查询当前登录用户名下的所有e卡
	 * 
	 * @author wangdi
	 * @date 2015年11月2日 上午9:54:19
	 * 
	 * @param request
	 * @param paginatedFilter
	 * @return
	 */
	@Remark("分页查询当前登录用户名下的所有e卡")
	@RequestMapping(value = "/userECard/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<PaginatedList<UserECard>> getUserECards(HttpServletRequest request, @RequestBody PaginatedFilter paginatedFilter) {
		Result<PaginatedList<UserECard>> result = Result.newOne();
		try {
			// 在过滤条件中加入当前用户ID
			UserContext userContext = getUserContext(request);
			MapContext map = paginatedFilter.getFilterItems();
			Integer userId = userContext.getUserId();
			map.put("userId", userId);
			// 执行查询
			PaginatedList<UserECard> paginatedList = eCardService.getUserECardsByFilter(paginatedFilter);
			// 查询e卡关联的转赠记录
			for (UserECard userECard : paginatedList.getRows()) {
				ECardTransferRec eCardTransferRec = eCardService.getECardTransferRecByCardIdAndUserIdTo(userECard.getId(), userId);
				userECard.seteCardTransferRec(eCardTransferRec);
			}
			// 查询结果赋值
			result.data = paginatedList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
			result.message = "获取用户e卡列表过程出现异常！";
		}

		return result;
	}

	/**
	 * 查询当前登录用户名下的所有除所选e卡之外的e卡，用于e卡合并
	 * 
	 * @author wangdi
	 * @date 2015年11月4日 下午8:13:20
	 * 
	 * @param request
	 * @return
	 */
	@Remark("查询当前登录用户名下的所有除所选e卡之外的e卡")
	@RequestMapping(value = "/userECard/list/other/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<UserECard>> getOtherUserECards(HttpServletRequest request) {
		Result<List<UserECard>> result = Result.newOne();
		try {
			// 获取过滤条件参数
			String cardId = request.getParameter("cardId");
			if (cardId == null) {
				logger.error(String.format("Parameter cardId is null. Class:%s getOtherUserEcards", getClass().getName()));
				result.type = Result.Type.error;
				result.message = "e卡未指定";
				return result;
			}
			String cardNo = request.getParameter("cardNo");

			// 查询当前用户ID
			UserContext userContext = getUserContext(request);
			Integer userId = userContext.getUserId();
			// 构造过滤条件
			MapContext filter = MapContext.newOne();
			filter.put("cardIdBesides", cardId);
			filter.put("userId", userId);
			filter.put("invalid", false);
			filter.put("cardNo", cardNo);
			filter.put("isShopBinded", false);
			// 执行查询
			List<UserECard> userECards = eCardService.getUserECardsByFilterNormal(filter);
			// 查询结果赋值
			result.data = userECards;
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
			result.message = "获取用户额外e卡列表过程出现异常！";
		}

		return result;
	}

	/**
	 * 查询 e卡交易记录
	 * 
	 * @author wangdi
	 * @date 2015年11月2日 上午9:54:29
	 * 
	 * @param request
	 * @return
	 */
	@Remark("查询e卡交易记录")
	@RequestMapping(value = "/eCardTransactRec/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<ECardTransactRec>> getECardTransactRecs(HttpServletRequest request) {
		Result<List<ECardTransactRec>> result = Result.newOne();
		try {
			String cardId = request.getParameter("cardId");
			if (cardId == null) {
				logger.error(String.format("Parameter cardId is null. Class:%s Method:getECardTransactRecs", getClass().getName()));
				result.type = Result.Type.error;
				result.message = "e卡未指定";
				return result;
			}
			UserContext userContext = getUserContext(request);
			Integer userId = userContext.getUserId();
			// TODO:应加入e卡ID是否属于当前登录用户的判断
			List<ECardTransactRec> eCardTransactRecs = eCardService.getECardTransactRecsByCardIdAndUserId(Integer.parseInt(cardId), userId);
			result.data = eCardTransactRecs;
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
			result.message = "获取交易记录过程出现异常！";
		}

		return result;
	}

	/**
	 * 分页查询e卡转赠记录
	 * 
	 * @author wangdi
	 * @date 2015年11月2日 上午9:54:38
	 * 
	 * @param request
	 * @param paginatedFilter
	 * @return
	 */
	@Remark("分页查询e卡转赠记录")
	@RequestMapping(value = "/eCardTransferRec/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<PaginatedList<ECardTransferRec>> getECardTransferRecs(HttpServletRequest request, @RequestBody PaginatedFilter paginatedFilter) {
		Result<PaginatedList<ECardTransferRec>> result = Result.newOne();
		try {
			// 在过滤条件中加入当前用户ID
			UserContext userContext = getUserContext(request);
			MapContext map = paginatedFilter.getFilterItems();
			Integer userId = userContext.getUserId();
			map.put("userIdFrom", userId);
			PaginatedList<ECardTransferRec> paginatedList = eCardService.getECardTransferRecByFilter(paginatedFilter);
			// 查询结果赋值
			result.data = paginatedList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
			result.message = "获取转赠记录失败";
		}

		return result;
	}

	/**
	 * e卡合并
	 * 
	 * @author wangdi
	 * @date 2015年11月5日 下午3:43:42
	 * 
	 * @param request
	 * @return
	 */
	@Remark("e卡合并")
	@RequestMapping(value = "/userECard/merge/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> mergeECards(HttpServletRequest request) {
		Result<?> result = Result.newOne();

		try {
			String cardIdToMerge = request.getParameter("cardIdToMerge");
			String cardIdToBeMerged = request.getParameter("cardIdToBeMerged");
			if (cardIdToMerge == null || cardIdToBeMerged == null) {
				logger.error(String.format("Parameter cardIdToMerge or cardIdMerged is null. Class:%s Method:mergeECards", getClass().getName()));
				result.type = Result.Type.warn;
				result.message = "原卡或目标卡未指定！";
				return result;
			}

			// 获取用户ID
			UserContext userContext = getUserContext(request);
			Integer userId = userContext.getUserId();

			// 检查用户和e卡的绑定关系
			if (!eCardService.existECardByUserIdAndCardId(userId, Integer.parseInt(cardIdToMerge), false) || !eCardService.existECardByUserIdAndCardId(userId, Integer.parseInt(cardIdToBeMerged), false)) {
				logger.error(String.format("At least one of the two cards for merging are not belong to current user. userId:%d cardIdToMerge:%s cardIdToBeMerged:%s", userId, cardIdToMerge, cardIdToBeMerged));
				result.type = Result.Type.warn;
				result.message = "待合并的原卡和目标卡中至少有一张不属于当前用户！";
				return result;
			}

			// 合并的两张卡必须绑定同一门店，或者都没有绑定门店
			UserECard cardFrom = eCardService.getUserECardInfo(Integer.parseInt(cardIdToMerge));
			Integer cardFromShopId = cardFrom.getShopId();
			UserECard cardTo = eCardService.getUserECardInfo(Integer.parseInt(cardIdToBeMerged));
			Integer cardToShopId = cardTo.getShopId();
			if (cardFromShopId != null || cardToShopId != null) {
				result.type = Result.Type.warn;
				result.message = "待合并的原卡和目标卡中至少有一张绑定了门店，暂不支持合并操作";
				return result;
			}

			// 合并e卡
			if (!eCardService.updateUserECardsForMerge(Integer.parseInt(cardIdToMerge), Integer.parseInt(cardIdToBeMerged))) {
				logger.error(String.format("Merging procedure failed. userId:%d cardIdToMerge:%s cardIdToBeMerged:%s", userId, cardIdToMerge, cardIdToBeMerged));
				result.type = Result.Type.warn;
				result.message = "合并失败！";
				return result;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
			result.message = "e卡合并过程中出现异常！";
		}

		return result;
	}

	/**
	 * 发送e卡转赠验证码短信
	 * 
	 * @author wangdi
	 * @date 2015年11月9日 下午4:19:53
	 * 
	 * @param request
	 * @return
	 */
	@Remark("发送转赠短信验证码")
	@RequestMapping(value = "/userECard/present/sms/code/send", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> sendSmsCodeForTransfer(HttpServletRequest request) {
		Result<Boolean> result = Result.newOne();

		try {
			UserContext userContext = getUserContext(request);
			String phoneNo = userContext.getPhoneNo();
			String reqIp = request.getRemoteAddr();
			// 发送普通验证码短信
			SmsUsage tplName = SmsUsage.normal;
			boolean success = sendSmsVerfCode(tplName, phoneNo, SmsCodes.NORMAL, reqIp);
			//
			result.data = success;
			if (success) {
				result.message = "短信验证码已发送";
			} else {
				result.type = Type.warn;
				result.message = "短信验证码发送失败";
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			result.data = false;
			result.message = "短信验证码发送失败";
		}

		return result;
	}

	/**
	 * e卡转赠
	 * 
	 * @author wangdi
	 * @date 2015年11月9日 下午4:22:53
	 * 
	 * @param request
	 * @return
	 */
	@Remark("e卡转赠")
	@RequestMapping(value = "/userECard/present/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> presentUserECard(HttpServletRequest request) {
		Result<Boolean> result = Result.newOne();

		// 获取参数
		String cardId = request.getParameter("cardId");
		String doneePhone = request.getParameter("doneePhone");
		String verfCode = request.getParameter("verfCode");
		if (doneePhone == null || verfCode == null || cardId == null) {
			result.data = false;
			result.message = "缺少参数";
			return result;
		}

		// 验证码校验
		SmsVerfCode smsVerfCode = new SmsVerfCode();
		UserContext userContext = getUserContext(request);
		String phoneNo = userContext.getPhoneNo();
		smsVerfCode.setPhoneNo(phoneNo);
		smsVerfCode.setVfCode(verfCode);
		smsVerfCode.setUsage(SmsUsage.normal);
		if (!settingService.validSmsVerfCode(smsVerfCode)) {
			result.data = false;
			result.message = "验证码错误";
			return result;
		}

		// 检查e卡是否归属于当前的登录的用户
		Integer userId = userContext.getUserId();
		if (!eCardService.existECardByUserIdAndCardId(userId, Integer.parseInt(cardId), false)) {
			logger.warn(String.format("The card for present is not belong to current user. userId:%d cardId:%s", userId, cardId));
			result.data = false;
			result.message = "该卡不属于当前用户，无法转赠！";
			return result;
		}

		// 不能转赠给用户自己
		if (userContext.getPhoneNo().equals(doneePhone)) {
			result.data = false;
			result.message = "不能转赠给自己";
			return result;
		}

		// 检查受赠用户是否存在
		if (!userService.existsUserByPhoneNo(doneePhone)) {
			logger.warn(String.format("Donee is not exist. donee phone number:%s", doneePhone));
			result.data = false;
			result.message = "受赠用户不存在";
			return result;
		}

		// 执行转赠
		if (!eCardService.updateUserECardForPresent(Integer.parseInt(cardId), userContext.getUserId(), doneePhone)) {
			logger.warn(String.format("Donee is not exist. donee phone number:%s", doneePhone));
			result.data = false;
			result.message = "转赠失败";
			return result;
		}
		// 修改验证码记录（已使用）
		settingService.updateSmsVerfCode(smsVerfCode);

		// 发送转赠成功短信通知
		UserECard userECard = eCardService.getUserECardInfo(Integer.parseInt(cardId));
		User userTo = userService.getUserByPhoneNo(doneePhone);
		MapContext dataModel = MapContext.newOne();
		dataModel.put("ecardName", userECard.geteCard().getName());
		dataModel.put("ecardNo", userECard.getCardNo());
		dataModel.put("faceVal", userECard.getFaceValue());
		dataModel.put("userPhoneTo", PhoneNumberUtil.asMaskedPhone(userTo.getPhoneNo()));
		dataModel.put("userNameTo", userTo.getNickName() == null ? "其他用户" : userTo.getNickName());
		dataModel.put("userPhoneFrom", PhoneNumberUtil.asMaskedPhone(userContext.getPhoneNo()));
		dataModel.put("userNameFrom", userContext.getUserName() == null ? "亿投车吧用户" : userContext.getUserName());
		sendSmsText(userContext.getPhoneNo(), SmsCodes.ECARD_TRANSFER_NOTIFY_FROM, dataModel);
		sendSmsText(userTo.getPhoneNo(), SmsCodes.ECARD_TRANSFER_NOTIFY_TO, dataModel);

		result.data = true;
		return result;
	}

	/**
	 * e卡删除(假删)
	 * 
	 * @author wangdi
	 * @date 2015年11月10日 上午10:43:41
	 * 
	 * @param request
	 * @return
	 */
	@Remark("e卡删除")
	@RequestMapping(value = "/userECard/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> deleteUserECard(HttpServletRequest request) {
		Result<Boolean> result = Result.newOne();
		String cardId = request.getParameter("cardId");
		if (cardId == null) {
			result.data = false;
			result.message = "缺少参数";
			return result;
		}

		UserContext userContext = getUserContext(request);
		Integer userId = userContext.getUserId();
		if (!eCardService.existECardByUserIdAndCardId(userId, Integer.parseInt(cardId), true)) {
			logger.warn(String.format("The card for present is not belong to current user. userId:%d cardId:%s", userId, cardId));
			result.data = false;
			result.message = "该卡不属于当前用户，无法删除";
			return result;
		}

		if (!eCardService.updateUserECardForDelete(Integer.parseInt(cardId))) {
			logger.warn(String.format("Deleting card failed. userId:%d cardId:%s", userId, cardId));
			result.data = false;
			result.message = "删除失败";
			return result;
		}

		result.data = true;
		return result;
	}

	@Remark("e卡首页")
	@RequestMapping(value = "/list/jsp", method = RequestMethod.GET)
	public String toCouponJsp() {
		return "ecard/ecardList";
	}

	/**
	 * e卡首页
	 * 
	 * @author 李超杰
	 * @date 2015年11月10日 下午10:54:53
	 * 
	 * @param request
	 * @return
	 */
	@Remark("e卡首页列表")
	@RequestMapping(value = "/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<ECard>> getECard(HttpServletRequest request) {
		Result<List<ECard>> result = Result.newOne();
		try {
			List<ECard> eCardList = eCardService.getNormalECards();
			// 查询结果赋值
			result.data = eCardList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
		}

		return result;
	}

	/**
	 * 查询当前登录用户名下的所有e卡
	 * 
	 * @author wangdi
	 * @date 2015年11月10日 上午10:43:22
	 * 
	 * @param request
	 * @return
	 */
	@Remark("查询当前登录用户名下的所有e卡")
	@RequestMapping(value = "/userECard/list/normal/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<UserECard>> getUserECardsNormal(HttpServletRequest request, @RequestBody UserECard userEcard) {
		Result<List<UserECard>> result = Result.newOne();
		try {
			// 查询当前用户ID
			UserContext userContext = getUserContext(request);
			Integer userId = userContext.getUserId();
			// Integer shopId=request.getParameter("shopId")!=null?Integer.parseInt(request.getParameter("shopId")):null;
			// 构造过滤条件
			MapContext filter = MapContext.newOne();
			filter.put("userId", userId);
			filter.put("invalid", false);
			filter.put("shopId", userEcard.getShopId());
			// 执行查询
			List<UserECard> userECards = eCardService.getUserECardsByFilterNormal(filter);
			// 查询结果赋值
			result.data = userECards;
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
			result.message = "获取用户e卡列表失败";
		}

		return result;
	}

	/**
	 * 查询当前登录用户名下的所有e卡
	 * 
	 * @author 邓华锋
	 * @date 2016年1月5日 下午8:30:38
	 * 
	 * @param request
	 * @param userEcard
	 * @return
	 */
	@Remark("查询当前登录用户名下的所有e卡")
	@RequestMapping(value = "/userECard/shop/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<Integer, Integer>> getECardShopsByUserId(HttpServletRequest request, @RequestBody UserECard userEcard) {
		Result<Map<Integer, Integer>> result = Result.newOne();
		try {
			// 查询当前用户ID
			UserContext userContext = getUserContext(request);
			Integer userId = userContext.getUserId();
			// 查询结果赋值
			result.data = eCardService.getECardShopsByUserId(userId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
			result.message = "获取用户绑定的e卡店铺列表失败";
		}
		return result;
	}

}
