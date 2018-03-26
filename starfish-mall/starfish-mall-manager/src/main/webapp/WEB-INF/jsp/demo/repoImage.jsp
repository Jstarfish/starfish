<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>图片对话框 + CKEditor 示例</title>
</head>
<body id="rootPanel">
	<div id="topPanel" class="ui-layout-north" style="padding:4px;text-align: center">
		<div style="line-height: 80px;">图片对话框 + CKEditor 示例 </div>
	</div>
	<div id="mainPanel" class="ui-layout-center" style="padding:0px;">
		<textarea id="demo-editor" >
		</textarea>
	</div>

	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />

	<script type="text/javascript">
		//相对路径的解析基础
		CKEDITOR.config.handlers = CKEDITOR.config.handlers || {};
		//
		var theEditor = null;
		//调整控件大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			//console.log("mainWidth:" + mainWidth + ", " + "mainHeight:" + mainHeight);
			//
			var editorWidth = mainWidth - 2;
			var editorHeight = mainHeight - 2;
			theEditor.resize(editorWidth, editorHeight);
		}
	
		function toAdjustCtrlsSize() {
			setTimeout(adjustCtrlsSize, 1000);
		}
	
		//----------------------------------------------------------------------------
	
		//处理上传了的文件
		function handleUploadedFiles(imageDlg, fileInfoList) {
			console.log(fileInfoList);
		}
	
		//把选择的商品介绍图片插入到editor中
		function getAndInsertGoodsIntroImages(imageDlg) {
			var imageItems = imageDlg.getSelected() || [];
			if (isArray(imageItems)) {
				//多选结果
				for (var i = 0; i < imageItems.length; i++) {
					var imageItem = imageItems[i];
					console.log(imageItem);
					var imageHtmlTpl = '<img src="{0}" /><br />';
					theEditor.insertHtml(imageHtmlTpl.format(imageItem["fileBrowseUrl"]));
				}
			} else {
				//单选结果
				var imageItem = imageItems;
				console.log(imageItem);
				var imageHtmlTpl = '<img src="{0}" /><br />';
				theEditor.insertHtml(imageHtmlTpl.format(imageItem["fileBrowseUrl"]));
			}
		}
	
		//处理删除意图
		function toDeleteGoodsIntroImage(imageDlg, imageItem) {
			//如果想要删除，直接返回true，然后在删除操作结束时调用 imageDlg.refresh();
			console.log("您要删除的图片是：" + JSON.encode(imageItem));
			//

			//imageDlg.refresh();
	
			//
			return true;
		}
	
		//图片选择对话框
		var repoImageDlg = RepoImageDlg.newOne();
	
		//示例 - 对话框配置参数
		var sampleDlgConfig = {
			title : "选择商品介绍图片",
			//singleSelect : true,
			//width : 980,
			//height : 600,
			//imageWidth : 210,
			//imageHeight : 130,
			//图片获取
			fetchUrl : "/demo/goods/image/intro/get",
			fetchParams : {
				goodsId : 123
			},
			//图片上传
			uploadUrl : "/file/upload",
			uploadParams : {
				usage : "image.goods",
				subUsage : "intro",
				goodsId : 123
			},
			uploadCallback : handleUploadedFiles,
			//删除回调
			deleteHanlder : toDeleteGoodsIntroImage,
			//确定回调
			okClickHandler : getAndInsertGoodsIntroImages
		};
	
		//显示图片选择对话框
		function openRepoImageDlg(editor) {
			var dlgConfig = sampleDlgConfig;
			//
			repoImageDlg.show(dlgConfig);
		}
	
		//
		$(function() {
			//页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 110,
				allowTopResize : false
			});
			//
			theEditor = CKEDITOR.replace('demo-editor', {
				extraPlugins : 'imagex'
			});
			//插件事件回调函数
			CKEDITOR.config.handlers['imagex'] = openRepoImageDlg;
			//
			toAdjustCtrlsSize();
			//
			$(window).resize(toAdjustCtrlsSize);
		});
	</script>

</body>
</html>