<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>销售楼层管理</title>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnAdd" class="button">添加</button>
					<%-- button id="btnDelShops" class="button">批量删除</button>--%>
				</div>
				<div class="group right aligned">
					<label class="label">专区名称</label> <input id="queryName"
						class="normal one wide input" /> <span class="spacer"></span> 
					<button id="btnToQuery" class="normal button">查询</button>
				</div>
			</div>
		</div>
		<table id="salesRegionGridCtrl"></table>
		<div id="salesRegionPager"></div>
	</div>
	<!-- 	隐藏Dialog -->
	<div id="userDlg" style="display: none;">
		<div id="addForm" class="form">
			<div id="userList" class="noBorder" style="margin-left: 20px;">
				<div id="singleSalesRegion" style="margin-left: 20px;">
					<div class="field row" style="margin-top: 10px;">
						<label class="field label required one wide">专区名称</label>
						<input type="text" class="field value one half wide" id="name" >
						<input type="hidden" class="field value one half wide" id="salesRegionId" >
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label required one wide">楼层名称</label>
						<select class="field value one half wide" id="floorNo"></select>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--选择专区dialog -->
	<div id="regionGoodsDialog" class="form" style="display: none;height: 600px;">
		
		<div class="ui-layout-center">
			<div class="filter section">
				<div class="filter row">
					<div class="group left aligned">
						<button id="btnAddProduct" class="button">添加</button>
						<%-- button id="btnDelShops" class="button">批量删除</button>--%>
					</div>
					<%--div class="group right aligned">
						<label class="label">商品名称</label> 
						<input id="productName" class="normal input" /> 
						<span class="spacer"></span> 
						<label class="label">标题</label> 
						<input id="productTitle" class="normal input" /> 
						<span class="spacer"></span> 
						<button id="btnQueryProduct" class="normal button">查询</button>
					</div> --%>
				</div>
			</div>
			<table id="regionGoodsGrid"></table>
			<div id="regionGoodsPager"></div>
		</div>
	</div>
	
	<!--选择商品dialog -->
	<div id="selectRegionGoodsDialog" class="form" style="display: none;height: 600px;">
		
		<div class="ui-layout-center">
			<div class="filter section">
				<div class="filter row">
					<div class="group right aligned">
						<label class="label">商品名称</label> 
						<input id="selectProductName" class="normal input" /> 
						<span class="spacer"></span>
						<label class="label">标题</label> 
						<input id="selectProductTitle" class="normal input" /> 
						<span class="spacer"></span>
						<button id="btnSelectQueryProduct" class="normal button">查询</button>
					</div>
				</div>
			</div>
			<table id="selectRegionGoodsGrid"></table>
			<div id="selectRegionGoodsPager"></div>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		var salesRegionGridCtrl = null;
		//
		var salesRegionGridHelper = JqGridHelper.newOne("");
		//
		var regionGoodsGridCtrl = null;
		//
		var regionGoodsGridHelper = JqGridHelper.newOne("");
		//
		var selectRegionGoodsGridCtrl = null;
		//
		var selectRegionGoodsGridHelper = JqGridHelper.newOne("");
		//
		var formProxy = FormProxy.newOne();
		//
		//获取商品楼层
		function loadSalesFloor(callback) {
			// 隐藏页面区
			var ajax = Ajax.post("/salesFloor/selectList/get");
			ajax.params({
				type : 0
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("floorNo", result.data);
					if($.isFunction(callback)){
						callback();
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		//加载销售楼层列表数据
		function loadSalesRegionData(){
			var postData = {
				type : 0
			};
			//加载销售楼层
			salesRegionGridCtrl= $id("salesRegionGridCtrl").jqGrid({
			      url : getAppUrl("/salesFloor/salesRegion/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : 250,
				  width : "100%",
			      colNames : ["楼层号", "楼层名称", "专区名称",  "操作"],  
			      colModel : [{
								name : "floorNo",
								index : "floorNo",
								width : 100,
								align : 'center'
							},
							{
								name : "salesFloor.name",
								index : "salesFloor.name",
								width : 100,
								align : 'center'
							},
							{
								name : "name",
								index : "name",
								width : 200,
								align : 'center'
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									return salesRegion = "[<a class='item' href='javascript:void(0);' onclick='openShowDlg(" + cellValue + ")' >查看</a>]<span class='chs spaceholder'></span>"
													+ "[<a class='item' href='javascript:void(0);' onclick='openEditDlg(" + cellValue + ")' >编辑</a>]<span class='chs spaceholder'></span>"
													+ "[<a class='item' href='javascript:void(0);' onclick='toShowProducts(" + cellValue + ")' >专区商品</a>]<span class='chs spaceholder'></span>"
													+ "[<a class='item' href='javascript:void(0);' onclick='toDelSalesRegion(" + cellValue + ")' >删除</a>]<span class='chs spaceholder'></span>";
								},
								width : 200,
								align : "center"
							}],  
				loadtext: "Loading....",
				multiselect : true,// 定义是否可以多选
				multikey:'ctrlKey',
				pager : "#salesRegionPager",
				loadComplete : function(gridData){
					salesRegionGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if(isFunction(callback)){
						callback();
					}
				},
				gridComplete : function(){
				},
				ondblClickRow : function(rowId) {
					openShowDlg(rowId);
				}
			});
		}
		
		function getCallbackAfterGridLoaded() {
		}
		var salesRegionFlag = false;
		//
		formProxy.addField({
			id : "name",
			required : true,
			rules : ["maxLength[30]"],
			messageTargetId : "name"
		});
		//
		formProxy.addField({
			id : "floorNo",
			required : true,
			rules : [{
				rule : function(idOrName, type, rawValue, curData) {
					checkNameAndFloorNo(rawValue);
					if(salesRegionFlag){
						return false;
					}
					return true;
				},
				message : "该楼层此专区已存在！"
			}],
			messageTargetId : "floorNo"
		});
		
		//------------------------调整控件大小-------------------------------------------------------------------

		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "salesRegionGridCtrl";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("salesRegionPager").height();
			salesRegionGridCtrl.setGridWidth(mainWidth - 1);
			salesRegionGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 60);
		}
		//---------------------------业务---------------------------------
		//打开新增对话框
		function openAddDlg() {
			curAction = "add";
			//
			toShowTheDlg();
		}
		//打开查看对话框
		function openShowDlg(salesRegionId) {
			curAction = "view";
			//
			toShowTheDlg(salesRegionId);
		}
		//打开查看对话框
		function openEditDlg(salesRegionId) {
			curAction = "mod";
			//
			toShowTheDlg(salesRegionId);
		}
		//（真正）显示对话款
		function toShowTheDlg(dataId) {
			//
			var theDlgId = "userDlg";
			jqDlgDom = $id(theDlgId);
			//对话框配置
			var dlgConfig = {
				width : Math.min(450, $window.width()),
				height : Math.min(300, $window.height()),
				modal : true,
				open : false
			};
			//
			if (curAction == "add") {
				dlgConfig.title = "销售楼层 - 新增";
				dlgConfig.buttons = {
					"保存" : function() {
						//收集并验证要提交的数据（如果验证不通过直接返回 return）
						var vldResult = formProxy.validateAll();
						if (!vldResult) {
							return;
						}
						var postData = formProxy.getValues();
						//
						goAddSalesRegion(postData);
						$(this).dialog("close");
						
					},
					"取消" : function() {
						//
						jqDlgDom.prop("continuousFlag", false);
						//
						$(this).dialog("close");
						//隐藏表单验证消息
						formProxy.hideMessages();
					}
				
				};
				dlgConfig.close = function() {
					formProxy.hideMessages();	
				};
			} else if (curAction == "mod") {
				dlgConfig.title = "销售楼层 - 修改";
				dlgConfig.buttons = {
					"保存" : function() {
						//收集并验证要提交的数据（如果验证不通过直接返回 return）
						var vldResult = formProxy.validateAll();
						if (!vldResult) {
							return;
						}
						var postData = formProxy.getValues();
						//
						goEditSalesRegion(postData);
						$(this).dialog("close");
					},
					"取消" : function() {
						$(this).dialog("close");
						//隐藏表单验证消息
						formProxy.hideMessages();
					}
				};
			} else {
				//== view 查看
				dlgConfig.title = "销售楼层 - 查看";
				dlgConfig.buttons = {
					"修改 > " : function() {
						$(this).dialog("close");
						
						openEditDlg(dataId);
					},
					"关闭" : function() {
						$(this).dialog("close");
					}
				};
			}
			//
			jqDlgDom.dialog(dlgConfig);
			//
			initTheDlgCtrls(dataId);
		}
		
		function initTheDlgCtrls(dataId) {
			//批量简单设置
			if (curAction == "add" || curAction == "mod") {
				jqDlgDom.find(":input").prop("disabled", false);
			} else {
				jqDlgDom.find(":input").prop("disabled", true);
			}
			
			if (curAction == "mod"){
				$id("no").prop("disabled", true);
			}
			//重置控件值
			$("#userDlg input[type=text]").val("");
			$("#userDlg textarea").val("");
			textSet("salesRegionId", "");
			textSet("floorNo", "");
			//
			if (curAction == "view" || curAction == "mod") {
				var data = salesRegionGridHelper.getRowData(dataId);
				if (data != null) {
					textSet("salesRegionId", data.id);
					textSet("name", data.name);
					textSet("floorNo", data.floorNo);
				}
			}
		}
		
		function goAddSalesRegion(salesRegionInfoMap){
			var hintBox = Layer.progress("正在保存数据...");
			
			var ajax = Ajax.post("/salesFloor/salesRegion/add/do");
			salesRegionInfoMap["type"] = 0;
			ajax.data(salesRegionInfoMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					
					salesRegionGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
					//
					getCallbackAfterGridLoaded = function(){
						
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
		
		function toDelSalesRegion(id){
			var theLayer = Layer.confirm('确定要删除该专区吗？', function() {
				var postData = {
					id : id,
					type : 0
				};
				var ajax = Ajax.post("/salesFloor/salesRegion/delete/by/id");
				ajax.data(postData);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						theLayer.hide();
						Layer.msgSuccess(result.message);
						salesRegionGridCtrl.jqGrid("setGridParam", {
							page : 1
						}).trigger("reloadGrid");
					} else {
						theLayer.hide();
						Layer.msgWarning(result.message);
					}
				});
				ajax.go();
			});
			
		}
		
		function goEditSalesRegion(salesRegionInfoMap){
			var hintBox = Layer.progress("正在保存数据...");
			
			var ajax = Ajax.post("/salesFloor/salesRegion/edit/do");
			salesRegionInfoMap["id"] = textGet("salesRegionId");
			salesRegionInfoMap["type"] = 0;
			ajax.data(salesRegionInfoMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					
					salesRegionGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
					//
					getCallbackAfterGridLoaded = function(){
						
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
		
		//更改车辆品牌 禁用 开启状态
		function openOrClose(id) {
			var salesRegionMap = salesRegionGridHelper.getRowData(id)
			var postData = {
				id : salesRegionMap.no,
				no : salesRegionMap.no,
				disabled : salesRegionMap.disabled
			};
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/salesRegion/isDisabled/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					salesRegionGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
					Layer.msgSuccess(result.message);
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.always(function() {
				//隐藏等待提示框
				loaderLayer.hide();
			});
			ajax.go();
		}
		
		//初始化专区商品Dialog
		function initRegionGoodsDialog(){
			regionGoodsDialog = $( "#regionGoodsDialog" ).dialog({
		        autoOpen: false,
		        width : Math.min(1200, $window.width()),
		        height : Math.min(600, $window.height()),
		        modal: true,
		        title : '专区商品',
		        buttons: {
		            "关闭": function() {
		            	regionGoodsDialog.dialog( "close" );
		          }
		        },
		        close: function() {
		        	regionGoodsDialog.dialog( "close" );
		        }
		      });
		}
		
		function toShowProducts(id){
			var filter = {
				regionId : id
			};
			textSet("salesRegionId", id);
			//
			loadSelectRegionGoodsData();
			//
			regionGoodsGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			regionGoodsDialog.dialog( "open" );
		}
		
		//加载商品列表数据
		function loadRegionGoodsData(){
			var postData = {
			};
			//加载商品
			regionGoodsGridCtrl= $id("regionGoodsGrid").jqGrid({
			      url : getAppUrl("/salesFloor/salesRegionGoods/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : 370,
				  width : "100%",
				  colNames : ["id", "商品Id", "商品名称", "标题", "商品数量", "市场价", "销售价", "商品状态", "操作"],  
			      colModel : [{
								name : "id",
								index : "id",
								hidden : true
							},
							{
								name : "productId",
								index : "productId",
								hidden : true
							},
							{
								name : "product.goodsName",
								index : "product.goodsName",
								width : 220,
								align : 'left',
							},
							{
								name : "product.title",
								index : "product.title",
								width : 340,
								align : 'left',
							},
							{
								name : "product.quantity",
								index : "product.quantity",
								width : 80,
								align : 'center'
							},
							{
								name : "product.marketPrice",
								index : "product.marketPrice",
								width : 100,
								align : 'center'
							},
							{
								name : "product.salePrice",
								index : "product.salePrice",
								width : 100,
								align : 'center'
							},
							{name:"product.shelfStatus",width:100,align : "center",formatter : function (cellValue) {
		                	  var str = "未上架";
		                	  if(cellValue == 1){
		                		  str = "已上架";
		                	  }else if(cellValue == 2){
		                		  str = "已下架";
		                	  }
								return str;
							}},
							{
							name : 'id',
							index : 'id',
							formatter : function(cellValue, option, rowObject) {
								return salesRegion = "[<a class='item' href='javascript:void(0);' onclick='delRegionGoods(" + cellValue + ")' >删除</a>]<span class='chs spaceholder'></span>";
							},
							width : 170,
							align : "center"
							}],  
				loadtext: "Loading....",
				multiselect : true,// 定义是否可以多选
				multikey:'ctrlKey',
				pager : "#regionGoodsPager",
				loadComplete : function(gridData){
					regionGoodsGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if(isFunction(callback)){
						callback();
					}
				},
				gridComplete : function(){
				}
			});
		}
		
		function openAddProductDlg(){
			//
			initSelectRegionGoodsDialog();
			//
			selectRegionGoodsGridCtrl.jqGrid("setGridParam", {postData :{},page:1}).trigger("reloadGrid");
			//
			selectRegionGoodsDialog.dialog( "open" );
		}
		
		//初始化选择商品Dialog
		function initSelectRegionGoodsDialog(){
			selectRegionGoodsDialog = $( "#selectRegionGoodsDialog" ).dialog({
		        autoOpen: false,
		        width : Math.min(1000, $window.width()),
		        height : Math.min(480, $window.height()),
		        modal: true,
		        title : '选择商品',
		        buttons: {
		        	"确定": function(){
		            	addSelectGoods();
		            },
		            "关闭": function() {
		            	selectRegionGoodsDialog.dialog( "close" );
		          }
		        },
		        close: function() {
		        	selectRegionGoodsDialog.dialog( "close" );
		        }
		      });
		}
		
		function loadSelectRegionGoodsData(){
			var postData = {
				shelfStatus : 1
			};
			//加载商品
			selectRegionGoodsGridCtrl= $id("selectRegionGoodsGrid").jqGrid({
			      url : getAppUrl("/goods/product/list/get/-mall"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : 250,
				  width : "100%",
			      colNames : ["商品名称", "标题", "商品数量", "市场价", "销售价", "商品状态", "" ],  
			      colModel : [
							{
								name : "goodsName",
								index : "goodsName",
								width : 220,
								align : 'left',
							},
							{
								name : "title",
								index : "title",
								width : 320,
								align : 'left',
							},
							{
								name : "quantity",
								index : "quantity",
								width : 80,
								align : 'center'
							},
							{
								name : "marketPrice",
								index : "marketPrice",
								width : 100,
								align : 'center'
							},
							{
								name : "salePrice",
								index : "salePrice",
								width : 100,
								align : 'center'
							},
							{name:"shelfStatus",width:100,align : "center",formatter : function (cellValue) {
		                	  var str = "未上架";
		                	  if(cellValue == 1){
		                		  str = "已上架";
		                	  }else if(cellValue == 2){
		                		  str = "已下架";
		                	  }
								return str;
							}},{
								name : "id",
								index : "id",
								hidden : true,
								formatter : function(cellValue, option, rowObject) {
									var salesRegionId = textGet("salesRegionId");
									var regionGoods = $id("regionGoodsGrid").jqGrid("getRowData");
									if(regionGoods){
										for(var i = 0; i < regionGoods.length; i++){
											if(regionGoods[i].productId == cellValue){
												return "1";
											}
										}
									}
									return '0';
								}
							}],  
				loadtext: "Loading....",
				multiselect : true,// 定义是否可以多选
				multikey:'ctrlKey',
				pager : "#selectRegionGoodsPager",
				loadComplete : function(gridData){
					selectRegionGoodsGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if(isFunction(callback)){
						callback();
					}
				},
				gridComplete : function(){
				}
			});

		}
		
		function addSelectGoods(){
			var regionGoods = selectRegionGoodsGridCtrl.jqGrid("getGridParam", "selarrrow");
			if (regionGoods == "") {
				Layer.msgWarning("亲！您还没选择商品呢！");
				return;
			}
			for (var i = 0; i < regionGoods.length; i++) {
				var rowData = selectRegionGoodsGridCtrl.jqGrid("getRowData", regionGoods[i]);//根据上面的id获得本行的所有数据
				var val = rowData["id"]; //获得制定列的值 （auditStatus 为colModel的name）
				if ("1" == val) {
					Layer.msgWarning("包含已经选择的商品");
					return;
				}
			}
			toAddRegionGoods(regionGoods);
		}
		
		function toAddRegionGoods(regionGoods){
			var postData = {
				ids : regionGoods,
				regionId : textGet("salesRegionId")
			};
			var ajax = Ajax.post("/salesFloor/regionGoods/add/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					selectRegionGoodsDialog.dialog("close");
					regionGoodsGridCtrl.jqGrid("setGridParam", {postData :{},page:1}).trigger("reloadGrid");
					selectRegionGoodsGridCtrl.jqGrid("setGridParam", {postData :{},page:1}).trigger("reloadGrid");
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
		}
		
		function getCallbackAfterGridLoaded() {

		}
		
		function delRegionGoods(id){
			var theLayer = Layer.confirm('确定要删除该商品吗？', function() {
				var postData = {
					id : id
				};
				var ajax = Ajax.post("/salesFloor/regionGoods/delete/by/id");
				ajax.data(postData);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						theLayer.hide();
						Layer.msgSuccess(result.message);
						regionGoodsGridCtrl.jqGrid("setGridParam", {
							page : 1
						}).trigger("reloadGrid");
						selectRegionGoodsGridCtrl.jqGrid("setGridParam", {
							page : 1
						}).trigger("reloadGrid");
					} else {
						theLayer.hide();
						Layer.msgWarning(result.message);
					}
				});
				ajax.go();
			});
		}
		
		function checkNameAndFloorNo(floorNo){
			var postData = {
				floorNo : floorNo,
			};
			var name = textGet("name")
			if(name){
				postData.name = name;
			}
			var id = textGet("salesRegionId")
			if(id){
				postData.id = id;
			}
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/salesFloor/salesRegion/get/by/name/and/floorNo");
			ajax.sync();
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					salesRegionFlag = true;
				} else{
					salesRegionFlag = false;
				}
			});
			ajax.always(function() {
				//隐藏等待提示框
				loaderLayer.hide();
			});
			ajax.go();
		}
		
		//---------------------------业务---------------------------------
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
			//
			loadSalesRegionData();
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
			//添加车辆品牌
			$id("btnAdd").click(openAddDlg);
			//
			$id("btnAddProduct").click(openAddProductDlg);
			//
			initRegionGoodsDialog();
			//
			loadSalesFloor();
			//专区商品
			loadRegionGoodsData();
			
			//绑定查询按钮
			$id("btnToQuery").click(function() {
				
				var filter = {
					type : 0
				};
				var name = textGet("queryName");
				if(name){
					filter.name = name;
				}
				//加载jqGridCtrl
				salesRegionGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			});
			
			//绑定查询专区商品按钮
			$id("btnQueryProduct").click(function() {
				
				var filter = {
				};
				var name = textGet("productName");
				if(name){
					filter.name = name;
				}
				//加载jqGridCtrl
				selectRegionGoodsGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			});
			//绑定查询商品按钮
			$id("btnSelectQueryProduct").click(function() {
				
				var filter = {
				};
				var name = textGet("selectProductName");
				if(name){
					filter.goodsName = name;
				}
				var title = textGet("selectProductTitle");
				if(title){
					filter.title = title;
				}
				//加载jqGridCtrl
				selectRegionGoodsGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			});
		});
	</script>
</body>
</html>