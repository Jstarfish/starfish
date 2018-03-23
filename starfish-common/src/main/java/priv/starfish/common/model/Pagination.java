package priv.starfish.common.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonZeroDeserializer;

/**
 * 分页基本信息
 * 
 * @author koqiui
 * 
 */
public class Pagination {
	/** 结果记录总数 */
	@JsonDeserialize(using = JsonZeroDeserializer.class)
	private int totalCount = 0;
	/** 分页大小（每页结果记录数） */
	private int pageSize = 10;
	/** 目标页码（从1开始） */
	private int pageNumber = 1;
	//
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Range<Integer> pageIndexRange = Range.newOne(-1, -1);

	private void adjustPageIndexRange() {
		int fromIndex = -1;
		int toIndex = -1;
		if (totalCount > 0) {
			fromIndex = (pageNumber - 1) * pageSize;
			toIndex = Math.min(totalCount - 1, pageNumber * pageSize - 1);
		}
		pageIndexRange.setFrom(fromIndex);
		pageIndexRange.setTo(toIndex);
	}

	public void reset() {
		this.setPageNumber(1);
	}

	//

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		//
		this.adjustPageIndexRange();
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
		//
		this.adjustPageIndexRange();
	}

	/** 【根据结果总数量,获取获取在当前分页大小和页码下第一条和最后一条记录在总记录中的索引位置】 */
	public Range<Integer> getPageIndexRange() {
		return pageIndexRange;
	}

	/** 【忽略结果总数量】获取在当前分页大小和页码下的第一条记录的索引位置 */
	public Integer getPageIndexFrom() {
		return (pageNumber - 1) * pageSize;
	}

	public void setPageIndexRange(Range<Integer> pageIndexRange) {
		this.pageIndexRange = pageIndexRange;
	}

	public static Pagination newOne() {
		return new Pagination();
	}
}
