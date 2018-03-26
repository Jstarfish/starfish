<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>车辆系列管理</title>
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
					<label class="label">系列名称</label> <input id="queryName"
						class="normal one half wide input" /> <span class="spacer"></span> 
					<button id="btnToQuery" class="normal button">查询</button>
				</div>
			</div>
		</div>
		<table id="carSerialGridCtrl"></table>
		<div id="carSerialPager"></div>
	</div>
	<!-- 	隐藏Dialog -->
	<div id="userDlg" style="display: none;">
		<div id="addForm" class="form">
			<div id="userList" class="noBorder" style="margin-left: 20px;">
				<div id="singlecarSerial" style="margin-left: 20px;">
					<input type="hidden" class="field value one half wide" id="carSerialId" >
					<div class="field row" style="margin-top: 10px;">
						<label class="field label required one half wide">车辆品牌</label>
						<select class="field value" id="brandId" name="brandId"><option value="" title="- 请选择 -">- 请选择 -</option></select>
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label required one half wide">型号名称</label>
						<input type="text" class="field value one half wide" id="name" >
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label one half wide">外部Id</label>
						<input type="text" class="field value one half wide" id="refId" >
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label one half wide">外部名称</label>
						<input type="text" class="field value one half wide" id="refName" >
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js?<%=System.currentTimeMillis()%>"></script>
	<script type="text/javascript">
		var carSerialGridCtrl = null;
		//
		var carSerialGridHelper = JqGridHelper.newOne("");
		//
		var formProxy = FormProxy.newOne();
		//获取车辆品牌
		function loadCarBrand(callback) {
			// 隐藏页面区
			var ajax = Ajax.post("/car/carBrand/selectList/get");
			ajax.params({});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("brandId", result.data);
					if($.isFunction(callback)){
						callback();
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
	
		//加载合作店列表数据
		function loadCarSerialData(){
			var postData = {
			};
			//加载合作店
			carSerialGridCtrl= $id("carSerialGridCtrl").jqGrid({
			      url : getAppUrl("/car/carSerial/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : 250,
				  width : "100%",
			      colNames : ["id", "车辆品牌", "系列名称", "拼音", "状态", "操作"],  
			      colModel : [{
								name : "id",
								index : "id",
								hidden : true
							},
							{
								name : "py",
								index : "py",
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
								name : "py",
								index : "py",
								width : 100,
								align : 'center'
							},
							{
								name : 'disabled',
								index : 'disabled',
								formatter : function(cellValue, option, rowObject) {
									if (cellValue == false) {
										return "启用";
									} else {
										return "禁用";
									}
								},
								width : 100,
								align : "center"
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									var carSerial = "[<a class='item' href='javascript:void(0);' onclick='openShowDlg(" + cellValue + ")' >查看</a>]<span class='chs spaceholder'></span>"
													+ "[<a class='item' href='javascript:void(0);' onclick='openEditDlg(" + cellValue + ")' >编辑</a>]<span class='chs spaceholder'></span>";
									if (rowObject.disabled == false) {
										return carSerial + "[<a class='item' href='javascript:void(0);' onclick='openOrClose(" + cellValue + ")' >禁用</a>]<span class='chs spaceholder'></span>" ;
									} else {
										return carSerial + "[<a class='item' href='javascript:void(0);' onclick='openOrClose(" + cellValue + ")' >开启</a>]<span class='chs spaceholder'></span>" ;
									}
								},
								width : 200,
								align : "center"
							}],  
				loadtext: "Loading....",
				multiselect : true,// 定义是否可以多选
				multikey:'ctrlKey',
				pager : "#carSerialPager",
				loadComplete : function(gridData){
					carSerialGridHelper.cacheData(gridData);
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
		
		//
		formProxy.addField({
			id : "name",
			required : true,
			messageTargetId : "name"
		});
		//
		formProxy.addField({
			id : "brandId",
			required : true,
			messageTargetId : "brandId"
		});
		//
		formProxy.addField({
			id : "refId",
			rules : ["maxLength[30]", "isNum"],
			messageTargetId : "refId"
		});
		//
		formProxy.addField({
			id : "refName",
			rules : ["maxLength[30]"],
			messageTargetId : "refName"
		});
		
		//------------------------调整控件大小-------------------------------------------------------------------

		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "carSerialGridCtrl";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("carSerialPager").height();
			carSerialGridCtrl.setGridWidth(mainWidth - 1);
			carSerialGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 60);
		}
		
		//---------------------------业务---------------------------------
		//打开新增对话框
		function openAddDlg() {
			curAction = "add";
			//
			toShowTheDlg();
		}
		//打开查看对话框
		function openShowDlg(carSerialId) {
			curAction = "view";
			//
			toShowTheDlg(carSerialId);
		}
		//打开查看对话框
		function openEditDlg(carSerialId) {
			curAction = "mod";
			//
			toShowTheDlg(carSerialId);
		}
		//（真正）显示对话款
		function toShowTheDlg(dataId) {
			//
			var theDlgId = "userDlg";
			jqDlgDom = $id(theDlgId);
			//对话框配置
			var dlgConfig = {
				width : Math.min(550, $window.width()),
				height : Math.min(400, $window.height()),
				modal : true,
				open : false
			};
			//
			if (curAction == "add") {
				dlgConfig.title = "车辆系列 - 新增";
				dlgConfig.buttons = {
					"保存" : function() {
						//收集并验证要提交的数据（如果验证不通过直接返回 return）
						var vldResult = formProxy.validateAll();
						if (!vldResult) {
							return;
						}
						var postData = formProxy.getValues();
						//
						goAddCarSerial(postData);
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
				dlgConfig.title = "车辆系列 - 修改";
				dlgConfig.buttons = {
					"保存" : function() {
						//收集并验证要提交的数据（如果验证不通过直接返回 return）
						var vldResult = formProxy.validateAll();
						if (!vldResult) {
							return;
						}
						var postData = formProxy.getValues();
						//
						goEditCarSerial(postData);
						$(this).dialog("close");
					},
					"取消" : function() {
						$(this).dialog("close");
						//隐藏表单验证消息
						formProxy.hideMessages();
					}
				};
				dlgConfig.close = function() {
					formProxy.hideMessages();	
				};
			} else {
				//== view 查看
				dlgConfig.title = "车辆系列 - 查看";
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
				$("#merchant_tbody").find(":input").prop("disabled", true);
				$("#userAccount_tbody").find(":input").prop("disabled", true);
				$("#bizLicense_tbody").find(":input").prop("disabled", true);
			} else {
				jqDlgDom.find(":input").prop("disabled", true);
			}
			//重置控件值
			$("#carSerialLogo").attr("src", "");
			$("#userDlg input[type=text]").val("");
			$("#userDlg textarea").val("");
			textSet("carSerialId", "");
			//
			if (curAction == "view" || curAction == "mod") {
				var data = carSerialGridHelper.getRowData(dataId);
				if (data != null) {
					textSet("carSerialId", data.id);
					textSet("name", data.name);
					textSet("refId", data.refId);
					textSet("refName", data.refName);
				}
				//获取车辆品牌
				loadCarBrand(function(){
					textSet("brandId", data.brandId);
				});
			}else{
				//获取车辆品牌
				loadCarBrand();
			}
		}
		
		function goAddCarSerial(shopInfoMap){
			var hintBox = Layer.progress("正在保存数据...");
			
			var ajax = Ajax.post("/car/carSerial/add/do");
			ajax.data(shopInfoMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					
					carSerialGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
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
		
		function goEditCarSerial(shopInfoMap){
			var hintBox = Layer.progress("正在保存数据...");
			
			var ajax = Ajax.post("/car/carSerial/edit/do");
			shopInfoMap["id"] = textGet("carSerialId");
			ajax.data(shopInfoMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					
					carSerialGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
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
		
		//更改车辆品牌 禁用 开启状态
		function openOrClose(id) {
			var carSerialMap = carSerialGridHelper.getRowData(id)
			var postData = {
				id : carSerialMap.id,
				disabled : carSerialMap.disabled
			};
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/car/carSerial/isDisabled/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					carSerialGridCtrl.jqGrid("setGridParam", {
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
			loadCarSerialData();
			//添加车辆系列
			$id("btnAdd").click(openAddDlg);
			//绑定查询按钮
			$id("btnToQuery").click(function() {
				
				var filter = {};
				var name = textGet("queryName");
				if(name){
					filter.name = name;
				}
				
				//加载jqGridCtrl
				carSerialGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			});
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
			
		});
	</script>
</body>
</html>