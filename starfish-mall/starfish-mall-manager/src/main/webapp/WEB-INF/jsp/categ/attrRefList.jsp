<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>属性参照</title>
<style type="text/css">
	.ui-jqgrid tr.jqgrow td {
	  white-space: normal !important;
	  height:auto;
	  vertical-align:text-top;
	  padding-top:2px;
	 }
</style>
</head>
<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding: 4px; vertical-align: bottom;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnAdd" class="button">添加</button>
					<span class="spacer"></span>
					<button id="btnDelBatch" class="button">批量删除</button>
				</div>
				<div class="group right aligned">
					<label class="label">名称</label>
					<input id="attrName" class="input one wide"><span class="spacer"></span>
					<label class="label one wide">内容类型</label>
					<select class="input one wide" id="attrType"></select><span class="spacer"></span>
					<button id="btnQuery" class="normal button">查询</button>
				</div>
			</div>
		</div>
	</div>
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<table id="attrRefList"></table>
		<div id="attrRefPager"></div>
	</div>
	<div id="attrRefDialog"></div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		var attrRefGrid;
		var curAction;
		var attrRefDialog;
		var formProxy = FormProxy.newOne();
		var searchFormProxy = FormProxy.newOne();
		var tempAttr = null;
		tempId = null;
		//名字唯一标识
		var nameFlag = true;
		searchFormProxy.addField({
			id : "attrName",
			rules : [ "maxLength[10]" ]
		});
		//类型
		var attrType = getDictSelectList("attrType","","-全部-");
		var type = getDictSelectList("dataType","","-请选择-");
		//加载数据列表
		function loadData() {
			var gridConfig = {
				url : getAppUrl("/categ/attrRef/list/get"),
				contentType : 'application/json',
				mtype : "post",
				datatype : 'json',
				colNames : [ "id", "名称", "内容类型", "code", "描述",
						"操作"],
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
							name : "type",
							width : 100,
							align : 'left',
							formatter : function(cellValue,
									option, rowObject) {
								return $("#attrType option[value="+ cellValue+ "]").text();
							}
						},
						{
							name : "code",
							width : 100,
							align : 'left'
						},
						{
							name : "desc",
							width : 150,
							align : 'left'
						},
						{
							name : 'id',
							index : 'id',
							formatter : function(cellValue,
									option, rowObject) {
								return "<span> [<a href='javascript:void(0);' onclick='attrRefDialogView("
										+ JSON.stringify(rowObject)
										+ ")' >查看</a>]   [<a href='javascript:void(0);' onclick='attrRefDialogEdit("
										+ JSON.stringify(rowObject)
										+ ")' >修改</a>]  [<a href='javascript:void(0);' onclick='goDeleteAttr("
										+ cellValue
										+ ")' >删除</a>] ";
							},
							width : 150,
							align : "center"
						}],
				multiselect : true,
				multikey:'ctrlKey',
				pager : "#attrRefPager",
				height : "auto"
			};
			//参数依次为jqGridId, jqGridConfig, gridHelper
			attrRefGrid = initMyJqGrid("attrRefList", gridConfig, gridHelper);
		}
		//
		var gridHelper = JqGridHelper.newOne("");
		//初始化dialogAdd
		function initDialogAdd() {
			$("div[aria-describedby='attrRefDialog']").find("span[class='ui-dialog-title']").html("添加属性参照");
			//
			attrRefDialog = $id("attrRefDialog").dialog({
				autoOpen : false,
				height : Math.min(300, $window.height()),
				width : Math.min(500, $window.width()),
				modal : true,
				buttons : {
					"保存" : defaultMethod,
					"取消" : function() {
						clearDialog();
						attrRefDialog.dialog("close");
					}
				},
				close : function() {
					clearDialog();
				}
			});
		}
		//初始化dialogView
		function initDialogView() {
			$("div[aria-describedby='attrRefDialog']").find("span[class='ui-dialog-title']").html("查看属性参照");
			//
			attrRefDialog = $id("attrRefDialog").dialog({
				autoOpen : false,
				height : Math.min(300, $window.height()),
				width : Math.min(500, $window.width()),
				modal : true,
				buttons : {
					"继续添加" : attrRefDialogAdd,
					"修改" : defaultMethod,
					"关闭" : function() {
						clearDialog();
						attrRefDialog.dialog("close");
					}
				},
				close : function() {
					clearDialog();
				}
			});
		}
		//初始化dialogEdit
		function initDialogEdit() {
			$("div[aria-describedby='attrRefDialog']").find("span[class='ui-dialog-title']").html("修改属性参照");
			//
			attrRefDialog = $id("attrRefDialog").dialog({
				autoOpen : false,
				height : Math.min(300, $window.height()),
				width : Math.min(500, $window.width()),
				modal : true,
				buttons : {
					"保存" : defaultMethod,
					"取消" : function() {
						clearDialog();
						attrRefDialog.dialog("close");
					}
				},
				close : function() {
					clearDialog();
				}
			});
		}

		//
		function defaultMethod() {
			if (curAction == "add") {
				addAttrRef();
			}
			if (curAction == "edit") {
				updateAttrRef();
			}
			if (curAction == "view") {
				attrRefDialogEdit(tempAttr);
			}
		}

		function attrRefDialogAdd() {
			clearDialog();
			curAction = "add";
			initDialogAdd();
			attrRefDialog.dialog("open");
		}

		//
		function attrRefDialogView(obj) {
			clearDialog();
			curAction = "view";
			tempAttr = obj;
			$id("name").val(tempAttr.name);
			$id("type").val(tempAttr.type);
			$id("desc").val(tempAttr.desc);
			initDialogView();
			$("input", "#attrRefDialog").attr("disabled", true);
			$("select", "#attrRefDialog").attr("disabled", true);
			$("textarea", "#attrRefDialog").attr("disabled", true);			
			if (tempAttr.type == "Enum") {
				havaBrandAttr();
				if (tempAttr.brandFlag) {
					$id("brand").attr("checked", "true");
					$id("brandGroup").show();
					$id("multiGroup").hide();
				}
				if (tempAttr.multiFlag) {
					$id("multi").attr("checked", "true");
					$id("brandGroup").hide();
					$id("multiGroup").show();
				}
			}
			attrRefDialog.dialog("open");
		}

		//
		function attrRefDialogEdit(obj) {
			clearDialog();
			tempAttr = obj;
			tempId = obj.id;
			curAction = "edit";
			initDialogEdit();
			$id("name").val(tempAttr.name);
			$id("type").val(tempAttr.type);
			$id("id").val(tempAttr.id);
			$id("desc").val(tempAttr.desc);
			if (tempAttr.type == "Enum") {
				havaBrandAttr();
				if (tempAttr.brandFlag) {
					$id("brand").attr("checked", "true");
					$id("brandGroup").show();
					$id("multiGroup").hide();
				}
				if (tempAttr.multiFlag) {
					$id("multi").attr("checked", "true");
					$id("brandGroup").hide();
					$id("multiGroup").show();
				}
			}
			attrRefDialog.dialog("open");
		}

		//
		function clearDialog() {
			curAction = null;
			tempAttr = null;
			tempId = null;
			formProxy.hideMessages();
			searchFormProxy.hideMessages();
			$id("name").val("");
			$id("type").val("");
			$id("id").val("");
			$id("desc").val("");
			$("input", "#attrRefDialog").attr("disabled", false);
			$("select", "#attrRefDialog").attr("disabled", false);
			$("textarea", "#attrRefDialog").attr("disabled", false);
			$id("brand").removeAttr("checked");
			$id("multi").removeAttr("checked");
			$id("multiGroup").hide();
			$id("brandGroup").hide();
		}

		//
		function initTpl() {
			var attrTplHtml = $id("attrTpl").html();
			var htmlStr = laytpl(attrTplHtml).render({});
			$id("attrRefDialog").html(htmlStr);
			loadSelectData("type", type);
			$id("type").change(changeTypeMethod);
			$id("brand").change(brandChange);
			$id("multi").change(multiChange);
			initDialogAdd();
			formProxy.addField({
				id : "name",
				required : true,
				rules : [ "maxLength[10]", {
					rule : function(idOrName, type, rawValue, curData) {
						if (tempAttr == null) {
							validateName(rawValue);
						} else {
							if (tempAttr.name != rawValue) {
								validateName(rawValue);
							}
						}
						return nameFlag;
					},
					message : "名称被占用！"
				} ]
			});
			formProxy.addField({
				id : "type",
				required : true
			});
			formProxy.addField({
				id : "desc",
				rules : [ "maxLength[250]" ]
			});
		}

		//typeChangeMethod
		function changeTypeMethod() {
			var type = $id("type").val();
			if (type == "Enum") {
				havaBrandAttr();
			} else {
				$id("multiGroup").hide();
				$id("brandGroup").hide();
				$id("brand").removeAttr("checked");
				$id("multi").removeAttr("checked");
			}
		}

		function addAttrRef() {
			var vldResult = formProxy.validateAll();
			if (vldResult) {
				var ajax = Ajax.post("/categ/attrRef/add/do");
				var nameVal = $id("name").val();
				var typeVal = $id("type").val();
				var enumFlagVal = typeVal == "Enum" ? true : false;
				var descVal = $id("desc").val();
				var brandFlagVal = $id("brand").is(':checked') ? 1 : 0;
				var multiFlagVal = $id("multi").is(':checked') ? 1 : 0;
				data = {
					name : nameVal,
					type : typeVal,
					enumFlag : enumFlagVal,
					desc : descVal,
					seqNo : 1,
					brandFlag : brandFlagVal,
					multiFlag : multiFlagVal
				};
				ajax.data(data);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						//加载最新数据列表
						attrRefGrid.jqGrid().trigger("reloadGrid");
						tempAttr = result.data;
						attrRefDialogView(tempAttr);

					} else {
						Layer.msgWarning(result.message);
					}
				});
				ajax.go();
			}
		}

		function updateAttrRef() {
			var vldResult = formProxy.validateAll();
			if (vldResult) {
				var ajax = Ajax.post("/categ/attrRef/update/do");
				var idVal = $id("id").val();
				var nameVal = $id("name").val();
				var typeVal = $id("type").val();
				var enumFlagVal = typeVal == "Enum" ? true : false;
				var descVal = $id("desc").val();
				var brandFlagVal = $id("brand").is(':checked') ? 1 : 0;
				var multiFlagVal = $id("multi").is(':checked') ? 1 : 0;
				data = {
					id : idVal,
					name : nameVal,
					type : typeVal,
					enumFlag : enumFlagVal,
					desc : descVal,
					seqNo : 1,
					brandFlag : brandFlagVal,
					multiFlag : multiFlagVal
				};
				ajax.data(data);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						//加载最新数据列表
						attrRefGrid.jqGrid().trigger("reloadGrid");
						tempAttr = result.data;
						attrRefDialogView(tempAttr);
					} else {
						Layer.msgWarning(result.message);
					}
				});
				ajax.go();
			}
		}

		//检查名字是否可用
		function validateName(name) {
			var ajax = Ajax.post("/categ/attrRef/name/isabel/do");
			ajax.data({
				name : name
			});
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				nameFlag = result.data;
			});
			ajax.go();
		}
		//
		function deleteAttrById(id){
			//
			var hintBox = Layer.progress("正在删除...");
			var ajax = Ajax.post("/categ/attrRef/delete/do");
			ajax.params({
				id : id
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					//
					attrRefGrid.jqGrid().trigger("reloadGrid");
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
		function checkInUse(id){
			var ajax = Ajax.post("/categ/attrRef/check/inUse/get");
			var postData = {
				id : ParseInt(id)
			};
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var inUse = result.data;
					if(inUse){
						Toast.show("亲，该属性正在被商品分类使用，不能删除哦~", 3000, "warning");
					}else{
						deleteAttrById(id);
					}
				}
			});
			ajax.go();
		}
		//
		function goDeleteAttr(id) {
			var theLayer = Layer.confirm('确定要删除该属性吗？', function() {
				theLayer.hide();
				checkInUse(id);
			})
		}

		//
		function deleteBatch() {
			var ids = attrRefGrid.jqGrid("getGridParam", "selarrrow");
			var postData = [];
			for (var i = 0; i < ids.length; i++) {
				postData.add(ParseInt(ids[i]));
			}
			if (ids == "") {
				Toast.show("请选择要删除的数据！");
			} else {
				var theLayer = Layer.confirm('确定要删除选择的数据吗？', function() {
					//
					var hintBox = Layer.progress("正在删除...");
					var ajax = Ajax.post("/categ/attrRef/delete/batch/do");
					ajax.data(postData);
					ajax.done(function(result, jqXhr) {
						if (result.type == "info") {
							Layer.msgSuccess(result.message);
							//
							attrRefGrid.jqGrid().trigger("reloadGrid");
						} else {
							Layer.msgWarning(result.message);
						}
					});
					ajax.always(function() {
						hintBox.hide();
					});
					ajax.go();
				});
			}
		}

		//查询
		function search() {
			var vldResult = searchFormProxy.validateAll();
			if (!vldResult) {
				return;
			}
			var nameVal = $id("attrName").val();
			var typeVal = $id("attrType").val();
			attrRefGrid.jqGrid("setGridParam", {
				postData : {
					filterStr : JSON.encode({
						"name" : nameVal,
						"type" : typeVal
					}, true)
				}
			}).trigger("reloadGrid");
		}

		//是否存在品牌属性
		function havaBrandAttr() {
			var ajax = Ajax.post("/categ/attrRef/brand/is/true");
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data;
					if (data) {
						$id("brandGroup").show();
						$id("multiGroup").show();
					} else {
						$id("brandGroup").hide();
						$id("multiGroup").show();
						if (tempAttr != null && tempAttr.brandFlag) {
							$id("brandGroup").show();
						}
					}
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
		}

		//brandCheckBox、multiCheckBox change
		function brandOrMultiChange() {
			var brandChecked = $id("brand").is(':checked');
			var multiChecked = $id("multi").is(':checked');
			if (brandChecked) {
				$id("multiGroup").hide();
			} else {
				$id("multiGroup").show();
			}
			if (multiChecked) {
				$id("brandGroup").hide();
			} else {
				havaBrandAttr();
			}
		}
		
		//brandCheckBox change
		function brandChange(){
			var brandChecked = $id("brand").is(':checked');
			if (brandChecked) {
				$id("multiGroup").hide();
			} else {
				$id("multiGroup").show();
			}
		}
		
		//multiCheckBox change
		function multiChange(){
			var multiChecked = $id("multi").is(':checked');
			if (multiChecked) {
				$id("brandGroup").hide();
			} else {
				havaBrandAttr();
			}
		}
		
		
		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			//console.log("mainWidth:" + mainWidth + ", " + "mainHeight:" + mainHeight);
			//
			var gridCtrlId = "attrRefList";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv",jqGridBox).height();
			var pagerHeight = $id("attrRefPager").height();
			attrRefGrid.setGridWidth(mainWidth - 1);
			attrRefGrid.setGridHeight(mainHeight - headerHeight - pagerHeight - 3);
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
			//隐藏布局north分割线
			$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
			loadSelectData("attrType", attrType);
			loadData();
			initTpl();
			$id("btnAdd").click(attrRefDialogAdd);
			$id("btnDelBatch").click(deleteBatch);
			$id("btnQuery").click(search);
			//
			attrRefGrid.bindKeys();
			
			winSizeMonitor.start(adjustCtrlsSize);
		});
	</script>
</body>
<!-- layTpl begin -->
<!-- 属性参照模板 -->
<script id="attrTpl" type="text/html">
	<div class="form">
		<input type="hidden" id="id" />
		<div class="field row">
			<label class="field label one wide required">名称</label> 
			<input class="field value one half wide" type="text" id="name" title="属性参照名称唯一" />
		</div>
		<div class="field row">
			<label class="field label one wide required">内容类型</label> 
			<select class="field value one half wide" id="type"></select>
			<div class="field group" id="brandGroup">
				<input type="checkbox" id="brand" /><label for="brand">品牌属性</label>				
			</div>
			<div class="field group" id="multiGroup">
				<input type="checkbox" id="multi" /><label for="multi">可以多选</label>					
			</div>
		</div>
		<div class="field row">
			<label class="field label one wide">描述</label> 
			<textarea class="field value two half wide" id="desc" title="请输入描述" style="height:80px;" />
		</div>
	</div>
</script>
<!-- layTpl end -->
</html>
