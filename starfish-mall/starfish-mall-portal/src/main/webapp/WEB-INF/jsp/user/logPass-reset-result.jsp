<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head.jsp"%>
	<div class="content">
        <div class="page-width">
            <div class="login retrieve-section">
                <h1>找回密码</h1>

                <div class="login-reg-cont">
                    <div class="order-process">
                        <ul class="steps-state">
                            <li class="actived"><i>1</i><span>身份验证</span></li>
                            <li class="actived"><i>2</i><span>修改密码</span><em></em></li>
                            <li class="active"><i>3</i><span>找回成功</span></li>
                        </ul>
                    </div>
                    <div class="reg-cont">
                        <div class="success">
                            <i></i><span>恭喜您，您的密码找回成功！</span>
                        </div>
                        <div class="text-center">
                            <input class="btn btn-h40 btn-w156" type="button" value="确定" id="btnGoIndex"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
	
	$(function() {
		$("#btnGoIndex").click(function() {
			var pageUrl = makeUrl(getAppUrl("/user/login/jsp"));
			setPageUrl(pageUrl);
		});
	});
	
	</script>
</body>
<!-- layTpl begin -->
<!-- layTpl end -->
</html>