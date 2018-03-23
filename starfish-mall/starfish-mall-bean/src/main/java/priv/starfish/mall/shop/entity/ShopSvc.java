package priv.starfish.mall.shop.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.svcx.entity.Svcx;

/**
 * 店铺服务状态
 * 
 * @author 邓华锋
 * @date 2016年01月18日 11:54:43
 *
 */
@Table(name = "shop_svc", uniqueConstraints = { @UniqueConstraint(fieldNames = { "shopId", "svcId" }) })
public class ShopSvc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT)
	private Long id;

	@Column(nullable = false, type = Types.INTEGER, desc = "店铺ID")
	@ForeignKey(refEntityClass = Shop.class, refFieldName = "id")
	private Integer shopId;

	@Column(nullable = false, type = Types.INTEGER, desc = "服务ID")
	@ForeignKey(refEntityClass = Svcx.class, refFieldName = "id")
	private Integer svcId;

	@Column(nullable = false, type = Types.VARCHAR, desc = "服务名称")
	private String svcName;

	@Column(nullable = false, type = Types.INTEGER, desc = "分类ID")
	private Integer kindId;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "更新时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;
	
	private String desc;
	
	private BigDecimal salePrice;

	private Svcx svc;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getSvcId() {
		return svcId;
	}

	public void setSvcId(Integer svcId) {
		this.svcId = svcId;
	}

	public String getSvcName() {
		return svcName;
	}

	public void setSvcName(String svcName) {
		this.svcName = svcName;
	}

	public Integer getKindId() {
		return kindId;
	}

	public void setKindId(Integer kindId) {
		this.kindId = kindId;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public Svcx getSvc() {
		return svc;
	}

	public void setSvc(Svcx svc) {
		this.svc = svc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	@Override
	public String toString() {
		return "ShopSvc [id=" + id + ", shopId=" + shopId + ", svcId=" + svcId + ", svcName=" + svcName + ", kindId=" + kindId + ", ts=" + ts + ", desc=" + desc + ", salePrice=" + salePrice + ", svc=" + svc + "]";
	}

}