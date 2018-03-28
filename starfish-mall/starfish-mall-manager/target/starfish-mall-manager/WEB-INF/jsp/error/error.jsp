<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
	<!-- Standard Meta -->
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">

	<!-- Vendor Specific -->
	<!-- Set renderer engine for 360 browser -->
	<meta name="renderer" content="webkit">
	
	<!-- Cache Meta -->
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Pragma" content="no-cache">
	<link rel="stylesheet" href="<%=resBaseUrl%>/css/common/basic.css" />
	<link rel="stylesheet" href="<%=resBaseUrl%>/css/libext/jquery.ext.css" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/common.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/app.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery.js"></script>
	<title>错误页面</title>
	<style type="text/css">
		.hr.vt.page.center {
			position:absolute;
			left:50%;
			top:50%;
			width:800px;
			height:400px;
			margin-left:-400px;
			margin-top:-220px;
			border:1px solid gray;
			border-radius : 6px;
		}
		
	</style>
	<script type="text/javascript">
		var errorInfo = (<%=request.getAttribute("error.info")  %>) || {};
		//
		$(function() {
			$("#errorMessage").text(errorInfo.message || "");
			$("#hintMessage").text(errorInfo.data || "");
			var errCode = errorInfo.code;
			if(errCode == 3101){
				//未登录
				setPageUrl("<%=appBaseUrl%>/user/login/jsp");
			}
		});
	</script>
</head>
<body>
	<div class="hr vt page center">
		<table style="width:100%;height:100%;font-size:16px;">
			<tr><td width="100%" height="50" style="text-align:center;border-bottom:1px dotted gray;font-size:18px;">错误信息</td></tr>
			<tr><td width="100%" height="290" style="text-align:center;color:brown;" id="errorMessage"></td></tr>
			<tr><td width="100%" height="50" style="text-align:center;color:blue;" id="hintMessage"></td></tr>
		</table>
	</div>
</body>
</html>