package priv.starfish.mall.pay.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;


/**
 * @ProjectName:rs-ezmall-bean
 * @ClassName:PayWay
 * @Description:支付方式参数
 * @date: 2015年9月2日
 * @author: WJJ
 * @version: V1.0
 */
@Table(name = "pay_way_param", desc = "支付方式参数", uniqueConstraints = { @UniqueConstraint(fieldNames = { "payWayId", "name" }) })
public class PayWayParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(type = Types.INTEGER)
	private Integer payWayId;
	
	@Column(type = Types.VARCHAR, length = 30, nullable = false)
	private String name;
	
	@Column(type = Types.VARCHAR, length = 1000)
	private String value;
	
	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;
	
	@Column(type = Types.INTEGER, nullable = false, defaultValue = "1")
	private Integer seqNo;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Integer getPayWayId() {
		return payWayId;
	}

	public void setPayWayId(Integer payWayId) {
		this.payWayId = payWayId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
