package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

/**
 * 常见问题分组
 * 
 * @author 邓华锋
 * @date 2015年9月19日 下午3:08:47
 *
 */
@Table(name = "faq_group")
public class FaqGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "所属分类主键")
	@ForeignKey(refEntityClass = FaqCat.class, refFieldName = "id")
	private Integer catId;

	@Column(nullable = false, type = Types.VARCHAR, length = 250, desc = "分组名称")
	private String name;

	@Column(nullable = false, type = Types.INTEGER, desc = "排序号", defaultValue = "0")
	private Integer seqNo;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "添加时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;

	private FaqCat faqCat;

	/**
	 * 常见问题列表
	 */
	private List<Faq> faqs;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
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

	public List<Faq> getFaqs() {
		return faqs;
	}

	public void setFaqs(List<Faq> faqs) {
		this.faqs = faqs;
	}

	public FaqCat getFaqCat() {
		return faqCat;
	}

	public void setFaqCat(FaqCat faqCat) {
		this.faqCat = faqCat;
	}

	@Override
	public String toString() {
		return "FaqGroup [id=" + id + ", catId=" + catId + ", name=" + name + ", seqNo=" + seqNo + ", ts=" + ts + ", faqCat=" + faqCat + ", faqs=" + faqs + "]";
	}

}