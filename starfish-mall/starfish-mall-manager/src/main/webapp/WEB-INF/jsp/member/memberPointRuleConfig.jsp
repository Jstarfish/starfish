<%@page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
<title>会员积分规则设置</title>
<style type="text/css">
table.gridtable {
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
	width: 590px;
	position: fixed;
}

table.gridtable th {
	border:1px solid #AAA;
	border-width: 1px;
	padding: 3px;
	border-style: solid;
	border-color: #666666;
	background-color: #dedede;
	height: 28px;
	line-height: 28px;
}

table.gridtable td {
	border:1px solid #AAA;
	border-width: 1px;
	padding: 3px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
	height: 28px;
	line-height: 28px;
	
}

.ui-spinner {
	height: 30px;
	width: 60px;
}

</style>
</head>

<body id="rootPanel">
	<div id="mainPanel" class="ui-layout-center" style="padding: 0px;">
		<div id="tabs" class="noBorder">
			<ul style="border-bottom-color:#ccc;">
				<li><a href="#trade_point_li">消费积分</a></li>
				<li style="display:none;"><a href="#login_point_li">登录积分</a></li>
				<li style="display:none;"><a href="#signIn_point_li">签到积分</a></li>
				<li style="display:none;"><a href="#point_upper_limit">每日积分上限</a></li>
				
				<div class="normal group right aligned">
					<button id="saveBtn" class="normal button">
						保存
					</button>
					<span class="normal spacer"></span>
					<button id="cancelBtn" class="normal button">
						取消
					</button>
				</div>
			</ul>
			
			<div id="trade_point_li">
				<div class="form">
					<div class="field row">
						<label class="field one and half wide label left align">兑换积分比例：</label>
						<label class="field one and half wide label">1元人民币兑换</label>
						<div class="field group">
							<input id="exchange_ratio" data-for="" data-code="trade.exchange.ratio" class="field half wide value"/>
							<label>积分</label>
						</div>
					</div>
					
					<div class="field row">
						<table class="gridtable">
							<thead><tr>
								<th width="284px;"><label class="normal label">会员等级</label></th>
								<th width="305px;"><label class="normal label">积分倍率</label></th>
							</tr></thead>
							<tbody>
								<tr>
									<td align="center"><label>注册会员</label></td>
									<td align="right">
										<input id="grade1_ratio" data-for="" data-code="trade.reward.grade1.ratio" maxlength="5" style="width: 48px;" class="spinner"/>
										<label class="normal label">倍</label>
									</td>
								</tr>
								<tr>
									<td align="center"><label>普通会员</label></td>
									<td align="right">
										<input id="grade2_ratio" data-for="" data-code="trade.reward.grade2.ratio" maxlength="5" style="width: 48px;" class="spinner"/>
										<label class="normal label">倍</label>
									</td>
								</tr>
								<tr>
									<td align="center"><label>白银会员</label></td>
									<td align="right">
										<input id="grade3_ratio" data-for="" data-code="trade.reward.grade3.ratio" maxlength="5" style="width: 48px;" class="spinner"/>
										<label class="normal label">倍</label>
									</td>
								</tr>
								<tr>
									<td align="center"><label>黄金会员</label></td>
									<td align="right">
										<input id="grade4_ratio" data-for="" data-code="trade.reward.grade4.ratio" maxlength="5" style="width: 48px;" class="spinner"/>
										<label class="normal label">倍</label>
									</td>
								</tr>
								<tr>
									<td align="center"><label>钻石会员</label></td>
									<td align="right">
										<input id="grade5_ratio" data-for="" data-code="trade.reward.grade5.ratio" maxlength="5" style="width: 48px;" class="spinner"/>
										<label class="normal label">倍</label>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					
				</div>
			</div>
			<div id="login_point_li" style="display:none;">
				<div class="form">
					<div class="field row">
						<label class="field one wide label left align">使用状态</label>
						<div class="field group">
							<input id="enabled_T" type="radio" name="enabled" value="true" />
							<label for="enabled_T">启用</label>
							<input id="enabled_F" type="radio" name="enabled" value="false" />
							<label for="enabled_F">禁用</label>
						</div>
					</div>
					
					<div class="field row">
						<table class="gridtable" style="display:none;">
							<thead><tr>
								<th width="25%"><label class="normal label">会员等级</label></th>
								<th width="25%"><label class="normal label">每日登录积分</label></th>
								<th width="25%"><label class="normal label">连续登录积分百分比</label></th>
								<th width="25%"><label class="normal label">连续登录积分增长周期</label></th>
							</tr></thead>
							<tbody>
								<tr>
									<td align="center"><label>注册会员</label></td>
									<td align="right">
										<div class="field group">
											<input id="grade1_login" value="10" style="width: 80px;" class="normal input"/>
											<label class="normal label">积分</label>
										</div>
									</td>
									<td align="right">
										<input value="10" class="spinner"/>
										<label class="normal label">%</label>
									</td>
									<td align="right">
										<div class="field group">
											<input value="7" class="normal input" style="width: 40px;"/>
											<label class="normal label">天</label>
										</div>
									</td>
								</tr>
								<tr>
									<td align="center"><label>普通会员</label></td>
									<td align="right">
										<div class="field group">
											<input id="grade1_login" value="10" style="width: 80px;" class="normal input"/>
											<label class="normal label">积分</label>
										</div>
									</td>
									<td align="right">
										<input value="10" class="spinner"/>
										<label class="normal label">%</label>
									</td>
									<td align="right">
										<div class="field group">
											<input value="7" class="normal input" style="width: 40px;"/>
											<label class="normal label">天</label>
										</div>
									</td>
								</tr>
								<tr>
									<td align="center"><label>白银会员</label></td>
									<td align="right">
										<div class="field group">
											<input id="grade1_login" value="10" style="width: 80px;" class="normal input"/>
											<label class="normal label">积分</label>
										</div>
									</td>
									<td align="right">
										<input value="10" class="spinner"/>
										<label class="normal label">%</label>
									</td>
									<td align="right">
										<div class="field group">
											<input value="7" class="normal input" style="width: 40px;"/>
											<label class="normal label">天</label>
										</div>
									</td>
								</tr>
								<tr>
									<td align="center"><label>黄金会员</label></td>
									<td align="right">
										<div class="field group">
											<input id="grade1_login" value="10" style="width: 80px;" class="normal input"/>
											<label class="normal label">积分</label>
										</div>
									</td>
									<td align="right">
										<input value="10" class="spinner"/>
										<label class="normal label">%</label>
									</td>
									<td align="right">
										<div class="field group">
											<input value="7" class="normal input" style="width: 40px;"/>
											<label class="normal label">天</label>
										</div>
									</td>
								</tr>
								<tr>
									<td align="center"><label>钻石会员</label></td>
									<td align="right">
										<div class="field group">
											<input id="grade1_login" value="10" style="width: 80px;" class="normal input"/>
											<label class="normal label">积分</label>
										</div>
									</td>
									<td align="right">
										<input value="10" class="spinner"/>
										<label class="normal label">%</label>
									</td>
									<td align="right">
										<div class="field group">
											<input value="7" class="normal input" style="width: 40px;"/>
											<label class="normal label">天</label>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					
				</div>
			</div>
			<div id="signIn_point_li" style="display:none;">
				<div class="form">
					<div class="field row">
						<label class="field one wide label left align">使用状态</label>
						<div class="field group">
							<input id="enabled_T" type="radio" name="enabled" value="true" />
							<label for="enabled_T">启用</label>
							<input id="enabled_F" type="radio" name="enabled" value="false" />
							<label for="enabled_F">禁用</label>
						</div>
					</div>
					
					<div class="field row">
						<table class="gridtable" style="display:none;">
							<thead><tr>
								<th width="25%"><label class="normal label">会员等级</label></th>
								<th width="25%"><label class="normal label">每日签到积分</label></th>
								<th width="25%"><label class="normal label">连续签到积分百分比</label></th>
								<th width="25%"><label class="normal label">连续签到积分增长周期</label></th>
							</tr></thead>
							<tbody>
								<tr>
									<td align="center"><label>注册会员</label></td>
									<td align="right">
										<div class="field group">
											<input id="grade1_login" value="10" style="width: 80px;" class="normal input"/>
											<label class="normal label">积分</label>
										</div>
									</td>
									<td align="right">
										<input value="10" class="spinner"/>
										<label class="normal label">%</label>
									</td>
									<td align="right">
										<div class="field group">
											<input value="7" class="normal input" style="width: 40px;"/>
											<label class="normal label">天</label>
										</div>
									</td>
								</tr>
								<tr>
									<td align="center"><label>普通会员</label></td>
									<td align="right">
										<div class="field group">
											<input id="grade1_login" value="10" style="width: 80px;" class="normal input"/>
											<label class="normal label">积分</label>
										</div>
									</td>
									<td align="right">
										<input value="10" class="spinner"/>
										<label class="normal label">%</label>
									</td>
									<td align="right">
										<div class="field group">
											<input value="7" class="normal input" style="width: 40px;"/>
											<label class="normal label">天</label>
										</div>
									</td>
								</tr>
								<tr>
									<td align="center"><label>白银会员</label></td>
									<td align="right">
										<div class="field group">
											<input id="grade1_login" value="10" style="width: 80px;" class="normal input"/>
											<label class="normal label">积分</label>
										</div>
									</td>
									<td align="right">
										<input value="10" class="spinner"/>
										<label class="normal label">%</label>
									</td>
									<td align="right">
										<div class="field group">
											<input value="7" class="normal input" style="width: 40px;"/>
											<label class="normal label">天</label>
										</div>
									</td>
								</tr>
								<tr>
									<td align="center"><label>黄金会员</label></td>
									<td align="right">
										<div class="field group">
											<input id="grade1_login" value="10" style="width: 80px;" class="normal input"/>
											<label class="normal label">积分</label>
										</div>
									</td>
									<td align="right">
										<input value="10" class="spinner"/>
										<label class="normal label">%</label>
									</td>
									<td align="right">
										<div class="field group">
											<input value="7" class="normal input" style="width: 40px;"/>
											<label class="normal label">天</label>
										</div>
									</td>
								</tr>
								<tr>
									<td align="center"><label>钻石会员</label></td>
									<td align="right">
										<div class="field group">
											<input id="grade1_login" value="10" style="width: 80px;" class="normal input"/>
											<label class="normal label">积分</label>
										</div>
									</td>
									<td align="right">
										<input value="10" class="spinner"/>
										<label class="normal label">%</label>
									</td>
									<td align="right">
										<div class="field group">
											<input value="7" class="normal input" style="width: 40px;"/>
											<label class="normal label">天</label>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					
				</div>
			</div>
			<div id="point_upper_limit" style="display:none;">
				<div class="form">
					<div class="field row">
						<label class="field one wide label left align">使用状态</label>
						<div class="field group">
							<input id="enabled_T" type="radio" name="enabled" value="true" />
							<label for="enabled_T">启用</label>
							<input id="enabled_F" type="radio" name="enabled" value="false" />
							<label for="enabled_F">禁用</label>
						</div>
					</div>
					
					<div class="field row">
						<table class="gridtable" style="display:none;">
							<thead><tr>
								<th width="284px;"><label class="normal label">会员等级</label></th>
								<th width="305px;"><label class="normal label">每日积分上限</label></th>
							</tr></thead>
							<tbody>
								<tr>
									<td align="center"><label>注册会员</label></td>
									<td align="right">
										<div class="field group">
											<input value="————" class="normal input" style="width: 80px;"/>
											<label class="normal label">积分</label>
										</div>
									</td>
								</tr>
								<tr>
									<td align="center"><label>普通会员</label></td>
									<td align="right">
										<div class="field group">
											<input value="————" class="normal input" style="width: 80px;"/>
											<label class="normal label">积分</label>
										</div>
									</td>
								</tr>
								<tr>
									<td align="center"><label>白银会员</label></td>
									<td align="right">
										<div class="field group">
											<input value="————" class="normal input" style="width: 80px;"/>
											<label class="normal label">积分</label>
										</div>
									</td>
								</tr>
								<tr>
									<td align="center"><label>黄金会员</label></td>
									<td align="right">
										<div class="field group">
											<input value="————" class="normal input" style="width: 80px;"/>
											<label class="normal label">积分</label>
										</div>
									</td>
								</tr>
								<tr>
									<td align="center"><label>钻石会员</label></td>
									<td align="right">
										<div class="field group">
											<input value="————" class="normal input" style="width: 80px;"/>
											<label class="normal label">积分</label>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					
				</div>
			</div>
		</div>
	</div>
		
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
	//实例化表单代理
	var formProxy = FormProxy.newOne();
	var jqTabsCtrl = null;
	//注册表单控件
	formProxy.addField({
		id : "exchange_ratio",
		required : true,
		type : "float",
		rules : ["isNum"]
	});
	formProxy.addField({
		id : "grade1_ratio",
		required : true,
		type : "float",
		rules : ["isNum"]
	});
	formProxy.addField({
		id : "grade2_ratio",
		required : true,
		type : "float",
		rules : ["isNum"]
	});
	formProxy.addField({
		id : "grade3_ratio",
		required : true,
		type : "float",
		rules : ["isNum"]
	});
	formProxy.addField({
		id : "grade4_ratio",
		required : true,
		type : "float",
		rules : ["isNum"]
	});
	formProxy.addField({
		id : "grade5_ratio",
		required : true,
		type : "float",
		rules : ["isNum"]
	});
	
	$(function() {
		//页面基本布局
		$id('rootPanel').layout({
			spacing_open : 1,
			spacing_closed : 1,
			allowTopResize : false
		});
		//加载tab页面
		jqTabsCtrl = $( "#tabs" ).tabs();
		//商品tab页签选中时触发
		jqTabsCtrl.on("tabsactivate", function(event, ui) {
			var currentHref = ui.newPanel.selector;
			var panlId = currentHref.substring(currentHref.indexOf("#")+1, currentHref.length);
			var table = $id(panlId).find("table.gridtable");
			var table_display = table.css("display");
			if(table_display == "none"){
				table.css("display","block");
			}
		});
		
		loadData();
		
		$(".spinner").spinner({
			min: 1.00,
			max: 100,
			step: 0.01
		});
		
		$id("saveBtn").click(function(){
			saveTradeRule();
		});
		
		$id("cancelBtn").click(function(){
			loadData();
		});

		winSizeMonitor.start(adjustCtrlsSize);
		
	});
	
	function saveTradeRule(){
		//
		var vldResult = formProxy.validateAll();
		if (vldResult) {
			var postData = [];
			var keyMap = new KeyMap();
			keyMap.add("code", $id("exchange_ratio").attr("data-code"));
			keyMap.add("value", $id("exchange_ratio").val());
			postData.add(keyMap);

			keyMap = new KeyMap();
			keyMap.add("code", $id("grade1_ratio").attr("data-code"));
			keyMap.add("value", $id("grade1_ratio").val());
			postData.add(keyMap);

			keyMap = new KeyMap();
			keyMap.add("code", $id("grade2_ratio").attr("data-code"));
			keyMap.add("value", $id("grade2_ratio").val());
			postData.add(keyMap);
			
			keyMap = new KeyMap();
			keyMap.add("code", $id("grade3_ratio").attr("data-code"));
			keyMap.add("value", $id("grade3_ratio").val());
			postData.add(keyMap);
			
			keyMap = new KeyMap();
			keyMap.add("code", $id("grade4_ratio").attr("data-code"));
			keyMap.add("value", $id("grade4_ratio").val());
			postData.add(keyMap);
			
			keyMap = new KeyMap();
			keyMap.add("code", $id("grade5_ratio").attr("data-code"));
			keyMap.add("value", $id("grade5_ratio").val());
			postData.add(keyMap);
			
			var hintBox = Layer.progress("正在保存数据...");

			var ajax = Ajax.post("/member/point/rule/save/do");
			ajax.data(postData);

			ajax.done(function(result, jqXhr) {
				//
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					loadData();
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
	
	function loadData(){

		var ajax = Ajax.post("/member/point/rule/list/get");

		ajax.done(function(result, jqXhr) {
			//
			if (result.type == "info") {
				var data = result.data;
				$.each(data, function(){
					var code = $(this)[0].code;
					var val = $(this)[0].value;
					var id = $(this)[0].id;
					$("input[data-code]").each(function(){
						if( code == $(this).attr("data-code")){
							$(this).val(val);
							$(this).attr("data-for", id);
						}
					});
					
				});
				
				
			}else {
				Layer.msgWarning("加载异常!");
			}

		});
		
		ajax.go();
	}
	
	//调整控件大小
	function adjustCtrlsSize() {
		var jqMainPanel = $id("mainPanel");
		var mainWidth = jqMainPanel.width();
		var mainHeight = jqMainPanel.height();
		console.log("mainWidth:" + mainWidth + ", " + "mainHeight:" + mainHeight);
		//
		var tabsCtrlWidth = mainWidth - 4;
		var tabsCtrlHeight = mainHeight - 8 - 38;
		jqTabsCtrl.width(tabsCtrlWidth);
		var tabsHeaderHeight = $(">[role=tablist]", jqTabsCtrl).height();
		var tabsPanels = $(">[role=tabpanel]", jqTabsCtrl);
		tabsPanels.width(tabsCtrlWidth - 4 * 2);
		tabsPanels.height(tabsCtrlHeight - tabsHeaderHeight - 30);
	}

	
	</script>
</body>

</html>
