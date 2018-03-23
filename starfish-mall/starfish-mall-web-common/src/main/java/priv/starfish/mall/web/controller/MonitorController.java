package priv.starfish.mall.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import priv.starfish.common.annotation.Remark;
import priv.starfish.mall.web.base.BaseController;

@Controller
@Remark("各种系统监控")
@RequestMapping(value = "/monitor")
public class MonitorController extends BaseController {

	@Remark("应用集群监控页面")
	@RequestMapping(value = { "/appCluster/jsp" }, method = RequestMethod.GET)
	public String toClusterPage() {
		return "monitor/appCluster";
	}
}
