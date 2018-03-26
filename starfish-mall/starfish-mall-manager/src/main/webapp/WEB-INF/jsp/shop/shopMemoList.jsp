<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>店铺备忘录管理</title>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned"><button id="btnToAdd" class="button">添加</button></div>
				<div class="group right aligned">
					<label class="label">关键字</label> <input id="keywords" class="input" maxlength="10"/> 
					<!-- <span class="spacer"></span> -->
					<button id="btnToQry" class="button">查询</button>
					<!-- <span class="vt divider"></span> -->
				</div>
			</div>
		</div>
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	
	<div id="dialog" style="display: none;">
		<div id="addForm" class="form">
			<div class="field row">
			<label class="field label required ">标题</label>
			<input type="text" id="title" name="title" class="field value three half wide" maxlength="30"/>
			</div>
			<div class="field row">
			<label class="field label required">内容<br/>(限250字)</label>
			<textarea id="content" name="content" class="field value three half wide" style="height:180px;" maxlength="250"></textarea>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js"></script>
	<script>
	$id("mainPanel").jgridDialog(
			{
				dlgTitle : "店铺备忘录",
				addUrl : "/shop/memo/add/do",
				updUrl : "/shop/memo/update/do",
				delUrl : "/shop/memo/delete/do",
				viewUrl : "/shop/memo/get",
				isUsePageCacheToView:false,
				jqGridGlobalSetting:{
					url : getAppUrl("/shop/memo/list/get"),
					colNames : [ "标题","内容","创建时间","操作" ],
					colModel : [
							{
								name : "title",
								index : "title",
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
								name : "createTime",
								index : "createTime",
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
				dlgGlobalConfig:{
					width : Math.min(550, $window.width()),
					height : Math.min(350, $window.height()),
					modal : true,
					open : false
				},
				modElementToggle : function(isShow) {
				},
				 addInit:function () {
					textSet("title", ""); 
					textSet("content", "");
				},
				 modAndViewInit:function (data) {
					textSet("title", data.title);
					textSet("content", data.content);
				},
				queryParam : function(postData,formProxyQuery) {
					var keywords = formProxyQuery.getValue("keywords");
					if (keywords!="") {
						postData['keywords'] = keywords;
					}
				},
				formProxyInit : function(formProxy) {
					// 注册表单控件
					formProxy.addField({
						id : "title",
						key : "title",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 若显示且为空，给予提示
								if ($id("title").is(":visible") && isNoB(rawValue)) {
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
				},
				formProxyQueryInit : function(formProxyQuery) {
					// 注册表单控件
					formProxyQuery.addField({
						id : "keywords",
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