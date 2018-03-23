//中文转拼音
var __chsToPinyinPath = "/xutil/pinyin.do";
function setChsToPinyinPath(chsToPinyinPath) {
	__chsToPinyinPath = chsToPinyinPath;
}

function chsToPinyin(chsStr, asJianpin, callback) {
	var ajax = Ajax.get(__chsToPinyinPath).params({
		chsStr : chsStr,
		asJianpin : asJianpin || false
	});
	ajax.done(callback);
	ajax.fail(callback);
	ajax.go();
}