package priv.starfish.mall.goods.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import priv.starfish.common.model.Couple;
import priv.starfish.mall.categ.entity.GoodsCatSpecItem;
import priv.starfish.mall.categ.entity.SpecRef;
import priv.starfish.mall.goods.entity.GoodsIntro;
import priv.starfish.mall.goods.entity.Product;

public class ProductDto implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 产品Id */
	private Long id;

	/** 产品 */
	private Product product;

	/** 购买汇总数 */
	private Long buySum;

	/** 销售价格 */
	private BigDecimal salePrice;

	/** 市场价格 */
	private BigDecimal marketPrice;
	
	/** 标题 */
	private String title;
	
	//----------------------------------------------------------
	
	/** 商品id */
	private Integer goodsId;
	
	/** 商品扩展信息 key:specRef, value: List<GoodsCatSpecItem>*/
	private Map<String, Couple<SpecRef, List<GoodsCatSpecItem>>> goodsEx;
	
	/** 货品扩展信息 key:specCode, value: specItemId*/
	private Map<String, Integer> productEx;
	
	//----------------------------------------------------------
	
	/** 商品介绍 */
	private GoodsIntro goodsIntro;
	
	/** 货品相册图片路径列表 */
	private List<String> albumImgUrls;
	
	/** 商品属性值列表 */
	private Map<String, String> keyAttrVals;
	
	/** 商品所有属性值列表 */
	private Map<String, String> attrVals;
	
	/** 货品所有规格值列表 */
	private Map<String, String> specVals;
	
	/** 商品颜色图片列表 key:specItemId, value:fileBrowsePath*/
	private Map<Integer, String> goodsColorImgs;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Long getBuySum() {
		return buySum;
	}

	public void setBuySum(Long buySum) {
		this.buySum = buySum;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public static ProductDto newOne(){
		return new ProductDto();
	}
	
	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Map<String, Couple<SpecRef, List<GoodsCatSpecItem>>>getGoodsEx() {
		return goodsEx;
	}

	public void setGoodsEx(Map<String, Couple<SpecRef, List<GoodsCatSpecItem>>> goodsEx) {
		this.goodsEx = goodsEx;
	}

	public Map<String, Integer> getProductEx() {
		return productEx;
	}

	public void setProductEx(Map<String, Integer> productEx) {
		this.productEx = productEx;
	}

	public GoodsIntro getGoodsIntro() {
		return goodsIntro;
	}

	public void setGoodsIntro(GoodsIntro goodsIntro) {
		this.goodsIntro = goodsIntro;
	}
	
	public List<String> getAlbumImgUrls() {
		return albumImgUrls;
	}

	public void setAlbumImgUrls(List<String> albumImgUrls) {
		this.albumImgUrls = albumImgUrls;
	}

	public Map<String, String> getKeyAttrVals() {
		return keyAttrVals;
	}

	public void setKeyAttrVals(Map<String, String> keyAttrVals) {
		this.keyAttrVals = keyAttrVals;
	}

	public Map<String, String> getAttrVals() {
		return attrVals;
	}

	public void setAttrVals(Map<String, String> attrVals) {
		this.attrVals = attrVals;
	}

	public Map<String, String> getSpecVals() {
		return specVals;
	}

	public void setSpecVals(Map<String, String> specVals) {
		this.specVals = specVals;
	}

	public Map<Integer, String> getGoodsColorImgs() {
		return goodsColorImgs;
	}

	public void setGoodsColorImgs(Map<Integer, String> goodsColorImgs) {
		this.goodsColorImgs = goodsColorImgs;
	}

	@Override
	public String toString() {
		return "ProductDto [id=" + id + ", product=" + product + ", buySum=" + buySum + ", salePrice=" + salePrice + ", marketPrice=" + marketPrice + ", title=" + title + ", goodsId=" + goodsId + ", goodsEx=" + goodsEx + ", productEx="
				+ productEx + ", goodsIntro=" + goodsIntro + ", albumImgUrls=" + albumImgUrls + ", keyAttrVals=" + keyAttrVals + ", attrVals=" + attrVals + ", specVals=" + specVals + ", goodsColorImgs=" + goodsColorImgs + "]";
	}

}
