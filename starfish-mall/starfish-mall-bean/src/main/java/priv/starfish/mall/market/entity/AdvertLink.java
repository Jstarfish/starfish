package priv.starfish.mall.market.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonDateSerializer;

@Table(name = "advert_link", uniqueConstraints = { @UniqueConstraint(fieldNames = { "advertId","imageUuid" }) })
public class AdvertLink implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 广告Id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = Advert.class, refFieldName = "id")
	private Integer advertId;

	/** imageUuid */
	@Column(nullable = false, type = Types.VARCHAR, length = 60)
	private String imageUuid;

	/** imageUsage */
	@Column(type = Types.VARCHAR, length = 30)
	private String imageUsage;

	/** imagePath */
	@Column(type = Types.VARCHAR, length = 250)
	private String imagePath;
	/** 帧 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer seqNo;

	/** 图片链接 */
	@Column(nullable = false, type = Types.VARCHAR, length = 250)
	private String linkUrl;
	
	/** 开始时间 */
	@Column(type = Types.DATE)
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date startDate;
	
	/** 结束时间 */
	@Column(type = Types.DATE)
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date endDate;
	
	/** 最小时间 */
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date minDate;
	
	/** 最大时间 */
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date maxDate;
	
	/** 广告链接图片浏览路径 */
	private String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAdvertId() {
		return advertId;
	}

	public void setAdvertId(Integer advertId) {
		this.advertId = advertId;
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

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getMinDate() {
		return minDate;
	}

	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

}
