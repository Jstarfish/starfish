
package priv.starfish.mall.settle.dto;

/**
 * description: 微信支付回调
 * 
 * @author
 * @since
 * @see
 */
public class WxPayResultDto {

	private String appid;

	private String bankType;

	private String cashFee;

	private String feeType;

	private String isSubscribe;

	private String mchId;

	private String nonceStr;

	private String openid;

	private String outTradeNo;

	private String resultCode;

	private String returnCode;

	private String sign;

	private String timeEnd;

	private String totalFee;

	private String tradeType;

	private String transactionId;

	private String return_msg;

	private String device_info;

	private String err_code;

	private String err_code_des;

	private String attach;

	private String out_refund_no;

	private String refund_id;

	private String refund_fee;

	private String refund_channel;

	private String cash_refund_fee;

	private String refund_count;

	private String refund_status_$n;

	private String refund_recv_accout_$n;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getCashFee() {
		return cashFee;
	}

	public void setCashFee(String cashFee) {
		this.cashFee = cashFee;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getIsSubscribe() {
		return isSubscribe;
	}

	public void setIsSubscribe(String isSubscribe) {
		this.isSubscribe = isSubscribe;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public String getErr_code_des() {
		return err_code_des;
	}

	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOut_refund_no() {
		return out_refund_no;
	}

	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}

	public String getRefund_id() {
		return refund_id;
	}

	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}

	public String getRefund_fee() {
		return refund_fee;
	}

	public void setRefund_fee(String refund_fee) {
		this.refund_fee = refund_fee;
	}

	public String getRefund_channel() {
		return refund_channel;
	}

	public void setRefund_channel(String refund_channel) {
		this.refund_channel = refund_channel;
	}

	public String getCash_refund_fee() {
		return cash_refund_fee;
	}

	public void setCash_refund_fee(String cash_refund_fee) {
		this.cash_refund_fee = cash_refund_fee;
	}

	public String getRefund_count() {
		return refund_count;
	}

	public void setRefund_count(String refund_count) {
		this.refund_count = refund_count;
	}

	public String getRefund_status_$n() {
		return refund_status_$n;
	}

	public void setRefund_status_$n(String refund_status_$n) {
		this.refund_status_$n = refund_status_$n;
	}

	public String getRefund_recv_accout_$n() {
		return refund_recv_accout_$n;
	}

	public void setRefund_recv_accout_$n(String refund_recv_accout_$n) {
		this.refund_recv_accout_$n = refund_recv_accout_$n;
	}

	@Override
	public String toString() {
		return "WxPayResultDto [appid=" + appid + ", bankType=" + bankType + ", cashFee=" + cashFee + ", feeType=" + feeType + ", isSubscribe=" + isSubscribe + ", mchId=" + mchId + ", nonceStr=" + nonceStr + ", openid=" + openid
				+ ", outTradeNo=" + outTradeNo + ", resultCode=" + resultCode + ", returnCode=" + returnCode + ", sign=" + sign + ", timeEnd=" + timeEnd + ", totalFee=" + totalFee + ", tradeType=" + tradeType + ", transactionId="
				+ transactionId + ", return_msg=" + return_msg + ", device_info=" + device_info + ", err_code=" + err_code + ", err_code_des=" + err_code_des + ", attach=" + attach + ", out_refund_no=" + out_refund_no + ", refund_id="
				+ refund_id + ", refund_fee=" + refund_fee + ", refund_channel=" + refund_channel + ", cash_refund_fee=" + cash_refund_fee + ", refund_count=" + refund_count + ", refund_status_$n=" + refund_status_$n
				+ ", refund_recv_accout_$n=" + refund_recv_accout_$n + "]";
	}

}