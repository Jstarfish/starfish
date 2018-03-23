package priv.starfish.mall.categ.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;

@Table(name = "goods_cat_menu_item_ad", uniqueConstraints = { @UniqueConstraint(fieldNames = { "menuItemId", "imageUuid" }) }, desc = "商品分类菜单项广告")
public class GoodsCatMenuItemAd implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 菜单项id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = GoodsCatMenuItem.class, refFieldName = "id")
	private Integer menuItemId;

	/** uuid */
	@Column(nullable = false, type = Types.VARCHAR, length = 60)
	private String imageUuid;

	/** usage，对应image.advert */
	@Column(type = Types.VARCHAR, length = 30)
	private String imageUsage;

	/** path */
	@Column(type = Types.VARCHAR, length = 250)
	private String imagePath;

	/** url */
	@Column(nullable = false, type = Types.VARCHAR, length = 250)
	private String linkUrl;

	/** 序列号 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer seqNo;

	/** 菜单项 */
	private GoodsCatMenuItem menuItem;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMenuItemId() {
		return menuItemId;
	}

	public void setMenuItemId(Integer menuItemId) {
		this.menuItemId = menuItemId;
	}

	public String getImageUuid() {
		return imageUuid;
	}

	public void setImageUuid(String imageUuid) {
		this.imageUuid = imageUuid;
	}

	public String getImageUsage() {
		return imageUsage;
	}

	public void setImageUsage(String imageUsage) {
		this.imageUsage = imageUsage;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public GoodsCatMenuItem getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(GoodsCatMenuItem menuItem) {
		this.menuItem = menuItem;
	}

	@Override
	public String toString() {
		return "GoodsCatMenuItemAd [id=" + id + ", menuItemId=" + menuItemId + ", imageUuid=" + imageUuid + ", imageUsage=" + imageUsage + ", imagePath=" + imagePath + ", linkUrl=" + linkUrl + ", seqNo=" + seqNo + ", menuItem=" + menuItem
				+ "]";
	}

}
