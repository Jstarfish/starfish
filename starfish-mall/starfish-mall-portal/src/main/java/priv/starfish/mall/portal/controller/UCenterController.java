package priv.starfish.mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import priv.starfish.common.annotation.Remark;
import priv.starfish.mall.service.SaleOrderService;
import priv.starfish.mall.service.SocialService;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户中心(只存放用户中心的页面跳转)
 * 
 * @author 郝江奎
 * @date 2015年10月22日 下午2:26:18
 *
 */
@Controller
@RequestMapping("/ucenter")
public class UCenterController extends BaseController {
	@Resource
	SocialService socialService;
	@Resource
	SaleOrderService saleOrderService;

	/**
	 * 用户中心首页页面
	 * 
	 * @author 郝江奎
	 * @date 2015年11月3日 下午5:44:11
	 * 
	 * @return
	 */
	@Remark("用户中心首页页面")
	@RequestMapping(value = "/index/jsp", method = RequestMethod.GET)
	public String toUcenterIndex() {
		return "ucenter/index";
	}

	// ---------------------------------------用户基本信息---------------------------
	/**
	 * 用户基本信息页面
	 * 
	 * @author 郝江奎
	 * @date 2015年11月3日 下午5:44:11
	 * 
	 * @return
	 */
	@Remark("用户基本信息页面")
	@RequestMapping(value = "/user/info/jsp", method = RequestMethod.GET)
	public String toUserInfo() {
		return "ucenter/user/info";
	}

	/**
	 * 用户常用联系方式
	 * 
	 * @author 郝江奎
	 * @date 2015年11月4日 上午11:44:11
	 * 
	 * @return
	 */
	@Remark("用户常用联系方式")
	@RequestMapping(value = "/user/linkWay/jsp", method = RequestMethod.GET)
	public String toUserLinkWay() {
		return "ucenter/user/linkWay";
	}

	/**
	 * 用户安全中心
	 * 
	 * @author 郝江奎
	 * @date 2015年11月4日 上午11:44:11
	 * 
	 * @return
	 */
	@Remark("用户安全中心")
	@RequestMapping(value = "/user/safe/jsp", method = RequestMethod.GET)
	public String toUserSafe() {
		return "ucenter/user/safe";
	}

	// ---------------------------------------用户中心博客---------------------------
	/**
	 * 用户博客列表页面
	 * 
	 * @author 邓华锋
	 * @date 2015年10月22日 上午11:44:11
	 * 
	 */
	@Remark("用户博客列表页面")
	@RequestMapping(value = "/blog/list/jsp", method = RequestMethod.GET)
	public String toSocialBlogList() {
		return "ucenter/social/blogList";
	}

	/**
	 * 发表博文
	 * 
	 * @author 邓华锋
	 * @date 2015年10月22日 下午2:27:02
	 * 
	 * @return
	 */
	@Remark("发表博文页面")
	@RequestMapping(value = "/blog/write/jsp", method = RequestMethod.GET)
	public String toSocialBlogWrite() {
		return "ucenter/social/blogWrite";
	}

	/**
	 * 
	 * 
	 * @author 郝江奎
	 * @date 2015年10月27日 下午3:40:18
	 * 
	 * @return
	 */
	@Remark("销售订单列表页面")
	@RequestMapping(value = "/saleOrder/list/jsp", method = RequestMethod.GET)
	public String toSaleOrderList(HttpServletRequest request) {
		return "ucenter/order/saleOrder";
	}

	/**
	 * 用户E卡列表页面
	 * 
	 * @author wangdi
	 * @date 2015年11月2日 上午9:52:26
	 * 
	 * @param request
	 * @return
	 */
	@Remark("用户E卡列表页面")
	@RequestMapping(value = "/ecard/list/jsp", method = RequestMethod.GET)
	public String toECardList(HttpServletRequest request) {
		return "ucenter/ecard/ecardList";
	}

	/**
	 * 用户E卡列表页面
	 * 
	 * @author wangdi
	 * @date 2015年11月2日 上午9:52:26
	 * 
	 * @param request
	 * @return
	 */
	@Remark("用户E卡订单列表页面")
	@RequestMapping(value = "/ecardOrder/list/jsp", method = RequestMethod.GET)
	public String toECardOrderList(HttpServletRequest request) {
		return "ucenter/order/ecardOrderList";
	}

	/**
	 * 用户车辆列表页面
	 * 
	 * @author 郝江奎
	 * @date 2016年1月25日 下午3:35:24
	 * 
	 * @param request
	 * @return
	 */
	@Remark("用户车辆列表页面")
	@RequestMapping(value = "/userCar/list/jsp", method = RequestMethod.GET)
	public String toUserCarList(HttpServletRequest request) {
		return "ucenter/car/userCar";
	}

	/**
	 * 服务套餐票
	 * 
	 * @author 邓华锋
	 * @date 2016年1月27日 上午12:29:05
	 * 
	 * @param request
	 * @return
	 */
	@Remark("服务套餐票列表页面")
	@RequestMapping(value = "/userSvcPackTick/list/jsp", method = RequestMethod.GET)
	public String toUserSvcPackTickList(HttpServletRequest request) {
		return "ucenter/order/userSvcPackTickList";
	}

	/**
	 * 意见反馈
	 * 
	 * @author 邓华锋
	 * @date 2016年1月27日 上午12:29:05
	 * 
	 * @param request
	 * @return
	 */
	@Remark("意见反馈页面")
	@RequestMapping(value = "/userFeedback/list/jsp", method = RequestMethod.GET)
	public String toUserFeedback(HttpServletRequest request) {
		return "ucenter/feedback/list";
	}
}
