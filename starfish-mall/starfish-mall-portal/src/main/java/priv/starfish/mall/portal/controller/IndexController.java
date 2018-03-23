package priv.starfish.mall.portal.controller;/**
 * Created by starfish on 2017/12/14.
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页
 * created by starfish on 2017-12-14
 */
@Controller
@RequestMapping("/")
public class IndexController {

    public String index(){
        return "index";
    }

}
