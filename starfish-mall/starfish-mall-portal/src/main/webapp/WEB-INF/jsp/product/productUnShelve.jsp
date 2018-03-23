<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/base.jsf" %>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<div class="content">
	<div class="page-width">
		<div class="goods-intr clearfix">
	        <div class="center">
	            <div class="unshelve">
	                <img src="<%=resBaseUrl%>/image-app/sorry.jpg" alt=""/>
	                <div class="unshelve-right">
	                    <h1>很抱歉，您查看的宝贝不存在，可能已被下架。</h1>
	                    <h2>您可以：</h2>
	                    <!-- <p>1. <a style="color:#0a96de;" href="javascript:history.go(-1);">点此返回</a></p> -->
	                    <p>1. 浏览<a style="color:#0a96de;" href="javascript:;" data-role="retProductList">购买其他商品</a>，选择其他替换商品。</p>
	                    <p>2. 您可拨打客服电话，漂亮可爱的客服妹子，会为您查找您周边门店库存情况。</p>
	                </div>
	            </div>
	        </div>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
//----------------------------------------------全局变量----------------------------------------------------

//----------------------------------------------onload----------------------------------------------------
$(function() {
	$("div.content").find("a[data-role='retProductList']").click(goProductListPage);
});

//----------------------------------------------业务处理----------------------------------------------------
//
function goProductListPage(){
	setPageUrl(getAppUrl("/product/supermarket/list/jsp"));
}

</script>

</body>
</html>