<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>文件上传、下载、操作 - demo</title>
</head>
<body>
	<table width="100%" border="1">
		<tr style="background-color: #EEE; height: 30px;">
			<td width="20%">说明</td>
			<td width="60%">示例</td>
			<td width="20%">&nbsp;</td>
		</tr>
		<tr style="height: 30px;">
			<td>无接收者示例</td>
			<td>
				<div class="form">
					<div class="field row">
						<label class="field label">文件</label> <input
							class="hidden file" name="file" type="file"
							id="fileToUpload" multiple="multiple" />
					</div>
				</div>
			</td>
			<td class="align center"><button class="normal button" onclick="fileToUpload();">上传</button></td>
		</tr>
		<tr style="height: 30px;">
			<td>上传提取内容示例</td>
			<td>
				<div class="form">
					<div class="field row">
						<label class="field label">文件</label> <input
							class="hidden file" name="file" type="file"
							id="fileToUploadExtract" />
					</div>
				</div>
			</td>
			<td class="align center"><button class="normal button" onclick="fileToUploadExtract();">上传</button></td>
		</tr>
		<tr style="height: 30px;">
			<td>上传文件(file.misc)保存示例</td>
			<td>
				<div class="form">
					<div class="field row">
						<label class="field label">文件</label> <input
							class="hidden file" name="file" type="file"
							id="fileToUploadFileMisc"  multiple="multiple"/>
					</div>
					<div id="fileInfoListHolder" style="border:1px solid yellow;"></div>
				</div>
			</td>
			<td class="align center"><button class="normal button" onclick="fileToUploadFileMisc();">上传</button></td>
		</tr>
		<tr style="height: 30px;">
			<td>上传文件(file.misc)更新示例</td>
			<td>
				<div class="form">
					<div class="field row">
						<label class="field label">文件</label> <input
							class="hidden file" name="file" type="file"
							id="fileToUpdateFileMisc" />						
					</div>
					<div class="field row">
						<label class="field label">uuid</label> <input
							class="field value three wide" id="fileUuidToUpdate" />
							<span class="normal spacer">
							<button class="normal button" onclick="downloadFileMisc()">下载</button></span>					
					</div>
				</div>
			</td>
			<td class="align center"><button class="normal button" onclick="fileToUpdateFileMisc();">上传更新</button></td>
		</tr>
	</table>
	<br/>
	<img id="demoImage" src="/back/repo/file/misc/demo/x.jpg" width=400 height=300 />

	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />

	<script type="text/javascript">
		//
		function fileToUpload() {
			sendFileUpload("fileToUpload", {
				url : getAppUrl("/file/upload"),
				dataType : "json",
				//自定义数据
				formData : {
					param1 : "pv1",
					param2 : "pv2"
				},
				done : function(e, result) {
					//done方法就是上传完毕的回调函数，其他回调函数可以自行查看api  
					//注意result要和jquery的ajax的data参数区分，这个对象包含了整个请求信息  
					//返回的数据在result.result中，假设我们服务器返回了一个json对象 
					var resultInfo = result.result;
					console.log(resultInfo);
					//
					var fileInfoList = getFileInfoList(resultInfo);
					for (var i = 0; i < fileInfoList.length; i++) {
						var fileInfo = fileInfoList[i];
						console.log(fileInfo);
					}
					//
					if(resultInfo.type=="info"){
						Layer.msgInfo(resultInfo.message);
					}
					else {
						Layer.msgWarning(resultInfo.message);
					}
				},
				fail : function(e, data) {
					console.log(data);
				}
			});
		}
		//
		function fileToUploadExtract() {
			sendFileUpload("fileToUploadExtract", {
				url : getAppUrl("/file/upload"),
				dataType : "json",
				//自定义数据
				formData : {
					usage : "member.extract"
				},
				done : function(e, result) {
					//done方法就是上传完毕的回调函数，其他回调函数可以自行查看api  
					//注意result要和jquery的ajax的data参数区分，这个对象包含了整个请求信息  
					//返回的数据在result.result中，假设我们服务器返回了一个json对象 
					var resultInfo = result.result;
					console.log(resultInfo);
					var fileInfoList = getFileInfoList(resultInfo);
					var fileInfo = fileInfoList[0];
					if(fileInfo.type=="info"){
						alert("文件内容提取结果：\n"+JSON.encode(fileInfo.extra));
					}
					else {
						Layer.msgWarning(fileInfo.message);
					}
				},
				fail : function(e, data) {
					console.log(data);
				},
				fileNamesChecker : function(fileNames) {
					fileNames = fileNames || [];
					var fileName = fileNames[0];
					if (!fileName.toLowerCase().endsWith(".xml")) {
						Layer.msgWarning("只能上传xml文件");
						return false;
					}
					return true;
				}
			});
		}
		//
		function fileToUploadFileMisc() {
			sendFileUpload("fileToUploadFileMisc", {
				url : getAppUrl("/file/upload"),
				dataType : "json",
				//自定义数据
				formData : {
					usage : "file.misc",
					subUsage : "demo"
				},
				done : function(e, result) {
					//done方法就是上传完毕的回调函数，其他回调函数可以自行查看api  
					//注意result要和jquery的ajax的data参数区分，这个对象包含了整个请求信息  
					//返回的数据在result.result中，假设我们服务器返回了一个json对象 
					var resultInfo = result.result;
					//后端返回的信息
					console && console.log(resultInfo);
					
					//单独提取文件信息列表
					var fileInfoList = getFileInfoList(resultInfo);
					var firstFileUuid = null;
					for (var i = 0; i < fileInfoList.length; i++) {
						var fileInfo = fileInfoList[i];
						if (fileInfo.type == "info") {
							if (firstFileUuid == null) {
								firstFileUuid = fileInfo.fileUuid;
							}
							if (isImageFile(fileInfo.fileName)) {
								$id("demoImage").attr("src", fileInfo.fileBrowseUrl);
							}
						}
					}
					//
					$id("fileUuidToUpdate").val(firstFileUuid);
					
					//直接显示文件上传结果信息
					showFileInfoList("fileInfoListHolder", resultInfo);
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
		//
		function fileToUpdateFileMisc() {
			var fileUuidToUpdate = $id("fileUuidToUpdate").val();
			if(isNoB(fileUuidToUpdate)){
				Layer.msgWarning("请指定要更新的资源文件的uuid");
				return;
			}
			sendFileUpload("fileToUpdateFileMisc", {
				url : getAppUrl("/file/upload"),
				dataType : "json",
				//自定义数据
				formData : {
					update : true,
					uuid : fileUuidToUpdate
				},
				done : function(e, result) {
					//done方法就是上传完毕的回调函数，其他回调函数可以自行查看api  
					//注意result要和jquery的ajax的data参数区分，这个对象包含了整个请求信息  
					//返回的数据在result.result中，假设我们服务器返回了一个json对象 
					var resultInfo = result.result;
					console.log(resultInfo);
					//
					var fileInfoList = getFileInfoList(resultInfo);
					for (var i = 0; i < fileInfoList.length; i++) {
						var fileInfo = fileInfoList[i];
						console.log(fileInfo);
					}
					//
					if(resultInfo.type=="info"){
						Layer.msgInfo(resultInfo.message);
					}
					else {
						Layer.msgWarning(resultInfo.message);
					}
				},
				fail : function(e, data) {
					console.log(data);
				}
			});
		}
		
		//
		function downloadFileMisc() {
			var fileUuidToDownload = $id("fileUuidToUpdate").val();
			if(isNoB(fileUuidToDownload)){
				Layer.msgWarning("请指定要下载的资源文件的uuid");
				return;
			}
			var downloadUrl = getAppUrl("/file/download");
			downloadFile(downloadUrl, {
				uuid : fileUuidToDownload
			});
		}
		//
		$(function() {
			initFileUpload("fileToUpload");
			initFileUpload("fileToUploadExtract");
			initFileUpload("fileToUploadFileMisc");
			initFileUpload("fileToUpdateFileMisc");
		});
	</script>

</body>
</html>