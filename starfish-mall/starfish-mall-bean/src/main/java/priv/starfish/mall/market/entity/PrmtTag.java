package priv.starfish.mall.market.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.List;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.mall.comn.misc.HasIcon;

@Table(name = "prmt_tag", uniqueConstraints = { @UniqueConstraint(fieldNames = { "code"}) }, desc = "促销标签")
public class PrmtTag implements Serializable, HasIcon{

	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String code;
	
	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc="名称")
	private String name;
	
	@Column(name = "`desc`", type = Types.VARCHAR, length = 250, desc="描述")
	private String desc;

	@Column(type = Types.VARCHAR, length = 60, desc="图标的uuid")
	private String iconUuid;

	@Column(type = Types.VARCHAR, length = 30, desc="图标的用途")
	private String iconUsage;
	
	@Column(type = Types.VARCHAR, length = 250, desc="图标路径")
	private String iconPath;
	
	@Column(nullable = false, type = Types.INTEGER)
	private Integer seqNo;
	
	/** 图片浏览路径 */
	private String fileBrowseUrl;
	
	/** 是否存在于货品或车辆服务中的标志 */
	private Boolean existFlag;
	
	private List<PrmtTagGoods> prmtTagGoods;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getIconUuid() {
		return iconUuid;
	}

	public void setIconUuid(String iconUuid) {
		this.iconUuid = iconUuid;
	}

	public String getIconUsage() {
		return iconUsage;
	}

	public void setIconUsage(String iconUsage) {
		this.iconUsage = iconUsage;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	public Boolean getExistFlag() {
		return existFlag;
	}

	public void setExistFlag(Boolean existFlag) {
		this.existFlag = existFlag;
	}

	public List<PrmtTagGoods> getPrmtTagGoods() {
		return prmtTagGoods;
	}

	public void setPrmtTagGoods(List<PrmtTagGoods> prmtTagGoods) {
		this.prmtTagGoods = prmtTagGoods;
	}

	@Override
	public String toString() {
		return "PrmtTag [id=" + id + ", code=" + code + ", name=" + name + ", desc=" + desc + ", iconUuid=" + iconUuid + ", iconUsage=" + iconUsage + ", iconPath=" + iconPath + ", seqNo=" + seqNo + ", fileBrowseUrl=" + fileBrowseUrl
				+ ", existFlag=" + existFlag + ", prmtTagGoods=" + prmtTagGoods + "]";
	}

}
