<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="base.jsf" %>
<!-- 公用 Javascript -->
    <script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery-migrate.min.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery-ui.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery.locale-cn.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/qtip/jquery.qtip.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery.timepicker.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/layout/jquery.layout.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery.scrollstop.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/lazyload/jquery.lazyload.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jqpaginator.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery.superslide.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery.flexslider-min.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/layer/layer.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/layer/extend/layer.ext.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/store.min.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/laytpl.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/common.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js/libext/layer.ext.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js/libext/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js/libext/toolbar.js"></script>
	
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/app.js"></script>
	
	<script type="text/javascript" src="<%=appBaseUrl%>/js/dictSelectLists/get"></script>
	<script type="text/javascript" src="<%=appBaseUrl%>/js/imageSizeDefs/get"></script>
	
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/main.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/login.js"></script>
	
	
	<!-- Global Javascript -->
	<script type="text/javascript">
	    //设置Ajax请求基本路径
	    Ajax.baseUrl = getAppUrl("");
	  	//quick-menu
	    jQuery("#quick-menu,#shopping-cart").slide({ type:"menu", titCell:".nLi", targetCell:".sub",effect:"slideDown",delayTime:300,triggerTime:0,defaultPlay:false,returnDefault:true});
	</script>