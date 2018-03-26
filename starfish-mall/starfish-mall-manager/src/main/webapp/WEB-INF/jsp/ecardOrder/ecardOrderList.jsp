<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>E卡订单列表</title>
</head>

<body id="rootPanel">
	
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group right aligned">
					<label class="label">订单号</label> <input id="queryOrderNo" class="input" /> 
					<span class="spacer"></span>
					<label class="label">店铺名称</label> <input id="queryShopName" class="input" /> 
					<span class="spacer"></span> 
					<label class="label">用户名</label> <input id="queryCustomerName" class="input" /> 
					<span class="spacer"></span> 
					<!-- 
					<label class="label">订单状态</label> 
					<select id="queryOrderState" class="input">
					<option>-全部-</option>
					<option value="unhandled">未付款</option>
					<option value="processing">未享用</option>
					<option value="cancelled">已取消</option>
					<option value="finished">已完成</option>
					</select>  
					-->
					<span class="spacer"></span> 
					<label class="label">下单日期</label> 
					<input class="input" id="fromDate" /> 至 <input class="input" id="toDate" />
					<span class="spacer two wide"></span>
					
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
					<label class="field inline label one half wide">店铺名称</label> 
					<input type="text" id="shopName" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field label required">用户名称</label> 
					<input type="text" id="userName" class="field one half value wide" /> 
					<label class="field inline label one half wide">联系电话</label> 
					<input type="text" id="phoneNo" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field label required">E卡名称</label> 
					<input type="text" id="cardName" class="field one half value wide" /> 
					<label class="field inline label one half wide">E卡面额</label> 
					<input type="text" id="faceValue" class="field one half value wide" />
				</div>
				<div class="field row">
					<label class="field  label required">E卡单价</label> 
					<input type="text" id="price" class="field one half value wide" /> 
					<label class="field inline label one half wide">购卡数量</label> 
					<input type="text" id="quantity" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">支付渠道</label> 
					<input type="text" id="payWay" class="field one half value wide" /> 
					<label class="field inline label one half wide">支付金额</label> 
					<input type="text" id="amount" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">支付状态</label> 
					<input type="text" id="paid" class="field one half value wide" />
					<label class="field inline label one half wide">支付时间</label> 
					<input type="text" id="payTime" class="field one half value wide" />  
				</div>
				
				<!-- <div class="field row" style="height: 60px">
					<label class="field  label required">商品名称</label> 
					<textarea id="subject" class="field three value wide " style="height: 50px;"></textarea>
				</div>
				<div class="field row">
					<label class="field  label required">订单描述</label> 
					<textarea id="orderDesc" class="field three value wide " style="height: 50px;"></textarea>
				</div> -->
				
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
		
		// 初始化日期控件
		$id("fromDate").datetimepicker({
			lang : 'ch',
			format : 'Y-m-d'
		});
		$id("toDate").datetimepicker({
			lang : 'ch',
			format : 'Y-m-d'
		});
		var toDate = new Date();
		var fromDate = new Date();
		fromDate.setDate(toDate.getDate() - 11);//查询10天的
		dateSet("toDate", toDate);
		dateSet("fromDate", fromDate);
	
	// -------------------------------------------------------------------------------------}}
	
	$id("mainPanel").jgridDialog(
			{
				dlgTitle : "E卡订单列表",
				isUsePageCacheToView : true,
				isShowViewShowModBtn: false,
				jqGridGlobalSetting:{
					url : getAppUrl("/ecardOrder/list/get/-mall"),
					postData : {
						filterStr : JSON.encode({
							fromDate : dateGet("fromDate"),
							toDate : dateGet("toDate")
						}, true)
					},
					colNames : ["id","订单号", "支付方式", "支付金额","用户名称","支付状态", "支付时间","操作"],
					colModel : [
							{name:"id",hidden:true},
							{
								name : "no",
								index : "no",
								width : 200,
								align : 'left'
							},
							{
								name : "payWay",
								index : "payWay",
								width : 100,
								align : 'center',
								formatter : function(cellValue,option, rowObject) {
										return payWays[cellValue];
								}
							},
							{
								name : "amount",
								index : "amount",
								width : 100,
								align : 'center'
							},
							{
								name : "userName",
								index : "userName",
								width : 250,
								align : 'center'
							},
							{
								name : "paid",
								index : "paid",
								width : 100,
								align : 'center',
								formatter : function(cellValue,option, rowObject) {
									if (cellValue == true) {
										return '已支付';
									} else{
										return '未支付';
									}
								}
							},
							{
								name : "payTime",
								index : "payTime",
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
					width : Math.min(650, $window.width()),
					height : Math.min(350, $window.height()),
					modal : true,
					open : false
				},
				 modAndViewInit:function (data) {
					 textSet("no", data.no);
					 textSet("userName", data.userName);
					 textSet("cardName", data.cardName);
					 if(!isNull(data.shopName)){
						 textSet("shopName", data.shopName);
					 }else{
						 textSet("shopName", "--无--");
					 }
					 textSet("phoneNo", data.phoneNo);
					 textSet("faceValue", data.faceValue);
					 textSet("quantity", data.quantity);
					 textSet("price", data.price);
					 textSet("amount", data.amount);
					 
					 textSet("payTime", data.payTime);
					 textSet("payWay", payWays[data.payWay]);
					 
					 var paid = data.paid;
					 if(paid == true){
						 paid = "已支付";
					 }else{
						 paid = "未支付";
					 }
					 textSet("paid", paid);
					
				 },
				queryParam : function(postData,formProxyQuery) {
					var no = formProxyQuery.getValue("queryOrderNo");
					if (no != "") {
						postData['no'] = no;
					}
					var shopName = formProxyQuery.getValue("queryShopName");
					if (shopName != "") {
						postData['shopName'] = shopName;
					}
					var userName = formProxyQuery.getValue("queryCustomerName");
					if (userName != "") {
						postData['userName'] = userName;
					}
					var fromDate = dateGet("fromDate");
					if (fromDate && fromDate != "") {
						postData['fromDate'] = fromDate;
					}
					var toDate = dateGet("toDate");
					if (toDate && toDate != "") {
						postData['toDate'] = toDate;
					}
				},
				formProxyQueryInit : function(formProxyQuery) {
					// 注册查询表单控件
					formProxyQuery.addField({
						id : "queryOrderNo",
						rules : [ "maxLength[20]" ]
					});
					formProxyQuery.addField({
						id : "queryShopName",
						rules : [ "maxLength[50]" ]
					});
					formProxyQuery.addField({
						id : "queryCustomerName",
						rules : [ "maxLength[30]" ]
					});
					formProxyQuery.addField({
						id : "fromDate",
						type : "date",
						rules : [ "isDate", "rangeDate[2015-01-01,2115-01-01]" ]
					});
					formProxyQuery.addField({
						id : "toDate",
						type : "date",
						rules : [ "isDate", "rangeDate[2015-01-01,2115-01-01]" ]
					});
				},
				pageLoad : function() {
					/* $(document).on("click", ".applyRefund", function() {
						toRefundConfirm($(this).attr("cellValue"));
					}); */
				}
			});
	//-----------------------------------------------------------------------------------}}
	
	</script>
</body>
</html>