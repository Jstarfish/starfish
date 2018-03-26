<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<style type="text/css">
.center {
	margin:0 auto;
}
.text-title {
	padding-left: 2px;
	padding-top: 6px;
	height: 40px;
	width: 120px;
	background-image: url(<%=resBaseUrl%>/image/merchant/redback.png);
}
.main {
	height: auto;
	background-image: url(<%=resBaseUrl%>/image/merchant/u2.png);
}
.text-white {
    color: #ffffff;
    font-size: 20px;
    font-style: normal;
    font-weight: 400;
    text-align: center;
}
.text-black {
	color: #333333;
    font-size: 14px;
    font-style: normal;
    font-weight: 700;
}
.text-cyan {
	color: #006600;
}
.text-red {
	 color: #ff0000;
}
.nav .text-white {
    color: #ffffff;
    font-size: 14px;
    font-style: normal;
    font-weight: 700;
    text-align: center;
}
.nav .text-black {
	color: #333333;
    font-size: 14px;
    font-style: normal;
    font-weight: 700;
    text-align: center;
}
.line-red {
	height: 2px;
	width: 100%;
	background-image: url(<%=resBaseUrl%>/image/merchant/u111_line.png);
}
.info {
	padding-left: 5px;
	vertical-align:middle;
    height: 35px;
    line-height: 35px;
    height: 35px;
    width: 245px;
	color: #999999;
    font-size: 12px;
    font-weight: 400;
    text-align: left;
    word-wrap: break-word;
    background-image: url(<%=resBaseUrl%>/image/merchant/u19.png);
}
.nav {
	height: 40px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u121.png);
}
.nav .first {
	float: left;
	height: 38px;
    width: 367px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u130.png);
}
.nav .first .left{
	float: inherit;
	height: 38px;
    width: 135px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u134.png);
}

.nav .first .center{
	float: inherit;
    width: 98px;
    height: 38px;
    line-height: 38px;
}
.nav .second {
	float: left;
	height: 38px;
    width: 337px;
}
.nav .second .left{
	float: inherit;
	height: 38px;
    width: 50px;
}
.nav .second .center{
	float: inherit;
    width: 150px;
    height: 38px;
    line-height: 38px;
}
.nav .second .right{
	float: inherit;
    width: 130px;
    color: #ffffff;
    font-size: 72px;
    font-style: normal;
    font-weight: 400;
    height: 38px;
    line-height: 38px;
    text-align: right;
}
.nav .third {
	float: left;
	height: 38px;
    width: 292px;
}
.nav .third .left{
	float: inherit;
	height: 38px;
    width: 50px;
}
.nav .third .center{
	float: inherit;
    width: 150px;
    height: 38px;
    line-height: 38px;
}
.password-level1 {
	width: 53px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u31.png);
}
.password-level2 {
	width: 106px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u31.png);
}
.password-level3 {
	width: 159px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u31.png);
}
.shop-info {
	height: 88px;
	width: 555px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u183.png);
	float: left;
	font-size: 12px;
    font-style: normal;
    line-height: 30px;
}
.next {
	height: 35px;
    width: 215px;
    cursor: pointer;
	background-image: url(<%=resBaseUrl%>/image/u82.png);
}
.result {
	height: 260px;
    width: 640px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u229.png);
}
.result .title {
	font-style: normal;
    font-weight: 700;
    text-align: left;
    padding-left: 5px;
	height: 55px;
	line-height: 55px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u231.png);
}
.result .content {
	height: 150px;
	padding-top: 30px;
    text-align: left;
    padding-left: 20px;
}
.result .toShop {
	cursor: pointer;
	width: 110px;
	height: 30px;
	line-height: 30px;
	background-image: url(<%=resBaseUrl%>/image/merchant/u241.png);
}


/**wang**/
.main {background: none; border: 1px solid #aaa; padding-bottom:20px;}
.text-title {width: auto; padding-left:50px; text-align: left; background: #f3f3f3; border-top:2px solid #e4393c; border-left:1px solid #aaa; border-right:1px solid #aaa; color: #333; line-height: 62px; height: 62px; padding-top:0;}
.form .field-special.row {height: auto; overflow:hidden;}
.identity {overflow: hidden; border:1px solid #aaa; padding-top：2px; padding-bottom:2px; display: inline-block;}
.identity li {float: left; width: 178px; padding:10px; overflow: hidden;}
.identity li.border-left {border-left:1px solid #aaa;}
.identity li img {width: 159px; height: 90px;}
.identity li span {display: block; text-align: center;}

.field-img {margin-left:280px;}
.img {border: 1px solid #aaa; padding:25px; width: 300px;}
.img img {width: 220px; height: 127px;}
.form .field.row .tip {font-size: 12px; color: #999;}

input.hidden.file, input[type="file"].hidden {overflow: hidden;}
</style>

<title>商户入驻申请</title>
</head>

<body class="center" style="width: 996px;" >
<div style="padding-top: 20px; padding-bottom:20px;">
	<div class="filter section" >
		<div>
			 <img id="scopeLogoImg" src="<%=resBaseUrl%>/image/logo.png" width="167px" height="52px"/>
		</div>
		<div class="group right aligned" style="float: right;margin-bottom: 5px;">
			<span style="float: right;font-weight:normal;">[<a style="display: inline-block;text-decoration:none;font-size: 12px;" href="javascript:;" id="goBack">&nbsp;返回&nbsp;</a>]</span>
		</div>
	</div>
</div>
<div>
	<div class="text-title text-white">商户入驻</div>
    	<div class="main">
    		<span class="hr divider">&nbsp;</span>
    		<div class="center" style="width: 996px;margin-top: 10px;">
    		<div class="form" >
     		<div id="merchant-info" style="margin: 0 76px">
     			<div style="font-size: 18px;color: #666">商户信息</div>
     			<div class="field row" style="margin: 10px 0;">
					<label class="field label required three half wide" >商户名：</label>
					<input class="field value two wide" id="shopNickName" style="height: 32px;width: 300px;line-height: 20px;padding-left: 10px;" placeholder="请输入您的真实姓名"/>
					<input type="hidden" id="merchantId" />
				</div>
     			<div class="field row" style="margin: 10px 0;">
					<label class="field label required three half wide" >真实姓名：</label>
					<input class="field value two wide" id="realName" style="height: 32px;width: 300px;line-height: 20px;padding-left: 10px;" placeholder="请输入您的真实姓名"/>
					<input type="hidden" id="merchantId" />
				</div>
				<div class="field row" style="margin: 10px 0;">
					<label class="field label three half wide">身份证号：</label>
					<input class="field value two wide" id="idCertNo" placeholder="请输入您的身份证号" style="height: 32px;width: 300px;line-height: 20px;padding-left: 10px;"/>
				</div>
				<div class="field row field-special" style="margin: 10px 0;">
					<label class="field label required three half wide">身份证正反面：</label>
					<ul class="identity">
						<li>
							<span id="idCertLeft">身份证正面</span>
							<img src="<%=resBaseUrl%>/image-app/img-none.jpg" id="leftIdCertImg" alt="" />
							<input type="hidden" id="leftImageUuid" />
							<input type="hidden" id="leftImageUsage" />
							<input type="hidden" id="leftImagePath" />
							<input type="hidden" id="leftImageBrowseUrl" />
							<div id="leftIdCertDiv" style="display: none;">
								<input class="hidden file" type="file" id="leftIdCertFile" />
								<button class="normal button" id="leftIdCertFileToUpload">上传</button>
							</div>
						</li>
						<li class="border-left">
							<span id="idCertRight">身份证反面</span>
							<img src="<%=resBaseUrl%>/image-app/img-none.jpg" id="rightIdCertImg" alt="" />
							<input type="hidden" id="rightImageUuid" />
							<input type="hidden" id="rightImageUsage" />
							<input type="hidden" id="rightImagePath" />
							<input type="hidden" id="rightImageBrowseUrl" />
							<div id="rightIdCertDiv" style="display: none;">
								<input class="hidden file" type="file" id="rightIdCertFile" />
								<button class="normal button" id="rightIdCertFileToUpload">上传</button>
							</div>
						</li>
					</ul>
				</div>
				<!-- 资金账户 -->
				<fieldset style="margin-left:185px;">
					<legend>
						<label class="field inline required label three half wide">资金账户：</label>
						<input type="button" class="normal one half wide button" value="选择资金账户"  id="selectAccountBtn"/>
						<input type="hidden" id="accountId" />
					</legend>
					<div id="account-content">
						<div class="field row">
							<label class="field label one half wide">银行名称</label>
							<input type="text" id="bankCode" disabled class="field value two wide" />
						</div>
						<div class="field row">
							<label class="field label one half wide">银行账户</label> 
							<input type="text" id="acctNo" disabled class="field value two wide" />
						</div>
						<div class="field row">
							<label class="field label one half wide">开户名</label> 
							<input type="text" id="acctName" disabled class="field value two wide" />
						</div>
						<div class="field row">
							<label class="field label one half wide">预留号码</label> 
							<input type="text" id="acctPhoneNo" disabled class="field value two wide" />
						</div>
						<div id="vfCodeDiv" class="field row" style="display: none;">
							<label class="field label one half wide">打款验证码</label> 
							<input type="text" id="vfCode" disabled class="field value two wide" maxlength="30"/>
						</div>
					</div>
				</fieldset>
			</div>
			<hr class="divider" style="margin: 20px 0;"></hr>
			<div id="shop-info" style="margin: 10px 0;margin: 0 76px">
				<div style="font-size: 18px;color: #666">店铺信息</div>	
				<div class="field row" style="margin: 10px 0;">
					<label class="field label required three half wide">店铺名称：</label>
					<input class="field value two wide" id="shopName" style="height: 32px;width: 300px;line-height: 20px;padding-left: 10px;" placeholder="请输入店铺名称" maxlength="20"/>
				</div>
				<div class="field row field-special" style="margin: 10px 0;">
					<label class="field label required three half wide">店面照片：</label>
					<div class="field-img">
						<div class="img">
							<img src="<%=resBaseUrl%>/image-app/img-none.jpg" id="shopLogo" alt="" />
							<input type="hidden" id="shopLogoUuid" />
							<input type="hidden" id="shopLogoUsage" />
							<input type="hidden" id="shopLogoPath" />
							<input type="hidden" id="shopLogoBrowseUrl" />
							<input class="hidden file" type="file" id="shopLogoFile" />
							<button class="normal button" id="shopLogoFileToUpload">上传</button>
						</div>
						<span class="tip">仅支持JPG、GIF、PNG、JPEG、BMP格式，文件小于4M</span>
					</div>
				</div>
				<div class="field row" style="margin: 10px 0;">
					<label class="field label three half wide">员工人数：</label>
					<input class="field value two wide" id="staffCount" style="height: 32px;width: 300px;line-height: 20px;padding-left: 10px;" placeholder="请输入员工人数" maxlength="9"/>
				</div>
				<!--TODO 待解决：地区获取不到-->
				<div class="field row" style="margin: 10px 0;">
					<label class="field label required three half wide">所在地区：</label>
					<select class="field value" id="province"></select>
					<select class="field value" id="city"></select>
					<select class="field value" id="county"></select>
					<select class="field value" id="town" style="display: none;"></select>
				</div>
				<div class="field row" style="margin: 10px 0;">
					<label class="field label required three half wide">所在街道：</label>
					<input class="field value two wide" id="street" style="height: 32px;width: 300px;line-height: 20px;padding-left: 10px;" placeholder="请输入详细地址" maxlength="100"/>
				</div>
				<div class="field row" style="margin: 10px 0;">
					<label class="field label required three half wide">店铺联系人：</label>
					<input class="field value two wide" id="linkMan" style="height: 32px;width: 300px;line-height: 20px;padding-left: 10px;" placeholder="请输入店铺联系人" maxlength="50"/>
				</div>
				<div class="field row" style="margin: 10px 0;">
					<label class="field label required three half wide">联系电话：</label>
					<input class="field value two wide" id="phoneNo" style="height: 32px;width: 300px;line-height: 20px;padding-left: 10px;" placeholder="请输入联系电话" maxlength="11"/>
				</div>
				<div class="field row" style="margin: 10px 0; height: auto;">
					<label class="field label required three half wide">经营范围：</label>
					<textarea id="shopBizScope" class="field value three wide" style="height:95px;width: 300px;padding: 5px;"></textarea>
				</div>
				<div class="field row" style="margin: 10px 0;">
					<label class="field label required three half wide">店铺类型：</label>
					<div class="field group">
						<input id="personalShop" type="radio" checked="checked" name="shopType" value="0" />
						<label for="personalShop">个人</label>
						<input id="enterpriseShop" type="radio" name="shopType" value="1" />
						<label for="enterpriseShop">企业</label>
					</div>
				</div>
				
				<!-- 企业营业执照 -->
				<fieldset style="margin-left:185px; margin-top:10px;">
					<legend>
						<label id="lincenseLabel" class="field inline label three half wide">营业执照：</label>
						<input type="button" class="normal button" value="选择执照"  id="selectBizLicenseBtn"/>
						<input type="hidden" id="licenseId" />
					</legend>
					<div id="bizLicense-content">
						<div class="field row">
							<label class="field label one half wide">注册号：</label>
							<input class="field value two wide" id="regNo" disabled/>
						</div>
						<div class="field row">
							<label class="field label one half wide">名称：</label>
							<input class="field value two wide" id="companyName" disabled/>
						</div>
						<div class="field row field-special" style="margin: 10px 0;">
							<label class="field label one half wide">营业执照图片：</label>
							<div style="margin-left:120px;">
								<div class="img">
									<img src="<%=resBaseUrl%>/image-app/img-none.jpg" id="bizLicenseImg" alt="" />
								</div>
							</div>
						</div>
						<div class="field row">
							<label class="field label one half wide">类型：</label>
							<input class="field value two wide" id="companyType" disabled/>
						</div>
						<div class="field row">
							<label class="field label one half wide">住所：</label>
							<input class="field value two wide" id="companyAddr" disabled/>
						</div>
						<div class="field row">
							<label class="field label one half wide">法定代表人：</label>
							<input class="field value two wide" id="legalMan" disabled/>
						</div>
						<div class="field row">
							<label class="field label one half wide">注册资本：</label>
							<input class="field value two wide" id="regCapital" disabled/>
						</div>
						<div class="field row">
							<label class="field label one half wide">成立日期</label>
							<input class="field value" id="estabDate" disabled/>
						</div>
						<div class="field row">
							<label class="field label one half wide">营业期限</label>
							<div class="field group">
								<input class="field value" disabled id="startDate" />
								至
								<input class="field value" disabled id="endDate" />
							</div>
						</div>
						<div class="field row" style="margin: 10px 0;height: 95px;">
							<label class="field label one half wide">经营范围：</label>
							<textarea id="bizScope" disabled class="field value three wide" style="height:95px;width: 300px;padding: 5px;"></textarea>
						</div>
					</div>
				</fieldset>
				<div class="field row" style="margin: 10px 0;height: 95px;">
					<label class="field label three half wide">留言：</label>
					<textarea id="applyMsg" class="field value three wide" style="height:95px;width: 300px;padding: 5px;" placeholder="请输入留言"></textarea>30字以内
				</div>
				<div class="field row" style="margin: 10px 0;">
					<label class="field label three half wide">推荐人：</label>
					<input class="field value two wide" id="referrerName" style="height: 32px;width: 300px;line-height: 20px;padding-left: 10px;" placeholder="请输入推荐人姓名" maxlength="50"/>
				</div>
				<div class="field row" style="margin: 10px 0;">
					<label class="field label three half wide">推荐人手机：</label>
					<input class="field value two wide" id="referrerPhoneNo" style="height: 32px;width: 300px;line-height: 20px;padding-left: 10px;" placeholder="请输入推荐人手机号" maxlength="11"/>
				</div>
				<div class="field row" style="margin: 10px 0;">
					<div class="next text-white center" id="applyBtn" style="height: 35px;line-height: 35px;">提交申请</div>
			     </div>
			</div>
			</div>
    		</div>
    	</div>	
</div>

<!--添加资金账户dialog -->
<div id="addUserAccountDialog" class="form" style="display: none;height: 600px;">
	<div class="ui-layout-center">
		<div class="field row">
			<label class="field label required one half wide">银行名称</label>
			<select id="addBankCode" class="field value two wide"></select>
		</div>
		<div class="field row">
			<label class="field label required one half wide">银行账户</label> 
			<input type="text" id="addAcctNo" class="field value two wide" placeholder="请输入银行账户" style="height: 32px;line-height: 20px;padding-left: 10px;"/>
		</div>
		<div class="field row">
			<label class="field label required one half wide">开户名</label> 
			<input type="text" id="addAcctName" class="field value two wide" placeholder="请输入开户人" style="height: 32px;line-height: 20px;padding-left: 10px;"/>
		</div>
		<div class="field row">
			<label class="field label one half wide">预留号码</label> 
			<input type="text" id="addAcctPhoneNo" class="field value two wide" placeholder="请输入预留电话" style="height: 32px;line-height: 20px;padding-left: 10px;"/>
		</div>
		<div id="addVfCodeDiv" class="field row">
			<label class="field label one half wide">打款验证码</label> 
			<input type="text" id="addVfCode" class="field value two wide" placeholder="请输入打款验证码" style="height: 32px;line-height: 20px;padding-left: 10px;" maxlength="30"/>
		</div>
	</div>
</div>

<!--选择资金账户dialog -->
<div id="selectUserAccountDialog" class="form" style="display: none;height: 600px;">
	<div class="ui-layout-center">
		<div class="field row">
			<button class="normal button" id="addAccountBtn">添加</button>
		</div>
		<br>
		<table id="userAccountGrid"></table>
		<div id="userAccountPager"></div>
	</div>
</div>

<!--添加企业营业执照dialog -->
<div id="addBizLicenseDialog" class="form" style="display: none;height: 600px;">
	<div class="ui-layout-center">
		<div class="field row">
			<label class="field label required one half wide">注册号：</label>
			<input class="field value two wide" id="addRegNo" placeholder="请输入注册号" style="height: 32px;line-height: 20px;padding-left: 10px;"/>
		</div>
		<div class="field row">
			<label class="field label required one half wide">名称：</label>
			<input class="field value two wide" id="addCompanyName" placeholder="请输入名称" style="height: 32px;line-height: 20px;padding-left: 10px;"/>
		</div>
		<div class="field row" style="height: auto;">
			<label class="field label required one half wide">营业执照图片：</label>
			<div style="display: inline-block;">
				<div class="img" style="padding: 3px; width: 240px; text-align: center; vertical-align: middle;">
					<img src="<%=resBaseUrl%>/image-app/img-none.jpg" id="addBizLicenseImg" style="vertical-align: middle;" />
				</div>
				<div style="margin-top:5px;">
					<input type="hidden" id="bizLicenseUuid" />
					<input type="hidden" id="bizLicenseUsage" />
					<input type="hidden" id="bizLicensePath" />
					<input type="hidden" id="bizLicenseBrowseUrl" />
					<input class="hidden file" type="file" id="bizLicenseFile" />
					<button class="normal button" id="bizLicenseFileToUpload">上传</button>
				</div>
				<span class="tip">仅支持JPG、GIF、PNG、JPEG、BMP格式，文件小于4M</span>
			</div>
		</div>
		<div class="field row">
			<label class="field label required one half wide">类型：</label>
			<input class="field value two wide" id="addCompanyType" placeholder="请输入类型" style="height: 32px;line-height: 20px;padding-left: 10px;"/>
		</div>
		<div class="field row">
			<label class="field label required one half wide">住所：</label>
			<input class="field value two wide" id="addCompanyAddr" placeholder="请输入住所" style="height: 32px;line-height: 20px;padding-left: 10px;"/>
		</div>
		<div class="field row">
			<label class="field label required one half wide">法定代表人 ：</label>
			<input class="field value two wide" id="addLegalMan" placeholder="请输入法定代表人" style="height: 32px;line-height: 20px;padding-left: 10px;"/>
		</div>
		<div class="field row">
			<label class="field label required one half wide">注册资本：</label>
			<input class="field value two wide" id="addRegCapital" placeholder="注册资本" style="height: 32px;line-height: 20px;padding-left: 10px;"/>
		</div>
		<div class="field row">
			<label class="field label required one half wide">成立日期</label>
			<input class="field value" id="addEstabDate" />
		</div>
		<div class="field row">
			<label class="field label required one half wide">营业期限</label>
			<div class="field group">
				<input class="field value" id="addStartDate" />
				至
				<input class="field value" id="addEndDate" />
			</div>
		</div>
		<div class="field row" style="margin: 10px 0;height: 95px;">
			<label class="field label one half wide">经营范围：</label>
			<textarea id="addBizScope" class="field value three wide" style="height:95px;padding: 5px;" placeholder="请输入经营范围"></textarea>1000字以内
		</div>
	</div>
</div>

<!--选择企业营业执照dialog -->
<div id="selectBizLicenseDialog" class="form" style="display: none;height: 600px;">
	<div class="ui-layout-center">
		<div class="field row">
			<button class="normal button" id="addBizLicenseBtn">添加</button>
		</div>
		<br>
		<table id="bizLicenseGrid"></table>
		<div id="bizLicensePager"></div>
	</div>
</div>
<!-- footer -->
<div style="height: 180px;text-align: center;">
	<div class="filter section center" >
		<div class="filter row center">
			 关于商城    | 联系我们    | 商家入驻    | 人才招聘    | 亿投车吧社区
		</div>
		<div class="filter row">
			Copyright© 亿投车吧  All Rights Reserved 版权所有
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
	//
	var isExistMerchantFlag = false;
	//
	var addUserAccountDialog = null;
	//
	var selectUserAccountDialog = null;
	//
	var selectBizLicenseDialog = null;
	//
	var addBizLicenseDialog = null;
	//
	var accountFormProxy = FormProxy.newOne();
	//
	var bizLicenseFormProxy = FormProxy.newOne();
	//
	var shopInfoFormProxy = FormProxy.newOne();
	//
    var accountGridHelper = JqGridHelper.newOne("");
    //
    var bizLicenseGridHelper = JqGridHelper.newOne("");
    //
    var accountGridCtrl = null;
    //
    var bizLicenseGridCtrl = null;
	
	//----------------------------------注册Form-----------------------------------------
	//
	shopInfoFormProxy.addField({
		id : "shopNickName",
		required : true,
		rules : ["maxLength[30]"],
		messageTargetId : "shopNickName"
	});
	//
	shopInfoFormProxy.addField({
		id : "realName",
		required : true,
		rules : ["maxLength[30]"],
		messageTargetId : "realName"
	});
	//
	shopInfoFormProxy.addField({
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
	shopInfoFormProxy.addField({
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
	//
	
	shopInfoFormProxy.addField({
		id : "shopName",
		required : true,
		rules : ["maxLength[30]", {
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
		id : "phoneNo",
		required : true,
		messageTargetId : "phoneNo"
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
		id : "selectAccountBtn",
		rules : [{
			rule : function(idOrName, type, rawValue, curData) {
				var accountId = $id("accountId").val();
				if(!accountId){
					return false;
				}
				return true;
			},
			message: "亲,您还未选择资金账户哦！"
		}],
		messageTargetId : "selectAccountBtn"
	});
	//
	shopInfoFormProxy.addField({
		id : "selectBizLicenseBtn",
		rules : [{
			rule : function(idOrName, type, rawValue, curData) {
				var typeFlag = getShopTypeFlag();
				if(typeFlag){
					var licenseId = $id("licenseId").val();
					if(!licenseId){
						return false;
					}
				}
				return true;
			},
			message: "亲,您还未选择营业执照哦！"
		}],
		messageTargetId : "selectBizLicenseBtn"
	});
	//-------------------------------------
	accountFormProxy.addField({
		id : "addBankCode",
		required : true,
		messageTargetId : "addBankCode"
	});
	accountFormProxy.addField({
		id : "addAcctNo",
		required : true,
		messageTargetId : "addAcctNo"
	});
	accountFormProxy.addField({
		id : "addAcctName",
		required : true,
		messageTargetId : "addAcctName"
	});
	accountFormProxy.addField({
		id : "addAcctPhoneNo",
		rules : ["isMobile"],
		messageTargetId : "addAcctPhoneNo"
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
		rules : ["isDigits","maxLength[18]"],
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
	
	$(function() {
		//校验商户是否存在
		isExistMerchant();
		//身份证正面
		initFileUpload("leftIdCertFile");
		//身份证反面
		initFileUpload("rightIdCertFile");
		//商铺照片
		initFileUpload("shopLogoFile");
		//组织机构照片
		initFileUpload("bizLicenseFile");
		//
		$id("addEstabDate").datepicker({
			  maxDate: new Date(),
		      changeMonth: true,
		      changeYear: true,
		      showYearOrder: "desc"
		});
		//初始化开始结束日期
		initDatePicker();
		//初始化登录信息
		ajaxUser();
		//
		loadBanks();
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
		
		//验证店铺名称
		//$id("shopName").blur(isExistShopName);
		
		//初始化Dialog
		initAddAccountDialog();
		initAddBizLicenseDialog();
		initSelectUserAccountDialog();
		initSelectBizLicenseDialog();
		
		//初始化资金账户
		loadUserAccountData();
		//初始化营业执照
		loadBizLicenseData();
		//
		$("input[name='shopType']").change(function(){
			//隐藏验证警告信息
			hideMiscTip("selectAccountBtn");
			hideMiscTip("selectBizLicenseBtn");
			//
			clearShowAccountDiv();
			var typeFlag = getShopTypeFlag();
			var postData = {
					typeFlag : typeFlag
			};
			accountGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(postData,true)},page:1}).trigger("reloadGrid");
			//1:对公账号 0：对私账号
			if(typeFlag){
				$id("vfCodeDiv").css("display", "block");
				$id("lincenseLabel").addClass("required");
			}else{
				$id("vfCodeDiv").css("display", "none");
				$id("lincenseLabel").removeClass("required");
			}
		});
		
		//选择资金账号
		$id("selectAccountBtn").click(goSelectAccount);
		//添加资金账号
		$id("addAccountBtn").click(goAddAccount);
		
		//选择营业执照
		$id("selectBizLicenseBtn").click(goSelectBizLicense);
		//添加营业执照
		$id("addBizLicenseBtn").click(goAddBizLicense);
		
		//绑定上传身份证正面
		$("#leftIdCertFileToUpload").click(leftIdCertFileToUpload);
		//绑定上传身份证反面
		$("#rightIdCertFileToUpload").click(rightIdCertFileToUpload);
		//绑定上传店铺照片
		$("#shopLogoFileToUpload").click(shopLogoFileToUpload);
		//绑定营业执照照片
		$("#bizLicenseFileToUpload").click(bizLicenseFileToUpload);
		
		//商户入驻
		$id("applyBtn").click(goSettledMerchant);
		
		//
		$("#province").empty().show();
		//返回
		$("#goBack").click(function() {
			setPageUrl(getAppUrl("/navgt/entry/index/jsp"));
		});
		
	});
	
	//---------------------------------业务处理-------------------------------------------------------------
	var isExistShopNameFlag = false;
	function isExistShopName(shopName){
		var ajax = Ajax.post("/merch/shop/exist/by/name");
		ajax.data({name : shopName});
		//同步验证
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			isExistShopNameFlag = result.data;
			/* if(isExistShopNameFlag){
				Toast.show("该名称已被占用！", 2000, "warning");
			} */
		});
		ajax.go();
	}
	
	function settledMerchant(shopInfoMap){
		var hintBox = Layer.progress("正在保存数据...");

		var ajax = Ajax.post("/merch/settled/apply/do");
		
		ajax.data(shopInfoMap);

		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				var theLayer = Layer.msgInfo("提交申请成功，请等待商城审核，审核结果（7个工作日内）以短信形式通知你！", function(layerIndex) {
				setPageUrl(getAppUrl("/navgt/entry/index/jsp"));
				}, 3000);
			} else {
				Layer.msgWarning(result.message);
			}
			
		});
		ajax.always(function() {
			hintBox.hide();
		});
		
		ajax.go();
	}
	
	function getShopInfoMap(){
		var shopInfoMap = {};
		if(isExistMerchantFlag){
			shopInfoMap["merchantId"] = intGet("merchantId");
			var merchantMap = {};
			merchantMap["name"] = textGet("shopNickName");
			shopInfoMap["merchant"] = merchantMap;
		}else{
			var merchantMap = {};
			merchantMap["name"] = textGet("shopNickName");
			merchantMap["realName"] = textGet("realName");
			merchantMap["idCertNo"] = textGet("idCertNo");
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
		//店铺信息
		var shop = {};
		shop["name"] = textGet("shopName");
		shop["logoUuid"] = textGet("shopLogoUuid");
		shop["logoUsage"] = textGet("shopLogoUsage");
		shop["logoPath"] = textGet("shopLogoPath");
		shop["staffCount"] = intGet("staffCount");
		//地区信息
		var regionId;
		if(isShowTown("town")){
			regionId = intGet("town");
		} else {
			regionId = intGet("county");
		}
		
		shop["regionId"] = regionId;
		shop["street"] = textGet("street");
		shop["linkMan"] = textGet("linkMan");
		shop["phoneNo"] = textGet("phoneNo");
		shop["bizScope"] = textGet("shopBizScope");
		var typeFlag = getShopTypeFlag();
		shop["entpFlag"] = typeFlag;
		shop["applyMsg"] = textGet("applyMsg");
		shop["referrerName"] = textGet("referrerName");
		shop["referrerPhoneNo"] = textGet("referrerPhoneNo");
		shopInfoMap["shop"] = shop;
		//资金账户
		shopInfoMap["accountId"] = intGet("accountId");
		//企业营业执照
		shopInfoMap["licenseId"] = intGet("licenseId");
		//
		return shopInfoMap;
	}
	
	function goSettledMerchant(){

        var result = shopInfoFormProxy.validateAll();
        alert(result)
		if(result){
			var shopInfoMap = getShopInfoMap();
			settledMerchant(shopInfoMap);
		}
	}
	
	//----------------------------营业执照-----------------------------------------
	
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
		alert(result);
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
	
	function goAddBizLicense(){
		clearAddBizLicenseDialog();
		//
		addBizLicenseDialog.dialog("open");
	}
	
	function goSelectBizLicense(){
		selectBizLicenseDialog.dialog("open");
		// 页面自适应
		adjustCtrlsSize();
	}
	
	//加载营业执照列表数据
	function loadBizLicenseData(){
		//加载资金账户
		bizLicenseGridCtrl= $("#bizLicenseGrid").jqGrid({
		      url : getAppUrl("/comn/bizLicense/list/get/by/userId"),  
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
	
	function setCheckedBizLicense(id){
		$("input:radio[name='bizLicense']").each(function(){
			var licenseId = $(this).val();
			if(licenseId == id){
				$(this).prop("checked", true);
				return;
			}
		});
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
	
	function showSelectLicenseInfo(id){
		if(id){
			$id("licenseId").val(id);
			var bizLicense = bizLicenseGridHelper.getRowData(id);
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
	
	//----------------------------资金账户-----------------------------------------
	function clearShowAccountDiv(){
		$id("accountId").val("");
		$id("bankCode").val("");
		$id("acctNo").val("");
		$id("acctName").val("");
		$id("acctPhoneNo").val("");
		$id("vfCode").val("");
	}
	
	function showSelectAccountInfo(id){
		if(id){
			$id("accountId").val(id);
			var account = accountGridHelper.getRowData(id);
			$id("bankCode").val(account.bankCode);
			$id("acctNo").val(account.acctNo);
			$id("acctName").val(account.acctName);
			$id("acctPhoneNo").val(account.phoneNo);
			$id("vfCode").val(account.vfCode);
		}
	}
	
	function setCheckedAccount(id){
		$("input:radio[name='account']").each(function(){
			var accountId = $(this).val();
			if(accountId == id){
				$(this).prop("checked", true);
				return;
			}
		});
	}
	
	function getCheckedAccount(){
		var accountId = null;
		$("input:radio[name='account']").each(function(){
			accountId = $(this).val();
			var isChecked = $(this).prop("checked");
			if(isChecked){
				return false;
			}
		});
		return accountId;
	}
	
	function saveUserAccount(accountMap){
		var hintBox = Layer.progress("正在保存数据...");

		var ajax = Ajax.post("/userAccount/add/do");
		
		ajax.data(accountMap);

		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
				var account = result.data;
				var accountId = account.id;
				addUserAccountDialog.dialog("close");
				//加载最新数据列表
				var typeFlag = getShopTypeFlag();
				var postData = {
						typeFlag : typeFlag
				};
				accountGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(postData,true)},page:1}).trigger("reloadGrid");
				//
				getCallbackAfterGridLoaded = function(){
					return function(){
						setCheckedAccount(accountId);
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
	
	function getUserAccount(){
		var accountMap = {};
		accountMap["bankCode"] = textGet("addBankCode");
		accountMap["bankName"] = $id("addBankCode").find("option:selected").text();
		accountMap["acctNo"] = textGet("addAcctNo");
		accountMap["acctName"] = textGet("addAcctName");
		accountMap["phoneNo"] = textGet("addAcctPhoneNo");
		accountMap["typeFlag"] = getShopTypeFlag();
		return accountMap;
	}
	
	function goSaveAccount(){
		var result = accountFormProxy.validateAll();
		if(result){
			var accountMap = getUserAccount();
			saveUserAccount(accountMap);
		}
	}
	
	function clearAddUserAccountDialog(){
		//
		loadBanks();
		//
		$id("addAcctNo").val("");
		$id("addAcctName").val("");
		$id("addAcctPhoneNo").val("");
		$id("addVfCode").val("");
		
	}
	
	function goAddAccount(){
		clearAddUserAccountDialog();
		var typeFlag = getShopTypeFlag();
		//1:对公账号 0：对私账号
		if(typeFlag){
			$id("addVfCodeDiv").css("display", "block");
		}else{
			$id("addVfCodeDiv").css("display", "none");
		}
		//
		addUserAccountDialog.dialog("open");
	}
	
	function goSelectAccount(){
		selectUserAccountDialog.dialog("open");
		// 页面自适应
		adjustCtrlsSize();
	}
	
	// 判断区是否显示
	function isShowTown(town){
 		return $id(town).val() != null;
	}
	
	function isExistMerchant(){
		var ajax = Ajax.post("/merch/exist/by/userId");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var merchant = result.data;
				var src = getResUrl("/image-app/img-none.jpg");
				if(merchant){
					$id("merchantId").val(merchant.id);
					isExistMerchantFlag = true;
					$id("shopNickName").prop("disabled", true);
					$id("shopNickName").val(merchant.name);
					$id("realName").prop("disabled", true);
					$id("realName").val(merchant.realName);
					$id("idCertNo").prop("disabled", true);
					$id("idCertNo").val(merchant.idCertNo);
					$id("leftIdCertDiv").css("display", "none");
					$id("rightIdCertDiv").css("display", "none");
					var merchantPics = merchant.merchantPics;
					if(merchantPics){
						for(var i=0; i<merchantPics.length; i++){
							var merchantPic = merchantPics[i];
							var code = merchantPic.code;
							if(code == "IDCert.front"){
								$id("leftIdCertImg").attr("src", merchantPic.fileBrowseUrl == null ? src : merchantPic.fileBrowseUrl);
								$id("leftImageUuid").val(merchantPic.imageUuid);
								$id("leftImageUsage").val(merchantPic.imageUsage);
								$id("leftImagePath").val(merchantPic.imagePath);
							}else if(code == "IDCert.back"){
								$id("rightIdCertImg").attr("src", merchantPic.fileBrowseUrl == null ? src : merchantPic.fileBrowseUrl);
								$id("rightImageUuid").val(merchantPic.imageUuid);
								$id("rightImageUsage").val(merchantPic.imageUsage);
								$id("rightImagePath").val(merchantPic.imagePath);
							}
						}
						
					}
				}else{
					isExistMerchantFlag = false;
					$id("merchantId").val("");
					$id("shopNickName").prop("disabled", false);
					$id("shopNickName").val("");
					$id("realName").prop("disabled", false);
					$id("realName").val("");
					$id("idCertNo").prop("disabled", false);
					$id("idCertNo").val("");
					$id("leftIdCertDiv").css("display", "block");
					$id("rightIdCertDiv").css("display", "block");
					$id("leftIdCertImg").attr("src", src);
					$id("rightIdCertImg").attr("src", src);
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	
	//------------------------调整控件大小-------------------------
	//调整控件大小
	function adjustCtrlsSize() {
		var selectAccountDialogWidth = $id("selectUserAccountDialog").find("div.ui-layout-center").width();
		var selectAccountDialogHeight = $id("selectUserAccountDialog").find("div.ui-layout-center").height();
		accountGridCtrl.setGridWidth(selectAccountDialogWidth - 1);
		//accountGridCtrl.setGridHeight(selectAccountDialogHeight - 3 - 46);
		var selectLicenseDialogWidth = $id("selectBizLicenseDialog").find("div.ui-layout-center").width();
		var selectLicenseDialogHeight = $id("selectBizLicenseDialog").find("div.ui-layout-center").height();
		bizLicenseGridCtrl.setGridWidth(selectLicenseDialogWidth - 1);
	}
	
	
	//--------------------------------初始化JqGrid-----------------------------------------------
	function getShopTypeFlag(){
		//1:对公账号 0：对私账号
		var typeFlag = radioGet("shopType");
		typeFlag = ParseInt(typeFlag);
		return typeFlag;
	}
	
	//加载资金账户列表数据
	function loadUserAccountData(){
		var postData = {
				typeFlag : getShopTypeFlag()
		};
		//加载资金账户
		accountGridCtrl= $("#userAccountGrid").jqGrid({
		      url : getAppUrl("/user/userAccount/list/get"),  
		      contentType : 'application/json',  
		      mtype : "post",  
		      datatype : 'json',
		      postData:{filterStr : JSON.encode(postData,true)},
		      height : "100%",
			  width : "100%",
		      colNames : ["id", "账户类型", "开户人","开户银行","银行账户","操作"],  
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
			//当从服务器返回响应时执行
			loadComplete : function(gridData){
				accountGridHelper.cacheData(gridData);
				var callback = getCallbackAfterGridLoaded();
				if(isFunction(callback)){
					callback();
				}
			},
			//当表格所有数据都加载完成而且其他的处理也都完成时触发此事件，排序，翻页同样也会触发此事件
			gridComplete : function(){
			    //小问题：user_account表的主键不是id，id自增没有初始值
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
	
	function getCallbackAfterGridLoaded(){}
	
	//--------------------------------初始化Dialog-----------------------------------------------
	//初始化选择资金账户Dialog
	function initSelectUserAccountDialog(){
		selectUserAccountDialog = $( "#selectUserAccountDialog" ).dialog({
	        autoOpen: false,
	        width : Math.min(900, $window.width()),
	        height : Math.min(300, $window.height()),
	        modal: true,
	        title : '选择资金账户',
	        buttons: {
	            "确定": function(){
	            	var accountId = getCheckedAccount();
	            	showSelectAccountInfo(accountId);
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
	
	//初始化添加资金账户Dialog
	function initAddAccountDialog(){
		addUserAccountDialog = $( "#addUserAccountDialog" ).dialog({
	        autoOpen: false,
	        width : Math.min(450, $window.width()),
	        height : Math.min(350, $window.height()),
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
	
	//初始化选择企业营业执照Dialog
	function initSelectBizLicenseDialog(){
		selectBizLicenseDialog = $( "#selectBizLicenseDialog" ).dialog({
	        autoOpen: false,
	        width : Math.min(900, $window.width()),
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
	
	//初始化添加企业营业执照Dialog
	function initAddBizLicenseDialog(){
		addBizLicenseDialog = $( "#addBizLicenseDialog" ).dialog({
	        autoOpen: false,
	        width : Math.min(750, $window.width()),
	        height : Math.min(600, $window.height()),
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
	
	//---------------------------------初始化数据-------------------------------------------------------------
	function loadBanks(){
		var ajax = Ajax.post("/comn/bank/selectList/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData("addBankCode", result.data);
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
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
	
	// 上传身份证正面文件
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
	
	// 上传店面照片文件
	function shopLogoFileToUpload(){
		var uuid = $id("shopLogoUuid").val();
		if(isNoB(uuid)){
			var formData = {
				usage : "image.logo",
				subUsage : "shop"
			};
		} else {
			var formData ={
				update : true,
				uuid : uuid
			};
		}
		sendFileUpload("shopLogoFile", {
			url : getAppUrl("/file/upload"),
			dataType : "json",
			// 自定义数据
			formData : formData,
			done : function(e, result) {
				var resultInfo = result.result;
				//单独提取文件信息列表
				var fileInfoList = getFileInfoList(resultInfo);
				if(resultInfo.type == "info"){
					$id("shopLogoUuid").val(fileInfoList[0].fileUuid);
					$id("shopLogoUsage").val(fileInfoList[0].fileUsage);
					$id("shopLogoPath").val(fileInfoList[0].fileRelPath);
					$id("shopLogoBrowseUrl").val(fileInfoList[0].fileBrowseUrl);
					$("#shopLogo").attr("src",fileInfoList[0].fileBrowseUrl+"?"+new Date().getTime());
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
	//获取登录信息
	function ajaxUser(){
    	var ajax = Ajax.post("/user/info/context/get");
    	ajax.done(function(result, jqXhr){
    		if(result.type== "info"){
    			var data = result.data;
    			if(data.userName){
    				$id("userName").html(data.userName);
    			}else{
    				$id("userName").html(data.phoneNo);
    			}
    			var entity = data.scopeEntity;
    			if(entity){
    				if(entity.logoUrl){
    					$id("scopeLogoImg").attr("src",entity.logoUrl);
    				}
    			}
    		}else{
    			Layer.warning(result.message);
    			setPageUrl(getAppUrl("/user/login/jsp"));
    		}
    	});
    	ajax.go();
    }
	
</script>
</body>
</html>