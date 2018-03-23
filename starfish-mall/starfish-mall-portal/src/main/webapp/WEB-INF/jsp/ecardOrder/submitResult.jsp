<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/head.jsp"%>
<div class="content">
	<div class="page-width">
			<input type="hidden" name="orderNo" id="orderNo" />
			<ul class="steps">
				<li class="actived"><i>1</i><span>我的购物车</span><em></em></li>
				<li class="actived"><i>2</i><span>填写核对订单</span><em></em></li>
				<li class="active"><i>3</i><span>订单提交成功</span></li>
			</ul>
			<div class="successful">
				<ul class="successflu-ul">
					<li class="item1"><h1>
							<i></i>订单提交成功，请尽快付款！
						</h1></li>
					<li class="item2" id="orderDetail"></li>
					<li class="item3">
						<p class="orange mb30">请您在提交订单后24小时内完成支付，否则订单会自动取消。</p> 
						<input class="btn btn-h40 fr" type="button" value="立即付款" id="orderSubmit" />
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
			id="question" /><input class="btn btn-w94h28" type="button"
			value="支付完成" id="payFinish" />
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
<script type="text/javascript">
	var params = extractUrlParams(location.href);
	var orderId = params.orderId;
	
	var payWay;
	$(function() {
		if (orderId) {
			var ajax = Ajax.post("/eCardOrder/submit/result/get");
			ajax.data({
				orderId : orderId
			});
			ajax.done(function(result, jqXhr) {
				var data = result.data;
				if (data == null) {
					return;
				}
				$id("orderNo").val(data.no);
				payWay = data.payWay;
				// 获取模板内容
				var tplHtml = $id("orderDetailTpl").html();
				// 生成/编译模板
				var htmlTpl = laytpl(tplHtml);
				// 根据模板和数据生成最终内容
				var htmlText = htmlTpl.render(data);
				$id("orderDetail").html(htmlText);
			});
			ajax.go();
		}

		function auditSaleorder(orderNo) {
			if (orderNo) {
				var ajax = Ajax.post("/eCardOrder/status/get");
				ajax.data({
					orderNo : orderNo
				});
				ajax.sync();
				ajax.done(function(result, jqXhr) {
					var data = result.data;
					if (data == null) {
						Layer.warning("订单异常，请重新下单。",function(){
							setPageUrl(getAppUrl("/"));
						});
						return;
					}
					//如果已取消、已完成等，弹出提示框，点击确认后，跳到订单中心
					if (data.cancelled == true){
						Layer.warning("订单已取消，请重新下单。",function(){
							setPageUrl(getAppUrl("/"));
						});
						return;
					}else if (data.paid == true){
						Layer.warning("订单已支付，请查看订单。",function(){
							setPageUrl(getAppUrl("/ucenter/ecardOrder/list/jsp"));
						});
						return;
					} else {
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
					}
					
				});
				ajax.go();
			}
		}
		
		$id("orderSubmit").click(function() {
			// 1
			var orderNo= $id("orderNo").val();
			//判断订单是否正常(未付款、未取消等)
			auditSaleorder(orderNo);
		});

		$id("question").click(function() {
			setPageUrl(getAppUrl("/ucenter/ecardOrder/list/jsp"))
		});

		$id("payFinish").click(function() {
			setPageUrl(getAppUrl("/ucenter/ecardOrder/list/jsp"))
		});

	});
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
 {{# var ecardOrder = d; }}
   <dl class="labels">
       <dt><label>订单编号：</label></dt>
       <dd class="red">{{ecardOrder.no}}</dd>
   </dl>
    <dl class="labels">   
       <dt><label>应付金额：</label></dt>
       <dd class="red">{{ecardOrder.amount}}</dd>
	</dl>
	<dl class="labels">
       <dt><label>支付方式：</label></dt>
	   <dd>{{payWays[ecardOrder.payWay]}}</dd>
   </dl>
</script>
</html>
