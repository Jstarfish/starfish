<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>活动管理</title>
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
					<label class="label">活动名称</label> <input id="queryName"
						class="normal one wide input" /> <span class="spacer"></span> 
					<label class="label one wide">发送对象</label>
					<select class="input one wide" id="dataFlag"></select><span class="spacer"></span>
					<label class="label one wide">状态</label>
					<select class="input one wide" id="status">
						<option value>请选择</option>
						<option value="0">未发布</option>
						<option value="1">发布中</option>
						<option value="2">已停止</option>
					</select><span class="spacer"></span>
					<button id="btnToQuery" class="normal button">查询</button>
				</div>
			</div>
		</div>
		<table id="activityGridCtrl"></table>
		<div id="activityPager"></div>
	</div>
	<!-- 	隐藏Dialog -->
	<div id="userDlg" style="display: none;">
		<div id="addForm" class="form">
			<div id="userList" class="noBorder" style="margin-left: 20px;">
				<div id="singleActivity" style="margin-left: 20px;">
					<div class="field row" style="margin-top: 10px;">
						<label class="field label required one wide">活动名称</label>
						<input type="text" class="field value one half wide" id="name" >
						<input type="hidden" class="field value one half wide" id="activityId" >
					</div>
					<div class="field row">
						<label class="field label one wide">活动图片</label> 
						<input name="file" type="file" id="fileToUploadFileimage" multiple="multiple" class="field value one half wide"  /> 
						<button class="normal button" id="btnfileToUploadFile">上传</button>
					</div>
					<div class="field row" style="height: 80px">
						<input type="hidden"  id="imageUuid" />
						<input type="hidden"  id="imageUsage" />
						<input type="hidden"  id="imagePath" />
						<label class="field label one wide">图片预览</label>
			        	<img id="activityImage" height="80px" width="120px" src="<%=resBaseUrl%>/image-app/img-none.jpg"/>
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label one wide required">发送对象</label>
						<select class="field value one half wide" id="targetFlag"></select>
					</div>
					<div class="field row">
						<label class="field label required">活动内容</label>
					</div>
					<div class="field row" id="warnMsg">
						<textarea id="content" name="content" class="field value three wide" style="height:200px;width:490px" maxlength="5000"></textarea>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		var activityGridCtrl = null;
		//
		var activityGridHelper = JqGridHelper.newOne("");
		//
		var formProxy = FormProxy.newOne();
		// 富文本编辑器
		var theEditor = null;
		//
		var activityTarget = getDictSelectList("activityTarget","","-请选择-");
		loadSelectData("targetFlag", activityTarget);
		//加载活动列表数据
		function loadActivityData(){
			var postData = {
			};
			//加载活动
			activityGridCtrl= $id("activityGridCtrl").jqGrid({
			      url : getAppUrl("/market/activity/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : 250,
				  width : "100%",
			      colNames : ["id", "活动名称", "发送对象", "发布时间", "状态", "操作"],  
			      colModel : [{
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
								name : "targetFlag",
								width : 100,
								align : 'center',
								formatter : function(cellValue,
										option, rowObject) {
									return $("#dataFlag option[value="+ cellValue+ "]").text();
								}
							},
							{
								name : "pubTime",
								index : "pubTime",
								width : 100,
								align : 'center',
							},
							{
								name : "status",
								index : "status",
								width : 100,
								align : "center",
								formatter : function(cellValue, option, rowObject) {
									if (cellValue == 0) {
										return '未发布';
									} else if (cellValue == 1) {
										return "发布中";
									} else if (cellValue == 2) {
										return "已停止";
									} 
								}
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									var activity = "[<a class='item' href='javascript:void(0);' onclick='openShowDlg(" + cellValue + ")' >查看</a>]<span class='chs spaceholder'></span>";
									if (rowObject.status == 1) {
										return activity + "[<a class='item' href='javascript:void(0);' onclick='openOrClose(" + cellValue + ")' >停止</a>]<span class='chs spaceholder'></span>" ;
									} else {
										return activity +  "[<a class='item' href='javascript:void(0);' onclick='openEditDlg(" + cellValue + ")' >编辑</a>]<span class='chs spaceholder'></span>"
										+ "[<a class='item' href='javascript:void(0);' onclick='openOrClose(" + cellValue + ")' >发布</a>]<span class='chs spaceholder'></span>"
										+ "[<a class='item' href='javascript:void(0);' onclick='delActivity(" + cellValue + ")' >删除</a>]<span class='chs spaceholder'></span>" ;
									}
								},
								width : 200,
								align : "center"
							}],  
				loadtext: "Loading....",
				multiselect : true,// 定义是否可以多选
				multikey:'ctrlKey',
				pager : "#activityPager",
				loadComplete : function(gridData){
					activityGridHelper.cacheData(gridData);
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
			id : "imageUuid",
			messageTargetId : "imageUuid"
		});
		//
		formProxy.addField({
			id : "imageUsage",
			messageTargetId : "imageUsage"
		});
		//
		formProxy.addField({
			id : "imagePath",
			messageTargetId : "imagePath"
		});
		//
		formProxy.addField({
			id : "targetFlag",
			required : true,
			messageTargetId : "targetFlag"
		});
		
		//------------------------调整控件大小-------------------------------------------------------------------

		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "activityGridCtrl";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("activityPager").height();
			activityGridCtrl.setGridWidth(mainWidth - 1);
			activityGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 60);
		}
		//---------------------------业务---------------------------------
		//打开新增对话框
		function openAddDlg() {
			curAction = "add";
			//
			toShowTheDlg();
		}
		//打开查看对话框
		function openShowDlg(activityId) {
			curAction = "view";
			//
			toShowTheDlg(activityId);
		}
		//打开查看对话框
		function openEditDlg(activityId) {
			curAction = "mod";
			//
			toShowTheDlg(activityId);
		}
		//（真正）显示对话款
		function toShowTheDlg(dataId) {
			//
			var theDlgId = "userDlg";
			jqDlgDom = $id(theDlgId);
			//对话框配置
			var dlgConfig = {
				width : Math.min(800, $window.width()),
				height : Math.min(550, $window.height()),
				modal : true,
				open : false
			};
			//
			if (curAction == "add") {
				dlgConfig.title = "活动 - 新增";
				dlgConfig.buttons = {
					"保存" : function() {
						//收集并验证要提交的数据（如果验证不通过直接返回 return）
						var vldResult = formProxy.validateAll();
						checkContent();
						if (!vldResult) {
							return;
						}
						var postData = formProxy.getValues();
						//
						goAddActivity(postData);
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
				dlgConfig.title = "活动 - 修改";
				dlgConfig.buttons = {
					"保存" : function() {
						//收集并验证要提交的数据（如果验证不通过直接返回 return）
						var vldResult = formProxy.validateAll();
						checkContent();
						if (!vldResult) {
							return;
						}
						var postData = formProxy.getValues();
						//
						goEditActivity(postData);
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
				dlgConfig.title = "活动 - 查看";
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
			$("#activityImage").attr("src", "");
			$("#userDlg input[type=text]").val("");
			$("#userDlg textarea").val("");
			textSet("targetFlag", "");
			textSet("activityId", "");
			if(theEditor==null){
				theEditor = CKEDITOR.replace("content");						
			}
			//
			if (curAction == "view" || curAction == "mod") {
				var data = activityGridHelper.getRowData(dataId);
				if (data != null) {
					textSet("activityId", data.id);
					textSet("name", data.name);
					textSet("imageUuid", data.imageUuid);
					textSet("imageUsage", data.imageUsage);
					textSet("imagePath", data.imagePath);
					$("#activityImage").attr("src", data.fileBrowseUrl);
					textSet("targetFlag", data.targetFlag);
					theEditor.setData(data.content);
				}
			}else{
				theEditor.setData("");
			}
		}
		
		// 检验供应商协议
		function checkContent() {
			var content = theEditor.getData();
			formProxy.addField({
				id : theEditor.id,
				get : function(idOrName, type, curData, asRawVal) {
					return content;
				},
				required : true,
				rules : [ {
					rule : function(idOrName, type, rawValue, curData) {
						// 不计算文本附带的特殊标记占用的8个字符位
						if (content.length > 2008) {
							return false;
						}
						return true;
					},
					message : "长度必须在1到2000个字符之间"
				} ],
				messageTargetId : "warnMsg"
			});
			return formProxy.validateAll();
		}
		
		function goAddActivity(activityInfoMap){
			var hintBox = Layer.progress("正在保存数据...");
			
			var ajax = Ajax.post("/market/activity/add/do");
			
			activityInfoMap["content"] = CKEDITOR.instances["content"].getData();
			ajax.data(activityInfoMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					
					activityGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
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
		
		function goEditActivity(activityInfoMap){
			var hintBox = Layer.progress("正在保存数据...");
			
			var ajax = Ajax.post("/market/activity/edit/do");
			activityInfoMap["id"] = textGet("activityId");
			activityInfoMap["content"] = CKEDITOR.instances["content"].getData();
			ajax.data(activityInfoMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					
					activityGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
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
			var activityMap = activityGridHelper.getRowData(id)
			var postData = {
				id : activityMap.id,
				status : activityMap.status
			};
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/market/activity/isStatus/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					activityGridCtrl.jqGrid("setGridParam", {
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
		
		function delActivity(id){
			var theLayer = Layer.confirm('确定要删除该活动吗？', function() {
				var postData = {
					id : id
				};
				var ajax = Ajax.post("/market/activity/delete/do");
				ajax.data(postData);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						theLayer.hide();
						Layer.msgSuccess(result.message);
						activityGridCtrl.jqGrid("setGridParam", {
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
						$("#activityImage").attr("src", resultInfo.data.files[0].fileBrowseUrl + "?" + new Date().getTime());
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
			loadSelectData("dataFlag", activityTarget);
			//
			loadActivityData();
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
			//添加车辆品牌
			$id("btnAdd").click(openAddDlg);
			//上传image
			initFileUpload("fileToUploadFileimage");
			//绑定修改模块上传按钮
			$id("btnfileToUploadFile").click(function() {
				fileToUploadFileIcon("fileToUploadFileimage");
			});
			
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
				var status = textGet("status");
				if(status){
					filter.status = status;
				}
				//加载jqGridCtrl
				activityGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			});
		});
	</script>
</body>
</html>