<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>结算</title>
</head>

<body id="rootPanel">
	
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					今日是：<label id="today" style="color: green;"></label>
					<span class="spacer"></span>
					<button id="createSettleInfo" class="button">生成清算单</button>
					<span class="spacer"></span>
					<button id="updateSettleInfo" class="button">更新清算单</button>
					<span class="spacer"></span>
					<button id="submitSettleInfo" class="button">生成结算单</button>
				</div>
				<div class="group right aligned">
					<label class="label">商户名称</label> <input id="queryMerchantName" class="input" /> 
					<label class="label">状态</label>
					<select id="querySettleFlag" class="input" >
						<option>-全部-</option>
						<option value="6">可生成结算单</option>
						<option value="7">未到结算日</option>
						<option value="8">无结算账户</option>
						<!-- <option value="9">未出清算单</option> -->
					</select> 
					<span class="spacer"></span>
					<!-- 起止，结束时间 -->
					<button id="btnToQry" class="button">查询</button>
				</div>
			</div>
		</div>
		
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	<form id="settleAction" action="<%=appBaseUrl%>/settle/alipay/batch/settle" target="_self" method="post">
		<input id="ids" name="ids" type="hidden"/>
	</form>
	<div id="dialog" style="display: none;">
		<div id="addForm" class="form">
				
				<div class="field row">
					<label class="field label required">商户名称</label> 
					<input type="text" id="merchantNameF" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field label required">结算方式</label> 
					<input type="text" id="settleWay" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">账户名</label> 
					<input type="text" id="acctName" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">结算账户</label> 
					<input type="text" id="acctNo" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">结算金额</label> 
					<input type="text" id="settleAmount" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">结算周期</label> 
					<input type="text" id="settleX" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">结算状态</label> 
					<input type="text" id="settleFlag" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">结算日期</label> 
					<input type="text" id="settleDay" class="field one half value wide" /> 
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
				<label class="field label">实际金额</label> 
				<input type="text" id="amount" class="field value one half wide" /> 
				<label class="field inline label one half wide">结算金额</label> 
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
						<label class="label">订单编号</label> 
						<input id="queryOrderNo" class="normal input" /> 
						<input id="queryMerchId" type="hidden" /> 
						<input id="querySettleDay" type="hidden" /> 
						<span class="spacer"></span> 
						<button id="btnQueryOrderDeatil" class="normal button">查询</button>
					</div>
				</div>
			</div>
			<table id="orderGrid"></table>
			<div id="orderPager"></div>
		</div>
	</div>
	
	<!--绑定账户详情 -->
	<div id="AcctDeatil" class="form" style="display: none;height: 300px;">
		
		<div class="ui-layout-center">
			<!--<div class="filter section">
				<div class="filter row">
					 <div class="group right aligned">
						<label class="label">订单编号</label> 
						<input id="queryOrderNo" class="normal input" /> 
						<input id="queryMerchId" type="hidden" /> 
						<input id="querySettleDay" type="hidden" /> 
						<span class="spacer"></span> 
						<button id="btnQueryOrderDeatil" class="normal button">查询</button> 
					</div>
				</div>
			</div>-->
			<table id="acctGrid"></table>
			<div id="acctPager"></div>
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
		
		// 结算方式字典
		var settleWays = {
			"alipay" : "支付宝",
			"directpay" : "银企直联",
			"unionpay" : "银联代付",
			"null" : ""
		};
		
		// 结算状态字典
		var settleStates = {
			"6" : "可生成结算单",
			"7" : "未到结算日",
			"8" : "无结算账户",
			"9" : "未出清算单"
		};
		
		//结算 确认提示框
		function settleConfirm(id) {
			//
			var yesHandler = function(layerIndex) {
				theLayer.hide();
				settleDo(id);
			};
			var noHandler = function(layerIndex) {
				theLayer.hide();
			};
			//
			var theLayer = Layer.confirm("您确定要执行结算吗？", yesHandler, noHandler);
		}
		//执行 单个 结算
		function settleDo(id){
			if(id){
				$id("ids").val(id);
				$id("settleAction").submit();
				
				//更改结算流程表中，结算状态为 settling  
				var ajax = Ajax.post("/settle/settling/do");
				ajax.data({
					id : id,
					settleFlag : 1,
					flag : 1,
				});
				ajax.done(function(result, jqXhr) {
					
				});
				ajax.go();
			}
		}
		
		//执行批量结算
		function settleDos(ids){
				//批量更改结算流程表中，结算状态为  settling  
				var ajax = Ajax.post("/settle/settling/do");
				ajax.data({
					ids : ids,
					settleFlag : 1,
					flag : 2
				});
				ajax.done(function(result, jqXhr) {
					
				});
				ajax.go();
		}
		
		
		//生成清算单信息
		function createSettleInfo(jqGridCtrl){
				var ajax = Ajax.post("/settle/createSettleInfo/do");
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
						jqGridCtrl.jqGrid("setGridParam", {
							page : 1
						}).trigger("reloadGrid");
					}
				});
				ajax.go();
		}
		
		//更新结算单
		function updateSettleInfo(jqGridCtrl){
				var ajax = Ajax.post("/settle/updateSettleInfo/do");
				/* ajax.data({
					settleFlag : 7
				}); */
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
		
		//提交结算单信息，去结算
		function submitSettleInfo(jqGridCtrl){
				var ajax = Ajax.post("/settle/submitSettleInfo/do");
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
		
		var OrderDeatil = null;
		var orderDeatilGridCtrl=null;
		//查看订单信息窗口
		var showOrderDialog;
		//
		var AcctDeatil = null;
		var acctDeatilGridCtrl=null;
		//
		var boundjqGridCtrl;
		
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
		//初始化绑定账户dialog
		function initAcctDeatil(){
			AcctDeatil = $( "#AcctDeatil" ).dialog({
		        autoOpen: false,
		        width : Math.min(800, $window.width()),
		        height : Math.min(380, $window.height()),
		        modal: true,
		        title : '结算账户',
		        buttons: {
		            "关闭": function() {
		            	AcctDeatil.dialog( "close" );
		          }
		        },
		        close: function() {
		        	AcctDeatil.dialog( "close" );
		        }
		      });
		}
		
		//打开订单详情对话框
		function openOrderDeatilDialog(obj){
			OrderDeatil.dialog("open");
			//
			loadOrderDeatil(obj);
		}
		
		//打开绑定账户对话框
		function openBoundAcctDialog(spId,merchantId){
			
			AcctDeatil.dialog("open");
			//
			loadAcctDeatil(spId,merchantId);
			
		}
		
		//
		function loadOrderDeatil(obj){
			$id("queryMerchId").val(obj.merchantId);
			$id("querySettleDay").val(obj.settleDay);
			var postData = {
					merchantId : obj.merchantId,
					settleDay : obj.settleDay
			};
			//加载本账期，订单详情
			if(orderDeatilGridCtrl==null){
				orderDeatilGridCtrl = $("#orderGrid").jqGrid({
				      url : getAppUrl("/settle/process/order/deatil/get"),  
				      contentType : 'application/json',  
				      mtype : "post",  
				      datatype : 'json',
				      postData:{filterStr : JSON.encode(postData,true)},
				      height : 240,
					  width : "100%",
				      colNames : ["id", "订单编号", "用户名称","联系人","商户名称","操作"],  
				      colModel : [{name:"id", index:"id", width:150,align : 'center', hidden : true},
				                  {name:"no", index:"no",width:200,align : 'center',},
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
			$id("amount").val(obj.amount);
			$id("settleAmount1").val(obj.settleAmount);
			
			$id("payWay").val(payWays[obj.payWay]);
			$id("payState").val(payStates[obj.payState]);
			
			showOrderDialog = $("#orderDialog").dialog({
				autoOpen : false,
				width : Math.min(640, $window.width()),
				height : Math.min(500, $window.height()),
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
		//
		function loadAcctDeatil(spId,merchantId){
			//$id("queryMerchId").val(obj.merchantId);
			//$id("querySettleDay").val(obj.settleDay);
			var postData = {
					merchantId : merchantId
			};
			//加载此商户已设置的资金结算账户
			if(acctDeatilGridCtrl==null){
				acctDeatilGridCtrl = $("#acctGrid").jqGrid({
				      url : getAppUrl("/merch/merchantSettleAcct/list/get"),  
				      contentType : 'application/json',  
				      mtype : "post",  
				      datatype : 'json',
				      postData:{filterStr : JSON.encode(postData,true)},
				      height : 200,
					  width : "100%",
					  colNames : ["id", "结算方式", "开户人","开户银行","银行账户","操作"],  
				      colModel : [{name:"id", index:"id", width:150,align : 'center', hidden : true},
				                  {
				                	  name:"settleWayCode", 
				                	  index:"settleWayCode",
				                	  width:110,
				                	  align : 'center',
				                	  formatter : function(cellValue){
				                	  	return settleWays[cellValue];
				                  	  }
				      			  },
				                  {name:"acctName", index:"acctName",width:130,align : 'center',},
				                  {name:"bankName", index:"bankName",width:140,align : 'center'},
				                  {name:"acctNo", index:"acctNo",width:230,align : 'center'},
				                  {
					                   name:"id", 
					                   index:"id", 
					                   width : 100, 
					                   align : "center",
					                   formatter : function(cellValue, option, rowObject) {
											var s = " <span>[<a class='item dlgBound' id='dlgBound' href='javascript:void(0);' onclick='openBoundConfirm(" + JSON.stringify(rowObject) +"," + spId + ")' >绑定</a>]"
											return s;
										}
					               }  			
								],  
					loadtext: "Loading....",
					pager : "#acctPager"
				});
			}else{
				acctDeatilGridCtrl.jqGrid("setGridParam", {
					postData : {
						filterStr : JSON.encode(postData, true)
					},
					page : 1
				}).trigger("reloadGrid");
			}
		}
		//
		//绑定账户 确认提示框  obj:结算资金账户
		function openBoundConfirm(obj,spId) {
			//
			var yesHandler = function(layerIndex) {
				theLayer.hide();
				boundAcctdo(obj,spId);
			};
			var noHandler = function(layerIndex) {
				theLayer.hide();
			};
			//
			var theLayer = Layer.confirm("您确定要绑定此账户吗？", yesHandler, noHandler);
		}
		//
		//将此settleprocess的结算账户绑定
		function boundAcctdo(obj,spId){
			var ajax = Ajax.post("/settle/bound/acct/by/settle/do");
			ajax.data({
				merchantId : obj.merchantId,
				settleWayCode : obj.settleWayCode,
				settleProcessId : spId//结算单ID
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					//
					AcctDeatil.dialog("close");
					//
					boundjqGridCtrl.jqGrid("setGridParam", {
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
				dlgTitle : "待结算列表",
				isUsePageCacheToView : true,
				isShowViewShowModBtn: false,
				jqGridGlobalSetting:{
					url : getAppUrl("/settle/sale/settle/waiting/list/get/-mall"),
					/* postData : {
						filterStr : JSON.encode({
							settleFlag : 0
						}, true)
					}, */
					colNames : ["id","商户名称", "结算方式", "账户名","结算账户","结算金额","结算周期","结算日期","状态","操作","",""],
					colModel : [
							{name:"id",hidden:true},
							{
								name : "merchantName",
								index : "merchantName",
								width : 200,
								align : 'center'
							},
							{
								name : "settleWay",
								index : "settleWay",
								width : 90,
								align : 'center',
								formatter : function(cellValue, option, rowObject) {
									return settleWays[cellValue];
								}
							},
							{
								name : "acctName",
								index : "acctName",
								width : 100,
								align : 'center'
							},
							{
								name : "acctNo",
								index : "acctNo",
								width : 200,
								align : 'center'
							},
							{
								name : "settleAmount",
								index : "settleAmount",
								width : 100,
								align : 'center',
								formatter : function(cellValue, option, rowObject) {
									return "<span style='color:red;'> "+ "￥ " + cellValue +"</span>";;
									
								}
							},
							{
								name : "settleX",
								index : "settleX",
								width : 90,
								align : 'center'
							},
							{
								name : "settleDay",
								index : "settleDay",
								width : 120,
								align : 'center'
							},
							{
								name : "settleFlag",
								index : "settleFlag",
								width : 120,
								align : 'center',
								formatter : function(cellValue, option, rowObject) {
									return settleStates[cellValue];
								}
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									var s = " <span>[<a class='item dlgview' id='dlgview' href='javascript:void(0);' cellValue='" + cellValue + "' >查看</a>]";
									var ss = "&nbsp;&nbsp;&nbsp;[<a class='item selectAcct' id='selectAcct' href='javascript:void(0);' cellValue='" + cellValue + "' cellValue1='" + rowObject.merchantId + "'>选择账户</a>]"
									var sss = "&nbsp;&nbsp;&nbsp;[<a href='javascript:void(0);' onclick='openOrderDeatilDialog("+JSON.stringify(rowObject)+")' >关联订单</a>]</span> "
									
									if(rowObject.settleFlag == 9){
										return sss;
									}
									
									return s + ss + sss;
									
								},
								width : 260,
								align : "center"
							},
							{
								name : "settleFlag",
								index : "settleFlag",
								hidden:true
							},
							{
								name : "tempDay",
								index : "tempDay",
								hidden:true
							}
							],
							//multiselect : true
				},
				// 修改和查看对话框
				modAndViewDlgConfig : {
					width : Math.min(350, $window.width()),
					height : Math.min(450, $window.height()),
					modal : true,
					open : false
				},
				 modAndViewInit:function (data) {
					 //textSet("merchantId", data.merchantId);
					 textSet("merchantNameF", data.merchantName);
					 textSet("acctName", data.acctName);
					 textSet("acctNo", data.acctNo);
					 textSet("settleAmount", data.settleAmount);
					 textSet("settleDay", data.settleDay);
					 textSet("settleX", data.settleX);
					 textSet("settleWay", settleWays[data.settleWay]);
					 textSet("settleFlag", settleStates[data.settleFlag]);
				 },
				queryParam : function(postData,formProxyQuery) {
					var merchantName = formProxyQuery.getValue("queryMerchantName");
					if (merchantName != "") {
						postData['merchantName'] = merchantName;
					}
					var settleFlag = singleGet("querySettleFlag");
					if (settleFlag && settleFlag != "-全部-") {
						postData['settleFlag'] = settleFlag;
					}
				},
				formProxyQueryInit : function(formProxyQuery) {
					// 注册查询表单控件
					formProxyQuery.addField({
						id : "queryMerchantName",
						rules : [ "maxLength[20]" ]
					});
				},
				pageLoad : function(jqGridCtrl) {
					
					$id("today").text(new Date().format("yyyy-MM-dd"));
					
					$(document).on("click", ".settle", function() {
						settleConfirm($(this).attr("cellValue"));
					});
					
					$(document).on("click", ".selectAcct", function() {
						boundjqGridCtrl = jqGridCtrl;
						openBoundAcctDialog($(this).attr("cellValue"),$(this).attr("cellValue1"));
					});
					
					//绑定生成清算单按钮
					$id("createSettleInfo").click(function() {
						createSettleInfo(jqGridCtrl);
					});
					//绑定更新清算单按钮
					$id("updateSettleInfo").click(function() {
						updateSettleInfo(jqGridCtrl);
					});
					//绑定生成结算单按钮
					$id("submitSettleInfo").click(function() {
						submitSettleInfo(jqGridCtrl);
					});
					
					//初始化 订单详情对话框
					initOrderDeatil();
					
					//初始化 绑定账户对话框
					initAcctDeatil();
					
					//条件查询订单详情
					$id("btnQueryOrderDeatil").click(function() {
						
						var filter = {};
						var no = textGet("queryOrderNo");
						if(no){
							filter.no = no;
						}
						var merchantId = textGet("queryMerchId");
						if(merchantId){
							filter.merchantId = merchantId;
						}
						var settleDay = textGet("querySettleDay");
						if(settleDay){
							filter.settleDay = settleDay;
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