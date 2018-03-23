package priv.starfish.mall.categ.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.mall.comn.dict.VolumeUnit;
import priv.starfish.mall.comn.dict.WeightUnit;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "goods_cat", uniqueConstraints = { @UniqueConstraint(fieldNames = { "name", "level" }) })
public class GoodsCat implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 分类名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** 级别 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer level;

	/** 父节点Id */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer parentId;

	/** 父节点Id，构造树使用 */
	private Integer pId;

	/** 所有父节点 */
	@Column(nullable = false, type = Types.VARCHAR, length = 60)
	private String idPath;

	/** 计数单位 */
	@Column(type = Types.VARCHAR, length = 15)
	private String unit;

	/** 重量单位 (mg, g, kg) */
	@Column(type = Types.VARCHAR, length = 15)
	private WeightUnit weightUnit;
	
	@Column(type = Types.VARCHAR, length = 15, desc="体积单位")
	private VolumeUnit volumeUnit;

	/** 是否启用规格 */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean hasSpec;

	/** 序号 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1")
	private Integer seqNo;
	
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false", desc = "叶子节点标识")
	private Boolean leafFlag;

	/** 最小价格 */
	@Column(type = Types.DECIMAL, precision = 18, scale = 4)
	private BigDecimal minPrice;

	/** 最大价格 */
	@Column(type = Types.DECIMAL, precision = 18, scale = 4)
	private BigDecimal maxPrice;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	/** 商品分类属性关联 */
	List<GoodsCatAttr> catAttrs;

	/** 商品分类规格关联 */
	List<GoodsCatSpec> catSpecs;

	/** 商品分类分组 */
	List<GoodsCatAttrGroup> catAttrGroups;

	/** 存储分组与属性规格关联 */
	Map<String, Object> groupDatas;

	/** 商品分类价格区间 */
	List<GoodsCatPriceRange> goodsCatPriceRanges;

	/** 子分类集合 */
	List<GoodsCat> sonsList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getpId() {
		return pId;
	}

	public void setpId(Integer pId) {
		this.pId = pId;
	}

	public String getIdPath() {
		return idPath;
	}

	public void setIdPath(String idPath) {
		this.idPath = idPath;
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

	public Boolean getHasSpec() {
		return hasSpec;
	}

	public void setHasSpec(Boolean hasSpec) {
		this.hasSpec = hasSpec;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Boolean getLeafFlag() {
		return leafFlag;
	}

	public void setLeafFlag(Boolean leafFlag) {
		this.leafFlag = leafFlag;
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

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public List<GoodsCatAttr> getCatAttrs() {
		return catAttrs;
	}

	public void setCatAttrs(List<GoodsCatAttr> catAttrs) {
		this.catAttrs = catAttrs;
	}

	public List<GoodsCatSpec> getCatSpecs() {
		return catSpecs;
	}

	public void setCatSpecs(List<GoodsCatSpec> catSpecs) {
		this.catSpecs = catSpecs;
	}

	public List<GoodsCatAttrGroup> getCatAttrGroups() {
		return catAttrGroups;
	}

	public void setCatAttrGroups(List<GoodsCatAttrGroup> catAttrGroups) {
		this.catAttrGroups = catAttrGroups;
	}

	public Map<String, Object> getGroupDatas() {
		return groupDatas;
	}

	public void setGroupDatas(Map<String, Object> groupDatas) {
		this.groupDatas = groupDatas;
	}

	public List<GoodsCatPriceRange> getGoodsCatPriceRanges() {
		return goodsCatPriceRanges;
	}

	public void setGoodsCatPriceRanges(List<GoodsCatPriceRange> goodsCatPriceRanges) {
		this.goodsCatPriceRanges = goodsCatPriceRanges;
	}

	public List<GoodsCat> getSonsList() {
		return sonsList;
	}

	public void setSonsList(List<GoodsCat> sonsList) {
		this.sonsList = sonsList;
	}

	public VolumeUnit getVolumeUnit() {
		return volumeUnit;
	}

	public void setVolumeUnit(VolumeUnit volumeUnit) {
		this.volumeUnit = volumeUnit;
	}

	@Override
	public String toString() {
		return "GoodsCat [id=" + id + ", name=" + name + ", level=" + level + ", parentId=" + parentId + ", pId=" + pId + ", idPath=" + idPath + ", unit=" + unit + ", weightUnit=" + weightUnit + ", volumeUnit=" + volumeUnit + ", hasSpec="
				+ hasSpec + ", seqNo=" + seqNo + ", leafFlag=" + leafFlag + ", minPrice=" + minPrice + ", maxPrice=" + maxPrice + ", ts=" + ts + ", catAttrs=" + catAttrs + ", catSpecs=" + catSpecs + ", catAttrGroups=" + catAttrGroups
				+ ", groupDatas=" + groupDatas + ", goodsCatPriceRanges=" + goodsCatPriceRanges + ", sonsList=" + sonsList + "]";
	}
}
