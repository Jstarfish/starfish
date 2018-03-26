<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head.jsp" %>
<!-- <html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
</head>
<body> -->
	<div class="content">
    <div class="page-width">
        <div class="successful">
            <ul class="successful-ul" id="successful-ul">
            </ul>
	            <div align="center" id="qrcode">
					<p >
					请打开微信——扫一扫
					<br><br>
					</p>
				</div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
<script type="text/javascript" src="<%=appBaseUrl%>/static/js-app/qrcode.js"></script>
<script type="text/javascript">
 var urlParams = extractUrlParams();
 var orderNo = urlParams.orderNo;
 //
 $(function(){
 		var ajax = Ajax.post("/pay/wechatpay/create/code/url/do");
 		ajax.data({
 			orderNo : orderNo
 		});
 		ajax.done(function(result, jqXhr) {
 			var data = result.data;
 			//alert(data);
 			if (data == null) {
 				return;
 			}
 			
 			//这个地址是code_url,这个很关键
 			var url = data;
 			if(url == ""){
 				//alert(result.message);
 			}
 			//参数1表示图像大小，取值范围1-10；参数2表示质量，取值范围'L','M','Q','H'
 			var qr = qrcode(10, 'M');
 			qr.addData(url);
 			qr.make();
 			var dom=document.createElement('DIV');
 			dom.innerHTML = qr.createImgTag();
 			var element=document.getElementById("qrcode");
 			element.appendChild(dom);
			
		});
		ajax.go();
		//
 }); 
 
 	
</script> 

</body>

</html>
