<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>e卡活动列表</title>
<style type="text/css">
table.gridtable {
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-collapse: collapse;
	width: 600px;
	margin-left: 100px;
}

table.gridtable th {
	border: 1px solid #AAA;
	padding: 3px;
	background-color: #dedede;
	height: 24px;
	line-height: 22px;
}

table.gridtable td {
	border-top:#AAA 1px dashed;
	padding: 3px;
	background-color: #ffffff;
	height: 24px;
	line-height: 22px;
}
</style>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnAdd" class="button">添加</button>
				</div>
				<div class="group right aligned">
					<label class="label">活动名称</label> <input id="queryName"
						class="input" /> <span class="spacer"></span>
					<button id="btnToQry" class="button">查询</button>
				</div>
			</div>
		</div>

		<table id="ecardAct_List"></table>
		<div id="ecardAct_pager"></div>
	</div>
	<!-- 添加广告dialog -->
	<div id="addActDialog" style="display: none;">
		<div class="form">
			<div class="field row">
				<label class="field label one wide required">活动名称</label>
				<input type="text" id="name" name="name" class="field value three half wide">
				<input type="hidden" id="actId"/>
			</div>
			<div class="field row">
				<label class="field label one wide">显示名称</label>
				<input type="text" id="title" name="title" class="field value three half wide">
			</div>
			<div class="field row">
				<label class="field label one half required">有效时间</label> 
				<input type="text" id="startTime" class="field value" />&nbsp;&nbsp;-&nbsp;&nbsp;
				<input type="text" id="endTime" class="field value" />
			</div>
			<div class="field row">
				<label class="field label one wide required">活动规则</label>
				<button id="btnAddRule" class="button">添加</button>
			</div>
			<div id="addRuleItem" style="height: auto; line-height: auto;">
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		var jqGridCtrl = null;
		// 当前操作
		var ecardActGridHelper = JqGridHelper.newOne("");
		//验证表单
		var noticeFormProxy = FormProxy.newOne();
		//获取系统时间
		var __date = getServerTime();
		//初始下拉
		var __activityType = getDictSelectList("activityType");
		//loadSelectData("activityType", __activityType);
		//缓存e卡类型下拉框
		var __ecardType = null;
		//------------------------------------------初始化配置---------------------------------
		$(function() {
			//页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				allowTopResize : false
			});
			//
			loadPrmtTagSvcsData();
			//初始时间控件
			initDatePicker();
			//加载ecardType
			loadEcardType();
			//
			$id("btnAdd").click(openAddDlg);
			//添加规则
			$id("btnAddRule").click(function(){
				addRuleItem();
			});
			//
			$id("queryBtn").click(queryPrmtTagSvcxs);

			$(document).on("click", ".updateDisabled", function() {
				var id = $(this).attr("cellValue");
				var obj = ecardActGridHelper.getRowData(id);
				var msg = "";
				if (obj.disabled == false) {
					msg = "e卡活动正在使用中，确定要停用吗？";
				} else {
					msg = "e卡活动已停用，确定要启用吗？";
				}
				disabled_confirm(msg, jqGridCtrl, obj);
			});
			$(document).on("click", ".updateDeleted", function() {
				var id = $(this).attr("cellValue");
				var obj = ecardActGridHelper.getRowData(id);
				//updateDeletedById(jqGridCtrl,obj);
				deleted_confirm("确定要删除e卡活动吗?", jqGridCtrl, obj);
			});
			//修改
			$(document).on("click", ".update", function() {
				var id = $(this).attr("cellValue");
				openEditDlg(id);
			});
			
			//查看
			$(document).on("click", ".viewObj", function() {
				var id = $(this).attr("cellValue");
				openShowDlg(id);
			});
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
		});
		//获取卡类型seect
		function loadEcardType() {
			// 隐藏页面区
			//hideTown();
			var ajax = Ajax.post("/ecard/selectList/get");
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data= result.data;
					var items = data.items;
					items.add({"value":"select","text":"--请选择--"});
					__ecardType = data;
					//loadSelectData("ecardType", result.data);
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "ecardAct_List";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv",
					jqGridBox).height();
			var pagerHeight = $id("ecardAct_pager").height();
			jqGridCtrl.setGridWidth(mainWidth - 1 - 4);
			jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight
					- 3 - 54);
		}
		//-----------------------------业务处理------------------------------------------------
		//
		function queryPrmtTagSvcxs() {
			var filter = {};
			var q_prmtTag = textGet("q_prmtTag");
			if (q_prmtTag) {
				filter.tagId = q_prmtTag;
			}
			//
			var q_name = textGet("q_name");
			if (q_name) {
				filter.name = q_name;
			}
			//
			jqGridCtrl.jqGrid("setGridParam", {
				postData : {
					filterStr : JSON.encode(filter, true)
				},
				page : 1
			}).trigger("reloadGrid");
		}
		function loadPrmtTagSvcsData(){
			var filter = {};
			//
			var q_name = textGet("q_name");
			if(q_name){
				filter.name = q_name;
			}
			//
			jqGridCtrl= $("#ecardAct_List").jqGrid({
			      url : getAppUrl("/market/ecardAct/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			     // postData:{filterStr : JSON.encode(filter,true)},
			      height : "100%",
				  width : "100%",
				  colNames : ["E卡活动主题", "年份", "活动描述","开始时间","结束时间", "状态", " 操作" ], 
				  colModel : [
								{name:"name",width:100,align : 'left',formatter : function (cellValue, option, rowObject) {
			                		return "<div class='auto height'>"+cellValue+"</div>";
								}},
								{
									name:"year",
									index : "year",
									width:40,
									align : 'left'
								},
								{
									name : "desc",
									index : "desc",
									width : 200,
									align : 'left'
								},
								{
									name : "startTime",
									index : "startTime",
									width : 100,
									align : 'left',
									formatter : function (cellValue, option, rowObject) {
											return rowObject.startTime;
									}
								},
								{
									name : "endTime",
									index : "endTime",
									width : 100,
									align : 'left',
									formatter : function (cellValue, option, rowObject) {
											//var valid = '   '+ rowObject.startTime+"~ "+(rowObject.endTime == null ? "" : rowObject.endTime);
											return rowObject.endTime == null ?"无期限":rowObject.endTime;
									}
								},
								{
									name : "actState",
									index : "actState",
									width : 50,
									align : 'center',
									formatter : function(cellValue, option, rowObject) {
										var actState = rowObject.actState;
										var text = getActStateText(actState);
										return text;
									}
								},
								{
									name : 'id',
									index : 'id',
									formatter : function(cellValue, option, rowObject) {
										var actState = rowObject.actState;
										
										var disablied = rowObject.disabled == true ? '启用' : '停用';
										var viewHtml = "[<a class='item viewObj' href='javascript:void(0);' cellValue='"+ cellValue+ "' >查看</a>]";
										var updateHtml ="&nbsp;&nbsp;[<a class='item update' href='javascript:void(0);' cellValue='"+ cellValue+ "' >修改</a>]";
										var disabledHtml ="&nbsp;&nbsp;[<a class='item updateDisabled' href='javascript:void(0);' cellValue='"+ cellValue+ "' >"+disablied+"</a>]";
										var deleteHtml ="&nbsp;&nbsp;[<a class='item updateDeleted' href='javascript:void(0);' cellValue='"+ cellValue+ "' >删除</a>]";
										//
										var tempItem = String.builder();
										if(actState == "unStarted"){
											tempItem.append(viewHtml+updateHtml+disabledHtml);
										}else if(actState == "ended"){
											tempItem.append(viewHtml+updateHtml+deleteHtml);
										}else if(actState == "onGoing"){
											tempItem.append(viewHtml+disabledHtml);
										}else if(actState == "disabled"){
											tempItem.append(viewHtml+updateHtml+deleteHtml);
											var parsedDate = Date.parseAsDate(__date).format("yyyy-MM-dd");
											if(rowObject.endTime > parsedDate){
												tempItem.append(disabledHtml);
											}
										}else if(actState == "deleted"){
											tempItem.append(viewHtml);
										}
										return tempItem.value;
									},
									width : 180,
									align : "left"
								} ],  
				//rowList:[10,20,30],
				multiselect:false,
				multikey:'ctrlKey',
				pager : "#ecardAct_pager",
				loadComplete : function(gridData) { // JqGridHelper缓存最新的grid数据
					ecardActGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if (isFunction(callback)) {
						callback();
					}
				}
			});
		}
		function getCallbackAfterGridLoaded() {

		}
		function getActStateText(actState) {
			if (actState == "unStarted") {
				return "<span>未开始</span>";//修改 停用
			} else if (actState == "ended") {
				return "<span style='color:gray;'>已结束</span>";// 修改  删除 
			} else if (actState == "onGoing") {
				return "<span style='color:green;'>进行中</span>";//停用 
			} else if (actState == "disabled") {
				return "<span style='color:brown'>已停用</span>";//修改 启用 删除
			} else if (actState == "deleted") {
				return "<span style='color:red;font-style: italic;'>已删除</span>";
			}
		}
		//-------------------------------------------------初始时间------------------------------------------------
			//初始化日历插件
		function initDatePicker() {
			var startTime = textGet("startTime");
			startTime = isNoB(startTime) ? null : startTime;
			var endTime = textGet("endTime");
			endTime = isNoB(endTime) ? null : endTime;
			$id("startTime").datepicker({
				maxDate : endTime,
				lang : 'ch',
				onSelect : function(dateText, inst) {
					$id("endTime").datepicker("option", "minDate", dateText);
					noticeFormProxy.validate("startTime");
				}
			});
			//
			$id("endTime").datepicker({
				endTime : endTime == null ? 0 : endTime,
				lang : 'ch',
				onSelect : function(dateText, inst) {
					$id("startTime").datepicker("option", "maxDate", dateText);
					noticeFormProxy.validate("endTime");
				}
			});
		}
		//--------------------------------------------对话框-----------------------------------------------------
		//-----------------------------------------验证-------------------------------------------------
		var __oldName = "";// 保留原名称
		function isExistByName(name) {
			var isExist = false;// 是否存在
			if (name == __oldName) {// 验证是否跟原名称一样，如果一样则跳过验证
				isExist = false;
			} else {
				var ajax = Ajax.post("/market/ecardAct/exist/by/name");
				ajax.data({
					name : name
				});
				ajax.sync();
				ajax.done(function(result, jqXhr) {
					isExist = result.data;
				});
				ajax.go();
			}
			return isExist;
		}
		//打开新增对话框
		function openAddDlg() {
			//
			toShowTheDlg(null,"add");
		}
		//打开查看对话框
		function openShowDlg(dataId) {
			//
			toShowTheDlg(dataId,"view");
		}
		//打开查看对话框
		function openEditDlg(dataId) {
			//
			toShowTheDlg(dataId,"mod");
		}
		//（真正）显示对话款
		function toShowTheDlg(dataId, curAction) {
			//
			var theDlgId = "addActDialog";
			jqDlgDom = $id(theDlgId);
			//对话框配置
			var dlgConfig = {
				width : Math.min(750, $window.width()),
				height : Math.min(700, $window.height()),
				modal : true,
				open : false
			};
			//
			if (curAction == "add") {
				initTheDlgCtrls("add",dataId);
				dlgConfig.title = "新增";
				dlgConfig.buttons = {
					"保存" : function() {
						var result = noticeFormProxy.validateAll();
						if(result){
							var postData = noticeFormProxy.getValues();
							saveEcardAct(jqGridCtrl,postData);
							$(this).dialog("close");
						}
					},
					"取消" : function() {
						//
						$(this).dialog("close");
						noticeFormProxy.hideMessages();
					}

				};
				dlgConfig.close = function() {
					noticeFormProxy.hideMessages();
				};
			} else if (curAction == "mod") {
				initTheDlgCtrls("mod",dataId);
				dlgConfig.title = "修改";
				dlgConfig.buttons = {
					"保存" : function() {
						var result = noticeFormProxy.validateAll();
						if(result){
							var postData = noticeFormProxy.getValues();
							postData.id = $id("actId").val();
							updateEcardAct(jqGridCtrl,postData);
							$(this).dialog("close");
						}
					},
					"取消" : function() {
						$(this).dialog("close");
						noticeFormProxy.hideMessages();
					}
				};
			} else {
				initTheDlgCtrls("view",dataId);
				//== view 查看
				dlgConfig.title = "查看";
				dlgConfig.buttons = {
					//"修改 > " : function() {
					//	$(this).dialog("close");
					//	openEditDlg(dataId);
						//
					//},
					"关闭" : function() {
						$(this).dialog("close");
						noticeFormProxy.hideMessages();
					}
				};
			}
			//
			jqDlgDom.dialog(dlgConfig);
			//
			//initTheDlgCtrls(dataId);
		}
		function initTheDlgCtrls(curAction,dataId) {
			//批量简单设置
			if(curAction == "add"){
				textSet("name", "");
				textSet("title", "");
				textSet("startTime", null);
				textSet("endTime", null);
				textSet("actId",-1);
				showRuleItem([]);
			}
			//
			if (curAction == "view" || curAction == "mod") {
				var data = ecardActGridHelper.getRowData(dataId);
				if(data != null){
					textSet("name", data.name);
					textSet("title", data.title);
					textSet("actId",data.id);
					textSet("startTime", data.startTime);
					textSet("endTime", data.endTime);
					__oldName = data.name;
					var ruleList = data.ecardRuleList;
					if(curAction == "view"){
						showRuleItem(ruleList,"view");
					}else{
						showRuleItem(ruleList,"mod");
					}
				}
			}
			if(curAction == "view"){
				$id("addActDialog").find(":input").prop("disabled", true);
			}else{
				$id("addActDialog").find(":input").prop("disabled", false);
			}
		}
		//------------------------------------------------对话框业务处理-----------------------------------------
		$id("addRuleItem").on("change","[name='condType']",function(){
			//name="units"
			var units = $(this).next().find("[data-name='units']");
			if($(this).val() == 0){
				units.html("元");
			}else{
				units.html("张");
			}
		});
		//选择展示
		$id("addRuleItem").on("change","[name='activityType']",function(){
			//name="units"
			setActivityTypeSpanHtml($(this));
		});
		//选择物品
		$id("addRuleItem").on("click","[data-name='selectItems']",function(){
			var activityType = $(this).parent().prev().val();
			var ruleId = $(this).parent().prev().attr("data-pre");
			if(activityType == "mzsp"){
				
			}else if(activityType == "mzsp"){
				
			}else if(activityType == "msyhq"){
				var ids = getRuleGiftIds($(this));
				var extParams ={};
				extParams.ids = ids;
				extParams.ruleId=ruleId;
				openSvcxDlg("选择优惠券","/market/ecardAct/bind/coupon/jsp","_selectCoupon",extParams);
			}
		});
		//打开选择框
		function openSvcxDlg(title,url,_argName,params) {
			//对话框信息
			var pageTitle = title;
			var pageUrl = url;
			var argName = argName;
			var extParams = params;
			//对话框参数 预存
			setDlgPageArg(argName, extParams);
			//
			pageUrl = makeDlgPageUrl(pageUrl, argName);
			//
			console.log("页面url：" + pageUrl);
			//打开对话框-----------------------------------------
			var theDlg = Layer.dialog({
				title : pageTitle,
				src : pageUrl,
				area : [ '100%', '100%' ],
				closeBtn : true,
				maxmin : true, //最大化、最小化
				btn : [ "关闭" ],
				yes : function() {
					theDlg.hide();
				}
			});
		}
		//赋值
		function setActivityTypeSpanHtml(obj){
			var span = obj.next();
			var displayName = switchDirectory(obj.val());
			span.html(displayName);
		}
		//获取
		function switchDirectory(activityType){
			var displayName="";
			var inputs = "<input type='text' class='field value' style='width: 60px;'/>";
			var selectDisA = "<a data-name='selectItems' style='color:blue;' href='javascript:void(0)'>选择</a>";
			var _units = {"yuan":"元","discount":"折"};
			switch (activityType){
				case "mjje":
					displayName = inputs+_units.yuan;
					break;
				case "mzsp":
					displayName = selectDisA;
					break;
				case "mzfw":
					displayName = selectDisA;
					break;
				case "msyhq":
					displayName = selectDisA;
					break;
				case "mxzk":
					displayName = inputs+_units.discount;
					break;
				default:
					displayName = "";
					break;
			}
			return displayName;
		} 
		//------------------------------------------------初始表单---------------------------------------------
		noticeFormProxy.addField({
			id : "name",
			key : "name",
			required : true,
			rules : [ {
				rule : function(idOrName, type, rawValue, curData) {
					// 若显示且为空，给予提示
					if ($id("name").is(":visible") && isNoB(rawValue)) {
						return false;
					}
					var isExist = isExistByName(rawValue);
					return !isExist;
				},
				message : "名称已存在!"
			} ,"maxLength[30]"]
		});
		noticeFormProxy.addField({
			id : "title",
			key : "title"
		});
		noticeFormProxy.addField({
			id : "startTime",
			key : "startTime",
			required : true
		});
		noticeFormProxy.addField({
			id : "endTime",
			key : "endTime",
			required : true
		});
		noticeFormProxy.addField({
			key : "ecardRuleList",
			get : function(key, type, curData){
				var ecardRuleList = [];
				var divLength = $("[data-name='addRule']").length;
				if(divLength != null && divLength > 0){
					$("[data-name='addRule']").each(function(){
						var rileItem = getEcardActRule(this);
						var ruleGiftItem = [];
						var activityType = rileItem.actType;
						if(activityType == "mzsp"){
							
						}else if(activityType == "mzfw"){
							
						}else if(activityType == "msyhq"){
							var trName = $(this).find("[name='activityType']").next().next().next().find("[name='giftInfo']");
							var trNameLength = trName.length;
							if(trNameLength != null && trNameLength >0){
								$(trName).each(function(){
									var giftItem = getEcardActGift(this);
									ruleGiftItem.add(giftItem);
								});
							}
						}else if(activityType == "mjje"){
							
						}else if(activityType == "mxzk"){
							
						}
						rileItem.ruleGiftItemList = ruleGiftItem;
						ecardRuleList.add(rileItem);
					});
				}
				return ecardRuleList;
			},
			onHideMessage : function(key){
				var divLength = $("[data-name='addRule']").length;
				if (divLength != null&& divLength > 0) {
					var ecardType = $attr("name","ecardType");
					hideMiscTip($id(ecardType));
					//
					var condTypeValueWin = $("[name='condType']").next().find("input");
					hideMiscTip($id(condTypeValueWin));
					//
					var selectItems = $attr("data-name","selectItems");
					hideMiscTip($id(selectItems));
				} else {
					hideMiscTip($id("btnAddRule"));
				}
			},
			rules :[{
				rule : function(key, type) {
					var isAdopt = false;
					var divLength = $("[data-name='addRule']").length;
					if(divLength != null && divLength>0){
						$("[data-name='addRule']").each(function(index){
							//判断e卡类型--------------------------------------------------------------------------
							var ecardType = $(this).find("[name='ecardType']");
							//
							if(!$id(ecardType).data("validateBound")){
								$id(ecardType).data("validateBound", true);
								$id(ecardType).change(function() {
									if(ecardType.val() == "select"){
										showErrorTip($(ecardType),"请选择e卡类型");
									}else{
										hideMiscTip($(ecardType));
									}
								});
							}
							$id(ecardType).trigger("change");
							//
							if(ecardType.val() == "select"){
								return false;
							}
							//判断条件类型值-------------------------------------------------------------------------
							var condType = $(this).find("[name='condType']").val();
							//文本框对象
							var condTypeValueWin = $(this).find("[name='condType']").next().find("input");
							//
							if(!$id(condTypeValueWin).data("validateBound")){
								$id(condTypeValueWin).data("validateBound", true);
								$id(condTypeValueWin).change(function() {
									var condTypeValue = condTypeValueWin.val();//文本框值
									if(isNoB(condTypeValue)){
										showErrorTip(condTypeValueWin,"不可为空！");
									}else{
										if(condType == 1){
											var isNum = FormProxy.validateRules.isNum(condTypeValue);
											if(!isNum){
												showErrorTip(condTypeValueWin,"必须是数字");
											}else{
												hideMiscTip(condTypeValueWin);
											}
										}else{
											var isMoney = FormProxy.validateRules.isMoney(condTypeValue);
											if(!isMoney){
												showErrorTip(condTypeValueWin,"必须是金额");
											}else{
												hideMiscTip(condTypeValueWin);
											}
										}
									}
								});
							}
							$id(condTypeValueWin).trigger("change");
							//
							var condTypeValue = condTypeValueWin.val();//文本框值
							if(isNoB(condTypeValue)){
								return false;
							}else{
								if(condType == 1){
									var isNum = FormProxy.validateRules.isNum(condTypeValue);
									if(!isNum){
										return false;
									}
								}else{
									var isMoney = FormProxy.validateRules.isMoney(condTypeValue);
									if(!isMoney){
										return false;
									}
								}
							}
							//选择-------------------------------------------------------------------------------
							var activityType = $(this).find("[name='activityType']").val();
							if(activityType == "mzsp"){
								
							}else if(activityType == "mzfw"){
								
							}else if(activityType == "msyhq"){
								var trNameLength = $(this).find("[name='activityType']").next().next().next().find("[name='giftInfo']").length;
								var selectItems = $(this).find("[name='activityType']").next().find("[data-name='selectItems']");
								if(trNameLength == null || trNameLength <=0){
									showErrorTip(selectItems,"请选择优惠券");
									return false;
								}else{
									hideMiscTip(selectItems);
								}
							}else if(activityType == "mjje"){
								
							}else if(activityType == "mxzk"){
								
							}
							if (index + 1 == divLength) {
								isAdopt = true;
							}
						});
					}else{
						//请添加规则
						showErrorTip($id("btnAddRule"),"请添加规则");
					}
					if (!isAdopt) {
						return false;
					}
					return true;
				}
			}]
		});
		//-------------------------------------------------业务逻辑-------------------------------------------------
		//获取规则
		function getEcardActRule(obj){
			var rileItem = {};
			var ruleId = $(obj).attr("data-ruleId");
			var ecardType = $(obj).find("[name='ecardType']").val();
			var condType = $(obj).find("[name='condType']").val();
			var condTypeValue = $(obj).find("[name='condType']").next().find("input").val();
			var activityType = $(obj).find("[name='activityType']").val();
			var firstTimeOnly =$(obj).find("[name='firstTimeOnly']:checked").val();
			//
			if(!isUndef(firstTimeOnly)){
				rileItem.firstTimeOnly = firstTimeOnly;
			}else{
				rileItem.firstTimeOnly = false;
			}
			rileItem.cardCode = ecardType || null;
			rileItem.condType = condType || null;
			rileItem.condValue = condTypeValue || null;
			rileItem.actType = activityType || null;
			if(ruleId!= null && ruleId!= ""){
				rileItem.id = ruleId;
			}else{
				rileItem.id = -1;
			}
			return rileItem;
		}
		//获取规则赠品
		function getEcardActGift(obj){
			var giftItem = {};
			var value = $(obj).attr("data-id");
			var type = "coupon";
			var flag = $(obj).attr("data-type");
			var text = $(obj).attr("data-name");
			var giftId = $(obj).attr("data-giftId");
			//
			//if(flag == "svc"){
			//	giftItem.flag = 1;
			//}else{
			//}
			giftItem.value = value || null;
			giftItem.type = type || null;
			giftItem.text = text || null;
			giftItem.flag = flag || null;
			if(giftId!= null && giftId!= ""){
				giftItem.id = giftId;
			}else{
				giftItem.id = -1;
			}
			return giftItem;
		}
		//保存操作
		function saveEcardAct(jqGridCtrl, obj) {
			var ajax = Ajax.post("/market/ecardAct/add/do");
			var data = obj;
			ajax.data(data);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					jqGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		//更新操作
		function updateEcardAct(jqGridCtrl, obj) {
			var ajax = Ajax.post("/market/ecardAct/update/do");
			var data = obj;
			ajax.data(data);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					jqGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		//上架提示
		function disabled_confirm(msg, jqGridCtrl, obj) {
			var yesHandler = function(layerIndex) {
				theLayer.hide();
				updateDisabled(jqGridCtrl, obj);
			};
			var noHandler = function(layerIndex) {
				theLayer.hide();
			};
			//
			var theLayer = Layer.confirm(msg, yesHandler, noHandler);

		}
		//删除提示
		function deleted_confirm(msg, jqGridCtrl, obj) {
			var yesHandler = function(layerIndex) {
				theLayer.hide();
				updateDeleted(jqGridCtrl, obj);
			};
			var noHandler = function(layerIndex) {
				theLayer.hide();
			};
			//
			var theLayer = Layer.confirm(msg, yesHandler, noHandler);
		}
		//启用禁用操作
		function updateDisabled(jqGridCtrl, obj) {
			var ajax = Ajax.post("/market/ecardAct/disabled/update/do");
			var data = {
				"id" : obj.id,
				"disabled" : obj.disabled == true ? false : true
			};
			ajax.data(data);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					jqGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		//删除操作
		function updateDeleted(jqGridCtrl, obj) {
			var ajax = Ajax.post("/market/ecardAct/deleted/update/do");
			var data = {
				"id" : obj.id,
				"deleted" : obj.deleted == true ? false : true
			};
			ajax.data(data);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					jqGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		//-------------------------------------------------添加规则---------------------------------------------
		function addRuleGiftItem(item){
			if(item != null){
				var theTpl = laytpl($id("addRuleGiftInfoTpl").html());
				var trLength = $("#packItem_tbd"+item.ruleId).children("tr").length;
				var htmlStr = theTpl.render(item);
				if (trLength <= 0) {
					$("#packItem_tbd"+item.ruleId).html(htmlStr);
				} else {
					$("#packItem_tbd"+item.ruleId+" tr:eq(-1)").after(htmlStr);
				}
				hideMiscTip($id("addRule"+item.ruleId).find("[data-name='selectItems']"));
				return true;
			}else{
				return false;
			}
		}
		function showRuleItem(items,curAction) {
			var viewDisplay = false;
			if(curAction == "view"){
				var viewDisplay = true;
			}
			if(items != null && items.length > 0){
				$("#addRuleItem").html("");
				for (var i = 0; i < items.length; i++) {
					var item = items[i];
					var theTpl = laytpl($id("actRuleListTpl").html());
					var itemObj = {
							viewDisplay : viewDisplay,
							item : item
						};
					var htmlStr = theTpl.render(itemObj);
					$("#addRuleItem").append(htmlStr);
					//
					//初始选送的物品或折扣
					loadSelectData("activityType"+item.id, __activityType);
					setSelectValue("activityType"+item.id, item.actType);
					if(curAction != "view"){
						setActivityTypeSpanHtml($id("activityType"+item.id));
					}
					//初始e卡选择
					if(__ecardType){
						loadSelectData("ecardType"+item.id, __ecardType);
						setSelectValue("ecardType"+item.id,item.cardCode);
					}
					//初始条件类型
					setSelectValue("condType"+item.id, item.condType);
				}
			}else{
				$("#addRuleItem").html({});
			}
		}
		function addRuleItem() {
			var theTpl = laytpl($id("addRuleInfoTpl").html());
			var divLength = $("[data-name='addRule']").length;
			var id= "-"+(divLength+1);
			if (divLength <= 0) {
				var htmlStr = theTpl.render({id:id});
				$("#addRuleItem").html(htmlStr);
			} else {
				var htmlStr = theTpl.render({id:id});
				$("#addRuleItem [data-name='addRule']:eq("+(divLength-1)+")").after(htmlStr);
			}
			//初始选送的物品或折扣
			loadSelectData("activityType"+id, __activityType);
			setSelectValue("activityType"+id, "msyhq");
			//
			setActivityTypeSpanHtml($id("activityType"+id));
			//初始e卡选择
			if(__ecardType){
				loadSelectData("ecardType"+id, __ecardType);
				setSelectValue("ecardType"+id,"select");
			}
			hideMiscTip($id("btnAddRule"));
		}
		//处理状态
		function getTypeName(type) {
			var typeName = null;
			switch (type) {
			case "nopay":
				typeName = "免付券";
				break;
			case "sprice":
				typeName = "特价券";
				break;
			case "deduct":
				typeName = "抵金券";
				break;
			}
			return typeName;
		}
		
		function getRuleGiftIds(obj){
			var ids = [];
			var trName = obj.parent().next().next().find("[name='giftInfo']");
			if(trName.length>0){
				trName.each(function(){
					var id = $(this).attr("data-id");
					var type = $(this).attr("data-type");
					if(type == 1){
						ids.add("svc"+id);
					}else{
						ids.add("goods"+id);
					}
				});
			}
			return ids;
		}
		//删除选择
		function onDeleteCouponInfo(ruleId,type,id) {
			if (id != null && ruleId != null && type != null) {
				$id("giftInfo"+ruleId+type+id).remove();
			} else {
				var theLayer = Layer.error("删除失败！", function(layerIndex) {
					theLayer.hide();
				});
			}
		}
	</script>
</body>
<script type="text/html" id="addRuleInfoTpl" title="套餐服务明细">
{{# var ecardId=d; }}
<div id="addRule{{ecardId.id}}" data-ruleId="-1" data-name="addRule" style="height: auto; line-height: auto;">
	<div class="field row" style="height: auto; line-height: auto;line-height:normal;">
		<label class="field label one wide" style="height: auto; line-height: auto;">&nbsp;</label>
		<hr class="divider" style="color: black;">
		<div>
			<span><input name="firstTimeOnly" type="checkbox" value="true">首次</span>
			<span>购买</span>
			<span>
				<select class="field value" id="ecardType{{ecardId.id}}" name="ecardType">
			</select>
			</span>
			<span>
				<select class="field value" id="condType{{ecardId.id}}" name="condType">
					<option value="1" title="数量">数量</option>
					<option value="0" title="价格">价格</option>
				</select>满
				<span><input type="text" class="field value" style="width: 60px;"/><span data-name="units">张</span></span>
			</span>
			<span>
				<select class="field value" id="activityType{{ecardId.id}}" data-pre="{{ecardId.id}}" name="activityType" disabled="disabled">
				</select>
				<span>
				</span>
				<label class="field label one wide">&nbsp;</label>
				<table data-role="staff_tab" class="gridtable">
					<tbody id="packItem_tbd{{ecardId.id}}">
					</tbody>
				</table>
			</span>
		</div>
	</div>
</div>
</script>
<script type="text/html" id="addRuleGiftInfoTpl" title="选择优惠券">
{{# var item = d; }}
{{# if(!isNoB(item)){ }}
	<tr id="giftInfo{{item.ruleId}}{{item.itemSelectType}}{{item.itemFlag}}{{item.itemId}}" name="giftInfo" data-name="{{item.itemName}}" data-id="{{item.itemId}}" data-type="{{item.itemFlag}}" data-giftId="-1">
		<td width="100px">{{# if(item.itemFlag == 1){ }}服务{{# }else{ }}商品{{# } }}</td>
		<td width="100px">{{getTypeName(item.itemType)}}</td>
		<td width="200px">{{item.itemName}}</td>
		<td width="50px"><a href="javascript:void(0)" style="color:blue;" onClick="onDeleteCouponInfo('{{item.ruleId}}','{{item.itemSelectType}}{{item.itemFlag}}','{{item.itemId}}')">删除</a></td>
	</tr>
{{# } }}
</script>

<script type="text/html" id="actRuleListTpl" title="查看修改">
{{# var viewDisplay=d.viewDisplay; }}
{{# var rule=d.item; }}
{{# if(!isNoB(rule)){ }}
<div id="addRule{{rule.id}}" data-ruleId="{{rule.id}}" data-name="addRule" style="height: auto; line-height: auto;">
	<div class="field row" style="height: auto; line-height: auto;line-height:normal;">
		<label class="field label one wide" style="height: auto; line-height: auto;">&nbsp;</label>
		<hr class="divider" style="color: black;">
		<div>
			<span><input name="firstTimeOnly" type="checkbox" value="true" {{# if(rule.firstTimeOnly == true){ }} checked="checked"{{# } }}>首次</span>
			<span>购买</span>
			<span>
				<select class="field value" id="ecardType{{rule.id}}" name="ecardType">
			</select>
			</span>
			<span>
				<select class="field value" id="condType{{rule.id}}" name="condType">
					<option value="1" title="数量">数量</option>
					<option value="0" title="价格">金额</option>
				</select>满
				<span><input type="text" class="field value" value="{{rule.condValue}}" style="width: 60px;"/><span data-name="units">{{# if(rule.condType == 1){ }}张{{# }else{ }}元{{# } }}</span></span>
			</span>
			<span>
				<select class="field value" id="activityType{{rule.id}}" data-pre="{{rule.id}}" name="activityType">
				</select>
				<span>
				</span>
				<label class="field label one wide">&nbsp;</label>
				<table data-role="staff_tab" class="gridtable">
					<tbody id="packItem_tbd{{rule.id}}">
						{{# var gifts = rule.ruleGiftItemList; }}
						{{# if(!isNoB(gifts) && gifts.length >0){ }}
							{{# for(var j = 0, leng = gifts.length; j < leng; j++){ }}
							{{# var gift = gifts[j]; }}
								<tr id="giftInfo{{rule.id}}{{gift.type}}{{gift.flag}}{{gift.value}}" name="giftInfo" data-name="{{gift.text}}" data-id="{{gift.value}}" data-type="{{gift.flag}}" data-giftId="{{gift.id}}">
									<td width="100px">{{# if(gift.flag == 1){ }}服务{{# }else{ }}商品{{# } }}</td>
									<td width="100px">{{getTypeName(gift.couponType)}}</td>
									<td width="200px">{{gift.text}}</td>
									<td width="50px"><a href="javascript:void(0)"{{# if(viewDisplay == false) { }} style="color:blue;" onClick="onDeleteCouponInfo('{{rule.id}}','{{gift.type}}{{gift.flag}}','{{gift.value}}')" {{# } }} >删除</a></td>
								</tr>
							{{# } }}
						{{# } }}
					</tbody>
				</table>
			</span>
		</div>
	</div>
</div>

{{# } }}

</script>
</html>
