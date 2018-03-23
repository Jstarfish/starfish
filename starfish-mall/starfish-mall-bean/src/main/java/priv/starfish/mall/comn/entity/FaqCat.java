package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

/**
 * 常见问题分类
 * 
 * @author 邓华锋
 * @date 2015年9月19日 下午3:08:47
 *
 */
@Table(name = "faq_cat")
public class FaqCat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.VARCHAR, length = 10, desc = "分类名称")
	private String name;

	@Column(nullable = false, type = Types.INTEGER, desc = "排序号", defaultValue = "0")
	private Integer seqNo;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "添加时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;

	/**
	 * 常见问题分组列表
	 */
	private List<FaqGroup> faqGroups;

	public List<FaqGroup> getFaqGroups() {
		return faqGroups;
	}

	public void setFaqGroups(List<FaqGroup> faqGroups) {
		this.faqGroups = faqGroups;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		return "FaqCat [id=" + id + ", name=" + name + ", seqNo=" + seqNo + ", ts=" + ts + ", faqGroups=" + faqGroups + "]";
	}

}