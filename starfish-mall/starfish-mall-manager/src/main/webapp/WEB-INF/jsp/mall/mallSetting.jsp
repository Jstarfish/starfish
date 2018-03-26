<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	<title>商城设置</title>
</head>
<body id="rootPanel">
	<div class="ui-layout-north" style="padding: 4px;" id="topPanel">
	</div>
	<div id="mainPanel" class="ui-layout-center" style="padding:0px;">
	<!-- 商城基本信息 -->
		<div class="form bordered" id="tab_basicInfo">
			<div class="field row">
				<input type="hidden" id="id" />
				<label class="field label two half wide">商城代码</label>
				<label id="code"></label>
			</div>
			<div class="field row">
				<label class="field label two half wide required">商城名称</label>
				<input class="field value two wide" type="text" id="name" />
			</div>
			<div class="field row">
				<label class="field label two half wide required">联系人</label>
				<input class="field value" type="text" id="linkMan" />
			</div>
			<div class="field row">
				<label class="field label two half wide required">手机号码</label>
				<input class="field value" type="text" id="phoneNo" />
			</div>
			<div class="field row">
				<label class="field label two half wide required">所在地区</label>
				<input type="hidden" id="regionId" />
				<input type="hidden" id="regionName" />
				<select class="field value" style="width: auto;" id="province"></select>
				<select class="field value" style="width: auto;" id="city"></select>
				<select class="field value" style="width: auto;" id="county"></select>	
				<select class="field value" style="width: auto;" id="town" style="display: none;"></select>
			</div>
			<div class="field row">
				<label class="field label two half wide required">街道地址</label>
				<input class="field value three wide" type="text" id="street" />
			</div>
			<div class="field row">
				<label class="field label two half wide">邮政编码</label>
				<input class="field value" type="text" id="postCode" maxlength="6" />
			</div>
			<%--div class="field row">
				<label class="field label two half wide">商城状态</label>
				<div class="field group">
					<input type="radio" id="mallStatusOn" name="disabled" value="false" checked="checked" />
					<label for="mallStatusOn">启用</label>
					<input type="radio" id="mallStatusOff" name="disabled"  value="true" />
					<label for="mallStatusOff">禁用</label>
				</div>
			</div> --%>
			<div class="field row">
				<label class="field label two half wide">商城LOGO</label>
				<input type="hidden" id="logoUuid" />
				<input type="hidden" id="logoUsage" />
				<input type="hidden" id="logoPath" />
				<input type="hidden" id="fileBrowseUrl" />
				<input class="hidden file" type="file" id="logoFile" />
			</div>
			<div class="field row">
				<button class="normal button" id="logoFileToUpload" style="margin-left: 200px;">上传</button>
			</div>
			<div class="field row" style="height: 45px;">
				<label class="field label two half wide">预览</label>
				<img id="logoImg" />
			</div>
			<div class="field row">
				<label class="field label two half wide">ICP证书或ICP备案证书号</label>
				<input class="field value two wide" type="text" id="icpNo" />
			</div>
			<div class="field row">
				<label class="field label two half wide">ICP 备案证书文件</label>
				<input type="hidden" id="icpFileUuid" />
				<input type="hidden" id="icpFileUsage" />
				<input type="hidden" id="icpFilePath" />
				<input class="hidden file" type="file" id="icpFile" />
			</div>
			<div class="field row">
				<button class="normal button" id="icpFileToUpload" style="margin-left: 200px;">上传</button>
			</div>
			<div class="field row" style="height:90px;">
				<label class="field label two half wide">商城描述</label>
				<textarea class="field value three wide" style="height:80px;" id="desc"></textarea>
			</div>
			
			<div class="field row align center">
				<button class="normal button" id="btnMallSave">保存</button>
				<span class="normal spacer"></span>
				<button class="normal button" id="btnMallCancel">取消</button>
			</div>
		</div>
	</div>
		

<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
	// 商城基本信息页面表单代理
	var mallFormProxy = FormProxy.newOne();
	// tabs、
	var theTabsCtrl;
	// 最多支持4级地区
	var maxLevel = 4;
	var curAction;
	
	// 加载商城基本信息
	function loadMall() {
		var ajax = Ajax.post("/mall/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var data = result.data;
				if (data != null) {
					fillMall(data);
				} else {
					initMall();
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}

	// 检验商城基本信息
	function checkMall() {
		mallFormProxy.addField("code");
		mallFormProxy.addField({
			id : "name",
			required : true,
			rules : [ "rangeLength[1, 30]" ]
		});
		mallFormProxy.addField({
			id : "icpNo",
			rules : [ "rangeLength[0, 30]" ]
		});
		mallFormProxy.addField("regionId");
		mallFormProxy.addField("regionName");
		mallFormProxy.addField({
			id : "province",
			required : true
		});
		mallFormProxy.addField({
			id : "city",
			required : true
		});
		mallFormProxy.addField({
			id : "county",
			required : true
		});
		mallFormProxy.addField({
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
			} ]
		});
		mallFormProxy.addField({
			id : "street",
			required : true,
			rules : [ "rangeLength[1, 60]" ]
		});
		mallFormProxy.addField("postCode");
		mallFormProxy.addField({
			id : "linkMan",
			required : true,
			rules : [ "rangeLength[1, 30]" ]
		});
		mallFormProxy.addField({
			id : "phoneNo",
			required : true,
			rules : [ "isMobile" ]
		});
		//mallFormProxy.addField({
			//name : "disabled",
			//required : true
		//});
		mallFormProxy.addField({
			id : "desc",
			rules : [ "rangeLength[0, 250]" ]
		});
		return mallFormProxy.validateAll();
	}

	// 判断地区中第四级town是否显示
	function isShowTown(town){
 		return $id(town).val() != null;
	}
	
	// 保存商城基本信息
	function saveMall() {
		var progressBar = Layer.progress("正在提交数据...");
		var ajax = Ajax.post("/mall/save/do");
		// 设置最后一级地区id
		var regionId;
		if(isShowTown("town")){
			regionId = $("#town").val();
		} else {
			regionId = $("#county").val();
		}
		var postData = ({
			id : $("#id").val(),
			name : $("#name").val(),
			icpNo : $("#icpNo").val(),
			icpUuid : $("#icpFileUuid").val(),
			icpUsage : $("#icpFileUsage").val(),
			icpPath : $("#icpFilePath").val(),
			regionId : regionId,
			regionName : $("#province option:selected").text() + $("#city option:selected").text() + $("#county option:selected").text() + $("#town option:selected").text(),
			street : $("#street").val(),
			postCode : $("#postCode").val(),
			linkMan : $("#linkMan").val(),
			phoneNo : $("#phoneNo").val(),
			//disabled : $("input:radio[name='disabled']:checked").val(),
			desc : $("#desc").val(),
			logoUuid : $("#logoUuid").val(),
			logoUsage : $("#logoUsage").val(),
			logoPath : $("#logoPath").val()
		});
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
				
				// 更新商城标题栏LOGO
				//console.log($(window.parent.document).find("#scopeLogoImg").html());
				console.log(objToJsonStr(window.parent.$("#scopeLogoImg")));
				//document.getElementById("managedFrame-module-1")
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.always(function() {
			progressBar.hide();
		});
		ajax.go();
	}

	// 填充商城基本信息
	function fillMall(data) {
		$("#id").val(data.id);
		$("#code").text(data.code);
		$("#name").val(data.name);
		$("#icpNo").val(data.icpNo);
		$("#icpFileUuid").val(data.icpUuid);
		$("#icpFileUsage").val(data.icpUsage);
		$("#icpFilePath").val(data.icpPath);
		initArea(data.regionId);
		$("#street").val(data.street);
		$("#postCode").val(data.postCode);
		$("#linkMan").val(data.linkMan);
		$("#phoneNo").val(data.phoneNo);
		// 商城状态是否禁用，true:1禁用；false：0启用；
		//if (data.disabled == 'true') {
		//	$("input:radio[id=mallStatusOff]").prop("checked", "checked");
		//} else if (data.disabled == 'false') {
			//$("input:radio[id=mallStatusOn]").prop("checked", "checked");
		//}
		$("#desc").val(data.desc);
		$("#logoUuid").val(data.logoUuid);
		$("#logoUsage").val(data.logoUsage);
		$("#logoPath").val(data.logoPath);
		$("#logoImg").attr("src", data.fileBrowseUrl+"?"+new Date().getTime());
		
	}

	// 初始化商城基本信息
	function initMall() {
		// 初始化商城基本信息省份和商城默认状态：启用
		loadProvince();
		$("input:radio[id=mallStatusOn]").prop("checked", "checked");
	}

	// 加载商城基本信息省份
	function loadProvince() {
		var ajax = Ajax.get("/setting/region/selectList/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData("province", result.data);
				
				loadMall();
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}

	// 根据商城基本信息中选择的省加载市
	function loadCity() {
		// 隐藏商城基本信息页面区、清除县级信息
		hideTown();
		$id("county").empty();

		// 如果没有选择省，直接返回
		var province = $("#province").val();
		if (isUndef(province) || eqNull(province) || province == '') {
			return;
		}

		var ajax = Ajax.get("/setting/region/selectList/get");
		ajax.params({
			parentId : province
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData("city", result.data);
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}

	// 根据商城基本信息中选择的市加载县
	function loadCounty() {
		// 隐藏商城基本信息页面区
		hideTown();

		// 如果没有选择市，直接返回
		var city = $("#city").val();
		if (isUndef(city) || eqNull(city) || city == '') {
			return;
		}

		var ajax = Ajax.get("/setting/region/selectList/get");
		ajax.params({
			parentId : city
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

	// 根据商城基本信息中选择的县加载区
	function loadTown() {
		// 隐藏商城基本信息页面区
		hideTown();

		// 如果没有选择县，直接返回
		var county = $("#county").val();
		if (isUndef(county) || eqNull(county) || county == '') {
			return;
		}

		var ajax = Ajax.get("/setting/region/selectList/get");
		ajax.params({
			parentId : county
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

	// 使用指定地区信息初始化省市县信息
	function initArea(regionId) {
		var params = {
			id : regionId
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
						// 加载省、市
						$("#province option[value='" + arr[0] + "']").attr("selected", true);
						loadCityByParentId(arr[0], arr[1]);
						// 加载县、区
						if (arr.length == maxLevel) {
							loadCountyByCityId(arr[1], arr[2]);
							loadTownByCountyId(arr[2], regionId);
						} else {
							loadCountyByCityId(arr[1], regionId);
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
				$("#county option[value='" + countyId + "']")
						.attr("selected", true);
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
				// 若有第四级地区，加载并显示
				var data = result.data;
				if (data.items.length > 0) {
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
	
	// 隐藏商城基本信息区下拉框及错误提示
	function hideTown() {
		$("#town").hide().empty();
		hideMiscTip("town");
	}

	// 隐藏商城设置页面所有错误提示
	function hideAllMessages() {
		mallFormProxy.hideMessages();
		bizParamFormProxy.hideMessages();
	}
	
	// 上传ICP备案证书文件
	function icpFileToUpload() {
		var uuid = $id("icpFileUuid").val();
		if(isNoB(uuid)){
			var formData = {
				usage : "file.misc",
			};
		} else {
			var formData ={
				update : true,
				uuid : uuid
			};
		}
		sendFileUpload("icpFile", {
			url : getAppUrl("/file/upload"),
			dataType : "json",
			// 自定义数据
			formData : formData,
			done : function(e, result) {
				var resultInfo = result.result;
				//单独提取文件信息列表
				var fileInfoList = getFileInfoList(resultInfo);
				if(resultInfo.type == "info"){
					$id("icpFileUuid").val(fileInfoList[0].fileUuid);
					$id("icpFileUsage").val(fileInfoList[0].fileUsage);
					$id("icpFilePath").val(fileInfoList[0].fileRelPath);
					$("#icpImg").attr("src",fileInfoList[0].fileBrowseUrl+"?"+new Date().getTime());
					Layer.msgSuccess("上传成功！");
				} else {
					Layer.msgWarning("上传失败！");
				}
			},
			fail : function(e, data) {
				console.log(data);
			},
			noFilesHandler : function() {
				Layer.msgWarning("请选择或更换证书文件");
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
	
	// 上传logo文件
	function logoFileToUpload(){
		var uuid = $id("logoUuid").val();
		if(isNoB(uuid)){
			var formData = {
				usage : "image.logo",
				subUsage : "mall"
			};
		} else {
			var formData ={
				update : true,
				uuid : uuid
			};
		}
		sendFileUpload("logoFile", {
			url : getAppUrl("/file/upload"),
			dataType : "json",
			// 自定义数据
			formData : formData,
			done : function(e, result) {
				var resultInfo = result.result;
				//单独提取文件信息列表
				var fileInfoList = getFileInfoList(resultInfo);
				if(resultInfo.type == "info"){
					$id("logoUuid").val(fileInfoList[0].fileUuid);
					$id("logoUsage").val(fileInfoList[0].fileUsage);
					$id("logoPath").val(fileInfoList[0].fileRelPath);
					$id("fileBrowseUrl").val(fileInfoList[0].fileBrowseUrl);
					$("#logoImg").attr("src",fileInfoList[0].fileBrowseUrl+"?"+new Date().getTime());
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
	
	
	//
	function getCallbackAfterGridLoaded(){}
	
	// 初始化页面
	$(function() {
		// 页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			allowTopResize : false,
			onresize : hideLayoutTogglers
		});
		// 隐藏布局north分割线
		$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
		// 
		hideLayoutTogglers();
		// 
		theTabsCtrl = $("#tabs").tabs();

		// 针对不同tab页绑定不同的保存操作
		$("#tabs").on("tabsactivate", function(event, ui) {
			// 隐藏商城设置页面所有错误提示
			hideAllMessages();
			
			var currentTab = ui.newTab.context.id;
			if (currentTab == "tabBizParams") {// 参数设置
				loadBizParams();
			} else if (currentTab == "tabBasicInfo") {// 商城基本信息
				loadProvince();
				loadMall();
			}
		});

		// 加载商城
		loadProvince();

		// 初始化上传控件
		initFileUpload("icpFile");
		$("#icpFileToUpload").click(icpFileToUpload);
		
		initFileUpload("logoFile");
		$("#logoFileToUpload").click(logoFileToUpload);
		
		var size = getImageSizeDef("image.logo");
		$id("logoImg").attr("width", size.width);
		$id("logoImg").attr("height", size.height);
		
		// 根据商城基本信息中选择的省加载市
		$("#province").change(function() {
			loadCity();
		});

		// 根据商城基本信息中选择的市加载县
		$("#city").change(function() {
			loadCounty();
		});

		// 根据商城基本信息中选择的县加载区
		$("#county").change(function() {
			loadTown();
		});
		
		// 保存/取消商城操作
		$id("btnMallSave").click(function() {
			if (checkMall()) {
				saveMall();
			}
		});
		$id("btnMallCancel").click(function() {
			loadProvince();
		});
		
		// 保存/取消参数设置
		$id("btnBizParamsSave").click(function() {
			if (checkBizParams()) {
				saveBizParams();
			}
		});
		$id("btnBizParamsCancel").click(function() {
			loadBizParams();
		});
		
	});
</script>
</body>

<script id="bizParamsTpl" type="text/html" title="业务参数模板">
{{# for(var i = 0, len = d.length; i < len; i++){ }}
	<div class="field row">
		<label class="field label two half wide required">{{ d[i].name }}</label>
		<input class="field value" type="text" id={{ d[i].code }} value={{ d[i].value }} />
	</div>
{{# } }}
</script>

</html>