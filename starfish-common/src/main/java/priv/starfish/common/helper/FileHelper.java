package priv.starfish.common.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.OSUtil;
import priv.starfish.common.util.StrUtil;

/**
 * 
 * @author Hu Changwei
 * @date 2013-12-12
 * 
 */
public class FileHelper {
	private static final Log logger = LogFactory.getLog(FileHelper.class);
	//
	public final static String ENCODING_UTF8 = "UTF-8";
	// 默认文件字符编码
	public final static String DEF_FILE_CHARSET = OSUtil.getFileEncoding();
	// public final static String DEF_FILE_CHARSET = "gb2312";
	public static final String FILE_SEPERATOR = OSUtil.getFileSeparator();

	// ===============================================================================
	public static String makeFullFileName(String fileDir, String fileName) {
		fileDir = StrUtil.replaceAllSlashes(fileDir, FILE_SEPERATOR);
		fileName = StrUtil.replaceAllSlashes(fileName, FILE_SEPERATOR);
		if (!fileDir.endsWith(FILE_SEPERATOR)) {
			fileDir = fileDir + FILE_SEPERATOR;
		}
		if (fileName.startsWith(FILE_SEPERATOR)) {
			fileName = fileName.substring(1);
		}
		return fileDir + fileName;
	}

	public static String makeFullUrl(String baseUrl, String subUrl) {
		baseUrl = StrUtil.replaceAllBackSlashes(baseUrl, StrUtil.SlashStr);
		subUrl = StrUtil.replaceAllBackSlashes(subUrl, StrUtil.SlashStr);
		if (!baseUrl.endsWith(StrUtil.SlashStr)) {
			baseUrl = baseUrl + StrUtil.SlashStr;
		}
		if (subUrl.startsWith(StrUtil.SlashStr)) {
			subUrl = subUrl.substring(1);
		}
		return baseUrl + subUrl;
	}

	private static Pattern blankRegPattern = Pattern.compile("\\s");

	public static String replaceFileNameBlanks(String fileName, String replacement) {
		if (replacement == null) {
			replacement = "_";
		}
		return blankRegPattern.matcher(fileName).replaceAll(replacement);
	}

	public static String replaceFileNameBlanks(String fileName) {
		return replaceFileNameBlanks(fileName, null);
	}

	public static String getShortFileName(String filePath) {
		return new File(filePath).getName();
	}

	public static String extractShortFileName(String filePath) {
		if (filePath == null) {
			// Should never happen.
			return "";
		}
		// check for Unix-style path
		int pos = filePath.lastIndexOf("/");
		if (pos == -1) {
			// check for Windows-style path
			pos = filePath.lastIndexOf("\\");
		}
		if (pos != -1) {
			// any sort of path separator found
			return filePath.substring(pos + 1);
		} else {
			// plain name
			return filePath;
		}
	}

	public static String extractFileNameExt(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		return dotIndex == -1 ? StrUtil.EmptyStr : fileName.substring(dotIndex);
	}

	// ===============================================================================
	public static BufferedReader getFileBufferedReader(String filePath) {
		return getFileBufferedReader(filePath, null);
	}

	public static BufferedReader getFileBufferedReader(String filePath, String charSet) {
		return getFileBufferedReader(new File(filePath), charSet);
	}

	/**
	 * 返回一个针对给定的文本文件的BufferedReader（注意：不要忘了用完后及时关闭reader）
	 * 
	 * @param file
	 * @param charSet
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	public static BufferedReader getFileBufferedReader(File file, String charSet) {
		if (charSet == null) {
			charSet = DEF_FILE_CHARSET;
		}
		try {
			return new BufferedReader(new InputStreamReader(new FileInputStream(file), charSet));
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	public static BufferedReader getResourceAsBufferedReader(String resUrl) {
		return getResourceAsBufferedReader(resUrl, null, null);
	}

	public static BufferedReader getResourceAsBufferedReader(String resUrl, String charSet) {
		return getResourceAsBufferedReader(resUrl, charSet, null);
	}

	public static BufferedReader getResourceAsBufferedReader(String resUrl, Class<?> refClass) {
		return getResourceAsBufferedReader(resUrl, null, refClass);
	}

	public static BufferedReader getResourceAsBufferedReader(String resUrl, String charSet, Class<?> refClass) {
		if (refClass == null) {
			refClass = FileHelper.class;
		}
		InputStream input = null;
		if ((input = refClass.getResourceAsStream(resUrl)) == null) {
			ClassLoader clsLoader = refClass.getClassLoader();
			while (clsLoader != null && (input = clsLoader.getResourceAsStream(resUrl)) == null) {
				clsLoader = clsLoader.getParent();
			}
			if (clsLoader == null) {
				return null;
			}
		}
		if (charSet == null) {
			charSet = DEF_FILE_CHARSET;
		}
		try {
			return new BufferedReader(new InputStreamReader(input, charSet));
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
			return null;
		}
	}

	public static InputStream getResourceAsInputStream(String resUrl, Class<?> refClass) {
		if (refClass == null) {
			refClass = FileHelper.class;
		}
		InputStream input = null;
		if ((input = refClass.getResourceAsStream(resUrl)) == null) {
			ClassLoader clsLoader = refClass.getClassLoader();
			while (clsLoader != null && (input = clsLoader.getResourceAsStream(resUrl)) == null) {
				clsLoader = clsLoader.getParent();
			}
			if (clsLoader == null) {
				return null;
			}
		}
		return input;
	}

	public static InputStream getResourceAsInputStream(String resUrl) {
		return getResourceAsInputStream(resUrl, null);
	}

	public static BufferedReader getBufferedReaderFor(InputStream input, String charSet) {
		if (charSet == null) {
			charSet = DEF_FILE_CHARSET;
		}
		try {
			return new BufferedReader(new InputStreamReader(input, charSet));
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
			return null;
		}
	}

	public static BufferedReader getBufferedReaderFor(InputStream input) {
		return getBufferedReaderFor(input, null);
	}

	public static String readBufferedReaderAsString(BufferedReader bufferedReader) {
		try {
			StringBuffer sb = new StringBuffer();
			String lineStr = null;
			boolean first = true;
			while ((lineStr = bufferedReader.readLine()) != null) {
				if (first) {
					first = false;
				} else {
					sb.append(OSUtil.getLineSeparator());
				}
				sb.append(lineStr);
			}
			bufferedReader.close();
			return sb.toString();
		} catch (IOException e) {
			logger.error(e);
			return null;
		}
	}

	public static String readFileAsString(String filePath) {
		BufferedReader fileReader = getFileBufferedReader(filePath);
		return readBufferedReaderAsString(fileReader);
	}

	public static String readResourceAsAsString(String resUrl) {
		BufferedReader resReader = getResourceAsBufferedReader(resUrl);
		return readBufferedReaderAsString(resReader);
	}

	public static long getFileTotalLines(BufferedReader reader, boolean closeReaderAfterUsage) {
		long totalLines = 0;
		try {
			if (reader.ready()) {
				while (reader.readLine() != null) {
					totalLines++;
				}
			}
		} catch (IOException e) {
			logger.error(e);
		} finally {
			if (closeReaderAfterUsage && reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		return totalLines;
	}

	// ===============================================================================
	public static BufferedWriter getFileBufferedWriter(String filePath) {
		return getFileBufferedWriter(filePath, false);
	}

	public static BufferedWriter getFileBufferedWriter(String filePath, boolean appendMode) {
		return getFileBufferedWriter(new File(filePath), null, appendMode);
	}

	public static BufferedWriter getFileBufferedWriter(String filePath, String charSet) {
		return getFileBufferedWriter(filePath, charSet, false);
	}

	public static BufferedWriter getFileBufferedWriter(String filePath, String charSet, boolean appendMode) {
		return getFileBufferedWriter(new File(filePath), charSet, appendMode);
	}

	/**
	 * 返回一个针对给定的文本文件的BufferedWriter（注意：不要忘了用完后及时关闭reader）
	 * 
	 * @param file
	 * @param charSet
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	public static BufferedWriter getFileBufferedWriter(File file, String charSet, boolean appendMode) {
		if (charSet == null) {
			charSet = DEF_FILE_CHARSET;
		}
		try {
			return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, appendMode), charSet));
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	// ======================================= string <-> csv
	// ========================================

	/**
	 * 解析（符合 CSVFormat.RFC4180）标准 的csv记录行数据
	 * 
	 * @param lineStr
	 * @return
	 */
	public static List<String> parseCsvLine(String lineStr) {
		List<String> results = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(lineStr, ",", true);
		if (tokenizer.countTokens() > 0) {
			StringBuffer sb = new StringBuffer();
			int quoteCount = 0;
			boolean lastIsSepChar = false;
			while (tokenizer.hasMoreTokens()) {
				String tocken = tokenizer.nextToken();
				// System.out.println(tocken);
				int tockenLen = tocken.length();
				if (tockenLen == 1 && tocken.charAt(0) == ',' && quoteCount % 2 == 0) {
					int fieldLen = sb.length();
					String field = null;
					if (fieldLen > 1 && sb.charAt(0) == '\"' && sb.charAt(fieldLen - 1) == '\"') {
						field = sb.subSequence(1, fieldLen - 1).toString();
					} else {
						field = sb.toString();
					}
					if (field.indexOf('\"') > -1) {
						field = field.replaceAll("\"\"", "\"");
					}
					results.add(field);
					sb = new StringBuffer();
					lastIsSepChar = true;
				} else {
					for (int i = 0; i < tockenLen; i++) {
						char ch = tocken.charAt(i);
						if (ch == '\"') {
							quoteCount++;
						}
					}
					sb.append(tocken);
					lastIsSepChar = false;
				}
			}
			if (lastIsSepChar) {
				results.add("");
			} else {
				int fieldLen = sb.length();
				String field = null;
				if (fieldLen > 1 && sb.charAt(0) == '\"' && sb.charAt(fieldLen - 1) == '\"') {
					field = sb.subSequence(1, fieldLen - 1).toString();
				} else {
					field = sb.toString();
				}
				if (field.indexOf('\"') > -1) {
					field = field.replaceAll("\"\"", "\"");
				}
				results.add(field);
			}
		}
		return results;
	}

	/**
	 * 根据给出的字段列表，生成（符合 CSVFormat.RFC4180）标准 的csv记录字符串
	 * 
	 * @param fieldList
	 * @return
	 */
	public static String makeCsvLine(final List<String> fieldList) {
		return makeCsvLine(fieldList.toArray(new String[fieldList.size()]));
	}

	/**
	 * 根据给出的字段数组，生成（符合 CSVFormat.RFC4180）标准 的csv记录字符串
	 * 
	 * @param fields
	 * @return
	 */
	public static String makeCsvLine(final String... fields) {
		StringBuffer sb = new StringBuffer();
		int fieldCount = fields.length;
		for (int i = 0; i < fieldCount; i++) {
			if (i > 0) {
				sb.append(',');
			}
			String field = fields[i];
			if (field == null) {
				sb.append(String.valueOf(null));
			} else {
				StringBuffer sb2 = new StringBuffer();
				boolean needQuoted = false;
				int chCount = field.length();
				for (int j = 0; j < chCount; j++) {
					char ch = field.charAt(j);
					sb2.append(ch);
					if (ch == '\r' || ch == '\n') {
						needQuoted = true;
					} else if (ch == ',') {
						needQuoted = true;
					} else if (ch == '"') {
						needQuoted = true;
						sb2.append('"');
					}
				}
				if (needQuoted) {
					sb.append('"').append(sb2).append('"');
				} else {
					sb.append(sb2);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param objects
	 * @return
	 */
	public static String makeCsvLine(final Object... objects) {
		int objCount = objects.length;
		String[] objStrs = new String[objCount];
		for (int i = 0; i < objCount; i++) {
			objStrs[i] = StrUtil.toString(objects[i]);
		}
		return makeCsvLine(objStrs);
	}

	// ===================================== json Array <-> string

	public static Object[] parseJsonArrayLine(String lineStr) {
		return JsonUtil.fromJson(lineStr, Object[].class);
	}

	public static String makeJsonArrayLine(final Object... objects) {
		return JsonUtil.toJson(objects);
	}

	public static String makeJsonArrayLine(final List<Object> objectList) {
		return makeJsonArrayLine(objectList.toArray(new Object[objectList.size()]));
	}

	// =====================================
	public static void makeDirsForFile(String filePath) {
		makeDirsForFile(new File(filePath));
	}

	public static void makeDirsForFile(File file) {
		File dir = new File(file.getParent());
		makeDirs(dir);
	}

	public static void makeDirs(String fileDir) {
		makeDirs(new File(fileDir));
	}

	public static void makeDirs(File fileDir) {
		if (!fileDir.exists() || !fileDir.isDirectory()) {
			fileDir.mkdirs();
			logger.info("目录已创建：" + fileDir.getAbsolutePath());
		}
	}

	public static void deleteFile(String file) {
		deleteFile(new File(file));
	}

	public static void deleteFile(File file) {
		if (file != null && file.exists()) {
			file.delete();
			logger.info("文件已删除：" + file.getAbsolutePath());
		}
	}

	//
	private static MimetypesFileTypeMap urlFileNameMap = new MimetypesFileTypeMap();

	public static String getContentType(String fileName) {
		return urlFileNameMap.getContentType(fileName);
	}

	public static String getContentType(File file) {
		return urlFileNameMap.getContentType(file);
	}
}
