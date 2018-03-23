package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.mall.comn.misc.HasImage;

/**
 * 店铺营业执照图片
 * 
 * @author 郝江奎
 * @date 2015-10-9 上午10:30:07
 * 
 */
@Table(name = "biz_license_pic", desc = "店铺营业执照图片")
public class BizLicensePic implements Serializable, HasImage {

	private static final long serialVersionUID = -515348260548242614L;

	@Id(type = Types.INTEGER, desc="图片ID")
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc="营业执照ID")
	private Integer licenseId;
	
	@Column(type = Types.VARCHAR, length = 30, desc="图片标题")
	private String title;

	@Column(nullable = false, type = Types.VARCHAR, length = 60, desc="图片 uuid")
	private String imageUuid;
	
	@Column(type = Types.VARCHAR, length = 30, desc="图片 usage")
	private String imageUsage;
	
	@Column(type = Types.VARCHAR, length = 250, desc="图片 path")
	private String imagePath;
	
	@Column(nullable = false, type = Types.INTEGER, desc="图片编号")
	private Integer seqNo;
	
	/** 图片浏览路径 */
	private String fileBrowseUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(Integer licenseId) {
		this.licenseId = licenseId;
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
		return "BizLicensePic [id=" + id + ", licenseId=" + licenseId + ", title=" + title + ", imageUuid=" + imageUuid + ", imageUsage=" + imageUsage + ", imagePath=" + imagePath + ", seqNo=" + seqNo + ", fileBrowseUrl=" + fileBrowseUrl
				+ "]";
	}

}
