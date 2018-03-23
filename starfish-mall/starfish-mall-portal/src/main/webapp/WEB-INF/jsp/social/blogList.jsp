<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head.jsp" %>
<div class="content" >
    <div class="page-width">
        <div class="crumbs"><a href="">首页</a>&gt;<span>车友分享</span></div>
        <div class="mod-main">
            <div class="mod-services" style="display:none">
            </div>
        </div>
        <ul class="blog-list" id="dataList">
           
        </ul>
        <div class="page-main">
			<!--分页-->
				<div class="bottom-page" style="text-align: center;">
						<a class="next" href="javascript:void(0)">查看更多晒单</a>
					</div>
				</div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
<script type="text/javascript">
//<!--
var pageNumber = 1;
var pageSize = 10;
var isLastPage = false;
var isSearch = false;
var pageInfo = {};
var pagination = {
	totalCount : 1,
	pageSize : pageSize,
	pageNumber : pageNumber
};
var filterItems = {};
pageInfo.pagination = pagination;
pageInfo.filterItems = filterItems;
var overplus=null;
function loadData() {
	if (!isLastPage) {
		var ajax = Ajax.post("/social/blog/list/get");
		ajax.data(pageInfo);
		ajax.done(function(result, jqXhr) {
			var rows = result.data.rows;
			if (rows == null) {
				isLastPage = true;
				$(".next").html("没有更多晒单");
				return;
			}
			// 获取模板内容
			var tplHtml = $("#blogRowTpl").html();
			// 生成/编译模板
			var htmlTpl = laytpl(tplHtml);

			// 根据模板和数据生成最终内容
			var htmlText = htmlTpl.render(rows);
			// 使用生成的内容
			if (isSearch) {
				$id("dataList").html(htmlText);
			} else {
				$id("dataList").append(htmlText);
			}
			if(overplus==null){
				overplus=rows.totalCount;
			}
			if (rows != null && rows.length < pageSize) {
				isLastPage = true;
				$(".next").html("没有更多晒单");
			} else {
				pageNumber += 1;
				pagination.pageNumber = pageNumber;
			}
			isSearch = false;
		});
		ajax.go();
	}
}
//加载车辆服务分组和车辆服务列表
function loadCarSvc() {
	var ajax = Ajax.post("/carSvc/svcGroup/list/do");
	ajax.data();
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			//获取模板内容
			var tplHtml = $id("carSvcListTpl").html();
			//生成/编译模板
			var theTpl = laytpl(tplHtml);
			//根据模板和数据生成最终内容
			var htmlStr = theTpl.render(result.data);
			//使用生成的内容
			$("div.mod-services").html(htmlStr);
		} else {
			Layer.warning(result.message);
		}
	});
	ajax.go();
}

$(function() {
	loadData();
	loadCarSvc();
	$(".next").click(function() {
		loadData();
	});
	$id("btnSearch").click(function() {
		isSearch = true;
		pageNumber = 1;
		filterItems = {
			keyword : $id("keyword").val()
		};
		loadData(searchParam);
	});
	$(document).on("click", "a.blogDetailUrl", function() {
		window.open(getAppUrl("/social/blog/detail/jsp?id="+$(this).attr("blogId")));
	});
	$(document).on("click", "a.shopDetailUrl", function() {
		window.open(getAppUrl("/shop/detail/jsp?shopId="+$(this).attr("shopId")));
	});
});
//-->
</script>
</body>
<script type="text/html" id="carSvcListTpl" title="查询车辆服务和分组信息列表">
	<h1>服务项目<span>（可多选）</span></h1>
	{{# for(var i = 0, len = d.length; i < len; i++){ }}
         {{# var svcGroup = d[i]; }}
         <dl>
         <dt>{{svcGroup.name}}：</dt>
         <dd>
           {{# if(!isNoB(svcGroup.svcxList)){ }}
           {{# for(var j = 0;j< svcGroup.svcxList.length; j++){ }}
                {{# var svc =svcGroup.svcxList[j]}}
				{{# if(svc.id!=-1){ }}
                <div class="control-checkbox-analog">
				<a id="carSvc{{svc.id}}" data-img="{{svc.fileBrowseUrlIcon2}}" data-imgTwo="{{svc.fileBrowseUrlIcon}}" href="javascript:void(0)" onClick="addSaleCartSvc({{svc.id}})">
					<img src="{{svc.fileBrowseUrlIcon}}" alt=""><span>{{svc.name}}</span><i></i>
				</a>
				</div>
			{{# } }}
			{{# } }}
         	{{# } }}
        </dd>
        </dl>
	{{# } }}
</script>
<script type="text/html" id="blogRowTpl">
		{{# var blogs = d; }}
		{{# for(var i=0, len=blogs.length; i<len ; i++) {  }}
		{{# var blog = blogs[i]; }}
		{{# var saleOrder = blog.saleOrder; }}
		{{# var saleOrderSvcs=null;}}
		{{# var userBlogImg = blog.userBlogImg; }}
		{{# if(saleOrder!=null){}}
			{{# saleOrderSvcs=saleOrder.saleOrderSvcs;}}
		{{#	}}}
				<li>
                <dl class="blog-item">
                    <dt>
						{{# if(userBlogImg!=null){ }}
								<a class="blogDetailUrl" href="javascript:void(0)" blogId="{{blog.id}}" title="{{blog.title}}"><img  src="{{userBlogImg.fileBrowseUrl}}" title="{{blog.title}}" /> </a>
						{{# } }}
                    </dt>
                    <dd>
                        <div class="blog-item-cont">
                        <h1><a class="blogDetailUrl" href="javascript:void(0)" blogId="{{blog.id}}" title="{{blog.title}}">{{blog.title}}</a></h1>
                        {{# if(saleOrder!=null){}}
                         <ul class="car-info">
                            {{# if(saleOrder.shopId!=null){ }}
                            <li class="info1"><i></i><!--<a href="">车型</a>-->在<a href="javascript:void(0)" class="shopDetailUrl" shopId="{{saleOrder.shopId}}">{{saleOrder.shopName}}</a>接受服务</li>
							{{# } }}
                            {{# if(saleOrderSvcs!=null){ }}
                            <li class="info2"><i></i><span>服务项目：</span>
								{{# for(var j=0, svlen=saleOrderSvcs.length; j<svlen ; j++) {  }}
									{{# var saleOrderSvc=saleOrderSvcs[j];}}
									<a href="">{{saleOrderSvc.svcName}}</a>
								{{# } }}
                        	</li>
                        	{{# } }}
						</ul>
                        {{# } }}
                        <div class="info-cont">
                             <a class="blogDetailUrl" href="javascript:void(0)" blogId="{{blog.id}}">
							{{# if(blog.content!=null&&blog.content.length>150){}}
                            	{{blog.content.substring(0,150)}}......
                            	{{#}else{}}
								{{blog.content}}
                            {{# } }}
                           </a>
                        </div>
						</div>
                        <div class="info-btnbox">
                            <span class="gray">{{blog.createTime}}</span>
                            <span class="user-level gold ml20">
                                <i></i><span>
                            {{# if(blog.user.realName!=null){}}
								{{blog.user.realName}}
							{{# }else{}}
								{{blog.user.phoneNo}}
							{{# }}}
                                </span>
                            </span>
                            <span class="fr">
                                <!--
                                <a class="info-preview mr20" href=""></a>
                                <a href="javascript:;" class="favour selected"><i></i><span>20</span></a>
								-->
                            </span>
                        </div>
                    </dd>
                </dl>
            </li>
		{{# } }}
</script>
</html>