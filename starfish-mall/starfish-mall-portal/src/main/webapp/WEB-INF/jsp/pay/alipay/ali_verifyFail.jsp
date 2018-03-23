<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/head.jsp" %>
<div class="content">
    <div class="page-width">
    	<form action="<%=appBaseUrl%>/pay/alipay/pay/by/ali" target="_self" method="post">
	        <input type="hidden" name="orderId" id="orderId"/>
	        <ul class="steps">
	            <li class="actived"><i>1</i><span>我的购物车</span><em></em></li>
	            <li class="actived"><i>2</i><span>填写核对订单</span><em></em></li>
	            <li class="active"><i>3</i><span>订单提交成功</span></li>
	        </ul>
	        <div class="successful">
	            <ul class="successflu-ul">
	                <li class="item3">
	                    <p class="orange mb30">支付返回验证失败，请联系商城客服</p>
	                </li>
	            </ul>
	        </div>
        </form>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
</body>
</html>
