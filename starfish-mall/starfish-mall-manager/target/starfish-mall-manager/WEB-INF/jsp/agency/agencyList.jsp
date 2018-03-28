<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/base.jsf"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>代理处管理</title>
</head>

<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned">
					<button id="btnAdd" class="button">添加</button>
					<button id="btnDelAgencies" class="button">批量删除</button>
					<button id="btnAuditAgencies" class="button">批量审核</button>
				</div>
				<span class="vt divider"></span>
				<button id="btnBatchImp" class="button" style="display: none">批量导入</button>
				<button id="btnBatchExp" class="button" style="display: none">批量导出</button>
				<div class="group right aligned">
					<label class="label">所属代理商</label> 
					<input id="queryAgentName" class="normal input" /> 
					<span class="spacer"></span> 
					<label class="label">代理处名称</label> 
					<input id="queryAgencyName" class="normal input" /> 
					<span class="spacer"></span> 
					<label class="label">代理处编号</label> 
					<input id="queryAgencyCode" class="normal input" /> 
					<span class="spacer"></span> 
					<label class="label">代理处状态</label> 
					<select id="queryAgencyStatus" class="normal input">
						<option value="-1">全部</option>
						<option value="0">启用</option>
						<option value="1">禁用</option>
						<option value="2">待审核</option>
						<option value="3">审核未通过</option>
					</select> 
					<span class="vt divider"></span>

					<button id="btnQueryAgency" class="normal button">查询</button>
				</div>
			</div>
		</div>

	</div>
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	<!-- 	隐藏Dialog -->
	<div id="dialog" style="display: none;">
		<fieldset>
			<legend>代理商信息</legend>
			<div class="form">
				<div class="field row">
					<label class="field label required">手机号码</label> 
					<input type="hidden" id="agentId" class="field value" /> 
					<input type="text" id="userPhoneNo" class="field one half value wide" /> 
					<span class="normal spacer"></span> 
					<label class="field inline label required">真实姓名</label> 
					<input type="text" id="realName" class="field one half value wide" />
				</div>
				<div class="field row">
					<label class="field label required">身份证号</label> 
					<input type="text" id="idCertNo" class="field one half value wide" /> 
					<span class="normal spacer"></span> 
					<label class="field inline label required">邮<span class="chs spaceholder two after"></span>箱</label> 
					<input type="text" id="email" class="field one half value wide" />
				</div>
			</div>
		</fieldset>
		<fieldset>
			<legend>代理处信息</legend>
			<div class="form">

				<div class="field row">
					<label class="field label required">代理名称</label> 
					<input type="text" id="name" class="field one half value wide" /> 
					<span class="normal spacer"></span> 
					<label class="field label required inline ">联系电话</label> 
					<input type="text" id="phoneNo" class="field one half value wide" />
				</div>
				<div class="field row">
					<label class="field inline label wide required">企业代理处</label>
					<div class="field group" style="width: 170px;">
						<input id="entpFlag-Y" type="radio" name="entpFlag" value="1" onChange="showLicense(true)" /> 
						<label for="entpFlag-Y">是</label>
						<input id="entpFlag-N" type="radio" name="entpFlag" value="0" checked="checked" onChange="showLicense(false)" /> 
						<label for="entpFlag-N">否</label>
					</div>
					<span class="normal spacer"></span>
					<div id="license" style="display: none;" class="field group">
						<label class="field inline label required">营业执照</label> 
						<input id="licenseId" type="text" class="field one half value wide" />
					</div>
				</div>
				<div class="field row">
					<label class="field  label required">联系人</label> 
					<input type="text" id="linkMan" class="field one half value wide" /> 
					<span class="normal spacer"></span> 
					<label class="field inline label ">固定电话</label>
					<input type="text" id="telNo" class="field one half value wide" />
				</div>
				<div class="field row" id="addHide">
					<label class="field label ">代理处编号</label> 
					<input type="text" id="code" class="field one half value wide" /> 
					<input type="hidden" id="agencyHiddenId" class="field one half value wide" /> 
					<input type="hidden" id="agencyHiddenRegionId" class="field one half value wide" /> 
					<input type="hidden" id="agencyHiddenRegionName" class="field one half value wide" /> 
					<span class="normal spacer"></span>
					<label class="field inline label" style="width: 62px">状态</label> 
					<select id="agencyStatus" class="field one half value wide">
						<option value="0">启用</option>
						<option value="1">禁用</option>
						<option value="2">待审核</option>
						<option value="3">审核未通过</option>
					</select>
				</div>
				<div class="field row" id="addHideTime">
					<label class="field  label">申请时间</label> 
					<input type="text" id="applyTime" class="field one half value wide" />
				</div>
				<div class="field row">
					<label class="field label required ">所在地</label> 
					<select class="field value" id="province"></select> 
					<select class="field value" id="city"></select> 
					<select class="field value" id="county"></select> 
					<select class="field value" id="town" style="display: none;"></select> 
					<span class="normal spacer"></span> 
					<span class="normal spacer"></span>
					<span class="normal spacer"></span>
				</div>
				<div class="field row">
					<label class="field  label required">街道地址</label> 
					<input type="text" id="street" class="field three value wide" /> 
					<span class="normal spacer three value wide"></span> 
					<!-- <span class="normal spacer three value wide"></span> -->
				</div>
				<div class="field row" style="height: 30px;display: none;">
					<div>
						<input type="hidden" id="logoUuid" /> 
						<input type="hidden" id="logoUsage" /> 
						<input type="hidden" id="logoPath" />
					</div>
				</div>
				<div class="field row" style="height: 60px;">
					<label class="field label required">经营范围</label>
					<textarea id="bizScope" class="field three value wide " style="height: 50px;"></textarea>
					<span class="normal spacer"></span>
				</div>
				<div class="field row" id="agencyMapShow">
					<label class="field  label">上传LOGO</label> 
					<input name="file" type="file" id="fileToUploadFileLogo" multiple="multiple" class="field value one half wide" />
					<button class="normal button" id="btnfileToUploadFile">上传</button>
					<span class="normal spacer"></span>
				</div>
				<div class="field row" style="height: 90px" id="agencyMapShowButton">
					<label class="field label">LOGO</label> 
					<img id="logoImgagency" height="80px" width="120px" /> 
					<span class="normal spacer"></span>
				</div>
			</div>
		</fieldset>
	</div>

	<div id="dialog_auditAgencies" title="批量审核代理处" style="display: none;">
		<div class="form">
			<div class="field row">
				<label class="field label one half wide">是否通过</label>
				<div class="field group">
					<input id="bathagencyAuditStatus-Y" type="radio" name="bathagencyAuditStatus" value="1" /> 
					<label for="bathagencyAuditStatus-Y">通过审核</label> 
					<input id="bathagencyAuditStatus-N" type="radio" name="bathagencyAuditStatus" value="2" /> 
					<label for="bathagencyAuditStatus-N">审核拒绝</label>
				</div>
			</div>
			<div class="field row" style="height: 100px;">
				<label class="field label one half wide">备注</label>
				<textarea id="bathagencyAuditDesc" class="field value two wide" style="height: 80px;"></textarea>
			</div>
		</div>
	</div>

	<div id="auditAgencyDialog" title="审核代理处" style="display: none;">
		<div class="form">
			<div class="field row">
				<input type="hidden" id="agencyAuditHiddenId" /> 
				<label class="field label one half wide">是否通过</label>
				<div class="field group">
					<input id="agencyAuditStatus-Y" type="radio" name="agencyAuditStatus" value="1" /> 
					<label for="agencyAuditStatus-Y">通过审核</label> 
					<input id="agencyAuditStatus-N" type="radio" name="agencyAuditStatus" value="2" /> 
					<label for="agencyAuditStatus-N">审核拒绝</label>
				</div>
			</div>
			<div class="field row" style="height: 100px;">
				<label class="field label one half wide">备注</label>
				<textarea id="agencyAuditDesc" class="field value two wide" style="height: 80px;"></textarea>
			</div>
		</div>
	</div>

	<div id="batchImpDialog" style="display: none;">
		<div class="form bordered">
			<div class="field row">
				<label class="field label">文件</label> 
				<input class="field value two wide" name="file" type="file" id="impMerchXls" multiple="multiple" />
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		//------------------------自定义变量-------------------------------------------------------------------

		//jqGrid缓存变量
		var jqGridCtrl = null;
		//批量导入代理商信息窗口
		var batchImpDialog;
		//添加代理处信息窗口
		var addAgencyDialog;
		//查看代理处信息窗口
		var showAgencyDialog;
		//修改代理处信息窗口
		var editAgencyDialog;
		//审核代理处信息窗口
		var auditAgencyDialog;
		//批量审核窗口
		var dialog_auditAgencies;
		//批量审核时，储存的ids
		var auditIds = "";

		// 代理处当前操作
		var agencyGridHelper = JqGridHelper.newOne("");
		//------------------------验证-------------------------------------------------------------------
		//实例化表单代理
		var formProxy = FormProxy.newOne();
		//
		formProxy.addField({
			id : "linkMan",
			required : true,
			rules : [ "maxLength[30]" ]
		});
		formProxy.addField({
			id : "name",
			required : true,
			rules : [ "maxLength[30]" ]
		});
		formProxy.addField({
			id : "userPhoneNo",
			key : "agent.user.phoneNo",
			required : true,
			rules : [ "maxLength[11]", "isMobile" ]
		});
		formProxy.addField({
			id : "phoneNo",
			required : true,
			rules : [ "maxLength[11]", "isMobile" ]
		});
		formProxy.addField({
			id : "street",
			required : true,
			rules : [ "maxLength[50]" ]
		});
		formProxy.addField({
			id : "bizScope",
			required : true,
			rules : [ "maxLength[100]" ]
		});
		formProxy.addField({
			id : "province",
			required : true
		});
		formProxy.addField({
			id : "city",
			required : true
		});
		formProxy.addField({
			id : "county",
			required : true
		});
		formProxy.addField({
			id : "town",
			rules : [ {
				rule : function(idOrName, type, rawValue, curData) {
					// 区若显示且为空，给予提示
					if ($("#town").is(":visible") && isNoB(rawValue)) {
						return false;
					}

					return true;
				},
				message : "此为必须提供项！"
			} ]
		});
		formProxy.addField({
			id : "telNo",
			rules : [ "maxLength[15]", "isTel" ]
		});
		
		//
		formProxy.addField({
			id : "email",
			key : "user.email",
		});
		formProxy.addField({
			id : "idCertNo",
			key : "user.idCertNo",
		});
		formProxy.addField({
			id : "realName",
			key : "agent.user.realName",
		});
		

		//实例化查询表单代理
		var formProxyQuery = FormProxy.newOne();
		
		//注册表单控件
		formProxyQuery.addField({
			id : "queryAgentName",
			rules : ["maxLength[30]"]
		});
		formProxyQuery.addField({
			id : "queryAgencyName",
			rules : ["maxLength[30]"]
		});
		formProxyQuery.addField({
			id : "queryAgencyCode",
			rules : ["maxLength[90]"]
		});
		formProxyQuery.addField({
			id : "queryAgencyStatus",
			type : "int",
			required : true
		});
		
		function showLicense(isShow) {
			if (isShow) {
				$id("license").show();
				formProxy.addField({
					id : "licenseId",
					key : "agency.licenseId",
					type : "int",
					required : true,
					rules : [ "maxLength[11]" ]
				});
			} else {
				formProxy.removeField("licenseId")
				$id("license").hide();
			}
		}

		//------------------------调整控件大小-------------------------------------------------------------------

		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "theGridCtrl";
			var jqGridBox = $("#gbox_" + gridCtrlId);
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv",
					jqGridBox).height();
			var pagerHeight = $id("theGridPager").height();
			jqGridCtrl.setGridWidth(mainWidth - 1);
			jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight
					- 3);
		}

		//------------------------jqGrid 初始化-------------------------------------------------------------------
		function initJqGrid() {
			//定义Grid及加载数据
			jqGridCtrl = $("#theGridCtrl").jqGrid(
							{
								url : getAppUrl("/agency/list/get"),
								contentType : 'application/json',
								mtype : "post",
								datatype : 'json',
								height : "100%",
								width : "100%",
								colNames : [ "id", "代理处编号", "代理处名称", "所在地",
										"所属代理商", "联系电话", "代理处状态", " 操作", "" ],
								colModel : [
										{
											name : "id",
											index : "id",
											hidden : true
										},
										{
											name : "code",
											index : "code",
											width : 100,
											align : 'right'
										},
										{
											name : "name",
											index : "name",
											width : 200,
											align : 'left',
										},
										{
											name : "regionName",
											index : "regionName",
											width : 200,
											align : 'left'
										},
										{
											name : "agent.user.realName",
											index : "agent.user.realName",
											width : 100,
											align : 'left'
										},
										{
											name : "phoneNo",
											index : "phoneNo",
											width : 100,
											align : 'right'
										},
										{
											name : "auditStatus",
											index : "auditStatus",
											width : 100,
											align : "center",
											formatter : function(cellValue,option, rowObject) {
												if (cellValue == 0) {
													return '待审核';
												} else if (cellValue == 1) {
													if (rowObject.disabled == false) {
														return "启用";
													} else {
														return "禁用";
													}
												} else {
													return '审核未通过';
												}
											}
										},
										{
											name : 'auditStatus',
											index : 'auditStatus',
											formatter : function(cellValue,option, rowObject) {
												if (cellValue == 0 || cellValue == 2) {
													return "[<a class='item' href='javascript:void(0);' onclick='openAuditDialog("
															+ JSON.stringify(rowObject)
															+ ")' >审核</a>]";
												} else if (cellValue == 1) {
													var agencyDo = "[<a class='item' href='javascript:void(0);' onclick='openshowAgencyDialog("
															+ JSON.stringify(rowObject)
															+ ")' >查看</a>]<span class='chs spaceholder'></span>[<a class='item' href='javascript:void(0);' onclick='openeditAgencyDialog("
															+ JSON.stringify(rowObject)
															+ ")' >修改</a>]<span class='chs spaceholder'></span>[<a class='item' href='javascript:void(0);' onclick='deleteAgency("
															+ JSON.stringify(rowObject)
															+ ")' >删除</a>]<span class='chs spaceholder'></span>";
													if (rowObject.disabled == false) {
														return agencyDo
																+ "[<a class='item' href='javascript:void(0);' onclick='openOrClose("
																+ JSON.stringify(rowObject)
																+ ")' >禁用</a>]";
													} else {
														return agencyDo
																+ "[<a class='item' href='javascript:void(0);' onclick='openOrClose("
																+ JSON.stringify(rowObject)
																+ ")' >开启</a>]";
													}
												} else {
													return '审核未通过';
												}
											},
											width : 200,
											align : "center"
										}, {
											name : "auditStatus",
											index : "auditStatus",
											hidden : true
										}, ],
								//rowList:[10,20,30],
								multiselect : true,
								pager : "#theGridPager",
								loadComplete : function(gridData) { // JqGridHelper缓存最新的grid数据
									agencyGridHelper.cacheData(gridData);
									var callback = getCallbackAfterGridLoaded();
									if (isFunction(callback)) {
										callback();
									}
								},
								ondblClickRow : function(rowId) {
									var userMap = agencyGridHelper.getRowData(rowId)
									openshowAgencyDialog(userMap);
								}
							});
		}

		function getCallbackAfterGridLoaded() {

		}

		//------------------------对代理处的操作------------------------------------------------------------------
		//更改代理处 禁用 开启状态
		function openOrClose(obj) {
			var postData = {
				id : obj.id,
				disabled : !obj.disabled
			};
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/agency/disabled/update/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					jqGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
					Layer.msgSuccess(result.message);
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.always(function() {
				//隐藏等待提示框
				loaderLayer.hide();
			});
			ajax.go();
		}

		//审核代理处
		function auditAgency() {
			var postData = {
				agencyId : $id("agencyAuditHiddenId").val(),
				auditStatus : $("input[name='agencyAuditStatus']:checked").val(),
				desc : $id("agencyAuditDesc").val()
			};
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/agency/audit/update/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					jqGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
					auditAgencyDialog.dialog("close");
					Layer.msgSuccess(result.message);
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.always(function() {
				//隐藏等待提示框
				loaderLayer.hide();
			});
			ajax.go();
		};

		//批量审核代理处
		function auditAgencies() {
			var postData = {
				ids : auditIds,
				auditStatus : $("input[name='bathagencyAuditStatus']:checked").val(),
				desc : $id("bathagencyAuditDesc").val()
			};
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/agency/batch/audit/update/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					auditIds = "";
					jqGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
					dialog_auditAgencies.dialog("close");
					Layer.msgSuccess(result.message);
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.always(function() {
				auditIds = "";
				//隐藏等待提示框
				loaderLayer.hide();
			});
			ajax.go();
		}

		//删除代理处 
		function deleteAgency(obj) {
			var theLayer = Layer.confirm('确定要删除该代理处吗？', function() {
				var postData = {
					id : obj.id
				};
				var ajax = Ajax.post("/agency/delete/do");
				ajax.data(postData);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						theLayer.hide();
						Layer.msgSuccess(result.message);
						jqGridCtrl.jqGrid("setGridParam", {
							page : 1
						}).trigger("reloadGrid");
					} else {
						theLayer.hide();
						Layer.msgWarning(result.message);
					}
				});
				ajax.go();
			});
		}

		//批量导入代理处
		function batchImpMerchant() {
			sendFileUpload("impMerchXls", {
				url : getAppUrl("/file/upload"),
				dataType : "json",
				//自定义数据
				formData : {
					usage : "agency.extract"
				},
				done : function(e, result) {
					//done方法就是上传完毕的回调函数，其他回调函数可以自行查看api  
					//注意result要和jquery的ajax的data参数区分，这个对象包含了整个请求信息  
					//返回的数据在result.result中，假设我们服务器返回了一个json对象 
					var resultInfo = result.result;
					console.log(resultInfo);
					var resultData = resultInfo.data || {};
					var files = resultData.files || [];
					for (var i = 0; i < files.length; i++) {
						var file = files[i];
						if (file.type == "info") {
							Layer.msgSuccess(file.message);
							batchImpDialog.dialog("close");
							jqGridCtrl.jqGrid("setGridParam", {
								page : 1
							}).trigger("reloadGrid");
						} else {
							Layer.msgWarning(file.message);
						}
					}
				},
				fail : function(e, data) {
					console.log(data);
				}
			});
		}
		//添加代理处
		function addAgency() {
			var vldResult = formProxy.validateAll();
			if (!vldResult) {
				return;
			}
			//设置最后一级地区id
			var townId = $id("town").val();
			if (isNoB(townId)) {
				var regionId = $id("county").val();
			} else {
				var regionId = townId;
			}
			//判断是否企业代理处
			var entpValue = $('input[name="entpFlag"]:checked ').val();
			  if(entpValue=="1"){
				 var entpFlag = true;
			  }else{
				 var entpFlag = false;  
			  }
			//
			var postData = {
				//id : $id("agencyHiddenId").val(),
				linkMan : $id("linkMan").val(),
				name : $id("name").val(),
				phoneNo : $id("phoneNo").val(),
				street : $id("street").val(),
				bizScope : $id("bizScope").val(),
				logoUuid : $id("logoUuid").val(),
				logoUsage : $id("logoUsage").val(),
				logoPath : $id("logoPath").val(),
				regionId : regionId,
				regionName : $("#province option:selected").text() + " "
						+ $("#city option:selected").text() + " "
						+ $("#county option:selected").text() + " "
						+ $("#town option:selected").text(),
				telNo : $id("telNo").val(),
				entpFlag : entpFlag,
				agentId : intGet("agentId")
			};
			//alert(JSON.encode(postData));
			var ajax = Ajax.post("/agency/add/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					addAgencyDialog.dialog("close");
					Layer.msgSuccess(result.message);
					queryAgency();
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
		}
		//修改代理处
		function updateAgency() {
			var vldResult = formProxy.validateAll();
			if (!vldResult) {
				return;
			}
			//
			var townId = $id("town").val();
			if (isNoB(townId)) {
				var regionId = $id("county").val();
			} else {
				var regionId = townId;
			}
			//判断是否企业代理处
			var entpValue = $('input[name="entpFlag"]:checked ').val();
			  if(entpValue=="1"){
				 var entpFlag = true;
			  }else{
				 var entpFlag = false;  
			  }
			//
			var postData = {
				id : $id("agencyHiddenId").val(),
				name : $id("name").val(),
				phoneNo : $id("phoneNo").val(),
				entpFlag : entpFlag,
				linkMan : $id("linkMan").val(),
				telNo : $id("telNo").val(),
				regionName : $("#province option:selected").text() + " "
						+ $("#city option:selected").text() + " "
						+ $("#county option:selected").text() + " "
						+ $("#town option:selected").text(),
				street : $id("street").val(),
				bizScope : $id("bizScope").val(),
				logoUuid : $id("logoUuid").val(),
				logoUsage : $id("logoUsage").val(),
				logoPath : $id("logoPath").val(),
				regionId : regionId
			};
			var loaderLayer = Layer.loading("正在提交数据...");
			var ajax = Ajax.post("/agency/update/do");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					jqGridCtrl.jqGrid("setGridParam", {
						page : 1
					}).trigger("reloadGrid");
					Layer.msgSuccess(result.message);
					editAgencyDialog.dialog("close");
					//openeditAgencyDialog(rowData);
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.always(function() {
				//隐藏等待提示框
				loaderLayer.hide();
			});
			ajax.go();
		}
		
		//
		function queryAgency(){ 
			var vldResult = formProxyQuery.validateAll();
			if (!vldResult) {
				return;
			}
			//
			var realName = textGet("queryAgentName");
			var name = textGet("queryAgencyName");
			var code = textGet("queryAgencyCode");
			var status = intGet("queryAgencyStatus");
				
			if (status == -1) {
				var filterStr = JSON.encode({
					"name" : name,
					"realName" : realName,
					"code" : code
				}, true);
			}else if (status == 0) {
				var filterStr = JSON.encode({
					"name" : name,
					"realName" : realName,
					"code" : code,
					"disabled" : false,
				}, true);
			} else if (status == 1) {
				var filterStr = JSON.encode({
					"name" : name,
					"realName" : realName,
					"code" : code,
					"disabled" : true,
				}, true);
			} else if (status == 2) {
				var filterStr = JSON.encode({
					"name" : name,
					"realName" : realName,
					"code" : code,
					"auditStatus" : "0"
				}, true);
			} else {
				var filterStr = JSON.encode({
					"name" : name,
					"realName" : realName,
					"code" : code,
					"auditStatus" : "2"
				}, true);
			}
			//alert(filterStr);
			//加载jqGridCtrl
			jqGridCtrl.jqGrid("setGridParam", {
				postData : {
					filterStr : filterStr
				},
				page : 1
			}).trigger("reloadGrid");
		}
		
		//检验手机号码是否唯一
		function checkUserPhoneNo(userPhoneNo) {
				var phoneNo1 = $id("userPhoneNo").val();
				var valiPhoneNo = formProxy.validate("userPhoneNo");
				if(phoneNo1 == null || phoneNo1 == '' || !valiPhoneNo){
					return;
				}
				var postData = {
						phoneNo : phoneNo1	
				};
				var ajax = Ajax.post("/agent/exist/by/phoneNo/do");
				ajax.data(postData);
				ajax.sync();
				ajax.done(function(result, jqXhr) {
						var data = result.data
						if(data){
							if(!data.agentFlag){
								var layer = Layer.confirm("此用户还不是代理商，请去往代理商页面，先添加其为代理商", function() {
									$id("userPhoneNo").val("");
									$id("email").val("");
									$id("idCertNo").val("");
									$id("realName").val("");
									hideMiscTip("userPhoneNo");
									layer.hide();
								}, function() {
									$id("userPhoneNo").val("");
									hideMiscTip("userPhoneNo");
									layer.hide();
								});
							}else{
								//获取用户信息
								var layer = Layer.confirm("此代理商存在，点击确定，继续添加代理处", function() {
									var user = data.user;
									$id("agentId").val(user.id);
									
									hideMiscTip("userPhoneNo");
									
									formProxy.setValue2("user.email",user.email);
									formProxy.setValue2("user.idCertNo",user.idCertNo);
									formProxy.setValue2("agent.user.realName",user.realName);
									
									$id("email").attr("disabled",true);
									$id("idCertNo").attr("disabled",true);
									$id("realName").attr("disabled",true);
									layer.hide();
								}, function() {
									$id("userPhoneNo").val("");
									$id("agentId").val("");
									layer.hide();
								});
							}
						}
				});
				ajax.fail(function() {
					$id("userPhoneNo").val("");
				});
				ajax.go();
			//}
		}
		
		//------------------------Dialog 弹出方法------------------------------------------------------------------
		//弹出添加代理处窗口
		function openAddDialog() {
			$id("divider").show();
			$id("acctDivider").show();

			initAddDialog();
			addAgencyDialog.dialog("open");
		}

		//弹出审核框（单）
		function openAuditDialog(obj) {
			$("input[name='agencyAuditStatus'][value='1']").attr("checked","checked");
			$id("agencyAuditHiddenId").val(obj.id);
			initAuditAgencyDialog(obj);
			auditAgencyDialog.dialog("open");
		}
		//弹出导入代理处窗口  
		function openImpDialog(obj) {
			initbatchImpDialog();
			batchImpDialog.dialog("open");
		}
		//弹出查看代理处窗口
		function openshowAgencyDialog(obj) {
			initshowAgencyDialog(obj);
			showAgencyDialog.dialog("open");
		}
		//弹出修改代理处窗口  
		function openeditAgencyDialog(obj) {
			initeditAgencyDialog(obj);
			editAgencyDialog.dialog("open");
		}

		//------------------------Dialog 初始化------------------------------------------------------------------
		function initAuditAgencyDialog(obj) {
			auditAgencyDialog = $("#auditAgencyDialog").dialog(
					{
						autoOpen : false,
						height : Math.min(300, $window.height()),
						width : Math.min(450, $window.width()),
						modal : true,
						title : '审核代理处',
						buttons : {
							"确定" : auditAgency,
							"取消" : function() {
								auditAgencyDialog.dialog("close");
							}
						},
						close : function() {
							$("input[name='agencyAuditStatus'][value='1']")
									.attr("checked", "checked");
							$id("agencyAuditDesc").val("");
							$id("agencyAuditHiddenId").val("");
						}
					});
		}

		function initDialog() {
			//初始化代理处审核Dialog
			dialog_auditAgencies = $("#dialog_auditAgencies").dialog(
					{
						autoOpen : false,
						height : Math.min(300, $window.height()),
						width : Math.min(450, $window.width()),
						modal : true,
						buttons : {
							"确定" : auditAgencies,
							"取消" : function() {
								dialog_auditAgencies.dialog("close");
							}
						},
						close : function() {
							$("input[name='bathagencyAuditStatus'][value='1']")
									.attr("checked", "checked");
							$id("bathagencyAuditDesc").val("");
						}
					});
		}

		//初始化导入界面
		function initbatchImpDialog() {
			batchImpDialog = $("#batchImpDialog").dialog({
				autoOpen : false,
				height : Math.min(200, $window.height()),
				width : Math.min(500, $window.width()),
				modal : true,
				title : '导入代理处',
				buttons : {
					"确定" : batchImpMerchant,
					"取消" : function() {
						batchImpDialog.dialog("close");
					}
				},
				close : function() {
					$("#batchImpDialog input").val("");
				}
			});
		}
		//初始化查看页面
		function initshowAgencyDialog(obj) {
			if (obj.regionId != null) {
				initArea(obj.regionId);
			}
			//
			$("#province").attr("disabled", "disabled");
			$("#city").attr("disabled", "disabled");
			$("#county").attr("disabled", "disabled");
			$("#town").attr("disabled", "disabled");
			$("#dialog  input").attr("disabled", "disabled");
			$("#dialog textarea").attr("disabled", "disabled");
			$("#btnfileToUploadFile").attr("disabled", "disabled");
			$("#agencyStatus").attr("disabled", "disabled");
			
			$id("email").val(obj.agent.user.email);
			$id("idCertNo").val(obj.agent.user.idCertNo);
			$id("realName").val(obj.agent.user.realName);
			$id("userPhoneNo").val(obj.agent.user.phoneNo);
			$id("code").val(obj.code);
			$id("linkMan").val(obj.linkMan);
			$id("name").val(obj.name);
			$id("applyTime").val(obj.regTime);
			$id("bizScope").val(obj.bizScope);
			$id("phoneNo").val(obj.phoneNo);
			$id("street").val(obj.street);
			$id("telNo").val(obj.telNo);
			$id("logoPath").val(obj.logoPath);
			$id("logoUuid").val(obj.logoUuid);
			$id("logoUsage").val(obj.logoUsage);
			$id("logoPath").val(obj.logoPath);
			$id("agencyHiddenRegionName").val(obj.regionName);
			
			if (obj.closed == false) {
				$("#agencyStatus option[value='0']").attr("selected", true);
			} else {
				$("#agencyStatus option[value='1']").attr("selected", true);
			}
			
			if (obj.fileBrowseUrl != null) {
				$("#logoImgagency").attr("src", obj.fileBrowseUrl);
				$id("agencyMapShow").hide();
			} else {
				$id("agencyMapShowButton").hide();
				$id("agencyMapShow").hide();
			}
			showAgencyDialog = $("#dialog").dialog({
				autoOpen : false,
				width : Math.min(700, $window.width()),
				height : Math.min(650, $window.height()),
				modal : true,
				title : '查看代理处',
				buttons : {
					"关闭" : function() {
						showAgencyDialog.dialog("close");
					}
				},
				close : function() {
				}
			});
		}
		//初始化添加窗口
		function initAddDialog() {
			$("#dialog input").attr("disabled", false);
			$("#dialog textarea").attr("disabled", false);
			$("#dialog textarea").val("");
			$("#dialog input[type=text]").val("");
			$("#agentId").val("");
			$id("addHide").hide();
			$id("addHideTime").hide();
			showLicense(false);
			addAgencyDialog = $("#dialog").dialog({
				autoOpen : false,
				width : Math.min(700, $(window).width()),
				height : Math.min(650, $(window).height()),
				modal : true,
				title : '添加代理处',
				buttons : {
					"确定" : addAgency,
					"取消" : function() {
						addAgencyDialog.dialog("close");
					}
				},
				close : function() {
					formProxy.hideMessages();
				}
			});
		}
		//初始化修改窗口
		function initeditAgencyDialog(obj) {
			if (obj.regionId != null) {
				initArea(obj.regionId);
			}
			$id("agencyMapShowButton").show();
			$id("agencyMapShow").show();
			$("#province").removeAttr("disabled");
			$("#city").removeAttr("disabled");
			$("#county").removeAttr("disabled");
			$("#town").removeAttr("disabled");
			$("#dialog input").removeAttr("disabled");
			$("#dialog textarea").removeAttr("disabled");
			$id("idCertNo").attr("disabled", "disabled");
			$id("applyTime").attr("disabled", "disabled");
			$id("code").attr("disabled", "disabled");
			$id("realName").attr("disabled", "disabled");
			$id("email").attr("disabled", "disabled");
			$id("agencypIng").attr("disabled", "disabled");
			$id("userPhoneNo").attr("disabled", "disabled");
			$("#agencyStatus").attr("disabled", "disabled");
			$("#btnfileToUploadFile").removeAttr("disabled");
			
			$id("idCertNo").val(obj.agent.user.idCertNo);
			$id("email").val(obj.agent.user.email);
			$id("realName").val(obj.agent.user.realName);
			$id("userPhoneNo").val(obj.agent.user.phoneNo);
			
			$id("agencyHiddenId").val(obj.id);
			$id("code").val(obj.code);
			$id("linkMan").val(obj.linkMan);
			$id("name").val(obj.name);
			$id("applyTime").val(obj.regTime);
			$id("bizScope").val(obj.bizScope);
			$id("phoneNo").val(obj.phoneNo);
			$id("logoPath").val(obj.logoPath);
			$id("logoUuid").val(obj.logoUuid);
			$id("logoUsage").val(obj.logoUsage);
			$id("logoPath").val(obj.logoPath);
			$id("agencyHiddenRegionId").val(obj.regionId);
			$id("agencyHiddenRegionName").val(obj.regionName);
			$id("street").val(obj.street);
			$id("telNo").val(obj.telNo);
			
			if (obj.fileBrowseUrl != null) {
				$("#logoImgagency").attr("src", obj.fileBrowseUrl);
			}
			if (obj.closed == false) {
				$("#agencyStatus option[value='0']").attr("selected", true);
			} else {
				$("#agencyStatus option[value='1']").attr("selected", true);
			}
			
			editAgencyDialog = $("#dialog").dialog({
				autoOpen : false,
				height : Math.min(600, $window.height()),
				width : Math.min(650, $window.width()),
				modal : true,
				title : '修改代理处',
				buttons : {
					"保存" : updateAgency,
					"关闭" : function() {
						editAgencyDialog.dialog("close");
					}
				},
				close : function() {
					formProxy.hideMessages();
				}
			});
		}
		//------------------------上传-------------------------
		//
		function fileToUploadFileIcon(fileId) {
			var fileUuidToUpdate = $id("logoUuid").val();
			if (isNoB(fileUuidToUpdate)) {
				var formData = {
					usage : "image.logo",
					subUsage : "agency"
				};
			} else {
				var formData = {
					update : true,
					uuid : fileUuidToUpdate
				};
			}
			//
			sendFileUpload(fileId, {
				url : getAppUrl("/file/upload"),
				dataType : "json",
				//自定义数据
				formData : formData,
				done : function(e, result) {
					var resultInfo = result.result;
					if (resultInfo.type == "info") {
						$id("logoUuid").val(resultInfo.data.files[0].fileUuid);
						$id("logoUsage")
								.val(resultInfo.data.files[0].fileUsage);
						$id("logoPath").val(
								resultInfo.data.files[0].fileRelPath);
						//$("#logoImgagency").makeUniqueRequest("");
						$("#logoImgagency").attr(
								"src",
								resultInfo.data.files[0].fileBrowseUrl + "?"
										+ new Date().getTime());
						;
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
		//------------------------加载省市县-------------------------
		function initArea(sonId) {
			var params = {
				id : sonId
			};
			var ajax = Ajax.get("/setting/region/get/by/id");
			ajax.params(params);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data;
					if (data != null) {
						var idPath = data.idPath;
						if (idPath != null) {
							arr = idPath.split(',');
							if (arr.length == 2) {
								$("#province option[value='" + arr[0] + "']")
										.attr("selected", true);
								loadCityByParentId(arr[0], arr[1]);
								loadCountyByCityId(arr[1], sonId);
							} else {
								$("#province option[value='" + arr[0] + "']")
										.attr("selected", true);
								loadCityByParentId(arr[0], arr[1]);
								loadCountyByCityId(arr[1], arr[2]);
								loadTownByCountyId(arr[2], sonId);
							}
						}
					}

				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		// 加载省份
		function loadProvince() {
			// 隐藏页面区
			hideTown();

			var ajax = Ajax.get("/setting/region/selectList/get");
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("province", result.data);
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}

		// 根据页面选择的省加载市
		function loadCity() {
			// 隐藏页面区
			hideTown();

			var ajax = Ajax.get("/setting/region/selectList/get");
			ajax.params({
				parentId : $("#province").val()
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					loadSelectData("city", result.data);
					$id("county").val("");
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
					$("#city option[value='" + cityId + "']").attr("selected",
							true);
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
					$("#county option[value='" + countyId + "']").attr(
							"selected", true);
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
					var data = result.data;

					// 标记商城基本信息区数量
					var hasTown = 0;
					for (var i = 0, len = data.items.length; i < len; i++) {
						hasTown++;
					}

					if (hasTown > 0) {
						loadSelectData("town", data);
						$("#town").show();
						$("#town option[value='" + townId + "']").attr(
								"selected", true);
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		// 根据页面选择的市加载县
		function loadCounty() {
			// 隐藏页面区
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

		// 根据选择的县加载区
		function loadTown() {
			// 隐藏页面区
			hideTown();

			var ajax = Ajax.get("/setting/region/selectList/get");
			ajax.params({
				parentId : $("#county").val()
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data;

					// 标记商城基本信息区数量
					var hasTown = 0;
					for (var i = 0, len = data.items.length; i < len; i++) {
						hasTown++;
					}

					if (hasTown > 0) {
						loadSelectData("town", data);
						$("#town").show();
					}
				} else {
					Layer.warning(result.message);
				}
			});
			ajax.go();
		}
		// 隐藏信息区下拉框及错误提示
		function hideTown() {
			$("#town").hide().empty();
			hideMiscTip("town");
		}
		//------------------------初始化-------------------------------------------------------------------

		$(function() {
			//页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 50,
				allowTopResize : false
			});
			//隐藏布局north分割线
			$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
			//初始化JqGrid
			initJqGrid();
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
			//绑定查询按钮
			$id("btnQueryAgency").click(queryAgency);
			//绑定添加按钮
			$id("btnAdd").click(openAddDialog);
			//
			$id("userPhoneNo").blur(function() {
				checkUserPhoneNo(userPhoneNo);
			});
			//绑定批量删除按钮
			$id("btnDelAgencies").click(function() {
				var ids = jqGridCtrl.jqGrid("getGridParam", "selarrrow");
				if (ids == "") {
					Layer.msgWarning("请选中要删除的代理处");
					return;
				}
				var theLayer = Layer.confirm('确定要删除选中的代理处吗？', function() {
					var postData = {
						ids : ids
					};
					var ajax = Ajax.post("/agency/delete/by/ids");
					ajax.data(postData);
					ajax.done(function(result, jqXhr) {
						if (result.type == "info") {
							theLayer.hide();
							Layer.msgSuccess(result.message);
							jqGridCtrl.jqGrid("setGridParam", {
								page : 1
							}).trigger("reloadGrid");
						} else {
							theLayer.hide();
							Layer.msgWarning(result.message);
						}
					});
					ajax.go();
				});
			});
			//绑定批量审核按钮
			$id("btnAuditAgencies").click(function() {
				var ids = jqGridCtrl.jqGrid("getGridParam", "selarrrow");
				if (ids == "") {
					Layer.msgWarning("请选中要审核的代理处");
					return;
				}
				for (var i = 0; i < ids.length; i++) {
					var rowData = jqGridCtrl.jqGrid("getRowData", ids[i]);//根据上面的id获得本行的所有数据
					var val = rowData.auditStatus; //获得制定列的值 （auditStatus 为colModel的name）
					if ("1" == val) {
						Layer.msgWarning("包含已经审核通过的代理处");
						return;
					}
				}
				auditIds = ids;
				initDialog();
				dialog_auditAgencies.dialog("open");
			});
			//批量导入代理处
			initFileUpload("impMerchXls");
			//上传Logo
			initFileUpload("fileToUploadFileLogo");

			$id("btnBatchImp").click(openImpDialog);
			//绑定修改模块上传按钮
			$id("btnfileToUploadFile").click(function() {
				fileToUploadFileIcon("fileToUploadFileLogo");
			});
			// 绑定区域change事件
			$("#province").change(function() {
				loadCity();
			});
			$("#city").change(function() {
				loadCounty();
			});
			$("#county").change(function() {
				loadTown();
			});
			//
			$("#province").empty().show();
			// 加载省列表
			loadProvince();
			var size = getImageSizeDef("image.logo");
			$id("logoImgagency").attr("width", size.width);
			$id("logoImgagency").attr("height", size.height);
		});
	</script>
</body>
</html>