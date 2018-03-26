<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>模板设置</title>
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
	<div id="mainPanel" class="ui-layout-center">
		<div id="tabs" class="noBorder">
			<ul>
				<li><a href="#mailTemplate" id="mail"><span>邮件模板</span></a></li>
				<li><a href="#smsTemplate" id="sms"><span>短信模板</span></a></li>
			</ul>
			<div id="mailTemplate">
				<div  class="ui-layout-north">
					<div class="filter section">
						<div class="filter row">
							<div class="group left aligned">
								<button id="btnMailAdd" class="normal button">添加</button>
							</div>
							<div class="group right aligned">
								<label class=" normal label">邮件主题</label> 
								<input id="paramMailSubject" type="text" class="normal input one half wide" /><span class="spacer"></span>
								<button id="btnMailSearch" class="normal button">查询</button>
							</div>
						</div>
					</div>
				</div>
				<table id="maillist"></table>
				<div id="mailpager"></div>
			</div>
			<div id="smsTemplate">
				<div  class="ui-layout-north">
					<div class="filter section">
						<div class="filter row">
							<div class="group left aligned">
								<button id="btnSmsAdd" class="normal button">添加</button>
							</div>
							<div class="group right aligned">
								<label class=" normal label">模板名称</label> 
								<input id="paramSmsName" type="text" class="normal input one half wide" /><span class="spacer"></span>
								<button id="btnSmsSearch" class="normal button">查询</button>
							</div>
						</div>
					</div>
				</div>
				<table id="smslist"></table>
				<div id="smspager"></div>
			</div>
		</div>
	</div>
	<div id="mailDialog" style="display: none;">
		<div class="form">
			<input type="hidden" id="mailId" />
			<div class="field row">
				<label class="field label one wide required">模板名称</label> 
				<input class="field value one half wide" type="text" id="mailName" title="模板名称唯一" />
			</div>
			<div class="field row">
				<label class="field label one wide required">code</label> 
				<input class="field value one half wide" type="text" id="mailCode" title="code唯一" />
			</div>
			<div class="field row">
				<label class="field label one wide required">模型</label> 
				<select class="field value one half wide" id="mailModelType"></select>
			</div>
			<div class="field row">
				<label class="field label one wide required">邮件主题</label> 
				<input class="field value one half wide" type="text" id="mailSubject" title="请输入邮件主题" />
			</div>
			<div class="field row">
				<label class="field label one wide required">类型</label>
				<input type="radio" name="type" id="txt" value="txt" checked="checked"/><label for="txt">纯文本邮件</label>
				<input type="radio" name="type" id="html" value="html"/><label for="html">HTML邮件</label>
			</div>
			<div class="field row" style="height:58px;">
				<label class="field label one wide">描述</label>
				<textarea class="field value three wide" id="mailDesc" style="height:50px;" title="请输入描述"></textarea>
				<label id="warnMsg"></label>
			</div>
			<div class="field row">
				<label class="field label one wide required">模板内容</label>
				<div id="mail-tplModel-var-trigger" class="labeled text">
					<div class="label">
						模型变量
					</div>
					<div class="text">
						选择插入
					</div>
					<div class="dropdown">
						&nbsp;
					</div>
				</div>
				<ul id="mail-tplModel-var-list" class="popup panel simple-dropdown-menu" style="list-style: none;margin:0;" ></ul>
			</div>
			<div id="mailContentTips" class="field row" style="padding-left:80px;">
				<textarea class="field value three half wide ckeditor" id="mailContent" title="请输入模板内容"></textarea>
			</div>
		</div>
	</div>
	<div id="smsDialog"></div>
	<div id="previewDialog" title="预览"></div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		//定义grid
		var mailGrid;
		var smsGrid;
		//定义dialog
		var mailDialog;
		var smsDialog;
		var previewDialog;
		//缓存当前邮件行数据
		var mailGridHelper = JqGridHelper.newOne("");
		 //缓存当前短信行数据
	    var smsGridHelper = JqGridHelper.newOne("");
		// 实例化表单代理
		var mailFormProxy = FormProxy.newOne();
		var smsFormProxy = FormProxy.newOne();
		var mailSearchFormProxy = FormProxy.newOne();
		var smsSearchFormProxy = FormProxy.newOne();
		//保存临时对象
		var tempMail = null;
		var tempSms = null;
		var tempSample = null;
		var tempTplModel = null;
		//名字唯一标识
		var nameFlag = true;
		//code唯一标识
		var codeFlag = true;
		//定义编辑器
		var ckeditor = CKEDITOR.replace("mailContent");
		//当前操作
		var curMailAction;
		var curSmsAction;
		//当前的tab
		var currentTab = "mail";
		//模板类型变量列表
		var mailTplModelVarList = null;
		var smsTplModelVarList = null;
		var jqTabsCtrl;
		
		mailFormProxy.addField({
			id : "mailName",
			required : true,
			rules : [ "maxLength[30]", {
				rule : function(idOrName, type, rawValue, curData) {
					if (tempMail == null) {
						checkMailName(rawValue);
					} else if (tempMail.name != rawValue) {
						checkMailName(rawValue);
					}
					return nameFlag;
				},
				message : "名称被占用！"
			} ]
		});
		mailFormProxy.addField({
			id : "mailCode",
			required : true,
			rules : [ "maxLength[30]", {
				rule : function(idOrName, type, rawValue, curDate) {
					if (tempMail == null) {
						checkMailCode(rawValue);
					} else if (tempMail.code != rawValue) {
						checkMailCode(rawValue);
					}
					return codeFlag;
				},
				message : "code被占用！"
			} ]
		});
		mailFormProxy.addField({
			id : "mailModelType",
			required : true
		});
		mailFormProxy.addField({
			id : "mailSubject",
			required : true,
			rules : [ "maxLength[30]" ]
		});
		mailFormProxy.addField({
			id : ckeditor.id,
			get : function(idOrName, type, curData, asRawVal) {
				return ckeditor.getData();
			},
			rules : [ {
				rule : function(idOrName, type, rawValue, curData) {
					// 不计算文本附带的特殊标记占用的8个字符位
					if (rawValue.length == 0) {
						return false;
					}
					return true;
				},
				message : "此项必须提供项"
			},{
				rule : function(idOrName, type, rawValue, curData) {
					if (rawValue.length > 1000) {
						return false;
					}
					return true;
				},
				message : "不能超过1000个字符长度"
			} ],
			messageTargetId : "mailContentTips"
		});
		mailFormProxy.addField({
			id : "mailDesc",
			rules : [ "maxLength[30]" ]
		});
		
		function getCallbackAfterGridLoaded(){}

		//初始化grid数据
		function initData() {
			//初始化mailTab数据
			mailGrid = $id("maillist")
					.jqGrid(
							{
								url : getAppUrl("/setting/mail/templates/list/get"),
								contentType : 'application/json',
								mtype : "post",
								datatype : 'json',
								colNames : [ "id", "名称", "主题", "类型", "操作" ],
								colModel : [
										{
											name : "id",
											index : "id",
											hidden : true
										},
										{
											name : "name",
											index : "name",
											align : "left"
										},
										{
											name : "subject",
											index : "subject",
											align : "left"
										},
										{
											name : "type",
											index : "type",
											align : "center",
											formatter : function(cellValue,
													option, rowObject) {
												if (cellValue == "txt") {
													return "纯文本邮件";
												}
												if (cellValue == "html") {
													return "HTML邮件";
												}
												return cellValue;
											}
										},
										{
											name : 'id',
											index : 'id',
											formatter : function(cellValue,
													option, rowObject) {
												return "&nbsp;&nbsp;<span> [<a class='item' href='javascript:void(0);' onclick='mailDialogViewOpen("
														+ JSON
																.stringify(rowObject)
														+ ")' >查看</a></span>]"
														+ "&nbsp;&nbsp;&nbsp;<span> [<a class='item' href='javascript:void(0);' onclick='mailDialogEditOpen("
														+ JSON
																.stringify(rowObject)
														+ ")' >修改</a>]</span>"
														+ "&nbsp;&nbsp;&nbsp;<span> [<a  class='item' href='javascript:void(0);' onclick='deleteMail("
														+ JSON
																.stringify(rowObject)
														+ ")' >删除</a>]</span>";
											},
											align : "center"
										} ],
								pager : "#mailpager",//分页div
								multiselect : true,// 定义是否可以多选
								multikey:'ctrlKey', 
								loadComplete : function(gridData){
									mailGridHelper.cacheData(gridData);
									var callback = getCallbackAfterGridLoaded();
									if(isFunction(callback)){
										callback();
									}

								},
								ondblClickRow: function(rowId) {
									var userMap = mailGridHelper.getRowData(rowId)
									mailDialogViewOpen(userMap);
								}
							});
			//初始化smsTab数据
			smsGrid = $id("smslist")
					.jqGrid(
							{
								url : getAppUrl("/setting/sms/templates/list/get"),
								contentType : 'application/json',
								mtype : "post",
								datatype : 'json',
								colNames : [ "id", "名称", "内容", "操作" ],
								colModel : [
										{
											name : "id",
											index : "id",
											hidden : true
										},
										{
											name : "name",
											index : "name",
											align : "left"
										},
										{
											name : "content",
											index : "content",
											align : "left"
										},
										{
											name : 'id',
											index : 'id',
											formatter : function(cellValue,
													option, rowObject) {
												return "&nbsp;&nbsp;<span> [<a class='item' href='javascript:void(0);' onclick='smsDialogViewOpen("
														+ JSON
																.stringify(rowObject)
														+ ")' >查看</a></span>]"
														+ "&nbsp;&nbsp;&nbsp;<span> [<a class='item' href='javascript:void(0);' onclick='smsDialogEditOpen("
														+ JSON
																.stringify(rowObject)
														+ ")' >修改</a>]</span>"
														+ "&nbsp;&nbsp;&nbsp;<span> [<a  class='item' href='javascript:void(0);' onclick='deleteSms("
														+ JSON
																.stringify(rowObject)
														+ ")' >删除</a>]</span>";
											},
											align : "center"
										} ],
								pager : "#smspager",//分页div 
								multiselect : true,// 定义是否可以多选
								multikey:'ctrlKey', 
								loadComplete : function(gridData){
									smsGridHelper.cacheData(gridData);
									var callback = getCallbackAfterGridLoaded();
									if(isFunction(callback)){
										callback();
									}

								},
								ondblClickRow: function(rowId) {
									var userMap = smsGridHelper.getRowData(rowId)
									smsDialogViewOpen(userMap);
								}
							});
		}

		//初始化预览dialog
		function initPreviewDialog() {
			previewDialog = $id("previewDialog").dialog({
				autoOpen : false,
				height : Math.min(500, $window.height()),
				width : Math.min(1000, $window.width()),
				modal : true,
				buttons : {
					"关闭" : function() {
						previewDialog.dialog("close");
					}
				},
				close : function() {
				}
			});
		}

		//初始化添加邮件模板dialog
		function initMailDialogAdd() {
			$("div[aria-describedby='mailDialog']").find("span[class='ui-dialog-title']").html("添加邮件模板");
			mailDialog = $id("mailDialog").dialog({
				autoOpen : false,
				height : Math.min(600, $window.height()),
				width : Math.min(750, $window.width()),
				modal : true,
				buttons : {
					"预览" : simulateAjaxPriview,
					"保存" : selectMailMethod,
					"取消" : function() {
						clearDialog();
						mailDialog.dialog("close");
					}
				},
				close : function() {
					clearDialog();
				}
			});
		}
		//初始化查看邮件模板dialog
		function initMailDialogView() {
			$("div[aria-describedby='mailDialog']").find("span[class='ui-dialog-title']").html("查看邮件模板");
			mailDialog = $id("mailDialog").dialog({
				autoOpen : false,
				height : Math.min(600, $window.height()),
				width : Math.min(750, $window.width()),
				modal : true,
				buttons : {
					"预览" : simulateAjaxPriview,
					"修改 >" : selectMailMethod,
					"关闭" : function() {
						clearDialog();
						mailDialog.dialog("close");
					}
				},
				close : function() {
					clearDialog();
				}
			});
		}
		//初始化修改邮件模板dialog
		function initMailDialogEdit() {
			$("div[aria-describedby='mailDialog']").find("span[class='ui-dialog-title']").html("修改邮件模板");
			mailDialog = $id("mailDialog").dialog({
				autoOpen : false,
				height : Math.min(600, $window.height()),
				width : Math.min(750, $window.width()),
				modal : true,
				buttons : {
					"预览" : simulateAjaxPriview,
					"保存" : selectMailMethod,
					"取消" : function() {
						clearDialog();
						mailDialog.dialog("close");
					}
				},
				close : function() {
					clearDialog();
				}
			});
		}

		//初始化添加短信模板dialog
		function initSmsDialogAdd() {
			$("div[aria-describedby='smsDialog']").find("span[class='ui-dialog-title']").html("添加短信模板");
			smsDialog = $id("smsDialog").dialog({
				autoOpen : false,
				height : Math.min(500, $window.height()),
				width : Math.min(600, $window.width()),
				modal : true,
				buttons : {
					"预览" : simulateAjaxPriview,
					"保存" : selectSmsMethod,
					"取消" : function() {
						clearDialog();
						smsDialog.dialog("close");
					}
				},
				close : function() {
					clearDialog();
				}
			});
		}
		//初始化修改短信模板dialog
		function initSmsDialogEdit() {
			$("div[aria-describedby='smsDialog']").find("span[class='ui-dialog-title']").html("修改短信模板");
			smsDialog = $id("smsDialog").dialog({
				autoOpen : false,
				height : Math.min(500, $window.height()),
				width : Math.min(600, $window.width()),
				modal : true,
				buttons : {
					"预览" : simulateAjaxPriview,
					"保存" : selectSmsMethod,
					"取消" : function() {
						clearDialog();
						smsDialog.dialog("close");
					}
				},
				close : function() {
					clearDialog();
				}
			});
		}
		//初始化查看短信模板dialog
		function initSmsDialogView() {
			$("div[aria-describedby='smsDialog']").find(
					"span[class='ui-dialog-title']").html("查看短信模板");
			smsDialog = $id("smsDialog").dialog({
				autoOpen : false,
				height : Math.min(500, $window.height()),
				width : Math.min(600, $window.width()),
				modal : true,
				buttons : {
					"预览" : simulateAjaxPriview,
					"修改 >" : selectSmsMethod,
					"关闭" : function() {
						clearDialog();
						smsDialog.dialog("close");
					}
				},
				close : function() {
					clearDialog();
				}
			});
		}

		//
		function selectMailMethod() {
			if (curMailAction == "add") {
				addMailTemplate();
			}
			if (curMailAction == "edit") {
				updateMailTemplate();
			}
			if (curMailAction == "view") {
				mailDialogEditOpen(tempMail);
			}
		}

		//
		function selectSmsMethod() {
			if (curSmsAction == "add") {
				addSmsTemplate();
			}
			if (curSmsAction == "edit") {
				updateSmsTemplate();
			}
			if (curSmsAction == "view") {
				smsDialogEditOpen(tempSms);
			}
		}

		//
		function mailDialogAddOpen() {
			clearDialog();
			curMailAction = "add";
			initMailDialogAdd();
			$id("mail-tplModel-var-trigger").show();
			ckeditor.setData('');
			mailDialog.dialog("open");
		}
		//
		function mailDialogViewOpen(obj) {
			clearDialog();
			curMailAction = "view";
			initMailDialogView();
			tempMail = obj;
			ckeditor.setData(tempMail.content);
			$id("mailName").val(tempMail.name);
			$id("mailSubject").val(tempMail.subject);
			$id("mailDesc").val(tempMail.desc);
			$id("mailCode").val(tempMail.code);
			$id("mailModelType").val(tempMail.modelCode);
			$id(obj.type).attr("checked", "true");
			$("input", "#mailDialog").attr("disabled", true);
			$("select", "#mailDialog").attr("disabled", true);
			$("textarea", "#mailDialog").attr("disabled", true);
			$id("mail-tplModel-var-trigger").hide();
			initVarListByCode(tempMail.modelCode);
			mailDialog.dialog("open");
		}
		//
		function mailDialogEditOpen(obj) {
			clearDialog();
			curMailAction = "edit";
			initMailDialogEdit();
			tempMail = obj;
			CKEDITOR.instances.mailContent.setData(tempMail.content);
			$id("mailName").val(tempMail.name);
			$id("mailSubject").val(tempMail.subject);
			$id(tempMail.type).attr("checked", "true");
			$id("mailId").val(tempMail.id);
			$id("mailCode").val(tempMail.code);
			$id("mailTs").val(tempMail.ts);
			$id("mailDesc").val(tempMail.desc);
			$id("mailModelType").val(tempMail.modelCode);
			$("input", "#mailDialog").attr("disabled", false);
			$("select", "#mailDialog").attr("disabled", false);
			$("textarea", "#mailDialog").attr("disabled", false);
			$id("mail-tplModel-var-trigger").show();
			initVarListByCode(tempMail.modelCode);
			mailDialog.dialog("open");
		}
		//
		function smsDialogAddOpen() {
			clearDialog();
			curSmsAction = "add";
			initSmsDialogAdd();
			$id("sms-tplModel-var-trigger").show();
			smsDialog.dialog("open");
		}
		//
		function smsDialogViewOpen(obj) {
			clearDialog();
			curSmsAction = "view";
			initSmsDialogView();
			tempSms = obj;
			$id("smsName").val(tempSms.name);
			$id("smsContent").val(tempSms.content);
			$id("smsCode").val(tempSms.code);
			$id("smsDesc").val(tempSms.desc);
			$id("smsModelType").val(tempSms.modelCode);
			$("input", "#smsDialog").attr("disabled", true);
			$("select", "#smsDialog").attr("disabled", true);
			$("textarea", "#smsDialog").attr("disabled", true);
			$id("sms-tplModel-var-trigger").hide();
			initVarListByCode(tempSms.modelCode);
			smsDialog.dialog("open");
		}
		//
		function smsDialogEditOpen(obj) {
			clearDialog();
			curSmsAction = "edit";
			initSmsDialogEdit();
			tempSms = obj;
			$id("smsId").val(tempSms.id);
			$id("smsCode").val(tempSms.code);
			$id("smsTs").val(tempSms.ts);
			$id("smsDesc").val(tempSms.desc);
			$id("smsName").val(tempSms.name);
			$id("smsContent").val(tempSms.content);
			$id("smsModelType").val(tempSms.modelCode);
			$("input", "#smsDialog").attr("disabled", false);
			$("select", "#smsDialog").attr("disabled", false);
			$("textarea", "#smsDialog").attr("disabled", false);
			$id("sms-tplModel-var-trigger").show();
			initVarListByCode(tempSms.modelCode);
			smsDialog.dialog("open");
		}
		//删除邮件模板
		function deleteMail(obj) {
			var theLayer = Layer.confirm('确定要删除？', function() {
				//
				var hintBox = Layer.progress("正在删除...");
				var ajax = Ajax.post("/setting/mail/template/delete/do");
				ajax.params({
					id : obj.id
				});
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						//
						mailGrid.jqGrid().trigger("reloadGrid");
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
		//删除短信模板
		function deleteSms(obj) {
			var theLayer = Layer.confirm('确定要删除？', function() {
				//
				var hintBox = Layer.progress("正在删除...");
				var ajax = Ajax.post("/setting/sms/template/delete/do");
				ajax.params({
					id : obj.id
				});
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						//
						smsGrid.jqGrid().trigger("reloadGrid");
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

		//添加短信模板
		function addSmsTemplate() {
			var vldResult = smsFormProxy.validateAll();
			if (vldResult) {
				var ajax = Ajax.post("/setting/sms/template/add/do");
				var nameVal = $id("smsName").val();
				var contentVal = $id("smsContent").val();
				var codeVal = $id("smsCode").val();
				var descVal = $id("smsDesc").val();
				var modelTypeVal = $id("smsModelType").val();
				data = {
					name : nameVal,
					content : contentVal,
					code : codeVal,
					modelCode : modelTypeVal,
					desc : descVal
				};
				ajax.data(data);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
					} else {
						Layer.msgWarning(result.message);
					}
					smsGrid.jqGrid().trigger("reloadGrid");
					tempSms = result.data;
					smsDialogViewOpen(tempSms);
				});
				ajax.go();
			}
		}

		//添加邮件模板
		function addMailTemplate() {
			var vldResult = mailFormProxy.validateAll();
			if (vldResult) {
				var ajax = Ajax.post("/setting/mail/template/add/do");
				var content = ckeditor.getData();
				data = {
					name : $id("mailName").val(),
					subject : $id("mailSubject").val(),
					type : FormProxy.fieldAccessors.radio.get("type"),
					content : content,
					code : $id("mailCode").val(),
					modelCode : $id("mailModelType").val(),
					desc : $id("mailDesc").val()
				};
				ajax.data(data);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
					} else {
						Layer.msgWarning(result.message);
					}
					mailGrid.jqGrid().trigger("reloadGrid");
					tempMail = result.data;
					mailDialogViewOpen(tempMail);
				});
				ajax.go();
			}
		}

		//检验短信模板名称是否唯一
		function checkSmsName(name) {
			var ajax = Ajax.post("/setting/sms/template/name/isabel/do");
			ajax.data(name);
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				nameFlag = result.data;
			});
			ajax.go();
		}

		//检验邮箱模板名称是否唯一
		function checkMailName(name) {
			var ajax = Ajax.post("/setting/mail/template/name/isabel/do");
			ajax.data(name);
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				nameFlag = result.data;
			});
			ajax.go();
		}

		//检验邮箱模板code是否唯一
		function checkMailCode(code) {
			var ajax = Ajax.post("/setting/mail/template/code/isabel/do");
			ajax.data(code);
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				codeFlag = result.data;
			});
			ajax.go();
		}

		//检验短信模板code是否唯一
		function checkSmsCode(code) {
			var ajax = Ajax.post("/setting/sms/template/code/isabel/do");
			ajax.data(code);
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				codeFlag = result.data;
			});
			ajax.go();
		}

		// 隐藏所有错误提示
		function clearDialog() {
			curMailAction = null;
			curSmsAction = null;
			tempMail = null;
			tempSms = null;
			tempSample = null;
			nameFlag = true;
			codeFlag = true;
			mailFormProxy.hideMessages();
			smsFormProxy.hideMessages();
			$id("smsName").val("");
			$id("smsContent").val("");
			$id("smsCode").val("");
			$id("smsDesc").val("");
			$id("smsModelType").val("");
			$id("mailName").val("");
			$id("mailSubject").val("");
			$id("mailCode").val("");
			$id("mailDesc").val("");
			$id("mailModelType").val("");
			$id("txt").attr("checked", "true");
			$("input", "#mailDialog").attr("disabled", false);
			$("select", "#mailDialog").attr("disabled", false);
			$("textarea", "#mailDialog").attr("disabled", false);
			$("input", "#smsDialog").attr("disabled", false);
			$("select", "#smsDialog").attr("disabled", false);
			$("textarea", "#smsDialog").attr("disabled", false);
			mailTplModelVarList.html("");
			smsTplModelVarList.html("");
		}

		//修改短信模板
		function updateSmsTemplate() {
			var vldResult = smsFormProxy.validateAll();
			if (vldResult) {
				var ajax = Ajax.post("/setting/sms/template/update/do");
				data = {
					id : $id("smsId").val(),
					code : $id("smsCode").val(),
					desc : $id("smsDesc").val(),
					name : $id("smsName").val(),
					modelCode : $id("smsModelType").val(),
					content : $id("smsContent").val()
				};
				ajax.data(data);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
					} else {
						Layer.msgWarning(result.message);
					}
					smsGrid.jqGrid().trigger("reloadGrid");
					tempSms = result.data;
					smsDialogViewOpen(tempSms);
				});
				ajax.go();
			}
		}
		//修改邮箱模板
		function updateMailTemplate() {
			console.log("tempMail.id:"+tempMail.id);
			console.log("tempMail.code:"+tempMail.code);
			console.log("tempMail.desc:"+tempMail.desc);
			console.log("tempMail.name:"+tempMail.name);
			console.log("tempMail.subject:"+tempMail.subject);
			console.log("tempMail.modelCode:"+tempMail.modelCode);
			console.log("tempMail.type:"+tempMail.type);
			console.log("tempMail.content:"+tempMail.content);
			var vldResult = mailFormProxy.validateAll();
			if (vldResult) {
				var ajax = Ajax.post("/setting/mail/template/update/do");
				var content = ckeditor.getData();
				data = {
					id : $id("mailId").val(),
					code : $id("mailCode").val(),
					desc : $id("mailDesc").val(),
					name : $id("mailName").val(),
					subject : $id("mailSubject").val(),
					modelCode : $id("mailModelType").val(),
					type : FormProxy.fieldAccessors.radio.get("type"),
					content : content
				};
				ajax.data(data);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
					} else {
						Layer.msgWarning(result.message);
					}
					mailGrid.jqGrid().trigger("reloadGrid");
					tempMail = result.data;
					mailDialogViewOpen(tempMail);
				});
				ajax.go();
			}
		}

		//邮箱模板查询
		function searchMail() {
			var name = $id("paramMailSubject").val();
			mailGrid.jqGrid("setGridParam", {
				postData : {
					filterStr : JSON.encode({
						"name" : name
					}, true)
				},
				page : 1
			}).trigger("reloadGrid");
		}

		//短信模板查询
		function searchSms() {
			var name = $id("paramSmsName").val();
			smsGrid.jqGrid("setGridParam", {
				postData : {
					filterStr : JSON.encode({
						"name" : name
					}, true)
				},
				page : 1
			}).trigger("reloadGrid");
		}

		//
		function initSmsTpl() {
			var smsTplHtml = $id("smsTpl").html();
			var htmlStr = laytpl(smsTplHtml).render({});
			$id("smsDialog").html(htmlStr);
			$id("smsModelType").change(changeTplModel);
			initSmsDialogAdd();
			smsFormProxy.addField({
				id : "smsName",
				required : true,
				rules : [ "maxLength[30]", {
					rule : function(idOrName, type, rawValue, curData) {
						if (tempSms == null) {
							checkSmsName(rawValue);
						} else if (tempSms.name != rawValue) {
							checkSmsName(rawValue);
						}
						return nameFlag;
					},
					message : "名称被占用！"
				} ]
			});
			smsFormProxy.addField({
				id : "smsCode",
				required : true,
				rules : [ "maxLength[30]", {
					rule : function(idOrName, type, rawValue, curData) {
						if (tempSms == null) {
							checkSmsCode(rawValue);
						} else if (tempSms.code != rawValue) {
							checkSmsCode(rawValue);
						}
						return codeFlag;
					},
					message : "code被占用！"
				} ]
			});
			smsFormProxy.addField({
				id : "smsModelType",
				required : true
			});
			smsFormProxy.addField({
				id : "smsContent",
				required : true,
				rules : [ "maxLength[1000]" ]
			});
			smsFormProxy.addField({
				id : "smsDesc",
				rules : [ "maxLength[250]" ]
			});
		}

		//初始化模板模型列表
		function initMailTplModels() {
			var ajax = Ajax.post("/setting/tpl/models/get/by/code");
			ajax.params({
				code : "mail"
			});
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("mailModelType", result.data);
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}

		//初始化模板模型列表
		function initSmsTplModels() {
			var ajax = Ajax.post("/setting/tpl/models/get/by/code");
			ajax.params({
				code : "sms"
			});
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("smsModelType", result.data);
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}

		//模板模型选择事件
		function changeTplModel() {
			var modelCode = "";
			if (currentTab == "mail") {
				modelCode = $id("mailModelType").val();
				if (modelCode == "") {
					tempSample = null;
					tempTplModel = null;
					mailTplModelVarList.html("");
				} else {
					getTplModelByCode(modelCode);
					if (tempTplModel != null) {
						renderTplModelVarList(tempTplModel);
					}
				}
			} else {
				modelCode = $id("smsModelType").val();
				if (modelCode == "") {
					tempSample = null;
					tempTplModel = null;
					smsTplModelVarList.html("");
				} else {
					getTplModelByCode(modelCode);
					if (tempTplModel != null) {
						renderTplModelVarList(tempTplModel);
					}
				}
			}
		}

		//查看模板时，初始化模型变量
		function initVarListByCode(modelCode) {
			getTplModelByCode(modelCode);
			if (tempTplModel != null) {
				renderTplModelVarList(tempTplModel);
			}
		}

		//根据code获得模板模型
		function getTplModelByCode(code) {
			var ajax = Ajax.post("/setting/tpl/model/get/by/code");
			ajax.sync();
			ajax.params({
				code : code
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data;
					tempTplModel = data;
					tempSample = data.sample;
				} else {
					tempSample = null;
					tempTplModel = null;
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}

		//生成模型变量表达式菜单项
		function renderTplModelVarList(modelInfo) {
			//获取模板内容
			var tplHtml = $id("tplVarItemsTemplate").html();

			//生成/编译模板
			var htmlTpl = laytpl(tplHtml);

			//根据模板和数据生成最终内容
			var htmlText = htmlTpl.render(modelInfo);

			if (currentTab == "mail") {
				//使用生成的内容
				mailTplModelVarList.html(htmlText);
				//设置属性并绑定事件
				var varList = modelInfo["varList"];
				mailTplModelVarList.find("li").each(function(idx, dom) {
					$(dom).data("expr", varList[idx].expr);
					$(dom).data("desc", varList[idx].desc);
					$(dom).click(insertTplModelVar);
				});
			} else {
				//使用生成的内容
				smsTplModelVarList.html(htmlText);
				//设置属性并绑定事件
				var varList = modelInfo["varList"];
				smsTplModelVarList.find("li").each(function(idx, dom) {
					$(dom).data("expr", varList[idx].expr);
					$(dom).data("desc", varList[idx].desc);
					$(dom).click(insertTplModelVar);
				});
			}
		}

		//把选择的模型变量表达式插入到模板编辑中
		function insertTplModelVar() {
			var jqThis = $(this);
			if (currentTab == "mail") {
				var strToInsert = jqThis.data("expr");
				var content = ckeditor.getData();
				ckeditor.setData(content+strToInsert);
				mailTplModelVarList.hide();
			} else {
				var jqTextArea = $id("smsContent");
				var strToInsert = jqThis.data("expr");
				//
				jqTextArea.replaceSelectedText(strToInsert, "select");
				//
				smsTplModelVarList.hide();
			}
		}

		//模拟Ajax到Controller层获取预览结果（预览）
		function simulateAjaxPriview() {
			var content = "";
			var sample = tempSample;
			var type = "txt";
			if (currentTab == "mail") {
				content = ckeditor.getData();
				type = FormProxy.fieldAccessors.radio.get("type");
			} else {
				content = $id("smsContent").val();
			}
			var ajax = Ajax.post("/setting/template/preview/do");
			ajax.data({
				content : content,
				sample : sample,
				type : type
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data;
					$id("previewDialog").html(data);
					previewDialog.dialog("open");
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			console.log("mainWidth:" + mainWidth + ", " + "mainHeight:" + mainHeight);
			//
			var tabsCtrlWidth = mainWidth;
			var tabsCtrlHeight = mainHeight;
			jqTabsCtrl.width(tabsCtrlWidth);
			var tabsHeaderHeight = $(">[role=tablist]", jqTabsCtrl).height();
			var tabsPanels = $(">[role=tabpanel]", jqTabsCtrl);
			tabsPanels.width(tabsCtrlWidth - 4 * 2);
			tabsPanels.height(tabsCtrlHeight - tabsHeaderHeight - 30);
			//
			var gridCtrlId,jqGridBox,headerHeight,pagerHeight;
			if(currentTab=="mail"){
				gridCtrlId = "maillist";
				jqGridBox = $("#gbox_" + gridCtrlId);
				headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
				pagerHeight = $id("mailpager").height();
			}else if(currentTab=="sms"){
				gridCtrlId = "smslist";
				jqGridBox = $("#gbox_" + gridCtrlId);
				headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
				pagerHeight = $id("smspager").height();
			}
			//
			if(mailGrid){
				mailGrid.setGridWidth(tabsCtrlWidth - 4 * 2);
				mailGrid.setGridHeight(tabsCtrlHeight - tabsHeaderHeight - 81 - pagerHeight - headerHeight);
			}
			//
			if(smsGrid){
				smsGrid.setGridWidth(tabsCtrlWidth - 4 * 2);
				smsGrid.setGridHeight(tabsCtrlHeight - tabsHeaderHeight - 81 - pagerHeight - headerHeight);
			}
		}

		//----------------------------------------初始化----------------------------------------------------

		$(function() {
			//页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 60,
				allowTopResize : false
			});
			//隐藏布局north分割线
			$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
			jqTabsCtrl = $id("tabs").tabs();
			// 针对不同tab页绑定不同的保存操作
			$id("tabs").on("tabsactivate", function(event, ui) {
				// 绑定保存操作
				currentTab = ui.newTab.context.id;
			});
			$id("mailModelType").change(changeTplModel);
			initMailDialogAdd();
			initData();
			initSmsTpl();
			initMailTplModels();
			initSmsTplModels();
			$id("btnMailAdd").click(mailDialogAddOpen);
			$id("btnSmsAdd").click(smsDialogAddOpen);
			$id("btnMailSearch").click(searchMail);
			$id("btnSmsSearch").click(searchSms);	
			mailTplModelVarList = $id("mail-tplModel-var-list");
			var mailPopupProxy = bindToPopup("#mail-tplModel-var-trigger", mailTplModelVarList, 0, "left");
			smsTplModelVarList = $id("sms-tplModel-var-list");
			var smsPopupProxy = bindToPopup("#sms-tplModel-var-trigger", smsTplModelVarList, 0, "left");
			initPreviewDialog();
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
		});
	</script>
</body>
<!-- layTpl begin -->
<!-- 短信模板 -->
<script id="smsTpl" type="text/html">
		<div class="form">
			<input type="hidden" id="smsId" />
			<div class="field row">
				<label class="field label one wide required">模板名称</label> 
				<input class="field value one half wide" type="text" id="smsName" title="模板名称唯一" />
			</div>
			<div class="field row">
				<label class="field label one wide required">code</label> 
				<input class="field value one half wide" type="text" id="smsCode" title="code唯一" />
			</div>
			<div class="field row">
				<label class="field label one wide required">模型</label> 
				<select class="field value one half wide" id="smsModelType"></select>
				<div id="sms-tplModel-var-trigger" class="labeled text">
					<div class="label">
						模型变量
					</div>
					<div class="text">
						选择插入
					</div>
					<div class="dropdown">
						&nbsp;
					</div>
				</div>
				<ul id="sms-tplModel-var-list" class="popup panel simple-dropdown-menu" style="list-style: none;margin:0;" ></ul>
			</div>
			<div class="field row" style="height:120px;">
				<label class="field label one wide required">模板内容</label> 
				<textarea class="field value three wide" id="smsContent" title="请输入模板内容" style="height:100px;" />
			</div>
			<div class="field row">
				<label class="field label one wide">描述</label> 
				<textarea class="field value three wide" id="smsDesc" title="请输入描述" style="height:100px;" />
			</div>
		</div>
	</script>
<!-- 模型变量表达式菜单项模板 -->
<script type="text/html" id="tplVarItemsTemplate">
		{{# var varList = d["varList"]; }}
		{{# for(var i=0, len=varList.length; i<len ; i++) {  }}
			{{# var varInfo = varList[i]; }}
		<li class="item">
			{{varInfo.desc}} <span style="color:#AAAAAA;">{{varInfo.expr}}</span>
		</li>
		{{# } }}
</script>
<!-- layTpl end -->
</html>