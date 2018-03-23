package priv.starfish.mall.market.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonDateSerializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "advert", uniqueConstraints = { @UniqueConstraint(fieldNames = { "name" }) })
public class Advert implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 位置编码 */
	@Column(type = Types.VARCHAR, length = 30)
	private String posCode;
	
	/** 广告位置*/
	private AdvertPos advertPos;

	/** 名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** 轮播组 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer seqCount;

	/** 描述 */
	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	/** 是否支持轮播 */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean caroFlag;

	/** 轮播时间 */
	@Column(type = Types.INTEGER)
	private Integer caroIntv;

	/** 轮播动画效果 */
	@Column(type = Types.VARCHAR, length = 30)
	private String caroAnim;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;
	
	/** 开始时间 */
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date startDate;

	/** 结束时间 */
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date endDate;

	private List<AdvertLink> advertLinks;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPosCode() {
		return posCode;
	}

	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}

	public AdvertPos getAdvertPos() {
		return advertPos;
	}

	public void setAdvertPos(AdvertPos advertPos) {
		this.advertPos = advertPos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSeqCount() {
		return seqCount;
	}

	public void setSeqCount(Integer seqCount) {
		this.seqCount = seqCount;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Boolean getCaroFlag() {
		return caroFlag;
	}

	public void setCaroFlag(Boolean caroFlag) {
		this.caroFlag = caroFlag;
	}

	public Integer getCaroIntv() {
		return caroIntv;
	}

	public void setCaroIntv(Integer caroIntv) {
		this.caroIntv = caroIntv;
	}

	public String getCaroAnim() {
		return caroAnim;
	}

	public void setCaroAnim(String caroAnim) {
		this.caroAnim = caroAnim;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public List<AdvertLink> getAdvertLinks() {
		return advertLinks;
	}

	public void setAdvertLinks(List<AdvertLink> advertLinks) {
		this.advertLinks = advertLinks;
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

	@Override
	public String toString() {
		return "Advert [id=" + id + ", posCode=" + posCode + ", advertPos=" + advertPos + ", name=" + name + ", seqCount=" + seqCount + ", desc=" + desc + ", caroFlag=" + caroFlag + ", caroIntv=" + caroIntv + ", caroAnim=" + caroAnim
				+ ", ts=" + ts + ", startDate=" + startDate + ", endDate=" + endDate + ", advertLinks=" + advertLinks + "]";
	}

}
