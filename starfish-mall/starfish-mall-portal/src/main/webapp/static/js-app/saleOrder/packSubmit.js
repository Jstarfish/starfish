submitOrderUrl="/saleOrder/svc/pack/submit/do";
isHasGoods=false;
isHasSvcs=false;
var svcPackUrl=extractUrlParams(location.href);
var svcPackId=svcPackUrl.id;
function getSaleCart(){
	var hintBox = Layer.progress("加载中...");
	var ajax = Ajax.post("/carSvc/svc/pack/detail/get");
	ajax.data({id:svcPackId});
	ajax.done(function(result, jqXhr) {
		if (result != "" && result.type == "info") {
			if (result.data != null) {
				svcPack=result.data;
				renderHtml(svcPack, "svcPackSvcTpl", "svcPackSvc");
				renderHtml(svcPack, "svcPackTpl", "svcPack");
				//$id("svcPackPrice").text("¥ "+svcPack.amountInfo.amount);
				saleCart.amountInfo.saleAmount=svcPack.amountInfo.amount;
				saleCart.amountInfo.amount=svcPack.amountInfo.amount;
				saleCart.svcAmount=svcPack.amountInfo.amount;
				saleCart.goodsAmount=0;
				saleCart.amountOuter = saleCart.amountInfo.saleAmount;
				payableGoodsAmount = saleCart.goodsAmount;// 应付商品金额
				payableSvcAffixPrice = saleCart.svcAmount;// 应付服务金额
				couponOuterAmount = saleCart.amountOuter;// 减去优惠券的应付金额
				renderSvHtmlByCartSvcList();
				$id("content").show();
				/*var saleCart = {
						"amountInfo" : {
							"saleAmount" : 0,
							"discAmount" : 0,
							"amount" : 0,
							"amountOuter":0,
							"settlePrice" : 0
						}
				};*/
/*				saleCart = result.data;
				saleCart.amountOuter = saleCart.amountInfo.saleAmount;
				payableGoodsAmount = saleCart.goodsAmount;// 应付商品金额
				payableSvcAffixPrice = saleCart.svcAmount;// 应付服务金额
				couponOuterAmount = saleCart.amountOuter;// 减去优惠券的应付金额
				renderSvHtmlByCartSvcList(saleCart);*/
			} else {
				saleCartamount(null);
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
function renderSvHtmlByCartSvcList(){
	saleCartamount(saleCart);
}
