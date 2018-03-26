<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>代理下单</title>
<style>
	.ui-layout-pane {
		background-color:#EEE;
		border-width : 0;
	}

</style>
</head>

<body id="rootPanel">
	<div id="leftPanel" class="ui-layout-west" style="padding: 0;display:none">
		<div id="leftPanelTop" class="ui-layout-north" style="padding: 4px;line-height:22px;">
			服務列表
		</div>
		<div id="leftPanelMain" class="ui-layout-center" style="padding: 2px;">
			<table id="theGridCtrl"></table>
			<div id="theGridPager"></div>
		</div>
	</div>
	<div id="rightPanel" class="ui-layout-center" style="padding: 0;display:none">
		<div id="rightPanelTop" class="ui-layout-north" style="padding: 4px;line-height:22px;">
			訂單信息
		</div>
		<div id="rightPanelMain" class="ui-layout-center" style="padding: 4px;background-color:#FFFFFF;">
			<!-- Grid view --> 
			<div class="form scroll-content" style="float:left;display:none" id="viewForm">
			<div class="field row">
				<label class="field label required one half wide">客户手机</label> <input
					type="text" id="userMobile" class="field value one half wide" maxlength="11" />
			</div>
			<div class="field row">
				<label class="field inline required label one half wide">客户车辆</label>
				<select class="field value " id="carId" name="carId" style="width:320px" disabled="disabled"><option value="" title="- 请选择客户车辆-">- 请选择客户车辆 -</option></select>
				<button id="btnAddUserCar" class="normal button" disabled="disabled" style="display:none">+新添车辆</button>
			</div>
			<!-- 
			<div class="field row">
				<label class="field label required one half wide">车型/车辆</label>
			</div>
			<div id="carModel" class="normal_grid">
				<table id="carGridCtrl" style="margin-right: 2px"></table>
			</div>
			 -->
			<div class="field row" style="display:none">
				<label class="field label required one half wide">联系人</label> <input
					type="text" id="linkMan" maxlength="30"
					class="field value one half wide" /> <label
					class="field inline label required one half wide">联系电话</label> <input
					type="text" id="linkNo" class="field value one half wide"
					maxlength="11" />
			</div>
			<div class="field row">
				 <label class="field inline label required one half wide">预约时间</label>
				<input type="text" id="planTime" class="field value one half wide"  readonly="true" />
			</div>
			<div class="field row">
				<label class="field label required one half wide">支付方式</label>
				<select id="payWay" class="field value one half wide">
				</select>
			</div>
			<div class="field row" style="height: 60px;">
				<label class="field label one half wide"
					style="vertical-align: middle;">备注</label>
				<textarea id="memo" class="field value three wide"
					maxlength="90" style="height: 50px;"></textarea>
			</div>
			<div class="field row">
				<label class="field label required one half wide">应付金额</label> 
				<div class="field group">
					<font id="saleAmount" color="red">¥ 00.00</font>
				</div>
					<label
					class="field inline label required one half wide" style="display:none">优惠金额</label> <input
					type="text" id="discAmount" class="field value one half wide" style="display:none"/>
			</div>
			<div class="field row" style="display:none">
				<label class="field label required one half wide">结算金额</label> <input
					type="text" id="amount" class="field value one half wide" />
			</div>
			
		</div>
			
		</div>
		<div id="rightPanelBottom" class="ui-layout-south" style="padding: 4px;line-height:52px;display:none">
			<div style="width:100%;height:100%;text-align:center;vertical-align:middle;">
				<button class="normal button" id="btnBack">&lt;&nbsp;&nbsp;返回</button> <span class="normal spacer four wide"></span><button class="normal button two wide" id="btnSubmit">提交</button>
			</div>
		</div>			
	</div>
<div id="addUserCarDialog" style="display: none;">
			<div id="addUserCarForm" class="form"  >
			<div class="field row" style="margin-top: 10px;">
				<label class="field label required">名称：</label>
				<input type="text" class="field value " maxlength="15" id="name"  name="name" style="width:200px"/>
			</div>
			<div class="field row">
				<label class="field label required">品牌：</label> 
				<select class="field value" id="brandId" name="brandId" style="width:200px"><option value="" title="- 请选择 -"></option></select>
			</div>
			<div class="field row">
				<label class="field label required">车系：</label> 
				<select class="field value" id="serialId" name="serialId" style="width:200px"><option value="" title="- 请选择 -"></option></select>
			</div>
			<div class="field row">
				<label class="field label required">车型：</label> 
				<select class="field value" id="modelId" name="modelId" style="width:200px" ><option value="" title="- 请选择 -"></option></select>
			</div>
		</div>
		</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
	$.fn.numeral = function(bl) {// 限制金额输入、兼容浏览器、屏蔽粘贴拖拽等
		$(this).keypress(function(e) {
			var keyCode = e.keyCode ? e.keyCode : e.which;
			if (bl) {// 浮点数
				if ((this.value.length == 0 || this.value.indexOf(".") != -1) && keyCode == 46)
					return false;
				return keyCode >= 48 && keyCode <= 57 || keyCode == 46 || keyCode == 8;
			} else {// 整数
				return keyCode >= 48 && keyCode <= 57 || keyCode == 8;
			}
		});
		$(this).bind("copy cut paste", function(e) { // 通过空格连续添加复制、剪切、粘贴事件
			if (window.clipboardData)// clipboardData.setData('text', clipboardData.getData('text').replace(/D/g, ''));
				return !clipboardData.getData('text').match(/D/);
			else
				event.preventDefault();
		});
		$(this).bind("dragenter", function() {
			return false;
		});
		$(this).css("ime-mode", "disabled");
		$(this).bind("focus", function() {
			if (this.value.lastIndexOf(".") == (this.value.length - 1)) {
				this.value = this.value.substr(0, this.value.length - 1);
			} else if (isNaN(this.value)) {
				this.value = "";
			}
		});
	}
	// iconClass : info, warning, error
	function toast(msg, duration, iconClass, callback) {
		Toast.show(msg, duration, iconClass, callback);
	}
	//緩存變量
	var jqGridCtrl;
	//缓存当前jqGrid数据行数组
	var gridHelper = JqGridHelper.newOne();
	// 模拟的服务表格数据
	var svcGridData = [];
	//调整控件大小
	function adjustCtrlsSize(winWidth, winHeight) {
		var jqMainPanel = $id("leftPanelMain");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		console.log("mainWidth:" + mainWidth + ", " + "mainHeight:" + mainHeight);
		//
		var gridCtrlId = "theGridCtrl";
		var jqGridBox = $("#gbox_" + gridCtrlId);
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
		var pagerHeight = $id("theGridPager").height();
		jqGridCtrl.setGridWidth(mainWidth - 1);
		jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3);
		//
		hideLayoutTogglers();
	}
	$(function() {
		// 页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			west__size : "40%",
			allowLeftResize : false
		});
		$id('leftPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 30,
			allowTopResize : false
		});
		
		$id('rightPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 30,
			south__size : 60,
			allowTopResize : false
		});
		
		//
		hideLayoutTogglers();
		//
		//初始化数据表格
		jqGridCtrl = $("#theGridCtrl").jqGrid({
			height : "100%",
			width : "100%",
			rownumbers : false,
			multiselect : true,
			contentType : 'application/json',
			mtype : "post",
			datatype : 'json',
			multikey : 'ctrlKey',
			url : getAppUrl("/shop/svc/list/get"),//获取列表链接
			pager : "#theGridPager",
			colModel : [{
				label : 'svcId',
				name : 'svcId',
				//width : 100,
				key : true,
				hidden : true
			}, {
				label : '服务',
				name : 'svcName',
				width : 100
			}, {
				label : '描述',
				name : 'desc',
				width : 130,
				align : 'center'
			}, {
				label : '价格',
				name : 'salePrice',
				width : 50,
				align : 'center'
			}],
			onSelectRow : function(rowid, status) {
				if (status) {
					totalAmount += parseFloat(jQuery("#theGridCtrl").getRowData(rowid).salePrice);
					updateAmountItems();
					// 记录已选服务的ID
					saleOrderContext.svcIds.add(parseInt(jQuery("#theGridCtrl").getRowData(rowid).svcId));
				} else {
					totalAmount -= parseFloat(jQuery("#theGridCtrl").getRowData(rowid).salePrice);
					updateAmountItems();
					// 清除已取消服务的ID记录
					var index = saleOrderContext.svcIds.indexOf(parseInt(jQuery("#theGridCtrl").getRowData(rowid).svcId));
					saleOrderContext.svcIds.removeAt(index);
				}
			},
			onSelectAll : function(aRowids, status) {
				if (status) {
					totalAmount = 0;
					saleOrderContext.svcIds = [];
					for (var i = 0, len = svcGridData.length; i < len; i++) {
						totalAmount += svcGridData[i].salePrice;
						updateAmountItems();
						// 记录已选服务ID
						saleOrderContext.svcIds.add(parseInt(svcGridData[i].svcId));
					}
				} else {
					totalAmount = 0;
					updateAmountItems();
					// 重置记录已选服务ID的数组
					saleOrderContext.svcIds = [];
				}
			},
			loadComplete : function(gridData) {
				svcGridData=gridData.rows;
				gridHelper.cacheData(gridData);
			}
		});
		//
		jqGridCtrl.bindKeys();
		//
		winSizeMonitor.start(adjustCtrlsSize);
		initDatePicker();
		loadPayWay(function() {
			textSet("payWay", "alipay");
		});
		$id("userMobile").numeral(false);
		$id("userMobile").blur(queryUser);
		
		loadCarBrand();
		// 根据品牌加载车系
		$("#brandId").change(function() {
			var brandId = textGet("brandId");
			loadCarSerial(brandId);
		});

		// 根据车系加载车型
		$("#serialId").change(function() {
			var serialId = textGet("serialId");
			loadCarModel(serialId);
		});

		$id("btnAddUserCar").click(toShowTheDlg);
		$id("btnSubmit").click(submitSaleOrder);
		$id("btnBack").click(function(){
			location.href=getAppUrl('/saleOrder/list/jsp/-shop');
		});
		$(document).keydown(function(event){ 
			if(event.keyCode==13){ 
				submitSaleOrder();
			} 
		}); 
		$id("leftPanel").show();
		$id("rightPanel").show();
		$id('viewForm').show();
		$id("rightPanelBottom").show();
		
		
	});
	function fillShopFormItems() {
		textSet("shopName", saleOrderContext.shopName);
	}

	function updateAmountItems() {
		$id("saleAmount").text("¥ "+totalAmount.toFixed(2));
		//textSet("saleAmount", totalAmount.toFixed(2));
		textSet("discAmount", 0.00);
		textSet("amount", totalAmount.toFixed(2));
	}
	//-----------------------------------
	/**
	 * 初始化预约时间
	 * 
	 * @author 邓华锋
	 * @date 2015年11月10日 上午12:12:05
	 * 
	 */
	function initDatePicker() {
		var nowDate = new Date();
		var minDate = nowDate;
		var maxDate = minDate.add(30, "day");
		var yearStart = minDate.getFullYear();
		var monthStart = minDate.getMonth();
		var yearEnd = maxDate.getFullYear();
		var monthEnd = maxDate.getMonth();
		var defaultDate = minDate.format("yyyy-MM-dd HH:mm");
		$id("planTime").val(defaultDate);
		$id("planTime").datetimepicker({
			maxDate : minDate.add(30, "day"),
			minDate : minDate,
			// minTime: "09:00",
			// maxTime: "18:00",
			changeMonth : true,
			changeYear : true,
			defaultTime : '09:00',
			defaultDate : minDate,
			showYearOrder : "desc",
			format : 'Y-m-d H:i',
			yearStart : yearStart,
			yearEnd : yearEnd,
			monthStart : monthStart,
			monthEnd : monthEnd,
			lang : 'ch',
			onSelect : function(dateText, inst) {
			}
		});
	}
	/**
	 * 加载支付方式
	 * 
	 * @author 邓华锋
	 * @date 2015年10月9日
	 * 
	 */
	function loadPayWay(callback) {
		// 隐藏页面区
		var ajax = Ajax.post("/setting/payWay/usbale/selectList/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData("payWay", result.data);
				if ($.isFunction(callback)) {
					callback();
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	// ------------------------客户车辆start-------------------------
	function getDefaultUserCar(userId) {
		var ajax = Ajax.post("/car/userCar/default/get");
		ajax.data({
			userId : userId
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var userCarSelect = result.data;
				if (userCarSelect != null) {
					$id("carId").val(userCarSelect.id);
				}
			} else {
				toast(result.message, null, "error");
			}
		});
		ajax.go();
	}
	function loadUserCarList(userId, callback) {
		// 隐藏页面区
		var ajax = Ajax.post("/car/userCar/selectList/get");
		ajax.data({
			userId : userId
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData("carId", result.data);
				console.log("userCarSize:" + result.data.items.length);
				if (result.data.items != null && result.data.items.length > 5) {
					$id("btnAddUserCar").hide();
				} else {
					$id("btnAddUserCar").show();
				}
				if ($.isFunction(callback)) {
					callback();
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	
	// 实例化表单代理
	var formUserCarProxy = FormProxy.newOne();
	formUserCarProxy.addField({
		id : "name",
		required : true,
		messageTargetId : "name"
	});
	formUserCarProxy.addField({
		id : "brandId",
		required : true,
		messageTargetId : "brandId"
	});
	//
	formUserCarProxy.addField({
		id : "serialId",
		required : true,
		messageTargetId : "serialId"
	});
	//
	formUserCarProxy.addField({
		id : "modelId",
		required : true,
		messageTargetId : "modelId"
	});
	var jqDlgDom = $id("addUserCarDialog");
	function initUserCar() {
		$id("name").val("");
		$id("brandId").val("");
		loadSelectData("serialId", "");
		loadSelectData("modelId", "");
	}
	// （真正）显示对话款
	function toShowTheDlg() {
		initUserCar();
		// 对话框配置
		var dlgConfig = {
			width : Math.min(450, $window.width()),
			height : Math.min(300, $window.height()),
			modal : true,
			open : false
		};
		dlgConfig.title = "新增客户车辆";
		dlgConfig.buttons = {
			"保存" : function() {
				// 收集并验证要提交的数据（如果验证不通过直接返回 return）
				var vldResult = formUserCarProxy.validateAll();
				if (!vldResult) {
					return;
				}
				var postData = formUserCarProxy.getValues();
				goAddUserCar(postData);
			},
			"取消" : function() {
				// jqDlgDom.prop("continuousFlag", false);
				// 隐藏表单验证消息
				formUserCarProxy.hideMessages();
				$(this).dialog("close");
			}
		};
		//
		jqDlgDom.bind('dialogclose', function(event, ui) {// dialog关闭事件
			// 隐藏表单验证消息
			formUserCarProxy.hideMessages();
		});
		jqDlgDom.dialog(dlgConfig);
	}

	function goAddUserCar(userCarInfoMap) {
		var hintBox = Layer.progress("正在保存数据...");
		var ajax = Ajax.post("/car/userCar/add/do");
		userCarInfoMap["userId"] = saleOrderContext.userId;
		userCarInfoMap["modelName"] = $("#brandId option:selected").text() + $("#serialId option:selected").text() + $("#modelId option:selected").text();
		ajax.data(userCarInfoMap);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
				loadUserCarList(saleOrderContext.userId, function() {
					$id("carId").val(result.data);
				});
				jqDlgDom.dialog("close");
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.always(function() {
			hintBox.hide();
		});
		ajax.go();
	}
	
	// 获取车辆品牌
	function loadCarBrand(callback) {
		// 隐藏页面区
		var ajax = Ajax.post("/car/carBrand/selectList/get");
		ajax.params({});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData("brandId", result.data);
				if ($.isFunction(callback)) {
					callback();
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	// 获取车辆车系
	function loadCarSerial(id, callback) {
		// 隐藏页面区
		var ajax = Ajax.post("/car/carSerial/selectList/get");
		ajax.params({
			brandId : id
		});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData("serialId", result.data);
				if ($.isFunction(callback)) {
					callback();
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	// 获取车辆车型
	function loadCarModel(id, callback) {
		// 隐藏页面区
		var ajax = Ajax.post("/car/carModel/selectList/get");
		ajax.params({
			serialId : id
		});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData("modelId", result.data);
				if ($.isFunction(callback)) {
					callback();
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	//------------------------------------------客户车辆end--------------------------------------------
	//---------------------------------------------------------------------------------------
		// 实例化销售订单表单代理
	var orderFormProxy = FormProxy.newOne();
	orderFormProxy.addField({
		id : "userMobile",
		key : "userMobile",
		required : true,
		rules : [ "maxLength[11]", "isMobile" ]
	});
	orderFormProxy.addField({
		id : "carId",
		key : "carId",
		required : true
	});

	orderFormProxy.addField({
		id : "payWay",
		key : "payWay",
		required : true,
	});

	orderFormProxy.addField({
		id : "memo",
		key : "memo",
		rules : [ "maxLength[90]" ]
	});
	// 所选服务价格汇总金额
	var totalAmount = 0.00;
	// 代理下单页面涉及的上下文信息
	var saleOrderContext = {};
	saleOrderContext.svcIds = [];
	// 初始化订单表单控件
	function initOrderForm() {
		$("#shopName").attr("disabled", true);
		$("#saleAmount").attr("disabled", true);
		$("#discAmount").attr("disabled", true);
		$("#amount").attr("disabled", true);

		$id("planTime").datetimepicker({
			lang : 'ch',
			format : 'Y-m-d H:i'
		});

		datetimeSet("planTime", new Date());
	}

	function clearSvcGridData() {
		jqGridCtrl.clearGridData();
		jqGridCtrl.trigger('reloadGrid');
		// 重置记录已选服务ID的数组
		saleOrderContext.svcIds = [];
	}
	// 实例化查询表单代理
	var queryFormProxy = FormProxy.newOne();

	queryFormProxy.addField({
		id : "userMobile",
		key : "userMobile",
		required : true,
		rules : [ "maxLength[11]", "isMobile" ]
	});
	
	function queryUser() {
		var vldResult = queryFormProxy.validateAll();
		if (vldResult) {
			var userMobile = $id("userMobile").val();
			var ajax = Ajax.post("/user/ensure/exist/by/phoneNo/-shop");
			ajax.data({
				phoneNo : userMobile
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					if (result.data.userId != -1) {
						// 用户信息赋值
						saleOrderContext.userId = result.data.userId;
						saleOrderContext.userName = result.data.userName;
						saleOrderContext.userPhone = result.data.userPhone;
						saleOrderContext.linkMan = result.data.linkMan;
						saleOrderContext.linkNo = result.data.linkNo;

						loadUserCarList(saleOrderContext.userId, function() {
							$id("carId").removeAttr("disabled");
							$id("btnAddUserCar").removeAttr("disabled");
							getDefaultUserCar(saleOrderContext.userId);
						});
						$id("user-warn").hide();
					} else {
						clearCarGridData();
						clearSvcGridData();
						$id("user-warn").show();
						$id("carSvc").hide();
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
	}
	function submitSaleOrder() {
		var vldResult = orderFormProxy.validateAll();
		if (!vldResult) {
			return;
		}

		var svcSelected = saleOrderContext.svcIds.length != 0;
		if (!svcSelected) {
			toast("请选择服务", null, "warning");
			return;
		}
		saleOrderContext.memo = textGet("memo");
		saleOrderContext.planTime = textGet("planTime");
		var hour = saleOrderContext.planTime.substring(10, 13);
		hour = ParseInt(hour);
		if (hour < 9 || hour > 18) {
			toast("预约时间范围请控制在运营时间早上9点到晚上6点", null, "warning");
			return;
		}
		saleOrderContext.payWay = textGet("payWay");
		saleOrderContext.carId = textGet("carId");
		var hintBox = Layer.progress("正在提交订单...");
		var ajax = Ajax.post("/saleOrder/save/do");
		ajax.data(saleOrderContext);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var resultMap = result.data;
				var callback = function() {
					location.href=getAppUrl('/saleOrder/list/jsp/-shop');
					//submitCallback(resultMap);// 提交返回方法
				};
				var closeDelay = 3000;
				toast(result.message, closeDelay, "info", callback);
			} else {
				toast(result.message, null, "error");
				hintBox.hide();
			}
		});
		ajax.always(function() {
			hintBox.hide();
		});
		ajax.go();
	}
	</script>
</body>
</html>