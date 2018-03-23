//分页大小（页面级）
var qry_pageSize = 10;
// [搜索用]的关键字符串
var qry_keywords = null;
//
// 过滤条件项
var qry_filterItems = {};
// 排序项
var qry_sortItems = {};
// 分页信息
var qry_pagination = {
	totalCount : 0,
	pageSize : qry_pageSize,
	pageNumber : 1
};

// 根据 新的的分页信息（或新的页码） 更新 本地分页信息
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

// 根据最新的信息生成新的查询条件
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

// 执行统一查询 ==========================================
function doTheQuery() {
	var postData = newQryFilterInfo();
	// ajax post 请求...

	// 结果返回...
	var paginatedList = null;
	// result.data;
	// 利用返回的分页信息
	var refPagination = paginatedList.pagination;
	//
	resetPagination(refPagination);
	//注意：jqPaginator的当前页面为 currentPage
	refPagination.currentPage = refPagination.pageNumber;
	// 根据新的分页信息（refPagination）刷新jqPaginator控件
	// ...
}

// 关键字 等
function keywordsAndQuery() {
	// 重建 qry_keywords
	// qry_keywords = ...;
	//
	// 重置页码
	resetPagination(1);
	//
	doTheQuery();
}

// 查询 按钮等
function filterAndQuery() {
	// 重建 qry_filterItems
	// qry_filterItems = {...};
	// 或 修改 qry_filterItems
	// merge(qry_filterItems, {...});
	//
	// 重置页码
	resetPagination(1);
	//
	doTheQuery();
}

// 排序 按钮等
function sortAndQuery() {
	// 重建 qry_sortItems
	// qry_sortItems = {...};
	// 或 修改 qry_sortItems
	// merge(qry_sortItems, {...});
	//
	// 重置页码
	resetPagination(1);
	//
	doTheQuery();
}

// 排序 按钮等
function pageNoAndQuery(pageNo) {
	// 变更页码
	resetPagination(pageNo);
	//
	doTheQuery();
}