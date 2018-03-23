<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/head.jsp" %>
<style>
<!--

.blog-td tbody td button{
	  cursor: pointer;
    font-size: 12px;
    padding: 4px 10px;
}
h3{
	font-weight: bold;
	color: #666666;
	font-size: 14px;
}
.mod-main{
	margin-top:0px;
}
.inputx{
	color: #666666;
	font-size: 14px;
	width:98.9%;
}
textarea{
	height:500px; 
	width:99.6%;
	font:12px/1.5 "sans serif",tahoma,verdana,helvetica
}
.blog-td {
    line-height: 24px;
}
.blog-td th {
    font-weight: normal;
}
.blog-td > thead {
    background: #f2f2f2 none repeat scroll 0 0;
    line-height: 38px;
}
.blog-td > tbody > tr.sep-row {
    border: 0 none;
    height: 10px;
}
.blog-td > tbody > tr.sep-row > td {
    border: 0 none;
    padding: 0;
}
.blog-td > tbody > .title {
    background: #f2f2f2 none repeat scroll 0 0;
}
.blog-td > tbody > .title > td, .blog-td > tbody > .title > th {
    border: 0 none;
    padding: 4px 10px;
    text-align: left;
}
.blog-td > tbody > tr {
}
.blog-td > tbody > tr > td {
    padding: 5px;
    text-align: left;
}
.blog-td > tbody > tr > th {
    padding: 5px;
    text-align: center;
}
.blog-td a:hover {
    color: #e4393c;
    text-decoration: underline;
}
.blog-td .name span {
    display: inline-block;
    margin-right: 5px;
    vertical-align: middle;
}
.blog-td .name b {
    background: rgba(0, 0, 0, 0) url("../image-app/icons.png") no-repeat scroll -140px 0;
    display: inline-block;
    height: 14px;
    vertical-align: middle;
    width: 13px;
}
-->
</style>
<div class="content" style="display:none" id="contentDiv">
	<div class="page-width">
		<div class="crumbs">
			<a href="/">首页</a>&gt;<a href="javascript:void(0)">我的订单</a>&gt;<a href="javascript:void(0)">发表博文</a>
		</div>
		<div class="section" >
			<jsp:include page="/WEB-INF/jsp/include/ucenterMenu.jsp"></jsp:include>
			<div class="section-right1">
				<div class="mod-main">
					<form id="blogForm" onSubmit="return false">
					<table class="blog-td" id="dataList" style="text-align: left">
						<tr><td><h3 id="opText">写博文</h3></td></tr>
						<tr><td>
							<table class="blog-td"><thead id="orderThead">
						</thead></table>
						</td></tr> 
						<tr>
							<td >
								<input type="hidden" name="orderId" id="orderId"/>
								<input class="inputx inputx-h26 " type="text" name="title" id="title" placeholder="输入博文标题" maxlength="30"><span class="red ml20" id="titleTip" style="display:none"></span>
							</td>
						</tr>
						<tr>
							<td>
							<textarea name="content" id="content" maxlength="500" ></textarea><span class="red ml20" id="contentTip" style="display:none"></span>
							</td>
						</tr>
						<tr>
							<td><label>是否匿名评论：</label>
								<input id="allowAnony-yes" type="radio" name="allowAnony" value="true" /> <label for="allowAnony-yes">是</label>
								<input id="allowAnony-no" type="radio" name="allowAnony" value="false" checked="checked"/> <label for="allowAnony-no">否</label>
							</td>
						</tr>
						<tr>
							<td >图片： <input name="file" type="file" id="fileToUploadFileImg" multiple="multiple" /><button class="normal button" id="btnfileToUploadFile">上传</button>
								<input type="hidden" id="userBlogImgId" name="userBlogImgId"/>
								<input type="hidden" id="imageUuid" name="imageUuid" />
								<input type="hidden" id="imageUsage" name="imageUsage" /> 
								<input type="hidden" id="imagePath" name="imagePath" />
							</td>
						</tr>
						<tr><td  id="imgBlogTd" style="display:none">
							<img id="imgBlog" height="160px" width="362px"/>
						</td></tr>
						<tr>
							<td style="text-align:center">
							<input type="hidden" id="published"/>
							<input type="button" class="btn btnw85h25" name="btnSaveBack" id="btnSaveBack" value="保存并返回" />
							<input type="button" class="btn btnw85h25" name="btnSave" id="btnSave" value="保存" />
							<input type="button" class="btn btnw85h25" name="btnPublish" id="btnPublish" value="发布博文" />
							<input type="button" class="btn btnw85h25" name="btnDraft" id="btnDraft" value="保存草稿" /></td>
						</tr>
					</table>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
<script type="text/javascript" src="<%=resBaseUrl%>/lib/upload/js/jquery.fileupload.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/lib/upload/js/jquery.fileupload.ext.js"></script>
<script type="text/javascript">
var blog=extractUrlParams(location.href);
var orderId=blog.orderId;
var blogId=blog.id;
var editor =null;
var isChangeImgData = false;
var isGoListPage=false;
function fileToUploadFileIcon(fileId) {
		var fileUuidToUpdate = $id("imageUuid").val();
		if (isNoB(fileUuidToUpdate)) {//为空时，添加
			var formData = {
				usage : "image.blog"
			};
		} else {//否则修改
			var formData = {
				update : true,
				uuid : fileUuidToUpdate
			};
		}
		sendFileUpload(fileId, {
			url : getAppUrl("/file/upload"),
			dataType : "json",
			//自定义数据
			formData : formData,
			done : function(e, result) {
				var resultInfo = result.result;
				if (resultInfo.type == "info") {
					$id("imgBlogTd").show();
					var fileBrowseUrl=resultInfo.data.files[0].fileBrowseUrl;
					$id("imageUuid").val(resultInfo.data.files[0].fileUuid);
					$id("imageUsage").val(resultInfo.data.files[0].fileUsage);
					$id("imagePath").val(resultInfo.data.files[0].fileRelPath);
					$id("imgBlog").attr("src", fileBrowseUrl + "?" + new Date().getTime());
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

function ajaxPost(url){
	var postData={};
	postData.title=$id("title").val();
	if (isNoB(postData.title)) {
		Layer.error("博文标题不能为空");
		return false;
	}
	postData.content=$id("content").val();
	if (isNoB(postData.content)) {
		Layer.error("博文内容不能为空");
		return false;
	}
	postData.allowAnony=$('input:radio[name="allowAnony"]:checked').val();
	console.log("allowAnony:"+postData.allowAnony);
	postData.orderId=$id("orderId").val();
	if(!isUndef(orderId)){
		postData.orderId=orderId;
	}
	postData.published=$id("published").val();
	
	var ajax = Ajax.post(url);
	if(blogId){
		postData["id"]=blogId;
	}
	if(isChangeImgData){
		var imgData={};
		imgData["id"]=$id("userBlogImgId").val();
		imgData["imageUuid"]=$id("imageUuid").val();
		imgData["imageUsage"]=$id("imageUsage").val();
		imgData["imagePath"]=$id("imagePath").val();
		postData["userBlogImg"] = imgData;
	}
	ajax.data(postData);
	
	ajax.done(function(result, jqXhr) {
		if(result.type="info"){
			var callback = function() {
				if(isGoListPage){
					location.replace(getAppUrl("/ucenter/blog/list/jsp"));
				}
			};
			var closeDelay = 3000;
			Layer.msgSuccess(result.message, callback, closeDelay);
		}else{
			var theLayer = Layer.error(result.message, function(layerIndex) {
				theLayer.hide();
			});
		}
	});
	ajax.go();
}
	$(function() {
		if(blogId){
			$id("btnPublish").hide();
			$id("btnDraft").hide();
			$id("opText").text("编辑博文");
		}else{
			$id("btnSave").hide();
			$id("btnSaveBack").hide();
		}
		//保存按钮
		$id("btnSave").click(function(){
			ajaxPost("/social/user/blog/save/do");
		});
		$id("btnSaveBack").click(function(){
			isGoListPage=true;
			ajaxPost("/social/user/blog/save/do");
		});
		
		//发布
		$id("btnPublish").click(function(){
			isGoListPage=true;
			ajaxPost("/social/user/blog/publish/do");
		});
		
		//保存为草稿
		$id("btnDraft").click(function(){
			ajaxPost("/social/user/blog/draft/do");
		});
		initFileUpload("fileToUploadFileImg",null,"none");
		//绑定修改模块上传按钮
		$id("btnfileToUploadFile").click(function() {
			isChangeImgData=true;
			fileToUploadFileIcon("fileToUploadFileImg",editor);
		});
		
		if(blogId){
			var ajax = Ajax.post("/social/user/blog/get");
			ajax.data({id:blogId});
			//ajax.async(false);
			ajax.done(function(result, jqXhr) {
				var data = result.data;
				if (data == null) {
					return;
				}
				textSet("title", data.title);
				textSet("published", data.published.toString());
				if(data.published==false){
					$id("btnPublish").show();
					$id("opText").text("编辑草稿");
				}
				textSet("content", data.content);
				textSet("orderId",data.orderId);
				radioSet("allowAnony", data.allowAnony.toString());
				
				if(data.userBlogImg!=null){
					$id("imgBlogTd").show();
					textSet("userBlogImgId", data.userBlogImg.id);
					textSet("imageUuid", data.userBlogImg.imageUuid);
					textSet("imageUsage", data.userBlogImg.imageUsage);
					textSet("imagePath", data.userBlogImg.imagePath);
					radioSet("allowAnony", data.allowAnony.toString());
					$id("imgBlog").attr("src",data.userBlogImg.fileBrowseUrl+"?"+new Date().getTime());
				}
				
				if(data.saleOrder!=null){
					// 获取模板内容
					var tplHtml = $id("orderRowTpl").html();
					// 生成/编译模板
					var htmlTpl = laytpl(tplHtml);
					// 根据模板和数据生成最终内容
					var htmlText = htmlTpl.render(data.saleOrder);
					$id("orderThead").html(htmlText);
				}
			});
			ajax.go();
		}

		if(orderId){
			var ajax = Ajax.post("/saleOrder/detail/get");
			ajax.data({orderId:orderId});
			ajax.done(function(result, jqXhr) {
				var data = result.data;
				if (data == null) {
					return;
				}
				// 获取模板内容
				var tplHtml = $id("orderRowTpl").html();
				// 生成/编译模板
				var htmlTpl = laytpl(tplHtml);
				// 根据模板和数据生成最终内容
				var htmlText = htmlTpl.render(data);
				$id("orderThead").html(htmlText);
			});
			ajax.go();
		}
		$id("contentDiv").show();
	});
	
//-->
</script>
</body>
<script type="text/html" id="orderRowTpl">
{{# var saleOrder = d; }}
<tr>
<th>
<input type="hidden" name="orderId" value="{{saleOrder.id}}"/>
{{#if(saleOrder!=null){}}
<label  style="padding-left:10px">订单号：</label>
<label>{{saleOrder.no}}</label>
<label style="padding-left:20px">维修店：</label><label>{{saleOrder.shopName}}</label>
	<label style="padding-left:20px">服务项目：</label>
<label>
	{{# var saleOrderSvcs=saleOrder.saleOrderSvcs; }}
	{{# for(var i=0,len=saleOrderSvcs.length;i<saleOrderSvcs.length;i++){ }}
		{{saleOrderSvcs[i].svcName}}&nbsp;
	{{# } }}
</label>
{{#}}}
</th>

</tr>
</script>
</html>
