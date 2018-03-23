<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	
	<title>示例 jsp 页面</title>
</head>
<body>
	先加载页面，再获取数据 <br/><br/>
	/demo/demo-jsp.jsp
	
	
	
	<textarea id="result" rows="50" cols="100"></textarea>
	
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	
	<script type="text/javascript">
	 	$(function(){
	 		var hintBox = Layer.progress("正在提交数据...");
	 		
	 		
	 		var postData = {
	 			nickName : "弧长诶",
	 			age : 23,
	 			password : "xxx",
	 			marriaged : false
	 		};
	 		
	 		var ajax = Ajax.post("/demo/xxx/data");
	 		
	 		ajax.data(postData);
	 		
	    	ajax.done(function(result, jqXhr){
	    		$id("result").val(JSON.encode(result));
	    		
	    		//Layer.msgInfo("结果已返回。");
	    	});
	    	ajax.always(function(){
	    		hintBox.hide();
	    	});
	    	ajax.go();
	    });
	</script>
</body>
</html>