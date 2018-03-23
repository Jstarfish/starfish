package priv.starfish.mall.goods.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import priv.starfish.mall.vendor.entity.Vendor;
import priv.starfish.common.annotation.*;
import priv.starfish.mall.categ.entity.GoodsCat;

@Table(name = "goods", uniqueConstraints = { @UniqueConstraint(fieldNames = { "catId", "shopId", "name", "vendorId" }) })
public class Goods implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;
	
	/** 商品编号 */
	@Column(type = Types.VARCHAR, length = 60)
	private String no;
	
	/** 商品分类Id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = GoodsCat.class, refFieldName = "id")
	private Integer catId;
	
	/** 店铺Id */
	@Column(type = Types.INTEGER)
	private Integer shopId;
	
	@Column(nullable = false, type = Types.INTEGER, desc="供应商id")
	@ForeignKey(refEntityClass = Vendor.class, refFieldName = "id")
	private Integer vendorId;

	/** 商品分类的父级Id集 */
	@Column(nullable = false, type = Types.VARCHAR, length = 60)
	private String catPath;
	
	/** 商品名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 60)
	private String name;
	
	/** 商品拼音 */
	@Column(type = Types.VARCHAR, length = 30)
	private String py;
	
	/** 商品标题*/
	@Column(type = Types.VARCHAR, length = 30)
	private String title;
	
	/** 商品数量 */
	@Column(type = Types.INTEGER)
	private Integer quantity;
	
	/** 最小价格 */
	@Column(type = Types.DECIMAL, precision = 18, scale = 4)
	private BigDecimal minPrice;

	/** 最大价格 */
	@Column(type = Types.DECIMAL, precision = 18, scale = 4)
	private BigDecimal maxPrice;
	
	/** 平均价格 */
	@Column(type = Types.DECIMAL, precision = 18, scale = 4)
	private BigDecimal avgPrice;
	
	/** 包装清单*/
	@Column(type = Types.VARCHAR, length = 250)
	private String packList;
	
	@Column(type = Types.BOOLEAN, defaultValue = "false", desc="是否启用规格")
	private Boolean hasSpec;
	
	//商品属性列表
	private List<GoodsAttrVal> attrVals = new ArrayList<GoodsAttrVal>(0);
	
	//货品列表
	private List<Product> products = new ArrayList<Product>(0);
	
	//商品介绍
	private GoodsIntro goodsIntro;
	
	//
	public static Goods newOne() {
		return new Goods();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getCatPath() {
		return catPath;
	}

	public void setCatPath(String catPath) {
		this.catPath = catPath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public BigDecimal getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(BigDecimal avgPrice) {
		this.avgPrice = avgPrice;
	}

	public List<GoodsAttrVal> getAttrVals() {
		return attrVals;
	}

	public void setAttrVals(List<GoodsAttrVal> attrVals) {
		this.attrVals = attrVals;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public GoodsIntro getGoodsIntro() {
		return goodsIntro;
	}

	public void setGoodsIntro(GoodsIntro goodsIntro) {
		this.goodsIntro = goodsIntro;
	}

	public String getPackList() {
		return packList;
	}

	public void setPackList(String packList) {
		this.packList = packList;
	}

	public Boolean getHasSpec() {
		return hasSpec;
	}

	public void setHasSpec(Boolean hasSpec) {
		this.hasSpec = hasSpec;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	@Override
	public String toString() {
		return "Goods [id=" + id + ", no=" + no + ", catId=" + catId + ", shopId=" + shopId + ", vendorId=" + vendorId + ", catPath=" + catPath + ", name=" + name + ", py=" + py + ", title=" + title + ", quantity=" + quantity
				+ ", minPrice=" + minPrice + ", maxPrice=" + maxPrice + ", avgPrice=" + avgPrice + ", packList=" + packList + ", hasSpec=" + hasSpec + ", attrVals=" + attrVals + ", products=" + products + ", goodsIntro=" + goodsIntro + "]";
	}

}
