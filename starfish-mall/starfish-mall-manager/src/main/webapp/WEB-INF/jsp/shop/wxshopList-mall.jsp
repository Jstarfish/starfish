<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>卫星店铺管理</title>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button class="button"  style="width: 100px;" onclick="IndexDistShopDocs();" >启动索引</button>
					<%-- button id="btnDelShops" class="button">批量删除</button>--%>
					<!--<button class="button" id="btnAuditShops" style="width: 100px;">批量审核</button>-->
				</div>
				<button id="btnBatchImp" class="button" style="display: none">批量导入</button>
				<button id="btnBatchExp" class="button" style="display: none">批量导出</button>
				<div class="group right aligned">
					<label class="label">店铺名称</label> <input id="queryShopName"
						class="normal input" /> <span class="spacer"></span> <label
						class="label">店铺状态</label> <select id="queryShopStatus"
						class="normal input">
						<option >全部</option>
						<option value="0">启用</option>
						<option value="1">禁用</option>
						<option value="2">待审核</option>
						<option value="3">审核通过</option>
						<option value="4">审核不通过</option>
					</select> <span class="vt divider"></span>
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
						<label class="field label required one half wide">店铺名称</label> 
						<input type="text" id="name" class="field value one half wide" />
					</div>
					<div class="field row">
						<label class="field label required one half wide">店铺Logo</label> 
						<input name="file" type="file" id="fileToUploadFileLogo" multiple="multiple" class="field value one half wide"  />
						<button class="normal button" id="btnfileToUploadFile">上传</button>
					</div>
					<div class="field row" style="height: 80px">
						<input type="hidden"  id="logoUuid" />
						<input type="hidden"  id="logoUsage" />
						<input type="hidden"  id="logoPath" />
						<label class="field label one half wide">图片预览</label>
			        	<img id="wxShopLogo" height="80px" width="120px" src="<%=resBaseUrl%>/image-app/img-none.jpg"/>
					</div>
					<div class="field row" style="margin-top: 8px">
						<label class="field label required one half wide">联系人</label>
						<input type="text" id="linkMan" class="field value one half wide" />
					</div>
					<div class="field row">
						<label class="field label required one half wide">联系电话</label>
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
						<label class="field label one half wide required">所在地</label>
						<select class="field value" id="province"></select>
						<select class="field value" id="city"></select>
						<select class="field value" id="county"></select>
						<select class="field value" id="town"></select>
					</div>
					<div class="field row">
						<label class="field label one half wide required">街道地址</label>
						<input type="text" id="street" class="field value three wide" maxlength="60" title="请输入街道地址" />
					</div>
					<span id="divider" class="normal hr divider"></span>
					<div id="bizLicenseList" class="noBorder" style="margin-left: 20px">
						<div style="margin: 10px 0;">
							<div class="group left aligned" style="float: left;">
								<label style="font-size: 14px;color: #666;" id="selectShop"><span style="color: red"> * </span>隶属加盟店</label>
							</div>
							<div class="group right aligned" style="float: right;">
								<button id="selectShopBtn" class="normal button">选择</button>
							</div>
						</div>
						<div style="text-align: center; vertical-align: middle;">
							<table class="gridtable" style="width: 100%;">
								<thead></thead>
								<tbody id="shop_tbody">
									<tr class="field row">
										<td align="right"><label class="field label one half wide" style="float: right;">加盟店名称</label></td>
										<td align="left">
											<input type="hidden" id="shopId" />
											<input type="text" id="shopName" class="field value two wide" />
										</td>
									</tr>
									<tr class="field row">
										<td align="right"><label class="field label one half wide" style="float: right;">店铺logo</label></td>
										<td align="left">
			        						<img id="shopLogo" height="80px" width="120px" src="<%=resBaseUrl%>/image-app/img-none.jpg"/>
										</td>
									</tr>
									<tr class="field row">
										<td align="right"><label class="field label one half wide" style="float: right;">联系人</label></td>
										<td align="left">
											<input type="text" id="shopLinkMan" class="field value two wide" />
										</td>
									</tr>
									<tr class="field row">
										<td align="right"><label class="field label one half wide" style="float: right;">联系人电话</label></td>
										<td align="left">
											<input type="text" id="shopTelNo" class="field value two wide" />
										</td>
									</tr>
									<tr class="field row">
										<td align="right"><label class="field label one half wide" style="float: right;">地址</label></td>
										<td align="left">
											<input type="text" id="shopRegionName" class="field value two wide" />
										</td>
									</tr>
									<tr class="field row">
										<td align="right"><label class="field label one half wide" style="float: right;">营业范围</label></td>
										<td align="left">
											<textarea id="shopBizScope" class="field value three wide" style="height:50px;"></textarea>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div id="shopList" class="noBorder">
				
				</div>
			</div>
		</div>
		<!--选择商户dialog -->
		<div id="selectShopDialog" class="form" style="display: none;height: 600px;">
			
			<div class="ui-layout-center">
				<div class="filter section">
					<div class="filter row">
						<div class="group right aligned">
							<label class="label">店铺名称</label> 
							<input id="selectShopName" class="normal input" /> 
							<span class="spacer"></span> 
							<button id="btnQueryShop" class="normal button">查询</button>
						</div>
					</div>
				</div>
				<table id="shopGrid"></table>
				<div id="shopPager"></div>
			</div>
		</div>
		<div id="dialog_auditShops" title="批量审核店铺" style="display: none;">
			<div class="form">
				<div class="field row">
					<label class="field label one half wide">是否通过</label>
					<div class="field group">
						<input id="bathshopAuditStatus-Y" type="radio"
							name="bathshopAuditStatus" value="2" checked="checked"/> <label
							for="bathshopAuditStatus-Y">通过审核</label> <input
							id="bathshopAuditStatus-N" type="radio" name="bathshopAuditStatus"
							value="-1" /> <label for="bathshopAuditStatus-N">审核拒绝</label>
					</div>
				</div>
				<div class="field row" style="height: 100px;">
					<label class="field label one half wide">备注</label>
					<textarea id="bathshopAuditDesc" class="field value two wide"
						style="height: 80px;"></textarea>
				</div>
			</div>
		</div>
		<div id="auditShopDialog" title="审核店铺" style="display: none;">
			<div class="form">
				<div class="field row">
					<input type="hidden" id="shopAuditHiddenId" />
					<input type="hidden" id="shopAuditHiddenName" />
					<label
						class="field label one half wide">是否通过</label>
					<div class="field group">
						<input id="shopAuditStatus-Y" type="radio" name="shopAuditStatus" value="2" checked="checked"/> 
							<label for="shopAuditStatus-Y">通过审核</label> 
						<input id="shopAuditStatus-N" type="radio" name="shopAuditStatus" value="-1" /> 
							<label for="shopAuditStatus-N">审核拒绝</label>
					</div>
				</div>
				<div class="field row" style="height: 100px;">
					<label class="field label one half wide">备注</label>
					<textarea id="shopAuditDesc" class="field value two wide"
						style="height: 80px;"></textarea>
				</div>
			</div>
		</div>
		<div id="batchImpDialog" style="display: none;">
			<div class="form bordered">
				<div class="field row">
					<label class="field label">文件</label> <input
						class="field value two wide" name="file" type="file"
						id="impMerchXls" multiple="multiple" />
				</div>
			</div>
		</div>
		<!--合作店统计dialog -->
		<div id="selectDistShopStatisDialog" style="display: none;">
			<div class="ui-layout-center">
				<div class="filter section">
					<div class="filter row">
						<label class="label">服务次数：</label><span id="statisSvcCount">0</span><br />
						<label class="label">代理订单：</label><span id="statisAgentCount">0</span><br />
						<label class="label">承接订单：</label><span id="statisAllocateCount">0</span><br />
						<label class="label">收入金额：</label><span id="statisIncomeAmount">0</span>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js?<%=System.currentTimeMillis()%>"></script>
	<script type="text/javascript">
		//
		var shopGridCtrl = null;
		// 商店当前操作
		var shopGridHelper = JqGridHelper.newOne("");
		//
		var selectBizLicenseDialog = null;
		//
		var isChangeImgData=false;
		//
		var auditShopDialog;
		//
		var auditIds = "";
		//批量审核窗口
		var dialog_auditShops;
		//
		var distShopGridCtrl = null;
		//
		$id("mainPanel").jgridDialog(
			{
				dlgTitle : "卫星店",
				addUrl : "/distshop/wxshop/add/do",
				updUrl : "/distshop/wxshop/update/do",
				delUrl : "/distshop/wxshop/delete/do",
				//isUsePageCacheToView:false,
				isShowViewShowModBtn: false,
				jqGridGlobalSetting:{
					multiselect : true,
					url : getAppUrl("/distshop/wxshop/list/get"),
					colNames : [ "id", "店铺名称", "联系人", "联系电话", "所在地", "店铺状态", "操作", "" ],
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
									if (cellValue == 0) {
										return '未审核';
									} else if (cellValue == 1) {
										return "审核通过";
									} else if (cellValue == 2) {
										if (rowObject.disabled == false) {
											return "启用";
										} else {
											return "禁用";
										}
									} else {
										return '审核未通过';
									}
								}
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									// if (rowObject.auditStatus == 0 || rowObject.auditStatus == -1) {
									//	return "[<a class='item dlgAuditview' href='javascript:void(0);' cellValue='" + cellValue + "' >审核</a>]";
									//} else if (rowObject.auditStatus == 1) {
									//	return "[<a class='item dlgAuditview' href='javascript:void(0);' cellValue='" + cellValue + "' >审核</a>]";
									//} else if (rowObject.auditStatus == 2) {
									//	var shopDo = "[<a class='item dlgview' href='javascript:void(0);' cellValue='" + cellValue + "'>查看</a>]<span class='chs spaceholder'></span>";
									//	shopDo = shopDo + "[<a class='item dlgStatisview' href='javascript:void(0);' cellValue='" + cellValue + "'>统计</a>]<span class='chs spaceholder'></span>";
									//	if (rowObject.disabled == false) {
									//		return shopDo + "[<a class='item dlgDisableview' href='javascript:void(0);' cellValue= '" + cellValue + "' >禁用</a>]";
									//	} else {
									//		return shopDo + "[<a class='item dlgDisableview' href='javascript:void(0);' cellValue='" + cellValue + "' >开启</a>]";
									//	}
									//} else {
									//	return '审核未通过';
									//}
									return "[<a class='item dlgview' href='javascript:void(0);' cellValue='" + cellValue + "'>查看</a>]";
								},
								width : 200,
								align : "center"
							}, {
								name : "auditStatus",
								index : "auditStatus",
								hidden : true,
								formatter : function(cellValue, option, rowObject) {
									if (cellValue == 0) {
										return '0';
									} else if (cellValue == 1) {
										return "1";
									} else if (cellValue == 2) {
										return "2";
									} else {
										return '1';
									}
								}
							}, ],
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
					intSet("wxshopId", null);
					textSet("name", "");
					$id("wxShopLogo").attr("src", "");
					textSet("linkMan", "");
					textSet("telNo", "");
					textSet("phoneNo", "");
					textSet("lng", "");
					textSet("lat", "");
					textSet("street", "");
					textSet("shopId", "");
					textSet("shopName", "");
					$id("shopLogo").attr("src", "");
					textSet("shopLinkMan", "");
					textSet("shopTelNo", "");
					textSet("shopRegionName", "");
					textSet("shopBizScope", "");
					// 加载地区
					bindRegionLists("province", "city", "county", "town");
					clearSelectData("city");
					clearSelectData("county");
					clearSelectData("town");
					$("#shop_tbody input").attr("disabled",true);
					$("#shop_tbody textarea").attr("disabled",true);
				},
				 modAndViewInit:function (data) {
					intSet("wxshopId", data.id);
					textSet("name", data.name);
					$id("wxShopLogo").attr("src", data.fileBrowseUrl);
					textSet("linkMan", data.linkMan);
					textSet("telNo", data.telNo);
					textSet("phoneNo", data.phoneNo);
					textSet("lng", data.lng);
					textSet("lat", data.lng);
					textSet("street", data.street);
					textSet("shopId", data.shopId);
					textSet("shopName", data.shop.name);
					$id("shopLogo").attr("src", data.shop.fileBrowseUrl);
					textSet("shopLinkMan", data.shop.linkMan);
					textSet("shopTelNo", data.shop.phoneNo);
					textSet("shopRegionName", data.shop.regionName);
					textSet("shopBizScope", data.shop.bizScope);
					// 加载地区
					bindRegionLists("province", "city", "county", "town", [ data.provinceId, data.cityId, data.countyId, data.townId ]);
					
					$("#shop_tbody input").attr("disabled",true);
					$("#shop_tbody textarea").attr("disabled",true);
				},
				queryParam : function(postData,formProxyQuery) {
					var name = textGet("queryShopName");
					if (name != "") {
						postData['name'] = name;
					}
					
					var disabled = singleGet("queryShopStatus");
					if(disabled && disabled != "全部"){
						if(disabled < 2){
							postData['disabled'] = disabled;	
						} else {
							if(disabled == 4){
								postData['auditStatus'] = -1;	
							}else{
								postData['auditStatus'] = disabled - 2;	
							}
							
						}
						
					}
				},
				formProxyInit : function(formProxy) {
					// 注册表单控件
					formProxy.addField({
						id : "name",
						key : "name",
						required : true,
						rules : ["maxLength[30]"],
						messageTargetId : "name"
					});
					formProxy.addField({
						id : "wxShopLogo",
						key : "name",
						rules : [{
							rule : function(idOrName, type, rawValue, curData) {
								var img = $id("wxShopLogo");
								var imgSrc = $(img).attr("src");
								var defaultImg = "img-none.jpg";
								
								var isSearch = strContains(imgSrc, defaultImg, false);
								if(isSearch){
									return false;
								}
								return true;
							},
							message: "亲,您还未上传店面图片哦！"
						}],
						messageTargetId : "wxShopLogo"
					});
					formProxy.addField({
						id : "linkMan",
						required : true,
						rules : ["maxLength[30]"],
						messageTargetId : "linkMan"
					});
					formProxy.addField({
						id : "telNo",
						required : true,
						rules : ["maxLength[15]", "isMobile"],
						messageTargetId : "telNo"
					});
					formProxy.addField({
						id : "phoneNo",
						rules : ["maxLength[15]", "isMobile"],
						messageTargetId : "phoneNo"
					});
					formProxy.addField({
						id : "lng",
						rules : ["maxLength[20]", "isDigitsOrHyphen", {
							rule : function(idOrName, type, rawValue, curData) {
								// 区若显示且为空，给予提示
								if (-180 > rawValue || rawValue > 180) {
									return false;
								}
				
								return true;
							},
							message : "您填写的经度不正确！"
						} ],
						messageTargetId : "lng"
					});
					formProxy.addField({
						id : "lat",
						rules : ["maxLength[20]", "isDigitsOrHyphen", {
							rule : function(idOrName, type, rawValue, curData) {
								// 区若显示且为空，给予提示
								if (-90 > rawValue || rawValue > 90) {
									return false;
								}
				
								return true;
							},
							message : "您填写的纬度不正确！"
						} ],
						messageTargetId : "lat"
					});
					//
					formProxy.addField({
						id : "province",
						key : "province",
						required : true,
						messageTargetId : "province"
					});
					//
					formProxy.addField({
						id : "city",
						key : "city",
						required : true,
						messageTargetId : "city"
					});
					//
					formProxy.addField({
						id : "county",
						key : "county",
						required : true,
						messageTargetId : "county"
					});
					//
					formProxy.addField({
						id : "town",
						key : "town",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 区若显示且为空，给予提示
								if (isNoB(rawValue)) {
									return false;
								}
				
								return true;
							},
							message : "此为必须提供项！"
						} ],
						messageTargetId : "town"
					});
					// 注册表单控件
					formProxy.addField({
						id : "street",
						key : "street",
						required : true,
						rules : ["maxLength[30]"],
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
					if(isChangeImgData){
						postData["ownerShopId"]=$id("shopId").val();
						postData["logoUuid"]=$id("logoUuid").val();
						postData["logoUsage"]=$id("logoUsage").val();
						postData["logoPath"]=$id("logoPath").val();
						postData["regionId"] = intGet("town");
					}
				},
				pageLoad : function(jqGridCtrl, cacheDataGridHelper) {
					distShopGridCtrl=this;
					//上传logo图片
					initFileUpload("fileToUploadFileLogo");
					//绑定修改模块上传按钮
					$id("btnfileToUploadFile").click(function(){
						fileToUploadFileIcon("fileToUploadFileLogo");
					});
					//
					loadMerchantData();
					//
					initShopDialog();
					//选择加盟店
					$id("selectShopBtn").click(function(){
						isChangeImgData=true;
						//查询卫星店铺
						$id("btnQueryShop").click(function() {
							var queryfilter = {
								auditStatus : 2,
								disabled : false
							};
							var name = $id("selectShopName").val();
							if(name){
								queryfilter.name = name;
							}
							//加载shopGridCtrl
							shopGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(queryfilter,true)},page:1}).trigger("reloadGrid");
						});
						selectShopDialog.dialog( "open" );
					});
					//
					$(document).on("click", ".dlgDisableview", function() {
						openOrClose($(this).attr("cellValue"), cacheDataGridHelper);
					});
					//
					$(document).on("click", ".dlgAuditview", function() {
						openAuditDialog($(this).attr("cellValue"), cacheDataGridHelper);
					});
					
					
					//绑定批量审核按钮
					$id("btnAuditShops").click(function() {
						var ids = jqGridCtrl.jqGrid("getGridParam", "selarrrow");
						if (ids == "") {
							Layer.msgWarning("请选中要审核的店铺");
							return;
						}
						for (var i = 0; i < ids.length; i++) {
							var rowData = jqGridCtrl.jqGrid("getRowData", ids[i]);//根据上面的id获得本行的所有数据
							var val = rowData["auditStatus"]; //获得制定列的值 （auditStatus 为colModel的name）
							if ("2" == val) {
								Layer.msgWarning("包含已经审核通过的店铺");
								return;
							}
						}
						auditIds = ids;
						initDialog();
						dialog_auditShops.dialog("open");
					});
					
					// 合作店统计信息
					initDistShopStatisDialog();
					$(document).on("click", ".dlgStatisview", function() {
						loadDistshopStatisData($(this).attr("cellValue"));
					});
					
				}
			});
		
		//上传
		function fileToUploadFileIcon(fileId) {
			var fileUuidToUpdate = $id("logoUuid").val();
			if (isNoB(fileUuidToUpdate)) {
				var formData = {
					usage : "image.shop",
				};
			} else {
				var formData = {
					update : true,
					uuid : fileUuidToUpdate
				};
			}
			
			sendFileUpload(fileId, {
				url : getAppUrl("/file/upload"),
				dataType : "json",
				//自定义数据
				formData : formData,
				done : function(e, result) {
					var resultInfo = result.result;
					if (resultInfo.type == "info") {
						$id("logoUuid").val(resultInfo.data.files[0].fileUuid);
						$id("logoUsage").val(resultInfo.data.files[0].fileUsage);
						$id("logoPath").val(resultInfo.data.files[0].fileRelPath);
						$id("wxShopLogo").attr("src",resultInfo.data.files[0].fileBrowseUrl + "?" + new Date().getTime());
					}
				},
				fail : function(e, data) {
					console.log(data);
				},
				noFilesHandler : function() {
					Layer.msgWarning("请选择或更换图片");
				},
				fileNamesChecker : function(fileNames) {
					fileNames = fileNames || [];
					for (var i = 0; i < fileNames.length; i++) {
						if (!isImageFile(fileNames[i])) {
							Layer.msgWarning("只能上传图片文件");
							return false;
						}
					}
					return true;
				}
			});
		}
		
		function IndexDistShopDocs(){
			var ajax = Ajax.post("/distshop/doc/index/do");
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess("操作成功!");
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		//加载加盟店列表数据
		function loadMerchantData(){
			var postData = {
				auditStatus : 2,
				disabled : false
			};
			//加载加盟店
			shopGridCtrl= $id("shopGrid").jqGrid({
			      url : getAppUrl("/shop/shop/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : 250,
				  width : "100%",
			      colNames : ["id", "店铺名称", "所在地", "所属商户", "店铺状态", " 操作"],  
			      colModel : [{
								name : "id",
								index : "id",
								hidden : true
							},
							{
								name : "shop.name",
								index : "shop.name",
								width : 200,
								align : 'left',
							},
							{
								name : "shop.regionName",
								index : "shop.regionName",
								width : 200,
								align : 'left'
							},
							{
								name : "shop.merchantName",
								index : "shop.merchantName",
								width : 100,
								align : 'left'
							},
							{
								name : "shop.auditStatus",
								index : "shop.auditStatus",
								width : 100,
								align : "center",
								formatter : function(cellValue, option, rowObject) {
									if (cellValue == 0) {
										return '未审核';
									} else if (cellValue == 1) {
										return "初审通过";
									} else if (cellValue == 2) {
										if (rowObject.shop.disabled == false) {
											return "启用";
										} else {
											return "禁用";
										}
									} else {
										return '审核未通过';
									}
								}
							},
			                  {name:"action", index:"action", width : 100, align : "center"} 
							],  
				loadtext: "Loading....",
				multiselect : false,// 定义是否可以多选
				multikey:'ctrlKey',
				pager : "#shopPager",
				loadComplete : function(gridData){
					shopGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if(isFunction(callback)){
						callback();
					}
				},
				gridComplete : function(){
					var ids = shopGridCtrl.jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						id = ParseInt(id);
						shopGridCtrl.jqGrid('setRowData', id, {
							action : "<input type='radio' name='shop' value='"+id+"'/>"
						});
					}
				}
			});
		}
		
		function getCallbackAfterGridLoaded() {

		}
		
		//初始化选择企业营业执照Dialog
		function initShopDialog(){
			selectShopDialog = $( "#selectShopDialog" ).dialog({
		        autoOpen: false,
		        width : Math.min(760, $window.width()),
		        height : Math.min(500, $window.height()),
		        modal: true,
		        title : '选择加盟店',
		        buttons: {
		        	"确定": function(){
		            	var shopId = getCheckedShop();
		            	showSelectShopInfo(shopId);
		            	selectShopDialog.dialog("close");
		            },
		            "关闭": function() {
		            	selectShopDialog.dialog( "close" );
		          }
		        },
		        close: function() {
		        	selectShopDialog.dialog( "close" );
		        }
		      });
		}
		
		function getCheckedShop(){
			var shopId = null;
			$("input:radio[name='shop']").each(function(){
				shopId = $(this).val();
				var isChecked = $(this).prop("checked");
				if(isChecked){
					return false;
				}
			});
			return shopId;
		}
		
		function showSelectShopInfo(id){
			if(id){
				$id("shopId").val(id);
				var shop = shopGridHelper.getRowData(id).shop;
				if(shop != null){
					$id("shopName").val(shop.name);
					$id("shopLogo").attr("src", shop.fileBrowseUrl);
					$id("shopLinkMan").val(shop.linkMan);
					$id("shopTelNo").val(shop.phoneNo);
					$id("shopRegionName").val(shop.regionName);
					$id("shopBizScope").val(shop.bizScope);
				}
			}
		}
		
		//更改店铺 禁用 开启状态
		function openOrClose(id, cacheDataGridHelper) {
			var wxshopMap = cacheDataGridHelper.getRowData(id)
			var postData = {
				id : wxshopMap.id,
				disabled : wxshopMap.disabled
			};
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/distshop/wxShop/isDisabled/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					distShopGridCtrl.reloadGridData();
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
		
		//弹出审核框（单）
		function openAuditDialog(shopId, cacheDataGridHelper) {
			$("input[name='shopAuditStatus'][value='2']").attr("checked", "checked");
			var distShopMap = cacheDataGridHelper.getRowData(shopId)
			$id("shopAuditHiddenId").val(shopId);
			$id("shopAuditHiddenName").val(distShopMap.name);
			//$id("shopAuditHiddenPhoneNo").val(shopMap.merchant.user.phoneNo);
			initAuditShopDialog();
			auditShopDialog.dialog("open");
		}
		
		function initAuditShopDialog() {
			auditShopDialog = $("#auditShopDialog").dialog({
				autoOpen : false,
				height : Math.min(300, $window.height()),
				width : Math.min(450, $window.width()),
				modal : true,
				title : '审核店铺',
				buttons : {
					"确定" : function() {
						auditShop();
					},
					"取消" : function() {
						auditShopDialog.dialog("close");
					}
				},
				close : function() {
					$("input[name='shopAuditStatus'][value='2']").attr("checked", "checked");
					$id("shopAuditDesc").val("");
					$id("shopAuditHiddenId").val("");
					$id("shopAuditHiddenName").val("");
				}
			});
		}
		
		//审核店铺
		function auditShop() {
			var postData = {
				id : $id("shopAuditHiddenId").val(),
				shopName : $id("shopAuditHiddenName").val(),
				auditStatus : $("input[name='shopAuditStatus']:checked").val(),
				auditorDesc : $id("shopAuditDesc").val()
			};
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/distshop/wxShop/audit/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					distShopGridCtrl.reloadGridData();
					auditShopDialog.dialog("close");
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
		};
		//批量审核店铺
		function auditShops() {
			var postData = {
				ids : auditIds,
				auditStatus : $("input[name='bathshopAuditStatus']:checked").val(),
				auditorDesc : $id("bathshopAuditDesc").val()
			};
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/distshop/wxShops/audit/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					auditIds = "";
					distShopGridCtrl.reloadGridData();
					dialog_auditShops.dialog("close");
					Layer.msgSuccess(result.message);
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.always(function() {
				auditIds = "";
				//隐藏等待提示框
				loaderLayer.hide();
			});
			ajax.go();
		}
		
		function initDialog() {
			//初始化店铺审核Dialog
			dialog_auditShops = $("#dialog_auditShops").dialog({
				autoOpen : false,
				height : Math.min(300, $window.height()),
				width : Math.min(450, $window.width()),
				modal : true,
				buttons : {
					"确定" : function() {
						auditShops();
					},
					"取消" : function() {
						dialog_auditShops.dialog("close");
					}
				},
				close : function() {
					$("input[name='bathshopAuditStatus'][value='2']").attr("checked", "checked");
					$id("bathshopAuditDesc").val("");
				}
			});
		}
		
		//初始化合作店统计Dialog
		function initDistShopStatisDialog(){
			selectDistShopStatisDialog = $( "#selectDistShopStatisDialog" ).dialog({
		        autoOpen: false,
		        width : Math.min(600, $window.width()),
		        height : Math.min(300, $window.height()),
		        modal: true,
		        title : '合作店统计',
		        buttons: {
		            "关闭": function() {
		            	selectDistShopStatisDialog.dialog( "close" );
		          }
		        },
		        close: function() {
		        	selectDistShopStatisDialog.dialog( "close" );
		        }
			});
		}
		
		// 加载合作店统计信息
		function loadDistshopStatisData(distributorId){
			var hintBox = Layer.progress("正在获取统计数据...");
			var ajax = Ajax.post("/distshop/statis/count/get");
			ajax.data({
				distributorId: distributorId
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var statis = result.data;
					var incomeAmount = statis.incomeAmount || 0;
					$id(statisAllocateCount).html(statis.allocateCount || 0);
					$id(statisAgentCount).html(statis.agentCount || 0);
					$id(statisIncomeAmount).html(incomeAmount.toFixed(2));
					$id(statisSvcCount).html(statis.svcCount || 0);
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.always(function() {
				hintBox.hide();
				selectDistShopStatisDialog.dialog( "open" );
			});
			ajax.go();
		}
	</script>
</body>
<script type="text/html" id="bizLicenseTpl" title="店铺等级信息模版">
		
</script>
</html>