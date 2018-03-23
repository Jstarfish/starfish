<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@include file="../include/base.jsf" %>
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
                <li class="nLi"><a target="_blank" href="<%=AppNodeCluster.getCurrent().getAbsBaseUrlByRole("web-back") %>/merch/entering/jsp" target="_blank"><span>加盟合作</span>|</a></li>
                <li class="nLi"><a target="_blank" href="<%=AppNodeCluster.getCurrent().getAbsBaseUrlByRole("web-back") %>/merch/entering/jsp" target="_blank"><span>产品合作</span></a></li>
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
	        <a class="logo" href="<%=appBaseUrl%>/"><img src="<%=resBaseUrl%>/image/logo.png" alt="车logo" /></a>
	        <div class="head-steps">
	        	<ul class="steps">
		            <li class="actived"><i>1</i><span>我的购物车</span><em></em></li>
		            <li class="active"><i>2</i><span>填写核对订单</span><em></em></li>
		            <li class="end"><i>3</i><span>订单提交成功</span></li>
		        </ul>
	        </div>
	    </div>
	</div>
<div class="content" id="content" style="display:none">
	<div class="page-width">
		<div class="mod-main2">
			<div class="mod-main2-tit">
				<h1>填写核对订单</h1><a href="javascript:void(0)" id="btnBack" class="anormal" style="float:right">返回修改购物车>></a>
			</div>
			<div class="cart-detail">
				<div class="cart-detail-con1">
					<table class="order-td shopping-td">
						<thead>
			            <tr>
			                <th colspan="2"><div class="text-left"><b>服务/商品详情</b></div></th>
			                <th width="100">单价</th>
			                <th width="100">数量</th>
			                <th width="100">小计</th>
			                <th width="55">状态</th>
			            </tr>
			            </thead>
						<tbody id="carSvcGoods">
						</tbody>
						<tbody id="optGoods">
						</tbody>
					</table>
				</div>
			</div>
			<div class="cart-detail">
				<h1 class="cart-detail-tit">
					服务门店<a href="javascript:void(0)" id="btnShopSelect" class="anormal" style="margin-left: 10px">[选择]</a>
				</h1>
				<div class="cart-detail-con">
					<div class="ml50" id="shopAddress" ><font style="font-style: italic">您还未选择服务门店</font></div>
					<input type="hidden" name="shopId" id="shopId" />
				</div>
			</div>
			<div class="cart-detail">
				<h1 class="cart-detail-tit">预约时间</h1>
				<div class="cart-detail-con">
					<div class="ml50">
						<input class="inputx inputx-h26" type="text" placeholder="预约时间"
							name="planTime" id="planTime" readonly="true" />
					</div>
				</div>
			</div>
			<div class="cart-detail">
				<h1 class="cart-detail-tit">
					联系人信息
					<div class="select-special select-contacts" style="margin-left: 15px">
						<span class="select-special-l">选择联系人</span> <span
							class="select-special-r"><i></i></span>
						<div class="clear"></div>
						<ul id="linkManList" class="linkManList">
						</ul>
					</div>
					<!--end-select-->
				</h1>
				<div class="cart-detail-con clearfix">
					<div id="addLinkManDiv" class="ml50">
						<input class="inputx inputx-h26" type="text" placeholder="手机号码" name="phoneNo" id="phoneNo" maxlength="11" />
						<span class="red ml20" id="phoneNoTip" style="display: none"></span>&nbsp;&nbsp;
						<input class="inputx inputx-h26" type="text" placeholder="联系人姓名" name="linkMan" id="linkMan" />
						<span class="red ml20" id="linkManTip" style="display: none"></span>	
						<input class="btn-normal" type="button" value="+加入常用联系人" id="btnAddLinkMan" style="display: none" />
					</div>
				</div>
				<div class="cart-detail">
	                <h1 class="cart-detail-tit">车辆信息
	                    <!--select-->
	                    <div class="select-special select-car" style="margin-left:29px;">
	                        <span class="select-special-l">请选择车辆</span>
	                        <span class="select-special-r"><i></i></span>
	                        <div class="clear"></div>
	                        <ul id="userCarList">
	                        </ul>
	                    </div>
	                    <!--end-select-->
	                    <a href="javascript:void(0);" class="anormal a1" id="btnAddUserCar"><span class="red" >+</span>添加车辆</a>
	                </h1>
	                <div class="cart-detail-con" style="padding-left: 102px;" id="userCar">
	               		<font style="font-style: italic">您还未选择车辆</font>
	                </div>
	            </div>
				<div class="cart-detail">
					<h1 class="cart-detail-tit">服务方式</h1>
					<div class="cart-detail-con">
						<dl class="labels">
							<dt>
								<label class="label">到店自提</label>
							</dt>
							<dd></dd>
						</dl>
					</div>
				</div>
				<div class="cart-detail">
					<h1 class="cart-detail-tit">支付及其它</h1>
					<div class="cart-detail-con">
						<dl class="labels" id="couponDl">
							<dt>
								<label>优惠券：</label>
							</dt>
							<dd>
								<div class="checkbox-analog">
										<input type="hidden" name="couponId" id="couponId" />
								</div>
								<a href="javascript:void(0)" class="anormal" id="btnCouponSelect">[ 选择 ]</a>
								<div class="coupon-selected" id="coupon-selected"
									style="display: none">
									<p id="svcCouponShow"></p>
									<p id="proCouponShow"></p>
									<p>
										优惠券优惠金额 <b class="red" id="couponPrice">0.00</b> 元
									</p>
								</div>
							</dd>
						</dl>
						<dl class="labels" id="ecardDl">
							<dt>
								<label>E卡支付：</label>
							</dt>
							<dd>
								<div class="checkbox-analog">
									<input type="hidden" name="ecardId" id="ecardId" /> 
								</div>
								<a href="javascript:void(0)" class="anormal" id="btnEcardSelect">[ 选择 ]</a>
								<div class="coupon-selected" id="ecard-selected" style="display:none">
								<!-- 	<table class="order-td consume-td border-no">
										<tbody>
											<tr id="ecardShowTr">
											</tr>
										</tbody>
									</table> -->
									<p id="ecardShowTr">
										
									</p>
									<p>
										E卡支付 <b class="red" id="ecardPrice">0.00</b> 元
									</p>
								</div>
							</dd>
						</dl>
						<dl class="labels" id="payPasswordDl" style="display:none">
							<dt>
								<label>支付密码：</label>
							</dt>
							<dd>
								<input class="inputx inputx-h26" type="password" placeholder="支付密码" name="payPassword" id="pPassword" />
								<a href="javascript:void(0)" id="btnSettingPayPassword" class="anormal"  style="display:none;color:red">您还没有支付密码，点击设置>></a>
								<a  href="javascript:void(0)" id="forgetPayPassword">忘记密码？</a>
							</dd>
						</dl>
						<dl class="labels" id="paywayDl">
							<dt>
								<label>支付方式：</label>
							</dt>
							<dd>
								<input type="hidden" name="payWay" id="payWay" />
								<div class="checkbox-analog" id="paywayList"></div>
							</dd>
						</dl>
					</div>
				</div>
				<div class="cart-detail">
					<h1 class="cart-detail-tit">应付详情</h1>
					<div class="cart-detail-con clearfix">
						<dl  class="cart-amount">
							<dt>商品合计：</dt>
							<dd id="goodsAmount">¥ 0</dd>
						</dl>
						<dl   class="cart-amount">
							<dt>服务合计：</dt>
							<dd id="svAmountShow"></dd>
						</dl>
						<dl id="couponAmountDl" class="cart-amount" style="display: none">
							<dt>
								<font id="couponTagetTypeFont"></font>优惠券：
							</dt>
							<dd id="couponAmount">-¥ 00.00</dd>
						</dl>
						<dl  id="ecardAmountDl" class="cart-amount" style="display: none">
							<dt>亿投车吧E卡支付：</dt>
							<dd id="ecardAmount">¥ 0</dd>
						</dl>
					</div>
				</div>
			</div>
		</div>
		<div id="cartToolbar" class="cart-toolbar">
			<div class="page-width">
				<div class="fr">
					<span class="total-price">应付金额： <span class="red"
						id="totalAmount">¥ 0.00</span></span> <input class="btn btn-h40"
						type="button" value="提交订单" id="btnSubmit" />
				</div>
					<div class="fr" style="float:left ; padding-top:5px;padding-left:32px;">
					<span >订单留言： </span><span>
					<input class="inputx inputx-h26 inputx-w400" type="text" name="leaveMsg" id="leaveMsg" placeholder="订单留言" maxlength="30" /></span>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
	<script type="text/javascript"	src="<%=resBaseUrl%>/lib/jquery/jquery.datetimepicker.js"></script>
	<!-- 加密专用js -->
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/bigint.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/barrett.js"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js/common/rsa.js"></script>
	<script type="text/javascript" src="<%=appBaseUrl%>/js/encrypt/get"></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/saleOrder/common.js?v=<%=System.currentTimeMillis() %>" ></script>
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/saleOrder/submit.js?v=<%=System.currentTimeMillis() %>" ></script>
	</body>
	<!-- 加载商品服务相关模板 start -->
<script type="text/html" id="saleCartRowTpl">
 {{# var cartSvList = d; }}
 {{# if(!isNoB(cartSvList)){ }}
 <tr class="title">
	<th colspan="7">
	    <div class="text-left">
	 <label class="mr20">车辆洗美</label>
	    </div>
	</th>
	   </tr>
	{{# for(var i=0,len=cartSvList.length; i < len; i++) {}}
		{{#  var cartSv=cartSvList[i];}}
	<tr>
		<td colspan="2">
	    <dl class="goods-item goods-item2">
	 <dt><a href="javascript:void(0)"><img src="{{cartSv.svcxAlbumImg}}" alt=""/></a></dt>
	 <dd class="text">
	 	<a href="">{{cartSv.carSvcName}}<br />
	 		<span class="gray1">{{cartSv.desc}}</span>
	 	</a>
	 </dd>
	    </dl>
		</td>
	<td>{{cartSv.salePrice}}</td>
	<td>
	    x 1
	</td>
<td>
{{#if(cartSv.amountInfo.discAmount>0){}}
<div style="text-decoration: line-through;">{{cartSv.salePrice}}</div>
<div><span class="red price1" style="font-size: 14px; display: inline-block; vertical-align: middle;">{{cartSv.amountInfo.amount}}</span></div>
<a title="会员专享折扣，折扣：{{cartSv.amountInfo.discAmount}}" href="javascript:;" class="hy_discount">会员折扣</a>
{{#}else{}}
<span class="red price1" style="font-size: 14px; display: inline-block; vertical-align: middle;">{{cartSv.salePrice}}</span>
{{#}}}
</td>
<td><font color="green">正常</font></td>
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
        <label class="mr20">尖品超市</label>
    </div>
</th>
</tr>
{{# if(!isNoB(optSvGoods)){ }}
{{# for(var j=0,len=optSvGoods.length; j < len; j++) { }}
	{{# var goods = optSvGoods[j]; }}
    {{# goodsMap.add(goods.goodsId,goods.productAmount); }}
<tr>
<td colspan="2">
    <dl class="goods-item goods-item2">
        <dt><a href="{{__appBaseUrl}}/product/detail/jsp?productId={{goods.productId}}"><img src="{{goods.productAlbumImg}}" title="{{goods.productName}}"/></a></dt>
        <dd class="text"><a href="{{__appBaseUrl}}/product/detail/jsp?productId={{goods.productId}}" target="_blank" title="{{goods.productName}}">{{goods.productName}}</a></dd>
    </dl>
</td>
<td>{{goods.productAmount}}</td>
<td>x {{goods.quantity}}</td>
<td><span class="red price1" style="font-size: 14px; display: inline-block; vertical-align: middle;">{{goods.amountInfo.amount}}</span></td>
<td id="status_{{goods.productId}}"><font color="green">有货</font></td>
</tr>
{{# } }}
{{# } }}
{{# } }}
</script>
	<!-- 加载商品服务相关模板  end -->
	<!-- 其他模板 start -->
	<!-- 店铺选择 -->
	<script type="text/html" id="selectShopTpl">
		{{# var selectShopDto = d; }}
		{{selectShopDto.shop.name}}<font color="#444444">（{{selectShopDto.shop.regionName}}）</font>
	</script>
<script type="text/html" id="linkManRowTPL" title="联系人列表">
{{# var linkWays = d; }}
{{# if(isUndef(linkWays)||isNull(linkWays)||linkWays.length==0){ }}
 <li style="color:red">您还未添加联系人</li>
{{# }else{ }}
{{# for(var i=0,len=linkWays.length; i < len;i++){}}
{{# var linkWay=linkWays[i];}}
<li>
    <a href="javascript:;">
        <span class="linkMan">{{linkWay.linkMan}}</span>
        <span class="phoneNo">{{linkWay.phoneNo}}</span>
    </a>
</li>
{{# }}}
{{# }}}
</script>
<script type="text/html" id="userCarListTPL">
{{# var userCars = d; }}
{{# if(isUndef(userCars)||isNull(userCars)||userCars.length==0){ }}
 <li style="color:red">您还未添加车辆</li>
{{# }else{ }}
{{# for(var i=0,len=userCars.length; i < len;i++){}}
{{# var userCar=userCars[i];}}
 <li data-id="{{userCar.id}}">
	<a href="javascript:;" >
	{{ userCar.name }} {{ userCar.carBrand.name }} {{ userCar.carSerial.name }} {{ userCar.carModel.name }}
    </a>
</li>
{{# }}}
{{# }}}
</script>
<script type="text/html" id="userCarTpl">
{{# var userCar = d; }}
{{# if(isUndef(userCar)||isNull(userCar)){ }}
	<font style="font-style: italic">您还未选择车辆</font>
{{# }else{ }}
{{ userCar.name }} {{ userCar.modelName }}
{{# } }}
</script>
	<script type="text/html" id="paywayRowTpl">
{{# var payways=d; }}
{{# for(var i=0,len=payways.length; i < len;i++){}}
	{{#var payway=payways[i]}}
<a class="mr20 {{# if(i==0){ }}active{{# } }}" href="javascript:;" data-code="{{payway.code}}" ><img src="{{resBaseUrl}}/image-app/{{payway.code}}.png" title="{{payway.name}}"><i></i></a>
{{# } }}
</script>
	<!-- 其他模板 end -->
	<!-- 优惠券模板start -->
	<!--弹出-优惠券-start-->
	<script type="text/html" id="couponRowTpl">
{{# var couponMap=d.couponMap; }}
{{# var type=d.type; }}
{{# if(!isUndef(couponMap)){ }}
<ul>
{{# for(var i=0,keys=couponMap.keys(),len=keys.length;i<len;i++){ }}
{{# var key=keys[i];}}
{{# var coupon=couponMap.get(key);}}
<li>
<span>
{{# if(coupon.targetType=="svc"){ }}
{{coupon.svcName}}（服务）
{{# }else if(coupon.targetType=="goods"){ }}
{{coupon.productName}}（商品）
{{# } }}
    {{couponType.get(coupon.type)}}</span>
<span>价格：
    {{# if(coupon.price==0){ }}
    	免付¥ {{coupon.payAmount}}
    {{# }else{ }}
		省去¥ {{coupon.payAmount}}
	{{# } }}
</span>
<span><a class="delete couponSelect" href="javascript:void(0)" 
	{{# if(coupon.targetType=="svc"){ }}
data-id="{{coupon.svcId}}"
{{# }else if(coupon.targetType=="goods"){ }}
data-id="{{coupon.productId}}"
{{# } }}
 data-type="{{coupon.targetType}}"  ></a></span>
</li>
{{# } }}
</ul>
{{# } }}
</script>
<div id="couponSelectDlg" class="popup-select" style="display: none;">
			 <div class="declare">
     服务优惠券
        <span class="gray fr"></span>
    </div>
		<table class="order-td consume-td">
				<tr class="title title1">
					<td  width="65">优惠券</td>
					<td width="47">价格</td>
					<td width="47">省去</td>
					<td>具体限制</td>
					<td width="230">有效期</td>
				</tr>
				<tbody id="svcCouponList">
				</tbody>
					</table>
					 <div class="declare">
      商品优惠券
        <span class="gray fr"></span>
    </div>
					<table class="order-td consume-td">
					<tr class="title title1">
						<td  width="65">优惠券</td>
					<td width="47">价格</td>
					<td width="47">省去</td>
					<td>具体限制</td>
					<td width="230">有效期</td>
				</tr>
				<tbody id="proCouponList">
				</tbody>
		</table>
	</div>
<script type="text/html" id="couponListTpl">
{{# var couponMap = d.couponMap; }}
{{# if(isUndef(couponMap)){ }}
<tr><td colspan="5">没有符合条件的优惠券</td></tr>
{{# }else{ }}
{{# var keys = couponMap.keys(); }}
{{# var len = keys.length; }}
{{# if(len==0||couponMap=={}||couponMap.toString()=="{}"){}}
	<tr><td colspan="5">没有符合条件的优惠券</td></tr>
{{# }else{ }}
{{# var type=d.type}}
{{# for(var i=0;i<len;i++){ }}
{{# var id=keys[i];}}
{{# var userCouponList=couponMap.get(parseInt(id));}}
{{# for(var j=0,glen=userCouponList.length;j<glen;j++){ }}
	{{# var coupon=userCouponList[j]; }}
	{{# coupon.targetType=type; }}
	{{# var couponSelect=null; }}
	{{# if(coupon.targetType=="goods"){ }}
		{{# couponSelect=selectProCouponMap.get(coupon.productId)}}
	{{# }else if(coupon.targetType=="svc"){}}
		{{# couponSelect=selectSvcCouponMap.get(coupon.svcId)}}
	{{#} }}
	{{# userCouponComputing(coupon);}}
	{{# var color="#ffcc33"}}
	{{# if(coupon.type=="nopay"){ }}
		{{# color="green"}}
	{{# }else if(coupon.type=="sprice"){ }}
		{{# color="red"}}
	{{# }else if(coupon.type=="deduct") { }}
		{{# color="orange"}}
	{{# } }}

<tr  {{# if(couponSelect!=null&&couponSelect.id==coupon.id){ }}class="tr-selected"{{# } }}>
	<td style="color:{{color}};"><div class="text-left"><input 
{{# if(coupon.targetType=="goods"){ }}
name="coupon" data-type="goods" data-id="{{coupon.productId}}"
{{# }else if(coupon.targetType=="svc"){}}
	name="coupon" data-type="svc" data-id="{{coupon.svcId}}"
{{#} }} type="checkbox" value="{{coupon.id}}" {{# if(couponSelect!=null&&couponSelect.id==coupon.id){ }}checked{{# } }}/>{{couponType.get(coupon.type)}}</div></td>
	<td style="color:{{color}};width:47px">
	{{# if(coupon.price==0){ }}
    	免付
    {{# }else{ }}
		¥ {{coupon.price}}
	{{# } }}
	</td>
<td style="color:{{color}};width:47px">
		¥ {{coupon.payAmount}}
	</td>
	<td >
{{# if(coupon.targetType=="goods"){ }}
	{{coupon.productName}}
{{# }else if(coupon.targetType=="svc"){}}
	{{coupon.svcName}}
{{#} }}
	</td>
	<td style="width:252px">{{coupon.startTime}} 至 {{coupon.endTime}}</td>
</tr>
		{{# } }}
{{# } }}
{{# } }}
{{# } }}
</script>
	<!-- 优惠券模板end -->
	<!-- e卡模板start -->
	<!--弹出-e卡-start-->
	<div id="ecardSelectDlg" class="popup-select" style="display: none;">
		<div class="declare">
			您一次只能使用1张E卡支付 <span class="gray fr">E卡金额合并请到个人中心“卡包”操作</span>
		</div>
		<table class="order-td consume-td">
			<tbody id="ecardList">
			</tbody>
		</table>
	</div>
	<script type="text/html" id="ecardListTpl">
<tr class="title title1" id="ecardTh">
	<td>亿投车吧E卡卡号</td>
	<td>绑定</td>
	<td>面值</td>
	<td>余额</td>
	<td>本次使用</td>
</tr>
{{# var ecards = d,j=0,showSize=0;}}
{{# if(isUndef(ecards)||isNull(ecards)||ecards.length==0){ }}
	<tr><td colspan="5">没有符合条件的e卡</td></tr>
{{# }else{ }}
{{# for(var i=0,len=ecards.length;i<len;i++){}}
	{{# var ecard=ecards[i];}}
	{{# thisUseComputing(ecard);}}
{{# if(ecard.payAmount>0){}}
{{#showSize++;}}
<tr {{# if(ecardSelect!=null&&ecardSelect.id==ecard.id){ }}class="tr-selected"{{# } }}>
	<td><div class="text-left"><input name="ecard" type="checkbox" value="{{ecard.id}}" {{# if(ecardSelect!=null&&ecardSelect.id==ecard.id){ }}checked{{# } }} /> {{ecard.cardNo}}</div></td>
	<td>
	{{# if(ecard.shopId==null){ }}
		亿投车吧
	{{# }else{ }}
		<a href="javascript:void(0);" class="shopDetailUrl" shopId="{{ecard.shopId}}">{{ecard.shopName}}</a>
	{{# } }}
	</td>
	<td style="color:blue">¥ {{ecard.faceValue}}</td>
	<td style="color:green">¥ {{ecard.remainVal}}</td>
	<td style="color:red">
		¥ {{ecard.payAmount}}
	</td>
</tr>
{{# } }}
{{# } }}
{{# if(showSize==0){}}
<tr><td colspan="5">没有符合条件的e卡</td></tr>
{{#}}}
{{# } }}
</script>
<script type="text/html" id="ecardRowTpl">
{{# var ecard=d; }}
<span>卡号：{{ecard.cardNo}}</span>
<span>余额：¥ {{ecard.remainVal}}</span>
<span>
<a class="delete" href="javascript:void(0)" id="ecardSelect"></a>
</span>
</script>
	<!-- e卡模板end -->
	<script type="text/html" id="updatePayPasswordTpl">
	    <div>
           <h2 style="padding: 10px 15px" id="updatePayPasswordTitle">修改支付密码</h2>
           <div>
               <dl class="labels labels1 name">
                   <dt><label class="label required">手机号码：</label></dt>
                   <dd>
                    <input class="inputx inputx-reg" type="text" id="payPhoneNo" maxlength="20" placeholder="请输入手机号" disabled/>
                    <span class="red" id="phoneNoTip"></span>
                   </dd>
               </dl>
				<dl class="labels labels1">
                   <dt><label class="label required">短信验证码：</label></dt>
                   <dd style="position: relative;">
 					<input class="inputx inputx-vf" type="text" placeholder="请输入短信验证码" id="phoneCode" maxlength="6" width="150px;"/>
					<input class="btn-normal btn-vf" type="button" value="获取短信验证码" id="btnSendSmsCode" style="top: 1px;"/>&nbsp;
                    <span class="red" id="smsCodeTip"></span>
                   </dd>
               </dl>
               <dl class="labels labels1 keyword">
                   <dt><label class="label required">支付密码：</label></dt>
                   <dd>
                    <input class="inputx inputx-reg" type="password" id="payPassword" maxlength="30" placeholder="请输入8位以上密码" />&nbsp;
                    <span class="red" id="payPasswordTip"></span>
                   </dd>
               </dl>
               <dl class="labels labels1 keyword">
                   <dt><label class="label required">确认支付密码：</label></dt>
                   <dd>
                    <input class="inputx inputx-reg" type="password" id="rePayPassword" maxlength="30" placeholder="请重复输入密码" />&nbsp;
                    <span class="red" id="rePayPasswordTip"></span>
                   </dd>
               </dl>
               <dl class="labels labels1">
                   <dt></dt>
                   <dd><input class="btn btn-h40" type="button" value="确定" onclick="doUpdatePayPassword()" /></dd>
               </dl>
           </div>
       </div>
</script>
 	<div   id="addUserCarTpl" style="display:none" style="margin-top: 10px">
	 	<dl class="dl-leftright" >
        		<dt><img src="<%=appBaseUrl%>/static/image-app/temp/car1.jpg" alt="" id="carImage"/></dt>
				<dd>
     				<dl class="labels babels1">
         				<dt><label class="label required">车辆名称：</label></dt>
         				<dd><input class="inputx inputx-h26" type="text" id="name" maxlength="30"/><span class="red ml20" id="linkManTip"></span></dd>
     				</dl>
     				<dl class="labels">
        		 		<dt><label class="label required">品牌：</label></dt>
         				<dd><select id="brandId" class="field value" style="border: 1px solid #ccc; height: 28px; width: 143px; padding: 3px 5px;"><option value="" title="- 请选择 -"></option></select><span class="red ml20" id="phoneNoTip" ></span></dd>
     				</dl>
     				<dl class="labels">
         				<dt><label class="label required">车系：</label></dt>
         				<dd><select class="field value" id="serialId" style="border: 1px solid #ccc; height: 28px; width: 143px; padding: 3px 5px;"><option value="" title="- 请选择 -"></option></select><span class="red ml20" id="emailTip"></span></dd>
     				</dl>
    				<dl class="labels">
       		 			<dt><label class="label required">车型：</label></dt>
        				<dd><select class="field value" id="modelId" style="border: 1px solid #ccc; height: 28px; width: 143px; padding: 3px 5px;"><option value="" title="- 请选择 -"></option></select></dd>
    				</dl>
				</dd>
    		</dl>
    </div>		
    <script type="text/html" id="svAmountShowTpl">
    {{# var saleCar=d;}}
	{{# if(saleCart.amountInfo.discAmount>0){ }}
		<div>
		<a title="会员专享折扣" href="javascript:;" class="hy_discount mr10" style="display: none;"  id="userDiscount">会员折扣</a>
		<span class="red price1 mr10" style="font-size: 14px; display: inline-block; vertical-align: middle;" id="svAmount">￥{{saleCart.svcAmount}}</span>
		<span style="text-decoration: line-through;display:none" id="oldSvAmount">￥{{saleCart.oldSvcAmount}}</span>
		</div>
	{{# }else{ }}
		￥{{saleCart.svcAmount}}
	{{# } }}
	</script>
	</html>