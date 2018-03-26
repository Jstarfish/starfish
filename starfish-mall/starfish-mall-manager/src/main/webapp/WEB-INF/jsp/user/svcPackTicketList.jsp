<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>销售订单列表</title>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group right aligned">
					<label class="label">服务名称</label> <input id="querySvcName"
						class="input" /> <span class="spacer"></span> <label
						class="label">用户昵称/手机号</label> <input id="queryUserName"
						class="input" /> <span class="spacer"></span> <label
						class="label">状态</label> <select id="queryState"
						class="input">
						<option>全部</option>
						<option value="unused">未使用</option>
						<option value="used">已使用</option>
						<option value="cancelled">已取消</option>
					</select> <span class="spacer"></span> <label class="label">完成日期</label> <input
						class="input" id="fromDate" /> 至 <input class="input" id="toDate" />
					<span class="spacer two wide"></span>
					<button id="btnQuery" class="button">查询</button>
				</div>
			</div>
		</div>
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	<div id="orderDialog" style="display: none;">
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
	
	<div id="detailDialog" style="display: none;">
		<div id="detailForm" class="form">
			<div class="field row">
				<label class="field label">服务项目</label> <input type="text"
					id="detailSvcName" class="field value one half wide" /> <label
					class="field inline label one half wide">用户手机</label> <input
					type="text" id="detailUserPhone" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">服务套餐</label> <input type="text"
					id="detailSvcPackName" class="field value one half wide" /> <label
					class="field inline label one half wide">隶属订单</label> <input
					type="text" id="detailOrderNo" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">服务票状态</label> <input type="text"
					id="detailState" class="field value one half wide" /> <label
					class="field inline label one half wide">服务门店</label> <input
					type="text" id="detailShopName" class="field value one half wide" />
			</div>
			<br />
			<span id="divider" class="normal hr divider"></span>
			<div class="field row">
				<label class="field label">服务原价</label> <input type="text"
					id="detailSalePrice" class="field value one half wide" /> <label
					class="field inline label one half wide">使用时间</label> <input
					type="text" id="detailFinishTime" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">操作人</label> <input type="text"
					id="detailActorName" class="field value one half wide" /> <label
					class="field inline label one half wide">操作人角色</label> <input
					type="text" id="detailActorRole" class="field value one half wide" />
			</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		// 服务票查看页面
		var detailDialog;
		// 订单信息查看对话框
		var orderDialog;
		// 缓存当前jqGrid数据行数组
		var shopGridHelper = JqGridHelper.newOne("");
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
			"weipay" : "微信在线支付",
			"pos" : "POS机刷卡",
			"unionpay" : "银联支付",
			"ecardpay" : "e卡支付",
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

		// 渲染
		function renderDetail(data, fromId, toId) {
			var tplHtml = $id(fromId).html();
			var theTpl = laytpl(tplHtml);
			var htmlStr = theTpl.render(data);
			$id(toId).find("tbody").html(htmlStr);
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
		var formProxyOrder = FormProxy.newOne();

		// 注册表单控件
		formProxyOrder.addField({
			id : "orderNo",
			key : "no",
		});
		formProxyOrder.addField({
			id : "merchantName",
			key : "merchantName",
		});
		formProxyOrder.addField({
			id : "shopName",
			key : "shop.name",
		});
		formProxyOrder.addField({
			id : "shopTel",
			key : "shop.telNo",
		});
		formProxyOrder.addField({
			id : "submitTime",
			key : "ts",
		});
		formProxyOrder.addField({
			id : "planTime",
			key : "planTime",
		});
		formProxyOrder.addField({
			id : "customerName",
			key : "customer.nickName",
		});
		formProxyOrder.addField({
			id : "linkMan",
			key : "linkMan",
		});
		formProxyOrder.addField({
			id : "phoneNo",
			key : "phoneNo",
		});
		formProxyOrder.addField({
			id : "telNo",
			key : "telNo",
		});
		formProxyOrder.addField({
			id : "email",
			key : "email",
		});
		formProxyOrder.addField({
			id : "carModel",
			key : "carModel",
		});
		formProxyOrder.addField({
			id : "regionName",
			key : "regionName",
		});
		formProxyOrder.addField({
			id : "street",
			key : "street",
		});
		formProxyOrder.addField({
			id : "leaveMsg",
			key : "leaveMsg",
		});
		formProxyOrder.addField({
			id : "saleAmount",
			key : "saleAmount",
		});
		formProxyOrder.addField({
			id : "discAmountSub",
			key : "discAmountSub",
		});
		formProxyOrder.addField({
			id : "amountInner",
			key : "amountInner",
		});
		formProxyOrder.addField({
			id : "amountOuter",
			key : "amountOuter",
		});
		formProxyOrder.addField({
			id : "orderState",
			key : "orderState",
		});
		formProxyOrder.addField({
			id : "payState",
			key : "payState",
		});
		formProxyOrder.addField({
			id : "payWay",
			key : "payWay",
		});
		formProxyOrder.addField({
			id : "distState",
			key : "distState",
		});
		formProxyOrder.addField({
			id : "cancelled",
			key : "cancelled",
		});
		formProxyOrder.addField({
			id : "closed",
			key : "closed",
		});

		// 实例化查询表单代理
		var formProxyQuery = FormProxy.newOne();

		// 注册表单控件
		formProxyQuery.addField({
			id : "querySvcName",
			rules : [ "maxLength[30]" ]
		});
		formProxyQuery.addField({
			id : "queryUserName",
			rules : [ "maxLength[30]" ]
		});
		formProxyQuery.addField({
			id : "queryState",
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

		// 初始化订单页面
		function initOrderDialog(obj) {
			$("#orderDialog input[type=text]").val("");
			$("#orderDialog textarea").val("");
			formProxyOrder.setValue2("no", obj.no);
			formProxyOrder.setValue2("merchantName", obj.merchantName);
			formProxyOrder.setValue2("shop.name", obj.shop.name);
			formProxyOrder.setValue2("shop.telNo", obj.shop.phoneNo);
			formProxyOrder.setValue2("ts", obj.createTime);
			formProxyOrder.setValue2("planTime", obj.planTime);
			formProxyOrder.setValue2("customer.nickName", obj.userName);
			formProxyOrder.setValue2("linkMan", obj.linkMan);
			formProxyOrder.setValue2("phoneNo", obj.phoneNo);
			formProxyOrder.setValue2("telNo", obj.telNo);
			formProxyOrder.setValue2("email", obj.email);
			formProxyOrder.setValue2("carModel", obj.carName);
			formProxyOrder.setValue2("regionName", obj.regionName);
			formProxyOrder.setValue2("street", obj.street);
			formProxyOrder.setValue2("leaveMsg", obj.leaveMsg);
			formProxyOrder.setValue2("saleAmount", "￥" + obj.saleAmount);
			formProxyOrder.setValue2("discAmountSub", "-￥" + (obj.discAmountSub || "0"));
			formProxyOrder.setValue2("amountInner", "-￥" + (obj.amountInner || "0"));
			formProxyOrder.setValue2("amountOuter", "￥" + (obj.amountOuter || "0"));
			formProxyOrder.setValue2("orderState", getOrderStateValue(obj));
			formProxyOrder.setValue2("payState", payStates[obj.payState]);
			formProxyOrder.setValue2("payWay", payWays[obj.payWay]);
			formProxyOrder.setValue2("distState", distStates[obj.distState]);
			formProxyOrder.setValue2("cancelled", judgeStates[obj.cancelled]);
			formProxyOrder.setValue2("closed", judgeStates[obj.closed]);

			if (obj.saleOrderSvcs) {
				renderDetail(obj, "detailedListTpl", "detailedList");
			}

			if (obj.saleOrderRecords) {
				renderDetail(obj.saleOrderRecords, "recordListTpl", "recordList");
			}

			$("#orderDialog input").attr("disabled", true);
			$("#orderDialog textarea").attr("disabled", true);

			orderDialog = $("#orderDialog").dialog({
				autoOpen : false,
				height : Math.min(650, $(window).height()),
				width : Math.min(700, $(window).width()),
				modal : true,
				title : '查看销售订单详情',
				buttons : {
					"关闭" : function() {
						orderDialog.dialog("close");
					}
				},
				close : function() {
					//formProxyOrder.hideMessages();
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
		function getTicketState(obj) {
			if (obj.cancelled == true) {
				return "<span style='color: gray'>已取消</span>";
			} else if (obj.finished == false) {
				return "<span style='color: orange'>未使用</span>";
			} else {
				return "<span style='color: blue'>已使用</span>";
			}
		}
		
		// 获取整合后的状态
		function getTicketStateValue(obj) {
			if (obj.cancelled == true) {
				return "已取消";
			} else if (obj.finished == false) {
				return "未使用";
			} else {
				return "已使用";
			}
		}

		// 弹出订单页面
		function openOrderDialog(orderId) {
			var ajax = Ajax.post("/saleOrder/detail/get");
			ajax.params({
				orderId : parseInt(orderId)
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					if (result.data != null) {
						initOrderDialog(result.data);
						orderDialog.dialog("open");
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		// 初始化查看页面
		function initDetailDialog(obj) {
			$("#detailDialog input[type=text]").val("");
			
			textSet("detailSvcName", obj.svcName);
			textSet("detailUserPhone", obj.user.phoneNo);
			textSet("detailSvcPackName", obj.svcPackName);
			textSet("detailOrderNo", obj.orderNo);
			textSet("detailState", getTicketStateValue(obj));
			textSet("detailShopName", obj.shop.name);
			textSet("detailSalePrice", "￥" + obj.svcSalePrice);
			textSet("detailFinishTime", obj.finishTime);
			textSet("detailActorName", obj.actorName);
			textSet("detailActorRole", obj.actRole);

			$("#detailDialog input").attr("disabled", true);

			detailDialog = $("#detailDialog").dialog({
				autoOpen : false,
				height : Math.min(400, $(window).height()),
				width : Math.min(700, $(window).width()),
				modal : true,
				title : '查看服务票详情',
				buttons : {
					"关闭" : function() {
						detailDialog.dialog("close");
					}
				},
				close : function() {
					//formProxyDetail.hideMessages();
				}
			});
		}
		
		// 弹出查看对话框
		function openDetailDialog(obj) {
			initDetailDialog(obj);
			detailDialog.dialog("open");
		}

		function queryTicket() {
			var vldResult = formProxyQuery.validateAll();
			if (!vldResult) {
				return;
			}
			var svcName = formProxyQuery.getValue("querySvcName");
			var userName = formProxyQuery.getValue("queryUserName");
			var state = formProxyQuery.getValue("queryState");
			var fromDate = formProxyQuery.getValue("fromDate");
			var toDate = formProxyQuery.getValue("toDate");
			// 加载jqGridCtrl
			jqGridCtrl.jqGrid("setGridParam", {
				postData : {
					filterStr : JSON.encode({
						"svcName" : svcName,
						"userName" : userName,
						"state" : state,
						"fromDate" : fromDate,
						"toDate" : toDate
					}, true)
				},
				page : 1
			}).trigger("reloadGrid");
		}

		// ------------------------初始化-------------------------
		$(function() {
			// 页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 160,
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

			jqGridCtrl = $("#theGridCtrl").jqGrid({
				url : getAppUrl("/user/svc/pack/ticket/list/get/-mall"),
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
				colNames : [ "id", "套餐名称", "服务项", "用户", "服务门店", "隶属订单", "状态", "完成时间", " 操作" ],
				colModel : [ {
					name : "id",
					index : "id",
					hidden : true
				}, {
					name : "svcPackName",
					index : "svcPackName",
					width : 100,
					align : 'left'
				}, {
					name : "svcName",
					index : "svcName",
					width : 100,
					align : 'left'
				}, {
					name : "user.nickName",
					index : "user.nickName",
					width : 100,
					align : 'left'
				}, {
					name : "shop.name",
					index : "shop.name",
					width : 100,
					align : 'left'
				}, {
					name : "orderNo",
					index : "orderNo",
					width : 200,
					align : 'center'
				}, {
					name : "state",
					index : "state",
					formatter : function(cellValue, option, rowObject) {
						return getTicketState(rowObject);
					},
					width : 80,
					align : 'center'
				}, {
					name : "finishTime",
					index : "finishTime",
					width : 100,
					align : "center"
				}, {
					name : 'id',
					index : 'id',
					formatter : function(cellValue, option, rowObject) {
						var s = "<span> [<a class='item' href='javascript:void(0);' onclick='openDetailDialog(" + JSON.stringify(rowObject) + ")' >查看</a>]";
						s += "<span> [<a class='item' href='javascript:void(0);' onclick='openOrderDialog(" + rowObject.orderId + ")' >查看隶属订单</a>]";
						return s;
					},
					width : 150,
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
					var ticket = shopGridHelper.getRowData(rowId)
					openDetailDialog(ticket);
				}
			});

			// 空函数，在弹出框消失后重写调用
			function getCallbackAfterGridLoaded() {
			}

			// 绑定查询
			$id("btnQuery").click(queryTicket);
		});
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