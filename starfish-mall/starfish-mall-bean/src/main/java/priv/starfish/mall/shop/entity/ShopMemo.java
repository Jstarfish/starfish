package priv.starfish.mall.shop.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

/**
 * 店铺备忘录
 * @author 邓华锋
 * @date 2016年02月19日 18:09:44
 *
 */
@Table(name = "shop_memo")
public class ShopMemo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id(type = Types.INTEGER)
	private Integer id;
	
	@Column(nullable = false,type = Types.INTEGER,desc = " 店铺ID")
	@ForeignKey(refEntityClass = Shop.class, refFieldName = "id")
	private Integer shopId;
	
	@Column(type = Types.VARCHAR,length = 30,desc = "店铺名称")
	private String shopName;
	
	@Column(nullable = false,type = Types.VARCHAR,length = 30,desc = "标题")
	private String title;
	
	@Column(nullable = false,type = Types.VARCHAR,length = 250,desc = "内容")
	private String content;
	
	@Column(nullable = false,type = Types.TIMESTAMP,desc = "创建时间",defaultValue="CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date createTime;
	
	@Column(type = Types.BOOLEAN,desc = "是否已删除",defaultValue="false")
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Boolean deleted;
	
	@Column(type = Types.TIMESTAMP,desc = "删除时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date deleteTime;
	
	@Column(nullable = false,type = Types.TIMESTAMP,desc = "更新时间",defaultValue="CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;
	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getShopId() {
		return shopId;
	}
	
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	
	public String getShopName() {
		return shopName;
	}
	
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Boolean getDeleted() {
		return deleted;
	}
	
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	public Date getDeleteTime() {
		return deleteTime;
	}
	
	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}
	
	public Date getTs() {
		return ts;
	}
	
	public void setTs(Date ts) {
		this.ts = ts;
	}
	
	public String toString() {
		return "ShopMemo ["+"id=" + id + ", "+"shopId=" + shopId + ", "+"shopName=" + shopName + ", "+"title=" + title + ", "+"content=" + content + ", "+"createTime=" + createTime + ", "+"deleted=" + deleted + ", "+"deleteTime=" + deleteTime + ", "+"ts=" + ts+"]";
	}
	
}