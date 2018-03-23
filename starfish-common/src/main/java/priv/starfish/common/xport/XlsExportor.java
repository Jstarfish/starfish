package priv.starfish.common.xport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import priv.starfish.common.base.TypedField;


/**
 * 
 * @author Hu Changwei
 * @date 2013-06-11
 * 
 *       <pre>
 * 功能：导出Xls文件流
 * </pre>
 */
public class XlsExportor extends Exportor {
	private OutputStream outStream;
	private String sheetName;
	private boolean prepared = false;
	//
	private WritableWorkbook wkBook;
	private WritableSheet wkSheet;

	public XlsExportor(OutputStream outputStream, String sheetName) {
		super(outputStream);
		//
		this.outStream = outputStream;
		this.sheetName = sheetName;
	}

	public XlsExportor(File outputFile, String sheetName)
			throws FileNotFoundException {
		this(new FileOutputStream(outputFile), sheetName);
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

	private String headerFontName = "Tahoma";
	private int headerFontSize = 10;
	private int headerRowHeight = 420;
	//
	private String bodyFontName = "Tahoma";
	private int bodyFontSize = 10;
	private int bodyRowHeight = 380;
	//
	private WritableFont headerFont = null;
	private WritableFont bodyFont = null;

	/**
	 * 写出列表名称行
	 * 
	 * @throws Exception
	 */
	protected final void writeHeaderRow() throws Exception {
		TypedField[] cols = this.getColumns();
		if (cols == null) {
			this.close();
			throw new Exception("尚未设置列信息！");
		}
		//
		for (int i = 0; i < cols.length; i++) {
			TypedField col = cols[i];
			if (col.getWidthInChars() >= 0) {
				this.wkSheet.setColumnView(i, col.getWidthInChars() + 2);
			}
			if (col.getType() == TypedField.Type.Date) {
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
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setFont(this.headerFont);
			wcf.setWrap(false);
			wcf.setAlignment(alignFromStr("center"));
			wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
			Label cell = new Label(i, 0, colName, wcf);
			this.wkSheet.addCell(cell);
		}
		//
		this.wkSheet.setRowView(0, this.headerRowHeight);
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
		this.wkBook = Workbook.createWorkbook(this.outStream);
		this.wkSheet = this.wkBook.createSheet(this.sheetName, 0);
		SheetSettings settings = this.wkSheet.getSettings();
		settings.setDefaultRowHeight(this.bodyRowHeight);
		settings.setVerticalFreeze(1);
		this.outStream = null;
		this.sheetName = null;
		//
		this.headerFont = new WritableFont(
				WritableFont.createFont(this.headerFontName),
				this.headerFontSize, WritableFont.BOLD);
		this.bodyFont = new WritableFont(
				WritableFont.createFont(this.bodyFontName), this.bodyFontSize);
		//
		prepared = true;
		//
		this.writeHeaderRow();
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
		//
		TypedField col = this.getColumns()[colIndex];
		TypedField.Type colType = col.getType();
		if (content == null && colType == TypedField.Type.String) {
			content = col.getNullAs();
		}
		if (content == null) {
			return;
		}
		Alignment colAlign = alignFromStr(col.getAlign());
		String format = col.getFormat();
		if (colType == TypedField.Type.Number) {
			WritableCellFormat wcf = null;
			if (format != null) {
				jxl.write.NumberFormat nf = new jxl.write.NumberFormat(format);
				wcf = new jxl.write.WritableCellFormat(nf);
			} else {
				wcf = new WritableCellFormat();
			}
			wcf.setFont(this.bodyFont);
			wcf.setWrap(col.isWrap());
			wcf.setAlignment(colAlign);
			wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
			Number cell = new Number(colIndex, rowIndex, Double.valueOf(content
					.toString()), wcf);
			this.wkSheet.addCell(cell);
		} else if (colType == TypedField.Type.Date) {
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setFont(this.bodyFont);
			wcf.setWrap(col.isWrap());
			wcf.setAlignment(colAlign);
			wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
			SimpleDateFormat df = (SimpleDateFormat) col.getExtra();
			Label cell = new Label(colIndex, rowIndex,
					df.format((Date) content), wcf);
			this.wkSheet.addCell(cell);
		} else if (colType == TypedField.Type.Boolean) {
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setFont(this.bodyFont);
			wcf.setWrap(col.isWrap());
			wcf.setAlignment(colAlign);
			wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
			String[] bf = (String[]) col.getExtra();
			Label cell = new Label(colIndex, rowIndex, TypedField.encodeBoolean(bf,
					(Boolean) content), wcf);
			this.wkSheet.addCell(cell);
		} else { // (colType == Column.Type.String) {
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setFont(this.bodyFont);
			wcf.setWrap(col.isWrap());
			wcf.setAlignment(colAlign);
			wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
			Label cell = new Label(colIndex, rowIndex, content.toString(), wcf);
			this.wkSheet.addCell(cell);
		}
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
		//
		if (rowContents == null) {
			return;
		}
		int dataCols = rowContents.length;
		//
		TypedField[] cols = this.getColumns();
		int totalCols = cols.length;
		for (int colIndex = 0; colIndex < totalCols; colIndex++) {
			if (colIndex >= dataCols) {
				break;
			}
			Object content = rowContents[colIndex];
			TypedField col = cols[colIndex];
			TypedField.Type colType = col.getType();
			if (content == null && colType == TypedField.Type.String) {
				content = col.getNullAs();
			}
			if (content == null) {
				continue;
			}
			Alignment colAlign = alignFromStr(col.getAlign());
			String format = col.getFormat();
			if (colType == TypedField.Type.Number) {
				WritableCellFormat wcf = null;
				if (format != null) {
					jxl.write.NumberFormat nf = new jxl.write.NumberFormat(
							format);
					wcf = new jxl.write.WritableCellFormat(nf);
				} else {
					wcf = new WritableCellFormat();
				}
				wcf.setFont(this.bodyFont);
				wcf.setWrap(col.isWrap());
				wcf.setAlignment(colAlign);
				wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
				Number cell = new Number(colIndex, rowIndex,
						Double.valueOf(content.toString()), wcf);
				this.wkSheet.addCell(cell);
			} else if (colType == TypedField.Type.Date) {
				WritableCellFormat wcf = new WritableCellFormat();
				wcf.setFont(this.bodyFont);
				wcf.setWrap(col.isWrap());
				wcf.setAlignment(colAlign);
				wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
				SimpleDateFormat df = (SimpleDateFormat) col.getExtra();
				Label cell = new Label(colIndex, rowIndex,
						df.format((Date) content), wcf);
				this.wkSheet.addCell(cell);
			} else if (colType == TypedField.Type.Boolean) {
				WritableCellFormat wcf = new WritableCellFormat();
				wcf.setFont(this.bodyFont);
				wcf.setWrap(col.isWrap());
				wcf.setAlignment(colAlign);
				wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
				String[] bf = (String[]) col.getExtra();
				Label cell = new Label(colIndex, rowIndex,
						TypedField.encodeBoolean(bf, (Boolean) content), wcf);
				this.wkSheet.addCell(cell);
			} else { // (colType == Column.Type.String) {
				WritableCellFormat wcf = new WritableCellFormat();
				wcf.setFont(this.bodyFont);
				wcf.setWrap(col.isWrap());
				wcf.setAlignment(colAlign);
				wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
				Label cell = new Label(colIndex, rowIndex, content.toString(),
						wcf);
				this.wkSheet.addCell(cell);
			}
		}
		//
		this.wkSheet.setRowView(rowIndex, this.bodyRowHeight);
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
		if (rowContents == null) {
			return;
		}
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
		if (rowContents == null) {
			return;
		}
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
				this.headerFont = null;
				this.bodyFont = null;
				//
				this.wkBook.write();
				this.wkBook.close();
				this.wkSheet = null;
				this.wkBook = null;
			} else {
				this.outStream = null;
				this.sheetName = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closed = true;
		}
	}

	public static void writeDataTo(HttpServletResponse response,
			String fileName, String dataName, TypedField[] columns,
			List<Map<String, Object>> dataRows) throws IOException, Exception {
		writeResponseHeader(response, fileName, "xls");
		writeDataTo(response.getOutputStream(), dataName, columns, dataRows);
		response.flushBuffer();
	}

	public static void writeDataTo(OutputStream outputStream, String dataName,
			TypedField[] columns, List<Map<String, Object>> dataRows)
			throws Exception {
		Exportor exporter = new XlsExportor(outputStream, dataName);
		exporter.setColumns(columns);
		exporter.prepare();
		int totalRows = dataRows.size();
		for (int i = 0; i < totalRows; i++) {
			exporter.writeDataRow(i, dataRows.get(i));
		}
		exporter.close();
	}
}
