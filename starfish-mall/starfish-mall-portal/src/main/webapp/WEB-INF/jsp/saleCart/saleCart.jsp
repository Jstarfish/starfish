<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/head.jsp" %>
<div id="content" class="content">
    <div class="page-width">
        <ul class="steps">
            <li class="active"><i>1</i><span>我的购物车</span><em></em></li>
            <li><i>2</i><span>填写核对订单</span><em></em></li>
            <li class="end"><i>3</i><span>订单提交成功</span></li>
        </ul>
        <div class="mod-main2">
           <!-- <div class="mod-main2-tit">
                <h1>全部服务<span id="allCartSvcCount"></span></h1>
            </div>  -->
            <table class="order-td shopping-td">
				<thead>
					<tr>
						<th width="100" class="total-tit ele-vlmiddle">
							<label class="mr20"><input id="selectAll" name="selectAll" type="checkbox" />&nbsp;&nbsp;全选</label></th>
							<th>商品</th>
							<th width="80">单价（元）</th>
							<th width="100">数量</th>
							<th width="90">小计（元）</th>
							<th width="39">操作</th>
						</tr>
				</thead>
				<tbody id="saleCartSvc"></tbody>
                <tbody id="optGoods"></tbody>
            </table>
        <div class="shopping-info mb20">
            <div>共 <span id="goodsCount" class="red"></span>件 商品总价: <span id="shoppingPrices" class="red"></span></div>
            <div>亿投车吧门店（<a class="anormal" href="javascript:void(0);">安装及费用标准说明</a>） 服务费: <span id="svcPrices" class="red"></span></div>
        </div>
        </div>
    </div>
    <div id="cartToolbar" class="cart-toolbar">
        <div class="page-width">
            <div class="cart-select ele-vlmiddle">
                <label class="mr20"><input id="selectAll" name="selectAll" type="checkbox"/>&nbsp;&nbsp;全选</label> <a onClick="onDeleteCart('','checkedDelete')" class="ml10" href="javascript:void(0)">删除选中的服务</a>
            </div>
            <div class="fr">
                <span class="total-price">合计金额： <span id="finalAmount" class="red"></span></span>
                <input id="next" class="btn btn-h40" type="button" value="下一步" />
            </div>
        </div>
    </div>
</div>	
<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript" src="<%=resBaseUrl%>/js-app/saleCart.js"></script>
</body>
<script type="text/html" id="nogoodsRowTpl">
	    <div class="page-width">
        <div class="cart-nogoods">
		{{# if(getLoginState()==0){ }}
			购物车内暂时没有商品，登录后将显示您之前加入的商品<br/>
		{{# }else{ }}
			购物车空空的哦~，去看看心仪的商品吧~<br/>
		{{# } }}
		{{# if(getLoginState()==0){ }}
			<a class="anormal ml20" href="{{getAppUrl('/user/login/jsp')}}">登录</a>
		{{# } }}
        	<a class="anormal ml20" href="{{getAppUrl('/product/supermarket/list/jsp')}}">去购物</a>
    	</div>
</script>
<script type="text/html" id="saleCartRowTpl">
{{# var svcList = d; }}
{{# if(!isNoB(svcList)){ }}
<tr class="title">
	<th colspan="6"><div class="text-left"><label class="mr20">洗美服务</label></div></th>
</tr>
{{# for(var i=0;i<svcList.length; i++) {}}
{{# var cart = svcList[i]; }}
<tr id="cartSvcTr{{cart.svcId}}">
	<td colspan="2">
		<dl class="goods-item goods-item2">
			<dd><input id="checkSvc{{cart.svcId}}" data-id="{{cart.svcId}}" data-type="svc" data-name="selectCheckbox" name="selectCheckbox" class="mr20" type="checkbox" /></dd>
			<dt><a href=""><img src="{{cart.svcxAlbumImg}}" alt="" /></a></dt>
			<dd class="text"><a href=""><b>{{cart.carSvcName}}</b><br />{{cart.desc}}</a></dd>
		</dl>
	</td>
	<td>¥&nbsp;{{cart.salePrice}}</td>
	<td>1</td>
	<td><div class="price1">¥&nbsp;{{cart.amountInfo.saleAmount}}</div></td>
	<td><a class="delete" onClick="onDeleteCart({{cart.svcId}},'svc')"></a></td>
</tr>
{{# } }}
{{# } }}
</script>

<script type="text/html" id="optGoodsRowTpl">
{{# var optGoods = d; }}
{{# if(!isNoB(optGoods) && optGoods.length>0){ }}
<tr class="title">
	<th colspan="6"><div class="text-left"><label class="mr20">自选商品</label></div></th>
</tr>
{{# for(var j=0;j<optGoods.length; j++) { }}
{{# var goods = optGoods[j]; }}
<tr id="cartGoodsTr{{goods.productId}}">
	<td colspan="2">
		<dl class="goods-item goods-item2">
			<dd><input id="checkGoods{{goods.productId}}" data-id="{{goods.productId}}" data-type="goods" data-name="selectCheckbox" name="selectCheckbox" class="mr20" type="checkbox" /></dd>
			<dt><a href="{{__appBaseUrl}}/product/detail/jsp?productId={{goods.productId}}"><img src="{{goods.productAlbumImg}}" alt="" /></a></dt>
			<dd class="text"><a href="{{__appBaseUrl}}/product/detail/jsp?productId={{goods.productId}}">{{goods.productName}}</a></dd>
		</dl>
	</td>
	<td>¥&nbsp;{{goods.productAmount}}</td>
	<td>
		<div id="numSpinner{{goods.productId}}" name="numSpinner" class="num-spinner" data-id="{{goods.productId}}">
			<a href="javascript:;" class="decr"><i></i></a><input type="text" name="" value="{{goods.quantity}}"><a href="javascript:;" class="incr"><i></i></a>
		</div> 
	</td>
	<td>
		{{# if(goods.amountInfo!=null){ }}
			<div class="price1">¥&nbsp;{{goods.amountInfo.saleAmount}}</div></td>
		{{# } }}
	<td><a class="delete" onClick="onDeleteCart({{goods.productId}},'goods')"></a></td>
</tr>
{{# } }}
{{# } }}
</script>
</html>