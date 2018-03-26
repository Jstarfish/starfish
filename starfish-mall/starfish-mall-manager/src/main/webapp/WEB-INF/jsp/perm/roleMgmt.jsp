<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<style type="text/css">
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
	
	li label{
		vertical-align: middle;
	}	
</style>
<title>角色管理</title>
</head>
<body id="rootPanel" style="padding: 4px;">
	<div id="mainPanel" class="ui-layout-center" style="padding: 0px;">
		<div id="tabs" class="noBorder">
			<ul>
				<li><a href="#sys"><span>系统</span></a></li>
				<li><a href="#mall"><span>商城</span></a></li>
				<li><a href="#shop"><span>店铺</span></a></li>
				<li><a href="#wxshop"><span>卫星店</span></a></li>
				<li style="display: none;"><a href="#agency"><span>代理处</span></a></li>
				
				<div class="normal group right aligned">
					<button id="btnSave" class="normal button">保存</button>
					<span class="normal spacer"></span>
					<button id="btnCancel" class="normal button">取消</button>
				</div>
			</ul>
			<div id="sys"></div>
			<div id="mall"></div>
			<div id="shop"></div>
			<div id="wxshop"></div>
			<div style="display: none;" id="agency"></div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
	//权限最初始状态列表
	var origMap = new KeyMap();
	var jqTabsCtrl;
	
	//初始化角色权限
	function initData(){
		var ajax = Ajax.post("/perm/role/mgmt/list/get");
		ajax.sync();
    	ajax.done(function(result, jqXhr){
    		if(result.type== "info"){
    			var data = result.data;
    			var siteTplHtml = $id("siteTpl").html();
    			var sysHtmlStr = laytpl(siteTplHtml).render(data["sys"]);
    			var mallHtmlStr = laytpl(siteTplHtml).render(data["mall"]);
    			var shopHtmlStr = laytpl(siteTplHtml).render(data["shop"]);
    			var wxshopHtmlStr = laytpl(siteTplHtml).render(data["wxshop"]);
    			//var agencyHtmlStr = laytpl(siteTplHtml).render(data["agency"]);
				$id("sys").html(sysHtmlStr);
				$id("mall").html(mallHtmlStr);
				$id("shop").html(shopHtmlStr);
				$id("wxshop").html(wxshopHtmlStr);
				//$id("agency").html(agencyHtmlStr);
				//设置系统角色的权限为禁用
				$("input:checkbox", "#sys").each(function(i,p){
					$(p).attr("disabled", 'disabled');
				});
				$("button","#sys").each(function(i,p){
					$(p).hide();
				});
				updatePermsStatus();
    		}
    		else {
    			Layer.msgWarning(result.message);
    		}
    	});
    	ajax.go();
	}

	//修改角色权限方法
	function updateRolePermission(){
		//
		var postData = getStatusChangeInfoList();
		
		//如果没修改则不发送请求
		if(postData.length == 0){
			Toast.show("您没有进行任何的修改");
			return;
		}
		var dataWrapper = { json : JSON.encode(postData, true)};
		var hintBox = Layer.progress("正在提交数据...");	
		var ajax = Ajax.post("/perm/role/mgmt/update/do");
		ajax.data(dataWrapper);
		ajax.done(function(result, jqXhr){
			if(result.type== "info"){
				Layer.msgSuccess(result.message);
    			//更改权限状态
    			updatePermsStatus();
			}else{
				Layer.msgWarning(result.message);
			}
		});
		ajax.always(function(){
			hintBox.hide();
		});
		ajax.go();
	}

	//
	function getStatusChangeInfoList(){
		var rolePermRelChanges = [];
		//
		var mallRoleId = ParseInt($("ul", "#mall").attr("data-role-id"));
		
		var mallChangeInfo = {
				"mainId" : mallRoleId,
				"subIdsAdded" : [],
				"subIdsDeleted" : []
			};
		//
		$("input:checkbox", "#mall").each(function(i,p){
			var permId = $(p).attr("data-id");
			var oldStatus = origMap.get(permId);
			var newStatus = $(p).is(':checked');
			if(newStatus != oldStatus){
				var slotKey = newStatus ? "subIdsAdded" : "subIdsDeleted";
				mallChangeInfo[slotKey].add(ParseInt(permId));
			}
		});
		if(mallChangeInfo["subIdsAdded"].length > 0 || mallChangeInfo["subIdsDeleted"].length > 0){
			rolePermRelChanges.add(mallChangeInfo);
		}
		//
		
		var shopRoleId = ParseInt($("ul", "#shop").attr("data-role-id"));
		var shopChangeInfo = {
				"mainId" : shopRoleId,
				"subIdsAdded" : [],
				"subIdsDeleted" : []
			};
		//
		$("input:checkbox", "#shop").each(function(i,p){
			var permId = $(p).attr("data-id");
			var oldStatus = origMap.get(permId);
			var newStatus = $(p).is(':checked');
			if(newStatus != oldStatus){
				var slotKey = newStatus ? "subIdsAdded" : "subIdsDeleted";
				shopChangeInfo[slotKey].add(ParseInt(permId));
			}
		});
		if(shopChangeInfo["subIdsAdded"].length > 0 || shopChangeInfo["subIdsDeleted"].length > 0){
			rolePermRelChanges.add(shopChangeInfo);
		}
		
		//wxshop
		var wxshopRoleId = ParseInt($("ul", "#shop").attr("data-role-id"));
		var wxshopChangeInfo = {
				"mainId" : wxshopRoleId,
				"subIdsAdded" : [],
				"subIdsDeleted" : []
			};
		$("input:checkbox", "#wxshop").each(function(i,p){
			var permId = $(p).attr("data-id");
			var oldStatus = origMap.get(permId);
			var newStatus = $(p).is(':checked');
			if(newStatus != oldStatus){
				var slotKey = newStatus ? "subIdsAdded" : "subIdsDeleted";
				wxshopChangeInfo[slotKey].add(ParseInt(permId));
			}
		});
		if(wxshopChangeInfo["subIdsAdded"].length > 0 || wxshopChangeInfo["subIdsDeleted"].length > 0){
			rolePermRelChanges.add(wxshopChangeInfo);
		}
		
		// WJJ
		var agencyRoleId = ParseInt($("ul", "#agency").attr("data-role-id"));
		var agencyChangeInfo = {
				"mainId" : agencyRoleId,
				"subIdsAdded" : [],
				"subIdsDeleted" : []
			};
		//
		$("input:checkbox", "#agency").each(function(i,p){
			var permId = $(p).attr("data-id");
			var oldStatus = origMap.get(permId);
			var newStatus = $(p).is(':checked');
			if(newStatus != oldStatus){
				var slotKey = newStatus ? "subIdsAdded" : "subIdsDeleted";
				agencyChangeInfo[slotKey].add(ParseInt(permId));
			}
		});
		if(agencyChangeInfo["subIdsAdded"].length > 0 || agencyChangeInfo["subIdsDeleted"].length > 0){
			rolePermRelChanges.add(agencyChangeInfo);
		}
		
		return rolePermRelChanges;
	}
	
	//取消操作
	function cancel(){
		//
		var postData = getStatusChangeInfoList();
		
		//如果没修改则不发送请求
		if(postData.length == 0){
			Toast.show("您没有进行任何的修改");
			return;
		}
		initData();
	}
	
	//更新权限状态
	function updatePermsStatus(){
		//清空权限初始化状态
		origMap.clear();
		//添加权限初始状态，全部为false
		$("input:checkbox").each(function(i,p){
			var permId = $(p).attr("data-id");
			origMap.add(permId, false);
		});
		//根据查询结果勾选权限，并修改权限初始状态map数据
		var ajax = Ajax.post("/perm/role/perms/list/get");
    	ajax.done(function(result, jqXhr){
    		if(result.type== "info"){
    			var checkedList = result.data || [], len = checkedList.length;
    			for(var i = 0; i < len; i++){
    				var rp = checkedList[i], permId = rp.permId;
    				$id("perm-"+permId).attr("checked", 'checked');
    				origMap.set(permId, true);
    			}
    			initButtonsHtml();
    		} else {
    			Layer.msgWarning(result.message);
    		}
    	});
    	ajax.go();
	}
	
	//
	function checkedAll(obj){
		var id = obj.id;
		var arry = id.split("-");
		var scope, moduleId, ul, letCheckedAll = false;
		scope = arry[0];
		moduleId = arry[1];
		ul = scope +"-ul-"+ moduleId;
		$("input:checkbox", "#"+ul).each(function(i,p){
			var checked = $(p).is(":checked");
			if(checked == false){
				letCheckedAll = true;
			}
		});
		console.log("letCheckedAll:"+letCheckedAll);
		if(letCheckedAll){
			$id(scope +"-"+ moduleId).html("全不选");
			$("input:checkbox", "#"+ul).each(function(i,p){
				$(p).attr("checked", 'checked');
			});	
		}else{
			$id(scope +"-"+ moduleId).html("全选");
			$("input:checkbox", "#"+ul).each(function(i,p){
				$(p).removeAttr("checked");
			});
		}
	}
	
	//
	function initButtonsHtml(){
		$("ul", "#mall").each(function(i, p){
			var id = p.id;
			var arry = id.split("-");
			var moduleId = arry[2];
			var letCheckedAll = false
			$("input:checkbox", "#"+id).each(function(j,k){
				var checked = $(k).is(":checked");
				if(checked == false){
					letCheckedAll = true;
				}
			});
			if(letCheckedAll){
				$id("mall-"+moduleId).html("全选");
			}else{
				$id("mall-"+moduleId).html("全不选");
			}
		});
		$("ul", "#shop").each(function(i, p){
			var id = p.id;
			var arry = id.split("-");
			var moduleId = arry[2];
			var letCheckedAll = false
			$("input:checkbox", "#"+id).each(function(j,k){
				var checked = $(k).is(":checked");
				console.log(k.id+":"+checked);
				if(checked == false){
					letCheckedAll = true;
				}
			});
			if(letCheckedAll){
				$id("shop-"+moduleId).html("全选");
			}else{
				$id("shop-"+moduleId).html("全不选");
			}
		});
		$("ul", "#wxshop").each(function(i, p){
			var id = p.id;
			var arry = id.split("-");
			var moduleId = arry[2];
			var letCheckedAll = false
			$("input:checkbox", "#"+id).each(function(j,k){
				var checked = $(k).is(":checked");
				console.log(k.id+":"+checked);
				if(checked == false){
					letCheckedAll = true;
				}
			});
			if(letCheckedAll){
				$id("wxshop-"+moduleId).html("全选");
			}else{
				$id("wxshop-"+moduleId).html("全不选");
			}
		});
		// WJJ
		$("ul", "#agency").each(function(i, p){
			var id = p.id;
			var arry = id.split("-");
			var moduleId = arry[2];
			var letCheckedAll = false
			$("input:checkbox", "#"+id).each(function(j,k){
				var checked = $(k).is(":checked");
				console.log(k.id+":"+checked);
				if(checked == false){
					letCheckedAll = true;
				}
			});
			if(letCheckedAll){
				$id("agency-"+moduleId).html("全选");
			}else{
				$id("agency-"+moduleId).html("全不选");
			}
		});
	}
	
	//调整控件大小
	function adjustCtrlsSize() {
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();

		var tabsCtrlWidth = mainWidth - 4;
		var tabsCtrlHeight = mainHeight - 8;
		jqTabsCtrl.width(tabsCtrlWidth);
		var tabsHeaderHeight = $(">[role=tablist]", jqTabsCtrl).height();
		var tabsPanels = $(">[role=tabpanel]", jqTabsCtrl);
		tabsPanels.width(tabsCtrlWidth - 4 * 2);
		tabsPanels.height(tabsCtrlHeight - tabsHeaderHeight - 30);
	}
	
	//-----------------------------------初始化代码---------------------------------------
	$(function() {  
		//页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			allowTopResize : false
		});
		jqTabsCtrl = $("#tabs").tabs();  
	   	
		initData();
	   	$id("btnSave").click(updateRolePermission);
	   	$id("btnCancel").click(cancel);
	   	$("input:checkbox").change(initButtonsHtml);
	   	
	 	// 页面自适应
		winSizeMonitor.start(adjustCtrlsSize);
	});
	</script>
</body>
	<!-- 模块权限静态模板 -->
	<script id="siteTpl" type="text/html">
			{{# var modules = d["modules"], role = d["role"], mdLen = modules.length}}
			{{# if(role != null){ }}
				<div style="text-align:center;margin:0 auto;"><label style="font-size:16px;">角色名称：{{ role.alias }}</label></div>
				{{# for(var i = 0; i < mdLen; i++){ }}
					{{# var module = modules[i], perms = module.permissions, psLen = perms.length; }}
					<div class='simple block'>
						<div class='header'>
							<label class='title'>{{ module.name }}</label>
							<span class="normal spacer"></span>
							<button id="{{role.scope}}-{{module.id}}" class="normal button" onclick="checkedAll(this);" ></button>
						</div>
					<ul class='list' data-role-id="{{ role.id }}" id="{{role.scope}}-ul-{{module.id}}">
					{{# for (var j = 0; j < psLen; j++) { }}
						<li>
							<input id="perm-{{ perms[j].id }}" data-id="{{ perms[j].id }}" name=perm type="checkbox" /> 
							<label for="perm-{{ perms[j].id }}">{{ perms[j].name }}</label>
						</li>
					{{# } }}
					</ul>
					</div>
				{{# } }}
			{{# }else{ }}
				<label>请初始化角色...</label>
			{{# }}}
	</script>
</html>
