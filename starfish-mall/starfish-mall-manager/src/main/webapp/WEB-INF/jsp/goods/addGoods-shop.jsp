<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>店铺添加商品</title>
<link rel="stylesheet" href="<%=resBaseUrl%>/css-app/wtree.css" />
<link rel="stylesheet" href="<%=resBaseUrl%>/lib/ztree/css/zTreeStyle.css" />
<style type="text/css">
#catTree li.level2>a>span.button~span {
	display: inline-block;
	width: 160px;
	overflow: hidden;
	white-space: nowrap;
	text-overflow: ellipsis;
}
</style>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 0px;">
		<div class="filter section">
			<div class="filter row">
				<div class="left group aligned" style="margin-left: 222px;">
					<button id="batchAddBtn" class="button wide" style="display: none;">批量添加</button>
				</div>
				<div class="right middle aligned group">
					<label class="label">名称</label>
					<input id="q_title" class="input one half wide" />
					<span class="spacer"></span>
					<button id="queryBtn" class="button">查询</button>
					<span class="vt divider"></span>
					<button id="retBtn" class="button one half wide">返回产品列表</button>
				</div>
				
			</div>
		</div>
		
		<div class="section" style="padding: 0px;">
	        <div class="section-left1">
	           <ul id="catTree" class="ztree"></ul>
	        </div>
	        <div class="section-right1">
	           <table id="productGridCtrl"></table>
				<div id="productGridPager"></div>
	        </div>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
	$(function() {
		//页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			allowTopResize : false
		});
		//
		initCatData();
		//
		loadProductData();
		//
		$id("batchAddBtn").click(goBatchAddProductToShop);
		//
		$id("queryBtn").click(queryShopProduct);
		//
		$id("retBtn").click(function(){
			var url = getAppUrl("/goods/list/jsp/-shop");
			setPageUrl(url);
		});
		
		//调整控件大小
		winSizeMonitor.start(adjustCtrlsSize);
	});
	
	
	//-------------------------------------------------全局变量---------------------------------------------------------------
	// 缓存当前jqGrid数据行数组
	var productGridHelper = JqGridHelper.newOne("");
	//
	var productGridCtrl = null;
	
	//-------------------------------------------------业务处理------------------------------------------------------------
	
	//
	function goBatchAddProductToShop(){
		var ids=productGridCtrl.jqGrid("getGridParam","selarrrow");
		if(!ids || ids.length == 0){
			Toast.show("请先选择要添加的产品~");
			return;
		}
		//TODO
		
	}
	//
	function queryShopProduct(){
		var title = textGet("q_title");
		var filter = {};
		if(title){
			filter.title = title;
		}
		var zTree = $.fn.zTree.getZTreeObj("catTree");
		var nodes = zTree.getSelectedNodes();
		if(!isNoB(nodes)){
			filter.catId = nodes[0].id;
		}
		//
		refreshProductData(filter);
	}
	
	function addProductToShop(pId){
		var hintBox = Layer.progress("正在保存数据...");

		var ajax = Ajax.post("/goods/product/add/do/-shop");
		//
		ajax.data({productId : pId});

		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				Toast.show(result.message);
				queryShopProduct();
			} else {
				Layer.msgWarning(result.message);
			}

		});
		ajax.always(function() {
			hintBox.hide();
		});
		
		ajax.go();
	}
	
	function goAddProduct(pId){
		if(pId) addProductToShop(ParseInt(pId));
	}
	
	function refreshProductData(filterMap){
		var page = productGridCtrl.getGridParam("page");
		//
		productGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filterMap,true)},page:page}).trigger("reloadGrid");
	}
	
	//------------------------------------------------初始化--------------------------------------------------------------------
	
	function isSelected(treeId, treeNode){
		var zTree = $.fn.zTree.getZTreeObj(treeId);
		var nodes = zTree.getSelectedNodes();
		for(var i=0; i<nodes.length; i++){
			var node = nodes[i];
			if(node.id == treeNode.id){
				return true;
			}
		}
		return false;
	}
	
	//
	function clickNode(e, treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj(treeId);
		var selected = isSelected(treeId, treeNode);
		//
		if(selected){
			var catId = treeNode.id;
			if(catId) refreshProductData({catId:catId});
		}
	}
	
	//
	function createCatTree(data) {
		var catNodes = [];
		$.each(data, function() {
			var nodeMap = {};
			//{id:4, pId:0, name:"XXX", open:true, nocheck:true},
			var data = $(this)[0];
			nodeMap["id"] = data.id;
			nodeMap["pId"] = data.parentId;
			nodeMap["name"] = data.name;
			nodeMap["idPath"] = data.idPath;
			nodeMap["hasSpec"] = data.hasSpec;
			if (data.level < 3) {
				nodeMap["open"] = true;
			}
			catNodes.add(nodeMap);
		});
		
		$.fn.zTree.init($("#catTree"), setting, catNodes);
	}
	
	var setting = {
		view : {
			dblClickExpand : true,
			showLine : false,
			showIcon : false
		},
		data : {
			simpleData : {
				enable : true
			}
		},
		callback : {
			beforeClick : function(treeId, treeNode) {
				var zTree = $.fn.zTree.getZTreeObj("catTree");
				if (treeNode.isParent) {
					zTree.expandNode(treeNode);
					return false;
				} else {
					return true;
				}
			},
			onClick : clickNode
		}
	};
	
	//初始化商品分类
	function initCatData() {
		var ajax = Ajax.post("/goods/categ/list/get");
		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				var cats = result.data;
				createCatTree(cats);
			} else {
				Layer.msgWarning(result.message);
			}
	
		});
		ajax.go();
	}
	
	function loadProductData(catId){
		var filter = {};
		if(catId){
			filter.catId = catId;
		}
		//
		var gridConfig = {
			      url : getAppUrl("/goods/product/list/get/-mall"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(filter,true)},
			      height : "100%",
				  width : "100%",
				  colNames : ["id","名称","分类","售价","上架状态","数量","操作"],  
			      colModel : [{name:"id",hidden:true},
			                  {name:"title",width:200,align : 'left',formatter : function (cellValue) {
			                	 return "<div class='auto height'>"+cellValue+"</div>";
								}},
							  {name:"catId",width:100,align : 'left',formatter : function (cellValue) {
			                	  var zTree = $.fn.zTree.getZTreeObj("catTree");
			                	  var node = zTree.getNodeByParam("id", cellValue, null);
								  return node.name;
							  }}, 
			                  {name:"salePrice",width:60,align : 'right',formatter : function (cellValue) {
				                	 return cellValue+"元";
								}}, 
			                  {name:"shelfStatus", index:"shelfStatus",width:80,align : 'center',formatter : function (cellValue) {
			                	  return cellValue == 1 ? '已上架' : cellValue == 2 ? '已下架' : '未上架';
								}},
							  {name:"quantity",width:60,align : 'right',formatter : function (cellValue,option,rowObject) {
				                	 var unit = rowObject.unit || "个";
				                	 return cellValue+unit;
								}},
			                  {name:'id',index:'id', formatter : function (cellValue,option,rowObject) {
			                	 var existFlag = rowObject.existFlag;
			                	 var status = rowObject.shelfStatus;
			                	 if(status != 1){
			                		 return "";
			                	 }
			                	 //
			                	 if(existFlag){
			                		 return "<span>√</span>";
			                	 }else{
			                		 return "<span> [<a class='item' href='javascript:void(0);' onclick='goAddProduct("
										+ cellValue
										+ ")' >添加到店铺</a>]";
										+ "</span>";
			                	 }
								
							},
						width:100,align:"center"}],
				loadtext: "正在加载....",
				//multiselect:true,
				multikey:'ctrlKey',
				pager : "#productGridPager",
				ondblClickRow: function(rowId) {
					goView(rowId);
				}
			};
		//注册jqgrid
		//productGridCtrl = initMyJqGrid("productGridCtrl", gridConfig, productGridHelper, getCallbackAfterGridLoaded);
		productGridCtrl = $id("productGridCtrl").jqGrid(gridConfig);
	}
	
	function getCallbackAfterGridLoaded(){}
	//-------------------------------------------------调整控件大小------------------------------------------------------------
	//调整控件大小
	function adjustCtrlsSize() {
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		var gridCtrlId = "productGridCtrl";
		var jqGridBox = $("#gbox_" + gridCtrlId);
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
		var pagerHeight = $id("productGridPager").height();
		productGridCtrl.setGridWidth(mainWidth - 1 - 230);
		productGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 70);
	}
	
	</script>
</html>
