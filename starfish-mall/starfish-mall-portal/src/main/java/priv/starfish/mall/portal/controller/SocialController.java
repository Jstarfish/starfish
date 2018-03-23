package priv.starfish.mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.comn.dict.AppType;
import priv.starfish.mall.comn.dict.UserType;
import priv.starfish.mall.order.dict.HandleFlag;
import priv.starfish.mall.service.SocialService;
import priv.starfish.mall.social.dic.SubjectType;
import priv.starfish.mall.social.entity.UserBlog;
import priv.starfish.mall.social.entity.UserBlogComment;
import priv.starfish.mall.social.entity.UserFeedback;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 社交
 * 
 * @author 邓华锋
 * @date 2015年10月27日 上午9:34:52
 *
 */
@Controller
@RequestMapping("/social")
public class SocialController extends BaseController {

	@Resource
	SocialService socialService;

	// -----------------------------------车友分享博客--------------------------
	/**
	 * 博客列表页面
	 * 
	 * @author 邓华锋
	 * @date 2015年10月14日 下午3:44:59
	 * 
	 * @return
	 */
	@Remark("博客列表页面")
	@RequestMapping(value = "/blog/list/jsp", method = RequestMethod.GET)
	public String toUserBlogList() {
		return "social/blogList";
	}

	/**
	 * 根据博客ID获取博客
	 * 
	 * @author 邓华锋
	 * @date 2015年10月14日 下午3:45:16
	 * 
	 * @return
	 */
	@Remark("根据博客ID获取博客")
	@RequestMapping(value = "/blog/detail/jsp", method = RequestMethod.GET)
	public String toUserBlogDetail() {
		return "social/blogDetail";
	}

	/**
	 * 根据博客ID获取博客
	 * 
	 * @author 邓华锋
	 * @date 2015年10月14日 下午3:45:16
	 * 
	 * @param userBlog
	 * @return
	 */
	@Remark("根据博客ID获取博客")
	@RequestMapping(value = "/blog/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<MapContext> getUserBlogById(HttpServletRequest request, @RequestBody UserBlog userBlog) {
		Result<MapContext> result = Result.newOne();
		MapContext mapContext = new MapContext();
		mapContext.put("blog", socialService.getUserBlogById(userBlog.getId()));
		mapContext.put("userContext", getUserContext(request));
		result.data = mapContext;
		return result;
	}

	/**
	 * 博客分页查询
	 * 
	 * @author 邓华锋
	 * @date 2015年10月14日 下午3:45:21
	 * 
	 * @return
	 */
	@Remark("博客分页查询")
	@RequestMapping(value = "/blog/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<PaginatedList<UserBlog>> getShareBlogs(@RequestBody PaginatedFilter paginatedFilter) {
		Result<PaginatedList<UserBlog>> result = Result.newOne();
		PaginatedList<UserBlog> paginatedList = socialService.getShareBlogsByFilter(paginatedFilter);
		result.data = paginatedList;
		return result;
	}

	// -----------------------------用户中心的博客处理------------------------------
	/**
	 * 保存博客私有方法
	 * 
	 * @author 邓华锋
	 * @date 2015年10月28日 上午9:45:15
	 * 
	 * @param request
	 * @param userBlog
	 * @param action
	 *            动作
	 * @return
	 */
	private Result<Integer> saveBlog(HttpServletRequest request, UserBlog userBlog, String action) {
		Result<Integer> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		// 获取页面缓存用户信息
		Integer userId = userContext.getUserId();
		if (userId != null && userContext.isAuthenticated()) {
			userBlog.setUserId(userId);
			boolean ok = false;
			if (userBlog.getId() != null) {
				ok = socialService.updateUserBlog(userBlog);
			} else {
				ok = socialService.saveUserBlog(userBlog);

			}
			if (ok) {
				result.data = userBlog.getId();
				result.message = action + "成功";
			} else {
				result.type = Type.warn;
				result.message = action + "失败";
			}
		}
		return result;
	}

	@Remark("发布博客")
	@RequestMapping(value = "/user/blog/publish/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> publishUserBlog(HttpServletRequest request, @RequestBody UserBlog userBlog) {
		userBlog.setPublished(true);
		return saveBlog(request, userBlog, "发布");
	}

	@Remark("保存为草稿")
	@RequestMapping(value = "/user/blog/draft/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> saveDraftUserBlog(HttpServletRequest request, @RequestBody UserBlog userBlog) {
		userBlog.setPublished(false);
		return saveBlog(request, userBlog, "保存草稿");
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
	@RequestMapping(value = "/user/blog/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> saveUserBlog(HttpServletRequest request, @RequestBody UserBlog userBlog) {
		return saveBlog(request, userBlog, "保存");
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
	@RequestMapping(value = "/user/blog/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteUserBlog(HttpServletRequest request, @RequestBody UserBlog userBlog) {
		Result<?> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		// 获取页面缓存用户信息
		Integer userId = userContext.getUserId();
		if (userId != null && userContext.isAuthenticated()) {
			userBlog.setUserId(userId);
			boolean ok = socialService.deleteUserBlogByIdAndUserId(userBlog.getId(), userId);
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
	 * 根据博客ID获取博客
	 * 
	 * @author 邓华锋
	 * @date 2015年10月14日 下午3:45:16
	 * 
	 * @param userBlog
	 * @return
	 */
	@Remark("根据博客ID和用户ID获取博客")
	@RequestMapping(value = "/user/blog/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<UserBlog> getUserBlogByIdAndUserId(HttpServletRequest request, @RequestBody UserBlog userBlog) {
		Result<UserBlog> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		result.data = socialService.getUserBlogByIdAndUserId(userBlog.getId(), userContext.getUserId());
		return result;
	}

	/**
	 * 获取用户博客草稿数
	 * 
	 * @author 邓华锋
	 * @date 2015年10月27日 下午7:35:37
	 * 
	 * @return
	 */
	@Remark("获取用户博客草稿数")
	@RequestMapping(value = "/user/blog/draft/count/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> getBlogDraftCount(HttpServletRequest request) {
		Result<Integer> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		result.data = socialService.getBlogDraftCountByUserId(userContext.getUserId());
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
	@Remark("用户博客分页查询")
	@RequestMapping(value = "/user/blog/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<PaginatedList<UserBlog>> getUserBlogs(HttpServletRequest request, @RequestBody PaginatedFilter paginatedFilter) {
		Result<PaginatedList<UserBlog>> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		PaginatedList<UserBlog> paginatedList = socialService.getUserBlogsByFilterAndUserId(paginatedFilter, userContext.getUserId());
		result.data = paginatedList;
		return result;
	}

	// ----------------------------------评论------------------------------
	/**
	 * 发表博客评论
	 * 
	 * @author 邓华锋
	 * @date 2015年10月14日 下午7:07:24
	 * 
	 * @param request
	 * @param userBlogComment
	 * @return
	 */
	@Remark("发表博客评论")
	@RequestMapping(value = "/blog/comment/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> saveUserBlogComment(HttpServletRequest request, @RequestBody UserBlogComment userBlogComment) {
		Result<Integer> result = Result.newOne();
		UserBlog userBlog = socialService.getUserBlogById(userBlogComment.getBlogId());
		if (userBlog != null) {
			UserContext userContext = getUserContext(request);
			if (userContext.getUserId() != null) {
				int userId = userContext.getUserId();
				if (userContext.isAuthenticated()) {// 认证是否登录
					userBlogComment.setUserId(userId);
					userBlogComment.setUserName(userContext.getUserName());
				} else {
					result.type = Result.Type.error;
					result.message = "此博文需账号认证通过后才能评论";
					return result;
				}
			} else {
				result.type = Result.Type.error;
				result.message = "此博文需要登录后才能评论";
				return result;
			}
			/*
			 * if (!userBlog.getAllowAnony()) {// 是否允许匿名评论
			 * 
			 * }
			 */

			boolean ok = socialService.saveUserBlogComment(userBlogComment);
			if (ok) {
				result.data = userBlogComment.getId();
				result.message = "发表评论成功";
			} else {
				result.type = Type.warn;
				result.message = "发表评论失败";
			}
		} else {
			result.type = Type.warn;
			result.message = "没有此博文，发表评论失败";
		}
		return result;
	}

	/**
	 * 博客评论分页
	 * 
	 * @author 邓华锋
	 * @date 2015年10月14日 下午7:07:32
	 * 
	 * @return
	 */
	@Remark("分页查询博客评论")
	@RequestMapping(value = "/blog/comment/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<PaginatedList<UserBlogComment>> getUserBlogComments(@RequestBody PaginatedFilter paginatedFilter) {
		Result<PaginatedList<UserBlogComment>> result = Result.newOne();
		PaginatedList<UserBlogComment> paginatedList = socialService.getUserBlogCommentsByFilter(paginatedFilter);
		result.data = paginatedList;
		return result;
	}

	/**
	 * 发表意见反馈
	 * 
	 * @author 邓华锋
	 * @date 2016年1月27日 上午10:39:46
	 * 
	 * @param request
	 * @param userFeedback
	 * @return
	 */
	@Remark("发表意见反馈")
	@RequestMapping(value = "/user/feedBack/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Long> saveUserFeedback(HttpServletRequest request, @RequestBody UserFeedback userFeedback) {
		Result<Long> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		if (userContext.getUserId() != null) {
			int userId = userContext.getUserId();
			if (userContext.isAuthenticated()) {// 认证是否登录
				userFeedback.setUserId(userId);
				userFeedback.setUserName(userContext.getUserName());
				userFeedback.setUserType(UserType.member.getValue());
				userFeedback.setAppType(AppType.web.name());
				userFeedback.setSubject(SubjectType.webapp.name());
				userFeedback.setHandleFlag(HandleFlag.untreated.getValue());
				if (socialService.saveUserFeedback(userFeedback)) {
					result.data = userFeedback.getId();
				}
				result.message = "提交意见反馈成功";
			} else {
				result.type = Result.Type.error;
				result.message = "需账号认证通过后才能反馈";
				return result;
			}
		} else {
			result.type = Result.Type.error;
			result.message = "需要登录后才能反馈";
			return result;
		}
		return result;
	}
	
	/**
	 * 分页查询用户的意见反馈
	 * 
	 * @author 邓华锋
	 * @date 2016年1月27日 上午10:41:44
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	@Remark("分页查询用户的意见反馈")
	@RequestMapping(value = "/user/feedBack/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<PaginatedList<UserFeedback>> getUserFeedBacks(HttpServletRequest request, @RequestBody PaginatedFilter paginatedFilter) {
		Result<PaginatedList<UserFeedback>> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		if (userContext.getUserId() != null) {
			paginatedFilter.getFilterItems().put("userId", userContext.getUserId());
		}
		PaginatedList<UserFeedback> paginatedList = socialService.getUserFeedbacksByFilter(paginatedFilter);
		result.data = paginatedList;
		return result;
	}
	// ------------------------------------------------------------------------------
}
