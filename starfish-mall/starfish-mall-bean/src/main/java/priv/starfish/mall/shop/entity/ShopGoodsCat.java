package priv.starfish.mall.shop.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.mall.categ.entity.GoodsCat;

/**
 * 店铺商品分类关联表
 * 
 * @author 廖晓远
 * @date 2015-5-29 上午10:50:07
 * 
 */
@Table(name = "shop_goods_cat", uniqueConstraints = { @UniqueConstraint(fieldNames = { "shopId", "catId" }) }, desc = "店铺商品分类关联表")
public class ShopGoodsCat implements Serializable {

	private static final long serialVersionUID = -515348260548242614L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(type = Types.INTEGER, nullable = false)
	@ForeignKey(refEntityClass = GoodsCat.class, refFieldName = "id")
	private Integer catId;

	@Column(type = Types.INTEGER, nullable = false)
	@ForeignKey(refEntityClass = Shop.class, refFieldName = "id")
	private Integer shopId;

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

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	@Override
	public String toString() {
		return "ShopGoodsCat [id=" + id + ", catId=" + catId + ", shopId=" + shopId + "]";
	}

}
