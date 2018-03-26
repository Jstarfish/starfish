<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	<title>个人信息</title>
</head>
<body id="rootPanel">
<div class="ui-layout-center" style="padding: 4px;" id="mainPanel">
	<div id="theCtrl" class="noBorder">
		<fieldset>
			<legend>基本信息</legend>
			<div class="form">
				<div class="field row">
					<label class="field label two wide required">昵称</label>
					<input class="field value one half wide" type="text" id="nickName" />
				</div>
				<div class="field row">
					<label class="field label two wide required">性别</label>
					<div class="field group">
					<input type="radio" id="gender-M" name="gender" value="M" />
					<label for="gender-M">男</label>
					<input type="radio" id="gender-F" name="gender"  value="F" />
					<label for="gender-F">女</label>
					<input type="radio" id="gender-X" name="gender"  value="X" checked="checked" />
					<label for="gender-X">保密</label>
				</div>
				</div>
				<div class="field row">
					<label class="field label two wide">邮箱</label>
					<input class="field value one half wide" type="text" id="email" />
				</div>
				<div class="field row">
					<label class="field label two wide required">真实姓名</label>
					<input class="field value one half wide" type="text" id="realName" />
				</div>
				<div class="field row">
					<label class="field label two wide">联系电话</label>
					<input class="field value" disabled="disabled" type="text" id="phoneNo" />
				</div>
				<div class="field row">
					<label class="field label two wide">用户角色</label>
					<input class="field value one half wide" disabled="disabled" type="text" id="roles" />
				</div>
				<span class="normal hr divider"></span>
				<div class="field row">
					<div class="align center">
						<button class="normal button" id="btnEdit">修改</button>
						<button class="normal button" id="btnSave">保存</button>
						<span class="normal spacer"></span>
						<button class="normal button" id="btnCancel">取消</button>
					</div>
				</div>
			</div>
		</fieldset>
		
		<fieldset>
			<legend>修改登录密码</legend>
			<div class="form">
				<div class="field row">
					<label class="field label two wide">旧密码</label>
					<input class="field value" type="password" id="password" />
					<a style="color: blue; text-decoration: none; display: none;" id="passwordTip" href="javascript:void(0);">显示符号</a>
				</div>
				<div class="field row">
					<label class="field label two wide">新密码</label>
					<input class="field value" type="password" id="newPassword" />
					<span style="color: blue; display: none;" id="newPasswordTip">密码为6-16位数字、字母（区分大小写）和特殊符号的组合</span>
				</div>
				<div class="field row">
					<label class="field label two wide">重复新密码</label>
					<input class="field value" type="password" id="reNewPassword" />
				</div>
				<span class="normal hr divider"></span>
				<div class="field row">
					<div class="align center">
						<button class="normal button" id="btnPswdEdit">修改</button>
						<button class="normal button" id="btnPswdSave">保存</button>
						<span class="normal spacer"></span>
						<button class="normal button" id="btnPswdCancel">取消</button>
					</div>
				</div>
			</div>
		</fieldset>
	</div>
</div>

<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/bigint.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/barrett.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/rsa.js"></script>
	<script type="text/javascript" src="<%=appBaseUrl%>/js/encrypt/get"></script>
<script type="text/javascript">
	//表单代理
	var formProxy = FormProxy.newOne();
	formProxy.addField({
		id : "nickName",
		required : true,
		rules : [ "maxLength[30]" ]
	});
	formProxy.addField({
		name : "gender",
		required : true
	});
	formProxy.addField({
		id : "email",
		rules : [ "isEmail" ]
	});
	formProxy.addField({
		id : "realName",
		required : true,
		rules : [ "maxLength[30]" ]
	});
	formProxy.addField("phoneNo");
	formProxy.addField("roles");
	
	// 加载用户数据
	function loadData() {
		var hintBox = Layer.progress("正在加载数据...");
		var ajax = Ajax.post("/user/info/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				fillUserInfo(result.data);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.always(function() {
			hintBox.hide();
		});
		ajax.go();
	}
	
	// 渲染用户信息
	function fillUserInfo(data) {
		if (eqNull(data)) {
			return;
		}
	
		textSet("nickName", data.nickName);
		radioSet("gender", data.gender);
		textSet("email", data.email);
		textSet("realName", data.realName);
		textSet("phoneNo", data.phoneNo);
		// 显示角色名
		var roleName, role = data.roles;
		for (var i = 0, len = role.length; i < len; i++) {
			if (!isNoE(role[i].name)) {
				roleName = role[i].name + ",";
			}
		}
		formProxy.setValue("roles", left(roleName, roleName.length - 1));
	}
	
	// 检验旧密码、新密码、重复输入的新密码
	function checkAllPassword() {
		var password = $("#password").val();
		var pLen = password.length;
		var newPassword = $("#newPassword").val();
		var newPLen = newPassword.length;
		var reNewPassword = $("#reNewPassword").val();
		var reNewPLen = reNewPassword.length;
	
		if (isNoE(password)) {
			if (isNoE(newPassword) && isNoE(reNewPassword)) {
				Toast.show("请先输入密码", 2000 ,"warning");
				return false;
			} else {
				showErrorTip("passwordTip", "请输入旧密码");
				return false;
			}
		} else {
			if (pLen < 6 || pLen > 16) {
				showErrorTip("passwordTip", "长度必须在6到16个字符之间");
				return false;
			}
	
			if (isNoE(newPassword)) {
				showErrorTip("newPasswordTip", "请输入新密码");
				return false;
			} else if (newPLen < 6 || newPLen > 16) {
				hideMiscTip("newPasswordTip");
				showErrorTip("newPasswordTip", "长度必须在6到16个字符之间");
				return false;
			}
	
			if (isNoE(reNewPassword)) {
				hideMiscTip("newPasswordTip");
				showErrorTip("reNewPassword", "请再次输入新密码");
				return false;
			} else if (reNewPLen < 6 || reNewPLen > 16) {
				hideMiscTip("reNewPassword");
				showErrorTip("reNewPassword", "长度必须在6到16个字符之间");
				return false;
			} else if (reNewPassword != newPassword) {
				hideMiscTip("reNewPassword");
				showErrorTip("reNewPassword", "新密码输入不一致，请重新输入");
				return false;
			}
	
			hideMiscTip("reNewPassword");
			return true;
		}
	}
	
	// 隐藏所有Tip
	function hideAllTip() {
		hideMiscTip("nickName");
		hideMiscTip("realName");
		hideMiscTip("passwordTip");
		hideMiscTip("newPasswordTip");
		hideMiscTip("reNewPassword");
	}
	
	// 编辑用户信息
	function toEdit() {
		$("#nickName, #email, #realName").prop("disabled", false);
		$("input[name=gender]").prop("disabled", false);
	
		$("#btnEdit").hide();
		$("#btnSave").show();
	}
	
	// 保存用户信息
	function toSave() {
		var hintBox = Layer.progress("正在保存数据...");
		var ajax = Ajax.post("/user/info/update/do");
		var postData = {
			nickName : $("#nickName").val(),
			gender : $("input:radio[name='gender']:checked").val(),
			email : $("#email").val(),
			realName : $("#realName").val()
		};
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				$("input").prop("disabled", true);
	
				$("#btnEdit").show();
				$("#btnSave").hide();
				Layer.msgSuccess(result.message);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.always(function() {
			hintBox.hide();
		});
		ajax.go();
	}
	
	// 取消用户编辑
	function toCancel() {
		// 刷新数据
		loadData();
		$("input").prop("disabled", true);
	
		hideAllTip();
		
		$("#btnSave").hide();
		$("#btnEdit").show();
	}
	
	// 编辑密码
	function toPswdEdit() {
		$("#password, #newPassword, #reNewPassword").prop("disabled", false);
		
		$("#passwordTip").show();
		$("#newPasswordTip").show();
	
		$("#btnPswdEdit").hide();
		$("#btnPswdSave").show();
	}
	
	// 保存密码
	function toPswdSave() {
		var hintBox = Layer.progress("正在保存数据...");
		var ajax = Ajax.post("/user/logPass/update/do");
		var postData = {
			oldPassword : encryptStr($("#password").val()),
			password : encryptStr($("#newPassword").val())
		};
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				$("#password").val('');
				$("#newPassword").val('');
				$("#reNewPassword").val('');
	
				$("#btnPswdEdit").show();
				$("#btnPswdSave").hide();
				Layer.msgSuccess(result.message);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.always(function() {
			hintBox.hide();
		});
		ajax.go();
	}
	
	// 取消用户编辑密码
	function toPswdCancel() {
		$("#passwordTip").hide();
		$("#newPasswordTip").hide();
		$("#password").val('');
		$("#newPassword").val('');
		$("#reNewPassword").val('');
		$("#password, #newPassword, #reNewPassword").prop("disabled", true);
	
		hideAllTip();
		
		$("#btnPswdSave").hide();
		$("#btnPswdEdit").show();
	}
	
	// 初始化页面
	$(function() {
		// 页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			allowTopResize : false,
			onresize : hideLayoutTogglers
		});
	
		// 隐藏布局north分割线
		$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
		// 隐藏布局布局按钮
		hideLayoutTogglers();
	
		// 加载用户数据
		loadData();
	
		// 起始所有内容都处于非编辑状态且不显示密码
		$("input").prop("disabled", true);
		$("#password").val('');
		$("#newPassword").val('');
		$("#reNewPassword").val('');
		$("#btnSave").hide();
		$("#btnPswdSave").hide();
	
		// 鼠标按下查看明文，鼠标松开显示密文
		$("#passwordTip").mousedown(function() {
			$("#password").prop("type", "text");
		});
		$("#passwordTip").mouseup(function() {
			$("#password").prop("type", "password");
		});
	
		// 编辑基本信息
		$("#btnEdit").click(function() {
			toEdit();
		});
	
		// 保存基本信息
		$("#btnSave").click(function() {
			if (formProxy.validateAll()) {
				toSave();
			}
		});
	
		// 编辑基本信息
		$("#btnCancel").click(function() {
			toCancel();
		});
		
		// 编辑密码
		$("#btnPswdEdit").click(function() {
			toPswdEdit();
		});
		
		// 保存密码
		$("#btnPswdSave").click(function() {
			if (checkAllPassword()) {
				hideAllTip();
				toPswdSave();
			}
		});
	
		// 取消密码编辑
		$("#btnPswdCancel").click(function() {
			toPswdCancel();
		});
	});
</script>
</body>
</html>