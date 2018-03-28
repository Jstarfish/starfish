<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>商品分类管理</title>
	<link rel="stylesheet" href="<%=resBaseUrl%>/lib/ztree/css/zTreeStyle.css" />
	<link rel="stylesheet" href="<%=resBaseUrl%>/lib/colorpicker/evol.colorpicker.css" />
	<style type="text/css">
		table.gridtable {
			font-family: verdana, arial, sans-serif;
			font-size: 11px;
			color: #333333;
			border-width: 1px;
			border-color: #666666;
			border-collapse: collapse;
			width: 100%;
		}
		
		table.gridtable th {
			border:1px solid #AAA;
			border-width: 1px;
			padding: 3px;
			border-style: solid;
			border-color: #666666;
			background-color: #dedede;
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
		}
		
		.list{
			list-style:none;
		    margin: 0px;
		    width: 100%;
		    padding-left: 18px;
		    display: inline-block;
		}
		
		.list li{
			float:left;
			width: 180px;
			height: 40px;
			line-height: 38px;
			vertical-align: middle;
			/* border-right: 1px dashed #D3D3D3;
			margin: 0px 5px; */
		}
	
	</style>
</head>

<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button class="normal button one half wide" id="btnExpandAll">全部展开</button>
					<span class="normal spacer"></span>
					<button class="normal button one half wide" id="btnCollapseAll">全部收起</button>
				</div>
				<div class="group right aligned">
					<label class="label">分类名称</label>
					<input id="queryName" class="input two wide" maxlength="30"/> 
				
					<span class="vt divider">&nbsp;</span>
					<span class="spacer two wide">&nbsp;</span>
				
					<button id="btnQuery" class="normal button one wide right aligned">查询</button>
				</div>
			</div>
		</div>
	</div>
	<div id="mainPanel" class="ui-layout-center">
		<div class="ui-layout-west" id="mainLeftPanel">
			<div>
				<div class="ztree" id="goodsCatData"></div>
			</div>
		</div>
		<div class="ui-layout-center form" id="mainRightPanel">
			<div class="simple block">
				<div class="header">
					<div class="normal group left aligned">
						<span class="normal spacer"></span>
						<button class="normal button one half wide" id="btnAddRoot" style="display: none;">增加一级分类</button>
						<button class="normal button one half wide" id="btnAddChild">增加下级</button>
						<button class="normal button one half wide" id="btnAddBrother">增加同级</button>
					</div>
					<div class="normal group right aligned">
						<button class="normal button" id="btnUpdate">修改</button>
						<button class="normal button" id="btnDelete">删除</button>
						
						<button class="normal button" id="btnSave">保存</button>
						<button class="normal button" id="btnCancel">取消</button>
					</div>
				</div>
			</div>
			<div id="showParents" style="min-height: 300px;">
				<div class="field row" style="display: none;" id="preTreeNodeDiv">
				<label class="field label">上级分类：</label>
				<label id="selNodePName"></label>
				</div>
				<div class="field row">
					<label class="field label">当前分类：</label>
					<label id="selNodeNameLabel"></label>
					<input type="text" id="update-parentName" class="field value" style="display: none;"/>
				</div>
			</div>
			<div id="showLevel3" style="display: none;min-height: 300px;">
				<div id="theShowTabsCtrl" class="noBorder">
					<ul>
						<li>
							<a href="#tabs-goodsCat-show">商品分类</a>
						</li>
						<li>
							<a href="#tabs-goodsCat-attr-show">关联属性</a>
						</li>
						<li>
							<a href="#tabs-goodsCat-spec-show">关联规格</a>
						</li>
						<li>
							<a href="#tabs-goodsCat-group-show">显示分组</a>
						</li>
						<li>
							<a href="#tabs-goodsCat-priceRange-show">价格区间</a>
						</li>
					</ul>
					<div id="tabs-goodsCat-show" class="form">
						<div class="field row">
							<label class="field label">当前分类：</label>
							<input type="text" id="show-name" class="field value"/>
						</div>
						<div class="field row">
							<label class="field label">计数单位：</label> 
							<input type="text" id="show-unit" class="field value" disabled="disabled"/>
						</div>
						<div class="field row">
							<label class="field label">重量单位：</label> 
							<select class="field value" id="show-weightUnit" disabled="disabled"></select>
						</div>
					</div>
					<div id="tabs-goodsCat-attr-show">
						<div id="goodsCatAttrShowDialog">
							<table id="goodsCatAttrShow"></table>
						</div>
						<div id="goodsCatAttrUpdateDialog">
							<div class="group">
								<button id="btnAttrAdd" class="normal button">添加</button>
								<button id="btnAttrDel" class="normal button">去除</button>
							</div>
							<table id="goodsCatAttrUpdate"></table>
						</div>
					</div>
					<div id="tabs-goodsCat-spec-show">
						<div id="goodsCatSpecShowDialog">
							<table id="goodsCatSpecGridShow"></table>
						</div>
						<div id="goodsCatSpecUpdateDialog">
							<div class="group">
							<button id="btnSpecAdd" class="normal button">添加</button>
							<button id="btnSpecDel" class="normal button">去除</button>
						</div>
						<table id="goodsCatSpecGridUpdate"></table>
						</div>
					</div>
					<div id="tabs-goodsCat-group-show">
						<div id="groupsUpdateShow" style="display: none;">
							<div class="group">
								<button id="btnGroupAdd" class="normal button">添加</button>
							</div>
						</div>
						<div id="groupShow">
						</div>
					</div>
					<div id="tabs-goodsCat-priceRange-show">
						<div id="priceRangeShowDialog">
							<table id="priceRangeShowList"></table>
						</div>
						<div id="priceRangeUpdateDialog">
							<div class="group">
								<button id="btnRangeEdit" class="normal button">修改</button>
							</div>
							<table id="priceRangeUpdateList"></table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="addDialog" class="form" style="display: none;">
		<div id="theTabsCtrl" class="noBorder">
			<ul>
				<li>
					<a href="#tabs-goodsCat">商品分类</a>
				</li>
				<li>
					<a href="#tabs-goodsCat-attr">关联属性</a>
				</li>
				<li>
					<a href="#tabs-goodsCat-spec">关联规格</a>
				</li>
				<li>
					<a href="#tabs-goodsCat-group">显示分组</a>
				</li>
				<li>
					<a href="#tabs-goodsCat-priceRange">价格区间</a>
				</li>
			</ul>
			<div id="tabs-goodsCat" class="form">
				<input type="hidden" id="level" />
				<input type="hidden" id="parentId" />
				<div class="field row">
					<label class="field label required">名称</label> 
					<input type="text" id="name" class="field value two wide" />
				</div>
				<div id="hasSpecDiv" class="field row" style="display: none;">
					<label class="field label">启用规格</label> 
					<div class="field group">
						<input id="hasSpec-T" type="radio" name="hasSpec" value="1" checked="checked"/>
						<label for="hasSpec-T">启用</label>
						<input id="hasSpec-F" type="radio" name="hasSpec" value="0" style="margin-left:8px;"/>
						<label for="hasSpec-F">禁用</label>
					</div>
				</div>
				<div id="extendColum" style="display: none;">
					<div class="field row">
						<label class="field label">计数单位</label> 
						<input type="text" id="unit" class="field value" />
					</div>
					<div class="field row">
						<label class="field label">重量单位</label> 
						<select class="field value" id="weightUnit"></select>
					</div>
				</div>
			</div>
			<div id="tabs-goodsCat-attr">
				<div class="group">
					<button id="btnAttrAdd" class="normal button">添加</button>
					<button id="btnAttrDel" class="normal button">去除</button>
				</div>
				<table id="goodsCatAttrGridAdd"></table>
				<div id="attrRefDialog" style="display: none;">
					<table id="attrRefList"></table>
					<div id="attrRefPager"></div>
					<div id="attrEnumValuesDialog" style="display: none;">
						<div id="attrEnumValuesBatchAddDialog" style="display: none;">
							<div class="group">
								输入值（每行一个）：
							</div>
							<div class="field row"  style="height:250px;">
								<textarea class="field value" style="height:230px;width:240px;"></textarea>
							</div>
						</div>
						<div class="group">
							<button id="btnAttrEnumAdd" class="normal button">添加单个</button>
							<button id="btnAttrEnumBatchAdd" class="normal button">添加多个</button>
							<button id="btnBrandDefBatchAdd" class="normal button">关联品牌</button>
							<div id="brandDefDialog">
								<div class="filter section">
									<div class="filter row">
										<label class="label">名称</label> <input id="brandName" class="input one wide" /> 
										<label class="label">代码</label> <input id="brandCode" class="input" /> 
										<button id="btnBrandDefQuery" class="button">查询</button>
									</div>
								</div>
								<table id="brandDefList"></table>
								<div id="brandDefPager"></div>
							</div>
						</div>
						<span style="padding-top: 10px;">&nbsp;</span>
						<input id="firstAttrEnumValue" type="hidden">
					</div>
				</div>
			</div>
			<div id="tabs-goodsCat-spec">
				<div class="group">
					<button id="btnSpecAdd" class="normal button">添加</button>
					<button id="btnSpecDel" class="normal button">去除</button>
				</div>
				<table id="goodsCatSpecGrid"></table>
				<div id="specRefDialog" style="display: none;">
					<div class="filter section">
						<div class="filter row">
							<label class="label">商品规格名称</label> 
							<input name="specName" type="text" class="input one wide"> 
							<span class="spacer"></span>
							<button class="button" id="btnSpecQuery" >查询</button>
						</div>
					</div>
					<table id="specRefList"></table>
					<div id="specRefPager"></div>
					<div id="specEnumValuesDialog" style="display: none;">
						<div id="specEnumValuesBatchAddDialog" style="display: none;">
							<div class="group">
								输入值（每行一个）：
							</div>
							<div class="field row"  style="height:250px;">
								<textarea class="field value two wide" style="height:230px;width:240px;"></textarea>
							</div>
						</div>
						<div id="colorDefDialog" style="display: none;">
							<div class="filter section">
								<div class="filter row">
									<label class="label">颜色名称</label> 
									<input id="colorName" type="text" class="input one wide"> 
									<span class="spacer"></span>
									<button class="button" id="btnColorQuery" >查询</button>
								</div>
							</div>
							<div id="colors"></div>
						</div>
						<div id="colorDefAddDialog" style="display:none" >
							<div class="form">
								<div class="field row">
									<label class="field label required">颜色名称</label> 
									<input type="text" id="colorDefName" class="field one half value wide" />
								</div>
								<div class="field row" style="display: inline-block;">
									<label class="field  label required">选择颜色</label> 
									<input type="text" id="colorDefExpr" class="field one half value wide" />
								</div>
						    </div>
					    </div>
						<div class="group">
							<button id="btnSpecEnumAdd" class="normal button">添加单个</button>
							<button id="btnColorAdd" class="normal button">添加单个</button>
							<button id="btnSpecEnumBatchAdd" class="normal button">添加多个</button>
							<button id="btnColorBatchAdd" class="normal button">导入颜色</button>
						</div>
						<span style="padding-top: 10px;">&nbsp;</span>
						<input id="firstSpecEnumValue" type="hidden">
					</div>
				</div>
			</div>
			<div id="tabs-goodsCat-group">
				<div class="group">
					<button id="btnGroupAdd" class="normal button">添加</button>
				</div>
				<div id="groupDialog" class="form" style="display: none;">
					<div class="field row">
						<label class="field labele">名称：</label>
						<input type="hidden" id="groupId"/>
						<input type="text" id="groupName" class="field value one half wide"/>
					</div>
					<div class="field row" style="height:40px;">
						<label class="field labele">描述：</label>
						<textarea id="groupDesc" class="field value three wide" style="line-height: 30px;height:30px;"></textarea>
					</div>
					<div class="field row">
						<div>
							<div class='simple block'><div class='header'>规格属性</div></div>
							<ul id="groupAttrs" class="list"></ul>
						</div>
					</div>
				</div>
				<div id="groupsAddShow"></div>
			</div>
			<div id="tabs-goodsCat-priceRange">
				<div class="group">
					<button id="btnRangeEdit" class="normal button">新增</button>
				</div>
				<table id="priceRangeList"></table>
				<div id="priceRangePager"></div>
				<div id="priceRangeDialog" style="display: none;">
					<div class="group">
						<button id="btnRangeAdd" class="normal button">添加</button>
						<button id="btnRangeDel" class="normal button">去除</button>
					</div>
					<div class="form">
						<div class="filter section">
							<div style="text-align: center; vertical-align: middle;">
								<table class="gridtable">
									<thead><tr>
										<th width="40%"><label class="normal label">价格下限</label></th>
										<th width="40%"><label class="normal label">价格上限</label></th>
										<th width="20%"><label class="normal label">操作</label></th>
									</tr></thead>
									<tbody id="grade_tbody"></tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/colorpicker/evol.colorpicker.js"></script>
	<script type="text/javascript">
		//添加窗口
		var addDialog;
		//分类属性展示页面
		var attrRefDialog;
		var operate = "add";
		var addTabs;
		var updateTabs;
		//覆盖渲染
		function renderDetail(fromId,toId,data) {
			var tplHtml = $id(fromId).html();
			var theTpl = laytpl(tplHtml);
			var htmlStr = theTpl.render(data);
			$id(toId).html(htmlStr);
		}
		//追加渲染
		function appendRenderDetail(fromId,toId,data) {
			var tplHtml = $id(fromId).html();
			var theTpl = laytpl(tplHtml);
			var htmlStr = theTpl.render(data);
			$id(toId).append(htmlStr);
		}
		
		var treeNodeId, treeNodeName;
		
		//表单代理
		var formProxy = FormProxy.newOne();
		//注册表单控件
		formProxy.addField({
			id : "name",
			required : true,
			rules : ["maxLength[30]"]
		});
		formProxy.addField({
			id : "unit",
			rules : ["maxLength[15]"]
		});
		formProxy.addField({
			id : "weightUnit",
			rules : ["maxLength[15]"]
		});
		
		//表单代理
		var updateFormProxy = FormProxy.newOne();
		//注册表单控件
		updateFormProxy.addField({
			id : "show-name",
			required : true,
			rules : ["maxLength[30]"]
		});
		updateFormProxy.addField({
			id : "show-unit",
			rules : ["maxLength[15]"]
		});
		updateFormProxy.addField({
			id : "show-weightUnit",
			rules : ["maxLength[15]"]
		});
		
		//表单代理
		var updateParentFormProxy = FormProxy.newOne();
		//注册表单控件
		updateParentFormProxy.addField({
			id : "update-parentName",
			required : true,
			rules : ["maxLength[30]"]
		});
		
		//当前选择的节点
		var selectedNode = null;
		
		//地区树配置
		var setting = {
			view : {
				dblClickExpand : false, // 关闭双击展开节点的功能。默认值：true
				showLine : false, // 隐藏连接线。默认值：true
				selectedMulti : false
			// true：多选；false：单选；默认值：true
			},
			data : {
				key : {
					name : "name",
					children : "children"
				},
				simpleData : {
					enable : true,
					idKey : "id",
					pIdKey : "parentId",
					rootPId : 0
				}
			},
			callback : {
				beforeExpand : beforeExpand, // 父节点展开之前的事件回调函数
				onExpand : onExpand, // 节点展开时的回调函数
				onClick : onClick
			// 绑定单击事件
			}
		};
		
		//控制树图标
		function showIconForTree(treeId, treeNode) {
			// 判断当前被选中的节点的展开/折叠状态
			var zTree = $.fn.zTree.getZTreeObj("goodsCatData");
			var selectedNode = zTree.getSelectedNodes();
			if (selectedNode.length > 0) {
				isOpen = selectedNode[0].open;
				if (isOpen) {
					selectedNode[0].iconSkin = getResUrl("/lib/ztree/image/arrow/down.png");
					return true;
				} else {
					selectedNode[0].iconSkin = getResUrl("/lib/ztree/image/arrow/right.png");
					return true;
				}
			}
		}
		
		//父节点展开之前的事件回调函数
		var curExpandNode = null;
		function beforeExpand(treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("goodsCatData");
			var pNode = curExpandNode ? curExpandNode.getParentNode() : null;
			var treeNodeP = treeNode.parentTId ? treeNode.getParentNode() : null;
			for (var i = 0, len = !treeNodeP ? 0 : treeNodeP.children.length; i < len; i++) {
				if (treeNode !== treeNodeP.children[i]) {
					zTree.expandNode(treeNodeP.children[i], false);
				}
			}
			while (pNode) {
				if (pNode === treeNode) {
					break;
				}
				pNode = pNode.getParentNode();
			}
			if (!pNode) {
				singlePath(treeNode);
			}
		}
		
		//节点展开时的回调函数
		function onExpand(event, treeId, treeNode) {
			curExpandNode = treeNode;
		}
		
		//绑定单击事件
		function onClick(e, treeId, treeNode) {
			operate = 'show';
			$id("btnUpdate").show();
			$id("btnDelete").show();
			cancelTreeNode();
			var zTree = $.fn.zTree.getZTreeObj("goodsCatData");
			selectedNode = zTree.getSelectedNodes();
			if (selectedNode[0].level == goodsCategLevels-1) {
				$id("btnAddChild").prop("disabled", true);
			} else {
				$id("btnAddChild").prop("disabled", false);
			}
			// 判断当前被选中的节点的展开/折叠状态
			var isOpen = false;
			if (selectedNode.length > 0) {
				isOpen = selectedNode[0].open;
				if (isOpen) {
					isOpen = false;
				} else {
					isOpen = true;
				}
				if (selectedNode[0].level > 0) {
					var pNode = selectedNode[0].getParentNode();
					$("#selNodePName").text(pNode.name);
					$("#preTreeNodeDiv").show();
				} else {
					$("#preTreeNodeDiv").hide();
				}
				$id("selNodeNameLabel").text(selectedNode[0].name);
				if(selectedNode[0].level == goodsCategLevels-1){
					updateFormProxy.setValue("show-name",selectedNode[0].name);
					updateFormProxy.setValue("show-unit",selectedNode[0].unit);
					updateFormProxy.setValue("show-weightUnit",selectedNode[0].weightUnit);
					showGoodsCatPriceRange(selectedNode[0].id);
					showGoodsCatAttrGroup(selectedNode[0].id);
					$id("showParents").hide();
					$id("showLevel3").show();
				}else{
					$id("showLevel3").hide();
					$id("showParents").show();
				}
			}
		
			zTree.expandNode(treeNode, isOpen, null, null, true);
		}
		
		//保持展开单一路径
		function singlePath(newNode) {
			if (newNode === curExpandNode) {
				return;
			}
		
			var rootNodes, tmpRoot, tmpTId=null, n;
			var zTree = $.fn.zTree.getZTreeObj("goodsCatData");
			if (!curExpandNode) {
				tmpRoot = newNode;
				while (tmpRoot) {
					tmpTId = tmpRoot.tId;
					tmpRoot = tmpRoot.getParentNode();
				}
				rootNodes = zTree.getNodes();
				for (var i = 0, len = rootNodes.length; i < len; i++) {
					n = rootNodes[i];
					if (n.tId != tmpTId) {
						zTree.expandNode(n, false);
					}
				}
			} else if (curExpandNode && curExpandNode.open) {
				if (newNode.parentTId === curExpandNode.parentTId) {
					zTree.expandNode(curExpandNode, false);
				} else {
					var newParents = [];
					while (newNode) {
						newNode = newNode.getParentNode();
						if (newNode === curExpandNode) {
							newParents = null;
							break;
						} else if (newNode) {
							newParents.push(newNode);
						}
					}
					if (newParents != null) {
						var oldNode = curExpandNode;
						var oldParents = [];
						while (oldNode) {
							oldNode = oldNode.getParentNode();
							if (oldNode) {
								oldParents.push(oldNode);
							}
						}
						if (newParents.length > 0) {
							zTree.expandNode(oldParents[Math.abs(oldParents.length
									- newParents.length) - 1], false);
						} else {
							zTree.expandNode(oldParents[oldParents.length - 1], false);
						}
					}
				}
			}
			curExpandNode = newNode;
		}
		
		//更新节点数据
		function updateNodeName(obj) {
			var zTree = $.fn.zTree.getZTreeObj("goodsCatData");
			var selectedNode = zTree.getSelectedNodes();
		
			if (selectedNode.length == 0) {
				Layer.warning("请先选择一个分类");
			}
			for (var i = 0, len = selectedNode.length; i < len; i++) {
				selectedNode[i].name = obj.name;
				selectedNode[i].unit = obj.unit;
				selectedNode[i].weightUnit = obj.weightUnit;
				zTree.updateNode(selectedNode[i]);
			}
		}
		
		//获取选择节点及其所有子节点
		function getChildNodes(treeNode) {
			var ztree = $.fn.zTree.getZTreeObj("goodsCatData");
			var childNodes = ztree.transformToArray(treeNode);
			var nodes = [];
			for (var i = 0; i < childNodes.length; i++) {
				nodes.add(childNodes[i].id);
			}
			return nodes;
		
		}
	
		// 加载重量单位
		function loadWeightUnit () {
			loadSelectData("weightUnit", getDictSelectList("weightUnit", "", "", ""));
			loadSelectData("show-weightUnit", getDictSelectList("weightUnit", "", "", ""));
		}
		
		//加载分类树
		function loadGoodsCatTree(name) {
			var ajax = Ajax.post("/categ/goodsCat/list/get");
			ajax.data({
				name : name || ""
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data;
					$.fn.zTree.init($("#goodsCatData"), setting, data);
					var zTree = $.fn.zTree.getZTreeObj("goodsCatData");
					// 默认选中并展开第一个节点
					if(data.length>0){
						$id("btnAddRoot").hide();
						$id("btnAddChild").show();
						$id("btnAddBrother").show();
						var node = zTree.getNodeByParam("id", data[0].id, null);
						zTree.selectNode(node, false);
						zTree.setting.callback.onClick(null, data[0].id, node);
					}else{
						$id("btnAddRoot").show();
						$id("btnAddChild").hide();
						$id("btnAddBrother").hide();
						var newNode = {
								id : 0,
								name : '无',
								level : 0,
								parentId : 0
							};
						zTree.selectNode(newNode, false);
						zTree.setting.callback.onClick(null, 0, newNode);
					}
					
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		//打开增加一级dialog
		function openAddRootDialog(){
			$id("name").val("");
			$id("unit").val("");
			$id("weightUnit").val("");
			$id("update-parentName").val("");
			cancelTreeNode();
			operate = "add";
			if (selectedNode == null) {
				Layer.warning("请先选择一个分类");
			} else {
				$id("hasSpecDiv").css("display", "none");
				$id("level").val(selectedNode[0].level + 1);
				$id("parentId").val(selectedNode[0].id);
				initParentCatDialog();
				parentDialog.dialog("open");
				$id("extendColum").hide();
			}
		}
		
		//打开增加下级dialog
		function openAddChildDialog(){
			$id("name").val("");
			$id("unit").val("");
			$id("weightUnit").val("");
			$id("update-parentName").val("");
			cancelTreeNode();
			operate = "add";
			if (selectedNode == null) {
				Layer.warning("请先选择一个分类");
			} else {
				var selectNodeLevel = selectedNode[0].level;
				$id("level").val(selectNodeLevel + 2);
				$id("parentId").val(selectedNode[0].id);
				var selectNodeLevel = selectedNode[0].level;
				var addNodeLevel = selectNodeLevel + 1;
				//
				if(addNodeLevel != goodsCategLevels-1){
					$id("hasSpecDiv").css("display", "none");
					initParentCatDialog();
					parentDialog.dialog("open");
					$id("extendColum").hide();
				}else{
					$id("hasSpecDiv").css("display", "block");
					$id("extendColum").show();
					radioSet("isGroup",0);
					addDialog.dialog("open");
					loadGoodsCatAttr();
					loadGoodsCatSpec();
					loadGoodsCatPriceRange();
				}
			}
		}
		
		//打开增加同级dialog
		function openAddBrotherDialog() {
			$id("name").val("");
			$id("unit").val("");
			$id("weightUnit").val("");
			$id("update-parentName").val("");
			cancelTreeNode();
			operate = "add";
			if (selectedNode == null) {
				Layer.warning("请先选择一个分类");
			} else {
				var selectNodeLevel = selectedNode[0].level;
				$id("level").val(selectNodeLevel + 1);
				$id("parentId").val(selectedNode[0].parentId);
				if (selectedNode[0].level == goodsCategLevels-1) {
					radioSet("isGroup",0);
					$id("extendColum").show();
					addDialog.dialog("open");
					loadGoodsCatAttr();
					loadGoodsCatSpec();
					loadGoodsCatPriceRange();
				}else{
					$id("extendColum").hide();
					initParentCatDialog();
					parentDialog.dialog("open");
				}
			}
		}
		
		//添加分类
		function addParentGoodsCat() {
			var vldResult = formProxy.validateAll();
			if (!vldResult) {
				return;
			}
			var ajax = Ajax.post("/categ/goodsCat/add/do");
			var name = formProxy.getValue("name");
			var level = intGet("level");
			var parentId = $id("parentId").val();
			var idPath = "";
			if(parseInt(level) == 1){
				idPath = "0";
			}
			//
			var goodsCat = {
					name : name,
			    	level : level,
					parentId : parseInt(parentId),
					hasSpec : false,
					idPath : idPath
				};
			var goodsCats = [];
			goodsCats.add(goodsCat);
			//
			var postData = {
					goodsCat : goodsCats
			};
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					var treeObj = $.fn.zTree.getZTreeObj("goodsCatData");
					var newNode = {
						id : result.data,
						name : name,
						level : 1,
						parentId : parentId
					};
					var pNode = treeObj.getNodeByParam("id", parentId, null);
					newNode = treeObj.addNodes(pNode, newNode);
					treeObj.selectNode(newNode[0], false);
					treeObj.setting.callback.onClick(null, newNode[0].id, newNode[0]);
					$id("btnAddRoot").hide();
					$id("btnAddChild").show();
					$id("btnAddBrother").show();
					parentDialog.dialog("close");
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
		}
		function addGoodsCat() {
			var vldResult = formProxy.validateAll();
			if (!vldResult) {
				return;
			}
			var ajax = Ajax.post("/categ/goodsCat/add/do");
			var name = formProxy.getValue("name");
			var parentId = intGet("parentId");
			var unit = formProxy.getValue("unit");
			var weightUnit = formProxy.getValue("weightUnit") || null;
			var goodsCatAttrs = getGoodsCatAttr();
			var goodsCatSpecs = getGoodsCatSpec();
			var goodsCatPriceRanges = getPriceRanges();
			var hasSpec = radioGet("hasSpec");
			var level = intGet("level");
			var treeObj = $.fn.zTree.getZTreeObj("goodsCatData");
			var idPath = "";
			idPath = parentId;
			var goodsCat = {
					name : name,
			    	level : level,
					parentId : parentId,
					pId : parentId,
					idPath : idPath,
					unit : unit,
					weightUnit : weightUnit,
					hasSpec : hasSpec,
					goodsCatPriceRanges : goodsCatPriceRanges
				};
			var goodsCats = [];
			goodsCats.add(goodsCat);
			//
			var postData = {
					goodsCat : goodsCats,
					goodsCatAttrs : JSON.encode(goodsCatAttrs, true),
					attrsEnumValues : JSON.encode(attrEnumMaps, true),
					goodsCatSpecs : JSON.encode(goodsCatSpecs, true),
					specsEnumValues : JSON.encode(specEnumMaps, true),
					groups : JSON.encode(groupMaps, true),
					groupData : JSON.encode(groupPostDataMap, true)
			};
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					var newNode = {
						id : result.data,
						name : name,
						level : level,
						parentId : parentId,
						unit : unit,
						weightUnit : weightUnit
					};
					var pNode = treeObj.getNodeByParam("id", parentId, null);
					newNode = treeObj.addNodes(pNode, newNode);
					treeObj.selectNode(newNode[0], false);
					treeObj.setting.callback.onClick(null, newNode[0].id, newNode[0]);
					addDialog.dialog("close");
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
		}
		
		//展示修改界面
		function showUpdateDialog() {
			if(!$id('btnAddRoot').is(':hidden')){
				return;	
			}
			operate = "update";
			$id("btnUpdate").hide();
			$id("btnDelete").hide();
			var sNode = $id("selNodeNameLabel");
			if (sNode.text() == null || sNode.text() == "") {
				Layer.warning("请先选择一个节点");
			} else {
				sNode.hide();
				if (selectedNode[0].level < goodsCategLevels-1) {
					$id("update-parentName").val(sNode.text());
					$id("update-parentName").show();
				} else {
					$("#theShowTabsCtrl input").removeProp("disabled");
					$("#theShowTabsCtrl select").removeProp("disabled");
					$id("goodsCatAttrUpdateDialog").show();
					$id("goodsCatAttrShowDialog").hide();
					$id("goodsCatSpecUpdateDialog").show();
					$id("goodsCatSpecShowDialog").hide();
					$id("priceRangeUpdateDialog").show();
					$id("priceRangeShowDialog").hide();
					$id("groupsUpdateShow").show();
					$id("groupShow").hide();
					groupMaps.clear();
					groupPostDataMap.clear();
					updateGoodsCatPriceRange(selectedNode[0].id);
					showGoodsCatAttrGroup(selectedNode[0].id);
				}
				$id("btnCancel").show();
				$id("btnSave").show();
			}
		}
		
		//取消修改
		function cancelTreeNode() {
			operate = 'show';
			$id("selNodeNameLabel").show();
			$id("update-parentName").hide();
			$("#theShowTabsCtrl input").attr("disabled",true);
			$("#theShowTabsCtrl select").attr("disabled",true);
			//
			$id("btnCancel").hide();
			$id("btnSave").hide();
			//
			attrEnumMaps.clear();
			specEnumMaps.clear();
			groupMaps.clear();
			groupPostDataMap.clear();
			$id("goodsCatAttrUpdateDialog").hide();
			$id("goodsCatAttrShowDialog").show();
			$id("goodsCatSpecUpdateDialog").hide();
			$id("goodsCatSpecShowDialog").show();
			$id("priceRangeUpdateDialog").hide();
			$id("priceRangeShowDialog").show();
			$id("groupsUpdateShow").hide();
			$id("groupShow").show();
			$id("groupsUpdateShow").find('.module').remove();
		}
		
		//保存修改
		function updateGoodsCat() {
			if (selectedNode == null) {
				Layer.warning("请先选择一个分类");
			} else {
				var vldResult = false;
				var postData = null;
				if (selectedNode[0].level < goodsCategLevels-1) {
					vldResult = updateParentFormProxy.validateAll();
					if (!vldResult) {
						return;
					}
					postData = {
					    	goodsCat :{
					    		id : selectedNode[0].id,
								name : updateParentFormProxy.getValue("update-parentName"),
								hasSpec : false,
						    	unit : ""
							}
						};
				} else {
					vldResult = updateFormProxy.validateAll();
					if (!vldResult) {
						return;
					}
					var goodsCatAttrs = getGoodsCatAttr();
					var goodsCatSpecs = getGoodsCatSpec();
					var goodsCatPriceRanges = getPriceRanges();
					var hasSpec = false;
					if(goodsCatSpecs.length>0){
						hasSpec = true;
					}
					postData = {
							goodsCat :{
								id : selectedNode[0].id,
								hasSpec : hasSpec,
								name : updateFormProxy.getValue("show-name"),
						    	unit : updateFormProxy.getValue("show-unit") || "",
						    	weightUnit : updateFormProxy.getValue("show-weightUnit") || null,
						    	goodsCatPriceRanges : goodsCatPriceRanges
							},
							goodsCatAttrs : JSON.encode(goodsCatAttrs, true),
							attrsEnumValues : JSON.encode(attrEnumMaps, true),
							goodsCatSpecs : JSON.encode(goodsCatSpecs, true),
							specsEnumValues : JSON.encode(specEnumMaps, true),
							groups : JSON.encode(groupMaps, true),
							groupData : JSON.encode(getGroupUpdateAttrs(), true)
					};
				}
				if (!vldResult) {
					return;
				}
				
				var zTree = $.fn.zTree.getZTreeObj("goodsCatData");
				var ajax = Ajax.post("/categ/goodsCat/update/do");
				ajax.data(postData);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						// 更新节点名称
						updateNodeName(postData.goodsCat);
						cancelTreeNode();
						var node = zTree.getNodeByParam("id", postData.goodsCat.id, null);
						zTree.selectNode(node, false);
						zTree.setting.callback.onClick(null, postData.goodsCat.id, node);
						Layer.info(result.message);
					} else {
						Layer.warning(result.message);
					}
				});
				ajax.go();
			}
		}
		
		//删除
		function deleteGoodsCat() {
			if(!$id('btnAddRoot').is(':hidden')){
				return;	
			}
			if (selectedNode == null) {
				Layer.warning("请先选择一个分类");
			} else {
				Layer.confirm("删除该分类将会删除该分类下所属的下级分类，确定要继续吗？", function() {
					var nodes = getChildNodes(selectedNode);
					var ajax = Ajax.post("/categ/goodsCat/delete/do");
					ajax.data(nodes);
					ajax.done(function(result, jqXhr) {
						if (result.type == "info") {
							Layer.msgSuccess(result.message);
							//var treeObj = $.fn.zTree.getZTreeObj("goodsCatData");
							loadGoodsCatTree();
							//treeObj.removeChildNodes(selectedNode);	
							//treeObj.removeNode(selectedNode);
						} else {
							Layer.msgWarning(result.message);
						}
					});
					ajax.go();
				});
			}
		}
		
		//------------------------商品分类属性参照列表-------------------------
		var attrRefTypeKetToText = new KeyMap();
		attrRefTypeKetToText.add("Text","单行文本");
		attrRefTypeKetToText.add("MlText","多行文本");
		attrRefTypeKetToText.add("Bool","布尔");
		attrRefTypeKetToText.add("Int","整数");
		attrRefTypeKetToText.add("Float","浮点");
		attrRefTypeKetToText.add("Date","日期");
		attrRefTypeKetToText.add("DateTime","日期时间");
		attrRefTypeKetToText.add("Enum","枚举选择");
		
		var attrRefTypeTextToKet = new KeyMap();
		attrRefTypeTextToKet.add("单行文本","Text");
		attrRefTypeTextToKet.add("多行文本","MlText");
		attrRefTypeTextToKet.add("布尔","Bool");
		attrRefTypeTextToKet.add("整数","Int");
		attrRefTypeTextToKet.add("浮点","Float");
		attrRefTypeTextToKet.add("日期","Date");
		attrRefTypeTextToKet.add("日期时间","DateTime");
		attrRefTypeTextToKet.add("枚举选择","Enum");
		
		var attrRefGrid = null;

		//加载数据列表
		function loadAttrRefData() {
			var existIds = [];
			if(operate=='update'){
				existIds = goodsCatAttrUpdateGrid.getCol("attrRef.id");
			}
			else if(operate=='add'){
				existIds = goodsCatAttrGrid.getCol("attrRef.id");
			}
			var ids = [];
			for(var i = 0;i<existIds.length;i++){
				ids.add(parseInt(existIds[i]));
			}
			var postData = null;
			if(ids.length>0){
				postData = JSON.encode({"uncontains":ids},true);
			}
			//加载jqGridCtrl
			if(attrRefGrid ==null){
				attrRefGrid = $id("attrRefList").jqGrid(
						{
							url : getAppUrl("/categ/attrRef/list/get"),
							contentType : 'application/json',
							mtype : "post",
							datatype : 'json',
							postData :{filterStr : postData},
							colNames : [ "id", "属性名称", "内容类型", "是否多选", "是否品牌"],
							colModel : [{name : "id",index : "id",hidden : true},
										{name : "name",width : 150,align : 'left'},
										{name : "type",width : 150,align : 'left',
											formatter : function(cellValue, option, rowObject) {
												return attrRefTypeKetToText.get(cellValue);
											}
										},
										{name : "multiFlag",width : 80,align : 'left',
											formatter : function(cellValue, option, rowObject) {
												return cellValue==true?'是':'否';
											}
										},
										{name : "brandFlag",width : 80,align : 'left',
											formatter : function(cellValue, option, rowObject) {
												return cellValue==true?'是':'否';
											}
										} 
										],
							multiselect : true,
							pager : "#attrRefPager",
							height:260
						});
			}else{
				attrRefGrid.jqGrid("setGridParam", {postData :{filterStr : postData}}).trigger("reloadGrid");
			}
		}
		
		function openAttrRefDialog(){
			loadAttrRefData();
			attrRefDialog.dialog("open");
		}
		//选中属性并返回给商品分类属性关联Dialog
		function fetchAttrRef(){
			var ids=attrRefGrid.jqGrid("getGridParam","selarrrow");
			for(var i = 0;i<ids.length;i++){
				var rowData = attrRefGrid.jqGrid("getRowData",ids[i]);
				var data = {
						id : null,
						keyFlag : false,
						attrRef : {
							id : rowData.id,
							name : rowData.name,
							type : attrRefTypeTextToKet.get(rowData.type),
							multiFlag : (rowData.multiFlag=='是'?true:false) ,
							brandFlag : (rowData.brandFlag=='是'?true:false)
						}
				};
				if(operate=='update'){
					goodsCatAttrUpdateGrid.addRowData("new-"+ids[i],data);
				}
				else if(operate=='add'){
					goodsCatAttrGrid.addRowData("new-"+ids[i],data);
				}
			}
			attrRefDialog.dialog("close");
		}
		
		
		//------------------------商品分类规格参照-------------------------
		
		var specRefDialog;
		var specRefList = null;
		// 初始化商品规格数据
		function loadSpecRefGridData() {
			var name = $("#specRefDialog input[name=specName]").val();
			var existIds = [];
			if(operate=='update'){
				existIds = goodsCatSpecGridUpdate.getCol("specRef.id");
			}
			else if(operate=='add'){
				existIds = goodsCatSpecGrid.getCol("specRef.id");
			}
			var ids = [];
			for(var i = 0;i<existIds.length;i++){
				ids.add(parseInt(existIds[i]));
			}
			var postData = null;
			if(ids.length>0){
				postData = JSON.encode({"uncontains":ids,"name":name},true);
			}else{
				postData = JSON.encode({"name":name},true);
			}
			if(specRefList == null){
				specRefList = $("#specRefList").jqGrid({
					url : getAppUrl("/categ/spec/ref/list/get"),
					postData :{filterStr : postData},
					contentType : 'application/json',
					mtype : "post",
					datatype : 'json',
					height : 260,
					colNames : [ "id", "规格名称", "使用颜色色值表", "销售属性"],
					colModel : [ {name : "id",hidden : true,align : "center"}, 
					             {name : "name",align : "left"}, 
					             {name : "colorFlag",width : 120,align : "center",formatter : function (cellValue) {
										return cellValue==true?'是':'否';}},
					             {name : "salesFlag",width : 80,align : "center",formatter : function (cellValue) {
										return cellValue==true?'是':'否';}}
					             ],
					multiselect : true, // 定义是否可以多选
					pager : "#specRefPager"
				});
			}else{
				specRefList.jqGrid("setGridParam", {postData :{filterStr : postData}}).trigger("reloadGrid");
			}
		}
		
		//打开规格列表界面
		function openSpecRefDialog(){
			$("#specRefDialog input[name=specName]").val("");
			loadSpecRefGridData();
			specRefDialog.dialog("open");
		}
		
		//选中规格并返回给商品分类规格关联Dialog
		function fetchSpecRef(){
			var ids=specRefList.jqGrid("getGridParam","selarrrow");
			for(var i = 0;i<ids.length;i++){
				var rowData = specRefList.jqGrid("getRowData",ids[i]);
				var data = {
						id : null,
						refId : rowData.id,
						specRef : {
							id : rowData.id,
							name : rowData.name,
							colorFlag : (rowData.colorFlag=='是'?true:false)
						}
				};
				if(operate=='update'){
					goodsCatSpecGridUpdate.addRowData("new-"+ids[i],data);
				}
				else if(operate=='add'){
					goodsCatSpecGrid.addRowData("new-"+ids[i],data);
				}
			}
			specRefDialog.dialog("close");
		}
		
		
		//------------------------商品分类属性关联列表-------------------------
		//jqGrid缓存变量
		var goodsCatAttrGrid = null;
		var goodsCatAttrShowGrid = null;
		var goodsCatAttrUpdateGrid = null;
		var attrEnumValuesDialog;
		var attrEnumValuesBatchAddDialog;
		var attrEnumMaps = new KeyMap();//枚举值Map
		var cacheAttrEnumMaps = new KeyMap();//枚举值Map
		var emunAddId = 0;//设定枚举值的属性Id
		var brandFlag = false;
		var brandDefGrid = null;
		var brandDefDialog;
		
		function showGoodsCatAttr(id){
			var catId = 0;
			if(null!=id && ""!=id){
				catId = parseInt(id);
			}
			if(goodsCatAttrShowGrid ==null){
				goodsCatAttrShowGrid = $("#goodsCatAttrShow").jqGrid({
				      url : getAppUrl("/categ/goodsCatAttr/list/get/by/catId"),  
				      contentType : 'application/json', 
				      mtype : "post",  
				      postData :{filterStr : JSON.encode({"catId":catId},true)},
				      datatype : 'json',
				      height : "100%",
					  width : "100%",
				      colNames : ["id","分组","属性名称", "内容类型","关键属性","是否搜索","是否品牌","是否多选","属性设置"],  
				      colModel : [{name:"id", index:"id", hidden:true},
				                  {name:"groupId", index:"groupId", hidden:true, formatter : function (cellValue,option,rowObject) {
						            	 var html ="<li><label>"+rowObject.attrRef.name+"</label></li>";
						     			 $("#showTabs-"+cellValue).append(html);
						            	 return cellValue;
										}},
				                  {name:"attrRef.name", index:"attrRef.name",width:170,align : 'left'}, 
				                  {name:"attrRef.type", index:"attrRef.type",width:100,align : 'left',formatter : function(cellValue,option,rowObject) {
										return attrRefTypeKetToText.get(cellValue);
									}},
				                  {name:"keyFlag", index:"keyFlag",width:80,align : 'center',formatter : function (cellValue,option,rowObject) {
										return cellValue==true?'是':'否';}
									}, 
									{name:"searchFlag", index:"searchFlag",width:80,align : 'center',formatter : function (cellValue,option,rowObject) {
										return cellValue==true?'是':'否';}
									},
									{name:"brandFlag", index:"brandFlag",width:80,align : 'center',formatter : function (cellValue,option,rowObject) {
										return cellValue==true?'是':'否';}
									},
									{name:"multiFlag", index:"multiFlag",width:80,align : 'center',formatter : function (cellValue,option,rowObject) {
										return cellValue==true?'是':'否';}
									},
									{name:'attrRef.type',index:'attrRef.type',width:150,align:"left", formatter : function (cellValue,option,rowObject) {
										if(cellValue=="Int" || cellValue == "Float"){
											return "单位名称<input name='unit' value='"+rowObject.unit+"'/>";
										}else if(cellValue=="Enum"){
											var enumValues = rowObject.goodsCatAttrItems;
											var options = "";
											for(var i = 0;i<enumValues.length;i++){
												var enumValue = enumValues[i];
												options+= "<option>"+enumValue.value+"</option>";
											}
											return "<select style='width:130px;'>"+options+"</select>";
										}else{
											return "";
										}
									}}
				                  ]
				});
			}else{
				goodsCatAttrShowGrid.jqGrid("setGridParam", {postData :{filterStr : JSON.encode({"catId":catId},true)}}).trigger("reloadGrid");
			}
		}
		
		function loadGoodsCatAttr(){ 
			var catId = 0;
			//加载jqGridCtrl
			if(goodsCatAttrGrid ==null){
				goodsCatAttrGrid = $("#goodsCatAttrGridAdd").jqGrid({
				      url : getAppUrl("/categ/goodsCatAttr/list/get/by/catId"),  
				      contentType : 'application/json', 
				      mtype : "post",  
				      postData :{filterStr : JSON.encode({"catId":catId},true)},
				      datatype : 'json',
				      height : "100%",
					  width : "100%",
				      colNames : ["id", "属性Id","属性名称", "内容类型","关键属性","是否搜索","是否品牌","是否多选","属性设置"],  
				      colModel : [{name:"id", index:"id", hidden:true},
				                  {name:"attrRef.id", index:"attrRef.id", hidden:true},
				                  {name:"attrRef.name", index:"attrRef.name",width:170,align : 'left'}, 
				                  {name:"attrRef.type", index:"attrRef.type",width:100,align : 'left',formatter : function(cellValue,option,rowObject) {
										return attrRefTypeKetToText.get(cellValue);
									}},
				                  {name:"keyFlag", index:"keyFlag",width:80,align : 'left',formatter : function (cellValue,option,rowObject) {
										return "<input type='checkbox' name='keyFlag' class='field value' "+(cellValue==true?'checked':'')+" value='1'/>&nbsp;&nbsp;是";}
									},
									{name:"searchFlag", index:"searchFlag",width:80,align : 'left',formatter : function (cellValue,option,rowObject) {
										return "<input type='checkbox' name='searchFlag' class='field value' "+(cellValue==true?'checked':'')+" value='1'/>&nbsp;&nbsp;是";}
									},
									{name:"brandFlag", index:"brandFlag",width:80,align : 'left',formatter : function (cellValue,option,rowObject) {
										var brandFlag = rowObject.attrRef.brandFlag
										return "<input type='checkbox' name='brandFlag' disabled='disabled' class='field value' "+(brandFlag==true?'checked':'')+" value='1'/>&nbsp;&nbsp;是";}
									},
									{name:"multiFlag", index:"multiFlag",width:80,align : 'left',formatter : function (cellValue,option,rowObject) {
										var multiFlag = rowObject.attrRef.multiFlag
										return "<input type='checkbox' name='multiFlag' disabled='disabled' class='field value' "+(multiFlag==true?'checked':'')+" value='1'/>&nbsp;&nbsp;是";}
									},
									{name:'attrRef.type',index:'attrRef.type',width:150,align:"center", formatter : function (cellValue,option,rowObject) {
										if(cellValue=="Int" || cellValue == "Float"){
											return "单位名称<input name='unit' maxlength = 15/>";
										}else if(cellValue=="Enum"){
											var brandFlag = rowObject.attrRef.brandFlag;
											return "<a href='javascript:void(0)' onclick='openAttrEnumDialog("+rowObject.attrRef.id+","+brandFlag+",\""+rowObject.attrRef.name+"\")'>设定枚举值</a>";
										}else{
											return "";
										}
									}}
				                  ],  
					 multiselect : true,
					 multikey:'ctrlKey',
					 });
			}else{
				goodsCatAttrGrid.jqGrid("setGridParam", {postData :{filterStr : JSON.encode({"catId":catId},true)}}).trigger("reloadGrid");
			}
		}
		
		function updateGoodsCatAttr(id){ 
			var catId = 0;
			if(null!=id && ""!=id){
				catId = parseInt(id);
			}
			//加载jqGridCtrl
			if(goodsCatAttrUpdateGrid ==null){
				goodsCatAttrUpdateGrid = $("#goodsCatAttrUpdate").jqGrid({
				      url : getAppUrl("/categ/goodsCatAttr/list/get/by/catId"),  
				      contentType : 'application/json', 
				      mtype : "post",  
				      postData :{filterStr : JSON.encode({"catId":catId},true)},
				      datatype : 'json',
				      height : "100%",
					  width : "100%",
				      colNames : ["id", "属性Id","分组Id","属性名称", "内容类型","关键属性","是否搜索","是否品牌","是否多选","属性设置"],  
				      colModel : [{name:"id", index:"id", hidden:true},
				                  {name:"attrRef.id", index:"attrRef.id", hidden:true},
				                  {name:"groupId", index:"groupId", hidden:true, formatter : function (cellValue,option,rowObject) {
				                	  	var  dataId = "attr"+"-"+rowObject.attrRef.id;
				                	  	var name = rowObject.attrRef.name;
				                	  	var html = "<li><label>"+name+"</label><span onclick='deleteGroupData(this);' class='ui-icon ui-icon-close' style='cursor: pointer;float:left;' role='presentation' data-id='"+dataId+"'>"+name+"</span></li>";
						            	if(cellValue){
						            		 $("#group-"+cellValue).find("ul").append(html);
						            	 }
						            	 return cellValue;
										}},
				                  {name:"attrRef.name", index:"attrRef.name",width:170,align : 'left'}, 
				                  {name:"attrRef.type", index:"attrRef.type",width:100,align : 'left',formatter : function(cellValue,option,rowObject) {
										return attrRefTypeKetToText.get(cellValue);
									}},
				                  {name:"keyFlag", index:"keyFlag",width:80,align : 'left',formatter : function (cellValue,option,rowObject) {
										return "<input type='checkbox' name='keyFlag' class='field value' "+(cellValue==true?'checked':'')+" value='1'/>&nbsp;&nbsp;是";}
									},  
									{name:"searchFlag", index:"searchFlag",width:80,align : 'left',formatter : function (cellValue,option,rowObject) {
										return "<input type='checkbox' name='searchFlag' class='field value' "+(cellValue==true?'checked':'')+" value='1'/>&nbsp;&nbsp;是";}
									},
									{name:"brandFlag", index:"brandFlag",width:80,align : 'left',formatter : function (cellValue,option,rowObject) {
										var brandFlag = rowObject.attrRef.brandFlag;
										return "<input type='checkbox' name='brandFlag' disabled='disabled' class='field value' "+(brandFlag==true?'checked':'')+" value='1'/>&nbsp;&nbsp;是";}
									},
									{name:"multiFlag", index:"multiFlag",width:80,align : 'left',formatter : function (cellValue,option,rowObject) {
										var multiFlag = rowObject.attrRef.multiFlag;
										return "<input type='checkbox' name='multiFlag' disabled='disabled' class='field value' "+(multiFlag==true?'checked':'')+" value='1'/>&nbsp;&nbsp;是";}
									},
									{name:'attrRef.type',index:'attrRef.type',width:150,align:"center", formatter : function (cellValue,option,rowObject) {
										if(cellValue=="Int" || cellValue == "Float"){
											return "单位名称<input name='unit' value='"+(rowObject.unit || "")+"' maxlength = 15/>";
										}else if(cellValue=="Enum"){
											var enumValues = rowObject.goodsCatAttrItems;
											if(enumValues){
												var map = new KeyMap();
												for(var i = 0;i<enumValues.length;i++){
													var enumValue = enumValues[i];
													var enumMap = new KeyMap();
													enumMap.add("id", enumValue.id);
						     						enumMap.add("attrId", rowObject.attrRef.id);
						     						enumMap.add("value", enumValue.value);
						     						enumMap.add("value2", enumValue.value2);
													map.add(enumValue.value,enumMap);
												}
												attrEnumMaps.add(rowObject.attrRef.id,map);
												cacheAttrEnumMaps.add(rowObject.attrRef.id,map);
											}
											var brandFlag = rowObject.attrRef.brandFlag;
											return "<a href='javascript:void(0)' onclick='openAttrEnumDialog("+rowObject.attrRef.id+","+brandFlag+",\""+rowObject.attrRef.name+"\")'>设定枚举值</a>";
										}else{
											return "";
										}
									}}
				                  ],  
					 multiselect : true,
					 multikey:'ctrlKey',
				});
			}else{
				goodsCatAttrUpdateGrid.jqGrid("setGridParam", {postData :{filterStr : JSON.encode({"catId":catId},true)}}).trigger("reloadGrid");
			}
		}
		
		function getGoodsCatAttr(){
			var gridCtrl = null;
			if(operate=='update'){
				gridCtrl = goodsCatAttrUpdateGrid;
			}else if(operate=='add'){
				gridCtrl = goodsCatAttrGrid;
			}
			var goodsCatAttrs = [];
			var rowIds = gridCtrl.getDataIDs();
			for(var i = 0;i<rowIds.length;i++){
				var goodsCatAttrMap = new KeyMap();
				var goodsCatAttr = gridCtrl.jqGrid("getRowData",rowIds[i]);
				goodsCatAttrMap.add("id",goodsCatAttr.id);
				goodsCatAttrMap.add("refId",goodsCatAttr["attrRef.id"]);
				if(operate=='update'){
					var unit = $("#goodsCatAttrUpdate #"+rowIds[i]+" input[name=unit]").val()||"";
					goodsCatAttrMap.add("unit",unit);
					var keyFlag = $("#goodsCatAttrUpdate #"+rowIds[i]+" input[name=keyFlag]").is(":checked");
					goodsCatAttrMap.add("keyFlag",keyFlag);
					var searchFlag = $("#goodsCatAttrUpdate #"+rowIds[i]+" input[name=searchFlag]").is(":checked");
					goodsCatAttrMap.add("searchFlag",searchFlag);
					var brandFlag = $("#goodsCatAttrUpdate #"+rowIds[i]+" input[name=brandFlag]").is(":checked");
					goodsCatAttrMap.add("brandFlag",brandFlag);
					var multiFlag = $("#goodsCatAttrUpdate #"+rowIds[i]+" input[name=multiFlag]").is(":checked");
					goodsCatAttrMap.add("multiFlag",multiFlag);
				}else if(operate=='add'){
					var unit = $("#goodsCatAttrGridAdd #"+rowIds[i]+" input[name=unit]").val()||"";
					goodsCatAttrMap.add("unit",unit);
					var keyFlag = $("#goodsCatAttrGridAdd #"+rowIds[i]+" input[name=keyFlag]").is(":checked");
					goodsCatAttrMap.add("keyFlag",keyFlag);
					var searchFlag = $("#goodsCatAttrGridAdd #"+rowIds[i]+" input[name=searchFlag]").is(":checked");
					goodsCatAttrMap.add("searchFlag",searchFlag);
					var brandFlag = $("#goodsCatAttrGridAdd #"+rowIds[i]+" input[name=brandFlag]").is(":checked");
					goodsCatAttrMap.add("brandFlag",brandFlag);
					var multiFlag = $("#goodsCatAttrGridAdd #"+rowIds[i]+" input[name=multiFlag]").is(":checked");
					goodsCatAttrMap.add("multiFlag",multiFlag);
				}
				var cellIndex = gridCtrl.jqGrid("getCell",rowIds[i],0);
				goodsCatAttrMap.add("seqNo",parseInt(cellIndex));
				goodsCatAttrs.add(goodsCatAttrMap);
			}
			return goodsCatAttrs;
		}
		function batchDelGoodsCatAttr(){
			var gridCtrl = null;
			if(operate=='update'){
				gridCtrl = goodsCatAttrUpdateGrid;
			}
			else if(operate=='add'){
				gridCtrl = goodsCatAttrGrid;
			}
			var ids = gridCtrl.jqGrid("getGridParam","selarrrow");
			if(ids==""){
				Layer.info("请选择要删除的数据！");
			}else{
				var postData = [];
				var tempData = [];
				for(var i = 0;i<ids.length;i++){
					if(ids[i].indexOf("new-")!=-1){
						tempData.add(ids[i]);
					}else{
						postData.add(ParseInt(ids[i]));
					}
				}
				Layer.confirm('确定要删除选择的数据吗？', function() {
					//
					var hintBox = Layer.progress("正在删除...");
					if(postData.length>0){
						var len = ids.length;  
						for(var i = 0;i < len ;i ++) {
							var attrRefId = gridCtrl.jqGrid("getRowData",ids[0])["attrRef.id"];
							if(operate=='update'){
								$("#tabs-goodsCat-group-show #attr-"+attrRefId).parent().remove();
							}else if(operate=='add'){
								$("#tabs-goodsCat-group #attr-"+attrRefId).parent().remove();
							}
							gridCtrl.jqGrid("delRowData", ids[0]);
						}  
						var ajax = Ajax.post("/categ/goodsCatAttr/delete/batch/do");
						ajax.data(postData);
						ajax.done(function(result, jqXhr) {
							if (result.type == "info") {
								Layer.msgSuccess(result.message);
							} else {
								Layer.msgWarning(result.message);
								//
								if(operate=='update'){
									updateGoodsCatAttr(selectedNode[0].id);
								}
								else if(operate=='add'){
									loadGoodsCatAttr();
								}
							}
						});
						ajax.always(function() {
							hintBox.hide();
						});
						ajax.go();
					}else{
						var len = ids.length;  
						for(var i = 0;i < len ;i ++) {
							var attrRefId = gridCtrl.jqGrid("getRowData",ids[0])["attrRef.id"];
							if(operate=='update'){
								$("#tabs-goodsCat-group-show #attr-"+attrRefId).parent().remove();
							}else if(operate=='add'){
								$("#tabs-goodsCat-group #attr-"+attrRefId).parent().remove();
							}
							gridCtrl.jqGrid("delRowData", ids[0]);
						} 
						Layer.msgWarning("删除成功");
					}
					hintBox.hide();
				});
			}
		}
		function openAttrEnumDialog(id,flag,title){
			brandFlag = flag;
			if(brandFlag){
				$id("btnAttrEnumAdd").hide();
				$id("btnAttrEnumBatchAdd").hide();
				$id("btnBrandDefBatchAdd").show();
			}else{
				$id("btnAttrEnumAdd").show();
				$id("btnAttrEnumBatchAdd").show();
				$id("btnBrandDefBatchAdd").hide();
			}
			$id("firstAttrEnumValue").nextAll().remove();
			emunAddId = id;
			var existMap = attrEnumMaps.get(emunAddId);
			if(existMap){
				addAttrEnumValue(existMap,brandFlag,false);
			}
			var titleName = "设定"+title+"枚举值";
			$("div[aria-describedby='attrEnumValuesDialog']").find("span[class='ui-dialog-title']").html(titleName);
			attrEnumValuesDialog.dialog("open");
		}
		
		//打开批量设定枚举值界面
		function openBatchAddAttrEnumDialog(){
			$("#attrEnumValuesBatchAddDialog textarea").val("");
			var title = $("div[aria-describedby='attrEnumValuesDialog']").find("span[class='ui-dialog-title']").html();;
			var titleName = "设定多个"+title.substring(2,title.length-3)+"枚举值";
			$("div[aria-describedby='attrEnumValuesBatchAddDialog']").find("span[class='ui-dialog-title']").html(titleName);
			attrEnumValuesBatchAddDialog.dialog("open");
		}
		//set页面枚举值到全局Map
		function setGoodsCatAttrEnum(){
			var oldMap = cacheAttrEnumMaps.get(emunAddId);
			var map = new KeyMap();
			$("#attrEnumValuesDialog input[type=text]").each(function(i){
				var value = $(this).val();
				var value2 = $(this).attr("data-value2") || "";
				if(null!=value && ""!=value){
					if(oldMap){
						if(!oldMap.contains(value)){
							var enumMap = new KeyMap();
							enumMap.add("value", value);
							enumMap.add("value2", value2);
							map.add(value,enumMap);
	 					}else{
	 						map.add(value,oldMap.get(value));
	 					}
					}else{
						var enumMap = new KeyMap();
						enumMap.add("value", value);
						enumMap.add("value2", value2);
						map.add(value,enumMap);
					}
				}
			 });
			attrEnumMaps.remove(emunAddId);
			attrEnumMaps.add(emunAddId,map);
			attrEnumValuesDialog.dialog("close");
		}
		//set批量添加页面枚举值到设定枚举值页面
		function batchSetGoodsCatAttrEnum(){
			var values = [];
			$("#attrEnumValuesBatchAddDialog textarea").each(function(i){
				var value = $(this).val();
				values = value.split(/\n/g);
			 });
			var map = new KeyMap();
			for(var i = 0;i<values.length;i++){
				var enumMap = new KeyMap();
				var value = values[i];
				enumMap.add("value",value);
				map.add(value,enumMap);
			}
			addAttrEnumValue(map);
		}
		//TODO
		//动态设定枚举值列表
		function addAttrEnumValue(valuesMap,brandFlag,addFlag){
			var disabled = "";
			if(brandFlag){
				disabled = "disabled";
			}
			if(valuesMap.size && valuesMap.size()>0){
				var html = "";
				var keys = valuesMap.keys()
				for(var i = 0;i<keys.length;i++){
					var map = valuesMap.get(keys[i]);
					var value = map.get('value');
					var value2 = map.get('value2') || "";
					html = "<div class='field row'>&nbsp;&nbsp;<input type='text' " +disabled+ " value='"+value+"' data-value2='"+value2+"' class='normal input' maxlength='30'/><button onclick='delAttrEnumValue(this)' class='normal button'>删除</button></div>";
					$id("firstAttrEnumValue").after(html);
				}
			}else{
				if($("#attrEnumValuesDialog div").length>20){
					Layer.msgWarning("添加数量太大");
				}else{
					if(brandFlag){
						return;
					}
					if(addFlag){
						return;
					}
					var html = "<div class='field row'>&nbsp;&nbsp;<input type='text' " +disabled+ " data-value2='' class='normal input' maxlength='30'/><button onclick='delAttrEnumValue(this)' class='normal button'>删除</button></div>";
					$id("firstAttrEnumValue").after(html);
				}
			}
		}
		//动态删除枚举值列表
		function delAttrEnumValue(obj){
			$(obj).parent().remove();
		}
		

		//加载数据列表
		function loadBrandDef() {
			var name = $id("brandName").val();
			var code = $id("brandCode").val();
			var existCodes = [];
			$("#attrEnumValuesDialog input[type=text]").each(function(i){
				var value2 = $(this).attr("data-value2");
				var value =  $(this).val();
				if(!isNoB(value)){
					existCodes.add(value2);
				}
			 });
			var postData = null;
			if(existCodes.length>0){
				postData = JSON.encode({name : name,
					code : code,"uncontainCodes":existCodes},true);
			}else{
				postData =JSON.encode( {
						name : name,
						code : code
					},true);
			}
			//加载jqGridCtrl
			if(brandDefGrid ==null){
				brandDefGrid = $id("brandDefList").jqGrid({
					height : "100%",
					url : getAppUrl("/categ/brandDef/list/get"),
					contentType : 'application/json',
					mtype : "post",
					datatype : 'json',
					colNames : [ "id","名称","code","Logo"],
					colModel : [ {name : "id",index:"id",hidden : true}, 
					             {name : "name",width : 150,align : "left"},
					             {name : "code",width : 100,align : "left"},
					             {name : "fileBrowseUrl",width : 200,align : "center",
					            	 formatter : function(cellValue, option, rowObject) {
					            		 if(!isNoB(cellValue)){
					            			 return "<img src='"+cellValue+"' width='100px' height='30px;'>"
					            		 }else{
					            			 return "无";
					            		 }
									}
								 }
					           ],
					multiselect : true, // 定义是否可以多选
					pager : "#brandDefPager",// 分页div
					});
			}else{
				brandDefGrid.jqGrid("setGridParam", {postData :{filterStr : postData}}).trigger("reloadGrid");
			}
		}
		
		function openBrandDefDialog(){
			brandDefDialog.dialog("open");
			loadBrandDef();
		}
	
		//set批量添加页面枚举值到设定枚举值页面
		function batchSetBrandDef(){
			var ids = brandDefGrid.jqGrid("getGridParam","selarrrow");
			var map = new KeyMap();
			for(var i = 0;i<ids.length;i++){
				var enumMap = new KeyMap();
				var rowData = brandDefGrid.jqGrid("getRowData",ids[i]);
				enumMap.add("value",rowData.name);
				enumMap.add("value2",rowData.code);
				map.add(rowData.name,enumMap);
			}
			addAttrEnumValue(map);
		}
		
		//------------------------商品分类规格关联-------------------------
		var goodsCatSpecGrid = null;
		var specEnumMaps = new KeyMap();//枚举值Map
		var specRowIdsMap = new KeyMap();//枚举值Map
		var specEnumValuesDialog;
		var specEnumValuesBatchAddDialog;
		var goodsCatSpecGridShow = null;
		var goodsCatSpecGridUpdate = null;
		var cacheSpecEnumMaps = new KeyMap();//枚举值Map
		var colorFlag = false;
		var colorDefDialog;
		var colorDefAddDialog;
		
		function showGoodsCatSpec(id){
			var catId = 0;
			if(null!=id && ""!=id){
				catId = parseInt(id);
			}
			if(goodsCatSpecGridShow == null){
				goodsCatSpecGridShow = $("#goodsCatSpecGridShow").jqGrid({
					height : "100%",
					width : "100%",
					url : getAppUrl("/categ/goodsCatSpec/list/get/by/catId"),
					contentType : 'application/json',
					postData :{filterStr : JSON.encode({"catId":catId},true)},
					mtype : "post",
					datatype : 'json',
					colNames : [ "id", "分组","规格名称","是否颜色","是否搜索", "规格值"],
					colModel : [{name : "id",index:"specRef.id",hidden : true,align : "center"}, 
					            {name : "groupId",index:"groupId",hidden : true,align : "center", formatter : function (cellValue,option,rowObject) {
						            	 var html ="<li><label>"+rowObject.specRef.name+"</label></li>";
						     			 $("#showTabs-"+cellValue).append(html);
						            	 return cellValue;
										}},
					            {name : "specRef.name",index:"specRef.name",width : 150,align : "left"},
					            {name : "colorFlag",index:"colorFlag",width : 80,align : "center", formatter : function (cellValue,option,rowObject) {
					            	 return cellValue==true?'是':'否';
								}}, 
								{name:"searchFlag", index:"searchFlag",width:80,align : 'center',formatter : function (cellValue,option,rowObject) {
									return cellValue==true?'是':'否';}
								},
					             {name : "refId",index:"refId",width : 350,align:"left", formatter : function (cellValue,option,rowObject) {
					            	var enumValues = rowObject.goodsCatSpecItems;
					            	var values = "";
					            	if(enumValues){
										for(var i = 0;i<enumValues.length;i++){
											var enumValue = enumValues[i];
											values+= enumValue.value+",";
										}
										if(values.length>0){
											values = values.substring(0,values.length-1);
										}
										if(values.length>40){
											values = values.substring(0,40)+"...";
										}
					            	 }
					            	 return "<div name='enumValues' class='auto height'>"+values+"</div>";
									}}
					            ]
				});
			}else{
				goodsCatSpecGridShow.jqGrid("setGridParam", {postData :{filterStr : JSON.encode({"catId":catId},true)}}).trigger("reloadGrid");
			}
		}
		
		// 初始化商品规格数据
		function loadGoodsCatSpec(id) {
			var catId = 0;
			if(null!=id && ""!=id){
				catId = parseInt(id);
			}
			if(goodsCatSpecGrid == null){
				goodsCatSpecGrid = $("#goodsCatSpecGrid").jqGrid({
					height : "100%",
					width : "100%",
					url : getAppUrl("/categ/goodsCatSpec/list/get/by/catId"),
					contentType : 'application/json',
					postData :{filterStr : JSON.encode({"catId":catId},true)},
					mtype : "post",
					datatype : 'json',
					colNames : [ "id","规格Id", "规格名称","是否颜色","是否搜索", "规格值", "操作"],
					colModel : [{name : "id",index:"specRef.id",hidden : true,align : "center"}, 
					            {name:"specRef.id", index:"specRef.id", hidden:true},
					            {name : "specRef.name",index:"specRef.name",width : 150,align : "left"},
					            {name : "colorFlag",index:"colorFlag",width : 80,align : "center", formatter : function (cellValue,option,rowObject) {
					            	 var colorFlag = rowObject.specRef.colorFlag;
									 return "<input type='checkbox' name='colorFlag' disabled='disabled' class='field value' "+(colorFlag==true?'checked':'')+" value='1'/>&nbsp;&nbsp;是";
								}},
								{name:"searchFlag", index:"searchFlag",width:80,align : 'left',formatter : function (cellValue,option,rowObject) {
										return "<input type='checkbox' name='searchFlag' class='field value' "+(cellValue==true?'checked':'')+" value='1'/>&nbsp;&nbsp;是";}
								},
					            {name : "refId",index:"refId",width : 250,align:"left", formatter : function (cellValue,option,rowObject) {
					            	var enumValues = rowObject.goodsCatSpecItems;
					            	var specRefId =  cellValue;
					            	var values = "";
					            	if(enumValues){
									var map = new KeyMap();
										for(var i = 0;i<enumValues.length;i++){
											var enumValue = enumValues[i];
						     				if(null!=value && ""!=value){
						     					if(!map.contains(value)){
						     						map.add(value,specRefId);
						     					}
						     				}
											values+= enumValue.value+",";
										}
										if(values.length>0){
											values = values.substring(0,values.length-1);
										}
										if(values.length>25){
											values = values.substring(0,25)+"...";
										}
										specEnumMaps.remove(specRefId);
						     			specEnumMaps.add(specRefId,map);
					            	 }
					            	 return "<div name='enumValues'>"+values+"</div>";
								}},  
								{name:'specRef.desc',index:'specRef.desc',width:100,align:"center", formatter : function (cellValue,option,rowObject) {
										var colorFlag = rowObject.specRef.colorFlag;
										return "<a href='javascript:void(0)' onclick='openSpecEnumDialog("+rowObject.refId+","+colorFlag+",\""+rowObject.specRef.name+"\")'>添加</a>";
								}}],
					multiselect : true, // 定义是否可以多选
				});
			}else{
				goodsCatSpecGrid.jqGrid("setGridParam", {postData :{filterStr : JSON.encode({"catId":catId},true)}}).trigger("reloadGrid");
			}
		}

		function updateGoodsCatSpec(id) {
			var catId = 0;
			if(null!=id && ""!=id){
				catId = parseInt(id);
			}
			if(goodsCatSpecGridUpdate == null){
				goodsCatSpecGridUpdate = $("#goodsCatSpecGridUpdate").jqGrid({
					height : "100%",
					width : "100%",
					url : getAppUrl("/categ/goodsCatSpec/list/get/by/catId"),
					contentType : 'application/json',
					postData :{filterStr : JSON.encode({"catId":catId},true)},
					mtype : "post",
					datatype : 'json',
					colNames : ["id","规格Id", "分组Id","规格名称","是否颜色","是否搜索", "规格值", "操作"],
					colModel : [{name : "id",index:"id",hidden : true,align : "center"}, 
					            {name:"specRef.id", index:"specRef.id", hidden:true},
					            {name:"groupId", index:"groupId", hidden:true, formatter : function (cellValue,option,rowObject) {
					            	var  dataId = "spec"+"-"+rowObject.specRef.id;
				                	var name = rowObject.specRef.name;
				                	var html = "<li><label>"+name+"</label><span onclick='deleteGroupData(this);' class='ui-icon ui-icon-close' style='cursor: pointer;float:left;' role='presentation' data-id='"+dataId+"'>"+name+"</span></li>";
					            	if(cellValue){
					            		 $("#group-"+cellValue).find("ul").append(html);
					            	 }
					            	 return cellValue;
									}},
					            {name : "specRef.name",index:"specRef.name",width : 150,align : "left"}, 
					            {name : "colorFlag",index:"colorFlag",width : 80,align : "center", formatter : function (cellValue,option,rowObject) {
					            	 var colorFlag = rowObject.specRef.colorFlag;
									return "<input type='checkbox' name='colorFlag' disabled='disabled' class='field value' "+(colorFlag==true?'checked':'')+" value='1'/>&nbsp;&nbsp;是";
								}},
								{name:"searchFlag", index:"searchFlag",width:80,align : 'left',formatter : function (cellValue,option,rowObject) {
									return "<input type='checkbox' name='searchFlag' class='field value' "+(cellValue==true?'checked':'')+" value='1'/>&nbsp;&nbsp;是";}
								},
					             {name : "refId",index:"refId",width : 250,align:"left", formatter : function (cellValue,option,rowObject) {
					            	var enumValues = rowObject.goodsCatSpecItems;
					            	var specRefId =  cellValue;
					            	var values = "";
					            	specRowIdsMap.add(specRefId,rowObject.id);
					            	if(enumValues){
									var map = new KeyMap();
										for(var i = 0;i<enumValues.length;i++){
											var enumValue = enumValues[i];
											var value = enumValue.value;
											var enumMap = new KeyMap();
											enumMap.add("id", enumValue.id);
				     						enumMap.add("value", value);
				     						enumMap.add("value2", enumValue.value2);
											map.add(value,enumMap);
											values+= value+",";
										}
										if(values.length>0){
											values = values.substring(0,values.length-1);
										}
										if(values.length>25){
											values = values.substring(0,25)+"...";
										}
										specEnumMaps.remove(specRefId);
						     			specEnumMaps.add(specRefId,map);
						     			cacheSpecEnumMaps.add(specRefId,map);
					            	 }
					            	 return "<div name='enumValues'>"+values+"</div>";
									}},  
									{name:'specRef.desc',index:'specRef.desc',width:100,align:"center", formatter : function (cellValue,option,rowObject) {
										var colorFlag = rowObject.specRef.colorFlag;
										return "<a href='javascript:void(0)' onclick='openSpecEnumDialog("+rowObject.refId+","+colorFlag+",\""+rowObject.specRef.name+"\")'>修改</a>";
									}}
					            ],
					multiselect : true, // 定义是否可以多选
					multikey:'ctrlKey'
				});
			}else{
				goodsCatSpecGridUpdate.jqGrid("setGridParam", {postData :{filterStr : JSON.encode({"catId":catId},true)}}).trigger("reloadGrid");
			}
		}
		
		function batchDelGoodsCatSpec(){
			var gridCtrl = null;
			if(operate=='update'){
				gridCtrl = goodsCatSpecGridUpdate;
			}
			else if(operate=='add'){
				gridCtrl = goodsCatSpecGrid;
			}
			var ids = gridCtrl.jqGrid("getGridParam","selarrrow");
			if(ids==""){
				Layer.info("请选择要删除的数据！");
			}else{
				var postData = [];
				var tempData = [];
				for(var i = 0;i<ids.length;i++){
					if(ids[i].indexOf("new-")!=-1){
						tempData.add(ids[i]);
					}else{
						postData.add(ParseInt(ids[i]));
					}
				}
				Layer.confirm('确定要删除选择的数据吗？', function() {
					//
					var hintBox = Layer.progress("正在删除...");
					if(postData.length>0){
						var len = ids.length;  
						for(var i = 0;i < len ;i ++) {
							var specRefId = gridCtrl.jqGrid("getRowData",ids[0])["specRef.id"];
							if(operate=='update'){
								$("#tabs-goodsCat-group-show #spec-"+specRefId).parent().remove();
							}else if(operate=='add'){
								$("#tabs-goodsCat-group #spec-"+specRefId).parent().remove();
							}
							gridCtrl.jqGrid("delRowData", ids[0]); 
						} 
						var ajax = Ajax.post("/categ/goodsCatSpec/delete/batch/do");
						ajax.data(postData);
						ajax.done(function(result, jqXhr) {
							if (result.type == "info") {
								Layer.msgSuccess(result.message);
								
							} else {
								Layer.msgWarning(result.message);
								//
								if(operate=='update'){
									updateGoodsCatSpec(selectedNode[0].id);
								}
								else if(operate=='add'){
									loadGoodsCatSpec();
								}
							}
						});
						ajax.always(function() {
							hintBox.hide();
						});
						ajax.go();
					}else{
						var len = ids.length;  
						for(var i = 0;i < len ;i ++) {
							var specRefId = gridCtrl.jqGrid("getRowData",ids[0])["specRef.id"];
							if(operate=='update'){
								$("#tabs-goodsCat-group-show #spec-"+specRefId).parent().remove();
							}else if(operate=='add'){
								$("#tabs-goodsCat-group #spec-"+specRefId).parent().remove();
							}
							gridCtrl.jqGrid("delRowData", ids[0]);  
						} 
						Layer.msgSuccess('删除成功');
					}
					hintBox.hide();
				});
			}
		}
		
		function getGoodsCatSpec(){
			var gridCtrl = null;
			if(operate=='update'){
				gridCtrl = goodsCatSpecGridUpdate;
			}else if(operate=='add'){
				gridCtrl = goodsCatSpecGrid;
			}
			var goodsCatSpecs = [];
			var rowIds = gridCtrl.getDataIDs();
			for(var i = 0;i<rowIds.length;i++){
				var goodsCatSpecMap = new KeyMap();
				var goodsCatSpec = gridCtrl.jqGrid("getRowData",rowIds[i]);
				goodsCatSpecMap.add("id",goodsCatSpec.id);
				goodsCatSpecMap.add("refId",parseInt(goodsCatSpec["specRef.id"]));
				var colorFlag = false;
				if(operate=='update'){
					colorFlag = $("#goodsCatSpecGridUpdate #"+rowIds[i]+" input[name=colorFlag]").is(":checked")
				}else if(operate=='add'){
					colorFlag = $("#goodsCatSpecGrid #"+rowIds[i]+" input[name=colorFlag]").is(":checked")
				}
				goodsCatSpecMap.add("colorFlag",colorFlag);
				var searchFlag = false;
				if(operate=='update'){
					searchFlag = $("#goodsCatSpecGridUpdate #"+rowIds[i]+" input[name=searchFlag]").is(":checked")
				}else if(operate=='add'){
					searchFlag = $("#goodsCatSpecGrid #"+rowIds[i]+" input[name=searchFlag]").is(":checked")
				}
				goodsCatSpecMap.add("searchFlag",searchFlag);
				var cellIndex = gridCtrl.jqGrid("getCell",rowIds[i],0);
				goodsCatSpecMap.add("seqNo",parseInt(cellIndex));
				goodsCatSpecs.add(goodsCatSpecMap);
			}
			return goodsCatSpecs;
		}
		function openSpecEnumDialog(id,flag,title){
			colorFlag = flag;
			if(colorFlag){
				$id("btnSpecEnumAdd").hide();
				$id("btnSpecEnumBatchAdd").hide();
				$id("btnColorBatchAdd").show();
				$id("btnColorAdd").show();
			}else{
				$id("btnSpecEnumAdd").show();
				$id("btnSpecEnumBatchAdd").show();
				$id("btnColorBatchAdd").hide();
				$id("btnColorAdd").hide();
			}
			$id("firstSpecEnumValue").nextAll().remove();
			emunAddId = id;
			var existMap = specEnumMaps.get(emunAddId);
			if(existMap){
				addSpecEnumValue(existMap,colorFlag,true);
			}
			var titleName = "设定"+title+"枚举值";
			$("div[aria-describedby='specEnumValuesDialog']").find("span[class='ui-dialog-title']").html(titleName);
			specEnumValuesDialog.dialog("open");
		}
		//打开批量设定枚举值界面
		function openBatchAddSpecEnumDialog(){
			$("#specEnumValuesBatchAddDialog textarea").val("");
			var title = $("div[aria-describedby='specEnumValuesDialog']").find("span[class='ui-dialog-title']").html();;
			var titleName = "设定多个"+title.substring(2,title.length-3)+"枚举值";
			$("div[aria-describedby='specEnumValuesBatchAddDialog']").find("span[class='ui-dialog-title']").html(titleName);
			specEnumValuesBatchAddDialog.dialog("open");
		}
		//TODO
		//set页面枚举值到全局Map
		function setGoodsCatSpecEnum(){
			var oldMap = cacheSpecEnumMaps.get(emunAddId);
			var map = new KeyMap();
			$("#specEnumValuesDialog input[type=text]").each(function(i){
				var value = $(this).val();
				var value2 = $(this).attr("data-value2") || "";
				if(null!=value && ""!=value){
					if(oldMap){
						if(!oldMap.contains(value)){
							var enumMap = new KeyMap();
							enumMap.add("value", value);
							enumMap.add("value2", value2);
							map.add(value,enumMap);
	 					}else{
	 						map.add(value,oldMap.get(value));
	 					}
					}else{
						var enumMap = new KeyMap();
						enumMap.add("value", value);
						enumMap.add("value2", value2);
						map.add(value,enumMap);
					}
				}
			 });
			
			specEnumMaps.remove(emunAddId);
			specEnumMaps.add(emunAddId,map);
			showEnumValues(emunAddId);
			specEnumValuesDialog.dialog("close");
		}
		function showEnumValues(emunAddId){
			var values = "";
			var data = [];
			var map = specEnumMaps.get(emunAddId);
			var rowId = specRowIdsMap.get(emunAddId);
			if(map){
				data = map.keys();
			}
			for(var i = 0;i<data.length;i++){
				var enumValue = data[i];
				values+= enumValue+",";
			}
			if(values.length>0){
				values = values.substring(0,values.length-1);
			}
			if(values.length>25){
				values = values.substring(0,25)+"...";
			}
			if(data.length>0){
				if(rowId!=null){
					obj = $("#goodsCatSpecGridUpdate  #"+rowId+" div[name=enumValues]");
				}else{
					obj = $("#goodsCatSpecGridUpdate  #new-"+emunAddId+" div[name=enumValues]");
				}
			}
			if(operate=='update'){
				obj.html(values);
			}
			else if(operate=='add'){
				$("#goodsCatSpecGrid  #new-"+emunAddId+" div[name=enumValues]").html(values);
			}
		}
		//set批量添加页面枚举值到设定枚举值页面
		function batchSetGoodsCatSpecEnum(){
			var values = [];
			$("#specEnumValuesBatchAddDialog textarea").each(function(i){
				var value = $(this).val();
				values = value.split(/\n/g);
			 });
			var map = new KeyMap();
			for(var i = 0;i<values.length;i++){
				var enumMap = new KeyMap();
				var value = values[i];
				enumMap.add("value",value);
				map.add(value,enumMap);
			}
			addSpecEnumValue(map);
		}
		//动态设定枚举值列表
		function addSpecEnumValue(valuesMap,colorFlag,addFlag){
			var disabled = "";
			if(colorFlag){
				disabled = "disabled";
			}
			if(valuesMap.size && valuesMap.size()>0){
				var html = "";
				var keys = valuesMap.keys()
				for(var i = 0;i<keys.length;i++){
					var map = valuesMap.get(keys[i]);
					var value = map.get('value');
					var value2 = map.get('value2') || "";
					html = "<div class='field row'>&nbsp;&nbsp;<input type='text' " +disabled+ " value='"+value+"' data-value2='"+value2+"' class='normal input' maxlength='30'/><button onclick='delSpecEnumValue(this)' class='normal button'>删除</button></div>";
					$id("firstSpecEnumValue").after(html);
				}
				;
			}else{
				if($("#specEnumValuesDialog div").length>20){
					Layer.msgWarning("添加数量太大");
				}else{
					if(colorFlag){
						return;
					}
					if(addFlag){
						return;
					}
					var html = "<div class='field row'>&nbsp;&nbsp;<input type='text' data-value2='' class='normal input' maxlength='30'/><button onclick='delSpecEnumValue(this)' class='normal button'>删除</button></div>";
					$id("firstSpecEnumValue").after(html);
				}
			}
		}
		//动态删除枚举值列表
		function delSpecEnumValue(obj){
			$(obj).parent().remove();
		}
		
		//set批量添加页面枚举值到设定枚举值页面
		function batchSetColor(){
			var map = new KeyMap();
			$("#colorDefDialog input:checked").each(function(i){
				var obj =  $(this);
				var colorLabel = obj.next();
				var colorHtml = $(colorLabel).html();
				var index = colorHtml.indexOf("</span>");
				var colorName = colorHtml.substring(index+7, colorHtml.length);
				var enumMap = new KeyMap();
				var name = obj.val().trim();
				enumMap.add("value",colorName);
				enumMap.add("value2",name);
				map.add(name,enumMap);
			});
			addSpecEnumValue(map);
		}
		
		//加载数据列表
		function loadColorDef() {
			var existNames = [];
			$("#specEnumValuesDialog input[type=text]").each(function(i){
				var value2 = $(this).attr("data-value2");
				var value =  $(this).val().trim();
				if(!isNoB(value)){
					existNames.add(value);
				}
			 });
			var name = $id("colorName").val() ||"";
			var postData = {};
			if(existNames.length>0){
				postData = {"uncontainNames":existNames,"name":name};
			}else{
				postData = {"name":name};
			}
			var ajax = Ajax.post("/categ/colorDef/list/get/all");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data;
					var values = "<div><ul style='float:left;'>"
					for(var i = 0;i<data.length;i++){
						var color = data[i];
						var html = "<li style='float:left;list-style:none;width: 160px;'><input type='checkbox' value='"+color.expr+"' id='color"+i+"'/><label for='color"+i+"'><span style='background-color: "+color.expr+";'>&nbsp;&nbsp;&nbsp;&nbsp;</span>"+color.name+"</label></li>";
						values+=html;
					}
					values+="</ul></div>"
					$id("colors").html(values);
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		function openColorDefDialog(){
			$("#colorDefAddDialog input[type=text]").val("");
			loadColorDef()
			colorDefDialog.dialog("open");
		}
		
		var formProxyColorDef = FormProxy.newOne();
		//注册表单控件
		formProxyColorDef.addField({
			id : "colorDefName",
			required : true,
			rules : ["maxLength[30]"]
		});
		formProxyColorDef.addField({
			id : "colorDefExpr",
			required : true,
			rules : [ "isHexColor"]
		});
		
		function openColorDefAddDialog(){
			$("#colorDefAddDialog input[type=text]").val("");
			colorDefAddDialog.dialog("open");
		}
		
		function addColorDef(){
			var vldResult = formProxyColorDef.validateAll();
			if (!vldResult) {
				return;
			}
			var map = new KeyMap();
			var enumMap = new KeyMap();
			var value = formProxyColorDef.getValue("colorDefName");
			enumMap.add("value",value);
			enumMap.add("value2",formProxyColorDef.getValue("colorDefExpr"));
			map.add(value,enumMap);
			addSpecEnumValue(map);
			colorDefAddDialog.dialog("close");
		}
		//------------------------属性分组-------------------------
		var addGroupDialog;
		var groupMaps = new KeyMap();
		var groupAddTabs;
		var groupUpdateTabs;
		var groupTempMap = null;
		var groupTempTabId = null;
		
		var groupPostDataMap = new KeyMap();
		//表单代理
		var groupFormProxy = FormProxy.newOne();
		//注册表单控件
		groupFormProxy.addField({
			id : "groupName",
			required : true,
			rules : ["maxLength[30]",
			         {
				rule : function(idOrName, type, rawValue, curData) {
					if(groupMaps.get(rawValue)){
						return false;
					}else{
						return true;
					}
				},
				message : "分组已存在！"
			}]
		});
		groupFormProxy.addField({
			id : "groupDesc",
			rules : ["maxLength[250]"]
		});
		//添加分组页面
		function openAddGroupDialog(){
			var keys = groupMaps.keys();
			if(keys.length>10){
				Layer.msgWarning("分组数量过大!");
				return;
			}
			$("#groupDialog input[type=text]").val("");
			$("#groupDialog textarea").val("");
			$("#groupDialog #groupAttrs").html("");
			var groupDataMap = getGroupAttrs();
			var html = "";
			var keys = groupDataMap.keys();
			
			var showDiv = "";
			if(operate=='update'){
				showDiv = "groupsUpdateShow";
			}else if(operate=='add'){
				showDiv = "groupsAddShow";
			}
			
			var existIds = []
			$("#"+showDiv+" span[role=presentation]").each(function(){
				existIds.add($(this).attr("data-id"));
			});
			
			for(var i = 0;i<keys.length;i++){
				var map = groupDataMap.get(keys[i]);
				var dataKeys = map.keys();
				var code = keys[i] == "goodsCatSpec" ? "spec" : "attr" ;
				for(var m = 0;m<dataKeys.length;m++){
					var  dataId = code+"-"+dataKeys[m];
					if(!existIds.contains(dataId)){
						html+="<li><input id='"+dataId+"' data-id='"+dataKeys[m]+"' type='checkbox' value='"+map.get(dataKeys[m])+"'/><label for='"+dataId+"'>"+map.get(dataKeys[m])+"</label></li>";
					}
				}
			}
			$id("groupAttrs").append(html);
			addGroupDialog.dialog("open");
		}
		//添加分组
		function addGroup(){
			var keys = groupMaps.keys();
			if(keys.length>10){
				Layer.msgWarning("分组数量过大!");
				return;
			}
			var vldResult = groupFormProxy.validateAll();
			if (!vldResult) {
				return;
			}
			var name = groupFormProxy.getValue("groupName");
			var desc = groupFormProxy.getValue("groupDesc");
			var newMap = new KeyMap();
			var id = $id("groupId").val();
			newMap.add("name",name);
			newMap.add("desc",desc);
			newMap.add("id",id);
			
			groupMaps.add(name,newMap);
			
			var attrMap = new KeyMap();
		    var specMap = new KeyMap();
		    var groupMap = groupPostDataMap.get(name);
		    if(groupMap){
		    	attrMap = groupMap.get("attr") || new KeyMap();
			    specMap = groupMap.get("spec") || new KeyMap();
		    }
		    var map = new KeyMap();
		    var attrs = [];
		    $('#groupAttrs input:checkbox:checked').each(function(i){
		    	var dataId = $(this).attr("id");
				var name = $(this).val();
				var type = dataId.substring(0,4);
				var id = dataId.substring(5,dataId.length);
				if(type=='attr'){
					attrMap.add(id,type);
				}else if(type=='spec'){
					specMap.add(id,type);
				}
				var name = {
						id : dataId,
						name : name
				}
				attrs.add(name);
			});
			map.add("attr",attrMap);
			map.add("spec",specMap);
			groupPostDataMap.add(name,map);
			
			var data = {
				id : id,
				name : name,
				desc : desc,
				attrs : attrs
			}
			var datas = []
			datas.add(data);
			
			var showDiv = "";
			if(operate=='update'){
				showDiv = "groupsUpdateShow";
			}else if(operate=='add'){
				showDiv = "groupsAddShow";
			}
			
			if(groupTempMap==null){
				appendRenderDetail("groupsAddHtml",showDiv,datas);
			}else{
				var oldName = groupTempMap.get("name");
				var oldObj = $id("group-"+oldName).parent().parent().parent();
				var tplHtml = $id("groupsAddHtml").html();
				var theTpl = laytpl(tplHtml);
				var htmlStr = theTpl.render(datas);
				oldObj.replaceWith($(htmlStr));
			}
			
			addGroupDialog.dialog("close");
		}
		
		function openUpdateGroupDialog(obj){
			$("#groupDialog input[type=text]").val("");
			$("#groupDialog textarea").val("");
			$("#groupDialog #groupAttrs").html("");
			
			var name = $(obj).attr("name");
			var map = groupMaps.get(name);
			groupMaps.remove(name);
			groupTempMap = map;
			groupFormProxy.setValue("groupName",name);
			groupFormProxy.setValue("groupDesc",map.get("desc"));
			$id("groupId").val(map.get("id"));
			
			var groupDataMap = getGroupAttrs();
			var html = "";
			var keys = groupDataMap.keys();
			
			var showDiv = "";
			if(operate=='update'){
				showDiv = "groupsUpdateShow";
			}else if(operate=='add'){
				showDiv = "groupsAddShow";
			}
			
			
			var existIds = []
			$("#"+showDiv+" span[role=presentation]").each(function(){
				existIds.add($(this).attr("data-id"));
			});
			
			var ownIds = [];
			$(obj).parent().parent().parent().parent().find("li").each(function(){
				var dataId = $(this).find("span").attr("data-id")
				ownIds.add(dataId);
				existIds.remove(dataId);
			});
			
			for(var i = 0;i<keys.length;i++){
				var map = groupDataMap.get(keys[i]);
				var dataKeys = map.keys();
				var code = keys[i] == "goodsCatSpec" ? "spec" : "attr" ;
				for(var m = 0;m<dataKeys.length;m++){
					var  dataId = code+"-"+dataKeys[m];
					if(!existIds.contains(dataId)){
						html+="<li><input id='"+dataId+"' data-id='"+dataKeys[m]+"' type='checkbox' value='"+map.get(dataKeys[m])+"'/><label for='"+dataId+"'>"+map.get(dataKeys[m])+"</label></li>";
					}
				}
			}
			$id("groupAttrs").append(html);
			for(var i = 0;i<ownIds.length;i++){
				$id(ownIds[i]).attr("checked",true);
			}
			addGroupDialog.dialog("open");
		}
		
		function deleteGroup(obj){
			var layer = null;
			layer =Layer.confirm('确定要删除该分组吗？', function(){
				var name = $(obj).attr("name");
				$id("group-"+name).parent().parent().parent().remove();
				groupMaps.remove(name);
				groupPostDataMap.remove(name);
				layer.hide();
			});
		}
		
		function addGroupData(){
			var tabId = "";
			var dataId = "";
			var tab = $id(tabId).find("li[aria-selected='true']");
		    var selectedId = tab.find("a").attr('href');
		    var groupName = tab.find("a").html();
		    var html = "";
		    var attrMap = new KeyMap();
		    var specMap = new KeyMap();
		    var groupMap = groupPostDataMap.get(groupName);
		    if(groupMap){
		    	attrMap = groupMap.get("attr") || new KeyMap();
			    specMap = groupMap.get("spec") || new KeyMap();
		    }
		    var map = new KeyMap();
			$('#'+dataId+' input:checkbox:checked').each(function(i){
				var dataId = $(this).attr("id");
				var name = $(this).val();
				var type = dataId.substring(0,4);
				var id = dataId.substring(5,dataId.length);
				if(type=='attr'){
					attrMap.add(id,type);
				}else if(type=='spec'){
					specMap.add(id,type);
				}
				$(this).parent().remove();
				html+="<li><input id='"+dataId+"' data-id='"+id+"' type='checkbox' value='"+name+"'/><label for='"+dataId+"'>"+name+"</label></li>";
			});
			map.add("attr",attrMap);
			map.add("spec",specMap);
			groupPostDataMap.add(groupName,map);
			$(selectedId).find("ul").append(html);
		}
		
		function deleteGroupData(obj){
			var groupName = $(obj).html();
			var dataId = $(obj).attr("data-id");
		    var attrMap = new KeyMap();
		    var specMap = new KeyMap();
		    var groupMap = groupPostDataMap.get(groupName);
		    if(groupMap){
		    	attrMap = groupMap.get("attr") || new KeyMap();
			    specMap = groupMap.get("spec") || new KeyMap();
		    }
		    var map = new KeyMap();
		    var type = dataId.substring(0,4);
			var id = dataId.substring(5,dataId.length);
			if(type=='attr'){
				attrMap.remove(id);
			}else if(type=='spec'){
				specMap.remove(id);
			}
			$(obj).parent().remove();
			map.add("attr",attrMap);
			map.add("spec",specMap);
			groupPostDataMap.add(groupName,map);
		}
		
		function getGroupAttrs(){
			var groupDataMap = new KeyMap();
			var attrGridCtrl = null;
			var goodsCatAttrMap = new KeyMap();
			if(operate=='update'){
				attrGridCtrl = goodsCatAttrUpdateGrid;
			}else if(operate=='add'){
				attrGridCtrl = goodsCatAttrGrid;
			}
			var datas = attrGridCtrl.getRowData();
			if(datas.length>0){
				for(var i = 0;i<datas.length;i++){
					var attrRefId = datas[i]["attrRef.id"];
					var attrRefName = datas[i]["attrRef.name"];
					goodsCatAttrMap.add(attrRefId,attrRefName);
				}
				groupDataMap.add("goodsCatAttr",goodsCatAttrMap);
			}
			var specGridCtrl = null;
			if(operate=='update'){
				specGridCtrl = goodsCatSpecGridUpdate;
			}else if(operate=='add'){
				specGridCtrl = goodsCatSpecGrid;
			}
			var goodsCatSpecMap = new KeyMap();
			datas = specGridCtrl.getRowData();
			if(datas.length>0){
				for(var i = 0;i<datas.length;i++){
					var specRefId = datas[i]["specRef.id"];
					var specRefName = datas[i]["specRef.name"];
					goodsCatSpecMap.add(specRefId,specRefName);
				}
				groupDataMap.add("goodsCatSpec",goodsCatSpecMap);
			}
			return groupDataMap;
		}
		
		function getGroupUpdateAttrs(){
			groupPostDataMap.clear();
			$("#groupsUpdateShow div[class=module]").each(function(){
				var groupName = $(this).find("a").html();
				var map = new KeyMap();
				var attrMap = new KeyMap();
				var specMap = new KeyMap();
				$(this).find("li").each(function(){
					var dataId = $(this).find("span").attr("data-id");
					var type = dataId.substring(0,4);
					var id = dataId.substring(5,dataId.length);
					if(type=='attr'){
						attrMap.add(id,type);
					}else if(type=='spec'){
						specMap.add(id,type);
					}
				});
				map.add("attr",attrMap);
				map.add("spec",specMap);
				groupPostDataMap.add(groupName,map);
			});
			return groupPostDataMap;
		}
		
		function showGoodsCatAttrGroup(catId){
			$id("groupAttrs").html("");
			$id("groupsAddShow").html("");
			if(null==catId || ""==catId){
				return;
			}
			var ajax = Ajax.post("/categ/goodsCatGroup/list/get/by/catId");
			ajax.data({
				catId : parseInt(selectedNode[0].id)
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data;
					if(operate=='show'){
						renderDetail("groupsHtml","groupShow",data);
						showGoodsCatAttr(catId);
						showGoodsCatSpec(catId);
					}else if(operate=='update'){
						for(var i = 0;i<data.length;i++){
							var map = new KeyMap();
							map.add("name",data[i].name);
							map.add("desc",data[i].desc);
							map.add("id",data[i].id);
							groupMaps.add(data[i].name,map);
						}
						appendRenderDetail("groupsAddHtml","groupsUpdateShow",data);
						updateGoodsCatAttr(catId);
						updateGoodsCatSpec(catId);
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		//------------------------商品分类价格区间-------------------------
		var priceRangeGrid = null;
		var priceRangeShowGrid = null;
		var priceRangeUpdateGrid = null;
		var priceRangeDialog;
		var priceRangeMap = new KeyMap();
		var size = 0;
		//表单代理
		var rangeFormProxy = FormProxy.newOne();
		
		function loadGoodsCatPriceRange(id){
			var catId = 0;
			if(null!=id && ""!=id){
				catId = parseInt(id);
			}
			if(priceRangeGrid == null){
				priceRangeGrid = $("#priceRangeList").jqGrid({
				    url : getAppUrl("/categ/goodsCatPriceRange/list/get/by/catId"),  
				    contentType : 'application/json', 
				    mtype : "post",  
				    postData :{filterStr : JSON.encode({"catId":catId},true)},
				    datatype : 'json',
				    height : "100%",
					width : "100%",
				    colNames : ["id", "分类Id","价格下限", "价格上限"],  
					colModel : [{name:"id", index:"id", hidden:true},
				                {name:"catId", index:"catId", hidden:true},
				                {name:"lowerPrice", index:"lowerPrice",width:200,align : 'right'}, 
				                {name:"upperPrice", index:"upperPrice",width:200,align : 'right', formatter : function (cellValue,option,rowObject) {
				                	if(cellValue==-1){
				                		return "———";
				                	}else{
				                		return cellValue;
				                	}
								}}]});
			}else{
				priceRangeGrid.jqGrid("setGridParam", {postData :{filterStr : JSON.encode({"catId":catId},true)}}).trigger("reloadGrid");
			}
		}
		function updateGoodsCatPriceRange(id){
			var catId = 0;
			if(null!=id && ""!=id){
				catId = parseInt(id);
			}
			if(priceRangeUpdateGrid == null){
				priceRangeUpdateGrid = $("#priceRangeUpdateList").jqGrid({
				    url : getAppUrl("/categ/goodsCatPriceRange/list/get/by/catId"),  
				    contentType : 'application/json', 
				    mtype : "post",  
				    postData :{filterStr : JSON.encode({"catId":catId},true)},
				    datatype : 'json',
				    height : "100%",
					width : "100%",
				    colNames : ["id", "分类Id","价格下限", "价格上限"],  
					colModel : [{name:"id", index:"id", hidden:true},
				                {name:"catId", index:"catId", hidden:true},
				                {name:"lowerPrice", index:"lowerPrice",width:200,align : 'right'}, 
				                {name:"upperPrice", index:"upperPrice",width:200,align : 'right', formatter : function (cellValue,option,rowObject) {
				                	if(cellValue==-1){
				                		return "———";
				                	}else{
				                		return cellValue;
				                	}
								}}]});
			}else{
				priceRangeUpdateGrid.jqGrid("setGridParam", {postData :{filterStr : JSON.encode({"catId":catId},true)}}).trigger("reloadGrid");
			}
		}
		function showGoodsCatPriceRange(id){
			var catId = 0;
			if(null!=id && ""!=id){
				catId = parseInt(id);
			}
			if(priceRangeShowGrid == null){
				priceRangeShowGrid = $("#priceRangeShowList").jqGrid({
				    url : getAppUrl("/categ/goodsCatPriceRange/list/get/by/catId"),  
				    contentType : 'application/json', 
				    mtype : "post",  
				    postData :{filterStr : JSON.encode({"catId":catId},true)},
				    datatype : 'json',
				    height : "100%",
					width : "100%",
				    colNames : ["id", "分类Id","价格下限", "价格上限"],  
					colModel : [{name:"id", index:"id", hidden:true},
				                {name:"catId", index:"catId", hidden:true},
				                {name:"lowerPrice", index:"lowerPrice",width:200,align : 'right'}, 
				                {name:"upperPrice", index:"upperPrice",width:200,align : 'right', formatter : function (cellValue,option,rowObject) {
				                	if(cellValue==-1){
				                		return "———";
				                	}else{
				                		return cellValue;
				                	}
								}}]});
			}else{
				priceRangeShowGrid.jqGrid("setGridParam", {postData :{filterStr : JSON.encode({"catId":catId},true)}}).trigger("reloadGrid");
			}
		}
		function openPriceRangeDialog(){
			$id("grade_tbody").html("");
			size = 0;
			var grid;
			if(operate=='update'){
				grid = priceRangeUpdateGrid;
			}else if(operate=='add'){
				grid = priceRangeGrid;
			}
			var ids = grid.getDataIDs();
			var len = ids.length;  
			for(var i = 0;i < len ;i ++) {
				var data = grid.getRowData(ids[i]);
				var rangeData = {
					id : data.id,
					lowerPrice : data.lowerPrice,
					upperPrice : data.upperPrice
				}
				addPriceRang(rangeData);
			}
			priceRangeDialog.dialog("open");
		}
		//修改最大价格后修改下一个的最小价格
		function changePrice(obj){
			var value = obj.value;
			if(!rangeFormProxy.validate(obj.id)){
				return;
			}
			var prev = $(obj).parent().parent().prev();
			var next = $(obj).parent().parent().next();
			var prevVal = $(prev.find("label")[0]).html();
			if(isNoB(prevVal)){
				prevVal = $(prev.find("input")).val();
			}
			if(parseInt(prevVal)>=parseInt(value)){
				showErrorTip(obj.id,"当前值不能小于上一个值")
				$($(obj).parent().parent().find("input")).val(parseInt(prevVal)+1);
				$(prev.find("label")[0]).html(parseInt(prevVal));
			}else{
				if(prev.length>0){
					$(prev.find("label")[0]).html(parseInt(value)-1);
					if(!isNoB($($(obj).parent().parent().find("label")[0]).html())){
						$($(obj).parent().parent().find("label")[0]).html(parseInt(value)+1);
					}
					$(obj).parent().parent().nextAll().each(function(i){
						var val = $(this).find("input").val();
						if(val){
							if(parseInt(val)<=parseInt(value)){
								var id = $(this).find("input").attr("id");
								deleteRangePrice(id.substring(0,id.length-6));
							}
						}else{
							var id = $(this).find("input").attr("id");
							deleteRangePrice(id.substring(0,id.length-6));
						}
					});
				}
			}
			
		}
		//
		function addPriceRang(data){
			var ranges = $id("grade_tbody").find("tr");
			var length = ranges.length;
			if(length > 10){
				Layer.warning("价格区间过多");
				return;
			}
			if(!(data && data.lowerPrice)){
				if(length == 0){
					data = {
							id : "new-"+size,
							lowerPrice : 0,
							upperPrice : ""
					};
				}else{
					var obj = $(ranges[length-1]).find("input");
					var val = obj.val();
					var result = rangeFormProxy.validate(obj.attr("id"));
					if(!result){
						return;
					}
					var obj = $(ranges[length-2]).find("label").html(parseInt(val)-1);
					data = {
						id : "new-"+size,
						lowerPrice : "",
						upperPrice : ""
					};
				}
			}
			if(data.upperPrice == "———"){
				data.upperPrice = "";
			}
			var theTpl = laytpl( $id("rangeTpl").html());
			var htmlStr = theTpl.render(data);
			$id("grade_tbody").append(htmlStr);
			//注册表单控件
			rangeFormProxy.addField({
				id : data.id+"-lower",
				type : "int",
				required : true,
				rules : ["maxLength[11]"]
			});
			size++;
		}
		//确定后刷新表格
		function addRangePrices(){
			var grid;
			if(operate=='update'){
				grid = priceRangeUpdateGrid;
			}else if(operate=='add'){
				grid = priceRangeGrid;
			}
			grid.clearGridData();
			$id("grade_tbody").find("tr").each(function(i){
				var map = new KeyMap();
				var obj = $(this).find("input");
				var id = obj.attr("name")+"";
				var lowerPrice = obj.val();
				if(lowerPrice && !isNoB(lowerPrice)){
					map.add("id",id);
					map.add("lowerPrice",obj.val());
					map.add("upperPrice",$(this).find("label").html());
					rangeFormProxy.removeField(id+"-lower");
					grid.addRowData("grid-"+id,objFromJsonStr(map.toJSON()));
				}
			 });
			priceRangeDialog.dialog("close");
		}
		function getPriceRanges(){
			var rangePrices = [];
			$id("grade_tbody").find("tr").each(function(i){
				var map = new KeyMap();
				var obj = $(this).find("input");
				var lowerPrice = obj.val();
				if(lowerPrice && !isNoB(lowerPrice)){
					var id = obj.attr("name");
					if(id.indexOf("new")==-1){
						map.add("id",parseInt(id));
					}else{
						map.add("id","");
					}
					map.add("lowerPrice",parseInt(lowerPrice));
					var upperPrice = $(this).find("label").html();
					if(isNoB(upperPrice)){
						upperPrice = -1;
					}else{
						upperPrice = parseInt(upperPrice);
					}
					map.add("upperPrice",upperPrice);
					rangePrices.add(map);
				}
			 });
			return rangePrices
		}
		function deleteRangePrice(id){
			var obj = $id(id+"-lower").parent().parent();
			var next = obj.next();
			if(next.length>0){
				var html = $(obj.find("label")[0]).html();
				$(next.find("label")[0]).html(html);
			}
			var next = obj.remove();
			rangeFormProxy.removeField(id+"-lower");
		}
		//------------------------初始化-------------------------
		//初始化dialog
		function initParentCatDialog(){
			parentDialog = $id("tabs-goodsCat").dialog({
				title : "添加商品分类",
				autoOpen : false,
				width : Math.min(500, $(window).width()),
				modal : true,
				buttons : {
					"保存" : function() {
						addParentGoodsCat();
					},
					"取消" : function() {
						parentDialog.dialog("close");
					}
				},
				close : function() {
					formProxy.hideMessages();
					parentDialog.dialog("destroy");
				}
			});
		}
		function initDialog() {
			addDialog = $id("addDialog").dialog({
				title : "添加商品分类",
				autoOpen : false,
				height : Math.min(500, $(window).height()),
				width : Math.min(850, $(window).width()),
				modal : true,
				buttons : {
					"保存" : function() {
						addGoodsCat();
					},
					"取消" : function() {
						addDialog.dialog("close");
					}
				},
				close : function() {
					formProxy.hideMessages();
					attrEnumMaps.clear();
					specEnumMaps.clear();
					addDialog.dialog("close");
				}
			});
			attrRefDialog = $id("attrRefDialog").dialog({
				title : "商品属性",
				autoOpen : false,
				height : Math.min(450, $(window).height()),
				width : Math.min(560, $(window).width()),
				modal : true,
				buttons : {
					"确定" : function() {
						fetchAttrRef();
					},
					"取消" : function() {
						attrRefDialog.dialog("close");
					}
				},
				close : function() {
					attrRefDialog.dialog("close");
				}
			});
			//添加商品属性枚举值
			attrEnumValuesDialog = $id("attrEnumValuesDialog").dialog({
				title : "设定枚举值",
				autoOpen : false,
				height : Math.min(400, $(window).height()),
				width : Math.min(400, $(window).width()),
				modal : true,
				buttons : {
					"确定" : function() {
						setGoodsCatAttrEnum();
						attrEnumValuesDialog.dialog("close");
					},
					"取消" : function() {
						attrEnumValuesDialog.dialog("close");
					}
				},
				close : function() {
					attrEnumValuesDialog.dialog("close");
				}
			});
			attrEnumValuesBatchAddDialog = $id("attrEnumValuesBatchAddDialog").dialog({
				title : "添加多个枚举值",
				autoOpen : false,
				height : Math.min(400, $(window).height()),
				width : Math.min(300, $(window).width()),
				modal : true,
				buttons : {
					"确定" : function() {
						batchSetGoodsCatAttrEnum();
						attrEnumValuesBatchAddDialog.dialog("close");
					},
					"取消" : function() {
						attrEnumValuesBatchAddDialog.dialog("close");
					}
				},
				close : function() {
					attrEnumValuesBatchAddDialog.dialog("close");
				}
			});
			brandDefDialog = $id("brandDefDialog").dialog({
				title : "关联品牌",
				autoOpen : false,
				height : Math.min(600, $(window).height()),
				width : Math.min(500, $(window).width()),
				modal : true,
				buttons : {
					"确定" : function() {
						batchSetBrandDef();
						brandDefDialog.dialog("close");
					},
					"取消" : function() {
						brandDefDialog.dialog("close");
					}
				},
				close : function() {
					brandDefDialog.dialog("close");
				}
			});
			specRefDialog = $id("specRefDialog").dialog({
				title : "商品规格",
				autoOpen : false,
				height : Math.min(550, $(window).height()),
				width : Math.min(460, $(window).width()),
				modal : true,
				buttons : {
					"确定" : function() {
						fetchSpecRef();
					},
					"取消" : function() {
						specRefDialog.dialog("close");
					}
				},
				close : function() {
					specRefDialog.dialog("close");
				}
			});
			//添加商品属性枚举值
			specEnumValuesDialog = $id("specEnumValuesDialog").dialog({
				title : "设定枚举值",
				autoOpen : false,
				height : Math.min(400, $(window).height()),
				width : Math.min(400, $(window).width()),
				modal : true,
				buttons : {
					"确定" : function() {
						setGoodsCatSpecEnum();
						specEnumValuesDialog.dialog("close");
					},
					"取消" : function() {
						specEnumValuesDialog.dialog("close");
					}
				},
				close : function() {
					specEnumValuesDialog.dialog("close");
				}
			});
			specEnumValuesBatchAddDialog = $id("specEnumValuesBatchAddDialog").dialog({
				title : "添加多个枚举值",
				autoOpen : false,
				height : Math.min(400, $(window).height()),
				width : Math.min(300, $(window).width()),
				modal : true,
				buttons : {
					"确定" : function() {
						batchSetGoodsCatSpecEnum();
						specEnumValuesBatchAddDialog.dialog("close");
					},
					"取消" : function() {
						specEnumValuesBatchAddDialog.dialog("close");
					}
				},
				close : function() {
					specEnumValuesBatchAddDialog.dialog("close");
				}
			});
			colorDefDialog = $id("colorDefDialog").dialog({
				title : "导入颜色",
				autoOpen : false,
				height : Math.min(600, $(window).height()),
				width : Math.min(800, $(window).width()),
				modal : true,
				buttons : {
					"确定" : function() {
						batchSetColor();
						colorDefDialog.dialog("close");
					},
					"取消" : function() {
						colorDefDialog.dialog("close");
					}
				},
				close : function() {
					colorDefDialog.dialog("close");
				}
			});
			colorDefAddDialog = $id("colorDefAddDialog").dialog({
				title : "添加颜色",
				autoOpen : false,
				height : Math.min(500, $(window).height()),
				width : Math.min(400, $(window).width()),
				modal : true,
				buttons : {
					"确定" : function() {
						addColorDef();
					},
					"取消" : function() {
						colorDefAddDialog.dialog("close");
					}
				},
				close : function() {
					formProxyColorDef.hideMessages();
					colorDefAddDialog.dialog("close");
				}
			});
			addGroupDialog = $id("groupDialog").dialog({
				title : "添加分组",
				autoOpen : false,
				height : Math.min(500, $(window).height()),
				width : Math.min(650, $(window).width()),
				modal : true,
				buttons : {
					"保存" : function() {
						addGroup();
					},
					"取消" : function() {
						if(groupTempMap!=null){
							groupMaps.add(groupTempMap.get("name"),groupTempMap);
						}
						addGroupDialog.dialog("close");
					}
				},
				close : function() {
					groupTempMap = null;
					groupTempTabId = null;
					groupFormProxy.hideMessages();
					addGroupDialog.dialog("close");
				}
			});
			priceRangeDialog = $id("priceRangeDialog").dialog({
				title : "修改价格区间",
				autoOpen : false,
				height : Math.min(500, $(window).height()),
				width : Math.min(400, $(window).width()),
				modal : true,
				buttons : {
					"确定" : function() {
						addRangePrices();
					},
					"取消" : function() {
						rangeFormProxy.hideMessages();
						priceRangeDialog.dialog("close");
					}
				},
				close : function() {
					rangeFormProxy.hideMessages();
					priceRangeDialog.dialog("close");
				}
			});
		}
		
		//--------------------------------------------------------------------------
		var goodsCategLevels = null;
		function loadGoodsCategLevels(){
			var ajax = Ajax.post("/categ/sysParam/categLevels/get");
			ajax.done(function(result, jqXhr) {
				goodsCategLevels = result.data;
				//alert("等级"+goodsCategLevels);
			});
			ajax.go();
		}
		//
		$(function() {
			//页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 68,
				allowTopResize : false,
				onresize : hideLayoutTogglers
			});
			//
			$id('mainPanel').layout({
				inset : 1,
				spacing_open : 1,
				spacing_closed : 1,
				west__size : 250,
				west__resizable : true,
				onresize : hideLayoutTogglers
			});
			//隐藏布局north分割线
			hideLayoutTogglers();
			$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "1");
			//加载系统参数-商品分类级数
			loadGoodsCategLevels();
			
			// 全部展开
			$id("btnExpandAll").click(function() {
				$.fn.zTree.getZTreeObj("goodsCatData").expandAll(true);
			});
			// 全部收起
			$id("btnCollapseAll").click(function() {
				$.fn.zTree.getZTreeObj("goodsCatData").expandAll(false);
			});
			//添加子节点
			$id("btnAddRoot").click(openAddRootDialog);
			$id("btnAddChild").click(openAddChildDialog);
			$id("btnAddBrother").click(openAddBrotherDialog);
			$id("btnUpdate").click(showUpdateDialog);
			$id("btnCancel").click(function() {
				$id("btnUpdate").show();
				$id("btnDelete").show();
				cancelTreeNode();
			});
			$id("btnSave").click(updateGoodsCat);
			$id("btnDelete").click(deleteGoodsCat);
			$("#tabs-goodsCat-attr #btnAttrAdd").click(openAttrRefDialog);
			$("#goodsCatAttrUpdateDialog #btnAttrAdd").click(openAttrRefDialog);
			$("#tabs-goodsCat-attr #btnAttrDel").click(batchDelGoodsCatAttr);
			$("#goodsCatAttrUpdateDialog #btnAttrDel").click(batchDelGoodsCatAttr);
			$id("btnAttrEnumAdd").click(addAttrEnumValue);
			$id("btnAttrEnumDel").click(delAttrEnumValue);
			$id("btnAttrEnumBatchAdd").click(openBatchAddAttrEnumDialog);
			$id("btnBrandDefBatchAdd").click(openBrandDefDialog);
			$("#tabs-goodsCat-spec #btnSpecAdd").click(openSpecRefDialog);
			$("#goodsCatSpecUpdateDialog #btnSpecAdd").click(openSpecRefDialog);
			$("#tabs-goodsCat-spec #btnSpecDel").click(batchDelGoodsCatSpec);
			$("#goodsCatSpecUpdateDialog #btnSpecDel").click(batchDelGoodsCatSpec);
			$id("btnSpecQuery").click(loadSpecRefGridData);
			$id("btnSpecEnumAdd").click(addSpecEnumValue);
			$id("btnSpecEnumDel").click(delSpecEnumValue);
			$id("btnSpecEnumBatchAdd").click(openBatchAddSpecEnumDialog);
			$id("btnColorBatchAdd").click(openColorDefDialog);
			$id("btnColorAdd").click(openColorDefAddDialog);
			$("#tabs-goodsCat-group #btnGroupAdd").click(openAddGroupDialog);
			$("#tabs-goodsCat-group-show #btnGroupAdd").click(openAddGroupDialog);
			$("#tabs-goodsCat-priceRange #btnRangeEdit").click(openPriceRangeDialog);
			$("#tabs-goodsCat-priceRange #btnRangeAdd").click(addPriceRang);
			$("#tabs-goodsCat-priceRange-show #btnRangeEdit").click(openPriceRangeDialog);
			$("#tabs-goodsCat-priceRange-show #btnRangeAdd").click(addPriceRang);
			$id("btnBrandDefQuery").click(loadBrandDef);
			// 条件查询
			$("#btnQuery").click(function() {
				var name = $id("queryName").val() ;
				loadGoodsCatTree(name);
			});
			
			$("#colorDefExpr").colorpicker();
			$id("btnColorQuery").click(loadColorDef);
			
			//初始化重量单位
			loadWeightUnit();
			//加载数据
			loadGoodsCatTree();
			//初始化各种界面
			initDialog();
			//初始化末级分类字段
			addTabs = $("#theTabsCtrl").tabs();
			addTabs.on("tabsactivate", function(event, ui) {
				formProxy.hideMessages();
				var vldResult = formProxy.validateAll();
				if (!vldResult) {
					addTabs.tabs({ active: 0 });
					return;
				}
				var currentPanl = ui.newPanel.selector;
			});
			updateTabs = $("#theShowTabsCtrl").tabs();
			updateTabs.on("tabsactivate", function(event, ui) {
				updateFormProxy.hideMessages();
				var currentPanl = ui.newPanel.selector;
				var vldResult = updateFormProxy.validateAll();
				if (!vldResult) {
					updateTabs.tabs({ active: 0 });
					return;
				}
				if(currentPanl=="#tabs-goodsCat-group-show"){
					if(operate=='update'){
						showGoodsCatAttrGroup();
					}
				}
			});
		});
	</script>
	
</body>
<script type="text/html" id="groupsHtml" title="分组信息">
	{{# for(var i = 0, len = d.length; i < len; i++){ }}
		<div class='module'>
			<div class='simple block'><div class='header'>{{ d[i].name }}</div></div>
			<ul class="list" id='showTabs-{{ d[i].id }}'>
			{{# if(d[i].attrs){ }}
				{{# var attrs =  d[i].attrs; for(var m = 0, mlen = attrs.length; m < mlen; m++){ }}
					<li>
						<label>{{ attrs[m].name }}</label>
					</li>
					{{# } }}
				{{# } }}
			</ul>
		</div>
	{{# } }}
</script>
<script type="text/html" id="groupsAddHtml" title="分组信息">
	{{# for(var i = 0, len = d.length; i < len; i++){ }}
		<div class='module' id="group-{{ d[i].id }}">
			<div class='simple block'><div class='header'><a id="group-{{ d[i].name }}">{{ d[i].name }}</a>
				<div class="normal group right aligned">
					<button onclick="openUpdateGroupDialog(this);" id="btnGroupEdit" name="{{ d[i].name }}" class="normal button">修改</button>
					<button onclick="deleteGroup(this);" role='presentation' name="{{ d[i].name }}" class="normal button">删除</button>
				</div>
				</div></div>
			<ul class="list">
			{{# if(d[i].attrs){ }}
				{{# var attrs =  d[i].attrs; for(var m = 0, mlen = attrs.length; m < mlen; m++){ }}
					<li>
						<label>{{ attrs[m].name }}</label>
						<span onclick="deleteGroupData(this);" 
				class='ui-icon ui-icon-close' style="cursor: pointer;float:left;" role='presentation' data-id ="{{ attrs[m].id }}">{{ d[i].name }}</span>
					</li>
					{{# } }}
				{{# } }}
			</ul>
		</div>
	{{# } }}
</script>
<script type="text/html" id="rangeTpl" title="价格区间模版">
	<tr>
		<td><input id="{{ d.id }}-lower" name="{{ d.id }}" value="{{ d.lowerPrice }}" style="width: 93%;" class="normal input" 
				onChange="changePrice(this)"/></td>
		{{# if(d.upperPrice == null) { }}
		<td><label style="width: 93%;" class="normal label" >———</label></td>
		{{# }else{ }}
		<td><label name="upperPrice" style="width: 93%;" class="normal label" >{{ d.upperPrice}}</label></td>
		{{# } }}
		<td><button class="normal button" onclick="deleteRangePrice('{{ d.id }}')">删除</button></td>
	</tr>
</script>
</html>