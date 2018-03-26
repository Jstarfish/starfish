<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<style type="text/css">

.list{
	list-style:none;
    margin: 0px;
    width: 100%;
    padding-left: 18px;
    display: inline-block;
}

.list li{
	float:left;
	width: 180px;
	height: 40px;
	line-height: 38px;
	vertical-align: middle;
	/* border-right: 1px dashed #D3D3D3;
	margin: 0px 5px; */
}

div.toggle{
	margin-left:3px;
}

div.module{
	margin: 5px 0px;
}
	
</style>
<title>权限管理</title>
</head>

<body id="rootPanel">
<div id="mainPanel" class="ui-layout-center" style="padding:0px;">
	<div id="tabs" class="noBorder">
		<ul style="border-bottom-color:#ccc;">
			<li><a href="#tab_sys">系统</a></li>
			<li><a href="#tab_mall">商城</a></li>
			<li><a href="#tab_shop">店铺</a></li>
			<li><a href="#tab_wxshop">卫星店</a></li>
			<li style="display: none;"><a href="#tab_agency">代理处</a></li>
			
			<div class="normal group right aligned">
				<button id="saveBtn" class="normal button">
					保存
				</button>
				<span class="normal spacer"></span>
				<button id="cancelBtn" class="normal button">
					取消
				</button>
			</div>
		</ul>
		<div id="tab_sys"></div>
		
		<div id="tab_mall"></div>
		
		<div id="tab_shop"></div>
		
		<div id="tab_wxshop"></div>
		
		<div style="display: none;" id="tab_agency"></div>
			
	</div>
</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		//权限最初始状态列表
		var resBaseUrl = getResUrl("");
		var origMap = new KeyMap();
		//
		var jqTabsCtrl;
		
		$(function() {
			//页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				allowTopResize : false
			});
			//加载tab页面
			jqTabsCtrl = $( "#tabs" ).tabs();
			
			//加载最新状态权限列表
			loadData();
			
			//确认是否保存权限修改
			$id("saveBtn").click(function(){
				var newMap = getChangeStateMap();
				var disabledIds = newMap["disabledIds"];
				if(disabledIds.length > 0){
					var theLayer = Layer.confirm('权限禁用后会删除关联数据，确定要保存修改吗？', function(){
						savaPermission(newMap);
			    	});
				}else{
					savaPermission(newMap);
				}
				
			});
			
			//取消修改
			$id("cancelBtn").click(function(){
				loadData();
			});
			//
			winSizeMonitor.start(adjustCtrlsSize);
		});
		
		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			console.log("mainWidth:" + mainWidth + ", " + "mainHeight:" + mainHeight);
			//

			var tabsCtrlWidth = mainWidth - 4;
			var tabsCtrlHeight = mainHeight - 8;
			jqTabsCtrl.width(tabsCtrlWidth);
			var tabsHeaderHeight = $(">[role=tablist]", jqTabsCtrl).height();
			var tabsPanels = $(">[role=tabpanel]", jqTabsCtrl);
			tabsPanels.width(tabsCtrlWidth - 4 * 2);
			tabsPanels.height(tabsCtrlHeight - tabsHeaderHeight - 30);
		}
		
		//保存权限修改
		function savaPermission(stateNewMap){
			var hintBox = Layer.progress("正在提交数据...");

	 		var ajax = Ajax.post("/perm/status/update/do");
	 		
	 		ajax.data(stateNewMap);
	 		
	    	ajax.done(function(result, jqXhr){
	    		//
	    		if(result.type== "info"){
	    			Layer.msgSuccess(result.message);
	    			//清空初始权限列表
	    			origMap.clear();
	    			//加载最新权限列表
	    			loadData();
	    		}else {
	    			Layer.msgWarning(result.message);
	    		}
	    		
	    	});
	    	ajax.always(function(){
	      		hintBox.hide();
	      	});
	    	
	    	ajax.go();
		}
		
		//获取被修改的权限列表
		function getChangeStateMap(){
			var stateMap = {
				"disabledIds" : [],
				"enabledIds" : []
			};
			//alert('origMap='+JSON.encode(origMap));
			$("div.toggle").each(function(){
				var switchBtn = SwitchButton.newOne(this);
				var id = switchBtn.value();
				var oldState = origMap.get(id);
				//获取开关状态
				var newState = switchBtn.isOff();
				if(newState != oldState){
					var slotKey = newState ? "disabledIds" : "enabledIds";
					stateMap[slotKey].add(parseInt(id));
				}
			});
			//alert('stateMap='+JSON.encode(stateMap));
			return stateMap;
		}
		
		//加载最新状态权限列表
		function loadData(){
			var ajax = Ajax.post("/perm/modu/and/perms/list/get");
	    	ajax.done(function(result, jqXhr){
	    		if(result.type== "info"){
	    			var data = result.data;
		    		var sysHtmlStr = laytpl($id("sysTpl").html()).render(data["sys"]);
		    		var mallHtmlStr = laytpl($id("siteTpl").html()).render(data["mall"]);
		    		var shopHtmlStr = laytpl($id("siteTpl").html()).render(data["shop"]);
		    		var wxshopHtmlStr = laytpl($id("siteTpl").html()).render(data["wxshop"]);
		    		// WJJ
		    		var agencyHtmlStr = laytpl($id("siteTpl").html()).render(data["agency"]);
		    		
		    		//alert(sysHtmlStr);
		    		$id("tab_sys").html(sysHtmlStr);
		    		$id("tab_mall").html(mallHtmlStr);
		    		$id("tab_shop").html(shopHtmlStr);
		    		$id("tab_wxshop").html(wxshopHtmlStr);
		    		//WJJ
		    		$id("tab_agency").html(agencyHtmlStr);
		    		
		    		//加载开关按钮事件
		    		$(".toggle").each(function(){
						var switchBtn = SwitchButton.newOne(this);
						var pid = $(this).attr("id");
						var disabled = $(this).attr("data-disabled");
						switchBtn.value(pid);
						if(disabled == 'true'){
							switchBtn.off();
						}else{
							switchBtn.on();
						}
						
						switchBtn.onChange(function(isOn, value) {
							console.log(isOn + " : " + value);
						});
					});
	    		}
	    		else {
	    			Layer.msgWarning(result.message);
	    		}
	    	});
	    	ajax.go();
		}
		
	</script>
</body>
<!-- 系统tab模板 -->
<script id="sysTpl" type="text/html">
{{# for(var i = 0, len = d.length; i < len; i++){ }}
{{# var m = d[i]; var ps = d[i].permissions;}}
<div class='module'>
	<div class='simple block'><div class='header'>{{ m.name }}</div></div>
	<ul class='list'>
	{{# for(var j = 0, _len = ps.length; j < _len; j++){ }}
		{{# var p = ps[j]; }}
		<li><img src='{{ resBaseUrl }}/image/star.png' style='margin-right:3px; width: 13px; height:13px;' /><label>{{ p.name }}</label></li>
	{{# } }}
	</ul>
</div>
{{# } }}
</script>

<!-- 商城或店铺或代理处tab模板 -->
<script id="siteTpl" type="text/html">
{{# for(var i = 0, len = d.length; i < len; i++){ }}
{{# var m = d[i]; var ps = d[i].permissions;}}
<div class='module'>
	<div class='simple block'><div class='header'>{{ m.name }}</div></div>
	<ul class='list'>
	{{# for(var j = 0, _len = ps.length; j < _len; j++){ }}
		{{# var p = ps[j]; }}
		<li>
			<label>{{ p.name }}</label>
			<div id='{{p.id}}' data-disabled='{{ p.disabled }}' class='toggle' ></div>
		</li>
	{{# } }}
	</ul>
</div>
{{# } }}
</script>
</html>
