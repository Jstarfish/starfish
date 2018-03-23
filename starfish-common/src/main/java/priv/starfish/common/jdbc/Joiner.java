package priv.starfish.common.jdbc;

import java.util.ArrayList;
import java.util.List;

import priv.starfish.common.jdbc.SqlBuilder.BuildingException;
import priv.starfish.common.util.StrUtil;

/**
 * 
 * @author Hu Changwei
 * @version 1.0
 */
public class Joiner {
	/**
	 * 连接类型
	 */
	public static enum JoinType {
		CrossJoin, InnerJoin, LeftJoin, RightJoin, FullJoin;
		// 默认连接类型
		public static final JoinType Default = InnerJoin;
	}

	protected JoinType joinType = JoinType.Default;
	protected String partX = null;
	protected String partY = null;
	protected List<String> onList = new ArrayList<String>();

	private void checkJoinPartType(Object partParam) throws BuildingException {
		if (partParam == null || !(partParam instanceof String || partParam instanceof Joiner)) {
			throw new BuildingException("join 的参数类型只能为 String 或 cn.snsoft.platform.sql.SqlBuilder.Joiner 或其子类！");
		}
	}

	protected Joiner(JoinType joinType) {
		if (joinType != null) {
			this.joinType = joinType;
		}
	}

	public Joiner join(Object part1, Object part2) throws BuildingException {
		checkJoinPartType(part1);
		checkJoinPartType(part2);
		this.partX = part1.toString();
		this.partY = part2.toString();
		return this;
	}

	public Joiner on(String... onStrs) {
		for (String onStr : onStrs) {
			onList.add(onStr);
		}
		return this;
	}

	protected String getJoinName() {
		return this.joinType.name();
	}

	public String toString() {
		String onStr = this.onList.size() > 1 ? SqlBuilder.WrapItemStrFilter.filter(StrUtil.join(this.onList, " AND "))
				: StrUtil.join(this.onList, " AND ");
		return this.partX + " " + this.getJoinName() + " " + this.partY + " ON " + onStr;
	}

	// 级联生成连接链
	public static Joiner crossJoin(Object part1, Object part2) throws BuildingException {
		return new CrossJoiner().join(part1, part2);
	}

	// 级联生成连接链
	public static Joiner innerJoin(Object part1, Object part2) throws BuildingException {
		return new InnerJoiner().join(part1, part2);
	}

	// 级联生成连接链
	public static Joiner leftJoin(Object part1, Object part2) throws BuildingException {
		return new LeftJoiner().join(part1, part2);
	}

	// 级联生成连接链
	public static Joiner rightJoin(Object part1, Object part2) throws BuildingException {
		return new RightJoiner().join(part1, part2);
	}

	// 级联生成连接链
	public static Joiner fullJoin(Object part1, Object part2) throws BuildingException {
		return new FullJoiner().join(part1, part2);
	}
}

//
class CrossJoiner extends Joiner {
	public CrossJoiner() throws BuildingException {
		super(JoinType.CrossJoin);
	}
}

class InnerJoiner extends Joiner {
	public InnerJoiner() throws BuildingException {
		super(JoinType.InnerJoin);
	}
}

class LeftJoiner extends Joiner {
	public LeftJoiner() throws BuildingException {
		super(JoinType.LeftJoin);
	}
}

class RightJoiner extends Joiner {
	public RightJoiner() throws BuildingException {
		super(JoinType.RightJoin);
	}
}

class FullJoiner extends Joiner {
	public FullJoiner() throws BuildingException {
		super(JoinType.FullJoin);
	}
}