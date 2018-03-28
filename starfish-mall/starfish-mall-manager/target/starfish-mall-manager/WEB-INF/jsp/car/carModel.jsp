<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>车辆型号管理</title>
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
					<label class="label">型号名称</label> <input id="queryName"
						class="normal input one half wide" /> <span class="spacer"></span> 
					<button id="btnToQuery" class="normal button">查询</button>
				</div>
			</div>
		</div>
		<table id="carModelGridCtrl"></table>
		<div id="carModelPager"></div>
	</div>
	
	<!-- 	隐藏Dialog -->
	<div id="userDlg" style="display: none;">
		<div id="addForm" class="form">
			<div id="userList" class="noBorder" style="margin-left: 20px;">
				<div id="singleCarBrand" style="margin-left: 20px;">
					<input type="hidden" class="field value one half wide" id="carModelId" >
					<div class="field row" style="margin-top: 10px;">
						<label class="field label required one half wide">品牌车系</label>
						<select class="field value" id="brandId" name="brandId"><option value="" title="- 请选择 -"></option></select>
						<select class="field value" id="serialId" name="serialId"><option value="" title="- 请选择 -"></option></select>
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label required one half wide">车型名称</label>
						<input type="text" class="field value two wide" id="name" >
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label one half wide">生产年份</label>
						<input type="text" class="field value two wide" id="makeYear" > 年
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label one half wide">排量</label>
						<input type="text" class="field value two wide" id="sweptVol" > L
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label one half wide">款型</label>
						<input type="text" class="field value two wide" id="style" >
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label one half wide">外部Id</label>
						<input type="text" class="field value two wide" id="refId" >
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label one half wide">外部名称</label>
						<input type="text" class="field value two wide" id="refName" >
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js?<%=System.currentTimeMillis()%>"></script>
	<script type="text/javascript">
		var carModelGridCtrl = null;
		//
		var carModelGridHelper = JqGridHelper.newOne("");
		//
		var formProxy = FormProxy.newOne();
		//获取车辆品牌
		function loadCarBrand(callback) {
			// 隐藏页面区
			var ajax = Ajax.post("/car/carBrand/selectList/get");
			ajax.params({});
			ajax.sync();
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
		//获取车辆车系
		function loadCarSerial(id, callback) {
			// 隐藏页面区
			var ajax = Ajax.post("/car/carSerial/selectList/get");
			ajax.params({
				brandId : id
			});
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("serialId", result.data);
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
		function loadCarModelData(){
			var postData = {
			};
			//加载合作店
			carModelGridCtrl= $id("carModelGridCtrl").jqGrid({
			      url : getAppUrl("/car/carModel/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : 250,
				  width : "100%",
			      colNames : ["id", "型号名称", "拼音", "全名", "生产年份", "状态", "操作"],  
			      colModel : [{
								name : "id",
								index : "id",
								hidden : true
							},
							{
								name : "name",
								index : "name",
								width : 250,
								align : 'left'
							},
							{
								name : "py",
								index : "py",
								width : 100,
								align : 'left',
							},
							{
								name : "fullName",
								index : "fullName",
								width : 150,
								align : 'left'
							},
							{
								name : "makeYear",
								index : "makeYear",
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
									var carModel = "[<a class='item' href='javascript:void(0);' onclick='openShowDlg(" + cellValue + ")' >查看</a>]<span class='chs spaceholder'></span>"
												+ "[<a class='item' href='javascript:void(0);' onclick='openEditDlg(" + cellValue + ")' >编辑</a>]<span class='chs spaceholder'></span>";;
									if (rowObject.disabled == false) {
										return carModel + "[<a class='item' href='javascript:void(0);' onclick='openOrClose(" + cellValue + ")' >禁用</a>]<span class='chs spaceholder'></span>" ;
									} else {
										return carModel + "[<a class='item' href='javascript:void(0);' onclick='openOrClose(" + cellValue + ")' >开启</a>]<span class='chs spaceholder'></span>" ;
									}
								},
								width : 200,
								align : "center"
							}],  
				loadtext: "Loading....",
				multiselect : true,// 定义是否可以多选
				multikey:'ctrlKey',
				pager : "#carModelPager",
				loadComplete : function(gridData){
					carModelGridHelper.cacheData(gridData);
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
			rules : ["maxLength[30]"],
			messageTargetId : "name"
		});
		//
		formProxy.addField({
			id : "serialId",
			required : true,
			rules : ["isNum", "maxLength[11]"],
			messageTargetId : "serialId"
		});
		//
		formProxy.addField({
			id : "makeYear",
			rules : ["isNum", "maxLength[11]"],
			messageTargetId : "makeYear"
		});
		formProxy.addField({
			id : "sweptVol",
			rules : ["isNum", "maxLength[10]"],
			messageTargetId : "sweptVol"
		});
		formProxy.addField({
			id : "style",
			rules : ["maxLength[30]"],
			messageTargetId : "style"
		});
		//
		formProxy.addField({
			id : "refId",
			rules : ["isNum", "maxLength[11]"],
			messageTargetId : "refId"
		});
		//
		formProxy.addField({
			id : "refName",
			rules : ["maxLength[60]"],
			messageTargetId : "refName"
		});
		
		//---------------------------业务---------------------------------
		//打开新增对话框
		function openAddDlg() {
			curAction = "add";
			//
			toShowTheDlg();
		}
		//打开查看对话框
		function openShowDlg(carModelId) {
			curAction = "view";
			//
			toShowTheDlg(carModelId);
		}
		//打开查看对话框
		function openEditDlg(carModelId) {
			curAction = "mod";
			//
			toShowTheDlg(carModelId);
		}
		//（真正）显示对话款
		function toShowTheDlg(dataId) {
			//
			var theDlgId = "userDlg";
			jqDlgDom = $id(theDlgId);
			//对话框配置
			var dlgConfig = {
				width : Math.min(600, $window.width()),
				height : Math.min(500, $window.height()),
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
						goAddcarModel(postData);
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
						goEditcarModel(postData);
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
			$("#carModelLogo").attr("src", "");
			$("#userDlg input[type=text]").val("");
			$("#userDlg textarea").val("");
			textSet("carModelId", "");
			//获取车辆品牌
			loadCarBrand();
			//
			if (curAction == "view" || curAction == "mod") {
				var data = carModelGridHelper.getRowData(dataId);
				if (data != null) {
					textSet("carModelId", data.id);
					textSet("name", data.name);
					textSet("makeYear", data.makeYear);
					textSet("sweptVol", data.sweptVol);
					textSet("style", data.style);
					textSet("refId", data.refId);
					textSet("refName", data.refName);
				}
				//加载品牌车系
				loadCarBrandSerial(data.serialId);
			}else{
				loadSelectData("serialId", "");
			}
			// 根据车型加载车系
			$("#brandId").change(function() {
				var brandId = textGet("brandId");
				loadCarSerial(brandId);
			});
		}
		
		function goAddcarModel(shopInfoMap){
			var hintBox = Layer.progress("正在保存数据...");
			
			var ajax = Ajax.post("/car/carModel/add/do");
			//
			shopInfoMap["fullName"] = $("#brandId option:selected").text() + $("#serialId option:selected").text() + textGet("name");
			ajax.data(shopInfoMap);
			//
			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					
					carModelGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
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
		
		function goEditcarModel(shopInfoMap){
			var hintBox = Layer.progress("正在保存数据...");
			
			var ajax = Ajax.post("/car/carModel/edit/do");
			//
			shopInfoMap["fullName"] = $("#brandId option:selected").text() + $("#serialId option:selected").text() + textGet("name");
			shopInfoMap["id"] = textGet("carModelId");
			ajax.data(shopInfoMap);
			//
			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					
					carModelGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
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
			var carModelMap = carModelGridHelper.getRowData(id)
			var postData = {
				id : carModelMap.id,
				disabled : carModelMap.disabled
			};
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/car/carModel/isDisabled/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					carModelGridCtrl.jqGrid("setGridParam", {
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
		
		function loadCarBrandSerial(serialId){
			// 隐藏页面区
			var ajax = Ajax.post("/car/carSerial/get/by/id");
			ajax.data({
				id : serialId
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var carSerial = result.data
					if(carSerial){
						//获取车辆品牌
						loadCarBrand(function(){
							textSet("brandId", carSerial.brandId);
						});
						//获取车辆系列
						loadCarSerial(carSerial.brandId, function(){
							textSet("serialId", carSerial.id);
						});
					}
					
				} else {
				}
			});
			ajax.go();
		}
		
		//------------------------调整控件大小-------------------------------------------------------------------

		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "carModelGridCtrl";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("carModelPager").height();
			carModelGridCtrl.setGridWidth(mainWidth - 1);
			carModelGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 60);
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
			loadCarModelData();
			//添加车辆型号
			$id("btnAdd").click(openAddDlg);
			//绑定查询按钮
			$id("btnToQuery").click(function() {
				
				var filter = {};
				var name = textGet("queryName");
				if(name){
					filter.name = name;
				}
				
				//加载jqGridCtrl
				carModelGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			});
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
			
		});
	</script>
</body>
</html>