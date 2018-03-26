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
					<label class="label">楼层名称</label> <input id="queryName"
						class="normal one wide input" /> <span class="spacer"></span> 
					<label class="label one wide">状态</label>
					<select class="input one wide" id="disabled">
						<option value>请选择</option>
						<option value="0">启用</option>
						<option value="1">禁用</option>
					</select><span class="spacer"></span>
					<button id="btnToQuery" class="normal button">查询</button>
				</div>
			</div>
		</div>
		<table id="salesFloorGridCtrl"></table>
		<div id="salesFloorPager"></div>
	</div>
	<!-- 	隐藏Dialog -->
	<div id="userDlg" style="display: none;">
		<div id="addForm" class="form">
			<div id="userList" class="noBorder" style="margin-left: 20px;">
				<div id="singleSalesFloor" style="margin-left: 20px;">
					<div class="field row" style="margin-top: 10px;">
						<label class="field label required one wide">楼层号</label>
						<input type="text" class="field value one half wide" id="no" > （1-6的楼层号）
						<input type="hidden" class="field value one half wide" id="salesFloorId" >
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label required one wide">楼层名称</label>
						<input type="text" class="field value one half wide" id="name" >
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label one wide required">楼层类型</label>
						<select class="field value one half wide" id="type"></select>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		var salesFloorGridCtrl = null;
		//
		var salesFloorGridHelper = JqGridHelper.newOne("");
		//
		var formProxy = FormProxy.newOne();
		//
		var salesFloorType = getDictSelectList("salesFloorType","","-请选择-");
		loadSelectData("type", salesFloorType);
		//
		//加载销售楼层列表数据
		function loadSalesFloorData(){
			var postData = {
			};
			//加载销售楼层
			salesFloorGridCtrl= $id("salesFloorGridCtrl").jqGrid({
			      url : getAppUrl("/salesFloor/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : 250,
				  width : "100%",
			      colNames : ["楼层号", "名称", "楼层类型", "专区数量", "状态", "操作"],  
			      colModel : [{
								name : "no",
								index : "no",
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
								name : "type",
								width : 100,
								align : 'center',
								formatter : function(cellValue,
										option, rowObject) {
									return $("#type option[value="+ cellValue+ "]").text();
								}
							},
							{
								name : "regionCount",
								index : "regionCount",
								width : 100,
								align : 'center',
							},
							{
								name : "disabled",
								index : "disabled",
								width : 100,
								align : "center",
								formatter : function(cellValue, option, rowObject) {
									if (cellValue == 0) {
										return '启用';
									} else if (cellValue == 1) {
										return "禁用";
									} 
								}
							},
							{
								name : 'no',
								index : 'no',
								formatter : function(cellValue, option, rowObject) {
									var salesFloor = "[<a class='item' href='javascript:void(0);' onclick='openShowDlg(" + cellValue + ")' >查看</a>]<span class='chs spaceholder'></span>"
													+ "[<a class='item' href='javascript:void(0);' onclick='openEditDlg(" + cellValue + ")' >编辑</a>]<span class='chs spaceholder'></span>";
									if (rowObject.disabled == 1) {
										return salesFloor + "[<a class='item' href='javascript:void(0);' onclick='openOrClose(" + cellValue + ")' >启用</a>]<span class='chs spaceholder'></span>" ;
									} else {
										return salesFloor + "[<a class='item' href='javascript:void(0);' onclick='openOrClose(" + cellValue + ")' >禁用</a>]<span class='chs spaceholder'></span>" ;
									}
								},
								width : 200,
								align : "center"
							}],  
				loadtext: "Loading....",
				multiselect : true,// 定义是否可以多选
				multikey:'ctrlKey',
				pager : "#salesFloorPager",
				loadComplete : function(gridData){
					salesFloorGridHelper.cacheData(gridData);
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
		var salesFloorNo = false;
		var salesFloorName = false;
		//
		formProxy.addField({
			id : "no",
			required : true,
			rules : ["isNum", {
				rule : function(idOrName, type, rawValue, curData) {
					if (rawValue < 1 || rawValue > 6 || parseInt(rawValue) != rawValue) {
						return false;
					}
					return true;
				},
				message : "请填写1-6的整数！"
			}, {
				rule : function(idOrName, type, rawValue, curData) {
					checkNo(rawValue);
					if(salesFloorNo){
						return false;
					}
					return true;
				},
				message : "该楼层已存在！"
			}],
			messageTargetId : "no"
		});
		//
		formProxy.addField({
			id : "name",
			required : true,
			rules : ["maxLength[30]", {
				rule : function(idOrName, type, rawValue, curData) {
					checkName(rawValue);
					if(salesFloorName){
						return false;
					}
					return true;
				},
				message : "该名称已存在！"
			}],
			messageTargetId : "name"
		});
		//
		formProxy.addField({
			id : "type",
			required : true,
			messageTargetId : "type"
		});
		
		//------------------------调整控件大小-------------------------------------------------------------------

		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "salesFloorGridCtrl";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("salesFloorPager").height();
			salesFloorGridCtrl.setGridWidth(mainWidth - 1);
			salesFloorGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 60);
		}
		//---------------------------业务---------------------------------
		//打开新增对话框
		function openAddDlg() {
			curAction = "add";
			//
			toShowTheDlg();
		}
		//打开查看对话框
		function openShowDlg(salesFloorId) {
			curAction = "view";
			//
			toShowTheDlg(salesFloorId);
		}
		//打开查看对话框
		function openEditDlg(salesFloorId) {
			curAction = "mod";
			//
			toShowTheDlg(salesFloorId);
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
						goAddSalesFloor(postData);
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
						goEditSalesFloor(postData);
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
			textSet("type", "");
			textSet("salesFloorId", "");
			//
			if (curAction == "view" || curAction == "mod") {
				var data = salesFloorGridHelper.getRowData("no", dataId);
				if (data != null) {
					textSet("salesFloorId", data.no);
					textSet("no", data.no);
					textSet("name", data.name);
					textSet("type", data.type);
				}
			}
		}
		
		function goAddSalesFloor(salesFloorInfoMap){
			var hintBox = Layer.progress("正在保存数据...");
			
			var ajax = Ajax.post("/salesFloor/add/do");
			
			ajax.data(salesFloorInfoMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					
					salesFloorGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
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
		
		function goEditSalesFloor(salesFloorInfoMap){
			var hintBox = Layer.progress("正在保存数据...");
			
			var ajax = Ajax.post("/salesFloor/edit/do");
			salesFloorInfoMap["id"] = textGet("salesFloorId");
			ajax.data(salesFloorInfoMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					
					salesFloorGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
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
			var salesFloorMap = salesFloorGridHelper.getRowData("no", id)
			var postData = {
				id : salesFloorMap.no,
				no : salesFloorMap.no,
				disabled : salesFloorMap.disabled
			};
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/salesFloor/isDisabled/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					salesFloorGridCtrl.jqGrid("setGridParam", {
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
		
		function fileToUploadFileIcon(fileId) {
			var fileUuidToUpdate = $id("IconUuid").val();
			if (isNoB(fileUuidToUpdate)) {
				var formData = {
					usage : "image.logo",
					subUsage : "shop"
				};
			} else {
				var formData = {
					update : true,
					uuid : fileUuidToUpdate
				};
			}
			sendFileUpload(fileId, {
				url : getAppUrl("/file/upload"),
				dataType : "json",
				//自定义数据
				formData : formData,
				done : function(e, result) {
					var resultInfo = result.result;
					if (resultInfo.type == "info") {
						$id("imageUuid").val(resultInfo.data.files[0].fileUuid);
						$id("imageUsage").val(resultInfo.data.files[0].fileUsage);
						$id("imagePath").val(resultInfo.data.files[0].fileRelPath);
						$("#salesFloorImage").attr("src", resultInfo.data.files[0].fileBrowseUrl + "?" + new Date().getTime());
						;
					}
				},
				fail : function(e, data) {
					console.log(data);
				},
				noFilesHandler : function() {
					Layer.msgWarning("请选择或更换图片");
				},
				fileNamesChecker : function(fileNames) {
					fileNames = fileNames || [];
					for (var i = 0; i < fileNames.length; i++) {
						if (!isImageFile(fileNames[i])) {
							Layer.msgWarning("只能上传图片文件");
							return false;
						}
					}
					return true;
				}
			});
		}
		
		function checkNo(no){
			var postData = {
				no : no,
			};
			var id = textGet("salesFloorId")
			if(id){
				postData.id = id;
			}
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/salesFloor/get/by/no");
			ajax.sync();
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					salesFloorNo = true;
				}else{
					salesFloorNo = false;
				}
			});
			ajax.always(function() {
				//隐藏等待提示框
				loaderLayer.hide();
			});
			ajax.go();
		}
		
		function checkName(name){
			var postData = {
				name : name,
			};
			var id = textGet("salesFloorId")
			if(id){
				postData.id = id;
			}
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/salesFloor/get/by/name");
			ajax.sync();
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					salesFloorName = true;
				} else{
					salesFloorName = false;
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
			loadSalesFloorData();
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
			//添加车辆品牌
			$id("btnAdd").click(openAddDlg);
			
			//绑定查询按钮
			$id("btnToQuery").click(function() {
				
				var filter = {};
				var name = textGet("queryName");
				if(name){
					filter.name = name;
				}
				var dataFlag = textGet("dataFlag");
				if(dataFlag){
					filter.targetFlag = dataFlag;
				}
				var disabled = textGet("disabled");
				if(disabled){
					filter.disabled = disabled;
				}
				//加载jqGridCtrl
				salesFloorGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			});
		});
	</script>
</body>
</html>