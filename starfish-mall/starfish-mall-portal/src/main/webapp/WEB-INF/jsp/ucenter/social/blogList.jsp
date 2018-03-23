<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/base.jsf" %><jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
<div class="content">
    <div class="page-width">
        <div class="crumbs"><a href="">首页</a>&gt;<a href="">用户中心</a>&gt;<span>我的博文</span></div>
        <div class="section">
           <jsp:include page="/WEB-INF/jsp/include/ucenterMenu.jsp"></jsp:include>
            <div class="section-right1">
                <div class="mod-main">
                    <!--右搜索-start-->
                    <div class="mod-main-tit clearfix">
                        <ul class="mod-command">
                            <li class="active"  published="true"><a href="javascript:void(0)">我的博文</a></li>
                            <li  published="false"><a href="javascript:void(0)">草稿箱<i id="draftBlogCount">0</i></a></li>
                        </ul>
                        <div class="mod-search">
                            <input class="search-input" type="text" placeholder="关键字" id="keyword"><input class="btn search-btn" type="button" id="btnSearch" />
                        </div>
                    </div>
                    <!--右搜索-end-->
                    <table class="order-td border-noright">
                        <tr class="title title1">
						<th width="500">博文名称</th>
						<th class="viewTd">查看</th>
						<th>评论数量</th>
						<th>发表时间</th>
						<th>操作</th>
						</tr>
                        <tbody id="blogList" >
                        </tbody>
                    </table>
                    <!--分页-->
                    <div class="pager-gap">
                        <div class="fr pager" id="jqPaginator">
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
	var ajax = Ajax.post("/social/user/blog/list/get");
	ajax.sync();
	ajax.data(postData);
	ajax.done(function(result, jqXhr) {
		var rows = result.data.rows;
		if (isUndef(rows)||isNull(rows)||rows.length==0) {
			$id("blogList").html("<td colspan='5' style='text-align:center' >没有博文</td>");
			return;
		}
		// 获取模板内容
		var tplHtml = $id("blogRowTpl").html();
		// 生成/编译模板
		var htmlTpl = laytpl(tplHtml);
		// 根据模板和数据生成最终内容
		var htmlText = htmlTpl.render(rows);
		$id("blogList").html(htmlText);
		var refPagination = result.data.pagination;
		resetPagination(refPagination);
		//注意：jqPaginator的当前页面为 currentPage
		refPagination.currentPage = refPagination.pageNumber;
		// 根据新的分页信息（refPagination）刷新jqPaginator控件
		$("#jqPaginator").jqPaginator("option",refPagination);
		if(postData.filterItems.published){
			$(".viewTd").show();
		}else{
			$(".viewTd").hide();
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

$(function() {
	merge(qry_filterItems, {published:true});
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
	
	$(document).on("click", ".btnView", function() {
		window.open(getAppUrl("/social/blog/detail/jsp?id=" + $(this).attr("blogId")));
	});
	//merge(qry_filterItems, {...});	
	$(document).on("click", ".btnEdit", function() {
		window.open(getAppUrl("/ucenter/blog/write/jsp?id=" + $(this).attr("blogId")));
	});
	///user/blog/draft/count/get
	$(document).on("click", ".btnDel", function() {
		var blogId = $(this).attr("blogId");
		var yesHandler = function(layerIndex) {
			var ajax = Ajax.post("/social/user/blog/delete/do");
			ajax.data({
				id : parseInt(blogId)
			});
			ajax.done(function(result, jqXhr) {
				if (result.type = "info") {
					var callback = function() {
						location.replace(getAppUrl("/ucenter/blog/list/jsp"));
					};
					var closeDelay = 3000;
					Layer.msgSuccess(result.message, callback, closeDelay);
				} else {
					var theLayer = Layer.error(result.message, function(layerIndex) {
						theLayer.hide();
					});
				}
			});
			ajax.go();
		};
		var noHandler = function(layerIndex) {
		};
		var theLayer = Layer.confirm("确定删除此博文吗？", yesHandler, noHandler);
	});
	
	var ajax = Ajax.post("/social/user/blog/draft/count/get");
	ajax.done(function(result, jqXhr) {
		if (result.type = "info") {
			$id("draftBlogCount").text(result.data)
		} 
	});
	ajax.go();
	
	$id("btnSearch").click(function(){
		keywordsAndQuery();
	});
	
	$(".mod-command li").click(function() {
		var published = $(this).attr("published");
		$id("keyword").val("");
		if (published == "true") {
			merge(qry_filterItems, {published:true});
			$(".viewTd").show();
		} else {
			merge(qry_filterItems, {published:false});
			$(".viewTd").hide();
		}
		filterAndQuery();
		$(this).siblings().removeClass('active').end().addClass('active');
	});
});
//-->
</script>
</body>
<script type="text/html" id="blogRowTpl">
{{# var blogs = d; }}
{{# for(var i=0, len=blogs.length; i<len ; i++) {  }}
{{# var blog = blogs[i]; }}
{{# var saleOrder = blog.saleOrder; }}
{{# var shop=null;}}
{{# var saleOrderSvcs=null;}}
{{# var userBlogImg = blog.userBlogImg; }}
{{# if(saleOrder!=null){}}
	{{# shop=saleOrder.shop;}}
	{{# saleOrderSvcs=saleOrder.saleOrderSvcs;}}
{{#	}}}
	<tr id="tr_{{blog.id}}">
      <td>
          <div class="text-left">
              <a class="anormal btnEdit" href="javascript:void(0);" blogId="{{blog.id}}">{{blog.title}}</a>
          </div>
      </td>
	<td class="viewTd"><a href="javascript:void(0)" class="btnView"  blogId="{{blog.id}}"><img src="{{resBaseUrl}}/image/toolbar/viewBLog.gif"/></a></td>
      <td>
          {{blog.commentCount}}
      </td>
       <td>{{blog.createTime}}</td>
      <td>
          <a class="anormal btnEdit" href="javascript:void(0);" blogId="{{blog.id}}">编辑</a><a class="anormal ml10 btnDel" href="javascript:void(0);" blogId="{{blog.id}}">删除</a>
      </td>
  </tr>
{{# } }}
</script>
</html>