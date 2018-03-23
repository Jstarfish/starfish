package priv.starfish.mall.goods.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;

@Table(name = "goods_intro_img", desc = "商品介绍图片")
public class GoodsIntroImg implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.BIGINT)
	private Long id;

	/** 商品 */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = Goods.class, refFieldName = "id")
	private Integer goodsId;

	/** uuid */
	@Column(nullable = false, type = Types.VARCHAR, length = 60)
	private String imageUuid;

	/** 用途：image.goods \ intro */
	@Column(type = Types.VARCHAR, length = 60)
	private String imageUsage;

	/** 路径 */
	@Column(type = Types.VARCHAR, length = 60)
	private String imagePath;
	
	/** 图片浏览路径 */
	private String fileBrowseUrl;
	
	/** 图片删除路径 */
	private String fileDeleteUrl;
	
	/** 图片uuid*/
	private String fileUuid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
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

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	public String getFileUuid() {
		return fileUuid;
	}

	public void setFileUuid(String fileUuid) {
		this.fileUuid = fileUuid;
	}

	public String getFileDeleteUrl() {
		return fileDeleteUrl;
	}

	public void setFileDeleteUrl(String fileDeleteUrl) {
		this.fileDeleteUrl = fileDeleteUrl;
	}

	@Override
	public String toString() {
		return "GoodsIntroImg [id=" + id + ", goodsId=" + goodsId + ", imageUuid=" + imageUuid + ", imageUsage=" + imageUsage + ", imagePath=" + imagePath + ", fileBrowseUrl=" + fileBrowseUrl + ", fileDeleteUrl=" + fileDeleteUrl
				+ ", fileUuid=" + fileUuid + "]";
	}

}
