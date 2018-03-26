package priv.starfish.mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.JqGridPage;
import priv.starfish.common.model.JqGridRequest;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.service.DistShopService;
import priv.starfish.mall.settle.entity.DistSettleRec;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 卫星店结算记录Controller
 * 
 * @author 李江
 * @date 2016年1月19日 上午10:07:05
 *
 */
@Remark("卫星店结算记录Controller")
@Controller
@RequestMapping("/distSettleRec")
public class DistSettleRecController extends BaseController {

	@Resource
	private DistShopService distShopService;

	/**
	 * 转向卫星店结算记录列表界面
	 * 
	 * @author 李江
	 * @date 2016年1月18日 下午2:50:51
	 * 
	 * @return String
	 */
	@Remark("转向卫星店结算记录列表界面")
	@RequestMapping(value = "/list/jsp", method = RequestMethod.GET)
	public String toWxshopListMall() {
		return "settle/distSettleRecList";
	}

	/**
	 * 分页查询卫星店结算记录
	 * 
	 * @author 李江
	 * @date 2016年1月19日 上午9:59:00
	 * 
	 * @param request
	 * @return JqGridPage<DistShop>
	 */
	@Remark("分页查询卫星店结算记录")
	@RequestMapping(value = "/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<DistSettleRec> listDistShop(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		// 获取登录店铺编号
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntity().getId();
		MapContext filterItems = paginatedFilter.getFilterItems();
		filterItems.put("shopId", shopId);
		PaginatedList<DistSettleRec> paginatedList = distShopService.selectDistSettleRecByFilter(paginatedFilter);
		// 返回数据
		JqGridPage<DistSettleRec> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}
}
