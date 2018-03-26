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
					<button id="btnBachSettle" class="button one half wide">支付宝批量结算</button>
					<span class="spacer"></span>
					<button id="btnBachSettleAsAbc" class="button one half wide">农行批量结算</button>
					<!-- <button id="createSettleInfo" class="button">生成结算单</button> -->
				</div>
				<div class="group right aligned">
					<label class="label">商户名称</label> <input id="queryMerchantId" class="input" /> 
					<label class="label">结算状态</label>
					<select id="querySettleFlag" class="input">
						<option>-全部-</option>
						<option value="1">结算中</option>
						<option value="2">结算失败</option>
						<option value="3">待结算</option>
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
	<form id="settleAction" action="<%=appBaseUrl%>/settle/alipay/batch/settle" target="_blank" method="post">
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
						<label class="label">订单号</label> 
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
	<!--手工结算关联信息-->
	<div id="SettleBill" class="form" style="display: none;height: 300px;">
		<div class="ui-layout-center">
			<div class="field row">
				<label class="field label required one half wide">关联结算单</label> 
				<input type="text" id="relationBill" class="field one half value wide" /> 
				<input type="hidden" id="relationBillId" class="field one half value wide" /> 
			</div>
		</div>
	</div>
	
	<div id="refundConfirmText" style="display: none;">
			<div style="margin: 30px 50px 0; text-align: center; vertical-align: middle;">
				<span >是否申请了结算操作，并收到支付宝申请成功的反馈？</span>
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
			"1" : "结算中",
			"2" : "结算失败",
			"3" : "待结算"
		};
		
		//结算 确认提示框
		function settleConfirm(obj,jqGridCtrl) {
			//
			var yesHandler = function(layerIndex) {
				theLayer.hide();
				
				// 判断结算方式
				if(obj.settleWay == "alipay"){
					settleDo(obj.id);
					//
					resultConfirm(obj,jqGridCtrl);
				}else if(obj.settleWay == ""){
					//
				}else if(obj.settleWay == "directpay"){
					//
					settleAsAbcDoAsSingle(obj.id, jqGridCtrl);
				}else{
					alert("结算出错，请联系技术人员");
				}
				
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
		
		//支付宝单个结算——是否去执行结算了   确认提示框
		function resultConfirm(obj,jqGridCtrl) {
			var dom = "#refundConfirmText";
			var theDlg = Layer.dialog({
				dom : dom, //或者 html string
				area : [ '300px', '200px' ],
				closeBtn : true,
				title : "结算操作确认",
				btn : [ '是，已收到', '否，未收到' ],
				yes : function() {
					theDlg.hide();
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
		//
		//支付宝批量结算——是否去执行结算了   确认提示框
		function batchResultConfirm(ids,jqGridCtrl) {
			var dom = "#refundConfirmText";
			var theDlg = Layer.dialog({
				dom : dom, //或者 html string
				area : [ '300px', '200px' ],
				closeBtn : true,
				title : "结算操作确认",
				btn : [ '是，已收到', '否，未收到' ],
				yes : function() {
					theDlg.hide();
					//1、更改状态为退款中（如果状态不为已退款的情况）
					//refundDosByAlipay(ids);
					
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
		//生成结算单信息
		/* function createSettleInfo(jqGridCtrl){
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
					}
				});
				ajax.go();
		} */
		
		//农行执行单个结算请求
		function settleAsAbcDoAsSingle(id, jqGridCtrl){
				var ajax = Ajax.post("/settle/settle/as/abc/do");
				ajax.data({
					id : id
				});
				ajax.done(function(result, jqXhr) {
					Layer.msgSuccess("请求成功");
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
		//农行执行批量结算请求
		function settleAsAbcDo(ids, jqGridCtrl){
				var ajax = Ajax.post("/settle/settle/as/abc/do");
				ajax.data({
					ids : ids
				});
				ajax.done(function(result, jqXhr) {
					Layer.msgSuccess("请求成功");
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
		var SettleBill = null;
		var orderDeatilGridCtrl=null;
		var jqGridCtrlBill = null;
		//查看订单信息窗口
		var showOrderDialog;
		//
	    //var orderGridHelper = JqGridHelper.newOne("");
		
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
		//初始化关联结算单dialog
		function initSettleBill(){
			SettleBill = $( "#SettleBill" ).dialog({
		        autoOpen: false,
		        width : Math.min(350, $window.width()),
		        height : Math.min(200, $window.height()),
		        modal: true,
		        title : '关联结算单',
		        buttons: {
		          	"确定": function(){
		          		saveSettleBill();
		          		SettleBill.dialog( "close" );
		          },
		            "关闭": function() {
		            	$id("relationBillId").val("");
		            	$id("relationBill").val("");
		            	SettleBill.dialog( "close" );
		          }
		        },
		        close: function() {
		        	SettleBill.dialog( "close" );
		        }
		      });
		}
		
		//打开订单详情对话框
		function openOrderDeatilDialog(obj){
			OrderDeatil.dialog("open");
			//
			loadOrderDeatil(obj);
		}
		//打开关联结算单对话框
		function openSettleBillDialog(rowData,jqGridCtrl){
			SettleBill.dialog("open");
			//
			$id("relationBillId").val(rowData.id);
			jqGridCtrlBill = jqGridCtrl;
			//loadOrderDeatil(obj);
		}
		
		//
		function loadOrderDeatil(obj){
			$id("queryMerchId").val(obj.merchantId);
			$id("querySettleDay").val(obj.settleDay);
			var postData = {
					merchantId : obj.merchantId,
					settleDay : obj.settleDay,
					spId : obj.id
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
				                  {name:"no", index:"no",width:150,align : 'center',},
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
		
		function saveSettleBill(){
			var settleId = $id("relationBillId").val();
			var billExtra = $id("relationBill").val();
			//
			var ajax = Ajax.post("/settle/manual/do");
			ajax.data({
				id : settleId,
				billExtra : billExtra
			});
			ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				//
				Layer.msgSuccess(result.message);
				jqGridCtrlBill.jqGrid("setGridParam", {
					page : 1
				}).trigger("reloadGrid");
				//
				$id("relationBillId").val("");
            	$id("relationBill").val("");
			} else {
				Layer.warning(result.message);
			}
			});
			ajax.go();
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
	// -------------------------------------------------------------------------------------}}
	
		$id("mainPanel").jgridDialog(
			{
				dlgTitle : "待结算列表",
				isUsePageCacheToView : true,
				isShowViewShowModBtn: false,
				jqGridGlobalSetting:{
					url : getAppUrl("/settle/sale/settle/list/get/-mall"),
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
								width : 150,
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
								width : 110,
								align : 'center',
								formatter : function(cellValue, option, rowObject) {
									return "<span style='color:red;'> "+ "￥ " + cellValue +"</span>";;
								}
							},
							{
								name : "settleX",
								index : "settleX",
								width : 100,
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
								width : 100,
								align : 'center',
								formatter : function(cellValue, option, rowObject) {
									return settleStates[cellValue];
								}
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									var s = "<span>[<a class='item dlgview' id='dlgview' href='javascript:void(0);' cellValue='" + cellValue + "' >查看</a>]"
									var ss = " &nbsp;&nbsp;&nbsp;[<a href='javascript:void(0);' onclick='openOrderDeatilDialog("+JSON.stringify(rowObject)+")' >关联订单</a>]"
									var sss = "&nbsp;&nbsp;&nbsp;[<a class='settle' name='settle"+cellValue+"' href='javascript:void(0);' cellValue='" + cellValue + "'>结算</a>]";
									//var ssss = "&nbsp;&nbsp;&nbsp;[<a class='finishSettle' href='javascript:void(0);' cellValue='" + cellValue + "'>完成</a>]</span>";
									
									if (rowObject.settleFlag == 1){
										return s + ss;
									}
									return s + ss + sss;
									
								},
								width : 200,
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
							multiselect : true
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
				 settingViewBtn:function(buttons,rowData,jqGridCtrl){
					 if(rowData.settleFlag != 1){
						buttons["手动结算"]=function() {
							openSettleBillDialog(rowData,jqGridCtrl);
							//$(this).dialog("close");
						};
					 }
				},
				queryParam : function(postData,formProxyQuery) {
					var merchantName = formProxyQuery.getValue("queryMerchantId");
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
						id : "queryMerchantId",
						rules : [ "maxLength[20]" ]
					});
				},
				pageLoad : function(jqGridCtrl,cacheDataGridHelper) {
					
					$id("today").text(new Date().format("yyyy-MM-dd"));
					
					// 操作——结算按钮
					$(document).on("click", ".settle", function() {
						var obj=cacheDataGridHelper.getRowData(parseInt($(this).attr("cellValue")));
						settleConfirm(obj,jqGridCtrl);
					});
					// 操作——完成按钮
					/* $(document).on("click", ".finishSettle", function() {
						var obj=cacheDataGridHelper.getRowData(parseInt($(this).attr("cellValue")));
						finishSettleConfirm(obj,jqGridCtrl);
					}); */
					//订单详情
					/* $(document).on("click", ".orderView", function() {
						openOrderDeatilDialog($(this).attr("rowObject"));
					}); */
					
					//绑定支付宝批量结算按钮
					$id("btnBachSettle").click(function() {
						var ids = jqGridCtrl.jqGrid("getGridParam", "selarrrow");
						if (ids == "") {
							Layer.msgWarning("请选中要结算项");
							return;
						}
						
						for (var i = 0; i < ids.length; i++) {
							var rowData = jqGridCtrl.jqGrid("getRowData", ids[i]);//根据上面的id获得本行的所有数据
							if (1 == rowData.settleFlag ) {
								Layer.msgWarning("包含已经为结算中的数据，请重新选择");
								return;
							}
							if (rowData.settleWay != "支付宝") { 
								Layer.msgWarning("包含结算方式不为支付宝的数据，请重新选择");
								return;
							}
						}
						
						var theLayer = Layer.confirm('确定要执行批量结算吗？', function() {
							$id("ids").val(ids);
							$id("settleAction").submit();
							theLayer.hide();
							
							settleDos(ids);
							batchResultConfirm(ids,jqGridCtrl);
						});
					});
					//绑定农行批量结算按钮
					$id("btnBachSettleAsAbc").click(function() {
						var ids = jqGridCtrl.jqGrid("getGridParam", "selarrrow");
						if (ids == "") {
							Layer.msgWarning("请选中要结算项");
							return;
						}
						
						for (var i = 0; i < ids.length; i++) {
							var rowData = jqGridCtrl.jqGrid("getRowData", ids[i]);//根据上面的id获得本行的所有数据
							if (1 == rowData.settleFlag ) {
								Layer.msgWarning("包含已经为结算中的数据，请重新选择");
								return;
							}
							if (rowData.settleWay != "银企直联") { // TODO 还应该判断settleWay力的codeX不等于abc(农行)
								Layer.msgWarning("包含结算方式不为农行的数据，请重新选择");
								return;
							}
						}
						
						var theLayer = Layer.confirm('确定要执行批量结算吗？', function() {
							theLayer.hide();
							// ajax去请求结算
							settleAsAbcDo(ids,jqGridCtrl);
						});
					});
					//绑定生成结算单按钮
					/* $id("createSettleInfo").click(function() {
						createSettleInfo(jqGridCtrl);
					}); */
					
					//初始化 订单详情对话框
					initOrderDeatil();
					
					//初始化填写人工结算关联信息对话框
					initSettleBill();
					
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