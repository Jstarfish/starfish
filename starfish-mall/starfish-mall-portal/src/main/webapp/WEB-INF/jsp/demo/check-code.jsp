<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	
	<title>验证码</title>
</head>
<body>

	<img id="code-image" alt="" src="" width="64" height="64" onclick="changeCode();">
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	
	<script type="text/javascript">	
	
	   function changeCode(){
		 	var imgObj = document.getElementById("code-image");
		 	imgObj.src = "checkCode.do?uid="	+ new Number(new Date());
		}
	
		$(function() {
			changeCode();
		});
	</script>
</body>
</html>