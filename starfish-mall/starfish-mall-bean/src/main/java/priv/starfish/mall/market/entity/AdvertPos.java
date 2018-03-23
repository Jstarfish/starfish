package priv.starfish.mall.market.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
@Table(name = "advert_pos", uniqueConstraints = { @UniqueConstraint(fieldNames = { "name" }) })
public class AdvertPos implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(auto = false, type = Types.VARCHAR, length = 30)
	private String code;

	/** 名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** 序号 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1")
	private Integer seqNo;

	/** 宽度 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer imageWidth;

	/** 高度 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer imageHeight;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Integer getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(Integer imageWidth) {
		this.imageWidth = imageWidth;
	}

	public Integer getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(Integer imageHeight) {
		this.imageHeight = imageHeight;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		return "AdvertPos [code=" + code + ", name=" + name + ", seqNo=" + seqNo + ", imageWidth=" + imageWidth + ", imageHeight=" + imageHeight + ", ts=" + ts + "]";
	}

}
