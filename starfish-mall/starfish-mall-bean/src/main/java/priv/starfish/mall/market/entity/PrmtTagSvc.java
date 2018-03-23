package priv.starfish.mall.market.entity;

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
import priv.starfish.mall.svcx.entity.Svcx;

@Table(name = "prmt_tag_svc", uniqueConstraints = { @UniqueConstraint(fieldNames = { "tagId", "svcId" }) }, desc = "促销标签服务")
public class PrmtTagSvc implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT, desc="主键")
	private Long id;

	@Column(nullable = false, type = Types.INTEGER, desc="促销标签id")
	@ForeignKey(refEntityClass = PrmtTag.class, refFieldName = "id")
	private Integer tagId;

	@Column(nullable = false, type = Types.INTEGER, desc="服务id")
	@ForeignKey(refEntityClass = Svcx.class, refFieldName = "id")
	private Integer svcId;
	
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1", desc="序号")
	private Integer seqNo;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc="时间戳")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;
	
	private Svcx svcx;
	
	private PrmtTag prmtTag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public Integer getSvcId() {
		return svcId;
	}

	public void setSvcId(Integer svcId) {
		this.svcId = svcId;
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

	public Svcx getSvcx() {
		return svcx;
	}

	public void setSvcx(Svcx svcx) {
		this.svcx = svcx;
	}

	public PrmtTag getPrmtTag() {
		return prmtTag;
	}

	public void setPrmtTag(PrmtTag prmtTag) {
		this.prmtTag = prmtTag;
	}

	@Override
	public String toString() {
		return "PrmtTagSvc [id=" + id + ", tagId=" + tagId + ", svcId=" + svcId + ", seqNo=" + seqNo + ", ts=" + ts
				+ ", svcx=" + svcx + ", prmtTag=" + prmtTag + "]";
	}

}
