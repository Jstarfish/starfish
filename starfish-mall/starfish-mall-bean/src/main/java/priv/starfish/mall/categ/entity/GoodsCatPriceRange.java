package priv.starfish.mall.categ.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;

@Table(name = "goods_cat_price_range", desc = "商品分类价格区间")
public class GoodsCatPriceRange implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 分类Id */
	@Column(type = Types.INTEGER, nullable = false)
	@ForeignKey(refEntityClass = GoodsCat.class, refFieldName = "id")
	private Integer catId;

	/** 价格下限 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer lowerPrice;

	/** 价格上限 */
	@Column(type = Types.INTEGER)
	private Integer upperPrice;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public Integer getLowerPrice() {
		return lowerPrice;
	}

	public void setLowerPrice(Integer lowerPrice) {
		this.lowerPrice = lowerPrice;
	}

	public Integer getUpperPrice() {
		return upperPrice;
	}

	public void setUpperPrice(Integer upperPrice) {
		this.upperPrice = upperPrice;
	}

	@Override
	public String toString() {
		return "GoodsCatPriceRange [id=" + id + ", catId=" + catId + ", lowerPrice=" + lowerPrice + ", upperPrice=" + upperPrice + "]";
	}

}
