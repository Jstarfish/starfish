<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>退款记录</title>
</head>

<body id="rootPanel">
	
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group right aligned">
					<label class="label">订单号</label> <input id="queryOrderNo" class="input" /> 
					<span class="spacer"></span>
					<label class="label">支付交易号</label> <input id="queryTradeNo" class="input" /> 
					<input id="queryRefundStatus" class="input" type="hidden" value="0"/> 
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
					<label class="field label required one half wide">订单号</label> 
					<input type="text" id="no" class="field one half value wide" /> 
					<span class="normal spacer"></span>
				</div>
				<div class="field row">
					<label class="field label required one half wide">用户ID</label> 
					<input type="text" id="userId" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field label required one half wide">退款金额</label> 
					<input type="text" id="totalFee" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field label required one half wide">支付方式</label> 
					<input type="text" id="payWayName" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field label required one half wide">退款状态</label> 
					<input type="text" id="refundStatus" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field label required one half wide">支付时间</label> 
					<input type="text" id="payTime" class="field one half value wide" />
				</div>
				<div class="field row">
					<label class="field label required one half wide">退款时间</label> 
					<input type="text" id="refundTime" class="field one half value wide" />
				</div>
				<div class="field row">
					<label class="field label required one half wide">支付交易号</label> 
					<input type="text" id="tradeNo" class="field two value wide" /> 
				</div>
				<div class="field row">
					<label class="field label required one half wide">退款批次号</label> 
					<input type="text" id="batchNo" class="field two value wide" /> 
				</div>
				
				<div id="wxabout" style="display: none;">
					<div class="field row">
						<label class="field label required one half wide">退款渠道</label> 
						<input type="text" id="refundChannel" class="field two value wide" /> 
					</div>
					<div class="field row">
						<label class="field label required one half wide">退入账户</label> 
						<input type="text" id="refundReceiveAccount" class="field two value wide" /> 
					</div>
					<div class="field row">
						<label class="field label required one half wide">商户退款号</label> 
						<input type="text" id="refundOrderNo" class="field two value wide" /> 
					</div>
					<div class="field row">
						<label class="field label required one half wide">微信退款号</label> 
						<input type="text" id="refundNo" class="field two value wide" /> 
					</div>
				</div>
				
				<div id="abcabout" style="display: none;">
					<div class="field row">
						<label class="field label required one half wide">银联退款号</label> 
						<input type="text" id="noForAbc" class="field two value wide" /> 
					</div>
					<div class="field row">
						<label class="field label required one half wide">商户退款号</label> 
						<input type="text" id="refundOrderNo" class="field two value wide" /> 
					</div>
					<div class="field row">
						<label class="field label required one half wide">退款凭证号</label> 
						<input type="text" id="voucherNo" class="field two value wide" /> 
					</div>
					<div class="field row">
						<label class="field label required one half wide">退款流水号</label> 
						<input type="text" id="iRspRef" class="field two value wide" /> 
					</div>
				</div>
				
				<div class="field row" style="height: 60px">
					<label class="field label required one half wide">商品名称</label> 
					<textarea id="subject" class="field three value wide " style="height: 50px;"></textarea>
				</div>
				<div class="field row">
					<label class="field label required one half wide">订单描述</label> 
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
		
	// -------------------------------------------------------------------------------------}}
	
	$id("mainPanel").jgridDialog(
			{
				dlgTitle : "退款记录列表",
				isUsePageCacheToView : true,
				isShowViewShowModBtn: false,
				jqGridGlobalSetting:{
					url : getAppUrl("/settle/sale/refundRec/list/get"),
					postData : {
						filterStr : JSON.encode({
							refundStatus : 0
						}, true)
					},
					colNames : ["id","订单号", "支付方式", "退款金额","退款批次号","退款状态", "退款时间","操作"],
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
								name : "batchNo",
								index : "batchNo",
								width : 250,
								align : 'center'
							},
							{
								name : "refundStatus",
								index : "refundStatus",
								width : 100,
								align : 'center',
								formatter : function(cellValue,option, rowObject) {
									if(cellValue == 0){
										return '成功';
									}
									return '转入代发';
								}
							},
							{
								name : "refundTime",
								index : "refundTime",
								width : 200,
								align : 'center'
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									var s = " <span>[<a class='item dlgview' id='dlgview' href='javascript:void(0);' cellValue='" + cellValue + "' >查看</a>]"
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
				 modAndViewInit:function (data) {
					 $id("wxabout").hide();
					 $id("abcabout").hide();
					 
					 textSet("no", data.no);
					 textSet("userId", data.userId);
					 textSet("totalFee", data.totalFee);
					 textSet("subject", data.subject);
					 textSet("orderDesc", data.orderDesc);
					 textSet("payWayName", payWays[data.payWayName]);
					 textSet("tradeNo", data.tradeNo);
					 textSet("payTime", data.payTime);
					 textSet("batchNo", data.batchNo);
					 textSet("refundTime", data.refundTime);
					 
					 refundStatus = data.refundStatus;
					 if(refundStatus == 0){
						 refundStatus = "成功";
					 }else{
						 refundStatus = "转入代发";
					 }
					 textSet("refundStatus", refundStatus);
					 
					 if(data.payWayName == "wechatpay"){
						 $id("wxabout").show();
						 textSet("refundChannel", data.refundChannel);
						 textSet("refundReceiveAccount", data.refundReceiveAccount);
						 textSet("refundOrderNo", data.refundOrderNo);
						 textSet("refundNo", data.refundNo);
					 }
					 
					 if(data.payWayName == "abcAsUnionpay"){
						 $id("abcabout").show();
						 textSet("noForAbc", data.noForAbc);
						 textSet("refundOrderNo", data.refundOrderNo);
						 textSet("voucherNo", data.voucherNo);
						 textSet("iRspRef", data.iRspRef);
					 }
					 
					 
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