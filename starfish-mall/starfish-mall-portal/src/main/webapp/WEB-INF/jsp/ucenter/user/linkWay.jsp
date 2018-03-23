<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/head.jsp" %>
<div class="content">
	<div class="page-width">
		<div class="crumbs">
			<a href="">首页</a>&gt;<a href="">账户设置</a>&gt;<a href="javascript:;">常用联系方式</a>
		</div>
		<div class="section">
			<jsp:include page="/WEB-INF/jsp/include/ucenterMenu.jsp" />
			<div class="section-right1">
                <div class="mod-main">
                    <div class="add-tit" >
                        <a class="btna-i" href="javascript:void(0);" id="topAddUserLinkWayBtn"><i>+</i><span>新增联系方式</span></a>
                        <%-- span class="gray ml10">您已增加5 个常用联系方式，最多可增加20个</span>--%>
                    </div>
                    <div class="add-cont" id="addLinkWay">
                        
                    </div>
                    <div class="add-cont" id="showLinkWay" >
                        
                    </div>
                    <%--div class="add-tit">
                        <a class="btna-i" href="javascript:void(0);" id="underAddUserLinkWayBtn"><i>+</i><span>增加常用联系方式</span></a>
                        <span class="gray ml10">您已增加5 个常用联系方式，最多可增加20个</span>
                    </div> --%>
                    <br/><br/><br/>
                </div>
            </div>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />   
<script type="text/javascript">
	//联系方式标识
	var aliasFlag = false;
	$(function() {
		
		getUserLinkWay();
		//添加联系方式
		$id("topAddUserLinkWayBtn").click(toAddUserLinkWay);
		//添加联系方式
		$id("underAddUserLinkWayBtn").click(toAddUserLinkWay);
	});
	
	//添加用户联系方式
	function toAddUserLinkWay() {
		getUserLinkWay();
		// 获取模板内容
		var tplHtml = $id("addLinkWayTpl").html();
		// 根据模板生成最终内容
		$id("addLinkWay").html(tplHtml);
		// 加载地区
		bindRegionLists("province", "city", "county", "town");
	}
	
	
	//获取用户联系方式
	function getUserLinkWay() {
		var postData = {};
		// ajax post 请求...
		var ajax = Ajax.post("/user/linkWay/list/get");
		ajax.sync();
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			var userLinkWayList = result.data;
			if (userLinkWayList != null) {
				// 获取模板内容
				var tplHtml = $id("linkWayTpl").html();
				// 生成/编译模板
				var htmlTpl = laytpl(tplHtml);
				// 根据模板和数据生成最终内容
				var htmlText = htmlTpl.render(userLinkWayList);
				$id("showLinkWay").html(htmlText);
				$id("addLinkWay").html("");
			}
			
		});
		ajax.go();
	}
	
	//编辑用户联系方式
	function toUpdateLinkWay(userLinkWay) {
		goback();
		// 获取模板内容
		var tplHtml = $id("updateLinkWayTpl").html();
		// 生成/编译模板
		var htmlTpl = laytpl(tplHtml);
		// 根据模板和数据生成最终内容
		var htmlText = htmlTpl.render(userLinkWay);
		$id("showLinkWayDialog"+userLinkWay.id).html(htmlText);
		$id("addLinkWay").html("");
		// 加载地区
		bindRegionLists("province", "city", "county", "town", [ userLinkWay.provinceId, userLinkWay.cityId, userLinkWay.countyId, userLinkWay.townId ]);
		
	}
	
	//
	function deleteLinkWay(id){
		var theLayer = Layer.confirm('确定要删除？', function() {
			//
			var hintBox = Layer.progress("正在删除...");
			var ajax = Ajax.post("/user/linkWay/delete/do");
			ajax.params({
				id : id
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					getUserLinkWay();
					//
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
	//
	function toSetDefaulted(id){
		var ajax = Ajax.post("/user/linkWay/set/defaulted/by/id");
		ajax.params({
			id : id
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				getUserLinkWay();
				//
			} else {
			}
		});
		ajax.always(function() {
		});
		ajax.go();
	}
	
	function addLinkWay(){
		//
		var postData = {}
		// 设置最后一级地区id
		var town = $("#town").val();
		var regionId = "";
		if(town == ""){
			regionId = $("#county").val();
		} else {
			regionId = $("#town").val();
		}
		var email = $("#email").val();
		var linkMan = $("#linkMan").val();
		if(!linkMan){
			$("#linkManTip").css("display", "");
			$("#linkManTip").html("请您填写联系人");
			return;
		}
		var phoneNo = $("#phoneNo").val();
		if(!phoneNo || !isMobile(phoneNo)){
			$("#phoneNoTip").css("display", "");
			$("#phoneNoTip").html("请您填写手机号码");
			return;
		}
		var address = $("#address").val();
		var alias = $("#alias").val();
		checkAlias(alias, -1);
		if(!alias){
			$("#aliasTip").css("display", "");
			$("#aliasTip").html("请您填写联系方式别名");
			return;
		}else if(aliasFlag){
			$("#aliasTip").css("display", "");
			$("#aliasTip").html("该联系方式别名已存在！");
			return;
		}
		if(email && !isValidEmail(email)){
			$("#emailTip").css("display", "");
			$("#emailTip").html("请正确填写邮箱");
			return;
		}
		postData = {
			email : email,
			regionId : regionId,
			phoneNo : phoneNo,
			linkMan : linkMan,
			address : address,
			alias : alias
		}
		var ajax = Ajax.post("/user/linkWay/save/do");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			//获取用户信息
			if (result.type == "info") {
				var member = result.data;
				//展示用户信息
				getUserLinkWay();
				Layer.msgSuccess(result.message);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.fail(function(result, jqXhr) {
			//隐藏等待提示框
			loaderLayer.hide();
		});
		ajax.go();
	}
	
	function updateLinkWay(id){
		//
		var postData = {}
		// 设置最后一级地区id
		var town = $("#town").val();
		var regionId = "";
		if(town == ""){
			regionId = $("#county").val();
		} else {
			regionId = $("#town").val();
		}
		var email = $id("email").val();
		
		var linkMan = $id("linkMan").val();
		if(!linkMan){
			$id("linkManTip").css("display", "");
			$id("linkManTip").html("请您填写联系人");
			return;
		}
		var phoneNo = $id("phoneNo").val();
		if(!phoneNo || !isMobile(phoneNo)){
			$id("phoneNoTip").css("display", "");
			$id("phoneNoTip").html("请您填写手机号码");
			return;
		}
		var address = $id("address").val();
		var alias = $id("alias").val();
		
		checkAlias(alias, id);
		if(!alias){
			$("#aliasTip").css("display", "");
			$("#aliasTip").html("请您填写联系方式别名");
			return;
		}else if(aliasFlag){
			$("#aliasTip").css("display", "");
			$("#aliasTip").html("该联系方式别名已存在！");
			return;
		}
		if(email && !isValidEmail(email)){
			$("#emailTip").css("display", "");
			$("#emailTip").html("请正确填写邮箱");
			return;
		}
		postData = {
			id : id,
			email : email,
			regionId : regionId,
			phoneNo : phoneNo,
			linkMan : linkMan,
			address : address,
			alias : alias
		}
		var ajax = Ajax.post("/user/linkWay/update/do");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			//获取用户信息
			if (result.type == "info") {
				var member = result.data;
				//展示用户信息
				getUserLinkWay();
				Layer.msgSuccess(result.message);
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.fail(function(result, jqXhr) {
			//隐藏等待提示框
			loaderLayer.hide();
		});
		ajax.go();
	}
	
	//检查联系方式别名是否重复
	function checkAlias(alias, id) {
		var ajax = Ajax.post("/user/alias/check/get");
		ajax.params({
			alias : alias
		});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			var userLinkWay = result.data;
			if (result.type == "info") {
				aliasFlag = false;
			}else if(userLinkWay.id == id){
				aliasFlag = false;
			}else{
				aliasFlag = true;
			}
				
		});
		ajax.go();
	}
	
	//返回用户联系方式
	function goback() {
		getUserLinkWay();
	}
	
</script>
</body>
<script type="text/html" id="addLinkWayTpl" title="添加联系方式模版">
 	<div class="add-block" >
		<div style="border-bottom: 1px dashed #e4e4e4; height: 25px; margin-bottom: 15px">
        	 <h2></h2>
        	<span class="operate">
		    	<a href="javascript:addLinkWay();" class="anormal">保存</a>
				<a href="javascript:goback();" class="anormal">取消</a>
			</span>
    	</div>
		<div class="add-block-cont2">
		    <dl class="labels babels1">
		        <dt><label class="label required">姓名：</label></dt>
		        <dd><input class="inputx inputx-h26" type="text" id="linkMan" maxlength="30"/><span class="red ml20" id="linkManTip" style="display:none"></span></dd>
		    </dl>
		    <dl class="labels">
		        <dt><label class="label required">手机：</label></dt>
		        <dd><input class="inputx inputx-h26" type="text" id="phoneNo" maxlength="15"/><span class="red ml20" id="phoneNoTip" style="display:none"></span></dd>
		    </dl>
		    <dl class="labels">
		        <dt><label>电子邮箱：</label></dt>
		        <dd><input class="inputx inputx-h26" type="text" id="email" maxlength="60"/><span class="red ml20" id="emailTip" style="display:none"></span></dd>
		    </dl>
		    <dl class="labels clearfix">
		        <dt><label>所在地区：</label></dt>
			<dd>
				<input type="hidden" id="regionId" />
				<input type="hidden" id="regionName" />
				<select class="field value" style="border: 1px solid #ccc; height: 28px; width: 120px; padding: 3px 5px;" id="province"></select>
				<select class="field value" style="border: 1px solid #ccc; height: 28px; width: 120px; padding: 3px 5px;" id="city"></select>
				<select class="field value" style="border: 1px solid #ccc; height: 28px; width: 120px; padding: 3px 5px;" id="county"></select>	
				<select class="field value" style="border: 1px solid #ccc; height: 28px; width: 120px; padding: 3px 5px;" id="town" style="display: none;"></select>
		     	</dd>
		   </dl>
		   <dl class="labels">
		       <dt><label>地址：</label></dt>
		       <dd><input class="inputx inputx-h26 inputx-w400" type="text" id="address" maxlength="90"/></dd>
		   </dl>
			<dl class="labels">
		        <dt><label class="label required">联系方式别名：</label></dt>
		        <dd><input class="inputx inputx-h26" type="text" id="alias" maxlength="30"/><span class="gray ml10">例如：您对他的称呼，如：“同事”,“同学”</span><span class="red ml20" id="aliasTip" style="display:none"></span></dd>
		    </dl>
		</div>
    </div>		
</script>
<script type="text/html" id="linkWayTpl" title="联系方式模版">
 {{# for(var i=0, len=d.length; i<len ; i++) {  }}
 {{# var userLinkWay = d[i]; }}
 <div class="add-block" id="showLinkWayDialog{{ userLinkWay.id }}">
    <div style="border-bottom: 1px dashed #e4e4e4; height: 25px; margin-bottom: 15px">
   		 <h2>{{userLinkWay.alias }}&nbsp;&nbsp;&nbsp;&nbsp;
			{{# if(userLinkWay.defaulted){ }}
			<span style="color: #b51320;font-size: 12px;">默认联系方式</span>
   			{{# } }}
   		</h2>
   		<span class="operate">
        	{{# if(!userLinkWay.defaulted){ }}
        	<a href="javascript:void(0);" onclick='toSetDefaulted({{ userLinkWay.id }})' class="anormal">设为默认</a>
			{{# } }}
        	<a href="javascript:void(0);" onclick='toUpdateLinkWay({{ JSON.encode(userLinkWay) }})' class="anormal">编辑</a>
        	<a href="javascript:void(0);" onclick="deleteLinkWay({{ userLinkWay.id }})" class="anormal">删除</a>
    	</span>
 	</div>
    <div class="add-block-cont1" id="showLinkWay{{ userLinkWay.id }}">
        <dl class="labels babels1">
            <dt><label>姓名：</label></dt>
            <dd>{{userLinkWay.linkMan }}</dd>
        </dl>
        <dl class="labels">
            <dt><label>手机：</label></dt>
            <dd>{{userLinkWay.phoneNo }}</dd>
        </dl>
        <dl class="labels">
            <dt><label>电子邮箱：</label></dt>
            <dd>{{userLinkWay.email ? userLinkWay.email: ""}}</dd>
        </dl>
        <dl class="labels">
            <dt><label>所在地区：</label></dt>
            <dd>{{userLinkWay.regionName ? userLinkWay.regionName: ""}}</dd>
        </dl>
        <dl class="labels">
            <dt><label>地址：</label></dt>
            <dd>{{userLinkWay.address ? userLinkWay.address: ""}}</dd>
        </dl>
		<dl class="labels">
            <dt><label>联系方式别名：</label></dt>
            <dd>{{userLinkWay.alias }}</dd>
        </dl>
    </div>
 </div>	
 {{# } }}		
</script>
<script type="text/html" id="updateLinkWayTpl" title="修改联系方式模版">
 <div style="border-bottom: 1px dashed #e4e4e4; height: 25px; margin-bottom: 15px">
   <h2>{{d.alias }}</h2>
   <span class="operate">
     	<a href="javascript:updateLinkWay({{ d.id }});" class="anormal">保存</a>
     	<a href="javascript:goback();" class="anormal">取消</a>
 	</span>
 </div>
 <div class="add-block-cont2">
     <dl class="labels babels1">
         <dt><label class="label required">姓名：</label></dt>
         <dd><input class="inputx inputx-h26" type="text" id="linkMan" value="{{d.linkMan }}" maxlength="30"/><span class="red ml20" id="linkManTip" style="display:none"></span></dd>
     </dl>
     <dl class="labels">
         <dt><label class="label required">手机：</label></dt>
         <dd><input class="inputx inputx-h26" type="text" id="phoneNo" value="{{d.phoneNo }}" maxlength="15"/><span class="red ml20" id="phoneNoTip" style="display:none"></span></dd>
     </dl>
     <dl class="labels">
         <dt><label>电子邮箱：</label></dt>
         <dd><input class="inputx inputx-h26" type="text" id="email" value="{{d.email ? d.email: ''}}" maxlength="60"/><span class="red ml20" id="emailTip" style="display:none"></span></dd>
     </dl>
     <dl class="labels clearfix">
         <dt><label>所在地区：</label></dt>
		<dd>
			<input type="hidden" id="regionId" />
			<input type="hidden" id="regionName" />
			<select class="field value" style="border: 1px solid #ccc; height: 28px; width: 120px; width="50px" padding: 3px 5px;" id="province"></select>
			<select class="field value" style="border: 1px solid #ccc; height: 28px; width: 120px; padding: 3px 5px;" id="city"></select>
			<select class="field value" style="border: 1px solid #ccc; height: 28px; width: 120px; padding: 3px 5px;" id="county"></select>	
			<select class="field value" style="border: 1px solid #ccc; height: 28px; width: 120px; padding: 3px 5px;" id="town" style="display: none;"></select>
      	</dd>
    </dl>
    <dl class="labels">
        <dt><label>地址：</label></dt>
        <dd><input class="inputx inputx-h26 inputx-w400" type="text" id="address" value="{{d.address ? d.address: ''}}" maxlength="90"/></dd>
    </dl>
	<dl class="labels">
		<dt><label class="label required">联系方式别名：</label></dt>
		<dd><input class="inputx inputx-h26" type="text" id="alias" value="{{d.alias }}" maxlength="30"/><span class="gray ml10">例如：您对他的称呼，如：“同事”,“同学”</span><span class="red ml20" id="aliasTip" style="display:none"></span></dd>
	</dl>
 </div>
</script>
</html>