package priv.starfish.mall.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import priv.starfish.common.annotation.Remark;
import priv.starfish.mall.web.base.BaseController;

@Controller
@Remark("微信相关功能接口")
@RequestMapping(value = "/weixin")
public class WeixinController extends BaseController {

}
