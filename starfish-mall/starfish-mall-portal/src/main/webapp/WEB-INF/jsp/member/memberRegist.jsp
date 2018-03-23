<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>新用户注册</title>
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
			<div class="caption row">
				<span>我已经注册，现在就</span><a href="<%=appBaseUrl%>/user/login/jsp">登录>></a>
			</div>
			<div class="field row">
				<label class="field label one half wide">邮箱地址</label> 
				<input class="field value two wide" type="text" id="email" title="请输入邮箱地址" />
			</div>
			<div class="field row">
				<label class="field label one half wide required">验证手机</label>
				<input class="field value two wide" type="text" id="phoneNo" title="请输入验证手机" />
			</div>
			<div class="field row">
				<label class="field label one half wide required">短信验证码</label> 
				<input class="field value one wide" type="text" id="smsCode" title="请输入短信验证码" />
				<button class="normal button two wide" id="btnSendCode">获取短信验证码</button>
			</div>
			<div class="field row">
				<label class="field label one half wide">&nbsp;</label> 
				<input type="checkbox" id="protocol" checked="checked">
				<label for="protocol" id="lblProtocol">同意《中远联合服务协议》</label>
			</div>
			<div class="action row">
				<button class="normal button" id="btnGoSecond">下一步</button>
			</div>
		</div>
		<div id = "secondStep" class="form" style="display: none;">
			<div class="field row">
				<label class="field label one half wide">昵称</label> 
				<input class="field value two wide" type="text" id="nickName" title="请输入昵称" />
			</div>
			<div class="field row">
				<label class="field label one half wide required">设置登录密码</label>
				<input class="field value two wide" type="password" id="pswd" title="请输入登录密码" />
			</div>
			<div class="field row">
				<label class="field label one half wide required">确认登录密码</label> 
				<input class="field value two wide" type="password" id="rePswd" title="请确认登录密码" />
			</div>
			<div class="field row">
				<label class="field label one half wide required">设置支付密码</label>
				<input class="field value two wide" type="password" id="payPswd" title="请输入支付密码" />
			</div>
			<div class="field row">
				<label class="field label one half wide required">确认支付密码</label>
				<input class="field value two wide" type="password" id="rePayPswd" title="请确认支付密码" />
			</div>
			<div class="action row">
				<button class="normal button" id="btnGoThrid">下一步</button>
			</div>
		</div>
		<div id = "thridStep" style="display: none;">
			恭喜，注册成功！
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
		<!-- 加密专用js -->
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/bigint.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/barrett.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/rsa.js"></script>
	<script type="text/javascript" src="<%=appBaseUrl%>/js/encrypt/get"></script>
	<script type="text/javascript">
	// 实例化第一个表单代理
	var firstFormProxy = FormProxy.newOne();
	//实例化第二个表单代理
	var secondFormProxy = FormProxy.newOne();
	//手机号是否可用标识
	var phoneFlag = false;
	//邮箱是否可用标识
	var emailFlag = true;
	
	//验证码用途
	var usage = "regist";
	//验证码标志
	var smsCodeFlag = false;
	firstFormProxy.addField({
		id : "email",
		rules : [ "maxLength[60]", "isEmail",{
			rule : function(idOrName, type, rawValue, curData) {
				if(isNoB(rawValue)){
					emailFlag = true;
				}else{
					validateEmail(rawValue);	
				}
				return emailFlag;
			},
			message : "邮箱被占用！"
		} ]
	});
	firstFormProxy.addField({
		id : "phoneNo",
		required : true,
		rules : ["isMobile" , {
			rule : function(idOrName, type, rawValue, curData) {
				validatePhone(rawValue);
				return phoneFlag;
			},
			message : "手机号被占用！"
		} ]
	});
	firstFormProxy.addField({
		id : "smsCode",
		required : true,
		rules : [ "eqLength[6]", "isNum" ]
	});
	
	secondFormProxy.addField({
		id : "nickName",
		rules : [ "maxLength[30]"]
	});
	secondFormProxy.addField({
		id : "pswd",
		required : true,
		rules : [ "isPwd[true]", {
			rule : function(idOrName, type, rawValue, curData) {
				var rePswd = $id("rePswd").val();
				if(!isNoB(rePswd)){
					secondFormProxy.validate("rePswd");
				}
				return true;
			},
		}]
	});
	secondFormProxy.addField({
		id : "rePswd",
		required : true,
		rules : [ "isPwd[true]", {
			rule : function(idOrName, type, rawValue, curData) {
				var pswd = $id("pswd").val();
				var flag = true;
				if(!isNoB(pswd)){
					flag = rawValue == pswd;
				}
				return flag;
			},
			message : "两次输入的密码不一致！"
		}]
	});
	secondFormProxy.addField({
		id : "payPswd",
		required : true,
		rules : [ "isPwd[true]", {
			rule : function(idOrName, type, rawValue, curData) {
				var rePayPswd = $id("rePayPswd").val();
				if(!isNoB(rePayPswd)){
					secondFormProxy.validate("rePayPswd");
				}
				return true;
			},
		}]
	});
	secondFormProxy.addField({
		id : "rePayPswd",
		required : true,
		rules : [ "isPwd[true]", {
			rule : function(idOrName, type, rawValue, curData) {
				var payPswd = $id("payPswd").val();
				var flag = true;
				if(!isNoB(payPswd)){
					flag = rawValue == payPswd;
				}
				return flag;
			},
			message : "两次输入的密码不一致！"
		}]
	});
	
	
	//手机号码是否可用
	function validatePhone(phoneNo){
		var ajax = Ajax.post("/user/exist/by/phoneNo/do");
		ajax.data(phoneNo);
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			phoneFlag = result.data == false;
		});
		ajax.go();
	}
	
	//邮箱是否可用
	function validateEmail(email){
		var ajax = Ajax.post("/user/exist/by/email/do");
		ajax.data(email);
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			emailFlag = result.data == false;
		});
		ajax.go();
	}
	
	//跳转第二步
	function goSecond(){
		var vldResult = firstFormProxy.validateAll();
		if($id("protocol").is(':checked')){
			hideMiscTip("lblProtocol");
		}else{
			showErrorTip("lblProtocol","请接受服务条款");
			return;
		}
		if(vldResult){
			validCode();
			if(smsCodeFlag){
				hideMiscTip("smsCode");
				showSecond();
			}else{
				showErrorTip("smsCode","验证码错误");
			}
		}	
	}
	
	//跳转第三步
	function goThird(){
		var vldResult = secondFormProxy.validateAll();
		if(vldResult){
			var hintBox = Layer.progress("正在提交数据...");
			var ajax = Ajax.post("/member/regist/do");
			ajax.data({
				email : firstFormProxy.getValue("email"),
				phoneNo : firstFormProxy.getValue("phoneNo"),
				nickName : secondFormProxy.getValue("nickName"),
				password : encryptStr(secondFormProxy.getValue("pswd")),
				payPassword : encryptStr(secondFormProxy.getValue("payPswd")),
			});
			ajax.done(function(result, jqXhr) {
				var data = result.data;
				if(data){
					showThird();
				}else{
					Layer.msgWarning("注册失败，请重试！");
				}
			});
			ajax.always(function() {
				hintBox.hide();
			});
			ajax.go();
		}
	}
	
	//服务条款change事件
	function protocolChange(){
		if($id("protocol").is(':checked')){
			hideMiscTip("lblProtocol");
		}
	}
	
	//发送验证码
	function sendCode(){
		var vldResult = firstFormProxy.validate("phoneNo");
		if(vldResult){
			var ajax = Ajax.post("/member/regist/sms/code/send");
			var phoneNo = $id("phoneNo").val();
			ajax.data(phoneNo);
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				var data = result.data;
				if(data){
					for (i = 1; i <= 60 * 2; i++) {
						window.setTimeout("update( " + i + ") ", i * 1000);
					}
					Layer.msgSuccess("短信验证码已发送至您的手机，请注意查收！");
				}else{
					Layer.msgWarning("发送失败，请重新发送！");
				}
			});
			ajax.go();
		}
	}
	
	//短信验证码验证
	function validCode(){
		var ajax = Ajax.post("/notify/sms/verf/code/valid");
		var phoneNo = $id("phoneNo").val();
		var smsCode = $id("smsCode").val();
		ajax.data({
			phoneNo : phoneNo,
			smsCode : smsCode,
			usage : usage
		});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			var data = result.data;
			if(data){
				smsCodeFlag = true;
			}else{
				smsCodeFlag = false;
			}
		});
		ajax.go();
	}
	
	//按钮倒计时
    function update(num) {
        var secs = 60 * 2;
        if (num == secs) {
        	$id("btnSendCode").html("获取短信验证码");
        	$id("btnSendCode").removeAttr("disabled");
        }
        else {
            printnr = secs - num;
            $id("btnSendCode").html( printnr + "秒后可以再次获取");
            $id("btnSendCode").attr("disabled", true);
        }
    }
	
	//div层显示隐藏切换
	function showFirst(){
		$id("firstStep").show();
		$id("secondStep").hide();
		$id("thridStep").hide();
	}
	function showSecond(){
		$id("firstStep").hide();
		$id("secondStep").show();
		$id("thridStep").hide();
	}
	function showThird(){
		$id("firstStep").hide();
		$id("secondStep").hide();
		$id("thridStep").show();
	}
	
	//-----------------------------------------初始化------------------------------------------
	$(function(){
		$id("protocol").change(protocolChange);
		$id("btnGoSecond").click(goSecond);
		$id("btnSendCode").click(sendCode);
		$id("btnGoThrid").click(goThird);
	});
	</script>
</body>
<!-- layTpl begin -->
<!-- layTpl end -->
</html>