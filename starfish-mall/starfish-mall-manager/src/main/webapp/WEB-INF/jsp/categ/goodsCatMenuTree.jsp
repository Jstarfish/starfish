<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	<link rel="stylesheet" href="<%=resBaseUrl%>/lib/ztree/css/zTreeStyle.css" />
	<title>商品分类菜单</title>
	<style type="text/css">
		table.gridtable {
			font-family: verdana, arial, sans-serif;
			font-size: 11px;
			color: #333333;
			border-width: 1px;
			border-color: #666666;
			border-collapse: collapse;
		}
		
		table.gridtable th {
			border:1px solid #AAA;
			border-width: 1px;
			padding: 3px;
			border-style: solid;
			border-color: #666666;
			background-color: #EFEFEF;
			height: 30px;
		}
		
		table.gridtable td {
			border:1px solid #AAA;
			border-width: 1px;
			padding: 3px;
			border-style: solid;
			border-color: #666666;
			background-color: #ffffff;
			height: 30px;
			text-align: center;
		}
		
		.lable.active {
			background-color: #FFE9D2;
			color: #000;
			border-radius: 2px;
		}
	</style>
</head>
<body id="rootPanel">
<div class="ui-layout-north" style="padding: 4px;">
	<div class="filter section">
		<div class="filter row">
			<div class="group left aligned">
				<input type="hidden" id="menuId" />
				<label class="label">菜单名称：</label><label id="menuName"></label>
				<span class="spacer"></span>
				<label class="label">导航深度：</label><label id="menuNavDepth"></label>
			</div>
			<div class="group right aligned">
				<button class="button" id="btnGoBack">返回</button>
			</div>
		</div>
	</div>
</div>

<div class="ui-layout-west"	style="padding: 0;">
	<div class="form">
		<div class="field row" style="background-color: #EFEFEF;">
			<div class="normal group left aligned">
				<label class="field label">一级菜单项</label>
			</div>
			<div class="normal group right aligned">
				<button class="normal button" id="btnAddFirstLevelMenuItem">添加</button>
			</div>
		</div>
		<input type="hidden" id="curFirstLevelMenuItemId" />
		<div style="padding: 4px;" id="firstLevelMenuItem"></div>
	</div>
</div>

<div class="ui-layout-center" style="padding: 4px;" id="thirdLevelMenuItemPanel">
	<div class="form">
		<div class="field row align center" id="itemCatTitle">
			<label>关联商品分类/链接</label>
		</div>
		<div class="field row align right" id="itemCatBtn">
			<button class="normal button" id="btnAddCat">新增链接</button>
			<span class="normal spacer"></span>
			<button class="normal button" id="btnRelCateg">关联分类</button>
			<span class="normal spacer"></span>
			<button class="normal button" id="btnSave">保存</button>
			<span class="normal spacer"></span>
			<button class="normal button" id="btnCancel">取消</button>
		</div>
		
		<table id="itemCatList"></table>
		<div id="itemCatPager"></div>
	</div>
</div>

<!-- 一级菜单项链接 -->
<div class="form" id="firstLevelMenuItemDialog">
	<div class="field row">
		<div class="normal group left aligned">
			<label class="label required">名称</label>
			<input class="normal input two half wide" type="text" id="firstLevelMenuItemName" />
			<span style="color: #999;" id="firstLevelMenuItemNameTip">菜单项之间以“、”分隔，最多支持四级。</span>
		</div>
		<div class="normal group right aligned">
			<button class="normal button" id="btnEditFirstLevelMenuItemLinks">设置链接</button>
		</div>
	</div>
	<table class="gridtable" style="width:100%;">
		<thead>
			<tr>
				<th width="20%"><label class="normal label">名称</label></th>
				<th width="20%"><label class="normal label">包含链接</label></th>
				<th width="60%"><label class="normal label">URL</label></th>
			</tr>
		</thead>
		<tbody id="firstLevelMenuItemLinksBody"></tbody>
	</table>
	<div class="field row">
		<span style="color: #999;">只有“包含链接”项被选择时，URL才为必填项且为有效数据。</span>
	</div>
</div>

<div id="goodsCatTree" style="display: none;">
	<div class="ztree" id="goodsCatData"></div>
</div>

<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
	// 菜单导航信息
	var menuId, menuName, menuNavDepth;
	// 一级菜单项对话框、三级菜单项Grid、三级菜单项下的关联分类dialog
	var firstLevelMenuItemDialog, thirdLevelMenuItemGrid, goodsCatTreeDialog;
	// 一级、三级菜单项表单代理
	var firstLevelMenuItemProxy = FormProxy.newOne();
	var thirdLevelMenuItemProxy = FormProxy.newOne();
	// 当前一级菜单项id
	var curFirstLevelMenuItemId;
	// 关联分类树
	var catTree;
	// 模拟数据计数器（后续要改）
	var count = 0;
	
	// 加载一级菜单项列表
	function loadFirstLevelMenuItems(menuId) {
		var ajax = Ajax.post("/categ/menu/items/first/level/get");
		ajax.data({
			menuId : menuId,
			level : 1
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var data = result.data;
				if(data){
					$id("firstLevelMenuItem").html(laytpl($id("firstLevelGoodsCategMenuTpl").html()).render(data));
				}
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	
	// 初始化一级菜单项中的链接项内容
	function initFirstLevelMenuItemDialog() {
		firstLevelMenuItemDialog = $("#firstLevelMenuItemDialog").dialog({
			autoOpen : false,
			title : "添加一级菜单项",
			width : Math.min(800, $window.width()),
			height : Math.min(350, $window.height()),
			modal : true,
			buttons : {
				"保存" : function() {
					saveFirstLevelMenuItem();
				},
				"取消" : function() {
					firstLevelMenuItemDialog.dialog("close");
				}
			},
			close : function() {
				firstLevelMenuItemProxy.hideMessages();
			}
		});
	}
	
	// 设置一级菜单项链接
	function editFirstLevelMenuItemLinks() {
		var name = $("#firstLevelMenuItemName").val().trim();
		if (!name) {
			Layer.msgWarning("请输入名称");
			return;
		}
		var names = name.split("、");
		// 最多支持四级菜单项
		if(names.length > 4){
			Layer.msgWarning("最多支持四级菜单项");
			return;
		}
		$id("firstLevelMenuItemLinksBody").empty();
		for (var i = 0; i < names.length; i++) {
			// 过滤无效菜单名称
			var _name = names[i];
			if(!_name && !_name.trim()){
				Layer.msgWarning("请输入有效菜单项名称");
				return;
			}
			
			var trHtml = "<tr><td><label class='label' id='name" + i + "'>" + names[i] + "</label></td>"
				+ "<td><input class='normal input' type='checkbox' id='hasUrl" + i + "' /></td>"
				+ "<td><input class='normal input three half wide' type='text' id='url" + i + "' /></td></tr>";
			$id("firstLevelMenuItemLinksBody").append(trHtml);
		}
	}

	// 检验一级菜单项
	function checkFirstLevelMenuItem() {
		// 防缓存
		firstLevelMenuItemProxy = FormProxy.newOne();
		
		firstLevelMenuItemProxy.addField("menuId");
		firstLevelMenuItemProxy.addField({
			id : "firstLevelMenuItemName",
			required : true,
			rules : ["rangeLength[1, 30]"]
		});
		
		$("#firstLevelMenuItemLinksBody tr").each(function(i, ip) {
			var name, hasUrl, url;
			$(ip).find("td").each(function(j, jp) {
				if (j == 0) {
					name = jp.children[0].name;
					return true;
				} else if (j == 1) {
					hasUrl = "'" + jp.children[0].checked + "'";
					return true;
				} else if (j == 2) {
					if (hasUrl) {
						url = jp.children[0].value;
					}
				}
			});
			
			firstLevelMenuItemProxy.addField({
				id : "name" + i,
				get : function(idOrName, type, curData, asRawVal) {
					return $id("name" + i).text().trim();
				}
			});
			firstLevelMenuItemProxy.addField({
				id : "hasUrl" + i,
				get : function(idOrName, type, curData, asRawVal) {
					return !($id("hasUrl" + i).attr("checked")) ? false : true;
				}
			});
			firstLevelMenuItemProxy.addField({
				id : "url" + i,
				get : function(idOrName, type, curData, asRawVal) {
					// 若“包含链接”项被选择，取对应的url值；否则返回""
					var hasUrl = firstLevelMenuItemProxy.getValue("hasUrl" + i);
					if(hasUrl) {
						return $id("url" + i).val();
					}
					return "";
				},
				rules : ["rangeLength[0, 250]", {
					rule : function(idOrName, type, rawValue, curData) {
						var hasUrl = firstLevelMenuItemProxy.getValue("hasUrl" + i);
						if(hasUrl && !rawValue) {
							return false;
						}
						return true;
					},
					message : "当“包含链接”项被选择时，该URL为必填项"
				}]
			});
		});
		
		return firstLevelMenuItemProxy.validateAll();
	}
	
	// 保存一级菜单项
	function saveFirstLevelMenuItem() {
		if (!checkFirstLevelMenuItem()) {
			return;
		}
		
		var ajax = Ajax.post("/categ/menu/item/first/level/add/do");
		ajax.data(firstLevelMenuItemProxy.getValues());
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
				// 更新一级菜单列表、加载三级菜单列表
				loadFirstLevelMenuItems(firstLevelMenuItemProxy.getValue("menuId"));
				//loadTrirdLevelMenuItems(result.data.id);
				firstLevelMenuItemDialog.dialog("close");
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	
	// 加载三级菜项信息
	function loadTrirdLevelMenuItems(pId, _this) {
		$("#firstLevelMenuItem .lable.active").each(function(i,obj){
			$(obj).removeClass("lable active");
		});
		$(_this).parent().addClass("lable active");
		
		$id("thirdLevelMenuItemPanel").show();
		
		var postData = JSON.encode({
			menuItemId : pId
		}, true);
		if (!thirdLevelMenuItemGrid) {
			thirdLevelMenuItemGrid = $id("itemCatList").jqGrid({
				url : getAppUrl("/categ/menu/item/cat/list/get"),
				contentType : 'application/json',
				mtype : "post",
				datatype : 'json',
				postData : {
					filterStr : postData
				},
				colNames : [ "id", "分类项ID", "分类标志", "分类ID", "分类/链接名称", "链接URL", "是否突出显示", "操作" ],
				colModel : [
					{
						name : "id",
						index : "id",
						hidden : true
					},
					{
						name : "menuItemId",
						hidden : true
					},
					{
						name : "catFlag",
						hidden : true
					},
					{
						name : "catId",
						hidden : true
					},
					{
						name : "name",
						width : 150,
						align : 'left',
						formatter : function(cellValue, option, rowObject) {
							if (rowObject.catFlag) {
								return "<input id='name-"
										+ option.rowId
										+ "' name='name' type='hidden' value='"
										+ (rowObject.name || "")
										+ "'/>"
										+ (rowObject.name || "");
							} else {
								return "<input id='name-"
										+ option.rowId
										+ "' name='name' value='"
										+ (rowObject.name || "")
										+ "'/>";
							}
						}
					},
					{
						name : "linkUrl",
						width : 150,
						align : 'left',
						formatter : function(cellValue, option, rowObject) {
							if (rowObject.catFlag) {
								return "<input id='linkUrl-" + option.rowId + "' name='linkUrl' type='hidden' value='" + (rowObject.linkUrl || "") + "'/>" + (rowObject.linkUrl || "");
							} else {
								return "<input id='linkUrl-" + option.rowId + "' name='linkUrl' value='" + (rowObject.linkUrl || "") + "'/>";
							}
						}
					},
					{
						name : "emphFlag",
						width : 70,
						align : 'center',
						formatter : function(cellValue, option, rowObject) {
							return "<input id='emphFlag-"
									+ option.rowId
									+ "' type='checkbox' name='emphFlag' class='field value' "
									+ (cellValue == true ? 'checked'
											: '')
									+ " value='1'/>&nbsp;&nbsp;是";
						}
					},
					{
						name : "ts",
						width : 100,
						align : 'center',
						formatter : function(cellValue, option, rowObject) {
							return "<button class='normal button' onclick='delThirdLevelMenuItem("
									+ rowObject.id
									+ ")'>删除</button>";
// 									+ "<button class='normal button' style='width: 20px;' onclick='moveOrder(0)'>↑</button>"
// 									+ "<button class='normal button' style='width: 20px;' onclick='moveOrder(1)'>↓</button>";
						}
					} ],
				pager : "#itemCatPager",
				height : "400"
			});
			
			adjustCtrlsSize();
		} else {
			thirdLevelMenuItemGrid.jqGrid("setGridParam", {
				postData : {
					filterStr : postData
				}
			}).trigger("reloadGrid");
		}
	}
	
	// 关联删除商品分类一级菜单
	function delFirstLevelMenuItem(id) {
		var theLayer = Layer.confirm('删除一级菜单项将删除该菜单项下面的所有关联的菜单项，确定吗？', function() {
			var hintBox = Layer.progress("正在删除...");
			var ajax = Ajax.post("/categ/first/level/menu/item/del/do");
			ajax.data({
				id : parseInt(id)
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
	
					// 刷新数据
					loadFirstLevelMenuItems(menuId);
					$id("thirdLevelMenuItemPanel").hide();
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
	
	// 删除商品分类三级菜单
	function delThirdLevelMenuItem(id) {
		var delId = id;
		var theLayer = Layer.confirm('确定要删除吗？', function() {
			var hintBox = Layer.progress("正在删除...");
			if (delId) {
				var ajax = Ajax.post("/categ/third/level/menu/item/del/do");
				ajax.data({
					id : parseInt(delId)
				});
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						hideMiscTip("name-" + delId);
						hideMiscTip("linkUrl-" + delId);
						thirdLevelMenuItemGrid.jqGrid("delRowData", delId);
						loadTrirdLevelMenuItems($id("curFirstLevelMenuItemId").val());
					} else {
						Layer.msgWarning(result.message);
					}
				});
				ajax.always(function() {
					theLayer.hide();
					hintBox.hide();
				});
				ajax.go();
			} else {
				var id = thirdLevelMenuItemGrid.jqGrid("getGridParam", "selrow");
				hideMiscTip("name-" + id);
				hideMiscTip("linkUrl-" + id);
				thirdLevelMenuItemGrid.jqGrid("delRowData", id);
				hintBox.hide();
			}
			thirdLevelMenuItemProxy.validateAll();
		});
	}
	
	function addMenuItemCat() {
		if (!$id("curFirstLevelMenuItemId").val()) {
			Layer.msgWarning("请选择菜单项");
		} else {
			// 先确定界面上的表单是否通过检验
			changeFormProxy();
			var vldResult = thirdLevelMenuItemProxy.validateAll();
			if (!vldResult) {
				return;
			}
			
			var data = {
				id : null,
				menuItemId : $id("curFirstLevelMenuItemId").val(),
				catFlag : false,
				catId : null,
				name : null,
				linkUrl : null,
				emphFlag : false
			};
			var rowId = "new-" + count;
			thirdLevelMenuItemGrid.addRowData(rowId, data);
			count++;
		}
	}
	
	function addMenuItemCatByTree() {
		if (!$id("curFirstLevelMenuItemId").val()) {
			Layer.warning("请选择菜单项");
		} else {
			var catIds = [];
			catIds = thirdLevelMenuItemGrid.getCol("catId");
			
			var nodes = catTree.getCheckedNodes(true);
			for (var i = 0; i < nodes.length; i++) {
				var node = nodes[i];
				var data = {
					id : null,
					menuItemId : $id("curFirstLevelMenuItemId").val(),
					catFlag : true,
					catId : node.id,
					name : node.name,
					linkUrl : 'http://test.example/' + node.id,
					emphFlag : false
				};
				var rowId = "new-" + count;
				if(!catIds.contains(node.id)){
					thirdLevelMenuItemGrid.addRowData(rowId, data);
					count++;
				}
			}
			goodsCatTreeDialog.dialog("close");
		}
	}
	
	function changeFormProxy() {
		thirdLevelMenuItemProxy = FormProxy.newOne();
		var rowIds = thirdLevelMenuItemGrid.getDataIDs();
		for (var i = 0; i < rowIds.length; i++) {
			var rowId = rowIds[i];
			thirdLevelMenuItemProxy.addField({
				id : "name-" + rowId,
				required : true,
				rules : [ "maxLength[30]", {
					rule : function(idOrName, type, rawValue, curData) {
						var rowIds = [];
						rowIds = thirdLevelMenuItemGrid.getDataIDs();
						var nowId = idOrName.substring(5, idOrName.length);
						rowIds.remove(nowId, false);
						var names = [];
						for (var m = 0; m < rowIds.length; m++) {
							var name = thirdLevelMenuItemGrid.getCell(rowIds[m], "name");
							names.add($id($(name).attr("id")).val());
						}
						if (names.contains(rawValue, false)) {
							return false;
						} else {
							return true;
						}
					},
					message : "名称已存在！"
				} ]
			});
			thirdLevelMenuItemProxy.addField({
				id : "linkUrl-" + rowId,
				required : true,
				rules : [ "maxLength[250]" ]
			});
		}
	}
	
	// 获取菜单项商品分类信息
	function getMenuItemCat() {
		var rowIds = thirdLevelMenuItemGrid.getDataIDs();
		var itemCats = [];
		for (var i = 0; i < rowIds.length; i++) {
			var map = new KeyMap();
			var itemCat = thirdLevelMenuItemGrid.jqGrid("getRowData", rowIds[i]);
			var name = $("#itemCatList #" + rowIds[i] + " input[name=name]").val() || "";
			if (name != "") {
				map.add("name", name);
				if (itemCat.id) {
					map.add("id", parseInt(itemCat.id));
				}
				map.add("menuItemId", parseInt(itemCat.menuItemId));
				map.add("catFlag", itemCat.catFlag == "true" ? true : false);
				if (itemCat.catId) {
					map.add("catId", parseInt(itemCat.catId));
				}
				var linkUrl = $("#itemCatList #" + rowIds[i] + " input[name=linkUrl]").val() || "";
				map.add("linkUrl", linkUrl);
				var emphFlag = $("#itemCatList #" + rowIds[i] + " input[name=emphFlag]").is(":checked");
				map.add("emphFlag", emphFlag);
				var cellIndex = thirdLevelMenuItemGrid.jqGrid("getCell", rowIds[i], 0);
				map.add("seqNo", parseInt(cellIndex));
				itemCats.push(map);
			}
		}
		return itemCats;
	}
	
	// 保存菜单项商品分类
	function saveMenuItemCat() {
		changeFormProxy();
		var vldResult = thirdLevelMenuItemProxy.validateAll();
		if (!vldResult) {
			return;
		}
		
		var ajax = Ajax.post("/categ/menu/item/cat/batch/save/do");
		ajax.data(getMenuItemCat());
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
				loadTrirdLevelMenuItems($id("curFirstLevelMenuItemId").val());
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	
	// 取消菜单项商品分类
	function cancelMenuItemCat() {
		thirdLevelMenuItemProxy.hideMessages();
		loadTrirdLevelMenuItems($id("curFirstLevelMenuItemId").val());
	}
	
	var setting = {
		data : {
			simpleData : {
				enable : true
			}
		},
		check : {
			enable : true,
			chkStyle : "checkbox",
			chkboxType : {
				"Y" : "p",
				"N" : "s"
			}
		}
	};
	
	// 加载商品分类树信息
	function loadGoodsCatTree() {
		if (!catTree) {
			var ajax = Ajax.post("/categ/goodsCat/list/get");
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data;
					var node = null;
					var catNodes = [];
					$.each(data, function() {
						var nodeMap = {};
						var data = $(this)[0];
						nodeMap["id"] = data.id;
						nodeMap["pId"] = data.parentId;
						nodeMap["name"] = data.name;
						if (data.level < 3) {
							nodeMap["open"] = true;
							nodeMap["nocheck"] = true;
						}
						catNodes.add(nodeMap);
					});
	
					$.fn.zTree.init($("#goodsCatData"), setting, catNodes);
					catTree = $.fn.zTree.getZTreeObj("goodsCatData");
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
	}
	
	// 打开关联分类树dialog（默认选中界面上已存在的关联分类）
	function openGoodsCatTreeDialog() {
		if (!$id("curFirstLevelMenuItemId").val()) {
			Layer.warning("请选择菜单项");
		} else {
			var catIds = [];
			catIds = thirdLevelMenuItemGrid.getCol("catId");
			for (var i = 0, len = catIds.length; i < len; i++) {
				var id = catIds[i];
				var node = catTree.getNodeByParam("id", id);
				if(node){
					catTree.checkNode(node, true);
				}
			}
			
			goodsCatTreeDialog.dialog("open");
		}
	}
	
	// 初始化商品分类树dialog
	function initGoodsCatTreeDialog() {
		goodsCatTreeDialog = $id("goodsCatTree").dialog({
			title : "商品分类",
			autoOpen : false,
			width : Math.min(300, $window.width()),
			height : Math.min(400, $window.height()),
			modal : true,
			buttons : {
				"确定" : function() {
					addMenuItemCatByTree();
				},
				"取消" : function() {
					goodsCatTreeDialog.dialog("close");
				}
			},
			close : function() {
				goodsCatTreeDialog.dialog("close");
			}
		});
	}

	// 自适应界面大小
	function adjustCtrlsSize() {
		if(thirdLevelMenuItemGrid){
			var jqMainPanel = $id("thirdLevelMenuItemPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			
			var itemCatTitle = $id("itemCatTitle").height();
			var itemCatBtn = $id("itemCatBtn").height();
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", $("#gbox_itemCatList")).height();
			var pagerHeight = $id("itemCatPager").height();
			thirdLevelMenuItemGrid.setGridWidth(mainWidth - 1);
			thirdLevelMenuItemGrid.setGridHeight(mainHeight - itemCatTitle - itemCatBtn - headerHeight - pagerHeight - 3);
		}
	}

	// 初始化页面
	$(function() {
		// 页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 56,
			west__size : 350,
			allowTopResize : false,
			onresize : hideLayoutTogglers
		});
		hideLayoutTogglers();
		// 初始化页面只显示一级菜单信息
		$id("thirdLevelMenuItemPanel").hide();
		thirdLevelMenuItemProxy.hideMessages();
		
		// 解析url参数
		var urlParams = extractUrlParams();
		menuId = parseInt(urlParams["menuId"]);
		menuName = decodeURI(urlParams["menuName"])
		menuNavDepth = parseInt(urlParams["menuNavDepth"]);
		$("#menuId").val(menuId);
		$("#menuName").text(menuName);
		$("#menuNavDepth").text(menuNavDepth);
		
		// 加载一级菜单项数据
		loadFirstLevelMenuItems(menuId);
		// 初始化一级菜单项的链接项对话框
		initFirstLevelMenuItemDialog();
		// 添加一级菜单项
		$("#btnAddFirstLevelMenuItem").click(function() {
			$id("firstLevelMenuItemName").val('');
			$id("firstLevelMenuItemLinksBody").empty();
			firstLevelMenuItemDialog.dialog("open");
		});
		// 设置一级菜单项链接
		$("#btnEditFirstLevelMenuItemLinks").click(editFirstLevelMenuItemLinks);
		
		// 初始化商品分类树dialog
		initGoodsCatTreeDialog();
		// 加载商品分类树信息
		loadGoodsCatTree();
		// 新增普通链接
		$id("btnAddCat").click(addMenuItemCat);
		// 新增分类链接
		$id("btnRelCateg").click(openGoodsCatTreeDialog);
		// 保存三级菜单项链接
		$("#btnSave").click(saveMenuItemCat);
		// 取消三级菜单项链接
		$("#btnCancel").click(cancelMenuItemCat);
		// 页面返回功能
		$("#btnGoBack").click(function(){
			history.go(-1);
		});
	
		// 页面自适应
		winSizeMonitor.start(adjustCtrlsSize);
	});
</script>
</body>
<script id="firstLevelGoodsCategMenuTpl" type="text/html" title="商品分类一级菜单">
	{{# for(var i = 0, len = d.length; i < len; i++){ }}
		<div class="field row">
			<div class="normal group left aligned">
				<label class="field label two wide align left" style="cursor: pointer;" onclick="loadTrirdLevelMenuItems({{ d[i].id }}, this);$id('curFirstLevelMenuItemId').val({{ d[i].id }});">{{ d[i].name }}</label>
			</div>
			<div class="normal group right aligned">
				<!-- <button class="normal button" style="width: 20px;">↑</button>
				<button class="normal button" style="width: 20px;" name="arrowDown">↓</button> -->
				<button class="normal button" style="width: 50px;" onclick="delFirstLevelMenuItem({{ d[i].id }});">删除</button>
			</div>
		</div>
	{{# } }}
</script>

</html>
