package priv.starfish.mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.market.entity.Advert;
import priv.starfish.mall.market.entity.EcardActRule;
import priv.starfish.mall.service.AdvertService;
import priv.starfish.mall.service.SalePrmtService;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Remark("促销、活动、广告、优惠券")
@Controller
@RequestMapping(value = "/market")
public class MarketController extends BaseController {

	@Resource
	private AdvertService advertService;

	@Resource
	private SalePrmtService salePrmtService;

	@Resource
	FileRepository fileRepository;

	/**
	 * 获取广告
	 * 
	 * @author 郝江奎
	 * @date 2015年9月18日 下午4:13:00
	 * 
	 * @return
	 */
	@Remark("获取广告")
	@RequestMapping(value = "/advert/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<Advert>> selectAdvertByPosCode(HttpServletRequest request) {
		Result<List<Advert>> result = Result.newOne();
		List<Advert> advert = advertService.getAdverts();
		if (advert.size() > 0) {
			result.data = advert;
			result.message = "获取成功";
		} else {
			result.message = "暂无广告";
		}
		return result;

	}

	/**
	 * 获取广告 by posCode
	 * 
	 * @author 郝江奎
	 * @date 2015年9月18日 下午4:13:00
	 * 
	 * @return
	 */
	@Remark("获取广告 by posCode")
	@RequestMapping(value = "/advert/get/by/posCode", method = RequestMethod.POST)
	@ResponseBody
	public Result<Advert> selectAdvertByPosCode(HttpServletRequest request, @RequestParam("posCode") String posCode) {
		Result<Advert> result = Result.newOne();
		Advert advert = advertService.getAdvertByPosCode(posCode);
		if (advert != null) {
			result.data = advert;
			result.message = "获取成功";
		} else {
			result.message = "暂无广告";
		}
		return result;

	}

	@Remark("根据ecardCode获取活动")
	@RequestMapping(value = "/ecardActRules/get/by/code", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<EcardActRule>> getEcardActRule(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<List<EcardActRule>> result = Result.newOne();
		String code = requestData.getTypedValue("code", String.class);
		if (code != null) {
			List<EcardActRule> ruleList = salePrmtService.getEcardActRulesByFilter(requestData);
			result.data = ruleList;
			result.message = "获取成功";
		} else {
			result.type = Type.warn;
			result.message = "获取失败";
		}
		return result;

	}

}
