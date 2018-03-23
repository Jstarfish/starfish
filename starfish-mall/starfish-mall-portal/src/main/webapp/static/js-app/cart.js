var saleOrderInfo = {};
var saleCartInfo = {};
var saleCartSvcList = [];
var local_saleCart = {}
// 渲染页面内容
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
// 根据id删除
function onDeleteCart(cartId, deleteType) {
	var msg = "确定要删除吗？";
	var yesHandler = function(layerIndex) {
		theLayer.hide();
		deleteCartid(cartId, deleteType);
	};
	var noHandler = function(layerIndex) {
		theLayer.hide();
	};
	var theLayer = Layer.confirm(msg, yesHandler, noHandler);
	// Layer.confirm("确定要删除吗？", deleteCartid(cartId, deleteType), noHandler);
}
// 删除
function deleteCartid(cartId, deleteType) {
	var ids = [];
	if (deleteType == "svc" || deleteType == "goods") {// 判断是否是单个商品或服务
		ids.add(cartId);
		saleOrderInfo.deleteType = deleteType;
	}
	// 删除tr
	var returnDelete = deleteTr(cartId, deleteType);
	if (returnDelete == true) {
		saleOrderInfo.ids = ids;
		// 计算价格
		if (saleCartSvcList != null && saleCartSvcList.length > 0) {
			// 删除本地购物车，留最新购物车
			deletelocal(ids, saleOrderInfo);
			//
			packageJson(null,null,null);
			computeAjax(saleOrderInfo);
		}
	}
}

// 删除本地最新购物车
function deletelocal(ids, saleOrderInfo) {
	// 删除本地最新购物车
	if (saleCartSvcList != null && saleCartSvcList.length > 0) {
		for (var i = 0; i < saleCartSvcList.length; i++) {
			var saleCartSvc = saleCartSvcList[i];
			if (saleOrderInfo.deleteType == "svc") {
				var index = ids.indexOf(saleCartSvc.svcId);
				if (index != 'undefined' && index > -1) {
					var index = saleCartSvcList.indexOf(saleCartSvc);
					saleCartSvcList.removeAt(index);
					i--;
				}
			} else if (saleOrderInfo.deleteType == "goods") {
				var goodsList = saleCartSvc.saleCartGoodsList;
				if (goodsList != null && goodsList.length > 0) {
					for (var j = 0; j < goodsList.length; j++) {
						var goods = goodsList[j];
						if (saleCartSvc.svcId + "" + goods.productId == ids[0]) {
							if (saleCartSvc.svcId == -1) {
								if (goodsList.length > 1) {
									var index = goodsList.indexOf(goods);
									goodsList.removeAt(index);
								} else {
									var index = saleCartSvcList.indexOf(saleCartSvc);
									saleCartSvcList.removeAt(index);
								}
							} else {
								var index = goodsList.indexOf(goods);
								goodsList.removeAt(index);
							}
						}
					}
				}
			}
		}
	}
}
// 删除tr
function deleteTr(cartId, deleteType) {
	if (deleteType == "svc") {
		var tabls = $id("saleCart" + cartId).parent().attr("id");
		if ($id(tabls).children("tr").length == 2) {
			$id(tabls).children(":first").remove();
		}
		$id("saleCart" + cartId).remove();
		//移除服务样式
		var carSvcId = $id("carSvc" + cartId);
		carSvcId.removeAttr("class");
		//$id("carSvc" + cartId).removeAttr("class");
		carSvcId.find("img").attr("src",carSvcId.attr("data-imgTwo"));
		return true;
	}
	if (deleteType == "goods") {
		var tabls = $id("cgoods" + cartId).parent().parent().attr("id");
		if ($id(tabls).children().children("tr").length == 1) {
			if (tabls == "goodsList-1") {
				// $id("cgoods"+cartId).remove();
				$id("optGoods").children().remove();
				return true;
			} else {
				Layer.msgWarning("至少剩余一种商品！")
				return false;
			}
		} else {
			$id("cgoods" + cartId).remove();
			return true;
		}
	}
}
// 初始化加载
function ajaxCart() {
	var ajax = Ajax.post("/saleCart/list/do");
	// alert(data);
	ajax.done(function(result, jqXhr) {
		//
		if (result != "" && result.type == "info") {
			if (result.data != null) {
				var carSvcGoods = [];// 车辆服务商品
				var optGoods = null;// 自选商品
				var cartSvcList = result.data.saleCartSvcList;
				if (cartSvcList.length > 0) {
					for (var i = 0; i < cartSvcList.length; i++) {
						var saleCartSvc = cartSvcList[i];
						if (saleCartSvc.svcId != -1) {
							carSvcGoods.push(saleCartSvc);
						} else {
							optGoods = saleCartSvc;
						}
					}
					saleCartamount(result.data);
					if (carSvcGoods.length > 0) {
						renderHtml(carSvcGoods, "saleCartRowTpl","carSvcGoods");
					}
					if (optGoods != null && optGoods != "") {
						renderHtml(optGoods, "optGoodsRowTpl","optGoods");
					}
					saleCartSvcList = cartSvcList;
					local_saleCart = result.data;
					renderNumSpinner("numSpinner", doOnNumChange, 1, 1,1000);
				}
			} else {
				saleCartamount(null);
			}
		} else {
			Layer.warning(result.message);
		}
	});
	ajax.go();
}
// 加减数量
function doOnNumChange(numValue, htDom) {
	var dataId = $(htDom).attr("data-id");
	var svcId = $(htDom).attr("id");
	$id("carts" + svcId).attr("checked", "checked");
	packageJson(svcId, dataId, numValue);
	// 计算价格请求
	computeAjax(saleOrderInfo);
}

// 计算价格
function computeAjax(saleOrder) {
	var ajax = Ajax.post("/saleCart/amount/calc/nocheck/do");
	ajax.data(saleOrder);
	ajax.done(function(result, jqXhr) {
		if (result.data != null) {
			var cartSvcList = result.data.saleCartSvcList;
			if (cartSvcList != null && cartSvcList.length > 0) {
				for (var i = 0; i < cartSvcList.length; i++) {
					var saleCartSvc = cartSvcList[i];
					$id("saleAmount" + saleCartSvc.svcId).html(
							"¥" + saleCartSvc.amountInfo.saleAmount);
				}
				saleCartamount(result.data);
			} else {
				saleCartamount(null);
			}
		} else {
			saleCartamount(null);
		}

	});
	ajax.go();
}
// 异步删除
function syncSvcCartDelete(saleOrder) {
	var ajax = Ajax.post("/saleCart/delete/sync/do");
	ajax.data(saleOrder);
	ajax.done(function(result, jqXhr) {
	});
	ajax.go();
}
// 异步更新商品数量
function syncSvcCartUpdate(saleOrder) {
	var ajax = Ajax.post("/saleCart/update/sync/do");
	ajax.data(saleOrder);
	ajax.done(function(result, jqXhr) {
	});
	ajax.go();
}
// 价格赋值
function saleCartamount(saleCart) {
	if (saleCart != null) {
		if (saleCart.goodsAmount != null) {
			$id("shoppingPrices").html("¥&nbsp;" + saleCart.goodsAmount);
		} else {
			$id("shoppingPrices").html("¥&nbsp;" + 0);
		}
		if (saleCart.cartSvcAffixPrice != null) {
			$id("svcPrices").html("¥&nbsp;" + saleCart.cartSvcAffixPrice);
		} else {
			$id("svcPrices").html("¥&nbsp;" + 0);
		}
		if (saleCart.amount != null) {
			$id("finalAmount").html("¥&nbsp;" + saleCart.amount);
		} else {
			$id("finalAmount").html("¥&nbsp;" + 0);
		}
		if (saleCart.goodsNumber != null) {
			$id("goodsNumber").html(saleCart.goodsNumber);
			$id("allCartSvcCount").html("（" + saleCart.goodsNumber + "）");
		} else {
			$id("goodsNumber").html("（" + 0 + "）");
			$id("allCartSvcCount").html("（" + 0 + "）");
		}
	} else {
		$id("shoppingPrices").html("¥&nbsp;" + 0);
		$id("goodsNumber").html("（" + 0 + "）");
		$id("finalAmount").html("¥&nbsp;" + 0);
		$id("svcPrices").html("¥&nbsp;" + 0);
	}

}
// 封装json 用于计算价格
function packageJson(svcId, goodsId, value) {
	// 车辆购物车服务列表
	var qry_saleCartSvcList = []
	if (saleCartSvcList != null && saleCartSvcList.length > 0) {
		for (var i = 0; i < saleCartSvcList.length; i++) {
			var saleCartSvc = saleCartSvcList[i];
			// alert(saleCart.checkFalg);
			// 车辆服务
			var qry_saleCartSvc = {}
			qry_saleCartSvc.id = saleCartSvc.id;
			qry_saleCartSvc.affixPrice = saleCartSvc.affixPrice;
			qry_saleCartSvc.svcId = saleCartSvc.svcId;
			// 存放车辆服务商品数据
			var qry_saleCartGoodsList = [];
			var saleCartGoodsList = saleCartSvc.saleCartGoodsList;
			if (saleCartGoodsList != null && saleCartGoodsList.length > 0) {
				for (var j = 0; j < saleCartGoodsList.length; j++) {
					var saleCartGoods = saleCartGoodsList[j];
					if (saleCartSvc.svcId == svcId
							&& goodsId == saleCartGoods.productId) {
						if (value != null) {
							saleCartGoods.quantity = value;
						}
					}
					// 车辆服务商品
					var qry_saleCartGoods = {}
					qry_saleCartGoods.id = saleCartGoods.id;
					qry_saleCartGoods.quantity = saleCartGoods.quantity;
					qry_saleCartGoods.productAmount = saleCartGoods.productAmount;
					//
					qry_saleCartGoodsList.push(qry_saleCartGoods);
				}
				qry_saleCartSvc.saleCartGoodsList = qry_saleCartGoodsList;
			}
			qry_saleCartSvcList.push(qry_saleCartSvc);
		}
	}
	//
	saleCartInfo.saleCartSvcList = qry_saleCartSvcList;
	saleCartInfo.saleCartCar = {};
	//
	saleOrderInfo.saleCartInfo = saleCartInfo;
}
$(function() {
	ajaxCart();
});