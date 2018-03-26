<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>模块管理</title>
</head>
<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding:4px;vertical-align: bottom;">
		<div class="simple block">
			<div class="header">
				<label>资源模块管理   &gt; 模块管理</label>
			</div>
		</div>
	</div>
	
	<div id="mainPanel" class="ui-layout-center">
	<div id="tabsSiteModule" class="noBorder">
		<ul id="urlSiteModule">
			<li data-role="mall"><a href="#tab_mall">商城</a></li>
			<li data-role="shop"><a href="#tab_shop">店铺</a></li>
			<li style="display: none;" data-role="agency"><a href="#tab_agency">代理处</a></li>
		</ul>
		<div id="tab_mall">
			<div  class="ui-layout-north" style="padding: 0;">
				<div class="filter section">
					<div class="filter row">
						<button id="btnAddMall_openDialog" class="normal button">新增</button>
						<span class="normal spacer"></span>
						<button id="btnDelMall" class="normal button">批量删除</button>
						<div class="normal group right aligned">
							<label class="normal label">模块名称</label> <input id="siteModuleMallName" class="normal input one half wide" /> 
							<span class="vt divider"></span>
							<button id="btnQuery_MallByName" class="normal button">查询</button>
						</div>
					</div>
				</div>
			</div>
			<div id="mall_grid" >
				<table id="malllist"></table>
				<div id="mallpager"></div>
			</div>
		</div>
		<div id="tab_shop">
			<div  class="ui-layout-north" style="padding: 0px;">
				<div class="filter section">
					<div class="filter row">
						<button id="btnAddShop_openDialog" class="normal button">新增</button>
						<span class="normal spacer"></span>
						<button id="btnDelShop" class="normal button">批量删除</button>
						<span class="normal spacer"></span>
						<div class="normal group right aligned">
							<label class="normal label">模块名称</label> <input id="siteModuleShopName" class="normal input one half wide" /> 
							<span class="vt divider"></span>
							<button id="btnQuery_ShopByName" class="normal button">查询</button>
						</div>
					</div>
				</div>
			</div>
			<div id="shop_grid" >
				<table id="shoplist" ></table>
				<div id="shoppager"></div>
			</div>
		</div>
		<!-- 新增代理处 -->
		<div style="display: none;" id="tab_agency">
			<div  class="ui-layout-north" style="padding: 0px;">
				<div class="filter section">
					<div class="filter row">
						<button id="addAgencyMduBtn" class="normal button">新增</button>
						<span class="normal spacer"></span>
						<button id="batchDelAgencyMduBtn" class="normal button">批量删除</button>
						<span class="normal spacer"></span>
						<div class="normal group right aligned">
							<label class="normal label">模块名称</label>
							<input id="moduleName_agency" class="normal input one half wide" /> 
							<span class="vt divider"></span>
							<button id="agencyQueryBtn" class="normal button">查询</button>
						</div>
					</div>
				</div>
			</div>
			<div id="agency_grid" >
				<table id="agencyList" ></table>
				<div id="agencyPager"></div>
			</div>
		</div>
		
	</div>
	</div>
	
    <div style="display: none">
    	<input type="hidden"  id="siteModuIconUuid" />
		<input type="hidden"  id="siteModuIconUsage" />
		<input type="hidden"  id="siteModuIconPath" />
    </div>
   	
   	<!-- dialog -->
    <div id="siteModuDialog"  style="display: none">
	    <div class="form">
	       <div class="field row">
	            <label class="field label required one half wide" >模块名称</label>
	            <input type="text"  id="siteModuName" class="field value two wide " >
	            <input type="hidden"  id="siteModuId">
	        	<input type="hidden"  id="siteModuScope">
	        	<input type="hidden"  id="siteModuSeqNo">
	       </div>
			<div class="field row" style="height:100px;">
	          <label class="field label one half wide" >描述</label>
	          <textarea id="siteModuDesc" class="field value two wide" style="height:80px;" ></textarea>
	       </div>
	       <div class="field row">
				<label class="field label one half wide">图片</label>
				<input name="file" type="file" id="fileToUploadFileIcon" multiple="multiple" class="field value two wide "  /> 
		        <button class="normal button" style="float:right;position:absolute;right:0;margin-right:20px;margin-top:6px;" id="btnfileToUploadFileIcon">上传</button>
		   </div>
		   <div class="field row " style="height : 80px;">
	        	<label class="field label  one half wide">图片预览</label>
	        	<img id="siteModuIconImg" src="" style="border:1px solid #EEE;" height="80px" width="120px" />
	       </div>
	    </div>
     </div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	
<script type="text/javascript"> 
	//------------------------初始化变量-------------------------
	var jqTabsCtrl;
	var currentPanl;
	// 初始化mallGrid;
	var mallGrid;
	// 初始化shopGrid;
	var shopGrid;
	// 代理处Grid
	var agencyGrid;
	// 初始化新增siteModule dialog
	var siteModuleDialogNew;
	var siteModuDialogEdit;
	var siteModuDialogView;
	var curAction;
	var isExsitName = true;
	var siteModuVName = "";
	
	// ------------------------验证表单-------------------------
	// 实例化新增模块表单代理
	var siteModuleFormProxy = FormProxy.newOne();
	// 注册表单控件
	siteModuleFormProxy.addField({
		id : "siteModuName",
		required : true,
		rules : [ "maxLength[30]", {
			rule : function(idOrName, type, rawValue, curData) {
				if (curAction == "mod") {
					if (!isNoB(siteModuVName) && siteModuVName == rawValue) {
						return true;
					}
				}
				//
				isExsitSiteModuByName(rawValue);
				if (isExsitName) {
					$(".ui-button-text").each(function() {
						if ($(this).html() == "保存") {
							var btn = $(this).parent();
							$(btn).prop("disabled", true);
							$(btn).css("cursor", "default");
							$(btn).css("opacity", "0.5");
						}
					});
					return false;
				}
				$(".ui-button-text").each(function() {
					if ($(this).html() == "保存") {
						var btn = $(this).parent();
						$(btn).prop("disabled", false);
						$(btn).css("cursor", "pointer");
						$(btn).css("opacity", "");
					}
				});
				return true;
			},
			message : "名称被占用！"
		} ]
	});
	siteModuleFormProxy.addField({
		id : "siteModuDesc",
		rules : [ "maxLength[100]" ]
	});
	
	// ------------------------初始化方法-------------------------
	$(function() {
		// 加载Tab页
		jqTabsCtrl = $("#tabsSiteModule").tabs();
		jqTabsCtrl.on("tabsactivate", function(event, ui) {
			currentPanl = ui.newPanel.selector;
		});
		// 初始化SiteModuleDialog
		// initSiteModuleDialog();
	
		// 初始化数据
		loadData();
		// 页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 56,
			allowTopResize : false
		});
	
		// 页面自适应
		winSizeMonitor.start(adjustCtrlsSize);
		// 绑定查询商城模块按钮
		$id("btnQuery_MallByName").click(function() {
			var name = $id("siteModuleMallName").val();
			mallGrid.jqGrid("setGridParam", {
				postData : {
					filterStr : JSON.encode({
						"scope" : "mall",
						"name" : name
					}, true)
				},
				page : 1,
				sortorder : "desc"
			}).trigger("reloadGrid");
		});
	
		// 绑定查询店铺模块按钮
		$id("btnQuery_ShopByName").click(function() {
			var name = $id("siteModuleShopName").val();
			shopGrid.jqGrid("setGridParam", {
				postData : {
					filterStr : JSON.encode({
						"scope" : "shop",
						"name" : name
					}, true)
				},
				page : 1,
				sortorder : "desc"
			}).trigger("reloadGrid");
		});
	
		// 绑定查询店铺模块按钮
		$id("agencyQueryBtn").click(function() {
			var name = $id("moduleName_agency").val();
			agencyGrid.jqGrid("setGridParam", {
				postData : {
					filterStr : JSON.encode({
						"scope" : "agency",
						"name" : name
					}, true)
				},
				page : 1,
				sortorder : "desc"
			}).trigger("reloadGrid");
		});
	
		// 绑定添加商城模块按钮
		$id("btnAddMall_openDialog").click(siteModuDialogAddOpen);
		// 绑定添加店铺模块按钮
		$id("btnAddShop_openDialog").click(siteModuDialogAddOpen);
		// 绑定添加代理处模块按钮
		$id("addAgencyMduBtn").click(siteModuDialogAddOpen);
		// 绑定删除商城模块按钮
		$id("btnDelMall").click(deleteSiteModule);
		// 绑定删除店铺模块按钮
		$id("btnDelShop").click(deleteSiteModule);
		// 绑定删除代理处模块按钮
		$id("batchDelAgencyMduBtn").click(deleteSiteModule);
		// 绑定商城模块功能管理
		$id("btnManageMallFuncSite")
				.click(
						function() {
							window.location.href = getAppUrl("/res/siteFunc/mgmt/jsp?siteModuFuncId=all");
						});
		// 绑定店铺模块功能管理
		$id("btnManageShopFuncSite")
				.click(
						function() {
							window.location.href = getAppUrl("/res/siteFunc/mgmt/jsp?siteModuFuncId=all");
						});
		// 绑定新增上传按钮
		$id("btnfileToUploadFileIcon").click(function() {
			fileToUploadFileIcon("fileToUploadFileIcon", "new");
		});
		// 文件上传
		initFileUpload("fileToUploadFileIcon");
		$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
	});
	
	// ------------------------Grid定义-------------------------
	var mallGridHelper = JqGridHelper.newOne("");
	var shopGridHelper = JqGridHelper.newOne("");
	var agencyGridHelper = JqGridHelper.newOne("");
	//
	function loadData() {
		// 初始化商场Tab数据
		mallGrid = $("#malllist")
				.jqGrid(
						{
							height : "100%",
							width : "100%",
							url : getAppUrl("/res/siteModu/list/get"),
							contentType : 'application/json',
							mtype : "post",
							datatype : 'json',
							postData : {
								filterStr : JSON.encode({
									"scope" : "mall"
								}, true)
							},
							colNames : [ "id", "模块名称", "描述", "操作" ],
							colModel : [
									{
										name : "id",
										index : "id",
										hidden : true
									},
									{
										name : "name",
										index : "name",
										width : 100,
										align : "left"
									},
									{
										name : "desc",
										index : "desc",
										width : 300,
										align : "left"
									},
									{
										name : 'id',
										index : 'id',
										align : "left",
										formatter : function(cellValue, option,
												rowObject) {
											return "[<a class='item' href='javascript:void(0);' onclick='toMallSiteFuncOpen("
													+ JSON.stringify(rowObject)
													+ ")' >模块功能管理</a>]"
													+ "<span class='chs spaceholder'></span>[<a class='item' href='javascript:void(0);' onclick='siteModuleDialogViewOpen("
													+ JSON.stringify(rowObject)
													+ ")' >查看</a>]"
													+ "<span class='chs spaceholder'></span>[ <a class='item' href='javascript:void(0);' onclick='siteModuleDialogUpdateOpen("
													+ JSON.stringify(rowObject)
													+ ")' >修改</a>]"
													+ "<span class='chs spaceholder'></span>[ <a  class='item' href='javascript:void(0);' onclick='btnDel("
													+ JSON.stringify(rowObject)
													+ ")' >删除</a>]";
										},
										width : 200,
										align : "center"
									} ],
							// rowList:[10,20,30],
							multiselect : true,
							multikey : 'ctrlKey',
							pager : "#mallpager", // 分页div
							loadComplete : function(gridData) {
								mallGridHelper.cacheData(gridData);
	
							},
							ondblClickRow : function(rowId) {
								var siteModuMap = mallGridHelper.getRowData(rowId)
								siteModuleDialogViewOpen(siteModuMap);
							}
						});
		// 初始化店铺Tab数据
		shopGrid = $("#shoplist")
				.jqGrid(
						{
							deepempty : true,
							height : "100%",
							width : "100%",
							url : getAppUrl("/res/siteModu/list/get"),
							contentType : 'application/json',
							mtype : "post",
							datatype : 'json',
							postData : {
								filterStr : JSON.encode({
									"scope" : "shop"
								}, true)
							},
							colNames : [ "id", "模块名称", "描述", "操作" ],
							colModel : [
									{
										name : "id",
										index : "id",
										hidden : true
									},
									{
										name : "name",
										index : "name",
										width : 100,
										align : "left"
									},
									{
										name : "desc",
										index : "desc",
										width : 300,
										align : "left"
									},
									{
										name : 'id',
										index : 'id',
										align : "left",
										formatter : function(cellValue, option,
												rowObject) {
											return "[<a class='item' href='javascript:void(0);' onclick='toShopSiteFuncOpen("
													+ JSON.stringify(rowObject)
													+ ")' >模块功能管理</a>]"
													+ "<span class='chs spaceholder'></span>[<a class='item' href='javascript:void(0);' onclick='siteModuleDialogViewOpen("
													+ JSON.stringify(rowObject)
													+ ")' >查看</a>]"
													+ "<span class='chs spaceholder'></span>[ <a class='item' href='javascript:void(0);' onclick='siteModuleDialogUpdateOpen("
													+ JSON.stringify(rowObject)
													+ ")' >修改</a>]"
													+ "<span class='chs spaceholder'></span>[ <a  class='item' href='javascript:void(0);' onclick='btnDel("
													+ JSON.stringify(rowObject)
													+ ")' >删除</a>]";
										},
										width : 200,
										align : "center"
									} ],
							multiselect : true,
							multikey : 'ctrlKey',
							pager : "#shoppager", // 分页div
							loadComplete : function(gridData) {
								shopGridHelper.cacheData(gridData);
	
							},
							ondblClickRow : function(rowId) {
								var siteModuMap = shopGridHelper.getRowData(rowId)
								siteModuleDialogViewOpen(siteModuMap);
							}
						});
	
		// 初始化代理处Tab数据
		agencyGrid = $("#agencyList")
				.jqGrid(
						{
							deepempty : true,
							height : "100%",
							width : "100%",
							url : getAppUrl("/res/siteModu/list/get"),
							contentType : 'application/json',
							mtype : "post",
							datatype : 'json',
							postData : {
								filterStr : JSON.encode({
									"scope" : "agency"
								}, true)
							},
							colNames : [ "id", "模块名称", "描述", "操作" ],
							colModel : [
									{
										name : "id",
										index : "id",
										hidden : true
									},
									{
										name : "name",
										index : "name",
										width : 100,
										align : "left"
									},
									{
										name : "desc",
										index : "desc",
										width : 300,
										align : "left"
									},
									{
										name : 'id',
										index : 'id',
										align : "left",
										formatter : function(cellValue, option,
												rowObject) {
											return "[<a class='item' href='javascript:void(0);' onclick='toShopSiteFuncOpen("
													+ JSON.stringify(rowObject)
													+ ")' >模块功能管理</a>]"
													+ "<span class='chs spaceholder'></span>[<a class='item' href='javascript:void(0);' onclick='siteModuleDialogViewOpen("
													+ JSON.stringify(rowObject)
													+ ")' >查看</a>]"
													+ "<span class='chs spaceholder'></span>[ <a class='item' href='javascript:void(0);' onclick='siteModuleDialogUpdateOpen("
													+ JSON.stringify(rowObject)
													+ ")' >修改</a>]"
													+ "<span class='chs spaceholder'></span>[ <a  class='item' href='javascript:void(0);' onclick='btnDel("
													+ JSON.stringify(rowObject)
													+ ")' >删除</a>]";
										},
										width : 200,
										align : "center"
									} ],
							multiselect : true,
							multikey : 'ctrlKey',
							pager : "#agencyPager", // 分页div
							loadComplete : function(gridData) {
								agencyGridHelper.cacheData(gridData);
	
							},
							ondblClickRow : function(rowId) {
								var siteModuMap = agencyGridHelper
										.getRowData(rowId)
								siteModuleDialogViewOpen(siteModuMap);
							}
						});
	
	}
	
	// ------------------------Dialog-------------------------
	// 清空Dialog
	function clearDialog() {
		$id("siteModuName").val("");
		$id("siteModuDesc").val("");
		$id("siteModuIconUuid").val("");
		$id("siteModuIconUsage").val("");
		$id("siteModuIconPath").val("");
		$("#siteModuIconImg").removeAttr("src");
		$id("siteModuIconUuid").val("");
		$id("siteModuIconUsage").val("");
		$id("siteModuIconPath").val("");
		$("input", "#siteModuDialog").prop("disabled", false);
		$("button", "#siteModuDialog").prop("disabled", false);
		$("textarea", "#siteModuDialog").prop("disabled", false);
		isExsitName = true;
		siteModuVName = "";
		siteModuleFormProxy.hideMessages();
	}
	// 循环方法
	function defaultMethod() {
		if (curAction == "add") {
			createSiteModule();
		}
		if (curAction == "edit") {
			updateSiteModule();
		}
		if (curAction == "view") {
			siteModuleDialogUpdateOpen(tempAttr);
		}
	}
	
	// 弹出增加模块
	function siteModuDialogAddOpen() {
		clearDialog();
		initSiteModuDialogAdd();
		curAction = "add";
		siteModuleDialogNew.dialog("open");
	}
	// 初始化添加模块Dialog
	function initSiteModuDialogAdd() {
		// 为新增siteModuleDialogNew进行赋值
		siteModuleDialogNew = $("#siteModuDialog").dialog({
			autoOpen : false,
			width : Math.min(550, $window.width()),
			height : Math.min(400, $window.height()),
			modal : true,
			title : "新增模块",
			buttons : {
				"保存" : defaultMethod,
				"取消" : function() {
					clearDialog();
					siteModuleDialogNew.dialog("close");
				}
			},
			close : function() {
	
				clearDialog()
			}
		});
	}
	// 查看模块窗口打开
	function siteModuleDialogViewOpen(obj) {
		clearDialog();
		curAction = "view";
		tempAttr = obj;
		$id("siteModuName").val(tempAttr.name);
		$id("siteModuDesc").val(tempAttr.desc);
		$id("siteModuId").val(tempAttr.id);
		$id("siteModuScope").val(tempAttr.scope);
		$id("siteModuIconUuid").val(tempAttr.iconUuid);
		$id("siteModuIconUsage").val(tempAttr.iconUsage);
		$id("siteModuIconPath").val(tempAttr.iconPath);
		$id("siteModuSeqNo").val(tempAttr.seqNo);
		$("#siteModuIconImg").attr("src", tempAttr.iconPath);
		$("input", "#siteModuDialog").prop("disabled", true);
		$("button", "#siteModuDialog").prop("disabled", true);
		$("textarea", "#siteModuDialog").prop("disabled", true);
		siteModuVName = tempAttr.name;
		initSiteModuDialogView();
		siteModuDialogView.dialog("open");
	}
	// 初始查看模块Dialog
	function initSiteModuDialogView() {
		// 为新增siteModuleDialogNew进行赋值
		siteModuDialogView = $("#siteModuDialog").dialog({
			autoOpen : false,
			width : Math.min(550, $window.width()),
			height : Math.min(400, $window.height()),
			modal : true,
			title : "查看模块",
			buttons : {
				"修改>" : defaultMethod,
				"关闭" : function() {
					clearDialog();
					siteModuDialogView.dialog("close");
				}
			},
			close : function() {
				clearDialog()
			}
		});
	}
	// 编辑模块Dialog打开
	function siteModuleDialogUpdateOpen(obj) {
		clearDialog();
		tempAttr = obj;
		tempId = obj.id;
		curAction = "edit";
		initSiteModuDialogUpdate();
		siteModuVName = tempAttr.name;
		$id("siteModuName").val(tempAttr.name);
		$id("siteModuDesc").val(tempAttr.desc);
		$id("siteModuId").val(tempAttr.id);
		$id("siteModuScope").val(tempAttr.scope);
		$id("siteModuIconUuid").val(tempAttr.iconUuid);
		$id("siteModuIconUsage").val(tempAttr.iconUsage);
		$id("siteModuIconPath").val(tempAttr.iconPath);
		$("#siteModuIconImg").attr("src", tempAttr.iconPath);
		$id("siteModuSeqNo").val(tempAttr.seqNo);
		siteModuDialogEdit.dialog("open");
	}
	// 初始化编辑模块Dialog
	function initSiteModuDialogUpdate() {
		// 为新增siteModuleDialogNew进行赋值
		siteModuDialogEdit = $("#siteModuDialog").dialog({
			autoOpen : false,
			width : Math.min(550, $window.width()),
			height : Math.min(400, $window.height()),
			modal : true,
			title : "更新模块",
			buttons : {
				"保存 " : defaultMethod,
				"取消" : function() {
					clearDialog();
					siteModuDialogEdit.dialog("close");
				}
			},
			close : function() {
				clearDialog()
			}
		});
	}
	
	// ------------------------对SiteModule 添加 修改 删除 选择删除 方法------------------------------------
	// 新增站点模块
	function createSiteModule() {
		var scope = $id("urlSiteModule").find("li[aria-selected='true']").attr(
				"data-role");
		var vldResult = siteModuleFormProxy.validateAll();
		if (!vldResult) {
			return;
		}
		var postData = {
			scope : scope,
			name : $id("siteModuName").val(),
			desc : $id("siteModuDesc").val(),
			iconUuid : $id("siteModuIconUuid").val(),
			iconUsage : $id("siteModuIconUsage").val(),
			iconPath : $id("siteModuIconPath").val(),
		};
		var ajax = Ajax.post("/res/siteModu/save/do");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
				if (scope == "mall") {
					mallGrid.jqGrid("setGridParam", {
						page : 1,
						sortname : "id",
						sortorder : "asc"
					}).trigger("reloadGrid");
				} else if (scope == "shop") {
					shopGrid.jqGrid("setGridParam", {
						page : 1,
						sortname : "id",
						sortorder : "asc"
					}).trigger("reloadGrid");
				} else if (scope == "agency") {
					agencyGrid.jqGrid("setGridParam", {
						page : 1,
						sortname : "id",
						sortorder : "asc"
					}).trigger("reloadGrid");
				}
				tempAttr = result.data;
				siteModuleDialogViewOpen(tempAttr);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	
	// 更新站点模块
	function updateSiteModule() {
		var scope = $id("urlSiteModule").find("li[aria-selected='true']").attr(
				"data-role");
		var vldResult = siteModuleFormProxy.validateAll();
		if (!vldResult) {
			return;
		}
		var postData = {
			scope : scope,
			name : $id("siteModuName").val(),
			desc : $id("siteModuDesc").val(),
			iconUuid : $id("siteModuIconUuid").val(),
			iconUsage : $id("siteModuIconUsage").val(),
			iconPath : $id("siteModuIconPath").val(),
			id : $id("siteModuId").val(),
			seqNo : $id("siteModuSeqNo").val()
	
		};
		var loaderLayer = Layer.loading("正在提交数据...");
		var ajax = Ajax.post("/res/siteModu/update/do");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
	
				if (scope == "mall") {
					mallGrid.jqGrid("setGridParam", {
						page : 1,
						sortname : "id",
						sortorder : "asc"
					}).trigger("reloadGrid");
				} else if (scope == "shop") {
					shopGrid.jqGrid("setGridParam", {
						page : 1,
						sortname : "id",
						sortorder : "asc"
					}).trigger("reloadGrid");
				} else if (scope == "agency") {
					agencyGrid.jqGrid("setGridParam", {
						page : 1,
						sortname : "id",
						sortorder : "asc"
					}).trigger("reloadGrid");
				}
				Layer.msgSuccess(result.message);
				tempAttr = result.data;
				siteModuleDialogViewOpen(tempAttr);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.always(function() {
			// 隐藏等待提示框
			loaderLayer.hide();
		});
		ajax.go();
	}
	
	// 删除功能模块
	function btnDel(obj) {
		var scope = $id("urlSiteModule").find("li[aria-selected='true']").attr(
				"data-role");
		var theLayer = Layer.confirm('模块下的功能所绑定的资源将将全部解绑，模块下的功能将全部删除，确定要删除该模块吗？',
				function() {
					var postData = {
						id : obj.id
					};
					var ajax = Ajax.post("/res/siteModu/delete/do");
					ajax.data(postData);
					ajax.done(function(result, jqXhr) {
						if (result.type == "info") {
							theLayer.hide();
							Layer.msgSuccess(result.message);
							if (scope == "mall") {
								mallGrid.jqGrid("setGridParam", {
									page : 1,
									sortname : "id",
									sortorder : "asc"
								}).trigger("reloadGrid");
							} else if (scope == "shop") {
								shopGrid.jqGrid("setGridParam", {
									page : 1,
									sortname : "id",
									sortorder : "asc"
								}).trigger("reloadGrid");
							} else if (scope == "agency") {
								agencyGrid.jqGrid("setGridParam", {
									page : 1,
									sortname : "id",
									sortorder : "asc"
								}).trigger("reloadGrid");
							}
						} else {
							theLayer.hide();
							Layer.msgWarning(result.message);
						}
					});
					ajax.go();
				});
	}
	
	// 选择删除
	function deleteSiteModule() {
		var scope = $id("urlSiteModule").find("li[aria-selected='true']").attr(
				"data-role");
		var ids = "";
		if (scope == "mall") {
			ids = mallGrid.jqGrid("getGridParam", "selarrrow");
		} else if (scope == "shop") {
			ids = shopGrid.jqGrid("getGridParam", "selarrrow");
		} else if (scope == "agency") {
			ids = agencyGrid.jqGrid("getGridParam", "selarrrow");
		}
		//
		if (ids == "") {
			Layer.info("请先选择要删除的记录");
			return;
		}
		var postData = [];
		for (var i = 0; i < ids.length; i++) {
			postData.add(ParseInt(ids[i]));
		}
		var theLayer = Layer.confirm('模块下的功能所绑定的资源将将全部解绑，模块下的功能将全部删除，确定要删除该模块吗？',
				function() {
					var ajax = Ajax.post("/res/siteModu/delete/by/ids");
					ajax.data(postData);
					ajax.done(function(result, jqXhr) {
						if (result.type == "info") {
							theLayer.hide();
							Layer.msgSuccess(result.message);
							if (scope == "mall") {
								mallGrid.jqGrid("setGridParam", {
									page : 1,
									sortname : "id",
									sortorder : "asc"
								}).trigger("reloadGrid");
							} else if (scope == "shop") {
								shopGrid.jqGrid("setGridParam", {
									page : 1,
									sortname : "id",
									sortorder : "asc"
								}).trigger("reloadGrid");
							} else if (scope == "agency") {
								agencyGrid.jqGrid("setGridParam", {
									page : 1,
									sortname : "id",
									sortorder : "asc"
								}).trigger("reloadGrid");
							}
						} else {
							theLayer.hide();
							Layer.msgWarning(result.message);
						}
					});
					ajax.go();
				});
	}
	
	function getCurrentScope() {
		var theTabLink = $id("tabsSiteModule").find(
				"ul>li.ui-tabs-active.ui-state-active").find("a:first");
		var scope = "sys";
		//
		if (theTabLink) {
			var href = $(theTabLink).attr("href");
			if (!isNoB(href)) {
				scope = href.substring(5, href.length);
			}
		}
		return scope;
	}
	
	// 检查颜色名字是否可用
	function isExsitSiteModuByName(name) {
		var ajax = Ajax.post("/res/siteModu/isExsit/by/name/do");
		var scope = getCurrentScope();
		//
		ajax.data({
			scope : scope,
			name : name
		});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			isExsitName = result.data;
		});
		ajax.go();
	}
	// ------------------------上传-------------------------
	function fileToUploadFileIcon(fileId, status) {
		var fileUuidToUpdate = $id("siteModuIconUuid").val();
		if (isNoB(fileUuidToUpdate)) {
			var formData = {
				usage : "image.icon"
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
			// 自定义数据
			formData : formData,
			done : function(e, result) {
				var resultInfo = result.result;
				if (resultInfo.type == "info") {
					$id("siteModuIconUuid").val(resultInfo.data.files[0].fileUuid);
					$id("siteModuIconUsage")
							.val(resultInfo.data.files[0].fileUsage);
					$id("siteModuIconPath").val(
							resultInfo.data.files[0].fileBrowseUrl);
					$("#siteModuIconImg").attr(
							"src",
							resultInfo.data.files[0].fileBrowseUrl + "?"
									+ new Date().getTime());
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
	// ------------------------other -------------------------
	// 管理功能模块
	function toMallSiteFuncOpen(obj) {
		window.location.href = getAppUrl("/res/siteFunc/mgmt/jsp?siteModuFuncId="
				+ obj.id + "&scope=mall");
	}
	
	function toShopSiteFuncOpen(obj) {
		window.location.href = getAppUrl("/res/siteFunc/mgmt/jsp?siteModuFuncId="
				+ obj.id + "&scope=shop");
	}
	// ------------------------调整控件大小 -------------------------
	// 调整控件大小
	function adjustCtrlsSize() {
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		console.log("mainWidth:" + mainWidth + ", " + "mainHeight:" + mainHeight);
		//
		var tabsCtrlWidth = mainWidth - 10;
		var tabsCtrlHeight = mainHeight;
		jqTabsCtrl.width(tabsCtrlWidth);
		var tabsHeaderHeight = $(">[role=tablist]", jqTabsCtrl).height();
		var tabsPanels = $(">[role=tabpanel]", jqTabsCtrl);
		tabsPanels.width(tabsCtrlWidth - 4 * 2);
		tabsPanels.height(tabsCtrlHeight - tabsHeaderHeight - 30);
		//
		var gridCtrlId, jqGridBox, headerHeight, pagerHeight;
		//
		if (currentPanl == "#tab_mall") {
			jqGridBox = $("#gbox_" + "malllist");
			headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox)
					.height();
			pagerHeight = $id("mallpager").height();
	
		} else if (currentPanl == "#tab_shop") {
			jqGridBox = $("#gbox_" + "shoplist");
			headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox)
					.height();
			pagerHeight = $id("shoppager").height();
		} else if (currentPanl == "#tab_agency") {
			jqGridBox = $("#gbox_" + "agencylist");
			headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox)
					.height();
			pagerHeight = $id("agencyPager").height();
		} else {
			gridCtrlId = "malllist";
			jqGridBox = $("#gbox_" + gridCtrlId);
			headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox)
					.height();
			pagerHeight = $id("mallpager").height();
		}
		//
		if (mallGrid) {
			mallGrid.setGridWidth(tabsCtrlWidth - 4 * 2);
			mallGrid.setGridHeight(tabsCtrlHeight - tabsHeaderHeight - 82
					- pagerHeight - headerHeight);
		}
		//
		if (shopGrid) {
			shopGrid.setGridWidth(tabsCtrlWidth - 4 * 2);
			shopGrid.setGridHeight(tabsCtrlHeight - tabsHeaderHeight - 82
					- pagerHeight - headerHeight);
		}
		//
		if (agencyGrid) {
			agencyGrid.setGridWidth(tabsCtrlWidth - 4 * 2);
			agencyGrid.setGridHeight(tabsCtrlHeight - tabsHeaderHeight - 82
					- pagerHeight - headerHeight);
		}
	
	}

</script>
</body>
</html>