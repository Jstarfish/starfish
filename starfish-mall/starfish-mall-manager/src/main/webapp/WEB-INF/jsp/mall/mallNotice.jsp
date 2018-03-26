<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	<title>商城设置</title>
</head>
<body id="rootPanel">
<div id="mainPanel" class="ui-layout-center" style="padding:4px;">
	<!--过滤区域 -->
	<div class="filter section">
		<div class="filter row">
			<div class="group left aligned">
				<button id="btnAddNotice" class="button">添加</button>
				<button id="btnDelNoticeByIds" class="button">批量删除</button>
			</div>
		</div>
	</div>
	<!--店铺人员-角色Grid -->
	<table id="mallNoticeList"></table>
	<div id="mallNoticePager"></div>
	<div id="mallNoticeDialog"></div>
</div>

<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
	// 商城公告页面表单代理
	var noticeFormProxy = FormProxy.newOne();
	// tabs、商城公告Grid
	var theTabsCtrl, mallNoticeGrid, mallNoticeDialog;
	// 商城公告当前操作
	var noticeGridHelper = JqGridHelper.newOne("");
	var curAction, tempMallNotice;
	var statusTextMap = new KeyMap();
	statusTextMap.add(0,"未发布");
	statusTextMap.add(1,"发布中");
	statusTextMap.add(2,"已停止");
	//
	var autoFlagTextMap = new KeyMap();
	autoFlagTextMap.add("true","自动");
	autoFlagTextMap.add("false","手动");
	//
	var statusBtnTextMap = new KeyMap();
	statusBtnTextMap.add(0,"立刻发布");
	statusBtnTextMap.add(1,"停止发布");
	statusBtnTextMap.add(2,"重新发布");
	
	// 加载商城公告
	function loadNotice() {
		var ajax = Ajax.post("/mall/notice/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				// 查询到信息时回显，查询不到信息时初始化商城公告
				var data = result.data;
				if (data != null) {
					if (data.status == 0) {// 0 : 未发布, 1: 发布中, 2：已停止
						$("#noticeUnreleased").prop("checked", "checked");
					} else if (data.status == 1) {
						$("#noticeReleasing").prop("checked", "checked");
					} else if (data.status == 2) {
						$("#noticeReleaseOver").prop("checked", "checked");
					}

					if (data.autoFlag == 0) {// 发布方式 ：1自动发布 / 0手动发布
						$("#noticeAutoFlagOff").prop("checked", "checked");
					} else if (data.autoFlag == 1) {
						$("#id=noticeAutoFlagOn").prop("checked", "checked");
					}

					$("#startDate").val(data.startDate);
					$("#endDate").val(data.endDate);
					$("#title").val(data.title);
					$("#content").val(data.content);
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}

	// 检验商城公告
	function checkNotice() {
		noticeFormProxy.addField({
			name : "status",
			required : true
		});
		noticeFormProxy.addField({
			name : "autoFlag",
			required : true
		});
		noticeFormProxy.addField({
			id : "startDate",
			type : "date",
			required : true,
			rules : [ "isDate" ]
		});
		noticeFormProxy.addField({
			id : "endDate",
			type : "date",
			required : true,
			rules : [ "isDate", "minDate[" + noticeFormProxy.getValue('startDate') + "]" ]
		});
		noticeFormProxy.addField({
			id : "title",
			required : true,
			rules : [ "rangeLength[1,20]" ]
		});
		noticeFormProxy.addField({
			id : "content",
			required : true,
			rules : [ "rangeLength[1,100]" ]
		});
		return noticeFormProxy.validateAll();
	}

	// 保存商城公告
	function saveNotice() {
		var ajax = Ajax.post("/mall/notice/save/do");
		ajax.data(noticeFormProxy.getValues());
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}

	// 隐藏商城设置页面所有错误提示
	function hideAllMessages() {
		noticeFormProxy.hideMessages();
	}
	
	// 加载商城公告
	function loadMallNotice(){
		mallNoticeGrid = $id("mallNoticeList").jqGrid({
			url : getAppUrl("/mall/notice/list/get"),
			contentType : 'application/json',
			mtype : "post",
			datatype : 'json',
			colNames : [ "id", "标题", "状态", "发布方式", "发布时间","操作" ],
			colModel : [
					{
						name : "id",
						index : "id",
						hidden : true
					},
					{
						name : "title",
						width : 200,
						align : 'left',
					},
					{
						name : "status",
						align : 'center',
						width : 50,
						formatter : function(cellValue, option, rowObject) {
							return statusTextMap.get(cellValue);
						}
					},
					{
						name : "autoFlag",
						align : 'center',
						width : 50,
						formatter : function(cellValue, option, rowObject) {
							return autoFlagTextMap.get(cellValue);
						}
					},
					{
						name : "id",
						width : 150,
						align : 'center',
						formatter : function(cellValue, option, rowObject) {
							return rowObject.pubTime || "";
						}
					},
					{
						name : 'id',
						index : 'id',
						width : 100,
						formatter : function(cellValue, option, rowObject) {
							return "<span> [<a href='javascript:void(0);' onclick='mallNoticeDialogView("
									+ JSON.stringify(rowObject)
									+ ")' >查看</a>]  [<a href='javascript:void(0);' onclick='mallNoticeDialogEdit("
									+ JSON.stringify(rowObject)
									+ ")' >修改</a>]  [<a href='javascript:void(0);' onclick='deleteMallNotice("
									+ cellValue
									+ ")' >删除</a>] ";
						},
						width : 150,
						align : "center"
					} ],
			pager : "#mallNoticePager",
			multiselect : true,// 定义是否可以多选
			multikey:'ctrlKey', 
			height : "auto",
			loadComplete : function(gridData) { // JqGridHelper缓存最新的grid数据
				noticeGridHelper.cacheData(gridData);
				var callback = getCallbackAfterGridLoaded();
				if (isFunction(callback)) {
					callback();
				}
			},
			ondblClickRow: function(rowId) {
				var userMap = noticeGridHelper.getRowData(rowId)
				mallNoticeDialogView(userMap);
			}
		});
	}
	//
	function getCallbackAfterGridLoaded(){}
	
	// 初始化商城公告模板
	function initMallNoticeTpl() {
		var noticeTplHtml = $id("mallNoticeTpl").html();
		var htmlStr = laytpl(noticeTplHtml).render({});
		$id("mallNoticeDialog").html(htmlStr);
		initDialogAdd();
		$('input[name="autoFlag"]').change(onAutoFlagChange);
		// 商城公告表达代理验证
		noticeFormProxy.addField({
			id : "title",
			required : true,
			rules : ["maxLength[100]"]
		});
		noticeFormProxy.addField({
			id : "content",
			rules : ["maxLength[1000]"]
		});
		noticeFormProxy.addField({
			id : "pubTime",
			type : "datetime",
			rules : [ "isDate", {
				rule : function(idOrName, type, rawValue, curData) {
					var autoVal = radioGet("autoFlag");
					if(autoVal == true){
						return !isNullOrEmpty(rawValue);
					}else{
						return true;
					}				
				},
				message : "此为必须提供项"
			}]
		});
		noticeFormProxy.addField({
			id : "endTime",
			type : "datetime",
			rules : [ "isDate", {
				rule : function(idOrName, type, rawValue, curData) {
					var autoVal = radioGet("autoFlag");
					if(autoVal == true){
						return !isNullOrEmpty(rawValue);
					}else{
						return true;
					}				
				},
				message : "此为必须提供项"
			}]
		});
	 }
	
	// 初始化日历插件
	function initDatePicker(){
		var pubTime = textGet("pubTime");
		pubTime = isNoB(pubTime) ? null : pubTime;
		var endTime = textGet("endTime");
		endTime = isNoB(endTime) ? null : endTime;
		$id("pubTime").datetimepicker({
			maxDate : endTime,
			changeMonth: true,
			changeYear: true,
			format: 'Y-m-d H:i',
			lang:'ch',
		    onSelect:function(dateText,inst){
		       $id("endTime").datepicker("option","minDate",dateText);
		       noticeFormProxy.validate("pubTime");
		    }
		});
		$id("endTime").datetimepicker({
			minDate :pubTime == null ? 0 : pubTime, 
			changeMonth: true,
			changeYear: true,
			format: 'Y-m-d H:i',
			lang:'ch',
			onSelect:function(dateText,inst){
		        $id("pubTime").datepicker("option","maxDate",dateText);
		        noticeFormProxy.validate("endTime");
		    }
		});
	}
	
	// 发布方式change事件
	function onAutoFlagChange(){
		hideMiscTip("pubTime");
		hideMiscTip("endTime");
		var autoVal = radioGet("autoFlag");
		if(autoVal == false){
			$id("lblPublishTime").removeClass("required");
			$id("beginTime").css("display", "none");
		}else{
			$id("lblPublishTime").addClass("required");
			$id("beginTime").css("display", "block");
		}
	}
	
	// 初始化dialogAdd
	function initDialogAdd() {
		$("div[aria-describedby='mallNoticeDialog']").find("span[class='ui-dialog-title']").html("添加商城公告");
		//
		mallNoticeDialog = $id("mallNoticeDialog").dialog({
			autoOpen : false,
			height : Math.min(400, $window.height()),
			width : Math.min(600, $window.width()),
			modal : true,
			buttons : {
				"保存" : defaultMethod,
				"取消" : function() {
					clearDialog();
					mallNoticeDialog.dialog("close");
				}
			},
			close : function() {
				clearDialog();
			}
		});
	}
	
	// 初始化dialogView
	function initDialogView() {
		$("div[aria-describedby='mallNoticeDialog']").find("span[class='ui-dialog-title']").html("查看商城公告");
		//
		mallNoticeDialog = $id("mallNoticeDialog").dialog({
			autoOpen : false,
			height : Math.min(400, $window.height()),
			width : Math.min(600, $window.width()),
			modal : true,
			buttons : {
				"继续添加" : mallNoticeDialogAdd,
				"修改 >" : defaultMethod,
				"关闭" : function() {
					clearDialog();
					mallNoticeDialog.dialog("close");
				}
			},
			close : function() {
				clearDialog();
			}
		});
	}
	
	// 初始化dialogEdit
	function initDialogEdit() {
		$("div[aria-describedby='mallNoticeDialog']").find("span[class='ui-dialog-title']").html("修改商城公告");
		//
		mallNoticeDialog = $id("mallNoticeDialog").dialog({
			autoOpen : false,
			height : Math.min(400, $window.height()),
			width : Math.min(600, $window.width()),
			modal : true,
			buttons : {
				"保存" : defaultMethod,
				"取消" : function() {
					clearDialog();
					mallNoticeDialog.dialog("close");
				}
			},
			close : function() {
				clearDialog();
			}
		});
	}

	//
	function defaultMethod() {
		if (curAction == "add") {
			saveMallNotice();
		}
		if (curAction == "edit") {
			updateMallNotice();
		}
		if (curAction == "view") {
			var id = tempMallNotice.id;
			tempMallNotice = noticeGridHelper.getRowData("id",id);
			mallNoticeDialogEdit(tempMallNotice);
		}
	}
	
	function mallNoticeDialogAdd() {
		clearDialog();
		curAction = "add";
		initDialogAdd();
		initDatePicker();
		$id("publishNoticeStatus").css("display", "none");
		$id("beginTime").css("display", "block");
		mallNoticeDialog.dialog("open");
	}

	//
	function mallNoticeDialogView(obj) {
		clearDialog();
		curAction = "view";
		tempMallNotice = obj;
		initDialogView();
		textSet("mallNoticeId", obj.id);
		radioSet("autoFlag", obj.autoFlag);
		textSet("pubTime", obj.pubTime);
		textSet("endTime", obj.endTime);
		textSet("title", obj.title);
		var status = obj.status;
		var statusText = statusTextMap.get(status);
		$id("status").html(statusText);
		$id("publishNotice").css("display", "none");
		textSet("content",obj.content);
		$("input", "#mallNoticeDialog").attr("disabled", true);
		$("select", "#mallNoticeDialog").attr("disabled", true);
		$("textarea", "#mallNoticeDialog").attr("disabled", true);
		mallNoticeDialog.dialog("open");
	}

	//
	function mallNoticeDialogEdit(obj) {
		clearDialog();
		tempMallNotice = obj;
		tempId = obj.id;
		curAction = "edit";
		initDialogEdit();
		textSet("mallNoticeId", tempId);
		var status = obj.status;
		var statusText = statusTextMap.get(status);
		$id("status").html(statusText);
		var publishNotice =statusBtnTextMap.get(status);
		$id("publishNotice").html(publishNotice);
		$id("publishNotice").unbind("click");
		if(status == 0 || status == 2){
			$id("publishNotice").click(function(){
				publishNotices(tempId);
			});
		} else {
			$id("publishNotice").click(function(){
				stopNotices(tempId);
			});
		}
		radioSet("autoFlag",obj.autoFlag);
		if(obj.autoFlag){
			$id("lblPublishTime").removeClass("required");
			$id("beginTime").css("display", "block");
		 }else{
			$id("lblPublishTime").addClass("required");
			$id("beginTime").css("display", "none");
		 }
		textSet("pubTime",obj.pubTime);
		textSet("endTime",obj.endTime);
		textSet("title",obj.title);
		textSet("content",obj.content);
		initDatePicker();
		mallNoticeDialog.dialog("open");
	}

	//
	function clearDialog() {
		curAction = null;
		tempMallNotice = null;
		tempId = null;
		noticeFormProxy.hideMessages();
		textSet("mallNoticeId","");
		radioSet("autoFlag",true);
		textSet("pubTime", null);
		textSet("endTime", null);
		textSet("title","");
		textSet("content","");
		$id("pubTime").datepicker("destroy");
		$id("endTime").datepicker("destroy");
		$id("publishNotice").css("display", "");
		$id("publishNoticeStatus").css("display", "");
		$("input", "#mallNoticeDialog").attr("disabled", false);
		$("select", "#mallNoticeDialog").attr("disabled", false);
		$("textarea", "#mallNoticeDialog").attr("disabled", false);
	}
	
	// 保存商城公告
	function saveMallNotice(){
		var vldResult = noticeFormProxy.validateAll();
		if(vldResult){
			var ajax = Ajax.post("/mall/notice/save/do");
			ajax.data({
				title : textGet("title"),
				content : textGet("content"),
				autoFlag : radioGet("autoFlag")==true,
				pubTime : textGet("pubTime"),
				endTime : textGet("endTime"),
				status : "0",
			});
			ajax.done(function(result, jqXhr) {
				Layer.msgSuccess(result.message);
				mallNoticeGrid.jqGrid("setGridParam").trigger("reloadGrid");
				tempMallNotice = result.data;
				mallNoticeDialogView(tempMallNotice);
			});
			ajax.go(); 
		}
	}
	
	//
	function delMallNoticeByIds(){
		var ids = mallNoticeGrid.jqGrid("getGridParam", "selarrrow");
		var postData = [];
		for (var i = 0; i < ids.length; i++) {
			postData.add(ParseInt(ids[i]));
		}
		if (ids == "") {
			Toast.show("请选择要删除的数据！");
		} else {
			var theLayer = Layer.confirm('确定要删除选择的数据吗？', function() {
				//
				var hintBox = Layer.progress("正在删除...");
				var ajax = Ajax.post("/mall/notice/delete/batch/do");
				ajax.data(postData);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						//
						mallNoticeGrid.jqGrid("setGridParam").trigger("reloadGrid");
					} else {
						Layer.msgWarning(result.message);
					}
				});
				ajax.always(function() {
					hintBox.hide();
				});
				ajax.go();
			});
		}
	}
	
	//
	function deleteMallNotice(id){
		var theLayer = Layer.confirm('确定要删除？', function() {
			//
			var hintBox = Layer.progress("正在删除...");
			var ajax = Ajax.post("/mall/notice/delete/do");
			ajax.params({
				id : id
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					//
					mallNoticeGrid.jqGrid("setGridParam").trigger("reloadGrid");
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.always(function() {
				hintBox.hide();
			});
			ajax.go();
		});
	}
	
	//
	function updateMallNotice(){
		var vldResult = noticeFormProxy.validateAll();console.log(vldResult);
		if(vldResult){
			var ajax = Ajax.post("/mall/notice/update/do");
			var dataMap = {
				id : intGet("mallNoticeId"),
				title : textGet("title"),
				content : textGet("content"),
				autoFlag : radioGet("autoFlag") == true,
				pubTime : datetimeGet("pubTime"),
				endTime : datetimeGet("endTime")
			};
			ajax.data(dataMap);
			ajax.done(function(result, jqXhr) {
				Layer.msgSuccess(result.message);
				mallNoticeGrid.jqGrid("setGridParam").trigger("reloadGrid");
				tempMallNotice = result.data;
				mallNoticeDialogView(tempMallNotice);
			});
			ajax.go(); 
		}
	}
	
	//
	function publishNotices(id){
		var theLayer = Layer.confirm('确定要发布？', function() {
			//
			var hintBox = Layer.progress("正在发布...");
			var ajax = Ajax.post("/mall/notice/publish/do");
			ajax.data({
				"id" : id
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					//
					mallNoticeGrid.jqGrid("setGridParam").trigger("reloadGrid");
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.always(function() {
				hintBox.hide();
			});
			ajax.go();
			var status = "1";
			var statusText = statusTextMap.get(status);
			$id("status").html(statusText);
			var publishNotice =statusBtnTextMap.get("1");
			$id("publishNotice").html(publishNotice);
			$id("publishNotice").click(function(){
				stopNotices(tempId);
			});
		});
	}
	
	//
	function stopNotices(id){
		var theLayer = Layer.confirm('确定要停止？', function() {
			//
			var hintBox = Layer.progress("正在停止...");
			var ajax = Ajax.post("/mall/notice/stop/do");
			ajax.data({
				"id" : id
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					//
					mallNoticeGrid.jqGrid("setGridParam").trigger("reloadGrid");
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.always(function() {
				hintBox.hide();
			});
			ajax.go();
			var status = "2";
			var statusText = statusTextMap.get(status);
			$id("status").html(statusText);
			var publishNotice =statusBtnTextMap.get("2");
			$id("publishNotice").html(publishNotice);
			$id("publishNotice").click(function(){
				publishNotices(tempId);
			});
		});
	}
	
	function adjustCtrlsSize(){
		adjustMallNoticeSize();
	}
	
	// 调整商城公告页面布局
	function adjustMallNoticeSize(){
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		var gridCtrlId = "mallNoticeList";
		var jqGridBox = $("#gbox_" + gridCtrlId);
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
		var pagerHeight = $id("mallNoticePager").height();
		//var btnNoticeHeight = $id("btnNotice").height();
		mallNoticeGrid.setGridWidth(mainWidth - 1);
		mallNoticeGrid.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 60);
	}

	// 初始化页面
	$(function() {
		// 页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 100,
			allowTopResize : false
		});
		initMallNoticeTpl();
		loadMallNotice();

		// 添加商城公告
		$id("btnAddNotice").click(mallNoticeDialogAdd);
		
		// 调整页面布局
		winSizeMonitor.start(adjustCtrlsSize);
	});
</script>
</body>

<script id="mallNoticeTpl" type="text/html" title="商城公告模板">
	<div class="form">
		<input type="hidden" id="mallNoticeId" />
		<div class="field row">
			<label class="field label one wide required">标题</label>
			<input class="field value two wide" type="text" id="title" />
		</div>
		<div class="field row" style="height: 82px;">
			<label class="field label one wide">内容</label>
			<textarea class="field value three wide" style="height:80px; resize:none;" id="content" ></textarea>
		</div>
		<div class="field row">
			<label class="field label one wide required">发布方式</label>
			<div class="field group">
				<input type="radio" id="noticeAutoFlagOn" name="autoFlag" value="1" />
				<label for="noticeAutoFlagOn">自动</label>
				<input type="radio" id="noticeAutoFlagOff" name="autoFlag" value="0" checked="checked"/>
				<label for="noticeAutoFlagOff">手动</label>
			</div>
		</div>
		<div class="field row" id="beginTime">
			<label id="lblPublishTime" class="field label one wide required">发布时间</label>
			<input class="one half wide field value" type="text" id="pubTime" readonly="true"/>
		</div>
		<div class="field row">
			<label id="lblEndTime" class="field label one wide required">结束时间</label>
			<input class="one half wide field value" type="text" id="endTime" readonly="true"/>
		</div>
		<div class="field row" style="float:left;" id="publishNoticeStatus">
			<label class="field label one wide">状态</label>
			<div style="height:120px;vertical-align: middle;float:left;">
			<label id='status' class='normal label'></label><span class="normal spacer"></span>
			</div>
			<button class="normal button" id="publishNotice" style="margin-top: 4px;">
			</button>
		</div>
	</div>
</script>
</html>