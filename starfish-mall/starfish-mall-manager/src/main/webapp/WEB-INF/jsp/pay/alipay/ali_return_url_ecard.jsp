<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/head.jsp" %>

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
 var params=extractUrlParams(location.href);
 var orderNo=params.out_trade_no;
 $(function(){
	 	var data = orderNo;
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
</script> 
</body>

<script type="text/html" id="successfulTpl">
				{{# var orderNo = d; }}
               <li class="item1 item4">
                    <h1><i></i>您已付款成功，购物时可以选择使用E卡了。</h1>
                    <div class="successful-text">
                        <span>订单号：{{orderNo}}</span>|<a class="anormal" href="{{__appBaseUrl}}/ucenter/ecardOrder/list/jsp">查看订单</a>|<a class="anormal" href="{{__appBaseUrl}}">继续购物</a>
                    </div>
                </li>
</script>
</html>
