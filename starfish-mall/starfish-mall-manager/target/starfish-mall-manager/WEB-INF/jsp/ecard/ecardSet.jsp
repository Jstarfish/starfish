<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>e卡类型管理</title>
</head>

<body id="rootPanel">
	
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<button id="btnToAdd" class="button">添加</button>
				<div class="group right aligned">
					<label class="label">e卡名称</label> <input id="queryName" class="input" /> 
					<span class="spacer"></span>
					<button id="btnToQry" class="button">查询</button>
				</div>
			</div>
		</div>
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	
	<div id="dialog" style="display: none;">
		<div id="addForm" class="form">
				
				<div class="field row">
					<label class="field label required">e卡名称</label> 
					<input type="text" id="name" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field label required">e卡编码</label> 
					<input type="text" id="code" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">面值</label> 
					<input type="text" id="faceVal" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">价格</label> 
					<input type="text" id="price" class="field one half value wide" /> 
				</div>
				<div class="field row">
					<label class="field  label required">身份级别</label> 
					<input type="text" id="rank" class="field one half value wide" /> 
				</div>
				<div class="field row" id="addHideAsSeq" style="display: none">
					<label class="field  label required">序号</label> 
					<input type="text" id="seqNo" class="field one half value wide" /> 
				</div>
				<div class="field row" id="addHide" style="display: none">
					<label class="field label" >状态</label> 
					<select id="ecardStatus" class="field one half value wide">
						<option value="0">启用</option>
						<option value="1">禁用</option>
					</select>
				</div>
				
				<div class="field row" id="imgFieldRow">
					<label class="field  label">上传图片</label> 
					<input name="file" type="file" id="fileToUploadFileLogo" multiple="multiple" class="field value one half wide" />
					<button class="normal button" id="btnfileToUploadFile">上传</button>
				</div>
				<div class="field row" style="height: 90px">
					<input type="hidden"  id="logoUuid" name="logoUuid" />
					<input type="hidden"  id="logoUsage" name="logoUsage"  />
					<input type="hidden"  id="logoPath" name="logoPath"  />
					<label class="field label">图片</label> 
					<img id="logoEcard" height="80px" width="120px" /> 
				</div>
				
			</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js"></script>
	<script>
	//-------------------------------------------自定义方法------------------------------{{
	var oldName = "";// 保留原名称
	var isExist = false;// 是否存在
	/**
	 * 检测是否存在相同的名称
	 * 
	 * @author WJJ
	 * @date 2015年10月13日
	 * 
	 * @param name
	 */
	function isExistByName(name) {
		if (name == oldName) {// 验证是否跟原名称一样，如果一样则跳过验证
			isExist = false;
		} else {
			var ajax = Ajax.post("/ecard/exist/by/name");
			ajax.data({
				name : name
			});
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				isExist = result.data;
			});
			ajax.go();
		}
	}
	//上传
	var isChangeImgData=false;
	function fileToUploadFileIcon(fileId) {
		var fileUuidToUpdate = $id("logoUuid").val();
		if (isNoB(fileUuidToUpdate)) {
			var formData = {
				usage : "image.logo",
				subUsage : "ecard"
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
					$id("logoUsage").val(resultInfo.data.files[0].fileUsage);
					$id("logoPath").val(resultInfo.data.files[0].fileRelPath);
					//$("#logoImgagency").makeUniqueRequest("");
					$("#logoEcard").attr("src",resultInfo.data.files[0].fileBrowseUrl + "?" + new Date().getTime());
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
	// -------------------------------------------------------------------------------------}}
	
	$id("mainPanel").jgridDialog(
			{
				pkFldName : "code",
				dlgTitle : "e卡类型列表",
				addUrl : "/ecard/add/do",
				updUrl : "/ecard/update/do",
				delUrl : "/ecard/delete/do",
				//viewUrl : "/ecard/ecard/get/by/code",
				isUsePageCacheToView : true,
				jqGridGlobalSetting:{
					url : getAppUrl("/ecard/list/get/by/filter"),
					colNames : [  "e卡名称", "面值", "价格", "身份级别", "序号","状态", "操作" ],
					colModel : [
							{
								name : "name",
								index : "name",
								width : 200,
								align : 'left'
							},
							{
								name : "faceVal",
								index : "faceVal",
								width : 100,
								align : 'center'
							},
							{
								name : "price",
								index : "price",
								width : 100,
								align : 'center'
							},
							{
								name : "rank",
								index : "rank",
								width : 100,
								align : 'center'
							},
							{
								name : "seqNo",
								index : "seqNo",
								width : 100,
								align : 'center'
							},
							{
								name : "disabled",
								index : "disabled",
								width : 100,
								align : "center",
								formatter : function(cellValue,option, rowObject) {
									if (cellValue == 0) {
										return '启用';
									}else{
										return "禁用";
									}
								}
							},
							{
								name : 'code',
								index : 'code',
								//key : true,
								formatter : function(cellValue, option, rowObject) {
									var s = " [<a class='item dlgview' id='dlgview' href='javascript:void(0);' cellValue='" + cellValue + "' >查看</a>]"
									var ss = "&nbsp;&nbsp;&nbsp;[<a class='item dlgupd' id='dlgupd' href='javascript:void(0);' cellValue='" + cellValue + "' >修改</a>]" 
									var sss = "&nbsp;&nbsp;&nbsp;[<a class='item dlgdel' href='javascript:void(0);' cellValue='" + cellValue + "'>删除</a>] ";
									return s + ss +sss;
								},
								width : 200,
								align : "center"
							} ]
				},
				// 增加对话框
				addDlgConfig : {
					width : Math.min(600, $window.width()),
					height : Math.min(400, $window.height()),
					modal : true,
					open : false
				},
				// 修改和查看对话框
				modAndViewDlgConfig : {
					width : Math.min(600, $window.width()),
					height : Math.min(450, $window.height()),
					modal : true,
					open : false
				},
				queryParam : function(postData,formProxyQuery) {
					var name = formProxyQuery.getValue("queryName");
					if (name != "") {
						postData['name'] = name;
					}
				},
				savePostDataChange : function(postData){
					  var status = intGet("ecardStatus");
					  if(status == "0"){
						  var disabled = false;
					  }else{
						  var disabled = true;
					  }
					  
					  postData["disabled"] = disabled
					  
					  if(isChangeImgData){
						  postData["logoUuid"]=textGet("logoUuid");
						  postData["logoUsage"]=textGet("logoUsage");
						  postData["logoPath"]=textGet("logoPath");
						}
				},
				formProxyInit : function(formProxy) {
					// 注册表单控件
					formProxy.addField({
						id : "name",
						key : "name",
						required : true,
						rules : [ "maxLength[30]", {
							rule : function(idOrName, type, rawValue, curData) {
								isExistByName(rawValue);
								return !isExist;
							},
							message : "名称已存在!"
						} ]
					});
					formProxy.addField({
						id : "faceVal",
						key : "faceVal",
						required : true,
						rules : [ "isMoney","maxLength[14]"]
					});
					formProxy.addField({
						id : "price",
						key : "price",
						required : true,
						rules : [ "isMoney","maxLength[14]" ]
					});
					formProxy.addField({
						id : "rank",
						key : "rank",
						required : true,
						rules : [ "isNum","maxLength[10]" ]
					});
					formProxy.addField({
						id : "seqNo",
						key : "seqNo"
					});
					formProxy.addField({
						id : "code",
						rules : [ "maxLength[30]" ]
					});
				},
				 addInit:function () {
						
						textSet("name", "");
						textSet("faceVal", "");
						textSet("rank", "");
						textSet("price", "");
						textSet("code", "");
						
						textSet("seqNo", "");
						//上传
						textSet("logoUuid", "");
						textSet("logoUsage", "");
						textSet("logoPath", "");
						$("#logoEcard").attr("src",null);
						
						$id("addHideAsSeq").hide();
						$id("addHide").hide();
					},
					 modAndViewInit:function (data) {
						 $id("addHide").show();
						 $id("addHideAsSeq").show();
						 
						 if(data.disabled == "0"){
							  intSet("ecardStatus", 0);
							  $("#dialog input").attr("disabled", "disabled");
							  $("#dialog button").attr("disabled", "disabled");
						  }else{
							  intSet("ecardStatus", 1);
						  }
						 
						 textSet("name", data.name);
						 textSet("rank", data.rank);
						 textSet("seqNo", data.seqNo);
						 textSet("code", data.code);
						 $("#code").attr("disabled", "disabled");
						 textSet("faceVal", data.faceVal);
						 textSet("price", data.price);
						 textSet("logoUuid", data.logoUuid);
						 textSet("logoUsage", data.logoUsage);
						 textSet("logoPath", data.logoPath);
						 $("#logoEcard").attr("src",data.fileBrowseUrl + "?" + new Date().getTime());
					},
				formProxyQueryInit : function(formProxyQuery) {
					// 注册查询表单控件
					formProxyQuery.addField({
						id : "queryName",
						rules : [ "maxLength[30]" ]
					});
				},
				saveOldData : function(data) {
					oldName = data.name;// 保存原来名称，用于检测是否存在
				},
				getDelComfirmTip : function(data) {
					var theSubject = data.name;
					return '确定要删除"' + theSubject + '"吗？';
				},
				pageLoad : function() {
					var size = getImageSizeDef("image.logo");
					$id("logoEcard").attr("width", size.width);
					$id("logoEcard").attr("height", size.height);
					
					//上传
					initFileUpload("fileToUploadFileLogo");
					//绑定修改模块上传按钮
					$id("btnfileToUploadFile").click(function(){
						isChangeImgData=true;
						fileToUploadFileIcon("fileToUploadFileLogo");
					});
				}
			});
	
	//-----------------------------------------------------------------------------------}}
	
	</script>
</body>
</html>