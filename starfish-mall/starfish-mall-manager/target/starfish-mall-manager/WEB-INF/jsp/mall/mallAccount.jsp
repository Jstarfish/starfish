<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	<title>商城资金账户</title>
</head>
<body id="rootPanel">
<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
	<div class="filter section">
		<div class="filter row">
			<div class="field row"  id="mallNoticeOperate">
				<button class="normal button" id="btnAddAccount">添加</button>
			</div>
		</div>
	</div>
	<table id="userAccountGrid"></table>
	<div id="userAccountPager"></div>
	<div id="userAccountDialog"></div>
	
	<div class="form" id="userAccountDlg" style="display: none;">
	<div class="field row">
		<label class="field label required  one half wide">银行名称</label>
		<select id="bankCode" class="field value two wide"></select>
	</div>
	<div class="field row">
		<label class="field label required one half wide">银行账号</label> 
		<input type="text" id="acctNo" class="field value two wide" />
	</div>
	<div class="field row">
		<label class="field label required one half wide">开户名</label> 
		<input type="text" id="acctName" class="field value two wide" />
	</div>
	<div class="field row">
		<label class="field label one half wide">预留号码</label> 
		<input type="text" id="phoneNo" class="field value two wide" />
	</div>
	<div id="vfCodeDiv" class="field row" >
		<label class="field label one half wide">打款验证码</label> 
		<input type="text" id="vfCode" class="field value two wide" maxlength="30"/>
	</div>

</div>
</div>


<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript">
	// 商城公告页面表单代理
	var accountFormProxy = FormProxy.newOne();
	// tabs、商城公告Grid
	var theTabsCtrl;
	//
    var accountGridHelper = JqGridHelper.newOne("");
	var curAction;
	
	//扩展对话框按钮栏控件（复选框）
	var dlgToolbarCtrlsName = "dlg-toolbar-ctrls";
	var dlgToolbarCtrlsHtml = '<span style="float:left;padding-left:15px;line-height:40px;color:blue;" class="align middle" name="' + dlgToolbarCtrlsName + '"><input class="align middle" type="checkbox" name="' + dlgToolbarCtrlsName + '-checkbox"  id="' + dlgToolbarCtrlsName + '-checkbox-id" />&nbsp;<label class="align middle" for="' + dlgToolbarCtrlsName + '-checkbox-id">保存后继续添加<label></span>';
	var dlgToolbarCtrls = null;
	accountFormProxy.addField({
		id : "bankCode",
		required : true,
		messageTargetId : "bankCode"
	});
	accountFormProxy.addField({
		id : "acctNo",
		required : true,
		rules : ["maxLength[30]"],
		messageTargetId : "acctNo"
	});
	accountFormProxy.addField({
		id : "acctName",
		required : true,
		rules : ["maxLength[30]"],
		messageTargetId : "acctName"
	});
	accountFormProxy.addField({
		id : "phoneNo",
		rules : ["isMobile","maxLength[11]"],
		messageTargetId : "phoneNo"
	});
	
	//加载资金账户数据
	function loadUserAccountData(){
		var postData = {
		};
		//加载资金账户
		accountGridCtrl= $("#userAccountGrid").jqGrid({
		      url : getAppUrl("/user/userAccount/list/get"),  
		      contentType : 'application/json',  
		      mtype : "post",  
		      datatype : 'json',
		      postData:{filterStr : JSON.encode(postData,true)},
		      height : "100%",
			  width : "100%",
		      colNames : ["id", "账户类型","开户银行","银行账号", "开户人","操作"],  
		      colModel : [{name:"id", index:"id", width:150,align : 'center', hidden : true},
		                  {name:"typeFlag", index:"typeFlag",width:110,align : 'center',formatter : function(cellValue){
		                	  return cellValue==0?'私人账户':'对公账户';
		                  }},
		                  {name:"bankName", index:"bankName",width:140,align : 'center'},
		                  {name:"acctNo", index:"acctNo",width:230,align : 'center'},
		                  {name:"acctName", index:"acctName",width:130,align : 'center',},
		                  {
								name : 'id',
								index : 'id',
								formatter : function(cellValue,
										option, rowObject) {
									return "<span> [<a href='javascript:void(0);' onclick='openViewDlg("
											+ cellValue + ")' >查看</a>]   [<a href='javascript:void(0);' onclick='openModDlg("
											+ cellValue + ")' >修改</a>]  [<a href='javascript:void(0);' onclick='deleteMallAccount("
											+ cellValue
											+ ")' >删除</a>] ";
								},
								width : 150,
								align : "center"
						}],  
			loadtext: "Loading....",
			multiselect : false,// 定义是否可以多选
			multikey:'ctrlKey',
			//pager : "#userAccountPager",
			loadComplete : function(gridData){
				accountGridHelper.cacheData(gridData);
				var callback = getCallbackAfterGridLoaded();
				if(isFunction(callback)){
					callback();
				}
			},
			ondblClickRow: function(rowId) {
				openViewDlg(rowId);
			}
			
		});
	}
	//
	function getCallbackAfterGridLoaded(){}
	
	//打开查看对话框
	function openViewDlg(dataId) {
		curAction = "view";
		//
		toShowTheDlg(dataId);
	}

	//打开新增对话框
	function openAddDlg() {
		curAction = "add";
		//
		toShowTheDlg();
	}

	//打开修改对话框
	function openModDlg(dataId) {
		curAction = "mod";
		//
		toShowTheDlg(dataId);
	}
	
	//（真正）显示对话款
	function toShowTheDlg(dataId) {
		//
		var theDlgId = "userAccountDlg";
		jqDlgDom = $id(theDlgId);
		//清除扩展的对话框按钮栏控件
		if (true) {
			var jqButtonPane = jqDlgDom.siblings(".ui-dialog-buttonpane");
			dlgToolbarCtrls = jqButtonPane.find("[name='" + dlgToolbarCtrlsName + "']");
			dlgToolbarCtrls.remove();
		}
		//对话框配置
		var dlgConfig = {
			width : Math.min(550, $window.width()),
			height : Math.min(400, $window.height()),
			modal : true,
			open : false
		};
		//
		if (curAction == "add") {
			dlgConfig.title = "商城资金账户 - 新增";
			dlgConfig.buttons = {
				"保存" : function() {
					//收集并验证要提交的数据（如果验证不通过直接返回 return）
					var vldResult = accountFormProxy.validateAll();
					if (!vldResult) {
						return;
					}
					var postData = accountFormProxy.getValues();
					//...
					saveMallAccount();
					var continuousFlag = jqDlgDom.prop("continuousFlag");
					if (continuousFlag) {
						//连续新增
						clearDialog();
						return;
					}else {
						$(this).dialog("close");
					}
				},
				"取消" : function() {
					//
					jqDlgDom.prop("continuousFlag", false);
					//
					$(this).dialog("close");
					//隐藏表单验证消息
					accountFormProxy.hideMessages();
				}
			};
			//
			if (true) {
				//显示“保存后继续添加”复选框
				dlgConfig.open = function(event, ui) {
					//
					var jqButtonPane = jqDlgDom.siblings(".ui-dialog-buttonpane");
					dlgToolbarCtrls = $($(dlgToolbarCtrlsHtml).appendTo(jqButtonPane));
					var theCheckbox = dlgToolbarCtrls.find("[name='" + dlgToolbarCtrlsName + "-checkbox']");
					var continuousFlag = jqDlgDom.prop("continuousFlag");
					theCheckbox.prop("checked", true);
					jqDlgDom.prop("continuousFlag", true);
					//
					theCheckbox.bind("click", function() {
						jqDlgDom.prop("continuousFlag", this.checked);
					});
				};
			}
		} else if (curAction == "mod") {
			dlgConfig.title = "商城资金账户 - 修改";
			dlgConfig.buttons = {
				"保存" : function() {
					//收集并验证要提交的数据（如果验证不通过直接返回 return）
					var vldResult = accountFormProxy.validateAll();
					if (!vldResult) {
						return;
					}
					var postData = accountFormProxy.getValues();
					postData.id = dataId;
					//
					updateMallAccount(dataId);
				},
				"取消" : function() {
					$(this).dialog("close");
					//隐藏表单验证消息
					accountFormProxy.hideMessages();
				}
			};
		} else {
			//== view 查看
			dlgConfig.title = "商城资金账户 - 查看";
			dlgConfig.buttons = {
				"修改 > " : function() {
					$(this).dialog("close");
					//
					openModDlg(dataId);
				},
				"关闭" : function() {
					$(this).dialog("close");
				}
			};
		}
		//
		jqDlgDom.dialog(dlgConfig);
		//
		initTheDlgCtrls(dataId);
	}
	
	function initTheDlgCtrls(dataId) {
		if (curAction == "add" || curAction == "mod") {
			jqDlgDom.find(":input").prop("disabled", false);
		} else {
			jqDlgDom.find(":input").prop("disabled", true);
		}
		//重置控件值
		clearDialog();
		//
		if (curAction == "view" || curAction == "mod") {
			var data = accountGridHelper.getRowData(dataId);
			if (data != null) {
				textSet("bankCode", data.bankCode);
				$("#bankName option[value='" + data.bankCode + "']").attr("selected", true);
				textSet("acctNo", data.acctNo);
				textSet("acctName", data.acctName);
				textSet("phoneNo", data.phoneNo);
				textSet("vfCode", data.vfCode);
			}
		}
	}
	
	
	function clearDialog(){
		textSet("bankCode", "");
		radioSet("bankName", $id("addBankCode").find("option:selected").text());
		textSet("acctNo", "");
		textSet("acctName", "");
		textSet("phoneNo", "");
		textSet("vfCode", "");
	}
	
	//保存店铺图片
	function saveMallAccount(){
		var vldResult = accountFormProxy.validateAll();
		if(vldResult){
			var ajax = Ajax.post("/userAccount/add/do");
			ajax.data({
				bankCode : textGet("bankCode"),
				bankName : $id("bankCode").find("option:selected").text(),
				acctNo : textGet("acctNo"),
				acctName : textGet("acctName"),
				phoneNo : textGet("phoneNo"),
				vfCode : textGet("vfCode"),
				typeFlag : "1",
			});
			ajax.done(function(result, jqXhr) {
				Layer.msgSuccess(result.message);
				accountGridCtrl.jqGrid("setGridParam").trigger("reloadGrid");
			});
			ajax.go(); 
		}
	}
	
	//保存店铺图片
	function updateMallAccount(id){
		var vldResult = accountFormProxy.validateAll();
		if(vldResult){
			var ajax = Ajax.post("/userAccount/update/do");
			ajax.data({
				id : id,
				bankCode : textGet("bankCode"),
				bankName : $id("bankCode").find("option:selected").text(),
				acctNo : textGet("acctNo"),
				acctName : textGet("acctName"),
				phoneNo : textGet("phoneNo"),
				vfCode : textGet("vfCode"),
			});
			ajax.done(function(result, jqXhr) {
				Layer.msgSuccess(result.message);
				accountGridCtrl.jqGrid("setGridParam").trigger("reloadGrid");
			});
			ajax.go(); 
		}
	}
	
	//
	function deleteMallAccount(id){
		var theLayer = Layer.confirm('确定要删除？', function() {
			//
			var hintBox = Layer.progress("正在删除...");
			var ajax = Ajax.post("/userAccount/delete/do");
			ajax.params({
				id : id
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					//
					accountGridCtrl.jqGrid("setGridParam").trigger("reloadGrid");
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
	
	function adjustCtrlsSize(){
		adjustMallAccountSize();
	}
	
	// 调整商城公告页面布局
	function adjustMallAccountSize(){
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		var gridCtrlId = "userAccountGrid";
		var jqGridBox = $("#gbox_" + gridCtrlId);
		var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv", jqGridBox).height();
		//var pagerHeight = $id("userAccountPager").height();
		//var btnNoticeHeight = $id("btnNotice").height();
		accountGridCtrl.setGridWidth(mainWidth - 15);
		accountGridCtrl.setGridHeight(mainHeight - headerHeight - 60);
	}
	
	function loadBanks(){
		var ajax = Ajax.post("/comn/bank/selectList/get");
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData("bankCode", result.data);
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}

	// 初始化页面
	$(function() {
		// 页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 56,
			allowTopResize : false,
			onresize : hideLayoutTogglers
		});
		loadUserAccountData();
		loadBanks();

		// 添加商城公告
		$id("btnAddAccount").click(openAddDlg);
		
		// 调整页面布局
		winSizeMonitor.start(adjustCtrlsSize);
	});
</script>
</body>

<script id="mallNoticeTpl" type="text/html" title="商城公告模板">
	<div class="form">
		<input type="hidden" id="mallNoticeId" />
		<div class="field row">
			<label class="field label one wide required">标题</label>
			<input class="field value two wide" type="text" id="title" />
		</div>
		<div class="field row" style="height: 82px;">
			<label class="field label one wide">内容</label>
			<textarea class="field value three wide" style="height:80px; resize:none;" id="content" ></textarea>
		</div>
		<div class="field row">
			<label class="field label one wide required">发布方式</label>
			<div class="field group">
				<input type="radio" id="noticeAutoFlagOn" name="autoFlag" value="1" />
				<label for="noticeAutoFlagOn">自动</label>
				<input type="radio" id="noticeAutoFlagOff" name="autoFlag" value="0" checked="checked"/>
				<label for="noticeAutoFlagOff">手动</label>
			</div>
		</div>
		<div class="field row" id="beginTime">
			<label id="lblPublishTime" class="field label one wide required">发布时间</label>
			<input class="one half wide field value" type="text" id="pubTime" readonly="true"/>
		</div>
		<div class="field row">
			<label id="lblEndTime" class="field label one wide required">结束时间</label>
			<input class="one half wide field value" type="text" id="endTime" readonly="true"/>
		</div>
		<div class="field row" style="float:left;" id="publishNoticeStatus">
			<label class="field label one wide">状态</label>
			<label id='status' class='normal label'></label><span class="normal spacer"></span>
			<button class="normal button" id="publishNotice" style="margin-top: 4px;">
			</button>
		</div>
	</div>
</script>
</html>