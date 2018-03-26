<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>支付设置</title>
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
				<div class="group right aligned">
					<label class="normal label">支付方式名称</label>
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
					<label class="one half wide required field label" >支付方式名称</label>
					<input id="apiName" class="field one half wide value" />
				</div>

				<span class="normal spacer"></span>
				<div class="field group">
					<label class="normal label">启用状态</label>
					<div class="field group">
						<input id="enabled-F" type="radio" checked="checked" name="disabled" value="false" />
						<label for="enabled-F">启用</label>
						<input id="enabled-T" type="radio" data-role="enabledApi" name="disabled" value="true" />
						<label for="enabled-T" data-role="enabledApi">禁用</label>
					</div>
				</div>
				
			</div>
			
			<div class="field row">
				<div class="field group">
					<label class="one and half wide required field label">代码</label>
					<input class="field three wide value" id="apiUrl" />
				</div>
			</div>
			
			<div class="field row" style="height: 70px;">
				<label class="field one half wide label">描述</label>
				<textarea id="apiDesc" class="field value three wide" style="height: 60px;"></textarea>
			</div>
			<span class="normal divider"></span>
			<div class="filter section">
				
				<div style="text-align: center; vertical-align: middle;">
					<table id="req_param_table" class="gridtable">
						<thead><tr>
							<th width="20%"><label class="normal label">参数名称</label></th>
							<th width="30%"><label class="normal label">参数值</label></th>
							<th width="30%"><label class="normal label">描述</label></th>
						</tr></thead>
						<tbody id="req_param_tbd">
							<tr data-role="request" data-name="param">
								<td>
									<label class="normal label"></label>
									<input data-role="name" style="width: 93%;" class="normal input" />
								</td>
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
				
				<div id="acctList" class="noBorder" style="margin-top: 10px;">
					<span class="normal divider"></span>
					<div style="margin: 10px 0;">
						<div class="group left aligned" style="float: left;">
							<label  style="font-size: 14px;color: #666;" id="selectAccount">绑定资金账户</label>
						</div>
						<div class="group right aligned" style="float: right;">
							<button id="selectAccountBtn" class="normal button">选择</button>
						</div>
					</div>
					<div style="text-align: center; vertical-align: middle;">
						<table  style="width: 100%; ">
							<thead></thead>
							<tbody id="userAccount_tbody" style="border: 0">
								<tr class="field row">
									<td align="right" width="18%"><label class="one and half wide field label" style="float: right;">银行名称</label></td>
									<td align="left">
										<input type="hidden" id="accountId" />
										<input type="text" id="bankName" class="field value two wide" disabled/>
									</td>
								</tr>
								<tr class="field row">
									<td align="right"><label class="one and half wide field label" style="float: right;">银行账户</label></td>
									<td align="left">
										<input type="text" id="acctNo" class="field value two wide" disabled/>
									</td>
								</tr>
								<tr class="field row">
									<td align="right"><label class="one and half wide field label" style="float: right;">开户名</label></td>
									<td align="left">
										<input type="text" id="acctName" class="field value two wide" disabled/>
									</td>
								</tr>
								<tr class="field row">
									<td align="right"><label class="one and half wide field label" style="float: right;">预留号码</label></td>
									<td align="left">
										<input type="text" id="phoneNo" class="field value two wide" disabled/>
									</td>
								</tr>
								<tr class="field row">
									<td align="right"><label class="one and half wide field label" style="float: right;">打印验证码</label></td>
									<td align="left">
										<input type="text" id="vfCode" class="field value two wide" disabled/>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
			<!--选择资金账户dialog -->
	<div id="selectUserAccountDialog" class="form" style="display: none;height: 600px;">
		<div class="ui-layout-center">
			<br>
			<table id="userAccountGrid"></table>
			<div id="userAccountPager"></div>
		</div>
	</div>
		
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		//初始化新增add_logis_api_dialog
		var logis_api_dialog;
		var logis_api_grid;
		var logis_com_grid;
		var _apiId = "";
		var method = "add";
		//
		var selectUserAccountDialog = null;
		//
		var accountFormProxy = FormProxy.newOne();
		//
	    var accountGridHelper = JqGridHelper.newOne("");
		
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

		apiFormProxy.addField("disabled");
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
			//加载支付方式信息
			loadApiData();
			
			loadSelectData("queryApiStatus", apiStatusData);

			//调整控件大小
			winSizeMonitor.start(adjustCtrlsSize);
			//初始化资金账户
			loadUserAccountData();
			//选择资金账号
			$id("selectAccountBtn").click(goSelectAccount);
			//
			initSelectUserAccountDialog();
			
			$id("queryApiBtn").click(function(){
				querySmsApi();
			});

		});
		
		function goSelectAccount(){
			selectUserAccountDialog.dialog("open");
			// 页面自适应
			adjustCtrlsSize();
		}
		
		//初始化选择资金账户Dialog
		function initSelectUserAccountDialog(){
			selectUserAccountDialog = $( "#selectUserAccountDialog" ).dialog({
		        autoOpen: false,
		        width : Math.min(800, $window.width()),
		        height : Math.min(300, $window.height()),
		        modal: true,
		        title : '选择资金账户',
		        buttons: {
		            "确定": function(){
		            	var accountId = getCheckedAccount();
		            	showSelectAccountInfo(accountId);
		            	selectUserAccountDialog.dialog("close");
		            },
		            "关闭": function() {
		            	selectUserAccountDialog.dialog( "close" );
		          }
		        },
		        close: function() {
		        	selectUserAccountDialog.dialog( "close" );
		        }
		      });
		}
		
		function getCheckedAccount(){
			var accountId = null;
			$("input:radio[name='account']").each(function(){
				accountId = $(this).val();
				var isChecked = $(this).prop("checked");
				if(isChecked){
					return false;
				}
			});
			return accountId;
		}
		
		function showSelectAccountInfo(id){
			if(id){
				$id("accountId").val(id);
				var account = accountGridHelper.getRowData(id);
				if(account != null){
					$id("bankCode").val(account.bankCode);
					$id("bankName").val(account.bankName);
					$id("acctNo").val(account.acctNo);
					$id("acctName").val(account.acctName);
					$id("phoneNo").val(account.phoneNo);
					$id("vfCode").val(account.vfCode);
				}
			}
		}
		
		//加载资金账户数据
		function loadUserAccountData(){
			var postData = {
			};
			//加载资金账户
			accountGridCtrl= $("#userAccountGrid").jqGrid({
			      url : getAppUrl("/user/userAccount/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : "100%",
				  width : "100%",
			      colNames : ["id", "账户类型", "开户人","开户银行","银行账户","操作"],  
			      colModel : [{name:"id", index:"id", width:150,align : 'center', hidden : true},
			                  {name:"typeFlag", index:"typeFlag",width:110,align : 'center',formatter : function(cellValue){
			                	  return cellValue==0?'私人账户':'对公账户';
			                  }},
			                  {name:"acctName", index:"acctName",width:130,align : 'center',},
			                  {name:"bankName", index:"bankName",width:140,align : 'center'},
			                  {name:"acctNo", index:"acctNo",width:230,align : 'center'},
			                  {name:"action", index:"action", width : 100, align : "center"} 			
							],  
				loadtext: "Loading....",
				multiselect : false,// 定义是否可以多选
				multikey:'ctrlKey',
				//pager : "#userAccountPager",
				loadComplete : function(gridData){
					accountGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if(isFunction(callback)){
						callback();
					}
				},
				gridComplete : function(){
					var ids = accountGridCtrl.jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						id = ParseInt(id);
						accountGridCtrl.jqGrid('setRowData', id, {
							action : "<input type='radio' name='account' value='"+id+"'/>"
						});
					}
				}
			});
		}
		
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
		
		
		function initEditDialog(){
			$id("testArea").css("display","block");
			//
			logis_api_dialog = $("#logis_api_dialog").dialog({
				autoOpen : false,
				height : Math.min(700, $window.height()),
				width : Math.min(760, $window.width()),
				modal : true,
				title : '编辑支付方式',
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
				height : Math.min(700, $window.height()),
				width : Math.min(760, $window.width()),
				modal : true,
				title : '查看支付方式',
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
			if(method == 'edit'){
				var vld = vldInputLogisApi();
				if(vld){
					var dataMap = getLogisApiMap();
					editLogisApi(dataMap);
				}
			}else{
				goEditApi(_apiId);
			}
			
		}

		var apiStatusData = {
			"items" : [ {
				"value" : "0",
				"text" : "启用"
			}, {
				"value" : "1",
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
			dataMap.add("code", $id("apiUrl").val());
			dataMap.add("accountId", $id("accountId").val());
			var disabled = apiFormProxy.getValue("disabled");
			dataMap.add("disabled", disabled);
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
				paramsMap.add("value", $(obj).find("input[data-role='value']").val());
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
				paramsMap.add("value", $(obj).find("input[data-role='value']").val());
				paramsMap.add("desc", $(obj).find("input[data-role='desc']").val());
				params.add(paramsMap);
			});
			//
			dataMap.add("payWayParamList", params);
			//alert(JSON.encode(dataMap));
			return dataMap;
		}

		/* 
		//添加支付方式
		function addLogisApi() {
			var hintBox = Layer.progress("正在保存数据...");

			var dataMap = getLogisApiMap();
			//alert(JSON.encode(dataMap));
			var ajax = Ajax.post("/setting/pay/api/add/do");
			
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
		} */

		
		function goViewApi(apiId){
			method = "view";
			_apiId = apiId;
			//
			initViewDialog();
			showLogisApiOnDialog(apiId);
			logis_api_dialog.dialog("open");
		}
		//当method=view和edit时
		function showLogisApiOnDialog(apiId){
			if(isNoB(apiId)) return;
			//
			var apiMap = apiGridHelper.getRowData(apiId);
			$id("apiId").val(apiMap.id);
			//
			$id("apiName").val(apiMap.name);
			$id("apiName").attr("disabled", "disabled");
			//
			$id("apiUrl").val(apiMap.code);
			$id("apiUrl").attr("disabled", "disabled");
			//
			$id("apiDesc").val(apiMap.desc);
			$id("apiDesc").attr("disabled", "disabled");
			//
			var disabledStr = apiMap.disabled.toString();
			apiFormProxy.setValue("disabled", disabledStr);
			//重置数据
			clearDialog();
			if(apiMap.outerWay){
				$id("acctList").css("display", "");
				if(apiMap.userAccount != null){
					textSet("bankName", apiMap.userAccount.bankName);
					textSet("acctNo", apiMap.userAccount.acctNo);
					textSet("acctName", apiMap.userAccount.acctName);
					textSet("phoneNo", apiMap.userAccount.phoneNo);
					textSet("vfCode", apiMap.userAccount.vfCode);
				}
			}else{
				$id("acctList").css("display", "none");
			}
			//
			if(method == "view"){
				$("[name='disabled']").each(function(){
					$(this).attr("disabled", "disabled");
				});
				$id("selectAccountBtn").attr("disabled", "disabled");
			}else{
				$("[name='disabled']").each(function(){
					$(this).removeAttr("disabled");
				});
				$id("selectAccountBtn").removeAttr("disabled");
				
			}
			//
			var params = apiMap.payWayParamList;
			var dataTab_q = $id("req_param_tbd").html("");
			for(var i=0; i<params.length; i++){
				var p = params[i];
					var trHtml = "<tr data-role='request' data-name='param'>";
					trHtml += "<td>";
					trHtml += "<input data-role='name' disabled data-for='"+p.id+"' value='"+p.name+"' style='width: 93%;' class='normal input' /></td>";
					if(method == "view"){
						trHtml += "<td><input value='"+p.value+"' disabled data-role='value' style='width: 93%;' class='normal input' /></td>";
					}else{
						trHtml += "<td><input value='"+p.value+"' data-role='value' style='width: 93%;' class='normal input' /></td>";
					}
					trHtml += "<td><input data-role='desc' disabled value='"+p.desc+"' style='width: 93%;' class='normal input' /></td>";
					$(dataTab_q).append(trHtml);
			}
		}
		
		function clearDialog(){
			textSet("bankName", "");
			textSet("acctNo", "");
			textSet("acctName", "");
			textSet("phoneNo", "");
			textSet("vfCode", "");
		}
		
		function goEditApi(apiId){
			method = "edit";
			_apiId = apiId;
			//
			initEditDialog();
			showLogisApiOnDialog(apiId);
			logis_api_dialog.dialog("open");
		}
		
		function editLogisApi(dataMap){
			
			var hintBox = Layer.progress("正在保存数据...");
			//alert(JSON.encode(dataMap));
			var ajax = Ajax.post("/setting/pay/api/update/do");
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
		
		/* function delLogisApi(apiId){
			if(isNoB(apiId)) return;
			//
			var theLayer = Layer.confirm('确定要删除该支付方式吗？', function(){
				
				var hintBox = Layer.progress("正在保存数据...");
				
				var ajax = Ajax.post("/setting/pay/api/delete/do");
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
		} */
		
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
			      url : getAppUrl("/setting/pay/api/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(filter,true)},
			      height : "100%",
				  width : "100%",
				  colNames : [ "id", "支付方式", "代码", "使用状态", "描述","操作" ],
			      colModel : [{name:"id", index:"id",width:100,align:'center', hidden: true}, 
			                  {name:"name", width:150, align:'left',}, 
			                  {name:"code", width:200, align:'left',},
			                  {name:"disabled", index:"disabled",width:70,align : 'left',formatter : function (cellValue) {
									return cellValue==false?'启用':'禁用';
								}},
			                  {name:"desc", width:200, align:'left',},
			                  {name:'id',index:'id', formatter : function (cellValue,option,rowObject) {
								return "<span> [<a class='item' href='javascript:void(0);' onclick='goViewApi("
								+ JSON.stringify(cellValue)
								+ ")' >查看</a>]   [<a class='item' href='javascript:void(0);' onclick='goEditApi("
								+ JSON.stringify(cellValue)
								+ ")' >修改</a>] ";
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
