<%@include file="base.jsf" %>
<!-- 文件、内容相关  Javascript -->
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/upload/js/jquery.iframe-transport.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/upload/js/jquery.fileupload.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/upload/js/jquery.xdr-transport.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/upload/js/jquery.fileupload.ext.js"></script>
	
	<script type="text/javascript" src="<%=resBaseUrl%>/lib/ckeditor/ckeditor.js"></script>
	
	<!-- Global Javascript -->
	<script type="text/javascript">
		//相对路径的解析基础
		CKEDITOR.config.baseHref = getAppUrl("/repo/");
		//
	</script>