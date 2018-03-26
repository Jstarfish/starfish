<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head.jsp" %>
<%String no = request.getAttribute("no").toString();%>
<div class="content">
    <div class="page-width">
        <div class="successful">
            <ul class="successful-ul" id="successful-ul">
            </ul>
            <br/><br/><br/><br/><br/><br/>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>

 <script type="text/javascript">
 var no = "<%=no%>";
 $(function(){
 	if(no){
 		var ajax = Ajax.post("/saleOrder/get/by/no");
 		ajax.data({
 			no : no
 		});
 		ajax.done(function(result, jqXhr) {
 			var data = result.data;
 			//alert(data);
 			if (data == null) {
 				return;
 			}
			// 获取模板内容
			var tplHtml = $id("successfulTpl").html();
			// 生成/编译模板
			var htmlTpl = laytpl(tplHtml);
			// 根据模板和数据生成最终内容
			var htmlText = htmlTpl.render(data);
			$id("successful-ul").html(htmlText);
		});
		ajax.go();
 	}
 }); 
</script> 

</body>

<script type="text/html" id="successfulTpl">
				{{# var saleOrder = d; }}
                <li class="item1 item4">
                    <h1><i></i>您已付款成功，请及时到店提货，到店保养您的爱车！</h1>
                    <div class="successful-text">
                        <span>订单号：{{saleOrder.no}} </span>|<a class="anormal" href="{{__appBaseUrl}}/ucenter/saleOrder/list/jsp">查看订单</a>|<a class="anormal" href="{{__appBaseUrl}}">继续购物</a>
                    </div>
                    <div class="successful-text">
                        <span>您的订单服务确认码:{{saleOrder.doneCode}}已发送至您的手机{{saleOrder.phoneNo}} （此码请注意保密，到店服务时需提交）</span>
                    </div>
                </li>
</script>

</html>
