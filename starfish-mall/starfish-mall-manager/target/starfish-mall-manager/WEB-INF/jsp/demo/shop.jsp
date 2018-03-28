<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>店铺信息</title>
</head>
<body>
	<table width="100%" border="1">
		<tr height="40" style="background-color:gray;color:#FFF;">
			<td colspan="2" class="align center">店铺营业状态</td>
		</tr>
		<tr height="40">
			<td>状态</td>
			<td><label id="status"></label></td>
		</tr>
		<tr height="40">
			<td>操作</td>
			<td><input type="radio" name="sealed" id="sealedN" /><label
				for="sealedN">开放接单</label>&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="radio" name="sealed"
				id="sealedY" /><label for="sealedY">不再接单</label>&nbsp;&nbsp;
				<input class="normal input" style="width:400px;" maxlength="90" id="cause"  />
			</td>
		</tr>
	</table>
	<br />

	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />

	<script type="text/javascript">
		//
		$(function() {
			initFileUpload("fileToUpload");
			initFileUpload("fileToUploadExtract");
			initFileUpload("fileToUploadFileMisc");
			initFileUpload("fileToUpdateFileMisc");
		});
	</script>

</body>
</html>