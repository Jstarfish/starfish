package priv.starfish.mall.interact.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.User;

@Table(name = "goods_inquiry_reply", desc = "商品咨询回复表")
public class GoodsInquiryReply implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.BIGINT)
	private long id;

	/** 咨询id */
	@Column(type = Types.BIGINT, nullable = false)
	@ForeignKey(refEntityClass = GoodsInquiry.class, refFieldName = "id")
	private long inquiryId;

	/** 范围 */
	@Column(type = Types.VARCHAR, length = 15, nullable = false)
	private AuthScope scope;

	/** 实体id */
	@Column(type = Types.INTEGER, nullable = false)
	private Integer entityId;

	/** 用户id */
	@Column(type = Types.INTEGER, nullable = false)
	@ForeignKey(refEntityClass = User.class, refFieldName = "id")
	private Integer userId;

	/** 回复内容 */
	@Column(type = Types.VARCHAR, length = 250, nullable = false)
	private String content;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date ts;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getInquiryId() {
		return inquiryId;
	}

	public void setInquiryId(long inquiryId) {
		this.inquiryId = inquiryId;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		return "GoodsInquiryReply [id=" + id + ", inquiryId=" + inquiryId + ", scope=" + scope + ", entityId=" + entityId + ", userId=" + userId + ", content=" + content + ", ts=" + ts + "]";
	}

	
}
