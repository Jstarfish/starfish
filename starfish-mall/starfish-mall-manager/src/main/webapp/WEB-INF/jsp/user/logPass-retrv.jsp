<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>找回登录密码</title>
<style type="text/css">
.center {
	margin:0 auto;
	margin-top: 200px;
}
</style>
</head>
<body id="rootPanel">
	<div style="width: 600px; height: auto;border: 1px solid gray;padding: 4px;" class="center">
		<div id = "firstStep" class="form" >
			<div class="caption row">设置密码<a href="javascript:;" style="float: right;font-size: 12px" id="btnLogin"><span>立马登录</span></a></div>
			<div class="field row">
				<label class="field label one half wide required" for="phoneNo">手机</label> 
				<input class="field value two wide " type="text" id="phoneNo" size="18" maxlength="11" value=""/>
			</div>
			<div class="field row">	 
				<label class="field label one half wide required" for="chkCode">验证码</label> 
				<input class="field value one wide" type="text" id="chkCode" name="chkCode" maxlength="4" onchange="validImgVfCode()">
				<span id="codeResult" style="display: inline-block;"></span>
				<img class="absMiddle" id="codeImage" style="height: 28px;" src="" />
				<span id="codeMessage">&nbsp;</span>
			</div>
			<div class="field row">
				<label class="field label one half wide required">短信验证码</label> 
				<input class="field value one wide" type="text" id="smsVfCode" title="请输入验证码" />
				<button class="normal button two wide" id="btnSendVfCode" disabled="disabled">获取验证码</button>
			</div>
			<div class="field row">
				<label class="field label one half wide required">设置新密码</label>
				<input class="field value two wide" type="password" id="password" title="请输入登录密码" />
			</div>
			<div class="field row">
				<label class="field label one half wide required">确认新密码</label> 
				<input class="field value two wide" type="password" id="confirmPassword" title="请确认登录密码" />
			</div>
			<div class="action row">
				<button class="normal button" id="btnSubmit">重置</button>
				<button class="normal button" id="btnCancel">取消</button>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<!-- 加密专用js -->
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/bigint.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/barrett.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/rsa.js"></script>
	<script type="text/javascript" src="<%=appBaseUrl%>/js/encrypt/get"></script>
	<script type="text/javascript">
	// 实例化表单代理
	var formProxy = FormProxy.newOne();
	//验证码用途
	var usage = "logPass";
	//验证码标志
	var vfCodeFlag = false;
	formProxy.addField({
		id : "phoneNo",
		required : true,
		rules : ["maxLength[11]","isMobile"]
	});
	formProxy.addField({
		id : "chkCode",
		required : true,
		rules : ["eqLength[4]"],
		messageTargetId : "codeMessage"
	});
	formProxy.addField({
		id : "smsVfCode",
		required : true,
		rules : ["eqLength[6]", "isNum" ],
		messageTargetId : "btnSendVfCode"
	});
	formProxy.addField({
		id : "password",
		required : true,
		rules : ["isPwd[true]",{
			rule : function(idOrName, type, rawValue, curData) {
				var value = $id("confirmPassword").val();
				if(!isNoB(value)){
					formProxy.validate("confirmPassword");
				}
				return true;
			}
		}]
	});
	formProxy.addField({
		id : "confirmPassword",
		required : true,
		rules : [{
			rule : function(idOrName, type, rawValue, curData) {
				var value = $id("password").val();
				if(isNoB(value)){
					return true;
				}else{
					return rawValue==value;
				}
			},
			message : "输入密码不一致！"
		}]
	});
	
	//验证图形验证码
	function validImgVfCode(){
		if(formProxy.validate("chkCode")){
			var ajax = Ajax.post("/notify/img/verf/code/valid");
			var verfCode = $id("chkCode").val();
			ajax.data(verfCode);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data;
					if(data){
						$id("btnSendVfCode").removeAttr("disabled");
						$id("codeResult").removeClass("ui-icon ui-icon-circle-close");
						$id("codeResult").addClass("ui-icon ui-icon-circle-check");
					}else{
						$id("codeResult").removeClass("ui-icon ui-icon-circle-check");
						$id("codeResult").addClass("ui-icon ui-icon-circle-close");
						$id("btnSendVfCode").prop("disabled",true);
					}
				}else{
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
	}
	
	//提交
	function submit(){
		var vldResult = formProxy.validateAll();
		if(vldResult){
			var hintBox = Layer.progress("正在提交数据...");
			var ajax = Ajax.post("/user/logPass/reset/do");
			var phoneNo = formProxy.getValue("phoneNo");
			var chkCode = formProxy.getValue("chkCode");
			var smsCode = formProxy.getValue("smsVfCode");
			var password = formProxy.getValue("password");
			password = encryptStr(password);
			ajax.data({
				"phoneNo" : phoneNo,
				"chkCode" : chkCode,
				"smsCode" : smsCode,
				"password" : password
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data;
					if(data){
						Toast.show("密码重置成功",2000,"info");
						var pageUrl = makeUrl(getAppUrl("/user/login/jsp"), {phoneNo : phoneNo});
						setPageUrl(pageUrl);
					}else{
						Toast.show(result.message,2000,"error");
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.always(function() {
				hintBox.hide();
			});
			ajax.go();
		}	
	}
	
	function cancel(){
		$("input").val("");
	}
	//发送短信验证码
	function sendSmsVfCode(){
		var result = formProxy.validate("phoneNo")&&formProxy.validate("chkCode");
		if(!result){
			return;
		}
		var data = {
				phoneNo : formProxy.getValue("phoneNo"),
				chkCode : formProxy.getValue("chkCode")
		}
		var hintBox = Layer.progress("正在发送短信...");
		var ajax = Ajax.post("/notify/sms/send/for/logPass/do");
		ajax.data(data);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var data = result.data;
				if(data){
					for (i = 1; i <= 60 * 2; i++) {
						window.setTimeout("update( " + i + ") ", i * 1000);
					}
					Layer.msgSuccess("短信验证码已发送至您的手机，请注意查收！");
				}else{
					Layer.msgWarning("发送失败，请重新发送！");
				}
			}else{
				changeImageCode();
				Layer.warning(result.message);
			}
		});
		ajax.always(function() {
			hintBox.hide();
		});
		ajax.go();
	}
	
	//按钮倒计时
    function update(num) {
        var secs = 60 * 2;
        if (num == secs) {
        	$id("btnSendVfCode").html("获取验证码");
        	$id("btnSendVfCode").removeAttr("disabled");
        }
        else {
            printnr = secs - num;
            $id("btnSendVfCode").html( printnr + "秒后可以再次获取");
            $id("btnSendVfCode").attr("disabled", true);
        }
    }
	//更换图形验证码
	function changeImageCode() {
		var imgObj = $id("codeImage").get(0);
		imgObj.src = getAppUrl() + "/xutil/checkCode.do?uid=" + genUniqueStr();
	}
	
	//返回登录页面
	function goLogin() {
		var phoneNo = formProxy.getValue("phoneNo");
		var pageUrl = makeUrl(getAppUrl("/user/login/jsp"), {phoneNo : phoneNo});
		setPageUrl(pageUrl);
	}
	
	//-----------------------------------------初始化------------------------------------------
	$(function(){
		$id("btnSendVfCode").click(sendSmsVfCode);
		$id("btnSubmit").click(submit);
		$id("codeImage").click(changeImageCode);
		$id("btnCancel").click(cancel);
		$id("btnLogin").click(goLogin);
		
		var phoneNo = extractUrlParams().phoneNo;
		if(phoneNo && app.consts.sysAdminName != phoneNo){
			$id("phoneNo").val(phoneNo);
		}
		
		$id("confirmPassword").bind('keydown', function (e) { 
			var key = e.which; 
			if (key == 13) { 
				e.preventDefault(); 
				submit();
			} 
			}); 
		changeImageCode();
	});
	</script>
</body>
<!-- layTpl begin -->
<!-- layTpl end -->
</html>