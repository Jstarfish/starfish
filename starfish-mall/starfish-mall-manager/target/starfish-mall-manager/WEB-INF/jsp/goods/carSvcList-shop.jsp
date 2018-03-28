<%@page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>车辆服务管理</title>

</head>
<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button id="addShopSvcBtn" class="button">添加</button>
				</div>
				<div class="group right aligned">
					<label class="label">服务名称</label>
					<input id="queryName" class="normal input one half wide" />
					<span class="spacer"></span>
					<button id="btnToQry" class="button">查询</button>
				</div>
			</div>
		</div>
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	
	<div id="dialog" style="display: none;">
		<div id="viewForm" class="form">
			<div class="field row">
				<label class="field label">服务名称</label> 
				<input type="text" id="name" disabled class="field value one half wide" /> 
				<label class="field inline label one wide">所属分组</label> 
				<input type="text" id="groupName" disabled class="field value one half wide" /> 
			</div>
			<div class="field row">
				<label class="field label">附加价</label> 
				<input type="text" id="affixPrice" disabled class="field value one half wide" /> 
				<label class="field inline label one wide">是否启用</label> 
				<div class="field group">
					<input type="radio" disabled id="defaulted-Y" name="disabled" value="0"> 
					<label for="defaulted-Y">启用</label> 
					<input type="radio" disabled id="defaulted-N" name="disabled" value="1">
					<label for="defaulted-N">禁用</label>
				</div>
			</div>
			<div class="field row" style="height:auto;">
				<label class="field label">服务描述</label>
				<textarea class="field value" disabled style="height: 50px; width:450px; resize: none;" id="desc"></textarea>
			</div>
			<div class="field row" style="height:auto;">
				<label class="field label">pc有色图标</label> 
				<img id="logoCarSvc" height="80px" width="120px" /> 
			</div>
			<div class="field row" style="height:auto;">
				<label class="field label">pc无色图标</label> 
				<img id="logoCarSvcTwo" height="80px" width="120px" /> 
			</div>
			<div class="field row" style="height:auto;">
				<label class="field label">app图标</label> 
				<img id="logoCarSvcApp" height="80px" width="120px" /> 
			</div>
		</div>
	</div>
	<div id="userRateSetDialog" style="display: none;">
		<div style="margin: 10px 0;">
			<label style="font-size: 14px;color: #666;">服务信息</label>
		</div>
		<div class="form">
			<input type="hidden" id="svcId"/>
			<input type="hidden" id="svcKindId"/>
			<div class="field row">
				<label class="field label one half wide">服务名称：</label> 
				<span id="svcName"></span>
			</div>
			<div class="field row">
				<label class="field label one half wide">服务图片：</label> 
				<img id="showLogoCarSvcAd" height="40px" width="100px" />
			</div>
			<div class="field row">
				<label class="field label one half wide">附加价：</label> 
				<span id="showAffixPrice"></span>元
			</div>
		</div>
		<table id="detailedList" width="90%" class="cleanTbl">
			<thead class="head">
				<tr class="row">
					<th>会员等级</th>
					<th>服务折扣</th>
				</tr>
			</thead>
			<tbody class="body" id="showUserRateSet">
			</tbody>
		</table>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js"></script>
	<script>
		var gridHelper = JqGridHelper.newOne("");
		var jqGridCtrl = null;
		var viewDialog = null;
		var userRateSetDialog;
		//初始化用户服务折扣
		initShowUserRateSetDialog();
		//初始化查看页面
		function initShowUserRateSetDialog() {
			userRateSetDialog = $("#userRateSetDialog").dialog({
				autoOpen : false,
				height : Math.min(500, $(window).height()),
				width : Math.min(500, $(window).width()),
				modal : true,
				title : '会员等级折扣',
				buttons : {
					"关闭" : function() {
						userRateSetDialog.dialog("close");
					}
				},
				close : function() {
				}
			});
		}
		
		//------------------------------------------初始化配置---------------------------------
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
			initViewDialog();
			//
			$id("btnToQry").click(queryCarSvcByName);
			//
			$id("addShopSvcBtn").click(openShowCarSvcDlg);
			
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
		});
		
		function queryCarSvcByName(){
			var filter = {};
			var q_name = textGet("queryName");
			if(q_name){
				filter.name = q_name;
			}
			var page = jqGridCtrl.getGridParam("page");
			//
			jqGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
		}
		
		function initViewDialog(){
			//
			viewDialog = $("#dialog").dialog({
				autoOpen : false,
				height : Math.min(400, $window.height()),
				width : Math.min(650, $window.width()),
				modal : true,
				title : '查看信息',
				buttons : {
					"关闭" : function() {
						viewDialog.dialog("close");
					}
				},
				close : function() {
					viewDialog.dialog("close");
				}
			});
		}
		
		function goView(rowId){
			var carSvc = gridHelper.getRowData(rowId);
			var svc = carSvc.svc;
			console.log("carSvc=>"+JSON.encode(carSvc));
			var id = svc.id;
			var groupId = svc.groupId;
			var name = svc.name;
			var desc = svc.desc;
			var salePrice = svc.salePrice;
			var seqNo = svc.seqNo;
			var disabled = svc.disabled;
			var groupName = svc.grounpName;
			$("input[name='disabled']").each(function(){
				var _disabled = $(this).val();
				if(_disabled == disabled){
					$(this).prop("checked", true);
					return;
				}
			});
			//
			$id("name").val(name);
			$id("groupName").val(groupName);
			$id("affixPrice").val(salePrice);
			$id("desc").val(desc);
			//var fileBrowseUrl = carSvc.fileBrowseUrl;
			$id("logoCarSvc").attr("src", svc.fileBrowseUrl);
			$id("logoCarSvcTwo").attr("src", svc.fileBrowseUrlIcon2);
			$id("logoCarSvcApp").attr("src", svc.fileBrowseUrlApp);
			viewDialog.dialog("open");
		}
		
		function loadCarSvcData(){
			var filter = {};
			var q_name = textGet("queryName");
			if(q_name){
				filter.name = q_name;
			}
			//
			jqGridCtrl= $("#theGridCtrl").jqGrid({
				  loadtext : "正在加载数据，请稍后...",
			      url : getAppUrl("/carSvc/shop/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(filter,true)},
			      height : "100%",
				  width : "100%",
				  colNames : ["服务名称", "服务描述","销售价","所属分组", " 操作" ], 
				  colModel : [
								{
									name : "svcName",
									index : "svcName",
									width : 100,
									align : 'left'
								},
								{
									name : "svc.desc",
									index : "svc.desc",
									width : 200,
									align : 'left',formatter : function (cellValue) {
					                	 return "<div class='auto height'>"+cellValue+"</div>";
									}
								},
								{
									name : "svc.salePrice",
									index : "svc.salePrice",
									width : 50,
									align : 'right',formatter : function (cellValue) {
					                	 return cellValue+"元";
									}
								},
								{
									name : "svc.carSvcGroup.name",
									index : "svc.carSvcGroup.name",
									width : 50,
									align : 'center'
								},
								{
									name : 'id',
									index : 'id',
									formatter : function(cellValue, option, rowObject) {
										return "<span> [<a class='item' href='javascript:void(0);' onclick='goView(" + cellValue + ")' >查看</a>]  " 
										+ "[<a class='item' href='javascript:void(0);' onclick='goDeleteShopSvc(" + cellValue + ")' >下架</a>]  "
										+ "[<a class='item' href='javascript:void(0);' onclick='goRateSetView(" + cellValue + ")' >身份折扣</a>]</span>  ";
									},
									width : 100,
									align : "center"
								} ],  
				//rowList:[10,20,30],
				multiselect:false,
				multikey:'ctrlKey',
				pager : "#theGridPager",
				loadComplete : function(gridData){
					gridHelper.cacheData(gridData);
				},
				ondblClickRow: function(rowId) {
					goView(rowId);
				}
			});
		}
		
		//-------------------------------------------------业务处理----------------------------------------------------------
		
		//
		function deleteShopSvc(shopSvcId){
			var hintBox = Layer.progress("正在保存数据...");
			//
			var postData = { id:shopSvcId };
		  	var ajax = Ajax.post("/carSvc/shop/svc/delete/by/id/do");
		  	    ajax.data(postData);
			    ajax.done(function(result, jqXhr){
					if (result.type == "info") {
						Toast.show(result.message);
						queryCarSvcByName();
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
		function goDeleteShopSvc(shopSvcId){
			if(!shopSvcId){
				Layer.msgWarning("当前服务信息丢失");
				return;
			} 
			//
			var theLayer ={};
			theLayer= Layer.confirm('确定要删除该店铺服务吗？', function(){
				theLayer.hide();
				deleteShopSvc(shopSvcId);
			});
		}
		
		function openShowCarSvcDlg(){
			//对话框信息
			var pageTitle = "选择汽车服务";
			var pageUrl = "/carSvc/list/for/shop/jsp";
			var extParams = {};
			//
			pageUrl = makeDlgPageUrl(pageUrl, {}, extParams);
			var theDlg = Layer.dialog({
				title : pageTitle,
				src : pageUrl,
				area : [ '100%', '90%' ],
				closeBtn : true,
				maxmin : true, //最大化、最小化
				btn : ["确定", "关闭"] ,
				yes : function() {
					queryCarSvcByName();
					theDlg.hide();
				},
				close : function() {
					queryCarSvcByName();
					theDlg.hide();
				},
			});
		}
		
		//-------------------------------------------------调整控件大小------------------------------------------------------------
		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "theGridCtrl";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("theGridPager").height();
			jqGridCtrl.setGridWidth(mainWidth - 1);
			jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 60);
		}
		
		function goRateSetView(id){
			var ajax = Ajax.post("/ecard/normal/list/get");
			ajax.sync();
		    ajax.done(function(result, jqXhr){
		    	if(result.type== "info"){
					userRateList = result.data;
					var theTpl = laytpl( $id("userRateSetTpl").html());
					var htmlStr = theTpl.render(userRateList);
					$id("showUserRateSet").html(htmlStr);
				}else{
					Layer.msgWarning(result.message);
				}
		    });
		    ajax.always(function() {
				//隐藏等待提示框
			});
			ajax.go();
			
			userRateSetDialog.dialog("open");
			
			var userRateSet = gridHelper.getRowData(id);
			$id("svcName").html(userRateSet.svc.name);
			$("#showLogoCarSvcAd").attr("src",userRateSet.svc.fileBrowseUrl+"?"+new Date().getTime());
			$id("showAffixPrice").html(userRateSet.svc.salePrice);
			textSet("svcId", id);
			textSet("svcKindId", userRateSet.svc.svcKindId);
			
			updateRateSet(userRateSet.svc.id);
		}
		
		function updateRateSet(id){
			var ajax = Ajax.post("/ecard/svcRankDisc/list/get");
			var svcxRankDiscMap = {
					svcId : id,
					shopId : -1
			}
			ajax.data(svcxRankDiscMap);
		    ajax.done(function(result, jqXhr){
		    	if(result.type== "info"){
					var svcxRankDiscList = result.data;
					if(svcxRankDiscList){
						for(var i = 0; i < svcxRankDiscList.length; i++){
							$id("rate"+svcxRankDiscList[i].rank).html(svcxRankDiscList[i].rate*10);
						}
					}
					
				}else{
					Layer.msgWarning(result.message);
				}
		    });
		    ajax.always(function() {
				//隐藏等待提示框
			});
			ajax.go();			
		}
	</script>
</body>
<script type="text/html" id="userRateSetTpl" title="会员等级折扣模版">
	{{# for(var i = 0, len = d.length; i < len; i++){ }}
		<tr class="row">
			<td>{{ d[i].name || "" }}会员</td>
			<td><span id="rate{{ d[i].rank }}"></span> 折</td>
		</tr>
	{{# } }}
</script>
</html>