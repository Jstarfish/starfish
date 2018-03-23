package priv.starfish.common.xport;

import priv.starfish.common.util.StrUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;


/**
 * 导出类
 * 
 * @author Hu Changwei
 * @date 2013-06-11
 * 
 *       <pre>
 * 典型用法步骤：
 * 1、new 
 * 2、setColumns
 * 3、prepare
 * 4、writeDataRow 或者 writeDataCell
 * 5、close
 * </pre>
 */
public abstract class Exportor extends XportBase {

	public Exportor(OutputStream outputStream) {
		super();
	}

	public Exportor(File outputFile) throws FileNotFoundException {
		super();
	}

	/**
	 * 写出列表名称行
	 * 
	 * @throws Exception
	 */
	protected abstract void writeHeaderRow() throws Exception;

	/**
	 * 执行准备工作
	 * 
	 * @throws Exception
	 */
	public abstract void prepare() throws Exception;

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
	public abstract void writeDataCell(int rowIndex, int colIndex, Object content) throws Exception;

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
	public abstract void writeDataCell(int rowIndex, String colName, Object content) throws Exception;

	/**
	 * 写出指定行的数据内容（注意行从0算起自动略过表头行）
	 * 
	 * @param rowIndex
	 *            数据行索引
	 * @param rowContents
	 *            数据行各列内容
	 * @throws Exception
	 */
	public abstract void writeDataRow(int rowIndex, Object[] rowContents) throws Exception;

	/**
	 * 写出指定行的数据内容（注意行从0算起自动略过表头行）
	 * 
	 * @param rowIndex
	 *            数据行索引
	 * @param rowContents
	 *            数据行各列的名称=>内容map
	 * @throws Exception
	 */
	public abstract void writeDataRow(int rowIndex, Map<String, Object> rowContents) throws Exception;

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
	public abstract void writeDataRow(int rowIndex, Object[] rowContents, Map<Integer, String> dataColIndex2ColNameMap) throws Exception;

	/**
	 * 结束导出（处理善后工作）
	 */
	public abstract void close();

	public static void writeResponseHeader(HttpServletResponse response, String fileName, String suffix) {
		writeResponseHeader(response, fileName, suffix, null);
	}

	/**
	 * 设置必要的HttpServletResponse头
	 * 
	 * @param response
	 * @param fileName
	 * @param suffix
	 * @param charEncoding
	 */
	public static void writeResponseHeader(HttpServletResponse response, String fileName, String suffix, String charEncoding) {
		if (!StrUtil.hasText(charEncoding)) {
			charEncoding = "UTF-8";
		}
		response.setCharacterEncoding(charEncoding);
		//
		fileName = fileName.replace(' ', '_');
		try {
			fileName = URLEncoder.encode(fileName, charEncoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setHeader("Content-disposition", "attachment;filename=" + fileName + "." + suffix);
		String mimeType = "application/x-msdownload";
		if ("csv".equalsIgnoreCase(suffix)) {
			mimeType = "text/comma-separated-values";
			response.setHeader("header", "present");
		} else if ("xls".equalsIgnoreCase(suffix)) {
			mimeType = "application/vnd.ms-excel";
		}
		response.setContentType(mimeType);
	}
}