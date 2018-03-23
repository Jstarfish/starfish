package priv.starfish.common.jdbc;

import priv.starfish.common.util.StrUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author Hu Changwei
 * @version 1.0
 * 
 *          <pre>
 * FROM
 * ON
 * JOIN
 * WHERE
 * GROUP BY
 * WITH CUBE 或 WITH ROLLUP
 * HAVING
 * SELECT
 * DISTINCT
 * ORDER BY
 * TOP
 *          </pre>
 */
public class SelectBuilder extends SqlBuilder {
	protected List<String> fieldList = new ArrayList<String>();
	protected boolean useDistinct = false;
	protected List<String> tableList = new ArrayList<String>();
	protected List<String> groupByList = new ArrayList<String>();
	protected List<String> havingList = new ArrayList<String>();
	protected List<String> orderByList = new ArrayList<String>();
	//
	protected boolean useAs = false;
	protected String alias = null;

	//
	public static SelectBuilder getInstance() {
		return new SelectBuilder();
	}

	public SelectBuilder select(String... fieldStrs) {
		this.isFieldState = false;
		for (String fieldStr : fieldStrs) {
			fieldList.add(fieldStr);
		}
		return this;
	}

	public SelectBuilder distinct() {
		useDistinct = true;
		return this;
	}

	public SelectBuilder from(String... tableStrs) {
		this.isFieldState = false;
		for (String tableStr : tableStrs) {
			tableList.add(tableStr);
		}
		return this;
	}

	public SelectBuilder groupBy(String... groupByStrs) {
		this.isFieldState = false;
		for (String groupByStr : groupByStrs) {
			groupByList.add(groupByStr);
		}
		return this;
	}

	public SelectBuilder having(String... havingStrs) {
		this.isFieldState = false;
		for (String havingStr : havingStrs) {
			havingList.add(havingStr);
		}
		return this;
	}

	protected String getHavingStr() {
		return StrUtil.join(this.havingList, "\n   AND ");
	}

	public SelectBuilder orderBy(String... orderByStrs) {
		this.isFieldState = false;
		for (String orderByStr : orderByStrs) {
			orderByList.add(orderByStr);
		}
		return this;
	}

	public SelectBuilder orderBy(String orderField, boolean desc) {
		this.isFieldState = false;
		String orderByStr = orderField + " " + (desc ? OrderDesc : OrderAsc);
		return orderBy(orderByStr);
	}

	public SelectBuilder alias(String alias) {
		this.isFieldState = false;
		this.useAs = false;
		this.alias = alias;
		return this;
	}

	public SelectBuilder asAlias(String alias) {
		this.isFieldState = false;
		this.useAs = true;
		this.alias = alias;
		return this;
	}

	public String toSQL() throws BuildingException {
		if (fieldList.size() <= 0 || (tableList.size() <= 0)) {
			throw new BuildingException("表名列表和from字符串不能同时为空，并且字段列表不能为空。");
		}
		StringBuilder sb = new StringBuilder();
		//
		sb.append("SELECT ");
		if (this.useDistinct) {
			sb.append("DISTINCT  ");
		}
		sb.append(StrUtil.join(fieldList, ", "));
		sb.append("\n");
		//
		sb.append("FROM   ").append(StrUtil.join(tableList, ", "));
		//
		if (whereList.size() > 0) {
			sb.append("\n");
			sb.append("WHERE  ").append(getWhereStr());
		}
		//
		if (groupByList.size() > 0) {
			sb.append("\n");
			sb.append("GROUP BY  ").append(StrUtil.join(groupByList, ", "));
			//
			if (havingList.size() > 0) {
				sb.append("\n");
				sb.append("HAVING    ").append(getHavingStr());
			}
		}
		//
		if (orderByList.size() > 0) {
			sb.append("\n");
			sb.append("ORDER BY  ").append(StrUtil.join(orderByList, ", "));
		}
		//
		String basicSql = "";
		if (StrUtil.hasText(this.alias)) {
			basicSql = WrapItemStrFilter.filter(sb.toString()) + (this.useAs ? " AS " : " ") + this.alias;
		} else {
			basicSql = sb.toString();
		}
		//
		String prepended = this.prependList.size() > 0 ? StrUtil.join(this.prependList, "\n") + "\n" : "";
		String appended = this.appendList.size() > 0 ? "\n" + StrUtil.join(this.appendList, "\n") : "";
		return prepended + basicSql + appended;
	}
}
