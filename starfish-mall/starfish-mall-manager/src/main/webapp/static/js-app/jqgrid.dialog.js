/**
*jqGrid和jquery dialog结合
* 基本功能实现：增删改查
* @license 邓华锋 jqGrid dialog JS - v1.0.0 - 2015-10-13
* Copyright(c) 2015, 邓华锋, denghuafeng@live.cn
* 
* License: http://www.denghuafeng.com
*/
(function(factory) {
	"use strict";
	if (typeof define === "function" && define.amd) {
		// AMD. Register as an anonymous module.
		define([ "jquery" ], factory);
	} else {
		// Browser globals
		factory(jQuery);
	}
}(function($) {
	"use strict";
	$.jgridDialog = $.jgridDialog || {};
	if (!$.jgridDialog.hasOwnProperty("defaults")) {
		$.jgridDialog.defaults = {};
	}
	$.extend($.jgridDialog, {
		version : "1.0.0"
	});
	
	$.fn.jgridDialog = function(pin) {
		if (typeof pin === 'string') {
			var fn = $.jgridDialog.getMethod(pin);
			if (!fn) {
				throw ("jqGrid - No such method: " + pin);
			}
			var args = $.makeArray(arguments).slice(1);
			return fn.apply(this, args);
		}
		return this.each(function() {
			if (pin != null && pin.data !== undefined) {
				pin.data = [];
			}
			var p = $.extend(true, {
				theDlgId : "dialog",// 模式对话框
				theGridCtrlId : "theGridCtrl",// jqGrid列表
				rootPanelId : "rootPanel",// 页面布局
				rootPanelSetting:{//页面布局配置
					spacing_open : 1,
					spacing_closed : 1,
					north__size : 50,
					allowTopResize : false
				},
				btnToAddId : "btnToAdd",// 增加按钮ID
				btnToQryId : "btnToQry",// 查询按钮ID
				pkFldName : "id",// 主键列名
				dlgTitle : "",// 模式对话框标题
				addUrl : "",//增加链接
				updUrl : "",//修改链接
				delUrl : "",//删除链接
				viewUrl : "",//如果isUsePageCacheToView为false，将使用此链接去查看这条记录的信息
				isUsePageCacheToView : true,// 是否使用页面缓存数据查看信息 默认使用，则viewUrl可为空
				isShowViewShowModBtn:true,//查看时是否显示修改按钮
				continuousSupported:true,//是否连续添加
				viewAfterSaving:false,// 是否保存后转到查看对话框
				jqGridGlobalSetting:{//jqGrid初始化时设置的变量
					url : getAppUrl(""),//获取列表链接
					contentType : 'application/json',
					mtype : "post",
					datatype : 'json',
					height : "100%",
					width : "100%",
					colNames : [],//参考jqGrid的colNames
					colModel : [],//参考jqGrid的colModel
					multiselect : false,
					multikey : 'ctrlKey',
					pager : "#theGridPager",// jqGrid分页
					loadComplete : function(gridData) {
						if ($.isFunction(cacheDataGridHelper.cacheData)) {
							cacheDataGridHelper.cacheData(gridData);// JqGridHelper缓存最新的grid数据
						}
						var callback = getCallbackAfterGridLoaded();
						if ($.isFunction(callback)) {
							callback();
						}
					},
					ondblClickRow : function(rowId) {
						openViewDlg(rowId);
					}
				},
				dlgGlobalConfig:{//全局对话框配置
					width : Math.min(600, $window.width()),
					height : Math.min(160, $window.height()),
					modal : true,
					open : false
				},
				addDlgConfig : {},// 增加对话框配置
				modAndViewDlgConfig : {},// 修改和查看对话框配置
				modDlgConfig:{},// 修改对话框配置
				viewDlgConfig:{},// 查看对话框配置
				formPostDataWay:"idOrName",//idOrName:formProxy的id或key,key:id出现命名一样时，用这个,serializeArray:jquery表单封装成json
				/**
				 * 注册增加和修改的验证表单控件
				 * 
				 * @author 邓华锋
				 * @date 2015年10月9日
				 * 
				 * @param formProxy
				 */
				formProxyInit : null,
				/**
				 * 注册查询验证表单控件
				 * 
				 * @author 邓华锋
				 * @date 2015年10月9日
				 * 
				 * @param formProxyQuery
				 */
				formProxyQueryInit : null,
				/**
				 * 增加时清空控件的值
				 * @author 邓华锋
				 * @date 2015年10月9日 
				 *
				 */
				addInit : null,
				/**
				 * 修改时给控件赋值
				 * @author 邓华锋
				 * @date 2015年10月9日
				 * 
				 * @param data
				 */
				modInit : null,
				/**
				 * 查看时控件赋值
				 * @author 邓华锋
				 * @date 2015年10月9日
				 * 
				 * @param data
				 */
				viewInit : null,
				/**
				 * 修改和查看时给控件赋值
				 * @author 邓华锋
				 * @date 2015年10月9日
				 * 
				 * @param data
				 */
				modAndViewInit : null,
				/**
				 * 设置增加窗口的按钮
				 * @author 邓华锋
				 * @date 2015年11月12日
				 * 
				 * @param buttons
				 * @param rowData
				 * @param jqGridCtrl
				 */
				settingAddBtn:null,
				/**
				 * 设置编辑时窗口的按钮
				 * @author 邓华锋
				 * @date 2015年11月12日
				 * 
				 * @param buttons
				 * @param rowData
				 * @param jqGridCtrl
				 */
				settingModBtn:null,
				/**
				 * 设置查看窗口的按钮
				 * @author 邓华锋
				 * @date 2015年11月12日
				 * 
				 * @param buttons
				 * @param rowData
				 * @param jqGridCtrl
				 */
				settingViewBtn:null,
				/**
				 * 封装查询参数
				 * 
				 * @author 邓华锋
				 * @date 2015年10月9日
				 * 
				 * @param postData
				 * @param formProxyQuery
				 */
				queryParam : null,
				/**
				 * 获得删除确认提示
				 * 
				 * @author 邓华锋
				 * @date 2015年10月9日
				 * 
				 * @param data
				 * @returns {String}
				 */
				getDelComfirmTip : null,
				/**
				 * 用于修改时显示隐藏元素
				 * @author 邓华锋
				 * @date 2015年10月9日
				 * 
				 * @param isShow
				 */
				modElementToggle : null,
				/**
				 * 用于查看详情时显示隐藏元素
				 * @author 邓华锋
				 * @date 2015年10月9日
				 * 
				 * @param isShow
				 */
				viewElementToggle : null,
				/**
				 * 保留原数据，用于验证对数据是否进行了修改
				 * 
				 * @author 邓华锋
				 * @date 2015年10月9日
				 * 
				 * @param data
				 */
				saveOldData : null,
				/**
				 * 保存数据时，可对post数据，进一步操作
				 * 
				 * @author 邓华锋
				 * @date 2015年10月15日
				 * 
				 * @param postData
				 */
				savePostDataChange:null,
				/**
				 * 删除时，可对post数据，进一步操作
				 * 
				 * @author 邓华锋
				 * @date 2015年10月15日
				 * 
				 * @param postData
				 */
				delPostDataChange:null,
				/**
				 * 页面加载
				 * 
				 * @author 邓华锋
				 * @date 2015年10月9日
				 * 
				 * @jqGridCtrl
				 * @cacheDataGridHelper
				 */
				pageLoad : null,
				/**
				 * jqGrid 额外配置
				 * 可设置相应的属性(例如：jqGridCtrl.jqGrid("rowNum",-1);)，
				 * 可以针对一些事件，对jqGrid的进行配置
				 * 例如:$id("btn").click(function(){jqGridCtrl.jqGrid('setSelection', "13");});
				 * @author 邓华锋
				 * @date 2015年10月14日
				 * 
				 * @param jqGridCtrl
				 */
				jqGridSetting:null
			}, $.jgridDialog.defaults, pin);
			// ------------------------------------------全局变量 start---------------------------{{
			var ts = this;
			var curAction = "view";

			// 缓存当前jqGrid数据行数组
			var cacheDataGridHelper = JqGridHelper.newOne("");
			// 模式窗口Dom对象
			var jqDlgDom = $id(p.theDlgId);
			// jqGrid缓存变量
			var jqGridCtrl = null;
			// --------------------------------扩展对话框按钮栏控件（复选框）----------------------------
			
			var dlgToolbarCtrlsAddName = "dlg-toolbar-ctrls";
			var dlgToolbarCtrlsHtmlAdd = '<span style="float:left;padding-left:15px;line-height:40px;color:blue;" class="align middle" name="' + dlgToolbarCtrlsAddName + '"><input class="align middle" type="checkbox" name="'
					+ dlgToolbarCtrlsAddName + '-checkbox"  id="' + dlgToolbarCtrlsAddName + '-checkbox-id" />&nbsp;<label class="align middle" for="' + dlgToolbarCtrlsAddName + '-checkbox-id">保存后继续添加<label></span>';
			var dlgToolbarCtrlsViewName = "dlg-toolbar-ctrls-view";
			var dlgToolbarCtrlsHtmlView = '<span style="float:left;padding-left:15px;line-height:40px;color:blue;" class="align middle" name="' + dlgToolbarCtrlsViewName + '"><input class="align middle" type="checkbox" name="'
					+ dlgToolbarCtrlsViewName + '-checkbox"  id="' + dlgToolbarCtrlsViewName + '-checkbox-id" />&nbsp;<label class="align middle" for="' + dlgToolbarCtrlsViewName + '-checkbox-id">保存后转查看<label></span>';
			// 工具栏
			var dlgToolbarCtrls = null;
			// --------------------------------end----------------------------
			// 实例化表单
			var formProxy = FormProxy.newOne();
			// 实例化查询表单
			var formProxyQuery = FormProxy.newOne();
			// ------------------------------------------------------------------
			// ------------------------------------------全局变量 end---------------------------}}
			// ------------------------------------------私有方法 start----------------------------{{
			// ------------------------------------------数据加载 start----------------------------{{
			/**
			 * 空函数，在弹出框消失后重写调用
			 * 
			 * @author 邓华锋
			 * @date 2015年10月9日
			 * 
			 */
			var getCallbackAfterGridLoaded = function() {
			}
			/**
			 * jqGrid数据加载
			 */
			var loadData = function() {
				jqGridCtrl = $id(p.theGridCtrlId).jqGrid(p.jqGridGlobalSetting);
				if ($.isFunction(p.jqGridSetting)) {
					p.jqGridSetting.call(ts, jqGridCtrl);
				}
			}
			/**
			 * 重新加载jqGrid数据
			 * 
			 * @author 邓华锋
			 * @date 2015年10月9日
			 * 
			 */
			ts.reloadGridData = function() {
				var vldResult = formProxyQuery.validateAll();
				if (!vldResult) {
					return;
				}
				var postData = {};
				// 封装查询参数
				if ($.isFunction(p.queryParam)) {
					p.queryParam.call(ts, postData, formProxyQuery);
				}
				// 加载jqGridCtrl
				jqGridCtrl.jqGrid("setGridParam", {
					postData : {
						filterStr : JSON.encode(postData, true)
					},
					page : jqGridCtrl.getGridParam("page")
				}).trigger("reloadGrid");
			}
			// ------------------------------------------数据加载 end----------------------------}}
			// ---------------------------------------------显示增改查对话框 start-----------------{{
			ts.submit=function(ts){
				// 收集并验证要提交的数据（如果验证不通过直接返回 return）
				var vldResult = formProxy.validateAll();
				if (!vldResult) {
					return false;
				}
				var postData ={};
				if(p.formPostDataWay=="key"){
					 postData = formProxy.getValues2();
				}else if(p.formPostDataWay=="serializeArray"){
					postData=$(theDlgId).serializeArray();
				}else{
					postData = formProxy.getValues();
				}
				toSaveData(postData, $(ts));
				return true;
			};
			/**
			 * 显示增改查对话框
			 * 
			 * @author 邓华锋
			 * @date 2015年10月9日
			 * 
			 * @param dataId
			 */
			var toShowTheDlg = function(dataId) {
				// 清除扩展的对话框按钮栏控件
				if (p.continuousSupported) {
					var jqButtonPane = jqDlgDom.siblings(".ui-dialog-buttonpane");
					dlgToolbarCtrls = jqButtonPane.find("[name='" + dlgToolbarCtrlsAddName + "']");
					dlgToolbarCtrls.remove();
				}
				if (p.viewAfterSaving) {
					var jqButtonPane = jqDlgDom.siblings(".ui-dialog-buttonpane");
					dlgToolbarCtrls = jqButtonPane.find("[name='" + dlgToolbarCtrlsViewName + "']");
					dlgToolbarCtrls.remove();
				}
				// 对话框配置
				var dlgConfig = p.dlgGlobalConfig;
				if (curAction == "add" && (p.continuousSupported || p.viewAfterSaving)) {
					// 显示“保存后继续添加”复选框
					dlgConfig.open = function(event, ui) {
						var jqButtonPane = jqDlgDom.siblings(".ui-dialog-buttonpane");
						var dlgToolbarCtrlsHtml = "";
						if (p.continuousSupported) {
							dlgToolbarCtrlsHtml += dlgToolbarCtrlsHtmlAdd;
						}
						if (p.viewAfterSaving) {
							dlgToolbarCtrlsHtml += dlgToolbarCtrlsHtmlView;
						}
						dlgToolbarCtrls = $($(dlgToolbarCtrlsHtml).appendTo(jqButtonPane));
						if (p.continuousSupported) {
							var theCheckbox = dlgToolbarCtrls.find("[name='" + dlgToolbarCtrlsAddName + "-checkbox']");
							var continuousFlag = jqDlgDom.prop("continuousFlag");
							theCheckbox.prop("checked", continuousFlag);
							theCheckbox.bind("click", function() {
								jqDlgDom.prop("continuousFlag", this.checked);
							});
						}
						if (p.viewAfterSaving) {
							var theCheckbox = dlgToolbarCtrls.find("[name='" + dlgToolbarCtrlsViewName + "-checkbox']");
							var isToView = jqDlgDom.prop("isToView");
							theCheckbox.prop("checked", isToView);
							theCheckbox.bind("click", function() {
								jqDlgDom.prop("isToView", this.checked);
							});
						}
					};
				}
				var isMETFun = $.isFunction(p.modElementToggle);
				// 用于修改时显示隐藏元素
				if (isMETFun) {
					p.modElementToggle.call(ts, false);
				}
				var isMETFunView = $.isFunction(p.viewElementToggle);
				if (isMETFunView) {
					p.viewElementToggle.call(ts, false);
				}
				if (curAction == "add") {
					if(!jQuery.isEmptyObject(p.addDlgConfig)){
						dlgConfig= $.extend(true,p.dlgGlobalConfig,p.addDlgConfig);
					}
					dlgConfig.title = p.dlgTitle + " - 新增";
					dlgConfig.buttons = {};
					if ($.isFunction(p.settingAddBtn)) {
						p.settingAddBtn.call(ts, dlgConfig.buttons,jqGridCtrl);
					}
					dlgConfig.buttons["保存"]=function() {
						ts.submit(this);
					};
					dlgConfig.buttons["取消"] =function() {
						jqDlgDom.prop("continuousFlag", false);
						$(this).dialog("close");
						// 隐藏表单验证消息
						formProxy.hideMessages();
						getCallbackAfterGridLoaded = function() {
						};
					};
				} else if (curAction == "mod") {
					if(!jQuery.isEmptyObject(p.modAndViewDlgConfig)){
						dlgConfig= $.extend(true,p.dlgGlobalConfig,p.modAndViewDlgConfig);
					}
					if(!jQuery.isEmptyObject(p.modDlgConfig)){
						dlgConfig= $.extend(true,p.dlgGlobalConfig,p.modAndViewDlgConfig,p.modDlgConfig);
					}
					// 显示隐藏元素
					if (isMETFun) {
						p.modElementToggle.call(ts, true);
					}
					dlgConfig.title = p.dlgTitle + " - 修改";
					dlgConfig.buttons = {};
					if ($.isFunction(p.settingModBtn)) {
						p.settingModBtn.call(ts, dlgConfig.buttons,cacheDataGridHelper.getRowData(p.pkFldName,dataId),jqGridCtrl);
					}
					dlgConfig.buttons["保存"]=function() {
							// 收集并验证要提交的数据（如果验证不通过直接返回 return）
							var vldResult = formProxy.validateAll();
							if (!vldResult) {
								return;
							}
							var postData = formProxy.getValues();
							postData[p.pkFldName] = dataId;
							toSaveData(postData, $(this));
					};
					dlgConfig.buttons["取消"] =function() {
						$(this).dialog("close");
						// 隐藏表单验证消息
						formProxy.hideMessages();
						getCallbackAfterGridLoaded = function() {
						};
					};
				} else if (curAction == "view") {
					if(!jQuery.isEmptyObject(p.modAndViewDlgConfig)){
						dlgConfig= $.extend(true,p.dlgGlobalConfig,p.modAndViewDlgConfig);
					}
					if(!jQuery.isEmptyObject(p.viewDlgConfig)){
						dlgConfig= $.extend(true,p.dlgGlobalConfig,p.modAndViewDlgConfig,p.viewDlgConfig);
					}
					// 用于修改时显示隐藏元素
					if (isMETFunView) {
						p.viewElementToggle.call(ts, true);
					}
					// == view 查看
					dlgConfig.title = p.dlgTitle + " - 查看";
					dlgConfig.buttons = {};
					if ($.isFunction(p.settingViewBtn)) {
						p.settingViewBtn.call(ts, dlgConfig.buttons,cacheDataGridHelper.getRowData(p.pkFldName,dataId),jqGridCtrl);
					}
					if(p.isShowViewShowModBtn){
						dlgConfig.buttons["修改 > "]=function() {
							$(this).dialog("close");
							//
							openModDlg(dataId);
						};
					}
					dlgConfig.buttons["关闭"] =function() {
							$(this).dialog("close");
							getCallbackAfterGridLoaded = function() {
							}
					};
				}
				//
				jqDlgDom.dialog(dlgConfig);
				initTheDlgCtrls(dataId);
			}
			/**
			 * 初始化模式窗口
			 * 
			 * @author 邓华锋
			 * @date 2015年10月9日
			 * 
			 * @param dataId
			 */
			var initTheDlgCtrls = function(dataId) {
				// 批量简单设置
				if (curAction == "add" || curAction == "mod") {
					jqDlgDom.find(":input").prop("disabled", false);
				} else {
					jqDlgDom.find(":input").prop("disabled", true);
				}
				if (curAction == "add") {
					// 增加时清空控件的值
					if ($.isFunction(p.addInit)) {
						p.addInit.call(ts,formProxy);
					}
				}
				//
				if (curAction == "view" || curAction == "mod") {
					var data = ajaxGetData(dataId);
				}
			}
			/**
			 * 打开新增对话框
			 * 
			 * @author denghuafeng
			 * @date 2015年10月9日
			 * 
			 * @param dataId
			 */
			var openAddDlg = function() {
				curAction = "add";
				toShowTheDlg();
			}

			/**
			 * 打开修改对话框
			 * 
			 * @author denghuafeng
			 * @date 2015年10月9日
			 * 
			 * @param dataId
			 */
			var openModDlg = function(dataId) {
				curAction = "mod";
				toShowTheDlg(dataId);

			}

			// ---------------------------------------------显示增改查对话框 end-----------------}}
			// ---------------------------------------数据保存 start-----------------------------------{{
			/**
			 * 提交保存数据
			 * 
			 * @author 邓华锋
			 * @date 2015年10月9日
			 * 
			 * @param postData
			 * @param jqDlg
			 */
			var toSaveData = function(postData, jqDlg) {
				// Ajax提交数据
				var dataId = ajaxSaveData(postData);
				// 保存成功后关闭对话框（否则直接return）
				jqDlg.dialog("close");
			}

			/**
			 * 保存数据 增加和修改
			 * 
			 * @author 邓华锋
			 * @date 2015年10月9日
			 * 
			 * @param postData
			 * @returns
			 */
			var ajaxSaveData = function(postData) {
				//post提交的数据中是否含有主键
				//var isHasPk=postData[p.pkFldName]?true:false;
				if (curAction == "add") {// 增加
					var ajax = Ajax.post(p.addUrl);
					if ($.isFunction(p.savePostDataChange)) {
						var result=p.savePostDataChange.call(ts, postData);
						if(result==false){
							return;
						}
					}
					ajax.data(postData);
					ajax.done(function(result, jqXhr) {
						if (result.type == "info") {
							postData[p.pkFldName] = result.data;
							// 刷新界面
							ts.reloadGridData();
							Layer.msgSuccess(result.message);
							// 连续新增
							if (p.continuousSupported) {
								curAction = "add";
								var continuousFlag = jqDlgDom.prop("continuousFlag");
								if (continuousFlag) {
									getCallbackAfterGridLoaded = function() {
										return function() {
											openAddDlg();
										};
									};
									return;
								}
							}
							// 转到查看对话框
							if (p.viewAfterSaving) {
								var isToView = jqDlgDom.prop("isToView");
								if (isToView) {
									getCallbackAfterGridLoaded = function() {
										return function() {
											openViewDlg(postData[p.pkFldName]);
										};
									};
								}
								return;
							}
						} else {
							Layer.msgWarning(result.message);
						}
					});
					ajax.go();
				} else if (curAction == "mod") {// 修改
					var ajax = Ajax.post(p.updUrl);
					if ($.isFunction(p.savePostDataChange)) {
						p.savePostDataChange.call(ts, postData);
					}
					ajax.data(postData);
					ajax.done(function(result, jqXhr) {
						if (result.type == "info") {
							// 刷新界面
							ts.reloadGridData();
							Layer.msgSuccess(result.message);
							// 转到查看对话框
							// 得到保存的数据对象的id
							if (p.viewAfterSaving) {
								getCallbackAfterGridLoaded = function() {
									return function() {
										openViewDlg(postData[p.pkFldName]);
									};
								};
								return;
							}
						} else {
							Layer.msgWarning(result.message);
						}
					});
					ajax.go();
				}

				return postData[p.pkFldName];
			}
			// ---------------------------------------数据保存 end-------------------------------------{{
			// -------------------------------------------删除------------------------------------{{
			/**
			 * 打开删除对话框
			 * 
			 * @author 邓华锋
			 * @date 2015年10月9日
			 * 
			 * @param dataId
			 */
			var openDelDlg = function(dataId) {
				var dataMap=cacheDataGridHelper.getRowData(p.pkFldName,dataId);
				var theSubject = "";
				// 获得删除确认提示
				if ($.isFunction(p.getDelComfirmTip)) {
					theSubject = p.getDelComfirmTip.call(ts, dataMap);
				}
				var theLayer = Layer.confirm(theSubject, function() {
					theLayer.hide();
					toDeleteData(dataId);
				});
			}

			/**
			 * 提交删除数据
			 * 
			 * @author 邓华锋
			 * @date 2015年10月9日
			 * 
			 * @param dataId
			 */
			var toDeleteData = function(dataId) {
				curAction = "del";
				var postData = {};
				if(isNumber(dataId)){
					dataId=parseInt(dataId);
				}
				postData[p.pkFldName] = dataId;
				if ($.isFunction(p.delPostDataChange)) {
					p.delPostDataChange.call(ts, postData);
				}
				// Ajax提交删除数据
				var dataId = ajaxDeleteData(postData);
			}

			/**
			 * 删除数据
			 * 
			 * @author denghuafeng
			 * @date 2015年10月9日
			 * 
			 * @param postData
			 * @returns
			 */
			var ajaxDeleteData = function(postData) {
				if (postData[p.pkFldName]) {
					var hintBox = Layer.progress("正在删除数据...");
					var ajax = Ajax.post(p.delUrl);
					ajax.data(postData);
					ajax.done(function(result, jqXhr) {
						if (result.type == "info") {
							Layer.msgSuccess(result.message);
							// 刷新界面
							ts.reloadGridData();
						} else {
							Layer.msgWarning(result.message);
						}
					});
					ajax.always(function() {
						hintBox.hide();
					});
					ajax.go();
					return postData[p.pkFldName];
				}
				return null;
			}
			// -------------------------------------查看----------------------------------------{{
			/**
			 * 打开查看对话框
			 * 
			 * @author 邓华锋
			 * @date 2015年10月9日
			 * 
			 * @param dataId
			 */
			var openViewDlg = function(dataId) {
				curAction = "view";
				toShowTheDlg(dataId);
			}
			/**
			 * 查看单个对象的数据
			 * 
			 * @author 邓华锋
			 * @date 2015年10月9日
			 * 
			 * @param dataId
			 * @returns
			 */
			var ajaxGetData = function(dataId) {
				var data = null;
				if (p.isUsePageCacheToView) {
					data = cacheDataGridHelper.getRowData(p.pkFldName,dataId);
					if (data != null) {
						// 保留原数据，用于验证对数据是否进行了修改
						if ($.isFunction(p.saveOldData)) {
							p.saveOldData.call(ts, data);
						}
						// 修改和查看是给控件赋值
						if ($.isFunction(p.modAndViewInit)) {
							p.modAndViewInit.call(ts, data,formProxy);
						}else{
							if (curAction == "mod"&&$.isFunction(p.modInit)) {
								p.modInit.call(ts, data,formProxy);
							}else if (curAction == "view"&&$.isFunction(p.viewInit)) {
								p.viewInit.call(ts, data,formProxy);
							}
						}
					}
				} else {
					var postData = {};
					postData[p.pkFldName] = parseInt(dataId);
					var ajax = Ajax.post(p.viewUrl);
					ajax.data(postData);
					ajax.done(function(result, jqXhr) {
						if (result.type == "info") {
							data = result.data;
							if (data != null) {
								// 保留原数据，用于验证对数据是否进行了修改
								if ($.isFunction(p.saveOldData)) {
									p.saveOldData.call(ts, data);
								}
								// 修改和查看是给控件赋值
								if ($.isFunction(p.modAndViewInit)) {
									p.modAndViewInit.call(ts, data,formProxy);
								}
							}
							return result.data;
						} else {
							Layer.msgWarning(result.message);
						}
					});
					ajax.always(function() {
					});
					ajax.go();
				}
			}
			// ---------------------------------------------------------------------------------}}
			// -------------------------------------------其他------------------------------------{{
			/**
			 * 调整控件大小
			 * 
			 * @author 邓华锋
			 * @date 2015年10月9日 下午7:00:35
			 * 
			 */
			var adjustCtrlsSize = function() {
				var jqMainPanel =  $(ts);
				var mainWidth = jqMainPanel.width();
				var mainHeight = jqMainPanel.height();
				var pager=p.pager;
				var pagerId=(pager==null||pager=="")?"#theGridPager":pager;
				var pagerHeight = $(pagerId).height();
				var jqGridBox = $("#gbox_" + p.theGridCtrlId);
				var headerHeight = $("> div.ui-jqgrid-view > div.ui-jqgrid-hdiv",jqGridBox).height();
				jqGridCtrl.setGridWidth(mainWidth - 1);
				jqGridCtrl.setGridHeight(mainHeight - headerHeight - pagerHeight- 3 - 60);
			}
			// --------------------------------------------------------------------------------}}
			// ------------------------------------------私有方法 end------------------------------}}
			// ------------------------------------------执行方法调用 start---------------------{{
			if ($.isFunction(p.formProxyInit)) {
				p.formProxyInit.call(ts, formProxy);
			}
			if ($.isFunction(p.formProxyQueryInit)) {
				p.formProxyQueryInit.call(ts, formProxyQuery);
			}

			// 页面基本布局
			$id(p.rootPanelId).layout(p.rootPanelSetting);
			// 隐藏布局north分割线
			$(".ui-layout-resizer.ui-layout-resizer-north").css("height", "0");
			loadData();
			jqDlgDom.bind('dialogclose', function(event, ui) {// dialog关闭事件
				// 隐藏表单验证消息
				formProxy.hideMessages();
				curAction = "";
				getCallbackAfterGridLoaded = function() {
				};
			});

			$id(p.btnToAddId).click(openAddDlg);
			$id(p.btnToQryId).click(ts.reloadGridData);
			$(document).on("click", ".dlgupd", function() {
				openModDlg($(this).attr("cellValue"));
			});
			$(document).on("click", ".dlgdel", function() {
				openDelDlg($(this).attr("cellValue"));
			});
			$(document).on("click", ".dlgview", function() {
				openViewDlg($(this).attr("cellValue"));
			});
			// 页面自适应
			winSizeMonitor.start(adjustCtrlsSize);

			// 页面加载
			if ($.isFunction(p.pageLoad)) {
				p.pageLoad.call(ts,jqGridCtrl,cacheDataGridHelper);
			}
			// ------------------------------------------执行方法调用 end---------------------}}
		});
	}
}));