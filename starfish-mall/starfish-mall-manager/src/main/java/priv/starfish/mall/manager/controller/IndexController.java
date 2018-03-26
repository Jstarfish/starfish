package priv.starfish.mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 页面跳转
 * Created by starfish on 2017/10/18.
 */
@Controller
@RequestMapping("index")
public class IndexController {

    @RequestMapping("/index")
    public String showIndex(){
        return "index";
    }

    //展示其他页面
   /* @RequestMapping("/{page}")
    public String showpage(@PathVariable String page) {
        return page;
    }*/

    @RequestMapping(params="method=logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        try {
            request.getSession().invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "login";
    }

    @RequestMapping
    public String index(HttpServletRequest request, HttpServletResponse response) {
        return "index";

    }
}
