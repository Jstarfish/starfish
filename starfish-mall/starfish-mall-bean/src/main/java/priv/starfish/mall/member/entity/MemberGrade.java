package priv.starfish.mall.member.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;

@Table(name = "member_grade_def", uniqueConstraints = { @UniqueConstraint(fieldNames = { "grade" }) })
public class MemberGrade implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 等级 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer grade;

	/** 名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** 积分上限 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer lowerPoint;

	/** 积分下限 */
	@Column(type = Types.INTEGER)
	private Integer upperPoint;

	@Column(type = Types.VARCHAR, length = 60)
	private String iconUuid;

	@Column(type = Types.VARCHAR, length = 30)
	private String iconUsage;

	@Column(type = Types.VARCHAR, length = 250)
	private String iconPath;
	
	/** 图片浏览路径 */
	private String fileBrowseUrl;

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

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	@Override
	public String toString() {
		return "MemberGrade [id=" + id + ", grade=" + grade + ", name=" + name + ", lowerPoint=" + lowerPoint + ", upperPoint=" + upperPoint + ", iconUuid=" + iconUuid + ", iconUsage=" + iconUsage + ", iconPath=" + iconPath
				+ ", fileBrowseUrl=" + fileBrowseUrl + "]";
	}

}
