<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>管理</title>
</head>

<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding: 4px;">
	<div class="filter section">
		<div class="filter row">
			<label class="label">关键字</label> <input id="queryKeyword" class="input" /> 
			<span class="spacer"></span>
			<button id="btnToQry" class="button">查询</button>
		</div>
		<span class="hr divider"></span>
		<div class="filter row">
			<div class="group left aligned">
				<button id="btnToAdd" class="button">添加</button>
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
			<label class="field label required ">发表者ID</label>
			<input type="text" id="userId" name="userId" class="field value one half wide" />
			
			
			<label class="field label required inline one half wide">冗余字段</label>
			<input type="text" id="userName" name="userName" class="field value one half wide" />
			</div>
			<div class="field row">
			<label class="field label required ">userType => 1: 会员, 2: 商户, 3:代理, 4 : 平台（商城）</label>
			<input type="text" id="userType" name="userType" class="field value one half wide" />
			
			
			<label class="field label required inline one half wide">priv.starfish.mall.comn.dict.AppType</label>
			<input type="text" id="appType" name="appType" class="field value one half wide" />
			</div>
			<div class="field row">
			<label class="field label required ">mobapp : 手机App, webapp web站点</label>
			<input type="text" id="subject" name="subject" class="field value one half wide" />
			
			
			<label class="field label required inline one half wide">反馈内容</label>
			<input type="text" id="content" name="content" class="field value one half wide" />
			</div>
			<div class="field row">
			<label class="field label required ">创建/发送时间</label>
			<input type="text" id="sendTime" name="sendTime" class="field value one half wide" />
			
			
			<label class="field label required inline one half wide">阅读/接收时间</label>
			<input type="text" id="recvTime" name="recvTime" class="field value one half wide" />
			</div>
			<div class="field row">
			<label class="field label required ">处理标记 0：初始（未处理），-1：不予处理，1：已处理</label>
			<input type="text" id="handleFlag" name="handleFlag" class="field value one half wide" />
			
			
			<label class="field label required inline one half wide">处理备忘</label>
			<input type="text" id="handleMemo" name="handleMemo" class="field value one half wide" />
			</div>
			<div class="field row">
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=appBaseUrl%>/static/js/jqgrid.dialog.js"></script>
	<script>
	$id("mainPanel").jgridDialog(
			{
				dlgTitle : "",
				viewUrl : "/social/userFeedback/by/id/get",
				isUsePageCacheToView:false,
				jqGridGlobalSetting:{
					url : getAppUrl("/social/userFeedback/list/get"),
					colNames : [ "发表者ID","用户名称","用户类型","priv.starfish.mall.comn.dict.AppType","mobapp : 手机App, webapp web站点","反馈内容","创建/发送时间","阅读/接收时间","处理标记 0：初始（未处理），-1：不予处理，1：已处理","处理备忘","发表时间","操作" ],
					colModel : [
							{
								name : "userId",
								index : "userId",
								width : 200,
								align : 'center'
							},
							{
								name : "userName",
								index : "userName",
								width : 200,
								align : 'center'
							},
							{
								name : "userType",
								index : "userType",
								width : 200,
								align : 'center'
							},
							{
								name : "appType",
								index : "appType",
								width : 200,
								align : 'center'
							},
							{
								name : "subject",
								index : "subject",
								width : 200,
								align : 'center'
							},
							{
								name : "content",
								index : "content",
								width : 200,
								align : 'center'
							},
							{
								name : "sendTime",
								index : "sendTime",
								width : 200,
								align : 'center'
							},
							{
								name : "recvTime",
								index : "recvTime",
								width : 200,
								align : 'center'
							},
							{
								name : "handleFlag",
								index : "handleFlag",
								width : 200,
								align : 'center'
							},
							{
								name : "handleMemo",
								index : "handleMemo",
								width : 200,
								align : 'center'
							},
							{
								name : "ts",
								index : "ts",
								width : 200,
								align : 'center'
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									var s = "<span>[<a class='item dlgview' href='javascript:void(0);' cellValue='" + cellValue + "'>查看</a>]&nbsp;&nbsp;&nbsp;" + "<span>[<a class='item dlgupd' href='javascript:void(0);' cellValue='" + cellValue + "' >修改</a>]" + "&nbsp;&nbsp;&nbsp;[<a class='item dlgdel' href='javascript:void(0);' cellValue='" + cellValue + "' >删除</a>]</span>  ";
									return s;
								},
								width : 200,
								align : "center"
							} ]
				},
				// 增加对话框
				addDlgConfig : {
					width : Math.min(400, $window.width()),
					height : Math.min(160, $window.height()),
					modal : true,
					open : false
				},
				// 修改和查看对话框
				modAndViewDlgConfig : {
					width : Math.min(600, $window.width()),
					height : Math.min(160, $window.height()),
					modal : true,
					open : false
				},
				modElementToggle : function(isShow) {
				},
				 addInit:function () {
					intSet("userId", null);
					textSet("userName", "");
					intSet("userType", null);
					textSet("appType", "");
					textSet("subject", "");
					textSet("content", "");
					dateSet("sendTime", null);
					dateSet("recvTime", null);
					intSet("handleFlag", null);
					textSet("handleMemo", "");
				},
				 modAndViewInit:function (data) {
					intSet("userId", data.userId);
					textSet("userName", data.userName);
					intSet("userType", data.userType);
					textSet("appType", data.appType);
					textSet("subject", data.subject);
					textSet("content", data.content);
					dateSet("sendTime", data.sendTime);
					dateSet("recvTime", data.recvTime);
					intSet("handleFlag", data.handleFlag);
					textSet("handleMemo", data.handleMemo);
				},
				queryParam : function(postData,formProxyQuery) {
					var keyword = formProxyQuery.getValue("queryKeyword");
					if (keyword != "") {
						postData['keyword'] = keyword;
					}
				},
				formProxyInit : function(formProxy) {
					// 注册表单控件
					formProxy.addField({
						id : "userId",
						key : "userId",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 若显示且为空，给予提示
								if ($id("userId").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "此为必填项！"
						} ]
					});
					// 注册表单控件
					formProxy.addField({
						id : "userName",
						key : "userName",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 若显示且为空，给予提示
								if ($id("userName").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "此为必填项！"
						} ]
					});
					// 注册表单控件
					formProxy.addField({
						id : "userType",
						key : "userType",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 若显示且为空，给予提示
								if ($id("userType").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "此为必填项！"
						} ]
					});
					// 注册表单控件
					formProxy.addField({
						id : "appType",
						key : "appType",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 若显示且为空，给予提示
								if ($id("appType").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "此为必填项！"
						} ]
					});
					// 注册表单控件
					formProxy.addField({
						id : "subject",
						key : "subject",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 若显示且为空，给予提示
								if ($id("subject").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "此为必填项！"
						} ]
					});
					// 注册表单控件
					formProxy.addField({
						id : "content",
						key : "content",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 若显示且为空，给予提示
								if ($id("content").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "此为必填项！"
						} ]
					});
					// 注册表单控件
					formProxy.addField({
						id : "sendTime",
						key : "sendTime",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 若显示且为空，给予提示
								if ($id("sendTime").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "此为必填项！"
						} ]
					});
					// 注册表单控件
					formProxy.addField({
						id : "recvTime",
						key : "recvTime",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 若显示且为空，给予提示
								if ($id("recvTime").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "此为必填项！"
						} ]
					});
					// 注册表单控件
					formProxy.addField({
						id : "handleFlag",
						key : "handleFlag",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 若显示且为空，给予提示
								if ($id("handleFlag").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "此为必填项！"
						} ]
					});
					// 注册表单控件
					formProxy.addField({
						id : "handleMemo",
						key : "handleMemo",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 若显示且为空，给予提示
								if ($id("handleMemo").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "此为必填项！"
						} ]
					});
				},
				formProxyQueryInit : function(formProxyQuery) {
					// 注册表单控件
					formProxyQuery.addField({
						id : "queryKeyword",
						rules : [ "maxLength[30]" ]
					});
				},
				saveOldData : function(data) {
				},
				getDelComfirmTip : function(data) {
					return '确定要删除此条记录吗？';
				},
				pageLoad : function() {
				}
			});
	//-----------------------------------------------------------------------------------}}
	</script>
</body>
</html>