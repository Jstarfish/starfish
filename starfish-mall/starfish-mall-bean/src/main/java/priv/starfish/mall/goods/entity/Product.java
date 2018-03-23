package priv.starfish.mall.goods.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.categ.entity.GoodsCat;
import priv.starfish.mall.comn.dict.VolumeUnit;
import priv.starfish.mall.comn.dict.WeightUnit;
import priv.starfish.mall.market.entity.PrmtTagGoods;

@Table(name = "product")
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT, desc = "主键")
	private Long id;

	@Column(type = Types.VARCHAR, length = 60, desc = "货品编号")
	private String no;

	@Column(nullable = false, type = Types.INTEGER, desc = "商品Id")
	@ForeignKey(refEntityClass = Goods.class, refFieldName = "id")
	private Integer goodsId;

	@Column(nullable = false, type = Types.VARCHAR, length = 60, desc = "商品名称")
	private String goodsName;

	@Column(nullable = false, type = Types.INTEGER, desc = "商品分类Id")
	@ForeignKey(refEntityClass = GoodsCat.class, refFieldName = "id")
	private Integer catId;

	@Column(type = Types.VARCHAR, length = 60, desc = "显示名称：商品名车+规格")
	private String title;

	@Column(nullable = false, type = Types.INTEGER, desc = "商品数量")
	private Integer quantity;

	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "进价")
	private BigDecimal purchPrice;

	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "批发价")
	private BigDecimal wholePrice;

	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "原价")
	private BigDecimal origPrice;

	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 4, desc = "销售价格")
	private BigDecimal salePrice;

	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "市场价格")
	private BigDecimal marketPrice;

	@Column(type = Types.NUMERIC, precision = 15, scale = 3, desc = "重量")
	private Double weight;

	@Column(type = Types.NUMERIC, precision = 15, scale = 3, desc = "体积")
	private Double volume;

	@Column(type = Types.INTEGER, defaultValue = "1", desc = "序号")
	private Integer seqNo;

	@Column(nullable = false, type = Types.INTEGER, desc = "上下架状态:0 - 未上架, 1 - 已上架， 2 - 已下架")
	private Integer shelfStatus;

	@Column(type = Types.TIMESTAMP, desc = "上架时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date shelfTime;

	@Column(type = Types.VARCHAR, length = 250, desc = "包装清单")
	private String packList;

	@Column(type = Types.BOOLEAN, desc = "删除状态")
	private Boolean deleted;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "创建时间 ")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date createTime;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "最后一次改变的时间 ")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date changeTime;

	@Column(type = Types.TIMESTAMP, desc = "索引时间 ")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date indexTime;

	@Column(type = Types.TIMESTAMP, desc = "静态化时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date staticTime;

	@Column(type = Types.VARCHAR, length = 60, desc = "uuid")
	private String htmlUuid;

	@Column(type = Types.VARCHAR, length = 60, desc = "用途")
	private String htmlUsage;

	@Column(type = Types.VARCHAR, length = 60, desc = "路径")
	private String htmlPath;

	@Column(type = Types.BOOLEAN, desc = "是否为赠品标识")
	private Boolean giftFlag;

	/** 是否存在于店铺中的标志 */
	private Boolean existFlag;

	/** 货品规格值List */
	private List<ProductSpecVal> specVals;

	/** 关联商品 */
	private Goods goods;

	/** 货品相册图片List */
	private List<ProductAlbumImg> productAlbumImgs;

	/** 计数单位 */
	private String unit;

	/** 重量单位 */
	private WeightUnit weightUnit;

	/** 体积单位 */
	private VolumeUnit volumeUnit;

	/** 促销标签 */
	List<PrmtTagGoods> prmtTagGoods;

	/** 购买汇总数 */
	private Long buySum;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
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

	public BigDecimal getPurchPrice() {
		return purchPrice;
	}

	public void setPurchPrice(BigDecimal purchPrice) {
		this.purchPrice = purchPrice;
	}

	public BigDecimal getWholePrice() {
		return wholePrice;
	}

	public void setWholePrice(BigDecimal wholePrice) {
		this.wholePrice = wholePrice;
	}

	public BigDecimal getOrigPrice() {
		return origPrice;
	}

	public void setOrigPrice(BigDecimal origPrice) {
		this.origPrice = origPrice;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Integer getShelfStatus() {
		return shelfStatus;
	}

	public void setShelfStatus(Integer shelfStatus) {
		this.shelfStatus = shelfStatus;
	}

	public Date getShelfTime() {
		return shelfTime;
	}

	public void setShelfTime(Date shelfTime) {
		this.shelfTime = shelfTime;
	}

	public String getPackList() {
		return packList;
	}

	public void setPackList(String packList) {
		this.packList = packList;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Date getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getIndexTime() {
		return indexTime;
	}

	public void setIndexTime(Date indexTime) {
		this.indexTime = indexTime;
	}

	public Date getStaticTime() {
		return staticTime;
	}

	public void setStaticTime(Date staticTime) {
		this.staticTime = staticTime;
	}

	public String getHtmlUuid() {
		return htmlUuid;
	}

	public void setHtmlUuid(String htmlUuid) {
		this.htmlUuid = htmlUuid;
	}

	public String getHtmlUsage() {
		return htmlUsage;
	}

	public void setHtmlUsage(String htmlUsage) {
		this.htmlUsage = htmlUsage;
	}

	public String getHtmlPath() {
		return htmlPath;
	}

	public void setHtmlPath(String htmlPath) {
		this.htmlPath = htmlPath;
	}

	public Boolean getGiftFlag() {
		return giftFlag;
	}

	public void setGiftFlag(Boolean giftFlag) {
		this.giftFlag = giftFlag;
	}

	public Boolean getExistFlag() {
		return existFlag;
	}

	public void setExistFlag(Boolean existFlag) {
		this.existFlag = existFlag;
	}

	public List<ProductSpecVal> getSpecVals() {
		return specVals;
	}

	public void setSpecVals(List<ProductSpecVal> specVals) {
		this.specVals = specVals;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public List<ProductAlbumImg> getProductAlbumImgs() {
		return productAlbumImgs;
	}

	public void setProductAlbumImgs(List<ProductAlbumImg> productAlbumImgs) {
		this.productAlbumImgs = productAlbumImgs;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public WeightUnit getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(WeightUnit weightUnit) {
		this.weightUnit = weightUnit;
	}

	public VolumeUnit getVolumeUnit() {
		return volumeUnit;
	}

	public void setVolumeUnit(VolumeUnit volumeUnit) {
		this.volumeUnit = volumeUnit;
	}

	public Long getBuySum() {
		return buySum;
	}

	public void setBuySum(Long buySum) {
		this.buySum = buySum;
	}

	public List<PrmtTagGoods> getPrmtTagGoods() {
		return prmtTagGoods;
	}

	public void setPrmtTagGoods(List<PrmtTagGoods> prmtTagGoods) {
		this.prmtTagGoods = prmtTagGoods;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", no=" + no + ", goodsId=" + goodsId + ", goodsName=" + goodsName + ", catId=" + catId + ", title=" + title + ", quantity=" + quantity + ", purchPrice=" + purchPrice + ", wholePrice=" + wholePrice
				+ ", origPrice=" + origPrice + ", salePrice=" + salePrice + ", marketPrice=" + marketPrice + ", weight=" + weight + ", volume=" + volume + ", seqNo=" + seqNo + ", shelfStatus=" + shelfStatus + ", shelfTime=" + shelfTime
				+ ", packList=" + packList + ", deleted=" + deleted + ", changeTime=" + changeTime + ", createTime=" + createTime + ", indexTime=" + indexTime + ", staticTime=" + staticTime + ", htmlUuid=" + htmlUuid + ", htmlUsage="
				+ htmlUsage + ", htmlPath=" + htmlPath + ", giftFlag=" + giftFlag + ", existFlag=" + existFlag + ", specVals=" + specVals + ", goods=" + goods + ", productAlbumImgs=" + productAlbumImgs + ", unit=" + unit + ", weightUnit="
				+ weightUnit + ", volumeUnit=" + volumeUnit + ", prmtTagGoods=" + prmtTagGoods + ", buySum=" + buySum + "]";
	}

}
