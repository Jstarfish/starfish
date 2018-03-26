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
</style>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 0px;">
		<div class="section" style="padding: 0px;">
	        <div class="section-left1">
	           <ul id="catTree" class="ztree"></ul>
	        </div>
	        <div class="section-right1">
		        <div style="float: left;">
		        	<div class="filter section">
						<div class="filter row">
							<div class="aligned group">
								<label class="label">商品名称</label>
								<input id="q_goodsName" class="input one half wide" />
								<button id="queryBtn" class="button">查询</button>
								<span class="vt divider"></span>
							</div>
						</div>
					</div>
		           <table id="goodsGridCtrl"></table>
					<div id="goodsGridPager"></div>
		        </div>
		        <div style="float: right;margin-right: 50px;">
		        <div class="filter section">
						<div class="filter row">
							<div class="right aligned group">
							</div>
						</div>
					</div>
		           <table id="productGridCtrl"></table>
					<div id="productGridPager"></div>
		        </div>
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
		loadGoodsData();
		//
		loadProductData();
		
		
		//
		$id("queryBtn").click(queryShopProduct);
		//
		
		//调整控件大小
		winSizeMonitor.start(adjustCtrlsSize);
		var winSizeProduct = new __WinSizeMonitor();
		winSizeProduct.start(productCtrlsSize);
		
	});
	
	
	//-------------------------------------------------全局变量---------------------------------------------------------------
	// 缓存当前jqGrid数据行数组
	var productGridHelper = JqGridHelper.newOne("");
	//
	var goodsGridCtrl = null;
	
	//
	var productGridCtrl = null;
	
	//-------------------------------------------------业务处理------------------------------------------------------------
	
	function queryShopProduct(){
		var goodsName = textGet("q_goodsName");
		var filter = {};
		if(goodsName){
			filter.goodsName = goodsName;
		}
		var zTree = $.fn.zTree.getZTreeObj("catTree");
		var nodes = zTree.getSelectedNodes();
		if(!isNoB(nodes)){
			filter.catId = nodes[0].id;
		}
		//
		refreshGoodsData(filter);
	}
	
	function refreshGoodsData(filterMap){
		goodsGridCtrl.jqGrid("setGridParam", {url : getAppUrl("/goods/goods/list/get"),postData :{filterStr : JSON.encode(filterMap,true)},page:1}).trigger("reloadGrid");
		productGridCtrl.jqGrid("clearGridData");
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
			if(catId) refreshGoodsData({catId:catId});
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
		//
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
	
	function loadGoodsData(catId){
		var filter = {};
		if(catId){
			filter.catId = catId;
		}
		//
		goodsGridCtrl= $("#goodsGridCtrl").jqGrid({
		      //url : getAppUrl("/goods/goods/list/get"),  
		      contentType : 'application/json',  
		      mtype : "post",  
		      datatype : 'json',
		      postData:{filterStr : JSON.encode(filter,true)},
		      height : "100%",
		      width:"100%",
			  colNames : ["商品名称","分类"],  
		      colModel : [
		                  {name:"name",width:100,align : 'left',formatter : function (cellValue) {
		                	 return cellValue;
							}},
						  {name:"catId",width:50,align : 'left',formatter : function (cellValue) {
		                	  var zTree = $.fn.zTree.getZTreeObj("catTree");
		                	  var node = zTree.getNodeByParam("id", cellValue, null);
							  return node.name;
						  }}],
			loadtext: "正在加载....",
			//multiselect:true,
			//multikey:'ctrlKey',
			pager : "#goodsGridPager",
			onCellSelect: function(rowId) {
				var filter = {};
				if(rowId){
					filter.goodsId = rowId;
				}
				refreshProductData(filter);
			}
		});
	}
	function refreshProductData(filterMap){
		productGridCtrl.jqGrid("setGridParam", {url:getAppUrl("/goods/product/list/by/catId"),postData :{filterStr : JSON.encode(filterMap,true)},page:1}).trigger("reloadGrid");

	}
	//初始货品列表
	function loadProductData(goodsId){
		var filter = {};
		if(goodsId){
			filter.goodsId = goodsId;
		}
		//
		productGridCtrl=$("#productGridCtrl").jqGrid({
		      //url : getAppUrl("/goods/product/list/by/catId"),  
		      contentType : 'application/json',  
		      mtype : "post",
		      datatype : 'json',
		      postData:{filterStr : JSON.encode(filter,true)},
		      height : "100%",
		      width:"100%",
			  colNames : ["选择商品","货品名称","价格(元)"],  
		      colModel : [
						 {name:"id",index:'id',width:50,align : 'center',formatter : function (cellValue) {
							 return "<input type='radio' id='validType"+cellValue+"' name='validProductId' value='"+cellValue+"'>";
						 }},
		                  {name:"title",width:300,align : 'left',formatter : function (cellValue) {
		                	 return "<div class='auto height'>"+cellValue+"</div>";
							}},
						  {name:"salePrice",width:50,align : 'right'}
						  ],
			loadtext: "正在加载....",
			//multiselect:true,
			multikey:'ctrlKey',
			pager : "#productGridPager",
			loadComplete : function(gridData){
				productGridHelper.cacheData(gridData);
				var callback = getCallbackAfterGridLoaded();
				if(isFunction(callback)){
					callback();
				}
			},
			onCellSelect: function(rowId) {
				$id("validType"+rowId).attr("checked",'checked');
			}
		});
	}
	
	function getCallbackAfterGridLoaded(){}
	//-------------------------------------------------调整控件大小------------------------------------------------------------
	//调整控件大小
	function adjustCtrlsSize() {
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		var gridCtrlId = "goodsGridCtrl";
		var jqGridBox = $("#gbox_" + gridCtrlId);
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
		var pagerHeight = $id("goodsGridPager").height();
		goodsGridCtrl.setGridWidth((mainWidth - 1 - 230)*0.4);
		goodsGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 70);
	}
	//调整控件大小
	function productCtrlsSize() {
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		var gridCtrlId = "productGridCtrl";
		var jqGridBox = $("#gbox_" + gridCtrlId);
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
		var pagerHeight = $id("productGridPager").height();
		productGridCtrl.setGridWidth((mainWidth - 1 - 230)*0.5);
		productGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 70);
	}
	//回调
	function getSelectedTargetInfo(){
		var productId = radioGet("validProductId");
		var json =  productGridHelper.getRowData(productId);
		json["targetName"]="商品";
		return json;
	}
	
	</script>
</html>
