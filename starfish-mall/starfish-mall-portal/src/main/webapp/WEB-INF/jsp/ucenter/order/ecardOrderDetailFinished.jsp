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
	var actionDescDict = {
		"submit" : "您提交了订单，请等待店铺系统确认",
		"confirm" : "系统已确认，等待买家付款",
		"finish" : "买家已付款，系统已发放e卡"
	}

	// 获取支付方式
	function getPayName(payWay) {
		var payWayName = null;
		switch (payWay) {
		case "alipay":
			payWayName = "支付宝";
			break;
		case "ecardpay":
			payWayName = "e卡支付";
			break;
		case "weipay":
			payWayName = "微信支付";
			break;
		case "tenpay":
			payWayName = "财付通";
			break;
		case "balanpay":
			payWayName = "余额支付";
			break;
		case "pos":
			payWayName = "pos刷卡";
			break;
		case "unionpay":
			payWayName = "银联支付";
			break;
		case "99bill":
			payWayName = "快钱支付";
			break;
		case "yeepay":
			payWayName = "易宝支付";
			break;
		default:
			payWayName = "支付宝";//测试
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
				<span>订单号：{{ eCardOrder.no }}</span>
				<span>状态：<em>完成</em></span>
			</h1>
		</div>
		<div class="order-process">
			<ul class="steps-state">
				<li class="actived"><i>1</i><span>提交订单</span><span class="time">{{ eCardOrder.createTime }}</span></li>
				<li class="actived"><i>2</i><span>付款</span><span class="time">{{ eCardOrder.payTime }}</span></li>
                <li class="active"><i>3</i><span>完成</span><span class="time">{{ eCardOrder.payTime }}</span></li>
			</ul>
		</div>
		<div class="order-block order-trace">
			<div class="block-tit">
				订单跟踪 <span class="phone-look"><img id="phoneApp" src="{{ getResUrl('/image-app/phone.png') }}"
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
								<td>操作</td>
							</tr>
							<tr>
								<td>
									<dl class="goods-item goods-item1">
										<dt>
											<a href=""><img src="{{ eCardOrder.fileBrowseUrl }}" alt="E卡图片" /></a>
										</dt>
										<dd class="text">
											<a href="">{{ eCardOrder.cardName }}</a>
										</dd>
									</dl>
								</td>
								<td>¥{{ eCardOrder.faceValue }}</td>
								<td>¥{{ eCardOrder.price }}</td>
								<td>{{ eCardOrder.quantity }}</td>
								<td><input class="btn-normal" type="button" value="还要购买" onclick="buyCards('{{ eCardOrder.cardCode }}')"/></td>
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
