<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	<title>商城设置</title>
</head>
<body id="rootPanel">
<div class="ui-layout-center" style="padding:0px;" id="mainPanel">
	<div id="tabs" class="noBorder">
		<ul id="tabsHead">
			<li><a href="#tab_mallAgreement" id="tabMallAgreement">商家协议</a></li>
			<li><a href="#tab_agentAgreement" id="tabAgentAgreement">代理商协议</a></li>
			<li><a href="#tab_vendorAgreement" id="tabVendorAgreement">供应商协议</a></li>
			<li><a href="#tab_memberAgreement" id="tabMemberAgreement">会员协议</a></li>
		</ul>
		
		<!-- 商家协议 -->
		<div class="form" id="tab_mallAgreement">
			<div class="field row" id="mallAgreementWarnDiv">
				<label class="field label" id="warnMsg"></label>
				<span style="color: #666;">商家协议内容不超过2000个字符</span>
			</div>			
			<textarea class="editor1" id="mallAgreementContent"></textarea>
			<div class="field row align center" id="btnMallAgree">
				<button class="normal button" id="btnMallAgreeSave">保存</button>
				<span class="normal spacer"></span>
				<button class="normal button" id="btnMallAgreeCancel">取消</button>
			</div>
		</div>
		
		<!-- 代理商协议 -->
		<div class="form" id="tab_agentAgreement">
			<div class="field row" id="agentAgreementWarnDiv">
				<label class="field label" id="warnMsg"></label>
				<span style="color: #666;">商家协议内容不超过2000个字符</span>
			</div>			
			<textarea class="editor3" id="agentAgreementContent"></textarea>
			<div class="field row align center" id="btnAgentAgree">
				<button class="normal button" id="btnAgentAgreeSave">保存</button>
				<span class="normal spacer"></span>
				<button class="normal button" id="btnAgentAgreeCancel">取消</button>
			</div>
		</div>
		
		<!-- 供应商协议 -->
		<div class="form" id="tab_vendorAgreement">
			<div class="field row" id="vendorAgreementWarnDiv">
				<label class="field label" id="warnMsg"></label>
				<span style="color: #666;">商家协议内容不超过2000个字符</span>
			</div>			
			<textarea class="editor4" id="vendorAgreementContent"></textarea>
			<div class="field row align center" id="btnVendorAgree">
				<button class="normal button" id="btnVendorAgreeSave">保存</button>
				<span class="normal spacer"></span>
				<button class="normal button" id="btnVendorAgreeCancel">取消</button>
			</div>
		</div>
		
		<!-- 会员协议 -->
		<div class="form" id="tab_memberAgreement">
			<div class="field row" id="memberAgreementWarnDiv">
				<label class="field label" id="warnMsg"></label>
				<span style="color: #666;">商家协议内容不超过2000个字符</span>
			</div>			
			<textarea class="editor2" id="memberAgreementContent"></textarea>
			<div class="field row align center" id="btnMemberAgree">
				<button class="normal button" id="btnMemberAgreeSave">保存</button>
				<span class="normal spacer"></span>
				<button class="normal button" id="btnMemberAgreeCancel">取消</button>
			</div>
		</div>
		
	</div>
</div>

<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
	// 商城协议表单代理
	var agreementFormProxy = FormProxy.newOne();
	// 富文本编辑器
	var theEditor1 = null;
	// 富文本编辑器
	var theEditor2 = null;
	// 富文本编辑器
	var theEditor3 = null;
	// 富文本编辑器
	var theEditor4 = null;
	
	// 加载商城协议
	function loadMallAgreement() {
		var ajax = Ajax.post("/mall/mallAgreement/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var data = result.data;
				if (data != null) {
					// 加载商家协议
					CKEDITOR.instances["mallAgreementContent"].setData(data.content);
				} else {
					initAgreement();
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	
	// 加载会员协议
	function loadMemberAgreement() {
		var ajax = Ajax.post("/mall/memberAgreement/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var data = result.data;
				if (data != null) {
					// 加载会员协议
					CKEDITOR.instances["memberAgreementContent"].setData(data.content);
				} else {
					initAgreement();
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	
	// 加载代理商协议
	function loadAgentAgreement() {
		var ajax = Ajax.post("/mall/agentAgreement/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var data = result.data;
				if (data != null) {
					// 加载代理协议
					CKEDITOR.instances["agentAgreementContent"].setData(data.content);
				} else {
					initAgreement();
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	
	// 加载供应商协议
	function loadVendorAgreement() {
		var ajax = Ajax.post("/mall/vendorAgreement/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var data = result.data;
				if (data != null) {
					// 加载代理协议
					CKEDITOR.instances["vendorAgreementContent"].setData(data.content);
				} else {
					initAgreement();
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}

	// 检验商家协议
	function checkMallAgreement() {
		var agreContent = theEditor1.getData();
		agreementFormProxy.addField({
			id : theEditor1.id,
			get : function(idOrName, type, curData, asRawVal) {
				return agreContent;
			},
			required : true,
			rules : [ {
				rule : function(idOrName, type, rawValue, curData) {
					// 不计算文本附带的特殊标记占用的8个字符位
					if (agreContent.length > 2008) {
						return false;
					}
					return true;
				},
				message : "长度必须在1到2000个字符之间"
			} ],
			messageTargetId : "warnMsg"
		});
		return agreementFormProxy.validateAll();
	}
	
	// 检验会员协议
	function checkMemberAgreement() {
		var agreContent = theEditor2.getData();
		agreementFormProxy.addField({
			id : theEditor2.id,
			get : function(idOrName, type, curData, asRawVal) {
				return agreContent;
			},
			required : true,
			rules : [ {
				rule : function(idOrName, type, rawValue, curData) {
					// 不计算文本附带的特殊标记占用的8个字符位
					if (agreContent.length > 2008) {
						return false;
					}
					return true;
				},
				message : "长度必须在1到2000个字符之间"
			} ],
			messageTargetId : "warnMsg"
		});
		return agreementFormProxy.validateAll();
	}
	
	// 检验代理商协议
	function checkAgentAgreement() {
		var agreContent = theEditor3.getData();
		agreementFormProxy.addField({
			id : theEditor3.id,
			get : function(idOrName, type, curData, asRawVal) {
				return agreContent;
			},
			required : true,
			rules : [ {
				rule : function(idOrName, type, rawValue, curData) {
					// 不计算文本附带的特殊标记占用的8个字符位
					if (agreContent.length > 2008) {
						return false;
					}
					return true;
				},
				message : "长度必须在1到2000个字符之间"
			} ],
			messageTargetId : "warnMsg"
		});
		return agreementFormProxy.validateAll();
	}
	
	// 检验供应商协议
	function checkVendorAgreement() {
		var agreContent = theEditor4.getData();
		agreementFormProxy.addField({
			id : theEditor4.id,
			get : function(idOrName, type, curData, asRawVal) {
				return agreContent;
			},
			required : true,
			rules : [ {
				rule : function(idOrName, type, rawValue, curData) {
					// 不计算文本附带的特殊标记占用的8个字符位
					if (agreContent.length > 2008) {
						return false;
					}
					return true;
				},
				message : "长度必须在1到2000个字符之间"
			} ],
			messageTargetId : "warnMsg"
		});
		return agreementFormProxy.validateAll();
	}

	// 保存商城协议
	function saveMallAgreement() {
		var ajax = Ajax.post("/mall/mallAgreement/save/do");
		ajax.data({
			content : CKEDITOR.instances["mallAgreementContent"].getData()
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	
	// 保存代理商协议
	function saveAgentAgreement() {
		var ajax = Ajax.post("/mall/agentAgreement/save/do");
		ajax.data({
			content : CKEDITOR.instances["agentAgreementContent"].getData()
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	
	// 保存供应商协议
	function saveVendorAgreement() {
		var ajax = Ajax.post("/mall/vendorAgreement/save/do");
		ajax.data({
			content : CKEDITOR.instances["vendorAgreementContent"].getData()
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	
	// 保存会员协议
	function saveMemberAgreement() {
		var ajax = Ajax.post("/mall/memberAgreement/save/do");
		ajax.data({
			content : CKEDITOR.instances["memberAgreementContent"].getData()
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}

	// 初始化商城协议信息
	function initAgreement() {
		// 初始化商家协议
		CKEDITOR.instances["mallAgreementContent"].setData('');
	}

	// 隐藏商城设置页面所有错误提示
	function hideAllMessages() {
		agreementFormProxy.hideMessages();
	}
	
	//
	function getCallbackAfterGridLoaded(){}
	
	function adjustCtrlsSize(){
		adjustMallAgreeSize();
		adjustAgentAgreeSize();
		adjustMemberAgreeSize();
		adjustVendorAgreeSize();
	}
	
	// 调整商城协议页面布局
	function adjustMallAgreeSize(){
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		
		var tabsHeadHeight = $id("tabsHead").height();
		var agreeWarnHeight = $id("mallAgreementWarnDiv").height();
		var btnAgreeHeight = $id("btnMallAgree").height();
		var editorWidth = mainWidth - 10;
		var editorHeight = mainHeight - 20;
		theEditor1.resize(editorWidth, editorHeight - tabsHeadHeight - agreeWarnHeight - btnAgreeHeight - 20);
	}
	// 调整会员协议页面布局
	function adjustMemberAgreeSize(){
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		
		var tabsHeadHeight = $id("tabsHead").height();
		var agreeWarnHeight = $id("memberAgreementWarnDiv").height();
		var btnAgreeHeight = $id("btnMemberAgree").height();
		var editorWidth = mainWidth - 10;
		var editorHeight = mainHeight - 20;
		theEditor2.resize(editorWidth, editorHeight - tabsHeadHeight - agreeWarnHeight - btnAgreeHeight - 20);
	}
	// 调整代理协议页面布局
	function adjustAgentAgreeSize(){
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		
		var tabsHeadHeight = $id("tabsHead").height();
		var agreeWarnHeight = $id("agentAgreementWarnDiv").height();
		var btnAgreeHeight = $id("btnAgentAgree").height();
		var editorWidth = mainWidth - 10;
		var editorHeight = mainHeight - 20;
		theEditor3.resize(editorWidth, editorHeight - tabsHeadHeight - agreeWarnHeight - btnAgreeHeight - 20);
	}
	
	// 调整供应商协议页面布局
	function adjustVendorAgreeSize(){
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		
		var tabsHeadHeight = $id("tabsHead").height();
		var agreeWarnHeight = $id("vendorAgreementWarnDiv").height();
		var btnAgreeHeight = $id("btnVendorAgree").height();
		var editorWidth = mainWidth - 10;
		var editorHeight = mainHeight - 20;
		theEditor4.resize(editorWidth, editorHeight - tabsHeadHeight - agreeWarnHeight - btnAgreeHeight - 20);
	}
	
	
	// 初始化页面
	$(function() {
		// 页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			allowTopResize : false,
			onresize : hideLayoutTogglers
		});
		// 隐藏布局north分割线
		$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
		// 
		hideLayoutTogglers();
		// 
		theTabsCtrl = $("#tabs").tabs();

		// 针对不同tab页绑定不同的保存操作
		$("#tabs").on("tabsactivate", function(event, ui) {
			// 隐藏商城设置页面所有错误提示
			hideAllMessages();
			
			var currentTab = ui.newTab.context.id;
			if (currentTab == "tabMallAgreement") {// 商家协议
				// 调整商家协议页面布局
				winSizeMonitor.start(adjustMallAgreeSize);
				loadMallAgreement();
			} else if (currentTab == "tabMemberAgreement") {// 会员协议
				// 调整会员协议页面布局
				winSizeMonitor.start(adjustMemberAgreeSize);
				loadMemberAgreement();
			} else if (currentTab == "tabAgentAgreement") {// 代理商协议
				// 调整代理商协议页面布局
				winSizeMonitor.start(adjustAgentAgreeSize);
				loadAgentAgreement();
			} else if (currentTab == "tabVendorAgreement") {// 供应商协议
				// 调整供应商协议页面布局
				winSizeMonitor.start(adjustVendorAgreeSize);
				loadVendorAgreement();
			}
		});

		// 保存/取消商城操作
		$id("btnMallSave").click(function() {
			if (checkMall()) {
				saveMall();
			}
		});
		$id("btnMallCancel").click(function() {
			loadProvince();
		});
		
		// 保存/取消参数设置
		$id("btnBizParamsSave").click(function() {
			if (checkBizParams()) {
				saveBizParams();
			}
		});
		$id("btnBizParamsCancel").click(function() {
			loadBizParams();
		});
		
		// 商家协议的富文本插件
		theEditor1 = CKEDITOR.replace("mallAgreementContent");
		// 会员协议的富文本插件
		theEditor2 = CKEDITOR.replace("memberAgreementContent");
		// 代理商协议的富文本插件
		theEditor3 = CKEDITOR.replace("agentAgreementContent");
		// 供应商协议的富文本插件
		theEditor4 = CKEDITOR.replace("vendorAgreementContent");
		
		// 保存/取消商城协议
		$id("btnMallAgreeSave").click(function() {
			if (checkMallAgreement()) {
				saveMallAgreement();
			}
		});
		
		// 保存/取消代理商协议
		$id("btnAgentAgreeSave").click(function() {
			if (checkAgentAgreement()) {
				saveAgentAgreement();
			}
		});
		
		// 保存/取消供应商协议
		$id("btnVendorAgreeSave").click(function() {
			if (checkVendorAgreement()) {
				saveVendorAgreement();
			}
		});
		
		// 保存/取消会员协议
		$id("btnMemberAgreeSave").click(function() {
			if (checkMemberAgreement()) {
				saveMemberAgreement();
			}
		});
		$id("btnMallAgreeCancel").click(function() {
			loadMallAgreement();
		});
		$id("btnAgentAgreeCancel").click(function() {
			loadAgentAgreement();
		});
		$id("btnVendorAgreeCancel").click(function() {
			loadVendorAgreement();
		});
		$id("btnMemberAgreeCancel").click(function() {
			loadMemberAgreement();
		});
		// 调整页面布局
		winSizeMonitor.start(adjustCtrlsSize);
		
		loadMallAgreement();
	});
</script>
</body>
</html>