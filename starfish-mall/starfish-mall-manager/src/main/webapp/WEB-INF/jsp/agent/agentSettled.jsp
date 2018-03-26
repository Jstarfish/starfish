<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<style type="text/css">
.center {
	margin:0 auto;
}
.text-title {
	padding-left: 2px;
	padding-top: 6px;
	height: 40px;
	width: 120px;
	background-image: url(<%=resBaseUrl%>/image/merchant/redback.png);
}
.main {
	height: 1010px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u2.png);
}
.text-white {
    color: #ffffff;
    font-size: 20px;
    font-style: normal;
    font-weight: 400;
    text-align: center;
}
.text-black {
	color: #333333;
    font-size: 14px;
    font-style: normal;
    font-weight: 700;
}
.text-cyan {
	color: #006600;
}
.text-red {
	 color: #ff0000;
}
.nav .text-white {
    color: #ffffff;
    font-size: 14px;
    font-style: normal;
    font-weight: 700;
    text-align: center;
}
.nav .text-black {
	color: #333333;
    font-size: 14px;
    font-style: normal;
    font-weight: 700;
    text-align: center;
}
.line-red {
	height: 2px;
	width: 100%;
	background-image: url(<%=resBaseUrl%>/image/merchant/u111_line.png);
}
.info {
	padding-left: 5px;
	vertical-align:middle;
    height: 35px;
    line-height: 35px;
    height: 35px;
    width: 245px;
	color: #999999;
    font-size: 12px;
    font-weight: 400;
    text-align: left;
    word-wrap: break-word;
    background-image: url(<%=resBaseUrl%>/image/merchant/u19.png);
}
.nav {
	height: 40px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u121.png);
}
.nav .first {
	float: left;
	height: 38px;
    width: 367px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u130.png);
}
.nav .first .left{
	float: inherit;
	height: 38px;
    width: 135px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u134.png);
}

.nav .first .center{
	float: inherit;
    width: 98px;
    height: 38px;
    line-height: 38px;
}
.nav .second {
	float: left;
	height: 38px;
    width: 337px;
}
.nav .second .left{
	float: inherit;
	height: 38px;
    width: 50px;
}
.nav .second .center{
	float: inherit;
    width: 150px;
    height: 38px;
    line-height: 38px;
}
.nav .second .right{
	float: inherit;
    width: 130px;
    color: #ffffff;
    font-size: 72px;
    font-style: normal;
    font-weight: 400;
    height: 38px;
    line-height: 38px;
    text-align: right;
}
.nav .third {
	float: left;
	height: 38px;
    width: 292px;
}
.nav .third .left{
	float: inherit;
	height: 38px;
    width: 50px;
}
.nav .third .center{
	float: inherit;
    width: 150px;
    height: 38px;
    line-height: 38px;
}
.password-level1 {
	width: 53px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u31.png);
}
.password-level2 {
	width: 106px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u31.png);
}
.password-level3 {
	width: 159px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u31.png);
}
.shop-info {
	height: 88px;
	width: 555px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u183.png);
	float: left;
	font-size: 12px;
    font-style: normal;
    line-height: 30px;
}
.next {
	height: 35px;
    width: 215px;
    cursor: pointer;
	background-image: url(<%=resBaseUrl%>/image/merchant/u82.png);
}
.result {
	height: 260px;
    width: 640px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u229.png);
}
.result .title {
	font-style: normal;
    font-weight: 700;
    text-align: left;
    padding-left: 5px;
	height: 55px;
	line-height: 55px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u231.png);
}
.result .content {
	height: 150px;
	padding-top: 30px;
    text-align: left;
    padding-left: 20px;
}
.result .toShop {
	cursor: pointer;
	width: 110px;
	height: 30px;
	line-height: 30px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u241.png);
}
</style>

<title>商户入驻申请</title>
</head>

<body class="center" style="width: 996px;" >
<div style="height: 180px;padding-top: 20px;">
	<div class="filter section" >
		<div class="filter row">
			 <img src="<%=resBaseUrl%>/image/merchant/logo.png"/>
		</div>
	</div>
</div>
<div>
	<div class="text-title text-white">商户入驻</div>
    	<div class="line-red"> </div>
    	<div class="main">
    		<span class="hr divider">&nbsp;</span>
    		<div class="nav">
    			<div class="first">
    				<div id="first" class="left"></div>
    				<div class="center text-white">1.填写商户信息</div>
     			</div>
     			<div class="second">
    				<div id="second" class="left"></div>
    				<div class="center text-black">2.填写店铺信息</div>
    				<div class="right">＞</div>
     			</div>
     			<div class="third">
    				<div id="third" class="left"></div>
    				<div class="center text-black">3.等待审核</div>
     			</div>
    		</div>
    		<div class="center" style="width: 830px;">
     		<div id="user-info" class="form">
     			<div class="field row" style="height: 35px;line-height: 35px;">
				</div>
				<div id="shops" class="field row" style="height: 88px;line-height: 88px;display: none;">
					<div style="width: 100px;float: left;">&nbsp;</div>
					<div class="shop-info">
						<div style="width: 130px;float: left;text-align: right;">您的开店情况： </div>
						<div id="shopsInfo" style="width: 250px;float: left;"></div>
						<div class="text-red" style="width:150px;float: right;">注意事项：如果拥有正在等待审核的店铺，则不能入驻新的店铺</div>
					</div>
				</div>
     			<div class="field row" style="height: 55px;line-height: 55px;">
					<label class="field label required two wide">手机号：</label>
					<input class="field value two wide" id="phoneNo" style="height: 35px;" maxlength="11"/>
					<label class="field label inline info" style="width: 245px;text-align: left;">请填写本人手机号，审核信息以短信通知</label>
				</div>
				<div class="field row" style="height: 55px;line-height: 55px;">
					<label class="field label required two wide">短信验证码：</label>
					<input class="field value" id="code" style="height: 35px;" maxlength="6" readonly="readonly"/>
					<button id="btnSendCode" class="normal button" style="width: 120px;">获取短信验证码</button>
					<label id="codeInfo" class="field label inline info" style="width: 245px;text-align: left;line-height: 17px;">请输入短信验证码</label>
				</div>
				<div class="field row" style="height: 55px;line-height: 55px;">
					<label class="field label required two wide">邮箱：</label>
					<input class="field value two wide" id="email" style="height: 35px;" readonly="readonly"/>
					<label class="field label inline info" style="width: 245px;text-align: left;">请填写常用邮箱</label>
				</div>
				<div class="field row" style="height: 55px;line-height: 55px;">
					<label class="field label two wide">昵称：</label>
					<input class="field value two wide" id="nickName" style="height: 35px;" readonly="readonly"/>
				</div>
				<div class="field row" style="height: 55px;line-height: 55px;">
					<label class="field label required two wide">设置登录密码：</label>
					<input class="field value two wide" id="password" type="password" style="height: 35px;" readonly="readonly" maxlength="16"/>
					<label class="field label inline info" style="width: 245px;text-align: left;line-height: 17px;">密码必须为6~16位由（非纯）字母、数字和下划线组成的字符串</label>
				</div>
				<div class="field row" style="height: 20px;line-height: 20px;">
					<div class="field label two wide">&nbsp;</div>
					<div>
						<label  style="float: left">安全强度：</label>
						<div class="password-level1" style="float: left">&nbsp;</div>
					</div>
				</div>
				<div class="field row" style="height: 55px;line-height: 55px;">
					<label class="field label required two wide">确认登录密码：</label>
					<input class="field value two wide" id="confirmPassword" type="password" style="height: 35px;" readonly="readonly" maxlength="16"/>
					<label class="field label inline info" style="width: 245px;text-align: left;">请再次输入密码</label>
				</div>
				<div class="field row" style="height: 55px;line-height: 55px;">
					<label class="field label required two wide">设置支付密码：</label>
					<input class="field value two wide" id="payPassword" type="password" style="height: 35px;" readonly="readonly" maxlength="16"/>
					<label class="field label inline info" style="width: 245px;text-align: left;line-height: 17px;">支付密码必须为6~16位由（非纯）字母、数字和下划线组成的字符串</label>
				</div>
				<div class="field row" style="height: 55px;line-height: 55px;">
					<label class="field label required two wide">确认支付密码：</label>
					<input class="field value two wide" id="confirmPayPassword" type="password" style="height: 35px;" readonly="readonly" maxlength="16"/>
					<label class="field label inline info" style="width: 245px;text-align: left;">请再次输入支付密码</label>
				</div>
				<div class="field row" style="height: 55px;line-height: 55px;">
					<label class="field label required two wide">真实姓名：</label>
					<input class="field value two wide" id="realName" style="height: 35px;" readonly="readonly" maxlength="20"/>
					<label class="field label inline info" style="width: 245px;text-align: left;">请输入2-20位字符</label>
				</div>
				<div class="field row" style="height: 55px;line-height: 55px;">
					<label class="field label required two wide">身份证号码：</label>
					<input class="field value two wide" id="idCertNo" style="height: 35px;" readonly="readonly" maxlength="18"/>
					<label class="field label inline info" style="width: 245px;text-align: left;">请输入与负责人姓名相符的身份证号码</label>
				</div>
				<div class="field row" style="height: 85px;line-height: 85px;">
					<div class="center" style="width: 200px;">
						<input type="checkbox" id="protocol">
						<label class="field label inline" for="protocol">同意金鼎点商户入驻协议</label>
					</div>
			     </div>
			     <div class="field row">&nbsp;</div>
			     <div class="field row" style="height: 85px;line-height: 85px;">
					<div class="next text-white center" id="btnNext" style="height: 35px;line-height: 35px;">下一步</div>
			     </div>
			</div>
			<div id="shop-info" class="form" style="display: none;">
				<div class="field row" style="height: 35px;line-height: 35px;">
				</div>
				<div class="field row" style="height: 55px;line-height: 55px;">
					<label class="field label required  two wide">店铺名称：</label> 
					<input type="text" id="name" class="field value two wide" />
				</div>
				<div class="field row" style="height: 55px;line-height: 55px;">
					<label class="field label required  two wide">店铺地址：</label>
					<select class="field value" id="province"></select>
					<select class="field value" id="city"></select>
					<select class="field value" id="county"></select>
					<select class="field value" id="town" style="display: none;"></select>
				</div>
				<div class="field row" style="height: 55px;line-height: 55px;">
					<label class="field label required  two wide">街道地址：</label>
					<input type="text" id="street" class="field value three wide" maxlength="60" title="请输入街道地址" />
				</div>
				<div class="field row" style="height: 100px;line-height: 100px;">
					<label class="field label required  two wide">经营范围：</label> 
					<textarea id="bizScope" class="field value three wide" style="height:95px;"></textarea>200字以内
				</div>
				<div class="field row">&nbsp;</div>
			     <div class="field row" style="height: 85px;line-height: 85px;">
					<div class="next text-white center" id="btnLast" style="height: 35px;line-height: 35px;">下一步</div>
			     </div>
			</div>
			<div id="result-info" class="center" style="display: none;">
				<div style="height: 100px;">&nbsp;</div>
				<div class="result center">
					<div class="title">等待审核</div>
					<div class="content">
						<div style="height: 30px;line-height: 30px;font-weight: 700;">
						<img src="<%=resBaseUrl%>/image/merchant/u237.png" width="20px" height="30px">
						您的入驻申请已经提交，请耐心等待审核！
						</div>
						<div style="height: 10px;">&nbsp;</div>
						<div style="padding-left: 20px;">您可登录本账号查看审核进度审核时效：1—7个工作日</div>
						<div style="height: 60px;">&nbsp;</div>
						<div class="toShop text-black" style="text-align: center;">去商城逛逛</div>
					</div>
				</div>
			</div>
    		</div>
    	</div>	
</div>

<div style="height: 180px;text-align: center;">
	<div class="filter section center" >
		<div class="filter row center">
			 关于商城    | 联系我们    | 商家入驻    | 人才招聘    | 金鼎点社区
		</div>
		<div class="filter row">
			Copyright© 金鼎点 All Rights Reserved 版权所有
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
	var auditText = ["待审核 ","审核通过","审核未通过"];
	//标记是否可以入驻
	var settledFlag = false;
	var sendFlag = false;
	var existFlag = false;
	//验证码用途
	var usage = "regist";
	//实例化表单代理
	var userFormProxy = FormProxy.newOne();
	userFormProxy.addField({
		id : "phoneNo",
		required : true,
		rules : ["maxLength[11]","isMobile"]
	});
	userFormProxy.addField({
		id : "code",
		required : true,
		rules : ["eqLength[6]", "isNum"],
		messageTargetId : "btnSendCode"
	});
	userFormProxy.addField({
		id : "email",
		required : true,
		rules : ["isEmail", {
			rule : function(idOrName, type, rawValue, curData) {
				validateEmail(rawValue);
				return emailFlag;
			},
			message : "邮箱已被注册！"
		}]
	});
	userFormProxy.addField({
		id : "nickName",
		rules : ["maxLength[30]"]
	});
	userFormProxy.addField({
		id : "realName",
		required : true,
		rules : ["maxLength[30]"]
	});
	userFormProxy.addField({
		id : "idCertNo",
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
	userFormProxy.addField({
		id : "password",
		required : true,
		rules : ["isPwd[true]",{
			rule : function(idOrName, type, rawValue, curData) {
				var value = $id("confirmPassword").val();
				if(!isNoB(value)){
					userFormProxy.validate("confirmPassword");
				}
				return true;
			}
		}]
	});
	userFormProxy.addField({
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
	userFormProxy.addField({
		id : "payPassword",
		required : true,
		rules : ["isPwd[true]",{
			rule : function(idOrName, type, rawValue, curData) {
				var value = $id("confirmPayPassword").val();
				if(!isNoB(value)){
					userFormProxy.validate("confirmPayPassword");
				}
				return true;
			}
		}]
	});
	userFormProxy.addField({
		id : "confirmPayPassword",
		required : true,
		rules : [{
			rule : function(idOrName, type, rawValue, curData) {
				var value = $id("payPassword").val();
				if(isNoB(value)){
					return true;
				}else{
					return rawValue==value;
				}
			},
			message : "输入支付密码不一致！"
		}]
	});
	
	//实例化表单代理 
	
	var shopFormProxy = FormProxy.newOne();
	shopFormProxy.addField({
		id : "name",
		required : true,
		rules : ["maxLength[30]", {
			rule : function(idOrName, type, rawValue, curData) {
				validateShop(rawValue);
				return nameFlag;
			},
			message : "店铺名称已被注册！"
		}]
	});
	shopFormProxy.addField({
		id : "province",
		required : true
	});
	shopFormProxy.addField({
		id : "city",
		required : true
	});
	shopFormProxy.addField({
		id : "county",
		required : true
	});
	shopFormProxy.addField({
		id : "town",
		rules : [ {
			rule : function(idOrName, type, rawValue, curData) {
				// 区若显示且为空，给予提示
				if ($("#town").is(":visible") && isNoB(rawValue)) {
					return false;
				}
	
				return true;
			},
			message : "此为必须提供项！"
		} ]
	});
	shopFormProxy.addField({
		id : "street",
		required : true,
		rules : ["maxLength[60]"]
	});
	shopFormProxy.addField({
		id : "bizScope",
		required : true,
		rules : ["maxLength[60]"]
	});
	
	var emailFlag = false;
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
	
	var nameFlag = false;
	//店铺名称是否可用
	function validateShop(name){
		var ajax = Ajax.post("/shop/exist/by/name/do");
		ajax.data(name);
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			nameFlag = result.data == false;
		});
		ajax.go();
	}

	//发送验证码
	function sendCode(){
		var vldResult = userFormProxy.validate("phoneNo");
		if(vldResult){
			var ajax = Ajax.post("/merch/settled/sms/code/send");
			var phoneNo = $id("phoneNo").val();
			ajax.data(phoneNo);
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data;
					if(data){
						for (i = 1; i <= 60 * 2; i++) {
							window.setTimeout("updateCode( " + i + ") ", i * 1000);
						}
						Layer.msgSuccess("短信验证码已发送至您的手机，请注意查收！");
						$id("code").prop("readonly",false);
						sendFlag = true;
					}else{
						Layer.msgWarning("发送失败，请重新发送！");
					}
				}else{
					Layer.msgWarning(result.message);
				}
				
			});
			ajax.go();
		}
	}
	
	//按钮倒计时
    function updateCode(num) {
        var secs = 60 * 2;
        if (num == secs) {
        	$id("codeInfo").html("请输入短信验证码");
        	$id("btnSendCode").prop("disabled", false);
        } else {
            printnr = secs - num;
            $id("codeInfo").html("校验码已发出，请注意查收短信，如果没有收到，你可以在 "+printnr + "秒后要求系统重新发送！");
            $id("btnSendCode").prop("disabled", true);
        }
    }
	
	function validCode(){
		if(sendFlag){
			var vldResult = userFormProxy.validate("phoneNo") && userFormProxy.validate("code");
			if(vldResult){
				var ajax = Ajax.post("/notify/sms/verf/code/valid");
				var phoneNo = $id("phoneNo").val();
				var vfCode = $id("code").val();
				ajax.data({
					phoneNo : phoneNo,
					vfCode : vfCode,
					usage : usage
				});
				ajax.sync();
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						var data = result.data;
						if(data){
								settledFlag = true;
								var phoneNo = userFormProxy.getValue("phoneNo");
								var postData = {
										phoneNo : phoneNo	
								};
								var ajax = Ajax.post("/merch/get/by/phoneNo");
								ajax.data(postData);
								ajax.sync();
								ajax.done(function(result, jqXhr) {
									var data = result.data;
									if(data){
										userFormProxy.hideMessages();
										existFlag = true;
										$("input").prop("readonly", true);
										$id("phoneNo").prop("readonly", false);
										$id("code").prop("readonly", false);
										$id("protocol").prop("readonly", false);
										$id("name").prop("readonly", false);
										$id("street").prop("readonly", false);
										
										userFormProxy.setValue("nickName",data.user.nickName);
										userFormProxy.setValue("password","123456");
										userFormProxy.setValue("payPassword","123456");
										$id("confirmPassword").val("123456");
										$id("confirmPayPassword").val("123456");
										userFormProxy.setValue("realName",data.user.realName);
										userFormProxy.setValue("idCertNo",data.user.idCertNo);
										userFormProxy.setValue("email",data.user.email);
										var shops = data.shops;
										if(shops){
											var html = "";
											for (var i = 0; i < shops.length; i++) {
												var textShow = "";
												var auditStatus = shops[i].auditStatus;
												if(auditStatus==1){
													textShow = "text-cyan";
												}else if(auditStatus==2){
													textShow = "text-red";
												}else if(auditStatus==0){
													settledFlag = false;
												}
												html+="<div><span class='keepSpace'>     </span>"+(i+1)+"."+shops[i].name+"<span  class='keepSpace'>      </span>"+
												"<span class='"+textShow+"'>"+
												auditText[shops[i].auditStatus]+"</span>"+"</div>";
											}
											$id("shopsInfo").html(html);
											$id("shops").show();
										}else{
											$id("shops").hide();
										}
									}else{
										existFlag = false;
										$("input").prop("readonly", false);
										var phoneNo = $id("phoneNo").val();
										var code = $id("code").val();
										$("input").val("");
										$id("phoneNo").val(phoneNo);
										$id("code").val(code);
									}
								});
								ajax.fail(function() {
									Layer.msgWarning(result.message);
								});
								ajax.go();
						}else{
							Layer.msgWarning("验证码错误");
						}
					}else{
						Layer.msgWarning(result.message);
					}
				});
				ajax.go();
			}
		}
	}
	
	// 隐藏信息区下拉框及错误提示
	function hideTown() {
		$("#town").hide().empty();
		hideMiscTip("town");
	}
	
	// 加载省份
	function loadProvince() {
		// 隐藏页面区
		hideTown();

		var ajax = Ajax.get("/setting/region/selectList/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData("province", result.data);
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}

	// 根据页面选择的省加载市
	function loadCity() {
		// 隐藏页面区
		hideTown();

		var ajax = Ajax.get("/setting/region/selectList/get");
		ajax.params({
			parentId : $("#province").val()
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData("city", result.data);
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}

	// 根据页面选择的市加载县
	function loadCounty() {
		// 隐藏页面区
		hideTown();

		var ajax = Ajax.get("/setting/region/selectList/get");
		ajax.params({
			parentId : $("#city").val()
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData("county", result.data);
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	
	// 根据选择的县加载区
	function loadTown() {
		// 隐藏页面区
		hideTown();

		var ajax = Ajax.get("/setting/region/selectList/get");
		ajax.params({
			parentId : $("#county").val()
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var data = result.data;
				
				// 标记商城基本信息区数量
				var hasTown = 0;
				for(var i = 0, len = data.items.length; i < len; i++){
					hasTown++;
				}

				if (hasTown > 0) {
					loadSelectData("town", data);
					$("#town").show();
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	

	function toNext(){
		var vldResult = false;
		if(existFlag){
			vldResult = userFormProxy.validate("phoneNo") && userFormProxy.validate("code");
		}else{
			vldResult = userFormProxy.validateAll();
		}
		if (!vldResult) {
			return;
		}
		if($id("protocol").is(':checked')){
			hideMiscTip("protocol");
			if(settledFlag){
				$id("user-info").hide();
				$id("shop-info").show();
				
			}else{
				Layer.msgWarning("您有正在等待审核的店铺，请修改手机号码或者等待审核结果");
				return;
			}
		}else{
			showErrorTip("protocol", "请勾选商户入驻协议");
			return;
		}
	}
	

	function toLast(){
		var vldResult = shopFormProxy.validateAll();
		if (!vldResult) {
			return;
		}
		var postData = {
	    	nickName : userFormProxy.getValue("nickName"),		
	    	phoneNo : userFormProxy.getValue("phoneNo"),
	    	idCertNo : userFormProxy.getValue("idCertNo"),			
	    	realName : userFormProxy.getValue("realName"),
	    	password : userFormProxy.getValue("password"),			
	    	payPassword : userFormProxy.getValue("payPassword"),
	    	email : userFormProxy.getValue("email"),
	    	name : shopFormProxy.getValue("name"),			
	    	regionId : parseInt(shopFormProxy.getValue("county")),
			regionName : $("#province option:selected").text() +" "+ $("#city option:selected").text() +" "+ $("#county option:selected").text() +" "+ $("#town option:selected").text(),
	    	street : shopFormProxy.getValue("street"),			
	    	bizScope : shopFormProxy.getValue("bizScope")
		};
		var ajax = Ajax.post("/merch/settled/add/do");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				$id("shop-info").hide();
				$id("result-info").show();
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	
	
	function init(){
		$("input").prop("readonly", true);
		$id("phoneNo").prop("readonly", false);
		$id("code").prop("readonly", false);
		$id("protocol").prop("readonly", false);
		$id("btnSendCode").prop("disabled", false);
		$("input").val("");
		$("textarea").val("");
		$id("protocol").prop("checked",false);
		$id("code").prop("readonly",true);
		
		// 加载省列表
		loadProvince();
	}
	
	//------------------------------------初始化代码--------------------------------------
	$(function() {
		//初始化
		init();
		
		$id("btnSendCode").click(sendCode);
		$id("btnNext").click(toNext);
		$id("btnLast").click(toLast);
		$id("code").blur(validCode);
		$id("protocol").click(function(){
			if($id("protocol").is(':checked')){
				hideMiscTip("protocol");
			}else{
				showErrorTip("protocol", "请勾选商户入驻协议");
			}
		});
		$id("phoneNo").change(function(){
			$("input").prop("readonly", true);
			var phoneNo = $id("phoneNo").val();
			$("input").val("");
			$id("phoneNo").val(phoneNo);
			$id("phoneNo").prop("readonly", false);
		});
		// 绑定区域change事件
		$id("province").change(loadCity);
		$id("city").change(loadCounty);
		$id("county").change(loadTown);
	});
</script>
</body>
</html>