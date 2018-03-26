package priv.starfish.mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import priv.starfish.common.annotation.Remark;

@Controller
@Remark("全局框架性页面导航")
@RequestMapping(value = "/navgt")
public class NavgtController {

	@Remark("多入口选择页面")
	@RequestMapping(value = "/entry/index/jsp", method = RequestMethod.GET)
	public String toEntryIndexPage() {

		return "navgt/entryIndex";
	}

	@Remark("系统后台管理页面")
	@RequestMapping(value = "/sys/root/frame/jsp", method = RequestMethod.GET)
	public String toSysRootFramePage() {

		return "navgt/sysRootFrame";
	}

	@Remark("商城后台管理页面")
	@RequestMapping(value = "/mall/root/frame/jsp", method = RequestMethod.GET)
	public String toMallRootFramePage() {

		return "navgt/mallRootFrame";
	}

	@Remark("店铺后台管理页面")
	@RequestMapping(value = "/shop/root/frame/jsp", method = RequestMethod.GET)
	public String toShopRootFramePage() {

		return "navgt/shopRootFrame";
	}

	@Remark("模块容器页面")
	@RequestMapping(value = "/module/frame/jsp", method = RequestMethod.GET)
	public String toModuleFramePage(@RequestParam("moduleId") Integer moduleId) {

		return "navgt/moduleFrame";
	}

}
