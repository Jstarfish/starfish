<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="base.jsf"%>
<div class="footer">
	<div class="page-width">
		<div class="footer-slogen">
            <img src="<%=resBaseUrl%>/image-app/temp/adv06.jpg" alt=""/>
        </div>
		<ul class="guide">
			<li><h3>新手帮助</h3>
				<ul>
					<li><a href="<%=appBaseUrl%>/comn/faq/jsp?id=1000">用户手册</a></li>
					<li><a href="<%=appBaseUrl%>/comn/faq/jsp?id=1001">服务流程</a></li>
					<li><a href="<%=appBaseUrl%>/comn/faq/jsp?id=1001">购物流程</a></li>
					<li><a href="<%=appBaseUrl%>/comn/faq/jsp?id=1002">常见问题</a></li>
				</ul></li>
			<li><h3>车吧特色</h3>
				<ul>
					<li><a href="<%=appBaseUrl%>/comn/faq/jsp?id=1003">车吧互动</a></li>
					<li><a href="<%=appBaseUrl%>/comn/faq/jsp?id=1003">玩转E卡</a></li>
					<li><a href="<%=appBaseUrl%>/comn/faq/jsp?id=1004">标准化服务</a></li>
				</ul></li>
			<li><h3>联系我们</h3>
				<ul>
					<li><a href="<%=appBaseUrl%>/comn/faq/jsp?id=1005">关于亿投车吧</a></li>
					<li><a href="<%=appBaseUrl%>/comn/faq/jsp?id=1006">联系方式</a></li>
					<li><a href="<%=appBaseUrl%>/comn/faq/jsp?id=1007">招贤纳士</a></li>
				</ul></li>
			<li><h3>诚邀合作</h3>
				<ul>
					<li><a href="<%=appBaseUrl%>/comn/faq/jsp?id=1008">加盟合作</a></li>
					<li><a href="<%=appBaseUrl%>/comn/faq/jsp?id=1009">产品合作</a></li>
					<li><a href="<%=appBaseUrl%>/comn/faq/jsp?id=1010">广告合作</a></li>
				</ul></li>
			<li class="footer-map">
				<div class="coverage">
					亿投车吧已向全国330城市开放10000余家安装门店，覆盖规模迅速扩大中；<br />
					<a class="anormal" href="<%=appBaseUrl%>/comn/faq/jsp?id=1012">了解详情&gt;&gt;</a><br />
					<!--TODO 暂不处理-->
					<%--<a class="anormal" href="<%=AppNodeCluster.getCurrent().getAbsBaseUrlByRole("web-back") %>/merch/entering/jsp" target="_blank">加盟合作&gt;&gt;</a>--%>
				</div>
			</li>

		</ul>
	</div>
	<div class="footer-con">
       <p>友情链接:<a class="ml10" href="#">极限户外-自驾</a><a href="#">天津企业</a><a href="#">北京团购</a><a href="#">绿野户外</a><a href="#">镭射眼智能行车记录仪</a><a href="#">汽配龙</a><a href="#">快递查询</a></p>
       <p>Copyright©亿车汇 All Rights Reserved 版权所有 亿车汇（天津） 电子商务有限公司 网站备案/许可证号：津ICP备15008268号-1</p>
   </div>
</div>
</div>
<!-- 公用 Javascript -->
<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery-migrate.min.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery-ui.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery.locale-cn.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/lib/qtip/jquery.qtip.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery.timepicker.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/lib/layout/jquery.layout.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery.scrollstop.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/lib/lazyload/jquery.lazyload.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jqpaginator.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery.superslide.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery.flexslider-min.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/lib/layer/layer.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/lib/layer/extend/layer.ext.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/js/common/store.min.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/js/common/laytpl.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/js/common/common.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/js/libext/layer.ext.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/js/libext/jquery.ext.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/js/libext/toolbar.js"></script>

<script type="text/javascript" src="<%=resBaseUrl%>/js-app/app.js"></script>

<script type="text/javascript" src="<%=appBaseUrl%>/js/dictSelectLists/get"></script>
<script type="text/javascript" src="<%=appBaseUrl%>/js/dictEnumVars/get"></script>
<script type="text/javascript" src="<%=appBaseUrl%>/js/imageSizeDefs/get"></script>

<script type="text/javascript" src="<%=resBaseUrl%>/js-app/main.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/js-app/login.js"></script>


<!-- Global Javascript -->
<script type="text/javascript">
	    //设置Ajax请求基本路径
	    Ajax.baseUrl = getAppUrl("");
	  	//quick-menu
	    jQuery("#quick-menu,#shopping-cart").slide({ type:"menu", titCell:".nLi", targetCell:".sub",effect:"slideDown",delayTime:300,triggerTime:0,defaultPlay:false,returnDefault:true});
	  	
	    //快速通道点击操作
	    $(".btnFastChannel").click(function(){
    	  $id("sub").toggle();
    	});
	  	
	  	//顶部购物车
		$id("nLi-shopping-cart-a").mouseover(function() {
	    	ajaxPostCartIndex();
		});
	  	
	  	//快速通道
		$("#nav1").css("display", "none");
		$("#nav > #fast-channel").css("display", "");
	  	
	  	
		//快捷通道商品分类
	    $("#nav .fast-maintain").attr("id", "fast-maintain");
	    //初始化快捷通道商品分类
	    initProductCatsData();
	    //初始化商城的信息
	    loadMall();
	  	
		//顶部全局搜索绑定事件
		$id("btnToSearch").click(function() {
	    	toKeySearch("");
		});
		
		$("#keySearch").bind('keydown', function(e) {
			var key = e.which;
			if (key == 13) {
				e.preventDefault();
				toKeySearch("");
			}
		});
		
		// 顶部全局搜索
		function toKeySearch(key) {
			var keySearch = key;
			if(keySearch == ""){
				//获取搜索的值
				keySearch = $("#keySearch").val();
			}
			//跳转货品列表页
			var pageUrl = makeUrl(getAppUrl("/product/supermarket/list/jsp"), {keySearch: keySearch});
			setPageUrl(pageUrl, "_blank");
		}
		
		// 渲染页面内容
		function renderIndexSaleCartHtml(data,fromId,toId) {
			//获取模板内容
			var tplHtml = $id(fromId).html();
			
			//生成/编译模板
			var theTpl = laytpl(tplHtml);
			
			//根据模板和数据生成最终内容
			var htmlStr = theTpl.render(data);
			//使用生成的内容
			$id(toId).html(htmlStr);
		}
		//删除请求
		function onDeleteGoodsIndex(productId,quantity) {
			var url="/saleCart/goods/sync/do";
			var saleCartGoodsPo={};
			saleCartGoodsPo.productId=productId;
			saleCartGoodsPo.quantity=quantity;
			saleCartGoodsPo.action="minus";
			//saleCartGoodsPo.checkFlag = null;
			postSaleCartIndex(url,saleCartGoodsPo)
		}
		//删除请求
		function onDeleteSvcIndex(svcId) {
			var url="/saleCart/svc/sync/do";
			var __saleCartSvcPo = {};
			__saleCartSvcPo.svcId = svcId;
			__saleCartSvcPo.action="minus";
			//__saleCartSvcPo.checkFlag = null;
			postSaleCartIndex(url,__saleCartSvcPo)
		}
		//公共请求
		function postSaleCartIndex(url,data) {
			var ajax = Ajax.post(url);
			if(data!=null){
				ajax.data(data);
			}
			ajax.done(function(result, jqXhr) {
				//
				if (result != "" && result.type == "info") {
					var saleCart=result.data;
					if (saleCart != null) {
						saleCartamountIndex(saleCart);
						renderIndexSaleCartHtml(saleCart,"optsaleCartRowTpl", "saleCart");
					} else {
						//$id("carSvcCount").html(0);
						$id("saleCart").html("");
						saleCartamountIndex(null);
					}
				} else {
					Layer.warning(result.message);
				}
				});
			ajax.go();
		}
		//初始请求
	  	function ajaxPostCartIndex() {
	  		postSaleCartIndex("/saleCart/list/do",{falgType:"all"});
		}
		//查询购物车数量
		getCartItemCount(function(count){
			$id("saleCartCountIndex").html(count);
		});
	  	$id("goSaleCart").click(function() {
	  		var pageUrl = makeUrl(getAppUrl("/saleCart/list/jsp"));
			setPageUrl(pageUrl);
		});
		//价格赋值和数量赋值
		function saleCartamountIndex(saleCart) {
			if(saleCart!=null){
				var amountInfo = saleCart.amountInfo;
				var saleAmount=amountInfo.saleAmount==null?0:amountInfo.saleAmount;
				var goodsCount=saleCart.goodsCount==null?0:saleCart.goodsCount;
				var svcCount=saleCart.svcCount==null?0:saleCart.svcCount;
				//
				$id("finalAmountIndex").html("¥&nbsp;"+saleAmount);
				$id("goodsCountIndex").html(goodsCount);
				$id("saleCartCountIndex").html(goodsCount+svcCount);
				$id("svcCountIndex").html(svcCount);
			}else{
				$id("goodsCountIndex").html(0);
				$id("finalAmountIndex").html("¥&nbsp;"+0);
				$id("saleCartCountIndex").html(0);
				$id("svcCountIndex").html(0);
			}
		}
	  
		//初始化商品分类
		function initProductCatsData() {
			var ajax = Ajax.post("/product/goods/categ/list/get");
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var cats = result.data;
					if(cats != null){
						fastGoodcategs(cats);
					}
				} else {
				}
			});

			ajax.go();
		}
	  
		//渲染商品分类区域
		function fastGoodcategs(cats){
			var htmlText = "";
		  	for(var i = 0; i < cats.length; i ++){
				var cat = cats[i]; 
				if(cat.level == 2){
					id = cat.id;
					//获取商品分类模板内容
					var tplHtml = $id("fast-maintainTpl").html();
					//生成/编译商品分类模板
					var htmlTpl = laytpl(tplHtml);
					//根据模板和数据生成最终内容
					html = htmlTpl.render(cat);
					htmlText = html + htmlText;
				}
			}
		    //使用生成的内容
			$("#fast-maintain").html(htmlText);
			thirdGoodcategs(cats)
		}
	  
		//3级商品分类
		function thirdGoodcategs(cats){
			for(var i = 0; i < cats.length; i ++){
				var cat = cats[i]; 
				if(cat.level == 3){
					parentId = cat.parentId;
					//获取子商品分类模板内容
					var childrenHtmlText = "<a href="+getAppUrl("") + "/product/supermarket/list/jsp?catId="+ cat.id +">"+ cat.name +"</a>";
					//添加 li
					$id("children"+parentId).append(childrenHtmlText);
				}
				
			}
		}
	  
		//按钮倒计时
	    function update(num) {
	        var secs = 60;
	        if (num == secs) {
	        	$id("smsCodeTip").html("");
	        	$id("btnSendVfCode").removeAttr("disabled");
	        }
	        else {
	            printnr = secs - num;
	            $id("smsCodeTip").html( printnr + "秒后可以再次获取");
	            $id("btnSendVfCode").attr("disabled", true);
	        }
	    }
		
	 	// 加载商城基本信息
		function loadMall() {
			var ajax = Ajax.post("/setting/mall/get");
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data;
					if (data != null) {
						$("#logoImg").html("<img src="+data.fileBrowseUrl+"?"+new Date().getTime()+"/>");
					} else {
					}
				} else {
				}
			});
			ajax.go();
		}
	  function strMaxWidth(str,maxwidth){
		  if(str != null && maxwidth != null && str.length>maxwidth){
			 return str.substring(0,maxwidth)+"...";
		  }else{
			 return str;
		  }
	  }
	</script>

	<script type="text/html" id="optsaleCartRowTpl">
	{{# var saleCart = d;}}
	{{# if(!isNoB(saleCart)){ }}
		{{# var svcList = saleCart.saleCartSvcList;}}
		{{# if(!isNoB(svcList) && svcList.length>0){ }}
			<li class="title">洗美服务<span style="width:120px;" class="shopping-price">小计：¥&nbsp;{{saleCart.svcAmount || 0.00}}</span></li>
			{{# for(var i=0,len=svcList.length; i<len; i++) { }}
			{{# var svc = svcList[i];}}
				<li>
        			<a class="shopping-img"><img src="{{svc.svcxAlbumImg}}" alt=""/></a>
        			<a class="shopping-text" title="{{svc.carSvcName}}">{{svc.carSvcName}}</a>
					{{# var amountInfo = svc.amountInfo || {}; }}
        			<span class="shopping-price"><span>¥&nbsp;{{amountInfo.saleAmount || 0.00}}</span>×1</span>
        			<a class="shopping-delete" onClick="onDeleteSvcIndex({{svc.svcId}})" href="javascript:void(0);">删除</a>
   				</li>
			{{# } }}
		{{# } }}

		{{# var cartGoods = saleCart.saleCartGoods;}}
		{{# if(!isNoB(cartGoods) && cartGoods.length>0){ }}
			<li class="title">自选商品<span style="width:120px;" class="shopping-price">小计：¥&nbsp;{{saleCart.goodsAmount || 0.00}}</span></li>
			{{# for(var i=0,len=cartGoods.length; i<len; i++) { }}
			{{# var goods = cartGoods[i];}}
			<li>
        		<a class="shopping-img" href="<%=appBaseUrl%>/product/detail/jsp?productId={{goods.productId}}"><img src="{{goods.productAlbumImg}}" alt=""/></a>
        		<a class="shopping-text" href="<%=appBaseUrl%>/product/detail/jsp?productId={{goods.productId}}" title="{{goods.productName}}">{{goods.productName}}</a>
        		<span class="shopping-price"><span>¥&nbsp;{{goods.productAmount}}</span>×{{goods.quantity}}</span>
        		<a class="shopping-delete" onClick="onDeleteGoodsIndex({{goods.productId}},{{goods.quantity}})" href="javascript:void(0);">删除</a>
   			</li>
			{{# } }}
		{{# } }}
	{{# } }}

	</script>

	<script type="text/html" id="fast-maintainTpl">
	<div class="maintain-cont">
       <h2>{{ d.name }}</h2>
	   <div id="children{{ d.id }}"></div>
   </div>
	</script>


