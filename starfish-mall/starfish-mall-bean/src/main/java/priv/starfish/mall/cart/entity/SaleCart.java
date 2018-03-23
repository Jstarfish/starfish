package priv.starfish.mall.cart.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.cart.dto.MiscAmountInfo;

@Table(name = "sale_cart")
public class SaleCart implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(auto = false, type = Types.INTEGER, desc = "= user.id")
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "服务数量")
	private Integer svcCount;

	@Column(nullable = false, type = Types.INTEGER, desc = "商品数量")
	private Integer goodsCount;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "添加时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date ts;

	// 购物车服务总服务费（下级服务总和）
	private BigDecimal svcAmount;
	
	//服务总折扣价格
	private BigDecimal svcDiscAmount;

	// 商品总价（下级商品总和）
	private BigDecimal goodsAmount;
	
	//商品总折扣价格
	private BigDecimal goodsDiscAmount;
	// 结算金额
	private BigDecimal settlePrice;

	// 购物车商品列表（购物车商品）
	private List<SaleCartGoods> saleCartGoods;

	// 购物车服务列表（购物车服务）
	private List<SaleCartSvc> saleCartSvcList;

	// 各种金额
	private MiscAmountInfo amountInfo;

	public BigDecimal getSvcAmount() {
		return svcAmount;
	}

	public void setSvcAmount(BigDecimal svcAmount) {
		this.svcAmount = svcAmount;
	}

	public BigDecimal getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(BigDecimal goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public List<SaleCartSvc> getSaleCartSvcList() {
		return saleCartSvcList;
	}

	public void setSaleCartSvcList(List<SaleCartSvc> saleCartSvcList) {
		this.saleCartSvcList = saleCartSvcList;
	}

	public static SaleCart newOne() {
		return new SaleCart();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public BigDecimal getSettlePrice() {
		return settlePrice;
	}

	public void setSettlePrice(BigDecimal settlePrice) {
		this.settlePrice = settlePrice;
	}

	public Integer getSvcCount() {
		return svcCount;
	}

	public void setSvcCount(Integer svcCount) {
		this.svcCount = svcCount;
	}

	public Integer getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(Integer goodsCount) {
		this.goodsCount = goodsCount;
	}

	public List<SaleCartGoods> getSaleCartGoods() {
		return saleCartGoods;
	}

	public void setSaleCartGoods(List<SaleCartGoods> saleCartGoods) {
		this.saleCartGoods = saleCartGoods;
	}

	public MiscAmountInfo getAmountInfo() {
		return amountInfo;
	}

	public void setAmountInfo(MiscAmountInfo amountInfo) {
		this.amountInfo = amountInfo;
	}

	public BigDecimal getSvcDiscAmount() {
		return svcDiscAmount;
	}

	public void setSvcDiscAmount(BigDecimal svcDiscAmount) {
		this.svcDiscAmount = svcDiscAmount;
	}

	public BigDecimal getGoodsDiscAmount() {
		return goodsDiscAmount;
	}

	public void setGoodsDiscAmount(BigDecimal goodsDiscAmount) {
		this.goodsDiscAmount = goodsDiscAmount;
	}

	@Override
	public String toString() {
		return "SaleCart [id=" + id + ", svcCount=" + svcCount + ", goodsCount=" + goodsCount + ", ts=" + ts + ", svcAmount=" + svcAmount + ", svcDiscAmount=" + svcDiscAmount + ", goodsAmount=" + goodsAmount + ", goodsDiscAmount="
				+ goodsDiscAmount + ", settlePrice=" + settlePrice + ", saleCartGoods=" + saleCartGoods + ", saleCartSvcList=" + saleCartSvcList + ", amountInfo=" + amountInfo + "]";
	}
}
