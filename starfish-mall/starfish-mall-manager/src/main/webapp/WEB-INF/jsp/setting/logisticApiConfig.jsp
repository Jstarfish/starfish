<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>物流查询服务商配置</title>
<style type="text/css">
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
	
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<button id="addApiBtn" class="normal button">添加</button>
				<div class="group right aligned">
					<label class="normal label">服务商名称</label>
					<input id="queryApiName" class="input one and half wide"> <span class="spacer"></span> 
					<label class="label">启用状态</label>
					<select class="input" id="queryApiStatus"></select><span class="spacer"></span>
					<button id="queryApiBtn" class="button">查询</button>
				</div>
			</div>
		</div>
		<div id="logis_api_grid">
			<table id="logis_api_list"></table>
			<div id="logis_api_pager"></div>
		</div>
	</div>

	<div id="logis_api_dialog" style="display:none;">
		<input type="hidden" id="apiId"/>
		<div id="apiform" class="form">
			<div class="field row">
				<div class="field group">
					<label class="one half wide required field label" >物流接口名称</label>
					<input id="apiName" class="field one half wide value" />
				</div>

				<span class="normal spacer"></span>
				<div class="field group">
					<label class="normal label">启用状态</label>
					<div class="field group">
						<input id="enabled-F" type="radio" checked="checked" name="enabled" value="false" />
						<label for="enabled-F">禁用</label>
						<input id="enabled-T" type="radio" data-role="enabledApi" disabled="disabled" name="enabled" value="true" />
						<label for="enabled-T" disabled="disabled" data-role="enabledApi">启用</label>
					</div>
				</div>
				
				<span class="normal spacer"></span>
				<div class="field group">
					<label class="normal label">接口方法类型</label>
					<div class="field group">
						<input id="method-G" type="radio" checked="checked" name="method" value="GET" />
						<label for="method-G">GET</label>
						<input id="method-P" type="radio" name="method" value="POST" />
						<label for="method-P">POST</label>
					</div>
				</div>
			</div>
			
			<div class="field row">
				<div class="field group">
					<label class="one and half wide required field label">物流接口地址</label>
					<input class="field three wide value" id="apiUrl" />
				</div>
			</div>
			
			<div class="field row" style="height: 70px;">
				<label class="field one half wide label">描述</label>
				<textarea id="apiDesc" class="field value three wide" style="height: 60px;"></textarea>
			</div>
			<span class="normal divider"></span>
			<div class="filter section">
				<div class="field row" style="width: 700px;">
					<span>请求参数设置</span>
					<button id="addReqParamBtn" class="normal button" style="width: 120px; margin: 2px 0px; float: right;">添加请求参数</button>
				</div>
				<div style="text-align: center; vertical-align: middle;">
					<table id="req_param_table" class="gridtable">
						<thead><tr>
							<th width="20%"><label class="normal label">参数名称(必填)</label></th>
							<th width="10%"><label class="normal label">变量标志</label></th>
							<th width="30%"><label class="normal label">参数值</label></th>
							<th width="30%"><label class="normal label">描述</label></th>
							<th width="10%"><label class="normal label">操作</label></th>
						</tr></thead>
						<tbody id="req_param_tbd">
							<tr data-role="request" data-name="param">
								<td>
									<label class="normal label"></label>
									<input data-role="name" style="width: 93%;" class="normal input" />
								</td>
								<td><input data-role="varFlag" type="checkbox" /></td>
								<td>
									<label class="label"></label>
									<input style="width: 93%;" data-role="value" class="normal input" />
								</td>
								<td><input data-role="desc" style="width: 93%;" class="normal input" /></td>
								<td></td>
							</tr>
						</tbody>
					</table>

				</div>
			</div>
			<span style="margin: 5px 0;" class="normal divider"></span>
			<div class="filter section">
				<div class="field row" style="width: 700px;">
					<span>响应参数设置</span>
					<button id="addRespParamBtn" class="normal button" style="width: 120px; margin: 2px 0px; float: right;">添加响应参数</button>
				</div>
				<div style="text-align: center; vertical-align: middle;">
					<table id="resp_param_table" class="gridtable">
						<thead><tr>
							<th width="20%"><label class="normal label">参数名称(必填)</label></th>
							<th width="10%"><label class="normal label">变量标志</label></th>
							<th width="30%"><label class="normal label">参数值</label></th>
							<th width="30%"><label class="normal label">描述</label></th>
							<th width="10%"><label class="normal label">操作</label></th>
						</tr></thead>
						<tbody id="resp_param_tbd">
							<tr data-role="response" data-name="param">
								<td><input data-role="name" style="width: 93%;" class="normal input" /></td>
								<td><input data-role="varFlag" type="checkbox" /></td>
								<td><input style="width: 93%;" data-role="value" class="normal input" /></td>
								<td><input data-role="desc" style="width: 93%;" class="normal input" /></td>
								<td></td>
							</tr>
						</tbody>
					</table>

				</div>

			</div>
			<span style="margin: 5px 0;" class="normal divider"></span>
			<div id="testArea" style="display:block; ">
				<div style="width: 700px;" class="field row">
					<label style="vertical-align: middle; margin-right:5px;" class="normal label">测试数据</label>
					<textarea id="testJsonData" class="field value three wide" style="overflow-y: hidden; width: 540px; height: 46px; line-height:44px; vertical-align: middle;">{exComCode:'zhongtong',exNo:'778532484697'}</textarea>
					<button id="vldApiBtn" style="vertical-align: middle; margin-left:5px;" class="normal button">验证</button>
				</div>
			</div>
		</div>
	</div>
		
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		//初始化新增add_logis_api_dialog
		var logis_api_dialog;
		var isValidate = false;
		//var isValidate = true;
		var logis_api_grid;
		var logis_com_grid;
		var _apiId = "";
		var method = "add";
		
		//实例化新增模块表单代理
		var apiFormProxy = FormProxy.newOne();

		apiFormProxy.addField({
			id : "apiName",
			required : true,
			messageTargetId : "apiName"
		});
		apiFormProxy.addField({
			id : "apiUrl",
			required : true,
			messageTargetId : "apiUrl"
		});

		apiFormProxy.addField("enabled");
		apiFormProxy.addField("method");
		apiFormProxy.addField("apiDesc");

		//权限最初始状态列表
		var baseUrl = getAppUrl("");
		//
		$(function() {
			//页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 70,
				allowTopResize : false
			});
			//加载物流服务商信息
			loadApiData();
			
			loadSelectData("queryApiStatus", apiStatusData);

			//调整控件大小
			winSizeMonitor.start(adjustCtrlsSize);
			
			//
			$("[data-role='enabledApi']").click(function(){
				var status = $(this).attr("disabled");
				if(status && !isValidate){
					Toast.show("亲，服务商验证通过后才可以启用哦！", 2000, "warning");
				}
			});
			
			//
			$id("addReqParamBtn").click(function() {
				addTr("req_param_table", "request");
			});
			//
			$id("addRespParamBtn").click(function(){
				addTr("resp_param_table","response");
			});
			
			$id("vldApiBtn").click(function() {
				var vld = vldInputLogisApi();
				if(vld){
					var dataMap = getLogisApiMap();
					var jsonStr = $id("testJsonData").val().trim();
					var formData = JSON.decode(jsonStr);
					var exComCode = formData.exComCode;
					var exNo = formData.exNo;
					if(exComCode == 'undefined' || exNo == 'undefined'){
						alert("测试数据格式不正确。参考用例：{exComCode:'zhongtong',exNo:'778532484697'}");
						return;
					}
					dataMap.add("exComCode", formData.exComCode);
					dataMap.add("exNo", formData.exNo);
					validateApi(dataMap);
				}
			});
			
			$id("queryApiBtn").click(function(){
				querySmsApi();
			});

			//绑定添加按钮
			$id("addApiBtn").click(function() {
				goAddApi();
			});
			
		});
		
		//-------------------------------------------------调整控件大小------------------------------------------------------------
		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "logis_api_list";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("logis_api_pager").height();
			logis_api_grid.setGridWidth(mainWidth - 1);
			logis_api_grid.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 50);
		}
		
		//-------------------------------------------------功能实现 --------------------------------------------------------------
		
		
		function goAddApi(){
			method = "add";
			isValidate = false;
			//isValidate = true;
			//
			initAddDialog();
			clearDialog();
			logis_api_dialog.dialog("open");
		}
		
		function initAddDialog(){
			$id("testArea").css("display","block");
			//
			logis_api_dialog = $("#logis_api_dialog").dialog({
				autoOpen : false,
				height : Math.min(500, $window.height()),
				width : Math.min(850, $window.width()),
				modal : true,
				title : '新增物流服务商接口',
				buttons : {
					"保存" : selectMethod,
					"取消" : function() {
						logis_api_dialog.dialog("close");
					}
				},
				close : function() {
					apiFormProxy.hideMessages();

				}
			});
		}
		
		function initEditDialog(){
			$id("testArea").css("display","block");
			//
			logis_api_dialog = $("#logis_api_dialog").dialog({
				autoOpen : false,
				height : Math.min(500, $window.height()),
				width : Math.min(850, $window.width()),
				modal : true,
				title : '编辑物流服务商接口',
				buttons : {
					"保存" : selectMethod,
					"关闭" : function() {
						logis_api_dialog.dialog("close");
					}
				},
				close : function() {
					apiFormProxy.hideMessages();

				}
			});
		}
		
		function initViewDialog(){
			$id("testArea").css("display","none");
			//
			logis_api_dialog = $("#logis_api_dialog").dialog({
				autoOpen : false,
				height : Math.min(500, $window.height()),
				width : Math.min(850, $window.width()),
				modal : true,
				title : '查看物流服务商接口',
				buttons : {
					"修改 >" : selectMethod,
					"关闭" : function() {
						logis_api_dialog.dialog("close");
					}
				},
				close : function() {
					apiFormProxy.hideMessages();

				}
			});
		}
		
		function vldInputLogisApi(){
			var vldstatus = apiFormProxy.validateAll();
			if(!vldstatus) return false;
			
			$("input[data-role='name']").each(function() {
				if ($(this).val() == "") {
					$(this).focus();
					vldstatus = false;
					return false;
				}
			});

			if (!vldstatus) {
				Toast.show("参数名称不能为空！", 2000, "warning");
				return false;
			}
			apiFormProxy.hideMessages();
			return true;
		}
		
		function selectMethod(){
			if(method == 'add'){
				var vld = vldInputLogisApi();
				if(vld) addLogisApi();
			}else if(method == 'edit'){
				var vld = vldInputLogisApi();
				if(vld){
					var dataMap = getLogisApiMap();
					editLogisApi(dataMap);
				}
			}else if(method == 'view'){
				goEditApi(_apiId);
			}
			
		}

		function addTr(tab, paramType) {
			var trHtml = "<tr data-role='"+paramType+"' data-name='param'>";
			trHtml += "<td><label class='normal label'></label><input data-role='name' style='width: 93%;' class='normal input' /></td>";
			trHtml += "<td><input data-role='varFlag' type='checkbox'/></td>";
			trHtml += "<td><label class='label'></label><input data-role='value' style='width: 93%;' class='normal input' /></td>";
			trHtml += "<td><input data-role='desc' style='width: 93%;' class='normal input' /></td>";
			trHtml += "<td><a onclick='delTr(this)' href='javascript:void(0);' style='text-decoration: underline;'>删除</a></td></tr>"
			var $tr = $("#" + tab + " tr:last");
			$tr.after(trHtml);
		}

		function delTr(obj) {
			$(obj).parent().parent().remove();
		}

		var apiStatusData = {
			"items" : [ {
				"value" : "1",
				"text" : "启用"
			}, {
				"value" : "0",
				"text" : "禁用"
			} ],
			"unSelectedItem" : {
				"value" : "",
				"text" : "- 全部 -"
			},
			"defaultValue" : ""
		};

		var addStatusData = {
			"items" : [ {
				"value" : "true",
				"text" : "启用"
			}, {
				"value" : "false",
				"text" : "禁用"
			} ],
			"defaultValue" : "false"
		};
		
		function getLogisApiMap() {
			var dataMap = new KeyMap("logisApi");
			//logis api
			var apiId = $id("apiId").val();
			dataMap.add("id", apiId);
			dataMap.add("name", $id("apiName").val());
			dataMap.add("url", $id("apiUrl").val());
			var enabled = apiFormProxy.getValue("enabled");
			dataMap.add("enabled", enabled);
			//
			dataMap.add("method", apiFormProxy.getValue("method"));
			dataMap.add("desc", $id("apiDesc").val());
			
			//params
			var params = [];
			$("tr[data-role='request']").each(function(i, obj) {
				var paramsMap = new KeyMap("params");
				var id = $(obj).find("input[data-role='name']").attr("data-for");
				if(id != 'undefined' && typeof(id) != 'undefined'){
					paramsMap.add("id", id);
				}
				paramsMap.add("name", $(obj).find("input[data-role='name']").val());
				paramsMap.add("varFlag",$(obj).find("input[data-role='varFlag']").prop("checked") == true);
				paramsMap.add("value", $(obj).find("input[data-role='value']").val());
				paramsMap.add("ioFlag", 1);
				paramsMap.add("desc", $(obj).find("input[data-role='desc']").val());
				params.add(paramsMap);
			});
			//
			$("tr[data-role='response']").each(function(i, obj) {
				var paramsMap = new KeyMap("params");
				var id = $(obj).find("input[data-role='name']").attr("data-for");
				if(id != 'undefined' && typeof(id) != 'undefined'){
					paramsMap.add("id", id);
				}
				paramsMap.add("name", $(obj).find("input[data-role='name']").val());
				paramsMap.add("varFlag",$(obj).find("input[data-role='varFlag']").prop("checked") == true);
				paramsMap.add("value", $(obj).find("input[data-role='value']").val());
				paramsMap.add("ioFlag", 2);
				paramsMap.add("desc", $(obj).find("input[data-role='desc']").val());
				params.add(paramsMap);
			});
			//
			dataMap.add("params", params);
			//alert(JSON.encode(dataMap));
			return dataMap;
		}

		function addLogisApi() {
			if(!isValidate){
				Toast.show("物流服务商还未经过验证！ ", 2000, "warning");
				return;
			}
			var hintBox = Layer.progress("正在保存数据...");

			var dataMap = getLogisApiMap();
			var ajax = Ajax.post("/setting/logis/api/add/do");
			
			ajax.data(dataMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					_apiId = result.data;
					//加载最新数据列表
					querySmsApi();
					getCallbackAfterGridLoaded = function(){
						return function(){
							goViewApi(_apiId);
						};
					};
				} else {
					Layer.msgWarning(result.message);
				}

			});
			ajax.always(function() {
				hintBox.hide();
			});
			
			ajax.go();
		}

		function validateApi(postData) {
			var hintBox = Layer.progress("正在验证数据...");
			
			var ajax = Ajax.post("/setting/logis/api/validate/do");
			ajax.data(postData);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					isValidate = true;
					var msg = "验证通过！查询状态返回信息：<font style='color:navy;'>"+result.message+"</font>。";
					$("[data-role='enabledApi']").each(function(){
						$(this).unbind("click");
						$(this).removeAttr("disabled");
					});
					//
					Layer.msgSuccess(msg);
					
				} else {
					isValidate = false;
					Layer.msgWarning(result.message);
				}

			});
			
			ajax.always(function() {
				hintBox.hide();
			});
			
			ajax.go();
		}
		
		function goViewApi(apiId){
			method = "view";
			_apiId = apiId;
			//
			initViewDialog();
			showLogisApiOnDialog(apiId);
			logis_api_dialog.dialog("open");
		}
		
		function clearDialog(){
			//
			$id("apiName").val("");
			$id("apiName").removeAttr("disabled");
			//
			apiFormProxy.setValue("enabled", "false");
			
			$("[name='enabled']").each(function(){
				$(this).removeAttr("disabled");
			});
			//
			$("[data-role='enabledApi']").each(function(){
				$(this).attr("disabled","disabled");
				$(this).click(function(){
					$(this).unbind("click");
					var status = $(this).attr("disabled");
					if(status && !isValidate){
						Toast.show("亲，服务商验证通过后才可以启用哦！", 2000, "warning");
					}
				});
			});
			
			//
			$("[name='method']").each(function(){
				$(this).removeAttr("disabled");
			});
			apiFormProxy.setValue("method", "GET");
			//
			$id("apiUrl").val("");
			$id("apiUrl").removeAttr("disabled");
			//
			$id("apiDesc").val("");
			$id("apiDesc").removeAttr("disabled");
			//
			$id("addReqParamBtn").css("display","block");
			//
			$id("addRespParamBtn").css("display","block");

			//request
			$id("req_param_tbd").html("");
			var trHtml = "<tr data-role='request' data-name='param'><td><label class='normal label'></label><input data-role='name' style='width: 93%;' class='normal input' /></td><td><input data-role='varFlag' type='checkbox' /></td><td><label class='label'></label><input style='width: 93%;' data-role='value' class='normal input' /></td><td><input data-role='desc' style='width: 93%;' class='normal input' /></td><td></td></tr>";
			$id("req_param_tbd").append(trHtml);
			
			//response
			$id("resp_param_tbd").html("");
			var trHtml = "<tr data-role='response' data-name='param'><td><label class='normal label'></label><input data-role='name' style='width: 93%;' class='normal input' /></td><td><input data-role='varFlag' type='checkbox' /></td><td><label class='label'></label><input style='width: 93%;' data-role='value' class='normal input' /></td><td><input data-role='desc' style='width: 93%;' class='normal input' /></td><td></td></tr>";
			$id("resp_param_tbd").append(trHtml);
			
			//response
			var trHtml = "{exComCode:'zhongtong',exNo:'778532484697'}";
			$id("testJsonData").text(trHtml);
			
		}
		//当method=view和edit时
		function showLogisApiOnDialog(apiId){
			if(isNoB(apiId)) return;
			//
			var apiMap = apiGridHelper.getRowData(apiId);
			$id("apiId").val(apiMap.id);
			//
			if(method == "view"){
				$id("apiName").val(apiMap.name);
				$id("apiName").attr("disabled", "disabled");
				//
				$id("apiUrl").val(apiMap.url);
				$id("apiUrl").attr("disabled", "disabled");
				//
				$id("apiDesc").val(apiMap.desc);
				$id("apiDesc").attr("disabled", "disabled");
				//
				var enabledStr = apiMap.enabled.toString();
				apiFormProxy.setValue("enabled", enabledStr);
				$("[name='enabled']").each(function(){
					$(this).attr("disabled", "disabled");
				});
				//
				$("[data-role='enabledApi']").each(function(){
					$(this).unbind("click");
				});
				//
				apiFormProxy.setValue("method", apiMap.method);
				$("[name='method']").each(function(){
					$(this).attr("disabled", "disabled");
				});
				
				$id("addReqParamBtn").css("display","none");
				$id("addRespParamBtn").css("display","none");
				
			}else{
				//
				$id("apiName").val(apiMap.name);
				$id("apiName").removeAttr("disabled");
				//
				$id("apiUrl").val(apiMap.url);
				$id("apiUrl").removeAttr("disabled");
				//
				var enabled = apiMap.enabled;
				var enabledStr = enabled.toString();
				apiFormProxy.setValue("enabled", enabledStr);
				$("[name='enabled']").each(function(){
					$(this).removeAttr("disabled");
				});
				//
				if(enabledStr == "true"){
					$("[data-role='enabledApi']").each(function(){
						$(this).unbind("click");
						$(this).removeAttr("disabled");
					});
				}else{
					$("[data-role='enabledApi']").each(function(){
						$(this).attr("disabled","disabled");
						$(this).click(function(){
							$(this).unbind("click");
							var status = $(this).attr("disabled");
							if(status && !isValidate){
								Toast.show("亲，服务商验证通过后才可以启用哦！", 2000, "warning");
							}
						});
					});
				}
				//
				apiFormProxy.setValue("method", apiMap.method);
				$("[name='method']").each(function(){
					$(this).removeAttr("disabled");
				});
				//
				$id("apiDesc").val(apiMap.desc);
				$id("apiDesc").removeAttr("disabled");
				//
				$id("addReqParamBtn").css("display","inline-block");
				$id("addRespParamBtn").css("display","inline-block");
					
			}
			//
			var params = apiMap.ps;
			var dataTab_q = $id("req_param_tbd").html("");
			var dataTab_p = $id("resp_param_tbd").html("");
			for(var i=0; i<params.length; i++){
				var p = params[i];
				if(p.ioFlag == 1 || p.ioFlag == '1'){
					var trHtml = "<tr data-role='request' data-name='param'>";
					trHtml += "<td>";
					if(method == "view"){
						trHtml += "<input data-role='name' disabled data-for='"+p.id+"' value='"+p.name+"' style='width: 93%;' class='normal input' /></td>";
						
						if(p.varFlag){
							trHtml += "<td><input data-role='varFlag' disabled checked type='checkbox'/></td>";
						}else{
							trHtml += "<td><input data-role='varFlag' disabled type='checkbox'/></td>";
						}
						
						trHtml += "<td><input value='"+p.value+"' disabled data-role='value' style='width: 93%;' class='normal input' /></td>";
						trHtml += "<td><input data-role='desc' disabled value='"+p.desc+"' style='width: 93%;' class='normal input' /></td>";
						trHtml += "<td></td></tr>";
					}else{
						trHtml += "<input data-role='name' data-for='"+p.id+"' value='"+p.name+"' style='width: 93%;' class='normal input' /></td>";
						
						if(p.varFlag){
							trHtml += "<td><input data-role='varFlag' checked type='checkbox'/></td>";
						}else{
							trHtml += "<td><input data-role='varFlag' type='checkbox'/></td>";
						}
						
						trHtml += "<td><input value='"+p.value+"' data-role='value' style='width: 93%;' class='normal input' /></td>";
						trHtml += "<td><input data-role='desc' value='"+p.desc+"' style='width: 93%;' class='normal input' /></td>";
						trHtml += "<td><a onclick='delTr(this)' href='javascript:void(0);' style='text-decoration: underline;'>删除</a></td></tr>";
					}
					$(dataTab_q).append(trHtml);
					
				}else if(p.ioFlag == 2 || p.ioFlag == '2'){
					var trHtml = "<tr data-role='response' data-name='param'>";
					trHtml += "<td>";
					if(method == "view"){
						trHtml += "<input data-role='name' disabled data-for='"+p.id+"' value='"+p.name+"' style='width: 93%;' class='normal input' /></td>";
						
						if(p.varFlag){
							trHtml += "<td><input data-role='varFlag' disabled checked type='checkbox'/></td>";
						}else{
							trHtml += "<td><input data-role='varFlag' disabled type='checkbox'/></td>";
						}
						
						trHtml += "<td><input value='"+p.value+"' disabled data-role='value' style='width: 93%;' class='normal input' /></td>";
						trHtml += "<td><input data-role='desc' disabled value='"+p.desc+"' style='width: 93%;' class='normal input' /></td>";
						trHtml += "<td></td></tr>";
					}else{
						trHtml += "<input data-role='name' data-for='"+p.id+"' value='"+p.name+"' style='width: 93%;' class='normal input' /></td>";
						
						if(p.varFlag){
							trHtml += "<td><input data-role='varFlag' checked type='checkbox'/></td>";
						}else{
							trHtml += "<td><input data-role='varFlag' type='checkbox'/></td>";
						}
						
						trHtml += "<td><input value='"+p.value+"' data-role='value' style='width: 93%;' class='normal input' /></td>";
						trHtml += "<td><input data-role='desc' value='"+p.desc+"' style='width: 93%;' class='normal input' /></td>";
						trHtml += "<td><a onclick='delTr(this)' href='javascript:void(0);' style='text-decoration: underline;'>删除</a></td></tr>";
					}
					$(dataTab_p).append(trHtml);
					
				}
			}
		}
		
		function goEditApi(apiId){
			method = "edit";
			isValidate = false;
			//isValidate = true;
			_apiId = apiId;
			//
			initEditDialog();
			showLogisApiOnDialog(apiId);
			logis_api_dialog.dialog("open");
		}
		
		function editLogisApi(dataMap){
			
			var hintBox = Layer.progress("正在保存数据...");
			
			var ajax = Ajax.post("/setting/logis/api/update/do");
				ajax.data(dataMap);
				ajax.done(function(result, jqXhr){
					if(result.type== "info"){
						Layer.msgSuccess(result.message);
						//加载最新数据列表
						logis_api_grid.jqGrid().trigger("reloadGrid");
						getCallbackAfterGridLoaded = function(){
							return function(){
								goViewApi(_apiId);
							};
						};
					}else{
						Layer.msgWarning(result.message);
					}
				});
				ajax.always(function() {
					hintBox.hide();
				});
				ajax.go();
		}
		
		function delLogisApi(apiId){
			if(isNoB(apiId)) return;
			//
			var theLayer = Layer.confirm('确定要删除该物流服务商吗？', function(){
				
				var hintBox = Layer.progress("正在保存数据...");
				
				var ajax = Ajax.post("/setting/logis/api/delete/do");
					ajax.data({id: parseInt(apiId)});
					ajax.done(function(result, jqXhr){
						if(result.type== "info"){
							Layer.msgSuccess(result.message);
							//加载最新数据列表
							logis_api_grid.jqGrid().trigger("reloadGrid");
							getCallbackAfterGridLoaded = function(){
								return function(){
									//getCallbackAfterGridLoaded();
								};
							};
						}else{
							Layer.msgWarning(result.message);
						}
					});
					ajax.always(function() {
						theLayer.hide();
						hintBox.hide();
					});
					ajax.go();
				});
		}
		
		function getCallbackAfterGridLoaded(){
			
		}
		
		function querySmsApi(){
			var filter = {};
			var apiName = textGet("queryApiName");
			if(apiName){
				filter.name = apiName;
			}
			var api_status = $id("queryApiStatus").find("option:selected").val();
			if(api_status){
				filter.status = api_status;
			}
			//
			logis_api_grid.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			getCallbackAfterGridLoaded = function(){
			};
		}
		
		var apiGridHelper = JqGridHelper.newOne("");
		
		function loadApiData() {
			//
			var filter = {};
			var apiName = textGet("queryApiName");
			if(apiName){
				filter.name = apiName;
			}
			var api_status = $id("queryApiStatus").find("option:selected").val();
			if(api_status){
				filter.status = api_status;
			}
			//
			logis_api_grid= $("#logis_api_list").jqGrid({
			      url : getAppUrl("/setting/logis/api/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(filter,true)},
			      height : "100%",
				  width : "100%",
				  colNames : [ "id", "物流服务商", "网址", "使用状态", "描述","操作" ],
			      colModel : [{name:"id", index:"id",width:100,align:'center', hidden: true}, 
			                  {name:"name", width:150, align:'left',}, 
			                  {name:"url", width:200, align:'left',},
			                  {name:"enabled", index:"enabled",width:70,align : 'left',formatter : function (cellValue) {
									return cellValue==true?'启用':'禁用';
								}},
			                  {name:"desc", width:200, align:'left',},
			                  {name:'id',index:'id', formatter : function (cellValue,option,rowObject) {
								return "<span> [<a class='item' href='javascript:void(0);' onclick='goViewApi("
								+ JSON.stringify(cellValue)
								+ ")' >查看</a>]   [<a class='item' href='javascript:void(0);' onclick='goEditApi("
								+ JSON.stringify(cellValue)
								+ ")' >修改</a>]  [<a class='item' href='javascript:void(0);' onclick='delLogisApi("
								+ cellValue
								+ ")' >删除</a>] ";
							},
						width:150,align:"center"}
			                  ],  
				loadComplete : function(gridData){
					apiGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if(isFunction(callback)){
						callback();
					}

				},
				ondblClickRow: function(rowId) {
					var apiMap = apiGridHelper.getRowData(rowId)
					goViewApi(apiMap.id);
				}
			});

		}
		
	</script>
</body>
</html>
