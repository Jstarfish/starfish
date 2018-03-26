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
	<div id="dialog" style="display: none;">
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
	
	<div id="completeDialog" style="display: none;">
		<div id="completeForm" class="form">
			<div class="field row">
				<label class="field label one half wide">服务项目</label> <input
					type="text" id="completeSvcName" class="field value one half wide" />
				<label class="field inline label one half wide">用户手机号</label> <input
					type="text" id="completeUserMobile"
					class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label required one half wide">确认码</label> <input
					type="text" id="doneCode" class="field value one half wide" maxlength="6" />
			</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		// 服务票查看页面
		var detailDialog;
		// 订单信息查看对话框
		var showDialog;
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
		
		// 确认完成表单代理对象
		var formProxyComplete = FormProxy.newOne();

		// 注册确认完成表单控件
		formProxyComplete.addField({
			id : "doneCode",
			key : "doneCode",
			required : true,
			rules : [ "maxLength[6]" ]
		});
		
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

		// 初始化查看页面
		function initOrderDialog(obj) {
			$("#dialog input[type=text]").val("");
			$("#dialog textarea").val("");
			formProxy.setValue2("no", obj.no);
			formProxy.setValue2("merchantName", obj.merchantName);
			formProxy.setValue2("shop.name", obj.shop.name);
			formProxy.setValue2("shop.telNo", obj.shop.phoneNo);
			formProxy.setValue2("ts", obj.createTime);
			formProxy.setValue2("planTime", obj.planTime);
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

			$("#dialog  input").attr("disabled", true);
			$("#dialog textarea").attr("disabled", true);

			showDialog = $("#dialog").dialog({
				autoOpen : false,
				height : Math.min(650, $(window).height()),
				width : Math.min(700, $(window).width()),
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

		// 弹出查看商户页面
		function openOrderDialog(orderId) {
			var ajax = Ajax.post("/saleOrder/detail/get");
			ajax.params({
				orderId : parseInt(orderId)
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					if (result.data != null) {
						initOrderDialog(result.data);
						showDialog.dialog("open");
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		// 初始化服务完成对话框
		function initCompleteDialog(obj) {
			$("#completeDialog input[type=text]").val("");

			textSet("completeSvcName", obj.svcName);
			textSet("completeUserMobile", obj.user.phoneNo);

			$("#completeDialog input").attr("disabled", true);
			$("#doneCode").attr("disabled", false);

			completeDialog = $("#completeDialog").dialog({
				autoOpen : false,
				height : Math.min(300, $(window).height()),
				width : Math.min(700, $(window).width()),
				modal : true,
				title : '确认完成',
				buttons : {
					"完成" : function() {
						if (complete(obj)) {
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
		
		// 完成
		function complete(obj) {
			var vResult = formProxyComplete.validateAll();
			if (!vResult) {
				return false;
			}
			
			var doneCode = textGet("doneCode");
			var ajax = Ajax.post("/user/svc/pack/ticket/finish/do/-shop");
			ajax.params({
				ticketId : parseInt(obj.id),
				doneCode : doneCode
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					if (result.data == true) {
						Layer.msgSuccess("已完成");
						location.href = getAppUrl("/user/svc/pack/ticket/list/jsp/-shop");
					} else {
						Layer.warning(result.message);
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();

			return true;
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
				url : getAppUrl("/user/svc/pack/ticket/list/get/-shop"),
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
						if (rowObject.finished == false && rowObject.cancelled == false) {
							s += "<span> [<a class='item' href='javascript:void(0);' onclick='openCompleteDialog(" + JSON.stringify(rowObject) + ")' >确认完成</a>]";
						}
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