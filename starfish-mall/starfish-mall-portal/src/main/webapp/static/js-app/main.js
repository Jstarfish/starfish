/**
 * MenuList 转 menuModel
 * 
 * @author 邓华锋
 * @date 2015年10月28日 下午3:10:14
 * 
 * @param menuList
 *            {Array} 菜单集合
 * @param rule
 *            自定义的规则
 * @returns {Array} MenuModel
 */
function menuList2MenuModel(menuList, rule) {
	// 默认级别规则
	var defaultLevelRule = {
		one : {
			name : "name",
			id : "id",
			link : null,
			childNodes : null
		},
		two : {
			name : "name",
			id : "id",
			link : null,
			childNodes : null
		},
		three : {
			name : "name",
			id : "id",
			link : null
		}
	};
	var levelRule = $.extend(true, defaultLevelRule, rule);
	var menuModel = [];

	for (var i = 0; i < menuList.length; i++) {
		var one = menuList[i];
		var menu = {};
		menu["id"] = one[levelRule.one.id];
		menu["name"] = one[levelRule.one.name];
		var oneChildNodes = [];

		// 2级
		if (levelRule.one.childNodes && levelRule.one.childNodes != null && one[levelRule.one.childNodes]) {
			var oneChildNodesData = one[levelRule.one.childNodes];
			for (var j = 0; j < oneChildNodesData.length; j++) {
				var two = oneChildNodesData[j];
				oneChildNodes[j] = {
					name : two[levelRule.two.name],
					id : two[levelRule.two.id]
				}
				var twoChildNodes = [];

				// 3级
				if (levelRule.two.childNodes && levelRule.two.childNodes != null && two[levelRule.two.childNodes]) {
					var twoChildNodesData = two[levelRule.two.childNodes];
					for (var k = 0; k < twoChildNodesData.length; k++) {
						var three = twoChildNodesData[j];
						twoChildNodes[k] = {
							name : three[levelRule.three.name],
							id : three[levelRule.three.id]
						}
						if (levelRule.three.link && levelRule.three.link != null) {
							twoChildNodes[k]["link"] == levelRule.three.link.replace("{{id}}", three[levelRule.three.id]);
						}
					}
				}

				if (levelRule.two.link && levelRule.two.link != null) {
					oneChildNodes[j]["link"] = levelRule.two.link.replace("{{id}}", two[levelRule.two.id]);
				}
				oneChildNodes[j]["childNodes"] = twoChildNodes;
			}
		}

		if (levelRule.one.link && levelRule.one.link != null) {
			menu["link"] = levelRule.one.link.replace("{{id}}", one[levelRule.one.id]);
		}
		menu["childNodes"] = oneChildNodes;
		menuModel[i] = menu;
	}
	// console.log(JSON.encode(menuModel));
	return menuModel;
}
// callback.call(this,id, text)
function renderMenuTree(domId, dataModel, callback, defId) {
	var jqDom = $id(domId);
	jqDom.empty();
	var voidLink = "javascript:void(0)";
	//
	jqDom.addClass("goods-menu");
	dataModel = dataModel || [];
	//
	defId = defId || null;
	var defJqDom = null;
	for (var i = 0; i < dataModel.length; i++) {
		var item1 = dataModel[i];
		var text1 = item1.name;
		var jqLi1 = $('<li><a></a></li>').appendTo(jqDom);
		jqLi1.find(">a").text(text1);
		var children1 = item1.childNodes;
		if (children1 == null || children1.length < 1) {
			var link = item1.link;
			if (isNoB(link)) {
				jqLi1.find(">a").attr("href", voidLink);
				//
				jqLi1.data("id", item1.id);
				jqLi1.click(function() {
					if (typeof callback == "function") {
						callback.call(this, $(this).data("id"), $(this).text());
					}
				});
				//
				if (defId && defId == item1.id) {
					defJqDom = jqLi1;
				}
			} else {
				jqLi1.find(">a").attr("href", link);
			}
		} else {
			var jqUl2 = $('<ul></ul>').appendTo(jqLi1);
			for (var j = 0; j < children1.length; j++) {
				var item2 = children1[j];
				var text2 = item2.name;
				var jqLi2 = $('<li><a></a></li>').appendTo(jqUl2);
				jqLi2.find(">a").text(text2);
				var children2 = item2.childNodes;
				if (children2 == null || children2.length < 1) {
					var link = item2.link;
					if (isNoB(link)) {
						jqLi2.find(">a").attr("href", voidLink);
						//
						jqLi2.data("id", item2.id);
						jqLi2.click(function() {
							if (typeof callback == "function") {
								callback.call(this, $(this).data("id"), $(this).text());
							}
						});
						//
						if (defId && defId == item2.id) {
							defJqDom = jqLi2;
						}
					} else {
						jqLi2.find(">a").attr("href", link);
					}
				} else {
					var jqUl3 = $('<ul></ul>').appendTo(jqLi2);
					for (var k = 0; k < children2.length; k++) {
						var item3 = children2[k];
						var text3 = item3.name;
						var jqLi3 = $('<li><a></a></li>').appendTo(jqUl3);
						jqLi3.find(">a").text(text3);
						// 末级
						var link = item3.link;
						if (isNoB(link)) {
							jqLi3.find(">a").attr("href", voidLink);
							//
							jqLi3.data("id", item3.id);
							jqLi3.click(function() {
								if (typeof callback == "function") {
									callback.call(this, $(this).data("id"), $(this).text());
								}
							});
							//
							if (defId && defId == item3.id) {
								defJqDom = jqLi3;
							}
						} else {
							jqLi3.find(">a").attr("href", link);
						}
					}
				}
			}
		}
	}
	// 绑定事件
	jqDom.find("a").parent("li").has("ul").children("a").append("<i></i>");
	jqDom.find("a").on("click", function() {
		if ($(this).siblings().is("ul")) {
			if ($(this).siblings("ul").is(":hidden")) {
				$(this).parent("li").addClass("active").siblings("li").removeClass("active").siblings("li").children("ul").find("li").removeClass("active");
				$(this).siblings("ul").slideDown();
				$(this).parent("li").siblings("li").find("ul").slideUp();
			} else {
				$(this).parent("li").removeClass("active");
				$(this).siblings("ul").slideUp();
			}
		} else {
			$(this).parent("li").addClass("active").siblings("li").removeClass("active");
		}
	});
	//
	if (defJqDom != null) {
		defJqDom.parents("ul").show();
		defJqDom.parents("li").addClass("active");
		defJqDom.addClass("active");
		defJqDom.trigger("click");
	}
}

// 排序-start

// callback.call(this,code, order)
function renderSorter(domId, dataModel, callback) {
	var jqDom = $id(domId);
	jqDom.empty();
	//
	jqDom.addClass("sorter");
	//
	var defaultLi = null;
	jqDom.on("click", ">li", function(e) {
		var activeLi = $(this).siblings("li.active");
		if (activeLi.length > 0) {
			activeDom = activeLi.get(0);
			if (activeDom != this) {
				activeLi = $(activeDom);
				activeLi.removeClass("active");
				var order = activeLi.data("order") || null;
				if (order != null) {
					activeLi.removeClass(order);
				}
				activeLi.data("order", null);
			}
		}
		//
		var orders = $(this).data("orders");
		var curOrder = $(this).data("order") || null;
		//
		var newOrder = orders.nextCycleElement(curOrder);
		if (curOrder != null) {
			$(this).removeClass(curOrder);
		}
		$(this).addClass(newOrder);
		$(this).data("order", newOrder);
		//
		$(this).addClass("active");
		//
		if (isFunction(callback)) {
			callback.call(this, $(this).data("code"), newOrder);
		}
	});
	//
	dataModel = dataModel || [];
	for (var i = 0; i < dataModel.length; i++) {
		var dataItem = dataModel[i];
		var code = dataItem.code;
		var text = dataItem.text;
		var orders = dataItem.orders || "desc";
		if (isString(orders)) {
			orders = orders.split(",");
		}
		for (var j = 0; j < orders.length; j++) {
			orders[j] = orders[j].trim().toLowerCase();
		}
		var jqLi = $("<li><a></a></li>").appendTo(jqDom);
		jqLi.find(">a").text(text);
		jqLi.data("code", code);
		jqLi.data("orders", orders);
		//
		if (dataItem.active == true) {
			defaultLi = jqLi;
		}
	}
	//
	if (defaultLi != null) {
		defaultLi.trigger("click");
	}
}

// 排序-end

// 整数调整控件
// callback.call(this,numValue, htmlDom)
function renderNumSpinner(domName, callback, initValue, minValue, maxValue) {
	maxValue = maxValue || null;
	minValue = minValue || 0;
	initValue = Math.max(initValue || 1, minValue);
	if (maxValue != null && maxValue != -1) {
		initValue = Math.min(initValue, maxValue);
	}
	//
	var jqDoms = $attr("name", domName);
	jqDoms.each(function() {
		var jqDom = $(this);
		//
		jqDom.addClass("num-spinner");
		//
		var jqInput = jqDom.find(">input");
		if (jqInput.length == 0) {
			jqDom.empty();
			//
			var innerHTML = '<a href="javascript:;" class="decr"><i></i></a><input type="text" name="" value="1"><a href="javascript:;" class="incr"><i></i></a>';
			jqDom.html(innerHTML);
			//
			jqInput = jqDom.find(">input");
			jqInput.val(initValue);
		}
		//
		jqDom.find(">input").change(function() {
			var value = ParseInt($(this).val());
			if (!isNum(value)) {
				var lastVal = $(this).data("value") || null;
				if (lastVal == null) {
					lastVal = initValue;
				}
				//
				$(this).val(lastVal);
			} else {
				var tmpDom = null;
				if (value <= minValue) {
					tmpDom = $(this).siblings("a.decr");
					tmpDom.prop("disabled", true);
					tmpDom.css("opacity", 0.5);
					value = minValue;
				} else {
					tmpDom = $(this).siblings("a.decr");
					tmpDom.prop("disabled", false);
					tmpDom.css("opacity", 1.0);
				}
				if (maxValue != null && maxValue != -1 && value >= maxValue) {
					tmpDom = $(this).siblings("a.incr");
					tmpDom.prop("disabled", true);
					tmpDom.css("opacity", 0.5);
					value = maxValue;
				} else {
					tmpDom = $(this).siblings("a.incr");
					tmpDom.prop("disabled", false);
					tmpDom.css("opacity", 1.0);
				}
				$(this).val(value);
				$(this).data("value", value);
				if (isFunction(callback)) {
					callback.call(this, value, this.parentNode);
				} else {
					console.log(value);
				}
			}
		});
		//
		jqDom.find("a.decr").click(function() {
			var jqInput = $(this).siblings("input");
			var value = ParseInt(jqInput.val());
			if (value <= minValue) {
				jqInput.val(minValue);
			} else {
				jqInput.val(value - 1);
				//
				jqInput.trigger("change");
			}
		});
		jqDom.find("a.incr").click(function() {
			var jqInput = $(this).siblings("input");
			var value = ParseInt(jqInput.val());
			if (maxValue != null && maxValue != -1 && value >= maxValue) {
				jqInput.val(maxValue);
			} else {
				jqInput.val(value + 1);
				//
				jqInput.trigger("change");
			}
		});
	});
}

function renderAlbumImages(domId, imageList, initIndex, previewClickCallback) {
	var jqDom = $id(domId);
	jqDom.empty();
	// 预览图
	var previewHtml = '<div class="spec-preview"><span class="jqzoom"><img /></span></div>';
	var jqPreview = $(previewHtml).appendTo(jqDom);
	//
	var jqZoomer = jqPreview.find(">.jqzoom");
	jqZoomer.jqueryzoom({
		preload : false,
		xzoom : 384,
		yzoom : 384
	});
	//
	var scrollerHtml = '<div class="spec-scroll"><a href="javascript:void(0)" class="prev disabled"></a><a href="javascript:void(0)" class="next"></a><div class="items"><ul name="imageList"></ul></div></div>';
	var jqScroller = $(scrollerHtml).appendTo(jqDom);
	var jqImageList = jqScroller.find('ul[name="imageList"]');
	// 生成图片List items
	imageList = imageList || [];
	var imageCount = imageList.length;
	for (var i = 0; i < imageCount; i++) {
		var imageInfo = imageList[i] || {};
		if (typeof imageInfo == "string") {
			imageInfo = {
				url : imageInfo
			};
		}
		var url = imageInfo.url;
		var thumbUrl = imageInfo.thumbUrl || url;
		imageInfo.thumbUrl = thumbUrl;
		var jqLi = $('<li><img /></li>').appendTo(jqImageList);
		var jqImage = jqLi.find(">img");
		jqImage.attr("src", thumbUrl);
		jqImage.attr("bimg", url);
		jqImage.attr("title", (i + 1) + "/" + imageCount);
	}
	//
	var jqImageItems = jqImageList.find("> li");
	var jqImages = jqImageList.find("> li > img");
	//
	jqImageList.on("click mouseover", ">li >img", function() {
		var curIndex = jqImages.index(this);
		jqZoomer.find(">img").data("index", curIndex);
		//
		jqZoomer.find(">img").attr("src", $(this).attr("src"));
		jqZoomer.find(">img").attr("jqimg", $(this).attr("bimg"));
		//
		jqImageItems.find(">img.active").removeClass("active");
		$(this).addClass("active");
	});
	//
	if (imageCount > 0) {
		initIndex = initIndex || 0;
		initIndex = Math.min(initIndex, imageCount);
		var jqInitImage = jqImages.eq(initIndex);
		jqZoomer.find(">img").data("index", initIndex);
		//
		jqZoomer.find(">img").attr("src", jqInitImage.attr("src"));
		jqZoomer.find(">img").attr("jqimg", jqInitImage.attr("bimg"));
		//
		jqInitImage.addClass("active");
	}
	// 点击事件
	jqPreview.bind("click", function() {
		var curIndex = jqZoomer.find(">img").data("index");
		if (typeof previewClickCallback == "function") {
			previewClickCallback(curIndex);
		} else {
			console.log("点击了第" + (curIndex + 1) + "个图片");
		}
	});
	//
	var tempLength = 0; // 临时变量,当前移动的长度
	var viewNum = 4; // 设置每次显示图片的个数量
	var moveNum = 1; // 每次移动的数量
	var moveTime = 300; // 移动速度,毫秒
	var moveLength = jqImageItems.eq(0).width() * moveNum; // 计算每次移动的长度
	var countLength = (jqImageItems.length - viewNum) * jqImageItems.eq(0).width(); // 计算总长度,总个数*单个长度
	// 下一张
	jqScroller.find("> .next").bind("click", function() {
		if (tempLength < countLength) {
			$(this).prev(".prev").removeClass("disabled");
			if ((countLength - tempLength) > moveLength) {
				jqImageList.animate({
					left : "-=" + moveLength + "px"
				}, moveTime);
				tempLength += moveLength;
				$(this).removeClass("disabled");
			} else {
				jqImageList.animate({
					left : "-=" + (countLength - tempLength) + "px"
				}, moveTime);
				tempLength += (countLength - tempLength);
				$(this).addClass("disabled");
			}
		}
	});
	// 上一张
	jqScroller.find("> .prev").bind("click", function() {
		if (tempLength > 0) {
			$(this).next(".next").removeClass("disabled");
			if (tempLength > moveLength) {
				$(this).removeClass("disabled");
				jqImageList.animate({
					left : "+=" + moveLength + "px"
				}, moveTime);
				tempLength -= moveLength;
			} else {
				jqImageList.animate({
					left : "+=" + tempLength + "px"
				}, moveTime);
				tempLength = 0;
				$(this).addClass("disabled");
			}
		}
	});
}

function renderAlbumViewer(domId, imageList, theTitle, showBuyBtn, buyBtnCallback) {
	var jqDom = $id(domId);
	jqDom.empty();
	//
	jqDom.addClass("viewer");
	//
	showBuyBtn = showBuyBtn || false;
	// 预览图
	var headHtml = '<div class="hd"><h1 name="xTitle"></h1><ul name="imageList"></ul></div>';
	var jqHead = $(headHtml).appendTo(jqDom);
	var jqXTitle = jqHead.find('[name="xTitle"]');
	jqXTitle.text(theTitle || "");
	if (showBuyBtn) {
		jqHead.append('<div class="hd-sweep"><input name="btnBuy" class="btn btn-w108" type="button" value="立即购买" /></div>');
		var jqBuyBtn = jqHead.find('input[name="btnBuy"]');
		jqBuyBtn.bind("click", function() {
			if (typeof buyBtnCallback == "function") {
				buyBtnCallback(theTitle);
			}
		});
	}
	//
	var bodyHtml = '<div class="bd"><ul name="imageList"></ul></div>';
	var jqBody = $(bodyHtml).appendTo(jqDom);
	var jqNav = $('<a class="prev" href="javascript:void(0)"></a> <a class="next" href="javascript:void(0)"></a>').appendTo(jqDom);
	//
	imageList = imageList || [];
	var jqHeadImageList = jqHead.find('ul[name="imageList"]');
	var jqBodyImageList = jqBody.find('ul[name="imageList"]');
	// 生成图片List items
	var imageCount = imageList.length;
	for (var i = 0; i < imageCount; i++) {
		var imageInfo = imageList[i] || {};
		if (typeof imageInfo == "string") {
			imageInfo = {
				url : imageInfo
			};
		}
		var url = imageInfo.url;
		var thumbUrl = imageInfo.thumbUrl || url;
		imageInfo.thumbUrl = thumbUrl;
		var jqLi = $('<li><img /></li>').appendTo(jqHeadImageList);
		var jqImage = jqLi.find(">img");
		jqImage.attr("src", thumbUrl);
		//
		jqLi = $('<li><img /></li>').appendTo(jqBodyImageList);
		jqImage = jqLi.find(">img");
		jqImage.attr("src", url);
	}
	//
	var jqHeadImageItems = jqHeadImageList.find("> li");
	var jqHeadImages = jqHeadImageList.find("> li > img");
	//
	jqHeadImageList.on("click mouseover", ">li >img", function() {
		jqHeadImageItems.find(">img.active").removeClass("active");
		$(this).addClass("active");
	});
	//
	if (imageCount > 0) {
		var initIndex = 0;
		var jqInitImage = jqHeadImages.eq(initIndex);
		jqInitImage.addClass("active");
	}
	// 加入滑动联动支持
	jqDom.slide({
		mainCell : ".bd ul",
		effect : "fold"
	});
}
$(function() {
	//
	$(".mod-services dd a").click(function() {
		$(this).addClass("active").siblings().removeClass("active");
	});
	//
	/* select */
	$(".select-special").click(function(event) {
		$(".select-special").children("ul").slideUp(100, function() {
			$(this).parent().removeClass("select-special-d");
		});
		if ($(this).children("ul").is(":hidden")) {
			$(this).children("ul").slideDown(100);
			$(this).addClass("select-special-d");
		}
		event.stopPropagation();
	});
	//
	$(document).click(function() {
		$(".select-special").children("ul").slideUp(100, function() {
			$(this).parent().removeClass("select-special-d");
		});
	});

	// 快速通道
	hoverShowHide("trackItem", "trackItemChild");

});

function makeTopSticky(domId, vtDiff, stickClass) {
	var jqDom = $id(domId);
	stickClass = stickClass || "fix-to-top";
	$(window).bind('scrollstop', function() {
		var diffHeight = $(this).scrollTop();
		diffHeight > vtDiff ? jqDom.addClass(stickClass) : jqDom.removeClass(stickClass);
	});
}

function makeBtmSticky(domId, vtDiff, stickClass) {
	var jqDom = $id(domId);
	stickClass = stickClass || "fix-to-btm";
	$(window).bind('scrollstop', function() {
		var diffHeight = $(document).height() - $(window).height() - $(this).scrollTop();
		diffHeight < vtDiff ? jqDom.removeClass(stickClass) : jqDom.addClass(stickClass);
	});
}
//
if (typeof $.fn.slide == "function") {
	// quick-menu
	$("#district,.order-dropdown,#quick-menu-cart").slide({
		type : "menu",
		titCell : ".nLi",
		targetCell : ".sub",
		effect : "slideDown",
		delayTime : 300,
		triggerTime : 0,
		defaultPlay : false,
		returnDefault : true
	});
    $("#nav").slide({
        type : "menu",
        titCell : ".nLi",
        targetCell : ".sub",
        effect : "slideDown",
        delayTime : 300,
        triggerTime : 0,
        returnDefault:true
    });

	// notice
	$(".txtScroll-top").slide({
		titCell : ".hd ul",
		mainCell : ".bd ul",
		autoPage : true,
		effect : "topLoop",
		autoPlay : true
	});
}

// 鼠标滑过
function hover(domId) {
	var jqDom = $id(domId);
	jqDom.hover(function() {
		$(this).addClass("hover");
	}, function() {
		$(this).removeClass("hover");
	})
}

function hoverGroup(domName) {
    var jqDom = $attr("name", domName);
    jqDom.hover(function() {
        $(this).addClass("hover");
    }, function() {
        $(this).removeClass("hover");
    })
}

// 鼠标滑过
function bindHoverEvent(listener, targetPath) {
	var jqListener = $id(listener);
	if (targetPath) {
		jqListener.on("mouseenter", targetPath, function() {
			$(this).addClass("hover");
		});
		jqListener.on("mouseleave", targetPath, function() {
			$(this).removeClass("hover");
		});
	} else {
		jqListener.on("mouseenter", function() {
			$(this).addClass("hover");
		});
		jqListener.on("mouseleave", function() {
			$(this).removeClass("hover");
		});
	}
}

//
function click(domName) {
	var jqDom = $attr("name", domName);
	jqDom.click(function() {
		$(this).siblings("div").slideToggle("fast");
	})
}

// 左右slide
/*
 * function xSlide(domName,slideName,distance,direction){ var jqDom = $attr("name",domName); var slideDom = $attr("name",slideName); direction = direction || "right"; jqDom.click(function(){
 * slideDom.animate({left:distance},1000) }) }
 */

function hoverShowHide(domName1, domName2) {
	var jqDom1 = $attr("name", domName1);
	var jqDom2 = $attr("name", domName2);
	jqDom1.hover(function() {
		$(this).addClass("hover");
		$(this).find(jqDom2).show();
	}, function() {
		$(this).removeClass("hover");
		$(this).find(jqDom2).hide();
	})
}

// 更换图形验证码
function changeImageCode() {
	var imgObj = $id("codeImage").get(0);
	imgObj.src = getAppUrl() + "/xutil/checkCode.do?uid=" + genUniqueStr();
}

// 右边栏 - 购物车
var rightToolBar = null;
function renderRightToolbar(autoCreateDom) {
	rightToolBar = new RightToolBar("#rightToolBarWrapper", autoCreateDom);
	rightToolBar.addMidButton({
		name : "cart",
		text : "购物车",
		hint : true,
		iconClass : "cart",
		showHandler : rightToolBarShowHandler,
		clickHandler : rightToolBarClickHandler
	});
	/*rightToolBar.addMidButton({
		name : "attention",
		text : "我的关注",
		hint : true,
		iconClass : "favor",
		clickHandler : rightToolBarClickHandler
	});*/
	rightToolBar.addMidButton({
		name : "toHome",
		text : "回到首页",
		iconClass : "home",
		clickHandler : rightToolBarClickHandler
	});
	//
	rightToolBar.addBtmButton({
		name : "toTop",
		text : "回顶部",
		iconClass : "toTop",
		clickHandler : rightToolBarClickHandler
	});
	// 其他初始化代码...

	// TODO 检查购物是否有东西...
	getCartItemCount(function(count) {
		var cartIsEmpty = count <= 0;
		if (cartIsEmpty) {
			// 隐藏红点
			rightToolBar.setButtonHint("cart", "");
		} else {
			// 显示红点
			rightToolBar.setButtonHint("cart", " ");
		}
	});
}
// 某一项显示（展开）
function rightToolBarShowHandler(btnName) {
	if (btnName == "cart") {
		if (!repeatChecker.isValidFor("cart-show-count", 2000)) {
			return;
		}
		// 获取并更新购物车中的商品/服务数量
		getCartItemCount(function(count) {
			var dispText = "购物车（" + count + "）";
			rightToolBar.setActionText(btnName, dispText);
		});
	}
	//
	console.log("Over的是：" + btnName);
}
// countCallback(count)
function getCartItemCount(countCallback) {
	var ajax = Ajax.post("/saleCart/count/get");
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			if (result.data != null && result.data >= 1) {
				if (typeof countCallback == "function") {
					countCallback(result.data);
				}
			} else {
				if (typeof countCallback == "function") {
					countCallback(0);
				}
			}
		} else {
			if (typeof countCallback == "function") {
				countCallback(0);
			}
		}
	});
	ajax.fail(function(result, jqXhr) {
		if (typeof countCallback == "function") {
			countCallback(0);
		}
	});
	ajax.go();
}
// 末一项点击
function rightToolBarClickHandler(btnName) {
	if (btnName == "toTop") {
		$(document).scrollTop(0);
	} else if (btnName == "toHome") {
		setPageUrl(getAppUrl("/"));
	} else if (btnName == "cart") {
		// 打开购物车页面
		setPageUrl(getAppUrl("/saleCart/list/jsp"));
	} else {
		// ...
		console.log("点击的是：" + btnName);
	}
}