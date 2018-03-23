<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <!-- Vendor Specific -->
    <!-- Set renderer engine for 360 browser -->
    <meta name="renderer" content="webkit">
    <!-- Cache Meta -->
    <meta http-equiv="Expires" content="0">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Pragma" content="no-cache">
    <!--<link rel="shortcut icon" href="<%=resBaseUrl%>/image/favicon.ico" type="image/x-icon" />-->
    <link rel="stylesheet" href="<%=resBaseUrl%>/css-app/main.css" />
    <!--[if lt IE 9]>
    <script type="text/javascript" src="<%=resBaseUrl%>/js/html5/html5shiv.js"></script>
    <![endif]-->
    <!-- Global Javascript -->
	<script type="text/javascript">
		var __appBaseUrl = "<%=appBaseUrl%>";
		//快捷方式获取应用url
		function getAppUrl(url){
			return __appBaseUrl + (url || "");
		}
	</script>
    <title>选择门店</title>
    <style>
    .optstatus{
    	width:30px;
    }
    </style>
</head>
	<body>
	<table class="order-td consume-td">
	<tbody>
		<tr class="title title1">
			<td>服务项目</td>
			<td>配件名称</td>
			<td>服务标准</td>
			<td>收费标准</td>
			<td>备注</td>
		</tr>
		<tr>
			<td>更换机油及机油滤清器</td>
			<td>机油、机油滤清器</td>
			<td>本项目不含消除保养灯服务，是否需要解码及其费用，需与安装店协商</td>
			<td>¥30.00</td>
			<td></td>
		</tr>
	</tbody>
</table>
</body>
</html>