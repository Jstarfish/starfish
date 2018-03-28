<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	<link rel="stylesheet" href="<%=resBaseUrl%>/lib/colorpicker/evol.colorpicker.css" />
	<title>颜色定义</title>
	<style type="text/css">
		div.colorBlock{
			height:20px;
			width:30px;
			border: 1px solid #c3c3c3;
		}
	</style>
</head>
<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding:4px;vertical-align: bottom;">
		<div class="filter section" >
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnAddColorDef" class="normal button">添加</button>
				</div>
				
				<div class="group right aligned">
					<label class="normal label">颜色名称</label>
				    <input id="queryColorDefName" class="normal input" />
					<span class="normal spacer"></span>
					<button id="btnQueryColorDef" class="normal button">查询</button>
			    </div>
			</div>
		</div>
	</div>
	
	<div id="mainPanel" class="ui-layout-center" style="padding:4px;">
		<table id="attrRefList"></table>
		<div id="attrRefPager"></div>
	</div>
	
	<div id="dialog_ColorDef" style="display:none" >
		<div id="addForm" class="form">
			<div class="field row">
				<label class="field label required">颜色名称</label> 
				<input type="text" id="colorDefName" class="field one half value wide" />
				<input type="hidden" id="colorDefId"  />
			</div>
			<div class="field row" id="colordefDiv">
				<label class="field  label required">选择颜色</label> 
				<input id="colorDefExpr" class="field one half value wide" />
			</div>
			<div class="field row" id="colordefShowDiv" >
				<label class="field  label required">选择颜色</label> 
				<div  id="colorShow"  ></div>
			</div>
		</div>
	</div>
	
<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript" src="<%=resBaseUrl%>/lib/colorpicker/evol.colorpicker.js"></script>
<script type="text/javascript">
	//------------------------初始化变量-------------------------
    //jqgrid
	var colorDefGrid;
	//新增Dialog
	var colorDefDialogAdd;
	//查看Dialog
	var colorDefDialogView;
	//编辑Dialog
	var colorDefDialogEdit;
	//缓存当前jqGrid数据行数组
	var colorDefGridHelper = JqGridHelper.newOne("");
	var curAction;
	var nameFlag=true;
	var clorDefVName="";
	//------------------------验证编辑店铺评分等级-------------------------
	 //实例化编辑商铺评分代理
	var formProxyColorDef = FormProxy.newOne();
	//注册表单控件
	formProxyColorDef.addField({
		id : "colorDefName",
		required : true,
		rules : ["maxLength[30]",{
			rule : function(idOrName, type, rawValue, curData) {
				if (clorDefVName == "") {
					validateName(rawValue);
				} else {
					if (clorDefVName != rawValue) {
						validateName(rawValue);
					}
				}
				return nameFlag;
			},
			message : "名称被占用！"
		}]
	});
	formProxyColorDef.addField({
		id : "colorDefExpr",
		required : true,
		rules : [ "isHexColor"]
	});
	
	//------------------------加载数据-------------------------
	//加载数据列表
	function loadData() {
		colorDefGrid = $id("attrRefList").jqGrid({
			url : getAppUrl("/categ/colorDef/list/get"),
			contentType : 'application/json',
			mtype : "post",
			datatype : 'json',
			colNames : [ "id", "名称", "颜色expr", "颜色显示", "日期", "操作" ],
			colModel : [
					{
						name : "id",
						index : "id",
						hidden : true
					},
					{
						name : "name",
						width : 100,
						align : 'left',
					},
					{
						name : "expr",
						width : 100,
						align : 'left',
					},
					{
						name : "expr",
						width : 100,formatter : function (cellValue,option,rowObject) {
							return '<div class="colorBlock" style="background-color: '+cellValue+';"/>';
						},
						align : 'center'
							
					},
					{
						name : "ts",
						width : 150,
						align : 'left'
					},
					{
						name : 'id',
						index : 'id',
						formatter : function(cellValue,
								option, rowObject) {
							return "<span> [<a href='javascript:void(0);' onclick='colorDialogView("
									+ JSON.stringify(rowObject)
									+ ")' >查看</a>]   [<a href='javascript:void(0);' onclick='colorDefDialogEditOpen("
									+ JSON.stringify(rowObject)
									+ ")' >修改</a>]  [<a href='javascript:void(0);' onclick='goDeleteColorDef("
									+ cellValue
									+ ")' >删除</a>] ";
						},
						width : 150,
						align : "center"
					} ],
			//multiselect : true,
			pager : "#attrRefPager",
			height:"auto",
			loadComplete : function(gridData) { // JqGridHelper缓存最新的grid数据
				colorDefGridHelper.cacheData(gridData);
				var callback = getCallbackAfterGridLoaded();
				if (isFunction(callback)) {
					callback();
				}
			},
			ondblClickRow: function(rowId) {
				var userMap = colorDefGridHelper.getRowData(rowId)
				colorDialogView(userMap);
			}
		});
	}
	
	//空函数，在弹出框消失后重写调用
	function getCallbackAfterGridLoaded() {
	}
	
	//------------------------打开窗口方法-------------------------
	//增加定义颜色窗口打开
	function colorDefDialogAddOpen(){
		clearDialog();
		initDialogAdd();
		curAction = "add";
		colorDefDialogAdd.dialog("open");
	}
	
	//查看定义颜色窗口打开
	function colorDialogView(obj){
		clearDialog();
		curAction = "view";
		tempAttr = obj;
		$("#colorDefExpr").colorpicker({color: tempAttr.expr});
		$id("colorDefName").val(tempAttr.name),
		clorDefVName=tempAttr.name;
		$id("colorDefExpr").val(tempAttr.expr),
		$id("colordefDiv").hide();
		$id("colordefShowDiv").show();
		$id("colorShow").attr("style","float:left;background-color: "+tempAttr.expr+";height:30px;width:30px ;")
		initDialogView();	
		colorDefDialogView.dialog("open");
	}
	
	//编辑定义颜色窗口打开
	function colorDefDialogEditOpen(obj){
		clearDialog();
		tempAttr = obj;
		tempId = obj.id;
		curAction = "edit";
		initDialogEdit();
		//$("#colorDefExpr").val(tempAttr.expr),
		$("#colorDefExpr").colorpicker({color: tempAttr.expr});
		$id("colorDefId").val(tempAttr.id),
		$id("colorDefName").val(tempAttr.name),
		$id("colorDefExpr").val(tempAttr.expr),
		clorDefVName=tempAttr.name;
		colorDefDialogEdit.dialog("open");
	}
	
	//循环方法
	function defaultMethod(){
		if(curAction == "add"){
			addColorDef();
		}
		if(curAction == "edit"){
			updateColorDef();
		}
		if(curAction == "view"){
			colorDefDialogEditOpen(tempAttr);
		}
	}
	
	//清除Dialog
	function clearDialog(){
		$id("colorDefId").val("");
		$id("colorDefName").val("");
		$id("colorDefExpr").val("");
		clorDefVName="";
		nameFlag=true;
		$("input", "#dialog_ColorDef").removeAttr("disabled");	
		$("#colorDefExpr").colorpicker({color: ""});
		$id("colordefDiv").show();
		$id("colordefShowDiv").hide();
		formProxyColorDef.hideMessages();
	}
	
	//------------------------初始化Dialog-------------------------
	//初始化增加定义颜色窗口dialogAdd
	function initDialogAdd(){
		colorDefDialogAdd = $id("dialog_ColorDef").dialog({
			autoOpen : false,
			width : Math.min(450, $window.width()),
		    height : Math.min(500, $window.height()),
			modal : true,
			title:"添加颜色定义",
			buttons : {
				"保存" : defaultMethod,
				"取消" : function() {
					clearDialog();
					colorDefDialogAdd.dialog("close");
				}
			},
			close : function() {
				clearDialog();
			}
		});
	}
	
	//初始化查看定义颜色dialogView
	function initDialogView(){
		$("input", "#dialog_ColorDef").prop("disabled", true);
		colorDefDialogView = $id("dialog_ColorDef").dialog({
			autoOpen : false,
			width : Math.min(450, $window.width()),
		    height : Math.min(500, $window.height()),
			modal : true,
			title:"查看颜色定义",
			buttons : {
				"继续添加" : colorDefDialogAddOpen,
				"修改 >" : defaultMethod,
				"关闭" : function() {
					clearDialog();
					colorDefDialogView.dialog("close");
				}
			},
			close : function() {
				clearDialog();
			}
		});
	}
	
	//初始化编辑定义颜色dialogEdit
	function initDialogEdit(){
		colorDefDialogEdit = $id("dialog_ColorDef").dialog({
			autoOpen : false,
			width : Math.min(450, $window.width()),
		    height : Math.min(500, $window.height()),
			modal : true,
			title:"修改颜色定义",
			buttons : {
				"保存" : defaultMethod,
				"取消" : function() {
					clearDialog();
					colorDefDialogEdit.dialog("close");
				}
			},
			close : function() {
				clearDialog();
			}
		});
	}
	
	//------------------------对颜色的操作-------------------------
    //增加自定义颜色
	function addColorDef(){
		 var vldResult = formProxyColorDef.validateAll();
			if (!vldResult) {
				return;
			}
			var ajax = Ajax.post("/categ/colorDef/add/do");
			data = {
				name :$id("colorDefName").val(),
				expr: $id("colorDefExpr").val(),
				seqNo : 1,
			};
			ajax.data(data);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);			
					//加载最新数据列表
					colorDefGrid.jqGrid().trigger("reloadGrid");
					tempAttr = result.data;
					colorDialogView(tempAttr);

				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
	}
	
	//编辑定义颜色
	function updateColorDef(){
		var vldResult = formProxyColorDef.validateAll();
		if (!vldResult) {
			return;
		}
		var ajax = Ajax.post("/categ/colorDef/update/do");
		data = {
			id : $id("colorDefId").val(),
			name :$id("colorDefName").val(),
			expr: $id("colorDefExpr").val(),
			seqNo : 1
		};
		ajax.data(data);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				Layer.msgSuccess(result.message);			
				//加载最新数据列表
				colorDefGrid.jqGrid().trigger("reloadGrid");
				tempAttr = result.data;
				colorDialogView(tempAttr);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	
	//查询定义颜色
	function QueryColorDef(){
		var name=$id("queryColorDefName").val();
		colorDefGrid.jqGrid("setGridParam", {postData :{filterStr : JSON.encode({"name":name},true)},page:1}).trigger("reloadGrid");
	}
	//
	function checkInUse(id){
		var ajax = Ajax.post("/categ/colorDef/check/inUse/get");
		var postData = {
			id : ParseInt(id)
		};
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var inUse = result.data;
				if(inUse){
					Toast.show("亲，该颜色正在被商品分类使用，不能删除哦~", 3000, "warning");
				}else{
					deleteColorDef(id);
				}
			}
		});
		ajax.go();
	}
	//
	function deleteColorDef(id){
		var postData = {
			id :id
		};
		var ajax = Ajax.post("/categ/colorDef/delete/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr){
				if(result.type== "info"){
					Layer.msgSuccess(result.message);
					var page = colorDefGrid.getGridParam("page");
					colorDefGrid.jqGrid("setGridParam", {page:page}).trigger("reloadGrid");
				}else{
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
	}
	
	//删除定义颜色
	function goDeleteColorDef(id){
		var theLayer = Layer.confirm('确定要该定义颜色吗？', function(){
			theLayer.hide();
			checkInUse(id)
		});
	}
	//检查颜色名字是否可用
	function validateName(name) {
		var ajax = Ajax.post("/categ/colorDef/name/isabel/do");
		ajax.data({
			name : name
		});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			nameFlag = result.data;
		});
		ajax.go();
	}
	//------------------------调整控件大小-------------------------
	
	//调整控件大小
	function adjustCtrlsSize() {
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		var gridCtrlId = "attrRefList";
		var jqGridBox = $("#gbox_" + gridCtrlId);
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
		var pagerHeight = $id("attrRefPager").height();
		colorDefGrid.setGridWidth(mainWidth - 1);
		colorDefGrid.setGridHeight(mainHeight - headerHeight - pagerHeight - 3);
	}

	//---------------------------------------------------初始化--------------------------------------------------------
	$(function() {
		//页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 56,
			allowTopResize : false
		});
		// 隐藏布局north分割线
		$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
		winSizeMonitor.start(adjustCtrlsSize);
		
		$id("colorDefExpr").colorpicker({showOn: "button"});
		loadData();
		$id("btnAddColorDef").click(colorDefDialogAddOpen);
		$id("btnQueryColorDef").click(QueryColorDef);
	});
</script>
</body>
</html>
