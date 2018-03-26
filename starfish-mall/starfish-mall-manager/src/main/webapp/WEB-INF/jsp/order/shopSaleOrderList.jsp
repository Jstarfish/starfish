<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>销售订单列表</title>
<style>
.normal-table {
	margin-left: 70px;
	height: 100px;
	width: 550px;
	overflow: auto;
}
.star-flag {
	color:red;
	font-weight:bold;
	font-size:18px;
	font-family: Times, TimesNR, 'New Century Schoolbook',
     Georgia, 'New York', serif;
    text-decoration: none;
}
.workers-list {
	margin-left: 60px;
	margin-right: 60px;
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 10px;
	padding-bottom:10px;
	border-style: solid;
	border-width: 1px;
	border-color: gray;
	height: 100px;
	overflow: auto;
}
.worker {
	display:inline-block; 
	width:100px;
}
.text-blue {
	color: blue;
}
.input-car {
	width: 360px !important;
}
.order-list {
	margin-left: 60px;
	margin-right: 60px;
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 10px;
	padding-bottom:10px;
	border-style: solid;
	border-width: 1px;
	border-color: gray;
	height: 300px;
	overflow: auto;
}
.order-info {
	margin-left: 10px;
	margin-right: 10px;
}
</style>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnSubmit" class="button"
						onclick="location.href=getAppUrl('/saleOrder/create/jsp/-shop')">代理下单</button>
				</div>
				<div class="group right aligned">
					<label class="label">用户名</label> <input id="queryCustomerName"
						class="input" /> <span class="spacer"></span> <label
						class="label">订单状态</label> <select id="queryOrderState"
						class="input">
						<option>全部</option>
						<option value="unhandled">未付款</option>
						<option value="processing">未服务</option>
						<option value="cancelled">已取消</option>
						<option value="finished">已完成</option>
					</select> <span class="spacer"></span> <label class="label">下单日期</label> <input
						class="input" id="fromDate" /> 至 <input class="input" id="toDate" />
					<span class="spacer two wide"></span>
					<button id="btnQuery" class="button">查询</button>
				</div>
			</div>
		</div>
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	<div id="showDialog" style="display: none;">
		<div id="viewForm" class="form">
			<div class="field row">
				<label class="field label">订单编号</label> <input type="text"
					id="orderNo" class="field value one half wide" /> <label
					class="field inline label one half wide">商家</label> <input
					type="text" id="merchantName" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">店铺</label> <input type="text"
					id="shopName" class="field value one half wide" /> <label
					class="field inline label one half wide">店铺电话</label> <input
					type="text" id="shopTel" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">下单时间</label> <input type="text"
					id="submitTime" class="field value one half wide" /> <label
					class="field inline label one half wide">预约时间</label> <input
					type="text" id="planTime" class="field value one half wide" />
			</div>
			<br />
			<span id="divider" class="normal hr divider"></span>
			<div class="field row">
				<label class="field label">用户</label> <input type="text"
					id="customerName" class="field value one half wide" /> <label
					class="field inline label one half wide">联系人</label> <input
					type="text" id="linkMan" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">电话</label> <input type="text"
					id="phoneNo" class="field value one half wide" /> <label
					class="field inline label one half wide">备用电话</label> <input
					type="text" id="telNo" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">电子邮箱</label> <input type="text"
					id="email" class="field value one half wide" /> <label
					class="field inline label one half wide">车辆信息</label> <input
					type="text" id="carName" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">所在地</label> <input type="text"
					id="regionName" class="field value one half wide" /> <label
					class="field inline label one half wide">地址</label> <input
					type="text" id="street" class="field value one half wide" />
			</div>
			<div class="field row" style="height: 60px;">
				<label class="field label" style="vertical-align: middle;">客户留言</label>
				<textarea id="leaveMsg" class="field value three wide"
					style="height: 50px;"></textarea>
			</div>
			<br />
			<span id="divider" class="normal hr divider"></span>
			<div class="field row">
				<label class="field label">订单总额</label> <input type="text"
					id="saleAmount" class="field value one half wide" /> <label
					class="field inline label one half wide">优惠券</label> <input
					type="text" id="discAmountSub" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">E卡</label> <input type="text"
					id="amountInner" class="field value one half wide" /> <label
					class="field inline label one half wide">应付金额</label> <input
					type="text" id="amountOuter" class="field value one half wide" />
			</div>
			<br />
			<span id="divider" class="normal hr divider"></span>
			<div class="field row">
				<label class="field label">订单状态</label> <input type="text"
					id="orderState" class="field value one half wide" /> <label
					class="field inline label one half wide">货运状态</label> <input
					type="text" id="distState" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">支付状态</label> <input type="text"
					id="payState" class="field value one half wide" /> <label
					class="field inline label one half wide">支付方式</label> <input
					type="text" id="payWay" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">是否已取消</label> <input type="text"
					id="cancelled" class="field value one half wide" /> <label
					class="field inline label one half wide">是否已关闭</label> <input
					type="text" id="closed" class="field value one half wide" />
			</div>
			<br />
			<span id="divider" class="normal hr divider"></span>
			<div class="field row">
				<label class="field label">服务&商品</label>
			</div>
			<div>
				<table id="svcList" width="100%" class="cleanTbl">
					<thead class="head">
						<tr class="row">
							<th>服务 / 商品名称</th>
							<th>单价</th>
							<th>数量</th>
						</tr>
					</thead>
					<tbody class="body">
					</tbody>
				</table>
			</div>
			<br />
			<span id="divider" class="normal hr divider"></span>
			<div class="field row">
				<label class="field label">处理记录</label>
			</div>
			<div>
				<table id="recordList" width="100%" class="cleanTbl">
					<thead class="head">
						<tr class="row">
							<th>操作人</th>
							<th>角色</th>
							<th>操作</th>
							<th>操作时间</th>
						</tr>
					</thead>
					<tbody class="body">
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<div id="payDialog" style="display: none;">
		<div id="payForm" class="form">
			<div class="field row">
				<label class="field label">订单编号</label> <input type="text"
					id="payOrderNo" class="field value one half wide" /> <label
					class="field inline label one half wide">店铺</label> <input
					type="text" id="payShopName" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">支付金额</label> <input type="text"
					id="payAmount" class="field value one half wide" /> <label
					class="field inline label one half wide">支付方式</label> <input
					type="text" id="payPayWay" class="field value one half wide" />
			</div>
		</div>
	</div>

	<div id="posDialog" style="display: none;">
		<div id="posForm" class="form">
			<div class="field row">
				<label class="field label one half wide">订单编号</label> <input
					type="text" id="posOrderNo" class="field value one half wide" /> <label
					class="field inline label one half wide">店铺</label> <input
					type="text" id="posShopName" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label one half wide">支付金额</label> <input
					type="text" id="posAmount" class="field value one half wide" /> <label
					class="field inline label one half wide">支付方式</label> <input
					type="text" id="posPayWay" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label required one half wide">支付凭据编号</label> <input
					type="text" id="posNo" class="field value one half wide" />
			</div>
		</div>
	</div>

	<div id="completeDialog" style="display: none;">
		<div id="completeForm" class="form">
			<div class="field row">
				<label class="field label one half wide">订单编号</label> <input
					type="text" id="completeOrderNo" class="field value one half wide" />
				<label class="field inline label one half wide">用户手机号</label> <input
					type="text" id="completeUserMobile"
					class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label required one half wide">确认码</label> <input
					type="text" id="doneCode" class="field value one half wide" maxlength="6" />
				<label
					class="field inline label one half wide">用户车辆</label> <input
					type="text" id="carInfo" maxlength="30" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label one half wide">服务清单</label>
			</div>
			<div class="normal-table">
				<table id="completeSvcList" width="100%" class="cleanTbl">
					<thead class="head">
						<tr class="row">
							<th>服务 / 商品名称</th>
							<th>单价</th>
							<th>数量</th>
						</tr>
					</thead>
					<tbody class="body">
					</tbody>
				</table>
			</div>
			<div class="field row">
				<label class="field label one half wide">服务技师</label>
			</div>
			<div id="workerList" class="workers-list">
				<ul>
				</ul>
			</div>
		</div>
	</div>
	
	<div id="addInfoDialog" style="display: none;">
		<div id="addInfoForm" class="form">
			<div class="field row">
				<label class="field label one half wide">用户车辆</label> <input
					type="text" id="userCar" class="field value one half wide input-car" maxlength="30" />
			</div>
			<div class="field row" style="height: 60px;">
				<label class="field label one half wide"
					style="vertical-align: middle;">备注信息</label>
				<textarea id="memo" class="field value three wide"
					maxlength="90" style="height: 50px;"></textarea>
			</div>
			<div class="field row">
				<label class="field label one half wide">服务技师</label>
			</div>
			<div id="addInfoDlgWorkerList" class="workers-list">
				<ul>
				</ul>
			</div>
		</div>
	</div>
	
	<div id="userInfoDialog" style="display: none;">
		<div id="userInfoForm" class="form">
			<div class="field row">
				<label class="field label one half wide">消费记录</label>
			</div>
			<div id="svcOrders" class="order-list">
				<ul>
				</ul>
			</div>
		</div>
	</div>
	
	<div id="refundConfirmText" style="display: none;">
			<div style="margin: 30px 50px 0; text-align: center; vertical-align: middle;">
				<span >是否执行了付款操作，并收到已支付成功的反馈？</span>
			</div>
	</div>

	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		// 订单信息查看对话框
		var showDialog;

		// 支付确认对话框
		var payDialog;

		// POS刷卡支付确认对话框
		var posDialog;

		// 服务完成确认对话框
		var completeDialog;
		
		// 补充信息对话框
		var addInfoDialog;
		
		// 用户信息对话框
		var userInfoDialog;

		// 缓存当前jqGrid数据行数组
		var shopGridHelper = JqGridHelper.newOne("");
		
		// 完成订单的技师的ID集合
		var workerIds = [];
		
		// 支付状态字典
		var payStates = {
			"unpaid" : "未支付",
			"paid" : "已支付",
			"refundApplied" : "已申请退款",
			"refunded" : "已退款"
		};

		// 货运状态字典
		var distStates = {
			"unfilled" : "未发货",
			"delivered" : "已发货",
			"goodsReceived" : "（发货）已收货",
			"returnsApplied" : "已申请退款",
			"returnsAgreed" : "同意退货",
			"returnsReceived" : "（退货）已收货"
		};

		// 是非状态字典
		var judgeStates = {
			"true" : "是",
			"false" : "否"
		};

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
			"alipay" : "/pay/alipay/proxy/pay",
			"wechatpay" : "/pay/wechatpay/pay",
			"abcAsUnionpay" : "/pay/abc/pay",
			"chinapay" : "银联电子"
		};

		// 操作字典
		var Actions = {
			"submit" : "创建订单",
			"pay" : "支付",
			"finish" : "服务完成",
			"cancel" : "取消订单",
			"close" : "关闭",
			"applyRefund" : "申请退款",
			"agreeRefund" : "同意退款",
			"refuseRefund" : "拒绝退款",
			"refund" : "退款"
		};

		// 渲染
		function renderDetail(data, fromId, toId) {
			var tplHtml = $id(fromId).html();
			var theTpl = laytpl(tplHtml);
			var htmlStr = theTpl.render(data);
			$id(toId).find("tbody").html(htmlStr);
		}
		
		// 渲染（通用）
		function renderHtml(data, fromId, toId) {
			var tplHtml = $id(fromId).html();
			var theTpl = laytpl(tplHtml);
			var htmlStr = theTpl.render(data);
			$id(toId).find("ul").html(htmlStr);
		}

		// 调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			console.log("mainWidth:" + mainWidth + ", " + "mainHeight:" + mainHeight);
			//
			var gridCtrlId = "theGridCtrl";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("theGridPager").height();
			jqGridCtrl.setGridWidth(mainWidth - 1);
			jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 90);
		}

		// jqGrid缓存变量
		var jqGridCtrl = null;

		// 实例化表单代理
		var formProxy = FormProxy.newOne();

		// 注册表单控件
		formProxy.addField({
			id : "orderNo",
			key : "no",
		});
		formProxy.addField({
			id : "merchantName",
			key : "merchantName",
		});
		formProxy.addField({
			id : "shopName",
			key : "shop.name",
		});
		formProxy.addField({
			id : "shopTel",
			key : "shop.telNo",
		});
		formProxy.addField({
			id : "submitTime",
			key : "ts",
		});
		formProxy.addField({
			id : "planTime",
			key : "planTime",
		});
		formProxy.addField({
			id : "planModTimes",
			key : "planModTimes",
		});
		formProxy.addField({
			id : "customerName",
			key : "customer.nickName",
		});
		formProxy.addField({
			id : "linkMan",
			key : "linkMan",
		});
		formProxy.addField({
			id : "phoneNo",
			key : "phoneNo",
		});
		formProxy.addField({
			id : "telNo",
			key : "telNo",
		});
		formProxy.addField({
			id : "email",
			key : "email",
		});
		formProxy.addField({
			id : "carName",
			key : "carName",
		});
		formProxy.addField({
			id : "regionName",
			key : "regionName",
		});
		formProxy.addField({
			id : "street",
			key : "street",
		});
		formProxy.addField({
			id : "leaveMsg",
			key : "leaveMsg",
		});
		formProxy.addField({
			id : "saleAmount",
			key : "saleAmount",
		});
		formProxy.addField({
			id : "discAmountSub",
			key : "discAmountSub",
		});
		formProxy.addField({
			id : "amountInner",
			key : "amountInner",
		});
		formProxy.addField({
			id : "amountOuter",
			key : "amountOuter",
		});
		formProxy.addField({
			id : "orderState",
			key : "orderState",
		});
		formProxy.addField({
			id : "payState",
			key : "payState",
		});
		formProxy.addField({
			id : "payWay",
			key : "payWay",
		});
		formProxy.addField({
			id : "distState",
			key : "distState",
		});
		formProxy.addField({
			id : "cancelled",
			key : "cancelled",
		});
		formProxy.addField({
			id : "closed",
			key : "closed",
		});

		// 实例化查询表单代理
		var formProxyQuery = FormProxy.newOne();

		// 注册过滤条件表单控件
		formProxyQuery.addField({
			id : "queryCustomerName",
			rules : [ "maxLength[30]" ]
		});
		formProxyQuery.addField({
			id : "queryOrderState",
			type : "text"
		});
		formProxyQuery.addField({
			id : "fromDate",
			type : "date",
			rules : [ "isDate", "rangeDate[2015-01-01,2115-01-01]" ]
		});
		formProxyQuery.addField({
			id : "toDate",
			type : "date",
			rules : [ "isDate", "rangeDate[2015-01-01,2115-01-01]" ]
		});

		// 支付表单代理对象
		var formProxyPos = FormProxy.newOne();

		// 注册POS支付表单控件
		formProxyPos.addField({
			id : "posNo",
			key : "posNo",
			required : true,
			rules : [ "maxLength[30]" ]
		});

		// 服务完成确认表单代理对象
		var formProxyComplete = FormProxy.newOne();

		// 注册服务完成确认表单控件
		formProxyComplete.addField({
			id : "doneCode",
			key : "doneCode",
			required : true,
			rules : [ "maxLength[6]" ]
		});
		
		// 服务完成确认表单代理对象
		var formProxyAddInfo = FormProxy.newOne();

		// 注册补充信息表单控件
		formProxyAddInfo.addField({
			id : "userCar",
			key : "userCar",
			rules : [ "maxLength[30]" ]
		});
		
		formProxyAddInfo.addField({
			id : "memo",
			key : "memo",
			rules : [ "maxLength[90]" ]
		});

		// 初始化查看页面
		function initShowDialog(obj) {
			renderSaleOrderData(obj);
			
			$("#showDialog  input").attr("disabled", true);
			$("#showDialog textarea").attr("disabled", true);

			showDialog = $("#showDialog").dialog({
				autoOpen : false,
				height : Math.min(650, $(window).height()),
				width : Math.min(750, $(window).width()),
				modal : true,
				title : '查看销售订单详情',
				buttons : {
					"关闭" : function() {
						showDialog.dialog("close");
					}
				},
				close : function() {
					formProxy.hideMessages();
				}
			});
		}
		
		function renderSaleOrderData(obj){
			$("#showDialog input[type=text]").val("");
			$("#showDialog textarea").val("");
			formProxy.setValue2("no", obj.no);
			formProxy.setValue2("merchantName", obj.merchantName);
			formProxy.setValue2("shop.name", obj.shop.name);
			formProxy.setValue2("shop.telNo", obj.shop.phoneNo);
			formProxy.setValue2("ts", obj.createTime);
			formProxy.setValue2("planTime", obj.planTime);
			formProxy.setValue2("planModTimes", obj.planModTimes);
			formProxy.setValue2("customer.nickName", obj.userName);
			formProxy.setValue2("linkMan", obj.linkMan);
			formProxy.setValue2("phoneNo", obj.phoneNo);
			formProxy.setValue2("telNo", obj.telNo);
			formProxy.setValue2("email", obj.email);
			formProxy.setValue2("carName", obj.carName);
			formProxy.setValue2("regionName", obj.regionName);
			formProxy.setValue2("street", obj.street);
			formProxy.setValue2("leaveMsg", obj.leaveMsg);
			formProxy.setValue2("saleAmount", "￥" + obj.saleAmount);
			formProxy.setValue2("discAmountSub", "-￥" + (obj.discAmountSub || "0"));
			formProxy.setValue2("amountInner", "-￥" + (obj.amountInner || "0"));
			formProxy.setValue2("amountOuter", "￥" + (obj.amountOuter || "0"));
			formProxy.setValue2("orderState", getOrderStateValue(obj));
			formProxy.setValue2("payState", payStates[obj.payState]);
			formProxy.setValue2("payWay", payWays[obj.payWay]);
			formProxy.setValue2("distState", distStates[obj.distState]);
			formProxy.setValue2("cancelled", judgeStates[obj.cancelled]);
			formProxy.setValue2("closed", judgeStates[obj.closed]);

			if (obj.saleOrderSvcs) {
				renderDetail(obj, "svcListTpl", "svcList");
			}

			if (obj.saleOrderRecords) {
				renderDetail(obj.saleOrderRecords, "recordListTpl", "recordList");
			}
			
		}

		// 弹出查看订单对话框
		function openShowDialog(obj) {
			initShowDialog(obj);
			showDialog.dialog("open");
		}

		// 初始化在线支付对话框
		function initPayDialog(obj) {
			$("#payDialog input[type=text]").val("");
			$("#payDialog textarea").val("");

			textSet("payOrderNo", obj.no);
			textSet("payShopName", obj.shop.name);
			textSet("payAmount", obj.amount);
			textSet("payPayWay", payWays[obj.payWay]);

			$("#payDialog input").attr("disabled", true);
			$("#payDialog textarea").attr("disabled", true);

			//$id("proxyNo").val(obj.no);
			
			payDialog = $("#payDialog").dialog({
				autoOpen : false,
				height : Math.min(300, $(window).height()),
				width : Math.min(700, $(window).width()),
				modal : true,
				title : '在线支付',
				buttons : {
					"支付" : function() {
						//Layer.msgInfo("在线支付功能尚未开通");
						//这里判断支付方式。
						var url = payWayUrl[obj.payWay];
						
						url = getAppUrl(makeUrl(url, {orderNo : obj.no}));
						
						//form提交
						var theForm = HiddenForm.newOne();
						theForm.id("orderForm");
						theForm.target("_blank");
						theForm.action(url);
						theForm.field("orderNo", obj.no );
						theForm.submit();
						
						//$id("proxyPayAction").submit();
						//关闭框
						payDialog.dialog("close");
						//弹出是否支付完成
						payResultConfirm(obj);
						
						//payDialog.dialog("close");
						//if (obj.payWay == "alipay") {
							//location.href = getAppUrl("/pay/alipay/pay") + "?orderNo=" + obj.no;
						//}
					},
					"取消" : function() {
						payDialog.dialog("close");
					}
				},
				close : function() {
					//
				}
			});
		}

		// 初始化POS支付对话框
		function initPosDialog(obj) {
			$("#posDialog input[type=text]").val("");

			textSet("posOrderNo", obj.no);
			textSet("posShopName", obj.shop.name);
			textSet("posAmount", obj.amount);
			textSet("posPayWay", payWays[obj.payWay]);

			$("#posDialog input").attr("disabled", true);
			$("#posNo").attr("disabled", false);

			posDialog = $("#posDialog").dialog({
				autoOpen : false,
				height : Math.min(300, $(window).height()),
				width : Math.min(700, $(window).width()),
				modal : true,
				title : 'POS支付',
				buttons : {
					"支付" : function() {
						if (obj.payWay == "pos") {
							if (doPosPay(obj)) {
								posDialog.dialog("close");
							}
						}
					},
					"取消" : function() {
						posDialog.dialog("close");
					}
				},
				close : function() {
					//
				}
			});
		}

		// 弹出代支付对话框
		function openPayDialog(obj) {
			if (obj.payWay == "pos") {
				initPosDialog(obj);
				posDialog.dialog("open");
			} else {
				initPayDialog(obj);
				payDialog.dialog("open");
			}
		}

		// 初始化服务完成对话框
		function initCompleteDialog(obj) {
			$("#completeDialog input[type=text]").val("");

			textSet("completeOrderNo", obj.no);
			textSet("completeUserMobile", obj.phoneNo);
			textSet("carInfo", obj.carName);

			$("#completeDialog input").attr("disabled", true);
			$("#doneCode").attr("disabled", false);
			$("#carInfo").attr("disabled", false);

			if (obj.saleOrderSvcs) {
				renderDetail(obj, "svcListTpl", "completeSvcList");
			}
			// 显示门店员工
			getWorkers("workerList");

			completeDialog = $("#completeDialog").dialog({
				autoOpen : false,
				height : Math.min(600, $(window).height()),
				width : Math.min(700, $(window).width()),
				modal : true,
				title : '完成服务',
				buttons : {
					"完成" : function() {
						if (completeSvc(obj)) {
							completeDialog.dialog("close");
						}
					},
					"取消" : function() {
						completeDialog.dialog("close");
					}
				},
				close : function() {
					//
				}
			});
		}

		// 弹出服务完成对话框
		function openCompleteDialog(obj) {
			initCompleteDialog(obj);
			completeDialog.dialog("open");
		}

		// 完成服务
		function completeSvc(obj) {
			var vResult = formProxyComplete.validateAll();
			if (!vResult) {
				return false;
			}
			
			var saleOrderActionPo = {};
			saleOrderActionPo.actionName = "complete";
			saleOrderActionPo.orderId = obj.id;
			saleOrderActionPo.doneCode = textGet("doneCode");
			saleOrderActionPo.carInfo = textGet("carInfo");
			saleOrderActionPo.workerIds = [];
			$("#workerList ul li input[type='checkbox']").each(function(){
				if ($(this).attr("checked") == "checked") {
					saleOrderActionPo.workerIds.add(parseInt($(this).attr("user-id")));
				}
			});
			
			var ajax = Ajax.post("/saleOrder/action/execute/do");
			ajax.data(saleOrderActionPo);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess("订单已完成");
					location.href = getAppUrl("/saleOrder/list/jsp/-shop");
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();

			return true;
		}
		
		// 初始化补充信息对话框
		function initAddInfoDialog(obj) {
			$("#addInfoDialog input[type=text]").val("");

			textSet("userCar", obj.carName);
			textSet("memo", obj.memo);
			
			var works = obj.saleOrderWorks;

			// 显示门店员工
			getWorkers("addInfoDlgWorkerList", works);

			addInfoDialog = $("#addInfoDialog").dialog({
				autoOpen : false,
				height : Math.min(400, $(window).height()),
				width : Math.min(700, $(window).width()),
				modal : true,
				title : '补充信息',
				buttons : {
					"提交" : function() {
						if (addInfo(obj)) {
							addInfoDialog.dialog("close");
						}
					},
					"取消" : function() {
						addInfoDialog.dialog("close");
					}
				},
				close : function() {
					//
				}
			});
		}

		// 弹出补充信息对话框
		function openAddInfoDialog(obj) {
			initAddInfoDialog(obj);
			addInfoDialog.dialog("open");
		}

		// 补充信息
		function addInfo(obj) {
			var vResult = formProxyAddInfo.validateAll();
			if (!vResult) {
				return false;
			}
			
			var saleOrderActionPo = {};
			saleOrderActionPo.actionName = "addInfo";
			saleOrderActionPo.orderId = obj.id;
			saleOrderActionPo.carInfo = textGet("userCar");
			saleOrderActionPo.memo = textGet("memo");
			saleOrderActionPo.workerIds = [];
			$("#addInfoDlgWorkerList ul li input[type='checkbox']").each(function(){
				if ($(this).attr("checked") == "checked") {
					saleOrderActionPo.workerIds.add(parseInt($(this).attr("user-id")));
				}
			});
			
			var ajax = Ajax.post("/saleOrder/action/execute/do");
			ajax.data(saleOrderActionPo);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess("补充信息完成");
					location.href = getAppUrl("/saleOrder/list/jsp/-shop");
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();

			return true;
		}
		
		function getWorkers(workerListId, works) {
			var ajax = Ajax.post("/shop/user/list/get/normal/-shop");
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					if (result.data == null) {
						return;
					}
					renderHtml(result.data, "workerListTpl", workerListId);
					$id(workerListId).find("ul li input[type='checkbox']").attr("disabled", false);
					$id(workerListId).find("ul li input[type='checkbox']").change(function(){
						var ckbox = $(this);
						var span_worker = ckbox.next();
						span_worker.toggleClass("text-blue");
					});
					
					if (works == null || works.length == 0) {
						return;
					}
					
					$id(workerListId).find("ul li input[type='checkbox']").each(function(){
						var workerId = parseInt($(this).attr("user-id"));
						for (var i = 0, len = works.length; i < len; i++) {
							if (works[i].workerId == workerId) {
								$(this).attr("checked", "checked");
								$(this).next().toggleClass("text-blue");
							}
						}
					});
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		// 初始化用户信息对话框
		function initUserInfoDialog(userId) {
			// 读取用户信息
			getUserInfo(userId);

			userInfoDialog = $("#userInfoDialog").dialog({
				autoOpen : false,
				height : Math.min(500, $(window).height()),
				width : Math.min(700, $(window).width()),
				modal : true,
				title : '用户信息',
				buttons : {
					"关闭" : function() {
						userInfoDialog.dialog("close");
					}
				},
				close : function() {
					//
				}
			});
		}

		// 弹出用户信息对话框
		function openUserInfoDialog(userId) {
			initUserInfoDialog(userId);
			userInfoDialog.dialog("open");
		}

		// 用户信息
		function getUserInfo(userId) {
			var ajax = Ajax.post("/user/saleOrder/list/get/-shop");
			ajax.params({
				userId : userId
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					renderHtml(result.data, "svcOrdersTpl", "svcOrders");
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();

			return true;
		}

		// POS支付
		function doPosPay(obj) {
			var vldResult = formProxyPos.validateAll();
			if (!vldResult) {
				return false;
			}
			// 获取输入的刷卡回执单编号
			var posNo = textGet("posNo");

			var ajax = Ajax.post("/saleOrder/action/execute/do");
			ajax.params({
				orderId : obj.id,
				action : "posPay",
				posNo : posNo
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess("支付成功");
					location.href = getAppUrl("/saleOrder/list/jsp/-shop");
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();

			return true;
		}
		
		function doCancel(orderId) {
			var yesHandler = function() {
				var saleOrderActionPo = {};
				saleOrderActionPo.actionName = "cancel";
				saleOrderActionPo.orderId = orderId;
				// 发送请求
				var ajax = Ajax.post("/saleOrder/action/execute/do");
				ajax.data(saleOrderActionPo);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess("订单已取消");
						location.href = getAppUrl("/saleOrder/list/jsp/-shop");
					} else {
						Layer.warning(result.message);
					}
				});
				ajax.go();
			}
			
			Layer.confirm("确定要取消该订单吗？", yesHandler);
		}
		
		// 获取整合后的状态信息
		function getOrderStateValue(obj) {
			if (obj.cancelled == true) {
				return "已取消";
			} else if (obj.payState == "unpaid") {
				return "未付款";
			} else if (obj.finished == true) {
				return "已完成";
			} else {
				return "未服务";
			}
		}

		// 获取整合后的状态信息（含样式）
		function getOrderState(obj) {
			if (obj.cancelled == true) {
				return "<span style='color: gray'>已取消</span>";
			} else if (obj.payState == "unpaid") {
				return "<span style='color: blue'>未付款</span>";
			} else if (obj.finished == true) {
				return "<span>已完成</span>";
			} else {
				return "<span style='color: orange'>未服务</span>";
			}
		}

		// 查询订单
		function querySaleOrder() {
			var vldResult = formProxyQuery.validateAll();
			if (!vldResult) {
				return;
			}
			var customerName = formProxyQuery.getValue("queryCustomerName");
			var orderState = formProxyQuery.getValue("queryOrderState");
			var fromDate = formProxyQuery.getValue("fromDate");
			var toDate = formProxyQuery.getValue("toDate");
			// 加载jqGridCtrl
			jqGridCtrl.jqGrid("setGridParam", {
				postData : {
					filterStr : JSON.encode({
						"customerName" : customerName,
						"orderState" : orderState,
						"fromDate" : fromDate,
						"toDate" : toDate
					}, true)
				},
				page : 1
			}).trigger("reloadGrid");
		}

		function getStarFlag(orderId) {
			var starFlag = null;
			var ajax = Ajax.post("/saleOrder/starFlag/get");
			ajax.params({
				orderId : parseInt(orderId)
			});
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					starFlag = result.data;
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
			
			return starFlag;
		}

		function setStarFlag(orderId, obj) {
			var starFlag = getStarFlag(orderId);
			if (starFlag == null) {
				return;
			}
			// 点击时修改状态
			starFlag = !starFlag;
			
			var ajax = Ajax.post("/saleOrder/starFlag/update/do");
			ajax.params({
				orderId : parseInt(orderId),
				starFlag : starFlag
			});
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					if (starFlag) {
						$(obj).html("&nbsp&nbsp&nbsp*&nbsp&nbsp&nbsp");
					} else {
						$(obj).html("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		function getUserName(userObj) {
			if (userObj.realName != null) {
				return userObj.realName;
			}
			if (userObj.nickName != null) {
				return userObj.nickName;
			}
			return userObj.phoneNo;
		}
		
		//支付结果   确认提示框
		function payResultConfirm(obj) {
			/* if(obj.payWay == "wechatpay"){
				var dom = "#qrCodeConfirmText";
			} */
			var dom = "#refundConfirmText";
			var theDlg = Layer.dialog({
				dom : dom, //或者 html string
				area : [ '300px', '200px' ],
				closeBtn : true,
				title : "支付操作确认",
				btn : [ '是，已支付', '否，未支付' ],
				yes : function() {
					theDlg.hide();
					//2、稍等
					var hintBox = Layer.progress("更新数据中，请稍等...");
					setTimeout(function() {
						hintBox.hide();
						//刷新
						jqGridCtrl.jqGrid("setGridParam", {
							page : 1
						}).trigger("reloadGrid");
					}, 2000);
					
				},
				cancel : function() {
					theDlg.hide();
					//2、稍等
					var hintBox = Layer.progress("更新数据中，请稍等...");
					setTimeout(function() {
						hintBox.hide();
						//刷新
						jqGridCtrl.jqGrid("setGridParam", {
							page : 1
						}).trigger("reloadGrid");
					}, 2000);
				}
			});
		}
		
		function initPlanTimeDatePicker(){
			// 初始化日期控件
			$id("planTime").datetimepicker({
				  lang:"ch",
			      format:"Y-m-d H:i"
			});
		}
		
		function saleOrderDialogEdit(rowId) {
			var userMap = shopGridHelper.getRowData(rowId);
			renderSaleOrderData(userMap);
			$("#showDialog  input").attr("disabled", true);
			$("#showDialog textarea").attr("disabled", true);
			$("#planTime").attr("disabled", false);
			initPlanTimeDatePicker();
			$("#showDialog").dialog({
				autoOpen : false,
				height : Math.min(650, $(window).height()),
				width : Math.min(750, $(window).width()),
				modal : true,
				title : '修改销售订单预约时间',
				buttons : {
					"保存" : function() {
						updateSaleOrder(rowId);
					},
					"关闭" : function() {
						$("#showDialog").dialog("close");
					}
				},
				close : function() {
					formProxy.hideMessages();
				}
			});
			$("#showDialog").dialog("open");
			
		}
		
		function updateSaleOrder(rowId) {
			var vldResult = formProxy.validateAll();
			if (vldResult) {
				var ajax = Ajax.post("/saleOrder/planTime/update/do");
				var planTimeVal = $id("planTime").val();
				data = {
					id : rowId,
					planTime : planTimeVal
				};
				ajax.data(data);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						//加载最新数据列表
						jqGridCtrl.jqGrid().trigger("reloadGrid");						
					} else {
						Layer.msgWarning(result.message);
					}
					
				});
				ajax.go();				
			}
			$("#showDialog").dialog("close");
		}
 

		// ------------------------初始化-------------------------
		$(function() {
			// 页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 55,
				allowTopResize : false
			});
			// 隐藏布局north分割线
			$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
			
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
			
			// 初始化日期控件
			$id("fromDate").datetimepicker({
				lang : 'ch',
				format : 'Y-m-d'
			});
			$id("toDate").datetimepicker({
				lang : 'ch',
				format : 'Y-m-d'
			});
			var toDate = new Date();
			var fromDate = new Date();
			fromDate.setDate(toDate.getDate() - 7);
			dateSet("toDate", toDate);
			dateSet("fromDate", fromDate);

			// 初始化表格控件
			jqGridCtrl = $("#theGridCtrl").jqGrid({
				url : getAppUrl("/saleOrder/list/get/-shop"),
				contentType : 'application/json',
				mtype : "post",
				datatype : 'json',
				height : "100%",
				width : "100%",
				postData : {
					filterStr : JSON.encode({
						"fromDate" : formProxyQuery.getValue("fromDate"),
						"toDate" : formProxyQuery.getValue("toDate")
					}, true)
				},
				colNames : [ "id", "标记", "订单编号", "店铺名称", "用户", "用户车辆", "订单状态", "下单时间", "预约时间 ", "预约时间修改次数 ", " 操作" ],
				colModel : [ {
					name : "id",
					index : "id",
					hidden : true
				}, {
					name : "starFlag",
					index : "starFlag",
					formatter : function(cellValue, option, rowObject) {
						var s = "<a class='star-flag' href='javascript:void(0);' onclick='setStarFlag(" + rowObject.id + "," + "this" + ")'>&nbsp&nbsp&nbsp&nbsp&nbsp</a>";
						if (rowObject.starFlag) {
							s = "<a class='star-flag' href='javascript:void(0);' onclick='setStarFlag(" + rowObject.id + "," + "this" + ")'>&nbsp&nbsp*&nbsp&nbsp</a>";
						}
						return s;
					},
					width : 30,
					align : 'center'
				}, {
					name : "no",
					index : "no",
					width : 130,
					align : 'center'
				}, {
					name : "shop.name",
					index : "shop.name",
					width : 100,
					align : 'center'
				}, {
					name : "userName",
					index : "userName",
					formatter : function(cellValue, option, rowObject) {
						return "<a href='javascript:void(0);' onclick='openUserInfoDialog(" + rowObject.userId + ")'>" + cellValue + "</a>";
					},
					width : 100,
					align : 'center'
				}, {
					name : "carName",
					index : "carName",
					width : 170,
					align : 'left'
				}, {
					name : "orderState",
					index : "orderState",
					formatter : function(cellValue, option, rowObject) {
						return getOrderState(rowObject);
					},
					width : 50,
					align : 'center'
				}, {
					name : "createTime",
					index : "createTime",
					width : 200,
					align : "left"
				}, {
					name : "planTime",
					index : "planTime",
					width : 200,
					align : "left"
				}, 
				 {
					name : "planModTimes",
					index : "planModTimes",
					width : 200,
					align : "center"
				},{
					name : 'id',
					index : 'id',
					formatter : function(cellValue, option, rowObject) {
						var s = "<span> [<a class='item' href='javascript:void(0);' onclick='openShowDialog(" + JSON.stringify(rowObject) + ")' >查看</a>]";
						if (rowObject.cancelled) {
							return s;
						} else if (rowObject.payState == "unpaid" && rowObject.cancelled == false) {
							s += "<span> [<a class='item' href='javascript:void(0);' onclick='openPayDialog(" + JSON.stringify(rowObject) + ")' >支付</a>]";
						} else if (rowObject.payState == "paid" && rowObject.finished == false) {
							s += "<span> [<a class='item' href='javascript:void(0);' onclick='openCompleteDialog(" + JSON.stringify(rowObject) + ")' >完成</a>]";
							s += "<span> [<a class='item' href='javascript:void(0);' onclick='doCancel(" + rowObject.id + ")' >取消</a>]";
						} else if (rowObject.payState == "paid" && rowObject.finished == true) {
							s += "<span> [<a class='item' href='javascript:void(0);' onclick='openAddInfoDialog(" + JSON.stringify(rowObject) + ")' >补充信息</a>]";
						}
						if(rowObject.finished!=true&&rowObject.cancelled != true){
							s+="[<a href='javascript:void(0);' onclick='saleOrderDialogEdit("
								+ cellValue
								+ ")' >修改预约时间</a>]";
						}
						return s;
					},
					width : 200,
					align : "center"
				} ],
				pager : "#theGridPager",
				loadComplete : function(gridData) { // JqGridHelper缓存最新的grid数据
					shopGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if (isFunction(callback)) {
						callback();
					}
				},
				ondblClickRow : function(rowId) {
					var userMap = shopGridHelper.getRowData(rowId)
					openShowDialog(userMap);
				}
			});

			// 空函数，在弹出框消失后重写调用
			function getCallbackAfterGridLoaded() {
			}

			// 绑定查询
			$id("btnQuery").click(querySaleOrder);
		});
	</script>
</body>
<script type="text/html" id="svcListTpl">
	{{# var svcs = d.saleOrderSvcs; }}
	{{# var goods = d.saleOrderGoods; }}
	{{# if (svcs != null) { }}
		{{# var svcCount = svcs.length; }}
		{{# for(var i = 0; i < svcCount; i++) {  }}
			<tr class="row">
				<td>{{ svcs[i].svcName || "" }}</td>
				<td>{{ svcs[i].salePrice || "" }}</td>
				<td>1</td>
			</tr>
		{{# } }}
	{{# } }}
	{{# if (goods != null) { }}
		{{# var goodsCount = goods.length; }}
		{{# for(var j = 0; j < goodsCount; j++) {  }}
			<tr class="row">
				<td>{{ goods[j].productName || "" }}</td>
				<td>{{ goods[j].salePrice || "" }}</td>
				<td>{{ goods[j].quantity || "" }}</td>
			</tr>
		{{# } }}
	{{# } }}
</script>
<script type="text/html" id="recordListTpl">
	{{# var records = d; }}
	{{# for(var i = 0, len = records.length; i < len; i++) {  }}
		<tr class="row">
			<td>{{ records[i].actorName || "" }}</td>
			<td>{{ records[i].actRole || "" }}</td>
			<td>{{ Actions[records[i].action] || "" }}</td>
			<td>{{ records[i].ts || "" }}</td>
		</tr>
	{{# } }}
</script>
<script type="text/html" id="workerListTpl">
	{{# var users = d; }}
	{{# for(var i = 0, len = users.length; i < len; i++) {  }}
		<li class="worker">
			<input type="checkbox" user-id="{{ users[i].id }}"></input>
			<span>{{ users[i].nickName || "" }}</span>
		</li>
	{{# } }}
</script>

<script type="text/html" id="svcOrdersTpl">
	{{# var saleOrders = d; }}
	{{# for(var i = 0, len = saleOrders.length; i < len; i++) {  }}
		<li class="order-info">时间: {{ saleOrders[i].finishedTime || "" }}</li>
		<li class="order-info">车辆: {{ saleOrders[i].carName || "" }}</li>
		<li class="order-info">
			服务：
			{{# var svcs = saleOrders[i].saleOrderSvcs; }}
			{{# if (svcs != null) { }}
				{{# for (var j = 0, len_svcs = svcs.length; j < len_svcs; j++) { }}
					{{# if (svcs[j].svcId != -1) { }}
						{{ svcs[j].svcName || "" }}
					{{# } }}
					{{# if (j != len_svcs - 1) { }}
						、
					{{# } }}
				{{# } }}
			{{# } }}
		</li>
		<li class="order-info">订单备注：{{ saleOrders[i].memo || "" }}</li>
		{{# if (i != len - 1) { }}
		<br/>
		{{# } }}
	{{# } }}
</script>
</html>