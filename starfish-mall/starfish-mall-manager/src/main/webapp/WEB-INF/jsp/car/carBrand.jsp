<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>车辆品牌管理</title>
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
					<label class="label">品牌名称</label> <input id="queryName"
						class="normal one half wide input" /> <span class="spacer"></span> 
					<button id="btnToQuery" class="normal button">查询</button>
				</div>
			</div>
		</div>
		<table id="carBrandGridCtrl"></table>
		<div id="carBrandPager"></div>
	</div>
	<!-- 	隐藏Dialog -->
	<div id="userDlg" style="display: none;">
		<div id="addForm" class="form">
			<div id="userList" class="noBorder" style="margin-left: 20px;">
				<div id="singleCarBrand" style="margin-left: 20px;">
					<input type="hidden" class="field value one half wide" id="carBrandId" >
					<div class="field row" style="margin-top: 10px;">
						<label class="field label required one half wide">品牌名称</label>
						<input type="text" class="field value one half wide" id="name" >
					</div>
					<div class="field row">
						<label class="field label one half wide">品牌Logo</label> 
						<input name="file" type="file" id="fileToUploadFileLogo" multiple="multiple" class="field value one half wide"  /> 
						<button class="normal button" id="btnfileToUploadFile">上传</button>
					</div>
					<div class="field row" style="height: 80px">
						<input type="hidden"  id="logoUuid" />
						<input type="hidden"  id="logoUsage" />
						<input type="hidden"  id="logoPath" />
						<label class="field label one half wide">图片预览</label>
			        	<img id="carBrandLogo" height="80px" width="120px" src="<%=resBaseUrl%>/image-app/img-none.jpg"/>
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
	<script type="text/javascript">
		var carBrandGridCtrl = null;
		//
		var carBrandGridHelper = JqGridHelper.newOne("");
		//
		var formProxy = FormProxy.newOne();
	
		//加载合作店列表数据
		function loadCarBrandData(){
			var postData = {
			};
			//加载合作店
			carBrandGridCtrl= $id("carBrandGridCtrl").jqGrid({
			      url : getAppUrl("/car/carBrand/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : 250,
				  width : "100%",
			      colNames : ["id", "品牌名称", "品牌logo", "拼音", "首字母", "状态", "操作"],  
			      colModel : [
							{
								name : "id",
								index : "id",
								hidden : true
							},
							{
								name : "name",
								index : "name",
								width : 200,
								align : 'center'
							},
							{
								name : "fileBrowseUrl",
								align : 'center',
								formatter : function(cellValue,
										option, rowObject) {
									return "<img src="+ rowObject.fileBrowseUrl + "?" + new Date().getTime() +" height='60px' weight='60px' >" || "" ;
							}},
							{
								name : "py",
								index : "py",
								width : 100,
								align : 'center',
							},
							{
								name : "zm",
								index : "zm",
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
									var carBrand = "[<a class='item' href='javascript:void(0);' onclick='openShowDlg(" + cellValue + ")' >查看</a>]<span class='chs spaceholder'></span>"
													+ "[<a class='item' href='javascript:void(0);' onclick='openEditDlg(" + cellValue + ")' >编辑</a>]<span class='chs spaceholder'></span>";
									if (rowObject.disabled == false) {
										return carBrand + "[<a class='item' href='javascript:void(0);' onclick='openOrClose(" + cellValue + ")' >禁用</a>]<span class='chs spaceholder'></span>" ;
									} else {
										return carBrand + "[<a class='item' href='javascript:void(0);' onclick='openOrClose(" + cellValue + ")' >开启</a>]<span class='chs spaceholder'></span>" ;
									}
								},
								width : 200,
								align : "center"
							}],  
				loadtext: "Loading....",
				multiselect : true,// 定义是否可以多选
				multikey:'ctrlKey',
				pager : "#carBrandPager",
				loadComplete : function(gridData){
					carBrandGridHelper.cacheData(gridData);
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
			id : "logoUuid",
			messageTargetId : "logoUuid"
		});
		//
		formProxy.addField({
			id : "logoUsage",
			messageTargetId : "logoUsage"
		});
		//
		formProxy.addField({
			id : "logoPath",
			messageTargetId : "logoPath"
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
			var gridCtrlId = "carBrandGridCtrl";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("carBrandPager").height();
			carBrandGridCtrl.setGridWidth(mainWidth - 1);
			carBrandGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 60);
		}
		//---------------------------业务---------------------------------
		//打开新增对话框
		function openAddDlg() {
			curAction = "add";
			//
			toShowTheDlg();
		}
		//打开查看对话框
		function openShowDlg(carBrandId) {
			curAction = "view";
			//
			toShowTheDlg(carBrandId);
		}
		//打开查看对话框
		function openEditDlg(carBrandId) {
			curAction = "mod";
			//
			toShowTheDlg(carBrandId);
		}
		//（真正）显示对话款
		function toShowTheDlg(dataId) {
			//
			var theDlgId = "userDlg";
			jqDlgDom = $id(theDlgId);
			//对话框配置
			var dlgConfig = {
				width : Math.min(600, $window.width()),
				height : Math.min(450, $window.height()),
				modal : true,
				open : false
			};
			//
			if (curAction == "add") {
				dlgConfig.title = "车辆品牌 - 新增";
				dlgConfig.buttons = {
					"保存" : function() {
						//收集并验证要提交的数据（如果验证不通过直接返回 return）
						var vldResult = formProxy.validateAll();
						if (!vldResult) {
							return;
						}
						var postData = formProxy.getValues();
						//
						goAddCarBrand(postData);
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
				dlgConfig.title = "车辆品牌 - 修改";
				dlgConfig.buttons = {
					"保存" : function() {
						//收集并验证要提交的数据（如果验证不通过直接返回 return）
						var vldResult = formProxy.validateAll();
						if (!vldResult) {
							return;
						}
						var postData = formProxy.getValues();
						//
						goEditCarBrand(postData);
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
				dlgConfig.title = "车辆品牌 - 查看";
				dlgConfig.buttons = {
					//"修改 > " : function() {
						//$(this).dialog("close");
						//
						//openEditDlg(dataId);
					//},
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
			$("#carBrandLogo").attr("src", "");
			$("#userDlg input[type=text]").val("");
			$("#userDlg textarea").val("");
			textSet("carBrandId", "");
			//
			if (curAction == "view" || curAction == "mod") {
				var data = carBrandGridHelper.getRowData(dataId);
				if (data != null) {
					textSet("carBrandId", data.id);
					textSet("name", data.name);
					textSet("logoUuid", data.logoUuid);
					textSet("logoUsage", data.logoUsage);
					textSet("logoPath", data.logoPath);
					$("#carBrandLogo").attr("src", data.fileBrowseUrl);
					textSet("refId", data.refId);
					textSet("refName", data.refName);
				}
			}
		}
		
		function goAddCarBrand(shopInfoMap){
			var hintBox = Layer.progress("正在保存数据...");
			
			var ajax = Ajax.post("/car/carBrand/add/do");
			ajax.data(shopInfoMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					
					carBrandGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
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
		
		function goEditCarBrand(shopInfoMap){
			var hintBox = Layer.progress("正在保存数据...");
			
			var ajax = Ajax.post("/car/carBrand/edit/do");
			shopInfoMap["id"] = textGet("carBrandId");
			ajax.data(shopInfoMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					
					carBrandGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
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
			var carBrandMap = carBrandGridHelper.getRowData(id)
			var postData = {
				id : carBrandMap.id,
				disabled : carBrandMap.disabled
			};
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/car/carBrand/isDisabled/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					carBrandGridCtrl.jqGrid("setGridParam", {
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
						$id("logoUuid").val(resultInfo.data.files[0].fileUuid);
						$id("logoUsage").val(resultInfo.data.files[0].fileUsage);
						$id("logoPath").val(resultInfo.data.files[0].fileRelPath);
						$("#carBrandLogo").attr("src", resultInfo.data.files[0].fileBrowseUrl + "?" + new Date().getTime());
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
			loadCarBrandData();
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
			//添加车辆品牌
			$id("btnAdd").click(openAddDlg);
			//上传Logo
			initFileUpload("fileToUploadFileLogo");
			//绑定修改模块上传按钮
			$id("btnfileToUploadFile").click(function() {
				fileToUploadFileIcon("fileToUploadFileLogo");
			});
			
			//绑定查询按钮
			$id("btnToQuery").click(function() {
				
				var filter = {};
				var name = textGet("queryName");
				if(name){
					filter.name = name;
				}
				
				//加载jqGridCtrl
				carBrandGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			});
		});
	</script>
</body>
</html>