package priv.starfish.mall.comn.dto;

/**
 * 长整型sequence信息（模拟oracle表的seqence功能用）
 * 
 * @author koqiui
 * @date 2015年10月20日 下午8:10:56
 *
 */
public class LongSeqInfo {
	private String tableName;

	private Long id;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
