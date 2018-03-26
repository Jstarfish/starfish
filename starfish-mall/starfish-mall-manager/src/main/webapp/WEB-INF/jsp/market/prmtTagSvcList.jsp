<%@page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>促销标签服务列表</title>
<style type="text/css">

</style>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="left aligned group">
					<label class="label">标签名称</label>
					<select id="q_prmtTag" class="input"></select> 
					<span class="spacer"></span>
					<label class="label">服务名称</label>
					<input id="q_name" class="input wide" /> 
					<span class="spacer"></span>
					<button id="queryBtn" class="button">查询</button>
				</div>
				<div class="right aligned group" style="padding-right: 5px;">
					<button id="saveSeqNoBtn" class="one half wide button">保存当前序号</button>
				</div>
			</div>
		</div>
		
		<table id="prmtTagSvcx_list"></table>
		<div id="prmtTagSvcx_pager"></div>
	</div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
	var jqGridCtrl = null;
	//------------------------------------------初始化配置---------------------------------
	$(function() {
		//页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			allowTopResize : false
		});
		//加载全部促销标签列表
		loadPrmtTagsData();
		//
		loadPrmtTagSvcsData();
		//
		$id("queryBtn").click(queryPrmtTagSvcxs);
		//
		$id("saveSeqNoBtn").click(goSaveTagSvcOrders);
		// 页面自适应
		winSizeMonitor.start(adjustCtrlsSize);
	});
	
	//调整控件大小
	function adjustCtrlsSize() {
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		var gridCtrlId = "prmtTagSvcx_list";
		var jqGridBox = $("#gbox_" + gridCtrlId);
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
		var pagerHeight = $id("prmtTagSvcx_pager").height();
		jqGridCtrl.setGridWidth(mainWidth - 1 - 4);
		jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 54);
	}
	
	//-----------------------------业务处理------------------------------------------------
	
	//
	function formPrmtTags(tags, targetId){
		var tagsData = {
				"unSelectedItem" : {
					"value" : "",
					"text" : "- 全部 -"
				},
				"defaultValue" : ""
			};
		//
		var items = [];
		for(var i=0; i<tags.length; i++){
			var tag = tags[i];
			var id = tag.id || null;
			var name = tag.name || null;
			var tagMap = {
				"value" : id,
				"text" : name
			};
			items.add(tagMap);
		}
		//
		tagsData.items = items;
		//
		loadSelectData(targetId, tagsData);
	}
	
	//
	function loadPrmtTagsData() {
		var ajax = Ajax.post("/market/prmtTag/list/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var tags = result.data;
				if(tags) formPrmtTags(tags, "q_prmtTag");
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	
	//
	function queryPrmtTagSvcxs(){
		var filter = {};
		var q_prmtTag = textGet("q_prmtTag");
		if(q_prmtTag){
			filter.tagId = q_prmtTag;
		}
		//
		var q_name = textGet("q_name");
		if(q_name){
			filter.name = q_name;
		}
		//
		jqGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
	}
	
	function loadPrmtTagSvcsData(){
		var filter = {};
		var q_prmtTag = textGet("q_prmtTag");
		if(q_prmtTag){
			filter.tagId = q_prmtTag;
		}
		//
		var q_name = textGet("q_name");
		if(q_name){
			filter.name = q_name;
		}
		//
		jqGridCtrl= $("#prmtTagSvcx_list").jqGrid({
		      url : getAppUrl("/market/prmtTag/svc/list/get"),  
		      contentType : 'application/json',  
		      mtype : "post",  
		      datatype : 'json',
		      postData:{filterStr : JSON.encode(filter,true)},
		      height : "100%",
			  width : "100%",
			  colNames : ["服务名称", "销售价", "标签名称", "描述", "图标", "序号", " 操作" ], 
			  colModel : [
							{name:"svcx.name",width:100,align : 'left',formatter : function (cellValue, option, rowObject) {
		                		return "<div class='auto height'>"+cellValue+"</div>";
							}},
							{name:"svcx.salePrice",width:40,align : 'right',formatter : function (cellValue) {
			                	 return cellValue + "元";
							}},
							{
								name : "prmtTag.name",
								index : "prmtTag.name",
								width : 100,
								align : 'left'
							},
							{
								name : "prmtTag.desc",
								index : "prmtTag.desc",
								width : 200,
								align : 'left',formatter : function (cellValue) {
				                	 return "<div class='auto height'>"+cellValue+"</div>";
								}
							},
							{
								name : "icon",
								index : "icon",
								width : 70,
								align : 'right'
							},
							{
								name : "seqNo",
								index : "seqNo",
								width : 40,
								align : 'right'
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
										var tagId = rowObject.tagId;
										return "<span> [<a class='item up"+tagId+"' href='javascript:;' onclick='upTagSvcOrder(this, "+cellValue+", "+tagId+")' >上移</a>] "
										+ "[<a class='item down"+tagId+"' href='javascript:;' onclick='downTagSvcOrder(this, "+cellValue+", "+tagId+")' >下移</a>] "
										+ "</span>";
								},
								width : 100,
								align : "center"
							} ],  
			//rowList:[10,20,30],
			multiselect:false,
			multikey:'ctrlKey',
			pager : "#prmtTagSvcx_pager"
		});
	}
	
	//
	function saveTagSvcOrders(tagSvcOrders){
		var hintBox = Layer.progress("正在保存数据...");
		//
		var postData = { tagSvcSeqNos:tagSvcOrders };
	  	var ajax = Ajax.post("/market/prmtTag/svc/seqNo/update/do");
	  	    ajax.data(postData);
		    ajax.done(function(result, jqXhr){
				if (result.type == "info") {
					Toast.show(result.message);
					queryPrmtTagSvcxs();
				} else {
					Layer.msgWarning(result.message);
				}
		 });
		//
	    ajax.always(function() {
			hintBox.hide();
		});
		//
		ajax.go();
	}
	
	//
	function goSaveTagSvcOrders(){
		var tagSvcOrders = getTagSvcOrders("prmtTagSvcx_list");  //[{tagSvcId:seqNo},...]
		//
		saveTagSvcOrders(tagSvcOrders);
	}
	
	//
	function getTagSvcOrders(tableId){
		var retVMap = [];   //[{tagSvcId:seqNo},...]
		var trs = $id(tableId).find("tr.jqgrow.ui-row-ltr");
		if(trs && trs.length > 0){
			for(var i=0; i<trs.length; i++){
				var item = {};
				var tr = trs[i];
				var tagSvcId = $(tr).attr("id") || null;
				var seqNoTd = $(tr).children("td[aria-describedby='prmtTagSvcx_list_seqNo']");
				var seqNo = $(seqNoTd).html() || null;;
				item.id = tagSvcId;
				item.seqNo = ParseInt(seqNo);
				//item[tagSvcId] = ParseInt(seqNo);
				retVMap.add(item);
			}
			//
			console.log("retVMap=>"+JSON.encode(retVMap));
		}
		return retVMap;
	}
	
	function upTagSvcOrder(THIS, tagSvcId, tagId) {
		//高亮当前同一个标签下的行
		$("tr td:last-child a").css("color","#000");
		//
		$("tr td:last-child a.up"+tagId).css("color","#f60");
		$("tr td:last-child a.down"+tagId).css("color","#f60");
		//
		var $tr = $(THIS).parents("tr[id='"+tagSvcId+"']");
		//
		var $prevTr = $tr.prev();
		var $actionTd = $prevTr.children(":last-child");
		var upLink = $actionTd.find("a.up"+tagId);
		if(upLink.length > 0){
			if ($tr.index() != 1) {
				//更改序号
				var $prevSeqNoTd = $prevTr.children("td[aria-describedby='prmtTagSvcx_list_seqNo']");
				var prevSeqNo = $prevSeqNoTd.html();
				//
				var $seqNoTd = $tr.children("td[aria-describedby='prmtTagSvcx_list_seqNo']");
				var currentSeqNo = $seqNoTd.html();
				//
				$prevSeqNoTd.html(currentSeqNo);
				$seqNoTd.html(prevSeqNo);
				//
				$tr.fadeOut().fadeIn();
				$tr.css("background", "#FFDAB9");
				$tr.siblings().css("background", "");
				$prevTr.before($tr);
			}
		}
	}
	
	function downTagSvcOrder(THIS, tagSvcId, tagId){
		//高亮当前同一个标签下的行
		$("tr td:last-child a").css("color","#000");
		//
		$("tr td:last-child a.up"+tagId).css("color","#f60");
		$("tr td:last-child a.down"+tagId).css("color","#f60");
		//
		var $tr = $(THIS).parents("tr[id='"+tagSvcId+"']");
		//
		var $nextTr = $tr.next();
		var $actionTd = $nextTr.children(":last-child");
		var upLink = $actionTd.find("a.down"+tagId);
		if(upLink.length > 0){
			var len = $("a.down");
			if ($tr.index() != len) {
				//更改序号
				var $nextSeqNoTd = $nextTr.children("td[aria-describedby='prmtTagSvcx_list_seqNo']");
				var nextSeqNo = $nextSeqNoTd.html();
				//
				var $seqNoTd = $tr.children("td[aria-describedby='prmtTagSvcx_list_seqNo']");
				var currentSeqNo = $seqNoTd.html();
				//
				$nextSeqNoTd.html(currentSeqNo);
				$seqNoTd.html(nextSeqNo);
				//
				$tr.fadeOut().fadeIn();
				$tr.css("background", "#FFDAB9");
				$tr.siblings().css("background", "");
				$tr.next().after($tr);
			}
		}
		
	}
	</script>
</body>


</html>
