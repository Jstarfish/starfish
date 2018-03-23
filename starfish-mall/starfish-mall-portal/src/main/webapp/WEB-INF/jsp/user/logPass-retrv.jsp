<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head.jsp"%>
	<div class="content">
        <div class="page-width">
            <div class="login retrieve-section">
                <h1>找回密码</h1>

                <div class="login-reg-cont">
                    <div class="order-process">
                        <ul class="steps-state">
                            <li class="active"><i>1</i><span>身份验证</span></li>
                            <li><i>2</i><span>修改密码</span><em></em></li>
                            <li><i>3</i><span>找回成功</span></li>
                        </ul>
                    </div>
                    <div class="reg-cont">
                        <dl class="labels labels1 phoneNo">
                            <dt><label class="label required">手机号码：</label></dt>
                            <dd>
	                            <label class="nk-label name-label"></label>
	                            <input class="inputx inputx-reg" type="text" id="phoneNo" placeholder="请输入手机号" maxlength="11"/>
	                            <span class="gray">请输入11位手机号码</span>
	                            <span class="red" id="phoneNoTip"></span>
                            </dd>
                        </dl>
                        <dl class="labels labels1">
                            <dt><label class="label required">验证码：</label></dt>
                            <dd>
	                            <input class="inputx inputx-vf" type="text" id="chkCode" placeholder="请输入验证码" />
								<span class="reg-yzm"><img width="80" height="36" src="" alt="" id="codeImage"/><a class="anormal" id="codeImageChange" href="javascript:;">换一张</a></span>
	                            <span class="red" id="chkCodeTip"></span>
                            </dd>
                        </dl>
                        <dl class="labels labels1">
                            <dt><label class="label required">短信验证码：</label></dt>
                            <dd>
	                            <input class="inputx inputx-vf" type="text" placeholder="请输入短信验证码" id="smsCode" maxlength="6"/>&nbsp;
	                            <input class="btn-normal" type="button" value="获取短信验证码" id="btnSendSmsCode"/>&nbsp;
	                            <span class="red" id="smsCodeTip"></span>
                            </dd>
                        </dl>
                        <dl class="labels labels1">
                            <dt></dt>
                            <dd><input class="btn btn-h40 btn-w156" type="button" value="下一步" id="btnCheckUser"/></dd>
                        </dl>
                    </div>
                </div>
            </div>
        </div>
    </div>

	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
	//
	function doCheckUser() {
		//清理提示
		clearTip();
		//获取数据
		var phoneNo = $("#phoneNo").val();
		if(!phoneNo){
			$("#phoneNoTip").html("请输入手机号码");
			return;
		}else if(!isMobile(phoneNo)){
			$("#phoneNoTip").html("请输入正确的手机号");
			return;
		}
		var chkCode = $("#chkCode").val();
		if(!chkCode){
			$("#chkCodeTip").html("请输入验证码");
			return;
		}
		var smsCode = $("#smsCode").val();
		if(!smsCode){
			$("#smsCodeTip").html("请输入手机验证码");
			return;
		}
		//生成提交数据
		var postData = ({
			phoneNo : phoneNo,
			chkCode : chkCode,
			smsCode : smsCode
		});
		//可以对postData进行必要的处理（如如数据格式转换）
		//显示等待提示框
		var loaderLayer = Layer.loading("正在验证提交信息...");
		//
		var ajax = Ajax.post("/user/logPass/check/do");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			//隐藏等待提示框
			loaderLayer.hide();
			//
			if (result.type == "info") {
				var pageUrl = makeUrl(getAppUrl("/user/logPass/reset/jsp"));
				setPageUrl(pageUrl);
			} else {
				if(result.code == 12){
					$("#phoneNoTip").html(result.message);
				}else if(result.code == 11){
					$("#smsCodeTip").html(result.message);
				}else if(result.code == 21){
					$("#chkCodeTip").html(result.message);
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
	function clearTip(){
		$("#phoneNoTip").html("");
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
		$("#btnCheckUser").click(doCheckUser);
		//发送短信验证码
		var errorCodeHandlerMap = {
			noCheckCode : noCheckCode,
			invalidPhoneNo : invalidPhoneNo
		}
		smsCodeSender = SmsCodeSender.newOne();
		smsCodeSender.bindCtrls("btnSendSmsCode", "phoneNo", "chkCode", errorCodeHandlerMap);
		smsCodeSender.setUsage("logPass");
		//图形验证码
		bindImageCodeCtrls("codeImageChange", "codeImage");
		$("#checkCode").bind('keydown', function (e) { 
			var key = e.which; 
			if (key == 13) { 
				e.preventDefault(); 
				doLogin();
			} 
			}); 
		changeImageCode();
	});
	
	</script>
</body>
<!-- layTpl begin -->
<!-- layTpl end -->
</html>