<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>产品列表</title>
<link rel="stylesheet" href="<%=resBaseUrl%>/lib/ztree/css/zTreeStyle.css" />
<style type="text/css">
</style>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<label class="label">产品名称</label>
				<input id="q_title" class="input one half wide"/> 
				<span class="spacer"></span> 
				<label class="label">商品分类</label>
				<input id="goodsCat" readonly="readonly" placeholder="请选择..." class="input wide" />
				<a id="clearGoodsCatBtn" href="javascript:void(0);">清空</a>
				<span class="spacer"></span> 
				<label class="field label">上下架</label>
				<select id="shelfStatus" class="input">
					<option value="">-全部-</option>
					<option value="0">未上架</option>
					<option value="1">已上架</option>
					<option value="2">已下架</option>
				</select>
				<span class="spacer"></span> 
				<label class="field label">是否删除</label>
				<select id=deleted class="input half wide">
					<option value="">-全部-</option>
					<option value="0" selected="selected">否</option>
					<option value="1">是</option>
				</select>
				<span class="vt divider"></span>
				<button id="btnQuery" class="button">查询</button>
			</div>
			<span class="hr divider"></span>
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnAddGoods" class="button">新增商品</button>
					<button id="btnBatchDel" class="button">批量删除</button>
					<span class="vt divider"></span>
					<button id="btnBatchShelfUp" class="button">批量上架</button>
					<button id="btnBatchShelfDown" class="button">批量下架</button>
				</div>
				<!-- <span class="vt divider"></span> -->
				<button id="btnBatchExp" class="button" style="display: none;">批量导出</button>
				<button id="btnAutoShelf" class="button" style="display: none; width: 100px;">自动上下架</button>
			</div>
		</div>
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	<div id="categTreeDialog" style="display: block; text-align: center;">
		<div id="menuContent">
			<ul id="catTree" class="ztree"></ul>
		</div>
	</div>
	<div id="productDetailDialog" >
		<div class="form">
			<div class="field row">
				<label class="field label">产品名称</label>
				<input id="title" class="field value three wide" disabled/> 
			</div>
			<div class="field row">
				<label class="field label">所属商品</label>
				<input id="v_goodsName" class="field value two wide" disabled/> 
			</div>
			<div class="field row">
				<div class="field group">
					<label class="field label">采购价</label>
					<input id="purchPrice" class="field value" disabled/>
				</div>
				<div class="field group">
					<label class="inline field label one half wide">批发价</label>
					<input id="wholePrice" class="field value" disabled/> 
				</div>
			</div>
			<div class="field row">
				<div class="field group">
					<label class="field label">原价</label>
					<input id="origPrice" class="field value" disabled/>
				</div>
				<div class="field group">
					<label class="inline field label one half wide">销售价</label>
					<input id="salePrice" class="field value" disabled/> 
				</div>
			</div>
			<div class="field row">
				<label class="field label">重量</label>
				<input id="weight" class="field value" disabled/>
				<label class="inline field label one half wide">库存量</label>
				<input id="quantity" class="field value" disabled/>
			</div>
			<div class="field row">
				<label class="field label">创建时间</label>
				<input id="createTime" class="field value" disabled/>
				<label class="inline field label one half wide">上下架状态</label>
				<input id="v_shelfStatus" class="field value" disabled/>
			</div>
			
		</div>
		
	</div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		// 缓存当前jqGrid数据行数组
		var productGridHelper = JqGridHelper.newOne("");
		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			console.log("mainWidth:" + mainWidth + ", " + "mainHeight:"+ mainHeight);
			//
			var gridCtrlId = "theGridCtrl";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv",jqGridBox).height();
			var pagerHeight = $id("theGridPager").height();
			jqGridCtrl.setGridWidth(mainWidth - 3);
			jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 90);
		}
		
		function goAddGoods(){
			setPageUrl(getAppUrl("/goods/add/jsp"));
		}
		
		function goAddProduct(id){
			setPageUrl(getAppUrl("/goods/edit/jsp?goodsId="+id+"&action=p"));
		}
		
		function goUpdate(gId, pId){
			setPageUrl(getAppUrl("/goods/edit/jsp?goodsId="+gId+"&action=p&pId="+pId));
		}
		
		function goShow(pId){
			//$id("productDetail").attr("src",getAppUrl("/goods/product/detail/jsp?goodsId="+gId+"&productId="+pId));
			var productMap = productGridHelper.getRowData(pId);
			showProduct(productMap);
			productDialog.dialog("open");
		}
		
		function showProduct(productMap){
			$id("title").val(productMap.title);
			$id("v_goodsName").val(productMap.goodsName);
			$id("purchPrice").val(productMap.purchPrice);
			$id("wholePrice").val(productMap.wholePrice);
			$id("origPrice").val(productMap.origPrice);
			$id("salePrice").val(productMap.salePrice);
			var weight = productMap.weight;
			if(weight) weight = weight+productMap.weightUnit;
			$id("weight").val(weight);
			var quantity = productMap.quantity;
			if(quantity) quantity = quantity+productMap.unit;
			$id("quantity").val(quantity);
			$id("createTime").val(productMap.createTime);
			var shelfStatus = productMap.shelfStatus;
			var statusText = "";
			statusText = shelfStatus == 1 ? "已上架" : (shelfStatus == 2 ?"已下架" : "未上架");
			$id("v_shelfStatus").val(statusText);
		}
		
		//删除单个
		function deleteById(id){
			var theLayer ={};
			theLayer= Layer.confirm('确定要删除该产品吗？', function(){
			var postData = { productId:id  };
		  	var ajax = Ajax.post("/goods/product/delete/by/id/do");
		  	    ajax.data(postData);
			    ajax.done(function(result, jqXhr){
			    	if(result.type== "info"){
						theLayer.hide();
						Layer.msgSuccess("删除成功");
						queryProducts();
					}else{
						theLayer.hide();
						Layer.msgWarning(result.message);
					}
			 });
			ajax.go();
			});
		  }
		
		//删除多个
		function batchDeleteByIds(){
			var ids=jqGridCtrl.jqGrid("getGridParam","selarrrow");
			if(ids==""){
				Toast.show("请先选择要上架的产品", 3000, "warning");
				return;
			}
			for(var i=0;i<ids.length;i++){
				var rowData = jqGridCtrl.jqGrid("getRowData",ids[i]);//根据上面的id获得本行的所有数据
		        var val= rowData.shelfStatus; //获得制定列的值 （auditStatus 为colModel的name）
		        if("1"==val){
		        	Toast.show("亲，上架商品需下架后才能删除哦~", 3000, "warning");
		        	return;
		        }
			}
			//
			var theLayer = {};
			theLayer = Layer.confirm('确定要删除所选产品吗？', function(){
			var hintBox = Layer.progress("正在删除...");
			var postData = { productIds : ids  };
		  	var ajax = Ajax.post("/goods/product/delete/by/ids");
		  	    ajax.data(postData);
			    ajax.done(function(result, jqXhr){
			    	if(result.type== "info"){
						theLayer.hide();
						Layer.msgSuccess("删除成功")
						queryProducts();
					}else{
						theLayer.hide();
						Layer.msgWarning(result.message);
					}
			 });
		    ajax.always(function() {
				hintBox.hide();
			});
			ajax.go();
			});
		  }
		
		var catNodes = [];
		var jqGridCtrl;
		var categTreeDialog;
		var productDialog;
		var searchFlag = false;
		
		function queryProducts(){
			var postData = {};
			var title = textGet("q_title");
			if(title){
				postData.title = title;
			}
			var catId = $id("goodsCat").data("data-id");
			if(catId){
				postData.catId = catId;
			}
			var shelfStatus = textGet("shelfStatus");
			if(shelfStatus){
				postData.shelfStatus = shelfStatus;
			}
			var deleted = textGet("deleted");
			if(deleted){
				postData.deleted = deleted;
			}else{
				postData.deleted = 0;
			}
			//获取当前页码
			var page = jqGridCtrl.getGridParam("page");
			jqGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(postData,true)},page:page}).trigger("reloadGrid");
			//
			getCallbackAfterGridLoaded = function(){};
		}
		//
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
		
		function createTree(data) {
			var catNodes = [];
			$.each(data, function() {
				var nodeMap = {};
				// {id:4, pId:0, name:"XXX", open:true, nocheck:true},
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
			//
			$.fn.zTree.init($("#catTree"), setting, catNodes);
		}
		function isSelected(treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj(treeId);
			var nodes = zTree.getSelectedNodes();
			for (var i = 0; i < nodes.length; i++) {
				var node = nodes[i];
				if (node.id == treeNode.id) {
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
			if (selected) {
				$id("goodsCat").val(treeNode.name);
				$id("goodsCat").data("data-id", treeNode.id);
			} else {
				$id("goodsCat").val("");
				$id("goodsCat").data("data-id", "");
			}
		}
		
		function showGoodsCats() {
			categTreeDialog.dialog("open");
		}
		//批量下架
		function btnBatchDownShelf(){
			ids=jqGridCtrl.jqGrid("getGridParam","selarrrow");
			if(ids==""){
				Toast.show("请先选择要下架的产品");
				return;
			}
			for(var i=0;i<ids.length;i++){
				var rowData = jqGridCtrl.jqGrid("getRowData",ids[i]);//根据上面的id获得本行的所有数据
		        var val= rowData.shelfStatus; //获得制定列的值 （auditStatus 为colModel的name）
		        if("2"==val){
		        	Toast.show("包含已经下架的商品");
		        	return;
		        }
			}
			var postData = [];
			for(var i = 0;i<ids.length;i++){
				postData.add(ids[i]);
			}
			var theLayer = Layer.confirm('确定要下架这些产品吗？', function() {
				//
				var hintBox = Layer.progress("正在下架...");
				var ajax = Ajax.post("/goods/products/batch/downShelf/do");
				ajax.data(postData);
				ajax.done(function(result, jqXhr) {
					//111
					jqGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
					getCallbackAfterGridLoaded = function(){};
					Layer.msgSuccess(result.message);
				});
				ajax.always(function() {
					hintBox.hide();
				});
				ajax.go();
			});
			
		}
		//批量上架
		function btnBatchUpShelf(){
			ids=jqGridCtrl.jqGrid("getGridParam","selarrrow");
			if(ids==""){
				Toast.show("请先选择要上架的产品");
				return;
			}
			for(var i=0;i<ids.length;i++){
				var rowData = jqGridCtrl.jqGrid("getRowData",ids[i]);//根据上面的id获得本行的所有数据
		        var val= rowData.shelfStatus; //获得制定列的值 （auditStatus 为colModel的name）
		        if("1"==val){
		        	Toast.show("包含已经上架的商品");
		        	return;
		        }
			}
			var postData = [];
			for(var i = 0;i<ids.length;i++){
				postData.add(ids[i]);
			}
			var theLayer = Layer.confirm('确定要上架这些产品吗？', function() {
				//
				var hintBox = Layer.progress("正在上架...");
				var ajax = Ajax.post("/goods/products/batch/upShelf/do");
				ajax.data(postData);
				ajax.done(function(result, jqXhr) {
					jqGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
					getCallbackAfterGridLoaded = function(){};
					Layer.msgSuccess(result.message);
				});
				ajax.always(function() {
					hintBox.hide();
				});
				ajax.go();
			});
			
		}
		//
		function clearGoodsCat(){
			$id("goodsCat").val("");
			$id("goodsCat").data("data-id", "");
			//取消当前所有选中的节点
			var treeObj = $.fn.zTree.getZTreeObj("catTree");
			treeObj.cancelSelectedNode();
		}
		//
		function initGoodsCat() {
			//
			var ajax = Ajax.post("/goods/categ/list/get");
		
			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					var cats = result.data;
					createTree(cats);
					$id("goodsCat").click(showGoodsCats);
					$id("clearGoodsCatBtn").click(clearGoodsCat);
					//
					loadProductData();
					
				} else {
					Layer.msgWarning(result.message);
				}
		
			});
		
			ajax.go();
		}
		
		function loadProductData(){
			//
			var postData = {};
			var title = textGet("q_title");
			if(title){
				postData.title = title;
			}
			var catId = $id("goodsCat").data("data-id");
			if(catId){
				postData.catId = catId;
			}
			var shelfStatus = textGet("shelfStatus");
			if(shelfStatus){
				postData.shelfStatus = shelfStatus;
			}
			var deleted = textGet("deleted");
			if(deleted){
				postData.deleted = deleted;
			}else{
				postData.deleted = 0;
			}
			//
			jqGridCtrl= $("#theGridCtrl").jqGrid({
			      url : getAppUrl("/goods/product/list/by/context"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : "100%",
				  width : "100%",
			      colNames : ["id","产品名称","分类","所属商品","原价（元）","销售价（元）","上下架"," 商品操作","操作",""],  
			      colModel : [{name:"id",hidden:true, width:30},
			                  {name:"title",width:100,align : 'left',formatter : function (cellValue) {
			                	 return "<div class='auto height'>"+cellValue+"</div>";
								}},
							  {name:"catId",width:40,align : 'center',formatter : function (cellValue) {
			                	  var zTree = $.fn.zTree.getZTreeObj("catTree");
			                	  var node = zTree.getNodeByParam("id", cellValue, null);
								  return node.name;
							  }}, 
							  {name:"goods.name",width:80,align : "left",formatter : function (cellValue,option,rowObject) {
				                	 return "<div class='auto height'>"+cellValue+"</div>";
							  }},
			                  {name:"origPrice",width:30,align : 'right',formatter : function (cellValue) {
				                	 return cellValue.toFixed(2);
								}}, 
							  {name:"salePrice",width:30,align : 'right',formatter : function (cellValue) {
		                	   		 return cellValue.toFixed(2);
							  }},
			                  
							  {name:"shelfStatus",width:30,align : "center",formatter : function (cellValue) {
			                	  var str = "未上架";
			                	  if(cellValue == 1){
			                		  str = "已上架";
			                	  }else if(cellValue == 2){
			                		  str = "已下架";
			                	  }
									return str;
								}},
							  {name:"goods.id",width:40,align : "center",formatter : function (cellValue,option,rowObject) {
				                	 return "[<a class='item' href='javascript:void(0);' onclick='goAddProduct("
										+ cellValue
										+ ")' >添加产品</a>]";
							  }},
			                  {name:'id',index:'id', formatter : function (cellValue,option,rowObject) {
			                	var shelfStatus = rowObject.shelfStatus;
			                	var actionStr = "[<a class='item' href='javascript:void(0);' onclick='goShow("+cellValue+ ")' >查看</a>] ";
			                	//
			                	if(shelfStatus == 1){//上架状态
			                		actionStr += "[<a class='item' href='javascript:void(0);' onclick='goUnShelveProduct("+ cellValue + ")' >下架</a>] ";
			                	}else{//未上架状态//下架状态
			                		//上架操作
			                		actionStr += "[<a class='item' href='javascript:void(0);' onclick='goShelveProduct("+ cellValue + ")' >上架</a>] ";
			                		//修改操作
			                		actionStr += "[<a class='item' href='javascript:void(0);' onclick='goUpdate("+ rowObject.goodsId + "," +cellValue+ ")' >修改</a>] ";
			                		//删除操作
			                		actionStr += "[<a class='item' href='javascript:void(0);' onclick='deleteById("+ cellValue+ ")' >删除</a>] ";
			                		//设置促销标签操作
			                		actionStr += "[<a class='item' href='javascript:void(0);' onclick='openShowPrmtTagDlg("+ cellValue+ ")' >设置促销标签</a>]";
			                	}
								return "<span>"+actionStr+"</span>";
							},
						width:100,align:"center"},
						{name:"shelfStatus",index:'shelfStatus', hidden:true}],  
				 multiselect:true,
				 multikey:'ctrlKey',
				 pager : "#theGridPager",
				 loadComplete : function(gridData) { // JqGridHelper缓存最新的grid数据
					 productGridHelper.cacheData(gridData);
						var callback = getCallbackAfterGridLoaded();
						if (isFunction(callback)) {
							callback();
						}
					}
			});
		}
		
		getCallbackAfterGridLoaded = function(){
		};
		
		function displayCategAttr(){
			var zTree = $.fn.zTree.getZTreeObj("catTree");
			var checkNode = zTree.getCheckedNodes(true);
			var categId = null;
			if(!isNoB(checkNode)){
				categId = checkNode[0].id;
			}
			categTreeDialog.dialog("close");
			getCallbackAfterGridLoaded = function(){};
		}
		
		function initDialog(){
			categTreeDialog = $("#categTreeDialog").dialog({
				autoOpen : false,
				height : Math.min(350, $window.height()),
				width : Math.min(550, $window.width()),
				modal : true,
				title : '商品分类选择',
				buttons : {
					"确定" : function() {
						displayCategAttr();
					},
					"取消" : function() {
						categTreeDialog.dialog("close");
						getCallbackAfterGridLoaded = function(){};
					}
				},
				close : function() {
					categTreeDialog.dialog("close");
					getCallbackAfterGridLoaded = function(){};
				}
				
			});
			//
			productDialog = $("#productDetailDialog").dialog({
				autoOpen : false,
				height : Math.min(400, $window.height()),
				width : Math.min(600, $window.width()),
				modal : true,
				title : '商品详情',
				buttons : {
					"关闭" : function() {
						productDialog.dialog("close");
						getCallbackAfterGridLoaded = function(){};
					}
				},
				close : function() {
					productDialog.dialog("close");
					getCallbackAfterGridLoaded = function(){};
				}
				
			});
		}
		//上架产品
		function goShelveProduct(productId){
			var hintBox = Layer.progress("正在保存数据...");
			var ajax = Ajax.post("/goods/product/shelve/by/id/do");
			ajax.data({productId:productId, shelfStatus:1});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					//
					queryProducts();
					
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
		function validateInUseByShop(productId){
			var inUse = false;
			var ajax = Ajax.post("/goods/product/validate/inUse/by/productId/do");
			ajax.data({productId:productId});
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					inUse = result.data > 0;
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
			//
			return inUse;
		}
		//下架产品
		function goUnShelveProduct(productId){
			var inUse = validateInUseByShop(productId);
			var msg = "确定要下架吗？";
			if(inUse){
				msg = "商品正在被店铺使用中，确定要下架吗？";
			}
			var theLayer = Layer.confirm(msg, function() {
				theLayer.hide();
				UnShelveProduct(productId);
			});
		}
		//下架货品
		function UnShelveProduct(productId){
			var hintBox = Layer.progress("正在保存数据...");
			var ajax = Ajax.post("/goods/product/shelve/by/id/do");
			ajax.data({productId:productId, shelfStatus:2});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					//
					queryProducts();
					
				} else {
					Layer.msgWarning(result.message);
				}

			});
			ajax.always(function() {
				hintBox.hide();
			});
			
			ajax.go();
		}
		
		//------------------------------促销标签----------------------------------------
		function openShowPrmtTagDlg(svcId){
			//对话框参数名
			var argName = "svcId";
			//对话框参数 预存
			setDlgPageArg(argName, svcId);
			//对话框信息
			var pageTitle = "选择促销标签";
			var pageUrl = "/market/prmtTag/list/for/product/jsp";
			var extParams = {};
			//
			pageUrl = makeDlgPageUrl(pageUrl, argName, extParams);
			var theDlg = Layer.dialog({
				title : pageTitle,
				src : pageUrl,
				area : [ '100%', '90%' ],
				closeBtn : true,
				maxmin : true, //最大化、最小化
				btn : ["关闭"]
			});
		}
		
		//------------------------初始化-------------------------
		$(function() {
			//页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 90,
				allowTopResize : false
			});
			//隐藏布局north分割线
			$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
			//
			initGoodsCat();
			initDialog();
			//
			$("#btnQuery").click(queryProducts);
			$("#btnAddGoods").click(goAddGoods);
			//绑定批量下架
			$id("btnBatchShelfDown").click(btnBatchDownShelf);
			$id("btnBatchShelfUp").click(btnBatchUpShelf);
			$id("btnBatchDel").click(batchDeleteByIds);
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
		});
	</script>
</body>

</html>
