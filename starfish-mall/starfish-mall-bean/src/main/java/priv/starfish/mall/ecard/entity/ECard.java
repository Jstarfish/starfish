package priv.starfish.mall.ecard.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "ecard", uniqueConstraints = { @UniqueConstraint(fieldNames = { "name" }) }, desc = "e卡")
public class ECard implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(auto = false, type = Types.VARCHAR, length = 30, desc = "卡(类型)代码")
	private String code;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "卡名称")
	private String name;

	@Column(nullable = false, type = Types.INTEGER, desc = "身份级别")
	private int rank;

	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 2, desc = "面值")
	private BigDecimal faceVal;

	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 2, desc = "价格（所需购买金额）")
	private BigDecimal price;

	@Column(type = Types.VARCHAR, length = 60, desc = "logo UUID")
	private String logoUuid;

	@Column(type = Types.VARCHAR, length = 30, desc = "image.logo \\ ecard")
	private String logoUsage;

	@Column(type = Types.VARCHAR, length = 250, desc = "相对图片地址")
	private String logoPath;

	@Column(nullable = false, type = Types.INTEGER, desc = "序号")
	private int seqNo;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false", desc = "是否禁用")
	private Boolean disabled;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false", desc = "删除标记")
	private Boolean deleted;

	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "时间戳")
	private Date ts;

	private String fileBrowseUrl;

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

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public BigDecimal getFaceVal() {
		return faceVal;
	}

	public void setFaceVal(BigDecimal faceVal) {
		this.faceVal = faceVal;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getLogoUuid() {
		return logoUuid;
	}

	public void setLogoUuid(String logoUuid) {
		this.logoUuid = logoUuid;
	}

	public String getLogoUsage() {
		return logoUsage;
	}

	public void setLogoUsage(String logoUsage) {
		this.logoUsage = logoUsage;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
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

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	@Override
	public String toString() {
		return "ECard [code=" + code + ", name=" + name + ", rank=" + rank + ", faceVal=" + faceVal + ", price=" + price + ", logoUuid=" + logoUuid + ", logoUsage=" + logoUsage + ", logoPath=" + logoPath + ", seqNo=" + seqNo + ", disabled="
				+ disabled + ", deleted=" + deleted + ", ts=" + ts + ", fileBrowseUrl=" + fileBrowseUrl + "]";
	}

}
