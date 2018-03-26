//被选中的属性参照id集合
var idsSelected = [];
// 是否当前操作为loadComplete
var loadComplete = false;
// 从checkedIds删除id
function removeItemFromList(item, list) {
	if (list.length < 1)
		return;
	for (var i = list.length - 1; i >= 0; i--) {
		var _item = list[i];
		if (_item == item) {
			list.removeAt(i);
			break;
		}
	}
}
// 从refIds删除refIds数组
function removeItemsFromList(items, list) {
	for (var i = 0; i < items.length; i++) {
		var _item = items[i];
		removeItemFromList(_item, list);
	}
}
//去重操作
Array.prototype.unique = function() {
	var n = {}, r = [], len = this.length, val, type;
	for (var i = 0; i < this.length; i++) {
		val = this[i];
		type = typeof val;
		if (!n[val]) {
			n[val] = [ type ];
			r.push(val);
		} else if (n[val].indexOf(type) < 0) {
			n[val].push(type);
			r.push(val);
		}
	}
	return r;
}
//
function initMyJqGrid(jqGridId, jqGridConfig, gridHelper, loadCompleteCallBack) {
	var jqGridCtrl = null;
	jqGridConfig = jqGridConfig || {};
	//
	var multiselect = jqGridConfig.multiselect;
	if(!multiselect){
		var tmpConfig = {
				loadComplete : function(gridData) {
					gridHelper.cacheData(gridData);
					//
					if(isFunction(loadCompleteCallBack)){
						loadCompleteCallBack();
					}
				}
		};
		//
		merge(jqGridConfig, tmpConfig);
		jqGridCtrl = $id(jqGridId).jqGrid(jqGridConfig);
		return jqGridCtrl;
	}
	//
	var theConfig = {
		onSelectRow : function(rowid, status) {
			if (!loadComplete) {
				if (status) {
					idsSelected.add(rowid);
				} else {
					removeItemFromList(rowid, idsSelected);
				}
			}
		},
		onSelectAll : function(aRowids, status) {
			if (!loadComplete) {
				if (status) {
					$.merge(idsSelected, aRowids);
					var tmpArr = idsSelected.unique();
					idsSelected = tmpArr;
				} else {
					removeItemsFromList(aRowids, idsSelected);
				}
			}
		},
		loadComplete : function(gridData) {
			gridHelper.cacheData(gridData);
			// 设置标志
			loadComplete = true;
			setSelectedByIds(idsSelected, jqGridId);
			//
			if(isFunction(loadCompleteCallBack)){
				loadCompleteCallBack();
			}
		}
	};
	//
	theConfig = merge(theConfig, jqGridConfig);
	//
	jqGridCtrl = $id(jqGridId).jqGrid(theConfig);
	// 设置选中的属性参照
	this.setSelectedByIds = function(idsSelected, jqGridId) {
		// 当前grid中所有id集合
		var ids = jqGridCtrl.jqGrid("getDataIDs");
		var currentGridIdLength = ids.length;
		var selectedIdLength = idsSelected.length;
		//
		for (var i = 0; i < selectedIdLength; i++) {
			var idSelected = idsSelected[i];
			jqGridCtrl.jqGrid("setSelection", idSelected);
			// 从ids集合中去除idSelected
			removeItemFromList(idSelected, ids);
		}
		//表明缓存中的id为当前grid的全选数据，此时勾选全选checkbox
		if (ids.length < 1) {
			var selectAllBox = "cb_" + jqGridId;
			$id(selectAllBox).prop("checked", true);
		}
		// 设置选中完成后，退出loadComplete操作
		loadComplete = false;
	}
	return jqGridCtrl;
}