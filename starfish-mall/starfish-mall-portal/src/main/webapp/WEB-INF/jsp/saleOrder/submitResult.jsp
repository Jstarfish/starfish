<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/head.jsp"%>
<div class="content">
	<div class="page-width">
			<input type="hidden" name="saleOrderNo" id="saleOrderNo" />
			<div class="successful" style="display: none;">
				<ul class="successful-ul">
					<li class="item1"><h1>
							<i></i>订单提交成功，请尽快付款！
						</h1></li>
					<li class="item2" id="orderDetail"></li>
					<li class="item3">
						<p class="orange mb30">请您在提交订单后24小时内完成支付，否则订单会自动取消。</p> <input
						class="btn btn-h40 fr" type="button" value="立即付款" id="orderSubmit" />
					</li>
				</ul>
			</div>
			<div class="unsuccessful" style="display: none;">
				<ul class="successful-ul">
					<li class="item1 item4">
						<h1>
							<i></i><span id="stateTitle"></span>
						</h1>
						<div class="successful-text">
							<span>订单号：<span data-id="orderId"></span></span>
						</div>
						<div class="successful-btn">
							<input class="btn" type="button" onClick="goSaleOrderJsp()"
								value="返回我的订单" />
						</div>
					</li>
				</ul>
			</div>
	</div>
</div>
<div id="pay-success" class="pay-success">
	<div class="pay-success-cont">
		<p>支付完成前，请不要关闭此验证窗口。</p>
		<p>支付完成后，请根据支付的情况点击下面按扭。</p>
	</div>
	<div class="pay-success-btn">
		<input class="btn-normal mr20" type="button" value="支付遇到问题"
			id="question" /><input id="payFinish" class="btn btn-w94h28"
			type="button" value="支付完成" />
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
<script type="text/javascript">
	
	
//<!--
var params=extractUrlParams(location.href);
var _orderNo=params.orderNo;
var _orderId=params.orderId;

var payWay;

$(function(){
	if(!isUndef(_orderNo) && _orderNo!=null){
		initSaleOrder(_orderNo,null);
	}else if(!isUndef(_orderId) && _orderId!=null){
		initSaleOrder(null,_orderId);
	}
	$id("question").click(function() {
		setPageUrl(getAppUrl("/ucenter/saleOrder/list/jsp"))
	});
	
	$id("payFinish").click(function() {
		setPageUrl(getAppUrl("/ucenter/saleOrder/list/jsp"))
	});

});
function getSaleOrder(orderNo,orderId,orderCallback){
	var ajax = Ajax.post("/saleOrder/detail/get");
	ajax.sync();
	if(orderNo!=null){
		ajax.data({orderNo:orderNo});
	}else if(orderId!=null){
		ajax.data({orderId:orderId});
	}
	ajax.done(function(result, jqXhr) {
		if (result != ""&&result.type=="info") {
			var data = result.data;
			payWay = data.payWay;
			if (typeof orderCallback == "function") {
				orderCallback(data);
			}
		}else{
			if (typeof orderCallback == "function") {
				orderCallback(null);
			}
		}
	});
	ajax.fail(function(result, jqXhr) {
		if (typeof orderCallback == "function") {
			orderCallback(null);
		}
	});
	ajax.go();
}
function initSaleOrder(orderNo,orderId) {
	getSaleOrder(orderNo,orderId,function(data){
		if(data == null){
			return ;
		}else{
			var orderNo = data.no;
			if (data.cancelled == true){
				$id("stateTitle").html("订单已取消，请重新下单。");
				$("[data-id='orderId']").html(orderNo);
				//
				$(".unsuccessful").css("display","block");
			} else if (data.payState == "unpaid"){
				$(".successful").css("display","block");
				$id("saleOrderNo").val(orderNo);
				//
				renderSaleOrderHtml(data,"orderDetailTpl","orderDetail");
			} else if (data.payState == "paid"){
				$id("stateTitle").html("订单已支付，请查看订单。");
				$("[data-id='orderId']").html(orderNo);
				//
				$(".unsuccessful").css("display","block");
			} else {
				$id("stateTitle").html("订单异常，请查看订单。");
				$("[data-id='orderId']").html(orderNo);
				//
				$(".unsuccessful").css("display","block");
			}
		}
	});
}
//
function goSaleOrderJsp(){
	setPageUrl(getAppUrl("/ucenter/saleOrder/list/jsp"));
}
//渲染页面内容
function renderSaleOrderHtml(data,fromId,toId) {
	//获取模板内容
	var tplHtml = $id(fromId).html();
	
	//生成/编译模板
	var theTpl = laytpl(tplHtml);
	
	//根据模板和数据生成最终内容
	var htmlStr = theTpl.render(data);
	
	//使用生成的内容
	$id(toId).html(htmlStr);
}

$id("orderSubmit").click(function(){
	var orderNo= $id("saleOrderNo").val();
	if(orderNo != null){
		getSaleOrder(orderNo, null, function(data){
			if(data != null){
				if (data.cancelled == true){
					Layer.warning("订单已取消，请重新下单。",function(){
						setPageUrl(getAppUrl("/"));
					});
				} else if (data.payState == "unpaid"){
					//form提交
					var url = payWayUrl[payWay];
					url = getAppUrl(makeUrl(url, {orderNo : orderNo}));
					var theForm = HiddenForm.newOne();
					theForm.id("orderForm");
					theForm.target("_blank");
					theForm.action(url);
					theForm.field("orderNo", orderNo);
					theForm.submit();
					// 2
					paySuccess();
					
				} else if (data.payState == "paid"){
					Layer.warning("订单已支付，请查看订单。",function(){
						setPageUrl(getAppUrl("/saleOrder/detail/jsp"));
					});
				} else {
					Layer.warning("订单异常，请查看订单。",function(){
						setPageUrl(getAppUrl("/ucenter/saleOrder/list/jsp"));
					});
				}
			}else{
				Layer.warning("未知错误");
			}
		});
	}
});

//-->
</script>

<script type="text/javascript">

	//支付方式字典
	var payWays = {
		"alipay" : "支付宝",
		"wechatpay" : "微信",
		"pos" : "POS刷卡",
		"unionpay" : "银联在线",
		"abcAsUnionpay" : "银联在线",
		"chinapay" : "银联电子",
		"ecardpay" : "E卡",
		"coupon" : "优惠券"
	};
	//支付方式链接
	var payWayUrl = {
		"alipay" : "/pay/alipay/pay/by/ali",
		"wechatpay" : "/pay/wechatpay/pay",
		"abcAsUnionpay" : "/pay/abc/pay",
		"chinapay" : "银联电子"
	};

	function paySuccess() {
		var dom = "#pay-success";
		Layer.dialog({
			dom : dom, //或者 html string
			area : [ '400px', '220px' ],
			title : "支付成功提示",
			closeBtn : false,
			btn : false
		//默认为 btn : ["确定", "取消"]
		});
	}
</script>

</body>
<script type="text/html" id="orderDetailTpl">
 {{# var saleOrder = d; }}
   <dl class="labels">
       <dt><label>订单编号：</label></dt>
       <dd>{{saleOrder.no}}</dd>
   </dl>
   <dl class="labels">
       <dt><label>服务门店：</label></dt>
       <dd>{{saleOrder.shopName}}</dd>
   </dl>
   <dl class="labels">
       <dt><label>预约时间：</label></dt>
       <dd>{{saleOrder.planTime}}</dd>
   </dl>
   <dl class="labels">
       <dt><label>支付方式：</label></dt>
	   <dd>{{payWays[saleOrder.payWay]}}</dd>
   </dl>
   <dl class="labels">
       <dt><label>应付金额：</label></dt>
       <dd class="red">￥{{saleOrder.amountOuter}}</dd>
   </dl>
</script>
</html>