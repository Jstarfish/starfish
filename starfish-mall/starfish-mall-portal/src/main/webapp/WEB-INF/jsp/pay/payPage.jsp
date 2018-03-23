<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
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

    <link rel="shortcut icon" href="<%=resBaseUrl%>/image/favicon.ico" type="image/x-icon" />
    <!-- Style Sheet -->
    <link rel="stylesheet" href="<%=resBaseUrl%>/lib/jquery/jquery-ui.css" />
    <link rel="stylesheet" href="<%=resBaseUrl%>/lib/qtip/jquery.qtip.css" />
    <link rel="stylesheet" href="<%=resBaseUrl%>/lib/jquery/jquery.timepicker.css" />
    <link rel="stylesheet" href="<%=resBaseUrl%>/lib/layout/layout-default.css" />
    <link rel="stylesheet" href="<%=resBaseUrl%>/css/common/basic.css" />
    <link rel="stylesheet" href="<%=resBaseUrl%>/css/libext/jquery.ext.css" />
    <link rel="stylesheet" href="<%=resBaseUrl%>/css-app/app.css" />

    <!--[if lt IE 9]>
    <script type="text/javascript" src="js/html5/html5shiv.js"></script>
    <![endif]-->
    <script type="text/javascript">
		var __appBaseUrl = "<%=appBaseUrl%>";
		//快捷方式获取应用url
		function getAppUrl(url){
			return __appBaseUrl + (url || "");
		}
	</script>

<title>支付</title>
<link rel="stylesheet" href="<%=resBaseUrl%>/css-app/main.css" />
</head>
<body>
	<div class="page-reg-login">
	    <div class="page-top">
	        <a class="logo" href="<%=appBaseUrl%>"><img src="<%=resBaseUrl%>/image/logo.png" alt=""/></a>
	    </div>
	    		<!-- 第一种：alipay-->
	            <form action="<%=appBaseUrl%>/pay/alipay/pay/by/ali" target="_self" method="post">
		            <div >
		                <ul>
		                    <li>
		                        <label ><input type="text" placeholder="订单号" id="orderId" name="orderId" style="height: 40px"/></label>
		                    </li>
		                    <li>
		                        <label ><input type="text" placeholder="订单名称" id="subject" name="subject" style="height: 40px"/></label>
		                    </li>
		                    <li>
		                        <label ><input type="text" placeholder="订单描述" id="desc" name="desc" style="height: 40px"/></label>
		                    </li>
		                    <li>
		                        <label ><input type="text" placeholder="商品展示地址" id="showUrl" name="showUrl" style="height: 40px"/></label>
		                    </li>
		                    <li>
		                        <label ><input type="text" placeholder="金额" id="amount" name="amount" style="height: 40px"/></label>
		                    </li>
		                </ul>
		                <div class="login-submit">
		                    <input type="submit" value="阿里支付" id="ali"/>
		                </div>
		            </div>
	            </form>
	    		<!-- 第二种：银联-->
	            <%-- <form action="<%=appBaseUrl%>/frontConsume" target="_blank" method="post"> --%>
	            <form action="<%=appBaseUrl%>/pay/unionpay/pay" target="_self" method="post">
	            <div >
	                <!-- <ul>
	                    <li>
	                        <label ><input type="text" placeholder="订单号" id="orderId" name="orderId" style="height: 40px"/></label>
	                    </li>
	                    <li>
	                        <label ><input type="text" placeholder="订单名称" id="subject" name="subject" style="height: 40px"/></label>
	                    </li>
	                    <li>
	                        <label ><input type="text" placeholder="订单描述" id="desc" name="desc" style="height: 40px"/></label>
	                    </li>
	                    <li>
	                        <label ><input type="text" placeholder="商品展示地址" id="showUrl" name="showUrl" style="height: 40px"/></label>
	                    </li>
	                    <li>
	                        <label ><input type="text" placeholder="金额" id="amount" name="amount" style="height: 40px"/></label>
	                    </li>
	                </ul> -->
	                <div class="login-submit">
	                    <input type="submit" value="银联支付" id="yinlian"/>
	                </div>
	            </div>
	            </form>
	            
	</div>

	<jsp:include page="/WEB-INF/jsp/include/script.jsp" />

	<script type="text/javascript">
	
	
		//------------------------------------初始化代码--------------------------------------
		$(function() {
			var centerAdjuster = makeProxy(centerInView, null,
					"rootPanel", window, -20);
			centerAdjuster();
			$(window).resize(centerAdjuster);
			//
			
			//购物车结算  点击-》确定订单信息，点击 提交订单-》
			//绑定 立即支付 按钮，点击的时候，弹出层：支付完成、支付遇到问题
			//点击 支付遇到问题：新打开一个页面，为帮助中心，常见问题，原弹出层不变
			//点击 已完成支付：弹出层消失，原页面跳转到订单中心，有支付按钮 ，点击后，跳到之前提交订单按钮之后的页面。（选择微信支付还是银联支付）
			$id("ali").click(function(){
				
			});
		});
		

	</script>
</body>
</html>
