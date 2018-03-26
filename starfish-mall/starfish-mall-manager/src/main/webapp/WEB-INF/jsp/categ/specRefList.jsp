<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	<title>规格参照</title>
</head>

<body id="rootPanel">
<!-- 查询条件 -->
<div class="ui-layout-north" style="padding: 4px;" id="topPanel">
	<div class="filter section">
		<div class="filter row">
			<div class="group left aligned">
				<button class="button" id="btnAdd">添加</button>
				<span class="spacer"></span>
				<button class="button" id="btnBatchDel">批量删除</button>
			</div>
			
			<div class="group right aligned">
				<label class="label">名称</label>
				<input class="input two wide" id="conName">
				<span class="spacer"></span>
				<button class="button" id="btnQuery" >查询</button>
			</div>
		</div>
	</div>
</div>
<!-- grid数据及分页 -->
<div class="ui-layout-center" style="padding: 4px;" id="mainPanel">
	<div id="theGridCtrl" class="noBorder">
		<table id="specRefList"></table>
		<div id="specRefPager"></div>
	</div>
</div>

<!-- 页面弹出框，涉及查看、添加、修改 -->
<div id="specRefDialog" style="display: none;">
	<input type="hidden" id="id"/>
	<div class="form">
		<div class="field row" id="nameRow">
			<label class="field label one half wide required">名称</label>
			<input class="field value one half wide" id="name" />
		</div>
		<div class="field row" id="colorFlagRow">
			<label class="field label one half wide required">是否颜色</label>
			<input type="radio" id="colorFlagTrue" name="colorFlag" value="true" />
			<label for="colorFlagTrue">是</label>
			<input type="radio" id="colorFlagFalse" name="colorFlag"  value="false"  checked="checked" />
			<label for="colorFlagFalse">否</label>
		</div>
		<div class="field row">
			<label class="field label one half wide" style="visibility: hidden;">&nbsp;</label>
			<span style="font-size:12px; color:gray; width:360px;">选择“是”，可以使用系统预置的颜色色值表；选择“否”，不可以使用。</span>
		</div>
		<div class="field row" id="salesFlagRow">
			<label class="field label one half wide required">销售专用</label>
			<input type="radio" id="salesFlagTrue" name="salesFlag" value="true" />
			<label for="colorFlagTrue">是</label>
			<input type="radio" id="salesFlagFalse" name="salesFlag"  value="false" checked="checked" />
			<label for="colorFlagFalse">否</label>
		</div>
		<div class="field row" id="descRow" style="height:120px;">
			<label class="field label one half wide">描述</label>
			<textarea class="field value three wide" maxlength="250" style="height:100px; resize:none;" id="desc"></textarea>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
	var jqGridCtrl;
	// 初始化弹出框
	var specRefDialog;
	var isValidate = false;
	var specRefId;
	// 默认弹出框方法是添加
	var method = "add";
	// 缓存当前jqGrid数据行数组
	var gridHelper = JqGridHelper.newOne("");

	// 创建表单代理并填充数据
	var formProxy = FormProxy.newOne();
	formProxy.addField({
		id : "name",
		required : true,
		rules : [ "maxLength[10]" ]
	});
	formProxy.addField({
		id : "colorFlag",
		required : true
	});
	formProxy.addField({
		id : "salesFlag",
		required : true
	});
	formProxy.addField({
		id : "desc",
		rules : [ "maxLength[250]" ]
	});
	
	// 检验弹出框内容
	function checkSpecRef() {
		return formProxy.validateAll();
	}

	// 取弹出框参数值
	function getSpecRefMap() {
		var dataMap = new KeyMap("specRefMap");
		dataMap.add("id", $id("id").val());
		dataMap.add("name", $id("name").val());
		dataMap.add("colorFlag", $("input:radio[name='colorFlag']:checked").val());
		dataMap.add("salesFlag", $("input:radio[name='salesFlag']:checked").val());
		dataMap.add("desc", $id("desc").val());
		return dataMap;
	}

	// 空函数，在弹出框消失后重写调用
	function getCallbackAfterGridLoaded() {
	}

	// 加载弹出框最新数据
	function loadDialogData(specRefId) {
		if (isNoB(specRefId)) {
			Layer.msgWarning("数据加载有误，请重新加载");
			return;
		}

		// 取最新数据
		var curData = gridHelper.getRowData(specRefId);
		if(curData == null){
			var ajax = Ajax.post("/categ/spec/ref/get");
			ajax.sync(true);
			ajax.data({id: specRefId});
			ajax.done(function(result, jqXhr) {
				var data = result.data;
				if (result.type == "info" && data != null) {
					fillDialogData(data);
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
		} else {
			fillDialogData(curData);
		}
	}

	// 填充对话框最新数据
	function fillDialogData(curData){
		// 更新id
		$id("id").val(curData.id);
		
		// 名称
		var rowHtml = "<label class='field label one half wide required'>名称</label>";
		rowHtml += "<input class='field value one half wide' id='name' value='" + curData.name + "' />";
		$id("nameRow").html(rowHtml);

		// 是否颜色
		rowHtml = "<label class='field label one half wide required'>是否颜色</label>";
		if (curData.colorFlag) {
			rowHtml += "<input type='radio' id='colorFlagTrue' name='colorFlag' value='true' checked='checked' />";
			rowHtml += "<label for='colorFlagTrue'>是</label>";
			rowHtml += "<input type='radio' id='colorFlagFalse' name='colorFlag'  value='false' />";
			rowHtml += "<label for='colorFlagFalse'>否</label>";
		} else {
			rowHtml += "<input type='radio' id='colorFlagTrue' name='colorFlag' value='true' />";
			rowHtml += "<label for='colorFlagTrue'>是</label>";
			rowHtml += "<input type='radio' id='colorFlagFalse' name='colorFlag'  value='false' checked='checked' />";
			rowHtml += "<label for='colorFlagFalse'>否</label>";
		}
		$id("colorFlagRow").html(rowHtml);

		// 是否销售专用
		rowHtml = "<label class='field label one half wide required'>是否销售专用</label>";
		if (curData.salesFlag) {
			rowHtml += "<input type='radio' id='salesFlagTrue' name='salesFlag' value='true' checked='checked' />";
			rowHtml += "<label for='salesFlagTrue'>是</label>";
			rowHtml += "<input type='radio' id='salesFlagFalse' name='salesFlag'  value='false' />";
			rowHtml += "<label for='salesFlagFalse'>否</label>";
		} else {
			rowHtml += "<input type='radio' id='salesFlagTrue' name='salesFlag' value='true' />";
			rowHtml += "<label for='salesFlagTrue'>是</label>";
			rowHtml += "<input type='radio' id='salesFlagFalse' name='salesFlag'  value='false' checked='checked' />";
			rowHtml += "<label for='salesFlagFalse'>否</label>";
		}
		$id("salesFlagRow").html(rowHtml);
		
		// 描述
		rowHtml = "<label class='field label one half wide'>描述</label>";
		rowHtml += "<textarea class='field value three wide' style='height:100px; resize:none;' id='desc'>" + curData.desc + "</textarea>";
		$id("descRow").html(rowHtml);
		
		// 判断操作方法初始化不同的界面显示
		if (method == "view") {
			$("#specRefDialog input").prop("disabled", true);
			$("#specRefDialog textarea").prop("disabled", true);
		} else {
			$("#specRefDialog input").prop("disabled", false);
			$("#specRefDialog textarea").prop("disabled", false);
		}
	}
	
	// 初始化数据
	function loadGridData() {
		$("#conName").val('');
		jqGridCtrl = $("#specRefList").jqGrid({
			height : "100%",
			width : "100%",
			url : getAppUrl("/categ/spec/ref/list/get"),
			contentType : 'application/json',
			mtype : "post",
			datatype : 'json',
			colNames : [ "code", "名称", "是否颜色", "是否销售专用", "描述", "操作" ],
			colModel : [ {
					name : "code",
					width : 100,
					align : "left"
				}, {
					name : "name",
					width : 200,
					align : "left"
				}, {
					name : "colorFlag",
					width : 100,
					align : "center",
					formatter : function(cellValue, option, rowObject) {
						if(cellValue){
							return "是";
						} else {
							return "否";
						}
					}
				}, {
					name : "salesFlag",
					width : 100,
					align : "center",
					formatter : function(cellValue, option, rowObject) {
						if(cellValue){
							return "是";
						} else {
							return "否";
						}
					}
				}, {
					name : "desc",
					width : 350,
					align : "left"
				}, {
					name : 'id',
					formatter : function(cellValue, option, rowObject) {
						return "<span>[<a href='javascript:void(0);' onclick='goView("
								+ JSON.stringify(cellValue)
								+ ")' >查看</a>]</span>"
								+ "<span> [<a href='javascript:void(0);' onclick='goEdit("
								+ JSON.stringify(cellValue)
								+ ")' >修改</a>]</span>"
								+ "<span> [<a href='javascript:void(0);' onclick='goDelSpecRef("
								+ cellValue
								+ ")' >删除</a>]</span>";
					},
					width : 200,
					align : "center"
				} ],
			multiselect : true, // 定义是否可以多选
			multikey:'ctrlKey', 
			pager : "#specRefPager", // 分页div
			loadComplete : function(gridData) { // JqGridHelper缓存最新的grid数据
				gridHelper.cacheData(gridData);
				var callback = getCallbackAfterGridLoaded();
				if (isFunction(callback)) {
					callback();
				}
			},
			ondblClickRow: function(rowId) {
				goView(rowId);
			}
		
		});
	}

	// 初始化新增框
	function initAddDialog() {
		// 清空残留值
		formProxy.setValue("name", "");
		$("#colorFlagFalse").prop("checked", "true");
		$("#salesFlagFalse").prop("checked", "true");
		formProxy.setValue("desc", "");

		specRefDialog = $("#specRefDialog").dialog({
			autoOpen : false,
			title : "添加规格参照",
			width : Math.min(600, $window.width()),
			height : Math.min(400, $window.height()),
			modal : true,
			buttons : {
				"保存" : function() {
					if (checkSpecRef()) {
						addSpecRef();
					}
				},
				"取消" : function() {
					specRefDialog.dialog("close");
					// 清空回调函数
					getCallbackAfterGridLoaded = function() {
					};
					// 清空查询条件
					$("#conName").val('');
				}
			},
			close : function() {
				formProxy.hideMessages();
			}
		});

		$("#specRefDialog input").prop("disabled", false);
		$("#specRefDialog textarea").prop("disabled", false);
	}

	// 查询
	function queryData() {
		var name = $id("conName").val();
		if(name.length > 10){
			Layer.msgWarning("名称长度应小于等于10位");
		} else {
			jqGridCtrl.jqGrid("setGridParam", {
				postData : {
					filterStr : JSON.encode({
						"name" : name
					}, true)
				}
			}).trigger("reloadGrid");
		}
	}

	// 初始化并打开新增框
	function goAdd() {
		method = "add";
		initAddDialog();
		specRefDialog.dialog("open");
	}

	// 批量删除
	function batchDel() {
		var ids = jqGridCtrl.jqGrid("getGridParam", "selarrrow");
		var postData = [];
		for (var i = 0, len = ids.length; i < len; i++) {
			postData.add(ParseInt(ids[i]));
		}

		if (ids == "") {
			Layer.msgWarning("请选择要删除的数据！");
		} else {
			var theLayer = Layer.confirm('确定要删除所选规格参照吗？', function() {
				var hintBox = Layer.progress("正在删除...");
				var ajax = Ajax.post("/categ/specRef/delete/by/ids");
				ajax.data(postData);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						// 加载最新数据列表
						jqGridCtrl.jqGrid().trigger("reloadGrid");
						// 清空回调函数
						getCallbackAfterGridLoaded = function() {
						};
					} else {
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
	}

	// 查看对话框
	function goView(id) {
		method = "view";
		specRefId = id;

		specRefDialog = $("#specRefDialog").dialog({
			autoOpen : false,
			title : "查看规格参照",
			width : Math.min(600, $window.width()),
			height : Math.min(400, $window.height()),
			modal : true,
			buttons : {
				"继续添加" : goAdd,
				"修改 >" : function() {
					goEdit(specRefId);
				},
				"关闭" : function() {
					specRefDialog.dialog("close");
				}
			},
			close : function() {
				formProxy.hideMessages();
			}
		});

		loadDialogData(specRefId);
		specRefDialog.dialog("open");
	}

	// 修改对话框
	function goEdit(id) {
		method = "edit";
		specRefId = id;
		specRefDialog = $("#specRefDialog").dialog({
			autoOpen : false,
			title : "修改规格参照",
			width : Math.min(600, $window.width()),
			height : Math.min(400, $window.height()),
			modal : true,
			buttons : {
				"保存" : function() {
					if (checkSpecRef()) {
						editSpecRef();
					}
				},
				"关闭" : function() {
					specRefDialog.dialog("close");
				}
			},
			close : function() {
				formProxy.hideMessages();
			}
		});

		loadDialogData(specRefId);
		specRefDialog.dialog("open");
	}

	// 添加对话框
	function addSpecRef() {
		var hintBox = Layer.progress("正在保存数据...");

		var ajax = Ajax.post("/categ/spec/ref/add/do");
		ajax.data(getSpecRefMap());
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
				specRefDialog.dialog("close");

				specRefId = result.data;
				// 加载最新数据列表
				jqGridCtrl.jqGrid().trigger("reloadGrid");

				// 打开查看框
				getCallbackAfterGridLoaded = function() {
					return function() {
						goView(specRefId);
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

	// 修改
	function editSpecRef(id) {
		var hintBox = Layer.progress("正在保存数据...");
		var ajax = Ajax.post("/categ/spec/ref/update/do");
		ajax.data(getSpecRefMap());
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
				// 加载最新数据列表
				jqGridCtrl.jqGrid().trigger("reloadGrid");
				getCallbackAfterGridLoaded = function() {
					return function() {
						goView(specRefId);
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
	function deleteSpecRefById(id){
		var hintBox = Layer.progress("正在删除...");
		var ajax = Ajax.post("/categ/spec/ref/delete/do");
		ajax.params({
			id : id
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
				// 加载最新数据列表
				jqGridCtrl.jqGrid().trigger("reloadGrid");
				// 清空回调函数
				getCallbackAfterGridLoaded = function() {
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
	function checkInUse(id){
		var ajax = Ajax.post("/categ/specRef/check/inUse/get");
		var postData = {
			id : ParseInt(id)
		};
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var inUse = result.data;
				if(inUse){
					Toast.show("亲，该规格正在被商品分类使用，不能删除哦~", 3000, "warning");
				}else{
					deleteSpecRefById(id);
				}
			}
		});
		ajax.go();
	}
	// 删除
	function goDelSpecRef(id) {
		var theLayer = Layer.confirm('确定要删除该规格参照吗？', function() {
			theLayer.hide();
			checkInUse(id);
		})
	}

	// 自适应界面大小
	function adjustCtrlsSize() {
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", $("#gbox_specRefList")).height();
		var pagerHeight = $id("specRefPager").height();
		jqGridCtrl.setGridWidth(mainWidth - 1);
		jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3);
	}

	// 初始化页面
	$(function() {
		// 页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 56,
			allowTopResize : false
		});

		// 隐藏布局north分割线
		$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");

		// 加载Grid数据
		loadGridData();

		// 初始化添加对话框
		initAddDialog();
		
		// 使用名称过滤查询
		$id("btnQuery").click(queryData);

		// 添加
		$id("btnAdd").click(function() {
			goAdd();
		});

		// 批量删除
		$id("btnBatchDel").click(function() {
			batchDel();
		});

		// 页面大小自适应
		winSizeMonitor.start(adjustCtrlsSize);
	});
</script>
</body>
</html>
