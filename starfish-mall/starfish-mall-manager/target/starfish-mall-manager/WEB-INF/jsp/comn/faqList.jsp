<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>常见问题管理</title>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 4px;">
		<div class="filter section">
			<div class="filter row">
				<div class="group left aligned"><button id="btnToAdd" class="button">添加</button></div>
				<div class="group right aligned">
					<label class="label">关键字</label>
					<input id="queryQuestion" class="input" />
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
				<label class="field label required">所属分类</label> 
				<select class="field value one half wide" id="catId" name="catId"><option value="" title="- 请选择 -">- 请选择 -</option></select>
				<label class="field label required inline one half wide">所属分组</label> 
				<select class="field value  one half wide" id="groupId" name="groupId"><option value="" title="- 请选择 -">- 请选择 -</option></select>
			</div>
			<div class="field row">
				<label class="field label required">问题</label> 
				<input type="text" id="question" name="question" class="field value one half wide" style="width:450px" maxlength="200"/>
				<label class="field inline label required one half wide" id="seqNoTxt" style="display:none">排序号</label> 
				<input type="text" id="seqNo" name="seqNo" value="" class="field value  one half wide" style="width:40px;display:none"/>
			</div>
			<div class="field row">
				<label class="field label required">答案</label>
			</div>
			<div class="field row" >
				<textarea id="answer" name="answer" class="field value three wide" style="height:200px;width:490px" maxlength="5000"></textarea>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript" src="<%=resBaseUrl%>/js-app/jqgrid.dialog.js"></script>
	<script>
	var theEditor1=null;
	//-------------------------------------------自定义方法------------------------------{{
	/**
	 * 加载常见问题分组
	 * @author 邓华锋
	 * @date 2015年10月9日 下午6:44:07
	 * 
	 * @param faqCatId
	 * @param selectedId 控件ID
	 * @param groupId
	 */
	function loadFaqGroup(faqCatId, selectedId, groupId) {
		// 隐藏页面区
		//hideTown();
		var ajax = Ajax.post("/comn/faqGroup/selectList/get");
		ajax.params({
			catId : faqCatId
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData(selectedId, result.data);
				if (typeof (groupId) != "undefined" && groupId != null) {
					$id(selectedId).val(groupId);
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}

	/**
	 * 加载常见问题分类
	 * @author 邓华锋
	 * @date 2015年10月9日
	 *
	 */
	function loadFaqCat() {
		// 隐藏页面区
		//hideTown();
		var ajax = Ajax.post("/comn/faqCat/selectList/get");
		ajax.params({});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData("catId", result.data);
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	//-----------------------------------------------------------------------------------}}
	//------------------------------------------初始化配置---------------------------------{{
	$id("mainPanel").jgridDialog(
			{
				dlgTitle : "常见问题",
				addUrl : "/comn/faq/add/do",
				updUrl : "/comn/faq/update/do",
				delUrl : "/comn/faq/delete/do",
				jqGridGlobalSetting:{
					url : getAppUrl("/comn/faq/list/get"),
					colNames : [ "问题", "答案", "所属分组", "排序号", "更新时间 ", " 操作" ],
					colModel : [
							{
								name : "question",
								index : "question",
								width : 200,
								align : 'left',
								formatter : function(cellValue, option, rowObject) {
									var maxwidth = 23;
									if (cellValue.length > maxwidth) {
										cellValue = cellValue.substring(0, maxwidth);
										cellValue = cellValue + "...";
									}
									return cellValue;
								}
							},
							{
								name : "answer",
								index : "answer",
								width : 400,
								align : 'left',
								formatter : function(cellValue, option, rowObject) {
									var maxwidth = 35;
									if (cellValue.length > maxwidth) {
										cellValue = cellValue.substring(0, maxwidth);
										cellValue = cellValue + "...";
									}
									return cellValue;
								}
							},
							{
								name : "faqGroup.name",
								index : "faqGroup.name",
								width : 120,
								align : 'center'
							},
							{
								name : "seqNo",
								index : "seqNo",
								width : 100,
								align : 'center'
							},
							{
								name : "ts",
								index : "ts",
								width : 120,
								align : "center"
							},
							{
								name : 'id',
								index : 'id',
								formatter : function(cellValue, option, rowObject) {
									var s = "<span>[<a class='item dlgview' href='javascript:void(0);' cellValue='" + cellValue + "'>查看</a>]&nbsp;&nbsp;&nbsp;" + "<span>[<a class='item dlgupd' href='javascript:void(0);' cellValue='" + cellValue + "' >修改</a>]" + "&nbsp;&nbsp;&nbsp;[<a class='item dlgdel' href='javascript:void(0);' cellValue='" + cellValue + "' >删除</a>]</span>  ";
									return s;
								},
								align : "center"
							} ]
				},
				//修改和查看对话框
				modAndViewDlgConfig : {
					width : Math.min(800, $window.width()),
					height : Math.min(600, $window.height()),
					modal : true,
					open : false
				},
				//增加对话框
				addDlgConfig : {
					width : Math.min(800, $window.width()),
					height : Math.min(600, $window.height()),
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
					textSet("question", "");
					textSet("answer", "");
					textSet("catId", "");
					textSet("groupId", "");
					textSet("seqNo", "");
					if(theEditor1==null){
						theEditor1 = CKEDITOR.replace("answer");						
					}
					theEditor1.setData("");
				},
				modAndViewInit : function(data) {
					textSet("question", data.question);
					textSet("answer", data.answer);
					textSet("catId", data.faqGroup.catId);
					//CKEDITOR.instances["answer"].setData(data.answer);
					//CKEDITOR.instances["question"].getData()
					textSet("seqNo", data.seqNo);
					loadFaqGroup(data.faqGroup.catId, "groupId", data.faqGroup.id);
					if(theEditor1==null){
						theEditor1 = CKEDITOR.replace("answer");						
					}
					theEditor1.setData(data.answer);
				},
				queryParam : function(postData, formProxyQuery) {
					var question = formProxyQuery.getValue("queryQuestion");
					if (question != "") {
						postData['keywords'] = question;
					}
				},
				formProxyInit : function(formProxy) {
					formProxy.addField({
						id : "question",
						key : "question",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 若显示且为空，给予提示
								if ($id("question").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "此为必填项！"
						} ]
					});
					formProxy.addField({
						id : "answer",
						key : "answer",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 若显示且为空，给予提示
								if ($id("answer").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "此为必填项！"
						} ]
					});
					formProxy.addField({
						id : "catId",
						key : "faqGroup.catId",
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
						id : "groupId",
						key : "groupId",
						rules : [ {
							rule : function(idOrName, type, rawValue, curData) {
								// 若显示且为空，给予提示
								if ($id("groupId").is(":visible") && isNoB(rawValue)) {
									return false;
								}
								return true;
							},
							message : "此为必填选！"
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
					//注册表单控件
					formProxyQuery.addField({
						id : "queryQuestion",
						rules : [ "maxLength[30]" ]
					});
				},
				getDelComfirmTip : function(data) {
					var theSubject = data.question;
					return '确定要删除"' + theSubject + '"吗？';
				},
				savePostDataChange:function(postData){
					//CKEDITOR.instances["question"].getData()
					postData["answer"]=theEditor1.getData();
				},
				pageLoad : function() {
					loadFaqCat();
					// 绑定区域change事件
					$id("catId").change(function() {
						loadFaqGroup($(this).val(), "groupId");
					});
					
					//CKEDITOR.instances["answer"].setData("");
				}
			});
	//-----------------------------------------------------------------------------------}}
	</script>
</body>
</html>