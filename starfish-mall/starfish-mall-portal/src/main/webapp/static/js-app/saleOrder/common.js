var saleCart = {
		"amountInfo" : {
			"saleAmount" : 0,
			"discAmount" : 0,
			"amount" : 0,
			"amountOuter":0,
			"settlePrice" : 0
		}
};
//全局配置
var isHasStatus=false;
var isHasCoupon=true;
var isHasGoods=true;
var isHasSvcs=true;
var saleOrderInfo = {
	saleCartInfo : {
		saleCartSvcList : [],
		saleCartGoods : []
	},
	saleOrderPo : {}
};
var selectShopDto = null;// 选择的门店
var cartSvcList = null;
var saleCartGoods = null;
var supermarketUrl = "/product/supermarket/list/jsp";// 尖品超市url
var svcPack=null;
var userCouponMap = null;// 缓存用户优惠券列表
var couponType = KeyMap.newOne();
var couponTarget = KeyMap.newOne();// 优惠券类型
var treatmentResult=null;
// ----------------------商品服务相关---------------------------------------{{
/**
 * 获取购物车选择的商品服务列表
 * 
 * @author 邓华锋
 * @date 2015年11月10日 上午12:17:00
 * 
 */
function getSaleCart() {
	var hintBox = Layer.progress("加载中...");
	var ajax = Ajax.post("/saleCart/info/fetch/get");
	ajax.done(function(result, jqXhr) {
		if (result != "" && result.type == "info") {
			if (result.data != null) {
				saleCart = result.data;
				saleCart.amountOuter = saleCart.amountInfo.amount;
				saleCart.oldSvcAmount=saleCart.svcAmount;
				if(saleCart.amountInfo.discAmount>0){
					saleCart.svcAmount=saleCart.svcAmount.subtract(saleCart.amountInfo.discAmount);
				}
				payableGoodsAmount = saleCart.goodsAmount;// 应付商品金额
				payableSvcAffixPrice = saleCart.svcAmount;// 应付服务金额
				couponOuterAmount = saleCart.amountOuter;// 减去优惠券的应付金额
				renderSvHtmlByCartSvcList(saleCart);
				$id("content").show();
			} else {
				window.onbeforeunload = function(event) {
				};
				toast("您还未选择商品服务,5秒后前往尖品超市", null, "error", function() {
					openPage(supermarketUrl);
				});
			}
		} else {
			toast(result.message, null, "warning", function() {
				openPage(supermarketUrl);
			});
		}
	});
	ajax.always(function() {
		hintBox.hide();
	});
	ajax.fail(function(result, jqXhr){
		if (result.message == "timeout") {
			toast("请求超时！", null, "warning", function() {});
		} else {
			toast(result.message, null, "error", function() {});
		}
	});
	ajax.go();
}

/**
 * 给对应价格赋值
 * 
 * @author 邓华锋
 * @date 2015年11月10日 上午12:08:57
 * 
 * @param saleCart
 */
function saleCartamount(saleCart) {
	if (saleCart != null) {
		if (saleCart.goodsAmount != null) {// 商品价格
			$id("goodsAmount").html("¥ " + saleCart.goodsAmount);
		} else {
			$id("goodsAmount").html("¥ " + 0);
		}
		if (saleCart.svcAmount != null) {// 服务价格
			renderHtml(saleCart, "svAmountShowTpl", "svAmountShow");
		} else {
			renderHtml(saleCart, "svAmountShowTpl", "svAmountShow");
		}
		if (saleCart.amountOuter != null) {// 总价
			$id("totalAmount").html("¥ " + saleCart.amountOuter);
		} else {
			$id("totalAmount").html("¥ " + 0);
		}
	} else {
		$id("goodsAmount").html("¥ " + 0);
		$id("totalAmount").html("¥ " + 0);
		$id("svcAmount").html("¥ " + 0);
	}
}
/**
 * 加载商品服务列表
 * 
 * @author 邓华锋
 * @date 2015年11月10日 上午12:17:00
 * 
 */
function renderSvHtmlByCartSvcList(saleCart) {
	if(isHasSvcs){
		cartSvcList = saleCart.saleCartSvcList;// 服务列表
	}
	if(isHasGoods){
		saleCartGoods = saleCart.saleCartGoods;// 商品列表
	}
	if (isHasSvcs&&cartSvcList == null && isHasGoods&&saleCartGoods == null) {
		window.onbeforeunload = function(event) {
		};
		toast("您还未选择商品服务,5秒后前往尖品超市", null, "error", function() {
			openPage(supermarketUrl);
		});
		return;
	}
	var size = 0
	if(isHasSvcs){
		var len=cartSvcList.length;
		 size += len;
		if (len > 0) {
			renderHtml(cartSvcList, "saleCartRowTpl", "carSvcGoods");
		}else {
			$id("carSvcGoods").remove();
		}
	}
	if(isHasGoods){
		var len = saleCartGoods.length;
		size += len;
		if (len > 0) {
			renderHtml(saleCartGoods, "optGoodsRowTpl", "optGoods");
		} else {
			$id("optGoods").remove();
		}
	}
	saleCartamount(saleCart);
	console.log("isHasSvcs:"+isHasSvcs+" isHasGoods:"+isHasGoods+" size:"+size);
	if ((isHasSvcs||isHasGoods)&&size == 0) {
		window.onbeforeunload = function(event) {
		};
		toast("您还未选择商品服务,5秒后前往尖品超市", null, "error", function() {
			openPage(supermarketUrl);
		});
	}
}
// -----------------------------------------------------------------}}
// -------------------------加载用户信息-------------------------------{{
var phoneNo = "";
var nickName = null;
// 加载用户信息
function initUserInfo() {
	var ajax = Ajax.post("/user/info/get");
	ajax.sync();
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			if (result.data != null) {
				var user = result.data.user;
				nickName = user.nickName;
				phoneNo = user.phoneNo;
			}
		}
	});
	ajax.go();
}
// -------------------------联系人-------------------------------{{
var linkManSelect = null;
var isExistLinkMan = false;// 是否存在联系人
var userLinkWayList = false;
function getDefaultLinkWay() {
	var ajax = Ajax.post("/user/linkWay/default/get");
	ajax.data({});
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			linkManSelect = result.data;
			showLinkWay();
		} else {
			toast(result.message, null, "error");
		}
	});
	ajax.go();
}
function getDefaultUserCar(){
	var ajax = Ajax.post("/car/userCar/default/get");
	ajax.data({});
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			userCarSelect = result.data;
			if(userCarSelect!=null){
				showUserCar();
			}
		} else {
			toast(result.message, null, "error");
		}
	});
	ajax.go();
}
function loadLinkMan() {
	var ajax = Ajax.post("/user/linkWay/list/get");
	ajax.sync();
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			userLinkWayList = result.data;
			renderHtml(userLinkWayList, "linkManRowTPL", "linkManList");
		} else {
			toast(result.message, null, "error");
		}
	});
	ajax.go();
}

var userCarSelect = null;
var userCarList = null;
function loadUserCar() {
	var ajax = Ajax.post("/car/userCar/list/get");
	ajax.sync();
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			userCarList = result.data;
			renderHtml(userCarList, "userCarListTPL", "userCarList");
		} else {
			toast(result.message, null, "error");
		}
	});
	ajax.go();
}
window.onbeforeunload = function(event) {
	var message = "您所查找的页面要使用已输入的信息。返回此页可能需要重复已进行的所有操作。";
	var c = event || window.event;
	if (/webkit/.test(navigator.userAgent.toLowerCase())) {
		return message;
	} else {
		c.returnValue = message;
	}
}

var goodsMap = KeyMap.newOne();
var svcCouponMap =null;
var proCouponMap =null;
function loadCouponList() {
	saleOrderInfo.saleCartInfo.saleCartSvcList = saleCart.saleCartSvcList;// 关联服务列表
	saleOrderInfo.saleCartInfo.saleCartGoods = saleCart.saleCartGoods;// 关联商品列表
	var ajax = Ajax.post("/user/coupon/map/get");
	ajax.sync();
	ajax.data(saleOrderInfo);
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			userCouponMap = KeyMap.from(result.data);
			/*
			 * var keys = couponMap.keys(); for(var i=0,len=keys.length;i<len;i++){ var key=keys[i]; var userCouponList=userCouponMap.get(key); if(key.contain("goods")){ var goodsId=key.substring(5);
			 * renderHtml({userCouponList:userCouponList,type:"goods"}, "couponListSelectTpl","goodsCoupon"+goodId); }else if(key.contain("svc")){ var svcId=key.substring(3);
			 * renderHtml({userCouponList:userCouponList,type:"svc"}, "couponListSelectTpl","svcCoupon"+svcId); } }
			 */
			svcCouponMap = KeyMap.from(userCouponMap.get("svc"));
			proCouponMap = KeyMap.from(userCouponMap.get("goods"));
			renderHtml({couponMap:svcCouponMap,type:"svc"}, "couponListTpl", "svcCouponList");
			renderHtml({couponMap:proCouponMap,type:"goods"}, "couponListTpl", "proCouponList");
		} else {
			toast(result.message, null, "error");
		}
	});
	ajax.go();
}
//根据优惠券类型 进行判断计算价格
function userCouponComputing(userCoupon) {
		if (userCoupon != null) {
			var affixPrice = null;
			var productAmount = null;
			var affixPrice = null;// 服务
			var productAmount = null;// 商品
			var price = 0;
			if (userCoupon.targetType == "goods") {
				var goods = saleCartGoods.find(function(obj, i) {
					return obj.productId === userCoupon.productId;
				});
				goods=goods||null;
				if(goods!=null){
					productAmount = goods.productAmount;
					if (userCoupon.type == "nopay") {// 无需用户支付 （关联具体商品或服务，持此券可免付相关商品或服务）
							price = productAmount;
					} else if (userCoupon.type == "sprice") {// 特价 （关联具体商品或服务，持此特价券购买相关固定价格的商品和服务可以享受特价）
						if (productAmount!=0) {
							var diffPrice = productAmount.subtract(userCoupon.price);
							price = diffPrice;
						}
					} else if (userCoupon.type == "deduct") {// 关联具体商品、分类或服务，持此券可以抵扣相应的金额
						price = userCoupon.price;
					}
				}
			} else if (userCoupon.targetType == "svc") {
				var cartSvc = cartSvcList.find(function(obj, i) {
					return obj.svcId === userCoupon.svcId;
				});
				cartSvc=cartSvc||null;
				if (cartSvc!=null) {
					affixPrice = cartSvc.salePrice;
					if(cartSvc.amountInfo.discAmount>0){
						affixPrice =cartSvc.amountInfo.amount;
					}
					if (userCoupon.type == "nopay") {// 无需用户支付 （关联具体商品或服务，持此券可免付相关商品或服务）
							price = affixPrice;
					} else if (userCoupon.type == "sprice") {// 特价 （关联具体商品或服务，持此特价券购买相关固定价格的商品和服务可以享受特价）
						if (affixPrice!=0) {
							var diffPrice = affixPrice.subtract(userCoupon.price);
							price = diffPrice;
						}
					} else if (userCoupon.type == "deduct") {// 关联具体商品、分类或服务，持此券可以抵扣相应的金额
						price = userCoupon.price;
					}
				}
			}
			userCoupon.payAmount = price;
		}
}
var payableGoodsAmount = 0;// 应付商品金额
var payableSvcAffixPrice = 0;// 应付服务金额
var couponOuterAmount = 0;// 减去优惠券的应付金额
var selectProCouponMap = KeyMap.newOne();
var selectSvcCouponMap = KeyMap.newOne();
function checkShow() {
	saleCart.amountOuter = saleCart.amountInfo.amount;// 重新计算
	payableGoodsAmount = saleCart.goodsAmount;// 应付商品金额
	payableSvcAffixPrice = saleCart.svcAmount;// 应付服务金额
	var couponPayAmount = 0;
	var isHasSelectCoupon = false;
	if(selectProCouponMap&&$.isFunction(selectProCouponMap.keys)){
		for (var i = 0, keys = selectProCouponMap.keys(), len = keys.length; i < len; i++) {
			if (i == 0) {
				isHasSelectCoupon = true;
			}
			var key = keys[i];
			var productCoupon = selectProCouponMap.get(key);
			payableGoodsAmount = payableGoodsAmount.subtract(productCoupon.payAmount);
			couponPayAmount = couponPayAmount.add(productCoupon.payAmount);
		}
	}
	if(selectSvcCouponMap&&$.isFunction(selectSvcCouponMap.keys)){
		for (var i = 0, keys = selectSvcCouponMap.keys(), len = keys.length; i < len; i++) {
			if (i == 0) {
				isHasSelectCoupon = true;
			}
			var key = keys[i];
			var svcCoupon = selectSvcCouponMap.get(key);
			payableSvcAffixPrice = payableSvcAffixPrice.subtract(svcCoupon.payAmount);
			couponPayAmount = couponPayAmount.add(svcCoupon.payAmount);
		}
	}
	// map格式
	if (isHasSelectCoupon) {
		saleCart.amountOuter = saleCart.amountOuter.subtract(couponPayAmount);
		$id("couponPrice").text(couponPayAmount);
		$id("couponAmount").text("-¥ " + couponPayAmount);
		$id("coupon-selected").show();
		$id("couponAmountDl").show();
	} else {
		initCouponSelect();
	}
	couponOuterAmount = saleCart.amountOuter;
	var ecardPayAmount = 0;
	if (ecardSelect != null) {
		thisUseComputing(ecardSelect);
		ecardPayAmount = ecardSelect.payAmount;
		saleCart.amountOuter = saleCart.amountOuter.subtract(ecardPayAmount);
		$id("ecardPrice").text(ecardPayAmount);
		$id("ecardAmount").text("-¥ " + ecardPayAmount);
		$id("ecard-selected").show();
		$id("ecardAmountDl").show();
		$id("payPasswordDl").show();
		if (isExistsPayPassword) {
			$id("pPassword").show();
			$id("forgetPayPassword").show();
			$id("btnSettingPayPassword").hide();
		} else {
			$id("btnSettingPayPassword").show();
			$id("pPassword").hide();
			$id("forgetPayPassword").hide();
		}
	} else {
		initEcardSelect();
	}

	// 如果优惠券支付的金额等于总价，则隐藏e卡和支付方式。
	if (couponPayAmount === saleCart.amountInfo.amount) {
		$id("payWay").val("coupon");
		initEcardSelect();
		$id("ecardDl").hide();
	} else {
		$id("ecardDl").show();
	}
	if (saleCart == null || saleCart.amountOuter > 0) {
		// TODO 默认显示 支付宝 支付方式设为支付宝
		$id("paywayDl").show();
	} else {// e卡支付总额=总应支付 或 优惠券支付+e卡支付=总应支付 或 优惠券支付=总应支付 (ecardPayAmount == totalAmount || couponPayAmount.add(ecardPayAmount) == totalAmount || couponPayAmount === totalAmount)
		$id("paywayDl").hide();
		$id("payWay").val("");
		if (ecardPayAmount == saleCart.amountOuter) {
			$id("payWay").val("ecard");
		}
	}
	saleCartamount(saleCart);
}
// -----------------------------------------------------------------}}
// --------------------------e卡----------------------------------{{
var ecardSelect = null;// 选中的E卡
var isExistsPayPassword = null;
var ecardShops = [];// 缓存的E卡门店
/**
 * e卡本次使用计算方法
 */
function thisUseComputing(ecard) {
	if (ecard != null) {
		var saleCartAmount = couponOuterAmount;
		if (ecard.shopId == null || (selectShopDto != null && ecard.shopId == selectShopDto.id) || payableGoodsAmount >= saleCartAmount) {
			if (ecard.remainVal >= saleCartAmount) {
				ecard.payAmount = saleCartAmount;
			} else {
				ecard.payAmount = ecard.remainVal;
			}
		} else {
			if (ecard.remainVal >= payableGoodsAmount) {
				ecard.payAmount = payableGoodsAmount;
			} else {
				ecard.payAmount = ecard.remainVal;
			}
		}
	}
}
function loadEcardList() {
	var ajax = Ajax.post("/ecard/userECard/list/normal/get");
	if (selectShopDto != null) {
		ajax.data({
			shopId : selectShopDto.id
		});
	}
	ajax.sync();
	ajax.done(function(result, jqXhr) {
		ecardList = result.data;
		renderHtml(ecardList, "ecardListTpl", "ecardList");
	});
	ajax.go();
}
// ----------------------------支付密码-----------------------------{{
function existPayPassword() {
	if (isExistsPayPassword != null) {
		return isExistsPayPassword;
	} else {
		var ajax = Ajax.post("/user/payPass/exist");
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var exist = result.data;
				if (exist) {
					isExistsPayPassword = true;
				} else {
					isExistsPayPassword = false;
				}

			}
		});
		ajax.go();
		return isExistsPayPassword;
	}
}
function payPassDlgCallBackFunc() {
	isExistsPayPassword = true;
	$id("pPassword").show();
	$id("forgetPayPassword").show();
	$id("btnSettingPayPassword").hide();
}
var paywayList = null;
function loadPayWay() {
	var ajax = Ajax.post("/setting/payWay/usbale/get");
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			paywayList = result.data;
			renderHtml(paywayList, "paywayRowTpl", "paywayList");
		} else {
			toast(result.message, null, "error");
		}
	});
	ajax.go();
}
/**
 * 渲染页面内容
 * 
 * @author 邓华锋
 * @date 2015年11月10日 上午12:20:37
 * 
 * @param data
 * @param fromId
 * @param toId
 */
function renderHtml(data, fromId, toId) {
	// 获取模板内容
	var tplHtml = $id(fromId).html();
	// 生成/编译模板
	var theTpl = laytpl(tplHtml);
	// 根据模板和数据生成最终内容
	var htmlStr = theTpl.render(data);
	// 使用生成的内容
	$id(toId).html(htmlStr);
}
var resBaseUrl = getResUrl("");
function openShopSelectDlgCallBackFunc(theDlg) {
	var shopId = null;
	var hasLackFlag = null;
	if (jsType == "pc") {
		// 对话框页面窗口对象
		var dlgWin = theDlg.getFrameWindow();
		// 对话框页面的接口函数
		var dlgCallback = dlgWin["getDlgResult"];
		// 获取返回值
		var dlgResult = dlgCallback();
		saleOrderInfo = dlgResult.saleOrderInfo;
		saleCart = dlgResult.saleCart;
		// couponSelect = saleCart.couponSelect;
		hasLackFlag = dlgResult.hasLackFlag;
	} else if (jsType == "app") {
		saleOrderInfo = theDlg.saleOrderInfo;
		saleCart = theDlg.saleCart;
	}
	var shopId = saleOrderInfo.saleOrderPo.shopId;
	var len = 0;
	if(isHasSvcs){
		if (!isUndef(saleCart.saleCartSvcList) && !isNull(saleCart.saleCartSvcList)) {
			len = saleCart.saleCartSvcList.length;
		}
	}
	if(isHasGoods){
		if (!isUndef(saleCart.saleCartGoods) && !isNull(saleCart.saleCartGoods)) {
			len += saleCart.saleCartGoods.length;
		}
	}
	if (isUndef(shopId) || isNull(shopId)) {
		if (jsType == "pc") {
			var theLayer = Layer.info("请选择门店", function(layerIndex) {
				theLayer.hide();
			});
		} else if (jsType == "app") {
			plus.nativeUI.toast("请选择门店");
		}
		return;
	} else if (hasLackFlag) {
		var theLayer = Layer.info("存在无货的商品，请删除后再进行操作", function(layerIndex) {
			theLayer.hide();
		});
		return;
	} else if ((isHasSvcs||isHasGoods)&&len == 0) {
		window.onbeforeunload = function(event) {
		};
		var theLayer = Layer.info("没有商品服务了，前往尖品超市继续购买", function(layerIndex) {
			openPage("/product/supermarket/list/jsp");
			theLayer.hide();
		});
		return;
	} else {
		var tempShopDto = saleOrderInfo.saleOrderPo.shopDto;
		var isChange = selectShopDto != null && tempShopDto.id != selectShopDto.id;
		var isFist = selectShopDto == null;
		selectShopDto = saleOrderInfo.saleOrderPo.shopDto;
		$("#shopId").val(selectShopDto.id);
		renderHtml(selectShopDto, "selectShopTpl", "shopAddress");
			// 重新加载子窗口操作后的服务列表
		renderSvHtmlByCartSvcList(saleCart);
		// 如果店铺选择更改，重新加载E卡列表，重新加载当前选择的E卡，则判断如果不是绑定此店的E卡，并更改e卡的价格,只能支付商品的价格
		if (isFist || isChange) {
			if (ecardSelect != null && ecardSelect.shopId != null) {
				// 如果所选的店铺ID等于E卡的店铺ID，可以支付服务和商品的价格
				if (selectShopDto.id == ecardSelect.shopId) {
					/*
					 * var theLayer = Layer.info("您所选E卡支持支付此门店的服务和商品价格", function(layerIndex) { theLayer.hide(); });
					 */
				} else {// 只能支付商品的价格
					if (jsType == "pc") {
						var theLayer = Layer.info("您所选E卡只能支付此门店的商品价格", function(layerIndex) {
							theLayer.hide();
						});
					} else if (jsType == "app") {
						plus.nativeUI.toast("您所选E卡只能支付此门店的商品价格");
					}
				}
			}
		}
		checkShow();// 重新计算检查显示
		if (!isUndef(treatmentResult)&&$.isFunction(treatmentResult)) {
			treatmentResult.call(this, theDlg, dlgResult);
		}
	}
}
var submitOrderUrl="/saleOrder/submit/do";
/**
 * 提交订单
 * 
 * @author 邓华锋
 * @date 2015年11月10日 上午12:14:32
 * 
 */
function submitOrder() {
	var saleOrderPo = saleOrderInfo.saleOrderPo;
	saleOrderPo.shopId = $id("shopId").val();
	serializeOrderJson(saleOrderPo);
	saleOrderPo.payWay = $id("payWay").val();
	saleOrderPo.leaveMsg = $id("leaveMsg").val();
	if (couponSelect != null) {
		saleOrderPo.userCouponId = couponSelect.id;
	}

	if (isNullOrBlank(saleOrderPo.shopId)) {
		toast("请选择门店", null, "error");
		return;
	}

	if (isNullOrBlank(saleOrderPo.planTime)) {
		toast("请选择预约时间", null, "error");
		return;
	}
	var hour = saleOrderPo.planTime.substring(10, 13);
	hour = ParseInt(hour);
	if (hour < 9 || hour > 18) {
		toast("预约时间范围请控制在运营时间早上9点到晚上6点", null, "error");
		return;
	}
	if (isNullOrBlank(saleOrderPo.linkMan)) {
		toast("请选择联系人", null, "error");
		return;
	}

	if (isNullOrBlank(saleOrderPo.linkNo) || !isMobile(saleOrderPo.linkNo.trim())) {
		toast("请您填写手机号码", null, "error");
		return;
	}
	if(userCarSelect==null||isUndef(userCarSelect)){
		toast("请选择车辆", null, "error");
		return;
	}else{
		saleOrderPo.carId=userCarSelect.id;
	}
	if (ecardSelect != null) {
		saleOrderPo.userEcardId = ecardSelect.id;
		saleOrderPo.payPassword = $id("pPassword").val();
		if (isNullOrBlank(saleOrderPo.payPassword)) {
			toast("请输入支付密码", null, "error");
			return;
		}
		saleOrderPo.payPassword = encryptStr(saleOrderPo.payPassword);
	}

	$id("btnSubmit").attr({
		"disabled" : "disabled"
	});
	
	if(svcPack==null){
		//组装服务优惠券
		for (var i = 0, keys = selectSvcCouponMap.keys(), len = keys.length; i < len; i++) {
			var key = keys[i];
			var svcCoupon = selectSvcCouponMap.get(key);
			var svc=saleCart.saleCartSvcList.find(function(item,i){
				return item.svcId===svcCoupon.svcId;
			});
			svc=svc||null;
			if(svc!=null){
				svc.userCoupon=svcCoupon;
			}
		}
		//组装商品优惠券
		for (var i = 0, keys = selectProCouponMap.keys(), len = keys.length; i < len; i++) {
			var key = keys[i];
			var productCoupon = selectProCouponMap.get(key);
			var goods=saleCart.saleCartGoods.find(function(item,i){
				return item.productId===productCoupon.productId;
			});
			goods=goods||null;
			if(goods!=null){
				goods.userCoupon=productCoupon;
			}
		}
		saleOrderInfo.saleCartInfo.saleCartSvcList = saleCart.saleCartSvcList;// 关联服务列表
		saleOrderInfo.saleCartInfo.saleCartGoods = saleCart.saleCartGoods;// 关联商品列表
	}else{
		saleOrderPo.packId=svcPack.id;
	}
	var ajax = Ajax.post(submitOrderUrl);
	ajax.data(saleOrderInfo);
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			window.onbeforeunload = function(event) {
			};
			var resultMap = result.data;
			var callback = function() {
				submitCallback(resultMap);// 提交返回方法
			};
			var closeDelay = 3000;
			toast(result.message, closeDelay, null, callback);
		} else {
			toast(result.message, null, "error", function() {
				$id("btnSubmit").removeAttr("disabled");
			});
		}
	});
	ajax.go();
}
