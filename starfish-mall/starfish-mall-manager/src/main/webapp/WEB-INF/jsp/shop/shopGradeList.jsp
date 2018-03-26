<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>店铺评级设置</title>
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
		<div id="mainPanel" class="ui-layout-center" style="padding:4px;">
		    <div class="filter section">
				<button id="editShopGrade" class="normal button">修改</button>
			</div>
			<table id="theGridCtrl"></table>
		</div>
		<div id="dialog_ShopGradeEdit" title="修改店铺评级" style="display: none">
			<div id="member_grade_form" class="form">
				<div class="filter section">
					<div style="text-align: center; vertical-align: middle;">
						<table class="gridtable" style="width: 100%">
							<thead><tr>
							    <th width="3%"><label class="normal label">等级</label></th>
								<th width="3%"><label class="normal label">级别</label></th>
								<th width="10%"><label class="normal label">等级名称</label></th>
								<th width="10%"><label class="normal label">&ge;积分下限</label></th>
								<th width="10%"><label class="normal label">&lt;积分上限</label></th>
								<th width="20%"><label class="normal label">Icon</label></th>
								<th width="44%"><label class="normal label">更换图片</label></th>
							</tr></thead>
							<tbody id="grade_tbody"></tbody>
						</table>
					</div>
				</div>
			</div>
      </div>
        <jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
		<script type="text/javascript">
		   
		    //------------------------初始化变量-------------------------
		 
			//初始化新增siteModule dialog
			var dialog_ShopGradeEdit;
			var jqGridCtrl=null;
			var gridHelper = JqGridHelper.newOne("");
			var gradeList = null;
			//------------------------验证编辑店铺评分等级-------------------------
			var shopGradeFormProxy = FormProxy.newOne();//广告链接
			function addValidate(trCount){
				for(var i=0;i<trCount;i++){
					shopGradeFormProxy.addField({
						id : "shopGradeName"+i,
						required : true,
						rules : ["maxLength[30]"]
					});
					shopGradeFormProxy.addField({
						id : "shopGradeLowerPoint"+i,
						required : true,
						rules : ["isNatual",{
							rule : function(idOrName, type, rawValue, curData) {
								var count=idOrName.slice(19);
								 if(parseInt(count)==0){
									 var down0=parseInt($id("shopGradeLowerPoint0").val());
									 var up0=parseInt($id("shopGradeUpperPointss0").val());
									 if(down0>up0){
										 return false;
									 }
									 return true;
								 }
								var prev=parseInt(count)-1;
							    var  prevLowerPoint=parseInt($id("shopGradeLowerPoint"+prev).val());
								if(parseInt(rawValue)<parseInt(prevLowerPoint)){
									return false;
								}else{
								$id("shopGradeUpperPointss"+prev).val(parseInt($id("shopGradeLowerPoint"+count).val()));	
								$id("showshopGradeUpperPoint"+prev).html(parseInt($id("shopGradeLowerPoint"+count).val()));
								var  samUpperPoint=$id("shopGradeUpperPointss"+count).val();
								if(parseInt($id("shopGradeLowerPoint"+count).val())>parseInt(samUpperPoint)){
									$id("shopGradeUpperPointss"+count).val(parseInt($id("shopGradeLowerPoint"+count).val()));	
									$id("showshopGradeUpperPoint"+count).html(parseInt($id("shopGradeLowerPoint"+count).val()));	
								}
								for(var i=parseInt(count)+1;i<gradeList.length;i++){
									var down=$id("shopGradeLowerPoint"+i).val();
									if(parseInt($id("shopGradeLowerPoint"+count).val())>parseInt(down)){
										$id("shopGradeLowerPoint"+i).val(parseInt($id("shopGradeLowerPoint"+count).val()));	
									}
									var up=$id("shopGradeUpperPointss"+i).val();
									if(parseInt($id("shopGradeLowerPoint"+count).val())>parseInt(up)){
										$id("shopGradeUpperPointss"+i).val(parseInt($id("shopGradeLowerPoint"+count).val()));	
										$id("showshopGradeUpperPoint"+i).html(parseInt($id("shopGradeLowerPoint"+count).val()));	
									}
								}
								//return true;
								}
							    return true;
						},
						message : "下限不能小于上一级下限或者同级上限"
					}]
					});
				}
				
			}
			
			
			//------------------------调整控件大小-------------------------
			//调整控件大小
			function adjustCtrlsSize() {
				var jqMainPanel = $id("mainPanel");
				var mainWidth = jqMainPanel.width();
				var mainHeight = jqMainPanel.height();
				var gridCtrlId = "theGridCtrl";
				var jqGridBox = $("#gbox_" + gridCtrlId);
				var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
				var pagerHeight = $id("theGridPager").height();
				jqGridCtrl.setGridWidth(mainWidth - 1);
				jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight  - 40);
			}
			//------------------------初始化dialog-------------------------
			function openEditShopGrade(){
				 renderGradeDialog(gradeList);
				 dialog_ShopGradeEdit.dialog("open" );
			}
			
			function renderGradeDialog(gradeList){
				$id("grade_tbody").html("");
				var theTpl = laytpl( $id("shopGradeTpl").html());
				var htmlStr = theTpl.render(gradeList);
				$id("grade_tbody").append(htmlStr);
				for(var i=0;i<gradeList.length;i++){
					 initFileUpload("fileToUpload"+i,"");
				}
				addValidate(gradeList.length);
			}
			function initShopGradeDialog(){
				//为新增dialog_ShopGradeEdit进行赋值
				dialog_ShopGradeEdit = $( "#dialog_ShopGradeEdit" ).dialog({
					autoOpen : false,
					height : Math.min(550, $window.height()),
					width : Math.min(1200, $window.width()),
					modal : true,
					title : '编辑店铺等级',
			        buttons: {
			            "保存": editShopGrade,
			            "取消": function() {
			             dialog_ShopGradeEdit.dialog( "close" );
			          }
			        },
			        close: function() {
			        	jqGridCtrl.jqGrid("setGridParam").trigger("reloadGrid");
			        	shopGradeFormProxy.hideMessages();
			        }
			      });
			}
			//------------------------对商铺评分等级进行操作的方法-------------------------
			//弹出编辑商铺评分等级的对话框
			function shopGradeDialogEditOpen(obj){
				dialog_ShopGradeEdit.dialog( "open" );	
			}
			//更新商铺评分等级
			function editShopGrade(){
			  var vldResult = shopGradeFormProxy.validateAll();
				if (!vldResult) {
				return;
				}
				var postData = {
						shopGrades:getShopGradeList()
				};
			    var loaderLayer = Layer.loading("正在提交数据...");
				var ajax = Ajax.post("/shop/shopGrade/update/do");
				ajax.data(postData);
			    ajax.done(function(result, jqXhr){
			    	if(result.type== "info"){
			    		dialog_ShopGradeEdit.dialog( "close" );
			    		jqGridCtrl.jqGrid("setGridParam").trigger("reloadGrid"); 
						Layer.msgSuccess(result.message);
					}else{
						Layer.msgWarning(result.message);
					}
			  });
			   ajax.always(function() {
					//隐藏等待提示框
					loaderLayer.hide();
				});
			ajax.go(); 
		}
			
			
		//------------------------上传-------------------------
		//
		function fileToUploadFileIcon(fileId,count,fileUuid,picName) {
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
					for(var i=0+count;i<=count+4;i++){
 						$id("shopGradeIconPath"+i).val(resultInfo.data.files[0].fileRelPath);
					    $id("shopGradeIconUuid"+i).val(resultInfo.data.files[0].fileUuid);
					    $id("shopGradeIconUsage"+i).val(resultInfo.data.files[0].fileUsage);
						}
					$(picName).attr("src",resultInfo.data.files[0].fileBrowseUrl+"?"+new Date().getTime());
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
		function getShopGradeList(){
			var arrGrades = new Array();
			for(var i=0;i<gradeList.length+1;i++){
				if($id("tr"+i).length>0){
						var linkData={
								id:$id("shopGradeId"+i).val(),
								grade :$id("shopGradeGrade"+i).val(),
								level :$id("shopGradeLevel"+i).val(),
								name :$id("shopGradeName"+i).val(),
								lowerPoint :parseInt($id("shopGradeLowerPoint"+i).val()),
								upperPoint :parseInt($id("shopGradeUpperPointss"+i).val()),
								iconPath :$id("shopGradeIconPath"+i).val(),
								iconUuid :$id("shopGradeIconUuid"+i).val(),
								iconUsage :$id("shopGradeIconUsage"+i).val()
							};		
						arrGrades.add(linkData);
				}
			}
			return arrGrades;
		}
		
		function initGrade(){
			jqGridCtrl= $("#theGridCtrl").jqGrid({
			      url :getAppUrl("/shop/shopGrade/list/get") ,  
			      contentType : 'application/json',  
			      mtype : "post",  
			      datatype : 'json',
			      height : "100%",
				  width : "100%",
			      colNames : ["id","等级","级别","等级名称", "&ge;积分下限","&lt;积分上限","显示图片"],  
			      colModel : [{name:"id", index:"id", hidden:true},  
			                  {name:"grade", index:"grade",width:40,align : 'center',cellattr: function(rowId, tv, rawObject, cm, rdata) {
		                            //合并单元格
		                            return 'id=\'grade' + rowId + "\'";
		                        }},
		                      {name:"level", index:"level",width:40,align : 'center'}, 
			                  {name:"name", index:"name",width:100,align : 'center' ,cellattr: function(rowId, tv, rawObject, cm, rdata) {
		                            //合并单元格
		                            return 'id=\'name' + rowId + "\'";
		                        }}, 
			                  {name:"lowerPoint", index:"lowerPoint",width:100,align : 'right',}, 
			                  {name:"upperPoint", index:"upperPoint",width:100,align : 'right',},  
			                  {name:'fileBrowseUrl',index:'fileBrowseUrl', align : 'center',formatter : function (cellValue,option,rowObject) {
			                	  if(cellValue!=null&&cellValue!=""){
			                		  var picString="";
			                		 for (var i=1;i< parseInt(rowObject.level)+1;i++){
			                			 var picStr= cellValue+"?"+new Date().getTime() 
			                			 picString=picString+"<img src='"+picStr+"' width='10px' height='15px' />" 
			                		 }
			                		 
				                	  return picString;  
			                	  }else{
			                		  return "";    
			                	  }
								},
							  width:120,align:"left"},
			                  ],
			                  rowNum:'120',
	 			              multiselect:false, //定义是否可以多选
	 			              height:"auto",
	 			             loadComplete : function(gridData){
	 							//gridHelper.cacheData(gridData);
	 							gradeList = gridData["rows"];
	 							renderGradeDialog(gradeList);
	 						},
							gridComplete: function() {
				                //②在gridComplete调用合并方法
				                var gridName = "theGridCtrl";
				                Merger(gridName, 'name');
				                Merger(gridName, 'grade')
				            }
			});
		}
		//公共调用方法
        function Merger(gridName, CellName) {
            //得到显示到界面的id集合
            var mya = $("#" + gridName + "").getDataIDs();
            //当前显示多少条
            var length = mya.length;
            for (var i = 0; i < length; i++) {
                //从上到下获取一条信息
                var before = $("#" + gridName + "").jqGrid('getRowData', mya[i]);
                //定义合并行数
                var rowSpanTaxCount = 1;
                for (j = i + 1; j <= length; j++) {
                    //和上边的信息对比 如果值一样就合并行数+1 然后设置rowspan 让当前单元格隐藏
                    var end = $("#" + gridName + "").jqGrid('getRowData', mya[j]);
                    if (before[CellName] == end[CellName]) {
                        rowSpanTaxCount++;
                        $("#" + gridName + "").setCell(mya[j], CellName, '', { display: 'none' });
                    } else {
                        rowSpanTaxCount = 1;
                        break;
                    }
                    $("#" + CellName + "" + mya[i] + "").attr("rowspan", rowSpanTaxCount);
                }
            }
        }
		//------------------------初始化 start-------------------------
		$(function() {
			initShopGradeDialog();
			//页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 60,
				allowTopResize : false
			});
			//隐藏布局north分割线
			$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
			initGrade();
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);
			//绑定上传按钮
			$id("editShopGrade").click(openEditShopGrade)
		});
		</script>
	</body>
<script type="text/html" id="shopGradeTpl" title="店铺等级信息模版">
	{{# for(var i = 0, len = d.length; i < len; i++){ }}
		<tr id="tr{{ i }}">
               {{# if(i%5==0) { }}
					<td  rowspan=5>
                       <label  class="normal label">{{ d[i].grade }}</label>
                     </td>
				{{# } }}
            <td>
                 <label  class="normal label">{{ d[i].level }}</label>
            </td>
			<td>
                <input type="hidden" id="shopGradeId{{ i }}" value="{{ d[i].id }}"/>
                <input type="hidden" id="shopGradeGrade{{ i }}" value="{{ d[i].grade }}"/>
                <input type="hidden" id="shopGradeLevel{{ i }}" value="{{ d[i].level }}"/>
                <input type="hidden" id="shopGradeUpperPointss{{ i }}" value="{{ d[i].upperPoint }}"/>
                <input type="hidden" id="shopGradeIconPath{{ i }}" value="{{ d[i].iconPath }}"/>
                <input type="hidden" id="shopGradeIconUuid{{ i }}" value="{{ d[i].iconUuid }}"/>
                <input type="hidden" id="shopGradeIconUsage{{ i }}" value="{{ d[i].iconUsage }}"/>
                <input data-role="shopGradeName" id="shopGradeName{{ i }}"  value="{{ d[i].name }}" style="width: 93%;" class="normal input" />
            </td>
            <td>
                 <input id="shopGradeLowerPoint{{ i }}" value="{{ d[i].lowerPoint }}" style="width: 93%;" class="normal input" />
             </td>
            <td>
                <label id="showshopGradeUpperPoint{{ i }}" class="normal label">{{ d[i].upperPoint }}</label>
            </td>
            <td>
           {{# if( d[i].fileBrowseUrl==null) { }}
              {{#  d[i].fileBrowseUrl ="";}}
           {{# } }}
             {{# for (var j = 0; j <i%5; j++) { }}
                <img name="shopGradeFileBrowseUrl{{ d[i].grade }}"  src="{{ d[i].fileBrowseUrl }}" width="10px" height="14px"/>
             {{# } }}
                <img name="shopGradeFileBrowseUrl{{ d[i].grade }}"  src="{{ d[i].fileBrowseUrl }}" width="10px" height="14px"/>
            </td>
         {{# if(i%5==0) { }}
            <td rowspan=5 align="left">
                 <input name="file" type="file" id="fileToUpload{{ i }}" multiple="multiple" class="field value "  /></br></br></br>
                  {{# if( d[i].fileBrowseUrl==""||d[i].fileBrowseUrl==null) { }}
                     <button class="normal button" onclick="fileToUploadFileIcon('fileToUpload{{ i }}',{{ i }},'{{ d[i].iconUuid }}',shopGradeFileBrowseUrl{{ d[i].grade }})">上传</button>
                  {{# }else{ }}
                     <button class="normal button" onclick="fileToUploadFileIcon('fileToUpload{{ i }}',{{ i }},'{{ d[i].iconUuid }}',shopGradeFileBrowseUrl{{ d[i].grade }})">更换图片</button>
			    {{# } }}
            </td>
         {{# } }}
		</tr>
	{{# } }}
		
</script>
</html>