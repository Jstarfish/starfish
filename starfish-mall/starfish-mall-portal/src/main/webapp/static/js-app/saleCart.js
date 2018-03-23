var _saleCartSvcList = null;
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
function onDeleteCart(dataId, deleteType) {
	var msg = "确定要删除吗？";
	var yesHandler = function(layerIndex) {
		theLayer.hide();
		deleteCartid(dataId, deleteType);
	};
	var noHandler = function(layerIndex) {
		theLayer.hide();
	};
	var theLayer = Layer.confirm(msg, yesHandler, noHandler);

}
// 删除
function deleteCartid(dataId, deleteType) {
	if (deleteType == "svc") {
		var svcPo = svcParameterPo(dataId, "minus", null);
		syncCartSvc(svcPo,callBackSaleCartAmount);
		deleteCartTr("cartSvcTr" + dataId);
	} else if (deleteType == "goods") {
		var quantity = $id("numSpinner" + dataId).find(">input").val();
		var goodsPo = goodsParameterPo(dataId, "minus", quantity, null);
		if (goodsPo != null) {
			syncCartGoods(goodsPo,callBackSaleCartAmount);
			deleteCartTr("cartGoodsTr" + dataId);
		}
	} else if (deleteType == "checkedDelete") {
		var cartPo = checkParameterPo("minus", null);
		if (cartPo != null) {
			syncCartManyCheckBox(cartPo,callBackSaleCartAmount);
			deleteCheckedCart(cartPo);
		}
	}
}
// 单个删除服务和商品
function deleteCartTr(dataId) {
	var tabls = $id(dataId).parent().attr("id");
	if ($id(tabls).children("tr").length == 2) {
		$id(tabls).children(":first").remove();
	}
	$id(dataId).remove();
}
// 选择性删除
function deleteCheckedCart(cartPo) {
	var svcPoList = cartPo.svcPoList;
	var goodsPoList = cartPo.goodsPoList;
	if (svcPoList != null && svcPoList.length > 0) {
		for (var i = 0; i < svcPoList.length; i++) {
			var svcPo = svcPoList[i];
			var svcId = svcPo.svcId;
			deleteCartTr("cartSvcTr" + svcId);
		}
	}
	if (goodsPoList != null && goodsPoList.length > 0) {
		for (var i = 0; i < goodsPoList.length; i++) {
			var goodsPo = goodsPoList[i];
			var productId = goodsPo.productId;
			deleteCartTr("cartGoodsTr" + productId);
		}
	}
}
// 初始化加载
function ajaxCart() {
	//var hintBox = Layer.progress("正在加载数据...");
	var ajax = Ajax.post("/saleCart/list/do");
	ajax.data({
		falgType : "check"
	});
	ajax.done(function(result, jqXhr) {
		//
		if (result != "" && result.type == "info") {
			var saleCart = result.data;
			if (result.data != null) {
				// 服务列表
				var cartSvcList = saleCart.saleCartSvcList;// 服务列表
				//
				_saleCartSvcList = cartSvcList;
				if (cartSvcList != null && cartSvcList.length > 0) {
					renderHtml(cartSvcList, "saleCartRowTpl", "saleCartSvc");
				}
				var goodList = saleCart.saleCartGoods;// 商品列表
				if (goodList != null && goodList.length > 0) {
					renderHtml(goodList, "optGoodsRowTpl", "optGoods");
				}
				saleCartamount(saleCart);
				// saleCartSvcList=cartSvcList;
				renderNumSpinner("numSpinner", doOnNumChange, 1, 1, 1000);
				doOnCheckbox();
				initChecked(saleCart);
				if ((cartSvcList == null || cartSvcList.length <= 0)
						&& (goodList == null || goodList.length <= 0)) {
					renderHtml([], "nogoodsRowTpl", "content");
					saleCartamount(null);
				}
			} else {
				// $(".content").html("没有购物车啊，自己想办法。");
				renderHtml([], "nogoodsRowTpl", "content");
				saleCartamount(null);
			}
		} else {
			Layer.warning(result.message);
		}
	});
	ajax.always(function() {
		//hintBox.hide();
	});
	ajax.go();
}
function initChecked(saleCart) {
	// 初始checkBox
	if (saleCart != null) {
		var cartSvcList = saleCart.saleCartSvcList;// 服务列表
		if (cartSvcList != null && cartSvcList.length > 0) {
			for (var i = 0; i < cartSvcList.length; i++) {
				var cartSvc = cartSvcList[i];
				if (cartSvc != null) {
					$id("checkSvc" + cartSvc.svcId).prop("checked",
							cartSvc.checkFlag || false);
				}
			}
		}
		var goodList = saleCart.saleCartGoods;// 商品列表
		if (goodList != null && goodList.length > 0) {
			for (var i = 0; i < goodList.length; i++) {
				var goods = goodList[i];
				if (goods != null) {
					$id("checkGoods" + goods.productId).prop("checked",
							goods.checkFlag || false);
				}
			}
		}
		checkParentObj();
	}
}
// --------------------------------------------------点击操作------------------------------------------------
// 点击事件
function doOnCheckbox() {
	$("[name='selectCheckbox']").click(function() {
		clickImplementCartSync(this);
		checkParentObj();
	});
	$("[name='selectAll']").click(function() {
		clickImplementCartAllSync(this, "update");
		checkNextObj(this);
	});
}
// 全选是不是选中
function checkParentObj() {
	// 获取subcheck的个数
	var chsub = $("input[type='checkbox'][name='selectCheckbox']").length;
	// 选中个数
	var checkedSub = $("input[type='checkbox'][name='selectCheckbox']:checked").length;
	//
	if (chsub == checkedSub) {
		$("[name='selectAll']").prop("checked", true);
	} else {
		$("[name='selectAll']").prop("checked", false);
	}
}
// 点击全选下节点全选中或全部选
function checkNextObj(obj) {
	if ($(obj).is(":checked")) {
		$("[name='selectCheckbox']").prop("checked", true);
		$("[name='selectAll']").prop("checked", true);
	} else {
		$("[name='selectCheckbox']").prop("checked", false);
		$("[name='selectAll']").prop("checked", false);
	}
}
function clickImplementCartAllSync(obj, action) {
	var cartPo = null;
	if ($(obj).is(":checked")) {
		cartPo = checkParameterPo(action, true);
	} else {
		cartPo = checkParameterPo(action, false);
	}
	if (cartPo != null) {
		syncCartManyCheckBox(cartPo,callBackSaleCartAmount);
	}
}
// 执行后台操作
function clickImplementCartSync(obj) {
	var checkType = $(obj).attr("data-type");
	if (checkType == "svc") {
		var svcId = $(obj).attr("data-id");
		if (svcId != null) {
			var svcPo = null;
			if ($(obj).is(":checked")) {
				svcPo = svcParameterPo(svcId, "update", true);
			} else {
				svcPo = svcParameterPo(svcId, "update", false);
			}
			if (svcPo != null) {
				syncCartSvc(svcPo,callBackSaleCartAmount);
			}
		} else {
			Layer.warning("参数异常");
		}
	} else if (checkType == "goods") {
		var productId = $(obj).attr("data-id");
		if (productId != null) {
			var goodsPo = null;
			if ($(obj).is(":checked")) {
				goodsPo = goodsParameterPo(productId, "update", null, true);
			} else {
				goodsPo = goodsParameterPo(productId, "update", null, false);
			}
			if (goodsPo != null) {
				syncCartGoods(goodsPo,callBackSaleCartAmount);
			}
		} else {
			Layer.warning("参数异常");
		}
	}
}
// 商品参数
function goodsParameterPo(productId, action, quantity, checkFlag) {
	var goodsPo = null;
	if (productId != null && action != null) {
		goodsPo = {};
		merge(goodsPo, {
			productId : productId,
			action : action,
			quantity : quantity,
			checkFlag : checkFlag
		});
	}
	return goodsPo;
}
// 服务参数
function svcParameterPo(svcId, action, checkFlag) {
	var svcPo = null;
	if (svcId != null && action != null) {
		svcPo = {};
		merge(svcPo, {
			svcId : svcId,
			action : action,
			checkFlag : checkFlag
		});
	}
	return svcPo;
}
// 全选封装参数
function checkParameterPo(action, checkFlag) {
	var cartPo = {};
	var svcPoList = [];
	var goodsPoList = [];
	$("[name='selectCheckbox']").each(
			function() {
				var dataType = $(this).attr("data-type");
				var dataId = $(this).attr("data-id");
				if (dataId != null && dataType != null) {
					if (dataType == "svc") {
						var svcPo = null;
						if (action == "update") {
							if ($(this).is(":checked")) {
								svcPo = svcParameterPo(dataId, action,
										checkFlag);
							} else {
								svcPo = svcParameterPo(dataId, action,
										checkFlag);
							}
						} else {
							if ($(this).is(":checked")) {
								svcPo = svcParameterPo(dataId, action,
										checkFlag);
							}
						}
						if (svcPo != null) {
							svcPoList.add(svcPo);
						}
					} else if (dataType == "goods") {
						var goodsPo = null;
						var quantity = $id("numSpinner" + dataId)
								.find(">input").val();
						if (action == "update") {
							if ($(this).is(":checked")) {
								goodsPo = goodsParameterPo(dataId, action,
										quantity, checkFlag);
							} else {
								goodsPo = goodsParameterPo(dataId, action,
										quantity, checkFlag);
							}
						} else {
							if ($(this).is(":checked")) {
								goodsPo = goodsParameterPo(dataId, action,
										quantity, checkFlag);
							}
						}
						if (goodsPo != null) {
							goodsPoList.add(goodsPo);
						}
					}
				}
			});
	if (svcPoList != null && svcPoList.length > 0) {
		cartPo.svcPoList = svcPoList;
	}
	if (goodsPoList != null && goodsPoList.length > 0) {
		cartPo.goodsPoList = goodsPoList;
	}
	return cartPo;
}
// ---------------------------------------------------------加减数量-----------------------------------------------------
function doOnNumChange(numValue, htDom) {
	var productId = $(htDom).attr("data-id");
	if (productId != null) {
		var goodsPo = goodsParameterPo(productId, "update", numValue, true);
		syncCartGoods(goodsPo,callBackSaleCartAmount);
		$id("checkGoods" + productId).prop("checked", true);
	}
}
// ---------------------------------------------------------计算待定-----------------------------------------------------
// 计算价格
function computeAjax(saleOrder) {
	var ajax = Ajax.post("/saleCart/amount/calc/do");
	ajax.data(saleOrder);
	ajax.done(function(result, jqXhr) {
		if (result.data != null) {
			var cartSvcList = result.data.saleCartSvcList;
			if (cartSvcList != null && cartSvcList.length > 0) {
				for (var i = 0; i < cartSvcList.length; i++) {
					var saleCartSvc = cartSvcList[i];
					$id("saleAmount" + saleCartSvc.svcId).html(
							"¥&nbsp;" + saleCartSvc.amountInfo.saleAmount);
				}
				saleCartamount(result.data);
			} else {
				// $(".content").html("没有购物车啊，自己想办法。");
				renderHtml([], "nogoodsRowTpl", "content");
				saleCartamount(null);
			}
		} else {
			// $(".content").html("没有购物车啊，自己想办法。");
			renderHtml([], "nogoodsRowTpl", "content");
			saleCartamount(null);
		}

	});
	ajax.go();
}
// ------------------------------------------------------回调函数-----------------------------------------------
function callBackSaleCartAmount(saleCart) {

}
// ----------------------------------------------------请求操作-------------------------------------------------
// 添加，更新，减少服务
function syncCartSvc(svcPo, callBackFuc) {
	var ajax = Ajax.post("/saleCart/svc/sync/do");
	ajax.data(svcPo);
	ajax.done(function(result, jqXhr) {
		// 返回执行
		if (result.type == "info") {
			var saleCart = result.data;
			if (saleCart != null) {
				// 价额赋值
				saleCartamount(saleCart);
			} else {
				renderHtml([], "nogoodsRowTpl", "content");
				saleCartamount(null);
			}
			//回调
			callBackFuc(saleCart);
		} else {
			Layer.warning(result.message);
		}
	});
	ajax.go();
}
// 添加，更新，减少商品
function syncCartGoods(goodsPo, callBackFuc) {
	var ajax = Ajax.post("/saleCart/goods/sync/do");
	ajax.data(goodsPo);
	ajax.done(function(result, jqXhr) {
		// 返回执行
		if (result.type == "info") {
			var saleCart = result.data;
			if (saleCart != null) {
				// 价额赋值
				saleCartamount(saleCart);
			} else {
				renderHtml([], "nogoodsRowTpl", "content");
				saleCartamount(null);
			}
			//回调
			callBackFuc(saleCart);
		} else {
			Layer.warning(result.message);
		}
	});
	ajax.go();
}
// 多选同步（更新，减少）
function syncCartManyCheckBox(cartPo, callBackFuc) {
	var ajax = Ajax.post("/saleCart/check/sync/do");
	ajax.data(cartPo);
	ajax.done(function(result, jqXhr) {
		// 返回执行
		if (result.type == "info") {
			var saleCart = result.data;
			if (saleCart != null) {
				// 价额赋值
				saleCartamount(saleCart);
			} else {
				renderHtml([], "nogoodsRowTpl", "content");
				saleCartamount(null);
			}
			//回调
			callBackFuc(saleCart);
		} else {
			Layer.warning(result.message);
		}
	});
	ajax.go();
}
// ---------------------------------------------------------价格赋值-----------------------------------------------------
// 价格赋值
function saleCartamount(saleCart) {
	if (saleCart != null) {
		if (saleCart.goodsAmount != null) {
			$id("shoppingPrices").html("¥&nbsp;" + saleCart.goodsAmount);
		} else {
			$id("shoppingPrices").html("¥&nbsp;" + 0);
		}
		if (saleCart.svcAmount != null) {
			$id("svcPrices").html("¥&nbsp;" + saleCart.svcAmount);
		} else {
			$id("svcPrices").html("¥&nbsp;" + 0);
		}
		var amountInfo = saleCart.amountInfo;
		if (amountInfo != null) {
			if (amountInfo.saleAmount != null) {
				$id("finalAmount").html("¥&nbsp;" + amountInfo.saleAmount);
			} else {
				$id("finalAmount").html("¥&nbsp;" + 0);
			}
		} else {
			$id("finalAmount").html("¥&nbsp;" + 0);
		}
		if (saleCart.goodsCount != null) {
			$id("goodsCount").html(saleCart.goodsCount);
		} else {
			$id("goodsCount").html("（" + 0 + "）");
		}
	} else {
		$id("goodsCount").html("（" + 0 + "）");
		$id("finalAmount").html("¥&nbsp;" + 0);
		$id("svcPrices").html("¥&nbsp;" + 0);
		$id("allCartSvcCount").html("（" + 0 + "）");
	}
}
//------------------------------------------------下一步--------------------------------------------------
$id("next").click(function() {
	var checkedLength = $(".order-td").find("input[type='checkbox'][name='selectCheckbox']:checked").length;
	if(checkedLength != null && checkedLength > 0){
		setPageUrl(getAppUrl("/saleOrder/submit/jsp"));
	}else{
		var theLayer = Layer.warning("请至少选中一种服务或一件商品！",function(){
			theLayer.hide();
		});
	}
	return null;
	var listPo = selectlocal();
	if (listPo.length > 0) {
		var ajax = Ajax.post("/saleCart/svc/all/sync/do");
		ajax.data(listPo);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				if (result.data != null) {
					// alert("跳转去结算页面！");
					setPageUrl(getAppUrl("/saleOrder/submit/jsp"));
				} else {
					// 提示
					Layer.warning(result.message || "操作失败");
				}
			} else {
				Layer.warning(result.message || "操作失败");
			}
		});
		ajax.go();
	} else {
		Layer.warning("请至少选中一件服务！");
	}
});
$(function() {
	ajaxCart();
});