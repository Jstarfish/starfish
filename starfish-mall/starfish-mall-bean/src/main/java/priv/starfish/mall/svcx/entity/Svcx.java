package priv.starfish.mall.svcx.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.List;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;

@Table(name = "svcx", uniqueConstraints = { @UniqueConstraint(fieldNames = { "kindId", "name" }) })
public class Svcx implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "所属种类主键")
	@ForeignKey(refEntityClass = SvcKind.class, refFieldName = "id")
	private Integer kindId;

	@Column(nullable = false, type = Types.VARCHAR, desc = "服务名称")
	private String name;

	@Column(nullable = false, type = Types.INTEGER, desc = "所属分组主键")
	@ForeignKey(refEntityClass = SvcGroup.class, refFieldName = "id")
	private Integer groupId;

	@Column(type = Types.VARCHAR, desc = "服务分组名称")
	private String grounpName;

	@Column(type = Types.VARCHAR, length = 30, desc = "image.svc  poster 海报图片")
	private String imageUsage;

	@Column(type = Types.VARCHAR, length = 60, desc = "")
	private String imageUuid;

	@Column(type = Types.VARCHAR, length = 250, desc = "图片")
	private String imagePath;

	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "销售价格", defaultValue = "0")
	private BigDecimal salePrice;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250, desc = "服务描述")
	private String desc;

	@Column(type = Types.VARCHAR, length = 30, desc = "image.svc  icon")
	private String iconUsage;

	@Column(type = Types.VARCHAR, length = 60, desc = "默认图标（不带颜色）")
	private String iconUuid;

	@Column(type = Types.VARCHAR, length = 250, desc = "无颜色图标")
	private String iconPath;

	@Column(type = Types.VARCHAR, length = 60, desc = "")
	private String iconUuid2;

	@Column(type = Types.VARCHAR, length = 250, desc = "活动图标（带颜色）")
	private String iconPath2;

	@Column(type = Types.VARCHAR, length = 60, desc = "")
	private String iconUuidApp;

	@Column(type = Types.VARCHAR, length = 250, desc = "App图标")
	private String iconPathApp;

	@Column(nullable = false, type = Types.INTEGER, desc = "排序号")
	private Integer seqNo;

	@Column(nullable = false, type = Types.BOOLEAN, desc = "是否不可用")
	private Boolean disabled;

	@Column(type = Types.VARCHAR, length = 250, desc = "适用范围")
	private String appRange;

	@Column(nullable = false, type = Types.BOOLEAN, desc = "是否删除", defaultValue = "false")
	private Boolean deleted;

	private SvcGroup carSvcGroup;

	// 图片
	private String fileBrowseUrl;

	// 无色图标
	private String fileBrowseUrlIcon;

	// 有色图片
	private String fileBrowseUrlIcon2;

	// app图标
	private String fileBrowseUrlApp;

	// 会员等级折扣
	private List<SvcxRankDisc> svcxRankDiscs;

	// 是否在某个店铺中存在标志
	private Boolean existFlag;

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

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getGrounpName() {
		return grounpName;
	}

	public void setGrounpName(String grounpName) {
		this.grounpName = grounpName;
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

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getIconUsage() {
		return iconUsage;
	}

	public void setIconUsage(String iconUsage) {
		this.iconUsage = iconUsage;
	}

	public String getIconUuid() {
		return iconUuid;
	}

	public void setIconUuid(String iconUuid) {
		this.iconUuid = iconUuid;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getIconUuid2() {
		return iconUuid2;
	}

	public void setIconUuid2(String iconUuid2) {
		this.iconUuid2 = iconUuid2;
	}

	public String getIconPath2() {
		return iconPath2;
	}

	public void setIconPath2(String iconPath2) {
		this.iconPath2 = iconPath2;
	}

	public String getIconUuidApp() {
		return iconUuidApp;
	}

	public void setIconUuidApp(String iconUuidApp) {
		this.iconUuidApp = iconUuidApp;
	}

	public String getIconPathApp() {
		return iconPathApp;
	}

	public void setIconPathApp(String iconPathApp) {
		this.iconPathApp = iconPathApp;
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

	public String getAppRange() {
		return appRange;
	}

	public void setAppRange(String appRange) {
		this.appRange = appRange;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public SvcGroup getCarSvcGroup() {
		return carSvcGroup;
	}

	public void setCarSvcGroup(SvcGroup carSvcGroup) {
		this.carSvcGroup = carSvcGroup;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	public String getFileBrowseUrlIcon() {
		return fileBrowseUrlIcon;
	}

	public void setFileBrowseUrlIcon(String fileBrowseUrlIcon) {
		this.fileBrowseUrlIcon = fileBrowseUrlIcon;
	}

	public String getFileBrowseUrlIcon2() {
		return fileBrowseUrlIcon2;
	}

	public void setFileBrowseUrlIcon2(String fileBrowseUrlIcon2) {
		this.fileBrowseUrlIcon2 = fileBrowseUrlIcon2;
	}

	public String getFileBrowseUrlApp() {
		return fileBrowseUrlApp;
	}

	public void setFileBrowseUrlApp(String fileBrowseUrlApp) {
		this.fileBrowseUrlApp = fileBrowseUrlApp;
	}

	public List<SvcxRankDisc> getSvcxRankDiscs() {
		return svcxRankDiscs;
	}

	public void setSvcxRankDiscs(List<SvcxRankDisc> svcxRankDiscs) {
		this.svcxRankDiscs = svcxRankDiscs;
	}

	public Boolean getExistFlag() {
		return existFlag;
	}

	public void setExistFlag(Boolean existFlag) {
		this.existFlag = existFlag;
	}

	@Override
	public String toString() {
		return "Svcx [id=" + id + ", kindId=" + kindId + ", name=" + name + ", groupId=" + groupId + ", grounpName=" + grounpName + ", imageUsage=" + imageUsage + ", imageUuid=" + imageUuid + ", imagePath=" + imagePath + ", salePrice="
				+ salePrice + ", desc=" + desc + ", iconUsage=" + iconUsage + ", iconUuid=" + iconUuid + ", iconPath=" + iconPath + ", iconUuid2=" + iconUuid2 + ", iconPath2=" + iconPath2 + ", iconUuidApp=" + iconUuidApp + ", iconPathApp="
				+ iconPathApp + ", seqNo=" + seqNo + ", disabled=" + disabled + ", appRange=" + appRange + ", deleted=" + deleted + ", carSvcGroup=" + carSvcGroup + ", fileBrowseUrl=" + fileBrowseUrl + ", fileBrowseUrlIcon="
				+ fileBrowseUrlIcon + ", fileBrowseUrlIcon2=" + fileBrowseUrlIcon2 + ", fileBrowseUrlApp=" + fileBrowseUrlApp + ", svcxRankDiscs=" + svcxRankDiscs + ", existFlag=" + existFlag + "]";
	}

}
