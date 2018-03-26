<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>合作店铺管理</title>
<style type="text/css">
table.gridtable {
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-collapse: collapse;
	width: 600px;
}

table.gridtable th {
	border: 1px solid #AAA;
	padding: 3px;
	background-color: #dedede;
	height: 24px;
	line-height: 22px;
}

table.gridtable td {
	border: 1px solid #AAA;
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
					<!-- <button id="btnToAddShop" class="button">添加</button> -->
					<%-- button id="btnDelShops" class="button">批量删除</button>--%>
				</div>
				<div class="group right aligned">
					<label class="label">店铺名称</label> <input id="queryShopName"
						class="normal input" /> <span class="spacer"></span> 
					<button id="btnToQry" class="normal button">查询</button>
				</div>
			</div>
		</div>
		
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
		
		<!-- 	隐藏Dialog -->
		<div id="dialog" style="display: none;">
			<div id="addForm" class="form">
				<div id="singleShop" >
					<div class="field row">
						<label class="field label one half wide">店铺名称</label> 
						<input type="text" id="name" class="field value one half wide" />
					</div>
					<div class="field row" style="height: 80px">
						<input type="hidden"  id="logoUuid" />
						<input type="hidden"  id="logoUsage" />
						<input type="hidden"  id="logoPath" />
						<label class="field label one half wide">店铺logo</label>
			        	<img id="wxShopLogo" height="80px" width="120px" src="<%=resBaseUrl%>/image-app/img-none.jpg"/>
					</div>
					<div class="field row" style="margin-top: 8px">
						<label class="field label one half wide">联系人</label>
						<input type="text" id="linkMan" class="field value one half wide" />
					</div>
					<div class="field row" style="margin-top: 8px">
						<label class="field label one half wide">真实名字</label>
						<input type="text" id="realName" class="field value one half wide" />
					</div>
					<div class="field row" style="margin-top: 8px">
						<label class="field label one half wide">身份证号</label>
						<input type="text" id="idCertNo" class="field value one half wide" />
					</div>
					<div class="field row">
						<label class="field label one half wide">联系电话</label>
						<input type="text" id="telNo" class="field value one half wide" />
					</div>
					<div class="field row">
						<label class="field label one half wide">手机号</label>
						<input type="text" id="phoneNo" class="field value one half wide" />
					</div>
					<div class="field row">
						<label class="field label one half wide">经度</label>
						<input type="text" id="lng" class="field value one half wide" />
					</div>
					<div class="field row">
						<label class="field label one half wide">纬度</label>
						<input type="text" id="lat" class="field value one half wide" />
					</div>
					<div class="field row">
						<label class="field label one half wide">所在地</label>
						<input type="text" id="provinceCity" class="field value three wide" maxlength="60" />
					</div>
					<div class="field row">
						<label class="field label one half wide">街道地址</label>
						<input type="text" id="street" class="field value three wide" maxlength="60" title="请输入街道地址" />
					</div>
				</div>
					<div class="ui-layout-center form">
						<div class="field row"
							style="height: auto; line-height: auto; width: 100%;">
							<table data-role="staff_tab" class="gridtable" style="width: 100%;">
								<thead>
									<tr>
										<th width="25%">服务名称</th>
										<th width="10%">销售价格</th>
										<th width="10%">下单利润</th>
										<th width="10%">派单利润</th>
										<th width="15%">服务状态</th>
										<th width="40%">审核描述</th>
									</tr>
								</thead>
								<tbody id="packItem_tbd">
								</tbody>
							</table>
						</div>
					</div>
			</div>
		</div>
		<div id="auditDialog" style="display: none;">
			<div id="addAuditForm" class="form">
				<input id="distShopId" name="distShopId" value="" hidden="true">
				<input id="auditType" name="auditType" value="" hidden="true">
				<div class="field row">
					<label class="field label one half wide required">审核状态</label> 
					<div class="field group">
						<input type="radio" id="defaulted-Y" name="disabled" value="true" checked="checked"> 
						<label for="defaulted-Y">通过</label> 
						<input type="radio" id="defaulted-N" name="disabled" value="false">
						<label for="defaulted-N">不通过</label>
					</div>
				</div>
				<div id="svcInfo" style="display: none;">
					<div class="field row">
						<label class="field label one half wide">当前服务售价:</label> 
						<span id="salePrice" style="color: red;"></span><span>&nbsp;&nbsp;元</span>
					</div>
					<div class="field row">
						<label class="field label one half wide required">派单利润</label> 
						<input type="text" id="distProfit" name="distProfit" class="field value one half wide" />
					</div>
					<div class="field row">
						<label class="field label one half wide required">下单利润</label> 
						<input type="text" id="distProfitX" name="distProfitX" class="field value one half wide" />
					</div>
				</div>
				<div id="auditDescDisplay" class="field row" style="height: 70px;display:none;">
					<label class="field label one half wide">拒绝原因</label>
					<textarea class="field value one half wide"
						style="height: 60px; width: 300px; resize: none;" name="auditDesc"
						id="auditDesc"></textarea>
					<span class="normal spacer"></span>
				</div>
			</div>
		</div>
		<div id="wxShopSvcDialog" class="form" style="display: none;height: 600px;">
			<div class="ui-layout-center">
				<table id="wxshopGrid"></table>
				<!-- <div id="wxshopPager"></div> -->
			</div>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js?<%=System.currentTimeMillis()%>"></script>
	<script type="text/javascript">
		//
		var wxshopGridCtrl = null;
		// 商店当前操作
		var distShopGridHelper = JqGridHelper.newOne("");
		//
		var selectBizLicenseDialog = null;
		//
		var isChangeImgData=false;
		//
		var auditDialog = null;
		//
		var auditIds = "";
		//
		var wxshopSvcGridCtrl = null;
		
		var wxShopSvcDialog = null;
		//验证表单
		var noticeFormProxy = FormProxy.newOne();
		//
		$id("mainPanel").jgridDialog(
			{
				dlgTitle : "合作店",
				addUrl : "/distshop/hzshop/add/do",
				updUrl : "/distshop/hzshop/update/do",
				delUrl : "/distshop/hzshop/delete/do",
				isUsePageCacheToView: true,
				isShowViewShowModBtn: false,
				jqGridGlobalSetting:{
					url : getAppUrl("/distshop/wxshop/list/get/-shop"),
					colNames : [ "id", "店铺名称", "联系人", "联系电话", "所在地","店铺状态", " 操作"],
					colModel : [
							{
								name : "id",
								index : "id",
								hidden : true
							},
							{
								name : "name",
								index : "name",
								width : 200,
								align : 'left',
							},
							{
								name : "linkMan",
								index : "linkMan",
								width : 100,
								align : 'left'
							},
							{
								name : "phoneNo",
								index : "phoneNo",
								width : 100,
								align : 'center'
							},
							{
								name : "regionName",
								index : "regionName",
								width : 200,
								align : 'left'
							},
							{
								name : "auditStatus",
								index : "auditStatus",
								width : 100,
								align : "center",
								formatter : function(cellValue, option, rowObject) {
									if (rowObject.disabled == false) {
										if (cellValue == 0) {
											return '<span style="color:green;">待审核</span>';
										} else if (cellValue == 1) {
											return '<span style="color:green;">审核通过</span>';
										} else {
											return '<span style="color:red;">未通过</span>';
										}
									} else {
										return "已禁用";
									}
								}
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									var tempItem = String.builder();
									var disablied = rowObject.disabled;
									tempItem.append("[<a class='item dlgview' href='javascript:void(0);' cellValue='" + cellValue + "'>查看</a>]");
									if(disablied == false){
										var auditStatus = rowObject.auditStatus;
										if(auditStatus == 0){
											tempItem.append("&nbsp;&nbsp;[<a class='item audit' href='javascript:void(0);' cellValue='" + cellValue + "'>店铺审核</a>]");
										}else if(auditStatus == 1){
											var svcList = rowObject.distShopSvcList;
											if(svcList != null && svcList.length >0){
												var isAudit = false;//是否显示服务审核
												for (var i = 0; i < svcList.length; i++) {
													var distSvc = svcList[i];
													var auditStatus = distSvc.auditStatus;
													if(auditStatus == 0){
														isAudit = true;
													}
												}
												if(isAudit){
													tempItem.append("&nbsp;&nbsp;[<a class='item' href='javascript:void(0);' onclick='openShowDialog(" + JSON.stringify(svcList) + ")'>服务审核</a>]");
												}
											}
										}else{
											
										}
									}
									return tempItem.value;
								},
								width : 200,
								align : "center"
							} ],
				},
				// 增加对话框
				addDlgConfig : {
					width : Math.min(680, $window.width()),
					height : Math.min(600, $window.height()),
					modal : true,
					open : false
				},
				// 修改和查看对话框
				modAndViewDlgConfig : {
					width : Math.min(680, $window.width()),
					height : Math.min(600, $window.height()),
					modal : true,
					open : false
				},
				modElementToggle : function(isShow) {
				},
				 addInit:function () {
				},
				 modAndViewInit:function (data) {
					textSet("name", data.name);
					textSet("linkMan", data.linkMan);
					textSet("telNo", data.telNo);
					textSet("phoneNo", data.phoneNo);
					textSet("realName", data.realName);
					textSet("idCertNo", data.idCertNo);
					textSet("lng", data.lng);
					textSet("lat", data.lng);
					textSet("provinceCity", data.address);
					textSet("street", data.street);
					showShopSvcItem(data.distShopSvcList);
					$id("wxShopLogo").attr("src", data.fileBrowseUrl + "?" + new Date().getTime());
					// 加载地区
					//bindRegionLists("province", "city", "county", "town", [ data.provinceId, data.cityId, data.countyId, data.townId ]);
				},
				queryParam : function(postData,formProxyQuery) {
					var distShopName = textGet("queryShopName");
					if (distShopName != "") {
						postData['distShopName'] = distShopName;
					}
					
				},
				formProxyInit : function(formProxy) {
					// 注册表单控件
					formProxy.addField({
						id : "name",
						key : "name",
						required : true,
						messageTargetId : "name"
					});
					// 注册表单控件
					formProxy.addField({
						id : "street",
						key : "street",
						required : true,
						messageTargetId : "street"
					});
					formProxy.addField({
						id : "selectShop",
						rules : [{
							rule : function(idOrName, type, rawValue, curData) {
								var shopId = $id("shopId").val();
								if(!shopId){
									return false;
								}
								return true;
							},
							message: "亲,您还未选择加盟店哦！"
						}],
						messageTargetId : "selectShop"
					});
				},
				formProxyQueryInit : function(formProxyQuery) {
					// 注册表单控件
					formProxyQuery.addField({
						id : "queryShopName",
						rules : [ "maxLength[30]" ]
					});
				},
				saveOldData : function(data) {
				},
				getDelComfirmTip : function(data) {
					return '确定要删除此条记录吗？';
				},
				savePostDataChange:function(postData){
				},
				pageLoad : function(jqGridCtrl, cacheDataGridHelper) {
					initAuditDialog(jqGridCtrl);
					//
					$(document).on("click", ".audit", function() {
						var id = $(this).attr("cellValue");
						textSet("distShopId",id);
						textSet("auditType","audit");
						$id("distProfit").val("");
						$id("distProfitX").val("");
						$id("svcInfo").css("display","none");
						$id("defaulted-Y").attr("checked","checked");
						closeAuditDescDiv();
						auditDialog.dialog("open");
					});
					$(document).on("click", ".auditSvc", function() {
						var id = $(this).attr("cellValue");
						textSet("distShopId",id);
						textSet("auditType","auditSvc");
						$id("distProfit").val("");
						$id("distProfitX").val("");
						$id("defaulted-Y").attr("checked","checked");
						$id("svcInfo").css("display","block");
						var svc = distShopGridHelper.getRowData(id);
						if(svc != null){
							var salePrice = svc.salePrice;
							$id("salePrice").html("￥"+salePrice);
						}
						closeAuditDescDiv();
						auditDialog.dialog("open");
					});
					initWxShopSvcDialog();
					loadWxshopSvcData();
				}
			});
		$("[name='disabled']").click(function(){
			var value = $(this).val();
			if(value == true ||value == "true"){
				closeAuditDescDiv();
				var auditType = textGet("auditType");
				if(auditType == "auditSvc"){
					$id("svcInfo").css("display","block");
				}
				noticeFormProxy.hideMessages();
			}else{
				openAuditDescDiv();
				noticeFormProxy.hideMessages();
			}
		});
		function openAuditDescDiv(){
			$id("auditDescDisplay").css("display","block");
			$id("svcInfo").css("display","none");
			$id("auditDesc").val("");
		}
		function closeAuditDescDiv(){
			$id("auditDescDisplay").css("display","none");
			$id("auditDesc").val("");
		}
		
		//初始化Dialog
		function initAuditDialog(jqGridCtrl){
			auditDialog = $( "#auditDialog" ).dialog({
		        autoOpen: false,
		        width : Math.min(500, $window.width()),
		        height : Math.min(300, $window.height()),
		        modal: true,
		        title : "审核",
		        buttons: {
		        	"确定": function(){
		        		var distShopId = textGet("distShopId");
		        		if(distShopId){
		        			var auditDesc = textGet("auditDesc");
		        			var disabled = radioGet("disabled");
		        			var auditStatus = null;
		        			if(disabled == "true" || disabled == true){
		        				auditStatus = 1;
		        			}else if(disabled == "false" || disabled == false){
		        				auditStatus = -1;
		        			}
		        			if(auditStatus == null){
		        				Layer.msgWarning("参数错误，请刷新页面！");
		        				return ;
		        			}
		        			var postData={
		        					id : distShopId,
		        					auditStatus : auditStatus,
		        					auditDesc : auditDesc
		        			}
		        			var auditType = textGet("auditType");
		        			if(auditType == "audit"){
		        				toUpDistShop(postData,jqGridCtrl);
		        				auditDialog.dialog("close");
		        			}else if(auditType == "auditSvc"){
		        				if(auditStatus == 1){
		        					var distProfit = textGet("distProfit");
			        				var distProfitX = textGet("distProfitX");
			        				var result = noticeFormProxy.validateAll();
			        				if(result){
			        					postData.distProfit = distProfit;
			        					postData.distProfitX = distProfitX;
			        					toUpDistShopSvc(postData,jqGridCtrl);
			        					auditDialog.dialog("close");
			        				}
		        				}else{
		        					toUpDistShopSvc(postData,jqGridCtrl);
		        					auditDialog.dialog("close");
		        				}
		        			}
		        		}else{
		        			Layer.msgWarning("参数错误，请刷新页面！");
		        		}
		            },
		            "关闭": function() {
		            	auditDialog.dialog( "close" );
		            	noticeFormProxy.hideMessages();
		          }
		        },
		        close: function() {
		        	auditDialog.dialog( "close" );
		        	noticeFormProxy.hideMessages();
		        }
		      });
		}
		//-----------------------------------------------------表单验证-------------------
		noticeFormProxy.addField({
			id : "distProfit",
			key : "distProfit",
			required : true,
			rules : [ "maxLength[18]","isMoney"]
		});
		noticeFormProxy.addField({
			id : "distProfitX",
			key : "distProfitX",
			required : true,
			rules : [ "maxLength[18]","isMoney"]
		});
		function toUpDistShop(postData,jqGridCtrl){
			var ajax = Ajax.post("/distshop/wxShop/audit/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					jqGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
		}
		
		function toUpDistShopSvc(postData,jqGridCtrl){
			var ajax = Ajax.post("/distshop/wxShopSvc/audit/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					var statusValue = "";
					var tempItem = String.builder();
					if (postData.auditStatus == 0) {
						tempItem.append("[<a class='item auditSvc' href='javascript:void(0);' cellValue='" + postData.id + "'>审核</a>]");
					} else if (postData.auditStatus == 1) {
						tempItem.append('<span></span>');
					} else {
						tempItem.append('<span></span>');
					}
					wxshopSvcGridCtrl.jqGrid("setCell",postData.id,"auditStatus",postData.auditStatus);
					wxshopSvcGridCtrl.jqGrid("setCell",postData.id,"id",tempItem.value);
					jqGridCtrl.jqGrid("setGridParam", {page : 1}).trigger("reloadGrid");
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
		}
		function openShowDialog(svcList){
			if(svcList != null && svcList.length >0){
				wxshopSvcGridCtrl.jqGrid('clearGridData');
				distShopGridHelper.cacheData(svcList);
				for ( var i = 0; i < svcList.length; i++){
					wxshopSvcGridCtrl.jqGrid('addRowData',svcList[i].id, svcList[i]);
				}
			}
			wxShopSvcDialog.dialog("open");
		}
		function initWxShopSvcDialog(){
			wxShopSvcDialog = $( "#wxShopSvcDialog").dialog({
		        autoOpen: false,
		        width : Math.min(800, $window.width()),
		        height : Math.min(600, $window.height()),
		        modal: true,
		        title : '审核服务',
		        buttons: {
		            "关闭": function() {
		            	wxShopSvcDialog.dialog( "close" );
		          }
		        },
		        close: function() {
		        	wxShopSvcDialog.dialog( "close" );
		        }
		      });
		}
		//加载卫星店服务列表数据
		function loadWxshopSvcData(){
			//加载卫星店
			wxshopSvcGridCtrl= $id("wxshopGrid").jqGrid({
			      datatype : 'local',
			      height : 400,
				  width : "100%",
			      colNames : ["服务名称", "价格", "派单利润", "下单利润", "服务状态 ", "操作"],  
			      colModel : [
							{
								name : "svcName",
								index : "svcName",
								width : 200,
								align : 'left',
							},
							{
								name : "salePrice",
								index : "salePrice",
								width : 80,
								align : 'left',
								formatter : function(cellValue, option, rowObject) {
									return "￥&nbsp;"+cellValue;
								}
							},
							{
								name : "distProfit",
								index : "distProfit",
								width : 80,
								align : 'left',
								formatter : function(cellValue, option, rowObject) {
									if(cellValue == null){
										return "";
									}else{
										return "￥&nbsp;"+cellValue;
									}
								}
							},
							{
								name : "distProfitX",
								index : "distProfitX",
								width : 80,
								align : 'left',
								formatter : function(cellValue, option, rowObject) {
									if(cellValue == null){
										return "";
									}else{
										return "￥&nbsp;"+cellValue;
									}
								}
							},
							{
								name : "auditStatus",
								index : "auditStatus",
								width : 100,
								align : "center",
								formatter : function(cellValue, option, rowObject) {
										if (cellValue == 0) {
											return '<span style="color:green;">待审核</span>';
										} else if (cellValue == 1) {
											return '<span style="color:green;">审核通过</span>';
										} else {
											return '<span style="color:red;">未通过</span>';
										}
								}
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
										var tempItem = String.builder();
										var auditStatus = rowObject.auditStatus;
										if(auditStatus == 0){
											tempItem.append("[<a class='item auditSvc' href='javascript:void(0);' cellValue='" + cellValue + "'>审核</a>]");
										}else if(auditStatus == 1){
											
										}else{
											
										}
									return tempItem.value;
								},
								width : 200,
								align : "center"
							}],  
				loadComplete : function(gridData){
					distShopGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if(isFunction(callback)){
						callback();
					}
				}
			});
		}
		function getCallbackAfterGridLoaded() {

		}
		//---------------------------------------------------------------------------------------------
		function showShopSvcItem(items) {
			var theTpl = laytpl($id("shopSvcItemTpl").html());
			var htmlStr = theTpl.render(items);
			$("#packItem_tbd").html(htmlStr);
		}
		function getAuditStatusValue(auditStatus){
			if (auditStatus == 0) {
				return '<span style="color:green;">待审核</span>';
			} else if (auditStatus == 1) {
				return '<span>审核通过</span>';
			} else {
				return '<span style="color:red;">未通过</span>';
			}
		}
	</script>
</body>
<script type="text/html" id="shopSvcItemTpl" title="合作店服务列表">
	{{# var items = d; }}
	{{# if(!isNoB(items) && items.length>0){ }}
	{{# for(var i = 0, len = items.length; i < len; i++){ }}
	{{# var item = items[i]; }}
		<tr id="packItemId{{item.svcId}}">
			<td><label data-role="svcName">{{item.svcName}}</label></td>
			<td><label data-role="svcSalePrice">￥{{item.salePrice}}</label></td>
			<td><label data-role="distProfit">{{# if(item.distProfit != null){ }}￥{{item.distProfit}}{{# } }}</label></td>
			<td><label data-role="distProfitX">{{# if(item.distProfitX != null){ }}￥{{item.distProfitX}}{{# } }}</label></td>
			<td><label>{{getAuditStatusValue(item.auditStatus)}}</label></td>
			<td><label>{{item.auditDesc || ""}}</label></td>
		</tr>
	{{# } }}
	{{# } }}
</script>
</html>