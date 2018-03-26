<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>客服历史</title>
<style type="text/css">

</style>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group right aligned">
					<label class="label">用户编号</label>
					<input id="q_servantId" class="input" /> 
					<span class="spacer"></span>
					<label class="label">用户名称</label>
					<input id="q_servantName" class="input" /> 
					<span class="spacer"></span>
					<label class="label">会员编号</label>
					<input id="q_memberId" class="input" /> 
					<span class="spacer"></span>
					<label class="label">会员名称</label>
					<input id="q_memberName" class="input" /> 
					<span class="spacer"></span> 
					<label class="label">日期</label>
					<input id="q_dateStr" class="input" /> 
					<span class="spacer"></span> 
					<span class="vt divider"></span>
					<button id="queryBtn" class="button">查询</button>
				</div>
			</div>

		</div>
		
		<table id="member_list"></table>
		<div id="member_pager"></div>
	</div>
	
	<div id="dialog" style="display: none;"></div>
		
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
	
	var record_dialog;
	
	$(function() {
		$id("q_dateStr").datepicker();
		//页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 160,
			allowTopResize : false
		});
		
		//加载初始化数据
		loadData();
		//调整控件大小
		winSizeMonitor.start(adjustCtrlsSize);
		
		$id("queryBtn").click(function(){
			//加载jqGridCtrl
			var filter = {};
			
			var servantId = intGet("q_servantId");
			if(servantId){
				filter.servantId = servantId;
			}
			
			var servantName = $id("q_servantName").val().trim();
			if(servantName){
				filter.servantName = servantName;
			}
			
			var memberId = intGet("q_memberId");
			if(memberId){
				filter.memberId = memberId;
			}
			
			var memberName = $id("q_memberName").val().trim();
			if(memberName){
				filter.memberName = memberName;
			}
			
			var dateStr = $id("q_dateStr").val().trim();
			if(dateStr){
				filter.dateStr = dateStr;
			}
			//
			jqGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			
			getCallbackAfterGridLoaded = function(){
				
			};
		});
		
	});
	
	//-------------------------------------------------调整控件大小------------------------------------------------------------
	//调整控件大小
	function adjustCtrlsSize() {
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		var gridCtrlId = "member_list";
		var jqGridBox = $("#gbox_" + gridCtrlId);
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
		var pagerHeight = $id("member_pager").height();
		jqGridCtrl.setGridWidth(mainWidth - 1);
		jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 90);
	}
	
	//-------------------------------------------------功能实现 --------------------------------------------------------------
	
	function goView(id){
		//
		initViewDialog();
		showRecordData(id);
		record_dialog.dialog("open");
	}
	
	function initViewDialog(){
		//
		record_dialog = $("#dialog").dialog({
			autoOpen : false,
			height : Math.min(400, $window.height()),
			width : Math.min(750, $window.width()),
			modal : true,
			title : '查看客服历史',
			buttons : {
				"关闭" : function() {
					record_dialog.dialog("close");
				}
			},
		});
	}
	
	function showRecordData(id){
		var recordMap = gridHelper.getRowData(id);
		var htmlStr = laytpl($id("viewRecordTpl").html()).render(recordMap);
		$id("dialog").html(htmlStr);
	}
	
	function getCallbackAfterGridLoaded(){
		
	}
	
	
	//jqGrid缓存变量
	var jqGridCtrl = null;
	//
	var gridHelper = JqGridHelper.newOne("");
	
	function loadData(){
		//
		jqGridCtrl= $("#member_list").jqGrid({
		      url : getAppUrl("/interact/service/record/list"),  
		      contentType : 'application/json',  
		      mtype : "post",  
		      datatype : 'json',
		      height : "100%",
			  width : "100%",
		      colNames : ["所属范围编号","所属范围名称","用户编号","用户名称","会员编号","会员名称","消息内容","日期", "操作"],  
		      colModel : [{name:"entityId", index:"entityId",width:80,align : 'left',},
		                  {name:"entityName", index:"entityName",width:80,align : 'left'},
		                  {name:"servantId", index:"servantId",width:80,align : 'right'},
		                  {name:"servantName", index:"servantName",width:80,align : "right"},
		                  {name:"memberId", index:"memberId",width:80,align : 'right'},
		                  {name:"memberName", index:"memberName",width:80,align : "right"},
						  {name:"content", index:"content", width:300, align : "left", formatter : function (cellValue) {
								return '<div class="auto height">'+cellValue+'</div>';
							}},
						  {name:"dateStr", index:"dateStr",width:110,align : "right"},
		                  {name:'id',index:'id', formatter : function (cellValue,option,rowObject) {
							return "<span> [<a class='item' href='javascript:void(0);' onclick='goView("
							+ cellValue
							+ ")' >查看</a>] ";
						},
					width:150,align:"center"}
		                  ],  
			//rowList:[10,20,30],
			multiselect:true,
			multikey:'ctrlKey',
			pager : "#member_pager",
			loadComplete : function(gridData){
				gridHelper.cacheData(gridData);
				var callback = getCallbackAfterGridLoaded();
				if(isFunction(callback)){
					callback();
				}

			},
			ondblClickRow: function(rowId) {
				goView(rowId);
			}
		});

	}
	//
	
	</script>
</body>

<script id="viewRecordTpl" type="text/html" title="查看客服历史模板">
<div class='form'>

	<div class='field row'>
		<div class='field group'>
			<label class='field label one half wide'>所属范围编号</label>
			<input type='text' id='entityId' value='{{ d.entityId }}' disabled class='field value' />
		</div>
		<span style='width: 80px;' class='normal spacer'></span>
		<div class='field group'>
			<label class='field label one half wide'>所属范围名称</label>
			<input type='text' id='entityName' value='{{ d.entityName }}' disabled class='field value' />
		</div>
	</div>
	
	<div class='field row'>
		<div class='field group'>
			<label class='field label one half wide'>用户编号</label>
			<input type='text' id='entityId' value='{{ d.servantId }}' disabled class='field value' />
		</div>
		<span style='width: 80px;' class='normal spacer'></span>
		<div class='field group'>
			<label class='field label one half wide'>用户名称</label>
			<input type='text' id='entityName' value='{{ d.servantName }}' disabled class='field value' />
		</div>
	</div>

	<div class='field row'>
		<div class='field group'>
			<label class='field label one half wide'>会员编号</label>
			<input type='text' id='entityId' value='{{ d.memberId }}' disabled class='field value' />
		</div>
		<span style='width: 80px;' class='normal spacer'></span>
		<div class='field group'>
			<label class='field label one half wide'>会员名称</label>
			<input type='text' id='entityName' value='{{ d.memberName }}' disabled class='field value' />
		</div>
	</div>
	
	<div class='field row'>
		<div class='field group'>
			<label class='field label one half wide'>消息类型</label>
			{{# if( d.directFlag ){ }}
				<input id='directFlag-T' value='接收的消息' disabled class='field value' type='text'/>
			{{# }else{ }}
				<input id='directFlag-F' value='发送的消息' disabled class='field value' type='text'/>
			{{# } }}
		</div>
		<span style='width: 80px;' class='normal spacer'></span>
		<div class='field group'>
			{{# if( d.directFlag ){ }}
				<label class='field label one half wide'>接收消息时间</label>
				<input id='directFlag-T' value='recvTime' disabled class='field value' type='text'/>
			{{# }else{ }}
				<label class='field label one half wide'>发送消息时间</label>
				<input id='directFlag-F' value='sendTime' disabled class='field value' type='text'/>
			{{# } }}
		</div>
	</div>

	<div class='field row'>
		<div class='field group'>
			<label class='field label one half wide'>内容</label>
			<textarea id='content' disabled style='height:120px;color:#000080;width:450px; ' class='field value three half wide'>{{ d.content }}</textarea>
		</div>
	</div>

</div>
</script>


</html>
