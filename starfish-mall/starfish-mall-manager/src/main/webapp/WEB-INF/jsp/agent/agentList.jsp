<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>代理商管理</title>
</head>

<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding: 4px;">
	<div class="filter section">
		<div class="filter row">
			<label class="label">代理商编号</label> <input id="queryId" class="input" /> 
			<span class="spacer"></span> 
			<label class="label">代理商姓名</label> <input id="queryRealName" class="input" /> 
			<span class="spacer"></span> 
			<label class="label">代理商状态</label> 
			<select id="queryDisabled" class="input">
				<option>全部</option>
				<option value="0">启用</option>
				<option value="1">禁用</option>
			</select> 
			<span class="spacer two wide"></span> 
			<span class="vt divider"></span>
			<span class="spacer two wide"></span>

			<button id="btnQuery" class="button">查询</button>
		</div>

		<span class="hr divider"></span>

		<div class="filter row">
			<div class="group left aligned">
				<button id="btnAdd" class="button">添加</button>
				<button id="btnBatchDel" class="button" style="display: none;">批量删除</button>
				<button class="button" style="width: 100px; display: none;">发送站内信</button>
			</div>
			<span class="vt divider"></span>
			<button id="btnBatchImp" class="button" style="display: none;">批量导入</button>
			<button id="btnBatchExp" class="button" style="width: 100px; display: none;">批量导出</button>
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
				<label class="field label required">代理昵称</label> 
				<input type="hidden" id="agentId" class="field value" />
				<input type="text" id="nickName" class="field value one half wide" />
				<label class="field inline label required one half wide">手机号码</label> 
				<input type="text" id="phoneNo" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label required">真实姓名</label> 
				<input type="text" id="realName" class="field value one half wide" />
				<label class="field inline label required one half wide">身份证号</label> 
				<input type="text" id="idCertNo" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label required">邮箱</label>
				<input type="text" id="email" class="field value three wide" />
			</div>
			<div class="field row" style="height:60px;">
					<label class="field label" style="vertical-align: middle;">备注</label> 
					<textarea id="memo" class="field value three wide" style="height:50px;"></textarea>
			</div>
			<span id="acctDivider" class="normal hr divider"></span>
			<div id="singleAcct" style="display:none;">
				<div class="field row">
					<label class="field label required">银行名称</label> 
					<select id="bankCode" class="field value one half wide"></select>
					<label class="field inline label required one half wide">银行账户</label> 
					<input type="text" id="acctNo" class="field value one half wide" />
				</div>
				<div class="field row">
					<label class="field label required">账户类型</label>
					<div class="field group">
						<input id="typeFlag-0" type="radio" name="typeFlag" value="0" checked="checked" onChange="changeTypeFlag(false)"/>
						<label for="typeFlag-0">对私账户C型</label>
						<input id="typeFlag-1" type="radio" name="typeFlag" value="1" onChange="changeTypeFlag(true)"/>
						<label for="typeFlag-1">对公账户B型</label>
					</div>
				</div>
				<div class="field row">
					<label class="field label required">开户名</label> 
					<input type="text" id="acctName" class="field value one half wide" />
					<div id="type0" class="field group">
						<label class="field inline label required one half wide">预留号码</label> 
						<input type="text" id="acctPhoneNo" class="field value one half wide" />
					</div>
					<div id="type1" style="display: none;" class="field group">
						<label class="field inline label required one half wide">打款验证码</label> 
						<input type="text" id="vfCode" class="field value one half wide" />
					</div>
				</div>
			</div>
			<span id="divider" class="normal hr divider"></span>
			<div id="acctList" class="noBorder">
			
			</div>
			<div id="shopList" class="noBorder">
			
			</div>
		</div>
	</div>

	<div id="batchImpDialog" style="display: none;">
		<div class="form bordered">
			<div class="field row">
				<label class="field label">文件</label> <input
					class="field value two wide" name="file" type="file"
					id="impMerchXls" multiple="multiple" />
			</div>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		//添加代理商信息页面
		var addDialog;
		//修改代理商信息页面
		var updateDialog;
		//查看代理商信息页面
		var showDialog;
		//批量导入代理商信息页面
		var batchImpDialog;
		//代理处列表
		var shopTabs;
		//账户列表
		var acctTabs;
		//修改时原始数据快照
		var agentObj = "";
		//数据状态。是否改变
		var dataIsDirty = false;
		//渲染
		function renderDetail(data,fromId,toId) {
			var tplHtml = $id(fromId).html();
			var theTpl = laytpl(tplHtml);
			var htmlStr = theTpl.render(data);
			$id(toId).html(htmlStr);
			var size = getImageSizeDef("image.logo");
			$("img").attr("width",size.width);
			$("img").attr("height",size.height);
		}
		
		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			console.log("mainWidth:" + mainWidth + ", " + "mainHeight:"
					+ mainHeight);
			//
			var gridCtrlId = "theGridCtrl";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv",
					jqGridBox).height();
			var pagerHeight = $id("theGridPager").height();
			jqGridCtrl.setGridWidth(mainWidth - 1);
			jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight
					- 3);
		}

		
		//jqGrid缓存变量
		var jqGridCtrl = null;
		//
		var gridHelper = JqGridHelper.newOne("");
		
		//实例化表单代理
		var formProxy = FormProxy.newOne();
		
		//注册表单控件
		formProxy.addField({
			id : "memo",
			key : "agent.memo",
			rules : ["maxLength[250]"]
		});
		formProxy.addField({
			id : "nickName",
			key : "user.nickName",
			required : true,
			rules : ["maxLength[30]"]
		});
		formProxy.addField({
			id : "phoneNo",
			key : "user.phoneNo",
			required : true,
			rules : ["maxLength[11]","isMobile",
			         {
				rule : function(idOrName, type, rawValue, curData) {
					checkPhoneNo(rawValue);
					return !phoneFlag;
				},
				message : "手机号码已注册！"
			}]
		});
		formProxy.addField({
			id : "email",
			key : "user.email",
			required : true,
			rules : ["isEmail", {
				rule : function(idOrName, type, rawValue, curData) {
					validateEmail(rawValue);
					return !emailFlag;
				},
				message : "邮箱已被注册！"
			}]
		});
		formProxy.addField({
			id : "idCertNo",
			key : "user.idCertNo",
			required : true,
			rules : ["maxLength[18]",{
				rule : function(idOrName, type, rawValue, curData) {
					var checkStr = rawValue;
					if (checkStr == null || checkStr.length > 18) {
						return false;
					}
					var regExp = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
					return regExp.test(checkStr);
				},
				message : "身份证号码错误！"
			}]
		});
		formProxy.addField({
			id : "realName",
			key : "user.realName",
			required : true,
			rules : ["maxLength[30]"]
		});
		
		//用户账户信息
		formProxy.addField({
			id : "bankCode",
			key : "user.userAccts.bankCode",
			required : true
		});
		formProxy.addField({
			id : "bankName",
			key : "user.userAccts.bankName"
		});
		formProxy.addField({
			id : "acctNo",
			key : "user.userAccts.acctNo",
			required : true,
			rules : ["maxLength[30]"]
		});
		formProxy.addField({
			id : "acctName",
			key : "user.userAccts.acctName",
			required : true,
			rules : ["maxLength[30]"]
		});
		formProxy.addField({
			id : "typeFlag",
			key : "user.userAccts.typeFlag",
			type : "int"
		});
		formProxy.addField({
			id : "acctPhoneNo",
			key : "user.userAccts.phoneNo",
			required : true,
			rules : ["maxLength[11]","isMobile"]
		});
		formProxy.addField({
			id : "vfCode",
			key : "user.userAccts.vfCode",
			required : true,
			rules : ["maxLength[30]"]
		});
		
		//实例化查询表单代理
		var formProxyQuery = FormProxy.newOne();
		
		//注册表单控件
		formProxyQuery.addField({
			id : "queryId",
			rules : ["maxLength[30]"]
		});
		formProxyQuery.addField({
			id : "queryRealName",
			rules : ["maxLength[30]"]
		});
		formProxyQuery.addField({
			id : "queryDisabled",
			type : "int",
			required : true
		});
		
		function showLicense(isShow){
			if(isShow){
				$id("license").show();
				formProxy.addField({
					id : "licenseId",
					key : "agency.licenseId",
					type : "int",
					required : true,
					rules : ["maxLength[11]"]
				});
			}else{
				formProxy.removeField("licenseId")
				$id("license").hide();
			}
		}
		//改变账户类型
		function changeTypeFlag(flag){
			if(flag){
				hideMiscTip("acctPhoneNo");
				$id("type0").hide();
				$id("type1").show();
				formProxy.removeField("acctPhoneNo");
				formProxy.addField({
					id : "vfCode",
					key : "user.userAccts.vfCode",
					required : true,
					rules : ["maxLength[30]"]
				});
			}else{
				hideMiscTip("vfCode");
				$id("type0").show();
				$id("type1").hide();
				formProxy.removeField("vfCode");
				formProxy.addField({
					id : "acctPhoneNo",
					key : "user.userAccts.phoneNo",
					required : true,
					rules : ["maxLength[11]","isMobile"]
				});
			}
		}
		//初始化添加页面
		function initAddDialog() {
			$("#dialog input").attr("disabled",false);
			$("#dialog textarea").attr("disabled",false);
			$("#dialog textarea").val("");
			$("#dialog input[type=text]").val("");
			$("#agentId").val("");
			changeTypeFlag(false);
			showLicense(false);
			radioSet("typeFlag",0);
			$id("singleShop").show();
			$id("shopList").hide();
			$id("singleAcct").show();
			$id("acctList").hide();
			addDialog = $("#dialog").dialog({
				autoOpen : false,
				height : Math.min(450, $(window).height()),
				width : Math.min(700, $(window).width()),
				modal : true,
				title : '添加代理商',
				buttons : {
					"确定" : addAgent,
					"取消" : function() {
						addDialog.dialog("close");
					}
				},
				close : function() {
					formProxy.hideMessages();
				}
			});
		}
		//初始化修改页面
		function initUpdateDialog(obj) {
			$("#dialog input").attr("disabled",false);
			$("#dialog textarea").attr("disabled",false);
			$("#dialog input[type=text]").val("");
			$("#dialog textarea").val("");
			$("#agentId").val(obj.id);
			agentObj = obj;
			formProxy.setValue2("agent.memo",obj.memo);
			formProxy.setValue2("user.nickName",obj.user.nickName);
			formProxy.setValue2("user.phoneNo",obj.user.phoneNo);
			formProxy.setValue2("user.email",obj.user.email);
			formProxy.setValue2("user.idCertNo",obj.user.idCertNo);
			formProxy.setValue2("user.realName",obj.user.realName);
			var userAccts = obj.user.userAccts;
			//alert(JSON.encode(userAccts));
			if(userAccts){
				renderDetail(userAccts,"acctHtml","acctList");
			}
			acctTabs =  $("#acctList").tabs();
			$id("singleAcct").hide();
			$id("acctList").show();
			updateDialog = $("#dialog").dialog({
				autoOpen : false,
				height : Math.min(650, $(window).height()),
				width : Math.min(700, $(window).width()),
				modal : true,
				title : '修改代理商',
				buttons : {
					"确定" : updateAgent,
					"取消" : function() {
						updateDialog.dialog("close");
					}
				},
				close : function() {
					formProxy.hideMessages();
					acctTabs.tabs( 'destroy' );
				}
			});
		}
		//初始化查看页面
		function initShowDialog(obj) {
			$("#dialog input[type=text]").val("");
			$("#dialog textarea").val("");
			formProxy.setValue2("agent.memo",obj.memo);
			formProxy.setValue2("user.nickName",obj.user.nickName);
			formProxy.setValue2("user.phoneNo",obj.user.phoneNo);
			formProxy.setValue2("user.email",obj.user.email)
			formProxy.setValue2("user.idCertNo",obj.user.idCertNo);
			formProxy.setValue2("user.realName",obj.user.realName);
			if(obj.user.userAccts){
				renderDetail(obj.user.userAccts,"acctHtml","acctList");
			}
			acctTabs =  $("#acctList").tabs();
			$id("singleAcct").hide();
			$id("acctList").show();
			$("#dialog  input").attr("disabled",true);
			$("#dialog textarea").attr("disabled",true);
			showDialog = $("#dialog").dialog({
				autoOpen : false,
				height : Math.min(500, $(window).height()),
				width : Math.min(700, $(window).width()),
				modal : true,
				title : '查看商户',
				buttons : {
					"关闭" : function() {
						showDialog.dialog("close");
					}
				},
				close : function() {
					formProxy.hideMessages();
					acctTabs.tabs( 'destroy' );
				}
			});
		}
		
		//弹出新增代理商页面  
		function openAddDialog() {
			$id("divider").show();
			$id("acctDivider").show();
			loadBanks();
			initAddDialog();
			addDialog.dialog("open");
		}
		
		//弹出修改代理商页面  
		function openUpdateDialog(obj) {
			$id("divider").hide();
			$id("acctDivider").hide();
			initUpdateDialog(obj);
			updateDialog.dialog("open");
		}
		
		//弹出查看代理商页面  
		function openShowDialog(obj) {
			$id("divider").hide();
			$id("acctDivider").hide();
			
			initShowDialog(obj);
			showDialog.dialog("open");
		}
		
		//检验手机号码是否唯一
		var phoneFlag = false;
		function checkPhoneNo(phoneNo) {
			var id = $id("agentId").val();
			var addFlag = (id == null || id == "");
			var updateFlag = (id != null && id != "" && agentObj && agentObj.user && phoneNo != agentObj.user.phoneNo);
			if(addFlag || updateFlag){
				var postData = {
						phoneNo : phoneNo	
				};
				var ajax = Ajax.post("/agent/exist/by/phoneNo/do");
				ajax.data(postData);
				ajax.sync();
				ajax.done(function(result, jqXhr) {
						var data = result.data
						if(data && data.userFlag){
							if(data.agentFlag){
								phoneFlag = true;
								
								var layer = Layer.confirm("用户信息已存在，且已经是代理商，无需重复添加", function() {
									$id("phoneNo").val("");
									hideMiscTip("phoneNo");
									layer.hide();
								}, function() {
									$id("phoneNo").val("");
									hideMiscTip("phoneNo");
									layer.hide();
								});
							}else{
								phoneFlag = true;
								//获取用户信息
								var layer = Layer.confirm("用户信息已存在，确定要继续添加代理商吗？", function() {
									var user = data.user;
									$id("agentId").val(user.id);
									phoneFlag = false;
									agentObj.user = user;
									
									hideMiscTip("nickName");
									hideMiscTip("phoneNo");
									hideMiscTip("email");
									hideMiscTip("idCertNo");
									hideMiscTip("realName");
									
									formProxy.setValue2("user.nickName",user.nickName);
									formProxy.setValue2("user.phoneNo",user.phoneNo);
									formProxy.setValue2("user.email",user.email);
									formProxy.setValue2("user.idCertNo",user.idCertNo);
									formProxy.setValue2("user.realName",user.realName);
									formProxy.setValue2("user.memo",user.memo);
									
									$id("nickName").attr("disabled",true);
									$id("phoneNo").attr("disabled",true);
									$id("email").attr("disabled",true);
									$id("idCertNo").attr("disabled",true);
									$id("realName").attr("disabled",true);
									$id("memo").attr("disabled",true);
									layer.hide();
								}, function() {
									phoneFlag = true;
									$id("agentId").val("");
									layer.hide();
								});
							}
						}
				});
				ajax.fail(function() {
					phoneFlag = false;
				});
				ajax.go();
			}
		}
		
		var emailFlag = false;
		//邮箱是否可用
		function validateEmail(email){
			var id = $id("agentId").val();
			var addFlag = (id == null || id == "");
			var updateFlag = (id != null && id != "" && agentObj && agentObj.user && email != agentObj.user.email);
			if(addFlag || updateFlag){
				var ajax = Ajax.post("/user/exist/by/email/do");
				ajax.data(email);
				ajax.sync();
				ajax.done(function(result, jqXhr) {
					emailFlag = result.data;
				});
				ajax.go();
			}
		}
		
		// 判断区是否显示
		function isShowTown(town){
	 		var value = $id("town").val();
	 		return value != null;
		}
		
		//新增代理商
		function addAgent() {
			var vldResult = formProxy.validateAll();
			if (!vldResult) {
				return;
			}
			var postData = {
				id : intGet("agentId"),
		        memo : textGet("memo"),
		        user : {
		        	id : intGet("agentId"),
		        	nickName : textGet("nickName"),		
			    	phoneNo : textGet("phoneNo"),
			    	realName : textGet("realName"),
			    	idCertNo : textGet("idCertNo"),			
			    	email : textGet("email"),
			    	userAccts : [
						{
							bankCode : textGet("bankCode"),
							bankName : $("#bankCode option:selected").text(),			
							acctNo : textGet("acctNo"),			
							acctName : textGet("acctName"),
							typeFlag : radioGet("typeFlag"),
							phoneNo : textGet("acctPhoneNo"),
							vfCode : textGet("vfCode")
						}    
			    	]
		        }
			};
			//alert(JSON.encode(postData));
			var ajax = Ajax.post("/agent/add/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					addDialog.dialog("close");
					Layer.msgSuccess(result.message);
					queryAgent();
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
		}
		
		//修改代理商
		function updateAgent() {
			var vldResult = formProxy.validate("memo") && formProxy.validate("nickName") && formProxy.validate("email") &&
			 formProxy.validate("phoneNo") && formProxy.validate("idCertNo") &&formProxy.validate("realName");
			if (!vldResult) {
				return;
			}
			var postData = {
				id : intGet("agentId"),
		        memo : textGet("memo"),
		        user : {
		        	id : intGet("agentId"),
		        	nickName : textGet("nickName"),		
			    	phoneNo : textGet("phoneNo"),
			    	realName : textGet("realName"),
			    	idCertNo : textGet("idCertNo"),			
			    	email : textGet("email")
		        }	
			};
			var ajax = Ajax.post("/agent/update/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					updateDialog.dialog("close");
					Layer.msgSuccess(result.message);
					queryAgent();
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
		}
		
		//启用禁用代理商
		function disableAgent(obj){
			var title="";
			if(obj.disabled){
				title = "启用";
			}else{
				title = "禁用";
			}
			var layer = Layer.confirm("确定要"+title+"该代理商吗？", function() {
				var postData = {
						id : obj.id,
						disabled : !obj.disabled,
						memo : obj.memo
				};
			    var loaderLayer = Layer.loading("正在提交数据...");
				var ajax = Ajax.post("/agent/update/do");
				ajax.data(postData);
			    ajax.done(function(result, jqXhr){
			    	if(result.type== "info"){
						jqGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid"); 
						Layer.msgSuccess(result.message);
					}else{
						Layer.msgWarning(result.message);
					}
			  });
			   ajax.always(function() {
					//隐藏等待提示框
					loaderLayer.hide();
				});
				ajax.go();
				layer.hide();
			});
		}
		
		//删除单个
		function deleteById(id){
			var theLayer ={};
			theLayer= Layer.confirm('代理商下的账户、代理处、个人信息等数据将全部删除，确定要删除该代理商吗？', function(){
			var postData = { id:parseInt(id)  };
		  	var ajax = Ajax.post("/agent/delete/do");
		  	    ajax.data(postData);
			    ajax.done(function(result, jqXhr){
			    	if(result.type== "info"){
						theLayer.hide();
						Layer.msgSuccess(result.message);
						queryAgent();
					}else{
						theLayer.hide();
						Layer.msgWarning(result.message);
					}
			 });
			ajax.go();
			});
		  }
		
		/* function batchExpMerchant(){
		}
		
		function batchImpMerchant() {
			sendFileUpload("impMerchXls", {
				url : getAppUrl("/file/upload"),
				dataType : "json",
				//自定义数据
				formData : {
					usage : "merchant.extract"
				},
				done : function(e, result) {
					//done方法就是上传完毕的回调函数，其他回调函数可以自行查看api  
					//注意result要和jquery的ajax的data参数区分，这个对象包含了整个请求信息  
					//返回的数据在result.result中，假设我们服务器返回了一个json对象 
					var resultInfo = result.result;
					console.log(resultInfo);
					var resultData = resultInfo.data || {};
					var files = resultData.files || [];
					for (var i = 0; i < files.length; i++) {
						var file = files[i];
						if(file.type== "info"){
							Layer.msgSuccess(file.message);
							batchImpDialog.dialog("close");
							queryAgent();
						}else{
							Layer.msgWarning(file.message);
						}
					}
				},
				fail : function(e, data) {
					console.log(data);
				}
			});
		} */
		
		function queryAgent(){ 
			var vldResult = formProxyQuery.validateAll();
			if (!vldResult) {
				return;
			}
			var id = intGet("queryId");
			var realName = textGet("queryRealName");
			var disabled = intGet("queryDisabled");
			//加载jqGridCtrl
			jqGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode({"id":id,"realName":realName,"disabled":disabled},true)},page:1}).trigger("reloadGrid");
		}
		
		//批量删除多个
		/* function batchDeleteByIds(){
			var ids=jqGridCtrl.jqGrid("getGridParam","selarrrow");
			if(ids==""){
				Layer.info("请选择要删除的数据！");
			}else{
				var theLayer = {};
				theLayer = Layer.confirm('代理商下的账户、代理处、个人信息等数据将全部删除，确定要删除该代理商吗？', function(){
				var hintBox = Layer.progress("正在删除...");
				var postData = { ids : ids  };
			  	var ajax = Ajax.post("/agent/delete/by/ids");
			  	    ajax.data(postData);
				    ajax.done(function(result, jqXhr){
				    	if(result.type== "info"){
							theLayer.hide();
							Layer.msgSuccess(result.message);
							queryAgent();
						}else{
							theLayer.hide();
							Layer.msgWarning(result.message);
						}
				 });
			    ajax.always(function() {
					hintBox.hide();
				});
				ajax.go();
				});
			}
		  } */
		
		function fileToUploadFileIcon(fileId) {
			var fileUuidToUpdate = $id("logoUuid").val();
			if(isNoB(fileUuidToUpdate)){
				var formData = {
					usage : "image.logo",
					subUsage : "agency"
				};
			}else{
				var formData ={
					update : true,
					uuid : fileUuidToUpdate
				};
			}
			sendFileUpload(fileId, {
				url : getAppUrl("/file/upload"),
				dataType : "json",
				//自定义数据
				formData :formData,
				done : function(e, result) {
					var resultInfo = result.result;
					if(resultInfo.type=="info"){
						$id("logoUuid").val(resultInfo.data.files[0].fileUuid);
						$id("logoUsage").val(resultInfo.data.files[0].fileUsage);
						$id("logoPath").val(resultInfo.data.files[0].fileRelPath);
						$("#logoImgshop").attr("src",resultInfo.data.files[0].fileBrowseUrl+"?"+new Date().getTime());
						}
				},
				fail : function(e, data) {
					console.log(data);
				},
				noFilesHandler : function() {
					Layer.msgWarning("请选择或更换图片");
				},
				fileNamesChecker : function(fileNames) {
					fileNames = fileNames || [];
					for (var i = 0; i < fileNames.length; i++) {
						if (!isImageFile(fileNames[i])) {
							Layer.msgWarning("只能上传图片文件");
							return false;
						}
					}
					return true;
				}
			});
		}
		
		function loadBanks(){
			var ajax = Ajax.post("/comn/bank/selectList/get");
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("bankCode", result.data);
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		//
		function getCallbackAfterGridLoaded(){
			
		}
		
		//------------------------初始化-------------------------
		$(function() {
			//页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 100,
				allowTopResize : false
			});
			//隐藏布局north分割线
			$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
			
			jqGridCtrl= $("#theGridCtrl").jqGrid({
			      url : getAppUrl("/agent/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      height : "100%",
				  width : "100%",
			      colNames : ["id", "代理商编号", "代理商姓名","联系电话","状态","代理处数量","注册时间 "," 操作"],  
			      colModel : [{name:"id", index:"id", hidden:true},  
			                  {name:"id", index:"id", width:100, align:'center'}, 
			                  {name:"user.realName", index:"user.realName", width:200, align:'left'}, 
			                  {name:"user.phoneNo", index:"user.phoneNo", width:100, align:'right'}, 
			                  {name:"disabled", index:"disabled", width:50, align:'left', formatter : function (cellValue) {
									return cellValue==false ? '启用' : '禁用';
								}},  
			                  {name:"agencies.length", index:"agencies.length", width:100, align:"left"},
			                  {name:"user.regTime", index:"user.regTime", width:200, align:"left"}, 
			                  {name:'id', index:'id', formatter : function (cellValue,option,rowObject) {
								var s = "<span> [<a class='item' href='javascript:void(0);' onclick='openShowDialog("
								+ JSON.stringify(rowObject)
								+ ")' >查看</a>]   [<a class='item' href='javascript:void(0);' onclick='openUpdateDialog("
								+ JSON.stringify(rowObject)
								+ ")' >修改</a>]  ";
								if(rowObject.disabled==false){
									return s+"[<a class='item' href='javascript:void(0);' onclick='disableAgent("
									+ JSON.stringify(rowObject)
									+ ")' >禁用</a>]";	
								}else{
									return s+"[<a class='item' href='javascript:void(0);' onclick='disableAgent("
									+ JSON.stringify(rowObject)
									+ ")' >启用</a>]";
								}
							  },width:200,align:"center"}
			                  ],  
				 //rowList:[10,20,30],
				 multiselect:true,
				 multikey:'ctrlKey',
				 pager : "#theGridPager",
				 loadComplete : function(gridData){ // JqGridHelper缓存最新的grid数据
					 gridHelper.cacheData(gridData);
						var callback = getCallbackAfterGridLoaded();
						if (isFunction(callback)) {
							callback();
						}
					},
					ondblClickRow: function(rowId) {
						var agentMap = gridHelper.getRowData(rowId)
						openShowDialog(agentMap);
					}
				});
				
				//空函数，在弹出框消失后重写调用
				function getCallbackAfterGridLoaded() {
				}
			
			//绑定查询
			$id("btnQuery").click(queryAgent);

			$id("btnAdd").click(openAddDialog);
			//$id("btnBatchDel").click(batchDeleteByIds);
			//$id("btnBatchImp").click(openImpDialog);
			
			$id("phoneNo").blur(function() {
				checkPhoneNo(phoneNo);
			});
			
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
		});
	</script>
</body>
<script type="text/html" id="acctHtml" title="账户信息模版">
	<ul>
	{{# for(var i = 0, len = d.length; i < len; i++){ }}
		<li><a href="#accts-{{ d[i].id }}">{{ d[i].bankName }}</a></li>
	{{# } }}
	</ul>
	{{# for(var i = 0, len = d.length; i < len; i++){ }}
	<div id="accts-{{ d[i].id }}">
	<div class="form">
			<div class="field row">
				<label class="field label">银行名称</label> 
				<input type="text" value="{{ d[i].bankName }}" class="field value one half wide" disabled=disabled/>
				<label class="field inline label one half wide">银行账户</label> 
				<input type="text" value="{{ d[i].acctNo }}" class="field value one half wide" disabled=disabled/>
			</div>
			<div class="field row">
				<div class="field group">
					<label class="field label">账户类型</label>
					{{# if(d[i].typeFlag){ }}
						<label>对公账户B型</label>
					{{# }else{ }}
						<label>对私账户C型</label>
					{{# } }}
				</div>
			</div>
			<div class="field row">
				<label class="field label">开户名</label> 
				<input type="text" value="{{ d[i].acctName }}" class="field value one half wide"  disabled=disabled/>
				{{# if(d[i].typeFlag){ }}
					<label class="field inline label one half wide">打款验证码</label> 
					<input type="text" value="{{ d[i].vfCode }}" class="field value one half wide"  disabled=disabled/>
				{{# }else{ }}
					<label class="field inline label one half wide">预留号码</label> 
					<input type="text" value="{{ d[i].phoneNo }}" class="field value one half wide"  disabled=disabled/>
				{{# } }}
			</div>
	</div>
	</div>
	{{# } }}
		
</script>
<script type="text/html" id="shopHtml" title="代理处信息模版">
	<ul>
	{{# for(var i = 0, len = d.length; i < len; i++){ }}
		<li><a href="#tabs-{{ d[i].id }}">{{ d[i].name }}</a></li>
	{{# } }}
	</ul>
	{{# for(var i = 0, len = d.length; i < len; i++){ }}
	<div id="tabs-{{ d[i].id }}">
	<div class="form">
		<div class="field row">
			<label class="field label">代理处名称</label> 
			<input type="text" value="{{ d[i].name }}" class="field value two wide" disabled=disabled/>
		</div>
		<div class="field row">
			<label class="field label">联系人</label> 
			<input type="text" value="{{ d[i].linkMan }}" class="field value" disabled=disabled/>
			<label class="field inline label">联系电话</label> 
			<input type="text" value="{{ d[i].phoneNo }}" class="field value" disabled=disabled/>
		</div>
		<div class="field row">
			<label class="field inline label">企业代理处</label>
			<div class="field group">
				<input type="radio" value="1" {{# if(d[i].entpFlag){ }} checked="checked" {{# } }} disabled=disabled/>
				<label>是</label>
				<input type="radio" value="0" {{# if(!d[i].entpFlag){ }} checked="checked" {{# } }} disabled=disabled/>
				<label>否</label>
			</div>
			{{# if(d[i].entpFlag){ }}
			<label class="field inline label">营业执照</label> 
			<input type="text" value="{{ d[i].licenseId }}" class="field value" disabled=disabled/>
			{{# } }}
		</div>
		<div class="field row">
			<label class="field label wide">代理处Logo</label>
		    <img height="80px" width="120px" src="{{ d[i].fileBrowseUrl }}"/>
		</div>
		<div class="field row">
			<label class="field label wide">所在地</label>
			<input type="text" value="{{ d[i].regionName }}" class="field value three wide" disabled=disabled/>
		</div>
		<div class="field row">
			<label class="field label wide">街道地址</label>
			<input type="text" value="{{ d[i].street }}" class="field value three wide" maxlength="60" title="请输入街道地址" disabled=disabled/>
		</div>
		<div class="field row" style="height:60px;">
			<label class="field label">经营范围</label> 
			<textarea class="field value three wide" style="height:50px;width:360px;" disabled=disabled>{{ d[i].bizScope }}</textarea>
		</div>
	</div>
	</div>
	{{# } }}
		
</script>
</html>