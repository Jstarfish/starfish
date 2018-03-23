package priv.starfish.mall.social.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.comn.entity.User;

/**
 * 博客评论
 * @author 邓华锋
 * @date 2015年10月15日 14:56:59
 *
 */
@Table(name = "user_blog_comment")
public class UserBlogComment implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id(type = Types.INTEGER)
	private Integer id;
	
	@Column(name="blogId",nullable = true, type = Types.INTEGER,desc = "博客ID")
	@ForeignKey(refEntityClass = UserBlog.class, refFieldName = "id")
	private Integer blogId;
	
	@Column(name="userId",nullable = true, type = Types.INTEGER,desc = "用户ID")
	@ForeignKey(refEntityClass = User.class, refFieldName = "id")
	private Integer userId;
	
	@Column(name="userName",nullable = false, type = Types.VARCHAR,length = 250,desc = "用户昵称或真实名称")
	private String userName;
	
	@Column(name="content",nullable = false, type = Types.VARCHAR,length = 250,desc = "纯文本")
	private String content;
	
	@Column(name="seqNo",nullable = false, type = Types.INTEGER,desc = "同一个 blogId下顺序号")
	private Integer seqNo;
	
	@Column(name="ts",nullable = false, type = Types.TIMESTAMP,desc = "发表时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;
	
	/** 评论者头像 */
	private String headFileBrowseUrl;
	
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
	 * 博客ID
	 */
	public Integer getBlogId() {
		return blogId;
	}
	
	/**
	 * 博客ID
	 */
	public void setBlogId(Integer blogId) {
		this.blogId = blogId;
	}
	
	/**
	 * 用户ID
	 */
	public Integer getUserId() {
		return userId;
	}
	
	/**
	 * 用户ID
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	/**
	 * 纯文本
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * 纯文本
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * 同一个 blogId下顺序号
	 */
	public Integer getSeqNo() {
		return seqNo;
	}
	
	/**
	 * 同一个 blogId下顺序号
	 */
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
	
	/**
	 * 发表时间
	 */
	public Date getTs() {
		return ts;
	}
	
	/**
	 * 发表时间
	 */
	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHeadFileBrowseUrl() {
		return headFileBrowseUrl;
	}

	public void setHeadFileBrowseUrl(String headFileBrowseUrl) {
		this.headFileBrowseUrl = headFileBrowseUrl;
	}

	@Override
	public String toString() {
		return "UserBlogComment [id=" + id + ", blogId=" + blogId + ", userId=" + userId + ", userName=" + userName + ", content=" + content + ", seqNo=" + seqNo + ", ts=" + ts + "]";
	}
	
}