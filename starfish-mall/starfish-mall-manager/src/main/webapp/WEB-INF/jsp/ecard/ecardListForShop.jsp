<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>用户e卡表</title>
</head>

<body id="rootPanel">
	
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group right aligned">
					<label class="label">用户名称</label> 
					<input id="queryUserId" class="input" /> 
					<span class="spacer"></span>
					<label class="label">类型</label> 
					<select id="queryCardType">
						<option value="-1">全部</option>
						<option value="eche">亿车卡</option>
						<option value="exiang">亿享卡</option>
						<option value="ezun">亿尊卡</option>
					</select> 
					<span class="spacer"></span>
					<label class="label">卡号</label> 
					<input id="queryCardNo" class="input" /> 
					<span class="spacer"></span>
					<label class="field label">状态</label> 
					<select id="queryStatus">
						<option value="-1">全部</option>
						<option value="1">使用中</option>
						<option value="2">已作废</option>
					</select>
					<span class="spacer"></span>
					<!-- 起止，结束时间、是否信息已确认 -->
					<button id="btnToQry" class="button">查询</button>
				</div>
			</div>
		</div>
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	
	<div id="dialog" style="display: none;">
		<div id="addForm" class="form">
				
				<div class="field row">
					<label class="field label required">用户名称</label> 
					<input type="text" id="userName" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">卡编码</label> 
					<input type="text" id="cardCode" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">卡编号</label> 
					<input type="text" id="cardNo" class="field one half value wide" /> 
				</div>
				<!-- <div class="field row">
					<label class="field  label">店铺ID</label> 
					<input type="text" id="shopId" class="field one half value wide" /> 
				</div> -->
				<!-- <div class="field row">
					<label class="field  label">店铺名称</label> 
					<input type="text" id="shopName" class="field one half value wide" /> 
				</div> -->
				<div class="field row">
					<label class="field  label required">面值</label> 
					<input type="text" id="faceValue" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">余额</label> 
					<input type="text" id="remainVal" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">购买时间</label> 
					<input type="text" id="buyTime" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field label">状态</label>
					<input type="text" id="invalid" class="field one half value wide" /> 
				</div>
				
			</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js"></script>
	<script>
	//-------------------------------------------自定义方法------------------------------{{
		//解绑 确认提示框
		function unbindConfirm(id,jqGridCtrl) {
			//
			var yesHandler = function(layerIndex) {
				theLayer.hide();
				unbind(id,jqGridCtrl);
			};
			var noHandler = function(layerIndex) {
				theLayer.hide();
			};
			//
			var theLayer = Layer.confirm("您确定要执行解绑吗？", yesHandler, noHandler);
		}
		//解绑
		function unbind(id,jqGridCtrl){
			if(id){
				//把用户E卡的值更新为null 
				var ajax = Ajax.post("/ecard/unbind/do");
				ajax.data({
					id : id
				});
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						//
						Layer.msgSuccess(result.message);
						jqGridCtrl.jqGrid("setGridParam", {
							page : 1
						}).trigger("reloadGrid");
					} else {
						Layer.warning(result.message);
					}
				});
				ajax.go();
			}
		}
		
		
	// -------------------------------------------------------------------------------------}}
	
	$id("mainPanel").jgridDialog(
			{
				dlgTitle : "用户e卡列表",
				isUsePageCacheToView : true,
				isShowViewShowModBtn: false,
				jqGridGlobalSetting:{
					url : getAppUrl("/ecard/list/get/-shop"),
					colNames : [  "用户ID", "卡类型", "卡号","面值","余额","购买时间","状态", "操作","" ],
					colModel : [
							{
								name : "userId",
								index : "userId",
								width : 100,
								align : 'left'
							},
							{
								name : "cardCode",
								index : "cardCode",
								width : 100,
								align : 'center',
								formatter : function(cellValue,option, rowObject) {
									if(cellValue == 'eche'){
										return "亿车卡";
									}else if(cellValue == 'ezun'){
										return "亿尊卡";
									}else if(cellValue == 'exiang'){
										return "亿享卡";
									}else{
										return "普通卡";
									}
								}
							},
							{
								name : "cardNo",
								index : "cardNo",
								width : 110,
								align : 'center'
							},
							{
								name : "faceValue",
								index : "faceValue",
								width : 100,
								align : 'center'
							},
							{
								name : "remainVal",
								index : "remainVal",
								width : 100,
								align : 'center'
							},
							{
								name : "buyTime",
								index : "buyTime",
								width : 150,
								align : 'center'
							},
							{
								name : "invalid",
								index : "invalid",
								width : 50,
								align : "center",
								formatter : function(cellValue,option, rowObject) {
									if(cellValue == 1){
										return "已作废";
									}else{
										return "使用中";
									}
								}
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									var s = " <span>[<a class='item dlgview' id='dlgview' href='javascript:void(0);' cellValue='" + cellValue + "' >查看</a>]"
									if(rowObject.shopId == null){
										return s;
									}else{
										return s + "&nbsp;&nbsp;&nbsp;[<a class='item unbind' href='javascript:void(0);' cellValue='" + cellValue + "' >解绑</a>]";
									}
									
								},
								width : 100,
								align : "center"
							},
							{
								name : "invalid",
								index : "invalid",
								hidden : true
							}]
				},
				// 修改和查看对话框
				modAndViewDlgConfig : {
					width : Math.min(400, $window.width()),
					height : Math.min(500, $window.height()),
					modal : true,
					open : false
				},
				modAndViewInit:function (data) {
					textSet("userId", data.userId);
					textSet("cardNo", data.cardNo);
					textSet("faceValue", data.faceValue);
					textSet("buyTime", data.buyTime);
					textSet("remainVal", data.remainVal);
					//textSet("shopId", data.shopId);
					//textSet("shopName", data.shopName);
					
					var cardCode = data.cardCode;
					if(cardCode == 'eche'){ 
						cardCode = "亿车卡";
					}else if(cardCode == 'ezun'){
						cardCode = "亿尊卡";
					}else if(cardCode == 'exiang'){
						cardCode = "亿享卡";
					}else{
						cardCode = "普通卡";
					}
					textSet("cardCode", cardCode);
					
					var invalid = data.invalid;
					if(invalid == 1){
						invalid = "已作废";
					}else{
						invalid = "使用中";
					}
					textSet("invalid", invalid);
				},
				queryParam : function(postData,formProxyQuery) {
					var cardNo = formProxyQuery.getValue("queryCardNo");
					if (cardNo != "") {
						postData['cardNo'] = cardNo;
					}
					var userName = formProxyQuery.getValue("queryUserId");
					if (userName != "") {
						postData['userName'] = userName;
					}
					/* var shopId = formProxyQuery.getValue("queryShopId");
					if (shopId != "") {
						postData['shopId'] = shopId;
					} */
					var cardCode = formProxyQuery.getValue("queryCardType");
					if (cardCode != -1) {
						postData['cardCode'] = cardCode;
					}
					var status = formProxyQuery.getValue("queryStatus");
					if (status == 2) {
						postData['invalid'] = true;
					}else {
						postData['invalid'] = false;
					}
				},
				formProxyQueryInit : function(formProxyQuery) {
					// 注册查询表单控件
					formProxyQuery.addField({
						id : "queryCardNo",
						rules : [ "maxLength[18]" ]
					});
					formProxyQuery.addField({
						id : "queryUserId",
						rules : [ "maxLength[11]" ]
					});
					/* formProxyQuery.addField({
						id : "queryShopId",
						rules : [ "maxLength[11]" ]
					}); */
					formProxyQuery.addField({
						id : "queryCardType"
					});
					formProxyQuery.addField({
						id : "queryStatus"
					});
				},
				pageLoad : function(jqGridCtrl) {
					$(document).on("click", ".unbind", function() {
						unbindConfirm($(this).attr("cellValue"),jqGridCtrl);
					});
				}
			});
	
	//-----------------------------------------------------------------------------------}}
	
	</script>
</body>
</html>