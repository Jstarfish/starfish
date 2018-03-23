<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head.jsp"%>
<div class="content">
	<div class="page-width">
		<div class="crumbs">
			<a href="">首页</a>&gt;<a href="">账户设置</a>&gt;<a href="javascript:;">账户安全</a>
		</div>
		<div class="section">
			<jsp:include page="/WEB-INF/jsp/include/ucenterMenu.jsp" />
			<div class="section-right1">
				<div class="mod-main1">
					<div class="order-tit">
						<h1>安全中心</h1>
					</div>
					<div class="safe-cont">
						<ul class="user-info">
							<li class="noline">
								<div class="user-safe">
									安全级别：
									<!--注：safe1 safe2 safe3 safe4 safe5 safe6六个级别-->
									<span class="safe-level safe1" id="safeLevel"> <i></i><span>很危险</span><!--中 高 很高-->
									</span>
								</div>
							</li>
							<li>
								<h3>
									<i></i>登录密码
								</h3> <a class="anormal fr" onclick="renderUpdatePasswordHtml()"
								href="javascript:;">修改</a> <span class="orange">互联网账号存在被盗风险，建议您定期更改密码以保护账户安全。</span>
							</li>
							<li>
								<h3>
									<i></i>手机验证<i class="icon1"></i>
								</h3> <a class="anormal fr" href="#">修改</a> <span>您验证的手机：<span
									id="userPhoneNo"></span> 若已丢失或停用，请立即更换， <span class="orange">避免账户被盗</span>
							</span>
							</li>
							<li>
								<h3>
									<i style="background: none" id="sucPayPwd"></i>支付密码<i class="icon2"></i>
								</h3> <a class="anormal fr" onclick="renderUpdatePayPasswordHtml()"
								href="javascript:;">修改</a> <a href="javascript:;">安全程度：</a> <!--注：safe1 safe2 safe3 safe4 safe5 safe6六个级别-->
								<span class="safe-level payPasswordLevel safe1" id="paySafeLevel"> <i></i> <!--中 高 很高-->
							</span>
							</li>
						</ul>
						<br /> <br /> <br /> <br /> <br />
					</div>
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
	var dataList = null;
	var phoneNo = "";

	$(function() {
		initUserInfo();
	});

	//加载用户信息
	function initUserInfo() {
		var ajax = Ajax.post("/user/info/get");
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				dataList = result.data;
				if (dataList != null) {
					phoneNo = dataList.user.phoneNo;
					$("#userPhoneNo").html(phoneNo)
					setUserSafeLevels(dataList.user.secureLevel);
					//设置支付密码等级
					var paySafeLevel = 0;
					var userVerfStatus = dataList.user.userVerfStatus;
					if(userVerfStatus != null){
						for(var i = 0; i < userVerfStatus.length; i++){
							if(userVerfStatus[i].aspect == "payPass"){
								paySafeLevel = userVerfStatus[i].secureLevel;
								$id("sucPayPwd").css("background", "")
							}
						}
					}
					setPayPasswordLevels(paySafeLevel);
					
				}
			}

		});
		ajax.go();
	}

	//设置账户安全等级
	function setUserSafeLevels(safeLevel) {
		if (safeLevel == 4) {
			$id("safeLevel").addClass('safe6'); 
			$id("safeLevel").find("span").text("较安全");
		} else if (safeLevel == 3) {
			$id("safeLevel").addClass('safe5'); 
			$id("safeLevel").find("span").text("安全");
		} else if (safeLevel == 2) {
			$id("safeLevel").addClass('safe4'); 
			$id("safeLevel").find("span").text("一般");
		} else if (safeLevel == 1) {
			$id("safeLevel").addClass('safe3'); 
			$id("safeLevel").find("span").text("有风险");
		} else {
			$id("safeLevel").addClass('safe2'); 
			$id("safeLevel").find("span").text("高风险");
		}
	}

	function setPayPasswordLevels(paySafeLevel) {
		if (paySafeLevel == 4) {
			$id("paySafeLevel").addClass('safe6'); 
		} else if (paySafeLevel == 3) {
			$id("paySafeLevel").addClass('safe5'); 
		} else if (paySafeLevel == 2) {
			$id("paySafeLevel").addClass('safe4'); 
		} else if (paySafeLevel == 1) {
			$id("paySafeLevel").addClass('safe3'); 
		} else {
			$id("paySafeLevel").addClass('safe2'); 
		}
	}
	
	//更新登录密码
	function doUpdatePassword() {
		//clearTip();
		//获取数据
		var oldPassword = $("#oldPassword").val();
		if (!oldPassword) {
			$("#oldPasswordTip").html("旧密码不能为空");
			return;
		}
		var password = $("#password").val();
		if (!password) {
			$("#passwordTip").html("密码不能为空");
			return;
		}else if(password.length<6)
	    {   
	        $("#passwordTip").html("请输入6-20位的密码");
	        return;
	    }
	    else if (/^[a-zA-Z]+$/ig.test(password) || /^[0-9]+$/ig.test(password)) 
	    {
	    	$("#passwordTip").html("密码不能为纯字母或纯数字");
	    	return;
		}
		var rePassword = $("#rePassword").val();
		if (!rePassword) {
			$("#rePasswordTip").html("确认密码不能为空");
			return;
		} else if (password != rePassword) {
			$("#rePasswordTip").html("您输入的密码不一致");
			return;
		}
		var secureLevel = getSecurityLevel(password);
		password = encryptStr(password);
		oldPassword = encryptStr(oldPassword);
		//生成提交数据
		var postData = ({
			password : password,
			oldPassword : oldPassword,
			secureLevel : secureLevel
		});
		//可以对postData进行必要的处理（如如数据格式转换）
		//显示等待提示框
		var loaderLayer = Layer.loading("正在验证信息...");
		//
		var ajax = Ajax.post("/user/logined/logPass/reset/do");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			//隐藏等待提示框
			loaderLayer.hide();
			if (result.type == "info") {
				updatePasswordDlg.hide();
				Layer.msgSuccess(result.message);
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

	//更新支付密码
	function doUpdatePayPassword() {
		//clearTip();
		//获取数据
		var phoneCode = $("#phoneCode").val();
		if (!phoneCode) {
			$("#smsCodeTip").html("手机验证码不能为空");
			return;
		}
		var payPassword = $("#payPassword").val();
		if (!payPassword) {
			$("#payPasswordTip").html("旧密码不能为空");
			return;
		}else if(payPassword.length<6)
	    {   
	        $("#passwordTip").html("请输入6-20位的密码");
	        return;
	    }
	    else if (/^[a-zA-Z]+$/ig.test(payPassword) || /^[0-9]+$/ig.test(payPassword)) 
	    {
	    	$("#passwordTip").html("密码不能为纯字母或纯数字");
	    	return;
		}
		var rePayPassword = $("#rePayPassword").val();
		if (!rePayPassword) {
			$("#rePayPasswordTip").html("确认密码不能为空");
			return;
		} else if (payPassword != rePayPassword) {
			$("#rePayPasswordTip").html("密码不一致");
			return;
		}
		var secureLevel = getSecurityLevel(payPassword);
		payPassword = encryptStr(payPassword);
		//生成提交数据
		var postData = ({
			payPassword : payPassword,
			phoneCode : phoneCode,
			secureLevel : secureLevel
		});
		//可以对postData进行必要的处理（如如数据格式转换）
		//显示等待提示框
		var loaderLayer = Layer.loading("正在验证信息...");
		//
		var ajax = Ajax.post("/user/payPass/reset/do");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			//隐藏等待提示框
			loaderLayer.hide();
			if (result.type == "info") {
				updatePayPasswordDlg.hide();
				Layer.msgSuccess(result.message);
				initUserInfo();
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
	
	var updatePasswordDlg=null;
	//渲染修改登录密码页面内容
	function renderUpdatePasswordHtml() {
		//获取模板内容
		var tplHtml = $id("updatePasswordTpl").html();
		//使用生成的内容
		updatePasswordDlg = Layer.dialog({
			title : false,
			dom : tplHtml,
			area : [ '560px', '300px' ],
			closeBtn : true,
			btn : false,
			shadeClose : true
		});
		if (phoneNo != "") {
			$("#phoneNo").val(phoneNo);
		}
	}
	
	var updatePayPasswordDlg=null;
	//渲染修改支付密码页面内容
	function renderUpdatePayPasswordHtml() {
		//获取模板内容
		var tplHtml = $id("updatePayPasswordTpl").html();
		//使用生成的内容
		updatePayPasswordDlg = Layer.dialog({
			title : false,
			dom : tplHtml,
			area : [ '560px', '300px' ],
			closeBtn : true,
			btn : false,
			shadeClose : true
		});
		if (phoneNo != "") {
			$("#payPhoneNo").val(phoneNo);
		}
		
		smsCodeSender = SmsCodeSender.newOne();
		smsCodeSender.bindCtrls("btnSendSmsCode", "payPhoneNo");
		smsCodeSender.setUsage("payPass");
		$id("btnSendSmsCode").attr("disabled", false);
	}
</script>

</body>
<script type="text/html" id="updatePasswordTpl">
	    <div>
           <h2 style="padding: 10px 15px">修改密码</h2>
           <div>
               <dl class="labels labels1 name">
                   <dt><label class="label required">手机号码：</label></dt>
                   <dd>
                    <input class="inputx inputx-reg" type="text" id="phoneNo" maxlength="20" placeholder="手机号" disabled/>
                    <span class="red" id="phoneNoTip"></span>
                   </dd>
               </dl>
				<dl class="labels labels1">
                   <dt><label class="label required">旧密码：</label></dt>
                   <dd>
                    <input class="inputx inputx-reg" type="password" placeholder="旧密码" maxlength="30" id="oldPassword"/>&nbsp;
                    <span class="red" id="oldPasswordTip"></span>
                   </dd>
               </dl>
               <dl class="labels labels1 keyword">
                   <dt><label class="label required">密码：</label></dt>
                   <dd>
                    <input class="inputx inputx-reg" type="password" id="password" maxlength="30" placeholder="6-20位的密码" />&nbsp;
                    <span class="red" id="passwordTip"></span>
                   </dd>
               </dl>
               <dl class="labels labels1 keyword">
                   <dt><label class="label required">确认密码：</label></dt>
                   <dd>
                    <input class="inputx inputx-reg" type="password" id="rePassword" maxlength="30" placeholder="确认密码" />&nbsp;
                    <span class="red" id="rePasswordTip"></span>
                   </dd>
               </dl>
               <dl class="labels labels1">
                   <dt></dt>
                   <dd><input class="btn btn-h40" type="button" value="确定" onclick="doUpdatePassword()" /></dd>
               </dl>
           </div>
       </div>
</script>

<script type="text/html" id="updatePayPasswordTpl">
	    <div>
           <h2 style="padding: 10px 15px">修改支付密码</h2>
           <div>
               <dl class="labels labels1 name">
                   <dt><label class="label required">手机号码：</label></dt>
                   <dd>
                    <input class="inputx inputx-reg" type="text" id="payPhoneNo" maxlength="20" placeholder="手机号" disabled/>
                    <span class="red" id="phoneNoTip"></span>
                   </dd>
               </dl>
				<dl class="labels labels1">
                   <dt><label class="label required">短信验证码：</label></dt>
                   <dd style="position: relative;">
                    <input class="inputx inputx-vf" type="text" placeholder="短信验证码" id="phoneCode" maxlength="6" width="150px;"/>
					<input class="btn-normal btn-vf" type="button" value="获取短信验证码" id="btnSendSmsCode" style="top: 1px;"/>&nbsp;
                    <span class="red" id="smsCodeTip"></span>
                   </dd>
               </dl>
               <dl class="labels labels1 keyword">
                   <dt><label class="label required">支付密码：</label></dt>
                   <dd>
                    <input class="inputx inputx-reg" type="password" id="payPassword" maxlength="30" placeholder="6-20位的密码" />&nbsp;
                    <span class="red" id="payPasswordTip"></span>
                   </dd>
               </dl>
               <dl class="labels labels1 keyword">
                   <dt><label class="label required">确认支付密码：</label></dt>
                   <dd>
                    <input class="inputx inputx-reg" type="password" id="rePayPassword" maxlength="30" placeholder="确认密码" />&nbsp;
                    <span class="red" id="rePayPasswordTip"></span>
                   </dd>
               </dl>
               <dl class="labels labels1">
                   <dt></dt>
                   <dd><input class="btn btn-h40" type="button" value="确定" onclick="doUpdatePayPassword()" /></dd>
               </dl>
           </div>
       </div>
</script>
</html>