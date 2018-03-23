package priv.starfish.mall.portal.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.Result;
import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.RSACrypter;
import priv.starfish.common.util.WebUtil;
import priv.starfish.mall.order.dict.OrderAction;
import priv.starfish.mall.order.dict.OrderType;
import priv.starfish.mall.order.dto.XOrderActionResult;
import priv.starfish.mall.service.DemoService;
import priv.starfish.mall.service.SettingService;
import priv.starfish.mall.settle.dto.PayDto;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.web.misc.XOrderMessageCacher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/demo")
public class DemoController extends BaseController {
	@Resource
	SettingService settingService;

	@Resource
	DemoService demoService;

	@Remark("加密页面")
	@RequestMapping(value = "/encrypt/jsp", method = RequestMethod.GET)
	public String toEncryptPage() {
		return "demo/encrypt";
	}

	@Remark("加密")
	@RequestMapping(value = "/decrypt/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> toDecrypt(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		//
		String passwordPlain = requestData.getTypedValue("passwordPlain", String.class);
		System.out.println("加密前：" + passwordPlain);

		String password = requestData.getTypedValue("password", String.class);
		System.out.println("加密后：" + password);
		//
		password = RSACrypter.decryptStringFromJs(password);
		System.out.println("解密后：" + password);
		//
		return result;
	}

	// ,
	@RequestMapping(value = "/hiddenForm/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void toDecrypt(HttpServletRequest request, PayDto requestData, HttpServletResponse response) {
		String htmlContent = JsonUtil.toFormattedJson(requestData);
		try {
			WebUtil.responseAsHtml(response, htmlContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Remark("订单信息监控页面")
	@RequestMapping(value = "/xOrder/message/jsp", method = RequestMethod.GET)
	public String xOrderMessagePage() {
		return "demo/xOrder-message";
	}

	@Remark("订单信息拉取示例")
	@RequestMapping(value = "/xOrder/message/get", method = RequestMethod.GET)
	@ResponseBody
	public Result<XOrderActionResult> getXOrderMessage(@RequestParam("orderType") OrderType orderType, @RequestParam("orderNo") String orderNo, @RequestParam("orderAction") OrderAction orderAction) {
		Result<XOrderActionResult> result = Result.newOne();

		result.data = XOrderMessageCacher.getInstance().getOrderActionResult(orderType, orderNo, orderAction);

		return result;
	}

}
