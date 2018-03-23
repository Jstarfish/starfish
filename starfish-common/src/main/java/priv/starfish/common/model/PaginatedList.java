package priv.starfish.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询结果
 * 
 * @author koqiui
 * @date 2015年10月19日 下午7:49:50
 * 
 * @param <TData>
 */
public class PaginatedList<TData> {
	/** 分页信息（可能已根据结果调整过） */
	private Pagination pagination = new Pagination();
	/** 结果数据记录 */
	private List<TData> rows = new ArrayList<TData>(0);

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public List<TData> getRows() {
		return rows;
	}

	public void setRows(List<TData> rows) {
		this.rows = rows;
	}

	public static <TData> PaginatedList<TData> newOne() {
		return new PaginatedList<TData>();
	}
}
