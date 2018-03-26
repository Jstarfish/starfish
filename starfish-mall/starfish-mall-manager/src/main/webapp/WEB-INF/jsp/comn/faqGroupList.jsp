<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>常见问题分组管理</title>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned"><button id="btnToAdd" class="button">添加</button></div>
				<div class="group right aligned">
					<label class="label">分组名称</label> <input id="queryName" class="input"  maxlength="10" /> 
					<!-- <span class="spacer"></span> -->
					<button id="btnToQry" class="button">查询</button>
					<!-- <span class="vt divider"></span> -->
				</div>
			</div>
		</div>
		<table id="theGridCtrl"></table>
		<div id="theGridPager"></div>
	</div>
	<div id="dialog" style="display: none;">
		<div id="addForm" class="form">
			<div class="field row">
				<label class="field label required">分组名称</label> 
				<input type="text" id="name" name="name" class="field value one half wide"  maxlength="10" />
				<label class="field inline label required one half wide">所属分类</label> 
				<select class="field value" id="catId" name="catId"><option value="" title="- 请选择 -">- 请选择 -</option></select>
				<label class="field inline label required one half wide" id="seqNoTxt" style="display:none">排序号</label> 
				<input type="text" id="seqNo" name="seqNo" value="" class="field value one half wide" style="width:70px;display:none"/>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js"></script>
	<script>
	//-------------------------------------------自定义方法------------------------------{{
	/**
	 * 加载常见问题分类
	 * @author 邓华锋
	 * @date 2015年10月9日
	 *
	 */
	function loadFaqCat(callback) {
		// 隐藏页面区
		var ajax = Ajax.post("/comn/faqCat/selectList/get");
		ajax.params({});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData("catId", result.data);
				if($.isFunction(callback)){
					callback();
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	//-----------------------------------------------------------------------------------}}
	//------------------------------------------初始化配置---------------------------------{{
	$id("mainPanel").jgridDialog({
		dlgTitle : "常见问题分组",
		addUrl : "/comn/faqGroup/add/do",
		updUrl : "/comn/faqGroup/update/do",
		delUrl : "/comn/faqGroup/delete/do",
		jqGridGlobalSetting:{
			url : getAppUrl("/comn/faqGroup/list/get"),
			colNames : [ "分组名称", "所属分类", "排序号", "更新时间 ", " 操作" ],
			colModel : [ {
				name : "name",
				index : "name",
				width : 200,
				align : 'center'
			}, {
				name : "faqCat.name",
				index : "faqCat.name",
				width : 200,
				align : 'center'
			}, {
				name : "seqNo",
				index : "seqNo",
				width : 100,
				align : 'center'
			}, {
				name : "ts",
				index : "ts",
				width : 200,
				align : "center"
			}, {
				name : 'id',
				index : 'id',
				formatter : function(cellValue, option, rowObject) {
					var s = "[<a class='item dlgupd' href='javascript:void(0);' cellValue='" + cellValue + "' >修改</a>]" + "&nbsp;&nbsp;&nbsp;[<a class='item dlgdel' href='javascript:void(0);' cellValue='" + cellValue + "' >删除</a>]</span>  ";
					return s;
				},
				align : "center"
			} ]
		},
		//增加对话框配置
		addDlgConfig : {
			width : Math.min(600, $window.width()),
			height : Math.min(160, $window.height()),
			modal : true,
			open : false
		},
		//修改和查看对话框配置
		modAndViewDlgConfig : {
			width : Math.min(800, $window.width()),
			height : Math.min(160, $window.height()),
			modal : true,
			open : false
		},
		modElementToggle : function(isShow) {
			if (isShow) {
				$id("seqNo").show();
				$id("seqNoTxt").show();
			} else {
				$id("seqNo").hide();
				$id("seqNoTxt").hide();
			}
		},
		addInit : function() {
			loadFaqCat();
			textSet("name", "");
			textSet("catId", "");
			textSet("seqNo", "");
		},
		modAndViewInit : function(data) {
			loadFaqCat(function(){
				textSet("catId", data.catId);
			});
			textSet("name", data.name);
			textSet("seqNo", data.seqNo);
		},
		queryParam : function(postData, formProxyQuery) {
			var name = formProxyQuery.getValue("queryName");
			if (name != "") {
				postData['name'] = name;
			}
		},
		formProxyInit : function(formProxy) {
			formProxy.addField({
				id : "name",
				key : "name",
				rules : [ {
					rule : function(idOrName, type, rawValue, curData) {
						// 若显示且为空，给予提示
						if ($id("name").is(":visible") && isNoB(rawValue)) {
							return false;
						}
						return true;
					},
					message : "此为必填！"
				} ]
			});
			formProxy.addField({
				id : "catId",
				key : "catId",
				rules : [ {
					rule : function(idOrName, type, rawValue, curData) {
						// 若显示且为空，给予提示
						if ($id("catId").is(":visible") && isNoB(rawValue)) {
							return false;
						}
						return true;
					},
					message : "此为必选！"
				} ]
			});
			formProxy.addField({
				id : "seqNo",
				key : "seqNo",
				rules : [ {
					rule : function(idOrName, type, rawValue, curData) {
						// 区若显示且为空，给予提示
						if ($id("seqNo").is(":visible") && isNoB(rawValue)) {
							return false;
						}
						return true;
					},
					message : "此为必填项！"
				} ]
			});
		},
		formProxyQueryInit : function(formProxyQuery) {
			formProxyQuery.addField({
				id : "queryName",
				rules : [ "maxLength[30]" ]
			});
		},
		getDelComfirmTip : function(data) {
			var theSubject = data.name;
			return '确定要删除"' + theSubject + '"分组(属于它的常见问题将一起删除)吗？';
		},
		pageLoad : function() {
			loadFaqCat();
		}
	});
	//-----------------------------------------------------------------------------------}}
	</script>
</body>
</html>