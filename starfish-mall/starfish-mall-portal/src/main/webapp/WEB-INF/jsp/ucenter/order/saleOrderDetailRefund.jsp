<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/head.jsp" %>
<div class="content">
    <div class="page-width">
        <div class="crumbs"><a href="">首页</a>&gt;<a href="">用户中心</a>&gt;<a href="">我的订单</a>&gt;<span>订单详情</span></div>
        <div class="section">
			<jsp:include page="/WEB-INF/jsp/include/ucenterMenu.jsp" />
            <div class="section-right1">
                <div class="order-detail">
                	<div id="orderNo" class="order-tit">
                    </div>
                    <table class="order-td shopping-td mb10 table-fixed">
                    	<tbody id="svcGoods">

                        </tbody>
                    </table>
                    <div class="order-block">
                        <div class="block-tit">退款信息</div>
                        <div class="block-cont">
                            <div id="order" class="block-info">
                            </div>
                        </div>
                    </div>
                    <div class="order-block">
                        <div class="block-tit">享用人信息</div>
                        <div class="block-cont">
                            <div id="usetContext" class="block-info">
                            </div>
                        </div>
                    </div>
                    <div class="order-block">
                        <div class="block-tit">商铺信息</div>
                        <div class="block-cont">
                            <div id="shopContext" class="block-info">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
	// 渲染页面内容
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
	//请求数据
	function doTheQuery() {
		var order = extractUrlParams(location.href);
		if (order != null && order.id != null) {
			ajaxPost(order.id);
		}
	}
	//页面加载
	function ajaxPost(orderId) {
		var ajax = Ajax.post("/saleOrder/detail/get");
		var formData = {
			orderId : orderId
		};
		ajax.data(formData);
		ajax.done(function(result, jqXhr) {
			var spanLength = null;
			if (result != "" && result.type == "info") {
				var order = result.data;
				if (order != null) {
					//订单号
					renderHtml(order, "orderNoRowTpl", "orderNo");
					//订单服务商品
					renderHtml(order.saleOrderSvcs, "orderSvcsRowTpl",
							"svcGoods");
					//订单信息
					renderHtml(order, "orderRowTpl", "order");
					//用户信息
					renderHtml(order, "usetContextRowTpl", "usetContext");
					//店铺信息
					renderHtml(order, "shopContextRowTpl", "shopContext");
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	//---------------------------------------------------业务处理--------------------------------------
	//继续购买
	function toBuySaleOrderSvc(id) {
		var ajax = Ajax.post("/saleCart/sync/saleOrder/do");
		var data={"orderId":id};
	    ajax.data(data)
	    ajax.done(function(result, jqXhr) {
	        if (result != ""&&result.type=="info") {
	        	if(result.data!=null){
	        		setPageUrl(getAppUrl("/saleCart/list/jsp"), "_blank");
	        	}
	        }else{
	        	Layer.warning(result.message);
	        }
		});
	    ajax.go();
	}
	$(function() {
		doTheQuery();
	});
</script>
</body>
<script type="text/html" id="orderNoRowTpl">
{{# var order = d;}}
{{# if(!isNoB(order)){ }}
	<h1>
		<span>订单号：{{order.no}}</span>
		<span>状态：已退款</span>
	</h1>
	<span class="fr">
		<input onclick="toBuySaleOrderSvc({{order.id}});" class="btn btnw85h25 btn-warning" type="button" value="继续购买" />
	</span>
{{# }else{ }}
	<p>暂无信息</p>
{{# } }}
</script>	
<script type="text/html" id="usetContextRowTpl">
	{{# var order = d;}}
	{{# if(!isNoB(order)){ }}
        <p><span>姓名：</span>{{order.linkMan}}</p>
        <p><span>手机：</span>{{order.phoneNo}}</p>
	{{# }else{ }}
		<p>暂无信息</p>
	{{# } }}
</script>
<script type="text/html" id="orderRowTpl">
	{{# var order = d;}}
	{{# if(!isNoB(order)){ }}
		<dl class="labels labels1">
          <dt><label>订单总金额：</label></dt>
          <dd>¥&nbsp;{{order.saleAmount}}</dd>
        </dl>
		<dl class="labels labels1">
          <dt><label>优惠金额：</label></dt>
          <dd>¥&nbsp;{{order.saleAmount-order.amount}}</dd>
        </dl>
		<dl class="labels labels1">
          <dt><label>实付金额：</label></dt>
          <dd>¥&nbsp;{{order.amount}}</dd>
        </dl>
		<dl class="labels labels1">
          <dt><label>退款总计：</label></dt>
          <dd>¥&nbsp;{{order.amount? order.amount : 0}}</dd>
        </dl>
		<dl class="labels labels1">
          <dt><label>e卡退款：</label></dt>
          <dd>¥&nbsp;{{order.amountInner? order.amountInner : 0}}</dd>
        </dl>
		<dl class="labels labels1">
          <dt><label>第三方退款：</label></dt>
          <dd>¥&nbsp;{{order.amountOuter? order.amountOuter : 0}}</dd>
        </dl>
	{{# }else{ }}
		<p>暂无信息</p>
	{{# } }}
</script>	
<script type="text/html" id="shopContextRowTpl">
	{{# var order = d;}}
	{{# if(!isNoB(order)){ }}
        <p><span>店铺名称：</span>{{order.shopName}}</p>
        <p><span>店铺地址：</span>{{order.regionName}}{{order.street}}</p>
	{{# }else{ }}
		<p>暂无信息</p>
	{{# } }}
</script>					
<script type="text/html" id="orderSvcsRowTpl">
	{{# var orderSvcList=d}}
		<tr class="title">
			<th width="100">商品名称</th>
			<th>商品名称</th>
			<th width="80">小计</th>
		</tr>
		{{# for(var i = 0; i< orderSvcList.length; i++){ }}
		{{# var ordeSvc=orderSvcList[i]}}
		<tr>
		<td>{{ordeSvc.svcName}}</td>
		<td class="has-inner-table">
		{{# if(!isNoB(ordeSvc.saleOrderGoods)){ }}
			<table class="inner-table">
			{{# for(var j = 0;j<ordeSvc.saleOrderGoods.length; j++){ }}
			{{# var svcGoods=ordeSvc.saleOrderGoods[j]}}
			{{# if(j==ordeSvc.saleOrderGoods.length-1){ }}
				<tr class="last">
			{{# }else{ }}
				<tr>
			{{# } }}
					<td>
						<dl class="goods-item">
						<dt><a href="{{__appBaseUrl}}/product/detail/jsp?productId={{svcGoods.productId}}"><img src="{{svcGoods.productAlbumImg}}" alt=""/></a></dt>
						<dd class="text"><a href="{{__appBaseUrl}}/product/detail/jsp?productId={{svcGoods.productId}}">{{svcGoods.productName}}</a></dd>
						</dl>
					</td>
					<td width="80">×{{svcGoods.quantity}}</td>
				</tr>
			{{# } }}
			</table>
		{{# }else{ }}
			<div class="goods-none" style="padding:10px;"><span style="color: #323232;">{{ordeSvc.svcDesc}}</span></div>
		{{# } }}
		</td>
		<td>¥&nbsp;{{ordeSvc.saleAmount}}</td>
		</tr>
	{{# } }}
</script>
</html>