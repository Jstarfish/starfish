package priv.starfish.common.xport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

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
public abstract class Importor extends XportBase {
	public Importor(InputStream inputStream) {
		super();
	}

	public Importor(File inputFile) throws FileNotFoundException {
		super();
	}

	/**
	 * 读取并检查导入文件中的列信息
	 * 
	 * @throws Exception
	 */
	protected abstract void readHeaderRow() throws Exception;

	/**
	 * 执行准备工作
	 * 
	 * @throws Exception
	 */
	public abstract void prepare() throws Exception;

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
	public abstract Object readDataCell(int rowIndex, int colIndex)
			throws Exception;

	/**
	 * 读取指定行、列的数据内容（注意行从0算起自动略过表头行）
	 * 
	 * @param rowIndex
	 *            数据行索引
	 *            数据列名称
	 * @return （类型转换后的）数据内容
	 * @throws Exception
	 */
	public abstract Object readDataCell(int rowIndex, String colName)
			throws Exception;

	/**
	 * 读取指定行的数据内容（注意行从0算起自动略过表头行）
	 * 
	 * @param rowIndex
	 *            数据行索引
	 * @return （类型转换后的）行数据内容
	 * @throws Exception
	 */
	public abstract Object[] readDataRow(int rowIndex) throws Exception;

	/**
	 * 读取指定行的数据内容（注意行从0算起自动略过表头行）
	 * 
	 * @param rowIndex
	 *            数据行索引
	 * @return （类型转换后的）数据行各列的名称=>内容map
	 * @throws Exception
	 */
	public abstract Map<String, Object> readDataRowAsMap(int rowIndex)
			throws Exception;

	/**
	 * 结束导出（处理善后工作）
	 */
	public abstract void close();

	public static InputStream readRequestFile(HttpServletRequest request)
			throws IOException {
		return readRequestFile(request, null);
	}

	/**
	 * 从HttpServletRequest析取文件输入流
	 * 
	 * @param request
	 * @param charEncoding
	 * @return
	 * @throws IOException
	 */
	public static InputStream readRequestFile(HttpServletRequest request,
			String charEncoding) throws IOException {
		if (charEncoding != null) {
			request.setCharacterEncoding(charEncoding);
		}
		if (ServletFileUpload.isMultipartContent(request)) {
			ServletFileUpload fileUpload = new ServletFileUpload(
					new DiskFileItemFactory());
			try {
				@SuppressWarnings("rawtypes")
				List allItems = fileUpload.parseRequest(request);
				int itemCount = allItems.size();
				FileItem theFileItem = null;
				for (int i = 0; i < itemCount; i++) {
					FileItem tmpItem = (FileItem) allItems.get(i);
					if (tmpItem.isFormField()) {
						continue;
					}
					String fileName = tmpItem.getName();
					if (!hasText(fileName)) {
						continue;
					}
					// 记录文件条目
					theFileItem = tmpItem;
					break;
				}
				if (theFileItem == null) {
					throw new IOException("没有解析到上传的文件！");
				}
				return theFileItem.getInputStream();
			} catch (FileUploadException e) {
				throw new IOException("文件解析失败：" + e.getMessage());
			}
		} else {
			throw new IOException(
					"要上传的文件所在表单的属性应为：enctype='multipart/form-data'，且 method='post' ！");
		}
	}
}
