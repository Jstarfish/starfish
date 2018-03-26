<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>修改商品</title>
<link rel="stylesheet" href="<%=resBaseUrl%>/lib/ztree/css/zTreeStyle.css" />
<style type="text/css">
table.gridtable {
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-collapse: collapse;
}

table.gridtable th {
	/* border:1px solid #d3d3d3;
	border-width: 1px; */
	padding: 3px;
	line-height: 23px;
	text-align: right;
}

table.gridtable td {
	/* border:1px solid #d3d3d3;
	border-width: 1px; */
	padding: 3px;
	line-height: 23px;
}

table.noBorderTab td{
	border-style: hidden;
}


div.menu ul{
	list-style:none;
    margin: 0px;
    padding: 0px;
    width: 1218px;
}

div.menu ul li{
	float:left;
	width: 230px;
	height: 185px;
}

.ulLayout{
	margin:0; 
	padding:0; 
	list-style:none;
}
.ulLayout li{
	display:inline-block;
	height: 30px;
	line-height: 30px;
	vertical-align: middle;
	padding:0px 5px;
	border:1px dashed #AAA;
	border-radius: 4px;
}
.ulLayout li:HOVER{
	background: #005AA0 none repeat scroll 0% 0%;
	color: #FFF;
}

.default.image.marker {
	position: absolute;
	top: 4px;
	right: 4px;
	width: 24px;
	height: 24px;
	line-height: 20px;
	border: 1px solid #AAA;
}

</style>
</head>

<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<label>商品管理   &gt; 商品修改</label>
				<div class="group right aligned" style="height:100%;">
					<button id="saveBtn" class="normal button">保存</button>
					<span class="vt divider"></span>
					<button id="retGoodsListBtn" class="normal one and half wide button">返回商品列表</button>
				</div>
			</div>
			
		</div>
	</div>
	
	<div id="mainPanel" class="ui-layout-center" style="padding: 0px;">
		<div id="tabs" class="noBorder">
			<ul style="border-bottom-color:#ccc;">
				<li><a href="#tab_basisInfo">基础信息</a></li>
				<li><a href="#tab_album">商品相册</a></li>
				<li><a href="#tab_products">具体商品</a></li>
				<li><a href="#tab_intro">商品介绍</a></li>
				<li style="display:none;"><a href="#tab_relatedGoods">相关商品</a></li>
				<li style="display:none;"><a href="#tab_parts">商品配件</a></li>
				<div class="filter section right align">
					<button id="addProductBtn" style="display:none;" class="normal button">添加</button>
				</div>
			</ul>
			<div id="tab_basisInfo" class="form">
				<div class="filter section">
					<div class="field row">
						<div class="field group">
							<label class="field label required one half wide">商品名称</label>
							<input id="goodsName" class="field two wide value"/>
						</div>
						<div class="field group">
							<label class="field label one half wide">副标题</label>
							<input id="goodsTitle" class="field two wide value" />
						</div>
					</div>
					<div class="field row">
						<div class="field group">
							<label class="field label required one half wide">商品分类</label>
							<input id="goodsCat" disabled data-hasSpec="" data-id="" data-parentId="" data-idPath="" readonly="readonly" style="line-height: 28px;" placeholder="请选择..." class="field two wide value" />
						</div>
						<div class="field group">
							<label class="field label one half wide">商品单位</label>
							<input disabled style="background-color: #fff;" class="field two wide value" id="categ_unit" />
						</div>
					</div>
					<div class="field row">
						<label class="field label required one half wide">供应商</label>
						<input id="vendorId" data-id="" class="field two wide value" readonly="readonly" style="line-height: 28px;" placeholder="请选择..."/>
					</div>
					<div class="field row" style="height: auto;">
						<div class="field group" style="height: auto;">
							<label class="field label one half wide">包装清单</label>
							<textarea id="packList" cols="45" rows="4" style="border-radius:4px; color:navy;"></textarea>
						</div>
					</div>
				</div>
				
				<div id="atrrs" class="filter section" style="display: block;">
					<div class="field row"><label>商品属性</label></div>
					<span class="hr divider"></span>
					<div class="field row">
						<label class="field label required one half wide">关键属性</label>
					</div>
					<div class="field row" style="height: auto;">
						<table id="keyAttr_tab" class="gridtable" style="width:500px; margin-left:20px;">
							<tr><td><label class="field label" style="margin-left:100px; text-align: left;">暂无数据</label></td></tr>
						</table>
					</div>
					<span class="hr divider"></span>
					<div class="field row">
						<label class="field label one half wide">非关键属性</label>
					</div>
					<div class="field row" style="height: auto;">
						<table id="noKeyAttr_tab" class="gridtable" style="width:500px; margin-left:20px;">
							<tr><td><label class="field label" style="margin-left:100px; text-align: left;">暂无数据</label></td></tr>
						</table>
					</div>
				</div>
			</div>
			<div id="tab_album">
				<div class="form">
					<table style="width: 100%;">
						<tr>
							<td>
								<label class="field one and half wide label">商品相册文件</label>
								<input class="hidden file" name="file" type="file" id="fileToUploadImage"  multiple="multiple"/>
							</td>
							<td>
								<button id="uploadBtn" class="normal button" onclick="fileToUploadImage();">上传</button>
							</td>
						</tr>
						<tr>
							<td colspan="2" style="border-bottom-width: 1px; border-bottom-color: #EEEEEE; border-bottom-style: solid;">
								
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div>
							        <ul id="goods_album_list" class="fluid list middle xy sortable"></ul>
							    </div>
							</td>
						</tr>
					</table>
				</div>
				
			</div>
			<div id="tab_products" style="overflow-y: scroll;">
				<div id="products"></div>
				<div id="dialog" style="display: none; text-align: center;">
					<label id="saveRetMsg" style="width: 100%; display: inline-block;height: 50px;line-height: 50px;font-size: 16px;">添加成功！</label>
					<div class="group" style="height:30px;">
						<button id="goOnEditBtn" style="width:130px;" class="normal button">继续修改></button>
						<span class="normal spacer"></span>
						<button id="retGoodsListBtn" style="width:130px;" class="normal button">返回商品列表</button>
					</div>
				</div>
				
				<div id="freeCombDialog" style="display: none; text-align: center;">
					<div id="specs_tab" class="form"></div>
				</div>
				
			</div>
			<div id="tab_intro">
				<textarea id="goods_intro_editor"></textarea>
			</div>
			<div id="tab_relatedGoods">
			</div>
			<div id="tab_parts">
			</div>
			
			<div id="categTreeDialog" style="display: none; text-align: center;">
				<div id="menuContent" class="menuContent" style="display:none; position: absolute;">
					<ul id="catTree" class="ztree"></ul>
				</div>
			</div>
			
		</div>
		
	</div>
		
	<!--选择供应商dialog -->
	<div id="selectVendorDialog" class="form" style="display: none;height: 600px;">
		<div class="ui-layout-center">
			<div class="filter section">
				<div class="filter row">
					<div class="group left aligned">
						<label class="label">供应商名称</label>
						<input id="queryVendorName" class="input value one half wide" /> 
						<span class="vt divider"></span>
						<button id="queryVendorBtn" class="button">查询</button>
					</div>
				</div>
			</div>
			<table id="vendorGrid"></table>
			<div id="vendorPager"></div>
		</div>
	</div>
	<!-- <textarea id='packList_{{ d.vid }}' cols='44' rows='4' style='border-radius:4px; color:navy; display:inline-block;'>{{ d.product.packList ? d.product.packList : '' }}</textarea> -->
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
	
	//----------------------------------------全局变量----------------------------------------------------------------
	//
	var jqTabsCtrl = null;
	//
	var weight_unit = null;
	//
	var specsMap = {};
	//
	var freeCombDialog = null;
	//商品实例化表单代理
	var basisInfoFormProxy = FormProxy.newOne();
	//
	var productFormProxyMap = {};
	//
	var urlParams = extractUrlParams();
	var currentGoodsId = urlParams["goodsId"];
	currentGoodsId = ParseInt(currentGoodsId);
	var actionType = urlParams["action"];
	var pId = urlParams["pId"];
	//
	var albumId = null;
	//
	var introId = null;
	//
	var theEditor = null;
	//
	var colorItems = null;
	//
	//相对路径的解析基础
	CKEDITOR.config.handlers = CKEDITOR.config.handlers || {};
	//
	var currentCategHasSpec = null;
	//
	var selectVendorDialog = null;
	//
	var vendorGridCtrl = null;
	//
	var vendorGridHelper = JqGridHelper.newOne("");
	//
	var categTreeDialog = null;
	//
	var currentCatId = null;
	//
	var currentGoodsName = null;
	//
	var currentVendorId = null;
	
	//--------------------------------------------------------注册表单--------------------------------------------------
	basisInfoFormProxy.addField({
		id : "goodsName",
		required : true,
		messageTargetId : "goodsName"
	});
	
	basisInfoFormProxy.addField({
		id : "goodsCat",
		required : true,
		messageTargetId : "goodsCat"
	});
	
	basisInfoFormProxy.addField({
		id : "vendorId",
		required : true,
		messageTargetId : "vendorId"
	});
	//----------------------------------------page onload----------------------------------------------------------------
	
	$(function() {
		//页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			allowTopResize : false
		});
		//加载tab页面
		jqTabsCtrl = $("#tabs").tabs();
		//
		initCategTreeDialog();
		//初始化商品分类
		initGoodsCat();
		//
		initFreeCombDialog();
		//初始化商品介绍
		initFileUpload("fileToUploadImage");
		//商品介绍图片
		theEditor = CKEDITOR.replace("goods_intro_editor", {
			extraPlugins : 'imagex'
		});
		CKEDITOR.config.handlers['imagex'] = openRepoImageDlg;
		//隐藏布局north分割线
		hideLayoutTogglers();
		$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "1");
		//初始化供应商
		initSelectVendorDialog();
		//
		loadVendorData();
		getCallbackAfterGridLoaded = function(){
			initGoodsData(currentGoodsId);
		}
		//选择供应商
		$id("vendorId").click(goSelectVendor);
		$id("queryVendorBtn").click(queryVendorByName);
		//
		$id("retGoodsListBtn").click(function(){
			var url = getAppUrl("/goods/list/jsp/-mall");
			setPageUrl(url);
		});
		//商品tab页签选中时触发
		jqTabsCtrl.on("tabsactivate", function(event, ui) {
			//隐藏所有from警告信息
			hideFormWarnMsgs();
			//
			var currentPanl = ui.newPanel.selector;
			if(currentPanl=="#tab_products"){
				$id("addProductBtn").css("display","inline-block");
			}else{
				$id("addProductBtn").css("display","none");
			}
			//
			if(currentPanl=="#tab_intro"){
				winSizeMonitor.start(adjustCtrlsSize);
			}
			
		});
		//
		if(!isNoB(actionType) && actionType == "p"){
			jqTabsCtrl.tabs({ active: 2 });
			$id("addProductBtn").css("display","inline-block");
		}
		
		//保存商品
		$id("saveBtn").click(function(){
			if(!isNoB(currentGoodsId)){
				updateGoods(currentGoodsId);
			}
		});
		//页面伸缩调整
		winSizeMonitor.start(adjustCtrlsSize);
	});
	
	//------------------------------------------------------业务处理--------------------------------------------------------------
	
	//
	function existGoodsName(goodsName, catId, vendorId) {
		//如果是当前商品，则不需要验证存在性
		if(goodsName == currentGoodsName && vendorId == currentVendorId){
			return false;
		}
		//
		var retV = false;
		var ajax = Ajax.post("/goods/name/exist/do");
		//
		ajax.data({
			goodsName : goodsName,
			catId : catId,
			vendorId : vendorId
		});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				retV = result.data;
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
		//
		return retV;
	}
	//	
	function getBasisInfoMap() {
		var basisInfoMap = {};
		basisInfoMap["id"] = currentGoodsId;
		var categId = $id("goodsCat").attr("data-id");
		if (isNoB(currentCatId)) {
			Layer.msgWarning("请选择商品分类！");
			return;
		}
		basisInfoMap["catId"] = ParseInt(currentCatId);
		var vendorId = $id("vendorId").attr("data-id");
		basisInfoMap["vendorId"] = isNoB(vendorId) ? null : ParseInt(vendorId);
		basisInfoMap["catPath"] = $id("goodsCat").attr("data-idPath");
		goodsName = textGet("goodsName");
		basisInfoMap["name"] = goodsName;
		var title = textGet("goodsTitle");
		basisInfoMap["title"] = title;
		var packList = textGet("packList");
		basisInfoMap["packList"] = packList;
		//
		var attrList = [];
		$("[data-role='goodsAttr']").each(function() {
			if ($(this).attr("multi-flag") || $(this).attr("multi-flag") == "true") {
				var keyFlag = $(this).attr("key-flag");
				var refCode = $(this).attr("ref-code");
				var attrId = $(this).attr("attr-id");
				$(this).find("input[name='" + $(this).attr("ref-code") + "']:checked").each(function() {
					var dataMap = {};
					dataMap["keyFlag"] = new Boolean(keyFlag);
					dataMap["refCode"] = refCode;
					dataMap["attrId"] = ParseInt(attrId);
					dataMap["attrVal"] = $(this).val();
					dataMap["attrItemId"] = $(this).attr("item-id") == null ? null : ParseInt($(this).attr("item-id"));
					dataMap["goodsId"] = currentGoodsId;
					dataMap["id"] = $(this).attr("attrVal-id");
					attrList.add(dataMap);
				});
			} else {
				var attrVal = $(this).val();
				if (!isNoB(attrVal)) {
					var dataMap = {};
					var keyFlag = $(this).attr("key-flag");
					var brandFlag = $(this).attr("brand-flag");
					var refCode = $(this).attr("ref-code");
					var attrId = $(this).attr("attr-id");
					dataMap["keyFlag"] = new Boolean(keyFlag);
					dataMap["brandFlag"] = new Boolean(brandFlag);
					dataMap["refCode"] = refCode;
					dataMap["attrId"] = ParseInt(attrId);
					dataMap["attrVal"] = $(this).val();
					dataMap["goodsId"] = currentGoodsId;
					dataMap["id"] = $(this).attr("attrVal-id");
					var item_id = null;
					item_id = $(this).find("option[value='" + $(this).val() + "']").attr("item-id");
					if (!isNoB(item_id)) dataMap["attrItemId"] = ParseInt(item_id);
					attrList.add(dataMap);
				}
	
			}
	
		});
		basisInfoMap["attrVals"] = attrList;
		return basisInfoMap;
	}
	//
	function validateProduct(vid) {
		var productFormProxy = productFormProxyMap[vid];
		//
		var vldResult = true;
		if (!isNoB(productFormProxy)) {
			vldResult = productFormProxy.validateAll();
		}
		//
		return vldResult;
	}
	//
	function validateGoods() {
		// 验证商品基本信息
		var result = validateBasisInfo();
		if (!result) {
			jqTabsCtrl.tabs({
				active : 0
			});
			return false;
		}
		// 验证具体商品信息
		$id("products").find("div[data-role^='product_item']").each(function() {
			var productStr = $(this).attr("data-role");
			var vid = productStr.substring(13, productStr.length);
			result = validateProduct(vid) && result;
		});
		//
		if (!result) {
			jqTabsCtrl.tabs({
				active : 2
			});
			return false;
		}
		//
		return true;
	}
	//
	function validateBasisInfo() {
		var vldResult = basisInfoFormProxy.validateAll();
		if(!vldResult) return false;
		//
		if(vldResult){
			var goodsName = textGet("goodsName");
			var catId = $id("goodsCat").attr("data-id");
			var vendorId = $id("vendorId").attr("data-id");
			//检查商品名称唯一性，true：存在、false：不存在
			var retV = existGoodsName(goodsName, catId, vendorId);
			if(retV){
				Toast.show("商品名称已经存在!", 3000, "warning");
				return false;
			}
		}
		//
		if (vldResult) {
			$id("keyAttr_tab").find("input[key-flag='true']").each(function() {
				if (isNoB($(this).val())) {
					Toast.show("关键属性不能为空!", 3000, "warning");
					return false;
				}
			});
		}
		return true;
	}
	// 根据vid获取产品Map
	function getProductMap(vid) {
		if (isNoB(vid)) return null;
		var categId = $id("goodsCat").attr("data-id");
		if (isNoB(currentCatId)) return null;
		//
		var currentP = $id("products").find("div[data-role='product_item_" + vid + "']:first");
		var productId = $(currentP).attr("data-productId");
		var quantity = intGet("quantity_" + vid);
		var purchPrice = floatGet("purchPrice_" + vid);
		var wholePrice = floatGet("wholePrice_" + vid);
		var origPrice = floatGet("origPrice_" + vid);
		var salePrice = floatGet("salePrice_" + vid);
		var marketPrice = floatGet("marketPrice_" + vid);
		var weight = floatGet("weight_" + vid);
		//var shelfStatus = intGet("shelfStatus_" + vid);
		//
		var productMap = {};
		productMap["id"] = productId;
		productMap["goodsId"] = currentGoodsId;
		productMap["goodsName"] = textGet("goodsName");
		productMap["catId"] = ParseInt(currentCatId);
		productMap["quantity"] = quantity;
		productMap["origPrice"] = origPrice.round(4);
		productMap["salePrice"] = salePrice.round(4);
		productMap["marketPrice"] = marketPrice == null ? null : marketPrice.round(4);
		productMap["purchPrice"] = purchPrice == null ? null : purchPrice.round(4);
		productMap["wholePrice"] = wholePrice == null ? null : wholePrice.round(4);
		productMap["weight"] = weight;
		productMap["shelfStatus"] = 0;
		productMap["packList"] = textGet("packList_" + vid);
		productMap["giftFlag"] = radioGet("giftFlag_" + vid);
		// 货品规格值
		var specList = [];
		$("div[id^='specText_" + vid + "']").each(function() {
			var specMap = {};
			specMap["goodsId"] = currentGoodsId;
			specMap["specId"] = $(this).attr("spec-id");
			specMap["specVal"] = $(this).html();
			specMap["colorFlag"] = $(this).attr("data-colorFlag");
			specMap["refCode"] = $(this).attr("ref-code");
			specMap["specItemId"] = $(this).attr("item-id");
			specMap["id"] = $(this).attr("specval-id");
			specMap["productId"] = productId;
			specList.add(specMap);
		});
		productMap["specVals"] = specList;
		// 货品相册图片
		var pAlbumImgs = [];
		var hrListSelector = $(currentP).find("ul.fluid.list.hr.sortable");
		var selectedItems = productAlbumfluidListHelper.getAll(hrListSelector);
		for (var i = 0; i < selectedItems.length; i++) {
			var imageItem = selectedItems[i];
			//console.log("imageItem=" + JSON.encode(imageItem));
			var pAlbumImg = {};
			var productId = imageItem.productId;
			if (!isNoB(productId)) {
				pAlbumImg["id"] = imageItem.id;
				pAlbumImg["imageId"] = imageItem.imageId;
			} else {
				pAlbumImg["id"] = "";
				pAlbumImg["imageId"] = imageItem.id;
			}
			pAlbumImg["seqNo"] = (i + 1);
			pAlbumImg["fileBrowseUrl"] = imageItem.fileBrowseUrl;
			pAlbumImgs.add(pAlbumImg);
		}
		//
		productMap["productAlbumImgs"] = pAlbumImgs;
		return productMap;
	}
	
	function formGoodsColorImgs(goodsColorImgs){
		var dataList = [];
		for(var key_specItemId in goodsColorImgs){
			var img = goodsColorImgs[key_specItemId];
			var dataMap = {};
			dataMap.id = img.id || null;
			dataMap.goodsId = img.goodsId || currentGoodsId || null;
			dataMap.specId = img.specId || null;
			dataMap.specItemId = img.specItemId || null;
			dataMap.imageId = img.imageId || null;
			dataMap.specVal = img.specVal || null;
			dataList.add(dataMap);
		}
		return dataList;
	}
	//
	function clearGoodsColorImgsByGoodsId(goodsId){
		if(!goodsId) return;
		//
		var ajax = Ajax.post("/goods/album/color/imgs/delete/do");
		ajax.data({goodsId:ParseInt(goodsId)});

		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
			} else {
				Layer.msgWarning(result.message);
			}

		});
		ajax.go();
	}
	//
	function saveGoodsColorImgs(){
		console.log("goodsColorImgs="+JSON.encode(goodsColorImgs));
		if(isEmptyObject(goodsColorImgs)){
			clearGoodsColorImgsByGoodsId(currentGoodsId);
		}else{
			//
			var ajax = Ajax.post("/goods/album/color/imgs/save/do");
			var _goodsColorImgs = formGoodsColorImgs(goodsColorImgs);
			ajax.data(_goodsColorImgs);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
				} else {
					Layer.msgWarning(result.message);
				}

			});
			ajax.go();
		}
	}
	//
	function updateGoods(_goodsId) {
		var result = validateGoods();
		if (!result) return;
		var goodsMap = {};
		var hintBox = Layer.progress("正在保存数据...");
		//保存商品相册颜色分组
		saveGoodsColorImgs();
		// 基本信息
		var basisInfoMap = getBasisInfoMap();
		goodsMap = basisInfoMap;
		// 产品列表信息
		var products = [];
		$id("products").find("div[data-role^='product_item']").each(function() {
			var productStr = $(this).attr("data-role");
			var vid = productStr.substring(13, productStr.length);
			var productMap = {};
			productMap = getProductMap(vid);
			if (!isNoB(productMap)) products.add(productMap);
		});
		goodsMap["products"] = products;
		// 商品介绍
		var goodsIntro = theEditor.getData();
		var introMap = {};
		introMap["id"] = introId;
		introMap["goodsId"] = currentGoodsId;
		introMap["content"] = goodsIntro;
		//
		goodsMap["goodsIntro"] = introMap;
		var ajax = Ajax.post("/goods/goods/save/do");
		ajax.data(goodsMap);

		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				var goods = result.data;
				currentGoodsId = goods.id;
				introId = goods.goodsIntro.id;
				refreshGoods(goods, false);
				Layer.msgSuccess(result.message);
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
	//
	function refreshGoods(goods, isBasisInfo) {
		if (isNoB(goods)) return;
		// 刷新基础信息
		$id("goodsName").val(goods.name);
		$id("goodsTitle").val(goods.title == null ? "" : goods.title);
		refreshCategAttr(goods.catId, goods.attrVals);
		if (!isBasisInfo) {
			// 产品列表
			initProductList(currentGoodsId, currentCategHasSpec);
			// 商品介绍
			initGoodsIntro(goods.id);
		}

	}
	// 删除商品
	function deleteProduct(pid) {
		var hintBox = Layer.progress("正在保存数据...");
		var ajax = Ajax.post("/goods/product/delete/by/id/do");
		ajax.data({"productId" : pid});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.always(function() {
			hintBox.hide();
		});
		ajax.go();
	}
	// 移除商品
	function removeProduct(vid) {
		var theLayer = Layer.confirm('确定要删除吗？', function() {
			theLayer.hide();
			var productId = null;
			var obj = $id("products").find("div[data-role='product_item_" + vid + "']:first");
			productId = $(obj).attr("data-productId");
			$(obj).slideUp(400, function() {
				$(obj).remove();
			});
			// 置空货品表单
			var productFormProxy = productFormProxyMap[vid];
			if (!isNoB(productFormProxy)) {
				productFormProxy = null;
			}
			if (!isNoB(productId))
				deleteProduct(productId);
		});
	}
	//
	function addFreeCombProducts(specCartList){
		if(specCartList.length > 6){
			Toast.show("亲，当前操作将创建过多产品哦！建议减少选择的规格值~", 3000, "warning");
			return;
		}
		//
		$(specCartList).each(function() {
			var product = {id:"", specVals:this};
			createAndRenderProduct(true, product, true);
		});
	}
	// 笛卡尔积
	function cartProduct(paramArray) {
		function addTo(curr, args) {
			var i, copy, rest = args.slice(1), last = !rest.length, result = [];
			for (i = 0; i < args[0].length; i++) {
				copy = curr.slice();
				copy.push(args[0][i]);
				if (last) {
					result.push(copy);
				} else {
					result = result.concat(addTo(copy, rest));
				}
			}
			return result;
		}
		return addTo([], paramArray);
	}
	//
	function getSpecCartData(){
		// [{"specId":s1,"itemId":i1-1},{"specId":s1,"itemId":i1-2}...],[{"specId":s2,"itemId":i2-1},{"specId":s2,"itemId":i2-2}...]...
		var dataArr = [];
		$id("specs_tab").find("ul[data-role='specKey']").each(function() {
			var specItemMaps = [];
			var specId = $(this).attr("spec-id");
			$(this).find("li>input[type='checkbox']:checked").each(function() {
				var specItemMap = {};
				specItemMap["specId"] = specId;
				specItemMap["specVal"] = $(this).attr("spec-val");
				specItemMap["colorFlag"] = $(this).attr("color-flag");
				specItemMap["refCode"] = $(this).attr("ref-code");
				specItemMap["specRef"] = {name: $(this).attr("spec-name")};
				specItemMap["specItemId"] = $(this).attr("item-id");
				specItemMap["goodsCatSpecItem"] = {value2: $(this).attr("item-val2")};
				specItemMaps.add(specItemMap);
			});
			dataArr.push(specItemMaps);
		});
		dataArr = cartProduct(dataArr);
		return dataArr;
	}
	//
	function goAddFreeCombProducts(){
		var result = validateFreeCombSpecs();
		$(".specCheckBox").click(function() {
			validateFreeCombSpecs();
		});
		if(result){
			freeCombDialog.dialog("close");
			var specCartList = getSpecCartData();
			addFreeCombProducts(specCartList);
		}
	}
	// 验证自由组合中每一个规格都有可选值
	function validateFreeCombSpecs() {
		var result = true;
		$id("specs_tab").find("ul[data-role='specKey']").each(function() {
			var specName = $(this).attr("data-specName");
			var isChecked = false;
			$(this).find("li>input[type='checkbox']").each(function() {
				if (typeof ($(this).attr("checked")) != "undefined") {
					isChecked = true;
					return false;
				}
			});
			if (!isChecked) {
				var tipObj = $(this).find("li:last").find("label:last");
				tipObj.html(specName + "未选择!");
				tipObj.css("display", "inline");
				result = false;
			} else {
				var tipObj = $(this).find("li:last").find("label:last");
				tipObj.css("display", "none");
			}
	
		});
		return result;
	}
	//
	function initFreeCombSpecData(specData) {
		var data = specData["data"];
		if (data.length == 0) {
			$id("specs_tab").html("<p>暂无规格数据</p>");
			return;
		}
		var specsHtmlStr = laytpl($id("freeCombSpecsTpl").html()).render(specData);
		$id("specs_tab").html(specsHtmlStr);
	}
	//
	function initGoodsSpecs(categId) {
		var data = {};
		var ajax = Ajax.post("/goods/specs/by/categId/get");
		ajax.data({categId : ParseInt(categId)});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				specsMap["data"] = result.data;
				initFreeCombSpecData(specsMap);
				freeCombDialog.dialog("open");
			} else {
				Layer.msgWarning(result.message);
			}

		});
		//
		ajax.go();
	}
	//
	function goFreeCombSpecs(catId){
		initGoodsSpecs(catId);
	}
	//
	function goAddProduct(){
		createAndRenderProduct(false, null, true);
	}
	//
	function bindAddProductEvent(hasSpec, catId){
		$id("addProductBtn").unbind("click");
		if(!hasSpec){
			$id("addProductBtn").click(goAddProduct);
		}else{
			$id("addProductBtn").click(function(){
				goFreeCombSpecs(catId);
			});
		}
	}
	// 列表辅助类对象
	var productAlbumfluidListHelper = FluidListHelper.newOne();
	// 处理选中的图片列表
	function echoSelected(vid) {
		var hrListSelector = $id("products").find("div[data-role='product_item_" + vid + "']:first").find("ul.fluid.list.hr.sortable");
		var selectedItems = productAlbumfluidListHelper.getSelected(hrListSelector) || null;
		if (isArray(selectedItems)) {
			// 多选结果
			for (var i = 0; i < selectedItems.length; i++) {
				var imageItem = selectedItems[i];
				console.log(imageItem);
			}
		} else {
			// 单选结果
			var imageItem = selectedItems;
			console.log(imageItem);
		}
	}
	//
	function deleteProductImage(id) {
		var ajax = Ajax.post("/goods/product/albumImg/delete/by/id/do");
		ajax.data({"id" : id});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	//
	function goDeleteProductImage(uuid, pid) {
		deleteRepoFile(uuid);
		if (!isNoB(pid)) deleteProductImage(pid);
	}
	//
	function initProductHrList(selector, productImgs) {
		//
		productAlbumfluidListHelper.asSortable(selector, 'x', echoIndexChanges);
		productAlbumfluidListHelper.asSelectable(selector);
		// 绑定移位按钮事件
		$(selector).next("div").find("button[data-role]").click(function() {
			var where = $(this).attr("data-role");
			productAlbumfluidListHelper.moveSelected(selector, where, echoIndexChanges);
		});
		// 绑定删除图片事件
		$(selector).next("div").find("button[data-action='remove']").click(function() {
			var selectedItems = productAlbumfluidListHelper.getSelected(selector) || null;
			if (selectedItems.length == 0) {
				Layer.msgWarning("未选中图片!");
				return;
			}
			var theLayer = Layer.confirm('确定要删除该图片吗？', function() {
				theLayer.hide();
				//
				if (!isNoB(selectedItems)) {
					for (var i = 0; i < selectedItems.length; i++) {
						var imageItem = selectedItems[i];
						var productId = imageItem.productId;
						if (!isNoB(productId)) {
							goDeleteProductImage(imageItem.imageUuid, imageItem.id);
						}
					}
				}
				//
				productAlbumfluidListHelper.removeSelected(selector);
			});

		});
		//
		if (!isNoB(productImgs)) {
			productAlbumfluidListHelper.addItems(selector, productImgs, noActionImageItemRenderer);
		}
		// 绑定插入图片事件
		var tableObj = $(selector).parent().parent().parent();
		$(tableObj).find("button[data-action='openImgDlg']").click(function() {
			var dlgConfig = albumImageDlgConfig;
			//
			dlgConfig.okClickHandler = function(imageDlg) {
				//选择商品相册图片
				var imageItems = imageDlg.getSelected() || [];
				//货品相册中的图片
				var allImgs = productAlbumfluidListHelper.getAll(selector);
				//
				for (var i = imageItems.length-1; i >= 0; i--) {
					var item = imageItems[i];
					var imgId = item.id;
					for (var j = allImgs.length-1; j >= 0; j--) {
						var productId = allImgs[j].productId;
						var _imgId = null;
						// 判断img是否存在数据库中
						if (!isNoB(productId)) {
							_imgId = allImgs[j].imageId;
						} else {
							_imgId = allImgs[j].id;
						}
						//
						if (_imgId == imgId) {
							imageItems.removeAt(i);
							allImgs.removeAt(j);
							break;
						}

					}
					console.log("imageItems=" + JSON.encode(imageItems));
				}
				productAlbumfluidListHelper.addItems(selector, imageItems, noActionImageItemRenderer);
			};
			//
			dlgConfig.fetchParams.goodsId = dlgConfig.fetchParams.goodsId || currentGoodsId;
			dlgConfig.uploadParams.goodsId = dlgConfig.uploadParams.goodsId || currentGoodsId;
			albumRepoImageDlg.show(dlgConfig);
		});

	}
	//hasSpec:是否启用规格，product:渲染货品, onlyCreated:页面新加货品
	function createAndRenderProduct(hasSpec, product, isNewProduct) {
		var vid = genUniqueStr();
		var productHtmlStr = laytpl($id("productTpl").html()).render({"vid" : vid,"product" : product});
		if(isNewProduct){
			var firstProduct = $id("products").find("div[data-role^='product_item']:first");
			if(firstProduct && firstProduct.length > 0){
				$(firstProduct).before(productHtmlStr);
			}else{
				$id("products").append(productHtmlStr);
			}
			//渲染规格
			if (hasSpec) {
				var specList = product.specVals;
				specList["vid"] = vid;
				var specsHtmlStr = laytpl($id("specTpl").html()).render(specList);
				$id("products").find("div[data-role='spec_list']:first").html(specsHtmlStr);
			}
		}else{
			$id("products").append(productHtmlStr);
			//渲染规格
			if (hasSpec) {
				var specList = product.specVals;
				specList["vid"] = vid;
				var specsHtmlStr = laytpl($id("specTpl").html()).render(specList);
				$id("products").find("div[data-role='spec_list']:last").html(specsHtmlStr);
			}
		}
		//
		var productObj = $id("products").find("div[data-role='product_item_" + vid + "']:first");
		// 渲染货品
		if(!isNoB(product)){
			$(productObj).find("div[data-role='detail_info']:first").find("[data-role='productKey']").each(function() {
				if ($(this).attr("id") == "salePrice_" + vid) {
					$(this).val(product.salePrice);
				} else if ($(this).attr("id") == "marketPrice_" + vid) {
					$(this).val(product.marketPrice);
				} else if ($(this).attr("id") == "origPrice_" + vid) {
					$(this).val(product.origPrice);
				} else if ($(this).attr("id") == "purchPrice_" + vid) {
					$(this).val(product.purchPrice);
				} else if ($(this).attr("id") == "wholePrice_" + vid) {
					$(this).val(product.wholePrice);
				} else if ($(this).attr("id") == "quantity_" + vid) {
					$(this).val(product.quantity);
				} else if ($(this).attr("id") == "weight_" + vid) {
					$(this).val(product.weight);
				}else if ($(this).attr("name") == "giftFlag_" + vid) {
					var giftFlag = product.giftFlag || 0;
					radioSet("giftFlag_" + vid, giftFlag);
				}else if ($(this).attr("id") == "packList_" + vid) {
					$(this).val(product.packList);
				}/*  else if ($(this).attr("id") == "shelfStatus_" + vid) {
					$(this).val(product.shelfStatus);
				} */
			});

			//
			var hrListSelector = $(productObj).find("ul.fluid.list.hr.sortable");

			initProductHrList(hrListSelector, product.productAlbumImgs);
		}
		//$id("products").find("div[data-role='product_item']:last").css("display", "block");
		// 创建formProxy
		var productFormProxy_vid = FormProxy.newOne();
		// 注册表单数据
		addProducntField(productFormProxy_vid, vid);
		//
		productFormProxyMap[vid] = productFormProxy_vid;
	}
	//
	function refreshProducts(products, hasSpec) {
		// 置空货品表单
		for ( var vid in productFormProxyMap) {
			var productFormProxy = productFormProxyMap[vid];
			if (!isNoB(productFormProxy)) {
				productFormProxy = null;
			}
		}
		$id("products").html("");
		//
		$(products).each(function() {
			createAndRenderProduct(hasSpec, this);
		});
		/*
		 * if(!isNoB(pId)){ var href = "product-"+pId; var anchor = $("a[name='"+href+"']"); var pos = $(anchor).offset().top; var tab_pos = $id("tab_products").offset().top; var scrollTop = pos -
		 * tab_pos; $id("tab_products").scrollTop(scrollTop); }
		 */
	}
	//
	function initProductList(goodsId, hasSpec) {
		var ajax = Ajax.post("/goods/products/shelve/by/goodsId/get");
		ajax.data({goodsId : parseInt(goodsId)});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var products = result.data;
				refreshProducts(products, hasSpec);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	//
	function initGoodsBasisInfo(goodsId) {
		if (isNoB(goodsId)) {
			Toast.show("亲，未选中修改的商品哦！", 2000, "warning");
			return;
		}
		//
		var hintBox = Layer.progress("正在加载数据...");
		var ajax = Ajax.post("/goods/basisInfo/by/id/get");
		ajax.data({"goodsId" : goodsId});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var goods = result.data;
				if (!isNoB(goods)) {
					currentCatId = goods.catId;
					currentGoodsName = goods.name;
					$id("goodsName").val(goods.name);
					$id("goodsCat").attr("data-id", goods.catId);
					$id("goodsTitle").val(goods.title == null ? "" : goods.title);
					$id("packList").val(goods.packList == null ? "" : goods.packList);
					var hasSpec = goods.hasSpec;
					initProductList(goodsId, hasSpec);
					//
					refreshCategAttr(currentCatId, goods.attrVals);
					//
					var vendorId = goods.vendorId;
					var vendor = vendorGridHelper.getRowData(vendorId);
					currentVendorId = vendorId;
					if(vendor){
						$id("vendorId").val(vendor.name);
						$id("vendorId").attr("data-id", vendorId);
					}
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
	//标识数据是否被改变，需要重新获取
	var colorSpecDirty = true;
	//商品相册颜色分组定义
	var retItems = [];
	function menuItemDataListProvider(targetInfo) {
		var tmpItems = [];
		//
		var item = targetInfo.item;
		var jqItem = $(item);
		var jqColorMarker = jqItem.find(">.default.image.marker");
		var lastItem = retItems[retItems.length-1];
		if(jqColorMarker.length >0){
			if(!retItems.length || lastItem && lastItem.value != 'cancelColorDefault'){
				tmpItems.add({
					text : "<span style='display:inline-block;width:100%;height:100%;line-height:98%;font-style:italic;color:#666;'>取消设置的颜色</span>",
					value : "cancelColorDefault"
				});
			}
		}else{
			if(lastItem && lastItem.value == 'cancelColorDefault'){
				retItems.removeAt(retItems.length-1);
			}
		}
		//
		if(!colorSpecDirty){
			 return $.merge(retItems, tmpItems);
		}
		var data = targetInfo.data;
		//获取货品所有颜色规格列表
		var ajax = Ajax.post("/goods/product/color/specVals/get");
		ajax.data({goodsId:data.goodsId || currentGoodsId});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				colorSpecDirty = false;
				var specItems = result.data;
				if(specItems && specItems.length > 0){
					for(var i=0; i<specItems.length; i++){
						var reItem = {};
						var specItem = specItems[i];
						reItem.specItemId = specItem.id;
						reItem.specId = specItem.specId;
						reItem.text = specItem.value+"默认图片";
						reItem.value = specItem.value2;
						reItem.specVal = specItem.value;
						retItems.add(reItem);
					}
				}
				
			} else {
				Layer.msgWarning(result.message);
			}
	
		});
		ajax.go();
		//
		return $.merge(retItems, tmpItems);
	}
	// 菜单项html生成（回调）函数
	function menuItemHtmlRenderer(targetInfo, menuItemData) {
		var itemTpl = laytpl($id('contextMenuItemTpl').html());
		return itemTpl.render(menuItemData);
	}
	//商品颜色图片集的全局变量
	var goodsColorImgs = {};
	//设置图片颜色分组回调函数
	function menuItemClickHandler(targetInfo, menuItemData) {
		// html element
		var item = targetInfo.item;
		// bound element data
		var data = targetInfo.data;
		//
		var color = menuItemData.value;
		if (isHexColor(color)) {
			var jqItem = $(item);
			jqItem.siblings().find(">.default.image.marker[for-color='" + color + "']").remove();
			var jqColorMarker = jqItem.find(">.default.image.marker");
			jqColorMarker.remove();
			jqColorMarker = $('<div class="default image marker"></div>').appendTo(jqItem);
			jqColorMarker.attr("for-color", color);
			jqColorMarker.css("background-color", color);
			//
			var specItemId = menuItemData.specItemId;
			var goodsAlbumImg = targetInfo.data;
			var goodsColorImg = {};
			merge(goodsColorImg, menuItemData);
			goodsColorImg.goodsId = goodsAlbumImg.goodsId;
			var currentImgId = goodsAlbumImg.id
			goodsColorImg.imageId = currentImgId;
			//删除currentImgId相关的goodsColorImgs中的数据
			for(var key_specItemId in goodsColorImgs){
				var img = goodsColorImgs[key_specItemId];
				var imgId = img.imageId;
				if(imgId == currentImgId){
					delete goodsColorImgs[key_specItemId];
					break;
				}
			}
			//缓存当前颜色分组图片
			var colorImg = goodsColorImgs[specItemId];
			if(colorImg){
				goodsColorImg.id = colorImg.id || "";
			}
			goodsColorImgs[specItemId] = goodsColorImg;
			//
		}else if(value = "cancelColorDefault"){
			var jqItem = $(item);
			var jqColorMarker = jqItem.find(">.default.image.marker");
			jqColorMarker.remove();
			//删除缓存数据
			var _imgId = data.id;
			for(var key_specItemId in goodsColorImgs){
				var img = goodsColorImgs[key_specItemId];
				var imgId = img.imageId;
				if(imgId == _imgId){
					delete goodsColorImgs[key_specItemId];
					break;
				}
			}
			//
		}
	}
	//
	var xyListSelector = "ul.fluid.list.xy.sortable";
	// 列表辅助类对象
	var goodsAlbumfluidListHelper = FluidListHelper.newOne();
	//
	function goInitGoodsAlbumImgs(goodsId){
		//
		var xyContextMenuInfo = {
			title : "把此图片设置为",
			width : 200,
			itemDataList : menuItemDataListProvider,
			itemHtmlRenderer : menuItemHtmlRenderer,
			itemClickHandler : menuItemClickHandler
		};
		goodsAlbumfluidListHelper.setContextMenuInfo(xyListSelector, xyContextMenuInfo);
		//
		initGoodsAlbumImgs(goodsId);
		
	}
	//
	function initGoodsAlbumImgs(goodsId) {
		var ajax = Ajax.post("/goods/albumImg/list/get");
		ajax.data({goodsId : goodsId});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var albumImgList = result.data;
				goodsAlbumfluidListHelper.asSortable(xyListSelector, 'xy', echoIndexChanges);
				goodsAlbumfluidListHelper.setItems(xyListSelector, albumImgList, imageItemRenderer);
				goodsAlbumfluidListHelper.setItemBtnClickHanlder(xyListSelector, function(itemInfo, btnName) {
					//
					var itemData = itemInfo.data;
					var itemDom = itemInfo.item;
					//
					if (btnName == "delete") {
						var uuid = itemInfo.data.fileUuid;
						goCheckIsExsitImage(itemDom, uuid, null);
						//
					} else if (btnName == "viewOriginal") {
						showImageViewBox(itemData["fileBrowseUrl"]);
					}
				});
				//获取商品颜色图片，渲染
				goRenderGoodsColorImgs(goodsId);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	//
	function goRenderGoodsColorImgs(goodsId){
		var ajax = Ajax.post("/goods/color/imgs/by/goodsId/get");
		ajax.data({goodsId : goodsId});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var colorImgs = result.data;
				//渲染商品颜色图片
				renderGoodsColorImgs(colorImgs);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	//渲染商品颜色图片
	function renderGoodsColorImgs(renderDataList){
		var imgList = $id("goods_album_list").find("li");
		//遍历商品相册图片
		$(imgList).each(function(){
			var imageId = $(this).attr("data-id");
			for(var i=0; i<renderDataList.length; i++){
				var renderData = renderDataList[i];
				var renderImgId = renderData.imageId;
				if(renderImgId == imageId){
					var value2 = renderData.specItem.value2;
					//追加imageMaker
					var imageMaker = "<div class='default image marker' for-color='"+value2+"' style='background-color: "+value2+";'></div>";
					$(imageMaker).appendTo($(this));
					//缓存数据
					var colorImgMap = {};
					colorImgMap.id = renderData.id;
					var itemId = renderData.specItemId;
					colorImgMap.specItemId = itemId;
					colorImgMap.specId = renderData.specId;
					colorImgMap.text = renderData.specItem.value + "默认图片";
					colorImgMap.value = renderData.specItem.value2;
					colorImgMap.specVal = renderData.specItem.value;
					colorImgMap.goodsId = renderData.goodsId;
					colorImgMap.imageId = renderImgId;
					goodsColorImgs[itemId] = colorImgMap;
				}
			}
		});
		//console.log(JSON.encode(goodsColorImgs));
	}
	//
	function initGoodsIntro(goodsId) {
		var ajax = Ajax.post("/goods/intro/by/goodsId/get");
		ajax.data({goodsId : goodsId});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var introInfo = result.data;
				if (!isNoB(introInfo))
					introId = introInfo.id;
				if (!isNoB(introInfo)) {
					CKEDITOR.instances["goods_intro_editor"].setData(introInfo.content);
				}
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	//
	function initGoodsData(goodsId) {
		//
		initGoodsBasisInfo(goodsId);
		//
		goInitGoodsAlbumImgs(goodsId);
		//
		initGoodsIntro(goodsId);
	}
	//
	function queryVendorByName() {
		var filter = {};
		var name = textGet("queryVendorName");
		if (name) {
			filter.name = name;
		}
		vendorGridCtrl.jqGrid("setGridParam", {postData : {filterStr : JSON.encode(filter, true)},page : 1}).trigger("reloadGrid");
		getCallbackAfterGridLoaded = function(){};
	}
	//
	function goSelectVendor() {
		queryVendorByName();
		selectVendorDialog.dialog("open");
		//
		basisInfoFormProxy.hideMessages();
	}
	//
	function deleteGoodsIntroImage(uuid, imageDlg) {
		var ajax = Ajax.post("/goods/intro/img/delete/do");
		ajax.sync();
		ajax.data({"fileUuid" : uuid});
		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				imageDlg.refresh();
			} else {
				Layer.msgWarning(result.message);
			}
	
		});
		ajax.go();
	}
	//
	function deleteRepoFile(uuid) {
		var ajax = Ajax.get('/file/delete?uuid=' + uuid);
		ajax.sync();
		ajax.done(function(result) {
			if (result.type == "info") {
	
			}
		});
		ajax.go();
	}
	//
	function saveGoodsIntroImgs(imageDlg, imgs) {
		var hintBox = Layer.progress("正在保存数据...");
	
		var ajax = Ajax.post("/goods/intro/imgs/save/do");
		ajax.data(imgs);
	
		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				if (!isNoB(imageDlg)) imageDlg.refresh();
			} else {
				Layer.msgWarning(result.message);
			}
	
		});
		ajax.always(function() {
			hintBox.hide();
		});
	
		ajax.go();
	}
	//categId:选中的商品分类，attrVals:选中的属性值集合
	function showCatAttrs(categId, attrVals) {
		var ajax = Ajax.post("/goods/categ/attrs/by/categId/get");
		ajax.data({
			categId : parseInt(categId)
		});
		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				var data = result.data;
				var keyAttr = [];
				var noKeyAttr = [];
				var attrsHtmlStr = "<tr><td><label class='field label'>暂无数据</label></td></tr>";
				$id("atrrs").css("display", "none");
				//
				$id("keyAttr_tab").html(attrsHtmlStr);
				$id("noKeyAttr_tab").html(attrsHtmlStr);
				//
				$id("atrrs").css({}).slideDown("fast");

				if (data != null && data.length > 0) {
					$.each(data, function() {
						if ($(this)[0].keyFlag)
							keyAttr.add($(this)[0]);
						else
							noKeyAttr.add($(this)[0]);
					});
					//
					attrsHtmlStr = laytpl($id("attrTabTpl").html()).render(keyAttr);

					$id("keyAttr_tab").html(attrsHtmlStr);
					//
					attrsHtmlStr = laytpl($id("attrTabTpl").html()).render(noKeyAttr);

					$id("noKeyAttr_tab").html(attrsHtmlStr);
				}
				// 渲染属性值
				$("[data-role='goodsAttr']").each(function() {
					var refCode = $(this).attr("ref-code");
					var multiFlag = $(this).attr("multi-flag");
					var attrVal = null;
					var items = $(this).find("input[type='checkbox']");
					var attrValId = null;
					var attrId = null;
					$(attrVals).each(function() {
						attrValId = this.id;
						attrId = this.attrId;
						if (this.refCode == refCode) {
							if (!multiFlag) {
								attrVal = this.attrVal;
								return false;
							} else {
								var attrItemId = this.attrItemId;
								$(items).each(function() {
									if ($(this).attr("item-id") == attrItemId) {
										$(this).attr("checked", "checked");
										$(this).attr("attrVal-id", attrValId);
									}
								});
							}
						}
					});
					if (attrId == $(this).attr("attr-id")) {
						$(this).attr("attrVal-id", attrValId);
					}
					if (!multiFlag) {
						$(this).val(attrVal);
					}
				});

			} else {
				Layer.msgWarning(result.message);
			}

		});
		ajax.go();
	}
	
	//确定商品分类 catId:选中的商品分类，attrVals:选中的属性值集合
	function refreshCategAttr(catId, attrVals) {
		if(isNoB(catId)){
			Toast.show("亲，您还未选择商品分类哦！", 3000, "warning");
			currentCategHasSpec = null;
			return;
		}
		//
		var zTree = $.fn.zTree.getZTreeObj("catTree");
		var node = zTree.getNodeByParam("id", catId);
		//
		currentCategHasSpec = node.hasSpec;
		bindAddProductEvent(currentCategHasSpec, catId);
		//
		$id("categ_unit").val(node.unit);
		$id("categ_unit").trigger("change");
		weight_unit = node.weightUnit;
		//初始化选中商品分类节点
		var zTree = $.fn.zTree.getZTreeObj("catTree");
		var node = zTree.getNodeByParam("id",catId);
		zTree.selectNode(node);
		$id("goodsCat").val(node.name)
		//$id("goodsCat").trigger("change");
		showCatAttrs(catId, attrVals);
		categTreeDialog.dialog("close");
	}
	
	//------------------------------------------------------初始化----------------------------------------------------------------
	function removeImage(uuid) {
		var ajax = Ajax.get('/file/delete?uuid=' + uuid);
		ajax.sync();
		ajax.done(function(result) {
			if (result.type == "info") {
				
			}
		});
		ajax.go();
	}
	//
	function deleteGoodsAlbumImg(uuid, imgDialg) {
		var ajax = Ajax.post("/goods/albumImg/delete/by/uuid/do");
		ajax.data({"uuid" : uuid});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				removeImage(uuid);
				if (!isNoB(imgDialg)) imgDialg.refresh();
				initGoodsAlbumImgs(currentGoodsId);
				return true;
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	//
	function deleteProductImages(productAlbumImgIds) {
		var ajax = Ajax.post("/goods/product/albumImgs/delete/do");
		var postData = [];
		for (var i = 0; i < productAlbumImgIds.length; i++) {
			postData.add({"id" : productAlbumImgIds[i]});
		}
		ajax.data(postData);
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				initProductList(currentGoodsId, currentCategHasSpec);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	//
	function goDeleteGoodsAlbumImg(uuid, productAlbumImgIds, liObj, imgDialg) {
		var msg = '确定要删除该图片吗？';
		if (!isNoB(productAlbumImgIds))
			msg = '该图片被具体商品引用，确定要删除该图片吗？';
		var theLayer = Layer.confirm(msg, function() {
			theLayer.hide();
			//
			var jqColorMarker = $(liObj).find(">.default.image.marker");
			if(jqColorMarker){
				var color = $(jqColorMarker).attr("for-color");
				//删除currentImgId相关的goodsColorImgs中的数据
				for(var key_specItemId in goodsColorImgs){
					var img = goodsColorImgs[key_specItemId];
					var _color = img.value;
					if(_color == color){
						delete goodsColorImgs[key_specItemId];
						break;
					}
					//
				}
			}
			//
			if (!isNoB(productAlbumImgIds)) deleteProductImages(productAlbumImgIds);
			deleteGoodsAlbumImg(uuid, imgDialg);
		});
	}
	//
	function goCheckIsExsitImage(THIS, uuid, imgDlag) {
		$id("tab_products").find("ul.fluid.list.middle.hr.sortable").each(function() {
			var resultItem = goodsAlbumfluidListHelper.findItemByData(this, function(itemData) {
				return itemData.imageUuid == uuid;
			});
			if (!isNoB(resultItem))
				$(resultItem).remove();
		});
		goCheckIsExsitImageFromDB(THIS, uuid, imgDlag);
	}
	//
	function goCheckIsExsitImageFromDB(THIS, uuid, imgDlag) {
		var productAlbumImgIds = null;
		var ajax = Ajax.post("/goods/product/albumImgIds/by/uuid/do");
		ajax.data({"uuid" : uuid});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				productAlbumImgIds = result.data;
				goDeleteGoodsAlbumImg(uuid, productAlbumImgIds, THIS, imgDlag);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	//
	function toDeleteGoodsAlbumImage(imageDlg, imageItem) {
		goCheckIsExsitImage(null, imageItem.fileUuid, imageDlg);
	}
	//
	function getGoodsAlbumImgs(goodsId) {
		var ajax = Ajax.post("/goods/albumImg/list/get");
		ajax.data({"goodsId" : goodsId});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var albumImgList = result.data;
				if (albumImgList.length > 0) {
					goodsAlbumfluidListHelper.asSortable(xyListSelector, 'xy', echoIndexChanges);
					goodsAlbumfluidListHelper.setItems(xyListSelector, albumImgList, imageItemRenderer);
				}
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	//
	function saveGoodsAlbumImgs(imageDlg, imgs) {
		var ajax = Ajax.post("/goods/albumImgs/save/do");
		ajax.data(imgs);
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				if (!isNoB(imageDlg)) imageDlg.refresh();
				getGoodsAlbumImgs(currentGoodsId);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	//
	function handleUploadedAlbumFiles(imageDlg, fileInfoList) {
		var imgsData = [];
		$(fileInfoList).each(function() {
			if (this.type == "info") {
				var imgMap = {};
				imgMap["shopId"] = 1;
				imgMap["goodsId"] = currentGoodsId;
				imgMap["imageUuid"] = this.fileUuid;
				imgMap["imageUsage"] = this.fileUsage;
				imgMap["imagePath"] = this.fileRelPath;
				imgsData.add(imgMap);
			}
		});
		saveGoodsAlbumImgs(imageDlg, imgsData)
		//console.log("handleUploadedAlbumFiles:" + JSON.encode(fileInfoList));
	}
	// 商品相册图片选择对话框
	var albumRepoImageDlg = RepoImageDlg.newOne();
	var albumImageDlgConfig = {
		title : "选择商品相册图片",
		fetchUrl : "/goods/albumImg/list/get",
		fetchParams : {
			goodsId : currentGoodsId
		},
		// 图片上传
		uploadUrl : "/file/upload",
		uploadParams : {
			usage : "image.goods",
			subUsage : "album",
			goodsId : currentGoodsId
		},
		uploadCallback : handleUploadedAlbumFiles,
		deleteHanlder : toDeleteGoodsAlbumImage
	};
	// 展示收缩产品div
	function expandDetail(handler) {
		var info_obj = $(handler).parent().parent().find("div[data-role='detail_info']");
		if (!isNoB(info_obj)) {
			$(info_obj).slideToggle("slow");
			$(info_obj).toggleClass("active");
			if ($(info_obj).hasClass("active")) {
				$(handler).html("隐藏");
			} else {
				$(handler).html("展开");
			}
		}
	}	
	//
	function initFreeCombDialog() {
		freeCombDialog = $("#freeCombDialog").dialog({
			autoOpen : false,
			height : Math.min(350, $window.height()),
			width : Math.min(550, $window.width()),
			modal : true,
			title : '选择规格',
			buttons : {
				"确定" : function() {
					goAddFreeCombProducts();
				},
				"取消" : function() {
					freeCombDialog.dialog("close");
				}
			},
			close : function() {
				freeCombDialog.dialog("close");
			}
	
		});
	}
	//
	function addProducntField(productFormProxy, vid) {
		productFormProxy.addField({
			id : "purchPrice_" + vid,
			type : "float",
			rules : [ "isNum" ],
			messageTargetId : "purchPrice_" + vid
		});
		productFormProxy.addField({
			id : "wholePrice_" + vid,
			type : "float",
			rules : [ "isNum" ],
			messageTargetId : "wholePrice_" + vid
		});
		productFormProxy.addField({
			id : "origPrice_" + vid,
			required : true,
			type : "float",
			rules : [ "isNum" ],
			messageTargetId : "origPrice_" + vid
		});
		productFormProxy.addField({
			id : "salePrice_" + vid,
			required : true,
			type : "float",
			rules : [ "isNum" ],
			messageTargetId : "salePrice_" + vid
		});
	
		productFormProxy.addField({
			id : "marketPrice_" + vid,
			type : "float",
			rules : [ "isNum" ],
			messageTargetId : "marketPrice_" + vid
		});
		productFormProxy.addField({
			id : "quantity_" + vid,
			required : true,
			type : "float",
			rules : [ "isNum" ],
			messageTargetId : "quantity_" + vid
		});
	
		productFormProxy.addField({
			id : "weight_" + vid,
			type : "float",
			rules : [ "isNum" ],
			messageTargetId : "weight_" + vid
		});
	}
	// 列表项目生成回调函数(itemData => itemHtml)
	var imageItemTpl = null;
	function imageItemRenderer(itemData) {
		if (imageItemTpl == null) {
			var tmpTplHtml = $id("imageItemTpl").html();
			imageItemTpl = laytpl(tmpTplHtml);
		}
		return imageItemTpl.render(itemData);
	}
	// 列表项目生成回调函数(itemData => itemHtml)
	var noActionImageItemTpl = null;
	function noActionImageItemRenderer(itemData) {
		if (noActionImageItemTpl == null) {
			var tmpTplHtml = $id("noActionImageItemTpl").html();
			noActionImageItemTpl = laytpl(tmpTplHtml);
		}
		return noActionImageItemTpl.render(itemData);
	}
	// 显示索引位置变化
	function echoIndexChanges(jqList, indexChanges) {
		//console.log("索引位置变化[" + jqList.selector + "]：" + JSON.encode(indexChanges));
	}
	//
	function hideFormWarnMsgs() {
		//
		basisInfoFormProxy.hideMessages();
		//
		for ( var vid in productFormProxyMap) {
			var productFormProxy = productFormProxyMap[vid];
			if (!isNoB(productFormProxy)) {
				productFormProxy.hideMessages();
			}
		}
	}
	//
	function loadVendorData() {
		var filter = {};
		var name = textGet("queryVendorName");
		if (name) {
			filter.name = name;
		}
		// 加载供应商
		vendorGridCtrl = $("#vendorGrid").jqGrid({
			url : getAppUrl("/vendor/list/get"),
			contentType : 'application/json',
			mtype : "post",
			datatype : 'json',
			postData : {
				filterStr : JSON.encode(filter, true)
			},
			height : "100%",
			width : "100%",
			colNames : [ "id", "名称", "业务范围", "联系电话", "操作" ],
			colModel : [ {
				name : "id",
				index : "id",
				width : 150,
				align : 'center',
				hidden : true
			}, {
				name : "name",
				index : "name",
				width : 230,
				align : 'left',
			}, {
				name : "bizScope",
				index : "bizScope",
				width : 350,
				align : 'left'
			}, {
				name : "phoneNo",
				index : "phoneNo",
				width : 150,
				align : 'left'
			}, {
				name : "action",
				index : "action",
				width : 100,
				align : "center"
			} ],
			loadtext : "Loading....",
			multiselect : false,// 定义是否可以多选
			multikey : 'ctrlKey',
			pager : "#vendorPager",
			loadComplete : function(gridData) {
				vendorGridHelper.cacheData(gridData);
				var callback = getCallbackAfterGridLoaded();
				if(isFunction(callback)){
					callback();
				}
			},
			gridComplete : function() {
				var ids = vendorGridCtrl.jqGrid('getDataIDs');
				for (var i = 0; i < ids.length; i++) {
					var id = ids[i];
					id = ParseInt(id);
					vendorGridCtrl.jqGrid('setRowData', id, {
						action : "<input type='radio' name='vendor' value='" + id + "'/>"
					});
				}
			}
		});
	}
	//
	function getCallbackAfterGridLoaded(){}
	//
	function showSelectVendor(id) {
		var vendor = vendorGridHelper.getRowData(id);
		$id("vendorId").val(vendor.name);
		$id("vendorId").attr("data-id", vendor.id);
	}
	//
	function getCheckedVendor() {
		var vendorId = null;
		$("input:radio[name='vendor']").each(function() {
			vendorId = $(this).val();
			var isChecked = $(this).prop("checked");
			if (isChecked) {
				return false;
			}
		});
		return vendorId;
	}
	//
	function initSelectVendorDialog() {
		selectVendorDialog = $("#selectVendorDialog").dialog({
			autoOpen : false,
			width : Math.min(900, $window.width()),
			height : Math.min(300, $window.height()),
			modal : true,
			title : '选择供应商',
			buttons : {
				"确定" : function() {
					var vendorId = getCheckedVendor();
					showSelectVendor(vendorId);
					selectVendorDialog.dialog("close");
				},
				"关闭" : function() {
					selectVendorDialog.dialog("close");
				}
			},
			close : function() {
				selectVendorDialog.dialog("close");
			}
		});
	}
	// 图片选择对话框
	var introRepoImageDlg = RepoImageDlg.newOne();
	var introImgDlgConfig = {
		title : "选择商品介绍图片",
		fetchUrl : "/goods/intro/imgs/get",
		fetchParams : {
			goodsId : currentGoodsId
		},
		// 图片上传
		uploadUrl : "/file/upload",
		uploadParams : {
			usage : "image.goods",
			subUsage : "intro",
			goodsId : currentGoodsId
		},
		uploadCallback : handleUploadedIntroImgs,
		// 删除回调
		deleteHanlder : toDeleteGoodsIntroImage,
		// 确定回调
		okClickHandler : getAndInsertGoodsIntroImages
	};
	// 显示图片选择对话框
	function openRepoImageDlg(editor) {
		var dlgConfig = introImgDlgConfig;
		//
		dlgConfig.fetchParams.goodsId = dlgConfig.fetchParams.goodsId || goodsId;
		dlgConfig.uploadParams.goodsId = dlgConfig.uploadParams.goodsId || goodsId;
		//
		introRepoImageDlg.show(dlgConfig);
	}
	// 处理上传了的文件
	function handleUploadedIntroImgs(imageDlg, fileInfoList) {
		//console.log("handleUploadedIntroImgs=" + JSON.encode(fileInfoList));
		var imgData = [];
		$(fileInfoList).each(function() {
			if (this.type == "info") {
				var imgMap = {};
				imgMap["goodsId"] = currentGoodsId;
				imgMap["imageUuid"] = this.fileUuid;
				imgMap["imageUsage"] = this.fileUsage;
				imgMap["imagePath"] = this.fileRelPath;
				imgData.add(imgMap);
			}
		});
		saveGoodsIntroImgs(imageDlg, imgData);
	}
	// 处理删除意图
	function toDeleteGoodsIntroImage(imageDlg, imageItem) {
		var theLayer = Layer.confirm('确定要删除该图片吗？', function() {
			theLayer.hide();
			//
			//console.log("您要删除的图片是：" + JSON.encode(imageItem));
			//
			deleteRepoFile(imageItem.fileUuid);
			deleteGoodsIntroImage(imageItem.fileUuid, imageDlg);
			return true;
		});
		return false;
	}
	//
	function showGoodsCatTree() {
		categTreeDialog.dialog("open");
		//
		basisInfoFormProxy.hideMessages();
		//
		var cityObj = $("#goodsCat");
		var cityOffset = $("#goodsCat").offset();
		$("#menuContent").css({}).slideDown("fast");
	}
	//初始化商品分类
	function initGoodsCat() {
		var ajax = Ajax.post("/goods/categ/list/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var cats = result.data;
				createTree(cats);
				//
				$id("goodsCat").click(function() {
					showGoodsCatTree();
				});
			} else {
				Layer.msgWarning(result.message);
			}
	
		});
		ajax.go();
	}
	// 把选择的商品介绍图片插入到editor中
	function getAndInsertGoodsIntroImages(imageDlg) {
		var imageItems = imageDlg.getSelected() || [];
		if (isArray(imageItems)) {
			// 多选结果
			for (var i = 0; i < imageItems.length; i++) {
				var imageItem = imageItems[i];
				//console.log(imageItem);
				var imageHtmlTpl = '<img src="{0}" /><br />';
				theEditor.insertHtml(imageHtmlTpl.format(imageItem["fileBrowseUrl"]));
			}
		} else {
			// 单选结果
			var imageItem = imageItems;
			//console.log(imageItem);
			var imageHtmlTpl = '<img src="{0}" /><br />';
			theEditor.insertHtml(imageHtmlTpl.format(imageItem["fileBrowseUrl"]));
		}
	}
	//
	function createTree(data) {
		var catNodes = [];
		$.each(data, function() {
			var nodeMap = {};
			// {id:4, pId:0, name:"XXX", open:true, nocheck:true},
			var data = $(this)[0];
			nodeMap["id"] = data.id;
			nodeMap["pId"] = data.parentId;
			nodeMap["name"] = data.name;
			nodeMap["idPath"] = data.idPath;
			nodeMap["hasSpec"] = data.hasSpec;
			nodeMap["unit"] = data.unit;
			nodeMap["weightUnit"] = data.weightUnit;
			if (data.level < 3) {
				nodeMap["open"] = true;
			}
			catNodes.add(nodeMap);
		});
	
		$.fn.zTree.init($("#catTree"), setting, catNodes);
	}
	//
	function isSelected(treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj(treeId);
		var nodes = zTree.getSelectedNodes();
		for (var i = 0; i < nodes.length; i++) {
			var node = nodes[i];
			if (node.id == treeNode.id) {
				return true;
			}
		}
		return false;
	}
	//
	function clickNode(e, treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj(treeId);
		var selected = isSelected(treeId, treeNode);
		//
		if (selected) {
			currentCatId = treeNode.id;
			$id("goodsCat").val(treeNode.name);
		} else {
			currentCatId = null;
			$id("goodsCat").val("");
		}
	}
	//
	var setting = {
		view : {
			dblClickExpand : true,
			showLine : false,
			showIcon : false
		},
		data : {
			simpleData : {
				enable : true
			}
		},
		callback : {
			beforeClick : function(treeId, treeNode) {
				var zTree = $.fn.zTree.getZTreeObj("catTree");
				if (treeNode.isParent) {
					zTree.expandNode(treeNode);
					return false;
				} else {
					return true;
				}
			},
			onClick : clickNode
		}
	};
	//
	function initCategTreeDialog() {
		categTreeDialog = $("#categTreeDialog").dialog({
			autoOpen : false,
			height : Math.min(350, $window.height()),
			width : Math.min(550, $window.width()),
			modal : true,
			title : '商品分类选择',
			buttons : {
				"确定" : function() {
					refreshCategAttr(currentCatId, null);
				},
				"取消" : function() {
					categTreeDialog.dialog("close");
				}
			},
			close : function() {
				categTreeDialog.dialog("close");
			}
	
		});
	}
	//
	function fileToUploadImage() {
		if (isNoB($id("fileToUploadImage").val())) {
			Layer.msgWarning("没有上传文件!");
			return;
		}
		var hintBox = Layer.progress("正在上传...");
		sendFileUpload("fileToUploadImage", {
			url : getAppUrl("/file/upload"),
			dataType : "json",
			// 自定义数据
			formData : {
				usage : "image.goods",
				subUsage : "album",
				reserveFileName : false,
			},
			done : function(e, result) {
				hintBox.hide();
				var resultInfo = result.result;

				// 单独提取文件信息列表
				var fileInfoList = getFileInfoList(resultInfo);
				// 后端返回的信息
				//console && console.log("fileToUploadImage:" + JSON.encode(fileInfoList));
				//
				goodsAlbumfluidListHelper.addItems(xyListSelector, fileInfoList, imageItemRenderer);

				var addAlbumImgs = [];
				$(fileInfoList).each(function() {
					if (this.type == "info") {
						var imgMap = {};
						imgMap["shopId"] = 1;
						imgMap["goodsId"] = currentGoodsId;
						imgMap["imageUuid"] = this.fileUuid;
						imgMap["imageUsage"] = this.fileUsage;
						imgMap["imagePath"] = this.fileRelPath;
						addAlbumImgs.add(imgMap);
					}
				});
				//
				if (addAlbumImgs.length > 0) {
					saveGoodsAlbumImgs(null, addAlbumImgs);
				}

			},
			fail : function(e, data) {
				hintBox.hide();
				console.log(data);
			}
		});
	}
	
	//------------------------------------------------------调整控件大小-----------------------------------------------------------	
	function adjustCtrlsSize() {
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		console.log("mainWidth:" + mainWidth + ", " + "mainHeight:"+ mainHeight);
		var tabsCtrlWidth = mainWidth - 4;
		var tabsCtrlHeight = mainHeight - 8;
		jqTabsCtrl.width(tabsCtrlWidth);
		var tabsHeaderHeight = $(">[role=tablist]", jqTabsCtrl).height();
		var tabsPanels = $(">[role=tabpanel]", jqTabsCtrl);
		var panelWidth = ParseInt(tabsCtrlWidth - 4 * 2 );
		var panelHeight = ParseInt(tabsCtrlHeight - tabsHeaderHeight - 30 );
		tabsPanels.width(panelWidth);
		tabsPanels.height(panelHeight);
		//
		theEditor.resize(panelWidth, panelHeight);
	}
	</script>
</body>
<!------------------------------------------------------商品信息模板------------------------------------------------------------->
<script id="productTpl" type="text/html" title="商品信息模板">
<div data-role='product_item_{{ d.vid }}' data-productId='{{ d.product ? d.product.id : "" }}' class='filter section' style='display:block; width:99%;'>
	<div class='form'><a name='product-{{ d.product == null ? "" : d.product.id }}'></a>
		<div data-role='spec_list' class='field row' style='border: 1px solid #AAA; background: #F1F1F1 none repeat scroll 0% 0%;line-height: auto;height: auto;vertical-align: middle;'>
		</div>
		
		<div data-role='detail_info' class='active' style='margin-top: -1px;border: 1px solid #AAA;border-top-style:hidden; background: #F9F9F9 none repeat scroll 0% 0%;border-radius: 0 0 4px 4px;'>
			<div class='field row'>
				<label class='field label one half wide'>采购价(元)</label>
				<input data-role='productKey' class='field value' id='purchPrice_{{ d.vid }}' maxlength="11"/>

				<label class='field inline label one half wide'>批发价(元)</label>
				<input data-role='productKey' class='field value' id='wholePrice_{{ d.vid }}' maxlength="11"/>
					{{# if(!isNoB(weight_unit)){ }}
						{{# if(weight_unit == 'kg') weight_unit = '千克';}}
						{{# if(weight_unit == 'g') weight_unit = '克';}}
						{{# if(weight_unit == 'mg') weight_unit = '豪克';}}
					{{# } }}
				<label class='field inline label one half wide'>商品重量({{ weight_unit == null? '':weight_unit }})</label>
				<input data-role='productKey' class='field value' id='weight_{{ d.vid }}' maxlength="11"/>
				
				<label class='field inline label one half wide'>可否为赠品</label>
				<div class='field group'>
					<input id='giftFlag-F' type='radio' checked data-role='productKey' name='giftFlag_{{ d.vid }}' value='0' />
					<label for='giftFlag-F'>否</label>
					<input id='giftFlag-T' type='radio' data-role='productKey' name='giftFlag_{{ d.vid }}' value='1' />
					<label for='giftFlag-T'>是</label>
				</div>
			</div>
				
			<div class='field row'>
				<label class='field label required one half wide'>原价(元)</label>
				<input data-role='productKey' class='field value' id='origPrice_{{ d.vid }}' maxlength="11"/>

				<label class='field inline label required one half wide'>销售价(元)</label>
				<input data-role='productKey' class='field value' id='salePrice_{{ d.vid }}' maxlength="11"/>

				<label class='field inline label one half wide'>市场价(元)</label>
				<input data-role='productKey' class='field value' id='marketPrice_{{ d.vid }}' maxlength="11"/>

				<label class='field inline label required one half wide'>库存数量</label>
				<input data-role='productKey' class='field value' id='quantity_{{ d.vid }}'  maxlength="9"/>
			</div>
			
			<div class='field row' style='height:auto;'>
				<label class='field label one half wide'>包装清单</label>
				<input data-role='productKey' class='field value' id='packList_{{ d.vid }}' style='width:368px;' maxlength="200"/>
			</div>

			<span class='normal hr divider'></span>
								
			<div class='field row' style='height: 222px;'>
				<table style='table-layout:fixed; width: 100%;'>
					<tr>
						<td width='40' class='align center'>货<br/>品<br/>相<br/>册</td>
						<td width='80%'>
							<div style='width:100%;height:216px;border:1px solid #AAA; border-radius:4px;'>
								<ul class='fluid list middle hr sortable' style='height:171px;'>
									
								</ul>

								<div style='height:40px;border-top: 1px solid #DDD;text-align: center;background-color: #EEEEEE;line-height: 38px;'>
									<button class='normal button' data-role='first'>
									&lt;&lt; 头部
									</button><span class='normal spacer'></span>
									<button class='normal button' data-role='prev'>
									&lt; 前移
									</button><span class='normal spacer'></span>
									<button class='normal button' data-role='next'>
									后移 &gt;
									</button><span class='normal spacer'></span>
									<button class='normal button' data-role='last'>
									尾部 &gt;&gt;
									</button>
									<button class='normal button one half wide' style='float:right;margin-top: 4px;' data-action='remove'>
									删除选中条目
									</button>

								</div>

							</div>
						</td>
						<td width='40' class='align center'>
							<button data-action='openImgDlg' class='normal button' style='width:28px; height: auto;'>添加图片</button>
						</td>
					</tr>
				</table>
				
			</div>
			
			<div class='field row'>
				<div class='group right align' style='height:100%; margin-right:5px;'>
					<a href='javascript:void(0);' style='float:right; padding-right: 5px;' onclick='removeProduct("{{ d.vid }}")'>移除</a>
				</div>
			</div>

		</div>
	</div>

</div>
</script>
<!------------------------------------------------------商品规格信息模板------------------------------------------------------------->
<script id="specTpl" type="text/html" title="商品规格信息模板">
<label class='field inline label' style='padding-left: 3px;'>规格(可选)</label>
<a href='javascript:void(0);' style='float:right; padding-right: 5px;' onclick='expandDetail(this)'>隐藏</a>
{{# var specVals = d;}}
{{# for(var i = 0, len = specVals.length; i < len; i++){ }}
{{# 	var specVal = specVals[i]; }}
<div id='specItem_{{ specVals.vid }}_{{ specVal.id }}' class='labeled text' style='cursor: default;'>
	<div data-role='specRef_name' class='label'>{{ specVal.specRef.name }}</div>
	{{# if(specVal.colorFlag){ }}
	<div id='specText_{{ specVals.vid }}_{{ specVal.id || "" }}' specVal-id='{{specVal.id || ""}}' spec-id='{{specVal.specId}}' data-colorFlag='{{ specVal.colorFlag }}' ref-code='{{specVal.refCode}}' item-id='{{specVal.specItemId}}' style='background-color: {{specVal.goodsCatSpecItem.value2}}; cursor: default;' class='text'>{{specVal.specVal}}</div>
	{{# }else{ }}
	<div id='specText_{{ specVals.vid }}_{{ specVal.id || "" }}' specVal-id='{{specVal.id || ""}}' spec-id='{{specVal.specId}}' data-colorFlag='{{ specVal.colorFlag }}' ref-code='{{specVal.refCode}}' item-id='{{specVal.specItemId}}' class='text' style='cursor: default;'>{{specVal.specVal}}</div>
	{{# } }}
</div>
{{# } }}
</script>
<!------------------------------------------------------自由组合弹出框模板------------------------------------------------------------->
<script id="freeCombSpecsTpl" type="text/html" title="自由组合弹出框模板">
{{# var dd = d.data;}}
{{# for(var i = 0, len = dd.length; i < len; i++){ }}
<div class='field row' style='height:auto;'>
	<label style='width: 20%;' class='field label required'>{{ dd[i].specRef.name }}</label>
	<div style='width: 80%; height:auto;' class='field group align left'>
		{{# var items = dd[i].goodsCatSpecItems;}}
		<div class='menu'>
			<ul style='width:100%;' data-role='specKey' spec-id='{{ dd[i].id }}' data-specName='{{ dd[i].specRef.name }}'>
		{{# for(var j = 0, _len = items.length; j < _len; j++){ }}
			{{# var item = items[j]}}
			<li style='width:auto; height:25px; padding:0px 5px;'>
				<input id='{{ dd[i].specRef.code }}_{{ item.id }}' spec-id='{{item.specId}}' spec-name='{{ dd[i].specRef.name }}' spec-val='{{item.value}}' color-flag='{{dd[i].colorFlag}}' ref-code='{{dd[i].specRef.code}}' item-id='{{ item.id }}' item-val2='{{item.value2}}' class='specCheckBox' type='checkbox' name='{{ dd[i].specRef.code }}' style='cursor: pointer;' />
				<label for='{{ dd[i].specRef.code }}_{{ item.id }}' class='specCheckBox' checked='checked' style='cursor: pointer;'>{{ item.value }}</label>
				{{# if(j == (_len-1)) { }}
				<label id='qTip_{{ dd[i].specRef.code }}_{{ item.id }}' style='display: none; border: 1px solid #F1D031;background-color: #FFFFA3;color: #F00;border-radius: 4px;padding: 2px 5px;'></label>
				{{# } }}
				</li>
			{{# } }}
		</ul></div>
	</div>
</div>
{{# } }}
</script>
<!------------------------------------------------------商品相册图片信息模板------------------------------------------------------------->

<!------------------------------------------------------属性表格信息模板------------------------------------------------------------->
<script id="attrTabTpl" type="text/html" title="属性表格信息模板">
{{# for(var i = 0, len = d.length; i < len; i++){ }}
<tr>
	<th width="30%"><label class="normal label">{{ d[i].attrRef.name }}</label></th>
	<td width="70%">
		{{# if(d[i].attrRef.enumFlag) { }}
			{{# if(isNoB(d[i].multiFlag) || !d[i].multiFlag) { }}
			<select attrVal-id='' ref-code='{{ d[i].attrRef.code }}' brand-flag='{{d[i].brandFlag}}' key-flag='{{ d[i].keyFlag }}' multi-flag='' data-role='goodsAttr' attr-id='{{ d[i].id }}' ref-id='{{d[i].refId }}' class="field value">
				{{# var items = d[i].goodsCatAttrItems; for(var j = 0, length = items.length; j < length; j++){ }}
					<option item-id='{{ items[j].id }}' value='{{ items[j].value }}'>{{ items[j].value }}</option>
				{{# } }}
			</select>
			{{# }else{ }}
				<div ref-code='{{ d[i].attrRef.code }}' key-flag='{{ d[i].keyFlag }}' multi-flag='true' data-role='goodsAttr' attr-id='{{ d[i].id }}' ref-id='{{d[i].refId }}' class='group align middle'>
				{{# var items = d[i].goodsCatAttrItems; for(var j = 0, length = items.length; j < length; j++){ }}
					<input attrVal-id='' id='{{ items[j].id }}' item-id='{{ items[j].id }}' value='{{ items[j].value }}' type='checkbox' name='{{ d[i].attrRef.code }}' />
					<label style='cursor: pointer; margin-right:5px;' for='{{ items[j].id }}'>{{ items[j].value }}</label>
				{{# } }}
				</div>
			{{# } }}
		{{# }else{ }}
			<input attrVal-id='' ref-code='{{ d[i].attrRef.code }}' key-flag='{{ d[i].keyFlag }}' data-role='goodsAttr' attr-id='{{ d[i].id }}' ref-id="{{d[i].refId }}" class="field two wide value" /> {{ d[i].unit == null ? "": d[i].unit }}
		{{# } }}
	</td>
</tr>
{{# } }}
</script>

<script type="text/html" id="imageItemTpl">
	{{# var itemData = d; }}
	<li data-id="{{itemData.id}}">
		<img class="content" src="{{ itemData.fileBrowseUrl }}"/>
		<div class="action bar">
			<a name="viewOriginal" style="float:left;margin-left:4px;" href="javascript:;" class="button">查看原图</a>
			<a name="delete" data-id="{{itemData.id}}" href="javascript:;" class="button" title="删除">X</a>
		</div>
	</li>
</script>

<script type="text/html" id="noActionImageItemTpl">
	{{# var itemData = d; }}
	<li data-id="{{itemData.id}}">
		<img class="content" src="{{ itemData.fileBrowseUrl }}"/>
	</li>
</script>

<script type="text/html" id="contextMenuItemTpl">
	{{# var itemData = d; }}
	<li class="item">
	{{# if(itemData.value) { }}
		{{# if(itemData.value == "cancelColorDefault") { }}
		<span style="display:inline-block;width:20px;height:20px;line-height:20px;vertical-align:middle;text-align:center;color:red;">X</span>	
		{{# } }}

		{{# if(isHexColor(itemData.value)){ }}
		<span style="display:inline-block;width:20px;height:20px;line-height:20px;vertical-align:middle; border:1px solid gray; background-color:{{itemData.value}}">&nbsp;</span>
		{{# } }}			
	{{# } }}
	{{itemData.text}}
	</li>
</script>
</html>
