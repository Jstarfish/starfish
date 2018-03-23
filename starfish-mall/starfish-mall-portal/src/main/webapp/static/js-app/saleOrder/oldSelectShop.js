Ajax.baseUrl = __appBaseUrl;// 子窗口需告知app路径,否则ajax 提交无appBaseUrl
//-----------------------缓存数据全局变量--------------------------{{
var dlgArg = getDlgArgForMe();// 从父窗口获取数据
var saleOrderInfo = dlgArg.saleOrderInfo;
var couponSelect = dlgArg.couponSelect;
var ecardShops = dlgArg.ecardShops;
var ecardShopIds=[];
var ecardShopCountMap=KeyMap.newOne();
var saleCart = dlgArg.saleCart;
var cartSvcList = saleCart.saleCartSvcList;// 服务列表
var optGoods = null;// 自选商品
var optGoodsLen = null;// 自选商品List长度
var carSvcGoods = [];// 车辆服务商品
var selectShopDto = {};// 选择的门店
var shopDtoList = [];// 缓存门店列表
var opProductIds = [];// 自选货品IDS
var productLackFlagMap = KeyMap.newOne();// 缓存货品状态
var lackFlagMap = KeyMap.newOne();// 只存缺货货品
var layuiLayerBtn = getHostWindow().$(".layui-layer-btn");// layer dialog窗口的按钮div
//父窗口调用的方法
function getDlgResult() {
	dlgArg.productLackFlagMap = productLackFlagMap;
	dlgArg.hasLackFlag = lackFlagMap.size() > 0;// 是否存在缺货货品
	return dlgArg;
}
//----------------------------------------------------------------}}
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
var j=0;
//----------------------------------------------------------------}}
//-----------------------操作门店和商品服务商品等方法--------------------------{{
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
function bindShopLiEvent(){
	$(document).on("click", ".shops-list li", function() {
		$(this).siblings().removeClass('active').end().addClass('active');
		var shopH1 = $(this).find(">h1");
		var shopId = $(shopH1).attr("shopId");
		// ****************缓存数据**********
		var saleOrderPo = {};//订单po
		selectShopDto = shopDtoList.find(function(elem, i) {
			return elem.id === parseInt(shopId);
		});
		saleOrderPo.shopId = selectShopDto.id;
		saleOrderPo.shopDto = selectShopDto;
		saleOrderInfo.saleOrderPo = saleOrderPo;
		
		// 显示选择的门店下对应商品的货品状态
		getShopProductLack(saleOrderPo.shopId);
		//active
		// ***************显示选择的店铺*************
	/* 	$(layuiLayerBtn).find("#selectShopTip").hide();
		$(layuiLayerBtn).find(".checkbox-analog>em>span").text($(shopH1).text());
		$(layuiLayerBtn).find("#selectShop").show(); */
	}); 
}
/**
* 获取E卡门店列表
* 
* @author 邓华锋
* @date 2015年11月14日 上午11:50:07
* 
*/
function loadEcardShops() {
	///userECard/shop/get
	if(isUndef(ecardShops)){
		ecardShops=[];
	}
	var ajax = Ajax.post("/ecard/userECard/shop/get");
	ajax.sync();
	ajax.done(function(result, jqXhr) {
		ecardShopCountMap = KeyMap.from(result.data);
	});
	ajax.go();
	if (ecardShops.length == 0) {
		// 检查缓存的店铺列表中是否含有用户绑定的E卡店铺，如果有直接获取
		var keys=ecardShopCountMap.keys();
		var j=0;
		for(var i=0,len=keys.length;i<len;i++){
			var ecardShopId=keys[i];
			var ecardShopCount=ecardShopCountMap.get(keys[i]);
			var count=ecardShopCount.count;
			var index = null;
			ecardShopId=ParseInt(ecardShopId);
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
			}else{
				ecardShopIds[j]=ecardShopId;
				j++;
			}
		} 
		// 如果E卡店铺IDS大于0，才去后台获取店铺数据
		if (ecardShopIds.length > 0) {
			var ajax = Ajax.post("/shop/list/get");
			ajax.data({
				pagination : {
					pageSize : 3,
					totalCount : 1,
					pageNumber : 1
				},
				filterItems : {
					ecardShopIds : ecardShopIds
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
							ecardShops[j] = ecardShop;
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
			//先删除存在的
			shopDtoList.remove(ecardShop.id,function(obj,i){
				return obj.id===ecardShop.id;
			});
			shopDtoList.prepend(ecardShop);//然后加入到最前面
		}
	}
}

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
	var ajax = Ajax.post("/shop/list/get");
	pageInfo.pagination.pageNumber = pageNumber;
	pageInfo.filterItems = {};
	ajax.data(pageInfo);
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			var totalCount = result.data.pagination.totalCount;
			var list = result.data.rows;
			if (isNull(list) || list.length == 0) {
				isLastPage = true;
				$(".loadMore>a").html("没有更多门店");
				return;
			}
			if (pageNumber > 1) {
				shopDtoList.append(list);
			} else {
				shopDtoList = list;
			}
			if (isNull(shopDtoList)) {
				isLastPage = true;
				$(".loadMore>a").html("没有更多门店");
				return;
			}
			if (pageNumber == 1) {
				loadEcardShops();
			}
			renderHtml(list, "shopListTpl", "shopsList");
			bindShopLiEvent();
			pageNumber++;
			if (shopDtoList.length < pageSize) {
				isLastPage = true;
				$(".loadMore>a").html("没有更多门店");
			}
			hover("#shopList>li");

		} else {
			Layer.warning(result.message);
		}
	});
	ajax.go();
}

//渲染页面内容
function renderSvHtml(data, fromId, toId) {
	// 获取模板内容
	var tplHtml = $id(fromId).html();

	// 生成/编译模板
	var theTpl = laytpl(tplHtml);

	// 根据模板和数据生成最终内容
	var htmlStr = theTpl.render(data);
	// 使用生成的内容
	$id(toId).html(htmlStr);
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
	if (!isNoB(optGoods.saleCartGoodsList)) {
		var saleCartGoodsList = optGoods.saleCartGoodsList;
		saleCartGoodsList.remove(productId, function(elem, i) {
			var isDelProduct = elem.productId === parseInt(productId);
			if (isDelProduct) {// 是否是要删除的货品
				if (couponSelect && couponSelect.tagerId == elem.productId) {// 去除优惠券
					couponSelect = null;
				}
				var oneGoodsAmount = elem.productAmount.multiply(elem.quantity);// 得到单个货品的总价
				saleCart.goodsAmount = saleCart.goodsAmount.subtract(oneGoodsAmount);// 原来商品价格减去删除的货品总价
				saleCart.amount = saleCart.amount.subtract(oneGoodsAmount);// 原来订单总价减去删除的货品总价
				optGoods.amountInfo.saleAmount = optGoods.amountInfo.saleAmount.subtract(oneGoodsAmount);// 商品小计减去删除的货品总价
				optGoodsLen--;
			}
			return isDelProduct;
		});
		lackFlagMap.remove(parseInt(productId));
		productLackFlagMap.remove(parseInt(productId));
		opProductIds.remove(parseInt(productId));
		if (optGoodsLen == 0) {
			cartSvcList.remove(function(obj, i) {
				return obj.svcId == -1;
			});
			$id("optGoods").remove();
		}
	}
	$id("cgoods" + productId).remove();
}

/**
* 获取商品服务列表
* 
* @author 邓华锋
* @date 2015年11月14日 上午11:41:44
* 
*/
function getSaleCart() {
	if (cartSvcList.length > 0) {
		for (var i = 0, len = cartSvcList.length; i < len; i++) {
			var saleCartSvc = cartSvcList[i];
			if (saleCartSvc.svcId != -1) {
				carSvcGoods.push(saleCartSvc);
			} else {
				optGoods = saleCartSvc;
			}
		}

		if (carSvcGoods.length > 0) {
			renderSvHtml(carSvcGoods, "saleCartRowTpl", "carSvcGoods");
		}

		if (optGoods != null && optGoods != "") {
			optGoodsLen = optGoods.saleCartGoodsList.length;
			if (optGoodsLen > 0) {
				renderSvHtml(optGoods, "optGoodsRowTpl", "optGoods");
			}
		}
	}
}

/**
* 根据选择的门店获取对应货品的缺货状态
* 
* @author 邓华锋
* @date 2015年11月14日 上午11:50:07
* 
* @param shopId
*/
function getShopProductLack(shopId) {
	var ajax = Ajax.post("/shop/product/lack/by/shopId/and/productIds/get");
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
				// KeyMap没有覆盖功能，所有只能先删除，后添加。
				lackFlagMap.remove(productId);
				if (lackFlag) {
					lackFlagMap.add(productId, lackFlag);
					$id("status_" + productId).html("<font color='red'>无货</font><a class='delete disabled' href='javascript:delGoods(\"" + productId + "\")'></a>");
				}
				// KeyMap没有覆盖功能，所有只能先删除，后添加。
				productLackFlagMap.remove(productId);
				productLackFlagMap.add(productId, lackFlag);
			}
		} else {
			Layer.msgError(result.message);
		}
	});
	ajax.go();
}
//----------------------------------------------------------------}}
//-----------------------选择城市相关方法--------------------------{{
//加载省份
function loadProvince() {
	var ajax = Ajax.get("/setting/region/list/get");
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			renderHtml(result.data, "provinceSelectTpl", "provinceSelect");
		} else {
			Layer.warning(result.message);
		}
	});
	ajax.go();
}

//根据页面选择的省加载市
function loadCity(provinceId) {
	var ajax = Ajax.get("/setting/region/list/get");
	ajax.params({
		parentId : provinceId
	});
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			renderHtml(result.data, "citySelectTpl", "citySelect");
		} else {
			Layer.warning(result.message);
		}
	});
	ajax.go();
}

//根据页面选择的市加载区/县
function loadCounty(cityId) {
	var ajax = Ajax.get("/setting/region/list/get");
	ajax.params({
		parentId : cityId
	});
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			renderHtml(result.data, "countySelectTpl", "countySelect");
		} else {
			Layer.warning(result.message);
		}
	});
	ajax.go();
}

function resetCitySelect() {
	$id("citySelect").children(".select-special-l").html("城市");
	$id("citySelect").children("ul").html("");
}

function resetCountySelect() {
	$id("countySelect").children(".select-special-l").html("区/县");
	$id("countySelect").children("ul").html("");
}

//渲染页面内容
function renderHtml(data, fromId, toId) {
	// 获取模板内容
	var tplHtml = $id(fromId).html();

	// 生成/编译模板
	var theTpl = laytpl(tplHtml);

	// 根据模板和数据生成最终内容
	var htmlStr = theTpl.render(data);
	if (isLoadShopData) {
		$id(toId).find("ul").append(htmlStr);
		isLoadShopData = false;
	} else {
		// 使用生成的内容
		$id(toId).find("ul").html(htmlStr);
	}
}

//选择省级区域的响应函数
function provinceSelect(obj) {
	// 设置控件中选中的区域名称
	var text = $(obj).html();
	$(obj).parents("ul").siblings(".select-special-l").html(text);
	// 重置市级和县/区级选择控件的内容
	resetCitySelect();
	resetCountySelect();
	// 刷新市级区域选项
	loadCity(obj.id);
	// 刷新门店列表
	getShopsByRegion(parseInt(obj.id), 1);
}

//选择市级区域的响应函数
function citySelect(obj) {
	// 设置控件中选中的区域名称
	var text = $(obj).html();
	$(obj).parents("ul").siblings(".select-special-l").html(text);
	// 重置县/区级选择控件的内容
	resetCountySelect();
	// 刷新区/县级区域选项
	loadCounty(obj.id);
	// 刷新门店列表
	getShopsByRegion(parseInt(obj.id), 2);
}

//选择区/县级区域的响应函数
function countySelect(obj) {
	// 设置控件中选中的区域名称
	var text = $(obj).html();
	$(obj).parents("ul").siblings(".select-special-l").html(text);
	// 刷新门店列表
	getShopsByRegion(parseInt(obj.id), 3);
}

//根据区域过滤条件获取门店列表
function getShopsByRegion(regionId, level) {
	shopDtoList = [];// 清空缓存数据
	pageNumber = 1;
	var ajax = Ajax.post("/shop/list/get");
	pageInfo.pagination.pageNumber = pageNumber;
	pageInfo.filterItems = {};
	pageInfo.filterItems.regionId = regionId;
	pageInfo.filterItems.level = level;
	ajax.data(pageInfo);
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			var totalCount = result.data.pagination.totalCount;
			shopDtoList=result.data.rows;
			renderHtml(result.data.rows, "shopListTpl", "shopsList");
			bindShopLiEvent();
		} else {
			Layer.warning(result.message);
		}
	});
	ajax.go();
}
//----------------------------------------------------------------}}
//-----------------------页面加载--------------------------{{
$(function() {
	//loadProvince();
	loadShops();
	getSaleCart();
	$(document).on("click", ".loadMore", function() {
		isLoadShopData = true;
		loadShops();
	});
	// 加载地区
	bindRegionLists("province", "city", "county", null);
	var provinceId=null;
	$id("province").change(function(){
		provinceId=$(this).val();
		if($(this).val()==""){
			pageInfo.pagination.pageNumber = 1;
			pageInfo.filterItems = {};
			loadShops();
		}else{
			getShopsByRegion(parseInt($(this).val()), 1);			
		}
	});
	var cityId=null;
	$id("city").change(function(){
		cityId=$(this).val();
		if($(this).val()==""){
			getShopsByRegion(parseInt(provinceId), 1);
		}else{
			getShopsByRegion(parseInt($(this).val()), 2);			
		}
	});
	$id("county").change(function(){
		if($(this).val()==""){
			getShopsByRegion(parseInt(cityId), 2);
		}else{
			getShopsByRegion(parseInt($(this).val()), 3);
		}
	});
	//显示门店选择信息
	/*	$(layuiLayerBtn).prepend($id("shops-selected").html());
		selectShopDto = saleOrderInfo.saleOrderPo.shopDto;
 	if(!isUndef(selectShopDto)&&!isNull(selectShopDto)){
		$(layuiLayerBtn).find("#selectShopTip").hide();
		$(layuiLayerBtn).find(".checkbox-analog>em>span").text(selectShopDto.shop.name + " 地址：" + selectShopDto.shop.regionName);
		$(layuiLayerBtn).find("#selectShop").show();
	} */
	
});