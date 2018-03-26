<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	<title>客服列表</title>
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
			background-color: #EFEFEF;
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
			text-align: center;
		}
	</style>
</head>

<body id="rootPanel">
<!-- 查询条件 -->
<div class="ui-layout-north" style="padding: 4px;" id="topPanel">
	<div class="filter section">
		<div class="filter row">
			<div class="group left aligned">
				<button class="normal button" id="btnAdd">添加</button>
				<span class="normal spacer"></span>
				<button class="normal button" id="btnBatchDel">批量删除</button>
			</div>
			<div class="group right aligned">
				<label class="normal label">人员名称</label>
				<input class="normal input" id="queryUserName">
				<span class="normal spacer"></span>
				<label class="normal label">客服编号</label>
				<input class="normal input" id="queryServantNo">
				<span class="normal spacer"></span>
				<label class="normal label">客服名称</label>
				<input class="normal input" id="queryServantName">
				<span class="normal spacer"></span>
				<button class="normal button" id="btnQuery" >查询</button>
			</div>
		</div>
	</div>
	<div id="viewOlsDialog"></div>
</div>

<!-- grid数据及分页 -->
<div class="ui-layout-center noBorder" style="padding: 4px;" id="mainPanel">
	<table id="olsList"></table>
	<div id="olsPager"></div>
</div>

<!--添加客服人员dialog -->
	<div id="addOlsDialog" style="display: none;height: 600px;">
		<div class="ui-layout-center">
			<div class="filter section">
				<div class="filter row">
					<div style="float:right;" class="group right align">
						<label class="label">员工名称</label> 
						<input id="queryUser" class="normal input"/> 
						<span class="spacer"></span>
						<button id="btnQueryUser" class="button">查询</button>
					</div>
				</div>
			</div>
			<table id="mallStaffGrid"></table>
			<div id="mallStaffPager"></div>
		</div>
    </div>

<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
	//------------------------初始化变量-------------------------
	var jqGridCtrl;
	//缓存当前jqGrid数据行数组
	var gridHelper = JqGridHelper.newOne("");
	 //
    var staffGridHelper = JqGridHelper.newOne("");
	var olsDialog = null;
	//客服代理
	var mallOlsFormProxy = FormProxy.newOne();
	//
    var addOlsDialog = null;
	//临时值
	var tempMallOls;
    var olsGridCtrl;
	var servantId;
	var method = "view";
	var formProxy = FormProxy.newOne();
	var viewOlsDialog;

	//------------------------初始化方法-------------------------
	$(function() {
		//页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 56,
			allowTopResize : false
		});
		//隐藏布局north分割线
		$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
		// 加载客服列表数据
		loadGridData();
		//初始化添加客服Dialog
		initAddOlsDialog();
		//初始化编辑，查看模板
		initMallOlsTpl();
		
		//绑定保存按钮事件
		$id("btnQuery").click(queryData);
		$id("btnQueryUser").click(queryUserData);
		$id("btnAdd").click(olsDialogAdd);
		$id("btnBatchDel").click(delmallOlsByIds);
		
		//tab切换事件
		$id("theTabsCtrl").on("tabsactivate", function(event, ui) {
			// 隐藏店铺设置页面所有错误提示
			hideAllMessages();
			// 绑定保存操作
			currentTab = ui.newTab.context.id;
		});
		
		// 页面自适应
		winSizeMonitor.start(adjustCtrlsSize);
	});
	
	//空函数，在弹出框消失后重写调用
	function getCallbackAfterGridLoaded() {
	}
	
	//初始化添加客服Dialog
	function initAddOlsDialog(){
		addOlsDialog = $( "#addOlsDialog" ).dialog({
	        autoOpen: false,
	        width : Math.min(900, $window.width()),
	        height : Math.min(300, $window.height()),
	        modal: true,
	        title : '添加客服',
	        buttons: {
	        	"保存" : defaultMethod,
	            "关闭": function() {
	            	clearDialog();
	            	addOlsDialog.dialog( "close" );
	          }
	        },
	        close: function() {
	        	addOlsDialog.dialog( "close" );
	        }
	      });
	}
	
	//添加客服
	function olsDialogAdd(){
		method = "add";
		clearaddOlsDialog();
		loadMallStaffData();
		addOlsDialog.dialog("open");
	}
	
	//初始化
	function clearaddOlsDialog(){
		$id("servantNo").val("");
		$id("servantName").val("");
		$id("userName").val("");
	}
	
	//加载客服列表
	function loadGridData() {
		clearaddOlsDialog();
		var filter = {};
		var servantNo = textGet("queryServantNo").trim();
		if(servantNo){
			filter.servantNo = servantNo;
		}
		var servantName = textGet("queryServantName").trim();
		if(servantName){
			filter.servantName = servantName;
		}
		var nickName = textGet("queryUserName").trim();
		if(nickName){
			filter.nickName = nickName;
		}
		jqGridCtrl = $("#olsList").jqGrid({
			height : "100%",
			width : "100%",
			url : getAppUrl("/interact/servant/list/get"),
			postData:{filterStr : JSON.encode(filter,true)},
			contentType : 'application/json',
			mtype : "post",
			datatype : 'json',
			colNames : [ "序号", "客服编号", "客服名称", "人员名称", "手机", "操作" ],
			colModel : [
					{
						name : "id",
						index : "id",
						hidden : true
					},
					{
						name : "servantNo",
						width : 200,
						align : "center"
					},
					{
						name : "servantName",
						width : 200,
						align : "center"
					},
					{
						name : "nickName",
						width : 200,
						align : "center"
					},
					{
						name : "phoneNo",
						width : 200,
						align : "center"
					},
					{
						name : 'id',
						formatter : function(cellValue, option, rowObject) {
							return "<span><a style='margin-left:12px;' href='javascript:void(0);' onclick='mallOlsDialogView("
									+ JSON.encode(rowObject)
									+ ")' >查看</a></span>"
									+ "<span><a style='margin-left:12px;' href='javascript:void(0);' onclick='mallOlsDialogEdit("
									+ JSON.encode(rowObject)
									+ ")' >修改</a></span>"
									+ "<span><a style='margin-left:12px;' href='javascript:void(0);' onclick='deletemallOls("
									+ JSON.encode(rowObject)
									+ ")' >删除</a></span>";
						},
						width : 200,
						align : "center"
					} ],
			multiselect : true,// 定义是否可以多选
			multikey:'ctrlKey', 
			pager : "#olsPager", // 分页div
			loadComplete : function(gridData) { // JqGridHelper缓存最新的grid数据
				gridHelper.cacheData(gridData);
				var callback = getCallbackAfterGridLoaded();
				if (isFunction(callback)) {
					callback();
				}
			},
			ondblClickRow: function(rowId) {
				var userMap = gridHelper.getRowData(rowId)
				mallOlsDialogView(userMap);
			}
		});
	}
	//加载店铺人员列表数据
	function loadMallStaffData(){
		var filter = {};
		var nickName = textGet("queryUser").trim();
		if(nickName){
			filter.nickName = nickName;
		}
		//加载人员列表
		olsGridCtrl= $("#mallStaffGrid").jqGrid({
		      url : getAppUrl("/interact/staff/list/get"),  
		      contentType : 'application/json',  
		      mtype : "post",  
		      datatype : 'json',
		      postData:{filterStr : JSON.encode(filter,true)},
		      height : 270,
			  width : "100%",
		      colNames : ["人员编号", "人员名称","手机","邮箱","角色"],  
		      colModel : [{name:"id", index:"id", hidden : true, width:50,align : 'center',},
		                  {name:"nickName", index:"nickName",width:168,align : 'center',},
		                  {name:"phoneNo", index:"phoneNo",width:180,align : 'center'},
		                  {name:"email", index:"email",width:200,align : 'center'},
		                  {name:'roles',index:'roles', align : 'center',formatter : function (cellValue,option,rowObject) {
		                	  var roleNames=[];
		                	  for(var i=0;i<cellValue.length;i++){
		                		  roleNames.add(cellValue[i].name);
								}
		                	  return roleNames;
							},width:260,align:"center"}
						],  
			multiselect : true,
			multikey:'ctrlKey',
			pager : "#mallStaffPager",
			loadComplete : function(gridData){
				staffGridHelper.cacheData(gridData);
				var callback = getCallbackAfterGridLoaded();
				if(isFunction(callback)){
					callback();
				}
			},
		});
	}
	
	//初始化客服模板
	 function initMallOlsTpl(){
		var mallOlsTplHtml = $id("mallOlsTpl").html();
		var htmlStr = laytpl(mallOlsTplHtml).render({});
		$id("viewOlsDialog").html(htmlStr);
		initEditOlsDialog();
		mallOlsFormProxy.addField({
			id : "servantNo",
			required : true,
			rules : ["maxLength[15]", {
				rule : function(idOrName, type, rawValue, curData) {
					checkServantNo(rawValue);
					return !servantNoFlag;
				},
				message : "客服编号已存在！"
			}]
		});
		mallOlsFormProxy.addField({
			id : "servantName",
			required : true,
			rules : ["maxLength[30]", {
				rule : function(idOrName, type, rawValue, curData) {
					checkServantName(rawValue);
					return !servantNameFlag;
				},
				message : "客服名称已存在！"
			}]
		});
	 }

	 var servantNoFlag = false;
	//客服编号是否已存在
	function checkServantNo(servantNo){
		var checkFlag = (servantNo != null && servantNo != "");
		var id = tempMallOls.id;
		if(checkFlag){
			var ajax = Ajax.post("/interact/exist/by/servantNo/do");
			ajax.data(servantNo);
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				var data = result.data;
				if(result.type == "info"){
					if(id != data.id){
						servantNoFlag = true;
					}else{
						servantNoFlag = false;
					}
				}else{
					servantNoFlag = false;
				}
			});
			ajax.go();
		}
	}
	var servantNameFlag = false;
	//客服名称是否已存在
	function checkServantName(servantName){
		var checkFlag = (servantName != null && servantName != "");
		var id = tempMallOls.id;
		if(checkFlag){
			var ajax = Ajax.post("/interact/exist/by/servantName/do");
			ajax.data(servantName);
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				var data = result.data;
				if(result.type == "info"){
					if(id != data.id){
						servantNameFlag = true;
					}else{
						servantNameFlag = false;
					}
					
				}else{
					servantNameFlag = false;
				}
			});
			ajax.go();
		}
	}
	//------------------------初始化Dialog-------------------------
		
		//修改人员Dialog
		function initEditOlsDialog(){
			olsDialog = $( "#viewOlsDialog" ).dialog({
		        autoOpen: false,
		        width : Math.min(800, $window.width()),
		        height : Math.min(480, $window.height()),
		        modal: true,
		        title : '编辑客服',
		        buttons: {
		        	"保存" : defaultMethod,
		            "取消": function(){
		            	clearDialog();
		            	olsDialog.dialog( "close" );
		          }
		        },
		        close: function() {
		        	clearDialog();
		        	olsDialog.dialog( "close" );
		        }
		      });
		}
		
		//查看人员Dialog
		function initViewOlsDialog(){
			olsDialog = $( "#viewOlsDialog" ).dialog({
		        autoOpen: false,
		        width : Math.min(800, $window.width()),
		        height : Math.min(480, $window.height()),
		        modal: true,
		        title : '查看客服',
		        buttons: {
		        	"修改 >" : defaultMethod,
		            "关闭": function(){
		            	clearDialog();
		            	olsDialog.dialog( "close" );
		          }
		        },
		        close: function() {
		        	clearDialog();
		        	olsDialog.dialog( "close" );
		        }
		      });
		}
		
		//初始化添加客服Dialog
		function initAddOlsDialog(){
			addOlsDialog = $( "#addOlsDialog" ).dialog({
		        autoOpen: false,
		        width : Math.min(900, $window.width()),
		        height : 500,
		        modal: true,
		        title : '添加客服',
		        buttons: {
		        	"确定" : defaultMethod,
		            "关闭": function() {
		            	clearDialog();
		            	addOlsDialog.dialog( "close" );
		          }
		        },
		        close: function() {
		        	addOlsDialog.dialog( "close" );
		        }
		      });
		}
		//自适应界面大小
		function adjustCtrlsSize() {
			var jqMainPanel = $id("mainPanel");
			var mainWidth = jqMainPanel.width();
			var mainHeight = jqMainPanel.height();
			var gridCtrlId = "olsList";
			var jqGridBox = $("#gbox_" + gridCtrlId);
		
			var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", $("#gbox_olsList")).height();
			var pagerHeight = $id("olsPager").height();
			jqGridCtrl.setGridWidth(mainWidth - 1);
			jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3);
		}
		
		//方法拦截
		function defaultMethod() {
			if (method == "add") {
				saveMallOls();
			}
			if (method == "edit") {
				updateMallOls();
			}
			if (method == "view") {
				var id = tempMallOls.id;
				tempMallOls = gridHelper.getRowData("id",id);
				mallOlsDialogEdit(tempMallOls);
			}
		}
		
		//添加客服
		function saveMallOls(){
			var ids = olsGridCtrl.jqGrid("getGridParam", "selarrrow");
			var postData = [];
			for (var i = 0; i < ids.length; i++) {
				postData.add(ParseInt(ids[i]));
			}
			if (ids == "") {
				Toast.show("请选择要添加的人员！");
			} else {
				//
				var hintBox = Layer.progress("正在保存...");
				var ajax = Ajax.post("/interact/servant/add");
				ajax.data(postData);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						//
						olsGridCtrl.jqGrid("setGridParam").trigger("reloadGrid");
						jqGridCtrl.jqGrid("setGridParam").trigger("reloadGrid");
					} else {
						Layer.msgWarning(result.message);
					}
				});
				ajax.always(function() {
					hintBox.hide();
				});
				ajax.go();
				addOlsDialog.dialog( "close" );
			}
		}
		//编辑客服
		function mallOlsDialogEdit(obj) {
			clearDialog();
			tempMallOls = obj;
			tempId = obj.id;
			method = "edit";
			initEditOlsDialog();
			textSet("id", tempId);
			textSet("nickName", obj.nickName);
			textSet("servantNo", obj.servantNo);
			textSet("servantName", obj.servantName);
			textSet("phoneNo", obj.phoneNo);
			textSet("email", obj.email);
			textSet("roles", obj.roles);
			$id("servantNo").attr("disabled", false);
			$id("servantName").attr("disabled", false);
			olsDialog.dialog("open");
		}
		//查看客服
		function mallOlsDialogView(obj) {
			clearDialog();
			tempMallOls = obj;
			tempId = obj.id;
			method = "view";
			initViewOlsDialog();
			textSet("id", tempId);
			textSet("nickName", obj.nickName);
			textSet("servantNo", obj.servantNo);
			textSet("servantName", obj.servantName);
			textSet("phoneNo", obj.phoneNo);
			textSet("email", obj.email);
			textSet("roles", obj.roles);
			olsDialog.dialog("open");
		}
		
		//更新客服信息
		function updateMallOls(){
			var vldResult = mallOlsFormProxy.validateAll();
			if(vldResult){
				var ajax = Ajax.post("/interact/servant/update/do");
				var dataMap = {
						id : intGet("id"),
						nickName : textGet("nickName"),
						servantNo : textGet("servantNo"),
						servantName : textGet("servantName"),
						phoneNo : textGet("phoneNo"),
						email : textGet("email"),
				};
				ajax.data(dataMap);
				ajax.done(function(result, jqXhr) {
					Layer.msgSuccess(result.message);
					jqGridCtrl.jqGrid("setGridParam").trigger("reloadGrid");
					tempMallOls = dataMap;
					mallOlsDialogView(tempMallOls);
				});
				ajax.go(); 
			}
		}
		
		//删除客服信息
		function deletemallOls(obj){
			tempId = obj.id;
			var theLayer = Layer.confirm('确定要删除？', function() {
				//
				var hintBox = Layer.progress("正在删除...");
				var ajax = Ajax.post("/interact/servant/delete/do");
				ajax.params({
					id : tempId
				});
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						Layer.msgSuccess(result.message);
						//
						olsGridCtrl.jqGrid("setGridParam").trigger("reloadGrid");
						jqGridCtrl.jqGrid("setGridParam").trigger("reloadGrid");
					} else {
						Layer.msgWarning(result.message);
					}
				});
				ajax.always(function() {
					hintBox.hide();
				});
				ajax.go();
			});
		}
		
		//批量删除客服信息
		function delmallOlsByIds(){
			var ids = jqGridCtrl.jqGrid("getGridParam", "selarrrow");
			var postData = [];
			for (var i = 0; i < ids.length; i++) {
				postData.add(ParseInt(ids[i]));
			}
			if (ids == "") {
				Toast.show("请选择要删除的数据！");
			} else {
				var theLayer = Layer.confirm('确定要删除选择的数据吗？', function() {
					//
					var hintBox = Layer.progress("正在删除...");
					var ajax = Ajax.post("/interact/servant/delete/batch/do");
					ajax.data(postData);
					ajax.done(function(result, jqXhr) {
						if (result.type == "info") {
							Layer.msgSuccess(result.message);
							//
							olsGridCtrl.jqGrid("setGridParam").trigger("reloadGrid");
							jqGridCtrl.jqGrid("setGridParam").trigger("reloadGrid");
						} else {
							Layer.msgWarning(result.message);
						}
					});
					ajax.always(function() {
						hintBox.hide();
					});
					ajax.go();
				});
			}
		}
		
		//查询
		function queryData() {
			var servantNo = $id("queryServantNo").val();
			var servantName = $id("queryServantName").val();
			var nickName = $id("queryUserName").val();
			jqGridCtrl.jqGrid("setGridParam", {
				postData : {
					filterStr : JSON.encode({
						"servantNo" : servantNo,
						"servantName" : servantName,
						"nickName" : nickName
					}, true)
				}
			}).trigger("reloadGrid");
			getCallbackAfterGridLoaded = function(){
			};
		}
		//查询人员
		function queryUserData() {
			var nickName = $id("queryUser").val();
			olsGridCtrl.jqGrid("setGridParam", {
				postData : {
					filterStr : JSON.encode({
						"nickName" : nickName
					}, true)
				}
			}).trigger("reloadGrid");
			getCallbackAfterGridLoaded = function(){
			};
		}
		//清空方法
		function clearDialog() {
			mallOlsFormProxy.hideMessages();
			$("input", "#viewOlsDialog").attr("disabled", true);
			$("select", "#viewOlsDialog").attr("disabled", true);
			$("textarea", "#viewOlsDialog").attr("disabled", true);
		}
		
		
</script>
</body>	
<!-- layTpl begin -->
<!-- 商城客服模板 -->
<script id="mallOlsTpl" type="text/html">
		<div class="form">
			<input type="hidden" id="id" />
			<div class="field row">
				<label class="field label one wide">人员名称</label>
				<input class="field value two wide" type="text" id="nickName"/>
			</div>
			<div class="field row">
				<label class="field label one wide required">客服编号</label>
				<input class="field value two wide" type="text" id="servantNo" />
			</div>
			<div class="field row">
				<label class="field label one wide required">客服名称</label>
				<input class="field value two wide" type="text" id="servantName" />
			</div>
			<div class="field row">
				<label class="field label one wide">手机</label>
				<input class="field value two wide" type="text" id="phoneNo"/>
			</div>
			<div class="field row">
				<label class="field label one wide">邮箱</label>
				<input class="field value two wide" type="text" id="email"/>
			</div>
		</div>
</script>
<!-- layTpl end -->					
</html>