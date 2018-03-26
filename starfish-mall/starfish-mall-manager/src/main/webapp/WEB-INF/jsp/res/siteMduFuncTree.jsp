<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<link rel="stylesheet" href="<%=resBaseUrl%>/lib/ztree/css/zTreeStyle.css" />
<title>模块功能管理</title>
<style type="text/css">
	
</style>
</head>
<body  id="rootPanel">
  <div id="topPanel" class="ui-layout-north" style="padding:4px;vertical-align: bottom;">
		<div class="simple block">
			<div class="header">
				<label>资源模块管理   &gt; 模块功能管理</label>
			</div>
		</div>
  </div>
  <div id="mainPanel" class="ui-layout-center" style="padding:0px;">
	<div id="theTabsCtrl" class="noBorder">
		<ul id="urlSiteModule">
			<li data-role="mall"><a href="#tab_mall">商城</a></li>
			<li data-role="shop"><a href="#tab_shop">店铺</a></li>
			<li style="display: none;" data-role="agency"><a href="#tab_agency">代理处</a></li>
				<div class="normal group right aligned" id="btnSitModFuncBackDiv" style="display: none">
						<button class="normal button" id="btnSitModFuncBack">返回</button>
						<span class="normal spacer"></span>
			   </div>
		</ul>
		<div id="tab_mall">
			<div class="content_wrap">
				<div class="zTreeDemoBackground left">
					<div class="simple block">
						<div class="header">
						    <img src='<%=resBaseUrl%>/lib/ztree/css/img/diy/1_open.png'/>
							<span>模块</span>
							<span class='chs spaceholder'></span>
						    <img src='<%=resBaseUrl%>/lib/ztree/css/img/diy/8.png'/>
							<span>功能</span>
				    		<span class='chs spaceholder'></span>
				    		<img src='<%=resBaseUrl%>/lib/ztree/css/img/diy/3.png'/>
				    		<span>资源</span>
				    		<span class='chs spaceholder'></span>
				    		<button id='btnAddMall_siteModuOpenDialog' class="normal button">增加模块</button>
						</div>
					</div>
					<ul id="treeMall" class="ztree"></ul>
				</div>
			</div>
	   </div>
	   <div id="tab_shop">
	   		<div class="content_wrap">
				<div class="zTreeDemoBackground left">
					<div class="simple block">
						<div class="header">
						    <img src='<%=resBaseUrl%>/lib/ztree/css/img/diy/1_open.png'/>
							<span>模块</span>
							<span class='chs spaceholder'></span>
						    <img src='<%=resBaseUrl%>/lib/ztree/css/img/diy/8.png'/>
							<span>功能</span>
				    		<span class='chs spaceholder'></span>
				    		<img src='<%=resBaseUrl%>/lib/ztree/css/img/diy/3.png'/>
				    		<span>资源</span>
				    		<span class='chs spaceholder'></span>
							<button  id='btnAddShop_siteModuOpenDialog'  class="normal button">增加模块</button>
						</div>
					</div>
					<ul id="treeShop" class="ztree"></ul>
				</div>
			</div>
	   </div>
	   <!-- 新增代理处 -->
	   <div style="display: none;" id="tab_agency">
	   		<div class="content_wrap">
				<div class="zTreeDemoBackground left">
					<div class="simple block">
						<div class="header">
						    <img src='<%=resBaseUrl%>/lib/ztree/css/img/diy/1_open.png'/>
							<span>模块</span>
							<span class='chs spaceholder'></span>
						    <img src='<%=resBaseUrl%>/lib/ztree/css/img/diy/8.png'/>
							<span>功能</span>
				    		<span class='chs spaceholder'></span>
				    		<img src='<%=resBaseUrl%>/lib/ztree/css/img/diy/3.png'/>
				    		<span>资源</span>
				    		<span class='chs spaceholder'></span>
							<button  id='addAgencySiteMduBtn'  class="normal button">增加模块</button>
						</div>
					</div>
					<ul id="agencyTree" class="ztree"></ul>
				</div>
			</div>
	   </div>
	   
	</div>
 </div>
     <!-- 	弹出Dialog  start-->
	<div id="siteModuleDialogCreate" title="新增模块" style="display: none">
		<div class="form">
			<div class="field row">
				<label class="field label required one half wide">模块名称</label>
				<input type="text"  id="siteModuleName" class="field value two wide" >
			</div>
			<div class="field row" style="height:100px;">
				<label class="field label one half wide" >描述</label>
				<textarea id="siteModuleDesc"  class="field value two wide" style="height:80px;"></textarea>
			</div>
			
			 <div class="field row">
				<label class="field label one half wide">图片</label>
				<input name="file" type="file" id="fileToUploadFileIcon" multiple="multiple" class="field value  two wide"  /> 
				<button class="normal button" style="float:right;position:absolute;right:0;margin-right:20px;margin-top:6px;" id="btnfileToUploadFileIcon">上传</button>
			</div>
			<div class="field row ">
	        	<label class="field label one half wide">图片预览</label>
	        	<img id="IconImg"  style="border:1px solid #EEE;" height="80px" width="120px"/>
	        </div>
		</div>
	</div>
	<div id="siteModuleDialogUpdate"  title="更新模块" style="display: none">
		<div class="form">
			<div class="field row">
				<label for="name" class="field label required one half wide">模块名称</label>
				<input type="text" id="siteModuleNameUpdate" class="field value two wide" >
			</div>
			<input type="hidden" id="siteModuleIdUpdate">
			<input type="hidden" id="siteModuleScopeUpdate">
			<input type="hidden" id="siteModuleSeqNoUpdate">
			<div class="field row" style="height:100px;">
				<label  class="field label  one half wide">描述</label>
				<textarea id="siteModuleDescUpdate"  class="field value two wide" style="height:80px;"></textarea>
			</div>
			<div class="field row">
				<label class="field label one half wide">图片</label>
				<input name="file" type="file" id="fileToUploadFileIconEdit" multiple="multiple" class="field value two wide"  /> 
				<button class="normal button" style="float:right;position:absolute;right:0;margin-right:20px;margin-top:6px;" id="btnfileToUploadFileIconEdit">上传</button>
			</div>
			<div class="field row ">
	        	<label class="field label one half wide">图片预览</label>
	        	<img id="IconImgEdit" style="border:1px solid #EEE;" height="80px" width="120px" />
	        </div>
		</div>
	</div>
	<div id="siteFunctionDialogAdd" title="新增功能" style="display: none">
		<div class="form">
			  <div class="field row">
				<label class="field label required one half wide">功能名称</label>
				<input type="text"  id="siteFunctionName"  class="field value two wide" >
				<input type="hidden"  id="siteFunctionModuleId"  class="field value two wide" >
			 </div>
			 <div class="field row" style="height:100px;">
				<label class="field label one half wide" >描述</label>
				<textarea id="siteFunctionDesc"  class="field value two wide" style="height:80px;"></textarea>
			</div>
			 <div class="field row">
					<label class="field label one half wide">图片</label>
					<input name="file" type="file" id="fileToUploadFileIconFunc" multiple="multiple" class="field value one half wide"  /> 
					<button class="normal button" style="float:right;position:absolute;right:0;margin-right:20px;margin-top:6px;" id="btnfileToUploadFileIconFunc">上传</button>
					
			</div>
			<div class="field row ">
	        	<label class="field label one half wide">图片预览</label>
	        	<img id="IconImgFunc" style="border:1px solid #EEE;" height="80px" width="120px" />
	        </div>
	    </div>
	</div>
	<div id="siteFunctionDialogUpdate" title="更新功能" style="display: none">
		<div class="form">
		  <div class="field row">
			<label class="field label required one half wide">功能名称</label>
			<input type="text" id="siteFunctionNameUpdate" class="field value two wide" >
		  </div>
			<input type="hidden" id="siteFunctionIdUpdate">
			<input type="hidden" id="siteFunctionSeqNoUpdate">
			<input type="hidden" id="siteFunctionModuleIdUpdate">
		  <div class="field row" style="height:100px;">
			<label class="field label  one half wide" >描述</label>
			<textarea id="siteFunctionDescUpdate" class="field value two wide" style="height:80px;"></textarea>
		  </div>
		  <div class="field row">
				<label class="field label one half wide">图片</label>
				<input name="file" type="file" id="fileToUploadFileIconFuncEdit" multiple="multiple" class="field value one half wide"  /> 
				<button class="normal button"  style="float:right;position:absolute;right:0;margin-right:20px;margin-top:6px;" id="btnfileToUploadFileIconFuncEdit">上传</button>
		 </div>
		  <div class="field row ">
	        	<label class="field label one half wide">图片预览</label>
	        	<img id="IconImgFuncEdit" style="border:1px solid #EEE;" height="80px" width="120px" />
	      </div>	
		</div>
	</div>
	<div id="selectSiteResourseDialog" title="关联资源" style="display: none">
		<fieldset>
			<div id="allResource"  class="filter section"></div>
		</fieldset>
	</div>
	<div style="display: none">
    	<input type="hidden"  id="IconUuid" />
		<input type="hidden"  id="IconUsage" />
		<input type="hidden"  id="IconPath" />
    </div>
	<!-- 	弹出Dialog  end-->
   <jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
   <script type="text/javascript" src="<%=resBaseUrl%>/lib/ztree/js/jquery.ztree.all.js"></script>
   <script type="text/javascript">
   
 	//------------------------初始化变量------------------------- 
 	var jqTabsCtrl;
    //存储
    var origMap = new KeyMap();
    //初始化新增siteModule dialog
	var siteModuleDialogCreate;
	//初始化更新siteModule dialog
	var siteModuleDialogUpdate;
	//初始化新增siteFunction dialog
	var siteFunctionDialogAdd;
	//初始化更新siteFunction dialog
	var siteFunctionDialogUpdate;
	//初始化选择资源siteResourse dialog
	var selectSiteResourseDialog;
	//功能ID
	var functionId="";
	var sitModuAddFlag=true;
	var sitModuUpdateFlag=true;
	var sitModuVName="";
	var resouseModuId="";
 	var resouseFuncId="";
	
	//------------------------表单验证------------------------- 
	
	//实例化新增模块表单代理
	var formProxyCreateSiteModule = FormProxy.newOne();
	//注册表单控件
	formProxyCreateSiteModule.addField({
		id : "siteModuleName",
		required : true,
		rules : ["maxLength[30]",{
			rule : function(idOrName, type, rawValue, curData) {
					validateSiteModuName(rawValue);
					//
					if (sitModuAddFlag) {
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
		}]
	});
	formProxyCreateSiteModule.addField({
		id : "siteModuleDesc",
		rules : ["maxLength[100]"]
	});
	//实例化更新模块表单代理
	var formProxyUpdateSiteModule = FormProxy.newOne();
	//注册表单控件
	formProxyUpdateSiteModule.addField({
		id : "siteModuleNameUpdate",
		required : true,
		rules : ["maxLength[30]",{
			rule : function(idOrName, type, rawValue, curData) {
				validateUpdateSiteModuName(rawValue);
				//
				if (sitModuUpdateFlag) {
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
	}]
	});
	formProxyUpdateSiteModule.addField({
		id : "siteModuleDescUpdate",
		rules : ["maxLength[100]"]
	});
	var formProxyCreateSiteFunc = FormProxy.newOne();
	//注册表单控件
	formProxyCreateSiteFunc.addField({
		id : "siteFunctionName",
		required : true,
		rules : ["maxLength[30]"]
	});
	formProxyCreateSiteFunc.addField({
		id : "siteFunctionDesc",
		rules : ["maxLength[100]"]
	});
	var formProxyUpdateSiteFunc = FormProxy.newOne();
	//注册表单控件
	formProxyUpdateSiteFunc.addField({
		id : "siteFunctionNameUpdate",
		required : true,
		rules : ["maxLength[30]"]
	});
	formProxyUpdateSiteFunc.addField({
		id : "siteFunctionDescUpdate",
		rules : ["maxLength[100]"]
	});
	
	//------------------------调整控件大小-------------------------
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
	}
	
	//------------------------初始化方法-------------------------
    
    //界面初始化方法 
    $(function(){
    	//加载Tab页
    	jqTabsCtrl = $("#theTabsCtrl").tabs({});
    	//页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 60,
			allowTopResize : false
		});
    	//
    	initSiteModuleDialog();
    	
		// 页面自适应
		winSizeMonitor.start(adjustCtrlsSize);
    	
    	//绑定添加商城模块按钮
		$id("btnAddMall_siteModuOpenDialog").click(function(){
			initSiteModuleDialog();
			siteModuleDialogCreate.dialog( "open" );
		});
		//绑定添加店铺模块按钮
		$id("btnAddShop_siteModuOpenDialog").click(function(){
			siteModuleDialogCreate.dialog( "open" );
		});
		//绑定添加代理处模块按钮
		$id("addAgencySiteMduBtn").click(function(){
			siteModuleDialogCreate.dialog( "open" );
		});
		
		//绑定新增模块上传按钮
		$id("btnfileToUploadFileIcon").click(function(){
			fileToUploadFileIcon("fileToUploadFileIcon","newModu");
		});
		//绑定修改模块上传按钮
		$id("btnfileToUploadFileIconEdit").click(function(){
			fileToUploadFileIcon("fileToUploadFileIconEdit","editModu");
		});
		//绑定新增模块上传按钮
		$id("btnfileToUploadFileIconFunc").click(function(){
			fileToUploadFileIcon("fileToUploadFileIconFunc","newFunc");
		});
		//绑定修改模块上传按钮
		$id("btnfileToUploadFileIconFuncEdit").click(function(){
			fileToUploadFileIcon("fileToUploadFileIconFuncEdit","editFunc");
		});
		//文件上传
		initFileUpload("fileToUploadFileIcon");
		initFileUpload("fileToUploadFileIconEdit");
		initFileUpload("fileToUploadFileIconFunc");
		initFileUpload("fileToUploadFileIconFuncEdit");
		//绑定返回按钮
		$id("btnSitModFuncBack").click(function(){
			window.location.href= getAppUrl("/res/siteModu/list/jsp");
		});
		$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
		var urlParams = extractUrlParams();
		var parmsId = decodeURI(urlParams["siteModuFuncId"]);
		var parmsScope = decodeURI(urlParams["scope"]);
		
		if(parmsScope==""||parmsScope=="undefined"){
			initTree("mall","");
			initTree("shop","");
			initTree("agency","")
		}else{
			if(parmsScope=="mall"){
				jqTabsCtrl.tabs({ active: 0 });
				initTree("mall",parmsId);
				initTree("shop","");
				initTree("agency","");
				resouseModuId=parmsId;
			}else{
				jqTabsCtrl.tabs({ active: 1 });
				initTree("mall","");
				initTree("shop",parmsId);
				initTree("agency","");
				resouseModuId=parmsId;
			}
		}
		//
		if(parmsId==""||parmsId=="undefined"){
			$id("btnSitModFuncBackDiv").attr("style","display:none");
		}else{
			$id("btnSitModFuncBackDiv").removeAttr("style");
		}
     });

	//------------------------Ztree setting-------------------------
    var setting = {
			view: {
				addHoverDom:bindFunc,
				removeHoverDom: unBindFunc,
				addDiyDom: addDiyDom,
				selectedMulti: false
			},
			data: {
				key:{
					name:"name",
				    children: "nodes"
				},
				simpleData: {
					enable: true,
					idKey: "id",

					isUse: "isUse",
					rootPId: null,
				}
			},
			callback: {
				onClick: fnClickTree,
				beforeExpand: zTreeBeforeExpand
			}
	};
   
	//------------------------对树的操作-------------------------
	
	function fnClickTree(event, treeId, treeNode) {
		if("module"==treeNode.type){
			resouseModuId=treeNode.id;
		}
		if("func"==treeNode.type){
			resouseFuncId=treeNode.id;
			//alert(resouseFuncId);
		}
		$('a').removeClass('curSelectedNode');
	    $("#" + treeNode.tId + "_a").addClass('curSelectedNode');
     };
     
    function zTreeBeforeExpand(treeId, treeNode){
    	if("module"==treeNode.type){
			resouseModuId=treeNode.id;
		}
    	if("func"==treeNode.type){
			resouseFuncId=treeNode.id;
			//alert(resouseFuncId);
		}
    	$('a').removeClass('curSelectedNode');
	    $("#" + treeNode.tId + "_a").addClass('curSelectedNode');
    	
    } 
   
    //初始化树数据 
    function initTree(scope,moduId,funcId){
    	//生成提交数据
		var postData = {
			scope : scope
		};
		//可以对postData进行必要的处理（如如数据格式转换）
		//显示等待提示框
		var loaderLayer = Layer.loading("正在获取数据...");
		var ajax = Ajax.post("/res/siteModu/get/tree/by/scope");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if(result.type== "info"){
			    var data=changeJson(result.data,moduId,funcId);
			    if(scope=="mall"){
			    	$.fn.zTree.init($("#treeMall"), setting, data);
			    }else if(scope=="shop"){
			    	$.fn.zTree.init($("#treeShop"), setting, data);
			    }else if(scope=="agency"){
			    	$.fn.zTree.init($("#agencyTree"), setting, data);
			    }
				
			}else{
				Layer.msgWarning(result.message);
			}
		});
		ajax.always(function() {
			//隐藏等待提示框
			loaderLayer.hide();
		});
		ajax.go();
    }
    
   //改变json 数据成为Ztree 需要格式
    function changeJson(jsondata,moduId,funcId){
	  var data=jsondata;
    	for(var i=0;i<data.length;i++){
			var childJson=data[i];
			if(childJson.id==moduId){
		    	childJson["open"]=true;	
		    	childJson["selectCss"]="yes";	
    		}
			var Jsonfunction=data[i].functions;
			if(Jsonfunction!=null){
			for(var j=0;j<Jsonfunction.length;j++){
				Jsonfunction[j].nodes= Jsonfunction[j].resources;
				delete Jsonfunction[j].resources;
				Jsonfunction[j]["icon"]="<%=resBaseUrl%>/lib/ztree/css/img/diy/8.png";
				Jsonfunction[j]["type"]="func";
				if(Jsonfunction[j].id==funcId){
					Jsonfunction[j]["open"]=true;
					Jsonfunction[j]["selectCss"]="yes";
				}
				var jsonResouse=Jsonfunction[j].nodes;
				if(jsonResouse!=null){
					for(var k=0;k<jsonResouse.length;k++){
						jsonResouse[k]["icon"]="<%=resBaseUrl%>/lib/ztree/css/img/diy/3.png";
					}
				}
			}   
			}
			    childJson["icon"]="<%=resBaseUrl%>/lib/ztree/css/img/diy/1_open.png";
			    childJson["type"]="module";
			    childJson.nodes=childJson.functions;
				delete childJson.functions;
		}
    	return data;
    }
    
    //为树的每个节点绑定增加.编辑.删除.事件
    
    function bindFunc(treeId, treeNode){
    	   if(treeNode.level<3){
    		   var data=treeNode;
    	    	var aObj = $("#" + treeNode.tId + "_a");
    	    	if ($("#BtnAdd_"+treeNode.id).length>0) return;
     	    	if("module"==treeNode.type||"func"==treeNode.type){
    	    		var editStr = "<img  id='BtnAdd_" + treeNode.id+ "' onfocus='this.blur();' src='<%=resBaseUrl%>/lib/ztree/css/img/diy/zj.png'/><img  id='BtnEdit_" + treeNode.id+ "' src='<%=resBaseUrl%>/lib/ztree/css/img/diy/2.png'/><img  id='BtnDel_" + treeNode.id+ "' src='<%=resBaseUrl%>/lib/ztree/css/img/diy/del.png'/>";
    	    	}else{
     	    		var editStr = "<img  id='BtnAdd_" + treeNode.id+ "' onfocus='this.blur();' src='<%=resBaseUrl%>/lib/ztree/css/img/diy/del.png'/>";	 
   	    	    }
    	    	aObj.append(editStr);
    	    	var btnEdit = $("#BtnEdit_"+treeNode.id);
    	    	var btnAdd = $("#BtnAdd_"+treeNode.id);
    	    	var btnDel = $("#BtnDel_"+treeNode.id);
    	    	if (btnAdd) btnAdd.bind("click", function(){
    	    		if("module"==treeNode.type){
    	    			$id("siteFunctionModuleId").val(treeNode.id);
    	    			$("#IconImgFunc").removeAttr("src");
    	    			initSiteModuleDialog();
    	    			siteFunctionDialogAdd.dialog( "open" ); 
    	    		}else if("func"==treeNode.type){
    	    			getAllSiteResourseByScope(treeNode.id);
    	    		}else{
    	    			btnDelSiteResouse(treeNode);	
    	    		}
    	    	});
    	    	if (btnEdit) btnEdit.bind("click", function(){
    	    		if("module"==treeNode.type){
    	    			$id("siteModuleNameUpdate").val(treeNode.name);
				 		$id("siteModuleDescUpdate").val(treeNode.desc);
				 		$id("siteModuleIdUpdate").val(treeNode.id);
				 		$id("siteModuleScopeUpdate").val(treeNode.scope);
				 		$id("siteModuleSeqNoUpdate").val(treeNode.seqNo)
				 		$id("IconUuid").val(treeNode.iconUuid);
						$id("IconUsage").val(treeNode.iconUsage);
						$id("IconPath").val(treeNode.iconPath);
				 		$("#IconImgEdit").attr("src",treeNode.iconPath);
				 		 sitModuVName=treeNode.name;
				 		initSiteModuleDialog();
				 		 siteModuleDialogUpdate.dialog( "open" ); 
    	    		}else if("func"==treeNode.type){
		    			  $id("siteFunctionNameUpdate").val(treeNode.name);
					      $id("siteFunctionDescUpdate").val(treeNode.desc);
					      $id("siteFunctionModuleIdUpdate").val(treeNode.moduleId);
					      $id("siteFunctionIdUpdate").val(treeNode.id);
					      $id("siteFunctionSeqNoUpdate").val(treeNode.seqNo);
					      $id("IconUuid").val(treeNode.iconUuid);
						  $id("IconUsage").val(treeNode.iconUsage);
						  $id("IconPath").val(treeNode.iconPath);
					 	  $("#IconImgFuncEdit").attr("src",treeNode.iconPath);
					 	 initSiteModuleDialog();
					      siteFunctionDialogUpdate.dialog( "open" );
    	    		}
    	    		
	    		  });
    	    	if (btnDel) btnDel.bind("click", function(){
    	    		if("module"==treeNode.type){
    	    			btnDelSiteModule(treeNode.id,treeNode.scope);	
    	    		}else if("func"==treeNode.type){
    	    			btnDelSiteFunction(treeNode.id,treeNode.moduleId);
    	    		}
    	    	});   
    	   }
    	
	 }
   //为树的每个节点解绑增加.编辑.删除.事件
    function unBindFunc(treeId, treeNode) {
    	$("#BtnEdit_"+treeNode.id).unbind().remove();
    	$("#BtnAdd_"+treeNode.id).unbind().remove();
    	$("#BtnDel_"+treeNode.id).unbind().remove();
    };
    
    function addDiyDom(treeId, treeNode){
    	 if(treeNode.level<3){
    		 if(treeNode.level<2){
    			 if(treeNode.selectCss=="yes"){
    				var flag="1";
    				var nodes= treeNode.nodes;
    				for(var i=0;i<nodes.length;i++){
    					if(nodes[i].selectCss=="yes"){	
    						flag="2";
    						break;
    					}	
    				}
    				if(flag=="1"){
    					$("#" + treeNode.tId + "_a").addClass('curSelectedNode');	
    				}
    	 	    } 
    		 }else{
    			 if(treeNode.selectCss=="yes"){
     	    		 $("#" + treeNode.tId + "_a").addClass('curSelectedNode');
     	    	 } 
    		 }
    	 }
    	
    }
    //------------------------初始化Dialog------------------------- 
    
    //初始化SiteModuleDialog
	function initSiteModuleDialog(){
		//新增SiteMOdule
		siteModuleDialogCreate = $( "#siteModuleDialogCreate" ).dialog({
	        autoOpen: false,
	        width : Math.min(550, $window.width()),
	        height : Math.min(400, $window.height()),
	        modal: true,
	        buttons: {
	            "保存": createSiteModule,
	            "取消": function() {
	             siteModuleDialogCreate.dialog( "close" );
	          }
	        },
	        close: function() {
	        	formProxyCreateSiteModule.hideMessages();
	        	$id("siteModuleName").val("");
	    		$id("siteModuleDesc").val("");
	    		$id("IconUuid").val("");
			    $id("IconUsage").val("");
			    $id("IconPath").val("");
			    $("#IconImg").removeAttr("src");
			    sitModuAddFlag=true;
	        }
	      });
		
		//更新SiteModule
		siteModuleDialogUpdate = $("#siteModuleDialogUpdate" ).dialog({
	        autoOpen: false,
	        width : Math.min(550, $window.width()),
	        height : Math.min(400, $window.height()),
	        modal: true,
	        buttons: {
	            "保存": updateSiteModule,
	            "取消": function() {
	             siteModuleDialogUpdate.dialog( "close" );
	          }
	        },
	        close: function() {
	        	formProxyUpdateSiteModule.hideMessages();
	        	$id("siteModuleNameUpdate").val("");
	    		$id("siteModuleDescUpdate").val("");
	    		$id("siteModuleIdUpdate").val("");
	    		$id("siteModuleScopeUpdate").val("");
	    		$id("IconUuid").val("");
			    $id("IconUsage").val("");
			    $id("IconPath").val("");
			    $("#IconImgEdit").removeAttr("src");
			    sitModuUpdateFlag=true;
				sitModuVName="";
	        }
	      });
		
		//增加SiteFunction
		siteFunctionDialogAdd=$( "#siteFunctionDialogAdd" ).dialog({
	        autoOpen: false,
	        width : Math.min(550, $window.width()),
	        height : Math.min(400, $window.height()),
	        modal: true,
	        buttons: {
	            "保存": addSiteFunction,
	            "取消": function() {
	             siteFunctionDialogAdd.dialog( "close" );
	          }
	        },
	        close: function() {
	        	formProxyCreateSiteFunc.hideMessages();
	        	$id("siteFunctionName").val("");
	    		$id("siteFunctionDesc").val("");
	    		$id("siteFunctionModuleId").val("");
	    		$id("IconUuid").val("");
			    $id("IconUsage").val("");
			    $id("IconPath").val("");
			    $("#IconImgFunc").removeAttr("src");
	        }
	      });
		
		//编辑SiteFunction
		siteFunctionDialogUpdate=$( "#siteFunctionDialogUpdate" ).dialog({
	        autoOpen: false,
	        width : Math.min(550, $window.width()),
	        height : Math.min(400, $window.height()),
	        modal: true,
	        buttons: {
	            "保存": updateSiteFunction,
	            "取消": function() {
	            	siteFunctionDialogUpdate.dialog( "close" );
	          }
	        },
	        close: function() {
	        	 formProxyUpdateSiteFunc.hideMessages();
	        	 $id("siteFunctionNameUpdate").val("");
		    	 $id("siteFunctionDescUpdate").val("");
		    	 $id("siteFunctionModuleIdUpdate").val("");
		    	 $id("siteFunctionIdUpdate").val("");
		    	 $id("siteFunctionSeqNoUpdate").val("");
		    	 $id("IconUuid").val("");
				 $id("IconUsage").val("");
				 $id("IconPath").val("");
				 $("#IconImgFuncEdit").removeAttr("src");
	        }
	      });
	   
		//绑定（解绑）资源 
		selectSiteResourseDialog = $id( "selectSiteResourseDialog" ).dialog({
	        autoOpen: false,
	        width : Math.min(650, $window.width()),
	        height : Math.min(500, $window.height()),
	        modal: true,
	        buttons: {
	            "保存": addResource,
	            "取消": function() {
	            	selectSiteResourseDialog.dialog( "close" );
	          }
	        },
	        close: function() {
	        	
	        }
	      });
	}
    
	//------------------------SiteModule 添加  修改  删除 操作------------------------- 
	  
	//新增站点模块
	function createSiteModule() {
		var vldResult = formProxyCreateSiteModule.validateAll();
		if (!vldResult) {
			return;
		}
		var scope = $id("urlSiteModule").find("li[aria-selected='true']").attr("data-role");
		var postData = {
			scope : scope,
			name : $id("siteModuleName").val(),
			desc : $id("siteModuleDesc").val(),
			iconUuid : $id("IconUuid").val(),
			iconUsage : $id("IconUsage").val(),
			iconPath : $id("IconPath").val()
	
		};
		var loaderLayer = Layer.loading("正在提交数据...");
		var ajax = Ajax.post("/res/siteModu/save/do");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				siteModuleDialogCreate.dialog("close");
				Layer.msgSuccess(result.message);
				$id("siteModuleName").val("");
				$id("siteModuleDesc").val("");
				$id("IconUuid").val("");
				$id("IconUsage").val("");
				$id("IconPath").val("");
				if ("mall" == scope) {
					initTree("mall", result.data.id, "");
				} else if ("shop" == scope) {
					initTree("shop", result.data.id, "");
				} else if ("agency" == scope) {
					initTree("agency", result.data.id, "");
				}
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
	
	  //更新站点模块
	  function updateSiteModule(){
		 var vldResult = formProxyUpdateSiteModule.validateAll();
			 if (!vldResult) {
				return;
			 }
		 var scope = $id("urlSiteModule").find("li[aria-selected='true']").attr("data-role");
	 	 var postData = {
	 			    name:$id("siteModuleNameUpdate").val(),
	 				desc:$id("siteModuleDescUpdate").val(),
	 				id:$id("siteModuleIdUpdate").val(),
	 				scope:$id("siteModuleScopeUpdate").val(),
	 				iconUuid:$id("IconUuid").val(),
				    iconUsage:$id("IconUsage").val(),
				    iconPath:$id("IconPath").val(),
				    seqNo:$id("siteModuleSeqNoUpdate").val()
	 		};
 	    var loaderLayer = Layer.loading("正在提交数据...");
 		var ajax = Ajax.post("/res/siteModu/update/do");
	 		ajax.data(postData);
	 	    ajax.done(function(result, jqXhr){
	 	    	if(result.type== "info"){
	 	    		siteModuleDialogUpdate.dialog( "close" );
	 	    		Layer.msgSuccess(result.message);
	 	    		$id("siteModuleNameUpdate").val("");
	 	    		$id("siteModuleDescUpdate").val("");
	 	    		$id("siteModuleIdUpdate").val("");
	 	    		$id("siteModuleScopeUpdate").val("");
	 	    		$id("IconUuid").val("");
				    $id("IconUsage").val("");
				    $id("IconPath").val("");
				    $id("siteModuleSeqNoUpdate").val("");
	 	    		if("mall"==scope){
	 	    			initTree("mall",result.data.id,"");
	  				}else if("shop"==scope){
	  					initTree("shop",result.data.id,"");
	  				}else if("agency"==scope){
	  					initTree("agency",result.data.id,"");
	  				} 
	 	    	}else{
	 	    		Layer.msgWarning(result.message);	
	 	    	}
	 	  });
	 	   ajax.always(function() {
				//隐藏等待提示框
				loaderLayer.hide();
			});
	 	ajax.go(); 
	  }
	  
	//删除模块   
	function btnDelSiteModule(id, scope) {
		var theLayer = Layer.confirm('模块下的功能所绑定的资源将将全部解绑，模块下的功能将全部删除，确定要删除该模块吗？', function() {
			var postData = {
				id : id,
				scope : scope
			};
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/res/siteModu/delete/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					theLayer.hide();
					Layer.msgSuccess(result.message);
					if ("mall" == scope) {
						initTree("mall", "", "");
					} else if ("shop" == scope) {
						initTree("shop", "", "");
					} else if ("agency" == scope) {
						initTree("agency", "", "");
					}
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.always(function() {
				// 隐藏等待提示框
				loaderLayer.hide();
			});
			ajax.go();
		});
	}
	
	function getCurrentScope(){
		var theTabLink = $id("theTabsCtrl").find("ul>li.ui-tabs-active.ui-state-active").find("a:first");
		var scope = "sys";
		//
		if(theTabLink){
			var href = $(theTabLink).attr("href");
			if(!isNoB(href)){
				scope = href.substring(5, href.length);
			}
		}
		return scope;
	}
	
	//
	 function  validateSiteModuName(name){
		 var ajax = Ajax.post("/res/siteModu/exsit/by/name");
		 var scope = getCurrentScope();
			//
			ajax.data({
				scope : scope,
				name : name
			});
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				sitModuAddFlag = result.data;
			});
			ajax.go();
	 }
	 function validateUpdateSiteModuName(name){
		 if(name==sitModuVName){
			 sitModuUpdateFlag=true;
		 }else{
			 var ajax = Ajax.post("/res/siteModu/exsit/by/name");
			 var scope = getCurrentScope();
				//
				ajax.data({
					scope : scope,
					name : name
				});
				ajax.sync();
				ajax.done(function(result, jqXhr) {
					sitModuUpdateFlag = result.data;
				});
				ajax.go(); 
			 
		 }
	 }
	 
	 //------------------------SiteFunction 添加  修改  删除 操作-------------------------
	
	//添加站点功能
	function addSiteFunction() {
		var vldResult = formProxyCreateSiteFunc.validateAll();
		if (!vldResult) {
			return;
		}
		var scope = $id("urlSiteModule").find("li[aria-selected='true']").attr(
				"data-role");
		var postData = {
			name : $id("siteFunctionName").val(),
			desc : $id("siteFunctionDesc").val(),
			moduleId : $id("siteFunctionModuleId").val(),
			iconUuid : $id("IconUuid").val(),
			iconUsage : $id("IconUsage").val(),
			iconPath : $id("IconPath").val(),
		};
		var loaderLayer = Layer.loading("正在提交数据...");
		var ajax = Ajax.post("/res/siteFunc/add/do");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				if ("mall" == scope) {
					initTree("mall", resouseModuId, result.data.id);
				} else if ("shop" == scope) {
					initTree("shop", resouseModuId, result.data.id);
				} else if ("agency" == scope) {
					initTree("agency", resouseModuId, result.data.id);
				}
				siteFunctionDialogAdd.dialog("close");
				Layer.msgSuccess(result.message);
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
		
	// 更新站点功能
	function updateSiteFunction() {
		var vldResult = formProxyUpdateSiteFunc.validateAll();
		if (!vldResult) {
			return;
		}
		var scope = $id("urlSiteModule").find("li[aria-selected='true']").attr("data-role");
		var postData = {
			name : $id("siteFunctionNameUpdate").val(),
			desc : $id("siteFunctionDescUpdate").val(),
			id : $id("siteFunctionIdUpdate").val(),
			moduleId : $id("siteFunctionModuleIdUpdate").val(),
			iconUuid : $id("IconUuid").val(),
			iconUsage : $id("IconUsage").val(),
			iconPath : $id("IconPath").val(),
			seqNo : $id("siteFunctionSeqNoUpdate").val()
		};
		var loaderLayer = Layer.loading("正在获取数据...");
		var ajax = Ajax.post("/res/siteFunc/update/do");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				if ("mall" == scope) {
					initTree("mall", resouseModuId, resouseFuncId);
				} else if("shop" == scope) {
					initTree("shop", resouseModuId, resouseFuncId);
				} else if("agency" == scope) {
					initTree("agency", resouseModuId, resouseFuncId);
				}
				siteFunctionDialogUpdate.dialog("close");
				Layer.msgSuccess(result.message);
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
	  
  //删除功能   
  function btnDelSiteFunction(id,moduleId){
	    var scope = $id("urlSiteModule").find("li[aria-selected='true']").attr("data-role");
    	var theLayer = Layer.confirm('功能下的资源将全部解绑，确定要删除该模块吗？', function(){
    		var postData = {
    				   id :id,
    				   moduleId:moduleId
    				};
    		   var loaderLayer = Layer.loading("正在提交数据...");
    		  	var ajax = Ajax.post("/res/siteFunc/delete/do");
    		  	   ajax.data(postData);
    		  ajax.done(function(result, jqXhr){
    			  if(result.type== "info"){
    				  theLayer.hide();
    				  Layer.msgSuccess(result.message);
    				  if("mall"==scope){
	    			  		initTree("mall",moduleId,"");
		  				}else if("shop"==scope){
		  					initTree("shop",moduleId,"");
		  				}else if("agency"==scope){
		  					initTree("agency",moduleId,"");
		  				}
    			  }else{
    				  Layer.msgWarning(result.message);  
    			  }
    		 });
    		  ajax.always(function() {
					loaderLayer.hide();
					//确保隐藏等待提示框
				});
    		ajax.go();
    	});
    }
	  
	//------------------------SiteResourse 查询  功能绑定/解绑资源   操作-------------------------
	 
	 //根据Scope查询所有资源
	 function getAllSiteResourseByScope(functionId){
		  this.functionId=functionId;
		  var scope = $id("urlSiteModule").find("li[aria-selected='true']").attr("data-role");
		  var postData = {
					"scope" : scope,
					"funcId": functionId
			};
			//显示等待提示框
			var loaderLayer = Layer.loading("正在获取数据...");
			var ajax = Ajax.post("/res/siteRes/list/get/all");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				 if(result.type== "info"){
					 renderDetail(result.data.resAll,'sitesourseAll','allResource');
						origMap.clear();
						$("input:checkbox").each(function(i,p){
							var resId = $id(p.id).val();
							origMap.add(resId, false);
						});
						//勾选选中的资源
						var resChecked=result.data.resChecked;
						 if(resChecked!=null){
							 for(var i=0;i<resChecked.length;i++){
								 $id("resource"+resChecked[i].id).attr("checked",true); 
								 origMap.set(resChecked[i].id, true);
							 }	  
						 }
						 initSiteModuleDialog();
						selectSiteResourseDialog.dialog( "open" );
				 }else{
					 Layer.msgWarning(result.message); 
				 }
			});
			ajax.always(function() {
				loaderLayer.hide();
			});
			ajax.go(); 
	  }
	
	//功能绑定/解绑资源
	 function addResource() {
	 	var scope = $id("urlSiteModule").find("li[aria-selected='true']").attr("data-role");
	 	var list = getListResourseData();
	 	var postData = {
	 		json : JSON.encode(list, true)
	 	};
	 	var hintBox = Layer.progress("正在提交数据...");
	 	var ajax = Ajax.post("/res/siteRes/batch/add/do");
	 	ajax.done(function(result, jqXhr) {
	 		if (result.type == "info") {
	 			selectSiteResourseDialog.dialog("close");
	 			if ("mall" == scope) {
	 				initTree("mall", resouseModuId, resouseFuncId);
	 				functionId = "";
	 			} else if ("shop" == scope) {
	 				initTree("shop", resouseModuId, resouseFuncId);
	 				functionId = "";
	 			} else if ("agency" == scope) {
	 				initTree("agency", resouseModuId, resouseFuncId);
	 				functionId = "";
	 			}
	 			Layer.msgSuccess(result.message);
	 		} else {
	 			Layer.msgWarning(result.message);
	 		}
	 	});
	 	ajax.data(postData);
	 	ajax.always(function() {
	 		hintBox.hide();
	 	});
	 	ajax.go();
	 }
	   
		//得到资源选择列表数据
		function getListResourseData(){
			var list = [];
			var changeInfo = {
					"mainId" : functionId,
					"subIdsAdded" : [],
					"subIdsDeleted" : []
			};
			//获取到需要解绑/绑定的资源数据
			$("input:checkbox", "#allResource").each(function(i,p){
				var resId = $id(p.id).val();
				var oldStatus = origMap.get(resId);
				var newStatus = $(p).is(':checked');
				if(newStatus != oldStatus){
					var slotKey = newStatus ? "subIdsAdded" : "subIdsDeleted";
					changeInfo[slotKey].add(ParseInt(resId));
				}
			});
			list.add(changeInfo);
			return list;
		}
		//解绑单个资源
		function btnDelSiteResouse(treeNode) {
			var scope = $id("urlSiteModule").find("li[aria-selected='true']").attr("data-role");
			var pNode = treeNode.getParentNode();
			var theLayer = Layer.confirm('确定要解绑该资源吗？', function() {
				var list = putListResourseData(pNode.id, treeNode.id);
				var postData = {
					json : JSON.encode(list, true)
				};
				var ajax = Ajax.post("/res/siteRes/batch/add/do");
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						selectSiteResourseDialog.dialog("close");
						if ("mall" == scope) {
							initTree("mall", resouseModuId, resouseFuncId);
						} else if("shop" == scope) {
							initTree("shop", resouseModuId, resouseFuncId);
						} else if("agency" == scope) {
							initTree("agency", resouseModuId, resouseFuncId);
						}
						Layer.msgSuccess(result.message);
					} else {
						Layer.msgWarning(result.message);
					}
				});
				ajax.data(postData);
				ajax.go();
			});
		}
		
		//得到资源选择列表数据
		function putListResourseData(funcId,souId){
			var list = [];
			var changeInfo = {
					"mainId" : funcId,
					"subIdsAdded" : [],
					"subIdsDeleted" : [souId]
			};
			list.add(changeInfo);
			return list;
		}
		//------------------------上传-------------------------
		//
		function fileToUploadFileIcon(fileId, status) {
			var fileUuidToUpdate = $id("IconUuid").val();
			if(isNoB(fileUuidToUpdate)){
				var formData = {
					usage : "image.icon",
					reserveFileName : true
				};
			}else{
				var formData ={
					update : true,
					uuid : fileUuidToUpdate
				};
			}
			sendFileUpload(fileId, {
				url : getAppUrl("/file/upload"),
				dataType : "json",
				//自定义数据
				formData :formData,
				done : function(e, result) {
					var resultInfo = result.result;
					if(resultInfo.type=="info"){
						$id("IconUuid").val(resultInfo.data.files[0].fileUuid);
						$id("IconUsage").val(resultInfo.data.files[0].fileUsage);
						$id("IconPath").val(resultInfo.data.files[0].fileBrowseUrl);
						if(status=="newModu"){
							$("#IconImg").attr("src",resultInfo.data.files[0].fileBrowseUrl+"?"+new Date().getTime());	
						}else if(status=="editModu"){
							$("#IconImgEdit").attr("src",resultInfo.data.files[0].fileBrowseUrl+"?"+new Date().getTime());	
						}else if(status=="newFunc"){
							$("#IconImgFunc").attr("src",resultInfo.data.files[0].fileBrowseUrl+"?"+new Date().getTime());	
						}else{
							$("#IconImgFuncEdit").attr("src",resultInfo.data.files[0].fileBrowseUrl+"?"+new Date().getTime());		
						}
						
					}
				},fail : function(e, data) {
					console.log(data);
				},noFilesHandler : function() {
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
		
		//------------------------other-------------------------	
		
		//将数据的值赋给静态html
		function renderDetail(data, html, div) {
			var tplHtml = $id(html).html();
			var theTpl = laytpl(tplHtml);
			var htmlStr = theTpl.render(data);
			$id(div).html(htmlStr);
		}
		
   </script>
</body>
  	<script type="text/html" id="sitesourseAll">
    <ul class='list'>
		{{# for(var i = 0, len = d.length; i < len; i++){ }}
           <li> 
		    <input name="resource" id="resource{{ d[i].id }}" 
		    type="checkbox" value={{ d[i].id }}><label for="resource{{ d[i].id }}">{{ d[i].name }}</label>
		   </li>
        {{# } }}
     </ul>

   </script>
</html>