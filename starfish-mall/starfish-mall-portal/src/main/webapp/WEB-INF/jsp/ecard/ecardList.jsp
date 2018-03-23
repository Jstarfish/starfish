<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/base.jsf"%><jsp:include
	page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
    <div class="content">
        <div class="page-width">
            <div class="section">
                <div class="section-left3">
                    <div class="ecard-banner">
                        <a href=""><img src="<%=resBaseUrl%>/image-app/temp/ecard-banner.jpg" alt=""/></a>
                    </div>
                </div>
                <div class="section-right3">
                    <div class="block news">
                        <h1>E卡资讯<a class="more" href="">更多&gt;&gt;</a></h1>
                        <ul class="newslist">
                            <li>
                                <a href="">万圣节赠卡活动</a><span class="time">2015-07-10</span>
                            </li>
                            <li>
                                <a href="">万圣节赠卡活动</a><span class="time">2015-07-10</span>
                            </li>
                            <li>
                                <a href="">万圣节赠卡活动</a><span class="time">2015-07-10</span>
                            </li>
                            <li class="last">
                                <a href="">万圣节赠卡活动</a><span class="time">2015-07-10</span>
                            </li>
                        </ul>
                    </div>
                    <div class="block card-srv">
                        <h1>持卡用户服务专区</h1>
                        <ul>
                            <li>
                                自助服务 <input class="btn btn-wautoh20 ml10" type="button" value="查询"/><input class="btn btn-wautoh20 ml10" type="button" value="赠卡"/>
                            </li>
                            <li>
                                销售热线 <span class="ml10">4000982198</span>
                            </li>
                            <li>
                                在线客服
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <!--楼1-start-->
            <ul id="eCardList" class="goods-list1 card-list1 card-index">
            </ul>
            <!--楼1-end-->
        </div>
    </div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
//------------------------------------------初始化--------------------------------------------------------
$(function() {
	ajaxPost();
});
//------------------------------------------加载页面请求----------------------------------------------------
function ajaxPost() {
	var ajax = Ajax.post("/ecard/list/get");
	ajax.done(function(result, jqXhr) {
		var spanLength = null;
		if (result != "" && result.type == "info") {
			var eCardList = result.data;
			if (eCardList != null&& eCardList.length>0) {
				//订单状态
				renderHtml(eCardList, "eCardListRowTpl","eCardList");
			}
			//点击购买
			$("input[name='submit']").click(function() {
				var code =$(this).attr("data-id");
				if(code){
					location.replace(getAppUrl("/eCardOrder/submit/jsp?code="+code));
				}else{
					Layer.warning("购买异常");
				}
			});
		} else {
			Layer.warning(result.message);
	}
});
	ajax.go();
}
//渲染页面内容
function renderHtml(data,fromId,toId) {
	//获取模板内容
	var tplHtml = $id(fromId).html();
	
	//生成/编译模板
	var theTpl = laytpl(tplHtml);
	
	//根据模板和数据生成最终内容
	var htmlStr = theTpl.render(data);
	
	//使用生成的内容
	$id(toId).html(htmlStr);
}
</script>
</body>
<script type="text/html" id="eCardListRowTpl">
	{{# var eCardList = d;}}
	{{# for(var i = 0; i< eCardList.length; i++){ }}
	{{# var ecard=eCardList[i]}}
		{{# var first = i+1}}
		<li {{# if(first%5==1){ }}class="first"{{# } }}>
           <a href="javascript:void(0)">
				<span class="ecard-img">
           			<img src="{{ecard.fileBrowseUrl}}" alt=""/>
					<span>¥&nbsp;{{ecard.faceVal}}</span>
				</span>
           </a>
           <div class="card-btn-box">
               <span class="red">¥&nbsp;{{ecard.price}}</span><input name="submit" data-id="{{ecard.code}}" class="btn btnw85h25 btn-w70" type="button" value="购买"/>
           </div>
       </li>
	{{# } }}
</script>	
</html>
