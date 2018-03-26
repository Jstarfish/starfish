package priv.starfish.mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.*;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.service.SocialService;
import priv.starfish.mall.social.entity.UserBlog;
import priv.starfish.mall.social.entity.UserFeedback;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 社交
 * 
 * @author 邓华锋
 * @date 2015年10月13日 下午2:06:33
 *
 */
@Controller
@RequestMapping("/social")
public class SocialController extends BaseController {
	@Resource
	SocialService socialService;

	// -----------------------------------用户博客--------------------------
	/**
	 * 用户博客列表页面
	 * 
	 * @author 邓华锋
	 * @date 2015年10月14日 下午3:44:59
	 * 
	 * @return
	 */
	@Remark("用户博客列表页面")
	@RequestMapping(value = "/blog/list/jsp", method = RequestMethod.GET)
	public String toUserBlogList() {
		return "social/blogList";
	}

	/**
	 * 保存用户博客
	 * 
	 * @author 邓华锋
	 * @date 2015年10月14日 下午3:45:07
	 * 
	 * @param userBlog
	 * @return
	 */
	@Remark("保存用户博客")
	@RequestMapping(value = "/blog/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> saveUserBlog(HttpServletRequest request, @RequestBody UserBlog userBlog) {
		Result<Integer> result = Result.newOne();
		// 获取页面缓存用户信息
		UserContext userContext = getUserContext(request);
		Integer userId = userContext.getUserId();
		if (userContext.isSysUser()) {
			userBlog.setUserId(userId);
			boolean ok = socialService.saveUserBlog(userBlog);
			if (ok) {
				result.data = userBlog.getId();
				result.message = "保存成功";
			} else {
				result.type = Type.warn;
				result.message = "保存失败";
			}
		}
		return result;
	}

	/**
	 * 修改用户博客
	 * 
	 * @author 邓华锋
	 * @date 2015年10月14日 下午3:46:38
	 * 
	 * @param userBlog
	 * @return
	 */
	@Remark("修改用户博客")
	@RequestMapping(value = "/blog/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateUserBlog(HttpServletRequest request, @RequestBody UserBlog userBlog) {
		Result<Integer> result = Result.newOne();
		// 获取页面缓存用户信息
		UserContext userContext = getUserContext(request);
		Integer userId = userContext.getUserId();
		if (userContext.isSysUser()) {
			userBlog.setUserId(userId);
			boolean ok = socialService.updateUserBlog(userBlog);
			if (ok) {
				result.message = "修改成功";
			} else {
				result.type = Type.warn;
				result.message = "修改失败";
			}
		} else {
			result.type = Result.Type.error;
			result.message = "登录超时，请重新登录";
		}

		return result;
	}

	/**
	 * 删除用户博客
	 * 
	 * @author 邓华锋
	 * @date 2015年10月14日 下午3:45:12
	 * 
	 * @param userBlog
	 * @return
	 */
	@Remark("删除用户博客")
	@RequestMapping(value = "/blog/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteUserBlog(HttpServletRequest request, @RequestBody UserBlog userBlog) {
		Result<?> result = Result.newOne();
		// 获取页面缓存用户信息
		UserContext userContext = getUserContext(request);
		Integer userId = userContext.getUserId();
		if (userContext.isSysUser()) {
			userBlog.setUserId(userId);
			boolean ok = socialService.deleteUserBlogById(userBlog.getId());
			if (ok) {
				result.message = "删除成功";
			} else {
				result.type = Type.warn;
				result.message = "删除失败";
			}
		} else {
			result.type = Result.Type.error;
			result.message = "登录超时，请重新登录";
		}

		return result;
	}

	/**
	 * 根据用户博客ID获取用户博客
	 * 
	 * @author 邓华锋
	 * @date 2015年10月14日 下午3:45:16
	 * 
	 * @param userBlog
	 * @return
	 */
	@Remark("根据博客ID获取用户博客")
	@RequestMapping(value = "/blog/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<UserBlog> getUserBlogById(@RequestBody UserBlog userBlog) {
		Result<UserBlog> result = Result.newOne();
		result.data = socialService.getUserBlogById(userBlog.getId());
		return result;
	}

	/**
	 * 用户博客分页查询
	 * 
	 * @author 邓华锋
	 * @date 2015年10月14日 下午3:45:21
	 * 
	 * @param request
	 * @return
	 */
	@Remark("博客分页查询")
	@RequestMapping(value = "/blog/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Object getUserBlogs(HttpServletRequest request) {
		UserContext userContext = getUserContext(request);
		Integer userId = userContext.getUserId();
		if (userContext.isSysUser()) {
			// 封装前台参数为JqGridRequest格式
			JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
			// 封装为PaginatedFilter格式
			PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
			MapContext filter = paginatedFilter.getFilterItems();
			filter.put("userId", userId);// 默认读取当前系统用户发表的博客
			PaginatedList<UserBlog> paginatedList = socialService.getUserBlogsByFilterAndUserId(paginatedFilter, userId);
			JqGridPage<UserBlog> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
			return jqGridPage;
		} else {
			Result<UserBlog> result = Result.newOne();
			result.type = Result.Type.error;
			result.message = "登录超时，请重新登录";
			return result;
		}
	}

	/**
	 * 更新博文审核状态
	 * 
	 * @author 邓华锋
	 * @date 2016年1月12日 下午12:28:24
	 * 
	 * @param request
	 * @param userBlog
	 * @return
	 */
	public Result<Integer> updateUserBlogAuditStatus(HttpServletRequest request, @RequestBody UserBlog userBlog) {
		Result<Integer> result = Result.newOne();
		// 获取页面缓存用户信息
		UserContext userContext = getUserContext(request);
		Integer userId = userContext.getUserId();
		if (userContext.isSysUser()) {
			userBlog.setUserId(userId);
			boolean ok = socialService.updateUserBlogAuditStatus(userBlog);
			if (ok) {
				result.message = "审核成功";
			} else {
				result.type = Type.warn;
				result.message = "审核失败";
			}
		} else {
			result.type = Result.Type.error;
			result.message = "登录超时，请重新登录";
		}

		return result;
	}

	// --------------------------------------------------用户反馈-------------------------------------------------------------
	/**
	 * 用户反馈页面
	 * 
	 * @author 邓华锋
	 * @date 2016年1月12日 下午12:32:42
	 * 
	 * @return
	 */
	@Remark("用户反馈页面")
	@RequestMapping(value = "/userFeedback/list/jsp", method = RequestMethod.GET)
	public String toUserFeedbackList() {
		return "social/userFeedbackList";
	}
	
	/**
	 * 用户反馈保存
	 * 
	 * @author 邓华锋
	 * @date 2016年1月12日 下午12:32:42
	 * 
	 * @return
	 */
	@Remark("用户反馈保存")
	@RequestMapping(value = "/userFeedback/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Long> saveUserFeedback(@RequestBody UserFeedback userFeedback) {
		Result<Long> result = Result.newOne();
		boolean ok = socialService.saveUserFeedback(userFeedback);
		if (ok) {
			result.data = userFeedback.getId();
			result.message = "保存成功";
		} else {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}
	
	/**
	 * 用户反馈修改
	 * 
	 * @author 邓华锋
	 * @date 2016年1月12日 下午12:32:42
	 * 
	 * @return
	 */
	@Remark("用户反馈修改")
	@RequestMapping(value = "/userFeedback/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateUserFeedback(@RequestBody UserFeedback userFeedback) {
		Result<Integer> result = Result.newOne();
		boolean ok = socialService.updateUserFeedback(userFeedback);
		if (ok) {
			result.message = "修改成功";
		} else {
			result.type = Type.warn;
			result.message = "修改失败";
		}
		return result;
	}
	
	/**
	 * 用户反馈删除
	 * 
	 * @author 邓华锋
	 * @date 2016年1月12日 下午12:32:42
	 * 
	 * @return
	 */
	@Remark("用户反馈删除")
	@RequestMapping(value = "/userFeedback/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteUserFeedback(@RequestBody UserFeedback userFeedback) {
		Result<?> result = Result.newOne();
		boolean ok = socialService.deleteUserFeedbackById(userFeedback.getId());
		if (ok) {
			result.message = "删除成功";
		} else {
			result.type = Type.warn;
			result.message = "删除失败";
		}
		return result;
	}
	
	/**
	 * 根据ID获取用户反馈
	 * 
	 * @author 邓华锋
	 * @date 2016年1月12日 下午12:32:42
	 * 
	 * @return
	 */
	@Remark("根据ID获取用户反馈")
	@RequestMapping(value = "/userFeedback/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<UserFeedback> getUserFeedbackById(@RequestBody UserFeedback userFeedback) {
		Result<UserFeedback> result = Result.newOne();
		result.data = socialService.getUserFeedbackById(userFeedback.getId());
		return result;
	}
	
	/**
	 * 用户反馈分页查询
	 * 
	 * @author 邓华锋
	 * @date 2016年1月12日 下午12:32:42
	 * 
	 * @return
	 */
	@Remark("用户反馈分页查询")
	@RequestMapping(value = "/userFeedback/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<UserFeedback> getUserFeedbacks(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		PaginatedList<UserFeedback> paginatedList = socialService.getUserFeedbacksByFilter(paginatedFilter);
		JqGridPage<UserFeedback> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

}
