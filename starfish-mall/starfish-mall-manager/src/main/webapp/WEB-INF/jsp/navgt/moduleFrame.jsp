<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
		<style type="text/css">
		</style>
		<title>Module Frame</title>
	</head>

	<body id="rootPanel" style="overflow: hidden;">
		<div id="leftPanel" class="ui-layout-west" style="padding:0;overflow-y:auto;background-color:#FAFAFA;">
			<div id="funcMenuAccordion" class="nav-blocks"></div>
		</div>
		<div id="mainPanel" class="ui-layout-center">
			<div id="titlePanel" class="ui-layout-north" style="padding:0;background-color:#FAFAFA;"></div>
			<div id="framesPanel" class="ui-layout-center" style="padding:0;">
			</div>
		</div>
		
		<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
		<script type="text/javascript">
			//从请求参数获取模块id
			//var moduleId = ???;
			var moduleId = extractUrlParams().moduleId;
			//alert(moduleId);
			//模拟获取给定（资源）模块的功能资源数据
			function initFunction(moduleId) {
				var postData = {
		    			siteModuleId : parseInt(moduleId)
		 		};
		    	var ajax = Ajax.post("/res/siteFunc/list/get/by/siteModuId");
		 		
		 		ajax.data(postData);
		 		
		    	ajax.done(function(result, jqXhr){
		    		if(result.type== "info"){
		    			var data = result.data || [];
		    			renderFuncMenuList("funcMenuAccordion", data, resMenuItemClickHandler);
		    			//模拟点击功能资源（手风琴）菜单项
		    			if(data.length >0){
		    				var func1st = data[0];
		    				var resList = func1st.resources || [];
		    				var initFuncResId = resList.length >0 ? resList[0].id : null;
		    				if(initFuncResId != null){
								clickFuncMenuItem("funcMenuAccordion", initFuncResId);
		    				}
		    			}
		    		}
		    		else {
		    			Layer.warning(result.message);
		    		}
		    	});
		    	ajax.go();
			}

			//功能资源菜单项点击处理函数
			function resMenuItemClickHandler(id, name, url) {
				//alert(id + ", " + name + ", " + url);
				var frameInfo = {
					id : id,
					src : getAppUrl(url),
					title : name
				};
				framesMgr.open(frameInfo);
			}

			//
			var framesMgr = FramesManager.newOne("framesPanel", "titlePanel");
			
			//
			$(function() {
				//
				$id('rootPanel').layout({
					spacing_open : 1,
					spacing_closed : 1,
					west__size : 200,
					resizable : false,
					onresize : hideLayoutTogglers
				});
				//
				$id('mainPanel').layout({
					inset : 1,
					spacing_open : 1,
					spacing_closed : 1,
					north__size : 33,
					resizable : false,
					onresize : hideLayoutTogglers
				});

				hideLayoutTogglers();
				
				//初始化
				framesMgr.init();

				//生成功能资源（手风琴）菜单
				initFunction(moduleId);
				
			});
		</script>
	</body>
</html>
