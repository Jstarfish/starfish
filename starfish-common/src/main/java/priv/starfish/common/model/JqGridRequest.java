package priv.starfish.common.model;

import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.TypeUtil;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class JqGridRequest implements Serializable {

	private static final long serialVersionUID = -1845230676944665285L;
	// 当前页码
	private Integer pageNumber;
	// 页面可显示行数
	private Integer pageSize;
	// 用于排序的列名
	private String sortName;
	// 排序的方式desc/asc
	private String sortOrder;
	// 是否是搜索请求
	private Boolean isSearch;
	// 已经发送的请求的次数
	private Integer reqTimes;

	private MapContext filterItems = new MapContext();

	public void setFilterStr(String filterStr) {
		if (StrUtil.hasText(filterStr)) {
			Map<String, Object> tmpMap = JsonUtil.fromJson(filterStr, TypeUtil.TypeRefs.StringObjectMapType);
			filterItems.clear();
			filterItems.putAll(tmpMap);
		}
	}

	public MapContext getFilterItems() {
		return filterItems;
	}

	public <T> T getFilterItemValue(String itemName, Class<T> valueType) {
		return filterItems.getTypedValue(itemName, valueType);
	}

	public Object getFilterItemValue(String itemName) {
		return filterItems.get(itemName);
	}

	public void setFilterItems(MapContext filterItems) {
		this.filterItems = filterItems;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public boolean getIsSearch() {
		return isSearch;
	}

	public void setIsSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}

	public Integer getReqTimes() {
		return reqTimes;
	}

	public void setReqTimes(Integer reqTimes) {
		this.reqTimes = reqTimes;
	}

	public PaginatedFilter toPaginatedFilter() {
		return JqGridRequest.toPaginatedFilter(this);
	}

	public static JqGridRequest fromRequest(HttpServletRequest request) {
		String tmpStr = null;
		JqGridRequest gridRequest = new JqGridRequest();

		tmpStr = request.getParameter("pageNumber");
		gridRequest.setPageNumber(Integer.valueOf(tmpStr));

		tmpStr = request.getParameter("pageSize");
		gridRequest.setPageSize(Integer.valueOf(tmpStr));

		tmpStr = request.getParameter("filterStr");

		gridRequest.setFilterStr(tmpStr);

		return gridRequest;
	}

	public static PaginatedFilter toPaginatedFilter(JqGridRequest jqGridRequest) {
		PaginatedFilter paginatedFilter = PaginatedFilter.newOne();
		Pagination pagination = paginatedFilter.getPagination();
		pagination.setPageNumber(jqGridRequest.getPageNumber());
		pagination.setPageSize(jqGridRequest.getPageSize());
		paginatedFilter.setFilterItems(jqGridRequest.getFilterItems());
		//
		return paginatedFilter;
	}

}
