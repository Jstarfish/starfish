<%@page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>促销标签列表</title>
<style type="text/css">

</style>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<button id="addBtn" style="display: none;" class="button">添加</button>
				<div class="right aligned group">
					<label class="label">标签名称</label>
					<input id="q_name" class="input wide" /> 
					<span class="spacer"></span>
					<button id="queryBtn" class="button">查询</button>
				</div>
			</div>
		</div>
		
		<table id="prmtTag_list"></table>
		<div id="prmtTag_pager"></div>
	</div>
	
	<div id="dialog" style="display: none;"></div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
	var jqGridCtrl = null;
	//
	var currentSvcId = null;
	
	//------------------------------------------初始化配置---------------------------------
	$(function() {
		//页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			allowTopResize : false
		});
		//
		var dlgArg = getDlgArgForMe();
		currentSvcId = dlgArg|| null;
		//
		loadPrmtTagData();
		//
		$id("queryBtn").click(queryPrmtTags);
		// 页面自适应
		winSizeMonitor.start(adjustCtrlsSize);
	});
	
	//调整控件大小
	function adjustCtrlsSize() {
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		var gridCtrlId = "prmtTag_list";
		var jqGridBox = $("#gbox_" + gridCtrlId);
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
		var pagerHeight = $id("prmtTag_pager").height();
		jqGridCtrl.setGridWidth(mainWidth - 1 - 4);
		jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 54);
	}
	
	//-----------------------------业务处理------------------------------------------------
	
	//
	function deletePrmtTag(tagId, currentSvcId){
		var hintBox = Layer.progress("正在保存数据...");
		//
		var postData = { tagId:tagId, svcId:currentSvcId };
	  	var ajax = Ajax.post("/carSvc/prmtTag/delete/do");
	  	    ajax.data(postData);
		    ajax.done(function(result, jqXhr){
				if (result.type == "info") {
					Toast.show(result.message);
					queryPrmtTags();
				} else {
					Layer.msgWarning(result.message);
				}
		 });
		//
	    ajax.always(function() {
			hintBox.hide();
		});
		//
		ajax.go();
	}
	
	//
	function goDeletePrmtTag(tagId){
		if(!currentSvcId){
			Layer.msgWarning("当前服务信息丢失");
			return;
		} 
		//
		var theLayer ={};
		theLayer= Layer.confirm('确定要删除该促销标签吗？', function(){
			theLayer.hide();
			deletePrmtTag(tagId, currentSvcId);
		});
	}
	
	//
	function addPrmtTag(tagId, currentSvcId){
		var hintBox = Layer.progress("正在保存数据...");

		var ajax = Ajax.post("/carSvc/prmtTag/add/do");
		//
		ajax.data({svcId:currentSvcId, prmtTagId:tagId});

		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				Toast.show(result.message);
				queryPrmtTags();
			} else {
				Layer.msgWarning(result.message);
			}

		});
		ajax.always(function() {
			hintBox.hide();
		});
		
		ajax.go();
	}
	
	//
	function goAddPrmtTag(tagId){
		if(!currentSvcId){
			Layer.msgWarning("当前服务信息丢失");
			return;
		} 
		//
		addPrmtTag(tagId, currentSvcId);
	}
	
	//
	function queryPrmtTags(){
		var filter = {};
		var q_name = textGet("q_name");
		if(q_name){
			filter.name = q_name;
		}
		//
		filter.svcId = currentSvcId;
		//
		jqGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
	}
	
	function loadPrmtTagData(){
		var filter = {};
		var q_name = textGet("q_name");
		if(q_name){
			filter.name = q_name;
		}
		//
		filter.svcId = currentSvcId;
		//
		jqGridCtrl= $("#prmtTag_list").jqGrid({
		      url : getAppUrl("/market/prmtTag/list/get/by/filter"),  
		      contentType : 'application/json',  
		      mtype : "post",  
		      datatype : 'json',
		      postData:{filterStr : JSON.encode(filter,true)},
		      height : "100%",
			  width : "100%",
			  colNames : ["标签代码", "标签名称", "描述", "图标", "是否已使用", " 操作" ], 
			  colModel : [
							{
								name : "code",
								index : "code",
								width : 80,
								align : 'left'
							},
							{
								name : "name",
								index : "name",
								width : 100,
								align : 'left'
							},
							{
								name : "desc",
								index : "desc",
								width : 200,
								align : 'left',formatter : function (cellValue) {
				                	 return "<div class='auto height'>"+cellValue+"</div>";
								}
							},
							{
								name : "icon",
								index : "icon",
								width : 70,
								align : 'right'
							},
							{
								name : "existFlag",
								index : "existFlag",
								width : 50,
								align : 'center',
								formatter : function(cellValue, option, rowObject) {
									 var existFlag = rowObject.existFlag;
									 //
									 if(existFlag){
				                		 return "<span title='已使用'>√</span>";
				                	 }else{
				                		 return "";
				                	 }
								}
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									 var existFlag = rowObject.existFlag;
									 //
									 if(existFlag){
										 return "<span> [<a class='item' href='javascript:void(0);' onclick='goDeletePrmtTag("
											+ cellValue
											+ ")' >删除</a>]";
											+ "</span>";
				                	 }else{
				                		 return "<span> [<a class='item' href='javascript:void(0);' onclick='goAddPrmtTag("
											+ cellValue
											+ ")' >添加</a>]";
											+ "</span>";
				                	 }
								},
								width : 50,
								align : "center"
							} ],  
			//rowList:[10,20,30],
			multiselect:false,
			multikey:'ctrlKey',
			pager : "#prmtTag_pager"
		});
	}
	</script>
</body>


</html>
