<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>合作店订单管理</title>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="right aligned group">
					<label class="label">合作店名称</label>
						<input id="queryDistShopName" class="input" /> <span class="spacer"></span>
					<label class="label">订单状态</label>
						<select id="queryOrderState" class="input">
							<option>全部</option>
							<option value="unhandled">未付款</option>
							<option value="processing">未享用</option>
							<option value="cancelled">已取消</option>
							<option value="finished">已完成</option>
						</select> <span class="spacer"></span>
					<label class="label">下单日期</label> 
						<input class="input" id="fromDate" /> 至 <input class="input" id="toDate" />
						<span class="spacer two wide"></span>
					<button id="btnQuery" class="button">查询</button>
				</div>
			</div>
		</div>
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	
	<div id="saleOrderDialog" style="display: none;">
		<div id="viewForm" class="form">
			<input type="hidden" id="id" />
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
					class="field inline label one half wide">用户车辆</label> <input
					type="text" id="carModel" class="field value one half wide" />
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
				<table id="detailedList" width="100%" class="cleanTbl">
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

	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		//-------------------------------------------------全局变量-------------------------------------------------------
		var orderIds = null;
		var factAmount = 0;
		var jqGridCtrl = null;
		var jqGridHelper = JqGridHelper.newOne("");
		
		var formProxyQuery = FormProxy.newOne();
		formProxyQuery.addField({
			id : "queryDistShopName",
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
			id : "carModel",
			key : "carModel",
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
		// 支付方式字典
		var payWays = {
			"alipay" : "支付宝在线支付",
			"wechatpay" : "微信在线支付",
			"abcAsUnionpay" : "银联在线支付",
			"ecardpay" : "e卡支付",
			"unionpay" : "银联支付",
			"coupon" : "优惠券支付"
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
		
		//-------------------------------------------------页面加载-------------------------------------------------------
		$(function() {
			// 调整页面布局
			adjustLayoutSize();
			// 初始化日期控件
			initDateTimePicker();
			// 绑定查询
			$id("btnQuery").click(refreshData);
			// 加载数据
			loadData();
			// 调整控件布局
			winSizeMonitor.start(adjustCtrlsSize);
		});
		
		//-------------------------------------------------订单详情-------------------------------------------------------
		
		// 初始化查看页面
		function initShowDialog(obj) {
			renderSaleOrderData(obj);

			$("#saleOrderDialog  input").attr("disabled", true);
			$("#saleOrderDialog textarea").attr("disabled", true);

			showDialog = $("#saleOrderDialog").dialog({
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

		// 获取整合后的状态（含样式）
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
		
		// 弹出查看商户页面
		function openShowDialog(obj) {
			initShowDialog(obj);
			showDialog.dialog("open");
		}
		
		// 装载数据
		function renderSaleOrderData(obj){
			$("#saleOrderDialog input[type=text]").val("");
			$("#saleOrderDialog textarea").val("");
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
			formProxy.setValue2("carModel", obj.carName);
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
				renderDetail(obj, "detailedListTpl", "detailedList");
			}

			if (obj.saleOrderRecords) {
				renderDetail(obj.saleOrderRecords, "recordListTpl", "recordList");
			}
		}
		
		//-------------------------------------------------初始数据-------------------------------------------------------
		
		// 加载数据
		function loadData(){
			jqGridCtrl = $("#theGridCtrl").jqGrid({
				url : getAppUrl("/distshop/order/list/get"),
				contentType : 'application/json',
				mtype : "post",
				datatype : 'json',
				height : "100%",
				width : "100%",
				postData : {
					filterStr : JSON.encode({
						fromDate : formProxyQuery.getValue("fromDate"),
						toDate : formProxyQuery.getValue("toDate"),
					}, true)
				},
				colNames : [ "id", "订单编号", "合作店名称", "用户", "用户车辆", "订单状态", "下单时间", "预约时间 ", "预约时间修改次数 ", "操作" ],
				colModel : [ {
					name : "id",
					index : "id",
					hidden : true
				}, {
					name : "no",
					index : "no",
					width : 130,
					align : 'center'
				}, {
					name : "distShopName",
					index : "distShopName",
					width : 130,
					align : 'center'
				}, {
					name : "userName",
					index : "userName",
					width : 100,
					align : 'center'
				}, {
					name : "carName",
					index : "carName",
					width : 130,
					align : 'center'
				},{
					name : "orderState",
					index : "orderState",
					formatter :  function(cellValue, option, rowObject) {
						return getOrderState(rowObject);
					},
					width : 80,
					align : 'center'
				}, {
					name : "createTime",
					index : "createTime",
					width : 130,
					align : "center"
				}, {
					name : "planTime",
					index : "planTime",
					width : 130,
					align : "center"
				}, {
					name : "planModTimes",
					index : "planModTimes",
					width : 120,
					align : 'center'
				}, {
					name : "id",
					index : "id",
					formatter : function(cellValue, option, rowObject) {
						var s = "<span> [<a class='item' href='javascript:void(0);' onclick='openShowDialog(" + JSON.stringify(rowObject) + ")' >查看</a>]";
						return s;
					},
					width : 80,
					align : 'center'
				} ],
				pager : "#theGridPager",
				loadComplete : function(gridData) { // JqGridHelper缓存最新的grid数据
					jqGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if (isFunction(callback)) {
						callback();
					}
				},
				ondblClickRow : function(rowId) {
					var userMap = jqGridHelper.getRowData(rowId);
					openShowDialog(userMap);
				}
			});
		}

		// 刷新数据
		function refreshData() {
			var vldResult = formProxyQuery.validateAll();
			if (!vldResult) {
				return;
			}
			var filter = {};
			var distShopName = formProxyQuery.getValue("queryDistShopName");
			var orderState = formProxyQuery.getValue("queryOrderState");
			var fromDate = formProxyQuery.getValue("fromDate");
			var toDate = formProxyQuery.getValue("toDate");
			if(distShopName){
				filter.distShopName = distShopName;
			}
			if(orderState){
				filter.orderState = orderState;
			}
			if(fromDate){
				filter.fromDate = fromDate;
			}
			if(toDate){
				filter.toDate = toDate;
			}
			jqGridCtrl.jqGrid("setGridParam", {
				postData : {
					filterStr : JSON.encode(filter, true)
				},
				page : 1
			}).trigger("reloadGrid");
		}
		
		// 初始化页面基本布局
		function adjustLayoutSize(){
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 160,
				allowTopResize : false
			});
		}
		
		// 渲染
		function renderDetail(data, fromId, toId) {
			var tplHtml = $id(fromId).html();
			var theTpl = laytpl(tplHtml);
			var htmlStr = theTpl.render(data);
			$id(toId).find("tbody").html(htmlStr);
		}
		
		// 初始化jqGrid控件布局
		function adjustCtrlsSize() {
			// 隐藏布局north分割线
			$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "theGridCtrl";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("theGridPager").height();
			jqGridCtrl.setGridWidth(mainWidth - 1);
			jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 90);
		}
		
		// 初始化日期控件
		function initDateTimePicker(){
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
		}
		
		// 空函数，在弹出框消失后重写调用
		function getCallbackAfterGridLoaded() { }
	</script>
</body>
<script type="text/html" id="detailedListTpl">
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
</html>