<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	<title>参数设置</title>
</head>
<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding:4px;">
		<div class="form">
			<div id="bizParamsData"></div>
			
			<div class="action row" >
				<button id="btnSave">保存</button>
			</div>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		// 初始化页面数据
		function initData() {
			var ajax = Ajax.post("/setting/bizParams/get");
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					$id("bizParamsData").html(laytpl($id("bizParamsTpl").html()).render(result.data));
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
	
		// 检验业务参数
		function checkBizParams() {
			var formProxy = FormProxy.newOne();
			
			formProxy.addField({
				id : "login.require.image.code",
				required : true
			});
			formProxy.addField({
				id : "login.fail.lock.count",
				required : true,
				rules : [ "isNatual"]
			});
			formProxy.addField({
				id : "auto.confirm.receive.days",
				required : true,
				rules : [ "isNatual"]
			});
			formProxy.addField({
				id : "auto.confirm.refund.days",
				required : true,
				rules : [ "isNatual"]
			});
			formProxy.addField({
				id : "max.merchant.shop.count",
				required : true,
				rules : [ "isNatual" ]
			});
			formProxy.addField({
				id : "shop.audit.first.by.agent",
				required : true
			});
			formProxy.addField({
				id : "after.sale.protection.days",
				required : true,
				rules : [ "isNatual" ]
			});
			formProxy.addField({
				id : "auto.cancel.unpaid.order.hours",
				required : true,
				rules : [ "isNatual" ]
			});
			formProxy.addField({
				id : "auto.cancel.return.goods.hours",
				required : true,
				rules : [ "isNatual" ]
			});
			return formProxy.validateAll();
		}
	
		// 获取被修改的业务参数
		function getBizParamChangedMap() {
			var bizParamChangedMap = new KeyMap();
	
			// 添加被修改的业务参数
			var bizParam, bizParams = $("#bizParamsData input");
			for (var i = 0; i < bizParams.length; i++) {
				bizParam = bizParams.get(i);
				if (bizParam.defaultValue != bizParam.value) {
					bizParamChangedMap.add(bizParam.id, bizParam.value);
				}
			}
	
			return bizParamChangedMap;
		}
	
		// 修改业务参数
		function updateBizParams() {
			var progressBar = Layer.progress("正在提交数据...");
	
			var ajax = Ajax.post("/setting/bizParams/update/do");
			var postData = getBizParamChangedMap();
			if (postData.size() == 0) {
				Layer.info("无数据需要更新，请修改后再保存！");
				return;
			}
	
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					initData();
					Layer.msgSuccess(result.message);
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.always(function() {
				progressBar.hide();
			});
			ajax.go();
		}
	
		// 初始化页面
		$(function() {
			// 页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 70,
				allowTopResize : false
			});
			// 隐藏布局north分割线
			$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
			
			// 初始化数据
			initData();
	
			// 修改业务参数
			$("#btnSave").button().click(function() {
				if (checkBizParams()) {
					updateBizParams();
				}
			});
		});
	</script>
</body>
<script id="bizParamsTpl" type="text/html" title="业务参数模板">
	{{# for(var i = 0, len = d.length; i < len; i++){ }}
		<div class="field row">
			<label class="field label two half wide required">{{ d[i].name }}</label>
			<input class="field value" type="text" id={{ d[i].code }} value={{ d[i].value }} />
		</div>
	{{# } }}
</script>
</html>