<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head.jsp"%>
<div class="content">
	<div class="page-width">
		<div class="crumbs">
			<a href="">首页</a>&gt;<a href="">用户中心</a>&gt;<a href="">资产中心</a>&gt;<span>优惠券</span>
		</div>
		<div class="section">
			<jsp:include page="/WEB-INF/jsp/include/ucenterMenu.jsp" />
			<div class="section-right1">
				<div class="mod-main">
					<div class="mod-main-tit clearfix">
						<ul class="mod-command">
							<li data-id="unused" class="active"><a
								href="javascript:void(0);">未使用<i style="display: none;"></i></a></li>
							<li data-id="finished"><a href="javascript:void(0);">已使用</a></li>
							<li data-id="invalid"><a href="javascript:void(0);">已过期</a></li>
						</ul>
						<a class="anormal fr" href="">优惠券规则说明</a>
					</div>
					<table class="order-td td-border-bottom">
						<thead>
							<tr>
								<th width="147">
									<ul class="quick-menu order-dropdown order-dropdown1">
										<li class="nLi dropdown"><a href="javascript:void(0);"><span>优惠券类型</span><i></i></a>
											<ul class="sub couponType">
												<li data-id="nopay"><a href="javascript:void(0);">免付券</a></li>
												<li data-id="sprice"><a href="javascript:void(0);">抵金券</a></li>
												<li data-id="deduct"><a href="javascript:void(0);">特价券</a></li>
											</ul></li>
									</ul>
								</th>
								<th><a class="asort active" href="javascript:void(0);">&nbsp;</a></th>
								<th><a class="asort" href="javascript:void(0);">&nbsp;</a></th>
								<th><a class="asort" href="javascript:void(0);">&nbsp;</a></th>
								<th width="260">操作</th>
							</tr>
						</thead>
						<tbody id="couponList">

						</tbody>
					</table>
					<!--分页-->
					<div class="pager-gap">
						<div id="jqPaginator" class="fr pager"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
	$(function() {
		initGqPaginator();
		//doTheQuery();
	});
	var paginatorCtrl = null;
	//-------------------------------------------------优惠券列表---------------------------------------------
	function doTheQuery() {
		var postData = newQryFilterInfo();
		// ajax post 请求...
		var ajax = Ajax.post("/user/coupon/list/get");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var data = result.data;
				//未使用数量
				var couponCount = data.couponUnusedCount;
				setCouponCount(couponCount);
				//
				var paginatedList = data.paginatedList;
				//更新分页信息
				var pagination = paginatedList.pagination;
				//
				var coupons = paginatedList.rows;
				var couponList = recombineCoupon(coupons);
				//
				renderCouponData(couponList);
				// 利用返回的分页信息
				var refPagination = pagination;
				//
				resetPagination(refPagination);
				// 根据新的分页信息（refPagination）刷新jqPaginator控件
				refPaginator();
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	//------------------------------------------------渲染页面-------------------------------------------------
	//渲染货品列表数据
	function renderCouponData(dataList) {
		//获取模板内容
		var tplHtml = $id("couponRowTpl").html();
		//生成/编译模板
		var htmlTpl = laytpl(tplHtml);
		//根据模板和数据生成最终内容
		var htmlText = htmlTpl.render(dataList);
		//使用生成的内容
		$id("couponList").html(htmlText);
	}
	//------------------------------------------------业务处理-------------------------------------------------
	//
	function recombineCoupon(coupons) {
		if (coupons != null && coupons.length > 0) {
			for (var i = 0; i < coupons.length; i++) {
				coupons[i].typeName = getTypeName(coupons[i].type);
				coupons[i].startTime = Date.parseAsDate(coupons[i].startTime)
						.format("yyyy.MM.dd");
				coupons[i].endTime = Date.parseAsDate(coupons[i].endTime)
						.format("yyyy.MM.dd");
			}
			return coupons;
		}
		return [];
	}
	//处理状态
	function getTypeName(type) {
		var typeName = null;
		switch (type) {
		case "nopay":
			typeName = "免付券";
			break;
		case "sprice":
			typeName = "特价券";
			break;
		case "deduct":
			typeName = "抵金券";
			break;
		}
		return typeName;
	}
	//优惠券数量赋值
	function setCouponCount(couponCount){
		if (couponCount != null && couponCount > 0) {
			$("ul.mod-command li i:eq(" + 0 + ")").show();
			$("ul.mod-command li i:eq(" + 0 + ")").html(couponCount);
		} else {
			$("ul.mod-command li i:eq(" + 0 + ")").hide();
		}
	}
	//刷新分页插件
	function refPaginator() {
		//同步GqPaginator数据模型
		qry_pagination.currentPage = qry_pagination.pageNumber;

		paginatorCtrl.jqPaginator('option', qry_pagination);
	}
	//点击coupon状态查询
	$("ul.mod-command li").click(function() {
		var couponState = $(this).attr("data-id");
		$(this).siblings().removeClass('active').end().addClass('active');
		if (couponState) {
			delete qry_filterItems.couponType;
			$(".couponType li a").css('color', '#646464');
			merge(qry_filterItems, {
				couponState : couponState
			});
		} else {
			delete qry_filterItems.couponState; //清空filterItems中的品牌项
		}
		filterAndQuery();
	});
	//点击coupon类型
	$(".couponType li").click(function() {
		var couponType = $(this).attr("data-id");
		$(".couponType li a").css('color', '#646464');
		$(this).find("> a").css('color', '#c13030');
		//$(this).find("> a").siblings().removeClass('active').end().addClass('active');
		if (couponType) {
			merge(qry_filterItems, {
				couponType : couponType
			});
		} else {
			delete qry_filterItems.couponType; //清空filterItems中的品牌项
		}
		filterAndQuery();
	});
	
	// 删除提示
	function onDeleteConFirm(id) {
		var msg = "确定要删除优惠券吗？";
		var yesHandler = function(layerIndex) {
			theLayer.hide();
			deleteUserCoupon(id);
		};
		var noHandler = function(layerIndex) {
			theLayer.hide();
		};
		var theLayer = Layer.confirm(msg, yesHandler, noHandler);
	}
	
	//删除优惠券
	function deleteUserCoupon(id) {
		var ajax = Ajax.post("/user/coupon/deleted/do");
		var postData = {
			id : id
		};
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var data = result.data;
				if(data==true){
					//删除一行
					$id("coupon"+id).remove();
					//
					getCouponItemCount();
				}else{
					Layer.msgWarning("删除失败");
				}
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	//查询优惠券未使用数量
	function getCouponItemCount() {
		var ajax = Ajax.post("/user/coupon/count/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var couponCount = result.data;
				setCouponCount(couponCount);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	//-------------------------------------------------初始化-------------------------------------------------
	//初始化分页控件
	function initGqPaginator() {
		paginatorCtrl = $id("jqPaginator")
				.jqPaginator(
						{
							totalCount : 0,
							pageSize : qry_pageSize,
							prev : '<a class="prev" href="javascript:void(0);">上一页<\/a>',
							next : '<a class="next"  href="javascript:void(0);">下一页<\/a>',
							page : '<a class="active" href="javascript:void(0);">{{page}}<\/a>',
							onPageChange : function(pageNumber) {
								pageNoAndQuery(pageNumber);
							}
						});
	}
	//--------------------------------------------------初始化分页信息------------------------------------------------
	//分页大小（页面级）
	var qry_pageSize = 5;
	// [搜索用]的关键字符串
	var qry_keywords = null;
	//
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
		// qry_filterItems = {...};
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

	function msgInfo(productId) {
		if(productId != null){
			var pageUrl =makeUrl(getAppUrl("/product/detail/jsp"),{productId:productId});
			setPageUrl(pageUrl, "_blank");
		}
	}
</script>
</body>
<script type="text/html" id="couponRowTpl">
	{{# var coupons = d; }}
	{{# if(!isNoB(coupons)){ }}
		{{# for(var i=0;i<coupons.length; i++){ }}
		{{# var coupon=coupons[i]}}
		<tr id="coupon{{coupon.id}}">
		<td colspan="4">
		{{# if(coupon.invalid==false){ }}
			{{# if(coupon.type=="nopay"){ }}
				<div class="coupon-detail coupon-detail1">
				<div class="coupon-img coupon-img1">
			{{# }else if(coupon.type=="sprice"){ }}
				<div class="coupon-detail coupon-detail3">
				<div class="coupon-img coupon-img3">
			{{# }else{ }}
				<div class="coupon-detail coupon-detail2">
				<div class="coupon-img coupon-img2">
			{{# } }}
		{{# }else{ }}
			<div class="coupon-detail disabled">
			{{# if(coupon.type=="nopay"){ }}
				<div class="coupon-img coupon-img1">
			{{# }else if(coupon.type=="sprice"){ }}
				<div class="coupon-img coupon-img3">
			{{# }else{ }}
				<div class="coupon-img coupon-img2">
			{{# } }}
		{{# } }}
		
		<h1>{{coupon.typeName}}</h1>
		{{# if(!isNoB(coupon.price)&&coupon.price>0){}}
			<h2>¥&nbsp;{{coupon.price}}</h2>
		{{# } }}
		<h3>{{coupon.startTime}}-{{coupon.endTime}}</h3>
		{{# if(!isNoB(coupon.limitAmount)&&coupon.limitAmount>0){ }}
			<h3>【消费满&nbsp;{{coupon.limitAmount}}&nbsp;可用】</h3>
		{{# }else{ }}
			<h3></h3>
		{{# } }}
		</div>
		<div class="coupon-text">
		<dl class="labels labels1">
		<dt><label>券编号：</label></dt>
		<dd>{{coupon.no}}</dd>
		</dl>
		<dl class="labels labels1">
		<dt><label>校验码：</label></dt>
		<dd>{{coupon.checkCode}}</dd>
		</dl>
		<dl class="labels labels1">
		<dt><label>品类限制：</label></dt>
		<dd>{{coupon.productName || ""}}</dd>
		</dl>
		<dl class="labels labels1">
		<dt><label>说明：</label></dt>
		{{#　if(coupon.type=='nopay'){ }}
			<dd>凭此券免费兑换{{strMaxWidth(coupon.productName,25) || ""}}</dd>
		{{#　} }}
		{{#　if(coupon.type=='sprice'){ }}
			<dd>凭此券可购买{{strMaxWidth(coupon.productName,25) || ""}}只需{{coupon.price}}元</dd>
		{{#　} }}
		{{#　if(coupon.type=='deduct'){ }}
			<dd>凭此券可抵扣{{{strMaxWidth(coupon.productName,25) || ""}}{{coupon.price}}元</dd>
		{{#　} }}
		</dl>
		</div>
		</div>
		</td>
		<td>
			{{# if(coupon.invalid==false){ }}
				<input class="btn-normal btn-w70 mb10" type="button" value="立即使用" onclick="msgInfo({{coupon.productId}})" /><br/>
			{{# } }}
			<input class="btn-normal btn-w70 mb10" type="button" onclick="onDeleteConFirm({{coupon.id}})" value="删除" /></td>
		</tr>
		{{# } }}
	{{# } }}

</script>
</html>