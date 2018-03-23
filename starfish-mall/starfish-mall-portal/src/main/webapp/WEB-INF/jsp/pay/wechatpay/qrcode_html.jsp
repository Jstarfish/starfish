<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head.jsp" %>
<div class="content">
    <div class="page-width">
        <div class="successful">
            <ul class="successful-ul" id="successful-ul">
            </ul>
	            <div align="center" id="qrcode">
					<p >
					请打开微信——扫一扫
					<br><br>
					</p>
				</div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
<script type="text/javascript" src="<%=appBaseUrl%>/static/js-app/qrcode.js"></script>
<script type="text/javascript">
 var urlParams = extractUrlParams();
 var orderNo = urlParams.orderNo;
 //alert(orderNo);
 //
 function parseOrderTypeByNo(orderNo){
 		if(!isNoB(orderNo)){
 			return orderNo.charAt(0) == "E" ? "ecardOrder" : "saleOrder";
 		}
 		return null;
 	}
	//webSocket （推送）--------------------------------------------------------
	var requestParams = {
		orderType : parseOrderTypeByNo(orderNo),
		orderNo : orderNo,
		orderAction : "pay"
	};
	var webSocketUrl = makeUrl("/misc/xOrder/message", requestParams);
	console.log(webSocketUrl);
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
				//Toast.show("订单支付成功", 3000, "info");
				//隐藏二维码，展示已支付成功的提示
				$id("qrcode").hide();
				showSuccessInfo(1, resultData);
				
			} else {
				//Toast.show("订单支付失败", 5000, "warn");
				//隐藏二维码，展示订单支付异常，请到订单中心查看。
				$id("qrcode").hide();
				showSuccessInfo(2, resultData);
				
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
	//
	function showSuccessInfo(flag , resultData){
		// 获取模板内容
		if(flag == 1){
			//
			if(resultData.orderType == 'saleOrder'){
				var ajax = Ajax.post("/saleOrder/get/by/no");
		 		ajax.data({
		 			no : resultData.orderNo
		 		});
		 		ajax.done(function(result, jqXhr) {
		 			var data = result.data;
		 			//alert(data);
		 			if (data == null) {
		 				return;
		 			}
					// 获取模板内容
					var tplHtml = $id("successfulTplAsSale").html();
					// 生成/编译模板
					var htmlTpl = laytpl(tplHtml);
					// 根据模板和数据生成最终内容
					var htmlText = htmlTpl.render(data);
					$id("successful-ul").html(htmlText);
				});
				ajax.go();
				
			}else{
				var tplHtml = $id("successfulTplAsEcard").html();
				// 生成/编译模板
				var htmlTpl = laytpl(tplHtml);
				// 根据模板和数据生成最终内容
				var htmlText = htmlTpl.render(resultData.orderNo);
				$id("successful-ul").html(htmlText);
			}
		}else{
			var tplHtml = $id("failFulTpl").html();
			// 生成/编译模板
			var htmlTpl = laytpl(tplHtml);
			// 根据模板和数据生成最终内容
			var htmlText = htmlTpl.render(resultData.orderNo);
			$id("successful-ul").html(htmlText);
		}
		
	}
 //
 $(function(){
 		var ajax = Ajax.post("/pay/wechatpay/create/code/url/do");
 		ajax.data({
 			orderNo : orderNo
 		});
 		ajax.done(function(result, jqXhr) {
 			var data = result.data;
 			//alert(data);
 			if (data == null) {
 				return;
 			}
 			
 			//这个地址是code_url,这个很关键
 			var url = data;
 			if(url == ""){
 				//alert(result.message);
 			}
 			//参数1表示图像大小，取值范围1-10；参数2表示质量，取值范围'L','M','Q','H'
 			var qr = qrcode(10, 'M');
 			qr.addData(url);
 			qr.make();
 			var dom=document.createElement('DIV');
 			dom.innerHTML = qr.createImgTag();
 			var element=document.getElementById("qrcode");
 			element.appendChild(dom);
			
		});
		ajax.go();
		//
		startToMonitor();
 }); 
 
 	
</script> 

</body>

<script type="text/html" id="successfulTplAsEcard">
				{{# var orderNo = d; }}
               <li class="item1 item4">
                    <h1><i></i>您已付款成功，购物时可以选择使用E卡了。</h1>
                    <div class="successful-text">
                        {{# var appUrl = getAppUrl("/ucenter/ecardOrder/list/jsp");}}
                        {{# var homeUrl = getAppUrl("/");}}
                        <span>订单号：{{orderNo}}</span>|<a class="anormal" href="{{appUrl}}">查看订单</a>|<a class="anormal" href="{{homeUrl}}">继续购物</a>
                    </div>
                </li>
</script>
<script type="text/html" id="successfulTplAsSale">
				 {{# var saleOrder = d; }}
    <h1><i></i>您已付款成功，
{{# if(saleOrder.svcPackId==null){}}
请及时到店提货，到店保养您的爱车！
{{# }else{ }}
请凭服务套餐票确认码，到店享用服务，保养您的爱车！
{{#}}}</h1>
    <div class="successful-text">
        <span>订单号：{{saleOrder.no}}</span>|<a class="anormal" href="{{__appBaseUrl}}/saleOrder/detail/jsp?id={{saleOrder.id}}">查看订单</a>|<a class="anormal" href="{{__appBaseUrl}}/product/supermarket/list/jsp">继续购物</a>
{{# if(saleOrder.svcPackId!=null){}}
    |<a class="anormal" href="{{__appBaseUrl}}/ucenter/userSvcPackTick/list/jsp">查看服务套餐票</a>
{{#}}}
    </div>
	{{# if(saleOrder.svcPackId==null){}}
    <div class="successful-text">
        <span>您的订单服务确认码:{{saleOrder.doneCode}}已发送至您的手机{{saleOrder.phoneNo}} （此码请注意保密，到店服务时需提交）</span>
    </div>
	{{#}}}
</script>
<script type="text/html" id="failFulTpl">
				{{# var orderNo = d; }}
               <li class="item1 item4">
                    <h1><i></i>订单支付异常，请到订单中心查看。</h1>
                    <div class="successful-text">
                        <span>订单号：{{orderNo}}</span>|<a class="anormal" href="{{__appBaseUrl}}/ucenter/ecardOrder/list/jsp">查看订单</a>
                    </div>
                </li>
</script>
</html>
