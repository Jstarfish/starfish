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
 * 卫星店备忘录
 * 
 * @author 邓华锋
 * @date 2016年02月19日 19:11:12
 *
 */
@Table(name = "dist_shop_memo")
public class DistShopMemo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "卫星店ID")
	@ForeignKey(refEntityClass = DistShop.class, refFieldName = "id")
	private Integer distShopId;

	@Column(type = Types.VARCHAR, length = 30, desc = "卫星店名称")
	private String distShopName;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "标题")
	private String title;

	@Column(nullable = false, type = Types.VARCHAR, length = 250, desc = "内容")
	private String content;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "创建时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date createTime;

	@Column(type = Types.BOOLEAN, desc = "是否已删除",defaultValue="false")
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Boolean deleted;

	@Column(type = Types.TIMESTAMP, desc = "删除时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date deleteTime;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "更新时间",defaultValue="CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDistShopId() {
		return distShopId;
	}

	public void setDistShopId(Integer distShopId) {
		this.distShopId = distShopId;
	}

	public String getDistShopName() {
		return distShopName;
	}

	public void setDistShopName(String distShopName) {
		this.distShopName = distShopName;
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
		return "DistShopMemo [" + "id=" + id + ", " + "distShopId=" + distShopId + ", " + "distShopName=" + distShopName + ", " + "title=" + title + ", " + "content=" + content + ", " + "createTime=" + createTime + ", " + "deleted="
				+ deleted + ", " + "deleteTime=" + deleteTime + ", " + "ts=" + ts + "]";
	}

}