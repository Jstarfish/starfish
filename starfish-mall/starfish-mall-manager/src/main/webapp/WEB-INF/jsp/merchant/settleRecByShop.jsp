<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>结算记录</title>
</head>

<body id="rootPanel">
	
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group right aligned">
					<!-- <label class="label">结算状态</label>
					<select id="queryState" class="input">
						<option>全部</option>
						<option value="3">结算成功</option>
						<option value="4">结算失败</option>
					</select> -->
					<label class="label">合作店名称</label> 
					<input class="input" id="queryDistShopName" />
					<span class="spacer"></span>
					<label class="label">结算日期</label> 
					<input class="input" id="fromDate" /> 至 <input class="input" id="toDate" /> 
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
					<label class="field label required one half wide">合作店ID</label> 
					<input type="text" id="distributorId" class="field two value wide" /> 
				</div>
				<div class="field row">
					<label class="field label required one half wide">合作店名称</label> 
					<input type="text" id="distShopName" class="field two value wide" /> 
				</div>
				<div class="field row">
					<label class="field label required one half wide">联系人</label> 
					<input type="text" id="distributorName" class="field two value wide" /> 
				</div>
				<div class="field row">
					<label class="field label required one half wide">应结金额</label> 
					<input type="text" id="amount" class="field two value wide" /> 
				</div>
				<div class="field row">
					<label class="field label required one half wide">实结金额</label> 
					<input type="text" id="theAmount" class="field two value wide" /> 
				</div>
				<div class="field row">
					<label class="field label required one half wide">结算日期</label> 
					<input type="text" id="theTime" class="field two value wide" /> 
				</div>
				<div class="field row">
					<label class="field label required one half wide">描述</label> 
					<textarea id="desc" class="field value two wide" style="height: 100px;"></textarea>
				</div>
			</div>
	</div>
	
	<div id="orderDialog" style="display: none;">
		<div id="viewForm" class="form">
			<div class="field row">
				<label class="field label">订单编号</label> 
				<input type="text" id="no" class="field value one half wide" /> 
				<label class="field inline label one half wide">用户名称</label> 
				<input type="text" id="userName" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">联系人</label> 
				<input type="text" id="linkMan" class="field value one half wide" /> 
				<label class="field inline label one half wide">联系电话</label> 
				<input type="text" id="phoneNo" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">预约时间</label> 
				<input type="text" id="planTime" class="field value one half wide" /> 
				<label class="field inline label one half wide">确认码</label> 
				<input type="text" id="doneCode" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">车辆名称</label> 
				<input type="text" id="carName" class="field value one half wide" /> 
				<label class="field inline label one half wide">车型名称</label> 
				<input type="text" id="carModel" class="field value one half wide" />
			</div>
			
			<br />
			<span id="divider" class="normal hr divider"></span>
			
			<div class="field row">
				<label class="field label">商家名称</label> 
				<input type="text" id="merchantName" class="field value one half wide" /> 
				<label class="field inline label one half wide">店铺名称</label> 
				<input type="text" id="shopName" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">销售金额</label> 
				<input type="text" id="saleAmount" class="field value one half wide" /> 
				<label class="field inline label one half wide">折扣金额</label> 
				<input type="text" id="discAmount" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">内部金额</label> 
				<input type="text" id="amountInner" class="field value one half wide" /> 
				<label class="field inline label one half wide">外部金额</label> 
				<input type="text" id="amountOuter" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">支付渠道</label> 
				<input type="text" id="payWay" class="field value one half wide" /> 
				<label class="field inline label one half wide">支付状态</label> 
				<input type="text" id="payState" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label">结算金额</label> 
				<input type="text" id="settleAmount1" class="field value one half wide" />
			</div>
		</div>
	</div>
	
	
	<!--订单详情 -->
	<div id="OrderDeatil" class="form" style="display: none;height: 600px;">
		
		<div class="ui-layout-center">
			<div class="filter section">
				<div class="filter row">
					<div class="group right aligned">
						<label class="label">订单号</label> 
						<input id="queryOrderNo" class="normal input" /> 
						<input id="queryRecId" type="hidden" /> 
						<span class="spacer"></span> 
						<button id="btnQueryOrderDeatil" class="normal button">查询</button>
					</div>
				</div>
			</div>
			<table id="orderGrid"></table>
			<div id="orderPager"></div>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js"></script>
	<script>
	//-------------------------------------------自定义方法------------------------------{{
		// 支付状态字典
		var payStates = {
			"unpaid" : "未支付",
			"paid" : "已支付",
			"refundApplied" : "已申请退款",
			"refunded" : "已退款"
		};
		
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
		
		var OrderDeatil = null;
		var orderDeatilGridCtrl=null;
		//查看订单信息窗口
		var showOrderDialog;
		
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
		
		
		//初始化订单详情dialog
		function initOrderDeatil(){
			OrderDeatil = $( "#OrderDeatil" ).dialog({
		        autoOpen: false,
		        width : Math.min(800, $window.width()),
		        height : Math.min(480, $window.height()),
		        modal: true,
		        title : '订单详情',
		        buttons: {
		            "关闭": function() {
		            	OrderDeatil.dialog( "close" );
		          }
		        },
		        close: function() {
		        	OrderDeatil.dialog( "close" );
		        }
		      });
		}
		
		//打开订单详情对话框
		function openOrderDeatilDialog(obj){
			OrderDeatil.dialog("open");
			//
			loadOrderDeatil(obj);
		}
		
		//
		function loadOrderDeatil(obj){
			$id("queryRecId").val(obj.id);
			var postData = {
					settleRecId2Dist : obj.id
			};
			//加载本账期，订单详情
			if(orderDeatilGridCtrl==null){
				orderDeatilGridCtrl = $("#orderGrid").jqGrid({
				      url : getAppUrl("/settle/dist/rec/orders/deatil/get"),  
				      contentType : 'application/json',  
				      mtype : "post",  
				      datatype : 'json',
				      postData:{filterStr : JSON.encode(postData,true)},
				      height : 240,
					  width : "100%",
				      colNames : ["id", "订单编号", "用户名称","联系人","商户名称","操作"],  
				      colModel : [{name:"id", index:"id", width:150,align : 'center', hidden : true},
				                  {name:"no", index:"no",width:100,align : 'center',},
				                  {name:"userName", index:"userName",width:140,align : 'center'},
				                  {name:"linkMan", index:"linkMan",width:100,align : 'center',},
				                  {name:"merchantName", index:"merchantName",width:140,align : 'center'},
				                  {
				                   name:"id", 
				                   index:"id", 
				                   width : 100, 
				                   align : "center",
				                   formatter : function(cellValue, option, rowObject) {
										var s = " <span>[<a class='item dlgview1' id='dlgview1' href='javascript:void(0);' onclick='openShowDlg(" + JSON.stringify(rowObject)
										+ ")' >查看</a>]"
										return s;
									}
				                  } 
								],  
					loadtext: "Loading....",
					pager : "#orderPager"
				});
			}else{
				orderDeatilGridCtrl.jqGrid("setGridParam", {
					postData : {
						filterStr : JSON.encode(postData, true)
					},
					page : 1
				}).trigger("reloadGrid");
			}
			
		}
		
		//查看订单详情
		function openShowDlg(obj){
			initshowOrderDialog(obj);
			showOrderDialog.dialog("open");
		}
		//初始化查看订单详情
		function initshowOrderDialog(obj) {
			//
			$("#orderDialog  input").attr("disabled", "disabled");
			
			$id("no").val(obj.no);
			$id("id").val(obj.id);
			$id("userName").val(obj.userName);
			$id("linkMan").val(obj.linkMan);
			$id("phoneNo").val(obj.phoneNo);
			$id("planTime").val(obj.planTime);
			$id("doneCode").val(obj.doneCode);
			$id("carName").val(obj.carName);
			$id("carModel").val(obj.carModel);
			$id("merchantName").val(obj.merchantName);
			$id("shopName").val(obj.shopName);
			$id("saleAmount").val(obj.saleAmount);
			$id("discAmount").val(obj.discAmount);
			$id("amountInner").val(obj.amountInner);
			$id("amountOuter").val(obj.amountOuter);
			$id("amount1").val(obj.amount);
			$id("settleAmount1").val(obj.settleAmount);
			
			$id("payWay").val(payWays[obj.payWay]);
			$id("payState").val(payStates[obj.payState]);
			
			showOrderDialog = $("#orderDialog").dialog({
				autoOpen : false,
				width : Math.min(700, $window.width()),
				height : Math.min(650, $window.height()),
				modal : true,
				title : '查看订单详情',
				buttons : {
					"关闭" : function() {
						showOrderDialog.dialog("close");
					}
				},
				close : function() {
				}
			});
		}
		
		
	// -------------------------------------------------------------------------------------}}
	
		$id("mainPanel").jgridDialog(
			{
				dlgTitle : "结算记录",
				isUsePageCacheToView : true,
				isShowViewShowModBtn: false,
				jqGridGlobalSetting:{
					url : getAppUrl("/settle/rec/by/shop/get/-shop"),
					postData : {
						filterStr : JSON.encode({
							fromDate : dateGet("fromDate"),
							toDate : dateGet("toDate")
						}, true)
					},
					colNames : ["结算单编号", "合作店名称", "合作店联系人", "备注", "结算日期", "应结金额", "实结金额", "操作"],
					colModel : [
							{
								name:"id",
								index : "id",
								width : 80,
								align : 'left'
							},
							{
								name : "distShopName",
								index : "distShopName",
								width : 100,
								align : 'center'
							},
							{
								name : "distributorName",
								index : "distributorName",
								width : 140,
								align : 'center'
							},
							{
								name : "desc",
								index : "desc",
								width : 200,
								align : 'left'
							},
							{
								name : "theTime",
								index : "theTime",
								width : 140,
								align : 'center'
							},
							{
								name : "amount",
								index : "amount",
								width : 80,
								align : 'right',
								formatter : function(cellValue, option, rowObject) {
									return "<span> "+ "￥ " + cellValue +"</span>";
									
								}
							},
							{
								name : "theAmount",
								index : "theAmount",
								width : 80,
								align : 'right',
								formatter : function(cellValue, option, rowObject) {
									return "<span style='color:red;'> "+ "￥ " + cellValue +"</span>";
									
								}
							},
							{
								name : 'id',
								index : 'id',
								width : 200,
								align : "center",
								formatter : function(cellValue, option, rowObject) {
									var s = " <span>[<a class='item dlgview' id='dlgview' href='javascript:void(0);' cellValue='" + cellValue + "' >查看</a>]"
									var ss = "&nbsp;&nbsp;&nbsp;[<a href='javascript:void(0);' onclick='openOrderDeatilDialog("+JSON.stringify(rowObject)+")' >关联订单</a>]</span> "
									return s + ss;
								}
								
							}
							]
							//multiselect : true
				},
				// 修改和查看对话框
				modAndViewDlgConfig : {
					width : Math.min(500, $window.width()),
					height : Math.min(520, $window.height()),
					modal : true,
					open : false
				},
				 modAndViewInit:function (data) {
					 textSet("distributorId", data.distributorId);
					 textSet("distributorName", data.distributorName);
					 textSet("amount", data.amount);
					 textSet("theAmount", data.theAmount);
					 textSet("distShopName", data.distShopName);
					 textSet("desc", data.desc);
					 textSet("theTime", data.theTime);
					 
				 },
				queryParam : function(postData,formProxyQuery) {
					var distShopName = formProxyQuery.getValue("queryDistShopName");
					if (distShopName != "") {
						postData['distShopName'] = distShopName;
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
					formProxyQuery.addField({
						id : "queryDistShopName",
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
				pageLoad : function(jqGridCtrl) {
					
					//初始化 订单详情对话框
					initOrderDeatil();
					
					//条件查询订单详情
					$id("btnQueryOrderDeatil").click(function() {
						
						var filter = {};
						var no = textGet("queryOrderNo");
						if(no){
							filter.no = no;
						}
						var settleRecId2Dist = textGet("queryRecId");
						if(settleRecId2Dist){
							filter.settleRecId2Dist = settleRecId2Dist;
						}
						//加载orderDeatilGridCtrl
						orderDeatilGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
					});
				}
			});
	//-----------------------------------------------------------------------------------}}
	</script>
</body>
</html>