<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>车辆服务管理</title>

<style type="text/css">
</style>
</head>
<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnToAdd" class="button">添加</button>
				</div>
				<div class="group right aligned">
					<label class="label">服务名称</label> <input id="queryName"
						class="input" /> <span class="spacer"></span>
					<button id="btnToQry" class="button">查询</button>
				</div>
			</div>
		</div>
	</div>
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	<div id="dialog" style="display: none;">
		<div id="addForm" class="form">
			<div class="field row">
				<label class="field label one half wide required">服务名称</label> 
				<input type="text" id="name" name="name" class="field value one half wide" /> 
			</div>
			<div class="field row">
				<label class="field label one half wide required">所属分组</label> 
				<select class="field value  one half wide" id="groupId" name="groupId">
				<option value="" title="- 请选择 -">- 请选择 -</option>
				</select>
			</div>
			<div class="field row">	
				<label class="field label one half wide required">销售价格</label> 
				<input type="text" id="salePrice" name="salePrice" class="field value one half wide" /> 
			</div>
			<div id="isShowDefaulted" class="field row"  style="width: 300px;display: none;">
				<label class="field label one half wide required">是否上架</label> 
				<div class="field group">
					<input type="radio" id="defaulted-Y" name="disabled" value="false" checked="checked"> 
					<label for="defaulted-Y">上架</label> 
					<input type="radio" id="defaulted-N" name="disabled" value="true">
					<label for="defaulted-N">下架</label>
				</div>
			</div>
			<div class="field row" style="height: 70px;">	
				<label class="field label one half wide" >服务描述</label>
				<textarea class="field value one half wide" style="height: 60px;width:350px; resize: none;" name="desc" id="desc"></textarea>
				<span class="normal spacer"></span>
			</div>
			<div class="field row" style="height: 70px;">	
				<label class="field label one half wide">使用范围</label>
				<textarea class="field value one half wide" style="height: 60px; width:350px;resize: none;" name="appRange" id="appRange"></textarea>
			</div>
			<div class="field row" id="imgFieldRow">
				<label class="field label one half wide">服务图片</label> 
				<input name="file" type="file" id="fileToUploadFileLogo" multiple="multiple" class="field value one half wide" />
				<button class="normal button" id="btnfileToUploadFile">上传</button>
			</div>
			<div class="field row" style="height: 60px">
				<input type="hidden"  id="imageUuid" name="imageUuid" />
				<input type="hidden"  id="imageUsage" name="imageUsage"  />
				<input type="hidden"  id="imagePath" name="imagePath"  />
				<label class="field label one half wide">LOGO</label> 
				<img id="logoCarSvc" height="80px" width="120px" /> 
			</div>
			<div class="field row" id="imgFieldRowTwo">
				<label class="field label one half wide">pc有色图标</label> 
				<input name="file" type="file" id="fileToUploadFileLogoTwo" multiple="multiple" class="field value one half wide" />
				<button class="normal button" id="btnfileToUploadFileTwo">上传</button>
			</div>
			<div class="field row" style="height: 60px">
				<input type="hidden"  id="iconUuid2" name="iconUuid2" />
				<input type="hidden"  id="iconPath2" name="iconPath2"  />
				<input type="hidden"  id="iconUsage2" name="iconUsage2"  />
				<label class="field label one half wide">LOGO</label> 
				<img id="logoCarSvcTwo" height="40px" width="100px" /> 
			</div>
			
			<div class="field row" id="imgFieldRowIcon">
				<label class="field label one half wide">pc无色图标</label> 
				<input name="file" type="file" id="fileToUploadFileLogoIcon" multiple="multiple" class="field value one half wide" />
				<button class="normal button" id="btnfileToUploadFileIcon">上传</button>
			</div>
			<div class="field row" style="height: 60px">
				<input type="hidden"  id="iconUuid" name="iconUuid" />
				<input type="hidden"  id="iconPath" name="iconPath"  />
				<input type="hidden"  id="iconUsage" name="iconUsage"  />
				<label class="field label one half wide">LOGO</label> 
				<img id="logoCarSvcIcon" height="40px" width="100px" /> 
			</div>
			
			<div class="field row" id="imgFieldRowApp">
				<label class="field label one half wide">app图标</label> 
				<input name="file" type="file" id="fileToUploadFileLogoApp" multiple="multiple" class="field value one half wide" />
				<button class="normal button" id="btnfileToUploadFileApp">上传</button>
			</div>
			<div class="field row" style="height: 60px">
				<input type="hidden"  id="iconUuidApp" name="iconUuidApp" />
				<input type="hidden"  id="iconPathApp" name="iconPathApp"  />
				<input type="hidden"  id="iconUsageApp" name="iconUsageApp"  />
				<label class="field label one half wide">LOGO</label> 
				<img id="logoCarSvcApp" height="40px" width="100px" /> 
			</div>
		</div>
	</div>
	
	<div id="userRateSetDialog" style="display: none;">
		<div style="margin: 10px 0;">
			<label style="font-size: 14px;color: #666;">服务信息</label>
		</div>
		<div class="form">
			<input type="hidden" id="svcId"/>
			<input type="hidden" id="svcKindId"/>
			<div class="field row">
				<label class="field label one half wide">服务名称：</label> 
				<span id="svcName"></span>
			</div>
			<div class="field row">
				<label class="field label one half wide">服务图片：</label> 
				<img id="showlogoCarSvcIcon" height="40px" width="100px" />
			</div>
			<div class="field row">
				<label class="field label one half wide">附加价：</label> 
				<span id="showSalePrice"></span>元
			</div>
		</div>
		<table id="detailedList" width="90%" class="cleanTbl">
			<thead class="head">
				<tr class="row">
					<th>会员等级</th>
					<th>服务折扣</th>
				</tr>
			</thead>
			<tbody class="body" id="showUserRateSet">
			</tbody>
		</table>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js"></script>
	<script>
		//------------------------------------------初始化配置---------------------------------
		var oldName = ""; // 保留原名称
		var oldKindId = ""; //保留原kindId;
		var oldGroupId = "";
		var isExist = false;// 是否存在
		function isExistByName(name,groupId) {
			if (oldKindId!="" && name == oldName && oldGroupId == groupId) {// 验证是否跟原名称一样，如果一样则跳过验证
				isExist = false;
			} else {
				var ajax = Ajax.post("/carSvc/svc/exist/by/name");
				ajax.data({
					name : name,
					groupId : groupId
				});
				ajax.sync();
				ajax.done(function(result, jqXhr) {
					isExist = result.data;
				});
				ajax.go();
			}
		}
		var userRateList;
		var userRateSetDialog;
		//初始化用户服务折扣
		initShowUserRateSetDialog();
		//初始化查看页面
		function initShowUserRateSetDialog() {
			userRateSetDialog = $("#userRateSetDialog").dialog({
				autoOpen : false,
				height : Math.min(500, $(window).height()),
				width : Math.min(500, $(window).width()),
				modal : true,
				title : '设置会员等级折扣',
				buttons : {
					"确定" : function(){
						goSaveOrUpdateUserRateSet();
		            },
					"关闭" : function() {
						userRateSetDialog.dialog("close");
					}
				},
				close : function() {
				}
			});
		}
		
		function goSaveOrUpdateUserRateSet(){
			
			var svcx = {};
			svcx["id"] = textGet("svcId");
			svcx["name"] = $id("svcName").text();
			svcx["kindId"] = textGet("svcKindId");
			//
			var svcxRankDiscs = new Array();
			
			for(var i = 0; i < userRateList.length; i++){
				//
				var svcxRankDisc = {};
				svcxRankDisc["rank"] = userRateList[i].rank;
				svcxRankDisc["shopId"] = -1;
				var rate = textGet("rate" + userRateList[i].rank);
				if (!isNum(rate*10) || rate.length > 6 || ( rate < 0 || rate > 10)) {
					Layer.warning("请您正确输入折扣");
					return;
				}
				svcxRankDisc["rate"] = rate/10;
				svcxRankDiscs.push(svcxRankDisc);
			}
			svcx["svcxRankDiscs"] = svcxRankDiscs;
			var ajax = Ajax.post("/ecard/svcRankDisc/save/do");
			ajax.data(svcx);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					userRateSetDialog.dialog("close");
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		
		//------------------------------------------初始化配置---------------------------------{{
		function loadRadio(disabled) {
			if(typeof (groupId) != "undefined" && groupId != null){
				
			}
		}
		function loadGroup(selectedId, groupId) {
			if (typeof (groupId) != "undefined" && groupId != null) {
				$id(selectedId).val(groupId);
			}
		}
		function loadCarSvcGroup() {
		// 隐藏页面区
		//hideTown();
		var ajax = Ajax.post("/carSvc/svcGroup/selectList/get");
		ajax.params({});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData("groupId", result.data);
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
//-----------------------------------------------图片上传--------------------------------------------------
	//上传
	function fileToUploadFileIcon(fileId) {
		var fileUuidToUpdate = $id("imageUuid").val();
		if (isNoB(fileUuidToUpdate)) {
			var formData = {
				usage : "image.svc",
				subUsage : "poster"
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
					$id("imageUuid").val(resultInfo.data.files[0].fileUuid);
					$id("imageUsage").val(resultInfo.data.files[0].fileUsage);
					$id("imagePath").val(resultInfo.data.files[0].fileRelPath);
					//$("#logoImgagency").makeUniqueRequest("");
					$("#logoCarSvc").attr("src",resultInfo.data.files[0].fileBrowseUrl + "?" + new Date().getTime());
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
	//-----------------------------------------------pc有色图标上传--------------------------------------------------
	//上传
	function fileToUploadFileIconTwo(fileId) {
		var fileUuidToUpdate = $id("iconUuid2").val();
		if (isNoB(fileUuidToUpdate)) {
			var formData = {
				usage : "image.svc",
				subUsage : "icon"
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
					$id("iconUuid2").val(resultInfo.data.files[0].fileUuid);
					$id("iconUsage2").val(resultInfo.data.files[0].fileUsage);
					$id("iconPath2").val(resultInfo.data.files[0].fileRelPath);
					//$("#logoImgagency").makeUniqueRequest("");
					$("#logoCarSvcTwo").attr("src",resultInfo.data.files[0].fileBrowseUrl + "?" + new Date().getTime());
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
	//--------------------------------------------------------app上传-----------------------------------------------
		//上传
	function fileToUploadFileIconApp(fileId) {
		var fileUuidToUpdate = $id("iconUuidApp").val();
		if (isNoB(fileUuidToUpdate)) {
			var formData = {
					usage : "image.svc",
					subUsage : "icon"
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
					$id("iconUuidApp").val(resultInfo.data.files[0].fileUuid);
					$id("iconUsageApp").val(resultInfo.data.files[0].fileUsage);
					$id("iconPathApp").val(resultInfo.data.files[0].fileRelPath);
					//$("#logoImgagency").makeUniqueRequest("");
					$("#logoCarSvcApp").attr("src",resultInfo.data.files[0].fileBrowseUrl + "?" + new Date().getTime());
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
	//--------------------------------------------------设置促销标签-----------------------------------------------------------
	function openShowPrmtTagDlg(svcId){
		//对话框参数名
		var argName = "svcId";
		//对话框参数 预存
		setDlgPageArg(argName, svcId);
		//对话框信息
		var pageTitle = "选择促销标签";
		var pageUrl = "/market/prmtTag/list/for/svc/jsp";
		var extParams = {};
		//
		pageUrl = makeDlgPageUrl(pageUrl, argName, extParams);
		var theDlg = Layer.dialog({
			title : pageTitle,
			src : pageUrl,
			area : [ '100%', '90%' ],
			closeBtn : true,
			maxmin : true, //最大化、最小化
			btn : ["关闭"]
		});
	}
	//--------------------------------------------------------无色上传-----------------------------------------------
	//上传
	function fileToUploadFileIconAd(fileId) {
		var fileUuidToUpdate = $id("iconUuid").val();
		if (isNoB(fileUuidToUpdate)) {
			var formData = {
					usage : "image.svc",
					subUsage : "icon"
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
					$id("iconUuid").val(resultInfo.data.files[0].fileUuid);
					$id("iconUsage").val(resultInfo.data.files[0].fileUsage);
					$id("iconPath").val(resultInfo.data.files[0].fileRelPath);
					//$("#logoImgagency").makeUniqueRequest("");
					$("#logoCarSvcIcon").attr("src",resultInfo.data.files[0].fileBrowseUrl + "?" + new Date().getTime());
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
		$id("mainPanel").jgridDialog(
				{
					dlgTitle : "车辆服务",
					//listUrl : "/goods/carSvc/list/get",
					addUrl : "/carSvc/add/do",
					updUrl : "/carSvc/update/do",
					delUrl : "/carSvc/delete/do",
					rootPanelSetting:{
						north__size : 60
					},
				jqGridGlobalSetting : {
					url : getAppUrl("/carSvc/list/get"),
					colNames : ["服务名称", "服务描述","附加价","所属分组", "是否上架 ", " 操作" ],
					colModel : [
							{
								name : "name",
								index : "name",
								width : 100,
								align : 'left'
							},
							{
								name : "desc",
								index : "desc",
								width : 300,
								align : 'left'
							},
							{
								name : "salePrice",
								index : "salePrice",
								width : 50,
								align : 'left'
							},
							{
								name : "carSvcGroup.name",
								index : "carSvcGroup.name",
								width : 100,
								align : 'center'
							},
							{
								name : "disabled",
								index : "disabled",
								width : 50,
								align : "left",
								formatter : function(cellValue) {
									return cellValue == true ? '<span style="color:red;">已下架</span>' : '<span style="color:green;">已上架</span>';
								}
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									var disablied = rowObject.disabled == true ? '上架': '下架';
									var tempItem = String.builder();
									tempItem.append("[<a class='item dlgview' href='javascript:void(0);' cellValue='"+ cellValue+ "' >查看</a>]");
									tempItem.append("&nbsp;&nbsp;[<a class='item updateDisabled' href='javascript:void(0);' cellValue='"+ cellValue+ "' >"+ disablied+ "</a>]");
									if (rowObject.disabled == true) {
										tempItem.append("&nbsp;&nbsp;[<a class='item dlgupd' href='javascript:void(0);' cellValue='" + cellValue + "' >修改</a>]");
										tempItem.append("&nbsp;&nbsp;[<a class='item dlgdel' href='javascript:void(0);' cellValue='" + cellValue + "' >删除</a>]");
										tempItem.append("&nbsp;&nbsp;[<a class='item' href='javascript:;' onclick='openShowPrmtTagDlg("+ cellValue+ ")' >设置促销标签</a>]");
										tempItem.append("&nbsp;&nbsp;[<a class='item dlgRateSet' href='javascript:void(0);' cellValue='" + cellValue + "' >设置身份折扣</a>]");
									}
									return tempItem.value;
								},
								width : 250,
								align : "center"
							} ]
				},
					// 增加对话框
					addDlgConfig : {
						width : Math.min(600, $window.width()),
						height : Math.min(550, $window.height()),
						modal : true,
						open : false
					},
					// 修改和查看对话框
					modAndViewDlgConfig : {
						width : Math.min(600, $window.width()),
						height : Math.min(550, $window.height()),
						modal : true,
						open : false
					},
					
					/**
					 * 用于修改时显示隐藏元素
					 */
					modElementToggle : function(isShow) {
						if(isShow){
							$id("isShowDefaulted").show();
						}else{
							$id("isShowDefaulted").hide();
						}
					},
					viewElementToggle:function(isShow){
						if(isShow){
							$id("isShowCatNames").show();
							$id("isShowDefaulted").show();
						}else{
							$id("isShowCatNames").hide();
							$id("isShowDefaulted").hide();
						}
					},
					/**
					 * 增加时清空控件的值
					 */
					addInit : function() {
						textSet("name", "");
						textSet("desc", "");
						textSet("seqNo", "");
						textSet("salePrice", "");
						//
						oldName = "";
						oldGroupId = "";
						oldKindId = "";
						//
						textSet("imageUuid", "");
						textSet("imageUsage", "");
						textSet("imagePath", "");
						$("#logoCarSvc").attr("src",null);
						//
						textSet("iconUuidApp", "");
						textSet("iconUsageApp", "");
						textSet("iconPathApp", "");
						$("#logoCarSvcApp").attr("src",null);
						//
						textSet("iconUuid2", "");
						textSet("iconUsage2", "");
						textSet("iconPath2", "");
						$("#logoCarSvcTwo").attr("src",null);
						//
						textSet("iconUuid", "");
						textSet("iconUsage", "");
						textSet("iconPath", "");
						$("#logoCarSvcIcon").attr("src",null);
						//
						//textSet("catNames", "");
						textSet("appRange", "");
					},

					/**
					 * 修改和查看是给控件赋值
					 */
					modAndViewInit : function(data) {
						textSet("seqNo", data.seqNo);
						textSet("name", data.name);
						textSet("desc", data.desc);
						textSet("salePrice", data.salePrice);
						textSet("appRange", data.appRange);
						loadGroup("groupId", data.carSvcGroup.id);
						if (data.disabled == true) {
							$id("defaulted-N").attr("checked", true);
						}else{
							$id("defaulted-Y").attr("checked", true);
						}
						 textSet("imageUuid", data.imageUuid);
						 textSet("imageUsage", data.imageUsage);
						 textSet("imagePath", data.imagePath);
						 $("#logoCarSvc").attr("src",data.fileBrowseUrl + "?" + new Date().getTime());
						 //
						 textSet("iconUuidApp", data.iconUuidApp);
						 textSet("iconUsageApp", data.iconUsage);
						 textSet("iconPathApp", data.iconPathApp);
						 $("#logoCarSvcApp").attr("src",data.fileBrowseUrlApp + "?" + new Date().getTime());
						 //
						 textSet("iconUuid2", data.iconUuid2);
						 textSet("iconUsage2", data.iconUsage);
						 textSet("iconPath2", data.iconPath2);
						 $("#logoCarSvcTwo").attr("src",data.fileBrowseUrlIcon2 + "?" + new Date().getTime());
						 //
						 textSet("iconUuid", data.iconUuid);
						 textSet("iconUsage", data.iconUsage);
						 textSet("iconPath", data.iconPath);
						 $("#logoCarSvcIcon").attr("src",data.fileBrowseUrlIcon + "?" + new Date().getTime());
					},

					/**
					 * 封装查询参数
					 */
					queryParam : function(querJson, formProxyQuery) {
						var name = formProxyQuery.getValue("queryName");
						if (name != "") {
							querJson['name'] = name;
						}
					},
					savePostDataChange : function(postData){
						postData["imageUuid"]=textGet("imageUuid");
						if(textGet("imageUsage")){
							postData["imageUsage"]=textGet("imageUsage");
						}
						postData["imagePath"]=textGet("imagePath");
						
						postData["iconUuidApp"]=textGet("iconUuidApp");
						if(textGet("iconUsageApp")){
							postData["iconUsage"]=textGet("iconUsageApp");
						}
						postData["iconPathApp"]=textGet("iconPathApp");
						
						postData["iconUuid2"]=textGet("iconUuid2");
						if(textGet("iconUsage2")){
							postData["iconUsage"]=textGet("iconUsage2");
						}
						postData["iconPath2"]=textGet("iconPath2");
						//
						postData["iconUuid"]=textGet("iconUuid");
						if(textGet("iconUsage")){
							postData["iconUsage"]=textGet("iconUsage");
						}
						postData["iconPath"]=textGet("iconPath");
					},
					/**
					 * 注册增加和修改的验证表单控件
					 */
					formProxyInit : function(formProxy) {
						//注册表单控件
						formProxy.addField({
							id : "name",
							key : "name",
							rules : [ {
								rule : function(idOrName, type, rawValue, curData) {
									// 若显示且为空，给予提示
									if ($id("name").is(":visible") && isNoB(rawValue)) {
										return false;
									}
									var groupId = $id("groupId").val();
									isExistByName(rawValue,groupId);
									return !isExist;
								},
								message : "名称已存在!"
							} ,"maxLength[30]"]
						});
						formProxy.addField({
							id : "desc",
							key : "desc",
							rules : ["maxLength[100]"]
						});
						formProxy.addField({
							id : "groupId",
							key : "groupId",
							rules : [ {
								rule : function(idOrName, type, rawValue, curData) {
									// 若显示且为空，给予提示
									if ($id("groupId").is(":visible") && isNoB(rawValue)) {
										return false;
									}
									return true;
								},
								message : "此为必填选！"
							} ]
						});
						formProxy.addField({
							id : "disabled",
							key : "disabled",
							required : true
						});
						formProxy.addField({
							id : "salePrice",
							key : "salePrice",
							required : true,
							rules : [ "maxLength[18]","isMoney"]
						});
						formProxy.addField({
							id : "appRange",
							key : "appRange",
							rules : ["maxLength[100]"]
						});
					},

					/**
					 * 注册查询验证表单控件
					 * @param formProxyQuery
					 */
					formProxyQueryInit : function(formProxyQuery) {
						// 注册表单控件
						formProxyQuery.addField({
							id : "queryName",
							rules : [ "maxLength[30]" ]
						});
					},
					saveOldData : function(data) {
						oldName = data.name;// 保存原来名称，用于检测是否存在
						oldKindId = data.kindId; // 保存原来种类，用于检测是否存在
						oldGroupId = data.groupId; // 保存原来种类，用于检测是否存在
					},
					/**
					 * 获得删除确认提示
					 */
					getDelComfirmTip : function(data) {
						var theSubject = data.name;
						return '确定要删除"' + theSubject + '"车辆服务吗?';
					},
					pageLoad : function(jqGridCtrl,cacheDataGridHelper) {
						loadCarSvcGroup();
						var size = getImageSizeDef("image.logo");
						$id("logoCarSvc").attr("width", size.width);
						$id("logoCarSvc").attr("height", size.height);
						//上传pc有色图标
						initFileUpload("fileToUploadFileLogo");
						//上传pc无色图标
						initFileUpload("fileToUploadFileLogoTwo");
						//app上传
						initFileUpload("fileToUploadFileLogoApp");
						//ad上传
						initFileUpload("fileToUploadFileLogoIcon");
						//绑定修改模块上传按钮
						$id("btnfileToUploadFile").click(function(){
							fileToUploadFileIcon("fileToUploadFileLogo");
						});
						//绑定修改模块上传按钮
						$id("btnfileToUploadFileApp").click(function(){
							fileToUploadFileIconApp("fileToUploadFileLogoApp");
						});
						//绑定修改模块上传按钮
						$id("btnfileToUploadFileTwo").click(function(){
							fileToUploadFileIconTwo("fileToUploadFileLogoTwo");
						});
						//绑定修改模块上传按钮
						$id("btnfileToUploadFileIcon").click(function(){
							fileToUploadFileIconAd("fileToUploadFileLogoIcon");
						});
						$(document).on("click", ".dlgRateSet", function() {
							userRateSet($(this).attr("cellValue"), cacheDataGridHelper);
						});
						//上架下架操作
						$(document).on("click",".updateDisabled",function() {
							var id = $(this).attr("cellValue");
							var obj = cacheDataGridHelper.getRowData(id);
							var msg = "";
							if (obj.disabled == false) {
								msg = "服务正在使用中，确定要下架吗？";
							} else {
								msg = "服务确定要上架吗？";
							}
							disabled_confirm(msg, jqGridCtrl,obj);
						});
						
					}
				});
		//上架提示
		function disabled_confirm(msg, jqGridCtrl, obj) {
			if (msg != "") {
				var yesHandler = function(layerIndex) {
					theLayer.hide();
					updateDisabled(jqGridCtrl, obj);
				};
				var noHandler = function(layerIndex) {
					theLayer.hide();
				};
				//
				var theLayer = Layer.confirm(msg, yesHandler, noHandler);
			} else {
				updateDisabled(jqGridCtrl, obj);
			}
		}
		//上下架操作
		function updateDisabled(jqGridCtrl, obj) {
			var ajax = Ajax.post("/carSvc/update/do");
			var data = {
				"id" : obj.id,
				"disabled" : obj.disabled == true ? false : true
			};
			ajax.data(data);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					jqGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		//----------------------------------------------------------------
		function userRateSet(id,cacheDataGridHelper){
			var ajax = Ajax.post("/ecard/normal/list/get");
			ajax.sync();
		    ajax.done(function(result, jqXhr){
		    	if(result.type== "info"){
					userRateList = result.data;
					var theTpl = laytpl( $id("userRateSetTpl").html());
					var htmlStr = theTpl.render(userRateList);
					$id("showUserRateSet").html(htmlStr);
				}else{
					Layer.msgWarning(result.message);
				}
		    });
		    ajax.always(function() {
				//隐藏等待提示框
			});
			ajax.go();
			
			userRateSetDialog.dialog("open");
			
			var userRateSet = cacheDataGridHelper.getRowData(id);
			$id("svcName").html(userRateSet.name);
			$("#showlogoCarSvcIcon").attr("src",userRateSet.fileBrowseUrl+"?"+new Date().getTime());
			$id("showSalePrice").html(userRateSet.salePrice);
			textSet("svcId", id);
			textSet("svcKindId", userRateSet.kindId);
			
			updateRateSet(id);
		}
		
		function updateRateSet(id){
			var ajax = Ajax.post("/ecard/svcRankDisc/list/get");
			var svcxRankDiscMap = {
					svcId : id,
					shopId : -1
			}
			ajax.data(svcxRankDiscMap);
		    ajax.done(function(result, jqXhr){
		    	if(result.type== "info"){
					var svcxRankDiscList = result.data;
					if(svcxRankDiscList){
						for(var i = 0; i < svcxRankDiscList.length; i++){
							$id("rate"+svcxRankDiscList[i].rank).val(svcxRankDiscList[i].rate*10);
						}
					}
					
				}else{
					Layer.msgWarning(result.message);
				}
		    });
		    ajax.always(function() {
				//隐藏等待提示框
			});
			ajax.go();			
		}
		
	</script>
</body>
<script type="text/html" id="userRateSetTpl" title="会员等级折扣模版">
	{{# for(var i = 0, len = d.length; i < len; i++){ }}
		<tr class="row">
			<td>{{ d[i].name || "" }}会员</td>
			<td><input type="text" id="rate{{ d[i].rank }}" class="field value one half wide" placeholder="0.0"/> 折</td>
			<input type="hidden" id="rank" style="width:150px;" value="{{ d[i].rank }}"/> 折
		</tr>
	{{# } }}
</script>
</html>