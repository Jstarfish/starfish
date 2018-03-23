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
	<style type="text/css">
		.shops-select .shops-more {
		    overflow: auto;
		    position: absolute;
		    top: 39px;
		    bottom: 0;
		    left: 0;
		    right: 0;
		}
	</style>
    <title>选择门店</title>
    <style>
    .optstatus{
    	width:30px;
    }
    </style>
</head>
<body>
<div class="shops-select" id="shopsDiv">

    
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
<input type="hidden" id="regionId" />
<input type="hidden" id="regionName" />
<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/lib/jquery/jquery.superslide.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/js/common/common.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/js/libext/jquery.ext.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/js-app/main.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/js-app/app.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/js/common/laytpl.js"></script>
<script type="text/javascript">
Ajax.baseUrl = __appBaseUrl;// 子窗口需告知app路径,否则ajax 提交无appBaseUrl
//-----------------------缓存数据全局变量--------------------------{{
var dlgArg = getDlgArgForMe();// 从父窗口获取数据
var saleOrderInfo = dlgArg.saleOrderInfo;
var shopDtoList=[];
//父窗口调用的方法
function getDlgResult() {
	var params={distShop:selectShopDto,saleOrder:saleOrderInfo};
	return params;
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
$(function() {
	loadShops();
	$(document).on("click", ".loadMore", function() {
		isLoadShopData = true;
		loadShops();
	});	

	// 加载地区
	bindRegionLists("province", "city", "county", null);
	
	var provinceId = null;
	$id("province").change(function() {
		provinceId = $(this).val();
		if ($(this).val() == "") {
			pageInfo.pagination.pageNumber = 1;
			pageInfo.filterItems = {};
			loadShops();
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
//----------------------------------------------------------------}}
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
	var saleOrderSvcs=saleOrderInfo.saleOrderSvcs;
	var svcxIds=[];
	for(var i=0;i<saleOrderSvcs.length;i++){
		var svcxId=saleOrderSvcs[i].svcId;
		svcxIds.push(svcxId);
	}
	var ajax = Ajax.post("/distshop/wxshop/list/get");
	pageInfo.pagination.pageNumber = pageNumber;
	pageInfo.filterItems.svcxIds=svcxIds;
	pageInfo.filterItems.ownerShopId=saleOrderInfo.shopId;
	ajax.data(pageInfo);
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			var totalCount = result.data.pagination.totalCount;
			selectShopDto=dlgArg.selectShopDto;
			var list = result.data.rows;
			if (isNull(list) || list.length == 0) {
				isLastPage = true;
				$(".loadMore>a").html("没有更多门店");
				return;
			}
						
			if (pageNumber > 1) {
				shopDtoList=shopDtoList.concat(list);
			} else {
				shopDtoList = list;
			}

			if (isNull(shopDtoList)) {
				isLastPage = true;
				$(".loadMore>a").html("没有更多门店");
				return;
			}
				
			renderHtml(shopDtoList, "shopListTpl", "shopsList");
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
	});
}

 

//根据区域过滤条件获取门店列表
function getShopsByRegion(regionId, level) {
	shopDtoList = [];// 清空缓存数据
	pageNumber = 1;
	var ajax = Ajax.post("/distshop/wxshop/list/get");
	var saleOrderSvcs=saleOrderInfo.saleOrderSvcs;
	var svcxIds=[];
	for(var i=0;i<saleOrderSvcs.length;i++){
		var svcxId=saleOrderSvcs[i].svcId;
		svcxIds.push(svcxId);
	}
	pageInfo.pagination.pageNumber = pageNumber;
	pageInfo.filterItems = {};
	pageInfo.filterItems.regionId = regionId;
	pageInfo.filterItems.level = level;
	pageInfo.filterItems.svcxIds = svcxIds;
	pageInfo.filterItems.ownerShopId=saleOrderInfo.shopId;
	ajax.data(pageInfo);
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			var totalCount = result.data.pagination.totalCount;
			shopDtoList = result.data.rows;
			renderHtml(result.data.rows, "shopListTpl", "shopsList");
			bindShopLiEvent();
		} else {
			Layer.warning(result.message);
		}
	});
	ajax.go();
}




</script>
</body>
<script type="text/html" id="shopListTpl" title="线下门店列表">
	{{# var shopDtos = d; }}
	{{# for(var i = 0, len = shopDtos.length; i < len; i++){ }}
		{{# var shop = shopDtos[i]; }}
		{{# var className = ""; }}
		{{# if(selectShopDto!=null&&selectShopDto.id==shop.id){ }}
			{{#className+="active"}}
		{{#}}}
		   		<li {{# if(className!=""){ }}class="{{className}}"{{#}}}>
                    <h1 shopId="{{shop.id}}">
					{{shop.name}}
					            
				</h1>
                    <dl class="pic-text">
                        <dt><img src="{{shop.fileBrowseUrl}}" title="{{shop.name}}" width="70" height="70"/></dt>
                        <dd>
                            <div>满意度：<span class="star"><i style="width: 90%"></i></span></div>
                            <div>此门店共完成134个单，有32条评论</div>
							<div>地址：<font id="address_{{shop.id}}">{{shop.regionName}}</font></div>
                        </dd>
                    </dl>
                    <i class="selected"></i>
                </li>
	{{# } }}
</script>


</html>