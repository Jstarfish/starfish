<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@include file="/WEB-INF/jsp/include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <!-- Vendor Specific -->
    <!-- Set renderer engine for 360 browser -->
    <meta name="renderer" content="webkit">
    <link rel="shortcut icon" href="<%=resBaseUrl%>/image/favicon.ico" type="image/x-icon" />
    <!-- Cache Meta -->
    <meta http-equiv="Expires" content="0">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Pragma" content="no-cache">
    <!--<link rel="shortcut icon" href="<%=resBaseUrl%>/image/favicon.ico" type="image/x-icon" />-->
    <link rel="stylesheet" href="<%=resBaseUrl%>/css-app/main.css" />
    <!--[if lt IE 9]>
    <script type="text/javascript" src="js/html5/html5shiv.js"></script>
    <![endif]-->
    <script type="text/javascript">
		var __appBaseUrl = "<%=appBaseUrl%>";
		//快捷方式获取应用url
		function getAppUrl(url){
			url = url || "";
			return url.indexOf("http") == 0 ?  url : __appBaseUrl + url;
		}
		var __resBaseUrl = "<%=resBaseUrl%>";
		//快捷方式获取资源url
		function getResUrl(url){
			url = url || "";
			return url.indexOf("http") == 0 ?  url : __resBaseUrl + url;
		}
	</script>
    <title>亿投车吧网</title>
</head>
<body class="page-login page-reg">
<div class="page-wrapper">
	<div class="header">
        <div class="page-width">
            <a class="logo" href="<%=appBaseUrl%>/"><img src="<%=resBaseUrl%>/image/logo.png" alt="车logo" /></a>
            <span class="logo-title">欢迎注册</span>
            <span class="reg-top-login">
                如果您已是会员，可以直接<a class="red" href="<%=appBaseUrl%>/user/login/jsp">登录</a>
            </span>
        </div>
    </div>
	<div class="content">
		<div class="page-width">
			<div class="login reg-section">
				<div class="login-reg-cont">
					<div class="reg-cont">
						<dl class="labels labels1 name">
							<dt>
								<label class="label required">昵称：</label>
							</dt>
							<dd>
								<input class="inputx inputx-reg" type="text" id="nickName"
									placeholder="昵称" maxlength="30" /> <span class="red"
									id="nickNameTip"></span>
							</dd>
						</dl>
						<dl class="labels labels1 regPhoneNo">
							<dt>
								<label class="label required">手机号码：</label>
							</dt>
							<dd>
								<label class="nk-label name-label"></label> <input
									class="inputx inputx-reg" type="text" id="regPhoneNo"
									maxlength="11" placeholder="手机号" /> <span class="red"
									id="phoneNoTip"></span>
							</dd>
						</dl>
						<dl class="labels labels1 password">
							<dt>
								<label class="label required">密码：</label>
							</dt>
							<dd>
								<label class="nk-label keyword-label"></label> <input
									class="inputx inputx-reg" type="password" id="password"
									maxlength="30" placeholder="6-20位的密码" />&nbsp; <span
									class="red" id="passwordTip"></span>
							</dd>
						</dl>
						<dl class="labels labels1 rePassword">
							<dt>
								<label class="label required">确认密码：</label>
							</dt>
							<dd>
								<label class="nk-label keyword-label"></label> <input
									class="inputx inputx-reg" type="password" id="rePassword"
									maxlength="30" placeholder="确认密码" />&nbsp; <span class="red"
									id="rePasswordTip"></span>
							</dd>
						</dl>
						<dl class="labels labels1">
							<dt>
								<label class="label required">图形验证码：</label>
							</dt>
							<dd>
								<input class="inputx inputx-vf" type="text" id="chkCode" placeholder="图形验证码" />
								<span class="reg-yzm"><img width="80" height="36" src="" alt="" id="codeImage"/><a class="anormal" id="codeImageChange" href="javascript:;">换一张</a></span>
								<span class="red" id="chkCodeTip"></span>
							</dd>
						</dl>
						<dl class="labels labels1">
							<dt>
								<label class="label required">短信验证码：</label>
							</dt>
							<dd>
								<input class="inputx inputx-vf" type="text"
									placeholder="短信验证码" id="smsCode" maxlength="6" />&nbsp; 
									<input class="btn-normal" type="button" value="获取短信验证码" id="btnSendSmsCode" />&nbsp; 
									<span class="red" id="smsCodeTip"></span>
							</dd>
						</dl>
						<dl class="labels labels1">
							<dt></dt>
							<dd>
								<input type="checkbox" checked id="agreement" />&nbsp;已阅读并同意<a
									class="anormal" href="javascript:;" id="renderAgreement">《亿投车吧用户注册条款》</a><span
									class="red" id="agreementTip"></span>
							</dd>
						</dl>
						<dl class="labels labels1">
							<dt></dt>
							<dd>
								<input class="btn btn-h40 btn-w310" type="button" value="立即注册" id="btnRegister" />
							</dd>
						</dl>
					</div>
				</div>
				<div id="agreementTpl" style="display: none">
					<div id="agreementContent"></div>
				</div>
			</div>
		</div>
		<div class="footer">
	        <div class="footer-con">
	            <p>友情链接:<a class="ml10" href="#">极限户外-自驾</a><a href="#">天津企业</a><a href="#">北京团购</a><a href="#">绿野户外</a><a href="#">镭射眼智能行车记录仪</a><a href="#">汽配龙</a><a href="#">快递查询</a></p>
	            <p>Copyright©亿车汇 All Rights Reserved 版权所有 亿车汇（天津） 电子商务有限公司 网站备案/许可证号：津ICP备15008268号-1</p>
	        </div>
	    </div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/include/script.jsp"/>
	<!-- 加密专用js -->
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/bigint.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/barrett.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/rsa.js"></script>
	<script type="text/javascript" src="<%=appBaseUrl%>/js/encrypt/get"></script>

<script type="text/javascript">
	$(".login-ul>li>.inputx-login").focus(function(event) {
		$(".login-ul>li").removeClass("focus");
		$(this).parent().addClass("focus");
		event.stopPropagation();
	});
	$(document).blur(function(event) {
		$(".login-ul>li>.inputx-login").parent().removeClass("focus");
	});
	loadMemberAgreement()

	//
	function doRegister() {
		clearTip();
		var registFlag = true;
		//获取数据
		if (!agreement.checked) {
			$("#agreementTip").html("您未同意公司协议");
			registFlag = false;
		}
		var nickName = $("#nickName").val();
		if(!nickName){
			$("#nickNameTip").html("昵称不能为空");
			registFlag = false;
		}
		var regPhoneNo = $("#regPhoneNo").val();
		if (!regPhoneNo) {
			$("#phoneNoTip").html("手机号不能为空");
			registFlag = false;
		} else if (!isMobile(regPhoneNo)) {
			$("#phoneNoTip").html("请输入正确的手机号");
			registFlag = false;
		}
		var password = $("#password").val();
		if (!password) {
			$("#passwordTip").html("密码不能为空");
			registFlag = false;
		}
		var rePassword = $("#rePassword").val();
		if (!rePassword) {
			$("#rePasswordTip").html("确认密码不能为空");
			registFlag = false;
		} else if (password != rePassword) {
			$("#rePasswordTip").html("您两次输入的密码不一致");
			registFlag = false;
		}
		var chkCode = $("#chkCode").val();
		if (!chkCode) {
			$("#chkCodeTip").html("请输入验证码");
			registFlag = false;
		}
		var smsCode = $("#smsCode").val();
		if (!smsCode) {
			$("#smsCodeTip").html("手机验证码不能为空");
			registFlag = false;
		}
		//
		var secureLevel = getSecurityLevel(password);

		if (!registFlag) {
			return;
		}
		password = encryptStr(password);
		//生成提交数据
		var postData = {
			nickName : nickName,
			password : password,
			phoneNo : regPhoneNo,
			chkCode : chkCode,
			smsCode : smsCode,
			secureLevel : secureLevel
		};
		//可以对postData进行必要的处理（如如数据格式转换）
		//显示等待提示框
		var loaderLayer = Layer.progress("正在验证注册信息...");
		//
		var ajax = Ajax.post("/user/regist/do");
		ajax.params({
			appType : "web"
		});
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			//隐藏等待提示框
			loaderLayer.hide();
			//
			if (result.type == "info") {
				var theLayer = Layer.msgInfo("注册成功，3秒 后转到之前的页面...", function(layerIndex) {
					if (result.data) {
						setPageUrl(getAppUrl(result.data));
					} else {
						setPageUrl(getAppUrl("/"));
					}
				}, 3000);
			} else {
				if (result.code == 11) {
					$("#smsCodeTip").html(result.message);
				} else if (result.code == 12) {
					$("#phoneNoTip").html(result.message);
				} else if (result.code == 21) {
					$("#chkCodeTip").html(result.message);
				}else {
					Layer.warning(result.message);
				}
				changeImageCode();
			}
		});
		ajax.fail(function(result, jqXhr) {
			//隐藏等待提示框
			loaderLayer.hide();
		});
		ajax.go();
	}

	//隐藏提示
	function clearTip() {
		$("#nickNameTip").html("");
		$("#phoneNoTip").html("");
		$("#passwordTip").html("");
		$("#rePasswordTip").html("");
		$("#chkCodeTip").html("");
		$("#smsCodeTip").html("");
	}
	
	//图形验证码错误提示
	function noCheckCode(chkCodeTip) {
		clearTip();
		$("#chkCodeTip").html(chkCodeTip);
	}
	
	//手机号错误提示
	function invalidPhoneNo(phoneNoTip) {
		clearTip();
		$("#phoneNoTip").html(phoneNoTip);
	}

	$(function() {
		$("#btnRegister").click(doRegister);
		//
		var errorCodeHandlerMap = {
			noCheckCode : noCheckCode,
			invalidPhoneNo : invalidPhoneNo
		}
		smsCodeSender = SmsCodeSender.newOne();
		smsCodeSender.bindCtrls("btnSendSmsCode", "regPhoneNo", "chkCode", errorCodeHandlerMap);
		smsCodeSender.setUsage("regist");
		//
		bindImageCodeCtrls("codeImageChange", "codeImage");
		$("#renderAgreement").click(renderAgreementHtml);
		$("#chkCode").bind('keydown', function(e) {
			var key = e.which;
			if (key == 13) {
				e.preventDefault();
				doLogin();
			}
		});
		changeImageCode();
	});

	//渲染商城协议页面内容
	function renderAgreementHtml() {
		//获取模板内容
		var tplHtml = $id("agreementTpl").html();
		//使用生成的内容
		Layer.dialog({
			title : "亿投车吧用户注册协议",
			dom : tplHtml,
			area : [ '800px', '480px' ],
			closeBtn : true,
			btn : [ "同意并继续" ],
			shadeClose : true
		});
	}

	// 加载会员协议
	function loadMemberAgreement() {
		var ajax = Ajax.post("/setting/memberAgreement/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var data = result.data;
				if (data != null) {
					$id("agreementContent").html(data.content);
				} else {
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
</script>
</body>
</html>