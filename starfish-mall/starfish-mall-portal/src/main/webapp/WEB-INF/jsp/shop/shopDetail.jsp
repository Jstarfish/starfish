<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/head.jsp"%>
<div class="content">
	<div class="page-width">
		<div class="crumbs">
			<a href="">首页</a>&gt;<a href="">线下门店</a>&gt;<span>门店详情</span>
		</div>
		<div class="section shops-intr">
			<div class="section-right1">
				<div id="shopDetail" class="goods-intr clearfix">
				
				</div>
				<div id="share-wx" style="display: none">
			        <div class="share-wx">
			            <img src="<%=resBaseUrl%>/image-app/temp/qr-code.jpg" alt=""/>
			        </div>
			    </div>
				<div class="shops-buy-ecard">
                    <table id="ecards" class="table-normal card-list1" style="background: #f8f8f8">
					</table>
				</div>
				<!--左-保养-start-->
				<div class="mb20">
                    <table id="carSvcs" class="table-normal card-list1" style="background: #f8f8f8">
                    </table>
                </div>
				<!--左-保养-end-->
				<!--左-评价-start-->
				<div class="slideTxtBox slideTxtBox1" style="display:none;">
					<div class="hd">
						<ul>
							<li>商品评价（0）</li>
							<li>好评（0）</li>
							<li>中评（0）</li>
							<li>差评（0）</li>
						</ul>
					</div>
					<div class="bd">
						<div class="bd-content">
							<table class="tb-praise">
								<thead>
									<tr>
										<td>评价心得</td>
										<td width="76">顾客满意度</td>
										<td>服务名称</td>
										<td>车型</td>
										<td width="135">评论者</td>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>不错的宝贝！不错的宝贝 <!--<span class="time">2015-09-22 00:21</span>--></td>
										<th><span class="star"><i style="width: 80%"></i></span></th>
										<th>洗车<br />换机油<br />大保养
										</th>
										<th>奥迪Q5<br />ADSLap3c98
										</th>
										<td>
											<div class="item-member bronze">
												<div class="item-name">
													<i><img src="<%=resBaseUrl %>/image-app/temp/favicon1.jpg"
														alt="" /></i><span>j***j</span>
												</div>
												<div class="item-grade">
													<span>铜牌会员</span><em>江苏</em>
												</div>
											</div>
										</td>
									</tr>
									<tr>
										<td class="padding10" colspan="5">
											<div class="comment-operate">
												<span class="time">2015-08-19 21:15</span><a name="reply"
													class="anormal" href="javascript:;">回复（100）</a><a
													class="anormal" href="">赞（100）</a>
												<div class="reply-textarea">
													<div class="arrow">
														<i class="a1"></i><i class="a2"></i>
													</div>
													<div class="reply-cont">
														<textarea class="inputx inputx-h26 inputx-auto"
															placeholder="回复 李李李:"></textarea>
														<div class="btnbox">
															<input class="btn-gray" type="button" value="提交回复" /><input
																class="btn-gray" type="button" value="取消" />
														</div>
													</div>
												</div>
											</div>
											<ul class="comment-replylist">
												<li>
													<div class="reply-info">
														<span class="user-name">eerrr</span>回复<span>ddddddddddd</span>:
														<span class="">同问同问同问同问</span>
													</div>
													<div class="comment-operate">
														<span class="time">2015-08-19 21:15</span><a name="reply"
															class="anormal" href="javascript:;">回复</a>
														<div class="reply-textarea">
															<div class="arrow">
																<i class="a1"></i><i class="a2"></i>
															</div>
															<div class="reply-cont">
																<textarea class="inputx inputx-h26 inputx-auto"
																	placeholder="回复 李李李:"></textarea>
																<div class="btnbox">
																	<input class="btn-gray" type="button" value="提交回复" /><input
																		class="btn-gray" type="button" value="取消" />
																</div>
															</div>
														</div>
													</div>
												</li>
											</ul>
										</td>
									</tr>
								</tbody>
								<tbody>
									<tr>
										<td>不错的宝贝！不错的宝贝 <!--<span class="time">2015-09-22 00:21</span>--></td>
										<th><span class="star"><i style="width: 80%"></i></span></th>
										<th>洗车<br />换机油<br />大保养
										</th>
										<th>奥迪Q5<br />ADSLap3c98
										</th>
										<td>
											<div class="item-member bronze">
												<div class="item-name">
													<i><img
														src="<%=resBaseUrl%>/image-app/temp/favicon1.jpg"
														alt="" /></i><span>j***j</span>
												</div>
												<div class="item-grade">
													<span>铜牌会员</span><em>江苏</em>
												</div>
											</div>
										</td>
									</tr>
									<tr>
										<td class="padding10" colspan="5">
											<div class="comment-operate">
												<span class="time">2015-08-19 21:15</span><a name="reply"
													class="anormal" href="javascript:;">回复（100）</a><a
													class="anormal" href="">赞（100）</a>
												<div class="reply-textarea">
													<div class="arrow">
														<i class="a1"></i><i class="a2"></i>
													</div>
													<div class="reply-cont">
														<textarea class="inputx inputx-h26 inputx-auto"
															placeholder="回复 李李李:"></textarea>
														<div class="btnbox">
															<input class="btn-gray" type="button" value="提交回复" /><input
																class="btn-gray" type="button" value="取消" />
														</div>
													</div>
												</div>
											</div>
										</td>
									</tr>
								</tbody>
							</table>
							<div class="pager-gap">
								<div class="fr pager">
									<a class="prev disabled" href="#nogo">上一页</a><a class="active"
										href="#nogo">1</a><a href="#nogo">2</a><a href="#nogo">3</a><a
										class="more" href="#nogo">...</a><a href="#nogo">10</a><a
										class="next" href="#nogo">下一页</a><span>共10页</span><span>到第
										<input class="page-text" type="text" />页
									</span><span><input class="btn btn-normal" type="button"
										value="确定" /></span>
								</div>
							</div>
						</div>
						<div class="bd-content">22222</div>
						<div class="bd-content">33333</div>
						<div class="bd-content">444</div>
					</div>
				</div>
				<!--左-评价-end-->
			</div>
			<div class="section-left1">
				<div class="adv1">
					<img src="<%=resBaseUrl%>/image-app/temp/adv001.jpg" alt="" />
				</div>
				<div class="adv">
					<img src="<%=resBaseUrl%>/image-app/temp/adv002.jpg" alt="" />
				</div>
			</div>
		</div>
	</div>
</div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
<script type="text/javascript">
	var urlParams = extractUrlParams();
	var shopId = parseInt(urlParams.shopId) || null;
	//
	Statis.addShopBrowseCount(shopId);
	// 门店名称
	var shopName = null;
	
	//渲染页面内容
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
	
	function getShopDetail() {
		if (shopId == null) {
			Layer.warning("未指定门店，无法获取详情");
			return;
		}
		
		var ajax = Ajax.post("/shop/detail/get");
		ajax.params({
			shopId : parseInt(shopId)
		});
		ajax.sync();
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var resultData = result.data;
				if (result.data.shop != null) {
					shopName = resultData.shop.name;
					renderHtml(resultData.shop, "shopDetailTpl", "shopDetail");
					hoverShowHide ("ashare","ashareCont");
					bindShareEvent();
					
					if (resultData.shopSvcs != null) {
						renderHtml(resultData.shopSvcs, "carSvcsTpl", "carSvcs");
					}
					//
					Statis.fetchShopBrowseCount(resultData.shop.id, function(count, shopId){
						$id("viewCount").text(count);
					});
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	
	function getShopECards() {
		var ajax = Ajax.post("/ecard/list/get");
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				if (result.data != null) {
					renderHtml(result.data, "ecardsTpl", "ecards");
				}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	
	function buyCards(cardCode) {
		if (shopId == null) {
			Layer.warning("未指定门店，无法购买店铺e卡");
			return;
		}
		var url = getAppUrl("/eCardOrder/submit/jsp?");
		var theParams = {
			code : cardCode, 
			shopId : shopId,
			shopName : shopName
		}
		var encodeUrl = makeUrl(url, theParams, true);
		window.open(encodeUrl);
	}
	
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
	
	function bindShareEvent(){
		var _title,_source,_sourceUrl,_pic,_showcount,_desc,_summary,_site,
		_url = document.location, _pic = '';
		//
		var shareDiv = $("span.ashare");
		//
		$(shareDiv).find("a.share_qzone").on("click", function(){
			//分享货品图片
			var albumImg  = $id("imagePreview").find("div.bd ul li img").first();
			if(albumImg) _pic = $(albumImg).attr("src") || '';
			//货品title
			_title = $id("shopTitle").html();
			//销售价
			var salePrice = $id("salePrice").html() || '';
			//摘要
			_summary = '我在@亿投车吧 发现了一个非常不错的门店：'+_title+'。 感觉不错，分享一下';
			//
			var _shareUrl = 'http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?';
			//
			var theParams = {
					url : _url||document.location, //参数url设置分享的内容链接|默认当前页location
					showcount : _showcount||0, //参数showcount是否显示分享总数,显示：'1'，不显示：'0'，默认不显示
					desc : _desc||'说点什么呗', //参数desc设置分享的描述，可选参数
					summary : _summary, //参数summary设置分享摘要，可选参数
					title : '>>分享自亿投车吧', //参数title设置分享标题，可选参数
					/* site : _site||'', //参数site设置分享来源，可选参数 */
					pics : _pic //参数pics设置分享图片的路径，多张图片以＂|＂隔开，可选参数
			};
			_shareUrl = makeUrl(_shareUrl, theParams, true);
			//
			window.open(_shareUrl);
		});
		//
		$(shareDiv).find("a.share_tsina").on("click", function(){
			//分享货品图片
			var albumImg  = $id("imagePreview").find("div.bd ul li img").first();
			if(albumImg) _pic = $(albumImg).attr("src") || '';
			//货品title
			_title = $id("shopTitle").html();
			//销售价
			var salePrice = $id("salePrice").html() || '';
			//
			var summary = '我在@亿投车吧 发现了一个非常不错的门店：'+_title+'。 感觉不错，分享一下';
			//
			var _shareUrl = 'http://v.t.sina.com.cn/share/share.php?&appkey=895033136';     //真实的appkey，必选参数 
			//
			var theParams = {
					url : _url||document.location, //参数url设置分享的内容链接|默认当前页location
					title : summary||'说点什么呗', //参数title设置分享的标题|默认当前页标题，可选参数
					source : _source||'',
					sourceUrl : _sourceUrl||'',
					content : 'utf-8', //参数content设置页面编码gb2312|utf-8，可选参数
					pics : _pic //参数pics设置分享图片的路径，多张图片以＂|＂隔开，可选参数
			};
			_shareUrl = makeUrl(_shareUrl, theParams, true);
		    //
		    window.open(_shareUrl); 
		});
		//
		$(shareDiv).find("a.share_weixin").on("click", function(){
			//http://cli.im/
			//<img width="200" src="http://cli.clewm.net/qrcode/2015/01/21/2031452178.png" />
		});
	}
	
	$(function() {
		// 获取店铺服务列表
		//getCarSvcs();
		
		// 获取门店详情
		getShopDetail();
		
		// 获取门店e卡列表
		getShopECards();
		
		/*tab页签*/
		jQuery(".slideTxtBox").slide({
			trigger : "click"
		});
		
		//回复
		click("reply");
		
		jQuery(".picFocus").slide({
			mainCell : ".bd ul",
			effect : "left",
			autoPlay : false
		});
	});

</script>
</body>
<script type="text/html" id="shopDetailTpl" title="线下门店详情">
	{{# var shop = d }}
	<div id="imagePreview" class="picFocus fl">
		<div class="bd">
			<ul>
				{{# if (shop.shopAlbumImgs == null || shop.shopAlbumImgs.length == 0) { }}
					<li><img
							src="{{ getResUrl('/image-app/temp/default-shop.jpg') }}" /></li>
				{{# } else { }}
					{{# for (var i = 0, len = shop.shopAlbumImgs.length; i < len && i < 4; i++) { }}
						<li><img
							src="{{ shop.shopAlbumImgs[i].fileBrowseUrl }}" /></li>
					{{# } }}
				{{# } }}
			</ul>
		</div>
		<div class="hd">
			<ul>
				{{# if (shop.shopAlbumImgs == null || shop.shopAlbumImgs.length == 0) { }}
					<li><img
							src="{{ getResUrl('/image-app/temp/default-shop.jpg') }}" /></li>
				{{# } else { }}
					{{# for (var i = 0, len = shop.shopAlbumImgs.length; i < len && i < 4; i++) { }}
						<li><img
							src="{{ shop.shopAlbumImgs[i].fileBrowseUrl }}" /></li>
					{{# } }}
				{{# } }}
			</ul>
		</div>
	</div>
	<div class="shops-summary">
		<h1><span id="shopTitle">{{ shop.name }}</span>
			<span class="ashare fr" name="ashare"><i></i>
				<span name="ashareCont">
					<a herf="#" class="icon1 share_weixin" onclick="openWeiXinDialog()">微信</a>
					<a herf="#" class="icon2 share_tsina">新浪微博</a>
					<a herf="#" class="icon3 share_qzone">QQ空间</a>
				</span>
			</span>
		</h1>
		<dl class="summary-detail">
			<dt>满意度：</dt>
			<dd>
				<div class="star1">
					<i style="width: 80%"></i>
				</div>
				<div class="fr">
					共完成 <b class="red">152</b> 次订单&nbsp;|&nbsp;<b class="red">41</b>次评价|&nbsp;<b id="viewCount" class="red">0</b>次浏览
				</div>
			</dd>
			<dt>所在区域：</dt>
			<dd>{{ shop.regionName }}</dd>
			<dt>门店地址：</dt>
			<dd>{{ shop.street }}</dd>
			<dt>营业时间：</dt>
			<dd>9:20-17:20（春节开业时间3月2日）</dd>
		</dl>
		<div class="summary-buy">
			<input class="btn btn-w180h45" type="button" value="服务下单" style="display: none;" />
		</div>
</script>
<script type="text/html" id="carSvcsTpl" title="车辆服务列表">
	{{# var carSvcs = d }}
	<tr>
		<td width="145" class="shops-buy-tit" style="height: 100px"><h1>服务项目</h1></td>
		<td style="line-height: 23px">
		{{# for (var i = 0, len = carSvcs.length; i < len; i++) { }}
			<span>{{ carSvcs[i].svcName }}</span>
			{{# if (i != len - 1) { }}
			<span>、</span>
			{{# } }}
		{{# } }}
		</td>
	</tr>
</script>
<script type="text/html" id="ecardsTpl" title="店铺e卡列表">
	{{# var ecards = d }}
	<tr>
		<td width="145" class="shops-buy-tit"><h1>店铺e卡</h1></td>
		{{# for (var i = 0, len = ecards.length; i < len; i++) { }}
		<td style="text-align: center">
			<div style="width: 170px; display: inline-block">
				<a href="javascript:void(0);"> <span class="ecard-img"> <img
						src="{{ ecards[i].fileBrowseUrl }}" alt="" /> <span>¥{{ ecards[i].faceVal }}</span>
				</a>
				<div class="card-btn-box">
					<span class="red">¥{{ ecards[i].price }}</span><input class="btn btnw85h25 btn-w70"
						type="button" value="购买" onclick="buyCards('{{ ecards[i].code }}')" />
				</div>
			</div>
		</td>
		{{# } }}
	</tr>
</script>
</html>