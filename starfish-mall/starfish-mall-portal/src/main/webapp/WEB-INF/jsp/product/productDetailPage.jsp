<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/base.jsf" %>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<div class="content">
    <div class="page-width">
        <div class="crumbs"><a href="<%=appBaseUrl%>/">首页</a>&gt;<a href="<%=appBaseUrl%>/product/supermarket/list/jsp">尖品超市</a>&gt;<a href="javascript:;">商品详情</a></div>
        <div class="goods-intr clearfix">
            <!--弹出-start-->
            <div id="viewer" class="viewer">
                <div class="hd">
                    <h1 id="viewer_title"></h1>
                    <ul>
                        <li><img alt="" src="<%=resBaseUrl%>/image-app/temp/goods2.jpg"></li>
                        <li><img alt="" src="<%=resBaseUrl%>/image-app/temp/goods1.png"></li>
                        <li><img alt="" src="<%=resBaseUrl%>/image-app/temp/goods2.jpg"></li>
                        <li><img alt="" src="<%=resBaseUrl%>/image-app/temp/goods1.png"></li>
                        <li><img alt="" src="<%=resBaseUrl%>/image-app/temp/goods2.jpg"></li>
                        <li><img alt="" src="<%=resBaseUrl%>/image-app/temp/goods1.png"></li>
                        <li><img alt="" src="<%=resBaseUrl%>/image-app/temp/goods2.jpg"></li>
                        <li><img alt="" src="<%=resBaseUrl%>/image-app/temp/goods1.png"></li>
                    </ul>
                    <div class="hd-sweep">
                        <input class="btn btn-w108" type="button" value="立即购买"/>
                    </div>
                </div>
                <div class="bd">
                    <ul>
                        <li><img alt="" src="<%=resBaseUrl%>/image-app/temp/goods2.jpg"></li>
                        <li><img alt="" src="<%=resBaseUrl%>/image-app/temp/goods1.png"></li>
                        <li><img alt="" src="<%=resBaseUrl%>/image-app/temp/goods2.jpg"></li>
                        <li><img alt="" src="<%=resBaseUrl%>/image-app/temp/goods1.png"></li>
                        <li><img alt="" src="<%=resBaseUrl%>/image-app/temp/goods2.jpg"></li>
                        <li><img alt="" src="<%=resBaseUrl%>/image-app/temp/goods1.png"></li>
                        <li><img alt="" src="<%=resBaseUrl%>/image-app/temp/goods2.jpg"></li>
                        <li><img alt="" src="<%=resBaseUrl%>/image-app/temp/goods1.png"></li>
                    </ul>
                </div>
                <!-- 下面是前/后按钮代码，如果不需要删除即可 -->
                <a class="prev" href="javascript:void(0)"></a>
                <a class="next" href="javascript:void(0)"></a>
            </div>
            <!--弹出-end-->
            <!--产品-图片展示-start-->
            <div class="goods-preview">
            	<div id="imagePreviewer"></div>
                <div id="imageViewer"></div>
                <div class="share">
					<span>分享：</span>
					<!-- <a class="a1" href="javascript:void(0);"></a> -->
					<a class="a2 share_qzone" title="qq空间" href="javascript:void(0);"></a>
					<a class="a3 share_tsina" title="新浪微博" href="javascript:void(0);"></a>
					<a class="a4 share_weixin" title="微信" onclick="openWeiXinDialog()" style="display:none;" href="javascript:void(0);"></a>
				</div>
	            <div id="share-wx" style="display: none">
			        <div class="share-wx" style="margin-top: 25px;">
			            <img id="qrCodeImg" src="" alt="扫一扫，即可分享"/>
			        </div>
			    </div>
            </div>
            <!--产品-图片展示-end-->
            <!--产品-文字展示-start-->
            <div class="goods-summary">
                <div class="summary-title">
                    <h1 id="productTitle"></h1>
                </div>
                 <div class="summary-price">
                    <div class="summary-part">
                        <span>价格：</span>
                        <div class="price">
                            <em class="price-sale"><i>￥</i><span id="salePrice"></span></em>
                        </div>
                    </div>
                    <div id="marketPriceDiv" class="summary-part">
                        <span>市场价：</span>
                        <div class="price">
                            <em id="marketPrice" class="price-market"></em>
                        </div>
                    </div>
                    <div class="summary-part">
                        <span>购买人数：</span><span id="buySummary" class="red"></span>
                    </div>
                </div>
                <div id="specListDiv" class="summary-detail"></div>
                <div id="buyNumDiv" class="summary-detail">
                	<dl>  
	                    <dt>数量：</dt>
	                    <dd><div data-id="" data-val="1" name="numSpinner" class="num-spinner"></div></dd>
	                </dl>
                </div>
                <div id="buyActionDiv" class="summary-buy">
                    <input data-role="addToCartBtn" class="btn btn-w180h45" type="button" value="加入购物车" />
                    <input data-role="goSubmitOrderBtn" class="btn1 btn-w180h45 ml10" type="button" value="立即购买" />
                </div>
            </div>
            <!--产品-文字展示-end-->
        </div>
        <div class="section">
            <div class="section-left1">
                <!--左菜单-开始-->
                <ul id="xMenu">

                </ul>
                <!--左菜单-结束-->
                <div class="block" style="display:none;">
                    <h1>浏览过的商品</h1>
                    <div class="no-result">
                        暂无浏览记录
                    </div>
                    <ul class="goods-viewed">
                        <li>
                            <a href=""><img src="<%=resBaseUrl%>/image-app/temp/goods1.png" alt=""/></a>
                            <a class="text" href="" title="佳通轮胎 Van600 155R13C 90/88S LT 8PR Giti 超强承载，更长里程佳通轮胎 Van600 155R13C 90/88S LT 8PR Giti 超强承载，更长里程">佳通轮胎 Van600 155R13C 90/88S LT 8PR Giti <span class="red1">超强承载，更长里程佳通轮胎</span> Van600 155R13C 90/88S LT 8PR Giti 超强承载，更长里程</a>
                            <div class="price">
                                <em class="price-sale"><i>￥</i>699</em><span class="gray">已有367人购买</span>
                            </div>
                        </li>
                        <li class="last">
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
                <!--商品详情-start-->
                <div class="slideTxtBox">
                    <div id="specParam" class="hd">
                        <ul><li>商品介绍</li><li>规格参数</li><li>商品评价(3)</li></ul>
                    </div>
                    <div class="bd">
                        <div class="bd-content">
                            <div class="bd-top">
                                <ul id="attrVals"></ul>
                            </div>
                            <div class="bd-middle">
                                <div id="goodsIntro" class="bd-detail"></div>
                            </div>
                        </div>
                        <div class="bd-content">
                            <div class="bd-top">
                               <table class="param-td">
	                               <tbody id="specVals"></tbody>
								</table>
                            </div>
                        </div>
                        <div class="bd-content">
                            <div class="bd-top">
                                <div class="comment-top">
                                    <div class="rate">
                                        <h2><em>100</em><i>%</i>好评</h2>
                                        <span>共有0人参与评价</span>
                                    </div>
                                    <div class="percent">
                                        <dl>
                                            <dt>好评<span>(85%)</span></dt>
                                            <dd><span style="width: 85%"></span></dd>
                                        </dl>
                                        <dl>
                                            <dt>中评<span>(10%)</span></dt>
                                            <dd><span style="width: 10%"></span></dd>
                                        </dl>
                                        <dl>
                                            <dt>差评<span>(5%)</span></dt>
                                            <dd><span style="width: 5%"></span></dd>
                                        </dl>
                                    </div>
                                    <div style="display: none;" class="comment-btn">
                                        <span>您可对已购商品进行评价</span>
                                        <input class="btn btn-w108" type="button" value="评价" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--商品详情-end-->
                <!--评价-start-->
                <div class="slideTxtBox slideTxtBox1">
                    <div class="hd">
                        <ul><li>商品评价（3）</li><li>好评（0）</li><li>中评（0）</li><li>差评（0）</li></ul>
                    </div>
                    <div class="bd">
                        <div class="bd-content">
                            <table class="tb-praise">
                                <thead>
                                <tr>
                                    <td>评价心得</td>
                                    <td width="76">顾客满意度</td>
                                    <td>车型</td>
                                    <td width="135">评论者</td>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>干活的师傅很利落，很快就搞定。</td>
                                    <th><span class="star"><i style="width: 100%"></i></span></th>
                                    <th>奥迪Q5<br/>ADSLap3c98</th>
                                    <td>
                                        <div class="item-member bronze">
                                            <div class="item-name"><i><img src="<%=resBaseUrl%>/image-app/temp/favicon1.jpg" alt=""/></i><span>hei***哈</span></div>
                                            <div class="item-grade"><span>铜牌会员</span><em>河南</em></div>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                                <tbody>
                                <tr>
                                    <td>店员态度很好，等待时间提供热水，瓜子，很赞。</td>
                                    <th><span class="star"><i style="width: 80%"></i></span></th>
                                    <th>奔驰<br/>WDTO123</th>
                                    <td>
                                        <div class="item-member bronze">
                                            <div class="item-name"><i><img src="<%=resBaseUrl%>/image-app/temp/favicon1.jpg" alt=""/></i><span>小***a</span></div>
                                            <div class="item-grade"><span>铜牌会员</span><em>江苏</em></div>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                                <tbody>
                                <tr>
                                    <td>商品质量很好，整体品质很有质感。</td>
                                    <th><span class="star"><i style="width: 80%"></i></span></th>
                                    <th>大众<br/>SERO43</th>
                                    <td>
                                        <div class="item-member bronze">
                                            <div class="item-name"><i><img src="<%=resBaseUrl%>/image-app/temp/favicon1.jpg" alt=""/></i><span>j***j</span></div>
                                            <div class="item-grade"><span>银牌牌会员</span><em>北京</em></div>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <!--评价-start-->
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery.jqzoom.js"></script>
<script type="text/javascript">
//----------------------------------------------全局变量----------------------------------------------------
var urlParams = extractUrlParams();
var productId = parseInt(urlParams.productId) || null;

//----------------------------------------------onload----------------------------------------------------
Statis.addGoodsBrowseCount(productId);
//
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
$(function() {
	//右边栏
    renderRightToolbar(true);
	//
	//绑定购物-增加减少
   	renderNumSpinner("numSpinner", doOnNumChange, 1, 1, 1000);
    /*tab页签*/
    jQuery(".slideTxtBox").slide({trigger:"click"});
	//评论回复
	click("reply");
	//初始化产品信息
	initProductData(productId);
	
	//绑定购物车事件
	bindCartEvent();
	//生成二维码
	/* var shareUrl = document.location || "";
	initQrCodeImg("qrCodeImg", shareUrl); */
	//绑定分享事件
	bindShareEvent();
	//产品详情页/门店详情页-评价标题超过一定范围fix
    makeTopSticky("specParam",675,"fixed-right");
});

//----------------------------------------------业务处理----------------------------------------------------

//
function initQrCodeImg(targetId, shareUrl){
	var qrCodeUrl = makeUrl("<%=appBaseUrl%>"+"/xutil/qrCode.do", {width : 200, height:200, url: shareUrl}, true);
	//
	$id(targetId).attr("src", qrCodeUrl);
}

//
function openWeiXinDialog(){
	var dom = "#share-wx";
    Layer.dialog({
        dom : dom, //或者 html string
        area : [ '400px', '300px' ],
        title : "扫描二维码",
        closeBtn : true,
        btn : false
        //默认为 btn : ["确定", "取消"]
    });
}

//车品超市-产品相册
    function test_dialogDom() {
        var dom = "#viewer";
        Layer.dialog({
            dom : dom, //或者 html string
            area : ['860px','600px'],
            title: false,
            closeBtn : true,
            btn : false //默认为 btn : ["确定", "取消"]
        });
    }

//渲染货品
function renderProduct(product){
	//渲染货品信息
	$id("productTitle").html(product.title || product.goodsName || "");
	$id("salePrice").text(product.salePrice);
	var marketPrice = product.marketPrice;
	if(marketPrice){
		$id("marketPriceDiv").css("display", "inline-block");
		$id("marketPrice").text("￥"+marketPrice);
	}else{
		$id("marketPriceDiv").css("display", "none");
	}
	//
	$id("buySummary").html(product.buySum);
	//渲染规格参数
	var specMap = product.goodsEx;
	var renderSpecMap = product.productEx;
	if(specMap){
		var goodsId = product.goodsId;
		var goodsColorImgs = product.goodsColorImgs;
		renderProductSpecs(specMap, renderSpecMap, goodsId, goodsColorImgs);
	}
	//货品相册图片
	var imgUrls = product.albumImgUrls;
	if(imgUrls && imgUrls.length > 0) renderAlbumImgs(imgUrls);
	//商品属性值
	var keyAttrVals = product.keyAttrVals;
	if(keyAttrVals && !isEmptyObject(keyAttrVals)) renderKeyAttrVals(keyAttrVals);
	//渲染货品规格
	var specVals = product.specVals;
	var attrVals = product.attrVals;
	if(attrVals && !isEmptyObject(attrVals)) merge(specVals, attrVals);
	if(specVals && !isEmptyObject(specVals)) renderSpecVals(specVals);
	//
	var intro = product.goodsIntro;
	//商品介绍
	if(intro){
		var introHtml = intro.content || "";
		$id("goodsIntro").html(introHtml);
	}
	
}

//
function renderSpecVals(specVals){
	//获取模板内容
	var tplHtml = $id("specValsTpl").html();
	//生成/编译模板
	var htmlTpl = laytpl(tplHtml);
	//根据模板和数据生成最终内容
	var htmlText = htmlTpl.render(specVals);
	//
	$id("specVals").html(htmlText);
}
//
function renderKeyAttrVals(attrVals){
	//获取模板内容
	var tplHtml = $id("attrValsTpl").html();
	//生成/编译模板
	var htmlTpl = laytpl(tplHtml);
	//根据模板和数据生成最终内容
	var htmlText = htmlTpl.render(attrVals);
	//
	$id("attrVals").html(htmlText);
}
//
function fromListAsImagePreviewDataList(imgUrls){
	var retList = [];
	for(var i=0; i<imgUrls.length; i++){
		var imagePreviewMap = {};
		var imgUrl = imgUrls[i];
		imagePreviewMap.url = imgUrl;
		retList.add(imagePreviewMap);
	}
	return retList;
}
//
function renderAlbumImgs(imgUrls){
	//var _imageList = fromListAsImagePreviewDataList(imgUrls);
	var _imageList = imgUrls;
	renderAlbumImages("imagePreviewer", _imageList, 0);
}
//
function renderProductSpecs(specMap, renderSpecMap, goodsId, goodsColorImgs){
	//获取模板内容
	var tplHtml = $id("specTpl").html();
	//生成/编译模板
	var htmlTpl = laytpl(tplHtml);
	//根据模板和数据生成最终内容
	var dataMap = {};
	dataMap.specMap = specMap;
	dataMap.colorImgs = goodsColorImgs;
	var htmlText = htmlTpl.render(dataMap);
	//
	$id("specListDiv").html(htmlText);
	//渲染选中的规格值列表
	if(renderSpecMap){
		$("dl[data-role='spec']").find("dt").each(function(){
			var specCode = $(this).attr("data-code");
			var specItemId = renderSpecMap[specCode];
			if(specItemId){
				var dl = $(this).parent();
				var selectItems = $(dl).find("a[data-role='specItem']");
				$(selectItems).each(function(){
					var _specItemId = $(this).attr("data-itemId");
					if(_specItemId == specItemId){
						$(this).addClass("active");
						return false;
					}
				});
			}
		});
	}
	//绑定规格选择响应事件
	bindSelectSpecItemEvent(goodsId);
}
//
function bindSelectSpecItemEvent(goodsId){
	$("dl[data-role='spec']").find("a[data-role='specItem']").on("click", function(){
		var filterSpecMap = {};
		//设置选中样式
		var specCode = $(this).attr("name");
		$(this).siblings("a[name='"+specCode+"']").removeClass("active");  //移除未选中项中的active样式
		$(this).addClass("active");  //设置当前对象为active状态
		//
		var itemId = $(this).attr("data-itemId");
		filterSpecMap[specCode] = itemId;
		$("dl[data-role='spec']").find("a[data-role='specItem'][name!='"+specCode+"'].active").each(function(){
			var _specCode = $(this).attr("name");
			var _itemId = $(this).attr("data-itemId");
			filterSpecMap[_specCode] = _itemId;
		});
		//获取当前选中规格下的产品信息
		getProductBySpecMap(filterSpecMap, goodsId);
	});
}
//
function getProductBySpecMap(specMap, goodsId){
	var ajax = Ajax.post("/product/productId/by/specCodes/and/specItemIds/get");
	ajax.data({specMap:specMap, goodsId:goodsId});
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			var productId = result.data;
			if(productId){
				setPageUrl(getAppUrl("/product/detail/jsp?productId=")+productId);
			}else{
				//Toast.show("暂无此商品信息", 3000, "info");
				setNoImg();
			}
		} else {
			Layer.msgWarning(result.message);
		}
	});
	ajax.go();
}
//
function setNoImg(){
	$id("imageViewer").html("");
	var imgList = $id("imagePreviewer").find("div.spec-preview>span>img");
	if(imgList && imgList.length > 0){
		var img = imgList[0];
		$(img).attr("src",getResUrl("/image/noimg.jpg"));
	}
	$("ul[name='imageList']").html("");
	//
	$id("productTitle").html("");
	//
	$id("salePrice").html("");
	//
	$id("buySummary").html("");
	//
	$id("buyNumDiv").css("display","none");
	//
	$id("buyActionDiv").css("display","none");
	//
	Layer.msgInfo("暂无此商品信息");
}
//
//----------------------------------------------初始化------------------------------------------------------
//绑定购物车事件
function bindCartEvent(){
	$("div.summary-buy").find("input[data-role='addToCartBtn']").on("click", function(){
		var spinner = $("div.summary-detail").find("div.num-spinner").first();
		var quantity = $(spinner).attr("data-val");
		//
		addProductToCart(spinner, productId, quantity, addCartSuccessCallBack);
	});
	//
	$("div.summary-buy").find("input[data-role='goSubmitOrderBtn']").on("click", function(){
		var spinner = $("div.summary-detail").find("div.num-spinner").first();
		var quantity = $(spinner).attr("data-val");
		//
		addProductToCart(spinner, productId, quantity, function(){
			setPageUrl(getAppUrl("/saleOrder/submit/jsp"));
		});
	});
}
//加入购物车成功后回调函数
function addCartSuccessCallBack(){
	getCartItemCount(function(count){
		$id("saleCartCount").html(count);
	});
}
//
function initProductData(productId){
	if(isNoB(productId)) return;
	//
	var ajax = Ajax.post("/product/info/get");
	ajax.data({productId : productId});
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			var product = result.data;
			if(product){
				renderProduct(product);
			}else{
				setPageUrl(getAppUrl("/product/unshelve/jsp"));
			}
		} else {
			Layer.msgWarning(result.message);
		}
	});
	ajax.go();
}

//
function bindShareEvent(){
	var _title,_source,_sourceUrl,_pic,_showcount,_desc,_summary,_site,
	_url = document.location, _pic = '';
	//
	var shareDiv = $("div.share");
	//
	$(shareDiv).find("a.share_qzone").on("click", function(){
		//分享货品图片
		var albumImg  = $id("imagePreviewer").find("div.spec-preview>span.jqzoom>img").first();
		if(albumImg) _pic = $(albumImg).attr("src") || '';
		//货品title
		_title = $id("productTitle").html();
		//销售价
		var salePrice = $id("salePrice").html() || '';
		//摘要
		_summary = '我在@亿投车吧 发现了一个非常不错的商品：'+_title+'　促销价：￥ '+salePrice+'。 感觉不错，分享一下';
		//
		var _shareUrl = 'http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?';
		//
		var theParams = {
				url : _url || '', //参数url设置分享的内容链接|默认当前页location
				showcount : _showcount||0, //参数showcount是否显示分享总数,显示：'1'，不显示：'0'，默认不显示
				desc : _desc||'说点什么呗', //参数desc设置分享的描述，可选参数
				summary : _summary, //参数summary设置分享摘要，可选参数
				title : '>>分享自亿投车吧', //参数title设置分享标题，可选参数
				/* site : _site||'', //参数site设置分享来源，可选参数 */
				pics : _pic //参数pics设置分享图片的路径，多张图片以＂|＂隔开，可选参数
		};
		_shareUrl = makeUrl(_shareUrl, theParams, true);
		//
		window.open(_shareUrl);
	});
	//
	$(shareDiv).find("a.share_tsina").on("click", function(){
		//分享货品图片
		var albumImg  = $id("imagePreviewer").find("div.spec-preview>span.jqzoom>img").first();
		if(albumImg) _pic = $(albumImg).attr("src") || '';
		//货品title
		_title = $id("productTitle").html();
		//销售价
		var salePrice = $id("salePrice").html() || '';
		//
		var summary = '我在@亿投车吧 发现了一个非常不错的商品：'+_title+'　促销价：￥ '+salePrice+'。 感觉不错，分享一下';
		//
		var _shareUrl = 'http://v.t.sina.com.cn/share/share.php?&appkey=895033136';     //真实的appkey，必选参数 
		//
		var theParams = {
				url : _url||'', //参数url设置分享的内容链接|默认当前页location
				title : summary||'说点什么呗', //参数title设置分享的标题|默认当前页标题，可选参数
				source : _source||'',
				sourceUrl : _sourceUrl||'',
				content : 'utf-8', //参数content设置页面编码gb2312|utf-8，可选参数
				pics : _pic //参数pics设置分享图片的路径，多张图片以＂|＂隔开，可选参数
		};
		_shareUrl = makeUrl(_shareUrl, theParams, true);
	    //
	    window.open(_shareUrl); 
	});
	//
	$(shareDiv).find("a.share_weixin").on("click", function(){
		//http://cli.im/
		//<img width="200" src="http://cli.clewm.net/qrcode/2015/01/21/2031452178.png" />
	});
}
//购物车中产品数量变化事件
function doOnNumChange(numValue, htDom) {
	var dataId = $(htDom).attr("data-id");
    $(htDom).attr("data-val", numValue);
    console.log(dataId + " : " + numValue);
}

</script>

</body>
<!----------------------------------------------------------信息模板------------------------------------------------------------->
<script type="text/html" id="specValsTpl" title="货品规格值模板">
{{# var specValMap = d;
    for(var spec in specValMap){
		var specVal = specValMap[spec];
}}
 	<tr><th>{{ spec }}</th><td>{{ specVal }}</td></tr>
{{# } }}
</script>

<script type="text/html" id="attrValsTpl" title="商品属性值模板">
{{# var attrValMap = d;
    for(var attr in attrValMap){
		var attrVal = attrValMap[attr];
}}
 	<li><span>{{ attr }}：</span><em>{{ attrVal }}</em></li>
{{# } }}
</script>

<script type="text/html" id="specTpl" title="货品规格模板">
{{# var specMap = d.specMap;
    var colorImgs = d.colorImgs;
    for(var specCode in specMap){
		var specRef = specMap[specCode].first;
		var colorFlag = specRef.colorFlag;
		var specItems = specMap[specCode].second;
}}
 	<dl data-role='spec'>
		<dt data-code='{{ specCode || "" }}'>{{ specRef.name || "" }}：</dt>
		<dd>
			<div class='checkbox-analog'>
			{{# for(var i=0; i<specItems.length; i++){ 
					var itemId = specItems[i].id || "" ;
			}}
				<a class='mr20' data-role='specItem' name='{{ specCode || "" }}' data-itemId='{{ itemId}}' href='javascript:;'>
					{{# if(!colorFlag){ }}
					<span>{{ specItems[i].value || "" }}</span>
					{{# }else{
						var browsePath = colorImgs[itemId] || "" ;
					}}
					<img width='32' height='32' src='{{ browsePath}}' alt='{{specItems[i].value || ""}}'>
					{{# } }}
					<i></i>
				</a>
			{{# } }}
			</div>
		</dd>
	</dl>
{{# } }}
</script>
</html>