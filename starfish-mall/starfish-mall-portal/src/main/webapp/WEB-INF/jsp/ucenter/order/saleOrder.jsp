<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/base.jsf"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>
<div class="content">
    <div class="page-width">
        <div class="crumbs"><a href="">首页</a>&gt;<a href="">用户中心</a>&gt;<span>我的订单</span></div>
        <div class="section">
            <jsp:include page="/WEB-INF/jsp/include/ucenterMenu.jsp" />
            <div class="section-right1">
                <div class="mod-main">
                    <div class="mod-main-tit clearfix">
                        <ul class="mod-command">
                            <li id="all" class="active"><a href="javascript:void(0);">全部订单</a></li>
                            <li id="unhandled"><a href="javascript:void(0);">待付款</a><i style="display: none;"></i></li>
                            <li id="processing"><a href="javascript:void(0);">待享用</a><i style="display: none;"></i></li>
                            <li id="finished"><a href="javascript:void(0);">车友分享</a><i style="display: none;"></i></li>
                        	<li class="special"><a href="">我的常购商品</a></li>
                        </ul>
                        <div class="mod-search">
                            <input class="search-input" type="text" placeholder="商品名称、商品编号、订单号"><input class="btn search-btn" type="button" />
                        </div>
                    </div>
                    <table id="tblData" class="order-td table-fixed">
                        <thead>
                            <tr>
                                <th width="130">
                                    <ul class="quick-menu order-dropdown order-dropdown1">
                                        <li class="nLi dropdown"><a style="width: 100px;" href="javascript:void(0);"><span id="radioDateSearch">全部订单</span><i></i></a>
                                            <ul id="dateSearch" class="sub">
                                                <li><a href="javascript:void(0);">全部订单</a><input type="text" name="" style="display: none;" value=""/></li>
                                                <li><a href="javascript:void(0);">近一个月订单</a><input type="text" name="month" style="display: none;" value="1"/></li>
                                                <li><a href="javascript:void(0);">近三个月订单</a><input type="text" name="month" style="display: none;" value="3"/></li>
                                                <li><a href="javascript:void(0);">近半年订单</a><input type="text" name="month" style="display: none;" value="6"/></li>
                                            </ul>
                                        </li>
                                    </ul>
                                </th>
                                <th width="370">订单详情</th>
                                <th width="100">收货人</th>
                                <th width="100">总计</th>
                                <th width="100">
                                    <ul class="quick-menu order-dropdown order-dropdown2">
                                        <li class="nLi dropdown"><a  href="javascript:void(0);"><span id="radioStateSearch">全部状态</span><i></i></a>
                                            <ul id="stateSearch" class="sub">
                                            	<li id="all_l"><a href="javascript:void(0);">全部状态</a></li>
                                                <li id="unhandled_l"><a href="javascript:void(0);">待付款</a></li>
                                                <li id="processing_l"><a href="javascript:void(0);">待享用</a></li>
                                                <li id="finished_l"><a href="javascript:void(0);">车友分享</a></li>
                                            </ul>
                                        </li>
                                    </ul>
                                </th>
                                <th width="100">操作</th>
                            </tr>
                        </thead>
                    </table>
                    <!--分页-->
                    <div class="pager-gap">
                        关于订单有任何问题请拨打 亿投车吧热线 <b class="red1">4000982198</b>
                        <div id="jqPaginator" class="fr pager">
                            <!-- <a class="pre diabled" href="#nogo">上一页</a>
                            <a class="active" href="#nogo">1</a>
                            <a class="next" href="#nogo">下一页</a> -->
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="pop-ticket" class="pop-ticket" style="display: none;">
	<div class="text-center">
		<div id="svcPackTicket" class="package-ticket">
	    	
   		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp" /> 
<script type="text/javascript">
//----------------------------------------------业务处理-------------------------------------------------
	var orderDto=[];

	//查询订单数量
	function onSaleOrderCount() {
		var ajax = Ajax.post("/saleOrder/count/do");
			ajax.done(function(result, jqXhr) {
				if (result.type = "info") {
					var orderStateTypeCount = result.data;
					if(orderStateTypeCount!=null){
						setOrderStateTypeCount(orderStateTypeCount);
					}
				} 
		});
		ajax.go();
	}
	function onSaleOrderDetail(obj,id,payState) {
		var cancelled =$(obj).attr("data-cancelled");
	   //是否取消
	   if(cancelled == "true"){
		   var pageUrl= makeUrl(getAppUrl("/saleOrder/cancelled/detail/jsp"),{id:id});
		   setPageUrl(pageUrl, "_blank");
	   }else{
		   //是否是已退款
		  // if(payState=="refunded"){
		  //   var pageUrl= makeUrl(getAppUrl("/saleOrder/refunded/detail/jsp"),{id:id});
			//   setPageUrl(pageUrl, "_blank");
		   //}else{
			   var pageUrl= makeUrl(getAppUrl("/saleOrder/detail/jsp"),{id:id});
			   setPageUrl(pageUrl, "_blank");
		  // }
	   }
	}
	//享用确认对话框
	function  onEnjoyConfirm(id) {
		var msg = "确认您的订单完成了吗？";
		//
		var yesHandler = function(layerIndex) {
			theLayer.hide();
			onEnjoySaleOrder(id);
		};
		var noHandler = function(layerIndex) {
			theLayer.hide();
		};
		//
		var theLayer = Layer.confirm(msg, yesHandler, noHandler);
	}
	function onEnjoySaleOrder(orderId){
		var hintBox = Layer.progress("正在操作...");
		var ajax = Ajax.post("/saleOrder/finished/do");
		var data={"id":orderId};
	    ajax.data(data)
	    ajax.done(function(result, jqXhr) {
	        if (result != ""&&result.type=="info") {
	        	if(result.data!=null){
	        		var orderId=result.data;
	        		$id("saleOrderState"+orderId).html("已完成");
	        		$id("saleOrderStateOper"+orderId).html("<div><a href='<%=appBaseUrl%>/ucenter/blog/write/jsp?orderId="+orderId+"'>车友分享</a></div><input onclick='toBuySaleOrderSvc("+orderId+");' class='btn-normal btn-w70' type='button' value='继续购买' />");
	        		onSaleOrderCount();
	        	}
	        }else{
	        	Layer.warning(result.message);
	        }
		});
	    ajax.always(function() {
			hintBox.hide();
		});
	    ajax.go();
	}
	//删除确认对话框
	function  onDeleteConfirm(id) {
		var msg = "您确定要删除订单吗？";
		//
		var yesHandler = function(layerIndex) {
			theLayer.hide();
			onDeleteSaleOrder(id);
		};
		var noHandler = function(layerIndex) {
			theLayer.hide();
		};
		//
		var theLayer = Layer.confirm(msg, yesHandler, noHandler);
	}
	//删除订单
	function onDeleteSaleOrder(orderId) {
		var hintBox = Layer.progress("正在删除...");
		var ajax = Ajax.post("/saleOrder/mark/deleted/do");
		var data={"id":orderId};
	    ajax.data(data)
	    ajax.done(function(result, jqXhr) {
	        if (result != ""&&result.type=="info") {
	        	if(result.data!=null){
	        		$id("order"+result.data).remove();
	        		onSaleOrderCount();
	        	}
	        }else{
	        	Layer.warning(result.message);
	        }
		});
	    ajax.always(function() {
			hintBox.hide();
		});
	    ajax.go();
	}
	//取消确认对话框
	function  onCancelledConfirm(id) {
		var msg = "您确定要取消订单吗？";
		//
		var yesHandler = function(layerIndex) {
			theLayer.hide();
			onCancelledSaleOrder(id);
		};
		var noHandler = function(layerIndex) {
			theLayer.hide();
		};
		//
		var theLayer = Layer.confirm(msg, yesHandler, noHandler);
	}
	//取消订单
	function onCancelledSaleOrder(id) {
		var hintBox = Layer.progress("正在取消...");
		var ajax = Ajax.post("/saleOrder/cancel/do");
		var data={"id":id};
	    ajax.data(data)
	    ajax.done(function(result, jqXhr) {
	        if (result != ""&&result.type=="info") {
	        	if(result.data!=null){
	        		var orderId=result.data;
	        		$id("saleOrderState"+orderId).html("已取消");
	        		$id("saleOrderStateOper"+orderId).html("<input onclick='toBuySaleOrderSvc("+orderId+");' class='btn-normal btn-w70' type='button' value='继续购买' />");
	        		$id("deleteOrder"+orderId).html("<a href='javascript:void(0);' onClick='onDeleteConfirm("+orderId+")' class='delete fr'></a>");
	        		$id("saleOrderDetail"+orderId).attr("data-cancelled",true);
	        		onSaleOrderCount();
	        	}
	        }else{
	        	Layer.warning(result.message);
	        }
		});
	    ajax.always(function() {
			hintBox.hide();
		});
	    ajax.go();
	}
	//继续购买
	function toBuySaleOrderSvc(id) {
		var svcPackId = $id("saleOrderStateOper"+id).attr("data-packId");
		if(!isNull(svcPackId) && svcPackId != "null"){
			 var pageUrl= makeUrl(getAppUrl("/saleOrder/svc/pack/submit/jsp"),{id:svcPackId});
			 setPageUrl(pageUrl, "_blank");
		}else{
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
	}
	//去付款
	function onPaymentOrder(id){
		var pageUrl =makeUrl(getAppUrl("/saleOrder/submit/result/jsp"),{orderId:id});
		setPageUrl(pageUrl, "_blank");
	}
	//给不同状态订单数量赋值
	function setOrderStateTypeCount(orderStateTypeCount) {
		if(null!=orderStateTypeCount&&orderStateTypeCount.unhandledCount!=0){
			$("ul.mod-command li i:eq("+0+")").show();
			$("ul.mod-command li i:eq("+0+")").html(orderStateTypeCount.unhandledCount);
		}else{
			$("ul.mod-command li i:eq("+0+")").hide();
			$("ul.mod-command li i:eq("+0+")").html(0);
		}
		if(null!=orderStateTypeCount&&orderStateTypeCount.processingCount!=0){
			$("ul.mod-command li i:eq("+1+")").show();
			$("ul.mod-command li i:eq("+1+")").html(orderStateTypeCount.processingCount);
		}else{
			$("ul.mod-command li i:eq("+1+")").hide();
			$("ul.mod-command li i:eq("+1+")").html(0);
		}
		if(null!=orderStateTypeCount&&orderStateTypeCount.finishedCount!=0){
			$("ul.mod-command li i:eq("+2+")").show();
			$("ul.mod-command li i:eq("+2+")").html(orderStateTypeCount.finishedCount);
		}else{
			$("ul.mod-command li i:eq("+2+")").hide();
			$("ul.mod-command li i:eq("+2+")").html(0);
		}
	}
	$(function () {
		//设计分页每页显示条数
		var pageSize = 5;
		var pageInfo={};
		var pagination = {pageSize:pageSize,totalCount:1,pageNumber:1};
		var filterItems={};
		pageInfo.pagination = pagination;
		pageInfo.filterItems = filterItems;
		var order=extractUrlParams(location.href);
		if(order!=null && order.orderState!=null){
			filterItems["orderState"]=order.orderState;
			$id(order.orderState).siblings().removeClass('active').end().addClass('active');
		}
		 $("#jqPaginator").jqPaginator({
	        totalCount: pagination.totalCount,
	        pageSize:pageSize,
	        currentPage: 1,
	        prev: '<a class="prev" href="javascript:void(0);">上一页<\/a>',
	        next:'<a class="next"  href="javascript:void(0);">下一页<\/a>',
	        page: '<a class="active" href="javascript:void(0);">{{page}}<\/a>',
	        onPageChange: function (pageNumber) {
	        	pagination.pageNumber=pageNumber;
	        	ajaxOrderList();
	        }
	    });
		
		$("#dateSearch li").click(function() {
			var periodValue =$(this).children("input").val();
			var periodName =$(this).children("input").attr("name");
			filterItems["periodValue"]=periodValue==''?0:periodValue;
			filterItems["periodName"]=periodName;
			pagination.pageNumber = 1;
			ajaxOrderList();
			$("#dateSearch li a").css('color','#646464');
			$(this).children("a").css('color','#c13030');
			$id("radioDateSearch").html($(this).children("a").html());
		});
		
		$("#stateSearch li").click(function() {
			var stateId=this.id;
			filterItems["orderState"]=stateId.substring(0,stateId.length-2);
			pagination.pageNumber = 1;
			ajaxOrderList();
			$("#stateSearch li a").css('color','#646464');
			$(this).children("a").css('color','#c13030');
			$id("radioStateSearch").html($(this).children("a").html());
			
			$id(stateId.substring(0,stateId.length-2)).siblings().removeClass('active').end().addClass('active');
		});
		
		$("ul.mod-command li").click(function(){
				filterItems["orderState"]=this.id;
				filterItems["periodValue"]=0;
				filterItems["periodName"]=null;
				pagination.pageNumber = 1;
				ajaxOrderList();
				if(this.id=="all"){
					$id("radioStateSearch").html("全部状态");
				}else{
					$id("radioStateSearch").html($id(this.id).children("a").html())
				}
				$("#stateSearch li a").css('color','#646464');
				$id(this.id+"_l").children("a").css('color','#c13030');
				
				$id("radioDateSearch").html("全部订单");
				
				$(this).siblings().removeClass('active').end().addClass('active');
		});
		
		// 获取整合后的状态信息
		function getOrderStateValue(obj) {
			var orderStateOperation={};
			if (obj.payState == "unpaid" &&obj.cancelled == false) {
				orderStateOperation["orderState"]="等待付款";
				orderStateOperation["orderStateOper"]="<input onclick='onPaymentOrder("+obj.id+");' class='btn-normal btn-w70' type='button' value='付款' /><div><a onclick='onCancelledConfirm("+obj.id+");' href='javascript:void(0);'>取消订单</a></div>";
				return orderStateOperation;
			} else if (obj.cancelled == true) {
				orderStateOperation["orderState"]="已取消";
				orderStateOperation["orderStateOper"]="<input onclick='toBuySaleOrderSvc("+obj.id+");' class='btn-normal btn-w70' type='button' value='继续购买' />";
				return orderStateOperation;
			} else if (obj.payState == "paid" && obj.finished == true && obj.cancelled == false) {
				orderStateOperation["orderState"]="已完成";
				orderStateOperation["orderStateOper"]="<div><a href='"+makeUrl(getAppUrl("/ucenter/blog/write/jsp"),{orderId:obj.id})+"'>车友分享</a></div><input onclick='toBuySaleOrderSvc("+obj.id+");' class='btn-normal btn-w70' type='button' value='继续购买' />";
				return orderStateOperation;
			} else if(obj.payState == "paid" && obj.finished == false && obj.cancelled == false){
				orderStateOperation["orderState"]="等待享用";
				orderStateOperation["orderStateOper"]='<input onclick="onEnjoyConfirm('+obj.id+');" class="btn-normal btn-w70" type="button" value="确认完成" />';
				return orderStateOperation;
			}else{
				return orderStateOperation;
			}
			//else if(obj.payState == "refunded"){
			//	orderStateOperation["orderState"]="已退款";
			//	orderStateOperation["orderStateOper"]="<input onclick='toBuySaleOrderSvc("+obj.id+");' class='btn-normal btn-w70' type='button' value='继续购买' />";
			//	return orderStateOperation;
			//}
		}
		
		
		
		function ajaxOrderList() {
	        var ajax = Ajax.post("/saleOrder/list/do");
	        pageInfo.pagination = pagination;
	    	pageInfo.filterItems = filterItems;
	        ajax.data(pageInfo)
	        ajax.done(function(result, jqXhr) {
				//
	            if (result != ""&&result.type=="info") {
	            	var orderList=result.data.paginatedList.rows;
	            	if(orderList != null && orderList.length>0){
	            		for (var i = 0; i < orderList.length; i++) {
							var saleOrder = orderList[i];
							saleOrder["orderStateOperation"]=getOrderStateValue(saleOrder);
						}
	            		//
	            		var orderStateTypeCount =result.data.orderStateTypeCount;
						//跟新查询order状态条目数
						setOrderStateTypeCount(orderStateTypeCount);
				        //跟新分页信息
				        var pagin =result.data.paginatedList.pagination;
				        pagination={totalCount:pagin.totalCount,pageSize:pagin.pageSize,pageNumber:pagin.pageNumber};
				        $id("#jqPaginator").jqPaginator("option",{ 
							totalCount:pagination.totalCount,
							currentPage: pagination.pageNumber
					  	});
	            	}
	            	//获取模板内容
					var tplHtml = $id("orderRowTpl").html();
					//生成/编译模板
					var htmlTpl = laytpl(tplHtml);
					//根据模板和数据生成最终内容
					var htmlText = htmlTpl.render(orderList);
					//使用生成的内容
					$("#tblData tbody").remove();
					$id("tblData").append(htmlText);
	            }else{
	            	Layer.warning(result.message);
	            }
			});
	        ajax.go();
		}
	});
	//
	function getSvcPackTicket(ticketId,saleAmount,amount){
		var ajax = Ajax.post("/saleOrder/svc/pack/ticket/get");
		ajax.data({ticketId:ticketId});
		ajax.done(function(result, jqXhr) {
			if (result != ""&& result.type=="info") {
				var data = result.data;
				if (data == null) {
					return;
				}
				data.saleAmount = saleAmount;
				data.amount = amount;
				// 获取模板内容
				var tplHtml = $id("svcPackTicketRowTpl").html();
				// 生成/编译模板
				var htmlTpl = laytpl(tplHtml);
				// 根据模板和数据生成最终内容
				var htmlText = htmlTpl.render(data);
				$id("svcPackTicket").html(htmlText);
				//
				var dom = "#pop-ticket";
				var btns =null;
				var title = "";
				if(data.finished == false && data.invalid == false){
					btns = ["确认完成", "取消"];
					titles = "套餐票（未完成）";
				}else{
					btns = ["取消"];
					titles = "套餐票（已完成）";
				}
				var theLayer = Layer.dialog({
					dom : dom, //或者 html string
					title : titles,
					area : [ '400px', '290px' ],
					closeBtn : false,
					btn : btns,
					yes : function() {
						if(data.finished == false && data.invalid == false){
							submitPackTicket(ticketId);
						}
						theLayer.hide();
					}
				});
			}else{
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	function submitPackTicket(ticketId){
		var ajax = Ajax.post("/saleOrder/userSvcPackTicket/confirm/finish/do");
		ajax.data({ticketId:ticketId});
		ajax.done(function(result, jqXhr) {
			if(result.type=="info"){
				if(result.data){
					//alert("成功");待定
					Layer.info("已完成");
				}else{
					Toast.show("套餐服务票确认失败", null, "error");
				}
			}else{
				Toast.show(result.message, null, "error");
			}
		});
		ajax.go();
	}
	function popTicket(ticketId,saleAmount,amount) {
		getSvcPackTicket(ticketId,saleAmount,amount);
	}

</script>
</body>	
<script  type="text/html" id="svcPackTicketRowTpl">
	{{# var svcPackTicket = d; }}
	{{# if(!isNoB(svcPackTicket)){ }}
		<div class="title">
	    	{{svcPackTicket.svcPackName}}
		</div>
		<div class="cont">
			<img src="{{svcPackTicket.fileBrowseUrl}}" alt="" />
			<span class="ellipsis" style="padding-left: 10px;">{{svcPackTicket.svcName}}</span>
		</div>
		<div class="price2">
			<span class="orig">原价：<span>￥{{svcPackTicket.saleAmount}}</span></span>
			<span class="dis">套餐价：<span>￥{{svcPackTicket.amount}}</span></span>
		</div>
	{{# } }}
</script>
<script type="text/html" id="orderRowTpl">
		{{# var orders = d; }}
		{{# if(!isNoB(orders) && orders.length>0){ }}
		{{# for(var i=0;i<orders.length; i++) { }}
		{{# var order = orders[i]; }}
			  <tbody id="order{{order.id}}">
                 <tr class="sep-row">
                    <td colspan="6"></td>
                 </tr>
                 <tr class="title">
                    <td colspan="3">
						<span class="gray">订单号：</span>{{order.no}}
						<span class="gray ml20">{{order.createTime}}</span>
						<a id="shopAddress" href="javascript:void(0);" class="shop-contact">{{order.regionName || ""}}{{order.street || ""}}</a></td>
                   
					<td colspan="2">
						{{# if(order.svcPackId != null){ }}
							<a href="javascript:void(0);" class="shop-contact">{{order.svcPackName || ""}}</a>
						{{# } }}
					</td>
                    <td>
					<span id="deleteOrder{{order.id}}">
					{{# if(order.cancelled == true || order.finished == true){ }}
						<a href="javascript:void(0);" onClick="onDeleteConfirm({{order.id}})" class="delete fr"></a>
					{{# }else{ }}
						<a href="javascript:void(0);" style="display: none;" class="delete fr"></a>
					{{# } }}
					</td>
					</span>
                 </tr>
				<tr>
					<td colspan="2" class="has-inner-table">
						<table class="inner-table">
							<tbody>
							{{# if(!isNoB(order.saleOrderSvcs)&& order.saleOrderSvcs.length>0){ }}
							{{# for(var j=0,len=order.saleOrderSvcs.length; j<len; j++) {  }}
							{{# var orderSvc = order.saleOrderSvcs[j];}}
 							{{# if(j == len-1 && order.saleOrderGoods.length <0){ }}
								<tr class="last">
							{{# }else{ }}
								<tr>
							{{# } }}
									<td>
										<dl class="goods-item">
											<dt><a href="#"><img src="{{orderSvc.svcxAlbumImg}}" alt="" /></a></dt>
											<dd class="text"><a href="#">{{orderSvc.svcName}}<br><span class="gray1">{{orderSvc.svcDesc}}</span></a></dd>
											{{# if(order.svcPackId != null && orderSvc.ticketId != null){ }}
												<dd class="icon-e"><a href="javascript:;" onclick="popTicket({{orderSvc.ticketId}},{{orderSvc.saleAmount}},{{orderSvc.amount}})"><img src="{{__appBaseUrl}}/static/image-app/icon-ticket.png" alt="" /></a></dd>
											{{# } }}
											<dd class="num gray">x1</dd>
										</dl>
									</td>
								</tr>
							{{# } }}
							{{# } }}
							{{# if(!isNoB(order.saleOrderGoods) && order.saleOrderGoods.length >0){ }}
							{{# for(var j=0,len=order.saleOrderGoods.length; j<len; j++) {  }}
							{{# var orderGoods = order.saleOrderGoods[j]; }}
							{{# if(j == len-1){ }}
								<tr class="last">
							{{# }else{ }}
								<tr>
							{{# } }}
									<td>
										<dl class="goods-item">
										<dt>
											<a href="{{__appBaseUrl}}/product/detail/jsp?productId={{orderGoods.productId}}"><img src="{{orderGoods.productAlbumImg}}" alt="" /></a>
										</dt>
										<dd class="text">
											<a href="{{__appBaseUrl}}/product/detail/jsp?productId={{orderGoods.productId}}">{{orderGoods.productName}}</a>
										</dd>
										<dd class="num gray">x{{orderGoods.quantity}}</dd>
										</dl>
									</td>
								</tr>
							{{# } }}
							{{# } }}
							</tbody>
						</table>
					</td>
					<td><div class="name"><span>{{order.linkMan}}</span><b></b></div></td>
					<td><div class="price1">¥&nbsp;{{order.amount}}</div><div class="gray">在线支付</div></td>
					<td><div id="saleOrderState{{order.id}}" class="gray">{{order.orderStateOperation.orderState}}</div>
						<a id="saleOrderDetail{{order.id}}" data-cancelled="{{order.cancelled}}" onclick="onSaleOrderDetail(this,{{order.id}},'{{order.payState}}');" href="javascript:void(0);">订单详情</a></td>
					<td id="saleOrderStateOper{{order.id}}" data-packId="{{order.svcPackId}}">{{order.orderStateOperation.orderStateOper}}</td>
				</tr>
         </tbody>
		{{# } }}
		{{# } }}
	</script>
 </html>