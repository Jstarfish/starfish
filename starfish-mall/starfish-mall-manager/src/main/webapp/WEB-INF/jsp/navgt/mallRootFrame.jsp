<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
		<title>商城管理</title>
		<style type="text/css">
		.topTable {
			width : 100%;
			height : 100%;
			table-layout: fixed;
			background-color: rgb(51, 123, 146);
			color : #FFF;
		}
		.statusBar {
			width:100%;
			height : 26px;
			line-height:26px;
			text-align:right;
			padding-right:4px;
			color:#DDDDDD;
		}
		</style>
	</head>

	<body id="rootPanel">
		<div id="topPanel" class="ui-layout-north" style="padding: 0;height:80px;">
			<table class="topTable">
			<tr>
				<td rowspan="2" class="align middle" width="360">
					<img id="scopeLogoImg" src="<%=resBaseUrl%>/image/logo.png" style="float:left;margin-left:10px;"  width="135" height="50"/>
					<span id="scopeEntityName" style="float:left;margin-left:30px;margin-top:10px;font-size: 22px;color: #FFF;">商城后台管理</span>
				</td>
				<td width="80%">
					<div class="statusBar">
						<span id="loginStatus" style="display: inline-block; "><span id="userName"></span>,&nbsp;欢迎您登录！ </span>
						&nbsp;[<a style="display: inline-block;text-decoration:none;color:yellow;" href="<%=appBaseUrl%>/user/logout/do">&nbsp;退出&nbsp;</a>]
						<span class="normal spacer"></span><span id="userSetting" style="cursor: pointer;border:1px solid #DDD;padding:2px 4px;border-radius:4px;">个人信息设置</span>
					</div>
				</td>
			</tr>
			<tr>
				<td height="43">
					<div id="topMainPanel" class="ui-layout-center" style="padding:0px;padding-right:4px;"></div>
				</td>
			</tr>
			</table>
		</div>
		<div id="framesPanel" class="ui-layout-center" style="padding:0;"></div>
		
		<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
		<script type="text/javascript">
		
			//模拟资源模块数据
			var moduleListData = [];
			
			//模拟点击模块菜单项
			var initModuleId = 0;
		
			//渲染函数
			function renderDetail(data,html,div){
				var tplHtml = $id(html).html();
				var theTpl = laytpl(tplHtml);
				var htmlStr = theTpl.render(data);
				$(div).html(htmlStr);
			}
			
			//获取登录信息
			function ajaxUser(){
		    	var ajax = Ajax.post("/user/info/context/get");
		    	ajax.done(function(result, jqXhr){
		    		if(result.type== "info"){
		    			var data = result.data;
		    			if(data.userName){
		    				$id("userName").html(data.userName);
		    			}else{
		    				$id("userName").html(data.phoneNo);
		    			}
		    			var entity = data.scopeEntity;
		    			if(entity){
		    				if(entity.logoUrl){
		    					$id("scopeLogoImg").attr("src",entity.logoUrl);
		    				}
		    				$id("scopeEntityName").html(entity.name);
		    			}
		    		}else{
		    			Layer.warning(result.message);
		    			setPageUrl(getAppUrl("/user/login/jsp"));
		    		}
		    	});
		    	ajax.go();
		    }
			
			//初始化用户模块
		    function initModule(){
		    	var ajax = Ajax.post("/res/siteModu/list/get/by/context");
		    	ajax.done(function(result, jqXhr){
		    		if(result.type== "info"){
		    			var data = result.data;
		    			if(data){
		    				for(var i = 0, len = data.length; i < len; i++){
			    				moduleListData.add({
			    					id : data[i].id,
			    					name : data[i].name
			    				});
			    			}
			    			initModuleId = data[0].id;
			    			renderModuleMenuList("topMainPanel", moduleListData, moduleMenuItemClickHandler);
			    			clickModuleMenuItem("topMainPanel", initModuleId);
		    			}else{
		    				setPageUrl(getAppUrl("/navgt/entry/index/jsp"));
		    			}
		    		}
		    		else {
		    			Layer.warning(result.message);
		    		}
		    	});
		    	ajax.go();
		    }
			
			//模块菜单项点击处理函数
			function moduleMenuItemClickHandler(id, name) {
				var frameInfo = {
					id : "module-" + id,
					src : getAppUrl("/navgt/module/frame/jsp?moduleId=" + id),
					title : name
				};
				framesMgr.open(frameInfo);
			}
			
			var framesMgr = FramesManager.newOne("framesPanel");
			$(function() {
				//
				$id('rootPanel').layout({
					spacing_open : 1,
					spacing_closed : 1,
					north__size : 80,
					resizable : false,
					onresize : hideLayoutTogglers
				});
				//
				hideLayoutTogglers();
				//初始化
				framesMgr.init();

				//初始化登录信息
				ajaxUser();
				//生成（资源）模块菜单
				initModule();
				
				//
				$id("userSetting").click(function(){
					var frameInfo = {
						id : "module-userInfo",
						src : getAppUrl("/user/personal/info/jsp")
					};
					framesMgr.open(frameInfo);
					//
					$(".nav-bar").find("li.item.active").each(function(){
						$(this).removeClass("active");
					});
				});
				
			});
		</script>
		
		<script type="text/html" id="content">
		{{# for(var i = 0, len = d.length; i < len; i++){ }}
		<a><div class="item">
      	<div class="content">
          <div class="name">{{ d[i].name }}</div>
      	</div>
  		</div></a>
		{{# } }}
	</script>
	</body>
</html>