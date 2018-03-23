<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/head.jsp" %>
	<div class="content">
        <div class="page-width">
            <div class="section mb20" style="margin-top: -10px">
                <div class="section-right2">
                    <div id="slideBox" class="slideBox">
                        <div class="hd" id="bannerNumber">
                            <ul></ul>
                        </div>
                        <div class="bd" id="banner">
                            <ul></ul>
                        </div>
                        <!-- 下面是前/后按钮代码，如果不需要删除即可 -->
                        <a class="prev" href="javascript:void(0)"></a>
                        <a class="next" href="javascript:void(0)"></a>
                    </div>
                </div>
                <div class="section-left2" >
                	<div class="adv-indexl" id="bannerRight">
                	</div>
                </div>
            </div>
			<!--服务套餐的广告-->
            <div class="super-sale mb20">
                <div class="title"><img src="<%=resBaseUrl%>/image-app/timer-tit.jpg" alt=""/></div>
                <div id="picScroll-left" class="picScroll-left">
					<div class="hd">
						<a class="next"></a>
						<a class="prev"></a>
					</div>
					<div class="bd">
	                <ul class="img-move" id="carSvcPack">
	                </ul>
	                </div>
               </div>
            </div>
            <div class="section floor floor1">
                <div class="title">
                    <h1>美容专区<i>AUTOMOBILE  SERVICE</i></h1>
                </div>
                <div class="clearfix">
                    <div class="section-left2">
                        <div class="floor1-adv" id="firstLeft">
                        </div>
                    </div>
                    <div class="section-right2">
                        <div class="slideTxtBox" id="carSvcList">

                        </div>
                    </div>
                </div>
            </div>
            <div class="adv" id="firstFloor">
            </div>
            <!--floor2-start-->
            <div class="section floor floor2">
                <div class="title">
                    <h1>尖品专区<i>BOUTIQUE  MART</i></h1>
                </div>
                <div class="clearfix">
                    <div class="section-left2">
                        <div class="floor2-adv" id="secondLeft">
                        </div>
                    </div>
                    <div class="section-right2">
                        <div class="slideTxtBox" id="productList">

                        </div>
                    </div>
                </div>
            </div>
            <div class="advs-5" id="secondFloor">
            </div>
            <!--floor2-end-->
            <div class="advs-link mb20">
                <ul class="img-move" id="helpImage">
                </ul>
            </div>
            <div class="buy-process">
                <h1>购买流程<img src="<%=resBaseUrl%>/image-app/buy-porcess-title.jpg" alt=""/></h1>
                <div class="buy-process-cont">
                    <img src="<%=resBaseUrl%>/image-app/process1.jpg" alt=""/>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/WEB-INF/jsp/include/foot.jsp"/>
   	<script type="text/javascript">
   	var sorterRendered = false;
      jQuery(".slideTxtBox").slide({});

   //加载广告信息
   function initAdvertImage(){
   var ajax = Ajax.post("/market/advert/list/get");
	ajax.sync();
	ajax.done(function(result, jqXhr) {
		//
		if (result.type == "info") {
			var dataList = result.data;
			var bannerNumberHtml = "";
			var bannerHtml = "";
			var bannerRightHtml = "";
			var middleAccrossHtml = "";
			var firstLeftHtml = "";
			var firstFloorHtml = "";
			var secondLeftHtml = "";
			var secondFloorHtml = "";
			var helpImageHtml = "";
			if(dataList != null){
				for(var i = 0 ; i < dataList.length; i++){
					var advertLinks = dataList[i].advertLinks;
					 if(dataList[i].posCode == "home.right.top"){
						//生成模板内容
						for(var j = 0 ; j < advertLinks.length; j++){
							var html = "<a href='"+ getAppUrl(advertLinks[j].linkUrl) + "' target='_blank'><img src='" + advertLinks[j].imageUrl + "'/></a>";
							bannerRightHtml = bannerRightHtml + html;
						}
					}else if(dataList[i].posCode == "home.top.lunbo"){
						for(var j = 0 ; j < advertLinks.length; j++){
							if(j < 5){
								var number = "<li>"+ j +"</li>";
								bannerNumberHtml = bannerNumberHtml + number;
								//生成模板内容
								var html = "<li style='float: left; width: 990px;'><a href='"+ getAppUrl(advertLinks[j].linkUrl) + "' target='_blank'><img src='" + advertLinks[j].imageUrl + "' /></a></li>";
								bannerHtml = bannerHtml + html;
							}
						}
					}else if(dataList[i].posCode == "home.middle.across"){
						for(var j = 0 ; j < advertLinks.length; j++){
							//生成模板内容
							var first = (j == 0 ? "first" : "");
							var html = "<li style='float: left;' class="+first+"><a href='#'><img src='" + advertLinks[j].imageUrl + "' /></a></li>";
							middleAccrossHtml = middleAccrossHtml + html;
						}
					}else if(dataList[i].posCode == "home.first.left"){
						//生成模板内容
						firstLeftHtml = "<a href='"+ getAppUrl(advertLinks[0].linkUrl) + "' target='_blank'><img src='" + advertLinks[0].imageUrl + "'/></a>"
					}else if(dataList[i].posCode == "home.first.floor"){
							//生成模板内容
						for(var j = 0 ; j < advertLinks.length; j++){
							html = "<a href='" + getAppUrl(advertLinks[j].linkUrl) + "' target='_blank'><img src='" + advertLinks[j].imageUrl + "'/></a>"
							firstFloorHtml = firstFloorHtml + html;
						}
					}else if(dataList[i].posCode == "home.second.left"){
							//生成模板内容
						secondLeftHtml = "<a href='"+ getAppUrl(advertLinks[0].linkUrl) + "' target='_blank'><img src='" + advertLinks[0].imageUrl + "'/></a>"
					}else if(dataList[i].posCode == "home.second.floor"){
							//生成模板内容
						for(var j = 0 ; j < advertLinks.length; j++){
							html = "<a href='"+ getAppUrl(advertLinks[j].linkUrl) + "' target='_blank'><img src='" + advertLinks[j].imageUrl + "'/></a>";
							secondFloorHtml =secondFloorHtml + html;
						}
					}else if(dataList[i].posCode == "home.help.image"){
						for(var j = 0 ; j < advertLinks.length; j++){
							//生成模板内容
							var first = (j == 0 ? "first" : "");
							var html = "<li style='float: left;'  class="+first+"><a href='#'><img src='" + advertLinks[j].imageUrl + "' /></a></li>";
							helpImageHtml = helpImageHtml + html;
						}
					}
				}
			}

			//生成banner的排序
			$id("bannerNumber").find("ul").html(bannerNumberHtml);
			//生成banner的内容
			$id("banner").find("ul").html(bannerHtml);
			//生成右上的广告内容
			$id("bannerRight").html(bannerRightHtml);
			//生成中部的广告内容
			$id("picScroll-left").html(middleAccrossHtml);
			//生成1楼左侧广告内容
			$id("firstLeft").html(firstLeftHtml);
			//生成1楼广告内容
			$id("firstFloor").html(firstFloorHtml);
			//生成2楼左侧广告内容
			$id("secondLeft").html(secondLeftHtml);
			//生成2楼广告内容
			$id("secondFloor").html(secondFloorHtml);
			//底部友情链接
			$id("helpImage").html(helpImageHtml);
		}
		//
		$id("slideBox").slide({mainCell:".bd ul",effect:"leftLoop",autoPlay:true});
		//服务套餐  左右循环切换
	    jQuery(".picScroll-left").slide({titCell:".hd ul",mainCell:".bd ul",autoPage:true,effect:"leftLoop",scroll:5,vis:5});
		bindHoverEvent("picScroll-left");
	});
	ajax.go();
   }

   function loadCarSvcPack() {
		var ajax = Ajax.post("/carSvc/svc/pack/list/get");

		var postData = {};
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var paginatedList = result.data;
				//
				var products = paginatedList.rows;
				if(products.length > 0){
					// 获取模板内容
					var tplHtml = $id("carSvcPackListTpl").html();
					// 生成/编译模板
					var htmlTpl = laytpl(tplHtml);
					var htmlText = htmlTpl.render(products);
					$id("carSvcPack").html(htmlText);
				}else{
					$id("carSvcPack").html("");
				}
			} else {
				Layer.msgWarning(result.message);
			}

		});
		ajax.always(function() {
		});
		ajax.go();
	}

   function initProducts() {
		var ajax = Ajax.post("/salesFloor/salesRegion/list/get");
		var postData = {
				type : 0
			};
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var productData = result.data;
				// 获取模板内容
				var tplHtml = $id("productListTpl").html();
				// 生成/编译模板
				var htmlTpl = laytpl(tplHtml);
				var htmlText = htmlTpl.render(productData);
				$id("productList").html(htmlText);
				//更多车品超市
				$id("productMore").click(function() {
					//跳转货品列表页
					var pageUrl = makeUrl(getAppUrl("/product/supermarket/list/jsp"));
					setPageUrl(pageUrl, "_blank");
				});
				//
				jQuery(".slideTxtBox").slide({});
			} else {
			}
		});
		ajax.always(function() {
		});
		ajax.go();
	}

 	//加载车辆服务列表
	function loadCarSvc() {
		var ajax = Ajax.post("/salesFloor/salesRegion/list/get");
		var postData = {
				type : 1
			};
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var carSvcData = result.data;

				// 获取模板内容
				var tplHtml = $id("carSvcListTpl").html();
				// 生成/编译模板
				var htmlTpl = laytpl(tplHtml);
				var htmlText = htmlTpl.render(carSvcData);
				$id("carSvcList").html(htmlText);
				//更多服务
				$id("carSvcMore").click(function() {
					//跳转服务列表页
					var pageUrl = makeUrl(getAppUrl("/carSvc/list/jsp"));
					setPageUrl(pageUrl, "_blank");
				});
				//
				jQuery(".slideTxtBox").slide({});
			} else {
			}
		});
		ajax.go();
	}

   $('.flexslider').flexslider({
          directionNav: true,
          pauseOnAction: false
      });

   $(function(){
	   //右边栏
	   renderRightToolbar(true);
	   //初始化广告信息
	   initAdvertImage();
	   //初始化套餐
	   loadCarSvcPack();
	   //初始化车品商品
	   initProducts();
	   //初始化服务列表
	   loadCarSvc();
	   //快捷通道商品分类
	   $("#nav1 .fast-maintain").attr("id", "fast-maintain");
	   //初始化快捷通道商品分类
	   initProductCatsData();
	 	//快速通道
	   $("#nav1").css("display", "");
	   $("#nav > #fast-channel").css("display", "none");
	   $("#nav1>.fast-channel").children(".sub").show();

   });

	 </script>
	 </body>
	 <script type="text/html" id="carSvcPackListTpl" title="服务套餐模版">
	{{# if(d.length > 0){  }}
	{{# for(var i=0, len=d.length; i< (10 > d.length? d.length : 10) ; i++) {  }}
	 <li>
         <a href="<%=appBaseUrl%>/carSvc/svc/pack/list/jsp?svcPackId={{d[i].id}}" target="_blank">
            <img src="{{ d[i].fileBrowseUrl }}" alt="{{ d[i].name }}"/>
			<span>{{ d[i].name }}</span>
         </a>
     </li>
	{{# } }}
	{{# }else{ }}
	 <div>暂无服务套餐信息！
     </div>
	{{# } }}
	</script>
	<script type="text/html" id="productListTpl" title="商品列表模版">
        {{# if(d.length > 0){  }}
        <div class="hd clearfix">
            <a class="fr anormal" id="productMore" href="javascript:;">更多&gt;&gt;</a>
            <ul>
                {{# for(var i=0; i< d.length; i++) {  }}
                {{# var saleRegion = d[i]; }}
                <li>{{ saleRegion.name }}<span>|</span></li>
                {{# } }}
            </ul>
        </div>
    <div class="bd">
		{{# for(var i=0; i< d.length; i++) {  }}
			<ul class="goods-list1">
		{{# var saleRegion = d[i]; var saleRegionGoodsList = saleRegion.salesRegionGoods; }}
       	{{# if(saleRegionGoodsList.length > 0){  }}
       		{{# for(var j=0, len=saleRegionGoodsList.length; j< (8 > saleRegionGoodsList.length? saleRegionGoodsList.length : 8) ; j++) {  }}
					{{# var saleRegionGoods = saleRegionGoodsList[j]; var productAlbumImgs = saleRegionGoods.productAlbumImgs; var product = saleRegionGoods.product}}
  				<li>
       				<a href="<%=appBaseUrl%>/product/detail/jsp?productId={{ product.id }}" target="_blank">
            			{{# if(productAlbumImgs && productAlbumImgs.length > 0){ }}
          				<img src="{{ productAlbumImgs[0].fileBrowseUrl }}" alt=""/>
					{{# }else{ }}
					<img src="static/image-app/temp/goods1.png" alt=""/>
  						{{# } }}
            			<span class="price">¥{{ product.salePrice }}</span>
            			<span class="text" >{{ product.title ? product.title : product.goodsName }}</span>
        			</a>
    			</li>
			{{# } }}
		{{# }else{ }}
 		<div>暂无商品信息！
    		</div>
       	{{# } }}
		</ul>
		{{# } }}
    </div>
	{{# } }}
	</script>
	<script type="text/html" id="carSvcListTpl" title="车辆服务列表模版">
	{{# if(d.length > 0){  }}
	<div class="hd clearfix">
        <a class="fr anormal" id="carSvcMore" href="javascript:;">更多&gt;&gt;</a>
		<ul>
		{{# for(var i=0; i< d.length; i++) {  }}
		{{# var saleRegion = d[i]; }}
        <li>{{ saleRegion.name }}<span>|</span></li>
		{{# } }}
		</ul>
    </div>
    <div class="bd">
		{{# for(var i=0; i< d.length; i++) {  }}
			<ul class="svs-list">
		{{# var saleRegion = d[i]; var saleRegionSvcList = saleRegion.salesRegionSvc; }}
       	{{# if(saleRegionSvcList.length > 0){  }}
       		{{# for(var j=0, len=saleRegionSvcList.length; j< (10 > saleRegionSvcList.length? saleRegionSvcList.length : 10) ; j++) {  }}
					{{# var saleRegionSvc = saleRegionSvcList[j]; var svcx = saleRegionSvc.svcx }}
  				<li>
       				<a href="<%=appBaseUrl%>/carSvc/list/jsp?svcId={{svcx.id}}" target="_blank">
             		<img src="{{ svcx.fileBrowseUrl }}" alt="{{ svcx.name }}"/>
					<span>{{ svcx.name }}</span>
         			</a>
    			</li>
			{{# } }}
		{{# }else{ }}
 		<div>暂无服务信息！
    		</div>
       	{{# } }}
		</ul>
		{{# } }}
    </div>
	{{# } }}
	</script>
</html>

