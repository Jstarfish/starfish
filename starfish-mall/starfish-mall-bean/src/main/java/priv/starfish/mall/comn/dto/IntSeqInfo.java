package priv.starfish.mall.comn.dto;

/**
 * 整数sequence信息（模拟oracle表的seqence功能用）
 * 
 * @author koqiui
 * @date 2015年10月20日 下午8:10:56
 *
 */
public class IntSeqInfo {
	private String tableName;

	private Integer id;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
