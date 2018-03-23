<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />

<title>订单消息示例页面</title>
</head>
<body>
	<textarea id="result" style="width: 100%; height: 200px;"></textarea>


	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />

	<script type="text/javascript">
		//webSocket （推送）--------------------------------------------------------
		var requestParams = {
			orderType : "saleOrder",
			orderNo : "Sxxxxxxx",
			orderAction : "pay"
		};
		var webSocketUrl = makeUrl("/misc/xOrder/message", requestParams);
		//
		var retryOnClose = true;
		var retryTimer = null;
		var retryInterval = 10 * 1000;
		var webSocket = null;
		var webSocketClosedAsRequired = false;
		//
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
				if (retryOnClose && !webSocketClosedAsRequired) {
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
			//结果数据处理
			webSocket.onmessage = function(evt) {
				var result = JSON.decode(evt.data);
				if (result != null) {
					console.log(result)
					if (result.type == "info") {
						var data = result.data;
						//处理结果数据
						handleResultData(data);
					} else {
						Toast.show(result.message, 5000, "warn");
					}
				} else {
					console.log("结果无数据");
				}
			};
		}
		//主动关闭webSocket
		function closeWebSocket() {
			webSocketClosedAsRequired = true;
			if (webSocket != null) {
				webSocket.close();
			}
		}
		//ajaxQuery (拉取)--------------------------------------------------------
		var ajaxQueryUrl = makeUrl("/demo/xOrder/message/get", requestParams);
		//
		function fetchByAjax() {
			var ajax = Ajax.get(ajaxQueryUrl);
			ajax.done(function(result) {
				if (result.type == "info") {
					var data = result.data;
					//处理结果数据
					handleResultData(data);
				} else {
					Toast.show(result.message, 5000, "warn");
				}
			});
			ajax.fail(function(result) {
				Toast.show(result.message, 5000, "warn");
			});
			ajax.go();
		}
		//websocket是否支持
		var webSocketSupported = getWebSocket() != null;
		//
		function handleResultData(resultData) {
			if (resultData == null) {
				//未得到有效结果
				if (!webSocketSupported) {
					//拉的方式继续轮训
					setTimeout(fetchByAjax, 5 * 1000);
				}
			} else {
				$id("result").val(JSON.encode(resultData));
				//
				if (resultData.orderActionResult == true) {
					Toast.show("订单支付成功", 3000, "info");
				} else {
					Toast.show("订单支付失败", 5000, "warn");
				}

				//如果可以停止了则
				if (webSocketSupported) {
					closeWebSocket();
				}
			}
		}
		//
		function startToMonitor() {
			if (webSocketSupported) {
				openWebSocket();
			} else {
				fetchByAjax();
			}
		}

		$(function() {
			//
			startToMonitor();
		});
	</script>
</body>
</html>