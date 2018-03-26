//
function loadPrmtTagsData() {
	var ajax = Ajax.post("/market/prmtTag/list/get");
	ajax.done(function(result, jqXhr) {
		if (result.type == "info") {
			var cats = result.data;
		} else {
			Layer.msgWarning(result.message);
		}
	});
	ajax.go();
}