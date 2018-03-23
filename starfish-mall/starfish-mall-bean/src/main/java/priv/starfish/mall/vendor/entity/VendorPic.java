package priv.starfish.mall.vendor.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;

/**
 * 供应商照片
 * 
 * @author "WJJ"
 * @date 2015年10月16日 上午9:44:23
 *
 */
@Table(name = "vendor_pic", desc = "供应商照片表")
public class VendorPic implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.BIGINT)
	private Long id;

	/** 供应商Id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = Vendor.class, refFieldName = "id")
	private Integer vendorId;

	/** 编号 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "code:IDCert.front[身份证正面]/IDCert.back[身份证反面]")
	private String code;

	/** 相片标题 */
	@Column(type = Types.VARCHAR, length = 30)
	private String title;

	/** uuid */
	@Column(nullable = false, type = Types.VARCHAR, length = 60)
	private String imageUuid;

	/** 用途：image.photo */
	@Column(type = Types.VARCHAR, length = 30)
	private String imageUsage;

	/** 路径 */
	@Column(type = Types.VARCHAR, length = 250)
	private String imagePath;

	/** 排序号 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1")
	private Integer seqNo;

	/** 图片浏览路径 */
	private String fileBrowseUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
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

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	@Override
	public String toString() {
		return "VendorPicture [id=" + id + ", vendorId=" + vendorId + ", code=" + code + ", title=" + title + ", imageUuid=" + imageUuid + ", imageUsage=" + imageUsage + ", imagePath=" + imagePath + ", seqNo=" + seqNo + ", fileBrowseUrl="
				+ fileBrowseUrl + "]";
	}

}
