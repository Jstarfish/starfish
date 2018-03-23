package priv.starfish.mall.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import priv.starfish.common.annotation.Remark;
import priv.starfish.mall.web.base.BaseController;

@Controller
@Remark("搜索相关接口")
@RequestMapping(value = "/search")
public class SearchController extends BaseController {

}
