package priv.starfish.mall.logistic.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "logistic_com", uniqueConstraints = { @UniqueConstraint(fieldNames = { "code" }) })
public class LogisCom implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 编码 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String code;

	/** 名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** logo图片存放路径 */
	@Column(type = Types.VARCHAR, length = 250)
	private String logoPath;

	/** 物流公司网址 */
	@Column(nullable = false, type = Types.VARCHAR, length = 250)
	private String url;

	/** 描述 */
	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	/** 启用/禁用 */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean disabled;

	/** 序号 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1")
	private Integer seqNo;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	/** 配送方式集 */
	private List<DeliveryWay> deliveryWays = new ArrayList<DeliveryWay>(0);

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

	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public List<DeliveryWay> getDeliveryWays() {
		return deliveryWays;
	}

	public void setDeliveryWays(List<DeliveryWay> deliveryWays) {
		this.deliveryWays = deliveryWays;
	}

	@Override
	public String toString() {
		return "BizParam [id=" + id + ", code=" + code + ", name=" + name + ", logoPath=" + logoPath + ", url=" + url + ", desc=" + desc + ", disabled=" + disabled + ", seqNo=" + seqNo + ", ts=" + ts + "]";
	}
}
