<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%@include file="/WEB-INF/jsp/include/head.jsp" %>
 <div class="content">
  	<div class="page-width" >
  		<div class="crumbs"><a href="">首页</a>&gt;<a href="">车友分享</a>&gt;<span>博文页</span></div>
  		<div class="blog-detail">
  			<h1 id="blogTitle"></h1>
  			<div id="blogDisplay" class="blog-sum clearfix" style="display: none;">	
				 <span class="ashare fr" name="ashare"><em class="gray">分享</em><i></i>
                    <span name="ashareCont">
                        <a class="icon1 share_weixin" onclick="openWeiXinDialog()" href="javascript:void(0);">微信</a>
                        <a herf="#" class="icon2 share_tsina">新浪微博</a>
                        <a herf="#" class="icon3 share_qzone">QQ空间</a>
                        <!--<a herf="#" class="icon4">QQ好友</a>
                        <a herf="#" class="icon5">人人网</a>  -->
                    </span>
                </span>
                <a href="javascript:;" id="buy-now" class="buy-now" style="clear: both">
                    <em>立即购买</em><i></i>
                </a>
                <div class="blog-info" id="orderInfo">
                    <div><span class="gray">服务时间：</span><span>2015-02-21 19:21</span></div>
                    <div><span class="gray">服务车型：</span><span>奥迪A4</span></div>
                    <div><span class="gray">服务店铺：</span><a class="anormal" href="">某某店铺</a></div>
                    <div><span class="gray">服务项目：</span><span>精品洗车、镀晶、普通洗车</span></div>
                </div>
            </div>
            <div id="share-wx" style="display: none">
			    <div class="share-wx">
			       <img src="<%=resBaseUrl%>/image-app/temp/qr-code.jpg" alt=""/>
			    </div>
			</div>
            <div id="blogDetail"></div>
  		</div>
  	</div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
<script type="text/javascript">
var blog=extractUrlParams(location.href);
var blogId=blog.id;
//缓存orderId
var _orderId=null;
// 缓存博文信息
var _blog=null;
//----------------------------------------------业务处理----------------------------------------------------
//----------------------------------------------分享----------------------------------------------------
//微信
function openWeiXinDialog(){
	var dom = "#share-wx";
    Layer.dialog({
        dom : dom, //或者 html string
        area : [ '400px', '300px' ],
        title : "扫描二维码",
        closeBtn : true,
        btn : false
        //默认为 btn : ["确定", "取消"]
    });
}
//--------------------------------------------------------------------------------------------
var _title,_source,_sourceUrl,_pic,_showcount,_desc,_summary,_site,
	_url = document.location, _pic = '';
//qq空间
var ashareCont = $("[name='ashareCont']");
//
$(ashareCont).find("a.share_qzone").on("click", function(){
	//分享博文图片
	var userBlogImg  = _blog.userBlogImg;
	if(userBlogImg!=null){
		_pic = userBlogImg.fileBrowseUrl || '';
	}
	//title
	_title = _blog.title;
	//摘要
	_summary = '我在@亿投车吧 发现了一个非常不错的博文：'+_title+'。 感觉不错，分享一下';
	
	_desc = _blog.content;
	//
	var _shareUrl = 'http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?';
	//
	var theParams = {
			url : _url || '', //参数url设置分享的内容链接|默认当前页location
			showcount : _showcount||0, //参数showcount是否显示分享总数,显示：'1'，不显示：'0'，默认不显示
			desc : _desc||'说点什么呗', //参数desc设置分享的描述，可选参数
			summary : _summary, //参数summary设置分享摘要，可选参数
			title : '>>分享自亿投车吧', //参数title设置分享标题，可选参数
			/* site : _site||'', //参数site设置分享来源，可选参数 */
			pics : _pic //参数pics设置分享图片的路径，多张图片以＂|＂隔开，可选参数
	};
	_shareUrl = makeUrl(_shareUrl, theParams, true);
	//
	//window.open(_shareUrl);
	setPageUrl(_shareUrl, "_blank");
});
//微博
$(ashareCont).find("a.share_tsina").on("click", function(){
	//分享博文图片
	var userBlogImg  = _blog.userBlogImg;
	if(userBlogImg!=null){
		_pic = userBlogImg.fileBrowseUrl || '';
	}
	//title
	_title = _blog.title;
	//摘要
	_summary = '我在@亿投车吧 发现了一个非常不错的博文：'+_title+'。 感觉不错，分享一下';
	
	_desc = _blog.content;
	//
	var _shareUrl = 'http://v.t.sina.com.cn/share/share.php?&appkey=895033136';   //真实的appkey，必选参数 
	//
	var theParams = {
			url : _url||document.location, //参数url设置分享的内容链接|默认当前页location
			title : _summary||'说点什么呗', //参数title设置分享的标题|默认当前页标题，可选参数
			source : _source||'',
			sourceUrl : _sourceUrl||'',
			content : 'utf-8', //参数content设置页面编码gb2312|utf-8，可选参数
			pics : _pic //参数pics设置分享图片的路径，多张图片以＂|＂隔开，可选参数
	};
	_shareUrl = makeUrl(_shareUrl, theParams, true);
    //
	setPageUrl(_shareUrl, "_blank");
});
//继续购买（暂时是必须登录
//继续购买
$id("buy-now").on("click",function(){
	if(_orderId){
		var url = getAppUrl("/saleCart/list/jsp");
		setPageUrl(url, "_blank", function(){
			var success = false;
			var ajax = Ajax.post("/saleCart/sync/saleOrder/do");
			var data={"orderId":_orderId};
		    ajax.sync();
		    ajax.data(data);
		    ajax.done(function(result, jqXhr) {
		        if (result != ""&&result.type=="info") {
		        	if(result.data!=null){
		        		success = true;
		        	}
		        }else{
		        	Layer.warning(result.message);
		        }
			});
		    ajax.go();
		    //
			return success;
		});
	}
});
//----------------------------------------------------分页----------------------------------------------------
//<!--
//分页大小（页面级）
var qry_pageSize = 10;
//[搜索用]的关键字符串
var qry_keywords = null;
//
//过滤条件项
var qry_filterItems = {blogId:blogId};
//排序项
var qry_sortItems = {};
//分页信息
var qry_pagination = {
	totalCount : 0,
	pageSize : qry_pageSize,
	pageNumber : 1
};
var wordLen=200;
$.fn.limitInputWords = function(options) {
	var defaults = {			
		len : 200,
		showId : "show"
	};
	var options = $.extend(defaults,options);	
	var $input = $(this);		
	var $show = $("#"+options.showId);
	$show.html(options.len);
	$input.live("keydown keyup blur",function(e){						
	  	var content =$(this).val();
		var length = content.length;
		var result = options.len - length;
		if (result >= 0){
			$show.html(result);
		}else{
			$(this).val(content.substring(0,options.len))
		}
	});	
}	
//根据 新的的分页信息（或新的页码） 更新 本地分页信息
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

//根据最新的信息生成新的查询条件
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
//查询 按钮等
function filterAndQuery() {
	// 重置页码
	resetPagination(1);
	//
	doTheQuery();
}

//排序 按钮等
function sortAndQuery() {
	// 重置页码
	resetPagination(1);
	//
	doTheQuery();
}

//排序 按钮等
function pageNoAndQuery(pageNo) {
	// 变更页码
	resetPagination(pageNo);
	//
	doTheQuery();
}
function sendComment() {
	var content=$id("content").val();
	if(isNullOrBlank(content)){
		Toast.show("评论内容不能为空", null, "error");
		return;
	}
	var postData = {content:$id("content").val(),blogId:blogId};
	var ajax = Ajax.post("/social/blog/comment/add/do");
	ajax.data(postData);
	ajax.done(function(result, jqXhr) {
		layer.alert(result.message);
		if (result.type == "info") {
			var commentCount = $id("commentCount").text();
			commentCount = parseInt(commentCount) + 1;
			$(".commentCount").text(commentCount);
			doTheQuery();
			$id("content").val("");
			//TODO 还远字数
			$id("sid").text(wordLen.toString());
		}
	});
	ajax.go();
}
function doTheQuery() {
	var postData = newQryFilterInfo();
	var ajax = Ajax.post("/social/blog/comment/list/get");
	ajax.data(postData);
	ajax.done(function(result, jqXhr) {
		var rows = result.data.rows;
		if (rows == null) {
			return;
		}
		var len=rows.length;
		// 获取模板内容
		var tplHtml = $id("commentRowListTpl").html();
		// 生成/编译模板
		var htmlTpl = laytpl(tplHtml);
		// 根据模板和数据生成最终内容
		var htmlText = htmlTpl.render(rows);
		$id("replyList").html(htmlText);
		var refPagination = result.data.pagination;
		resetPagination(refPagination);
		//注意：jqPaginator的当前页面为 currentPage
		refPagination.currentPage = refPagination.pageNumber;
		// 根据新的分页信息（refPagination）刷新jqPaginator控件
		$("#jqPaginator").jqPaginator("option",refPagination);
		//$("#jqPaginator")
		if(len==0){
			$id("jqPaginator").hide();
		}else{
			$id("jqPaginator").show();
		}
	});
	ajax.go();
}
//------------------------------------------------渲染页面-------------------------------------------------
// 渲染页面内容
function renderHtml(data,fromId,toId) {
	//获取模板内容
	var tplHtml = $id(fromId).html();
	
	//生成/编译模板
	var theTpl = laytpl(tplHtml);
	
	//根据模板和数据生成最终内容
	var htmlStr = theTpl.render(data);
	
	//使用生成的内容
	$id(toId).html(htmlStr);
}
$(function() {
	if(blogId){
		var ajax = Ajax.post("/social/blog/get");
		ajax.data({id:blogId});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			var data = result.data;
			if (data == null) {
				return;
			}
			var blog=data.blog;
			if(blog != null && blog.title != null){
				$id("blogTitle").html(blog.title);
			}
			var orderId = blog.orderId;
			if(orderId !=null &&orderId != ""){
				_orderId=orderId;
				//缓存博文
				var blogs={
					title:blog.title,
					content:blog.content,
					userBlogImg:blog.userBlogImg
				}
				_blog=blogs;
				//
				renderHtml(blog.saleOrder,"orderInfoTpl","orderInfo");
				$id("blogDisplay").css("display","block");
			}else{
				$id("blogDisplay").css("display","none");
			}
			renderHtml(data,"blogDetailTpl","blogDetail");
		});
		ajax.go();
		$id("content").limitInputWords({					       
			len : wordLen,//限制输入的字符个数				       
			showId : "sid"//显示剩余字符文本标签的ID
		});
		//loadCommentData();
		
		$id("btnSendComment").click(function() {
			sendComment();
		});
		
		$("#jqPaginator").jqPaginator({
			totalCount : 0,
			pageSize : qry_pageSize,
			pageNumber : 1,
	        prev: '<a class="prev" href="javascript:void(0);">上一页<\/a>',
	        next:'<a class="next"  href="javascript:void(0);">下一页<\/a>',
	        page: '<a class="active" href="javascript:void(0);">{{page}}<\/a>',
	        onPageChange: function (pageNumber) {
	        	qry_pagination.pageNumber=pageNumber;
	        	doTheQuery();
	        }
	    });
	}
});
//-->
    //分享
    hoverShowHide ("ashare","ashareCont");

    bindHoverEvent("#buy-now");
</script>
</body>
<script type="text/html" id="orderInfoTpl">
	{{# var order =d}}
	{{#if(order){ }}
		<div><span class="gray">服务时间：</span> <span>{{order.finishTime}}</span></div>
		{{# if(order.carName){ }}
			<div><span class="gray">服务车型：</span> <span>{{order.carName || ""}}</span></div>
		{{# } }}
   	 	<div><span class="gray">服务店铺：</span> <a class="anormal" href="javascript:void(0)" onclick="setPageUrl(getAppUrl('/shop/detail/jsp?shopId={{ order.shopId }}'),'_blank');">{{order.shopName}}</a></div>
    	<div><span class="gray">服务项目：</span>
			{{# var svcNames=[]; }}
			{{# var svcs=order.saleOrderSvcs;}}
			{{# if(svcs!=null&&svcs.length>0){ }}
				{{# for(var i=0, len=svcs.length; i<len ; i++) {  }}
					{{# var svc =svcs[i];}}
					{{# svcNames.add(svc.svcName)}}
				{{# } }}
			{{# } }}
			<span>{{svcNames.join("、")}}</span>
		</div>
	{{# } }}
</script>
<script type="text/html" id="blogDetailTpl">
	{{# var blog=d.blog}}
	{{# var userContext=d.userContext}}
		<div class="blog-con">
		{{#if (blog.userBlogImg != null) {}}
			<div class="text-center mb20"><img width="439" height="328" src="{{blog.userBlogImg.fileBrowseUrl}}" title="" /></div>
		{{#}}}
			<p>{{blog.content}}</p>
		</div>
		<!--网友评论-->
		<div class="blog-comment">
			<div class="blog-comment-tit">网友评论</div>
				<div class="blog-comment-cont">
					<div class="pin_title">
                        <span class="bq_img"></span>
                        <span class="pin_total">已有<a href="javascript:void(0);" id="commentCount" class="commentCount red">{{blog.commentCount}}</a>条评论</span>
                    </div>

                    {{#if (userContext.userId != null) {}}
 					<textarea class="textarea textarea-auto" id="content" name="content" rows="5"></textarea>
					<div class="clearfix">
                        <div class="comment-tip">
                            <span>(您的评论需要经过审核才能显示,请文明发言！)&nbsp;&nbsp;</span><span id="show">剩余字数：<i id="sid">0</i></span>
                        </div>
						<div class="check">
                            <!--<label>验证码：<input name="code" class="inputx inputx-h26 inputx-w106" id="yzmText" onfocus="var offset = $(this).offset();$('#yzm').css({'left': +offset.left-2, 'top': +offset.top-$('#yzm').height()});$('#yzm').show();$('#yzmText').data('hide', 1)" onblur='$("#yzmText").data("hide", 0);setTimeout("hide_code()", 2000)' type="text"></label>
                            <div id="yzm" class="yzm"><img id="checkcode" onclick='this.src=this.src+"&amp;"+Math.random()' src="index.php_files/api.png"><br>点击图片更换</div>-->
                            <input value="发表评论" class="btn btn-w120h28 btn-w70" type="button"  id="btnSendComment">
                        </div> 
                    </div>

                    {{#} else {}}
                    <table class="order-td" style="width: 100%:">
						<tr>
							<td style="height: 45px;border: 1px solid #c13030;">
								您需要登录后才可以评论&nbsp;&nbsp;&nbsp;&nbsp;
								<a href="{{__appBaseUrl}}/user/login/jsp" target="_blank">登录</a> | 
								<a href="{{__appBaseUrl}}/user/reg/jsp" target="_blank">立即注册</a>
							</td>
						</tr>
					</table>
                    {{#}}}
                    <ul class="blog-apply" id="replyList">
                    </ul>
                    <div class="pager-gap">
                        <div class="fr pager" id="jqPaginator">
                            <a class="prev disabled" href="#nogo">上一页</a><a class="active" href="#nogo">1</a><a href="#nogo">2</a><a href="#nogo">3</a><a class="more" href="#nogo">...</a><a href="#nogo">10</a><a class="next" href="#nogo">下一页</a><span>共10页</span><span>到第 <input class="page-text" type="text"/>页</span><span><input class="btn btn-normal" type="button" value="确定" /></span>
						</div>
					</div>
				</div>
			</div>
		</div>
</script>
<script type="text/html" id="commentRowListTpl">
{{# var comments = d; }}
{{# for(var i=0, len=comments.length; i<len ; i++) {  }}
{{# var comment = comments[i]; }}
<li>
   <h1>
{{#if(blog.allowAnony){}}
	匿名用户：
{{#}else{}}
{{#if(isNullOrBlank(comment.userName)){}}
	亿投车吧用户：
{{#}else{}}
	{{comment.userName}}：
{{#}}}
{{# } }}
</span> 
   <span class="fr">
      发表于：{{comment.ts}}</span></h1>
    <div class="apply-con">
     {{comment.content}}
    </div>
     <div class="apply-btnbox">
      <!-- <a href="">回复</a><a href="">赞（0）</a>-->
      </div>
</li>
{{# } }}
</script>
</html>