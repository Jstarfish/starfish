package priv.starfish.common.xload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import priv.starfish.common.helper.FileHelper;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.util.FileSize;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.common.util.WebUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 文件上传接收析取、并发送给ObservableFileReceiver对象<br/>
 * 注意：要求一个ObservableFileReceiver对象能够逐个处理一次上传的所有文件（或者忽略）<br/>
 * 
 * @author Hu Changwei
 * 
 */
public class FileUploader {
	// 单例对象
	private static final FileUploader instance = new FileUploader();

	public static FileUploader getInstance() {
		return instance;
	}

	public static List<CommonsMultipartFile> extractMultipartFiles(HttpServletRequest request) {
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultiValueMap<String, MultipartFile> multiFileMap = multipartRequest.getMultiFileMap();
			List<CommonsMultipartFile> multiFileList = new ArrayList<CommonsMultipartFile>();
			for (String fileParamName : multiFileMap.keySet()) {
				List<MultipartFile> tmpFiles = multiFileMap.get(fileParamName);
				for (MultipartFile tmpFile : tmpFiles) {
					CommonsMultipartFile multiFile = (CommonsMultipartFile) tmpFile;
					if (multiFile != null) {
						multiFileList.add(multiFile);
					}
				}
			}
			return multiFileList;
		} else {
			return null;
		}
	}

	// 文件上传配置信息
	private UploadConfig uploadConfig = new UploadConfig();
	private DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
	// 缓存变量
	private int maxUploadFileSizeMB = 0;
	private long maxUploadFileSize = 0;
	//
	private int maxUploadTotalSizeMB = 0;
	private long maxUploadTotalSize = 0;

	// 文件接收者管理器
	private FileReceiverMgr fileReceiverMgr = null;

	//
	public void setUploadConfig(UploadConfig uploadConfig) {
		this.uploadConfig = uploadConfig;
		//
		File tmpDir = this.uploadConfig.getTmpFileDir();
		if (tmpDir != null) {
			this.fileItemFactory.setRepository(tmpDir);
		}
		//
		int sizeKB = this.uploadConfig.getTotalSizeLimit();
		this.maxUploadTotalSizeMB = sizeKB / FileSize.K;
		this.maxUploadTotalSize = sizeKB * FileSize.K;
		//
		sizeKB = this.uploadConfig.getFileSizeLimit();
		this.maxUploadFileSizeMB = sizeKB / FileSize.K;
		this.maxUploadFileSize = sizeKB * FileSize.K;
		//
		sizeKB = this.uploadConfig.getToDiskThreshold();
		this.fileItemFactory.setSizeThreshold(sizeKB * FileSize.K);
	}

	public UploadConfig getUploadConfig() {
		return this.uploadConfig;
	}

	//
	public void setFileReceiverMgr(FileReceiverMgr fileReceiverMgr) {
		this.fileReceiverMgr = fileReceiverMgr;
	}

	public FileReceiverMgr getFileReceiverMgr() {
		return this.fileReceiverMgr;
	}

	//
	public void upload(HttpServletRequest request, HttpServletResponse response) {
		//
		Result<Map<String, Object>> returnInfo = Result.newOne();
		//
		if (!"POST".equalsIgnoreCase(request.getMethod())) {
			returnInfo.type = Type.warn;
			returnInfo.message = "文件上传只能通过POST方法！";
		} else {
			// 判断enctype属性是否为multipart/form-data
			if (ServletFileUpload.isMultipartContent(request)) {
				List<CommonsMultipartFile> multiFileList = extractMultipartFiles(request);
				if (!TypeUtil.isNullOrEmpty(multiFileList)) {
					// 通过org.springframework.web.multipart.commons.CommonsMultipartResolver拦截的
					returnInfo = uploadAndReturn(TypeUtil.listToArray(multiFileList, CommonsMultipartFile.class), request, response);
				} else {
					// 普通或未被spring 拦截
					Map<String, Object> resultData = new LinkedHashMap<String, Object>();
					returnInfo.data = resultData;
					// 提取参数配置
					HashMap<String, Object> paramsMap = new LinkedHashMap<String, Object>();
					Enumeration<String> paramNames = request.getParameterNames();
					while (paramNames.hasMoreElements()) {
						String paramName = paramNames.nextElement();
						paramsMap.put(paramName, request.getParameter(paramName));
					}
					resultData.put("params", paramsMap);
					//
					List<FileResult> fileResultList = new ArrayList<FileResult>();
					resultData.put("files", fileResultList);
					int fileCount = 0;
					//
					ServletFileUpload fileUpload = new ServletFileUpload(this.fileItemFactory);
					fileUpload.setSizeMax(this.maxUploadTotalSize);
					fileUpload.setFileSizeMax(this.maxUploadFileSize);
					//
					FileReceiver fileReceiver = this.fileReceiverMgr.pick(paramsMap);
					try {
						long totalFileSize = 0;
						List<FileItem> allItems = fileUpload.parseRequest(request);
						int itemCount = allItems.size();
						List<FileItem> fileItems = new ArrayList<FileItem>();
						for (int i = 0; i < itemCount; i++) {
							FileItem tmpItem = (FileItem) allItems.get(i);
							if (tmpItem.isFormField()) {
								continue;
							}
							String fileName = tmpItem.getName();
							if (StrUtil.isNullOrBlank(fileName.trim())) {
								continue;
							}
							// 记录文件条目
							fileItems.add(tmpItem);
							//
							totalFileSize += tmpItem.getSize();
						}
						//
						fileCount = fileItems.size();
						resultData.put("fileCount", fileCount);
						resultData.put("totalFileSize", totalFileSize);
						resultData.put("totalFileSizeStr", FileSize.toSizeStr(totalFileSize));
						//
						int failedFileCount = 0;
						for (int i = 0; i < fileCount; i++) {
							FileItem tmpItem = fileItems.get(i);
							String fieldName = tmpItem.getFieldName();
							long fileSize = tmpItem.getSize();
							String fileSizeStr = FileSize.toSizeStr(fileSize);
							String originalFileName = tmpItem.getName();
							String fileName = FileHelper.replaceFileNameBlanks(originalFileName);
							fileName = FileHelper.extractShortFileName(fileName);
							Map<String, Object> infoMap = new LinkedHashMap<String, Object>(paramsMap);
							infoMap.put("fieldName", fieldName);
							infoMap.put("originalFileName", originalFileName);
							infoMap.put("fileName", fileName);
							infoMap.put("fileSize", fileSize);
							infoMap.put("fileSizeStr", fileSizeStr);
							//
							InputStream stream = tmpItem.getInputStream();
							// ==========================================================>>
							FileResult fileResult = fileReceiver.receiveFile(infoMap, stream);
							// ==========================================================<<
							tmpItem.delete();
							//
							if (fileResult == null) {
								fileResult = new FileResult();
								fileResult.setOriginalFileName(originalFileName);
								fileResult.setFileName(fileName);
								fileResult.setFileSize(fileSize);
								fileResult.setExtra(infoMap);
								fileResult.type = Type.error;
								fileResult.message = "文件接收者[" + fileReceiver.getClass().getName() + "]没有给出任何信息！";
							}
							fileResultList.add(fileResult);
							//
							if (!fileResult.type.equals(Type.info)) {
								failedFileCount++;
							}
						}
						if (fileCount == 0) {
							returnInfo.type = Type.warn;
							returnInfo.message = "没有检测到要上传的文件 ！";
						} else {
							returnInfo.message = "文件上传结束，共" + fileCount + "个文件";
							if (failedFileCount > 0) {
								returnInfo.type = Type.warn;
								returnInfo.message += "(" + failedFileCount + "个文件处理失败)";
							}
						}
					} catch (FileUploadException e) {
						// e.printStackTrace();
						String rawMsg = e.getMessage();
						System.out.println("FileUploadException : " + rawMsg);
						String expClassName = e.getClass().getName();
						String chsMsg = expClassName;
						if (expClassName.indexOf("FileSizeLimit") != -1) {
							if (fileCount > 1) {
								chsMsg = "单个文件大小不能超过" + this.maxUploadFileSizeMB + "M";
							} else {
								chsMsg = "文件大小不能超过" + this.maxUploadFileSizeMB + "M";
							}
						} else if (expClassName.indexOf("SizeLimit") != -1) {
							if (fileCount > 1) {
								chsMsg = "单个文件大小不能超过" + this.maxUploadFileSizeMB + "M，";
								chsMsg += "单次上传文件总大小不能超过" + this.maxUploadTotalSizeMB + "M";
							} else {
								chsMsg = "文件大小不能超过" + this.maxUploadFileSizeMB + "M";
							}
						}
						//
						returnInfo.type = Type.warn;
						returnInfo.message = "文件上传失败 （" + chsMsg + "）！";
					} catch (Exception e) {
						String rawMsg = e.getMessage();
						System.out.println(e.getClass().getName() + ": " + rawMsg);
						returnInfo.type = Type.warn;
						returnInfo.message = "文件上传失败！";
					}
				}
			} else {
				returnInfo.type = Type.warn;
				returnInfo.message = "要上传的文件所在表单的属性应为：enctype='multipart/form-data'，且 method='post' ！";
			}
		}
		//
		try {
			WebUtil.responseAsJson(response, returnInfo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//
	public Result<Map<String, Object>> uploadAndReturn(CommonsMultipartFile[] multiFiles, HttpServletRequest request, HttpServletResponse response) {
		//
		Result<Map<String, Object>> returnInfo = Result.newOne();
		//
		Map<String, Object> resultData = new LinkedHashMap<String, Object>();
		returnInfo.data = resultData;
		// 提取参数配置
		HashMap<String, Object> paramsMap = new LinkedHashMap<String, Object>();
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			paramsMap.put(paramName, request.getParameter(paramName));
		}
		resultData.put("params", paramsMap);
		//
		List<FileResult> fileResultList = new ArrayList<FileResult>();
		resultData.put("files", fileResultList);
		int fileCount = multiFiles == null ? 0 : multiFiles.length;
		if (fileCount < 1) {
			returnInfo.type = Type.warn;
			returnInfo.message = "没有检测到要上传的文件 ！";
		} else {
			FileReceiver fileReceiver = this.fileReceiverMgr.pick(paramsMap);
			try {
				long totalFileSize = 0;
				int itemCount = fileCount;
				List<FileItem> fileItems = new ArrayList<FileItem>();
				for (int i = 0; i < itemCount; i++) {
					CommonsMultipartFile multipartFile = multiFiles[i];
					FileItem tmpItem = multipartFile.getFileItem();
					if (tmpItem.isFormField()) {
						continue;
					}
					String fileName = tmpItem.getName();
					if (StrUtil.isNullOrBlank(fileName.trim())) {
						continue;
					}
					// 记录文件条目
					fileItems.add(tmpItem);
					//
					totalFileSize += tmpItem.getSize();
				}
				//
				fileCount = fileItems.size();
				resultData.put("fileCount", fileCount);
				resultData.put("totalFileSize", totalFileSize);
				resultData.put("totalFileSizeStr", FileSize.toSizeStr(totalFileSize));
				//
				int failedFileCount = 0;
				for (int i = 0; i < fileCount; i++) {
					FileItem tmpItem = fileItems.get(i);
					String fieldName = tmpItem.getFieldName();
					long fileSize = tmpItem.getSize();
					String fileSizeStr = FileSize.toSizeStr(fileSize);
					String originalFileName = tmpItem.getName();
					String fileName = FileHelper.replaceFileNameBlanks(originalFileName);
					fileName = FileHelper.extractShortFileName(fileName);
					Map<String, Object> infoMap = new LinkedHashMap<String, Object>(paramsMap);
					infoMap.put("fieldName", fieldName);
					infoMap.put("originalFileName", originalFileName);
					infoMap.put("fileName", fileName);
					infoMap.put("fileSize", fileSize);
					infoMap.put("fileSizeStr", fileSizeStr);
					//
					InputStream stream = tmpItem.getInputStream();
					// ==========================================================>>
					FileResult fileResult = fileReceiver.receiveFile(infoMap, stream);
					// ==========================================================<<
					tmpItem.delete();
					//
					if (fileResult == null) {
						fileResult = new FileResult();
						fileResult.setOriginalFileName(originalFileName);
						fileResult.setFileName(fileName);
						fileResult.setFileSize(fileSize);
						fileResult.setExtra(infoMap);
						fileResult.type = Type.error;
						fileResult.message = "文件接收者[" + fileReceiver.getClass().getName() + "]没有给出任何信息！";
					}
					fileResultList.add(fileResult);
					//
					if (!fileResult.type.equals(Type.info)) {
						failedFileCount++;
					}
				}
				if (fileCount == 0) {
					returnInfo.type = Type.warn;
					returnInfo.message = "没有检测到要上传的文件 ！";
				} else {
					returnInfo.message = "文件上传结束，共" + fileCount + "个文件";
					if (failedFileCount > 0) {
						returnInfo.type = Type.warn;
						returnInfo.message += "(" + failedFileCount + "个文件处理失败)";
					}
				}
			} catch (Exception e) {
				String rawMsg = e.getMessage();
				System.out.println(e.getClass().getName() + ": " + rawMsg);
				returnInfo.type = Type.warn;
				returnInfo.message = "文件上传失败！";
			}
		}
		//
		return returnInfo;
	}

}
