<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/base.jsf"%><jsp:include
	page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
<div class="content">
	<div class="page-width">
		<div class="crumbs">
			<a href="">首页</a>&gt;<a href="">用户中心</a>&gt;<a href="">账户中心</a>&gt;<span>账户安全</span>
		</div>
		<div class="section">
			<jsp:include page="/WEB-INF/jsp/include/ucenterMenu.jsp" />
			<div class="section-right1">
				<div class="mod-main1">
					<div class="order-tit">
						<h1>意见反馈</h1>
					</div>
					<div class="suggestion">
						<div class="suggestion-top">
							<div class="fr">
								关键字：<input class="inputx" id="keywords" type="text" placeholder="反馈内容关键字" /> <span
									class="ml10">提交时间：<input id="sendTime" class="inputx inputx-w106"
									type="text" />~<input id="sendTime2" class="inputx inputx-w106" type="text" /></span>
								<input id="quryFeekBack" class="btn-red-border ml10" type="button" value="查询" />
							</div>
							<input type="button" class="btn btn-w90"
								onclick="popSuggestion()" value="我要反馈" />
						</div>
						<div class="suggestion-cont">
							<table id="packList" class="tb-praise">
								
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
	</div>
</div>
<div id="pop-suggestion" class="pop-suggestion" style="display: none;">
	<h1>反馈内容</h1>
	<textarea id="textValue" class="textarea textarea-auto"></textarea>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
<script type="text/javascript">
	$(function(){
		initGqPaginator();
		
		initDatePicker();
	});
	
	//初始化日历插件
	function initDatePicker() {
		var startTime = textGet("sendTime");
		startTime = isNoB(startTime) ? null : startTime;
		var endTime = textGet("sendTime2");
		endTime = isNoB(endTime) ? null : endTime;
		$id("sendTime").datepicker({
			maxDate : endTime,
			lang : 'ch',
			onSelect : function(dateText, inst) {
				$id("sendTime2").datepicker("option", "minDate", dateText);
			}
		});
		//
		$id("sendTime2").datepicker({
			endTime : endTime == null ? 0 : endTime,
			lang : 'ch',
			onSelect : function(dateText, inst) {
				$id("sendTime").datepicker("option", "maxDate", dateText);
			}
		});
	}
	var paginatorCtrl = null;
	//回复
	function popSuggestion() {
		var dom = "#pop-suggestion";
		var theLayer = Layer.dialog({
			dom : dom, //或者 html string
			title : "意见反馈",
			area : [ '500px', '300px' ],
			closeBtn : true,
			btn : [ "提交", "取消" ],
			yes : function() {
				var textValue = textGet("textValue");
				if (isNullOrEmpty(textValue)) {
					Layer.msgWarning("请输入反馈内容！");
					return;
				}
				if (textValue.length < 10) {
					Layer.msgWarning("内容最少10个字！");
					return;
				}
				saveFeedback(textValue);
				theLayer.hide();
			}
		});
	}

	function saveFeedback(backValue) {
		var ajax = Ajax.post("/social/user/feedBack/add/do");
		var postData = {
			content : backValue
		};
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var data = result.data;
				if (data != null) {
					filterAndQuery();
				} else {
					Layer.msgWarning("删除失败");
				}
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}

	function doTheQuery() {
		var postData = newQryFilterInfo();
		// ajax post 请求...
		var ajax = Ajax.post("/social/user/feedBack/list/get");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				//
				var paginatedList = result.data;
				//更新分页信息
				var pagination = paginatedList.pagination;
				//
				var packList = paginatedList.rows;
				//
				renderPackData(packList);
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
	function getHandleFlagVal(handleFlag){
		var handleFlagVal = "";
		if(handleFlag == 0){
			handleFlagVal = "未处理";
		}else if(handleFlag == -1){
			handleFlagVal = "不予处理";
		}else if(handleFlag == 1){
			handleFlagVal = "已处理";
		}
		return handleFlagVal;
	}
	$id("quryFeekBack").click(function(){
		var keyword = $(".inputx").val();
		var startTime = $id("sendTime").val();
		var endTime = $id("sendTime2").val();
		if(keyword =="" && startTime =="" && endTime ==""){
			Layer.msgWarning("请输入查询条件");
			return ;
		}
		if(startTime != null && startTime != ""){
			merge(qry_filterItems, {startTime: startTime});
		}else{
			merge(qry_filterItems, {startTime: null});
		}
		if(keyword != null && keyword != ""){
			qry_keywords = keyword;
		}
		if(endTime != null && endTime != ""){
			merge(qry_filterItems, {endTime: endTime});
		}else{
			merge(qry_filterItems, {endTime: null});
		}
		filterAndQuery();
	});
	//------------------------------------------------渲染页面-------------------------------------------------
	//渲染货品列表数据
	function renderPackData(dataList) {
		//获取模板内容
		var tplHtml = $id("feedBackListRowTpl").html();
		//生成/编译模板
		var htmlTpl = laytpl(tplHtml);
		//根据模板和数据生成最终内容
		var htmlText = htmlTpl.render(dataList);
		//使用生成的内容
		$id("packList").html(htmlText);
	}
	//刷新分页插件
	function refPaginator() {
		//同步GqPaginator数据模型
		qry_pagination.currentPage = qry_pagination.pageNumber;

		paginatorCtrl.jqPaginator('option', qry_pagination);
	}
	//-------------------------------------------------初始化-------------------------------------------------
	//初始化分页控件
	function initGqPaginator() {
		paginatorCtrl = $id("jqPaginator").jqPaginator({
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
	//-----------------------------------------------------初始分页信息-------------------------------------------
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
		qry_keywords= $id("keywords").val();
		// 重置页码
		resetPagination(1);
		//
		doTheQuery();
	}

	// 查询 按钮等
	function filterAndQuery() {
		// 重置页码
		resetPagination(1);
		//
		doTheQuery();
	}

	// 排序 按钮等
	function sortAndQuery() {
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
</script>
</body>
<script type="text/html" id="feedBackListRowTpl">
	<thead>
		<tr>
			<td>反馈内容</td>
			<td width="100">主题</td>
			<td width="140">提交时间</td>
			<td width="70">操作</td>
		</tr>
	</thead>
	{{# var backList = d; }}
	{{# if(!isNoB(backList) && backList.length >0){ }}
	{{# for(var i=0;i<backList.length; i++){ }}
	{{# var back = backList[i]; }}
		<tbody>
			<tr>
				<td>{{back.content}}</td>
				<th>{{back.userType}}</th>
				<th>{{back.sendTime}}</th>
				<th><!--<a href="" class="anormal">删除</a>--></th>
			</tr>
			<tr>
				<td class="padding10" colspan="4">
					<div class="comment-operate">
						<span class="state">{{getHandleFlagVal(back.handleFlag)}}</span>
						<div class="reply-textarea first">
						<div class="arrow">
							<i class="a1"></i><i class="a2"></i>
						</div>
						</div>
					</div>
					{{# if(back.handleFlag == 1) { }}
						<ul class="comment-replylist">
						<li>
							<div class="reply-info">
								<div class="fl">
									<span class="customer-svc">平台客服小花</span>:
								</div>
								<div class="orange1">
									{{back.handleMemo || ""}}</span><span class="time">{{back.recvTime || ""}}
								</div>
							</div>
						</li>
						</ul>
					{{# } }}
					
				</td>
			</tr>
		</tbody>
	{{# } }}
	{{# } }}
</script>
</html>
