<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>角色列表</title>
<style type="text/css">
	.ui-jqgrid tr.jqgrow td {
	  white-space: normal !important;
	  height:auto;
	  vertical-align:text-top;
	  padding-top:2px;
	 }
	 
	hr.divider{
		border-top: 0.5px solid #D3D3D3;
	}
	
	.list{
		list-style:none;
	    margin: 0px;
	    width: 100%;
	    padding-left: 18px;
	    display: inline-block;
	}
	
	.list li{
		float:left;
		width: 180px;
		height: 40px;
		line-height: 38px;
		vertical-align: middle;
	}

	.title {
		font-weight: bold;
		font-size: 16px;
	}
</style>
</head>
<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding: 4px; vertical-align: bottom;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnAdd" class="button">新增角色</button>
					<span class="spacer"></span>
					<button id="btnDelBatch" class="button">批量删除</button>
				</div>
			</div>
		</div>
	</div>
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<table id="roleList"></table>
		<div id="rolePager"></div>
	</div>
	<div id="roleDialog"></div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
	var scope = "shop";
	var addList = [];
	var delList = [];
	var nameMap = new KeyMap();
	nameMap.add("admin", "店铺管理员");
	nameMap.add("member", "店铺会员");
	//权限最初始状态列表
	var origMap = new KeyMap();
	var roleGrid;
	var curAction;
	var roleDialog;
	var formProxy = FormProxy.newOne();
	var searchFormProxy = FormProxy.newOne();
	// 缓存当前jqGrid数据行数组
	var shopRoleGridHelper = JqGridHelper.newOne("");
	var tempRole = null;
	var tempRoleId = null;
	//唯一标识
	var uniqFlag = true;
	searchFormProxy.addField({
		id : "paramName",
		rules : [ "maxLength[30]"]
	});
	
	//加载数据列表
	function loadData() {
		roleGrid = $id("roleList")
				.jqGrid(
						{
							url : getAppUrl("/perm/roles/list/get/-shop"),
							contentType : 'application/json',
							mtype : "post",
							datatype : 'json',
							colNames : [ "id", "名称", "可分配", "操作" ],
							colModel : [
									{
										name : "id",
										index : "id",
										hidden : true
									},
									{
										name : "name",
										width : 150,
										align : 'left',
										formatter : function(cellValue, option, rowObject) {
											return nameMap.get(cellValue) == null ? cellValue : nameMap.get(cellValue);
										}
									},
									{
										name : "grantable",
										width : 150,
										align : 'left',
										formatter : function(cellValue, option, rowObject) {
											if(cellValue){
												return "是";
											}
											return "否";
										}
									},
									{
										name : 'id',
										index : 'id',
										formatter : function(cellValue, option, rowObject) {
											var name = rowObject.name;
											if(name == "admin"){
												return "<span> [<a href='javascript:void(0);' onclick='roleAdminDialogView("
												+ JSON.stringify(rowObject)
												+ ")' >查看</a>]</span>";
											}
											return "<span> [<a href='javascript:void(0);' onclick='roleDialogView("
													+ JSON.stringify(rowObject)
													+ ")' >查看</a>]</span><span>[<a href='javascript:void(0);' onclick='roleDialogEdit("
													+ JSON
															.stringify(rowObject)
													+ ")' >修改</a>]</span><span>[<a href='javascript:void(0);' onclick='deleteRole("
													+ JSON
															.stringify(rowObject)
													+ ")' >删除</a>]</span>";
										},
										width : "200",
										align : "center"
									} ],
							pager : "#rolePager",
							height:"auto",
							multiselect : true,
							multikey:'ctrlKey',
							loadComplete : function(gridData) { // JqGridHelper缓存最新的grid数据
								shopRoleGridHelper.cacheData(gridData);
								var callback = getCallbackAfterGridLoaded();
								if (isFunction(callback)) {
									callback();
								}
							},
							ondblClickRow: function(rowId) {
								var userMap = shopRoleGridHelper.getRowData(rowId)
								roleDialogView(userMap);
							}
						});

	}
	getCallbackAfterGridLoaded = function(){
	};
	
	//初始化dialogAdd
	function initDialogAdd(){
		$("div[aria-describedby='roleDialog']").find("span[class='ui-dialog-title']").html("新增角色");
		//
		roleDialog = $id("roleDialog").dialog({
			autoOpen : false,
			height : Math.min(500, $window.height()),
			width : Math.min(650, $window.width()),
			modal : true,
			buttons : {
				"保存" : defaultMethod,
				"取消" : function() {
					clearDialog();
					roleDialog.dialog("close");
				}
			},
			close : function() {
				clearDialog();
			}
		});
	}
	//初始化dialogView
	function initDialogView(){
		$("div[aria-describedby='roleDialog']").find("span[class='ui-dialog-title']").html("查看角色");
		//
		roleDialog = $id("roleDialog").dialog({
			autoOpen : false,
			height : Math.min(500, $window.height()),
			width : Math.min(650, $window.width()),
			modal : true,
			buttons : {
				"修改" : defaultMethod,
				"关闭" : function() {
					clearDialog();
					roleDialog.dialog("close");
				}
			},
			close : function() {
				clearDialog();
			}
		});
	}
	//初始化adminDialogView
	function initAdminDialogView() {
		$("div[aria-describedby='roleDialog']").find("span[class='ui-dialog-title']").html("查看角色");
		//
		roleDialog = $id("roleDialog").dialog({
			autoOpen : false,
			height : Math.min(500, $window.height()),
			width : Math.min(650, $window.width()),
			modal : true,
			buttons : {
				"关闭" : function() {
					clearDialog();
					roleDialog.dialog("close");
				}
			},
			close : function() {
				clearDialog();
			}
		});
	}
	//初始化dialogEdit
	function initDialogEdit(){
		$("div[aria-describedby='roleDialog']").find("span[class='ui-dialog-title']").html("修改角色");
		//
		roleDialog = $id("roleDialog").dialog({
			autoOpen : false,
			height : Math.min(500, $window.height()),
			width : Math.min(650, $window.width()),
			modal : true,
			buttons : {
				"保存" : defaultMethod,
				"取消" : function() {
					clearDialog();
					roleDialog.dialog("close");
				}
			},
			close : function() {
				clearDialog();
			}
		});
	}
	
	//
	function defaultMethod(){
		if(curAction == "add"){
			addRole();
		}
		if(curAction == "edit"){
			updateRole();
		}
		if(curAction == "view"){
			roleDialogEdit(tempRole);
		}
	}
	
	function roleDialogAdd(){
		clearDialog();
		curAction = "add";
		updatePermsStatus(tempRoleId);
		initDialogAdd();
		roleDialog.dialog("open");
	}
	
	//
	function roleDialogView(obj){
		clearDialog();
		curAction = "view";
		tempRole = obj;
		tempRoleId = obj.id;
		$id("name").val(obj.name);
		radioSet("grantable",obj.grantable);
		textSet("desc",obj.desc);
		updatePermsStatus(tempRoleId)
		initDialogView();	
		$("input", "#roleDialog").attr("disabled",true);
		$("select", "#roleDialog").attr("disabled",true);
		$("textarea", "#roleDialog").attr("disabled",true);
		roleDialog.dialog("open");
	}
	//
	function roleAdminDialogView(obj) {
		clearDialog();
		curAction = "view";
		tempRole = obj;
		tempRoleId = obj.id;
		$id("name").val(obj.name);
		radioSet("grantable",obj.grantable);
		textSet("desc",obj.desc);
		updatePermsStatus(tempRoleId)
		initAdminDialogView();
		$("input", "#roleDialog").attr("disabled", true);
		$("select", "#roleDialog").attr("disabled", true);
		$("textarea", "#roleDialog").attr("disabled", true);
		roleDialog.dialog("open");
	}
	
	//
	function roleDialogEdit(obj){
		clearDialog();
		curAction = "edit";
		tempRole = obj;
		tempRoleId = obj.id;
		$id("id").val(obj.id);
		$id("name").val(obj.name);
		radioSet("grantable",obj.grantable);
		textSet("desc",obj.desc);
		updatePermsStatus(obj.id);
		initDialogEdit();	
		roleDialog.dialog("open");
	}
	
	//
	function clearDialog(){
		curAction = null;
		tempRole = null;
		tempRoleId = null;
		addList = [];
		delList = [];
		formProxy.hideMessages();
		searchFormProxy.hideMessages();
		$id("id").val("");
		$id("name").val("");
		radioSet("grantable",1);
		textSet("desc","");
		$("input", "#roleDialog").attr("disabled",false);
		$("select", "#roleDialog").attr("disabled",false);
		$("textarea", "#roleDialog").attr("disabled",false);
	}
	
	//
	function initTpl(){
		var ajax = Ajax.post("/perm/modules/and/perms/get");
		ajax.data(scope);
    	ajax.done(function(result, jqXhr){
    		if(result.type== "info"){
    			var data = result.data;
    			var deliveryTplHtml = $id("deliveryTpl").html();
    			var htmlStr = laytpl(deliveryTplHtml).render(data);
    			$id("roleDialog").html(htmlStr);
    			formProxy.addField({
    				id : "name",
    				required : true,
    				rules : [ "maxLength[30]", {
    					rule : function(idOrName, type, rawValue, curData) {
    						if(tempRole == null){
    							validateName(rawValue);
    						}else{
    							if(tempRole.name != rawValue){
    								validateName(rawValue);
    							}
    						}
    						return uniqFlag;
    					},
    					message : "名称被占用！"
    				} ]
    			});
    			initDialogAdd();
    		}
    		else {
    			Layer.msgWarning(result.message);
    		}
    	});
    	ajax.go();
	}
	
	//更新权限状态
	function updatePermsStatus(id){
		//清空权限初始化状态
		origMap.clear();
		//添加权限初始状态，全部为false
		$("input:checkbox").each(function(i,p){
			var permId = $(p).attr("data-id");
			$("input:checkbox").removeAttr("checked");
			origMap.add(permId, false);
		});
		if(id != null){
			//根据查询结果勾选权限，并修改权限初始状态map数据
			var ajax = Ajax.post("/perm/perms/by/roleId/get");
			ajax.sync();
			ajax.params({"id" : ParseInt(id)});
	    	ajax.done(function(result, jqXhr){
	    		if(result.type== "info"){
	    			var data = result.data;
	    			var checkedList = data, len = checkedList.length;
	    			for(var i = 0; i < len; i++){
	    				var rp = checkedList[i], permId = rp.permId;
	    				$id("perm-"+permId).attr("checked", 'checked');
	    				origMap.set(permId, true);
	    			}
	    		}
	    		else {
	    			Layer.msgWarning(result.message);
	    		}
	    	});
	    	ajax.go();
		}
	}
	
	//
	function addRole(){
		var vldResult = formProxy.validateAll();
			if(vldResult){
				getAddAndDelList();
				var ajax = Ajax.post("/perm/role/add/do");
				var nameVal = $id("name").val();
				data = {
					name : nameVal,
					scope : scope,
					grantable : ParseInt(radioGet("grantable")),
					desc : textGet("desc"),
					subIdsAdded : addList,
					subIdsDeleted : delList,
					seqNo : 1
				};
				ajax.data(data);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);			
						//加载最新数据列表
						roleGrid.jqGrid().trigger("reloadGrid");
						tempRole = result.data;
						roleDialogView(tempRole);
					}
				});
				ajax.go();
			}
	}
	
	//
	function getAddAndDelList(){
		$("input:checkbox").each(function(i,p){
			var permId = $(p).attr("data-id");
			var oldStatus = origMap.get(permId);
			var newStatus = $(p).is(':checked');
			if(newStatus != oldStatus){
				if(newStatus){
					addList.add(ParseInt(permId));
				}
				if(oldStatus){
					delList.add(ParseInt(permId));
				}
			}
		});
	}
	
	function updateRole(){
		var vldResult = formProxy.validateAll();
		if(vldResult){
			getAddAndDelList();
			var ajax = Ajax.post("/perm/role/update/do");
			var idVal = $id("id").val();
			var nameVal = $id("name").val();
			data = {
				id : idVal,
				name : nameVal,
				scope : scope,
				grantable : ParseInt(radioGet("grantable")),
				desc : textGet("desc"),
				subIdsAdded : addList,
				subIdsDeleted : delList,
				seqNo : 1
			};
			ajax.data(data);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);			
					//加载最新数据列表
					roleGrid.jqGrid().trigger("reloadGrid");
					tempRole = result.data;
					roleDialogView(tempRole);
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
		}
	}
	
	//检查名字是否可用
	function validateName(name){
		var ajax = Ajax.post("/perm/role/uniq/get");
		ajax.sync();
		ajax.data({
			name : name,
			scope : scope
		});
		ajax.done(function(result, jqXhr) {
			if(result.type == "info"){
				uniqFlag = result.data;
			}
		});
		ajax.go(); 
	}
	
	//
	function deleteRole(obj){
		var id = obj.id;
		var name = nameMap.get(obj.name);
		var theLayer = Layer.confirm('确定要删除？', function() {
			//
			var hintBox = Layer.progress("正在删除...");
			var ajax = Ajax.post("/perm/role/delete/do");
			ajax.params({
				id : id
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					//
					roleGrid.jqGrid().trigger("reloadGrid");
				}else if(result.type == "warn"){
					deleteRoleAndUserRole(id);
				}
			});
			ajax.always(function() {
				hintBox.hide();
			});
			ajax.go();
		});
	}
	
	//
	function deleteRoleAndUserRole(id){
		var theLayer = Layer.confirm('该角色已被使用，确定要删除？', function() {
			//
			var hintBox = Layer.progress("正在删除...");
			var ajax = Ajax.post("/perm/role/and/userRole/delete/do");
			ajax.params({
				id : id
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var flag = result.data;
					if(flag){
						Layer.msgSuccess("删除成功！");
						//
						roleGrid.jqGrid().trigger("reloadGrid");
					}else{
						Layer.msgSuccess("删除失败！");
					}
				}
			});
			ajax.always(function() {
				hintBox.hide();
			});
			ajax.go();
		});
	}
	
	//查询
	function search() {
		var vldResult = searchFormProxy.validateAll();
		if (!vldResult) {
			return;
		}
		var nameVal = $id("paramName").val();
		roleGrid.jqGrid("setGridParam", {
			postData : {
				filterStr : JSON.encode({
					"name" : nameVal,
					"scope" : scope
				}, true)
			}
		}).trigger("reloadGrid");
	}
	
	//
	function deleteBatch(){
		var ids = roleGrid.jqGrid("getGridParam","selarrrow");
		var postData = [];
		for(var i = 0;i<ids.length;i++){
			postData.add(ParseInt(ids[i]));
		}
		if(ids==""){
			Toast.show("请选择要删除的数据！");
		}else{
			if(postData.contains(3)){
				Toast.show("店铺管理员不能被删除！");
				return;
			}
			var theLayer = Layer.confirm('确定要删除选择的数据吗？', function() {
				//
				var hintBox = Layer.progress("正在删除...");
				var ajax = Ajax.post("/perm/role/delete/batch/do");
				ajax.data(postData);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						//
						roleGrid.jqGrid().trigger("reloadGrid");
					}else if(result.type == "warn"){
						deleteBatchRoleAndUserRole(postData);
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
	function deleteBatchRoleAndUserRole(postData){
		var theLayer = Layer.confirm('选择的角色中有被使用，确定要删除？', function() {
			//
			var hintBox = Layer.progress("正在删除...");
			var ajax = Ajax.post("/perm/role/and/userRole/delete/batch/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var flag = result.data;
					if(flag){
						Layer.msgSuccess("删除成功！");
						//
						roleGrid.jqGrid().trigger("reloadGrid");
					}else{
						Layer.msgSuccess("删除失败！");
					}
				}
			});
			ajax.always(function() {
				hintBox.hide();
			});
			ajax.go();
		});
	}
	
	//调整控件大小
	function adjustCtrlsSize() {
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		//
		var gridCtrlId = "roleList";
		var jqGridBox = $("#gbox_" + gridCtrlId);
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
		var pagerHeight = $id("rolePager").height();
		roleGrid.setGridWidth(mainWidth - 1);
		roleGrid.setGridHeight(mainHeight - headerHeight - pagerHeight - 3);
	}

	//---------------------------------------------------初始化--------------------------------------------------------
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
		
		loadData();
		initTpl();	
		$id("btnAdd").click(roleDialogAdd);
// 		$id("btnQuery").click(search);
		$id("btnDelBatch").click(deleteBatch);
		//
		roleGrid.bindKeys();
		
		// 页面自适应
		winSizeMonitor.start(adjustCtrlsSize);
	});
</script>
</body>
<!-- layTpl begin -->
<!-- 属性模板 -->
<script id="deliveryTpl" type="text/html">
	<div class="form" style="margin-bottom: 8px;">
		<input type="hidden" id="id" />
		<div class="field row" style="float: left;">
			<label class="field label one wide required">名称</label> 
			<input class="field value one half wide" type="text" id="name" title="请输入角色名称" />
		</div>
		<div class="field row">
			<label class="field label one wide required">可分配</label> 
			<input id="support" type="radio" name="grantable" value="1" checked="checked" /><label for = "support">是</label>
			<input id="opposed" type="radio" name="grantable" value="0" /><label for = "opposed">否</label>
		</div>
		<div class="field row" style="height:80px;">
			<label class="field label one wide">描述</label>
			<textarea class="field value three wide" id="desc" maxlength="250" style="height:80px;"></textarea>
		</div>
	</div>
	<span class="normal hr divider"></span>
	<div class="filter section">
		{{# var modules = d, mdLen = modules.length; }}
		{{# for(var i = 0; i < mdLen; i++){ }}
			{{# var module = modules[i], perms = module.permissions, psLen = perms.length; }}
			<div class='module'>
			<div><label class='title'>{{ module.name }}</label><hr class='divider'></div>
			<ul class='list'>
			{{# for (var j = 0; j < psLen; j++) { }}
				<li>
					<input id="perm-{{ perms[j].id }}" data-id="{{ perms[j].id }}" name=perm type="checkbox" /> 
					<label for="perm-{{ perms[j].id }}">{{ perms[j].name }}</label>
				</li>
			{{# } }}
			</ul>	
			</div>	
		{{# } }}
</script>
<!-- layTpl end -->
</html>
