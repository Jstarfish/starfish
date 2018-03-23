package priv.starfish.mall.settle.entity;

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
import priv.starfish.mall.order.entity.SaleOrder;

/**
 * 结算账期与订单、商户的关联表
 * 
 * @author "WJJ"
 * @date 2015年11月14日 下午5:04:15
 *
 */
@Table(name = "settle_order")
public class SettleOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT)
	private Long id;

	@Column(type = Types.BIGINT, desc = "SettleProcess, id")
	private Long processId;

	@Column(type = Types.VARCHAR, length = 60, desc = "订单编号")
	private String no;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Override
	public String toString() {
		return "SettleOrder [id=" + id + ", processId=" + processId + ", no=" + no + "]";
	}

}
