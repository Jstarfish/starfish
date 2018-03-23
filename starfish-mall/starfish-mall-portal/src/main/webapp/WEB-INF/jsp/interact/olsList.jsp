<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
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

    <!--<link rel="shortcut icon" href="<%=resBaseUrl%>/image/favicon.ico" type="image/x-icon" />-->
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

<title>在线客服</title>
<link rel="stylesheet" href="<%=resBaseUrl%>/css-app/main.css" />
</head>
<body>
<div class="ol-service">
    <div class="ols-top">
        <a href="<%=appBaseUrl%>"><img src="<%=resBaseUrl%>/image/logo1.png" alt=""/></a>
        <span class="chat-objet">花博士在线客服</span>
        <!--<span class="split-line"></span>
        <span class="chat-objet">在线客服</span>-->
    </div>
    <div class="ols-content" id="content">
        <div class="ols-con" id="interacts">
            <div class="msg-notice">
                <i class="notice"></i>
                <p>18739913753_p， 欢迎使用在线客服！</p>
                <p class="notice-tip">温馨提示：如果您对商品规格、介绍等有疑问，可以在商品详情页点击“在线客服”进行咨询，或者在商品详情页“商品咨询”处发起咨询，会得到及时专业的回复！</p>
            </div>
            <h1>请选择您需要咨询的业务：</h1>
            <ul class="msg-consult">
            </ul>
            <div class="ols-adv"><img src="<%=resBaseUrl%>/image-app/temp/adv1.jpg" alt=""/></div>
        </div>
    </div>
</div>
	<!-- 客服列表模板 -->
    <script type="text/html" id="bannerMap">
 	{{# for(var i = 0, len = d.length; i < len; i++){ }}
	{{# var interact = d[i]; }}
		<li>
            <img src="<%=resBaseUrl%>/image/misc/face1.jpg" alt=""/>
            <h2>{{ interact.servantName }}</h2>
            <input class="btn btn-normal" type="button" value="开始咨询" onclick="gochat({{ interact.id }});"/>
        </li>
	{{# } }}
	</script>
<jsp:include page="/WEB-INF/jsp/include/script.jsp" />
<script type="text/javascript">
	$(function(){
   	var dataList = [];  //
   	//获取模板内容
	var tplHtml = $id("bannerMap").html();
	//生成/编译模板
	var htmlTpl = laytpl(tplHtml);
	var ajax = Ajax.post("/interact/servant/list/get");
	ajax.sync();
	ajax.done(function(result, jqXhr) {
		//
		if (result.type == "info") {
			var dataList = result.data;
			//根据模板和数据生成最终内容
			var htmlText = htmlTpl.render(dataList);
			//使用生成的内容
			$id("interacts").find("ul").html(htmlText);
		} 
	});
	ajax.go();
	$('.flexslider').flexslider({
        directionNav: true,
        pauseOnAction: false
    });
   });
	
	function gochat(interactId){
		setPageUrl(getAppUrl("/interact/olsChat/jsp?interactId="+interactId));
	};
</script>
</body>
</html>