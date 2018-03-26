<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	<title>商品分类菜单</title>
</head>
<body id="rootPanel">
	<div class="ui-layout-north" style="padding:4px;" id="topPanel">
		<div class="filter section" >
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnAddGoodsCatMenu" class="button" >添加</button>
				</div>
			
				<div class="group right aligned">
					<label class="label">菜单名称</label>
					<input class="input one wide" id="queryGoodsCatMenuName" />
					<span class="spacer"></span>
					<button class="button" id="btnQueryGoodsCatMenu">查询</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="ui-layout-center" style="padding:4px;" id="mainPanel">
		<div class="noBorder">
			<table id="theGridCtrl"></table>
			<div id="theGridPager"></div>
		</div>
	</div>
		
	<div id="dialog_goodsCatMenuCreate" style="display: none;">
		<div id="addForm" class="form">
			<input type="hidden" id="GoodsCatMenuId" class="field two value wide" />
			<div class="field row">
				<label class="field label required">菜单名称</label> 
				<input type="text" id="GoodsCatMenuName" class="field two value wide" />
			</div>
			<div class="field row">
				<label class="field label required">导航深度</label> 
				<select class="field value" id="GoodsCatMenuNavDepth" >
					<option value="1">1</option>
					<option value="2">2</option>
				</select>
<!-- 				<div class="field group"> -->
<!-- 					<input id="navDepthOne" type="radio" name="navDepth" value="1" checked="checked" /> -->
<!-- 					<label for="navDepthOne">1</label> -->
<!-- 					<input id="navDepthTwo" type="radio" name="navDepth" value="2" /> -->
<!-- 					<label for="navDepthTwo">2</label> -->
<!-- 				</div> -->
			</div>
			<div class="field row">
				<label class="field label required">是否启用</label> 
				<div class="field group two wide ">
					<input id="disabled-Y" type="radio" name="GoodsCatMenuDisabled" value="0" checked="checked"/>
					<label for="disabled-Y">启用</label>
					<input id="disabled-N" type="radio" name="GoodsCatMenuDisabled" value="1" />
					<label for="disabled-N">禁用</label>
				</div>
			</div>
			<div class="field row">
				<label class="field label required">是否默认</label> 
				<div class="field group ">
					<input id="defaulted-Y" type="radio" name="GoodsCatMenuDefaulted" value="1" checked="checked" />
					<label for="defaulted-Y">是</label>
					<input id="defaulted-N" type="radio" name="GoodsCatMenuDefaulted" value="0" />
					<label for="defaulted-N">否</label>
				</div>
			</div>
			<div class="field row" style="height:120px;">
				<label class="field label">描述</label>
				<textarea class="field value two wide" style="height:100px; resize: none;" id="GoodsCatMenuDesc"></textarea>
			</div>
		</div>
	</div>

<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
	//------------------------初始化变量-------------------------
	//jqGrid缓存变量
	var jqGridCtrl = null;
	
	//缓存当前jqGrid数据行数组
	var goodcatGridHelper = JqGridHelper.newOne("");
	var dialog_goodsCatMenuCreate;
	var dialog_goodsCatMenuShow;
	// ------------------------验证-------------------------
	// 实例化表单代理
	var formProxy = FormProxy.newOne();
	// 注册表单控件
	formProxy.addField({
		id : "GoodsCatMenuId",
		key : "GoodsCatMenu.id",
	});
	formProxy.addField({
		id : "GoodsCatMenuName",
		key : "GoodsCatMenu.name",
		required : true,
		rules : [ "maxLength[30]" ]
	});
	formProxy.addField({
		id : "GoodsCatMenuNavDepth",
		key : "GoodsCatMenu.navDepth",
		required : true,
		type : "int",
		rules : [ "rangeValue[1,2]" ]
	});
	formProxy.addField({
		id : "GoodsCatMenuDefaulted",
		key : "goodsCatMenu.defaulted",
		required : true
	});
	formProxy.addField({
		id : "GoodsCatMenuDisabled",
		key : "goodsCatMenu.disabled",
		required : true
	});
	formProxy.addField({
		id : "GoodsCatMenuDesc",
		key : "goodsCatMenu.desc",
		rules : [ "maxLength[100]" ]
	});

	// ------------------------调整控件大小-------------------------
	// 调整控件大小
	function adjustCtrlsSize() {
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		//
		var gridCtrlId = "theGridCtrl";
		var jqGridBox = $("#gbox_" + gridCtrlId);
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
		var pagerHeight = $id("theGridPager").height();
		jqGridCtrl.setGridWidth(mainWidth - 1);
		jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3);
	}
	
	// ------------------------jqGrid-------------------------
	function initJqGrid() {
		// 初始化数据表格
		jqGridCtrl = $("#theGridCtrl").jqGrid({
			url : getAppUrl("/categ/menu/list/get"),
			contentType : 'application/json',
			mtype : "post",
			datatype : 'json',
			height : "100%",
			width : "100%",
			colNames : [ "id", "菜单名称", "导航深度", "描述", "是否启用", "是否默认", "最后修改时间", "相关操作" ],
			colModel : [
				{
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
					name : "navDepth",
					index : "navDepth",
					width : 80,
					align : 'center',
				},
				{
					name : "desc",
					index : "desc",
					width : 300,
					align : 'left',
				},
				{
					name : "disabled",
					index : "disabled",
					width : 85,
					formatter : function(cellValue, option, rowObject) {
						if (cellValue == true) {
							return "否";
						} else {
							return "是";
						}
					},
					align : 'center',
				},
				{
					name : "defaulted",
					index : "defaulted",
					formatter : function(cellValue, option, rowObject) {
						if (cellValue == true) {
							return "是";
						} else {
							return "否";
						}
					},
					width : 85,
					align : 'center',
				},
				{
					name : "ts",
					index : "ts",
					width : 150,
					align : 'center',
				},
				{
					name : 'id',
					index : 'id',
					align : 'center',
					formatter : function(cellValue, option, rowObject) {
						return "[<a class='item' href='javascript:void(0);' onclick='goodsCatMenuDialogShowOpen("
								+ JSON.stringify(rowObject)
								+ ")' >查看</a>]<span class='chs spaceholder'></span>[<a class='item' href='javascript:void(0);' onclick='goodsCatMenuDel("
								+ JSON.stringify(rowObject)
								+ ")' >删除</a>]<span class='chs spaceholder'></span>[<a class='item' href='javascript:void(0);' onclick='toEditMenuPage("
								+ JSON.stringify(rowObject)
								+ ")' >菜单项</a>]";
					},
					width : 380,
					align : "center"
				} ],
			// rowList:[10,20,30],
			multiselect : true,// 定义是否可以多选
			multikey:'ctrlKey', 
			pager : "#theGridPager",
			loadComplete : function(gridData) { // JqGridHelper缓存最新的grid数据
				goodcatGridHelper.cacheData(gridData);
				var callback = getCallbackAfterGridLoaded();
				if (isFunction(callback)) {
					callback();
				}
			},
			ondblClickRow: function(rowId) {
				var userMap = goodcatGridHelper.getRowData(rowId)
				goodsCatMenuDialogShowOpen(userMap);
			}
			
		});
	}
	
	//
	function getCallbackAfterGridLoaded(){
	}

	// ------------------------dialog初始化-------------------------
	// 为新增dialog_goodsCatMenuCreate进行初始化
	function initGoodsCatMenuDialogCreate() {
		dialog_goodsCatMenuCreate = $("#dialog_goodsCatMenuCreate").dialog({
			autoOpen : false,
			width : Math.min(480, $window.width()),
			height : Math.min(400, $window.height()),
			modal : true,
			title : '添加商品分类菜单',
			buttons : {
				"保存" : addGoodsCatMenu,
				"取消" : function() {
					dialog_goodsCatMenuCreate.dialog("close");
				}
			},
			close : function() {
				$("#dialog_goodsCatMenuCreate input[type=text]").val("");
				$("#GoodsCatMenuNavDepth").val("");
				$("#dialog_goodsCatMenuCreate textarea").val("");
			}
		});
	}
	// 为查看dialog_goodsCatMenuCreate进行赋值
	function initGoodsCatMenuDialogShow(obj) {
		formProxy.setValue2("GoodsCatMenu.id", obj.id);
		formProxy.setValue2("GoodsCatMenu.name", obj.name);
		formProxy.setValue2("GoodsCatMenu.navDepth", obj.navDepth);
		formProxy.setValue2("goodsCatMenu.defaulted", obj.defaulted);
		formProxy.setValue2("goodsCatMenu.disabled", obj.disabled);
		formProxy.setValue2("goodsCatMenu.desc", obj.desc);
		$("#dialog_goodsCatMenuCreate textarea").attr("disabled", "disabled");
		$("#dialog_goodsCatMenuCreate select").attr("disabled", "disabled");
		$("#dialog_goodsCatMenuCreate input").attr("disabled", "disabled");
		dialog_goodsCatMenuShow = $("#dialog_goodsCatMenuCreate").dialog({
			autoOpen : false,
			width : Math.min(480, $window.width()),
			height : Math.min(400, $window.height()),
			modal : true,
			title : '查看商品分类菜单',
			buttons : {
				"修改 >": initGoodsCatMenuDialogEdit,
				"关闭" : function() {
					dialog_goodsCatMenuShow.dialog("close");
				}
			},
			close : function() {
				$("#dialog_goodsCatMenuCreate input[type=text]").val("");
				$("#dialog_goodsCatMenuCreate select").val("");
				$("#dialog_goodsCatMenuCreate textarea").val("");
				$("#dialog_goodsCatMenuCreate textarea").removeAttr("disabled");
				$("#dialog_goodsCatMenuCreate select").removeAttr("disabled");
				$("#dialog_goodsCatMenuCreate input").removeAttr("disabled");
			}
		});
	}
	
	// 为编辑dialog_goodsCatMenuCreate
	function initGoodsCatMenuDialogEdit() {
		$("#dialog_goodsCatMenuCreate textarea").removeAttr("disabled");
		$("#dialog_goodsCatMenuCreate select").removeAttr("disabled");
		$("#dialog_goodsCatMenuCreate input").removeAttr("disabled");
		dialog_goodsCatMenuShow = $("#dialog_goodsCatMenuCreate").dialog({
			autoOpen : false,
			width : Math.min(480, $window.width()),
			height : Math.min(400, $window.height()),
			modal : true,
			title : '查看商品分类菜单',
			buttons : {
				"保存": updateGoodsCatMenu,
				"关闭" : function() {
					dialog_goodsCatMenuShow.dialog("close");
				}
			},
			close : function() {
				$("#dialog_goodsCatMenuCreate input[type=text]").val("");
				$("#dialog_goodsCatMenuCreate select").val("");
				$("#dialog_goodsCatMenuCreate textarea").val("");
				$("#dialog_goodsCatMenuCreate textarea").removeAttr("disabled");
				$("#dialog_goodsCatMenuCreate select").removeAttr("disabled");
				$("#dialog_goodsCatMenuCreate input").removeAttr("disabled");
			}
		});
	}

	// ------------------------商品分类菜单操作-------------------------
	// 新增商品分类菜单
	function addGoodsCatMenu() {
		var vldResult = formProxy.validateAll();
		if (!vldResult) {
			return;
		}
		var postData = {
			name : formProxy.getValue("GoodsCatMenuName"),
			navDepth : parseInt(formProxy.getValue("GoodsCatMenuNavDepth")),
			disabled : parseInt(formProxy.getValue("GoodsCatMenuDisabled")),
			defaulted : parseInt(formProxy.getValue("GoodsCatMenuDefaulted")),
			desc : formProxy.getValue("GoodsCatMenuDesc")
		};
		var ajax = Ajax.post("/categ/menu/create/do");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				jqGridCtrl.jqGrid("setGridParam", {
					page : 1
				}).trigger("reloadGrid");
				dialog_goodsCatMenuCreate.dialog("close");
				Layer.msgSuccess(result.message);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	
	// 修改商品分类菜单
	function updateGoodsCatMenu() {
		var vldResult = formProxy.validateAll();
		if (!vldResult) {
			return;
		}
		var postData = {
			id : formProxy.getValue("GoodsCatMenuId"),
			name : formProxy.getValue("GoodsCatMenuName"),
			navDepth : parseInt(formProxy.getValue("GoodsCatMenuNavDepth")),
			disabled : parseInt(formProxy.getValue("GoodsCatMenuDisabled")),
			defaulted : parseInt(formProxy.getValue("GoodsCatMenuDefaulted")),
			desc : formProxy.getValue("GoodsCatMenuDesc")
		};
		var ajax = Ajax.post("/categ/menu/update/do");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				jqGridCtrl.jqGrid("setGridParam", {
					page : 1
				}).trigger("reloadGrid");
				tempCate = result.data;
				initGoodsCatMenuDialogShow(tempCate);
				Layer.msgSuccess(result.message);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}

	// 查看商品分类菜单
	function goodsCatMenuDialogShowOpen(obj) {
		initGoodsCatMenuDialogShow(obj);
		dialog_goodsCatMenuShow.dialog("open");
	}

	// 删除商品分类菜单
	function goodsCatMenuDel(obj) {
		var theLayer = Layer.confirm('确定要删除该商品分类吗？', function() {
			var postData = {
				id : obj.id
			};
			var ajax = Ajax.post("/categ/menu/delete/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					jqGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
					theLayer.hide();
					Layer.msgSuccess(result.message);
				} else {
					theLayer.hide();
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
		});
	}
	
	// 跳转到商品分类菜单项页面
	function toEditMenuPage(obj) {
		var url;
		if(obj.navDepth == 1){
			url = getAppUrl("/categ/menu/tree/jsp?menuId=" + obj.id + "&menuName=" + obj.name + "&menuNavDepth=1&menuStatus=edit");
		} else if (obj.navDepth == 2) {
			url = getAppUrl("/categ/second/depth/menu/tree/jsp?menuId=" + obj.id + "&menuName=" + obj.name + "&menuNavDepth=2&menuStatus=edit");
		}
		window.location.href = url;
	}
	
	// ------------------------初始化-------------------------
	$(function() {
		// 页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 56,
			allowTopResize : false,
			onresize : hideLayoutTogglers
		});
		// 隐藏布局north分割线
		//$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
		// 
 		hideLayoutTogglers();
		
		// 初始化JqGrid
		$("#queryGoodsCatMenuName").val('');
		initJqGrid();
		
		// 绑定打开商品分类菜单
		$id("btnQueryGoodsCatMenu").click(function() {
			var name = $id("queryGoodsCatMenuName").val();
			// 加载jqGridCtrl
			jqGridCtrl.jqGrid("setGridParam", {
				postData : {
					filterStr : JSON.encode({
						"name" : name
					}, true)
				},
				page : 1
			}).trigger("reloadGrid");
		});
		
		// 绑定添加商品分类菜单
		$id("btnAddGoodsCatMenu").click(function() {
			initGoodsCatMenuDialogCreate();
			dialog_goodsCatMenuCreate.dialog("open");
		});
		
		// 页面自适应
		winSizeMonitor.start(adjustCtrlsSize);
	});
</script>
</body>
</html>