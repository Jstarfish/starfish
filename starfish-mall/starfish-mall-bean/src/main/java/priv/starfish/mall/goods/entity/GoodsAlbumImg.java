package priv.starfish.mall.goods.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.comn.misc.HasImage;

@Table(name = "goods_album_img", desc = "商品相册图片")
public class GoodsAlbumImg implements Serializable, HasImage {
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

	/** 用途：image.goods \ album */
	@Column(type = Types.VARCHAR, length = 30)
	private String imageUsage;

	/** 路径 */
	@Column(type = Types.VARCHAR, length = 250)
	private String imagePath;

	/** 图片浏览路径 */
	private String fileBrowseUrl;

	/** 图片名称 */
	private String fileName;

	/** 图片uuid */
	private String fileUuid;

	/** 图片分组（如 白色款/红色款） */
	@Column(type = Types.VARCHAR, length = 30)
	private String imageGroup;

	/** 序号 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1")
	private Integer seqNo;

	/** 路径 */
	@Column(type = Types.VARCHAR, length = 250)
	private String thumbPath;

	/** 路径 */
	@Column(type = Types.VARCHAR, length = 250)
	private String middlePath;

	/** 路径 */
	@Column(type = Types.VARCHAR, length = 250)
	private String smallPath;

	/** 最后一次改变的时间 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date changeTime;

	/** 操作时间 */
	@Column(type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date handleTime;

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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUuid() {
		return fileUuid;
	}

	public void setFileUuid(String fileUuid) {
		this.fileUuid = fileUuid;
	}

	public String getImageGroup() {
		return imageGroup;
	}

	public void setImageGroup(String imageGroup) {
		this.imageGroup = imageGroup;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getThumbPath() {
		return thumbPath;
	}

	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}

	public String getMiddlePath() {
		return middlePath;
	}

	public void setMiddlePath(String middlePath) {
		this.middlePath = middlePath;
	}

	public String getSmallPath() {
		return smallPath;
	}

	public void setSmallPath(String smallPath) {
		this.smallPath = smallPath;
	}

	public Date getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	@Override
	public String toString() {
		return "GoodsAlbumImg [id=" + id + ", goodsId=" + goodsId + ", imageUuid=" + imageUuid + ", imageUsage=" + imageUsage + ", imagePath=" + imagePath + ", fileBrowseUrl=" + fileBrowseUrl + ", fileName=" + fileName + ", fileUuid="
				+ fileUuid + ", imageGroup=" + imageGroup + ", seqNo=" + seqNo + ", thumbPath=" + thumbPath + ", middlePath=" + middlePath + ", smallPath=" + smallPath + ", changeTime=" + changeTime + ", handleTime=" + handleTime + "]";
	}

}
