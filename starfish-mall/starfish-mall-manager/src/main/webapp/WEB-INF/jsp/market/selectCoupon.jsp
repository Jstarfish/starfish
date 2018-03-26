<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>选择优惠券</title>

<style type="text/css">
</style>
</head>
<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div style="float: left;">
			<div class="filter section">
				<div class="filter row">
					<div class="aligned group">
						<label class="label">服务优惠券名称</label> <input id="q_goodsName"
							class="input one half wide" style="width: 100px"/>
						<button id="querySvcBtn" class="button">查询</button>
						<span class="vt divider"></span>
					</div>
				</div>
			</div>
			<table id="carSvcGridCtrl"></table>
			<div id="carSvcGridPager"></div>
		</div>
		<div style="float: right; margin-right: 20px;">
			<div class="filter section">
				<div class="filter row">
					<div class="aligned group">
						<label class="label">商品优惠券名称</label> <input id="q_goodsName"
							class="input one half wide" style="width: 100px" />
						<button id="queryGoodsBtn" class="button">查询</button>
						<span class="vt divider"></span>
					</div>
				</div>
			</div>
			<table id="productGridCtrl"></table>
			<div id="productGridPager"></div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script>
		//------------------------------------------初始化配置---------------------------------{{
		//获取宿主已选择服务id
		var __dlgArg = getDlgArgForMe() == null ? {} : getDlgArgForMe();
		//
		// 缓存当前jqGrid数据行数组
		var productGridHelper = JqGridHelper.newOne("");
		var carSvcGridHelper = JqGridHelper.newOne("");
		//
		var productGridCtrl=null;
		var carSvcGridCtrl=null;
		$(function(){
			//页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				allowTopResize : false
			});
			
			loadProductData();
			loadSvcCouponData();
			var winSizeProduct = new __WinSizeMonitor();
			winSizeProduct.start(productCtrlsSize);
			
			var winSizeSvc = new __WinSizeMonitor();
			winSizeSvc.start(carSvcCtrlsSize);
		});
		//初始货品列表
		function loadProductData(){
			var filter = {};
			filter.disabled = false;
			//
			productGridCtrl=$("#productGridCtrl").jqGrid({
			      url : getAppUrl("/market/coupon/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(filter,true)},
			      height : "100%",
			      width:"100%",
				  colNames : ["名称","货品名称","操作"],  
			      colModel : [
			                  {name:"name",width:150,align : 'left',formatter : function (cellValue) {
			                	 return "<div class='auto height'>"+cellValue+"</div>";
								}},
							  {name:"productName",width:300,align : 'right'},
							  {name:"id",index:'id',width:100,align : 'center',formatter : function (cellValue) {
								    var ids = __dlgArg.ids;
									var index =ids.indexOf("goods"+cellValue);
									if(index != -1){
										return "<span id='goods"+cellValue+"'>已选择</span>";
									}else{
										return "<span id='goods"+cellValue+"'><a class='item bindGoods' href='javascript:void(0);' cellValue='" + cellValue + "' >选择</a></span>";
									}
								 }},
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
				}
			});
		}
		
		//-------------------------------------------------服务优惠券列表-------------------------------------------
		//初始服务优惠券列表
		function loadSvcCouponData(){
			var filter = {};
			filter.disabled = false;
			//
			carSvcGridCtrl=$("#carSvcGridCtrl").jqGrid({
			      url : getAppUrl("/market/svcCoupon/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(filter,true)},
			      height : "100%",
			      width:"100%",
				  colNames : ["名称","服务名称","操作"],  
			      colModel : [
			                  {name:"name",width:200,align : 'left',formatter : function (cellValue) {
			                	 return "<div class='auto height'>"+cellValue+"</div>";
								}},
							  {name:"svcName",width:100,align : 'right'},
							  {name:"id",index:'id',width:100,align : 'center',formatter : function (cellValue) {
								  var ids = __dlgArg.ids;
								  var index =ids.indexOf("svc"+cellValue);
								  if(index != -1){
										return "<span id='svc"+cellValue+"'>已选择</span>";
								  }else{
										return "<span id='svc"+cellValue+"'><a class='item bindSvc' href='javascript:void(0);' cellValue='" + cellValue + "' >选择</a></span>";
									}
								 }},
							  ],
				loadtext: "正在加载....",
				//multiselect:true,
				multikey:'ctrlKey',
				pager : "#carSvcGridPager",
				loadComplete : function(gridData){
					carSvcGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if(isFunction(callback)){
						callback();
					}
				}
			});
		}
		
		$(document).on("click", ".bindSvc", function() {
			var id=$(this).attr("cellValue");
			var obj = carSvcGridHelper.getRowData(id);
			var item ={
					itemId:obj.id,
					itemName:obj.name,
					itemSelectType:"coupon",
					itemFlag:1,
					itemType:obj.type,
					ruleId : __dlgArg.ruleId
			}
			var flag = callHostFunc("addRuleGiftItem",item);
			if(flag){
				Toast.show("加入成功！");
				$id("svc"+id).html("已选择");
				__dlgArg.ids.add(id);
			}
		});
		
		$(document).on("click", ".bindGoods", function() {
			var id=$(this).attr("cellValue");
			var obj = productGridHelper.getRowData(id);
			var item ={
					itemId:obj.id,
					itemName:obj.name,
					itemSelectType:"coupon",
					itemFlag:0,
					itemType:obj.type,
					ruleId : __dlgArg.ruleId
			}
			var flag = callHostFunc("addRuleGiftItem",item);
			if(flag){
				Toast.show("加入成功！");
				$id("goods"+id).html("已选择");
				__dlgArg.ids.add(id);
			}
		});
		//调整控件大小
		function productCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "productGridCtrl";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("productGridPager").height();
			productGridCtrl.setGridWidth((mainWidth - 1 - 100)*0.5);
			productGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 70);
		}
		//调整控件大小
		function carSvcCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "carSvcGridCtrl";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("carSvcGridPager").height();
			carSvcGridCtrl.setGridWidth((mainWidth - 1 - 100)*0.5);
			carSvcGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 70);
		}
		function getCallbackAfterGridLoaded() {
		}
	</script>
</body>
</html>