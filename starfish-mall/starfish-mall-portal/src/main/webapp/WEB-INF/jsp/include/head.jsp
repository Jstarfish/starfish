<%@include file="base.jsf" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <!-- <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"> -->

    <!-- Vendor Specific -->
    <!-- Set renderer engine for 360 browser -->
    <meta name="renderer" content="webkit">

    <!-- Cache Meta -->
    <meta http-equiv="Expires" content="0">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Pragma" content="no-cache">
    <link rel="shortcut icon" href="<%=resBaseUrl%>/image/favicon.ico" type="image/x-icon" />

    <!-- Style Sheet -->
    <link rel="stylesheet" href="<%=resBaseUrl%>/lib/jquery/jquery-ui.css" />
    <link rel="stylesheet" href="<%=resBaseUrl%>/css/libext/jquery.ext.css" />
    <link rel="stylesheet" href="<%=resBaseUrl%>/css-app/main.css" />
    <link rel="stylesheet" href="<%=resBaseUrl%>/css/libext/toolbar.css" />
    
    <link rel="stylesheet" href="<%=resBaseUrl%>/lib/jquery/jquery.datetimepicker.css" />

    <!--[if lt IE 9]>
    <script type="text/javascript" src="js/html5/html5shiv.js"></script>
    <![endif]-->
    <!-- Global Javascript -->
	<script type="text/javascript">
		var __appBaseUrl = "<%=appBaseUrl%>";
		//快捷方式获取应用url
		function getAppUrl(url){
			url = url || "";
			return url.indexOf("http") == 0 ?  url : __appBaseUrl + url;
		}
		var __resBaseUrl = "<%=resBaseUrl%>";
		//快捷方式获取资源url
		function getResUrl(url){
			url = url || "";
			return url.indexOf("http") == 0 ?  url : __resBaseUrl + url;
		}
	</script>

<title>亿投车吧</title>
</head>
<body>
<div class="page-wrapper">
	<div class="topbar">
		<div class="page-width clearfix">
            <ul id="district" class="quick-menu">
                <li class="nLi dropdown menu-district"><a href="<%=appBaseUrl%>/"><img src="<%=resBaseUrl%>/image-app/district.png" alt=""/><span>河北</span><i></i></a>
                    <em></em>
                    <%--div class="sub">
                        <ul class="sub-content">
                            <li><a href="#" class="selected">河北</a></li>
                        </ul>
                    </div> --%>
                </li>
                <!--<li class="nLi munu-none"><span>全国330城市已覆盖，10000家加盟店</span></li>-->
                <!--TODO 暂不处理-->
                <%--<li class="nLi"><a target="_blank" href="<%=AppNodeCluster.getCurrent().getAbsBaseUrlByRole("web-back") %>/merch/entering/jsp" target="_blank"><span>加盟合作</span>|</a></li>
                <li class="nLi"><a target="_blank" href="<%=AppNodeCluster.getCurrent().getAbsBaseUrlByRole("web-back") %>/merch/entering/jsp" target="_blank"><span>产品合作</span></a></li>--%>
            </ul>
            <ul id="quick-menu-cart" class="quick-menu">
                <li class="nLi"><a href="<%=appBaseUrl%>/ucenter/saleOrder/list/jsp"><span>我的订单</span>|</a></li>
                <li class="nLi"><a href="<%=appBaseUrl%>/ucenter/index/jsp"><span>用户中心</span>|</a></li>
                <li class="nLi dropdown shopping-cart"><a id="nLi-shopping-cart-a" href="<%=appBaseUrl%>/saleCart/list/jsp"><span>购物车（<b id="saleCartCountIndex">0</b>）</span><i></i></a><em></em>
                	<div class="sub">
                        <h1>最新加入的商品</h1>
                        <ul id="saleCart" class="sub-content">
                        	
                        </ul>
                        <!--<div class="content-none">
                            您的购物车中暂无商品，赶快选择心爱的商品吧！
                        </div>-->
                        <div class="sub-bottom">
                            <div class="sum">共<span id="svcCountIndex" class="num"></span>个服务,<span id="goodsCountIndex" class="num"></span>个商品 总金额：<span id="finalAmountIndex" class="price"></span></div>
                            <input id="goSaleCart" class="btn btn-w70" type="button" value="去购物车"/>
                        </div>
                    </div>
                </li>
            </ul>
            <ul id="quick-menu" class="quick-menu">
                <li class="nLi"><a href="<%=appBaseUrl%>/user/login/jsp"><span> 登录 </span>|</a></li>
                <li class="nLi"><a href="<%=appBaseUrl%>/user/regist/jsp"><span> 注册 </span>|</a></li>
            </ul>
		</div>
	</div>
	<div class="header">
		<div class="page-width">
			<a class="logo" href="<%=appBaseUrl%>/" id="logoImg"></a>
            <div class="search-section">
                <div class="search">
                    <input class="search-input" type="text" placeholder="户外用品名称、品牌、零件号" id="keySearch"><input class="btn search-btn" type="button" id="btnToSearch" value="搜索" />
                </div>
                <%--div class="search-keyword">
                    热门搜索：<a href="javascript:toKeySearch('洗车');">洗车</a>|<a href="javascript:toKeySearch('户外用品');">户外用品</a>|<a href="javascript:toKeySearch('XX品牌');">XX品牌</a>
                </div> --%>
            </div>
            <div class="fr" style="margin-top: 8px">
                <img src="<%=resBaseUrl%>/image-app/slogan.png" alt=""/>
            </div>
		</div>
	</div>
    <div class="navbar">
        <div class="page-width">
            <%--ul id="nav1" class="nav" style="display: none">
                <li class="nLi fast-channel on">
                   <h3><a href="javascript:;">快速通道</a></h3>
                   <ul class="sub">
                       <li class="first" name="trackItem"><a href="<%=appBaseUrl%>/carSvc/list/jsp"><i class="icon1"></i>车辆洗美</a>
                           <div class="fast-cont" name="trackItemChild">
                               <div class="fast-bg"></div>
                               <div class="fast-supermarket">
                                   <img src="<%=resBaseUrl%>/image-app/temp/fast-adv1.jpg" alt=""/>
                                   <div class="mod-search">
                                       <input class="search-input" type="text" placeholder="车品"><input class="btn search-btn" type="button" />
                                       <div class="search-keyword">
                                           <a href="javascript:;">精品户外</a>|<a href="javascript:;">自驾必备套装</a>
                                       </div>
                                   </div>
                               </div>
                           </div>
                       </li>
                       <li name="trackItem"><a href="<%=appBaseUrl%>/product/supermarket/list/jsp"><i class="icon2"></i>车品超市</a>
                           <div class="fast-cont" name="trackItemChild">
                               <div class="fast-bg"></div>
                                <div class="fast-maintain" >
                                   <div class="maintain-cont">
                                   </div>
                               </div>
                           </div>
                       </li>
                   </ul>
                </li>
            </ul> --%>
            <ul id="nav" class="nav">
                <li class="nLi on">
                    <h3><a href="<%=appBaseUrl%>/">首页</a></h3>
                </li>
                <li class="nLi">
                    <h3><a href="<%=appBaseUrl%>/ecard/list/jsp">特惠E卡</a></h3>
                </li>
                <li class="nLi">
                    <h3><a href="<%=appBaseUrl%>/carSvc/list/jsp">车辆洗美</a></h3>
                </li>
                <li class="nLi">
                    <h3><a href="<%=appBaseUrl%>/product/supermarket/list/jsp">尖品超市</a></h3>
                </li>
                <li class="nLi">
                    <h3><a href="<%=appBaseUrl%>/shop/list/jsp">线下门店</a></h3>
                </li>
                <li class="nLi">
                    <h3><a href="<%=appBaseUrl%>/social/blog/list/jsp">车友分享</a></h3>
                </li>
                <li class="nLi dropdown appdown" title="试用内测版">
                    <h3><a href="javascript:;">下载APP<img src="<%=resBaseUrl%>/image-app/appdown.png" /></a></h3>
                    <ul class="sub">
                        <li>
                            <img src="<%=appBaseUrl%>/xutil/qrCode.do?width=200&height=200&url=http%3A%2F%2Fopenbox.mobilem.360.cn%2Findex%2Fd%2Fsid%2F3181998" alt="下载APP"/>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>