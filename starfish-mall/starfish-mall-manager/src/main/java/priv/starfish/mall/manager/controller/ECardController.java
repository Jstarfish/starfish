package priv.starfish.mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.*;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.ecard.entity.ECard;
import priv.starfish.mall.ecard.entity.ECardTransactRec;
import priv.starfish.mall.ecard.entity.ECardTransferRec;
import priv.starfish.mall.ecard.entity.UserECard;
import priv.starfish.mall.service.ECardOrderService;
import priv.starfish.mall.service.ECardService;
import priv.starfish.mall.svcx.entity.Svcx;
import priv.starfish.mall.svcx.entity.SvcxRankDisc;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * e卡相关
 * 
 * @author "WJJ"
 * @date 2015年10月18日 下午4:07:05
 *
 */
@Controller
@RequestMapping("/ecard")
public class ECardController extends BaseController {
	@Resource
	ECardService ecardService;

	@Resource
	ECardOrderService eCardOrderService;

	// TODO--------------------------------------- e卡设置 ----------------------------------------------

	@Remark("e卡设置页面")
	@RequestMapping(value = "/type/mgmt/jsp", method = RequestMethod.GET)
	public String toEcardSet() {
		return "ecard/ecardSet";
	}

	/**
	 * e卡列表
	 * 
	 * @author 郝江奎
	 * @date 2016年1月12日 下午5:47:50
	 * 
	 * @param request
	 * @return
	 */
	@Remark("e卡列表")
	@RequestMapping(value = "/normal/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<ECard>> getECard(HttpServletRequest request) {
		Result<List<ECard>> result = Result.newOne();
		try {
			List<ECard> eCardList = ecardService.getNormalECards();
			// 查询结果赋值
			result.data = eCardList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
		}
		return result;
	}

	/**
	 * e卡服务身份打折列表
	 * 
	 * @author 郝江奎
	 * @date 2016年1月12日 下午6:47:50
	 * 
	 * @param request
	 * @return
	 */
	@Remark("e卡服务身份打折列表")
	@RequestMapping(value = "/svcRankDisc/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<SvcxRankDisc>> getSvcRankDisc(HttpServletRequest request, @RequestBody SvcxRankDisc svcxRankDisc) {
		Result<List<SvcxRankDisc>> result = Result.newOne();
		try {
			List<SvcxRankDisc> eCardList = ecardService.getSvcRankDiscs(svcxRankDisc.getShopId(), svcxRankDisc.getSvcId());
			// 查询结果赋值
			result.data = eCardList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
		}
		return result;
	}

	/**
	 * 修改e卡服务身份打折
	 * 
	 * @author 郝江奎
	 * @date 2016年1月12日 下午6:47:50
	 * 
	 * @param request
	 * @return
	 */
	@Remark("修改e卡服务身份打折")
	@RequestMapping(value = "/svcRankDisc/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Svcx> saveSvcRankDisc(HttpServletRequest request, @RequestBody Svcx svcx) {
		Result<Svcx> result = Result.newOne();
		boolean ok = false;
		try {
			ok = ecardService.saveSvcRankDiscs(svcx);
			// 查询结果赋值
			if (ok) {
				result.message = "修改成功";
			} else {
				result.type = Type.warn;
				result.message = "修改失败";
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			result.type = Result.Type.error;
		}
		return result;
	}

	@Remark("分页查询e卡")
	@RequestMapping(value = "/list/get/by/filter", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<ECard> getEcards(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<ECard> paginatedList = ecardService.getECardsByFilter(paginatedFilter);
		//
		JqGridPage<ECard> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	@Remark("添加e卡类型")
	@RequestMapping(value = "/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> saveEcard(@RequestBody ECard ecard) {
		Result<String> result = Result.newOne();
		boolean ok = ecardService.saveECard(ecard);
		if (ok) {
			result.data = ecard.getCode();
			result.message = "保存成功";
		} else {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}

	@Remark("修改e卡类型")
	@RequestMapping(value = "/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateEcard(@RequestBody ECard ecard) {
		Result<Integer> result = Result.newOne();
		//
		ECard card = ecardService.getECardById(ecard.getCode());
		if (card.getSeqNo() != ecard.getSeqNo()) {
			ecardService.updateECardForSeqNo(card.getSeqNo(), ecard.getSeqNo());
		}

		// 更新此条数据
		boolean ok = ecardService.updateECard(ecard);
		if (ok) {
			result.message = "修改成功";
		} else {
			result.type = Type.warn;
			result.message = "修改失败";
		}
		return result;
	}

	@Remark("删除e卡类型")
	@RequestMapping(value = "/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteEcard(@RequestBody ECard ecard) {
		Result<?> result = Result.newOne();
		boolean flag = eCardOrderService.getECardOrderCountByCode(ecard.getCode());
		if (flag) {
			result.type = Type.warn;
			result.message = "此E卡含有关联数据，不可删除";
			return result;
		}
		//
		boolean ok = ecardService.deleteECardById(ecard.getCode());
		if (ok) {
			result.message = "删除成功";
		} else {
			result.type = Type.warn;
			result.message = "删除失败";
		}
		return result;
	}

	@Remark("根据e卡code获取e卡")
	@RequestMapping(value = "/ecard/get/by/code", method = RequestMethod.POST)
	@ResponseBody
	public Result<ECard> getEcardById(@RequestBody ECard ecard) {
		Result<ECard> result = Result.newOne();
		result.data = ecardService.getECardById(ecard.getCode());
		return result;
	}

	@Remark("根据名称判断是否已存在")
	@RequestMapping(value = "/exist/by/name", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> existEcardByName(@RequestBody ECard ecard) {
		Result<Boolean> result = Result.newOne();
		result.data = ecardService.existECardByName(ecard.getName());
		return result;
	}
	// TODO--------------------------------------- e卡列表 ----------------------------------------------

	/**
	 * 商城后台，所有已购E卡
	 * 
	 * @author "WJJ"
	 * @date 2015年12月24日 下午2:39:49
	 * 
	 * @return
	 */
	@Remark("e卡列表页面")
	@RequestMapping(value = "/list/jsp", method = RequestMethod.GET)
	public String toEcardList() {
		return "ecard/ecardList";
	}

	@Remark("分页查询 用户e卡")
	@RequestMapping(value = "/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<UserECard> getUserEcards(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<UserECard> paginatedList = ecardService.getUserECardsByFilterBack(paginatedFilter);
		//
		JqGridPage<UserECard> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 店铺后台，所有此店铺相关E卡
	 * 
	 * @author "WJJ"
	 * @date 2015年12月24日 下午2:39:49
	 * 
	 * @return
	 */
	@Remark("店铺e卡列表页面")
	@RequestMapping(value = "/list/jsp/-shop", method = RequestMethod.GET)
	public String toEcardListForShop() {
		return "ecard/ecardListForShop";
	}

	@Remark("分页查询 用户e卡")
	@RequestMapping(value = "/list/get/-shop", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<UserECard> getUserEcardsForShop(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntity().getId();

		MapContext filterItems = paginatedFilter.getFilterItems();
		filterItems.put("shopId", shopId);
		//
		PaginatedList<UserECard> paginatedList = ecardService.getUserECardsByFilterBack(paginatedFilter);
		//
		JqGridPage<UserECard> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	// TODO--------------------------------------- e卡交易记录 ----------------------------------------------

	@Remark("e卡交易记录")
	@RequestMapping(value = "/transact/rec/list/jsp", method = RequestMethod.GET)
	public String toEcardTradeRec() {
		return "ecard/ecardTradeRec";
	}

	@Remark("分页查询 e卡交易记录")
	@RequestMapping(value = "/transact/rec/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<ECardTransactRec> getEcardTradeRec(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		MapContext filter = paginatedFilter.getFilterItems();
		String cardNo = filter.getTypedValue("cardId", String.class);
		
		UserECard userECard = ecardService.getUserECardByCardNo(cardNo);
		filter.put("cardId", userECard.getId());
		//
		PaginatedList<ECardTransactRec> paginatedList = ecardService.getECardTradeRecByFilter(paginatedFilter);
		//
		JqGridPage<ECardTransactRec> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	@Remark("解除E卡绑定")
	@RequestMapping(value = "/unbind/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<UserECard> unbindUserECard(@RequestBody UserECard userECard) {
		Result<UserECard> result = Result.newOne();
		//
		boolean ok = ecardService.unbindUserECard(userECard);
		if (ok) {
			result.message = "解绑成功";
		} else {
			result.type = Type.warn;
			result.message = "解绑失败";
		}
		return result;
	}
	// TODO--------------------------------------- e卡转赠记录 ----------------------------------------------

	@Remark("e卡转赠记录 ")
	@RequestMapping(value = "/transfer/rec/list/jsp", method = RequestMethod.GET)
	public String toEcardTransferRec() {
		return "ecard/ecardTransferRec";
	}

	@Remark("分页查询 e卡转赠记录")
	@RequestMapping(value = "/transfer/rec/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<ECardTransferRec> getEcardTransferRec(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<ECardTransferRec> paginatedList = ecardService.getECardTransferRecByFilter(paginatedFilter);
		//
		JqGridPage<ECardTransferRec> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	@Remark("获取车ecard列表")
	@RequestMapping(value = "/selectList/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SelectList> getCarSvcGroupList() {
		Result<SelectList> result = Result.newOne();
		SelectList selectList = SelectList.newOne();
		List<ECard> listEcard = ecardService.getNormalECards();
		if (null != listEcard && listEcard.size() > 0) {
			for (ECard ecard : listEcard) {
				selectList.addItem(ecard.getCode(), ecard.getName());
			}
		}
		selectList.setDefaultValue("");
		result.data = selectList;
		return result;
	}
}
