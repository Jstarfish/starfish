<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>供应商管理</title>
</head>

<body id="rootPanel">
	
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<button id="btnToAdd" class="button">添加</button>
				<div class="group right aligned">
					<label class="label">供应商名称</label> <input id="queryName" class="input" /> 
					<span class="spacer"></span>
					<button id="btnToQry" class="button">查询</button>
				</div>
			</div>
		</div>
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	
	<div id="dialog" style="display: none;">
		<div id="addForm" class="form">
			<!-- <div class="field row">
				<label class="field label required">昵称</label> 
				<input type="hidden" id="vendorId" class="field value" />
				<input type="text" id="nickName" class="field value one half wide" />
				<label class="field inline label required one half wide">手机号码</label> 
				<input type="text" id="phoneNo" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label required">真实姓名</label> 
				<input type="text" id="realName" class="field value one half wide" />
				<label class="field inline label required one half wide">身份证号</label> 
				<input type="text" id="idCertNo" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label required">邮箱</label>
				<input type="text" id="email" class="field value three wide" />
			</div>
			<span id="acctDivider" class="normal hr divider"></span> -->
				<div style="margin: 10px 0;">
					<label style="font-size: 14px;color: #666;">管理员信息</label>
				</div>
				<div class="field row">
					<label class="field label required one half wide">管理员昵称</label> 
					<input type="hidden" id="vendorId" class="field value" />
					<input type="text" id="nickName" class="field value one half wide" />
					<span class="normal spacer"></span> 
					<label class="field label required inline one half wide">手机号码</label> 
					<input type="text" id="phoneNo" class="field value one half wide" />
				</div>
				<span id="divider" class="normal hr divider"></span>
				<div style="margin: 10px 0;">
					<label style="font-size: 14px;color: #666;">供应商信息</label>
				</div>
				<div class="field row">
					<label class="field label required one half wide">供应商名</label> 
					<input type="text" id="name" class="field one half value wide" /> 
				</div>
				<div class="field row" style="display: none">
					<label class="field label required inline one half wide">联系电话</label> 
					<input type="text" id="linkPhoneNo" class="field one half value wide" />
				</div>
				<!-- <div class="field row">
					<label class="field inline label wide required">企业供应商</label>
					<div class="field group" style="width: 170px;">
						<input id="entpFlag-Y" type="radio" name="entpFlag" value="true" /> 
						<label for="entpFlag-Y">是</label>
						<input id="entpFlag-N" type="radio" name="entpFlag" value="false" checked="checked" /> 
						<label for="entpFlag-N">否</label>
					</div>
					<span class="normal spacer"></span>
					<div id="license" class="field group"> 
						<label class="field label">营业执照</label> 
						<input id="licenseId" type="text" class="field one half value wide" />
					</div>
				</div> -->
				<div class="field row">
					<label class="field  label required one half wide">联系人</label> 
					<input type="text" id="linkMan" class="field one half value wide" /> 
					<span class="normal spacer"></span> 
					<label class="field inline label one half wide">固定电话</label>
					<input type="text" id="telNo" class="field one half value wide" />
				</div>
				<div class="field row" id="addHide" style="display: none">
					<label class="field label one half wide">供应商编号</label> 
					<input type="text" id="code" class="field one half value wide" /> 
					<input type="hidden" id="py" /> 
					<span class="normal spacer"></span>
					<label class="field inline label one half wide" >状态</label> 
					<select id="vendorStatus" class="field one half value wide">
						<option value="0">启用</option>
						<option value="1">禁用</option>
					</select>
				</div>
				<div class="field row">
					<label class="field label required  one half wide">所在地</label> 
					<select class="field value" id="province"></select> 
					<select class="field value" id="city"></select> 
					<select class="field value" id="county"></select> 
					<select class="field value" id="town" style="display: none;"></select> 
					<span class="normal spacer"></span> 
					<span class="normal spacer"></span>
					<span class="normal spacer"></span>
				</div>
				<div class="field row">
					<label class="field  label required one half wide">街道地址</label> 
					<input type="text" id="street" class="field three value wide" /> 
					<span class="normal spacer three value wide"></span> 
				</div>
				<div class="field row" style="height: 60px;">
					<label class="field label required one half wide">经营范围</label>
					<textarea id="bizScope" class="field three value wide " style="height: 50px;"></textarea>
					<span class="normal spacer"></span>
				</div>
				<div class="field row" style="height: 60px;">
					<label class="field label one half wide">备注</label>
					<textarea id="memo" class="field three value wide " style="height: 50px;"></textarea>
					<span class="normal spacer"></span>
				</div>
				
				<div class="field row" id="imgFieldRow">
					<label class="field  label one half wide">上传LOGO</label> 
					<input name="file" type="file" id="fileToUploadFileLogo" multiple="multiple" class="field value one half wide" />
					<button class="normal button" id="btnfileToUploadFile">上传</button>
				</div>
				<div class="field row" style="height: 90px">
					<input type="hidden"  id="logoUuid" name="logoUuid" />
					<input type="hidden"  id="logoUsage" name="logoUsage"  />
					<input type="hidden"  id="logoPath" name="logoPath"  />
					<label class="field label one half wide">LOGO</label> 
					<img id="logoVendor" height="80px" width="120px" /> 
				</div>
				
			</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js"></script>
	<script>
	//-------------------------------------------自定义方法------------------------------{{
	var oldName = "";// 保留原名称
	var isExist = false;// 是否存在
	/**
	 * 检测是否存在相同的名称
	 * 
	 * @author WJJ
	 * @date 2015年10月13日
	 * 
	 * @param name
	 */
	function isExistByName(name) {
		if (name == oldName) {// 验证是否跟原名称一样，如果一样则跳过验证
			isExist = false;
		} else {
			var ajax = Ajax.post("/vendor/exist/by/name");
			ajax.data({
				name : name
			});
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				isExist = result.data;
			});
			ajax.go();
		}
	}
	//上传
	var isChangeImgData=false;
	function fileToUploadFileIcon(fileId) {
		var fileUuidToUpdate = $id("logoUuid").val();
		if (isNoB(fileUuidToUpdate)) {
			var formData = {
				usage : "image.logo",
				subUsage : "vendor"
			};
		} else {
			var formData = {
				update : true,
				uuid : fileUuidToUpdate
			};
		}
		//
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
					//$("#logoImgagency").makeUniqueRequest("");
					$("#logoVendor").attr("src",resultInfo.data.files[0].fileBrowseUrl + "?" + new Date().getTime());
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
	// -------------------------------------------------------------------------------------}}
	
	$id("mainPanel").jgridDialog(
			{
				dlgTitle : "供应商列表",
				addUrl : "/vendor/add/do",
				updUrl : "/vendor/update/do",
				delUrl : "/vendor/delete/do",
				viewUrl : "/vendor/get",
				isUsePageCacheToView : true,
				jqGridGlobalSetting:{
					url : getAppUrl("/vendor/list/get"),
					colNames : [  "供应商名称", "联系人", "手机号","状态", "操作" ],
					colModel : [
							{
								name : "name",
								index : "name",
								width : 200,
								align : 'left'
							},
							{
								name : "linkMan",
								index : "linkMan",
								width : 100,
								align : 'center'
							},
							{
								name : "phoneNo",
								index : "phoneNo",
								width : 100,
								align : 'center'
							},
							{
								name : "disabled",
								index : "disabled",
								width : 200,
								align : "left",
								formatter : function(cellValue,option, rowObject) {
									if (cellValue == 0) {
										return '启用';
									}else{
										return "禁用";
									}
								}
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									var s = " <span>[<a class='item dlgview' id='dlgview' href='javascript:void(0);' cellValue='" + cellValue + "' >查看</a>]"
											+ "&nbsp;&nbsp;&nbsp;[<a class='item dlgupd' id='dlgupd' href='javascript:void(0);' cellValue='" + cellValue + "' >修改</a>]" 
											+ "&nbsp;&nbsp;&nbsp;[<a class='item dlgdel' href='javascript:void(0);' cellValue='" + cellValue + "'>删除</a>]</span> ";
									return s;
								},
								width : 200,
								align : "center"
							} ]
				},
				// 增加对话框
				addDlgConfig : {
					width : Math.min(800, $window.width()),
					height : Math.min(600, $window.height()),
					modal : true,
					open : false
				},
				// 修改和查看对话框
				modAndViewDlgConfig : {
					width : Math.min(800, $window.width()),
					height : Math.min(600, $window.height()),
					modal : true,
					open : false
				},
				modElementToggle : function(isShow) {
					if (isShow) {
						$id("town").show();
					} else {
						$id("town").hide();
					}
				},
				
				queryParam : function(postData,formProxyQuery) {
					var name = formProxyQuery.getValue("queryName");
					if (name != "") {
						postData['name'] = name;
					}
				},
				savePostDataChange : function(postData){
					postData["code"] = textGet("code");
					postData["py"] = textGet("py");
					postData["phoneNo"] = textGet("linkPhoneNo");
					
					//设置最后一级地区id
					var townId = $id("town").val();
					if (isNoB(townId)) {
						var regionId = $id("county").val();
					} else {
						var regionId = townId;
					}
					/* //判断是否企业供应商
					var entpValue = $('input[name="entpFlag"]:checked ').val();
					  if(entpValue=="true"){
						 var entpFlag = true;
					  }else{
						 var entpFlag = false;  
					  } */
					  
					  var status = intGet("vendorStatus");
					  if(status == "0"){
						  var disabled = false;
					  }else{
						  var disabled = true;
					  }
					  
					  postData["regionName"]= $("#province option:selected").text() + " "
						+ $("#city option:selected").text() + " "
						+ $("#county option:selected").text() + " "
						+ $("#town option:selected").text();
					  postData["regionId"] = regionId,
					  postData["entpFlag"] = true,
					  postData["disabled"] = disabled
					  
					  var user = {
							  nickName : textGet("nickName"),
							  phoneNo : textGet("phoneNo"),
					  }
					  postData["user"] = user
					  
					  if(isChangeImgData){
						  postData["logoUuid"]=textGet("logoUuid");
						  postData["logoUsage"]=textGet("logoUsage");
						  postData["logoPath"]=textGet("logoPath");
						}
				},
				formProxyInit : function(formProxy) {
					/* //绑定radio事件
					$("input[type='radio'][name='entpFlag']").click(function(){
						showLicense($(this).val() == "true",formProxy);
					}); */ 
					
					/* formProxy.addField({
						id : "licenseId",
						key : "licenseId",
						type : "int",
						//required : true,
						rules : [ "maxLength[11]"]
					}); */
					
					// 注册表单控件
					formProxy.addField({
						id : "name",
						key : "name",
						required : true,
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								isExistByName(rawValue);
								return !isExist;
							},
							message : "名称已存在!"
						} ]
					});
					formProxy.addField({
						id : "name",
						key : "name",
						required : true,
						rules : ["maxLength[30]"]
					});
					
					formProxy.addField({
						id : "nickName",
						key : "user.nickName",
						required : true,
						rules : ["maxLength[30]"]
					});
					formProxy.addField({
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
					
					/*formProxy.addField({
						id : "realName",
						key : "user.realName",
						required : true,
						rules : ["maxLength[30]"]
					});
					formProxy.addField({
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
					formProxy.addField({
						id : "email",
						key : "user.email",
						required : true,
						rules : ["isEmail", {
							rule : function(idOrName, type, rawValue, curData) {
								validateEmail(rawValue);
								return !emailFlag;
							},
							message : "邮箱已被注册！"
						}]
					}); */
					
					formProxy.addField({
						id : "linkPhoneNo",
						key : "phoneNo",
						//required : true,
						rules : [ "maxLength[11]", "isMobile" ]
					});
					formProxy.addField({
						id : "linkMan",
						key : "linkMan",
						required : true,
						rules : [ "maxLength[30]" ]
					});
					formProxy.addField({
						id : "telNo",
						key : "telNo",
						rules : [ "maxLength[15]", "isTel" ]
					});
					formProxy.addField({
						id : "street",
						key : "street",
						required : true,
						rules : [ "maxLength[60]" ]
					});
					formProxy.addField({
						id : "bizScope",
						key : "bizScope",
						required : true,
						rules : [ "maxLength[1000]" ]
					});
					formProxy.addField({
						id : "memo",
						key : "memo",
						rules : [ "maxLength[250]" ]
					});
					
					formProxy.addField({
						id : "province",
						required : true
					});
					formProxy.addField({
						id : "city",
						required : true
					});
					formProxy.addField({
						id : "county",
						required : true
					});
					
					formProxy.addField({
						id : "code",
						key : "code"
					});
					formProxy.addField({
						id : "py",
						key : "py"
					});
					
					formProxy.addField({
						id : "town",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 地区  若显示且为空，给予提示
								if ($("#town").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "此为必须提供项！"
						} ]
					});
					
				},
				 addInit:function (formProxy) {
					 $id("vendorId").val("");
					 
						textSet("nickName", "");
						textSet("phoneNo", "");
						/* textSet("realName", "");
						textSet("idCertNo", "");
						textSet("email", ""); */
						
						textSet("name", "");
						textSet("linkPhoneNo", "");
						//radioSet("entpFlag", "false");
						textSet("linkMan", "");
						textSet("telNo", "");
						
						$("#province").val("");
						$("#city").val("-1");
						$("#county").val("-1");
						
						textSet("street", "");
						textSet("bizScope", "");
						textSet("memo", "");
						//上传
						textSet("logoUuid", "");
						textSet("logoUsage", "");
						textSet("logoPath", "");
						$("#logoVendor").attr("src",null);
						
						$id("addHide").hide();
						//$id("license").hide();
						//showLicense(false,formProxy);
					},
					 modAndViewInit:function (data,formProxy) {
						 if (data.regionId != null) {
							 initArea(data.regionId);		
						 } 
						 $id("addHide").show();
						 
						 textSet("nickName", data.user.nickName);
						 textSet("phoneNo", data.user.phoneNo);
						 $("#nickName").attr("disabled", true);
						 $("#phoneNo").attr("disabled", true);
						 /*textSet("realName", data.user.realName);
						 textSet("idCertNo", data.user.idCertNo);
						 textSet("email", data.user.email); */
						 
						 textSet("name", data.name);
						 textSet("linkPhoneNo", data.phoneNo);
						 textSet("linkMan", data.linkMan);
						 textSet("telNo", data.telNo);
						 textSet("street", data.street);
						 textSet("bizScope", data.bizScope);
						 textSet("py", data.py);
						 textSet("memo", data.memo);
						 
						 textSet("code", data.code);
						 $("#code").attr("disabled", true);
						 
						  if(data.disabled == "0"){
							  intSet("vendorStatus", 0);
						  }else{
							  intSet("vendorStatus", 1);
						  }

						 textSet("logoUuid", data.logoUuid);
						 textSet("logoUsage", data.logoUsage);
						 textSet("logoPath", data.logoPath);
						 $("#logoVendor").attr("src",data.fileBrowseUrl + "?" + new Date().getTime());
						 /* radioSet("entpFlag", data.entpFlag.toString());
						 if(data.entpFlag.toString() == "true"){
							 showLicense(true,formProxy);
							 textSet("licenseId", data.licenseId);
						 }else{
							 showLicense(false,formProxy);
							 textSet("licenseId", "");
						 } */
					},
				formProxyQueryInit : function(formProxyQuery) {
					// 注册查询表单控件
					formProxyQuery.addField({
						id : "queryName",
						rules : [ "maxLength[30]" ]
					});
				},
				saveOldData : function(data) {
					oldName = data.name;// 保存原来名称，用于检测是否存在
				},
				getDelComfirmTip : function(data) {
					var theSubject = data.name;
					return '确定要删除"' + theSubject + '"吗？';
				},
				pageLoad : function() {
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
					//
					$("#province").empty().show();
					// 加载省列表
					loadProvince();
					var size = getImageSizeDef("image.logo");
					$id("logoVendor").attr("width", size.width);
					$id("logoVendor").attr("height", size.height);
					
					//上传
					initFileUpload("fileToUploadFileLogo");
					//绑定修改模块上传按钮
					$id("btnfileToUploadFile").click(function(){
						isChangeImgData=true;
						fileToUploadFileIcon("fileToUploadFileLogo");
					});
					
					$id("phoneNo").blur(function() {
						checkPhoneNo(phoneNo);
						textSet("linkPhoneNo", textGet("phoneNo"));
						
					});
				}
			});
	
			//WJJ
			/* function showLicense(isShow,formProxy) {
				if (isShow) {
					$id("license").show();
					
					formProxy.addField({
						id : "licenseId",
						key : "vendor.licenseId",
						type : "int",
						required : true,
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 若显示且为空，给予提示
								if ($("#licenseId").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "此为必须提供项！"
						} ]
					});
					
				} else {
					formProxy.removeField("licenseId")
					$id("license").hide();
				}
			} */
			//检验手机号码是否唯一
			var phoneFlag = false;
			var vendorObj = "";
			function checkPhoneNo(phoneNo) {
				var id = $id("vendorId").val();
				var addFlag = (id == null || id == "");
				var updateFlag = (id != null && id != "" && vendorObj && vendorObj.user && phoneNo != vendorObj.user.phoneNo);
				if(addFlag || updateFlag){
					var postData = {
							phoneNo : phoneNo	
					};
					var ajax = Ajax.post("/vendor/exist/by/phoneNo");
					ajax.data(postData);
					ajax.sync();
					ajax.done(function(result, jqXhr) {
							var data = result.data
							if(data && data.userFlag){
								if(data.vendorFlag){
									phoneFlag = true;
									
									var layer = Layer.confirm("用户信息已存在，且已经是供应商，无需重复添加", function() {
										$id("phoneNo").val("");
										hideMiscTip("phoneNo");
										layer.hide();
									}, function() {
										$id("phoneNo").val("");
										hideMiscTip("phoneNo");
										layer.hide();
									});
								}else{
									phoneFlag = true;
									//获取用户信息
									var layer = Layer.confirm("用户信息已存在，确定要继续添加供应商吗？", function() {
										var user = data.user;
										$id("vendorId").val(user.id);
										phoneFlag = false;
										vendorObj.user = user;
										
										hideMiscTip("nickName");
										hideMiscTip("phoneNo");
										
										textSet("nickName",user.nickName);
										textSet("phoneNo",user.phoneNo);
										
										$id("nickName").attr("disabled",true);
										$id("phoneNo").attr("disabled",true);
										layer.hide();
									}, function() {
										phoneFlag = true;
										$id("vendorId").val("");
										layer.hide();
									});
								}
							}
					});
					ajax.fail(function() {
						phoneFlag = false;
					});
					ajax.go();
				}
			}
	//-----------------------------------------------------------------------------------}}
	
	//--------begin-----------------------加载省市县------------------------------
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
								$("#province option[value='" + arr[0] + "']")
										.attr("selected", true);
								loadCityByParentId(arr[0], arr[1]);
								loadCountyByCityId(arr[1], sonId);
							} else {
								$("#province option[value='" + arr[0] + "']")
										.attr("selected", true);
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
					$id("county").val("");
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
					$("#city option[value='" + cityId + "']").attr("selected",
							true);
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
					$("#county option[value='" + countyId + "']").attr(
							"selected", true);
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
						$("#town option[value='" + townId + "']").attr(
								"selected", true);
					}
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
					for (var i = 0, len = data.items.length; i < len; i++) {
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
		// 隐藏信息区下拉框及错误提示
		function hideTown() {
			$("#town").hide().empty();
			hideMiscTip("town");
		}
		//--------end-----------------------加载省市县------------------------------
	</script>
</body>
</html>