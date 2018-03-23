//getServerTime() => 2016-01-22 13:31:23
//getServerTime("asTs") => 2016-01-22 13:29:42.715
//getServerTime("asNum") => 1453440425195
function getServerTime(xtra) {
	var retTime = null;
	//
	var ajax = Ajax.get("/xutil/serverTime.do");
	//
	if (xtra == "asTs") {
		ajax.params({
			asTs : true
		});
	} else if (xtra == "asNum") {
		ajax.params({
			asNum : true
		});
	}
	//
	ajax.sync();
	ajax.done(function(result) {
		retTime = result.data;
	});
	ajax.fail(function(result) {
		retTime = null;
	});
	ajax.go();
	//
	return retTime;
}

declare("app.consts");
//
app.consts = {
	sysAdminName : 'sysadmin',
	maxSingleProductCount : 1000
};

// 是否认证失败
function isAuthenFail(result) {
	var authenFailCode = 3101;
	return result.type != "info" && result.code == authenFailCode;
}

// 是否鉴权失败
function isAuthorFail(result) {
	var authorFailCode = 3201;
	return result.type != "info" && result.code == authorFailCode;
}

// 显示图片原图查看box
function showImageViewBox(imgSrc) {
	if (!$.colorbox) {
		return;
	}
	var imgHtml = '<img src="{0}"  />'.format(imgSrc);
	var maxWidth = $(window).width() - 40;
	var maxHeight = $(window).height() - 40;
	$.colorbox({
		html : imgHtml,
		maxWidth : maxWidth,
		maxHeight : maxHeight
	});
}

/* ul li 列表辅助类：支持拖放排序、单选、多选等 */
function FluidListHelper() {
	var defaultSelectMode = "multi";
	//
	this.asSortable = function(listSelector, axis, indexChangeCallback) {
		var jqList = $(listSelector);
		axis = axis || 'xy';
		//
		jqList.sortable({
			items : 'li',
			axis : axis,
			cursor : 'move',
			opacity : 0.6,
			revert : 150,
			start : function(evnt, ui) {
				var jqLi = ui.item;
				var index = jqList.find(">li").index(jqLi);
				jqLi.data("index", index);
			},
			stop : function(evnt, ui) {
				var jqLi = ui.item;
				var newIndex = jqList.find(">li").index(jqLi);
				var oldIndex = jqLi.data("index");
				jqLi.data("index", newIndex);
				//
				if (oldIndex != newIndex) {
					var indexChanges = [];
					indexChanges.add({
						"old" : oldIndex,
						"new" : newIndex
					});
					// console.log(indexChanges);
					if (indexChangeCallback) {
						indexChangeCallback(jqList, indexChanges);
					}
				}
			}
		});
		//
		return this;
	};
	// selectMode : single | multi
	this.asSelectable = function(listSelector, selectMode) {
		var jqList = $(listSelector);
		jqList.addClass("selectable");
		//
		selectMode = selectMode || defaultSelectMode;
		jqList.data("selectMode", selectMode);
		//
		jqList.attr("title", (selectMode == "multi" ? "Ctrl+单击 或 " : "") + "直接双击可选中/去选某一项");
		jqList.on("click", ">li", function(evnt) {
			if (selectMode == "multi" && evnt.ctrlKey) {
				var target = $(this);
				if (target.hasClass("selected")) {
					target.removeClass("selected");
				} else {
					target.addClass("selected");
				}
			}
		});
		jqList.on("dblclick", ">li", function() {
			var target = $(this);
			if (target.hasClass("selected")) {
				target.removeClass("selected");
			} else {
				if (selectMode == "single") {
					jqList.find(">li").removeClass("selected");
				}
				target.addClass("selected");
			}
		});
		//
		return this;
	};
	//
	this.getSelected = function(listSelector) {
		var jqList = $(listSelector);
		var selectMode = jqList.data("selectMode") || defaultSelectMode;
		if (selectMode == "single") {
			var jqSelected = jqList.find(">li.selected");
			return jqSelected.length > 0 ? jqSelected.data("itemData") : null;
		} else {
			var retList = [];
			jqList.find(">li.selected").each(function() {
				retList.add($(this).data("itemData"));
			});
			return retList;
		}
		//
		return undefined;
	};
	//
	this.removeSelected = function(listSelector) {
		var jqList = $(listSelector);
		var jqSelected = jqList.find(">li.selected");
		var aniDelay = 200;
		jqSelected.fadeOut(aniDelay, function() {
			$(this).remove();
		});
	};
	//
	this.getAll = function(listSelector) {
		var jqList = $(listSelector);
		var retList = [];
		jqList.find(">li").each(function() {
			retList.add($(this).data("itemData"));
		});
		return retList;
	};
	//
	this.moveSelected = function(listSelector, where, indexChangeCallback) {
		var jqList = $(listSelector);
		var jqLiDomArray = [];
		var indices = [];
		//
		var jqLiDoms = jqList.find(">li");
		var jqSelected = jqList.find(">li.selected");
		jqSelected.each(function() {
			var index = jqLiDoms.index($(this));
			indices.add(index);
		});
		// console.log(">> 选中的索引位置：" + indices.join(","));
		if (indices.length == 0) {
			return;
		}
		//
		for (var i = jqLiDoms.length - 1; i >= 0; i--) {
			var jqLiDom = $(jqLiDoms.get(i));
			if (jqLiDom.hasClass("selected")) {
				jqLiDom.hide();
			}
			// console.log(jqLiDom.data("itemData").name);
			jqLiDomArray.add(jqLiDom.detach());
		}
		jqLiDomArray.reverse();
		//
		var indexChanges = moveArrayElementsAt(jqLiDomArray, indices, where);
		if (indexChanges != null) {
			// console.log(indexChanges);
			if (indexChangeCallback) {
				indexChangeCallback(jqList, indexChanges);
			}
		}
		for (var i = 0, j = jqLiDomArray.length; i < j; i++) {
			var jqLiDom = jqLiDomArray[i];
			jqList.append(jqLiDom);
			if (jqLiDom.hasClass("selected")) {
				var aniDelay = 500;
				jqLiDom.fadeIn(aniDelay, function() {
					$(this).show();
				});
			}
		}
		//
		return this;
	};
	//
	this.clearItems = function(listSelector) {
		var jqList = $(listSelector);
		jqList.empty();
		//
		return this;
	};
	//
	this.setItems = function(listSelector, itemDataList, itemHtmlRenderer, appendMode) {
		var jqList = $(listSelector);
		appendMode = appendMode === true;
		if (!appendMode) {
			this.clearItems(jqList);
		}
		//
		itemDataList = itemDataList || [];
		for (var i = 0; i < itemDataList.length; i++) {
			var itemData = itemDataList[i];
			var itemHtml = itemHtmlRenderer(itemData);
			var itemDom = $(itemHtml).appendTo(jqList);
			var jqLiDom = $(itemDom);
			jqLiDom.data("itemData", itemData);
			var aniDelay = (jqList.size()) * 500 + 500;
			jqLiDom.fadeIn(aniDelay, function() {
				$(this).show();
			});
		}
		//
		return this;
	};
	//
	this.addItems = function(listSelector, itemDataList, itemHtmlRenderer) {
		return this.setItems(listSelector, itemDataList, itemHtmlRenderer, true);
	};

	//
	this.setContextMenuInfo = function(listSelector, contextMenuInfo) {
		var jqList = $(listSelector);
		var contextMenu = ContextMenu.newOne().init(contextMenuInfo);
		//
		jqList.on("contextmenu", ">li", function(evnt) {
			var pageX = evnt.pageX;
			var pageY = evnt.pageY;
			var itemData = $(this).data("itemData");
			var targetInfo = {
				data : itemData,
				item : this
			};
			contextMenu.show(pageX, pageY, targetInfo);
			//
			event.preventDefault();
		});
	};
	//
	this.setItemBtnClickHanlder = function(listSelector, clickHandler) {
		var jqList = $(listSelector);
		//
		jqList.on("click", ">li >.action.bar >.button", function(evnt) {
			var jqBtn = $(this);
			var jqLi = jqBtn.parents("li");
			var itemData = jqLi.data("itemData");
			var itemInfo = {
				data : itemData,
				item : jqLi.get(0)
			};
			clickHandler && clickHandler(itemInfo, jqBtn.attr("name"));
		});
	};
	// isFunc();
	this.findItemsByData = function(listSelector, isFunc) {
		var jqList = $(listSelector);
		//
		var retItems = [];
		jqList.find(">li").each(function() {
			var itemData = $(this).data("itemData");
			if (isFunc(itemData) == true) {
				retItems.add(this);
			}
		});
		return retItems;
	};
	this.findItemByData = function(listSelector, isFunc) {
		var jqList = $(listSelector);
		//
		var retItem = null;
		jqList.find(">li").each(function() {
			var itemData = $(this).data("itemData");
			if (isFunc(itemData) == true) {
				retItem = this;
				return false;
			}
		});
		return retItem;
	};
	//
	return this;
}

//
FluidListHelper.newOne = function() {
	return new FluidListHelper();
};

/**
 * <pre>
 * 绑定地区选择下拉列表以实现联动&lt;br/&gt; 
 * 可以如下使用：&lt;br/&gt; 
 * bindRegionLists(&quot;province&quot;, [ 45 ]); &lt;br/&gt;
 * bindRegionLists(&quot;province&quot;, &quot;city&quot;, [ 45, 4508]); &lt;br/&gt;
 * bindRegionLists(&quot;province&quot;, &quot;city&quot;, &quot;county&quot;,[ 45, 4508, 450803 ]);&lt;br/&gt;
 * bindRegionLists(&quot;province&quot;, &quot;city&quot;, &quot;county&quot;, &quot;town&quot;, [ 45, 4508, 450803, 450803102 ]);
 * </pre>
 * 
 * @author koqiui
 * @date 2015年11月3日 下午9:18:03
 * 
 * @param provinceSelId
 *            省份select控件id
 * @param [citySelId]
 *            城市select控件id
 * @param [countySelId]
 *            县区select控件id
 * @param [townSelId]
 *            乡镇select控件id
 * @param [initVals]
 *            初始选择值[provinceId, citySelId, countyId, townId]
 */
function bindRegionLists(provinceSelId, citySelId, countySelId, townSelId, initVals) {
	// 获取初始列表选择值（provinceId, citySelId, countyId, townId）
	if (isArray(citySelId)) {
		initVals = citySelId;
		//
		citySelId = null;
		countySelId = null;
		townSelId = null;
	} else if (isArray(countySelId)) {
		initVals = countySelId;
		//
		countySelId = null;
		townSelId = null;
	} else if (isArray(townSelId)) {
		initVals = townSelId;
		//
		townSelId = null;
	}
	initVals = initVals || [];
	var initProvinceVal = initVals.length > 0 ? initVals[0] : null;
	var initCityVal = initVals.length > 1 ? initVals[1] : null;
	var initCountyVal = initVals.length > 2 ? initVals[2] : null;
	var initTownVal = initVals.length > 3 ? initVals[3] : null;
	//
	var totalLevels = 1;
	if (citySelId) {
		totalLevels++;
	}
	if (countySelId) {
		totalLevels++;
	}
	if (townSelId) {
		totalLevels++;
	}
	// 清除指定级别的列表
	function clearSelectList(level) {
		if (level <= 4) {
			townSelId && clearSelectData(townSelId);
		}
		if (level <= 3) {
			countySelId && clearSelectData(countySelId);
		}
		if (level <= 2) {
			citySelId && clearSelectData(citySelId);
		}
		if (level <= 1) {
			setSelectValue(provinceSelId, "");
		}
	}
	//
	function loadProvinceList() {
		var ajax = Ajax.get("/setting/region/selectList/get");
		ajax.params({
			parentId : null
		});
		ajax.done(function(result) {
			if (result.type == "info") {
				var resultData = result.data;
				// console.log(resultData);
				loadSelectData(provinceSelId, resultData);
				// 处理初始化值
				if (initProvinceVal) {
					setSelectValue(provinceSelId, initProvinceVal);
					initProvinceVal = null;
					if (totalLevels > 1) {
						$id(provinceSelId).triggerHandler("change");
					}
				}
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	//
	function loadCityList(parentId) {
		var ajax = Ajax.get("/setting/region/selectList/get");
		ajax.params({
			parentId : parentId
		});
		ajax.done(function(result) {
			if (result.type == "info") {
				var resultData = result.data;
				// console.log(resultData);
				loadSelectData(citySelId, resultData);
				// 处理初始化值
				if (initCityVal) {
					setSelectValue(citySelId, initCityVal);
					initCityVal = null;
					if (totalLevels > 2) {
						$id(citySelId).triggerHandler("change");
					}
				}
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	//
	function loadCountyList(parentId) {
		var ajax = Ajax.get("/setting/region/selectList/get");
		ajax.params({
			parentId : parentId
		});
		ajax.done(function(result) {
			if (result.type == "info") {
				var resultData = result.data;
				// console.log(resultData);
				loadSelectData(countySelId, resultData);
				// 处理初始化值
				if (initCountyVal) {
					setSelectValue(countySelId, initCountyVal);
					initCountyVal = null;
					if (totalLevels > 3) {
						$id(countySelId).triggerHandler("change");
					}
				}
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}
	function loadTownList(parentId) {
		var ajax = Ajax.get("/setting/region/selectList/get");
		ajax.params({
			parentId : parentId
		});
		ajax.done(function(result) {
			if (result.type == "info") {
				var resultData = result.data;
				// console.log(resultData);
				loadSelectData(townSelId, resultData);
				// 处理初始化值
				if (initTownVal) {
					setSelectValue(townSelId, initTownVal);
					initTownVal = null;
				}
			} else {
				Layer.msgWarning(result.message);
			}
		});
		ajax.go();
	}

	//
	$id(provinceSelId).change(function() {
		var val = $(this).val();
		if (val) {
			clearSelectList(3);
			citySelId && loadCityList(parseInt(val));
		} else {
			clearSelectList(2);
		}
	});
	//
	$id(citySelId).change(function() {
		var val = $(this).val();
		if (val) {
			clearSelectList(4);
			countySelId && loadCountyList(parseInt(val));
		} else {
			clearSelectList(3);
		}
	});
	//
	$id(countySelId).change(function() {
		var val = $(this).val();
		if (val) {
			townSelId && loadTownList(parseInt(val));
		} else {
			clearSelectList(4);
		}
	});
	//
	loadProvinceList();
	//
}

// 短信验证码发送
declare("SmsCodeSender.core");
SmsCodeSender.core.fn = function() {
	var _usageUrls = {
		"normal" : "/notify/sms/send/for/normal/do",
		"regist" : "/notify/sms/send/for/regist/do",
		"logPass" : "/notify/sms/send/for/logPass/do",
		"payPass" : "/notify/sms/send/for/payPass/do"
	};
	//
	var _usage = "";
	// 发送间隔秒数
	var sendInterval = 60;
	var sendBtnText;
	var sendBtnHint = "{0}秒后可再发";
	//
	var jqSendBtn, jqPhoneNo, jqChkCode;
	var isCountingDown = false;
	var __countedSeconds = 0;
	//
	this.setUsage = function(smsUsage) {
		_usage = smsUsage;
		//
		return this;
	};

	function disableSendBtn(disabled) {
		disabled = disabled != false;
		//
		jqSendBtn.prop("disabled", disabled);
		jqSendBtn.attr("disabled", disabled);
	}

	function countdownSendBtn() {
		if (!isCountingDown) {
			isCountingDown = true;
			__countedSeconds = 0;
		}
		var leftSeconds = sendInterval - __countedSeconds;
		var hintText = sendBtnHint.format(leftSeconds);
		jqSendBtn.val(hintText);
		jqSendBtn.text(hintText);
		//
		if (leftSeconds > 0) {
			console.log("leftSeconds:" + leftSeconds);
			setTimeout(countdownSendBtn, 1000);
			__countedSeconds++;
		} else {
			isCountingDown = false;
			jqSendBtn.val(sendBtnText);
			jqSendBtn.text(sendBtnText);
			disableSendBtn(false);
		}
	}
	// {"noCheckCode", "invalidPhoneNo", ...}
	this.bindCtrls = function(sendBtnId, phoneNoInputId, chkCodeInputId, errorCodeHandlerMap) {
		errorCodeHandlerMap = errorCodeHandlerMap || {};
		//
		jqSendBtn = $id(sendBtnId);
		jqPhoneNo = $id(phoneNoInputId);
		if (chkCodeInputId) {
			jqChkCode = $id(chkCodeInputId);
		}
		//
		jqPhoneNo.on("keyup", function() {
			if (!isCountingDown) {
				var phoneNoOk = isMobile(this.value);
				//
				disableSendBtn(!phoneNoOk);
			}
		});
		//
		sendBtnText = jqSendBtn.val() || jqSendBtn.text();
		jqSendBtn.on("click", function() {
			// 防止快速连按
			if (!repeatChecker.isValidFor("SmsCodeSender", 1000)) {
				return;
			}
			//
			var postUrl = null;
			if (!_usage) {
				console.error("开发人员：您尚未设置短信发送用途[请调用.setUsage()]！");
			} else {
				postUrl = _usageUrls[_usage];
				if (!postUrl) {
					console.error("开发人员：无法发送短信[请检查.setUsage()]");
				}
			}
			//
			var postData = {};
			//
			if (jqChkCode != null) {
				var chkCode = jqChkCode.val();
				if (isNoB(chkCode)) {
					if (errorCodeHandlerMap["noCheckCode"]) {
						errorCodeHandlerMap["noCheckCode"]("请输入图形验证码");
					} else {
						Layer.msgWarning("请输入图形验证码");
					}
					return;
				}
				postData["chkCode"] = chkCode;
			}
			var phoneNo = jqPhoneNo.val();
			if (!isMobile(phoneNo)) {
				if (errorCodeHandlerMap["invalidPhoneNo"]) {
					errorCodeHandlerMap["invalidPhoneNo"]("请输入有效手机号");
				} else {
					Layer.msgWarning("请输入有效手机号");
				}
				return;
			}
			postData["phoneNo"] = phoneNo;
			//
			disableSendBtn();
			//
			var ajax = Ajax.post(postUrl).data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var success = result.data;
					if (success) {
						countdownSendBtn();
						//
						Layer.msgSuccess(result.message);
					} else {
						disableSendBtn(false);
						Layer.msgWarning(result.message);
					}
				} else {
					disableSendBtn(false);
					var resultCode = result.code;
					if (resultCode > 0 && errorCodeHandlerMap[resultCode + ""]) {
						errorCodeHandlerMap[resultCode + ""](result.message);
					} else {
						Layer.msgWarning(result.message);
					}
				}
			});
			ajax.on404(function() {
				Layer.msgWarning("请求的url不存在");
			});
			ajax.fail(function(result, jqXhr) {
				disableSendBtn(false);
				Layer.msgWarning(result.message);
			});
			ajax.go();
		});
		// 初始
		disableSendBtn();
		//
		return this;
	};
};
SmsCodeSender.newOne = function() {
	return new SmsCodeSender.core.fn();
};

// triggerId, imgId
// data-img-id = img.id
function bindImageCodeCtrls(triggerId, imgId) {
	var trigger, img;
	if (typeof triggerId == "string") {
		// 直接调用的
		trigger = $id(triggerId);
		if (typeof (imgId) != "string") {
			// 从属性获取关联图片id
			imgId = trigger.attr("data-img-id");
		}
	} else {
		// 作为事件处理函数调用的
		trigger = $(this);
		// 从属性获取关联图片id
		imgId = trigger.attr("data-img-id");
	}
	if (!imgId) {
		imgId = "chkCodeImage";
	}
	img = $id(imgId);
	if (!trigger.data("img-bound")) {
		trigger.data("img-bound", true);
		//
		var handler = function() {
			var freshUrl = getAppUrl() + "/xutil/checkCode.do?uid=" + genUniqueStr();
			img.attr("src", freshUrl);
		};
		trigger.on("click", handler);
		img.on("click", handler);
		//
		img.trigger("click");
	}
}

//全局统计对象
var Statis = {
	addGoodsBrowseCount: function(productId) {
		if (productId == null) {
			return;
		}
		var ajax = Ajax.post("/statis/goods/browse/count/add");
		ajax.data({
			productId: productId
		});
		ajax.done(function(result) {
			if (result.type == "info") {
				console.log("商品浏览次数已增加");
			} else {
				console.log(result.message);
				console.log("商品浏览次数增加失败");
			}
		});
		ajax.fail(function(result) {
			console.log(result.message);
			//
			console.log("商品浏览次数增加失败");
		});
		ajax.go();
	},
	fetchGoodsBrowseCount: function(productId, callback) {
		if (productId == null) {
			return;
		}
		var ajax = Ajax.get("/statis/goods/browse/count/get/by/id");
		ajax.params({
			productId: productId
		});
		ajax.done(function(result) {
			if (result.type == "info") {
				var count = result.data;
				console.log("商品浏览次数为：" + count);
				if (typeof callback == "function") {
					callback(count, productId);
				}
			} else {
				console.log(result.message);
				//
				console.log("商品浏览次数获取失败");
			}
		});
		ajax.fail(function(result) {
			console.log(result.message);
			//
			console.log("商品浏览次数获取失败");
		});
		ajax.go();
	},
	addShopBrowseCount: function(shopId) {
		if (shopId == null) {
			return;
		}
		var ajax = Ajax.post("/statis/shop/browse/count/add");
		ajax.data({
			shopId: shopId
		});
		ajax.done(function(result) {
			if (result.type == "info") {
				console.log("店铺浏览次数已增加");
			} else {
				console.log("店铺浏览次数增加失败");
			}
		});
		ajax.fail(function(result) {
			console.log(result.message);
			//
			console.log("店铺浏览次数增加失败");
		});
		ajax.go();
	},
	fetchShopBrowseCount: function(shopId, callback) {
		if (shopId == null) {
			return;
		}
		var ajax = Ajax.get("/statis/shop/browse/count/get/by/id");
		ajax.params({
			shopId: shopId
		});
		ajax.done(function(result) {
			if (result.type == "info") {
				var count = result.data;
				//console.log("店铺浏览次数为：" + count);
				if (typeof callback == "function") {
					callback(count, shopId);
				}
			} else {
				console.log(result.message);
				//
				console.log("店铺浏览次数获取失败");
			}
		});
		ajax.fail(function(result) {
			console.log(result.message);
			//
			console.log("店铺浏览次数获取失败");
		});
		ajax.go();
	}
};