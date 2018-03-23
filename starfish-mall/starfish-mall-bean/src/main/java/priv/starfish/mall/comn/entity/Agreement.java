package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "agreement", uniqueConstraints = { @UniqueConstraint(fieldNames = { "target" }) }, desc = "（各种）协议")
public class Agreement implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 协议对象：会员 member */
	public static final String TARGET_MEMBER = "member";
	/** 协议对象：商户 merchant */
	public static final String TARGET_MERCHANT = "merchant";
	/** 协议对象：代理 agent */
	public static final String TARGET_AGENT = "agent";
	/** 协议对象：供应商 agent */
	public static final String TARGET_VENDOR = "vendor";

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 协议对象 : member 会员, merchant 商户, agent 代理 */
	@Column(nullable = false, type = Types.VARCHAR, length = 15)
	private String target;

	/** 内容 */
	@Column(nullable = false, type = Types.CLOB)
	private String content;

	/** 序列号 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer seqNo;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
		return "Agreement [id=" + id + ", target=" + target + ", content=" + content + ", seqNo=" + seqNo + ", ts=" + ts + "]";
	}

}
