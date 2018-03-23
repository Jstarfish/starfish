package priv.starfish.mall.shop.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.comn.misc.HasImage;

@Table(name = "shop_album_img", uniqueConstraints = { @UniqueConstraint(fieldNames = { "imageUuid" }) })
public class ShopAlbumImg implements Serializable, HasImage {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 唯一标识 shopId = shop.id的值 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer shopId;

	/** 标题 */
	@Column(type = Types.VARCHAR, length = 30)
	private String title;

	/** 图片 imageUuid */
	@Column(nullable = false, type = Types.VARCHAR, length = 60)
	private String imageUuid;

	/** 图片 imageUsage */
	@Column(type = Types.VARCHAR, length = 30)
	private String imageUsage;

	/** 图片路径 */
	@Column(type = Types.VARCHAR, length = 250)
	private String imagePath;

	/** 序号 */
	@Column(nullable = false, type = Types.INTEGER)
	private int seqNo;

	/** 添加时间 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	/** 图片浏览路径 */
	private String fileBrowseUrl;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUuid() {
		return imageUuid;
	}

	public void setImageUuid(String imageUuid) {
		this.imageUuid = imageUuid;
	}

	public String getImageUsage() {
		return imageUsage;
	}

	public void setImageUsage(String imageUsage) {
		this.imageUsage = imageUsage;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	@Override
	public String toString() {
		return "ShopAlbumImg [id=" + id + ", shopId=" + shopId + ", title=" + title + ", imageUuid=" + imageUuid + ", imageUsage=" + imageUsage + ", imagePath=" + imagePath + ", seqNo=" + seqNo + ", ts=" + ts + ", fileBrowseUrl="
				+ fileBrowseUrl + "]";
	}
}
