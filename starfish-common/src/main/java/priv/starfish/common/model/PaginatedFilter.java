package priv.starfish.common.model;

import priv.starfish.common.util.MapContext;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 分页查询过滤条件
 * 
 * @author koqiui
 * @date 2015年10月19日 下午7:49:29
 *
 */
public class PaginatedFilter {
	/** [搜索用]的关键字符串 */
	private String keywords;
	/** 过滤条件项 */
	private MapContext filterItems = MapContext.newOne();
	/** 排序项 */
	private LinkedHashMap<String, String> sortItems;
	/** 分页信息 */
	private Pagination pagination = Pagination.newOne();

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public MapContext getFilterItems() {
		return filterItems;
	}

	public void setFilterItems(MapContext filterItems) {
		this.filterItems = filterItems;
	}

	public void setFilterItems(Map<String, Object> filterItems) {
		if (this.filterItems == null) {
			this.filterItems = new MapContext();
		} else {
			this.filterItems.clear();
		}
		this.filterItems.putAll(filterItems);
	}

	public LinkedHashMap<String, String> getSortItems() {
		return sortItems;
	}

	public void setSortItems(LinkedHashMap<String, String> sortItems) {
		this.sortItems = sortItems;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public static PaginatedFilter newOne() {
		return new PaginatedFilter();
	}

	@Override
	public String toString() {
		return "PaginatedFilter [keywords=" + keywords + ", filterItems=" + filterItems + ", sortItems=" + sortItems + ", pagination=" + pagination + "]";
	}
}
