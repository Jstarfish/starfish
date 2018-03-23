package priv.starfish.mall.goods.doc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.IdField;
import priv.starfish.common.json.JsonDateTimeSerializer;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.model.Couple;
import priv.starfish.common.xsearch.EsDoc;
import priv.starfish.mall.categ.entity.GoodsCatSpecItem;
import priv.starfish.mall.categ.entity.SpecRef;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 产品（货品）doc
 * 
 * @author koqiui
 * @date 2015年10月21日 下午3:31:49
 *
 */
@IdField(name = "id")
public class ProductDoc extends EsDoc {
	
	/** 货品Id */
	private Long id;
	
	/** 货品编号 */
	@JsonInclude(Include.NON_NULL)
	private String no;
	
	/** 标题 */
	@JsonInclude(Include.NON_NULL)
	private String title;

	/** 销售价格 */
	private BigDecimal salePrice;

	/** 市场价格 */
	@JsonInclude(Include.NON_NULL)
	private BigDecimal marketPrice;
	
	/** 库存数量(-1表示长期有货) */
	private Integer quantity;
	
	/** 重量 */
	@JsonInclude(Include.NON_NULL)
	private Double weight;
	
	/** 体积 */
	@JsonInclude(Include.NON_NULL)
	private Double volume;
	
	/** 购买汇总数 */
	@JsonInclude(Include.NON_NULL)
	private Integer buySum;
	
	/** 浏览汇总数 */
	@JsonInclude(Include.NON_NULL)
	private Integer browseSum;
	
	/** 上架时间 */
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private Date shelfTime;
	
	//----------------------------------------------------------
	
	/** 商品id */
	private Integer goodsId;
	
	/** 货品相册图片路径列表 */
	@JsonInclude(Include.NON_NULL)
	private List<String> albumImgUrls;
	
	/** 商品关键属性值列表 */
	@JsonInclude(Include.NON_NULL)
	private Map<String, String> keyAttrVals;
	
	/** 商品所有属性值列表 */
	@JsonInclude(Include.NON_NULL)
	private Map<String, String> attrVals;
	
	/** 货品所有规格值列表 */
	@JsonInclude(Include.NON_NULL)
	private Map<String, String> specVals;
	
	/** 商品包装清单 */
	@JsonInclude(Include.NON_NULL)
	private String packList;
	
	//----------------------------------------------------------
	
	/** 商品介绍 */
	@JsonInclude(Include.NON_NULL)
	private String introContent;
	
	/** 商品颜色图片列表 key:specItemId, value:fileBrowsePath*/
	@JsonInclude(Include.NON_NULL)
	private Map<Integer, String> goodsColorImgs;
	
	/** 商品扩展信息 key:specRef, value: List<GoodsCatSpecItem>*/
	@JsonInclude(Include.NON_NULL)
	private Map<String, Couple<SpecRef, List<GoodsCatSpecItem>>> goodsEx;
	
	/** 货品扩展信息 key:specCode, value: specItemId*/
	@JsonInclude(Include.NON_NULL)
	private Map<String, Integer> productEx;
	

	//-------------------------getter/setter---------------------------------
	
	

}
