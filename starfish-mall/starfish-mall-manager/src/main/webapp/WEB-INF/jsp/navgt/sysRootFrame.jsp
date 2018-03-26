<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>系统管理</title>
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
					<span id="scopeEntityName" style="float:left;margin-left:30px;margin-top:10px;font-size: 22px;color: #FFF;">系统后台管理</span>
				</td>
				<td width="80%">
					<div class="statusBar">
						<span id="loginStatus" style="display: inline-block; "><span id="userName"></span>,&nbsp;欢迎您登录！ </span>
						&nbsp;[<a style="display: inline-block;text-decoration:none;color:yellow;" href="<%=appBaseUrl%>/user/logout/do">&nbsp;退出&nbsp;</a>]
					</div>
				</td>
			</tr>
			<tr>
				<td height="43">
					<div id="topMainPanel" class="ui-layout-center" style="padding:0px;padding-right:4px;">
						<ul id="nav" class="nav-bar">
							<li class="item active" data-id="1" data-role="link" for="permMgmt">权限管理</li>
							<li class="item" data-id="2" data-role="link" for="roleMgmt">角色管理</li>
							<li id="xmenu" class="item" for="roleMgmt" style="padding-right:30px;">资源模块管理</li>
							<li class="item" data-id="6" data-role="link" for="parmSetting">参数设置</li>
							<li class="item" data-id="3" data-role="link" for="registeMall">注册商城信息</li>
						</ul>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div id="framesPanel" class="ui-layout-center" style="padding: 0;"></div>

	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		//定义全局变量FramesManager
		var framesMgr = FramesManager.newOne("framesPanel");
		var moduleId = 1;
		var moudleName = "";
		var moduleUrl = getAppUrl("/perm/mgmt/jsp");
		
		//菜单点击处理函数
		function clickMenuItem(id, name, url) {
			var frameInfo = {
				id : id,
				src : url,
				title : name
			};
			framesMgr.closeOthers();
			framesMgr.open(frameInfo);
		}
		
		function initFirstMenu(){
			$("#nav >.item").each(function(i,obj){
				var roleName = $(obj).attr("for");
				if(roleName == 'roleMgmt'){
					$(obj).attr("data-url",getAppUrl("/perm/role/mgmt/jsp"));
				}else if(roleName == 'parmSetting'){
					$(obj).attr("data-url",getAppUrl("/setting/sysParam/jsp"));
				}else if(roleName == 'registeMall'){
					$(obj).attr("data-url",getAppUrl("/mall/regist/jsp"));
				}else{
					$(obj).attr("data-url",getAppUrl("/perm/mgmt/jsp"));
				}
			});
		}
		
		function initSecondMenu(){
			//二级菜单 init(id,isAlignRight,width)
			var xmenu = new DropdownMenu().init("xmenu",null,135);
			var menuItems = [ {
				text : "资源注册",
				id : 5,
				name : '',
				url : getAppUrl('/res/sysRes/regist/list/jsp')
			}, {
				text : "模块管理",
				id : 4,
				name : '',
				url : getAppUrl('/res/siteModu/list/jsp')
			}, {
				text : "模块功能管理",
				id : 0,
				name : '',
				url : getAppUrl('/res/siteFunc/mgmt/jsp')
			} ];
			xmenu.setMenuItems(menuItems);
			xmenu.setMenuItemClickHandler(function(data, text) {
				//
				$("#nav").find(">.item.active").removeClass("active");
				$("#xmenu").addClass("active");
				//
				clickMenuItem(data.id, "", data.url);
			});
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
	    			}
	    		}else{
	    			Layer.warning(result.message);
	    			setPageUrl(getAppUrl("/user/login/jsp"));
	    		}
	    	});
	    	ajax.go();
	    }
		
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
			$(".ui-layout-resizer", "#topPanel").css("height", "0");
			
			hideLayoutTogglers();
			//FramesManager初始化，清空framesPanel中的内容
			framesMgr.init();
			//初始化登录信息
			ajaxUser();
			//初始化一级菜单
			initFirstMenu();
			
			//导航条设置
			$("#nav").find(">.item").on("click",function(){
				$("#nav").find(">.item.active").removeClass("active");
				$(this).addClass("active");
			})

			//初始化二级菜单
			initSecondMenu();
			
			//首页展示
			clickMenuItem(moduleId, moudleName, moduleUrl);
			
			//一级菜单[系统模块]单击事件
			$("#nav > [data-role='link']").click(function() {
				//parameters:id,name,url
				clickMenuItem($(this).attr("data-id"),'',$(this).attr("data-url"));
			});
		});
	</script>
</body>
</html>