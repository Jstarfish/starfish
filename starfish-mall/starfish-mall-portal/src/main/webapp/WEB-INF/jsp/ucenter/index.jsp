<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/head.jsp" %>
	<div class="content">
	    <div class="page-width">
	        <div class="crumbs"><a href="">首页</a>&gt;<span>用户中心</span></div>
	        <div class="section">
	            <jsp:include page="/WEB-INF/jsp/include/ucenterMenu.jsp" />
	            <div class="section-right1">
	                <div class="mod-main">
	                    <ul class="user-info">
	                        <li class="user-detail">
	                            <img src="" alt="" id="headImage"/>
	                            <h1 id="nickName"></h1>
	                            <div class="user-level gold">
	                                <i></i><a href="javascript:;" id="gradeVO"></a>
	                            </div>
	                            <div class="user-safe">
	                                <a href="<%=appBaseUrl%>/ucenter/user/safe/jsp">帐户安全：</a>
	                                <!--注：safe1 safe2 safe3 safe4 safe5 safe6六个级别-->
	                                <span class="safe-level safe1">
	                                    <i></i><span></span><!--中 高 很高-->
	                                </span>
	                                <a href="<%=appBaseUrl%>/ucenter/user/safe/jsp">提升</a>
	                            </div>
	                        </li>
	                        <li class="info-detail">
	                            <b>我的订单：</b><span>待付款<a href="javascript:;" id="unhandledCount"></a></span><span>待享用<a href="javascript:;" id="processingCount"></a></span><span>待发博文<a href="javascript:;" id="finishedCount"></a></span>
	                        </li>
	                        <li class="info-detail">
	                            <b>我的e卡：</b><a href="javascript:;" id="ecardCount"></a>张
	                        </li>
	                    </ul>
	                    <br/><br/><br/><br/><br/><br/><br/><br/>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
    <jsp:include page="/WEB-INF/jsp/include/foot.jsp"/>
   	<script type="text/javascript">
   	$(function() {
   		initSaleOrderCount();
   		getMemberInfo();
   		initEcardCount();
   		//绑定e卡列表页面跳转
		$id("ecardCount").click(function() {
			var pageUrl = makeUrl(getAppUrl("/ucenter/ecard/list/jsp"));
			setPageUrl(pageUrl);
		});
		//待付款订单列表页面跳转
		$id("unhandledCount").click(function() {
			var pageUrl = makeUrl(getAppUrl("/ucenter/saleOrder/list/jsp"), {orderState: "unhandled"});
			setPageUrl(pageUrl);
		});
		//待享用订单列表页面跳转
		$id("processingCount").click(function() {
			var pageUrl = makeUrl(getAppUrl("/ucenter/saleOrder/list/jsp"), {orderState: "processing"});
			setPageUrl(pageUrl);
		});
		//待发博文订单列表页面跳转
		$id("finishedCount").click(function() {
			var pageUrl = makeUrl(getAppUrl("/ucenter/saleOrder/list/jsp"), {orderState: "finished"});
			setPageUrl(pageUrl);
		});
	})
	
	//我的订单
	function initSaleOrderCount() {
   		var ajax = Ajax.post("/saleOrder/count/do");
   		ajax.done(function(result, jqXhr) {
   			if (result.type = "info") {
   				var everyCount = result.data;
   				$id("unhandledCount").text(everyCount.unhandledCount);
   				$id("processingCount").text(everyCount.processingCount);
   				$id("finishedCount").text(everyCount.finishedCount);
   			} 
   		});
   		ajax.go();
	}
	   
   	function initEcardCount() {
   		var qry_filterItems = {};
		qry_filterItems.invalid = false;
		var postData = {
				filterItems : qry_filterItems || {},
			};
   		var ajax = Ajax.post("/ecard/userECard/list/get");
   		ajax.data(postData);
   		ajax.done(function(result, jqXhr) {
   			var eCardList = result.data.rows;
   			if (eCardList != null) {
   				$id("ecardCount").text(eCardList.length);
   			} else {
	   		}
	   	});
   		ajax.go();
   	}
	   
  	//获取用户详细信息
	function getMemberInfo() {
		//
		var postData = {};
		var ajax = Ajax.post("/user/info/get");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			//获取用户信息
			if (result.type == "info") {
				var member = result.data;
				//展示用户信息
				showMerberInfo(member);
			} else {

			}
		});
		ajax.fail(function(result, jqXhr) {
			//隐藏等待提示框
			loaderLayer.hide();
		});
		ajax.go();
	}
	//用户信息
	function showMerberInfo(member) {
		$("#headImage").attr("src", member.fileBrowseUrl + "?" + new Date().getTime());
		$id("nickName").html(member.user.nickName? member.user.nickName : member.user.phoneNo);
		$id("gradeVO").html(member.gradeVO.name? member.gradeVO.name : "注册会员");
		var safeLevel = member.user.secureLevel;
		if (safeLevel == 4) {
			$(".safe-level").addClass('safe6'); 
			$(".safe-level").find("span").text("较安全");
		} else if (safeLevel == 3) {
			$(".safe-level").addClass('safe5'); 
			$(".safe-level").find("span").text("安全");
		} else if (safeLevel == 2) {
			$(".safe-level").addClass('safe4'); 
			$(".safe-level").find("span").text("一般");
		} else if (safeLevel == 1) {
			$(".safe-level").addClass('safe3'); 
			$(".safe-level").find("span").text("有风险");
		} else {
			$(".safe-level").addClass('safe2'); 
			$(".safe-level").find("span").text("高风险");
		}
	}
	   
	</script>
	 
	 </body>
</html>

