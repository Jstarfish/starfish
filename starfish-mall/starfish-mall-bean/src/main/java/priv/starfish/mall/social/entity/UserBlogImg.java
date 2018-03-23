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

/**
 * 博客图片信息
 * @author 邓华锋
 * @date 2015年10月15日 13:58:49
 *
 */
@Table(name = "user_blog_img")
public class UserBlogImg implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id(type = Types.BIGINT)
	private Long id;
	
	@Column(name="blogId",nullable = false, type = Types.INTEGER,desc = "博文ID")
	@ForeignKey(refEntityClass = UserBlog.class, refFieldName = "id")
	private Integer blogId;
	
	@Column(name="imageUuid",nullable = false, type = Types.VARCHAR,length = 60,desc = "图片UUID")
	private String imageUuid;
	
	@Column(name="imageUsage",nullable = true, type = Types.VARCHAR,length = 30,desc = "图片用途")
	private String imageUsage;
	
	@Column(name="imagePath",nullable = true, type = Types.VARCHAR,length = 250,desc = "图片路径")
	private String imagePath;
	
	/**
	 * 图片路径
	 */
	private String fileBrowseUrl;
	
	@Column(name="ts",nullable = false, type = Types.TIMESTAMP,desc = "发表时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;
	
	
	/**
	 * 
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * 
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * 博文ID
	 */
	public Integer getBlogId() {
		return blogId;
	}
	
	/**
	 * 博文ID
	 */
	public void setBlogId(Integer blogId) {
		this.blogId = blogId;
	}
	
	/**
	 * 图片UUID
	 */
	public String getImageUuid() {
		return imageUuid;
	}
	
	/**
	 * 图片UUID
	 */
	public void setImageUuid(String imageUuid) {
		this.imageUuid = imageUuid;
	}
	
	/**
	 * 图片用途
	 */
	public String getImageUsage() {
		return imageUsage;
	}
	
	/**
	 * 图片用途
	 */
	public void setImageUsage(String imageUsage) {
		this.imageUsage = imageUsage;
	}
	
	/**
	 * 图片路径
	 */
	public String getImagePath() {
		return imagePath;
	}
	
	/**
	 * 图片路径
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
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
	
	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	public String toString() {
		return "UserBlogImg ["+"id=" + id + ", "+"blogId=" + blogId + ", "+"imageUuid=" + imageUuid + ", "+"imageUsage=" + imageUsage + ", "+"imagePath=" + imagePath + ", "+"ts=" + ts+"]";
	}
	
}