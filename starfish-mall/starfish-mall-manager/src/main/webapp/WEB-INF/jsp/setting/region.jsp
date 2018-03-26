<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	<link rel="stylesheet" href="<%=resBaseUrl%>/lib/ztree/css/zTreeStyle.css" />
	<title>地区设置</title>
</head>
<body id="rootPanel">
<!-- 查询条件 -->
<div class="ui-layout-north" style="padding: 4px;" id="topPanel">
	<div class="filter section">
		<div class="filter row">
			<div class="group right aligned">
				<input type="hidden" id="id" />
				<label class="normal label">城市名称</label>
				<input class="normal input" id="name">
				<span class="normal spacer"></span>
				<button class="normal button" id="btnSearch" >查询</button>
			</div>
		</div>
	</div>
</div>

<div class="ui-layout-center" id="mainPanel">
	<div class="ui-layout-west"	style="padding: 4px;">
		<div class="filter section" >
			<div>
				<div class="ztree" id="regionsData"></div>
			</div>
		</div>
	</div>
	
	<div class="ui-layout-center" style="padding: 4px;">
		<div class="simple block">
			<div class="header">
				<div class="normal group left aligned">
					<span class="normal spacer"></span>
					<button class="normal button" id="btnAddNext">增加下级</button>
					<span class="normal spacer"></span>
					<button class="normal button" id="btnAddBrother">增加同级</button>
				</div>
				<div class="normal group right aligned">
					<button class="normal button" id="btnUpdate">修改</button>
					<span class="normal spacer"></span>
					<button class="normal button" id="btnDelete">删除</button>
				</div>
			</div>
		</div>
			
		<div class="form">
			<div class="field row" style="display: none;" id="preTreeNodeDiv">
				<label class="field label three half wide">上级名称：</label>
				<label id="selNodePName"></label>
			</div>
			<div class="field row">
				<label class="field label three half wide">本级名称：</label>
				<div id="selNodeNameDiv"><label id="selNodeNameLabel"></label></div>
			</div>
			<div class="field row" align="center" style="display: none;" id="saveAndCancelBtnDiv">
				<div class="normal group">
					<button class="normal button" id="btnCancel">取消</button>
					<span class="normal spacer"></span>
					<button class="normal button" id="btnSave">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 增加下级、增加同级dialog -->
	<div id="brotherNodeDialogAdd" title="新增同级"></div>
	<div id="nextNodeDialogAdd" title="新增下级"></div>
</div>
	
<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
	// 地区最大支持四个级别（ztree级别从0开始）
	var maxLevel = 3;
	// 表单代理
	var searchFormProxy = FormProxy.newOne();
	var addAreaFormProxy = FormProxy.newOne();
	var updateFormProxy = FormProxy.newOne();
	// 当前展开的节点
	var curExpandNode = null;
	// 当前选择的节点
	var selectedNode = null;
	// 增加下级dialog
	var nextNodeDialogAdd;
	// 增加同级dialog
	var brotherNodeDialogAdd;
	
	searchFormProxy.addField({
		id : "name",
		rules : ["maxLength[30]"]
	});
	
	// 绑定单击事件
	function onDblClick(event, treeId, treeNode) {
		// 赋值当前选择节点
		var theTree = $.fn.zTree.getZTreeObj(treeId);
		selectedNode = theTree.getSelectedNodes();
		
		// 控制“增加下级”和“增加同级”按钮显示
		var level = selectedNode[0].level;
		if (level == maxLevel) {
			$id("btnAddNext").prop("disabled", true);
		} else {
			$id("btnAddNext").prop("disabled", false);
		}
	
		// 判断当前被选中的节点的展开/折叠状态
		var isOpen = selectedNode[0].open;
		if (isOpen) {
			isOpen = false;
		} else {
			isOpen = true;
		}

		// 控制右侧面板上级名称的显示与否
		if (level > 0) {
			var pNode = selectedNode[0].getParentNode();
			$("#selNodePName").text(pNode.name);
			$("#preTreeNodeDiv").show();
		} else {
			$("#preTreeNodeDiv").hide();
		}
		$("#selNodeNameLabel").text(selectedNode[0].name);
		
		// 加载下一级节点
		var hasChildNodes = treeNode.isParent || treeNode.level < maxLevel;
		if (hasChildNodes && !treeNode.loaded) {
			if (treeNode.level > maxLevel) {
				return;
			}
			
			// 标记为已更新（从而只执行一次）
			treeNode.loaded = true;
			treeNode.icon = smallLoadingImageUrl;
			theTree.updateNode(treeNode);
			
			// 加载数据
			var ajax = Ajax.post("/setting/children/regions/get");
			ajax.data({
				parentId : treeNode.id
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					// 如果下级有数据加载并展开
					var data = result.data;
					if(data){
						loadTreeNodes(theTree, treeNode, data);
						
						// 判断当前被选中的节点的展开/折叠状态
						//var isOpen = treeNode.open;
						if (isOpen) {
							isOpen = false;
						} else {
							isOpen = true;
						}
						theTree.expandNode(treeNode, isOpen, null, null, true);
					} else {
						Layer.warning("下级无数据");
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
	}

	// 隐藏树节点前固定的+-控件
	function addDiyDom(treeId, treeNode) {
		$("#" + treeNode.tId + "_switch").remove();
	}
	
	// 绑定单击事件
	function onClick(e, treeId, treeNode) {
		// 赋值当前选择节点
		selectedNode = $.fn.zTree.getZTreeObj(treeId).getSelectedNodes();
		
		// 系统最大支持四级地区
		var level = selectedNode[0].level;
		if (level == 3) {
			$id("btnAddNext").prop("disabled", true);
		} else {
			$id("btnAddNext").prop("disabled", false);
		}
	
		// 判断当前被选中的节点的展开/折叠状态
		var isOpen = selectedNode[0].open;
		if (isOpen) {
			isOpen = false;
		} else {
			isOpen = true;
		}

		// 显示父级名称和选择节点名称
		if (level > 0) {
			var pNode = selectedNode[0].getParentNode();
			$("#selNodePName").text(pNode.name);
			$("#preTreeNodeDiv").show();
		} else {
			$("#preTreeNodeDiv").hide();
		}
		$("#selNodeNameLabel").text(selectedNode[0].name);
	}
	
	// 加载地区树
	function loadRegionTree(theTreeId, treeSetting, cityName) {
		var ajax = Ajax.post("/setting/region/tree/get");
		if (cityName) {
			ajax.data({
				name : cityName
			});
		}
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var data = result.data;
				data.forEach(function(el, index) {
					el.isParent = el.level < maxLevel;
				});
				// 加载地区数据
				$.fn.zTree.init($id(theTreeId), treeSetting, data);
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	
	// 打开增加下级dialog
	function nextNodeDialogAddOpen() {
		if (!selectedNode) {
			Layer.warning("请先选择一个节点");
		} else {
			var areaTplHtml = $id("areaTpl").html();
			var htmlStr = laytpl(areaTplHtml).render({});
			$id("nextNodeDialogAdd").html(htmlStr);
			// ztree级别从0开始，后台地区级别从1开始
			$id("areaLevel").val(selectedNode[0].level + 2);
			$id("areaParentId").val(selectedNode[0].id);
			if(selectedNode[0].level > 0){
				$id("areaIdPath").val(selectedNode[0].idPath + "-");
			}
			nextNodeDialogAdd.dialog("open");
		}
	}
	
	// 打开增加同级dialog
	function brotherNodeDialogAddOpen() {
		if (!selectedNode) {
			Layer.warning("请先选择一个节点");
		} else {
			var areaTplHtml = $id("areaTpl").html();
			var htmlStr = laytpl(areaTplHtml).render({});
			$id("brotherNodeDialogAdd").html(htmlStr);
			// ztree级别从0开始，后台地区级别从1开始
			$id("areaLevel").val(selectedNode[0].level + 1);
			$id("areaParentId").val(selectedNode[0].parentId ? selectedNode[0].parentId : -1);
			$id("areaIdPath").val(selectedNode[0].idPath);
			brotherNodeDialogAdd.dialog("open");
		}
	}
	
	// 更新节点名称
	function updateTreeNode() {
		$("#btnUpdate, #btnDelete").prop("disabled", true);
		$("#btnCancel, #btnSave").show();
		
		var sNode = $("#selNodeNameLabel");
		if (sNode.text() == null || sNode.text() == "") {
			Layer.warning("请先选择一个节点");
		} else {
			hideAllMessages();
			sNode.remove();
			var zTree = $.fn.zTree.getZTreeObj("regionsData");
			var selectedNode = zTree.getSelectedNodes();
			$("#selNodeNameDiv").append("<input class='field value two wide' type='text' id='selNodeNameInput' value='" + selectedNode[0].name + "'/>");
			$("#saveAndCancelBtnDiv").show();
		}
	}
	
	// 取消修改
	function cancelTreeNode() {
		$("#btnUpdate, #btnDelete").prop("disabled", false);
		
		var zTree = $.fn.zTree.getZTreeObj("regionsData");
		var selectedNode = zTree.getSelectedNodes();
		$("#selNodeNameInput").remove();
		$("#selNodeNameDiv").append("<label id='selNodeNameLabel'>" + selectedNode[0].name + "</label>");
		$("#btnCancel, #btnSave").hide();
	}
	
	// 保存修改
	function saveTreeNode() {
		// 检验修改的节点名称
		updateFormProxy.addField({
			id : "selNodeNameInput",
			required : true
		});
		if (!updateFormProxy.validateAll()) {
			return;
		}
	
		var zTree = $.fn.zTree.getZTreeObj("regionsData");
		var selectedNode = zTree.getSelectedNodes();
		var newName = textGet("selNodeNameInput");
		var ajax = Ajax.post("/setting/region/tree/update/do");
		var postData = ({
			id : selectedNode[0].id,
			name : newName
		});
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				$("#selNodeNameInput").remove();
				$("#selNodeNameDiv").append("<label id='selNodeNameLabel'>" + newName + "</label>");
				$("#btnUpdate, #btnDelete").prop("disabled", false);
				$("#btnCancel, #btnSave").hide();
	
				// 更新节点名称
				updateNodeName(newName);
				//Layer.info(result.message);
			} else {
				Layer.warning(result.message);
				$("#btnCancel, #btnSave").show();
			}
		});
		ajax.go();
	}
	
	// 隐藏页面所有错误提示
	function hideAllMessages() {
		addAreaFormProxy.hideMessages();
		updateFormProxy.hideMessages();
	}
	
	// 更新节点名称
	function updateNodeName(newName) {
		var zTree = $.fn.zTree.getZTreeObj("regionsData");
		var selectedNode = zTree.getSelectedNodes();
	
		if (selectedNode.length == 0) {
			Layer.warning("请先选择一个节点");
		}
	
		for (var i = 0, len = selectedNode.length; i < len; i++) {
			selectedNode[i].name = newName;
			zTree.updateNode(selectedNode[i]);
		}
	}
	
	// 初始化增加下级dialog
	function initAddNextNodeDialog(){
		brotherNodeDialogAdd = $id("brotherNodeDialogAdd").dialog({
			autoOpen : false,
			height : Math.min(200, $window.height()),
			width : Math.min(400, $window.width()),
			modal : true,
			buttons : {
				"保存" : function() {
					addRegion();
				},
				"取消" : function() {
					hideAllMessages();
					brotherNodeDialogAdd.html('');
					brotherNodeDialogAdd.dialog("close");
				}
			},
			close : function() {
				hideAllMessages();
				brotherNodeDialogAdd.html('');
			}
		});
	}
	
	// 初始化增加同级dialog
	function initAddBrotherNodeDialog(){
		nextNodeDialogAdd = $id("nextNodeDialogAdd").dialog({
			autoOpen : false,
			height : Math.min(200, $window.height()),
			width : Math.min(400, $window.width()),
			modal : true,
			buttons : {
				"保存" : function() {
					addRegion();
				},
				"取消" : function() {
					hideAllMessages();
					nextNodeDialogAdd.html('');
					nextNodeDialogAdd.dialog("close");
				}
			},
			close : function() {
				hideAllMessages();
				nextNodeDialogAdd.html('');
			}
		});
	}
	
	// 添加地区
	function addRegion() {
		if (checkAreaForm()) {
			var ajax = Ajax.post("/setting/region/add/do");
			var name = $id("areaName").val();
			var level = $id("areaLevel").val();
			var parentId = $id("areaParentId").val();
			var idPath = $id("areaIdPath").val();
			var data = {
				name : name,
				level : level,
				parentId : parentId,
				idPath : idPath
			};
			ajax.data(data);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var treeObj = $.fn.zTree.getZTreeObj("regionsData");
					var data = result.data;
					var newNode = {
						id : data.id,
						name : data.name,
						level : data.level,
						idPath : data.idPath,
						parentId : data.parentId
					};
					// 更新父节点
					var pNode = treeObj.getNodeByParam("id", parentId, null);
					newNode = treeObj.addNodes(pNode, newNode);
					// 单独选中新增的节点
					var node = treeObj.getNodeByParam("id", data.id, null);
					treeObj.selectNode(node, false);
					selectedNode = node;
				} else {
					Layer.msgWarning(result.message);
				}
				closeAllDialog();
			});
			ajax.go();
		}
	}
	
	// checkAreaForm
	function checkAreaForm() {
		addAreaFormProxy = FormProxy.newOne();
		addAreaFormProxy.addField({
			id : "areaName",
			required : true,
			rules : [ "maxLength[30]" ]
		});
		return addAreaFormProxy.validateAll();
	}
	
	//关闭所有的dialog
	function closeAllDialog() {
		brotherNodeDialogAdd.dialog("close");
		nextNodeDialogAdd.dialog("close");
	}
	
	// 获取选择节点及其所有子节点
	function getChildNodes(treeNode) {
		var ztree = $.fn.zTree.getZTreeObj("regionsData");
		var childNodes = ztree.transformToArray(treeNode);
		var nodes = [];
		for (i = 0; i < childNodes.length; i++) {
			nodes.add(childNodes[i].id);
		}
		return nodes;
	}
	
	// 删除地区
	function deleteTreeNode() {
		if (!selectedNode) {
			Layer.warning("请先选择一个节点");
		} else {
			Layer.confirm("删除该地区将会删除该地区下所属的下级地区，确定要继续吗？", function() {
				var nodes = getChildNodes(selectedNode);
				var ajax = Ajax.post("/setting/regions/delete/do");
				ajax.data(nodes);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						var treeObj = $.fn.zTree.getZTreeObj("regionsData");
						var nodes = treeObj.getSelectedNodes();
						for (var i=0, l=nodes.length; i < l; i++) {
							treeObj.removeNode(nodes[i]);
						}
						
						Layer.msgSuccess(result.message);
					} else {
						Layer.msgWarning(result.message);
					}
					closeAllDialog();
				});
				ajax.go();
			});
		}
	}
	
	// 初始化页面
	$(function() {
		// 页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 60,
			allowTopResize : false,
			onresize : hideLayoutTogglers
		});
		$id('mainPanel').layout({
			inset : 1,
			spacing_open : 1,
			spacing_closed : 1,
			west__size : 300,
			west__resizable : true,
			onresize : hideLayoutTogglers
		});
		// 隐藏布局north分割线
		//$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
		// 隐藏面板调节开关
		hideLayoutTogglers();
		
		// 树绑定的div名称
		var theTreeId = "regionsData";
		// 加载默认树设置，并绑定事件
		var treeSetting = {
			view : {
				dblClickExpand : true,
				showLine : true,
				selectedMulti : false,
				addDiyDom: addDiyDom
			},
			data : {
				keep : {
					parent : true
				},
				simpleData : {
					enable : true,
					idKey : "id",
					pIdKey : "parentId",
					rootPId : -1
				}
			},
			callback : {
				onClick : onClick,
				onDblClick : onDblClick
			}
		};
		// 加载地区树
		loadRegionTree(theTreeId, treeSetting, null);
		// 条件查询
		$("#btnSearch").click(function() {
			if (searchFormProxy.validateAll()) {
				loadRegionTree(theTreeId, treeSetting, searchFormProxy.getValue("name"));
			}
		});
	
		// 增加下级
		$("#btnAddNext").click(function() {
			nextNodeDialogAddOpen();
		});
		// 增加同级
		$("#btnAddBrother").click(function() {
			brotherNodeDialogAddOpen();
		});
	
		// 修改
		$("#btnUpdate").click(function() {
			updateTreeNode();
		});
		// 删除
		$("#btnDelete").click(function() {
			deleteTreeNode();
		});
		// 取消修改
		$("#btnCancel").click(function() {
			cancelTreeNode();
		});
		// 保存修改
		$("#btnSave").click(function() {
			saveTreeNode();
		});
	
		// 初始化增加下级dialog
		initAddNextNodeDialog();
		// 初始化增加同级dialog
		initAddBrotherNodeDialog();
	});
</script>
</body>
<script id="areaTpl" type="text/html" title="新增、修改地区模板">
	<div class="form">
		<input type="hidden" id="areaLevel" />
		<input type="hidden" id="areaParentId" />
		<input type="hidden" id="areaIdPath" />
		<div class="field row">
			<label class="field label one wide required">地区名称</label> 
			<input class="field value one half wide" type="text" id="areaName" />
		</div>
	</div>
</script>
</html>