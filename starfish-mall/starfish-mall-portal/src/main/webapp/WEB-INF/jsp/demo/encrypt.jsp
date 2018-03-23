<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<%@include file="/WEB-INF/jsp/include/base.jsf"%>
<title>js加密示例 jsp 页面</title>
</head>
<body>

	<div style="padding-left:100px;">
		密码：<br/>
		<input id="password" type="text" class="inputx" /><br/><br/><br/>
		<input id="sumbitBtn" type="button" class="normal btn" value="提交" />
	</div>

	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	
	<!-- 加密专用js -->
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/bigint.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/barrett.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/rsa.js"></script>
	<script type="text/javascript" src="<%=appBaseUrl%>/js/encrypt/get"></script>

	<script type="text/javascript">
		function toSumbit() {
			var hintBox = Layer.progress("正在提交数据...");
			//
			var passwordPlain = $id("password").val();
			//...
			//加密密码
			var password = encryptStr(passwordPlain);
			//
			var postData = {
				password : password,
				passwordPlain : passwordPlain
			};
			
			console.log(postData);

			var ajax = Ajax.post("/demo/decrypt/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				$id("result").val(JSON.encode(result));
			});
			ajax.always(function() {
				hintBox.hide();
			});
			ajax.go();
		}
		$(function() {
			$id("sumbitBtn").on("click", toSumbit);
		});
	</script>
</body>
</html>