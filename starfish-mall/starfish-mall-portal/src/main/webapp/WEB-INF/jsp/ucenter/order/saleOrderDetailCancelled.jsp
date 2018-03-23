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
                    	<tbody id="orderItems">

                        </tbody>
                    </table>
                    <div class="order-block order-trace">
                        <div class="block-tit">
                            	订单跟踪
                            <span class="phone-look"><img src="<%=resBaseUrl%>/image-app/phone.png" alt=""/>手机查看更方便</span>
                        </div>
                        <div class="block-cont">
                            <table class="trance-tb">
                                <thead>
                                <tr>
                                    <th width="130">处理时间</th>
                                    <th>处理信息</th>
                                    <th width="180">操作人</th>
                                </tr>
                                </thead>
                                <tbody id="orderRecord">
                                
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="order-block">
                        <div class="block-tit">订单信息</div>
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
	var actionDescDict = {
			"submit" : "您提交了订单，请等待系统确认！",
			"finish" :"感谢您在亿投车吧享受服务，欢迎您再次光临！",
			"pay" : "买家已付款，请等待享受服务！",
			"cancel" : "您的订单已取消！",
			"applyRefund" : "您的订单已申请退款！",
			"agreeRefund" : "您的订单已同意退款！",
			"refuseRefund" : "您的订单已拒绝退款！",
			"refund" : "您的订单已退款！"
		}
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
					renderHtml(order, "orderItemsRowTpl","orderItems");
					//订单信息
					renderHtml(order, "orderRowTpl", "order");
					//订单跟踪
					renderHtml(order.saleOrderRecords,"orderRecordsRowTpl", "orderRecord");
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
		var url = getAppUrl("/saleCart/list/jsp");
		setPageUrl(url, "_blank", function(){
			var success = false;
			var ajax = Ajax.post("/saleCart/sync/saleOrder/do");
			var data={"orderId":id};
		    ajax.sync();
		    ajax.data(data);
		    ajax.done(function(result, jqXhr) {
		        if (result != ""&&result.type=="info") {
		        	if(result.data!=null){
		        		success = true;
		        	}
		        }else{
		        	Layer.warning(result.message);
		        }
			});
		    ajax.go();
		    
		    //
			return success;
		});
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
		<span>状态：已取消</span>
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
        <p><span>姓名：</span>{{order.linkMan || ""}}</p>
        <p><span>手机：</span>{{order.phoneNo || ""}}</p>
	{{# }else{ }}
		<p>暂无信息</p>
	{{# } }}
</script>
<script type="text/html" id="orderRecordsRowTpl">
	{{# var orderRecords = d;}}
	{{# if(!isNoB(orderRecords)&&orderRecords.length>0){ }}
	{{# for(var i = 0, len = orderRecords.length; i < len; i++){ }}
	{{# var record=orderRecords[i]}}
		<tr>
           <td>{{record.ts}}</td>
           <td>{{actionDescDict[record.action]}}</td>
           <td>{{record.actorName==null?"":record.actorName}}</td>
        </tr>
	{{# } }}
	{{# } }}
</script>
<script type="text/html" id="orderRowTpl">
	{{# var order = d;}}
	{{# if(!isNoB(order)){ }}
		<p><span>订单总金额：</span>¥&nbsp;{{order.saleAmount}}</p>
        <p><span>配送方式：</span>到店享用</p>
        <p><span>下单时间：</span>{{order.createTime}}</p>
		<p><span>订单留言：</span>{{order.leaveMsg || ""}}</p>
        <p><span>预约时间：</span>{{order.planTime}}</p>
		<p><span>取消时间：</span>{{order.changeTime}}</p>
		<p><span>取消原因：</span>恭请期待...</p>
	{{# }else{ }}
		<p>暂无信息</p>
	{{# } }}
</script>	
<script type="text/html" id="shopContextRowTpl">
	{{# var order = d;}}
	{{# if(!isNoB(order)){ }}
        <p><span>店铺名称：</span>{{order.shopName || ""}}</p>
        <p><span>店铺地址：</span>{{order.regionName || ""}}{{order.street || ""}}</p>
	{{# }else{ }}
		<p>暂无信息</p>
	{{# } }}
</script>					
<script type="text/html" id="orderItemsRowTpl">
	{{# var orderItem=d}}
		<tr class="title">
			<th width="100">服务/商品名称</th>
			<th width="100">单价</th>
			<th width="100">数量</th>
		</tr>
	{{# var orderSvcList=orderItem.saleOrderSvcs; }}
	{{# if(!isNoB(orderSvcList) && orderSvcList.length>0){ }}
	{{# for(var i = 0; i< orderSvcList.length; i++){ }}
	{{# var ordeSvc=orderSvcList[i]}}
		<tr>
            <td>
			<dl class="goods-item goods-item2">
				<dt><a href=""><img src="{{ordeSvc.svcxAlbumImg}}" alt=""/></a></dt>
				<dd class="text"><a href="">{{ordeSvc.svcName}}<br /><span class="gray1">{{ordeSvc.svcDesc}}</span></a></dd>
			</dl>
            </td>
            <td>¥&nbsp;{{ordeSvc.saleAmount}}</td>
            <td>x1</td>
        </tr>
	{{# } }}
	{{# } }}

	{{# var orderGoodsList=orderItem.saleOrderGoods; }}
	{{# if(!isNoB(orderGoodsList) && orderGoodsList.length>0){ }}
	{{# for(var i = 0; i< orderGoodsList.length; i++){ }}
	{{# var orderGoods=orderGoodsList[i]}}
		<tr>
            <td>
			<dl class="goods-item goods-item2">
				<dt><a href="{{__appBaseUrl}}/product/detail/jsp?productId={{orderGoods.productId}}"><img src="{{orderGoods.productAlbumImg}}" alt=""/></a></dt>
				<dd class="text"><a href="{{__appBaseUrl}}/product/detail/jsp?productId={{orderGoods.productId}}">{{orderGoods.productName}}</a></dd>
			</dl>
            </td>
            <td>¥&nbsp;{{orderGoods.saleAmount}}</td>
            <td>×{{orderGoods.quantity}}</td>
        </tr>
	{{# } }}
	{{# } }}
</script>
</html>