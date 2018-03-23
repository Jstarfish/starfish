package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "site_res", uniqueConstraints = { @UniqueConstraint(fieldNames = { "funcId", "resId" }) })
public class SiteResource implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/**
	 * 功能ID
	 */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = SiteFunction.class, refFieldName = "id")
	private Integer funcId;

	/**
	 * 资源ID
	 */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = Resource.class, refFieldName = "id")
	private Integer resId;

	/**
	 * 名称
	 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String dispName;

	/**
	 * 排序
	 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1")
	private Integer seqNo;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts = new Date();

	public SiteResource() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFuncId() {
		return funcId;
	}

	public void setFuncId(Integer funcId) {
		this.funcId = funcId;
	}

	public Integer getResId() {
		return resId;
	}

	public void setResId(Integer resId) {
		this.resId = resId;
	}

	public String getDispName() {
		return dispName;
	}

	public void setDispName(String dispName) {
		this.dispName = dispName;
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

	@Override
	public String toString() {
		return "SiteResource [id=" + id + ", funcId=" + funcId + ", resId=" + resId + ", dispName=" + dispName + ", seqNo=" + seqNo + ", ts=" + ts + "]";
	}

}
