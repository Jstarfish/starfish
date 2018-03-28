<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>e卡交易记录</title>
</head>

<body id="rootPanel">
	
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<!-- <button id="btnToAdd" class="button">添加</button> -->
				<div class="group right aligned">
					<label class="label">卡编号</label> 
					<input id="queryCardId" class="input" /> 
					<span class="spacer"></span>
					<label class="field label">状态</label> 
					<select id="queryStatus">
						<option value="0">全部</option>
						<option value="1">支出</option>
						<option value="2">转入</option>
					</select>
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
				
				<div class="field row">
					<label class="field label required">交易说明</label> 
					<input type="text" id="subject" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">卡编号</label> 
					<input type="text" id="cardNo" class="field one half value wide" /> 
				</div>
				<!-- <div class="field row">
					<label class="field  label required">关联ID</label> 
					<input type="text" id="targetId" class="field one half value wide" /> 
				</div> -->
				<div class="field row">
					<label class="field  label required">关联事项</label> 
					<input type="text" id="targetStr" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">交易金额</label> 
					<input type="text" id="theVal" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">余额</label> 
					<input type="text" id="remainVal" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">交易时间</label> 
					<input type="text" id="theTime" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field label">交易类型</label> 
					<select id="directFlag" class="field one half value wide">
						<option value="0">全部</option>
						<option value="1">支出</option>
						<option value="2">转入</option>
					</select>
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
				dlgTitle : "e卡交易记录",
				//viewUrl : "/ecard/ecard/get/by/code",
				isUsePageCacheToView : true,
				isShowViewShowModBtn: false,
				jqGridGlobalSetting:{
					url : getAppUrl("/ecard/transact/rec/list/get"),
					colNames : [  "卡编号", "交易说明","交易类型","交易金额","交易时间", "操作" ],
					colModel : [
							{
								name : "userECard.cardNo",
								index : "userECard.cardNo",
								width : 100,
								align : 'left'
							},
							{
								name : "subject",
								index : "subject",
								width : 100,
								align : 'center',
								formatter : function(cellValue,option, rowObject) {
									if (cellValue == "orderpay") {
										return "订单支付";
									}else if(cellValue == "orderRefund"){
										return "E卡退款";
									}else{
										return "E卡受赠";
									}
								}
							},
							{
								name : "directFlag",
								index : "directFlag",
								width : 50,
								align : "left",
								formatter : function(cellValue,option, rowObject) {
									if (cellValue == 1) {
										return "转入";
									}else{
										return "转出";
									}
								}
							},
							{
								name : "theVal",
								index : "theVal",
								width : 100,
								align : 'center',
								formatter : function(cellValue,option, rowObject) {
									if (rowObject.directFlag == 1) {
										return "+ " + cellValue;
									}else{
										return "- " + cellValue;
									}
								}
							},
							{
								name : "theTime",
								index : "theTime",
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
					height : Math.min(500, $window.height()),
					modal : true,
					open : false
				},
				modAndViewInit:function (data) {
					//textSet("cardId", data.cardId);
					textSet("cardNo", data.userECard.cardNo);
					textSet("targetId", data.targetId);
					textSet("targetStr", data.targetStr);
					textSet("theVal", data.theVal);
					textSet("remainVal", data.remainVal);
					textSet("theTime", data.theTime);
					
					var subject = data.subject;
					if(subject == 'orderpay'){ 
						subject = "订单支付";
					}else if(subject == 'orderRefund'){
						subject = "E卡退款";
					}else{
						subject = "E卡受赠";
					}
					textSet("subject", subject);
					
					var directFlag = data.directFlag;
					if(directFlag == 1){ 
						directFlag = "转入";
					}else{
						directFlag = "转出";
					}
					textSet("directFlag", directFlag);
					
				},
				queryParam : function(postData,formProxyQuery) {
					var cardNo = formProxyQuery.getValue("queryCardId");
					if (cardNo != "") {
						postData['cardNo'] = cardNo;
					}
					var status = formProxyQuery.getValue("queryStatus");
					if (status == 1) {
						postData['directFlag'] = 1;
					}else{
						postData['directFlag'] = -1;
					}
				},
				formProxyQueryInit : function(formProxyQuery) {
					// 注册查询表单控件
					formProxyQuery.addField({
						id : "queryCardId",
						rules : [ "maxLength[30]" ]
					});
					formProxyQuery.addField({
						id : "queryStatus"
					});
				}
			});
	
	//-----------------------------------------------------------------------------------}}
	
	</script>
</body>
</html>