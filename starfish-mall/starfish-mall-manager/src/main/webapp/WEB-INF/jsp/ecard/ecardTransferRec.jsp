<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>e卡转赠记录</title>
</head>

<body id="rootPanel">
	
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group right aligned">
					<label class="label">卡编号</label> 
					<input id="queryCardId" class="input" /> 
					<span class="spacer"></span>
					<button id="btnToQry" class="button">查询</button>
				</div>
			</div>
		</div>
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	
	<div id="dialog" style="display: none;">
		<div id="addForm" class="form">
				
				<!-- <div class="field row">
					<label class="field  label required one half wide">卡ID</label> 
					<input type="text" id="cardId" class="field one half value wide" /> 
				</div> -->
				<div class="field row">
					<label class="field  label required one half wide">卡编号</label> 
					<input type="text" id="cardNo" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required one half wide">卡类型</label> 
					<input type="text" id="cardCode" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required one half wide">卡余额</label> 
					<input type="text" id="remainVal" class="field one half value wide" /> 
				</div>
				<!-- <div class="field row">
					<label class="field  label required one half wide">转出用户ID</label> 
					<input type="text" id="userIdFrom" class="field one half value wide" /> 
				</div> -->
				<div class="field row">
					<label class="field  label required one half wide">转出用户名称</label> 
					<input type="text" id="userNameFrom" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required one half wide">转出用户手机号</label> 
					<input type="text" id="userPhoneFrom" class="field one half value wide" /> 
				</div>
				<!-- <div class="field row">
					<label class="field  label required one half wide">受赠用户ID</label> 
					<input type="text" id="userIdTo" class="field one half value wide" /> 
				</div> -->
				<div class="field row">
					<label class="field  label required one half wide">受赠用户名称</label> 
					<input type="text" id="userNameTo" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required one half wide">受赠用户手机号</label> 
					<input type="text" id="userPhoneTo" class="field one half value wide" /> 
				</div>
				
			</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js"></script>
	<script>
	//-------------------------------------------自定义方法------------------------------{{
	
	// -------------------------------------------------------------------------------------}}
	
	$id("mainPanel").jgridDialog(
			{
				dlgTitle : "e卡转赠记录",
				isUsePageCacheToView : true,
				isShowViewShowModBtn: false,
				jqGridGlobalSetting:{
					url : getAppUrl("/ecard/transfer/rec/list/get"),
					colNames : [ "卡编号","余额", "转出用户名称","转出用户手机号","受赠用户名称","受赠用户手机号", "操作" ],
					colModel : [
							/* {
								name : "cardId",
								index : "cardId",
								width : 100,
								align : 'center'
							}, */
							{
								name : "cardNo",
								index : "cardNo",
								width : 150,
								align : 'center'
							},
							{
								name : "remainVal",
								index : "remainVal",
								width : 100,
								align : 'center'
							},
							{
								name : "userNameFrom",
								index : "userNameFrom",
								width : 150,
								align : 'left'
							},
							{
								name : "userPhoneFrom",
								index : "userPhoneFrom",
								width : 150,
								align : 'center'
							},
							{
								name : "userNameTo",
								index : "userNameTo",
								width : 150,
								align : 'center'
							},
							{
								name : "userPhoneTo",
								index : "userPhoneTo",
								width : 150,
								align : 'center'
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									var s = " <span>[<a class='item dlgview' id='dlgview' href='javascript:void(0);' cellValue='" + cellValue + "' >查看</a>]"
									return s;
								},
								width : 200,
								align : "center"
							} ]
				},
				// 修改和查看对话框
				modAndViewDlgConfig : {
					width : Math.min(400, $window.width()),
					height : Math.min(400, $window.height()),
					modal : true,
					open : false
				},
				modAndViewInit:function (data) {
					//textSet("cardId", data.cardId);
					textSet("cardNo", data.cardNo);
					//textSet("userIdFrom", data.userIdFrom);
					textSet("userNameFrom", data.userNameFrom);
					textSet("userPhoneFrom", data.userPhoneFrom);
					//textSet("userIdTo", data.userIdTo);
					textSet("userNameTo", data.userNameTo);
					textSet("userPhoneTo", data.userPhoneTo);
					textSet("remainVal", data.remainVal);
					
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
					textSet("cardCode", cardCode)
					
				},
				queryParam : function(postData,formProxyQuery) {
					var cardNo = formProxyQuery.getValue("queryCardId");
					if (cardNo != "") {
						postData['cardNo'] = cardNo;
					}
				},
				formProxyQueryInit : function(formProxyQuery) {
					// 注册查询表单控件
					formProxyQuery.addField({
						id : "queryCardId",
						rules : [ "maxLength[30]" ]
					});
				}
			});
	
	//-----------------------------------------------------------------------------------}}
	
	</script>
</body>
</html>