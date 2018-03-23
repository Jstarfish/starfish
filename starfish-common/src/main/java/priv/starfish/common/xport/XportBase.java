package priv.starfish.common.xport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.format.Alignment;

import priv.starfish.common.base.TypedField;
import priv.starfish.common.helper.FileHelper;

/**
 * 导入导出基类
 * 
 * @author Hu Changwei
 * @date 2013-06-11
 */
public abstract class XportBase {
	public final static String DEF_FILE_CHARSET = FileHelper.DEF_FILE_CHARSET;
	protected String fileCharSet = DEF_FILE_CHARSET;

	public void setFileCharSet(String fileCharSet) {
		this.fileCharSet = fileCharSet;
	}

	private static final String EMPTY_STR = "";
	private static final String NULL_STR = "null";

	protected static boolean hasText(String chkStr) {
		return chkStr != null && !chkStr.trim().equals(EMPTY_STR);
	}

	protected static boolean isNullStr(String chkStr) {
		return NULL_STR.equals(chkStr);
	}

	/**
	 * 字符串 =》 alignment 枚举值
	 * 
	 * @param align
	 * @return
	 */
	protected static Alignment alignFromStr(String align) {
		if (!hasText(align)) {
			return Alignment.LEFT;
		}
		if (align.toLowerCase().startsWith("r")) {
			return Alignment.RIGHT;
		} else if (align.toLowerCase().startsWith("c")) {
			return Alignment.CENTRE;
		} else {
			return Alignment.LEFT;
		}
	}

	/**
	 * 名称是否-大小写不敏感
	 */
	protected boolean caseInsensitive = true;

	public XportBase(boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

	public XportBase() {
		this(true);
	}

	protected TypedField[] columns;

	/**
	 * 设置导入导出列信息
	 * 
	 * @param columns
	 */
	public final void setColumns(List<TypedField> columns) {
		this.columns = (TypedField[]) columns.toArray(new TypedField[columns.size()]);
	}

	/**
	 * 设置导入导出列信息
	 * 
	 * @param columns
	 */
	public final void setColumns(TypedField[] columns) {
		this.columns = columns;
	}

	public final TypedField[] getColumns() {
		return this.columns;
	}

	protected Map<String, Integer> cachedColIndexes = new HashMap<String, Integer>();

	public final int getColIndexByName(String colName) {
		if (this.caseInsensitive) {
			colName = colName.toLowerCase();
			if (!cachedColIndexes.containsKey(colName)) {
				boolean found = false;
				TypedField[] cols = this.getColumns();
				for (int i = 0; i < cols.length; i++) {
					String tmpColName = cols[i].getName();
					if (tmpColName.equalsIgnoreCase(colName)) {
						cachedColIndexes.put(colName, Integer.valueOf(i));
						found = true;
						break;
					}
				}
				if (!found) {
					cachedColIndexes.put(colName, Integer.valueOf(-1));
				}
			}
		} else {
			if (!cachedColIndexes.containsKey(colName)) {
				boolean found = false;
				TypedField[] cols = this.getColumns();
				for (int i = 0; i < cols.length; i++) {
					String tmpColName = cols[i].getName();
					if (tmpColName.equals(colName)) {
						cachedColIndexes.put(colName, Integer.valueOf(i));
						found = true;
						break;
					}
				}
				if (!found) {
					cachedColIndexes.put(colName, Integer.valueOf(-1));
				}
			}
		}
		return cachedColIndexes.get(colName).intValue();
	}

	/**
	 * 返回数据行数
	 * 
	 * @return
	 */
	public abstract int getTotalDataRows();
}
