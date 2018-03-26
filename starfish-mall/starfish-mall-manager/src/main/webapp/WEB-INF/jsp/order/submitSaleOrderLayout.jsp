<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>代理下单</title>
<style>
	.ui-layout-pane {
		background-color:#EEE;
		border-width : 0;
	}

</style>
</head>

<body id="rootPanel">
	<div id="leftPanel" class="ui-layout-west" style="padding: 0;">
		<div id="leftPanelTop" class="ui-layout-north" style="padding: 4px;line-height:22px;">
			服務列表
		</div>
		<div id="leftPanelMain" class="ui-layout-center" style="padding: 2px;">
			<table id="theGridCtrl"></table>
			<div id="theGridPager"></div>
		</div>
	</div>
	<div id="rightPanel" class="ui-layout-center" style="padding: 0;">
		<div id="rightPanelTop" class="ui-layout-north" style="padding: 4px;line-height:22px;">
			訂單信息
		</div>
		<div id="rightPanelMain" class="ui-layout-center" style="padding: 4px;background-color:#FFFFFF;">
			<!-- Grid view --> 這裡是表單
			
			
		</div>
		<div id="rightPanelBottom" class="ui-layout-south" style="padding: 4px;line-height:52px;">
			<div style="width:100%;height:100%;text-align:center;vertical-align:middle;">
				<button class="normal button">&lt;&nbsp;&nbsp;返回</button> <span class="normal spacer four wide"></span><button class="normal button two wide">提交</button>
			</div>
		</div>			
	</div>

	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
	// iconClass : info, warning, error
	//緩存變量
	var jqGridCtrl;
	//缓存当前jqGrid数据行数组
	var gridHelper = JqGridHelper.newOne();
	//调整控件大小
	function adjustCtrlsSize(winWidth, winHeight) {
		var jqMainPanel = $id("leftPanelMain");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		console.log("mainWidth:" + mainWidth + ", " + "mainHeight:" + mainHeight);
		//
		var gridCtrlId = "theGridCtrl";
		var jqGridBox = $("#gbox_" + gridCtrlId);
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
		var pagerHeight = $id("theGridPager").height();
		jqGridCtrl.setGridWidth(mainWidth - 1);
		jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3);
		//
		hideLayoutTogglers();
	}
	$(function() {
		// 页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			west__size : "40%",
			allowLeftResize : false
		});
		$id('leftPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 30,
			allowTopResize : false
		});
		
		$id('rightPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 30,
			south__size : 60,
			allowTopResize : false
		});
		//
		hideLayoutTogglers();
		//
		//初始化数据表格
		jqGridCtrl = $("#theGridCtrl").jqGrid({
			height : "100%",
			width : "100%",
			pager : "#theGridPager",
			//multiselect : true,
			colModel : [{
				label : '用户id',
				name : 'userId',
				width : 100,
				key : true
			}, {
				label : '用户名',
				name : 'userName',
				width : 100
			}, {
				label : '消费日期',
				name : 'date',
				width : 100,
				align : 'center'
			}, {
				label : '消费金额',
				name : 'money',
				width : 80,
				align : 'right'
			}, {
				label : '用途',
				name : 'usage',
				width : 280
			}],
			loadComplete : function(gridData) {
				gridHelper.cacheData(gridData);
			}
		});
		//
		jqGridCtrl.bindKeys();
		//
		winSizeMonitor.start(adjustCtrlsSize);
	});
	
	// ------------------------初始化-------------------------
	</script>
</body>
</html>