<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/base.jsf"%><jsp:include
	page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
<div class="content">
    <div class="page-width">
        <div class="mod-main2">
            <div class="mod-main2-tit">
                <h1>填写核对订单</h1>
            </div>
            <div class="cart-detail">
                <h1 class="cart-detail-tit">商品清单</h1>
                <div class="cart-detail-con1">
                    <!--e卡详情-start-->
                    <table class="order-td">
                        <tbody id="eCardInfo">
                        </tbody>
                    </table>
                    <!--e卡详情-end-->
                </div>
                <div id="actRule" class="mb20" style="margin-top: -10px;">
                	
                </div>
            </div>
            <div class="cart-detail">
                <h1 class="cart-detail-tit">支付及其它</h1>
                <div class="cart-detail-con">
                    <dl class="labels">
                        <dt><label>支付方式：</label></dt>
                        <dd>
                            <div class="checkbox-analog" id="payWayList">
                            </div>
                        </dd>
                    </dl>
                </div>
            </div>
            <div class="cart-detail">
                <h1 class="cart-detail-tit">应付详情</h1>
                <div class="cart-detail-con clearfix">
                    <dl class="cart-amount">
                        <dt>商品总金额：</dt>
                        <dd id="goodsAmount"></dd>
                    </dl>
                    <dl class="cart-amount">
                        <dt>在线支付：</dt>
                        <dd id="payAmount"></dd>
                    </dl>
                </div>
            </div>
        </div>
    </div>
    <div id="cartToolbar" class="cart-toolbar">
        <div class="page-width">
            <div class="fr">
                <span class="total-price">应付金额： <span class="red"></span></span>
                <input id="submitOrder" class="btn btn-h40" type="button" value="提交订单" />
            </div>
            <div class="fr" style="float:left ; padding-top:5px;padding-left:32px;">
				<span >订单留言： </span>
				<span><input class="inputx inputx-h26 inputx-w400" type="text" id="orderMessage" placeholder="订单留言" maxlength="30" /></span>
			</div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
//------------------------------------------缓存支付方式---------------------------------------------------
var _payWay=null;
var _shopId=null;
var _shopName=null;
//------------------------------------------初始化--------------------------------------------------------
	$(function() {
		makeBtmSticky("cartToolbar", 300);
	    var ecard=extractUrlParams(location.href,true);
		if (ecard != null && ecard.code != null) {
			if(ecard.shopId!=null){
				_shopId=ecard.shopId;
			}
			_shopName = ecard.shopName;
			//decodeURIComponent()
			ajaxPost(ecard.code);
			getEcardActRule(ecard.code);
			initPayWay();
		}
	});
	//------------------------------------------加载页面请求----------------------------------------------------
	function ajaxPost(code) {
		var ajax = Ajax.post("/eCardOrder/submit/info/get");
		var formData = {
				code : code
			};
		ajax.data(formData);
		ajax.done(function(result, jqXhr) {
			if (result != "" && result.type == "info") {
				var eCard = result.data;
				if (eCard != null) {
					//订单状态
					renderHtml(eCard, "eCardRowTpl", "eCardInfo");
					initECard(eCard);
				}
				renderNumSpinner("numSpinner", doOnNumChange, 1, 1,100);
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	function getEcardActRule(code){
		var ajax = Ajax.post("/market/ecardActRules/get/by/code");
		var formData = {
				code : code
			};
		ajax.data(formData);
		ajax.done(function(result, jqXhr) {
			if (result != "" && result.type == "info") {
				var ruleList = result.data;
				if (ruleList != null) {
					//订单状态
					renderHtml(ruleList, "ruleListRowTpl", "actRule");
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	//-----------------------------------------------初始化处理-----------------------------------------------
	function initECard(eCard){
		//初始金额
		$id("goodsAmount").html("¥&nbsp;"+eCard.price);
		$id("payAmount").html("¥&nbsp;"+eCard.price);
		$(".total-price span").html("¥&nbsp;"+eCard.price);
		//初始支付方式(默认银联，暂定)
		_payWay="alipay";
		//
		if(!isUndef(_shopName) && _shopName!=null){
			$id("shopName").html("&nbsp;&nbsp;（此卡适用于平台全商品及&nbsp;"+_shopName+"&nbsp;服务消费）");
		}
	}
	function initPayWay() {
		var ajax = Ajax.post("/setting/payWay/usbale/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var paywayList = result.data;
				if(paywayList!=null &&paywayList.length>0){
					renderHtml(paywayList, "paywayRowTpl", "payWayList");
				}
			}
		});
		ajax.go();
	}
	//----------------------------------------------业务处理-----------------------------------------------
	//计算金额
	function doOnNumChange(numValue, htDom) {
		var price = $(htDom).attr("data-id");
		if(price){
			var saleAmount=price*numValue;
			//小计
			$id("saleAmount").html("¥&nbsp;"+saleAmount);
			//商品金额
			$id("goodsAmount").html("¥&nbsp;"+saleAmount);
			//支付金额
			$id("payAmount").html("¥&nbsp;"+saleAmount);
			//应付金额
			$(".total-price span").html("¥&nbsp;"+saleAmount);
		}else{
			Layer.warning("异常");
		}
	}
	//选择支付方式
	$(".checkbox-analog").on("click","a",function() {
		//获取支付方式
		var payWay=$(this).attr("data-id");
		//缓存支付方式
		_payWay=payWay;
		//选中改变样式
		$(".checkbox-analog a").removeClass("active");
		$(this).addClass("active");
		//
	});
	//提交订单
	$id("submitOrder").click(function() {
		//获取当前订单code(暂时支持一种卡订单)
		var code=$("div[name='numSpinner']").attr("id");
		//获取当前数量
		var quantity=$("div[name='numSpinner'] >input").val();
		//获取支付方式
		var payWay=_payWay;
		//获取订单留言
		var orderMessage=$id("orderMessage").val();
		//
		var eOrderInfo={"code":code,"quantity":quantity,"payWay":payWay,"orderMessage":orderMessage};
		if(_shopId!=null){
			eOrderInfo.shopId=_shopId;
		}
		submitPost(eOrderInfo);
	});
	
	function submitPost(eOrderInfo){
		var hintBox = Layer.progress("正在提交数据...");
		var ajax = Ajax.post("/eCardOrder/submit/do");
		ajax.data(eOrderInfo);
		ajax.done(function(result, jqXhr) {
			if (result != "" && result.type == "info") {
				var orderId=result.data;
				location.replace(getAppUrl("/eCardOrder/submit/result/jsp?orderId="+orderId));
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.always(function() {
			hintBox.hide();
		});
		ajax.go();
	}
	//-------------------------------------------渲染页面------------------------------------------------
	//渲染页面内容
	function renderHtml(data, fromId, toId) {
		//获取模板内容
		var tplHtml = $id(fromId).html();

		//生成/编译模板
		var theTpl = laytpl(tplHtml);

		//根据模板和数据生成最终内容
		var htmlStr = theTpl.render(data);

		//使用生成的内容
		$id(toId).html(htmlStr);
	}
</script>
</body>
<script type="text/html" id="paywayRowTpl">
{{# var payways=d; }}
{{# for(var i=0,len=payways.length; i < len;i++){}}
	{{#var payway=payways[i]}}
	<a data-id="{{payway.code}}" class="mr20 {{# if(i==0){ }}active{{# } }}" href="javascript:void(0);"><img src="<%=resBaseUrl%>/image-app/{{payway.code}}.png" title="{{payway.name}}"><i></i></a>
{{# } }}
</script>
<script type="text/html" id="eCardRowTpl">
	{{# var eCard = d;}}
	{{# if(!isNoB(eCard)){ }}
	<tr class="title title1">
		<td width="500">商品信息</td>
		<td>面额（元）</td>
		<td>单价（元）</td>
		<td>数量</td>
		<td>小计（元）</td>
	</tr>
	<tr>
		<td>
			<dl class="goods-item goods-item1">
				<dt><a href=""><img src="{{eCard.fileBrowseUrl}}" alt=""/></a>
                    <div class="card-amount">¥&nbsp;{{eCard.faceVal}}</div>
                </dt>
				<dd class="text" style="width: 400px;"><a href="">{{eCard.name}}</a><span id="shopName" style="color:#FF8800;"><span></dd>
			</dl>
		</td>
		<td>
			¥&nbsp;{{eCard.faceVal}}
		</td>
		<td>
			¥&nbsp;{{eCard.price}}
		</td>
		<td><div name="numSpinner" class="num-spinner" id="{{eCard.code}}" data-id="{{eCard.price}}"></div></td>
		<td id="saleAmount">
			¥&nbsp;{{eCard.price}}
		</td>
	</tr>
{{# } }}
</script>	

<script type="text/html" id="ruleListRowTpl">
{{# var ruleList = d;}}
{{# if(!isNoB(ruleList) && ruleList.length >0){ }}
<p style="margin-bottom: 5px;">【活动规则】</p>
{{# for(var i=0,len=ruleList.length; i<len; i++){ }}
{{# var rule = ruleList[i];}}
<p class="red" style="margin-left:20px;">{{rule.desc}}。</p>
{{# } }}
{{# } }}
</script>	
</html>
