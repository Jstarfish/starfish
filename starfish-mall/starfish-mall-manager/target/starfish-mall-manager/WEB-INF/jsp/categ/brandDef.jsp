<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>品牌定义</title>
</head>

<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnAdd" class="button">添加</button>
				</div>
				
				<div class="group right aligned">
					<label class="label">名称</label> <input id="queryName" class="input one half wide" /> 
					<span class="spacer"></span> 
					<label class="label">代码</label> <input id="queryCode" class="input one half wide" /> 
					<span class="spacer two wide"></span> 
					<span class="vt divider"></span>
					<span class="spacer two wide"></span>
					<button id="btnQuery" class="button">查询</button>
				</div>
			</div>
		</div>
	</div>
	
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<table id="brandDefGrid"></table>
		<div id="brandDefPager"></div>
	</div>

	<!-- 页面弹出框，涉及查看、添加、修改 -->
	<div id="brandDefDialog" style="display: none;">
		<input type="hidden" id="id"/>
		<input type="hidden"  id="logoUuid" />
		<input type="hidden"  id="logoUsage" />
		<input type="hidden"  id="logoPath" />
		<input type="hidden"  id="fileBrowseUrl" />
		<div class="form">
			<div class="field row">
				<label class="field label wide required">名称</label>
				<input class="field value two wide" id="name" onblur="changePy(this)"/>
			</div>
			<div class="field row">
				<label class="field label required">代码</label>
				<input class="field value two wide" id="code" />
			</div>
			<div class="field row">
				<label class="field label required">简拼</label>
				<input class="field value two wide" id="py" disabled="disabled"/>
			</div>
			<div class="field row" id="logo">
				<label class="field label">品牌Logo</label>
				<input name="file" type="file" id="fileToUploadFileIcon" multiple="multiple" class="field value one half wide"  /> 
			</div>
			<div class="field row" id="logo">
				<label class="field label">&nbsp;</label>
				<button class="normal button" id="btnUpload">上传</button>
			</div>
			<div class="field row">
				<label class="field label">预览</label>
				<img id="brandIconImg" />
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		//缓存当前jqGrid数据行数组
		var brandDefGridHelper = JqGridHelper.newOne("");
		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			//
			var gridCtrlId = "brandDefGrid";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("brandDefPager").height();
			jqGridCtrl.setGridWidth(mainWidth - 1);
			jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3);
		}
		
		function changePy(obj){
			if(method!="show"){
				var chsStr = $id("name").val();
				if(chsStr.trim().length>0){
					chsToPinyin(chsStr, true, function(result, jqXhr) {
						if (result.type == "info") {
							$id("py").val(result.data);
						} else {
							Layer.msgWarning(result.message);
						}
					});
				}
			}
		}
		
		var jqGridCtrl = null;
		// 初始化弹出框
		var brandDialog;
		
		var method = "";
		// 创建表单代理并填充数据
		var formProxy = FormProxy.newOne();
		formProxy.addField({
			id : "name",
			required : true,
			rules : [ "maxLength[10]" ]
		});
		formProxy.addField({
			id : "code",
			required : true,
			rules : [ "maxLength[30]",
			         {
				rule : function(idOrName, type, rawValue, curData) {
					checkCode(rawValue);
					return !codeFlag;
				},
				message : "代码已存在！"
			} ]
		});
		formProxy.addField({
			id : "py"
		});
		formProxy.addField({
			id : "logoPath"
		});
		formProxy.addField({
			id : "logoUuid"
		});
		formProxy.addField({
			id : "logoUsage"
		});
		formProxy.addField({
			id : "id"
		});
		formProxy.addField({
			id : "fileBrowseUrl"
		});
		
		//检验手机号码是否唯一
		var codeFlag = false;
		var brandObj = "";
		function checkCode(code) {
			var id = brandObj.id;
			var addFlag = (id == null || id == "");
			var updateFlag = (id != null && id != "" && code != brandObj.code);
			if(addFlag || updateFlag){
				var ajax = Ajax.post("/categ/brandDef/exist/by/code/do");
				ajax.data(code);
				ajax.sync();
				ajax.done(function(result, jqXhr) {
					codeFlag = result.data;
				});
				ajax.fail(function() {
					codeFlag = false;
				});
				ajax.go();
			}
		}
		
		function fileToUploadFileIcon() {
			var uuid = $id("logoUuid").val();
			if(isNoB(uuid)){
				var formData = {
					usage : "image.logo",
					subUsage : "brand"
				};
			}else{
				var formData ={
					update : true,
					uuid : uuid
				};
			}
			sendFileUpload("fileToUploadFileIcon", {
				url : getAppUrl("/file/upload"),
				dataType : "json",
				//自定义数据
				formData :formData ,
				done : function(e, result) {
					var resultInfo = result.result;
					if(resultInfo.type=="info"){
						$id("logoUuid").val(resultInfo.data.files[0].fileUuid);
						$id("logoUsage").val(resultInfo.data.files[0].fileUsage);
						$id("logoPath").val(resultInfo.data.files[0].fileRelPath);
						$id("fileBrowseUrl").val(resultInfo.data.files[0].fileBrowseUrl);
						$("#brandIconImg").attr("src",resultInfo.data.files[0].fileBrowseUrl+"?"+new Date().getTime());
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
		
		// 初始化数据
		function loadGridData() {
			var name = $id("queryName").val();
			var code = $id("queryCode").val();
			var postData = {
				name : name,
				code : code
			};
			jqGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(postData,true)}}).trigger("reloadGrid");
		}
		
		function openAddDialog(){
			method = "add";
			$id("logo").show();
			brandDialog = $("#brandDefDialog").dialog({
				autoOpen : false,
				title : "添加品牌",
				height : Math.min(450, $(window).height()),
				width : Math.min(450, $(window).width()),
				modal : true,
				buttons : {
					"保存" : function() {
						addBrandDef();
					},
					"取消" : function() {
						formProxy.hideMessages();
						brandDialog.dialog("close");
					}
				},
				close : function() {
					formProxy.hideMessages();
					brandDialog.dialog("close");
				}
			});
			$("#brandDefDialog input").prop("disabled", false);
			$("#brandDefDialog #py").prop("disabled", true);
			$("#brandDefDialog button").prop("disabled", false);
			$("#brandDefDialog input").val("");
			$("#brandIconImg").attr("src","");
			
			var wrapperId = "wrapper-fileToUploadFileIcon";
			var jqWrapper = $id(wrapperId);
			jqWrapper.find('label').text("");
			
			brandDialog.dialog("open");
		}
		
		function openShowDialog(obj){
			method = "show";
			$id("logo").hide();
			$("#brandDefDialog input").val("");
			$("#brandIconImg").attr("src","");
			
			var wrapperId = "wrapper-fileToUploadFileIcon";
			var jqWrapper = $id(wrapperId);
			jqWrapper.find('label').text("");
			
			brandDialog = $("#brandDefDialog").dialog({
				autoOpen : false,
				title : "查看品牌",
				height : Math.min(450, $(window).height()),
				width : Math.min(450, $(window).width()),
				modal : true,
				buttons : {
					"继续添加" : function() {
						openAddDialog();
					},
					"修改 >" : function() {
						openUpdateDialog(obj);
					},
					"关闭" : function() {
						brandDialog.dialog("close");
					}
				},
				close : function() {
					formProxy.hideMessages();
					loadGridData();
					brandDialog.dialog("close");
				}
			});
			formProxy.setValues(obj);
			$("#brandIconImg").attr("src",obj.fileBrowseUrl+"?"+new Date().getTime());
			$("#brandDefDialog input").prop("disabled", true);
			$("#brandDefDialog button").prop("disabled", true);
			brandDialog.dialog("open");
		}
		
		function openUpdateDialog(obj){
			method = "update";
			brandObj = obj;
			$id("logo").show();
			$("#brandDefDialog input").val("");
			$("#brandIconImg").attr("src","");
			
			var wrapperId = "wrapper-fileToUploadFileIcon";
			var jqWrapper = $id(wrapperId);
			jqWrapper.find('label').text("");
			
			brandDialog = $("#brandDefDialog").dialog({
				autoOpen : false,
				title : "修改品牌",
				height : Math.min(450, $(window).height()),
				width : Math.min(450, $(window).width()),
				modal : true,
				buttons : {
					"保存" : function() {
						updateBrandDef();
					},
					"关闭" : function() {
						formProxy.hideMessages();
						brandDialog.dialog("close");
					}
				},
				close : function() {
					formProxy.hideMessages();
					brandDialog.dialog("close");
					loadGridData();
				}
			});
			formProxy.setValues(obj);
			$("#brandIconImg").attr("src",formProxy.getValue("fileBrowseUrl")+"?"+new Date().getTime());
			$("#brandDefDialog input").prop("disabled", false);
			$("#brandDefDialog button").prop("disabled", false);
			$("#brandDefDialog #py").prop("disabled", true);
			brandDialog.dialog("open");
		}
		
		// 添加对话框
		function addBrandDef() {
			var vldResult = formProxy.validateAll();
			if (!vldResult) {
				return;
			}
			
			if(isNoB(formProxy.getValue("logoPath"))){
				var theLayer = Layer.confirm('您还没有上传品牌图片，确定保存吗？', function() {
					var hintBox = Layer.progress("正在保存数据...");
					
					var postData = formProxy.getValues();
					var ajax = Ajax.post("/categ/brandDef/create/do");
					ajax.data(postData);
					ajax.done(function(result, jqXhr) {
						if (result.type == "info") {
							Layer.msgSuccess(result.message);
							brandDialog.dialog("close");
							openShowDialog(result.data);
						} else {
							Layer.msgWarning(result.message);
						}
					});
					ajax.always(function() {
						hintBox.hide();
					});
					ajax.go();
				});
			}else{
				var hintBox = Layer.progress("正在保存数据...");
				
				var postData = formProxy.getValues();
				var ajax = Ajax.post("/categ/brandDef/create/do");
				ajax.data(postData);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						brandDialog.dialog("close");
						openShowDialog(result.data);
					} else {
						Layer.msgWarning(result.message);
					}
				});
				ajax.always(function() {
					hintBox.hide();
				});
				ajax.go();
			}
			
		}

		// 修改对话框
		function updateBrandDef() {
			var vldResult = formProxy.validateAll();
			if (!vldResult) {
				return;
			}
			if(isNoB(formProxy.getValue("logoPath"))){
				var theLayer = Layer.confirm('您还没有上传品牌图片，确定保存吗？', function() {
					var hintBox = Layer.progress("正在保存数据...");
					
					var postData = formProxy.getValues();
					var ajax = Ajax.post("/categ/brandDef/update/do");
					ajax.data(postData);
					ajax.done(function(result, jqXhr) {
						if (result.type == "info") {
							Layer.msgSuccess(result.message);
							brandDialog.dialog("close");
							openShowDialog(result.data);
						} else {
							Layer.msgWarning(result.message);
						}
					});
					ajax.always(function() {
						hintBox.hide();
					});
					ajax.go();
				});
			}else{
				var hintBox = Layer.progress("正在保存数据...");
				
				var postData = formProxy.getValues();
				var ajax = Ajax.post("/categ/brandDef/update/do");
				ajax.data(postData);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						brandDialog.dialog("close");
						openShowDialog(result.data);
					} else {
						Layer.msgWarning(result.message);
					}
				});
				ajax.always(function() {
					hintBox.hide();
				});
				ajax.go();
			}
		}
		//
		function deleteBrandDef(id){
			var ajax = Ajax.post("/categ/brandDef/delete/do");
			var postData = {
				id : ParseInt(id)
			};
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					// 加载最新数据列表
					loadGridData();
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
		}
		//
		function checkInUse(id){
			var ajax = Ajax.post("/categ/brandDef/check/inUse/get");
			var postData = {
				id : ParseInt(id)
			};
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var inUse = result.data;
					if(inUse){
						Toast.show("亲，该品牌正在被商品分类使用，不能删除哦~", 3000, "warning");
					}else{
						deleteBrandDef(id);
					}
				}
			});
			ajax.go();
		}
		// 删除
		function goDelBrandDef(id) {
			var theLayer = Layer.confirm('确定要删除该品牌吗？', function() {
				theLayer.hide();
				checkInUse(id);
			});
		}
		//------------------------初始化-------------------------
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
			//初始化数据表格
			jqGridCtrl = $("#brandDefGrid").jqGrid({
				height : "100%",
				width : "100%",
				url : getAppUrl("/categ/brandDef/list/get"),
				contentType : 'application/json',
				mtype : "post",
				datatype : 'json',
				colNames : [ "id","名称","代码","简拼","更新时间","操作" ],
				colModel : [ {name : "id",index:"id",hidden : true}, 
				             {name : "name",width : 300,align : "left"},
				             {name : "code",width : 100,align : "left"}, 
				             {name : "py",width : 100,align : "left"},
				             {name : "ts",width : 150,align : "left"},
				             {name : 'id',width : 200,align : "center",
				            	 formatter : function(cellValue, option, rowObject) {
									return "<span><a href='javascript:void(0);' onclick='openShowDialog("
											+ objToJsonStr(rowObject)
											+ ")' >查看</a></span>"
											+ "<span> <a style='margin-left:12px;' href='javascript:void(0);' onclick='openUpdateDialog("
											+ objToJsonStr(rowObject)
											+ ")' >修改</a></span>"
											+ "<span> <a style='margin-left:12px;' href='javascript:void(0);' onclick='goDelBrandDef("
											+ cellValue
											+ ")' >删除</a></span>";
								}
				             } ],
				multiselect : true, // 定义是否可以多选
				multikey:'ctrlKey',
				pager : "#brandDefPager", // 分页div
				loadComplete : function(gridData) { // JqGridHelper缓存最新的grid数据
					brandDefGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if (isFunction(callback)) {
						callback();
					}
				},
				ondblClickRow: function(rowId) {
					var userMap = brandDefGridHelper.getRowData(rowId)
					openShowDialog(userMap);
				}
			});
			
			function getCallbackAfterGridLoaded(){}
			
			//文件上传
			initFileUpload("fileToUploadFileIcon");
			
			var size = getImageSizeDef("image.logo");
			$id("brandIconImg").attr("width",size.width);
			$id("brandIconImg").attr("height",size.height);
			// 添加
			$id("btnAdd").click(openAddDialog);
			//绑定上传按钮
			$id("btnUpload").click(fileToUploadFileIcon);
			// 使用名称过滤查询
			$id("btnQuery").click(loadGridData);
			
			winSizeMonitor.start(adjustCtrlsSize);
		});
	</script>
</body>
</html>