package priv.starfish.mall.logistic.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;

@Table(name = "logistic_api_param", uniqueConstraints = { @UniqueConstraint(fieldNames = { "apiId", "name", "ioFlag" }) })
public class LogisApiParam implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 物流查询接口Id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = LogisApi.class, refFieldName = "id")
	private Integer apiId;

	/** 名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** 变量表达式的标志 */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean varFlag;

	/** 值或表达式 */
	@Column(type = Types.VARCHAR, length = 1000)
	private String value;

	/** 是否为请求或响应参数的标志 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1")
	private Integer ioFlag;

	/** 序号 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1")
	private Integer seqNo;

	/** 描述 */
	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApiId() {
		return apiId;
	}

	public void setApiId(Integer apiId) {
		this.apiId = apiId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getVarFlag() {
		return varFlag;
	}

	public void setVarFlag(Boolean varFlag) {
		this.varFlag = varFlag;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getIoFlag() {
		return ioFlag;
	}

	public void setIoFlag(Integer ioFlag) {
		this.ioFlag = ioFlag;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "LogisticApiParam [id=" + id + ", apiId=" + apiId + ", name=" + name + ", varFlag=" + varFlag + ", value=" + value + ", ioFlag=" + ioFlag + ", seqNo=" + seqNo + ", desc=" + desc + "]";
	}

}
