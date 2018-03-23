<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/head.jsp" %>
<div class="content">
	<div class="page-width">
		<div class="crumbs">
			<a href="">首页</a>&gt;<a href="javascript:;">我的车辆</a>&gt;<a href="javascript:;">我的车辆</a>
		</div>
		<div class="section">
			<jsp:include page="/WEB-INF/jsp/include/ucenterMenu.jsp" />
			<div class="section-right1">
                <div class="mod-main">
                    <div class="add-tit" >
                        <a class="btna-i" href="javascript:void(0);" id="topAddUserCarBtn"><i>+</i><span>新增车辆</span></a>
                        <span class="gray ml10" id="carNumber"></span>
                    </div>
                    <div class="add-cont" id="addUserCar">
                        
                    </div>
                    <div class="add-cont" id="showUserCar" >
                        
                    </div>
                    <br/><br/><br/>
                </div>
            </div>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />   
<script type="text/javascript">
	//用户车辆标识
	var aliasFlag = false;
	//
	var carNumber = 0;
	$(function() {
		
		getUserCar();
		//添加用户车辆
		$id("topAddUserCarBtn").click(toAddUserCar);
		//添加用户车辆
		$id("underAddUserCarBtn").click(toAddUserCar);
	});
	
	//添加用户车辆
	function toAddUserCar() {
		getUserCar();
		if(carNumber >= 5){
			Layer.warning("抱歉！您最多只能添加5个车辆信息");
			return;
		}
		// 获取模板内容
		var tplHtml = $id("addUserCarTpl").html();
		// 根据模板生成最终内容
		$id("addUserCar").html(tplHtml);
		loadCarBrand();
		loadSelectData("serialId", "");
		loadSelectData("modelId", "");
		//
		initCarInfo();
	}
	
	//获取品牌信息
	function getCarBrand(id) {
		// ajax post 请求...
		var ajax = Ajax.post("/car/carBrand/get/by/id");
		ajax.params({
			id : id
		});
		ajax.done(function(result, jqXhr) {
			var carBrand = result.data;
			if (carBrand != null) {
				$id("carImage").attr("src", carBrand.fileBrowseUrl + "?" + new Date().getTime());
			}
			
		});
		ajax.go();
	}

	//获取用户车辆
	function getUserCar() {
		var postData = {};
		// ajax post 请求...
		var ajax = Ajax.post("/car/userCar/list/get");
		ajax.data(postData);
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			var userCarList = result.data;
			if (userCarList != null) {
				carNumber = userCarList.length;
				$id("carNumber").html("您已增加" + carNumber + " 个车辆，最多可增加5个");
				// 获取模板内容
				var tplHtml = $id("userCarTpl").html();
				// 生成/编译模板
				var htmlTpl = laytpl(tplHtml);
				// 根据模板和数据生成最终内容
				var htmlText = htmlTpl.render(userCarList);
				$id("showUserCar").html(htmlText);
				$id("addUserCar").html("");
			}
			
		});
		ajax.go();
	}
	
	//编辑用户车辆
	function toUpdateUserCar(userCar) {
		getUserCar();
		// 获取模板内容
		var tplHtml = $id("updateUserCarTpl").html();
		// 生成/编译模板
		var htmlTpl = laytpl(tplHtml);
		// 根据模板和数据生成最终内容
		var htmlText = htmlTpl.render(userCar);
		$id("showUserCarDialog"+userCar.id).html(htmlText);
		$id("addUserCar").html("");
		//
		initCarInfo();
		//获取车辆品牌
		loadCarBrand(function(){
			textSet("brandId", userCar.brandId);
		});
		//获取车辆系列
		loadCarSerial(userCar.brandId, function(){
			textSet("serialId", userCar.serialId);
		});
		//获取车辆类型
		loadCarModel(userCar.serialId, function(){
			textSet("modelId", userCar.modelId);
		});
		
	}
	
	function initCarInfo(){
		// 根据品牌加载车系
		$("#brandId").change(function() {
			var brandId = textGet("brandId");
			getCarBrand(brandId);
			loadCarSerial(brandId);
			loadSelectData("modelId", "");
		});
		
		// 根据车系加载车型
		$("#serialId").change(function() {
			var serialId = textGet("serialId");
			loadCarModel(serialId);
		});
	}
	
	//
	function deleteUserCar(id){
		var theLayer = Layer.confirm('确定要删除？', function() {
			//
			var hintBox = Layer.progress("正在删除...");
			var ajax = Ajax.post("/car/userCar/delete/do");
			ajax.params({
				id : id
			});
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					Layer.msgSuccess(result.message);
					getUserCar();
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
		var ajax = Ajax.post("/car/userCar/defaulted/set/do");
		ajax.params({
			id : id
		});
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				getUserCar();
				//
			} else {
			}
		});
		ajax.always(function() {
		});
		ajax.go();
	}
	
	function addUserCar(){
		//
		var postData = {}
		// 设置最后一级地区id
		var name = $id("name").val();
		if(!name){
			$("#nameTip").css("display", "");
			$("#nameTip").html("请填写车辆名称");
			return;
		}
		
		var brandId = $id("brandId").val();
		if(!brandId){
			$("#brandIdTip").css("display", "");
			$("#brandIdTip").html("请选择品牌");
			return;
		}
		
		var serialId = $id("serialId").val();
		if(!serialId){
			$("#serialIdTip").css("display", "");
			$("#serialIdTip").html("请选择车系");
			return;
		}
		
		var modelId = $id("modelId").val();
		if(!modelId){
			$("#modelIdTip").css("display", "");
			$("#modelIdTip").html("请选择车型");
			return;
		}
		
		var modelName = $("#brandId option:selected").text() + $("#serialId option:selected").text() + $("#modelId option:selected").text();
		
		postData = {
			name : name,
			brandId : brandId,
			serialId : serialId,
			modelId : modelId,
			modelName : modelName
		}
		var ajax = Ajax.post("/car/userCar/add/do");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			//获取用户信息
			if (result.type == "info") {
				var member = result.data;
				//展示用户信息
				getUserCar();
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
	
	function updateUserCar(id){
		//
		var postData = {}
		// 设置最后一级地区id
		var id = $id("carId").val();
		var name = $id("name").val();
		if(!name){
			$("#nameTip").css("display", "");
			$("#nameTip").html("请填写车辆名称");
			return;
		}
		
		var brandId = $id("brandId").val();
		if(!brandId){
			$("#brandIdTip").css("display", "");
			$("#brandIdTip").html("请选择品牌");
			return;
		}
		
		var serialId = $id("serialId").val();
		if(!serialId){
			$("#serialIdTip").css("display", "");
			$("#serialIdTip").html("请选择车系");
			return;
		}
		
		var modelId = $id("modelId").val();
		if(!modelId){
			$("#modelIdTip").css("display", "");
			$("#modelIdTip").html("请选择车型");
			return;
		}
		
		var modelName = $("#brandId option:selected").text() + $("#serialId option:selected").text() + $("#modelId option:selected").text();
		
		postData = {
			id : id,
			name : name,
			brandId : brandId,
			serialId : serialId,
			modelId : modelId,
			modelName : modelName
		}
		var ajax = Ajax.post("/car/userCar/edit/do");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			//获取用户信息
			if (result.type == "info") {
				var member = result.data;
				//展示用户信息
				getUserCar();
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
	
	//检查用户车辆别名是否重复
	function checkAlias(alias, id) {
		var ajax = Ajax.post("/user/alias/check/get");
		ajax.params({
			alias : alias
		});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			var userCar = result.data;
			if (result.type == "info") {
				aliasFlag = false;
			}else if(userCar.id == id){
				aliasFlag = false;
			}else{
				aliasFlag = true;
			}
				
		});
		ajax.go();
	}
	
	//获取车辆品牌
	function loadCarBrand(callback) {
		// 隐藏页面区
		var ajax = Ajax.post("/car/carBrand/selectList/get");
		ajax.params({});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData("brandId", result.data);
				if($.isFunction(callback)){
					callback();
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	//获取车辆车系
	function loadCarSerial(id, callback) {
		// 隐藏页面区
		var ajax = Ajax.post("/car/carSerial/selectList/get");
		ajax.params({
			brandId : id
		});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData("serialId", result.data);
				if($.isFunction(callback)){
					callback();
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	//获取车辆车型
	function loadCarModel(id, callback) {
		// 隐藏页面区
		var ajax = Ajax.post("/car/carModel/selectList/get");
		ajax.params({
			serialId : id
		});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				loadSelectData("modelId", result.data);
				if($.isFunction(callback)){
					callback();
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	
	//返回用户用户车辆
	function goback() {
		getUserCar();
	}
	
</script>
</body>
<script type="text/html" id="addUserCarTpl" title="添加用户车辆模版">
 	<div class="add-block" >
		<div style="height: 25px; margin-bottom: 15px">
        	 <h2></h2>
        	<span class="operate">
		    	<a href="javascript:addUserCar();" class="anormal">保存</a>
				<a href="javascript:goback();" class="anormal">取消</a>
			</span>
    	</div>
		<div class="add-block-cont1">
		    <dl class="dl-leftright">
        		<dt><img src="<%=appBaseUrl%>/static/image-app/temp/car1.jpg" alt="" id="carImage"/></dt>
				<dd>
     				<dl class="labels babels1">
         				<dt><label class="label required">车辆名称：</label></dt>
         				<dd><input class="inputx inputx-h26" type="text" id="name" maxlength="30"/><span class="red ml20" id="linkManTip" style="display:none"></span></dd>
     				</dl>
     				<dl class="labels">
        		 		<dt><label class="label required">品牌：</label></dt>
         				<dd><select id="brandId" class="field value" style="border: 1px solid #ccc; height: 28px; width: 143px; padding: 3px 5px;"><option value="" title="- 请选择 -"></option></select><span class="red ml20" id="phoneNoTip" style="display:none"></span></dd>
     				</dl>
     				<dl class="labels">
         				<dt><label class="label required">车系：</label></dt>
         				<dd><select class="field value" id="serialId" style="border: 1px solid #ccc; height: 28px; width: 143px; padding: 3px 5px;"><option value="" title="- 请选择 -"></option></select><span class="red ml20" id="emailTip" style="display:none"></span></dd>
     				</dl>
    				<dl class="labels">
       		 			<dt><label class="label required">车型：</label></dt>
        				<dd><select class="field value" id="modelId" style="border: 1px solid #ccc; height: 28px; width: 143px; padding: 3px 5px;"><option value="" title="- 请选择 -"></option></select></dd>
    				</dl>
				</dd>
    		</dl>
		</div>
    </div>		
</script>
<script type="text/html" id="userCarTpl" title="用户车辆模版">
 {{# for(var i=0, len=d.length; i<len ; i++) {  }}
 {{# var userCar = d[i]; }}
 <div class="add-block" id="showUserCarDialog{{ userCar.id }}">
    <div style=" height: 25px; margin-bottom: 15px">
   		 <h2>{{userCar.name }}&nbsp;&nbsp;&nbsp;&nbsp;
			{{# if(userCar.defaulted){ }}
			<span style="color: #b51320;font-size: 12px;">用户默认车辆</span>
   			{{# } }}
   		</h2>
   		<span class="operate">
        	{{# if(!userCar.defaulted){ }}
        	<a href="javascript:void(0);" onclick='toSetDefaulted({{ userCar.id }})' class="anormal">设为默认</a>
			{{# } }}
        	<a href="javascript:void(0);" onclick='toUpdateUserCar({{ JSON.encode(userCar) }})' class="anormal">编辑</a>
        	<a href="javascript:void(0);" onclick="deleteUserCar({{ userCar.id }})" class="anormal">删除</a>
    	</span>
 	</div>
    <div class="add-block-cont1" id="showUserCar{{ userCar.id }}">
        <dl class="dl-leftright">
        	<dt><img src="{{ userCar.carBrand.fileBrowseUrl }}" alt=""/></dt>
        	<dd>
            	<dl class="labels">
                	<dt><label>车辆名称：</label></dt>
                	<dd>{{ userCar.name }}</dd>
            	</dl>
            	<dl class="labels">
                	<dt><label>品牌：</label></dt>
                	<dd>{{ userCar.carBrand.name }}</dd>
            	</dl>
				<dl class="labels">
                	<dt><label>车系：</label></dt>
                	<dd>{{ userCar.carSerial.name }}</dd>
            	</dl>
				<dl class="labels">
                	<dt><label>车型：</label></dt>
                	<dd>{{ userCar.carModel.name }}</dd>
            	</dl>
        	</dd>
    	</dl>
    </div>
 </div>	
 {{# } }}		
</script>
<script type="text/html" id="updateUserCarTpl" title="修改用户车辆模版">
 <div style="border-bottom: 1px dashed #e4e4e4; height: 25px; margin-bottom: 15px">
   <h2>{{d.name }}</h2>
   <span class="operate">
     	<a href="javascript:updateUserCar({{ d.id }});" class="anormal">保存</a>
     	<a href="javascript:goback();" class="anormal">取消</a>
 	</span>
 </div>
  <div class="add-block-cont1" id="showUserCar{{ d.id }}">
     <dl class="dl-leftright">
        <dt><img src="<%=appBaseUrl%>/static/image-app/temp/car1.jpg" alt="" id="carImage"/></dt>
		<dd>
     		<input type="hidden" id="carId" value="{{d.id }}" maxlength="30"/>
     		<dl class="labels babels1">
         		<dt><label class="label required">车辆名称：</label></dt>
         		<dd><input class="inputx inputx-h26" type="text" id="name" value="{{d.name }}" maxlength="30"/><span class="red ml20" id="nameTip" style="display:none"></span></dd>
     		</dl>
     		<dl class="labels">
        		<dt><label class="label required">品牌：</label></dt>
         		<dd><select class="field value" id="brandId" style="border: 1px solid #ccc; height: 28px; width: 143px; padding: 3px 5px;"><option value="" title="- 请选择 -"></option></select><span class="red ml20" id="brandIdTip" style="display:none"></span></dd>
     		</dl>
     		<dl class="labels">
         		<dt><label class="label required">车系：</label></dt>
         		<dd><select class="field value" id="serialId" style="border: 1px solid #ccc; height: 28px; width: 143px; padding: 3px 5px;"><option value="" title="- 请选择 -"></option></select><span class="red ml20" id="serialIdTip" style="display:none"></span></dd>
     		</dl>
    		<dl class="labels">
       		 	<dt><label class="label required">车型：</label></dt>
        		<dd><select class="field value" id="modelId" style="border: 1px solid #ccc; height: 28px; width: 143px; padding: 3px 5px;"><option value="" title="- 请选择 -"></option></select><span class="red ml20" id="modelIdTip" style="display:none"></span></dd>
    		</dl>
		</dd>
    </dl>
 </div>
</script>
</html>