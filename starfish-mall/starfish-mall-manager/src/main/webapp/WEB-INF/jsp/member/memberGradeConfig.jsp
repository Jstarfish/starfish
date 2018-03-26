<%@page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>会员等级设置</title>
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
	height: 30px;
}

table.gridtable td {
	border:1px solid #AAA;
	border-width: 1px;
	padding: 3px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
	height: 30px;
}

</style>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div id="member_grid">
			<div class="filter section">
				<button id="editBtn" class="normal button">修改</button>
			</div>
			<table id="member_list"></table>
			<div id="member_pager"></div>
		</div>
	</div>

	<div id="member_grade_dialog" style="display:none;">
		<div id="member_grade_form" class="form">
			<div class="filter section">
				<div style="text-align: center; vertical-align: middle;">
					<table class="gridtable">
						<thead><tr>
							<th width="8%"><label class="normal label">等级</label></th>
							<th width="20%"><label class="normal label">等级名称</label></th>
							<th width="15%"><label class="normal label">&ge; 积分下限</label></th>
							<th width="15%"><label class="normal label">&lt; 积分上限</label></th>
							<th width="12%"><label class="normal label">Icon</label></th>
							<th width="30%"><label class="normal label">上传icon</label></th>
						</tr></thead>
						<tbody id="grade_tbody"></tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
	var member_grade_dialog;
	//实例化表单代理
	var formProxy = FormProxy.newOne();
	
	var isExecute = false;
	
	$(function() {
		//页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 60,
			allowTopResize : false
		});
		//
		initDialog();
		//加载短信服务商信息
		loadData();
		//调整控件大小
		winSizeMonitor.start(adjustCtrlsSize);
	});
	
	//-------------------------------------------------调整控件大小------------------------------------------------------------
	//调整控件大小
	function adjustCtrlsSize() {
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		var gridCtrlId = "member_list";
		var jqGridBox = $("#gbox_" + gridCtrlId);
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
		var pagerHeight = $id("member_pager").height();
		jqGridCtrl.setGridWidth(mainWidth - 1);
		jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 40);
	}
	
	//-------------------------------------------------功能实现 --------------------------------------------------------------
	
	//初始化文件上传控件
	function initFileUpload(fileCtrl, domIdToShowFiles, maxWidth) {
		var theCtrlId = $id(fileCtrl).attr("id");
		if (isNoE(theCtrlId)) {
			theCtrlId = genUniqueStr();
			$id(fileCtrl).attr("id", theCtrlId);
		}
		var wrapperId = "wrapper-" + theCtrlId;
		var jqWrapper = $id(wrapperId);
		if (jqWrapper.length == 0) {
			jqWrapper = $("<div id='" + wrapperId + "' style='display:inline-block; text-align:left; position: absolute;'><button class='normal button'>选择文件</button><label style='padding-left:4px;color:navy;'></label></div>").insertAfter($id(theCtrlId));
			jqWrapper.append($id(theCtrlId));
			$id(theCtrlId).css("padding", 0);
			$id(theCtrlId).css("border", 0);
			$id(theCtrlId).width(0);
			$id(theCtrlId).height(0);
			jqWrapper.find('button').click(function(event) {
				$id(theCtrlId).trigger("click");
			});
		}
		if (!maxWidth) {
			maxWidth = 280;
		}
		jqWrapper.css("width", maxWidth);
		var initConfig = {
			limitConcurrentUploads : 1,
			autoUpload : false,
			replaceFileInput : false,
			change : function(e, data) {
				// 缓存当前选择的文件
				var files = data.files;
				var fileCount = files.length;
				$id(theCtrlId).prop("curFiles", files);
				//
				console && console.log("文件数 : " + fileCount);
				var fileName = "";
				$.each(files, function(index, file) {
					if (index == 0) {
						fileName = file.name;
					}
					console && console.log('文件名 : ' + file.name);
				});
				if (fileCount > 1) {
					fileName = fileCount + "个文件";
				}
				jqWrapper.find('label').text(fileName);
			}
		};
		//
		$id(theCtrlId).fileupload(initConfig);
	}
	//------------------------上传-------------------------
	//
	function fileToUploadFileIcon(fileId,grade,fileUuid,picName) {
		if(isNoB(fileUuid)||fileUuid=="null"){
			var formData = {
				usage : "image.icon",
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
			formData :formData ,
			done : function(e, result) {
				var resultInfo = result.result;
				if(resultInfo.type=="info"){
					$id("iconPath"+grade).val(resultInfo.data.files[0].fileRelPath);
				    $id("iconUuid"+grade).val(resultInfo.data.files[0].fileUuid);
				    $id("iconUsage"+grade).val(resultInfo.data.files[0].fileUsage);
					$(picName).attr("src",resultInfo.data.files[0].fileBrowseUrl+"?"+new Date().getTime());
					$(picName).css("display", "block");
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
				return true;
			}
		});
	}
	//
	function renderGradeDialog(gradeList){
		$id("grade_tbody").html("");
		var theTpl = laytpl( $id("gradeTpl").html());
		var htmlStr = theTpl.render(gradeList);
		$id("grade_tbody").append(htmlStr);
		
		$("input[data-role='lowerPoint']").each(function(){
			$(this).bind("change", function () {
				var dataForId = $(this).attr("data-for");
				$id(dataForId).attr("real-data", ParseInt($(this).val()));
				$id(dataForId).html(ParseInt($(this).val()));
		    })
		});
		//将等级名称注入到表单
		var gradeNames = $("input[data-role='gradeName']");
		for(var gi=0; gi<gradeNames.length; gi++){
			//移除之前的绑定
			formProxy.removeField("gradeName_"+gi);
			//
			formProxy.addField({
				id : "gradeName_"+gi,
				required : true,
				messageTargetId : "gradeName_"+gi
			});
		}
		
		//将积分下限集合注入到表单中
		var lowerPoints = $("input[data-role='lowerPoint']");
		for(var i=0; i<lowerPoints.length; i++){
			//移除之前的绑定
			formProxy.removeField("lowerPoint_"+i);
			//
			formProxy.addField({
				id : "lowerPoint_"+i,
				required : true,
				rules : ["isNatual",{
					rule : function(idOrName, type, rawValue, curData) {
						var index = idOrName.substring(idOrName.indexOf("_")+1, idOrName.length);
						index = ParseInt(index);
						var currentLowerPoint = ParseInt(rawValue);
						if(index == 0){
							var nextIndex = index + 1;
							var nextLowerPoint = $id("lowerPoint_"+nextIndex).val();
							if(!isNoB(nextLowerPoint)){
								nextLowerPoint = ParseInt(nextLowerPoint);
								if(nextLowerPoint < currentLowerPoint){
									return false;
								}
							}
						}
						var prevIndex = index-1;
						var prevLowerPoint = $id("lowerPoint_"+prevIndex).val();
						prevLowerPoint = ParseInt(prevLowerPoint);
						if(currentLowerPoint < prevLowerPoint){
							return false;
						}
						return true;
					},
					message : "下限不能小于上一级下限"
				}],
				messageTargetId : "lowerPoint_"+i
			});
		}
		//初始化上传icon组件
		for(var i=0; i<gradeList.length; i++){
			initFileUpload("fileToUpload"+i,"");
		}
	}
	
	function vldGradeDialog(){
		var result = formProxy.validateAll();
		return result;
	}
	
	function getGradeList(){
		var dataList =[];
		$("#grade_tbody > tr").each(function(){
			var tmpMap = new KeyMap("gradeMap");
			$(this).find("td").each(function(){
				if($(this).attr("data-for") == 'maxUpperPoint'){
					tmpMap.add("upperPoint", null);
				}else if($(this).attr("data-for") == 'upperPoint'){
					var upperPoint = $(this).find("label[data-role='upperPoint']").attr("real-data");
					tmpMap.add("upperPoint", parseInt(upperPoint));
				}else if($(this).attr("data-for") == 'grade'){
					var id = $(this).find("input[data-role='gradeId']").val()
					tmpMap.add("id", parseInt(id));
					//
					var grade = $(this).find("label[data-role='grade']").html()
					tmpMap.add("grade", parseInt(grade));
				}else if($(this).attr("data-for") == 'gradeName'){
					tmpMap.add("name", $(this).find("input[data-role='gradeName']").val());
				}else if($(this).attr("data-for") == 'lowerPoint'){
					var lowerPoint = $(this).find("input[data-role='lowerPoint']").val();
					tmpMap.add("lowerPoint", parseInt(lowerPoint));
				}else if($(this).attr("data-for") == 'gradeIcon'){
					var iconPath = $(this).find("input[data-role='iconPath']").val();
					var iconUuid = $(this).find("input[data-role='iconUuid']").val();
					var iconUsage = $(this).find("input[data-role='iconUsage']").val();
					tmpMap.add("iconPath", iconPath);
					tmpMap.add("iconUuid", iconUuid);
					tmpMap.add("iconUsage", iconUsage);
				}
				
			});
			dataList.add(tmpMap);
		});
		return dataList;
	}
	
	function editGrade(){
		if(!isExecute) return;
		
		var vldResult = vldGradeDialog();
		
		if(vldResult){
			var hintBox = Layer.progress("正在保存数据...");

			var dataList = getGradeList();
			var ajax = Ajax.post("/member/grade/update/do");
			ajax.data(dataList);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					jqGridCtrl.jqGrid().trigger("reloadGrid");
				}else {
					Layer.msgWarning(result.message);
				}

			});
			ajax.always(function() {
				hintBox.hide();
			});
			
			ajax.go();
		}
		
	}
	
	function initDialog(){
		member_grade_dialog = $("#member_grade_dialog").dialog({
			autoOpen : false,
			height : Math.min(450, $window.height()),
			width : $window.width()-10,
			modal : true,
			title : '编辑会员等级',
			buttons : {
				"保存" : editGrade,
				"取消" : function() {
					member_grade_dialog.dialog("close");
				}
			},
			close : function() {
				formProxy.hideMessages();

			}
		});
		
	}
	//jqGrid缓存变量
	var jqGridCtrl = null;
	//
	var gridHelper = JqGridHelper.newOne("");
	
	var gradeList = null;
	//
	function loadData(){
		jqGridCtrl= $("#member_list").jqGrid({
		      url : getAppUrl("/member/grade/list/get"),  
		      contentType : 'application/json',  
		      mtype : "post",  
		      datatype : 'json',
			  colNames : [ "id","会员等级", "会员等级名称", "&ge;积分下限", "&lt;积分上限", "icon"],
		      colModel : [{name:"id", index:"id", hidden:true}, 
		                  {name:"grade", width:50, align:'left'}, 
		                  {name:"name", width:150, align:'left',}, 
		                  {name:"lowerPoint", width:100, align:'right',},
		                  {name:"upperPoint", width:100, align:'right',formatter : function (cellValue) {
		                	    var showUpperPoint = null;
		                	  	if(cellValue == null){
		                	  		showUpperPoint = '———';
		                	  	}else{
		                	  		showUpperPoint = cellValue;
		                	  	}
								return showUpperPoint;
							}},
							{name:'fileBrowseUrl',index:'fileBrowseUrl', align : 'center',formatter : function (cellValue,option,rowObject) {
								 var url = cellValue || "";
			                	  if(url){
			                		  url = "<img src='"+url+"' width='10px' height='15px' />";
			                	  }
			                	  return url;
							},width:120,align:"left"}
		                 ],  
			//rowList:[10,20,30],
			//multiselect:true,
			//pager : "#member_pager",
			loadComplete : function(gridData){
				//gridHelper.cacheData(gridData);
				gradeList = gridData["rows"];
				//
				$id("editBtn").click(function(){
					isExecute = true;
					renderGradeDialog(gradeList);
					member_grade_dialog.dialog("open");
				});
			}
		});

	}
	//
	
	</script>
</body>
<script type="text/html" id="gradeTpl" title="会员等级信息模版">
	{{# for(var i = 0, len = d.length; i < len; i++){ }}
	{{# var showUpperPoint = ParseInt(d[i].upperPoint); }}
		<tr>
			<td data-for="grade"><input type="hidden" data-role="gradeId" value="{{ d[i].id }}"/><label data-role="grade" class="normal label">{{ d[i].grade }}</label></td>
			<td data-for="gradeName"><input data-role="gradeName" id="gradeName_{{i}}" value="{{ d[i].name }}" style="width: 93%;" class="normal input" /></td>
			<td data-for="lowerPoint">
				{{# if(i==0){ }}
					<input data-for="" data-role="lowerPoint" id="lowerPoint_{{ d[i].lowerPoint }}" style="width: 93%;" class="normal input right align" value='{{ d[i].lowerPoint }}' />
				{{# }else{ }}
					<input data-for="upperPoint_{{ d[i-1].upperPoint }}" id="lowerPoint_{{i}}" data-role="lowerPoint" style="width: 93%;" class="normal input right align" value='{{ d[i].lowerPoint }}' />
				{{# } }}
			</td>
			{{# if(d[i].upperPoint == null) { }}
				<td data-for="maxUpperPoint" align="right"><label style="width: 93%;" class="normal label" >———</label></td>
			{{# }else{ }}
				{{# if(i+1 <= len) { }}
					<td data-for="upperPoint" align="right">
						<label data-role="upperPoint" style="width: 93%;" id="upperPoint_{{ d[i].upperPoint }}" real-data="{{ d[i].upperPoint }}" class="normal label">{{ showUpperPoint }}</label>
					</td>
				{{# } }}
			{{# } }}
			<td data-for="gradeIcon">
				<input type="hidden" data-role="iconPath" id="iconPath{{ d[i].grade }}" value="{{ d[i].iconPath }}"/>
                <input type="hidden" data-role="iconUuid" id="iconUuid{{ d[i].grade }}" value="{{ d[i].iconUuid }}"/>
                <input type="hidden" data-role="iconUsage" id="iconUsage{{ d[i].grade }}" value="{{ d[i].iconUsage }}"/>
				<img name="gradeIcon{{ d[i].grade }}"
				{{# if(d[i].fileBrowseUrl) { }}
				 style="display: block;"
				{{# }else{ }}
				style="display: none;"
				{{# }}}
				src="{{ d[i].fileBrowseUrl || ''}}" width="10px" height="14px"/>
			</td>
			<td style="text-align:left;">
				<input name="file" type="file" id="fileToUpload{{ i }}" class="field value"/>
                  {{# if( d[i].fileBrowseUrl==""||d[i].fileBrowseUrl==null) { }}
                     <button class="normal button" style="margin-top:3px; float:right; position: relative;" onclick="fileToUploadFileIcon('fileToUpload{{ i }}',{{ d[i].grade }},'{{ d[i].iconUuid }}',gradeIcon{{ d[i].grade }})">上传</button>
                  {{# }else{ }}
                     <button class="normal button" style="margin-top:3px; float:right; position: relative;" onclick="fileToUploadFileIcon('fileToUpload{{ i }}',{{ d[i].grade }},'{{ d[i].iconUuid }}',gradeIcon{{ d[i].grade }})">更换图片</button>
			    {{# } }}
			</td>
		</tr>
	{{# } }}
		
</script>
</html>
