<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>车辆服务套餐管理</title>

<style type="text/css">
table.gridtable {
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-collapse: collapse;
	width: 600px;
}

table.gridtable th {
	border: 1px solid #AAA;
	padding: 3px;
	background-color: #dedede;
	height: 24px;
	line-height: 22px;
}

table.gridtable td {
	border: 1px solid #AAA;
	padding: 3px;
	background-color: #ffffff;
	height: 24px;
	line-height: 22px;
}
</style>
</head>
<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<label class="label">套餐名称</label> <input id="queryName"
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
				<label class="field label one half wide required">所属分类</label> <select
					class="field value  one half wide" id="kindId" name="kindId">
					<option value="" title="- 请选择 -">- 请选择 -</option>
				</select>
			</div>
			<div class="field row">
				<label class="field label one half wide required">套餐名称</label> <input
					type="text" id="name" name="name" class="field value one half wide" />
					<input type="hidden" id="packId" name="packId" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label one half wide required">有效时间</label> <input
					type="text" id="startTime" class="field value" />至 <input
					type="text" id="endTime" class="field value" />
			</div>
			<div class="field row" style="height: 70px;">
				<label class="field label one half wide">套餐描述</label>
				<textarea class="field value one half wide"
					style="height: 60px; width: 350px; resize: none;" name="desc"
					id="desc"></textarea>
				<span class="normal spacer"></span>
			</div>
			<div class="field row" style="height: 70px;">
				<label class="field label one half wide">套餐备注</label>
				<textarea class="field value one half wide"
					style="height: 60px; width: 350px; resize: none;" name="memo"
					id="memo"></textarea>
				<span class="normal spacer"></span>
			</div>
			<div class="field row" style="height: 60px">
				<input type="hidden" id="imageUuid" name="imageUuid" /> <input
					type="hidden" id="imageUsage" name="imageUsage" /> <input
					type="hidden" id="imagePath" name="imagePath" /> <label
					class="field label one half wide">套餐图片</label> <img
					id="logoSvcPack" height="40px" width="100px" />
			</div>
			<div class="ui-layout-center form">
				<div class="field row"
					style="height: auto; line-height: auto; width: 100%;">
					<table data-role="staff_tab" class="gridtable" style="width: 100%;">
						<thead>
							<tr>
								<th width="25%">服务名称</th>
								<th width="25%">销售价格</th>
								<th width="25%">折扣率</th>
								<th width="25%">操作</th>
							</tr>
						</thead>
						<tbody id="packItem_tbd">
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript"
		src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js"></script>
	<script>
		//------------------------------------------初始化配置---------------------------------
		function loadSvcKindId(selectedId, kindId) {
			if (typeof (kindId) != "undefined" && kindId != null) {
				$id(selectedId).val(kindId);
			}
		}
		function loadSvcKind() {
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
		var filter = {};
		filter.disabled = false;
		//-----------------------------------------------图片上传--------------------------------------------------
		$id("mainPanel")
				.jgridDialog(
						{
							dlgTitle : "车辆服务套餐",
							//listUrl : "/goods/carSvc/list/get",
							isShowViewShowModBtn :false,
							rootPanelSetting : {
								north__size : 60
							},
							jqGridGlobalSetting : {
								url : getAppUrl("/carSvc/pack/list/get"),
								postData:{filterStr : JSON.encode(filter,true)},
								colNames : [ "套餐名称", "套餐描述", "上下架 ", " 操作" ],
								colModel : [
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
											name : "disabled",
											index : "disabled",
											width : 50,
											align : "left",
											formatter : function(cellValue) {
												return cellValue == true ? '<span style="color:red;">未上架</span>'
														: '<span style="color:green;">已上架</span>';
											}
										},
										{
											name : 'id',
											index : 'id',
											formatter : function(cellValue,option, rowObject) {
												var tempItem = String.builder();
												tempItem.append("[<a class='item dlgview' href='javascript:void(0);' cellValue='"+ cellValue+ "' >查看</a>]");
												return tempItem.value;
											},
											width : 150,
											align : "center"
										} ]
							},
							// 增加对话框
							addDlgConfig : {
								width : Math.min(600, $window.width()),
								height : Math.min(550, $window.height()),
								modal : true,
								open : false
							},
							// 修改和查看对话框
							modAndViewDlgConfig : {
								width : Math.min(600, $window.width()),
								height : Math.min(550, $window.height()),
								modal : true,
								open : false
							},

							/**
							 * 用于修改时显示隐藏元素
							 */
							modElementToggle : function(isShow) {
							},
							viewElementToggle : function(isShow) {
							},
							/**
							 * 增加时清空控件的值
							 */
							addInit : function() {
							},

							/**
							 * 修改和查看是给控件赋值
							 */
							modAndViewInit : function(data) {
								//textSet("seqNo", data.seqNo);
								textSet("name", data.name);
								textSet("desc", data.desc);
								textSet("memo", data.memo);
								textSet("startTime", data.startTime);
								textSet("endTime", data.endTime);
								textSet("imageUuid", data.imageUuid);
								textSet("imageUsage", data.imageUsage);
								textSet("imagePath", data.imagePath);
								textSet("packId", data.id);
								loadSvcKindId("kindId",data.kindId);
								$("#logoSvcPack").attr("src",data.fileBrowseUrl + "?"+ new Date().getTime());
								showPackItem(data.packItemList);
							},

							/**
							 * 封装查询参数
							 */
							queryParam : function(querJson, formProxyQuery) {
								var name = formProxyQuery.getValue("queryName");
								if (name != "") {
									querJson['name'] = name;
								}
								querJson['disabled'] = false;
							},
							savePostDataChange : function(postData) {
							},
							/**
							 * 注册增加和修改的验证表单控件
							 */
							formProxyInit : function(formProxy) {
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
							saveOldData : function(data) {
							},
							/**
							 * 获得删除确认提示
							 */
							getDelComfirmTip : function(data) {
							},
							pageLoad : function(jqGridCtrl, cacheDataGridHelper) {
								loadSvcKind();
								var size = getImageSizeDef("image.logo");
								$id("logoCarSvc").attr("width", size.width);
								$id("logoCarSvc").attr("height", size.height);
							}
						});
		//
		function showPackItem(items) {
			var viewDisplay = $id("viewDisplay").val();
			var packItemObj = {
				viewDisplay : viewDisplay,
				items : items
			};
			var theTpl = laytpl($id("svcPackItemTpl").html());
			var htmlStr = theTpl.render(packItemObj);
			$("#packItem_tbd").html(htmlStr);
		}
	</script>
</body>
<script type="text/html" id="svcPackItemTpl" title="套餐服务明细">
	{{# var packItemObj = d; }}
	{{# var items = packItemObj.items; }}
	{{# if(!isNoB(items) && items.length>0){ }}
	{{# for(var i = 0, len = items.length; i < len; i++){ }}
	{{# var item = items[i]; }}
		<tr id="packItemId{{item.svcId}}">
			<td><label data-role="svcName">{{item.svcName}}</label></td>
			<td><label data-role="svcSalePrice">￥{{item.svcSalePrice}}</label></td>
			<td><label><input class="field value one half wide" style="width: 100px;" name="svcRates" disabled="true" type="text" value="{{(item.rate*100).toFixed(0)}}"/></label>%</td>
		</tr>
	{{# } }}
	{{# } }}
</script>
</html>