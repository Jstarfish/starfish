package priv.starfish.mall.shop.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import priv.starfish.common.util.JsonUtil;
import priv.starfish.mall.shop.entity.ShopParam;
import priv.starfish.mall.shop.entity.ShopParam.MetaInfo;

public class ShopParamDto {
	public Integer id;

	public Integer shopId;

	// 商品相关设置 code : goods -------------------------------------
	// 库存预警容限值
	public Integer goodsStockAlertValve;

	// 店铺发票设置code : invoice
	// 普通发票
	public Boolean invoicePlainProvided;
	public Double invoicePlainTaxRate;
	// 增值税发票
	public Boolean invoiceAddedProvided;
	public Double invoiceAddedTaxRate;

	// 短信发送设置code : sms ----------------------------------------
	// 商家发货时是否给顾客发短信
	public Boolean smsSendToCustomerOnMerchantDispatching;
	// 顾客下订单时是否给商家发短信
	public Boolean smsSendToMerchantOnCustomerCreatingOrder;
	// 商家做店内活动时是否给顾客发短信
	public Boolean smsSendToCustomerOnMerchantActivity;

	// 配送方式设置code : delivery.way -------------------------------
	// 选择的配送方式id列表
	public List<Integer> deliveryWayIds;

	// 运费设置code : freight.fee ------------------------------------
	// 是否固定运费
	public Boolean freightFeeFixed;
	// 每单固定运费
	public Double freightFeeFixedFee;
	// 每单免运费金额
	public Double freightFeeFreeFeeValve;
	// 未达到条件每单运费
	public Double freightFeeLessFee;

	public ShopParam toShopParam(String code) {
		MetaInfo metaInfo = null;
		Map<String, Object> valueMap = new LinkedHashMap<String, Object>();
		if (ShopParam.goods.code.equalsIgnoreCase(code)) {
			metaInfo = ShopParam.goods;
			//
			valueMap.put("stockAlertValve", this.goodsStockAlertValve);
		} else if (ShopParam.invoice.code.equalsIgnoreCase(code)) {
			metaInfo = ShopParam.invoice;
			//
			Map<String, Object> tmpMap = new LinkedHashMap<String, Object>();
			valueMap.put("plain", tmpMap);
			tmpMap.put("provided", this.invoicePlainProvided);
			tmpMap.put("taxRate", this.invoicePlainTaxRate);
			tmpMap = new LinkedHashMap<String, Object>();
			valueMap.put("added", tmpMap);
			tmpMap.put("provided", this.invoiceAddedProvided);
			tmpMap.put("taxRate", this.invoiceAddedTaxRate);
		} else if (ShopParam.sms.code.equalsIgnoreCase(code)) {
			metaInfo = ShopParam.sms;
			//
			valueMap.put("sendToCustomerOnMerchantDispatching", this.smsSendToCustomerOnMerchantDispatching);
			valueMap.put("sendToMerchantOnCustomerCreatingOrder", this.smsSendToMerchantOnCustomerCreatingOrder);
			valueMap.put("sendToCustomerOnMerchantActivity", this.smsSendToCustomerOnMerchantActivity);
		} else if (ShopParam.deliveryWay.code.equalsIgnoreCase(code)) {
			metaInfo = ShopParam.deliveryWay;
			//
			valueMap.put("ids", this.deliveryWayIds);
		} else if (ShopParam.freightFee.code.equalsIgnoreCase(code)) {
			metaInfo = ShopParam.freightFee;
			//
			valueMap.put("fixed", this.freightFeeFixed);
			valueMap.put("fixedFee", this.freightFeeFixedFee);
			valueMap.put("freeFeeValve", this.freightFeeFreeFeeValve);
			valueMap.put("lessFee", this.freightFeeLessFee);
		}
		//
		if (metaInfo == null) {
			return null;
		}
		ShopParam shopParam = new ShopParam();
		shopParam.setId(this.id);
		shopParam.setShopId(this.shopId);
		shopParam.setCode(metaInfo.code);
		shopParam.setName(metaInfo.name);
		shopParam.setValue(JsonUtil.toJson(valueMap));
		//
		return shopParam;
	}
}
