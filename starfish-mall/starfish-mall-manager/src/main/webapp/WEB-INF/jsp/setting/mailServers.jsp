<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>邮箱服务器设置</title>
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
		<div class="group left aligned" style="padding-top: 4px;">
			<button id="btnAdd" class="normal button">添加</button>
		</div>
	</div>
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<table id="maillist"></table>
		<div id="mailpager"></div>
	</div>
	<div id="mailServerDialog" style="display: none;">
		<div class="form">
			<input type="hidden" id="id" />
			<div class="field row">
				<label class="field label one half wide required">名称</label> 
				<input class="field value two wide" type="text" id="name" title="邮箱服务器名字唯一" />
			</div>
			<div class="field row">
				<label class="field label one half wide required">类型</label> 
				<select class="field value one wide" id="type"></select>
			</div>
			<div class="field row">
				<label class="field label one half wide required">账号</label> 
				<input class="field value two wide" type="text" id="account" title="请输入邮箱账号" />
			</div>
			<div class="field row" id="pswdDiv">
				<label class="field label one half wide required">密码</label> 
				<input class="field value two wide" type="password" id="password" title="请输入邮箱密码" />
			</div>
			<div class="field row" id="recvDiv">
				<label class="field label one half wide required">收件服务器</label> 
				<input class="field value two wide" type="text" id="recvServer" title="请输入收件服务器" />
				<input id="recvSSL" type="checkbox"/><label for="recvSSL">SSL</label>
				<label>端口号</label><input id="recvPort" class="field value half wide" type="text" />
			</div>
			<div class="field row" id="sendDiv">
				<label class="field label one half wide required">发件服务器</label> 
				<input class="field value two wide" type="text" id="sendServer" title="请输入发件服务器" />				
				<input id="sendSSL" type="checkbox" /><label id="lblSSL" for="sendSSL">SSL</label>
				<label id="lblSendPort">端口号</label><input id="sendPort" class="field value half wide" type="text" />
			</div>
			<div class="field row">
				<label class="field label one half wide required">显示名称</label> 
				<input class="field value two wide" type="text" id="dispName" title="显示名称是发件人名称" />
			</div>
			<div class="field row">
				<label class="field label one half wide required">状态</label> 
				<input id="using" type="radio" name="enabled" value="1" /><label for="using">启用</label>
				<input id="close" type="radio" name="enabled" checked="checked" value="0" /><label for="close">禁用</label>
			</div>
			<span class="normal hr divider"></span>
			<div class="field row" id="emailDiv" style="margin-top: 10px;">
				<label class="field label one half wide">测试邮箱地址</label> 
				<input class="field value two wide" type="text" id="mailAddres" title="请输入测试邮箱" />
				<button id="btnSendMail" class="normal button">发送</button>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		//初始化mailGrid
		var mailGrid;
		//初始化dialog
		var mailServerDialog;
		//缓存当前一行
	    var mailServerGridHelper = JqGridHelper.newOne("");
		// 实例化表单代理
		var mailFormProxy = FormProxy.newOne();
		var sendMailFormProxy = FormProxy.newOne();
		//用户名唯一标识
		var nameFlag = true;
		//临时的mailServer
		var tempMailServer = null;
		var tempSuccess = null;
		//当前操作
		var curAction = null;
		var sendFlag = false;
		
		mailFormProxy.addField({
			id : "name",
			required : true,
			rules : [ "maxLength[30]", {
				rule : function(idOrName, type, rawValue, curData) {
					if (tempMailServer == null) {
						checkName(rawValue);
					} else if (tempMailServer.name != rawValue) {
						checkName(rawValue);
					}
					return nameFlag;
				},
				message : "名称被占用！"
			} ]
		});
		mailFormProxy.addField({
			id : "account",
			required : true,
			rules : [ "maxLength[30]", "isEmail" ]
		});
		mailFormProxy.addField({
			id : "password",
			required : true,
			rules : [ "maxLength[30]" ]
		});
		mailFormProxy.addField({
			id : "recvServer",
			rules : [ "maxLength[30]", {
				rule : function(idOrName, type, rawValue, curData) {
					var type = $id("type").val();
					if (type != "exchange") {
						return !isNullOrEmpty(rawValue);
					} else {
						return true;
					}
				},
				message : "此为必须提供项"
			} ]
		});
		mailFormProxy.addField({
			id : "recvPort",
			rules : [ "isDigits", "rangeValue[0,65635]", {
				rule : function(idOrName, type, rawValue, curData) {
					var type = $id("type").val();
					if (type != "exchange") {
						return !isNullOrEmpty(rawValue);
					} else {
						return true;
					}
				},
				message : "此为必须提供项"
			} ]
		});
		mailFormProxy.addField({
			id : "sendServer",
			required : true,
			rules : [ "maxLength[30]" ]
		});
		mailFormProxy.addField({
			id : "sendPort",
			rules : [ "isDigits", "rangeValue[0,65635]", {
				rule : function(idOrName, type, rawValue, curData) {
					var type = $id("type").val();
					if (type != "exchange") {
						return !isNullOrEmpty(rawValue);
					} else {
						return true;
					}
				},
				message : "此为必须提供项"
			} ]
		});
		mailFormProxy.addField({
			id : "dispName",
			required : true,
			rules : [ "maxLength[30]" ]
		});
		
		sendMailFormProxy.addField({
			id : "mailAddres",
			required : true,
			rules : [ "maxLength[30]", "isEmail" ]
		});

		//初始化添加对话框
		function initDialogAdd() {
			$("div[aria-describedby='mailServerDialog']").find("span[class='ui-dialog-title']").html("添加邮箱服务器");
			//
			mailServerDialog = $id("mailServerDialog").dialog({
				autoOpen : false,
				height : Math.min(500, $window.height()),
				width : Math.min(750, $window.width()),
				modal : true,
				buttons : {
					"保存" : defaultMethod,
					"取消" : function() {
						clearDialog();
						mailServerDialog.dialog("close");
					}
				},
				close : function() {
					clearDialog();
				}
			});
		}

		//初始化添加对话框
		function initDialogView() {
			$("div[aria-describedby='mailServerDialog']").find("span[class='ui-dialog-title']").html("查看邮箱服务器");
			//
			mailServerDialog = $id("mailServerDialog").dialog({
				autoOpen : false,
				height : Math.min(400, $window.height()),
				width : Math.min(750, $window.width()),
				modal : true,
				buttons : {
					"修改 >" : defaultMethod,
					"关闭" : function() {
						clearDialog();
						mailServerDialog.dialog("close");
					}
				},
				close : function() {
					clearDialog();
				}
			});
		}

		//初始化修改对话框
		function initDialogEdit() {
			$("div[aria-describedby='mailServerDialog']").find("span[class='ui-dialog-title']").html("修改邮箱服务器");
			//
			mailServerDialog = $id("mailServerDialog").dialog({
				autoOpen : false,
				height : Math.min(500, $window.height()),
				width : Math.min(750, $window.width()),
				modal : true,
				buttons : {
					"保存" : defaultMethod,
					"取消" : function() {
						clearDialog();
						mailServerDialog.dialog("close");
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
				addMailServer();
			}
			if (curAction == "edit") {
				updateMailServer();
			}
			if (curAction == "view") {
				mailServerDialogUpdateOpen(tempMailServer)
			}
		}

		//初始化grid数据
		function initData() {
			//初始化商场Tab数据
			mailGrid = $("#maillist")
					.jqGrid(
							{
								url : getAppUrl("/setting/mail/servers/list/get"),
								contentType : 'application/json',
								mtype : "post",
								datatype : 'json',
								colNames : [ "id", "名称", "状态", "创建时间", "操作" ],
								colModel : [
										{
											name : "id",
											index : "id",
											hidden : true
										},
										{
											name : "name",
											index : "name",
											width : 100,
											align : "left"
										},
										{
											name : "enabled",
											index : "enabled",
											width : 100,
											align : "center",
											formatter : function(cellValue,
													option, rowObject) {
												if (cellValue) {
													return "启用";
												}
												return "禁用";
											}
										},
										{
											name : "ts",
											index : "ts",
											width : 100,
											align : "left"
										},
										{
											name : 'id',
											index : 'id',
											formatter : function(cellValue,
													option, rowObject) {
													return "&nbsp;&nbsp;<span> [<a class='item' href='javascript:void(0);' onclick='mailServerDialogShowOpen("
															+ JSON.stringify(rowObject)
															+ ")' >查看</a></span>]"
															+ "&nbsp;<span> [<a class='item' href='javascript:void(0);' onclick='mailServerDialogUpdateOpen("
															+ JSON.stringify(rowObject)
															+ ")' >修改</a>]</span>"
															+ "&nbsp;<span> [<a  class='item' href='javascript:void(0);' onclick='isDelete("
															+ JSON.stringify(rowObject)
															+ ")' >删除</a>]</span>";
											},
											width : 100,
											align : "center"
										} ],
								pager : "#mailpager", //分页div 
								loadComplete : function(gridData){
									mailServerGridHelper.cacheData(gridData);
									var callback = getCallbackAfterGridLoaded();
									if(isFunction(callback)){
										callback();
									}

								},
								ondblClickRow: function(rowId) {
									var userMap = mailServerGridHelper.getRowData(rowId)
									mailServerDialogShowOpen(userMap);
								}
							});
		}

		//打开添加dialog
		function mailServerDialogAddOpen() {
			clearDialog();
			curAction = "add";
			initDialogAdd();
			$id("recvPort").val("110");
			$id("sendPort").val("25");
			mailServerDialog.dialog("open");
		}

		//打开查看dialog
		function mailServerDialogShowOpen(obj) {
			clearDialog();
			curAction = "view";
			initDialogView();
			tempMailServer = obj;
			typeInit(obj.type);
			$id("emailDiv").hide();
			$id("pswdDiv").hide();
			$id("id").val(obj.id);
			$id("name").val(obj.name);
			$id("account").val(obj.account);
			$id("recvServer").val(obj.recvServer);
			$id("type").val(obj.type);
			if (obj.recvSSL) {
				$id("recvSSL").attr("checked", "true");
			}
			$id("recvPort").val(obj.recvPort);
			$id("sendServer").val(obj.sendServer);
			if (obj.sendSSL) {
				$id("sendSSL").attr("checked", "true");
			}
			$id("sendPort").val(obj.sendPort);
			$id("dispName").val(obj.dispName);
			if (obj.enabled) {
				$id("using").attr("checked", "true");
			} else {
				$id("close").attr("checked", "true");
			}
			$("input", "#mailServerDialog").attr("disabled", true);
			$("select", "#mailServerDialog").attr("disabled", true);
			$("textarea", "#mailServerDialog").attr("disabled", true);
			mailServerDialog.dialog("open");
		}
		
		//
		function getCallbackAfterGridLoaded(){}

		//打开修改dialog
		function mailServerDialogUpdateOpen(obj) {
			clearDialog();
			curAction = "edit";
			initDialogEdit();
			tempMailServer = obj;
			typeInit(obj.type);
			$id("id").val(obj.id);
			$id("name").val(obj.name);
			$id("account").val(obj.account);
			$id("password").val(obj.password);
			$id("recvServer").val(obj.recvServer);
			$id("type").val(obj.type);
			if (obj.recvSSL) {
				$id("recvSSL").attr("checked", "true");
			}
			$id("recvPort").val(obj.recvPort);
			$id("sendServer").val(obj.sendServer);
			if (obj.sendSSL) {
				$id("sendSSL").attr("checked", "true");
			}
			$id("sendPort").val(obj.sendPort);
			$id("dispName").val(obj.dispName);
			if (obj.enabled) {
				$id("using").attr("checked", "true");
				sendFlag = true;
				tempSuccess = obj;
			} else {
				$id("close").attr("checked", "true");
			}
			mailServerDialog.dialog("open");
		}

		//关闭所有的dialog
		function clearDialog() {
			mailFormProxy.hideMessages();
			sendMailFormProxy.hideMessages();
			nameFlag = true;
			tempMailServer = null;
			curAction = null;
			sendFlag = false;
			tempSuccess = null;
			$id("id").val("");
			$id("name").val("");
			$id("account").val("");
			$id("password").val("");
			$id("recvServer").val("");
			$id("recvSSL").removeAttr("checked");
			$id("recvPort").val("");
			$id("sendServer").val("");
			$id("sendSSL").removeAttr("checked");
			$id("sendPort").val("");
			$id("dispName").val("");
			$id("mailAddres").val("");
			$id("enable").attr("checked", "true");
			$id("emailDiv").show();
			$id("pswdDiv").show();
			$id("close").attr("checked", "true");
			$("input", "#mailServerDialog").attr("disabled", false);
			$("select", "#mailServerDialog").attr("disabled", false);
			$("textarea", "#mailServerDialog").attr("disabled", false);
		}

		//确定删除吗
		function isDelete(obj) {
			var theLayer = Layer.confirm('确定要删除？', function() {
				//
				var hintBox = Layer.progress("正在删除...");
				var ajax = Ajax.post("/setting/mail/server/delete/do");
				ajax.params({
					id : obj.id
				});
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						//
						mailGrid.jqGrid("setGridParam").trigger("reloadGrid");
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

		//检验名称唯一
		function checkName(name) {
			var ajax = Ajax.post("/setting/mail/server/name/isabel/do");
			ajax.data(name);
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				nameFlag = result.data;
			});
			ajax.go();
		}

		// 加载邮箱type
		function loadMailType() {
			var mailTypeList = getDictSelectList("mailType","","");
			loadSelectData("type", mailTypeList);
		}

		//邮箱类型change方法
		function typeChange() {
			mailFormProxy.hideMessages();
			var type = $id("type").val();
			$id("recvDiv").show();
			$id("sendSSL").show();
			$id("lblSSL").show();
			$id("lblSendPort").show();
			$id("sendPort").show();
			if (type == "exchange") {
				$id("recvDiv").hide();
				$id("sendSSL").hide();
				$id("lblSSL").hide();
				$id("lblSendPort").hide();
				$id("sendPort").hide();
			}
			recvCheckedChange();
			sendCheckedChange();
		}

		//初始化不同的类型显示不同的组件
		function typeInit(type) {
			$id("recvDiv").show();
			$id("sendSSL").show();
			$id("lblSSL").show();
			$id("lblSendPort").show();
			$id("sendPort").show();
			if (type == "exchange") {
				$id("recvDiv").hide();
				$id("sendSSL").hide();
				$id("lblSSL").hide();
				$id("lblSendPort").hide();
				$id("sendPort").hide();
			}
		}
		
		//收件服务器SSL复选框选择状态发生改变
		function recvCheckedChange(){
			var recvPort = "";
			var recvIsChecked = $id("recvSSL").is(':checked');
			var type = $id("type").val();
			if(type == "smtp"){
				if(recvIsChecked){
					recvPort = "995";
				}else{
					recvPort = "110"
				}
			}else if(type == "imap"){
				if(recvIsChecked){
					recvPort = "993";
				}else{
					recvPort = "143";
				}
			}
			$id("recvPort").val(recvPort);
		}
		
		//发件服务器SSL复选框选择状态发生改变
		function sendCheckedChange(){
			var sendPort = "";
			var sendIsChecked = $id("sendSSL").is(':checked');
			if(sendIsChecked){
				sendPort = "465";
			}else{
				sendPort = "25";
			}
			$id("sendPort").val(sendPort);
		}

		//添加服务器
		function addMailServer() {
			var vldResult = mailFormProxy.validateAll();
			if (vldResult) {
				var enabled = ParseInt(radioGet("enabled")) == 1;
				if(enabled && !sendFlag){
					Toast.show("测试邮件发送成功后，才能启用！", 2000, "warning");
					return;
				}
				var ajax = Ajax.post("/setting/mail/server/add/do");
				var data, type = $id("type").val();
				if (type == "smtp" || type == "imap") {
					data = {
						name : $id("name").val(),
						type : $id("type").val(),
						account : $id("account").val(),
						password : $id("password").val(),
						dispName : $id("dispName").val(),
						recvServer : $id("recvServer").val(),
						recvSSL : $id("recvSSL").is(':checked') ? 1 : 0,
						recvPort : ParseInt($id("recvPort").val()),
						sendServer : $id("sendServer").val(),
						sendSSL : $id("sendSSL").is(':checked') ? 1 : 0,
						sendPort : ParseInt($id("sendPort").val()),
						enabled : ParseInt(radioGet("enabled"))
					};
				}
				if (type == "Exchange") {
					data = {
						name : $id("name").val(),
						type : $id("type").val(),
						account : $id("account").val(),
						password : $id("password").val(),
						dispName : $id("dispName").val(),
						recvServer : $id("recvServer").val(),
						enabled : ParseInt(radioGet("enabled"))
					};
				}
				ajax.data(data);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						mailGrid.jqGrid("setGridParam").trigger("reloadGrid");
						tempMailServer = result.data;
						mailServerDialogShowOpen(tempMailServer);
					} else {
						Layer.msgWarning(result.message);
					}
				});
				ajax.go();
			}
		}

		//修改邮箱服务器
		function updateMailServer() {
			var vldResult = mailFormProxy.validateAll();
			if (vldResult) {
				var enabled = ParseInt(radioGet("enabled")) == 1;
				if(enabled && !sendFlag){
					Toast.show("测试邮件发送成功后，才能启用！", 2000, "warning");
					return;
				}
				var ajax = Ajax.post("/setting/mail/server/update/do");
				var data, type = $id("type").val();
				if (type == "smtp") {
					data = {
						id : $id("id").val(),
						name : $id("name").val(),
						type : $id("type").val(),
						account : $id("account").val(),
						password : $id("password").val(),
						dispName : $id("dispName").val(),
						recvServer : $id("recvServer").val(),
						recvSSL : $id("recvSSL").is(':checked') ? 1 : 0,
						recvPort : ParseInt($id("recvPort").val()),
						sendServer : $id("sendServer").val(),
						sendSSL : $id("sendSSL").is(':checked') ? 1 : 0,
						sendPort : ParseInt($id("sendPort").val()),
						enabled : ParseInt(radioGet("enabled"))
					};
				}
				if (type == "exchange") {
					data = {
						id : $id("id").val(),
						name : $id("name").val(),
						type : $id("type").val(),
						account : $id("account").val(),
						password : $id("password").val(),
						dispName : $id("dispName").val(),
						recvServer : $id("recvServer").val(),
						enabled : ParseInt(radioGet("enabled"))
					};
				}
				ajax.data(data);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						mailGrid.jqGrid("setGridParam").trigger("reloadGrid");
						tempMailServer = result.data;
						mailServerDialogShowOpen(tempMailServer);
					} else {
						Layer.msgWarning(result.message);
					}
				});
				ajax.go();
			}
		}

		function testSendMail() {
			var vldMail = mailFormProxy.validateAll();
			var vldAddress = sendMailFormProxy.validateAll();
			if (vldMail && vldAddress) {
				var hintBox = Layer.progress("正在发送测试邮件...");
				var ajax = Ajax.post("/setting/mail/server/test/send");
				var data, type = $id("type").val();
				if (type == "smtp") {
					data = {
						type : $id("type").val(),
						account : $id("account").val(),
						password : $id("password").val(),
						dispName : $id("dispName").val(),
						recvServer : $id("recvServer").val(),
						recvSSL : $id("recvSSL").is(':checked') ? 1 : 0,
						recvPort : ParseInt($id("recvPort").val()),
						sendServer : $id("sendServer").val(),
						sendSSL : $id("sendSSL").is(':checked') ? 1 : 0,
						sendPort : ParseInt($id("sendPort").val()),
						mailAddres : textGet("mailAddres")
					};
				}else if (type == "exchange") {
					data = {
						name : $id("name").val(),
						type : $id("type").val(),
						account : $id("account").val(),
						password : $id("password").val(),
						dispName : $id("dispName").val(),
						recvServer : $id("recvServer").val(),
						mailAddress : textGet("mailAddress")
					};
				}
				ajax.data(data);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						hintBox.hide();
						sendFlag = true;
						if (type == "smtp") {
							tempSuccess = {
								type : $id("type").val(),
								account : $id("account").val(),
								password : $id("password").val(),
								dispName : $id("dispName").val(),
								recvServer : $id("recvServer").val(),
								recvSSL : $id("recvSSL").is(':checked') ? 1 : 0,
								recvPort : ParseInt($id("recvPort").val()),
								sendServer : $id("sendServer").val(),
								sendSSL : $id("sendSSL").is(':checked') ? 1 : 0,
								sendPort : ParseInt($id("sendPort").val()),
							};
						}else if (type == "exchange") {
							tempSuccess = {
								type : $id("type").val(),
								account : $id("account").val(),
								password : $id("password").val(),
								dispName : $id("dispName").val(),
								recvServer : $id("recvServer").val(),
							};
						}
						Layer.msgSuccess("发送成功");
					} else {
						sendFlag = false;
						Layer.msgWarning("发送失败");
					}
				});
				ajax.always(function() {
					hintBox.hide();
				});
				ajax.go();
			}
		}
		
		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			console.log("mainWidth:" + mainWidth + ", " + "mainHeight:" + mainHeight);
			//
			var gridCtrlId = "maillist";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("mailpager").height();
			mailGrid.setGridWidth(mainWidth - 1);
			mailGrid.setGridHeight(mainHeight - headerHeight - pagerHeight - 3);
		}

		//--------------------------------------------初始化数据-----------------------------------------------------------
		$(function() {
			//页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 40,
				allowTopResize : false
			});
			//隐藏布局north分割线
			$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
			initData();
			loadMailType();
			$id("type").change(typeChange);
			initDialogAdd();
			$id("btnAdd").click(mailServerDialogAddOpen);
			$id("recvSSL").change(recvCheckedChange);
			$id("sendSSL").change(sendCheckedChange);
			$id("btnSendMail").click(testSendMail);
			//
			mailGrid.bindKeys();
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
		});
	</script>
</body>
<!-- layTpl begin -->
<!-- 模块权限静态模板 -->
<script id="mailTpl" type="text/html">

	</script>
<!-- layTpl end -->
</html>