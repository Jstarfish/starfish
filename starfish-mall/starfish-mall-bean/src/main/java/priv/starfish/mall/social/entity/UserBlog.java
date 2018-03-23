package priv.starfish.mall.social.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.order.entity.SaleOrder;

/**
 * 
 * @author 邓华锋
 * @date 2015年10月13日 18:41:02
 *
 */
@Table(name = "user_blog")
public class UserBlog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(name = "orderId", nullable = true, type = Types.BIGINT, desc = "销售订单Id（可为空）")
	private Long orderId;

	@Column(name = "userId", nullable = false, type = Types.INTEGER, desc = "用户Id")
	@ForeignKey(refEntityClass = User.class, refFieldName = "id")
	private Integer userId;

	@Column(name = "title", nullable = false, type = Types.VARCHAR, length = 30, desc = "标题")
	private String title;

	@Column(name = "content", nullable = false, type = Types.CLOB, desc = "内容")
	private String content;

	@Column(name = "allowAnony", nullable = true, type = Types.BOOLEAN, desc = "是否允许匿名（未登录）评论")
	private boolean allowAnony;

	@Column(name = "createTime", nullable = false, type = Types.TIMESTAMP, desc = "发表时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date createTime;

	@Column(name = "changeTime", nullable = false, type = Types.TIMESTAMP, desc = "变更时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date changeTime;

	@Column(name = "indexTime", type = Types.TIMESTAMP, desc = "索引时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date indexTime;

	@Column(name = "published", nullable = true, type = Types.BOOLEAN, desc = "是否发布", defaultValue = "0")
	private boolean published;

	@Column(name = "auditStatus", nullable = true, type = Types.INTEGER, desc = "审核状态", defaultValue = "0")
	private Integer auditStatus;
	
	@Column(name = "deleted", nullable = true, type = Types.BOOLEAN, desc = "是否删除", defaultValue = "0")
	private boolean deleted;

	private SaleOrder saleOrder;

	private User user;

	/**
	 * 发表者头像
	 */

	private String headFileBrowseUrl;

	/**
	 * 博客评论数
	 */
	private int commentCount;

	/**
	 * 博客图片
	 */
	private UserBlogImg userBlogImg;

	/**
	 * 博客图片列表
	 */
	private List<UserBlogImg> userBlogImgs;

	/**
	 * 
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 销售订单Id（可为空）
	 */
	public Long getOrderId() {
		return orderId;
	}

	/**
	 * 销售订单Id（可为空）
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	/**
	 * 用户Id
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * 用户Id
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * 标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 是否允许匿名（未登录）评论
	 */
	public boolean getAllowAnony() {
		return allowAnony;
	}

	/**
	 * 是否允许匿名（未登录）评论
	 */
	public void setAllowAnony(boolean allowAnony) {
		this.allowAnony = allowAnony;
	}

	public Date getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}

	public Date getIndexTime() {
		return indexTime;
	}

	public void setIndexTime(Date indexTime) {
		this.indexTime = indexTime;
	}

	public SaleOrder getSaleOrder() {
		return saleOrder;
	}

	public void setSaleOrder(SaleOrder saleOrder) {
		this.saleOrder = saleOrder;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<UserBlogImg> getUserBlogImgs() {
		return userBlogImgs;
	}

	public void setUserBlogImgs(List<UserBlogImg> userBlogImgs) {
		this.userBlogImgs = userBlogImgs;
	}

	public UserBlogImg getUserBlogImg() {
		return userBlogImg;
	}

	public void setUserBlogImg(UserBlogImg userBlogImg) {
		this.userBlogImg = userBlogImg;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public String getHeadFileBrowseUrl() {
		return headFileBrowseUrl;
	}

	public void setHeadFileBrowseUrl(String headFileBrowseUrl) {
		this.headFileBrowseUrl = headFileBrowseUrl;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "UserBlog [id=" + id + ", orderId=" + orderId + ", userId=" + userId + ", title=" + title + ", content=" + content + ", allowAnony=" + allowAnony + ", createTime=" + createTime + ", changeTime=" + changeTime + ", indexTime="
				+ indexTime + ", published=" + published + ", auditStatus=" + auditStatus + ", saleOrder=" + saleOrder + ", user=" + user + ", headFileBrowseUrl=" + headFileBrowseUrl + ", commentCount=" + commentCount + ", userBlogImg="
				+ userBlogImg + ", userBlogImgs=" + userBlogImgs + "]";
	}
}