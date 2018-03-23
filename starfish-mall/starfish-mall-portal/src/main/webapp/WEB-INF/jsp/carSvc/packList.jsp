<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/head.jsp" %>
<link rel="stylesheet" href="<%=resBaseUrl%>/lib/jquery/jquery.datetimepicker.css" />
<div class="content">
    <div class="page-width" id="content" style="display:none">
        <div class="crumbs"><a href="">首页</a>&gt;<span>服务套餐</span></div>
        <ul class="steps">
            <li class="active"><i>1</i><span>选择服务套餐</span><em></em></li>
            <li><i>2</i><span>填写核对订单</span><em></em></li>
            <li class="end"><i>3</i><span>订单提交成功</span></li>
        </ul>
        <div class="mod-main" style="padding-bottom: 0">
            <div class="mod-services">
                <h1>服务套餐<span>（单选）</span></h1>
                <dl class="svs-package" >
                    <dd id="svcPack">
                	</dd>
            	</dl>
            </div>
        </div>
        <table class="order-td shopping-td">
            <thead>
            <tr>
                <th>服务项</th>
                <th width="150">单价</th>
                <th width="150">折扣价</th>
                <th width="150">数量</th>
            </tr>
            </thead>
            <tbody id="svcPackSvc">
            </tbody>
        </table>
        <br />
    </div>
    <div class="cart-toolbar">
        <div class="page-width">
            <div class="fr">
                <span class="total-price">合计金额： <span class="red" id="svcPackPrice">¥0.00</span></span>
                <input class="btn btn-h40" type="button" value="下一步" id="btnNext"/>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
<script type="text/javascript"	src="<%=resBaseUrl%>/lib/jquery/jquery.datetimepicker.js"></script>
<script type="text/javascript">
//<!--
//carSvc/pack/list/get
//svcPackId
var svcPackUrl=extractUrlParams(location.href);
var svcPackId=svcPackUrl.svcPackId;
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
var packList=null;
function renderHtml(data, fromId, toId) {
	// 获取模板内容
	var tplHtml = $id(fromId).html();
	// 生成/编译模板
	var theTpl = laytpl(tplHtml);
	// 根据模板和数据生成最终内容
	var htmlStr = theTpl.render(data);
	// 使用生成的内容
	$id(toId).html(htmlStr);
}
//执行统一查询 ==========================================
function doTheQuery() {
	var hintBox = Layer.progress("加载中...");
	var postData = newQryFilterInfo();
	// ajax post 请求...
	var ajax = Ajax.post("/carSvc/svc/pack/list/get");
	//ajax.sync();
	ajax.data(postData);
	ajax.done(function(result, jqXhr) {
		var rows = result.data.rows;
		if (isUndef(rows)||isNull(rows)||rows.length==0) {
			//$id("blogList").html("<td colspan='5' style='text-align:center' >没有博文</td>");
			return;
		}
		packList=rows;
		
		if(isUndef(svcPackId)){
			svcPack=packList[0];
		}else{
			svcPack=packList.find(function(item,i){
				return item.id===parseInt(svcPackId);
			});
			svcPack=svcPack||null;
		}
		if(svcPack!=null){
			renderHtml(svcPack, "svcPackSvcTpl", "svcPackSvc");
		}else{
			
		}
		$id("svcPackPrice").text("¥"+svcPack.amountInfo.amount.toFixed(2));
		renderHtml(packList, "svcPackTpl", "svcPack");
		var refPagination = result.data.pagination;
		resetPagination(refPagination);
		//注意：jqPaginator的当前页面为 currentPage
		refPagination.currentPage = refPagination.pageNumber;
		// 根据新的分页信息（refPagination）刷新jqPaginator控件
		//$("#jqPaginator").jqPaginator("option",refPagination);
	
		if(postData.filterItems.published){
			$(".viewTd").show();
		}else{
			$(".viewTd").hide();
		}
		$id("content").show();
	});
	ajax.always(function() {
		hintBox.hide();
	});
	ajax.fail(function(result, jqXhr){
		if (result.message == "timeout") {
			toast("请求超时！", null, "warning", function() {});
		} else {
			toast(result.message, null, "error", function() {});
		}
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

var resBaseUrl = getResUrl("");
var svcPack=null;
$(function(){
	doTheQuery();
	$("div.control-checkbox-analog>a").live("click",function(){
		var dataId=$(this).attr("data-id");
		svcPack=packList.find(function(item,i){
			return item.id===parseInt(dataId);
		});
		svcPack=svcPack||null;
		if(svcPack!=null){
			renderHtml(svcPack, "svcPackSvcTpl", "svcPackSvc");
			$(this).addClass("active");
		}
		$id("svcPackPrice").text("¥"+svcPack.amountInfo.amount.toFixed(2));
		$(this).parent().siblings().find("a").removeClass("active");
	});
	$id("btnNext").click(function(){
		location.replace(getAppUrl("/saleOrder/svc/pack/submit/jsp?id="+svcPack.id));
	});
});
//-->
</script>

</body>
<script type="text/html" id="svcPackTpl">
{{# var svcPackList = d; }}
{{# for(var i=0, len=svcPackList.length; i<len ; i++) {  }}
{{# var sPack = svcPackList[i]; }}
<div class="control-checkbox-analog" >
	<a 
{{# if(svcPack!=null&&svcPack.id==sPack.id){ }}
class="active"
{{# } }}
href="javascript:;" title="{{sPack.name}}" data-id="{{sPack.id}}">
    	<img src="{{sPack.fileBrowseUrl}}" title="{{sPack.name}}"/>
    	<span class="ellipsis">{{sPack.name}}</span>
    	<em></em>
    	<i></i>
	</a>
</div>
{{# } }}
</script>
 <script type="text/html" id="svcPackSvcTpl">
{{# var sPack = d; }}
{{# var svcList = d.packItemList; }}
 {{# sPack.price = 0; }}
 {{# for(var i=0, len=svcList.length; i<len ; i++) {  }}
{{# var svc = svcList[i]; }}
 <tr>
 <td>
     <dl class="goods-item goods-item2">
         <dt><a href="javascript:void(0)"><img src="{{svc.fileBrowseUrl}}" title="{{svc.svcName}}"/></a></dt>
         <dd class="text"><a href="">{{svc.svcName}}</a></dd>
     </dl>
 </td>
 <td>¥{{svc.svcSalePrice.toFixed(2)}}</td>
 <td>¥{{svc.amountInfo.amount.toFixed(2)}}</td>
 <td>
     1
 </td>
</tr>
{{# } }}
</script>
</html>