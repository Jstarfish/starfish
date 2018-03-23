//商品相关设置 code : goods
var goods = {
	stockAlertValve : 1 // 库存预警容限值
};

// 发票设置code : invoice
var invoice = {
	// 普通发票
	plain : {
		provided : true,
		taxRate : 2.0
	},
	// 增值税发票
	added : {
		provided : true,
		taxRate : 1.8
	}
};

// 短信发送设置code : sms
var sms = {
	sendToCustomerOnMerchantDispatching : true, // 商家发货时是否给顾客发短信
	sendToMerchantOnCustomerCreatingOrder : true, // 顾客下订单时是否给商家发短信
	sendToCustomerOnMerchantActivity : true // 商家做店内活动时是否给顾客发短信
};

// 配送方式设置code : delivery.way
var deliveryWay = {
	ids : [ 1, 2 ] //选择的配送方式id列表
};

// 运费设置code : freight.fee
var freightFee = {
	fixed : true, // 是否固定运费
	fixedFee : 15.00, // 每单固定运费
	freeFeeValve : 39.00, // 每单免运费金额
	lessFee : 10.00 // 未达到条件每单运费
};
