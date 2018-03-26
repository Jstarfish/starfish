<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>车辆品牌管理</title>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnAdd" class="button">添加</button>
					<%-- button id="btnDelShops" class="button">批量删除</button>--%>
				</div>
				<div class="group right aligned">
					<label class="label">车辆名称</label> <input id="queryName"
						class="normal one half wide input" /> <span class="spacer"></span> 
					<button id="btnToQuery" class="normal button">查询</button>
				</div>
			</div>
		</div>
		<table id="userCarGridCtrl"></table>
		<div id="userCarPager"></div>
	</div>
	<!-- 	隐藏Dialog -->
	<div id="userDlg" style="display: none;">
		<div id="addForm" class="form">
			<div id="userList" class="noBorder" style="margin-left: 20px;">
				<div style="margin: 10px 0; margin-left: 8px">
					<div class="group left aligned" style="float: left;">
						<label  style="font-size: 14px;color: #666;" id="selectMember"><span style="color: red"> * </span>会员信息</label>
					</div>
					<div class="group right aligned" style="float: right;">
						<button id="selectMemberBtn" class="normal button">选择</button>
					</div>
				</div>
				<div style="text-align: center; vertical-align: middle;">
					<table class="gridtable" style="width: 100%;">
						<thead></thead>
						<tbody id="member_tbody">
							<tr class="field row">
								<td align="right" width="24%"><label class="field label one half wide" style="float: right;">车主昵称</label></td>
								<td align="left">
									<input type="hidden" id="userId" />
									<input type="text" id="nickName" class="field value one half wide" />
								</td>
							</tr>
							<tr class="field row">
								<td align="right"><label class="field label one wide" style="float: right;">手机号</label></td>
								<td align="left">
									<input type="text" id="phoneNo" class="field value one half wide" />
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<span id="divider" class="normal hr divider"></span>
			<div id="carList" class="noBorder" style="margin-left: 20px;">
				<div id="singleUserCar" style="margin-left: 20px;">
					<div style="margin: 10px 0;">
						<label class="field inline label three half wide" style="font-size: 14px;color: #666;">车辆信息</label>
						<input type="hidden" id="userCarId"/>
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label required one half wide">车辆名称</label>
						<input type="text" class="field value one half wide" id="name" >
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label required one half wide">品牌车型</label>
						<select class="field value" id="brandId" name="brandId"><option value="" title="- 请选择 -"></option></select>
						<select class="field value" id="serialId" name="serialId"><option value="" title="- 请选择 -"></option></select>
						<select class="field value" id="modelId" name="modelId"><option value="" title="- 请选择 -"></option></select>
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label one half wide">车身颜色</label>
						<input type="text" class="field value one half wide" id="color" >
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label one half wide">机动车身份标识</label>
						<input type="text" class="field value one half wide" id="vklIdNo" >
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label one half wide">车辆类型</label>
						<input type="text" class="field value one half wide" id="vklType" >
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label one half wide">车牌号</label>
						<input type="text" class="field value one half wide" id="plateNo" >
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label one half wide">发动机号</label>
						<input type="text" class="field value one half wide" id="engineNo" >
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label one half wide">生产年份</label>
						<input type="text" class="field value one half wide" id="makeYear" > 年
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label one half wide">购买年份</label>
						<input type="text" class="field value one wide" id="buyYear" style="width: 79px"> 年
						<input type="text" class="field value one wide" id="buyMonth" style="width: 79px"> 月
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label one half wide">价格</label>
						<input type="text" class="field value one half wide" id="price" > 万
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label one half wide">行驶公里数</label>
						<input type="text" class="field value one half wide" id="mileage" > 万
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<!--选择商户dialog -->
	<div id="selectMemberDialog" class="form" style="display: none;height: 600px;">
		
		<div class="ui-layout-center">
			<div class="filter section">
				<div class="filter row">
					<div class="group right aligned">
						<label class="label">会员昵称</label> 
						<input id="selectName" class="normal input" /> 
						<span class="spacer"></span> 
						<label class="label">手机号</label> 
						<input id="selectPhoneNo" class="normal input" /> 
						<span class="spacer"></span> 
						<button id="btnQueryMember" class="normal button">查询</button>
					</div>
				</div>
			</div>
			<table id="memberGrid"></table>
			<div id="memberPager"></div>
		</div>
	</div>
	<!--车辆服务记录dialog -->
	<div id="userCarSvcRecDialog" class="form" style="display: none;height: 600px;">
		
		<div class="ui-layout-center">
			<div class="filter section">
				<div class="filter row">
					<div class="group right aligned">
						<label class="label">订单Id</label> 
						<input id="selectOrderId" class="normal input" /> 
						<span class="spacer"></span> 
						<label class="label">加盟店名称</label> 
						<input id="selectShopName" class="normal input" /> 
						<span class="spacer"></span> 
						<button id="btnQueryCarSvc" class="normal button">查询</button>
					</div>
				</div>
			</div>
			<table id="userCarSvcRecGridCtrl"></table>
			<div id="userCarSvcRecPager"></div>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		var userCarGridCtrl = null;
		//
		var userCarGridHelper = JqGridHelper.newOne("");
		//
		var formProxy = FormProxy.newOne();
		//
		var selectMemberDialog = null;
		//
	    var memberGridHelper = JqGridHelper.newOne("");
		//
		var userCarSvcRecGridCtrl = null;
		
		//获取车辆品牌
		function loadCarBrand(callback) {
			// 隐藏页面区
			var ajax = Ajax.post("/car/carBrand/selectList/get");
			ajax.params({});
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("brandId", result.data);
					if($.isFunction(callback)){
						callback();
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		//获取车辆车系
		function loadCarSerial(id, callback) {
			// 隐藏页面区
			var ajax = Ajax.post("/car/carSerial/selectList/get");
			ajax.params({
				brandId : id
			});
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("serialId", result.data);
					if($.isFunction(callback)){
						callback();
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		//获取车辆车型
		function loadCarModel(id, callback) {
			// 隐藏页面区
			var ajax = Ajax.post("/car/carModel/selectList/get");
			ajax.params({
				serialId : id
			});
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("modelId", result.data);
					if($.isFunction(callback)){
						callback();
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
	
		//加载合作店列表数据
		function loadUserCarData(){
			var postData = {
			};
			//加载合作店
			userCarGridCtrl= $id("userCarGridCtrl").jqGrid({
			      url : getAppUrl("/car/userCar/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : 250,
				  width : "100%",
			      colNames : ["id", "车主", "车辆名称", "品牌车型", "车身颜色", "生产年份", "操作"],  
			      colModel : [{
								name : "id",
								index : "id",
								hidden : true
							},
							{
								name : "user.nickName",
								index : "user.nickName",
								width : 100,
								align : 'center',
							},
							{
								name : "name",
								index : "name",
								width : 200,
								align : 'center'
							},
							{
								name : "modelName",
								index : "modelName",
								width : 200,
								align : 'center'
							},
							{
								name : "color",
								index : "color",
								width : 100,
								align : 'center',
							},
							{
								name : "makeYear",
								index : "makeYear",
								width : 100,
								align : 'center'
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									return  "[<a class='item' href='javascript:void(0);' onclick='openShowDlg(" + cellValue + ")' >查看</a>]<span class='chs spaceholder'></span>"
										  + "[<a class='item' href='javascript:void(0);' onclick='openEditDlg(" + cellValue + ")' >编辑</a>]<span class='chs spaceholder'></span>"
										  + "[<a class='item' href='javascript:void(0);' onclick='goUserCarSvcRec(" + cellValue + ")' >服务记录</a>]<span class='chs spaceholder'></span>";
								},
								width : 200,
								align : "center"
							}],  
				loadtext: "Loading....",
				multiselect : true,// 定义是否可以多选
				multikey:'ctrlKey',
				pager : "#userCarPager",
				loadComplete : function(gridData){
					userCarGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if(isFunction(callback)){
						callback();
					}
				},
				gridComplete : function(){
				},
				ondblClickRow : function(rowId) {
					openShowDlg(rowId);
				}
			});
		}
		
		function getCallbackAfterGridLoaded() {
		}
		//
		formProxy.addField({
			id : "selectMember",
			rules : [{
				rule : function(idOrName, type, rawValue, curData) {
					var userId = $id("userId").val();
					if(!userId){
						return false;
					}
					return true;
				},
				message: "亲,您还未选择车主哦！"
			}],
			messageTargetId : "selectMember"
		});
		//
		formProxy.addField({
			id : "userId",
			messageTargetId : "userId"
		});
		//
		formProxy.addField({
			id : "name",
			required : true,
			rules : ["maxLength[30]"],
			messageTargetId : "name"
		});
		//
		formProxy.addField({
			id : "brandId",
			messageTargetId : "brandId"
		});
		//
		formProxy.addField({
			id : "serialId",
			messageTargetId : "serialId"
		});
		//
		formProxy.addField({
			id : "modelId",
			required : true,
			messageTargetId : "modelId"
		});
		//
		formProxy.addField({
			id : "modelName",
			messageTargetId : "modelName"
		});
		//
		formProxy.addField({
			id : "color",
			rules : ["maxLength[15]"],
			messageTargetId : "color"
		});
		//
		formProxy.addField({
			id : "vklIdNo",
			rules : ["maxLength[20]"],
			messageTargetId : "vklIdNo"
		});
		//
		formProxy.addField({
			id : "vklType",
			rules : ["maxLength[15]"],
			messageTargetId : "vklType"
		});
		//
		formProxy.addField({
			id : "plateNo",
			rules : ["maxLength[10]"],
			messageTargetId : "plateNo"
		});
		//
		formProxy.addField({
			id : "engineNo",
			rules : ["maxLength[15]"],
			messageTargetId : "engineNo"
		});
		//
		formProxy.addField({
			id : "makeYear",
			rules : ["isNum", "maxLength[9]"],
			messageTargetId : "makeYear"
		});
		//
		formProxy.addField({
			id : "buyYear",
			rules : ["isNum", "maxLength[9]"],
			messageTargetId : "buyYear"
		});
		//
		formProxy.addField({
			id : "buyMonth",
			rules : ["isNum", "maxLength[9]"],
			messageTargetId : "buyMonth"
		});
		//
		formProxy.addField({
			id : "price",
			rules : ["isNum", "maxLength[10]"],
			messageTargetId : "price"
		});
		//
		formProxy.addField({
			id : "mileage",
			rules : ["isNum", "maxLength[10]"],
			messageTargetId : "mileage"
		});
		
		//------------------------调整控件大小-------------------------------------------------------------------

		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "userCarGridCtrl";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("userCarPager").height();
			userCarGridCtrl.setGridWidth(mainWidth - 1);
			userCarGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 60);
		}
		//---------------------------业务---------------------------------
		//打开新增对话框
		function openAddDlg() {
			curAction = "add";
			//
			toShowTheDlg();
		}
		//打开查看对话框
		function openShowDlg(userCarId) {
			curAction = "view";
			//
			toShowTheDlg(userCarId);
		}
		//打开查看对话框
		function openEditDlg(userCarId) {
			curAction = "mod";
			//
			toShowTheDlg(userCarId);
		}
		//（真正）显示对话款
		function toShowTheDlg(dataId) {
			//
			var theDlgId = "userDlg";
			jqDlgDom = $id(theDlgId);
			//对话框配置
			var dlgConfig = {
				width : Math.min(650, $window.width()),
				height : Math.min(660, $window.height()),
				modal : true,
				open : false
			};
			//
			if (curAction == "add") {
				dlgConfig.title = "会员车辆 - 新增";
				dlgConfig.buttons = {
					"保存" : function() {
						//收集并验证要提交的数据（如果验证不通过直接返回 return）
						var vldResult = formProxy.validateAll();
						if (!vldResult) {
							return;
						}
						var postData = formProxy.getValues();
						//
						goAddUserCar(postData);
						$(this).dialog("close");
						
					},
					"取消" : function() {
						//
						jqDlgDom.prop("continuousFlag", false);
						//
						$(this).dialog("close");
						//隐藏表单验证消息
						formProxy.hideMessages();
					}
				
				};
				dlgConfig.close = function() {
					formProxy.hideMessages();	
				};
			} else if (curAction == "mod") {
				dlgConfig.title = "会员车辆 - 修改";
				dlgConfig.buttons = {
					"保存" : function() {
						//收集并验证要提交的数据（如果验证不通过直接返回 return）
						var vldResult = formProxy.validateAll();
						if (!vldResult) {
							return;
						}
						var postData = formProxy.getValues();
						//
						goEditUserCar(postData);
						$(this).dialog("close");
					},
					"取消" : function() {
						$(this).dialog("close");
						//隐藏表单验证消息
						formProxy.hideMessages();
					}
				};
			} else {
				//== view 查看
				dlgConfig.title = "会员车辆 - 查看";
				dlgConfig.buttons = {
					"修改 > " : function() {
						$(this).dialog("close");
						
						openEditDlg(dataId);
					},
					"关闭" : function() {
						$(this).dialog("close");
					}
				};
			}
			//
			jqDlgDom.dialog(dlgConfig);
			//
			initTheDlgCtrls(dataId);
		}
		
		function initTheDlgCtrls(dataId) {
			//批量简单设置
			if (curAction == "add" || curAction == "mod") {
				jqDlgDom.find(":input").prop("disabled", false);
				$("#member_tbody").find(":input").prop("disabled", true);
				$("#userAccount_tbody").find(":input").prop("disabled", true);
				$("#bizLicense_tbody").find(":input").prop("disabled", true);
			} else {
				jqDlgDom.find(":input").prop("disabled", true);
			}
			//重置控件值
			$("#userCarLogo").attr("src", "");
			$("#userDlg input[type=text]").val("");
			$("#userDlg textarea").val("");
			textSet("userCarId", "");
			//获取车辆品牌
			loadCarBrand();
			//
			if (curAction == "view" || curAction == "mod") {
				var data = userCarGridHelper.getRowData(dataId);
				if (data != null) {
					textSet("userId", data.userId);
					textSet("nickName", data.user.nickName);
					textSet("phoneNo", data.user.phoneNo);
					textSet("userCarId", data.id);
					textSet("name", data.name);
					textSet("color", data.color);
					textSet("vklIdNo", data.vklIdNo);
					textSet("vklType", data.vklType);
					textSet("plateNo", data.plateNo);
					textSet("engineNo", data.engineNo);
					textSet("makeYear", data.makeYear);
					textSet("buyYear", data.buyYear);
					textSet("buyMonth", data.buyMonth);
					textSet("price", data.price);
					textSet("mileage", data.mileage);
					//获取车辆品牌
					loadCarBrand(function(){
						textSet("brandId", data.brandId);
					});
					//获取车辆系列
					loadCarSerial(data.brandId, function(){
						textSet("serialId", data.serialId);
					});
					//获取车辆类型
					loadCarModel(data.serialId, function(){
						textSet("modelId", data.modelId);
					});
				}
			}else{
				loadSelectData("serialId", "");
				loadSelectData("modelId", "");
			}
			
			// 根据品牌加载车系
			$("#brandId").change(function() {
				var brandId = textGet("brandId");
				loadCarSerial(brandId);
			});
			
			// 根据车系加载车型
			$("#serialId").change(function() {
				var serialId = textGet("serialId");
				loadCarModel(serialId);
			});
			
		}
		
		function goAddUserCar(userCarInfoMap){
			var hintBox = Layer.progress("正在保存数据...");
			
			var ajax = Ajax.post("/car/userCar/add/do");
			//
			userCarInfoMap["modelName"] = $("#brandId option:selected").text() + $("#serialId option:selected").text() + $("#modelId option:selected").text();
			
			ajax.data(userCarInfoMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					
					userCarGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
					//
					getCallbackAfterGridLoaded = function(){
						
					};
					
				} else {
					Layer.msgWarning(result.message);
				}

			});
			ajax.always(function() {
				hintBox.hide();
			});
			
			ajax.go();
		}
		
		function goEditUserCar(userCarInfoMap){
			var hintBox = Layer.progress("正在保存数据...");
			
			var ajax = Ajax.post("/car/userCar/edit/do");
			//
			userCarInfoMap["modelName"] = $("#brandId option:selected").text() + $("#serialId option:selected").text() + $("#modelId option:selected").text();
			userCarInfoMap["id"] = $id("userCarId").val();
			
			ajax.data(userCarInfoMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					
					userCarGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
					//
					getCallbackAfterGridLoaded = function(){
						
					};
					
				} else {
					Layer.msgWarning(result.message);
				}

			});
			ajax.always(function() {
				hintBox.hide();
			});
			
			ajax.go();
		}
		
		//更改车辆品牌 禁用 开启状态
		function openOrClose(id) {
			var userCarMap = userCarGridHelper.getRowData(id)
			var postData = {
				id : userCarMap.id,
				disabled : userCarMap.disabled
			};
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/car/userCar/isDisabled/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					userCarGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
					Layer.msgSuccess(result.message);
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.always(function() {
				//隐藏等待提示框
				loaderLayer.hide();
			});
			ajax.go();
		}
		
		function goSelectMember(){
			
			//绑定查询按钮
			$id("btnQueryMember").click(function() {
				var filter = {
				};
				var nickName = textGet("selectName");
				if(nickName){
					filter.nickName = nickName;
				}
				var phoneNo = textGet("selectPhoneNo");
				if(phoneNo){
					filter.phoneNo = phoneNo;
				}
				memberGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			});
			selectMemberDialog.dialog("open");
			// 页面自适应
			adjustCtrlsSize();
		}
		
		//初始化选择会员Dialog
		function initSelectMemberDialog(){
			selectMemberDialog = $( "#selectMemberDialog" ).dialog({
		        autoOpen: false,
		        width : Math.min(800, $window.width()),
		        height : Math.min(480, $window.height()),
		        modal: true,
		        title : '选择车主',
		        buttons: {
		            "确定": function(){
		            	var memberId = getCheckedMember();
		            	showSelectMemberInfo(memberId);
		            	//
		    			var postData = {
		    					userId : memberId
		    			};
		            	//
		            	memberGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(postData,true)},page:1}).trigger("reloadGrid");
		    			
		            	selectMemberDialog.dialog("close");
		            },
		            "关闭": function() {
		            	selectMemberDialog.dialog( "close" );
		          }
		        },
		        close: function() {
		        	selectMemberDialog.dialog( "close" );
		        }
		      });
		}
		
		//加载商户列表数据
		function loadMemberData(){
			var postData = {
			};
			//加载商户
			memberGridCtrl= $("#memberGrid").jqGrid({
			      url : getAppUrl("/member/mall/list/by/page/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : 240,
				  width : "100%",
			      colNames : ["id", "会员昵称", "手机号", "身份证","邮箱","操作"],  
			      colModel : [{name:"id", index:"id", width:150,align : 'center', hidden : true},
			                  {name:"user.nickName", index:"user.nickName",width:120,align : 'left',},
			                  {name:"user.phoneNo", index:"user.phoneNo",width:130,align : 'left',},
			                  {name:"user.idCertNo", index:"user.idCertNo",width:200,align : 'left'},
			                  {name:"user.email", index:"user.email",width:190,align : 'left'},
			                  {name:"action", index:"action", width : 100, align : "center"} 
							],  
				loadtext: "Loading....",
				multiselect : false,// 定义是否可以多选
				multikey:'ctrlKey',
				pager : "#memberPager",
				loadComplete : function(gridData){
					memberGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if(isFunction(callback)){
						callback();
					}
				},
				gridComplete : function(){
					var ids = memberGridCtrl.jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						id = ParseInt(id);
						memberGridCtrl.jqGrid('setRowData', id, {
							action : "<input type='radio' name='member' value='"+id+"'/>"
						});
					}
				}
			});
		}
		
		function getCheckedMember(){
			var memberId = null;
			$("input:radio[name='member']").each(function(){
				memberId = $(this).val();
				var isChecked = $(this).prop("checked");
				if(isChecked){
					return false;
				}
			});
			return memberId;
		}
		
		function showSelectMemberInfo(id){
			if(id){
				$id("userId").val(id);
				var member = memberGridHelper.getRowData(id);
				if(member.user != null){
					$id("nickName").val(member.user.nickName);
					$id("phoneNo").val(member.user.phoneNo);
				}
				
			}
		}
		
		function goUserCarSvcRec(id){
			var filter = {
					carId : id
				};
			//绑定查询按钮
			$id("btnQueryCarSvc").click(function() {
				var filterStr = {
					carId : id	
				};
				var orderId = textGet("selectOrderId");
				if(orderId){
					filterStr.orderId = orderId;
				}
				var shopName = textGet("selectShopName");
				if(shopName){
					filterStr.shopName = shopName;
				}
				userCarSvcRecGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filterStr,true)},page:1}).trigger("reloadGrid");
			});
			userCarSvcRecGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			userCarSvcRecDialog.dialog("open");
		}
		
		//初始化车辆服务记录Dialog
		function initUserCarSvcRecDialog(){
			userCarSvcRecDialog = $( "#userCarSvcRecDialog" ).dialog({
		        autoOpen: false,
		        width : Math.min(800, $window.width()),
		        height : Math.min(480, $window.height()),
		        modal: true,
		        title : '车辆服务记录',
		        buttons: {
		            "关闭": function() {
		            	userCarSvcRecDialog.dialog( "close" );
		          }
		        },
		        close: function() {
		        	userCarSvcRecDialog.dialog( "close" );
		        }
		})};
		
		
		//加载车辆服务记录列表数据
		function loadUserCarSvcRecData(){
			var postData = {
			};
			//加载合作店
			userCarSvcRecGridCtrl= $id("userCarSvcRecGridCtrl").jqGrid({
			      url : getAppUrl("/car/userCarSvcRec/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : 250,
				  width : "100%",
			      colNames : ["id", "订单Id", "服务时间", "服务门店", "服务内容"],  
			      colModel : [{
								name : "id",
								index : "id",
								hidden : true
							},
							{
								name : "orderId",
								index : "orderId",
								width : 100,
								align : 'center'
							},
							{
								name : "dateVal",
								index : "dateVal",
								width : 114,
								align : 'center',
							},
							{
								name : "shopName",
								index : "shopName",
								width : 200,
								align : 'center'
							},
							{
								name : "svcNames",
								index : "svcNames",
								width : 300,
								align : 'center',
							}
							],  
				loadtext: "Loading....",
				multiselect : true,// 定义是否可以多选
				multikey:'ctrlKey',
				pager : "#userCarSvcRecPager",
				gridComplete : function(){
				},
			});
		}
		
		//---------------------------业务---------------------------------
		$(function() {
			//页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 56,
				allowTopResize : false
			});
			//隐藏布局north分割线
			$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
			//
			loadUserCarData();
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
			//添加车辆品牌
			$id("btnAdd").click(openAddDlg);
			//上传Logo
			initFileUpload("fileToUploadFileLogo");
			//绑定修改模块上传按钮
			$id("btnfileToUploadFile").click(function() {
				fileToUploadFileIcon("fileToUploadFileLogo");
			});
			//加载会员
			loadMemberData()
			//
			initSelectMemberDialog();
			//选择车主
			$id("selectMemberBtn").click(goSelectMember);
			//车辆服务记录
			initUserCarSvcRecDialog();
			//车辆服务记录列表
			loadUserCarSvcRecData();
			//绑定查询按钮
			$id("btnToQuery").click(function() {
				
				var filter = {};
				var name = textGet("queryName");
				if(name){
					filter.name = name;
				}
				
				//加载jqGridCtrl
				userCarGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			});
		});
	</script>
</body>
</html>