<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>用户博客管理</title>
</head>

<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding: 4px;">
	<div class="filter section">
		<div class="filter row">
			<label class="label">关键字</label> <input id="queryKeyword" class="input one half wide" /> 
			<span class="spacer"></span>
			<button id="btnToQry" class="button">查询</button>
			<span class="spacer"></span>
			<button id="btnToAdd" class="button">发表博文</button>
		</div>
	</div>
	</div>
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	<div id="dialog" style="display: none;">
		<div id="addForm" class="form">
			<div class="field row" style="height:50px">
			<label class="field label required ">标题</label>
			<input type="text" id="title" name="title" class="field value three half wide" maxlength="20" />
			</div>
			<div class="field row" style="height:350px">
			<textarea id="content" name="content" class="field value three half wide" style="height:180px;" maxlength="4000"></textarea>
			</div>
			<div class="field row" style="height:50px" id="imgFieldRow">
				<label class="field label ">图片</label>
					<input name="file" type="file" id="fileToUploadFileImg" multiple="multiple" class="field value one half wide"  /> 
					<button class="normal button" id="btnfileToUploadFile">上传</button>
			</div>
			<div class="field row" style="height:80px">
				<input type="hidden"  id="userBlogImgId" name="userBlogImgId" />
				<input type="hidden"  id="imageUuid" name="imageUuid" />
				<input type="hidden"  id="imageUsage" name="imageUsage"  />
				<input type="hidden"  id="imagePath" name="imagePath"  />
				<label class="field label wide">图片预览</label>
		        <img id="imgBlog" height="80px" width="120px" />
			</div>
			<div class="field row"  >
			<label class="field label" >匿名评论</label>
			<input id="allowAnony-yes" type="radio" name="allowAnony" value="true" />
			<label for="allowAnony-yes">是</label>
			<input id="allowAnony-no" type="radio" name="allowAnony" value="false" checked />
			<label for="allowAnony-no">否</label>
			<input id="published" type="hidden" name="published" value="false"/>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js?<%=System.currentTimeMillis()%>"></script>
	<script>
	//----------------------------------------------自定义方法-----------------------------------{{
		var isChangeImgData=false;
		function fileToUploadFileIcon(fileId) {
			var fileUuidToUpdate = $id("imageUuid").val();
			if(isNoB(fileUuidToUpdate)){//为空时，添加
				var formData = {
					usage : "image.blog"
				};
			}else{//否则修改
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
						$id("imageUuid").val(resultInfo.data.files[0].fileUuid);
						$id("imageUsage").val(resultInfo.data.files[0].fileUsage);
						$id("imagePath").val(resultInfo.data.files[0].fileRelPath);
						$id("imgBlog").attr("src",resultInfo.data.files[0].fileBrowseUrl+"?"+new Date().getTime());
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
	//------------------------------------------------------------------------------------------------------------------}}
	//-----------------------------------------------初始化配置----------------------------------{{
	var theEditor1=null;
	$id("mainPanel").jgridDialog(
			{
				dlgTitle : "用户博客",
				addUrl : "/social/blog/add/do",
				updUrl : "/social/blog/update/do",
				delUrl : "/social/blog/delete/do",
				jqGridGlobalSetting:{
					url : getAppUrl("/social/blog/list/get"),
					colNames : ["标题","内容","匿名评论","是否已发布","评论数","创建时间","更新时间","操作" ],
					colModel : [
							{
								name : "title",
								index : "title",
								width : 200,
								align : 'center',
								formatter : function(cellValue, option, rowObject) {
									var maxwidth = 20;
									if (cellValue.length > maxwidth) {
										cellValue = cellValue.substring(0, maxwidth);
										cellValue = cellValue + "...";
									}
									return cellValue;
								}
							},
							{
								name : "content",
								index : "content",
								width : 200,
								align : 'center',
								formatter : function(cellValue, option, rowObject) {
									var maxwidth = 20;
									if (cellValue.length > maxwidth) {
										cellValue = cellValue.substring(0, maxwidth);
										cellValue = cellValue + "...";
									}
									return cellValue;
								}
							},
							{
								name : "allowAnony",
								index : "allowAnony",
								width : 50,
								align : 'center',
								formatter : function(cellValue, option, rowObject) {
									return cellValue==true?"<font color='green'>是</font>":"<font color='red'>否</font>";
								}
							},
							{
								name : "published",
								index : "published",
								width : 50,
								align : 'center',
								formatter : function(cellValue, option, rowObject) {
									return cellValue==true?"<font color='green'>是</font>":"<font color='red'>否</font>";
								}
							},
							{
								name : "commentCount",
								index : "commentCount",
								width : 60,
								align : 'center'
							},
							{
								name : "createTime",
								index : "createTime",
								width : 65,
								align : 'center'
							},
							{
								name : "changeTime",
								index : "changeTime",
								width : 65,
								align : 'center'
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									var view="<span>[<a class='item dlgview' href='javascript:void(0);' cellValue='" + cellValue + "'>查看</a>]";
									/* if(!rowObject.published){
										view="<span>[<a class='item view' href='javascript:void(0);' cellValue='" + cellValue + "'>预览</a>]";
									} */
									var s = view+"&nbsp;&nbsp;&nbsp;" + "<span>[<a class='item dlgupd' href='javascript:void(0);' cellValue='" + cellValue + "' >修改</a>]" + "&nbsp;&nbsp;&nbsp;[<a class='item dlgdel' href='javascript:void(0);' cellValue='" + cellValue + "' >删除</a>]</span>  ";
									return s;
								},
								width : 200,
								align : "center"
							} ]
				},
				dlgGlobalConfig:{
					width : Math.min(620, $window.width()),
					height : Math.min(710, $window.height()),
					modal : true,
					open : false
				},
				 addInit:function () {
					textSet("title", "");
					textSet("content", "");
					radioSet("allowAnony", "false");
					textSet("userBlogImgId", "");
					textSet("imageUuid", "");
					textSet("imageUsage", "");
					textSet("imagePath", "");
					$id("imgBlog").attr("src","");
					$id("imgFieldRow").show();
					textSet("published", "false");
					if(theEditor1==null){
						theEditor1 = CKEDITOR.replace("content");						
					}
					theEditor1.setData("");
				},
				modElementToggle : function(isShow) {
					
				},
				settingAddBtn:function(buttons,jqGridCtrl){
					var ts=this;
					buttons["发布"]=function() {
						textSet("published", "true");
						ts.submit(this);
					};
				},
				settingModBtn:function(buttons,rowData,jqGridCtrl){
					var ts=this;
					if(!rowData.published){
						buttons["发布"]=function() {
							textSet("published", "true");
							ts.submit(this);
						};
					}
				},
				 modAndViewInit:function (data) {
					textSet("title", data.title);
					textSet("content", data.content);
					radioSet("allowAnony", data.allowAnony.toString());
					textSet("published", data.published.toString());
					if(data.userBlogImg!=null){
						textSet("userBlogImgId", data.userBlogImg.id);
						textSet("imageUuid", data.userBlogImg.imageUuid);
						textSet("imageUsage", data.userBlogImg.imageUsage);
						textSet("imagePath", data.userBlogImg.imagePath);
						$id("imgBlog").attr("src",data.userBlogImg.fileBrowseUrl+"?"+new Date().getTime());
					}
					if(theEditor1==null){
						theEditor1 = CKEDITOR.replace("content");						
					}
					theEditor1.setData(data.content);
					
				},
				queryParam : function(querJson,formProxyQuery) {
					var keyword = formProxyQuery.getValue("queryKeyword");
					if (keyword != "") {
						querJson['keywords'] = keyword;
					}
				},
				formProxyInit : function(formProxy) {
					formProxy.addField({
						id : "title",
						key : "title",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 若显示且为空，给予提示
								if ($id("title").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "此为必填项！"
						} ]
					});
					formProxy.addField({
						id : "content",
						key : "content",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 若显示且为空，给予提示
								if ($id("content").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "此为必填项！"
						} ]
					});
					formProxy.addField({
						id : "allowAnony",
						key : "allowAnony",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 若显示且为空，给予提示
								if ($id("allowAnony").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "此为必填项！"
						} ]
					});
					formProxy.addField({
						id : "published",
						key : "published"
					});
				},
				formProxyQueryInit : function(formProxyQuery) {
					formProxyQuery.addField({
						id : "queryKeyword",
						rules : [ "maxLength[30]" ]
					});
				},
				savePostDataChange:function(postData){
					if(isChangeImgData){
						var imgData={};
						imgData["id"]=$id("userBlogImgId").val();
						imgData["imageUuid"]=$id("imageUuid").val();
						imgData["imageUsage"]=$id("imageUsage").val();
						imgData["imagePath"]=$id("imagePath").val();
						postData["userBlogImg"] = imgData;
					}
					postData["content"]=theEditor1.getData();
				},
				getDelComfirmTip : function(data) {
					return '确定要删除此条记录吗？';
				},
				pageLoad:function(){
					initFileUpload("fileToUploadFileImg",null,"none");
					//绑定修改模块上传按钮
					$id("btnfileToUploadFile").click(function(){
						isChangeImgData=true;
						fileToUploadFileIcon("fileToUploadFileImg");
					});
				}
			});
	//-----------------------------------------------------------------------------------}}
	</script>
</body>
</html>