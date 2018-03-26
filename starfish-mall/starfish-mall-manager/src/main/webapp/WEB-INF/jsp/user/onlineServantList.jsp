<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	<title>在线客服</title>
</head>

<body id="rootPanel">
<!-- 查询条件 -->
<div class="ui-layout-north" style="padding: 4px;" id="topPanel">
	<div class="filter section">
		<div class="filter row">
			<div class="group right aligned">
				<label class="label">人员编号</label>
				<input class="input" id="servantId">
				<span class="spacer"></span>
				<label class="label">人员名称</label>
				<input class="input two wide" id="servantName">
				<span class="spacer"></span>
				<label class="label">会员名称</label>
				<input class="input two wide" id="memberName">
				<span class="spacer"></span>
				<button class="button" id="btnQuery" >查询</button>
			</div>
		</div>
	</div>
</div>

<!-- grid数据及分页 -->
<div class="ui-layout-center" style="padding: 4px;" id="mainPanel">
	<table id="svntRecList"></table>
	<div id="svntRecPager"></div>
</div>

<!-- 页面弹出框，涉及查看、添加、修改 -->
<div id="svntRecDialog">
	<div class="form">
		<div class="field row" style="height:500px;" id="recordsDiv">
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
	// 商品规格Grid
	var jqGridCtrl = null;
	// 缓存当前jqGrid数据行数组
	var gridHelper = JqGridHelper.newOne("");
	var svntRecDialog = null;
	
	// 初始化商品规格数据
	function loadGridData() {
		$("#servantId").val('');
		$("#servantName").val('');
		$("#memberName").val('');
		jqGridCtrl = $("#svntRecList").jqGrid({
			height : "100%",
			width : "100%",
			url : getAppUrl("/user/servant/list/get"),
			contentType : 'application/json',
			mtype : "post",
			datatype : 'json',
			colNames : [ "id", "人员编号", "人员名称", "会员名称", "消息记录", "时间", "操作" ],
			colModel : [ {
					name : "id",
					key : true,
					align : "center"
				}, {
					name : "servantId",
					width : 200,
					align : "right"
				}, {
					name : "servantName",
					width : 200,
					align : "center"
				}, {
					name : "memberName",
					width : 200,
					align : "center"
				}, {
					name : "content",
					width : 200,
					align : "left"
				}, {
					name : "sendTime",
					width : 200,
					align : "center"
				}, {
					name : 'memberId',
					formatter : function(cellValue, option, rowObject) {
						return "<span><a href='javascript:void(0);' onclick='goView("
								+ JSON.stringify(cellValue)
								+ ")' >查看</a></span>";
					},
					width : 200,
					align : "center"
				} ],
			multiselect : true, // 定义是否可以多选
			pager : "#svntRecPager", // 分页div
			loadComplete : function(gridData) { // JqGridHelper缓存最新的grid数据
				gridHelper.cacheData(gridData);
				var callback = getCallbackAfterGridLoaded();
				if (isFunction(callback)) {
					callback();
				}
			}
		});
	}

	// 查询
	function queryData() {
		var servantId = $id("servantId").val();
		var servantName = $id("servantName").val();
		var memberName = $id("memberName").val();
		jqGridCtrl.jqGrid("setGridParam", {
			postData : {
				filterStr : JSON.encode({
					"servantId" : servantId,
					"servantName" : servantName,
					"memberName" : memberName
				}, true)
			}
		}).trigger("reloadGrid");
	}
	
	// 空函数，在弹出框消失后重写调用
	function getCallbackAfterGridLoaded() {
	}
	
	// 
	function goView(memberId){
		var ajax = Ajax.post("/user/servant/member/get");
		ajax.data({memberId : memberId});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				$id("recordsDiv").html(laytpl($id("svntRecTpl").html()).render(result.data));
				initViewDialog();
				svntRecDialog.dialog("open");
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	
	// 
	function initViewDialog(){
		svntRecDialog = $("#svntRecDialog").dialog({
			autoOpen : false,
			title : "消息记录",
			height : Math.min(450, $window.height()),
			width : Math.min(500, $window.width()),
			modal : true,
			buttons : {
				"关闭" : function() {
					svntRecDialog.dialog("close");
				}
			},
			close : function() {
			}
		});
	}
	
	// 自适应界面大小
	function adjustCtrlsSize() {
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", $("#gbox_svntRecList")).height();
		var pagerHeight = $id("svntRecPager").height();
		jqGridCtrl.setGridWidth(mainWidth - 1);
		jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3);
	}

	// 初始化页面
	$(function() {
		// 页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 60,
			allowTopResize : false
		});

		// 隐藏布局north分割线
		$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
		
		// 加载商品规格列表数据
		loadGridData();

		$id("btnQuery").click(queryData);
		
		initViewDialog();
		
		// 页面自适应
		winSizeMonitor.start(adjustCtrlsSize);
	});
</script>
</body>
<script id="svntRecTpl" type="text/html" title="客服会员对话记录">
{{# for(var i = 0, len = d.length; i < len; i++){ }}
	{{# if(d[i].directFlag == 1){  }} <!-- 1 表示接收会员消息, 2 表示发送消息给会员 -->
		<div class="field row">
			<label style="color: back;">{{ d[i].memberName }}&nbsp;&nbsp;{{ d[i].sendTime }}</label>
		</div>
	{{# } else if(d[i].directFlag == 2){ }}
		<div class="field row">
			<label style="color: back;">{{ d[i].servantName }}&nbsp;&nbsp;{{ d[i].sendTime }}</label>
		</div>
	{{# } }}
	<div class="field row auto height">
		&nbsp;&nbsp;<label style="color: gray;">{{ d[i].content }}</label>
	</div>
{{# } }}
</script>
</html>
