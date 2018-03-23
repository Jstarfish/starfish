<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">

    <!-- Vendor Specific -->
    <!-- Set renderer engine for 360 browser -->
    <meta name="renderer" content="webkit">

    <!-- Cache Meta -->
    <meta http-equiv="Expires" content="0">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Pragma" content="no-cache">

    <!--<link rel="shortcut icon" href="<%=resBaseUrl%>/image/favicon.ico" type="image/x-icon" />-->
    <!-- Style Sheet -->
    <link rel="stylesheet" href="<%=resBaseUrl%>/lib/jquery/jquery-ui.css" />
    <link rel="stylesheet" href="<%=resBaseUrl%>/lib/qtip/jquery.qtip.css" />
    <link rel="stylesheet" href="<%=resBaseUrl%>/lib/jquery/jquery.timepicker.css" />
    <link rel="stylesheet" href="<%=resBaseUrl%>/lib/layout/layout-default.css" />
    <link rel="stylesheet" href="<%=resBaseUrl%>/css/common/basic.css" />
    <link rel="stylesheet" href="<%=resBaseUrl%>/css/libext/jquery.ext.css" />
    <link rel="stylesheet" href="<%=resBaseUrl%>/css-app/app.css" />

    <!--[if lt IE 9]>
    <script type="text/javascript" src="js/html5/html5shiv.js"></script>
    <![endif]-->
    <script type="text/javascript">
		var __appBaseUrl = "<%=appBaseUrl%>";
		//快捷方式获取应用url
		function getAppUrl(url){
			return __appBaseUrl + (url || "");
		}
	</script>

<title>在线客服</title>
<link rel="stylesheet" href="<%=resBaseUrl%>/css-app/main.css" />
</head>

<body id="rootPanel">
<div class="ol-service">
    <div class="ols-top">
        <a href="<%=appBaseUrl%>"><img src="<%=resBaseUrl%>/image/logo1.png" alt=""/></a>
        <span class="chat-objet">花博士在线客服</span>
        <!--<span class="split-line"></span>
        <span class="chat-objet">在线客服</span>-->
    </div>
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
</div>
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
	<jsp:include page="/WEB-INF/jsp/include/script.jsp" />

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