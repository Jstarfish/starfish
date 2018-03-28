<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>店铺产品列表</title>
<link rel="stylesheet" href="<%=resBaseUrl%>/lib/ztree/css/zTreeStyle.css" />
<style type="text/css">
</style>
</head>
<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<button id="addBtn" class="button">添加产品</button>
				<span class="vt divider"></span>
				<button id="batchDeleteBtn" class="button">批量删除</button>
				<div class="right aligned group">
					<label class="label">名称</label> 
					<input id="q_title" class="input one half wide"/> 
					<span class="spacer"></span> 
					<label class="label">商品分类</label>
					<input id="q_goodsCat" data-id="" data-unit="" data-weightUnit="" readonly="readonly" placeholder="请选择..." class="input one wide" />
					<span class="spacer"></span> 
					<label class="label">供货状态</label>
					<select id=q_lackFlag class="input">
						<option>-请选择-</option>
						<option value="0">有货</option>
						<option value="1">无货</option>
					</select> 
					<span class="vt divider"></span>
					<button id="queryBtn" class="button">查询</button>
				</div>
				
			</div>
		</div>
		<table id="productGridCtrl"></table>
		<div id="productGridPager"></div>
	</div>
	
	<div id="catDialog" style="display: none; text-align: center;">
		<div id="menuContent" class="menuContent" style="display:none; position: absolute;">
			<ul id="catUl" class="ztree"></ul>
		</div>
	</div>
	
	<div id="viewProductDialog" style="display: none;">
		<div class="form">
			<div class="field row" style="height:auto">
				<label class="field label">产品名称</label>
				<textarea id="title" disabled rows="3" style="border-radius:4px; color:navy;"></textarea>
			</div>
			<div class="field row" style="height:auto">
				<label class="field label">所属商品</label>
				<textarea id="goodsName" disabled rows="3" style="border-radius:4px; color:navy;"></textarea>
			</div>
			<div class="field row">
				<label class="field label">商品分类</label>
				<input id="catName" type="text" disabled class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">库存量</label>
				<input id="quantity" type="text" disabled class="field value one half wide" />
				<label id="unit" class="normal label"></label>
			</div>
			<div class="field row">
				<label class="field label">重量</label>
				<input id="weight" type="text" disabled class="field value one half wide" />
				<label id="weightUnit" class="normal label"></label>
			</div>
			<div class="field row">
				<label class="field label">包装列表</label>
				<input id="packList" type="text" disabled class="field value one half wide" />
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
			north__size : 160,
			allowTopResize : false
		});
		//
		initCategDialog();
		//初始化商品分类
		initGoodsCat();
		//
		initViewDialog();
		//
		$id("queryBtn").click(refreshProductData);
		//
		$id("batchDeleteBtn").click(goDeleteBatchShopProduct);
		//
		$id("addBtn").click(function(){
			var url = getAppUrl("/goods/product/add/jsp/-shop");
			setPageUrl(url);
		});
		
		//调整控件大小
		winSizeMonitor.start(adjustCtrlsSize);
	});
	
	//-------------------------------------------------全局变量---------------------------------------------------------------
	var categDialog = null;
	//
	var productGridCtrl = null;
	//
	var productGridHelper = JqGridHelper.newOne("");
	//
	var viewProductDialog = null;
	
	
	//-------------------------------------------------业务处理------------------------------------------------------------
	
	//
	function goDeleteBatchShopProduct(){
		var ids=productGridCtrl.jqGrid("getGridParam","selarrrow");
		if(!ids || ids.length == 0){
			Toast.show("请先选择要删除的产品~");
			return;
		}
		//
		var theLayer = Layer.confirm('确定要删除该项么？', function() {
			theLayer.hide();
			var int_ids = [];
			for(var i=0; i<ids.length; i++){
				var id = ids[i];
				int_ids.add(ParseInt(id));
			}
			deleteBatchShopProduct(int_ids);
		});
	}
	//
	function deleteBatchShopProduct(ids){
		var hintBox = Layer.progress("正在删除...");
		//
		var ajax = Ajax.post("/goods/shopProduct/delete/by/Ids/-shop");
		data = {ids:ids};
		ajax.data(data);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				refreshProductData();
				Layer.msgSuccess(result.message);			
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.always(function() {
			hintBox.hide();
		});
		ajax.go();
	}
	
	function deleteShopProduct(shopProductId){
		var hintBox = Layer.progress("正在删除数据...");

		var ajax = Ajax.post("/goods/shopProduct/delete/do/-shop");
		
		ajax.data({id : shopProductId});

		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
				refreshProductData();
			} else {
				Layer.msgWarning(result.message);
			}

		});
		ajax.always(function() {
			hintBox.hide();
		});
		ajax.go();
	}
	
	function goDeleteShopProduct(shopProductId){
		var theLayer = Layer.confirm('确定要删除吗？', function() {
			theLayer.hide();
			deleteShopProduct(shopProductId);
		});
	}
	
	function showProduct(product){
		var zTree = $.fn.zTree.getZTreeObj("catUl");
  	  	//
		var productNo = product.no;
		var goodsName = product.goodsName;
		var catId = product.catId;
		var node = zTree.getNodeByParam("id", catId, null);
		var catName = node.name;
		var quantity = product.quantity;
		var weight = product.weight;
		var packList = product.packList;
		var unit = node.unit;
		var weightUnit =node.weightUnit;
		var title = product.title;
		
		//
		$id("productNo").val(productNo);
		$id("title").html(title);
		$id("goodsName").html(goodsName);
		$id("catName").val(catName);
		$id("quantity").val(quantity);
		$id("weight").val(weight);
		$id("packList").val(packList);
		$id("unit").html(unit);
		$id("weightUnit").html(weightUnit);
	}
	
	function getProductById(productId){
		var hintBox = Layer.progress("正在加载数据...");

		var ajax = Ajax.post("/goods/product/get/by/id/-shop");
		
		ajax.data({productId : productId});

		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				var product = result.data;
				if(product) showProduct(product);
			} else {
				Layer.msgWarning(result.message);
			}

		});
		ajax.always(function() {
			hintBox.hide();
		});
		ajax.go();
	}
	
	function goView(rowId){
		var shopProduct = productGridHelper.getRowData(rowId);
		var productId = shopProduct.productId;
		getProductById(productId);
		viewProductDialog.dialog("open");
	}
	
	function goUpdateLackFlag(shopProdouctId, lackFlag){
		if(lackFlag){
			var theLayer = Layer.confirm('无货状态会影响亲店铺的被推荐率,确定要继续吗？', function() {
				updateLackFlag(shopProdouctId, lackFlag, theLayer);
			});
		}else{
			updateLackFlag(shopProdouctId, lackFlag);
		}
		
	}
	
	function updateLackFlag(shopProdouctId, lackFlag, layerDlg){
		var ajax = Ajax.post("/goods/product/update/lackFlag/do/-shop");
		ajax.data({id:shopProdouctId, lackFlag:lackFlag});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
				refreshProductData();
			} else {
				Layer.msgWarning(result.message);
			}
		});
		//
		ajax.always(function(){
			if(layerDlg) layerDlg.hide();
		});
		//
		ajax.go();
	}
	
	function refreshProductData(){
		var filter = {};
		var title = textGet("q_title");
		if(title){
			filter.title = title.trim();
		}
		var catId = $id("q_goodsCat").attr("data-id");
		if(catId){
			filter.catId = ParseInt(catId);
		}
		var lackFlag = intGet("q_lackFlag");
		if(lackFlag){
			filter.lackFlag = lackFlag;
		}
		var page = productGridCtrl.getGridParam("page");
		productGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:page}).trigger("reloadGrid");
	}
	
	function loadProductData(treeObj){
		var filter = {};
		var title = textGet("q_title");
		if(title){
			filter.title = title.trim();
		}
		var catId = $id("q_goodsCat").attr("data-id");
		if(catId){
			filter.catId = ParseInt(catId);
		}
		var lackFlag = intGet("q_lackFlag");
		if(lackFlag){
			filter.lackFlag = lackFlag;
		}
		//
		var gridConfig = {
			      url : getAppUrl("/goods/product/list/get/-shop"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(filter,true)},
			      height : "100%",
				  width : "100%",
				  colNames : ["id","产品名称","分类","销售价","供货状态","操作"],  
			      colModel : [{name:"id",hidden:true},
			                  {name:"title",width:200,align : 'left',formatter : function (cellValue) {
			                	 var title = cellValue || "";
			                	 return "<div class='auto height'>"+title+"</div>";
								}},
							  {name:"catId",width:100,align : 'left',formatter : function (cellValue) {
			                	  var node = treeObj.getNodeByParam("id", cellValue, null);
								  return node.name;
							  }}, 
			                  {name:"salePrice",width:60,align : 'right',formatter : function (cellValue) {
				                	 return cellValue+"元";
								}}, 
			                  {name:"lackFlag", index:"lackFlag",width:70,align : 'center',formatter : function (cellValue) {
									return cellValue==false?'有货':'<font style="color:red;">无货</font>';
								}},
			                  {name:'id',index:'id', formatter : function (cellValue,option,rowObject) {
			                	  var actionStr = "<span> [<a class='item' href='javascript:void(0);' onclick='goView("
										+ cellValue
										+ ")' >查看</a>]   ";
			                	  //
			                	  var lackFlag = rowObject.lackFlag;
			                	  if(!lackFlag){
			                		  actionStr += "[<a class='item' href='javascript:void(0);' onclick='goUpdateLackFlag("
											+ cellValue + "," + 1 + ")' >缺货</a>]   ";
			                	  }else{
			                		  actionStr += "[<a class='item' href='javascript:void(0);' onclick='goUpdateLackFlag("
											+ cellValue+ "," + 0 + ")' >有货</a>]  ";
			                	  }
			                	  actionStr += "[<a class='item' href='javascript:void(0);' onclick='goDeleteShopProduct("
										+ cellValue+ ")' >删除</a>]   "
										+ "</span>";
								return actionStr;
							},
						width:180,align:"center"}],  
				multiselect:true,
				multikey:'ctrlKey',
				pager : "#productGridPager",
				ondblClickRow: function(rowId) {
					goView(rowId);
				}
			};
		
		//注册jqGrid
		productGridCtrl = initMyJqGrid("productGridCtrl", gridConfig, productGridHelper, getCallbackAfterGridLoaded);

	}
	
	//------------------------------------------------初始化--------------------------------------------------------------------
	
	function initViewDialog(){
		//
		viewProductDialog = $("#viewProductDialog").dialog({
			autoOpen : false,
			height : Math.min(420, $window.height()),
			width : Math.min(350, $window.width()),
			modal : true,
			title : '查看产品信息',
			buttons : {
				"关闭" : function() {
					viewProductDialog.dialog("close");
				}
			},
			close : function() {

			}
		});
	}
	
	function showGoodsCatTree(){
		var cityObj = $("#q_goodsCat");
		var cityOffset = $("#q_goodsCat").offset();
		$("#menuContent").css({
		}).slideDown("fast");
		//
		categDialog.dialog("open");
	}
	
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
			$id("q_goodsCat").attr("data-id", treeNode.id);
			$id("q_goodsCat").val(treeNode.name);
			$id("q_goodsCat").attr("data-unit", treeNode.unit);
			$id("q_goodsCat").attr("data-weightUnit", treeNode.weightUnit);
		}else{
			$id("q_goodsCat").attr("data-id", "");
			$id("q_goodsCat").val("");
			$id("q_goodsCat").attr("data-unit", "");
			$id("q_goodsCat").attr("data-weightUnit", "");
		}
	}
	
	//
	function createTree(data) {
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
			nodeMap["unit"] = data.unit;
			nodeMap["weightUnit"] = data.weightUnit;
			if (data.level < 3) {
				nodeMap["open"] = true;
			}
			catNodes.add(nodeMap);
		});
		
		 var zTree = $.fn.zTree.init($("#catUl"), setting, catNodes);
		 loadProductData(zTree);
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
				var zTree = $.fn.zTree.getZTreeObj("catUl");
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
	
	function initGoodsCat(){
		var hintBox = Layer.progress("正在加载...");
		//
		var ajax = Ajax.post("/goods/categ/list/get");
	
		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				var cats = result.data;
				createTree(cats);
				//
				$id("q_goodsCat").click(function(){
					showGoodsCatTree();
				});
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.always(function() {
			hintBox.hide();
		});
		ajax.go();
	}
	
	function initCategDialog(){
		categDialog = $("#catDialog").dialog({
			autoOpen : false,
			height : Math.min(350, $window.height()),
			width : Math.min(550, $window.width()),
			modal : true,
			title : '商品分类选择',
			buttons : {
				"确定" : function() {
					categDialog.dialog("close");
				},
				"取消" : function() {
					categDialog.dialog("close");
				}
			},
			close : function() {
				categDialog.dialog("close");
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
		var gridCtrlId = "productGridCtrl";
		var jqGridBox = $("#gbox_" + gridCtrlId);
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
		var pagerHeight = $id("productGridPager").height();
		productGridCtrl.setGridWidth(mainWidth - 1);
		productGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 60);
	}
	</script>
</body>
</html>
