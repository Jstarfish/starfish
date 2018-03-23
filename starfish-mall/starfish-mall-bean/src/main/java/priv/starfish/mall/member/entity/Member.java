package priv.starfish.mall.member.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.User;

@Table(name = "member", uniqueConstraints = {})
public class Member implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER, auto = false)
	private Integer id;

	/** 头像uuid */
	@Column(type = Types.VARCHAR, length = 60)
	private String headUuid;

	/** 头像图片用途 */
	@Column(type = Types.VARCHAR, length = 30)
	private String headUsage;

	/** 头像图片路径 */
	@Column(type = Types.VARCHAR, length = 250)
	private String headPath;

	@Column(type = Types.VARCHAR, length = 15, desc = "从前台添加时为null，从商城添加时为null，从店铺添加时为shop")
	private AuthScope scope;

	@Column(type = Types.INTEGER, desc = "从前台添加时为null，从商城添加时为null，从店铺添加时为shop.id")
	private Integer entityId;

	/** 头像图片浏览路径 */
	private String fileBrowseUrl;

	/** 是否禁用 */
	@Column(type = Types.BOOLEAN, nullable = false, defaultValue = "false")
	private Boolean disabled;

	/** 备忘录 */
	@Column(type = Types.VARCHAR, length = 250)
	private String memo;

	/** 积分 */
	@Column(type = Types.INTEGER, defaultValue = "0")
	private Integer point;

	/** 等级 */
	@Column(type = Types.INTEGER, defaultValue = "1")
	private Integer grade;

	@Column(nullable = false, type = Types.INTEGER, desc = "身份（为购买过的最高ecard的最大的rank值）", defaultValue = "0")
	private Integer rank;

	private MemberGrade gradeVO;

	private User user;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHeadUuid() {
		return headUuid;
	}

	public void setHeadUuid(String headUuid) {
		this.headUuid = headUuid;
	}

	public String getHeadUsage() {
		return headUsage;
	}

	public void setHeadUsage(String headUsage) {
		this.headUsage = headUsage;
	}

	public String getHeadPath() {
		return headPath;
	}

	public void setHeadPath(String headPath) {
		this.headPath = headPath;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public MemberGrade getGradeVO() {
		return gradeVO;
	}

	public void setGradeVO(MemberGrade gradeVO) {
		this.gradeVO = gradeVO;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	public AuthScope getScope() {
		return scope;
	}

	public void setScope(AuthScope scope) {
		this.scope = scope;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@Override
	public String toString() {
		return "Member [id=" + id + ", headUuid=" + headUuid + ", headUsage=" + headUsage + ", headPath=" + headPath + ", scope=" + scope + ", entityId=" + entityId + ", fileBrowseUrl=" + fileBrowseUrl + ", disabled=" + disabled + ", memo="
				+ memo + ", point=" + point + ", grade=" + grade + ", rank=" + rank + ", gradeVO=" + gradeVO + ", user=" + user + "]";
	}

}
