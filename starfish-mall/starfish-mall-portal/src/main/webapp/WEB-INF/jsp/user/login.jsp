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
<body class="page-login">
<div class="page-wrapper">
	<div class="header">
		<div class="page-width">
			<a class="logo" href="<%=appBaseUrl%>/"><img src="<%=resBaseUrl%>/image/logo.png" alt="车logo" /></a>
            <span class="logo-title">欢迎登录</span>
		</div>
	</div>
    <div class="content">
        <div class="page-width">
            <div class="section login-section">
                <div class="section-right">
                    <div class="login">
						<h1>用户登录<a class="a-reg" href="<%=appBaseUrl%>/user/regist/jsp"><span>立即注册</span><i></i></a></h1>
						<div class="login-cont">
							<div class="tip" id="titleTip"></div>
							<div>
								<div class="tip tip01" id="loginTip"
									style="display: none;"></div>
							</div>
							<ul class="login-ul">
								<li class="name"><label class="nk-label name-label"></label>
									<input class="inputx inputx-login" type="text"
									placeholder="手机号" id="phoneNo" maxlength="11" /></li>
								<li class="keyword"><label class="nk-label keyword-label"></label>
									<input class="inputx inputx-login" type="password"
									placeholder="密码" id="password" maxlength="60" /></li>
								<li class="code">
									<input class="inputx inputx-login inputx-vf" type="text" id="chkCode" placeholder="验证码" />
									<span class="reg-yzm"><img width="80" height="36" src="" alt="" id="codeImage"/><a class="anormal" id="codeImageChange" href="javascript:;">换一张</a></span>
									</li>
									
								<li class="option"><label class="ele-vlmiddle"><input
										type="checkbox" id="autoLogin" /> <span>自动登录</span></label> <a
									class="fr" href="<%=appBaseUrl%>/user/logPass/retrv/jsp">忘记密码？</a>
								</li>
								<li><input class="btn btn-h40 btn-wauto" type="button"
									value="立即登录" id="btnLogin" /></li>
							</ul>
						</div>
					</div>
                </div>
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
	$(".login-ul>li>.inputx-login").blur(function(event) {
		$(".login-ul>li>.inputx-login").parent().removeClass("focus");
	});

	//
	function doLogin() {
		//清理提示
		clearTip();
		//获取数据
		var loginFlag = true;
		var phoneNo = $("#phoneNo").val();
		if (!phoneNo) {
			$("#loginTip").html("请输入帐户名");
			$("#titleTip").css("display","none");
			$("#loginTip").css("display", "");
			return;
		} else if (!isMobile(phoneNo)) {
			$("#loginTip").html("请输入正确的手机号");
			$("#titleTip").css("display","none");
			$("#loginTip").css("display", "");
			return;
		}
		var password = $("#password").val();
		if (!password) {
			$("#loginTip").html("请输入密码");
			$("#titleTip").css("display","none");
			$("#loginTip").css("display", "");
			return;
		}
		var chkCode = $("#chkCode").val();
		if (!chkCode) {
			$("#loginTip").html("请输入验证码");
			$("#titleTip").css("display","none");
			$("#loginTip").css("display", "");
			return;
		}

		var alFlag = true;
		if (autoLogin.checked) {
			alFlag = true;
		} else {
			alFlag = false;
		}
		//生成提交数据
		password = encryptStr(password);
		var postData = ({
			phoneNo : phoneNo,
			password : password,
			chkCode : chkCode,
			alFlag : alFlag
		});
		//可以对postData进行必要的处理（如如数据格式转换）
		//显示等待提示框
		var loaderLayer = Layer.progress("正在验证登录信息...");
		//
		var ajax = Ajax.post("/user/login/do");
		ajax.params({
			appType : "web"
		});
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			//隐藏等待提示框
			loaderLayer.hide();
			//
			if (result.type == "info") {
				var theLayer = Layer.msgInfo("登录成功，马上转到之前的页面...", function(layerIndex) {
					if (result.data) {
						setPageUrl(getAppUrl(result.data));
					} else {
						setPageUrl(getAppUrl("/"));
					}
				}, 1000);
			} else {
				$("#titleTip").css("display","none");
				$("#loginTip").css("display", "");
				if(result.code == "10" ){
					$("#loginTip").html(result.message + "&nbsp;&nbsp;<a href='<%=appBaseUrl%>/user/logPass/trv/jsp'>去设置</a>");
				}else{
					$("#loginTip").html(result.message);
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
		$("#loginTip").html("");
	}

	$(function() {
		var centerAdjuster = makeProxy(centerInView, null, "rootPanel", window, -20);
		centerAdjuster();
		$(window).resize(centerAdjuster);
		//绑定图形验证码
		bindImageCodeCtrls("codeImageChange", "codeImage");
		//绑定控件事件
		$("#btnLogin").click(doLogin);
		$("#chkCode").bind('keydown', function(e) {
			var key = e.which;
			if (key == 13) {
				e.preventDefault();
				doLogin();
			}
		});
		//
		$id("titleTip").html("公共场所不建议自动登录，以防账号丢失");
		changeImageCode();
	});
</script>
</body>
</html>