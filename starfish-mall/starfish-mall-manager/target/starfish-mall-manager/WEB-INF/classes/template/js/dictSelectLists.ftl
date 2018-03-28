var __dictSelectLists = {};
<#list dictSelectListNames as listName >
__dictSelectLists["${listName}"] = ${dictSelectListJsons["${listName}"]};
</#list>

//获取（用于下拉列表的）指定枚举字典信息
function getDictSelectList(name, unSelectValue, unSelectText, defaultValue) {
	var dictSelectList = __dictSelectLists[name];
	if (dictSelectList) {
		dictSelectList = merge({}, dictSelectList);
		//
		delete dictSelectList["unSelectedItem"];
		delete dictSelectList["defaultValue"];
		//
		if (unSelectText) {
			dictSelectList["unSelectedItem"] = {};
			dictSelectList["unSelectedItem"]["value"] = unSelectValue;
			dictSelectList["unSelectedItem"]["text"] = unSelectText;
		}
		if (typeof defaultValue != "undefined") {
			dictSelectList["defaultValue"] = defaultValue;
		}
	}
	return dictSelectList;
}