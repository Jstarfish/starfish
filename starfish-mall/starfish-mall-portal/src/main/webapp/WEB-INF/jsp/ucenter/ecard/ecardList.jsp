<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/base.jsf"%><jsp:include
	page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
<div class="content">
	<div class="page-width">
		<div class="crumbs">
			<a href="">首页</a>&gt;<a href="">个人中心</a>&gt;<a href="">我的e卡</a>
		</div>
		<div class="section">
			<jsp:include page="/WEB-INF/jsp/include/ucenterMenu.jsp"></jsp:include>
			<div class="section-right1">
				<div class="mod-main">
					<div class="mod-main-tit clearfix">
						<ul class="mod-command">
							<li id="valid" class="active"><a href="javascript:void(0);"
								onclick="getValidCards()">可使用</a></li>
							<li id="invalid"><a href="javascript:void(0);"
								onclick="getInvalidCards()">已作废</a></li>
							<li id="transfer" class="special"><a
								href="javascript:void(0);" onclick="getTransferRecs()">转赠历史</a></li>
						</ul>
						<a class="anormal fr" href="#" style="display:none">e卡规则说明</a> <a
							class="anormal-line fr" href="#" onclick="window.open(getAppUrl('/ecard/list/jsp'))">购买e卡</a>
					</div>
					<table class="order-td border-noright" id="userECardRows"
						style="display: none">
						<tbody>
						</tbody>
					</table>
					<table class="order-td border-noright" id="eCardTransferRecRows">
						<tbody>
							<tr class="title title1">
								<td width="240">名称</td>
								<td>余额</td>
								<td>转赠时间</td>
								<td>受赠人名称</td>
								<td>受赠人手机号</td>
								<td>使用状态</td>
							</tr>
						</tbody>
					</table>
					<!--分页-->
					<div class="pager-gap">
						关于订单有任何问题请拨打 亿投车吧热线 <b class="red1">4000982198</b>
						<div class="fr pager" id="jqPaginator">
							<a class="prev" href="#nogo">上一页</a><a class="active"
								href="#nogo">1</a><a class="next" href="#nogo">下一页</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
<div id="text-msg" class="popup-cont" style="display: none">
	<div class="consume-tit">
		卡号: <span id="cardNo"></span> 卡类型: <span id="cardCode"></span> 面额:¥<span
			id="faceValue"></span>
	</div>
	<table class="order-td consume-td" id="eCardTrasactRecRows">
		<tbody>
		</tbody>
	</table>
</div>
<div id="text-merge" class="popup-cont" style="display: none">
	<table class="order-td consume-td" id="otherUserECards">
		<tbody>
		</tbody>
	</table>
	<div class="orange mt10">注：选中目标卡,点击【合并】操作</div>
</div>
<!--合并卡-end-->
<!--转赠-start-->
<div id="text-present" class="popup-present" style="display: none">
	<dl class="labels labels1">
		<dt>
			<label class="label required">短信验证码：</label>
		</dt>
		<dd>
			<input id="verfCode" class="inputx inputx-w80" type="text" maxlength="6" />&nbsp;<input
				id="btnSendSms" class="btn-normal" type="button" value="获取验证码" 
				onclick="sendVerifySms();" />
		</dd>
	</dl>
	<dl class="labels labels1">
		<dt>
			<label class="label required">受赠人手机：</label>
		</dt>
		<dd>
			<input id="doneePhone" class="inputx inputx-w180" type="text" maxlength="11" />
		</dd>
	</dl>
	<!--<ul class="present-ul">
        <li>
            <label class="label required">短信验证码：</label><input class="inputx inputx-w80" type="text"/>&nbsp;<input class="btn-normal" type="button" value="获取短信验证码" />
        </li>
        <li>
            <label class="label required">转赠人手机：</label><input class="inputx inputx-w180" type="text"/><a class="anormal-line ml10" href="">获取名称</a>
        </li>
        <li>
            <label class="label required">转赠人名称：</label><span>获取名称自动显示</span>
        </li>
    </ul>-->
</div>
<!--转赠-end-->
<script type="text/javascript">
	var isInvalid = false;
	//分页大小（页面级）
	var qry_pageSize = 6;
	// [搜索用]的关键字符串
	var qry_keywords = null;
	// 过滤条件项
	var qry_filterItems = {};
	// 排序项
	var qry_sortItems = {};
	// 分页信息
	var qry_pagination = {
		totalCount : 0,
		pageSize : qry_pageSize,
		pageNumber : 1
	};
	
	var initPaginator = true;

	var cardCodeDict = {
		"diamond" : "钻石卡",
		"platina" : "白金卡",
		"jk" : "金卡",
		"silver" : "银卡",
		"mass" : "大众卡"
	}

	// 待合并的原卡ID
	var cardIdToMerge = null;

	// 待合并的目标卡ID
	var cardIdToBeMerged = null;

	var presentFormProxy = FormProxy.newOne();

	presentFormProxy.addField({
		id : "verfCode",
		key : "verfCode",
		required : true,
		rules : [ "maxLength[6]" ]
	});

	presentFormProxy.addField({
		id : "doneePhone",
		key : "doneePhone",
		required : true,
		rules : [ "maxLength[11]", "isMobile" ]
	});
	
	// 是否倒计时中
	var isCountingDown = false;
	// 倒计时显示格式
	var sendBtnHint = "{0}秒后可再发";
	// 倒计时进行时间
	var countedSeconds = 0;
	// 发送短信间隔时间
	var sendInterval = 60;
	
	// 禁用发送按钮
	function disableSendBtn(disabled) {
		disabled = disabled != false;
		//
		$id("btnSendSms").prop("disabled", disabled);
		$id("btnSendSms").attr("disabled", disabled);
	}
	
	// 发送短信倒计时
	function countdownSendBtn() {
		if (!isCountingDown) {
			isCountingDown = true;
			countedSeconds = 0;
		}
		var leftSeconds = sendInterval - countedSeconds;
		var hintText = sendBtnHint.format(leftSeconds);
		$id("btnSendSms").val(hintText);
		$id("btnSendSms").text(hintText);
		//
		if (leftSeconds > 0) {
			console.log("leftSeconds:" + leftSeconds);
			setTimeout(countdownSendBtn, 1000);
			countedSeconds++;
		} else {
			isCountingDown = false;
			$id("btnSendSms").val("获取验证码");
			$id("btnSendSms").text("获取验证码");
			disableSendBtn(false);
		}
	}
	
	// 发送短信验证码
	function sendVerifySms() {
		// 禁用发送验证码按钮
		disableSendBtn(true);
		
		var ajax = Ajax.post("/ecard/userECard/present/sms/code/send");
		ajax.done(function(result, jqXhr) {
			var data = result.data;
			if (data) {
				countdownSendBtn();
				Layer.msgSuccess(result.message);
			} else {
				disableSendBtn(false);
				Layer.msgWarning(result.message);
			}
		});
		ajax.fail(function() {
			disableSendBtn(false);
			Layer.msgWarning("网络不给力，请稍候再试");
		});
		ajax.go();
	}

	function initUserCardPaginator() {
		initPaginator = true;
		$("#jqPaginator").jqPaginator({
			totalCount : 0,
			pageSize : qry_pageSize,
			pageNumber : 1,
			prev : '<a class="prev" href="javascript:void(0);">上一页<\/a>',
			next : '<a class="next" href="javascript:void(0);">下一页<\/a>',
			page : '<a class="active" href="javascript:void(0);">{{page}}<\/a>',
			onPageChange : function(pageNumber) {
				qry_pagination.pageNumber = pageNumber;
				doTheCardQuery();				
			}
		});

		// 清空过滤条件
		qry_filterItems = {};
	}

	function initTransferRecPaginator() {
		initPaginator = true;
		$("#jqPaginator").jqPaginator({
			totalCount : 0,
			pageSize : qry_pageSize,
			pageNumber : 1,
			prev : '<a class="prev" href="javascript:void(0);">上一页<\/a>',
			next : '<a class="next" href="javascript:void(0);">下一页<\/a>',
			page : '<a class="active" href="javascript:void(0);">{{page}}<\/a>',
			onPageChange : function(pageNumber) {
				qry_pagination.pageNumber = pageNumber;
				doTheTransferRecQuery();
			}
		});

		// 清空过滤条件
		qry_filterItems = {};
	}

	function getValidCards() {
		$("#eCardTransferRecRows").hide();
		$("#userECardRows").show();
		$("#valid").addClass("active");
		$("#invalid").removeClass("active");
		$("#transfer").removeClass("active");
		initUserCardPaginator();
		isInvalid = false;
		filterAndQueryCards();
	}

	function getInvalidCards() {
		$("#eCardTransferRecRows").hide();
		$("#userECardRows").show();
		$("#valid").removeClass("active");
		$("#invalid").addClass("active");
		$("#transfer").removeClass("active");
		initUserCardPaginator();
		isInvalid = true;
		filterAndQueryCards();
	}

	function getTransferRecs() {
		$("#valid").removeClass("active");
		$("#invalid").removeClass("active");
		$("#transfer").addClass("active");
		$("#userECardRows").hide();
		$("#eCardTransferRecRows").show();
		initTransferRecPaginator();
		filterAndQueryTransferRecs();
	}

	// 根据 新的的分页信息（或新的页码） 更新 本地分页信息
	function resetPagination(refPagination) {
		refPagination = refPagination || {};
		if (typeof refPagination == "number") {
			refPagination = {
				pageNumber : refPagination
			};
		}
		//
		merge(qry_pagination, refPagination);
	}

	// 根据最新的信息生成新的查询条件
	function newQryFilterInfo() {
		var retData = {
			keywords : qry_keywords || null,
			filterItems : qry_filterItems || {},
			sortItems : qry_sortItems || {},
			pagination : qry_pagination
		};
		if (retData.pagination) {
			retData.pagination.totalCount = 0;
		}
		return retData;
	}

	// 渲染页面内容
	function renderHtml(data, fromId, toId) {
		//获取模板内容
		var tplHtml = $id(fromId).html();

		//生成/编译模板
		var theTpl = laytpl(tplHtml);

		//根据模板和数据生成最终内容
		var htmlStr = theTpl.render(data);

		//使用生成的内容
		$id(toId).find("tbody").html(htmlStr);
	}

	// 执行统一查询 ==========================================
	function doTheCardQuery() {
		var postData = newQryFilterInfo();

		var ajax = Ajax.post("/ecard/userECard/list/get");
		ajax.sync();
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			var rows = result.data.rows;
			if (rows == null) {
				return;
			}
			// 生成列表
			renderHtml(rows, "userECardRowsTpl", "userECardRows");
			// 设置鼠标滑过显示e卡赠送人信息
			//hoverGroup("present-info");

			// ----刷新分页控件-----
			var refPagination = result.data.pagination;
			resetPagination(refPagination);
			// 注意：jqPaginator的当前页面为 currentPage
			refPagination.currentPage = refPagination.pageNumber;
			// 根据新的分页信息（refPagination）刷新jqPaginator控件
			$("#jqPaginator").jqPaginator("option", refPagination);

		});
		ajax.go();
	}

	function doTheTransferRecQuery() {
		var postData = newQryFilterInfo();

		var ajax = Ajax.post("/ecard/eCardTransferRec/list/get");
		ajax.sync();
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			var rows = result.data.rows;
			if (rows == null) {
				return;
			}
			// 生成列表
			renderHtml(rows, "eCardTransferRecRowsTpl", "eCardTransferRecRows");

			// ----刷新分页控件-----
			var refPagination = result.data.pagination;
			resetPagination(refPagination);
			// 注意：jqPaginator的当前页面为 currentPage
			refPagination.currentPage = refPagination.pageNumber;
			// 根据新的分页信息（refPagination）刷新jqPaginator控件
			$("#jqPaginator").jqPaginator("option", refPagination);
		});
		ajax.go();
	}

	// 关键字 等
	function keywordsAndQuery() {
		// 重建 qry_keywords
		// qry_keywords = ...;
		//
		// 重置页码
		resetPagination(1);
		//
		doTheQuery();
	}

	// 查询 按钮等
	function filterAndQueryCards() {
		// 重建 qry_filterItems
		qry_filterItems = {};
		qry_filterItems.invalid = isInvalid;
		// 或 修改 qry_filterItems
		// merge(qry_filterItems, {...});
		//
		// 重置页码
		resetPagination(1);
		//
		doTheCardQuery();
	}

	// 查询 按钮等
	function filterAndQueryTransferRecs() {
		// 重建 qry_filterItems
		qry_filterItems = {};
		// 或 修改 qry_filterItems
		// merge(qry_filterItems, {...});
		//
		// 重置页码
		resetPagination(1);
		//
		doTheTransferRecQuery();
	}

	// 排序 按钮等
	function sortAndQuery() {
		// 重建 qry_sortItems
		// qry_sortItems = {...};
		// 或 修改 qry_sortItems
		// merge(qry_sortItems, {...});
		//
		// 重置页码
		resetPagination(1);
		//
		doTheCardQuery();
	}

	// 排序 按钮等
	function pageNoAndQuery(pageNo) {
		// 变更页码
		resetPagination(pageNo);
		//
		doTheCardQuery();
	}

	// 弹出收支明细窗口
	function popupConsume(cardId, cardNo, cardCode, faceValue, cardName) {
		$("#cardNo").html(cardNo);
		$("#cardCode").html(cardName);
		$("#faceValue").html(faceValue);

		var ajax = Ajax.post("/ecard/eCardTransactRec/list/get");
		ajax.sync();
		ajax.params({
			cardId : cardId
		});
		ajax.done(function(result, jqXhr) {
			if (result.data == null) {
				return;
			}
			// 生成列表
			renderHtml(result.data, "eCardTransactRecRowsTpl", "eCardTrasactRecRows");
		});
		ajax.go();

		var dom = "#text-msg";
		Layer.dialog({
			dom : dom, //或者 html string
			title : "e卡收支明细",
			area : [ '600px', '400px' ],
			closeBtn : true,
			btn : false
		//默认为 btn : ["确定", "取消"]
		});
	}

	function popupMerge(cardId) {
		// 记录待合并的原卡ID
		cardIdToMerge = cardId;

		// 获取其他的e卡
		var ajax = Ajax.post("/ecard/userECard/list/other/get");
		ajax.params({
			cardId : cardId
		});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			var data = result.data;
			if (data == null) {
				return;
			}
			// 生成列表
			renderHtml(data, "otherUserECardsTpl", "otherUserECards");
		});
		ajax.go();

		// 对话框
		var dom = "#text-merge";
		var yesHandler = function(layerIndex) {
			var ajax = Ajax.post("/ecard/userECard/merge/do");
			ajax.params({
				cardIdToMerge : cardIdToMerge,
				cardIdToBeMerged : cardIdToBeMerged
			});
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					theLayer.hide();
					Layer.msgInfo("恭喜您！合并成功，原卡余额已转移至目标卡，原卡已作废处理！");
					getValidCards();
					//location.href = getAppUrl("/ucenter/ecard/list/jsp");
				} else {
					theLayer.hide();
					Layer.warning(result.message);
				}
			});
			ajax.go();
		};
		var theLayer = Layer.dialog({
			dom : dom, //或者 html string
			title : "选择目标卡",
			area : [ '600px', '400px' ],
			closeBtn : true,
			yes : yesHandler,
			no : function() {
				theLayer.hide();
			}
		});
	}

	function popupPresent(cardId) {
		var dom = "#text-present";

		textSet("verfCode", "");
		textSet("doneePhone", "");
		disableSendBtn(isCountingDown);
		//$("#btnSendSms").attr("disabled", false);

		var yesHandler = function(layerIndex) {
			var verfCode = textGet("verfCode");
			if (isNullOrEmpty(verfCode)) {
				Layer.msgWarning("请输入短信验证码");
				return;
			}
			var mobile = textGet("doneePhone");
			if (isNullOrEmpty(mobile)) {
				Layer.msgWarning("请输入受赠人手机号");
				return;
			}
			if (!isMobile(mobile)) {
				Layer.msgWarning("输入的受赠人手机号格式不正确");
				return;
			}
			var verfCode = textGet("verfCode");
			var doneePhone = textGet("doneePhone");
			var ajax = Ajax.post("/ecard/userECard/present/do");
			ajax.params({
				verfCode : verfCode,
				cardId : cardId,
				doneePhone : doneePhone
			});
			ajax.sync();
			ajax.done(function(result, jqXhr) {
				var data = result.data;
				if (data) {
					theLayer.hide();
					Layer.msgSuccess("恭喜您，转赠成功！");
					getValidCards();
				} else {
					Layer.msgWarning(result.message);
				}
			});
			ajax.go();
		};
		
		var theLayer = Layer.dialog({
			dom : dom, //或者 html string
			title : "转赠",
			area : [ '500px', '320px' ],
			closeBtn : true,
			yes : yesHandler,
			no : function() {
			}
		});
	}

	function cardMergeSelect(obj, cardId) {
		// 记录待合并的目标卡ID
		cardIdToBeMerged = cardId;
		// 显示选中状态
		$(".tr-selected").removeClass("tr-selected");
		$(obj).addClass("tr-selected");
		$(obj).find(":radio").attr("checked", true);
	}
	
	function deleteECard(cardId) {
		var ajax = Ajax.post("/ecard/userECard/delete/do");
		ajax.params({
			cardId : cardId
		});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			var data = result.data;
			if (data) {
				Layer.msgSuccess("删除成功");
				getInvalidCards();
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}

	$(function() {
		// 默认先获取可用e卡列表
		getValidCards();
		bindHoverEvent("userECardRows", "tbody tr td div.present-info");
	})
</script>
</body>
<script type="text/html" id="otherUserECardsTpl">
	{{# var userECards = d; }}
		<tr class="title title1">
            <td>卡号</td>
            <td>卡类型</td>
            <td>面额</td>
            <td>余额</td>
        </tr>
	{{# for(var i = 0, len = userECards.length; i < len; i++) { }}
		<tr onclick="cardMergeSelect(this, {{ userECards[i].id }});">
            <td><div class="text-left"><input name="otherCard" type="radio" />{{ userECards[i].cardNo }}</div></td>
            <td>{{ userECards[i].eCard.name }}</td>
            <td>¥{{ userECards[i].faceValue }}</td>
            <td>¥{{ userECards[i].remainVal }}</td>
        </tr>
	{{# } }}
</script>

<script type="text/html" id="eCardTransactRecRowsTpl">
	{{# var eCardTransactRecs = d; }}
		<tr class="title title1">
			<td>时间</td>
			<td>订单编号</td>
			<td>收支金额</td>
			<td>余额</td>
		</tr>
	{{# for(var i = 0, len = eCardTransactRecs.length; i < len; i++) { }}
		<tr>
			<td>{{ eCardTransactRecs[i].theTime }}</td>
			<td>{{ eCardTransactRecs[i].targetStr || "合并操作无订单" }}</td>
			<td>{{ eCardTransactRecs[i].directFlag == 1 ? "" : "-" }}¥{{ eCardTransactRecs[i].theVal }}</td>
			<td>¥{{ eCardTransactRecs[i].remainVal }}</td>
		</tr>
	{{# } }}
</script>

<script type="text/html" id="userECardRowsTpl">
	{{# var userECards = d; }}
		<tr class="title title1">
			<td width="240">名称</td>
			<td>余额</td>
			<td>购买时间</td>
			<td>适用于</td>
			<td>使用状态</td>
			<td>操作</td>
    	</tr>
	{{# for(var i = 0, len = userECards.length; i < len; i++) { }}
		<tr>
			<td>
				<div class="e-card">
					<img src="{{ userECards[i].fileBrowseUrl }}" alt="E卡" />
					<div class="card-amount">¥&nbsp;{{ userECards[i].faceValue }}</div>
					<div class="card-num">{{ userECards[i].cardNo }}</div>
				</div>
			</td>
			<td>¥{{ userECards[i].remainVal }}</td>
			<td>{{ userECards[i].buyTime }}
				{{# if (userECards[i].eCardTransferRec != null) { }}
					<div name="present-info" class="present-info">
						<ul class="detail-ul">
							<li><span>转赠人名称：</span>{{ userECards[i].eCardTransferRec.userNameFrom }}</li>
							<li><span>转赠人手机：</span>{{ userECards[i].eCardTransferRec.userPhoneFrom }}</li>
							<li><span>转赠时间：</span>{{ userECards[i].eCardTransferRec.ts }}</li>
						</ul>
					</div>
				{{# } }}
			</td>
			{{# if (userECards[i].shopId != null) { }}
			<td><a href="javascript:void(0);" onclick="window.open(getAppUrl('/shop/detail/jsp?shopId={{ userECards[i].shopId }}'));">{{ strMaxWidth(userECards[i].shopName, 8) || "" }}</a></td>
			{{# } else { }}
			<td><span>全平台</span></td>
			{{# } }}
			<td>
				<div class="gray">{{# if (!isInvalid) { }} 可使用 {{# } else { }} 已作废 {{# } }}</div>
					<a class="anormal" href="javascript:void(0);" 
						onclick="popupConsume({{ userECards[i].id }}, 
												'{{ userECards[i].cardNo }}', 
												'{{ userECards[i].cardCode }}', 
												'{{ userECards[i].faceValue }}',
												'{{ userECards[i].eCard.name }}');">收支明细</a>
			</td>
			<td>
				<div class="card-btnbox">
					{{# if (!isInvalid) { }}
						{{# if(userECards[i].shopId == null) { }}
						<input class="btn-normal btn-w90" onclick="popupMerge('{{ userECards[i].id }}');" type="button"
							value="合并到其它卡" /><br />
						{{# } }}
						<input class="btn-normal btn-w90" onclick="popupPresent('{{ userECards[i].id }}');"
							type="button" value="转赠" />
					{{# } else { }}
						<input class="btn-normal btn-w90" type="button" onclick="deleteECard('{{ userECards[i].id }}')"
							value="删除" />
					{{# } }}
				</div>
			</td>
		</tr>
	{{# } }}
</script>

<script type="text/html" id="eCardTransferRecRowsTpl">
	{{# var eCardTransferRecs = d; }}
		<tr class="title title1">
			<td width="240">名称</td>
			<td>余额</td>
			<td>转赠时间</td>
			<td>受赠人名称</td>
			<td>受赠人手机号</td>
			<td>使用状态</td>
		</tr>
	{{# for(var i = 0, len = eCardTransferRecs.length; i < len; i++) { }}
		<tr>
			<td>
				<div class="e-card">
					<img src="{{ eCardTransferRecs[i].fileBrowseUrl }}" alt="E卡" />
					<div class="card-num">{{ eCardTransferRecs[i].cardNo }}</div>
					<div class="card-amount">¥&nbsp;{{ eCardTransferRecs[i].faceValue }}</div>
				</div>
			</td>
			<td>¥{{ eCardTransferRecs[i].remainVal }}</td>
			<td>{{ eCardTransferRecs[i].ts }}</td>
			<td>{{ eCardTransferRecs[i].userNameTo }}</td>
			<td>{{ eCardTransferRecs[i].userPhoneTo }}</td>
			<td>
				<div class="gray">已转赠</div>
			</td>
		</tr>
	{{# } }}
</script>
</html>
