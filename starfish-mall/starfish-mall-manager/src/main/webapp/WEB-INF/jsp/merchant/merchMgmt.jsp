<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>商户管理</title>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnAdd" class="button">添加</button>
					<button id="btnBatchDel" class="button" style="display: none;">批量删除</button>
					<button class="button" style="width: 100px;display: none;">发送站内信</button>
					<button id="btnBatchImp" class="button" style="display: none;">批量导入</button>
					<button id="btnBatchExp" class="button" style="width: 100px;display: none;">批量导出</button>
				</div>
				<div class="group right aligned">
					<label class="label">商户编号</label> <input id="queryId" class="input" /> 
					<span class="spacer"></span> 
					<label class="label">商户姓名</label> <input id="queryRealName" class="input" /> 
					<span class="spacer"></span> 
					<label class="label">商户状态</label> 
					<select id="queryDisabled" class="input">
						<option>全部</option>
						<option value="0">启用</option>
						<option value="1">禁用</option>
					</select> 
					<span class="spacer two wide"></span> 
					<span class="vt divider"></span>
					<span class="spacer two wide"></span>
		
					<button id="btnQuery" class="button">查询</button>
				</div>
			</div>
		</div>
		
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
		
		<div id="merchantSettleDialog" style="display: none;">
			<div id="showSettleWayDialog">
				
			</div>
		</div>
		
		<div id="addDialog" style="display: none;">
			<div id="addForm" class="form">
				<div style="margin: 10px 0;">
					<label style="font-size: 14px;color: #666;">管理员信息</label>
				</div>
				<div class="field row">
					<label class="field label required one half wide">手机号码</label> 
					<input type="text" id="phoneNo" class="field value one half wide" />
					<label class="field inline label required one half wide">管理员昵称</label> 
					<input type="text" id="nickName" class="field value one half wide" />
				</div>
				<span id="divider" class="normal hr divider"></span>
				<div style="margin: 10px 0;">
					<label style="font-size: 14px;color: #666;">商户信息</label>
				</div>
				<div class="field row">
					<label class="field label required one half wide">商户名</label> 
					<input type="hidden" id="userId" class="field value" />
					<input type="text" id="shopName" class="field value three wide" />
				</div>
				<div class="field row">
					<label class="field label required one half wide">真实姓名</label> 
					<input type="text" id="realName" class="field value one half wide" />
					<label class="field inline label required one half wide">身份证号</label> 
					<input type="text" id="idCertNo" class="field value one half wide" />
				</div>
				<div class="field row">
					<label class="field label required one half wide">身份证正面</label> 
					<input name="file" type="file" id="leftIdCertFile" multiple="multiple" class="field value one half wide"  /> 
					<button class="normal button" id="leftIdCertFileToUpload">上传</button>
				</div>
				<div class="field row">
					<input type="hidden" id="leftImageUuid" />
					<input type="hidden" id="leftImageUsage" />
					<input type="hidden" id="leftImagePath" />
					<input type="hidden" id="leftImageBrowseUrl" />
					<label class="field label one half wide">图片预览</label>
		        	<img id="leftIdCertImg" height="80px" width="120px" src="<%=resBaseUrl%>/image-app/img-none.jpg"/>
				</div>
				<div class="field row" style="margin-top: 10px;">
					<label class="field label required one half wide" >身份证反面</label> 
					<input name="file" type="file" id="rightIdCertFile" multiple="multiple" class="field value one half wide"  /> 
					<button class="normal button" id="rightIdCertFileToUpload">上传</button>
				</div>
				<div class="field row">
					<input type="hidden" id="rightImageUuid" />
					<input type="hidden" id="rightImageUsage" />
					<input type="hidden" id="rightImagePath" />
					<input type="hidden" id="rightImageBrowseUrl" />
					<label class="field label one half wide">图片预览</label>
		        	<img id="rightIdCertImg" height="80px" width="120px" src="<%=resBaseUrl%>/image-app/img-none.jpg"/>
				</div>
				<div class="field row" style="margin-top: 10px;">
					<label class="field label required one half wide">邮箱</label>
					<input type="text" id="email" class="field value three wide" />
				</div>
				<div class="field row" style="height:60px;">
						<label class="field label one half wide" style="vertical-align: middle;">备注</label> 
						<textarea id="memo" class="field value three wide" style="height:50px;width: 450px;"></textarea>
				</div>
				<div id="singleShop" style="margin-left: 20px">
				<span id="divider" class="normal hr divider"></span>
					<!-- 资金账户 -->
					<div style="margin: 10px 0;">
						<label class="field inline label three half wide" style="font-size: 14px;color: #666;">资金账户</label>
					</div>
					<div id="account-content">
						<div class="field row">
							<label class="field inline label one half wide required">账户类型</label>
							<div class="field group">
								<input id="entpFlag-Y" type="radio" name="shopType" value="0" checked="checked"/>
								<label for="entpFlag-Y">个人</label>
								<input id="entpFlag-N" type="radio" name="shopType" value="1"/>
								<label for="entpFlag-N">企业</label>
							</div>
						</div>
						<div class="field row">
							<label class="field label required  one half wide">银行名称</label>
							<select id="bankCode" class="field value two wide" onchange="changBankInfo();"></select>
						</div>
						<div class="field row">
							<label class="field label required one half wide">账户号</label> 
							<input type="text" id="acctNo" class="field value two wide" />
						</div>
						<div class="field row">
							<label class="field label required one half wide">开户名</label> 
							<input type="text" id="acctName" class="field value two wide" />
						</div>
						<div id="bankInfo" style="display: block;">
							<div class="field row">
								<label class="field label required one half wide">开户省份</label>
								<select class="field value two wide" id="provinceCode"></select>
							</div>
							<div class="field row">
								<label class="field label required one half wide">开户行名</label> 
								<input type="text" id="bankFullName" class="field value two wide" />
							</div>
							<div class="field row">
								<label class="field label required one half wide">联行行号</label> 
								<input type="text" id="relatedBankNo" class="field value two wide" />
							</div>
							<div class="field row">
								<label class="field label one half wide">预留号码</label> 
								<input type="text" id="acctPhoneNo" class="field value two wide" />
							</div>
							<div id="vfCodeDiv" class="field row" style="display: none;">
								<label class="field label one half wide">打款验证码</label> 
								<input type="text" id="vfCode" class="field value two wide" maxlength="30"/>
							</div>
						</div>
					</div>
					<span id="divider" class="normal hr divider"></span>
					<!-- 企业营业执照 -->
					<div style="margin: 10px 0;">
						<label class="field inline label three half wide" style="font-size: 14px;color: #666;">营业执照</label>
					</div>
					<div id="bizLicense-content">
						<div class="field row">
							<label class="field label required one half wide">注册号：</label>
							<input type="text" class="field value two wide" id="regNo" />
						</div>
						<div class="field row">
							<label class="field label required one half wide">名称：</label>
							<input type="text" class="field value two wide" id="companyName" />
						</div>
						<div class="field row">
							<label class="field label required one half wide" >营业执照图片</label> 
							<input name="file" type="file" id="bizLicenseFile" multiple="multiple" class="field value one half wide"  /> 
							<button class="normal button" id="bizLicenseFileToUpload">上传</button>
						</div>
						<div class="field row">
							<input type="hidden" id="bizLicenseUuid" />
							<input type="hidden" id="bizLicenseUsage" />
							<input type="hidden" id="bizLicensePath" />
							<input type="hidden" id="bizLicenseBrowseUrl" />
							<label class="field label one half wide">图片预览</label>
				        	<img id="bizLicenseImg" height="80px" width="120px" src="<%=resBaseUrl%>/image-app/img-none.jpg"/>
						</div>
						<div class="field row" style="margin-top: 10px;">
							<label class="field label required one half wide">类型：</label>
							<input type="text" class="field value two wide" id="companyType" />
						</div>
						<div class="field row">
							<label class="field label required one half wide">住所：</label>
							<input type="text" class="field value two wide" id="companyAddr" />
						</div>
						<div class="field row">
							<label class="field label required one half wide">法定代表人：</label>
							<input type="text" class="field value two wide" id="legalMan" />
						</div>
						<div class="field row">
							<label class="field label required one half wide">注册资本：</label>
							<input type="text" class="field value two wide" id="regCapital" />万元
						</div>
						<div class="field row">
							<label class="field label required one half wide">成立日期</label>
							<input type="text" class="field value" id="estabDate" />
						</div>
						<div class="field row">
							<label class="field label required one half wide">营业期限</label>
							<div class="field group">
								<input type="text" class="field value" id="startDate" />
								至
								<input type="text" class="field value" id="endDate" />
							</div>
						</div>
						<div class="field row" style="height: 80px;">
							<label class="field label required one half wide">经营范围：</label>
							<textarea id="bizScope" class="field value three wide" style="height:75px;width: 260px;padding: 5px;"></textarea>
						</div>
					</div>
				<span id="divider" class="normal hr divider"></span>
				</div>
			</div>
		</div>
		<div id="showDialog" style="display: none;">
			<div id="showForm" class="form">
				<div style="margin: 10px 0;">
					<label style="font-size: 14px;color: #666;">管理员信息</label>
				</div>
				<div class="field row">
					<label class="field label required one half wide">手机号码</label> 
					<input type="text" id="showPhoneNo" class="field value one half wide" />
					<label class="field inline label required one half wide">管理员昵称</label> 
					<input type="text" id="showNickName" class="field value one half wide" />
					
				</div>
				<div style="margin: 10px 0;">
					<label style="font-size: 14px;color: #666;">商户信息</label>
				</div>
				<div class="field row">
					<label class="field label required one half wide">商户名</label> 
					<input type="text" id="showShopName" class="field value three wide" />
				</div>
				<div class="field row">
					<label class="field label required one half wide">真实姓名</label> 
					<input type="text" id="showRealName" class="field value one half wide" />
					<label class="field inline label required one half wide">身份证号</label> 
					<input type="text" id="showIdCertNo" class="field value one half wide" />
				</div>
				<div class="field row">
					<label class="field label required one half wide">身份证正面</label> 
					<input name="file" type="file" id="showLeftIdCertFile" multiple="multiple" class="field value one half wide"  /> 
					<button class="normal button" id="showLeftIdCertFileToUpload">上传</button>
				</div>
				<div class="field row">
					<input type="hidden" id="showLeftId" />
					<input type="hidden" id="showLeftImageUuid" />
					<input type="hidden" id="showLeftImageUsage" />
					<input type="hidden" id="showLeftImagePath" />
					<input type="hidden" id="showLeftImageBrowseUrl" />
					<label class="field label one half wide">图片预览</label>
		        	<img id="showLeftIdCertImg" height="80px" width="120px" src="<%=resBaseUrl%>/image-app/img-none.jpg"/>
				</div>
				<div class="field row" style="margin-top: 10px;">
					<label class="field label required one half wide" >身份证反面</label> 
					<input name="file" type="file" id="showRightIdCertFile" multiple="multiple" class="field value one half wide"  /> 
					<button class="normal button" id="showRightIdCertFileToUpload">上传</button>
				</div>
				<div class="field row">
					<input type="hidden" id="showRightId" />
					<input type="hidden" id="showRightImageUuid" />
					<input type="hidden" id="showRightImageUsage" />
					<input type="hidden" id="showRightImagePath" />
					<input type="hidden" id="showRightImageBrowseUrl" />
					<label class="field label one half wide">图片预览</label>
		        	<img id="showRightIdCertImg" height="80px" width="120px" src="<%=resBaseUrl%>/image-app/img-none.jpg"/>
				</div>
				<div class="field row" style="margin-top: 10px;">
					<label class="field label required one half wide">邮箱</label>
					<input type="text" id="showEmail" class="field value three wide" />
				</div>
				<div class="field row" style="height:60px;">
						<label class="field label one half wide" style="vertical-align: middle;">备注</label> 
						<textarea id="showMemo" class="field value three wide" style="height:50px;width: 450px;"></textarea>
				</div>
				<span id="divider" class="normal hr divider"></span>
				<div id="acctList" class="noBorder" style="margin-left: 20px;">
					<div style="margin: 10px 0;height: 25px;">
						<div class="group left aligned" style="float: left;">
							<label  style="font-size: 14px;color: #666;">资金账户</label>
						</div>
						<div class="group right aligned" style="float: right;">
							<button id="btnAddAccount" class="normal button">添加</button>
						</div>
					</div>
					<div style="text-align: center; vertical-align: middle;">
						<table class="gridtable" style="width: 100%;  border: 1px solid grey;">
							<thead><tr>
							    <th width="20%"><label class="normal label">账户类型</label></th>
								<th width="10%"><label class="normal label">开户名</label></th>
								<th width="25%"><label class="normal label">银行名称</label></th>
								<th width="25%"><label class="normal label">开户人</label></th>
								<th width="20%"><label class="normal label">预留号码</label></th>
							</tr></thead>
							<tbody id="userAccount_tbody"></tbody>
						</table>
					</div>
				</div>
				<div id="bizLicenseList" class="noBorder" style="margin-left: 20px;">
					<div style="margin: 10px 0;height: 25px">
						<div class="group left aligned" style="float: left;">
							<label style="font-size: 14px;color: #666;">营业执照</label>
						</div>
						<div class="group right aligned" style="float: right;">
							<button id="btnAddBizLicense" class="normal button">添加</button>
						</div>
					</div>
					<div style="text-align: center; vertical-align: middle;">
						<table class="gridtable" style="width: 100%;  border: 1px solid grey;">
							<thead><tr>
							    <th width="15%"><label class="normal label">注册号</label></th>
								<th width="10%"><label class="normal label">法定法人</label></th>
								<th width="20%"><label class="normal label">名称</label></th>
								<th width="20%"><label class="normal label">类型</label></th>
								<th width="10%"><label class="normal label">注册资本</label></th>
								<th width="25%"><label class="normal label">住所</label></th>
							</tr></thead>
							<tbody id="bizLicense_tbody"></tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<!--添加资金账户dialog -->
		<div id="addUserAccountDialog" class="form" style="display: none;height: 650px;">
			<div class="ui-layout-center">
				<div class="field row">
					<label class="field inline label one half wide required">账户类型</label>
					<div class="field group">
						<input id="entpFlag-Y" type="radio" name="addShopType" value="0" checked="checked"/>
						<label for="entpFlag-Y">个人</label>
						<input id="entpFlag-N" type="radio" name="addShopType" value="1"/>
						<label for="entpFlag-N">企业</label>
					</div>
				</div>
				<div class="field row">
					<label class="field label required one half wide">银行名称</label>
					<select id="addBankCode" class="field value two wide" onchange="changAddBankInfo();"></select>
				</div>
				<div class="field row">
					<label class="field label required one half wide">账户号</label> 
					<input type="text" id="addAcctNo" class="field value two wide" placeholder="账户号" style="height: 32px;line-height: 20px;padding-left: 10px;"/>
				</div>
				<div class="field row">
					<label class="field label required one half wide">开户名</label> 
					<input type="text" id="addAcctName" class="field value two wide" placeholder="开户人" style="height: 32px;line-height: 20px;padding-left: 10px;"/>
				</div>
				<div id="addBankInfo">
					<div class="field row">
						<label class="field label required one half wide">开户省份</label>
						<select class="field value two wide" id="addProvinceCode" style="height: 32px;line-height: 20px;padding-left: 10px;"></select>
					</div>
					<div class="field row">
						<label class="field label required one half wide">开户行名</label> 
						<input type="text" id="addBankFullName" class="field value two wide" placeholder="开户人" style="height: 32px;line-height: 20px;padding-left: 10px;"/>
					</div>
					<div class="field row">
						<label class="field label required one half wide">联行行号</label> 
						<input type="text" id="addRelatedBankNo" class="field value two wide" placeholder="开户人" style="height: 32px;line-height: 20px;padding-left: 10px;"/>
					</div>
					<div class="field row">
						<label class="field label one half wide">预留号码</label> 
						<input type="text" id="addAcctPhoneNo" class="field value two wide" placeholder="预留电话" style="height: 32px;line-height: 20px;padding-left: 10px;"/>
					</div>
					<div id="addVfCodeDiv" class="field row" style="display: none">
						<label class="field label one half wide">打款验证码</label> 
						<input type="text" id="addVfCode" class="field value two wide" placeholder="打款验证码" style="height: 32px;line-height: 20px;padding-left: 10px;" maxlength="30"/>
					</div>
				</div>
			</div>
		</div>
		<!--添加企业营业执照dialog -->
		<div id="addBizLicenseDialog" class="form" style="display: none;height: 600px;">
			<div class="ui-layout-center">
				<div class="field row">
					<label class="field label required one half wide">注册号：</label>
					<input class="field value two wide" id="addRegNo" placeholder="注册号" style="height: 32px;line-height: 20px;padding-left: 10px;"/>
				</div>
				<div class="field row">
					<label class="field label required one half wide">名称：</label>
					<input class="field value two wide" id="addCompanyName" placeholder="名称" style="height: 32px;line-height: 20px;padding-left: 10px;"/>
				</div>
				<div class="field row">
					<label class="field label required one half wide">营业执照图片</label> 
					<input name="file" type="file" id="addBizLicenseFile" multiple="multiple" class="field value one half wide"  /> 
					<button class="normal button" id="addBizLicenseFileToUpload">上传</button>
				</div>
				<div class="field row">
					<input type="hidden" id="addBizLicenseUuid" />
					<input type="hidden" id="addBizLicenseUsage" />
					<input type="hidden" id="addBizLicensePath" />
					<input type="hidden" id="addBizLicenseBrowseUrl" />
					<label class="field label one half wide">图片预览</label>
		        	<img id="addBizLicenseImg" height="80px" width="120px" />
				</div>
				<div class="field row" style="margin-top: 10px;">
					<label class="field label required one half wide">类型：</label>
					<input class="field value two wide" id="addCompanyType" placeholder="类型" style="height: 32px;line-height: 20px;padding-left: 10px;"/>
				</div>
				<div class="field row">
					<label class="field label required one half wide">住所：</label>
					<input class="field value two wide" id="addCompanyAddr" placeholder="住所" style="height: 32px;line-height: 20px;padding-left: 10px;"/>
				</div>
				<div class="field row">
					<label class="field label required one half wide">法定代表人 ：</label>
					<input class="field value two wide" id="addLegalMan" placeholder="法定代表人" style="height: 32px;line-height: 20px;padding-left: 10px;"/>
				</div>
				<div class="field row">
					<label class="field label required one half wide">注册资本：</label>
					<input class="field value two wide" id="addRegCapital" placeholder="注册资本" style="height: 32px;line-height: 20px;padding-left: 10px;"/>万元
				</div>
				<div class="field row">
					<label class="field label required one half wide">成立日期</label>
					<input class="field value" id="addEstabDate" />
				</div>
				<div class="field row">
					<label class="field label required one half wide">营业期限</label>
					<div class="field group">
						<input type="text" class="field value" id="addStartDate" />
						至
						<input type="text" class="field value" id="addEndDate" />
					</div>
				</div>
				<div class="field row" style="margin: 10px 0;height: 95px;">
					<label class="field label one half wide">经营范围：</label>
					<textarea id="addBizScope" class="field value three wide" style="height:75px;width:300px; padding: 5px;" placeholder="经营范围"></textarea>1000字以内
				</div>
			</div>
		</div>
		
		<!--选择资金账户dialog -->
		<div id="selectUserAccountDialog" class="form" style="display: none;height: 600px;">
			<div class="ui-layout-center">
				<table id="userAccountGrid"></table>
				<div id="userAccountPager"></div>
			</div>
		</div>
	</div>
	
	
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		//
		var isExistMerchantFlag = false;
		//添加商户信息页面
		var addDialog;
		//修改商户信息页面
		var updateDialog;
		//查看商户信息页面
		var showDialog;
		//查看商户结算资金账户
		var merchantSettleDialog;
		//批量导入商户信息页面
		var batchImpDialog;
		//
		var accountFormProxy = FormProxy.newOne();
		//
		var bizLicenseFormProxy = FormProxy.newOne();
		//
		var shopInfoFormProxy = FormProxy.newOne();
		//缓存当前jqGrid数据行数组
		var shopGridHelper = JqGridHelper.newOne("");
		//
	    var accountGridHelper = JqGridHelper.newOne("");
	    //
	    var bizLicenseGridHelper = JqGridHelper.newOne("");
		//店铺列表
		var shopTabs;
		//账户列表
		var acctTabs;
		//修改时原始数据快照
		var merchObj = "";
		//数据状态。是否改变
		var dataIsDirty = false;
		//
	    var accountGridCtrl = null;
		
	  //修改时原始数据快照
		var settleWayCode = "";
		
	  	//空函数，在弹出框消失后重写调用
		function getCallbackAfterGridLoaded() {
		}
		
		//------------------------初始化-------------------------
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
			
			jqGridCtrl= $("#theGridCtrl").jqGrid({
			      url : getAppUrl("/merch/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      height : "100%",
				  width : "100%",
			      colNames : ["id", "商户编号", "商户姓名","联系电话","状态","开店数量","注册时间 "," 操作"],  
			      colModel : [{name:"id", index:"id", hidden:true},  
			                  {name:"id", index:"id",width:100,align : 'center'}, 
			                  {name:"name", index:"name",width:150,align : 'center',}, 
			                  {name:"user.phoneNo", index:"user.phoneNo",width:150,align : 'center'}, 
			                  {name:"disabled", index:"disabled",width:100,align : 'center',formatter : function (cellValue) {
									return cellValue==false?'启用':'禁用';
								}},  
			                  {name:"shopCount", index:"shopCount",width:50,align : "center"},
			                  {name:"user.regTime", index:"user.regTime",width:200,align : "center"}, 
			                  {name:'id',index:'id', formatter : function (cellValue,option,rowObject) {
								var s = "<span> [<a class='item' href='javascript:void(0);' onclick='openShowDialog("
								+ JSON.stringify(rowObject)
								+ ")' >查看</a>]   [<a class='item' href='javascript:void(0);' onclick='openUpdateDialog("
								+ JSON.stringify(rowObject)
								+ ")' >修改</a>]   [<a class='item' href='javascript:void(0);' onclick='openMerchantSettleDialog("
								+ cellValue
								+ ")' >设置结算资金账户</a>]   ";
								if(rowObject.disabled==false){
									return s+"[<a class='item' href='javascript:void(0);' onclick='disableMerchant("
									+ JSON.stringify(rowObject)
									+ ")' >禁用</a>]";	
								}else{
									return s+"[<a class='item' href='javascript:void(0);' onclick='disableMerchant("
									+ JSON.stringify(rowObject)
									+ ")' >启用</a>]";
								}
							},
						width:200,align:"center"}
			                  ],  
				//rowList:[10,20,30],
				 multiselect:true,
				 multikey:'ctrlKey',
				pager : "#theGridPager",
				loadComplete : function(gridData) { // JqGridHelper缓存最新的grid数据
					shopGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if ($.isFunction(callback)) {
						callback();
					}
				},
				ondblClickRow: function(rowId) {
					var userMap = shopGridHelper.getRowData(rowId)
					openShowDialog(userMap);
				}
			});
			
			initFileUpload("impMerchXls");
			
			//身份证正面
			initFileUpload("leftIdCertFile");
			//身份证反面
			initFileUpload("rightIdCertFile");
			//营业执照照片
			initFileUpload("bizLicenseFile");
			//添加营业执照照片
			initFileUpload("addBizLicenseFile");
			//身份证正面
			initFileUpload("showLeftIdCertFile");
			//身份证反面
			initFileUpload("showRightIdCertFile");
			
			//绑定上传身份证正面
			$("#leftIdCertFileToUpload").click(function(){
				leftIdCertFileToUpload("leftIdCertFile");
			});
			//绑定上传身份证反面
			$("#rightIdCertFileToUpload").click(function(){
				rightIdCertFileToUpload("rightIdCertFile");
			});
			
			//绑定上传身份证正面
			$("#showLeftIdCertFileToUpload").click(function(){
				showLeftIdCertFileToUpload("showLeftIdCertFile");
			});
			//绑定上传身份证反面
			$("#showRightIdCertFileToUpload").click(function(){
				showRightIdCertFileToUpload("showRightIdCertFile");
			});
			
			//绑定营业执照照片
			$("#bizLicenseFileToUpload").click(function(){
				bizLicenseFileToUpload("bizLicenseFile");
			});
			
			//绑定添加营业执照照片
			$("#addBizLicenseFileToUpload").click(function(){
				addBizLicenseFileToUpload("addBizLicenseFile");
			});
			
			//初始化Dialog
			initAddAccountDialog();
			//初始化Dialog
			initAddBizLicenseDialog();
			//初始化结算资金账户
			initShowMerchantSettleDialog();
			
			loadUserAccountData();
			
			initSelectUserAccountDialog();
			
			var size = getImageSizeDef("image.logo");
			$("img").attr("width",size.width);
			$("img").attr("height",size.height);
			
			//绑定查询
			$id("btnQuery").click(queryMerchent);

			$id("btnAdd").click(openAddDialog);
			$id("btnBatchDel").click(batchDeleteByIds);
			$id("btnBatchImp").click(openImpDialog);
			
			//绑定添加资金账户
			$id("btnAddAccount").click(function(){
				goAddAccount();
			});
			
			//绑定添加营业执照
			$id("btnAddBizLicense").click(function(){
				goAddBizLicense();
			});
			
			//添加结算资金账户
			$id("btnAddMerchantSettleAcct").click(function(){
				goAddMerchantSettleAcct();
			});
			
			//
			$id("estabDate").datepicker({
				changeMonth: true,
		        changeYear: true,
		        showYearOrder: "desc",
				lang:'ch',
			});
			$id("addEstabDate").datepicker({
				changeMonth: true,
		        changeYear: true,
		        showYearOrder: "desc",
				lang:'ch',
			});
			//初始化开始结束日期
			initDatePicker({
				changeMonth: true,
		        changeYear: true,
		        showYearOrder: "desc",
				lang:'ch',
			});
			//初始化开始结束日期
			initAddDatePicker({
				changeMonth: true,
		        changeYear: true,
		        showYearOrder: "desc",
				lang:'ch',
			});
			
			//添加资金账号
			//$id("addAccountBtn").click(goAddAccount);
			
			$("input[name='shopType']").change(function(){
				//隐藏验证警告信息
				hideMiscTip("selectAccountBtn");
				hideMiscTip("selectBizLicenseBtn");
				//
				clearShowAccountDiv();
				var typeFlag = getShopTypeFlag();
				//1:对公账号 0：对私账号
				changLincenseLabel(typeFlag);
			});
			
			$("input[name='addShopType']").change(function(){
				//隐藏验证警告信息
				hideMiscTip("selectAccountBtn");
				hideMiscTip("selectBizLicenseBtn");
				//
				clearShowAccountDiv();
				var typeFlag = getAddShopTypeFlag();
				//1:对公账号 0：对私账号
				changAddLincenseLabel(typeFlag);
			});
			
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
		});
		
		function changBankInfo() {
			var bankName = $id("bankCode").find("option:selected").text()
			if(bankName == "微信支付" || bankName == "支付宝" || bankName == "财付通"){
				$id("bankInfo").css("display", "none");
				shopInfoProxy.addField({
					id : "provinceCode",
					messageTargetId : "provinceCode"
				});
				shopInfoProxy.addField({
					id : "bankFullName",
					messageTargetId : "bankFullName"
				});
				shopInfoProxy.addField({
					id : "relatedBankNo",
					messageTargetId : "relatedBankNo"
				});
				shopInfoProxy.addField({
					id : "acctPhoneNo",
					rules : ["isMobile"],
					messageTargetId : "acctPhoneNo"
				});
			}else{
				$id("bankInfo").css("display", "");
				shopInfoProxy.addField({
					id : "provinceCode",
					required : true,
					messageTargetId : "provinceCode"
				});
				shopInfoProxy.addField({
					id : "bankFullName",
					required : true,
					messageTargetId : "bankFullName"
				});
				shopInfoProxy.addField({
					id : "relatedBankNo",
					required : true,
					messageTargetId : "relatedBankNo"
				});
				shopInfoProxy.addField({
					id : "acctPhoneNo",
					rules : ["isMobile"],
					messageTargetId : "acctPhoneNo"
				});
			}
		}
		
		function changAddBankInfo() {
			var addBankName = $id("addBankCode").find("option:selected").text()
			if(addBankName == "微信支付" || addBankName == "支付宝" || addBankName == "财付通"){
				$id("addBankInfo").css("display", "none");
				accountFormProxy.addField({
					id : "addProvinceCode",
					messageTargetId : "addProvinceCode"
				});
				accountFormProxy.addField({
					id : "addBankFullName",
					messageTargetId : "addBankFullName"
				});
				accountFormProxy.addField({
					id : "addRelatedBankNo",
					messageTargetId : "addRelatedBankNo"
				});
				accountFormProxy.addField({
					id : "addAcctPhoneNo",
					rules : ["isMobile", "maxLength[15]"],
					messageTargetId : "addAcctPhoneNo"
				});
			}else{
				$id("addBankInfo").css("display", "");
				accountFormProxy.addField({
					id : "addProvinceCode",
					required : true,
					messageTargetId : "addProvinceCode"
				});
				accountFormProxy.addField({
					id : "addBankFullName",
					required : true,
					messageTargetId : "addBankFullName"
				});
				accountFormProxy.addField({
					id : "addRelatedBankNo",
					required : true,
					messageTargetId : "addRelatedBankNo"
				});
				accountFormProxy.addField({
					id : "addAcctPhoneNo",
					rules : ["isMobile", "maxLength[15]"],
					messageTargetId : "addAcctPhoneNo"
				});
			}
		}
		
		//获取开会省份
		function loadBankProvinceCode() {
			// 隐藏页面区
			var ajax = Ajax.post("/userAccount/bankProvinceCode/selectList/get");
			ajax.params({
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("provinceCode", result.data);
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		//获取开户省份
		function loadAddBankProvinceCode() {
			// 隐藏页面区
			var ajax = Ajax.post("/userAccount/bankProvinceCode/selectList/get");
			ajax.params({
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("addProvinceCode", result.data);
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		//------------------------------封装数据-----------------
		//实例化表单代理
		var shopInfoProxy = FormProxy.newOne();
		
		//注册表单控件
		shopInfoProxy.addField({
			id : "nickName",
			key : "user.nickName",
			required : true,
			rules : ["maxLength[30]"]
		});
		shopInfoProxy.addField({
			id : "phoneNo",
			key : "user.phoneNo",
			required : true,
			rules : ["maxLength[11]","isMobile",
			         {
				rule : function(idOrName, type, rawValue, curData) {
					checkPhoneNo(rawValue);
					return !phoneFlag;
				},
				message : "手机号码已注册！"
			}]
		});
		shopInfoProxy.addField({
			id : "shopName",
			key : "name",
			required : true,
			rules : ["maxLength[30]"]
		});
		shopInfoProxy.addField({
			id : "realName",
			key : "user.realName",
			required : true,
			rules : ["maxLength[30]"]
		});
		shopInfoProxy.addField({
			id : "idCertNo",
			key : "user.idCertNo",
			required : true,
			rules : ["maxLength[18]",{
				rule : function(idOrName, type, rawValue, curData) {
					var checkStr = rawValue;
					if (checkStr == null || checkStr.length > 18) {
						return false;
					}
					var regExp = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
					return regExp.test(checkStr);
				},
				message : "身份证号码错误！"
			}]
		});
		shopInfoProxy.addField({
			id : "leftIdCertImg",
			rules : [{
				rule : function(idOrName, type, rawValue, curData) {
					var img = $id("leftIdCertImg");
					var imgSrc = $(img).attr("src");
					var defaultImg = "img-none.jpg";
					
					var isSearch = strContains(imgSrc, defaultImg, false);
					if(isSearch){
						return false;
					}
					return true;
				},
				message: "亲,您还未上传身份证图片哦！"
			}],
			messageTargetId : "leftIdCertImg"
		});
		//
		shopInfoProxy.addField({
			id : "rightIdCertImg",
			rules : [{
				rule : function(idOrName, type, rawValue, curData) {
					var img = $id("rightIdCertImg");
					var imgSrc = $(img).attr("src");
					var defaultImg = "img-none.jpg";
					
					var isSearch = strContains(imgSrc, defaultImg, false);
					if(isSearch){
						return false;
					}
					return true;
				},
				message: "亲,您还未上传身份证图片哦！"
			}],
			messageTargetId : "rightIdCertImg"
		});
		shopInfoProxy.addField({
			id : "email",
			key : "user.email",
			required : true,
			rules : ["maxLength[60]", "isEmail", {
				rule : function(idOrName, type, rawValue, curData) {
					validateEmail(rawValue);
					return !emailFlag;
				},
				message : "邮箱已被注册！"
			}]
		});
		//-------------------------------------
		shopInfoProxy.addField({
			id : "bankCode",
			required : true,
			messageTargetId : "bankCode"
		});
		shopInfoProxy.addField({
			id : "acctNo",
			required : true,
			messageTargetId : "acctNo"
		});
		shopInfoProxy.addField({
			id : "acctName",
			required : true,
			messageTargetId : "acctName"
		});
		//-------------------------------------
		shopInfoProxy.addField({
			id : "regNo",
			required : true,
			rules : ["maxLength[30]"],
			messageTargetId : "regNo"
		});
		shopInfoProxy.addField({
			id : "companyName",
			required : true,
			rules : ["maxLength[30]"],
			messageTargetId : "companyName"
		});
		shopInfoProxy.addField({
			id : "bizLicenseImg",
			rules : [{
				rule : function(idOrName, type, rawValue, curData) {
					var img = $id("bizLicenseImg");
					var imgSrc = $(img).attr("src");
					var defaultImg = "img-none.jpg";
					
					var isSearch = strContains(imgSrc, defaultImg, false);
					if(isSearch){
						return false;
					}
					return true;
				},
				message: "亲,您还未上传营业执照图片哦！"
			}],
			messageTargetId : "bizLicenseImg"
		});
		shopInfoProxy.addField({
			id : "companyType",
			required : true,
			rules : ["maxLength[30]"],
			messageTargetId : "companyType"
		});
		shopInfoProxy.addField({
			id : "companyAddr",
			required : true,
			rules : ["maxLength[100]"],
			messageTargetId : "companyAddr"
		});
		shopInfoProxy.addField({
			id : "legalMan",
			required : true,
			rules : ["maxLength[30]"],
			messageTargetId : "legalMan"
		});
		shopInfoProxy.addField({
			id : "regCapital",
			required : true,
			rules : ["isMoney","maxLength[16]"],
			messageTargetId : "regCapital"
		});
		
		shopInfoProxy.addField({
			id : "estabDate",
			required : true,
			rules : ["isDate"],
			messageTargetId : "estabDate"
		});
		shopInfoProxy.addField({
			id : "startDate",
			required : true,
			rules : ["isDate"],
			messageTargetId : "startDate"
		});
		shopInfoProxy.addField({
			id : "endDate",
			required : true,
			rules : ["isDate"],
			messageTargetId : "endDate"
		});
		shopInfoProxy.addField({
			id : "bizScope",
			rules : ["maxLength[1000]"],
			messageTargetId : "bizScope"
		});
		
		//实例化表单代理
		var showShopInfoProxy = FormProxy.newOne();
		
		//注册表单控件
		showShopInfoProxy.addField({
			id : "showNickName",
			key : "user.nickName",
			required : true,
			rules : ["maxLength[30]"]
		});
		showShopInfoProxy.addField({
			id : "showPhoneNo",
			key : "user.phoneNo",
			required : true,
			rules : ["maxLength[11]","isMobile",
			         {
				rule : function(idOrName, type, rawValue, curData) {
					checkPhoneNo(rawValue);
					return !phoneFlag;
				},
				message : "手机号码已注册！"
			}]
		});
		showShopInfoProxy.addField({
			id : "showShopName",
			key : "name",
			required : true,
			rules : ["maxLength[30]"]
		});
		showShopInfoProxy.addField({
			id : "showRealName",
			key : "user.realName",
			required : true,
			rules : ["maxLength[30]"]
		});
		showShopInfoProxy.addField({
			id : "showIdCertNo",
			key : "user.idCertNo",
			required : true,
			rules : ["maxLength[18]",{
				rule : function(idOrName, type, rawValue, curData) {
					var checkStr = rawValue;
					if (checkStr == null || checkStr.length > 18) {
						return false;
					}
					var regExp = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
					return regExp.test(checkStr);
				},
				message : "身份证号码错误！"
			}]
		});
		showShopInfoProxy.addField({
			id : "showLeftIdCertImg",
			rules : [{
				rule : function(idOrName, type, rawValue, curData) {
					var img = $id("showLeftIdCertImg");
					var imgSrc = $(img).attr("src");
					var defaultImg = "img-none.jpg";
					
					var isSearch = strContains(imgSrc, defaultImg, false);
					if(isSearch){
						return false;
					}
					return true;
				},
				message: "亲,您还未上传身份证图片哦！"
			}],
			messageTargetId : "showLeftIdCertImg"
		});
		//
		showShopInfoProxy.addField({
			id : "showRightIdCertImg",
			rules : [{
				rule : function(idOrName, type, rawValue, curData) {
					var img = $id("showRightIdCertImg");
					var imgSrc = $(img).attr("src");
					var defaultImg = "img-none.jpg";
					
					var isSearch = strContains(imgSrc, defaultImg, false);
					if(isSearch){
						return false;
					}
					return true;
				},
				message: "亲,您还未上传身份证图片哦！"
			}],
			messageTargetId : "showRightIdCertImg"
		});
		showShopInfoProxy.addField({
			id : "showEmail",
			key : "user.email",
			required : true,
			rules : ["maxLength[60]", "isEmail", {
				rule : function(idOrName, type, rawValue, curData) {
					validateEmail(rawValue);
					return !emailFlag;
				},
				message : "邮箱已被注册！"
			}]
		});
		showShopInfoProxy.addField({
			id : "showMemo",
			key : "showMemo",
			rules : ["maxLength[250]"]
		});
		//-------------------------------------
		accountFormProxy.addField({
			id : "addBankCode",
			required : true,
			rules : ["maxLength[15]"],
			messageTargetId : "addBankCode"
		});
		accountFormProxy.addField({
			id : "addAcctNo",
			required : true,
			rules : ["maxLength[30]"],
			messageTargetId : "addAcctNo"
		});
		accountFormProxy.addField({
			id : "addAcctName",
			required : true,
			rules : ["maxLength[30]"],
			messageTargetId : "addAcctName"
		});
		
		//-------------------------------------
		bizLicenseFormProxy.addField({
			id : "addRegNo",
			required : true,
			rules : ["maxLength[30]"],
			messageTargetId : "addRegNo"
		});
		bizLicenseFormProxy.addField({
			id : "addCompanyName",
			required : true,
			rules : ["maxLength[30]"],
			messageTargetId : "addCompanyName"
		});
		bizLicenseFormProxy.addField({
			id : "addBizLicenseImg",
			rules : [{
				rule : function(idOrName, type, rawValue, curData) {
					var img = $id("addBizLicenseImg");
					var imgSrc = $(img).attr("src");
					var defaultImg = "img-none.jpg";
					
					var isSearch = strContains(imgSrc, defaultImg, false);
					if(isSearch){
						return false;
					}
					return true;
				},
				message: "亲,您还未上传营业执照图片哦！"
			}],
			messageTargetId : "addBizLicenseImg"
		});
		bizLicenseFormProxy.addField({
			id : "addCompanyType",
			required : true,
			rules : ["maxLength[30]"],
			messageTargetId : "addCompanyType"
		});
		bizLicenseFormProxy.addField({
			id : "addCompanyAddr",
			required : true,
			rules : ["maxLength[100]"],
			messageTargetId : "addCompanyAddr"
		});
		bizLicenseFormProxy.addField({
			id : "addLegalMan",
			required : true,
			rules : ["maxLength[30]"],
			messageTargetId : "addLegalMan"
		});
		bizLicenseFormProxy.addField({
			id : "addRegCapital",
			required : true,
			rules : ["isMoney","maxLength[16]"],
			messageTargetId : "addRegCapital"
		});
		
		bizLicenseFormProxy.addField({
			id : "addEstabDate",
			required : true,
			rules : ["isDate"],
			messageTargetId : "addEstabDate"
		});
		bizLicenseFormProxy.addField({
			id : "addStartDate",
			required : true,
			rules : ["isDate"],
			messageTargetId : "addStartDate"
		});
		bizLicenseFormProxy.addField({
			id : "addEndDate",
			required : true,
			rules : ["isDate"],
			messageTargetId : "addEndDate"
		});
		bizLicenseFormProxy.addField({
			id : "addBizScope",
			rules : ["maxLength[1000]"],
			messageTargetId : "addBizScope"
		});
		
		//初始化日历插件
		 function initDatePicker(){
			var startDate = textGet("startDate");
			startDate = isNoB(startDate) ? null : startDate;
			var endDate = textGet("endDate");
			endDate = isNoB(endDate) ? null : endDate;
			$id("startDate").datepicker({
				maxDate : endDate,
				changeMonth: true,
		        changeYear: true,
		        showYearOrder: "desc",
				lang:'ch',
			    onSelect:function(dateText,inst){
			       $id("endDate").datepicker("option","minDate",dateText);
			       shopInfoProxy.validate("startDate");
			    }
			});
			//
			$id("endDate").datepicker({
				minDate :startDate == null ? 0 : startDate, 
				changeMonth: true,
		        changeYear: true,
		        showYearOrder: "desc",
				lang:'ch',
				onSelect:function(dateText,inst){
			        $id("startDate").datepicker("option","maxDate",dateText);
			        shopInfoProxy.validate("endDate");
			    }
			});
		 }
		
		//初始化日历插件
		 function initAddDatePicker(){
			var addStartDate = textGet("addStartDate");
			addStartDate = isNoB(addStartDate) ? null : addStartDate;
			var addEndDate = textGet("addEndDate");
			addEndDate = isNoB(addEndDate) ? null : addEndDate;
			$id("addStartDate").datepicker({
				maxDate : addEndDate,
				changeMonth: true,
		        changeYear: true,
		        showYearOrder: "desc",
				lang:'ch',
			    onSelect:function(dateText,inst){
			       $id("addEndDate").datepicker("option","minDate",dateText);
			       bizLicenseFormProxy.validate("addStartDate");
			    }
			});
			//
			$id("addEndDate").datepicker({
				minDate :addStartDate == null ? 0 : addStartDate, 
				changeMonth: true,
		        changeYear: true,
		        showYearOrder: "desc",
				lang:'ch',
				onSelect:function(dateText,inst){
			        $id("addStartDate").datepicker("option","maxDate",dateText);
			        bizLicenseFormProxy.validate("addEndDate");
			    }
			});
		 }
		
		
		function queryMerchent(){ 
			var filter = {};
			var id = textGet("queryId");
			if(id){
				filter.id = id;
			}
			var name = textGet("queryRealName");
			if(name){
				filter.name = name;
			}
			var queryDisabled = singleGet("queryDisabled");
			if(queryDisabled && queryDisabled != "全部"){
				filter.disabled = queryDisabled;
			}
			//加载jqGridCtrl
			jqGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
		}
		
		//--------------------------初始化dialog--------------
		//弹出新增商户页面  
		function openAddDialog() {
			$("#addDialog input[type=text]").val("");
			$("#addDialog input[type=text]").attr("disabled",false);
			$("#addDialog textarea").val("");
			$("#leftIdCertImg").attr("src",getResUrl("/image-app/img-none.jpg"));
			$("#rightIdCertImg").attr("src",getResUrl("/image-app/img-none.jpg"));
			$("#bizLicenseImg").attr("src",getResUrl("/image-app/img-none.jpg"));
			$("#userId").val("");
			$("#showLeftId").val("");
			$("#showRightId").val("");
			$("#bizScope").val("");
			$id("bankInfo").css("display", "");
			
			
			$id("divider").show();
			$id("acctDivider").show();
			loadBanks();
			initAddDialog();
			loadBankProvinceCode();
			addDialog.dialog("open");
		}
		
		//弹出导入商户页面  
		function openImpDialog(obj) {
			initbatchImpDialog();
			batchImpDialog.dialog("open");
		}
		
		//弹出查看商户页面  
		function openShowDialog(obj) {
			loadBanks();
			$id("divider").hide();
			$id("acctDivider").hide();
			initShowDialog(obj);
			showDialog.dialog("open");
		}
		
		//初始化查看页面
		function initShowDialog(obj) {
			$("#showDialog  input").attr("disabled",true);
			$("#showDialog textarea").attr("disabled",true);
			$id("btnAddAccount").css("display", "none");
			$id("btnAddBizLicense").css("display", "none");
			initSetShowDialog(obj);
			showDialog = $("#showDialog").dialog({
				autoOpen : false,
				height : Math.min(650, $(window).height()),
				width : Math.min(700, $(window).width()),
				modal : true,
				title : '查看商户',
				buttons : {
					"关闭" : function() {
						showDialog.dialog("close");
					}
				},
				close : function() {
					shopInfoProxy.hideMessages();
				}
			});
		}
		
		//弹出修改商户页面  
		function openUpdateDialog(obj) {
			loadBanks();
			$id("divider").hide();
			$id("acctDivider").hide();
			merchObj = obj;
			initUpdateDialog(obj);
			showDialog.dialog("open");
		}
		
		//初始化修改页面
		function initUpdateDialog(obj) {
			$("#showDialog  input").attr("disabled",false);
			$("#showDialog textarea").attr("disabled",false);
			$id("btnAddAccount").css("display", "block");
			$id("btnAddBizLicense").css("display", "block");
			initSetShowDialog(obj);
			showDialog = $("#showDialog").dialog({
				autoOpen : false,
				height : Math.min(650, $(window).height()),
				width : Math.min(700, $(window).width()),
				modal : true,
				title : '修改商户',
				buttons : {
					"保存" : function() {
						goUpdateMerchant();
					},
					"关闭" : function() {
						showDialog.dialog("close");
						showShopInfoProxy.hideMessages();
					}
				},
				close : function() {
					showShopInfoProxy.hideMessages();
				}
			});
		}
		
		function initSetShowDialog(obj){
			var userAccts = obj.user.userAccts;
			renderUserAccountDialog(userAccts);
			var bizLicenses = obj.user.bizLicenses;
			renderBizLicenseDialog(bizLicenses);
			textSet("userId", obj.user.id);
			textSet("showNickName", obj.user.nickName);
			textSet("showPhoneNo", obj.user.phoneNo);
			textSet("showShopName", obj.name);
			textSet("showRealName", obj.realName);
			textSet("showIdCertNo", obj.idCertNo);
			var merchantPics = obj.merchantPics;
			for(var i = 0, len = merchantPics.length; i < len; i++){ 
				var code = merchantPics[i].code;
				var fileBrowseUrl = merchantPics[i].fileBrowseUrl;
				if(code == "IDCert.front"){
					textSet("showLeftId", merchantPics[i].id);
					textSet("showLeftImageUuid", merchantPics[i].imageUuid);
					textSet("showLeftImageUsage", merchantPics[i].imageUsage);
					textSet("showLeftImagePath", merchantPics[i].imagePath);
					$("#showLeftIdCertImg").attr("src",fileBrowseUrl+"?"+new Date().getTime());
				}else{
					textSet("showRightId", merchantPics[i].id);
					textSet("showRightImageUuid", merchantPics[i].imageUuid);
					textSet("showRightImageUsage", merchantPics[i].imageUsage);
					textSet("showRightImagePath", merchantPics[i].imagePath);
					$("#showRightIdCertImg").attr("src",fileBrowseUrl+"?"+new Date().getTime());
				}
			}
			textSet("showEmail", obj.user.email);
			textSet("showMemo", obj.memo);
		}
		
		function showUserAccountDialog(id){
			merchant = shopGridHelper.getRowData("id",id);
			renderUserAccountDialog(merchant.user.userAccts);
		}
		
		function renderUserAccountDialog(userAccountList){
			if(userAccountList != null){
				$id("userAccount_tbody").html("");
				//获取模板内容
				var tplHtml = $id("userAccountTpl").html();
				//生成/编译模板
				var htmlTpl = laytpl(tplHtml);
				//根据模板和数据生成最终内容
				var htmlText = htmlTpl.render(userAccountList);
				//使用生成的内容
				$id("acctList").find("tbody").html(htmlText);
			} else {
				$id("acctList").find("tbody").html("暂无信息");
			}
			
		}
		
		function renderBizLicenseDialog(bizLicenseList){
			if(bizLicenseList != null){
				$id("bizLicense_tbody").html("");
				var theTpl = laytpl( $id("bizLicenseTpl").html());
				var htmlStr = theTpl.render(bizLicenseList);
				$id("bizLicenseList").find("tbody").html(htmlStr);
			} else {
				$id("bizLicenseList").find("tbody").html("暂无信息");
			}
			
		}
		
		//初始化添加页面
		function initAddDialog() {
			$("#dialog input[type=text]").val("");
			addDialog = $("#addDialog").dialog({
				autoOpen : false,
				height : Math.min(700, $(window).height()),
				width : Math.min(750, $(window).width()),
				modal : true,
				title : '添加商户',
				buttons : {
					"确定" : goAddMerchant,
					"取消" : function() {
						addDialog.dialog("close");
					}
				},
				close : function() {
					shopInfoProxy.hideMessages();
				}
			});
		}
		
		//初始化添加资金账户Dialog
		function initAddAccountDialog(){
			addUserAccountDialog = $( "#addUserAccountDialog" ).dialog({
		        autoOpen: false,
		        width : Math.min(600, $window.width()),
		        height : Math.min(500, $window.height()),
		        modal: true,
		        title : '添加资金账户',
		        buttons: {
		            "保存": function(){
		            	goSaveAccount();
		            },
		            "关闭": function() {
		            	addUserAccountDialog.dialog( "close" );
		            	accountFormProxy.hideMessages();
		          }
		        },
		        close: function() {
		        	addUserAccountDialog.dialog( "close" );
		        	accountFormProxy.hideMessages();
		        }
		      });
		}
		
		//初始化添加企业营业执照Dialog
		function initAddBizLicenseDialog(){
			addBizLicenseDialog = $( "#addBizLicenseDialog" ).dialog({
		        autoOpen: false,
		        width : Math.min(600, $window.width()),
		        height : Math.min(630, $window.height()),
		        modal: true,
		        title : '添加企业营业执照',
		        buttons: {
		            "保存": function(){
		            	goSaveBizLicense();
		            },
		            "关闭": function() {
		            	addBizLicenseDialog.dialog( "close" );
		            	bizLicenseFormProxy.hideMessages();
		          }
		        },
		        close: function() {
		        	addBizLicenseDialog.dialog( "close" );
		        	bizLicenseFormProxy.hideMessages();
		        }
		      });
		}
		
		function goSaveAccount(){
			var result = accountFormProxy.validateAll();
			if(result){
				var accountMap = getUserAccount();
				saveUserAccount(accountMap);
				
				addUserAccountDialog.dialog("close");
			}
		}
		
		function getUserAccount(){
			var accountMap = {};
			accountMap["userId"] = textGet("userId");
			accountMap["bankCode"] = textGet("addBankCode");
			accountMap["bankName"] = $id("addBankCode").find("option:selected").text();
			accountMap["acctNo"] = textGet("addAcctNo");
			accountMap["acctName"] = textGet("addAcctName");
			accountMap["provinceCode"] = textGet("addProvinceCode");
			accountMap["bankFullName"] = textGet("addBankFullName");
			accountMap["relatedBankNo"] = textGet("addRelatedBankNo");
			accountMap["phoneNo"] = textGet("addAcctPhoneNo");
			accountMap["typeFlag"] = getAddShopTypeFlag();
			return accountMap;
		}
		
		function saveUserAccount(accountMap){
			var hintBox = Layer.progress("正在保存数据...");

			var ajax = Ajax.post("/userAccount/add/do");
			
			ajax.data(accountMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					var userAccount = result.data;
					Layer.msgSuccess(result.message);
					var account = result.data;
					var accountId = account.id;
					addUserAccountDialog.dialog("close");
					//加载最新数据列表
					var typeFlag = getShopTypeFlag();
					var id = userAccount.userId;
					jqGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
					getCallbackAfterGridLoaded = function(){
						return function(){
							showUserAccountDialog(id);
						};
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
		
		function saveBizLicense(licenseMap){
			var hintBox = Layer.progress("正在保存数据...");

			var ajax = Ajax.post("/comn/bizLicense/add/do");
			
			ajax.data(licenseMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					var bizLicense = result.data;
					var licenseId = bizLicense.id;
					addBizLicenseDialog.dialog("close");
					
					bizLicenseGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid");
					//
					getCallbackAfterGridLoaded = function(){
						return function(){
							setCheckedBizLicense(licenseId);
						};
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
		
		function getBizLicenseMap(){
			var licenseMap = {};
			licenseMap["userId"] = textGet("userId");
			licenseMap["regNo"] = textGet("addRegNo");
			licenseMap["name"] = textGet("addCompanyName");
			licenseMap["type"] = textGet("addCompanyType");
			licenseMap["address"] = textGet("addCompanyAddr");
			licenseMap["legalMan"] = textGet("addLegalMan");
			licenseMap["regCapital"] = textGet("addRegCapital");
			licenseMap["estabDate"] = textGet("addEstabDate");
			licenseMap["startDate"] = textGet("addStartDate");
			licenseMap["endDate"] = textGet("addEndDate");
			licenseMap["bizScope"] = textGet("addBizScope");
			var licensePicmap = {};
			licensePicmap["title"] = "营业执照图片";
			licensePicmap["imageUuid"] = textGet("bizLicenseUuid");
			licensePicmap["imageUsage"] = textGet("bizLicenseUsage");
			licensePicmap["imagePath"] = textGet("bizLicensePath");
			licenseMap["bizLicensePic"] = licensePicmap;
			return licenseMap;
		}
		
		function goSaveBizLicense(){
			var result = bizLicenseFormProxy.validateAll();
			if(result){
				var licenseMap = getBizLicenseMap();
				saveBizLicense(licenseMap);
			}
		}
		
		function clearAddBizLicenseDialog(){
			$id("addRegNo").val("");
			$id("addCompanyName").val("");
			var src = getResUrl("/image-app/img-none.jpg");
			$id("addBizLicenseImg").attr("src", src);
			$id("addCompanyType").val("");
			$id("addCompanyAddr").val("");
			$id("addLegalMan").val("");
			$id("addRegCapital").val("");
			$id("addEstabDate").val("");
			$id("addStartDate").val("");
			$id("addEndDate").val("");
			$id("addBizScope").val("");
		}
		
		//--------------------------业务----------------------------
		//选择账户类型
		function getShopTypeFlag(){
			//1:对公账号 0：对私账号
			var typeFlag = radioGet("shopType");
			typeFlag = ParseInt(typeFlag);
			return typeFlag;
		}
		
		//选择账户类型
		function getAddShopTypeFlag(){
			//1:对公账号 0：对私账号
			var typeFlag = radioGet("addShopType");
			typeFlag = ParseInt(typeFlag);
			return typeFlag;
		}
		
		//改变内容
		function changLincenseLabel(typeFlag){
			//1:对公账号 0：对私账号
			if(typeFlag){
				$id("vfCodeDiv").css("display", "block");
			}else{
				$id("vfCodeDiv").css("display", "none");
			}
		}
		
		//改变内容
		function changAddLincenseLabel(typeFlag){
			//1:对公账号 0：对私账号
			if(typeFlag){
				$id("addVfCodeDiv").css("display", "block");
			}else{
				$id("addVfCodeDiv").css("display", "none");
			}
		}
		
		//删除多个
		function batchDeleteByIds(){
			var ids=jqGridCtrl.jqGrid("getGridParam","selarrrow");
			if(ids==""){
				Layer.info("请选择要删除的数据！");
			}else{
				var theLayer = {};
				theLayer = Layer.confirm('商户下的账户、店铺、个人信息等数据将全部删除，确定要删除该商户吗？', function(){
				var hintBox = Layer.progress("正在删除...");
				var postData = { ids : ids  };
			  	var ajax = Ajax.post("/merch/delete/by/ids");
			  	    ajax.data(postData);
				    ajax.done(function(result, jqXhr){
				    	if(result.type== "info"){
							theLayer.hide();
							Layer.msgSuccess(result.message);
							queryMerchent();
						}else{
							theLayer.hide();
							Layer.msgWarning(result.message);
						}
				 });
			    ajax.always(function() {
					hintBox.hide();
				});
				ajax.go();
				});
			}
		  }
		function goUpdateMerchant(){
			var result = showShopInfoProxy.validateAll();
			if(result){
				var shopInfoMap = getUpdateShopInfoMap();
				updateMerchant(shopInfoMap);
			}
		}
		
		function getUpdateShopInfoMap(){
			var merchantMap = {};
			//用户信息
			var userMap = {};
			userMap["nickName"] = textGet("showNickName");
			userMap["phoneNo"] = textGet("showPhoneNo");
			userMap["id"] = textGet("userId");
			userMap["email"] = textGet("showEmail");
			merchantMap["user"] = userMap;
			
			merchantMap["id"] = intGet("userId");
			merchantMap["name"] = textGet("showShopName");
			merchantMap["realName"] = textGet("showRealName");
			merchantMap["idCertNo"] = textGet("showIdCertNo");
			merchantMap["email"] = textGet("showEmail");
			merchantMap["memo"] = textGet("showMemo");
			merchantMap["disabled"] = "false";
			//身份证正面
			var merchantPics = [];
			var merchantFPicMap = {};
			merchantFPicMap["code"] = "IDCert.front";
			merchantFPicMap["title"] = "身份证正面图片";
			merchantFPicMap["id"] = textGet("showLeftId");
			merchantFPicMap["imageUuid"] = textGet("showLeftImageUuid");
			merchantFPicMap["imageUsage"] = textGet("showLeftImageUsage");
			merchantFPicMap["imagePath"] = textGet("showLeftImagePath");
			merchantPics.add(merchantFPicMap);
			//身份证反面
			var merchantBPicMap = {};
			merchantBPicMap["code"] = "IDCert.back";
			merchantBPicMap["title"] = "身份证反面图片";
			merchantBPicMap["id"] = textGet("showRightId");
			merchantBPicMap["imageUuid"] = textGet("showRightImageUuid");
			merchantBPicMap["imageUsage"] = textGet("showRightImageUsage");
			merchantBPicMap["imagePath"] = textGet("showRightImagePath");
			merchantPics.add(merchantBPicMap);
			merchantMap["merchantPics"] = merchantPics;
			//
			return merchantMap;
		}
		
		function updateMerchant(shopInfoMap){
			var hintBox = Layer.progress("正在保存数据...");

			var ajax = Ajax.post("/merch/update/do");
			ajax.data(shopInfoMap);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					showDialog.dialog("close");
					
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
		
		function goAddMerchant(){
			var result = shopInfoProxy.validateAll();
			if(result){
				var shopInfoMap = getShopInfoMap();
				addMerchant(shopInfoMap);
			}
		}
		
		function getShopInfoMap(){
			var shopInfoMap = {};
			//用户信息
			var userMap = {};
			userMap["nickName"] = textGet("nickName");
			userMap["phoneNo"] = textGet("phoneNo");
			userMap["id"] = textGet("userId");
			userMap["email"] = textGet("email");
			shopInfoMap["user"] = userMap;
			
			if(isExistMerchantFlag){
				shopInfoMap["merchantId"] = intGet("merchantId");
			}else{
				var merchantMap = {};
				merchantMap["name"] = textGet("shopName");
				merchantMap["realName"] = textGet("realName");
				merchantMap["idCertNo"] = textGet("idCertNo");
				merchantMap["email"] = textGet("email");
				merchantMap["memo"] = textGet("memo");
				shopInfoMap["merchant"] = merchantMap;
				//身份证正面
				var merchantPics = [];
				var merchantFPicMap = {};
				merchantFPicMap["code"] = "IDCert.front";
				merchantFPicMap["title"] = "身份证正面图片";
				merchantFPicMap["imageUuid"] = textGet("leftImageUuid");
				merchantFPicMap["imageUsage"] = textGet("leftImageUsage");
				merchantFPicMap["imagePath"] = textGet("leftImagePath");
				merchantPics.add(merchantFPicMap);
				//身份证反面
				var merchantBPicMap = {};
				merchantBPicMap["code"] = "IDCert.back";
				merchantBPicMap["title"] = "身份证反面图片";
				merchantBPicMap["imageUuid"] = textGet("rightImageUuid");
				merchantBPicMap["imageUsage"] = textGet("rightImageUsage");
				merchantBPicMap["imagePath"] = textGet("rightImagePath");
				merchantPics.add(merchantBPicMap);
				shopInfoMap["merchantPics"] = merchantPics;
			}
			//资金账户
			shopInfoMap["accountId"] = intGet("accountId");
			var accountMap = {};
			accountMap["typeFlag"] = getShopTypeFlag();
			accountMap["bankCode"] = singleGet("bankCode");
			accountMap["bankName"] = $id("bankCode").find("option:selected").text();
			accountMap["acctNo"] = textGet("acctNo");
			accountMap["acctName"] = textGet("acctName");
			accountMap["provinceCode"] = textGet("provinceCode");
			accountMap["bankFullName"] = textGet("bankFullName");
			accountMap["relatedBankNo"] = textGet("relatedBankNo");
			accountMap["phoneNo"] = textGet("acctPhoneNo");
			accountMap["vfCode"] = textGet("vfCode");
			shopInfoMap["userAccount"] = accountMap;
			
			//企业营业执照
			shopInfoMap["licenseId"] = intGet("licenseId");
			var bizLicenseMap = {};
			bizLicenseMap["regNo"] = textGet("regNo")
			bizLicenseMap["name"] = textGet("companyName")
			bizLicenseMap["type"] = textGet("companyType");
			bizLicenseMap["address"] = textGet("companyAddr");
			bizLicenseMap["legalMan"] = textGet("legalMan");
			bizLicenseMap["regCapital"] = textGet("regCapital");
			bizLicenseMap["estabDate"] = textGet("estabDate");
			bizLicenseMap["startDate"] = textGet("startDate");
			bizLicenseMap["endDate"] = textGet("endDate");
			bizLicenseMap["bizScope"] = textGet("bizScope");
			shopInfoMap["bizLicense"] = bizLicenseMap;
			//营业执照图片
			var bizLicensePicMap = {};
			bizLicensePicMap["imageUuid"] = textGet("bizLicenseUuid");
			bizLicensePicMap["imageUsage"] = textGet("bizLicenseUsage");
			bizLicensePicMap["imagePath"] = textGet("bizLicensePath");
			shopInfoMap["bizLicensePic"] = bizLicensePicMap;
			//
			return shopInfoMap;
		}
		
		function addMerchant(shopInfoMap){
			var hintBox = Layer.progress("正在保存数据...");

			var ajax = Ajax.post("/merch/add/do");
			ajax.data(shopInfoMap);
			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					addDialog.dialog("close");
					
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
		
		// 上传身份证反面文件
		function leftIdCertFileToUpload(){
			var uuid = $id("leftImageUuid").val();
			if(isNoB(uuid)){
				var formData = {
					usage : "image.photo"
				};
			} else {
				var formData ={
					update : true,
					uuid : uuid
				};
			}
			sendFileUpload("leftIdCertFile", {
				url : getAppUrl("/file/upload"),
				dataType : "json",
				// 自定义数据
				formData : formData,
				done : function(e, result) {
					var resultInfo = result.result;
					//单独提取文件信息列表
					var fileInfoList = getFileInfoList(resultInfo);
					if(resultInfo.type == "info"){
						$id("leftImageUuid").val(fileInfoList[0].fileUuid);
						$id("leftImageUsage").val(fileInfoList[0].fileUsage);
						$id("leftImagePath").val(fileInfoList[0].fileRelPath);
						$id("leftImageBrowseUrl").val(fileInfoList[0].fileBrowseUrl);
						$("#leftIdCertImg").attr("src",fileInfoList[0].fileBrowseUrl+"?"+new Date().getTime());
						Layer.msgSuccess("上传成功！");
					} else {
						Layer.msgWarning("上传失败！");
					}
				},
				fail : function(e, data) {
					console.log(data);
					Layer.msgWarning("上传失败！");
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
		
		// 上传身份证反面文件
		function rightIdCertFileToUpload(){
			var uuid = $id("rightImageUuid").val();
			if(isNoB(uuid)){
				var formData = {
					usage : "image.photo"
				};
			} else {
				var formData ={
					update : true,
					uuid : uuid
				};
			}
			sendFileUpload("rightIdCertFile", {
				url : getAppUrl("/file/upload"),
				dataType : "json",
				// 自定义数据
				formData : formData,
				done : function(e, result) {
					var resultInfo = result.result;
					//单独提取文件信息列表
					var fileInfoList = getFileInfoList(resultInfo);
					if(resultInfo.type == "info"){
						$id("rightImageUuid").val(fileInfoList[0].fileUuid);
						$id("rightImageUsage").val(fileInfoList[0].fileUsage);
						$id("rightImagePath").val(fileInfoList[0].fileRelPath);
						$id("rightImageBrowseUrl").val(fileInfoList[0].fileBrowseUrl);
						$("#rightIdCertImg").attr("src",fileInfoList[0].fileBrowseUrl+"?"+new Date().getTime());
						Layer.msgSuccess("上传成功！");
					} else {
						Layer.msgWarning("上传失败！");
					}
				},
				fail : function(e, data) {
					console.log(data);
					Layer.msgWarning("上传失败！");
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
		
		// 上传身份证反面文件
		function showLeftIdCertFileToUpload(){
			var uuid = $id("showLeftImageUuid").val();
			if(isNoB(uuid)){
				var formData = {
					usage : "image.photo"
				};
			} else {
				var formData ={
					update : true,
					uuid : uuid
				};
			}
			sendFileUpload("showLeftIdCertFile", {
				url : getAppUrl("/file/upload"),
				dataType : "json",
				// 自定义数据
				formData : formData,
				done : function(e, result) {
					var resultInfo = result.result;
					//单独提取文件信息列表
					var fileInfoList = getFileInfoList(resultInfo);
					if(resultInfo.type == "info"){
						$id("showLeftImageUuid").val(fileInfoList[0].fileUuid);
						$id("showLeftImageUsage").val(fileInfoList[0].fileUsage);
						$id("showLeftImagePath").val(fileInfoList[0].fileRelPath);
						$id("showLeftImageBrowseUrl").val(fileInfoList[0].fileBrowseUrl);
						$("#showLeftIdCertImg").attr("src",fileInfoList[0].fileBrowseUrl+"?"+new Date().getTime());
						Layer.msgSuccess("上传成功！");
					} else {
						Layer.msgWarning("上传失败！");
					}
				},
				fail : function(e, data) {
					console.log(data);
					Layer.msgWarning("上传失败！");
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
		
		// 上传身份证反面文件
		function showRightIdCertFileToUpload(){
			var uuid = $id("showRightImageUuid").val();
			if(isNoB(uuid)){
				var formData = {
					usage : "image.photo"
				};
			} else {
				var formData ={
					update : true,
					uuid : uuid
				};
			}
			sendFileUpload("showRightIdCertFile", {
				url : getAppUrl("/file/upload"),
				dataType : "json",
				// 自定义数据
				formData : formData,
				done : function(e, result) {
					var resultInfo = result.result;
					//单独提取文件信息列表
					var fileInfoList = getFileInfoList(resultInfo);
					if(resultInfo.type == "info"){
						$id("showRightImageUuid").val(fileInfoList[0].fileUuid);
						$id("showRightImageUsage").val(fileInfoList[0].fileUsage);
						$id("showRightImagePath").val(fileInfoList[0].fileRelPath);
						$id("showRightImageBrowseUrl").val(fileInfoList[0].fileBrowseUrl);
						$("#showRightIdCertImg").attr("src",fileInfoList[0].fileBrowseUrl+"?"+new Date().getTime());
						Layer.msgSuccess("上传成功！");
					} else {
						Layer.msgWarning("上传失败！");
					}
				},
				fail : function(e, data) {
					console.log(data);
					Layer.msgWarning("上传失败！");
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
		
		// 上传营业执照图片文件
		function bizLicenseFileToUpload(){
			var uuid = $id("bizLicenseUuid").val();
			if(isNoB(uuid)){
				var formData = {
					usage : "image.license"
				};
			} else {
				var formData ={
					update : true,
					uuid : uuid
				};
			}
			sendFileUpload("bizLicenseFile", {
				url : getAppUrl("/file/upload"),
				dataType : "json",
				// 自定义数据
				formData : formData,
				done : function(e, result) {
					var resultInfo = result.result;
					//单独提取文件信息列表
					var fileInfoList = getFileInfoList(resultInfo);
					if(resultInfo.type == "info"){
						$id("bizLicenseUuid").val(fileInfoList[0].fileUuid);
						$id("bizLicenseUsage").val(fileInfoList[0].fileUsage);
						$id("bizLicensePath").val(fileInfoList[0].fileRelPath);
						$id("bizLicenseBrowseUrl").val(fileInfoList[0].fileBrowseUrl);
						$("#bizLicenseImg").attr("src",fileInfoList[0].fileBrowseUrl+"?"+new Date().getTime());
						Layer.msgSuccess("上传成功！");
						bizLicenseFormProxy.validate("bizLicenseImg");
					} else {
						Layer.msgWarning("上传失败！");
					}
				},
				fail : function(e, data) {
					console.log(data);
					Layer.msgWarning("上传失败！");
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
		
		// 上传添加营业执照图片文件
		function addBizLicenseFileToUpload(){
			var uuid = $id("addBizLicenseUuid").val();
			if(isNoB(uuid)){
				var formData = {
					usage : "image.license"
				};
			} else {
				var formData ={
					update : true,
					uuid : uuid
				};
			}
			sendFileUpload("addBizLicenseFile", {
				url : getAppUrl("/file/upload"),
				dataType : "json",
				// 自定义数据
				formData : formData,
				done : function(e, result) {
					var resultInfo = result.result;
					//单独提取文件信息列表
					var fileInfoList = getFileInfoList(resultInfo);
					if(resultInfo.type == "info"){
						$id("addBizLicenseUuid").val(fileInfoList[0].fileUuid);
						$id("addBizLicenseUsage").val(fileInfoList[0].fileUsage);
						$id("addBizLicensePath").val(fileInfoList[0].fileRelPath);
						$id("addBizLicenseBrowseUrl").val(fileInfoList[0].fileBrowseUrl);
						$("#addBizLicenseImg").attr("src",fileInfoList[0].fileBrowseUrl+"?"+new Date().getTime());
						Layer.msgSuccess("上传成功！");
						bizLicenseFormProxy.validate("addBizLicenseImg");
					} else {
						Layer.msgWarning("上传失败！");
					}
				},
				fail : function(e, data) {
					console.log(data);
					Layer.msgWarning("上传失败！");
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
		
		//检验手机号码是否唯一
		var phoneFlag = false;
		function checkPhoneNo(phoneNo) {
			phoneFlag = false;
			var id = $id("userId").val();
			var postData = {
					phoneNo : phoneNo	
			};
			var ajax = Ajax.post("/merch/exist/by/phoneNo");
			ajax.data(postData);
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data
					if(data && data.userFlag){
						//获取用户信息
						var user = data.user;
						if(data.merchFlag){
							if(user.id == id){
								phoneFlag = false;
							}else{
								phoneFlag = true;
							}
						}else{
							
							$id("userId").val(user.id);
							phoneFlag = false;
							merchObj.user = user;
							hideMiscTip("phoneNo");
							shopInfoProxy.setValue2("user.nickName",user.nickName);
							shopInfoProxy.setValue2("user.phoneNo",user.phoneNo);
							$id("nickName").attr("disabled",true);
						}
					}else{
						phoneFlag = false;
						$id("nickName").attr("disabled",false);
					}
				}else{
					phoneFlag = true;
					$id("nickName").attr("disabled",false);
					Layer.warning(result.message);
				}
			});
			ajax.fail(function() {
				phoneFlag = false;
			});
			ajax.go();
		}
		
		var emailFlag = false;
		//邮箱是否可用
		function validateEmail(email){
			var id = $id("userId").val();
			var addFlag = (id == null || id == "");
			var updateFlag = (id != null && id != "" && merchObj && merchObj.user && email != merchObj.user.email);
			if(addFlag || updateFlag){
				var ajax = Ajax.post("/user/exist/by/email");
				ajax.data(email);
				ajax.sync();
				ajax.done(function(result, jqXhr) {
					emailFlag = result.data;
				});
				ajax.go();
			}
		}
		
		function goAddAccount(){
			clearAddUserAccountDialog();
			loadAddBankProvinceCode();
			addUserAccountDialog.dialog("open");
		}
		
		function goAddBizLicense(){
			clearAddBizLicenseDialog();
			addBizLicenseDialog.dialog("open");
		}
		
		function clearAddBizLicenseDialog(){
			$id("addRegNo").val("");
			$id("addCompanyName").val("");
			var src = getResUrl("/image-app/img-none.jpg");
			$id("addBizLicenseImg").attr("src", src);
			$id("addCompanyType").val("");
			$id("addCompanyAddr").val("");
			$id("addLegalMan").val("");
			$id("addRegCapital").val("");
			$id("addEstabDate").val("");
			$id("addStartDate").val("");
			$id("addEndDate").val("");
			$id("addBizScope").val("");
		}
		
		
		function clearAddUserAccountDialog(){
			//
			loadAddBanks();
			//
			$id("addAcctNo").val("");
			$id("addAcctName").val("");
			$id("addAcctPhoneNo").val("");
			$id("addVfCode").val("");
			$id("addBankFullName").val("");
			$id("addRelatedBankNo").val("");
			loadSelectData("addProvinceCode", "");
			$id("addBankInfo").css("display", "");
		}
		
		function clearShowAccountDiv(){
			$id("accountId").val("");
			$id("bankCode").val("");
			$id("bankName").val("");
			$id("acctNo").val("");
			$id("acctName").val("");
			$id("acctPhoneNo").val("");
			$id("vfCode").val("");
			$id("bankFullName").val("");
			$id("relatedBankNo").val("");
			loadSelectData("provinceCode", "");
		}
		
		function loadAddBanks(){
			var ajax = Ajax.post("/comn/bank/selectList/get");
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("addBankCode", result.data);
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		//启用禁用商户
		function disableMerchant(obj){
			var title="";
			if(obj.disabled){
				title = "启用";
			}else{
				title = "禁用";
			}
			var layer = Layer.confirm("确定要"+title+"该商户吗？", function() {
				var postData = {
						id : obj.id,
						disabled : obj.disabled,
						memo : obj.memo
				};
			    var loaderLayer = Layer.loading("正在提交数据...");
				var ajax = Ajax.post("/merch/disalbed/update/do");
				ajax.data(postData);
			    ajax.done(function(result, jqXhr){
			    	if(result.type== "info"){
						jqGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid"); 
						Layer.msgSuccess(result.message);
					}else{
						Layer.msgWarning(result.message);
					}
			  });
			   ajax.always(function() {
					//隐藏等待提示框
					loaderLayer.hide();
				});
				ajax.go();
				layer.hide();
			});
		}
		
		//继续
		//渲染
		function renderDetail(data,fromId,toId) {
			var tplHtml = $id(fromId).html();
			var theTpl = laytpl(tplHtml);
			var htmlStr = theTpl.render(data);
			$id(toId).html(htmlStr);
			var size = getImageSizeDef("image.logo");
			$("img").attr("width",size.width);
			$("img").attr("height",size.height);
		}
		
		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			console.log("mainWidth:" + mainWidth + ", " + "mainHeight:"
					+ mainHeight);
			//
			var gridCtrlId = "theGridCtrl";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv",
					jqGridBox).height();
			var pagerHeight = $id("theGridPager").height();
			jqGridCtrl.setGridWidth(mainWidth - 1);
			jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight
					- 3 - 60);
		}
		
		function loadBanks(){
			var ajax = Ajax.post("/comn/bank/selectList/get");
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("bankCode", result.data);
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		//--------------------------------- 设置结算资金账户 -----------------------------------------
		
		//初始化查看页面
		function initShowMerchantSettleDialog() {
			merchantSettleDialog = $("#merchantSettleDialog").dialog({
				autoOpen : false,
				height : Math.min(650, $(window).height()),
				width : Math.min(750, $(window).width()),
				modal : true,
				title : '设置结算资金账户',
				buttons : {
					"确定" : function(){
						merchantSettleDialog.dialog("close");
		            },
					"关闭" : function() {
						merchantSettleDialog.dialog("close");
					}
				},
				close : function() {
				}
			});
		}
		
		function getCheckedAccount(){
			var accountId = null;
			$("input:radio[name='account']").each(function(){
				var isChecked = $(this).prop("checked");
				if(isChecked){
					accountId = $(this).val();
					return false;
				}
			});
			return accountId;
		}
		
		//弹出设置结算资金账户
		function openMerchantSettleDialog(id) {
		    var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/userAccount/settleWay/get/do");
			ajax.params({
				userId : id
			});
			ajax.sync();
		    ajax.done(function(result, jqXhr){
		    	if(result.type== "info"){
					jqGridCtrl.jqGrid("setGridParam", {page:1}).trigger("reloadGrid"); 
					var settleWayList = result.data;
					var theTpl = laytpl( $id("settleWayTpl").html());
					var htmlStr = theTpl.render(settleWayList);
					$id("showSettleWayDialog").html(htmlStr);
				}else{
					Layer.msgWarning(result.message);
				}
		    });
		    ajax.always(function() {
				//隐藏等待提示框
				loaderLayer.hide();
			});
			ajax.go();
			merchantSettleDialog.dialog("open");
		}
		
		//加载资金账户列表数据
		function loadUserAccountData(){
			var postData = {
			};
			//加载资金账户
			accountGridCtrl= $("#userAccountGrid").jqGrid({
			      url : getAppUrl("/merch/userAccount/list/get"),  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      postData:{filterStr : JSON.encode(postData,true)},
			      height : "100%",
				  width : "100%",
			      colNames : ["id", "账户类型", "开户人","开户银行","账户号","操作"],  
			      colModel : [{name:"id", index:"id", width:150,align : 'center', hidden : true},
			                  {name:"typeFlag", index:"typeFlag",width:110,align : 'center',formatter : function(cellValue){
			                	  return cellValue==0?'私人账户':'对公账户';
			                  }},
			                  {name:"acctName", index:"acctName",width:130,align : 'center',},
			                  {name:"bankName", index:"bankName",width:140,align : 'center'},
			                  {name:"acctNo", index:"acctNo",width:230,align : 'center'},
			                  {name:"action", index:"action", width : 100, align : "center"} 			
							],  
				loadtext: "Loading....",
				multiselect : false,// 定义是否可以多选
				multikey:'ctrlKey',
				//pager : "#userAccountPager",
				loadComplete : function(gridData){
					accountGridHelper.cacheData(gridData);
					var callback = getCallbackAfterGridLoaded();
					if(isFunction(callback)){
						callback();
					}
				},
				gridComplete : function(){
					var ids = accountGridCtrl.jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						id = ParseInt(id);
						accountGridCtrl.jqGrid('setRowData', id, {
							action : "<input type='radio' name='account' value='"+id+"'/>"
						});
					}
				}
			});
		}
		
		function toBindAccount(userId, code){
			accountGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode({userId:userId},true)},page:1}).trigger("reloadGrid");
			settleWayCode = code;
			selectUserAccountDialog.dialog("open");
			// 页面自适应
			adjustCtrlsSize();
		}
		
		function toUpdateAccount(code){
			$id(code + "btnUpdate").css("display", "none");
			$id(code + "btnSave").css("display", "");
			$id(code).css("display", "none");
			$id(code + "update").css("display", "");
		}
		
		function toSaveAccount(userId, id, code){
			$id(code + "btnUpdate").css("display", "");
			$id(code + "btnSave").css("display", "none");
			$id(code).css("display", "");
			$id(code + "update").css("display", "none");
			var settleX = $id(code + "settleX").val();
			if(!settleX && !isNum(settleX)){
				Layer.msgWarning("请正确填写结算时间");
				return;
			}
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/merch/settle/acct/update/do");
			ajax.data({
				id : id,
				settleX : settleX
			});
			ajax.sync();
		    ajax.done(function(result, jqXhr){
		    	if(result.type== "info"){
		    		openMerchantSettleDialog(userId);
				}else{
					
				}
		    });
		    ajax.always(function() {
				//隐藏等待提示框
				loaderLayer.hide();
			});
			ajax.go();
		}
		
		
		function toUnBindAccount(userId, code){
			var theLayer = Layer.confirm('确定要清除该结算账户吗？', function() {
				var postData = {
					merchantId : userId,
					settleWayCode : code
				};
				var ajax = Ajax.post("/merch/settle/acct/unbind/do");
				ajax.data(postData);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						theLayer.hide();
			    		openMerchantSettleDialog(userId);
						Layer.msgSuccess(result.message);
					} else {
						theLayer.hide();
						Layer.msgWarning(result.message);
					}
				});
				ajax.go();
			});
		}
		
		//初始化选择资金账户Dialog
		function initSelectUserAccountDialog(){
			selectUserAccountDialog = $( "#selectUserAccountDialog" ).dialog({
		        autoOpen: false,
		        width : Math.min(760, $window.width()),
		        height : Math.min(300, $window.height()),
		        modal: true,
		        title : '选择资金账户',
		        buttons: {
		            "确定": function(){
		            	var accountId = getCheckedAccount();
		            	goMerchantSettleAcct(accountId, settleWayCode);
		            	selectUserAccountDialog.dialog("close");
		            },
		            "关闭": function() {
		            	selectUserAccountDialog.dialog( "close" );
		          }
		        },
		        close: function() {
		        	selectUserAccountDialog.dialog( "close" );
		        }
		      });
		}
		
		function goMerchantSettleAcct(id, code){
			if(id == null){
				Layer.msgWarning("请选择账户");
			}else{
				var account = accountGridHelper.getRowData(id);
				var loaderLayer = Layer.loading("正在提交数据...");
				var ajax = Ajax.post("/merch/settle/acct/bind/do");
				ajax.data({
					accountId : id,
					merchantId : account.userId,
					bankCode : account.bankCode,
					bankName : account.bankName,
					acctNo : account.acctNo,
					acctName : account.acctName,
					provinceCode : account.provinceCode,
					bankFullName : account.bankFullName,
					relatedBankNo : account.relatedBankNo,
					typeFlag : account.typeFlag,
					settleWayCode : code
				});
				ajax.sync();
			    ajax.done(function(result, jqXhr){
			    	if(result.type== "info"){
			    		var merchantSettleAcct = result.data;
			    		openMerchantSettleDialog(account.userId);
			    		Layer.msgSuccess(result.message)
					}else{
						Layer.msgWarning(result.message);
					}
			    });
			    ajax.always(function() {
					//隐藏等待提示框
					loaderLayer.hide();
				});
				ajax.go();
			}
		}
	</script>
</body>
<script type="text/html" id="userAccountTpl" title="店铺等级信息模版">
	{{# for(var i = 0, len = d.length; i < len; i++){ }}
		<tr id="tr{{ i }}">
			<td>
                <label id="showshopGradeUpperPoint{{ i }}" class="normal label">{{ d[i].typeFlag == 0 ? "私人账户":"对公账户" }}</label>
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
<script type="text/html" id="settleWayTpl" title="结算资金账户模版">
	{{# for(var i = 0, len = d.length; i < len; i++){ }}
	{{# var merchantSettleAcct = d[i].merchantSettleAcct }}
	<div class="simple block" data-bankCode="weixin">
		<div class="header">
			<label>{{ d[i].name }}账户</label>
			<div class="normal group" style="margin-left:40px;">
				<button id="btnSelect" class="normal button" onclick="toBindAccount('{{ d[i].userId }}', '{{ d[i].code }}');">
					绑定账户	
				</button>
				<span class="normal spacer"></span>
				<button id="btnClear" class="normal button" onclick="toUnBindAccount('{{ d[i].userId }}', '{{ d[i].code }}');" style="display:{{ merchantSettleAcct != null ? '':'none'}}">
						清除
				</button>
				<span class="normal spacer"></span>
				<button id="{{ d[i].code }}btnUpdate" class="normal button" onclick="toUpdateAccount('{{ d[i].code }}');" style="display:{{ merchantSettleAcct != null ? '':'none'}}">
						修改周期
				</button>
				{{# if(merchantSettleAcct != null){ }}
				<button id="{{ d[i].code }}btnSave" class="normal button" onclick="toSaveAccount('{{ d[i].userId }}', '{{ merchantSettleAcct.id }}', '{{ d[i].code }}');" style="display: none">
						保存周期
				</button>
				{{# } }}
			</div>
		</div>
		
		<div class="body" style="padding-top:4px;">
		{{# if(merchantSettleAcct != null){ }}
			<table class="cleanTbl" width="300">
				<tbody class="body">
					<tr class="body row">
						<td width="100">银行名称：</td>
						<td width="200">{{ merchantSettleAcct.bankName }}</td>
					</tr>
					<tr class="body row">
						<td>银行账号：</td>
						<td>{{ merchantSettleAcct.acctNo }}</td>
					</tr>
					<tr class="body row">
						<td>开户人：</td>
						<td>{{ merchantSettleAcct.acctName }}</td>
					</tr>
					<tr class="body row" id="{{ d[i].code }}">
						<td>结算周期：</td>
						<td>{{ merchantSettleAcct.settleX }} 天</td>
					</tr>
					<tr class="body row" id="{{ d[i].code }}update" style="display:none">
						<td>结算周期：</td>
						<td><input class="field value two wide" type="text" id="{{ d[i].code }}settleX" value="{{ merchantSettleAcct.settleX }}" style="width: 50px;"/> 天</td>
					</tr>
				</tbody>
			</table>
		{{# }else{ }}
			暂无绑定数据！
		{{# } }}
		</div>
	</div>
	{{# } }}
</script>
</html>