<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>物流公司列表</title>
<style type="text/css">
table.gridtable {
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
	width: 700px;
}

table.gridtable th {
	border:1px solid #AAA;
	border-width: 1px;
	padding: 3px;
	border-style: solid;
	border-color: #666666;
	background-color: #dedede;
	height: 30px;
}

table.gridtable td {
	border:1px solid #AAA;
	border-width: 1px;
	padding: 3px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
	height: 30px;
}

div.image.layout{
	display: inline-block;
	position: relative;
	/* border: 1px solid #D3D3D3;
	border-radius: 4px; */
	width:85px;
	height: 85px;
	line-height: 85px;
}
</style>
</head>

<body id="rootPanel">
	
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<button id="addComBtn" class="normal button">添加</button>
				<div class="group right aligned">
					<label class="normal label">物流公司名称</label>
					<input id="comQueryName" class="input one and half wide"> <span class="spacer"></span> 
					<button id="queryComBtn" class="button">查询</button>
				</div>
			</div>
		</div>
		<div id="logis_com_grid">
			<table id="logis_com_list"></table>
			<div id="logis_com_pager"></div>
		</div>
	</div>

	<div id="logis_com_dialog" style="display:none;" class="form">
		<div class="field row">
			<label class="one half wide required field label" >物流公司名称</label>
			<input id="comName" class="field two half wide value" />
		</div>
		
		<div class="field row">
			<label class="one half wide required field label">编码</label>
			<input id="comCode" class="field two half wide value" />
		</div>
		
		<div class="field row">
			<label class="one half wide required field label">物流公司网址</label>
			<input class="field value two half wide" id="comUrl" />
		</div>
		
		<div class="field row">
        	<label class="field label one half wide">公司Logo网址</label>
        	<input class="field value two half wide" id="logoPath" />
        </div>
        
        <div class="field row">
        	<div class="field group">
				<label class="field label one half wide">启用状态</label>
				
				<div class="field group">
					<input id="disabled-F" type="radio" checked="checked" name="disabled" value="false" />
					<label for="disabled-F">启用</label>
					<input id="disabled-T" type="radio" name="disabled" value="true" />
					<label for="disabled-T">禁用</label>
				</div>
			</div>
        </div>
	
		<div class="field row">
			<label class="field one half wide label">描述</label>
			<textarea id="comDesc" class="field two half wide value" style="height: 60px; "></textarea>
		</div>	
	</div>
		
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		//
		var logis_com_grid = null;
		//
		var view_com_dialog = null;
		var add_com_dialog = null;
		var edit_com_dialog = null;
		//
		var method = "add";
		//实例化新增模块表单代理
		var comFormProxy = FormProxy.newOne();
		//
		comFormProxy.addField({
			id : "comName",
			required : true,
			messageTargetId : "comName"
		});
		comFormProxy.addField({
			id : "comCode",
			required : true,
			messageTargetId : "comCode"
		});
		comFormProxy.addField({
			id : "comUrl",
			required : true,
			messageTargetId : "comUrl"
		});

		comFormProxy.addField("disabled");
		comFormProxy.addField("method");
		comFormProxy.addField("comDesc");
		//
		$(function() {
			//页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 70,
				allowTopResize : false
			});
			
			//
			loadData();
			//
			initAddDialog();
			//调整控件大小
			winSizeMonitor.start(adjustCtrlsSize);
			//
			$id("queryComBtn").click(function(){
				refreshComList();
			});
			
			$id("addComBtn").click(function(){
				goAddCom();
			});
		});
		
		//-------------------------------------------------调整控件大小------------------------------------------------------------
		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "logis_com_list";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("logis_com_pager").height();
			logis_com_grid.setGridWidth(mainWidth - 3);
			logis_com_grid.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 55);
		}
		
		//-------------------------------------------------功能实现 --------------------------------------------------------------
		
		//
		function deleteCom(id){
			if(isNoB(id)) return;
			//
			var theLayer = Layer.confirm('确定要删除该物流公司吗？', function(){
				
				var hintBox = Layer.progress("正在保存数据...");
				
				var ajax = Ajax.post("/setting/logis/company/delete/do");
					ajax.data({id: parseInt(id)});
					ajax.done(function(result, jqXhr){
						if(result.type== "info"){
							Layer.msgSuccess(result.message);
							//加载最新数据列表
							refreshComList();
							getCallbackAfterGridLoaded = function(){
								return function(){
									//getCallbackAfterGridLoaded();
								};
							};
						}else{
							Layer.msgWarning(result.message);
						}
					});
					ajax.always(function() {
						theLayer.hide();
						hintBox.hide();
					});
					ajax.go();
				});
		}
		
		//
		function editLogisCom(comId){
			var hintBox = Layer.progress("正在保存数据...");
			//
			var dataMap = getLogisComMap();
			dataMap["id"] = ParseInt(comId);
			//
			var ajax = Ajax.post("/setting/logis/company/update/do");
			
			ajax.data(dataMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					var comId = result.data;
					//加载最新数据列表
					refreshComList();
					getCallbackAfterGridLoaded = function(){
						return function(){
							goViewCom(comId);
						};
					};
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
		function initEditDialog(comId){
			edit_com_dialog = $("#logis_com_dialog").dialog({
				autoOpen : false,
				height : Math.min(400, $window.height()),
				width : Math.min(550, $window.width()),
				modal : true,
				title : '编辑物流公司',
				buttons : {
					"保存" : function(){
						selectMethod(comId);
					},
					"关闭" : function() {
						edit_com_dialog.dialog("close");
					}
				},
				close : function() {
					comFormProxy.hideMessages();

				}
			});
		}
		
		//
		function goEditCom(comId){
			method = "edit";
			//
			initEditDialog(comId);
			showLogisComOnDialog(comId);
			edit_com_dialog.dialog("open");
		}
		
		//
		function showLogisComOnDialog(comId){
			var comMap = comGridHelper.getRowData(comId);
			//
			$id("comName").val(comMap.name);
			//
			$id("comCode").val(comMap.code);
			//
			$id("comUrl").val(comMap.url);
			//
			$id("logoPath").val(comMap.logoPath == null ? '' : comMap.logoPath);
			//
			$id("comDesc").val(comMap.desc == null ? '' : comMap.desc);
			//
			var disabledStr = comMap.disabled.toString();
			comFormProxy.setValue("disabled", disabledStr);
			
			
			if(method == "view"){
				$id("comName").attr("disabled", "disabled");
				//
				$id("comCode").attr("disabled", "disabled");
				//
				$id("comUrl").attr("disabled", "disabled");
				//
				$id("logoPath").attr("disabled", "disabled");
				//
				$id("comDesc").attr("disabled", "disabled");
				//
				$("[name='disabled']").each(function(){
					$(this).attr("disabled", "disabled");
				});
			}else{
				$id("comName").removeAttr("disabled");
				//
				$id("comCode").removeAttr("disabled");
				//
				$id("comUrl").removeAttr("disabled");
				//
				$id("logoPath").removeAttr("disabled");
				//
				$id("comDesc").removeAttr("disabled");
				//
				$("[name='disabled']").each(function(){
					$(this).removeAttr("disabled");
				});
			}
		}
		
		//
		function initViewDialog(comId){
			view_com_dialog = $("#logis_com_dialog").dialog({
				autoOpen : false,
				height : Math.min(400, $window.height()),
				width : Math.min(550, $window.width()),
				modal : true,
				title : '查看物流公司',
				buttons : {
					"修改 >" : function(){
						selectMethod(comId);
					},
					"关闭" : function() {
						view_com_dialog.dialog("close");
					}
				},
				close : function() {
					comFormProxy.hideMessages();

				}
			});
		}
		
		//
		function goViewCom(comId){
			method = "view";
			initViewDialog(comId);
			showLogisComOnDialog(comId);
			view_com_dialog.dialog("open");
		}
		
		//
		function getLogisComMap(){
			var dataMap = {};
			var name = textGet("comName");
			dataMap["name"] = name;
			var code = textGet("comCode");
			dataMap["code"] = code;
			var disabled = comFormProxy.getValue("disabled");
			dataMap["disabled"] = disabled;
			var url = textGet("comUrl");
			dataMap["url"] = url;
			var logoPath = textGet("logoPath");
			dataMap["logoPath"] = logoPath;
			var desc = textGet("comDesc");
			dataMap["desc"] = desc;
			return dataMap;
		}
		
		//
		function addLogisCom(){
			var hintBox = Layer.progress("正在保存数据...");

			var dataMap = getLogisComMap();
			var ajax = Ajax.post("/setting/logis/company/add/do");
			
			ajax.data(dataMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					var comId = result.data;
					//加载最新数据列表
					refreshComList();
					getCallbackAfterGridLoaded = function(){
						return function(){
							goViewCom(comId);
						};
					};
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
		function vldInputLogisCom(){
			var vldstatus = comFormProxy.validateAll();
			if(!vldstatus) return false;
			return true;
		}
		
		//
		function clearDialog(){
			//
			$id("comName").val("");
			$id("comName").removeAttr("disabled");
			//
			$id("comCode").val("");
			$id("comCode").removeAttr("disabled");
			//
			$id("comUrl").val("");
			$id("comUrl").removeAttr("disabled");
			//
			$id("logoPath").val("");
			$id("logoPath").removeAttr("disabled");
			//
			$id("comDesc").val("");
			$id("comDesc").removeAttr("disabled");
			//
			comFormProxy.setValue("disabled", "false");
			$("[name='disabled']").each(function(){
				$(this).removeAttr("disabled");
			});
		}
		
		//
		function selectMethod(comId){
			if(method == 'add'){
				var vld = vldInputLogisCom();
				if(vld) addLogisCom();
			}else if(method == 'view'){
				goEditCom(comId);
			}else if(method == 'edit'){
				var vld = vldInputLogisCom();
				if(vld){
					editLogisCom(comId);
				}
			}
		}
		
		//
		function initAddDialog(){
			add_com_dialog = $("#logis_com_dialog").dialog({
				autoOpen : false,
				height : Math.min(400, $window.height()),
				width : Math.min(550, $window.width()),
				modal : true,
				title : '新增物流公司',
				buttons : {
					"保存" : selectMethod,
					"取消" : function() {
						clearDialog();
						add_com_dialog.dialog("close");
					}
				},
				close : function() {
					comFormProxy.hideMessages();

				}
			});
		}
		
		function goAddCom(){
			method = "add";
			initAddDialog();
			clearDialog();
			add_com_dialog.dialog("open");
		}
		
		//
		function refreshComList(){
			var filter = {};
			var name = textGet("comQueryName");
			if(name){
				filter.name = name;
			}
			//
			logis_com_grid.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			getCallbackAfterGridLoaded = function(){
			};
		}
		//
		function getCallbackAfterGridLoaded(){
			
		}
		
		//
		var comGridHelper = JqGridHelper.newOne("");
		
		function loadData() {
			//
			var filter = {};
			var comName = textGet("comQueryName");
			if(comName){
				filter.name = comName;
			}
			//
			logis_com_grid= $("#logis_com_list").jqGrid({
			      url : getAppUrl("/setting/logis/coms/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(filter,true)},
			      height : "100%",
				  width : "100%",
				  colNames : [ "id", "物流公司名称", "网址", "使用状态", "描述","操作" ],
			      colModel : [{name:"id", index:"id",width:100,align:'center', hidden: true}, 
			                  {name:"name", width:150, align:'left',}, 
			                  {name:"url", width:200, align:'left',},
			                  {name:"disabled", index:"disabled",width:70,align : 'left',formatter : function (cellValue) {
									return cellValue==true?'禁用':'启用';
								}},
			                  {name:"desc", width:200, align:'left',},
			                  {name:'id',index:'id', formatter : function (cellValue,option,rowObject) {
								return "<span> [<a class='item' href='javascript:void(0);' onclick='goViewCom("
								+ JSON.stringify(cellValue)
								+ ")' >查看</a>]   [<a class='item' href='javascript:void(0);' onclick='goEditCom("
								+ JSON.stringify(cellValue)
								+ ")' >修改</a>] "; 
								/* [<a class='item' href='javascript:void(0);' onclick='deleteCom("
								+ cellValue
								+ ")' >删除</a>] "; */
							},
						width:150,align:"center"}
			                  ],  
				loadComplete : function(gridData){
					comGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if(isFunction(callback)){
						callback();
					}

				},
				ondblClickRow: function(rowId) {
					var comMap = comGridHelper.getRowData(rowId)
					goViewCom(comMap.id);
				}
			});

		}
		
	</script>
</body>
</html>
