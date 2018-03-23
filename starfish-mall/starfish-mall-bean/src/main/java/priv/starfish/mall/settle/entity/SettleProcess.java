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
import priv.starfish.common.json.JsonDateTimeSerializer;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.merchant.entity.Merchant;
import priv.starfish.mall.order.entity.SaleOrder;

/**
 * 结算流程（详细）表
 * 
 * @author "WJJ"
 * @date 2015年11月7日 下午4:19:19
 *
 */
@Table(name = "settle_process")
public class SettleProcess implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT)
	private Long id;

	@Column(nullable = false, type = Types.INTEGER, desc = "商户ID")
	private Integer merchantId;

	@Column(type = Types.VARCHAR, length = 30, desc = "商户名")
	private String merchantName;

	@Column(type = Types.INTEGER, desc = "结算周期")
	private Integer settleX;

	@Column(type = Types.VARCHAR, length = 30, desc = "结算方式")
	private String settleWay;

	@Column(type = Types.VARCHAR, length = 30, desc = "具体结算方式")
	private String settleWayCodeX;

	@Column(type = Types.VARCHAR, length = 30, desc = "真实姓名")
	private String acctName;

	@Column(type = Types.VARCHAR, length = 30, desc = "收款方账号")
	private String acctNo;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "结算金额")
	private String settleAmount;

	@Column(type = Types.VARCHAR, length = 34, desc = "请求流水号")
	private String reqNo;

	/** 0:成功， 1：结算中，2：结算失败，3：待结算 , (4:未确认 , 5:有异议),6: 可提交结算，7： 未到结算日 ，8：无结算账户 **/
	@Column(type = Types.INTEGER, length = 8, desc = "结算标识")
	private Integer settleFlag;

	@Column(type = Types.VARCHAR, length = 250, desc = "成功或失败原因")
	private String reason;

	@Column(type = Types.VARCHAR, length = 34, desc = "转账批次号")
	private String batchNo;

	@Column(type = Types.VARCHAR, length = 34, desc = "响应流水号")
	private String repNo;

	@Column(type = Types.VARCHAR, length = 30, desc = "完成时间，格式为yyyy-MM-dd HH:mm:ss")
	private String doneTime;

	@Column(type = Types.VARCHAR, length = 30, desc = "备注说明、用途")
	private String memo;

	@Column(type = Types.VARCHAR, length = 30, desc = "调账金额")
	private String changeAmount;

	@Column(type = Types.VARCHAR, length = 250, desc = "调账说明")
	private String changeMemo;

	@Column(type = Types.INTEGER, desc = "调账方向标记： 1：退（之前少结算了），-1： 补（之前多结算了）")
	private Integer directFlag;

	@Column(type = Types.BOOLEAN, defaultValue = "FALSE", desc = "是否异常标记")
	private Boolean exceptionFlag;

	@Column(type = Types.DATE, desc = "结算日期")
	private Date settleDay;

	@Column(type = Types.DATE, desc = "创建日期")
	private Date createDay;

	@Column(type = Types.VARCHAR, length = 3, desc = "结算标识 0：系统自动，1：人工手动")
	private String settleType;

	@Column(type = Types.VARCHAR, length = 30, desc = "手动结算时，人工结算单信息")
	private String billExtra;

	@Column(type = Types.TIMESTAMP, desc = "支付宝点击结算时，临时生成的时间")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date tempDay;

	@Column(type = Types.VARCHAR, length = 10, desc = "交易编码(农行)，对私汇兑：CFRT21。对公汇兑：CFRT02。")
	private String transCode;

	@Column(type = Types.VARCHAR, length = 3, desc = "省市代码")
	private String provCode;

	@Column(type = Types.VARCHAR, length = 3, desc = "农行它行标志 0：农行，1：他行")
	private String otherBankFlag;

	@Column(type = Types.VARCHAR, length = 3, desc = "对公对私标识 0: 对私，1：对公")
	private String acctFlag;

	@Column(type = Types.VARCHAR, length = 100, desc = "(收款方)开户行名")
	private String crBankName;

	@Column(type = Types.VARCHAR, length = 30, desc = "(收款方)开户行号")
	private String crBankNo;

	@Column(type = Types.VARCHAR, length = 3, desc = "返回来源")
	private String respSource;

	@Column(type = Types.VARCHAR, length = 10, desc = "返回码")
	private String respCode;

	@Column(type = Types.VARCHAR, length = 256, desc = "返回信息")
	private String respInfo;

	@Column(type = Types.VARCHAR, length = 1, desc = "数据文件标识,0:非文件、1:文件")
	private String fileFlag;

	@Column(type = Types.VARCHAR, length = 4, desc = "记录行数")
	private String recordNum;

	@Column(type = Types.VARCHAR, length = 4, desc = "字段列数")
	private String fieldNum;

	@Column(type = Types.VARCHAR, length = 1024, desc = "应答私有数据区")
	private String respPrvData;

	@Column(type = Types.VARCHAR, length = 20, desc = "批量文件名")
	private String batchFileName;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;

	/** 关联订单 **/
	private List<SaleOrder> saleOrder;

	/** 关联销售结算记录 **/
	private SaleSettleRec saleSettleRec;

	/** 关联结算记录 **/
	private SettleRec settleRec;

	/** 关联商户 **/
	private Merchant merchant;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getAcctName() {
		return acctName;
	}

	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getSettleAmount() {
		return settleAmount;
	}

	public void setSettleAmount(String settleAmount) {
		this.settleAmount = settleAmount;
	}

	public Integer getSettleFlag() {
		return settleFlag;
	}

	public void setSettleFlag(Integer settleFlag) {
		this.settleFlag = settleFlag;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getReqNo() {
		return reqNo;
	}

	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}

	public String getRepNo() {
		return repNo;
	}

	public void setRepNo(String repNo) {
		this.repNo = repNo;
	}

	public String getDoneTime() {
		return doneTime;
	}

	public void setDoneTime(String doneTime) {
		this.doneTime = doneTime;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public List<SaleOrder> getSaleOrder() {
		return saleOrder;
	}

	public void setSaleOrder(List<SaleOrder> saleOrder) {
		this.saleOrder = saleOrder;
	}

	public Date getSettleDay() {
		return settleDay;
	}

	public void setSettleDay(Date settleDay) {
		this.settleDay = settleDay;
	}

	public Date getCreateDay() {
		return createDay;
	}

	public void setCreateDay(Date createDay) {
		this.createDay = createDay;
	}

	public String getSettleWayCodeX() {
		return settleWayCodeX;
	}

	public void setSettleWayCodeX(String settleWayCodeX) {
		this.settleWayCodeX = settleWayCodeX;
	}

	public String getSettleType() {
		return settleType;
	}

	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}

	public String getBillExtra() {
		return billExtra;
	}

	public void setBillExtra(String billExtra) {
		this.billExtra = billExtra;
	}

	public Date getTempDay() {
		return tempDay;
	}

	public void setTempDay(Date tempDay) {
		this.tempDay = tempDay;
	}

	public SaleSettleRec getSaleSettleRec() {
		return saleSettleRec;
	}

	public void setSaleSettleRec(SaleSettleRec saleSettleRec) {
		this.saleSettleRec = saleSettleRec;
	}

	public SettleRec getSettleRec() {
		return settleRec;
	}

	public void setSettleRec(SettleRec settleRec) {
		this.settleRec = settleRec;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Integer getSettleX() {
		return settleX;
	}

	public void setSettleX(Integer settleX) {
		this.settleX = settleX;
	}

	public String getSettleWay() {
		return settleWay;
	}

	public void setSettleWay(String settleWay) {
		this.settleWay = settleWay;
	}

	public String getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(String changeAmount) {
		this.changeAmount = changeAmount;
	}

	public String getChangeMemo() {
		return changeMemo;
	}

	public void setChangeMemo(String changeMemo) {
		this.changeMemo = changeMemo;
	}

	public Integer getDirectFlag() {
		return directFlag;
	}

	public void setDirectFlag(Integer directFlag) {
		this.directFlag = directFlag;
	}

	public Boolean getExceptionFlag() {
		return exceptionFlag;
	}

	public void setExceptionFlag(Boolean exceptionFlag) {
		this.exceptionFlag = exceptionFlag;
	}

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public String getProvCode() {
		return provCode;
	}

	public void setProvCode(String provCode) {
		this.provCode = provCode;
	}

	public String getOtherBankFlag() {
		return otherBankFlag;
	}

	public void setOtherBankFlag(String otherBankFlag) {
		this.otherBankFlag = otherBankFlag;
	}

	public String getAcctFlag() {
		return acctFlag;
	}

	public void setAcctFlag(String acctFlag) {
		this.acctFlag = acctFlag;
	}

	public String getCrBankName() {
		return crBankName;
	}

	public void setCrBankName(String crBankName) {
		this.crBankName = crBankName;
	}

	public String getCrBankNo() {
		return crBankNo;
	}

	public void setCrBankNo(String crBankNo) {
		this.crBankNo = crBankNo;
	}

	public String getRespSource() {
		return respSource;
	}

	public void setRespSource(String respSource) {
		this.respSource = respSource;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespInfo() {
		return respInfo;
	}

	public void setRespInfo(String respInfo) {
		this.respInfo = respInfo;
	}

	public String getFileFlag() {
		return fileFlag;
	}

	public void setFileFlag(String fileFlag) {
		this.fileFlag = fileFlag;
	}

	public String getRecordNum() {
		return recordNum;
	}

	public void setRecordNum(String recordNum) {
		this.recordNum = recordNum;
	}

	public String getFieldNum() {
		return fieldNum;
	}

	public void setFieldNum(String fieldNum) {
		this.fieldNum = fieldNum;
	}

	public String getRespPrvData() {
		return respPrvData;
	}

	public void setRespPrvData(String respPrvData) {
		this.respPrvData = respPrvData;
	}

	public String getBatchFileName() {
		return batchFileName;
	}

	public void setBatchFileName(String batchFileName) {
		this.batchFileName = batchFileName;
	}

	@Override
	public String toString() {
		return "SettleProcess [id=" + id + ", merchantId=" + merchantId + ", merchantName=" + merchantName + ", settleX=" + settleX + ", settleWay=" + settleWay + ", settleWayCodeX=" + settleWayCodeX + ", acctName=" + acctName + ", acctNo="
				+ acctNo + ", settleAmount=" + settleAmount + ", reqNo=" + reqNo + ", settleFlag=" + settleFlag + ", reason=" + reason + ", batchNo=" + batchNo + ", repNo=" + repNo + ", doneTime=" + doneTime + ", memo=" + memo
				+ ", changeAmount=" + changeAmount + ", changeMemo=" + changeMemo + ", directFlag=" + directFlag + ", exceptionFlag=" + exceptionFlag + ", settleDay=" + settleDay + ", createDay=" + createDay + ", settleType=" + settleType
				+ ", billExtra=" + billExtra + ", tempDay=" + tempDay + ", transCode=" + transCode + ", provCode=" + provCode + ", otherBankFlag=" + otherBankFlag + ", acctFlag=" + acctFlag + ", crBankName=" + crBankName + ", crBankNo="
				+ crBankNo + ", respSource=" + respSource + ", respCode=" + respCode + ", respInfo=" + respInfo + ", fileFlag=" + fileFlag + ", recordNum=" + recordNum + ", fieldNum=" + fieldNum + ", respPrvData=" + respPrvData
				+ ", batchFileName=" + batchFileName + ", ts=" + ts + ", saleOrder=" + saleOrder + ", saleSettleRec=" + saleSettleRec + ", settleRec=" + settleRec + ", merchant=" + merchant + "]";
	}

}
