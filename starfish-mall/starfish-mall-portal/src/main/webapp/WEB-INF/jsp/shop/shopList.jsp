<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head.jsp"%>
<div class="content">
	<div class="page-width">
		<div class="crumbs">
			<a href="">首页</a>&gt;<span>线下门店</span>
		</div>
		<div class="mod-main" style="display:none;">
			<div id="carSvcs" class="mod-services"></div>
		</div>
		<div class="mod-shops">
			<div class="mod-shops-title clearfix">
				<span>您所在的区域：</span>
				<!--select-->
				<div id="provinceSelect" class="select-special">
					<span class="select-special-l">省份</span> <span
						class="select-special-r"><i></i></span>
					<div class="clear"></div>
					<ul>
					</ul>
				</div>
				<!--end-select-->
				<!--select-->
				<div id="citySelect" class="select-special">
					<span class="select-special-l">城市</span> <span
						class="select-special-r"><i></i></span>
					<div class="clear"></div>
					<ul>
					</ul>
				</div>
				<!--end-select-->
				<!--select-->
				<div id="countySelect" class="select-special">
					<span class="select-special-l">区/县</span> <span
						class="select-special-r"><i></i></span>
					<div class="clear"></div>
					<ul>
					</ul>
				</div>
				<!--end-select-->
				<div class="mod-search">
					<input id="searchIput" class="search-input" type="text"
						placeholder="请输入您的详细地址"><input id="searchBtn"
						class="btn search-btn" type="button" />
				</div>
			</div>
			<div class="mod-shops-title1">
				<ul id="shopsSorter">
				</ul>
			</div>
			<div id="shopList">
				<ul class="mod-shops-list">
				</ul>
			</div>
			<div id="noRecordDiv" style="display: none;" class="no-result">
				抱歉，暂无查询结果。</div>
			<!--分页-->
			<div class="pager-gap1">
				<div id="jqPaginator" class="fr pager"></div>
			</div>
		</div>
	</div>
</div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
<script type="text/javascript">
	// 与后台PaginationFilter对应的JSON结构定义
	var pageSize = 6;
	var pageInfo = {};
	var pagination = {
		pageSize : pageSize,
		totalCount : 1,
		pageNumber : 1
	};
	var filterItems = {};
	pageInfo.pagination = pagination;
	pageInfo.filterItems = filterItems;

	//加载省份
	function loadProvince() {
		var ajax = Ajax.get("/setting/region/list/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				renderHtml(result.data, "provinceSelectTpl", "provinceSelect");
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}

	// 根据页面选择的省加载市
	function loadCity(provinceId) {
		var ajax = Ajax.get("/setting/region/list/get");
		ajax.params({
			parentId : provinceId
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				renderHtml(result.data, "citySelectTpl", "citySelect");
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}

	// 根据页面选择的市加载区/县
	function loadCounty(cityId) {
		var ajax = Ajax.get("/setting/region/list/get");
		ajax.params({
			parentId : cityId
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				renderHtml(result.data, "countySelectTpl", "countySelect");
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}

	function resetCitySelect() {
		$id("citySelect").children(".select-special-l").html("城市");
		$id("citySelect").children("ul").html("");
	}

	function resetCountySelect() {
		$id("countySelect").children(".select-special-l").html("区/县");
		$id("countySelect").children("ul").html("");
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
		$id(toId).find("ul").html(htmlStr);
	}

	// 选择省级区域的响应函数
	function provinceSelect(obj) {
		// 设置控件中选中的区域名称
		var text = $(obj).html();
		$(obj).parents("ul").siblings(".select-special-l").html(text);
		// 重置市级和县/区级选择控件的内容
		resetCitySelect();
		resetCountySelect();
		// 刷新市级区域选项
		loadCity(obj.id);
		// 刷新门店列表
		getShopsByRegion(parseInt(obj.id), 1);
	}

	// 选择市级区域的响应函数
	function citySelect(obj) {
		// 设置控件中选中的区域名称
		var text = $(obj).html();
		$(obj).parents("ul").siblings(".select-special-l").html(text);
		// 重置县/区级选择控件的内容
		resetCountySelect();
		// 刷新区/县级区域选项
		loadCounty(obj.id);
		// 刷新门店列表
		getShopsByRegion(parseInt(obj.id), 2);
	}

	// 选择区/县级区域的响应函数
	function countySelect(obj) {
		// 设置控件中选中的区域名称
		var text = $(obj).html();
		$(obj).parents("ul").siblings(".select-special-l").html(text);
		// 刷新门店列表
		getShopsByRegion(parseInt(obj.id), 3);
	}

	// 根据区域过滤条件获取门店列表
	function getShopsByRegion(regionId, level) {
		// 更新分页控件
		$("#jqPaginator").jqPaginator({
			totalCount : 1,
			pageSize : pageSize,
			visiblePages : 5,
			currentPage : 1,
			wrapper : '<ul class="msg-consult"></ul>',
			prev : '<a class="prev" href="javascript:void(0);">上一页<\/a>',
			next : '<a class="next"  href="javascript:void(0);">下一页<\/a>',
			page : '<a class="active" href="javascript:void(0);">{{page}}<\/a>',
			onPageChange : function(pageNumber) {
				var ajax = Ajax.post("/shop/list/get");
				pageInfo.pagination.pageNumber = pageNumber;
				pageInfo.filterItems = {};
				pageInfo.filterItems.regionId = regionId;
				pageInfo.filterItems.level = level;
				ajax.data(pageInfo);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						var totalCount = result.data.pagination.totalCount;
						if (result.data.rows && result.data.rows.length > 0) {
							$id("shopsSorter").css("display", "inline-block");
							$id("noRecordDiv").css("display", "none");
						} else {
							$id("shopsSorter").css("display", "none");
							$id("noRecordDiv").css("display", "block");
							//$id("shopList").html("");
						}

						renderHtml(result.data.rows, "shopListTpl", "shopList");
						$("#jqPaginator").jqPaginator("option", {
							totalCount : totalCount,
							pageSize : pageSize
						})
					} else {
						Layer.warning(result.message);
					}
				});
				ajax.go();
			}
		});
	}

	// 根据详细地址搜索门店列表
	function queryShops() {
		// 更新分页控件
		$("#jqPaginator").jqPaginator({
			totalCount : 1,
			pageSize : pageSize,
			visiblePages : 5,
			currentPage : 1,
			wrapper : '<ul class="msg-consult"></ul>',
			prev : '<a class="prev" href="javascript:void(0);">上一页<\/a>',
			next : '<a class="next"  href="javascript:void(0);">下一页<\/a>',
			page : '<a class="active" href="javascript:void(0);">{{page}}<\/a>',
			onPageChange : function(pageNumber) {
				var text = $id("searchIput").val();
				var ajax = Ajax.post("/shop/list/get");
				pageInfo.pagination.pageNumber = pageNumber;
				pageInfo.filterItems = {};
				pageInfo.filterItems.address = text;
				ajax.data(pageInfo);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						var totalCount = result.data.pagination.totalCount;
						if (result.data.rows && result.data.rows.length > 0) {
							$id("shopsSorter").css("display", "inline-block");
							$id("noRecordDiv").css("display", "none");
						} else {
							$id("shopsSorter").css("display", "none");
							$id("noRecordDiv").css("display", "block");
							//$id("shopList").html("");
						}

						renderHtml(result.data.rows, "shopListTpl", "shopList");
						$("#jqPaginator").jqPaginator("option", {
							totalCount : totalCount,
							pageSize : pageSize
						})
					} else {
						Layer.warning(result.message);
					}
				});
				ajax.go();
			}
		});
	}

	// 获取全部门店列表
	function loadShops() {
		// 更新分页控件
		$("#jqPaginator").jqPaginator({
			totalCount : 1,
			pageSize : pageSize,
			visiblePages : 5,
			currentPage : 1,
			wrapper : '<ul class="msg-consult"></ul>',
			prev : '<a class="prev" href="javascript:void(0);">上一页<\/a>',
			next : '<a class="next"  href="javascript:void(0);">下一页<\/a>',
			page : '<a class="active" href="javascript:void(0);">{{page}}<\/a>',
			onPageChange : function(pageNumber) {
				var ajax = Ajax.post("/shop/list/get");
				pageInfo.pagination.pageNumber = pageNumber;
				pageInfo.filterItems = {};
				ajax.data(pageInfo);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						var totalCount = result.data.pagination.totalCount;
						if (result.data.rows && result.data.rows.length > 0) {
							$id("shopsSorter").css("display", "inline-block");
							$id("noRecordDiv").css("display", "none");
						} else {
							$id("shopsSorter").css("display", "none");
							$id("noRecordDiv").css("display", "block");
							//$id("shopList").html("");
						}

						renderHtml(result.data.rows, "shopListTpl", "shopList");
						$("#jqPaginator").jqPaginator("option", {
							totalCount : totalCount,
							pageSize : pageSize
						})
					} else {
						Layer.warning(result.message);
					}
				});
				ajax.go();
			}
		});
	}

	var __sortModel = [ {
		code : "composite",
		text : "综合",
		orders : "desc",
		active : true
	}, {
		code : "satisfaction",
		text : "满意度",
		orders : "desc"
	}, {
		code : "addup",
		text : "累计安装",
		orders : "desc"
	} ];

	function doOnSortClick(code, order) {
		console.log(code + " : " + order);
	}

	function getCarSvcs() {
		var ajax = Ajax.post("/carSvc/svcGroup/list/do");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				if (result.data != null) {
					//获取模板内容
					var tplHtml = $id("carSvcsTpl").html();

					//生成/编译模板
					var theTpl = laytpl(tplHtml);

					//根据模板和数据生成最终内容
					var htmlStr = theTpl.render(result.data);

					//使用生成的内容
					$id("carSvcs").html(htmlStr);
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}

	function selectSvc(obj) {
		$(obj).toggleClass("active");
	}

	$(function() {
		//线下门店-右-门店排序
		renderSorter("shopsSorter", __sortModel, doOnSortClick);

		//getCarSvcs();

		loadProvince();
		// 增加搜索按钮响应
		$id("searchBtn").click(queryShops);
		// Enter快捷键响应
		$(document).keydown(function(e) {
			if (e.which == 13) {
				queryShops();
			}
		});

		loadShops();
	});
</script>
</body>
<script type="text/html" id="provinceSelectTpl" title="省级区域选项生成模版">
	<li><a href="javascript:void(0);" onclick="location.href=getAppUrl('/shop/list/jsp')">全部</a></li>
	{{# for (var i = 0, len = d.length; i < len; i++) { }}
	<li><a id="{{ d[i].id }}" href="javascript:void(0)" onclick="provinceSelect(this)">{{ d[i].name }}</a></li>
	{{# } }}
</script>
<script type="text/html" id="citySelectTpl" title="市级区域选项生成模版">
	{{# for (var i = 0, len = d.length; i < len; i++) { }}
	<li><a id="{{ d[i].id }}" href="javascript:void(0)" onclick="citySelect(this)">{{ d[i].name }}</a></li>
	{{# } }}
</script>
<script type="text/html" id="countySelectTpl" title="区/县级区域选项生成模版">
	{{# for (var i = 0, len = d.length; i < len; i++) { }}
	<li><a id="{{ d[i].id }}" href="javascript:void(0)" onclick="countySelect(this)">{{ d[i].name }}</a></li>
	{{# } }}
</script>
<script type="text/html" id="shopListTpl" title="线下门店列表">
	{{# for (var i = 0, len = d.length; i < len; i++) { }}
		<li>
	        <dl>
	            <dt><a href=""><img src="{{ d[i].shop.fileBrowseUrl }}" alt="" onclick="window.open(getAppUrl('/shop/detail/jsp?shopId={{ d[i].shop.id }}'));" /></a></dt>
	            <dd class="a1">
					<a href="javascript:void(0)" onclick="window.open(getAppUrl('/shop/detail/jsp?shopId={{ d[i].shop.id }}'));">
						<span>门店名称：</span><b>{{ d[i].shop.name }}</b><br />
						<span class="gray1">门店地址：</span>{{ d[i].shop.street }}<br />
						<span>所在区域：</span>{{ d[i].shop.regionName }}<br />
					</a>
				</dd>
	            <dd class="a2">
					<div class="vl-middle">
						<p>7：00-20：00营业</p>
						<p>（春节开业时间为3月1号）</p>
					</div>
				</dd>
	            <dd class="a2">
					<div class="vl-middle">
						<p>共完成110个订单</p>
						<p><a class="ared" href="">41人评价</a>&nbsp;&nbsp;<a class="anormal" href="">查看&gt;&gt;</a></p>
					</div>
				</dd>
	            <dd class="a2">
					<div class="vl-middle">
						<p>满意度</p>
						<span class="star"><i style="width: 90%"></i></span>
					</div>
				</dd>
	            <dd class="a3">
	                <h3>服务项目：</h3>
	                <ul>
					{{# var shopSvcs = d[i].shopSvcs; }}
					{{# if (shopSvcs != null) { }}
						{{# for (var j = 0, svcs_len = d[i].shopSvcs.length; j < svcs_len; j++) { }}
	                    <span>{{ shopSvcs[j].svcName }}</span>
						{{# if (j != svcs_len - 1) { }}
						<span>、</span>
						{{# } }}
						{{# } }}
	                {{# } }}
	                </ul>
	            </dd>
	        </dl>
	    </li>
	{{# } }}
</script>
<script type="text/html" id="carSvcsTpl" title="车辆服务列表">
	{{# var carSvcGroups = d }}
		<h1>服务项目<span>（多选）</span></h1>
	{{# for (var i = 0, len = carSvcGroups.length; i < len; i++) { }}
		<dl>
			<dt>{{ carSvcGroups[i].name }}</dt>
			<dd>
			{{# var carSvcs = carSvcGroups[i].svcxList }}
			{{# for (var j = 0, length = carSvcs.length; j < length; j++) { }}
				<a href="javascript:void(0)" onclick="selectSvc(this)">{{ carSvcs[j].name }}</a>
			{{# } }}
			</dd>
		</dl>
	{{# } }}
</script>
</html>