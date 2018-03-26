<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>商品咨询</title>
<style type="text/css">
</style>
</head>
<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding: 4px; vertical-align: bottom;">
		<div class="filter section">
			<div class="filter row">
				<div class="group right aligned">
					<label class="label one wide">咨询类型</label>
					<select class="field value" id="inquiryType"></select><span class="spacer"></span>
					<button id="btnQuery" class="normal button">查询</button>
				</div>
			</div>
		</div>
	</div>
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<table id="inquiryList"></table>
		<div id="inquiryPager"></div>
	</div>
	<div id="inquiryDialog"></div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		var inquiryGrid;
		
		var inquiryType = getDictSelectList("InquiryType","","-全部-");
		
		//加载数据列表
		function loadData() {
			inquiryGrid = $id("inquiryList").jqGrid({
				url : getAppUrl("/interact/inquiry/list/get"),
				contentType : 'application/json',
				mtype : "post",
				datatype : 'json',
				colNames : [ "id", "商品名称", "会员昵称", "类型", "是否回复", "咨询时间", "操作" ],
				colModel : [
						{
							name : "id",
							index : "id",
							hidden : true
						},
						{
							name : "goods.name",
							width : 100,
							align : 'left',
						},
						{
							name : "member.user.nickName",
							width : 100,
							align : 'left',
						},
						{
							name : "typeFlag",
							width : 100,
							align : 'left',
							formatter : function(cellValue,
									option, rowObject) {
								return $(
										"#inquiryType option[value="
												+ cellValue
												+ "]").text();
							}
						},
						{
							name : "replyFlag",
							width : 100,
							align : 'left',
							formatter : function(cellValue, option, rowObject) {
								return cellValue ? "是" : "否";
							}
						},
						{
							name : "ts",
							width : 150,
							align : 'left'
						},
						{
							name : 'id',
							index : 'id',
							formatter : function(cellValue,
									option, rowObject) {
								return "<span> [<a href='javascript:void(0);' onclick='inquiryDialogView("
										+ JSON
												.stringify(rowObject)
										+ ")' >查看</a>]   [<a href='javascript:void(0);' onclick='inquiryDialogEdit("
										+ JSON
												.stringify(rowObject)
										+ ")' >修改</a>]  [<a href='javascript:void(0);' onclick='deleteAttr("
										+ cellValue
										+ ")' >删除</a>] ";
							},
							width : 150,
							align : "center"
						} ],
				multiselect : true,
				pager : "#inquiryPager",
				height : "auto"
			});
		}
		
		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "inquiryList";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv",jqGridBox).height();
			var pagerHeight = $id("inquiryPager").height();
			attrRefGrid.setGridWidth(mainWidth - 1);
			attrRefGrid.setGridHeight(mainHeight - headerHeight - pagerHeight - 3);
		}
		
		//---------------------------------------------------初始化--------------------------------------------------------
		$(function() {
			//页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 56,
				allowTopResize : false
			});
			//隐藏布局north分割线
			$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
			loadSelectData("inquiryType", inquiryType);
			loadData();
			//
			inquiryGrid.bindKeys();
			
			winSizeMonitor.start(adjustCtrlsSize);
		});
	</script>
</body>
<!-- layTpl begin -->
<!-- 属性参照模板 -->
<script id="attrTpl" type="text/html">
</script>
<!-- layTpl end -->
</html>
