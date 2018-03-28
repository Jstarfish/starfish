<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<style type="text/css">
	
	*{margin:0; padding:0; list-style:none; }
	body{ background:#fff; font:normal 12px/22px 宋体;  }
		
	.clearfix::after {
	    clear: both;
	    content: ".";
	    display: block;
	    height: 0;
	    visibility: hidden;
	}
		
	.pro1 dl{ background-color:#F7F7F7; padding:10px; overflow:hidden; padding-bottom:0;}
	.pro1 dl dt{ line-height:22px; font-size:14px;text-align: center;}
	.pro1 dl dd{ float:left; width:100%; line-height:24px; margin-bottom:3px;}
	.pro2_bd_box .bd{ border:1px solid #e8e8e8;padding:15px; overflow:hidden}
	.pro2_bd_box .bd ul li{ margin-bottom:15px;} 
	.pro2_bd_box .bd ul li p{ line-height:24px; }
	.pro2_bd_box .title{font-size:16px; font-family:"微软雅黑";}
	.pro2_bd_box .title strong{ padding-left:12px;}
	.pro2_bd_box .title_far{ line-height:35px; border:1px solid #e8e8e8; border-bottom:0;}
	.pro_right .yes{ margin-top:15px;}
	
	/*规格*/
	.sys_item_spec{ float:left; width:auto;}
	.sys_item_spec dl.iteminfo_parameter dt{ line-height:24px;}
	.sys_item_spec dl.iteminfo_parameter dd{ line-height:24px; margin-bottom:3px;}
	
	.iteminfo_parameter_default{ }
	.iteminfo_parameter dt{  float:left; display:inline; width:72px; white-space:nowrap; text-align:right; }
	.iteminfo_parameter  dd{ float:left; margin-left:10px;}
	.iteminfo_mktprice{font-style:normal; margin-left:10px; font-family:"宋体" ;}
</style>
<title>商品详情</title>
</head>
<body>
	<div class=" s_main_in">
		<div class="goods_d_box clearfix">
			<div class="g_d_name">
				<h2 id="goodTitle"></h2>
			</div>
			<div class="g_d_b_son clearfix">
				<div class="sys_item_spec">
					<dl
						class="clearfix iteminfo_parameter iteminfo_parameter_default lh32">
						<dt>市场价：</dt>
						<dd>
							<span class="iteminfo_mktprice_red">￥</span> <span
								class="iteminfo_mktprice_red" id="salePrice"></span> <span
								class="iteminfo_mktprice"> <del class="sys_item_mktprice">￥</del>
								<del class="sys_item_mktprice" id="marketPrice"></del>
							</span>
						</dd>
					</dl>
					<div id="goodSpecsDiv" class="nullCss"></div>
				</div>
			</div>
		</div>
		<div>
			<div class="pro2_bd_box">
				<div class="title_far">
					<h4 class="title">
						<strong>规格参数</strong>
					</h4>
				</div>
			</div>
			<div id="goodGuige"></div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
	var goodsId,urlParams,productId;
	//获取商品信息
	function initProductData(productId){
		 data = {id:productId};
		 var ajax = Ajax.post("/goods/product/get/by/id");
		  ajax.data(data);
		  ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				renderProductData(result.data);
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();	
	}
	function initGoodsAndShopDate(goodsId){
		 data = {goodsId:goodsId};
		 var ajax = Ajax.post("/goods/basisInfo/by/id/get");
		  ajax.data(data);
		  ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var date=result.data
				var shopDate=date.shop;
				initGoodsAttr(goodsId,date.catId);
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();		
	}
	function initGoodsAttr(goodsId,catId){
		 data = {
				 id:goodsId,
				 catId:catId
				 };
		 var ajax = Ajax.post("/goods/product/goodsAttrVal/by/catId");
		  ajax.data(data);
		  ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var date=result.data;
				var html=""
				for(var x=0;x<date.length;x++){
					html=html+"<div class='yes pro1' style='display:block;'><dl class='clearfix'><dt class='clearfix' ><em class='f-n'><strong>"+date[x].name+"</strong></em>"
					var attrs=date[x].goodsCatAttrs;
					for(var y=0;y<attrs.length;y++){
						html=html+"<dd> <em>"+attrs[y].attrRef.name+"：</em>"
						var attrsValu=attrs[y].attrRef.goodsAttrVals;
						for(var z=0;z<attrsValu.length;z++){
							html=html+attrsValu[z].attrVal+"<span class='chs spaceholder'/>"
						}
						html=html+"</dd>"
					}
					html=html+"</dt></dl></div>"
				}
				$id("goodGuige").html(html);
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();		
	}
	function renderProductData(data){
		//加载图片
		var albumImgs=data.basic.productAlbumImgs;
		var avlumHtml="<ul>"
		var	ablumShowHtml="<ul class='clearfix  sp_det_big_box'>"
		for(var i=0;i<albumImgs.length;i++){
			avlumHtml=avlumHtml+"<li><a target='_blank'><img  src='"+albumImgs[i].fileBrowseUrl+"' /></a></li>"
			ablumShowHtml=ablumShowHtml+"<li><img  src='"+albumImgs[i].fileBrowseUrl+"'/></li>"
		}
		avlumHtml=avlumHtml+"</ul>";
		ablumShowHtml=ablumShowHtml+"</ul>";
		$id("ablumPic").html(avlumHtml);
		$id("ablumPicShow").html(ablumShowHtml);
		$(".picFocus").slide({ mainCell:".bd ul",effect:"left",autoPlay:true });
		//加载规格
		var basic=data.basic;
		var goodSpecs=data.goodSpecs;
		var htmlStr=""
		for(var j=0;j<goodSpecs.length;j++){
			htmlStr=htmlStr+"<dl class='clearfix iteminfo_parameter sys_item_specpara' name='"+goodSpecs[j].specRef.code+"' data-sid='2'><dt>"+goodSpecs[j].specRef.name+"：</dt><dd><ul class='sys_spec_text'>";
			var specsValue=goodSpecs[j].goodsCatSpecItems;
			if(specsValue){
				for(var k=0;k<specsValue.length;k++){
					htmlStr=htmlStr+"<li liName='"+goodSpecs[j].specRef.code+"' liValue='"+specsValue[k].id+"' id='"+goodSpecs[j].specRef.code+specsValue[k].id+"'>"+specsValue[k].value+"<i></i></li>"
				}
			}
			htmlStr=htmlStr+"</ul></dd></dl>"
		}
		$id("goodSpecsDiv").html(htmlStr);
		addCss();
		//选中规格
		    var specVals=data.basic.specVals;
			var specVal="<span class='chs spaceholder'/>"
			for(var l=0;l<specVals.length;l++){
				var liId=specVals[l].refCode+specVals[l].specItemId;
				$id(liId).attr("class","selected");
				specVal=specVal+"<span class='chs spaceholder'/>"+specVals[l].specVal;
			}
			$id("goodTitle").html(basic.goodsName+specVal);
			$id("salePrice").html(basic.salePrice);
			$id("marketPrice").html(basic.marketPrice);
			goodsId=basic.goodsId;
			initGoodsAndShopDate(basic.goodsId);
	}
	
	function addCss(){
		$(".nullCss .sys_item_specpara").each(function(){
			var i=$(this);
			var p=i.find("ul>li");
			p.click(function(){
				if(!!$(this).hasClass("selected")){
					$(this).removeClass("selected");
				}else{
					$(this).addClass("selected").siblings("li").removeClass("selected");
				}
				getNewProduct();
			})
		})	
	}
	function getGoodsCatSpecItem(){
		var list=new Array();
		$(".nullCss .sys_item_specpara").each(function(){
			var i=$(this);
			var p=i.find("ul>li");
			 p.each(function(){
				if(!!$(this).hasClass("selected")){
					 var specRefCode=$(this).get(0).getAttribute("liName");
					 var specsValue=	parseInt($(this).get(0).getAttribute("liValue"));
					 var specdata = {
						specRefCode: specRefCode,
						specsValue:	specsValue
					};
					list.add(specdata);
				}
			}) 
		})
		return list;
	}
	function getNewProduct(){
		 data = {
				goodsId:goodsId,
				specIds:getGoodsCatSpecItem()
				};
		 console.log(data);	
	}
	
	$(function(){
		urlParams = extractUrlParams();
		productId = decodeURI(urlParams["productId"]);
		initProductData(productId);
	});
	</script>
</body>
</html>