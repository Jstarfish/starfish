<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>店铺管理</title>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnAdd" class="button">添加</button>
					<%-- button id="btnDelShops" class="button">批量删除</button>--%>
					<button class="button" id="btnAuditShops" style="width: 100px;">批量审核</button>
					<button class="button"  style="width: 100px;" onclick="IndexShopDocs();" >启动索引</button>
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
						<option value="3">初审通过</option>
						<option value="4">终审通过</option>
						<option value="5">审核未通过</option>
					</select> <span class="vt divider"></span>
					<button id="btnQueryShop" class="normal button">查询</button>
				</div>
			</div>
		</div>
		
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
		
		<!-- 	隐藏Dialog -->
		<div id="userDlg" style="display: none;">
			<div id="addForm" class="form">
				<div id="userList" class="noBorder" style="margin-left: 20px;">
					<div style="margin: 10px 0;">
						<div class="group left aligned" style="float: left;">
							<label  style="font-size: 14px;color: #666;" id="selectMerchant"><span style="color: red"> * </span>商户信息</label>
						</div>
						<div class="group right aligned" style="float: right;">
							<button id="selectMerchantBtn" class="normal button">选择</button>
						</div>
					</div>
					<div style="text-align: center; vertical-align: middle;">
						<table class="gridtable" style="width: 100%;">
							<thead></thead>
							<tbody id="merchant_tbody">
								<tr class="field row">
									<td align="right" width="18%"><label class="field label one half wide" style="float: right;">管理员昵称</label></td>
									<td align="left">
										<input type="hidden" id="merchantId" />
										<input type="text" id="nickName" class="field value two wide" />
									</td>
								</tr>
								<tr class="field row">
									<td align="right"><label class="field label one half wide" style="float: right;">手机号</label></td>
									<td align="left">
										<input type="text" id="phoneNo" class="field value two wide" />
									</td>
								</tr>
								<tr class="field row">
									<td align="right"><label class="field label one half wide" style="float: right;">商户名称</label></td>
									<td align="left">
										<input type="text" id="name" class="field value two wide" />
									</td>
								</tr>
								<tr class="field row">
									<td align="right"><label class="field label one half wide" style="float: right;">真实姓名</label></td>
									<td align="left">
										<input type="text" id="realName" class="field value two wide" />
									</td>
								</tr>
								<tr class="field row">
									<td align="right"><label class="field label one half wide" style="float: right;">身份证号</label></td>
									<td align="left">
										<input type="text" id="idCertNo" class="field value two wide" />
									</td>
								</tr>
								<tr class="field row">
									<td align="right"><label class="field label one half wide" style="float: right;">邮箱</label></td>
									<td align="left">
										<input type="text" id="email" class="field value two wide" />
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<span id="divider" class="normal hr divider"></span>
				<div id="singleShop" style="margin-left: 20px;">
					<div style="margin: 10px 0;">
						<label class="field inline label three half wide" style="font-size: 14px;color: #666;">店铺信息</label>
						<input type="hidden" id="shopId"/>
					</div>
					<div class="field row">
						<label class="field label required one half wide">店铺名称</label> 
						<input type="text" id="shopName" class="field value one half wide" />
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
			        	<img id="shopLogo" height="80px" width="120px" src="<%=resBaseUrl%>/image-app/img-none.jpg"/>
					</div>
					<div class="field row" style="margin-top: 10px;">
						<label class="field label one half wide">员工人数：</label>
						<input type="text" class="field value one half wide" id="staffCount" >
					</div>
					<div class="field row">
						<label class="field label required one half wide">联系人</label> 
						<input type="text" id="linkMan" class="field value one half wide" />
						<label class="field inline label required one half wide">联系电话</label> 
						<input type="text" id="telNo" class="field value one half wide" />
					</div>
					<div class="field row">
						<label class="field label one half wide">经度</label> 
						<input type="text" id="lng" class="field value one half wide" />
						<label class="field inline label one half wide">纬度</label> 
						<input type="text" id="lat" class="field value one half wide" />
					</div>
					<div class="field row">
						<label class="field label one half wide required">所在地</label>
						<select class="field value" id="province"></select>
						<select class="field value" id="city"></select>
						<select class="field value" id="county"></select>
						<select class="field value" id="town" style="display: none;"></select>
					</div>
					<div class="field row">
						<label class="field label one half wide required">街道地址</label>
						<input type="text" id="street" class="field value three wide" maxlength="60" title="请输入街道地址" />
					</div>
					<div class="field row" style="height: 75px;">
						<label class="field label required one half wide">经营范围</label>
						<textarea id="shopBizScope" class="field value three wide" style="height:70px;"></textarea>
					</div>
					<div class="field row">
						<label class="field label required one half wide">是否自营</label>
						<div class="field group">
							<input id="selfFlag-Y" type="radio" name="selfFlag" value="1" />
							<label for="selfFlag-Y">是</label>
							<input id="selfFlag-N" type="radio" name="selfFlag" value="0" checked="checked"/>
							<label for="selfFlag-N">否</label>
						</div>
					</div>
					<div class="field row">
						<label class="field inline label one half wide required">店铺类型</label>
						<div class="field group">
							<input id="entpFlag-Y" type="radio" name="shopType" value="0" checked="checked"/>
							<label for="entpFlag-Y">个人</label>
							<input id="entpFlag-N" type="radio" name="shopType" value="1"/>
							<label for="entpFlag-N">企业</label>
						</div>
						
						<div id="license" style="display: none;" class="field group">
							<label class="field inline label required">营业执照</label> 
							<input id="licenseId" type="text" class="field value"/>
						</div>
					</div>
					<div class="field row" style="height:60px;">
						<label class="field label one half wide" style="vertical-align: middle;">备注</label> 
						<textarea id="memo" class="field value three wide" style="height:50px;"></textarea>
					</div>
					<div class="field row">
						<label class="field label one half wide">推荐人</label> 
						<input type="text" id="referrerName" class="field value one half wide" />
						<label class="field inline label one half wide">推荐人电话</label> 
						<input type="text" id="referrerPhoneNo" class="field value one half wide" />
					</div>
					<span id="divider" class="normal hr divider"></span>
					<div id="bizLicenseList" class="noBorder">
						<div style="margin: 10px 0;">
							<div class="group left aligned" style="float: left;">
								<label style="font-size: 14px;color: #666;" id="selectBizLicense"><span style="color: red"> * </span>营业执照</label>
							</div>
							<div class="group right aligned" style="float: right;">
								<button id="selectBizLicenseBtn" class="normal button">选择</button>
							</div>
						</div>
						<div style="text-align: center; vertical-align: middle;">
							<table class="gridtable" style="width: 100%;">
								<thead></thead>
								<tbody id="bizLicense_tbody">
									<tr class="field row">
										<td align="right" width="18%"><label class="field label one half wide" style="float: right;">注册号</label></td>
										<td align="left">
											<input type="hidden" id="licenseId" />
											<input type="text" id="regNo" class="field value two wide" />
										</td>
									</tr>
									<tr class="field row">
										<td align="right"><label class="field label one half wide" style="float: right;">名称</label></td>
										<td align="left">
											<input type="text" id="companyName" class="field value two wide" />
										</td>
									</tr>
									<tr class="field row">
										<td align="right"><label class="field label one half wide one half wide" style="float: right;">营业执照图片</label></td>
										<td align="left">
											<img src="<%=resBaseUrl%>/image-app/img-none.jpg" id="bizLicenseImg" alt=""  width="120" height="80"/>
										</td>
									</tr>
									<tr class="field row">
										<td align="right"><label class="field label one half wide" style="float: right;">类型</label></td>
										<td align="left">
											<input type="text" id="companyType" class="field value two wide" />
										</td>
									</tr>
									<tr class="field row">
										<td align="right"><label class="field label one half wide" style="float: right;">住所</label></td>
										<td align="left">
											<input type="text" id="companyAddr" class="field value two wide" />
										</td>
									</tr>
									<tr class="field row">
										<td align="right"><label class="field label one half wide" style="float: right;">法定代表人</label></td>
										<td align="left">
											<input type="text" id="legalMan" class="field value two wide" />
										</td>
									</tr>
									<tr class="field row">
										<td align="right"><label class="field label one half wide" style="float: right;">注册资本</label></td>
										<td align="left">
											<input type="text" id="regCapital" class="field value two wide" />
										</td>
									</tr>
									<tr class="field row">
										<td align="right"><label class="field label one half wide" style="float: right;">成立日期</label></td>
										<td align="left">
											<input type="text" id="estabDate" class="field value two wide" />
										</td>
									</tr>
									<tr class="field row">
										<td align="right"><label class="field label one half wide" style="float: right;">营业期限</label></td>
										<td align="left">
											<input type="text" class="field value" disabled id="startDate" />
											至
											<input type="text" class="field value" disabled id="endDate" />
										</td>
									</tr>
									<tr class="field row">
										<td align="right"><label class="field label one half wide" style="float: right;">经营范围</label></td>
										<td align="left">
											<input type="text" id="bizScope" class="field value two wide" />
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
		<div id="selectMerchantDialog" class="form" style="display: none;height: 600px;">
			
			<div class="ui-layout-center">
				<div class="filter section">
					<div class="filter row">
						<div class="group right aligned">
							<label class="label">商户名</label> 
							<input id="selectShopName" class="normal input" /> 
							<span class="spacer"></span> 
							<button id="btnQueryMerchant" class="normal button">查询</button>
						</div>
					</div>
				</div>
				<table id="merchantGrid"></table>
				<div id="merchantPager"></div>
			</div>
		</div>
		<!--选择资金账户dialog -->
		<div id="selectUserAccountDialog" class="form" style="display: none;height: 600px;">
			<div class="ui-layout-center">
				<br>
				<table id="userAccountGrid"></table>
				<div id="userAccountPager"></div>
			</div>
		</div>
		<!--选择企业营业执照dialog -->
		<div id="selectBizLicenseDialog" class="form" style="display: none;height: 600px;">
			<div class="ui-layout-center">
				<br>
				<table id="bizLicenseGrid"></table>
				<div id="bizLicensePager"></div>
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
					<input type="hidden" id="shopAuditHiddenPhoneNo" /> <label
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
	</div>
	
	<!--合作店dialog -->
	<div id="selectHzShopDialog" class="form" style="display: none;height: 600px;">
		
		<div class="ui-layout-center">
			<div class="filter section">
				<div class="filter row">
					<div class="group right aligned">
						<label class="label">店铺名称</label> 
						<input id="selectHzShopName" class="normal input" /> 
						<span class="spacer"></span> 
						<button id="btnQueryHzShop" class="normal button">查询</button>
					</div>
				</div>
			</div>
			<table id="hzshopGrid"></table>
			<div id="hzshopPager"></div>
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
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		//------------------------自定义变量-------------------------------------------------------------------
		var isExistMerchantFlag = false;
		//jqGrid缓存变量
		var jqGridCtrl = null;
		//
		var curAction = "";
		//添加商户信息页面
		var shopDialog;
		var auditIds = "";
		//批量审核窗口
		var dialog_auditShops;
		//批量导入商户信息窗口
		var batchImpDialog;
		//查看店鋪信息窗口
		var addShopDialog;
		//查看店鋪信息窗口
		var showShopDialog;
		//
		var merchantFormProxy = FormProxy.newOne();
		//
		var bizLicenseFormProxy = FormProxy.newOne();
		//
		var selectMerchantDialog = null;
		//
		var selectBizLicenseDialog = null;
		//
	    var merchantGridHelper = JqGridHelper.newOne("");
	    //
	    var bizLicenseGridHelper = JqGridHelper.newOne("");
		//修改店鋪信息窗口
		var editShopDialog;
		var auditShopDialog;
		// 商店当前操作
		var shopGridHelper = JqGridHelper.newOne("");
		//
		var hzshopGridCtrl = null;
		//
		var hzShopGridHelper = JqGridHelper.newOne("");
		//
		var basicShopId = "";
		//------------------------验证-------------------------------------------------------------------
		//实例化表单代理
		//
		var shopInfoFormProxy = FormProxy.newOne();
		//
		shopInfoFormProxy.addField({
			id : "shopName",
			required : true,
			rules : ["maxLength[20]", {
				rule : function(idOrName, type, rawValue, curData) {
					if(isExistMerchantFlag){
						isExistShopName(rawValue);
						if(isExistShopNameFlag){
							return false;
						}
					}
					return true;
				},
				message : "该名称已被占用！"
			}],
			messageTargetId : "shopName"
		});
		//
		shopInfoFormProxy.addField({
			id : "shopLogo",
			rules : [{
				rule : function(idOrName, type, rawValue, curData) {
					var img = $id("shopLogo");
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
			messageTargetId : "shopLogo"
		});
		//
		shopInfoFormProxy.addField({
			id : "staffCount",
			rules : ["isNatual"],
			messageTargetId : "staffCount"
		});
		//
		shopInfoFormProxy.addField({
			id : "province",
			required : true,
			messageTargetId : "province"
		});
		//
		shopInfoFormProxy.addField({
			id : "city",
			required : true,
			messageTargetId : "city"
		});
		//
		shopInfoFormProxy.addField({
			id : "county",
			required : true,
			messageTargetId : "county"
		});
		//
		shopInfoFormProxy.addField({
			id : "town",
			rules : [ {
				rule : function(idOrName, type, rawValue, curData) {
					// 区若显示且为空，给予提示
					if (isShowTown("town") && isNoB(rawValue)) {
						return false;
					}
	
					return true;
				},
				message : "此为必须提供项！"
			} ],
			messageTargetId : "town"
		});
		//
		shopInfoFormProxy.addField({
			id : "street",
			required : true
		});
		//
		shopInfoFormProxy.addField({
			id : "linkMan",
			required : true,
			messageTargetId : "linkMan"
		});
		//
		shopInfoFormProxy.addField({
			id : "telNo",
			required : true,
			messageTargetId : "telNo"
		});
		//
		shopInfoFormProxy.addField({
			id : "lng",
			rules : ["maxLength[20]","isNum"],
			messageTargetId : "lng"
		});
		//
		shopInfoFormProxy.addField({
			id : "lat",
			rules : ["maxLength[20]","isNum"],
			messageTargetId : "lat"
		});
		//
		shopInfoFormProxy.addField({
			id : "shopBizScope",
			required : true,
			messageTargetId : "shopBizScope"
		});
		//
		shopInfoFormProxy.addField({
			id : "applyMsg",
			rules : ["maxLength[30]"],
			messageTargetId : "applyMsg"
		});
		//
		shopInfoFormProxy.addField({
			id : "referrerPhoneNo",
			rules : ["isMobile"],
			messageTargetId : "referrerPhoneNo"
		});
		//
		shopInfoFormProxy.addField({
			id : "selectMerchant",
			rules : [{
				rule : function(idOrName, type, rawValue, curData) {
					var merchantId = $id("merchantId").val();
					if(!merchantId){
						return false;
					}
					return true;
				},
				message: "亲,您还未选择商户哦！"
			}],
			messageTargetId : "selectMerchant"
		});
		//
		shopInfoFormProxy.addField({
			id : "selectBizLicense",
			rules : [{
				rule : function(idOrName, type, rawValue, curData) {
					var licenseId = $id("licenseId").val();
					if(!licenseId){
						return false;
					}
					return true;
				},
				message: "亲,您还未选择营业执照哦！"
			}],
			messageTargetId : "selectBizLicense"
		});

		//------------------------调整控件大小-------------------------------------------------------------------

		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "theGridCtrl";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			var pagerHeight = $id("theGridPager").height();
			jqGridCtrl.setGridWidth(mainWidth - 1);
			jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 60);
		}

		//------------------------jqGrid 初始化-------------------------------------------------------------------
		function initJqGrid() {
			//定义Grid及加载数据
			jqGridCtrl = $("#theGridCtrl").jqGrid(
					{
						url : getAppUrl("/shop/shop/list/get"),
						contentType : 'application/json',
						mtype : "post",
						datatype : 'json',
						height : "100%",
						width : "100%",
						colNames : [ "id", "店铺编号", "店铺名称", "所在地", "所属商户", "联系电话", "店铺状态", " 操作", "" ],
						colModel : [
								{
									name : "id",
									index : "id",
									hidden : true
								},
								{
									name : "shop.code",
									index : "shop.code",
									width : 100,
									align : 'left',
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
									name : "shop.phoneNo",
									index : "shop.phoneNo",
									width : 100,
									align : 'right'
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
								{
									name : 'id',
									index : 'id',
									formatter : function(cellValue, option, rowObject) {
										if (rowObject.shop.auditStatus == 0 || rowObject.shop.auditStatus == -1) {
											return "[<a class='item' href='javascript:void(0);' onclick='openAuditDialog(" + cellValue + ")' >审核</a>]";
										} else if (rowObject.shop.auditStatus == 1) {
											return "[<a class='item' href='javascript:void(0);' onclick='openAuditDialog(" + cellValue + ")' >审核</a>]";
										} else if (rowObject.shop.auditStatus == 2) {
											var shopDo = "[<a class='item' href='javascript:void(0);' onclick='openShowDlg(" + cellValue
													+ ")' >查看</a>]<span class='chs spaceholder'></span>[<a class='item' href='javascript:void(0);' onclick='openEditDlg(" + cellValue
													+ ")' >修改</a>]<span class='chs spaceholder'></span>";
											var distShop = "[<a class='item' href='javascript:void(0);' onclick='openHzshopDlg(" + cellValue
											+ ")' >合作店铺</a>]<span class='chs spaceholder'></span>";
											if (rowObject.shop.disabled == false) {
												return shopDo + "[<a class='item' href='javascript:void(0);' onclick='openOrClose(" + cellValue + ")' >禁用</a>]<span class='chs spaceholder'></span>" + distShop;
											} else {
												return shopDo + "[<a class='item' href='javascript:void(0);' onclick='openOrClose(" + cellValue + ")' >开启</a>]<span class='chs spaceholder'></span>" + distShop;
											}
										} else {
											return '审核未通过';
										}
									},
									width : 200,
									align : "center"
								}, {
									name : "shop.auditStatus",
									index : "shop.auditStatus",
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
						//rowList:[10,20,30],
						multiselect : true,// 定义是否可以多选
						multikey : 'ctrlKey',
						pager : "#theGridPager",
						loadComplete : function(gridData) { // JqGridHelper缓存最新的grid数据
							shopGridHelper.cacheData(gridData);
							var callback = getCallbackAfterGridLoaded();
							if (isFunction(callback)) {
								callback();
							}
						},
						ondblClickRow : function(rowId) {
							openShowDlg(rowId);
						}

					});
		}

		function getCallbackAfterGridLoaded() {

		}
		
		
		//------------------------对店铺的操作------------------------------------------------------------------
		//弹出审核框（单）
		function openAuditDialog(shopId) {
			$("input[name='shopAuditStatus'][value='2']").attr("checked", "checked");
			var shopMap = shopGridHelper.getRowData(shopId)
			$id("shopAuditHiddenId").val(shopId);
			$id("shopAuditHiddenName").val(shopMap.shop.name);
			$id("shopAuditHiddenPhoneNo").val(shopMap.merchant.user.phoneNo);
			initAuditShopDialog(shopMap);
			auditShopDialog.dialog("open");
		}
		
		function IndexShopDocs(){
			var ajax = Ajax.post("/shop/doc/index/do");
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess("操作成功!");
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		function initAuditShopDialog(obj) {
			auditShopDialog = $("#auditShopDialog").dialog({
				autoOpen : false,
				height : Math.min(300, $window.height()),
				width : Math.min(450, $window.width()),
				modal : true,
				title : '审核店铺',
				buttons : {
					"确定" : auditShop,
					"取消" : function() {
						auditShopDialog.dialog("close");
					}
				},
				close : function() {
					$("input[name='shopAuditStatus'][value='2']").attr("checked", "checked");
					$id("shopAuditDesc").val("");
					$id("shopAuditHiddenId").val("");
					$id("shopAuditHiddenName").val("");
					$id("shopAuditHiddenPhoneNo").val("");
				}
			});
		}
		//更改店铺 禁用 开启状态
		function openOrClose(id) {
			var shopMap = shopGridHelper.getRowData(id)
			var postData = {
				id : shopMap.id,
				disabled : shopMap.shop.disabled
			};
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/shop/shop/isDisabled/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					jqGridCtrl.jqGrid("setGridParam", {
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
		//审核店铺
		function auditShop() {
			var postData = {
				shopId : $id("shopAuditHiddenId").val(),
				shopName : $id("shopAuditHiddenName").val(),
				phoneNo : $id("shopAuditHiddenPhoneNo").val(),
				auditStatus : $("input[name='shopAuditStatus']:checked").val(),
				desc : $id("shopAuditDesc").val()
			};
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/shop/shop/audit/shop/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					jqGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
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
				desc : $id("bathshopAuditDesc").val()
			};
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/shop/shop/audit/shops/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					auditIds = "";
					jqGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
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
		//删除店铺 
		function deleteShop(shopId) {
			var theLayer = Layer.confirm('确定要删除该店铺吗？', function() {
				var postData = {
					id : shopId
				};
				var ajax = Ajax.post("/shop/shop/delete/by/id");
				ajax.data(postData);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						theLayer.hide();
						Layer.msgSuccess(result.message);
						jqGridCtrl.jqGrid("setGridParam", {
							page : 1
						}).trigger("reloadGrid");
					} else {
						theLayer.hide();
						Layer.msgWarning(result.message);
					}
				});
				ajax.go();
			});
		}
		//批量导入店铺
		function batchImpMerchant() {
			sendFileUpload("impMerchXls", {
				url : getAppUrl("/file/upload"),
				dataType : "json",
				//自定义数据
				formData : {
					usage : "shop.extract"
				},
				done : function(e, result) {
					//done方法就是上传完毕的回调函数，其他回调函数可以自行查看api  
					//注意result要和jquery的ajax的data参数区分，这个对象包含了整个请求信息  
					//返回的数据在result.result中，假设我们服务器返回了一个json对象 
					var resultInfo = result.result;
					console.log(resultInfo);
					var resultData = resultInfo.data || {};
					var files = resultData.files || [];
					for (var i = 0; i < files.length; i++) {
						var file = files[i];
						if (file.type == "info") {
							Layer.msgSuccess(file.message);
							batchImpDialog.dialog("close");
							jqGridCtrl.jqGrid("setGridParam", {
								page : 1
							}).trigger("reloadGrid");
						} else {
							Layer.msgWarning(file.message);
						}
					}
				},
				fail : function(e, data) {
					console.log(data);
				}
			});
		}

		
		//------------------------Dialog 初始化------------------------------------------------------------------
		
		//初始化选择商户Dialog
		function initSelectMerchantDialog(){
			selectMerchantDialog = $( "#selectMerchantDialog" ).dialog({
		        autoOpen: false,
		        width : Math.min(800, $window.width()),
		        height : Math.min(480, $window.height()),
		        modal: true,
		        title : '选择商户',
		        buttons: {
		            "确定": function(){
		            	var merchantId = getCheckedMerchant();
		            	showSelectMerchantInfo(merchantId);
		            	//
		    			var postData = {
		    					userId : merchantId
		    			};
		            	//
		            	bizLicenseGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(postData,true)},page:1}).trigger("reloadGrid");
		    			
		            	selectMerchantDialog.dialog("close");
		            },
		            "关闭": function() {
		            	selectMerchantDialog.dialog( "close" );
		          }
		        },
		        close: function() {
		        	selectMerchantDialog.dialog( "close" );
		        }
		      });
		}
		
		//初始化选择企业营业执照Dialog
		function initSelectBizLicenseDialog(){
			selectBizLicenseDialog = $( "#selectBizLicenseDialog" ).dialog({
		        autoOpen: false,
		        width : Math.min(800, $window.width()),
		        height : Math.min(300, $window.height()),
		        modal: true,
		        title : '选择企业营业执照',
		        buttons: {
		        	"确定": function(){
		            	var licenseId = getCheckedBizLicense();
		            	showSelectLicenseInfo(licenseId);
		            	selectBizLicenseDialog.dialog("close");
		            },
		            "关闭": function() {
		            	selectBizLicenseDialog.dialog( "close" );
		          }
		        },
		        close: function() {
		        	selectBizLicenseDialog.dialog( "close" );
		        }
		      });
		}
		
		
		//------------------------加载数据------------------------------------------------------------------
		//加载商户列表数据
		function loadMerchantData(){
			var postData = {
			};
			//加载商户
			merchantGridCtrl= $("#merchantGrid").jqGrid({
			      url : getAppUrl("/merch/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : 240,
				  width : "100%",
			      colNames : ["id", "管理员名称", "手机号","商户名","身份证","邮箱","操作"],  
			      colModel : [{name:"id", index:"id", width:150,align : 'center', hidden : true},
			                  {name:"user.nickName", index:"user.nickName",width:100,align : 'center',},
			                  {name:"user.phoneNo", index:"user.phoneNo",width:100,align : 'center',},
			                  {name:"name", index:"name",width:140,align : 'center'},
			                  {name:"idCertNo", index:"idCertNo",width:140,align : 'center'},
			                  {name:"user.email", index:"user.email",width:140,align : 'center'},
			                  {name:"action", index:"action", width : 100, align : "center"} 
							],  
				loadtext: "Loading....",
				multiselect : false,// 定义是否可以多选
				multikey:'ctrlKey',
				pager : "#merchantPager",
				loadComplete : function(gridData){
					merchantGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if(isFunction(callback)){
						callback();
					}
				},
				gridComplete : function(){
					var ids = merchantGridCtrl.jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						id = ParseInt(id);
						merchantGridCtrl.jqGrid('setRowData', id, {
							action : "<input type='radio' name='merchant' value='"+id+"'/>"
						});
					}
				}
			});
		}
		
		//加载营业执照列表数据
		function loadBizLicenseData(){
			//加载营业执照
			bizLicenseGridCtrl= $("#bizLicenseGrid").jqGrid({
			      url : getAppUrl("/comn/bizLicense/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      height : "100%",
				  width : "100%",
			      colNames : ["id", "注册号", "名称","类型","开始日期","结束日期", "操作"],  
			      colModel : [{name:"id", index:"id", width:150,align : 'center', hidden : true},
			                  {name:"regNo", index:"regNo",width:120,align : 'center'},
			                  {name:"name", index:"name",width:130,align : 'center',},
			                  {name:"type", index:"type",width:140,align : 'center'},
			                  {name:"startDate", index:"startDate",width:120,align : 'center'},
			                  {name:"endDate", index:"endDate",width:120,align : 'center'},
			                  {name:"action", index:"action", width : 100, align : "center"} 			
							],  
				loadtext: "Loading....",
				multiselect : false,// 定义是否可以多选
				multikey:'ctrlKey',
				//pager : "#userAccountPager",
				loadComplete : function(gridData){
					bizLicenseGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if(isFunction(callback)){
						callback();
					}
				},
				gridComplete : function(){
					var ids = bizLicenseGridCtrl.jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						id = ParseInt(id);
						bizLicenseGridCtrl.jqGrid('setRowData', id, {
							action : "<input type='radio' name='bizLicense' value='"+id+"'/>"
						});
					}
				}
			});
		}
		
		function showSelectMerchantInfo(id){
			if(id){
				$id("merchantId").val(id);
				var merchant = merchantGridHelper.getRowData(id);
				if(merchant.user != null){
					$id("nickName").val(merchant.user.nickName);
					$id("phoneNo").val(merchant.user.phoneNo);
					$id("name").val(merchant.name);
					$id("realName").val(merchant.realName);
					$id("idCertNo").val(merchant.idCertNo);
					$id("email").val(merchant.user.email);
				}
				
			}
		}
		
		function showSelectLicenseInfo(id){
			if(id){
				$id("licenseId").val(id);
				var bizLicense = bizLicenseGridHelper.getRowData(id);
				if(bizLicense != null){
					$id("regNo").val(bizLicense.regNo);
					$id("companyName").val(bizLicense.name);
					$id("companyType").val(bizLicense.type);
					$id("companyAddr").val(bizLicense.address);
					$id("legalMan").val(bizLicense.legalMan);
					$id("regCapital").val(bizLicense.regCapital);
					$id("estabDate").val(bizLicense.estabDate);
					$id("startDate").val(bizLicense.startDate);
					$id("endDate").val(bizLicense.endDate);
					$id("bizScope").val(bizLicense.bizScope);
					var licenseImgUrl = bizLicense.bizLicensePic.fileBrowseUrl;
					$id("bizLicenseImg").attr("src", licenseImgUrl);
				}
			}
		}
		
		function getCallbackAfterGridLoaded(){}
		//---------------------------业务---------------------------------
		//打开新增对话框
		function openAddDlg() {
			curAction = "add";
			//
			toShowTheDlg();
		}
		//打开查看对话框
		function openShowDlg(shopId) {
			curAction = "view";
			//
			toShowTheDlg(shopId);
		}
		//打开查看对话框
		function openEditDlg(shopId) {
			curAction = "mod";
			//
			toShowTheDlg(shopId);
		}
		//（真正）显示对话款
		function toShowTheDlg(dataId) {
			//
			var theDlgId = "userDlg";
			jqDlgDom = $id(theDlgId);
			//对话框配置
			var dlgConfig = {
				width : Math.min(750, $window.width()),
				height : Math.min(700, $window.height()),
				modal : true,
				open : false
			};
			//
			if (curAction == "add") {
				dlgConfig.title = "店铺 - 新增";
				dlgConfig.buttons = {
					"保存" : function() {
						//收集并验证要提交的数据（如果验证不通过直接返回 return）
						var vldResult = shopInfoFormProxy.validateAll();
						if (!vldResult) {
							return;
						}
						var postData = shopInfoFormProxy.getValues();
						//
						goAddOrEditShop();
						$(this).dialog("close");
						
					},
					"取消" : function() {
						//
						jqDlgDom.prop("continuousFlag", false);
						//
						$(this).dialog("close");
						//隐藏表单验证消息
						shopInfoFormProxy.hideMessages();
					}
				
				};
				dlgConfig.close = function() {
					shopInfoFormProxy.hideMessages();	
				};
			} else if (curAction == "mod") {
				dlgConfig.title = "店铺 - 修改";
				dlgConfig.buttons = {
					"保存" : function() {
						//收集并验证要提交的数据（如果验证不通过直接返回 return）
						var vldResult = shopInfoFormProxy.validateAll();
						if (!vldResult) {
							return;
						}
						goAddOrEditShop();
						$(this).dialog("close");
					},
					"取消" : function() {
						$(this).dialog("close");
						//隐藏表单验证消息
						shopInfoFormProxy.hideMessages();
					}
				};
			} else {
				//== view 查看
				dlgConfig.title = "店铺 - 查看";
				dlgConfig.buttons = {
					"修改 > " : function() {
						$(this).dialog("close");
						//
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
				$("#merchant_tbody").find(":input").prop("disabled", true);
				$("#userAccount_tbody").find(":input").prop("disabled", true);
				$("#bizLicense_tbody").find(":input").prop("disabled", true);
			} else {
				jqDlgDom.find(":input").prop("disabled", true);
			}
			//重置控件值
			$("#shopLogo").attr("src", "");
			$("#bizLicenseImg").attr("src", "");
			$("#province option[value='" + "" + "']").attr("selected", true);
			singleSet("selfFlag", "0");
			singleSet("shopType", "0");
			$("#userDlg input[type=text]").val("");
			$("#userDlg textarea").val("");
			textSet("merchantId", "");
			textSet("licenseId", "");
			//
			if (curAction == "view" || curAction == "mod") {
				var data = shopGridHelper.getRowData(dataId);
				if (data != null) {
					textSet("shopId", data.shop.id);
					textSet("shopName", data.shop.name);
					textSet("logoUuid", data.shop.logoUuid);
					textSet("logoUsage", data.shop.logoUsage);
					textSet("logoPath", data.shop.logoPath);
					$("#shopLogo").attr("src", data.shop.fileBrowseUrl);
					textSet("staffCount", data.shop.staffCount);
					textSet("linkMan", data.shop.linkMan);
					textSet("telNo", data.shop.phoneNo);
					textSet("lng", data.shop.lng);
					textSet("lat", data.shop.lat);
					if (data.shop.regionId != null) {
						initArea(data.shop.regionId);
					}
					textSet("street", data.shop.street);
					textSet("shopBizScope", data.shop.bizScope);
					textSet("memo", data.shop.memo);
					textSet("referrerName", data.shop.referrerName);
					textSet("referrerPhoneNo", data.shop.referrerPhoneNo);
					singleSet("selfFlag", data.shop.selfFlag);
					singleSet("shopType", data.shop.shopType);
					//
					var merchantId = data.shop.merchantId;
					textSet("merchantId", merchantId);
					showSelectMerchantInfo(merchantId);
					
					//
	    			var postData = {
	    					userId : merchantId
	    			};
	            	bizLicenseGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(postData,true)},page:1}).trigger("reloadGrid");
					//
					var licenseId = data.shop.licenseId;
					textSet("licenseId", licenseId);
					showSelectLicenseInfo(licenseId);
				}
			}
		}
		
		function getCheckedMerchant(){
			var merchantId = null;
			$("input:radio[name='merchant']").each(function(){
				merchantId = $(this).val();
				var isChecked = $(this).prop("checked");
				if(isChecked){
					return false;
				}
			});
			return merchantId;
		}
		
		function getCheckedBizLicense(){
			var licenseId = null;
			$("input:radio[name='bizLicense']").each(function(){
				licenseId = $(this).val();
				var isChecked = $(this).prop("checked");
				if(isChecked){
					return false;
				}
			});
			return licenseId;
		}
		
		//------------------------上传-------------------------
		//
		function fileToUploadFileIcon(fileId) {
			var fileUuidToUpdate = $id("IconUuid").val();
			if (isNoB(fileUuidToUpdate)) {
				var formData = {
					usage : "image.logo",
					subUsage : "shop"
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
						//$("#logoImgshop").makeUniqueRequest("");
						$("#shopLogo").attr("src", resultInfo.data.files[0].fileBrowseUrl + "?" + new Date().getTime());
						;
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
		//
		function hideTown() {
			$("#town").hide().empty();
			hideMiscTip("town");
		}
		
		// 加载省份
		function loadProvince() {
			// 隐藏页面区
			hideTown();

			var ajax = Ajax.get("/setting/region/selectList/get");
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("province", result.data);
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		// 判断区是否显示
		function isShowTown(town){
	 		return $id(town).val() != null;
		}
		
		function initArea(sonId) {
			var params = {
				id : sonId
			};
			var ajax = Ajax.get("/setting/region/get/by/id");
			ajax.data(params);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data;
					if (data != null) {
						var idPath = data.idPath;
						if (idPath != null) {
							arr = idPath.split(',');
							if (arr.length == 2) {
								$("#province option[value='" + arr[0] + "']").attr("selected", true);
								loadCityByParentId(arr[0], arr[1]);
								loadCountyByCityId(arr[1], sonId);
							} else {
								$("#province option[value='" + arr[0] + "']").attr("selected", true);
								loadCityByParentId(arr[0], arr[1]);
								loadCountyByCityId(arr[1], arr[2]);
								loadTownByCountyId(arr[2], sonId);
							}
						}
					}

				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		// 根据页面选择的省加载市
		function loadCityByParentId(parentId, cityId) {
			// 隐藏页面区
			hideTown();

			var ajax = Ajax.get("/setting/region/selectList/get");
			ajax.params({
				parentId : parentId
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("city", result.data);
					$("#city option[value='" + cityId + "']").attr("selected", true);
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		// 根据页面选择的市加载县
		function loadCountyByCityId(cityId, countyId) {
			// 隐藏页面区
			hideTown();

			var ajax = Ajax.get("/setting/region/selectList/get");
			ajax.params({
				parentId : cityId
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("county", result.data);
					$("#county option[value='" + countyId + "']").attr("selected", true);
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		// 根据选择的县加载区
		function loadTownByCountyId(countyId, townId) {
			// 隐藏页面区
			hideTown();

			var ajax = Ajax.get("/setting/region/selectList/get");
			ajax.params({
				parentId : countyId
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data;

					// 标记商城基本信息区数量
					var hasTown = 0;
					for (var i = 0, len = data.items.length; i < len; i++) {
						hasTown++;
					}

					if (hasTown > 0) {
						loadSelectData("town", data);
						$("#town").show();
						$("#town option[value='" + townId + "']").attr("selected", true);
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}

		// 根据页面选择的省加载市
		function loadCity() {
			// 隐藏页面区
			hideTown();

			var ajax = Ajax.get("/setting/region/selectList/get");
			ajax.params({
				parentId : $("#province").val()
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("city", result.data);
					$id("county").empty();
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}

		// 根据页面选择的市加载县
		function loadCounty() {
			// 隐藏页面区
			hideTown();

			var ajax = Ajax.get("/setting/region/selectList/get");
			ajax.params({
				parentId : $("#city").val()
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("county", result.data);
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		// 根据选择的县加载区
		function loadTown() {
			// 隐藏页面区
			hideTown();

			var ajax = Ajax.get("/setting/region/selectList/get");
			ajax.params({
				parentId : $("#county").val()
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data;
					
					// 标记商城基本信息区数量
					var hasTown = 0;
					for(var i = 0, len = data.items.length; i < len; i++){
						hasTown++;
					}

					if (hasTown > 0) {
						loadSelectData("town", data);
						$("#town").show();
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		function getSelfFlag(){
			//1:对公账号 0：对私账号
			var selfFlag = radioGet("selfFlag");
			selfFlag = ParseInt(selfFlag);
			return selfFlag;
		}
		
		function getShopTypeFlag(){
			//1:对公账号 0：对私账号
			var typeFlag = radioGet("shopType");
			typeFlag = ParseInt(typeFlag);
			return typeFlag;
		}
		
		function goSelectMerchant(){
			selectMerchantDialog.dialog("open");
			// 页面自适应
			adjustCtrlsSize();
		}
		
		function goSelectBizLicense(){
			var merchantId = textGet("merchantId");
			if(merchantId == ""){
				Layer.msgWarning("请先选择商户，谢谢！");
				return;
			}
			selectBizLicenseDialog.dialog("open");
			// 页面自适应
			adjustCtrlsSize();
		}
		
		
		//------------------------操作-------------------------
		
		function goAddOrEditShop(){
			var shopInfoMap = {};
			shopInfoMap["name"] = textGet("shopName");
			shopInfoMap["logoUuid"] = textGet("logoUuid");
			shopInfoMap["logoUsage"] = textGet("logoUsage");
			shopInfoMap["logoPath"] = textGet("logoPath");
			shopInfoMap["staffCount"] = textGet("staffCount");
			shopInfoMap["linkMan"] = textGet("linkMan");
			shopInfoMap["phoneNo"] = textGet("telNo");
			shopInfoMap["lng"] = textGet("lng");
			shopInfoMap["lat"] = textGet("lat");
			//地区信息
			var regionId;
			if(isShowTown("town")){
				regionId = intGet("town");
			} else {
				regionId = intGet("county");
			}
			shopInfoMap["regionId"] = regionId;
			shopInfoMap["street"] = textGet("street");
			shopInfoMap["bizScope"] = textGet("shopBizScope");
			//
			var selfFlag = getSelfFlag();
			shopInfoMap["selfFlag"] = selfFlag;
			//
			var typeFlag = getShopTypeFlag();
			shopInfoMap["entpFlag"] = typeFlag;
			shopInfoMap["memo"] = textGet("memo");
			shopInfoMap["referrerName"] = textGet("referrerName");
			shopInfoMap["referrerPhoneNo"] = textGet("referrerPhoneNo");
			shopInfoMap["merchantId"] = textGet("merchantId");
			shopInfoMap["merchantName"] = textGet("name");
			shopInfoMap["licenseId"] = textGet("licenseId");
			if(curAction == "add"){
				addShop(shopInfoMap);
			}else {
				shopInfoMap["id"] = textGet("shopId");
				editShop(shopInfoMap);
			}
			
		}
		
		function addShop(shopInfoMap){
			var hintBox = Layer.progress("正在保存数据...");

			var ajax = Ajax.post("/shop/add/do");
			ajax.data(shopInfoMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					
					jqGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
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
		
		function editShop(shopInfoMap){
			var hintBox = Layer.progress("正在保存数据...");

			var ajax = Ajax.post("/shop/update/do");
			ajax.data(shopInfoMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					
					jqGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
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
		
		function initDialog() {
			//初始化店铺审核Dialog
			dialog_auditShops = $("#dialog_auditShops").dialog({
				autoOpen : false,
				height : Math.min(300, $window.height()),
				width : Math.min(450, $window.width()),
				modal : true,
				buttons : {
					"确定" : auditShops,
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
		
		//加载合作店列表数据
		function loadHzshopData(){
			var postData = {
			};
			//加载合作店
			hzshopGridCtrl= $id("hzshopGrid").jqGrid({
			      url : getAppUrl("/distshop/wxshop/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : 250,
				  width : "100%",
			      colNames : ["id", "手机号", "店铺名称", "联系人", "联系电话", "所在地", "操作"],  
			      colModel : [{
								name : "id",
								index : "id",
								hidden : true
							},
							{
								name : "phoneNo",
								index : "phoneNo",
								width : 100,
								align : 'center'
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
								name : "telNo",
								index : "telNo",
								width : 100,
								align : 'center'
							},
							{
								name : "regionName",
								index : "regionName",
								width : 215,
								align : 'left'
							},
							{
								name : "id",
								index : "id",
								width : 200,
								align : 'center',
								formatter : function(cellValue, option, rowObject) {
									return "[<a class='item dlgStatisview' href='javascript:void(0);' cellValue='" + cellValue + "'>统计</a>]";
								}
							}],  
				loadtext: "Loading....",
				multiselect : true,// 定义是否可以多选
				multikey:'ctrlKey',
				pager : "#hzshopPager",
				loadComplete : function(gridData){
					hzShopGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if(isFunction(callback)){
						callback();
					}
				},
				gridComplete : function(){
				}
			});
		}
		
		//初始化合作店Dialog
		function initHzShopDialog(){
			selectHzShopDialog = $( "#selectHzShopDialog" ).dialog({
		        autoOpen: false,
		        width : Math.min(1000, $window.width()),
		        height : Math.min(480, $window.height()),
		        modal: true,
		        title : '合作店列表',
		        buttons: {
		            "关闭": function() {
		            	selectHzShopDialog.dialog( "close" );
		          }
		        },
		        close: function() {
		        	selectHzShopDialog.dialog( "close" );
		        }
		      });
		}
		
		function openHzshopDlg(shopId){
			basicShopId = shopId;
			var filter = {
				auditStatus : 1,
				disabled : false,
				ownerShopId : basicShopId
			};
			//加载hzshopGridCtrl
			hzshopGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			selectHzShopDialog.dialog( "open" );
		}
		
		function getCallbackAfterGridLoaded() {

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
		
		//------------------------初始化-------------------------------------------------------------------

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
			//初始化JqGrid
			initJqGrid();
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
			//绑定查询按钮
			$id("btnQueryShop").click(function() {
				
				var filter = {};
				var name = textGet("queryShopName");
				if(name){
					filter.name = name;
				}
				var disabled = singleGet("queryShopStatus");
				if(disabled && disabled != "全部"){
					if(disabled < 2){
						filter.disabled = disabled;	
					} else {
						if(disabled = 5){
							filter.auditStatus = -1;	
						}else{
							filter.auditStatus = disabled - 2;	
						}
					}
					
				}
				
				//加载jqGridCtrl
				jqGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			});
			//绑定批量删除按钮
			$id("btnDelShops").click(function() {
				var ids = jqGridCtrl.jqGrid("getGridParam", "selarrrow");
				if (ids == "") {
					Layer.msgWarning("请选中要删除的店铺");
					return;
				}
				var theLayer = Layer.confirm('确定要删除该店铺吗？', function() {
					var postData = {
						ids : ids
					};
					var ajax = Ajax.post("/shop/shop/delete/by/ids/do");
					ajax.data(postData);
					ajax.done(function(result, jqXhr) {
						if (result.type == "info") {
							theLayer.hide();
							Layer.msgSuccess(result.message);
							jqGridCtrl.jqGrid("setGridParam", {
								page : 1
							}).trigger("reloadGrid");
						} else {
							theLayer.hide();
							Layer.msgWarning(result.message);
						}
					});
					ajax.go();
				});
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
					var val = rowData["shop.auditStatus"]; //获得制定列的值 （auditStatus 为colModel的name）
					if ("2" == val) {
						Layer.msgWarning("包含已经审核通过的店铺");
						return;
					}
				}
				auditIds = ids;
				initDialog();
				dialog_auditShops.dialog("open");
			});
			//批量导入店铺
			initFileUpload("impMerchXls");
			//上传Logo
			initFileUpload("fileToUploadFileLogo");
			//添加店铺
			$id("btnAdd").click(openAddDlg);
			
			//初始化商户
			loadMerchantData();
			//初始化营业执照
			loadBizLicenseData();
			//
			initSelectMerchantDialog();
			initSelectBizLicenseDialog();

			//绑定修改模块上传按钮
			$id("btnfileToUploadFile").click(function() {
				fileToUploadFileIcon("fileToUploadFileLogo");
			});
			
			// 加载省列表
			loadProvince();
			// 绑定区域change事件
			$("#province").change(function() {
				loadCity();
			});
			$("#city").change(function() {
				loadCounty();
			});
			$("#county").change(function() {
				loadTown();
			});
			
			//选择商户
			$id("selectMerchantBtn").click(goSelectMerchant);
			//查询商户
			$id("btnQueryMerchant").click(function() {
				
				var filter = {};
				var name = textGet("selectShopName");
				if(name){
					filter.name = name;
				}
				//加载merchantGridCtrl
				merchantGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			});
			//选择营业执照
			$id("selectBizLicenseBtn").click(goSelectBizLicense);
			//
			var size = getImageSizeDef("image.logo");
			$id("img").attr("width", size.width);
			$id("img").attr("height", size.height);
			//
			initHzShopDialog()
			//
			loadHzshopData();
			//查询合作店铺
			$id("btnQueryHzShop").click(function() {
				var filter = {
					auditStatus : 1,
					disabled : false,
					ownerShopId : basicShopId
				};
				var distShopName = $id("selectHzShopName").val();
				if(distShopName){
					filter.distShopName = distShopName;
				}
				//加载hzshopGridCtrl
				hzshopGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			});
			
			// 合作店统计信息
			initDistShopStatisDialog();
			$(document).on("click", ".dlgStatisview", function() {
				loadDistshopStatisData($(this).attr("cellValue"));
			});
		});
	</script>
</body>
<script type="text/html" id="merchantTpl" title="店铺等级信息模版">
	{{# for(var i = 0, len = d.length; i < len; i++){ }}
		<tr id="tr{{ i }}">
			<td>
                <label id="showshopGradeUpperPoint{{ i }}" class="normal label">{{ d[i].acctName }}</label>
            </td>
			<td>
                <label id="showshopGradeUpperPoint{{ i }}" class="normal label">{{ d[i].acctNo }}</label>
            </td>
            <td>
                 <label id="showshopGradeUpperPoint{{ i }}" class="normal label">{{ d[i].bankName }}</label>
             </td>
 			<td>
                <label id="showshopGradeUpperPoint{{ i }}" class="normal label">{{ d[i].acctName }}</label>
            </td>
 			<td>
                <label id="showshopGradeUpperPoint{{ i }}" class="normal label">{{ d[i].phoneNo }}</label>
            </td>
		</tr>
	{{# } }}
		
</script>
<script type="text/html" id="bizLicenseTpl" title="店铺等级信息模版">
	{{# for(var i = 0, len = d.length; i < len; i++){ }}
		<tr id="tr{{ i }}">
			<td>
                <label id="showshopGradeUpperPoint{{ i }}" class="normal label">{{ d[i].regNo }}</label>
            </td>
            <td>
                 <label id="showshopGradeUpperPoint{{ i }}" class="normal label">{{ d[i].legalMan }}</label>
             </td>
            <td>
                <label id="showshopGradeUpperPoint{{ i }}" class="normal label">{{ d[i].name }}</label>
            </td>
 			<td>
                <label id="showshopGradeUpperPoint{{ i }}" class="normal label">{{ d[i].type }}</label>
            </td>
			<td>
                <label id="showshopGradeUpperPoint{{ i }}" class="normal label">{{ d[i].regCapital }}万元</label>
            </td>
 			<td>
                <label id="showshopGradeUpperPoint{{ i }}" class="normal label">{{ d[i].address }}</label>
            </td>
		</tr>
	{{# } }}
		
</script>
</html>