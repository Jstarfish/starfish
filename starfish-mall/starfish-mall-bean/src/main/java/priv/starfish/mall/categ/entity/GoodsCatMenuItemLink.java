package priv.starfish.mall.categ.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;

@Table(name = "goods_cat_menu_item_link", uniqueConstraints = { @UniqueConstraint(fieldNames = { "menuItemId", "name" }) }, desc = "商品分类菜单项链接")
public class GoodsCatMenuItemLink implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 菜单项id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = GoodsCatMenuItem.class, refFieldName = "id")
	private Integer menuItemId;

	/** 名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** 是否有链接，true:1有；false：0无； */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean linkFlag;

	/** url */
	@Column(type = Types.VARCHAR, length = 250)
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getLinkFlag() {
		return linkFlag;
	}

	public void setLinkFlag(Boolean linkFlag) {
		this.linkFlag = linkFlag;
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
		return "GoodsCatMenuItemLink [id=" + id + ", menuItemId=" + menuItemId + ", name=" + name + ", linkFlag=" + linkFlag + ", linkUrl=" + linkUrl + ", seqNo=" + seqNo + ", menuItem=" + menuItem + "]";
	}

}
