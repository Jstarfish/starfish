<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>结算记录</title>
</head>

<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding: 4px;">
	
		<div class="filter section">
			<div class="filter row">
				<!-- <button id="btnToAdd" class="button">添加</button> -->
				<div class="group right aligned">
					<label class="label">此页面还没做</label> 
					<label class="label">结算对象名称</label> <input id="queryPeerName" class="input" /> 
					<span class="spacer"></span>
					<!-- <label class="label">主题</label> <input id="querySubject" class="input" /> 
					<span class="spacer"></span>
					<label class="label">操作人员</label> <input id="queryOperatorName" class="input" /> 
					<span class="spacer"></span> -->
					<!-- 起止，结束时间、是否信息已确认 -->
					<button id="btnToQry" class="button">查询</button>
				</div>
			</div>
		</div>

	</div>
	
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	
	<div id="dialog" style="display: none;">
		<div id="addForm" class="form">
				
				<div class="field row">
					<label class="field label required">主题</label> 
					<input type="text" id="subject" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">结算对象</label> 
					<input type="text" id="faceVal" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">结算金额</label> 
					<input type="text" id="price" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">结算单据信息</label> 
					<input type="text" id="billExtra" class="field one half value wide" /> 
				</div>
				<div class="field row" id="addHide" style="display: none">
					<label class="field label" style="width: 62px">结算状态</label> 
					<select id="ecardStatus" class="field one half value wide">
						<option value="0">未确认</option>
						<option value="1">已确认</option>
					</select>
				</div>
				<div class="field row">
					<label class="field  label required">操作人员</label> 
					<input type="text" id="operatorName" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">结算时间</label> 
					<input type="text" id="ts" class="field one half value wide" /> 
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
				dlgTitle : "结算记录列表",
				//viewUrl : "/ecard/ecard/get/by/code",
				isUsePageCacheToView : true,
				jqGridGlobalSetting:{
					url : getAppUrl("/ecard/merch/settleCapitalRec/list/get"),
					colNames : [  "主题", "结算对象", "结算金额","结算单据信息","结算状态","结算时间","操作人员", "操作" ],
					colModel : [
							{
								name : "subject",
								index : "subject",
								width : 200,
								align : 'left'
							},
							{
								name : "peerName",
								index : "peerName",
								width : 100,
								align : 'center'
							},
							{
								name : "amount",
								index : "amount",
								width : 100,
								align : 'center'
							},
							{
								name : "billExtra",
								index : "billExtra",
								width : 100,
								align : 'center'
							},
							
							
							{
								name : "confirmed",
								index : "confirmed",
								width : 200,
								align : "left",
								formatter : function(cellValue,option, rowObject) {
									if (cellValue == 0) {
										return "未确认";
									}else{
										return "已确认";
									}
								}
							},
							{
								name : "ts",
								index : "ts",
								width : 100,
								align : 'center'
							},
							{
								name : "operatorName",
								index : "operatorName",
								width : 100,
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
					width : Math.min(600, $window.width()),
					height : Math.min(400, $window.height()),
					modal : true,
					open : false
				},
				queryParam : function(postData,formProxyQuery) {
					var peerName = formProxyQuery.getValue("queryPeerName");
					if (peerName != "") {
						postData['peerName'] = peerName;
					}
				},
				formProxyQueryInit : function(formProxyQuery) {
					// 注册查询表单控件
					formProxyQuery.addField({
						id : "queryPeerName",
						rules : [ "maxLength[30]" ]
					});
				}
			});
	
	//-----------------------------------------------------------------------------------}}
	
	</script>
</body>
</html>