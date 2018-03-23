package priv.starfish.mall.shop.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.List;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.mall.comn.misc.HasIcon;

@Table(name = "shop_grade_def", uniqueConstraints = { @UniqueConstraint(fieldNames = { "grade", "level" }) }, desc = "店铺评级表")
public class ShopGrade implements Serializable, HasIcon {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键==用户Id
	 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/**
	 * 级别
	 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer grade;

	/**
	 * 档位
	 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer level;

	/** 名字 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/**
	 * 最小分数
	 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer lowerPoint;

	/**
	 * 最大分数
	 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer upperPoint;

	/** 图片路径 */
	@Column(nullable = false, type = Types.VARCHAR, length = 250)
	private String iconPath;

	/** icon UUID */
	@Column(type = Types.VARCHAR, length = 60)
	private String iconUuid;

	/** icon Usage */
	@Column(type = Types.VARCHAR, length = 30)
	private String iconUsage;
	
	/** 图片浏览路径 */
	private String fileBrowseUrl;
    private List<ShopGrade> shopGrades; 
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
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

	public Integer getLowerPoint() {
		return lowerPoint;
	}

	public void setLowerPoint(Integer lowerPoint) {
		this.lowerPoint = lowerPoint;
	}

	public Integer getUpperPoint() {
		return upperPoint;
	}

	public void setUpperPoint(Integer upperPoint) {
		this.upperPoint = upperPoint;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getIconUuid() {
		return iconUuid;
	}

	public void setIconUuid(String iconUuid) {
		this.iconUuid = iconUuid;
	}

	public String getIconUsage() {
		return iconUsage;
	}

	public void setIconUsage(String iconUsage) {
		this.iconUsage = iconUsage;
	}

	@Override
	public String toString() {
		return "ShopGrade [id=" + id + ", grade=" + grade + ", level=" + level + ", name=" + name + ", lowerPoint=" + lowerPoint + ", upperPoint=" + upperPoint + ", iconPath=" + iconPath + ", iconUuid=" + iconUuid + ", iconUsage="
				+ iconUsage + "]";
	}

	public ShopGrade() {
		super();
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	public List<ShopGrade> getShopGrades() {
		return shopGrades;
	}

	public void setShopGrades(List<ShopGrade> shopGrades) {
		this.shopGrades = shopGrades;
	}
	
}
