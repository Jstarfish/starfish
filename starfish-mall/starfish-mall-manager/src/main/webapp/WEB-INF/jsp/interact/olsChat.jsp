<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>在线客服</title>
	<style type="text/css">
		/* 交谈对象列表   */
		.ols.peer.list {
			display: block;
			margin: 0;
			padding: 2px;
			width: 100%;
			height: 100%;
			overflow-y: auto;
			list-style: none;
			background-color: #007C7C;
		}
		.ols.peer.list > .item {
			line-height: 20px;
			vertical-align: middle;
			word-wrap: break-word;
			margin: 2px 0;
			padding: 10px 4px;
			cursor: pointer;
			color: #FFF;
		}
		.ols.peer.list > .item > table {
			width: 100%;
			height: 100%;
			table-layout: fixed;
		}
		.ols.peer.list > .item.active {
			border-radius: 18px;
			background-color: #00A5A5;
			color: #000;
			font-weight: bold;
			cursor : default;
		}
		.ols.peer.list > .item.hover {
			border-radius: 18px;
			background-color: #89C2C2;
		}
		.ols.peer.list > .item  .num.hint {
			border-radius: 11px;
			width: 22px;
			height: 22px;
			color: #FF0000;
			background-color: #F1D031;
			display: block;
			text-align: center;
			vertical-align: middle;
		}
		.my.record {
			margin-top : 6px;
			margin-bottom : 14px;
			width : 100%;
			border : none;
			overflow-x:hidden;
		}
		.my.record > .ts {
			width : 100%;
			height : 24px;
			color : gray;
			text-align:center;
		}
		.my.record > .ts >.user {
			float : right;
			padding-left : 20px;
			color : #333;
			border-bottom : 1px dotted #AAA;
		}
		.my.record > .words {
			width : 100%;
			padding-right : 40px;
			color : #666;
			text-align:right;
		}
		
		.peer.record {
			margin-top : 6px;
			margin-bottom : 14px;
			width : 100%;
			border : none;
			overflow-x:hidden;
		}
		.peer.record > .ts {
			width : 100%;
			height : 24px;
			color : gray;
			text-align:center;
			
		}
		.peer.record > .ts >.user {
			float : left;
			padding-right : 20px;
			color : #000;
			border-bottom : 1px dotted #AAA;
			font-weight : bold;
		}
		.peer.record > .words {
			width : 100%;
			padding-left : 40px;
			color : #333;
		}
		.icon.close {
			background: url('<%=resBaseUrl %>/image/icon/msg/icon-close.gif') no-repeat scroll center left;
			background-size: 24px;
		}
	</style>
</head>

<body id="rootPanel">
	<div title="交谈对象列表区" id="peerListPanel" class="ui-layout-west" style="padding:0px;">
		<ul id="peerList" class="ols peer list"></ul>
	</div>
	<div id="centerPanel" class="ui-layout-center" style="padding:0px;">
		<div title="交谈对象信息区" id="peerPanel" class="ui-layout-north" style="padding:4px;background-color:#EFEFEF;">
			<table id="peerInfoTbl" style="width: 100%;height: 100%;table-layout: fixed">
				<tr>
					<td width="60" style="text-align:center;"><img style="border-radius:8px;border:1px solid #EEEEEE;width:40px;height:40px;" name="peerLogo" /></td>
					<td width="200" class="breakWord keepSpace"  name="peerName"></td>
					<td width="50%" name="orgName" style="color:#333;text-align:left;font-size:16px;font-weight:bold;color:navy;"></td>
					<td width="100" style="text-align:right;"><button id="btnClosePeerDlg" class="close icon" style="border-radius:4px;border:1px solid #AAA;text-align:left;cursor:pointer;width:100px;height:32px;line-height:28px;padding:2px 2px 2px 30px;vertical-align:middle;">结束谈话</button></td>
				</tr>
			</table>
		</div>
		<div id="centerPanel2" class="ui-layout-center" style="padding:0px;">
			<div id="centerPanel3" class="ui-layout-center" style="padding:0px;">
				<div id="centerPanel4" class="ui-layout-center" style="padding:0px;background-color:#FDFDFD;">
					<div title="交谈记录区" id="recordPanel" class="ui-layout-center" style="padding:4px;">
						<div id="recordArea" style="width:100%;height:100%;overflow-y: auto;">
							
						
						</div>
					</div>
					<div title="交谈反馈状态区" id="statusPanel" class="ui-layout-south" style="padding:0px 4px;">
						<span style="height:26px;line-height:26px;color:gray;" id="statusContent">aaaa</span>
					</div>
				</div>
				<div id="inputPanel" class="ui-layout-south" style="padding:0px;">
					<table style="width:100%;height:100%;" cellpadding="0" cellspacing="0">
						<tr>
							<td style="height:80%;width:100%;" colspan="2">
							<textarea title="按 Ctrl + 回车键 直接发送" id="inputCtrl" style="width:100%;height:100%;border:0;resize: none;"></textarea>
							</td>
						</tr>
						<tr style="background-color:#EFEFEF;">
							<td style="height:30px; text-align:left; width:60%;"><span id="inputHint" style="color:gray;"></span></td>
							<td style="height:30px; text-align:right;width:120px;"><button id="btnToSpeak" style="border-radius:2px;border:1px solid #AAAAAA;width:100px;height:26px;">发送</button></td>
						</tr>
					</table>
				</div>
			</div>
			<div title="交谈主题信息区" id="subjectPanel" class="ui-layout-east" style="padding:0px;background-color:#EFEFEF;">
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />

	<script type="text/javascript">
			var olsServletPath = "/olsServlet.do";
			var blankCustomerLogoUrl = "http://myeducs.cn/uploadfile/200905/10/2A133241973.png";
			var blankServantLogoUrl = "http://www.qqzhi.com/uploadpic/2014-06-01/023844138.jpg";
			//消息查询时间间隔
			var msgQueryInterval = 5*1000;
			var urlParams = extractUrlParams();
			//
			//会话来源/发起者（C:顾客, S:客服）
			var curSource = urlParams["source"] || "C";
			//会话对方类型
			var curPeerType = curSource == "S" ? "C" : "S";
			//当前用户/发起者信息
			var myId = urlParams["myId"] || null;
			var myInfo = null;
			//当前对话对方信息
			var curPeerId = urlParams["peerId"] || null;
			var curPeerInfo = null;
			//会话焦点
			var focusCode = urlParams["focusCode"] || null;
			var focusId = urlParams["focusId"] || null;
			
			//
			var curSnId = null;
			var snTimeout = 10;
			var lastReqTs = -1;
			//
			var peersInfoMap = {};
			var peersWordsHtmlMap = {};
			var peersUnReadMsgCount = {};
			
			//获取交谈人员信息
			function fetchPeerInfo(type, id, sync) {
				var ajax = Ajax.get(olsServletPath).params({
					action : "peerInfo",
					peerType : type,
					peerId : id
				});
				//
				sync = sync == true;
				//
				if(sync){
					ajax.sync();
				}
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						var peerInfo = result.data || null;
						if (peerInfo) {
							var peerId = peerInfo.id;
							//缓存人员信息
							if (type == curSource) {
								if (myId == peerId) {
									myInfo = peerInfo;
									console && console.log(myInfo);
								}
							} else {
								peersInfoMap[peerId] = peerInfo;
								//
								updatePeerUI(peerInfo);
							}
							return;
						}
					}
					console && console.log(result);
				});
				ajax.go();
			}
			//获取交谈焦点信息
			function fetchFocusInfo(code, id, sync) {
				var ajax = Ajax.get(olsServletPath).params({
					action : "focusInfo",
					focusCode : code,
					focusId : id
				});
				//
				sync = sync == true;
				//
				if(sync){
					ajax.sync();
				}
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						var focusInfo = result.data || null;
						if (focusInfo) {
							console && console.log(focusInfo);
						}
						return;
					}
					console && console.log(result);
				});
				ajax.go();
			}

			//交谈对象列表项加焦点样式
			function focusPeerItem() {
				var peerListItem = $(this);
				if (peerListItem.hasClass("active")) {
					return;
				}
				peerListItem.addClass("hover");
			}
			//交谈对象列表项去焦点样式
			function blurPeerItem() {
				var peerListItem = $(this);
				if (peerListItem.hasClass("active")) {
					return;
				}
				peerListItem.removeClass("hover");
			}
			//更新交谈对象相关界面信息
			function updatePeerUI(peerInfo){
				var peerId = peerInfo.id;
				//更新谈话对象列表信息
				var jqPeerItem = jqPeerList.find(">li[data-peer-id='" + peerId + "']");
				var blankLogoUrl = curPeerType == "S" ? blankServantLogoUrl : blankCustomerLogoUrl;
				var peerLogoUrl = peerInfo.logoUrl || blankLogoUrl;
				jqPeerItem.find("[name='peerLogo']").attr("src", peerLogoUrl);
				jqPeerItem.find("[name='peerName']").text(peerInfo.name || "{未知}");
				if (curPeerType == "S") {
					jqPeerItem.find("[name='orgName']").text(peerInfo.orgName || "{未知}");
					jqPeerItem.find("[name='no']").text(peerInfo.no || "{未知}");
				}
				if (peerId == curPeerId) {
					//更新当前谈话对象其他信息
					curPeerInfo = peerInfo;
					jqPeerInfoTbl.find("[name='peerLogo']").attr("src", peerLogoUrl);
					var peerNameText = peerInfo.name || "{未知}";
					if(curPeerType == "S"){
						peerNameText += " : " + (curPeerInfo.no || "{未知}");
					}
					var peerRole = curPeerType == "S" ? "（客服）" : "（顾客）";
					jqPeerInfoTbl.find("[name='peerName']").text(peerNameText + " " + peerRole);
					if(curPeerType=="S"){
						jqPeerInfoTbl.find("[name='orgName']").text(curPeerInfo.orgName || "{未知}");
					}
					else {
						jqPeerInfoTbl.find("[name='orgName']").empty();
					}
					jqBtnClosePeerDlg.prop("disabled", false);
					jqBtnClosePeerDlg.css("opacity", "1.0");
					jqBtnClosePeerDlg.css("cursor", "pointer");
					//
					var curRecordHtml = peersWordsHtmlMap[curPeerId] || "";
					jqRecordArea.html(curRecordHtml);
				}
			}
			//
			function initPeerUI(){
				jqPeerInfoTbl.find("[name='peerLogo']").attr("src", "");
				//
				if(curPeerType=="S"){
					jqPeerInfoTbl.find("[name='peerName']").text("客服名称");
					jqPeerInfoTbl.find("[name='orgName']").text("所属店铺");
				}
				else {
					jqPeerInfoTbl.find("[name='peerName']").text("顾客名称");
					jqPeerInfoTbl.find("[name='orgName']").text("");
				}
				jqBtnClosePeerDlg.prop("disabled", true);
				jqBtnClosePeerDlg.css("opacity", "0.3");
				jqBtnClosePeerDlg.css("cursor", "default");
				//
				jqRecordArea.html("");
			}
			//清除交谈对象相关界面信息
			function clearPeerUI(peerId){
				if(isNoB(peerId)){
					return;
				}
				//更新谈话对象列表信息
				var jqPeerItem = jqPeerList.find(">li[data-peer-id='" + peerId + "']");
				jqPeerItem.remove();
				//
				if(peerId == curPeerId){
					//更新当前谈话对象其他信息
					var peerInfo = curPeerInfo;
					curPeerInfo = curPeerId = null;
					//
					initPeerUI();
				}
			}
			//更新交谈对象的未读消息数
			function updatePeerUnreadMsgNum(peerId){
				var unReadMsgCount = peersUnReadMsgCount[peerId] || 0;
				var peerListItem = jqPeerList.find(">li[data-peer-id='" + peerId + "']");
				if(peerListItem.size()>0){
					var jqMsgNum = peerListItem.find("[name='peerUnreadMsgNum']");
					if(unReadMsgCount >0){
						jqMsgNum.show();
						jqMsgNum.text(unReadMsgCount);
					}
					else {
						jqMsgNum.hide();
						jqMsgNum.text("");
					}
				}
			}
			//切换到指定的谈话对象
			function switchToPeer(peerListItem) {
				var jqItemDom = $(peerListItem);
				//
				jqPeerList.find(">li[data-peer-id]").removeClass("active hover");
				jqItemDom.addClass("active");
				//
				curPeerId = jqItemDom.data("peerId");
				//
				peersUnReadMsgCount[curPeerId] = 0;
				updatePeerUnreadMsgNum(curPeerId);
				//
				curPeerInfo = peersInfoMap[curPeerId] || {id : curPeerId};
				console.log("当前会话对象 : " + JSON.encode(curPeerInfo));
				//
				updatePeerUI(curPeerInfo);
			}
			function switchToPeerById(peerId) {
				var peerListItem = jqPeerList.find(">li[data-peer-id='" + peerId + "']");
				if(peerListItem.size()>0){
					switchToPeer(peerListItem);
				}
			}
			//生成谈话对象列表项
			function renderPeerList(peerIds) {
				peerIds = peerIds || [];
				var peerListItemTpl = curPeerType == "C" ? peerListItemCustomerTpl : peerListItemServantTpl ;
				for (var i = 0, len = peerIds.length; i < len; i++) {
					var peerId = peerIds[i];
					var peerInfo = peersInfoMap[peerId];
					if (peerInfo == null) {
						fetchPeerInfo(curPeerType, peerId);
					}
					var tmpPeerInfo = peerInfo || {
						id : peerId
					};
					if (jqPeerList.find(">li[data-peer-id='" + peerId + "']").size() == 0) {
						var itemHtml = peerListItemTpl.render(tmpPeerInfo);
						var peerDom = $(itemHtml).appendTo(jqPeerList);
						$(peerDom).data("peerId", peerId);
						$(peerDom).on("mouseenter", focusPeerItem);
						$(peerDom).on("mouseleave", blurPeerItem);
						$(peerDom).click(function() {
							switchToPeer(this);
						});
					}
				}
			}
			//
			function showStatusInfo(statusText){
				$id("statusContent").text(statusText || "");
				//
				setTimeout(function(){
					$id("statusContent").empty();
				}, 5000);
			}
			//显示输入状态信息
			function showInputHint(hintText){
				$id("inputHint").text(hintText || "");
				//
				setTimeout(function(){
					$id("inputHint").empty();
				}, 2000);
			}
			function showCurUnReadMsgHint(count){
				if(count > 0){
					showStatusInfo("刚刚收到 "+count+"消息");
				}
			}
			//启动会话
			function olsStart(){
				var data = {
					eventType : "start",
					eventSource : curSource
				};
				if(curSource == "S"){
					data.servantId = myId;
				}
				else {
					data.customerId = myId;
				}
				//
				var ajax = Ajax.post(olsServletPath).data(data);
				ajax.done(function(result, jqXhr){
					console && console.log(result);
					//
					if(result.type == "start-ack"){
						var attrs = result.attrs;
						curSnId = attrs.snId;
						snTimeOut = attrs.timeout;
						showStatusInfo("会话已建立");
						//
						if(!isNoB(curPeerId)){
							fetchPeerInfo(curPeerType, curPeerId);
							//
							var initPeerIds = [];
							initPeerIds.add(curPeerId);
							renderPeerList(initPeerIds);
							switchToPeerById(curPeerId);
						}
						//
						setTimeout(olsListen, msgQueryInterval);
					}
					else {
						showStatusInfo(result.attrs.reason);
					}
				});
				ajax.go();
			}
			
			/*发送心跳
			function olsHeatbeat(){
				var data = {
					eventType : "heartbeat",
					eventSource : curSource,
					snId : curSnId
				};
				//
				var ajax = Ajax.post(olsServletPath).data(data);
				ajax.done(function(result, jqXhr){
					console && console.log(result);
					//
					if(result.type == "heartbeat-ack"){
						console && console.log(result.type);
					}
					else {
						console && console.log(result.attrs.reason);
					}
				});
				ajax.go();
			}*/
			
			//监听（查询）消息
			function olsListen(){
				var data = {
					eventType : "listen",
					eventSource : curSource,
					snId : curSnId
				};
				if(curSource == "S"){
					data.customerId = curPeerId;
					data.servantId = myId;
				}
				else {
					data.customerId = myId;
					data.servantId = curPeerId;
				}
				//
				var ajax = Ajax.post(olsServletPath).data(data);
				ajax.done(function(result, jqXhr){
					console && console.log(result);
					//
					if(result.type == "listen-ack"){
						console && console.log(result.type);
						var attrs = result.attrs;
						var messageMap = attrs.messages || {};
						var peerIds = KeyMap.from(messageMap).keys();
						renderPeerList(peerIds);
						for(var i=0; i<peerIds.length; i++){
							var peerId = peerIds[i];
							var messages = messageMap[peerId] || [];
							var msgCount = messages.length;
							//
							var unReadMsgCount = peersUnReadMsgCount[peerId] || 0;
							unReadMsgCount += msgCount;
							peersUnReadMsgCount[peerId] = (peerId == curPeerId) ? 0 : unReadMsgCount;
							//
							for(var j=0; j<msgCount; j++){
								var message = messages[j];
								var content = message.content;
								var ts = message.ts;
								var wordsInfo = {
									peerId : peerId,
									content : content,
									ts : ts
								};
								renderPeerWords(wordsInfo);
							}
							//
							if(peerId == curPeerId){
								showCurUnReadMsgHint(unReadMsgCount);
							}
							else {
								updatePeerUnreadMsgNum(peerId);
							}
							//
							setTimeout(function(){
								updatePeerUI(peersInfoMap[peerId]);
							}, 1000);
						}
						//自动定位第一个对象
						if(isNoB(curPeerId) && peerIds.length >0){
							switchToPeerById(peerIds[0]);
						}
						//
						setTimeout(olsListen, msgQueryInterval);
					}
					else{
						var reason = result.attrs && result.attrs.reason;
						if(result.type == "toack"){
							Layer.msgWarning(reason || "因长时间未发言超时了");
						}
						else {
							Layer.msgWarning(reason || "遇到未知问题");
						}
					}
				});
				ajax.go();
			}
			//
			function renderMyWords(wordsInfo){
				var peerId = wordsInfo.peerId;
				var curRecordHtml = peersWordsHtmlMap[peerId] || "";
				var newHtml = myWordsTpl.render(wordsInfo);
				curRecordHtml += newHtml;
				peersWordsHtmlMap[peerId] = curRecordHtml;
				if(peerId == curPeerId){
					jqRecordArea.html(curRecordHtml);
				}
			}
			//
			function renderPeerWords(wordsInfo){
				var peerId = wordsInfo.peerId;
				var curRecordHtml = peersWordsHtmlMap[peerId] || "";
				var newHtml = peerWordsTpl.render(wordsInfo);
				curRecordHtml += newHtml;
				peersWordsHtmlMap[peerId] = curRecordHtml;
				if(peerId == curPeerId){
					jqRecordArea.html(curRecordHtml);
				}
			}
			
			//说话/发言
			function olsSpeak(content){
				var data = {
					eventType : "speak",
					eventSource : curSource,
					snId : curSnId,
					content : content || ""
				};
				if(curSource == "S"){
					data.customerId = curPeerId;
					data.servantId = myId;
				}
				else {
					data.customerId = myId;
					data.servantId = curPeerId;
				}
				//
				var ajax = Ajax.post(olsServletPath).data(data);
				ajax.done(function(result, jqXhr){
					console && console.log(result);
					//
					if(result.type == "speak-ack"){
						jqInputCtrl.val("");
						//
						var attrs = result.attrs;
						//
						var content = attrs.content;
						var ts = attrs.ts;
						var peerId = curPeerType == "C" ? attrs.customerId : attrs.servantId;
						var wordsInfo = {
							peerId : peerId,
							content : content,
							ts : ts
						};
						renderMyWords(wordsInfo);
					}
					else{
						var reason = result.attrs && result.attrs.reason;
						if(result.type == "toack"){
							Layer.msgWarning(reason || "因长时间未发言超时了");
						}
						else {
							showStatusInfo(reason || "遇到未知问题");
						}
					}
				});
				ajax.go();
			}
			
			function olsEnd(){
				if(isNoB(curSnId)){
					return;
				}
				var data = {
					eventType : "end",
					eventSource : curSource,
					snId : curSnId
				};
				//
				var ajax = Ajax.post(olsServletPath).data(data);
				ajax.always(function(result, jqXhr){
					console && console.log(result);
				});
				ajax.go();
			}
			//
			function toDoSpeaking(){
				if(isNoB(curSnId)){
					showInputHint("会话未建立或已断开，发送失败！");
					return;
				}
				var content = jqInputCtrl.val();
				content = content.trim();
				if(isNoB(content)){
					showInputHint("请输入有效内容！");
					return;
				}
				//
				olsSpeak(content);
			}
			//结束与当前对象的交谈
			function closePeerDlg(){
				if(curPeerId == null){
					return;
				}
				//
				clearPeerUI(curPeerId);
				//
				var jqPeerListItems = jqPeerList.find(">li[data-peer-id]");
				if(jqPeerListItems.size() > 0){
					switchToPeer(jqPeerListItems.get(0));
				}
			}
			//全局缓存变量
			var peerListItemCustomerTpl, peerListItemServantTpl, myWordsTpl, peerWordsTpl;
			var jqPeerList, jqPeerInfoTbl, jqRecordArea, jqBtnClosePeerDlg;
			//
			function hideTogglers() {
				$(".ui-layout-resizer").find(">.ui-layout-toggler").hide();
			}
			//窗口关闭时结束整个会话
			$(window).unload(function(){
				olsEnd();
			});
			//
			$(function() {
				//页面外层布局
				$id('rootPanel').layout({
					spacing_open : 1,
					spacing_closed : 1,
					west__size : 300,
					west__sizable : true,
					onresize : hideTogglers
				});
				//内部
				$id('centerPanel').layout({
					spacing_open : 1,
					spacing_closed : 1,
					north__size : 100,
					north__resizable : false,
					onresize : hideTogglers
				});
				//
				$id('centerPanel2').layout({
					spacing_open : 1,
					spacing_closed : 1,
					east__size : 0,
					east__resizable : true,
					onresize : hideTogglers
				});
				//
				$id('centerPanel3').layout({
					spacing_open : 1,
					spacing_closed : 1,
					south__size : 140,
					south__resizable : true,
					onresize : hideTogglers
				});
				//
				$id('centerPanel4').layout({
					spacing_open : 1,
					spacing_closed : 1,
					south__size : 26,
					south__resizable : false,
					onresize : hideTogglers
				});
				//
				$id("centerPanel4").find(">.ui-layout-resizer-south").hide();
				hideTogglers();
				//初始化变量
				peerListItemCustomerTpl = laytpl($id("peerListItemCustomerTpl").html());
				peerListItemServantTpl = laytpl($id("peerListItemServantTpl").html());
				myWordsTpl = laytpl($id("myWordsTpl").html());
				peerWordsTpl = laytpl($id("peerWordsTpl").html());
				//
				jqPeerList = $id("peerList");
				jqPeerInfoTbl = $id('peerInfoTbl');
				jqRecordArea = $id('recordArea');
				jqInputCtrl = $id('inputCtrl');
				$id("btnToSpeak").click(toDoSpeaking);
				jqInputCtrl.keydown(function(evnt){
					if(evnt.ctrlKey && evnt.keyCode == 13){
						$id("btnToSpeak").trigger("click");
					}
				});
				jqBtnClosePeerDlg = $id("btnClosePeerDlg");
				jqBtnClosePeerDlg.click(closePeerDlg);
				initPeerUI();
				//
				if(!isNoB(myId)){
					//获取我的信息
					fetchPeerInfo(curSource, myId, true);
					//启动会话
					olsStart();
				}
				else {
					Layer.msgWarning("缺少url参数");
				}
			});
		</script>
	</body>
	<!-- 在线顾客列表项 -->
	<script type="text/html" id="peerListItemCustomerTpl">
		{{# var peerInfo = d; }}
		<li class="item" data-peer-id="{{peerInfo.id}}">
			<table>
				<tr>
					<td width="40"><img style="border-radius:20px;border:1px solid #EEEEEE;width:32px;height:32px;" name="peerLogo" /></td><td><span name="peerName" class="breakWord keepSpace">{{peerInfo.name || '顾客.'}}</span></td><td width="24"><span style="display:none;" name="peerUnreadMsgNum" class="num hint">&nbsp;</span></td>
				</tr>
			</table>
		</li>
	</script>
	<!-- 在线客服人员列表项 -->
	<script type="text/html" id="peerListItemServantTpl">
		{{# var peerInfo = d; }}
		<li class="item" data-peer-id="{{peerInfo.id}}">
			<table>
				<tr>
					<td width="40"><img style="border-radius:20px;border:1px solid #EEEEEE;width:32px;height:32px;" name="peerLogo" /></td><td><span class="breakWord keepSpace"></span><span name="peerName">{{peerInfo.name || '客服.'}}</span>  [ <span name="orgName" style="color:#333;">{{peerInfo.orgName || '店铺.'}}</span> : <span name="no" style="color:#333;">{{peerInfo.no || '客服号.'}}</span> ]</span></td><td width="24"><span style="display:none;" name="peerUnreadMsgNum" class="num hint">&nbsp;</span></td>
				</tr>
			</table>
		</li>	
	</script>
	
	<script type="text/html" id="myWordsTpl">
		{{# var wordsInfo = d; }}
		<div class="my record">
			<div class="ts"><span class="user">我说：</span></div>
			<div class="words breakWord keepSpace">{{ wordsInfo.content || ""}}</div>
		</div>
	</script>
	<script type="text/html" id="peerWordsTpl">
		{{# var wordsInfo = d; }}
		<div class="peer record">
			<div class="ts">---- {{ wordsInfo.ts }} ----<span class="user">{{ curPeerType == "S" ? '顾客说' : '客服说'}}</span></div>
			<div class="words breakWord keepSpace">{{ wordsInfo.content || ""}}</div>
		</div>
	</script>							
</html>