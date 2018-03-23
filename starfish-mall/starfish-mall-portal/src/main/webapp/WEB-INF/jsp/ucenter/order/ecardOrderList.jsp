<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/base.jsf"%><jsp:include
	page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
<div class="content">
	<div class="page-width">
		<div class="crumbs">
			<a href="">首页</a>&gt;<a href="">用户中心</a>&gt;<a href="">订单中心</a>&gt;<span>e卡订单</span>
		</div>
		<div class="section">
			<jsp:include page="/WEB-INF/jsp/include/ucenterMenu.jsp"></jsp:include>
			<div class="section-right1">
				<div class="mod-main">
					<div class="mod-main-tit clearfix">
						<ul class="mod-command">
							<li id="allOrdersLi" class="active"><a href="javascript:toggleClass(allOrdersLi);"
								onclick="getOrdersWithoutStatus(this);">全部订单</a></li>
							<li id="unpaidOrdersLi" ><a href="javascript:toggleClass(unpaidOrdersLi);"
								onclick="getOrdersWithStatus('unpaid', this);">待付款</a></li>
						</ul>
					</div>
					<table id="eCardOrderRows" class="order-td"
						style="table-layout: fixed;">
						<thead>
							<tr>
								<th width="130">
									<ul class="quick-menu order-dropdown order-dropdown1">
										<li id="timeFilter" class="nLi dropdown"><a href="#"><span>近三个月订单</span><i></i></a>
											<ul class="sub">
												<li><a href="#"
													onclick="getOrdersWithTimeLimit(-1, this)">近一个月订单</a></li>
												<li><a href="#"
													onclick="getOrdersWithTimeLimit(-3, this)">近三个月订单</a></li>
												<li><a href="#"
													onclick="getOrdersWithTimeLimit(-6, this)">近半年订单</a></li>
												<li><a href="#"
													onclick="getOrdersWithoutTimeLimit(this)">全部订单</a></li>
											</ul></li>
									</ul>
								</th>
								<th width="260">订单详情</th>
								<th width="100">面额</th>
								<th width="100">单价</th>
								<th width="100">总计</th>
								<th width="100">
									<ul class="quick-menu order-dropdown order-dropdown2">
										<li id="statusFilter" class="nLi dropdown"><a href="#"><span>全部状态</span><i></i></a>
											<ul class="sub">
												<li><a href="#" onclick="getOrdersWithoutStatus(this)">全部状态</a></li>
												<li><a href="#"
													onclick="getOrdersWithStatus('unpaid', this)">待付款</a></li>
												<li><a href="#"
													onclick="getOrdersWithStatus('finished', this)">已完成</a></li>
												<li><a href="#"
													onclick="getOrdersWithStatus('cancelled', this)">已取消</a></li>
											</ul></li>
									</ul>
								</th>
								<th>操作</th>
							</tr>
						</thead>
					</table>
					<!--分页-->
					<div class="pager-gap" >
						<div class="fr pager" id="jqPaginator">
							<a class="prev disabled" href="#nogo">上一页</a><a class="active"
								href="#nogo">1</a><a class="next" href="#nogo">下一页</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
<!--转赠-end-->
<script type="text/javascript">
	//分页大小（页面级）
	var qry_pageSize = 6;
	// [搜索用]的关键字符串
	var qry_keywords = null;
	// 过滤条件项
	var qry_filterItems = {};
	// 排序项
	var qry_sortItems = {};
	// 分页信息
	var qry_pagination = {
		totalCount : 0,
		pageSize : qry_pageSize,
		pageNumber : 1
	};

	var paid = false;

	// 根据 新的的分页信息（或新的页码） 更新 本地分页信息
	function resetPagination(refPagination) {
		refPagination = refPagination || {};
		if (typeof refPagination == "number") {
			refPagination = {
				pageNumber : refPagination
			};
		}
		//
		merge(qry_pagination, refPagination);
	}

	// 根据最新的信息生成新的查询条件
	function newQryFilterInfo() {
		var retData = {
			keywords : qry_keywords || null,
			filterItems : qry_filterItems || {},
			sortItems : qry_sortItems || {},
			pagination : qry_pagination
		};
		if (retData.pagination) {
			retData.pagination.totalCount = 0;
		}
		return retData;
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
		$id(toId).find("tbody").remove();
		$id(toId).append(htmlStr);
	}

	// 执行统一查询 ==========================================
	function doTheQuery() {
		var postData = newQryFilterInfo();

		var ajax = Ajax.post("/eCardOrder/list/get");
		ajax.sync();
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			var rows = result.data.rows;
			if (rows == null) {
				return;
			}
			// 生成列表
			renderHtml(rows, "eCardOrderRowsTpl", "eCardOrderRows");

			// ----刷新分页控件-----
			var refPagination = result.data.pagination;
			resetPagination(refPagination);
			// 注意：jqPaginator的当前页面为 currentPage
			refPagination.currentPage = refPagination.pageNumber;
			// 根据新的分页信息（refPagination）刷新jqPaginator控件
			$("#jqPaginator").jqPaginator("option", refPagination);

		});
		ajax.go();
	}

	// 关键字 等
	function keywordsAndQuery() {
		// 重建 qry_keywords
		// qry_keywords = ...;
		//
		// 重置页码
		resetPagination(1);
		//
		doTheQuery();
	}

	// 查询 按钮等
	function filterAndQuery() {
		// 重建 qry_filterItems
		// qry_filterItems = {};
		// 或 修改 qry_filterItems
		// merge(qry_filterItems, {...});
		//
		// 重置页码
		resetPagination(1);
		//
		doTheQuery();
	}

	// 排序 按钮等
	function sortAndQuery() {
		// 重建 qry_sortItems
		// qry_sortItems = {...};
		// 或 修改 qry_sortItems
		// merge(qry_sortItems, {...});
		//
		// 重置页码
		resetPagination(1);
		//
		doTheQuery();
	}

	// 排序 按钮等
	function pageNoAndQuery(pageNo) {
		// 变更页码
		resetPagination(pageNo);
		//
		doTheQuery();
	}

	function getAllOrders() {
		qry_filterItems.paid = null;
		filterAndQuery();
	}

	function getUnpaidOrders() {
		qry_filterItems.paid = false;
		filterAndQuery();
	}

	function getOrdersWithoutTimeLimit(obj) {
		qry_filterItems.fromDate = null;
		filterAndQuery();
		if (obj != null) {
			$("#timeFilter").find("span").html($(obj).html());
		}
	}

	function getOrdersWithTimeLimit(month, obj) {
		var now = new Date();
		qry_filterItems.fromDate = now.addMonths(month).format();
		filterAndQuery();
		if (obj != null) {
			$("#timeFilter").find("span").html($(obj).html());
		}
	}

	function getOrdersWithoutStatus(obj) {
		qry_filterItems.status = null;
		filterAndQuery();
		if (obj != null) {
			$("#statusFilter").find("span").html($(obj).html());
		}
	}

	function getOrdersWithStatus(status, obj) {
		qry_filterItems.status = status;
		filterAndQuery();
		if (obj != null) {
			$("#statusFilter").find("span").html($(obj).html());
		}
	}

	function showDetail(paid, cancelled, orderId) {
		if (paid) {
			window.open(getAppUrl("/eCardOrder/detail/finished/jsp?orderId=" + orderId));
		} else if (cancelled) {
			window.open(getAppUrl("/eCardOrder/detail/cancelled/jsp?orderId=" + orderId));
		} else if (!paid) {
			window.open(getAppUrl("/eCardOrder/detail/unpaid/jsp?orderId=" + orderId));
		}

		return;
	}
	
	// 获取支付方式
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
	
	function toggleClass(id){
		$id(id).siblings().removeClass('active').end().addClass('active');
	}
	
	function buyCards(cardCode) {
		window.open(getAppUrl("/eCardOrder/submit/jsp?code=" + cardCode));
	}
	
	function payOrder(orderId) {
		window.open(getAppUrl("/eCardOrder/submit/result/jsp?orderId="+orderId));
	}
	
	function cancelOrder(orderId) {
		var ajax = Ajax.post("/eCardOrder/cancel/do");
		ajax.sync();
		ajax.params({
			orderId : orderId
		});
		ajax.done(function(result, jqXhr) {
			if (result.data == false || result.data == null) {
				Layer.msgWarning(result.message);
			} else {
				Layer.msgSuccess("订单取消成功");
				filterAndQuery();
			}
		});
		ajax.go();
	}
	
	$(function() {
		$("#jqPaginator").jqPaginator({
			totalCount : 0,
			pageSize : qry_pageSize,
			pageNumber : 1,
			prev : '<a class="prev" href="javascript:void(0);">上一页<\/a>',
			next : '<a class="next" href="javascript:void(0);">下一页<\/a>',
			page : '<a class="active" href="javascript:void(0);">{{page}}<\/a>',
			onPageChange : function(pageNumber) {
				qry_pagination.pageNumber = pageNumber;
				doTheQuery();
			}
		});

		getOrdersWithTimeLimit(-3);
	})
</script>
</body>
<script type="text/html" id="eCardOrderRowsTpl">
	{{# var eCardOrders = d; }}
	{{# for(var i = 0, len = eCardOrders.length; i < len; i++) { }}
		<tbody>
			<tr class="sep-row">
				<td colspan="6"></td>
			</tr>
			<tr class="title">
				<td colspan="3"><span class="gray">订单号：</span>{{ eCardOrders[i].no }}<span
					class="gray ml20">{{ eCardOrders[i].createTime }}&nbsp;&nbsp;</span><a href="#"
					class="shop-contact">亿投车吧</a></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td></td>
			</tr>
			<tr>
				<td colspan="2">
					<dl class="goods-item goods-item1">
						<dt>
							<a href="#" onclick="showDetail({{ eCardOrders[i].paid }}, {{ eCardOrders[i].cancelled }}, '{{ eCardOrders[i].id }}')"><img src="{{ eCardOrders[i].fileBrowseUrl }}" alt="E卡图片" /></a>
						</dt>
						<dd class="text">
							<a href="#" onclick="showDetail({{ eCardOrders[i].paid }}, {{ eCardOrders[i].cancelled }}, '{{ eCardOrders[i].id }}')">{{ eCardOrders[i].cardName }}</a>
						</dd>
						<dd class="num gray">×{{ eCardOrders[i].quantity }}</dd>
					</dl>
				</td>
				<td>¥{{ eCardOrders[i].faceValue }}</td>
				<td>¥{{ eCardOrders[i].price }}</td>
				<td>¥{{ eCardOrders[i].amount }}
					<div class="gray">{{ getPayName(eCardOrders[i].payWay) }}</div>
				</td>
				{{# if (eCardOrders[i].paid) { }}
					<td><div class="gray">已完成</div> <a href="#" onclick="showDetail({{ eCardOrders[i].paid }}, {{ eCardOrders[i].cancelled }}, '{{ eCardOrders[i].id }}')">订单详情</a></td>
					<td><input class="btn-normal btn-w90" type="button" value="继续购买" onclick="buyCards('{{ eCardOrders[i].cardCode }}')" /></td>
				{{# } else if (eCardOrders[i].cancelled) { }}
					<td><div class="gray">已取消</div> <a href="#" onclick="showDetail({{ eCardOrders[i].paid }}, {{ eCardOrders[i].cancelled }}, '{{ eCardOrders[i].id }}')">订单详情</a></td>
					<td><input class="btn-normal btn-w90" type="button" value="立即购买" onclick="buyCards('{{ eCardOrders[i].cardCode }}')" /></td>
				{{# } else if (!eCardOrders[i].paid) { }}
					<td><div class="gray">待付款</div> <a href="#" onclick="showDetail({{ eCardOrders[i].paid }}, {{ eCardOrders[i].cancelled }}, '{{ eCardOrders[i].id }}')">订单详情</a></td>
					<td><input class="btn-normal btn-w90" type="button" value="付款" onclick="payOrder('{{ eCardOrders[i].id }}')" /><br/><a class="gray" href="#" onclick="cancelOrder('{{ eCardOrders[i].id }}')">取消订单</a></td>
				{{# } }}
			</tr>
		</tbody>
	{{# } }}
</script>
</html>
