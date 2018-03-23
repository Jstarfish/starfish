package priv.starfish.mall.categ.dto;

import java.util.Date;
import java.util.List;

import priv.starfish.mall.categ.entity.GoodsCatMenu;
import priv.starfish.mall.categ.entity.GoodsCatMenuItemLink;

public class GoodsCatMenuItemDto {
	/** 主键 */
	private Integer id;
	/** 菜单id */
	private Integer menuId;
	/** 父节点Id */
	private Integer parentId;
	/** 级别 */
	private Integer level;
	/** 名称 */
	private String name;
	/** 是否有链接，true:1有；false：0无； */
	private Boolean linkFlag;
	/** 序列号 */
	private Integer seqNo;
	/** 时间戳 */
	private Date ts;
	/** 菜单 */
	private GoodsCatMenu menu;
	/** 菜单项链接 */
	private List<GoodsCatMenuItemLink> links;

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

	public List<GoodsCatMenuItemLink> getLinks() {
		return links;
	}

	public void setLinks(List<GoodsCatMenuItemLink> links) {
		this.links = links;
	}

}
