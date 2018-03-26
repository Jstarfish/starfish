<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>常见问题分类管理</title>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned"><button id="btnToAdd" class="button">添加</button></div>
				<div class="group right aligned">
					<label class="label">分类名称</label> <input id="queryName" class="input" maxlength="10"/> 
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
				<label class="field label required">分类名称</label> 
				<input type="text" id="name" name="name" class="field value one half wide"  maxlength="10"/>
				<label class="field inline label required one half wide" id="seqNoTxt" style="display:none">排序号</label> 
				<input type="text" id="seqNo" name="seqNo"  value="" class="field value one half wide" style="width:70px;display:none"/>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js"></script>
	<script>
	//-------------------------------------------自定义方法------------------------------{{
	var oldName = "";// 保留原名称
	var isExist = false;// 是否存在
	/**
	 * 检测是否存在相同的分类名称
	 * 
	 * @author 邓华锋
	 * @date 2015年10月9日
	 * 
	 * @param name
	 */
	function isExistByName(name) {
		if (name == oldName) {// 验证是否跟原名称一样，如果一样则跳过验证
			isExist = false;
		} else {
			var ajax = Ajax.post("/comn/faqCat/exist/by/name");
			ajax.data({
				name : name
			});
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				isExist = result.data;
			});
			ajax.go();
		}
	}
	// -------------------------------------------------------------------------------------}}
	$id("mainPanel").jgridDialog(
			{
				dlgTitle : "常见问题分类",
				addUrl : "/comn/faqCat/add/do",
				updUrl : "/comn/faqCat/update/do",
				delUrl : "/comn/faqCat/delete/do",
				jqGridGlobalSetting:{
					url : getAppUrl("/comn/faqCat/list/get"),
					colNames : [  "分类名称", "排序号", "更新时间 ", " 操作" ],
					colModel : [
							{
								name : "name",
								index : "name",
								width : 200,
								align : 'left'
							},
							{
								name : "seqNo",
								index : "seqNo",
								width : 100,
								align : 'center'
							},
							{
								name : "ts",
								index : "ts",
								width : 200,
								align : "left"
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									var s = "<span>[<a class='item dlgupd' id='dlgupd' href='javascript:void(0);' cellValue='" + cellValue + "' >修改</a>]" + "&nbsp;&nbsp;&nbsp;[<a class='item dlgdel' href='javascript:void(0);' cellValue='"
											+ cellValue + "'>删除</a>]</span>  ";
									return s;
								},
								align : "center"
							} ]
				},
				jqGridSetting:function(jqGridCtrl){
					$("#update").click(function(){
						jqGridCtrl.jqGrid("setGridParam", {
							postData : {
								filterStr : JSON.encode(postData, true)
							},
							page : 1
						}).trigger("reloadGrid");
					});
				},
				addDlgConfig : {
					width : Math.min(400, $window.width()),
					height : Math.min(160, $window.height()),
					modal : true,
					open : false
				},
				modAndViewDlgConfig : {
					width : Math.min(600, $window.width()),
					height : Math.min(160, $window.height()),
					modal : true,
					open : false
				},
				modElementToggle : function(isShow) {
					if (isShow) {
						$id("seqNo").show();
						$id("seqNoTxt").show();
					} else {
						$id("seqNo").hide();
						$id("seqNoTxt").hide();
					}
				},
				 addInit:function () {
					textSet("name", "");
					textSet("seqNo", "");
				},
				 modAndViewInit:function (data) {
					textSet("name", data.name);
					textSet("seqNo", data.seqNo);
				},
				queryParam : function(postData,formProxyQuery) {
					var name = formProxyQuery.getValue("queryName");
					if (name != "") {
						postData['name'] = name;
					}
				},
				
				formProxyInit : function(formProxy) {
					formProxy.addField({
						id : "name",
						key : "name",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								message="名称已存在";
								// 若显示且为空，给予提示
								if ($id("name").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "名称不能为空"
						},{
							rule : function(idOrName, type, rawValue, curData) {
								isExistByName(rawValue);
								return !isExist;
							},
							message : "分类名称已存在"
						} ]
					});
					
					formProxy.addField({
						id : "seqNo",
						key : "seqNo",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 区若显示且为空，给予提示
								if ($id("seqNo").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "此为必填项！"
						} ]
					});
				},
				formProxyQueryInit : function(formProxyQuery) {
					formProxyQuery.addField({
						id : "queryName",
						rules : [ "maxLength[30]" ]
					});
				},
				saveOldData : function(data) {
					oldName = data.name;// 保存原来名称，用于检测是否存在
				},
				getDelComfirmTip : function(data) {
					var theSubject = data.name;
					return '确定要删除"' + theSubject + '"分类（属于它的分组和常用问题将会一起删除）吗？';
				}
			});
	//-----------------------------------------------------------------------------------}}
	</script>
</body>
</html>