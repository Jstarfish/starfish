Ajax.baseUrl = __appBaseUrl;// 子窗口需告知app路径,否则ajax 提交无appBaseUrl
// -----------------------缓存数据全局变量--------------------------{{
var dlgArg = getDlgArgForMe();// 从父窗口获取数据
var saleCart = dlgArg.saleCart;
var saleOrderInfo = dlgArg.saleOrderInfo;
var ecardShopIds = [];
var isHasGoods=dlgArg.isHasGoods;
var isHasSvcs=dlgArg.isHasSvcs;
//父窗口调用的方法
function getDlgResult() {
	dlgArg.hasLackFlag = lackFlagMap.size() > 0;// 是否存在缺货货品
	return dlgArg;
}
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
//-----------------------页面加载--------------------------{{
var provinceId = "";
$(function() {
	loadShops();
	$(document).on("click", ".loadMore", function() {
		isLoadShopData = true;
		loadShops();
	});
	
	getSaleCart();
	// 加载地区
	bindRegionLists("province", "city", "county", null);
	
	$id("province").change(function() {
		provinceId = $(this).val();
		if ($(this).val() == "") {
			getShopsByRegion("", 1);
		} else {
			getShopsByRegion(parseInt($(this).val()), 1);
		}
	});
	var cityId = null;
	$id("city").change(function() {
		cityId = $(this).val();
		if ($(this).val() == "") {
			getShopsByRegion(parseInt(provinceId), 1);
		} else {
			getShopsByRegion(parseInt($(this).val()), 2);
		}
	});
	$id("county").change(function() {
		if ($(this).val() == "") {
			getShopsByRegion(parseInt(cityId), 2);
		} else {
			getShopsByRegion(parseInt($(this).val()), 3);
		}
	});
});
var optGoodsLen=0;
var optGoods=[];
var cartSvcList=[];
/**
 * 获取商品服务列表
 * 
 * @author 邓华锋
 * @date 2015年11月14日 上午11:41:44
 * 
 */
function getSaleCart() {
	if(isHasSvcs){
		cartSvcList = saleCart.saleCartSvcList;// 服务列表
	}
	var saleCartGoods=[];
	if(isHasGoods){
		saleCartGoods = saleCart.saleCartGoods;// 商品列表
		optGoods=saleCartGoods;
	}
	if (isHasSvcs&&cartSvcList == null && isHasGoods&&saleCartGoods == null) {
		toast("您还未选择商品服务,5秒后前往尖品超市", null, "error", function() {
			openPage(supermarketUrl);
		});
		return;
	}
	var size = 0
	if(isHasSvcs){
		var len=cartSvcList.length;
		svsLen=len;
		 size += len;
		if (len > 0) {
			renderHtml(cartSvcList, "saleCartRowTpl", "carSvcGoods");
		}else {
			$id("carSvcGoods").remove();
		}
	}
	if(isHasGoods){
		var len = saleCartGoods.length;
		optGoodsLen=len;
		size += len;
		if (len > 0) {
			renderHtml(saleCartGoods, "optGoodsRowTpl", "optGoods");
		} else {
			$id("optGoods").remove();
		}
	}
	console.log("isHasSvcs:"+isHasSvcs+" isHasGoods:"+isHasGoods+" size:"+size);
	if ((isHasSvcs||isHasGoods)&&size == 0) {
		toast("您还未选择商品服务,5秒后前往尖品超市", null, "error", function() {
			openPage(supermarketUrl);
		});
	}
	
	//服务套餐页面的门店选择
	if(isHasGoods==false&&isHasSvcs==false){
		$(".shops-select-left").hide();
		$(".shops-select-right").css({left:"0px"});
		//$id("shopsDiv").removeClass("shops-select-right");
		//$id("shopsDiv").addClass("shops-select-right");
		//$(".shops-select-right").style({left: "0px",position: ""});
		//document.write("<style>.shops-select-right{position:none;left: none}</style>");
	}
}


//-----------------------分页相关全局变量--------------------------{{
var pageNumber = 1; // 设置当前页数，全局变量
var pageSize = 4;
var pageInfo = {};
var pagination = {
	pageSize : pageSize,
	totalCount : 1,
	pageNumber : 1
};
var filterItems = {};
pageInfo.pagination = pagination;
pageInfo.filterItems = filterItems;
var isLastPage = false;// 是否是最后一页
var isLoadShopData = false;// 是否是门店加载数据
var j = 0;
// ----------------------------------------------------------------}}
var selectShopDto = {};// 选择的门店
var selectShopDto=null;
/**
 * 获取全部门店列表 *
 * 
 * @author 邓华锋
 * @date 2015年11月14日 上午11:50:07
 * 
 */
function loadShops() {
	if (isLastPage) {
		return;
	}
	console.log("pagination.pageNumber："+pageInfo.pagination.pageNumber);
	var ajax = Ajax.post("/shop/list/get");
	ajax.data(pageInfo);
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			pagination=result.data.pagination;
			selectShopDto=dlgArg.selectShopDto;
			var list = result.data.rows;
			//var pageNumber=pagination.pageNumber;
			if (pageNumber > 1) {
				//删除重复的E卡商店
				for (var i = 0, len = ecardShops.length; i < len; i++) {
					var ecardShop = ecardShops[i];
					// 先删除存在的
					list.remove(ecardShop.id, function(obj, i) {
						return obj.id === ecardShop.id;
					});
				}
				shopDtoList.append(list);
			} else {
				shopDtoList = list;
			}
			if (pageNumber == 1) {
				loadEcardShops();
			}
			renderHtml(shopDtoList, "shopListTpl", "shopsList");
			var y=pagination.totalCount-pagination.pageSize*pagination.pageNumber;//计算剩余记录数
			if(y<=0){
				isLastPage = true;
				$(".loadMore>a").html("没有更多门店");
			}
			bindShopLiEvent();
			pageNumber++;
			pageInfo.pagination.pageNumber=pageNumber;
			hover("#shopList>li");
		} else {
			Layer.warning(result.message);
		}
	});
	ajax.go();
}
function bindShopLiEvent() {
	$(document).on("click", ".shops-list li", function() {
		$(this).siblings().removeClass('active').end().addClass('active');
		var shopH1 = $(this).find(">h1");
		var shopId = $(shopH1).attr("shopId");
		// ****************缓存数据**********
		var saleOrderPo = {};// 订单po
		selectShopDto = shopDtoList.find(function(elem, i) {
			return elem.id === parseInt(shopId);
		});
		saleOrderPo.shopId = selectShopDto.id;
		saleOrderPo.shopDto = selectShopDto;
		saleOrderInfo.saleOrderPo = saleOrderPo;

		// 显示选择的门店下对应商品的货品状态
		getShopProductLack(saleOrderPo.shopId);
		getShopSvcStatus(saleOrderPo.shopId);
		// active
		// ***************显示选择的店铺*************
		/*
		 * $(layuiLayerBtn).find("#selectShopTip").hide(); $(layuiLayerBtn).find(".checkbox-analog>em>span").text($(shopH1).text()); $(layuiLayerBtn).find("#selectShop").show();
		 */
	});
}
function renderEcardShops() {
	// 获取模板内容
	var tplHtml = $id("shopListTpl").html();
	// 生成/编译模板
	var theTpl = laytpl(tplHtml);
	// 根据模板和数据生成最终内容
	var htmlStr = theTpl.render(ecardShops);
	$id("shopsList").find("ul").prepend(htmlStr);
	hover("#shopList>li");
}
var ecardShopCountMap=null;
var ecardShops = dlgArg.ecardShops;//缓存e卡店铺，如果不缓存，请设为null
/**
 * 获取E卡门店列表
 * 
 * @author 邓华锋
 * @date 2015年11月14日 上午11:50:07
 * 
 */
function loadEcardShops() {
	if (isUndef(ecardShops)||isNull(ecardShops)) {
		ecardShops = [];
	}
	if (ecardShops.length == 0) {
		
		var ajax = Ajax.post("/ecard/userECard/shop/get");
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			ecardShopCountMap = KeyMap.from(result.data);
		});
		ajax.go();
		var keys = ecardShopCountMap.keys();
		var j = 0;
		// 检查缓存的店铺列表中是否含有用户绑定的E卡店铺，如果有直接获取
		for (var i = 0, len = keys.length; i < len; i++) {
			var ecardShopId = keys[i];
			var ecardShopCount = ecardShopCountMap.get(keys[i]);
			var count = ecardShopCount.count;
			var index = null;
			ecardShopId = ParseInt(ecardShopId);
			var shopDto = shopDtoList.find(function(obj, i) {// 查找
				var isFind = obj.shop.id === ecardShopId;
				if (isFind) {
					index = i;
				}
				return isFind;
			});
			if (shopDto != null) {
				shopDtoList.removeAt(index);// 先移除
				shopDto.isECard = true;// 添加是否是E卡店铺
				shopDtoList.prepend(shopDto);// 加入到最前面
				ecardShops.add(shopDto);
			} else {
				ecardShopIds[j] = ecardShopId;
				j++;
			}
		}
		
		// 如果缓存中没有E卡店铺，既ecardShopIds大于0，才去后台获取e卡店铺数据
		if (ecardShopIds.length > 0) {
			var ajax = Ajax.post("/shop/list/get");
			ajax.sync();
			ajax.data({
				pagination : {
					pageSize : ecardShopIds.length,
					pageNumber : 1
				},
				filterItems : {
					shopIds : ecardShopIds
				}
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var totalCount = result.data.pagination.totalCount;
					var list = result.data.rows;
					if (isNull(list) || list.length == 0) {
						return;
					}
					if (ecardShops.length == 0) {
						ecardShops = list;
						for (var i = 0, len = ecardShops.length; i < len; i++) {
							var ecardShop = ecardShops[i];
							ecardShop.isECard = true;// 添加是否是E卡店铺
							shopDtoList.prepend(ecardShop);// 加入到最前面
							ecardShopIds.remove(ecardShop.id);// 移除
						}
					} else {
						for (var i = 0, len = list.length; i < len; i++) {
							var ecardShop = list[i];
							ecardShop.isECard = true;// 添加是否是E卡店铺
							shopDtoList.prepend(ecardShop);// 加入到最前面
							ecardShops.add(ecardShop);//添加e卡店铺
							j++;
							ecardShopIds.remove(ecardShop.id);// 移除
						}
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
	} else {
		for (var i = 0, len = ecardShops.length; i < len; i++) {
			var ecardShop = ecardShops[i];
			// 先删除存在的
			var isDelete=shopDtoList.remove(ecardShop.id, function(obj, i) {
				return obj.id === ecardShop.id;
			});
			if(isDelete||(provinceId==""&&pageInfo.pagination.pageNumber == 1)){
				shopDtoList.prepend(ecardShop);// 然后加入到最前面
			}
		}
	}
}

//根据区域过滤条件获取门店列表
function getShopsByRegion(regionId, level) {
	shopDtoList = [];// 清空缓存数据
	pageNumber = 1;
	pageInfo.pagination.pageNumber = 1;
	isLastPage=false;
	if(level==1&&regionId==""){
		pageInfo.filterItems ={};
	}else{
		pageInfo.filterItems = {regionId:regionId,level:level};
	}
	loadShops();
}
var lackFlagMap = KeyMap.newOne();// 只存缺货货品
var opProductIds = [];// 自选货品IDS
var opProductIds = [];// 自选货品IDS
/**
 * 根据选择的门店获取对应货品的缺货状态
 * 
 * @author 邓华锋
 * @date 2015年11月14日 上午11:50:07
 * 
 * @param shopId
 */
function getShopProductLack(shopId) {
	var ajax = Ajax.post("/shop/product/lack/get");
	ajax.data({
		shopId : parseInt(shopId),
		productIds : opProductIds
	});
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			if (result.data == null || result.data.length == 0) {
				return;
			}
			var map = KeyMap.from(result.data);
			for (var i = 0, len = opProductIds.length; i < len; i++) {// 遍历显示产品状态
				var productId = opProductIds[i];
				var lackId="goods"+productId;
				var lackFlag = true;
				if (map) {
					var shopProduct = map.get(productId);
					if (shopProduct) {
						lackFlag = shopProduct.lackFlag;
						if (lackFlag == false) {
							$id("status_" + productId).html("<font color='green'>有货</font>");
						}
					}
				}
				lackFlagMap.remove(lackId);
				if (lackFlag) {
					lackFlagMap.add(lackId, lackFlag);
					$id("status_" + productId).html("<font color='red'>无货</font><a class='delete disabled' href='javascript:delGoods(\"" + productId + "\")'></a>");
				}
			}
		} else {
			Layer.msgError(result.message);
		}
	});
	ajax.go();
}
var svcIds=[];
function getShopSvcStatus(shopId){
	var ajax = Ajax.post("/shop/svc/status/get");
	ajax.data({
		shopId : parseInt(shopId),
		svcIds : svcIds
	});
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			if (result.data == null || result.data.length == 0) {
				return;
			}
			var map = KeyMap.from(result.data);
			var mapLen=0;
			if(map){
				mapLen=map.keys().length;
			}
			for (var i = 0, len = svcIds.length; i < len; i++) {// 遍历显示产品状态
				var svcId = svcIds[i];
				var lackFlag = true;
				var lackId="svc"+svcId;
				if (mapLen>0) {
					var shopSvc = map.get(svcId);
					if (shopSvc) {
						lackFlag=false;
						$id("svcStatus_" + svcId).html("<font color='green'>正常</font>");
					}
				}
				lackFlagMap.remove(lackId);
				if (lackFlag) {
					lackFlagMap.add(lackId, lackFlag);
					$id("svcStatus_" + svcId).html("<font color='red'>不提供</font><a class='delete disabled' href='javascript:delSvcs(\"" + svcId + "\")'></a>");
				}
			}
		} else {
			Layer.msgError(result.message);
		}
	});
	ajax.go();
}

/**
 * 删除单个商品
 * 
 * @author 邓华锋
 * @date 2015年11月14日 上午11:39:05
 * 
 * @param productId
 */
function delGoods(productId) {
	if (!isNoB(optGoods)) {
		optGoods.remove(productId, function(ts,vItem, i) {
			var isDelProduct = ts.productId === parseInt(vItem);
			if (isDelProduct) {// 是否是要删除的货品
				var oneGoodsAmount = ts.productAmount.multiply(ts.quantity);// 得到单个货品的总价
				saleCart.goodsAmount = saleCart.goodsAmount.subtract(oneGoodsAmount);// 原来商品价格减去删除的货品总价
				saleCart.amountInfo.amount = saleCart.amountInfo.amount.subtract(oneGoodsAmount);// 原来订单总价减去删除的货品总价
				optGoodsLen--;
			}
			return isDelProduct;
		});
		lackFlagMap.remove("goods"+productId);
		opProductIds.remove(parseInt(productId));
		if (optGoodsLen == 0) {
			$id("optGoods").remove();
		}
	}
	$id("cgoods" + productId).remove();
}

var svsLen=0;
/**
 * 删除单个服务
 * 
 * @author 邓华锋
 * @date 2015年11月14日 上午11:39:05
 * 
 * @param svcId
 */
function delSvcs(svcId) {
	if (!isNoB(cartSvcList)) {
		cartSvcList.remove(svcId, function(ts,vItem, i) {
			var isDelProduct = ts.svcId === parseInt(vItem);
			if (isDelProduct) {// 是否是要删除的货品
				var oneSvcAmount = ts.salePrice;// 得到单个服务的总价
				saleCart.oldSvcAmount = saleCart.oldSvcAmount.subtract(oneSvcAmount);
				if(ts.amountInfo.discAmount>0){
					oneSvcAmount=ts.amountInfo.amount;
					saleCart.amountInfo.discAmount=saleCart.amountInfo.discAmount.subtract(ts.amountInfo.discAmount);
				}
				saleCart.svcAmount = saleCart.svcAmount.subtract(oneSvcAmount);// 原来商品价格减去删除的货品总价
				saleCart.amountInfo.amount = saleCart.amountInfo.amount.subtract(oneSvcAmount);// 原来订单总价减去删除的货品总价
				svsLen--;
			}
			return isDelProduct;
		});
		lackFlagMap.remove("svc"+svcId);
		svcIds.remove(ParseInt(svcId));
		if (svsLen == 0) {
			$id("carSvcGoods").remove();
		}
	}
	$id("svc" + svcId).remove();
}