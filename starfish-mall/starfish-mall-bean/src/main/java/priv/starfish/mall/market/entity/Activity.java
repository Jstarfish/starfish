package priv.starfish.mall.market.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.comn.misc.HasImage;

@Table(name = "activity", uniqueConstraints = { @UniqueConstraint(fieldNames = { "year", "name", "targetFlag" }) })
public class Activity implements Serializable, HasImage {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER, desc = "主键")
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "发生的年份，取系统时间，不可更新")
	private Integer year;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "活动名称")
	private String name;

	@Column(nullable = false, type = Types.INTEGER, desc = "发布对象：-1：所有人， 0：会员，1：店铺，2：卫星店")
	private Integer targetFlag;

	@Column(nullable = false, type = Types.CLOB, desc = "内容")
	private String content;

	@Column(type = Types.VARCHAR, length = 60, desc = "uuid")
	private String imageUuid;

	@Column(type = Types.VARCHAR, length = 30, desc = "用途：image.goods")
	private String imageUsage;

	@Column(type = Types.VARCHAR, length = 250, desc = "图片路径")
	private String imagePath;

	/** 图片浏览路径 */
	private String fileBrowseUrl;

	@Column(nullable = false, type = Types.INTEGER, desc = "发布人Id")
	private Integer creatorId;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "发布人名称")
	private String creatorName;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "创建时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date createTime;

	@Column(type = Types.TIMESTAMP, desc = "发布开始时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date pubTime;

	@Column(type = Types.TIMESTAMP, desc = "发布结束时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date endTime;

	@Column(nullable = false, type = Types.INTEGER, desc = "0 : 未发布, 1: 发布中, 2：已停止")
	private Integer status;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "时间戳")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTargetFlag() {
		return targetFlag;
	}

	public void setTargetFlag(Integer targetFlag) {
		this.targetFlag = targetFlag;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImageUuid() {
		return imageUuid;
	}

	public void setImageUuid(String imageUuid) {
		this.imageUuid = imageUuid;
	}

	public String getImageUsage() {
		return imageUsage;
	}

	public void setImageUsage(String imageUsage) {
		this.imageUsage = imageUsage;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPubTime() {
		return pubTime;
	}

	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		return "Activity [id=" + id + ", year=" + year + ", name=" + name + ", targetFlag=" + targetFlag + ", content=" + content + ", imageUuid=" + imageUuid + ", imageUsage=" + imageUsage + ", imagePath=" + imagePath + ", fileBrowseUrl="
				+ fileBrowseUrl + ", creatorId=" + creatorId + ", creatorName=" + creatorName + ", createTime=" + createTime + ", pubTime=" + pubTime + ", endTime=" + endTime + ", status=" + status + ", ts=" + ts + "]";
	}

}
