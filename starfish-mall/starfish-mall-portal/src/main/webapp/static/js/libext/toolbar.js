/*  右边停靠工具栏    */
function RightToolBar(wrapper, autoCreateDom) {
	var THIS = this;
	var jqWrapper, jqToolBar, jqTopTabs, jqMidTabs, jqBtmTabs;
	//
	function addButton(jqTabs, btnInfo) {
		var jqTab = $('<div class="tab"></div>').appendTo(jqTabs);
		jqTab.attr("name", btnInfo.name);
		var jqIcon = $($('<i data-role="icon" class="icon" ></i>').appendTo(jqTab));
		var jqAction = $($('<span data-role="action" class="action" ></span>').appendTo(jqTab));
		jqAction.text(btnInfo.text);
		var btnName = btnInfo.name;
		var clickHandler = btnInfo.clickHandler;
		if (clickHandler) {
			jqTab.on("click", '>[data-role="action"]', function() {
				clickHandler(btnName);
			});
		}
		var showHandler = btnInfo.showHandler;
		if (showHandler) {
			jqTab.on("show", '>[data-role="action"]', function() {
				showHandler(btnName);
			});
		}
		//
		if (btnInfo.iconClass) {
			jqIcon.addClass(btnInfo.iconClass);
			jqAction.addClass(btnInfo.iconClass);
		}
		//
		if (btnInfo.hint == true) {
			var hintClass = btnInfo.hintClass || "hint";
			var jqHint = $($('<span data-role="hint" class="' + hintClass + '" ></span>').appendTo(jqTab));
		}
		if (btnInfo.fixed == true) {
			jqIcon.addClass("fixed");
			jqAction.addClass("fixed");
		}
	}

	//
	this.addMidButton = function(btnInfo) {
		btnInfo = btnInfo || {};
		if (jqMidTabs == null) {
			jqMidTabs = $($('<div class="tabs middle"></div>').appendTo(jqToolBar));
		}
		//
		addButton(jqMidTabs, btnInfo);
		//
		jqMidTabs.css("margin-top", -1 * (jqMidTabs.height() / 2));
		//
		return this;
	};
	//
	this.addBtmButton = function(btnInfo) {
		btnInfo = btnInfo || {};
		if (jqBtmTabs == null) {
			jqBtmTabs = $($('<div class="tabs bottom"></div>').appendTo(jqToolBar));
		}
		//
		addButton(jqBtmTabs, btnInfo);
		//
		return this;
	};
	//
	this.getButton = function(btnName) {
		var jqBtn = jqToolBar.find('>.tabs >.tab[name="' + btnName + '"]');
		return jqBtn.length > 0 ? jqBtn.get(0) : null;
	};
	//
	this.setActionText = function(btnName, text) {
		var jqBtn = jqToolBar.find('>.tabs >.tab[name="' + btnName + '"]');
		var jqAction = jqBtn.find('>[data-role="action"]');
		if (jqAction) {
			jqAction.text(text);
		}
	};
	//
	this.setButtonHint = function(btnName, hintText) {
		var jqBtn = jqToolBar.find('>.tabs >.tab[name="' + btnName + '"]');
		var jqHint = jqBtn.find('>[data-role="hint"]');
		if (hintText) {
			jqHint.text(hintText);
			jqHint.show();
		} else {
			jqHint.empty();
			jqHint.hide();
		}
	};
	//
	this.transToButtonFrom = function(btnName, from, endCallback) {
		endCallback = endCallback || function() {
		};
		$(from).effect("transfer", {
			to : THIS.getButton(btnName),
			className : "basic transfer"
		}, 600, endCallback);
	};
	//
	{
		// 初始化
		jqWrapper = $(wrapper);
		if (jqWrapper.length == 0) {
			if (autoCreateDom == true) {
				jqWrapper = $('<div></div>').appendTo(document.body);
			} else {
				return;
			}
		}
		jqWrapper.addClass("rightToolBarWrapper");
		jqWrapper.empty();
		jqToolBar = $($('<div class="rightToolBar"></div>').appendTo(jqWrapper));
		//
		jqToolBar.on("mouseenter", '>.tabs >.tab >[data-role="icon"]', function() {
			if ($(this).hasClass("fixed")) {
				return;
			}
			$(this).addClass("hover");
			var jqAction = $(this).next('[data-role="action"]');
			jqAction.show();
			jqAction.animate({
				width : 100
			}, 300);
			jqAction.trigger("show");
		});
		jqToolBar.on("mouseleave", '>.tabs >.tab >[data-role="action"]', function() {
			if ($(this).hasClass("fixed")) {
				return;
			}
			$(this).animate({
				width : 34
			}, 200, function() {
				$(this).hide();
				$(this).prev('[data-role="icon"]').removeClass("hover");
			});
		});
	}
	//
	return this;
}