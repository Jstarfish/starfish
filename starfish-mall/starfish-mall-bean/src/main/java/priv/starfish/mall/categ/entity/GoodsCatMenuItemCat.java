package priv.starfish.mall.categ.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.common.json.JsonDateTimeDeserializer;

@Table(name = "goods_cat_menu_item_cat", uniqueConstraints = { @UniqueConstraint(fieldNames = { "menuItemId", "name" }) }, desc = "商品分类菜单项_商品分类")
public class GoodsCatMenuItemCat implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 菜单项id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = GoodsCatMenuItem.class, refFieldName = "id")
	private Integer menuItemId;

	/** 是否关联实际商品分类 id ，是表示 name 取值默认为该分类的名称，url会在静态化时生成；否表示 name 为填入的名称且linkUrl是自定义的url */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean catFlag;

	/** 商品分类id */
	@Column(type = Types.INTEGER)
	private Integer catId;

	/** 名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** url */
	@Column(type = Types.VARCHAR, length = 250)
	private String linkUrl;

	/** 是否强调此最终分类链接（比如像京东的分类一样以红字表示） */
	@Column(type = Types.BOOLEAN, defaultValue = "false")
	private Boolean emphFlag;

	/** 序列号 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer seqNo;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonDateTimeDeserializer.class)
	private Date ts;

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

	public Boolean getCatFlag() {
		return catFlag;
	}

	public void setCatFlag(Boolean catFlag) {
		this.catFlag = catFlag;
	}

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public Boolean getEmphFlag() {
		return emphFlag;
	}

	public void setEmphFlag(Boolean emphFlag) {
		this.emphFlag = emphFlag;
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

	public GoodsCatMenuItem getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(GoodsCatMenuItem menuItem) {
		this.menuItem = menuItem;
	}

	@Override
	public String toString() {
		return "GoodsCatMenuItemCat [id=" + id + ", menuItemId=" + menuItemId + ", catFlag=" + catFlag + ", catId=" + catId + ", name=" + name + ", linkUrl=" + linkUrl + ", emphFlag=" + emphFlag + ", seqNo=" + seqNo + ", ts=" + ts
				+ ", menuItem=" + menuItem + "]";
	}

}
