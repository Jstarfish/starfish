<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/head.jsp" %>
<style>
 .faq-detail {
    padding: 20px 50px;
    border: 0px solid #e4e4e4;
    margin-bottom: 10px;
    overflow: hidden;
    zoom:1;
    min-height:300px;
}
 </style>
 <div class="content">
    <div class="page-width">
        <div class="crumbs"><a href="">首页</a>&gt;<span>帮助中心</span></div>
        <div class="section">
            <div class="section-left1">
                <div class="munu-tit">
                    <h1>常见问题</h1>
                    <!--左菜单-开始-->
                    <ul id="helpMenu">
                    </ul>
                    <!--左菜单-结束-->
                </div>
            </div>
            <div class="section-right1">
                <div class="mod-main">
                    <div class="help-search">
                        <div class="mod-search">
                            <input class="search-input" type="text" placeholder="请输入问题关键词，如'订单'" id="keyword"><input class="btn search-btn" type="button" id="btnSearch"/>
                        </div>
                        <div class="help-contact">
                            <i></i><h1>亿投车吧服务热线</h1><h2>010-52416399</h2>
                        </div>
                    </div>
                    <div class="help-cont">
                        <!-- 首页 -->
                        <!--热点问题-start-->
                        <div class="help-block" id="faqIndexDiv">
                            <h1>热点问题</h1>
                            <div class="block-cont block-cont1">
                                <div class="slideTxtBox help-slide">
                                    <div class="hd">
                                        <ul id="groupTitleList"></ul>
                                    </div>
                                    <div class="bd">
                                        <ul class="bd-cont" id="groupFaqList">
                                          
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!--热点问题-end-->
                        
                        <!-- 列表 -->
                     	<!--购物流程-start-->
                        <div class="help-block"  id="faqListDiv" style="display:none">
                            <h1 id="tagH"><a class="active" href="" id="catName" style="">全部</a>&gt;
                        <a href=""  id="groupName">推荐</a></h1>
                            <div class="block-cont">
                                <ul class="bd-cont" id="faqList">
                                </ul>
                            </div>
                        </div>
                        <!--购物流程-end-->
                        
                        <!-- 搜索start -->
                        <div class="search-result" id="faqSearchDiv" style="display:none">
                            <ul class="result-list" id="noResult"><li class="no-result">
								抱歉，没有您搜索的内容
							</li></ul>
                                <h1 id="searchTitle">
                                关于“<span class="orange" id="keywordText">订单</span>”，共找到<span class="orange" id="searchResultCount">380</span>相关问题
                            </h1>
                            <ul class="result-list" id="faqSearchResult">
                            </ul>
                            <div class="pager-gap">
                                <div class="fr pager" id="pager">
                                    <a class="prev disabled" href="#nogo">上一页</a><a class="active" href="#nogo">1</a><a href="#nogo">2</a><a href="#nogo">3</a><a class="more" href="#nogo">...</a><a href="#nogo">10</a><a class="next" href="#nogo">下一页</a><span>共10页</span><span>到第 <input class="page-text" type="text"/>页</span><span><input class="btn-normal" type="button" value="确定" /></span>
                                </div>
                            </div>
                        </div>
                        <!-- 搜索end -->
                         <!-- 常见问题详情start -->
                        <div class="help-block"  id="faqDetailDiv" style="display:none">
                            <h1 id="tagHD"></h1>
                            <div class="block-cont" id="faqDetail">
                            </div>
                        </div>
                        <!-- 常见问题详情end -->
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
<script type="text/javascript">
//<!--
var faqUrl = extractUrlParams(location.href);
var groupId = faqUrl.groupId;
var faqId = faqUrl.id;
var initCId = groupId;

// 分页大小（页面级）
var qry_pageSize = 10;
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
var isLoad=false;
// 执行统一查询 ==========================================
function doTheQuery() {
	var postData = newQryFilterInfo();
	// ajax post 请求...
	var ajax = Ajax.post("/comn/faq/list/get");
	ajax.data(postData);
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			var rows = result.data.rows;
			if (isUndef(rows)||isNullOrBlank(rows)|| $.isEmptyObject(rows) || rows.length == 0) {
				$(".no-result").show();
				$id("pager").hide();
				$id("noResult").show();
				$id("faqList").html("");
				$id("faqList").hide();
				$id("searchTitle").hide();
				$id("faqSearchDiv").siblings().hide().end().show();
				return;
			}
			$id("noResult").hide();
			var keyword=$id("keyword").val();
			if(keyword.isBlank()){//列表页面
				// 获取模板内容
				var tplHtml = $id("faqRowTpl").html();
				// 生成/编译模板
				var htmlTpl = laytpl(tplHtml);
				// 根据模板和数据生成最终内容
				var htmlText = htmlTpl.render(rows);
				$id("faqList").html(htmlText);
				$id("faqListDiv").siblings().hide().end().show();
			}else{//搜索页面  
				// 获取模板内容
				var tplHtml = $id("faqSearchResultTpl").html();
				// 生成/编译模板
				var htmlTpl = laytpl(tplHtml);
				// 根据模板和数据生成最终内容
				var htmlText = htmlTpl.render(rows);
				$id("faqSearchResult").html(htmlText);
				$id("faqSearchDiv").siblings().hide().end().show();
				$id("searchTitle").show();
				$id("pager").show();
			}
			$id("faqList").show();
			$(".no-result").hide();
			var refPagination = result.data.pagination;
			resetPagination(refPagination);
			// 注意：jqPaginator的当前页面为 currentPage
			refPagination.currentPage = refPagination.pageNumber;
			$id("keywordText").text(keyword);
			$id("searchResultCount").text(refPagination.totalCount);
			// 根据新的分页信息（refPagination）刷新jqPaginator控件
			if(!isLoad){
				$("#pager").jqPaginator({
					pageSize : qry_pageSize,
					pageNumber : 1,
					prev : '<a class="prev" href="javascript:void(0);">上一页<\/a>',
					next : '<a class="next"  href="javascript:void(0);">下一页<\/a>',
					page : '<a class="active" href="javascript:void(0);">{{page}}<\/a>',
					onPageChange : function(pageNumber) {
						qry_pagination.pageNumber = pageNumber;
						if(isLoad){
							doTheQuery();
						}else{
							isLoad=true;
						}
					}
				});
			}else{
				$("#pager").jqPaginator("option", refPagination);
			}
			
		}
	});
	ajax.go();
}

// 关键字 等
function keywordsAndQuery() {
	// 重建 qry_keywords
	// qry_keywords = ...;
	qry_keywords = $id("keyword").val();
	// 重置页码
	resetPagination(1);
	//
	doTheQuery();
}
// 清除clean
function cleanKeywordsAndQuery() {
	// 重建 qry_keywords
	// qry_keywords = ...;
	qry_keywords = null;
	$id("keyword").val("");
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
var menuModelRule = {
	one : {
		childNodes : "faqGroups"
	}
};
var isFaqPageLoader = false;
function doOnMenuItemClick(cid, text) {
	var catName = $(this).parent().parent().find(">a").text();
	if (catName.trim() != "") {
		$id("catName").text(catName);
		console.log("clicked : " + cid + " , " + text);
		$id("groupName").text(text);
		if (!isFaqPageLoader) {// 判断不是常见问题详情页面加载
			$id("faqDetail").hide();
			$id("faqDetail").text("");
			merge(qry_filterItems, {
				groupId : cid
			});
			filterAndQuery();
		} else {
			isFaqPageLoader = false;
		}
	}
}
function getGroupFaqs(groupId){
	var ajax = Ajax.post("/comn/faq/list/get");
	ajax.data({pagination :{
			pageSize : 10,
			totalCount : 1,
			pageNumber : 1
		},filterItems : {groupId:groupId}});
	ajax.done(function(resultS, jqXhr) {
		if (resultS.type == "info") {
			var totalCount = resultS.data.pagination.totalCount;
			var list=resultS.data.rows;
			if(isNull(list)||list.length==0){
				return;
			}
			// 获取模板内容
			var tplHtml = $id("groupFaqListTpl").html();
			// 生成/编译模板
			var htmlTpl = laytpl(tplHtml);
			// 根据模板和数据生成最终内容1
			var htmlText = htmlTpl.render(list);
			$id("groupFaqList").html(htmlText);
		} else {
			Layer.warning(result.message);
		}
	});
	ajax.go();
}
function getRandomNum(Min,Max){   
var Range = Max - Min;   
var Rand = Math.random();   
return (Min + Math.round(Rand * Range));   
}
$(function() {
	$id("keyword").val("");
	if (faqId) {
		var ajax = Ajax.post("/comn/faq/get");
		ajax.data({
			id : faqId
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var faq = result.data;
				if (isUndef(faq)||isNullOrBlank(faq)|| $.isEmptyObject(faq) || faq.length == 0) {
					$(".no-result").show();
					$id("noResult").show();
					return;
				}
				// 获取模板内容
				var tplHtml = $id("faqDetailTpl").html();
				// 生成/编译模板
				var htmlTpl = laytpl(tplHtml);
				// 根据模板和数据生成最终内容
				var htmlText = htmlTpl.render(faq);
				$id("faqDetail").html(htmlText);
				$id("faqDetail").show();
				$id("faqDetailDiv").siblings().hide().end().show();
				initCId = faq.groupId;
				isFaqPageLoader = true;
			} else {
				var theLayer = Layer.error(result.message, function(layerIndex) {
					theLayer.hide();
				});
			}
		});
		ajax.go();
	}
	var ajax = Ajax.post("/comn/faq/cat/list/get");
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			var carList=result.data;
			var len=carList.length;
			//TODO 随机取出分组 按分类个数  随机取5个分类下的随机一个分组
			renderMenuTree("helpMenu", menuList2MenuModel(carList, menuModelRule), doOnMenuItemClick, initCId);
			
			var groups=[];
			var j=0;
			for(var i=0;i<len;i++){
				var faqGroups=carList[i].faqGroups;
				var glen=faqGroups.length;
				if(glen>0){
					var faqGroup=faqGroups[0];
					groups[j]=faqGroup;
					j++;
				}
			}
			// 获取模板内容
			var tplHtml = $id("groupTitleListTpl").html();
			// 生成/编译模板
			var htmlTpl = laytpl(tplHtml);
			// 根据模板和数据生成最终内容
			var htmlText = htmlTpl.render(groups);
			$id("groupTitleList").html(htmlText);
			var glen=groups.length;
			if(glen>0){
				getGroupFaqs(groups[0].id);				
			}
		}
	});
	ajax.go();
	$id("btnSearch").click(function() {
		keywordsAndQuery();
	});
	$(".faq-top").show();
	$("#groupTitleList>li").live("click",function(){
		var groupId=$(this).attr("groupId");
		getGroupFaqs(groupId);
	});
	
	$("a.faqURL").live("click", function() {
		window.open(getAppUrl("/comn/faq/jsp?id=" + $(this).attr("faqId")));
	});
});
//-->
</script>
</body>
<script type="text/html" id="faqRowTpl">
{{# var faqs = d; }}
{{# for(var i=0, len=faqs.length; i<len ; i++) {  }}
{{# var faq = faqs[i]; }}
<li><a href="javascript:void(0)"  title="{{faq.question}}" faqId="{{faq.id}}" class="faqURL">{{faq.question}}</a></li>
{{# } }}
</script>
<script type="text/html" id="faqSearchResultTpl">
{{# var faqs = d; }}
{{# for(var i=0, len=faqs.length; i<len ; i++) {  }}
{{# var faq = faqs[i]; }}
<li>
	<h2><a href="javascript:void(0)" faqId="{{faq.id}}" title="{{faq.question}}" class="faqURL">{{faq.question}}</a></h2>
    <p>
       {{#if(faq.answer!=null&&faq.answer.length>150){}}
       		{{faq.answer.substring(0,150)}}.....
		{{# }else{ }}
    		{{faq.answer}}
		{{# } }}
    </p>
</li>
{{# } }}
</script>
<script type="text/html" id="faqDetailTpl">
{{# var faq = d; }}
<div class="faq-title" style="text-align:center">
<h2>{{faq.question}}</h2>
</div>
<div class="faq-detail">
{{faq.answer}}
</div>
</script>
<script type="text/html" id="groupTitleListTpl">
{{# var groups = d; }}
{{# for(var i=0, len=groups.length; i<len ; i++) {  }}
{{# var group = groups[i]; }}
<li groupId="{{group.id}}">{{group.name}}<i></i></li>	
{{# } }}
</script>
<script type="text/html" id="groupFaqListTpl">
{{# var faqs = d; }}
{{# for(var i=0, len=faqs.length; i<len ; i++) {  }}
{{# var faq = faqs[i]; }}
<li><a href="javascript:void(0)" faqId="{{faq.id}}" title="{{faq.question}}" class="faqURL">{{faq.question}}</a></li>
{{# } }}
</script>
</html>
