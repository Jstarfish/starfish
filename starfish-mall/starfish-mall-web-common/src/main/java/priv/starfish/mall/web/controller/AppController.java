package priv.starfish.mall.web.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.model.Result;
import priv.starfish.mall.web.base.BaseController;

import java.util.List;

@Controller
@RequestMapping(value = "/app", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppController extends BaseController {

	// ---------------------------------------------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	Result<List<?>> getAllApps() {
		Result<List<?>> result = Result.newOne();
		result.message = "什么也没有，你上当了！";
		return result;
	}
}
