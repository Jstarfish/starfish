//-----------------------缓存数据全局变量--------------------------{{
// pc app
var jsType = "pc";
// -----------------------------------------------------------------}}
// -----------------------配置--------------------------{{
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
function openPage(url, target, beforeOpen) {
	setPageUrl(getAppUrl(url), target, beforeOpen);
}
function openDlg(dlgArgs, dlgViewId, dlgUrl, callbackFunc, options) {
	// 对话框信息
	var body = $("body");
	$(body).css({
		overflow : "hidden"
	}); // 禁用滚动条
	var theDlg = null;
	options = options || {};
	if (dlgUrl) {
		// 对话框参数名
		var argName = dlgViewId || "argx";
		// 对话框参数值
		var argValue = dlgArgs;
		// 对话框参数 预存
		setDlgPageArg(argName, argValue);
		var pageUrl = dlgUrl;
		var extParams = {};
		pageUrl = makeDlgPageUrl(pageUrl, argName, extParams);
		options["src"] = pageUrl;
	}
	options["closeBtn"] = true;
	options["maxmin"] = false;
	if (callbackFunc && typeof callbackFunc == "function") {
		options["yes"] = function(index) {
			callbackFunc(theDlg, index);
		};
	}
	options["end"] = function() {
		$(body).css({
			overflow : "scroll"
		}); // 启用滚动条
	};
	// 打开对话框-----------------------------------------
	theDlg = Layer.dialog(options);
}

// -----------------------------------------------------------------}}
// -----------------------页面加载----------------------------------{{
$(function() {
	makeBtmSticky("cartToolbar", 300);
	getSaleCart();
	initDatePicker();
	getDefaultLinkWay();

	// *****************店铺选择************
	$id("shopId").val("");
	$id("btnShopSelect").click(function() {
		openShopSelectDlg();
	});
	// 支付方式
	openPayWayDlg();

	$(".mr20").live("click", function() {
		var payWay = $(this).attr("data-code");
		$id("payWay").val(payWay);
		$(this).siblings().removeClass('active').end().addClass('active');
	});

	// 联系人
	$(".select-contacts").click(function(event) {
		openLinkManDlg();
		event.stopPropagation();
	});
	
	$id("linkMan").val("");
	$id("phoneNo").val("");
	$id("phoneNo").numeral(false);

	getDefaultLinkWay();
	

	$id("linkMan").blur(function() {
		checkAilias();
	});

	$id("btnAddLinkMan").click(function() {
		addLinkMan();
	});
	getDefaultUserCar();
	//车辆选择
	$(".select-car").click(function(event) {
		openUserCarDlg();
		event.stopPropagation();
	});
	
	$id("btnAddUserCar").click(function() {
		addUserCar();
	});
	
	// ------------------优惠券------------------------
	initCouponSelect();

	$id("btnCouponSelect").click(function() {
		couponSelectDlg();
	});

	$(".layui-layer-content table>tbody#proCouponList>tr").live("click", function() {
		trSelect(this, "coupon");
	});
	$(".layui-layer-content table>tbody#svcCouponList>tr").live("click", function() {
		trSelect(this, "coupon");
	});
	$(".layui-layer-content input[name=coupon]").live("click", function(event) {
		checkboxSelect(this, "coupon", event);
	});
	
	$(".couponSelect").live("click", function() {
		//initCouponSelect();
		var dataType=$(this).attr("data-type");
		var dataId=$(this).attr("data-id");
		if(dataType=="svc"){
			selectSvcCouponMap.remove(dataId);
		}else if(dataType=="goods"){
			selectProCouponMap.remove(dataId);
		}
		$(this).parent().parent().remove();
		checkShow();
	});
	// ------------------e卡------------------------
	initEcardSelect();

	$id("btnEcardSelect").click(function() {
		ecardSelectDlg();
	});

	$(".layui-layer-content table>tbody#ecardList>tr").live("click", function() {
		trSelect(this, "ecard");
	});

	$(".layui-layer-content input[name=ecard]").live("click", function(event) {
		checkboxSelect(this, "ecard", event);
	});

	$id("ecardSelect").live("click", function() {
		initEcardSelect();
		checkShow();
	});

	// 忘记密码
	$id("forgetPayPassword").click(function() {
		openPayPassDlg();
	});

	// 订单提交
	$id("btnSubmit").click(function(e) {
		submitOrder();
	});
	$id("btnSubmit").removeAttr("disabled");

	$("a.tip3").live("click", function() {
		// 打开对话框-----------------------------------------
		var theDlg = Layer.dialog({
			title : "安装费详细说明",
			src : getAppUrl("/comn/installation/fee/details/jsp"),
			area : [ '800px', '260px' ],
			closeBtn : true,
			maxmin : true, // 最大化、最小化
			btn : [ "知道了" ]
		});
	});

	$("a.svcPriceExplain").live("click", function() {
		// 打开对话框-----------------------------------------
		var theDlg = Layer.dialog({
			title : "安装费详细说明",
			src : getAppUrl("/comn/installation/fee/details/jsp"),
			area : [ '800px', '260px' ],
			closeBtn : true,
			maxmin : true, // 最大化、最小化
			btn : [ "知道了" ]
		});
	});

	$id("btnBack").click(function() {
		openPage("/saleCart/list/jsp");
	});

	$(document).on("click", "a,input", function() {
		if (getLoginState() == 0) {
			var pageUrl = makeUrl(getAppUrl("/user/login/jsp"));
			setPageUrl(pageUrl);
		}
	});
});
// -----------------------------------------------------------------}}
function showUserCar() {
	renderHtml(userCarSelect, "userCarTpl", "userCar");
}
function showLinkWay() {
	if (linkManSelect != null) {
		$id("linkMan").val(linkManSelect.linkMan);
		$id("phoneNo").val(linkManSelect.phoneNo);
	} else {
		initUserInfo();
		$id("linkMan").val(nickName);
		$id("phoneNo").val(phoneNo);
	}
}
function openLinkManDlg() {
	loadLinkMan();
	openLinkManDlgCallbackFunc();
}
function openLinkManDlgCallbackFunc() {
	$("div.select-contacts>ul>li").click(function(event) {
		if($(this).text()=="您还未添加联系人"){
			return;
		}
		linkManSelect = {};
		$id("btnAddLinkMan").hide();
		var span = $(this).find("span.linkMan");
		linkManSelect["linkMan"] = span.text();
		span = $(this).find("span.phoneNo");
		linkManSelect["phoneNo"] = span.text();
		showLinkWay();
	});
	$(".select-contacts").show();
}

function openUserCarDlg() {
	loadUserCar();
	openUserCarDlgCallbackFunc();
}
function openUserCarDlgCallbackFunc() {
	$("div.select-car>ul>li").click(function(event) {
		if($(this).text()=="您还未添加车辆"){
			return;
		}
		var dataId=$(this).attr("data-id");
		if(isUndef(dataId)){
			return;
		}
		userCarSelect = userCarList.find(function(item,i){
			return item.id===ParseInt(dataId);
		});;
		showUserCar();
	});
	$(".select-car").show();
}

/**
 * 检查是否存在别名一样的
 * 
 * @author 邓华锋
 * @date 2015年11月10日 上午12:06:23
 * 
 */
function checkAilias() {
	if ($id("linkMan").val().trim().length > 0) {
		var ajax = Ajax.post("/user/alias/check/get");
		ajax.sync();
		ajax.params({
			alias : $id("linkMan").val()
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "warn") {
				userLinkWay = result.data;
				if (userLinkWay == null) {
					$id("btnAddLinkMan").show();
					isExistLinkMan = false;
					return;
				}
				isExistLinkMan = true;
				$id("linkMan").val(userLinkWay.linkMan);
				$id("phoneNo").val(userLinkWay.phoneNo);
				$id("btnAddLinkMan").hide();
			} else {
				$id("btnAddLinkMan").show();
			}
		});
		ajax.go();
	}
}
/**
 * 加入联系人
 * 
 * @author 邓华锋
 * @date 2015年11月10日 上午12:15:46
 * 
 */
function addLinkMan() {
	var ajax = Ajax.post("/user/linkWay/save/do");
	var postData = {};
	postData.alias = $("#linkMan").val();
	postData.linkMan = $("#linkMan").val().trim();
	if (isNullOrBlank(postData.linkMan)) {
		$id("linkManTip").show();
		$id("linkManTip").html("请填写联系人");
		return;
	} else {
		$id("linkManTip").hide();
	}

	postData.phoneNo = $("#phoneNo").val().trim();
	if (isNullOrBlank(postData.phoneNo)) {
		$id("phoneNoTip").show();
		$id("phoneNoTip").html("请填写手机号码");
		return;
	} else if (!isMobile(postData.phoneNo.trim())) {
		$id("phoneNoTip").show();
		$id("phoneNoTip").html("请填写正确的手机号码");
		return;
	} else {
		$id("phoneNoTip").hide();
	}
	$id("btnAddLinkMan").attr({
		"disabled" : "disabled"
	});
	/*
	 * checkAilias(); if (isExistLinkMan) { if (userLinkWay == null) { $id("btnAddLinkMan").show(); isExistLinkMan = false; return; } var msg = "联系人已经存在，请重新填写联系人或无需加入联系人"; isExistLinkMan = true;
	 * toast(msg, null, "error", function(){ $id("btnAddLinkMan").hide(); }); return; }
	 */
	ajax.data(postData);
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			var callback = function() {
				$id("btnAddLinkMan").hide();
				$id("btnAddLinkMan").attr({
					disabled : false
				});
			};
			var closeDelay = 3000;
			toast(result.message, closeDelay, "info", callback);
		} else {
			toast(result.message, null, "error");
		}
	});
	ajax.go();
}
//用户车辆标识
var aliasFlag = false;
//
var carNumber = 0;
function addUserCar(){
	getUserCar();
	if(carNumber >= 5){
		Layer.warning("抱歉！您最多只能添加5个车辆信息");
		return;
	}
	loadCarBrand();
	loadSelectData("serialId", "");
	loadSelectData("modelId", "");
	//
	initCarInfo();
	var options = {
		dom : "#addUserCarTpl", // 或者 html string
		title : "新增车辆",
		area : [ '470px', '260px' ],
		btn : [ "保存","取消" ]
	};
	openDlg(null, null, null, userCarDlgCallbackFunc, options);
}
function initCarInfo(){
	$id("name").val("");
	$id("brandId").val("");
	$id("serialId").val("");
	$id("modelId").val("");
	// 根据品牌加载车系
	$("#brandId").change(function() {
		var brandId = textGet("brandId");
		getCarBrand(brandId);
		loadCarSerial(brandId);
		loadSelectData("modelId", "");
	});
	
	// 根据车系加载车型
	$("#serialId").change(function() {
		var serialId = textGet("serialId");
		loadCarModel(serialId);
	});
}
function userCarDlgCallbackFunc(rspInfo, index){
	var postData = {}
	// 设置最后一级地区id
	var name = $id("name").val();
	if(!name){
		$("#nameTip").css("display", "");
		$("#nameTip").html("请填写车辆名称");
		return;
	}
	
	var brandId = $id("brandId").val();
	if(!brandId){
		$("#brandIdTip").css("display", "");
		$("#brandIdTip").html("请选择品牌");
		return;
	}
	
	var serialId = $id("serialId").val();
	if(!serialId){
		$("#serialIdTip").css("display", "");
		$("#serialIdTip").html("请选择车系");
		return;
	}
	
	var modelId = $id("modelId").val();
	if(!modelId){
		$("#modelIdTip").css("display", "");
		$("#modelIdTip").html("请选择车型");
		return;
	}
	
	var modelName = $("#brandId option:selected").text() + $("#serialId option:selected").text() + $("#modelId option:selected").text();
	
	postData = {
		name : name,
		brandId : brandId,
		serialId : serialId,
		modelId : modelId,
		modelName : modelName
	}
	var ajax = Ajax.post("/car/userCar/add/do");
	ajax.data(postData);
	ajax.sync();
	ajax.done(function(result, jqXhr) {
		//获取用户信息
		if (result.type == "info") {
			var member = result.data;
			//展示用户信息
			//getUserCar();
			Layer.msgSuccess(result.message);
		} else {
			Layer.msgWarning(result.message);
		}
	});
	ajax.fail(function(result, jqXhr) {
		//隐藏等待提示框
		loaderLayer.hide();
	});
	ajax.go();
	layer.close(index);
}

//添加用户车辆
function toAddUserCar() {
	getUserCar();
	if(carNumber >= 5){
		Layer.warning("抱歉！您最多只能添加5个车辆信息");
		return;
	}
	// 获取模板内容
	var tplHtml = $id("addUserCarTpl").html();
	// 根据模板生成最终内容
	$id("addUserCar").html(tplHtml);
	loadCarBrand();
	loadSelectData("serialId", "");
	loadSelectData("modelId", "");
	//
	initCarInfo();
}
//获取车辆品牌
function loadCarBrand(callback) {
	// 隐藏页面区
	var ajax = Ajax.post("/car/carBrand/selectList/get");
	ajax.params({});
	ajax.sync();
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			loadSelectData("brandId", result.data);
			if($.isFunction(callback)){
				callback();
			}
		} else {
			Layer.warning(result.message);
		}
	});
	ajax.go();
}
//获取车辆车系
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
			if($.isFunction(callback)){
				callback();
			}
		} else {
			Layer.warning(result.message);
		}
	});
	ajax.go();
}
//获取车辆车型
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
			if($.isFunction(callback)){
				callback();
			}
		} else {
			Layer.warning(result.message);
		}
	});
	ajax.go();
}

//获取品牌信息
function getCarBrand(id) {
	// ajax post 请求...
	var ajax = Ajax.post("/car/carBrand/get/by/id");
	ajax.params({
		id : id
	});
	ajax.done(function(result, jqXhr) {
		var carBrand = result.data;
		if (carBrand != null) {
			$id("carImage").attr("src", carBrand.fileBrowseUrl + "?" + new Date().getTime());
		}
		
	});
	ajax.go();
}

//获取用户车辆
function getUserCar() {
	var postData = {};
	// ajax post 请求...
	var ajax = Ajax.post("/car/userCar/list/get");
	ajax.data(postData);
	ajax.sync();
	ajax.done(function(result, jqXhr) {
		var userCarList = result.data;
		if (userCarList != null) {
			carNumber = userCarList.length;
			/*$id("carNumber").html("您已增加" + carNumber + " 个车辆，最多可增加5个");
			// 获取模板内容
			var tplHtml = $id("userCarTpl").html();
			// 生成/编译模板
			var htmlTpl = laytpl(tplHtml);
			// 根据模板和数据生成最终内容
			var htmlText = htmlTpl.render(userCarList);
			$id("showUserCar").html(htmlText);
			$id("addUserCar").html("");*/
		}
		
	});
	ajax.go();
}


// --------------------------优惠券----------------------------------{{
var couponSelect = null;
couponType.add("nopay", "免付券");
couponType.add("sprice", "特价券");
couponType.add("deduct", "抵金券");

/**
 * 弹出选择优惠券窗口
 * 
 * @author 邓华锋
 * @date 2015年11月10日 上午12:20:37
 * 
 */
function couponSelectDlg() {
	loadCouponList();
	var options = {
		dom : "#couponSelectDlg", // 或者 html string
		title : "请选择优惠券",
		area : [ '850px', '400px' ]
	};
	openDlg(null, null, null, couponSelectDlgCallbackFunc, options);
}
function couponSelectDlgCallbackFunc(rspInfo, index) {
	selectProCouponMap = KeyMap.newOne();
	selectSvcCouponMap = KeyMap.newOne();
	var proCheckboxs=$("#proCouponList input[name=coupon]:checked");
	for(var i=0,len=proCheckboxs.length;i<len;i++){
		var proCheckbox=proCheckboxs[i];
		var productId=$(proCheckbox).attr("data-id");
		var userCouponId=$(proCheckbox).val();
		var proCouponList=proCouponMap.get(productId);
		var proCoupon=proCouponList.find(function(obj,i){
			return obj.id===parseInt(userCouponId);
		});
		if(proCoupon!=null){
			selectProCouponMap.set(productId,proCoupon);
		}
	}
	renderHtml({couponMap:selectProCouponMap,type:"goods"}, "couponRowTpl", "proCouponShow");
	var svcCheckboxs=$("#svcCouponList input[name=coupon]:checked");
	for(var i=0,len=svcCheckboxs.length;i<len;i++){
		var svcCheckbox=svcCheckboxs[i];
		var svcId=$(svcCheckbox).attr("data-id");
		var userCouponId=$(svcCheckbox).val();
		var svcCouponList=svcCouponMap.get(svcId);
		var svcCoupon=svcCouponList.find(function(obj,i){
			return obj.id===parseInt(userCouponId);
		});
		if(svcCoupon!=null){
			selectSvcCouponMap.set(svcId,svcCoupon);
		}
	}
	renderHtml({couponMap:selectSvcCouponMap,type:"svc"}, "couponRowTpl", "svcCouponShow");
	checkShow();
	layer.close(index);
}

function initCouponSelect() {
	$id("couponId").val("");
	$id("coupon-selected").hide();
	$id("couponShowTr").text("");
	$id("couponPrice").text("0");
	$id("couponAmountDl").hide();
	couponSelect = null;
	selectCouponLis=[];
}

/**
 * 弹出选择e卡窗口
 * 
 * @author 邓华锋
 * @date 2015年11月10日 上午12:20:37
 * 
 */
function ecardSelectDlg() {
	if ((isExistsPayPassword != null && isExistsPayPassword == false) || !existPayPassword()) {
		toast("您还未设置支付密码，请设置支付密码", null, "error", function() {
			openPayPassDlg();
		});
		return;
	}
	loadEcardList();
	var options = {
		dom : "#ecardSelectDlg", // 或者 html string
		title : "选择e卡",
		area : [ '550px', '350px' ]
	};
	openDlg(null, null, null, ecardSelectCallBackFunc, options);
}

function ecardSelectCallBackFunc(rspInfo, index) {
	var ecardId = $id("ecardId").val();
	// 如果没变化
	if (ecardSelect != null && parseInt(ecardId) == ecardSelect.id) {
		layer.close(index);
		return;
	}
	if (ecardId == "") {
		ecardSelect = null;
	} else {
		ecardSelect = ecardList.find(function(elem, i) {
			return elem.id === parseInt(ecardId);
		});
		renderHtml(ecardSelect, "ecardRowTpl", "ecardShowTr");
	}
	checkShow();
	layer.close(index);
}

function initEcardSelect() {
	ecardSelect = null;
	$id("ecardId").val("");
	$id("ecardSelect").val("");
	$id("ecard-selected").hide();
	$id("ecardShowTr").text("");
	$id("ecardPrice").text("0");
	$id("pPassword").text("");
	$id("payPasswordDl").hide();
	$id("ecardAmountDl").hide();
	$id("ecardAmount").text("¥ 0");
}
var updatePayPasswordDlg = null;
function openPayPassDlg() {
	if (phoneNo == "") {
		initUserInfo();
	}
	// 获取模板内容
	var tplHtml = $id("updatePayPasswordTpl").html();
	// 使用生成的内容
	updatePayPasswordDlg = Layer.dialog({
		title : false,
		dom : tplHtml,
		area : [ '560px', '300px' ],
		closeBtn : true,
		btn : false,
		shadeClose : true
	});
	if (phoneNo != "") {
		$("#payPhoneNo").val(phoneNo);
	}

	smsCodeSender = SmsCodeSender.newOne();
	smsCodeSender.bindCtrls("btnSendSmsCode", "payPhoneNo");
	smsCodeSender.setUsage("payPass");
	$id("btnSendSmsCode").attr("disabled", false);
}

// 更新支付密码
function doUpdatePayPassword() {
	// clearTip();
	// 获取数据
	var phoneCode = $("#phoneCode").val();
	if (!phoneCode) {
		$("#smsCodeTip").html("手机验证码不能为空");
		return;
	}
	var payPassword = $("#payPassword").val();
	if (!payPassword) {
		$("#payPasswordTip").html("旧密码不能为空");
		return;
	} else if (payPassword.length < 6) {
		$("#passwordTip").html("请输入6位以上密码");
		return;
	} else if (!/^[a-zA-Z_0-9]{6,16}$/ig.test(payPassword)) {
		$("#passwordTip").html("密码必须为6~16位由字母、数字和下划线组成的字符串");
		return;
	} else if (/^[a-zA-Z]+$/ig.test(payPassword) || /^[0-9]+$/ig.test(payPassword)) {
		$("#passwordTip").html("密码不能为纯字母或纯数字");
		return;
	}
	var rePayPassword = $("#rePayPassword").val();
	if (!rePayPassword) {
		$("#rePayPasswordTip").html("确认密码不能为空");
		return;
	} else if (payPassword != rePayPassword) {
		$("#rePayPasswordTip").html("您输入的密码不一致");
		return;
	}
	var secureLevel = getSecurityLevel(payPassword);
	payPassword = encryptStr(payPassword);
	// 生成提交数据
	var postData = ({
		payPassword : payPassword,
		phoneCode : phoneCode,
		secureLevel : secureLevel
	});
	// 可以对postData进行必要的处理（如如数据格式转换）
	// 显示等待提示框
	var loaderLayer = Layer.loading("正在验证信息...");
	//
	var ajax = Ajax.post("/user/payPass/reset/do");
	ajax.data(postData);
	ajax.done(function(result, jqXhr) {
		// 隐藏等待提示框
		loaderLayer.hide();
		if (result.type == "info") {
			updatePayPasswordDlg.hide();
			Layer.msgSuccess(result.message);
			payPassDlgCallBackFunc();
		} else {
			Layer.msgWarning(result.message);
		}

	});
	ajax.fail(function(result, jqXhr) {
		// 隐藏等待提示框
		loaderLayer.hide();
	});
	ajax.go();
}

var payWaySelect = {
	code : "alipay",
	name : "支付宝"
};
function openPayWayDlg() {
	loadPayWay();
}

// -----------------------选择门店----------------------------------{{
function openShopSelectDlg() {
	// 对话框参数名
	var argName = "argx";
	// 对话框参数值
	var argValue = {
		saleCart : saleCart,
		saleOrderInfo : saleOrderInfo,
		ecardShops : ecardShops,
		selectShopDto:selectShopDto,
		isHasGoods:isHasGoods,
		isHasSvcs:isHasSvcs
	};
	var pageUrl = "/saleOrder/shop/select/jsp";
	var options = {
		title : "选择门店",
		area : [ '1000px', '560px' ],
		btn : [ "确定选择此店" ]
	};
	openDlg(argValue, argName, pageUrl, openShopSelectDlgCallBackFunc, options);
}
// **********点击选择复选框*****************
function checkboxSelect(ts, name, event) {
	var id = name + "Id";
	var input = "input[name=" + name + "]";
	if(name=="coupon"){
		var dataType=$(ts).attr("data-type");
		var dataId=$(ts).attr("data-id");
		input="input[name=" + name + "][data-type="+dataType+"][data-id="+dataId+"]";
	}
	
	var tr = $(ts).parent().parent().parent();
	var checked = $(ts).attr("checked");
	if (!isUndef(checked) && checked == "checked") {
		$(tr).siblings().removeClass('tr-selected').end().addClass('tr-selected');
		$id(id).val($(ts).val());
	} else {
		$(tr).removeClass('tr-selected');
		$id(id).val("");
	}

	$(tr).siblings().find(input).attr("checked", "checked").end().find(input).attr("checked", false);
	event.stopPropagation();
}
function trSelect(ts, name) {
	var id = name + "Id";
	var input = "input[name=" + name + "]";
	var checkbox = $(ts).find(input);
	if(name=="coupon"){
		var dataType=$(checkbox).attr("data-type");
		var dataId=$(checkbox).attr("data-id");
		input="input[name=" + name + "][data-type="+dataType+"][data-id="+dataId+"]";
	}
	var checked = $(checkbox).attr("checked");
	var val = $(checkbox).val();
	if (isUndef(checked) || checked != "checked") {
		$(checkbox).attr("checked", "checked");
		$(ts).siblings().removeClass('tr-selected').end().addClass('tr-selected');
		$id(id).val(val);
	} else {
		$(checkbox).attr("checked", false);
		$(ts).removeClass('tr-selected');
		$id(id).val("");
	}
	$(ts).siblings().find(input).attr("checked", "checked").end().find(input).attr("checked", false);
}
// -----------------------------------------------------------------}}

/**
 * 处理选择门店返回的结果
 */
function treatmentResult(theDlg, dlgResult) {
	if(isHasStatus){
		// 遍历更新货品状态
		var productLackFlagMap = dlgResult.productLackFlagMap;
		if (productLackFlagMap) {
			var keys = productLackFlagMap.keys();
			for (var i = 0, len = keys.length; i < len; i++) {
				var productId = keys[i];
				var lackFlag = productLackFlagMap.get(productId);
				if (lackFlag == false) {
					$id("status_" + productId).html("<font color='green'>有货</font>");
				} else {
					$id("status_" + productId).html("<font color='red'>无货</font>");
				}
			}
		}
	}
	theDlg.hide();
}
/**
 * 初始化预约时间
 * 
 * @author 邓华锋
 * @date 2015年11月10日 上午12:12:05
 * 
 */
function initDatePicker() {
	var nowDate = new Date();
	var minDate = nowDate.add(1, "day");
	var maxDate = minDate.add(30, "day");
	var yearStart = minDate.getFullYear();
	var monthStart = minDate.getMonth();
	var yearEnd = maxDate.getFullYear();
	var monthEnd = maxDate.getMonth();
	var defaultDate = minDate.format("yyyy-MM-dd");
	defaultDate += " 09:00";
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
function serializeOrderJson(saleOrderPo) {
	saleOrderPo.linkMan = $id("linkMan").val();
	saleOrderPo.linkNo = $id("phoneNo").val();
	saleOrderPo.planTime = $id("planTime").val();
}
function submitCallback(resultMap) {
	if (resultMap.isPayFinished) {
		openPage("/saleOrder/pay/result/jsp?orderNo=" + resultMap.no);
	} else {
		openPage("/saleOrder/submit/result/jsp?orderNo=" + resultMap.no);
	}
}