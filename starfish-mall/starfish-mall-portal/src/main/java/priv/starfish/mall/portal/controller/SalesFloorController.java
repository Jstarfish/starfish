package priv.starfish.mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.Result;
import priv.starfish.mall.market.entity.SalesRegion;
import priv.starfish.mall.service.SalesFloorService;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;
import java.util.List;

@Remark("楼层相关处理")
@Controller
@RequestMapping(value = "/salesFloor")
public class SalesFloorController extends BaseController {

	@Resource
	private SalesFloorService salesFloorService;
	
	/**
	 * 查询销售专区
	 * 
	 * @author 郝江奎
	 * @date 2016年2月3日 上午10:41:54
	 * 
	 * @return
	 */
	@Remark("查询销售专区")
	@RequestMapping(value = "/salesRegion/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<SalesRegion>> getSalesRegion(@RequestBody SalesRegion salesRegion) {
		Result<List<SalesRegion>> result = Result.newOne();
		// 销售专区
		List<SalesRegion> salesRegionList = salesFloorService.getSalesRegionListByType(salesRegion.getType());
		result.data = salesRegionList;
		return result;
	}
	
}
