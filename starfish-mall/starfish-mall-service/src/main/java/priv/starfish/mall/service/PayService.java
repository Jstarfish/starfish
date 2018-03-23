package priv.starfish.mall.service;

import com.abc.trustpay.client.JSON;
import priv.starfish.common.model.Result;
import priv.starfish.mall.settle.dto.WxPayResultDto;
import priv.starfish.mall.settle.entity.PayRefundRec;

public interface PayService extends BaseService {

	/**
	 * 支付宝--支付完成后，一系列业务操作
	 * 
	 * @author "WJJ"
	 * @date 2015年12月16日 上午10:22:59
	 * 
	 * @param no
	 * @param subject
	 * @param body
	 * @param amount
	 * @param tradeNo
	 * @param tradeStatus
	 * @param payTime
	 * @param extraParam
	 */
	void payFinishedOperation(String no, String subject, String body, String amount, String tradeNo, String tradeStatus, String payTime, String extraParam);

	/**
	 * 农行跳银联--支付完成后，一系列业务操作
	 * 
	 * @author "WJJ"
	 * @date 2016年1月4日 下午6:46:38
	 * 
	 * @param no
	 * @param amount
	 * @param tradeNo
	 */
	void payFinishedOperationByAbc(String no, String amount, String tradeNo);

	/**
	 * 为请求农行跨银联支付组装请求数据
	 * 
	 * @author "WJJ"
	 * @date 2016年1月8日 下午3:56:41
	 * 
	 * @param orderNo
	 * @param linkType
	 * @param orderTime
	 * @param orderDate
	 * @param total_fee
	 * @param subject
	 * @return
	 */
	JSON createReqDataForAbcpay(String orderNo, String linkType, String subject, String total_fee, String orderDate, String orderTime);

	/**
	 * 微信支付——组装订单请求信息，获得微信二维码支付链接
	 * 
	 * @author "WJJ"
	 * @date 2016年1月15日 下午5:41:28
	 * 
	 * @param no
	 * @return
	 */
	Result<String> createWechatpayQrcode(String no, Result<String> result);

	/**
	 * 微信——扫码支付，成功之后，一系列业务逻辑
	 * 
	 * @author "WJJ"
	 * @date 2016年1月15日 下午7:01:23
	 * 
	 * @param wpr
	 */
	void payfinishedOperationByWechatpay(WxPayResultDto wpr);

	/**
	 * 根据支付记录表id，查找支付记录
	 * 
	 * @author "WJJ"
	 * @date 2016年1月18日 上午11:44:15
	 * 
	 * @param id
	 * @return
	 */
	PayRefundRec getPayRecById(Long id);
}
