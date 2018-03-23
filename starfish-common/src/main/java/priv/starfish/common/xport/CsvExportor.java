package priv.starfish.common.xport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import priv.starfish.common.base.TypedField;


/**
 * 
 * @author Hu Changwei
 * @date 2013-06-11
 * 
 *       <pre>
 * 功能：导出csv文件流
 * </pre>
 */
public class CsvExportor extends Exportor {
	private OutputStream outStream;
	private boolean prepared = false;
	// 是否使用数据列表头
	private boolean useHeader = true;
	//
	private CSVPrinter csvWriter;

	public CsvExportor(OutputStream outputStream) {
		this(outputStream, true);
	}

	public CsvExportor(OutputStream outputStream, boolean useHeader) {
		super(outputStream);
		//
		this.outStream = outputStream;
		this.useHeader = useHeader;
	}

	public CsvExportor(File outputFile) throws FileNotFoundException {
		this(outputFile, true);
	}

	public CsvExportor(File outputFile, boolean useHeader)
			throws FileNotFoundException {
		this(new FileOutputStream(outputFile), useHeader);
	}

	private int totalDataRows = 0;

	/**
	 * 获取读取到的数据行数
	 * 
	 * @return 读取到的数据行数
	 */
	public final int getTotalDataRows() {
		return this.totalDataRows;
	}

	// 行数据缓冲区
	private Object[] lineBuffer = null;
	// 行是否需要输出
	private boolean lineNeedFlush = false;

	private void clearLineBuffer() {
		if (this.lineBuffer == null) {
			this.lineBuffer = new Object[this.getColumns().length];
		} else {
			for (int i = 0; i < this.getColumns().length; i++) {
				this.lineBuffer[i] = null;
			}
		}
		lineNeedFlush = false;
	}

	private void copyIntoLineBuffer(Object[] rowContents) {
		for (int i = 0; i < this.getColumns().length; i++) {
			this.lineBuffer[i] = rowContents[i];
		}
	}

	private void checkLineState(int rowIndex) throws Exception {
		if (rowIndex < this.totalDataRows) {
			this.close();
			throw new Exception("csv数据输出不支持向后写！");
		} else if (rowIndex - this.totalDataRows > 1) {
			this.close();
			throw new Exception("csv数据输出不支持跳行写！");
		}
	}

	/**
	 * 写出列表名称行
	 * 
	 * @throws Exception
	 */
	protected final void writeHeaderRow() throws Exception {
		TypedField[] cols = this.getColumns();
		for (int i = 0; i < cols.length; i++) {
			TypedField col = cols[i];
			if (col.getType() == TypedField.Type.Number) {
				// 设置数值格式
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
			String colName = col.getName();
			if (this.useHeader) {
				this.csvWriter.print(colName);
			}
		}
		if (this.useHeader) {
			this.csvWriter.println();
		}
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
		PrintStream outStreamWriter = new PrintStream(this.outStream, true,
				this.fileCharSet);
		this.csvWriter = new CSVPrinter(outStreamWriter, CSVFormat.RFC4180);
		this.outStream = null;
		//
		clearLineBuffer();
		//
		prepared = true;
		//
		this.writeHeaderRow();
	}

	private void writeDataRowInternal(final Object[] rowContents)
			throws IOException {
		TypedField[] cols = this.getColumns();
		int totalCols = cols.length;
		for (int colIndex = 0; colIndex < totalCols; colIndex++) {
			Object content = rowContents[colIndex];
			TypedField col = cols[colIndex];
			TypedField.Type colType = col.getType();
			if (content == null && colType == TypedField.Type.String) {
				content = col.getNullAs();
			}
			String strVal = "";
			if (content != null) {
				String format = col.getFormat();
				if (colType == TypedField.Type.Number) {
					if (format != null) {
						NumberFormat nf = (NumberFormat) col.getExtra();
						strVal = nf.format(content);
					} else {
						strVal = content.toString();
					}
				} else if (colType == TypedField.Type.Date) {
					SimpleDateFormat df = (SimpleDateFormat) col.getExtra();
					strVal = df.format((Date) content);
				} else if (colType == TypedField.Type.Boolean) {
					String[] bf = (String[]) col.getExtra();
					strVal = TypedField.encodeBoolean(bf, (Boolean) content);
				} else { // (colType == Column.Type.String) {
					strVal = content.toString();
				}
			}
			this.csvWriter.print(strVal);
		}
		this.csvWriter.println();
		//
		this.clearLineBuffer();
	}

	/**
	 * 写出指定行、列的数据（注意行从0算起自动略过表头行）
	 * 
	 * @param rowIndex
	 *            数据行索引
	 * @param colIndex
	 *            数据列索引
	 * @param content
	 *            数据内容
	 * @throws Exception
	 */
	public final void writeDataCell(int rowIndex, int colIndex, Object content)
			throws Exception {
		if (rowIndex < 0) {
			this.close();
			throw new Exception("数据行索引应该从0开始！");
		}
		// 忽略标题行
		rowIndex = rowIndex + 1;
		this.checkLineState(rowIndex);
		//
		if (rowIndex > this.totalDataRows) {
			if (this.lineNeedFlush) {
				writeDataRowInternal(this.lineBuffer);
			}
		}
		//
		this.lineBuffer[colIndex] = content;
		this.lineNeedFlush = true;
		//
		if (rowIndex > this.totalDataRows) {
			this.totalDataRows = rowIndex;
		}
	}

	/**
	 * 
	 * 写出指定行、列的数据（注意行从0算起自动略过表头行）
	 * 
	 * @param rowIndex
	 *            数据行索引
	 * @param colName
	 *            数据列名称
	 * @param content
	 *            数据内容
	 * @throws Exception
	 */
	public final void writeDataCell(int rowIndex, String colName, Object content)
			throws Exception {
		int colIndex = this.getColIndexByName(colName);
		this.writeDataCell(rowIndex, colIndex, content);
	}

	/**
	 * 写出指定行的数据内容（注意行从0算起自动略过表头行）
	 * 
	 * @param rowIndex
	 *            数据行索引
	 * @param rowContents
	 *            数据行各列内容
	 * @throws Exception
	 */
	public final void writeDataRow(int rowIndex, Object[] rowContents)
			throws Exception {
		if (rowIndex < 0) {
			this.close();
			throw new Exception("数据行索引应该从0开始！");
		}
		// 忽略标题行
		rowIndex = rowIndex + 1;
		this.checkLineState(rowIndex);
		//
		if (rowIndex > this.totalDataRows) {
			if (this.lineNeedFlush) {
				writeDataRowInternal(this.lineBuffer);
			}
		}
		//
		this.copyIntoLineBuffer(rowContents);
		this.lineNeedFlush = true;
		//
		if (rowIndex > this.totalDataRows) {
			this.totalDataRows = rowIndex;
		}
	}

	/**
	 * 写出指定行的数据内容（注意行从0算起自动略过表头行）
	 * 
	 * @param rowIndex
	 *            数据行索引
	 * @param rowContents
	 *            数据行各列的名称=>内容map
	 * @throws Exception
	 */
	public final void writeDataRow(int rowIndex, Map<String, Object> rowContents)
			throws Exception {
		TypedField[] cols = this.getColumns();
		int totalCols = cols.length;
		Object[] rowData = new Object[totalCols];
		for (int i = 0; i < totalCols; i++) {
			TypedField col = cols[i];
			String colName = col.getName();
			Object content = rowContents.get(colName);
			rowData[i] = content;
		}
		this.writeDataRow(rowIndex, rowData);
	}

	/**
	 * 
	 * 写出指定行的数据内容（注意行从0算起自动略过表头行）
	 * 
	 * @param rowIndex
	 *            数据行索引
	 * @param rowContents
	 *            数据行各列内容
	 * @param dataColIndex2ColNameMap
	 *            rowContents中的索引位置与写出列名称对应map
	 * @throws Exception
	 */
	public final void writeDataRow(int rowIndex, Object[] rowContents,
			Map<Integer, String> dataColIndex2ColNameMap) throws Exception {
		int dataCols = rowContents.length;
		int totalCols = this.getColumns().length;
		Object[] rowData = new Object[totalCols];
		for (int i = 0; i < totalCols; i++) {
			if (i >= dataCols) {
				break;
			}
			String colName = dataColIndex2ColNameMap.get(i);
			if (colName != null) {
				int index = this.getColIndexByName(colName);
				rowData[index] = rowContents[i];
			}
		}
		this.writeDataRow(rowIndex, rowData);
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
				if (this.lineNeedFlush) {
					writeDataRowInternal(this.lineBuffer);
				}
				this.lineBuffer = null;
				this.csvWriter.flush();
				this.csvWriter.close();
			} else {
				this.outStream = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closed = true;
		}
	}

	public static void writeDataTo(HttpServletResponse response,
			String fileName, TypedField[] columns,
			List<Map<String, Object>> dataRows) throws IOException, Exception {
		writeResponseHeader(response, fileName, "csv");
		writeDataTo(response.getOutputStream(), columns, dataRows);
		response.flushBuffer();
	}

	public static void writeDataTo(OutputStream outputStream, TypedField[] columns,
			List<Map<String, Object>> dataRows) throws Exception {
		Exportor exporter = new CsvExportor(outputStream);
		exporter.setColumns(columns);
		exporter.prepare();
		int totalRows = dataRows.size();
		for (int i = 0; i < totalRows; i++) {
			exporter.writeDataRow(i, dataRows.get(i));
		}
		exporter.close();
	}
}
