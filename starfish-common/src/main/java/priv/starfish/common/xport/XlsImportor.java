package priv.starfish.common.xport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import priv.starfish.common.base.TypedField;


/**
 * 导入类
 * 
 * @author Hu Changwei
 * @date 2013-06-11
 * 
 *       <pre>
 * 典型用法步骤：
 * 1、new 
 * 2、setColumns
 * 3、prepare
 * 4、getTotalDataRows
 * 5、readDataRow 或者 readDataCell
 * 6、close
 * </pre>
 */
public class XlsImportor extends Importor {
	private InputStream inStream;
	private String sheetName;
	private boolean prepared = false;
	//
	private Workbook wkBook;
	private Sheet wkSheet;

	public XlsImportor(InputStream inputStream, String sheetName) {
		super(inputStream);
		//
		this.inStream = inputStream;
		this.sheetName = sheetName;
	}

	public XlsImportor(InputStream inputStream) {
		this(inputStream, null);
	}

	public XlsImportor(File inputFile, String sheetName)
			throws FileNotFoundException {
		this(new FileInputStream(inputFile), sheetName);
	}

	public XlsImportor(File inputFile) throws FileNotFoundException {
		this(inputFile, null);
	}

	/**
	 * 缓存：导入列与导入文件中对应列的索引映射关系map
	 */
	private Map<Integer, Integer> colIndex2SrcColIndex;

	private int totalDataRows = 0;

	/**
	 * 获取读取到的数据行数
	 * 
	 * @return 读取到的数据行数
	 */
	public final int getTotalDataRows() {
		return this.totalDataRows;
	}

	/**
	 * 读取并检查导入文件中的列信息
	 * 
	 * @throws Exception
	 */
	protected final void readHeaderRow() throws Exception {
		TypedField[] cols = this.getColumns();
		if (cols == null) {
			this.close();
			throw new Exception("尚未设置列信息！");
		}
		int _totalRows = this.wkSheet.getRows();
		if (_totalRows < 1) {
			this.close();
			throw new Exception("源文件中第一行缺少列信息！");
		}
		colIndex2SrcColIndex = new HashMap<Integer, Integer>();
		int dataCols = this.wkSheet.getColumns();
		for (int i = 0; i < dataCols; i++) {
			Cell cell = this.wkSheet.getCell(i, 0);
			String colName = cell.getContents();
			int colIndex = this.getColIndexByName(colName);
			if (colIndex != -1) {
				colIndex2SrcColIndex.put(colIndex, i);
			}
		}
		List<String> missingCols = new ArrayList<String>();
		System.out.println("== 目标列对应的的源列索引 ==>>");
		for (int i = 0; i < cols.length; i++) {
			TypedField col = cols[i];
			if (col.getType() == TypedField.Type.Number) {
				String subType = col.getSubType();
				if (subType == null) {
					subType = "double";
					col.setSubType(subType);
				}
				if (!TypedField.isSupportedNumberType(subType)) {
					this.close();
					throw new Exception("不支持的数值类型\"" + subType + "\"！");
				}
				String format = col.getFormat();
				if (format != null) {
					NumberFormat nf = new DecimalFormat(format);
					col.setExtra(nf);
				}
			} else if (col.getType() == TypedField.Type.Date) {
				// 设置默认日期格式
				String format = col.getFormat();
				if (format == null) {
					format = TypedField.DefaultDateFormatStr;
					col.setFormat(format);
				}
				col.setExtra(new SimpleDateFormat(format));
			} else if (col.getType() == TypedField.Type.Boolean) {
				// 设置boolean格式
				String format = col.getFormat();
				if (format == null) {
					format = TypedField.DefaultBoolFormatStr;
					col.setFormat(format);
				}
				col.setExtra(TypedField.parseBooleanPair(format));
			}
			//
			Integer srcIndex = colIndex2SrcColIndex.get(i);
			if (srcIndex == null || srcIndex.intValue() < 0) {
				missingCols.add(col.getName());
				System.out.println(col.getName() + " => ?");
			} else {
				System.out.println(col.getName() + " => " + srcIndex);
			}
		}
		System.out.println("==========================<<");
		if (missingCols.size() > 0) {
			StringBuffer sb = new StringBuffer();
			sb.append("源文件中找不到以下列：");
			for (int i = 0; i < missingCols.size(); i++) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(missingCols.get(i));
			}
			this.close();
			throw new Exception(sb.toString());
		}
		//
		this.totalDataRows = _totalRows - 1;
	}

	/**
	 * 执行准备工作
	 * 
	 * @throws Exception
	 */
	public final void prepare() throws Exception {
		if (prepared) {
			this.close();
			throw new Exception("prepare()方法只能调用一次！");
		}
		this.wkBook = Workbook.getWorkbook(this.inStream);
		if (this.sheetName != null) {
			this.wkSheet = this.wkBook.getSheet(this.sheetName);
		} else {
			this.wkSheet = this.wkBook.getSheet(0);
		}
		this.inStream = null;
		this.sheetName = null;
		//
		prepared = true;
		//
		this.readHeaderRow();
	}

	/**
	 * 读取指定行、列的数据内容（注意行从0算起自动略过表头行）
	 * 
	 * @param rowIndex
	 *            数据行索引
	 * @param colIndex
	 *            数据列索引
	 * @return （类型转换后的）数据内容
	 * @throws Exception
	 */
	public final Object readDataCell(int rowIndex, int colIndex)
			throws Exception {
		if (rowIndex < 0 || rowIndex >= this.getTotalDataRows()) {
			this.close();
			throw new Exception("数据行索引越界 [0 ~ " + this.getTotalDataRows()
					+ "]！");
		}
		// 忽略标题行
		rowIndex = rowIndex + 1;
		int srcColIndex = this.colIndex2SrcColIndex.get(colIndex);
		Cell cell = this.wkSheet.getCell(srcColIndex, rowIndex);
		String content = cell.getContents();
		//
		TypedField col = this.getColumns()[colIndex];
		TypedField.Type colType = col.getType();
		if (colType == TypedField.Type.Number) {
			if (hasText(content) && !isNullStr(content)) {
				Number value = Double.valueOf(content);
				String format = col.getFormat();
				if (format != null) {
					NumberFormat nf = (NumberFormat) col.getExtra();
					value = nf.parse(content);
				}
				String subType = col.getSubType();
				if (subType.equalsIgnoreCase("int")) {
					return value.intValue();
				} else if (subType.equalsIgnoreCase("long")) {
					return value.longValue();
				} else if (subType.equalsIgnoreCase("double")) {
					return value.doubleValue();
				} else if (subType.equalsIgnoreCase("float")) {
					return value.floatValue();
				} else if (subType.equalsIgnoreCase("short")) {
					return value.shortValue();
				} else if (subType.equalsIgnoreCase("byte")) {
					return value.byteValue();
				} else {
					return value.doubleValue();
				}
			} else {
				return null;
			}
		} else if (colType == TypedField.Type.Date) {
			if (hasText(content) && !isNullStr(content)) {
				SimpleDateFormat df = (SimpleDateFormat) col.getExtra();
				return df.parse(content);
			} else {
				return null;
			}
		} else if (colType == TypedField.Type.Boolean) {
			if (hasText(content) && !isNullStr(content)) {
				String[] bf = (String[]) col.getExtra();
				return TypedField.decodeBoolean(bf, content);
			} else {
				return null;
			}
		} else {
			String nullVal = (String) col.getNullAs();
			if (nullVal != null && nullVal.equals(content)) {
				return null;
			} else {
				return content;
			}
		}
	}

	/**
	 * 读取指定行、列的数据内容（注意行从0算起自动略过表头行）
	 * 
	 * @param rowIndex
	 *            数据行索引
	 *            数据列名称
	 * @return （类型转换后的）数据内容
	 * @throws Exception
	 */
	public final Object readDataCell(int rowIndex, String colName)
			throws Exception {
		int colIndex = this.getColIndexByName(colName);
		return this.readDataCell(rowIndex, colIndex);
	}

	/**
	 * 读取指定行的数据内容（注意行从0算起自动略过表头行）
	 * 
	 * @param rowIndex
	 *            数据行索引
	 * @return （类型转换后的）行数据内容
	 * @throws Exception
	 */
	public final Object[] readDataRow(int rowIndex) throws Exception {
		if (rowIndex < 0 || rowIndex >= this.getTotalDataRows()) {
			this.close();
			throw new Exception("数据行索引越界 [0 ~ " + this.getTotalDataRows()
					+ "]！");
		}
		// 忽略标题行
		rowIndex = rowIndex + 1;
		//
		TypedField[] cols = this.getColumns();
		int totalCols = cols.length;
		Object[] rowContents = new Object[totalCols];
		for (int colIndex = 0; colIndex < totalCols; colIndex++) {
			int srcColIndex = this.colIndex2SrcColIndex.get(colIndex);
			Cell cell = this.wkSheet.getCell(srcColIndex, rowIndex);
			String content = cell.getContents();
			//
			TypedField col = cols[colIndex];
			TypedField.Type colType = col.getType();
			if (colType == TypedField.Type.Number) {
				if (hasText(content) && !isNullStr(content)) {
					Number value = Double.valueOf(content);
					String format = col.getFormat();
					if (format != null) {
						NumberFormat nf = (NumberFormat) col.getExtra();
						value = nf.parse(content);
					}
					String subType = col.getSubType();
					if (subType.equalsIgnoreCase("int")) {
						rowContents[colIndex] = value.intValue();
					} else if (subType.equalsIgnoreCase("long")) {
						rowContents[colIndex] = value.longValue();
					} else if (subType.equalsIgnoreCase("double")) {
						rowContents[colIndex] = value.doubleValue();
					} else if (subType.equalsIgnoreCase("float")) {
						rowContents[colIndex] = value.floatValue();
					} else if (subType.equalsIgnoreCase("short")) {
						rowContents[colIndex] = value.shortValue();
					} else if (subType.equalsIgnoreCase("byte")) {
						rowContents[colIndex] = value.byteValue();
					} else {
						rowContents[colIndex] = value.doubleValue();
					}
				}
			} else if (colType == TypedField.Type.Date) {
				if (hasText(content) && !isNullStr(content)) {
					SimpleDateFormat df = (SimpleDateFormat) col.getExtra();
					rowContents[colIndex] = df.parse(content);
				}
			} else if (colType == TypedField.Type.Boolean) {
				if (hasText(content) && !isNullStr(content)) {
					String[] bf = (String[]) col.getExtra();
					rowContents[colIndex] = TypedField.decodeBoolean(bf, content);
				}
			} else {
				String nullVal = (String) col.getNullAs();
				if (nullVal != null && nullVal.equals(content)) {
					//
				} else {
					rowContents[colIndex] = content;
				}
			}
		}
		return rowContents;
	}

	/**
	 * 读取指定行的数据内容（注意行从0算起自动略过表头行）
	 * 
	 * @param rowIndex
	 *            数据行索引
	 * @return （类型转换后的）数据行各列的名称=>内容map
	 * @throws Exception
	 */
	public final Map<String, Object> readDataRowAsMap(int rowIndex)
			throws Exception {
		Object[] rowContents = this.readDataRow(rowIndex);
		Map<String, Object> retMap = new HashMap<String, Object>();
		TypedField[] cols = this.getColumns();
		int totalCols = cols.length;
		for (int i = 0; i < totalCols; i++) {
			retMap.put(cols[i].getName(), rowContents[i]);
		}
		return retMap;
	}

	private boolean closed = false;

	/**
	 * 结束导出（处理善后工作）
	 */
	public final void close() {
		if (closed) {
			return;
		}
		try {
			if (prepared) {
				this.wkBook.close();
				this.wkSheet = null;
				this.wkBook = null;
			} else {
				this.inStream = null;
				this.sheetName = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closed = true;
		}
	}

	public static List<Map<String, Object>> readDataFrom(
			HttpServletRequest request, String dataName, TypedField[] columns)
			throws Exception {
		InputStream inputStream = readRequestFile(request);
		return readDataFrom(inputStream, dataName, columns);
	}

	public static List<Map<String, Object>> readDataFrom(
			InputStream inputStream, String dataName, TypedField[] columns)
			throws Exception {
		Importor importor = new XlsImportor(inputStream, dataName);
		importor.setColumns(columns);
		importor.prepare();
		int totalRows = importor.getTotalDataRows();
		List<Map<String, Object>> dataRows = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < totalRows; i++) {
			Map<String, Object> dataRow = importor.readDataRowAsMap(i);
			dataRows.add(dataRow);
		}
		importor.close();
		return dataRows;
	}
}
