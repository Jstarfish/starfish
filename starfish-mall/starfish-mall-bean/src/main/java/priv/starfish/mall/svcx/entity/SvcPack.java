package priv.starfish.mall.svcx.entity;

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
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonDateDeserializer;
import priv.starfish.common.json.JsonDateSerializer;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.cart.dto.MiscAmountInfo;

@Table(name = "svc_pack", uniqueConstraints = { @UniqueConstraint(fieldNames = { "kindId", "name" }) })
public class SvcPack implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "所属种类主键")
	@ForeignKey(refEntityClass = SvcKind.class, refFieldName = "id")
	private Integer kindId;

	@Column(nullable = false, type = Types.VARCHAR, desc = "名称")
	private String name;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "开始时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date startTime;

	@Column(type = Types.TIMESTAMP, desc = "结束时间")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date endTime;

	@Column(type = Types.TIMESTAMP, desc = "启用时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date pubTime;

	@Column(type = Types.VARCHAR, length = 30, desc = "image.svc  poster 海报图片")
	private String imageUsage;

	@Column(type = Types.VARCHAR, length = 60, desc = "")
	private String imageUuid;

	@Column(type = Types.VARCHAR, length = 250, desc = "图片")
	private String imagePath;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250, desc = "分组描述")
	private String desc;

	@Column(nullable = false, type = Types.INTEGER, desc = "排序号")
	private Integer seqNo;

	@Column(type = Types.BOOLEAN, desc = "是否禁用")
	private Boolean disabled;

	@Column(type = Types.BOOLEAN, desc = "删除标记")
	private Boolean deleted;

	@Column(type = Types.VARCHAR, length = 250)
	private String memo;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "创建时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date createTime;

	private List<SvcPackItem> packItemList;

	private String fileBrowseUrl;

	private MiscAmountInfo amountInfo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getKindId() {
		return kindId;
	}

	public void setKindId(Integer kindId) {
		this.kindId = kindId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getPubTime() {
		return pubTime;
	}

	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}

	public String getImageUsage() {
		return imageUsage;
	}

	public void setImageUsage(String imageUsage) {
		this.imageUsage = imageUsage;
	}

	public String getImageUuid() {
		return imageUuid;
	}

	public void setImageUuid(String imageUuid) {
		this.imageUuid = imageUuid;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<SvcPackItem> getPackItemList() {
		return packItemList;
	}

	public void setPackItemList(List<SvcPackItem> packItemList) {
		this.packItemList = packItemList;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public MiscAmountInfo getAmountInfo() {
		return amountInfo;
	}

	public void setAmountInfo(MiscAmountInfo amountInfo) {
		this.amountInfo = amountInfo;
	}

	@Override
	public String toString() {
		return "SvcPack [id=" + id + ", kindId=" + kindId + ", name=" + name + ", startTime=" + startTime + ", endTime=" + endTime + ", pubTime=" + pubTime + ", imageUsage=" + imageUsage + ", imageUuid=" + imageUuid + ", imagePath="
				+ imagePath + ", desc=" + desc + ", seqNo=" + seqNo + ", disabled=" + disabled + ", deleted=" + deleted + ", memo=" + memo + ", createTime=" + createTime + ", packItemList=" + packItemList + ", fileBrowseUrl="
				+ fileBrowseUrl + ", amountInfo=" + amountInfo + "]";
	}

}
