<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/head.jsp" %>
<div class="content">
    <div class="page-width">
        <div class="crumbs"><a href="">首页</a>&gt;<a href="">用户中心</a>&gt;<a href="">我的订单</a>&gt;<span>订单详情</span></div>
        <div class="section">
			<jsp:include page="/WEB-INF/jsp/include/ucenterMenu.jsp" />
            <div class="section-right1">
                <div class="order-detail">
                    <div id="orderState" class="order-tit">
                    </div>
                    <div class="order-process">
                        <ul class="steps-state">
                        </ul>
                    </div>
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
                    <div class="order-block order-block1">
                        <div class="block-tit">
                         	   订单信息
                        </div>
                        <div class="block-cont">
                            <div id="usetContext" class="block-info">
                                
                            </div>
                            <div id="payWay" class="block-info">
                            </div>
                            <div id="plan" class="block-info">
                            	
                            </div>
                            <div id="orderleaveMsg" class="block-info">
                            	
                            </div>
                            <div id="shopContext" class="block-info">
                            </div>
                            <div id="distShopContext" class="block-info">
                            </div>
                            <div class="block-info noline">
                                <h2>服务&商品</h2>
                                <!--订单详情-start-->
                                <table class="order-td shopping-td table-fixed">
                                    <tbody id="orderItems">

                                    </tbody>
                                </table>
                                <!--订单详情-end-->
                            </div>
                        </div>
                         <div class="shopping-info">
                            <p>
                                <em>总金额：</em><span id="saleAmount" class="red"></span><br/>
                            </p>
                            <p id="discAmount">
                            </p>
                            <p id="amountInner">
                            </p>
                             <p class="total"><b><em>应付金额：</em><span id="amount" class="red"></span></b></p>
                         </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
var __orderId = null;
var saleOrderInfo;
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
function renderHtml(data,fromId,toId) {
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
    var order=extractUrlParams(location.href);
    if(order!=null&&order.id!=null){
    	__orderId = order.id;
    	ajaxPost(order.id);
    }
}
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
					saleOrderInfo=order;
					order["orderStateOperation"]=getOrderStateValue(order);
					order["payWayName"]=getPayName(order.payWay);
					//订单状态
					renderHtml(order, "orderStateRowTpl","orderState");
					//生成进度条
					renderOrderStateNode(order);
					//用户信息
					renderHtml(order, "usetContextRowTpl","usetContext");
					//店铺信息
					renderHtml(order, "shopContextRowTpl","shopContext");
					//分销店信息
					if(order.distributorId != null){
						renderHtml(order, "distShopContextRowTpl","distShopContext");
					}
					//支付方式
					renderHtml(order, "payWayRowTpl","payWay");
					//预约时间
					renderHtml(order, "planRowTpl","plan");
					//订单跟踪
					renderHtml(order.saleOrderRecords,"orderRecordsRowTpl", "orderRecord");
					//订单服务商品
					renderHtml(order, "orderItemsRowTpl","orderItems");
					//订单留言
					renderHtml(order, "orderleaveMsgRowTpl", "orderleaveMsg");
					//总计
					$id("saleAmount").html("¥&nbsp;"+order.saleAmount);
					$id("amount").html("¥&nbsp;"+order.amountOuter);
					//优惠金额
					if(order.discAmount!=null&&order.discAmount>0.00){
						$id("discAmount").html("<em>-优惠金额：</em><span class='red'>¥&nbsp;"+order.discAmount+"</span>");
					}
					//E卡金额（暂时只有e卡暂时用它不在查询）
					if(order.amountInner!=null&&order.amountInner>0.00){
						$id("amountInner").html("<em>-e卡支付：</em><span class='red'>¥&nbsp;"+order.amountInner+"</span>");
					}
				}
				checkIfExistsShop();
			} else {
				Layer.warning(result.message);
		}
	});
		ajax.go();
	}

	//获取订单当前节点订单记录
	function getSaleOrderRecord(saleOrderRecord,action) {
		if(!isNull(saleOrderRecord)&&saleOrderRecord.length>0){
			for (var i = 0; i < saleOrderRecord.length; i++) {
				if(action==saleOrderRecord[i].action){
					return saleOrderRecord[i].ts;
				}
			}
		}
		return '';
	}
	//生成订单进度条
	function renderOrderStateNode(obj) {
		var orderStateOperation=obj.orderStateOperation;
		var orderStateNode=orderStateOperation.orderStateNode;
		if(orderStateNode=='submit'){
			var submitTs=getSaleOrderRecord(obj.saleOrderRecords,orderStateNode);
			$(".steps-state").html("<li class='actived'><i>1</i><span>提交订单</span><span class='time'>"+submitTs+"</span></li><li class='active'><i>2</i><span>等待支付</span></li><li><i>3</i><span>等待享用</span></li><li><i>4</i><span>已完成</span></li>");
			show_time(obj.createTime);
		}else if(orderStateNode=='pay'){
			//获取上一个节点时间
			var submitTs=getSaleOrderRecord(obj.saleOrderRecords,"submit");
			//当前节点时间
			var thisTs=getSaleOrderRecord(obj.saleOrderRecords,orderStateNode);
			$(".steps-state").html("<li class='actived'><i>1</i><span>提交订单</span><span class='time'>"+submitTs+"</span></li><li class='actived'><i>2</i><span>已支付</span><span class='time'>"+thisTs+"</span></li><li class='active'><i>3</i><span>等待享用</span></li><li><i>4</i><span>已完成</span></li>");
		}else if(orderStateNode=='finish'){
			//获取上几个节点时间
			var submitTs=getSaleOrderRecord(obj.saleOrderRecords,"submit");
			var payTs=getSaleOrderRecord(obj.saleOrderRecords,"pay");
			//当前节点时间
			var thisTs=getSaleOrderRecord(obj.saleOrderRecords,orderStateNode);
			$(".steps-state").html("<li class='actived'><i>1</i><span>提交订单</span><span class='time'>"+submitTs+"</span></li><li class='actived'><i>2</i><span>已支付</span><span class='time'>"+payTs+"</span></li><li class='actived'><i>3</i><span>已享用</span><span class='time'>"+thisTs+"</span></li><li class='actived'><i>4</i><span>已完成</span><span class='time'>"+thisTs+"</span></li>");
		}else if(orderStateNode=='cancel'){
			//获取提交订单节点时间
			var submitTs=getSaleOrderRecord(obj.saleOrderRecords,"submit");
			//当前节点时间
			var thisTs=getSaleOrderRecord(obj.saleOrderRecords,orderStateNode);
			$(".steps-state").html("<li class='actived'><i>1</i><span>提交订单</span><span class='time'>"+submitTs+"</span></li><li class='actived'><i>2</i><span>已取消</span><span class='time'>"+thisTs+"</span></li>");
		}
	}
	//待支付显示支付时间倒计时  24小时之内
	function show_time(submitTs) {
		if(submitTs!=null){
			var date=new Date();
			var submitTime=Date.parseAsDate(submitTs);
			var time_distance=(24*60*60*1000)-(date-submitTime);
			//天
			var int_day=Math.floor(time_distance/86400000);
			time_distance-=int_day*86400000;
			//时
			var int_hour=Math.floor(time_distance/3600000);
			time_distance-=int_hour*3600000;
			//分
			var int_minute=Math.floor(time_distance/60000);
			var dateTime=int_hour+"小时"+int_minute+"分";
			if(int_hour>0){
				$(".remain-time").show();
				$id("overplusTime").html(dateTime);
			}
		}
	}
	//获取支付方式
	function getPayName(payWay) {
		var payWayName = null;
		switch (payWay) {
		case "alipay":
			payWayName = "支付宝";
			break;
		case "ecardpay":
			payWayName = "E卡";
			break;
		case "wechatpay":
			payWayName = "微信";
			break;
		case "pos":
			payWayName = "POS刷卡";
			break;
		case "unionpay":
			payWayName = "银联在线";
			break;
		case "abcAsUnionpay":
			payWayName = "银联在线";
			break;
		case "chinapay":
			payWayName = "银联电子";
			break;
		case "coupon":
			payWayName = "优惠券";
			break;
		}
		return payWayName;
	}
	//获取整合后的状态信息
	function getOrderStateValue(obj) {
		var orderStateOperation = {};
		if (obj.payState == "unpaid" &&obj.cancelled == false) {
			orderStateOperation["orderStateNode"]="submit";//已提交
			orderStateOperation["orderState"] = "<em class='warning'>等待付款</em>";
			orderStateOperation["orderStateOper"] = "<input class='btn btnw85h25 btn-warning' onclick='onPaymentOrder("+obj.id+");' type='button' value='付款' /><input onclick='onCancelledConfirm("+obj.id+");' class='btn btnw85h25 btn-undo' type='button' value='取消订单'/>";
			return orderStateOperation;
		} else if (obj.cancelled == true) {
			orderStateOperation["orderStateNode"]="cancel";//已取消
			orderStateOperation["orderState"] = "<em class='warning'>已取消</em>";
			orderStateOperation["orderStateOper"] = "<input onclick='toBuySaleOrderSvc("+obj.id+");' class='btn btnw85h25 btn-warning' type='button' value='继续购买' />";
			return orderStateOperation;
		} else if (obj.payState == "paid" && obj.finished == true && obj.cancelled == false) {
			orderStateOperation["orderStateNode"]="finish";//已完成
			orderStateOperation["orderState"] = "<em class='waiting'>已完成</em>";
			orderStateOperation["orderStateOper"] = "<input onclick='onBlogWrite("+obj.id+");' class='btn btnw85h25 btn-undo' type='button' value='车友分享' />&nbsp;&nbsp;<input onclick='toBuySaleOrderSvc("+obj.id+");' class='btn btnw85h25 btn-waiting' type='button' value='继续购买' />";
			return orderStateOperation;
		} else if(obj.payState == "paid" && obj.finished == false && obj.cancelled == false){
			orderStateOperation["orderStateNode"]="pay";//已支付
			orderStateOperation["orderState"] = "<em class='waiting'>等待享用</em>";
			orderStateOperation["orderStateOper"] = "<input onclick='onEnjoyConfirm("+obj.id+");' class='btn btnw85h25 btn-waiting' type='button' value='确认完成' />";
			return orderStateOperation;
		}else{
			return orderStateOperation;
		}
	}
	
	//------------------------------------------------------------------------------------------------
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
		var ajax = Ajax.post("/saleOrder/cancel/do");
		var data={"id":id};
	    ajax.data(data)
	    ajax.done(function(result, jqXhr) {
	        if (result != ""&&result.type=="info") {
	        	if(result.data!=null){
	        		var orderId=result.data;
	        		location.replace(getAppUrl("/saleOrder/cancelled/detail/jsp?id="+orderId));
	        	}
	        }else{
	        	Layer.warning(result.message);
	        }
		});
	    ajax.go();
	}
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
	//发表博文
	function onBlogWrite(id) {
		var pageUrl= makeUrl(getAppUrl("/ucenter/blog/write/jsp"),{orderId:id});
		setPageUrl(pageUrl, "_blank");
	}
	//去付款
	function onPaymentOrder(id){
		var pageUrl= makeUrl(getAppUrl("/saleOrder/submit/result/jsp"),{orderId:id});
	    setPageUrl(pageUrl, "_blank");
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
	        		ajaxPost(orderId);
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
	
	function changeSvcShop(params){
		var ajax = Ajax.post("/saleOrder/change/shop/do");
	    ajax.data(params)
	    ajax.done(function(result, jqXhr) {
	        if (result != ""&&result.type=="info") {
	        	if(__orderId != null){
	        		ajaxPost(__orderId);
	        	}
	        	Layer.msgSuccess("操作成功!");
	        	return true;
	        }else{
	        	Layer.warning(result.message);
	        	return false;
	        }
		});
	    
	    ajax.go();
	}
	
	function checkIfExistsShop(){
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
				document.getElementById("selectShopBtn").style.display="";
	        }
		});
	    
	    ajax.go();
	}
	
	function openShopDia(){

		//对话框参数名
		var argName = "argx";
		//对话框参数值
		var argValue = {
			saleOrderInfo:saleOrderInfo
		};
		//对话框参数 预存
		setDlgPageArg(argName, argValue);
		//对话框信息
		var pageTitle = "选择服务门店";
		var pageUrl = "/saleOrder/ucenter/shop/select/jsp";
		var extParams = {};
		
		pageUrl = makeDlgPageUrl(pageUrl, argName, extParams);

		//打开对话框-----------------------------------------
		var theDlg = Layer.dialog({
			title : pageTitle,
			src : pageUrl,
			area : ['1000px', '600px'],
			closeBtn : true,
			maxmin : true, //最大化、最小化
			btn : ["确定选择此店"],
			yes : function() {
				//对话框页面窗口对象
				var dlgWin = theDlg.getFrameWindow();
				//对话框页面的接口函数
				var dlgCallback = dlgWin["getDlgResult"];
				//获取返回值
				var dlgResult = dlgCallback();				
				if (dlgResult == null) {
					Layer.msgWarning("请选择服务店铺");
					return false;
				} else {					
					var params={
							distShopId:dlgResult.distShop.id,
							saleOrderId:dlgResult.saleOrder.id										
					}
					if(changeSvcShop(params)){
						
						var svcShopNameHtml="<span>店铺名称：</span>"+dlgResult.distShop.name;
						var svcShopAddressHtml="<span>店铺地址：</span>"+dlgResult.distShop.regionName+dlgResult.distShop.street;				
						$id("svcShopName").html(svcShopNameHtml);
						$id("svcShopAddress").html(svcShopAddressHtml);										
						
					}
					theDlg.hide();
				}
			}
		});
	}
	
	$(function() {
		doTheQuery();
		
	});
</script>
</body>
<script type="text/html" id="orderStateRowTpl">
<h1>
{{# var order = d;}}
{{# if(!isNoB(order)){ }}
	<span>订单号：{{order.no}}</span>
	<span id="saleOrderState{{order.id}}">状态：{{order.orderStateOperation.orderState || ""}}</span>
	<span class="remain-time" style="display: none;"><i></i><span id="overplusTime"></span></span>
</h1>
<span id="saleOrderStateOper{{order.id}}" class="fr">
	{{order.orderStateOperation.orderStateOper || ""}}
</span>
{{# }else{ }}
	<p>暂无信息</p>
{{# } }}
</script>
<script type="text/html" id="payWayRowTpl">
	{{# var order = d;}}
	{{# if(!isNoB(order)){ }}
 		<h2>支付及配送方式</h2>
        <p><span>支付方式：</span>{{order.payWayName || ""}}</p>
        <p><span>配送方式：</span>到店享用</p>
	{{# }else{ }}
		<p>暂无信息</p>
	{{# } }}
</script>
<script type="text/html" id="orderleaveMsgRowTpl">
	{{# var order = d;}}
	{{# if(!isNoB(order)){ }}
 		<h2>订单留言</h2>
        <p><span>留言：</span>{{order.leaveMsg || ""}}</p>
	{{# }else{ }}
		<p>暂无信息</p>
	{{# } }}
</script>	
<script type="text/html" id="planRowTpl">
	{{# var order = d;}}
	{{# if(!isNoB(order)){ }}
 		<h2>预约时间</h2>
        <p><span>预约时间：</span>{{order.planTime}}</p>
	{{# }else{ }}
		<p>暂无信息</p>
	{{# } }}
</script>	
<script type="text/html" id="usetContextRowTpl">
	{{# var order = d;}}
	{{# if(!isNoB(order)){ }}
		<h2>享用人信息</h2>
        <p><span>姓名：</span>{{order.linkMan || ""}}</p>
        <p><span>手机：</span>{{order.phoneNo || ""}}</p>
	{{# }else{ }}
		<p>暂无信息</p>
	{{# } }}
</script>
<script type="text/html" id="shopContextRowTpl">
	{{# var order = d;}}
	{{# if(!isNoB(order)){ }}
		<h2>店铺信息
		{{# if(order.payState == "paid" && order.finished == false && order.cancelled == false&& order.svcOnly == true&&order.creatorId==order.userId){ }}
			<input id="selectShopBtn" type="button" class='btn btnw85h25 btn-waiting' onclick="javascript:openShopDia();" value="选择合作店" style="display:none;"/>
		{{# } }}
		</h2>
        <p id="svcShopName"><span>店铺名称：</span>{{order.shopName || ""}}</p>
        <p id="svcShopAddress"><span>店铺地址：</span>{{order.address || ""}}</p>
	{{# }else{ }}
		<p>暂无信息</p>
	{{# } }}
</script>	
<script type="text/html" id="distShopContextRowTpl">
	{{# var order = d;}}
	{{# if(!isNoB(order) && !isNoB(order.distributorId)){ }}
		<h2>服务店铺信息</h2>
		{{# if(!isNoB(order.distributorId)){ }}
			<p><span>服务店名称：</span>{{order.distShopName || ""}}</p>
			<p><span>服务店电话：</span>{{order.distShopPhoneNo || ""}}</p>
			<p><span>服务店地址：</span>{{order.distShopAddress || ""}}</p>
		{{# } }}
	{{# }else{ }}
		<p>暂无信息</p>
	{{# } }}
</script>	
<script type="text/html" id="orderRowTpl">
	{{# var orders = d;}}
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
<script type="text/html" id="orderItemsRowTpl">
	{{# var orderItem=d}}
		<tr class="title">
			<th>服务/商品名称</th>
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