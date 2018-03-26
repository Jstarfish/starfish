<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	<title>参数设置</title>
</head>
<body id="rootPanel">
	<div class="ui-layout-north" style="padding: 4px;" id="topPanel">
		<div class="filter section">
			<div class="simple block">
				<div class="header">
					<label>平台设置&nbsp;&gt;&nbsp;参数设置</label>
				</div>
			</div>
		</div>
	</div>

	<div id="mainPanel" class="ui-layout-center" style="padding:0px;">
		<div id="tabs" class="noBorder">
			<div class="form">
				<div class="field row">
					<label class="field label two half wide required">商品是否仅由商城提供</label>
					<div class="field group">
						<input id="goods.definded.only.by.mall-true" type="radio" name="goodsProvidedOnlyByMall" value="true" />
						<label for="goods.definded.only.by.mall-true">是</label>
						<input id="goods.definded.only.by.mall-false" type="radio" name="goodsProvidedOnlyByMall" value="false" checked="checked"/>
						<label for="goods.definded.only.by.mall-false">否</label>
					</div>
				</div>
				<div class="field row">
					<label class="field label two half wide required">商品分类级数</label>
					<input class="field value half wide" style="text-align: center;" maxlength="1" type="text" id="goods.categ.levels"/>
				</div>
				<span class="normal hr divider"></span>
				<div class="field row align center">
					<button class="normal button" id="btnSave">保存</button>
					<span class="normal spacer"></span>
					<button class="normal button" id="btnCancel">取消</button>
				</div>
			</div>
		</div>
	</div>
	
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		var formProxy = FormProxy.newOne();
		formProxy.addField({
			name : "goodsProvidedOnlyByMall",
			required : true
		});
		formProxy.addField({
			id : "goods.categ.levels",
			required : true,
			rules : ["isNatual"]
		});
	
		// 初始化页面数据
		function initData() {
			var ajax = Ajax.post("/setting/sysParams/all/get");
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data;
					if(data != null){
						for(var i = 0; i < data.length; i++){
							var temp = data[i];
							// 商品是否仅由商城提供
							if(temp.code == "goods.definded.only.by.mall"){
								var value = temp.value;
								if(value == 'true'){
									$id("goods.definded.only.by.mall-true").attr("checked","true");
									$id("goods.definded.only.by.mall-true").attr("disabled","disabled");
									$id("goods.definded.only.by.mall-false").attr("disabled","disabled");
								} else if(temp.value == 'false') {
									$id("goods.definded.only.by.mall-false").attr("checked","true");
									$id("goods.definded.only.by.mall-true").attr("disabled","disabled");
									$id("goods.definded.only.by.mall-false").attr("disabled","disabled");
								}
							} else if (temp.code == "goods.categ.levels") {// 商品分类级数
								$id("goods.categ.levels").val(temp.value);
								if(temp.value != ''){
								$id("goods.categ.levels").attr("disabled","disabled");
								}
							}
						}
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
	
		// 检验业务参数
		function checkSysParams() {
			return formProxy.validateAll();
		}
	
		// 修改业务参数
		function updateSysParams() {
			var progressBar = Layer.progress("正在提交数据...");
			var ajax = Ajax.post("/setting/sysParams/update/do");
			var postData = new KeyMap();
			postData.add("goods.definded.only.by.mall", radioGet("goodsProvidedOnlyByMall"));
			postData.add("goods.categ.levels", textGet("goods.categ.levels"));
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
				north__size : 60,
				allowTopResize : false,
				onresize : hideLayoutTogglers
			});
			// 隐藏布局north分割线
			$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
			// 
			hideLayoutTogglers();
			
			// 初始化数据
			initData();
	
			// 保存系统参数
			$("#btnSave").click(function() {
				if(confirm("保存之后就不能修改,确定要保存吗?")){
				if (checkSysParams()) {
					updateSysParams();
				}
				}
			});
			
			// 取消修改
			$("#btnCancel").click(function() {
				hideMiscTip("goods.categ.levels");
				initData();
			});
		});
	</script>
</body>
</html>