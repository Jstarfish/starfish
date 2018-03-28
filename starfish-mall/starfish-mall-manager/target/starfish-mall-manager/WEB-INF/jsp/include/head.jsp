<%@include file="base.jsf" %>
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
<link rel="shortcut icon" href="<%=resBaseUrl%>/image/favicon.ico" type="image/x-icon" />

<!-- Style Sheet -->
<link rel="stylesheet" href="<%=resBaseUrl%>/lib/jquery/jquery-ui.css" />
<link rel="stylesheet" href="<%=resBaseUrl%>/lib/qtip/jquery.qtip.css" />
<link rel="stylesheet" href="<%=resBaseUrl%>/lib/jquery/jquery.timepicker.css" />
<link rel="stylesheet" href="<%=resBaseUrl%>/lib/layout/layout-default.css" />
<link rel="stylesheet" href="<%=resBaseUrl%>/lib/colorbox/jquery.colorbox.css" />
<link rel="stylesheet" href="<%=resBaseUrl%>/lib/jqgrid/css/jquery.jqgrid.css">
<jsp:include page="/WEB-INF/jsp/include/head-cms.jsp" />
<link rel="stylesheet" href="<%=resBaseUrl%>/css/common/basic.css" />
<link rel="stylesheet" href="<%=resBaseUrl%>/css/libext/jquery.ext.css" />
<link rel="stylesheet" href="<%=resBaseUrl%>/css-app/navframes.css" />
<link rel="stylesheet" href="<%=resBaseUrl%>/css-app/app.css" />
<link rel="stylesheet" href="<%=resBaseUrl%>/lib/jquery/jquery.datetimepicker.css" />

<!--[if lt IE 9]>
<script type="text/javascript" src="<%=resBaseUrl%>/js/html5/html5shiv.js"></script>
<![endif]-->

<!-- Global Javascript -->
<script type="text/javascript">
    var __appBaseUrl = "<%=appBaseUrl%>";
    //快捷方式获取应用url
    function getAppUrl(url){
        url = url || "";
        return url.indexOf("http") == 0 ?  url : __appBaseUrl + url;
    }
    var __resBaseUrl = "<%=resBaseUrl%>";
    //快捷方式获取资源url
    function getResUrl(url){
        url = url || "";
        return url.indexOf("http") == 0 ?  url : __resBaseUrl + url;
    }
</script>