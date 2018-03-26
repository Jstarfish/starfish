<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>服务优惠券列表</title>

<style type="text/css">
.targetType a{
margin-left:20px;
color: blue;
}
</style>
</head>
<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnToAdd" class="button">添加</button>
				</div>
			</div>
		</div>
	</div>
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	<div id="mainPanelRecycle" class="ui-layout-center" style="padding: 4px;">
		<table id="recycleGridCtrl"></table>
		<div id="recycleGridPager"></div>
	</div>
	<div id="dialog" style="display: none;">
		<div id="addForm" class="form">
			<div class="field row">
				<label class="field label one half wide required">名称：</label> 
				<input type="text" id="name" name="name" class="field value one half wide" /> 
			</div>
			<div class="field row">
				<label class="field label one half wide required">显示为：</label> 
				<input type="text" id="title" name="title" class="field value one half wide" />
			</div>
			<div class="field row">	
				<label class="field label one half wide required">类型：</label> 
				 <div id="svcCouponType" class="field group">
				 	<input type="radio" id="type-T" name="type" value="sprice" checked="checked">
					<label for="type-T">特价券</label>
					<input type="radio" id="type-D" name="type" value="deduct">
					<label for="type-D">抵金券</label>
					<input type="radio" id="type-M" name="type" value="nopay"> 
					<label for="type-M">免付券</label> 
				</div>
			</div>
			<div id="isShowType">
				<div class="field row">
				<label class="field label one half wide">最低消费金额：</label> 
				<input type="text" id="limitAmount" name="limitAmount" class="field value one half wide" />元
				</div>
				<div class="field row">
					<label class="field label one half wide">折扣金额：</label> 
					<input type="text" id="price" name="price" class="field value one half wide" />元
				</div>
			</div>
			<div class="field row">
				<label class="field label one half wide">结算金额：</label> 
				<input type="text" id="settlePrice" name="settlePrice" class="field value one half wide" />元
			</div>
			<div class="field row">	
				<label class="field label one half wide required" >关联：</label>
				<div class="targetType">
					<a id="carSvc" href="#">服务</a>
				</div>		
			</div>
			<div id="showTargetInfo" style="display: none;">
				<div class="field row">	
					<label class="field label one half wide" >&nbsp;</label>
					<div id="svcName" class="field group">
					</div>	
					<input  type="hidden" id="svcId" name="svcId"  value="">
				</div>
			</div>
			
			<div class="field row">
				<label class="field label one half wide required">发放时间：</label> 
				<input type="text" id="issueStartTime" class="field value" />至
				<input type="text" id="issueEndTime" class="field value" />
			</div>
			<div class="field row">	
				<label class="field label one half wide required">有效期：</label> 
				 <div class="field group">
				 	<input type="radio" id="validType-T" name="validType" value="0" checked="checked">
					<label for="validType-T">按天数</label>
					<input type="radio" id="validType-D" name="validType" value="1">
					<label for="validType-D">按时间段</label>
				</div>
			</div>
			<div id="isShowValidTypeDays">
				<div class="field row">
				<label class="field label one half wide">天数：</label>
				<input type="text" id="validDays" name="validDays" class="field value" />天
				</div>
			</div>
			<div id="isShowValidTypeTime" style="display: none;">
				<div class="field row">
					<label class="field label one half wide">时间：</label>
					<div class="field group">
					<input type="text" id="validStartTime" class="field value" />至
					<input type="text" id="validEndTime" class="field value" />
					</div>
				</div>
			</div>
			<div class="field row">
				<label class="field label one half wide">总张数：</label> 
				<input type="text" id="limitCount" name="limitCount" class="field value" />
				<span style="color: red;">&nbsp;&nbsp;张</span>
			</div>
			<div class="field row" style="height: 70px;">	
				<label class="field label one half wide" >描述：</label>
				<textarea class="field value one half wide" style="height: 60px;width:400px; resize: none;" name="desc" id="desc"></textarea>
				<span class="normal spacer"></span>
			</div>
		</div>
	</div>	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js"></script>
	<script>
		//-----------------------------------------验证-------------------------------------------------
		var oldName = "";// 保留原名称
		var isExist = false;// 是否存在
		function isExistByName(name) {
			if (name == oldName) {// 验证是否跟原名称一样，如果一样则跳过验证
				isExist = false;
			} else {
				var ajax = Ajax.post("/market/svcCoupon/exist/by/name");
				ajax.data({
					name : name
				});
				ajax.sync();
				ajax.done(function(result, jqXhr) {
					isExist = result.data;
				});
				ajax.go();
			}
		}
		//------------------------------------------初始化配置---------------------------------{{
		// 获取整合后的状态信息
		function getCouponValidValue(obj) {
			if(obj.validDays!=null&&obj.validDays!=""){
				return obj.validDays+"&nbsp;天";
			}else if(obj.validStartTime!=null){
				var valid ="<span style='color:green;'>"+obj.validStartTime+"</span>到<span style='color:red;'>"+(obj.validEndTime)+"</span>";
				return valid;
			}
		}
		//弹出提示
		function target_confirm(msg,diaLogInfo) {
			//
			var yesHandler = function(layerIndex) {
				theLayer.hide();
				diaLog(diaLogInfo);
			};
			var noHandler = function(layerIndex) {
				theLayer.hide();
			};
			//
			var theLayer = Layer.confirm(msg, yesHandler, noHandler);
		}
		//
		function diaLog(diaLogInfo) {
			Layer.dialog({
				title : diaLogInfo.title,
				id:diaLogInfo.id,
				src : diaLogInfo.src,
				area : [ '100%', '100%' ],
				closeBtn : true,
				btn : [ "确定", "取消" ], //,
				yes : function(index) {
					var iframeWin=getFrameWin(index).contentWindow;
					var targetInfo=iframeWin.getSelectedTargetInfo();
					showTargetInfo(targetInfo);
	            	Layer.hideAll();
				},
				cancel : function() {
					Layer.hideAll();
				}
			});
		}
		var jqGridGlobalSetting=null;
		//
		function updateDisabledById(jqGridCtrl,obj){
			var ajax = Ajax.post("/market/svcCoupon/disabled/update/do");
			var data={"id":obj.id,"disabled":obj.disabled==true?false:true};
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
		//
		function updateDeletedById(jqGridCtrl,obj){
			var ajax = Ajax.post("/market/svcCoupon/mark/deleted/update/do");
			var data={"id":obj.id,"deleted":obj.deleted==true?false:true};
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
		//获取frame窗口
		function getFrameWin(layerIndex) {
			var theMain = $id("layui-layer" + layerIndex).find("> div.layui-layer-content");
			var frame = theMain.find(" > iframe ");
			return frame.size() > 0 ? frame.get(0) : null;
		}
		//赋值
		function showTargetInfo(targetInfo) {
			$id("svcName").html("你关联啦"+targetInfo.targetName+"：<span style='color:red'>"+targetInfo.name+"</span>");
			$id("svcId").val(targetInfo.id);
			$id("showTargetInfo").show();
		}
		//点击类型显示不同
		$("input[name='type']").click(function() {
			var type=$(this).attr("value");
			showOrHideType(type);
		});
		//
		function showOrHideType(type) {
			if(type=="sprice"||type=="deduct"){
				$id("isShowType").show();
			}else{
				$id("isShowType").hide();
			}
		}
		//
		$("input[name='validType']").click(function() {
			var type=$(this).attr("value");
			showOrHideValid(type);
		});
		//显示隐藏
		function showOrHideValid(type) {
			if(type=="0"){
				$id("isShowValidTypeDays").show();
				$id("isShowValidTypeTime").hide();
			}else if(type=="1"){
				$id("isShowValidTypeDays").hide();
				$id("isShowValidTypeTime").show();
			}
		}
		//初始化日历插件
		 function initDatePicker(shopInfoProxy){
				var issueStartTime = textGet("issueStartTime");
				issueStartTime = isNoB(issueStartTime) ? null : issueStartTime;
				var issueEndTime = textGet("issueEndTime");
				issueEndTime = isNoB(issueEndTime) ? null : issueEndTime;
				$id("issueStartTime").datepicker({
					maxDate : issueEndTime,
					lang:'ch',
				    onSelect:function(dateText,inst){
				       $id("issueEndTime").datepicker("option","minDate",dateText);
				       shopInfoProxy.validate("issueStartTime");
				    }
				});
				//
				$id("issueEndTime").datepicker({
					issueEndTime :issueEndTime == null ? 0 : issueEndTime, 
					lang:'ch',
					onSelect:function(dateText,inst){
				        $id("issueStartTime").datepicker("option","maxDate",dateText);
				        shopInfoProxy.validate("issueEndTime");
				    }
				});
		}
		//初始化日历插件
		 function initvalidStartPicker(shopInfoProxy){
				var validStartTime = textGet("validStartTime");
				validStartTime = isNoB(validStartTime) ? null : validStartTime;
				var validEndTime = textGet("validEndTime");
				validEndTime = isNoB(validEndTime) ? null : validEndTime;
				$id("validStartTime").datepicker({
					maxDate : validEndTime,
					lang:'ch',
				    onSelect:function(dateText,inst){
				       $id("validEndTime").datepicker("option","minDate",dateText);
				       shopInfoProxy.validate("validStartTime");
				    }
				});
				//
				$id("validEndTime").datepicker({
					validEndTime :validEndTime == null ? 0 : validEndTime, 
					lang:'ch',
					onSelect:function(dateText,inst){
				        $id("validStartTime").datepicker("option","maxDate",dateText);
				        shopInfoProxy.validate("validEndTime");
				    }
				});
		}
		
		//
		function disabled_confirm(msg,jqGridCtrl,obj) {
			var yesHandler = function(layerIndex) {
				theLayer.hide();
				updateDisabledById(jqGridCtrl,obj);
			};
			var noHandler = function(layerIndex) {
				theLayer.hide();
			};
			//
			var theLayer = Layer.confirm(msg, yesHandler, noHandler);
		}
		//
		function deleted_confirm(msg,jqGridCtrl,obj) {
			var yesHandler = function(layerIndex) {
				theLayer.hide();
				updateDeletedById(jqGridCtrl,obj);
			};
			var noHandler = function(layerIndex) {
				theLayer.hide();
			};
			//
			var theLayer = Layer.confirm(msg, yesHandler, noHandler);
		}
		var recycleGridCtrl=null;
		$(function() {
			var _prevWin = window.parent;
			var titleId=$("#titlePanel",_prevWin.document).find("> div .title");
			titleId.html("<a id='couponId' href='javascript:void(0);' style='color:#000000;text-decoration: none;'>优惠券列表</a>&nbsp;|&nbsp;<a id='recycleId' href='javascript:void(0);' style='color:#000000;text-decoration: none;'>回收站</a>");
			var recycleId=$("#recycleId",_prevWin.document);
			var couponId=$("#couponId",_prevWin.document);
			recycleId.click(function() {
				$id("mainPanel").hide();
				$id("mainPanelRecycle").show();
				recyceGrid()
				var winSizeProduct = new __WinSizeMonitor();
				winSizeProduct.start(adjustCtrlsSize);
			});
			$id("mainPanel").jgridDialog(
					{
						dlgTitle : "服务优惠券",
						//listUrl : "/goods/carSvc/list/get",
						addUrl : "/market/svcCoupon/add/do",
						updUrl : "/market/svcCoupon/update/do",
						//delUrl : "/market/coupon/update/deleted/do",
						rootPanelSetting:{
							north__size : 60
						},
					jqGridGlobalSetting : {
						url : getAppUrl("/market/svcCoupon/list/get"),
						colNames : ["名称", "显示为","类型","关联","发放时间", "有效期", "总数","状态","操作" ],
						colModel : [
								{
									name : "name",
									index : "name",
									width : 100,
									align : 'left'
								},
								{
									name : "title",
									index : "title",
									width : 200,
									align : 'left'
								},
								{
									name : "type",
									index : "type",
									width : 40,
									align : 'left',
										formatter : function(cellValue) {
											var type=null;
											switch (cellValue) {
											case "nopay":
												type="免付券";
												break;
											case "sprice":
												type="特价券";
												break;
											case "deduct":
												type="抵金券";
												break;
											}
											return type;
										}
								},
								{
									name : "svcName",
									index : "svcName",
									width : 100,
									align : 'left'
								},
								{
									name : "issueStartTime",
									index : "issueStartTime",
									width : 70,
									align : 'left'
								},
								 {
									name : "valid",
									index : "valid",
									formatter : function(cellValue, option, rowObject) {
										return getCouponValidValue(rowObject);
									},
									width : 150,
									align : 'center'
								},
								{
									name : "limitCount",
									index : "limitCount",
									width : 50,
									align : 'left'
								},
								{
									name : "disabled",
									index : "disabled",
									width : 60,
									align : 'center',
									formatter : function(cellValue) {
										return cellValue == true ? '已禁用' : '已启用';
									}
								},
								{//
									name : 'id',
									index : 'id',
									formatter : function(cellValue, option, rowObject) {
										var disablied=rowObject.disabled==true?'启用':'禁用';
										var tempItem = String.builder();
										tempItem.append("[<a class='item dlgview' href='javascript:void(0);' cellValue='"+ cellValue+ "' >查看</a>]");
										tempItem.append("&nbsp;&nbsp;&nbsp;[<a class='item updateDisabled' href='javascript:void(0);' cellValue='"+ cellValue+ "' >"+ disablied+ "</a>]");
										if (rowObject.disabled == true) {
											tempItem.append("&nbsp;&nbsp;&nbsp;[<a class='item dlgupd' href='javascript:void(0);' cellValue='"+ cellValue+ "' >修改</a>]");
											tempItem.append("&nbsp;&nbsp;&nbsp;[<a class='item updateDeleted' href='javascript:void(0);' cellValue='"+ cellValue+ "' >删除</a>]");
										}
										return tempItem.value;
									},
									width : 130,
									align : "center"
								} ],
								ondblClickRow : function(rowId) {
									
								}
					},
						// 增加对话框
						addDlgConfig : {
							width : Math.min(650, $window.width()),
							height : Math.min(700, $window.height()),
							modal : true,
							open : false
						},
						// 修改和查看对话框
						modAndViewDlgConfig : {
							width : Math.min(650, $window.width()),
							height : Math.min(700, $window.height()),
							modal : true,
							open : false
						},
						
						/**
						 * 增加时清空控件的值
						 */
						addInit : function() {
							textSet("name", "");
							textSet("title", "");
							radioSet("type","sprice");
							radioSet("validType","0");
							showOrHideType("sprice");
							showOrHideValid("0");
							textSet("limitAmount", "");
							textSet("price", "");
							textSet("settlePrice", "");
							textSet("svcId", "");
							textSet("issueStartTime", "");
							textSet("issueEndTime", "");
							textSet("validDays", "");
							textSet("validStartTime", "");
							textSet("validEndTime", "");
							textSet("limitCount", "");
							textSet("desc", "");
							$id("showTargetInfo").hide();
							$id("svcName").html("");
						},

						/**
						 * 修改和查看是给控件赋值
						 */
						modAndViewInit : function(data) {
							textSet("name",data.name);
							textSet("title",data.title);
							radioSet("type",data.type);
							radioSet("validType",data.validType);
							showOrHideValid(data.validType);
							showOrHideType(data.type);
							textSet("limitAmount",data.limitAmount);
							textSet("price", data.price);
							textSet("settlePrice", data.settlePrice);
							textSet("svcId", data.svcId);
							textSet("issueStartTime", data.issueStartTime);
							textSet("issueEndTime",data.issueEndTime);
							textSet("validDays", data.validDays);
							textSet("validStartTime", data.validStartTime);
							textSet("validEndTime", data.validEndTime);
							textSet("limitCount", data.limitCount);
							textSet("desc",data.desc);
							$id("svcCouponType").find(":input").prop("disabled", true);
							$id("svcName").html("关联服务名称："+data.svcName);
							$id("showTargetInfo").show();
						},
						/**
						 * 封装查询参数
						 */
						queryParam : function(querJson, formProxyQuery) {
							var name = formProxyQuery.getValue("queryName");
							if (name != "") {
								querJson['name'] = name;
							}
						},

						/**
						 * 注册增加和修改的验证表单控件
						 */
						formProxyInit : function(formProxy) {
							//注册表单控件
							formProxy.addField({
								id : "name",
								key : "name",
								required : true,
								rules : [ {
									rule : function(idOrName, type, rawValue, curData) {
										// 若显示且为空，给予提示
										if ($id("name").is(":visible") && isNoB(rawValue)) {
											return false;
										}
										isExistByName(rawValue);
										return !isExist;
									},
									message : "名称已存在!"
								} ,"maxLength[30]"]
							});
							formProxy.addField({
								id : "title",
								key : "title",
								rules : [ {
									rule : function(idOrName, type, rawValue, curData) {
										// 若显示且为空，给予提示
										if ($id("title").is(":visible") && isNoB(rawValue)) {
											return false;
										}
										return true;
									},
									message : "此为必选！"
								} ,"maxLength[30]"]
							});
							formProxy.addField({
								id : "type",
								key : "type",
								required : true
							});
							formProxy.addField({
								id : "validType",
								key : "validType",
								required : true
							});
							formProxy.addField({
								id : "limitAmount",
								key : "limitAmount",
								//required : true,
								rules : ["isNatual"]
							});
							formProxy.addField({
								id : "price",
								key : "price",
								rules : ["maxLength[18]","isMoney"]
							});
							formProxy.addField({
								id : "settlePrice",
								key : "settlePrice",
								rules : ["maxLength[18]","isMoney"]
							});
							formProxy.addField({
								id : "issueStartTime",
								key : "issueStartTime",
								required : true
							});
							formProxy.addField({
								id : "svcId",
								key : "svcId"
							});
							formProxy.addField({
								id : "issueEndTime",
								key : "issueEndTime",
								required : true
							});
							formProxy.addField({
								id : "limitCount",
								key : "limitCount",
								rules : [ {
									rule : function(idOrName, type, rawValue, curData) {
										// 若显示且为空，给予提示
										if ($id("limitCount").is(":visible") && isNoB(rawValue)) {
											return false;
										}
										return true;
									},
									message : "此为必填，无限制填（-1）！"
								}]
							});
							formProxy.addField({
								id : "validDays",
								key : "validDays",
								rules : [ {
									rule : function(idOrName, type, rawValue, curData) {
										// 若显示且为空，给予提示
										if ($id("validDays").is(":visible") && isNoB(rawValue)) {
											if(radioGet("validType")==0){
												return false
											}
											return true;
										}
										return true;
									},
									message : "亲，你选择的有效期是按天数，请填写时间！"
								}]
							});
							formProxy.addField({
								id : "validEndTime",
								key : "validEndTime",
								rules : [ {
									rule : function(idOrName, type, rawValue, curData) {
										// 若显示且为空，给予提示
										if ($id("validEndTime").is(":visible") && isNoB(rawValue)) {
											if(radioGet("validType")==1){
												return false
											}
											return true;
										}
										return true;
									},
									message : "此为必须提供项！"
								}]
							});
							formProxy.addField({
								id : "desc",
								key : "desc",
								rules : ["maxLength[100]"]
							});
							formProxy.addField({
								id : "validStartTime",
								key : "validStartTime",
								rules : [ {
									rule : function(idOrName, type, rawValue, curData) {
										// 若显示且为空，给予提示
										if ($id("validEndTime").is(":visible") && isNoB(rawValue)) {
											if(radioGet("validType")==1){
												return false
											}
											return true;
										}
										return true;
									},
									message : "此为必须提供项！"
								}]
							});
							initDatePicker(formProxy);
							initvalidStartPicker(formProxy);
						},

						/**
						 * 注册查询验证表单控件
						 * @param formProxyQuery
						 */
						formProxyQueryInit : function(formProxyQuery) {
							// 注册表单控件
							formProxyQuery.addField({
								id : "queryName",
								rules : [ "maxLength[30]" ]
							});
						},
						saveOldData : function(data) {
							oldName = data.name;// 保存原来名称，用于检测是否存在
						},
						pageLoad : function(jqGridCtrl,cacheDataGridHelper) {
							$(document).on("click", ".updateDisabled", function() {
								var id=$(this).attr("cellValue");
								var obj=cacheDataGridHelper.getRowData(id);
								var msg="";
								if(obj.disabled==true){
									msg="确定要启用" + obj.name + "优惠券吗?";
								}else{
									msg="确定要禁用" + obj.name + "优惠券吗?";
								}
								disabled_confirm(msg,jqGridCtrl,obj);
							});
							$(document).on("click", ".updateDeleted", function() {
								var id=$(this).attr("cellValue");
								var obj=cacheDataGridHelper.getRowData(id);
								//updateDeletedById(jqGridCtrl,obj);
								if(obj.disabled==true){
									deleted_confirm("确定要删除" + obj.name + "优惠券吗?",jqGridCtrl,obj);
								}else{
									Layer.msgWarning("请先禁用优惠券，禁用后可删除!");
								}
							});
							couponId.click(function() {
								$id("mainPanelRecycle").hide();
								$id("mainPanel").show();
								jqGridCtrl.jqGrid("setGridParam", {url : getAppUrl("/market/svcCoupon/list/get"),page:1}).trigger("reloadGrid");
							});
							//点击服务
							$id("carSvc").click(function() {
								var diaLogInfo={
										"title":"选择服务",
										"id":"carSvcInfo",
										"src":"<%=appBaseUrl%>/market/coupon/bind/svc/jsp"
										};
									diaLog(diaLogInfo);
							});
						}
					});
		});
		function recyceGrid() {
			recycleGridCtrl= $("#recycleGridCtrl").jqGrid({
			      url : getAppUrl("/market/svcCoupon/recycle/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      //postData:{filterStr : JSON.encode(filter,true)},
			      height : "100%",
			      width:"100%",
			      colNames : ["名称", "显示为","类型","关联","发放时间", "有效期", "总数","备注"],
				  colModel : [
							{
								name : "name",
								index : "name",
								width : 100,
								align : 'left'
							},
							{
								name : "title",
								index : "title",
								width : 300,
								align : 'left'
							},
							{
								name : "type",
								index : "type",
								width : 40,
								align : 'left',
									formatter : function(cellValue) {
										var type=null;
										switch (cellValue) {
										case "nopay":
											type="免付券";
											break;
										case "sprice":
											type="特价券";
											break;
										case "deduct":
											type="抵金券";
											break;
										}
										return type;
									}
							},
							{
								name : "svcName",
								index : "svcName",
								width : 50,
								align : 'left'
							},
							{
								name : "issueStartTime",
								index : "issueStartTime",
								width : 70,
								align : 'left'
							},
							 {
								name : "valid",
								index : "valid",
								formatter : function(cellValue, option, rowObject) {
									return getCouponValidValue(rowObject);
								},
								width : 150,
								align : 'center'
							},
							{
								name : "limitCount",
								index : "limitCount",
								width : 50,
								align : 'left'
							},
							{
								name : "desc",
								index : "desc",
								width : 300,
								align : 'left'
							}],
				loadtext: "正在加载....",
				//multiselect:true,
				//multikey:'ctrlKey',
				pager : "#recycleGridPager"
			});
		}
		function adjustCtrlsSize() {
			var jqMainPanel = $id("rootPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "recycleGridCtrl";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("recycleGridPager").height();
			recycleGridCtrl.setGridWidth(mainWidth - 10);
			recycleGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight-10);
		}
	</script>
</body>
</html>