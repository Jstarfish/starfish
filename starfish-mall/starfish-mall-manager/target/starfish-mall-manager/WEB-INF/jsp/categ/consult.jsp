<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>商品咨询</title>
<style type="text/css">

</style>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group right aligned">
					<label class="label">咨询商品</label>
					<input id="q_goodsName" class="input" /> 
					<span class="spacer"></span>
					<label class="label">咨询状态</label>
					<select id="q_replyFlag" class="normal input">
						<option value="-1">全部</option>
						<option value="0">未回复</option>
						<option value="1">已回复</option>
					</select>
					<span class="spacer"></span>
					<label class="label">咨询日期</label>
					<input id="q_dateBegin" class="input" />—— 
					<input id="q_dateEnd" class="input" /> 
					<span class="spacer"></span> 
					<span class="vt divider"></span>
					<button id="queryBtn" class="button">查询</button>
				</div>
			</div>

		</div>
		
		<table id="inquiry_list"></table>
		<div id="inquiry_pager"></div>
	</div>
	
	<div id="dialog" style="display: none;"></div>
		
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
	
	var record_dialog;
	
	$(function() {
		//绑定日期控件
		$id("q_dateBegin").datepicker();
		$id("q_dateEnd").datepicker();
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
			
			var goodsName = $id("q_goodsName").val().trim();
			if(goodsName){
				filter.goods.name = goodsName;
			}
			
			var status = intGet("q_replyFlag");
			if(status == 0){
				filter.replyFlag = false;
			}else if(status == 1){
				filter.replyFlag = true;
			}
			
			var dateBegin = $id("q_dateBegin").val().trim();
			if(dateBegin){
				filter.dateBegin = dateBegin;
			}
			
			var dateEnd = $id("q_dateEnd").val().trim();
			if(dateEnd){
				filter.dateEnd = dateEnd;
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
		var gridCtrlId = "inquiry_list";
		var jqGridBox = $("#gbox_" + gridCtrlId);
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
		var pagerHeight = $id("inquiry_pager").height();
		jqGridCtrl.setGridWidth(mainWidth - 1);
		jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 90);
	}
	
	//-------------------------------------------------功能实现 --------------------------------------------------------------
	
	function goView(id){
		//
		initViewDialog();
		showInquiryData(id);
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
	
	function showInquiryData(id){
		var inquiryMap = gridHelper.getRowData(id);
		var htmlStr = laytpl($id("viewInquiryTpl").html()).render(inquiryMap);
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
		jqGridCtrl= $("#inquiry_list").jqGrid({
		      url : getAppUrl("/categ/consult/list"),  
		      contentType : 'application/json',  
		      mtype : "post",  
		      datatype : 'json',
		      height : "100%",
			  width : "100%",
		      colNames : ["咨询商品","咨询内容","咨询人","咨询时间","联系电话","咨询状态", "操作"],  
		      colModel : [{name:"goods.name", index:"goods.name",width:80,align : 'left',},
		                  {name:"content", index:"content",width:80,align : 'left'},
		                  {name:"member.user.nickname", index:"member.user.nickname",width:80,align : 'right'},
		                  {name:"ts", index:"ts",width:80,align : "right"},
		                  {name:"member.user.phoneNo", index:"member.user.phoneNo",width:80,align : 'right'},
						  {name:"replyFlag", index:"replyFlag", width:300, align : "left", formatter : function (cellValue) {
							  if (cellValue == 0) {
									return '未回复';
								}else {
									return '已回复';
								}
							}},
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
			pager : "#inquiry_pager",
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

<script id="viewInquiryTpl" type="text/html" title="查看客服历史模板">
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
