<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>运费设置</title>
</head>
<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding:0px;">
			<div id="theMainCtrl" class="noBorder">
				<div class="form" style="padding-left: 10px;">
					<div class="field row">
					<input id="paramId" type="hidden" type="text" value=""/>
						<input id="freightFeeFixed-N" type="radio" name="freightFeeFixed" value="0" checked onclick="hideMessage()"/>
						<label for="freightFeeFixed-N">
						每订单消费满 <input class="field value" id="freightFeeFreeFeeValve" disabled="disabled" onkeyup="value=value.replace(/\.\d{2,}$/,value.substr(value.indexOf('.'),3))"/> 元，免运费。未达到条件，收运费，每订单运费
						<input class="field value" id="freightFeeLessFee" disabled="disabled" onkeyup="value=value.replace(/\.\d{2,}$/,value.substr(value.indexOf('.'),3))"/>
						元。
						</label>
					</div>
					<div class="field row">
						<input id="freightFeeFixed-Y" type="radio" name="freightFeeFixed" value="1" onclick="hideMessage()"/>
						<label for="freightFeeFixed-Y">
						每订单固定运费<input class="field value" id="freightFeeFixedFee" disabled="disabled" onkeyup="value=value.replace(/\.\d{2,}$/,value.substr(value.indexOf('.'),3))"/> 元。
						</label>
					</div>
					<div class="action row">
						<button id="btnSave" class="normal button">
							保存
						</button>
						<span class="spacer"></span>
						<button id="btnCancel" class="normal button">
							取消
						</button>
					</div>
				</div>
			</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
	var jqTabsCtrl;
	//调整控件大小
	function adjustCtrlsSize() {
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		console.log("mainWidth:" + mainWidth + ", " + "mainHeight:"
				+ mainHeight);
		var mainCtrlWidth = mainWidth - 4;
		var mainCtrlHeight = mainHeight - 8;
		var mainCtrl = $("#theMainCtrl");
		mainCtrl.width(mainCtrlWidth - 4 * 2);
		mainCtrl.height(mainCtrlHeight - 30);
	}

	function initDisabledColum(){
		var value = $attr("input[name]:checked", "freightFeeFixed").val();
		if(value==1){
			$id("freightFeeFixedFee").prop("disabled", false);
			$id("freightFeeFreeFeeValve").prop("disabled", true);
			$id("freightFeeLessFee").prop("disabled", true);
		}else{
			$id("freightFeeFixedFee").prop("disabled", true);
			$id("freightFeeFreeFeeValve").prop("disabled", false);
			$id("freightFeeLessFee").prop("disabled", false);
		}
	}
	
	function hideMessage(){
		var value = $attr("input[name]:checked", "freightFeeFixed").val();
		if(value==1){
			hideMiscTip("freightFeeFreeFeeValve");
			hideMiscTip("freightFeeLessFee");
		}else{
			hideMiscTip("freightFeeFixedFee");
		}
	}

	var formProxy = FormProxy.newOne();
	//注册表单控件
	formProxy.addField({
		id : "freightFeeFixedFee",
		type : "float",
		rules : ["isMoney","rangeValue[0,1000000]",
		         {
			rule : function(idOrName, type, rawValue, curData) {
				var value = $attr("input[name]:checked", "freightFeeFixed").val();
				if(value==1){
					return !isNullOrEmpty($id("freightFeeFixedFee").val());
				}else{
					return true;
				}
			},
			message : "这是必填项！"
		}]
	});
	formProxy.addField({
		id : "freightFeeFreeFeeValve",
		type : "float",
		rules : ["isMoney","rangeValue[0,1000000]",
		         {
			rule : function(idOrName, type, rawValue, curData) {
				var value = $attr("input[name]:checked", "freightFeeFixed").val();
				if(value==1){
					return true;
				}else{
					return !isNullOrEmpty($id("freightFeeFreeFeeValve").val());
				}
			},
			message : "这是必填项！"
		}]
	});
	formProxy.addField({
		id : "freightFeeLessFee",
		type : "float",
		rules : ["isMoney","rangeValue[0,1000000]",
		         {
			rule : function(idOrName, type, rawValue, curData) {
				var value = $attr("input[name]:checked", "freightFeeFixed").val();
				if(value==1){
					return true;
				}else{
					return !isNullOrEmpty($id("freightFeeLessFee").val());
				}
			},
			message : "这是必填项！"
		}]
	});
	
	function loadFreightSet(){
		formProxy.hideMessages();
		//生成提交数据
		var postData = {
				code : "freight.fee"
		};
		var ajax = Ajax.post("/shop/param/val/get");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var data = result.data;
				if(data){
					$id("paramId").val(data.id);
					var dataVal = JSON.decode(data.value);
					if(dataVal.fixed){
						radioSet("freightFeeFixed",1);
					}else{
						radioSet("freightFeeFixed",0);
					}
					$id("freightFeeFixedFee").val(dataVal.fixedFee);
					$id("freightFeeFreeFeeValve").val(dataVal.freeFeeValve);
					$id("freightFeeLessFee").val(dataVal.lessFee);
					initDisabledColum();
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.fail(function(result, jqXhr) {
			//隐藏等待提示框
			loaderLayer.hide();
		});
		ajax.always(function(result, jqXhr) {
			Layer.hideAll();
		});
		ajax.go();
	}
	
	function saveFreightSet() {
		//验证数据
		var vldResult = formProxy.validateAll();
		if (!vldResult) {
			return;
		}
		//生成提交数据
		var postData = {
				id : $id("paramId").val() == "" ? null : parseInt($id("paramId").val()),
				freightFeeFixed : parseInt(radioGet("freightFeeFixed")),
				freightFeeFixedFee : formProxy.getValue("freightFeeFixedFee"),
				freightFeeFreeFeeValve : formProxy.getValue("freightFeeFreeFeeValve"),
				freightFeeLessFee : formProxy.getValue("freightFeeLessFee")
		};
		//可以对postData进行必要的处理（如如数据格式转换）
		//显示等待提示框
		var loaderLayer = Layer.loading("正在保存...");
		//
		var ajax = Ajax.post("/shop/param/val/save/do");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			//隐藏等待提示框
			loaderLayer.hide();
			//
			if (result.type == "info") {
				Layer.msgSuccess("保存成功");
				loadFreightSet();
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.fail(function(result, jqXhr) {
			//隐藏等待提示框
			loaderLayer.hide();
		});
		ajax.go();
	}
	//
	$(function() {
		//页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			allowTopResize : false
		});
		// 页面自适应
		winSizeMonitor.start(adjustCtrlsSize);
		
		$attr("input[name]", "freightFeeFixed").click(initDisabledColum);
		$id("btnCancel").click(loadFreightSet);
		$id("btnSave").click(saveFreightSet);
		loadFreightSet();
		initDisabledColum();
	});
	</script>
</body>
</html>
