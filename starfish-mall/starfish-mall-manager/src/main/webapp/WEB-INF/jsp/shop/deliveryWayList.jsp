<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>配送方式设置</title>
<style type="text/css">
	.ui-jqgrid tr.jqgrow td {
	  white-space: normal !important;
	  height:auto;
	  vertical-align:text-top;
	  padding-top:2px;
	 }

	hr.divider{
		border-top: 0.5px solid #D3D3D3;
	}
	
	.list{
		list-style:none;
	    margin: 0px;
	    width: 100%;
	    padding-left: 18px;
	    display: inline-block;
	}
	
	.list li{
		float:left;
		width: 180px;
		height: 40px;
		line-height: 38px;
		vertical-align: middle;
	}
	

		
	.title {
		font-weight: bold;
		font-size: 16px;
	}
	
	li label{
		vertical-align: middle;
	}
	
	table.gridtable {
		font-family: verdana, arial, sans-serif;
		font-size: 11px;
		color: #333333;
		border-width: 1px;
		border-color: #666666;
		border-collapse: collapse;
		width: 700px;
	}
	
	table.gridtable th {
		border:1px solid #AAA;
		border-width: 1px;
		padding: 3px;
		border-style: solid;
		border-color: #666666;
		background-color: #dedede;
		height: 30px;
	}
	
	table.gridtable td {
		border:1px solid #AAA;
		border-width: 1px;
		padding: 3px;
		border-style: solid;
		border-color: #666666;
		background-color: #ffffff;
		height: 30px;
	}
</style>
</head>
<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding: 4px; vertical-align: bottom;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnAdd" class="normal button one half wide">选择配送方式</button>
					<button id="btnDelBatch" class="normal button">批量删除</button>
				</div>
				<div class="group right aligned">
					<label class="label">名称</label> <input id="paramName" class="input one half wide"> <span class="spacer"></span>
					<label class="label one wide">物流公司</label> <input id="paramCom" class="input one half wide"><span class="spacer"></span>
					<button id="btnQuery" class="normal button">查询</button>
				</div>
			</div>
		</div>

	</div>
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<table id="deliveryWayList"></table>
		<div id="deliveryWayPager"></div>
	</div>
	<div id="deliveryWayDialog"></div>
	<div id="logisComDialog" title="选择方式设置"></div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		var paramVals = [];
		var addList = [];
		var delList = [];
		var checkedList = [];
		var origMap = new KeyMap();
		var code = "delivery.way";
		var name = "配送方式设置";
		var deliveryWayGrid;
		var curAction;
		var deliveryWayDialog;
		var logisComDialog;
		var formProxy = FormProxy.newOne();
		var searchFormProxy = FormProxy.newOne();
		// 缓存当前jqGrid数据行数组
		var deliveryWayGridHelper = JqGridHelper.newOne("");
		var tempDeliveryWay = null;
		var tempId = null;
		var shopId = null;
		var logisComGroup = "";
		//唯一标识
		var uniqFlag = true;
		searchFormProxy.addField({
			id : "paramName",
			rules : [ "maxLength[30]" ]
		});
		searchFormProxy.addField({
			id : "paramCom",
			rules : [ "maxLength[30]" ]
		});

		//加载数据列表
		function loadData() {
			getParamVals();
			deliveryWayGrid = $id("deliveryWayList")
					.jqGrid(
							{
								url : getAppUrl("/setting/deliveryWay/list/get"),
								contentType : 'application/json',
								mtype : "post",
								postData : {
									filterStr : JSON.encode({
										"ids" : paramVals == '' ? [ -1 ]
												: paramVals
									}, true)
								},
								datatype : 'json',
								colNames : [ "id", "名称", "物流公司", "保价费率",
										"货到付款", "使用状态", "操作" ],
								colModel : [
										{
											name : "id",
											index : "id",
											hidden : true
										},
										{
											name : "name",
											width : 150,
											align : 'left',
										},
										{
											name : "comId",
											width : 150,
											align : 'left',
											formatter : function(cellValue,
													option, rowObject) {
												return $(
														"#company option[value="
																+ cellValue
																+ "]").text();
											}
										},
										{
											name : "insureRate",
											width : 150,
											align : 'left',
											formatter : function(cellValue,
													option, rowObject) {
												return cellValue + "%";
											}
										},
										{
											name : "supportPod",
											width : 150,
											align : 'left',
											formatter : function(cellValue,
													option, rowObject) {
												if (cellValue) {
													return "支持";
												}
												return "暂不支持";
											}
										},
										{
											name : "disabled",
											width : 150,
											align : 'left',
											formatter : function(cellValue,
													option, rowObject) {
												if (cellValue) {
													return "禁用";
												}
												return "启用";
											}
										},
										{
											name : 'id',
											index : 'id',
											formatter : function(cellValue,
													option, rowObject) {
												return "<span> [<a href='javascript:void(0);' onclick='deliveryWayDialogView("
														+ JSON
																.stringify(rowObject)
														+ ")' >查看</a>]   [<a href='javascript:void(0);' onclick='deleteDelivery("
														+ cellValue
														+ ")' >删除</a>] ";
											},
											width : "200",
											align : "center"
										} ],
								pager : "#deliveryWayPager",
								height : "auto",
								multiselect : true,
								multikey:'ctrlKey',
								loadComplete : function(gridData) { // JqGridHelper缓存最新的grid数据
									deliveryWayGridHelper.cacheData(gridData);
									var callback = getCallbackAfterGridLoaded();
									if (isFunction(callback)) {
										callback();
									}
								},
								ondblClickRow: function(rowId) {
									var userMap = deliveryWayGridHelper.getRowData(rowId)
									deliveryWayDialogView(userMap);
								}
							});

		}
		
		getCallbackAfterGridLoaded = function(){
		};

		//初始化dialogView
		function initDeliveryWayDialogView() {
			$("div[aria-describedby='deliveryWayDialog']").find("span[class='ui-dialog-title']").html("查看配送方式");
			//
			deliveryWayDialog = $id("deliveryWayDialog").dialog({
				autoOpen : false,
				height : Math.min(500, $window.height()),
				width : Math.min(500, $window.width()),
				modal : true,
				buttons : {
					"关闭" : function() {
						clearDialog();
						deliveryWayDialog.dialog("close");
					}
				},
				close : function() {
					clearDialog();
				}
			});
		}

		function initLogisComDialog() {
			logisComDialog = $id("logisComDialog").dialog({
				autoOpen : false,
				height : Math.min(500, $window.height()),
				width : Math.min(735, $window.width()),
				modal : true,
				buttons : {
					"保存" : saveParam,
					"关闭" : function() {
						clearDialog();
						logisComDialog.dialog("close");
					}
				},
				close : function() {
					clearDialog();
				}
			});
		}

		//
		function deliveryWayDialogView(obj) {
			clearDialog();
			tempDeliveryWay = obj;
			$id("name").val(tempDeliveryWay.name);
			$id("company").val(tempDeliveryWay.comId);
			$id("insureRate").val(tempDeliveryWay.insureRate);
			$id("desc").val(tempDeliveryWay.desc);
			if (tempDeliveryWay.disabled) {
				$id("unable").attr("checked", "true");
			} else {
				$id("enable").attr("checked", "true");
			}
			if (tempDeliveryWay.supportPod) {
				$id("support").attr("checked", "true");
			} else {
				$id("opposed").attr("checked", "true");
			}
			initDeliveryWayDialogView();
			$("input", "#deliveryWayDialog").attr("disabled", true);
			$("select", "#deliveryWayDialog").attr("disabled", true);
			$("textarea", "#deliveryWayDialog").attr("disabled", true);
			deliveryWayDialog.dialog("open");
		}

		//
		function clearDialog() {
			curAction = null;
			tempDeliveryWay = null;
			tempId = null;
			uniqFlag = true;
			addList = [];
			delList = [];
			checkedList = [];
			formProxy.hideMessages();
			searchFormProxy.hideMessages();
			$id("name").val("");
			$id("company").val("");
			$id("insureRate").val("");
			$id("desc").val("");
			$id("enable").attr("checked", "true");
			$id("support").attr("checked", "true");
			$("input", "#deliveryWayDialog").attr("disabled", false);
			$("select", "#deliveryWayDialog").attr("disabled", false);
			$("textarea", "#deliveryWayDialog").attr("disabled", false);
		}

		//
		function initTpl() {
			var deliveryTplHtml = $id("deliveryTpl").html();
			var htmlStr = laytpl(deliveryTplHtml).render({});
			$id("deliveryWayDialog").html(htmlStr);
			initDeliveryWayDialogView();
		}

		// 加载物流公司
		function loadCompany() {
			var ajax = Ajax.post("/setting/logis/company/list/get");
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("company", result.data);
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}

		//
		function deleteDelivery(id) {
			var theLayer = Layer.confirm('确定要删除？', function() {
				paramVals.splice(jQuery.inArray(id, paramVals), 1);
				var hintBox = Layer.progress("正在删除...");
				var ajax = Ajax.post("/shop/param/val/delete/do");
				paramVals = paramVals.sort();
				if(paramVals.length == 0){
					paramVals = [-1];
				}
				ajax.data({
					id : tempId,
					shopId : shopId,
					deliveryWayIds : paramVals
				});
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						//
						getParamVals();
						updatePermsStatus();
						deliveryWayGrid.jqGrid(
								"setGridParam",
								{
									postData : {
										filterStr : JSON.encode({
											"ids" : paramVals == '' ? [ -1 ]
													: paramVals
										}, true)
									}
								}).trigger("reloadGrid");
						logisComDialog.dialog("close");
						clearDialog();
					}
				});
				ajax.always(function() {
					hintBox.hide();
				});
				ajax.go();
			});
		}

		//查询
		function search() {
			var vldResult = searchFormProxy.validateAll();
			if (!vldResult) {
				return;
			}
			var nameVal = $id("paramName").val();
			var comNameVal = $id("paramCom").val();
			deliveryWayGrid.jqGrid("setGridParam", {
				postData : {
					filterStr : JSON.encode({
						"name" : nameVal,
						"comName" : comNameVal,
						"ids" : paramVals == '' ? [ -1 ] : paramVals
					}, true)
				}
			}).trigger("reloadGrid");
		}

		function getParamVals() {
			if(!isNoB(paramVals)){
				paramVals.clear();
			}
			var ajax = Ajax.post("/shop/param/val/get");
			ajax.data({
				code : code
			});
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				var data = result.data;
				if (data != null) {
					var dataVal = JSON.decode(data.value);
					paramVals = dataVal.ids;
					tempId = data.id;
					code = data.code;
					name = data.name;
					shopId = data.shopId;
				}
			});
			ajax.go();
		}

		function initLogisComGroup() {
			var ajax = Ajax.post("/setting/logis/company/group/get");
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				var data = result.data;
				logisComGroup = data;
				var tplHtml = $id("logisComTpl").html();
				var htmlStr = laytpl(tplHtml).render(data);
				$id("logisComDialog").html(htmlStr);
				initLogisComDialog();
			});
			ajax.go();
		}

		function saveParam() {
			getAddAndDelList();
			if(!isNoE(checkedList)){
				checkedList = checkedList.sort();
			}else{
				checkedList = [-1];
			}		
			if (addList.length == 0 && delList.length == 0) {
				Toast.show("没有任何改变，无效的操作！");
			} else {
				var ajax = Ajax.post("/shop/param/val/save/do");
				ajax.data({
					id : tempId,
					shopId : shopId,
					deliveryWayIds : checkedList
				});
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
					Layer.msgSuccess(result.message);
					getParamVals();
					updatePermsStatus();
					deliveryWayGrid.jqGrid("setGridParam", {
						postData : {
							filterStr : JSON.encode({
								"ids" : paramVals == '' ? [ -1 ] : paramVals
							}, true)
						}
					}).trigger("reloadGrid");
					logisComDialog.dialog("close");
					clearDialog();
					}
				});
				ajax.go();
			}
		}

		function logisComDialogOpen() {
			if(logisComGroup == ""){
				Toast.show("没有可选择的配送方式");
			}else{
				updatePermsStatus();
				logisComDialog.dialog("open");
			}
		}

		//更新权限状态
		function updatePermsStatus() {
			//清空权限初始化状态
			origMap.clear();
			//添加权限初始状态，全部为false
			$("input:checkbox", "#logisComDialog").each(function(i, p) {
				var deliveryId = $(p).attr("data-id");
				$("input:checkbox", "#logisComDialog").removeAttr("checked");
				origMap.add(deliveryId, false);
			});
			var len = 0;
			if(!isNoB(paramVals)){
				len = paramVals.length;
			}
			for (var i = 0; i < len; i++) {
				var pvId = paramVals[i];
				$id("dw-" + pvId).attr("checked", 'checked');
				origMap.set(pvId, true);
			}
		}

		//
		function getAddAndDelList() {
			checkedList = FormProxy.fieldAccessors.checkbox.get("deliveryWay");
			$("input:checkbox", "#logisComDialog").each(function(i, p) {
				var deliveryId = $(p).attr("data-id");
				var oldStatus = origMap.get(deliveryId);
				var newStatus = $(p).is(':checked');
				if (newStatus != oldStatus) {
					if (newStatus) {
						addList.add(deliveryId);
					}
					if (oldStatus) {
						delList.add(deliveryId);
					}
				}
			});
		}

		//
		function deleteBatch() {
			var ids = deliveryWayGrid.jqGrid("getGridParam", "selarrrow");
			if (ids == "") {
				Toast.show("请选择要删除的数据！");
			} else {
				var theLayer = Layer.confirm('确定要删除选择的数据吗？',
								function() {
									//
									var hintBox = Layer.progress("正在删除...");
									if(ids.length == paramVals.length){
										paramVals = [-1];
									}else{
										for (var i = 0; i < ids.length; i++) {
											paramVals.splice(jQuery.inArray(ParseInt(ids[i]),paramVals), 1);
										}
									}
									var ajax = Ajax.post("/shop/param/val/delete/do");
									paramVals = paramVals.sort();
									ajax.data({
										id : tempId,
										shopId : shopId,
										deliveryWayIds : paramVals
									});
									ajax.done(function(result, jqXhr) {
											if (result.type == "info") {
												Layer.msgSuccess(result.message);
												//
												getParamVals();
												updatePermsStatus();
												deliveryWayGrid.jqGrid("setGridParam",{postData : {filterStr : JSON.encode({"ids" : paramVals == '' ? [ -1 ]: paramVals},true)}}).trigger("reloadGrid");
												logisComDialog.dialog("close");
												clearDialog();
											}
										});
									ajax.always(function() {
										hintBox.hide();
									});
									ajax.go();
								});
			}
		}
		
		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			console.log("mainWidth:" + mainWidth + ", " + "mainHeight:"+ mainHeight);
			//
			var gridCtrlId = "deliveryWayList";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv",jqGridBox).height();
			var pagerHeight = $id("deliveryWayPager").height();
			deliveryWayGrid.setGridWidth(mainWidth - 1);
			deliveryWayGrid.setGridHeight(mainHeight - headerHeight - pagerHeight - 3);
		}

		//---------------------------------------------------初始化--------------------------------------------------------
		$(function() {
			//页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 56,
				allowTopResize : false
			});
			//隐藏布局north分割线
			$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
			loadData();
			initTpl();
			loadCompany();
			initLogisComGroup();
			$id("btnAdd").click(logisComDialogOpen);
			$id("btnQuery").click(search);
			$id("btnDelBatch").click(deleteBatch);
			//
			deliveryWayGrid.bindKeys();
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
		});
	</script>
</body>
<!-- <br /><label>注：保价费用相对于运费，如：运费100元，保价费用为1%，则保价费用计算得出：1元</label> -->
<!-- layTpl begin -->
<!-- 属性模板 -->
<script id="deliveryTpl" type="text/html">
		<div class="form">
			<div class="field row">
				<label class="field label one wide required">名称</label> 
				<input class="field value one half wide" type="text" id="name" title="请输入配送方式名称" />
			</div>
			<div class="field row">
				<label class="field label one wide required">物流公司</label> 
				<select class="field value one half wide" id="company"></select>
			</div>
			<div class="field row">
				<label class="field label one wide required">使用状态</label> 
				<input id="enable" type="radio" name="disabled" value="0" checked="checked" /><label for = "enable">启用</label>
				<input id="unable" type="radio" name="disabled" value="1" /><label for = "unable">禁用</label>
			</div>
			<div class="field row">
				<label class="field label one wide required">保价费率</label> 
				<input class="field value half wide" type="text" id="insureRate" title="请输入保价费率" onkeyup="value=value.replace(/\.\d{2,}$/,value.substr(value.indexOf('.'),3))" /><label>%</label>
						
			</div>
			<div style="margin-left:80px;">
				<span style="color:gray;">注：保价费用相对于运费，如：运费100元，保价费用为1%，则保价费用计算得出：1元</span>		
			</div>
			<div class="field row">
				<label class="field label one wide required">货到付款</label> 
				<input id="support" type="radio" name="supportPod" value="1" checked="checked" /><label for = "support">支持</label>
				<input id="opposed" type="radio" name="supportPod" value="0" /><label for = "opposed">暂不支持</label>
			</div>
			<div class="field row">
				<label class="field label one wide">描述</label>
				<textarea class="field value three wide" id="desc" style="height:100px;"></textarea>
			</div>
		</div>
</script>
<!-- 物流公司 -->
<script id="logisComTpl" type="text/html">
		<div>
			{{# var logisComs = d, lcLen = logisComs.length; }}
				{{# for(var i = 0; i < lcLen; i++){ }}
					{{# var logisCom = logisComs[i], deliveryWays = logisCom.deliveryWays, dwLen = deliveryWays.length; }}
						<div class='simple block'>
						<div class='header'><label class='title'>{{ logisCom.name }}</label></div>
						<table class="gridtable">
						<thead>
							<tr>
								<th width="30%"><label class="normal label">配送方式名称</label></th>
								<th width="10%"><label class="normal label">保价费率(%)</label></th>
								<th width="10%"><label class="normal label">货到付款</label></th>
								<th width="10%"><label class="normal label">选择</label></th>
							</tr>
						</thead>
						{{# for (var j = 0; j < dwLen; j++) { }}
						<tbody>
							<tr>
								<td><label class="normal label">{{ deliveryWays[j].name }}</label></td>
								<td align="right"><label class="normal label">{{ deliveryWays[j].insureRate }}</label></td>
								<td align="center"><label class="normal label">{{ deliveryWays[j].supportPod ? "支持" : "不支持"}}</label></td>
								<td align="center"><input id="dw-{{ deliveryWays[j].id }}" data-id="{{ deliveryWays[j].id }}" name="deliveryWay" type="checkbox" value="{{ deliveryWays[j].id }}" /> </td>
							</tr>
						</tbody>
						{{# } }}
						</table>
						</div>		
				{{# } }}
		</div>
</script>
<!-- layTpl end -->
</html>
