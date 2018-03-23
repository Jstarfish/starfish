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
	
	function buyCards(cardCode) {
		window.open(getAppUrl("/eCardOrder/submit/jsp?code=" + cardCode));
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
				<span>订单号：{{ eCardOrder.no }}</span> <span>状态：<em>已取消</em></span>
			</h1>
		</div>
		<table class="order-td mb10">
			<tbody>
				<tr class="title title1">
					<td width="450">商品信息</td>
					<td>单价</td>
					<td>数量</td>
					<td>操作</td>
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
					<td>¥{{ eCardOrder.price }}</td>
					<td>{{ eCardOrder.quantity }}</td>
					<td><input class="btn-normal" type="button" value="立即购买" onclick="buyCards('{{ eCardOrder.cardCode }}')"/></td>
				</tr>
			</tbody>
		</table>
		<div class="order-block">
			<div class="block-tit">订单信息</div>
			<div class="block-cont">
				<div class="block-info">
					<p>
						<span>支付方式：</span>{{ getPayName(eCardOrder.payWay) }}
					</p>
					<p>
						<span>下单时间：</span>{{ eCardOrder.createTime }}
					</p>
					<p>
						<span>取消时间：</span>{{ eCardOrder.cancelTime }}
					</p>
					<p>
						<span>取消原因：</span>主动取消订单
					</p>
				</div>
			</div>
		</div>
		<div class="order-block">
			<div class="block-tit">收货人信息</div>
			<div class="block-cont">
				<div class="block-info">
					<p>
						<span>姓名：</span>{{ eCardOrder.userName == null ? "" : eCardOrder.userName }}
					</p>
					<p>
						<span>手机：</span>{{ eCardOrder.phoneNo == null ? "" : eCardOrder.phoneNo }}
					</p>
				</div>
			</div>
		</div>
		<div class="order-block">
			<div class="block-tit">结算信息</div>
			<div class="block-cont">
				<div class="block-info">
					<p>
						<span>订单总金额：</span>{{ eCardOrder.amount }}元
					</p>
				</div>
			</div>
		</div>
</script>
</html>
