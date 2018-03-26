<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	<title>店铺人员列表</title>
	<style type="text/css">
		.vt.list{
			margin-left: 0;
			padding-left: 0;
			display: inline;
		}
		
		.vt.list li{
			border: 1px solid #CCC;
			border-radius: 4px;
			list-style: none;
			margin:3px 0px;
			height: 28px;
			line-height: 26px;
			padding: 0px 6px;
			
		}
		
		.select{
			background-color: #ffc;
		}
		
		.hr.list{
			list-style:none;
		    display: inline-block;
		}
		
		.hr.list li{
			float:left;
			width: auto;
			padding: 0px 10px;
		}
		
		table.gridtable {
			font-family: verdana, arial, sans-serif;
			font-size: 11px;
			color: #333333;
			border-collapse: collapse;
			width:600px;
		}
		
		table.gridtable th {
			border:1px solid #AAA;
			padding: 3px;
			background-color: #dedede;
			height: 24px;
			line-height: 22px;
		}
		
		table.gridtable td {
			border:1px solid #AAA;
			padding: 3px;
			background-color: #ffffff;
			height: 24px;
			line-height: 22px;
		}
	
	</style>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding:4px;">
		<!--过滤区域 -->
		<div class="filter section">
			<div class="filter row">
				<div class="group align left">
					<button id="btnAddStaff" class="button">添加人员</button>
				</div>
				<div style="float:right;" class="group align right">
					<label class="label">人员名称</label> 
					<input id="queryStaffName" class="input one half wide" /> 
					<span class="spacer two wide"></span>
					<button id="queryShopStaffBtn" class="button">查询</button>
				</div>
			</div>
		</div>
		<!--店铺人员-角色Grid -->
		<table id="shopStaffGrid"></table>
		<div id="shopStaffPager"></div>
		
		<!--查看 修改人员dialog -->
		<div id="viewStaffDialog" style="display: none;">
			<div class="ui-layout-center form">
				<div class="field row" style="height: auto;line-height: auto; width:100%;">
					<table data-role="staff_tab" class="gridtable" style="width:100%;">
						<thead>
							<tr>
								<th width="25%">昵称</th>
								<th width="25%">性别</th>
								<th width="25%">手机号</th>
								<th width="25%">邮箱</th>
							</tr>
						</thead>
						<tbody id="staff_tbd">
							<tr>
								<td><label data-role="nickName"></label></td>
								<td><label data-role="gender"></label></td>
								<td><label data-role="phoneNo"></label></td>
								<td><label data-role="email"></label></td>
							</tr>
						</tbody>
					</table>
				</div>
				
				<div class="field row" style="height: auto;line-height: auto; margin-top:10px;">
					<fieldset >
						<legend >角色分配</legend>
						<table style="table-layout:fixed;with:100%;height:100%;border-spacing:1px;">
							<tr>
								<td width="150px;" class="align top">
									<div style="width:100%;height:220px;overflow-y:auto;">
										<ul id="userRoleList" class="vt list"></ul>
										
									</div>
								</td>
								<td width="80%" class="align top">
									<div id="rolePermList" style="width:100%;height:220px;overflow-y:auto;padding: 0px 3px;"></div>
								</td>
							</tr>
						</table>
					</fieldset>
					
				</div>
				
			</div>
	    </div>
    
	</div>
	
	
	<!--添加人员dialog -->
	<div id="addStaffDialog" style="display: none;">
		<div class="ui-layout-center">
			<div class="filter section">
				<div class="filter row">
					<div style="float:right;" class="group right align">
						<label class="label">会员名称</label> 
						<input id="queryUserName" class="input one half wide"/> 
						<span class="spacer"></span>
						<label class="label">会员手机</label> 
						<input id="queryUserPhoneNo" class="input one half wide" maxlength="11"/> 
						<span class="spacer"></span>
						<button id="btnQueryUser" class="button">查询</button>
					</div>
				</div>
			</div>
			<table id="shopUserGrid"></table>
			<div id="shopUserPager"></div>
		</div>
    </div>
	    
    <!--分配人员角色dialog -->
	<div id="assignRoleDialog" class="form" style="display: none;">
		<div class="field row" style="height: auto;line-height: auto; margin-top:10px;">
			<fieldset >
				<legend >角色分配</legend>
				<table style="table-layout:fixed;with:100%;height:100%;border-spacing:1px;">
					<tr>
						<td width="150px;" class="align top">
							<div style="width:100%;height:220px;overflow-y:auto;">
								<ul id="assignUserRoleList" class="vt list"></ul>
							</div>
						</td>
						<td width="80%" class="align top">
							<div id="assignRolePermList" style="width:100%;height:220px;overflow-y:auto;padding: 0px 3px;"></div>
						</td>
					</tr>
				</table>
			</fieldset>
			
		</div>
	</div>
       
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		   
    //------------------------初始化变量-------------------------
    //
    var staffGridHelper = JqGridHelper.newOne("");
    //
    var assginStaffGridHelper = JqGridHelper.newOne("");
    //
    var shopStaffGridCtrl = null;
    //
    var shopUserGridCtrl = null;
    //
    var addShopStaffDialog = null;
    //
	var assignRoleDialog = null;
    //
    var viewStaffDialog = null;
    //
    var editStaffDialog = null;
    //
    var method = "add";
    //某店铺下所有角色和权限列表
    var allRoles = [];
    
    //------------------------初始化 start-------------------------
	$(function() {
		//页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 100,
			allowTopResize : false
		});
		//初始化添加人员Dialog
		initAddStaffDialog();
		
		//加载店铺人员列表数据
		loadShopStaffData();
		
		//根据手机号或昵称查询用户--加载用户数据
		loadUserData();
		
		//某店铺下所有角色和权限列表
		loadAllRoles();
		
		//绑定查询人员事件
		$id("queryShopStaffBtn").click(function(){
			var filter = {};
			var nickName = textGet("queryStaffName").trim();
			if(nickName){
				filter.nickName = nickName;
			}
			//
			shopStaffGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			getCallbackAfterGridLoaded = function(){
			};
		});
		
		//绑定查询用户事件
		$id("btnQueryUser").click(function(){
			var filter = {};
			var phoneNo = textGet("queryUserPhoneNo");
			if(phoneNo){
				filter.phoneNo = phoneNo.trim();
			}
			var userName = textGet("queryUserName");
			if(userName){
				filter.nickName = userName;
			}
			shopUserGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
		});
		
		//绑定添加人员事件
		$id("btnAddStaff").click(function(){
			goAddStaff();
		});
		
		// 页面自适应
		winSizeMonitor.start(adjustCtrlsSize);
		
	});
    
	//------------------------处理-----------------------------------
	
	//
	function renderAssignUserRole(userId){
		//渲染人员角色区域--ul[assignUserRoleList]
		var urTpl = $id(userRolesTpl).html();
		var userRolesHtmlStr = laytpl(urTpl).render(allRoles);
		$id("assignUserRoleList").html(userRolesHtmlStr);
		//渲染角色权限区域--div[assignRolePermList]
		var rpTpl = $id("rolePermsTpl").html();
		var rolePermsHtmlStr = laytpl(rpTpl).render(allRoles);
		$id("assignRolePermList").html(rolePermsHtmlStr);
		//
		bindShowRolePerms("assignUserRoleList","assignRolePermList");
		//
		var userMap = assginStaffGridHelper.getRowData(userId);
		var roles = userMap.roles;
		//渲染已选定的角色
		$id("assignUserRoleList").find("li[data-role='role']").each(function(){
			var grantable = $(this).attr("data-grantable");
			var roleId = $(this).attr("data-id");
			if(grantable == 'true'){
				for(var i=0; i<roles.length; i++){
					var _roleId = roles[i].id;
					if(roleId == _roleId){
						$(this).find("input[type='checkbox']").attr("checked","checked");
					}
				}
			}else{
				$(this).remove();
				var adminPermDiv = $id("assignRolePermList").find("div[data-role='perm'][for-roleId='"+roleId+"']");
				adminPermDiv.remove();
			}
			
		});
		
		//默认选择首个
		var firstLi = $id("assignUserRoleList").find("li:first");
		$(firstLi).addClass("select");
		//
		var firstPermDiv = $id("assignRolePermList").find("div[data-role='perm']:first");
		$(firstPermDiv).css("display", "block");
	}
	
	//
	function assignUserRoles(userId){
		//初始化分配角色Dialog
		initAssignRoleDialog(userId);
		renderAssignUserRole(userId);
		assignRoleDialog.dialog("open");
	}
	
	//
	function getChangeUserRoles(staffId, userRoleListId){
		var staffMap = null;
		if(userRoleListId == "userRoleList"){
			staffMap = staffGridHelper.getRowData(staffId);
		}else if(userRoleListId == "assignUserRoleList"){
			staffMap = assginStaffGridHelper.getRowData(staffId);
		}
		//
		var old_roles = staffMap.roles;
		var new_roleIds = [];
		$id(userRoleListId).find("li>input[type='checkbox']:checked").each(function(){
			var checkedLi = $(this).parent();
			var roleId = $(checkedLi).attr("data-id");
			if(!isNoB(roleId)){
				roleId = ParseInt(roleId);
				new_roleIds.add(roleId);
			}
		});
		var dataMap = {
			"deleteRoleIds" : [],
			"addRoleIds" : []
		};
		//
		for(var oi=0; oi<old_roles.length; oi++){
			var role = old_roles[oi];
			var grantable = role.grantable;
			if(!grantable) continue;
			var old_roleId = role.id;
			var isContains = false;
			for(var ni=new_roleIds.length-1; ni>=0; ni--){
				var new_roleId = new_roleIds[ni];
				if(old_roleId == new_roleId){
					isContains = true;
					new_roleIds.removeAt(ni);
					break;
				}
			}
			//
			if(!isContains){
				dataMap["deleteRoleIds"].add(old_roleId);
			}
		}
		//
		if(new_roleIds.length > 0){
			dataMap["addRoleIds"] = new_roleIds;
		}
		//
		return dataMap;
	}
	//
	function editStaff(staffId, userRoleListId){
		var hintBox = Layer.progress("正在保存数据...");
		//
		var dataMap = {};
		var roleChangeMap = getChangeUserRoles(staffId, userRoleListId);
		dataMap.deleteRoleIds = roleChangeMap["deleteRoleIds"];
		dataMap.addRoleIds = roleChangeMap["addRoleIds"];
		dataMap.userId = staffId;
		//
		var ajax = Ajax.post("/shop/user/roles/update/do");
		
		ajax.data(dataMap);

		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
				//加载最新数据列表
				if(userRoleListId == "userRoleList"){
					var filter = {};
					var nickName = textGet("queryStaffName").trim();
					if(nickName){
						filter.nickName = nickName;
					}
					//
					shopStaffGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
					getCallbackAfterGridLoaded = function(){
						goViewStaff(staffId);
					};
				}else if(userRoleListId == "assignUserRoleList"){
					var filter = {};
					var phoneNo = textGet("queryUserPhoneNo").trim();
					if(phoneNo){
						filter.phoneNo = phoneNo;
					}
					var userName = textGet("queryUserName");
					if(userName){
						filter.nickName = userName;
					}
					//
					shopUserGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
					//
					var filter = {};
					var nickName = textGet("queryStaffName").trim();
					if(nickName){
						filter.nickName = nickName;
					}
					//
					shopStaffGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
					getCallbackAfterGridLoaded = function(){};
				}
				
			} else {
				Layer.msgWarning(result.message);
			}

		});
		ajax.always(function() {
			hintBox.hide();
		});
		
		ajax.go()
	}
	
	//
	function selectMethod(staffId){
		if(method == 'view'){
			goEditStaff(staffId)
		}else if(method == 'edit'){
			editStaff(staffId, "userRoleList");
		}
	}
	
	function loadAllRoles(){
		var ajax = Ajax.post("/shop/roles/and/perms/get");

		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				var roles = result.data;
				console.log("roles=>"+JSON.encode(roles));
				//
				if(!isNoB(roles) && roles.length > 0){
					//每个角色中的权限列表按照模块分组
					var dataList = [];  //
					for(var rri=0, rrlen=roles.length; rri<rrlen; rri++){
						var role = roles[rri];
						var roleMap = {};
						roleMap.id = role.id;
						roleMap.scope = role.scope;
						roleMap.name = role.name;
						roleMap.entityId = role.entityId;
						roleMap.grantable = role.grantable;
						var perms = role.perms;
						var tempArr = [];
						var moduleList = [];
						for(var pi=0; pi<perms.length; pi++){
							var moduleId = perms[pi].moduleId;
							var _moduleId = null;
							//
							if(pi < perms.length-1){
								_moduleId = perms[pi+1].moduleId;
							}else{
								_moduleId = perms[pi-1 < 0 ? 0: pi-1].moduleId;
							}
							//
							var perm = perms[pi];
							var permMap = {};
							permMap.id = perm.id;
							permMap.moduleId = perm.moduleId;
							permMap.name = perm.name;
							permMap.seqNo = perm.seqNo;
							tempArr.push(permMap);
							//
							if (moduleId != _moduleId) {
								var module = perms[pi].module;
				            	var moduleMap = {};
				            	moduleMap.id = module.id;
				            	moduleMap.name = module.name;
				            	moduleMap.desc = module.desc;
				            	moduleMap.seqNo = module.seqNo;
				            	var permList = tempArr.slice(0);
				            	moduleMap["perms"] = permList;
				                moduleList.add(moduleMap);
				                tempArr.length = 0;
				            }
							//
							if(pi == perms.length-1 && tempArr.length > 0){
								var module = perms[pi].module;
				            	var moduleMap = {};
				            	moduleMap.id = module.id;
				            	moduleMap.name = module.name;
				            	moduleMap.desc = module.desc;
				            	moduleMap.seqNo = module.seqNo;
				            	var permList = tempArr.slice(0);
				            	moduleMap["perms"] = permList;
				                moduleList.add(moduleMap);
				                tempArr.length = 0;
							}
							
						}
						roleMap["modules"] = moduleList;
						dataList.add(roleMap);
					}
					allRoles = dataList;
					console.log("分组后："+JSON.encode(allRoles));
				}
				
			} 
		});
		//
		ajax.go();
	}
	
	//
	function bindShowRolePerms(userRoleListId, rolePermListId){
		$id(userRoleListId).find("li>label").click(function(){
			var li =  $(this).parent();
			var roleId = li.attr("data-id");
			var permBlock = $id(rolePermListId).find("div[data-role='perm'][for-roleId='"+roleId+"']");
			//
			if(!isNoB(permBlock)){
				$id(rolePermListId).find("div[data-role='perm']").css("display","none");
				//
				$(permBlock).css("display","block");
				//
				$id(userRoleListId).find("li").removeClass("select");
				//
				li.addClass("select");
			}
		});
	}
	
	//
	function showStaffOnDialog(staffId){
		var staffMap = staffGridHelper.getRowData(staffId);
		var staffBody = $id("staff_tbd");
		var nickNameLabel = $(staffBody).find("label[data-role='nickName']");
		var genderLabel = $(staffBody).find("label[data-role='gender']");
		var phoneNoLabel = $(staffBody).find("label[data-role='phoneNo']");
		var emailLabel = $(staffBody).find("label[data-role='email']");
		//
		$(nickNameLabel).html(staffMap.nickName);
		var gender = staffMap.gender;
		var genderStr = "保密";
		//
		if(gender == 'M'){
			genderStr = "男";
		}else if(gender == 'F'){
			genderStr = '女';
		}
		//
		$(genderLabel).html(genderStr);
		//
		$(phoneNoLabel).html(staffMap.phoneNo);
		//
		$(emailLabel).html(staffMap.email);
		//
		var userRoleList = $id("userRoleList");
		var roles = staffMap.roles;
		if(method == "view"){
			//渲染人员角色区域--ul[userRoleList]
			var urTpl = $id(userRolesTpl).html();
			var userRolesHtmlStr = laytpl(urTpl).render(roles);
			userRoleList.html(userRolesHtmlStr);
			//渲染角色权限区域--div[rolePermList]
			var rpTpl = $id("rolePermsTpl").html();
			var _rolePerms = getPermsFromRoles(roles, allRoles);
			var rolePermsHtmlStr = laytpl(rpTpl).render(_rolePerms);
			$id("rolePermList").html(rolePermsHtmlStr);
			//
			bindShowRolePerms("userRoleList", "rolePermList");
			//
			userRoleList.find("li>input[type='checkbox']").each(function(){
				$(this).css("display", "none");
			});
		}else{//method=edit
			//渲染人员角色区域--ul[userRoleList]
			var urTpl = $id(userRolesTpl).html();
			var userRolesHtmlStr = laytpl(urTpl).render(allRoles);
			userRoleList.html(userRolesHtmlStr);
			//渲染角色权限区域--div[rolePermList]
			var rpTpl = $id("rolePermsTpl").html();
			var rolePermsHtmlStr = laytpl(rpTpl).render(allRoles);
			$id("rolePermList").html(rolePermsHtmlStr);
			//渲染已选定的角色
			userRoleList.find("li[data-role='role']").each(function(){
				var grantable = $(this).attr("data-grantable");
				var roleId = $(this).attr("data-id");
				if(grantable == 'true'){
					for(var i=0; i<roles.length; i++){
						var _roleId = roles[i].id;
						if(roleId == _roleId){
							$(this).find("input[type='checkbox']").attr("checked","checked");
						}
					}
				}else{
					$(this).remove();
					var adminPermDiv = $id("rolePermList").find("div[data-role='perm'][for-roleId='"+roleId+"']");
					adminPermDiv.remove();
				}
			});
			//
			bindShowRolePerms("userRoleList", "rolePermList");
			//
			userRoleList.find("li>input[type='checkbox']").each(function(){
				$(this).css("display", "inline-block");
			});
		}
		//默认选择首个
		var firstLi = userRoleList.find("li:first");
		$(firstLi).addClass("select");
		//
		var firstPermDiv = $id("rolePermList").find("div[data-role='perm']:first");
		$(firstPermDiv).css("display", "block");
		
	}
	//获取roles的角色-权限集合
	function getPermsFromRoles(roles, rolePerms){
		var retRolePerms = [];
		for(var r=0; r<roles.length; r++){
			var _role = roles[r];
			var _roleName = _role.name;
			//遍历rolePrems
			for(var i=rolePerms.length-1; i>=0; i--){
				var role = rolePerms[i];
				var roleName = role.name;
				if(roleName == _roleName){
					retRolePerms.add(rolePerms[i]);
					break;
				}
				//
			}
			//
		}
		//
		return retRolePerms;
	}
	//
	function clearAddShopStaffDialog(){
		$id("queryUserName").val("");
		$id("queryUserPhoneNo").val("");
		shopUserGridCtrl.jqGrid("clearGridData");
	}
	//
	function goAddStaff(){
		method = "add";
		clearAddShopStaffDialog();
		addShopStaffDialog.dialog("open");
	}
	//
	function goViewStaff(staffId){
		method = "view";
		initViewStaffDialog(staffId);
		var staffMap = staffGridHelper.getRowData(staffId);
		var roles = staffMap.roles;
		var isAdmin = false;
		for(var i=0; i<roles.length; i++){
			var role = roles[i];
			var entityId = role.entityId;
			if(entityId == -1){
				isAdmin = true;
				break;
			} 
		}
		//
		if(isAdmin){
			var viewDialogDiv = $id("viewStaffDialog").parent();
			viewDialogDiv.find("div.ui-dialog-buttonset").find("button>span").each(function(){
				var buttonText = $(this).html();
				if(buttonText.contains("修改")){
					$(this).parent().css("display","none");
				}
			});
		}
		showStaffOnDialog(staffId);
		viewStaffDialog.dialog("open");
	}
	//
	function goEditStaff(staffId){
		method = "edit";
		initEditStaffDialog(staffId);
		showStaffOnDialog(staffId);
		editStaffDialog.dialog("open");
	}
	//
	function goUnbindUserRoles(staffId){
		var theLayer = Layer.confirm('确定要解除所有角色吗？', function(){
			
			var hintBox = Layer.progress("正在保存数据...");
			
			var ajax = Ajax.post("/shop/user/roles/unbind/do");
				ajax.data({userId: ParseInt(staffId)});
				ajax.done(function(result, jqXhr){
					if(result.type== "info"){
						Layer.msgSuccess(result.message);
						//加载最新数据列表
						var filter = {};
						var nickName = textGet("queryStaffName").trim();
						if(nickName){
							filter.nickName = nickName;
						}
						//
						shopStaffGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
						getCallbackAfterGridLoaded = function(){};
					}else{
						Layer.msgWarning(result.message);
					}
				});
				ajax.always(function() {
					theLayer.hide();
					hintBox.hide();
				});
				ajax.go();
			});
	}
	//
	function getCallbackAfterGridLoaded(){}
	
	//------------------------初始化JqGrid-------------------------
	//加载店铺人员列表数据
	function loadShopStaffData(){
		var filter = {};
		var nickName = textGet("queryStaffName").trim();
		if(nickName){
			filter.nickName = nickName;
		}
		//
		shopStaffGridCtrl= $("#shopStaffGrid").jqGrid({
		      url : getAppUrl("/shop/staff/list/get"),  
		      contentType : 'application/json',  
		      mtype : "post",  
		      datatype : 'json',
		      postData:{filterStr : JSON.encode(filter,true)},
		      height : "100%",
			  width : "100%",
		      colNames : ["人员编号", "人员名称","手机","邮箱","角色","操作"],  
		      colModel : [{name:"id", index:"id", width:50,align : 'right',},
		                  {name:"nickName", index:"nickName",width:110,align : 'left',},
		                  {name:"phoneNo", index:"phoneNo",width:80,align : 'right'},
		                  {name:"email", index:"email",width:130,align : 'left'},
		                  {name:'roles',index:'roles', align : 'center',formatter : function (cellValue,option,rowObject) {
		                	  var roleNames=[];
		                	  for(var i=0;i<cellValue.length;i++){
		                		  roleNames.add(cellValue[i].name);
								}
		                	  return roleNames;
							},width:230,align:"left"},
		                  {name:'id',index:'id',width:100,align:"left", formatter : function (cellValue,option,rowObject) {
		                	  var roles = rowObject.roles;
		                	  var isAdmin = false;
		                	  for(var i=0; i<roles.length; i++){
		                		  var role = roles[i];
		                		  var entityId = role.entityId;
		                		  if(entityId == -1) isAdmin = true;
		                	  }
		                	  if(!isAdmin){
		                		  return "[<a class='item' href='javascript:void(0);' onclick='goViewStaff("
									+ cellValue
									+ ")' >查看</a>]<span class='chs spaceholder'></span>[<a class='item' href='javascript:void(0);' onclick='goEditStaff("
									+ cellValue
									+ ")' >修改</a>]<span class='chs spaceholder'></span>[<a class='item' href='javascript:void(0);' onclick='goUnbindUserRoles("
									+ cellValue
									+ ")' >解除</a>]";
		                	  }else{
		                		  return "[<a class='item' href='javascript:void(0);' onclick='goViewStaff("
									+ cellValue
									+ ")' >查看</a>]";
		                	  }
		                	  
							}
		                  }
						],  
			pager : "#shopStaffPager",
			loadComplete : function(gridData){
				staffGridHelper.cacheData(gridData);
				var callback = getCallbackAfterGridLoaded();
				if(isFunction(callback)){
					callback();
				}

			},
			ondblClickRow: function(rowId) {
				var userMap = staffGridHelper.getRowData(rowId)
				goViewStaff(rowId);
			}
		});
	}
	//
	function loadUserData(){
		var filter = {};
		var phoneNo = textGet("queryUserPhoneNo");
		if(phoneNo){
			filter.phoneNo = phoneNo.trim();
		}
		var userName = textGet("queryUserName");
		if(userName){
			filter.nickName = userName;
		}
		//
		shopUserGridCtrl= $("#shopUserGrid").jqGrid({
		      url : getAppUrl("/shop/user/list/get"),  
		      contentType : 'application/json',  
		      mtype : "post",  
		      datatype : 'json',
		      postData:{filterStr : JSON.encode(filter,true)},
		      colNames : ["id","用户昵称","手机","角色","操作"],  
		      colModel : [{name:"id", index:"id", hidden:true},
		                  {name:"nickName", index:"nickName",width:150,align : 'left',},
		                  {name:"phoneNo", index:"phoneNo",width:100,align : 'right'},
		                  {name:'roles',index:'roles', align : 'center',formatter : function (cellValue,option,rowObject) {
		                	  var roleNames=[];
		                	  for(var i=0;i<cellValue.length;i++){
		                		  roleNames.add(cellValue[i].name);
								}
		                	  return roleNames;
							},width:230,align:"left"},
		                  {name:'id',index:'id', formatter : function (cellValue,option,rowObject) {
		                	  var roles = rowObject.roles;
		                	  var isAdmin = false;
		                	  for(var i=0; i<roles.length; i++){
		                		  var role = roles[i];
		                		  var entityId = role.entityId;
		                		  if(entityId == -1) isAdmin = true;
		                	  }
		                	  if(!isAdmin){
		                		  if(roles.length > 0){
		                			  return "[<a class='item' href='javascript:void(0);' onclick='assignUserRoles("
										+ cellValue
										+ ")' >修改角色</a>]";
		                		  }else{
		                			  return "[<a class='item' href='javascript:void(0);' onclick='assignUserRoles("
										+ cellValue
										+ ")' >分配角色</a>]";
		                		  }
		                		  
		                	  }else{
		                		  return "";
		                	  }
						},
					width:150,align:"center"}
		                  ],
              loadComplete : function(gridData){
            	assginStaffGridHelper.cacheData(gridData);
  				var callback = getCallbackAfterGridLoaded();
  				if(isFunction(callback)){
  					callback();
  				}
            	adjustaddStaffDialogSize();
   			 },
		});
	}
	
	//------------------------初始化Dialog-------------------------
	
	//修改人员Dialog
	function initEditStaffDialog(staffId){
		editStaffDialog = $( "#viewStaffDialog" ).dialog({
	        autoOpen: false,
	        width : Math.min(800, $window.width()),
	        height : Math.min(480, $window.height()),
	        modal: true,
	        title : '修改人员',
	        buttons: {
	        	"保存" : function(){
					selectMethod(staffId);
				},
	            "关闭": function(){
	            	editStaffDialog.dialog( "close" );
	          }
	        },
	        close: function() {
	        	editStaffDialog.dialog( "close" );
	        }
	      });
	}
	
	//查看人员Dialog
	function initViewStaffDialog(staffId){
		viewStaffDialog = $( "#viewStaffDialog" ).dialog({
	        autoOpen: false,
	        width : Math.min(800, $window.width()),
	        height : Math.min(480, $window.height()),
	        modal: true,
	        title : '查看人员',
	        buttons: {
	        	"修改 >" : function(){
	        		selectMethod(staffId);
				},
	            "关闭": function(){
	            	viewStaffDialog.dialog( "close" );
	          }
	        },
	        close: function() {
	        	viewStaffDialog.dialog( "close" );
	        }
	      });
	}
	
	//初始化添加人员Dialog
	function initAddStaffDialog(){
		addShopStaffDialog = $( "#addStaffDialog" ).dialog({
	        autoOpen: false,
	        width : Math.min(900, $window.width()),
	        height : Math.min(300, $window.height()),
	        modal: true,
	        title : '添加人员',
	        buttons: {
	            "关闭": function() {
	            	addShopStaffDialog.dialog( "close" );
	          }
	        },
	        close: function() {
	        	addShopStaffDialog.dialog( "close" );
	        }
	      });
	}
	
	//初始化分配角色Dialog
	function initAssignRoleDialog(userId){
		assignRoleDialog = $( "#assignRoleDialog" ).dialog({
	       autoOpen: false,
	       width : Math.min(800, $window.width()),
	       height : Math.min(400, $window.height()),
	       modal: true,
	       title : '分配人员角色',
	       buttons: {
	       	"确定":function(){
	       		editStaff(userId, "assignUserRoleList");
	       	},
           "关闭": function() {
           		assignRoleDialog.dialog( "close" );
         	}
	       },
	       close: function() {
	       		assignRoleDialog.dialog( "close" );
	       }
	     });
	}
	
	//------------------------调整控件大小-------------------------
	//调整控件大小
	function adjustCtrlsSize() {
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		var gridCtrlId = "shopStaffGrid";
		var jqGridBox = $("#gbox_" + gridCtrlId);
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
		var pagerHeight = $id("shopUsePager").height();
		shopStaffGridCtrl.setGridWidth(mainWidth - 1);
		shopStaffGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 90);
	}
		
	//调整添加用户对话框控件大小
	function adjustaddStaffDialogSize(){
		var dialogPanel = $id("addStaffDialog");
		var dialogWidth = dialogPanel.width();
		//
		if(dialogWidth < 0){
			dialogWidth = Math.min(900, $window.width()) - 36;
		}
		var dialogHeight = dialogPanel.height();
		if(dialogHeight < 0){
			dialogHeight = Math.min(300, $window.height());
		} 
		//
		var userGridCtrlId = "shopUserGrid";
		var jqUserGridBox = $("#gbox_" + userGridCtrlId);
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqUserGridBox).height();
		shopUserGridCtrl.setGridWidth(dialogWidth - 1);
		shopUserGridCtrl.setGridHeight(91);
	}		
	</script>
</body>
<!-- 用户角色列表模板 -->
<script id="userRolesTpl" type="text/html">
{{# for(var i = 0, len = d.length; i < len; i++){ }}
{{# var role = d[i]; }}
<li data-role='role' data-id='{{ role.id }}' data-grantable='{{ role.grantable }}'>
	<input id='{{role.name}}_{{role.id}}' type='checkbox'>
	<label class='align middle' for='{{role.name}}_{{role.id}}'>{{role.name}}</label>
</li>
{{# } }}
</script>

<!-- 角色权限列表模板 -->
<script id="rolePermsTpl" type="text/html">
{{# for(var i = 0, roles = d, len = d.length; i < len; i++){ }}
{{# var role = d[i]; }}
<div data-role='perm' for-roleId='{{ role.id }}' style='display:none;' class='simple block'>
	{{# var modules = role.modules; }}

	{{# if(modules.length < 1) { }}
		<div class='header'>暂无权限</div>
	{{# } }}

	{{# for(var mi = 0, mlen = modules.length; mi < mlen; mi++){ }}
	{{# var module = modules[mi]; }}
	<div class='header'>{{ module.name }}</div>
	<div class='body'>
		<ul class='hr list' style='width: 100%;'>
			{{# for(var pi = 0, perms = module.perms, plen = perms.length; pi < plen; pi++){ }}
				{{# var perm = perms[pi]; }}
				<li>
					<label>{{ perm.name }}</label>
				</li>
			{{# } }}			
		</ul>
	</div>
	{{# } }}
</div>
{{# } }}
</script>
</html>