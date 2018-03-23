package priv.starfish.common.model;

import java.util.Collections;
import java.util.List;

/**
 * 用于jqgrid分页显示，查询数据返回到前台
 * 
 * @author liangdong
 *
 * @param <T>
 */
public class JqGridPage<T> {

	/**
	 * 传给前台的grid结果集
	 */
	private List<T> rows = Collections.emptyList();

	/**
	 * 当前页 /前台显示第几页
	 */
	private Integer pageNumber = 0;

	/**
	 * 总页数
	 */
	private Integer totalPages = 0;

	/**
	 * 总记录数
	 */
	private Integer totalCount = 0;

	/**
	 * 用户需要传入数据
	 */
	private Object extraData;

	/**
	 * 取总页数.
	 * 
	 * @return long
	 */
	private int calcTotalPages(Integer totalCount, Integer pageSize) {
		if (pageSize < 1) {
			throw new IllegalArgumentException("分页大小[pageSize]不能小于1");
		}
		if (totalCount % pageSize == 0) {
			return totalCount / pageSize;
		} else {
			return totalCount / pageSize + 1;
		}
	}

	/**
	 * 返回空页
	 */
	public JqGridPage() {
		this(Collections.<T> emptyList(), 0, 0, 10);
	}

	/**
	 * @param gridResult
	 *            grid结果集
	 * @param pageNumber
	 *            当前页
	 * @param pageSize
	 *            每页多少
	 * @param totalCount
	 *            总记录数
	 */
	public JqGridPage(List<T> rows, Integer pageNumber, Integer totalCount, Integer pageSize) {
		this.rows = rows;
		this.pageNumber = pageNumber;
		this.totalPages = this.calcTotalPages(totalCount, pageSize);
		this.totalCount = totalCount;
	}

	public static <T> JqGridPage<T> fromPaginatedList(PaginatedList<T> paginatedList) {
		Pagination pagination = paginatedList.getPagination();
		return new JqGridPage<T>(paginatedList.getRows(), pagination.getPageNumber(), pagination.getTotalCount(), pagination.getPageSize());
	}

	/**
	 * @return gridResult
	 */
	public List<T> getRows() {
		return rows;
	}

	/**
	 * @param gridResult
	 *            赋值 gridResult
	 */
	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	/**
	 * @return page
	 */
	public Integer getPageNumber() {
		return pageNumber;
	}

	/**
	 * @param pageNumber
	 *            赋值 page
	 */
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * @return total
	 */
	public Integer getTotalPages() {
		return totalPages;
	}

	/**
	 * @param totalPages
	 *            赋值 total
	 */
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * @return records
	 */
	public Integer getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount
	 *            赋值 records
	 */
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return userdata
	 */
	public Object getExtraData() {
		return extraData;
	}

	/**
	 * @param extraData
	 *            赋值 userdata
	 */
	public void setExtraData(Object extraData) {
		this.extraData = extraData;
	}
}
