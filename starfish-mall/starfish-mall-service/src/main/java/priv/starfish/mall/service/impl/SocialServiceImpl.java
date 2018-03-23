package priv.starfish.mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.comn.dict.Gender;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.member.entity.Member;
import priv.starfish.mall.order.entity.SaleOrder;
import priv.starfish.mall.order.entity.SaleOrderSvc;
import priv.starfish.mall.service.MemberService;
import priv.starfish.mall.service.SaleOrderService;
import priv.starfish.mall.service.SocialService;
import priv.starfish.mall.service.UserService;
import priv.starfish.mall.dao.social.UserBlogCommentDao;
import priv.starfish.mall.dao.social.UserBlogDao;
import priv.starfish.mall.dao.social.UserBlogImgDao;
import priv.starfish.mall.dao.social.UserFeedbackDao;
import priv.starfish.mall.social.entity.UserBlog;
import priv.starfish.mall.social.entity.UserBlogComment;
import priv.starfish.mall.social.entity.UserBlogImg;
import priv.starfish.mall.social.entity.UserFeedback;

@Service("blogService")
public class SocialServiceImpl extends BaseServiceImpl implements SocialService {
	
	@Resource
	UserBlogDao userBlogDao;
	
	@Resource
	UserBlogCommentDao userBlogCommentDao;
	
	@Resource
	UserBlogImgDao userBlogImgDao;
	
	@Resource
	SaleOrderService saleOrderService;
	
	@Resource
	UserService userService;
	
	@Resource
	MemberService memberService;
	
	@Resource
	UserFeedbackDao userFeedbackDao;

	// --------------------------------用户博客------------------------------------
	@Override
	public boolean saveUserBlog(UserBlog userBlog) {
		boolean bl = userBlogDao.insert(userBlog) > 0;
		if (bl) {
			UserBlogImg ubi = userBlog.getUserBlogImg();
			if (userBlog.getUserBlogImg() != null) {
				ubi.setBlogId(userBlog.getId());
				userBlogImgDao.insert(userBlog.getUserBlogImg());
			}
		}
		return bl;
	}

	@Override
	public boolean deleteUserBlogById(Integer id) {
		UserBlog userBlog = userBlogDao.selectById(id);
		UserBlogImg userBlogImg = userBlog.getUserBlogImg();
		if (userBlogImg != null) {// 如果有图片信息，则删除图片
			userBlogImgDao.deleteById(userBlogImg.getId());
		}
		return userBlogDao.deleteById(id) > 0;
	}

	@Override
	public boolean deleteUserBlogByIdAndUserId(Integer id, Integer userId) {
		UserBlog userBlog = userBlogDao.selectByIdAndUserId(id, userId);
		if (userBlog != null) {// 判断此用户的此博文是否存在
			UserBlogImg userBlogImg = userBlog.getUserBlogImg();
			if (userBlogImg != null) {// 如果有图片信息，则删除图片
				userBlogImgDao.deleteById(userBlogImg.getId());
			}
			return userBlogDao.deleteById(id) > 0;
		}
		return false;
	}

	@Override
	public boolean updateUserBlog(UserBlog userBlog) {
		boolean bl = userBlogDao.update(userBlog) > 0;
		if (bl) {
			UserBlogImg ubi = userBlog.getUserBlogImg();
			if (userBlog.getUserBlogImg() != null) {
				ubi.setBlogId(userBlog.getId());
				userBlogImgDao.update(userBlog.getUserBlogImg());
			}
		}
		return bl;
	}

	@Override
	public boolean updateUserBlogAuditStatus(UserBlog userBlog) {
		return userBlogDao.updateAuditStatus(userBlog) > 0;
	}

	@Override
	public PaginatedList<UserBlog> getUserBlogsByFilterAndUserId(PaginatedFilter paginatedFilter, Integer userId) {
		MapContext filterItems = paginatedFilter.getFilterItems();
		if (filterItems == null) {
			filterItems = new MapContext();
		}
		if (userId != null) {
			filterItems.put("userId", userId);
		}
		return getBlogsByFilter(paginatedFilter, true);
	}

	@Override
	public PaginatedList<UserBlog> getShareBlogsByFilter(PaginatedFilter paginatedFilter) {
		MapContext filterItems = paginatedFilter.getFilterItems();
		if (filterItems == null) {
			filterItems = new MapContext();
		}
		// 分享列表里，不能按用户ID查询，保护用户隐私
		filterItems.remove("userId");
		filterItems.put("published", true);// 查看已发布的文章
		return getBlogsByFilter(paginatedFilter, true);
	}

	@Override
	public UserBlog getUserBlogById(Integer id) {
		UserBlog userBlog = userBlogDao.selectById(id);
		if (userBlog != null) {
			// 订单信息
			setUserBlogAssociatedData(userBlog, true,false);
		}
		return userBlog;
	}

	@Override
	public UserBlog getUserBlogByIdAndUserId(Integer blogId, Integer userId) {
		UserBlog userBlog = userBlogDao.selectByIdAndUserId(blogId, userId);
		setUserBlogAssociatedData(userBlog, true,true);
		return userBlog;
	}
	
	@Override
	public boolean deleteFalseUserBlog(Integer id, Integer userId) {
		UserBlog userBlog = userBlogDao.selectByIdAndUserId(id, userId);
		if(userBlog.isDeleted()){
			return true;
		}else{
			userBlog.setDeleted(true);
			return userBlogDao.update(userBlog) > 0;
		}
	}
	
	@Override
	public boolean userBlogToDeliver(Integer id, Integer userId) {
		UserBlog userBlog = userBlogDao.selectByIdAndUserId(id, userId);
		if(userBlog.isPublished()){
			return true;
		}else{
			userBlog.setPublished(true);
			return userBlogDao.update(userBlog) > 0;
		}
	}

	@Override
	public int getBlogDraftCountByUserId(Integer userId) {
		return userBlogDao.selectBlogDraftCountByUserId(userId);
	}
	
	@Override
	public int getBlogDeliverCountByUserId(Integer userId) {
		return userBlogDao.getBlogDeliverCountByUserId(userId);
	}

	// -------------------------------------博客处理私有方法------------------------------------
	/**
	 * 获取博客分页列表
	 * 
	 * @author 邓华锋
	 * @date 2015年10月26日 下午5:00:00
	 * 
	 * @param paginatedFilter
	 * @param isRmSensInfo
	 *            是否去除用户和订单的敏感信息
	 * @return
	 */
	private PaginatedList<UserBlog> getBlogsByFilter(PaginatedFilter paginatedFilter, boolean isRmSensInfo) {
		MapContext filter = paginatedFilter.getFilterItems();
		if (filter != null) {
			if (StrUtil.hasText(paginatedFilter.getKeywords())) {
				filter.put("keywords", paginatedFilter.getKeywords());
			}
		}
		PaginatedList<UserBlog> paginatedList = userBlogDao.selectByFilter(paginatedFilter);
		List<UserBlog> userBLogList = paginatedList.getRows();
		if (userBLogList.size() > 0) {
			for (UserBlog userBlog : userBLogList) {
				setUserBlogAssociatedData(userBlog, isRmSensInfo,false);
			}
		}
		return paginatedList;
	}

	/**
	 * 设置博客关联对象的数据：订单、用户和图片
	 * 
	 * @author 邓华锋
	 * @date 2015年10月21日 下午7:15:17
	 * 
	 * @param userBlog
	 * @param isRmSensInfo
	 *            是否去除用户和订单的敏感信息
	 */
	private void setUserBlogAssociatedData(UserBlog userBlog, boolean isRmSensInfo,boolean isSetOrderNo) {
		if (userBlog != null) {
			User user = null;
			// 订单
			if (userBlog.getOrderId() != null && Long.valueOf(userBlog.getOrderId()) != 0) {
				SaleOrder saleOrder = saleOrderService.getSaleOrderById(userBlog.getOrderId());
				if (saleOrder != null) {
					if (isRmSensInfo) {
						String no=saleOrder.getNo();
						saleOrder = getRmSensInforSaleOrder(saleOrder);
						if(isSetOrderNo){
							saleOrder.setNo(no);
						}
					}
					user = saleOrder.getCustomer();// 订单的用户信息
					userBlog.setSaleOrder(saleOrder);
				}
			}

			// 用户
			if (user == null && userBlog.getUserId() != null && Integer.valueOf(userBlog.getUserId()) != 0) {
				user = userService.getUserById(userBlog.getUserId());
			}
			// 获取发表者头像
			Member member = memberService.getMemberById(user.getId());
			userBlog.setHeadFileBrowseUrl(member.getFileBrowseUrl());

			// 是否去除敏感信息
			if (isRmSensInfo) {
				user = getRmSensInforUser(user);
			}
			userBlog.setUser(user);

			// 图片
			UserBlogImg userBlogImg = userBlogImgDao.selectByBlogId(userBlog.getId());

			setFileBrowseUrl(userBlogImg);// 设置图片链接地址
			userBlog.setUserBlogImg(userBlogImg);
			// 获取评论数
			userBlog.setCommentCount(userBlogCommentDao.selectCountByBlogId(userBlog.getId()));
		}
	}

	/**
	 * 去除订单敏感信息,只保留要用到的字段
	 * 
	 * @author 邓华锋
	 * @date 2015年10月21日 下午2:58:34
	 * 
	 * @param saleOrder
	 * @return
	 */
	private SaleOrder getRmSensInforSaleOrder(SaleOrder saleOrder) {
		SaleOrder simpleOrder = new SaleOrder();
		if (saleOrder != null) {
			simpleOrder.setShopId(saleOrder.getShopId());
			simpleOrder.setShopName(saleOrder.getShopName());
			simpleOrder.setCarModel(saleOrder.getCarModel());
			simpleOrder.setCarName(saleOrder.getCarName());
			simpleOrder.setFinishTime(saleOrder.getFinishTime());
			List<SaleOrderSvc> saleOrderSvcs = saleOrder.getSaleOrderSvcs();
			if (saleOrderSvcs != null && saleOrderSvcs.size() > 0) {
				// 服务项目名称
				List<SaleOrderSvc> simpleSaleOrderSvcs = new ArrayList<SaleOrderSvc>();
				for (SaleOrderSvc saleOrderSvc : saleOrderSvcs) {
					SaleOrderSvc simpleSaleOrderSvc = new SaleOrderSvc();
					simpleSaleOrderSvc.setSvcName(saleOrderSvc.getSvcName());
					simpleSaleOrderSvcs.add(simpleSaleOrderSvc);
				}
				simpleOrder.setSaleOrderSvcs(simpleSaleOrderSvcs);
			}
			saleOrder = simpleOrder;
		}
		return saleOrder;
	}

	/**
	 * 去除用户敏感信息
	 * 
	 * @author 邓华锋
	 * @date 2015年10月21日 下午7:12:51
	 * 
	 * @param user
	 * @return
	 */
	private User getRmSensInforUser(User user) {
		if (user == null || user.getId() == null || Integer.valueOf(user.getId()) == 0) {
			return null;
		}
		User simpleUser = new User();

		String phoneNo = user.getPhoneNo();
		if (StrUtil.hasText(phoneNo)) {
			phoneNo = phoneNo.substring(0, 3) + "****" + phoneNo.substring(7);
			simpleUser.setPhoneNo(phoneNo);
		}

		String realName = user.getRealName();
		if (StrUtil.hasText(realName)) {
			String x = realName.substring(0, 1);
			if (user.getGender() == Gender.M) {
				realName = x + "先生";
			} else if (user.getGender() == Gender.F) {
				realName = x + "女士";
			} else if (user.getGender() == Gender.X) {
				int len = realName.length() - 1;
				realName = x;
				String str = "";
				for (int i = 0; i < len; i++) {
					str += "x";
				}
				realName += str;
			}
			simpleUser.setRealName(realName);
		}

		simpleUser.setNickName(user.getNickName());
		return simpleUser;
	}

	/**
	 * 获取图片实际路径
	 * 
	 * @author 邓华锋
	 * @date 2015年10月22日 上午10:34:51
	 * 
	 * @param userBlogImg
	 */
	private void setFileBrowseUrl(UserBlogImg userBlogImg) {
		if (userBlogImg != null) {
			String fileBrowseUrl = fileRepository.getFileBrowseUrl(userBlogImg.getImageUsage(), userBlogImg.getImagePath());
			if (StrUtil.isNullOrBlank(fileBrowseUrl)) {
				fileBrowseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
			}
			userBlogImg.setFileBrowseUrl(fileBrowseUrl);
		}
	}

	// ---------------------------------------博客评论--------------------------------------------
	@Override
	public boolean saveUserBlogComment(UserBlogComment userBlogComment) {
		return userBlogCommentDao.insert(userBlogComment) > 0;
	}

	@Override
	public boolean deleteUserBlogCommentById(Integer id) {
		return userBlogCommentDao.deleteById(id) > 0;
	}

	@Override
	public boolean updateUserBlogComment(UserBlogComment userBlogComment) {
		return userBlogCommentDao.update(userBlogComment) > 0;
	}

	@Override
	public PaginatedList<UserBlogComment> getUserBlogCommentsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<UserBlogComment> paginatedList = userBlogCommentDao.selectByFilter(paginatedFilter);
		List<UserBlogComment> list = paginatedList.getRows();
		if(list != null && !list.isEmpty()){
			for (UserBlogComment userBlogComment : list) {
				Member member = memberService.getMemberById(userBlogComment.getUserId());
				userBlogComment.setHeadFileBrowseUrl(member.getFileBrowseUrl());
			}
		}
		return paginatedList;
	}

	@Override
	public UserBlogComment getUserBlogCommentById(Integer id) {
		return userBlogCommentDao.selectById(id);
	}

	// -------------------------------------------用户反馈------------------------------
	// -----------------------------------------------------------------------------------
	@Override
	public boolean saveUserFeedback(UserFeedback userFeedback) {
		return userFeedbackDao.insert(userFeedback) > 0;
	}

	@Override
	public boolean deleteUserFeedbackById(Long id) {
		return userFeedbackDao.deleteById(id) > 0;
	}

	@Override
	public boolean updateUserFeedback(UserFeedback userFeedback) {
		return userFeedbackDao.update(userFeedback) > 0;
	}

	@Override
	public PaginatedList<UserFeedback> getUserFeedbacksByFilter(PaginatedFilter paginatedFilter) {
		return userFeedbackDao.selectByFilter(paginatedFilter);
	}

	@Override
	public UserFeedback getUserFeedbackById(Long id) {
		return userFeedbackDao.selectById(id);
	}
}
