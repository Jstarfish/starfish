<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>合作店订单列表</title>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<button id="batchSettleBtn" class="button">批量结算</button>
				<div class="right aligned group">
					<label class="label">合作店名称</label>
						<input id="queryDistShopName" class="input" /> <span class="spacer"></span>
					<label class="label">结算状态</label>
						<select id="querySettleState" class="input">
							<option value="">全部</option>
							<option value="unsettle">未结算</option>
							<option value="settle">已结算</option>
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
		<div class="form">
			<div class="field row">
				<label class="field label">应结金额：</label> 
				<input type="text" id="factAmount" disabled="true" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">实结金额：</label> 
				<input type="text" id="settleAumont" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">结算日期：</label> 
				<input type="text" id="settleDate" class="field value one half wide" />
			</div>
			<div class="field row" style="height: 60px;">
				<label class="field label" style="vertical-align: middle;">结算备注：</label>
				<textarea id="settleDesc" class="field value three wide" style="height: 100px;"></textarea>
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
			id : "querySettleState",
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
		
		var formProxySettle = FormProxy.newOne();
		formProxySettle.addField({
			id : "settleAumont",
			required : true,
			rules : [ "isNum" ]
		});
		formProxySettle.addField({
			id : "settleDesc",
			required : true,
			rules : [ "maxLength[300]" ]
		});
		formProxySettle.addField({
			id : "settleDate",
			type : "datetime",
			required : true,
			rules : [ "isDate" , "rangeDate[2015-01-01 00:00,2115-01-01 00:00]" ]
		});
		
		//-------------------------------------------------页面加载-------------------------------------------------------
		$(function() {
			// 调整页面布局
			adjustLayoutSize();
			// 初始化日期控件
			initDateTimePicker();
			// 绑定查询
			$id("btnQuery").click(refreshData);
			// 绑定批量结算
			$id("batchSettleBtn").click(openBatchSettleDialog);
			// 加载数据
			loadData();
			// 初始化结算页面
			initShowDialog();
			// 调整控件布局
			winSizeMonitor.start(adjustCtrlsSize);
		});
		
		//-------------------------------------------------业务处理-------------------------------------------------------
		
		// 批量结算打开页面
		function openBatchSettleDialog(){
			factAmount = 0;
			orderIds = [];
			var distIds = [];
			var ids=jqGridCtrl.jqGrid("getGridParam","selarrrow");
			if(!ids || ids.length == 0){
				Layer.msgWarning("请选择要结算的订单");
				return;
			}
			for(var i=0; i<ids.length; i++){
				var rowData = jqGridHelper.getRowData(ids[i]);
				if(rowData.settleRecId2Dist != null && rowData.settleRecId2Dist != ""){
					Layer.msgWarning("请选择未结算的合作店结算");
					return;
				}
				orderIds = orderIds + rowData.id + ",";
				distIds.add(rowData.distributorId);
				factAmount = factAmount + rowData.distProfit;
			}
			if(isEqual(distIds)){
				orderIds = orderIds.substring(0, orderIds.length-1);
				$id("factAmount").val(factAmount);
				showDialog.dialog("open");
			}else{
				Layer.msgWarning("请选择同一的合作店结算");
				return;
			}
		}
		
		// 结算打开页面
		function openSettleDialog(rowObject){
			factAmount = rowObject.distProfit;
			orderIds = rowObject.id;
			$id("factAmount").val(factAmount);
			showDialog.dialog("open");
		}
		
		/* // 已结算打开页面
		function openSettleInfoDialog(rowObject){
			$id("factAmount").val(rowObject.factAmount);
			$id("settleAumont").val(factAmount);
			$id("settleDate").val(factAmount);
			$id("settleDesc").val(factAmount);
		} */
		
		// 批量结算操作
		function batchSettleDistOrder(){
			var vldResult1 = formProxySettle.validate("settleAumont");
			var vldResult2 = formProxySettle.validate("settleDesc");
			var vldResult3 = formProxySettle.validate("settleDate");
			if( vldResult1 && vldResult2 && vldResult3 ){
				var settleDesc = formProxySettle.getValue("settleDesc");
				var settleAumont = formProxySettle.getValue("settleAumont");
				var settleDate = formProxySettle.getValue("settleDate");
				var hintBox = Layer.progress("正在结算...");
				var ajax = Ajax.post("/distshop/order/batch/settle/do");
				ajax.params({
					orderIds: orderIds,
					settleDate: settleDate,
					amount: factAmount,
					theAmount: settleAumont,
					desc: settleDesc
				});
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						refreshData();
						Layer.msgSuccess(result.message);
						showDialog.dialog("close");
					} else {
						Layer.msgWarning(result.message);
					}
				});
				ajax.always(function() {
					hintBox.hide();
				});
				ajax.go();
			}
		}
		// 判断是否是同一合作店
		function isEqual(ids){
			var id = 0;
			for(var i=0; i<ids.length; i++){
				if(i == 0){
					id = ids[i];
				}
				if(i != 0 && id != ids[i]){
					return false;
				}
			}
			return true;
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
		
		//-------------------------------------------------初始数据-------------------------------------------------------
		
		// 加载数据
		function loadData(){
			jqGridCtrl = $("#theGridCtrl").jqGrid({
				url : getAppUrl("/distshop/order/settle/list/get"),
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
				colNames : [ "id", "订单编号", "合作店名称", "订单状态", "订单类型", "订单利润", "完成时间", "结算状态", "关联结算单", "结算时间", "操作" ],
				colModel : [ {
					name : "id",
					index : "id",
					hidden : true
				}, {
					name : "no",
					index : "no",
					width : 120,
					align : 'center'
				}, {
					name : "distShopName",
					index : "distShopName",
					width : 120,
					align : 'center'
				}, {
					name : "orderState",
					index : "orderState",
					formatter :  function(cellValue, option, rowObject) {
						return getOrderState(rowObject);
					},
					width : 100,
					align : 'center'
				}, {
					name : "creatorFlag",
					index : "creatorFlag",
					formatter :  function(cellValue, option, rowObject) {
						if(rowObject.creatorFlag == 2){
							return "代理订单";
						}else{
							return "承接订单";
						}
					},
					width : 100,
					align : "center"
				}, {
					name : "distProfit",
					index : "distProfit",
					width : 100,
					align : 'right'
				}, {
					name : "finishTime",
					index : "finishTime",
					width : 140,
					align : 'center'
				}, {
					name : "settleRecId2Dist",
					index : "settleRecId2Dist",
					formatter : function(cellValue, option, rowObject) {
						if (rowObject.settleRecId2Dist != null) {
							return "已结算";
						} else {
							return "<span style='color: blue'>未结算</span>";
						}
					},
					width : 100,
					align : 'center'
				}, {
					name : "settleRecId2Dist",
					index : "settleRecId2Dist",
					width : 100,
					align : 'center'
				}, {
					name : "distSettleRec.theTime",
					index : "distSettleRec.theTime",
					width : 140,
					align : "center"
				}, {
					name : "id",
					index : "id",
					formatter : function(cellValue, option, rowObject) {
						if (rowObject.settleRecId2Dist != null) {
							return "";
						} else {
							return "<span> [<a class='item' href='javascript:void(0);' onclick='openSettleDialog(" + JSON.stringify(rowObject) + ")' >结算</a>]";
						}
					},
					width : 100,
					align : "center"
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
					alert(rowId);
					var userMap = jqGridHelper.getRowData(rowId)
					openShowDialog(userMap);
				},
				multiselect:true,
				multikey:'ctrlKey'
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
			var distIfSettle = formProxyQuery.getValue("querySettleState");
			var fromDate = formProxyQuery.getValue("fromDate");
			var toDate = formProxyQuery.getValue("toDate");
			if(distShopName){
				filter.distShopName = distShopName;
			}
			if(distIfSettle){
				filter.distIfSettle = distIfSettle;
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
		
		// 初始化结算页面
		function initShowDialog(obj) {
			showDialog = $("#saleOrderDialog").dialog({
				autoOpen : false,
				height : Math.min(550, $(window).height()),
				width : Math.min(600, $(window).width()),
				modal : true,
				title : '查看销售订单详情',
				buttons : {
					"确定" : function() {
						batchSettleDistOrder();
					},
					"取消" : function() {
						showDialog.dialog("close");
					}
				},
				close : function() {
					$id("settleAumont").val("");
					$id("settleDesc").val("");
					var settleDate = new Date().format("yyyy/MM/dd HH:mm");
					$id("settleDate").val(settleDate);
				}
			});
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
			
			var settleDate = new Date().format("yyyy/MM/dd HH:mm");
			$id("settleDate").datetimepicker({
				lang : 'ch'
			});
			$id("settleDate").val(settleDate);
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