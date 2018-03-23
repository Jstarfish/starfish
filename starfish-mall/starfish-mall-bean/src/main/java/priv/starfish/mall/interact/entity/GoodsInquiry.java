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
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.goods.entity.Goods;
import priv.starfish.mall.goods.entity.Product;
import priv.starfish.mall.member.entity.Member;

@Table(name = "goods_inquiry", desc = "商品咨询表")
public class GoodsInquiry implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.BIGINT)
	private long id;

	/** 会员编号 */
	@Column(type = Types.INTEGER, nullable = false)
	@ForeignKey(refEntityClass = User.class, refFieldName = "id")
	private Integer userId;

	/** 商品id */
	@Column(type = Types.INTEGER, nullable = false)
	@ForeignKey(refEntityClass = Goods.class, refFieldName = "id")
	private Integer goodsId;

	/** 货品id */
	@Column(type = Types.BIGINT, nullable = false)
	@ForeignKey(refEntityClass = Product.class, refFieldName = "id")
	private long productId;

	/** 咨询类型 1:商品咨询、2:库存及配送、3:支付问题、4:发票及保修、5:促销及赠品 */
	@Column(type = Types.INTEGER, nullable = false)
	private Integer typeFlag;

	/** 咨询内容 */
	@Column(type = Types.VARCHAR, length = 250, nullable = false)
	private String content;

	/** 是否已回复 */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean replyFlag;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date ts;

	private String dateBegin;

	private String dateEnd;

	/** 咨询回复实体 */
	private GoodsInquiryReply goodsInquiryReply;

	/** 会员实体 */
	private Member member;

	/** 商品实体 */
	private Goods goods;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public Integer getTypeFlag() {
		return typeFlag;
	}

	public void setTypeFlag(Integer typeFlag) {
		this.typeFlag = typeFlag;
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

	public GoodsInquiryReply getGoodsInquiryReply() {
		return goodsInquiryReply;
	}

	public void setGoodsInquiryReply(GoodsInquiryReply goodsInquiryReply) {
		this.goodsInquiryReply = goodsInquiryReply;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public Boolean getReplyFlag() {
		return replyFlag;
	}

	public void setReplyFlag(Boolean replyFlag) {
		this.replyFlag = replyFlag;
	}

	public String getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	@Override
	public String toString() {
		return "GoodsInquiry [id=" + id + ", userId=" + userId + ", goodsId=" + goodsId + ", productId=" + productId + ", typeFlag=" + typeFlag + ", content=" + content + ", replyFlag=" + replyFlag + ", ts=" + ts + ", dateBegin="
				+ dateBegin + ", dateEnd=" + dateEnd + ", goodsInquiryReply=" + goodsInquiryReply + ", member=" + member + ", goods=" + goods + "]";
	}

}
