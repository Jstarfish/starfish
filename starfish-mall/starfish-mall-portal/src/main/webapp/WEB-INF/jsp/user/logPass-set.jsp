<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head.jsp"%>
	<div class="content">
        <div class="page-width">
            <div class="login retrieve-section">
               <h1>设置密码</h1>

                <div class="login-reg-cont">
                    <div class="order-process">
                        <ul class="steps-state">
                            <li class="actived"><i>1</i><span>身份验证</span></li>
                            <li class="active"><i>2</i><span>设置密码</span><em></em></li>
                            <li><i>3</i><span>设置成功</span></li>
                        </ul>
                    </div>
                    <div class="reg-cont">
                        <dl class="labels labels1 keyword">
                            <dt><label class="label required">新密码：</label></dt>
                            <dd>
	                            <label class="nk-label keyword-label"></label>
	                            <input class="inputx inputx-reg" type="password" id="password" placeholder="请输入6-20位的密码" maxlength="30"/>&nbsp;
	                            <span class="red" id="passwordTip"></span>
                            </dd>
                        </dl>
                        <dl class="labels labels1 keyword">
                            <dt><label class="label required">确认密码：</label></dt>
                            <dd>
	                            <label class="nk-label keyword-label"></label>
	                            <input class="inputx inputx-reg" type="password" id="rePassword" placeholder="请再次输入密码" maxlength="30"/>&nbsp;
	                            <span class="red" id="rePasswordTip"></span>
                            </dd>
                        </dl>
                        <dl class="labels labels1">
                            <dt></dt>
                            <dd><input class="btn btn-h40 btn-w156" type="button" value="下一步" id="btnResetPassword"/></dd>
                        </dl>
                    </div>
                </div>
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
	//
	function doResetPassword() {
		//清理提示
		clearTip();
		//获取数据
		var password = $("#password").val();
		if(!password){
			$("#passwordTip").html("请输入密码");
			return;
		}else if(password.length < 6){
			$("#passwordTip").html("请输入6-20位的密码");
			return;
		}
		var rePassword = $("#rePassword").val();
		if(!rePassword){
			$("#rePasswordTip").html("请再次输入密码");
			return;
		}else if(password != rePassword){
			$("#rePasswordTip").html("您俩次输入的密码不一致");
			return;
		}
		var secureLevel = getSecurityLevel(password);
		password = encryptStr(password);
		//生成提交数据
		var postData = ({
			password : password,
			secureLevel : secureLevel
		});
		//可以对postData进行必要的处理（如如数据格式转换）
		//显示等待提示框
		var loaderLayer = Layer.loading("正在验证密码信息...");
		//
		var ajax = Ajax.post("/user/logPass/reset/do");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			//隐藏等待提示框
			loaderLayer.hide();
			//
			if (result.type == "info") {
				var pageUrl = makeUrl(getAppUrl("/user/logPass/set/result/jsp"));
				setPageUrl(pageUrl);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.fail(function(result, jqXhr) {
			//隐藏等待提示框
			loaderLayer.hide();
		});
		ajax.go();
	}
	
	//隐藏提示
	function clearTip(){
		$("#passwordTip").html("");
		$("#rePasswordTip").html("");
	}
	
	$(function() {
		$("#btnResetPassword").click(doResetPassword);
		$("#rePassword").bind('keydown', function (e) { 
			var key = e.which; 
			if (key == 13) { 
				e.preventDefault(); 
				doResetPassword();
			} 
			}); 
	});
	
	</script>
</body>
<!-- layTpl begin -->
<!-- layTpl end -->
</html>