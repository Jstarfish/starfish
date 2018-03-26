<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@include file="../include/base.jsf" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>入口选择</title>
<style type="text/css">
#mainPanel {
	padding: 0;
}

#mainPanel>.filter.section {
	margin-top: -7px;
}

.entrance-selct {
	width: 650px;
	min-height: 50px;
	margin: 0 auto;
}

.entry-section {
	border: 1px solid #dbdbdb;
	position: relative;
	margin-bottom: 10px;
}

.entry-section .section-header {
	position: absolute;
	left: 0;
	top: 0;
	bottom: 0;
	width: 66px;
	color: #7e7e7e;
	background-color: #f3f3f3;
	text-align: center;
	border-right: 1px solid #dbdbdb;
}

.entry-section .section-header span {
	position: absolute;
	left: 0;
	right: 0;
	top: 50%;
	margin-top: -8px;
}

.entry-section .section-body {
	margin-left: 66px;
	padding: 4px 6px 8px 10px;
	color: #a5a5a5;
}

.entry-item {
	padding-top: 4px;
	border-top: 1px solid #f3f3f3;
	margin-top: 4px;
}

.entry-item.first {
	border-top: 0;
	margin-top: 0;
}

.entry-item>dl {
	overflow: hidden;
	border: 1px solid #fff;
	border-left-width: 8px;
	padding: 9px 0 9px 6px;
	position: relative;
	cursor: pointer;
	text-align: left;
}

.entry-item>dl:after {
	content: "";
	position: absolute;
	top: 5px;
	right: 188px;
	bottom: 5px;
	width: 1px;
	background: #f3f3f3;
}

.entry-item>dl.hover {
	border-color: #ffc100;
}

.entry-logo img {
	width: 106px;
	height: 75px;
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	border-radius: 5px;
	display: block;
}

.entry-logo {
	float: left;
}

.entry-detail h3 {
	font-weight: normal;
	line-height: 28px;
	color: #031f61;
}

.entry-detail p {
	font-size: 0.86em;
}

.entry-detail {
	margin-left: 124px;
	margin-right: 188px;
	line-height: 25px;
}

.entry-desc {
	float: right;
	width: 180px;
	height: 75px;
	display: table;
	font-size: 0.86em;
	text-align: center;
}

.entry-desc span {
	display: table-cell;
	vertical-align: middle;
}

.entry-section.mall .entry-detail h3 {
	margin-bottom: 20px;
}

.green {
	color: #00b151;
}

.yellow {
	color: #ffc100;
}

.red {
	color: #c20000;
}

.btn {
	border: 0;
	color: #333;
	font-size: 0.86em;
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	border-radius: 5px;
	cursor: pointer;
}

.btn.big {
	width: 234px;
	height: 28px;
	line-height: 28px;
}

.btn.yellow {
	background: #fcc20c;
	color: #333;
}

.btn.yellow:hover {
	background: #ffd23c;
}

.btn.yellow:active {
	background: #e5ad00;
}

.entry-submit {
	padding-top: 12px;
	padding-bottom: 4px;
}

.entry-item.disabled .entry-logo img {
	filter: alpha(opacity = 40);
	-moz-opacity: 0.4;
	opacity: 0.4;
}

.entry-item.disabled .entry-detail h3 {
	font-style: italic;
	color: #BBB;
}
</style>
</head>
<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" align="center">
		<div class="filter section" style="text-align: center;">
			<div class="filter row center">
				<div class="simple block">
					<div class="header">
						<div class="ui-layout-center entrance-selct">
							<label>欢迎进入管理后台</label>
							<span style="float: right;font-weight:normal;">[<a style="display: inline-block;text-decoration:none;font-size: 12px;" href="<%=appBaseUrl%>/user/logout/do">&nbsp;退出&nbsp;</a>]</span>
						</div>
					</div>
				</div>
			</div>
			<span class="hr divider"></span>
			<div class="filter row center">您可以选择进入</div>
		</div>
		<div id="content" class="ui-layout-center entrance-selct"></div>
	</div>

	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
		//获取所有角色
		function loadData() {
			var ajax = Ajax.post("/user/entry/list/get");
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var resultData = result.data;
					console.log(JSON.encode(resultData));
					var mall = resultData.mall;
					var shops = resultData.shops;
					var agencies = resultData.agencies;
					//
					if (mall) {
						renderDetail(mall, "mallEntityTpl");
					}
					if (shops) {
						renderDetail(shops, "shopEntityTpl");
					}
					if (agencies) {
						renderDetail(agencies, "agenciesEntityTpl");
					}
				} else {
					Layer.warning(result.message);
					setPageUrl(getAppUrl("/user/login/jsp"));
				}

			});
			ajax.go();
		}

		//渲染
		function renderDetail(data, fromId) {
			var tplHtml = $id(fromId).html();
			var theTpl = laytpl(tplHtml);
			var htmlStr = theTpl.render(data);
			$id("content").append(htmlStr);
		}

		//存储选择入口
		function entryEntity(entityId, scope, status) {
			if (!isNoB(status)) {
				if (scope == "shop") {
					if (status == 5) {
						Toast.show("亲，您的店铺已关闭~", 2000, "warning");
						return;
					} else {
						if (status == -2) {
							Toast.show("亲，店铺已被禁用，请及时联系商城客服处理吧~", 2000, "warning");
							return;
						} else if (status != 2) {
							Toast.show("亲，还未通过最后审核~", 2000, "warning");
							return;
						}
					}

				} else if (scope == "agency") {
					if (status != 1) {
						Toast.show("亲，还未通过最后审核~", 2000, "warning");
						return;
					}
				}
			}
			//
			var postData = {
				entityId : ParseInt(entityId),
				scope : scope
			};
			var ajax = Ajax.post("/user/entry/target/get");
			var loaderLayer = Layer.loading("正在进入...");
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				//隐藏等待提示框
				loaderLayer.hide();
				if (result.type == "info") {
					Layer.progress("正在进入。。。");
					setPageUrl(getAppUrl(result.data));
				} else {
					Layer.warning(result.message);
					setPageUrl(getAppUrl(result.data));
				}
			});
			ajax.fail(function(result, jqXhr) {
				//隐藏等待提示框
				loaderLayer.hide();
			});
			ajax.go();
		}

		//------------------------------------初始化代码--------------------------------------
		$(function() {
			//页面基本布局
			$id('rootPanel').layout({
				spacing_open : 1,
				spacing_closed : 1,
				north__size : 60,
				allowTopResize : false
			});
			//
			loadData();
			//
			//多入口 list鼠标滑过
			$id("content").on("mouseover", ".entry-item>dl", function() {
				$(this).addClass("hover");
			});
			$id("content").on("mouseout", ".entry-item>dl", function() {
				$(this).removeClass("hover");
			});

		});

		//渲染
		function goMerchSetting() {
			setPageUrl(getAppUrl("/merch/entering/jsp"));
		}
	</script>
</body>
<script type="text/html" id="mallEntityTpl" title="商城展示">
	<dl class="entry-section mall">
		<dt class="section-header"><span>商城</span></dt>
		<dd class="section-body">
			<ul class="entry-list">
				<li class="entry-item first" onclick="entryEntity({{ d.id }},'mall', null)">
					<dl>
						<dt class="entry-logo"><img src="{{ d.logoPath }}" alt="商城logo" /></dt>
						<dd class="entry-desc"><span>{{ d.bizScope ? d.bizScope : "" }}</span></dd>
						<dd class="entry-detail"><h3>{{ d.name }}</h3><p>{{ d.desc ? d.bizScope : "" }}</p></dd>
					</dl>
				</li>
			</ul>
		</dd>
	</dl>
</script>
<script type="text/html" id="shopEntityTpl" title="店铺展示">
	<dl class="entry-section">
		<dt class="section-header"><span>店铺</span></dt>
		<dd class="section-body">
			<ul class="entry-list">
				{{# for(var i = 0, len = d.length; i < len; i++){ 
						var entryItemClass = "entry-item";
						var statusClass;
						var statusText;
						var status = d[i].auditStatus;
						if(i==0){entryItemClass = "entry-item first";}
						if (d[i].closed == 1){
							entryItemClass = entryItemClass + " disabled";
							statusClass = "gray";
							statusText = "已关闭";
							status = 5;
							} else {
							if (d[i].disabled == 1){
								entryItemClass = entryItemClass + " disabled";
								statusClass = "red";
								statusText = "已禁用";
								status = -2;
							}else{
								switch (status){
									case 0 :
										statusClass = "yellow";
										statusText = "未审核";
										break;
									case 1 :
										statusClass = "yellow";
										statusText = "初审通过";
										break;
									case 2 :
										statusClass = "green";
										statusText = "正常";
										break;
									case -1 :
										statusClass = "red";
										statusText = "审核未通过";
										break;
							}
							}
						}
					}}	

				<li class="{{ entryItemClass }}" onclick="entryEntity({{ d[i].id }},'shop', {{status}})">
					<dl>
						<dt class="entry-logo"><img src="{{ d[i].logoPath }}" alt="" /></dt>
						<dd class="entry-desc"><span>{{ d[i].bizScope }}</span></dd>
						<dd class="entry-detail"><h3>{{ d[i].name }}</h3><p>申请：{{ d[i].applyTime }}</p><p>状态：<span class="{{ statusClass }}">{{ statusText }}</span></p></dd>
					</dl>
				</li>
				{{# } }}
				{{# if(d.length == 0){ }}
				<li class="entry-item entry-submit">
					<input class="btn yellow big" type="button" onclick="goMerchSetting();" value="申请店铺" />
				</li>
				{{# }else{ }}
				<li class="entry-item entry-submit">
					<input class="btn yellow big" type="button" onclick="goMerchSetting();" value="申请新店铺" />
				</li>
				{{# } }}
			</ul>
		</dd>
	</dl>
</script>
<script type="text/html" id="agenciesEntityTpl" title="代理商">
	<dl class="entry-section">
		<dt class="section-header"><span>代理处</span></dt>
		<dd class="section-body">
			<ul class="entry-list">
				{{# for(var i = 0, len = d.length; i < len; i++){
						var entryItemClass = "entry-item";
						var statusClass;
						var statusText;
						var status = d[i].auditStatus;
						if(i==0){entryItemClass = "entry-item first";}

						switch (status){
							case 0 :
								statusClass = "yellow";
								statusText = "未审核";
								break;
							case 1 :
								statusClass = "yellow";
								statusText = "初审通过";
								break;
							case 2 :
								statusClass = "green";
								statusText = "正常";
								break;
							case -1 :
								statusClass = "red";
								statusText = "审核未通过";
								break;
						}
				}}

				<li class="{{ entryItemClass }}" onclick="entryEntity({{ d[i].id }},'agency', {{status}})">
					<dl>
						<dt class="entry-logo"><img src="{{ d[i].logoPath }}" alt="" /></dt>
						<dd class="entry-desc"><span>{{ d[i].bizScope }}</span></dd>
						<dd class="entry-detail"><h3>{{ d[i].name }}</h3><p>申请：{{ d[i].applyTime }}</p><p>状态：<span class="{{ statusClass }}">{{ statusText }}</span></p></dd>
					</dl>
				</li>

				{{# } }}
				{{# if(d.length == 0){ }}
				<li class="entry-item entry-submit">
					<input class="btn yellow big" type="button" value="申请代理处" />
				</li>
				{{# }else{ }}
				<li class="entry-item entry-submit">
					<input class="btn yellow big" type="button" value="申请新代理处" />
				</li>
				{{# } }}
			</ul>
		</dd>
	</dl>
</script>
</html>