package priv.starfish.mall.comn.entity;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;

import java.io.Serializable;
import java.sql.Types;
import java.util.List;


@Table(name = "region", uniqueConstraints = { @UniqueConstraint(fieldNames = { "code" }), @UniqueConstraint(fieldNames = { "parentId", "name" }) }, desc = "地区表")
public class Region implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 地区表支持最大级别 */
	public static final int MAX_LEVEL = 4;

	/** 主键 */
	@Id(auto = false, type = Types.INTEGER)
	private Integer id;

	/** code */
	@Column(nullable = false, type = Types.VARCHAR, length = 15)
	private String code;

	/** 名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** 简拼 */
	@Column(type = Types.VARCHAR, length = 30)
	private String py;

	/** 级别 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer level;

	/** 父Id */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer parentId;

	/** 父code */
	@Column(nullable = false, type = Types.VARCHAR, length = 15)
	private String parentCode;

	/** 全路径名 */
	@Column(nullable = false, type = Types.VARCHAR, length = 100)
	private String idPath;

	/** 百度地图城市代码 */
	@Column(type = Types.INTEGER)
	private Integer bdCityCode;

	/** 序列号 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer seqNo;

	// 下级id列表
	private List<Integer> childIds;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getIdPath() {
		return idPath;
	}

	public void setIdPath(String idPath) {
		this.idPath = idPath;
	}

	public Integer getBdCityCode() {
		return bdCityCode;
	}

	public void setBdCityCode(Integer bdCityCode) {
		this.bdCityCode = bdCityCode;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public List<Integer> getChildIds() {
		return childIds;
	}

	public void setChildIds(List<Integer> childIds) {
		this.childIds = childIds;
	}

	@Override
	public String toString() {
		return "Region [id=" + id + ", code=" + code + ", name=" + name + ", py=" + py + ", level=" + level + ", parentId=" + parentId + ", parentCode=" + parentCode + ", bdCityCode=" + bdCityCode + ", idPath=" + idPath + ", seqNo=" + seqNo
				+ "]";
	}

}
