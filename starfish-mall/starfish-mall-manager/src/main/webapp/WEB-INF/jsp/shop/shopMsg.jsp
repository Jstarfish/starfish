<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>基本信息</title>
</head>

<body id="rootPanel">
		<div id="mainPanel" class="ui-layout-center" style="padding:0px;">
			<div id="theTabsCtrl" class="noBorder">
				<ul>
					<li>
						<a href="#baseInfo" id = "base_info">基本信息</a>
					</li>
					<li>
						<a href="#shopImage" id = "shop_image">店铺相册</a>
					</li>
					<li>
						<a href="#shopNotice" id = "shop_notice">店铺公告</a>
					</li>
					<%--<li>
						<a href="#invoiceSetting" id = "invoice_setting">发票设置</a>
					</li>
					li><a href="#smsSetting" id = "sms_setting">短信设置</a></li> --%>
				</ul>
				<div id="baseInfo">
				    <fieldset>
						<legend>店铺信息</legend>
						<div id="addForm" class="form">
							<div class="field row" style="height: 90px" >
								 <label class="field label one half wide ">LOGO</label> 
								 <img id="shopLogoPath" width="100px" height="80px" />
								 <span class="normal spacer"></span>
							</div>
							<div class="field row">
								<label class="field label one half wide">店铺名称</label> 
								<input type="text" id="shopName" class="field two value wide" />
								<span class="normal spacer"></span>
								<label class="field label inline one half wide ">店铺编号</label> 
								<input type="text" id="shopId" class="field two value wide" />
								<span class="normal spacer"></span>
							</div>
							<div class="field row">
								<label class="field label one half wide ">所在地</label> 
								<input type="text" id="shopRegionName" class="field two value wide" />
								<span class="normal spacer"></span>
								<label class="field label inline one half wide ">店铺状态</label> 
								<div class="field group">
									<input id="shopClose-Y" type="radio" name="shopClose" value="0" />
									<label for="shopClose-Y">启用</label>
									<input id="shopClose-N" type="radio" name="shopClose" value="1" />
									<label for="shopClose-N">禁用</label>
								</div>
							</div>
								
							<div class="field row">
								<label class="field label one half wide ">联系人</label> 
								<input type="text" id="shopLinkMan" class="field two value wide" />
								<span class="normal spacer"></span>
								<label class="field label inline one half wide ">手机号码</label> 
								<input type="text" id="shopPhoneNo" class="field two value wide" />
								
							</div>
							<div class="field row" style="height: 80px">
								<label class="field label one half wide ">店铺描述</label> 
								<textarea id="shopBizScope" class="field two value wide " style="height:50px; width: 420px"></textarea>
							    <span class="normal spacer"></span>
							   
							</div>
						</div>	
					</fieldset>
				    <fieldset>
						<legend>商户信息</legend>
						<div  class="form">
							<div class="field row">
								<label class="field label one half wide ">商户编号</label> 
								<input type="text" id="merchantId" class="field two value wide" />
								<span class="normal spacer"></span>
								<label class="field label inline one half wide ">商户姓名</label> 
								<input type="text" id="merchantNickName" class="field two value wide" />
							</div>
							<div class="field row">
								<label class="field label one half wide ">联系电话</label> 
								<input type="text" id="merchantPhoneNo" class="field two value wide" />
								<span class="normal spacer"></span>
								<label class="field label inline one half wide ">身份证号</label> 
								<input type="text" id="merchantIdCertNo" class="field two value wide" />
							</div>
							<div class="field row">
								<label class="field label one half wide ">邮箱地址</label> 
								<input type="text" id="merchantEmail" class="field two value wide" />
								<span class="normal spacer"></span>
								<label class="field label inline one half wide ">注册时间</label> 
								<input type="text" id="merchantTs" class="field two value wide" />
							</div>
							<div class="field row">
								<label class="field label one half wide ">商户状态</label> 
								<div class="field group">
									<input id="merchantDisabled-Y" type="radio" name="merchantDisabled" value="0" />
									<label for="merchantDisabled-Y">启用</label>
									<input id="merchantDisabled-N" type="radio" name="merchantDisabled" value="1" />
									<label for="merchantDisabled-N">禁用</label>
								</div>
							</div>
						</div>	
					</fieldset>
					<fieldset>
						<legend>营业执照</legend>
						<div  class="form">
							<div class="field row" style="height: 90px" >
								 <label class="field label one half wide ">营业执照图片</label> 
								 <img id="bizLicenseImg" width="100px" height="80px" src="<%=resBaseUrl%>/image-app/img-none.jpg"/>
								 <span class="normal spacer"></span>
							</div>
							<div class="field row">
								<label class="field label one half wide ">注册号</label> 
								<input type="text" id="regNo" class="field two value wide" />
								<span class="normal spacer"></span>
								<label class="field label inline one half wide ">名称</label> 
								<input type="text" id="companyName" class="field two value wide" />
							</div>
							<div class="field row">
								<label class="field label one half wide ">类型</label> 
								<input type="text" id="companyType" class="field two value wide" />
								<span class="normal spacer"></span>
								<label class="field label inline one half wide ">住所</label> 
								<input type="text" id="companyAddr" class="field two value wide" />
							</div>
								
							<div class="field row">
								<label class="field label one half wide ">法定代表人</label> 
								<input type="text" id="legalMan" class="field two value wide" />
								<span class="normal spacer"></span>
								<label class="field label inline one half wide ">注册资本</label> 
								<input type="text" id="regCapital" class="field two value wide" />
							</div>
							<div class="field row">
								<label class="field label one half wide ">成立日期</label> 
								<input type="text" id="estabDate" class="field two value wide" />
								<span class="normal spacer"></span>
								<label class="field label inline one half wide ">营业期限</label> 
								<div class="field group">
									<input class="field value" id="startDate" />
									至
									<input class="field value" id="endDate" />
								</div>
							</div>
							<div class="field row" style="height: 80px">
								<label class="field label one half wide">经营范围：</label>
								<textarea id="bizScope" class="field two value wide " style="height:50px; width: 420px" disabled></textarea>
							</div>
						</div>	
					</fieldset>
					
				</div>
				<div id="shopImage">
					<div  class="ui-layout-north">
						<div class="filter section">
							<div class="filter row">
								<div class="group left aligned">
									<button id="btnAddShopImage" class="button">添加</button>
									<span class="spacer"></span>
									<button id="btnDelShopImageByIds" class="button">批量删除</button>
								</div>
							</div>
						</div>
					</div>
					<table id="shopImageList"></table>
					<div id="shopImagePager"></div>
					<div id="shopImageDialog"></div>
				</div>
				<div id="shopNotice">
					<div  class="ui-layout-north">
						<div class="filter section">
							<div class="filter row">
								<div class="group left aligned">
									<button id="btnAddShopNotice" class="button">添加</button>
									<span class="spacer"></span>
									<button id="btnDelShopNoticeByIds" class="button">批量删除</button>
								</div>
							</div>
						</div>
					</div>
					<table id="shopNoticeList"></table>
					<div id="shopNoticePager"></div>
					<div id="shopNoticeDialog"></div>
				</div>
				<%--div id="invoiceSetting">
					<div id="invoiceType">
						<div class="filter section" align="right">
							<div class="filter row">
								<button class="normal button" id="btnInvoiceSave">保存</button>
								<span class="spacer"></span>
							</div>
						</div>
					    <div  class="form">
					    	<div class="field row" style="height: 100px;"> 
					    	    <label class="field inline"></label> 
					   			<input id="basicBill" type="checkbox" name="bill" value="0" />
					   			<label for="basicBill" style="color:gray;">本店铺提供“普通”发票，收取开票金额
					   				<input type="text"  id="basicBillValue"value="2.0" class="field value"  style="width: 50px"/> %税点，及购买100元商品如开发票，实际收款应该为100+100×<span id="basicBillSpan">2.0</span>%
					   				<font size="2" color="red"> 税点不能小于2.0%</font>
					   			</label>
					   			<br/>
					  	 		<label class="field inline"></label> 
					  	 		<input id="addValueBill" type="checkbox" name="bill" value="1" /> 
					  	 		<input id="shopIdBillHidden" type="hidden"  value="" />
					  	 		<label  for="addValueBill" style="color:gray;">本店铺提供“增值税”发票，收取开票金额
					  	 			<input type="text" id="addValueBillValue" value="2.0" class="field value" style="width: 50px" /> %税点，及购买100元商品如开发票，实际收款应该为100+100×<span id="addValueBillSpan">2.0</span>%
					  	 			<font size="2" color="red"> 税点不能小于2.0%</font>
					  	 		</label>
					  	 		<br>
								<span class="normal spacer"></span>
					        </div>
					    </div>
					</div> --%>
				</div>
				<div id="smsSetting"></div>
			</div>
		</div>
        <jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
		<script type="text/javascript">
		
		   //------------------------初始化变量-------------------------
		   var noticeGridHelper = JqGridHelper.newOne("");
		   var imageGridHelper = JqGridHelper.newOne("");
		   var shopNoticeGrid;
		   var shopImageGrid;
		   var jqTabsCtrl;
		   //店铺公告表单代理
		   var noticeFormProxy = FormProxy.newOne();
		   //店铺相册表单代理
		   var imageFormProxy = FormProxy.newOne();
		   //当前tab页id
		   var currentTab = "base_info";
		   //临时值
		   var tempShopNotice;
		   var tempId = null;
		   var tempSmsId = null;
		   var smsCode = "sms";
		   var statusTextMap = new KeyMap();
		   statusTextMap.add(0,"未发布");
		   statusTextMap.add(1,"发布中");
		   statusTextMap.add(2,"已停止");
		   //
		   var autoFlagTextMap = new KeyMap();
		   autoFlagTextMap.add("true","自动");
		   autoFlagTextMap.add("false","手动");
		   //
		   var statusBtnTextMap = new KeyMap();
		   statusBtnTextMap.add(0,"立刻发布");
		   statusBtnTextMap.add(1,"停止发布");
		   statusBtnTextMap.add(2,"重新发布");
		   //
		   var shopNoticeDialog;
		   var shopImageDialog;
		   var curAction;
		   
		 //扩展对话框按钮栏控件（复选框）
			var dlgToolbarCtrlsName = "dlg-toolbar-ctrls";
			var dlgToolbarCtrlsHtml = '<span style="float:left;padding-left:15px;line-height:40px;color:blue;" class="align middle" name="' + dlgToolbarCtrlsName + '"><input class="align middle" type="checkbox" name="' + dlgToolbarCtrlsName + '-checkbox"  id="' + dlgToolbarCtrlsName + '-checkbox-id" />&nbsp;<label class="align middle" for="' + dlgToolbarCtrlsName + '-checkbox-id">保存后继续添加<label></span>';
			var dlgToolbarCtrls = null;
		 //------------------------验证-------------------------
			//实例化发票设置表单代理
			var formBillProxy = FormProxy.newOne();
			formBillProxy.addField({
				id : "basicBillValue",
				key : "plain.taxRate",
				type : "float",
				rules : ["isMoney","rangeValue[2.0,100]"],
			});
			formBillProxy.addField({
				id : "addValueBillValue",
				key : "added.taxRate",
				type : "float",
				rules : ["isMoney","rangeValue[2.0,100]"],
			});

			//------------------------初始化方法-------------------------
			$(function() {
				//页面基本布局
				$id('rootPanel').layout({
					spacing_open : 1,
					spacing_closed : 1,
					north__size : 60,
					allowTopResize : false
				});
				//隐藏布局north分割线
				$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");

				//基本信息
				jqTabsCtrl = $("#theTabsCtrl").tabs({});
				jqTabsBillCtrl = $("#theBillCtrl").tabs({});

				//加载店铺信息
				loadShopMsg();
				//加载发票设置
				loadInvoice();
				
				//初始化模板
				initShopImageTpl();
				initShopNoticeTpl();
				//initSmsSettingTpl();
				loadShopImage();
				loadShopNotice();
				loadSmsSetting();
				
				//绑定保存按钮事件
				$id("btnAddShopImage").click(shopImageDialogAdd);
				$id("btnDelShopImageByIds").click(delShopImageByIds);
				
				$id("btnAddShopNotice").click(shopNoticeDialogAdd);
				$id("btnDelShopNoticeByIds").click(delShopNoticeByIds);
				$id("btnSmsSave").click(saveSmsSetting);
				$id("btnInvoiceSave").click(saveInvoice);
				
				//绑定取消按钮事件
				$id("btnNoticeCancel").click(cancelMethod);
// 				$id("btnSmsCancel").click(cancelMethod);
				$id("btnInvoiceCancel").click(cancelMethod);
				//上传Logo
				initFileUpload("fileToUploadShopImage");
				$id("btnShopImageToUploadFile").click(function() {
					fileToUploadFileIcon("fileToUploadShopImage");
				});
				
				//tab切换事件
				$id("theTabsCtrl").on("tabsactivate", function(event, ui) {
					// 隐藏店铺设置页面所有错误提示
					hideAllMessages();
					// 绑定保存操作
					currentTab = ui.newTab.context.id;
				});
				
				// 页面自适应
				winSizeMonitor.start(adjustCtrlsSize);
			});
			
			//取消按钮绑定方法
			function cancelMethod(){
				hideAllMessages();
				loadShopMsg();
				loadShopNotice();
				loadInvoice();
				loadSmsSetting();
			}
			
			// 隐藏店铺设置页面所有错误提示
			function hideAllMessages() {
				noticeFormProxy.hideMessages();
				formBillProxy.hideMessages();
			}
			
			//保存店铺公告
			function saveShopNotice(){
				var vldResult = noticeFormProxy.validateAll();
				if(vldResult){
					var ajax = Ajax.post("/shop/notice/save/do");
					ajax.data({
						title : textGet("title"),
						content : textGet("content"),
						autoFlag : radioGet("autoFlag")==true,
						pubTime : textGet("pubTime"),
						endTime : textGet("endTime"),
						status : "0",
					});
					ajax.done(function(result, jqXhr) {
						Layer.msgSuccess(result.message);
						shopNoticeGrid.jqGrid("setGridParam").trigger("reloadGrid");
						tempShopNotice = result.data;
						shopNoticeDialogView(tempShopNotice);
					});
					ajax.go(); 
				}
			}	
			 
			//保存店铺图片
			function saveShopImage(){
				var vldResult = imageFormProxy.validateAll();
				if(vldResult){
					var ajax = Ajax.post("/shop/album/image/save/do");
					ajax.data({
						title : textGet("imageTitle"),
						imageUuid : textGet("imageUuid"),
						imageUsage : textGet("imageUsage"),
						imagePath : textGet("imagePath"),
					});
					ajax.done(function(result, jqXhr) {
						Layer.msgSuccess(result.message);
						shopImageGrid.jqGrid("setGridParam").trigger("reloadGrid");
					});
					ajax.go(); 
				}
			}
			 
			//
			function updateShopImage(){
				var vldResult = imageFormProxy.validateAll();
				if(vldResult){
					var ajax = Ajax.post("/shop/album/image/update/do");
					var dataMap = {
							id : intGet("shopImageId"),
							title : textGet("imageTitle"),
							imageUuid : textGet("imageUuid"),
							imageUsage : textGet("imageUsage"),
							imagePath : textGet("imagePath"),
					};
					ajax.sync();
					ajax.data(dataMap);
					ajax.done(function(result, jqXhr) {
						tempShopImage = result.data;
						Layer.msgSuccess(result.message);
						shopImageGrid.jqGrid("setGridParam").trigger("reloadGrid");
						getCallbackAfterGridLoaded = function(){
							return function(){
								shopImageDialogView(tempShopImage.id);
							};
						};						
					});
					ajax.go(); 
				}
			}
			 
			//------------------------自动调整控件大小-------------------------
			//调整控件大小
			function adjustCtrlsSize() {
				var jqMainPanel = $id("mainPanel");
				var mainWidth = jqMainPanel.width();
				var mainHeight = jqMainPanel.height();
				console.log("mainWidth:" + mainWidth + ", " + "mainHeight:" + mainHeight);
				//

				var tabsCtrlWidth = mainWidth - 4;
				var tabsCtrlHeight = mainHeight - 8;
				jqTabsCtrl.width(tabsCtrlWidth);
				var tabsHeaderHeight = $(">[role=tablist]", jqTabsCtrl).height();
				var tabsPanels = $(">[role=tabpanel]", jqTabsCtrl);
				tabsPanels.width(tabsCtrlWidth - 4 * 2);
				tabsPanels.height(tabsCtrlHeight - tabsHeaderHeight - 30);
				
				var gridCtrlId,jqGridBox,headerHeight,pagerHeight;
				gridCtrlId = "shopNoticeList";
				jqGridBox = $("#gbox_" + gridCtrlId);
				headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
				pagerHeight = $id("shopNoticePager").height();
				console.log("tabsCtrlHeight:"+tabsCtrlHeight);
				console.log("tabsHeaderHeight:"+tabsHeaderHeight);
				console.log("pagerHeight:"+pagerHeight);
				console.log("headerHeight:"+headerHeight);
				shopImageGrid.setGridWidth(tabsCtrlWidth - 2*4);
				if(pagerHeight < 1 || headerHeight < 1){
					shopImageGrid.setGridHeight(tabsCtrlHeight - tabsHeaderHeight - 136);
				}else{
					shopImageGrid.setGridHeight(tabsCtrlHeight - tabsHeaderHeight - pagerHeight - headerHeight - 81);
				}
				
				shopNoticeGrid.setGridWidth(tabsCtrlWidth - 2*4);
				if(pagerHeight < 1 || headerHeight < 1){
					shopNoticeGrid.setGridHeight(tabsCtrlHeight - tabsHeaderHeight - 136);
				}else{
					shopNoticeGrid.setGridHeight(tabsCtrlHeight - tabsHeaderHeight - pagerHeight - headerHeight - 81);
				}
				
				
			}

			 //------------------------加载店铺信息-------------------------
			 function loadShopMsg(){
				var ajax = Ajax.post("/shop/shop/get/by/id");
					ajax.done(function(result, jqXhr){
						if(result.type== "info"){
							var data=result.data;
							$id("shopId").val(data.shop.id);
							$id("shopName").val(data.shop.name);
							$id("shopClose").val();
							$id("shopRegionName").val(data.shop.regionName);
							$id("shopPhoneNo").val(data.shop.phoneNo);
							$id("shopLinkMan").val(data.shop.linkMan);
							//$id("shopAcctName").val(data.merchantAccount.acctName);
							//$id("shopAcctNo").val(data.merchantAccount.acctNo);
							//$id("shopBankName").val(data.merchantAccount.bankName);
							$id("shopBizScope").val(data.shop.bizScope);
							if(data.shop.logoPath!=null){
								$("#shopLogoPath").attr("src",data.shop.fileBrowseUrl);	
							}

							if(data.shop.closed==false){
								$("input[name='shopClose'][value='0']").attr("checked","checked");	
							}else{
								$("input[name='shopClose'][value='1']").attr("checked","checked");
							}
							
							$("#baseInfo").find("input").attr("disabled",true);
							$("#baseInfo").find("input:radio").attr("disabled",true);
							$("#baseInfo  input").attr("readonly",true);
							$id("shopBizScope").attr("disabled","disabled");
							
							if(data.merchantId != null){
								$id("merchantId").val(data.merchant.id);
								$id("merchantNickName").val(data.merchant.name);
								$id("merchantIdCertNo").val(data.merchant.idCertNo);
								$id("merchantPhoneNo").val(data.shop.phoneNo);
								$id("merchantEmail").val(data.merchant.user.email);
								$id("merchantTs").val(data.merchant.user.ts);
								if(data.merchant.disabled==false){
									$("input[name='merchantDisabled'][value='0']").attr("checked","checked");	
								}else{
									$("input[name='merchantDisabled'][value='1']").attr("checked","checked");
								}
							}
							
							if(data.accountId != null){
								if(data.userAccount.shopType==false){
									$("input[name='shopType'][value='0']").attr("checked","checked");	
								}else{
									$("input[name='shopType'][value='1']").attr("checked","checked");
								}
								$id("bankCode").val(data.userAccount.bankCode);
								$id("acctNo").val(data.userAccount.acctNo);
								$id("acctName").val(data.userAccount.acctName);
								$id("acctPhoneNo").val(data.userAccount.acctPhoneNo);
								$id("vfCode").val(data.userAccount.vfCode);
							}
							
							if(data.licenseId != null){
								if(data.bizLicensePic.imagePath!=null){
									$("#bizLicenseImg").attr("src",data.bizLicensePic.fileBrowseUrl);	
								}
								$id("regNo").val(data.bizLicense.regNo);
								$id("companyName").val(data.bizLicense.name);
								$id("companyType").val(data.bizLicense.type);
								$id("companyAddr").val(data.bizLicense.address);
								$id("legalMan").val(data.bizLicense.legalMan);
								$id("regCapital").val(data.bizLicense.regCapital);
								$id("estabDate").val(data.bizLicense.estabDate);
								$id("startDate").val(data.bizLicense.startDate);
								$id("endDate").val(data.bizLicense.endDate);
								$id("bizScope").val(data.bizLicense.bizScope);
							}
							
						}else{
						}
					});
					ajax.go();
			   }
			//------------------------发票设置-------------------------
			  //加载发票设置
			 function loadInvoice(){
				var postData = {
				code:"invoice"
				};
				var ajax = Ajax.post("/shop/param/val/get");
				ajax.data(postData);
				ajax.done(function(result, jqXhr){
					if(result.type== "info"){
					 var data= result.data;	
					 if(data){
						  fillInvoiceData(data); 
					  }
					}
				});
				ajax.go();	
			} 	
			
			
			   //保存更新发票设置
				function saveInvoice(){
					var vldResult = formBillProxy.validateAll();
					if (!vldResult) {
						return;
					}
					if($id('#basicBill').is(':checked')) {
						plainFlag=true;
					}else{
						plainFlag=false;
					}
					if($id('#addValueBill').is(':checked')) {
						addFlag=true;
					}else{
						addFlag=false;
					}
					id=$id("shopIdBillHidden").val();
					if(id==""){
						id=null;	
					}
					var plainValue=$id("basicBillValue").val();
					var addValue =$id("addValueBillValue").val();
					var postData = {
							id:id,
							invoicePlainProvided : plainFlag,
							invoicePlainTaxRate:plainValue,
							invoiceAddedProvided:addFlag,
							invoiceAddedTaxRate:addValue
						};
						var ajax = Ajax.post("/shop/param/val/save/do");
							ajax.data(postData);
							ajax.done(function(result, jqXhr){
								if(result.type== "info"){
									$id("basicBillSpan").html(plainValue);
									$id("addValueBillSpan").html(addValue);
									$id("btnInvoiceSave").html("修改");
									Layer.msgSuccess(result.message);
								}
							});
							ajax.go();	
				} 
				//发票设置(填充数据)
				 function fillInvoiceData(data){
					 $id("shopIdBillHidden").val(data.id);
					 var parm= JSON.decode(data.value);
					 formBillProxy.setValue2("plain.taxRate",parm.plain.taxRate);
					 formBillProxy.setValue2("added.taxRate",parm.added.taxRate);
					 //$id("basicBillValue").val(parm.plain.taxRate);
					 //$id("addValueBillValue").val(parm.added.taxRate);
					 $id("basicBillSpan").html(parm.plain.taxRate);
					 $id("addValueBillSpan").html(parm.added.taxRate);
					 if(parm.plain.provided==true){
						 $id('basicBill').attr("checked","checked");
					 }else{
						 $id('basicBill').removeAttr("checked"); 
					 }
					 if(parm.added.provided==true){
						 $id('addValueBill').attr("checked","checked");
					 }else{
						 $id('addValueBill').removeAttr("checked");  
					 }
				}
				
				
			//初始化店铺相册模板
			 function initShopImageTpl(){
				var imageTplHtml = $id("shopImageTpl").html();
				var htmlStr = laytpl(imageTplHtml).render({});
				$id("shopImageDialog").html(htmlStr);
				shopImageDialogAdd();
				$("#shopImageDialog").dialog("close");
				//店铺公告表达代理验证
				imageFormProxy.addField({
					id : "imageTitle",
					rules : ["maxLength[30]"]
				});
				imageFormProxy.addField({
					id : "shopImageSrc",
					rules : [{
						rule : function(idOrName, type, rawValue, curData) {
							var img = $id("shopImageSrc");
							var imgSrc = $(img).attr("src");
							if(imgSrc == ""){
								return false;
							}
							return true;
						},
						message: "亲,您还未上传店面图片哦！"
					}],
					messageTargetId : "shopImageSrc"
				});
					
			 }
				
			 //初始化店铺公告模板
			 function initShopNoticeTpl(){
					var noticeTplHtml = $id("shopNoticeTpl").html();
					var htmlStr = laytpl(noticeTplHtml).render({});
					$id("shopNoticeDialog").html(htmlStr);
					initDialogAdd();
					$('input[name="autoFlag"]').change(onAutoFlagChange);
					//店铺公告表达代理验证
					noticeFormProxy.addField({
						id : "title",
						required : true,
						rules : ["maxLength[100]"]
					});
					noticeFormProxy.addField({
						id : "content",
						rules : ["maxLength[1000]"]
					});
					noticeFormProxy.addField({
						id : "pubTime",
						type : "datetime",
						rules : [ "isDate", {
							rule : function(idOrName, type, rawValue, curData) {
								var autoVal = radioGet("autoFlag");
								if(autoVal == true){
									return !isNullOrEmpty(rawValue);
								}else{
									return true;
								}				
							},
							message : "此为必须提供项"
						}]
					});
					noticeFormProxy.addField({
						id : "endTime",
						type : "datetime",
						rules : [ "isDate", {
							rule : function(idOrName, type, rawValue, curData) {
								var autoVal = radioGet("autoFlag");
								if(autoVal == true){
									return !isNullOrEmpty(rawValue);
								}else{
									return true;
								}				
							},
							message : "此为必须提供项"
						}]
					});
					
			 }
			 
			 //初始化短信设置模板
			 function initSmsSettingTpl(){
				 var smsTplHtml = $id("smsSettingTpl").html();
				 var htmlStr = laytpl(smsTplHtml).render({});
				 $id("smsSetting").html(htmlStr);
			 }
			 
			 //发布方式change事件
			 function onAutoFlagChange(){
				 hideMiscTip("pubTime");
				 hideMiscTip("endTime");
				 var autoVal = radioGet("autoFlag");
				 if(autoVal == false){
					$id("lblPublishTime").removeClass("required");
					$id("beginTime").css("display", "none");
				 }else{
					$id("lblPublishTime").addClass("required");
					$id("beginTime").css("display", "block");
				 }
			 }
			 
			//加载店铺相册
			 function loadShopImage(){
				shopImageGrid = $id("shopImageList").jqGrid({
					url : getAppUrl("/shop/album/image/get/-shop"),
					contentType : 'application/json',
					mtype : "post",
					datatype : 'json',
					colNames : [ "id", "标题", "图片",  "上传时间", "操作" ],
					colModel : [
							{
								name : "id",
								index : "id",
								hidden : true
							},
							{
								name : "title",
								width : 100,
								align : 'left',
							},
							{
								name : "fileBrowseUrl",
								align : 'center',
								formatter : function(cellValue,
										option, rowObject) {
									return "<img src="+ rowObject.fileBrowseUrl + "?" + new Date().getTime() +" height='100px' weight='100px' >" || "" ;
								}
							},
							{
								name : "ts",
								width : 150,
								align : 'center',
								formatter : function(cellValue,
										option, rowObject) {
									return rowObject.ts || "";
								}
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue,
										option, rowObject) {
									return "<span> [<a href='javascript:void(0);' onclick='shopImageDialogView("
											+ JSON.encode(cellValue)
											+ ")' >查看</a>]   [<a href='javascript:void(0);' onclick='shopImageDialogEdit("
											+ JSON.encode(cellValue)
											+ ")' >修改</a>]  [<a href='javascript:void(0);' onclick='deleteShopImage("
											+ cellValue
											+ ")' >删除</a>] ";
								},
								width : 150,
								align : "center"
							} ],
					multiselect : true,
					multikey:'ctrlKey',
					pager : "#shopImagePager",
					height : "auto",
					loadComplete : function(gridData){
						imageGridHelper.cacheData(gridData);
						var callback = getCallbackAfterGridLoaded();
						if(isFunction(callback)){
							callback();
						}
					},
					ondblClickRow: function(rowId) {
						var userMap = ImageGridHelper.getRowData(rowId)
						shopImageDialogView(userMap);
					}
				
				});
			 }
			
			 function getCallbackAfterGridLoaded(){}
			 
			 //加载店铺公告
			 function loadShopNotice(){
				shopNoticeGrid = $id("shopNoticeList").jqGrid({
					url : getAppUrl("/shop/notice/get"),
					contentType : 'application/json',
					mtype : "post",
					datatype : 'json',
					colNames : [ "id", "标题", "状态", "发布方式", "发布时间","操作" ],
					colModel : [
							{
								name : "id",
								index : "id",
								hidden : false
							},
							{
								name : "title",
								width : 100,
								align : 'left',
							},
							{
								name : "status",
								align : 'left',
								formatter : function(cellValue,
										option, rowObject) {
									return statusTextMap.get(cellValue);
								}
							},
							{
								name : "autoFlag",
								align : 'left',
								formatter : function(cellValue,
										option, rowObject) {
									return autoFlagTextMap.get(cellValue);
								}
							},
							{
								name : "id",
								width : 150,
								align : 'left',
								formatter : function(cellValue,
										option, rowObject) {
									return rowObject.pubTime || "";
								}
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue,
										option, rowObject) {
									return "<span> [<a href='javascript:void(0);' onclick='shopNoticeDialogView("
											+ JSON.encode(rowObject)
											+ ")' >查看</a>]   [<a href='javascript:void(0);' onclick='shopNoticeDialogEdit("
											+ JSON.encode(rowObject)
											+ ")' >修改</a>]  [<a href='javascript:void(0);' onclick='deleteShopNotice("
											+ cellValue
											+ ")' >删除</a>] ";
								},
								width : 150,
								align : "center"
							} ],
					multiselect : true,
					multikey:'ctrlKey',
					pager : "#shopNoticePager",
					height : "auto",
					loadComplete : function(gridData){
						noticeGridHelper.cacheData(gridData);
					},
					ondblClickRow: function(rowId) {
						var userMap = noticeGridHelper.getRowData(rowId)
						shopNoticeDialogView(userMap);
					}
				
				});
			 }
			 
			 //初始化日历插件
			 function initDatePicker(){
				var pubTime = textGet("pubTime");
				pubTime = isNoB(pubTime) ? null : pubTime;
				var endTime = textGet("endTime");
				endTime = isNoB(endTime) ? null : endTime;
				$id("pubTime").datetimepicker({
					maxDate : endTime,
					changeMonth: true,
					changeYear: true,
					format: 'Y-m-d H:i',
					lang:'ch',
				    onSelect:function(dateText,inst){
				       $id("endTime").datepicker("option","minDate",dateText);
				       noticeFormProxy.validate("pubTime");
				    }
				});
				$id("endTime").datetimepicker({
					minDate :pubTime == null ? 0 : pubTime, 
					changeMonth: true,
					changeYear: true,
					format: 'Y-m-d H:i',
					lang:'ch',
					onSelect:function(dateText,inst){
				        $id("pubTime").datepicker("option","maxDate",dateText);
				        noticeFormProxy.validate("endTime");
				    }
				});
			 }
			 
			 //
				function fileToUploadFileIcon(fileId) {
					var fileUuidToUpdate = $id("imageUuid").val();
					if (isNoB(fileUuidToUpdate)) {
						var formData = {
							usage : "image.logo",
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
								$id("imageUuid").val(resultInfo.data.files[0].fileUuid);
								$id("imageUsage").val(resultInfo.data.files[0].fileUsage);
								$id("imagePath").val(resultInfo.data.files[0].fileRelPath);
								//$("#logoImgshop").makeUniqueRequest("");
								$("#shopImageSrc").attr("src", resultInfo.data.files[0].fileBrowseUrl + "?" + new Date().getTime());
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
			 
			 function shopImageDialogAdd() {
				clearImageDialog();
				curAction = "add";
				showShopImageDialog();
			}
			 
			 //
			function shopImageDialogView(id) {
				clearImageDialog();
				curAction = "view";
				showShopImageDialog(id);
			}

				//
			function shopImageDialogEdit(id) {
				clearImageDialog();
				curAction = "mod";
				showShopImageDialog(id);
			}
			 
		 	//
			function deleteShopImage(id){
				var theLayer = Layer.confirm('确定要删除？', function() {
					//
					var hintBox = Layer.progress("正在删除...");
					var ajax = Ajax.post("/shop/album/image/delete/do");
					ajax.params({
						id : id
					});
					ajax.done(function(result, jqXhr) {
						if (result.type == "info") {
							Layer.msgSuccess(result.message);
							//
							shopImageGrid.jqGrid("setGridParam").trigger("reloadGrid");
						} else {
							Layer.msgWarning(result.message);
						}
					});
					ajax.always(function() {
						hintBox.hide();
					});
					ajax.go();
				});
			}
			 
			 //
			function delShopImageByIds(){
				var ids = shopImageGrid.jqGrid("getGridParam", "selarrrow");
				var postData = [];
				for (var i = 0; i < ids.length; i++) {
					postData.add(ParseInt(ids[i]));
				}
				if (ids == "") {
					Toast.show("请选择要删除的数据！");
				} else {
					var theLayer = Layer.confirm('确定要删除选择的数据吗？', function() {
						//
						var hintBox = Layer.progress("正在删除...");
						var ajax = Ajax.post("/shop/image/delete/batch/do");
						ajax.data(postData);
						ajax.done(function(result, jqXhr) {
							if (result.type == "info") {
								Layer.msgSuccess(result.message);
								//
								shopImageGrid.jqGrid("setGridParam").trigger("reloadGrid");
							} else {
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
			 
			function clearImageDialog() {
				curAction = null;
				imageFormProxy.hideMessages();
				textSet("imageTitle", null);
				textSet("imageUuid", "");
				textSet("imageUsage","");
				textSet("imagePath","");
				$("#shopImageSrc").attr("src", "");
			}
			 
			 //加载短信设置
			 function loadSmsSetting(){
					var ajax = Ajax.post("/shop/param/val/get");
					ajax.data({
						code : smsCode
					});
					ajax.sync();
					ajax.done(function(result, jqXhr) {
						var data = result.data;
						if(data != null){
							var dataVal = JSON.decode(data.value);
							tempSmsId = data.id;
							radioSet("smsSendToCustomerOnMerchantDispatching",dataVal.sendToCustomerOnMerchantDispatching);
							radioSet("smsSendToMerchantOnCustomerCreatingOrder",dataVal.sendToMerchantOnCustomerCreatingOrder);
							radioSet("smsSendToCustomerOnMerchantActivity",dataVal.sendToCustomerOnMerchantActivity);
						}
					});
					ajax.go(); 
			 }
			 
			 //保存短信设置
			 function saveSmsSetting(){
					var ajax = Ajax.post("/shop/param/val/save/do");
					ajax.sync();
					ajax.data({
						id : tempSmsId,
						smsSendToCustomerOnMerchantDispatching : ParseInt(FormProxy.fieldAccessors.radio.get("smsSendToCustomerOnMerchantDispatching")),
						smsSendToMerchantOnCustomerCreatingOrder : ParseInt(FormProxy.fieldAccessors.radio.get("smsSendToMerchantOnCustomerCreatingOrder")),
						smsSendToCustomerOnMerchantActivity : ParseInt(FormProxy.fieldAccessors.radio.get("smsSendToCustomerOnMerchantActivity"))
					});
					ajax.done(function(result, jqXhr) {
						Layer.msgSuccess(result.message);
						loadSmsSetting();
					});
					ajax.go(); 
			 }
			//初始化dialogAdd
			function showShopImageDialog(dataId) {
				//
				var theDlgId = "shopImageDialog";
				jqDlgDom = $id(theDlgId);
				//清除扩展的对话框按钮栏控件
				if (true) {
					var jqButtonPane = jqDlgDom.siblings(".ui-dialog-buttonpane");
					dlgToolbarCtrls = jqButtonPane.find("[name='" + dlgToolbarCtrlsName + "']");
					dlgToolbarCtrls.remove();
				}
				//对话框配置
				var dlgConfig = {
					width : Math.min(600, $window.width()),
					height : Math.min(380, $window.height()),
					modal : true,
					open : false
				};
				//
				if (curAction == "add") {
					dlgConfig.title = "商户图片 - 新增";
					dlgConfig.buttons = {
						"保存" : function() {
							//收集并验证要提交的数据（如果验证不通过直接返回 return）
							var vldResult = imageFormProxy.validateAll();
							if (!vldResult) {
								return;
							}
							var postData = imageFormProxy.getValues();
							//...
							//
							saveShopImage();
							var continuousFlag = jqDlgDom.prop("continuousFlag");
							if (continuousFlag) {
								//连续新增
								clearImageDialog();
								return;
							}else {
								$(this).dialog("close");
							}
						},
						"取消" : function() {
							//
							jqDlgDom.prop("continuousFlag", false);
							//
							$(this).dialog("close");
							//隐藏表单验证消息
							imageFormProxy.hideMessages();
						}
					};
					//
					if (true) {
						//显示“保存后继续添加”复选框
						dlgConfig.open = function(event, ui) {
							//
							var jqButtonPane = jqDlgDom.siblings(".ui-dialog-buttonpane");
							dlgToolbarCtrls = $($(dlgToolbarCtrlsHtml).appendTo(jqButtonPane));
							var theCheckbox = dlgToolbarCtrls.find("[name='" + dlgToolbarCtrlsName + "-checkbox']");
							var continuousFlag = jqDlgDom.prop("continuousFlag");
							theCheckbox.prop("checked", true);
							jqDlgDom.prop("continuousFlag", true);
							//
							theCheckbox.bind("click", function() {
								jqDlgDom.prop("continuousFlag", this.checked);
							});
						};
					}
				} else if (curAction == "mod") {
					dlgConfig.title = "商户图片 - 修改";
					dlgConfig.buttons = {
						"保存" : function() {
							//收集并验证要提交的数据（如果验证不通过直接返回 return）
							var vldResult = imageFormProxy.validateAll();
							if (!vldResult) {
								return;
							}
							var postData = imageFormProxy.getValues();
							//
							updateShopImage(dataId);
						},
						"取消" : function() {
							$(this).dialog("close");
							//隐藏表单验证消息
							imageFormProxy.hideMessages();
						}
					};
				} else {
					//== view 查看
					dlgConfig.title = "商户图片 - 查看";
					dlgConfig.buttons = {
						"修改 > " : function() {
							$(this).dialog("close");
							//
							shopImageDialogEdit(dataId);
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
					$id("shopImageDialog").find(":input").prop("disabled", false);
				} else {
					$id("shopImageDialog").find(":input").prop("disabled", true);
				}
				//重置控件值
				textSet("imageTitle","");
				textSet("imageUuid","");
				textSet("imageUsage","");
				textSet("imagePath","");
				$("#shopImageSrc").attr("src", "");
				//
				if (curAction == "view" || curAction == "mod") {
					var data = imageGridHelper.getRowData(dataId);
					if (data != null) {
						textSet("shopImageId",dataId);
						textSet("imageTitle",data.title);
						textSet("imageUuid",data.imageUuid);
						textSet("imageUsage",data.imageUsage);
						textSet("imagePath",data.imagePath);
						$("#shopImageSrc").attr("src", data.fileBrowseUrl + "?" + new Date().getTime());
					}
				}
			}
				
				//初始化dialogAdd
				function initDialogAdd() {
					$("div[aria-describedby='shopNoticeDialog']").find("span[class='ui-dialog-title']").html("添加店铺公告");
					//
					shopNoticeDialog = $id("shopNoticeDialog").dialog({
						autoOpen : false,
						height : Math.min(380, $window.height()),
						width : Math.min(600, $window.width()),
						modal : true,
						buttons : {
							"保存" : defaultMethod,
							"取消" : function() {
								clearDialog();
								shopNoticeDialog.dialog("close");
							}
						},
						close : function() {
							clearDialog();
						}
					});
				}
				//初始化dialogView
				function initDialogView() {
					$("div[aria-describedby='shopNoticeDialog']").find("span[class='ui-dialog-title']").html("查看店铺公告");
					//
					shopNoticeDialog = $id("shopNoticeDialog").dialog({
						autoOpen : false,
						height : Math.min(400, $window.height()),
						width : Math.min(600, $window.width()),
						modal : true,
						buttons : {
							"继续添加" : shopNoticeDialogAdd,
							"修改 >" : defaultMethod,
							"关闭" : function() {
								clearDialog();
								shopNoticeDialog.dialog("close");
							}
						},
						close : function() {
							clearDialog();
						}
					});
				}
				//初始化dialogEdit
				function initDialogEdit() {
					$("div[aria-describedby='shopNoticeDialog']").find("span[class='ui-dialog-title']").html("修改店铺公告");
					//
					shopNoticeDialog = $id("shopNoticeDialog").dialog({
						autoOpen : false,
						height : Math.min(400, $window.height()),
						width : Math.min(600, $window.width()),
						modal : true,
						buttons : {
							"保存" : defaultMethod,
							"取消" : function() {
								clearDialog();
								shopNoticeDialog.dialog("close");
							}
						},
						close : function() {
							clearDialog();
						}
					});
				}
				
				//
				function defaultMethod() {
					if (curAction == "add") {
						saveShopNotice();
					}
					if (curAction == "edit") {
						updateShopNotice();
					}
					if (curAction == "view") {
						var id = tempShopNotice.id;
						tempShopNotice = noticeGridHelper.getRowData("id",id);
						shopNoticeDialogEdit(tempShopNotice);
					}
				}

				function shopNoticeDialogAdd() {
					clearDialog();
					curAction = "add";
					initDialogAdd();
					initDatePicker();
					$id("publishNoticeStatus").css("display", "none");
					$id("beginTime").css("display", "block");
					shopNoticeDialog.dialog("open");
				}

				//
				function shopNoticeDialogView(obj) {
					clearDialog();
					curAction = "view";
					tempShopNotice = obj;
					initDialogView();
					textSet("shopNoticeId",obj.id);
					radioSet("autoFlag",obj.autoFlag);
					textSet("pubTime",obj.pubTime);
					textSet("endTime",obj.endTime);
					textSet("title",obj.title);
					var status = obj.status;
					var statusText = statusTextMap.get(status);
					$id("status").html(statusText);
					$id("publishNotice").css("display", "none");
					textSet("content",obj.content);
					$("input", "#shopNoticeDialog").attr("disabled", true);
					$("select", "#shopNoticeDialog").attr("disabled", true);
					$("textarea", "#shopNoticeDialog").attr("disabled", true);
					shopNoticeDialog.dialog("open");
				}

				//
				function shopNoticeDialogEdit(obj) {
					clearDialog();
					tempShopNotice = obj;
					tempId = obj.id;
					curAction = "edit";
					initDialogEdit();
					textSet("shopNoticeId", tempId);
					var status = obj.status;
					var statusText = statusTextMap.get(status);
					$id("status").html(statusText);
					var publishNotice =statusBtnTextMap.get(status);
					$id("publishNotice").html(publishNotice);
					$id("publishNotice").unbind("click");
					if(status == 0 || status == 2){
						$id("publishNotice").click(function(){
							publishNotices(tempId);
						});
					} else {
						$id("publishNotice").click(function(){
							stopNotices(tempId);
						});
					}
					radioSet("autoFlag",obj.autoFlag);
					if(obj.autoFlag){
						$id("lblPublishTime").removeClass("required");
						$id("beginTime").css("display", "block");
					 }else{
						$id("lblPublishTime").addClass("required");
						$id("beginTime").css("display", "none");
					 }
					textSet("pubTime",obj.pubTime);
					textSet("endTime",obj.endTime);
					textSet("title",obj.title);
					textSet("content",obj.content);
					initDatePicker();
					shopNoticeDialog.dialog("open");
				}

				//
				function clearDialog() {
					curAction = null;
					tempShopNotice = null;
					tempId = null;
					noticeFormProxy.hideMessages();
					textSet("shopNoticeId","");
					radioSet("autoFlag",true);
					textSet("pubTime", null);
					textSet("endTime", null);
					textSet("title","");
					textSet("content","");
					$id("pubTime").datepicker("destroy");
					$id("endTime").datepicker("destroy");
					$id("publishNotice").css("display", "");
					$id("publishNoticeStatus").css("display", "");
					$("input", "#shopNoticeDialog").attr("disabled", false);
					$("select", "#shopNoticeDialog").attr("disabled", false);
					$("textarea", "#shopNoticeDialog").attr("disabled", false);
				}
				
				//
				function delShopNoticeByIds(){
					var ids = shopNoticeGrid.jqGrid("getGridParam", "selarrrow");
					var postData = [];
					for (var i = 0; i < ids.length; i++) {
						postData.add(ParseInt(ids[i]));
					}
					if (ids == "") {
						Toast.show("请选择要删除的数据！");
					} else {
						var theLayer = Layer.confirm('确定要删除选择的数据吗？', function() {
							//
							var hintBox = Layer.progress("正在删除...");
							var ajax = Ajax.post("/shop/notice/delete/batch/do");
							ajax.data(postData);
							ajax.done(function(result, jqXhr) {
								if (result.type == "info") {
									Layer.msgSuccess(result.message);
									//
									shopNoticeGrid.jqGrid("setGridParam").trigger("reloadGrid");
								} else {
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
				
				//
				function deleteShopNotice(id){
					var theLayer = Layer.confirm('确定要删除？', function() {
						//
						var hintBox = Layer.progress("正在删除...");
						var ajax = Ajax.post("/shop/notice/delete/do");
						ajax.params({
							id : id
						});
						ajax.done(function(result, jqXhr) {
							if (result.type == "info") {
								Layer.msgSuccess(result.message);
								//
								shopNoticeGrid.jqGrid("setGridParam").trigger("reloadGrid");
							} else {
								Layer.msgWarning(result.message);
							}
						});
						ajax.always(function() {
							hintBox.hide();
						});
						ajax.go();
					});
				}
				
				//
				function updateShopNotice(){
					var vldResult = noticeFormProxy.validateAll();
					if(vldResult){
						var ajax = Ajax.post("/shop/notice/update/do");
						var dataMap = {
								id : intGet("shopNoticeId"),
								title : textGet("title"),
								content : textGet("content"),
								autoFlag : radioGet("autoFlag") == true,
								pubTime : datetimeGet("pubTime"),
								endTime : datetimeGet("endTime")
						};
						ajax.data(dataMap);
						ajax.done(function(result, jqXhr) {
							Layer.msgSuccess(result.message);
							shopNoticeGrid.jqGrid("setGridParam").trigger("reloadGrid");
							tempShopNotice = result.data;
							shopNoticeDialogView(tempShopNotice);
						});
						ajax.go(); 
					}
				}
				
				//
				function publishNotices(id){
					var theLayer = Layer.confirm('确定要发布？', function() {
						//
						var hintBox = Layer.progress("正在发布...");
						var ajax = Ajax.post("/shop/notice/publish/do");
						ajax.data({
							"id" : id
						});
						ajax.done(function(result, jqXhr) {
							if (result.type == "info") {
								Layer.msgSuccess(result.message);
								//
								shopNoticeGrid.jqGrid("setGridParam").trigger("reloadGrid");
							} else {
								Layer.msgWarning(result.message);
							}
						});
						ajax.always(function() {
							hintBox.hide();
						});
						ajax.go();
						var status = "1";
						var statusText = statusTextMap.get(status);
						$id("status").html(statusText);
						var publishNotice =statusBtnTextMap.get("1");
						$id("publishNotice").html(publishNotice);
						$id("publishNotice").click(function(){
							stopNotices(tempId);
						});
					});
				}
				
				//
				function stopNotices(id){
					var theLayer = Layer.confirm('确定要停止？', function() {
						//
						var hintBox = Layer.progress("正在停止...");
						var ajax = Ajax.post("/shop/notice/stop/do");
						ajax.data({
							"id" : id
						});
						ajax.done(function(result, jqXhr) {
							if (result.type == "info") {
								Layer.msgSuccess(result.message);
								//
								shopNoticeGrid.jqGrid("setGridParam").trigger("reloadGrid");
							} else {
								Layer.msgWarning(result.message);
							}
						});
						ajax.always(function() {
							hintBox.hide();
						});
						ajax.go();
						var status = "2";
						var statusText = statusTextMap.get(status);
						$id("status").html(statusText);
						var publishNotice =statusBtnTextMap.get("2");
						$id("publishNotice").html(publishNotice);
						$id("publishNotice").click(function(){
							publishNotices(tempId);
						});
					});
				}
			 
		</script>
	</body>
	<!-- layTpl begin -->
<!-- 店铺相册模板 -->
<script id="shopImageTpl" type="text/html">
		<div class="form" id="shopImageTpl" >
			<input type="hidden" id="shopImageId" />
			<div class="field row">
				<label class="field label one wide">标题</label>
				<input class="field value two wide" type="text" id="imageTitle" />
			</div>
			<div class="field row">
				<label class="field label required one wide">店铺图片</label> 
				<input name="file" type="file" id="fileToUploadShopImage" multiple="multiple" class="field value one half wide"  /> 
				<button class="normal button" id="btnShopImageToUploadFile">上传</button>
			</div>
			<div class="field row" style="height: 80px">
				<input type="hidden"  id="imageUuid" />
				<input type="hidden"  id="imageUsage" />
				<input type="hidden"  id="imagePath" />
				<label class="field label one wide">图片预览</label>
		        <img id="shopImageSrc" height="80px" width="120px" src="<%=resBaseUrl%>/image-app/img-none.jpg"/>
			</div>
		</div>
</script>
<!-- 店铺公告模板 -->
<script id="shopNoticeTpl" type="text/html">
		<div class="form">
			<input type="hidden" id="shopNoticeId" />
			<div class="field row">
				<label class="field label one wide required">标题</label>
				<input class="field value two wide" type="text" id="title" />
			</div>
			<div class="field row" style="height: 82px;">
				<label class="field label one wide">内容</label>
				<textarea class="field value three wide" style="height:80px; resize:none;" id="content" ></textarea>
			</div>
			<div class="field row">
				<label class="field label one wide required">发布方式</label>
				<div class="field group">
					<input type="radio" id="noticeAutoFlagOn" name="autoFlag" value="1" />
					<label for="noticeAutoFlagOn">自动</label>
					<input type="radio" id="noticeAutoFlagOff" name="autoFlag" value="0" checked="checked"/>
					<label for="noticeAutoFlagOff">手动</label>
				</div>
			</div>
			<div class="field row" id="beginTime">
				<label id="lblPublishTime" class="field label one wide required">发布时间</label>
				<input class="one half wide field value" type="text" id="pubTime" readonly="true"/>
			</div>
			<div class="field row">
				<label id="lblEndTime" class="field label one wide required">结束时间</label>
				<input class="one half wide field value" type="text" id="endTime" readonly="true"/>
			</div>
			<div class="field row" style="float:left;" id="publishNoticeStatus">
				<label class="field label one wide">状态</label>
				<div style="height:120px;vertical-align: middle;float:left;">
				<label id='status' class='normal label' ></label><span class="normal spacer"></span>
				</div>
				<button class="normal button" id="publishNotice" style="margin-top: 4px;">
				</button>
			</div>
			
		</div>
</script>
<!-- 短信设置模板  -->
<script id="smsSettingTpl" type="text/html">
		<div class="form">
			<div class="field row">
				<label class="field label three wide required">商家发货时是否给顾客发短信</label>
				<div class="field group">
					<input type="radio" id="sendOne" name="smsSendToCustomerOnMerchantDispatching" value="1"/>
					<label for="sendOne">是</label>
					<input type="radio" id="notSendOne" name="smsSendToCustomerOnMerchantDispatching" value="0" checked="checked"/>
					<label for="notSendOne">否</label>
				</div>
			</div>
			<div class="field row">
				<label class="field label three wide required">顾客下订单时是否给商家发短信</label>
				<div class="field group">
				<div class="field group">
					<input type="radio" id="sendTwo" name="smsSendToMerchantOnCustomerCreatingOrder" value="1"/>
					<label for="sendTwo">是</label>
					<input type="radio" id="notSendTwo" name="smsSendToMerchantOnCustomerCreatingOrder" value="0" checked="checked"/>
					<label for="notSendTwo">否</label>
				</div>
				</div>
			</div>
			<div class="field row">
				<label class="field label three wide required">商家做店内活动时是否给顾客发短信</label>
				<div class="field group">
				<div class="field group">
					<input type="radio" id="sendThree" name="smsSendToCustomerOnMerchantActivity" value="1"/>
					<label for="sendThree">是</label>
					<input type="radio" id="notSendThree" name="smsSendToCustomerOnMerchantActivity" value="0" checked="checked"/>
					<label for="notSendThree">否</label>
				</div>
				</div>
			</div>
			<div class="field row align left">
				<button class="normal button" id="btnSmsSave">保存</button>
			</div>
		</div>
</script>
<!-- layTpl end -->
</html>