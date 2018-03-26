<%@page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>店铺会员管理设置</title>
<style type="text/css">

</style>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<button id="addBtn" class="button">添加</button>
				<span class="vt divider"></span>
				<div class="right aligned group">
					<label class="label">会员编号</label>
					<input id="q_memberNo" class="input wide" /> 
					<span class="spacer"></span> 
					<label class="label">用户名</label>
					<input id="q_nickName" class="input wide" /> 
					<span class="spacer"></span> 
					<label class="label">会员等级</label> 
					<select id="q_grade" class="input"></select> 
					<span class="spacer"></span> 
					<span class="vt divider"></span>
					<span class="spacer"></span>
					<button id="queryBtn" class="button">查询</button>
				</div>
			</div>
		</div>
		
		<table id="member_list"></table>
		<div id="member_pager"></div>
	</div>
	
	<div id="dialog" style="display: none;"></div>
	
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
	//实例化表单代理
	var formProxy = null;
	
	var member_dialog = null;
	
	var isExsit = false;
	
	var gradeData = {
			"items" : [ {
				"value" : "1",
				"text" : "注册会员"
			}, {
				"value" : "2",
				"text" : "普通会员"
			}, {
				"value" : "3",
				"text" : "白银会员"
			}, {
				"value" : "4",
				"text" : "黄金会员"
			}, {
				"value" : "5",
				"text" : "钻石会员"
			} ],
			"unSelectedItem" : {
				"value" : "",
				"text" : "- 请选择 -"
			},
			"defaultValue" : ""
		};
	
	function initFormField(){
		formProxy = FormProxy.newOne();
		formProxy.addField({
			id : "nickName",
			required : true,
			rules : ["maxLength[30]"],
			messageTargetId : "nickName"
		});
		
		formProxy.addField({
			id : "phoneNo",
			required : true,
			rules : ["maxLength[11]","isMobile",
			         {
				rule : function(idOrName, type, rawValue, curData) {
					if(method == "edit"){
						if(!isNoB(orgPhoneNo) && orgPhoneNo == rawValue){
							return true;
						}
					}
					//
					isExsitMemberByPhoneNo(rawValue);
					if(isExsit){
						$(".ui-button-text").each(function(){
							if($(this).html() == "保存"){
								var btn = $(this).parent();
								$(btn).prop("disabled",true);
								$(btn).css("cursor","default");
								$(btn).css("opacity","0.5");
							}
						});
						return false;
					}
					$(".ui-button-text").each(function(){
						if($(this).html() == "保存"){
							var btn = $(this).parent();
							$(btn).prop("disabled",false);
							$(btn).css("cursor","pointer");
							$(btn).css("opacity","");
						}
					});
					return true;
				},
				message : "该用户已经存在!"
			}],
			messageTargetId : "phoneNo"
		});
		
		formProxy.addField("gender");
		
		formProxy.addField({
			id : "point",
			rules : ["isNatual","maxLength[9]"],
			messageTargetId : "point"
		});
		
		formProxy.addField({
			id : "idCertNo",
			rules : ["maxLength[18]",{
				rule : function(idOrName, type, rawValue, curData) {
					var checkStr = rawValue;
					if(checkStr == "" || checkStr == null) return true;
					if (checkStr.length > 18) return false;
					var regExp = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
					return regExp.test(checkStr);
				},
				message : "身份证号码错误！"
			}],
			messageTargetId : "idCertNo"
		});
		
		formProxy.addField({
			id : "realName",
			rules : ["maxLength[30]"],
			messageTargetId : "realName"
		});
		
		formProxy.addField({
			id : "email",
			rules : ["isEmail"],
			messageTargetId : "email"
		});
		
		formProxy.addField("qq");
		
		formProxy.addField({
			id : "birthDate",
			type : "date",
			rules : ["isDate"]
		});
		
		formProxy.addField({
			id : "memo",
			rules : ["maxLength[250]"],
			messageTargetId : "memo"
		});
		
	}
	
	//返回是否存在相同的会员
	function isExsitMemberByPhoneNo(phoneNo){
		var ajax = Ajax.post("/member/isExsit/by/phoneNo/do");
		ajax.data({phoneNo:phoneNo});
		//同步验证
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			isExsit = result.data;
		});
		ajax.go();
	}
	
	$(function() {
		//页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			north__size : 160,
			allowTopResize : false
		});
		//
		loadSelectData("q_grade", gradeData);
		//加载初始数据
		loadData();
		//调整控件大小
		winSizeMonitor.start(adjustCtrlsSize);
		//
		$id("addBtn").click(function(){
			goAdd();
		});
		//
		$id("queryBtn").click(function(){
			var filter = {"shopId":1};
			
			var memberId = intGet("q_memberNo");
			if(memberId){
				filter.memberId = memberId;
			}
			var grade = intGet("q_grade");
			if(grade){
				filter.grade = grade;
			}
			var nickName = $id("q_nickName").val().trim();
			//
			var isMobile = FormProxy.validateRules.isMobile(nickName);
			//
			if(isMobile){
				filter.phoneNo = nickName;
			}else{
				filter.nickName = nickName;
			}
			//加载jqGridCtrl
			jqGridCtrl.jqGrid("setGridParam", {postData :{filterStr : JSON.encode(filter,true)},page:1}).trigger("reloadGrid");
			//
			getCallbackAfterGridLoaded = function(){
			};
		});
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
		jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight - 3 - 60);
	}
	
	//-------------------------------------------------功能实现 --------------------------------------------------------------
	function selectMethod(){
		if(method == 'add'){
			var vldResult = formProxy.validateAll();
			if(vldResult) {
				var dataMap = getMemberMap();
				addMember(dataMap);
			}
		}
	}
	
	function getMemberMap(){
		var dataMap = new KeyMap("memberMap");
		dataMap.add("disabled", false);
		dataMap.add("memo", formProxy.getValue("memo"));
		//dataMap.add("point", formProxy.getValue("point"));
		var userMap = new KeyMap("user");
		userMap.add("nickName", formProxy.getValue("nickName"));
		userMap.add("gender", formProxy.getValue("gender"));
		userMap.add("phoneNo", formProxy.getValue("phoneNo"));
		userMap.add("idCertNo", formProxy.getValue("idCertNo"));
		userMap.add("realName", formProxy.getValue("realName"));
		userMap.add("email", formProxy.getValue("email"));
		userMap.add("qq", formProxy.getValue("qq"));
		userMap.add("birthDate", formProxy.getValue("birthDate"));
		userMap.add("secureLevel", "0");
		dataMap.add("user",userMap);
		return dataMap;
	}
	
	function addMember(dataMap){
		var hintBox = Layer.progress("正在保存数据...");

		var ajax = Ajax.post("/member/add/do");
		
		ajax.data(dataMap);

		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				Layer.msgSuccess(result.message);
				var userId = result.data;
				//加载最新数据列表
				jqGridCtrl.jqGrid().trigger("reloadGrid");
				getCallbackAfterGridLoaded = function(){
					showMemberById(userId);
				};
			} else {
				Layer.msgWarning(result.message);
			}

		});
		ajax.always(function() {
			hintBox.hide();
		});
		
		ajax.go();
	}
	
	function showMemberById(id){
		var hintBox = Layer.progress("正在加载数据...");
		
		var ajax = Ajax.post("/member/mall/list/get");

		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				var memberMap = null;
				$.each(result.data,function(){
					if($(this)[0].id == id){
						memberMap = $(this)[0];
					}
				});
				if(!isNoB(memberMap)) {
					method = "view";
					initViewDialog();
					var htmlStr = laytpl($id("viewMemberTpl").html()).render(memberMap);
					$id("dialog").html(htmlStr);
					member_dialog.dialog("open");
				}
				getCallbackAfterGridLoaded = function(){
				};
				
			} else {
				Layer.msgWarning(result.message);
			}

		});
		ajax.always(function() {
			hintBox.hide();
		});
		
		ajax.go();
	}
	
	function showMemberData(id){
		var memberMap = gridHelper.getRowData("id",id);
		var htmlStr = laytpl($id("viewMemberTpl").html()).render(memberMap);
		$id("dialog").html(htmlStr);
		
	}
	
	function goView(id){
		method = "view";
		//
		initViewDialog();
		showMemberData(id);
		member_dialog.dialog("open");
	}
	
	function initViewDialog(){
		//
		member_dialog = $("#dialog").dialog({
			autoOpen : false,
			height : Math.min(400, $window.height()),
			width : Math.min(750, $window.width()),
			modal : true,
			title : '查看会员信息',
			buttons : {
				"关闭" : function() {
					member_dialog.dialog("close");
				}
			},
			close : function() {

			}
		});
	}
	
	function goAdd(){
		method = "add";
		//
		initAddDialog();
		member_dialog.dialog("open");
	}
	
	function initAddDialog(){
		var htmlStr = laytpl($id("addMemberTpl").html()).render({});
		$id("dialog").html(htmlStr);
		
		$id("birthDate").datepicker();
		
		initFormField();
		//
		member_dialog = $("#dialog").dialog({
			autoOpen : false,
			height : Math.min(400, $window.height()),
			width : Math.min(750, $window.width()),
			modal : true,
			title : '新增会员信息',
			buttons : {
				"保存" : selectMethod,
				"取消" : function() {
					member_dialog.dialog("close");
				}
			},
			close : function() {
				formProxy.hideMessages();

			}
		});
	}
	
	function getCallbackAfterGridLoaded(){}
	
	
	//jqGrid缓存变量
	var jqGridCtrl = null;
	//
	var gridHelper = JqGridHelper.newOne("");
	
	function loadData(){
		var filter = {};
		
		var memberId = intGet("q_memberNo");
		if(memberId){
			filter.memberId = memberId;
		}
		var grade = intGet("q_grade");
		if(grade){
			filter.grade = grade;
		}
		var nickName = $id("q_nickName").val().trim();
		//
		var isMobile = FormProxy.validateRules.isMobile(nickName);
		//
		if(isMobile){
			filter.phoneNo = nickName;
		}else{
			filter.nickName = nickName;
		}
		
		jqGridCtrl= $("#member_list").jqGrid({
	      url : getAppUrl("/member/shop/list/by/page/get"),  
	      contentType : 'application/json',  
	      mtype : "post",  
	      datatype : 'json',
	      postData:{filterStr : JSON.encode(filter,true)},
	      height : "100%",
		  width : "100%",
	      colNames : ["会员编号", "昵称","会员等级","联系电话"," 可用积分","状态","备注","操作"],  
	      colModel : [{name:"id", index:"id", width:50,align : 'right',},
	                  {name:"user.nickName", index:"member.user.nickName",width:110,align : 'left',},
	                  {name:"gradeVO.name", index:"member.gradeVO.name",width:100,align : 'left'},
	                  {name:"user.phoneNo", index:"member.user.phoneNo",width:80,align : 'right'},
	                  {name:"point", index:"member.point",width:80,align : "right"},
	                  {name:"disabled", index:"disabled",width:70,align : 'left',formatter : function (cellValue) {
							return cellValue==false?'启用':'禁用';
						}},
					  {name:"memo", index:"memo", width:200, align : "left",formatter : function (cellValue) {
							return '<div class="auto height">'+cellValue+'</div>';
						}},
	                  {name:'id',index:'id', formatter : function (cellValue,option,rowObject) {
	                	  var disabled = rowObject["disabled"];
	                	  var disabledStr = "禁用";
	                	  if(disabled){
	                		  disabledStr = "启用";
	                	  }
						return "<span> [<a class='item' href='javascript:void(0);' onclick='goView("
						+ cellValue
						+ ")' >查看</a>]";
					},
				width:150,align:"center"}
	                  ],  
		//rowList:[10,20,30],
		multiselect:true,
		multikey:'ctrlKey',
		pager : "#member_pager",
		loadComplete : function(gridData){
			gridHelper.cacheData(gridData);
			var callback = getCallbackAfterGridLoaded();
			if(isFunction(callback)){
				callback();
			}

		},
		ondblClickRow: function(rowId) {
			var memberMap = gridHelper.getRowData(rowId)
			goView(memberMap.id);
		}
	});

	}
	
	</script>
</body>

<script id="addMemberTpl" type="text/html" title="新增会员信息模板">
<div class='form'>
	<div class='field row'>
		<div class='field group'>
			<label class='field label required'>昵称</label> 
			<input type='text' id='nickName' class='field value' />
		</div>
		<span style='width: 80px;' class='normal spacer'></span>
		<div class='field group'>
			<label class='field label'>性别</label>
			<select class='field value' id='gender'>
				<option value='X'>保密</option>
				<option value='M'>男</option>
				<option value='F'>女</option>
			</select>
		</div>
	</div>
	
	<div class='field row'>
		<div class='field group'>
			<label class='field label required'>手机号码</label> 
			<input type='text' id='phoneNo' maxlength="11" class='field value' />
		</div>
		<span style='width: 80px;' class='normal spacer'></span>
		<div class='field group'>
			<label class='field label'>身份证号</label> 
			<input type='text' id='idCertNo' maxlength="30" class='field one and half wide value' />
		</div>
	</div>
	
	<div class='field row'>
		<div class='field group'>
			<label class='field label'>真实姓名</label> 
			<input type='text' id='realName' class='field value' />
		</div>
		<span style='width: 80px;' class='normal spacer'></span>
		<div class='field group'>
			<label class='field label'>邮箱</label> 
			<input type='text' id='email' class='field one and half wide value' />
		</div>
	</div>
	
	<div class='field row'>
		<div class='field group'>
			<label class='field label'>QQ</label> 
			<input type='text' id='qq' maxlength="15" class='field value' />
		</div>
		<span style='width: 80px;' class='normal spacer'></span>
		<div class='field group'>
			<label class='field label'>出生日期</label>
			<input id='birthDate' class='field one and half wide value'/>
		</div>
	</div>
	
	<div class='field row'>
		<div class='field group'>
			<label class='field label'>备注</label>
			<textarea id='memo'  maxlength="250" class='field value two wide' style='height:80px;'></textarea>
		</div>
	</div>

</div>
</script>


<script id="viewMemberTpl" type="text/html" title="查看会员信息模板">
<div class='form'>

	<div class='field row'>
		<div class='field group'>
			<label class='field label required'>昵称</label>
			<input type='text' id='nickName' value='{{ d.user.nickName }}' disabled class='field value' />
		</div>
		<span style='width: 80px;' class='normal spacer'></span>
		<div class='field group'>
			<label class='field label'>性别</label>
			<select disabled class='field value' id='gender'>
				{{# if( d.user.gender == 'M' ){ }}
					<option value='M'>男</option>
				{{# }else if(d.user.gender == 'F'){ }}
					<option value='F'>女</option>
				{{# }else{ }}
					<option value='X'>保密</option>
				{{# } }}
			</select>
		</div>
	</div>
	
	<div class='field row'>
		<div class='field group'>
			<label class='field label required'>手机号码</label>
			<input type='text' id='phoneNo' value='{{ d.user.phoneNo }}' disabled class='field value' />
		</div>
		<span style='width: 80px;' class='normal spacer'></span>
		<div class='field group'>
			<label class='field label'>身份证号</label>
			{{# if(isNoB(d.user.idCertNo)) d.user.idCertNo = ''}}
			<input type='text' id='idCertNo' value='{{ d.user.idCertNo }}' disabled class='field one and half wide value'/>
		</div>
	</div>
	
	<div class='field row'>
		<div class='field group'>
			<label class='field label'>真实姓名</label>
			{{# if(isNoB(d.user.realName)) d.user.realName = ''}}
			<input type='text' id='realName' value='{{ d.user.realName }}' disabled class='field value' />
		</div>
		<span style='width: 80px;' class='normal spacer'></span>
		<div class='field group'>
			<label class='field label'>邮箱</label>
			{{# if(isNoB(d.user.email)) d.user.email = ''}}
			<input type='text' id='email' value='{{ d.user.email }}' disabled class='field one and half wide value' />
		</div>
	</div>
	
	<div class='field row'>
		<div class='field group'>
			<label class='field label'>QQ</label>
			{{# if(isNoB(d.user.qq)) d.user.qq = ''}}
			<input type='text' id='qq' value='{{ d.user.qq }}' disabled class='field value' />
		</div>
		<span style='width: 80px;' class='normal spacer'></span>
		<div class='field group'>
			<label class='field label'>出生日期</label>
			{{# if(isNoB(d.user.birthDate)) d.user.birthDate = ''}}
			<input id='birthDate' value='{{ d.user.birthDate.left(10) }}' disabled class='field one and half wide value'/>
		</div>
	</div>
	
	<div class='field row'>
		<div class='field group'>
			<label class='field label'>积分</label>
			<input id='point' value='{{ d.point }}' disabled class='field value' type='text'/>
		</div>
		<span style='width: 80px;' class='normal spacer'></span>
		<div class='field group'>
			<label class='field label'>会员等级</label>
			{{# var name = ''; if(d.grade == 1){ }}
			{{#   name = '注册会员'; }}
			{{# }else if(d.grade == 2){ }}
			{{#   name = '普通会员'; }}
			{{# }else if(d.grade == 3){ }}
			{{#   name = '白银会员'; }}
			{{# }else if(d.grade == 4){ }}
			{{#   name = '黄金会员'; }}
			{{# }else if(d.grade == 5){ }}
			{{#   name = '黄金会员'; }}
			{{# } }}
			<input id='grade' value='{{ name }}' disabled class='field value' type='text'/>
		</div>
	</div>

	<div class='field row'>
		<div class='field group'>
			<label class='field label required'>状态</label>
			{{# if( d.disabled ){ }}
				<input id='disabled-T' value='禁用' disabled class='field value' type='text'/>
			{{# }else{ }}
				<input id='disabled-F' value='启用' disabled class='field value' type='text'/>
			{{# } }}
		</div>
		<span style='width: 80px;' class='normal spacer'></span>
		<div class='field group'>
			<label class='field label'>备注</label>
			<textarea id='memo' disabled style='height:80px;color:#000080; ' class='field value two wide'>{{ d.memo }}</textarea>
		</div>
	</div>

</div>
</script>

</html>
