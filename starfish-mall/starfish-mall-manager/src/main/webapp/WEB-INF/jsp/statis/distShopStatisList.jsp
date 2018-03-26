<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>卫星店铺业绩统计</title>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="right aligned group">
					<label class="label">店铺名称</label>
						<input id="queryName" class="input" /> <span class="spacer"></span>
					<label class="label">所在地区</label>
						<input id="queryRegionName" class="input" /> <span class="spacer"></span>
					<label class="label">日期</label> 
						<input class="input" id="fromDate" /> 至 <input class="input" id="toDate" />
						<span class="spacer two wide"></span>
					<button id="btnQuery" class="button">查询</button>
				</div>
			</div>
		</div>
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>

	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		//-------------------------------------------------全局变量-------------------------------------------------------
		var jqGridCtrl = null;
		var jqGridHelper = JqGridHelper.newOne("");
		var formProxyQuery = FormProxy.newOne();
		formProxyQuery.addField({
			id : "queryRegionName",
			type : "text"
		});
		formProxyQuery.addField({
			id : "queryName",
			type : "text",
			rules : [ "maxLength[30]" ]
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
		
		//-------------------------------------------------业务处理-------------------------------------------------------
		
		
		//-------------------------------------------------初始数据-------------------------------------------------------
		
		// 加载数据
		function loadData(){
			jqGridCtrl = $("#theGridCtrl").jqGrid({
				url : getAppUrl("/distshop/achievement/list/get"),
				contentType : 'application/json',
				mtype : "post",
				datatype : 'json',
				height : "100%",
				width : "100%",
				postData : {
					filterStr : JSON.encode({
						name : formProxyQuery.getValue("queryName"),
						regionName : formProxyQuery.getValue("queryRegionName"),
						fromDate : formProxyQuery.getValue("fromDate"),
						toDate : formProxyQuery.getValue("toDate")
					}, true)
				},
				colNames : [ "id", "店铺名称", "所在地区", "代下单数", "承接单数", "收入金额", "访客数" ],
				colModel : [ {
					name : "id",
					index : "id",
					hidden : true
				}, {
					name : "name",
					index : "name",
					width : 100,
					align : 'center'
				}, {
					name : "regionName",
					index : "regionName",
					width : 200,
					align : 'center'
				}, {
					name : "agentCount",
					index : "agentCount",
					width : 100,
					align : 'right'
				}, {
					name : "allocateCount",
					index : "allocateCount",
					width : 100,
					align : 'right'
				}, {
					name : "incomeAmount",
					index : "incomeAmount",
					width : 100,
					align : 'right',
					formatter : function(cellValue, option, rowObject) {
						cellValue = cellValue || 0;
						return cellValue.toFixed(2);
					}
				}, {
					name : "visitorCount",
					index : "visitorCount",
					width : 100,
					align : 'right'
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
					var statisMap = jqGridHelper.getRowData(rowId)
				}
			});
		}

		// 刷新数据
		function refreshData() {
			var vldResult = formProxyQuery.validateAll();
			if (!vldResult) {
				return;
			}
			jqGridCtrl.jqGrid("setGridParam", {
				postData : {
					filterStr : JSON.encode({
						name : formProxyQuery.getValue("queryName"),
						regionName : formProxyQuery.getValue("queryRegionName"),
						fromDate : formProxyQuery.getValue("fromDate"),
						toDate : formProxyQuery.getValue("toDate")
					}, true)
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
</html>