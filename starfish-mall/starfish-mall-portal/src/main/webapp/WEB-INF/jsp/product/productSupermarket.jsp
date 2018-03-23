<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/base.jsf" %>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<div class="content">
    <div class="page-width">
        <div class="crumbs"><a href="<%=appBaseUrl%>/">首页</a>&gt;<a href="javascript:;">尖品超市</a></div>
        <div class="section clearfix" style="overflow: inherit;">
            <div class="section-left1">
                <ul id="goodsCatTree"></ul>
                <div class="block" style="display:none;">
                    <h1>浏览过的商品</h1>
                    <ul class="goods-viewed">
                        <li>
                            <a href=""><img src="<%=resBaseUrl%>/image-app/temp/goods1.png" alt=""/></a>
                            <a class="text" href="" title="佳通轮胎 Van600 155R13C 90/88S LT 8PR Giti 超强承载，更长里程佳通轮胎 Van600 155R13C 90/88S LT 8PR Giti 超强承载，更长里程">佳通轮胎 Van600 155R13C 90/88S LT 8PR Giti <span class="red1">超强承载，更长里程佳通轮胎</span> Van600 155R13C 90/88S LT 8PR Giti 超强承载，更长里程</a>
                            <div class="price">
                                <em class="price-sale"><i>￥</i>699</em><span class="gray">已有367人购买</span>
                            </div>
                        </li>
                    </ul>
                </div>
                <div class="adv" style="display:none;">
                    <img src="<%=resBaseUrl%>/image-app/temp/adv01.jpg" alt=""/>
                </div>
            </div>
            <div class="section-right1">
                <div class="mod-main1">
                    <dl class="brand-select">
                        <dt>品牌：</dt>
                        <dd id="brandBar"></dd>
                    </dl>
                </div>
                <div class="goods-content">
                    <div class="goods-title">
                        <ul id="theSorter"></ul>
                        <!--分页-->
                        <div id="headerGqPaginator" class="fr pager"> </div>
                        <div class="num">共<span id="totalCount">0</span>个商品</div>
                    </div>
                    <ul id="productList" class="goods-list clearfix"></ul>
                    <div id="noRecordDiv" style="display: none;" class="no-result">
                        抱歉，暂无查询结果。您可以直接联系热线122-22333客服直接帮您匹配所需产品。
                    </div>
                    <div class="pager-gap">
                        <div id="footerGqPaginator" class="fr pager">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
//----------------------------------------------全局变量----------------------------------------------------
var urlParams = extractUrlParams();
var defaultCatId = urlParams["catId"];
//
var footerGqPaginatorCtrl = null;
var headerGqPaginatorCtrl = null;
//是否渲染标识
var footerPagerRendered = false;
var headerPagerRendered = false;
var sorterRendered = false;
//排序数据模型
var sortModel = [{
	code : "salePrice",
	text : "价格",
	orders : "asc,desc",
	active : true
}/* , {
	code : "eval",
	text : "评论数",
	orders : "desc"
} */];
//----------------------------------------------onload----------------------------------------------------
//把产品加入购物车
function addProductToCart(srcDom, productId, quantity, successCallback) {
	if(quantity > 0){
    	var jqSrc = $(srcDom);
    	rightToolBar.transToButtonFrom("cart", jqSrc, function() {
    		//" "只显示红圈, "" 不显示红圈
    		rightToolBar.setButtonHint("cart", " ");
    	});
	}
	//调用购物车同步接口...
	var ajax = Ajax.post("/product/add/cart/do");
	ajax.data({productId:productId, quantity:quantity});
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			if(typeof successCallback == "function"){
				successCallback();
			}
			//Layer.msgSuccess("亲，添加成功啦~");
		} else {
			Layer.msgWarning(result.message);
		}
	});
	ajax.go();
}

//加入购物车成功后回调函数
function addCartSuccessCallBack(){
	getCartItemCount(function(count){
		$id("saleCartCount").html(count);
	});
}

$(function() {
	//右边栏
    renderRightToolbar(true);
	//绑定添加产品至购物车事件
	$id("productList").on("click", ">li input[data-role='addToCartBtn']" , function(){
		var jqBtn = $(this);
		var jqLi = jqBtn.parents("li").first();
		var spinner = jqLi.find("[name='numSpinner']").first();
		//
		var productId = parseInt($(spinner).attr("data-id"));
		var quantity = parseInt($(spinner).attr("data-val"));
		//
		addProductToCart(jqLi, productId, quantity, addCartSuccessCallBack);
	});
	//初始化头部分页插件
	initHeaderGqPaginator("headerGqPaginator");
	//初始化尾部分页插件
	initFooterGqPaginator("footerGqPaginator");
	//渲染排序项
	renderSorter("theSorter", sortModel, doOnSortClick);
	//初始化商品分类
	if(defaultCatId){
		//有默认分类
		initCatsData(defaultCatId);
	}else{
		//无默认分类
		initCatsData();
	}
	//绑定滑过产品时响应事件
	bindHoverEvent("productList", ">li");
	//
	bindOpenProductDetailPage("productList", ">li");
});

//----------------------------------------------业务处理----------------------------------------------------

//
function bindOpenProductDetailPage(listener, targetPath){
	var jqListener = $id(listener);
	targetPath = targetPath || ">li";
	jqListener.on("click", targetPath, function(event) {
		var liObj = $(this);
		//
		var jqToBeExcluded = $(liObj).find("div.add-cat");
		if ($.contains(jqToBeExcluded.get(0), event.target)) {
			return false;
		}
		//
		var productId = $(liObj).attr("data-id");
		var url = getAppUrl("/product/detail/jsp?productId=")+ParseInt(productId);
		window.open(url, "_blank");
	});
}

//排序事件 code:排序字段 order:desc or asc
function doOnSortClick(code, order) {
	//页面首次加载时不执行排序事件
	if(!sorterRendered){
		sorterRendered = true;
		return;
	}
	console.log(code + " --->: " + order);
	qry_sortItems = {};  //清空排序项
	qry_sortItems[code] = order;
	console.log("sortItems:"+JSON.encode(qry_sortItems));
	sortAndQuery();
}
//刷新分页插件
function refreshGqPaginator(){
	//同步GqPaginator数据模型
	qry_pagination.currentPage = qry_pagination.pageNumber;
	//console.log(JSON.encode(qry_pagination));
	headerGqPaginatorCtrl.jqPaginator('option', qry_pagination);
	//
	footerGqPaginatorCtrl.jqPaginator('option', qry_pagination);
}
//格式化商品分类数据，形成树形结构
function formCatTreeData(cats){
	var treeData = [];
	//筛选2级节点
	for(var i=cats.length-1; i>=0; i--){
		var pNode = {};
		var cat = cats[i];
		var level = cat.level;
		if(level == 2){
			pNode.id = cat.id;
			pNode.name = cat.name;
			pNode.level = level;
			treeData.add(pNode);
			cats.removeAt(i);
		}
	}
	//筛选3级节点，绑定在相应2级节点下
	if(treeData.length > 0){
		$(treeData).each(function(){
			if(cats.length == 0) return false;
			var pId = this.id;
			var childNodes = [];  //子节点集合
			for(var j=cats.length-1; j>=0; j--){
				var node = {};
				var _cat = cats[j];
				var _level = _cat.level;
				var _pId = _cat.parentId;
				if(_level == 3 && _pId == pId){
					node.id = _cat.id;
					node.name = _cat.name;
					node.level = _level;
					node.pId = pId;
					childNodes.add(node);
					cats.removeAt(i);
				}
				this.childNodes = childNodes;
			}
		});
	}
	//console.log(JSON.encode(treeData));
	return treeData;
}
//绑定选择品牌事件
function bindSelectBrandEvent(domId, filterCode){
	$id(domId).find("a").on("click", function() {
		$(this).siblings("a").removeClass("active");  //移除未选中项中的active样式
		$(this).addClass("active");  //设置当前对象为active状态
		var brandId = $(this).attr("data-id");
		if(brandId){
			merge(qry_filterItems, {brandId: ParseInt(brandId)});
		}else{
			delete qry_filterItems.brandId;  //清空filterItems中的品牌项
		}
		filterAndQuery();
	});
}

//渲染品牌条区域
function renderBrandBar(brands){
	//获取模板内容
	var tplHtml = $id("brandBarTpl").html();
	//生成/编译模板
	var htmlTpl = laytpl(tplHtml);
	//根据模板和数据生成最终内容
	var htmlText = htmlTpl.render(brands);
	//使用生成的内容
	$id("brandBar").html(htmlText);
	//绑定选择品牌过滤事件
	bindSelectBrandEvent("brandBar", "brand");
}
//刷新品牌条
function refreshBrandBar(catId){
	var ajax = Ajax.post("/product/brand/list/by/catId/get");
	var postData = {};
	if(catId) postData = {catId : catId};
	ajax.data(postData);
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			var brands = result.data;
			if(brands) renderBrandBar(brands);  //渲染品牌条
		} else {
			Layer.msgWarning(result.message);
		}
	});
	ajax.go();
}
//去刷新列表页右侧主题内容
function goRefreshSectionRight(catId){
	if(catId){
		//刷新商品分类相关的品牌区域
		refreshBrandBar(catId);
		//刷新产品列表区域
		qry_filterItems = {catId : catId};
		filterAndQuery();
	}else{
		//刷新品牌区域
		refreshBrandBar();
		qry_filterItems = {};
		filterAndQuery();
	}
}
//渲染商品分类菜单
function renderCatTree(treeData, catId){
	var menuDataX = [
               {
                   name: "尖品超市",
                   childNodes: treeData
               }
           ];
	if(catId){
		//绑定分类菜单事件,并渲染已选择的分类
		renderMenuTree("goodsCatTree", menuDataX, goRefreshSectionRight, catId);
	}else{
		//绑定分类菜单事件
		renderMenuTree("goodsCatTree", menuDataX, goRefreshSectionRight);
		goRefreshSectionRight();
	}
	//默认展开商品分类
	var firstLi = $id("goodsCatTree").find("li").first();
	if(firstLi){
		if(!$(firstLi).hasClass("active")){
			$(firstLi).find(">a").trigger("click");
		} 
	}
}
//购物车中产品数量变化事件
function doOnNumChange(numValue, htDom) {
    var dataId = $(htDom).attr("data-id");
    $(htDom).attr("data-val", numValue);
    console.log(dataId + " : " + numValue);
}

//渲染货品列表数据
function renderProductData(dataList){
	//获取模板内容
	var tplHtml = $id("productTpl").html();
	//生成/编译模板
	var htmlTpl = laytpl(tplHtml);
	//根据模板和数据生成最终内容
	var htmlText = htmlTpl.render(dataList);
	//使用生成的内容
	$id("productList").html(htmlText);
	//绑定购物-增加减少
    renderNumSpinner("numSpinner", doOnNumChange, 1, 1, 1000);
}
//从productList中，获取productId的购买总数量
function getBuySumByPoroductId(productList, productId){
	if(isNoB(productId)) return null;
	//
	for(var i=0; i<productList.length; i++){
		var product = productList[i];
		var pId = product.id;  //产品id
		if(pId == productId){
			return product.buySum;
		}
	}
	return null;
}

//渲染购买产品总数量
function renderProductListBuySum(products){
	if(products && products.length == 0) return;
	//
	var productList = $id("productList").find("li"); //页面展示的产品列表
	for(var i=0; i<productList.length; i++){
		var product = productList[i];
		var pId = $(product).attr("data-id");  //产品id
		var buySum = getBuySumByPoroductId(products, pId);
		if(buySum){
			var buysumSpan = $(product).find("div.price>span.buysum");
			$(buysumSpan).html(buySum+"人购买");
		}
	}
	
}

function fetchBuyProductSum(productIds){
	if(!productIds || productIds.length == 0) return null;
	//
	var ajax = Ajax.post("/product/products/buyNum/summary/get");
	ajax.data({productIds : productIds});
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			var products = result.data;
			renderProductListBuySum(products);  //渲染购买产品总数量
		}
	});
	ajax.go();
}

//从productList中，获取productId的销售价格和市场价格
function getPricesByPoroductId(productList, productId){
	if(isNoB(productId)) return null;
	//
	for(var i=0; i<productList.length; i++){
		var product = productList[i];
		var pId = product.id;  //产品id
		if(pId == productId){
			var dataMap = {};
			dataMap.salePrice = product.salePrice;
			dataMap.marketPrice = product.marketPrice;
			return dataMap;
		}
	}
	return null;
}

function renderProductListPrices(products){
	if(products && products.length == 0) return;
	//
	var productList = $id("productList").find("li"); //页面展示的产品列表
	for(var i=0; i<productList.length; i++){
		var product = productList[i];
		var pId = $(product).attr("data-id");  //产品id
		var pricesMap = getPricesByPoroductId(products, pId);
		if(pricesMap){
			var salePrice = pricesMap.salePrice;
			var marketPrice = pricesMap.marketPrice;
			var priceDiv = $(product).find("div.price");
			var salePriceSpan = $(priceDiv).find(".price-sale");
			$(salePriceSpan).html("<i>￥</i>"+salePrice);
			if(marketPrice && marketPrice > salePrice){
				var marketPriceSpan = $(priceDiv).find(".price-market");
				$(marketPriceSpan).html("<i>￥</i>"+marketPrice);
			}
		}
	}
}

function fetchProductPrices(productIds){
	if(!productIds || productIds.length == 0) return null;
	//
	var ajax = Ajax.post("/product/products/prices/get");
	ajax.data({productIds : productIds});
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			var products = result.data;
			renderProductListPrices(products);  //渲染产品销售价格和市场价格
		}
	});
	ajax.go();
}

function getIdsFromProductList(productList){
	if(!productList) return null;
	var ids = [];
	for(var i=0; i<productList.length; i++){
		var product = productList[i];
		var id = product.id;
		if(id) ids.add(id);
	}
	return ids;
}

function doTheQuery() {
	//	
	var ajax = Ajax.post("/product/list/by/filter/get");
	
	var postData = newQryFilterInfo();
	ajax.data(postData);

	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			var paginatedList = result.data;
			var pagination = paginatedList.pagination;
			$id("totalCount").html(pagination.totalCount);
			//
			var products = paginatedList.rows;
			if(products && products.length > 0){
				$id("theSorter").css("display", "block");
				$id("noRecordDiv").css("display", "none");
				renderProductData(products);
				var ids = getIdsFromProductList(products);
				//获取购买产品数量统计数据
				fetchBuyProductSum(ids);
				//获取产品销售价格和市场价格
				fetchProductPrices(ids);
			}else{
				$id("theSorter").css("display", "none");
				$id("noRecordDiv").css("display", "block");
				$id("productList").html("");
			}
			//利用返回的分页信息
			var refPagination = pagination;
			resetPagination(refPagination);
			//根据新的分页信息（refPagination）刷新jqPaginator控件
			refreshGqPaginator();
		} else {
			Layer.msgWarning(result.message);
		}

	});
	//
	ajax.go();
}

//----------------------------------------------初始化----------------------------------------------------
//初始化头部分页控件
function initHeaderGqPaginator(domId) {
	headerGqPaginatorCtrl = $id(domId).jqPaginator({
		totalCount : 1,
		pageSize : qry_pageSize,
		visiblePages: 0,
		prev : '<a class="prev" href="javascript:void(0);">上一页<\/a>',
		next : '<a class="next"  href="javascript:void(0);">下一页<\/a>',
		page : '<a class="active" href="javascript:void(0);">{{page}}<\/a>',
		onPageChange : function(pageNumber) {
			//首次加载不响应事件
			if(!headerPagerRendered){
				headerPagerRendered = true;
				return;
			}
			pageNoAndQuery(pageNumber);
		}
	});
}
//初始化尾部分页控件
function initFooterGqPaginator(domId) {
	footerGqPaginatorCtrl = $id(domId).jqPaginator({
		totalCount : 0,
		pageSize : qry_pageSize,
		prev : '<a class="prev" href="javascript:void(0);">上一页<\/a>',
		next : '<a class="next"  href="javascript:void(0);">下一页<\/a>',
		page : '<a class="active" href="javascript:void(0);">{{page}}<\/a>',
		onPageChange : function(pageNumber) {
			//首次加载不响应事件
			if(!footerPagerRendered){
				footerPagerRendered = true;
				return;
			}
			//
			pageNoAndQuery(pageNumber);
		}
	});
}

//--------------------------------------------------初始化------------------------------------------------
//分页大小（页面级）
var qry_pageSize = 20;
//[搜索用]的关键字符串
var qry_keywords = null;
//过滤条件项
var qry_filterItems = {};
//排序项
var qry_sortItems = {salePrice : "asc"};
//分页信息
var qry_pagination = {
	totalCount : 0,
	pageSize : qry_pageSize,
	pageNumber : 1
};

var params=extractUrlParams(location.href);
qry_keywords = decodeURIComponent(params.keySearch);
if(qry_keywords == "undefined"){
	qry_keywords = "";
}
$("#keySearch").val(qry_keywords);

//根据 新的的分页信息（或新的页码） 更新 本地分页信息
function resetPagination(refPagination) {
	refPagination = refPagination || {};
	if ( typeof refPagination == "number") {
		refPagination = {
			pageNumber : refPagination
		};
	}
	merge(qry_pagination, refPagination);
}

//根据最新的信息生成新的查询条件
function newQryFilterInfo() {
	var retData = {
			keywords : qry_keywords || null,
			filterItems : qry_filterItems || {},
			sortItems : qry_sortItems || {},
			pagination : qry_pagination
		};
		if (retData.pagination) {
			retData.pagination.totalCount = 0;
		}
		return retData;
}

//关键字 等
function keywordsAndQuery() {
	//重建 qry_keywords
	//qry_keywords = ...;
	//
	//重置页码
	resetPagination(1);
	//
	doTheQuery();
}

//查询 按钮等
function filterAndQuery() {
	//重建 qry_filterItems
	//qry_filterItems = {...};
	//或 修改 qry_filterItems
	//merge(qry_filterItems, {...});
	//
	//重置页码
	resetPagination(1);
	//
	doTheQuery();
}

//排序 按钮等
function sortAndQuery() {
	//重建 qry_sortItems
	//qry_sortItems = {...};
	//或 修改 qry_sortItems
	//merge(qry_sortItems, {...});
	//
	//重置页码
	resetPagination(1);
	//
	doTheQuery();
}

//排序 按钮等
function pageNoAndQuery(pageNo) {
	//变更页码
	resetPagination(pageNo);
	//
	doTheQuery();
}

//初始化商品分类
function initCatsData(catId) {
	var ajax = Ajax.post("/product/goods/categ/list/get");
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			var cats = result.data;
			var catTreeData = formCatTreeData(cats);
			//console.log(JSON.encode(catTreeData));
			renderCatTree(catTreeData, catId);
		} else {
			Layer.msgWarning(result.message);
		}
	});

	ajax.go();
}

</script>
</body>
<!----------------------------------------------------------信息模板------------------------------------------------------------->
<script id="productTpl" type="text/html" title="产品信息模板">
{{# for(var i=0, len=d.length; i<len ; i++) {  }}
{{# 	var product = d[i], productAlbumImgs = product.productAlbumImgs;  }}
<li data-id="{{ product.id }}">
    <div class="goods-list-item-cont">
	    <a data-role="linkDetail" href="javascript:void(0);">
		{{# if(productAlbumImgs && productAlbumImgs.length > 0){ }}
		<img src="{{ productAlbumImgs[0].fileBrowseUrl }}" alt=""/>
		{{# }else{ }}
		<img src="image-app/temp/goods1.png" alt=""/>
		{{# } }}
	    </a>
 		<div class="price">
	        <em class="price-sale" style = "float: left"></em>
			<em class="price-market"></em>
	    	<span class="gray buysum"></span>
			{{# var prmtTagGoods = product.prmtTagGoods }}
			{{# if(prmtTagGoods && prmtTagGoods.length > 0){ }}
 			{{# var prmtTag = prmtTagGoods[0].prmtTag }}
 			<em class="prmtTag" style = "float: right">{{ prmtTag.name }}</em>
	    	{{# }else{ }}
			{{# } }}
	    </div>
	    <a data-role="linkDetail" class="text" href="javascript:void(0);" title="{{ product.title ? product.title : product.goodsName }}">
		{{ product.title ? product.title : product.goodsName}}
	    </a>
	    <div class="add-cat">
	        <div data-id="{{ product.id }}" data-val="1" name="numSpinner" class="num-spinner"></div>
	        <input class="btn btn-w100h24 fr" type="button" data-role="addToCartBtn" value="加入购物车" />
	    </div>
    </div>
</li>
{{# } }}
</script>

<script id="brandBarTpl" type="text/html" title="品牌条信息模板">
<a data-id="" class="active" href="javascript:void(0);">不限</a>
{{# var brands = d; }}
{{# for(var i=0, len=brands.length; i<len ; i++) {  }}
{{# 	var brand = brands[i];  }}
		<a data-id="{{brand.id}}" href="javascript:void(0);">{{brand.name ? brand.name : ""}}</a>
{{# } }}
</script>
</html>