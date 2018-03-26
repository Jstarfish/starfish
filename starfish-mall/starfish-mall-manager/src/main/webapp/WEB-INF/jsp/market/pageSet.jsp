<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<link rel="stylesheet" href="<%=resBaseUrl%>/lib/ztree/css/zTreeStyle.css" />
<title>页面设置</title>
<style type="text/css">
	table.gridtable {
		font-family: verdana, arial, sans-serif;
		font-size: 11px;
		color: #333333;
		border-width: 1px;
		border-color: #666666;
		border-collapse: collapse;
	}
	
	table.gridtable th {
		border:1px solid #AAA;
		border-width: 1px;
		padding: 3px;
		border-style: solid;
		border-color: #666666;
		background-color: #dedede;
	}
	
	table.gridtable td {
		border:1px solid #AAA;
		border-width: 1px;
		border-style: solid;
		border-color: #666666;
		background-color: #ffffff;
	}
	
</style>
</head>
<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding:4px;">
		<div id="tabs" class="noBorder">
			<ul style="border-bottom-color:#ccc;">
				<li><a href="#tab_advertMgmt">定位广告</a></li>
				<li style="display:none;"><a href="#tab_topPromotion">置顶推广</a></li>
			</ul>
			<!-- 定位广告pannel -->
			<div id="tab_advertMgmt">
				<div class="filter section" >
					<div class="filter row">
						<div class="group left aligned">
							<button id="addAdvertBtn" class="button">添加</button>
							<span class="spacer"></span>
							<button id="batchDeleteBtn" class="button" >批量删除</button>
						</div>
						<div class="group right aligned">
							<label class="label">广告编号</label>
							<input id="qryAdvertNo" class="input one half wide" />
							<span class="spacer"></span>
							<label class="label">广告名称</label>
							<input id="qryAdvertName" class="input one half wide" />
							<span class="vt divider"></span>
							<button id="queryAdvertBtn" class="button">查询</button>
						</div>
					</div>
				</div>
				
				<!-- 广告grid table -->
				<table id="advert_list"></table>
				<div id="advert_pager"></div>
			</div>
			
			<!-- 置顶推广pannel -->
			<div id="tab_topPromotion" style="display:none;"></div>
		</div>
	</div>
	<!-- 添加广告dialog -->
	<div id="addAdvertDialog" style="display: none;">
		<div class="form">
			<div class="field row">
				<label class="field label" style="vertical-align: middle;">广告位置</label> 
				<select id="advertPosList" class="field value one half wide"></select>
				
				<label class="field label required inline one half wide" >广告名称</label>
				<input type="text"  id="advertName" class="field value two wide " >
				<input type="hidden" id="advertId">
			</div>
			
			<div class="field row" style="height:70px;">
				<label class="field label" >广告描述</label>
				<textarea id="advertDesc" class="field value two wide" style="height:50px; width: 310px" ></textarea>
			</div>
			
			<span class="normal divider"></span>
			
			<div id="addAdvertLinkActionRow" class="field row">
				<label class="field label">添加图片</label>
				<input name="file" type="file" id="fileToUploadFileIcon" multiple="multiple" class="field value two wide"  /> 
				<button class="normal button" id="btnfileToUploadFileIcon">上传</button>
			</div>
			<div class="field row" style="height: auto; line-height: auto;">
				<table class="gridtable" id="advertLinkTable" style="width: 100%;">
					<thead>
						<tr>
							<th width="13%"><label class="normal label">图片</label></th>
							<th width="47%"><label class="normal label">广告链接</label></th>
							<th width="40%"><label class="normal label">操作</label></th>
						</tr>
					</thead>
					<tbody id="advertLinkTbd"></tbody>
				</table>
			</div>
			
			<div id="caroDIV" class="field row" style="display: none;">
				<span class="normal divider" style="margin-bottom: 5px;"></span>
				<label class="field label">轮播动画</label>
				<select id="advertCaroAnimList" class="field value one half wide"></select>
				<label class="field inline label one half wide" style="vertical-align: middle;">轮播时间间隔</label> 
				<input type="text"  id="advertCaroIntv" class="field value" >
			</div>
		</div>
	</div>
	
<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
	var jqTabsCtrl = null;
	//
	var curAction = "view";
	//实例化表单代理
	var advertFormProxy = FormProxy.newOne();
	//
	var orgAdvertName = null;
	var isExsit = false;
	//
	advertFormProxy.addField({
		id : "advertName",
		required : true,
		rules : ["maxLength[30]",{
			rule : function(idOrName, type, rawValue, curData) {
				if(curAction == "mod"){
					if(!isNoB(orgAdvertName) && orgAdvertName == rawValue){
						return true;
					}
				}
				//
				isExsitAdvertByName(rawValue);
				return !isExsit;
			},
			message : "名称已被占用！"
		} ]
	});
	
	advertFormProxy.addField({
		id : "advertId",
		type : "int"
	});
	
	advertFormProxy.addField("advertPosList");
	
	advertFormProxy.addField({
		id : "advertDesc",
		rules : ["maxLength[250]"]
	});
	
	advertFormProxy.addField("advertCaroAnimList");
	
	advertFormProxy.addField({
		id : "advertCaroIntv",
		type : "int",
		rules : ["isNatual"]
	});
	//
	var advertGridCtrl = null;
	//
	var advertGridHelper = JqGridHelper.newOne("");
	//扩展对话框按钮栏控件（复选框）
	var dlgToolbarCtrlsName = "dlg-toolbar-ctrls";
	var dlgToolbarCtrlsHtml = '<span style="float:left;padding-left:15px;line-height:40px;color:blue;" class="align middle" name="' + dlgToolbarCtrlsName + '"><input class="align middle" type="checkbox" name="' + dlgToolbarCtrlsName + '-checkbox"  id="' + dlgToolbarCtrlsName + '-checkbox-id" />&nbsp;<label class="align middle" for="' + dlgToolbarCtrlsName + '-checkbox-id">保存后继续添加<label></span>';
	var dlgToolbarCtrls = null;
	//是否保存后转到查看对话框
	var viewAfterSaving = true;
	//是否支持连续添加
	var continuousSupported = true;
	//
	var jqDlgDom = null;
	//
	var trCount = 0;
	//
	$(function(){
		//页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 70,
			allowTopResize : false
		});
		//加载tab页面
		jqTabsCtrl = $("#tabs").tabs();
		//
		initAdvertGrid();
		//
		loadAdvertList();
		//
		initFileUpload("fileToUploadFileIcon");
		//绑定新增模块上传按钮
		$id("btnfileToUploadFileIcon").click(function(){
			fileToUploadFileIcon("fileToUploadFileIcon","","","add");
		});
		//加载广告位置数据
		loadAdvertPos();
		//
		loadCaroAnims();
		//
		$id("addAdvertBtn").click(openAddDlg);
		//
		$id("queryAdvertBtn").click(loadAdvertList);
		//
		$id("batchDeleteBtn").click(goDeleteBatchAdverts);
		// 页面自适应
		winSizeMonitor.start(adjustCtrlsSize);
	})
	
	//
	function goDeleteBatchAdverts(){
		var ids = advertGridCtrl.jqGrid("getGridParam", "selarrrow");
		if(!ids || ids.length == 0){
			Toast.show("请选择需要删除的数据~", 3000, "warning");
			return;
		}
		var theLayer = Layer.confirm('确定要删除该项么？', function() {
			theLayer.hide();
			var int_ids = [];
			for(var i=0; i<ids.length; i++){
				var id = ids[i];
				int_ids.add(ParseInt(id));
			}
			deleteBatchAdverts(int_ids);
		});
	}
	//
	function deleteBatchAdverts(ids){
		var hintBox = Layer.progress("正在删除...");
		//
		var ajax = Ajax.post("/market/advert/delete/by/ids");
		data = {ids:ids};
		ajax.data(data);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				advertGridCtrl.jqGrid().trigger("reloadGrid");
				Layer.msgSuccess(result.message);			
			} else {
				Layer.msgWarning(result.message);
			}
			getCallbackAfterGridLoaded = function(){
				return function(){};
			};
		});
		ajax.always(function() {
			hintBox.hide();
		});
		ajax.go();
	}
	
	// 加载轮播效果
	function loadCaroAnims() {
		loadSelectData("advertCaroAnimList", getDictSelectList("caroAnim","", "-无-"));
	}
	
	//删除图片链接
	function deleteAdvertLink(THIS) {
		var theLayer = Layer.confirm('确定要删除该项么？', function() {
			var linkId = $(THIS).attr("data-id");
			var theTr = $(THIS).parent().parent().parent();
			if (isNoB(linkId) || linkId == "undefined") {
				theLayer.hide();
				$(theTr).remove();
				Layer.msgSuccess("操作成功");
				//判断是否显示轮播设置
				var advertLinkCount = $id("advertLinkTbd").find("tr").size();
				if(advertLinkCount < 2){
					$id("caroDIV").css("display", "none");
				}
			} else {
				var ajax = Ajax.post("/market/advertLink/delete/do");
				data = { id : linkId };		
				ajax.data(data);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						theLayer.hide();
						$(theTr).remove();
						Layer.msgSuccess(result.message);
						//判断是否显示轮播设置
						var advertLinkCount = $id("advertLinkTbd").find("tr").size();
						if(advertLinkCount < 2){
							$id("caroDIV").css("display", "none");
						}
					} else {
						Layer.msgWarning(result.message);
					}
				});
				ajax.go();
			}
		});
	}
	
	function isExsitAdvertByName(advertName){
		var ajax = Ajax.post("/market/advert/exist/by/name");
		ajax.data({
			name : advertName
		});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			isExsit = result.data;
		});
		ajax.go();
	}
	
	// 加载预定义广告位置
	function loadAdvertPos() {
		var ajax = Ajax.post("/market/advertPos/list/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var posOptions = result.data;
				loadSelectData("advertPosList", posOptions);
				//
				$id("advertPosList").on("change", function(){
					var advertPos = $(this).val();
					if(!isNoB(advertPos)){
						$id("advertName").val(advertPos);
						$id("advertName").prop("readonly", true);
					}else{
						$id("advertName").val("");
						$id("advertName").removeAttr("readonly");
					}
				});
				
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	
	//获取图片链接对象
	function getAdvertLinks(){
		var arrLinks = [];
		var advertLinkTrs = $id("advertLinkTbd").find("tr");
		for(var i=0; i<advertLinkTrs.length; i++){
			var advertLinkTr = advertLinkTrs[i];
			var id = $(advertLinkTr).attr("data-id");
			var advertLinkImage = $(advertLinkTr).find("td>img[data-role='advertLinkImage']");
			var imageUuid = $(advertLinkImage).attr("data-uuid");
			var imageUsage = "image.advert";
			var imagePath = $(advertLinkImage).attr("data-relPath");
			var seqNo = i+1;
			var advertLinkInput = $(advertLinkTr).find("td>input[data-role='advertLinkUrl']");
			var linkUrl = $(advertLinkInput).val();
			if(!linkUrl){
				Toast.show("亲，你还未添加广告链接哦~", 3000, "warning");
				return arrLinks;
			}
			//
			var advertLink = {
					id : (id == "undefined" || id == "") ? null : id,
					imageUuid : imageUuid,
					imageUsage : imageUsage,
					imagePath : imagePath,
					seqNo : ParseInt(seqNo),
					linkUrl : linkUrl
			}
			//
			arrLinks.add(advertLink);
		}
		return arrLinks;
	}
	
	function saveAdvert(advert){
		var hintBox = Layer.progress("正在修改...");
		//
		var ajax = Ajax.post("/market/advert/add/do");
		ajax.data(advert);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var advertId = result.data;
				Layer.msgSuccess(result.message);			
				//加载最新数据列表
				advertGridCtrl.jqGrid().trigger("reloadGrid");
				//连续新增
				if (continuousSupported) {
					var continuousFlag = jqDlgDom.prop("continuousFlag");
					if (continuousFlag) {
						getCallbackAfterGridLoaded = function(){
							return function(){
								openAddDlg();
							};
						};
						return;
					}
				}
				//
				if (viewAfterSaving) {
					getCallbackAfterGridLoaded = function(){
						return function(){
							openViewDlg(advertId);
						};
					};
					return;
				}
				//
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.always(function() {
			hintBox.hide();
		});
		ajax.go();
	}
	
	function updateAdvert(advert){
		var hintBox = Layer.progress("正在修改...");
		//
		var ajax = Ajax.post("/market/advert/update/do");
		ajax.data(advert);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var advertId = advert.id;
				Layer.msgSuccess(result.message);			
				//加载最新数据列表
				advertGridCtrl.jqGrid().trigger("reloadGrid");
				//
				if (viewAfterSaving) {
					getCallbackAfterGridLoaded = function(){
						return function(){
							openViewDlg(advertId);
						};
					};
					return;
				}

			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.always(function() {
			hintBox.hide();
		});
		ajax.go();
	}
	
	//显示对话款
	function toShowTheDlg(advertId) {
		//
		var theDlgId = "addAdvertDialog";
		jqDlgDom = $id(theDlgId);
		//清除扩展的对话框按钮栏控件
		if (continuousSupported) {
			var jqButtonPane = jqDlgDom.siblings(".ui-dialog-buttonpane");
			dlgToolbarCtrls = jqButtonPane.find("[name='" + dlgToolbarCtrlsName + "']");
			dlgToolbarCtrls.remove();
		}
		//对话框配置
		var dlgConfig = {
			width : Math.min(700, $window.width()),
			height : Math.min(600, $window.height()),
			modal : true,
			open : false
		};
		//
		if (continuousSupported) {
			//显示“保存后继续添加”复选框
			dlgConfig.open = function(event, ui) {
				//
				var jqButtonPane = jqDlgDom.siblings(".ui-dialog-buttonpane");
				dlgToolbarCtrls = $($(dlgToolbarCtrlsHtml).appendTo(jqButtonPane));
				var theCheckbox = dlgToolbarCtrls.find("[name='" + dlgToolbarCtrlsName + "-checkbox']");
				var continuousFlag = jqDlgDom.prop("continuousFlag");
				theCheckbox.prop("checked", continuousFlag);
				//
				theCheckbox.bind("click", function() {
					jqDlgDom.prop("continuousFlag", this.checked);
				});
			};
		}
		//
		if (curAction == "add") {
			//
			singleSet("advertPosList","");
			//
			dlgConfig.title = "广告 - 新增";
			dlgConfig.buttons = {
				"保存" : function() {
					var result = advertFormProxy.validateAll();
					if(result){
						var advertLinks=getAdvertLinks();
						if(advertLinks.length < 1){
							return;
						}
						//
						var postData = {
								posCode : textGet("advertPosList"),
								name: textGet("advertName"),
								seqCount : advertLinks.length,
								desc : textGet("advertDesc"),
								caroAnim: textGet("advertCaroAnimList"),
								caroIntv : intGet("advertCaroIntv"),
								advertLinks:advertLinks
						};
						saveAdvert(postData);
					}
				},
				"取消" : function() {
					//
					jqDlgDom.prop("continuousFlag", false);
					//
					$(this).dialog("close");
					//隐藏表单验证消息
					advertFormProxy.hideMessages();
					//
					getCallbackAfterGridLoaded = function(){
						return function(){
						};
					};
				}
			};
			
		} else if (curAction == "mod") {
			//
			dlgConfig.title = "广告 - 修改";
			dlgConfig.buttons = {
				"保存" : function() {
					var result = advertFormProxy.validateAll();
					if(result){
						var advertLinks=getAdvertLinks();
						if(advertLinks.length<1){
							return;
						}
						//
						var postData = {
								id : intGet("advertId"),
								posCode : textGet("advertPosList") || null,
								name: textGet("advertName") || null,
								seqCount : advertLinks.length,
								desc : textGet("advertDesc") || null,
								caroAnim: textGet("advertCaroAnimList") || null,
								caroIntv : intGet("advertCaroIntv"),
								advertLinks:advertLinks
						};
						//
						updateAdvert(postData);
					}
				},
				"取消" : function() {
					$(this).dialog("close");
					//隐藏表单验证消息
					advertFormProxy.hideMessages();
					//
					getCallbackAfterGridLoaded = function(){
						return function(){
						};
					};
				}
			};
		} else {
			//== view 查看
			dlgConfig.title = "广告 - 查看";
			dlgConfig.buttons = {
				"修改 > " : function() {
					$(this).dialog("close");
					//
					openModDlg(advertId);
				},
				"关闭" : function() {
					$(this).dialog("close");
					//
					getCallbackAfterGridLoaded = function(){
						return function(){
						};
					};
				}
			};
		}
		//
		jqDlgDom.dialog(dlgConfig);
		//
		initTheDlgCtrls(advertId);
	}
	
	function initTheDlgCtrls(advertId){
		//重置控件值
		textSet("advertName", "");
		textSet("advertDesc", "");
		$id("advertLinkTbd").html("");
		textSet("advertCaroAnimList", "");
		intSet("advertCaroIntv", "");
		//
		if (curAction == "view" || curAction == "mod") {
			var advert = advertGridHelper.getRowData(advertId);
			if (advert != null) {
				//保留初值
				orgAdvertName = advert.name;
				//
				textSet("advertPosList", advert.posCode);
				textSet("advertName", advert.name);
				textSet("advertDesc", advert.desc);
				intSet("advertId", advertId);
				textSet("advertCaroAnimList", advert.caroAnim);
				intSet("advertCaroIntv", advert.caroIntv);
				
				var advertLinks = advert.advertLinks;
				if(advertLinks != null && advertLinks.length > 0){
					for(var i=0; i<advertLinks.length; i++){
						var advertLink = advertLinks[i];
						//
						renderAppendDetail(advertLink, "advertLinkTpl", "advertLinkTbd");
						
					}
				}
			}
		}
		
		//批量简单设置
		var actionSpans = $id("advertLinkTbd").find("tr>td[data-role='actionTd']>span");
		//
		if (curAction == "add" || curAction == "mod") {
			jqDlgDom.find(":input").prop("disabled", false);
			$id("addAdvertLinkActionRow").css("display", "block");
			if(actionSpans.length > 1){
				$id("caroDIV").css("display", "block");
			}else{
				$id("caroDIV").css("display", "none");
			}
			//
			$(actionSpans).css("display", "block");
		} else {
			jqDlgDom.find(":input").prop("disabled", true);
			$id("addAdvertLinkActionRow").css("display", "none");
			//隐藏操作列
			$(actionSpans).css("display", "none");
			
		}
	}
	
	//打开新增对话框
	function openAddDlg() {
		curAction = "add";
		//
		$id("caroDIV").css("display", "none");
		//
		toShowTheDlg();
	}
	
	//打开查看对话框
	function openViewDlg(advertId) {
		curAction = "view";
		//
		toShowTheDlg(advertId);
	}
	
	//打开修改对话框
	function openModDlg(advertId) {
		curAction = "mod";
		//
		toShowTheDlg(advertId);
	}
	
	//打开删除对话框
	function openDelDlg(advertId) {
		var theSubject = "选中的广告";
		var theLayer = Layer.confirm("确定要删除" + theSubject + "吗？", function() {
			theLayer.hide();
			//
			deleteAdvertById(advertId);
		});
	}
	
	//删除广告
	function deleteAdvertById(advertId){
		var hintBox = Layer.progress("正在删除...");
		//
		var ajax = Ajax.post("/market/advert/delete/do");
		data = { id:advertId };
		ajax.data(data);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				advertGridCtrl.jqGrid().trigger("reloadGrid");
				Layer.msgSuccess(result.message);			
			} else {
				Layer.msgWarning(result.message);
			}
			getCallbackAfterGridLoaded = function(){
				return function(){};
			};
		});
		ajax.always(function() {
			hintBox.hide();
		});
		ajax.go();
	}
	
	function loadAdvertList(){
		//
		var filter = {};
		var advertNo = intGet("qryAdvertNo");
		if(advertNo){
			filter.id = advertNo;
		}
		var advertName = textGet("qryAdvertName");
		if(advertName){
			filter.name = advertName;
		}
		//
		var gridParamMap = {
				url : getAppUrl("/market/advert/list/get"),
				contentType : 'application/json',
				mtype : "post",
				datatype : 'json',
				postData :{filterStr : JSON.encode(filter,true)},
				page:1
		};
		//重新加载广告数据
		advertGridCtrl.jqGrid("setGridParam", gridParamMap).trigger("reloadGrid");
		getCallbackAfterGridLoaded = function(){
			return function(){};
		};
	}
	
	function initAdvertGrid(){
		var advertGridConfig = {
			height : "100%",
			width : "100%",
			colNames : [ "广告编号","广告位置", "广告名称","描述", "操作" ],
			colModel : [{name : "id",index : "id",width : 150},
			            {name : "advertPos",index : "advertPos",align : "left",width : 220,formatter : function (cellValue) {
							return isNoB(cellValue)?'自定义':cellValue.name;
						}},
						{name : "name",index : "name",align : "left",width : 220},
						{name : "desc",index : "desc",align : "left",width : 220},
						{name : 'id',index : 'id',formatter : function(cellValue,option, rowObject) {
							return "&nbsp;&nbsp;<span> [<a class='item' href='javascript:void(0);' onclick='openViewDlg("
									+ cellValue
									+ ")' >查看</a></span>]"
									+ "&nbsp;&nbsp;&nbsp;<span> [<a class='item' href='javascript:void(0);' onclick='openModDlg	("
									+ cellValue
									+ ")' >修改</a>]</span>"
									+ "&nbsp;&nbsp;&nbsp;<span> [<a  class='item' href='javascript:void(0);' onclick='openDelDlg("
									+ cellValue
									+ ")' >删除</a>]</span>";
						},
						align : "center",
						width : 200
					}],
			pager : "#advert_pager",//分页div
			rowList:[5,10,15],
			multiselect:true, //定义是否可以多选
			multikey:'ctrlKey',
			loadComplete : function(gridData){
				advertGridHelper.cacheData(gridData);
				var callback = getCallbackAfterGridLoaded();
				if(isFunction(callback)){
					callback();
				}

			},
			ondblClickRow: function(rowId) {
				openViewDlg(rowId);
			}
		};
		//
		advertGridCtrl = $id("advert_list").jqGrid(advertGridConfig);
	}
	
	//------------------------上传-------------------------
	
	//上传更新图片
	function fileToUploadFileIcon(fileId,count,fileUuid,status){
		if(isNoB(fileUuid)){
			var formData = {
				usage : "image.advert"
			};
		}else{
			var formData ={
				update : true,
				uuid : fileUuid
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
					if(status=="add"){
						var fileData=resultInfo.data.files;
						  for(var i=0;i<fileData.length;i++){
							  var renderData={
										imageUuid :resultInfo.data.files[i].fileUuid,
										imageUsage :resultInfo.data.files[i].fileUsage,
										imagePath :resultInfo.data.files[i].fileRelPath,
										imageUrl :resultInfo.data.files[i].fileBrowseUrl
									};
							  
							  renderAppendDetail(renderData, "advertLinkTpl", "advertLinkTbd");
							  //当前行加1
							  trCount++;
						  }
					}else{
						$("#img"+count).attr("src",resultInfo.data.files[0].fileBrowseUrl+"?"+new Date().getTime());
					}
				}
			},fail : function(e, data) {
				console.log(data);
			},noFilesHandler : function() {
				Layer.msgWarning("请选择或更换图片");
			},
			fileNamesChecker : function(fileNames) {
				fileNames = fileNames || [];
				if(status=="add"){
					for (var i = 0; i < fileNames.length; i++) {
						if (!isImageFile(fileNames[i])) {
							Layer.msgWarning("只能上传图片文件");
							return false;
						}
					}	
				}else{
					if(fileNames.length>1){
						Layer.msgWarning("修改时只能修改一张图片");
						return false;
					}
					for (var i = 0; i < fileNames.length; i++) {
						if (!isImageFile(fileNames[i])) {
							Layer.msgWarning("只能上传图片文件");
							return false;
						}
					}	
				}
				return true;
			}
		});
	}
	
	function pervAdvertLink(THIS){
		var insert = $(THIS).closest ('tr');
        var tr = insert.prev ('tr');
        tr.before (insert);
	}
	
	function nextAdvertLink(THIS){
		var insert = $(THIS).closest ('tr');
        var tr = insert.next ('tr');
        tr.after (insert);
	}
	
	//将数据的值赋给静态html
	function renderAppendDetail(d, html, div) {
		var tplHtml = $id(html).html();
		var theTpl = laytpl(tplHtml);
		var htmlStr = theTpl.render(d);
		$id(div).append(htmlStr);
		//
		initFileUpload("fileToUpload"+trCount);
		//判断是否显示轮播设置
		var advertLinkCount = $id("advertLinkTbd").find("tr").size();
		if(advertLinkCount > 1){
			$id("caroDIV").css("display", "block");
		}
	}
	
	//------------------------调整控件大小-------------------------
	
	//调整控件大小
	function adjustCtrlsSize() {
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		//
		var tabsCtrlWidth = mainWidth - 4;
		var tabsCtrlHeight = mainHeight - 8;
		jqTabsCtrl.width(tabsCtrlWidth);
		var tabsHeaderHeight = $(">[role=tablist]", jqTabsCtrl).height();
		var tabsPanels = $(">[role=tabpanel]", jqTabsCtrl);
		var panelWidth = ParseInt(tabsCtrlWidth - 4 * 2 );
		var panelHeight = ParseInt(tabsCtrlHeight - tabsHeaderHeight - 30 );
		tabsPanels.width(panelWidth);
		tabsPanels.height(panelHeight);
		
		var gridCtrlId = "advert_list";
		var jqGridBox = $("#gbox_" + gridCtrlId);
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
		var pagerHeight = $id("advert_pager").height();
		//
		var tabsWidth = tabsPanels.width();
		var tabsHeight = tabsPanels.height();
		advertGridCtrl.setGridWidth(tabsWidth - 3);
		advertGridCtrl.setGridHeight(tabsHeight - headerHeight - pagerHeight - 3 - 50);
	}
	
	function getCallbackAfterGridLoaded(){}
</script>

<!--TODO <input name="file" type="file" id="fileToUpload{{ d.trCount }}" multiple="multiple" class="field value " />
<button class="normal button" onclick="fileToUploadFileIcon('fileToUpload{{ d.trCount }}',{{ d.trCount }},'{{ d.imageUuid }}','update')">更换图片</button> -->
</body>
<script type="text/html" id="advertLinkTpl">
<tr data-id="{{ d.id }}">
	<td class="align center middle">
		<img data-role="advertLinkImage" data-relPath="{{ d.imagePath }}" src="{{ d.imageUrl }}" data-uuid="{{ d.imageUuid }}" style="width: 35px; height: 35px; vertical-align: middle; padding: 3px 0px;" />
	</td>
	<td>
		<label class="label required"></label> 
		<input class="normal input two wide" data-role="advertLinkUrl" value="{{ !isNoB(d.linkUrl)?d.linkUrl:'' }}"/>
	</td>
	<td data-role="actionTd">
		<span class="align center middle" style="display: block;"> [
			<a class="item" data-id="{{ d.id }}" href="javascript:void(0);" onclick="deleteAdvertLink(this)">删除</a>
			]<span class='chs spaceholder'></span>
			[
			<a class="item" href="javascript:void(0);" onclick="pervAdvertLink(this)">上移</a>
			]<span class='chs spaceholder'></span>
			[
			<a class="item" href="javascript:void(0);" onclick="nextAdvertLink(this)">下移</a>
			]
		</span>
	</td>
</tr>
</script>

</html>