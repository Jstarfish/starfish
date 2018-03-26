<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	<title>注册商城信息</title>
</head>
<body id="rootPanel">
<div class="ui-layout-north" style="padding: 4px;" id="topPanel">
	<div class="simple block">
		<div class="header">
			<label>平台设置&nbsp;&gt;&nbsp;商城设置</label>
		</div>
	</div>
</div>

<div id="mainPanel" class="ui-layout-center" style="padding:0px;">
	<div id="tabs" class="noBorder">
		<ul>
			<li><a href="#tab_mall" id="tabLinkMall">商城基本信息</a></li>
			<li><a href="#tab_sys" id="tabLinkSys">商城负责人信息</a></li>
		</ul>

		<div id="tab_mall">
			<div class="form">
				<input type="hidden" id="id" />
				<div class="field row">
					<label class="field label two wide required">代码</label>
					<input class="field value" type="text" id="code" maxlength="50" />
				</div>
				<div class="field row">
					<label class="field label two wide required">商城名称</label>
					<input class="field value" type="text" id="name" maxlength="30" />
				</div>
				<div class="field row">
					<label class="field label two wide required">联系人</label>
					<input class="field value" type="text" id="linkMan" maxlength="30" />
				</div>
				<div class="field row">
					<label class="field label two wide required">手机号</label>
					<input class="field value" type="text" id="phoneNo" maxlength="11" />
				</div>
				<div class="field row">
					<label class="field label two wide required">所在地区</label>
					<select class="field value" style="width: auto;" id="province"></select>
					<select class="field value" style="width: auto;" id="city"></select>
					<select class="field value" style="width: auto;" id="county"></select>
					<select class="field value" style="width: auto;" id="town" style="display: none;"></select>
				</div>
				<div class="field row">
					<label class="field label two wide required">街道地址</label>
					<input class="field value three wide" type="text" id="street" maxlength="60" />
				</div>
				<div class="field row align center">
					<button id="btnNext">下一步</button>
				</div>
			</div>
		</div>
		
		<div id="tab_sys">
			<div class="form" id="mallSysDiv">
				<input type="hidden" id="userId" />
				<div class="field row">
					<label class="field label two wide required">负责人昵称</label>
					<input class="field value" type="text" id="nickName" />
				</div>
				<div class="field row">
					<label class="field label two wide required">负责人手机</label>
					<input class="field value" type="text" id="userPhoneNo" maxlength="11" />
				</div>
				<span class="normal hr divider"></span>
				<div class="field row align center">
					<button id="btnRegistSys">保存</button>
				</div>
			</div>
		</div>
	</div>
</div>
	
<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
	// 最多支持4级地区
	var maxLevel = 4;
	
	// 实例化表单代理
	var mallFormProxy = FormProxy.newOne();
	var sysFormProxy = FormProxy.newOne();
	
	// 初始化商城基本信息页面
	function initMallPage(){
		//changeTypeFlag(false);
		var ajax = Ajax.post("/mall/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var data = result.data;
				if(data){
					$("#id").val(data.id);
					$("#code").val(data.code);
					$("#name").val(data.name);
					$("#linkMan").val(data.linkMan);
					initArea(data.regionId);
					$("#street").val(data.street);
					$("#phoneNo").val(data.phoneNo);
					/* var flag = data.userAccount.typeFlag;
					if(flag == "0"){
						$("#typeFlag-0").attr("checked","checked");
						changeTypeFlag(false);
					}else{
						$("#typeFlag-1").attr("checked","checked");
						changeTypeFlag(true);
					} */
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
						singleSet("province",arr[0]);
						loadCityByParentId(arr[0], arr[1]);
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
	
	// 加载商城基本信息省份
	function loadProvince() {
		var ajax = Ajax.get("/setting/region/selectList/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData("province", result.data);
				// 确保省份加载完以后再初始化商城信息
				initMallPage();
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

		var ajax = Ajax.get("/setting/region/selectList/get");
		ajax.params({
			parentId : $("#province").val()
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

	// 根据商城基本信息中选择的县加载区
	function loadTown() {
		// 隐藏商城基本信息页面区
		hideTown();

		var ajax = Ajax.get("/setting/region/selectList/get");
		ajax.params({
			parentId : $("#county").val()
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var data = result.data;
				if (data.items.length > 0) {
					loadSelectData("town", data);
					$("#town").show();
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
	
	// 检验商城基本信息的注册表单
	function checkRegistMall() {
		mallFormProxy.addField({
			id : "code",
			required : true,
 			rules : [ "rangeLength[1,90]" , {
 				rule : function(idOrName, type, rawValue, curData) {
 					if (validateCode(rawValue)) {
 						return true;
 					}
 					return false;
 				},
 				message : "该商城编号已存在"
 			}]
		});
		mallFormProxy.addField({
			id : "code",
			required : true,
			rules : [ "rangeLength[1,90]" ]
		});
		mallFormProxy.addField({
			id : "name",
			required : true,
			rules : [ "rangeLength[1,30]" ]
		});
		mallFormProxy.addField({
			id : "linkMan",
			required : true,
			rules : [ "rangeLength[1,30]" ]
		});
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
			required : true
		});
		mallFormProxy.addField({
			id : "phoneNo",
			required : true,
			rules : [ "isMobile" ]
		});
		
		return mallFormProxy.validateAll();
	}

	// 判断区是否显示
	function isShowTown(town){
 		//return $id(town).is(":visible");
 		// 比较值可支持多个tab页中的判断
 		return $id(town).val() != null;
	}
	
	// 注册商城基本信息
	function registMall() {
		if (checkRegistMall()) {
			var ajax = Ajax.post("/mall/admin/get");
			ajax.sync(true);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data;
					if (data == null) {
						// 选择第二个tab
						$("#tabs").tabs("option", "active", 1);
					}
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
		}
	}

	// 初始化商城管理员页面
	function loadMallAdminInfo() {
		var ajax = Ajax.post("/mall/admin/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var data = result.data;
				if (data != null && data.id != null) {
					$("#userId").val(data.id);
					$("#nickName").val(data.nickName);
					$("#userPhoneNo").val(data.phoneNo);
					$("#userPhoneNo").prop("disabled", true);
				}
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	
	// 检验商城管理员页面的注册表单
	function checkRegistSys() {
		sysFormProxy.addField({
			id : "nickName",
			required : true,
			rules : [ "rangeLength[1,30]" ]
		});
		sysFormProxy.addField({
			id : "userPhoneNo",
			required : true,
			rules : [ "isMobile" , {
				rule : function(idOrName, type, rawValue, curData) {
					var userPhoneStatus = $id("userPhoneNo").prop("disabled");
					if(userPhoneStatus) return true;
					if (validatePhoneNo(rawValue, $id("userId").val())) {
						return true;
					}
					return false;
				},
				message : "该手机号码已被注册"
			}]
		});
// 		sysFormProxy.addField({
// 			id : "password",
// 			required : true,
// 			rules : [ "isPwd" ]
// 		});
// 		sysFormProxy.addField({
// 			id : "rePassword",
// 			required : true,
// 			rules : [ "isPwd", {
// 				rule : function(idOrName, type, rawValue, curData) {
// 					if ($id("password").val() == rawValue) {
// 						return true;
// 					}
// 					return false;
// 				},
// 				message : "再次密码不一致，请重新输入"
// 			}]
// 		});
		return sysFormProxy.validateAll();
	}
	
	function saveMallInfo(){
		if(!checkRegistMall()){
			mallFormProxy.hideMessages();
			// 选择第一个tab
			$("#tabs").tabs("option", "active", 0);
			return;
		}
		var ajax = Ajax.post("/mall/regist/or/update/do");
		// 设置最后一级地区id
		var regionId;
		if(isShowTown("town")){
			regionId = $("#town").val();
		} else {
			regionId = $("#county").val();
		}
		ajax.data({
			id : $id("id").val(), // 商城id
			operatorId : $id("userId").val(), // 用户id
			code : $id("code").val(),
			name : $id("name").val(),
			linkMan : $id("linkMan").val(),
			regionId : regionId,
			regionName : $("#province option:selected").text() + $("#city option:selected").text() + $("#county option:selected").text() + $("#town option:selected").text(),
			street : $id("street").val(),
			phoneNo : $id("phoneNo").val(),
			bizScope : "mall",
			nickName : $id("nickName").val(),
			//J
//				password : $id("password").val()
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
				$id("userId").val(result.data.operatorId);
				$("#userPhoneNo").prop("disabled", true);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}

	// 注册商城管理员
	function registSys() {
		if (checkRegistSys()) {
			saveMallInfo();
		}
	}

	// 检验商城编号是否唯一
	function validateCode(code) {
		var ok;
		var ajax = Ajax.post("/mall/code/validate/do");
		ajax.sync(true);
		ajax.data({
			code : code
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				ok = true;
			} else {
				ok = false;
			}
		});
		ajax.go();
		
		return ok;
	}
	
	// 检验商城手机号是否唯一
	function validatePhoneNo(phoneNo, userId) {
		var ok;
		var ajax = Ajax.post("/mall/phoneNo/validate/do");
		ajax.sync(true);
		ajax.data({
			id : userId,
			phoneNo : phoneNo
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				ok = true;
			} else {
				ok = false;
			}
		});
		ajax.go();
		
		return ok;
	}
	
	// 隐藏商城基本信息区下拉框及错误提示
	function hideTown() {
		$("#town").hide().empty();
		hideMiscTip("town");
	}

	// 隐藏商城管理员信息区下拉框及错误提示
	function hideSysTown() {
		$("#sysTown").hide().empty();
		hideMiscTip("sysTown");
	}

	// 隐藏商城注册页面所有错误提示
	function hideAllMessages() {
		mallFormProxy.hideMessages();
		sysFormProxy.hideMessages();
	}

	/* //改变账户类型
	function changeTypeFlag(flag){
		if(flag){
			hideMiscTip("acctPhoneNo");
			$id("type0").hide();
			$id("type1").show();
			mallFormProxy.removeField("acctPhoneNo");
			mallFormProxy.addField({
				id : "vfCode",
				key : "userAccount.vfCode",
				required : true,
				rules : ["maxLength[30]"]
			});
		}else{
			hideMiscTip("vfCode");
			$id("type0").show();
			$id("type1").hide();
			mallFormProxy.removeField("vfCode");
			mallFormProxy.addField({
				id : "acctPhoneNo",
				key : "userAccount.phoneNo",
				required : true,
				rules : ["maxLength[11]","isMobile"]
			});
			
		}
	} */
	// 初始化页面
	$(function() {
		// 页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 60,
			allowTopResize : false,
			onresize : hideLayoutTogglers
		});
		// 隐藏布局north分割线
		$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
		// 
		hideLayoutTogglers();
		
		$("#tabs").tabs();
		$("button").button();

		// 加载商城基本信息省份
		loadProvince();

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

		// 初始化商城管理员页面错误提示
		$("#tabLinkMall").click(function() {
			hideAllMessages();
		});
		
		// 跳转到第二个tab页
		$("#btnNext").click(function() {
			if(checkRegistMall()){
				$("#tabs").tabs("option", "active", 1);
			}
			loadMallAdminInfo();
		});

		// 初始化商城管理员页面
		$("#tabLinkSys").click(function() {
			hideAllMessages();
			loadMallAdminInfo();
		});

		// 注册商城管理员
		$("#btnRegistSys").click(function() {
			registSys();
		});
	});
</script>
</body>
</html>