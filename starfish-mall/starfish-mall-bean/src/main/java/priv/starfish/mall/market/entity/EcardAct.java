package priv.starfish.mall.market.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonDateDeserializer;
import priv.starfish.common.json.JsonDateSerializer;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "ecard_act", uniqueConstraints = { @UniqueConstraint(fieldNames = { "year", "name" }) }, desc = "e卡活动")
public class EcardAct implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "发生的年份，取系统时间，不可更新")
	private Integer year;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "名称")
	private String name;

	@Column(type = Types.VARCHAR, length = 30, desc = "前台显示名称")
	private String title;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "开始时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date startTime;

	@Column(type = Types.TIMESTAMP, desc = "无值时代表长期有效")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date endTime;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	@Column(nullable = false, type = Types.INTEGER, desc = "排序号（基于年份计算）")
	private Integer seqNo;

	@Column(nullable = false, type = Types.BOOLEAN, desc = "是否禁用")
	private Boolean disabled;

	@Column(nullable = false, type = Types.BOOLEAN, desc = "删除标记")
	private Boolean deleted;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "创建时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date ts;

	private String actState;

	// 活动规则
	private List<EcardActRule> ecardRuleList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getActState() {
		return actState;
	}

	public void setActState(String actState) {
		this.actState = actState;
	}

	public List<EcardActRule> getEcardRuleList() {
		return ecardRuleList;
	}

	public void setEcardRuleList(List<EcardActRule> ecardRuleList) {
		this.ecardRuleList = ecardRuleList;
	}

	@Override
	public String toString() {
		return "EcardAct [id=" + id + ", year=" + year + ", name=" + name + ", title=" + title + ", startTime=" + startTime + ", endTime=" + endTime + ", desc=" + desc + ", seqNo=" + seqNo + ", disabled=" + disabled + ", deleted=" + deleted
				+ ", ts=" + ts + ", actState=" + actState + ", ecardRuleList=" + ecardRuleList + "]";
	}

}
