<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>退款审核</title>
</head>

<body id="rootPanel">
	
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group right aligned">
					<label class="label">订单号</label> <input id="queryOrderNo" class="input" /> 
					<span class="spacer"></span>
					<label class="label">交易号</label> <input id="queryTradeNo" class="input" /> 
					<input id="queryRefundStatus" class="input" type="hidden" value="6"/> 
					<span class="spacer"></span>
					<!-- 起止，结束时间 -->
					<button id="btnToQry" class="button">查询</button>
				</div>
			</div>
		</div>
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	
	<div id="dialog" style="display: none;">
		<div id="addForm" class="form">
				
				<div class="field row">
					<label class="field label required">订单号</label> 
					<input type="text" id="no" class="field one half value wide" /> 
					<span class="normal spacer"></span>
				</div>
				<div class="field row">
					<label class="field label required">用户ID</label> 
					<input type="text" id="userId" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">支付金额</label> 
					<input type="text" id="totalFee" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">支付方式</label> 
					<input type="text" id="payWayName" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">支付状态</label> 
					<input type="text" id="tradeStatus" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">申请时间</label> 
					<input type="text" id="applyRefundTime" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">交易号</label> 
					<input type="text" id="tradeNo" class="field two value wide" /> 
				</div>
				<div class="field row" style="height: 60px">
					<label class="field  label required">商品名称</label> 
					<textarea id="subject" class="field three value wide " style="height: 50px;"></textarea>
				</div>
				<div class="field row">
					<label class="field  label required">订单描述</label> 
					<textarea id="orderDesc" class="field three value wide " style="height: 50px;"></textarea>
				</div>
				
			</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js"></script>
	<script>
	//-------------------------------------------自定义方法------------------------------{{
		// 支付方式字典
		var payWays = {
			"alipay" : "支付宝",
			"wechatpay" : "微信",
			"pos" : "POS刷卡",
			"unionpay" : "银联在线",
			"abcAsUnionpay" : "农行转银联",
			"ecardpay" : "E卡",
			"coupon" : "优惠券"
		};
		//审核通过 确认提示框
		function toRefundConfirm(rowData,jqGridCtrl) {
			//
			var yesHandler = function(layerIndex) {
				theLayer.hide();
				toRefund(rowData,jqGridCtrl);
			};
			var noHandler = function(layerIndex) {
				theLayer.hide();
			};
			//
			var theLayer = Layer.confirm("您确定要审核通过吗？", yesHandler, noHandler);
		}
		//更改支付记录为 可退款 状态
		function toRefund(rowData,jqGridCtrl){
			var ajax = Ajax.post("/settle/refund/audit/do");
			ajax.data({
				id : rowData.id,
				refundStatus : 3
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					//
					jqGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
				} else {
					Layer.warning(result.message);
				}
				
			});
			ajax.go();
		}
		
		//审核不通过 确认提示框
		function refuseRefundConfirm(rowData,jqGridCtrl) {
			//
			var yesHandler = function(layerIndex) {
				theLayer.hide();
				refuseRefund(rowData,jqGridCtrl);
			};
			var noHandler = function(layerIndex) {
				theLayer.hide();
			};
			//
			var theLayer = Layer.confirm("确定选择不通过吗？", yesHandler, noHandler);
		}
		//更改支付记录为 拒绝退款 状态
		function refuseRefund(rowData,jqGridCtrl){
			var ajax = Ajax.post("/settle/refund/audit/do");
			ajax.data({
				id : rowData.id,
				refundStatus : 4
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					//
					jqGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
				} else {
					Layer.warning(result.message);
				}
				
			});
			ajax.go();
		}
	
	// -------------------------------------------------------------------------------------}}
	
	$id("mainPanel").jgridDialog(
			{
				dlgTitle : "支付记录列表",
				isUsePageCacheToView : true,
				isShowViewShowModBtn: false,
				jqGridGlobalSetting:{
					url : getAppUrl("/settle/sale/refund/audit/list/get"),
					postData : {
						filterStr : JSON.encode({
							refundStatus : 6
						}, true)
					},
					colNames : ["id","订单号", "支付方式", "支付金额","交易号","支付状态", "申请时间","操作"],
					colModel : [
							{name:"id",hidden:true},
							{
								name : "no",
								index : "no",
								width : 200,
								align : 'left'
							},
							{
								name : "payWayName",
								index : "payWayName",
								width : 100,
								align : 'center',
								formatter : function(cellValue,option, rowObject) {
										return payWays[cellValue];
								}
							},
							{
								name : "totalFee",
								index : "totalFee",
								width : 100,
								align : 'center'
							},
							{
								name : "tradeNo",
								index : "tradeNo",
								width : 250,
								align : 'center'
							},
							{
								name : "tradeStatus",
								index : "tradeStatus",
								width : 100,
								align : 'center',
								formatter : function(cellValue,option, rowObject) {
									if (cellValue == "TRADE_SUCCESS") {
										return '成功';
									} else if (cellValue == "TRADE_FINISHED") {
										return '支付完成';
									} else{
										return '';
									}
								}
							},
							{
								name : "applyRefundTime",
								index : "applyRefundTime",
								width : 200,
								align : 'center'
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									//var s = " <span>[<a class='item dlgupd' id='dlgupd' href='javascript:void(0);' cellValue='" + cellValue + "' >审核</a>]"
									var s = " <span>[<a class='item dlgview' id='dlgview' href='javascript:void(0);' cellValue='" + cellValue + "' >审核</a>]"
								//	+ "&nbsp;&nbsp;&nbsp;[<a class='applyRefund' name='applyRefund"+cellValue+"' href='javascript:void(0);' cellValue='" + cellValue + "'>申请退款</a>]</span> ";
									return s;
								},
								width : 200,
								align : "center"
							} ]
				},
				// 修改和查看对话框
				modAndViewDlgConfig : {
					width : Math.min(600, $window.width()),
					height : Math.min(500, $window.height()),
					modal : true,
					open : false
				},
				settingViewBtn:function(buttons,rowData,jqGridCtrl){
						buttons["审核通过"]=function() {
							toRefundConfirm(rowData,jqGridCtrl);
							$(this).dialog("close");
						};
						buttons["审核不通过"]=function() {
							refuseRefundConfirm(rowData,jqGridCtrl);
							$(this).dialog("close");
						};
				},
				 modAndViewInit:function (data) {
					 textSet("no", data.no);
					 textSet("userId", data.userId);
					 textSet("totalFee", data.totalFee);
					 textSet("subject", data.subject);
					 textSet("orderDesc", data.orderDesc);
					 textSet("payWayName", payWays[data.payWayName]);
					 textSet("tradeNo", data.tradeNo);
					 textSet("applyRefundTime", data.applyRefundTime);

					 var tradeStatus = data.tradeStatus;
					 if(tradeStatus == "TRADE_SUCCESS"){
						 tradeStatus = "成功";
					 }else if(tradeStatus == "TRADE_FINISHED"){
						 tradeStatus = "支付完成";
					 }else{
						 tradeStatus = "";
					 }
					 textSet("tradeStatus", tradeStatus);
					 
					 
				 },
				queryParam : function(postData,formProxyQuery) {
					var no = formProxyQuery.getValue("queryOrderNo");
					if (no != "") {
						postData['no'] = no;
					}
					var tradeNo = formProxyQuery.getValue("queryTradeNo");
					if (tradeNo != "") {
						postData['tradeNo'] = tradeNo;
					}
					postData['refundStatus'] = formProxyQuery.getValue("queryRefundStatus");
				},
				formProxyQueryInit : function(formProxyQuery) {
					// 注册查询表单控件
					formProxyQuery.addField({
						id : "queryOrderNo",
						rules : [ "maxLength[20]" ]
					});
					formProxyQuery.addField({
						id : "queryTradeNo",
						rules : [ "maxLength[64]" ]
					});
					formProxyQuery.addField({
						id : "queryRefundStatus",
					});
				},
				pageLoad : function(jqGridCtrl) {
					$(document).on("click", ".applyRefund", function() {
						toRefundConfirm($(this).attr("cellValue"),jqGridCtrl);
					});
				}
			});
	//-----------------------------------------------------------------------------------}}
	
	</script>
</body>
</html>