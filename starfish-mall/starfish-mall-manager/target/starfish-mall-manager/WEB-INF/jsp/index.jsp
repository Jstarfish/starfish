<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
		<title>后台管理</title>
	</head>
	<body>
		
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	
		<script type="text/javascript">
		
			$(function() {
				setPageUrl(getAppUrl("/user/login/jsp"));
			});
			
		</script>
		
	</body>
</html>