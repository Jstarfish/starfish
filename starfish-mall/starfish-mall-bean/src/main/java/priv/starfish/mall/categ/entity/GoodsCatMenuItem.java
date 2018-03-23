package priv.starfish.mall.categ.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.common.json.JsonDateTimeDeserializer;

@Table(name = "goods_cat_menu_item", uniqueConstraints = { @UniqueConstraint(fieldNames = { "menuId", "level", "name" }) }, desc = "商品分类菜单项")
public class GoodsCatMenuItem implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 菜单id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = GoodsCatMenu.class, refFieldName = "id")
	private Integer menuId;

	/** 父节点Id */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer parentId;

	/** 级别 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer level;

	/** 名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** 是否有链接，true:1有；false：0无； */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean linkFlag;

	/** 序列号 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer seqNo;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonDateTimeDeserializer.class)
	private Date ts;

	/** 菜单 */
	private GoodsCatMenu menu;

	/** 子菜单列表 */
	private List<GoodsCatMenuItem> sonsList;

	/** 关联分类列表 */
	private List<GoodsCatMenuItemCat> goodsCatList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public GoodsCatMenu getMenu() {
		return menu;
	}

	public void setMenu(GoodsCatMenu menu) {
		this.menu = menu;
	}

	public List<GoodsCatMenuItem> getSonsList() {
		return sonsList;
	}

	public void setSonsList(List<GoodsCatMenuItem> sonsList) {
		this.sonsList = sonsList;
	}

	public List<GoodsCatMenuItemCat> getGoodsCatList() {
		return goodsCatList;
	}

	public void setGoodsCatList(List<GoodsCatMenuItemCat> goodsCatList) {
		this.goodsCatList = goodsCatList;
	}

	@Override
	public String toString() {
		return "GoodsCatMenuItem [id=" + id + ", menuId=" + menuId + ", parentId=" + parentId + ", level=" + level + ", name=" + name + ", linkFlag=" + linkFlag + ", seqNo=" + seqNo + ", ts=" + ts + ", menu=" + menu + "]";
	}

}
