package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

/**
 * 常见问题
 * 
 * @author 邓华锋
 * @date 2015年9月19日 下午3:08:47
 *
 */
@Table(name = "faq")
public class Faq implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "所属分组主键")
	@ForeignKey(refEntityClass = FaqGroup.class, refFieldName = "id")
	private Integer groupId;

	@Column(nullable = false, type = Types.VARCHAR, length = 250, desc = "问题")
	private String question;

	@Column(nullable = false, type = Types.VARCHAR, length = 5000, desc = "答案")
	private String answer;

	@Column(nullable = false, type = Types.INTEGER, desc = "排序号", defaultValue = "0")
	private Integer seqNo;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "添加时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;

	private FaqGroup faqGroup;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public FaqGroup getFaqGroup() {
		return faqGroup;
	}

	public void setFaqGroup(FaqGroup faqGroup) {
		this.faqGroup = faqGroup;
	}

	@Override
	public String toString() {
		return "Faq [id=" + id + ", groupId=" + groupId + ", question=" + question + ", answer=" + answer + ", seqNo=" + seqNo + ", ts=" + ts + ", faqGroup=" + faqGroup + "]";
	}
}
