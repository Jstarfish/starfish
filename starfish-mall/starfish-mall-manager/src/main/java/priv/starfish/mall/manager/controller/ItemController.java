/*
package priv.starfish.mall.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.mall.manager.pojo.MallItem;

import java.util.List;

*/
/**
 * Created by starfish on 2017/10/17.
 *//*

@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/list")
    public String itemList(){
        return "item/itemList";
    }

    @ResponseBody
    @RequestMapping("/getItemList")
    public Object listUsers(MallItem form) {
        List<MallItem> itemList = itemService.getItemList(form);
        return itemList;
    }
}
*/
