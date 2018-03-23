package priv.starfish.common.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import priv.starfish.common.base.IStringFilter;
import priv.starfish.common.util.DateUtil;
import priv.starfish.common.util.StrUtil;

/**
 * 
 * @author Hu Changwei
 * @version 1.0
 */
public abstract class SqlBuilder {
	public static class BuildingException extends RuntimeException {
		private static final long serialVersionUID = 796828287529766454L;

		public BuildingException() {
			super();
		}

		public BuildingException(String message) {
			super(message);
		}

		public BuildingException(String message, Throwable cause) {
			super(message, cause);
		}

		public BuildingException(Throwable cause) {
			super(cause);
		}
	}

	// 默认占位符
	public static final String JdbcParamPlaceHolder = "?";

	// 默认占位符制造类
	private static class OgnlPlaceHolderMaker implements IPlaceHolderMaker {
		private final static String prefix = "#{";
		private final static String suffix = "}";

		@Override
		public String makePlaceHolder(String orginalStr) {
			return prefix + orginalStr + suffix;
		}

		@Override
		public boolean isPlaceHolder(String valueStr) {
			if (!StrUtil.hasText(valueStr)) {
				return false;
			}
			return valueStr.equals(JdbcParamPlaceHolder) || valueStr.startsWith(prefix) && valueStr.endsWith(suffix);
		}
	}

	public static final IPlaceHolderMaker DefaultPlaceHolderMaker = new OgnlPlaceHolderMaker();

	//
	protected IPlaceHolderMaker placeHolderMaker = SqlBuilder.DefaultPlaceHolderMaker;

	public void setPlaceHolderMaker(IPlaceHolderMaker placeHolderMaker) {
		this.placeHolderMaker = placeHolderMaker;
	}

	public boolean isPlaceHolder(Object value) {
		if (value instanceof String) {
			return this.placeHolderMaker.isPlaceHolder((String) value);
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public static String toValueStr(Object value) {
		if (value == null) {
			return StrUtil.NullStr;
		}
		if (value instanceof CharSequence) {
			return strVal(((CharSequence) value).toString());
		}
		if (value instanceof Date) {
			return strVal(DateUtil.toStdTimestampStr((Date) value));
		}
		if (value instanceof Character) {
			return strVal(((Character) value).toString());
		}
		if (value instanceof Enum) {
			return strVal(((Enum) value).name());
		}
		return value.toString();
	}

	// 常量字符串
	public static final String ParenthesesLeft = "(";
	public static final String ParenthesesRight = ")";
	// 常量字符串-排序
	public static final String OrderAsc = "ASC";
	public static final String OrderDesc = "DESC";

	// 对条目加()
	public static final IStringFilter WrapItemStrFilter = new IStringFilter() {
		public String filter(String original) {
			if (original == null) {
				return Boolean.TRUE.toString();
			}
			return ParenthesesLeft + original + ParenthesesRight;
		}
	};

	// 转义mysql 字符串值
	private static String escapeMySqlStrVal(String valStr, boolean forLike) {
		if (valStr == null) {
			return null;
		}
		//
		StringBuilder sb = new StringBuilder();
		char[] valChars = valStr.toCharArray();
		for (int i = 0; i < valChars.length; i++) {
			char xChar = valChars[i];
			switch (xChar) {
			case '\'':
				sb.append("\\'");
				break;
			case '\"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '%':
				sb.append(forLike ? "\\%" : xChar);
				break;
			case '_':
				sb.append(forLike ? "\\_" : xChar);
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\0':
				sb.append("\\0");
				break;

			default:
				sb.append(xChar);
			}
		}
		return sb.toString();
	}

	// 转义sql语句中字符串值
	public static final IStringFilter ValueStrFilter = new IStringFilter() {
		public String filter(String original) {
			return escapeMySqlStrVal(original, false);
		}
	};

	// 转义sql语句中like字符串值
	public static final IStringFilter ValueLikeStrFilter = new IStringFilter() {
		public String filter(String original) {
			return escapeMySqlStrVal(original, true);
		}
	};

	// 单引并转义字符串值
	// 'xxx'
	public static String strVal(String val) {
		return val == null ? null : "'" + ValueStrFilter.filter(val) + "'";
	}

	// 单引并转义like字符串值
	private static String toLikeStrVal(String val, LikeType likeType) {
		if (val == null) {
			return null;
		}
		String likeStr = ValueLikeStrFilter.filter(val);
		//
		if (likeType == null) {
			likeType = LikeType.Default;
		}
		if (likeType == LikeType.Center) {
			return "'%" + likeStr + "%'";
		} else if (likeType == LikeType.Left) {
			return "'" + likeStr + "%'";
		} else if (likeType == LikeType.Right) {
			return "'%" + likeStr + "'";
		} else {
			return "'%" + likeStr + "%'";
		}
	}

	// => '%xxx%'
	public static String likeStrVal(String val) {
		return toLikeStrVal(val, null);
	}

	// => 'xxx%'
	public static String leftLikeStrVal(String val) {
		return toLikeStrVal(val, LikeType.Left);
	}

	// => '%xxx'
	public static String rightLikeStrVal(String val) {
		return toLikeStrVal(val, LikeType.Right);
	}

	// 用 AND 连接字符串
	public static String AND(String... andItemStrs) throws BuildingException {
		int totalParams = andItemStrs.length;
		if (totalParams < 1) {
			throw new BuildingException("缺少参数.");
		}
		return StrUtil.join(andItemStrs, " AND ");
	}

	// 用 OR 连接字符串
	public static String OR(String... orItemStrs) throws BuildingException {
		int totalParams = orItemStrs.length;
		if (totalParams < 1) {
			throw new BuildingException("缺少参数.");
		}
		return WrapItemStrFilter.filter(StrUtil.join(orItemStrs, " OR "));
	}

	//
	protected List<String> prependList = new ArrayList<String>();
	//
	protected List<String> appendList = new ArrayList<String>();
	//
	protected String tableName;
	//
	protected boolean isFieldState = false;
	//
	protected List<String> whereList = new ArrayList<String>();

	public SqlBuilder where(String... whereStrs) {
		this.isFieldState = false;
		for (String whereStr : whereStrs) {
			whereList.add(whereStr);
		}
		return this;
	}

	protected String getWhereStr() {
		return StrUtil.join(this.whereList, "\n   AND ");
	}

	public SqlBuilder prepend(String... strs) {
		this.isFieldState = false;
		for (String str : strs) {
			this.prependList.add(str);
		}
		return this;
	}

	public SqlBuilder prependNone() {
		this.isFieldState = false;
		this.prependList.clear();
		return this;
	}

	public SqlBuilder append(String... strs) {
		this.isFieldState = false;
		for (String str : strs) {
			this.appendList.add(str);
		}
		return this;
	}

	public SqlBuilder appendNone() {
		this.isFieldState = false;
		this.appendList.clear();
		return this;
	}

	//
	public abstract String toSQL() throws Exception;
}
