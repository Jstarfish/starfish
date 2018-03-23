package priv.starfish.mall.shop.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.mall.comn.misc.HasImage;

@Table(name = "dist_shop_pic", desc = "商户照片表")
public class DistShopPic implements Serializable, HasImage {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.BIGINT)
	private Long id;
	
	/** 卫星店Id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = DistShop.class, refFieldName = "id")
	private Integer distShopId;
	
	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc="code:IDCert.front[身份证正面]/IDCert.back[身份证反面]")
	private String code;
	
	/** 相片标题 */
	@Column(type = Types.VARCHAR, length = 30)
	private String title;
	
	/** uuid */
	@Column(nullable = false, type = Types.VARCHAR, length = 60)
	private String imageUuid;

	/** 用途：image.goods \ intro */
	@Column(type = Types.VARCHAR, length = 30)
	private String imageUsage;

	/** 路径 */
	@Column(type = Types.VARCHAR, length = 250)
	private String imagePath;
	
	/** 图片浏览路径 */
	private String fileBrowseUrl;

	/** 排序号 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer seqNo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDistShopId() {
		return distShopId;
	}

	public void setDistShopId(Integer distShopId) {
		this.distShopId = distShopId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	@Override
	public String toString() {
		return "DistShopPic [id=" + id + ", distShopId=" + distShopId + ", code=" + code + ", title=" + title + ", imageUuid=" + imageUuid + ", imageUsage=" + imageUsage + ", imagePath=" + imagePath + ", fileBrowseUrl=" + fileBrowseUrl
				+ ", seqNo=" + seqNo + "]";
	}

}
