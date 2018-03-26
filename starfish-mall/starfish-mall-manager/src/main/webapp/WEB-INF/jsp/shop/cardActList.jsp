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
			//加载ecardType
			loadEcardType();
			//
			$id("btnToQry").click(queryPrmtTagSvcxs);
			
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
			//
			var q_name = textGet("queryName");
			if (q_name) {
				filter.name = q_name;
			}
			filter.disabled = false;
			filter.deleted = false;
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
			var q_name = textGet("queryName");
			if(q_name){
				filter.name = q_name;
			}
			filter.disabled = false;
			filter.deleted = false;
			//
			jqGridCtrl= $("#ecardAct_List").jqGrid({
			      url : getAppUrl("/market/ecardAct/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(filter,true)},
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
										var viewHtml = "[<a class='item viewObj' href='javascript:void(0);' cellValue='"+ cellValue+ "' >查看</a>]";
										//
										var tempItem = String.builder();
										if(actState == "unStarted"){
											//tempItem.append(viewHtml+updateHtml+disabledHtml);
										}else if(actState == "ended"){
											//tempItem.append(viewHtml+updateHtml+deleteHtml);
										}else if(actState == "onGoing"){
											tempItem.append(viewHtml);
										}else if(actState == "disabled"){
											
										}else if(actState == "deleted"){
											//tempItem.append(viewHtml);
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
		//打开查看对话框
		function openShowDlg(dataId) {
			//
			toShowTheDlg(dataId,"view");
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
			} else if (curAction == "mod") {
			} else {
				initTheDlgCtrls("view",dataId);
				//== view 查看
				dlgConfig.title = "查看";
				dlgConfig.buttons = {
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
		function showRuleItem(items,curAction) {
			if(items != null && items.length > 0){
				$("#addRuleItem").html("");
				for (var i = 0; i < items.length; i++) {
					var item = items[i];
					var theTpl = laytpl($id("actRuleListTpl").html());
					var itemObj = {
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
		//处理状态
		function getTypeName(type) {
			var typeName = "";
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
		
	</script>
</body>

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
