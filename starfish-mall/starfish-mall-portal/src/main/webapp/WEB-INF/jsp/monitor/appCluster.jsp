<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	<title>应用集群监控</title>
	<style type="text/css">
		.list {
			width : 100%;
			table-layout : fixed;
			border-collapse: collapse;
			border-spacing : 1;
		}
		.list > thead {
			background-color : #999999;
			color : #FFF;
		}
		.list > tbody {
			background-color : #FFF;
		}
		.list tr>th {
			height : 40px;
			padding-left : 4px;
		}
		.list tr>td {
			height : 40px;
			padding-left : 4px;
		}
		.list tr>th, .list tr>td {
			text-align : left;
		}
		
		.front.row {
			background-color : #FFF;
		}
		.back.row {
			background-color : #EBEBEB;
		}
		.task.row {
			background-color : #E3F3FB;
		}
	</style>
</head>
<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding:0px;">
		<div class="filter section" >
				<div class="filter row">
					<label style="padding-left:4px;font-weight:bold;">应用集群监控</label>
				</div>
		</div>
	</div>
	<div id="mainPanel" class="ui-layout-center" style="padding:0px;">
		<table class="list" cellpadding="0">
			<thead>
				<tr><th width="150">节点名称</th>
					<th width="100">应用名称</th>
					<th>Root Url</th>
					<th width="200">操作系统</th>
					<th width="150">应用状态</th>
					<th width="150">启动时间</th>
					<th width="150">最后报告时间</th>
				</tr>
			</thead>
			<tbody id="appNodeList" class="app-nodes">
			</tbody>
		</table>
	</div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		//URL地址...
		var webSocketUrl = "/appNodeCluster";
		//
		var retryOnClose = true;
		var retryTimer = null;
		var retryInterval = 10 * 1000;
		var webSocket = null;
		//自定义协议请求代码（代表请求何种数据...）
		var requestCode = {
		
		};
		//自定义协议结果代码（可联合使用requestCode...）
		var resultCode = {
			appNodeInfo : 1601
		};
		//打开WebSocket连接并注册结果处理函数
		function openWebSocket() {
			if (getWebSocket() == null) {
				Layer.msgWarning("当前浏览器不支持 WebSocket，请更换浏览器");
				return null;
			} else if (webSocket != null) {
				return webSocket;
			}
			var serverBase = getServerBase();
			var wsServerBase = null;
			if (serverBase.startsWith("https://")) {
				wsServerBase = replaceStr(serverBase, "https://", "wss://");
			} else {
				wsServerBase = replaceStr(serverBase, "http://", "ws://");
			}
			//console.log("Server Base >> " + wsServerBase);
			//
			webSocket = new WebSocket(wsServerBase + getAppUrl(webSocketUrl));
			webSocket.onopen = function(evt) {
				clearTimeout(retryTimer);
				//
				//Toast.show("WebSocket连接已打开", 2000);
				//
				//console.log("WebSocket连接已打开");
			};
			webSocket.onclose = function(evt) {
				webSocket = null;
				//
				console.log("WebSocket连接已关闭");
				//
				if (retryOnClose) {
					retryTimer = setTimeout(openWebSocket, retryInterval);
				}
			};
			webSocket.onerror = function(msg) {
				webSocket = null;
				//
				Toast.show("WebSocket连接发生错误", 5000, "warn");
				//
				//console.log("WebSocket连接发生错误");
			};
			//结果数据处理（...）-----------------------------------------------
			webSocket.onmessage = function(evt) {
				var result = JSON.decode(evt.data);
				if (result != null) {
					if (result.type == "info") {
						var data = result.data;
						if (result.code == resultCode.appNodeInfo) {
							renderAppNodeRow(data);
							//
							//console.log(data);
						} else {
							//var request = {};
							//webSocket.send(JSON.encode(request));
							//
							Layer.msgInfo(data);
						}
					} else {
						Toast.show(result.message, 5000, "warn");
					}
				} else {
					console.log("结果无数据");
				}
			};
		}
		
		//
		var appRoleMap = {};
		appRoleMap["web-front"] = "Web前台";
		appRoleMap["web-back"] = "Web后台";
		appRoleMap["web-task"] = "Web任务";
		appRoleMap["mob-front"] = "Mob前台";
		appRoleMap["mob-back"] = "Mob后台";
		//
		var clusterHeartbeatInterval = null;
		var serverDelaySeconds = 30;
		var serverDownSeconds = 90;
		var appNodeTs= new KeyMap();
		//检查应用节点状态
		function updateAppNodeStatus(jqRow, status){
			var jqStatus = jqRow.find("[name='status']");
			if(status == "warning"){
				jqStatus.text("暂延迟响应");
				jqStatus.removeClass("info error");
				jqStatus.addClass("warning");
			}
			else if(status == "error"){
				jqStatus.text("已停止响应");
				jqStatus.removeClass("info warning");
				jqStatus.addClass("error");
			}
			else {
				jqStatus.text("正常运行中");
				jqStatus.removeClass("error warning");
				jqStatus.addClass("info");
			}
		}
		function checkAppNodesStatus(){
			var nodeNames = appNodeTs.keys();
			var curTime = new Date();
			for(var i=0; i< nodeNames.length; i++){
				var nodeName = nodeNames[i];
				var nodeTs = appNodeTs.get(nodeName);
				var delaySeconds = curTime.diff(nodeTs, "second");
				var jqRow = jqAppNodeList.find(">tr[data-name='"+nodeName+"']");
				var status = "info";
				if(delaySeconds >= serverDelaySeconds && delaySeconds < serverDownSeconds){
					status = "warning";
					//console.log("延迟：" + curTime.format('yyyy-MM-dd HH:mm:ss' +" ->  "+ nodeTs.format('yyyy-MM-dd HH:mm:ss')));
				}
				else if(delaySeconds > serverDownSeconds){
					status = "error";
					//console.log("宕机：" + curTime.format('yyyy-MM-dd HH:mm:ss' +" ->  "+ + nodeTs.format('yyyy-MM-dd HH:mm:ss')));
				}
				updateAppNodeStatus(jqRow, status);
			}
			//
			setTimeout(checkAppNodesStatus, clusterHeartbeatInterval * 500);
		}
		//
		function renderAppNodeRow(appNodeInfo) {
			var appNode = appNodeInfo.appNode;
			if(clusterHeartbeatInterval == null){
				clusterHeartbeatInterval = appNode.clusterHeartbeatInterval;
				checkAppNodesStatus();
				//console.log("报告时间间隔(s)："+clusterHeartbeatInterval);
			}
			appNode.osInfo.isWin = appNode.osInfo.name.toLowerCase().indexOf("win")!=-1;
			var ts = appNodeInfo.ts;
			//
			var appNodeName = appNode.name;
			//console.log(appNodeName +"  "+ts);
			//
			appNodeTs.set(appNodeName, Date.parseAsDate(ts));
			//
			var jqRow = jqAppNodeList.find(">tr[data-name='"+appNodeName+"']");
			if(jqRow.length <1){
				var listItemHtml = jqAppNodeListItemTpl.render(appNodeInfo);
				var listItemDom = $(listItemHtml).appendTo(jqAppNodeList);
				jqRow = $(listItemDom);
			}
			else {
				var jqTs = jqRow.find(">td[name='ts']");
				jqTs.text(ts);
			}
			updateAppNodeStatus(jqRow, "info");
		}
	
		var jqAppNodeList, jqAppNodeListItemTpl;
		// 初始化页面
		$(function() {
			//页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 60,
				allowTopResize : false
			});
			//
			jqAppNodeList = $id("appNodeList");			
			jqAppNodeListItemTpl = laytpl($id("appNodeListItemTpl").html());
			//
			openWebSocket();	
		});
	</script>
</body>
<script type="text/html" id="appNodeListItemTpl">
	{{# var data = d; }}
	<tr height="30" data-name="{{data.appNode.name}}" class="{{data.appNode.role}} row">
		<td name="name">{{data.appNode.name}}</td>
		<td name="roleName">{{appRoleMap[data.appNode.role]}}</td>
		<td name="absBaseUrl"><a href="{{data.appNode.absBaseUrl}}" target="_blank">{{data.appNode.absBaseUrl}}</a></td>
		<td name="osInfo" class="keepSpace"><div class="os icon {{data.appNode.osInfo.isWin ? 'win' : 'lnx'}}">&nbsp;</div> {{data.appNode.osInfo.name}}({{data.appNode.osInfo.arch}})</td>
		<td><div name="status" class="status info">正常运行中</div></td>
		<td name="startedTime">{{data.appNode.startedTime}}</td>
		<td name="ts">{{data.ts}}</td>
	</tr>
</script>
</html>