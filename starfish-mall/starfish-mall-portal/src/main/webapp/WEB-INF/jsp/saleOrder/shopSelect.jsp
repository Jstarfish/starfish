<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <!-- Vendor Specific -->
    <!-- Set renderer engine for 360 browser -->
    <meta name="renderer" content="webkit">
    <!-- Cache Meta -->
    <meta http-equiv="Expires" content="0">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Pragma" content="no-cache">
    <!--<link rel="shortcut icon" href="<%=resBaseUrl%>/image/favicon.ico" type="image/x-icon" />-->
    <link rel="stylesheet" href="<%=resBaseUrl%>/css-app/main.css" />
    <!--[if lt IE 9]>
    <script type="text/javascript" src="<%=resBaseUrl%>/js/html5/html5shiv.js"></script>
    <![endif]-->
    <!-- Global Javascript -->
	<script type="text/javascript">
		var __appBaseUrl = "<%=appBaseUrl%>";
		//快捷方式获取应用url
		function getAppUrl(url){
			return __appBaseUrl + (url || "");
		}
	</script>
    <title>选择门店</title>
    <style>
    .optstatus{
    	width:30px;
    }
    </style>
</head>
<body>
<div class="shops-select">
    <div class="shops-select-left">
        <table class="order-td shopping-td">
            <thead>
            <tr>
             <th colspan="2"><div class="text-left">
								<b>服务/商品</b>
							</div></th>
						<th width="55">状态</th>
            </tr>
            </thead>
        </table>
        <div class="shops-select-con">
            <table class="order-td shopping-td">
                <tbody id="carSvcGoods">
              
                </tbody>
                <tbody  id="optGoods">
               
                </tbody>
            </table>
            <div class="shops-selected" id="shops-selected" style="display:none">
                已选门店：
                <div class="checkbox-analog"  id="selectShopTip">
                    <em class="mr20 active" id="selectShop" ><span>亿投车吧良乡路北店</span><i></i></em><input class="btn btn-w120h28" type="button" value="确定选择此店"/>
                </div>
            </div>
        </div>
    </div>
    <div class="shops-select-right" id="shopsDiv">
        <div class="mod-shops-title clearfix">
          <select class="field value" style="border: 1px solid #ccc; height: 28px; width: 120px; padding: 3px 5px;" id="province"></select>
				<select class="field value" style="border: 1px solid #ccc; height: 28px; width: 120px; padding: 3px 5px;" id="city"></select>
				<select class="field value" style="border: 1px solid #ccc; height: 28px; width: 120px; padding: 3px 5px;" id="county"></select>	
        </div>
        <div class="shops-more" >
            <ul id="shopsList" class="shops-list" >
            </ul>
            <div class="loadMore"><a href="javascript:void(0);">加载更多&gt;&gt;</a></div>
        </div>
    </div>
</div>
<input type="hidden" id="regionId" />
<input type="hidden" id="regionName" />
<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery.superslide.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/js/common/common.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/js/libext/jquery.ext.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/js-app/main.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/js-app/app.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/js/common/laytpl.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/js-app/saleOrder/selectShop.js?v=1.0.0" ></script>
<script type="text/javascript">
//-----------------------------------------------------------}}
</script>
</body>
<script type="text/html" id="shopListTpl" title="线下门店列表">
	{{# var shopDtos = d; }}
	{{# for(var i = 0, len = shopDtos.length; i < len; i++){ }}
		{{# var shopDto = shopDtos[i]; }}
		{{# var shop = shopDto.shop; }}
		{{# var className = ""; }}
		{{# if(ecardShopCountMap){ }}
			{{# var ecardShopCount=ecardShopCountMap.get(shopDto.id.toString());}}
			{{# if(ecardShopCount){ }}
				{{# shopDto.isECard=true; }}
			{{# }else{ }}
				{{# shopDto.isECard=false; }}
			{{# } }}
		{{# } }}
		
		{{# if(shopDto.isECard){ }}
			{{#className="shops-list-special"}}
		{{#}}}
		{{# if(selectShopDto!=null&&selectShopDto.id==shop.id){ }}
			{{#className+="active"}}
		{{#}}}
		   		<li {{# if(className!=""){ }}class="{{className}}"{{#}}}>
                    <h1 shopId="{{shop.id}}">
					{{shop.name}}
					{{# if(shopDto.isECard){ }}
						{{# if(ecardShopCount){ }}
            		        {{# var count=ecardShopCount.count; }}
            				<span class="special ml10">
                				您有{{count}}张e卡，服务消费绑定了此店铺。
							</span>
            		    {{# } }}
                	{{#}}}                    
				</h1>
                    <dl class="pic-text">
                        <dt><img src="{{shop.fileBrowseUrl}}" title="{{shop.name}}" width="70" height="70"/></dt>
                        <dd>
                           <!-- <div>满意度：<span class="star"><i style="width: 90%"></i></span></div>
                            <div>此门店共完成134个单，有32条评论</div>-->
							<div>地址：<font id="address_{{shop.id}}">{{shop.regionName}}</font></div>
                        </dd>
                    </dl>
                    <i class="selected"></i>
                </li>
	{{# } }}
</script>
<script type="text/html" id="saleCartRowTpl">
{{# var cartSvList = d; }}
 {{# if(!isNoB(cartSvList)){ }}
 <tr class="title">
	<th colspan="7">
	    <div class="text-left">
	 <label class="mr20">洗美服务</label>
	    </div>
	</th>
	   </tr>
	{{# for(var i=0,len=cartSvList.length; i < len; i++) {}}
		{{#var cartSv=cartSvList[i];}}
		{{#svcIds[i]=cartSv.svcId;}}
	<tr id="svc{{cartSv.svcId}}">
		<td colspan="2">
	    <dl class="goods-item goods-item2">
	 <dt><a href="javascript:void(0)" ><img src="{{cartSv.svcxAlbumImg}}" alt=""/></a></dt>
	 <dd class="text">
	 	<a href="">{{cartSv.carSvcName}}<br />
	 		<span class="gray1">{{cartSv.desc}}</span>
	 	</a>
	 </dd>
	    </dl>
		</td>
	<td id="svcStatus_{{cartSv.svcId}}" width="55"><font color="green">正常</font></td>
</tr>
{{# } }}
{{# } }}
</script>
<script type="text/html" id="optGoodsRowTpl">
{{# var optSvGoods = d; }}
{{# if(!isNoB(optSvGoods)){ }}
<tr class="title">
<th colspan="7">
    <div class="text-left">
        <label class="mr20">自选商品</label>
    </div>
</th>
</tr>
	{{# if(!isNoB(optSvGoods)){ }}
		{{# for(var j=0,len=optSvGoods.length; j < len; j++) { }}
			{{# var goods = optSvGoods[j]; }}
			{{# opProductIds[j] = goods.productId; }}
<tr id="cgoods{{goods.productId}}">
<td colspan="2">
    <dl class="goods-item goods-item2">
        <dt><a href="{{__appBaseUrl}}/product/detail/jsp?productId={{goods.productId}}"><img src="{{goods.productAlbumImg}}" title="{{goods.productName}}"/></a></dt>
        <dd class="text"><a href="{{__appBaseUrl}}/product/detail/jsp?productId={{goods.productId}}" target="_blank" title="{{goods.productName}}">{{goods.productName}}</a></dd>
    </dl>
</td>
<td id="status_{{goods.productId}}" width="55"><font color="green">有货</font></td>
</tr>
			{{# } }}
	{{# } }}
{{# } }}
</script>
</html>