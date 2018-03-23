<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head.jsp"%>
<div class="content">
	<div class="page-width">
		<div class="crumbs">
			<a href="">首页</a>&gt;<a href="">账户设置</a>&gt;<a href="javascript:;">基本信息</a>
		</div>
		<div class="section">
			<jsp:include page="/WEB-INF/jsp/include/ucenterMenu.jsp" />
			<div class="section-right1">
				<div class="mod-main">
					<dl class="acct-baseinfo">
						<dt style="text-align: left;">
							<img alt="" id="headImage"/> 
							<input name="file" type="file" id="fileToUploadFileImg" multiple="multiple" />
							<br/>
							<br/>
							<button class="btn-normal" id="btnfileToUploadFile">上传</button>
							<input type="hidden" id="headUuid" name="headUuid" />
							<input type="hidden" id="headUsage" name="headUsage" /> 
							<input type="hidden" id="headPath" name="headPath" />
						</dt>
						<dd>
							<dl class="labels labels1">
								<dt>
									<label>昵称：</label>
								</dt>
								<dd>
									<input class="inputx" type="text" id="nickName" maxlength="30"/>
								</dd>
							</dl>
							<dl class="labels labels1">
								<dt>
									<label>姓名：</label>
								</dt>
								<dd>
									<input class="inputx" type="text" id="realName" maxlength="30"/>
								</dd>
							</dl>
							<dl class="labels labels1">
								<dt>
									<label>性别：</label>
								</dt>
								<dd>
									<label class="mr20"> <input class="inputx" type="radio" name="gender" checked="checked" value="X"/> 保密 </label>
									<label class="mr20"> <input class="inputx" type="radio" name="gender" value="M"/> 男 </label>
									<label> <input class="inputx" type="radio" name="gender" value="F"/> 女 </label>
								</dd>
							</dl>
							<dl class="labels labels1">
								<dt>
									<label>手机：</label>
								</dt>
								<dd>
									<input class="inputx" type="text" id="phoneNo" disabled="disabled" maxlength="11"/>
								</dd>
							</dl>
							<dl class="labels labels1">
								<dt>
									<label>邮箱：</label>
								</dt>
								<dd>
									<input class="inputx" type="text" id="email" maxlength="60"/>
									<span class="red" id="emailTip"></span>
								</dd>
							</dl>
							<dl class="labels labels1">
								<dt>
									<label>生日：</label>
								</dt>
								<dd>
									<input class="inputx" id="birthDate" maxlength="60"/>
								</dd>
							</dl>
							<dl class="labels labels1">
								<dt></dt>
								<dd>
									<input class="btn btn-w120h28 btn-w70" type="button" value="保存" id="toSaveMemberBtn"/>
								</dd>
							</dl>
						</dd>
					</dl>

					<br />
					<br />
					<br />
					<br />
					<br />
					<br />
					<br />
					<br />
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
<script type="text/javascript" src="<%=resBaseUrl%>/lib/upload/js/jquery.fileupload.js"></script>
<script type="text/javascript" src="<%=resBaseUrl%>/lib/upload/js/jquery.fileupload.ext.js"></script>
<script type="text/javascript">
	$(function() {
		//获取用户详细信息
		getMemberInfo();
		$id("birthDate").datepicker({
		      changeMonth: true,
		      changeYear: true,
		      yearRange: "1920:2015",
		      showYearOrder: "desc"
		});
		//保存会员信息
		$id("toSaveMemberBtn").click(toSaveMember);
		
		initFileUpload("fileToUploadFileImg");
		//绑定修改模块上传按钮
		$id("btnfileToUploadFile").click(function() {
			isChangeImgData=true;
			fileToUploadFileIcon("fileToUploadFileImg");
		});
	});
	
	function fileToUploadFileIcon(fileId) {
		var fileUuidToUpdate = $id("headUuid").val();
		if (isNoB(fileUuidToUpdate)) {//为空时，添加
			var formData = {
				usage : "image.head"
			};
		} else {//否则修改
			var formData = {
				update : true,
				uuid : fileUuidToUpdate
			};
		}
		sendFileUpload(fileId, {
			url : getAppUrl("/file/upload"),
			dataType : "json",
			//自定义数据
			formData : formData,
			done : function(e, result) {
				var resultInfo = result.result;
				if (resultInfo.type == "info") {
					var fileBrowseUrl=resultInfo.data.files[0].fileBrowseUrl;
					$id("headUuid").val(resultInfo.data.files[0].fileUuid);
					$id("headUsage").val(resultInfo.data.files[0].fileUsage);
					$id("headPath").val(resultInfo.data.files[0].fileRelPath);
					$id("headImage").attr("src", fileBrowseUrl + "?" + new Date().getTime());
				}
			},
			fail : function(e, data) {
				console.log(data);
			},
			noFilesHandler : function() {
				Layer.msgWarning("请选择或更换图片");
			},
			fileNamesChecker : function(fileNames) {
				fileNames = fileNames || [];
				for (var i = 0; i < fileNames.length; i++) {
					if (!isImageFile(fileNames[i])) {
						Layer.msgWarning("只能上传图片文件");
						return false;
					}
				}
				return true;
			}
		});
	}
	
	//获取用户详细信息
	function getMemberInfo() {
		//
		var postData = {};
		var ajax = Ajax.post("/user/info/get");
		ajax.data(postData);
		ajax.done(function(result, jqXhr) {
			//获取用户信息
			if (result.type == "info") {
				var member = result.data;
				//展示用户信息
				showMerberInfo(member);
			} else {

			}
		});
		ajax.fail(function(result, jqXhr) {
			//隐藏等待提示框
			loaderLayer.hide();
		});
		ajax.go();
	}
	
	//用户信息
	function showMerberInfo(member) {
		$("#headImage").attr("src", member.fileBrowseUrl+ "?" + new Date().getTime());
		$id("headUuid").val(member.headUuid);
		$id("headUsage").val(member.headUsage);
		$id("headPath").val(member.headPath);
		$id("nickName").val(member.user.nickName);
		$id("realName").val(member.user.realName);
		if(member.user.gender != null){
			$("input:radio[name=gender]").each(function() {
				this.checked = member.user.gender != null && this.value == member.user.gender;
			});
		}
		$id("phoneNo").val(member.user.phoneNo);
		$id("birthDate").val(member.user.birthDate);
		$id("email").val(member.user.email);
	}
	
	//保存修改数据
	function toSaveMember() {
		//获取数据
		var user = {};
		var email = $id("email").val();
		if(email && !isValidEmail(email)){
			$("#emailTip").html("邮箱格式不正确");
			return;
		}
		
		user["nickName"] = $id("nickName").val();
		user["realName"] = $id("realName").val();
		var gender = $("input:radio[name=gender]:checked").val();
		user["gender"] = gender;
		user["birthDate"] = $id("birthDate").val();
		user["email"] = email;
		var member = {};
		member["memo"] = "新手";
		member["user"] = user;
		member["headUuid"] = $id("headUuid").val();
		member["headUsage"] = $id("headUsage").val();
		member["headPath"] = $id("headPath").val();
		//可以对postData进行必要的处理（如如数据格式转换）
		//显示等待提示框
		var loaderLayer = Layer.loading("正在保存用户信息...");
		//
		var ajax = Ajax.post("/user/info/update/do");
		ajax.data(member);
		ajax.done(function(result, jqXhr) {
			//隐藏等待提示框
			loaderLayer.hide();
			//
			if (result.type == "info") {
				Layer.msgSuccess("保存成功");
				getMemberInfo();
			} else {
			}
		});
		ajax.fail(function(result, jqXhr) {
			//隐藏等待提示框
			loaderLayer.hide();
		});
		ajax.go();
	}
</script>

</body>

</html>