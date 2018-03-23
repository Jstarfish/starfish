<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/base.jsf"%><jsp:include
	page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
<div class="content">
	<div class="page-width">
		<div class="crumbs">
			<div class="crumbs">
				<a href="">首页</a>&gt;<a href="">用户中心</a>&gt;<a href="">订单中心</a>&gt;<span>e卡订单</span>
			</div>
		</div>
		<div class="section">
			<jsp:include page="/WEB-INF/jsp/include/ucenterMenu.jsp"></jsp:include>
			<div class="section-right1">
				<div id="eCardOrderDetail" class="order-detail"></div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
<!--转赠-end-->
<script type="text/javascript">
	// 订单处理动作描述信息
	var actionDescDict = {
		"submit" : "您提交了订单，请等待店铺系统确认",
		"confirm" : "系统已确认，等待买家付款",
		"pay" : "买家已付款，系统已发放e卡"
	}

	// 获取支付方式
	function getPayName(payWay) {
		var payWayName = null;
		switch (payWay) {
		case "alipay":
			payWayName = "支付宝";
			break;
		case "ecardpay":
			payWayName = "E卡";
			break;
		case "wechatpay":
			payWayName = "微信";
			break;
		case "pos":
			payWayName = "POS刷卡";
			break;
		case "unionpay":
			payWayName = "银联在线";
			break;
		case "abcAsUnionpay":
			payWayName = "银联在线";
			break;
		case "chinapay":
			payWayName = "银联电子";
			break;
		case "coupon":
			payWayName = "优惠券";
			break;
		}
		return payWayName;
	}

	//渲染页面内容
	function renderHtml(data, fromId, toId) {
		//获取模板内容
		var tplHtml = $id(fromId).html();

		//生成/编译模板
		var theTpl = laytpl(tplHtml);

		//根据模板和数据生成最终内容
		var htmlStr = theTpl.render(data);

		//使用生成的内容
		$id(toId).html(htmlStr);
	}
	
	function cancelOrder(orderId) {
		var ajax = Ajax.post("/eCardOrder/cancel/do");
		ajax.sync();
		ajax.params({
			orderId : orderId
		});
		ajax.done(function(result, jqXhr) {
			if (result.data == false || result.data == null) {
				Layer.msgWarning(result.message);
			} else {
				Layer.msgSuccess("订单取消成功");
				location.href=getAppUrl("/eCardOrder/detail/cancelled/jsp?orderId=" + orderId);
			}
		});
		ajax.go();
	}
	
	function payOrder(orderId) {
		window.open(getAppUrl("/eCardOrder/submit/result/jsp?orderId="+orderId));
	}
	
	function remainTimeForPay(createTime) {
		if (createTime != null || Date.isValidDate(createTime)) {
			var cTime = Date.parseAsDate(createTime);
			var pTime = cTime.addHours(24);
			var now = new Date();
			if (pTime <= now) {
				return "剩余0时0分";
			}
			var remainHour = pTime.diff(now, "hour");
			var remainMinute = pTime.diff(now.addHours(remainHour), "minute");
			return "剩余" + remainHour + "时" + remainMinute + "分";
		}
		return "";
	}

	$(function() {
		var urlParams = extractUrlParams(location.href);
		var orderId = urlParams.orderId;

		var ajax = Ajax.post("/eCardOrder/detail/get");
		ajax.params({
			orderId : parseInt(orderId)
		});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			var data = result.data;
			if (data == null) {
				return;
			}
			// 生成列表
			renderHtml(data, "eCardOrderDetailTpl", "eCardOrderDetail");
		});
		ajax.go();
	})
</script>
</body>
<script type="text/html" id="eCardOrderDetailTpl">
	{{# var eCardOrder = d; }}
		<div class="order-tit">
			<h1>
				<span>订单号：{{ eCardOrder.no }}</span> <span>状态：<em class="warning">等待付款</em></span>
				<span class="remain-time"><i></i><span>{{ remainTimeForPay(eCardOrder.createTime) }}</span></span> <input
					class="btn btnw85h25 btn-warning" type="button" value="付款" onclick="payOrder('{{ eCardOrder.id }}')" />
			</h1>
			<input class="btn btnw85h25 btn-undo" type="button" value="取消订单"  onclick="cancelOrder('{{ eCardOrder.id }}')"/>
		</div>
		<div class="order-process">
			<ul class="steps-state">
				<li class="actived"><i>1</i><span>提交订单</span><span class="time">{{ eCardOrder.createTime }}</span></li>
				<li class="active"><i>2</i><span>等待付款</span><em></em></li>
				<li><i>3</i><span>完成</span></li>
			</ul>
		</div>
		<div class="order-block order-trace">
			<div class="block-tit">
				订单跟踪 <span class="phone-look"><img src="{{ getResUrl('/image-app/phone.png') }}"
					alt="" />手机查看更方便</span>
			</div>
			<div class="block-cont">
				<table class="trance-tb">
					<thead>
						<tr>
							<th width="130">处理时间</th>
							<th>处理信息</th>
							<th width="180">操作人</th>
						</tr>
					</thead>
					<tbody>
						{{# var eCardOrderRecords = eCardOrder.eCardOrderRecords; }}
						{{# for(var i = 0, len = eCardOrderRecords.length; i < len; i++){ }}
							<tr>
								<td>{{ eCardOrderRecords[i].ts }}</td>
								<td>{{ actionDescDict[eCardOrderRecords[i].action] }}</td>
								<td>{{ eCardOrderRecords[i].actorName == null ? "" : eCardOrderRecords[i].actorName }}</td>
							</tr>
						{{# } }}
					</tbody>
				</table>
			</div>
		</div>
		<div class="order-block order-block1">
			<div class="block-tit">订单信息</div>
			<div class="block-cont">
				<div class="block-info">
					<h2>收货人信息</h2>
					<p>
						<span>姓名：</span>{{ eCardOrder.userName == null ? "" : eCardOrder.userName }}
					</p>
					<p>
						<span>手机：</span>{{ eCardOrder.phoneNo == null ? "" : eCardOrder.phoneNo }}
					</p>
				</div>
				<div class="block-info">
					<h2>支付方式及下单时间</h2>
					<p>
						<span>支付方式：</span>{{ getPayName(eCardOrder.payWay) }}
					</p>
					<p>
						<span>下单时间：</span>{{ eCardOrder.createTime }}
					</p>
				</div>
				<div class="block-info noline">
					<h2>商品</h2>
					<!--订单详情-start-->
					<table class="order-td mb10">
						<tbody>
							<tr class="title title1">
								<td width="450">商品信息</td>
								<td>面额</td>
								<td>单价</td>
								<td>数量</td>
							</tr>
							<tr>
								<td>
									<dl class="goods-item goods-item1">
										<dt>
											<a href=""><img src="{{ eCardOrder.fileBrowseUrl }}" alt="E卡图片" /></a>
										</dt>
										<dd class="text">
											<a href="">{{ eCardOrder.cardName == null ? "" : eCardOrder.cardName }}</a>
										</dd>
									</dl>
								</td>
								<td>¥{{ eCardOrder.faceValue }}</td>
								<td>¥{{ eCardOrder.price }}</td>
								<td>{{ eCardOrder.quantity }}</td>
							</tr>
						</tbody>
					</table>
					<!--订单详情-end-->
					<div class="shopping-info">
						<p>
							总商品金额：<span class="red">¥{{ eCardOrder.amount }}</span>
						</p>
						<p>
							<b>应付金额：<span class="red">¥{{ eCardOrder.amount }}</span></b>
						</p>
					</div>
				</div>
			</div>
		</div>
</script>
</html>
