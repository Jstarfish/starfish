<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>选择汽车服务列表</title>
<style type="text/css">
</style>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 0 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="right middle aligned group">
					<label class="label">服务名称</label> 
					<input id="q_name" class="input one half wide" />
					<span class="spacer"></span>
					<button id="queryBtn" class="button">查询</button>
				</div>
			</div>
		</div>

		<table id="carSvc_list"></table>
		<div id="carSvc_pager"></div>
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
			loadCarSvcData();
			//
			$id("queryBtn").click(queryCarSvcData);
			//调整控件大小
			winSizeMonitor.start(adjustCtrlsSize);
		});

		//-------------------------------------------------全局变量---------------------------------------------------------------
		//
		var carSvcCtrl = null;

		//-------------------------------------------------业务处理------------------------------------------------------------
		
		//
		function deleteShopSvc(svcId){
			var hintBox = Layer.progress("正在保存数据...");
			//
			var postData = { svcId:svcId };
		  	var ajax = Ajax.post("/carSvc/shop/svc/delete/do");
		  	    ajax.data(postData);
			    ajax.done(function(result, jqXhr){
					if (result.type == "info") {
						Toast.show(result.message);
						queryCarSvcData();
					} else {
						Layer.msgWarning(result.message);
					}
			 });
			//
		    ajax.always(function() {
				hintBox.hide();
			});
			//
			ajax.go();
		}
		
		//
		function goDeleteShopSvc(svcId){
			if(!svcId){
				Layer.msgWarning("当前服务信息丢失");
				return;
			} 
			//
			var theLayer ={};
			theLayer= Layer.confirm('确定要删除该店铺服务吗？', function(){
				theLayer.hide();
				deleteShopSvc(svcId);
			});
		}

		//
		function addShopSvc(svcId) {
			var hintBox = Layer.progress("正在保存数据...");
			var ajax = Ajax.post("/carSvc/shop/svc/add/do");
			ajax.data({svcId : svcId});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Toast.show(result.message);
					queryCarSvcData();
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.always(function() {
				hintBox.hide();
			});
			//
			ajax.go();
		}
		
		//
		function goAddShopSvc(svcId){
			if(!svcId){
				Layer.msgWarning("获取不到服务信息");
				return;
			}
			//
			addShopSvc(svcId);
		}

		function queryCarSvcData() {
			var page = carSvcCtrl.getGridParam("page");
			//
			var filter = {};
			filter.disabled = false;
			//
			var svcName = textGet("q_name");
			if (svcName) {
				filter.name = svcName;
			}
			//
			carSvcCtrl.jqGrid("setGridParam", {postData : {filterStr : JSON.encode(filter, true)}, page : page}).trigger("reloadGrid");
		}

		//------------------------------------------------初始化--------------------------------------------------------------------

		function loadCarSvcData() {
			var filter = {};
			filter.disabled = false;
			//
			var svcName = textGet("q_name");
			if (svcName) {
				filter.name = svcName;
			}
			//
			var gridConfig = {
				url : getAppUrl("/carSvc/list/get"),
				contentType : 'application/json',
				mtype : "post",
				datatype : 'json',
				postData : {
					filterStr : JSON.encode(filter, true)
				},
				height : "100%",
				width : "100%",
				colNames : [ "服务名称", "描述", "销售价格", "所属分组", "状态", "操作" ],
				colModel : [
						{
							name : "name",
							index : "name",
							width : 50,
							align : 'left'
						},
						{
							name : "desc",
							index : "desc",
							width : 100,
							align : 'left',formatter : function (cellValue) {
			                	 return "<div class='auto height'>"+cellValue+"</div>";
							}
						},
						{
							name : "salePrice",
							index : "salePrice",
							width : 30,
							align : 'right',
							formatter : function(cellValue) {
								return cellValue + "元";
							}
						},
						{
							name : "carSvcGroup.name",
							index : "carSvcGroup.name",
							width : 30,
							align : 'center'
						},
						{
							name : "existFlag",
							index : "existFlag",
							width : 30,
							align : 'center',
							formatter : function(cellValue) {
								 var existFlag = cellValue;
								 //
								 if(existFlag){
			                		 return "<span title='已使用'>√</span>";
			                	 }else{
			                		 return "";
			                	 }
							}
						},
						{
							name : "id",
							index : 'id',
							width : 30,
							align : 'center',
							formatter : function(cellValue, option, rowObject) {
								 var existFlag = rowObject.existFlag;
								 //
								 if(existFlag){
									 return "<span> [<a class='item' href='javascript:void(0);' onclick='goDeleteShopSvc("
										+ cellValue
										+ ")' >下架</a>]";
										+ "</span>";
			                	 }else{
			                		 return "<span> [<a class='item' href='javascript:void(0);' onclick='goAddShopSvc("
										+ cellValue
										+ ")' >添加</a>]";
										+ "</span>";
			                	 }
							},
						} ],
				loadtext : "正在加载....",
				multiselect:false,
				multikey : 'ctrlKey',
				pager : "#carSvc_pager"
			};
			//注册jqgrid
			carSvcCtrl = $id("carSvc_list").jqGrid(gridConfig);
		}

		//-------------------------------------------------调整控件大小------------------------------------------------------------
		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "carSvc_list";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("carSvc_pager").height();
			carSvcCtrl.setGridWidth(mainWidth - 1 - 4);
			carSvcCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 60);
		}
	</script>
</html>
