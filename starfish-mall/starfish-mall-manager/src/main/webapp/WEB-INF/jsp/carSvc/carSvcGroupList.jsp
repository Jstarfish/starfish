<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>车辆服务分组管理</title>

<style type="text/css">
</style>
</head>

<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnToAdd" class="button">添加</button>
				</div>
				<div class="group right aligned">
					<label class="label">服务分组</label> <input id="queryName"
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
	<div id="dialog" style="display: none;">
		<div id="addForm" class="form">
			<div class="field row">
				<label class="field label required">分组名称</label> <input type="text"
					id="name" name="name" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label required">所属分类</label> 
				<select class="field value  one half wide" id="kindId" name="kindId">
				<option value="" title="- 请选择 -">- 请选择 -</option>
				</select>
			</div>
			<div class="field row" style="height: 70px;">
				<label class="field label">分组描述</label>
				<textarea class="field value one half wide"
					style="height: 60px; width: 350px; resize: none;" name="desc"
					id="desc"></textarea>
			</div>
			<div id="isShowDefaulted" class="field row"
				style="width: 300px; display: none;">
				<label class="field label required">是否启用</label>
				<div class="field group">
					<input type="radio" id="defaulted-Y" name="disabled" value="false"
						checked="checked"> <label for="defaulted-Y">启用</label> <input
						type="radio" id="defaulted-N" name="disabled" value="true">
					<label for="defaulted-N">禁用</label>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript"
		src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js"></script>
	<script>
		//------------------------------------------初始化配置---------------------------------{{

		var oldName = "";// 保留原名称
		var oldKindId = ""; //保留原kindId;
		var isExist = false;// 是否存在
		function isExistByName(name,kindId) {
			if (name == oldName && oldKindId == kindId) {// 验证是否跟原名称一样，如果一样则跳过验证
				isExist = false;
			} else {
				var ajax = Ajax.post("/carSvc/svcGroup/exist/by/name");
				ajax.data({
					name : name,
					kindId : kindId
				});
				ajax.sync();
				ajax.done(function(result, jqXhr) {
					isExist = result.data;
				});
				ajax.go();
			}
		}
		function loadGroup(selectedId, kindId) {
			if (typeof (kindId) != "undefined" && kindId != null) {
				$id(selectedId).val(kindId);
			}
		}
		function loadCarSvcKind() {
			// 隐藏页面区
			//hideTown();
			var ajax = Ajax.post("/carSvc/svcKind/selectList/get");
			ajax.params({});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("kindId", result.data);
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		$id("mainPanel")
				.jgridDialog(
						{
							dlgTitle : "车辆服务分组",
							//listUrl : "/goods/carSvcGroup/list/get",
							addUrl : "/carSvc/svcGroup/add/do",
							updUrl : "/carSvc/svcGroup/update/do",
							delUrl : "/carSvc/svcGroup/delete/do",
							rootPanelSetting : {
								north__size : 60
							},
							//viewUrl : "/basis/faqCat/get",
							//isUsePageCacheToView : true,
							//theGridPagerId : "",
							jqGridGlobalSetting : {
								pager : "",
								rowNum : -1,
								url : getAppUrl("/carSvc/svcGroup/list/get"),
								colNames : [ "分组名称", "分组描述", "所属分类 ", "是否启用 ", "操作" ],
								colModel : [
										{
											name : "name",
											index : "name",
											width : 150,
											align : 'left'
										},
										{
											name : "desc",
											index : "desc",
											width : 500,
											align : 'left'
										},
										{
											name : "kindName",
											index : "kindName",
											width : 80,
											align : 'left'
										},
										{
											name : "disabled",
											index : "disabled",
											width : 50,
											align : "left",
											formatter : function(cellValue) {
												return cellValue == true ? '禁用'
														: '启用';
											}
										},
										{
											name : 'id',
											index : 'id',
											formatter : function(cellValue,
													option, rowObject) {
												var s = "[<a class='item dlgupd' href='javascript:void(0);' cellValue='"
														+ cellValue
														+ "' >修改</a>]"
														+ "&nbsp;&nbsp;&nbsp;[<a class='item dlgdel' href='javascript:void(0);' cellValue='"
														+ cellValue
														+ "' >删除</a>]</span>  ";
												return s;
											},
											width : 120,
											align : "center"
										} ]
							},
							// 增加对话框
							addDlgConfig : {
								width : Math.min(500, $window.width()),
								height : Math.min(300, $window.height()),
								modal : true,
								open : false
							},
							// 修改和查看对话框
							modAndViewDlgConfig : {
								width : Math.min(500, $window.width()),
								height : Math.min(300, $window.height()),
								modal : true,
								open : false
							},

							/**
							 * 用于修改时显示隐藏元素
							 */
							modElementToggle : function(isShow) {
								if (isShow) {
									$id("isShowDefaulted").show();
								} else {
									$id("isShowDefaulted").hide();
								}
							},
							viewElementToggle : function(isShow) {
								if (isShow) {
									$id("isShowDefaulted").show();
								} else {
									$id("isShowDefaulted").hide();
								}
							},
							//jqGridSetting:function(jqGridCtrl){
							//	jqGridCtrl.gridList.jqGrid({rowNum:-1,});
							//},
							/**
							 * 增加时清空控件的值
							 */
							addInit : function() {
								textSet("name", "");
								textSet("desc", "");
								oldName = "";
								oldKindId = "";
							},

							/**
							 * 修改和查看是给控件赋值
							 */
							modAndViewInit : function(data) {
								textSet("name", data.name);
								textSet("desc", data.desc);
								loadGroup("kindId", data.kindId);
								if (data.disabled == true) {
									$id("defaulted-N").attr("checked", true);
								} else {
									$id("defaulted-Y").attr("checked", true);
								}
								//textSet("seqNo", data.seqNo);
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
							 * 注册增加和修改的验证表单控件
							 */
							formProxyInit : function(formProxy) {
								//注册表单控件
								formProxy.addField({
											id : "name",
											key : "name",
											required : true,
											rules : [{rule : function(idOrName, type,rawValue,curData) {
													// 若显示且为空，给予提示
													if ($id("name").is(":visible")&& isNoB(rawValue)) {
														return false;
													}
													var kindId = $id("kindId").val();
													isExistByName(rawValue,kindId);
													return !isExist;
													},
												message : "名称已存在!"
											}, "maxLength[30]" ]
										});
								formProxy.addField({
											id : "desc",
											key : "desc",
											rules : [
													{
														rule : function(
																idOrName, type,
																rawValue,
																curData) {
															// 若显示且为空，给予提示
															if ($id("catId")
																	.is(
																			":visible")
																	&& isNoB(rawValue)) {
																return false;
															}
															return true;
														},
														message : "此为必选！"
													}, "maxLength[100]" ]
										});
								formProxy.addField({
									id : "disabled",
									key : "disabled"
								});
								formProxy.addField({
									id : "kindId",
									key : "kindId",
									rules : [ {
										rule : function(idOrName, type, rawValue, curData) {
											// 若显示且为空，给予提示
											if ($id("kindId").is(":visible") && isNoB(rawValue)) {
												return false;
											}
											return true;
										},
										message : "此为必填选！"
									} ]
								});
							},
							/**
							 * 注册查询验证表单控件
							 */
							formProxyQueryInit : function(formProxyQuery) {
								// 注册表单控件
								formProxyQuery.addField({
									id : "queryName",
									rules : [ "maxLength[30]" ]
								});
							},
							saveOldData : function(data) {
								oldName = data.name;// 保存原来名称，用于检测是否存在
								oldKindId = data.kindId; // 保存原来种类，用于检测是否存在
							},
							/**
							 * 获得删除确认提示
							 */
							getDelComfirmTip : function(data) {
								var theSubject = data.name;
								return '确定要删除"' + theSubject
										+ '"分组吗？';
							},
							pageLoad : function() {
								loadCarSvcKind();
							}
						});
	</script>
</body>
</html>