<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>车辆服务套餐管理</title>

<style type="text/css">
table.gridtable {
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-collapse: collapse;
	width: 600px;
}

table.gridtable th {
	border: 1px solid #AAA;
	padding: 3px;
	background-color: #dedede;
	height: 24px;
	line-height: 22px;
}

table.gridtable td {
	border: 1px solid #AAA;
	padding: 3px;
	background-color: #ffffff;
	height: 24px;
	line-height: 22px;
}
</style>
</head>
<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnToAdd" class="button">添加</button>
				</div>
				<div class="group right aligned">
					<label class="label">套餐名称</label> <input id="queryName"
						class="input" /> <span class="spacer"></span>
					<button id="btnToQry" class="button">查询</button>
				</div>
			</div>
		</div>
	</div>
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	<div id="dialog" style="display: none;">
		<div id="addForm" class="form">
			<div class="field row">
				<label class="field label one half wide required">所属分类</label> <select
					class="field value  one half wide" id="kindId" name="kindId">
					<option value="" title="- 请选择 -">- 请选择 -</option>
				</select>
			</div>
			<div class="field row">
				<label class="field label one half wide required">套餐名称</label> <input
					type="text" id="name" name="name" class="field value one half wide" />
					<input type="hidden" id="packId" name="packId" class="field value one half wide" />
			</div>
			<div class="field row">
				<label class="field label one half wide required">有效时间</label> <input
					type="text" id="startTime" class="field value" />至 <input
					type="text" id="endTime" class="field value" />
			</div>
			<div class="field row" style="height: 70px;">
				<label class="field label one half wide">套餐描述</label>
				<textarea class="field value one half wide"
					style="height: 60px; width: 350px; resize: none;" name="desc"
					id="desc"></textarea>
				<span class="normal spacer"></span>
			</div>
			<div class="field row" style="height: 70px;">
				<label class="field label one half wide">套餐备注</label>
				<textarea class="field value one half wide"
					style="height: 60px; width: 350px; resize: none;" name="memo"
					id="memo"></textarea>
				<span class="normal spacer"></span>
			</div>
			<div class="field row" id="imgFieldRow">
				<label class="field label one half wide">套餐图片</label> <input
					name="file" type="file" id="fileToUploadFileLogo"
					multiple="multiple" class="field value one half wide" />
				<button class="normal button" id="btnfileToUploadFile">上传</button>
			</div>
			<div class="field row" style="height: 60px">
				<input type="hidden" id="imageUuid" name="imageUuid" /> <input
					type="hidden" id="imageUsage" name="imageUsage" /> <input
					type="hidden" id="imagePath" name="imagePath" /> <label
					class="field label one half wide">LOGO</label> <img
					id="logoSvcPack" height="40px" width="100px" />
			</div>
			<div class="field row">
				<label class="field label one half wide required">关联服务</label>
				<div class="targetType">
					<a id="carSvc" href="#">选择</a>
				</div>
			</div>
			<input type="hidden" id="viewDisplay" value="0" name="viewDisplay"
				style="display: none;" />
			<div class="ui-layout-center form">
				<div class="field row"
					style="height: auto; line-height: auto; width: 100%;">
					<table data-role="staff_tab" class="gridtable" style="width: 100%;">
						<thead>
							<tr>
								<th width="25%">服务名称</th>
								<th width="25%">销售价格</th>
								<th width="25%">折扣率</th>
								<th width="25%">操作</th>
							</tr>
						</thead>
						<tbody id="packItem_tbd">
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript"
		src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js"></script>
	<script>
		//------------------------------------------初始化配置---------------------------------
		var oldName = ""; // 保留原名称
		var oldKindId = ""; //保留原kindId;
		var isExist = false;// 是否存在
		function isExistByName(name,kindId) {
			if (name == oldName && oldKindId == kindId) {// 验证是否跟原名称一样，如果一样则跳过验证
				isExist = false;
			} else {
				var ajax = Ajax.post("/carSvc/pack/exist/by/name");
				ajax.data({
					name : name,
					kindId : kindId
				});
				ajax.sync();
				ajax.done(function(result, jqXhr) {
					isExist = result.data;
				});
				ajax.go();
			}
		}
		function loadSvcKindId(selectedId, kindId) {
			if (typeof (kindId) != "undefined" && kindId != null) {
				$id(selectedId).val(kindId);
			}
		}
		function loadSvcKind() {
			// 隐藏页面区
			//hideTown();
			var ajax = Ajax.post("/carSvc/svcKind/selectList/get");
			ajax.params({});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("kindId", result.data);
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		//初始化日历插件
		function initDatePicker(shopInfoProxy) {
			var startTime = textGet("startTime");
			startTime = isNoB(startTime) ? null : startTime;
			var endTime = textGet("endTime");
			endTime = isNoB(endTime) ? null : endTime;
			$id("startTime").datepicker({
				maxDate : endTime,
				lang : 'ch',
				onSelect : function(dateText, inst) {
					$id("endTime").datepicker("option", "minDate", dateText);
					shopInfoProxy.validate("startTime");
				}
			});
			//
			$id("endTime").datepicker({
				endTime : endTime == null ? 0 : endTime,
				lang : 'ch',
				onSelect : function(dateText, inst) {
					$id("startTime").datepicker("option", "maxDate", dateText);
					shopInfoProxy.validate("endTime");
				}
			});
		}
		function openSvcxDlg() {
			//对话框信息
			var pageTitle = "选择服务";
			var pageUrl = "/carSvc/bind/svc/jsp";
			var argName = "__checkSvcIds";
			var extParams = getcheckSvc();
			//对话框参数 预存
			setDlgPageArg(argName, extParams);
			//
			pageUrl = makeDlgPageUrl(pageUrl, argName);
			//
			console.log("页面url：" + pageUrl);
			//打开对话框-----------------------------------------
			var theDlg = Layer.dialog({
				title : pageTitle,
				src : pageUrl,
				area : [ '100%', '100%' ],
				closeBtn : true,
				maxmin : true, //最大化、最小化
				btn : [ "关闭" ],
				yes : function() {
					theDlg.hide();
				}
			});
		}
		//-----------------------------------------------图片上传--------------------------------------------------
		//上传
		function fileToUploadFileIcon(fileId) {
			var fileUuidToUpdate = $id("imageUuid").val();
			if (isNoB(fileUuidToUpdate)) {
				var formData = {
					usage : "image.svc",
					subUsage : "poster"
				};
			} else {
				var formData = {
					update : true,
					uuid : fileUuidToUpdate
				};
			}
			//
			sendFileUpload(fileId,
					{
						url : getAppUrl("/file/upload"),
						dataType : "json",
						//自定义数据
						formData : formData,
						done : function(e, result) {
							var resultInfo = result.result;
							if (resultInfo.type == "info") {
								$id("imageUuid").val(
										resultInfo.data.files[0].fileUuid);
								$id("imageUsage").val(
										resultInfo.data.files[0].fileUsage);
								$id("imagePath").val(
										resultInfo.data.files[0].fileRelPath);
								//$("#logoImgagency").makeUniqueRequest("");
								$("#logoSvcPack").attr(
										"src",
										resultInfo.data.files[0].fileBrowseUrl
												+ "?" + new Date().getTime());
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
		$id("mainPanel")
				.jgridDialog(
						{
							dlgTitle : "车辆服务套餐",
							//listUrl : "/goods/carSvc/list/get",
							addUrl : "/carSvc/pack/add/do",
							updUrl : "/carSvc/pack/update/do",
							delUrl : "/carSvc/delete/do",
							rootPanelSetting : {
								north__size : 60
							},
							jqGridGlobalSetting : {
								url : getAppUrl("/carSvc/pack/list/get"),
								colNames : [ "套餐名称","套餐价格(元)", "套餐描述","开始时间","结束时间", "上下架 ", " 操作" ],
								colModel : [
										{
											name : "name",
											index : "name",
											width : 100,
											align : 'left'
										},
										{
											name : "amountInfo.amount",
											index : "amountInfo.amount",
											width : 50,
											align : 'left',
											formatter : function(cellValue) {
												var amount = cellValue.toFixed(2);
												return "&nbsp;&nbsp;￥&nbsp;"+amount;
											}
										},
										{
											name : "desc",
											index : "desc",
											width : 300,
											align : 'left'
										},
										{
											name : "startTime",
											index : "startTime",
											width : 60,
											align : 'center',
											formatter : function (cellValue, option, rowObject) {
													return rowObject.startTime;
											}
										},
										{
											name : "endTime",
											index : "endTime",
											width : 60,
											align : 'center',
											formatter : function (cellValue, option, rowObject) {
													//var valid = '   '+ rowObject.startTime+"~ "+(rowObject.endTime == null ? "" : rowObject.endTime);
													return rowObject.endTime == null ?"无期限":rowObject.endTime;
											}
										},
										{
											name : "disabled",
											index : "disabled",
											width : 50,
											align : "center",
											formatter : function(cellValue) {
												return cellValue == true ? '<span style="color:red;">未上架</span>'
														: '<span style="color:green;">已上架</span>';
											}
										},
										{
											name : 'id',
											index : 'id',
											formatter : function(cellValue,option, rowObject) {
												var disablied = rowObject.disabled == true ? '上架'
														: '下架';
												var tempItem = String.builder();
												tempItem.append("[<a class='item dlgview' href='javascript:void(0);' cellValue='"+ cellValue+ "' >查看</a>]");
												tempItem.append("&nbsp;&nbsp;&nbsp;[<a class='item updateDisabled' href='javascript:void(0);' cellValue='"+ cellValue+ "' >"+ disablied+ "</a>]");
												if (rowObject.disabled == true) {
													tempItem.append("&nbsp;&nbsp;&nbsp;[<a class='item dlgupd' href='javascript:void(0);' cellValue='"+ cellValue+ "' >修改</a>]");
													tempItem.append("&nbsp;&nbsp;&nbsp;[<a class='item updateDeleted' href='javascript:void(0);' cellValue='"+ cellValue+ "' >删除</a>]");
												}
												return tempItem.value;
											},
											width : 150,
											align : "center"
										} ]
							},
							// 增加对话框
							addDlgConfig : {
								width : Math.min(600, $window.width()),
								height : Math.min(550, $window.height()),
								modal : true,
								open : false
							},
							// 修改和查看对话框
							modAndViewDlgConfig : {
								width : Math.min(600, $window.width()),
								height : Math.min(550, $window.height()),
								modal : true,
								open : false
							},

							/**
							 * 用于修改时显示隐藏元素
							 */
							modElementToggle : function(isShow) {
								$id("viewDisplay").val(0);
								$id("carSvc").click(function() {
									openSvcxDlg();
								});
								$id("carSvc").css("color", "blue");
							},
							viewElementToggle : function(isShow) {
								$id("viewDisplay").val(1);
								$id("carSvc").unbind("click");
								$id("carSvc").css("color", "#222222");
							},
							/**
							 * 增加时清空控件的值
							 */
							addInit : function() {
								textSet("name", "");
								textSet("desc", "");
								textSet("memo", "");
								textSet("startTime", null);
								textSet("endTime", null);
								textSet("imageUuid", "");
								textSet("imageUsage", "");
								textSet("imagePath", "");
								textSet("packId", "");
								$("#logoSvcPack").attr("src", null);
								oldName = "";
								oldKindId = "";
								showPackItem([]);
								//
								$id("carSvc").css("color", "blue");
								$id("carSvc").click(function() {
									openSvcxDlg();
								});
								//
							},

							/**
							 * 修改和查看是给控件赋值
							 */
							modAndViewInit : function(data) {
								//textSet("seqNo", data.seqNo);
								textSet("name", data.name);
								textSet("desc", data.desc);
								textSet("memo", data.memo);
								textSet("startTime", data.startTime);
								textSet("endTime", data.endTime);
								textSet("imageUuid", data.imageUuid);
								textSet("imageUsage", data.imageUsage);
								textSet("imagePath", data.imagePath);
								textSet("packId", data.id);
								loadSvcKindId("kindId",data.kindId);
								$("#logoSvcPack").attr("src",data.fileBrowseUrl + "?"+ new Date().getTime());
								showPackItem(data.packItemList);
							},

							/**
							 * 封装查询参数
							 */
							queryParam : function(querJson, formProxyQuery) {
								var name = formProxyQuery.getValue("queryName");
								if (name != "") {
									querJson['name'] = name;
								}
							},
							savePostDataChange : function(postData) {
								postData["imageUuid"] = textGet("imageUuid");
								if (textGet("imageUsage")) {
									postData["imageUsage"] = textGet("imageUsage");
								}
								postData["imagePath"] = textGet("imagePath");
							},
							/**
							 * 注册增加和修改的验证表单控件
							 */
							formProxyInit : function(formProxy) {
								//注册表单控件
								formProxy.addField({
									id : "name",
									key : "name",
									rules : [{
										rule : function(idOrName, type,rawValue,curData) {
											// 若显示且为空，给予提示
											if ($id("name").is(":visible")&& isNoB(rawValue)) {
												return false;
											}
											var kindId = $id("kindId").val();
											isExistByName(rawValue,kindId);
											return !isExist;
										},
										message : "名称已存在!"
									}, "maxLength[30]" ]
								});
								formProxy.addField({
									id : "endTime",
									key : "endTime",
									required : true
								});
								formProxy.addField({
									id : "startTime",
									key : "startTime",
									required : true
								});
								formProxy.addField({
									id : "desc",
									key : "desc",
									rules : [ "maxLength[100]" ]
								});
								formProxy.addField({
									id : "memo",
									key : "memo",
									rules : [ "maxLength[250]" ]
								});
								formProxy.addField({
									id : "kindId",
									key : "kindId",
									rules : [ {
										rule : function(idOrName, type, rawValue, curData) {
											// 若显示且为空，给予提示
											if ($id("kindId").is(":visible") && isNoB(rawValue)) {
												return false;
											}
											return true;
										},
										message : "此为必填选！"
									} ]
								});
								formProxy.addField({
									key : "packItemList",
									get : function(key, type, curData) {
										var packItemList = [];
										var svcLength = $("[name='svcRates']").length;
										if (svcLength != null&& svcLength > 0) {
											var packId = $id("packId").val();
											$("[name=svcRates]").each(function() {
												var svcId = $(this).attr("data-svcId");
												if (svcId) {
													var item = {};
													var rate = $(this).val();
													if (rate == null|| rate == '') {
														rate = 100;
													}
													item.svcId = svcId;
													item.rate = rate;
													if(packId != "null" && packId != ""){
														item.packId = packId;
													}
													packItemList.add(item);
												}
											});
										}
										return packItemList;
									},
									onHideMessage : function(key) {
										//供formProxy.hideMessages()时调用
										var svcLength = $("[name='svcRates']").length;
										if (svcLength != null&& svcLength > 0) {
											var rate = $attr("name","svcRates");
											hideMiscTip($id(rate));
										} else {
											hideMiscTip($id("carSvc"));
										}
									},
									rules : [ {
										rule : function(key, type) {
											var isAdopt = false;
											var svcLength = $("[name='svcRates']").length;
											if (svcLength != null&& svcLength > 0) {
												$("[name=svcRates]").each(function(index) {
													var svcId = $(this).attr("data-svcId");
													if (svcId) {
														var rate = $(this).val();
														if (isNoB(rate)) {
															showErrorTip(this,"不可为空！");
															return false;
														} else {
															var isNum = FormProxy.validateRules.isNum(rate);
															if (!isNum) {
																showErrorTip(this,"必须为数值！");
																return false;
															} else {
																var isMin = FormProxy.validateRules.minValue(rate,1);
																if (!isMin) {
																	showErrorTip(this,"最小值不能小于1");
																	return false;
																} else {
																	var isMax = FormProxy.validateRules.maxValue(rate,100);
																	if (!isMax) {
																		showErrorTip(this,"最大值不能大于100");
																		return false;
																	} else {
																		hideMiscTip(this);
																		if (index + 1 == svcLength) {
																			isAdopt = true;
																		}
																	}
																}
															}
														}
													}
												});
											} else {
												showErrorTip($id("carSvc"),"请选择服务");
											}
											if (!isAdopt) {
												return false;
											}
											return true;
										}
									}]
								});
								initDatePicker(formProxy);
							},
							/**
							 * 注册查询验证表单控件
							 * @param formProxyQuery
							 */
							formProxyQueryInit : function(formProxyQuery) {
								// 注册表单控件
								formProxyQuery.addField({
									id : "queryName",
									rules : [ "maxLength[30]" ]
								});
							},
							saveOldData : function(data) {
								oldName = data.name;// 保存原来名称，用于检测是否存在
								oldKindId = data.kindId; // 保存原来种类，用于检测是否存在
							},
							/**
							 * 获得删除确认提示
							 */
							getDelComfirmTip : function(data) {
								//var theSubject = data.name;
								//return '确定要删除"' + theSubject + '"车辆服务吗?';
							},
							pageLoad : function(jqGridCtrl, cacheDataGridHelper) {
								loadSvcKind();
								var size = getImageSizeDef("image.logo");
								$id("logoCarSvc").attr("width", size.width);
								$id("logoCarSvc").attr("height", size.height);
								//上传
								initFileUpload("fileToUploadFileLogo");
								//绑定修改模块上传按钮
								$id("btnfileToUploadFile").click (function() {
									fileToUploadFileIcon("fileToUploadFileLogo");
								});
								//上架下架操作
								$(document).on("click",".updateDisabled",function() {
									var id = $(this).attr("cellValue");
									var obj = cacheDataGridHelper.getRowData(id);
									var msg = "";
									if (obj.disabled == false) {
										msg = "服务套餐正在使用中，确定要下架吗？";
									} else {
										msg = "服务套餐确定要上架吗？";
									}
									disabled_confirm(msg, jqGridCtrl,obj);
								});
								$(document).on("click",".updateDeleted",function() {
									var id = $(this).attr("cellValue");
									var obj = cacheDataGridHelper.getRowData(id);
									//updateDeletedById(jqGridCtrl,obj);
									if (obj.disabled == true) {
										deleted_confirm("确定要删除此服务套餐吗?",jqGridCtrl, obj);
									} else {
										Layer.msgWarning("请先下架服务套餐，下架后可删除!");
									}
								});
							}
						});
		//
		function getcheckSvc() {
			var svcIds = [];
			$("[name=svcRates]").each(function() {
				var svcId = $(this).attr("data-svcId");
				if (svcId) {
					svcIds.add(svcId);
				}
			});
			return svcIds;
		}
		//
		function showPackItem(items) {
			var viewDisplay = $id("viewDisplay").val();
			var packItemObj = {
				viewDisplay : viewDisplay,
				items : items
			};
			var theTpl = laytpl($id("svcPackItemTpl").html());
			var htmlStr = theTpl.render(packItemObj);
			$("#packItem_tbd").html(htmlStr);
		}
		function addPackItem(item) {
			if (item != null) {
				var theTpl = laytpl($id("svcPackItemInfoTpl").html());
				var htmlStr = theTpl.render(item);
				var trLength = $("#packItem_tbd").children("tr").length;
				if (trLength <= 0) {
					$("#packItem_tbd").html(htmlStr);
				} else {
					$("#packItem_tbd tr:eq(0)").after(htmlStr);
				}
				hideMiscTip($id("carSvc"));
				return true;
			} else {
				return false;
			}
		}
		function delPackItem(svcId) {
			if (svcId) {
				$id("packItemId" + svcId).remove();
			} else {
				var theLayer = Layer.error("删除失败！", function(layerIndex) {
					theLayer.hide();
				});
			}
		}
		//上架提示
		function disabled_confirm(msg, jqGridCtrl, obj) {
			if (msg != "") {
				var yesHandler = function(layerIndex) {
					theLayer.hide();
					updateDisabled(jqGridCtrl, obj);
				};
				var noHandler = function(layerIndex) {
					theLayer.hide();
				};
				//
				var theLayer = Layer.confirm(msg, yesHandler, noHandler);
			} else {
				updateDisabled(jqGridCtrl, obj);
			}

		}
		//删除提示
		function deleted_confirm(msg, jqGridCtrl, obj) {
			var yesHandler = function(layerIndex) {
				theLayer.hide();
				updateDeleted(jqGridCtrl, obj);
			};
			var noHandler = function(layerIndex) {
				theLayer.hide();
			};
			//
			var theLayer = Layer.confirm(msg, yesHandler, noHandler);
		}
		//上下架操作
		function updateDisabled(jqGridCtrl, obj) {
			var ajax = Ajax.post("/carSvc/pack/disabled/update/do");
			var data = {
				"id" : obj.id,
				"disabled" : obj.disabled == true ? false : true
			};
			ajax.data(data);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					jqGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		//删除操作
		function updateDeleted(jqGridCtrl, obj) {
			var ajax = Ajax.post("/carSvc/pack/deleted/update/do");
			var data = {
				"id" : obj.id,
				"deleted" : obj.deleted == true ? false : true
			};
			ajax.data(data);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					jqGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		function autoToInt(obj){
			$(obj).val(ParseInt($(obj).val()));
		}
	</script>
</body>
<script type="text/html" id="svcPackItemInfoTpl" title="套餐服务明细">
	{{# var item = d; }}
	{{# if(!isNoB(item)){ }}
		<tr id="packItemId{{item.svcId}}">
			<td><label data-role="svcName">{{item.svcName}}</label></td>
			<td><label data-role="svcSalePrice">￥{{item.svcSalePrice}}</label></td>
			<td><label><input class="field value one half wide" onBlur="autoToInt(this)" style="width: 100px;" name="svcRates" data-svcId = "{{item.svcId}}" id="svcRate{{item.svcId}}" type="text" value=""/></label>%</td>
			<td><a href="javascript:void(0)" style="color:blue;" onClick="delPackItem('{{item.svcId}}')">移除</a></td>
		</tr>
	{{# } }}
</script>
<script type="text/html" id="svcPackItemTpl" title="套餐服务明细">
	{{# var packItemObj = d; }}
	{{# var viewDisplay= packItemObj.viewDisplay; }}
	
	{{# var items = packItemObj.items; }}
	{{# if(!isNoB(items) && items.length>0){ }}
	{{# for(var i = 0, len = items.length; i < len; i++){ }}
	{{# var item = items[i]; }}
		<tr id="packItemId{{item.svcId}}">
			<td><label data-role="svcName">{{item.svcName}}</label></td>
			<td><label data-role="svcSalePrice">￥{{item.svcSalePrice}}</label></td>
			<td><label><input class="field value one half wide" onBlur="autoToInt(this)" style="width: 100px;" name="svcRates" {{# if(viewDisplay == 1) { }} disabled="true" {{# } }} data-svcId = "{{item.svcId}}" id="svcRate{{item.svcId}}" type="text" value="{{(item.rate*100).toFixed(0)}}"/></label>%</td>
			<td><a href="javascript:void(0)"{{# if(viewDisplay == 0) { }} style="color:blue;" onClick="delPackItem('{{item.svcId}}') {{# } }}">移除</a></td>
		</tr>
	{{# } }}
	{{# } }}
</script>
</html>