<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/base.jsf" %><jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
<div class="content">
    <div class="page-width">
        <div class="crumbs"><a href="">首页</a>&gt;<a href="">用户中心</a>&gt;<a href="">资产中心</a>&gt;<span>服务套餐票</span></div>
        <div class="section">
          		<jsp:include page="/WEB-INF/jsp/include/ucenterMenu.jsp"></jsp:include>
                <div class="section-right1">
                <div class="mod-main" style="display:none">
                    <div class="mod-main-tit clearfix">
                        <ul class="mod-command">
                            <li class="active" data-state="unused"><a href="javascript:void(0);">未使用<i id="unusedCount">1</i></a></li>
                            <li data-state="used"><a href="javascript:void(0);">已使用</a></li>
                            <li data-state="cancelled"><a href="javascript:void(0);">已取消</a></li>
                        </ul>
                        <a class="anormal fr" href="">服务套餐票规则说明</a>
                    </div>
                    <table class="order-td td-border-bottom" id="svcPackTickList">
                        
                    </table>
                    <!--分页-->
                    <div class="pager-gap">
                        <div class="fr pager"  id="jqPaginator">
                            <a class="prev disabled" href="#nogo">上一页</a><a class="active" href="#nogo">1</a><a class="next" href="#nogo">下一页</a>
                        </div>
                    </div>
                </div>
            </div>
              </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
<script type="text/javascript">
//未使用的 finshed为false并且 invalid = false并且cancelled=false,已使用finshed为true并且invalid = true并且cancelled=false, 取消为finshed为false并且 invalid = true 并且cancelled=true
//<!--
var resBaseUrl = getResUrl("");
//分页大小（页面级）
var qry_pageSize = 10;
//[搜索用]的关键字符串
var qry_keywords = null;
//
//过滤条件项
var qry_filterItems = {};
//排序项
var qry_sortItems = {};
//分页信息
var qry_pagination = {
	totalCount : 0,
	pageSize : qry_pageSize,
	pageNumber : 1
};

//根据 新的的分页信息（或新的页码） 更新 本地分页信息
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

//根据最新的信息生成新的查询条件
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

//执行统一查询 ==========================================
function doTheQuery() {
	var postData = newQryFilterInfo();
	// ajax post 请求...
	var ajax = Ajax.post("/saleOrder/svc/pack/ticket/list/get");
	//ajax.sync();
	ajax.data(postData);
	ajax.done(function(result, jqXhr) {
		var rows = result.data.rows;
		/* if (isUndef(rows)||isNull(rows)||rows.length==0) {
			$id("blogList").html("<td colspan='5' style='text-align:center' >没有博文</td>");
			return;
		} */
		// 获取模板内容
		var tplHtml = $id("svcPackTickListTpl").html();
		// 生成/编译模板
		var htmlTpl = laytpl(tplHtml);
		// 根据模板和数据生成最终内容
		var htmlText = htmlTpl.render(rows);
		$id("svcPackTickList").html(htmlText);
		var refPagination = result.data.pagination;
		resetPagination(refPagination);
		//注意：jqPaginator的当前页面为 currentPage
		refPagination.currentPage = refPagination.pageNumber;
		// 根据新的分页信息（refPagination）刷新jqPaginator控件
		$("#jqPaginator").jqPaginator("option",refPagination);
		//$id("unusedCount").text(refPagination.totalCount);
		$(".mod-main").show();
	});
	ajax.go();
}

//关键字 等
function keywordsAndQuery() {
	// 重建 qry_keywords
	// qry_keywords = ...;
	qry_keywords= $id("keyword").val();
	// 重置页码
	resetPagination(1);
	//
	doTheQuery();
}
//清除clean
function cleanKeywordsAndQuery() {
	// 重建 qry_keywords
	// qry_keywords = ...;
	qry_keywords=null;
	$id("keyword").val("");
	// 重置页码
	resetPagination(1);
	//
	doTheQuery();
}
//查询 按钮等
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

//排序 按钮等
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

//排序 按钮等
function pageNoAndQuery(pageNo) {
	// 变更页码
	resetPagination(pageNo);
	//
	doTheQuery();
}
function getUnUsedCount(){
	var ajax = Ajax.post("/saleOrder/userSvcPackTicket/unUsed/count/get");
	//ajax.sync();
	ajax.done(function(result, jqXhr) {
		$id("unusedCount").text(result.data);
	});
	ajax.go();
}
//state=unused ,  used ,cancelled
$(function(){
	merge(qry_filterItems, {state:"unused"});
	$("#jqPaginator").jqPaginator({
		totalCount : 0,
		pageSize : qry_pageSize,
		pageNumber : 1,
        prev: '<a class="prev" href="javascript:void(0);">上一页<\/a>',
        next:'<a class="next"  href="javascript:void(0);">下一页<\/a>',
        page: '<a class="active" href="javascript:void(0);">{{page}}<\/a>',
        onPageChange: function (pageNumber) {
        	qry_pagination.pageNumber=pageNumber;
        	doTheQuery();
        }
    });
	getUnUsedCount();
	$("ul.mod-command li").click(function(){
		$(this).siblings().removeClass('active').end().addClass('active');
		var state=$(this).attr("data-state");
		merge(qry_filterItems, {state:state});
		qry_pagination.pageNumber=1;
		doTheQuery();
	});
	$(".btn-normal").live("click",function(){
		var ts=this;
		var ticketId=$(ts).attr("data-id");
		var msg = "服务套餐票确定完成吗？";
		$(ts).attr({
			"disabled" : "disabled"
		});
		var yesHandler = function(layerIndex) {
			var ajax = Ajax.post("/saleOrder/userSvcPackTicket/confirm/finish/do");
			ajax.data({ticketId:ticketId});
			ajax.done(function(result, jqXhr) {
				if(result.type=="info"){
					if(result.data){
						Toast.show(result.message, null, "info",function(){
							doTheQuery();
						});
					}else{
						Toast.show("套餐服务票确认失败", null, "error");
						$(ts).removeAttr("disabled");
					}
				}else{
					Toast.show(result.message, null, "error");
					$(ts).removeAttr("disabled");
				}
			});
			ajax.go();
			theLayer.hide();
		};
		var noHandler = function(layerIndex) {
			$(ts).removeAttr("disabled");
			theLayer.hide();
		};
		//
		var theLayer = Layer.confirm(msg, yesHandler, noHandler);
	});
});
//-->
</script>
</body>
<script type="text/html" id="svcPackTickListTpl">
<thead>
<tr>
    <th class="text-left">
		<ul id="sort-time"></ul>
    </th>
    <th width="150">服务确认码</th>
    <th width="280">隶属订单</th>
   {{# if(qry_filterItems.state=="unused"){}}
    <th width="150">操作</th>
{{# } }}
</tr>
</thead>
<tbody>
{{# var svcPackTickList=d;}}
{{# var len=svcPackTickList.length;}}
{{# if (isUndef(svcPackTickList)||isNull(svcPackTickList)||len==0) { }}
<tr>
<td colspan="4">
	没有符合条件的服务套餐票
</td>
</tr>
{{# }else{ }}
{{#  for(var i=0,len=svcPackTickList.length;i<len;i++){ }}
{{#  var svcPackTick=svcPackTickList[i]; }}
<tr>
<td class="text-left">
    <div class="package-ticket">
    	<div class="title">
    		{{svcPackTick.svcPackName}}
    	</div>
    	<div class="cont">
    		<img src="{{svcPackTick.fileBrowseUrl}}" title="{{svcPackTick.svcName}}"/>
    		<span class="ellipsis">&nbsp;&nbsp;{{svcPackTick.svcName}}</span>
    	</div>
		<div class="price2">
			<span class="orig">原价：<span>￥{{svcPackTick.svcSalePrice}}</span></span>
			<span class="dis">
				折扣价：<span>￥{{svcPackTick.svcSalePrice.multiply(svcPackTick.rate)}}</span>
			</span>
		</div>
    </div>
</td>
<td><div class="cisc"><span>******</span><a class="cisc-look" href="javascript:void(0);" title="{{svcPackTick.doneCode}}"></a></div></td>
<td>
	<div>订单号：<a href="{{__appBaseUrl}}/saleOrder/detail/jsp?id={{svcPackTick.orderId}}" class="anormal">{{svcPackTick.orderNo}}</a></div>
</td>
{{# if(qry_filterItems.state=="unused"){}}
<td><input class="btn-normal btn-w90 mb10" type="button" value="确认完成"  data-id="{{svcPackTick.id}}"  /></td>
{{# } }}  
</tr>
{{# } }}
{{# } }}
 </tbody>
</script>
</html>