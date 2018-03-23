package priv.starfish.common.model;


import priv.starfish.common.util.NumUtil;

/**
 * 分页调整基础类
 * 
 * @author koqiui
 * 
 */
public class GenericPager {
	private final Pagination pagination;
	private int totalPages = 0;

	public GenericPager(Pagination pagination) {
		this.pagination = pagination;
		// 纠正分页号
		this.totalPages = NumUtil.ceil(this.pagination.getTotalCount(), this.pagination.getPageSize());
		int rightPageNumber = NumUtil.narrow(this.pagination.getPageNumber(), 1, this.totalPages);
		this.pagination.setPageNumber(rightPageNumber);
	}

	public int getTotalPages() {
		return this.totalPages;
	}

	public Pagination firstPage() {
		if (this.totalPages <= 0) {
			return null;
		}
		this.pagination.setPageNumber(1);
		return this.pagination;
	}

	public Pagination lastPage() {
		if (this.totalPages <= 0) {
			return null;
		}
		this.pagination.setPageNumber(this.totalPages);
		return this.pagination;
	}

	public Pagination nextPage() {
		if (this.isLastPage()) {
			return null;
		}
		this.pagination.setPageNumber(this.pagination.getPageNumber() + 1);
		return this.pagination;
	}

	public Pagination prevPage() {
		if (this.isFirstPage()) {
			return null;
		}
		this.pagination.setPageNumber(this.pagination.getPageNumber() - 1);
		return this.pagination;
	}

	private boolean isFirstPage() {
		return this.totalPages <= 0 || pagination.getPageNumber() == 1;
	}

	private boolean isLastPage() {
		return this.totalPages <= 0 || pagination.getPageNumber() == this.totalPages;
	}
}
