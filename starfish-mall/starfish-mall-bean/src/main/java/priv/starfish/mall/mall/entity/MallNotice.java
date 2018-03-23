package priv.starfish.mall.mall.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonDateDeserializer;
import priv.starfish.common.json.JsonDateSerializer;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "mall_notice")
public class MallNotice implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 公告标题 */
	@Column(nullable = false, type = Types.VARCHAR, length = 100)
	private String title;

	/** 公告内容 */
	@Column(type = Types.VARCHAR, length = 1000)
	private String content;

	/** 是否自动发布 =》 发布方式 ： true：1自动发布 / false：0手动发布 */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean autoFlag;

	/** 发布开始时间 */
	@Column(type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date pubTime;

	/** 发布结束时间 */
	@Column(type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date endTime;

	/** 发布开始日期 */
	@Column(type = Types.DATE)
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date startDate;

	/** 发布结束日期 */
	@Column(type = Types.DATE)
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date endDate;

	/** 0 : 未发布, 1: 发布中, 2：已停止 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Boolean getAutoFlag() {
		return autoFlag;
	}

	public void setAutoFlag(Boolean autoFlag) {
		this.autoFlag = autoFlag;
	}

	public Date getPubTime() {
		return pubTime;
	}

	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "MallNotice [id=" + id + ", title=" + title + ", content=" + content + ", autoFlag=" + autoFlag + ", pubTime=" + pubTime + ", endTime=" + endTime + ", startDate=" + startDate + ", endDate=" + endDate + ", status=" + status
				+ "]";
	}

}
