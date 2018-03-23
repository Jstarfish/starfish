package priv.starfish.common.jdbc;


import priv.starfish.common.util.StrUtil;

/**
 * 
 * 
 * @author Hu Changwei
 * @version 1.0
 */
public class DeleteBuilder extends SqlBuilder {
	public static final boolean useAsterisk = false;
	//
	protected String alias = null;

	public static DeleteBuilder getInstance() {
		return new DeleteBuilder();
	}

	public DeleteBuilder deleteFrom(String tableName) {
		this.isFieldState = false;
		this.tableName = tableName;
		return this;
	}

	public DeleteBuilder alias(String alias) {
		this.isFieldState = false;
		this.alias = alias;
		return this;
	}

	@Override
	public String toSQL() throws BuildingException {
		if (!StrUtil.hasText(this.tableName)) {
			throw new BuildingException("表名不能为空。");
		}
		StringBuilder sb = new StringBuilder();
		//
		sb.append("DELETE ");
		if (useAsterisk) {
			sb.append("* ");
		}
		sb.append("\n");
		//
		sb.append("FROM   ").append(this.tableName);
		if (StrUtil.hasText(this.alias)) {
			sb.append("	").append(this.alias);
		}
		//
		if (whereList.size() > 0) {
			sb.append("\n");
			sb.append("WHERE  ").append(getWhereStr());
		}
		//
		String prepended = this.prependList.size() > 0 ? StrUtil.join(this.prependList, "\n") + "\n" : "";
		String appended = this.appendList.size() > 0 ? "\n" + StrUtil.join(this.appendList, "\n") : "";
		return prepended + sb.toString() + appended;
	}

}
