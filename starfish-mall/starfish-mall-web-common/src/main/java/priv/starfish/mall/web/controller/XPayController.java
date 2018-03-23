package priv.starfish.mall.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.Result;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.web.base.BaseController;

@Controller
@Remark("支付相关接口（微信、支付宝等）")
@RequestMapping(value = "/xpay")
public class XPayController extends BaseController {

	@Remark("微信订单支付")
	@RequestMapping(value = { "/weixin/order" }, method = RequestMethod.POST)
	@ResponseBody
	public Result<?> getDemoData(@RequestBody MapContext requestData) {
		System.out.println(requestData);

		Result<?> result = Result.newOne();
		//
		return result;
	}
}
