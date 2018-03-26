<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>车辆服务管理</title>

<style type="text/css">
</style>
</head>
<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<label class="label">服务名称</label> <input id="queryName"
						class="input" /> <span class="spacer"></span>
					<button id="btnToQry" class="button">查询</button>
				</div>
			</div>
		</div>
	</div>
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js"></script>
	<script>
		//------------------------------------------初始化配置---------------------------------{{
			
		var carSvcGridHelper = JqGridHelper.newOne("");
		function loadRadio(disabled) {
			if(typeof (groupId) != "undefined" && groupId != null){
				
			}
		}
		function loadGroup(selectedId, groupId) {
			if (typeof (groupId) != "undefined" && groupId != null) {
				$id(selectedId).val(groupId);
			}
		}
		
		//回调
		function getSelectedTargetInfo(){
			var carSvcId = radioGet("validCarSvcId");
			var json = carSvcGridHelper.getRowData(carSvcId);
			json["targetType"]="3";
			json["targetName"]="服务";
			return json;
		}
		$id("mainPanel").jgridDialog(
				{
					dlgTitle : "车辆服务",
					//listUrl : "/goods/carSvc/list/get",
					rootPanelSetting:{
						north__size : 60
					},
				jqGridGlobalSetting : {
					url : getAppUrl("/carSvc/list/get"),
					colNames : ["选择商品","服务名称", "服务描述","附加价(元)","所属分组","使用范围", "是否启用 "],
					colModel : [
							{name:"id",index:'id',width:50,align : 'center',formatter : function (cellValue) {
								 return "<input type='radio' id='validType"+cellValue+"' name='validCarSvcId' value='"+cellValue+"'>";
							}},
							{
								name : "name",
								index : "name",
								width : 100,
								align : 'left'
							},
							{
								name : "desc",
								index : "desc",
								width : 300,
								align : 'left'
							},
							{
								name : "salePrice",
								index : "salePrice",
								width : 50,
								align : 'left'
							},
							{
								name : "carSvcGroup.name",
								index : "carSvcGroup.name",
								width : 100,
								align : 'center'
							},
							{
								name : "appRange",
								index : "appRange",
								width : 150,
								align : 'center'
							},
							{
								name : "disabled",
								index : "disabled",
								width : 50,
								align : "left",
								formatter : function(cellValue) {
									return cellValue == true ? '禁用' : '启用';
								}
							}],
							loadComplete : function(gridData){
								carSvcGridHelper.cacheData(gridData);
								var callback = getCallbackAfterGridLoaded();
								if(isFunction(callback)){
									callback();
								}

							},
							onCellSelect: function(rowId) {
								$id("validType"+rowId).attr("checked",'checked');
							}
				},
					/**
					 * 封装查询参数
					 */
					queryParam : function(querJson, formProxyQuery) {
						var name = formProxyQuery.getValue("queryName");
						if (name != "") {
							querJson['name'] = name;
						}
					},

					/**
					 * 注册查询验证表单控件
					 * @param formProxyQuery
					 */
					formProxyQueryInit : function(formProxyQuery) {
						// 注册表单控件
						formProxyQuery.addField({
							id : "queryName",
							rules : [ "maxLength[30]" ]
						});
					},
					pageLoad : function() {
					}
				});
		function getCallbackAfterGridLoaded(){}
	</script>
</body>
</html>