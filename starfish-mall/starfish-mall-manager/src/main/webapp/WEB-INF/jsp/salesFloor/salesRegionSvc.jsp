<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>销售楼层管理</title>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnAdd" class="button">添加</button>
					<%-- button id="btnDelShops" class="button">批量删除</button>--%>
				</div>
				<div class="group right aligned">
					<label class="label">专区名称</label> <input id="queryName"
						class="normal one wide input" /> <span class="spacer"></span> 
					<button id="btnToQuery" class="normal button">查询</button>
				</div>
			</div>
		</div>
		<table id="salesRegionGridCtrl"></table>
		<div id="salesRegionPager"></div>
	</div>
	<!-- 	隐藏Dialog -->
	<div id="userDlg" style="display: none;">
		<div id="addForm" class="form">
			<div id="userList" class="noBorder" style="margin-left: 20px;">
				<div id="singleSalesRegion" style="margin-left: 20px;">
					<div class="field row" style="margin-top: 10px;">
						<label class="field label required one wide">专区名称</label>
						<input type="text" class="field value one half wide" id="name" >
						<input type="hidden" class="field value one half wide" id="salesRegionId" >
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label required one wide">楼层名称</label>
						<select class="field value one half wide" id="floorNo"></select>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<!--选择专区dialog -->
	<div id="regionSvcsDialog" class="form" style="display: none;height: 600px;">
		
		<div class="ui-layout-center">
			<div class="filter section">
				<div class="filter row">
					<div class="group left aligned">
						<button id="btnAddSvc" class="button">添加</button>
						<%-- button id="btnDelShops" class="button">批量删除</button>--%>
					</div>
					<%--<div class="group right aligned">
						<label class="label">服务分组名称</label> 
						<input id="svcGroup" class="normal input" /> 
						<span class="spacer"></span> 
						<label class="label">服务名称</label> 
						<input id="svcName" class="normal input" /> 
						<span class="spacer"></span> 
						<button id="btnQuerySvc" class="normal button">查询</button>
					</div>--%>
				</div>
			</div>
			<table id="regionSvcsGrid"></table>
			<div id="regionSvcsPager"></div>
		</div>
	</div>
	
	<!--选择服务dialog -->
	<div id="selectRegionSvcDialog" class="form" style="display: none;height: 600px;">
		
		<div class="ui-layout-center">
			<div class="filter section">
				<div class="filter row">
					<div class="group right aligned">
						<label class="label">服务分组名称</label> 
						<input id="selectSvcGroup" class="normal input" /> 
						<span class="spacer"></span>
						<label class="label">服务名称</label> 
						<input id="selectSvcName" class="normal input" /> 
						<span class="spacer"></span>
						<button id="btnSelectQuerySvc" class="normal button">查询</button>
					</div>
				</div>
			</div>
			<table id="selectRegionSvcGrid"></table>
			<div id="selectRegionSvcPager"></div>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		var salesRegionGridCtrl = null;
		//
		var salesRegionGridHelper = JqGridHelper.newOne("");
		//
		var formProxy = FormProxy.newOne();
		//
		var regionSvcsGridCtrl = null;
		//
		var regionSvcsGridHelper = JqGridHelper.newOne("");
		//
		var selectRegionSvcGridCtrl = null;
		//
		var selectRegionSvcGridHelper = JqGridHelper.newOne("");
		//
		//获取商品楼层
		function loadSalesFloor(callback) {
			// 隐藏页面区
			var ajax = Ajax.post("/salesFloor/selectList/get");
			ajax.params({
				type : 1
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("floorNo", result.data);
					if($.isFunction(callback)){
						callback();
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		//加载销售楼层列表数据
		function loadSalesRegionData(){
			var postData = {
				type : 1
			};
			//加载销售楼层
			salesRegionGridCtrl= $id("salesRegionGridCtrl").jqGrid({
			      url : getAppUrl("/salesFloor/salesRegion/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : 250,
				  width : "100%",
			      colNames : ["楼层号", "楼层名称", "专区名称", "操作"],  
			      colModel : [{
								name : "floorNo",
								index : "floorNo",
								width : 100,
								align : 'center'
							},
							{
								name : "salesFloor.name",
								index : "salesFloor.name",
								width : 100,
								align : 'center'
							},
							{
								name : "name",
								index : "name",
								width : 200,
								align : 'center'
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									return salesRegion = "[<a class='item' href='javascript:void(0);' onclick='openShowDlg(" + cellValue + ")' >查看</a>]<span class='chs spaceholder'></span>"
													+ "[<a class='item' href='javascript:void(0);' onclick='openEditDlg(" + cellValue + ")' >编辑</a>]<span class='chs spaceholder'></span>"
													+ "[<a class='item' href='javascript:void(0);' onclick='toShowSvcs(" + cellValue + ")' >专区服务</a>]<span class='chs spaceholder'></span>"
													+ "[<a class='item' href='javascript:void(0);' onclick='toDelSalesRegion(" + cellValue + ")' >删除</a>]<span class='chs spaceholder'></span>";
								},
								width : 200,
								align : "center"
							}],  
				loadtext: "Loading....",
				multiselect : true,// 定义是否可以多选
				multikey:'ctrlKey',
				pager : "#salesRegionPager",
				loadComplete : function(gridData){
					salesRegionGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if(isFunction(callback)){
						callback();
					}
				},
				gridComplete : function(){
				},
				ondblClickRow : function(rowId) {
					openShowDlg(rowId);
				}
			});
		}
		
		function getCallbackAfterGridLoaded() {
		}
		var salesRegionFlag = false;
		//
		formProxy.addField({
			id : "name",
			required : true,
			rules : ["maxLength[30]"],
			messageTargetId : "name"
		});
		//
		formProxy.addField({
			id : "floorNo",
			required : true,
			rules : [{
				rule : function(idOrName, type, rawValue, curData) {
					checkNameAndFloorNo(rawValue);
					if(salesRegionFlag){
						return false;
					}
					return true;
				},
				message : "该楼层此专区已存在！"
			}],
			messageTargetId : "floorNo"
		});
		
		//------------------------调整控件大小-------------------------------------------------------------------

		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "salesRegionGridCtrl";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("salesRegionPager").height();
			salesRegionGridCtrl.setGridWidth(mainWidth - 1);
			salesRegionGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 60);
		}
		//---------------------------业务---------------------------------
		//打开新增对话框
		function openAddDlg() {
			curAction = "add";
			//
			toShowTheDlg();
		}
		//打开查看对话框
		function openShowDlg(salesRegionId) {
			curAction = "view";
			//
			toShowTheDlg(salesRegionId);
		}
		//打开查看对话框
		function openEditDlg(salesRegionId) {
			curAction = "mod";
			//
			toShowTheDlg(salesRegionId);
		}
		//（真正）显示对话款
		function toShowTheDlg(dataId) {
			//
			var theDlgId = "userDlg";
			jqDlgDom = $id(theDlgId);
			//对话框配置
			var dlgConfig = {
				width : Math.min(450, $window.width()),
				height : Math.min(300, $window.height()),
				modal : true,
				open : false
			};
			//
			if (curAction == "add") {
				dlgConfig.title = "销售楼层 - 新增";
				dlgConfig.buttons = {
					"保存" : function() {
						//收集并验证要提交的数据（如果验证不通过直接返回 return）
						var vldResult = formProxy.validateAll();
						if (!vldResult) {
							return;
						}
						var postData = formProxy.getValues();
						//
						goAddSalesRegion(postData);
						$(this).dialog("close");
						
					},
					"取消" : function() {
						//
						jqDlgDom.prop("continuousFlag", false);
						//
						$(this).dialog("close");
						//隐藏表单验证消息
						formProxy.hideMessages();
					}
				
				};
				dlgConfig.close = function() {
					formProxy.hideMessages();	
				};
			} else if (curAction == "mod") {
				dlgConfig.title = "销售楼层 - 修改";
				dlgConfig.buttons = {
					"保存" : function() {
						//收集并验证要提交的数据（如果验证不通过直接返回 return）
						var vldResult = formProxy.validateAll();
						if (!vldResult) {
							return;
						}
						var postData = formProxy.getValues();
						//
						goEditSalesRegion(postData);
						$(this).dialog("close");
					},
					"取消" : function() {
						$(this).dialog("close");
						//隐藏表单验证消息
						formProxy.hideMessages();
					}
				};
			} else {
				//== view 查看
				dlgConfig.title = "销售楼层 - 查看";
				dlgConfig.buttons = {
					"修改 > " : function() {
						$(this).dialog("close");
						
						openEditDlg(dataId);
					},
					"关闭" : function() {
						$(this).dialog("close");
					}
				};
			}
			//
			jqDlgDom.dialog(dlgConfig);
			//
			initTheDlgCtrls(dataId);
		}
		
		function initTheDlgCtrls(dataId) {
			//批量简单设置
			if (curAction == "add" || curAction == "mod") {
				jqDlgDom.find(":input").prop("disabled", false);
			} else {
				jqDlgDom.find(":input").prop("disabled", true);
			}
			
			if (curAction == "mod"){
				$id("no").prop("disabled", true);
			}
			//重置控件值
			$("#userDlg input[type=text]").val("");
			$("#userDlg textarea").val("");
			textSet("salesRegionId", "");
			textSet("floorNo", "");
			//
			if (curAction == "view" || curAction == "mod") {
				var data = salesRegionGridHelper.getRowData(dataId);
				if (data != null) {
					textSet("salesRegionId", data.id);
					textSet("name", data.name);
					textSet("floorNo", data.floorNo);
				}
			}
		}
		
		function goAddSalesRegion(salesRegionInfoMap){
			var hintBox = Layer.progress("正在保存数据...");
			
			var ajax = Ajax.post("/salesFloor/salesRegion/add/do");
			salesRegionInfoMap["type"] = 1;
			ajax.data(salesRegionInfoMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					
					salesRegionGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
					//
					getCallbackAfterGridLoaded = function(){
						
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
		
		function goEditSalesRegion(salesRegionInfoMap){
			var hintBox = Layer.progress("正在保存数据...");
			
			var ajax = Ajax.post("/salesFloor/salesRegion/edit/do");
			salesRegionInfoMap["id"] = textGet("salesRegionId");
			salesRegionInfoMap["type"] = 1;
			ajax.data(salesRegionInfoMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					
					salesRegionGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
					//
					getCallbackAfterGridLoaded = function(){
						
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
		
		function toDelSalesRegion(id){
			var theLayer = Layer.confirm('确定要删除该专区吗？', function() {
				var postData = {
					id : id,
					type : 1
				};
				var ajax = Ajax.post("/salesFloor/salesRegion/delete/by/id");
				ajax.data(postData);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						theLayer.hide();
						Layer.msgSuccess(result.message);
						salesRegionGridCtrl.jqGrid("setGridParam", {
							page : 1
						}).trigger("reloadGrid");
					} else {
						theLayer.hide();
						Layer.msgWarning(result.message);
					}
				});
				ajax.go();
			});
			
		}
		
		//更改车辆品牌 禁用 开启状态
		function openOrClose(id) {
			var salesRegionMap = salesRegionGridHelper.getRowData(id)
			var postData = {
				id : salesRegionMap.no,
				no : salesRegionMap.no,
				disabled : salesRegionMap.disabled
			};
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/salesRegion/isDisabled/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					salesRegionGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
					Layer.msgSuccess(result.message);
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.always(function() {
				//隐藏等待提示框
				loaderLayer.hide();
			});
			ajax.go();
		}
		
		//初始化专区商品Dialog
		function initRegionSvcsDialog(){
			regionSvcsDialog = $( "#regionSvcsDialog" ).dialog({
		        autoOpen: false,
		        width : Math.min(980, $window.width()),
		        height : Math.min(600, $window.height()),
		        modal: true,
		        title : '专区服务',
		        buttons: {
		            "关闭": function() {
		            	regionSvcsDialog.dialog( "close" );
		          }
		        },
		        close: function() {
		        	regionSvcsDialog.dialog( "close" );
		        }
		      });
		}
		
		function toShowSvcs(id){
			var filter = {
				regionId : id
			};
			textSet("salesRegionId", id);
			//
			loadSelectRegionSvcData();
			//
			regionSvcsGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			regionSvcsDialog.dialog( "open" );
		}
		
		//加载商品列表数据
		function loadRegionSvcsData(){
			var postData = {
			};
			//加载商品
			regionSvcsGridCtrl= $id("regionSvcsGrid").jqGrid({
			      url : getAppUrl("/salesFloor/salesRegionSvcs/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : 370,
				  width : "100%",
				  colNames : ["id", "服务id", "服务分组名称", "服务名称", "销售价", "服务状态", "操作"],  
			      colModel : [{
								name : "id",
								index : "id",
								hidden : true
							},
							{
								name : "svcId",
								index : "svcId",
								hidden : true
							},
							{
								name : "svcx.grounpName",
								index : "svcx.grounpName",
								width : 200,
								align : 'left',
							},
							{
								name : "svcx.name",
								index : "svcx.name",
								width : 300,
								align : 'left',
							},
							{
								name : "svcx.salePrice",
								index : "svcx.salePrice",
								width : 120,
								align : 'center'
							},
							{name:"svcx.disabled",width:100,align : "center",formatter : function (cellValue) {
		                	  var str = "启用";
		                	  if(cellValue == 0){
		                		  str = "启用";
		                	  }else if(cellValue == 1){
		                		  str = "禁用";
		                	  }
								return str;
							}},
							{
							name : 'id',
							index : 'id',
							formatter : function(cellValue, option, rowObject) {
								return salesRegion = "[<a class='item' href='javascript:void(0);' onclick='delRegionSvc(" + cellValue + ")' >删除</a>]<span class='chs spaceholder'></span>";
							},
							width : 170,
							align : "center"
							}],  
				loadtext: "Loading....",
				multiselect : true,// 定义是否可以多选
				multikey:'ctrlKey',
				pager : "#regionSvcsPager",
				loadComplete : function(gridData){
					regionSvcsGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if(isFunction(callback)){
						callback();
					}
				},
				gridComplete : function(){
				}
			});
		}
		
		function openAddSvcDlg(){
			//
			initSelectRegionSvcDialog();
			//
			selectRegionSvcGridCtrl.jqGrid("setGridParam", {postData :{},page:1}).trigger("reloadGrid");
			//
			selectRegionSvcDialog.dialog( "open" );
		}
		
		//初始化选择商品Dialog
		function initSelectRegionSvcDialog(){
			selectRegionSvcDialog = $( "#selectRegionSvcDialog" ).dialog({
		        autoOpen: false,
		        width : Math.min(820, $window.width()),
		        height : Math.min(480, $window.height()),
		        modal: true,
		        title : '选择服务',
		        buttons: {
		        	"确定": function(){
		            	addSelectSvc();
		            },
		            "关闭": function() {
		            	selectRegionSvcDialog.dialog( "close" );
		          }
		        },
		        close: function() {
		        	selectRegionSvcDialog.dialog( "close" );
		        }
		      });
		}
		
		function loadSelectRegionSvcData(){
			var postData = {
			};
			//加载商品
			selectRegionSvcGridCtrl= $id("selectRegionSvcGrid").jqGrid({
			      url : getAppUrl("/carSvc/list/get"),  
			      contentType : 'application/json', 
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : 250,
				  width : "100%",
			      colNames : ["id", "服务分组名称", "服务名称", "销售价", "服务状态", "" ],  
			      colModel : [{
								name : "id",
								index : "id",
								hidden : true
							},
							{
								name : "grounpName",
								index : "grounpName",
								width : 200,
								align : 'left',
							},
							{
								name : "name",
								index : "name",
								width : 300,
								align : 'left',
							},
							{
								name : "salePrice",
								index : "salePrice",
								width : 120,
								align : 'center'
							},
							{name:"disabled",width:110,align : "center",formatter : function (cellValue) {
		                	  var str = "启用";
		                	  if(cellValue == 0){
		                		  str = "启用";
		                	  }else if(cellValue == 1){
		                		  str = "禁用";
		                	  }
								return str;
							}},{
								name : "id",
								index : "id",
								hidden : true,
								formatter : function(cellValue, option, rowObject) {
									var salesRegionId = textGet("salesRegionId");
									var regionSvcs = $id("regionSvcsGrid").jqGrid("getRowData");
									if(regionSvcs){
										for(var i = 0; i < regionSvcs.length; i++){
											if(regionSvcs[i].svcId == cellValue){
												return "1";
											}
										}
									}
									return '0';
								}
							}],  
				loadtext: "Loading....",
				multiselect : true,// 定义是否可以多选
				multikey:'ctrlKey',
				pager : "#selectRegionSvcPager",
				loadComplete : function(gridData){
					selectRegionSvcGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if(isFunction(callback)){
						callback();
					}
				},
				gridComplete : function(){
				}
			});

		}
		
		function addSelectSvc(){
			var regionSvc = selectRegionSvcGridCtrl.jqGrid("getGridParam", "selarrrow");
			if (regionSvc == "") {
				Layer.msgWarning("亲！您还没选择商品呢！");
				return;
			}
			for (var i = 0; i < regionSvc.length; i++) {
				var rowData = selectRegionSvcGridCtrl.jqGrid("getRowData", regionSvc[i]);//根据上面的id获得本行的所有数据
				var val = rowData["id"]; //获得制定列的值 （auditStatus 为colModel的name）
				if ("1" == val) {
					Layer.msgWarning("包含已经选择的服务");
					return;
				}
			}
			toAddRegionSvc(regionSvc);
		}
		
		function toAddRegionSvc(regionSvc){
			var postData = {
				ids : regionSvc,
				regionId : textGet("salesRegionId")
			};
			var ajax = Ajax.post("/salesFloor/regionSvc/add/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					selectRegionSvcDialog.dialog("close");
					regionSvcsGridCtrl.jqGrid("setGridParam", {postData :{},page:1}).trigger("reloadGrid");
					selectRegionSvcGridCtrl.jqGrid("setGridParam", {postData :{},page:1}).trigger("reloadGrid");
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
		}
		
		function delRegionSvc(id){
			var theLayer = Layer.confirm('确定要删除该服务吗？', function() {
				var postData = {
					id : id
				};
				var ajax = Ajax.post("/salesFloor/regionSvc/delete/by/id");
				ajax.data(postData);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						theLayer.hide();
						Layer.msgSuccess(result.message);
						regionSvcsGridCtrl.jqGrid("setGridParam", {
							page : 1
						}).trigger("reloadGrid");
						selectRegionSvcGridCtrl.jqGrid("setGridParam", {
							page : 1
						}).trigger("reloadGrid");
					} else {
						theLayer.hide();
						Layer.msgWarning(result.message);
					}
				});
				ajax.go();
			});
		}
		
		function checkNameAndFloorNo(floorNo){
			var postData = {
				floorNo : floorNo,
			};
			var name = textGet("name")
			if(name){
				postData.name = name;
			}
			var id = textGet("salesRegionId")
			if(id){
				postData.id = id;
			}
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/salesFloor/salesRegion/get/by/name/and/floorNo");
			ajax.sync();
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					salesRegionFlag = true;
				} else{
					salesRegionFlag = false;
				}
			});
			ajax.always(function() {
				//隐藏等待提示框
				loaderLayer.hide();
			});
			ajax.go();
		}
		
		//---------------------------业务---------------------------------
		$(function() {
			//页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 56,
				allowTopResize : false
			});
			//隐藏布局north分割线
			$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
			//
			loadSalesRegionData();
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
			//添加车辆品牌
			$id("btnAdd").click(openAddDlg);
			//
			loadSalesFloor();
			//
			initRegionSvcsDialog();
			//
			$id("btnAddSvc").click(openAddSvcDlg);
			//
			loadRegionSvcsData();
			//绑定查询按钮
			$id("btnToQuery").click(function() {
				
				var filter = {
					type : 1
				};
				var name = textGet("queryName");
				if(name){
					filter.name = name;
				}
				//加载jqGridCtrl
				salesRegionGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			});
			
			//绑定查询专区商品按钮
			$id("btnQuerySvc").click(function() {
				var filter = {
					regionId : textGet("salesRegionId")
				};
				var name = textGet("svcName");
				if(name){
					filter.name = name;
				}
				var grounpName = textGet("svcGroup");
				if(grounpName){
					filter.grounpName = grounpName;
				}
				//加载jqGridCtrl
				regionSvcsGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			});
			//绑定查询服务按钮
			$id("btnSelectQuerySvc").click(function() {
				var filter = {
				};
				var name = textGet("selectSvcName");
				if(name){
					filter.name = name;
				}
				var grounpName = textGet("selectSvcGroup");
				if(grounpName){
					filter.grounpName = grounpName;
				}
				//加载jqGridCtrl
				selectRegionSvcGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			});
		});
	</script>
</body>
</html>