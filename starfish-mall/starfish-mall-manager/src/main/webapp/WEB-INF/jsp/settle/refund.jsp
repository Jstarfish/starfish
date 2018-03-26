<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>退款操作页面</title>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnBachRefundForAlipay" class="button one half wide">支付宝批量退款</button>
					<span class="spacer"></span>
					<button id="btnBachRefundForEcardpay" class="button one half wide">E卡批量退款</button>
					<span class="spacer"></span>
					<span class="spacer"></span>
					<button id="queryUpdateWechatpayRefund" class="button one half wide">微信退款查询</button>
				</div>
				<div class="group right aligned">
					<label class="label">订单号</label> <input id="queryOrderNo" class="input" /> 
					<span class="spacer"></span>
					<label class="label">交易号</label> <input id="queryTradeNo" class="input" /> 
					<span class="spacer"></span>
					<label class="label">支付方式</label>
					<select id="queryPayWay" class="input">
						<option>全部</option>
						<option value="alipay">支付宝</option>
						<option value="wechatPay">微信</option>
						<option value="ecardpay">E卡</option>
						<option value="unionPay">银联</option>
					</select> 
					<span class="spacer"></span>
					<!-- <input id="queryRefundStatus" class="input" type="hidden" value="3"/> -->
					<!-- 起止，结束时间 -->
					<button id="btnToQry" class="button">查询</button>
				</div>
			</div>
		</div>
	
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	<form id="refundAction" action="<%=appBaseUrl%>/settle/alipay/batch/refund" target="_blank" method="post">
		<input id="ids" name="ids" type="hidden"/>
	</form>
	<div id="dialog" style="display: none;">
		<div id="addForm" class="form">
				
				<div class="field row">
					<label class="field label required">订单编号</label> 
					<input type="text" id="no" class="field one half value wide" /> 
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
					<label class="field  label required">退款状态</label> 
					<input type="text" id="refundStatus" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">支付时间</label> 
					<input type="text" id="payTime" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">申请时间</label> 
					<input type="text" id="applyRefundTime" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">审核时间</label> 
					<input type="text" id="auditRefundTime" class="field one half value wide" /> 
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
	<div id="refundConfirmText" style="display: none;">
			<div style="margin: 30px 50px 0; text-align: center; vertical-align: middle;">
				<span >是否申请了退款操作，并收到支付宝申请成功的反馈？</span>
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
		//退款 确认提示框
		function refundConfirm(obj,jqGridCtrl) {
			//
			var yesHandler = function(layerIndex) {
				theLayer.hide();
				//退款时判断支付方式
				if(obj.payWayName == "alipay"){
					
					$id("ids").val(obj.id);
					$id("refundAction").submit();
					
					resultConfirm(obj,jqGridCtrl);
				}else if(obj.payWayName == "wechatpay"){
					refundDoByWechatpay(obj.id,jqGridCtrl);
				}else if(obj.payWayName == "ecardpay"){
					refundDoByEcardpay(obj.id,jqGridCtrl);
				}else if(obj.payWayName == "abcAsUnionpay"){
					refundDoByAbcAsUnionpay(obj.id,jqGridCtrl);
				}else{
					alert("退款出错，请联系技术人员");
				}
				//
			};
			var noHandler = function(layerIndex) {
				theLayer.hide();
			};
			//
			var theLayer = Layer.confirm("您确定要执行退款吗？", yesHandler, noHandler);
		}
		
		//支付宝单个退款——是否去执行退款了   确认提示框
		function resultConfirm(obj,jqGridCtrl) {
			var dom = "#refundConfirmText";
			var theDlg = Layer.dialog({
				dom : dom, //或者 html string
				area : [ '300px', '200px' ],
				closeBtn : true,
				title : "退款操作确认",
				btn : [ '是，已收到', '否，未收到' ],
				yes : function() {
					theDlg.hide();
					//1、更改状态为退款中（如果状态不为已退款的情况）
					refundDoByAlipay(obj.id);
					//2、稍等
					var hintBox = Layer.progress("更新数据中，请稍等...");
					setTimeout(function() {
						hintBox.hide();
						//刷新
						jqGridCtrl.jqGrid("setGridParam", {
							page : 1
						}).trigger("reloadGrid");
					}, 5000);
					
				},
				cancel : function() {
					theDlg.hide();
					//2、稍等
					var hintBox = Layer.progress("更新数据中，请稍等...");
					setTimeout(function() {
						hintBox.hide();
						//刷新
						jqGridCtrl.jqGrid("setGridParam", {
							page : 1
						}).trigger("reloadGrid");
					}, 10000);
				}
			});
		}
		//支付宝批量退款——是否去执行退款了   确认提示框
		function batchResultConfirm(ids,jqGridCtrl) {
			var dom = "#refundConfirmText";
			var theDlg = Layer.dialog({
				dom : dom, //或者 html string
				area : [ '300px', '200px' ],
				closeBtn : true,
				title : "退款操作确认",
				btn : [ '是，已收到', '否，未收到' ],
				yes : function() {
					theDlg.hide();
					//1、更改状态为退款中（如果状态不为已退款的情况）
					refundDosByAlipay(ids);
					
					//2、稍等
					var hintBox = Layer.progress("更新数据中，请稍等...");
					setTimeout(function() {
						hintBox.hide();
						//刷新
						jqGridCtrl.jqGrid("setGridParam", {
							page : 1
						}).trigger("reloadGrid");
					}, 5000);
				},
				cancel : function() {
					theDlg.hide();
					//2、稍等
					var hintBox = Layer.progress("更新数据中，请稍等...");
					setTimeout(function() {
						hintBox.hide();
						//刷新
						jqGridCtrl.jqGrid("setGridParam", {
							page : 1
						}).trigger("reloadGrid");
					}, 10000);
				}
			});
		}
		
		//支付宝——执行 单个  退款 ——
		function refundDoByAlipay(id){
			//if(id){
				//$id("ids").val(id);
				//$id("refundAction").submit();
				
				//更改支付记录表中，退款状态为  refunding  
				var ajax = Ajax.post("/settle/refunding/do");
				ajax.data({
					id : id,
					refundStatus : 2,
					flag : 1,
				});
				ajax.done(function(result, jqXhr) {
					
				});
				ajax.go();
			//}
			//
		}
		//支付宝——执行多个退款——
		function refundDosByAlipay(ids){
				//var hintBox = Layer.progress("正在加载数据...");
				//批量更改支付记录表中，退款状态为  refunding  
				var ajax = Ajax.post("/settle/refunding/do");
				ajax.data({
					ids : ids,
					refundStatus : 2,
					flag : 2
				});
				ajax.done(function(result, jqXhr) {
					
				});
				/* ajax.always(function() {
					hintBox.hide();
				}); */
				ajax.go();
		}
		//E卡——执行 单个  退款 ——
		function refundDoByEcardpay(id,jqGridCtrl){
				
			//执行E卡退款 
			var ajax = Ajax.post("/settle/refund/for/eCard/do");
			ajax.data({
				id : id,
				refundStatus : 0,
				flag : 1
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
		//
		}
		//E卡——执行 批量 退款 ——
		function refundDosByEcardpay(ids,jqGridCtrl){
				
			//执行E卡退款 
			var ajax = Ajax.post("/settle/refund/for/eCard/do");
			ajax.data({
				id : ids,
				refundStatus : 0,
				flag : 2
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
		//
		}
		
		//农行转银联——执行 单个  退款 ——
		function refundDoByAbcAsUnionpay(id,jqGridCtrl){
			//执行单笔退款 
			var ajax = Ajax.post("/settle/abcpay/single/refund");
			ajax.data({
				id : id
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
		//
		}
		//微信——执行 单个  退款 ——
		function refundDoByWechatpay(id,jqGridCtrl){
			//执行单笔退款 
			var ajax = Ajax.post("/settle/wechatpay/single/refund");
			ajax.data({
				id : id
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
					//
					jqGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
				}
			});
			ajax.go();
		//
		}
		
		//微信——执行 退款查询
		function queryUpdateWechatpayRefund(jqGridCtrl){
				var ajax = Ajax.post("/settle/wechatpay/query/refund");
				ajax.data({
					
				});
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						//
						Layer.msgSuccess(result.message);
						jqGridCtrl.jqGrid("setGridParam", {
							page : 1
						}).trigger("reloadGrid");
					} else {
						Layer.warning(result.message);
					}
				});
				ajax.go();
		}
		
		//取消退款 确认提示框
		function cancelRefundConfirm(id,jqGridCtrl) {
			//
			var yesHandler = function(layerIndex) {
				theLayer.hide();
				cancelRefundDo(id,jqGridCtrl);
			};
			var noHandler = function(layerIndex) {
				theLayer.hide();
			};
			//
			var theLayer = Layer.confirm("您确定要执行取消退款吗？", yesHandler, noHandler);
		}
		//取消退款
		function cancelRefundDo(id,jqGridCtrl){
				//更改支付记录表中，退款状态为  4 
				var ajax = Ajax.post("/settle/refund/cancel/do");
				ajax.data({
					id : id,
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
			//
		}
	// -------------------------------------------------------------------------------------}}
	
		$id("mainPanel").jgridDialog(
			{
				dlgTitle : "支付记录列表",
				isUsePageCacheToView : true,
				isShowViewShowModBtn: false,
				jqGridGlobalSetting:{
					url : getAppUrl("/settle/sale/toRefund/list/get"),
					colNames : ["id","订单编号", "支付方式", "支付金额","交易号","退款状态", "申请时间","审核时间","操作","",""],
					colModel : [
							{name:"id",hidden:true},
							{
								name : "no",
								index : "no",
								width : 170,
								align : 'left'
							},
							{
								name : "payWayName",
								index : "payWayName",
								width : 90,
								align : 'center',
								formatter : function(cellValue,option, rowObject) {
										return payWays[cellValue];
								}
							},
							{
								name : "totalFee",
								index : "totalFee",
								width : 90,
								align : 'center'
							},
							{
								name : "tradeNo",
								index : "tradeNo",
								width : 230,
								align : 'center'
							},
							{
								name : "refundStatus",
								index : "refundStatus",
								width : 90,
								align : 'center',
								formatter : function(cellValue,option, rowObject) {
									if (cellValue == 3) {
										return '待退款';
									} else if (cellValue == 1) {
										return '退款失败';
									} else if (cellValue == 2) {
										return '退款中';
									} else{
										return '';
									}
								}
							},
							{
								name : "applyRefundTime",
								index : "applyRefundTime",
								width : 170,
								align : 'center'
							},
							{
								name : "auditRefundTime",
								index : "auditRefundTime",
								width : 170,
								align : 'center'
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									var s = " <span>[<a class='item dlgview' id='dlgview' href='javascript:void(0);' cellValue='" + cellValue + "' >查看</a>]";
									var ss = "&nbsp;&nbsp;&nbsp;[<a class='cancelRefund' href='javascript:void(0);' cellValue='" + cellValue + "'>取消退款</a>]";
									var sss =  "&nbsp;&nbsp;&nbsp;[<a class='refund' href='javascript:void(0);' cellValue='" + cellValue + "' >退款</a>]</span> ";
									//var date =  new Date().format("yyyy-MM-dd");
									if(rowObject.refundStatus == 2){
										return s;
									}
									
									return s + ss +sss;
								},
								width : 200,
								align : "center"
							},
							{
								name : "refundStatus",
								index : "refundStatus",
								hidden:true
							},
							{
								name : "payWayName",
								index : "payWayName",
								hidden:true
							}],
							multiselect : true
				},
				// 修改和查看对话框
				modAndViewDlgConfig : {
					width : Math.min(600, $window.width()),
					height : Math.min(500, $window.height()),
					modal : true,
					open : false
				},
				 modAndViewInit:function (data) {
					 textSet("no", data.no);
					 textSet("userId", data.userId);
					 textSet("totalFee", data.totalFee);
					 textSet("subject", data.subject);
					 textSet("orderDesc", data.orderDesc);
					 textSet("payWayName", payWays[data.payWayName]);
					 textSet("tradeNo", data.tradeNo);
					 textSet("payTime", data.payTime);
					 textSet("applyRefundTime", data.applyRefundTime);
					 textSet("auditRefundTime", data.auditRefundTime);
					 
					 var refundStatus = data.refundStatus;
					 if(refundStatus == 3){
						 refundStatus = "待退款";
					 }else if(refundStatus == 1){
						 refundStatus = "退款失败";
					 }else if(refundStatus == 2){
						 refundStatus = "退款中";
					 }else{
						 refundStatus = "";
					 }
					 textSet("refundStatus", refundStatus);
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
					var payWayName = singleGet("queryPayWay");
					if (payWayName && payWayName != "全部") {
						postData['payWayName'] = payWayName;
					}
					//postData['refundStatus'] = formProxyQuery.getValue("queryRefundStatus");
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
					/* formProxyQuery.addField({
						id : "queryRefundStatus",
					}); */
				},
				pageLoad : function(jqGridCtrl,cacheDataGridHelper) {
					$(document).on("click", ".refund", function() {
						var obj=cacheDataGridHelper.getRowData(parseInt($(this).attr("cellValue")));
						refundConfirm(obj,jqGridCtrl);
					});
					
					$(document).on("click", ".cancelRefund", function() {
						cancelRefundConfirm($(this).attr("cellValue"),jqGridCtrl);
					});
					
					//绑定微信退款查询按钮
					$id("queryUpdateWechatpayRefund").click(function() {
						queryUpdateWechatpayRefund(jqGridCtrl);
					});
					
					//绑定支付宝批量退款按钮
					$id("btnBachRefundForAlipay").click(function() {
						var ids = jqGridCtrl.jqGrid("getGridParam", "selarrrow");
						if (ids == "") {
							Layer.msgWarning("请选中要退款项");
							return;
						}
						
						for (var i = 0; i < ids.length; i++) {
							var rowData = jqGridCtrl.jqGrid("getRowData", ids[i]);//根据上面的id获得本行的所有数据
							var val = rowData.refundStatus; //获得制定列的值 （settleFlag 为colModel的name）
							if (2 == val) {
								Layer.msgWarning("包含已经提交退款的数据，请重新选择");
								return;
							}
							if(rowData.payWayName != "alipay"){
								Layer.msgWarning("包含非支付宝支付的数据，请重新选择");
								return;
							}
						}
						
						var theLayer = Layer.confirm('确定要执行支付宝批量退款吗？', function() {
								$id("ids").val(ids);
								$id("refundAction").submit();
								theLayer.hide();
								
								//refundDosByAlipay(ids);
								
								batchResultConfirm(ids,jqGridCtrl);
						});
						
						
					});
					
					//绑定E卡批量退款按钮
					$id("btnBachRefundForEcardpay").click(function() {
						var ids = jqGridCtrl.jqGrid("getGridParam", "selarrrow");
						if (ids == "") {
							Layer.msgWarning("请选中要退款项");
							return;
						}
						
						for (var i = 0; i < ids.length; i++) {
							var rowData = jqGridCtrl.jqGrid("getRowData", ids[i]);//根据上面的id获得本行的所有数据
							/* var val = rowData.refundStatus; //获得制定列的值 （settleFlag 为colModel的name）
							if (2 == val) {
								Layer.msgWarning("包含已经提交退款的数据，请重新选择");
								return;
							} */
							if(rowData.payWayName != "ecardpay"){
								Layer.msgWarning("包含非E卡支付的数据，请重新选择");
								return;
							}
						}
						
						var theLayer = Layer.confirm('确定要执行E卡批量退款吗？', function() {
							refundDosByEcardpay(ids,jqGridCtrl);
						});
						
					});
				}
			});
	//-----------------------------------------------------------------------------------}}
	</script>
</body>
</html>