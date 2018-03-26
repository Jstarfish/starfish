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
</style>
</head>
<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding: 4px; vertical-align: bottom;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnAdd" class="normal button">添加</button>
				</div>
				<div class="group right aligned">
					<label class="label">名称</label> <input id="paramName" class="input one wide"> <span class="spacer"></span>
					<label class="label one wide">物流公司</label> <input id="paramCom" class="input one wide"> <span class="spacer"></span>
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
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		var deliveryWayGrid;
		var curAction;
		var deliveryWayDialog;
		var formProxy = FormProxy.newOne();
		var searchFormProxy = FormProxy.newOne();
		var tempDeliveryWay = null;
		var tempId = null;
		//缓存当前jqGrid数据行数组
		var deliveryWayGridHelper = JqGridHelper.newOne("");
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
			deliveryWayGrid = $id("deliveryWayList")
					.jqGrid(
							{
								url : getAppUrl("/setting/deliveryWay/list/get"),
								contentType : 'application/json',
								mtype : "post",
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
											formatter : function(cellValue,option, rowObject) {
												return $("#company option[value=" + cellValue + "]").text();
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
														+ JSON.stringify(rowObject)
														+ ")' >查看</a>]   [<a href='javascript:void(0);' onclick='deliveryWayDialogEdit("
														+ JSON.stringify(rowObject)
														+ ")' >修改</a>]  [<a href='javascript:void(0);' onclick='deleteDelivery("
														+ cellValue
														+ ")' >删除</a>] ";
											},
											width : "200",
											align : "center"
										} ],
								pager : "#deliveryWayPager",
								height : "auto",
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

		//初始化dialogAdd
		function initDialogAdd() {
			$("div[aria-describedby='deliveryWayDialog']").find("span[class='ui-dialog-title']").html("添加配送方式");
			//
			deliveryWayDialog = $id("deliveryWayDialog").dialog({
				autoOpen : false,
				height : Math.min(500, $window.height()),
				width : Math.min(500, $window.width()),
				modal : true,
				buttons : {
					"保存" : defaultMethod,
					"取消" : function() {
						clearDialog();
						deliveryWayDialog.dialog("close");
					}
				},
				close : function() {
					clearDialog();
				}
			});
		}
		//初始化dialogView
		function initDialogView() {
			$("div[aria-describedby='deliveryWayDialog']").find("span[class='ui-dialog-title']").html("查看配送方式");
			//
			deliveryWayDialog = $id("deliveryWayDialog").dialog({
				autoOpen : false,
				height : Math.min(500, $window.height()),
				width : Math.min(500, $window.width()),
				modal : true,
				buttons : {
					"修改 >" : defaultMethod,
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
		//初始化dialogEdit
		function initDialogEdit() {
			$("div[aria-describedby='deliveryWayDialog']").find(
					"span[class='ui-dialog-title']").html("修改配送方式");
			//
			deliveryWayDialog = $id("deliveryWayDialog").dialog({
				autoOpen : false,
				height : Math.min(500, $window.height()),
				width : Math.min(500, $window.width()),
				modal : true,
				buttons : {
					"保存" : defaultMethod,
					"取消" : function() {
						clearDialog();
						deliveryWayDialog.dialog("close");
					}
				},
				close : function() {
					clearDialog();
				}
			});
		}

		//
		function defaultMethod() {
			if (curAction == "add") {
				addDeliveryWay();
			}
			if (curAction == "edit") {
				updateDeliveryWay();
			}
			if (curAction == "view") {
				deliveryWayDialogEdit(tempDeliveryWay);
			}
		}

		function deliveryWayDialogAdd() {
			clearDialog();
			curAction = "add";
			initDialogAdd();
			loadCompany();
			deliveryWayDialog.dialog("open");
		}

		//
		function deliveryWayDialogView(obj) {
			clearDialog();
			loadCompany();
			curAction = "view";
			tempDeliveryWay = obj;
			tempId = obj.id;
			$id("name").val(tempDeliveryWay.name);
			singleSet("company",tempDeliveryWay.comId);
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
			initDialogView();
			$("input", "#deliveryWayDialog").attr("disabled", true);
			$("select", "#deliveryWayDialog").attr("disabled", true);
			$("textarea", "#deliveryWayDialog").attr("disabled", true);
			deliveryWayDialog.dialog("open");
		}

		//
		function deliveryWayDialogEdit(obj) {
			clearDialog();
			curAction = "edit";
			tempDeliveryWay = obj;
			loadCompany();
			tempId = obj.id;
			$id("id").val(tempDeliveryWay.id);
			singleSet("company",tempDeliveryWay.comId);
			$id("name").val(tempDeliveryWay.name);
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
			initDialogEdit();
			deliveryWayDialog.dialog("open");
		}

		//
		function clearDialog() {
			curAction = null;
			tempDeliveryWay = null;
			tempId = null;
			uniqFlag = true;
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
			clearSelectData("company");
		}

		//
		function initTpl() {
			var deliveryTplHtml = $id("deliveryTpl").html();
			var htmlStr = laytpl(deliveryTplHtml).render({});
			$id("deliveryWayDialog").html(htmlStr);
			loadCompany();
			initDialogAdd();
			formProxy.addField({
				id : "company",
				required : true
			});
			formProxy.addField({
				id : "insureRate",
				required : true,
				rules : [ "isMoney", "rangeValue[0,100]" ]
			});
			formProxy.addField({
				id : "desc",
				rules : [ "maxLength[250]" ]
			});
			formProxy
					.addField({
						id : "name",
						required : true,
						rules : [
								"maxLength[30]",
								{
									rule : function(idOrName, type, rawValue,
											curData) {
										var comId = $id("company").val();
										if (rawValue != "" && comId != "") {
											if (tempDeliveryWay == null) {
												uniqValidate();
											} else {
												if (tempDeliveryWay.name != rawValue
														|| tempDeliveryWay.comId != comId) {
													uniqValidate();
												}
											}
										}
										return uniqFlag;
									},
									message : "名称被占用！"
								} ]
					});
		}

		// 加载物流公司
		function loadCompany() {
			var ajax = Ajax.post("/setting/logis/company/list/get");
			ajax.sync();
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
		function addDeliveryWay() {
			var vldResult = formProxy.validateAll();
			if (vldResult) {
				var ajax = Ajax.post("/setting/deliveryWay/add/do");
				var nameVal = $id("name").val();
				var comIdVal = ParseInt($id("company").val());
				var disabledVal = ParseInt(FormProxy.fieldAccessors.radio
						.get("disabled"));
				var insureRateVal = ParseFloat($id("insureRate").val());
				var supportPodVal = ParseInt(FormProxy.fieldAccessors.radio
						.get("supportPod"));
				var descVal = $id("desc").val();
				data = {
					name : nameVal,
					comId : comIdVal,
					disabled : disabledVal,
					insureRate : insureRateVal,
					supportPod : supportPodVal,
					desc : descVal,
					seqNo : 1
				};
				ajax.data(data);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						//加载最新数据列表
						deliveryWayGrid.jqGrid().trigger("reloadGrid");
						tempDeliveryWay = result.data;
						deliveryWayDialogView(tempDeliveryWay);
					} else {
						Layer.msgWarning(result.message);
					}
				});
				ajax.go();
			}
		}

		//
		function updateDeliveryWay() {
			var vldResult = formProxy.validateAll();
			if (vldResult) {
				var ajax = Ajax.post("/setting/deliveryWay/update/do");
				var idVal = $id("id").val();
				var nameVal = $id("name").val();
				var comIdVal = ParseInt($id("company").val());
				var disabledVal = ParseInt(FormProxy.fieldAccessors.radio
						.get("disabled"));
				var insureRateVal = ParseFloat($id("insureRate").val());
				var supportPodVal = ParseInt(FormProxy.fieldAccessors.radio
						.get("supportPod"));
				var descVal = $id("desc").val();
				data = {
					id : idVal,
					name : nameVal,
					comId : comIdVal,
					disabled : disabledVal,
					insureRate : insureRateVal,
					supportPod : supportPodVal,
					desc : descVal,
					seqNo : 1
				};
				ajax.data(data);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						//加载最新数据列表
						deliveryWayGrid.jqGrid().trigger("reloadGrid");
						tempDeliveryWay = result.data;
						deliveryWayDialogView(tempDeliveryWay);
					} else {
						Layer.msgWarning(result.message);
					}
				});
				ajax.go();
			}
		}

		//检查名字是否可用
		function uniqValidate() {
			var name = $id("name").val();
			var comId = $id("company").val();
			var ajax = Ajax.post("/setting/deliveryWay/by/name/and/comId/get");
			ajax.data({
				name : name,
				comId : ParseInt(comId)
			});

			ajax.sync();
			ajax.done(function(result, jqXhr) {
				uniqFlag = result.data;
			});
			ajax.fail(function() {
				uniqFlag = false;
			});
			ajax.go();
		}

		//
		function deleteDelivery(id) {
			var theLayer = Layer.confirm('确定要删除？', function() {
				//
				var hintBox = Layer.progress("正在删除...");
				var ajax = Ajax.post("/setting/deliveryWay/delete/do");
				ajax.params({
					id : id
				});
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						//
						loadCompany();
						//
						deliveryWayGrid.jqGrid().trigger("reloadGrid");
					} else {
						Layer.msgWarning(result.message);
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
			loadCompany();
			deliveryWayGrid.jqGrid("setGridParam", {
				postData : {
					filterStr : JSON.encode({
						"name" : nameVal,
						"comName" : comNameVal
					}, true)
				}
			}).trigger("reloadGrid");
		}
		
		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			console.log("mainWidth:" + mainWidth + ", " + "mainHeight:"
					+ mainHeight);
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
			initTpl();
			loadData();
			$id("btnAdd").click(deliveryWayDialogAdd);
			$id("btnQuery").click(search);
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
			<input type="hidden" id="id" />
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
<!-- layTpl end -->
</html>
