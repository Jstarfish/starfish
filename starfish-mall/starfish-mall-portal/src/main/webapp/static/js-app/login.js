var phoneFlag = false;
// 初始化用户登录信息
$(function() {
	var ajax = Ajax.post("/user/current/get");
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			var data = result.data;
			if (data) {
				var html = "";
				var name = data.nickName;
				if (isNoB(name)) {
					name = data.phoneNo;
				}
				var url = getAppUrl("/user/logout/do");
				html = "<span class='welcome'>你好，<span><a href='" + getAppUrl("/ucenter/index/jsp") + "'>" + name + "</a></span>，欢迎来到<a href='" + getAppUrl("/") + "'>亿投车吧</a><span class='reg-quit'><a href='" + url
						+ "'>[退出]</a></span></span>";
				$id("quick-menu").css("width", "auto");
				$id("quick-menu").html(html);
			}
		} else {
		}
	});
	ajax.go();
})

// 0: 未登陆,1:已登录
function getLoginState() {
	var resultFlag = false;
	//
	var ajax = Ajax.get("/user/has/loggedin/get");
	ajax.sync();
	ajax.done(function(result) {
		if (result.type == "info") {
			resultFlag = (result.data == true) ? 1 : 0;
		}
	});
	ajax.go();
	//
	return resultFlag;
}

$(document).ready(function() {

	// 注册手机号栏失去焦点
	$("#regPhoneNo").live("blur", function() {
		var phoneNo = $("#regPhoneNo").val();
		checkPhoneNo();
		var phoneNoFlag = false;
		if (phoneNo == "") {
			$("#phoneNoTip").html("手机号不能为空");
		} else if (!isMobile(phoneNo)) {
			$("#phoneNoTip").html("请输入正确的手机号!");
		} else if (phoneFlag) {
			$("#phoneNoTip").html("手机号已经存在");
		} else {
			$("#phoneNoTip").html("");
			phoneNoFlag = true;
		}

		if (phoneNoFlag) {
			$(".regPhoneNo").find("dd").find("label").addClass("label-succeed");
		} else {
			$(".regPhoneNo").find("dd").find("label").removeClass("label-succeed");
		}

	});

	// 手机号栏失去焦点
	$("#phoneNo").live("blur", function() {
		var phoneNo = $("#phoneNo").val();
		var phoneNoFlag = false;
		checkPhoneNo();
		if (phoneNo == "") {
			$("#phoneNoTip").html("手机号不能为空");
		} else if (!isMobile(phoneNo)) {
			$("#phoneNoTip").html("请输入正确的手机号!");
		} else if (!phoneFlag) {
			$("#phoneNoTip").html("手机号不存在");
		} else {
			$("#phoneNoTip").html("");
			phoneNoFlag = true;
		}

		if (phoneNoFlag) {
			$(".phoneNo").find("dd").find("label").addClass("label-succeed");
		} else {
			$(".phoneNo").find("dd").find("label").removeClass("label-succeed");
		}
	});

	// 邮箱栏失去焦点
	$("#email").live("blur", function() {
		var email = $("#email").val();
		if (email == "") {
			$("#emailTip").html("");
		} else if (!isValidEmail(email)) {
			$("#emailTip").html("邮箱格式不正确");
		} else {
			$("#emailTip").html("");
		}
	});

	// 旧密码栏失去焦点
	$("#oldPassword").live("blur", function() {
		var oldPassword = $("#oldPassword").val();
		if (oldPassword == "") {
			$("#oldPasswordTip").html("密码不能为空");
		} else {
			$("#oldPasswordTip").html("");
		}
	});

	// 密码栏失去焦点
	$("#password").live("blur", function() {
		var password = $("#password").val();
		var passwordFlag = false;
		if (password == "") {
			$("#passwordTip").html("密码不能为空");
		} else if (password.length < 6) {
			$("#passwordTip").html("请输入6-20位的密码");
		} else if (/^[a-zA-Z]+$/ig.test(password) || /^[0-9]+$/ig.test(password)) {
			$("#passwordTip").html("密码不能为纯字母或纯数字");
		} else {
			$("#passwordTip").html("");
			passwordFlag = true;
		}

		if (passwordFlag) {
			$(".password").find("dd").find("label").addClass("label-succeed");
		} else {
			$(".password").find("dd").find("label").removeClass("label-succeed");
		}
	});

	// 确认密码栏失去焦点
	$("#rePassword").live("blur", function() {
		var rePassword = $("#rePassword").val();
		var password = $("#password").val();
		var rePasswordFlag = false;
		if (rePassword == "") {
			$("#rePasswordTip").html("确认密码不能为空");
		} else if (password != rePassword) {
			$("#rePasswordTip").html("您输入的密码不一致");
		} else {
			$("#rePasswordTip").html("");
			rePasswordFlag = true;
		}

		if (rePasswordFlag) {
			$(".rePassword").find("dd").find("label").addClass("label-succeed");
		} else {
			$(".rePassword").find("dd").find("label").removeClass("label-succeed");
		}
	});

	// 支付密码栏失去焦点
	$("#payPassword").live("blur", function() {
		var payPassword = $("#payPassword").val();
		if (payPassword == "") {
			$("#payPasswordTip").html("密码不能为空");
		} else if (payPassword.length < 6) {
			$("#passwordTip").html("请输入6-20位的密码");
		} else if (/^[a-zA-Z]+$/ig.test(payPassword) || /^[0-9]+$/ig.test(payPassword)) {
			$("#passwordTip").html("密码不能为纯字母或纯数字");
		} else {
			$("#payPasswordTip").html("");
		}
	});

	// 确认支付密码栏失去焦点
	$("#rePayPassword").live("blur", function() {
		var rePayPassword = $("#rePayPassword").val();
		var payPassword = $("#payPassword").val();
		if (rePayPassword == "") {
			$("#rePayPasswordTip").html("密码不能为空");
		} else if (payPassword != rePayPassword) {
			$("#rePayPasswordTip").html("您输入的密码不一致");
		} else {
			$("#rePayPasswordTip").html("");
		}
	});

	// 图形验证码栏失去焦点
	$("#chkCode").live("blur", function() {
		var chkCode = $("#chkCode").val();
		if (chkCode == "") {
			$("#chkCodeTip").html("图形验证码不能为空");
		} else {
			$("#chkCodeTip").html("");
		}
	});

	// 手机验证码栏失去焦点
	$("#smsCode").live("blur", function() {
		var phoneCode = $("#phoneCode").val();
		if (phoneCode == "") {
			$("#smsCodeTip").html("手机验证码不能为空");
		} else {
			$("#smsCodeTip").html("");
		}
	});

});

// 检查手机号是否存在
function checkPhoneNo() {
	// 获取手机号
	var phoneNo = $id("regPhoneNo").val();
	if (!phoneNo) {
		phoneNo = $id("phoneNo").val();
	}
	// 生成提交数据
	var postData = ({
		phoneNo : phoneNo
	});
	// 可以对postData进行必要的处理（如如数据格式转换）
	// 显示等待提示框
	//
	var ajax = Ajax.post("/user/exist/by/phoneNo");
	ajax.data(postData);
	ajax.sync();
	ajax.done(function(result, jqXhr) {
		//
		if (result.type == "info") {
			if (result.data) {
				phoneFlag = true;
			} else {
				phoneFlag = false;
			}
		}
	});
	ajax.go();

}
// 会员密码等级 暂定 0，1，2，3，4五级
function getSecurityLevel(password) {
	var securityLevelFlag = 0;
	if (password.length < 8) {
		return 0;
	} else {
		if (/[a-z]/.test(password)) {
			securityLevelFlag++; // lowercase
		}
		if (/[A-Z]/.test(password)) {
			securityLevelFlag++; // uppercase
		}
		if (/[0-9]/.test(password)) {
			securityLevelFlag++; // digital
		}
		if (containSpecialChar(password)) {
			securityLevelFlag++; // specialcase
		}
		return securityLevelFlag;
	}
}
// 匹配特殊符号
function containSpecialChar(str) {
	var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\*)(\()(\))(\-)(\_)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\.)(\/)(\<)(\>)(\?)(\)]+/);
	return (containSpecial.test(str));
}
