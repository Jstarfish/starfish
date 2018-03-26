<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>资源注册</title>
</head>
<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding:4px;vertical-align: bottom;">
		<div class="simple block">
			<div class="header">
				<label>资源模块管理   &gt; 系统资源注册</label>
			</div>
			<input type="hidden" id="scope" value="sys"></input>
		</div>
	</div>
	<div id="mainPanel" class="ui-layout-center" style="padding:4px;">
		<div id="resourceTabs" class="noBorder">
			<ul>
				<li><a href="#sys_resource">系统</a></li>
				<li><a href="#mall_resource">商城</a></li>
				<li><a href="#shop_resource">店铺</a></li>
				<li><a href="#wxshop_resource">卫星店</a></li>
				<li style="display: none;"><a href="#agency_resource">代理处</a></li>
			</ul>
			<div id="sys_resource">
				<div class="filter section">
					<div class="filter row">
						<button onclick="openAddOrUpdateWindow(true)" class="normal button">添加</button>
						<span class="spacer"></span>
						<button onclick="deleteResource()" class="normal button">删除</button>
						<div class="normal group right aligned">
							<label class="label">资源名称：</label><input class="normal input one half wide" type="text" id="name-sys" maxlength="30"/>
							<span class="spacer"></span>
							<label class="label">URL：</label> <input id="url-sys" class="normal input two wide"  type="text" maxlength="250"/> 
							<span class="vt divider"></span>
							<button id="button" onclick="queryByScope('sys')" class="normal button">查询</button>
						</div>
					</div>
				</div>
				<div id="sys_resource_grid">
					<table id="list_sys"></table>
					<div id="pager_sys"></div>
				</div>
			</div>
			<div id="mall_resource">
				<div class="filter section">
					<div class="filter row">
						<button onclick="openAddOrUpdateWindow(true)" class="normal button">添加</button>
						<span class="spacer"></span>
						<button onclick="deleteResource()" class="normal button">删除</button>
						<div class="normal group right aligned">
							<label class="label">资源名称：</label><input class="normal input one half wide" type="text" id="name-mall" maxlength="30"/>
							<span class="spacer"></span>
							<label class="label">URL：</label> <input id="url-mall" class="normal input two wide"  type="text" maxlength="250"/> 
							<span class="vt divider"></span>
							<button id="button" onclick="queryByScope('mall')" class="normal button">查询</button>
						</div>
					</div>
				</div>
				<div id="mall_resource_grid">
					<table id="list_mall"></table>
					<div id="pager_mall"></div>
				</div>
			</div>
			<div id="shop_resource">
				<div class="filter section">
					<div class="filter row">
						<button onclick="openAddOrUpdateWindow(true)" class="normal button">添加</button>
						<span class="spacer"></span>
						<button onclick="deleteResource()" class="normal button">删除</button>
						<div class="normal group right aligned">
							<label class="label">资源名称：</label><input class="normal input one half wide" type="text" id="name-shop" maxlength="30"/>
							<span class="spacer"></span>
							<label class="label">URL：</label> <input id="url-shop" class="normal input two wide"  type="text" maxlength="250"/> 
							<span class="vt divider"></span>
							<button id="button" onclick="queryByScope('shop')" class="normal button">查询</button>
						</div>
					</div>
				</div>
				<div id="shop_resource_grid">
					<table id="list_shop"></table>
					<div id="pager_shop"></div>
				</div>
			</div>
			<div id="wxshop_resource">
				<div class="filter section">
					<div class="filter row">
						<button onclick="openAddOrUpdateWindow(true)" class="normal button">添加</button>
						<span class="spacer"></span>
						<button onclick="deleteResource()" class="normal button">删除</button>
						<div class="normal group right aligned">
							<label class="label">资源名称：</label><input class="normal input one half wide" type="text" id="name-wxshop" maxlength="30"/>
							<span class="spacer"></span>
							<label class="label">URL：</label> <input id="url-wxshop" class="normal input two wide"  type="text" maxlength="250"/> 
							<span class="vt divider"></span>
							<button id="button" onclick="queryByScope('wxshop')" class="normal button">查询</button>
						</div>
					</div>
				</div>
				<div id="wxshop_resource_grid">
					<table id="list_wxshop"></table>
					<div id="pager_wxshop"></div>
				</div>
			</div>
			<!-- 增加代理处 -->
			<div style="display: none;" id="agency_resource">
				<div class="filter section">
					<div class="filter row">
						<button onclick="openAddOrUpdateWindow(true)" class="normal button">添加</button>
						<span class="spacer"></span>
						<button onclick="deleteResource()" class="normal button">删除</button>
						<div class="normal group right aligned">
							<label class="label">资源名称：</label><input class="normal input one half wide" type="text" id="name-agency" maxlength="30"/>
							<span class="spacer"></span>
							<label class="label">URL：</label> <input id="url-agency" class="normal input two wide"  type="text" maxlength="250"/> 
							<span class="vt divider"></span>
							<button id="button" onclick="queryByScope('agency')" class="normal button">查询</button>
						</div>
					</div>
				</div>
				<div id="agency_resource_grid">
					<table id="list_agency"></table>
					<div id="pager_agency"></div>
				</div>
			</div>
		</div>
		<div id="resDialog" style="display: none;">
			    <div class="form">
				<div class="field row">
					<label class="field label required" for="newname">资源名称</label> 
					<input class="field value one half wide" type="text" id="newname" size="18" maxlength="13"/>
					<input type="hidden" id="resourId"/>
				</div>
				<div class="field row">
					<label class="field label required" for="resourceUrl">URL</label> 
					<input class="field value two wide" type="text" id="resourceUrl"/>
				</div>
				<div class="field row">
					<label class="field label required">是否页面</label>
					<div class="field group">
					<input id="pageFlag-Y" type="radio" name="pageFlag" value="1"/>
					<label for="pageFlag-Y">是</label>
					<input id="pageFlag-N" type="radio" name="pageFlag" value="0" checked/>
					<label for="pageFlag-N">否</label>
					</div>
				</div>
				<div class="field row">	 
					<label class="field label" for="permissionName">绑定权限</label> 
					<input class="field value one half wide" type="text" id="permissionName" name="permissionName" maxlength="4" readonly="readonly"> 
					<input type="button" id="btnBind" value="绑定" class="normal button"/>
					<input type="button" id="btnUnBind" value="解绑" class="normal button" style="display: none;"/>
					<input type="hidden" id="permId"/>
				</div>
			</div>
		</div>
		<div id =perList></div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />

	<script type="text/javascript">
	var sysGrid, mallGrid, shopGrid, agencyGrid;
	var jqTabsCtrl;
	var currentPanl;
	var permDialog;
	var resDialog;
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
		//
		var gridCtrlId,jqGridBox,headerHeight,pagerHeight;
		if(currentPanl=="#sys_resource"){
			gridCtrlId = "list_sys";
			jqGridBox = $("#gbox_" + gridCtrlId);
			headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			pagerHeight = $id("pager_sys").height();
		}else if(currentPanl=="#mall_resource"){
			gridCtrlId = "list_mall";
			jqGridBox = $("#gbox_" + gridCtrlId);
			headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			pagerHeight = $id("pager_mall").height();
		}else if(currentPanl=="#shop_resource"){
			gridCtrlId = "list_shop";
			jqGridBox = $("#gbox_" + gridCtrlId);
			headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			pagerHeight = $id("pager_shop").height();
		}else if(currentPanl=="#wxshop_resource"){
			gridCtrlId = "list_wxshop";
			jqGridBox = $("#gbox_" + gridCtrlId);
			headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			pagerHeight = $id("pager_wxshop").height();
		}else if(currentPanl=="#agency_resource"){
			gridCtrlId = "list_agency";
			jqGridBox = $("#gbox_" + gridCtrlId);
			headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			pagerHeight = $id("pager_agency").height();
		}else{
			gridCtrlId = "list_sys";
			jqGridBox = $("#gbox_" + gridCtrlId);
			headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
			pagerHeight = $id("pager_sys").height();
		}
		//
		if(sysGrid){
			sysGrid.setGridWidth(tabsCtrlWidth - 4 * 2);
			sysGrid.setGridHeight(tabsCtrlHeight - tabsHeaderHeight - 82 - pagerHeight - headerHeight);
		}
		//
		if(mallGrid){
			mallGrid.setGridWidth(tabsCtrlWidth - 4 * 2);
			mallGrid.setGridHeight(tabsCtrlHeight - tabsHeaderHeight - 82 - pagerHeight - headerHeight);
		}
		//
		if(shopGrid){
			shopGrid.setGridWidth(tabsCtrlWidth - 4 * 2);
			shopGrid.setGridHeight(tabsCtrlHeight - tabsHeaderHeight - 82 - pagerHeight - headerHeight);
		}
		
		if(wxshopGrid){
			wxshopGrid.setGridWidth(tabsCtrlWidth - 4 * 2);
			wxshopGrid.setGridHeight(tabsCtrlHeight - tabsHeaderHeight - 82 - pagerHeight - headerHeight);
		}
		//
		if(agencyGrid){
			agencyGrid.setGridWidth(tabsCtrlWidth - 4 * 2);
			agencyGrid.setGridHeight(tabsCtrlHeight - tabsHeaderHeight - 82 - pagerHeight - headerHeight);
		}
	}

	var resourcePage;
	var pageSave;

	function initData() {
		loadDataByScope("sys");
		loadDataByScope("mall");
		loadDataByScope("shop");
		loadDataByScope("wxshop");
		loadDataByScope("agency");
	}

	// 加载系统资源
	function loadDataByScope(scope) {
		//$("#scope").val(scope);
		
		var url = $("#url-"+scope).val();
		var name = $("#name-"+scope).val();
		var filterItems = {
			scope : scope,
			url : url,
			name : name
		};
		var postData = {
			filterStr : JSON.encode(filterItems)
		};
		var list_id = 'list_' + scope;
		var pager_id = 'pager_' + scope;
		var gridConfig = {
				height : "100%",
				width : "100%",
				url : getAppUrl("/res/sysRes/list/get"),
				datatype : "json",
				mtype : 'POST',
				postData : postData,
				colNames : ['id', '资源名称', 'URL', '是否页面', '权限名称', '操作' ],
				colModel : [{name : 'id',index : 'id',hidden:true},
							{name : 'name',index : 'name',width : 90},
							{name : 'pattern',index : 'pattern',width : 150},
							{name : 'pageFlag',index : 'pageFlag',width : 50,align : "center",
								formatter : function(cellValue, option, rowObject) {
									return cellValue==true?'是':'否';
								}},
							{name : 'permission.name',index : 'permission.name',width : 100},
							{name : 'id',index : 'id',width : 50,formatter : function(cellValue, option, rowObject) {
									return "&nbsp;&nbsp;&nbsp;<span> [<a class='item' href='javascript:void(0);' onclick='updateResource("
											+ objToJsonStr(rowObject)
											+ ")' >修改</a>]</span>";
								}} ],
				pager : '#' + pager_id,
				multiselect : true,
				multikey:'ctrlKey'
			};
		if(scope=='sys'){
			sysGrid = $id(list_id).jqGrid(gridConfig);
		}else if(scope=='mall'){
			mallGrid = $id(list_id).jqGrid(gridConfig);
		}else if(scope=='shop'){
			shopGrid = $id(list_id).jqGrid(gridConfig);
		}else if(scope=='wxshop'){
			wxshopGrid = $id(list_id).jqGrid(gridConfig);
		}else if(scope=='agency'){
			agencyGrid = $id(list_id).jqGrid(gridConfig);
		}
	}

	//实例化表单代理
	var formProxy = FormProxy.newOne();//广告
	formProxy.addField({
		id : "newname",
		required : true,
		rules : ["maxLength[30]"]
	});
	formProxy.addField({
		id : "resourceUrl",
		required : true,
		rules : ["maxLength[250]",{
			rule : function(idOrName, type, rawValue, curData) {
				checkUrl(rawValue);
				return !urlFlag;
			},
			message : "资源已注册！"
		}]
	});
	formProxy.addField({
		id : "pageFlag",
		type : "int"
	});
	formProxy.addField({
		id : "permissionName",
		messageTargetId : "btnBind"
	});
	
	var resObj;
	// flag:true-->save false:update
	function openAddOrUpdateWindow(flag) {
		var title = "新增资源";
		if (!flag || flag == 'false') {
			title = "修改资源";
			$id("saveOrUpdateBtn").val("修改");
		}else{
			$id("btnUnBind").hide();
			$("#resDialog input[type='text']").val("");
			$("#resDialog input[type='hidden']").val("");
			radioSet("pageFlag",0);
		}
		$("div[aria-describedby='resDialog']").find("span[class='ui-dialog-title']").html(title);
		//resDialog.title = title;
		resDialog.dialog("open");
	}

	function getCurrentScope(){
		var theTabLink = $id("resourceTabs").find("ul>li.ui-tabs-active.ui-state-active").find("a:first");
		var scope = "sys";
		//
		if(theTabLink){
			var href = $(theTabLink).attr("href");
			if(!isNoB(href)){
				scope = href.substring(1, href.indexOf("_"));
			}
		}
		return scope;
	}
	
	function openBindPermDialog(){
		var scope = getCurrentScope();
		var titleName = "";
		if (scope == "sys") {
			titleName = "系统权限";
		}
		if (scope == "mall") {
			titleName = "商城权限";
		}
		if (scope == "shop") {
			titleName = "店铺权限"
		}
		if (scope == "wxshop") {
			titleName = "卫星店权限"
		}
		var postData = {
				scope : scope
		};
		$("div[aria-describedby='perList']").find("span[class='ui-dialog-title']").html(titleName);
		//permDialog.title = titleName;
		var ajax = Ajax.post("/perm/sysRes/perms/list/get");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			var data = result.data;
			var permTplHtml = $id("permTpl").html();
			var permTplStr = laytpl(permTplHtml).render(data[scope]);
			$("#perList").html(permTplStr);
			radioSet("permRadio",$id("permId").val());
			permDialog.dialog("open");
		});
		ajax.go();
	}
	
	//权限解绑
	function unBindPerm(){
		$id("permId").val("");
		$("#permissionName").val("");
		Layer.msgSuccess("权限解绑成功");
	}

	function updateResource(resourceInfo) {
		resObj = resourceInfo;
		$("#resDialog input[type='text']").val("");
		$("#resDialog input[type='hidden']").val("");
		radioSet("pageFlag",0);
		openAddOrUpdateWindow(false);
		$id("newname").val(resourceInfo.name);
		$id("resourId").val(resourceInfo.id);
		$id("resourceUrl").val(resourceInfo.pattern);
		radioSet("pageFlag",resourceInfo.pageFlag);
		$id("permId").val(resourceInfo.permId);
		if(resourceInfo.permission){
			$("#permissionName").val(resourceInfo.permission.name);
			$id("btnUnBind").show();
		}else{
			$id("btnUnBind").hide();
		}
	}

	function saveOrUpdatePermAndRes() {
		var vldResult = formProxy.validateAll();
		if (!vldResult) {
			return;
		}
		var scope = getCurrentScope();
		var newname = $("#newname").val();
		var resourceUrl = $("#resourceUrl").val();
		var resourId = $("#resourId").val();
		var permName = $id("permissionName").val();
		var permId = null;
		if(!isNoB(permName)){
			permId = parseInt($("#permId").val());
		}
		
		var postData = {
			id : resourId,
			name : newname,
			pattern : resourceUrl,
			permId : permId,
			scope : scope,
			type : "url",
			pageFlag : parseInt(radioGet("pageFlag")),
			desc : "最新添加",
			seqNo : 1
		};
		var ajax = Ajax.post("/res/sysRes/save/do");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
				$('#list_' + scope).jqGrid('setGridParam', {
					datatype : 'json',
					page : 1
				}).trigger("reloadGrid");
				resDialog.dialog("close");
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}

	//检验手机号码是否唯一
	var urlFlag = false;
	function checkUrl(pattern) {
		var id = null;
		if(resObj){
			id = resObj.id;
		}
		var addFlag = (id == null || id == "");
		var updateFlag = (id != null && id != "" && pattern != resObj.pattern);
		if(addFlag || updateFlag){
			var ajax = Ajax.post("/res/sysRes/exist/by/pattern/do");
			ajax.data(pattern);
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				urlFlag = result.data;
			});
			ajax.fail(function() {
				urlFlag = false;
			});
			ajax.go();
		}
	}
	
	function selectPermission() {
		var permissionId = $('input:radio[name="permRadio"]:checked').val();
		if(isNoB(permissionId)){
			return;
		}
		var perName = $("#permName" + permissionId + "").val();
		$("#permId").val(permissionId);
		$("#permissionName").val(perName);
		$id("btnUnBind").show();
		permDialog.dialog("close");
	}
	
	//查询
	function queryByScope(scope){
		var url = $("#url-"+scope).val();
		var name = $("#name-"+scope).val();
		var filterItems = {
			scope : scope,
			url : url,
			name : name
		};
		var postData = {
			filterStr : JSON.encode(filterItems, true)
		};
		$('#list_' + scope).jqGrid('setGridParam', {
			datatype : 'json',
			postData : postData, // 发送数据
			page : 1
		}).trigger("reloadGrid"); // 重新载入
	}

	function deleteResource() {
		var scope = getCurrentScope();
		var delMap = {
			"resourceIds" : []
		};
		var del_resource = jQuery('#list_' + scope).jqGrid('getGridParam', 'selarrrow');
		var resNames = "";
		for (var i = 0; i < del_resource.length; i++) {
			var resourceId = jQuery('#list_' + scope).jqGrid('getCell', del_resource[i], 'id');
			delMap["resourceIds"].add(resourceId);
			resNames+=","+jQuery('#list_' + scope).jqGrid('getCell', del_resource[i], 'name');
		}
		if(resNames.length>0)
		resNames = resNames.substring(1,resNames.length);
		if (parseInt(delMap["resourceIds"].length) == 0) {
			Layer.warning("请选择操作对象");
			return;
		}
		var theLayer = Layer.confirm('确定要删除('+resNames+')资源吗', function() {
			var ajax = Ajax.post("/res/sysRes/delete/do");
			ajax.data(delMap);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					$('#list_' + scope).jqGrid('setGridParam', {
						datatype : 'json',
						page : 1
					}).trigger("reloadGrid"); // 重新载入
					theLayer.hide();
				} else {
					theLayer.hide();
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
		});
	}
	
	function initDialog() {
		permDialog = $id("perList").dialog({
			autoOpen : false,
			height : Math.min(500, $(window).height()),
			width : Math.min(800, $(window).width()),
			modal : true,
			buttons : {
				"确定" : function() {
					selectPermission();
				},
				"取消" : function() {
					permDialog.dialog("close");
				}
			},
			close : function() {
				permDialog.dialog("close");
			}
		});
		resDialog = $id("resDialog").dialog({
			autoOpen : false,
			title : "保存资源",
			height : Math.min(300, $(window).height()),
			width : Math.min(500, $(window).width()),
			modal : true,
			buttons : {
				"保存" : function() {
					saveOrUpdatePermAndRes();
				},
				"取消" : function() {
					resDialog.dialog("close");
				}
			},
			close : function() {
				formProxy.hideMessages();
				resDialog.dialog("close");
			}
		});
	}
	
	$(function() {
		initDialog();
		initData();
		
		//页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 55,
			allowTopResize : false,
			onresize : hideLayoutTogglers
		});
		
		hideLayoutTogglers();
		//隐藏布局north分割线
		$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
		
		jqTabsCtrl = $("#resourceTabs").tabs();
		jqTabsCtrl.on("tabsactivate", function(event, ui) {
			currentPanl = ui.newPanel.selector;
		});

		$id("btnBind").click(openBindPermDialog);
		$id("btnUnBind").click(unBindPerm);
		// 页面自适应
		winSizeMonitor.start(adjustCtrlsSize);
	});
	
	</script>
</body>
<script id="window_save" type="text/html" title="sava_window">
          
</script>
<script id="permTpl" type="text/html" title="permList">
	<div >
		{{# for(var i = 0, len = d.length; i < len; i++){ }}
			<div id="radioset" style="float: left;padding: 15px;">
			<input type="radio" id="radio{{d[i].id}}" name="permRadio" value={{d[i].id}} >
			<input type="hidden" id="permName{{d[i].id}}"  value={{ d[i].name}}>
			<label for="radio{{d[i].id}}">{{ d[i].name }}</label>
			</div>
		{{# } }}
	</div>
</script>
</html>
