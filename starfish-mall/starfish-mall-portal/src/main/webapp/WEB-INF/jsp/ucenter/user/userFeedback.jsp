<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/base.jsf" %><jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
<div class="content">
    <div class="page-width">
        <div class="crumbs"><a href="">首页</a>&gt;<a href="">用户中心</a>&gt;<a href="">资产中心</a>&gt;<span>服务套餐票</span></div>
        <div class="section">
          		<jsp:include page="/WEB-INF/jsp/include/ucenterMenu.jsp"></jsp:include>
                 <div class="section-right1">
                <div class="mod-main1">
                    <div class="order-tit">
                        <h1>意见反馈</h1>
                    </div>
                    <div class="suggestion">
                    	<div class="suggestion-top">
                    		<div class="fr">
                    			关键字：<input class="inputx" type="text" value="反馈内容关键字" />
                    			<span class="ml10">提交时间：<input class="inputx inputx-w106" type="text" />~<input class="inputx inputx-w106" type="text" /></span>
                    			<input class="btn-red-border ml10" type="button" value="查询" />
                    		</div>
                    		<input type="button" class="btn btn-w90" value="我要反馈" id="btnFeedBack" />
                    	</div>
                    	<div class="suggestion-cont">
							<table class="tb-praise" id="dataList">
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
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
<!--<script type="text/javascript" src="<%=appBaseUrl%>/js/dictSelectLists/get"></script>-->
<script type="text/javascript">
//var handleFlag = getDictSelectList("handleFlag","","-请选择-");
//loadSelectData("targetFlag", activityTarget);
//	untreated("初始（未处理）", 0), unhandled("不予处理", -1), treated("已处理", 1);
function openDlg(dlgArgs, dlgViewId, dlgUrl, callbackFunc, options) {
	// 对话框信息
	var body = $("body");
	$(body).css({
		overflow : "hidden"
	}); // 禁用滚动条
	var theDlg = null;
	options = options || {};
	if (dlgUrl) {
		// 对话框参数名
		var argName = dlgViewId || "argx";
		// 对话框参数值
		var argValue = dlgArgs;
		// 对话框参数 预存
		setDlgPageArg(argName, argValue);
		var pageUrl = dlgUrl;
		var extParams = {};
		pageUrl = makeDlgPageUrl(pageUrl, argName, extParams);
		options["src"] = pageUrl;
	}
	options["closeBtn"] = true;
	options["maxmin"] = false;
	if (callbackFunc && typeof callbackFunc == "function") {
		options["yes"] = function(index) {
			callbackFunc(theDlg, index);
		};
	}
	options["end"] = function() {
		$(body).css({
			overflow : "scroll"
		}); // 启用滚动条
	};
	// 打开对话框-----------------------------------------
	theDlg = Layer.dialog(options);
}
var handleFlagMap=KeyMap.newOne();
handleFlagMap.put(0,"未处理");
handleFlagMap.put(-1,"不予处理");
handleFlagMap.put(1,"已处理");

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
	var ajax = Ajax.post("/social/user/feedBack/list/get");
	ajax.sync();
	ajax.data(postData);
	ajax.done(function(result, jqXhr) {
		var rows = result.data.rows;
		/* if (isUndef(rows)||isNull(rows)||rows.length==0) {
			$id("blogList").html("<td colspan='5' style='text-align:center' >没有博文</td>");
			return;
		} */
		// 获取模板内容
		var tplHtml = $id("dataListTpl").html();
		// 生成/编译模板
		var htmlTpl = laytpl(tplHtml);
		// 根据模板和数据生成最终内容
		var htmlText = htmlTpl.render(rows);
		$id("dataList").html(htmlText);
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
//state=unused ,  used ,cancelled
$(function(){
	//merge(qry_filterItems, {state:"unused"});
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
	$id("btnFeedBack").click(function(){
		
	});
});
//-->
</script>
</body>
<script type="text/html" id="dataListTpl">
<thead>
<tr>
	<td>反馈内容</td>
	<td width="100">主题</td>
	<td width="140">提交时间</td>
	<!--<td width="70">操作</td>-->
</tr>
</thead>
{{# var dataList=d;}}
{{#  for(var i=0,len=dataList.length;i<len;i++){ }}
{{#  var data=dataList[i]; }}
<tbody >
<tr>
	<td>{{data.content}}</td>
	<th>{{data.subject}}</th>
	<th >{{data.sendTime}}</th>
	<!--<th>
		<a href="javascript:void(0)" class="anormal del" data-id="{{data.id}}">删除</a>
	</th>-->
</tr>
<tr>
	<td class="padding10" colspan="4">
		<div class="comment-operate">
			<span class="state">{{handleFlagMap.get(data.handleFlag)}}</span><span>回复（1）</span>
			<div class="reply-textarea first">
				<div class="arrow">
					<i class="a1"></i><i class="a2"></i>
				</div>
			</div>
		</div>
		{{# if(data.handleMemo!=null){ }}
			<ul class="comment-replylist" >
			<li>
				<div class="reply-info">
					<div class="fl"><span class="customer-svc">亿投车吧客服</span>:</div>
					<div class="orange1">{{data.handleMemo}}<span class="time">{{data.ts}}</span></div>
				</div>
			</li>
			</ul>
		{{# } }}
	</td>
</tr>
</tbody>
{{#  } }}
</script>
</html>