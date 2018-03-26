<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />

<title>系统登录</title>
<style type="text/css">
	.from-panel{
		position: absolute;
		top: 50%;
		left: 50%; 
		width: 600px; 
		height: 270px;  
		margin-top: -135px;
		margin-left: -300px; 
		border: 1px solid gray; 
		border-radius: 4px;
	}
</style>

</head>
<body style="margin: 0; padding: 0;">
	<div id="rootPanel"
		style="position: absolute; top: 0; left: 0; width: 100%; height: 100%;">
		<div id="theForm" class="form from-panel">
			<div class="caption row">管理后台 - 登录</div>
			<div class="field row">
				<label class="field label two wide" for="phoneNo">手机</label> <input
					class="field value two wide" type="text" id="phoneNo" size="18"
					maxlength="11" placeholder="请输入手机号" />
			</div>
			<div class="field row">
				<label class="field label two wide" for="password">密码</label> <input
					class="field value two wide" type="password" id="password"
					size="18" maxlength="16" placeholder="请输入密码" />
			</div>
			<div class="field row">
				<label class="field label two wide" for="chkCode">验证码</label> <input
					class="field value two wide" type="text" id="chkCode"
					name="chkCode" maxlength="4"> <img class="absMiddle"
					id="codeImage" style="height: 28px;" src="" /> <span
					id="codeMessage">&nbsp;</span>
			</div>
			<div class="field row">
				<label class="field label two wide">&nbsp;</label> <a
					href="javascript:void(0);goRetrieveLogPass()">忘记密码？马上找回</a> <span
					class="spacer two wide">&nbsp;</span> <a
					href="javascript:void(0);goRetrieveLogPass()">设置初始密码</a>
			</div>
			<div class="action row">
				<button class="normal button" id="btnLogin">登录</button>
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
		//调整控件大小
		function adjustCtrlsSize(winWidth, winHeight) {
			$id("rootPanel").width(winWidth);
			$id("rootPanel").height(winHeight);
			//
			centerInView("theForm");
		}
		//实例化表单代理
		var formProxy = FormProxy.newOne();
		//注册表单控件
		formProxy.addField({
			id : "phoneNo",
			required : true,
			//rules : [ "isMobile" ]
		});
		formProxy.addField({
			id : "password",
			required : true
		});
		formProxy.addField({
			id : "chkCode",
			required : true,
			messageTargetId : "codeMessage"
		});

		//更换图形验证码
		function changeImageCode() {
			var imgObj = $id("codeImage").get(0);
			imgObj.src = getAppUrl() + "/xutil/checkCode.do?uid=" + genUniqueStr();
		}

		function goRetrieveLogPass() {
			var pageUrl = makeUrl(getAppUrl("/user/logPass/retrv/jsp"), {phoneNo : $id("phoneNo").val()});
			setPageUrl(pageUrl);
		}
		//
		function doLogin() {
			//验证数据
			var vldResult = formProxy.validateAll();
			if (!vldResult) {
				return;
			}
			//生成提交数据
			var postData = formProxy.getValues();
			//可以对postData进行必要的处理（如如数据格式转换）
			//TODO 先不转换
			//postData.password = encryptStr(postData.password);
			//显示等待提示框
			var loaderLayer = Layer.loading("正在验证登录信息...");
			//
			var ajax = Ajax.post("/user/login/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				//隐藏等待提示框
				loaderLayer.hide();
				//
				if (result.type == "info") {
					Layer.progress("登录成功");
					setPageUrl(getAppUrl(result.data));
				} else {
					Toast.show(result.message, 2000, "error");
					//Layer.warning(result.message);
					changeImageCode();
				}
			});
			ajax.fail(function(result, jqXhr) {
				//隐藏等待提示框
				loaderLayer.hide();
			});
			ajax.go();
		}

		//------------------------------------初始化代码--------------------------------------
		$(function() {
			var phoneNo = extractUrlParams().phoneNo;
			if (phoneNo && app.consts.sysAdminName != phoneNo) {
				$id("phoneNo").val(phoneNo);
			}
			//
			//绑定控件事件
			$id("codeImage").click(changeImageCode);
			$id("btnLogin").click(doLogin);
			$id("chkCode").bind('keydown', function(e) {
				var key = e.which;
				if (key == 13) {
					e.preventDefault();
					doLogin();
				}
			});
			//
			changeImageCode();
		});
	</script>
</body>
</html>